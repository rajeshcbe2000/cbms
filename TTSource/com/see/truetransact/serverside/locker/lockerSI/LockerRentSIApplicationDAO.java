/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LockerRentSIApplicationDAO.java
 *
 * Created on February 29, 2012, 5:54 PM
 */
package com.see.truetransact.serverside.locker.lockerSI;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.servicetax.ServiceTaxCalculation;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.locker.lockersurrender.LockerSurrenderDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.locker.lockersurrender.LockerSurrenderTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rajesh
 */
public class LockerRentSIApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;
    private TransactionTO to;
    private LockerSurrenderDAO lockerSurrenderDAO = new LockerSurrenderDAO();
    private LogDAO logDAO;
    private LogTO logTO;
    private String lockerIssId = "";
    private String generateSingleTransId="";
    //private String SingTranID;
    ArrayList finallist;
    private List lockerTransHeadList ;

    /**
     * Creates a new instance of LockerRentSIApplicationTO
     */
    public LockerRentSIApplicationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public java.util.HashMap execute(HashMap map) throws Exception {
        //returnMap = new HashMap();
        HashMap returnMap = new HashMap();
        List newerlist = new ArrayList();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in locker Rent SI Application DAO : " + map);
        //  sqlMap.startTransaction();
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        final String command = CommonUtil.convertObjToStr(map.get("MODE"));
        if(map.containsKey("LOCKER_RENT_DEPOSIT_LIST")&&map.get("LOCKER_RENT_DEPOSIT_LIST")!=null){
           newerlist= doLockerRentTrans(map,command);
           returnMap.put("error", newerlist);
        }
        else{
        ArrayList finallist = (ArrayList) map.get("Finallist");
        newerlist = doServiceTaxTrans(map, command);
        System.out.println("te error is:" + newerlist);
        // sqlMap.commitTransaction();
        returnMap.put("error", newerlist);
        }

        return returnMap;
    }

    public java.util.HashMap executeQuery(java.util.HashMap obj) throws Exception {
        HashMap returnMap = new HashMap();
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return returnMap;

    }

    private void insertData(HashMap obj, String command) throws Exception {
        setLockerIssId(getLockerSurrenderId());

    }

    public List doServiceTaxTrans(HashMap map, String command) throws Exception {

        HashMap batchmap = new HashMap();
        List interestList = new ArrayList();
        ArrayList batchList = new ArrayList();
        List interestRow = new ArrayList();
        HashMap errorMap = null;
        ArrayList finallist = (ArrayList) map.get("Finallist");
        String branch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        _branchCode=branch;
        // String id=CommonUtil.convertObjToStr(map.get("USER_ID"));
        String lock = CommonUtil.convertObjToStr(map.get("lockertype"));
        String mod = CommonUtil.convertObjToStr(map.get("MODE"));
        String name = CommonUtil.convertObjToStr(map.get("name"));
        int ryear = CommonUtil.convertObjToInt(map.get("ryear"));
        int rmonth = CommonUtil.convertObjToInt(map.get("rmonth"));
        String trans_mode = CommonUtil.convertObjToStr(map.get("TRANS_MODE"));
        System.out.println("$$$$$" + lock);
        HashMap headMap = new HashMap();
        headMap.put("value", lock);
        interestList = new ArrayList();
        lockerTransHeadList = sqlMap.executeQueryForList("getLockerAccountHeads", headMap);
        LockerSurrenderDAO lockerSurrenderDAO = new LockerSurrenderDAO();
        LockerSurrenderTO lockerSurrenderTO = new LockerSurrenderTO();
        ArrayList list = null;
        System.out.println("####list" + finallist.size());
        generateSingleTransId=this.generateLinkID();
        for (int j = 0; j < finallist.size(); j++) {
            try {
                list = (ArrayList) finallist.get(j);
                double charge = new Double(list.get(4).toString()).doubleValue();
                mod = CommonUtil.convertObjToStr(map.get("MODE"));
                double service = new Double(list.get(5).toString()).doubleValue();
                double balance = new Double(list.get(9).toString()).doubleValue();
                System.out.println("the list is" + interestList);
                interestList.add(list.get(1));
                System.out.println("the list is" + list);
                if (map.containsKey("DAY_END")) {
                    lock = CommonUtil.convertObjToStr(list.get(11).toString());
                }
                boolean str = new Boolean(list.get(0).toString()).booleanValue();
                if (str == true) {
                    HashMap h = new HashMap();
                    HashMap hashmap = new HashMap();
                    HashMap stmap = new HashMap();
                    hashmap.put("LOCKER_NUM", list.get(1));
                    hashmap.put("PROD_ID", lock);
                    HashMap Smap = (HashMap) sqlMap.executeQueryForObject("getDetails", hashmap);
                    System.out.println("######" + Smap);
                    String custid = CommonUtil.convertObjToStr(Smap.get("CUST_ID"));
                    // String issuid=CommonUtil.convertObjToStr(Smap.get("REMARKS"));
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getLockerAccountHeads", CommonUtil.convertObjToStr(map.get("lockertype")));
                    System.out.println("######" + acHeads);
                    ChargesServiceTaxTO chargesServiceTaxTO = new ChargesServiceTaxTO();
                    TransactionTO transactionTO = new TransactionTO();
                    lockerSurrenderTO = new LockerSurrenderTO();
                    lockerSurrenderDAO = new LockerSurrenderDAO();
                    transactionTO.setProductId(list.get(7).toString());
                    transactionTO.setProductType(list.get(8).toString());
                    transactionTO.setDebitAcctNo(list.get(10).toString());
                    transactionTO.setApplName(name);
                    if (trans_mode.equals("CASH")) {
                        transactionTO.setTransType("CASH");
                    }
                    if (trans_mode.equals("TRANSFER")) {
                        transactionTO.setTransType("TRANSFER");
                    }
                    transactionTO.setInstType("vocher");
                    transactionTO.setChequeDt(currDt);
                    lockerSurrenderTO.setLocNum(list.get(1).toString());
                    lockerSurrenderTO.setCustId(custid);
                    lockerSurrenderTO.setBranchID(branch);
                    lockerSurrenderTO.setSurRenew("RENEW");
                    lockerSurrenderTO.setProdId(lock);
                    lockerSurrenderTO.setAcctName(list.get(2).toString());
                    double amount = new Double(list.get(4).toString()).doubleValue();
                    double serv = new Double(list.get(5).toString()).doubleValue();
                    double fine = new Double(list.get(6).toString()).doubleValue();
                    lockerSurrenderTO.setCharges(new Double(amount));
                    lockerSurrenderTO.setServiceTax(new Double(serv));
                    lockerSurrenderTO.setPenalAmount(fine); // Added by nithya on 06-02-2016 for 0003604: locker standing instruction
                    transactionTO.setTransAmt(new Double(amount + serv + fine));
                    if (map.containsKey("SERVIC_TAX_REQ") && map.get("SERVIC_TAX_REQ") != null && CommonUtil.convertObjToStr(map.get("SERVIC_TAX_REQ")).equalsIgnoreCase("Y")) {
                        HashMap serviceTaxMap = calculateServiceTax(amount, serv);
                        map.put("serviceTaxDetails", serviceTaxMap);
                        map.put("serviceTaxDetailsTO", setServiceTaxDetails(serviceTaxMap, CommonUtil.convertObjToStr(list.get(0))));
                        transactionTO.setTransAmt(amount + serv + fine + CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT")));
                    }
                    lockerSurrenderTO.setCommand(mod);
                    lockerSurrenderTO.setSurDt(currDt);
                    System.out.println("LockerSurrenderTO" + lockerSurrenderTO);
                    transactionTO.setCommand("INSERT");
                    System.out.println("list#@$@:" + list);
                    HashMap hash = (HashMap) sqlMap.executeQueryForObject("getRemarksDetails", CommonUtil.convertObjToStr(list.get(1)));
                    System.out.println("######hash" + hash);
                    String sd = CommonUtil.convertObjToStr(hash.get("STATUS_DT"));
                    System.out.println("@@@@@@@@" + sd);
                    Date d = DateUtil.getDateMMDDYYYY(sd);
                    int dd = d.getDate();
                    System.out.println("@@@@@@@" + dd);
                    int m = d.getMonth() + 1;
                    System.out.println("@@@@@@@" + m);
                    int year = d.getYear() + 1900;
                    System.out.println("@@@@@@@" + year);
                    HashMap hhmap = new HashMap();
                    map.put("MODE", mod);
                    map.put("BRANCH_CODE", branch);
                    map.remove("Finallist");
                    map.remove("name");
                    map.remove("lockertype");
                    LinkedHashMap transMap = new LinkedHashMap();
                    transMap.put("1", transactionTO);
                    LinkedHashMap notDeleteMap = new LinkedHashMap();
                    notDeleteMap.put("NOT_DELETED_TRANS_TOs", transMap);
                    map.put("TransactionTO", notDeleteMap);
                    map.put("LockerSurrenderTO", lockerSurrenderTO);
                    map.put("generateSingleTransId",generateSingleTransId);
                    //System.out.println("Here generated Single ID"+generateSingleTransId);
                    System.out.println("LockerSurrenderTO" + lockerSurrenderTO);
                    System.out.println("#######before" + map);
                    batchmap = lockerSurrenderDAO.execute(map);
                    System.out.println("####### after lockerSurrenderDAO.execute:" + map);
                    String id = CommonUtil.convertObjToStr(map.get("USER_ID"));
                    mod = "Authorize";
                    map.remove("COMMAND");
                    HashMap singleAuthorizeMap = new HashMap();
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap shmap = new HashMap();
                    String listyear = list.get(3).toString();
                    Date listdate = DateUtil.getDateMMDDYYYY(listyear);
                    int listday = listdate.getDate();
                    int listmon = listdate.getMonth() + 1;
                    int listyy = listdate.getYear() + 1900;
                    System.out.println("the listyy is" + listyy);
                    System.out.println("the ryear is" + ryear);
                    int lstyear = 0;
                    if (listyy == ryear) {
                        if (listmon <= rmonth) {
                            lstyear = ryear + 1;
                        }
                    } else {
                        if (listmon < rmonth) {
                            lstyear = ryear + 1;
                            System.out.println("in if" + lstyear);
                        } else {
                            lstyear = ryear;
                            System.out.println("in else" + lstyear);
                        }
                    }
                    Date rentdate = DateUtil.getDate(listday, listmon, lstyear);
                    System.out.println("the rentdt is" + rentdate);
                    authDataMap.put("LOCKER_NUM", list.get(1).toString());
                    authDataMap.put("CUST_ID", custid);
                    authDataMap.put("PROD_ID", lock);
                    authDataMap.put("ISSUE_ID", map.get("LOCKER_SURRENDER_ID").toString());
                    authDataMap.put("SUR_OR_RENEW", "RENEW");
                    arrList.add(authDataMap);
                    System.out.println("#######" + authDataMap);
                    HashMap authmap = new HashMap();
                    authmap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                    authmap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    authmap.put(CommonConstants.USER_ID, id);
                    authmap.put("year", rentdate);
                    HashMap hmap = new HashMap();
                    hmap.put(CommonConstants.AUTHORIZEMAP, authmap);
                    HashMap fhashmap = new HashMap();
                    transactionTO.setCommand("Authorize");
                    transactionTO.setBatchId(map.get("LOCKER_SURRENDER_ID").toString());
                    String batch = transactionTO.getBatchId();
                    System.out.println("$$$$$$$" + batch);
                    hmap.put("TransactionTO", notDeleteMap);
                    hmap.put(CommonConstants.BRANCH_ID, branch);
                    hmap.put(CommonConstants.USER_ID, id);
                    hmap.put("DEBIT_LOAN_TYPE", "DP");//Added  by sreekrishnan
                    System.out.println("@@@@@@@ before authorization" + hmap);
                    lockerSurrenderDAO.execute(hmap);
                    System.out.println("@@@@@@@ after authorization" + hmap);
                    batchList.add(batchmap.get("BATCH_ID"));
                }

            } catch (Exception e) {
                String locno = CommonUtil.convertObjToStr(list.get(1));
                System.out.println("the loc no is" + locno);
                if (errorMap == null) {
                    errorMap = new HashMap();
                }
                if (e instanceof TTException) {
                    errorMap.put(locno, e);
                } else {
                    errorMap.put(locno, new TTException(e));
                }

                System.out.println("the error map is" + errorMap);
                System.out.println("the batchmap map is" + batchmap);
                if (interestList.size() >= 1) {
                    interestList.set(0, errorMap);
                    System.out.println("in if block");
                } else {
                    interestList.add(0, errorMap);
                }
                e.printStackTrace();
            }
        }
        batchList.add(trans_mode);
        batchList.add(batchmap.get("SINGLE_TRANS_ID"));
        interestList=null;
        interestList = new ArrayList();
        interestList.add(batchList);
        System.out.println("the interestList is" + interestList);
        return interestList;
    }
   
      public List doLockerRentTrans(HashMap map, String command) throws Exception {

        HashMap batchmap = new HashMap();
        List interestList = null;
        ArrayList batchList = null;
        List interestRow = new ArrayList();
        HashMap errorMap = null;
        ArrayList finallist = (ArrayList) map.get("LOCKER_RENT_DEPOSIT_LIST");
        String branch = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        _branchCode = branch;
        String lock = CommonUtil.convertObjToStr(map.get("PROD_ID"));
        String mod = CommonUtil.convertObjToStr(map.get("MODE"));
        int ryear = CommonUtil.convertObjToInt(map.get("YEAR"));
        int rmonth = CommonUtil.convertObjToInt(map.get("MONTH"));
        String trans_mode = CommonConstants.TX_TRANSFER;
        LockerSurrenderDAO lockerSurrenderDAO = new LockerSurrenderDAO();
        LockerSurrenderTO lockerSurrenderTO = new LockerSurrenderTO();
        ArrayList list = null;
        HashMap headMap = new HashMap();
        headMap.put("value", lock);
        interestList = new ArrayList();
        lockerTransHeadList = sqlMap.executeQueryForList("getLockerAccountHeads", headMap);
        generateSingleTransId = this.generateLinkID();
        for (int j = 0; j < finallist.size(); j++) {
            sqlMap.startTransaction();
            try {
                list = (ArrayList) finallist.get(j);
                batchList = new ArrayList();
                batchList.add(list.get(0));
                batchList.add(CommonUtil.convertObjToStr(list.get(4)));
                double charge = CommonUtil.convertObjToDouble(list.get(5));
                mod = CommonUtil.convertObjToStr(map.get("MODE"));
                double service = CommonUtil.convertObjToDouble(list.get(6));
                HashMap whereMap = new HashMap();
                whereMap.put("LOCKER_NUM", list.get(0));
                whereMap.put("PROD_ID", lock);
                HashMap resultMap = (HashMap) sqlMap.executeQueryForObject("getDetails", whereMap);
                String custid = "";
                String name = "";
                if (resultMap != null && resultMap.containsKey("CUST_ID")) {
                    custid = CommonUtil.convertObjToStr(resultMap.get("CUST_ID"));
                    name = CommonUtil.convertObjToStr(resultMap.get("NAME"));
                }
                ChargesServiceTaxTO chargesServiceTaxTO = new ChargesServiceTaxTO();
                TransactionTO transactionTO = new TransactionTO();
                lockerSurrenderTO = new LockerSurrenderTO();
                lockerSurrenderDAO = new LockerSurrenderDAO();
                transactionTO.setProductId(CommonUtil.convertObjToStr(list.get(2)));
                transactionTO.setProductType(CommonUtil.convertObjToStr(list.get(3)));
                transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(list.get(4)));
                transactionTO.setApplName(name);
                transactionTO.setTransType(CommonConstants.TX_TRANSFER);
                transactionTO.setInstType("vocher");
                transactionTO.setChequeDt(currDt);
                lockerSurrenderTO.setLocNum(CommonUtil.convertObjToStr(list.get(0)));
                lockerSurrenderTO.setCustId(custid);
                lockerSurrenderTO.setBranchID(branch);
                lockerSurrenderTO.setSurRenew("RENEW");
                lockerSurrenderTO.setProdId(lock);
                lockerSurrenderTO.setAcctName(name);
                double amount = CommonUtil.convertObjToDouble(list.get(5));
                double serv = CommonUtil.convertObjToDouble(list.get(6));
                double fine = 0;
                lockerSurrenderTO.setCharges(CommonUtil.convertObjToDouble(amount));
                lockerSurrenderTO.setServiceTax(CommonUtil.convertObjToDouble(serv));
                lockerSurrenderTO.setPenalAmount(fine);
                transactionTO.setTransAmt(CommonUtil.convertObjToDouble(amount + serv + fine));
                lockerSurrenderTO.setCommand(mod);
                lockerSurrenderTO.setSurDt(currDt);
                transactionTO.setCommand("INSERT");
                if (map.containsKey("SERVIC_TAX_REQ") && map.get("SERVIC_TAX_REQ") != null && CommonUtil.convertObjToStr(map.get("SERVIC_TAX_REQ")).equalsIgnoreCase("Y")) {
                    HashMap serviceTaxMap = calculateServiceTax(amount, serv);
                    map.put("serviceTaxDetails", serviceTaxMap);
                    map.put("serviceTaxDetailsTO", setServiceTaxDetails(serviceTaxMap, CommonUtil.convertObjToStr(list.get(0))));
                    transactionTO.setTransAmt(amount + serv + fine + CommonUtil.convertObjToDouble(serviceTaxMap.get("TOT_TAX_AMT")));
                }
                map.put("MODE", mod);
                map.put("BRANCH_CODE", branch);
                map.remove("LOCKER_RENT_DEPOSIT_LIST");
                map.remove("name");
                map.remove("PROD_ID");
                LinkedHashMap transMap = new LinkedHashMap();
                transMap.put("1", transactionTO);
                LinkedHashMap notDeleteMap = new LinkedHashMap();
                notDeleteMap.put("NOT_DELETED_TRANS_TOs", transMap);
                map.put("TransactionTO", notDeleteMap);
                map.put("LockerSurrenderTO", lockerSurrenderTO);
                map.put("generateSingleTransId", generateSingleTransId);
                map.put("LOCKER_DEPOSIT_SI_DAO", "LOCKER_DEPOSIT_SI_DAO");
                batchmap = lockerSurrenderDAO.execute(map);
                map.remove("serviceTaxDetails");
                map.remove("serviceTaxDetailsTO");
                String id = CommonUtil.convertObjToStr(map.get("USER_ID"));
                mod = "Authorize";
                map.remove("COMMAND");
                HashMap singleAuthorizeMap = new HashMap();
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                HashMap shmap = new HashMap();
                String listyear = CommonUtil.convertObjToStr(list.get(8));
                Date listdate = DateUtil.getDateMMDDYYYY(listyear);
                int listday = listdate.getDate();
                int listmon = listdate.getMonth() + 1;
                int listyy = listdate.getYear() + 1900;
                int lstyear = 0;
                if (listyy == ryear) {
                    if (listmon <= rmonth) {
                        lstyear = ryear + 1;
                    }
                } else {
                    if (listmon < rmonth) {
                        lstyear = ryear + 1;
                    } else {
                        lstyear = ryear;
                    }
                }
                Date rentdate = DateUtil.getDate(listday, listmon, lstyear);
                authDataMap.put("LOCKER_NUM", CommonUtil.convertObjToStr(list.get(0)));
                authDataMap.put("CUST_ID", custid);
                authDataMap.put("PROD_ID", lock);
                authDataMap.put("ISSUE_ID", CommonUtil.convertObjToStr(map.get("LOCKER_SURRENDER_ID")));
                authDataMap.put("SUR_OR_RENEW", "RENEW");
                arrList.add(authDataMap);
                HashMap authmap = new HashMap();
                authmap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                authmap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authmap.put(CommonConstants.USER_ID, id);
                authmap.put("year", rentdate);
                authmap.put("COLLECT_RENT_YEAR", lstyear);
                authmap.put("COLLECT_RENT_MONTH",listmon);
                HashMap authExeMap = new HashMap();
                authExeMap.put(CommonConstants.AUTHORIZEMAP, authmap);
                HashMap fhashmap = new HashMap();
                transactionTO.setCommand("Authorize");
                transactionTO.setBatchId(map.get("LOCKER_SURRENDER_ID").toString());
                String batch = transactionTO.getBatchId();
                authExeMap.put("TransactionTO", notDeleteMap);
                authExeMap.put(CommonConstants.BRANCH_ID, branch);
                authExeMap.put(CommonConstants.USER_ID, id);
                authExeMap.put("DEBIT_LOAN_TYPE", "DP");
                  authExeMap.put("LOCKER_DEPOSIT_SI_DAO", "LOCKER_DEPOSIT_SI_DAO");
                  lockerSurrenderDAO.execute(authExeMap);
                  depositIntrestTransaction(CommonUtil.convertObjToStr(list.get(4)), transactionTO.getTransAmt(), CommonUtil.convertObjToStr(list.get(2)));
                  batchList.add(batchmap.get("BATCH_ID"));
                  batchList.add(trans_mode);
                  batchList.add(batchmap.get("SINGLE_TRANS_ID"));
                  interestList.add(batchList);
                  sqlMap.commitTransaction();
              } catch (Exception e) {
                  batchList.add("Error" + e.getMessage());
                  batchList.add(trans_mode);
                  batchList.add("");
                  interestList.add(batchList);
                  sqlMap.rollbackTransaction();
                  e.printStackTrace();
                  throw e;
              }
          }
        return interestList;
    }
            
    public ServiceTaxDetailsTO setServiceTaxDetails(HashMap serviceTax_Map,String lockerNum) {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setCommand("INSERT");
            objservicetaxDetTo.setStatus("CREATED");
            objservicetaxDetTo.setStatusBy(logTO.getUserId());
            objservicetaxDetTo.setAcct_Num(lockerNum);
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
            objservicetaxDetTo.setCreatedBy(logTO.getUserId());
            objservicetaxDetTo.setCreatedDt(currDt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
    private HashMap calculateServiceTax(double charges, double serviceTax) {
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (lockerTransHeadList != null && lockerTransHeadList.size() > 0) {
            HashMap lockerTransHeadMap = (HashMap) lockerTransHeadList.get(0);
            //-- GST for Penal --
            if (charges > 0) {
                String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("LOC_RENT_AC_HD"));
                HashMap checkForTaxMap = checkServiceTaxApplicable(achd);
                taxMap = getGSTAmountMap(checkForTaxMap);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, charges);
                if (taxMap != null && taxMap.size() > 0) {
                    taxSettingsList.add(taxMap);
                }
            }
            //-- GST for Penal --
            if (serviceTax > 0) {
                String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("SERV_TAX_AC_HD"));
                HashMap checkForTaxMap = checkServiceTaxApplicable(achd);
                taxMap = getGSTAmountMap(checkForTaxMap);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, serviceTax);
                if (taxMap != null && taxMap.size() > 0) {
                    taxSettingsList.add(taxMap);
                }
            }
            //-- GST for Penal --
