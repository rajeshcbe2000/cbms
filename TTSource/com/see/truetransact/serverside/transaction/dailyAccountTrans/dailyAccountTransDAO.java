/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * dailyAccountTransDAO.java
 */

package com.see.truetransact.serverside.transaction.dailyAccountTrans;
import com.see.truetransact.serverside.payroll.arrear.*;

import com.see.truetransact.serverside.generalledger.GLOpeningUpdate.*;
import com.see.truetransact.serverside.supporting.balanceupdate.*;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import com.see.truetransact.transferobject.generalledger.GLOpeningUpdateTO;
import com.see.truetransact.transferobject.payroll.arrear.ArrearTO;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyAccountTransTO;
import java.util.Date;

public class dailyAccountTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private BalanceUpdate objBalanceUpdateTO;
    private String userID = "";
    private String branchCode = "";
    private ArrayList selectedArrayList;
    private ArrayList deletedArrayList;
    private int totalCount = 0;
    private BalanceUpdateTO objTO;
    BalanceUpdateTO objTO1 = new BalanceUpdateTO();
    Date currDt = null;
    int key = 1;
    private HashMap returnDataMap = new HashMap();

    public dailyAccountTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsPayRoll", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsPayRoll", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void doDebitCredit(ArrayList batchList,boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, _branchCode);
        data.put(CommonConstants.USER_ID, "admin");
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        returnDataMap = transferDAO.execute(data, false);
        System.out.println("returnDataMap^$^$^$^$^$^$^$"+returnDataMap);
        //if (isAutoAuthorize == true) {
        //    authorizeTransaction(returnDataMap);
        //}
    }

    public void authorizeTransaction(HashMap returnDataMap) throws Exception {
        try {
            if (returnDataMap != null && returnDataMap.get("TRANS_ID") != null && !returnDataMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + returnDataMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                cashAuthMap.put(CommonConstants.USER_ID, "admin");
                TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                //transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertImportData(HashMap map) throws Exception {
        try {
            DailyAccountTransTO accountTo = new DailyAccountTransTO();;
            ArrayList importList = new ArrayList();
            ArrayList importDataList = new ArrayList();
            if (map.containsKey("IMPORT_DATA_LIST")) {
                importList = (ArrayList) map.get("IMPORT_DATA_LIST");
                System.out.println("importList#%##^"+importList);
                if(importList!=null && importList.size()>0){
                    for(int i=0;i<importList.size();i++){
                        //importDataList = (ArrayList) importList.get(i);
                        accountTo = (DailyAccountTransTO) importList.get(i);                   
                        sqlMap.executeUpdate("insertDailyCollectionDetails", accountTo);
                    }                    
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            dailyAccountTransDAO dao = new dailyAccountTransDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("dailyAccountTransDAO Execute Method : " + map);
        HashMap returnMap = new HashMap();
        try{
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.AUTHORIZESTATUS)) {
            TransferDAO trasnDAO = new TransferDAO();
            returnDataMap = trasnDAO.execute(map);
            if(command.equals(CommonConstants.TOSTATUS_INSERT)){
               //Deleting entries...
                deleteData(map);
            }
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            System.out.println("ENTRY TO DELETE");
            deleteData(map);
        }else if (command.equals("TRANSACTION")) {
            
        }else if (command.equals("IMPORT")) {
            if (map.containsKey("IMPORT_DATA_LIST")) {
                insertImportData(map);
            } 
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnDataMap;
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void deleteData(HashMap deleteMap) throws Exception {
        try {
            sqlMap.startTransaction();
            if ((deleteMap.containsKey("COMMAND") && deleteMap.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE))||
                   deleteMap.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT) ) {
                sqlMap.executeUpdate("DeleteImportedData", deleteMap);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List lstData = ServerUtil.executeQuery("getSelectCashTransactionTODAILYCons", map);
        DailyAccountTransTO dailyTo;
        if(lstData!=null && lstData.size()>0){
            returnMap.put("DAILY_TRANS_DATA", lstData);
        }
        return returnMap;
    }
}
