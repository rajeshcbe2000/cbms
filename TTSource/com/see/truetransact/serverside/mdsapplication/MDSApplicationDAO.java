/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSApplicationDAO.java
 *
 * Created on June 18, 2011, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Sathiya
 *
 * @modified Pinky @modified Rahul
 */
public class MDSApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private MDSApplicationTO mdsApplicationTO = null;
    private MDSMasterMaintenanceTO MaintenanceTO;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    HashMap execReturnMap = new HashMap();
    private Date currDt = null;
    final String SCREEN = "TD";
    private SMSSubscriptionTO objSMSSubscriptionTO = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSApplicationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
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

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("########" + map);
        HashMap returnMap = new HashMap();
        objNomineeDAO = new NomineeDAO();
//        String where = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
//        List list = (List) sqlMap.executeQueryForList("getSelectMDSApplicationTO", where);
        if (map.get("TRANS_ID").toString().equals("")) {
            map.put("TRANS_ID", null);
        }
        List list = (List) sqlMap.executeQueryForList("getSelectMDSApplicationTO", map);
        returnMap.put("mdsApplicationTO", list);
        System.out.println("list...." + list);
        HashMap whereMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        String transId = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
        HashMap getTransMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(_branchCode);

        getTransMap.put("TRANS_ID", transId);
        if (list != null) {
            Date currDt1 = null;
            MDSApplicationTO to = new MDSApplicationTO();
            to = (MDSApplicationTO) list.get(0);
            System.out.println("to..." + to);
            currDt1 = new java.sql.Date(getProperDateFormat(to.getStatusDt()).getTime());//changed by jithin
            getTransMap.put("TRANS_DT", getProperDateFormat(to.getStatusDt()));//currDt1);
        }
        // getTransMap.put("TRANS_DT", currDt);
        System.out.println("nnnnsssss" + getTransMap);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put(CommonConstants.MAP_WHERE, getTransMap);
        list = transactionDAO.getData(whereMap);
        returnMap.put("TransactionTO", list);
        map.put("DEPOSIT_NO", map.get("CHITTAL_NO") + "_" + map.get("SUB_NO"));
        list = (List) sqlMap.executeQueryForList("getSelectRenewNomineeTOTD", map);
        if (list != null && list.size() > 0) {
            returnMap.put("AccountNomineeList", list);
        }
        HashMap editRemitMap = new HashMap();
        HashMap editMap = new HashMap();
        editMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        editMap.put("TRANS_DT", currDt.clone());
        editMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
        editRemitMap.put("TRANS_ID", CommonUtil.convertObjToStr(map.get("TRANS_ID")));
        List lstRemit = sqlMap.executeQueryForList("getSelectRemitTransactionAmt", editMap);
        if (lstRemit != null && lstRemit.size() > 0) {
            editRemitMap = (HashMap) lstRemit.get(0);
            List lstEdit = sqlMap.executeQueryForList("getSelectApplnAmt", editMap);
            if (lstEdit != null && lstEdit.size() > 0) {
                editMap = (HashMap) lstEdit.get(0);
                double amount = CommonUtil.convertObjToDouble(editMap.get("INST_AMT")).doubleValue();
                getTransMap = new HashMap();
                String debitAcNo = CommonUtil.convertObjToStr(editRemitMap.get("DEBIT_ACCT_NO"));
                String prodType = CommonUtil.convertObjToStr(editRemitMap.get("PRODUCT_TYPE"));
                String prodId = CommonUtil.convertObjToStr(editRemitMap.get("PROD_ID"));
                if (!editRemitMap.get("TRANS_TYPE").equals("") && editRemitMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    if (!prodType.equals("") && prodType.equals("GL") && prodId.equals("")) {
                        getTransMap.put("LINK_BATCH_ID", editRemitMap.get("BATCH_ID"));
                    } else {
                        getTransMap.put("LINK_BATCH_ID", editRemitMap.get("DEBIT_ACCT_NO"));
                    }
                } else {
                    getTransMap.put("LINK_BATCH_ID", editRemitMap.get("BATCH_ID"));
                }
                getTransMap.put("TODAY_DT", currDt.clone());
                getTransMap.put("INITIATED_BRANCH", _branchCode);
                System.out.println("########getTransferTransBatchID returnMap depCloseAmt:" + returnMap);
                if (amount > 0) {
                    getTransMap.put("AMOUNT", new Double(amount));
                    List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("AMT_TRANSACTION", lst.get(0));
                        System.out.println("AMT_TRANSACTION TRANSFER :" + returnMap);
                    }
                    lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                    if (lst != null && lst.size() > 0) {
                        returnMap.put("AMT_TRANSACTION", lst.get(0));
                        System.out.println("AMT_TRANSACTION CASH :" + returnMap);
                    }
                    lst = null;
                }
            }
        }
        HashMap smssubMap = new HashMap();
        MDSApplicationTO applicationTo  = new MDSApplicationTO();
        applicationTo = (MDSApplicationTO)  ((List) returnMap.get("mdsApplicationTO")).get(0);
        smssubMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        smssubMap.put("PROD_ID", applicationTo.getSchemeName());
        smssubMap.put("ACT_NUM", applicationTo.getChittalNo());
        list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smssubMap);
        if (list != null && list.size() > 0) {
            returnMap.put("SMSSubscriptionTO", list);
        }
        list = null;
                
        System.out.println("returnMap : " + returnMap);
        return returnMap;
    }

    private void updateChittalNo() throws Exception {
        HashMap where = new HashMap();
        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        int len = 11;
        double nofOfMember = 0;
        int suffix = 0;
        int lastValue = 0;  //AJITH Changed from double
        String genID = "";
        String value = CommonUtil.convertObjToStr(mdsApplicationTO.getSchemeName());
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
//        HashMap deletedStatusMap = new HashMap();
//        deletedStatusMap.put("SCHEME_NAME",mdsApplicationTO.getSchemeName());
//        List lst = sqlMap.executeQueryForList("getSelectDeletedRecordNo", deletedStatusMap);
//        if(lst!=null && lst.size()>0){
//            deletedStatusMap = (HashMap)lst.get(0);
//            int minChitNo = CommonUtil.convertObjToInt(deletedStatusMap.get("MIN_CHIT_NO"));
//            lst = (List) sqlMap.executeQueryForList("getChittalNO", value);
//            if(lst != null && lst.size() > 0){
//                mapData = (HashMap)lst.get(0);
//            }
//            if (mapData.containsKey("PREFIX")) {
//                strPrefix = (String) mapData.get("PREFIX");
//            }
//            suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//            double nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//            nofOfMember = CommonUtil.convertObjToDouble(mapData.get("NO_OF_MEMBER_PER_DIVISION")).doubleValue();
//            lastValue = CommonUtil.convertObjToDouble(mapData.get("LAST_VALUE")).doubleValue();
//            int numFrom = strPrefix.trim().length();
//            if(minChitNo == 1 && !deletedStatusMap.get("STATUS").equals("DELETED") && !deletedStatusMap.get("AUTHORIZE_STATUS").equals("REJECTED") &&
//            !deletedStatusMap.get("AUTHORIZE_STATUS").equals("DELETED")){
//                String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//                String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE")))+1);
//                genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//                where.put("SCHEME_NAME",value);
//                where.put("VALUE", nxtID);
//                where.put("SUFFIX", String.valueOf(mdsApplicationTO.getDivisionNo()));
//                System.out.println("####"+where);
//                sqlMap.executeUpdate("updateChittalNxtNum", where);
//            }else{
//                mdsApplicationTO.setChitNo(new Double(minChitNo));
//                String minChit = String.valueOf(Integer.parseInt(String.valueOf(deletedStatusMap.get("MIN_CHIT_NO"))));
//                genID = strPrefix.toUpperCase() + CommonUtil.lpad(minChit, len - numFrom, '0');
//                HashMap usedMap = new HashMap();
//                usedMap.put("DELETED_USED","Y");
//                usedMap.put("CHITTAL_NO",genID);
//                where.put("SUFFIX", String.valueOf(suffix));
//                System.out.println("####"+where);
//                sqlMap.executeUpdate("updateUsedChittalNum", usedMap);
//            }
//        }else{
        List lst = (List) sqlMap.executeQueryForList("getChittalNO", value);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
        }
        if (mapData.containsKey("PREFIX")) {
            strPrefix = (String) mapData.get("PREFIX");
        }
        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
        double nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
        nofOfMember = CommonUtil.convertObjToDouble(mapData.get("NO_OF_MEMBER_PER_DIVISION")).doubleValue();
        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE")); //AJITH
        int numFrom = strPrefix.trim().length();
        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
        //String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1); //AJITH
        int nxtID = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE")) + 1;  //AJITH
        genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
        mdsApplicationTO.setChitNo(lastValue);
        where.put("SCHEME_NAME", value);
        where.put("VALUE", nxtID);
        where.put("SUFFIX", mdsApplicationTO.getDivisionNo());  //AJITH Changed from String.valueOf(mdsApplicationTO.getDivisionNo())
        System.out.println("####" + where);
        sqlMap.executeUpdate("updateChittalNxtNum", where);
