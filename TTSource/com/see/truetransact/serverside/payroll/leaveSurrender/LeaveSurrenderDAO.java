/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-08-2015
 */
package com.see.truetransact.serverside.payroll.leaveSurrender;

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
import com.see.truetransact.transferobject.directorboardmeeting.DirectorBoardTO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.leavesurrender.LeaveSurrenderTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class LeaveSurrenderDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LeaveSurrenderTO ObjLeaveSurrenderTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(LeaveSurrenderDAO.class);
    private Date currDt = null;
    private String generateSingleTransId="";
    private TransactionDAO transactionDAO = null;
    private String transType ="";
    /**
     * Creates a new instance of roleDAO
     */
    public LeaveSurrenderDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getLeaveSurrenderTO", map);
        returnMap.put("LeaveSurrenderTO", list);
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(map.get("LEAVE_SURRENDER_ID")));
        List list1 = transactionDAO.getData(whereMap);
        returnMap.put("TransactionTO", list1);
        return returnMap;
    }

    private String getLeaveSurrenderNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LEAVE_SURRENDER_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private void insertTransactionDetails(HashMap map) throws Exception {
        System.out.println("map ---Leave- :" + map.get("TransactionTo"));
        HashMap editMap = new HashMap();
        HashMap transTomap = (HashMap) map.get("TransactionTo");
        System.out.println("transTomap  Leave:" + transTomap);
        TransactionTO objTransTo = new TransactionTO();
        if (transTomap != null && transTomap.size() > 0) {

            objTransTo = (TransactionTO) transTomap.get("1");
            System.out.println("objTransTo-- Leave:" + objTransTo);
        }
        generateSingleTransId = generateLinkID();
        HashMap acHeads = new HashMap();
        acHeads = (HashMap) sqlMap.executeQueryForObject("getPaycodeHead", new HashMap());
        System.out.println("acHeads " + acHeads);
        transType = objTransTo.getTransType();
        HashMap loanAuthTransMap = new HashMap();
        if (objTransTo != null && objTransTo.getTransType() != null && objTransTo.getTransType().equals("CASH")) {
            loanAuthTransMap.put("SELECTED_BRANCH_ID", _branchCode);
            loanAuthTransMap.put("BRANCH_CODE", _branchCode);
            loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACC_HD"));
            loanAuthTransMap.put("LIMIT", objTransTo.getTransAmt());//dataMap.get("LIMIT"));
            loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
            loanAuthTransMap.put("USER_ID", map.get("USER"));
            loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
            loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
            HashMap cashMap = new HashMap();
            if (objTransTo.getTransAmt() > 0) {
                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
            }
            cashMap.put("BRANCH_CODE", _branchCode);
        } else {
            TxTransferTO transferTo = new TxTransferTO();
            TransactionDAO transactionDAO = null;
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            ArrayList transList = new ArrayList();
            HashMap crMap = new HashMap();
            String oldAcctNo = "";
            crMap.put("ACT_NUM", objTransTo.getDebitAcctNo());
            if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                if (oaAcctHead != null && oaAcctHead.size() > 0) {
                    crMap = (HashMap) oaAcctHead.get(0);
                }
            } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                List oaAcctHead = sqlMap.executeQueryForList("getSAAccNoProdIdDet", crMap);
                if (oaAcctHead != null && oaAcctHead.size() > 0) {
                    crMap = (HashMap) oaAcctHead.get(0);
                }
            }else if (!objTransTo.getProductType().equals("") && objTransTo.getProductType().equals(TransactionFactory.ADVANCES)) {
                List oaAcctHead = sqlMap.executeQueryForList("getADAccNoProdIdDet", crMap);
                if (oaAcctHead != null && oaAcctHead.size() > 0) {
                    crMap = (HashMap) oaAcctHead.get(0);
                }

            }
            HashMap txMap = new HashMap();
            txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACC_HD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "VOUCHER");
            txMap.put(TransferTrans.PARTICULARS, "Leave Surrender - " + ObjLeaveSurrenderTO.getEmpId());
            txMap.put(TransferTrans.CR_ACT_NUM, objTransTo.getDebitAcctNo());
            txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
            txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put("INITIATED_BRANCH", _branchCode);
            transferTo = transactionDAO.addTransferDebitLocal(txMap, objTransTo.getTransAmt());
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setLinkBatchId(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            transferTo.setGlTransActNum(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            transList.add(transferTo);
            System.out.println("dataMap KKK K:" + txMap);
            if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
            } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
            }else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.ADVANCES)) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
            }

            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
            txMap.put("generateSingleTransId", generateSingleTransId);

            txMap.put(TransferTrans.PARTICULARS, "Leave Surrender - " + ObjLeaveSurrenderTO.getEmpId());
            txMap.put("generateSingleTransId", generateSingleTransId);
            if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.OPERATIVE)) {
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
            } else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.SUSPENSE)) {
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
            }else if (CommonUtil.convertObjToStr(objTransTo.getProductType()).equals(TransactionFactory.ADVANCES)) {
                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
            }
            txMap.put("INITIATED_BRANCH", _branchCode);
            transferTo = transactionDAO.addTransferCreditLocal(txMap, objTransTo.getTransAmt());
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER")));
            transferTo.setLinkBatchId(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            transferTo.setGlTransActNum(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            transList.add(transferTo);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.doTransferLocal(transList, _branchCode);
        }
        TransactionTO objTransactionTO = null;
        if (transTomap != null && transTomap.size() > 0) {
            objTransactionTO = (TransactionTO) transTomap.get("1");
            objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
            objTransactionTO.setBatchId(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            objTransactionTO.setBatchDt(currDt);
            objTransactionTO.setTransId(ObjLeaveSurrenderTO.getLeaveSurrenderId());
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
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

    private ArrayList loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        String particulars = "";
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
        objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
        objCashTO.setTransType(CommonConstants.DEBIT);
        objCashTO.setSingleTransId(generateSingleTransId);
        objCashTO.setParticulars(CommonUtil.convertObjToStr(ObjLeaveSurrenderTO.getEmpId()) + " :Leave Surrender");
        objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
        objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
        objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
        objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
        objCashTO.setLinkBatchId(ObjLeaveSurrenderTO.getLeaveSurrenderId());
        objCashTO.setCommand("INSERT");
        objCashTO.setScreenName("Leave Surrender");
        System.out.println("objCashTO 1st one:" + objCashTO);
        cashList.add(objCashTO);
        return cashList;
    }
    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjLeaveSurrenderTO.setLeaveSurrenderId(getLeaveSurrenderNo());
            System.out.println("%%%%%%%%%" + ObjLeaveSurrenderTO.getLeaveSurrenderId());
            insertTransactionDetails(map);
            sqlMap.executeUpdate("insertLeaveSurrenderTO", ObjLeaveSurrenderTO);
//            logTO.setData(ObjDirectorBoardTO.toString());
//            logTO.setPrimaryKey(ObjDirectorBoardTO.getKeyData());
//            logTO.setStatus(ObjDirectorBoardTO.getStatus());
//            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            System.out.println("%%%%%Amount in daooo" + ObjLeaveSurrenderTO.getLeaveAmt());
            sqlMap.executeUpdate("updateDirectorBoardTO", ObjLeaveSurrenderTO);
            //  logTO.setData(ObjDirectorBoardTO.toString());
            //  logTO.setPrimaryKey(ObjDirectorBoardTO.getKeyData());
            //  logTO.setStatus(ObjDirectorBoardTO.getStatus());
            //  logDAO.addToLog(logTO);
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
            String Statusby = ObjLeaveSurrenderTO.getStatusBy();
            ObjLeaveSurrenderTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteLeaveSurrenderTO", ObjLeaveSurrenderTO);
            // logTO.setData(ObjDirectorBoardTO.toString());
            ///  logTO.setPrimaryKey(ObjDirectorBoardTO.getKeyData());
            //  logTO.setStatus(ObjDirectorBoardTO.getStatus());
            //  logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { UserDAO dao = new
     * UserDAO(); HashMap map=new HashMap();
     * map.put(CommonConstants.MAP_NAME,"getSelectTerminalMasterTO");
     * map.put(CommonConstants.MAP_WHERE,"T0001042"); map=dao.executeQuery(map);
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        ObjLeaveSurrenderTO = (LeaveSurrenderTO) map.get("LeaveSurrenderTO");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("LEAVE_SURRENDER_ID", ObjLeaveSurrenderTO.getLeaveSurrenderId());
            returnMap.put("SINGLE_TRANS_ID", generateSingleTransId);
            returnMap.put("TRANS_TYPE", transType);
            transType ="";
            generateSingleTransId="";
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        }
        if(map.containsKey(CommonConstants.AUTHORIZEMAP)){
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            System.out.println("authMap --- :" + authMap);
            ArrayList alist = (ArrayList) authMap.get("AUTHORIZEDATA");
            System.out.println("alist-------- " + alist);
            String auth_status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            System.out.println("auth_status########h- :" + auth_status);
            HashMap authMappp = (HashMap) alist.get(0);
            String linkBatchId = CommonUtil.convertObjToStr(authMappp.get("LEAVE_SURRENDER_ID"));
            authMappp.put(CommonConstants.BRANCH_ID, _branchCode);
            authMappp.put(CommonConstants.USER_ID, map.get("USER"));
            authMappp.put("INITIATED_BRANCH", _branchCode);
            authMappp.put("LINK_BATCH_ID", linkBatchId);
            authMappp.put("TODAY_DT", currDt);
            HashMap cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
            cashAuthMap.put(CommonConstants.USER_ID,map.get("USER"));
            TransactionDAO transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.authorizeCashAndTransfer(linkBatchId, auth_status, cashAuthMap);
            sqlMap.executeUpdate("authorizeLeaveSurrender", authMappp);
        }

        map = null;
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        return getData(obj);
    }
    //  public HashMap execute(HashMap obj) throws Exception {
    //   }
    //   public HashMap executeQuery(HashMap obj) throws Exception {
    //  }
//    public HashMap execute(HashMap obj) throws Exception {
    // }
    //   public HashMap executeQuery(HashMap obj) throws Exception {
    //  }
//    private void destroyObjects() {
//        ObjDirectorBoardTO = null;
//        logTO = null;
//        logDAO = null;
//    }
//    
//    private void authorize(HashMap map) throws Exception {
//        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
//        HashMap AuthMap= new HashMap();
//        AuthMap= (HashMap) selectedList.get(0);
//        System.out.println("@@@@@@@AuthMap"+AuthMap);
//        try {
//            sqlMap.startTransaction();
//            logTO.setData(map.toString());
//            sqlMap.executeUpdate("authorizeEmpTransfer", AuthMap);
//            if(AuthMap.get("STATUS").equals("AUTHORIZED")){
//              sqlMap.executeUpdate("updatePresentBranch", AuthMap);  
//            }
//            sqlMap.commitTransaction();
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
//    }
}
