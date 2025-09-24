/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositMultiClosingDAO.java
 * 
 * Created on Tue Jan 11 13:18:08 IST 2015
 */
package com.see.truetransact.serverside.deposit.multipleclosing;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.deposit.closing.DepositClosingDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import java.util.*;

/**
 * DepositMultiClosing DAO.
 *
 */
public class DepositMultiClosingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private Map returnMap = null;
    private Iterator processLstIterator;
    private TransactionDAO transactionDAO = null;

    /**
     * Creates a new instance of DepositMultiClosingDAO
     */
    public DepositMultiClosingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            DepositMultiClosingDAO dao = new DepositMultiClosingDAO();
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

    private String generateMultiDepositClosingID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MULTI_DEP_CLOSE_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String multiID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return multiID;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap dataMap = new HashMap();
        HashMap authMap = new HashMap();
        HashMap finalMap = new HashMap();
        String key1 = "";
        System.out.println(" ###### Map in mmmmmm DAO : " + map);
//        String singleTransIdForDepMultiClosing = null;
        String singleDepMultiClosingID = null;
        try {
            returnMap = new HashMap();
            if (map != null && map.size() > 0) {
//                singleTransIdForDepMultiClosing = generateLinkID();
                singleDepMultiClosingID = generateMultiDepositClosingID();
                finalMap = (HashMap) map.get("finalMap");
                processLstIterator = finalMap.keySet().iterator();
                if (map.containsKey("MULTIPLE_DEPOSIT_CLOSING")) {
                    for (int i = 0; i < finalMap.size(); i++) {
                        key1 = (String) processLstIterator.next();
                        System.out.println(" ## map.get(key1): " + finalMap.get(key1));
                        dataMap = (HashMap) finalMap.get(key1);
//                        dataMap.put("MULTI_CLOSE_SINGLE_TRANS_ID", singleTransIdForDepMultiClosing);
                        dataMap.put("MULTIPLE_DEPOSIT_CLOSING", "MULTIPLE_DEPOSIT_CLOSING");
                        dataMap.put(CommonConstants.SCREEN,"Deposit Multiple Closing");
                        DepositClosingDAO daoDep = new DepositClosingDAO();
                        
                        returnMap = daoDep.execute(dataMap);
                        if (returnMap.containsKey("STATUS")) {
                            if (returnMap.get("STATUS").equals("SUCCESS")) {
                                HashMap closMap = (HashMap) dataMap.get("CLOSUREMAP");
                                double additionalInt = 0.0;
                                System.out.println("multi all map " + dataMap);
                                if (closMap != null && closMap.size() > 0) {
                                    closMap.put(CommonConstants.STATUS, CommonConstants.STATUS_CREATED);
                                    closMap.put("CLOSE_DT", CommonUtil.getProperDate(currDt, currDt));
                                    closMap.put("DEPOSIT_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closMap.get("DEPOSIT_DT")))));
                                    closMap.put("MATURITY_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closMap.get("MATURITY_DT")))));
                                    closMap.put("MULTI_DEP_CLOSE_ID", singleDepMultiClosingID);
                                    closMap.put("ROI", CommonUtil.convertObjToDouble(closMap.get("ROI")));
                                    closMap.put("DEPOSIT_AMT", CommonUtil.convertObjToDouble(closMap.get("DEPOSIT_AMT")));
                                    closMap.put("PAID_INTEREST", CommonUtil.convertObjToDouble(closMap.get("PAID_INTEREST")));
                                    closMap.put("DR_INTEREST", CommonUtil.convertObjToDouble(closMap.get("DR_INTEREST")));
                                    if(closMap.containsKey("MODE") && closMap.get("MODE") != null && CommonUtil.convertObjToStr(closMap.get("MODE")).equals("NORMAL")){
                                       if(closMap.containsKey("ADD_INT_AMOUNT") && closMap.get("ADD_INT_AMOUNT") != null){
                                           additionalInt = CommonUtil.convertObjToDouble(closMap.get("ADD_INT_AMOUNT"));
                                       }
                                    }
                                    closMap.put("PAY_AMT", (CommonUtil.convertObjToDouble(closMap.get("PAY_AMT")) + additionalInt));
                                    closMap.put("TOTAL_AMT", CommonUtil.convertObjToDouble(closMap.get("TOTAL_AMT")) - additionalInt);
                                    closMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(closMap.get("TOTAL_BALANCE")));
                                    closMap.put("CUST_ID_UP", CommonUtil.convertObjToStr(map.get("CUST_ID_UP")));
                                    closMap.put("FROM_ACC_CLOSE", CommonUtil.convertObjToStr(map.get("FROM_ACC_CLOSE")));
                                    closMap.put("TO_ACC_CLOSE", CommonUtil.convertObjToStr(map.get("TO_ACC_CLOSE")));
                                    if(closMap.containsKey("TDS_SHARE") && closMap.get("TDS_SHARE") != null){
                                      closMap.put("TDS_AMOUNT", CommonUtil.convertObjToDouble(closMap.get("TDS_SHARE")));  
                                    }else{
                                      closMap.put("TDS_AMOUNT", CommonUtil.convertObjToDouble(0.0));  
                                    }
                                    if (dataMap.containsKey("TransactionTO")) {
                                        HashMap transMap = (HashMap) dataMap.get("TransactionTO");
                                        if (transMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                            System.out.println("inside 2");
                                            HashMap transFinalMap = (HashMap) transMap.get("NOT_DELETED_TRANS_TOs");
                                            TransactionTO transto = (TransactionTO) transFinalMap.get("1");
                                            System.out.println("to her" + transto);
                                            System.out.println("trans type hr" + transto.getTransType());
                                            closMap.put("CLOSING_MODE", CommonUtil.convertObjToStr(transto.getTransType()));
                                            closMap.put("PROD_TYPE", CommonUtil.convertObjToStr(transto.getProductType()));
                                            closMap.put("PROD_ID_TRANS", CommonUtil.convertObjToStr(transto.getProductId()));
                                            closMap.put("ACT_NUM", CommonUtil.convertObjToStr(transto.getDebitAcctNo()));
                                        }
                                    }
                                    sqlMap.executeUpdate("insertDepositMultiClosingTemp", closMap);
                                }
                            }
                        }
                    }
                }
                if (map.containsKey("MULTIPLE_DEPOSIT_AUTHORIZE") && map != null) {
                    processLstIterator = finalMap.keySet().iterator();
                    for (int i = 0; i < finalMap.size(); i++) {
                        key1 = (String) processLstIterator.next();
                        authMap = new HashMap();
                        HashMap closeMap = (HashMap) finalMap.get(key1);
                        System.out.println("closu map auth: " + closeMap);
                        HashMap closureMap = new HashMap();
                        closureMap = (HashMap) closeMap.get("CLOSUREMAP");
                        authMap.put("USER_ID", CommonUtil.convertObjToStr(closeMap.get("USER_ID")));
                        authMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                        authMap.put("UI_PRODUCT_TYPE", "TD");
                        HashMap authorizeMap = new HashMap();
                        authorizeMap.put("USER_ID", CommonUtil.convertObjToStr(closeMap.get("USER_ID")));
                        if (map.containsKey(CommonConstants.STATUS_AUTHORIZED)) {
                            authorizeMap.put("AUTHORIZESTATUS", CommonConstants.STATUS_AUTHORIZED);
                        } else if (map.containsKey(CommonConstants.STATUS_REJECTED)) {
                            authorizeMap.put("AUTHORIZESTATUS", CommonConstants.STATUS_REJECTED);
                        }
                        authorizeMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(_branchCode));
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(closeMap.get("USER_ID")));
                        String depNo = CommonUtil.convertObjToStr(closureMap.get("DEPOSIT_NO"));
                        if (depNo != null && depNo.contains("_")) {
                            int index = depNo.indexOf("_");
                            depNo = depNo.substring(0, index);
                            System.out.println(" depNo -----> " + depNo);
                        }
                        authDataMap.put("ACCOUNTNO", depNo + "_1");
                        arrList.add(authDataMap);
                        authorizeMap.put("AUTHORIZEDATA", arrList);
                        authMap.put("AUTHORIZEMAP", authorizeMap);
                        authMap.put("MODE", closeMap.get("MODE"));
                        authMap.put("CLOSUREMAP", closeMap.get("CLOSUREMAP"));
                        authMap.put("MULTIPLE_DEPOSIT_CLOSING", closeMap.get("MULTIPLE_DEPOSIT_CLOSING"));
                        DepositClosingDAO daoDep1 = new DepositClosingDAO();
                        returnMap = daoDep1.execute(authMap);
                    }
                    dataMap = null;
                    authMap = null;
                    finalMap = null;
                }
            }
        } catch (Exception e) {
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
        HashMap returnMap = new HashMap();
        return executeQuery(obj, true);
    }

    public HashMap executeQuery(HashMap obj, boolean enableTrans) throws Exception {
        System.out.println("#### Inside DepositClosingDAO executeQuery..." + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        HashMap output = new HashMap();
        DepositClosingDAO depClosDAO = new DepositClosingDAO();
        output = depClosDAO.executeQuery(obj);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return output;
    }

    private void destroyObjects() {
    }
}