//        }
//        return genID;
    }

   public void commonTransactionCashandTransfer(HashMap map, MDSApplicationTO mdsApplicationTo,String generateSingleTransId, HashMap applicationMap) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("mdsApplicationTo####" + mdsApplicationTo + map);
        ArrayList bonusList = null;
        ArrayList forfeiteBonusList = null;
        double forFeitedAmt = 0.0;
        HashMap graceForFeiteMap = null;        
        double forfeiteBonusAmount = 0.0;
        double customerBonus = 0.0;        
        System.out.println("AppLICATION MAp" + applicationMap);
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(mdsApplicationTo.getBranchCode());
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        System.out.println("mdsCurrentReceiptEntryTO.getBonusAmtPayable()" + mdsApplicationTo.getInstAmt());
        if (CommonUtil.convertObjToDouble(mdsApplicationTo.getInstAmt()) > 0 && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0) {
            System.out.println("Bonus Started");
            //Debit
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");      
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, mdsApplicationTo.getBranchCode());//BRANCH_ID);            
            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsApplicationTo.getChittalNo() + "_" + mdsApplicationTo.getSubNo());            
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put("TRANS_MOD_TYPE", "MDS");
//            System.out.println("txMap : " + txMap+"serviceAmt :"+mdsCurrentReceiptEntryTO.getBonusAmtPayable());
            System.out.println("First TXTMAP" + txMap);
            double totalAmount = CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"));
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(totalAmount));
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsApplicationTo.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsApplicationTo.getBranchCode());//bb1 BRANCH_ID);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsApplicationTo.getChittalNo()));
            transferTo.setGlTransActNum(mdsApplicationTo.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
            System.out.println("transferTo List 6 : " + transferTo);        
            
            //Credit                
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, mdsApplicationTo.getBranchCode());//BRANCH_ID);
            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + mdsApplicationTo.getChittalNo() + "_" + mdsApplicationTo.getSubNo());            
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            System.out.println("Fifth TXTMAP" + txMap + "   " + totalAmount);
            System.out.println("Before " + transferTo);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmount);
            System.out.println("Before " + transferTo);
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(mdsApplicationTo.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(mdsApplicationTo.getBranchCode());
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsApplicationTo.getChittalNo()));                    
            transferTo.setGlTransActNum(mdsApplicationTo.getChittalNo());
            transferTo.setSingleTransId(generateSingleTransId);
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            TxTransferTO.add(transferTo);
            }
            System.out.println("transferTo List 8 in bonus : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
            System.out.println("transferDAO List Last : " + map);
            HashMap transMap = transferDAO.execute(map, false);
            System.out.println("transferDAO List transMap : " + transMap); 
            HashMap linkBatchMap = new HashMap();
            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("TRANS_DT", currDt);
            linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
            execReturnMap.put("BONUS_TRANS_ID", transMap.get("TRANS_ID"));
            linkBatchMap = null;
            transMap = null;
           
}
        
    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            String generateSingleCashId = "";
            String generateSingleTransId = generateLinkID();
            mdsApplicationTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            mdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
