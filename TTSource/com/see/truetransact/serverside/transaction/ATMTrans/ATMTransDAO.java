/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ATMTransDAO.java
 */

package com.see.truetransact.serverside.transaction.ATMTrans;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author noora
 */
public class ATMTransDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private Date currDt = null;

    public ATMTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    /**
     * executeQuery
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = null;
        int transId = 0;
        if (CommonUtil.convertObjToStr(obj.get("COMMAND")).equals("ENQ")) {
            transId = setATMLog(obj);
            returnMap = getBalance(obj);
            String intiatedBranch = getInitiatedATMTerminalId(obj);
            returnMap.put("INITIATED_BRANCH", intiatedBranch);
        } else if (CommonUtil.convertObjToStr(obj.get("COMMAND")).equals("MST")) {
            transId = setATMLog(obj);
            List resultList = getMiniStatement(obj);
            returnMap = new HashMap();
            returnMap.put("RESULT_LIST", resultList);
            String intiatedBranch = getInitiatedATMTerminalId(obj);
            returnMap.put("INITIATED_BRANCH", intiatedBranch);
        } else if (CommonUtil.convertObjToStr(obj.get("COMMAND")).equals("WDL")) {
            returnMap = new HashMap();
            String intiatedBranch = getInitiatedATMTerminalId(obj);
            returnMap.put("INITIATED_BRANCH", intiatedBranch);
            if (obj.get("DEBIT_FLAG") != null) {
                Boolean debitFlag = (Boolean) obj.get("DEBIT_FLAG");
                if (!debitFlag) {
                    //reversal request
                    String traceNo = obj.get("REVERSAL_TRACE_NO") == null ? "" : obj.get("REVERSAL_TRACE_NO").toString();
                    if (traceNo.trim().length() > 0) {
                        if (checkOriginalMessageExists(obj)) {
                            List list = getATMLogByReversalTraceNo(obj);
                            if (list == null || list.size() < 2) {//process reversal
                                System.out.println("map size :: " + list.size());
                                returnMap = execute(obj, true);
                                returnMap.put("ALREADY_PROCESSED", 0);
                            } else {
                                //already processed reversal
                                returnMap.put("ALREADY_PROCESSED", 1);
                            }
                        } else {
                            returnMap.put("ORIGINAL_MESSAGE_EXISTS", 0);
                        }

                    } else {
                        returnMap = execute(obj, true);
                        returnMap.put("ALREADY_PROCESSED", 0);
                    }
                } else {
                    //withdrawal request
                    returnMap = execute(obj, true);
                }
            } else {
                returnMap = execute(obj, true);
            }
            transId = setATMLog(obj);
        }
        return returnMap;
    }

    /**
     * executeQuery
     */
    public HashMap executeUpdate(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = new HashMap();
        int transId = updateATMLog(obj);

        returnMap.put("ATM_TRANSACTION_ID", transId);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    private int setATMLog(HashMap map) throws Exception {
        int transId = sqlMap.executeUpdate("setATMLog", map);
        return transId;
    }

    private List getATMLogByReversalTraceNo(HashMap map) throws Exception {
        HashMap returnMap = null;
        List list = (List) sqlMap.executeQueryForList("getATMLogByReversalTraceNo", map);
//        if (list != null && list.size() > 0) {
//            returnMap = (HashMap) list.get(0);
//        }
        return list;
    }

    private Boolean checkOriginalMessageExists(HashMap map) throws Exception {
        HashMap returnMap = null;
        List list = (List) sqlMap.executeQueryForList("getATMLogByTraceNo", map);
        String request;
        Boolean hasOriginalMessage = false;
        if (list != null && list.size() > 0) {
            for (Iterator it = list.iterator(); it.hasNext();) {
                returnMap = (HashMap) it.next();
                request = returnMap.get("REQUEST") == null ? "" : returnMap.get("REQUEST").toString();
                if (request.trim().length() > 0 && request.startsWith("0200") || request.startsWith("200")) {
                    hasOriginalMessage = true;
                    return hasOriginalMessage;
                }
            }
        }
        return hasOriginalMessage;
    }

    private int updateATMLog(HashMap map) throws Exception {
        int transId = sqlMap.executeUpdate("updateATMLog", map);
        return transId;
    }

    private HashMap getBalance(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getBalanceForATM", map);
        if (list != null && list.size() > 0) {
            returnMap = (HashMap) list.get(0);
        }
        return returnMap;
    }

    private String getInitiatedATMTerminalId(HashMap map) throws Exception {
        String intiatedBranch = "";
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getInitiatedATMTerminalId", map);
        if (list != null && list.size() > 0) {
            returnMap = (HashMap) list.get(0);
            intiatedBranch = returnMap.get("INITIATED_BRANCH").toString();
        }
        return intiatedBranch;
    }

    private List getMiniStatement(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getMiniStatementForATM", map);
        return list;
    }

    /*
     * private HashMap doCashTransaction(HashMap map) throws Exception {
     * //DB_DRIVER_NAME=oracle.jdbc.driver.OracleDriver, //IP_ADDR=192.168.1.2,
     * //USER_ID=administrator, //SELECTED_BRANCH_ID=8001, //SCREEN=Cash,
     * //DENOMINATION_LIST=null, //OLDAMOUNT=0.0,
     * //CashTransactionTO=<%%>command=INSERT<%%>class=CashTransactionTO<%%>package=com.see.truetransact.transferobject.transaction.cash.CashTransactionTO<%%>null<%%>key=transId<%%>value=<%%><%%>transId=<%%>acHdId=2005001001<%%>actNum=8001101037014<%%>inpAmount=0.0<%%>inpCurr=<%%>amount=200.0<%%>transDt=null<%%>transType=DEBIT<%%>instType=VOUCHER<%%>instDt=2013-10-15
     * 00:00:00.0<%%>tokenNo=A1<%%>initTransId=administrator<%%>initChannType=CASHIER<%%>particulars=To
     * test<%%>narration=<%%>status=<%%>instrumentNo1=<%%>instrumentNo2=<%%>availableBalance=null<%%>prodId=101<%%>prodType=OA<%%>authorizeStatus=null<%%>authorizeBy=<%%>authorize_By_2=<%%>authorizeDt=null<%%>authorizeRemarks=<%%>statusBy=administrator<%%>branchId=8001<%%>statusDt=null<%%>linkBatchId=null<%%>initiatedBranch=8001<%%>linkBatchDt=null<%%>panNo=<%%>loanHierarchy=<%%>authorizeStatus_2=null<%%>shift=<%%>,
     * // BANK_CODE=8000, // BRANCH_CODE=8001, // MODULE=Transaction, //
     * ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER=ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER,
     * // PRODUCTTYPE=OA
     *
     * }
     */
    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        HashMap execReturnMap = new HashMap();
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        execReturnMap = cashDAO.execute(map);
        cashDAO = null;
        System.out.println("execReturnMap in DAO :: " + execReturnMap);
        return execReturnMap;
    }
}