//                    if (txtPenalAmt.getText().length() > 0) {
//                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("PENAL_INTEREST_AC_HEAD"));
//                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
//                        taxMap = getGSTAmountMap(checkForTaxMap);
//                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtPenalAmt.getText());
//                        if (taxMap != null && taxMap.size() > 0) {
//                            taxSettingsList.add(taxMap);
//                        }
//                    }
            System.out.println("taxSettingsList :: " + taxSettingsList);
            HashMap serviceTax_Map = setCaseExpensesAmount(taxSettingsList);
            return serviceTax_Map;
        }
        return null;
    }
    public void depositIntrestTransaction(String depNo, double creditAmt, String productId)throws Exception  {
        HashMap updateMap = new HashMap();
        updateMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(depNo).replaceAll("_1", ""));
        InterestBatchTO interestBatchTO = null;
        Date intApplyDate = DateUtil.addDays(currDt, -1);
        HashMap acctDtlMap = new HashMap();
        try {
            HashMap paramMap = new HashMap();
            paramMap.put("CUR_DT", currDt);
            paramMap.put("PROD_ID", productId);
            paramMap.put("ACT_NUM", depNo);
            List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
            if (acctDtl != null && acctDtl.size() > 0) {
                acctDtlMap = (HashMap) acctDtl.get(0);
                sqlMap.executeUpdate("updateLockerDepositTempDate", updateMap);
                interestBatchTO = new InterestBatchTO();
                Date intDt = (Date) currDt.clone();
                Date lstDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                if (lstDt.getDate() > 0) {
                    intDt.setDate(lstDt.getDate());
                    intDt.setMonth(lstDt.getMonth());
                    intDt.setYear(lstDt.getYear());
                }
                interestBatchTO.setIntDt(intDt);
//                interestBatchTO.setIntDt(currDt);
                interestBatchTO.setApplDt(intApplyDate);
                interestBatchTO.setActNum(depNo);
                interestBatchTO.setProductId(productId);
                interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                interestBatchTO.setCustId(CommonUtil.convertObjToStr(acctDtlMap.get("CUST_ID")));
                interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")));
                interestBatchTO.setIntType("SIMPLE");
                interestBatchTO.setProductType(TransactionFactory.DEPOSITS);
                interestBatchTO.setTrnDt(currDt);
                HashMap renewalCountMap = new HashMap();
                renewalCountMap.put("ACT_NUM", CommonUtil.convertObjToStr(depNo).replaceAll("_1", ""));
                List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                if (lstCount != null && lstCount.size() > 0) {
                    renewalCountMap = (HashMap) lstCount.get(0);
                    interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                }
                if (creditAmt > 0) {
                    interestBatchTO.setDrCr("CREDIT");
                    interestBatchTO.setTransLogId("A");
                    interestBatchTO.setIntAmt(CommonUtil.convertObjToDouble(creditAmt));
                    interestBatchTO.setTot_int_amt(CommonUtil.convertObjToDouble(creditAmt));
                    sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                    sqlMap.executeUpdate("updateLastInterestAmount", interestBatchTO);
                    HashMap upMapint = new HashMap();
                    upMapint.put("actNum", depNo);
                    upMapint.put("applDt", intApplyDate);
                    sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                    Date nextIntappldt = (Date) currDt.clone();
                    Date nextDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("NEXT_INT_APPL_DT")));
                    if (nextDt.getDate() > 0) {
                        nextIntappldt.setDate(nextDt.getDate());
                        nextIntappldt.setMonth(nextDt.getMonth());
                        nextIntappldt.setYear(nextDt.getYear());
                    }
                    upMapint.put("nextIntappldt", nextIntappldt);
                    sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);
                    interestBatchTO.setDrCr("DEBIT");
                    interestBatchTO.setTransLogId("A");
                    interestBatchTO.setIntAmt(CommonUtil.convertObjToDouble(creditAmt));
                    interestBatchTO.setTot_int_amt(CommonUtil.convertObjToDouble(0.0));
                    sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                    sqlMap.executeUpdate("updateTotIndDrawnAmount", interestBatchTO);
                }
            }
        } catch (Exception e) {
          sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        } finally {
            creditAmt = 0.0;
            interestBatchTO = null;
        }
    }

    private HashMap getGSTAmountMap(HashMap checkForTaxMap) {
        HashMap taxMap = new HashMap();
        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
            }
        }
        return taxMap;
    }
private HashMap setCaseExpensesAmount(List taxSettingsList) {     
        ServiceTaxCalculation objServiceTax;
        HashMap serviceTax_Map = new HashMap();
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } 
             //   return serviceTax_Map;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
         return serviceTax_Map;
    }
        public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
//        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = null;                  
            try {
                accHeadList = sqlMap.executeQueryForList("getCheckServiceTaxApplicableForShare", whereMap);
            } catch (SQLException ex) {
                Logger.getLogger(LockerRentSIApplicationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
//        }
        return checkForTaxMap;
    }
    private String getLockerSurrenderId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOCKER_SURRENDER_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public java.lang.String getLockerIssId() {
        return lockerIssId;
    }

    /**
     * Setter for property lockerIssId.
     *
     * @param lockerIssId New value of property lockerIssId.
     */
    public void setLockerIssId(java.lang.String lockerIssId) {
        this.lockerIssId = lockerIssId;
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
}