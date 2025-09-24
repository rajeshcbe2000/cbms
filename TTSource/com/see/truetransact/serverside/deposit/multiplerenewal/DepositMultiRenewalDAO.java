/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationDAO.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.deposit.multiplerenewal;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.deposit.TermDepositDAO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import java.util.*;

/**
 * DepositInterestApplication DAO.
 *
 */
public class DepositMultiRenewalDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;
    private Iterator processLstIterator;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public DepositMultiRenewalDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            DepositMultiRenewalDAO dao = new DepositMultiRenewalDAO();
            HashMap inputMap = new HashMap();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap dataMap = new HashMap();
        HashMap authMap = new HashMap();
        System.out.println("###### DepositMultiRenewal exeute method in DAO : ###### " + map);
        String singleTransIdForDepMultiRenewal = null;
        try {
            sqlMap.startTransaction();
            returnMap = new HashMap();
            if (map != null) {
                singleTransIdForDepMultiRenewal = generateLinkID();//Commented By Suresh R  19-Jun-2019  (KDSA-548 : Multiple deposit rollback and multiple renewal rollback has not done properly)
                System.out.println(" ######map.size()O : " + map.size());
                HashMap finalMap = (HashMap) map.get("finalMap");
                processLstIterator = finalMap.keySet().iterator();
                String key1 = "";                
                for (int i = 0; i < finalMap.size(); i++) {
                    System.out.println("#@#@#@#@#@#@#@#@#@#@  i : " + i);
                    key1 = (String) processLstIterator.next();                                        
                    dataMap = (HashMap) finalMap.get(key1);
//                    singleTransIdForDepMultiRenewal = generateLinkID();//Added By Suresh R  19-Jun-2019  (KDSA-548 : Multiple deposit rollback and multiple renewal rollback has not done properly)
                    //Multiple Deposit Creation generating Single TransID. So Multiple Renewal also should be Single Trans ID for Every Deposit, Otherwise Rollback will be the problem.(Discussed with Jithesh & Sathiya, Then coding changes done)
                    dataMap.put("MULTI_REN_SINGLE_TRANS_ID", singleTransIdForDepMultiRenewal);
                    TermDepositDAO daoDep = new TermDepositDAO();
                    daoDep.execute(dataMap, false);
                    authMap = new HashMap();
                    HashMap renMap = (HashMap) dataMap.get("RENEWALMAP");
                    authMap.put("USER_ID", CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                    authMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                    authMap.put("UI_PRODUCT_TYPE", "TD");
                    HashMap authorizeMap = new HashMap();
                    authorizeMap.put("USER_ID", CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                    authorizeMap.put("AUTHORIZESTATUS", "AUTHORIZED");
                    authorizeMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                    authorizeMap.put("DEPOSIT_MULTIPLE_RENEWAL","DEPOSIT_MULTIPLE_RENEWAL");//Added By Kannan AR
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(renMap.get("USER_ID")));
                    String depNo = CommonUtil.convertObjToStr(renMap.get("DEPOSIT_NO"));
                    if (depNo != null && depNo.contains("_")) {
                        int index = depNo.indexOf("_");
                        depNo = depNo.substring(0, index);                        
                    }
                    authDataMap.put("DEPOSIT NO", depNo);
                    arrList.add(authDataMap);
                    authorizeMap.put("AUTHORIZEDATA", arrList);
                    authMap.put("AUTHORIZEMAP", authorizeMap);
                    TermDepositDAO daoDep1 = new TermDepositDAO();
                    daoDep1.execute(authMap,false); 
                }
                returnMap.put("STATUS", "S");

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            System.out.println("#@#@#@#@#@#@#@#@#@#@  roll Transaction : ");
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            returnMap.put("STATUS", "F");
            throw e;
        }
        destroyObjects();

        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        if (obj.containsKey("TDS_CALCULATION")) {   // Added by nithya on 06-02-2020 for KD-1090         
            returnMap = calcuateTDS(obj, true);           
        }
        return returnMap;
    }
    
    private HashMap calcuateTDS(HashMap obj, boolean enableTrans) throws Exception { // Added by nithya on 06-02-2020 for KD-1090
        //System.out.println("####### doDepositClose tdsMap " + obj);
        String depositBranch = (String) obj.get(CommonConstants.BRANCH_ID);
        HashMap tdsCalcMap = new HashMap();
        tdsCalcMap = obj;
        //System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;      
        //System.out.println("excecuting here....1");
        TdsCalc tdsCalculator = new TdsCalc(depositBranch);
        //System.out.println("executing here2");
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
        //System.out.println("executing here8888888888888888888");
        Date tdsDate = (Date) ServerUtil.getCurrentDate(depositBranch);;
        tdsMap.put("INT_DATE", tdsDate.clone());        
        //System.out.println("executing here========================");
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        HashMap whereMap = new HashMap();//TDS product Level Checking Calculate Yes/No
        whereMap.put("PROD_ID", prod_id);
        //System.out.println("******************** where" + whereMap);
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
                        //System.out.println("####### Final Debenture Transfer tdsMap " + tdsMap);                       
                    }
                }
            }
        }
        return tdsMap;
    }

    private void destroyObjects() {
//        standingLst = null;
    }
}