//            mdsApplicationTO.setChittalNo(getChittalNo());
//            updateChittalNo();
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
//            double totalAmt = CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (mdsApplicationTO.getIsTran().equals("Y")) {
                if (map.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    double totalAmt = 0.0;
                    totalAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
                    HashMap applicationMap = new HashMap();
                    applicationMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                    if (lst != null && lst.size() > 0) {
                        applicationMap = (HashMap) lst.get(0);                        
                    }
                    if (transactionTO.getTransType().equals("TRANSFER")) {
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                        HashMap debitMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                            debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }
                        }
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                            debitMap.put("prodId", transactionTO.getProductId());
                            lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }
                        }

                        if (totalAmt > 0) {
                            txMap = new HashMap();
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
                            }  else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                                txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
                            }
                            HashMap susMap = new HashMap();                // Suspense Acc Head
                            susMap.put("PROD_ID", transactionTO.getProductId());
                            List susLst = sqlMap.executeQueryForList("getAccountHeadProdSA", susMap);
                            if (susLst != null && susLst.size() > 0) {
                                susMap = (HashMap) susLst.get(0);
                                if (transactionTO.getProductType() != null && !transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(susMap.get("AC_HEAD")));
                                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
                                }
                            }
                            //Babu commented for the mantis id : 7509
                            //txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            //txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());//commented by sreekrishnan and added lines in line no.319 and no.313
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("DR_INST_TYPE", "VOUCHER");
                            txMap.put(TransferTrans.PARTICULARS, mdsApplicationTO.getSchemeName() + "-" + CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()
                                    + "_" + mdsApplicationTO.getSubNo()));
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
                            System.out.println("txMap : " + txMap + "totalAmt :" + totalAmt);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            }
                            System.out.println("transferTo List 1 : " + transferTo);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            if (mdsApplicationTO.getThalayal().equals("Y")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                            } else if (mdsApplicationTO.getMunnal().equals("Y")) {
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                            } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")){
                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                            } else {
                                if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                                } else {
                                    HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                                    suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
                                    List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                                    if (suspenseLst != null && suspenseLst.size() > 0) {
                                        suspenseMap = (HashMap) suspenseLst.get(0);
                                        txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                        txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
                                    }
                                }
                            }
                            txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            System.out.println("txMap : " + txMap + "serviceAmt :" + totalAmt);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            
                            if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
                                transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
                            }
                            System.out.println("transferTo List 2 : " + transferTo);
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", mdsApplicationTO.getCommand());
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                       // map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
                        HashMap transMap = transferDAO.execute(map, false);
                        mdsApplicationTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        System.out.println("transMap AFTER : " + transMap + "totalAmt :" + totalAmt);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transactionDAO.setBatchId(mdsApplicationTO.getTransId());
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);
//                    HashMap remitMap = new HashMap();
//                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                    if(lst!=null && lst.size()>0){
//                        remitMap = (HashMap)lst.get(0);
//                        if(!map.containsKey("LOCKER_SURRENDER_DAO")){
//                            remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                        }else{
//                            remitMap.put("BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
//                        }
//                        remitMap.put("BATCH_DT", currDt);
//                        System.out.println("remitMap : " + remitMap);
//                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
//                    }
                        HashMap linkBatchMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));//changed By Nidhin transactionTO.getDebitAcctNo()
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                        //Changed by sreekrishnan
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")){
                            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                        }
                        linkBatchMap = null;
                        transMap = null;
                        //Added by sreekrishnan
                        //if (map.containsKey("PREDEFINED_INSTALL_BONUS") && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0){
                       //     commonTransactionCashandTransfer(map,mdsApplicationTO,generateSingleTransId,applicationMap);
                        //}
                    } else if (transactionTO.getTransType().equals("CASH")) {
                        generateSingleCashId = generateLinkID();
                        transactionTO.setChequeNo("SERVICE_TAX");
                        applicationMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                        applicationMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
                        applicationMap.put("CHIT_NO", mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo());
                        applicationMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                        applicationMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        ArrayList applncashList = applicationList(map,applicationMap, String.valueOf(totalAmt));
                        applicationMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        applicationMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                        //applicationMap.put("TRANS_MOD_TYPE", "MDS");
                        HashMap cashMap = cashTransactionDAO.execute(applicationMap, false);
                        System.out.println("cashMap :" + cashMap);
                        mdsApplicationTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        String command = CommonUtil.convertObjToStr(mdsApplicationTO.getCommand());
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(mdsApplicationTO.getStatusDt());
                        map.put("MODE", "INSERT");
                        transactionDAO.setBatchId(mdsApplicationTO.getTransId());
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);

