/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSBankAdvanceDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication.mdsbankadvance;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
//import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.mdsapplication.mdsbankadvance.MDSBankAdvanceTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Suresh
 *
 *
 */
public class MDSBankAdvanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    private Date currDt = null;
    final String SCREEN = "TD";
    public List bankAdvanceList = null;
//    private HashMap TermLoanCloseCharge=new HashMap();
    private double totalAmt = 0.0;
    private String bankAdvID = "";
    HashMap execReturnMap = new HashMap();

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSBankAdvanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap transMap = new HashMap();
        return transMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in DAO: " + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        execReturnMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("$#$#$#$#$ : Command : " + command);
        if (map.containsKey("MDS_BANK_ADVANCE")) {
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);
                bankAdvanceList = (List) map.get("MDS_BANK_ADVANCE");
                System.out.println("@##$#$% bankAdvanceList #### :" + bankAdvanceList);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("inbside here!!!");
                    insertData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();

        }
        System.out.println(" @#@@############# execReturnMap :" + execReturnMap);
        return execReturnMap;
    }

    private String getBankAdvTransId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MDS_BANK_ADV_ID");
        String shgTransId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return shgTransId;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (bankAdvanceList != null && bankAdvanceList.size() > 0) {
                bankAdvID = getBankAdvTransId();
                execReturnMap.put("BANK_ADV_ID", bankAdvID);
                String schemeName = "";
                double bankAdvanceAmt = 0.0;
                schemeName = CommonUtil.convertObjToStr(map.get("SCHEME_NAME"));
                double bonusAmt = 0.0;
                if (map.containsKey("TOTAL_AMOUNT")) {
                    totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT")).doubleValue();
                }
                if (map.containsKey("BONUS_AMOUNT")) {
                    bonusAmt = CommonUtil.convertObjToDouble(map.get("BONUS_AMOUNT")).doubleValue();
                }
                bankAdvanceAmt = totalAmt - bonusAmt;
                System.out.println("@#$@#$@#$@#$bankAdvanceAmt:" + bankAdvanceAmt + " , bonusAmt:" + bonusAmt + " , totalAmt:" + totalAmt);
                TransactionTO transactionTO = new TransactionTO();
                HashMap txMap = new HashMap();
                HashMap transMap = new HashMap();
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));

                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setInitiatedBranch(BRANCH_ID);
                transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                HashMap applicationMap = new HashMap();
                applicationMap.put("SCHEME_NAME", map.get("SCHEME_NAME"));
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                }
                // Credit Insert Start
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                txMap = new HashMap();
                System.out.println("Transfer Started Credit MDS Receipt: ");
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                System.out.println("$#$$$#$#$# Prod Type GL " + applicationMap.get("RECEIPT_HEAD"));
                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "Receipt : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) { // Added by nithya on 18-08-2020 for KD-2162
                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                } 
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                System.out.println("txMap Debit : " + txMap + "totalAmt :" + totalAmt);
                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("BANK ADVANCE");
                transferTo.setLinkBatchId(bankAdvID);
                TxTransferTO.add(transferTo);
                // Credit Acc INSERT End

                // Debit Insert Start Bank Advance account head
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                System.out.println("Transfer Started debit Bank Advance: ");
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                System.out.println("$#$$$#$#$# Prod Type Debit GL advance :" + applicationMap.get("BANKING_REP_PAY_HEAD"));
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "Bank Advance : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) { // Added by nithya on 18-08-2020 for KD-2162
                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                } 
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                System.out.println("txMap Debit : " + txMap + "bankAdvanceAmt :" + bankAdvanceAmt);
                transferTo = transactionDAO.addTransferDebitLocal(txMap, bankAdvanceAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("BANK ADVANCE");
                transferTo.setLinkBatchId(bankAdvID);
                TxTransferTO.add(transferTo);
                // Debit Acc INSERT End

                // Debit Insert Start Bonus account head
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                System.out.println("Transfer Started debit Bonus: ");
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                System.out.println("$#$$$#$#$# Prod Type Debit GL advance :" + applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, "Bonus : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) { // Added by nithya on 18-08-2020 for KD-2162
                    txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                }
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                System.out.println("txMap Debit : " + txMap + "bonusAmt :" + bonusAmt);
                transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("BANK ADVANCE");
                transferTo.setLinkBatchId(bankAdvID);
                TxTransferTO.add(transferTo);
                // Debit Acc INSERT End

                transferDAO = new TransferDAO();
                HashMap transactionMap = new HashMap();
                transactionMap.put("TxTransferTO", TxTransferTO);
                transactionMap.put("MODE", map.get("COMMAND"));
                transactionMap.put("COMMAND", map.get("COMMAND"));
                transactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                transactionMap.put("LINK_BATCH_ID", bankAdvID);
                transactionMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("################ tansactionMap :" + transactionMap);
                transMap = transferDAO.execute(transactionMap, false);
                MDSBankAdvanceTO objMDSBankAdvanceTO = null;
                HashMap dataMap = null;
                for (int k = 0; k < bankAdvanceList.size(); k++) {
                    dataMap = new HashMap();
                    dataMap = (HashMap) bankAdvanceList.get(k);
                    dataMap.put("SCHEME_NAME", schemeName);
                    dataMap.put("SHG_TRANS_ID", bankAdvID);
                    dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                    dataMap.put("STATUS_DT", currDt);
                    dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                    System.out.println("############## dataMap " + dataMap);
                    objMDSBankAdvanceTO = new MDSBankAdvanceTO();
                    objMDSBankAdvanceTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objMDSBankAdvanceTO.setBankAdvId(bankAdvID);
                    objMDSBankAdvanceTO.setBankAdvDt(currDt);
                    objMDSBankAdvanceTO.setSchemeName(schemeName);
                    objMDSBankAdvanceTO.setChittalNo(CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                    objMDSBankAdvanceTO.setSubNo(CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    objMDSBankAdvanceTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                    objMDSBankAdvanceTO.setInstallmentNo(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST_NO")));
                    objMDSBankAdvanceTO.setInstallmentDt((Date) dataMap.get("INST_DATE"));
                    objMDSBankAdvanceTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")));
                    objMDSBankAdvanceTO.setBonusAmt(CommonUtil.convertObjToDouble(dataMap.get("BONUS")));
                    objMDSBankAdvanceTO.setRepaid("N");
                    objMDSBankAdvanceTO.setStatus(CommonConstants.STATUS_CREATED);
                    objMDSBankAdvanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    objMDSBankAdvanceTO.setStatusDt(currDt);
                    sqlMap.executeUpdate("insertMDSBankAdvanceTO", objMDSBankAdvanceTO);
                    objLogTO.setData(objMDSBankAdvanceTO.toString());
                    objLogDAO.addToLog(objLogTO);
                    dataMap.clear();
                    dataMap = null;
                    objMDSBankAdvanceTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        String linkBatchId = "";
        HashMap transferTransParam = new HashMap();
        transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
        transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
        transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
        linkBatchId = CommonUtil.convertObjToStr(map.get("BANK_ADV_ID"));
        transferTransParam.put("LINK_BATCH_ID", linkBatchId);
        System.out.println(transferTransParam + "  linkBatchId####" + linkBatchId);
        ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
        if (transferTransList != null) {
            String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
            System.out.println("###@@# batchId : " + batchId);
            HashMap transAuthMap = new HashMap();
            transAuthMap.put("BATCH_ID", batchId);
            transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
            transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
            transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
            transferDAO.execute(transferTransParam, false);
            System.out.println("#$$$$$$$$$$$$$ AuthorizeMap : " + AuthorizeMap);
            sqlMap.executeUpdate("authorizeMDSBankAdvanceDetails", AuthorizeMap);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        bankAdvanceList = null;
    }

    public static void main(String str[]) {
        try {
            MDSBankAdvanceDAO dao = new MDSBankAdvanceDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
