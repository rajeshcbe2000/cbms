/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingHomeDAO.java
 *
 * Created on Thu May 20 15:53:06 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.closing;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.deposit.closing.PartialWithdrawalCheckingRule;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
//import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.deposit.closing.DepositWithDrawalTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
//import java.lang.Math ;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
//import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO ;
//import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.DateUtil;
//import com.see.truetransact.commonutil.interestcalc.CommonCalculateInterest;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.DepositIntTask;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.operativeaccount.AccountClosingDAO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.*;

/**
 * DepositClosingDAO
 *
 * @author pinky
 * @modified Sunil
 * @modified Sathiya
 */
public class DepositClosingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ArrayList listTOs;
    private TransactionDAO transactionDAO = null;
    private InterestBatchTO interestBatchTO = null;
    DepositWithDrawalTO objTO;
    private AccountClosingTO accountClosingTO;
    private AccountClosingDAO accountClosingDAO;
    private RemittanceIssueDAO remittanceIssueDAO;
    private RemittanceIssueTO remittanceIssueTO;
    private String prodId = null;
    public List chargeLst = null;
    private HashMap returnMap = new HashMap();
    Date curDate = null;
//    private String generateSingleTransIdMultiClosing = "";
    /**
     * Creates a new instance of DepositClosingDAO
     */
    public DepositClosingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map, boolean enableTrans) throws Exception {
        System.out.println("getData :" + map);
        boolean prematureCummulative = false;
        HashMap returnMap = new HashMap();
        String depositNo = (String) map.get("DEPOSITNO");
        int depositSubNo =  CommonUtil.convertObjToInt(map.get("DEPOSITSUBNO"));
        String compositeDepositNo = depositNo + "_" + depositSubNo;
        List list = (List) sqlMap.executeQueryForList("getDepositDetail", depositNo);
        //        System.out.println("getData List : " + list);
        returnMap.put("DEPOSIT_DETAILS", list);
        //        System.out.println("getData returnMap : "+returnMap);
        HashMap closeMap = new HashMap();
        if (list != null && list.size() > 0) {
            HashMap dataMap = (HashMap) list.get(0);
            closeMap.put("CATEGORY_ID", dataMap.get("CATEGORY"));

        }
        closeMap.put("DEPOSITNO", depositNo);
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
      //  Date date1 = sdf.parse(CommonUtil.convertObjToStr(map.get("DEPOSIT_DT")));
        closeMap.put("DEPOSIT_DT",  map.get("DEPOSIT_DT"));
        closeMap.put("CUSTID", returnMap.get("CUST_ID"));
        //        System.out.println("########CloseMap : "+closeMap);
        String sbRate ="";
        String sbProd = "";
        String editDepositNo = CommonUtil.convertObjToStr(map.get("DEPOSITNO"));
        if (editDepositNo != null && editDepositNo.length() > 0) {
            String batchID = "";
            HashMap editMap = new HashMap();
            editMap.put("ACT_NUM", editDepositNo);
            List lst = sqlMap.executeQueryForList("getDepositClosingAccounts", editMap);
            if (lst != null && lst.size() > 0) {
                editMap = (HashMap) lst.get(0);
                //                System.out.println("########editMap : "+editMap);
                HashMap getTransMap = new HashMap();
                HashMap acHeadMap = new HashMap();
                HashMap acHeads = new HashMap();
                acHeadMap.put("PROD_ID", map.get("PRODUCT_ID"));
                String prodId = CommonUtil.convertObjToStr(acHeadMap.get("PROD_ID"));
                //                System.out.println("########getData acHeads : "+acHeads);
                double depCloseAmt = CommonUtil.convertObjToDouble(editMap.get("PAYABLE_BAL")).doubleValue();
                double intAmt = CommonUtil.convertObjToDouble(editMap.get("CHRG_DETAILS")).doubleValue();
                double intPayAmt = CommonUtil.convertObjToDouble(editMap.get("INT_PAYABLE")).doubleValue();
                double tdsAmt = CommonUtil.convertObjToDouble(editMap.get("ACT_CLOSING_CHRG")).doubleValue();
                double delayAmt = CommonUtil.convertObjToDouble(editMap.get("UNUSED_CHK")).doubleValue();//this column is storing for RD delayed installment amounts.....noly
                HashMap transDetMap = new HashMap();
                returnMap.put("TRANS_DETAILS", transDetMap);
                HashMap payOrderMap = new HashMap();
                payOrderMap.put("REMARKS", editDepositNo + "_1");
                lst = (List) sqlMap.executeQueryForList("getSelectDepositPayOrder", payOrderMap);
                if (lst != null && lst.size() > 0) {
                    payOrderMap = (HashMap) lst.get(0);
                    getTransMap.put("LINK_BATCH_ID", payOrderMap.get("VARIABLE_NO"));
                } else {
                    getTransMap.put("LINK_BATCH_ID", editDepositNo + "_1");
                }
                getTransMap.put("TODAY_DT", curDate);
                getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                if (depCloseAmt > 0) {
                    getTransMap.put("AMOUNT", new Double(CommonUtil.convertObjToStr(editMap.get("PAYABLE_BAL"))));
                    //                    System.out.println("######## depCloseAmt :"+getTransMap);
                    lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER", lst.get(0));
                        //                        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+returnMap);
                    }
                    lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_AMT_DETAILS_CASH", lst.get(0));
                        //                        System.out.println("########getCashTransBatchID returnMap depCloseAmt:"+returnMap);
                    }
                    lst = null;
                }
                getTransMap.put("LINK_BATCH_ID", editDepositNo + "_1");
                if (intAmt > 0) {
                    getTransMap.put("AMOUNT", new Double(CommonUtil.convertObjToStr(editMap.get("CHRG_DETAILS"))));
                    //                    System.out.println("######## intAmt :"+getTransMap);
                    lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        batchID = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BATCH_ID"));
                        returnMap.put("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER", lst.get(0));
                        //                        System.out.println("########getTransferTransBatchID intAmt:"+returnMap);
                    }
                    lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        //                        System.out.println("########getCashTransBatchID lst intAmt:"+lst);
                        batchID = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BATCH_ID"));
                        returnMap.put("DEPOSIT_CLOSING_INT_DETAILS_CASH", lst.get(0));
                        //                        System.out.println("########getCashTransBatchID intAmt:"+returnMap);
                    }
                    lst = null;
                }
                if (intPayAmt > 0) {
                    getTransMap.put("AMOUNT", new Double(CommonUtil.convertObjToStr(editMap.get("INT_PAYABLE"))));
                    //                    System.out.println("########batchID : "+batchID);
                    //                    System.out.println("######## intPayAmt:"+getTransMap);
                    if (intAmt == intPayAmt) {
                        if (!batchID.equals("") && batchID != null && batchID.length() > 0) {
                            getTransMap.put("BATCH_ID", batchID);
                        }
                    }
                    lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER", lst.get(0));
                        //                        System.out.println("########getTransferTransBatchIDintPayAmt: "+returnMap);
                    }
                    lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH", lst.get(0));
                        //                        System.out.println("########getCashTransBatchID returnMap intPayAmt: "+returnMap);
                    }
                    batchID = "";
                    lst = null;
                }
                if (tdsAmt > 0) {
                    getTransMap.put("AMOUNT", new Double(CommonUtil.convertObjToStr(editMap.get("ACT_CLOSING_CHRG"))));
                    //                    System.out.println("########batchID : "+batchID);
                    //                    System.out.println("######## tdsAmt:"+getTransMap);
                    lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER", lst.get(0));
                        //                        System.out.println("########getTransferTransBatchID tdsAmt: "+returnMap);
                    }
                    lst = null;
                }
                if (delayAmt > 0) {
                    getTransMap.put("AMOUNT", new Double(CommonUtil.convertObjToStr(editMap.get("UNUSED_CHK"))));
                    //                    System.out.println("########batchID : "+batchID);
                    //                    System.out.println("######## delayAmt:"+getTransMap);
                    lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER", lst.get(0));
                        //                        System.out.println("########getTransferTransBatchID returnMap delayAmt:"+returnMap);
                    }
                    lst = null;
                }
                getTransMap = null;
                acHeadMap = null;
                acHeads = null;
                transDetMap = null;
                payOrderMap = null;
                //                System.out.println("########getData final editMap returnMap : "+returnMap);
            }
            editMap = null;
        }
        if (map.containsKey("CUSTID") && map.containsKey(CommonConstants.PRODUCT_ID)) {
            closeMap.put("CUSTID", map.get("CUSTID"));
            closeMap.put("PRODID", map.get(CommonConstants.PRODUCT_ID));
        } else {
            closeMap.put("CUSTID", "");
            closeMap.put("PRODID", "");
        }
        if (map.containsKey("CLOSING_TYPE") && CommonUtil.convertObjToStr(map.get("CLOSING_TYPE")).equals("PREMATURE")
                && CommonUtil.convertObjToStr(closeMap.get("PRODID")).length() > 0) {

            List lst = (List) sqlMap.executeQueryForList("getChangedProductId", closeMap);
            if (lst != null && lst.size() > 0) {
                HashMap resultMap = (HashMap) lst.get(0);
                if (CommonUtil.convertObjToStr(resultMap.get("PREMATURE_CLOSURE_APPLY")).equals("Deposit Rate") &&
                        CommonUtil.convertObjToStr(resultMap.get("FIXED_DEPOSIT_PRODUCT")).length() > 0) {
                    closeMap.put("PRODID", resultMap.get("FIXED_DEPOSIT_PRODUCT"));
                    prematureCummulative = true;
                }else if (CommonUtil.convertObjToStr(resultMap.get("PREMATURE_CLOSURE_APPLY")).equals("SB Rate") &&
                        CommonUtil.convertObjToStr(resultMap.get("FIXED_DEPOSIT_PRODUCT")).length() > 0){
                    sbProd = CommonUtil.convertObjToStr(resultMap.get("FIXED_DEPOSIT_PRODUCT"));
                    sbRate = CommonUtil.convertObjToStr(resultMap.get("PREMATURE_CLOSURE_APPLY"));
                    prematureCummulative = true;
                }
            }

        }
        
        Date endDt = null;
        if (map.containsKey("FLOATING_RATE") && map.get("FLOATING_RATE") != null) {
            if (map.containsKey("FLOATING_TYPE") && CommonUtil.convertObjToStr(map.get("FLOATING_TYPE")).equals("WITH_PERIOD")) {
                //                for without period
                System.out.println("#$%#$%#$%weudf:" + map);
                returnMap.put("FLOATING_TYPE", "WITH_PERIOD");
                returnMap.put("DEPOSIT_CLOSE_DETAILS_FLOATING", map);

            } else if (map.containsKey("FLOATING_TYPE") && CommonUtil.convertObjToStr(map.get("FLOATING_TYPE")).equals("WITHOUT_PERIOD")) {
                //               for with period
                System.out.println("#$%#$%#$%weusfsdff:" + map);
                List lst = (List) sqlMap.executeQueryForList("getRoiDtFrFloatingWithoutPeriod", map);
                HashMap floatingRateMap = new HashMap();
                floatingRateMap.put("DEPOSIT_CLOSE_DETAILS_WITHOUT_PERIOD", lst);
                floatingRateMap.put("DEPOSIT_CLOSE_DETAILS_MAP", map);
                floatingRateMap.put("FLOATING_TYPE", "WITHOUT_PERIOD");
                returnMap.put("DEPOSIT_CLOSE_DETAILS_FLOATING", floatingRateMap);
            }
        } else {
            if (map.containsKey("DEPOSIT_DT") && !map.get("DEPOSIT_DT").equals("")
                    && map.containsKey("MATURITY_DT") && !map.get("MATURITY_DT").equals("")) {
                if (map.containsKey("CLOSING_TYPE") && map.get("CLOSING_TYPE").equals("PREMATURE")) {
                    endDt = curDate;
                } else {
                    endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                }
                Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("DEPOSIT_DT")));
                long period = DateUtil.dateDiff(startDt, endDt);
                long count = 0;
                if (map.containsKey("CLOSING_TYPE") && map.get("CLOSING_TYPE").equals("PREMATURE")) {
//                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
//                        int month = startDt.getMonth();
//                        int startYear = startDt.getYear() + 1900;
//                        if (month == 1 && startYear % 4 == 0) {
//                            count++;
//                        }
//                        startDt = DateUtil.addDays(startDt, 30);
//                    }
//                    period -= count;
                    //added by jithin 8413
                    System.out.println("closeMAp" + closeMap);
                    if (closeMap.containsKey("CATEGORY_ID") && closeMap.get("CATEGORY_ID").equals("SENIOR_CITIZENS")) {
                        List newList = sqlMap.executeQueryForList("getPrematureRateDetails", closeMap);
                        if (newList != null && newList.size() > 0) {
                            HashMap senMap = (HashMap) newList.get(0);
                            if (senMap.get("NORMAL_RATE_FOR_SENIOR_CITIZEN").equals("Y")) {
                                int periodUpto = 0;
                                if (senMap.get("MONTHS_UPTO_NORMAL_RATE") != null) {
                                    periodUpto = CommonUtil.convertObjToInt(senMap.get("MONTHS_UPTO_NORMAL_RATE"));
                                }
                                periodUpto *= 30;
                                System.out.println("period" + period + "periodUpto" + periodUpto);
                                if ((int) period < periodUpto) {
                                    closeMap.put("CATEGORY_ID", "GENERAL_CATEGORY");
                                    prematureCummulative = true;
                                }

                            }
                            senMap = null;
                            newList = null;
                        }
                    }
                }
                closeMap.put("PERIOD", new Long(period));
                List lst = null;
				//Added by chithra on 23-04-14
                List addDetList=null;
                if (prematureCummulative) {  // added by abi
                    closeMap.put("PRODUCT_TYPE", "TD");
                    closeMap.put("PROD_ID", closeMap.get("PRODID"));
//                     closeMap.put("CATEGORY_ID",map.get("CATEGORY_ID"));
                    if(sbProd!=null && sbRate.equalsIgnoreCase("SB Rate")){
                        closeMap.put("PROD_ID", sbProd); 
                        closeMap.put("PRODUCT_TYPE", "OA");
                    }
                    closeMap.put("AMOUNT", CommonUtil.convertObjToDouble(map.get("AMOUNT")));

                    System.out.println("closeMap###" + closeMap);

                    lst = (List) sqlMap.executeQueryForList("icm.getInterestRates", closeMap);
                } else {
                    lst = (List) sqlMap.executeQueryForList("getDepositClosingDetails", closeMap);
                    //Added by Chithra on 23-04-14
                    HashMap whereAddDet = new HashMap();
                    whereAddDet.put("PRODID", closeMap.get("PRODID"));
                    addDetList = (List) sqlMap.executeQueryForList("getAdditionalInterestConditions", whereAddDet);
                    String renewalOpt = "";
                    String matuOpt = "";
                    String notRenewMatu = "";
                    String sbrateProdId = "";// added by chithra 17-05-14
                    String closureYn = "";
                    String deathMarkedYn = "";
                    HashMap addIntdet = new HashMap();
                    String SBorDep = "";
                    if (addDetList != null && addDetList.size() > 0) {
                        addIntdet = (HashMap) addDetList.get(0);
                        renewalOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_RENEWAL"));
                        matuOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_MATURITY"));
                        notRenewMatu = CommonUtil.convertObjToStr(addIntdet.get("ELIGIBLE_TWO_RATE"));
                        SBorDep = CommonUtil.convertObjToStr(addIntdet.get("INT_RATE_APPLIED_OVERDUE"));
                        sbrateProdId = CommonUtil.convertObjToStr(addIntdet.get("SBRATE_PRODID"));// added by chithra 17-05-14
                        closureYn = CommonUtil.convertObjToStr(addIntdet.get("CLOSURE_INT_YN"));
                        deathMarkedYn = CommonUtil.convertObjToStr(addIntdet.get("DEATHMARKED_INT_YN"));
                     }
                     
                     HashMap whereAddIntdet=new HashMap();
                     List addInterest=null;
                     List addMatInterest=null;
                    List deathList=null;
                     if(closureYn!=null&&closureYn.equalsIgnoreCase("Y"))
                     {
                      if(SBorDep!=null&&SBorDep.trim().length()>0&&SBorDep.equalsIgnoreCase("Y"))    {// added by chithra 17-05-14 
                          long diff = 0;
                          addInterest=null;
                          Date strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                          Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURRENT_DT")));
                          diff = DateUtil.dateDiff(strMatDt, currDt);
                          HashMap sbInterestMap = new HashMap();
                          sbInterestMap.put("PRODUCT_TYPE", "OA");
                          sbInterestMap.put("PROD_ID", sbrateProdId);
                          sbInterestMap.put("CATEGORY_ID", closeMap.get("CATEGORY_ID"));
                          sbInterestMap.put("DEPOSIT_DT", map.get("CURRENT_DT"));
                          sbInterestMap.put("AMOUNT", CommonUtil.convertObjToDouble(map.get("AMOUNT")));
                          sbInterestMap.put("PERIOD", new Double(diff));
                          System.out.println("########lstInt : " + sbInterestMap);
                            if (deathMarkedYn != null && deathMarkedYn.trim().length() > 0 && deathMarkedYn.equalsIgnoreCase("N")) {
                          addInterest = (List) sqlMap.executeQueryForList("icm.getInterestRates", sbInterestMap);
                            } else {
                                HashMap whereMap = new HashMap();
                                whereMap.put("CUSTID", CommonUtil.convertObjToStr(map.get("CUSTID")));
                                deathList = (List) sqlMap.executeQueryForList("getDeathMarkingForCust", whereMap);
                                if (deathList != null && deathList.size() > 0) {
                                    addInterest = (List) sqlMap.executeQueryForList("icm.getInterestRates", sbInterestMap);
                                } else {
                                    addInterest = null;
                                }

                            }
                        } else if (SBorDep != null && SBorDep.trim().length() > 0 && SBorDep.equalsIgnoreCase("N")) {
                     if (renewalOpt != null && renewalOpt.length() > 0 && renewalOpt.equalsIgnoreCase("Y")) {
                        long diff = 0;
                        addInterest=null;
                        Date strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                        Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURRENT_DT")));
                        diff = DateUtil.dateDiff(strMatDt, currDt);
                        whereAddIntdet.put("DEPOSIT_DT", map.get("CURRENT_DT"));
                        whereAddIntdet.put("PRODID", closeMap.get("PRODID"));
                        whereAddIntdet.put("CUSTID", closeMap.get("CUSTID"));
                        whereAddIntdet.put("PERIOD", diff);
                        whereAddIntdet.put("CATEGORY_ID", closeMap.get("CATEGORY_ID"));
                        whereAddIntdet.put("DEPOSITNO", closeMap.get("DEPOSITNO"));
                        addInterest = (List) sqlMap.executeQueryForList("getDepositClosingDetails", whereAddIntdet);
                    }
                      if (matuOpt != null && matuOpt.length() > 0) {
                        long diff = 0;
                        whereAddIntdet=new HashMap();
                        Date strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                        Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURRENT_DT")));
                        diff = DateUtil.dateDiff(strMatDt, currDt);
                        whereAddIntdet.put("DEPOSIT_DT", map.get("MATURITY_DT"));
                        whereAddIntdet.put("PRODID",CommonUtil.convertObjToStr(closeMap.get("PRODID")));
                        whereAddIntdet.put("CUSTID", CommonUtil.convertObjToStr(closeMap.get("CUSTID")));
                        whereAddIntdet.put("PERIOD", diff);
                        whereAddIntdet.put("CATEGORY_ID", CommonUtil.convertObjToStr(closeMap.get("CATEGORY_ID")));
                        whereAddIntdet.put("DEPOSITNO", CommonUtil.convertObjToStr(closeMap.get("DEPOSITNO")));
                        addMatInterest = (List) sqlMap.executeQueryForList("getDepositClosingDetails", whereAddIntdet);
                      }
                     if (notRenewMatu != null && notRenewMatu.length() > 0) {
                        long diff = 0;
                        whereAddIntdet=new HashMap();
                        Date strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                        Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURRENT_DT")));
                        diff = DateUtil.dateDiff(strMatDt, currDt);
                        whereAddIntdet.put("DEPOSIT_DT", map.get("MATURITY_DT"));
                        whereAddIntdet.put("PRODID", closeMap.get("PRODID"));
                        whereAddIntdet.put("CUSTID", closeMap.get("CUSTID"));
                        whereAddIntdet.put("PERIOD", diff);
                        whereAddIntdet.put("CATEGORY_ID", closeMap.get("CATEGORY_ID"));
                        whereAddIntdet.put("DEPOSITNO", closeMap.get("DEPOSITNO"));
                        addMatInterest = (List) sqlMap.executeQueryForList("getDepositClosingDetails", whereAddIntdet);
                        
                         diff = 0;
                         whereAddIntdet=new HashMap();
                         addInterest=null;
                         strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("MATURITY_DT")));
                        currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CURRENT_DT")));
                        diff = DateUtil.dateDiff(strMatDt, currDt);
                        whereAddIntdet.put("DEPOSIT_DT", map.get("CURRENT_DT"));
                        whereAddIntdet.put("PRODID", closeMap.get("PRODID"));
                        whereAddIntdet.put("CUSTID", closeMap.get("CUSTID"));
                        whereAddIntdet.put("PERIOD", diff);
                        whereAddIntdet.put("CATEGORY_ID", closeMap.get("CATEGORY_ID"));
                        whereAddIntdet.put("DEPOSITNO", closeMap.get("DEPOSITNO"));
                        addInterest = (List) sqlMap.executeQueryForList("getDepositClosingDetails", whereAddIntdet);
                        
                    }
                     }
                     }
                       returnMap.put("RENEWAL_AND_MATURED_DETAILS",addIntdet);
                    returnMap.put("DEPOSIT_ADDITIONAL_INTEREST_DETAILS", addInterest); 
                     returnMap.put("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS", addMatInterest);  
                     //End on 21-04-14
                }
                returnMap.put("DEPOSIT_CLOSE_DETAILS", lst);
                lst = null;
                if (CommonUtil.convertObjToStr(depositSubNo) != null && map.containsKey(CommonConstants.PRODUCT_ID)) {
                    // __Executes at the "OK" click in UI __Get Interest Details __ HashMap below contains INT_Before_Maturity,
                    // __INT_AT_Maturity, INT_After_Maturity, __ HashMap key is the Deposit+subDepositNo

                    //                HashMap interestReturned = getIntPayable(map, enableTrans) ;
                    HashMap interestReturned = new HashMap();
                    if (interestReturned != null) {
                        double interest = CommonUtil.convertObjToDouble(
                                interestReturned.get(CommonConstants.TOTAL_INTEREST_INT)).doubleValue();
                        returnMap.put("INTEREST_DETAILS", interestReturned);
                    } else {
                        interestReturned = new HashMap();
                        interestReturned.put(CommonConstants.AT_MATURITY_INT, "0.0");
                        interestReturned.put(CommonConstants.BEFORE_MATURITY_INT, "0.0");
                        interestReturned.put(CommonConstants.AFTER_MATURITY_INT, "0.0");
                        interestReturned.put(CommonConstants.TOTAL_INTEREST_INT, "0.0");
                        returnMap.put("INTEREST_DETAILS", interestReturned);
                        returnMap.put("TDS_DETAILS", "0.0");
                        //                    System.out.println("getData IntPayable"+ interestReturned);
                    }
                    // __Get Interest Drawn (Sum of Deposit_Interest table),
                    // __returns sum of TDS applied also
                    HashMap intMap = (HashMap) sqlMap.executeQueryForObject("getInterestDrawn", compositeDepositNo);
                    if (intMap == null) {
                        intMap = new HashMap();
                        intMap.put("INT_AMT", "0.0");
                        intMap.put("TDS_AMT", "0.0");
                    }
                    returnMap.put("INTEREST_DRAWN", intMap);
                    String lastAppliedDt = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getLastInterestAppliedDate", compositeDepositNo));
                    returnMap.put("INT_LAST_APPL_DT", lastAppliedDt);
                    intMap = null;
                    interestReturned = null;
                }
            }
        }
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE, compositeDepositNo);
        list = transactionDAO.getData(whereMap);
        // The following block added by Rajesh
        TransactionTO transactionTO = null;
        if (list != null && list.size() > 0) {
            transactionTO = (TransactionTO) list.get(0);
            if (transactionTO.getDebitAcctNo() != null) {
                returnMap.put("TRANS_DETAILS", transactionTO.getDebitAcctNo());
            }
        }

        returnMap.put("TransactionTO", list);
        whereMap = null;
        closeMap = null;
        list = null;
        System.out.println("########getData returnMap before going OB : " + returnMap);
        return returnMap;
    }

    private HashMap getIntPayable(HashMap map, boolean enableTrans) {
        //        System.out.println("DepositClosing getIntPayable :"+map);
        HashMap dataMap = null;
        StringBuffer strb = null;
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("IntPayableTask");
            header.setTransactionType(CommonConstants.PAYABLE);
            header.setProductType(TransactionFactory.DEPOSITS);

            header.setBranchID((String) map.get(CommonConstants.BRANCH_ID));
            header.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
            header.setUserID((String) map.get(CommonConstants.USER_ID));

            //__ Account Nos...
            ArrayList acctList = new ArrayList();
            strb = new StringBuffer();
            strb.append((String) map.get("DEPOSITNO"));
            strb.append("_");
            strb.append((String) map.get("DEPOSITSUBNO"));
            acctList.add(strb.toString());

            //__ Closing date
            ArrayList dateList = new ArrayList();
            dateList.add(curDate);

            HashMap taskParam = new HashMap();
            taskParam.put(CommonConstants.BRANCH, (String) map.get(CommonConstants.BRANCH_ID));
            if (map.containsKey(CommonConstants.PRODUCT_ID)) {
                taskParam.put(CommonConstants.PRODUCT_ID, (String) map.get(CommonConstants.PRODUCT_ID));
            }
            if (map.containsKey("PROD_ID")) {
                taskParam.put(CommonConstants.PRODUCT_ID, (String) map.get("PROD_ID"));
            }
            //             taskParam.put(CommonConstants.PRODUCT_ID, "FDGEN");
            taskParam.put(CommonConstants.ACT_NUM, acctList);
            //            taskParam.put("CLOSING_DT", dateList);
            if (map.containsKey("MAT_DATE") && map.get("MAT_DATE") != null) {
                taskParam.put("CLOSING_DT", map.get("MAT_DATE"));
            } else {
                taskParam.put("CLOSING_DT", curDate);
            }
            //            System.out.println("taskParam:" +taskParam);
            if (map.containsKey("MODE")) {
                if (map.get("MODE").equals(CommonConstants.TOSTATUS_RENEW)) {
                    taskParam.put("MODE", CommonConstants.TOSTATUS_RENEW);
                }
            }
            header.setTaskParam(taskParam);
            DepositIntTask tsk = new DepositIntTask(header, enableTrans);
            //            System.out.println("getInterestData() : " +tsk);
            dataMap = tsk.getInterestData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (HashMap) dataMap.get(strb.toString());
    }
    double interestAmtAfterMaturity = 0;

    private void insertData() throws Exception {
        sqlMap.executeUpdate("insertDepositWithDrawalTO", objTO);
    }

    private void updateData() throws Exception {
        sqlMap.executeUpdate("updateDepositWithDrawalTO", objTO);
    }

    private void deleteData() throws Exception {
        sqlMap.executeUpdate("deleteStatusDepositWithDrawalTO", objTO);
    }

    private void insertPartialWithDrawalList() throws Exception {
        int size = listTOs.size();
        HashMap where = new HashMap();
        List list;
        double amt = 0, yearAmt = 0;
        if (size > 0) {
            objTO = (DepositWithDrawalTO) listTOs.get(0);
            where.put("DEPOSITNO", objTO.getDepositNo());
            where.put("SUBNO", CommonUtil.convertObjToInt(objTO.getDepositSubNo()));
            where = (HashMap) sqlMap.executeQueryForObject("getProdPWDetails", where);
            where.put("DEPOSITNO", objTO.getDepositNo());
            where.put("SUBNO", CommonUtil.convertObjToInt(objTO.getDepositSubNo()));
            where.put("TODAY_DT", curDate);
            for (int i = 0; i < size; i++) {
                objTO = (DepositWithDrawalTO) listTOs.get(i);
                amt += objTO.getWithdrawAmt().doubleValue();
                where.put("WITHDRAWALDT", objTO.getWithdrawDt());

                list = (List) sqlMap.executeQueryForList("checkDepositDateNew", where);
                if (list != null && list.size() > 0) {
                    yearAmt += objTO.getWithdrawAmt().doubleValue();
                }
                where.put("WDAMT", new Double(amt));
                where.put("NOPW", new Integer(i + 1));
                where.put("YEARWDAMT", new Double(yearAmt));
                if (objTO.getStatus().compareToIgnoreCase(CommonConstants.STATUS_DELETED) != 0) {
                    checkRule(where);
                }
                if (objTO.getWithdrawNo().equalsIgnoreCase("-")) {
                    objTO.setWithdrawNo(this.getWithDrawalNo());
                    insertData();
                } else {
                    updateData();
                }
            }
        }
    }

    private void checkRule(HashMap where) throws Exception {
        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();

        context.addRule(new PartialWithdrawalCheckingRule());
        ArrayList arrlist = (ArrayList) engine.validateAll(context, where);
        if (arrlist != null && arrlist.size() > 0) {
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, arrlist);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.deposit.closing.PartialWithdrawalRuleHashMap");
            sqlMap.rollbackTransaction();
            throw new TTException(exception);
        }
    }

    private String getWithDrawalNo() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DEPOSIT.PARTIAL_WITHDRAWALNO");
        String lienNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        where = null;
        return lienNo;
    }

    public static void main(String str[]) {
        try {
            DepositClosingDAO dao = new DepositClosingDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap insertAccHead(HashMap closureMap, HashMap map, String branchID, String mode) throws Exception {
        System.out.println("insertAccHead map:" + map + "closureMap:" + closureMap + "branchID:" + branchID);
        String behavesLike = CommonUtil.convertObjToStr(closureMap.get("BEHAVES_LIKE"));
        String screenName = CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN));
        String currROI = "";       
        System.out.println("behavesLike" + behavesLike);
        closureMap = (HashMap) map.get("CLOSUREMAP");
         if(closureMap.containsKey("CURR_RATE_OF_INT") && closureMap.get("CURR_RATE_OF_INT") != null){
             currROI = " ROI - " + CommonUtil.convertObjToStr(closureMap.get("CURR_RATE_OF_INT"));
         }
        HashMap serviceChargeMap = new HashMap();
        HashMap serviceTaxDetails = new HashMap();
        HashMap serviceTaxDetailsForLoan = new HashMap();
        chargeLst = new ArrayList();
//        String generateSingleTransId = "";
         // Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
             String isTransferCharges = "";
             HashMap ChargeTranByTransferMap = new HashMap();
             List isChargeTranByTransferList = sqlMap.executeQueryForList("getIsChargesTransactionByTransfer", closureMap);
             if(isChargeTranByTransferList != null && isChargeTranByTransferList.size() > 0){
                 ChargeTranByTransferMap = (HashMap)isChargeTranByTransferList.get(0);
                 if(ChargeTranByTransferMap.containsKey("IS_TRANFER_CHARGES") && ChargeTranByTransferMap.get("IS_TRANFER_CHARGES") != null){
                     isTransferCharges = CommonUtil.convertObjToStr(ChargeTranByTransferMap.get("IS_TRANFER_CHARGES"));
                 }
             }             
        // End
        if (closureMap.containsKey("SERVICE_CHARGE_DETAILS")) {
            serviceChargeMap = (HashMap) closureMap.get("SERVICE_CHARGE_DETAILS");
        }
        if (closureMap.containsKey("Charge List Data") && closureMap.get("Charge List Data") != null) {
            chargeLst = (List) closureMap.get("Charge List Data");
            //added by Chithra
            if (closureMap.containsKey("serviceTax_Details") && closureMap.get("serviceTax_Details") != null) {
                serviceTaxDetails = (HashMap) closureMap.get("serviceTax_Details");
            }
            //End....
        } else if (closureMap.containsKey("serviceTax_Details") && closureMap.get("serviceTax_Details") != null) {
            serviceTaxDetails = (HashMap) closureMap.get("serviceTax_Details");
        }
//        branchID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        String debitBranchID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        String creditBranchID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        String initiatedBranch = CommonUtil.convertObjToStr(map.get(CommonConstants.INITIATED_BRANCH));
        HashMap transMap = new HashMap();
        if (closureMap.containsKey("OLDTRANSACTION")) {
            transMap = (HashMap) closureMap.get("OLDTRANSACTION");
        }
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", closureMap);
        String head = null;
        String direct = null;
        String closedStatus = null;
        String closeStatus = null;
        HashMap returnCalcMap = new HashMap();
        HashMap loanMap = new HashMap();
        HashMap othBankMap = new HashMap();
        interestBatchTO = new InterestBatchTO();
        HashMap txMap = new HashMap();
        boolean cumulativeReceivable = false;
        ArrayList transferList = new ArrayList();
        transactionDAO.setInitiatedBranch(initiatedBranch);
        String lien = CommonUtil.convertObjToStr(closureMap.get("LIEN_STATUS"));
        String closingStatus = CommonUtil.convertObjToStr(map.get("COMMAND"));
        double servicePercentage = 0.0;
        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
            servicePercentage = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(acHeads.get("SERVICE_CHARGE"))).doubleValue();
        }
        String depositNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
        String subNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_SUB_NO"));
        String depositSubNo = depositNo + "_" + subNo;
        System.out.println("insertAccHead depositSubNo : " + depositSubNo);
        String generateSingleTransId = "";
        //KD-3579 : TERM DEPOSIT LTD CLOSING
        if(closureMap.containsKey("LTD_CLOSE_SINGLE_TRANS_ID") && closureMap.get("LTD_CLOSE_SINGLE_TRANS_ID") != null){
            System.out.println("Execute inside LTD transfer");
            generateSingleTransId = CommonUtil.convertObjToStr(closureMap.get("LTD_CLOSE_SINGLE_TRANS_ID"));
        }else{
            generateSingleTransId = generateLinkID();
        }
        String generateSingleCashId = "";
        HashMap ltdBalanceMap = new HashMap();
        ltdBalanceMap.put("DEPOSITNO", depositNo);
        List lastApplDt = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", ltdBalanceMap);
        if (lastApplDt != null && lastApplDt.size() > 0) {
            ltdBalanceMap = (HashMap) lastApplDt.get(0);
            closedStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("ACCT_STATUS"));
            closeStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("STATUS"));
        }
        if (transMap != null && transMap.size() > 0) {//while coming for edit mode only it will work...
            transMap.put("TRANS_DETAILS", closureMap.get("TRANS_DETAILS"));
            if (closingStatus.equals("UPDATE") || map.get("MODE").equals("UPDATE") || closingStatus.equals("DELETE")) {
                makeOldNewTransaction(transMap, map);
                map.put("COMMAND", "INSERT");
                map.put("MODE", "INSERT");
                if ((closeStatus.equals("CREATED") || closeStatus.equals("LIEN")) && closedStatus.equals("CLOSED")) {
                    closedStatus = "NEW";
                } else if (closeStatus.equals("MODIFIED") && closedStatus.equals("MATURED")) {
                    closedStatus = "NEW";
                } else if (closeStatus.equals("MODIFIED") && closedStatus.equals("CLOSED")) {
                    closedStatus = "MATURED";
                } else if (closeStatus.equals("CREATED") && closedStatus.equals("MATURED")) {
                    closedStatus = "NEW";
                }
            }
        }
        // this for operative account for transfer deposit to operative ac......
        TxTransferTO transferTo = null;
        TransactionTO transactionTODebit = new TransactionTO();
        LinkedHashMap transactionDetailsMap = new LinkedHashMap();
        TransactionTO transactionTO;
        String instNo1 = null;
        String instNo2 = null;
        if (map.containsKey("TransactionTO")) {
            transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO;
            if (transactionDetailsMap.size() > 0) {
                if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null) {
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO != null && transactionTO.getTransType().equals("TRANSFER")) {
                            transactionTODebit.setTransType("TRANSFER");
                            String prodId = CommonUtil.convertObjToStr(transactionTODebit.getProductId());
                            transactionTODebit.setProductId(transactionTO.getProductId());
                            transactionTODebit.setProductType(TransactionFactory.OPERATIVE);
                            transactionTODebit.setDebitAcctNo(transactionTO.getDebitAcctNo());
                            transactionTODebit.setBranchId(transactionTO.getBranchId());
                         //   creditBranchID = CommonUtil.convertObjToStr(transactionTO.getBranchId());
                            transactionTODebit.setScreenName((String) map.get(CommonConstants.SCREEN));
                            allowedTransDetailsTO.put("1", transactionTODebit);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);
                            transactionDAO.setTransType("DEBIT");
                            allowedTransDetailsTO.put("1", transactionTO);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);
                        }                        
                    }
                }
            }
        }        
        if (map.containsKey("TransactionTO")) {
            transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO;
            if (transactionDetailsMap.size() > 0) {
                if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null) {
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO != null && transactionTO.getProductType() != null) {
                            if (transactionTO.getProductType().equals("TL") || transactionTO.getProductType().equals("AD")) {
                                loanMap.put("PROD_ID", transactionTO.getProductId());
                                List lstLoan = sqlMap.executeQueryForList("getLienAccounts", transactionTO.getDebitAcctNo());
                                if (lstLoan != null && lstLoan.size() > 0) {
                                    loanMap = (HashMap) lstLoan.get(0);
                                }
                            }
                            if (transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                HashMap wMap = new HashMap();
                                wMap.put("REC_OTHER_BANK", transactionTO.getProductId());
                                List lstOB = sqlMap.executeQueryForList("getAccountHeadProd1", wMap);
                                if (lstOB != null && lstOB.size() > 0) {
                                    othBankMap = (HashMap) lstOB.get(0);
                                }
                            }
                            if (transactionTO != null && transactionTO.getProductType().equals("RM")) {
                                instNo1 = transactionTO.getChequeNo();
                                instNo2 = transactionTO.getChequeNo2();
                            }
                            if (transactionTO != null && transactionTO.getTransType().equals("CASH")) {
                                //KD-3579 : TERM DEPOSIT LTD CLOSING
                                  if (closureMap.containsKey("LTD_CLOSE_SINGLE_TRANS_ID") && closureMap.get("LTD_CLOSE_SINGLE_TRANS_ID") != null) {
                                      System.out.println("Execute inside LTD cash");
                                    generateSingleCashId = CommonUtil.convertObjToStr(closureMap.get("LTD_CLOSE_SINGLE_TRANS_ID"));
                                } else {
                                    generateSingleCashId = generateLinkID();
                                }
                            }
                        }
                    }
                }
            }
        }        
        //        if(lien != null && lien.length()>0){
        HashMap closeMap = new HashMap();
        closeMap.put("PROD_ID", closureMap.get("PROD_ID"));
        List lst = sqlMap.executeQueryForList("getMaturedAccountHead", closeMap);
        if (lst != null && lst.size() > 0) {
            closeMap = (HashMap) lst.get(0);
            head = transactionTODebit.getDebitAcctNo();
            direct = CommonUtil.convertObjToStr(closeMap.get("MATURITY_DEPOSIT"));
            if (head != null && head.length() > 0) {
                if (head.equals(direct)) {
                } else {
                    head = "GL";
                    direct = "OTHER";
                }
            } else {
                head = "GL";
                direct = "OTHER";
            }
        }
        HashMap headMap = new HashMap();
        headMap.put("DEPOSIT_NO", depositNo);
        List lstHead = sqlMap.executeQueryForList("getInstallTypeHead", headMap);
        if (lstHead != null && lstHead.size() > 0) {
            headMap = (HashMap) lstHead.get(0);
        }
        double payAmt = 0.0;
        double interestAmt = 0.0;
        double balPay = 0.0;
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO.setLinkBatchID(depositSubNo);
        if (!closingStatus.equals("DELETE") && closureMap.get("BEHAVES_LIKE") != null) {
            transactionTO = new TransactionTO();
            TransactionTO transTO = new TransactionTO();
            ArrayList cashList = new ArrayList();
            HashMap depIntMap = new HashMap();
            depIntMap.put("DEPOSIT_NO", depositSubNo);
            depIntMap.put("ROI", closureMap.get("ROI"));
            depIntMap.put("DEPOSIT_AMT", closureMap.get("DEPOSIT_AMT"));
            depIntMap.put("PROD_ID", closureMap.get("PROD_ID"));
            depIntMap.put("CUST_ID", closureMap.get("CUST_ID"));
            double prev_interst=0;
            double AgentCommAmt = CommonUtil.convertObjToDouble(closureMap.get("AGENT_COMMISION_AMT")).doubleValue();
            double penalAmt = CommonUtil.convertObjToDouble(closureMap.get("DELAYED_AMOUNT")).doubleValue();
            double clrAmt = CommonUtil.convertObjToDouble(closureMap.get("CLEAR_BALANCE")).doubleValue();
            double totalAmt = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_AMT")).doubleValue();
            double depAmt = CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")).doubleValue();
            double cashdepAmt = CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")).doubleValue();// This is deposit amount for the customer...
            double tdsAmt = CommonUtil.convertObjToDouble(closureMap.get("TDS_SHARE")).doubleValue();
            double intPay = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue(); // This is interest amount for the customer...
            double paid = CommonUtil.convertObjToDouble(closureMap.get("PAID_INTEREST")).doubleValue();
            double creditInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();
              //modified by rishad 17/10/2016  mantis 0005215
            double debitInt = CommonUtil.convertObjToDouble(closureMap.get("DR_INTEREST")).doubleValue();
           // double debitInt = CommonUtil.convertObjToDouble(closureMap.get("INTEREST_AMT")).doubleValue();
           // double debitInt = CommonUtil.convertObjToDouble(closureMap.get("DR_INTEREST")).doubleValue();
            double drawnAmt = CommonUtil.convertObjToDouble(closureMap.get("INTEREST_DRAWN")).doubleValue();
            prev_interst = CommonUtil.convertObjToDouble(closureMap.get("PREV_INTEREST_AMT")).doubleValue();
            double add_int_amt= CommonUtil.convertObjToDouble(closureMap.get("ADD_INT_AMOUNT")).doubleValue();//added by chithra on 16-05-14 For additional int amt
            double add_Loan_int_amt = CommonUtil.convertObjToDouble(closureMap.get("ADD_LOAN_INT_AMOUNT"));
            String addDays=CommonUtil.convertObjToStr(closureMap.get("ADD_INT_DAYS"));
            double interest = creditInt - drawnAmt;
            double pay_bal_Int=0;
            double rec_recivableAmt = 0;
            //Added by sreekrishnan
            double totalCharge = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_CHARGE")).doubleValue();
            if (closureMap.containsKey("BOTH_PAY_REC")) {
                HashMap recPayMap = (HashMap) closureMap.get("BOTH_PAY_REC");
                if (recPayMap != null && recPayMap.containsKey("PAYABLE_REC_INT")) {
                    pay_bal_Int = CommonUtil.convertObjToDouble(recPayMap.get("PAYABLE_REC_INT"));
                    intPay = intPay + pay_bal_Int;
                }
            }else{
               intPay = intPay + prev_interst; 
            }
            String frmDate="",toDate="";
            Date startDt = null,endDt = null; //Added for KD-3460
            String noOfIntDays = "";
            if(closureMap.containsKey("FROM_INT_DATE")){
                frmDate=CommonUtil.convertObjToStr(closureMap.get("FROM_INT_DATE"));
                startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closureMap.get("FROM_INT_DATE")));
            }
            if(closureMap.containsKey("TO_INT_DATE")){
                toDate=CommonUtil.convertObjToStr(closureMap.get("TO_INT_DATE"));
                endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closureMap.get("TO_INT_DATE")));
            }            
            if(frmDate.length() > 0 && toDate.length() > 0){
                noOfIntDays = " - " + String.valueOf(DateUtil.dateDiff(startDt, endDt)) +" Days ";
            }
            if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") || closureMap.get("BEHAVES_LIKE").equals("DAILY")||closureMap.get("BEHAVES_LIKE").equals("THRIFT")) {
                totalAmt = totalAmt + penalAmt + AgentCommAmt;
            }
             if ((closureMap.get("BEHAVES_LIKE").equals("RECURRING")||closureMap.get("BEHAVES_LIKE").equals("THRIFT"))&& closureMap.containsKey("REC_RECIVABLE")){
                 rec_recivableAmt =CommonUtil.convertObjToDouble(closureMap.get("REC_RECIVABLE"));
             }
             if (closureMap.get("BEHAVES_LIKE").equals("DAILY")&& closureMap.containsKey("REC_RECIVABLE")){
                 rec_recivableAmt =CommonUtil.convertObjToDouble(closureMap.get("REC_RECIVABLE"));
             }
            if (closedStatus.equals("NEW")) {
                if (creditInt < 0 || creditInt > 0) {
                    interestAmt = creditInt - intPay;
                    //                interestAmt = interest;
                    balPay = intPay;
                    System.out.println("NEW interestAmt: " + interestAmt);
                } else {
                    intPay = intPay;
                }
            } else if (closedStatus.equals("MATURED")) {
                balPay = interest;
                tdsAmt = 0.0;
                penalAmt = 0.0;
                System.out.println("MATURED balPay:" + balPay);
            }
            //added by chithra
            double loan_amount = 0, blncLonamt = 0;
            if (lien != null && lien.length() > 0) {
                HashMap loanDetMap = new HashMap();
                if (closureMap.containsKey("LTDCLOSINGDATA")) {
                    loanDetMap = (HashMap) closureMap.get("LTDCLOSINGDATA");
                }
                if (loanDetMap != null && loanDetMap.containsKey("AMOUNT")) {
                    loan_amount = CommonUtil.convertObjToDouble(loanDetMap.get("AMOUNT"));
                }
                if (depAmt < loan_amount) {
                    blncLonamt = loan_amount - depAmt;
                }
            }
            //end..
            //|| closureMap.get("BEHAVES_LIKE").equals("BENEVOLENT")
            if ((closureMap.get("BEHAVES_LIKE").equals("FIXED") ||closureMap.get("BEHAVES_LIKE").equals("BENEVOLENT")  || closureMap.get("BEHAVES_LIKE").equals("FLOATING_RATE")) && closureMap.containsKey("TRANSFER_OUT_MODE")
                    && (closureMap.get("TRANSFER_OUT_MODE").equals("N") || closureMap.get("TRANSFER_OUT_MODE").equals("NO"))) {     
              double fixIntAmt = intPay+add_int_amt;
                 if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                 	double   intam = intPay - blncLonamt;
                    if (intam < 0) {
                        fixIntAmt = add_int_amt - (intam * -1);
                    } else if(intam>0){
                        fixIntAmt=intam+add_int_amt;
                    }
                }
                if ((totalAmt+totalCharge) >= depAmt) {//payable....//Modified by sreekrishnan
                    System.out.println("********* FIXED Payable STARTED...");
                    if (map.containsKey("TransactionTO")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                    transactionDAO.execute(map);
                                }
                                if (interestAmt > 0 || interestAmt < 0 || balPay > 0) {
                                    if (interestAmt > 0) {//excess amount recovering from paybale (is a mediator A/c)crediting paid
                                        System.out.println("interestAmt>0 IF fd : " + interestAmt);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                        
                                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                        } else {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt>0 Payable txMap fd : " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        interestAmt = 0.0;
                                    }
                                    if (interestAmt < 0) {//balance amount debiting from paid crediting payable
                                        interestAmt = interestAmt * -1;
                                        System.out.println("interestAmt<0 IF fd :" + interestAmt);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt<0 txMap fd : " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    }
                                    if (balPay > 0) {//interestamt and balpay adding and debiting payable crediting particualr A/c(Cash or Transfer)
                                        System.out.println("balPay IF: " + balPay);
                                        if (tdsAmt > 0) {//incase any tds is there means here itself debiting tdsAmt creding TDS A/c
                                            System.out.println("tdsAmt IF fd : " + tdsAmt);
                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.PARTICULARS, "Tds Deduction" + depositSubNo);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                            
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put(TransferTrans.CR_AC_HD, (String) closureMap.get("TDS_ACHD"));//crediting TDS A/c head
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);

                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tdsAmt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferList.add(transferTo);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                            returnCalcMap.put("TDS_AMT", new Double(tdsAmt));
                                        }
                                        balPay = balPay - tdsAmt;                                        
                                        if (balPay > 0) {//deducting TDS Amt, balance left over amt only crediting particular A/c(Cash or Transfer)
                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            if (!head.equals(direct)) {
                                                System.out.println("balPay after TDS IF: " + balPay);
                                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                                    System.out.println("TRANSFER balPay after TDS IF: " + balPay);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TD")) {
                                                        HashMap newDepMap = new HashMap();
                                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                                        if (lst != null && lst.size() > 0) {
                                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                            
                                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                        }
                                                    } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("AD")) {
                                                        // The following block uncommented by Rajesh
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        System.out.println("*******Lien payable AD: " + txMap);
                                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                                        }
                                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                        //modified by rishad interbranch issue
                                                       //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.CR_BRANCH,transactionTODebit.getBranchId());
                                                    }else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TL")) {
                                                       
                                                    } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                        //                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                                        //                                                        ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("RM")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                                        txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                    } else if (!head.equals(direct)) {
                                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    }
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);                   
                                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(balPay));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                                    if (transactionTO.getProductType().equals("OA")) {
                                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                                    } else if (transactionTO.getProductType().equals("AB")) {
                                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                                    } else if (transactionTO.getProductType().equals("SA")) {
                                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                                    } else if (transactionTO.getProductType().equals("TL")) {
                                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                                    } else if (transactionTO.getProductType().equals("AD")) {
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                    } else {
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                                    }
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(balPay));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                                   
                                                } else if (transactionTO.getTransType().equals("CASH")) {
                                                    System.out.println("CASH balPay after TDS IF fd : " + balPay);
                                                    if (balPay > 0) {
                                                        txMap = new HashMap();
                                                        transferList = new ArrayList(); // for local transfer
                                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                       
                                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                        transactionDAO.addTransferDebit(txMap, balPay);
                                                        transactionDAO.deleteTxList();
                                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                        transTO.setTransType("CASH");
                                                        transTO.setBatchId(depositSubNo);
                                                        transTO.setTransAmt(new Double(balPay));
                                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                        cashList.add(transTO);
                                                        transactionDAO.setScreenName(screenName);
                                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                                        transactionDAO.addCashList(cashList);
                                                        transactionDAO.doTransfer();
                                                    }
                                                } else if (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                                        && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("PARTIALLY_CLOSING")) {
                                                    txMap = new HashMap();
                                                    transferList = new ArrayList();
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.PARTICULARS, "");
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE", "GL");       
                                                    HashMap newDepMap = new HashMap();
                                                    newDepMap.put("PROD_ID", (String) closureMap.get("PROD_ID"));
                                                    newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                                    if (lst != null && lst.size() > 0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                                        txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.PARTICULARS, "");
                                                    }
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                                    if (transactionTO.getProductType().equals("OA")){
                                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                                    }else if(transactionTO.getProductType().equals("AB")){
                                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                                    }else if(transactionTO.getProductType().equals("SA")){
                                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                                    }else if(transactionTO.getProductType().equals("TL")){
                                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                                    }else if(transactionTO.getProductType().equals("AD")){
                                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                                    }else
                                                            txMap.put("TRANS_MOD_TYPE", "GL");
                    								txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                                }
                                            }
                                        }
                                    }
                                    returnCalcMap.put("INT_PAY_AMT", new Double(balPay));
                                } else {//all amount taking from intpaid to intpaylbe again debit intpayable to particular account crediting..
                                    System.out.println("intPay ELSE ----------:" + intPay);
                                    //added by chithra
                                    if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                                        intPay = intPay - blncLonamt;
                                        if (intPay < 0) {
                                            add_int_amt = add_int_amt - (intPay *-1);} }
                                    //End...
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                   
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                   
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put("generateSingleTransId", generateSingleTransId);

                                    returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                                    if (tdsAmt > 0) {//incase any tds is there means here itself debiting tdsAmt creding TDS A/c
                                        System.out.println("tdsAmt ELSE fd :" + tdsAmt);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) closureMap.get("TDS_ACHD"));//crediting TDS A/c head
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, tdsAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferList.add(transferTo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        returnCalcMap.put("TDS_AMT", new Double(tdsAmt));
                                    }
                                    intPay = intPay - tdsAmt;                                    
                                    System.out.println("tdsAmt after TDS ELSE fd :" + intPay);
                                    if (!head.equals(direct)) {
                                        if (transactionTO != null && transactionTO.getTransType().equals("TRANSFER") && closureMap.containsKey("FULLY_CLOSING_STATUS")
                                                && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                                            System.out.println("TRANSFER tdsAmt after TDS ELSE fd :" + intPay);
                                            System.out.println("balPay after TDS IF prodType fd : " + transactionTO.getProductType());
                                            if (intPay > 0) {//deducting TDS Amt, balance left over amt only crediting particular A/c(Transfer)
                                                txMap = new HashMap();
                                                //transferList = new ArrayList();
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.PARTICULARS, /*transactionTODebit.getDebitAcctNo()+*/" From "+frmDate+" To "+toDate + noOfIntDays +currROI);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE", "GL");    
                                                if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TD")) {
                                                    HashMap newDepMap = new HashMap();
                                                    newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                                    newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                                    if (lst != null && lst.size() > 0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.PARTICULARS, /*transactionTODebit.getDebitAcctNo()+*/" From "+frmDate+" To "+toDate + noOfIntDays + currROI);
                                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    }
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("AD")) {
                                                    // The following block uncommented by Rajesh
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    System.out.println("*******Lien payable AD fd : " + txMap);
                                                } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                    if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                                    }
                                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    //modified by rishad 24/06/2015
                                                    //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());
                                                }else if (transactionTO.getProductType().equals("TL")) {
                                                  
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                    //                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                                    //                                                    ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                    txMap.put("TRANS_MOD_TYPE",transactionTO.getProductType());
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    System.out.println("*******No Lien payable OA fd : " + txMap);
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("RM")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                                    txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    txMap.put("TRANS_MOD_TYPE","TD");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                    System.out.println("intPay txMap Payable RM : " + txMap);
                                                } else if (!head.equals(direct)) {
                                                    txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE","GL");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    System.out.println("intPay txMap Payable Not a GL : " + txMap);
                                                }
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                                txMap.put(TransferTrans.PARTICULARS,/* depositSubNo+*/" From "+frmDate+" To "+toDate + noOfIntDays + currROI);
                                                if (transactionTO.getProductType().equals("OA")){
                                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(transactionTO.getProductType().equals("AB")){
                                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(transactionTO.getProductType().equals("SA")){
                                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                                }else if(transactionTO.getProductType().equals("TL")){
                                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                                }else if(transactionTO.getProductType().equals("AD")){
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                                if(!closureMap.get("BEHAVES_LIKE").equals("FIXED")){
                    							txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);  
                                                }
                                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                                //transactionDAO.doTransferLocal(transferList, branchID);//GOOS
                                               
                                            }
                                             if(add_int_amt>0){
                                                txMap = new HashMap();
                                                //transferList = new ArrayList();
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.PARTICULARS, addDays+" "+transactionTODebit.getDebitAcctNo());
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE","GL");
                                                if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TD")) {
                                                    HashMap newDepMap = new HashMap();
                                                    newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                                    newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                                    if (lst != null && lst.size() > 0) {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.PARTICULARS,addDays+" "+ transactionTODebit.getDebitAcctNo());
                                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                        txMap.put("TRANS_MOD_TYPE","TD");
                                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    }
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("AD")) {
                                                    // The following block uncommented by Rajesh
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                                    txMap.put("TRANS_MOD_TYPE","AD");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                                     txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                     if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                                         txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                                     }
                                                     txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                     txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                     //Modified by rishad 24/06/2015
                                                      txMap.put(TransferTrans.CR_BRANCH,transactionTODebit.getBranchId() );
                                                     //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                 }else if (transactionTO.getProductType().equals("TL")) {
                                                    
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                    //txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                                    //ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                                    txMap.put("TRANS_MOD_TYPE",transactionTO.getProductType());
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                    System.out.println("*******No Lien payable OA fd : " + txMap);
                                                } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("RM")) {
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                                    txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    txMap.put("TRANS_MOD_TYPE","TD");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                    System.out.println("intPay txMap Payable RM : " + txMap);
                                                } else if (!head.equals(direct)) {
                                                    txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put("TRANS_MOD_TYPE","GL");
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    System.out.println("intPay txMap Payable Not a GL : " + txMap);
                                                }
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleTransId);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(add_int_amt));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                                txMap.put(TransferTrans.PARTICULARS,addDays+" "+ depositSubNo);
                                                if (transactionTO.getProductType().equals("OA")){
                                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(transactionTO.getProductType().equals("AB")){
                                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(transactionTO.getProductType().equals("SA")){
                                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                                }else if(transactionTO.getProductType().equals("TL")){
                                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                                }else if(transactionTO.getProductType().equals("AD")){
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                                if(!closureMap.get("BEHAVES_LIKE").equals("FIXED")){
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(add_int_amt));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                                }
                                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                                //transactionDAO.doTransferLocal(transferList, branchID);   
                                                    }                                             
                                             
                                        } else if (transactionTO.getTransType().equals("CASH") && closureMap.containsKey("FULLY_CLOSING_STATUS")
                                                && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                                            System.out.println("CASH tdsAmt after TDS ELSE fd :" + tdsAmt);
                                            if (intPay > 0) {//deducting TDS Amt, balance left over amt only crediting particular A/c(Cash)
                                                txMap = new HashMap();
                                                transferList = new ArrayList(); // for local transfer
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.PARTICULARS, /*depositSubNo+*/" From "+frmDate+" To "+toDate + noOfIntDays + currROI);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE","GL");
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put(TransferTrans.NARRATION, "");
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleCashId);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                transactionDAO.addTransferDebit(txMap, intPay);
                                                transactionDAO.deleteTxList();
                                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                transTO.setTransType("CASH");
                                                transTO.setBatchId(depositSubNo);
                                                transTO.setTransAmt(new Double(intPay));
                                                transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                cashList=new ArrayList();
                                                cashList.add(transTO);
                                                transactionDAO.setScreenName(screenName);
                                                transactionDAO.setInitiatedBranch(initiatedBranch);
                                                transactionDAO.addCashList(cashList);
                                                transactionDAO.doTransfer();
                                            }
                                             if (add_int_amt > 0) {//added by chithra on 29-05-14
                                                txMap = new HashMap();
                                                transferList = new ArrayList(); // for local transfer
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.PARTICULARS,addDays+ " "+depositSubNo);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE","GL");
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                               
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put(TransferTrans.NARRATION, "");
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleCashId);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                transactionDAO.addTransferDebit(txMap, add_int_amt);
                                                transactionDAO.deleteTxList();
                                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                transTO.setTransType("CASH");
                                                transTO.setBatchId(depositSubNo);
                                                transTO.setTransAmt(new Double(add_int_amt));
                                                transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                cashList=new ArrayList();
                                                cashList.add(transTO);
                                                transactionDAO.setScreenName(screenName);
                                                transactionDAO.setInitiatedBranch(initiatedBranch);
                                                transactionDAO.addCashList(cashList);
                                                transactionDAO.doTransfer();
                                            }
                                        } else if (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                                && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("PARTIALLY_CLOSING")) {
                                            System.out.println("transactionTO null :");
                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, "");
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put("TRANS_MOD_TYPE","GL");
                                            HashMap newDepMap = new HashMap();
                                            newDepMap.put("PROD_ID", (String) closureMap.get("PROD_ID"));
                                            newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                            if (lst != null && lst.size() > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                                txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                txMap.put("TRANS_MOD_TYPE","TD");
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.PARTICULARS, "");
                                            }
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                            
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            if (transactionTO.getProductType().equals("OA")){
                                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(transactionTO.getProductType().equals("AB")){
                                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(transactionTO.getProductType().equals("SA")){
                                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                                }else if(transactionTO.getProductType().equals("TL")){
                                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                                }else if(transactionTO.getProductType().equals("AD")){
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                    }
                                    returnCalcMap.put("INT_AMT", new Double(intPay));
                                }
                                if (depAmt > 0 && (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                        && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")
                                        || closureMap.containsKey("FULLY_CLOSING_STATUS")
                                        && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("PARTIALLY_CLOSING"))
                                        && transactionTO != null) {
                                    System.out.println("transactionTO depAmt :" + transactionTO + "transactionTODebit" + transactionTODebit);
                                    txMap = new HashMap();
                                    //transferList = new ArrayList();
                                    if (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                            && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                                        if (closedStatus.equals("NEW")) {
                                            if (lien != null && lien.length() > 0) {
                                                depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                                if(blncLonamt<=0){
                                                	depAmt = depAmt - intPay-add_int_amt;
                                                } else{
                                                    depAmt=0;}
                                            } else {
                                                depAmt = depAmt;
                                                if(closureMap.get("BEHAVES_LIKE").equals("BENEVOLENT"))
                                                {
                                                depAmt=totalAmt;
                                                }
                                                System.out.println("No Lien fd depAmt:" + depAmt);
                                            }
                                        } else if (closedStatus.equals("MATURED")) {
                                            depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                            if(blncLonamt<=0){
                                            	depAmt = depAmt - balPay-add_int_amt;
                                            } else{
                                                depAmt=0;}
                                        }
                                    } else {
                                          if(blncLonamt<=0){
                                          	depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                          } else{
                                            depAmt=0;}
                                    }
                                    if (closedStatus.equals("NEW")) {
                                        if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                            if (head.equals(direct)) {
                                                System.out.println("EQUALS NON fd GL :" + depAmt);
                                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                 txMap.put("TRANS_MOD_TYPE", "GL");
                                            } else {
                                                System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                 txMap.put("TRANS_MOD_TYPE", "TD");
                                            }
                                        } else {
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                        }
                                    } else if (closedStatus.equals("MATURED")) {
                                        txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                         txMap.put("TRANS_MOD_TYPE", "TD");
                                    }
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    if (transactionTO.getTransType().equals("TRANSFER")) {//want to edit
                                        System.out.println("TRANSFER fd depAmt: " + depAmt);
                                            //Added  by sreekrishnan
                                            double tr_Dr_amt=0;
                                            if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                double depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
//                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
//                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                   // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                   // transferList.add(transferTo);
            //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                                }
                                            }
                                            //added by chithra for service Tax
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                // This block of cose rewriting for Service tax to GST conversion
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                   
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                               
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                        }
                                        if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TD")) {
                                            HashMap newDepMap = new HashMap();
                                            newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                            newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                            if (lst != null && lst.size() > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                                txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                                txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                 txMap.put("TRANS_MOD_TYPE", "TD");
                                            }
                                        } else if (transactionTO.getProductType() != null && transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                             txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            System.out.println("*******Lien payable fd : " + txMap);
                                        } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("AD")) {
                                            // The following block uncommented by Rajesh
                                            txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                             txMap.put("TRANS_MOD_TYPE", "AD");
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            System.out.println("*******Lien payable AD: " + txMap);
                                        } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                            txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                            if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                            }
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_BRANCH,  transactionTODebit.getBranchId());
                                           //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TL")) {
                                            
                                        } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            //                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                            //                                            ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                            txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                             txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        } else if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("RM")) {
                                            if (balPay > 0) {
                                                depAmt = balPay + depAmt;
                                            } else if (intPay > 0) {
                                                depAmt = intPay + depAmt;
                                            }
                                            LinkedHashMap notDeleteMap = new LinkedHashMap();
                                            LinkedHashMap transferMap = new LinkedHashMap();
                                            HashMap remittanceMap = new HashMap();
                                            remittanceIssueDAO = new RemittanceIssueDAO();
                                            remittanceIssueTO = new RemittanceIssueTO();
                                            String favouringName = transactionTODebit.getDebitAcctNo();
                                            transactionTODebit.setApplName(favouringName);
                                            transactionTODebit.setTransAmt(new Double(depAmt));
                                            transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                            transactionTODebit.setProductType("TD");
                                            transactionTODebit.setDebitAcctNo(depositSubNo);
                                            remittanceIssueDAO.setFromotherDAo(false);
                                            notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                            transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                            remittanceMap.put("TransactionTO", transferMap);
                                            remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                            remittanceMap.put("OPERATION_MODE", "ISSUE");
                                            remittanceMap.put("AUTHORIZEMAP", null);
                                            remittanceMap.put("USER_ID", map.get("USER_ID"));
                                            remittanceMap.put("MODULE", "Remittance");
                                            remittanceMap.put("MODE", "INSERT");
                                            remittanceMap.put("SCREEN", "Issue");
                                            HashMap behavesMap = new HashMap();
                                            behavesMap.put("BEHAVES_LIKE", "PO");
                                            List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                behavesMap = (HashMap) lstRemit.get(0);
                                            }
                                            HashMap draweeMap = new HashMap();
                                            if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                                lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                                if (lstRemit != null && lstRemit.size() > 0) {
                                                    draweeMap = (HashMap) lstRemit.get(0);
                                                }
                                            }
                                            remittanceIssueTO.setDraweeBranchCode(branchID);
                                            remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                            remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                            remittanceIssueTO.setCity("560");
                                            remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                            remittanceIssueTO.setFavouring(favouringName);
                                            remittanceIssueTO.setBranchId(branchID);
                                            remittanceIssueTO.setRemarks(depositSubNo);
                                            remittanceIssueTO.setCommand("INSERT");
                                            remittanceIssueTO.setAmount(new Double(depAmt));
                                            remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                            remittanceIssueTO.setExchange(new Double(0.0));
                                            remittanceIssueTO.setPostage(new Double(0.0));
                                            remittanceIssueTO.setOtherCharges(new Double(0.0));
                                            remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                            remittanceIssueTO.setInstrumentNo1(instNo1);
                                            remittanceIssueTO.setInstrumentNo2(instNo2);
                                            remittanceIssueTO.setStatusDt(curDate);
                                            remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            LinkedHashMap remitMap = new LinkedHashMap();
                                            LinkedHashMap remMap = new LinkedHashMap();
                                            remMap.put(String.valueOf(1), remittanceIssueTO);
                                            remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                            remittanceMap.put("RemittanceIssueTO", remitMap);
                                            remittanceIssueDAO.execute(remittanceMap);
                                        }
                                        if (!transactionTO.getProductType().equals("RM")) {
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                           
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, depAmt-tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            if (transactionTO.getProductType().equals("OA")){
                                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                                }else if(transactionTO.getProductType().equals("AB")){
                                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                                }else if(transactionTO.getProductType().equals("SA")){
                                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                                }else if(transactionTO.getProductType().equals("TL")){
                                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                                }else if(transactionTO.getProductType().equals("AD")){
                                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                                }else
                                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                            if(closureMap.get("BEHAVES_LIKE").equals("FIXED")){
                                                depAmt+=fixIntAmt; 
                                            }
                                            if(tdsAmt > 0){ // Added by nithya on 18-02-2020 for KD-1090
                                               depAmt -= tdsAmt;  
                                            }
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, depAmt-tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.setForDebitInt(true);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                            //this one is Storing record for REMITT_ISSUE Table....
                                            transactionTODebit.setTransAmt(new Double(depAmt-tr_Dr_amt));
                                            map.put("TransactionTO", transactionDetailsMap);
                                            transactionDAO.doTransfer();
                                            fixIntAmt = 0.0;
                                        }
                                        returnCalcMap.put("PAY_AMT", new Double(depAmt-tr_Dr_amt));
                                    } else if (transactionTO.getTransType().equals("CASH") && depAmt>0) {
                                        System.out.println("CASH fd depAmt: " + depAmt+"add_int_amt :"+add_int_amt);
                                        //Added  by sreekrishnan
                                            double tr_Dr_amt=0;
                                            if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                double depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
//                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
//                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                   // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                   // transferList.add(transferTo);
            //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                                }
                                            }
                                            //added by chithra for service Tax
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }                                             
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                              
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.NARRATION, "");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        if(behavesLike.equals("BENEVOLENT")){
                                           //  double depositAmount = totalAmt-intPay;
                                            double depositAmount = totalAmt;
                                            transactionTO.setTransAmt(CommonUtil.convertObjToDouble(depositAmount));
                                            transactionDAO.addTransferDebit(txMap, depositAmount);
                                        } else {
                                            transactionTO.setTransAmt(new Double(depAmt-tr_Dr_amt));
                                            transactionDAO.addTransferDebit(txMap, depAmt-tr_Dr_amt);
                                        }
                                        transactionDAO.deleteTxList();
                                        transTO = new TransactionTO();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        //Jeffin John
                                        if(behavesLike.equals("BENEVOLENT")){
                                            //double depositAmount = totalAmt-intPay;
                                            double depositAmount = totalAmt;
                                            transTO.setTransAmt(CommonUtil.convertObjToDouble(depositAmount));
                                        }else{
                                            transTO.setTransAmt(new Double(depAmt-tr_Dr_amt));
                                        }
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                      //  returnCalcMap.put("PAY_AMT", new Double(depAmt-tr_Dr_amt));
                                           double depositAmount = totalAmt-intPay;
                                    if(behavesLike.equals("BENEVOLENT")){
                                        returnCalcMap.put("PAY_AMT", CommonUtil.convertObjToDouble(depositAmount));
                                    } else {
                                        returnCalcMap.put("PAY_AMT", new Double(depAmt-tr_Dr_amt));
                                    }  }
                                    //returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                }
                            }
                        }
                    }
                    System.out.println("FIXED Payable COMPLETED...");
                } else {  // Receivable Amount from the customer....
                    System.out.println("********* FIXED Receivable STARTED...");
                    double paycreditInt = 0.0;
                    double receiveInt = drawnAmt - debitInt;
                    double zeroInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();
                    if (receiveInt > 0) {
                        receiveInt = receiveInt;
                    } else {
                        intPay = receiveInt * -1;
                    }
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO1;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO1 = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO1 != null) {
                                transactionTO = (TransactionTO) allowedTransDetailsTO1.get(String.valueOf(1));
                            }
                        }
                    }
                    if (receiveInt > 0) {//first one amount taking from intpaylbe to intpaid
                        System.out.println("transactionTO" + transactionTO);
                        if (transactionTO.getTransType().equals("TRANSFER") || map.containsKey("LTD")) {
                             paycreditInt = receiveInt + zeroInt;
                            if (intPay > 0 && !transactionTO.getTransType().equals("TRANSFER")) {//first one amount taking from intpaylbe to particular account....
                                if (transactionTO.getProductType().equals("GL")) {
                                    System.out.println("*******Lien Status payable : " + txMap);
                                } 
//                                else {
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    System.out.println("receiveInt IF: " + receiveInt);
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// debited to interest payable account head......
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.DR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));// crediting to Operative account head......
                                    txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    if (transactionTO.getProductType().equals("OA")){
                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                    }else if(transactionTO.getProductType().equals("AB")){
                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                    }else if(transactionTO.getProductType().equals("SA")){
                                        txMap.put("TRANS_MOD_TYPE", "SA");
                                    }else if(transactionTO.getProductType().equals("TL")){
                                        txMap.put("TRANS_MOD_TYPE", "TL");
                                    }else if(transactionTO.getProductType().equals("AD")){
                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                    }else
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                    System.out.println("intPay txMap : " + txMap);
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(receiveInt));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(receiveInt));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                            }
                           
                          if(transactionTO.getTransType().equals("TRANSFER")){
                            if (paycreditInt > 0) {
                                System.out.println("paycreditInt>0 IF:" + paycreditInt);
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                               
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                
                                if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                }
                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                System.out.println("interestAmt txMap : " + txMap);
//                              transferTo = transactionDAO.addTransferCreditLocal(txMap, paycreditInt);
//                              transferTo.setSingleTransId(generateSingleTransId);
//                              transferList.add(transferTo);
                                //txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                txMap.put(TransferTrans.PARTICULARS, "Excess Interest Paid Recollected");
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, paycreditInt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                             //   transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                             //   transactionDAO.doTransferLocal(transferList, branchID);
                            }
                            }
                        } else {
                            System.out.println("CASH depAmt:" + depAmt);
                            System.out.println("intPay @@@:" + intPay);
                            transactionDAO.execute(map);
                            if(pay_bal_Int > 0){
                                intPay =intPay-pay_bal_Int;
                            }
                            txMap = new HashMap();
                            transferList = new ArrayList(); // for local transfer                           
                            if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                System.out.println("(String)acHeads.get(\"INT_RECOVERY_HEAD\")>>>" + (String) acHeads.get("INT_RECOVERY_HEAD"));
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));
                            }
                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            //                            txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                            //txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                            txMap.put(TransferTrans.PARTICULARS, "Excess Interest Paid Recollected");
                            txMap.put(TransferTrans.CR_PROD_ID, null);