//                    HashMap debitMap = new HashMap();
//                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
//                    if(lst!=null && lst.size()>0){
//                        debitMap = (HashMap)lst.get(0);
//                        if(!applicationMap.containsKey("LOCKER_SURRENDER_DAO"))
//                            debitMap.put("BATCH_ID", cashMap.get("TRANS_ID"));
//                        else
//                            debitMap.put("BATCH_ID", applicationMap.get("LOCKER_SURRENDER_ID"));
//                        debitMap.put("BATCH_DT", currDt);
//                        debitMap.put("TRANS_DT",currDt);
//                        debitMap.put("INITIATED_BRANCH",map.get("BRANCH_CODE"));
//                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", debitMap);
//                        debitMap = null;
//                        
//                        
//                        
//                    }
                        HashMap linkBatchMap = new HashMap();
                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
                        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
                        linkBatchMap = null;
                        //Added by sreekrishnan
                        //if (map.containsKey("PREDEFINED_INSTALL_BONUS") && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0){
                        //    commonTransactionCashandTransfer(map,mdsApplicationTO,generateSingleCashId,applicationMap);
                        //}
                    }
                }                
            }
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            if (mdsApplicationTO.getNominee().equals("Y")) {
                ArrayList nomineeTOList = new ArrayList();
                if (map.containsKey("AccountNomineeTO")) {
                    nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
                }
                ArrayList nomineeDeleteList = new ArrayList();
                if (map.containsKey("AccountNomineeDeleteTO")) {
                    nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
                }
                if (nomineeTOList != null && nomineeTOList.size() > 0) {
                    NomineeDAO objNomineeDAO = new NomineeDAO();
                    //                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
                    objNomineeDAO.insertData(nomineeTOList, mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
                    if (nomineeDeleteList != null) {
                        objNomineeDAO.insertData(nomineeDeleteList, mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo(), true, USERID, "DEPOIST", objLogTO, objLogDAO);
                    }
                }
            }

            if (mdsApplicationTO.getIsTran().equals("N")) {
                mdsApplicationTO.setTransId("");
                sqlMap.executeUpdate("insertApplicationTOForNo", mdsApplicationTO);


//                  System.out.println("@$#$@$%@$#masterMap :" +);                     // INSERT MASTER MAINTANANCE DETAILS
//                List lst = ServerUtil.executeQuery("getApplDetails",masterMap);
//                if(lst!=null && lst.size()>0){
//                   HashMap masterMap = (HashMap)lst.get(0);
                MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
                MaintenanceTO.setSchemeName(mdsApplicationTO.getSchemeName());
                MaintenanceTO.setChittalNo(mdsApplicationTO.getChittalNo());
                MaintenanceTO.setSubNo(mdsApplicationTO.getSubNo());    //AJITH
                MaintenanceTO.setDivisionNo(mdsApplicationTO.getDivisionNo());  //AJITH
                MaintenanceTO.setChitStartDt(mdsApplicationTO.getChitStartDt());
                MaintenanceTO.setMemberNo(mdsApplicationTO.getMembershipNo());
                MaintenanceTO.setMemberName(mdsApplicationTO.getMembershipName());
                MaintenanceTO.setNominee(mdsApplicationTO.getNominee());
                //MaintenanceTO.setNomineeName(mdsApplicationTO.getN);
                MaintenanceTO.setMemberType(mdsApplicationTO.getMembershipType());
                MaintenanceTO.setSalaryRecovery(mdsApplicationTO.getSalaryRecovery());
                MaintenanceTO.setBranchCode(mdsApplicationTO.getBranchCode());
                MaintenanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                MaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                MaintenanceTO.setStatusDt(currDt);
                MaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                MaintenanceTO.setAuthorizedBy((String) map.get(CommonConstants.USER_ID));
                MaintenanceTO.setAuthorizedDt(currDt);
                MaintenanceTO.setBondSet("N");
                MaintenanceTO.setApplicationSet("N");
//                    MaintenanceTO.setBondNo(getChittalBondNO());
                sqlMap.executeUpdate("insertMDSMasterMaintenanceTO", MaintenanceTO);
//                }





            } else {
                sqlMap.executeUpdate("insertApplicationTO", mdsApplicationTO);
            }
            //Changed By Suresh
            if (mdsApplicationTO.getCoChittal().equals("N")) {
                updateChittalNo();
            } else {
                UpdateCoChittalNo(map);
            }
            //Added by sreekrishnan
            if (objSMSSubscriptionTO != null) {
                System.out.println("objSMSSubscriptionTO%$%$%$%$%$%%"+objSMSSubscriptionTO);
                updateSMSSubscription(mdsApplicationTO);
            }
            
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateSMSSubscription(MDSApplicationTO mdsApplicationTO) throws Exception {
        objSMSSubscriptionTO.setStatusBy(mdsApplicationTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(getProperDateFormat(mdsApplicationTO.getStatusDt()));
        objSMSSubscriptionTO.setCreatedBy(mdsApplicationTO.getStatusBy());
        objSMSSubscriptionTO.setCustId(mdsApplicationTO.getCudt_id());
        if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() > 0 && objSMSSubscriptionTO.getSubscriptionDt() != null) {
            List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
            if (list != null && list.size() > 0) {
                sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
            } else {
                sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
            }
            if(mdsApplicationTO.getCommand().equals("UPDATE")){
                HashMap smsMap = new HashMap();
                smsMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
                smsMap.put("PROD_ID", objSMSSubscriptionTO.getProdId());
                smsMap.put("ACCOUNTNO", objSMSSubscriptionTO.getActNum());
                smsMap.put(CommonConstants.USER_ID, mdsApplicationTO.getStatusBy());
                smsMap.put("STATUS", "AUTHORIZED");
                smsMap.put(CommonConstants.AUTHORIZEDT, getProperDateFormat(currDt.clone()));
                System.out.println("authMap$#$#%#%#%#%"+smsMap);
                sqlMap.executeUpdate("authorizeSMSSubscriptionMap", smsMap);
            }
        }
    }
    
    private void deleteSMSSubscription(MDSApplicationTO mdsApplicationTO) throws Exception {
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        checkMap.put("PROD_ID", mdsApplicationTO.getSchemeName());
        checkMap.put("ACT_NUM", mdsApplicationTO.getChittalNo());
        checkMap.put("STATUS_DT", getProperDateFormat(mdsApplicationTO.getStatusDt()));
        checkMap.put(CommonConstants.USER_ID, mdsApplicationTO.getStatusBy());
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", checkMap);
        checkMap.clear();
        checkMap = null;
    }
    
     private void authorizeSMSSubscription(HashMap map) throws Exception {        
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        authMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        authMap.put("PROD_ID", mdsApplicationTO.getSchemeName());
        authMap.put("ACCOUNTNO", mdsApplicationTO.getChittalNo());
        authMap.put(CommonConstants.USER_ID, mdsApplicationTO.getStatusBy());
        authMap.put("STATUS", authMap.get("AUTHORIZESTATUS"));
        authMap.put(CommonConstants.AUTHORIZEDT, getProperDateFormat(currDt));
        System.out.println("authMap$#$#%#%#%#%"+authMap);
        sqlMap.executeUpdate("authorizeSMSSubscriptionMap", authMap);
        authMap.clear();
        authMap = null;
    }
//    private void getTransDetails (String batchId) throws Exception {
//        HashMap getTransMap = new HashMap();
//        getTransMap.put("BATCH_ID", batchId);
//        getTransMap.put("TRANS_DT", currDt);
//        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
//        execReturnMap = new HashMap();
//        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
//        if(transList!=null && transList.size()>0){
//            execReturnMap.put("TRANSFER_TRANS_LIST",transList);
//        }
//        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
//        if(cashList!=null && cashList.size()>0){
//            execReturnMap.put("CASH_TRANS_LIST",cashList);
//        }
//        getTransMap.clear();
//        getTransMap = null;
//        transList = null;
//        cashList = null;
//    }
    private ArrayList applicationList(HashMap map,HashMap applnMap, String totalAmt) throws Exception {
        System.out.println("applnMap :" + applnMap);
        ArrayList cashList = new ArrayList();
        if (mdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            if (CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                objCashTO.setProdType("GL");
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_HEAD")));
            } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")){
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("RECEIPT_HEAD")));
            }else {
                HashMap suspenseMap = new HashMap();                // Suspense Acc Head
                suspenseMap.put("PROD_ID", applnMap.get("SUSPENSE_PROD_ID"));
                List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
                if (suspenseLst != null && suspenseLst.size() > 0) {
                    suspenseMap = (HashMap) suspenseLst.get(0);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(suspenseMap.get("AC_HEAD")));
                    objCashTO.setProdType(TransactionFactory.SUSPENSE);
                    objCashTO.setProdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_PROD_ID")));
                    objCashTO.setActNum(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_ACC_NO")));
                }
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(totalAmt));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(totalAmt));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
            objCashTO.setStatusDt(mdsApplicationTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            //objCashTO.setParticulars(CommonUtil.convertObjToStr(applnMap.get("SCHEME_NAME")) + "-" + CommonUtil.convertObjToStr(applnMap.get("CHIT_NO")));
            objCashTO.setParticulars(CommonUtil.convertObjToStr(applnMap.get("CHIT_NO"))); // Removed scheme name from particulars by nithya on 03-08-2016 for 4983
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
            objCashTO.setCommand(mdsApplicationTO.getCommand());
            objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            objCashTO = null;
        }
        return cashList;
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("objCashTO 1st one------<>" + chargesMap);
        MDSApplicationTO mdsAppTO = new MDSApplicationTO();
        String USERID = CommonUtil.convertObjToStr(chargesMap.get(CommonConstants.USER_ID));
        ArrayList nomineeTOList = (ArrayList) chargesMap.get("AccountNomineeTO");
        ArrayList nomineeDeleteList = (ArrayList) chargesMap.get("AccountNomineeDeleteTO");
        
        //Updating sms subcriprion
        System.out.println("objSMSSubscriptionTO%#%#%#%"+objSMSSubscriptionTO);
        if (chargesMap.containsKey("SMSSubscriptionTO")) {
               updateSMSSubscription(mdsApplicationTO);
//        }else{            
//              deleteSMSSubscription(mdsApplicationTO);
        }
        
        // Update the data regarding the NomineeTable...
        if (nomineeTOList != null) {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            if (nomineeTOList.size() > 0) {
                System.out.println("Nominee List " + nomineeTOList);
                objNomineeDAO.deleteData(mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo(), SCREEN);
                objNomineeDAO.insertData(nomineeTOList, mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
            }
            if (nomineeDeleteList != null) {
                objNomineeDAO.insertData(nomineeDeleteList, mdsApplicationTO.getChittalNo() + "_" + mdsApplicationTO.getSubNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
            }
        }
        if(chargesMap.containsKey("NO_TRANSACTION")){
            HashMap dataMap = new HashMap();
            dataMap.put("MEMBER_TYPE", mdsApplicationTO.getMembershipType());
            dataMap.put("MEMBER_NO", mdsApplicationTO.getMembershipNo());
            dataMap.put("MEMBER_NAME", mdsApplicationTO.getMembershipName());
            dataMap.put("CUST_ID", mdsApplicationTO.getCudt_id());
            dataMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
            mdsApplicationTO.setMembershipName(mdsApplicationTO.getMembershipName());
            mdsApplicationTO.setMembershipNo(mdsApplicationTO.getMembershipNo());
            mdsApplicationTO.setMembershipType(mdsApplicationTO.getMembershipType());
            mdsApplicationTO.setChittalNo(mdsApplicationTO.getChittalNo());
            mdsApplicationTO.setCudt_id(mdsApplicationTO.getCudt_id());
            //sqlMap.executeUpdate("updateMDSApplication", dataMap);
        }
        HashMap statusMap = new HashMap();
        String status = "";
        statusMap.put("CHITTAL_NO",CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
        List stsList = sqlMap.executeQueryForList("getStatusMDSApp", statusMap);
        if(stsList != null && stsList.size() > 0){
        status = CommonUtil.convertObjToStr(((HashMap) stsList.get(0)).get("STATUS"));
        }
        statusMap.put("TRANS_ID", null);
        List mdsAppsList = sqlMap.executeQueryForList("getSelectMDSApplicationTO", statusMap);
        System.out.println("getSelectMDSApplicationTOmdsAppsList"+mdsAppsList);
        if(mdsAppsList != null && mdsAppsList.size() >0){
            mdsAppTO = (MDSApplicationTO)mdsAppsList.get(0);
        }
        if (status.equals(CommonConstants.STATUS_CREATED) && mdsAppTO.getAuthorizeStatus() != null) {
            if(mdsAppTO.getAuthorizeStatus().equals(CommonConstants.STATUS_AUTHORIZED)){
            mdsApplicationTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            mdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        }else{
            mdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        mdsApplicationTO.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get(CommonConstants.USER_ID)));
        //mdsApplicationTO.setAuthorizeStatus("");
        System.out.println("mdsApplicationTOnnn :" + mdsApplicationTO);
        sqlMap.executeUpdate("updateApplicationTO", mdsApplicationTO);
        sqlMap.executeUpdate("updateMdsMasterMaintenance", mdsApplicationTO);
        sqlMap.executeUpdate("updateMdsPrizedMoneyDetails", mdsApplicationTO);
        sqlMap.executeUpdate("updateMdsMemberReceiptEntry", mdsApplicationTO);
        sqlMap.executeUpdate("updateMdsReceiptEntry", mdsApplicationTO);
        if(!chargesMap.containsKey("NO_TRANSACTION")){
            HashMap a = new HashMap();
            a.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
            List list = (List) sqlMap.executeQueryForList("getSelectMDSApplicationTO", a);
            System.out.println("mdsApplsadasdicationTO" + list);

            HashMap schNamMap = new HashMap();
            schNamMap.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
            List isTranList = sqlMap.executeQueryForList("getSelIsTran", schNamMap);
            if (isTranList != null && isTranList.size() > 0) {
                schNamMap = new HashMap();
                schNamMap = (HashMap) isTranList.get(0);
                String isTran = schNamMap.get("TRANS_FIRST_INSTALLMENT").toString();
                System.out.println("isTranisTranisTranisTran" + isTran);
                if (isTran.equals("Y")) {
                    // System.out.println("charges Map"+chargesMap);
                    if (chargesMap.containsKey("TransactionTO")) {
                        HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                        if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            if (transactionDetailsMap.get("NOT_DELETED_TRANS_TOs") != null) {
                                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            }
                        }
                        //   System.out.println("allowedTransDetailsTO"+allowedTransDetailsTO.size());
                        if (allowedTransDetailsTO.size() > 0) {
                            TransactionTO transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            String batchid = transactionTO.getBatchId();
                            String transdid = transactionTO.getTransId();
                            HashMap amap = new HashMap();
                            amap.put("BATCH_ID", batchid);
                            amap.put("TRANS_ID", transdid);
                            amap.put("CURRDATE", mdsApplicationTO.getStatusDt());
                            amap.put("BRANCH_CODE", chargesMap.get("BRANCH_CODE"));
                            sqlMap.executeUpdate("updateRemitIssueTransAppl", amap);
                        }
                    }
                }   
            }
        }
        //  getSelectMDSApplicationTO  
    }

    private ArrayList applicationUpdate(HashMap chargesMap) throws Exception {//updating Transaction
        System.out.println("chargesMap :" + chargesMap);
        ArrayList cashList = new ArrayList();
        //        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        //        objChargesServiceTaxTO = (ChargesServiceTaxTO)chargesMap.get("ChargesServiceTaxTO");
        //        System.out.println("objCashTransactionTO :"+objChargesServiceTaxTO);
        if (mdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            HashMap tempMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
            String trans_id = CommonUtil.convertObjToStr(mdsApplicationTO.getTransId());
            CashTransactionTO objCashTO = new CashTransactionTO();
            //            List lst = sqlMap.executeQueryForList("getSelectENTERED_AMOUNT",trans_id);
            //            if(lst!=null && lst.size()>0){
            objCashTO = new CashTransactionTO();
            //                objCashTO = (CashTransactionTO) lst.get(0);
            objCashTO.setInpAmount(mdsApplicationTO.getInstAmt());
            objCashTO.setAmount(mdsApplicationTO.getInstAmt());
            objCashTO.setProdType(CommonUtil.convertObjToStr("GL"));
            objCashTO.setInstrumentNo2("MDS_APPLICATION");
            objCashTO.setCommand(mdsApplicationTO.getCommand());
            cashList.add(objCashTO);
            //            }
            //            lst = null;
            objCashTO = null;
        }
        //        objChargesServiceTaxTO = null;
        return cashList;
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        String BRANCH_ID = CommonUtil.convertObjToStr(chargesMap.get("BRANCH_CODE"));
        double totalAmt = CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_UPDATE);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tempMap = new HashMap();
        HashMap deleteMap = new HashMap();
        double oldAmount = 0.0;
        if (chargesMap.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) chargesMap.get("TransactionTO");
            System.out.println("Inside deleteData chargesMap containskey :" + chargesMap);
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            System.out.println("Inside deleteData transactionTO :" + transactionTO);
            mdsApplicationTO.setStatus(CommonConstants.STATUS_DELETED);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("Inside TRANSFER ");
                if (chargesMap.containsKey("AMT_TRANSACTION") && CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue() > 0) {
                    tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                    System.out.println("#### tempMap : " + tempMap);
                    oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                    tempMap.remove("TRANS_ID");
                    tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                    double newAmount = CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(mdsApplicationTO.getCommand());
                    if (mdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (mdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                }
                                txTransferTO.setStatusDt(currDt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                            }
                        }
                        transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, commandMode);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                }
                deleteMap.put("TRANS_ID", tempMap.get("BATCH_ID"));
            } else if (transactionTO.getTransType().equals("CASH")) {
                tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                System.out.println("#### inside CASH tempMap : " + tempMap);
                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                CashTransactionDAO cashDAO = new CashTransactionDAO();
                CashTransactionTO cashTO = new CashTransactionTO();
                HashMap cashMap = new HashMap();
                String transId = CommonUtil.convertObjToStr(tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap.put("TRANS_DT", currDt);
                cashMap.put("INITIATED_BRANCH", BRANCH_ID);
                System.out.println("cashMap 1st :" + cashMap);
                List lstCash = (List) sqlMap.executeQueryForList("getSelectCashTransactionTO", cashMap);
                if (lstCash != null && lstCash.size() > 0) {
                    for (int i = 0; i < lstCash.size(); i++) {
                        cashTO = (CashTransactionTO) lstCash.get(i);
                        cashTO.setCommand("DELETE");
                        cashTO.setStatus(CommonConstants.STATUS_DELETED);
                        cashMap.put("CashTransactionTO", cashTO);
                        cashMap.put("BRANCH_CODE", BRANCH_ID);
                        cashMap.put("USER_ID", chargesMap.get("USER_ID"));
                        cashMap.put("OLDAMOUNT", new Double(oldAmount));
                        cashMap.put("SELECTED_BRANCH_ID", BRANCH_ID);
                        System.out.println("cashMap :" + cashMap);
                        cashDAO.execute(cashMap, false);
                    }
                }
                deleteMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                cashMap = null;
                cashTO = null;
                cashDAO = null;
            }
            deleteMap = null;
            tempMap = null;
        }
        System.out.println("mdsApplicationTO :" + mdsApplicationTO);
        mdsApplicationTO.setAuthorizeStatus("DELETED");
        sqlMap.executeUpdate("updateApplicationTO", mdsApplicationTO);
        sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", mdsApplicationTO);
    }

    private String getChittalBondNO() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        // where.put(CommonConstants.MAP_WHERE, "GBID");
        // return "";
        HashMap map = generateID();
        //System.out.println("MAP IN DAOOOOOO=========="+map);
        return (String) map.get(CommonConstants.DATA);
        //  return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "CHITTAL_BOND_NO"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("AuthorizeMap :" + map);
        HashMap masterMap = new HashMap();
        if (map.containsKey("MASTER_DATA")) {
            masterMap = (HashMap) map.get("MASTER_DATA");
            System.out.println("masterMap" + masterMap);
        }
