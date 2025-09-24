/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.transaction.multipleCash;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import org.apache.log4j.Logger;

import com.see.truetransact.transferobject.transaction.multipleCash.MultipleCashTransactionTO;
import java.util.Date;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class MultipleCashTransactionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private MultipleCashTransactionTO objTO;
    MultipleCashTransactionTO multipleCashTO;
    private LogDAO logDAO;
    private LogTO logTO;
    HashMap returnMap;
    List returnList;
    List list;
    private Date currDt = currDt = ServerUtil.getCurrentDate(_branchCode);
    private final static Logger log = Logger.getLogger(MultipleCashTransactionDAO.class);

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public MultipleCashTransactionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

      private CashTransactionTO createCashTransactionTO(HashMap dataMap,String generateSingleTransId) throws Exception{
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("LINKID")));
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACC_NO")));
        objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
        objCashTO.setTransType(CommonConstants.CREDIT);
        objCashTO.setParticulars("By Cash : " + dataMap.get("PARTICULARS"));
        objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objCashTO.setInitTransId(multipleCashTO.getStatusBy());
        objCashTO.setBranchId(_branchCode);
        objCashTO.setStatusBy(multipleCashTO.getStatusBy());
        objCashTO.setStatusDt(currDt);
        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        objCashTO.setAuthorizeRemarks("MULTIPLE_CASH");
        objCashTO.setInitiatedBranch(_branchCode);
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setSingleTransId(generateSingleTransId);
        objCashTO.setTransModType("GL");
        objCashTO.setCommand("INSERT");
        objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
        objCashTO.setScreenName(multipleCashTO.getScreenName());
        System.out.println("objCashTO 1st one:" + objCashTO);return objCashTO;
    }

   private CashTransactionTO createCashTransactionTODebit(HashMap dataMap,String generateSingleTransId) throws Exception{
        CashTransactionTO objCashTO = new CashTransactionTO();
        System.out.println("dataMap.get(LINKID)>>>" + dataMap.get("LINKID"));
        objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("LINKID")));
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACC_NO")));
        objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
        objCashTO.setTransType(CommonConstants.DEBIT);
        objCashTO.setParticulars("By Cash : " + dataMap.get("PARTICULARS"));
        objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objCashTO.setInitTransId(multipleCashTO.getStatusBy());
        objCashTO.setBranchId(_branchCode);
        objCashTO.setStatusBy(multipleCashTO.getStatusBy());
        objCashTO.setStatusDt(currDt);
        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        objCashTO.setAuthorizeRemarks("MULTIPLE_CASH");
        objCashTO.setInitiatedBranch(_branchCode);
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setCommand("INSERT");
	objCashTO.setSingleTransId(generateSingleTransId);
        objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_NO")));
        objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
        objCashTO.setInstType(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_TYPE")));
        objCashTO.setTransModType("GL");
        objCashTO.setInstDt(CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_DATE")))));
        objCashTO.setScreenName(multipleCashTO.getScreenName());
        System.out.println("objCashTO 1st one:" + objCashTO);
        return objCashTO;
    }

    private void insertData(HashMap map) throws Exception {
        System.out.println("map in insert data---"+map);
        try {
            String generateSingleTransId = generateLinkID();
            sqlMap.startTransaction();
            HashMap amap;
            System.out.println("list..." + list);
            for (int i = 0; i < list.size(); i++) {
                amap = (HashMap) list.get(i);
                if (amap.containsKey("TRANSACTION_TYPE") && amap.get("TRANSACTION_TYPE").equals("CR")) {
                    ArrayList cashList = new ArrayList();
                    cashList.add(createCashTransactionTO(amap, generateSingleTransId));
                    doCashTrans(cashList, _branchCode, false);
                }
                if (amap.containsKey("TRANSACTION_TYPE") && amap.get("TRANSACTION_TYPE").equals("DB")) {
                    ArrayList cashList = new ArrayList();
                    cashList.add(createCashTransactionTODebit(amap, generateSingleTransId));
                    doCashTrans(cashList, _branchCode, false);
                }
            }
            returnMap.put("CASH_TRANS_LIST", returnList);
            returnList = new ArrayList();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
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
    
     private  void doCashTrans(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
       
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
        data.put("SCREEN_NAME", "Multiple Cash");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        HashMap cashTransMap = cashDAO.execute(data, false);
        System.out.println("########### cashTransMap" + cashTransMap);
        currDt = currDt = (Date) currDt.clone();
        System.out.println("currDt.." + currDt + "bbbb");
        cashTransMap.put("TRANS_DT", currDt);
        cashTransMap.put("BRANCH_CODE", _branchCode);
        System.out.println("cashTransMap,,,," + cashTransMap);
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsMultiple", cashTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnList.add(cashList);
            System.out.println("########cashlist>>>>>>>>>>>///" + cashList);
        }
        HashMap linkBatchMap = new HashMap();
        linkBatchMap.put("LINK_BATCH_ID", cashTransMap.get("TRANS_ID"));
        linkBatchMap.put("TRANS_ID", cashTransMap.get("TRANS_ID"));
        linkBatchMap.put("TRANS_DT", currDt);
        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        returnMap = new HashMap();
        returnList = new ArrayList();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        System.out.println("currDt----"+currDt);
        logDAO = new LogDAO();
        logTO = new LogTO();
        try {
            list = new ArrayList();
            list = (ArrayList) map.get("LIST");
            multipleCashTO = new MultipleCashTransactionTO();
            multipleCashTO = (MultipleCashTransactionTO) map.get("MultipleCashTO");
            objTO.setBranCode(_branchCode);
        } catch (Exception e) {
            System.out.println("ee" + e);
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            if (authMap != null) {
                authorizeNew(map, authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void authorizeNew(HashMap authMap, HashMap map) throws Exception {
        System.out.println("autnMap in authorize---"+authMap);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        System.out.println("mmmmmmmm" + map);
        String status = CommonUtil.convertObjToStr(map.get("AUTHORIZESTATUS"));
        String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
        String singleTransId = CommonUtil.convertObjToStr(map.get("SINGLE_TRANS_ID"));
        CashTransactionDAO transactionDAO = null;
        try {
            sqlMap.startTransaction();
            System.out.println("singleTransID---" + singleTransId);
            HashMap detailsMap = new HashMap();
            detailsMap.put("SINGLE_TRANS_ID", singleTransId);
            detailsMap.put("TRANS_DT", CommonUtil.getProperDate(currDt,currDt));
            List dataList = sqlMap.executeQueryForList("getAllForSelectedFromCash", detailsMap);
            System.out.println("dataListdataList" + dataList);
            if (dataList != null && dataList.size() > 0) {
                transactionDAO = new CashTransactionDAO();
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap dataMapNew = (HashMap) dataList.get(i);
                    if (dataMapNew != null && dataMapNew.size() > 0 && dataMapNew.containsKey("TRANS_ID")) {
                        String transId = CommonUtil.convertObjToStr(dataMapNew.get("TRANS_ID"));
                        System.out.println("transIdtransId" + transId);
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList arrList = new ArrayList();
                        HashMap singleAuthorizeMap = new HashMap();
                        singleAuthorizeMap.put("STATUS", status);
                        singleAuthorizeMap.put("TRANS_ID", transId);
                        singleAuthorizeMap.put("USER_ID", userId);
                        arrList.add(singleAuthorizeMap);
                        System.out.println("before making new DAO map :" + map);
                        map = new HashMap();
                        map.put("SCREEN", "Cash");
                        map.put("USER_ID", userId);
                        map.put("SELECTED_BRANCH_ID", branchCode);
                        map.put("BRANCH_CODE", branchCode);
                        map.put("MODULE", "Transaction");
                        map.put("SCREEN_NAME", "Multiple Cash");
                        HashMap dataMap = new HashMap();
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        dataMap.put("DAILY", "DAILY");
                        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                        System.out.println("before entering DAO map :" + map);
                        cashTransactionDAO.execute(map, false);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            System.out.println("eeeee>>>" + e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
        return obj;
    }

    private void destroyObjects() {
        objTO = null;
        logTO = null;
        logDAO = null;
    }
}