//                            txMap.put(TransferTrans.CR_PROD_ID, closureMap.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("TRANS_MOD_TYPE","GL");
                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            //Added by Sreekrishnan
                            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                            if (listData != null && listData.size() > 0) {
                                HashMap map1 = (HashMap) listData.get(0);
                                if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "");
                                }else
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            }else{
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            }
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("generateSingleTransId", generateSingleCashId);
                            transactionTO.setTransAmt(new Double(intPay));
                            transactionDAO.setTransType(CommonConstants.DEBIT);
                            transactionDAO.addTransferCredit(txMap, intPay);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setBatchId(depositSubNo);
                            transTO.setTransAmt(new Double(intPay));
                            transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                            cashList = new ArrayList();
                            cashList.add(transTO);
                            if(intPay > 0){
                                transactionDAO.setScreenName(screenName);
                            transactionDAO.setInitiatedBranch(initiatedBranch);    
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            }                            
                        }
                        returnCalcMap.put("INT_PAY_AMT", new Double(paycreditInt));
                        returnCalcMap.put("INT_AMT", new Double(receiveInt));
                    } else {//all amount taking from intpaid to intpaylbe again debit intpayable to particular account...
                        System.out.println("intPay ELSE: ff" + intPay);
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                        txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                       
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        System.out.println("interestAmt else Receivable : " + txMap);
                        txMap.put("TRANS_MOD_TYPE", "TD");
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put("TRANS_MOD_TYPE", "TD");
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
						//Comm deposit provision
                       // transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                       // transactionDAO.doTransferLocal(transferList, branchID);

                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                        
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                        
                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                        }
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        System.out.println("interestAmt txMap Payable: " + txMap);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
						//Comm deposit provision
                      //  transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                       // transactionDAO.doTransferLocal(transferList, branchID);
                    }
                    returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                    returnCalcMap.put("INT_AMT", new Double(intPay));
                    if (map.containsKey("TransactionTO") && closureMap.containsKey("FULLY_CLOSING_STATUS")
                            && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        double depWithInterest = 0.0;
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                }
                                if (pay_bal_Int > 0) {
                                    intPay = intPay - pay_bal_Int;
                                }
                                if (lien != null && lien.length() > 0) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    depWithInterest = depAmt + intPay;
                                } else {
                                    depWithInterest = depAmt;
                                    depAmt = depAmt - intPay;
                                    System.out.println("No Lien depAmt:" + depAmt);
                                    System.out.println("depWithInterest s" + depWithInterest);
                                }
                                if (pay_bal_Int > 0) {
                                    depAmt = depAmt + pay_bal_Int;
                                }
                                System.out.println("depAmt check :"+depAmt);
                                txMap = new HashMap();
                                //transferList = new ArrayList(); // for local transfer
                                if (closedStatus.equals("NEW")) {
                                    if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        if (head.equals(direct)) {
                                            System.out.println("EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            //                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        } else {
                                            System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    System.out.println("closedStatus###" + closedStatus);
                                    //                                    txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("MATURITY_DEPOSIT"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                    System.out.println("TRANSFER depAmt:" + depAmt);
                                            //Added  by sreekrishnan
                                            double tr_Dr_amt=0;
                                            if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                double depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT     " + chargeMap);
                                                    System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
//                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
//                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                   // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                   // transferList.add(transferTo);
            //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                                }
                                            }
                                            //added by chithra for service Tax
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                System.out.println("serviceTaxDetails 3");
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax =0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }                                              
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    serTaxAmt -= swachhCess;
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    serTaxAmt -= krishikalyanCess;
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                               
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                    }
                                    if (transactionTO.getProductType().equals("TD")) {
                                        HashMap newDepMap = new HashMap();
                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                        if (lst != null && lst.size() > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                        }
                                    } else if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien Status : " + txMap);
                                    } else if (transactionTO.getProductType().equals("AD")) {
                                        // The following block uncommented by Rajesh
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put("TRANS_MOD_TYPE", "AD");
                                        System.out.println("*******Lien payable AD: " + txMap);
                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by chithra on 14-10-14
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());
                                       //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    }else if (transactionTO.getProductType().equals("TL")) {
                                      //added by rishad 17/dec/2018 
                                        txMap.put(TransferTrans.CR_AC_HD, (String)loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien payable TL: " + txMap);
                                    } else if (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                        System.out.println("$@#$@#$#@$#@$ sreeee"+acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        //                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                        //                                        ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put("TRANS_MOD_TYPE", "OA");
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        System.out.println("*******No Lien Status : " + txMap);
                                    } else if (transactionTO.getProductType().equals("RM")) {
                                        System.out.println("*******No Lien Status : " + transactionDetailsMap);
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                                        LinkedHashMap transferMap = new LinkedHashMap();
                                        HashMap remittanceMap = new HashMap();
                                        remittanceIssueDAO = new RemittanceIssueDAO();
                                        remittanceIssueTO = new RemittanceIssueTO();
                                        String favouringName = transactionTODebit.getDebitAcctNo();
                                        transactionTODebit.setApplName(favouringName);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                        transactionTODebit.setProductType("TD");
                                        transactionTODebit.setDebitAcctNo(depositSubNo);
                                        remittanceIssueDAO.setFromotherDAo(false);
                                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                        remittanceMap.put("TransactionTO", transferMap);
                                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                        remittanceMap.put("OPERATION_MODE", "ISSUE");
                                        remittanceMap.put("AUTHORIZEMAP", null);
                                        remittanceMap.put("USER_ID", map.get("USER_ID"));
                                        remittanceMap.put("MODULE", "Remittance");
                                        remittanceMap.put("MODE", "INSERT");
                                        remittanceMap.put("SCREEN", "Issue");
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remittanceIssueTO.setAmount(new Double(depAmt));
                                        remittanceIssueTO.setDraweeBranchCode(branchID);
                                        remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                        remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        remittanceIssueTO.setCity("560");
                                        remittanceIssueTO.setFavouring(favouringName);
                                        remittanceIssueTO.setBranchId(branchID);
                                        remittanceIssueTO.setRemarks(depositSubNo);
                                        remittanceIssueTO.setCommand("INSERT");
                                        remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                        remittanceIssueTO.setExchange(new Double(0.0));
                                        remittanceIssueTO.setPostage(new Double(0.0));
                                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                                        remittanceIssueTO.setStatusDt(curDate);
                                        remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                        remittanceIssueTO.setInstrumentNo1(instNo1);
                                        remittanceIssueTO.setInstrumentNo2(instNo2);
                                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        LinkedHashMap remitMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        remMap.put(String.valueOf(1), remittanceIssueTO);
                                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                        remittanceMap.put("RemittanceIssueTO", remitMap);
                                        System.out.println(" remittanceMap :" + remittanceMap);
                                        remittanceIssueDAO.execute(remittanceMap);
                                    }
                                    if (!transactionTO.getProductType().equals("RM")) {
                                        transactionDAO.execute(map);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        ///////////////////////////
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, depWithInterest-tr_Dr_amt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        if (transactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (transactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (transactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (transactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (transactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, depAmt-tr_Dr_amt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        //Added by chithra on 26-09-14-- FOR receivable and payable intrest transaction
                                        if (pay_bal_Int > 0) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("INT_PAY")));
                                            txMap.put(TransferTrans.PARTICULARS, "");
                                            txMap.put(TransferTrans.DR_ACT_NUM, "");
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "INTREST PAYABLE");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                            txMap.put("generateSingleTransId", generateSingleTransId);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, pay_bal_Int);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);                                           
                                        }
                                        //End-------
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.doTransfer();
                                    }
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                } else {
                                    if (map.containsKey("LTD")) {
                                        System.out.println("CASH depAmt:" + depAmt);
                                        //                                    transactionDAO.execute(map);
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt());
                                        txMap = new HashMap();
                                        transferList = new ArrayList(); // for local transfer
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                       
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        transactionTO.setTransAmt(new Double(depAmt));
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        transactionDAO.addTransferDebit(txMap, depAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(depAmt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        HashMap logMap = new HashMap();
                                        transactionDAO.setScreenName(screenName);
                                        logMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        logMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                                        logMap.put(CommonConstants.IP_ADDR, map.get(CommonConstants.IP_ADDR));
                                        logMap.put(CommonConstants.MODULE, map.get(CommonConstants.MODULE));
                                        logMap.put(CommonConstants.SCREEN, map.get(CommonConstants.SCREEN));
                                        transactionDAO.setInitialValuesForLogTO(logMap);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                    } else {                                        
                                        System.out.println("CASH depAmt:" + depAmt);
                                        //Added  by sreekrishnan
                                            double tr_Dr_amt=0;
                                            if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                double depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT     " + chargeMap);
                                                    System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                   
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
//                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
//                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                   // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                   // transferList.add(transferTo);
            //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                                }
                                            }
                                            //added by chithra for service Tax
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                System.out.println("serviceTaxDetails 4");
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                   
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                               
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                        txMap = new HashMap();
                                        transferList = new ArrayList(); // for local transfer
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        transactionTO.setTransAmt(new Double(cashdepAmt-tr_Dr_amt));
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        transactionDAO.addTransferDebit(txMap, cashdepAmt-tr_Dr_amt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(cashdepAmt-tr_Dr_amt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        //Added BY Suresh
                                        System.out.println("############## Deposit Closing Premature Receivable Amount >0 Issue : " + _branchCode);
                                        HashMap logMap = new HashMap();
                                        logMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        logMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                                        logMap.put(CommonConstants.IP_ADDR, map.get(CommonConstants.IP_ADDR));
                                        logMap.put(CommonConstants.MODULE, map.get(CommonConstants.MODULE));
                                        logMap.put(CommonConstants.SCREEN, map.get(CommonConstants.SCREEN));
                                        transactionDAO.setInitialValuesForLogTO(logMap);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        //Added by chithra on 26-09-14-- FOR receivable and payable intrest transaction
                                        if (pay_bal_Int > 0 && receiveInt>0) {
                                        txMap = new HashMap();
                                        transferList = new ArrayList(); 
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("INT_PAY")));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_ACT_NUM, "");
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        transactionTO.setTransAmt(pay_bal_Int);
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        transactionDAO.addTransferDebit(txMap, pay_bal_Int);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(pay_bal_Int);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        logMap = new HashMap();
                                        logMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        logMap.put(CommonConstants.BRANCH_ID, initiatedBranch);
                                        logMap.put(CommonConstants.IP_ADDR, map.get(CommonConstants.IP_ADDR));
                                        logMap.put(CommonConstants.MODULE, map.get(CommonConstants.MODULE));
                                        logMap.put(CommonConstants.SCREEN, map.get(CommonConstants.SCREEN));
                                        transactionDAO.setInitialValuesForLogTO(logMap);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        }
                                        //End------------------
                                        returnCalcMap.put("PAY_AMT", new Double(cashdepAmt));
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("FIXED Receivable COMPLETED...");
                }
            } else if ((closureMap.get("BEHAVES_LIKE").equals("FIXED") || closureMap.get("BEHAVES_LIKE").equals("BENEVOLENT"))
                    && closureMap.get("TRANSFER_OUT_MODE").equals("Y")) {
                System.out.println("FIXED Transfer Out Started...");
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO != null) {
                                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            }
                            HashMap transferBranchMap = new HashMap();
                            transferBranchMap.put("TRANSFER_OUT_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            lst = sqlMap.executeQueryForList("getSelectBranchList", transferBranchMap);
                            if (lst != null && lst.size() > 0) {
                                transferBranchMap = (HashMap) lst.get(0);
                            }

                            HashMap transOutMap = new HashMap();
                            transOutMap.put("DEPOSIT_NO", depositNo);
                            lst = sqlMap.executeQueryForList("getIntCrIntDrawn", transOutMap);
                            if (lst != null && lst.size() > 0) {
                                transOutMap = (HashMap) lst.get(0);
                                double totalIntCredit = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                double totalIntDrawn = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                interestAmt = totalIntCredit - totalIntDrawn;
                                System.out.println("interestAmt : " + interestAmt);
                            }
                            if (interestAmt > 0) {//excess amount recovering from paybale (is a mediator A/c)crediting paid
                                System.out.println("interestAmt>0 IF fd : " + interestAmt);
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                               
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                }
                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                System.out.println("interestAmt>0 Payable txMap fd : " + txMap);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered" + depositSubNo);
                                if (transactionTO.getProductType().equals("OA")) {
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                } else if (transactionTO.getProductType().equals("AB")) {
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                } else if (transactionTO.getProductType().equals("SA")) {
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                } else if (transactionTO.getProductType().equals("TL")) {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                } else if (transactionTO.getProductType().equals("AD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                returnCalcMap.put("INT_AMT", new Double(interestAmt));
                                interestAmt = 0.0;
                            }

                            if (closedStatus.equals("NEW")) {
                                if (lien != null && lien.length() > 0) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    System.out.println("Lien depAmt non fd:" + depAmt);
                                } else {
                                    depAmt = totalAmt - penalAmt;
                                    System.out.println("No Lien depAmt non fd:" + depAmt);
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                depAmt = depAmt;
                                System.out.println("MATURED Lien depAmt non fd:" + depAmt);
                            }
                            txMap = new HashMap();
                            transferList = new ArrayList(); // for local transfer
                            if (closedStatus.equals("NEW")) {
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                    System.out.println("Non fd GL :" + depAmt);
                                    if (head.equals(direct)) {
                                        System.out.println("EQUALS NON fd GL :" + depAmt);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    } else {
                                        System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else {
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            if (transactionTO.getTransType().equals("TRANSFER")) {
                                System.out.println("TRANSFER depAmt non fd: " + depAmt);
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    System.out.println("*******Lien Status : " + txMap);
                                }
                            }
                            if (!transactionTO.getProductType().equals("RM")) {
                                System.out.println("TRANSFER txMap : " + txMap);
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                /////////////////
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + depositSubNo + " " + transferBranchMap.get("Branch Name"));
                                if (transactionTO.getProductType().equals("OA")) {
                                    txMap.put("TRANS_MOD_TYPE", "OA");
                                } else if (transactionTO.getProductType().equals("AB")) {
                                    txMap.put("TRANS_MOD_TYPE", "AB");
                                } else if (transactionTO.getProductType().equals("SA")) {
                                    txMap.put("TRANS_MOD_TYPE", "SA");
                                } else if (transactionTO.getProductType().equals("TL")) {
                                    txMap.put("TRANS_MOD_TYPE", "TL");
                                } else if (transactionTO.getProductType().equals("AD")) {
                                    txMap.put("TRANS_MOD_TYPE", "AD");
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                }
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);

                                transactionTODebit.setTransAmt(new Double(depAmt));
                                map.put("TransactionTO", transactionDetailsMap);
                                transactionDAO.execute(map);
                                transactionDAO.doTransfer();
                            }
                            returnCalcMap.put("PAY_AMT", new Double(depAmt));
                            HashMap transferMap = new HashMap();
                            transferMap.put("DEPOSIT_NO", depositSubNo);
                            List lstTrans = sqlMap.executeQueryForList("getTransOutDepositDetails", transferMap);
                            if (lstTrans != null && lstTrans.size() > 0) {
                                transferMap = (HashMap) lstTrans.get(0);
                            }
                            HashMap transferOutMap = new HashMap();
                            transferOutMap.put("TRANS_ID", transferMap.get("BATCH_ID"));
                            transferOutMap.put("DEPOSIT_NO", depositNo);
                            transferOutMap.put("TRANS_DT", curDate);
                            transferOutMap.put("TRANS_AMT", new Double(depAmt));
                            transferOutMap.put("STATUS", closingStatus);
                            transferOutMap.put("STATUS_BY", map.get("USER_ID"));
                            transferOutMap.put("STATUS_DT", curDate);
                            transferOutMap.put("CREATED_BY", map.get("USER_ID"));
                            transferOutMap.put("CREATED_DT", curDate);
                            transferOutMap.put("TRANS_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            transferOutMap.put("INTER_TRANS_NO", getInter_TransID());
                            transferOutMap.put("CURRENT_BRANCH", branchID);
                            transferOutMap.put("TRANS_OUT_FLAG", "N");
                            returnMap.put("INTER_TRANS_NO", transferOutMap.get("INTER_TRANS_NO"));
                            System.out.println("transferOutMap :" + transferOutMap);
                            System.out.println("returnMap :" + returnMap);
                            sqlMap.executeUpdate("insertTransferOutDeposits", transferOutMap);
                            transferOutMap = null;
                            transferMap = null;
                            transferBranchMap = null;
                            transOutMap = null;
                        }
                    }
                }
                System.out.println("FIXED Transfer Out Started...");
            }
            //starting thrift  
            if ((closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") || closureMap.get("BEHAVES_LIKE").equals("RECURRING")
                    || closureMap.get("BEHAVES_LIKE").equals("DAILY")||closureMap.get("BEHAVES_LIKE").equals("THRIFT"))
                    && closureMap.containsKey("TRANSFER_OUT_MODE") && (closureMap.get("TRANSFER_OUT_MODE").equals("N") || closureMap.get("TRANSFER_OUT_MODE").equals("NO"))) {//CUMMULATIVE DEPOSIT HOLDERS..
                System.out.println("totalAmt hereee :: " + totalAmt +"  --- clrAmt " + clrAmt);
                if (totalAmt >= clrAmt || totalAmt < clrAmt) {//payable....
                    double serviceCharge = 0.0;
                    if (map.containsKey("TransactionTO")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                }
                                System.out.println("CUMMULATIVE Payable STARTED...");
                                System.out.println("interestAmt : balPay: " + balPay + "-" + interestAmt);
                                if (interestAmt > 0 || interestAmt < 0 || balPay > 0) {//first one amount taking from intpaylbe to intpaid
                                    double rateofInt = CommonUtil.convertObjToDouble(closureMap.get("CURR_RATE_OF_INT")).doubleValue();
                                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && rateofInt == 0) {
                                        System.out.println("rateofInt 0 IF non fd: " + rateofInt);
                                        interestAmt = balPay;
                                        balPay = 0.0;
                                    }
                                    if (interestAmt > 0) {
                                        System.out.println("interestAmt>0 IF non fd: " + interestAmt);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                      
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                     
                                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                        } else {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt txMap Payable: " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }
                                    if (interestAmt < 0) {
                                        interestAmt = interestAmt * -1;
                                        System.out.println("interestAmt<0 IF non fd: " + interestAmt);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt Payable 1sttxMap : " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }
                                    System.out.println("Before balPay>0 IF non fd: " + balPay);
                                    if (balPay > 0) {//first one amount taking from intpaylbe to particular account....
                                        System.out.println("balPay>0 IF non fd: " + balPay);
                                        //                            if(transactionTO.getProductType().equals("GL")){
                                        //                                System.out.println("*******Lien Status payable : "+txMap);
                                        //                            }else{
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                       
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// crediting to Operative account head......
                                        txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                        System.out.println("intPay txMap Payable: " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(balPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(balPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        //                            }
                                    }
                                    returnCalcMap.put("INT_PAY_AMT", new Double(balPay));
                                    returnCalcMap.put("INT_AMT", new Double(interestAmt));
                                } else {//all amount taking from intpaid to intpaylbe again debit intpayable to particular account...
                                    System.out.println("interestAmt ELSE non fd: " + intPay);
                                    //||closureMap.get("BEHAVES_LIKE").equals("BENEVOLENT") 29-12
                                    if((closureMap.get("BEHAVES_LIKE").equals("RECURRING")||closureMap.get("BEHAVES_LIKE").equals("DAILY")||closureMap.get("BEHAVES_LIKE").equals("THRIFT"))&&!transactionTO.getTransType().equals("CASH")){
                                       intPay = intPay-rec_recivableAmt; 
                                    }
                                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING")) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                        if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                                            intPay = intPay - blncLonamt;
                                        }
                                    }
                                    if (intPay > 0) {
                                        if ((closureMap.get("BEHAVES_LIKE").equals("DAILY") && !transactionTO.getTransType().equals("CASH")) || (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")
                                                || closureMap.get("BEHAVES_LIKE").equals("RECURRING") ||closureMap.get("BEHAVES_LIKE").equals("THRIFT"))) {
                                            txMap = new HashMap();
                                            transferList = new ArrayList(); // for local transfer
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                                            //Added By Suresh
                                            if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && acHeads.get("SERVICE_CHARGE_ALLOWED").equals("Y")
                                                    && !acHeads.get("SERVICE_CHARGE_ALLOWED").equals("") && servicePercentage > 0) {
                                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                                    if (mode.equals(CommonConstants.NORMAL_CLOSURE)) {
                                                        //Interest
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                                        //  txMap.put(TransferTrans.CR_ACT_NUM,CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                                        txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                                        if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("GL")) {
                                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                                        }
                                                    } else {
                                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_CHARGE_ACHD")); // credited to Service Charge Account Head......
                                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    }
                                                }
                                            } else {
                                                if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable") && intPay > 0) {
                                                    cumulativeReceivable = true;
                                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.PARTICULARS, "Excess Interest Paid Recollected");
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo); 
                                                }
                                                System.out.println("555555555");
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                                txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                            }
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                           
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            System.out.println(" cummulative TRANSFER PAYABLE COMMON insertAccHead TXMAP : " + txMap);
                                            txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                           
                                            if (!closureMap.get("BEHAVES_LIKE").equals("DAILY") && !cumulativeReceivable){
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));//By Nidhin
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo); 
                                            }
                                            if (closureMap.get("BEHAVES_LIKE").equals("THRIFT")){
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                 transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));//added by rishad
                                                 transferTo.setSingleTransId(generateSingleTransId);
                                                 transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                 transferList.add(transferTo); 
                                                 if(transactionTO.getTransType().equals("CASH")){
                                                 transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                                 transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                                 }
                                            }
                                            if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && add_int_amt > 0) { // Added by nithya on 10-12-2019 for KD-1048
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                txMap.put(TransferTrans.PARTICULARS, "Interest Amount-" + addDays +" " + depositSubNo);
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(add_int_amt));//By Nidhin
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                            }                                            
//                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));//By Nidhin
//                                            transferTo.setSingleTransId(generateSingleTransId);
//                                            transferList.add(transferTo);
//                                            txMap.put("TRANS_MOD_TYPE", "TD");
//                                            txMap.put(TransferTrans.PARTICULARS,"Interest Amount "+depositSubNo);
//                                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay)) ;
//                                            transferTo.setSingleTransId(generateSingleTransId);
//                                            transferList.add(transferTo);
//                                            }
                                        }	
                                        
                                        //Added By Suresh //Service Charge
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && mode.equals(CommonConstants.NORMAL_CLOSURE) && transactionTO.getTransType().equals("TRANSFER")
                                                && acHeads.get("SERVICE_CHARGE_ALLOWED").equals("Y") && !acHeads.get("SERVICE_CHARGE_ALLOWED").equals("") && servicePercentage > 0) {
                                            serviceCharge = 0.0;
                                            if (serviceChargeMap.containsKey("TOTAL_DEP_DAY_END_AMT") && serviceChargeMap.containsKey("DAILY_INT_CALC")) {
                                                //                                            serviceCharge = intPay*2/100;
                                                if (serviceChargeMap.get("DAILY_INT_CALC").equals("WEEKLY")) {
                                                    serviceCharge = CommonUtil.convertObjToDouble(serviceChargeMap.get("TOTAL_DEP_DAY_END_AMT")).doubleValue() * 7 * servicePercentage / 36500;
                                                } else {
                                                    serviceCharge = CommonUtil.convertObjToDouble(serviceChargeMap.get("TOTAL_DEP_DAY_END_AMT")).doubleValue() * servicePercentage / 1200;
                                                }
//                                            serviceCharge = clrAmt*servicePercentage/100;
                                                Rounding rod = new Rounding();
                                                serviceCharge = (double) rod.getNearest((long) (serviceCharge * 100), 100) / 100;
                                                System.out.println("##########   serviceCharge : " + serviceCharge);
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_CHARGE_ACHD")); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Service Charge " + depositSubNo);
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                                                if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("GL")) {
                                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                                }
                                                txMap.put(TransferTrans.PARTICULARS, "Service Charge  " + depositSubNo);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(serviceCharge));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferList.add(transferTo);
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(serviceCharge));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                            }
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                                        transactionDAO.doTransferLocal(transferList, branchID);
                                        returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                                        returnCalcMap.put("INT_AMT", new Double(intPay));
                                    }	
                                    //Added By Chithra
                                    if (rec_recivableAmt > 0 &&(closureMap.get("BEHAVES_LIKE").equals("RECURRING")||closureMap.get("BEHAVES_LIKE").equals("DAILY")||closureMap.get("BEHAVES_LIKE").equals("THRIFT"))) {                                        
                                        txMap = new HashMap();
                                        transferList = new ArrayList(); // for local transfer
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        //txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));// Changed by nithya on 07-02-2017 for 5664
                                        //txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID")); // Commented by nithya on 07-02-2017 for 5664
                                        //txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);// Commented by nithya on 07-02-2017 for 5664
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        //txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);// Commented by nithya on 07-02-2017 for 5664
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);// Changed by nithya on 07-02-2017 for 5664
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(rec_recivableAmt));//By Nidhin
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                    }
                                }
                                if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && penalAmt > 0) {
                                    System.out.println("penalAmt  non fd:" + penalAmt);
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                                    txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD")); // credited to  interest Provisiniong Account head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, penalAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    returnCalcMap.put("PENAL_AMT", new Double(penalAmt));
                                }
                                /*if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && AgentCommAmt > 0) {// Commented the block of code for agent commission transaction by nithya for GST implementation for Agent Commission along with charges
                                    System.out.println("AgentCommAmt  non fd:" + AgentCommAmt);
                                    System.out.println("Executing here for agent commission.....");
                                    txMap = new HashMap();
                                    System.out.println("inside beaves like daily0011 ");
                                    transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                                    txMap.put(TransferTrans.PARTICULARS,"Penalty - Delayed Amount");
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    ///////////////////////////////////
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");

                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD")); // credited to  interest Provisiniong Account head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, AgentCommAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                
                                    //System.out.println("ist trans ID"+generateSingleTransId);
                                    //Tranfer trnas partic edited Pending Interest TO Agent Commision
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.PARTICULARS,"Agent Commision "+depositSubNo);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo =  transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt) ;
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                   
                                    
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                   // System.out.println("Transfer LISTS----))))"+transferList);
                                   // transactionDAO.doTransferLocal(transferList, branchID);
                                    returnCalcMap.put("PENAL_AMT",new Double(AgentCommAmt));
                                }*/

                                System.out.println("Before if tdsAmt  non fd:" + tdsAmt);
                                if (tdsAmt > 0) {
                                    System.out.println("tdsAmt  non fd:" + tdsAmt);
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.PARTICULARS, "Tds Deduction" + depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) closureMap.get("TDS_ACHD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);

                                    System.out.println("*******tds details Status : " + txMap);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tdsAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    returnCalcMap.put("TDS_AMT", new Double(tdsAmt));
                                }
                                //                    if(map.containsKey("TransactionTO")){
                                //                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                //                        LinkedHashMap allowedTransDetailsTO;
                                //                        if (transactionDetailsMap.size()>0)
                                //                            if(transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")){
                                //                                allowedTransDetailsTO = (LinkedHashMap)transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                //                                if(allowedTransDetailsTO!=null){
                                //                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                //                                }
                                if (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                        && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                                    if (closedStatus.equals("NEW")) {
                                        if (lien != null && lien.length() > 0) {
                                            depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                            System.out.println("Lien depAmt non fd:" + depAmt);
                                        } else {
                                            depAmt = totalAmt - penalAmt - AgentCommAmt;
                                            System.out.println("No Lien depAmt non fd:" + depAmt);
                                        }
                                    } else if (closedStatus.equals("MATURED")) {
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                        depAmt = depAmt;
                                        System.out.println("MATURED Lien depAmt non fd:" + depAmt);
                                    }
                                } else {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    System.out.println("Lien Status fd depAmt:" + depAmt);
                                }
                                 //added by Chithra
                                if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {                                    
                                    double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    depAmt = depAmt - ser_tax_Amt;
                                    }
                                //End
                                txMap = new HashMap();
                                // transferList = new ArrayList();
                                if (closedStatus.equals("NEW")) {
                                    if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        System.out.println("Non fd GL :" + depAmt);
                                        if (head.equals(direct)) {
                                            System.out.println("EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            //                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        } else {
                                            System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    //txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("MATURITY_DEPOSIT"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                System.out.println("TRANSFER  non fd:" + txMap);
                                //Nidhin 26-05-2014
                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                    System.out.println("TRANSFER depAmt non fd:>>><<<" + depAmt);
                                                      //Added  by sreekrishnan
                                            if (!closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                                double tr_Dr_amt1=0;                                            
                                            if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                double depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT   in recurring  " + chargeMap);
                                                    System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
    //                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
    //                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                   // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    tr_Dr_amt1=tr_Dr_amt1+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                   // transferList.add(transferTo);
            //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                                }
                                            }
                                            //added by chithra for service Tax
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                System.out.println("serviceTaxDetails 5");
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                               
                                                tr_Dr_amt1 = tr_Dr_amt1 + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                //System.out.println("rish@@@@@@@@@@@@@@@@"+tr_Dr_amt1);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt1);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                        }
                                }
                                    if (transactionTO.getProductType().equals("TD")) {
                                        HashMap newDepMap = new HashMap();
                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                        if (lst != null && lst.size() > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                        }
                                    } else if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien  non fd: " + txMap);
                                    } else if (transactionTO.getProductType().equals("AD")) {
                                        // The following block uncommented by Rajesh
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien payable AD : " + txMap);
                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by chithra on 14-10-14
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());              
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());
                                      //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    }else if (transactionTO.getProductType().equals("TL")) {
                                        //added by rishad 17/dec/2018 
                                        txMap.put(TransferTrans.CR_AC_HD, (String)loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien payable TL : " + txMap);
                                    } else if (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        //                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                        //                                        ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put("TRANS_MOD_TYPE",transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        System.out.println("*******No Lien  non fd: " + txMap);
                                    } else if (transactionTO.getProductType().equals("RM")) {
                                        //Added By Suresh
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            depAmt -= debitInt;
                                        }
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                                        LinkedHashMap transferMap = new LinkedHashMap();
                                        HashMap remittanceMap = new HashMap();
                                        remittanceIssueDAO = new RemittanceIssueDAO();
                                        remittanceIssueTO = new RemittanceIssueTO();
                                        String favouringName = transactionTODebit.getDebitAcctNo();
                                        transactionTODebit.setApplName(favouringName);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                        transactionTODebit.setProductType("TD");
                                        transactionTODebit.setDebitAcctNo(depositSubNo);
                                        remittanceIssueDAO.setFromotherDAo(false);
                                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                        remittanceMap.put("TransactionTO", transferMap);
                                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                        remittanceMap.put("OPERATION_MODE", "ISSUE");
                                        remittanceMap.put("AUTHORIZEMAP", null);
                                        remittanceMap.put("USER_ID", map.get("USER_ID"));
                                        remittanceMap.put("MODULE", "Remittance");
                                        remittanceMap.put("MODE", "INSERT");
                                        remittanceMap.put("SCREEN", "Issue");
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remittanceIssueTO.setAmount(new Double(depAmt));
                                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                        remittanceIssueTO.setDraweeBranchCode(branchID);
                                        remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        remittanceIssueTO.setCity("560");
                                        remittanceIssueTO.setFavouring(favouringName);
                                        remittanceIssueTO.setBranchId(branchID);
                                        remittanceIssueTO.setRemarks(depositSubNo);
                                        remittanceIssueTO.setCommand("INSERT");
                                        remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                        remittanceIssueTO.setExchange(new Double(0.0));
                                        remittanceIssueTO.setPostage(new Double(0.0));
                                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                                        remittanceIssueTO.setStatusDt(curDate);
                                        remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                        remittanceIssueTO.setInstrumentNo1(instNo1);
                                        remittanceIssueTO.setInstrumentNo2(instNo2);
                                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        LinkedHashMap remitMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        remMap.put(String.valueOf(1), remittanceIssueTO);
                                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                        remittanceMap.put("RemittanceIssueTO", remitMap);
                                        System.out.println(" remittanceMap :" + remittanceMap);
                                        remittanceIssueDAO.execute(remittanceMap);
                                    }
                                    if (!transactionTO.getProductType().equals("RM")) {
                                        //Added By Suresh
                                        double depAmtwitint = depAmt;
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY") || closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                           //modified by rishad 18/11/2016 mantis 0005443
                                            // depAmt -= debitInt;
                                            if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {                                                
                                                depAmt += intPay;                                                
                                            }else{
                                            depAmt-=intPay;
                                        }
                                        }
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            depAmt = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_BALANCE"));
                                            double tot = getTotalAmounts();
                                            depAmt -= tot;
                                            if (AgentCommAmt > 0) {
                                                depAmt -= AgentCommAmt;
                                            }
                                            if (lien != null && lien.length() > 0) {
                                                if (depAmt > loan_amount) {
                                                    depAmt = depAmt - loan_amount;
                                                } else {
                                                    depAmt = 0;
                                                }
                                            }
                                        }
                                        if (closureMap.get("BEHAVES_LIKE").equals("RECURRING")){
                                            depAmt -= debitInt;
                                            if(rec_recivableAmt>0){
                                                depAmt =depAmt+rec_recivableAmt;
                                            }
                                        }
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")){
                                               depAmtwitint=depAmt;
                                        }                                        
                                        if (!closureMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                            if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {                                                
                                                double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                depAmt = depAmt - ser_tax_Amt;
                                            }
                                        } 
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        ///////////////////
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        double amt = 0;
                                        if (closureMap.containsKey("BEHAVES_LIKE") && closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            amt = depAmt + intPay;
                                            if (rec_recivableAmt > 0) {
                                                amt = depAmt - rec_recivableAmt;
                                            }

                                        }
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put("LINK_BATCH_ID",depositSubNo);
                                        //System.out.println("debit amount...... NITHYA before " + depAmt);                                        
                                        if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && lien != null && lien.length() > 0) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                            if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {                                                
                                                double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                depAmtwitint = depAmtwitint + ser_tax_Amt;
                                            }
                                        } 
                                        if(closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && add_int_amt > 0){ // Added by nithya on 10-12-2019 for KD-1048
                                            depAmtwitint += add_int_amt;
                                        }
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, depAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        if (transactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (transactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (transactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (transactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                            
                                        } else if (transactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            if (transactionTO.getProductType().equals("TL")) {
                                                 transactionDAO.setLinkBatchID(null);
                                                txMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                            }
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, amt);
                                        } else {
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch); 
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, depAmtwitint);
                                        }
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        if (transactionTO.getProductType().equals("TL")) {
                                            HashMap transLoanMap = interestCalculationTLAD(transactionTODebit.getDebitAcctNo(), transactionTODebit.getProductId(),transactionTO.getProductType());
                                             double service_taxAmt = 0;
                                                HashMap serviceTaxMap = new HashMap();
                                                if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                                                    serviceTaxDetailsForLoan = (HashMap) map.get("serviceTaxDetails");
                                                    if (serviceTaxDetailsForLoan.containsKey("TOT_TAX_AMT") && serviceTaxDetailsForLoan.get("TOT_TAX_AMT") != null) {
                                                          transLoanMap.put("TOT_SER_TAX_AMT", serviceTaxDetailsForLoan.get("TOT_TAX_AMT"));
                                                          transLoanMap.put("SER_TAX_HEAD", serviceTaxDetailsForLoan.get("TAX_HEAD_ID"));
                                                          transLoanMap.put("SER_TAX_MAP", serviceTaxDetailsForLoan);
                                                    }                                                   
                                                }
                                            transactionDAO.setLoanAmtMap(transLoanMap);
                                            transactionDAO.setLoanDebitInt("DP");
                                        }
                                        if (!closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        transactionDAO.doTransfer();
                                        
                                    }
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                //Transafer Entry
                                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                        txMap = new HashMap();
                                        // transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting to int paid a/c head
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        System.out.println("debt tx" + txMap);
                                        if (intPay > 0) {
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                             txMap.put("LINK_BATCH_ID",depositSubNo);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                        }
                                        
                                        String depositChrgHead = "";
                                        //transferList = new ArrayList();
                                        double depositClosingCharge = 0.0;
                                        double tr_Dr_amt=0;
                                        if (chargeLst != null && chargeLst.size() > 0) {                                            
                                            for (int i = 0; i < chargeLst.size(); i++) {
                                                depositClosingCharge = 0.0;
                                                HashMap chargeMap = new HashMap();
                                                String accHead = "";
                                                double chargeAmt = 0;
                                                String chargeType = "";
                                                chargeMap = (HashMap) chargeLst.get(i);
                                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                    System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT     " + chargeMap);
                                                    System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                                    System.out.println("account Headd" + depositChrgHead);
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                     txMap.put("LINK_BATCH_ID",depositSubNo);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                     transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                                    
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                                    txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);                                                   
                                                    tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                    transferTo.setSingleTransId(generateSingleTransId);                                                   
                                                }
                                            }
                                        }  
                                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                                System.out.println("serviceTaxDetails 6");
                                                double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }                                               
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                     txMap.put("LINK_BATCH_ID",depositSubNo);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                    
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    txMap.put("LINK_BATCH_ID",depositSubNo);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                    
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    txMap.put("LINK_BATCH_ID",depositSubNo);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                                
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                           if (AgentCommAmt > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD")); // credited to Service Charge Account Head......
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");                                            
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, "Agent Commision " + transactionTO.getDebitAcctNo());
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            txMap.put("LINK_BATCH_ID",depositSubNo);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            returnCalcMap.put("PENAL_AMT",new Double(AgentCommAmt));
                                            tr_Dr_amt = tr_Dr_amt + AgentCommAmt;
                                           }
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));//Added by nithya on 26-09-2019 for KD-624
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            txMap.put("LINK_BATCH_ID",depositSubNo);
                                            // Added by nithya on 07-01-2019 for KD 361 - Group Deposit Closure- While Doing Group Deposit Cloure Transfer nit Tally.
                                            if(depAmt < 0){
                                                tr_Dr_amt = tr_Dr_amt + depAmt;
                                            }
                                            // End
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                        if (transactionTO.getProductType().equals("TL")) {
                                            transferList.add("DEPOSIT_TO_LOAN");
                                        }
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);

                                        System.out.println("transactionTOers+B In trnasdfer Entrry" + transactionTO.getTransType());
                                        List chargeLsts = null;
                                    }
                                } else if (transactionTO.getTransType().equals("CASH")) {
                                    System.out.println("CASH depAmt non  fd:" + depAmt);
                                    System.out.println("closureMap.get(BEHAVES_LIKE)"+closureMap.get("BEHAVES_LIKE"));
                                    transactionDAO.execute(map);
                                    //Added BY Suresh
                                    //modified by rishad 04/04/2014
                                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                        System.out.println("depAmt -= debitInt"+depAmt + "  : intPay : "+ intPay +" debitInt : "+debitInt);
//                                        if(closureMap.containsKey("AGENT_COMMISION_AMT") && CommonUtil.convertObjToDouble(closureMap.get("AGENT_COMMISION_AMT")) < 0){
                                        depAmt -= intPay;   // Total Amount-Interest Amount PAY_AMT
//                                        }
                                        System.out.println("## BEHAVES_LIKE_DAILY  CASH depAmt non fd:" + depAmt);
                                    }
                                     if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                         if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                         double amount=intPay-prev_interst;
                                              depAmt += amount;
                                         }else{
                                              double amount=intPay-prev_interst;
                                         depAmt -= amount;   // Total Amount-Interest Amount
                                         }
                                        System.out.println("## BEHAVES_LIKE_DAILY  CASH depAmt non fd: in cumalative" + depAmt);
                                    }
                                     if (closureMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                         depAmt = clrAmt;
                                     }
                                     if(closureMap.get("BEHAVES_LIKE").equals("DAILY") && rec_recivableAmt>0){
                                         depAmt = clrAmt; 
                                         intPay=0;
                                     }
                                    System.out.println("## txMap  CASH depAmt hh nn  nn non fd:" + txMap);
                                    txMap = new HashMap();                                    
                                    transferList = new ArrayList(); // for local transfer
                                        //Added  by sreekrishnan
                                        double tr_Dr_amt=0;
                                      if (!closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                        if (chargeLst != null && chargeLst.size() > 0) {                                            
                                        for (int i = 0; i < chargeLst.size(); i++) {
                                            double depositClosingCharge = 0.0;
                                            HashMap chargeMap = new HashMap();
                                            String accHead = "";
                                            double chargeAmt = 0;
                                            String chargeType = "";
                                            chargeMap = (HashMap) chargeLst.get(i);
                                            if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT   in recurring  " + chargeMap);
                                                System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                ////////////////////////////////
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleCashId);
//                                                  txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                txMap.put("TRANS_MOD_TYPE", "TD");
//                                                  txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                               // transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                tr_Dr_amt=tr_Dr_amt+CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                               // transferList.add(transferTo);
        //                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        //                                    transactionDAO.doTransferLocal(transferList, branchID);
                                            }
                                        }
                                        //added by chithra for service Tax
                                        if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                            System.out.println("serviceTaxDetails 7");
                                            double swachhCess = 0.0;
                                                double krishikalyanCess = 0.0;
                                                double serTaxAmt = 0.0;
                                                double totalServiceTaxAmt = 0.0;
                                                double normalServiceTax = 0.0;
                                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                                }
                                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                                }
                                                if (swachhCess > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(krishikalyanCess > 0){
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);                                                  
                                                }
                                                if(normalServiceTax > 0) {
                                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    transferList.add(transferTo);
                                                }                                           
                                            tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                            transferTo.setSingleTransId(generateSingleTransId);
                                        }
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, tr_Dr_amt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                    
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }
                                }
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                    txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put("generateSingleTransId", generateSingleCashId);      
                                    boolean depositLienExist = false;
                                    if(closureMap.get("BEHAVES_LIKE").equals("DAILY") && isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        System.out.println("Executing here");
                                        transactionTO.setTransAmt(CommonUtil.convertObjToDouble(closureMap.get("TOTAL_BALANCE")));
                                        transactionDAO.setTransType(CommonConstants.CREDIT);
                                        transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(closureMap.get("TOTAL_BALANCE")));
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        System.out.println("transactionto.get" + transTO.getTransId());
                                        transTO.setTransAmt(CommonUtil.convertObjToDouble(closureMap.get("TOTAL_BALANCE")));
                                    }else{                                        
                                        if(closureMap.get("BEHAVES_LIKE").equals("RECURRING") && lien != null && lien.length() > 0){// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                            //System.out.println("lien... " + lien);                                          
                                               //System.out.println("depAmt... before cash"+ depAmt);
                                               //System.out.println("transamt... before cash"+ transactionTO.getTransAmt());
                                               depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue()+tr_Dr_amt;
                                               //System.out.println("depAmt... before after "+ depAmt);
                                               if (blncLonamt <= 0) {
                                                   //System.out.println("depAmt... before cash inside if :"+ depAmt+" intPay : "+ intPay);
                                                   depAmt = depAmt - intPay - add_int_amt;
                                                   System.out.println("depAmt... after cash inside if"+ depAmt);
                                               } else {
                                                   depositLienExist = true;
                                                   depAmt = 0;
                                               }                                            
                                        }
                                        //System.out.println("depositLienExist ... "+ depositLienExist);
                                        if(!depositLienExist){
                                        transactionTO.setTransAmt(new Double(depAmt - tr_Dr_amt));
                                        transactionDAO.addTransferDebit(txMap, depAmt - tr_Dr_amt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        System.out.println("transactionto.get" + transTO.getTransId());
                                        transTO.setTransAmt(new Double(depAmt - tr_Dr_amt));
                                        }
                                    }
                                    
                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    cashList = new ArrayList();
                                    cashList.add(transTO);
                                    transactionDAO.setScreenName(screenName);
                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                     if(!depositLienExist){// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                     }
                                   // System.out.println("recivable amount.................."+rec_recivableAmt);
                                     //Added By Chithra 10319: Weekly RD customisation 
                                    if (closureMap.containsKey("BEHAVES_LIKE") && closureMap.get("BEHAVES_LIKE").equals("RECURRING") && rec_recivableAmt > 0
                                            || closureMap.get("BEHAVES_LIKE").equals("THRIFT") && rec_recivableAmt > 0||closureMap.containsKey("BEHAVES_LIKE") && closureMap.get("BEHAVES_LIKE").equals("DAILY") && rec_recivableAmt > 0) {
                                        transactionDAO.execute(map);
                                        txMap = new HashMap();
                                        transferList = new ArrayList(); 
                                        //txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));// Added by nithya on 28-10-2020 for KD-2372
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        //txMap.put(TransferTrans.CR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        if (listData != null && listData.size() > 0) {
                                            HashMap map1 = (HashMap) listData.get(0);
                                            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "");
                                            } else {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            }
                                        } else {
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        }
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        transactionTO.setTransAmt(new Double(rec_recivableAmt));
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, rec_recivableAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(rec_recivableAmt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        if (rec_recivableAmt > 0) {
                                            transactionDAO.setScreenName(screenName);
                                            transactionDAO.setInitiatedBranch(initiatedBranch);
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                        }
                                    }
                                    //Added By Nidhin 6-03-2014
                                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && debitInt > 0) {
                                    txMap = new HashMap();
                                    transferList = new ArrayList(); // for local transfer
                                    System.out.println("##### INTEREST_DEBIT_START RD" + intPay);
                                      //  System.out.println("");
                                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING")) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                        if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                                            debitInt = intPay ;
                                        }
                                    }
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put("generateSingleTransId", generateSingleCashId);
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure-Interest");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    transactionDAO.addTransferDebit(txMap, debitInt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setBatchId(depositSubNo);
                                    transTO.setTransAmt(new Double(debitInt));
                                   // System.out.println("transactionto.get1"+transTO.getTransId());
                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    cashList = new ArrayList();
                                    cashList.add(transTO);
                                    transactionDAO.setScreenName(screenName);
                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    System.out.println("##### INTEREST_DEBIT_END RD");
                                    }
                                    //Added By Suresh
                                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && intPay > 0) {
                                        depAmt -= debitInt;   // Total Amount-Interest Amount
                                        System.out.println("## BEHAVES_LIKE_DAILY  CASH depAmt non fd:" + depAmt);
                                        txMap = new HashMap();
                                        System.out.println("##### INTEREST_DEBIT_START" + intPay);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "TD");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