//        HashMap hmap=new HashMap();
//        hmap.put("CHITTAL_NO",mdsApplicationTO.getChitNo());
        List stsList = sqlMap.executeQueryForList("getStatusMDSApp", masterMap);
        String sts = ((HashMap) stsList.get(0)).get("STATUS").toString();
        System.out.println("mdsApplicationTO....." + mdsApplicationTO.getStatus());

        if (!sts.equals("MODIFIED")) {
            if (map.containsKey("TransactionTO") && map.get("TransactionTO") != null) {
                System.out.println("dfsf");
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                TransactionTO transactionTO = new TransactionTO();
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap applnAuthMap = new HashMap();
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    HashMap authorizeMap = new HashMap();
                    authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                    System.out.println("map :" + map);
                    HashMap cashAuthMap = new HashMap();
                    String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                    applnAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
                    applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    applnAuthMap.put("TRANS_ID", transactionTO.getBatchId());
                    String linkBatchId = "";
                    if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL")) {
                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
                    } else {
                        linkBatchId = CommonUtil.convertObjToStr(transactionTO.getBatchId());
                    }
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
                    TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    authorizeMap = null;
                    cashAuthMap = null;
                } else {
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    ArrayList arrList = new ArrayList();
                    HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                    HashMap singleAuthorizeMap = new HashMap();
                    String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                    applnAuthMap.put("AUTHORIZE_STATUS", status);
                    singleAuthorizeMap.put("STATUS", status);
                    singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                    singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                    applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    applnAuthMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                    arrList.add(singleAuthorizeMap);
                    String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                    String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
                    System.out.println("before making new DAO map :" + map);
                    map = new HashMap();
                    map.put("SCREEN", "Cash");
                    map.put("USER_ID", userId);
                    map.put("SELECTED_BRANCH_ID", branchCode);
                    map.put("BRANCH_CODE", branchCode);
                    map.put("MODULE", "Transaction");
                    HashMap dataMap = new HashMap();
                    dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                    dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    dataMap.put("DAILY", "DAILY");
                    map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                    System.out.println("before entering DAO map :" + map);
                    cashTransactionDAO.execute(map, false);
                    cashTransactionDAO = null;
                    dataMap = null;
                }
                //Added by sreekrishnan 
