/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-07-2015
 */
package com.see.truetransact.serverside.payroll.pfInterestApplication;

import com.see.truetransact.serverside.directoryboardsetting.*;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.directoryboardsetting.DirectoryBoardTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.PayRollTo;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.sun.tools.javac.v8.tree.Tree;
import java.util.Date;
import java.util.logging.Level;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class PFInterestApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DirectoryBoardTO ObjDirectoryBoardTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(PFInterestApplicationDAO.class);
    private Date currDt = null;
    private String current_user;
    private TransactionDAO transactionDAO = null;
    /**
     * Creates a new instance of roleDAO
     */
    public PFInterestApplicationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getEmployeeDetailsForPF", map);
          ArrayList oneList = new ArrayList();
        for(int i=0;i<list.size();i++){
          
            HashMap each =(HashMap)list.get(i);
            if(each!=null && each.size()>0){
                String empId= CommonUtil.convertObjToStr(each.get("EMPLOYEEID"));
                 String empName= CommonUtil.convertObjToStr(each.get("EMPLOYEE_NAME"));
                 HashMap whereMap = new HashMap();
              whereMap.put("PF_INT_CALC", "PF_INT_CALC");
            // whereMap.put("FROM_ACC_NO", map.get("FROM_ACC_NO"));
          //   whereMap.put("TO_ACC_NO", map.get("FROM_ACC_NO"));
            // whereMap.put("FROM_DATE",  map.get("FROM_DATE"));
              if( each.get("INT_CALC_DT")!=null &&  !each.get("INT_CALC_DT").equals("")){
                whereMap.put("FROM_DATE",  each.get("INT_CALC_DT"));  
              }
              else{
                 whereMap.put("FROM_DATE",  map.get("FROM_DATE")); 
              }
             whereMap.put("TO_DATE",  map.get("TO_DATE"));
             whereMap.put("INT_RATE",  CommonUtil.convertObjToDouble(map.get("INT_RATE")));
             whereMap.put("EMP_ID",  empId);
             whereMap.put("PAY_CODE",CommonUtil.convertObjToStr(each.get("PAY_CODE")));
                System.out.println("whereMap -------- :"+whereMap);
                   List intlist = (List) sqlMap.executeQueryForList("getPFInterest", whereMap);
                   System.out.println("Intlist     :"+intlist);
                   if(intlist!=null && intlist.size()>0){
                       HashMap intmap = (HashMap)intlist.get(0);
                       HashMap tempmap = new HashMap();
                       tempmap.put("EMP_ID",  empId);
                       tempmap.put("EMP_NAME",empName);
                       tempmap.put("INTEREST",intmap.get("INTEREST"));
                       if (each.get("INT_CALC_DT") != null && !each.get("INT_CALC_DT").equals("")) {
                           tempmap.put("INT_CALC_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(each.get("INT_CALC_DT")))));
                       } else {
                           tempmap.put("INT_CALC_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("FROM_DATE")))));
                       }
                       oneList.add(tempmap);
                   }
            }
        }
        
        returnMap.put("INT_DETAILS", oneList);
        System.out.println("returnMap----------:"+returnMap);
        return returnMap;
    }
    private HashMap insertData(HashMap map) throws Exception {
        HashMap returnDataMap = new HashMap();
        try {
            sqlMap.startTransaction();
            ArrayList toObList = (ArrayList) map.get("INT_PROCESS_DETAILS");
            System.out.println("toObList >>>>>>>>>>>>>>> :::" + toObList);
            HashMap achdMap = new HashMap();
          //  achdMap.put("PAY_CODE", "PF");
            List achdLst = ServerUtil.executeQuery("getPFPayCode", achdMap);
            if (achdLst != null && achdLst.size() > 0) {
                achdMap = new HashMap();
                achdMap = (HashMap) achdLst.get(0);
            }
            ArrayList transferList = new ArrayList();
            double totAmt = 0;
            String payrollId = getPayRollID();
            for (int i = 0; i < toObList.size(); i++) {
                System.out.println("toObListXXXXXXXX :" + toObList.get(i));
                ArrayList single = (ArrayList) toObList.get(i);
                String empNo = CommonUtil.convertObjToStr(single.get(2));
                insertPayroll(single, map, payrollId,CommonUtil.convertObjToStr(achdMap.get("PAY_CODE")));
                HashMap toMap = new HashMap();
                toMap.put("EMP_ID", empNo);
                toMap.put("TRANS_AMT", single.get(3));
                totAmt += CommonUtil.convertObjToDouble(single.get(3));
                transferList.add(doTransaction(toMap, achdMap));
            }
            TransactionTO transactionTO = new TransactionTO();
            HashMap toMap = new HashMap();
            toMap.put("TRANS_AMT", totAmt);
            toMap.put("DR_ACC_HD", map.get("DR_ACHD"));
            transferList.add(doTransactionForDebit(toMap));
            System.out.println("transferList >>>> :" + transferList);
            if (transferList != null && transferList.size() > 0) {
                returnDataMap = doDebitCredit(transactionTO, transferList, _branchCode, true);
                System.out.println("returnDataMap@!!@#!@#!@#"+returnDataMap);                
                HashMap updateMap = new HashMap();
                updateMap.put("PAYROLLID", payrollId);
                updateMap.put("generateSingleTransId", CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
                sqlMap.executeUpdate("updatePayrollData", updateMap);
                returnDataMap.putAll(getTransDetails(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID"))));
            }
            returnDataMap.put("PAYROLL_ID",payrollId);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
       
        return returnDataMap;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateDirectoryBoardTO", ObjDirectoryBoardTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = ObjDirectoryBoardTO.getStatusBy();
            ObjDirectoryBoardTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteDirectoryBoardTO", ObjDirectoryBoardTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap--" + map);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        current_user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        logDAO = new LogDAO();
        logTO = new LogTO();
        
        
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            returnMap = insertData(map);
            
        }
        
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjDirectoryBoardTO = null;
        logTO = null;
        logDAO = null;
    }
    private void insertPayroll(ArrayList arList,HashMap map,String payrollId,String paycode) throws Exception {
        final PayRollTo payRollTo = new PayRollTo();
        payRollTo.setEmployeeId(CommonUtil.convertObjToStr(arList.get(2)));
        payRollTo.setMonth(currDt);
        payRollTo.setPayType(CommonConstants.GL_TRANSMODE_TYPE);
        payRollTo.setTransDt(currDt);
        payRollTo.setPayCode(paycode);
        payRollTo.setPayDesc("");
        payRollTo.setAmount((CommonUtil.convertObjToDouble(arList.get(3))));
        payRollTo.setProdType(CommonConstants.GL_TRANSMODE_TYPE);
        payRollTo.setSrlNo(CommonUtil.convertObjToInt("1"));
        payRollTo.setStatus("posted");
        payRollTo.setFromDt(CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(arList.get(4)))));
        payRollTo.setToDt(CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TO_DATE")))));
        payRollTo.setCreatedBy(current_user);
        payRollTo.setCreatedDt(currDt);
        payRollTo.setStatusBy(current_user);
        payRollTo.setAuthorizeBy(current_user);
        payRollTo.setAuthorizeStatus("Y");
        payRollTo.setTransType("CREDIT");
        payRollTo.setPayrollId(payrollId);
        sqlMap.executeUpdate("insertPayRollTo", payRollTo);

        final List listData;
        HashMap data = new HashMap();
        data.put("EMPLOYEEID", payRollTo.getEmployeeId());
        HashMap hash = new HashMap();
        listData = sqlMap.executeQueryForList("getPFEmployeeDetails", data);
        if (listData != null && listData.size() > 0) {
            hash = (HashMap) listData.get(0);
        }

        HashMap pfMap = new HashMap();
        pfMap.put("PF_NO", CommonUtil.convertObjToStr(hash.get(("PF_ACT_NO"))));
        pfMap.put("TRAN_DT", currDt.clone());
        pfMap.put("TRANS_ID", "");
        pfMap.put("BATCH_ID", "");
        pfMap.put("TRANS_MODE", "");
        pfMap.put("AMOUNT", payRollTo.getAmount());
        pfMap.put("PROD_TYPE", "C");
        pfMap.put("CREATED_BY", payRollTo.getCreatedBy());
        pfMap.put("CREATED_DATE", currDt.clone());
        pfMap.put("AUTHORIZED_BY", null);
        pfMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
        pfMap.put("BRANCHID", _branchCode);
        pfMap.put("STATUS", "posted");
        pfMap.put("REMARK", "");
        pfMap.put("PF_TRANS_TYPE", "PF");
        pfMap.put("TRANS_TYPE", "CREDIT");
        sqlMap.executeUpdate("InsertPfTransTo", pfMap);
        sqlMap.executeUpdate("updatepfmasterbalance", pfMap);

        HashMap updateMap = new HashMap();
        updateMap.put("employeeId", payRollTo.getEmployeeId());
        Date ltDt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TO_DATE"))), -1);
        updateMap.put("LAST_INT_CALC_DT", CommonUtil.getProperDate(currDt, ltDt));
        sqlMap.executeUpdate("updatePFPayMasterLastIntDate", updateMap);


    }private String getPayRollID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PAYROLL_ENTRY_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private TxTransferTO doTransaction(HashMap map, HashMap achdMap) {
        TransactionTO transactionTO = new TransactionTO();
         TransferTrans objTransferTrans = new TransferTrans();
        try {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            HashMap txMap = new HashMap();
            objTransferTrans.setInitiatedBranch(_branchCode);
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("ACC_HD")));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put("AUTHORIZEREMARKS", map.get("PAY_CODE_TYPE"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.PARTICULARS, "By" + "" + "PF");
            txMap.put("LINK_BATCH_ID", map.get("EMP_ID"));
            txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
           System.out.println("objTransferTrans---- crrrr :"+objTransferTrans);
           return  objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(map.get("TRANS_AMT")));
        } catch (ServiceLocatorException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private TxTransferTO doTransactionForDebit(HashMap map) {
        TransactionTO transactionTO = new TransactionTO();
        TransferTrans objTransferTrans = new TransferTrans();
        try {
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            HashMap txMap = new HashMap();
            objTransferTrans.setInitiatedBranch(_branchCode);
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(map.get("DR_ACC_HD")));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put("AUTHORIZEREMARKS", map.get("PAY_CODE_TYPE"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.PARTICULARS, "By" + "" + "PF");
            txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
            return objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(map.get("TRANS_AMT")));

        } catch (ServiceLocatorException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private HashMap doDebitCredit(TransactionTO transactionTO, ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
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
        HashMap returnDataMap = transferDAO.execute(data, false);
        System.out.println("returnDataMap -------- :" + returnDataMap);

        HashMap transferTransParam = new HashMap();
        TransferTrans objTrans = new TransferTrans();
        transferTransParam.put(CommonConstants.BRANCH_ID, branchCode);
        transferTransParam.put(CommonConstants.USER_ID, current_user);
        transferTransParam.put("BATCH_ID", returnDataMap.get("TRANS_ID"));
        transferTransParam.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");

        System.out.println("transferTransList###" + batchList.size() + "transferTransParam###" + transferTransParam);
        if (batchList.size() > 0) {
            objTrans.doTransferAuthorize(batchList, transferTransParam);
        }
        transactionTO.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setTransId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setStatus(CommonConstants.TOSTATUS_INSERT);
        transactionTO.setBranchId(_branchCode);

        transactionTO = null;
        return returnDataMap;
    }
    
    private HashMap getTransDetails(String batchId) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = null;        
        transList = (List) sqlMap.executeQueryForList("getTransferDetailsInvestment", getTransMap);       
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
        return returnMap;
    }
}