//                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure-Interest");
//                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        transactionDAO.addTransferDebit(txMap, intPay);
                                        //edit
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(intPay));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        System.out.println("transactionto.get2"+transTO.getTransId());
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        System.out.println("##### INTEREST_DEBIT_END");
                                        //Service Charge
                                        if (acHeads.get("SERVICE_CHARGE_ALLOWED").equals("Y") && !acHeads.get("SERVICE_CHARGE_ALLOWED").equals("") && servicePercentage > 0) {
                                            if (mode.equals(CommonConstants.NORMAL_CLOSURE)) {
                                                serviceCharge = 0.0;
//                                            serviceCharge = intPay*2/100;
//                                            serviceCharge = clrAmt*2/100;
//                                                serviceCharge = clrAmt*servicePercentage/100;
                                                if (serviceChargeMap.containsKey("TOTAL_DEP_DAY_END_AMT") && serviceChargeMap.containsKey("DAILY_INT_CALC")) {
                                                    if (serviceChargeMap.get("DAILY_INT_CALC").equals("WEEKLY")) {
                                                        serviceCharge = CommonUtil.convertObjToDouble(serviceChargeMap.get("TOTAL_DEP_DAY_END_AMT")).doubleValue() * 7 * servicePercentage / 36500;
                                                    } else {
                                                        serviceCharge = CommonUtil.convertObjToDouble(serviceChargeMap.get("TOTAL_DEP_DAY_END_AMT")).doubleValue() * servicePercentage / 1200;
                                                    }
                                                    Rounding rod = new Rounding();
                                                    serviceCharge = (double) rod.getNearest((long) (serviceCharge * 100), 100) / 100;
                                                    System.out.println("##########   serviceCharge : " + serviceCharge);
                                                }
                                            } else {
                                                serviceCharge = intPay;
                                            }
                                            //CREDIT SERVICE_CHARGE_AMOUNT
                                            if (serviceCharge > 0) {
                                                txMap = new HashMap();
                                                transactionDAO.setTransType("DEBIT");
                                                System.out.println("##### SERVICE_CHARGE_START" + serviceCharge);
                                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("SERVICE_CHARGE_ACHD"));// crediting to Service Charge account head......
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put("generateSingleTransId", generateSingleCashId);
                                                txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure-Service Charge");
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "TD");                                               
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                transactionDAO.addTransferCredit(txMap, serviceCharge);
                                                transactionDAO.deleteTxList();
                                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                transTO.setTransType("CASH");
                                                transTO.setBatchId(depositSubNo);
                                                transTO.setTransAmt(new Double(serviceCharge));
                                                transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                              //  System.out.println("transactionto.get3"+transTO.getTransId());
                                                cashList = new ArrayList();
                                                cashList.add(transTO);
                                                transactionDAO.setScreenName(screenName);
                                                transactionDAO.setInitiatedBranch(initiatedBranch);
                                                transactionDAO.addCashList(cashList);
                                                transactionDAO.doTransfer();
                                              //  System.out.println("##### SERVICE_CHARGE_END");
                                            }
                                        }
                                    }
                                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                        if (AgentCommAmt > 0 ) {
                                            System.out.println("interestAmt>0 IF non fd:asasas " + AgentCommAmt);
                                            //txMap = new HashMap();
                                            //transferList = new ArrayList();                                           
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                            System.out.println("interestAmt txMap Payable: " + txMap);
                                            txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            if (transactionTO.getTransType().equals("CASH") && isTransferCharges.equalsIgnoreCase("N")) {// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                                transactionDAO.setTransType(CommonConstants.DEBIT);
                                                transactionDAO.addTransferCredit(txMap, AgentCommAmt);
                                                transactionDAO.deleteTxList();
                                                transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                transTO.setTransType(CommonConstants.TX_CASH);
                                                transTO.setBatchId(depositSubNo);
                                                transTO.setTransAmt(AgentCommAmt);
                                                transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                cashList = new ArrayList();
                                                cashList.add(transTO);
                                                transactionDAO.setScreenName(screenName);
                                                transactionDAO.setInitiatedBranch(initiatedBranch);
                                                System.out.println("cashList...agent commission :: " + cashList);
                                                transactionDAO.addCashList(cashList);
                                                transactionDAO.doTransfer();
                                            } else {
                                                transferList.add(transferTo);
                                            }

//                                        if((!closureMap.containsKey("Charge List Data")) || chargeLst != null){
//                                        txMap.put(TransferTrans.DR_ACT_NUM,depositSubNo);
//                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));//debiting Deposit a/c head
//                                                                                   
//                                        txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
//                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
//                                        txMap.put(TransferTrans.CURRENCY, "INR");
//                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                        txMap.put("TRANS_MOD_TYPE", "TD");
//                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
//                                        //added by vivek
//                                         System.out.println(" txMap.get(TransferTrans.DR_ACT_NUM)"+txMap.get(TransferTrans.DR_ACT_NUM));
//                                         transferTo = transactionDAO.addTransferDebitLocal(txMap, AgentCommAmt);
//                                        transferTo.setSingleTransId(generateSingleTransId);
//                                        transferList.add(transferTo);
//
//                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                                        transactionDAO.doTransferLocal(transferList, branchID);
//                                        }
                                        }
                                        //commented by rishad 01/06/2015