//                if(true){
//                    System.out.println("in bonus #@$@#$#@$ auht");
//                    HashMap preMap = new HashMap();
//                    HashMap authorizeMap = new HashMap();
//                    String linkBatchId = ""; 
//                    preMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
//                    preMap.put("TRANS_DT", currDt.clone());
//                    if (transactionTO.getTransType().equals("TRANSFER")) {
//                        preMap.put("TRANS_MODE", "TRANSFER");                        
//                    }else{
//                        preMap.put("TRANS_MODE", "CASH");                        
//                    } 
//                    preMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
//                    List preList = ServerUtil.executeQuery("getPredifiendBonusTransId", preMap);
//                    if (preList != null && preList.size() > 0) {
//                        preMap = (HashMap) preList.get(0);
//                        linkBatchId = CommonUtil.convertObjToStr(preMap.get("BATCH_ID"));
//                        applnAuthMap.put("TRANS_ID", CommonUtil.convertObjToStr(preMap.get("BATCH_ID")));                   
//                        System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());                    
//                        authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
//                        System.out.println("map :" + map);
//                        HashMap cashAuthMap = new HashMap();
//                        String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
//                        applnAuthMap.put("AUTHORIZE_STATUS", authorizeStatus);
//                        applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID")); 
//                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
//                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//                        System.out.println(" cashAuthMap :" + cashAuthMap + "linkBatchId :" + linkBatchId + "authorizeStatus :" + authorizeStatus);
//                        TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
//                        System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
//                        authorizeMap = null;
//                        cashAuthMap = null;
//                    }
//                }
//            //Changed By Suresh
//            if(mdsApplicationTO.getCoChittal().equals("N")){
//                updateChittalNo();
//            }else{
//                UpdateCoChittalNo();
//            }
                applnAuthMap.put("AUTHORIZE_DT", currDt);
                applnAuthMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                applnAuthMap.put("SCHEME", mdsApplicationTO.getSchemeName());
                applnAuthMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsApplicationTO.getSubNo()));
                if(mdsApplicationTO.getCudt_id()!=null && mdsApplicationTO.getCudt_id().length()>0){
                    applnAuthMap.put("CUST_ID",mdsApplicationTO.getCudt_id());
                }                
                System.out.println("0 "+mdsApplicationTO.getCudt_id());
                sqlMap.executeUpdate("updateAuthorizeStatusAppl", applnAuthMap);
                sqlMap.executeUpdate("updateMDSAvailBalance", applnAuthMap);
                applnAuthMap = null;
                HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                if (status.equals("AUTHORIZED")) {
                    System.out.println("@$#$@$%@$#masterMap :" + masterMap);                     // INSERT MASTER MAINTANANCE DETAILS
                    List lst = ServerUtil.executeQuery("getApplDetails", masterMap);
                    if (lst != null && lst.size() > 0) {
                        masterMap = (HashMap) lst.get(0);
                        MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
                        MaintenanceTO.setSchemeName(CommonUtil.convertObjToStr(masterMap.get("SCHEME_NAME")));
                        MaintenanceTO.setChittalNo(CommonUtil.convertObjToStr(masterMap.get("CHITTAL_NO")));
                        MaintenanceTO.setSubNo(CommonUtil.convertObjToInt(masterMap.get("SUB_NO")));    //AJITH
                        MaintenanceTO.setDivisionNo(CommonUtil.convertObjToInt(masterMap.get("DIVISION_NO")));  //AJITH
                        MaintenanceTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(masterMap.get("CHIT_START_DT"))));
                        MaintenanceTO.setMemberNo(CommonUtil.convertObjToStr(masterMap.get("MEMBER_NO")));
                        MaintenanceTO.setMemberName(CommonUtil.convertObjToStr(masterMap.get("MEMBER_NAME")));
                        MaintenanceTO.setNominee(CommonUtil.convertObjToStr(masterMap.get("NOMINEE")));
                        MaintenanceTO.setNomineeName(CommonUtil.convertObjToStr(masterMap.get("NOMINEE_NAME")));
                        MaintenanceTO.setMemberType(CommonUtil.convertObjToStr(masterMap.get("MEMBER_TYPE")));
                        MaintenanceTO.setSalaryRecovery(CommonUtil.convertObjToStr(masterMap.get("SALARY_RECOVERY")));
                        MaintenanceTO.setBranchCode(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                        MaintenanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        MaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                        MaintenanceTO.setStatusDt(currDt);
                        MaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                        MaintenanceTO.setAuthorizedBy((String) map.get(CommonConstants.USER_ID));
                        MaintenanceTO.setAuthorizedDt(currDt);
                        MaintenanceTO.setBondSet("N");
                        MaintenanceTO.setApplicationSet("N");
//                    MaintenanceTO.setBondNo(getChittalBondNO());
                        sqlMap.executeUpdate("insertMDSMasterMaintenanceTO", MaintenanceTO);
                    }
                }
                masterMap = null;
            }
        } else {
            HashMap applnAuthMap = new HashMap();
            HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
            applnAuthMap.put("AUTHORIZE_STATUS", status);
            applnAuthMap.put("AUTHORIZE_DT", currDt);
            applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
            applnAuthMap.put("TRANS_ID", mdsApplicationTO.getTransId());
            applnAuthMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
            applnAuthMap.put("SCHEME", mdsApplicationTO.getSchemeName());
            applnAuthMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsApplicationTO.getSubNo()));
            applnAuthMap.put("CUST_ID", mdsApplicationTO.getCudt_id());

            sqlMap.executeUpdate("updateAuthorizeStatusAppl", applnAuthMap);
        }
        HashMap authorizeMap1 = (HashMap) map.get("AUTHORIZEMAP");
        String status1 = CommonUtil.convertObjToStr(authorizeMap1.get("AUTHORIZESTATUS"));
        if(status1.equals("REJECTED"))
        {
             HashMap mapNew = new HashMap();
             mapNew.put("SCHEME_NAME", mdsApplicationTO.getSchemeName());
             List dataList = sqlMap.executeQueryForList("getLastChittalNo", mapNew);
             int lastno=0;
             for(int i=0;i<dataList.size();i++)
             {
                HashMap newMap = (HashMap) dataList.get(0);
                if(newMap!=null && newMap.get("NEXT_CHITTAL_NO")!=null)
                {
                    String Last_no = newMap.get("NEXT_CHITTAL_NO").toString();
                    lastno = CommonUtil.convertObjToInt(Last_no);
                }
             }
             mapNew.put("CHITTAL_NO",lastno-1);
             sqlMap.executeUpdate("updateLastChittalNo", mapNew);
        }
         map = null;
    }

    private void UpdateCoChittalNo(HashMap map) throws Exception {
        HashMap where = new HashMap();
        HashMap mapData = new HashMap();
        String strPrefix = "";
        double instAmt = 0.0;
        String value = CommonUtil.convertObjToStr(mdsApplicationTO.getSchemeName());
        List lst = (List) sqlMap.executeQueryForList("getChittalNO", value);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            instAmt = CommonUtil.convertObjToDouble(mapData.get("INSTALLMENT_AMOUNT")).doubleValue();
            //String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);  //AJITH
            int nxtID = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE")) + 1;
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", value);
            whereMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
            List instlst = ServerUtil.executeQuery("getSumOfCoChittalInstAmt", whereMap);
            System.out.println("#### Ins List" + instlst);
            if (map.containsKey("UPDATE_CHIT_NO") && !map.get("UPDATE_CHIT_NO").equals("N")) {
                if (instlst != null && instlst.size() > 0) {
//                whereMap = (HashMap)instlst.get(0);
//                double sumInstAmt = CommonUtil.convertObjToDouble(whereMap.get("INST_AMT")).doubleValue();
//                if(sumInstAmt == instAmt){
//                    where.put("SCHEME_NAME",value);
//                    where.put("VALUE", nxtID);
//                    where.put("SUFFIX", String.valueOf("1"));
//                    sqlMap.executeUpdate("updateChittalNxtNum", where);
//                }
                } else {
                    where.put("SCHEME_NAME", value);
                    where.put("VALUE", nxtID);
                    where.put("SUFFIX", 1);   //AJITH Changed from String.valueOf("1")
                    sqlMap.executeUpdate("updateChittalNxtNum", where);
                }
            }
        }
    }

    public static void main(String str[]) {
        try {
            MDSApplicationDAO dao = new MDSApplicationDAO();
            HashMap inputMap = new HashMap();
            //inputMap.put("ACCT_HD", "CCS_D");

            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * To Verify The Account Head data...
         */
        System.out.println("Map in DAO:ghfhfgh " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        /*
         * data fot the Normal operations like Insert, Update, and/or Delete...
         */
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

        if (map.containsKey("mdsApplicationTO")) {
            mdsApplicationTO = new MDSApplicationTO();
            mdsApplicationTO = (MDSApplicationTO) map.get("mdsApplicationTO");
            mdsApplicationTO.setStatusBy(objLogTO.getUserId());
            final String command = mdsApplicationTO.getCommand();
            System.out.println("commandcommandcommand" + command);
            //            operativeAcctProductTO.setSuserId((String) map.get(CommonConstants.USER_ID));
            /*
             * No User id is supplied from the OB, so it cannot be obtained from
             * the HashMap map...
             */
            //            operativeAcctProductTO.setSuserId((String) CommonConstants.USER_ID);
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);  
                
                
                if (map.containsKey("SMSSubscriptionTO")) {
                    objSMSSubscriptionTO = new SMSSubscriptionTO();
                    objSMSSubscriptionTO = (SMSSubscriptionTO) map.get("SMSSubscriptionTO");
                }            
                System.out.println("objSMSSubscriptionTO%$^$^$^$^"+objSMSSubscriptionTO);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                    execReturnMap.put("CHITTAL_NO", mdsApplicationTO.getChittalNo());
                    execReturnMap.put("TRANS_ID", mdsApplicationTO.getTransId());
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                    execReturnMap.put("CHITTAL_NO",mdsApplicationTO.getChittalNo());
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                    deleteSMSSubscription(mdsApplicationTO);
                } else if (map.containsKey("AUTHORIZEMAP")) {
                    authorize(map);
                    //Added for sms subscription                    
                    authorizeSMSSubscription(map);
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
        return execReturnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        mdsApplicationTO = null;
        objSMSSubscriptionTO = null;
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