//                                        else if (AgentCommAmt > 0 && closureMap.get("Charge List Data") == null) {//By Nidhin
//                                        System.out.println("interestAmt>0 IF non fd:asasas >0 " + AgentCommAmt);
//                                        //txMap = new HashMap();
//                                        //transferList = new ArrayList();
//                                        System.out.println("mode.equals(CommonConstants.PREMATURE_CLOSURE)333>>>" + mode.equals(CommonConstants.PREMATURE_CLOSURE));
//                                        System.out.println("map.containsKey(\"PAYORRECEIVABLE\")333>>>" + map.containsKey("PAYORRECEIVABLE"));
//                                        System.out.println("map.get(\"PAYORRECEIVABLE\")3333>>>" + map.get("PAYORRECEIVABLE"));
//                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD"));
//                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
//                                        txMap.put(TransferTrans.CURRENCY, "INR");
//                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                        System.out.println("interestAmt txMap Payable: " + txMap);
//                                        txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
//                                        txMap.put("TRANS_MOD_TYPE", "TD");
//                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt);
//                                        transferTo.setSingleTransId(generateSingleTransId);
//                                        transferList.add(transferTo);
//                                        
//                                        txMap.put(TransferTrans.DR_ACT_NUM,depositSubNo);
//                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));//debiting Deposit a/c head
//                                                                                   
//                                        txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
//                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
//                                        txMap.put(TransferTrans.CURRENCY, "INR");
//                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                        txMap.put("TRANS_MOD_TYPE", "TD");
//                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
//                                        //added by vivek
//                                         System.out.println(" txMap.get(TransferTrans.DR_ACT_NUM)"+txMap.get(TransferTrans.DR_ACT_NUM));
//                                         transferTo = transactionDAO.addTransferDebitLocal(txMap, AgentCommAmt);
//                                        transferTo.setSingleTransId(generateSingleTransId);
//                                        transferList.add(transferTo);
//                                        System.out.println("Agent commisionn Completed");
//                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                                        transactionDAO.doTransferLocal(transferList, branchID);
//                                    }
                                    }
                    //Edit NIDIHIN
                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                        System.out.println("transactionTOers+BehaveseLike" + transactionTO.getTransType());
                        List chargeLsts = null;
                        String depositChrgHead = "";
                        //transferList = new ArrayList();
                        double depositClosingCharge = 0.0;
                        double extraAgentAmt = 0.0;
                        if(closureMap.get("Charge List Data") != null || closureMap.get("Charge List Data") == null){
                            extraAgentAmt = AgentCommAmt;
                        }
                        if (chargeLst != null && chargeLst.size() > 0) {
                            double tot_Dr_amt=0;
                            for (int i = 0; i < chargeLst.size(); i++) {
                                depositClosingCharge = 0.0;
                                HashMap chargeMap = new HashMap();
                                String accHead = "";
                                double chargeAmt = 0;
                                String chargeType = "";
                                chargeMap = (HashMap) chargeLst.get(i);
                                if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                    System.out.println("CHARGE_AMOUNT_CHARGE_AMOUNT     " + chargeMap);
                                    System.out.println("##########   serviceCharge : " + depositClosingCharge);
                                    System.out.println("account Headd" + depositChrgHead);
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));                                        
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleTransId);
//                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
//                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put("TRANS_MOD_TYPE", "TD");                                       
                                    }                                   
                                     chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                     chargeAmt += extraAgentAmt;
                                     tot_Dr_amt = tot_Dr_amt + chargeAmt;
                                    //transferTo = transactionDAO.addTransferDebitLocal(txMap,chargeAmt);
                                   // transferTo.setSingleTransId(generateSingleTransId);
                                  //  transferList.add(transferTo);
                                    //transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    //transactionDAO.doTransferLocal(transferList, branchID);
                                    extraAgentAmt = 0.0;
                                }
                            }
						//added by chithra for service Tax
                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                System.out.println("serviceTaxDetails 8");
                                double swachhCess = 0.0;
                                double krishikalyanCess = 0.0;
                                double serTaxAmt = 0.0;
                                double totalServiceTaxAmt = 0.0;
                                double normalServiceTax =0.0;
                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                }
                                if (swachhCess > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap,swachhCess);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(swachhCess);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        //  System.out.println("transactionto.get4"+transTO.getTransId());
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                         transferList.add(transferTo);
                                    }   
                                }
                                if (krishikalyanCess > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap,krishikalyanCess);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(krishikalyanCess);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));                                      
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                        transferList.add(transferTo);
                                    } 
                                }
                                if (normalServiceTax > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if (isTransferCharges.equalsIgnoreCase("N")) {// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, normalServiceTax);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(normalServiceTax);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                        transferList.add(transferTo);
                                    }                                   
                                }                                
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                tot_Dr_amt = tot_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                extraAgentAmt = 0.0;
                            }
                             if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                
                            }else{
                                 txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                 txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                 txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                 txMap.put(TransferTrans.CURRENCY, "INR");
                                 txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                 txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                 txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                 txMap.put("generateSingleTransId", generateSingleTransId);
//                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                 txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
//                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                 txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                 txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                 txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                 transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_Dr_amt);
                                 transferTo.setSingleTransId(generateSingleTransId);
                                 transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                 transferList.add(transferTo);
                                 transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                 transactionDAO.doTransferLocal(transferList, initiatedBranch);
                             }                                        
                        } else {
                            depositClosingCharge = 0.0;
                            String accHead = "";
                            double chargeAmt = 0;
                            String chargeType = "";
                            double totalServiceTaxAmt = 0.0;
                            if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                System.out.println("serviceTaxDetails agent commission");
                                double swachhCess = 0.0;
                                double krishikalyanCess = 0.0;
                                double serTaxAmt = 0.0;                                
                                double normalServiceTax =0.0;
                                if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                    serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                    swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                    krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                }
                                if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                    normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                }                              
                                if (swachhCess > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap,swachhCess);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(swachhCess);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        //  System.out.println("transactionto.get4"+transTO.getTransId());
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                         transferList.add(transferTo);
                                    }                                               
                                }
                                if (krishikalyanCess > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if(isTransferCharges.equalsIgnoreCase("N")){// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap,krishikalyanCess);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(krishikalyanCess);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));                                      
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                        transferList.add(transferTo);
                                    }
                                }
                                if (normalServiceTax > 0) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    if (isTransferCharges.equalsIgnoreCase("N")) {// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, normalServiceTax);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(normalServiceTax);
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }else{
                                        transferList.add(transferTo);
                                    }
                                   
                                }                                
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);  
                            }
                            txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(acHeads.get("FIXED_DEPOSIT_ACHD")));
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                            txMap.put("generateSingleTransId", generateSingleTransId);
//                          txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
//                          txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            chargeAmt += extraAgentAmt;
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, chargeAmt+totalServiceTaxAmt);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            if (isTransferCharges.equalsIgnoreCase("N")) {// Addded by nithya on 03-02-2018  for 0009064: deposit closing(daily type)cash payment issue
//                                if(chargeAmt > 0){
//                                    transactionDAO.addTransferDebit(txMap, chargeAmt);
//                                    transactionDAO.deleteTxList();
//                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
//                                    transTO.setTransType(CommonConstants.TX_CASH);
//                                    transTO.setBatchId(depositSubNo);
//                                    transTO.setTransAmt(chargeAmt);
//                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
//                                    cashList = new ArrayList();
//                                    cashList.add(transTO);
//                                    transactionDAO.setScreenName("Deposit Account Closing");
//                                    transactionDAO.setInitiatedBranch(initiatedBranch);
//                                    transactionDAO.addCashList(cashList);
//                                    transactionDAO.doTransfer();
//                                }
                            }else{
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);
                            }                            
                            extraAgentAmt = 0.0;

                        }
                    }
                                    //added by rshad 04/04/2014
                                     if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && intPay > 0) {
                                         System.out.println("Execute here cash receivable :: " + map.get("PAYORRECEIVABLE") +" mode :: " + mode);
                                         if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                             System.out.println("Executing inside ppp");
                                             depAmt += intPay;   // Total Amount-Interest Amount
                                             txMap = new HashMap();
                                             txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                             txMap.put(TransferTrans.CR_BRANCH, debitBranchID);
                                             txMap.put(TransferTrans.CURRENCY, "INR");
                                             txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                             txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                             txMap.put("TRANS_MOD_TYPE", "TD");
                                             txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                             txMap.put("generateSingleTransId", generateSingleCashId);
                                             txMap.put(TransferTrans.PARTICULARS, "Excess Interest Paid Recollected");
                                             txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                             transactionDAO.setTransType(CommonConstants.DEBIT);
                                             transactionDAO.addTransferCredit(txMap, intPay);
                                             transactionDAO.deleteTxList();
                                             transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                             transTO.setTransType(CommonConstants.TX_CASH);
                                             transTO.setBatchId(depositSubNo);
                                             transTO.setTransAmt(new Double(intPay));
                                             transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                             cashList = new ArrayList();
                                             cashList.add(transTO);
                                         }else{ 
                                        depAmt -= debitInt;   // Total Amount-Interest Amount
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
//                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure-Interest");
//                                        txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        transactionDAO.addTransferDebit(txMap, intPay);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(intPay));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                      //  System.out.println("transactionto.get4"+transTO.getTransId());
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                         }
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();                                       
                                    }
                                     if (closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") && add_int_amt > 0) { // Added by nithya on 10-12-2019 for KD-1048
                                        System.out.println("inside cash add_int_amt depAmt :: " + depAmt +":: debitInt :: "+ debitInt);
                                        depAmt -= debitInt;   // Total Amount-Interest Amount
                                        txMap = new HashMap();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount-" + addDays +" " + depositSubNo);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        transactionDAO.addTransferDebit(txMap, add_int_amt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType(CommonConstants.TX_CASH);
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(add_int_amt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName(screenName);
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();                                       
                                    } 
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                }
                            }
                        }
                    }
                    System.out.println("CUMMULATIVE PAYABLE COMPLETED...");
                } else { // Receivable Amount for the customer....
                    System.out.println("CUMMULATIVE Receivable STARTED...");
                    double paycreditInt = 0.0;
                    double receiveInt = drawnAmt - debitInt;
                    double zeroInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();
                    if (receiveInt > 0) {
                        receiveInt = receiveInt;
                    } else {
                        intPay = receiveInt * -1;
                    }
                    if (receiveInt > 0) {//first one amount taking from intpaylbe to intpaid
                        if (intPay > 0) {//first one amount taking from intpaylbe to particular account....
                            //                            if(transactionTO.getProductType().equals("GL")){
                            //                                System.out.println("*******Lien Status Receivable non fd: "+txMap);
                            //                            }else{
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            System.out.println("receiveInt IF non fd: " + receiveInt);
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// debited to interest payable account head......
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_PROD_ID, (String) closureMap.get("PROD_ID"));
                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            ////////////////////////////////////
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");

                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));// crediting to Operative account head......
                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                            System.out.println("intPay txMap non fd: " + txMap);
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(receiveInt));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(receiveInt));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                            //                            }
                        }
                        paycreditInt = receiveInt + zeroInt;
                        if (paycreditInt > 0) {
                            System.out.println("paycreditInt IF non fd:" + paycreditInt);
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            /////////////////////////////////
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                            //added by vivek
 
                            if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                            }
                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                             txMap.put("TRANS_MOD_TYPE","GL");
                             txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, paycreditInt);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, paycreditInt);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        }
                        returnCalcMap.put("INT_PAY_AMT", new Double(paycreditInt));
                        returnCalcMap.put("INT_AMT", new Double(receiveInt));
                    } else {//all amount taking from intpaid to intpaylbe again debit intpayable to particular account...
                        System.out.println("intPay ELSE non fd:" + intPay);
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));//debiting to int paid a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                         txMap.put("TRANS_MOD_TYPE","GL");
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        ///////////////////////////
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");

                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);

                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        ////////////////////////////////////
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                        //added by vivek
//                        System.out.println("mode.equals(CommonConstants.PREMATURE_CLOSURE)5555>>>" + mode.equals(CommonConstants.PREMATURE_CLOSURE));
//                        System.out.println("map.containsKey(\"PAYORRECEIVABLE\")5555>>>" + map.containsKey("PAYORRECEIVABLE"));
//                        System.out.println("map.get(\"PAYORRECEIVABLE\")5555>>>" + map.get("PAYORRECEIVABLE"));
                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                        }
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                         txMap.put("TRANS_MOD_TYPE","GL");
                      //  System.out.println("interestAmt txMap Payable: " + txMap);
                         txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                        returnCalcMap.put("INT_AMT", new Double(intPay));
                    }
                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && penalAmt > 0) {
                        System.out.println("penalAmt non fd:" + penalAmt);
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        //////////////////////////////
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");

                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD")); // credited to  interest Provisiniong Account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE","GL");
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, penalAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("PENAL_AMT", new Double(penalAmt));
                    }
                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && AgentCommAmt > 0) {
                        System.out.println("AgentCommAmt  non fd:" + AgentCommAmt);
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        //////////////////////////////////
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");

                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD")); // credited to  interest Provisiniong Account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE","GL");    
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, AgentCommAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("PENAL_AMT", new Double(AgentCommAmt));
                    }
                    if (map.containsKey("TransactionTO")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                }
                                if (closedStatus.equals("NEW")) {
                                    if (lien != null && lien.length() > 0) {
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                        System.out.println("Lien depAmt non fd:" + depAmt);
                                    } else {
                                        depAmt = totalAmt - penalAmt - AgentCommAmt;
                                        System.out.println("No Lien depAmt non fd:" + depAmt);
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    depAmt = depAmt;
                                    System.out.println("MATURED Lien depAmt non fd:" + depAmt);
                                }
                                if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {
                                    double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    depAmt = depAmt - ser_tax_Amt;
                                }
                                txMap = new HashMap();
                                transferList = new ArrayList(); // for local transfer
                                if (closedStatus.equals("NEW")) {
                                    if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        System.out.println("Non fd GL :" + depAmt);
                                        if (head.equals(direct)) {
                                            System.out.println("EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        } else {
                                            System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                    System.out.println("TRANSFER depAmt non fd: " + depAmt);
                                    if (transactionTO.getProductType().equals("TD")) {
                                        HashMap newDepMap = new HashMap();
                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                        if (lst != null && lst.size() > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        }
                                    } else if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien Status : " + txMap);
                                    } else if (transactionTO.getProductType().equals("AD")) {
                                        // The following block uncommented by Rajesh
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien payable AD: " + txMap);
                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by chithra on 14-10-14
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());
                                       //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    }else if (transactionTO.getProductType().equals("TL")) {
                                        //txMap.put(TransferTrans.CR_AC_HD, (String)loanMap.get("ACCT_HEAD"));
                                        //txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        //txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        //txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                        //txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        System.out.println("*******Lien payable TL: " + txMap);
                                    } else if (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        //                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                        //                                        ADDED HERE BY NIKHIL FOR SUSPENSE ACCOUNT
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        System.out.println("*******No Lien Status : " + txMap);
                                    } else if (transactionTO.getProductType().equals("RM")) {
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                                        LinkedHashMap transferMap = new LinkedHashMap();
                                        HashMap remittanceMap = new HashMap();
                                        remittanceIssueDAO = new RemittanceIssueDAO();
                                        remittanceIssueTO = new RemittanceIssueTO();
                                        String favouringName = transactionTODebit.getDebitAcctNo();
                                        transactionTODebit.setApplName(favouringName);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                        transactionTODebit.setProductType("TD");
                                        transactionTODebit.setDebitAcctNo(depositSubNo);
                                        remittanceIssueDAO.setFromotherDAo(false);
                                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                        remittanceMap.put("TransactionTO", transferMap);
                                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                        remittanceMap.put("OPERATION_MODE", "ISSUE");
                                        remittanceMap.put("AUTHORIZEMAP", null);
                                        remittanceMap.put("USER_ID", map.get("USER_ID"));
                                        remittanceMap.put("MODULE", "Remittance");
                                        remittanceMap.put("MODE", "INSERT");
                                        remittanceMap.put("SCREEN", "Issue");
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remittanceIssueTO.setAmount(new Double(depAmt));
                                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                        remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        remittanceIssueTO.setFavouring(favouringName);
                                        remittanceIssueTO.setBranchId(branchID);
                                        //                                        remittanceIssueTO.setDraweeBank(branchID);
                                        remittanceIssueTO.setDraweeBranchCode(branchID);
                                        remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remittanceIssueTO.setRemarks(depositSubNo);
                                        remittanceIssueTO.setCity("560");
                                        remittanceIssueTO.setCommand("INSERT");
                                        remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                        remittanceIssueTO.setExchange(new Double(0.0));
                                        remittanceIssueTO.setPostage(new Double(0.0));
                                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                                        remittanceIssueTO.setStatusDt(curDate);
                                        remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                        remittanceIssueTO.setInstrumentNo1(instNo1);
                                        remittanceIssueTO.setInstrumentNo2(instNo2);
                                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        LinkedHashMap remitMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        remMap.put(String.valueOf(1), remittanceIssueTO);
                                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                        remittanceMap.put("RemittanceIssueTO", remitMap);
                                        System.out.println(" remittanceMap :" + remittanceMap);
                                        remittanceIssueDAO.execute(remittanceMap);
                                    }
                                    if (!transactionTO.getProductType().equals("RM")) {
                                        System.out.println("TRANSFER txMap : " + txMap);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        //////////////////////////////////
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(depAmt));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(depAmt));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);

                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        transactionDAO.doTransfer();
                                    }
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                } else if (transactionTO.getTransType().equals("CASH")) {
                                    System.out.println("CASH depAmt non fd: " + depAmt);
                                    transactionDAO.execute(map);
                                    txMap = new HashMap();
                                    transferList = new ArrayList(); // for local transfer
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// Debit deposit interest account head......
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    ////////////////////////////////
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put("generateSingleTransId", generateSingleCashId);
                                    transactionDAO.addTransferDebit(txMap, depAmt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setBatchId(depositSubNo);
                                    transTO.setTransAmt(new Double(depAmt));
                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    cashList.add(transTO);
                                    transactionDAO.setScreenName(screenName);
                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                }
                            }
                        }
                    }
                    System.out.println("CUMMULATIVE Receivable COMPLETED...");
                }
            } else if ((closureMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") || closureMap.get("BEHAVES_LIKE").equals("RECURRING")|| closureMap.get("BEHAVES_LIKE").equals("THRIFT"))
                    && closureMap.containsKey("TRANSFER_OUT_MODE") && closureMap.get("TRANSFER_OUT_MODE").equals("Y")) {
                System.out.println("CUMMULATIVE Transfer Out Started...");
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO != null) {
                                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            }
                            HashMap transferOutMap = new HashMap();
                            HashMap transferBranchMap = new HashMap();
                            transferBranchMap.put("TRANSFER_OUT_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            lst = sqlMap.executeQueryForList("getSelectBranchList", transferBranchMap);
                            if (lst != null && lst.size() > 0) {
                                transferBranchMap = (HashMap) lst.get(0);
                            }
                            HashMap transOutMap = new HashMap();
                            transOutMap.put("DEPOSIT_NO", depositNo);
                            lst = sqlMap.executeQueryForList("getIntCrIntDrawn", transOutMap);
                            if (lst != null && lst.size() > 0) {
                                transOutMap = (HashMap) lst.get(0);
                                double totalIntCredit = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                double totalIntDrawn = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                interestAmt = totalIntCredit - totalIntDrawn;
                                System.out.println("interestAmt : " + interestAmt);
                            }
                            if (interestAmt > 0) {//excess amount recovering from paybale (is a mediator A/c)crediting paid
                                System.out.println("interestAmt>0 IF fd : " + interestAmt);
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                //////////////////////////
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
//                                //added by vivek
//                                System.out.println("mode.equals(CommonConstants.PREMATURE_CLOSURE)6666>>>" + mode.equals(CommonConstants.PREMATURE_CLOSURE));
//                                System.out.println("map.containsKey(\"PAYORRECEIVABLE\")6666>>>" + map.containsKey("PAYORRECEIVABLE"));
//                                System.out.println("map.get(\"PAYORRECEIVABLE\")6666>>>" + map.get("PAYORRECEIVABLE"));
                                if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                }
                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                System.out.println("interestAmt>0 Payable txMap fd : " + txMap);
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered " + depositSubNo);
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                returnCalcMap.put("INT_AMT", new Double(interestAmt));
                                interestAmt = 0.0;
                            }
                            if (closedStatus.equals("NEW")) {
                                if (lien != null && lien.length() > 0) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    System.out.println("Lien depAmt non fd:" + depAmt);
                                } else {
                                    depAmt = totalAmt - penalAmt;
                                    System.out.println("No Lien depAmt non fd:" + depAmt);
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                depAmt = depAmt;
                                System.out.println("MATURED Lien depAmt non fd:" + depAmt);
                            }
                            txMap = new HashMap();
                            transferList = new ArrayList(); // for local transfer
                            if (closedStatus.equals("NEW")) {
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                    System.out.println("Non fd GL :" + depAmt);
                                    if (head.equals(direct)) {
                                        System.out.println("EQUALS NON fd GL :" + depAmt);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    } else {
                                        System.out.println("NOT EQUALS NON fd GL :" + depAmt);
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else {
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + " " + transferBranchMap.get("Branch Name"));
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            if (transactionTO.getTransType().equals("TRANSFER")) {
                                System.out.println("TRANSFER depAmt non fd: " + depAmt);
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    System.out.println("*******Lien Status : " + txMap);
                                }
                            }
                            if (!transactionTO.getProductType().equals("RM")) {
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                /////////////////////////////////
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                System.out.println("TRANSFER txMap : " + txMap);
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + depositSubNo + " " + transferBranchMap.get("Branch Name"));
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);

                                transactionTODebit.setTransAmt(new Double(depAmt));
                                map.put("TransactionTO", transactionDetailsMap);
                                transactionDAO.execute(map);
                                transactionDAO.doTransfer();
                            }
                            returnCalcMap.put("PAY_AMT", new Double(depAmt));
                            HashMap transferMap = new HashMap();
                            transferMap.put("DEPOSIT_NO", depositSubNo);
                            List lstTrans = sqlMap.executeQueryForList("getTransOutDepositDetails", transferMap);
                            if (lstTrans != null && lstTrans.size() > 0) {
                                transferMap = (HashMap) lstTrans.get(0);
                            }
                            transferOutMap.put("TRANS_ID", transferMap.get("BATCH_ID"));
                            transferOutMap.put("DEPOSIT_NO", depositNo);
                            transferOutMap.put("TRANS_DT", curDate);
                            transferOutMap.put("TRANS_AMT", new Double(depAmt));
                            transferOutMap.put("STATUS", closingStatus);
                            transferOutMap.put("STATUS_BY", map.get("USER_ID"));
                            transferOutMap.put("STATUS_DT", curDate);
                            transferOutMap.put("CREATED_BY", map.get("USER_ID"));
                            transferOutMap.put("CREATED_DT", curDate);
                            transferOutMap.put("TRANS_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            transferOutMap.put("INTER_TRANS_NO", getInter_TransID());
                            transferOutMap.put("CURRENT_BRANCH", branchID);
                            transferOutMap.put("TRANS_OUT_FLAG", "N");
                            returnMap.put("INTER_TRANS_NO", transferOutMap.get("INTER_TRANS_NO"));
                            System.out.println("transferOutMap :" + transferOutMap);
                            System.out.println("returnMap :" + returnMap);
                            sqlMap.executeUpdate("insertTransferOutDeposits", transferOutMap);
                            transferOutMap = null;
                            transferMap = null;
                            transferBranchMap = null;
                            transOutMap = null;
                        }
                    }
                    System.out.println("CUMMULATIVE Transfer Out Started...");
                }
            }
        }
        String authBy2 = CommonUtil.convertObjToStr(closureMap.get("AUTHORIZE_BY_2"));
        if (generateSingleTransId != null && generateSingleTransId.length() > 0&& authBy2!=null && authBy2.length()>0) {
            HashMap udtMap = new HashMap();
            udtMap.put("AUTHORIZE_BY_2", authBy2);
            udtMap.put("SINGLE_TRANS_ID", generateSingleTransId);
            udtMap.put("TRANS_DT", (Date) curDate.clone());
            sqlMap.executeUpdate("updateTranssferTransAuthorizedBy2", udtMap);
        }if(generateSingleCashId != null && generateSingleCashId.length() > 0&& authBy2!=null && authBy2.length()>0){
             HashMap udtMap = new HashMap();
            udtMap.put("AUTHORIZE_BY_2", authBy2);
            udtMap.put("SINGLE_TRANS_ID", generateSingleCashId);
            udtMap.put("TRANS_DT", (Date) curDate.clone());
            sqlMap.executeUpdate("updateCashTransAuthorizedBy2", udtMap);
        }
        if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) closureMap.get("serviceTax_DetailsTo");
            insertServiceTaxDetails(objserviceTaxDetailsTO);
        }
        if (serviceTaxDetailsForLoan != null && serviceTaxDetailsForLoan.size() > 0) {
            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
            insertServiceTaxDetails(objserviceTaxDetailsTO);
        }
        return returnCalcMap;
    }
    //added by Rishad M.P 05/Dec/2018 for Deposit To Loan 
    private HashMap interestCalculationTLAD(String accountNo, String prodID, String prodType) {
        HashMap map = new HashMap();
        HashMap insertPenal = new HashMap();
        HashMap hash = null;
        List lst = new ArrayList();
        try {
            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            if (prodType.equals("AD")) {
                lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map); //Added by nithya on 23-03-2020 for KD-1666
            } else {
                lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", _branchCode);
                map.put(CommonConstants.BRANCH_ID, _branchCode);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", curDate.clone());
                TaskHeader header = new TaskHeader();
                header.setBranchID(_branchCode);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                } else if (hash != null && hash.size() > 0) {
                    double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM", accountNo);
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", map.get("CURR_DATE"));
                    List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("NOTICE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("OTHER CHARGES") && chargeAmt > 0) {
                                insertPenal.put("OTHER CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                                insertPenal.put("EP_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.containsKey("CHARGE_TYPE") && chargeMap.get("CHARGE_TYPE") != null && chargeMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                                insertPenal.put("ARC_COST", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }
                    }
                    chargeList = null;
                }
                interestcalTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map = null;
        hash = null;
        return insertPenal;
    }    
    
    private HashMap calcuateTDS_1(HashMap obj, boolean enableTrans) throws Exception {
        System.out.println("####### doDepositClose tdsMap " + obj);
        HashMap tdsCalcMap = new HashMap();
        tdsCalcMap = obj;
        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(_branchCode);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String Prod_type = "TD";
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        intTrfAmt = CommonUtil.convertObjToDouble(tdsCalcMap.get("TDS_AMOUNT")).doubleValue();
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        tdsMap.put("INT_DATE", curDate);
        //        tdsMap.put("INT_DATE", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ServerUtil.getCurrentDate(_branchCode))));
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));

        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
        if (exceptionList == null || exceptionList.size() <= 0) {
            tdsMap = new HashMap();
            tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
            System.out.println("####### doDepositClose tdsMap " + tdsMap);
            if (tdsMap != null) {
                interestBatchTO.setIsTdsApplied("Y");
                interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
            }
        }
        return tdsMap;
    }

    public void updatePayableReceivableAmt(HashMap map) throws Exception {//interest amount is inserting for deposit interest table...
        HashMap authMap = new HashMap();
        HashMap transMap = new HashMap();
        String interestBatchId="";
        interestBatchTO = new InterestBatchTO();
        HashMap closureMap = (HashMap) map.get("CLOSUREMAP");
        if (closureMap != null && closureMap.size() > 0) {
            System.out.println("***** map authorize time :" + map);
            authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
            if (authData != null) {
                authMap = (HashMap) authData.get(0);
            }
            double interest = 0.0;
            double balAmt = 0.0;
            double crAmt = 0.0;
            double drAmt = 0.0;
            double intAmt = 0.0;
            //added by rishad for deposit interest updation 16/05/2020 block rh
            if (closureMap.containsKey("OLDTRANSACTION")) {
                transMap = (HashMap) closureMap.get("OLDTRANSACTION");
                if (transMap.containsKey("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER")) {
                    HashMap tempMap = (HashMap) transMap.get("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER");
                    if (tempMap != null) {
                        interestBatchId = CommonUtil.convertObjToStr(tempMap.get("BATCH_ID"));
                    }
                }
                if (transMap.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER")) {
                    HashMap tempMap = (HashMap) transMap.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER");
                    if (tempMap != null) {
                        interestBatchId = CommonUtil.convertObjToStr(tempMap.get("BATCH_ID"));
                    }
                }
                if (transMap.containsKey("DEPOSIT_CLOSING_INT_DETAILS_CASH")) {
                    HashMap tempMap = (HashMap) transMap.get("DEPOSIT_CLOSING_INT_DETAILS_CASH");
                    if (tempMap != null) {
                        interestBatchId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                    }
                }
                if (transMap.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH")) {
                    HashMap tempMap = (HashMap) transMap.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH");
                    if (tempMap != null) {
                        interestBatchId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                    }
                }
            }
            //end block rh
            // Added by nithya on 25-07-2019 for KD 554 - Term Deposit Interest Payment Details Insertion To Deposit_Interest Table Is Not Correct When Interest Is Edited By User.
            String deptNo = CommonUtil.convertObjToStr(authMap.get("ACCOUNTNO"));
            double actualIntPaid = 0.0;
            if (deptNo.lastIndexOf("_") != -1) {
                deptNo = deptNo.substring(0, deptNo.lastIndexOf("_"));
            }
            HashMap editMap = new HashMap();
            editMap.put("ACT_NUM", deptNo);
            List intLst = sqlMap.executeQueryForList("getDepositClosingAccounts", editMap);
            if(intLst != null && intLst.size() > 0){
                HashMap intMap = (HashMap)intLst.get(0);
                actualIntPaid = CommonUtil.convertObjToDouble(intMap.get("INT_PAYABLE"));
            }
            
            HashMap behavesMap = new HashMap();
            behavesMap.put("PROD_ID", closureMap.get("PROD_ID"));
            List lstBehaves = sqlMap.executeQueryForList("getBehavesLikeForDeposit", behavesMap);
            if (lstBehaves != null && lstBehaves.size() > 0) {
                behavesMap = (HashMap) lstBehaves.get(0);
            }
            TransactionTO transactionTODebit = new TransactionTO();
            LinkedHashMap transactionDetailsMap = new LinkedHashMap();
            TransactionTO transactionTO = new TransactionTO();
            if (map.containsKey("TransactionTO")) {
                transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO;
                if (transactionDetailsMap.size() > 0) {
                    if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs") && transactionDetailsMap.get("NOT_DELETED_TRANS_TOs") != null
                            && transactionDetailsMap.size() > 0) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO != null && transactionTO.getTransType().equals("TRANSFER")) {
                            transactionTODebit.setTransType("TRANSFER");
                            String prodId = CommonUtil.convertObjToStr(transactionTODebit.getProductId());
                            transactionTODebit.setProductId(transactionTO.getProductId());
                            transactionTODebit.setProductType(TransactionFactory.OPERATIVE);
                            transactionTODebit.setDebitAcctNo(transactionTO.getDebitAcctNo());
                            allowedTransDetailsTO.put("1", transactionTODebit);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);
                            transactionDAO.setTransType("DEBIT");
                            allowedTransDetailsTO.put("1", transactionTO);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);
                            System.out.println("######allowedTransDetailsTO : " + allowedTransDetailsTO);
                        }
                    }
                }
            }
            String head = null;
            String direct = null;
            String trans = "";
            HashMap closeMap = new HashMap();
            closeMap.put("PROD_ID", closureMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getMaturedAccountHead", closeMap);
            if (lst != null && lst.size() > 0) {
                closeMap = (HashMap) lst.get(0);
                if (transactionTO != null) {
                    head = transactionTO.getDebitAcctNo();
                    direct = CommonUtil.convertObjToStr(closeMap.get("MATURITY_DEPOSIT"));
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        if (head.equals(direct)) {
                        } else {
                            head = "GL";
                            direct = "OTHER";
                        }
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        head = "GL";
                        direct = "OTHER";
                    }
                    trans = transactionTO.getProductType();
                    System.out.println("***** trans :" + trans);
                }
            }
            String depositNo = CommonUtil.convertObjToStr(authMap.get("ACCOUNTNO"));
            if (depositNo.lastIndexOf("_") != -1) {
                depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
            }
            HashMap renewalCountMap = new HashMap();
            renewalCountMap.put("ACT_NUM", depositNo);
            List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
            if (lstCount != null && lstCount.size() > 0) {
                renewalCountMap = (HashMap) lstCount.get(0);
                interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
            }
            String depositIntNo = CommonUtil.convertObjToStr(authMap.get("ACCOUNTNO"));
            System.out.println("***** depositNo :" + depositIntNo);
            //                System.out.println("***** getTransAmt :"+transactionTO.getTransAmt());
            HashMap depMap = new HashMap();
            double totalAmt = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_AMT")).doubleValue();
            double depAmt = CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")).doubleValue(); // This is deposit amount for the customer...
            double tdsAmt = CommonUtil.convertObjToDouble(closureMap.get("TDS_SHARE")).doubleValue();
            double intPay = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue(); // This is interest amount for the customer...
            double paid = CommonUtil.convertObjToDouble(closureMap.get("PAID_INTEREST")).doubleValue();
            double creditInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();
            double debitInt = CommonUtil.convertObjToDouble(closureMap.get("DR_INTEREST")).doubleValue();
            double drawnAmt = CommonUtil.convertObjToDouble(closureMap.get("INTEREST_DRAWN")).doubleValue();
            double clrAmt = CommonUtil.convertObjToDouble(closureMap.get("CLEAR_BALANCE")).doubleValue();
            double penalAmt = CommonUtil.convertObjToDouble(closureMap.get("DELAYED_AMOUNT")).doubleValue();
            double prev_interst = CommonUtil.convertObjToDouble(closureMap.get("PREV_INTEREST_AMT")).doubleValue();
            double add_interst= CommonUtil.convertObjToDouble(closureMap.get("ADD_INT_AMOUNT"));
            interestBatchTO.setActNum(depositIntNo);
            if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                interestBatchTO.setIntType("SIMPLE");
            } else {
                interestBatchTO.setIntType("COMPOUND");
            }
            interestBatchTO.setApplDt(curDate);
            interestBatchTO.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
            interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")));
            interestBatchTO.setCustId(CommonUtil.convertObjToStr(closureMap.get("CUST_ID")));
            interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(closureMap.get("ROI")));
            intAmt = CommonUtil.convertObjToDouble(closureMap.get("INT_AMT")).doubleValue();
            interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(closureMap.get("INT_PAY")));
            interestBatchTO.setProductType("TD");
            interestBatchTO.setTrnDt(curDate);
            //Added by nithya on 15-05-2020 for KD-1090
            if(tdsAmt > 0){
                interestBatchTO.setTdsAmt(tdsAmt);
                interestBatchTO.setIsTdsApplied("Y");
                interestBatchTO.setLastTdsApplDt(curDate);
            }
            // End
            interest = paid - drawnAmt + prev_interst;
            double receiveInt = 0.0;
            System.out.println("***** interest :" + interest);
            receiveInt = drawnAmt - debitInt;

            String closedStatus = null;
            String closeStatus = null;
            HashMap ltdBalanceMap = new HashMap();
            ltdBalanceMap.put("DEPOSITNO", closureMap.get("DEPOSIT_NO"));
            List lastApplDt = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", ltdBalanceMap);
            if (lastApplDt != null && lastApplDt.size() > 0) {
                ltdBalanceMap = (HashMap) lastApplDt.get(0);
                closedStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("ACCT_STATUS"));
                closeStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("STATUS"));
            }
            System.out.println("***** receiveInt :" + receiveInt);
            double interestAmt = 0.0;
            double balPay = 0.0;
            if (creditInt == 0) {
                intPay = intPay;
                System.out.println("***** creditInt :" + intPay);
            } else {
                interest = paid - drawnAmt;
                balPay = interest - intPay;
                System.out.println("***** creditInt else:" + creditInt);
            }
            if (closureMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                totalAmt = totalAmt + penalAmt;
            }
            totalAmt=totalAmt+add_interst;
            System.out.println("interest :" + interest + "balPay :" + balPay + "totalAmt :" + totalAmt + "clrAmt :" + clrAmt);
            if (totalAmt >= clrAmt) {//payable....if(paid>debitInt){
                //|| behavesMap.get("BEHAVES_LIKE").equals("BENEVOLENT")
                if ((closedStatus.equals("MODIFIED") || closeStatus.equals("LTDMODIFIED"))
                        && closeStatus.equals("CLOSED") &&(behavesMap.get("BEHAVES_LIKE").equals("FIXED"))) {
                    double balInt = CommonUtil.convertObjToDouble(ltdBalanceMap.get("TOTAL_INT_CREDIT")).doubleValue();
                    double drawnInt = CommonUtil.convertObjToDouble(ltdBalanceMap.get("TOTAL_INT_DRAWN")).doubleValue();
                    balInt = balInt - drawnInt;
                    if (balInt > 0) {
                        depMap.put("INT_AMT", new Double(balInt));
                        depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                        sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                        interestBatchTO.setDrCr("DEBIT");
                        interestBatchTO.setTransLogId("Payable");
                        interestBatchTO.setIntAmt(new Double(intPay));
                        interestBatchTO.setTot_int_amt(new Double(0.0));
                        interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                    }
                } else if (trans == null || !trans.equals("GL")) {
                    System.out.println("***** payable not a GL :" + clrAmt);
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                    if (creditInt == 0) {
                        System.out.println("***** totalAmt>clrAmt interest==0:" + interest);
                        if (intPay > 0) {
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            //interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));// nithya...
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                        if (add_interst > 0) {
                            interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(closureMap.get("ADD_INT_RATE")));
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(add_interst));
                            interestBatchTO.setTot_int_amt(new Double(add_interst));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(add_interst));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    } else {
                        if (balPay < 0) {
                            balPay = balPay * -1;
                            System.out.println("***** totalAmt>clrAmt interest:" + interest);
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(balPay));
                            interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            balPay = 0.0;
                        }
                        if (balPay > 0) {
                            System.out.println("***** totalAmt>clrAmt interest:" + interest);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(balPay));
                            intPay = interest - balPay;
                            interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                        if (intPay > 0) {
                            System.out.println("***** totalAmt>clrAmt intPay:" + intPay);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    }
                } else {
                    System.out.println("***** payable GL debitInt:" + debitInt);
                    debitInt = debitInt - tdsAmt;
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                    if (creditInt == 0) {
                        //|| behavesMap.get("BEHAVES_LIKE").equals("BENEVOLENT")
                        if (intPay > 0 && (behavesMap.get("BEHAVES_LIKE").equals("FIXED")|| behavesMap.get("BEHAVES_LIKE").equals("BENEVOLENT"))) {
                            interest = 0.0;
                            System.out.println("***** payable GL debitInt:" + debitInt);
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            //interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));// nithya...
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            if (head.equals(direct)) {
                            } else {
                                System.out.println("***** payable GL debitInt:" + debitInt);
                                interestBatchTO.setDrCr("DEBIT");
                                interestBatchTO.setTransLogId("Receivable");
                                interestBatchTO.setIntAmt(new Double(intPay));
                                interestBatchTO.setTot_int_amt(new Double(0.0));
                                interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            }
                        } else {
                            depMap.put("INT_AMT", new Double(debitInt));
                            depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                            //added by abi

                            if (head.equals(direct) && closedStatus.equals("MATURED")) {
                                if (!behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                    sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                                }
                            } else {
                                sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                            }
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            //interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));// nithya...
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    } else {
                        if (balPay < 0) {
                            balPay = balPay * -1;
                            System.out.println("***** totalAmt>clrAmt interest:" + interest);
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(balPay));
                            //interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));// nithya...
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            balPay = 0.0;
                        }
                        if (balPay > 0) {
                            System.out.println("***** totalAmt>clrAmt interest:" + interest);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(balPay));
                            intPay = interest - balPay;
                            interestBatchTO.setTot_int_amt(new Double(intPay));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                        if (intPay > 0) {
                            System.out.println("***** totalAmt>clrAmt intPay:" + intPay);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(intPay));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    }
                }
            } else {//upto here proper dont change...elsePart also working for FD dep...
                System.out.println("***** receivable not a GL :" + clrAmt);
                if (trans == null || !trans.equals("GL")) {
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                    if (!behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                        depMap.put("INT_AMT", new Double(debitInt));
                        depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                        sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                    }
                    if (receiveInt > 0) {
                        double drawn = paid - debitInt;
                        System.out.println("*****  totalAmt<clrAmt else interest:" + interest);
                        interestBatchTO.setDrCr("CREDIT");
                        interestBatchTO.setTransLogId("Receivable");
                        interestBatchTO.setIntAmt(new Double(drawn));
                        interest = interest + receiveInt;
                        //interestBatchTO.setTot_int_amt(new Double(interest));// commented by nithya on 12-07-2019 
                        interestBatchTO.setTot_int_amt(new Double(0.0));
                        interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        //                        if(!behavesMap.get("BEHAVES_LIKE").equals("FIXED")){
                        depMap.put("INT_AMT", new Double(debitInt));
                        depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                        sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                        if (interest > 0) {
                            System.out.println("***** intPay>0 if else con intPay:" + intPay);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(interest));
                            //interestBatchTO.setTot_int_amt(new Double(0.0));// Commented by nithya on 12-07-2019
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                        //                        }
                    }
                } else {
                    System.out.println("***** receivable not a GL :" + clrAmt);
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                    depMap.put("INT_AMT", new Double(debitInt));
                    depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                    sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
                    if (creditInt == 0) {
                        System.out.println("***** totalAmt<clrAmt else interest==0:" + interest);
                        if (receiveInt > 0) {
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(receiveInt));
                            //interestBatchTO.setTot_int_amt(new Double(receiveInt));
                            interestBatchTO.setTot_int_amt(new Double(actualIntPaid));// nithya
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(receiveInt));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    } else {
                        if (receiveInt > 0) {
                            System.out.println("*****  totalAmt>clrAmt else receiveInt:" + receiveInt);
                            interestBatchTO.setDrCr("CREDIT");
                            interestBatchTO.setTransLogId("Receivable");
                            interestBatchTO.setIntAmt(new Double(receiveInt));
                            interest = interest + receiveInt;
                            interestBatchTO.setTot_int_amt(new Double(interest));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                        if (intPay > 0) {
                            System.out.println("*****  totalAmt<clrAmt else intPay:" + intPay);
                            interestBatchTO.setDrCr("DEBIT");
                            interestBatchTO.setTransLogId("Payable");
                            interestBatchTO.setIntAmt(new Double(interest));
                            interestBatchTO.setTot_int_amt(new Double(0.0));
                            interestBatchTO.setLastTdsRecivedFrom(interestBatchId);
                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                        }
                    }
                }
            }
            if (closureMap.containsKey("TRANSFER_OUT_MODE") && closureMap.get("TRANSFER_OUT_MODE").equals("Y")) {
                System.out.println("TransferOut Y : " + drawnAmt);
                depMap.put("INT_AMT", new Double(drawnAmt));
                depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                sqlMap.executeUpdate("updateClosingBalIntAmount", depMap);
                depMap.put("INT_AMT", new Double(drawnAmt));
                depMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                sqlMap.executeUpdate("updateClosingTotIndAmount", depMap);
            } else if (closureMap.containsKey("TRANSFER_OUT_MODE")
                    && (closureMap.get("TRANSFER_OUT_MODE").equals("N") || closureMap.get("TRANSFER_OUT_MODE").equals("NO"))) {
            }
        }
    }

    private void doAuthorize(HashMap map) throws Exception {
        HashMap singleAuthorizeMap;  
        HashMap cashAuthMap;
        String linkBatchId = null, batchId = null, prodId = null, acHead = null, prodType = null;
        DepSubNoAccInfoTO depSubNoAccInfoTO = new DepSubNoAccInfoTO();
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("DoAuthorize map :" + map + "authData:" + authData + "authMap:" + authMap);
        String authorizeStatus;
        for (int i = 0, j = authData.size(); i < j; i++) {
            linkBatchId = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("ACCOUNTNO"));//Transaction Batch Id
            singleAuthorizeMap = new HashMap();
            if (authMap.get("AUTHORIZESTATUS").equals("AUTHORIZED")) {
                updatePayableReceivableAmt(map);
              //Added By Chithra on 12-06-14
                HashMap amap = new HashMap();
                String dep_no = linkBatchId;
                if (dep_no.lastIndexOf("_") != -1) {
                    dep_no = dep_no.substring(0, dep_no.lastIndexOf("_"));
                }
                amap.put("DEPOSIT_NO", dep_no);
                amap.put("PAID_DATE", curDate);
                sqlMap.executeUpdate("upadateInterestDetails", amap);
                //End......
            }
            authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));

            singleAuthorizeMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
            singleAuthorizeMap.put("ACCOUNTNO", linkBatchId);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            singleAuthorizeMap.put("AUTHORIZEDT", curDate);
            System.out.println("DoAuthorize singleAuthorizeMap : " + singleAuthorizeMap);
            String actNum = linkBatchId;
            HashMap closureMap = (HashMap) map.get("CLOSUREMAP");//ClosureMap
            System.out.println("DoAuthorize closureMap : " + closureMap);
            if (closureMap.containsKey("LTDCLOSINGDATA")) {//this condition will work only ltd deposits ohterwise it wont work.
                HashMap ltdClosingMap = new HashMap();
                ltdClosingMap = (HashMap) closureMap.get("LTDCLOSINGDATA");
                System.out.println("doAuthorize ltdClosingMap " + ltdClosingMap);
                if (ltdClosingMap != null && ltdClosingMap.size() > 0) {
                    HashMap totalMap = new HashMap();
                    accountClosingDAO = new AccountClosingDAO();
                    ltdClosingMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    ltdClosingMap.put("USER_ID", map.get("USER_ID"));
                    ltdClosingMap.put("DEPOSIT_TRANSACTION", "DEPOSIT_TRANSACTION");
                    if (authMap.get("AUTHORIZESTATUS").equals("AUTHORIZED")) {
                        ltdClosingMap.put("MODE", "AUTHORIZE");
                    } else {
                        ltdClosingMap.put("MODE", null);
                    }
                    ltdClosingMap.put("BEHAVE_LIKE", "LOANS_AGAINST_DEPOSITS");
                    ltdClosingMap.put("PROD_TYPE", "TermLoan");
                    System.out.println("before going DAO doAuthorize:" + ltdClosingMap);
                    accountClosingDAO.execute(ltdClosingMap);
                    totalMap = null;
                    ltdClosingMap = null;
                }
            }
            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", closureMap);

            TransactionTO transactionTODebit = new TransactionTO();
            LinkedHashMap transactionDetailsMap = new LinkedHashMap();
            TransactionTO transactionTO = null;
            if (actNum.lastIndexOf("_") != -1) {
                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
            }
            String closedStatus = null;
            String closeStatus = null;
            String type = null;
            HashMap ltdBalanceMap = new HashMap();
            ltdBalanceMap.put("DEPOSITNO", actNum);
            List lastApplDt = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", ltdBalanceMap);
            if (lastApplDt != null && lastApplDt.size() > 0) {
                ltdBalanceMap = (HashMap) lastApplDt.get(0);
                closedStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("ACCT_STATUS"));
                closeStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("STATUS"));
                type = CommonUtil.convertObjToStr(ltdBalanceMap.get("PAYMENT_TYPE"));
            }
            ltdBalanceMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
            ltdBalanceMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            ltdBalanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            ltdBalanceMap.put("AUTHORIZEDT", curDate);
            if (authMap.get("AUTHORIZESTATUS").equals("REJECTED")) {
                System.out.println("Rejection Started.:");
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            if (transactionTO != null) {
                                transactionTO.setStatus(CommonConstants.STATUS_DELETED);
                                transactionTO.setBranchId(_branchCode);
                                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
                                sqlMap.executeUpdate("updateDepositCloseDate", ltdBalanceMap);
                            }
                        }
                    }
                }
                singleAuthorizeMap.put("ACCOUNTNO", actNum + "_1");
                singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                sqlMap.executeUpdate("authorizeDepositAccountClose", singleAuthorizeMap);
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                sqlMap.executeUpdate("authorizeDepositSubAccountClose", singleAuthorizeMap);
                singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                singleAuthorizeMap.put("DELETE", CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("authorizeUpdateAccountCloseTOForDep", singleAuthorizeMap);
                sqlMap.executeUpdate("updatePenalInt", singleAuthorizeMap);
                List multiCloseList = sqlMap.executeQueryForList("checkDepositMultiClosingTemp", singleAuthorizeMap);// added by shihad for 9892
                if (map.containsKey("MULTIPLE_DEPOSIT_CLOSING") || (multiCloseList!=null && multiCloseList.size()>0)) {
                    sqlMap.executeUpdate("authorizeMultiDepositAccountCloseTemp", singleAuthorizeMap);
                }
                HashMap rejectMap = new HashMap();
                rejectMap.put("DEPOSIT_NO", actNum);
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                ltdBalanceMap.put("DEPOSITNO", actNum);
                System.out.println("#######DoAuthorize linkBatchId : " + singleAuthorizeMap);

                if (closeStatus.equals("LIEN") && closedStatus.equals("CLOSED")) {
                    ltdBalanceMap.put("NORM_STATUS", "LIEN");
                    ltdBalanceMap.put("ACCT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTDMODIFIED CLOSED : " + ltdBalanceMap);
                } else if (closeStatus.equals("LIEN") && closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("NORM_STATUS", "LIEN");
                    ltdBalanceMap.put("ACCT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("MODIFIED CLOSED : " + ltdBalanceMap);
                } else if (closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("ACCT_STATUS", "NEW");
                    ltdBalanceMap.put("NORM_STATUS", CommonConstants.STATUS_CREATED);
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTD MATURED : " + ltdBalanceMap);
                } else {
                    sqlMap.executeUpdate("updateSubDepositCloseNew", rejectMap);
                    sqlMap.executeUpdate("updateDepositAcinfoCloseNew", rejectMap);
                }
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", singleAuthorizeMap);
                
                if (map.containsKey("SERVICE_TAX_AUTH")) {
                    HashMap serAuth_Map = new HashMap();
                    serAuth_Map.put("STATUS", singleAuthorizeMap.get("STATUS"));
                    serAuth_Map.put("USER_ID", map.get(CommonConstants.USER_ID));
                    serAuth_Map.put("AUTHORIZEDT", curDate);
                    serAuth_Map.put("ACCT_NUM", singleAuthorizeMap.get("ACCOUNTNO"));
                    sqlMap.executeUpdate("authorizeServiceTaxDetails", serAuth_Map);
                    if (transactionTO!= null && transactionTO.getTransType().equalsIgnoreCase("TRANSFER") && transactionTO.getProductType().equalsIgnoreCase("TL")) {
                        serAuth_Map.put("ACCT_NUM", transactionTO.getDebitAcctNo());
                        sqlMap.executeUpdate("authorizeServiceTaxDetails", serAuth_Map);
                    }
                }
                System.out.println("Rejection Completed.:");
            } else {
                System.out.println("Authorization Started.:");
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            if (transactionTO != null) {
                                prodId = CommonUtil.convertObjToStr(transactionTO.getProductId());
                                acHead = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                                prodType = CommonUtil.convertObjToStr(transactionTO.getProductType());
                                if (closedStatus.equals("MATURED")) {
                                    transactionTO.setStatus("MATURED");
                                } else {
                                    transactionTO.setStatus("MODIFIED");
                                }
                                if (closeStatus.equals("LIEN") || closedStatus.equals("MATURED")) {
                                    transactionTO.setBranchId(_branchCode);
                                    sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
                                }
                            }
                        }
                    }
                }
                singleAuthorizeMap.put("ACCOUNTNO", linkBatchId);
                ltdBalanceMap.put("DEPOSITNO", actNum);
                System.out.println("singleAuthorizeMap : " + singleAuthorizeMap + "actNum" + actNum + "ltdBalanceMap" + ltdBalanceMap);

                if (closeStatus.equals("LIEN") && closedStatus.equals("CLOSED")) {
                    ltdBalanceMap.put("NORM_STATUS", CommonConstants.STATUS_MODIFIED);
                    ltdBalanceMap.put("ACCT_STATUS", "CLOSED");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "CLOSED");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTDMODIFIED CLOSED : " + ltdBalanceMap);
                } else if (closeStatus.equals("LIEN") && closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("NORM_STATUS", "LTDMODIFIED");
                    ltdBalanceMap.put("ACCT_STATUS", "MATURED");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "MATURED");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("MODIFIED CLOSED : " + ltdBalanceMap);
                } else if (closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("ACCT_STATUS", "MATURED");
                    ltdBalanceMap.put("NORM_STATUS", "LTDMODIFIED");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "MATURED");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTD MATURED : " + ltdBalanceMap);
                } else {
                    sqlMap.executeUpdate("authorizeDepositAccountClose", singleAuthorizeMap);
                    System.out.println("DoAuthorize actNum :" + actNum + "linkBatchId" + linkBatchId);
                    singleAuthorizeMap.put("ACCOUNTNO", actNum);
                    sqlMap.executeUpdate("authorizeDepositSubAccountClose", singleAuthorizeMap);
                }
                List multiCloseList = sqlMap.executeQueryForList("checkDepositMultiClosingTemp", singleAuthorizeMap); // added by shihad for 9892
                if (map.containsKey("MULTIPLE_DEPOSIT_CLOSING") || (multiCloseList != null && multiCloseList.size() > 0)) {
                    sqlMap.executeUpdate("authorizeMultiDepositAccountCloseTemp", singleAuthorizeMap);
                }
                HashMap behavesMap = new HashMap();
                behavesMap.put("PROD_ID", closureMap.get("PROD_ID"));
                List lstBehaves = sqlMap.executeQueryForList("getBehavesLikeForDeposit", behavesMap);
                if (lstBehaves != null && lstBehaves.size() > 0) {
                    behavesMap = (HashMap) lstBehaves.get(0);
                    if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                        HashMap siMap = new HashMap();
                        siMap.put("ACT_NUM", actNum + "_1");
                        lstBehaves = ServerUtil.executeQuery("getselectForRecurring", siMap);
                        if (lstBehaves != null && lstBehaves.size() > 0) {
                            HashMap siUpdateMap = new HashMap();
                            siMap = (HashMap) lstBehaves.get(0);
                            siUpdateMap.put("STATUS", "CLOSED");
                            siUpdateMap.put("SI_ID", siMap.get("SI_ID"));
                            sqlMap.executeUpdate("updateSIinRecurringCredit", siUpdateMap);
                            sqlMap.executeUpdate("updateSIinRecurringDebit", siUpdateMap);
                            sqlMap.executeUpdate("updateSIinRecurring", siUpdateMap);
                            siUpdateMap = null;
                            siMap = null;
                            behavesMap = null;
                        }
                    }
                }
                if (map.containsKey("DEPOSIT_PENAL_AMT")) {
                    HashMap penalMap = new HashMap();
                    penalMap.put("DEPOSIT_NO", actNum);
//                    penalMap.put("DELAYED_AMOUNT", String.valueOf(map.get("DEPOSIT_PENAL_AMT")));
//                    penalMap.put("DELAYED_MONTH", String.valueOf(map.get("DEPOSIT_PENAL_MONTH")));
                      penalMap.put("DELAYED_AMOUNT", CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_AMT")));
                      penalMap.put("DELAYED_MONTH", CommonUtil.convertObjToDouble(map.get("DEPOSIT_PENAL_MONTH")));
                    sqlMap.executeUpdate("updateDepositPenalAmount", penalMap);
                    penalMap = null;
                }
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", singleAuthorizeMap);
                if (map.containsKey("SERVICE_TAX_AUTH")) {
                    HashMap serAuth_Map = new HashMap();
                    serAuth_Map.put("STATUS", singleAuthorizeMap.get("STATUS"));
                    serAuth_Map.put("USER_ID", map.get(CommonConstants.USER_ID));
                    serAuth_Map.put("AUTHORIZEDT", curDate);
                    serAuth_Map.put("ACCT_NUM", singleAuthorizeMap.get("ACCOUNTNO"));
                    sqlMap.executeUpdate("authorizeServiceTaxDetails", serAuth_Map);
                    if (transactionTO!= null && transactionTO.getTransType().equalsIgnoreCase("TRANSFER") && transactionTO.getProductType().equalsIgnoreCase("TL")) {
                        serAuth_Map.put("ACCT_NUM", transactionTO.getDebitAcctNo());
                        sqlMap.executeUpdate("authorizeServiceTaxDetails", serAuth_Map);
                    }
                }
                HashMap flexiMap = new HashMap();
                flexiMap.put("DEPOSIT_NO", actNum);
                List lstflexi = sqlMap.executeQueryForList("getFlexiAccountNo", flexiMap);
                if (lstflexi != null && lstflexi.size() > 0) {
                    flexiMap = (HashMap) lstflexi.get(0);
                    double deptAmt = 0.0;
                    double depAmt = CommonUtil.convertObjToDouble(flexiMap.get("TOTAL_BALANCE")).doubleValue();
                    String flexiActNum = CommonUtil.convertObjToStr(flexiMap.get("FLEXI_ACT_NUM"));
                    List lstLien = sqlMap.executeQueryForList("getFlexiBalanceDeposit", flexiMap);
                    if (lstLien != null && lstLien.size() > 0) {
                        flexiMap = (HashMap) lstLien.get(0);
                        double minBal2 = CommonUtil.convertObjToDouble(flexiMap.get("MIN_BAL2")).doubleValue();
                        flexiMap.put("LIEN_AC_NO", flexiActNum);
                        flexiMap.put("DEPOSIT_NO", actNum);
                        lstLien = sqlMap.executeQueryForList("getDetailsForSBLienAct", flexiMap);
                        if (lstLien != null && lstLien.size() > 0) {
                            flexiMap = (HashMap) lstLien.get(0);
                            double totLienAmt = CommonUtil.convertObjToDouble(flexiMap.get("LIEN_AMT")).doubleValue();
                            if (totLienAmt > 0) {
                                deptAmt = depAmt - minBal2;
                                System.out.println("totLienAmt if Normal : " + deptAmt);
                                HashMap depositMap = new HashMap();
                                depositMap.put("DEPOSIT_NO", actNum);
                                depositMap.put("BALANCE", new Double(totLienAmt));
                                sqlMap.executeUpdate("update.FlexiDepositClosingUnLien", depositMap);
                                sqlMap.executeUpdate("updateFlexiAmountForDeposits", depositMap);
                            } else {
                                System.out.println("totLienAmt if Normal : " + deptAmt);
                                deptAmt = depAmt - minBal2;
                            }
                            System.out.println("totLienAmt if Normal : " + totLienAmt);
                        } else {
                            System.out.println("deptAmt else Normal : " + depAmt);
                            deptAmt = depAmt - minBal2;
                            System.out.println("deptAmt else Normal : " + deptAmt);
                        }
                        HashMap inputMap = new HashMap();
                        inputMap.put("DEOSIT_AMT", new Double(depAmt * -1));
                        inputMap.put("FLEXI_DEOSIT_AMT", new Double(deptAmt * -1));
                        inputMap.put("ACT_NUM", flexiActNum);
                        sqlMap.executeUpdate("Flexi.updateFlexiBalance", inputMap);
                        lstLien = null;
                    }
                }
                lstflexi = null;
                //below coding r using for closed details viewing purposes...
                HashMap statusMap = new HashMap();
                if (actNum.lastIndexOf("_") != -1) {
                    actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                }
                statusMap.put("DEPOSIT NO", actNum);
                List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", statusMap);
                //Below map "tmpHashMap" added by Ajay Sharma for mantis ID 9086 dated 14 May 2014
                //(after rollback system was updating balance 0 so made the changes as per the previous stage)
                HashMap tmpHashMap=new HashMap();
                tmpHashMap.put("DEPOSITNO", actNum);
                if (lst != null && lst.size() > 0) {
                    statusMap = (HashMap) lst.get(0);
                    tmpHashMap.put("CLEAR_BALANCE",statusMap.get("CLEAR_BALANCE"));
                    tmpHashMap.put("AVAILABLE_BALANCE",statusMap.get("AVAILABLE_BALANCE"));
                    tmpHashMap.put("TOTAL_BALANCE",statusMap.get("TOTAL_BALANCE"));
                    depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_NO")));
                    depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(statusMap.get("DEPOSIT_SUB_NO")));
                    depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_DT"))));
                    depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_DD")));
                    depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_MM")));
                    depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_YY")));
                    depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_AMT")));
                    depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(statusMap.get("INTPAY_MODE")));
                    depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(statusMap.get("INTPAY_FREQ")));
                    depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("MATURITY_DT"))));
                    depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(statusMap.get("RATE_OF_INT")));
                    depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(statusMap.get("MATURITY_AMT")));
                    depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(statusMap.get("TOT_INT_AMT")));
                    depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(statusMap.get("PERIODIC_INT_AMT")));
                    depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(statusMap.get("STATUS")));
                    depSubNoAccInfoTO.setClearBalance(new Double(0.0));
                    depSubNoAccInfoTO.setAvailableBalance(new Double(0.0));
                    depSubNoAccInfoTO.setCloseDt(curDate);
                    depSubNoAccInfoTO.setCreateBy(CommonUtil.convertObjToStr(statusMap.get("CREATE_BY")));
                    depSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(statusMap.get("CLOSE_BY")));
                    depSubNoAccInfoTO.setAuthorizeDt(curDate);
                    depSubNoAccInfoTO.setAuthorizeBy(CommonUtil.convertObjToStr(statusMap.get("AUTHORIZE_BY")));
                    depSubNoAccInfoTO.setAuthorizeStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                    depSubNoAccInfoTO.setAcctStatus("CLOSED");
                    depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_INT_APPL_DT"))));
                    depSubNoAccInfoTO.setLastTransDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_TRANS_APPL_DT"))));
                    depSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_CREDIT")));
                    depSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_DRAWN")));
                    depSubNoAccInfoTO.setTotalIntDebit(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_DEBIT")));
                    depSubNoAccInfoTO.setTotalInstallments(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INSTALLMENTS")));
                    depSubNoAccInfoTO.setTotalInstallPaid(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INSTALL_PAID")));
                    depSubNoAccInfoTO.setTotalBalance(new Double(0.0));
                    depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(statusMap.get("INSTALL_TYPE")));
                    depSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(statusMap.get("CALENDER_DAY")));
                    depSubNoAccInfoTO.setFlexi_status(CommonUtil.convertObjToStr(statusMap.get("FLEXI_STATUS")));
                    //Commented By Kannan AR after discuss with Mr.Sathiya Reg. Jira id. KDSA-181 
                    //sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
                    sqlMap.executeUpdate("updateTEMPBalanceTD", tmpHashMap); 
                    //sqlMap.executeUpdate("updateTEMPBalanceSAMENO", tmpHashMap);
                    HashMap intMap = new HashMap();
                    statusMap.put("DEPOSIT_NO", actNum);
                    lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", statusMap);
                    if (lst != null && lst.size() > 0) {
                        statusMap = (HashMap) lst.get(0);
                        intMap.put("ACT_NUM", statusMap.get("DEPOSIT_NO"));
                        double currRate = CommonUtil.convertObjToDouble(statusMap.get("CURR_RATE_OF_INT")).doubleValue();
                        if (currRate == 0) {
                            currRate = 0.0;
                        }
                        double penalRate = CommonUtil.convertObjToDouble(statusMap.get("PENAL_RATE")).doubleValue();
                        if (penalRate == 0) {
                            penalRate = 0.0;
                        }
                        double sbInt = CommonUtil.convertObjToDouble(statusMap.get("SB_INT_AMT")).doubleValue();
                        if (sbInt == 0) {
                            sbInt = 0.0;
                        }
                        double sbPeriod = CommonUtil.convertObjToDouble(statusMap.get("SB_PERIOD_RUN")).doubleValue();
                        if (sbPeriod == 0) {
                            sbPeriod = 0.0;
                        }
                        double interestAmt = CommonUtil.convertObjToDouble(statusMap.get("INTEREST_AMT")).doubleValue();
                        if (interestAmt == 0) {
                            interestAmt = 0.0;
                        }
                        intMap.put("ACCT_STATUS", statusMap.get("ACCT_STATUS"));
                        intMap.put("CURR_RATE_OF_INT", currRate);
                        intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(sbInt));
                        //intMap.put("SB_PERIOD_RUN", String.valueOf(sbPeriod));
                        intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(sbPeriod));
                        intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(interestAmt));
                        intMap.put("INT", CommonUtil.convertObjToDouble(penalRate));
                        sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                        intMap = null;
                        HashMap renewalMap = new HashMap();
                        renewalMap.put("DEPOSIT_NO", statusMap.get("DEPOSIT_NO"));
                        lst = sqlMap.executeQueryForList("getSelectMaxSLNo", renewalMap);
                        if (lst != null && lst.size() > 0) {
                            renewalMap = (HashMap) lst.get(0);
                            double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                            renewalMap.put("DEPOSIT_NO", statusMap.get("DEPOSIT_NO"));
                            renewalMap.put("COUNT", new Double(maxNo + 1));
                            sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                        } else {
                            renewalMap.put("DEPOSIT_NO", statusMap.get("DEPOSIT_NO"));
                            renewalMap.put("COUNT", new Double(1));
                            sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                        }
                    }
                    HashMap slNoMap = new HashMap();
                    slNoMap.put("DEPOSIT_NO", actNum);
                    if (depSubNoAccInfoTO.getIntpayMode().equals("TRANSFER")) {
                        if (statusMap.get("INT_PAY_PROD_TYPE").equals("GL") || statusMap.get("INT_PAY_PROD_TYPE").equals("RM")) {
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTONoGL", slNoMap);
                        } else {
                            slNoMap.put("INT_PAY_PROD_ID", statusMap.get("INT_PAY_PROD_ID"));
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTONo", slNoMap);
                        }
                    } else if (depSubNoAccInfoTO.getIntpayMode().equals("CASH")) {
                        System.out.println("slNoMap :" + statusMap);
                        HashMap nullMap = new HashMap();
                        nullMap.put("DEPOSIT_NO", actNum);
                        sqlMap.executeUpdate("updateDepSubNoAccInfoTONoInterestNull", nullMap);
                    }
                    HashMap remitMap = new HashMap();
                    remitMap.put("BATCH_ID", actNum + "_1");
                    List remitList = sqlMap.executeQueryForList("getSelectDepositTransMode", remitMap);
                    if (remitList != null && remitList.size() > 0) {
                        remitMap = (HashMap) remitList.get(0);
                        sqlMap.executeUpdate("updateTokenNOFromOtherModule", remitMap);
                    }
                    slNoMap = null;
                    statusMap = null;
                    remitMap = null;
                }
                //System.out.println("paid : "+paid + " subMap : "+subMap+" debitInt : "+debitInt);
                double clPaidInt = CommonUtil.convertObjToDouble(closureMap.get("PAID_INTEREST"));
                double clIntdrawn = CommonUtil.convertObjToDouble(closureMap.get("INTEREST_DRAWN"));
                HashMap replaceMap = new HashMap();
                replaceMap.put("DEPOSIT NO", actNum);
                replaceMap.put("TOTAL_INT_CREDIT", clPaidInt);
                replaceMap.put("TOTAL_INT_DRAWN", clIntdrawn);
                System.out.println("closureMap === " + closureMap);
                sqlMap.executeUpdate("creditDrawnHistory", replaceMap);
                sqlMap.executeUpdate("creditDrawnHistorySameNO", replaceMap);

                HashMap nomineeMap = new HashMap();
                nomineeMap.put("DEPOSIT_NO", actNum);
                nomineeMap.put("STATUS", "EXISTING");
                nomineeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                nomineeMap.put("CURR_DATE", curDate);
                sqlMap.executeUpdate("StatusUpdationinTD", nomineeMap);
                nomineeMap = null;
                System.out.println("Authorization Completed.:");
            }
            HashMap transOutMap = new HashMap();
            transOutMap.put("DEPOSIT_NO", actNum);
            List transList = sqlMap.executeQueryForList("getTransferOutDeposit", transOutMap);
            if (transList != null && transList.size() > 0) {
                transOutMap = (HashMap) transList.get(0);
                if (authMap.get("AUTHORIZESTATUS").equals(CommonConstants.STATUS_AUTHORIZED)) {
                    transOutMap.put("AUTHORIZE_STATUS", authMap.get("AUTHORIZESTATUS"));
                    transOutMap.put("AUTHORIZE_DT", curDate);
                    transOutMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                    System.out.println("transOutMap :" + transOutMap);
                    sqlMap.executeUpdate("updateTransferOutDeposits", transOutMap);
                } else if (authMap.get("AUTHORIZESTATUS").equals(CommonConstants.STATUS_REJECTED)) {
                    sqlMap.executeUpdate("updateTransferOutRejectionDeposits", transOutMap);
                }
            }
            if (closureMap.containsKey("TRANSFER_OUT_MODE")) {
                transOutMap = new HashMap();
                transOutMap.put("DEPOSIT_NO", actNum);
                transOutMap.put("TRANS_OUT", closureMap.get("TRANSFER_OUT_MODE"));
                sqlMap.executeUpdate("updateTransferOutFlag", transOutMap);
            }
            transList = null;
            //payorder coding...
            HashMap payOrderMap = new HashMap();
            payOrderMap.put("REMARKS", linkBatchId);
            remittanceIssueTO = new RemittanceIssueTO();
            List lstPay = sqlMap.executeQueryForList("getSelectDepositPayOrder", payOrderMap);
            if (lstPay != null && lstPay.size() > 0) {
                HashMap selectMap = (HashMap) lstPay.get(0);
                String variableNo = CommonUtil.convertObjToStr(selectMap.get("VARIABLE_NO"));
                LinkedHashMap notDeleteMap = new LinkedHashMap();
                LinkedHashMap transferMap = new LinkedHashMap();
                HashMap remittanceMap = new HashMap();
                remittanceIssueDAO = new RemittanceIssueDAO();
                remittanceIssueTO = new RemittanceIssueTO();
                LinkedHashMap remitMap = new LinkedHashMap();
                LinkedHashMap remMap = new LinkedHashMap();

                String favouringName = CommonUtil.convertObjToStr(selectMap.get("FAVOURING"));
                double amount = CommonUtil.convertObjToDouble(selectMap.get("AMOUNT")).doubleValue();
                transactionTODebit.setApplName(favouringName);
                transactionTODebit.setTransAmt(new Double(amount));
                transactionTODebit.setTransType("TRANSFER");
                transactionTODebit.setProductId(CommonUtil.convertObjToStr(selectMap.get("PROD_ID")));
                transactionTODebit.setProductType("RM");
                transactionTODebit.setDebitAcctNo(favouringName);
                remittanceIssueDAO.setFromotherDAo(false);
                notDeleteMap.put(String.valueOf(1), null);
                transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                remittanceMap.put("TransactionTO", transferMap);
                remittanceMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceMap.put("OPERATION_MODE", "ISSUE");
                remittanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                remittanceMap.put("MODULE", null);
                remittanceMap.put("MODE", null);
                remittanceMap.put("SCREEN", null);
                remittanceIssueTO.setDraweeBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setAmount(new Double(amount));
                remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(selectMap.get("PROD_ID")));
                remittanceIssueTO.setFavouring(favouringName);
                //                remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                remittanceIssueTO.setRemarks(linkBatchId);
                remittanceIssueTO.setCommand(null);
                remittanceIssueTO.setTotalAmt(new Double(amount));
                remittanceIssueTO.setExchange(new Double(0.0));
                remittanceIssueTO.setPostage(new Double(0.0));
                remittanceIssueTO.setOtherCharges(new Double(0.0));
                remittanceIssueTO.setStatusDt(curDate);
                remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                remittanceIssueTO.setAuthorizeDt(curDate);
                remittanceIssueTO.setAuthorizeStatus(authorizeStatus);
                remittanceIssueTO.setAuthorizeUser(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                remMap.put(String.valueOf(1), remittanceIssueTO);
                remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                remittanceMap.put("RemittanceIssueTO", remitMap);
                HashMap transferTransMap = new HashMap();
                transferTransMap.put("LINK_BATCH_ID", variableNo);
                transferTransMap.put("TODAY_DT", curDate);
                transferTransMap.put("AMOUNT", new Double(amount));
                transferTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                lstPay = sqlMap.executeQueryForList("getTransferTransBatchID", transferTransMap);
                if (lstPay != null && lstPay.size() > 0) {
                    transferTransMap = (HashMap) lstPay.get(0);
                }
                HashMap authorizeMap = new HashMap();
                HashMap authorizeData = new HashMap();
                ArrayList data = new ArrayList();
                String transBatchId = CommonUtil.convertObjToStr(selectMap.get("BATCH_ID"));
                authorizeData.put("STATUS", authorizeStatus);
                authorizeData.put("BATCH_ID", transBatchId);
                data.add(authorizeData);
                authorizeMap.put("AUTHORIZESTATUS", authorizeStatus);
                authorizeMap.put("AUTHORIZEDATA", data);
                remittanceMap.put("AUTHORIZEMAP", authorizeMap);
                System.out.println(" remittanceMap :" + remittanceMap);
                remittanceIssueDAO.execute(remittanceMap);
                remittanceMap = null;
                authorizeMap = null;
                authorizeData = null;
                data = null;
                notDeleteMap = null;
                transferMap = null;
                remittanceMap = null;
                remitMap = null;
                remMap = null;
                transferTransMap = null;
                payOrderMap = null;
                selectMap = null;
            }
            singleAuthorizeMap = null;
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("INITIATED_BRANCH", map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put("LINK_BATCH_ID", linkBatchId);
            cashAuthMap.put("TODAY_DT", curDate);
            cashAuthMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
            System.out.println("#######DoAuthorize cashAuthMap : " + cashAuthMap);
            //added by sreekrishnan for cashier
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                    //added for checking traansaction type count for identifieying rececvable is there or not
                    int count = 0;
                    List countLst = sqlMap.executeQueryForList("getCashTransTypeCount", cashAuthMap);
                    if (countLst != null && countLst.size() > 0) {
                        HashMap objCount = (HashMap) countLst.get(0);
                        count = CommonUtil.convertObjToInt(objCount.get("COUNT"));
                    }
                    if(count == 2){
                        List lst = sqlMap.executeQueryForList("getCashTransTransID", cashAuthMap);
                        if (lst != null && lst.size() > 0) {
                            HashMap obj = (HashMap) lst.get(0);
                            cashAuthMap.put("DEPOSIT_CLOSING_PAYMENT", "DEPOSIT_CLOSING_PAYMENT");
                            cashAuthMap.put("TRANS_ID", obj.get("TRANS_ID"));
                        }
                    }
                }
            }
            System.out.println("linkBatchId or TRANS_ID"+cashAuthMap.get("TRANS_ID"));
            System.out.println("DEPOSIT_CLOSING_PAYMENT"+cashAuthMap.get("DEPOSIT_CLOSING_PAYMENT"));
            //end
            //modified by Rishad M.P for Deposit To Loan Authorization (Incase Of Regular Daily Deposit) 12/dec/2018
            if (transactionTO != null && transactionTO.getTransType().equalsIgnoreCase("TRANSFER") && (transactionTO.getProductType().equalsIgnoreCase("TL") || transactionTO.getProductType().equalsIgnoreCase("AD"))) { // AD added by nithya on 23-03-2020 for KD-1666
                // for identifiying only this type of transact (ie. regular deposit to loan ) 
                cashAuthMap.put("DEPOSIT_CLOSING_LOAN", "DEPOSIT_CLOSING_LOAN");
                cashAuthMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(authMap.get("SINGLE_TRANS_ID")));
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap.remove("DEPOSIT_CLOSING_LOAN");
            } else {
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
            }
            if (closureMap.containsKey("OLDTRANSACTION") && closureMap.get("OLDTRANSACTION") != null) {
                HashMap tempMap = (HashMap) closureMap.get("OLDTRANSACTION");
                if (tempMap != null && tempMap.size() > 0 && tempMap.containsKey("TRANS_DETAILS") && tempMap.get("TRANS_DETAILS") != null) {
                    linkBatchId = CommonUtil.convertObjToStr(tempMap.get("TRANS_DETAILS"));
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                    tempMap.clear();
                }
                tempMap = null;
            }

            //            if(authMap.get("AUTHORIZESTATUS").equals("AUTHORIZED")){//matured head updating...
            //                HashMap installMap = new HashMap();
            //                installMap.put("DEPOSIT_NO",actNum);
            //                if(closedStatus.equals("CLOSED")){
            //                    installMap.put("INSTALL_TYPE", "");
            //                    System.out.println("#######DoAuthorize installMap : "+installMap);
            //                    sqlMap.executeUpdate("updateAccountHeadDeposits", installMap);
            //                }else{
            //                    installMap.put("INSTALL_TYPE", acHead);
            //                    System.out.println("#######DoAuthorize installMap : "+installMap);
            //                    sqlMap.executeUpdate("updateAccountHeadDeposits", installMap);
            //                }
            //                installMap = null;
            //            }
            prodType = null;
            prodId = null;
            authMap = null;
            cashAuthMap = null;
            ltdBalanceMap = null;
            map = null;
            closureMap = null;
            remittanceIssueTO = null;
            depSubNoAccInfoTO = null;
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("execute map " + map);
        HashMap returnMap = new HashMap();
//        if (map.containsKey("MULTIPLE_DEPOSIT_CLOSING")) {
//            if (map.containsKey("MULTI_CLOSE_SINGLE_TRANS_ID")) {
//                generateSingleTransIdMultiClosing = CommonUtil.convertObjToStr(map.get("MULTI_CLOSE_SINGLE_TRANS_ID"));
//            }
//        } commented by shihad
        returnMap = execute(map, true);
        if (map.containsKey("MULTIPLE_DEPOSIT_CLOSING")) {
            returnMap.put("STATUS", "SUCCESS");
        }
        return returnMap;
    }

    public HashMap execute(HashMap map, boolean enableTrans) throws Exception {
        System.out.println("Execute Method:" + map + "enableTrans:" + enableTrans);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        try {
            if (enableTrans) {
                sqlMap.startTransaction();
            }
            HashMap returnMap = null;
            returnMap = new HashMap();
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            final String command = (String) map.get("COMMAND");
            final String mode = (String) map.get("MODE");
            if (mode.equals(CommonConstants.PARTIAL_WITHDRAWL)) {//"PARTIAL_WITHDRAWAL"
                listTOs = (ArrayList) map.get("PARTIALWITHDRAWALTO");
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    this.insertPartialWithDrawalList();
                    returnMap = new HashMap();
                    returnMap.put("DEPOSIT_ACT_NUM", objTO.getDepositNo());
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    this.insertPartialWithDrawalList();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            } else if (mode.equals(CommonConstants.NORMAL_CLOSURE) && map.get("AUTHORIZEMAP") == null) {//"NORMAL"
                doDepositClose(map, enableTrans, mode);
            } else if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.get("AUTHORIZEMAP") == null) { //"PREMATURE"
                doDepositClose(map, enableTrans, mode);
            } else if (mode.equals(CommonConstants.TRANSFER_OUT_CLOSURE) && map.get("AUTHORIZEMAP") == null) { //"PREMATURE"
                doDepositClose(map, enableTrans, mode);
            } else if (map.containsKey("AUTHORIZEMAP") && map.get("AUTHORIZEMAP") != null) {
                doAuthorize(map);
            }
            if (enableTrans) {
                sqlMap.commitTransaction();
            }
            System.out.println("returnMap :" + returnMap);
            ServiceLocator.flushCache(sqlMap);
            System.gc();
            return returnMap;
        } catch (Exception e) {

            if (enableTrans) {
                sqlMap.rollbackTransaction();
            }

            e.printStackTrace();
            throw e;
        }

        //        destroyObjects();

    }

    private String getInter_TransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INTER_BRANCH_TRANS_NO");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return executeQuery(obj, true);
    }

    public HashMap executeQuery(HashMap obj, boolean enableTrans) throws Exception {
        System.out.println("#### Inside DepositClosingDAO executeQuery..." + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        System.out.println("executeQuery CustId" + obj.get("CUST_ID"));
        //        HashMap output =  getData(obj, enableTrans);
        //        return output;
        HashMap output = new HashMap();
        if (obj.containsKey("TDS_CALCULATION")) {
            output = calcuateTDS(obj, enableTrans);
        } else {
            output = getData(obj, enableTrans);
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return output;
    }

    private void makeOldNewTransaction(HashMap transMap, HashMap map) throws Exception {// If Edit mode
        HashMap transactionMap = new HashMap();
        HashMap closureMap = (HashMap) map.get("CLOSUREMAP");//ClosureMap
        transactionMap = transMap;
        accountClosingTO = new AccountClosingTO();
        HashMap transDetMap = (HashMap) transMap.get("TRANS_DETAILS");
        System.out.println("########makeOldNewTransaction transDetMap : " + transDetMap);
        double tdsAmt = CommonUtil.convertObjToDouble(closureMap.get("TDS_SHARE")).doubleValue();
        if (transactionMap != null) {
            HashMap acHeadMap = new HashMap();
            HashMap acHeads = new HashMap();
            acHeadMap.put("PROD_ID", closureMap.get("PROD_ID"));
            String prodId = CommonUtil.convertObjToStr(acHeadMap.get("PROD_ID"));
            if (prodId != null && !prodId.equals("") && prodId.length() > 0) {
                acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", acHeadMap);
            }
            System.out.println("########makeOldNewTransaction acHeads : " + acHeads);
            System.out.println("####### makeOldNewTransaction map " + map);
            System.out.println("####### makeOldNewTransaction transactionMap " + transactionMap);
            System.out.println("####### makeOldNewTransaction transDetMap " + transDetMap);
            TransactionTO transactionTODebit = new TransactionTO();
            LinkedHashMap transactionDetailsMap = new LinkedHashMap();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            if (map.containsKey("TransactionTO")) {
                transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO;
                if (transactionDetailsMap.size() > 0) {
                    if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        System.out.println("####### makeOldNewTransaction transactionTO " + transactionTO);
                    }
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_CASH")) {//from here to cash transaction...
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_AMT_DETAILS_CASH");
                tempMap.put("TRANS_DT", curDate);
                tempMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("DEPOSIT_CLOSING_AMT_DETAILS_CASH tempMap :" + tempMap);
                if (tempMap != null) {
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    System.out.println("####### AMT oldAmount" + oldAmount);
                    CashTransactionTO txTransferTO = null;
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null && lst.size() > 0) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                            if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            }
                            txTransferTO.setStatusDt(curDate);
                            txTransferTO.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            //akhil@
                            txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transactionMap.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            transactionMap.put("CashTransactionTO", txTransferTO);
                            transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                            transactionMap.put("PRODUCTTYPE", "TD");
                            transactionMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            System.out.println("####### cash AMT transactionMap " + transactionMap);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(transactionMap, false);
                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_INT_DETAILS_CASH")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_INT_DETAILS_CASH");
                tempMap.put("TRANS_DT", curDate);
                tempMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("DEPOSIT_CLOSING_INT_DETAILS_CASH tempMap :" + tempMap);
                if (tempMap != null) {
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();
                    System.out.println("####### INT oldAmount" + oldAmount);
                    CashTransactionTO txTransferTO = null;
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null && lst.size() > 0) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                            //akhil@
                            txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                            if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            }
                            txTransferTO.setStatusDt(curDate);
                            txTransferTO.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transactionMap.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            transactionMap.put("CashTransactionTO", txTransferTO);
                            transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                            transactionMap.put("PRODUCTTYPE", "TD");
                            transactionMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            System.out.println("####### cash INT transactionMap " + transactionMap);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(transactionMap, false);
                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH");
                tempMap.put("TRANS_DT", curDate);
                tempMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH tempMap :" + tempMap);
                if (tempMap != null) {
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();
                    System.out.println("####### PAY_INT oldAmount" + oldAmount);
                    CashTransactionTO txTransferTO = null;
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null && lst.size() > 0) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                            //akhil@
                            txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));

                            if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            }
                            txTransferTO.setStatusDt(curDate);
                            txTransferTO.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transactionMap.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            transactionMap.put("CashTransactionTO", txTransferTO);
                            transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                            transactionMap.put("PRODUCTTYPE", "TD");
                            transactionMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            System.out.println("####### cash PAY transactionMap " + transactionMap);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(transactionMap, false);
                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    tempMap = null;
                }
            }//upto here cash transaction details deleting.....

            if (transactionMap.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER");
                System.out.println("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER tempMap :" + tempMap);
                if (tempMap != null) {
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", curDate);
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.remove("TRANS_ID");
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    System.out.println("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER :" + oldAmount);
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(curDate);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                tempMap.put("TRANS_ID", transactionMap.get("TRANS_ID"));
                                batchList.add(txTransferTO);
                            }
                            System.out.println("####### cash batchList DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER " + batchList);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonUtil.convertObjToStr(map.get("COMMAND")));
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER");
                System.out.println("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER tempMap :" + tempMap);
                if (tempMap != null) {
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", curDate);
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.remove("TRANS_ID");
                    System.out.println("DEPOSIT_CLOSING_INT_DETAILS tempMap: " + tempMap);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();
                    System.out.println("DEPOSIT_CLOSING_INT_DETAILS " + oldAmount);
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(curDate);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                tempMap.put("TRANS_ID", transactionMap.get("TRANS_ID"));
                                batchList.add(txTransferTO);
                            }
                            System.out.println("DEPOSIT_CLOSING_INT_DETAILS batchList:" + batchList);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonUtil.convertObjToStr(map.get("COMMAND")));
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER");
                System.out.println("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER tempMap :" + tempMap);
                if (tempMap != null) {
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", curDate);
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.remove("TRANS_ID");
                    System.out.println("DEPOSIT_CLOSING_PAY_INT_DETAILS tempMap:" + tempMap);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();
                    System.out.println("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER" + oldAmount);
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(curDate);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                tempMap.put("TRANS_ID", transactionMap.get("TRANS_ID"));
                                batchList.add(txTransferTO);
                            }
                            System.out.println("DEPOSIT_CLOSING_PAY_INT_DETAILS batchList :" + batchList);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonUtil.convertObjToStr(map.get("COMMAND")));
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER");
                System.out.println("DEPOSIT_CLOSING_TDS_DETAILS tempMap :" + tempMap);
                if (tempMap != null) {
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", curDate);
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.remove("TRANS_ID");
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(curDate);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                tempMap.put("TRANS_ID", transactionMap.get("TRANS_ID"));
                                batchList.add(txTransferTO);
                            }
                            System.out.println("DEPOSIT_CLOSING_TDS_DETAILS batchList :" + batchList);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonUtil.convertObjToStr(map.get("COMMAND")));
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            }
            if (transactionMap.containsKey("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER")) {
                HashMap tempMap = (HashMap) transactionMap.get("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER");
                System.out.println("DEPOSIT_CLOSING_DELAY_DETAILS tempMap" + tempMap);
                if (tempMap != null) {
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", curDate);
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.remove("TRANS_ID");
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    //                    double newAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if (oldAmount > 0) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(curDate);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                txTransferTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                transactionMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                                tempMap.put("TRANS_ID", transactionMap.get("TRANS_ID"));
                                batchList.add(txTransferTO);
                            }
                            System.out.println("####### cash batchList DEPOSIT_CLOSING_AMT_DETAILS " + batchList);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.setLinkBatchId(closureMap.get("DEPOSIT_NO") + "_1");
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonUtil.convertObjToStr(map.get("COMMAND")));
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                }
            }
            //            String actNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
            //            if (actNo.lastIndexOf("_")!=-1)
            //                actNo = actNo.substring(0,actNo.lastIndexOf("_"));
            //            System.out.println("#######delete actNum : "+actNo);
            //            HashMap actMap = new HashMap();
            //            actMap.put("ACT_NUM",actNo);
            //            actMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_REJECTED);
            //            actMap.put("STATUS", CommonConstants.STATUS_DELETED);
            //            sqlMap.executeUpdate("updateDeleteDepositActClosing", actMap);

            if (map.get("COMMAND").equals("DELETE")) {
                HashMap singleAuthorizeMap = new HashMap();
                String actNum = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                singleAuthorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                singleAuthorizeMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                singleAuthorizeMap.put("ACCOUNTNO", actNum + "_1");
                singleAuthorizeMap.put("AUTHORIZEDT", curDate);
                sqlMap.executeUpdate("authorizeDepositAccountClose", singleAuthorizeMap);
                if (actNum.lastIndexOf("_") != -1) {
                    actNum = actNum.substring(0, actNum.lastIndexOf("_"));
                }
                System.out.println("#######delete actNum : " + actNum);
                HashMap ltdBalanceMap = new HashMap();
                String closedStatus = null;
                String closeStatus = null;
                String type = null;
                ltdBalanceMap.put("DEPOSITNO", actNum);
                List lastApplDt = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", ltdBalanceMap);
                if (lastApplDt != null && lastApplDt.size() > 0) {
                    ltdBalanceMap = (HashMap) lastApplDt.get(0);
                    closedStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("ACCT_STATUS"));
                    closeStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("STATUS"));
                    type = CommonUtil.convertObjToStr(ltdBalanceMap.get("PAYMENT_TYPE"));
                }
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                sqlMap.executeUpdate("authorizeDepositSubAccountClose", singleAuthorizeMap);

                HashMap rejectMap = new HashMap();
                rejectMap.put("DEPOSIT_NO", actNum);
                singleAuthorizeMap.put("ACCOUNTNO", actNum);
                ltdBalanceMap.put("DEPOSITNO", actNum);
                ltdBalanceMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                ltdBalanceMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                ltdBalanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                ltdBalanceMap.put("AUTHORIZEDT", curDate);

                if (closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("ACCT_STATUS", "MATURED");
                    ltdBalanceMap.put("NORM_STATUS", "LTDMODIFIED");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "MATURED");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTD MATURED : " + ltdBalanceMap);
                } else if (closeStatus.equals("LIEN") && closedStatus.equals("CLOSED")) {
                    ltdBalanceMap.put("NORM_STATUS", "LIEN");
                    ltdBalanceMap.put("ACCT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "MATURED");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("LTDMODIFIED CLOSED : " + ltdBalanceMap);
                } else if (closeStatus.equals("LIEN") && closedStatus.equals("MATURED")) {
                    ltdBalanceMap.put("NORM_STATUS", "LIEN");
                    ltdBalanceMap.put("ACCT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLTDModified", ltdBalanceMap);//Updating deposit_sub_acinfo table...
                    ltdBalanceMap.put("DEPOSIT_STATUS", "NEW");
                    sqlMap.executeUpdate("updateStatusLienAcinfoModified", ltdBalanceMap);//Updating deposit_acinfo table...
                    System.out.println("MODIFIED CLOSED : " + ltdBalanceMap);
                } else {
                    sqlMap.executeUpdate("updateSubDepositCloseNew", rejectMap);
                    sqlMap.executeUpdate("updateDepositAcinfoCloseNew", rejectMap);
                }
                System.out.println("#######delete singleAuthorizeMap : " + singleAuthorizeMap);
                singleAuthorizeMap.put("STATUS", "");
                singleAuthorizeMap.put("DELETE", CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("authorizeUpdateAccountCloseTOForDep", singleAuthorizeMap);
                sqlMap.executeUpdate("updatePenalInt", singleAuthorizeMap);
                System.out.println("#######delete singleAuthorizeMap : " + singleAuthorizeMap);
            }
            transactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", transactionTO);
            if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("RM")) {
                HashMap payMap = new HashMap();
                payMap.put("STATUS", CommonConstants.STATUS_DELETED);
                payMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO") + "_1");
                sqlMap.executeUpdate("updateSelectDepositPayOrder", payMap);
            }
            HashMap transOutMap = new HashMap();
            transOutMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
            List transList = sqlMap.executeQueryForList("getTransferOutDeposit", transOutMap);
            if (transList != null && transList.size() > 0) {
                transOutMap = (HashMap) transList.get(0);
                transOutMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                transOutMap.put("AUTHORIZE_DT", curDate);
                transOutMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                System.out.println("transOutMap :" + transOutMap);
                sqlMap.executeUpdate("updateTransferOutRejectionDeposits", transOutMap);
            }
            if (closureMap.containsKey("TRANSFER_OUT_MODE")) {
                transOutMap = new HashMap();
                transOutMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                transOutMap.put("TRANS_OUT", closureMap.get("TRANSFER_OUT_MODE"));
                sqlMap.executeUpdate("updateTransferOutFlag", transOutMap);
            }
        }
    }

    private void doDepositClose(HashMap map, boolean enableTrans, String mode) throws Exception {
        System.out.println("doDepositClose map " + map);
        HashMap closureMap = (HashMap) map.get("CLOSUREMAP");//ClosureMap
        double balance = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_BALANCE")).doubleValue();
        if (!map.containsKey("TERMMAP")) {
            closureMap.put("INTEREST_AMT", new Double(CommonUtil.convertObjToStr(closureMap.get("INTEREST_AMT"))));
        }
        if (map.containsKey("TERMMAP")) {
            HashMap termMap = (HashMap) map.get("TERMMAP");
            if (termMap != null) {
                closureMap.put("PENALTY_INT", termMap.get("PENALTY_INT"));
                closureMap.put("CURR_RATE_OF_INT", CommonUtil.convertObjToDouble(termMap.get("CURR_RATE_OF_INT")));
                closureMap.put("PENAL_RATE", termMap.get("PENAL_RATE"));
                closureMap.put("TYPES_OF_DEPOSIT", "");
                closureMap.put("INTEREST_AMT", new Double(CommonUtil.convertObjToStr(termMap.get("INTEREST_AMT"))));
                termMap = null;
            }
        }
        if (closureMap.containsKey("LTDCLOSINGDATA")) {//this condition will work only ltd deposits ohterwise it wont work.
            double addloanIntAmt=0;
            if(closureMap.containsKey("ADD_LOAN_INT_AMOUNT")){
               addloanIntAmt=CommonUtil.convertObjToDouble(closureMap.get("ADD_LOAN_INT_AMOUNT")) ;
            }
            HashMap ltdClosingMap = new HashMap();
            ltdClosingMap = (HashMap) closureMap.get("LTDCLOSINGDATA");
            System.out.println("doDepositClose ltdClosingMap " + ltdClosingMap);
            if (ltdClosingMap != null && ltdClosingMap.size() > 0) {
                HashMap totalMap = new HashMap();
                accountClosingDAO = new AccountClosingDAO();
                ltdClosingMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                ltdClosingMap.put("USER_ID", map.get("USER_ID"));
                ltdClosingMap.put("DEPOSIT_TRANSACTION", "DEPOSIT_TRANSACTION");
                ltdClosingMap.put("MODE", map.get("COMMAND"));
                ltdClosingMap.put("AUTHORIZEMAP", null);
                ltdClosingMap.put("PROD_TYPE", "TermLoan");
                ltdClosingMap.put("BEHAVE_LIKE", "LOANS_AGAINST_DEPOSITS");
                totalMap = (HashMap) ltdClosingMap.get("TOTAL_AMOUNT");
                totalMap.put("CALCULATE_INT", ltdClosingMap.get("CALCULATE_INT"));
                totalMap.put("CALCULATE_PENAL_INT", ltdClosingMap.get("CALCULATE_PENAL_INT"));
                double interest=0;
                if(totalMap.get("INTEREST")!=null && !totalMap.get("INTEREST").equals("")){
                      interest =CommonUtil.convertObjToDouble(totalMap.get("INTEREST"));
                }else{
                    interest=0;
                }
               // interest =CommonUtil.convertObjToDouble(totalMap.get("INTEREST"));
                if (interest == 0) {
                    totalMap.put("INTEREST", ltdClosingMap.get("INTEREST"));
                    totalMap.put("CURR_MONTH_INT", ltdClosingMap.get("INTEREST"));
                }
                ltdClosingMap.put("TOTAL_AMOUNT", totalMap);
                ltdClosingMap.put("ADD_LOAN_INT_AMOUNT", addloanIntAmt);
                if (closureMap.containsKey("BEHAVES_LIKE") && !closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                    ltdClosingMap.put("DEPOSIT_AMT", closureMap.get("DEPOSIT_AMT"));
                }
                ltdClosingMap.put("DEPOSIT_INTEREST_AMT", closureMap.get("INTEREST_AMT"));
                ltdClosingMap.put("ADD_INT_AMOUNT", closureMap.get("ADD_INT_AMOUNT"));
                ltdClosingMap.put("DEPOSITCLOSINGDAO", "DEPOSITCLOSING");
                HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", closureMap);
                ltdClosingMap.put("ACC_HEADS",acHeads);
                //KD-3579 : TERM DEPOSIT LTD CLOSING
                HashMap loanCloseReturnMap = accountClosingDAO.execute(ltdClosingMap);
                if(loanCloseReturnMap != null && loanCloseReturnMap.containsKey("TRANSFER_TRANS_LIST") &&  loanCloseReturnMap.get("TRANSFER_TRANS_LIST") != null){
                    List loanCloseTransList =(List) loanCloseReturnMap.get("TRANSFER_TRANS_LIST");
                    if(loanCloseTransList != null && loanCloseTransList.size() > 0){
                        HashMap loanCloseTransMap = (HashMap) loanCloseTransList.get(0);
                        if(loanCloseTransMap.containsKey("SINGLE_TRANS_ID") && loanCloseTransMap.get("SINGLE_TRANS_ID") != null){
                            closureMap.put("LTD_CLOSE_SINGLE_TRANS_ID",loanCloseTransMap.get("SINGLE_TRANS_ID"));
                        }
                    }
                }
                
                System.out.println("nithya :: loanCloseReturnMap :: " + loanCloseReturnMap);
                if (ltdClosingMap.containsKey("LTD_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("LTD_CLOSING_STATUS")).equals("CLOSE")
                        && ltdClosingMap.containsKey("FULLY_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                    closureMap.put("CLOSING_STATUS", "CLOSE");
                    closureMap.put("FULLY_CLOSING_STATUS", "FULLY_CLOSING");
                } else if (ltdClosingMap.containsKey("LTD_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("LTD_CLOSING_STATUS")).equals("CLOSE")
                        && ltdClosingMap.containsKey("FULLY_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("FULLY_CLOSING_STATUS")).equals("PARTIALLY_CLOSING")) {
                    closureMap.put("CLOSING_STATUS", "DONT_CLOSE");
                    closureMap.put("FULLY_CLOSING_STATUS", "PARTIALLY_CLOSING");
                } else if (ltdClosingMap.containsKey("LTD_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("LTD_CLOSING_STATUS")).equals("DONT_CLOSE")
                        && ltdClosingMap.containsKey("FULLY_CLOSING_STATUS")
                        && CommonUtil.convertObjToStr(ltdClosingMap.get("FULLY_CLOSING_STATUS")).equals("PARTIALLY_CLOSING")) {
                    closureMap.put("FULLY_CLOSING_STATUS", "PARTIALLY_CLOSING");
                }
                totalMap = null;
                ltdClosingMap = null;
            }
        } else {
            closureMap.put("CLOSING_STATUS", "CLOSE");
            closureMap.put("FULLY_CLOSING_STATUS", "FULLY_CLOSING");
        }
        HashMap transactionMap = (HashMap) map.get("TransactionTO");
        closureMap.put("PROD_ID", closureMap.get("PROD_ID"));
        closureMap.put("PROD_TYPE", closureMap.get("PROD_TYPE"));
        TdsCalc tdsCalculator = new TdsCalc(_branchCode);
        HashMap calculationMap = new HashMap();
        //Call insert tds
        double intTrfAmt = 0.0;
        interestBatchTO = new InterestBatchTO();


        TdsCalc tds = new TdsCalc(_branchCode);
        String CustId = CommonUtil.convertObjToStr(closureMap.get("CUST_ID"));
        String Prod_type = "TD";
        String prod_id = CommonUtil.convertObjToStr(closureMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
        intTrfAmt = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue();

        //        if(CommonUtil.convertObjToDouble(closureMap.get("TDS_SHARE")).doubleValue() > 0) {
        //            tdsCalculator.setInsertData(CustId,Prod_type,prod_id,accnum,null,null,null);
        //            closureMap.put("TDS_AMT",tdsCalculator.getTdsAmt());
        //        }

        //Call insert interest
        if (closureMap.get("INTEREST") != null) {
            //Call Rahul's class here ......
            TaskHeader header = new TaskHeader();
            header.setTaskClass("IntPayableTask");
            header.setTransactionType(CommonConstants.PAYABLE);
            header.setProductType(TransactionFactory.DEPOSITS);

            header.setBranchID(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            header.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            header.setUserID(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

            //__ Account Nos are comming as a List...
            ArrayList acctList = new ArrayList();
            acctList.add(CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO")) + "_"
                    + CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_SUB_NO")));
            System.out.println("####### doDepositClose acctList " + acctList);


            HashMap taskParam = new HashMap();
            taskParam.put(CommonConstants.BRANCH, header.getBranchID());
            taskParam.put(CommonConstants.PRODUCT_ID, closureMap.get("PROD_ID"));
            taskParam.put(CommonConstants.ACT_NUM, acctList);

            header.setTaskParam(taskParam);
            DepositIntTask tsk = new DepositIntTask(header, enableTrans);
            tsk.insertData();
        }
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setBatchId(CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO")) + "_"
                + CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_SUB_NO")));
        transactionDAO.setBatchDate(curDate);
        map.put("MODE", map.get("COMMAND"));
        closureMap.put("COMMAND", map.get("COMMAND"));
        String closingStatus = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (map.containsKey("TransactionTO")) {
            if(CommonUtil.convertObjToStr(closureMap.get("BEHAVES_LIKE")).equalsIgnoreCase("RECURRING")){ // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation. [ RD Closing code lines rewritten and created new function ]
              calculationMap = insertAccHead_RD_new(closureMap, map, CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)), mode);  
            }else{
              calculationMap = insertAccHead(closureMap, map, CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)), mode);
            }
        }
        if (!closingStatus.equals("DELETE")) {
            accountClosingTO = new AccountClosingTO();
            accountClosingTO.setActNum(CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO")));
            accountClosingTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            accountClosingTO.setStatusDt(curDate);
            accountClosingTO.setUnusedChk(CommonUtil.convertObjToDouble(calculationMap.get("PENAL_AMT")));
            accountClosingTO.setActClosingChrg(CommonUtil.convertObjToDouble(calculationMap.get("TDS_AMT")));
            accountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(calculationMap.get("INT_PAY_AMT")));
            accountClosingTO.setChrgDetails(CommonUtil.convertObjToDouble(calculationMap.get("INT_AMT")));
            accountClosingTO.setPayableBal(CommonUtil.convertObjToDouble(calculationMap.get("PAY_AMT")));
            if (closingStatus.equals("INSERT")) {
                accountClosingTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertAccountClosingTO", accountClosingTO);
            } else {
                accountClosingTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateAccountClosingTOForDep", accountClosingTO);
            }
            System.out.println("####### doDepositClose calculationMap " + calculationMap);

            closureMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
            closureMap.put("CLOSE_DT", curDate);

            //-- Close the A/c now. Update the tables Deposit_AcInfo, Deposit_Sub_AcInfo
            String head = null;
            String prodId = null;
            LinkedHashMap transactionDetailsMap = new LinkedHashMap();
            TransactionTO transactionTO = new TransactionTO();
            LinkedHashMap allowedTransDetailsTO;
            if (map.containsKey("TransactionTO") && closureMap.containsKey("CLOSING_STATUS")
                    && CommonUtil.convertObjToStr(closureMap.get("CLOSING_STATUS")).equals("CLOSE")) {
                transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (transactionDetailsMap.size() > 0) {
                    if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        if (allowedTransDetailsTO != null) {
                            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        }
                        System.out.println("transactionTO : " + transactionTO);
                    }
                }
            }
            HashMap ltdMap = new HashMap();
            System.out.println("####### doDepositClose closureMap: " + closureMap);
            HashMap headMap = new HashMap();
            headMap.put("PROD_ID", closureMap.get("PROD_ID"));
            List lst = sqlMap.executeQueryForList("getMaturedAccountHead", headMap);
            if (lst != null && lst.size() > 0) {
                headMap = (HashMap) lst.get(0);
                head = transactionTO.getDebitAcctNo();
                prodId = CommonUtil.convertObjToStr(headMap.get("MATURITY_DEPOSIT"));
                if (head != null && head.length() > 0) {
                    if (head.equals(prodId)) {
                    } else {
                        head = "GL";
                        prodId = "OTHER";
                    }
                }
            }
            //added by chithra 17-05-14
            String intamt = CommonUtil.convertObjToStr(closureMap.get("INTEREST_AMT"));
            String addint = CommonUtil.convertObjToStr(closureMap.get("ADD_INT_AMOUNT"));
              String addlonInt = CommonUtil.convertObjToStr(closureMap.get("ADD_LOAN_INT_AMOUNT"));
            if (addint != null && CommonUtil.convertObjToDouble(addint) > 0) {
                double totInt = CommonUtil.convertObjToDouble(intamt) + CommonUtil.convertObjToDouble(addint);
                closureMap.remove("INTEREST_AMT");
                closureMap.put("INTEREST_AMT", totInt);
                String totat = CommonUtil.convertObjToStr(closureMap.get("TOTAL_AMT"));
                double totamtt = CommonUtil.convertObjToDouble(totat) + CommonUtil.convertObjToDouble(addint);
                closureMap.remove("TOTAL_AMT");
                closureMap.put("TOTAL_AMT", totamtt);

            }
            if (closureMap.containsKey("MODE") && closureMap.get("MODE") != null && closureMap.get("MODE").equals("NORMAL")) {
                closureMap.put("FLEXI_STATUS", "NR");
                closureMap.put("PENALTY_INT", "NO");
            } else if (closureMap.containsKey("MODE") && closureMap.get("MODE") != null && closureMap.get("MODE").equals("PREMATURE")) {
                closureMap.put("FLEXI_STATUS", "PC");
            }
            if (transactionTO.getTransType().equals("TRANSFER") && transactionTO.getProductId().equals("")
                    && transactionTO.getProductType().equals("GL")) {
                closureMap.put("ACT_NUM", closureMap.get("DEPOSIT_NO"));
                if (head.equals(prodId)) {
                    closureMap.put("DEPOSIT_STATUS", "MATURED");
                    closureMap.put("ACCT_STATUS", "MATURED");
                    System.out.println("fd dep other than matured head..");
                    closureMap.put("CURR_RATE_OF_INT",CommonUtil.convertObjToDouble(closureMap.get("CURR_RATE_OF_INT")));
                    sqlMap.executeUpdate("updateDepositAcinfoMatClose", closureMap);
                    sqlMap.executeUpdate("updateSubDepositStatus", closureMap);
                } else {
                    closureMap.put("DEPOSIT_STATUS", "CLOSED");
                    closureMap.put("ACCT_STATUS", "CLOSED");
                    closureMap.put("CURR_RATE_OF_INT",CommonUtil.convertObjToDouble(closureMap.get("CURR_RATE_OF_INT")));
                    sqlMap.executeUpdate("updateDepositAcinfoMatClose", closureMap);
                    sqlMap.executeUpdate("updateSubDepositStatus", closureMap);
                }
                System.out.println("doDepositClose closureMap LIEN :" + closureMap);
                sqlMap.executeUpdate("updateDepositAcinfoMatClose", closureMap);
                sqlMap.executeUpdate("updateSubDepositStatus", closureMap);
                closureMap.put("ACCT_HEAD", transactionTO.getDebitAcctNo());
                //                sqlMap.executeUpdate("updateCloseDEPOSIT", closureMap);
            } else {
                System.out.println("#@#@#@#@#@#@# postgres change : "+closureMap);
                closureMap.put("CURR_RATE_OF_INT", CommonUtil.convertObjToDouble(closureMap.get("CURR_RATE_OF_INT")));
                sqlMap.executeUpdate("updateSubDepositClose", closureMap);
                sqlMap.executeUpdate("updateDepositAcinfoClose", closureMap);
                closureMap.put("ACCT_HEAD", "");
                //                sqlMap.executeUpdate("updateCloseDEPOSIT", closureMap);
            }
            if (closureMap.containsKey("TRANSFER_OUT_MODE")) {
                HashMap transOutMap = new HashMap();
                transOutMap.put("DEPOSIT_NO", closureMap.get("DEPOSIT_NO"));
                transOutMap.put("TRANS_OUT", closureMap.get("TRANSFER_OUT_MODE"));
                sqlMap.executeUpdate("updateTransferOutFlag", transOutMap);
            }
        }

        //this query executes is_tds_applied for yes is updated for a particlar table name.
        String depositNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
        String subNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_SUB_NO"));

        String depositSubNo = depositNo + "_" + subNo;

        HashMap hash = new HashMap();
        hash.put("DEPOSIT_NO", depositSubNo);
        hash.put("CUSTID", closureMap.get("CUST_ID"));
        //        hash.put("PRODID",closureMap.get("PROD_ID"));
        hash.put("TDS_AMT", tdsCalculator.getTdsAmt() == null ? new Double(0) : tdsCalculator.getTdsAmt());
        System.out.println("doDepositClose hash" + hash);
        sqlMap.executeUpdate("updateDepositInterestTaxApplied", hash);
        System.out.println("downdoDepositClose hash" + hash);

        // -- This qry executes only if all sub A/c are closed. Handled in qry itself
        sqlMap.executeUpdate("updateMasterDepositClose", closureMap);
        System.out.println("Completed for Particular Deposit Transactions...");
        calculationMap = null;
        map = null;
        closureMap = null;
        transactionMap = null;
        hash = null;
//        HashMap amap = new HashMap();
//        amap.put("DEPOSIT_NO", depositNo);
//        amap.put("PAID_DATE", curDate);
//        sqlMap.executeUpdate("upadateInterestDetails", amap);
    }

    private void destroyObjects() {
        objTO = null;
    }
	//By Nidhin 20-05-2014
private double getTotalAmounts(){
    double depositClosingCharge = 0.0;
    HashMap chargeMap = new HashMap();
    if (chargeLst != null && chargeLst.size() > 0) {
        for (int i = 0; i < chargeLst.size(); i++) {
            chargeMap = (HashMap) chargeLst.get(i);
            if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                depositClosingCharge += CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
            }
        }
    }
    return CommonUtil.convertObjToDouble(depositClosingCharge);
}
    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
//added by chithra for service Tax
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    //*************************** Modified function *************************************************//
    // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation. [ RD Closing code lines rewritten and created new function ]
    public HashMap insertAccHead_RD_new(HashMap closureMap, HashMap map, String branchID, String mode) throws Exception {
        System.out.println("inside insertAccHead_RD_new ::insertAccHead map:" + map + "closureMap:" + closureMap + "branchID:" + branchID);
        String behavesLike = CommonUtil.convertObjToStr(closureMap.get("BEHAVES_LIKE"));        
        closureMap = (HashMap) map.get("CLOSUREMAP");
        HashMap serviceChargeMap = new HashMap();
        HashMap serviceTaxDetails = new HashMap();
        HashMap serviceTaxDetailsForLoan = new HashMap();
        chargeLst = new ArrayList();
        String isTransferCharges = "";
        HashMap ChargeTranByTransferMap = new HashMap();
        String lien = CommonUtil.convertObjToStr(closureMap.get("LIEN_STATUS"));
        List isChargeTranByTransferList = sqlMap.executeQueryForList("getIsChargesTransactionByTransfer", closureMap);
        if (isChargeTranByTransferList != null && isChargeTranByTransferList.size() > 0) {
            ChargeTranByTransferMap = (HashMap) isChargeTranByTransferList.get(0);
            if (ChargeTranByTransferMap.containsKey("IS_TRANFER_CHARGES") && ChargeTranByTransferMap.get("IS_TRANFER_CHARGES") != null) {
                isTransferCharges = CommonUtil.convertObjToStr(ChargeTranByTransferMap.get("IS_TRANFER_CHARGES"));
            }
        }
        if (lien != null && lien.length() > 0) {
            isTransferCharges = "Y";
        }
        if (closureMap.containsKey("SERVICE_CHARGE_DETAILS")) {
            serviceChargeMap = (HashMap) closureMap.get("SERVICE_CHARGE_DETAILS");
        }
        if (closureMap.containsKey("Charge List Data") && closureMap.get("Charge List Data") != null) {
            chargeLst = (List) closureMap.get("Charge List Data");            
            if (closureMap.containsKey("serviceTax_Details") && closureMap.get("serviceTax_Details") != null) {
                serviceTaxDetails = (HashMap) closureMap.get("serviceTax_Details");
            }          
        } else if (closureMap.containsKey("serviceTax_Details") && closureMap.get("serviceTax_Details") != null) {
            serviceTaxDetails = (HashMap) closureMap.get("serviceTax_Details");
        }
        String debitBranchID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        String creditBranchID = CommonUtil.convertObjToStr(map.get("BRANCH_ID"));
        String initiatedBranch = CommonUtil.convertObjToStr(map.get(CommonConstants.INITIATED_BRANCH));
        HashMap transMap = new HashMap();
        if (closureMap.containsKey("OLDTRANSACTION")) {
            transMap = (HashMap) closureMap.get("OLDTRANSACTION");
        }
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", closureMap);       
        String head = null;
        String direct = null;
        String closedStatus = null;
        String closeStatus = null;
        HashMap returnCalcMap = new HashMap();
        HashMap loanMap = new HashMap();
        HashMap othBankMap = new HashMap();
        interestBatchTO = new InterestBatchTO();
        HashMap txMap = new HashMap();
        ArrayList transferList = new ArrayList();
        transactionDAO.setInitiatedBranch(initiatedBranch);               
        String closingStatus = CommonUtil.convertObjToStr(map.get("COMMAND"));      
        double servicePercentage = 0.0;
        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
            servicePercentage = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(acHeads.get("SERVICE_CHARGE"))).doubleValue();
        }
        String depositNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
        String subNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_SUB_NO"));
        String depositSubNo = depositNo + "_" + subNo;        
        String generateSingleTransId = generateLinkID();
        String generateSingleCashId = "";
        HashMap ltdBalanceMap = new HashMap();
        ltdBalanceMap.put("DEPOSITNO", depositNo);
        List lastApplDt = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", ltdBalanceMap);
        if (lastApplDt != null && lastApplDt.size() > 0) {
            ltdBalanceMap = (HashMap) lastApplDt.get(0);
            closedStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("ACCT_STATUS"));
            closeStatus = CommonUtil.convertObjToStr(ltdBalanceMap.get("STATUS"));            
        }
        if (transMap != null && transMap.size() > 0) {
            System.out.println("insertAccHead transMap if condition " + transMap);
            transMap.put("TRANS_DETAILS", closureMap.get("TRANS_DETAILS"));
            if (closingStatus.equals("UPDATE") || map.get("MODE").equals("UPDATE") || closingStatus.equals("DELETE")) {
                makeOldNewTransaction(transMap, map);
                map.put("COMMAND", "INSERT");
                map.put("MODE", "INSERT");
                if ((closeStatus.equals("CREATED") || closeStatus.equals("LIEN")) && closedStatus.equals("CLOSED")) {
                    closedStatus = "NEW";
                } else if (closeStatus.equals("MODIFIED") && closedStatus.equals("MATURED")) {
                    closedStatus = "NEW";
                } else if (closeStatus.equals("MODIFIED") && closedStatus.equals("CLOSED")) {
                    closedStatus = "MATURED";
                } else if (closeStatus.equals("CREATED") && closedStatus.equals("MATURED")) {
                    closedStatus = "NEW";
                }
                System.out.println("transMap closeStatus:" + closeStatus + "transMap closedStatus:" + closedStatus);
            }            
        }      
        TxTransferTO transferTo = null;
        TransactionTO transactionTODebit = new TransactionTO();
        LinkedHashMap transactionDetailsMap = new LinkedHashMap();
        TransactionTO transactionTO;
        String instNo1 = null;
        String instNo2 = null;
        if (map.containsKey("TransactionTO")) {
            transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO;
            if (transactionDetailsMap.size() > 0) {
                if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null) {
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO != null && transactionTO.getTransType().equals("TRANSFER")) {
                            transactionTODebit.setTransType("TRANSFER");
                            String prodId = CommonUtil.convertObjToStr(transactionTODebit.getProductId());
                            transactionTODebit.setProductId(transactionTO.getProductId());
                            transactionTODebit.setProductType(TransactionFactory.OPERATIVE);
                            transactionTODebit.setDebitAcctNo(transactionTO.getDebitAcctNo());
                            transactionTODebit.setBranchId(transactionTO.getBranchId());                     
                            transactionTODebit.setScreenName((String) map.get(CommonConstants.SCREEN));
                            allowedTransDetailsTO.put("1", transactionTODebit);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);
                            transactionDAO.setTransType("DEBIT");
                            allowedTransDetailsTO.put("1", transactionTO);
                            transactionDetailsMap.put("NOT_DELETED_TRANS_TOs", allowedTransDetailsTO);
                            closureMap.put("TransactionTO", transactionDetailsMap);                           
                        }                        
                    }
                }
            }
        }        
        if (map.containsKey("TransactionTO")) {
            transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO;
            if (transactionDetailsMap.size() > 0) {
                if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    if (allowedTransDetailsTO != null) {
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        if (transactionTO != null && transactionTO.getProductType() != null) {
                            if (transactionTO.getProductType().equals("TL") || transactionTO.getProductType().equals("AD")) {
                                loanMap.put("PROD_ID", transactionTO.getProductId());
                                List lstLoan = sqlMap.executeQueryForList("getLienAccounts", transactionTO.getDebitAcctNo());
                                if (lstLoan != null && lstLoan.size() > 0) {
                                    loanMap = (HashMap) lstLoan.get(0);                                    
                                }
                            }
                            if (transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by Chithra 14-10-114
                                HashMap wMap = new HashMap();
                                wMap.put("REC_OTHER_BANK", transactionTO.getProductId());
                                List lstOB = sqlMap.executeQueryForList("getAccountHeadProd1", wMap);
                                if (lstOB != null && lstOB.size() > 0) {
                                    othBankMap = (HashMap) lstOB.get(0);
                                }
                            }
                            if (transactionTO != null && transactionTO.getProductType().equals("RM")) {
                                instNo1 = transactionTO.getChequeNo();
                                instNo2 = transactionTO.getChequeNo2();                                
                            }
                            if (transactionTO != null && transactionTO.getTransType().equals("CASH")) {
                                    generateSingleCashId = generateLinkID();
                            }
                        }
                    }
                }
            }
        }        
        HashMap closeMap = new HashMap();
        closeMap.put("PROD_ID", closureMap.get("PROD_ID"));
        List lst = sqlMap.executeQueryForList("getMaturedAccountHead", closeMap);
        if (lst != null && lst.size() > 0) {
            closeMap = (HashMap) lst.get(0);
            head = transactionTODebit.getDebitAcctNo();
            direct = CommonUtil.convertObjToStr(closeMap.get("MATURITY_DEPOSIT"));
            if (head != null && head.length() > 0) {
                if (head.equals(direct)) {
                } else {
                    head = "GL";
                    direct = "OTHER";
                }
            } else {
                head = "GL";
                direct = "OTHER";
            }
        }        
        HashMap headMap = new HashMap();
        headMap.put("DEPOSIT_NO", depositNo);
        List lstHead = sqlMap.executeQueryForList("getInstallTypeHead", headMap);
        if (lstHead != null && lstHead.size() > 0) {
            headMap = (HashMap) lstHead.get(0);           
        }
        double payAmt = 0.0;
        double interestAmt = 0.0;
        double balPay = 0.0;
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO.setLinkBatchID(depositSubNo);        
        if (!closingStatus.equals("DELETE") && closureMap.get("BEHAVES_LIKE") != null) {
            transactionTO = new TransactionTO();
            TransactionTO transTO = new TransactionTO();
            ArrayList cashList = new ArrayList();
            HashMap depIntMap = new HashMap();
            depIntMap.put("DEPOSIT_NO", depositSubNo);
            depIntMap.put("ROI", closureMap.get("ROI"));
            depIntMap.put("DEPOSIT_AMT", closureMap.get("DEPOSIT_AMT"));
            depIntMap.put("PROD_ID", closureMap.get("PROD_ID"));
            depIntMap.put("CUST_ID", closureMap.get("CUST_ID"));
            double prev_interst=0;
            double AgentCommAmt = CommonUtil.convertObjToDouble(closureMap.get("AGENT_COMMISION_AMT")).doubleValue();
            double penalAmt = CommonUtil.convertObjToDouble(closureMap.get("DELAYED_AMOUNT")).doubleValue();
            double clrAmt = CommonUtil.convertObjToDouble(closureMap.get("CLEAR_BALANCE")).doubleValue();
            double totalAmt = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_AMT")).doubleValue();
            double depAmt = CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")).doubleValue();
            double cashdepAmt = CommonUtil.convertObjToDouble(closureMap.get("DEPOSIT_AMT")).doubleValue();
            double tdsAmt = CommonUtil.convertObjToDouble(closureMap.get("TDS_SHARE")).doubleValue();
            double intPay = CommonUtil.convertObjToDouble(closureMap.get("PAY_AMT")).doubleValue(); 
            double paid = CommonUtil.convertObjToDouble(closureMap.get("PAID_INTEREST")).doubleValue();
            double creditInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();            
            double debitInt = CommonUtil.convertObjToDouble(closureMap.get("DR_INTEREST")).doubleValue();           
            double drawnAmt = CommonUtil.convertObjToDouble(closureMap.get("INTEREST_DRAWN")).doubleValue();
            prev_interst = CommonUtil.convertObjToDouble(closureMap.get("PREV_INTEREST_AMT")).doubleValue();
            double add_int_amt= CommonUtil.convertObjToDouble(closureMap.get("ADD_INT_AMOUNT")).doubleValue();
            double add_Loan_int_amt = CommonUtil.convertObjToDouble(closureMap.get("ADD_LOAN_INT_AMOUNT"));
            String addDays=CommonUtil.convertObjToStr(closureMap.get("ADD_INT_DAYS"));
            double interest = creditInt - drawnAmt;
            double pay_bal_Int=0;
            double rec_recivableAmt = 0;   
            double rdTotalChargeAmt = 0.0;
            double intAmtTobeDebited = 0.0;
            double amtFromDeposit = 0.0;
            boolean isDebitFormDeposit = false;
            boolean rdClosingChargeExists = false;
            boolean intDebitHead = false;
            double initialIntPay = intPay;
            double totalCharge = CommonUtil.convertObjToDouble(closureMap.get("TOTAL_CHARGE")).doubleValue();
            if (closureMap.containsKey("BOTH_PAY_REC")) {
                HashMap recPayMap = (HashMap) closureMap.get("BOTH_PAY_REC");
                if (recPayMap != null && recPayMap.containsKey("PAYABLE_REC_INT")) {
                    pay_bal_Int = CommonUtil.convertObjToDouble(recPayMap.get("PAYABLE_REC_INT"));
                    intPay = intPay + pay_bal_Int;
                }
            }else{
               intPay = intPay + prev_interst; 
            }
            String frmDate="",toDate="";
            if(closureMap.containsKey("FROM_INT_DATE")){
                frmDate=CommonUtil.convertObjToStr(closureMap.get("FROM_INT_DATE"));
            }
            if(closureMap.containsKey("TO_INT_DATE")){
                toDate=CommonUtil.convertObjToStr(closureMap.get("TO_INT_DATE"));
            }           
            totalAmt = totalAmt + penalAmt + AgentCommAmt;                                
            rec_recivableAmt = CommonUtil.convertObjToDouble(closureMap.get("REC_RECIVABLE"));            
            if (closedStatus.equals("NEW")) {
                if (creditInt < 0 || creditInt > 0) {
                    interestAmt = creditInt - intPay;                  
                    balPay = intPay;                    
                } else {
                    intPay = intPay;
                }
            } else if (closedStatus.equals("MATURED")) {
                balPay = interest;
                tdsAmt = 0.0;
                penalAmt = 0.0;               
            }           
            double loan_amount = 0, blncLonamt = 0;
            if (lien != null && lien.length() > 0) {
                HashMap loanDetMap = new HashMap();
                if (closureMap.containsKey("LTDCLOSINGDATA")) {
                    loanDetMap = (HashMap) closureMap.get("LTDCLOSINGDATA");
                }
                if (loanDetMap != null && loanDetMap.containsKey("AMOUNT")) {
                    loan_amount = CommonUtil.convertObjToDouble(loanDetMap.get("AMOUNT"));
                }
                if (depAmt < loan_amount) {
                    blncLonamt = loan_amount - depAmt;
                }
            }                      
            if (closureMap.containsKey("TRANSFER_OUT_MODE") && (closureMap.get("TRANSFER_OUT_MODE").equals("N") || closureMap.get("TRANSFER_OUT_MODE").equals("NO"))) {
                System.out.println("RECURRING Payable STARTED...");
                if (totalAmt >= clrAmt || totalAmt < clrAmt) {
                    double serviceCharge = 0.0;
                    if (map.containsKey("TransactionTO")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                }                                
                                System.out.println("interestAmt : balPay: " + balPay + "-" + interestAmt);
                                if (interestAmt > 0 || interestAmt < 0 || balPay > 0) {
                                    double rateofInt = CommonUtil.convertObjToDouble(closureMap.get("CURR_RATE_OF_INT")).doubleValue();
                                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && rateofInt == 0) {                                        
                                        interestAmt = balPay;
                                        balPay = 0.0;
                                    }
                                    if (interestAmt > 0) {                                        
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                       
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                       
                                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                        } else {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt txMap Payable: " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }
                                    if (interestAmt < 0) {
                                        interestAmt = interestAmt * -1;                                        
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                       
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        System.out.println("interestAmt Payable 1sttxMap : " + txMap);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }                                    
                                    if (balPay > 0) {                                                                        
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);                                        
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(balPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(balPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    }
                                    returnCalcMap.put("INT_PAY_AMT", new Double(balPay));
                                    returnCalcMap.put("INT_AMT", new Double(interestAmt));
                                } else {
                                    transferList = new ArrayList(); 
                                    if (!transactionTO.getTransType().equals("CASH")) {
                                        intPay = intPay - rec_recivableAmt;
                                    }
                                    if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                                        intPay = intPay - blncLonamt;
                                    }
                                   // System.out.println("intPay hereeee :: " + intPay);
                                    if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                    if (intPay > 0) {
                                        txMap = new HashMap();                                        
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));                                       
                                        txMap.put(TransferTrans.CURRENCY, "INR");    
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");                                        
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put("LINK_BATCH_ID", depositSubNo); //Added by nithya on 23-03-2020 for KD-1666
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(intPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);//                                      
                                        returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                                        returnCalcMap.put("INT_AMT", new Double(intPay));
                                    } 
                                    }else{    
                                    if (intPay > 0) {
                                        txMap = new HashMap();                                        
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);                                         
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, (String) closureMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");                                        
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put("LINK_BATCH_ID", depositSubNo); //Added by nithya on 23-03-2020 for KD-1666
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(intPay));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);//                                      
                                        returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                                        returnCalcMap.put("INT_AMT", new Double(intPay));
                                    } 
                                    }                                   
                                    if (rec_recivableAmt > 0) {                                        
                                        txMap = new HashMap();
                                        //transferList = new ArrayList();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);                                        
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                                        
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(rec_recivableAmt));//By Nidhin
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                    }
                                }
                                if (penalAmt > 0) {                                    
                                    txMap = new HashMap();
                                    //transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                                    txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                   
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD")); // credited to  interest Provisiniong Account head......
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    returnCalcMap.put("PENAL_AMT", new Double(penalAmt));
                                }                                                              
                                if (tdsAmt > 0) {                                   
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.PARTICULARS, "Tds Deduction" + depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put(TransferTrans.CR_AC_HD, (String) closureMap.get("TDS_ACHD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put("TRANS_MOD_TYPE", "GL");
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                                    
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tdsAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmt);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    transferList.add(transferTo);
                                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                    transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                    returnCalcMap.put("TDS_AMT", new Double(tdsAmt));
                                }                               
                                if (closureMap.containsKey("FULLY_CLOSING_STATUS")
                                        && CommonUtil.convertObjToStr(closureMap.get("FULLY_CLOSING_STATUS")).equals("FULLY_CLOSING")) {
                                    if (closedStatus.equals("NEW")) {
                                        System.out.println("depAmt :: closedStatus :: " + depAmt );
                                        if (lien != null && lien.length() > 0) {
                                            depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();                                            
                                        } else {
                                            depAmt = totalAmt - penalAmt - AgentCommAmt;                                            
                                           // System.out.println("depAmt   after :: " + depAmt);
                                        }
                                    } else if (closedStatus.equals("MATURED")) {
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                        depAmt = depAmt;                                        
                                    }
                                } else {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();                                   
                                }                               
                                if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {
                                    double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    depAmt = depAmt - ser_tax_Amt;
                                }                               
                                txMap = new HashMap();                               
                                if (closedStatus.equals("NEW")) {
                                    if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {                                       
                                        if (head.equals(direct)) {                                            
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);                                         
                                        } else {                                            
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else if (closedStatus.equals("MATURED")) {                                    
                                    txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);                                
                                double totalChargeForRD = 0.0;
                                if (transactionTO.getTransType().equals("TRANSFER")) {
                                     boolean depositLienExist = false;
                                    double tr_Dr_amt1 = 0;
                                    if (chargeLst != null && chargeLst.size() > 0) {
                                        for (int i = 0; i < chargeLst.size(); i++) {
                                            double depositClosingCharge = 0.0;
                                            HashMap chargeMap = new HashMap();
                                            String accHead = "";
                                            double chargeAmt = 0;
                                            String chargeType = "";
                                            chargeMap = (HashMap) chargeLst.get(i);
                                            if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD")));
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                txMap.put("generateSingleTransId", generateSingleCashId);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                tr_Dr_amt1 = tr_Dr_amt1 + CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                            }
                                        }
                                        if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                            double swachhCess = 0.0;
                                            double krishikalyanCess = 0.0;
                                            double serTaxAmt = 0.0;
                                            double totalServiceTaxAmt = 0.0;
                                            double normalServiceTax = 0.0;
                                            if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                            }
                                            if (swachhCess > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                            }
                                            if (krishikalyanCess > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                            }
                                            if (normalServiceTax > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                transferList.add(transferTo);
                                            }
                                            tr_Dr_amt1 = tr_Dr_amt1 + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                            transferTo.setSingleTransId(generateSingleTransId);
                                        }
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        totalChargeForRD = tr_Dr_amt1;
                                        if (lien != null && lien.length() > 0) {
                                            depositLienExist = true;                                           
                                        }    
                                    }

                                    if (transactionTO.getProductType().equals("TD")) {
                                        HashMap newDepMap = new HashMap();
                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                        if (lst != null && lst.size() > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                        }
                                    } else if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    } else if (transactionTO.getProductType().equals("AD")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by chithra on 14-10-14
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());
                                    } else if (transactionTO.getProductType().equals("TL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                    } else if (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                    } else if (transactionTO.getProductType().equals("RM")) {
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                                        LinkedHashMap transferMap = new LinkedHashMap();
                                        HashMap remittanceMap = new HashMap();
                                        remittanceIssueDAO = new RemittanceIssueDAO();
                                        remittanceIssueTO = new RemittanceIssueTO();
                                        String favouringName = transactionTODebit.getDebitAcctNo();
                                        transactionTODebit.setApplName(favouringName);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                        transactionTODebit.setProductType("TD");
                                        transactionTODebit.setDebitAcctNo(depositSubNo);
                                        remittanceIssueDAO.setFromotherDAo(false);
                                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                        remittanceMap.put("TransactionTO", transferMap);
                                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                        remittanceMap.put("OPERATION_MODE", "ISSUE");
                                        remittanceMap.put("AUTHORIZEMAP", null);
                                        remittanceMap.put("USER_ID", map.get("USER_ID"));
                                        remittanceMap.put("MODULE", "Remittance");
                                        remittanceMap.put("MODE", "INSERT");
                                        remittanceMap.put("SCREEN", "Issue");
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remittanceIssueTO.setAmount(new Double(depAmt));
                                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                        remittanceIssueTO.setDraweeBranchCode(branchID);
                                        remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        remittanceIssueTO.setCity("560");
                                        remittanceIssueTO.setFavouring(favouringName);
                                        remittanceIssueTO.setBranchId(branchID);
                                        remittanceIssueTO.setRemarks(depositSubNo);
                                        remittanceIssueTO.setCommand("INSERT");
                                        remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                        remittanceIssueTO.setExchange(new Double(0.0));
                                        remittanceIssueTO.setPostage(new Double(0.0));
                                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                                        remittanceIssueTO.setStatusDt(curDate);
                                        remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                        remittanceIssueTO.setInstrumentNo1(instNo1);
                                        remittanceIssueTO.setInstrumentNo2(instNo2);
                                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        LinkedHashMap remitMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        remMap.put(String.valueOf(1), remittanceIssueTO);
                                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                        remittanceMap.put("RemittanceIssueTO", remitMap);
                                        System.out.println(" remittanceMap :" + remittanceMap);
                                        remittanceIssueDAO.execute(remittanceMap);
                                    }
                                    if (!transactionTO.getProductType().equals("RM")) {
                                        double depAmtwitint = depAmt;                                        
                                        depAmt -= debitInt;                                        
                                        depAmt += drawnAmt;
                                        if (rec_recivableAmt > 0) {
                                            depAmt = depAmt + rec_recivableAmt;
                                        }
                                        if (lien != null && lien.length() > 0) {
                                            depAmt = depAmt;
                                        }else{
                                            depAmt += totalChargeForRD;
                                        }                                       
                                        depAmtwitint -= penalAmt;                                       
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        double amt = 0;
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put("LINK_BATCH_ID", depositSubNo);
                                        double ser_tax_Amt = 0.0;
                                        if (lien != null && lien.length() > 0) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                            if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {
                                                ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                depAmtwitint = depAmtwitint + ser_tax_Amt;
                                            }
                                        }                                       
                                        if(depositLienExist && depAmt > 0){
                                           double finalAmt = depAmt + totalChargeForRD + ser_tax_Amt;                                           
                                           transferTo = transactionDAO.addTransferDebitLocal(txMap, finalAmt); 
                                        }else{
                                           transferTo = transactionDAO.addTransferDebitLocal(txMap, depAmt);
                                        }
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        if (transactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (transactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (transactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (transactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");

                                        } else if (transactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        if (closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            if (transactionTO.getProductType().equals("TL")) {
                                                transactionDAO.setLinkBatchID(null);
                                                txMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                            }
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, amt);
                                        } else {
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                             if (transactionTO.getProductType().equals("AD")) { //Added by nithya on 23-03-2020  for KD-1666                               
                                                 transactionDAO.setLinkBatchID(null);
                                                 txMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                                            }
                                            transferTo = transactionDAO.addTransferCreditLocal(txMap, depAmtwitint);
                                        }
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        if (transactionTO.getProductType().equals("TL") || transactionTO.getProductType().equals("AD") ) { // AD added by  ithya on 23-03-2020 for KD-1666
                                            HashMap transLoanMap = interestCalculationTLAD(transactionTODebit.getDebitAcctNo(), transactionTODebit.getProductId(),transactionTO.getProductType());
                                            double service_taxAmt = 0;
                                            HashMap serviceTaxMap = new HashMap();
                                            if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                                                serviceTaxDetailsForLoan = (HashMap) map.get("serviceTaxDetails");
                                                if (serviceTaxDetailsForLoan.containsKey("TOT_TAX_AMT") && serviceTaxDetailsForLoan.get("TOT_TAX_AMT") != null) {
                                                    transLoanMap.put("TOT_SER_TAX_AMT", serviceTaxDetailsForLoan.get("TOT_TAX_AMT"));
                                                    transLoanMap.put("SER_TAX_HEAD", serviceTaxDetailsForLoan.get("TAX_HEAD_ID"));
                                                    transLoanMap.put("SER_TAX_MAP", serviceTaxDetailsForLoan);
                                                }
                                            }
                                            transactionDAO.setLoanAmtMap(transLoanMap);
                                            transactionDAO.setLoanDebitInt("DP");
                                        }
                                        if (!closureMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                            if (transactionTO.getProductType().equals("AD")) { //Added by nithya on 23-03-2020 for KD-1666
                                                transferList.add("DEPOSIT_TO_LOAN");
                                            }
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        transactionDAO.doTransfer();
                                    }
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                } else if (transactionTO.getTransType().equals("CASH")) {
                                    transactionDAO.execute(map);
                                    depAmt = clrAmt;
                                    txMap = new HashMap();
                                    transferList = new ArrayList();
                                    double tr_Dr_amt = 0;
                                    double totalDebitAmoutFromDeposit = 0.0;
                                    if (chargeLst != null && chargeLst.size() > 0) {
                                        for (int i = 0; i < chargeLst.size(); i++) {
                                            double depositClosingCharge = 0.0;
                                            HashMap chargeMap = new HashMap();
                                            String accHead = "";
                                            double chargeAmt = 0;
                                            String chargeType = "";
                                            chargeMap = (HashMap) chargeLst.get(i);
                                            if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charge " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", "TD");
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                if (isTransferCharges.equalsIgnoreCase("N")) {
                                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                                    transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transactionDAO.deleteTxList();
                                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                    transTO.setTransType(CommonConstants.TX_CASH);
                                                    transTO.setBatchId(depositSubNo);
                                                    transTO.setTransAmt(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")));
                                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    cashList = new ArrayList();
                                                    cashList.add(transTO);
                                                    transactionDAO.setScreenName("Deposit Account Closing");
                                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                                    transactionDAO.addCashList(cashList);
                                                    transactionDAO.doTransfer();
                                                } else {
                                                    transferList.add(transferTo);
                                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                                    txMap.put("generateSingleTransId", generateSingleCashId);//                                              
                                                    txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                                    txMap.put("TRANS_MOD_TYPE", "TD");//                                             
                                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                    transferTo.setSingleTransId(generateSingleTransId);
                                                }
                                                tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                                            }
                                        }
                                        if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
                                            double swachhCess = 0.0;
                                            double krishikalyanCess = 0.0;
                                            double serTaxAmt = 0.0;
                                            double totalServiceTaxAmt = 0.0;
                                            double normalServiceTax = 0.0;
                                            if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                                                serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                                totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                                swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                                krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                                            }
                                            if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                                normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                                            }
                                            if (swachhCess > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                if (isTransferCharges.equalsIgnoreCase("N")) {
                                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                                    transactionDAO.addTransferCredit(txMap, swachhCess);
                                                    transactionDAO.deleteTxList();
                                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                    transTO.setTransType(CommonConstants.TX_CASH);
                                                    transTO.setBatchId(depositSubNo);
                                                    transTO.setTransAmt(swachhCess);
                                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    cashList = new ArrayList();
                                                    cashList.add(transTO);
                                                    transactionDAO.setScreenName("Deposit Account Closing");
                                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                                    transactionDAO.addCashList(cashList);
                                                    transactionDAO.doTransfer();
                                                } else {
                                                    transferList.add(transferTo);
                                                }
                                            }
                                            if (krishikalyanCess > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID"))); // credited to Service Charge Account Head......
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                if (isTransferCharges.equalsIgnoreCase("N")) {
                                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                                    transactionDAO.addTransferCredit(txMap, krishikalyanCess);
                                                    transactionDAO.deleteTxList();
                                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                    transTO.setTransType(CommonConstants.TX_CASH);
                                                    transTO.setBatchId(depositSubNo);
                                                    transTO.setTransAmt(swachhCess);
                                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    cashList = new ArrayList();
                                                    cashList.add(transTO);
                                                    transactionDAO.setScreenName("Deposit Account Closing");
                                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                                    transactionDAO.addCashList(cashList);
                                                    transactionDAO.doTransfer();
                                                } else {
                                                    transferList.add(transferTo);
                                                }
                                            }
                                            if (normalServiceTax > 0) {
                                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID")));
                                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.PARTICULARS, "Deposits Charges Service Tax " + transactionTO.getDebitAcctNo());
                                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                                transferTo.setSingleTransId(generateSingleTransId);
                                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                if (isTransferCharges.equalsIgnoreCase("N")) {
                                                    transactionDAO.setTransType(CommonConstants.DEBIT);
                                                    transactionDAO.addTransferCredit(txMap, normalServiceTax);
                                                    transactionDAO.deleteTxList();
                                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                                    transTO.setTransType(CommonConstants.TX_CASH);
                                                    transTO.setBatchId(depositSubNo);
                                                    transTO.setTransAmt(swachhCess);
                                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                                    cashList = new ArrayList();
                                                    cashList.add(transTO);
                                                    transactionDAO.setScreenName("Deposit Account Closing");
                                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                                    transactionDAO.addCashList(cashList);
                                                    transactionDAO.doTransfer();
                                                } else {
                                                    transferList.add(transferTo);
                                                }
                                            }
                                            tr_Dr_amt = tr_Dr_amt + CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                            transferTo.setSingleTransId(generateSingleTransId);                                            
                                        }  
                                        rdTotalChargeAmt += tr_Dr_amt;
                                    }
                                    if (penalAmt > 0 && isTransferCharges.equalsIgnoreCase("Y")) {
                                        rdTotalChargeAmt += penalAmt;
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD"));
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        if (listData != null && listData.size() > 0) {
                                            HashMap map1 = (HashMap) listData.get(0);
                                            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "");
                                            } else {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            }
                                        } else {
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        }
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                    }
                                    if (intPay > 0 && (lien == null || lien.length() == 0) && isTransferCharges.equalsIgnoreCase("Y")) {
                                        intDebitHead = true;
                                        rdClosingChargeExists = true;
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, null);
                                       
                                        System.out.println("inside cash intPay :: " + intPay + "rdTotalChargeAmt :: " + rdTotalChargeAmt + "");
                                        if (intPay >= rdTotalChargeAmt) {
                                            tr_Dr_amt = rdTotalChargeAmt;
                                            intAmtTobeDebited = intPay - rdTotalChargeAmt;
                                            isDebitFormDeposit = false;
                                        } else {
                                            tr_Dr_amt = intPay;
                                            intAmtTobeDebited = 0;
                                            amtFromDeposit = rdTotalChargeAmt - tr_Dr_amt;
                                            isDebitFormDeposit = true;
                                        }
                                        if (amtFromDeposit > 0) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put("TRANS_MOD_TYPE", "TD");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, amtFromDeposit);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                        }
                                    }
                                    if (intPay > 0 && (lien == null || lien.length() == 0) && isTransferCharges.equalsIgnoreCase("Y")) {
                                        totalDebitAmoutFromDeposit = tr_Dr_amt;
                                    } else {
                                        totalDebitAmoutFromDeposit = tr_Dr_amt + penalAmt;
                                    }
                                    if (totalDebitAmoutFromDeposit > 0) {
                                        if (isTransferCharges.equalsIgnoreCase("N")) {
                                            txMap = new HashMap();
                                        } else {
                                            if (intDebitHead) {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                                txMap.put(TransferTrans.DR_ACT_NUM, null);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put(TransferTrans.DR_PROD_ID, null);
                                            } else {
                                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                            }                                         
                                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                            txMap.put("generateSingleTransId", generateSingleCashId);
                                            txMap.put(TransferTrans.PARTICULARS, "Deposits Charge ");
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);                                           
                                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDebitAmoutFromDeposit);
                                            transferTo.setSingleTransId(generateSingleTransId);
                                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                            transferList.add(transferTo);
                                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        }
                                    }
                                    boolean depositLienExist = false;
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                    txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put("generateSingleTransId", generateSingleCashId);
                                    if (lien != null && lien.length() > 0) {
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() + tr_Dr_amt;
                                        if (blncLonamt <= 0) {
                                            depAmt = depAmt - intPay - add_int_amt;
                                        } else {
                                            depositLienExist = true;
                                            depAmt = 0;
                                        }
                                    }
                                    if (!depositLienExist) {
                                        if (isTransferCharges.equalsIgnoreCase("N")) {
                                            transactionTO.setTransAmt(depAmt);
                                            transactionDAO.setTransType(CommonConstants.CREDIT);
                                            transactionDAO.addTransferDebit(txMap, depAmt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setBatchId(depositSubNo);
                                            transTO.setTransAmt(new Double(depAmt));
                                        } else {
                                            if (isDebitFormDeposit && intPay > 0 && (lien == null || lien.length() == 0)) {
                                                tr_Dr_amt = amtFromDeposit;
                                            }
                                            if (!isDebitFormDeposit && intPay > 0 && (lien == null || lien.length() == 0)) {
                                                tr_Dr_amt = 0;
                                            }
                                            if(intPay <= 0 && penalAmt > 0 && (lien == null || lien.length() == 0)){
                                                tr_Dr_amt = rdTotalChargeAmt;
                                            }
                                            if (lien != null && lien.length() > 0) {
                                                tr_Dr_amt = tr_Dr_amt +  penalAmt;
                                            }
                                            transactionTO.setTransAmt(new Double(depAmt - tr_Dr_amt));
                                            transactionDAO.addTransferDebit(txMap, depAmt - tr_Dr_amt);
                                            transactionDAO.deleteTxList();
                                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            transTO.setTransType("CASH");
                                            transTO.setBatchId(depositSubNo);
                                            transTO.setTransAmt(new Double(depAmt - tr_Dr_amt));
                                        }

                                    }

                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    cashList = new ArrayList();
                                    cashList.add(transTO);
                                    transactionDAO.setScreenName("Deposit Account Closing");
                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                    if (!depositLienExist) {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }
                                    if (rec_recivableAmt > 0) {
                                        transactionDAO.execute(map);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        //txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));//KD-3662 : RD premature closer charge amount collection head issue
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        if (listData != null && listData.size() > 0) {
                                            HashMap map1 = (HashMap) listData.get(0);
                                            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "");
                                            } else {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            }
                                        } else {
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        }
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        transactionTO.setTransAmt(new Double(rec_recivableAmt));
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, rec_recivableAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(rec_recivableAmt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        if (rec_recivableAmt > 0) {
                                            transactionDAO.setScreenName("Deposit Account Closing");
                                            transactionDAO.setInitiatedBranch(initiatedBranch);
                                            transactionDAO.addCashList(cashList);
                                            transactionDAO.doTransfer();
                                        }
                                    }

                                    if (debitInt > 0) {
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        if (lien != null && lien.length() > 0 && blncLonamt > 0) {
                                            debitInt = intPay;
                                        }else if(rdClosingChargeExists && isTransferCharges.equalsIgnoreCase("Y")){
                                            debitInt = intAmtTobeDebited;
                                        }
                                        if(drawnAmt >0){
                                           debitInt -= drawnAmt; 
                                        }
                                        if(debitInt > 0){
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure-Interest");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        transactionDAO.addTransferDebit(txMap, debitInt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(debitInt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName("Deposit Account Closing");
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                        }
                                    }

                                    // Penal Transaction - Delayed Ampount
                                    if (penalAmt > 0 && isTransferCharges.equalsIgnoreCase("N")) {
                                        transactionDAO.execute(map);
                                        txMap = new HashMap();
                                        transferList = new ArrayList();
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD"));
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        if (listData != null && listData.size() > 0) {
                                            HashMap map1 = (HashMap) listData.get(0);
                                            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "");
                                            } else {
                                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                            }
                                        } else {
                                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        }
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put("TRANS_MOD_TYPE", "TD");
                                        txMap.put("generateSingleTransId", generateSingleCashId);
                                        transactionTO.setTransAmt(new Double(rec_recivableAmt));
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        transactionDAO.addTransferCredit(txMap, penalAmt);
                                        transactionDAO.deleteTxList();
                                        transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        transTO.setTransType("CASH");
                                        transTO.setBatchId(depositSubNo);
                                        transTO.setTransAmt(new Double(penalAmt));
                                        transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        cashList = new ArrayList();
                                        cashList.add(transTO);
                                        transactionDAO.setScreenName("Deposit Account Closing");
                                        transactionDAO.setInitiatedBranch(initiatedBranch);
                                        transactionDAO.addCashList(cashList);
                                        transactionDAO.doTransfer();
                                    }
                                    // End - Penal Transaction

                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                }
                            }
                        }
                    }
                    System.out.println("RECURRING PAYABLE COMPLETED...");
                } else { // Receivable Amount for the customer....
                    System.out.println("RECURRING Receivable STARTED...");
                    double paycreditInt = 0.0;
                    double receiveInt = drawnAmt - debitInt;
                    double zeroInt = CommonUtil.convertObjToDouble(closureMap.get("CR_INTEREST")).doubleValue();
                    if (receiveInt > 0) {
                        receiveInt = receiveInt;
                    } else {
                        intPay = receiveInt * -1;
                    }
                    if (receiveInt > 0) {
                        if (intPay > 0) {
                            txMap = new HashMap();
                            transferList = new ArrayList();                            
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));// debited to interest payable account head......
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_PROD_ID, (String) closureMap.get("PROD_ID"));
                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                          
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY"));// crediting to Operative account head......
                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                           
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(receiveInt));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(receiveInt));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranch);                           
                        }
                        paycreditInt = receiveInt + zeroInt;
                        if (paycreditInt > 0) {                            
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                          
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                           
                            if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                            }
                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                             txMap.put("TRANS_MOD_TYPE","GL");
                             txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, paycreditInt);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                            txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, paycreditInt);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        }
                        returnCalcMap.put("INT_PAY_AMT", new Double(paycreditInt));
                        returnCalcMap.put("INT_AMT", new Double(receiveInt));
                    } else {                        
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));//debiting to int paid a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                         txMap.put("TRANS_MOD_TYPE","GL");
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                       
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PAY")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);

                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));//debiting int payable a/c head
                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                        
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                       
                        if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));//crediting to int paid a/c head
                        }
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE","GL");                     
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Interest Amount " + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, intPay);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("INT_PAY_AMT", new Double(intPay));
                        returnCalcMap.put("INT_AMT", new Double(intPay));
                    }
                    if (closureMap.get("BEHAVES_LIKE").equals("RECURRING") && penalAmt > 0) {                       
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); // Debited to interest payable account head......
                        txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                      
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DELAYED_ACHD")); // credited to  interest Provisiniong Account head......
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE","GL");
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, penalAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("PENAL_AMT", new Double(penalAmt));
                    }
                    if (closureMap.get("BEHAVES_LIKE").equals("DAILY") && AgentCommAmt > 0) {                        
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD")); 
                        txMap.put(TransferTrans.PARTICULARS, "Penalty - Delayed Amount");
                        txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                       
                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("COMMISION_HEAD")); 
                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("TRANS_MOD_TYPE","GL");    
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, AgentCommAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest" + depositSubNo);
                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, AgentCommAmt);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                        returnCalcMap.put("PENAL_AMT", new Double(AgentCommAmt));
                    }
                    if (map.containsKey("TransactionTO")) {
                        transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO;
                        if (transactionDetailsMap.size() > 0) {
                            if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null) {
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                }
                                if (closedStatus.equals("NEW")) {
                                    if (lien != null && lien.length() > 0) {
                                        depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();                                        
                                    } else {
                                        depAmt = totalAmt - penalAmt - AgentCommAmt;                                       
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                    depAmt = depAmt;                                    
                                }
                                if (serviceTaxDetails != null && serviceTaxDetails.containsKey("TOT_TAX_AMT")) {
                                    double ser_tax_Amt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                                    depAmt = depAmt - ser_tax_Amt;
                                }
                                txMap = new HashMap();
                                transferList = new ArrayList(); 
                                if (closedStatus.equals("NEW")) {
                                    if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {                                       
                                        if (head.equals(direct)) {                                            
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        } else {                                           
                                            txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        }
                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else if (closedStatus.equals("MATURED")) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                    txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                if (transactionTO.getTransType().equals("TRANSFER")) {                                    
                                    if (transactionTO.getProductType().equals("TD")) {
                                        HashMap newDepMap = new HashMap();
                                        newDepMap.put("PROD_ID", transactionTODebit.getProductId());
                                        newDepMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", newDepMap);
                                        if (lst != null && lst.size() > 0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) newDepMap.get("FIXED_DEPOSIT_ACHD"));
                                            txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                            txMap.put(TransferTrans.PARTICULARS, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                            txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        }
                                    } else if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                                       
                                    } else if (transactionTO.getProductType().equals("AD")) {                                      
                                        txMap.put(TransferTrans.CR_AC_HD, (String) loanMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                                       
                                    } else if (!transactionTO.getProductId().equals("") && transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {//added by chithra on 14-10-14
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        if (othBankMap != null && othBankMap.containsKey("PRINCIPAL_AC_HD")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToDouble(othBankMap.get("PRINCIPAL_AC_HD")));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());
                                        txMap.put(TransferTrans.CR_BRANCH, transactionTODebit.getBranchId());                                       
                                    }else if (transactionTO.getProductType().equals("TL")) {                                       
                                    } else if (transactionTO.getProductType().equals("OA") || transactionTO.getProductType().equals("SA")) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROD_ID"));
                                        txMap.put(TransferTrans.CR_PROD_ID, transactionTODebit.getProductId());                                       
                                        txMap.put(TransferTrans.CR_PROD_TYPE, transactionTO.getProductType());
                                        txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                        txMap.put(TransferTrans.CR_ACT_NUM, transactionTODebit.getDebitAcctNo());                                       
                                    } else if (transactionTO.getProductType().equals("RM")) {
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        LinkedHashMap notDeleteMap = new LinkedHashMap();
                                        LinkedHashMap transferMap = new LinkedHashMap();
                                        HashMap remittanceMap = new HashMap();
                                        remittanceIssueDAO = new RemittanceIssueDAO();
                                        remittanceIssueTO = new RemittanceIssueTO();
                                        String favouringName = transactionTODebit.getDebitAcctNo();
                                        transactionTODebit.setApplName(favouringName);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        transactionTODebit.setProductId(CommonUtil.convertObjToStr(closureMap.get("PROD_ID")));
                                        transactionTODebit.setProductType("TD");
                                        transactionTODebit.setDebitAcctNo(depositSubNo);
                                        remittanceIssueDAO.setFromotherDAo(false);
                                        notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                        transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                        remittanceMap.put("TransactionTO", transferMap);
                                        remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                        remittanceMap.put("OPERATION_MODE", "ISSUE");
                                        remittanceMap.put("AUTHORIZEMAP", null);
                                        remittanceMap.put("USER_ID", map.get("USER_ID"));
                                        remittanceMap.put("MODULE", "Remittance");
                                        remittanceMap.put("MODE", "INSERT");
                                        remittanceMap.put("SCREEN", "Issue");
                                        HashMap behavesMap = new HashMap();
                                        behavesMap.put("BEHAVES_LIKE", "PO");
                                        List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                        if (lstRemit != null && lstRemit.size() > 0) {
                                            behavesMap = (HashMap) lstRemit.get(0);
                                        }
                                        HashMap draweeMap = new HashMap();
                                        if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                            lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                            if (lstRemit != null && lstRemit.size() > 0) {
                                                draweeMap = (HashMap) lstRemit.get(0);
                                            }
                                        }
                                        remittanceIssueTO.setAmount(new Double(depAmt));
                                        remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                        remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                        remittanceIssueTO.setFavouring(favouringName);
                                        remittanceIssueTO.setBranchId(branchID);                        
                                        remittanceIssueTO.setDraweeBranchCode(branchID);
                                        remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                        remittanceIssueTO.setRemarks(depositSubNo);
                                        remittanceIssueTO.setCity("560");
                                        remittanceIssueTO.setCommand("INSERT");
                                        remittanceIssueTO.setTotalAmt(new Double(depAmt));
                                        remittanceIssueTO.setExchange(new Double(0.0));
                                        remittanceIssueTO.setPostage(new Double(0.0));
                                        remittanceIssueTO.setOtherCharges(new Double(0.0));
                                        remittanceIssueTO.setStatusDt(curDate);
                                        remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                        remittanceIssueTO.setInstrumentNo1(instNo1);
                                        remittanceIssueTO.setInstrumentNo2(instNo2);
                                        remittanceIssueTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                                        LinkedHashMap remitMap = new LinkedHashMap();
                                        LinkedHashMap remMap = new LinkedHashMap();
                                        remMap.put(String.valueOf(1), remittanceIssueTO);
                                        remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                        remittanceMap.put("RemittanceIssueTO", remitMap);                                       
                                        remittanceIssueDAO.execute(remittanceMap);
                                    }
                                    if (!transactionTO.getProductType().equals("RM")) {
                                        System.out.println("TRANSFER txMap : " + txMap);
                                        txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                        
                                        txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(depAmt));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                        txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                        transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(depAmt));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                        transferList.add(transferTo);
                                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                        transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                        transactionTODebit.setTransAmt(new Double(depAmt));
                                        map.put("TransactionTO", transactionDetailsMap);
                                        transactionDAO.execute(map);
                                        transactionDAO.doTransfer();
                                    }
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                } else if (transactionTO.getTransType().equals("CASH")) {                                  
                                    transactionDAO.execute(map);
                                    txMap = new HashMap();
                                    transferList = new ArrayList(); 
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                    txMap.put(TransferTrans.CURRENCY, "INR");
                                    txMap.put(TransferTrans.PARTICULARS, "Self A/c Closure");
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                                    
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");
                                    txMap.put("generateSingleTransId", generateSingleCashId);
                                    transactionDAO.addTransferDebit(txMap, depAmt);
                                    transactionDAO.deleteTxList();
                                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                    transTO.setTransType("CASH");
                                    transTO.setBatchId(depositSubNo);
                                    transTO.setTransAmt(new Double(depAmt));
                                    transTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    cashList.add(transTO);
                                    transactionDAO.setScreenName("Deposit Account Closing");
                                    transactionDAO.setInitiatedBranch(initiatedBranch);
                                    transactionDAO.addCashList(cashList);
                                    transactionDAO.doTransfer();
                                    returnCalcMap.put("PAY_AMT", new Double(depAmt));
                                }
                            }
                        }
                    }
                    System.out.println("RECURRING Receivable COMPLETED...");
                }
            } else if (closureMap.containsKey("TRANSFER_OUT_MODE") && closureMap.get("TRANSFER_OUT_MODE").equals("Y")) {               
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO != null) {
                                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            }
                            HashMap transferOutMap = new HashMap();
                            HashMap transferBranchMap = new HashMap();
                            transferBranchMap.put("TRANSFER_OUT_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            lst = sqlMap.executeQueryForList("getSelectBranchList", transferBranchMap);
                            if (lst != null && lst.size() > 0) {
                                transferBranchMap = (HashMap) lst.get(0);
                            }
                            HashMap transOutMap = new HashMap();
                            transOutMap.put("DEPOSIT_NO", depositNo);
                            lst = sqlMap.executeQueryForList("getIntCrIntDrawn", transOutMap);
                            if (lst != null && lst.size() > 0) {
                                transOutMap = (HashMap) lst.get(0);
                                double totalIntCredit = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                double totalIntDrawn = CommonUtil.convertObjToDouble(transOutMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                interestAmt = totalIntCredit - totalIntDrawn;                               
                            }
                            if (interestAmt > 0) {                               
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("INT_PAY"));
                                txMap.put(TransferTrans.PARTICULARS, depositSubNo);
                                txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                            
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");//                              
                                if (mode.equals(CommonConstants.PREMATURE_CLOSURE) && map.containsKey("PAYORRECEIVABLE") && map.get("PAYORRECEIVABLE") != null && map.get("PAYORRECEIVABLE").equals("Receivable")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_RECOVERY_HEAD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("INT_PROV_ACHD"));
                                }
                                txMap.put(TransferTrans.CR_BRANCH, creditBranchID);
                                txMap.put(TransferTrans.CURRENCY, "INR");
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);                                
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Excess Amount Recovered " + depositSubNo);
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                returnCalcMap.put("INT_AMT", new Double(interestAmt));
                                interestAmt = 0.0;
                            }
                            if (closedStatus.equals("NEW")) {
                                if (lien != null && lien.length() > 0) {
                                    depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();                                    
                                } else {
                                    depAmt = totalAmt - penalAmt;                                    
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                depAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                                depAmt = depAmt;                                
                            }
                            txMap = new HashMap();
                            transferList = new ArrayList(); 
                            if (closedStatus.equals("NEW")) {
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {                                    
                                    if (head.equals(direct)) {                                        
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    } else {                                        
                                        txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                } else {
                                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                                    txMap.put(TransferTrans.PARTICULARS, "T/O " + transferBranchMap.get("Branch Name"));
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("FIXED_DEPOSIT_ACHD"));
                                }
                            } else if (closedStatus.equals("MATURED")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) headMap.get("INSTALL_TYPE"));
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + " " + transferBranchMap.get("Branch Name"));
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, debitBranchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_ID, closureMap.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            if (transactionTO.getTransType().equals("TRANSFER")) {                                
                                if (transactionTO.getProductId().equals("") && transactionTO.getProductType().equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, transactionTODebit.getDebitAcctNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, creditBranchID);                                   
                                }
                            }
                            if (!transactionTO.getProductType().equals("RM")) {
                                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");                              
                                txMap.put(TransferTrans.DR_INSTRUMENT_2, "Deposit Closure");                                
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "T/O " + depositSubNo + " " + transferBranchMap.get("Branch Name"));
                                txMap.put(TransferTrans.INITIATED_BRANCH, initiatedBranch);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, Math.abs(depAmt));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranch);
                                transactionTODebit.setTransAmt(new Double(depAmt));
                                map.put("TransactionTO", transactionDetailsMap);
                                transactionDAO.execute(map);
                                transactionDAO.doTransfer();
                            }
                            returnCalcMap.put("PAY_AMT", new Double(depAmt));
                            HashMap transferMap = new HashMap();
                            transferMap.put("DEPOSIT_NO", depositSubNo);
                            List lstTrans = sqlMap.executeQueryForList("getTransOutDepositDetails", transferMap);
                            if (lstTrans != null && lstTrans.size() > 0) {
                                transferMap = (HashMap) lstTrans.get(0);
                            }
                            transferOutMap.put("TRANS_ID", transferMap.get("BATCH_ID"));
                            transferOutMap.put("DEPOSIT_NO", depositNo);
                            transferOutMap.put("TRANS_DT", curDate);
                            transferOutMap.put("TRANS_AMT", new Double(depAmt));
                            transferOutMap.put("STATUS", closingStatus);
                            transferOutMap.put("STATUS_BY", map.get("USER_ID"));
                            transferOutMap.put("STATUS_DT", curDate);
                            transferOutMap.put("CREATED_BY", map.get("USER_ID"));
                            transferOutMap.put("CREATED_DT", curDate);
                            transferOutMap.put("TRANS_BRANCH_CODE", closureMap.get("TRANSFER_OUT_BRANCH_CODE"));
                            transferOutMap.put("INTER_TRANS_NO", getInter_TransID());
                            transferOutMap.put("CURRENT_BRANCH", branchID);
                            transferOutMap.put("TRANS_OUT_FLAG", "N");
                            returnMap.put("INTER_TRANS_NO", transferOutMap.get("INTER_TRANS_NO"));
                            System.out.println("transferOutMap :" + transferOutMap);
                            System.out.println("returnMap :" + returnMap);
                            sqlMap.executeUpdate("insertTransferOutDeposits", transferOutMap);
                            transferOutMap = null;
                            transferMap = null;
                            transferBranchMap = null;
                            transOutMap = null;
                        }
                    }
                    System.out.println("CUMMULATIVE Transfer Out Started...");
                }
            }
        }
        String authBy2 = CommonUtil.convertObjToStr(closureMap.get("AUTHORIZE_BY_2"));
        if (generateSingleTransId != null && generateSingleTransId.length() > 0&& authBy2!=null && authBy2.length()>0) {
            HashMap udtMap = new HashMap();
            udtMap.put("AUTHORIZE_BY_2", authBy2);
            udtMap.put("SINGLE_TRANS_ID", generateSingleTransId);
            udtMap.put("TRANS_DT", (Date) curDate.clone());
            sqlMap.executeUpdate("updateTranssferTransAuthorizedBy2", udtMap);
        }if(generateSingleCashId != null && generateSingleCashId.length() > 0&& authBy2!=null && authBy2.length()>0){
             HashMap udtMap = new HashMap();
            udtMap.put("AUTHORIZE_BY_2", authBy2);
            udtMap.put("SINGLE_TRANS_ID", generateSingleCashId);
            udtMap.put("TRANS_DT", (Date) curDate.clone());
            sqlMap.executeUpdate("updateCashTransAuthorizedBy2", udtMap);
        }
        if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {
            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) closureMap.get("serviceTax_DetailsTo");
            insertServiceTaxDetails(objserviceTaxDetailsTO);
        }
        if (serviceTaxDetailsForLoan != null && serviceTaxDetailsForLoan.size() > 0) {
            ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
            insertServiceTaxDetails(objserviceTaxDetailsTO);
        }
        return returnCalcMap;
    }   
    
     private HashMap calcuateTDS(HashMap obj, boolean enableTrans) throws Exception { // Added by nithya on 06-02-2020 for KD-1090
        System.out.println("####### doDepositClose tdsMap " + obj);
        HashMap tdsCalcMap = new HashMap();
        tdsCalcMap = obj;
        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(_branchCode);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String Prod_type = "TD";
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        intTrfAmt = CommonUtil.convertObjToDouble(tdsCalcMap.get("TDS_AMOUNT")).doubleValue();
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        closeMap.put("CLOSING_TYPE", tdsCalcMap.get("CLOSING_TYPE"));
        tdsMap.put("INT_DATE", curDate);        
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        HashMap whereMap = new HashMap();//TDS product Level Checking Calculate Yes/No
        whereMap.put("PROD_ID", prod_id);
        List prodTDSList = (List) sqlMap.executeQueryForList("icm.getDepositMaturityIntRate", whereMap);        //Product Level Checking
        if (prodTDSList != null && prodTDSList.size() > 0) {
            whereMap = (HashMap) prodTDSList.get(0);
            if (CommonUtil.convertObjToStr(whereMap.get("CALCULATE_TDS")).equals("Y")) {
                whereMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
                List depAccList = (List) sqlMap.executeQueryForList("getTDSAccountLevel", whereMap);            //Account Level Checking
                if (depAccList != null && depAccList.size() > 0) {
                    List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
                    if (exceptionList == null || exceptionList.size() <= 0) {
                        whereMap.put("CUST_ID", CustId);
                        List custList = (List) sqlMap.executeQueryForList("getCustData", whereMap);             //Checking Customer having PAN or not.
                        if (custList != null && custList.size() > 0) {
                            whereMap = (HashMap) custList.get(0);
                            if (CommonUtil.convertObjToStr(whereMap.get("PAN_NUMBER")).length() > 0) {
                                tdsCalculator.setPan(true);
                            } else {
                                tdsCalculator.setPan(false);
                            }
                        }
                        tdsCalculator.setIsTransaction(false);
                        tdsMap = new HashMap();
                        tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
                        System.out.println("####### Final Debenture Transfer tdsMap " + tdsMap);
                        if (tdsMap != null) {
                            interestBatchTO.setIsTdsApplied("Y");
                            interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
                        }
                    }
                }
            }
        }
        return tdsMap;
    }
  
}
