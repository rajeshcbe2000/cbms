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
package com.see.truetransact.serverside.gdsapplication;

import com.see.truetransact.serverside.mdsapplication.*;
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
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import java.util.*;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Sathiya
 *
 * @modified Pinky @modified Rahul
 */
public class GDSApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private GDSApplicationTO gdsApplicationTO = null;
    private MDSMasterMaintenanceTO MaintenanceTO;
    TransferDAO transferDAO = new TransferDAO();
    NomineeDAO objNomineeDAO = new NomineeDAO();
    HashMap execReturnMap = new HashMap();
    private Date currDt = null;
    final String SCREEN = "TD";
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    private List gdsApplicationList = new ArrayList();

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public GDSApplicationDAO() throws ServiceLocatorException {
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
        List list1 = (List) sqlMap.executeQueryForList("getSelectGroupNameTO", map);
        returnMap.put("groupNameTO", list1);
//        String where = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
//        List list = (List) sqlMap.executeQueryForList("getSelectMDSApplicationTO", where);
        if (map.get("TRANS_ID").toString().equals("")) {
            map.put("TRANS_ID", null);
        }
        List list = (List) sqlMap.executeQueryForList("getSelectGDSApplicationTO", map);
        returnMap.put("gdsApplicationTO", list);
        System.out.println("list...." + list);
        HashMap whereMap = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        String transId = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
        HashMap getTransMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(_branchCode);

        getTransMap.put("TRANS_ID", transId);
        if (list != null) {
            Date currDt1 = null;
            GDSApplicationTO to = new GDSApplicationTO();
            to = (GDSApplicationTO) list.get(0);
            System.out.println("to..." + to);
            currDt1 = new java.sql.Date(getProperDateFormat(to.getStatusDt()).getTime());//changed by jithin
            getTransMap.put("TRANS_DT", getProperDateFormat(to.getStatusDt()));//currDt1);
        }
        // getTransMap.put("TRANS_DT", currDt);
        System.out.println("nnnnsssss" + getTransMap);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put(CommonConstants.MAP_WHERE, getTransMap);
        list = transactionDAO.getData(whereMap);
        System.out.println("list transactionDAO.getData :: "+ list);
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
            List lstEdit = sqlMap.executeQueryForList("getGDSSelectApplnAmt", editMap);
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
        GDSApplicationTO applicationTo = new GDSApplicationTO();
        applicationTo = (GDSApplicationTO) ((List) returnMap.get("gdsApplicationTO")).get(0);
        smssubMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        smssubMap.put("PROD_ID", applicationTo.getGroupName());
        smssubMap.put("ACT_NUM", applicationTo.getChittalNo());
        list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smssubMap);
        if (list != null && list.size() > 0) {
            returnMap.put("SMSSubscriptionTO", list);
        }
        list = null;

        System.out.println("returnMap : " + returnMap);
        return returnMap;
    }

    private void updateGroupNo() throws Exception {
//        HashMap where = new HashMap();
//        HashMap mapData = new HashMap();
//        String strPrefix = "";
//        String strNum = "";
//        int len = 11;
//        double nofOfMember = 0;
//        int suffix = 0;
//        double lastValue = 0;
//        String genID = "";
//        String value = CommonUtil.convertObjToStr(gdsApplicationTO.getSchemeName());
        HashMap schemeMap = new HashMap();
//        schemeMap.put("NEXT_GDSNo", gdsApplicationTO.getNextGDSNo());

//        HashMap deletedStatusMap = new HashMap();
//        deletedStatusMap.put("SCHEME_NAME",gdsApplicationTO.getSchemeName());
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
//                sqlMap.executeUpdate("updateGDSChittalNxtNum", where);
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
//        List lst = (List) sqlMap.executeQueryForList("getGDSChittalNO", value);
//        if (lst != null && lst.size() > 0) {
//            mapData = (HashMap) lst.get(0);
//        }
//        if (mapData.containsKey("PREFIX")) {
//            strPrefix = (String) mapData.get("PREFIX");
//        }

        int NEXT_GDS_NO = CommonUtil.convertObjToInt(schemeMap.get("NEXT_GDS_NO")) + 1;
        schemeMap.put("NEXT_GDS_NO", NEXT_GDS_NO);
        schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                String gDSNoInString=CommonUtil.lpad(nextGenNo, 5, '0');
//                gDSNoGen=CommonUtil.convertObjToStr(hash.get("BRANCH_ID"))+CommonUtil.convertObjToStr(hash.get("GROUP_NO"))+CommonUtil.convertObjToStr(gDSNoInString); 
//        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//        double nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//        nofOfMember = CommonUtil.convertObjToDouble(mapData.get("NO_OF_MEMBER_PER_DIVISION")).doubleValue();
//        lastValue = CommonUtil.convertObjToDouble(mapData.get("LAST_VALUE")).doubleValue();
//        int numFrom = strPrefix.trim().length();
//        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
//        genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//        gdsApplicationTO.setChitNo(new Double(lastValue));
//        where.put("SCHEME_NAME", value);
//        where.put("VALUE", nxtID);
//        where.put("SUFFIX", String.valueOf(gdsApplicationTO.getNoOfDivisions()));
//        System.out.println("####" + where);
        sqlMap.executeUpdate("updateGDSNxtNum", schemeMap);
//        }
//        return genID;
    }

    private void updateChittalNo(GDSApplicationTO gdsLocalApplicationTO) throws Exception {
        HashMap where = new HashMap();
        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        int len = 11;
        double nofOfMember = 0;
        int suffix = 0;
        int lastValue = 0;
        String genID = "";
        String value = CommonUtil.convertObjToStr(gdsLocalApplicationTO.getSchemeName());
//        HashMap schemeMap = new HashMap();
//        schemeMap.put("GROUP_NAME", gdsLocalApplicationTO.getGroupName());
//        HashMap deletedStatusMap = new HashMap();
//        deletedStatusMap.put("SCHEME_NAME",gdsApplicationTO.getSchemeName());
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
//                sqlMap.executeUpdate("updateGDSChittalNxtNum", where);
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
        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
        int numFrom = strPrefix.trim().length();
        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
        //String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
        int nxtID = CommonUtil.convertObjToInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1;
        genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
        gdsLocalApplicationTO.setChitNo(lastValue);
        where.put("SCHEME_NAME", value);
        where.put("VALUE", nxtID);
        //where.put("SUFFIX", String.valueOf(gdsLocalApplicationTO.getNoOfDivisions()));
        where.put("SUFFIX", gdsLocalApplicationTO.getNoOfDivisions());
        System.out.println("####" + where);
        sqlMap.executeUpdate("updateChittalNxtNum", where);
//        }
//        return genID;
    }

    public void commonTransactionCashandTransfer(HashMap map, GDSApplicationTO gdsApplicationTO, String generateSingleTransId, HashMap applicationMap) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        System.out.println("gdsApplicationTO####" + gdsApplicationTO + map);
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
        transactionDAO.setInitiatedBranch(gdsApplicationTO.getBranchCode());
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        System.out.println("mdsCurrentReceiptEntryTO.getBonusAmtPayable()" + gdsApplicationTO.getInstallmentAmount());
        if (CommonUtil.convertObjToDouble(gdsApplicationTO.getInstallmentAmount()) > 0 && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS")) > 0) {
            System.out.println("Bonus Started");
            //Debit
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, gdsApplicationTO.getBranchCode());//BRANCH_ID);            
            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo());
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
            transferTo.setBranchId(gdsApplicationTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(gdsApplicationTO.getBranchCode());//bb1 BRANCH_ID);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
            transferTo.setGlTransActNum(gdsApplicationTO.getChittalNo());
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
            txMap.put(TransferTrans.CR_BRANCH, gdsApplicationTO.getBranchCode());//BRANCH_ID);
            txMap.put(TransferTrans.PARTICULARS, "Bonus :" + applicationMap.get("MP_MDS_CODE") + "-" + gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo());
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
            transferTo.setBranchId(gdsApplicationTO.getBranchCode());//BRANCH_ID);
            transferTo.setInitiatedBranch(gdsApplicationTO.getBranchCode());
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
            transferTo.setGlTransActNum(gdsApplicationTO.getChittalNo());
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
            
            HashMap gdsApplTOMap = new HashMap();
            HashMap chillalSchemeMap = new HashMap();
            String generateSingleCashId = "";
            String generateSingleTransId = generateLinkID();
            gdsApplicationTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
            gdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
//            gdsApplicationTO.setChittalNo(getChittalNo());
//            updateChittalNo();
            ArrayList transferList = new ArrayList();
            
            TransferTrans transferTrans = new TransferTrans();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            TransactionTO transactionTO = new TransactionTO();
            HashMap txMap = new HashMap();
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
//            double totalAmt = CommonUtil.convertObjToDouble(gdsApplicationTO.getInstAmt()).doubleValue();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            if (gdsApplicationTO.getIsTran().equals("Y")) {
             // generating chittal number and saving in this block   
                 HashMap schemeMap1 = new HashMap();
                System.out.println("###########FLAGgdsApplicationTO.getGroupNo()" + gdsApplicationTO.getGroupNo());
                schemeMap1.put("GROUP_NO", gdsApplicationTO.getGroupNo());
                List list1 = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap1);
                HashMap individualSchemeMap1 = new HashMap();
                if (list1 != null && list1.size() > 0) {
                    for (int j = 0; j < list1.size(); j++) {
                        individualSchemeMap1 = (HashMap) list1.get(j);
                        System.out.println("schemename" + individualSchemeMap1);
                        HashMap mapData = new HashMap();
                        HashMap mapData1 = new HashMap();
                        String strPrefix = "";
                        String strNum = "";
                        int len = 13;
                        int nofOfMember = 0;
                        int suffix = 0;
                        int lastValue = 0;
                        int minDivisionNo = 0;
                        int maxNofCoMember = 0;
                        GDSApplicationTO tempGDSApplicationTO = new GDSApplicationTO();
                        tempGDSApplicationTO = gdsApplicationTO;
                        // GDSApplicationTO tempGDSApplicationTO = new GDSApplicationTO();
                        System.out.println("schme name" + CommonUtil.convertObjToStr(individualSchemeMap1.get("SCHEME_NAME")));
                        tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(individualSchemeMap1.get("SCHEME_NAME")));
                        tempGDSApplicationTO.setGroupNo(CommonUtil.convertObjToStr(individualSchemeMap1.get("GROUP_NO")));
                        tempGDSApplicationTO.setInstallmentAmount(CommonUtil.convertObjToDouble(individualSchemeMap1.get("INSTALLMENT_AMOUNT")));
                        System.out.println("INSTALLMENT_AMOUNT" + tempGDSApplicationTO.getInstallmentAmount());
 
                        HashMap whereSchemeMap = new HashMap();
                        whereSchemeMap.put("SCHEME_NAME", tempGDSApplicationTO.getSchemeName());
                        List lst = (List) ServerUtil.executeQuery("getGDSChittalNOUISide", whereSchemeMap);
                        if (lst != null && lst.size() > 0) {
                            mapData = (HashMap) lst.get(0);
                        }
                        if (mapData.containsKey("PREFIX")) {
                            strPrefix = (String) mapData.get("PREFIX");
                        }
                        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
                        int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
                        nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
                        maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
                        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
                        int numFrom = strPrefix.trim().length();
                        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
                        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
                        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                        tempGDSApplicationTO.setChittalNo(genID);
                        //getSelectGDSSchemeForChittal
//                        HashMap schMap = new HashMap();
//                        schMap.put("CHITTAL_NO",genID);
//                        List schlst = (List) ServerUtil.executeQuery("getSelectGDSSchemeForChittal", schMap);
//                        if(schlst != null && schMap.size() > 0){
//                            schMap = (HashMap)schlst.get(0);
//                            tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(schMap.get("SCHEME_NAME")));
//                        }
                        sqlMap.executeUpdate("insertGDSApplicationTOForNo", tempGDSApplicationTO);
                        gdsApplicationList.add(tempGDSApplicationTO);
                        gdsApplTOMap.put(genID, tempGDSApplicationTO);
                        chillalSchemeMap.put(genID,tempGDSApplicationTO.getSchemeName());
                        System.out.println("gdsApplicationList " + j + "::" + gdsApplicationList);
                        MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
                        MaintenanceTO.setSchemeName(tempGDSApplicationTO.getSchemeName());
                        MaintenanceTO.setChittalNo(tempGDSApplicationTO.getChittalNo());
                        MaintenanceTO.setSubNo(CommonUtil.convertObjToInt(tempGDSApplicationTO.getSubNo()));
                        MaintenanceTO.setDivisionNo(CommonUtil.convertObjToInt(tempGDSApplicationTO.getNoOfDivisions()));
                        MaintenanceTO.setChitStartDt(tempGDSApplicationTO.getSchemeStartDt());
                        MaintenanceTO.setMemberNo(tempGDSApplicationTO.getMembershipNo());
                        MaintenanceTO.setMemberName(tempGDSApplicationTO.getMembershipName());
                        MaintenanceTO.setNominee(tempGDSApplicationTO.getNominee());
                        //MaintenanceTO.setNomineeName(gdsApplicationTO.getN);
                        MaintenanceTO.setMemberType(tempGDSApplicationTO.getMembershipType());
                        MaintenanceTO.setSalaryRecovery(tempGDSApplicationTO.getSalaryRecovery());
                        MaintenanceTO.setBranchCode(tempGDSApplicationTO.getBranchCode());
                        MaintenanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        MaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                        MaintenanceTO.setStatusDt(currDt);
                        // MaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                        // MaintenanceTO.setAuthorizedBy((String) map.get(CommonConstants.USER_ID));
                        // MaintenanceTO.setAuthorizedDt(currDt);
                        MaintenanceTO.setBondSet("N");
                        MaintenanceTO.setApplicationSet("N");
                        MaintenanceTO.setMemberGDSNo(CommonUtil.convertObjToStr(gdsApplicationTO.getGds_No()));

//                    MaintenanceTO.setBondNo(getChittalBondNO());
                        mapData1.put("GDS_NO", gdsApplicationTO.getGroupNo());
                        mapData1.put("NEXT_GDS_NO", CommonUtil.convertObjToInt(gdsApplicationTO.getNextGDSNo()));
                        sqlMap.executeUpdate("updateGDSNxtNum", mapData1);
                        sqlMap.executeUpdate("insertGDSMasterMaintenanceTO", MaintenanceTO);
                        //gdsApplicationList.add(tempGDSApplicationTO);
                        System.out.println("temp data ::" + tempGDSApplicationTO.getSchemeName());

                        if (tempGDSApplicationTO.getCoChittal().equals("N")) {
                            updateChittalNo(tempGDSApplicationTO);
                        } else {
                            UpdateCoChittalNo(map);
                        }
                    }
                    System.out.println("shany... gdsApplTOMap :: "+ gdsApplTOMap);
                }
                 System.out.println("gdsApplicationList data ::"+gdsApplicationList);  
                
                if (map.containsKey("TransactionTO")) {
                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    }
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    double totalAmt = 0.0;
                    totalAmt = CommonUtil.convertObjToDouble((transactionTO.getTransAmt()).doubleValue()/list1.size());
//                    HashMap applicationMap = new HashMap();
//                    applicationMap.put("SCHEME_NAME", gdsApplicationTO.getSchemeName());
//                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
//                    if (lst != null && lst.size() > 0) {
//                        applicationMap = (HashMap) lst.get(0);
//                    }
                    List lst=null;
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
                            if(!map.containsKey("THALAYAL_CHITTAL")){
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
                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
                                }
                            }
                            //Babu commented for the mantis id : 7509
                            //txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                            //txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());//commented by sreekrishnan and added lines in line no.319 and no.313
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("DR_INST_TYPE", "VOUCHER");
                            txMap.put(TransferTrans.PARTICULARS, gdsApplicationTO.getGroupName() + "-" + CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()
                                    + "_" + gdsApplicationTO.getSubNo()));
                            if (transactionTO.getProductType().equals("OA")) {
                                txMap.put("TRANS_MOD_TYPE", "OA");
                            } else if (transactionTO.getProductType().equals("AB")) {
                                txMap.put("TRANS_MOD_TYPE", "AB");
                            } else if (transactionTO.getProductType().equals("SA")) {
                                txMap.put("TRANS_MOD_TYPE", "SA");
                            } else if (transactionTO.getProductType().equals("TL")) {
                                txMap.put("TRANS_MOD_TYPE", "TL");
                            } else if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("TRANS_MOD_TYPE", "AD");
                            } else {
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            }
                            System.out.println("txMap : " + txMap + "totalAmt :" + totalAmt);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble((transactionTO.getTransAmt()).doubleValue()));
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
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
                            TxTransferTO.add(transferTo);
                           } 
                            // Adding code for transfer trans - by nithya using hashmap instead of list 
                            Iterator iterate = gdsApplTOMap.entrySet().iterator();
                            if (null != gdsApplTOMap && gdsApplTOMap.size() > 0) {
                                int schemeIndex=0; 
                                ArrayList list = new ArrayList();
                                while (iterate.hasNext()) {
                                    GDSApplicationTO objGDSApplicationTO = new GDSApplicationTO();
                                    Map.Entry entry = (Map.Entry) iterate.next();
                                    Object key1 = (Object) entry.getKey();
                                    Object value = (Object) entry.getValue();
                                    System.out.println("key : " + key1.toString() + " value : " + value.toString());
                                    objGDSApplicationTO = (GDSApplicationTO) value;
                                    System.out.println("nithya :: " + objGDSApplicationTO);
                                    HashMap individualSchemeMap = (HashMap) list1.get(schemeIndex);
                                    System.out.println("schemename" + individualSchemeMap); 
                                    HashMap applicationMap = new HashMap();
                                    applicationMap.put("SCHEME_NAME", chillalSchemeMap.get(key1));
                                    
                                    // applicationMap.put("SCHEME_NAME", gdsApplicationTO.getSchemeName());
                                    List lst1 = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
                                    if (lst1 != null && lst1.size() > 0) {
                                        applicationMap = (HashMap) lst1.get(0);
                                    }
                                    
                                    //Thalayal - Debit transaction starts
                                    if (map.containsKey("THALAYAL_CHITTAL") && !map.get("THALAYAL_CHITTAL").equals("") && map.get("THALAYAL_CHITTAL").equals("Y")) {
                                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                            txMap = new HashMap();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) applicationMap.get("THALAYAL_REP_PAY_HEAD"));
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.PARTICULARS, gdsApplicationTO.getGroupName() + "-" + CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()
                                            + "_" + gdsApplicationTO.getSubNo()));
                                            txMap.put("TRANS_MOD_TYPE", "GL");
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
                                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
                                            TxTransferTO.add(transferTo);
                                        }
                                    }
                                    // Thalayal - Debit transaction ends                                    
                                    
                                    txMap = new HashMap();
                                    transferTo = new TxTransferTO();
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    if (gdsApplicationTO.getThalayal().equals("Y")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
                                    } else if (gdsApplicationTO.getMunnal().equals("Y")) {
                                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                                    } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
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
                                    transferTo.setLinkBatchId(gdsApplicationTO.getGds_No());
                                    System.out.println("transferTo List 2 : " + transferTo);
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(key1));
                                    transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                                    TxTransferTO.add(transferTo);
                                    schemeIndex ++;
                                }
                            }
                            // End Adding code for transfer trans - by nithya using hashmap instead of list 
                            
                            // modify code for credit side transaction for each scheme
                            HashMap schemeMap = new HashMap();
//                            schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                             List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
                      //       totalAmt=totalAmt/list1.size();
//                             for (int i = 0; i < list1.size(); i++) {
//                                 HashMap individualSchemeMap = (HashMap) list1.get(i);
//                                 HashMap applicationMap = new HashMap();
//                                 applicationMap.put("SCHEME_NAME", individualSchemeMap.get("SCHEME_NAME"));
//
//                                 // applicationMap.put("SCHEME_NAME", gdsApplicationTO.getSchemeName());
//                                 List lst1 = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
//                                 if (lst1 != null && lst1.size() > 0) {
//                                     applicationMap = (HashMap) lst1.get(0);
//                                 }
//                                 txMap = new HashMap();
//                                 transferTo = new TxTransferTO();
//                                 txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                                 if (gdsApplicationTO.getThalayal().equals("Y")) {
//                                     txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
//                                 } else if (gdsApplicationTO.getMunnal().equals("Y")) {
//                                     txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
//                                 } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
//                                     txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
//                                 } else {
//                                     if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
//                                         txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
//                                     } else {
//                                         HashMap suspenseMap = new HashMap();                // Suspense Acc Head
//                                         suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
//                                         List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
//                                         if (suspenseLst != null && suspenseLst.size() > 0) {
//                                             suspenseMap = (HashMap) suspenseLst.get(0);
//                                             txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
//                                             txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
//                                             txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
//                                             txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
//                                         }
//                                     }
//                                 }
//                                 txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
//                                 txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
//                                 txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                                 txMap.put("TRANS_MOD_TYPE", "MDS");
//                                 System.out.println("txMap : " + txMap + "serviceAmt :" + totalAmt);
//                                 transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt);
//                                 transferTo.setTransId("-");
//                                 transferTo.setBatchId("-");
//                                 transferTo.setInitiatedBranch(BRANCH_ID);
//                                 transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
//                                 transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                                 
//                                 if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
//                                     transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
//                                 }
//                                 transferTo.setLinkBatchId(gdsApplicationTO.getGds_No());
//                                 System.out.println("transferTo List 2 : " + transferTo);
//                                 transferTo.setSingleTransId(generateSingleTransId);
//                                 transferTo.setGlTransActNum(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
//                                 transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                                 TxTransferTO.add(transferTo);
//
//                             }
                        }
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", gdsApplicationTO.getCommand());
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                        // map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
                        HashMap transMap = transferDAO.execute(map, false);
                        gdsApplicationTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        System.out.println("transMap AFTER : " + transMap + "totalAmt :" + totalAmt);
                        transactionTO.setChequeNo("SERVICE_TAX");
                        transactionDAO.setBatchId(gdsApplicationTO.getTransId());
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
                        HashMap transIdMap=new HashMap();
                        
                        transIdMap.put("TRANS_ID",transMap.get("TRANS_ID"));
                        transIdMap.put("gds_No", gdsApplicationTO.getGds_No());
                        transIdMap.put("GDS_APPLICATION","GDS_APPLICATION");
                        sqlMap.executeUpdate("updateTransIdGDSApplication", transIdMap);
                        //Changed by sreekrishnan
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")) {
                            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                        }
                        linkBatchMap = null;
                        transMap = null;
                        //Added by sreekrishnan
                        //if (map.containsKey("PREDEFINED_INSTALL_BONUS") && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0){
                        //     commonTransactionCashandTransfer(map,gdsApplicationTO,generateSingleTransId,applicationMap);
                        //}
                    } 
                    else if (transactionTO.getTransType().equals("CASH")) {
                        System.out.println("gdsApplicationList :: " + gdsApplicationList);
                        HashMap schemeMap = new HashMap();
//                        HashMap cashMap  = new HashMap();
//                        schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                        List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
//                             totalAmt=totalAmt/list.size();
//                   //          CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
//                             for (int i = 0; i < list.size(); i++) {
//                              HashMap   individualSchemeMap = (HashMap) list.get(i);
                            HashMap applicationMap = new HashMap();
                        generateSingleCashId = generateLinkID();
                      //   totalAmt=totalAmt/list1.size();
                        transactionTO.setChequeNo("SERVICE_TAX");
                        applicationMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
//                        applicationMap.put("SCHEME_NAME", gdsApplicationTO.getGroupName());
//                        applicationMap.put("CHIT_NO", gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo());
                        applicationMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                        applicationMap.put("TRANS_MOD_TYPE", "MDS");
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        //ArrayList applncashList = applicationList(map, applicationMap, list1, String.valueOf(totalAmt));
                        ArrayList applncashList = applicationListNew(map, applicationMap, gdsApplTOMap, String.valueOf(totalAmt),list1,chillalSchemeMap);
                        //gdsApplicationList
                        System.out.println("applncashList  "+applncashList);
                        applicationMap.put("DAILYDEPOSITTRANSTO", applncashList);
                        applicationMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                        //applicationMap.put("TRANS_MOD_TYPE", "MDS");
                         
                        HashMap cashMap = cashTransactionDAO.execute(applicationMap, false);
                       // cashMap = cashTransactionDAO.execute(applicationMap, false);
                     //   }//for loop closinf for cash
                        System.out.println("cashMap :" + cashMap);
                        gdsApplicationTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        String command = CommonUtil.convertObjToStr(gdsApplicationTO.getCommand());
                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
                        transactionTO.setBatchDt(gdsApplicationTO.getStatusDt());
                         HashMap transIdMap=new HashMap();
                        transIdMap.put("TRANS_ID",cashMap.get("TRANS_ID"));
                        transIdMap.put("gds_No", gdsApplicationTO.getGds_No());
                        transIdMap.put("GDS_APPLICATION","GDS_APPLICATION");
                        sqlMap.executeUpdate("updateTransIdGDSApplication", transIdMap);
                        
                        
                        map.put("MODE", "INSERT");
                        
                        transactionDAO.setBatchId(gdsApplicationTO.getTransId());
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
                        //    commonTransactionCashandTransfer(map,gdsApplicationTO,generateSingleCashId,applicationMap);
                        //}
                   
                             }
                             
                }
            }
            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            if (gdsApplicationTO.getNominee().equals("Y")) {
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
                    objNomineeDAO.insertData(nomineeTOList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
                    if (nomineeDeleteList != null) {
                        objNomineeDAO.insertData(nomineeDeleteList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), true, USERID, "DEPOIST", objLogTO, objLogDAO);
                    }
                }
            }

            if (gdsApplicationTO.getIsTran().equals("N")) {
                gdsApplicationTO.setTransId("");
                //generating multiple chittal number based on  schema for particular Group GDS
                HashMap schemeMap = new HashMap();
                System.out.println("###########FLAGgdsApplicationTO.getGroupNo()" + gdsApplicationTO.getGroupNo());
                schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
                List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
                HashMap individualSchemeMap = new HashMap();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        individualSchemeMap = (HashMap) list.get(i);
                        System.out.println("schemename"+individualSchemeMap);
                        HashMap mapData = new HashMap();
                        HashMap mapData1 = new HashMap();
                        String strPrefix = "";
                        String strNum = "";
                        int len = 13;
                        int nofOfMember = 0;
                        int suffix = 0;
                        int lastValue = 0;
                        int minDivisionNo = 0;
                        int maxNofCoMember = 0;
                        GDSApplicationTO tempGDSApplicationTO = gdsApplicationTO;
                        System.out.println("schme name"+CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
                        tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
                        HashMap whereSchemeMap = new HashMap();
                        whereSchemeMap.put("SCHEME_NAME", tempGDSApplicationTO.getSchemeName());
                        List lst = (List) ServerUtil.executeQuery("getGDSChittalNOUISide", whereSchemeMap);
                        if (lst != null && lst.size() > 0) {
                            mapData = (HashMap) lst.get(0);
                        }
                        if (mapData.containsKey("PREFIX")) {
                            strPrefix = (String) mapData.get("PREFIX");
                        }
                        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
                        int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
                        nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
                        maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
                        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
                        int numFrom = strPrefix.trim().length();
                        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
                        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
                        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                        tempGDSApplicationTO.setChittalNo(genID);
                        sqlMap.executeUpdate("insertGDSApplicationTOForNo", tempGDSApplicationTO);

                        MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
                        MaintenanceTO.setSchemeName(tempGDSApplicationTO.getSchemeName());
                        MaintenanceTO.setChittalNo(tempGDSApplicationTO.getChittalNo());
                        MaintenanceTO.setSubNo(CommonUtil.convertObjToInt(tempGDSApplicationTO.getSubNo()));
                        MaintenanceTO.setDivisionNo(CommonUtil.convertObjToInt(tempGDSApplicationTO.getNoOfDivisions()));
                        MaintenanceTO.setChitStartDt(tempGDSApplicationTO.getSchemeStartDt());
                        MaintenanceTO.setMemberNo(tempGDSApplicationTO.getMembershipNo());
                        MaintenanceTO.setMemberName(tempGDSApplicationTO.getMembershipName());
                        MaintenanceTO.setNominee(tempGDSApplicationTO.getNominee());
                        //MaintenanceTO.setNomineeName(gdsApplicationTO.getN);
                        MaintenanceTO.setMemberType(tempGDSApplicationTO.getMembershipType());
                        MaintenanceTO.setSalaryRecovery(tempGDSApplicationTO.getSalaryRecovery());
                        MaintenanceTO.setBranchCode(tempGDSApplicationTO.getBranchCode());
                        MaintenanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        MaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                        MaintenanceTO.setStatusDt(currDt);
//                        MaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
//                        MaintenanceTO.setAuthorizedBy((String) map.get(CommonConstants.USER_ID));
//                        MaintenanceTO.setAuthorizedDt(currDt);
                        MaintenanceTO.setMemberGDSNo(gdsApplicationTO.getGds_No());
                        
                        MaintenanceTO.setBondSet("N");
                        MaintenanceTO.setApplicationSet("N");
//                    MaintenanceTO.setBondNo(getChittalBondNO());
//                        mapData1.put("GDS_NO",gdsApplicationTO.getGroupNo());
//                        mapData1.put("NEXT_GDS_NO",gdsApplicationTO.getNextGDSNo());
//                        sqlMap.executeUpdate("updateGDSNxtNum", mapData1);
                        sqlMap.executeUpdate("insertGDSMasterMaintenanceTO", MaintenanceTO);
                        System.out.println("temp data"+tempGDSApplicationTO.getSchemeName());
                        if (tempGDSApplicationTO.getCoChittal().equals("N")) {
                            updateChittalNo(tempGDSApplicationTO);
                        } else {
                            UpdateCoChittalNo(map);
                        }

                    }
                }
                //enf of code
//                  System.out.println("@$#$@$%@$#masterMap :" +);                     // INSERT MASTER MAINTANANCE DETAILS
//                List lst = ServerUtil.executeQuery("getApplDetails",masterMap);
//                if(lst!=null && lst.size()>0){
//                   HashMap masterMap = (HashMap)lst.get(0);

//                }





            } 
            
//            else {
//                
//                
//                
//                HashMap schemeMap = new HashMap();
//                System.out.println("###########FLAG" + gdsApplicationTO.getGroupNo());
//                schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
//                HashMap individualSchemeMap = new HashMap();
//                if (list != null && list.size() > 0) {
//                    for (int i = 0; i < list.size(); i++) {
//                        individualSchemeMap = (HashMap) list.get(i);
//                        System.out.println("schemename"+individualSchemeMap);
//                        HashMap mapData = new HashMap();
//                        HashMap mapData1 = new HashMap();
//                        String strPrefix = "";
//                        String strNum = "";
//                        int len = 13;
//                        int nofOfMember = 0;
//                        int suffix = 0;
//                        int lastValue = 0;
//                        int minDivisionNo = 0;
//                        int maxNofCoMember = 0;
//                        GDSApplicationTO tempGDSApplicationTO = gdsApplicationTO;
//                        System.out.println("schme name"+CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        HashMap whereSchemeMap = new HashMap();
//                        whereSchemeMap.put("SCHEME_NAME", tempGDSApplicationTO.getSchemeName());
//                        List lst = (List) ServerUtil.executeQuery("getGDSChittalNOUISide", whereSchemeMap);
//                        if (lst != null && lst.size() > 0) {
//                            mapData = (HashMap) lst.get(0);
//                        }
//                        if (mapData.containsKey("PREFIX")) {
//                            strPrefix = (String) mapData.get("PREFIX");
//                        }
//                        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//                        int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//                        nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
//                        maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
//                        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
//                        int numFrom = strPrefix.trim().length();
//                        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//                        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
//                        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//                        tempGDSApplicationTO.setChittalNo(genID);
//                        sqlMap.executeUpdate("insertGDSApplicationTOForNo", tempGDSApplicationTO);
//                    }}
//             //  
//            }
            //Changed By Suresh
         
            //Added by sreekrishnan
//            if (objSMSSubscriptionTO != null) {
//                System.out.println("objSMSSubscriptionTO%$%$%$%$%$%%"+objSMSSubscriptionTO);
//                updateSMSSubscription(gdsApplicationTO);
//            }
            HashMap mapData1=new HashMap();
             mapData1.put("GDS_NO",gdsApplicationTO.getGroupNo());
             mapData1.put("NEXT_GDS_NO",CommonUtil.convertObjToInt(gdsApplicationTO.getNextGDSNo()));
             sqlMap.executeUpdate("updateGDSNxtNum", mapData1);
            objLogDAO.addToLog(objLogTO);
            
           
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
//
//    private void insertData1(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
//        try {
//
//            String generateSingleCashId = "";
//            String generateSingleTransId = generateLinkID();
//            gdsApplicationTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
//            gdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
////            gdsApplicationTO.setChittalNo(getChittalNo());
////            updateChittalNo();
//            ArrayList transferList = new ArrayList();
//            TransferTrans transferTrans = new TransferTrans();
//            TxTransferTO transferTo = new TxTransferTO();
//            ArrayList TxTransferTO = new ArrayList();
//            TransactionTO transactionTO = new TransactionTO();
//            HashMap txMap = new HashMap();
//            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
////            double totalAmt = CommonUtil.convertObjToDouble(gdsApplicationTO.getInstAmt()).doubleValue();
//            transferTrans.setInitiatedBranch(BRANCH_ID);
//            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//            transactionDAO.setInitiatedBranch(_branchCode);
//            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
//            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//            if (gdsApplicationTO.getIsTran().equals("Y")) {
//                
//                
//                
//                if (map.containsKey("TransactionTO")) {
//                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
//                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                    }
//                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
//                    double totalAmt = 0.0;
//                    totalAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
////                    HashMap applicationMap = new HashMap();
////                    applicationMap.put("SCHEME_NAME", gdsApplicationTO.getSchemeName());
////                    List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
////                    if (lst != null && lst.size() > 0) {
////                        applicationMap = (HashMap) lst.get(0);
////                    }
//                    List lst=null;
//                    if (transactionTO.getTransType().equals("TRANSFER")) {
//                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                        String linkBatchId = CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo());
//                        HashMap debitMap = new HashMap();
//                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
//                            debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
//                           lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
//                            if (lst != null && lst.size() > 0) {
//                                debitMap = (HashMap) lst.get(0);
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
//                            }
//                        }
//                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("AD")) {
//                            debitMap.put("prodId", transactionTO.getProductId());
//                            lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
//                            if (lst != null && lst.size() > 0) {
//                                debitMap = (HashMap) lst.get(0);
//                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
//                            }
//                        }
//
//                        if (totalAmt > 0) {
//                            txMap = new HashMap();
//                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
//                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
//                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
//                            } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("OA")) {
//                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
//                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
//                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
//                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
//                                txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
//                            }
//                            HashMap susMap = new HashMap();                // Suspense Acc Head
//                            susMap.put("PROD_ID", transactionTO.getProductId());
//                            List susLst = sqlMap.executeQueryForList("getAccountHeadProdSA", susMap);
//                            if (susLst != null && susLst.size() > 0) {
//                                susMap = (HashMap) susLst.get(0);
//                                if (transactionTO.getProductType() != null && !transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("SA")) {
//                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(susMap.get("AC_HEAD")));
//                                    txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
//                                    txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
//                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
//                                    txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());
//                                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
//                                }
//                            }
//                            //Babu commented for the mantis id : 7509
//                            //txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
//                            //txMap.put(TransferTrans.DR_BRANCH, transactionTO.getBranchId());//commented by sreekrishnan and added lines in line no.319 and no.313
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                            txMap.put("DR_INST_TYPE", "VOUCHER");
//                            txMap.put(TransferTrans.PARTICULARS, gdsApplicationTO.getGroupName() + "-" + CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()
//                                    + "_" + gdsApplicationTO.getSubNo()));
//                            if (transactionTO.getProductType().equals("OA")) {
//                                txMap.put("TRANS_MOD_TYPE", "OA");
//                            } else if (transactionTO.getProductType().equals("AB")) {
//                                txMap.put("TRANS_MOD_TYPE", "AB");
//                            } else if (transactionTO.getProductType().equals("SA")) {
//                                txMap.put("TRANS_MOD_TYPE", "SA");
//                            } else if (transactionTO.getProductType().equals("TL")) {
//                                txMap.put("TRANS_MOD_TYPE", "TL");
//                            } else if (transactionTO.getProductType().equals("AD")) {
//                                txMap.put("TRANS_MOD_TYPE", "AD");
//                            } else {
//                                txMap.put("TRANS_MOD_TYPE", "GL");
//                            }
//                            System.out.println("txMap : " + txMap + "totalAmt :" + totalAmt);
//                            transferTo = transactionDAO.addTransferDebitLocal(txMap, totalAmt);
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                            if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
//                                transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
//                            }
//                            System.out.println("transferTo List 1 : " + transferTo);
//                            transferTo.setSingleTransId(generateSingleTransId);
//                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
//                            TxTransferTO.add(transferTo);
//                            
//                            
//                            // modify code for credit side transaction for each scheme
//                            HashMap schemeMap = new HashMap();
//                            schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                             List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
//                             totalAmt=totalAmt/list.size();
//                             for (int i = 0; i < list.size(); i++) {
//                              HashMap   individualSchemeMap = (HashMap) list.get(i);
//                            HashMap applicationMap = new HashMap();
//                            applicationMap.put("SCHEME_NAME", individualSchemeMap.get("SCHEME_NAME"));
//                            
//                   // applicationMap.put("SCHEME_NAME", gdsApplicationTO.getSchemeName());
//                    List lst1 = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);
//                    if (lst1 != null && lst1.size() > 0) {
//                        applicationMap = (HashMap) lst1.get(0);
//                    }
//                            txMap = new HashMap();
//                            transferTo = new TxTransferTO();
//                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                            if (gdsApplicationTO.getThalayal().equals("Y")) {
//                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
//                            } else if (gdsApplicationTO.getMunnal().equals("Y")) {
//                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
//                            } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
//                                txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
//                            } else {
//                                if (CommonUtil.convertObjToStr(applicationMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
//                                    txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("SUSPENSE_HEAD"));
//                                } else {
//                                    HashMap suspenseMap = new HashMap();                // Suspense Acc Head
//                                    suspenseMap.put("PROD_ID", applicationMap.get("SUSPENSE_PROD_ID"));
//                                    List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
//                                    if (suspenseLst != null && suspenseLst.size() > 0) {
//                                        suspenseMap = (HashMap) suspenseLst.get(0);
//                                        txMap.put(TransferTrans.CR_AC_HD, suspenseMap.get("AC_HEAD"));
//                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
//                                        txMap.put(TransferTrans.CR_PROD_ID, applicationMap.get("SUSPENSE_PROD_ID"));
//                                        txMap.put(TransferTrans.CR_ACT_NUM, applicationMap.get("SUSPENSE_ACC_NO"));
//                                    }
//                                }
//                            }
//                            txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
//                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
//                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
//                            txMap.put("TRANS_MOD_TYPE", "MDS");
//                            System.out.println("txMap : " + txMap + "serviceAmt :" + totalAmt);
//                            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt);
//                            transferTo.setTransId("-");
//                            transferTo.setBatchId("-");
//                            transferTo.setInitiatedBranch(BRANCH_ID);
//                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//
//                            if (!transactionTO.getProductType().equals("") && !transactionTO.getProductType().equals("GL") && !transactionTO.getProductId().equals("")) {
//                                transferTo.setLinkBatchId(transactionTO.getDebitAcctNo());
//                            }
//                            System.out.println("transferTo List 2 : " + transferTo);
//                            transferTo.setSingleTransId(generateSingleTransId);
//                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
//                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                            TxTransferTO.add(transferTo);
//                            
//                        }
//                        }
//                        HashMap applnMap = new HashMap();
//                        transferDAO = new TransferDAO();
//                        map.put("MODE", gdsApplicationTO.getCommand());
//                        map.put("COMMAND", map.get("MODE"));
//                        map.put("TxTransferTO", TxTransferTO);
//                        // map.put("TRANS_MOD_TYPE_MMBS", CommonConstants.MMBS);
//                        HashMap transMap = transferDAO.execute(map, false);
//                        gdsApplicationTO.setTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
//                        System.out.println("transMap AFTER : " + transMap + "totalAmt :" + totalAmt);
//                        transactionTO.setChequeNo("SERVICE_TAX");
//                        transactionDAO.setBatchId(gdsApplicationTO.getTransId());
//                        transactionDAO.setBatchDate(currDt);
//                        transactionDAO.execute(map);
////                    HashMap remitMap = new HashMap();
////                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
////                    if(lst!=null && lst.size()>0){
////                        remitMap = (HashMap)lst.get(0);
////                        if(!map.containsKey("LOCKER_SURRENDER_DAO")){
////                            remitMap.put("BATCH_ID", transMap.get("TRANS_ID"));
////                        }else{
////                            remitMap.put("BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
////                        }
////                        remitMap.put("BATCH_DT", currDt);
////                        System.out.println("remitMap : " + remitMap);
////                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", remitMap);
////                    }
//                        HashMap linkBatchMap = new HashMap();
//                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
//                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
//                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
//                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
//                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));//changed By Nidhin transactionTO.getDebitAcctNo()
//                        } else {
//                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
//                        }
//                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_DT", currDt);
//                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
//                        //Changed by sreekrishnan
//                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")) {
//                            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
//                        }
//                        linkBatchMap = null;
//                        transMap = null;
//                        //Added by sreekrishnan
//                        //if (map.containsKey("PREDEFINED_INSTALL_BONUS") && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0){
//                        //     commonTransactionCashandTransfer(map,gdsApplicationTO,generateSingleTransId,applicationMap);
//                        //}
//                    } 
//                    else if (transactionTO.getTransType().equals("CASH")) {
//                        
//                        HashMap schemeMap = new HashMap();
////                        HashMap cashMap  = new HashMap();
//                        schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                        List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
////                             totalAmt=totalAmt/list.size();
////                   //          CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
////                             for (int i = 0; i < list.size(); i++) {
////                              HashMap   individualSchemeMap = (HashMap) list.get(i);
//                            HashMap applicationMap = new HashMap();
//                        generateSingleCashId = generateLinkID();
//                         totalAmt=totalAmt/list.size();
//                        transactionTO.setChequeNo("SERVICE_TAX");
//                        applicationMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
//                        applicationMap.put("SCHEME_NAME", gdsApplicationTO.getGroupName());
//                        applicationMap.put("CHIT_NO", gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo());
//                        applicationMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
//                        applicationMap.put("TRANS_MOD_TYPE", "MDS");
//                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
//                        ArrayList applncashList = applicationList(map, applicationMap, String.valueOf(totalAmt));
//                        applicationMap.put("DAILYDEPOSITTRANSTO", applncashList);
//                        applicationMap.put("SINGLE_TRANS_ID", generateSingleCashId);
//                        //applicationMap.put("TRANS_MOD_TYPE", "MDS");
//                         
//                        HashMap cashMap = cashTransactionDAO.execute(applicationMap, false);
//                        cashMap = cashTransactionDAO.execute(applicationMap, false);
//                     //   }//for loop closinf for cash
//                        System.out.println("cashMap :" + cashMap);
//                        gdsApplicationTO.setTransId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                        String command = CommonUtil.convertObjToStr(gdsApplicationTO.getCommand());
//                        transactionTO.setBatchId(CommonUtil.convertObjToStr(cashMap.get("TRANS_ID")));
//                        transactionTO.setBatchDt(gdsApplicationTO.getStatusDt());
//                        map.put("MODE", "INSERT");
//                        
//                        transactionDAO.setBatchId(gdsApplicationTO.getTransId());
//                        transactionDAO.setBatchDate(currDt);
//                        transactionDAO.execute(map);
//                             
//
////                    HashMap debitMap = new HashMap();
////                    lst = sqlMap.executeQueryForList("getSelectMaxTransIdForCash",null);
////                    if(lst!=null && lst.size()>0){
////                        debitMap = (HashMap)lst.get(0);
////                        if(!applicationMap.containsKey("LOCKER_SURRENDER_DAO"))
////                            debitMap.put("BATCH_ID", cashMap.get("TRANS_ID"));
////                        else
////                            debitMap.put("BATCH_ID", applicationMap.get("LOCKER_SURRENDER_ID"));
////                        debitMap.put("BATCH_DT", currDt);
////                        debitMap.put("TRANS_DT",currDt);
////                        debitMap.put("INITIATED_BRANCH",map.get("BRANCH_CODE"));
////                        sqlMap.executeUpdate("updateRemitIssueTableCashBatchId", debitMap);
////                        debitMap = null;
////                        
////                        
////                        
////                    }
//                        HashMap linkBatchMap = new HashMap();
//                        linkBatchMap.put("LINK_BATCH_ID", cashMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_ID", cashMap.get("TRANS_ID"));
//                        linkBatchMap.put("TRANS_DT", currDt);
//                        linkBatchMap.put("INITIATED_BRANCH", BRANCH_ID);
//                        
//                        sqlMap.executeUpdate("updateLinkBatchIdCash", linkBatchMap);
//                        linkBatchMap = null;
//                        //Added by sreekrishnan
//                        //if (map.containsKey("PREDEFINED_INSTALL_BONUS") && CommonUtil.convertObjToDouble(map.get("PREDEFINED_INSTALL_BONUS"))>0){
//                        //    commonTransactionCashandTransfer(map,gdsApplicationTO,generateSingleCashId,applicationMap);
//                        //}
//                   
//                             }
//                             
//                }
//            }
//            String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
//            if (gdsApplicationTO.getNominee().equals("Y")) {
//                ArrayList nomineeTOList = new ArrayList();
//                if (map.containsKey("AccountNomineeTO")) {
//                    nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
//                }
//                ArrayList nomineeDeleteList = new ArrayList();
//                if (map.containsKey("AccountNomineeDeleteTO")) {
//                    nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
//                }
//                if (nomineeTOList != null && nomineeTOList.size() > 0) {
//                    NomineeDAO objNomineeDAO = new NomineeDAO();
//                    //                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
//                    objNomineeDAO.insertData(nomineeTOList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
//                    if (nomineeDeleteList != null) {
//                        objNomineeDAO.insertData(nomineeDeleteList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), true, USERID, "DEPOIST", objLogTO, objLogDAO);
//                    }
//                }
//            }
//
//            if (gdsApplicationTO.getIsTran().equals("N")) {
//                gdsApplicationTO.setTransId("");
//                //generating multiple chittal number based on  schema for particular Group GDS
//                HashMap schemeMap = new HashMap();
//                System.out.println("###########FLAG" + gdsApplicationTO.getGroupNo());
//                schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
//                HashMap individualSchemeMap = new HashMap();
//                if (list != null && list.size() > 0) {
//                    for (int i = 0; i < list.size(); i++) {
//                        individualSchemeMap = (HashMap) list.get(i);
//                        System.out.println("schemename"+individualSchemeMap);
//                        HashMap mapData = new HashMap();
//                        HashMap mapData1 = new HashMap();
//                        String strPrefix = "";
//                        String strNum = "";
//                        int len = 13;
//                        int nofOfMember = 0;
//                        int suffix = 0;
//                        int lastValue = 0;
//                        int minDivisionNo = 0;
//                        int maxNofCoMember = 0;
//                        GDSApplicationTO tempGDSApplicationTO = gdsApplicationTO;
//                        System.out.println("schme name"+CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        HashMap whereSchemeMap = new HashMap();
//                        whereSchemeMap.put("SCHEME_NAME", tempGDSApplicationTO.getSchemeName());
//                        List lst = (List) ServerUtil.executeQuery("getGDSChittalNOUISide", whereSchemeMap);
//                        if (lst != null && lst.size() > 0) {
//                            mapData = (HashMap) lst.get(0);
//                        }
//                        if (mapData.containsKey("PREFIX")) {
//                            strPrefix = (String) mapData.get("PREFIX");
//                        }
//                        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//                        int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//                        nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
//                        maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
//                        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
//                        int numFrom = strPrefix.trim().length();
//                        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//                        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
//                        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//                        tempGDSApplicationTO.setChittalNo(genID);
//                        sqlMap.executeUpdate("insertGDSApplicationTOForNo", tempGDSApplicationTO);
//
//                        MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
//                        MaintenanceTO.setSchemeName(tempGDSApplicationTO.getSchemeName());
//                        MaintenanceTO.setChittalNo(tempGDSApplicationTO.getChittalNo());
//                        MaintenanceTO.setSubNo(tempGDSApplicationTO.getSubNo());
//                        MaintenanceTO.setDivisionNo(String.valueOf(tempGDSApplicationTO.getNoOfDivisions()));
//                        MaintenanceTO.setChitStartDt(tempGDSApplicationTO.getSchemeStartDt());
//                        MaintenanceTO.setMemberNo(tempGDSApplicationTO.getMembershipNo());
//                        MaintenanceTO.setMemberName(tempGDSApplicationTO.getMembershipName());
//                        MaintenanceTO.setNominee(tempGDSApplicationTO.getNominee());
//                        //MaintenanceTO.setNomineeName(gdsApplicationTO.getN);
//                        MaintenanceTO.setMemberType(tempGDSApplicationTO.getMembershipType());
//                        MaintenanceTO.setSalaryRecovery(tempGDSApplicationTO.getSalaryRecovery());
//                        MaintenanceTO.setBranchCode(tempGDSApplicationTO.getBranchCode());
//                        MaintenanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
//                        MaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
//                        MaintenanceTO.setStatusDt(currDt);
//                        MaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
//                        MaintenanceTO.setAuthorizedBy((String) map.get(CommonConstants.USER_ID));
//                        MaintenanceTO.setAuthorizedDt(currDt);
//                        MaintenanceTO.setBondSet("N");
//                        MaintenanceTO.setApplicationSet("N");
////                    MaintenanceTO.setBondNo(getChittalBondNO());
//                        mapData1.put("GDS_NO",gdsApplicationTO.getGroupNo());
//                        mapData1.put("NEXT_GDS_NO",gdsApplicationTO.getNextGDSNo());
//                        sqlMap.executeUpdate("updateGDSNxtNum", mapData1);
//                        sqlMap.executeUpdate("insertMDSMasterMaintenanceTO", MaintenanceTO);
//                        System.out.println("temp data"+tempGDSApplicationTO.getSchemeName());
//                        if (tempGDSApplicationTO.getCoChittal().equals("N")) {
//                            updateChittalNo(tempGDSApplicationTO);
//                        } else {
//                            UpdateCoChittalNo(map);
//                        }
//
//                    }
//                }
//                //enf of code
////                  System.out.println("@$#$@$%@$#masterMap :" +);                     // INSERT MASTER MAINTANANCE DETAILS
////                List lst = ServerUtil.executeQuery("getApplDetails",masterMap);
////                if(lst!=null && lst.size()>0){
////                   HashMap masterMap = (HashMap)lst.get(0);
//
////                }
//
//
//
//
//
//            } else {
//                
//                
//                
//                HashMap schemeMap = new HashMap();
//                System.out.println("###########FLAG" + gdsApplicationTO.getGroupNo());
//                schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
//                HashMap individualSchemeMap = new HashMap();
//                if (list != null && list.size() > 0) {
//                    for (int i = 0; i < list.size(); i++) {
//                        individualSchemeMap = (HashMap) list.get(i);
//                        System.out.println("schemename"+individualSchemeMap);
//                        HashMap mapData = new HashMap();
//                        HashMap mapData1 = new HashMap();
//                        String strPrefix = "";
//                        String strNum = "";
//                        int len = 13;
//                        int nofOfMember = 0;
//                        int suffix = 0;
//                        int lastValue = 0;
//                        int minDivisionNo = 0;
//                        int maxNofCoMember = 0;
//                        GDSApplicationTO tempGDSApplicationTO = gdsApplicationTO;
//                        System.out.println("schme name"+CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        tempGDSApplicationTO.setSchemeName(CommonUtil.convertObjToStr(individualSchemeMap.get("SCHEME_NAME")));
//                        HashMap whereSchemeMap = new HashMap();
//                        whereSchemeMap.put("SCHEME_NAME", tempGDSApplicationTO.getSchemeName());
//                        List lst = (List) ServerUtil.executeQuery("getGDSChittalNOUISide", whereSchemeMap);
//                        if (lst != null && lst.size() > 0) {
//                            mapData = (HashMap) lst.get(0);
//                        }
//                        if (mapData.containsKey("PREFIX")) {
//                            strPrefix = (String) mapData.get("PREFIX");
//                        }
//                        suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//                        int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//                        nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
//                        maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
//                        lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
//                        int numFrom = strPrefix.trim().length();
//                        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//                        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
//                        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//                        tempGDSApplicationTO.setChittalNo(genID);
//                        sqlMap.executeUpdate("insertGDSApplicationTOForNo", tempGDSApplicationTO);
//                    }}
//             //  sqlMap.executeUpdate("insertGDSApplicationTOForNo", gdsApplicationTO);
//            }
//            //Changed By Suresh
//         
//            //Added by sreekrishnan
////            if (objSMSSubscriptionTO != null) {
////                System.out.println("objSMSSubscriptionTO%$%$%$%$%$%%"+objSMSSubscriptionTO);
////                updateSMSSubscription(gdsApplicationTO);
////            }
//
//            objLogDAO.addToLog(objLogTO);
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
//    }

    private void updateSMSSubscription(GDSApplicationTO gdsApplicationTO) throws Exception {
        objSMSSubscriptionTO.setStatusBy(gdsApplicationTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(getProperDateFormat(gdsApplicationTO.getStatusDt()));
        if (objSMSSubscriptionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
        } else {
            objSMSSubscriptionTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objSMSSubscriptionTO.setAuthorizedBy(gdsApplicationTO.getStatusBy());
        }
//        deleteSMSSubscription(gdsApplicationTO);
        sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
    }

    private void deleteSMSSubscription(GDSApplicationTO gdsApplicationTO) throws Exception {
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        checkMap.put("PROD_ID", gdsApplicationTO.getGroupName());
        checkMap.put("ACT_NUM", gdsApplicationTO.getChittalNo());
        checkMap.put("STATUS_DT", getProperDateFormat(gdsApplicationTO.getStatusDt()));
        checkMap.put(CommonConstants.USER_ID, gdsApplicationTO.getStatusBy());
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", checkMap);
        checkMap.clear();
        checkMap = null;
    }

    private void authorizeSMSSubscription(HashMap map) throws Exception {
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        authMap.put("PROD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
        authMap.put("PROD_ID", gdsApplicationTO.getGroupName());
        authMap.put("ACCOUNTNO", gdsApplicationTO.getChittalNo());
        authMap.put(CommonConstants.USER_ID, gdsApplicationTO.getStatusBy());
        authMap.put("STATUS", authMap.get("AUTHORIZESTATUS"));
        authMap.put(CommonConstants.AUTHORIZEDT, getProperDateFormat(currDt));
        System.out.println("authMap$#$#%#%#%#%" + authMap);
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

    private ArrayList applicationList(HashMap map, HashMap applnMap,List list1, String totalAmt) throws Exception {
        
        
//        HashMap schemeMap = new HashMap();
//                        HashMap cashMap  = new HashMap();
//                            schemeMap.put("GROUP_NO", gdsApplicationTO.getGroupNo());
//                             List list = sqlMap.executeQueryForList("getGDSSelectGroupMDSDepositTO", schemeMap);
////                             totalAmt=totalAmt/list.size();
//                   //          CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
//                             for (int i = 0; i < list.size(); i++) {
//                              HashMap   individualSchemeMap = (HashMap) list.get(i);
        System.out.println("applnMap :" + applnMap);
        HashMap schemeMap = new HashMap();
        
        ArrayList cashList = new ArrayList();
        if (gdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            
            for(int i=0;i<list1.size();i++)
            {
             applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));                     
             applnMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
             applnMap.put("TRANS_MOD_TYPE", "MDS");   
             schemeMap=(HashMap)list1.get(i); 
             // nithya
             
             List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                    if (headlst != null && headlst.size() > 0) {
                        applnMap = (HashMap) headlst.get(0);
                    }
             // nithya end       
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
            if (CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                objCashTO.setProdType("GL");
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_HEAD")));
            } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("RECEIPT_HEAD")));
            } else {
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
            objCashTO.setStatusDt(gdsApplicationTO.getStatusDt());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
           // objCashTO.setParticulars(CommonUtil.convertObjToStr(schemeMap.get("SCHEME_NAME")) + "-" + CommonUtil.convertObjToStr(applnMap.get("CHIT_NO")));
            objCashTO.setParticulars(CommonUtil.convertObjToStr(schemeMap.get("CHIT_NO"))); // Removed scheme name from particulars by nithya on 03-08-2016 for 4983
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(schemeMap.get("CHIT_NO")));
            objCashTO.setCommand(gdsApplicationTO.getCommand());
            objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
            objCashTO = null;
        } 
        }
        return cashList;
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        System.out.println("objCashTO 1st one------<>" + chargesMap);
        GDSApplicationTO mdsAppTO = new GDSApplicationTO();
        String USERID = CommonUtil.convertObjToStr(chargesMap.get(CommonConstants.USER_ID));
        ArrayList nomineeTOList = (ArrayList) chargesMap.get("AccountNomineeTO");
        ArrayList nomineeDeleteList = (ArrayList) chargesMap.get("AccountNomineeDeleteTO");

        //Updating sms subcriprion
        System.out.println("objSMSSubscriptionTO%#%#%#%" + objSMSSubscriptionTO);
        if (chargesMap.containsKey("SMSSubscriptionTO")) {
            updateSMSSubscription(gdsApplicationTO);
        } else {
            deleteSMSSubscription(gdsApplicationTO);
        }

        // Update the data regarding the NomineeTable...
        if (nomineeTOList != null) {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            if (nomineeTOList.size() > 0) {
                System.out.println("Nominee List " + nomineeTOList);
                objNomineeDAO.deleteData(gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), SCREEN);
                objNomineeDAO.insertData(nomineeTOList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
            }
            if (nomineeDeleteList != null) {
                objNomineeDAO.insertData(nomineeDeleteList, gdsApplicationTO.getChittalNo() + "_" + gdsApplicationTO.getSubNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
            }
        }
        if (chargesMap.containsKey("NO_TRANSACTION")) {
            HashMap dataMap = new HashMap();
            dataMap.put("MEMBER_TYPE", gdsApplicationTO.getMembershipType());
            dataMap.put("MEMBER_NO", gdsApplicationTO.getMembershipNo());
            dataMap.put("MEMBER_NAME", gdsApplicationTO.getMembershipName());
            dataMap.put("CUST_ID", gdsApplicationTO.getCudt_id());
            dataMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
            gdsApplicationTO.setMembershipName(gdsApplicationTO.getMembershipName());
            gdsApplicationTO.setMembershipNo(gdsApplicationTO.getMembershipNo());
            gdsApplicationTO.setMembershipType(gdsApplicationTO.getMembershipType());
            gdsApplicationTO.setChittalNo(gdsApplicationTO.getChittalNo());
            gdsApplicationTO.setCudt_id(gdsApplicationTO.getCudt_id());
            //mdsApplicationTO.executeUpdate("updateMDSApplication", dataMap);
        }
        HashMap statusMap = new HashMap();
        String status = "";
//        statusMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(gdsApplicationTO.getChittalNo()));
//        List stsList = sqlMap.executeQueryForList("getStatusMDSApp", statusMap);
        statusMap.put("GDS_NO", CommonUtil.convertObjToStr(gdsApplicationTO.getGds_No()));
        List stsList = sqlMap.executeQueryForList("getStatusGDSApp", statusMap);
        if (stsList != null && stsList.size() > 0) {
            status = CommonUtil.convertObjToStr(((HashMap) stsList.get(0)).get("STATUS"));
        }
        statusMap.put("TRANS_ID", null);
        List mdsAppsList = sqlMap.executeQueryForList("getSelectGDSApplicationTO", statusMap);
        System.out.println("getSelectGDSApplicationTOmdsAppsList" + mdsAppsList);
        if (mdsAppsList != null && mdsAppsList.size() > 0) {
            mdsAppTO = (GDSApplicationTO) mdsAppsList.get(0);
        }
        if (status.equals(CommonConstants.STATUS_CREATED) && mdsAppTO.getAuthorizeStatus() != null) {
            if (mdsAppTO.getAuthorizeStatus().equals(CommonConstants.STATUS_AUTHORIZED)) {
                gdsApplicationTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else {
                gdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
            }
        } else {
            gdsApplicationTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        gdsApplicationTO.setStatusBy(CommonUtil.convertObjToStr(chargesMap.get(CommonConstants.USER_ID)));
        //gdsApplicationTO.setAuthorizeStatus("");
        System.out.println("gdsApplicationTO :" + gdsApplicationTO);
        sqlMap.executeUpdate("updateGDSApplicationTO", gdsApplicationTO);
        sqlMap.executeUpdate("updateMdsMasterMaintenance", gdsApplicationTO);
        sqlMap.executeUpdate("updateMdsPrizedMoneyDetails", gdsApplicationTO);
        sqlMap.executeUpdate("updateMdsMemberReceiptEntry", gdsApplicationTO);
        sqlMap.executeUpdate("updateMdsReceiptEntry", gdsApplicationTO);
        if (!chargesMap.containsKey("NO_TRANSACTION")) {
            HashMap a = new HashMap();
            a.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
            List list = (List) sqlMap.executeQueryForList("gdsApplicationTO", a);
            System.out.println("mdsApplsadasdicationTO" + list);

            HashMap schNamMap = new HashMap();
            schNamMap.put("SCHEME_NAME", gdsApplicationTO.getGroupName());
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
                            amap.put("CURRDATE", gdsApplicationTO.getStatusDt());
                            amap.put("BRANCH_CODE", chargesMap.get("BRANCH_CODE"));
                            sqlMap.executeUpdate("updateRemitIssueTransAppl", amap);
                        }
                    }
                }
            }
        }
        //  gdsApplicationTO  
    }

    private ArrayList applicationUpdate(HashMap chargesMap) throws Exception {//updating Transaction
        System.out.println("chargesMap :" + chargesMap);
        ArrayList cashList = new ArrayList();
        //        ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
        //        objChargesServiceTaxTO = (ChargesServiceTaxTO)chargesMap.get("ChargesServiceTaxTO");
        //        System.out.println("objCashTransactionTO :"+objChargesServiceTaxTO);
        if (gdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            HashMap tempMap = (HashMap) chargesMap.get("SERVICE_TAX_AMT_TRANSACTION");
            String trans_id = CommonUtil.convertObjToStr(gdsApplicationTO.getTransId());
            CashTransactionTO objCashTO = new CashTransactionTO();
            //            List lst = sqlMap.executeQueryForList("getSelectENTERED_AMOUNT",trans_id);
            //            if(lst!=null && lst.size()>0){
            objCashTO = new CashTransactionTO();
            //                objCashTO = (CashTransactionTO) lst.get(0);
            objCashTO.setInpAmount(gdsApplicationTO.getInstallmentAmount());
            objCashTO.setAmount(gdsApplicationTO.getInstallmentAmount());
            objCashTO.setProdType(CommonUtil.convertObjToStr("GL"));
            objCashTO.setInstrumentNo2("MDS_APPLICATION");
            objCashTO.setCommand(gdsApplicationTO.getCommand());
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
        double totalAmt = CommonUtil.convertObjToDouble(gdsApplicationTO.getInstallmentAmount()).doubleValue();
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
            gdsApplicationTO.setStatus(CommonConstants.STATUS_DELETED);
            if (transactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("Inside TRANSFER ");
                if (chargesMap.containsKey("AMT_TRANSACTION") && CommonUtil.convertObjToDouble(gdsApplicationTO.getInstallmentAmount()).doubleValue() > 0) {
                    tempMap = (HashMap) chargesMap.get("AMT_TRANSACTION");
                    System.out.println("#### tempMap : " + tempMap);
                    oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("TRANS_DT", currDt);
                    tempMap.put("INITIATED_BRANCH", BRANCH_ID);
                    tempMap.remove("TRANS_ID");
                    tempMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(BRANCH_ID));
                    double newAmount = CommonUtil.convertObjToDouble(gdsApplicationTO.getInstallmentAmount()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    String commandMode = CommonUtil.convertObjToStr(gdsApplicationTO.getCommand());
                    if (gdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            for (int i = 0; i < lst.size(); i++) {
                                txTransferTO = (TxTransferTO) lst.get(i);
                                if (gdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
        System.out.println("gdsApplicationTO :" + gdsApplicationTO);
        gdsApplicationTO.setAuthorizeStatus("DELETED");
        sqlMap.executeUpdate("updateGDSApplicationTO", gdsApplicationTO);
        sqlMap.executeUpdate("updateMDSShadowCreditDrAmt", gdsApplicationTO);
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
//         AuthorizeMap :{DB_DRIVER_NAME=oracle.jdbc.driver.OracleDriver, IP_ADDR=192.168.0.103, USER_ID=sysadmin,
//         MEMBER_GDSNO=0001GDS04600051, UPDATE_CHIT_NO=, AMT_TRANSACTION={TRANS_ID=C0100218, AMOUNT=4000}, 
//         SCREEN=null, AUTHORIZEMAP={USER_ID=sysadmin, AUTHORIZESTATUS=REJECTED, BRANCH_CODE=0001, 
//         AUTHORIZEDT=2017-04-24 00:00:00.0, 
//         AUTHORIZEDATA=[{TRANS_ID=C0100218}]}, 
//         TransactionTO={NOT_DELETED_TRANS_TOs={1=<%%>command=null<%%>class=TransactionTO<%%>
//         package=com.see.truetransact.transferobject.common.transaction.TransactionTO<%%>null
//                 <%%>key=batchId+transId<%%>value=C0100218+TXD18002<%%><%%>batchId=C0100218<%%>
//         batchDt=2017-04-24 00:00:00.0<%%>transId=TXD18002<%%>applName=Lazar C R<%%>transType=CASH
//         <%%>transAmt=12000.0<%%>productId=null<%%>productType=null<%%>debitAcctNo=null<%%>
//         chequeNo=SERVICE_TAX<%%>chequeNo2=null<%%>chequeDt=2017-04-24 00:00:00.0<%%>status=CREATED
//         <%%>instType=WITHDRAW_SLIP<%%>tokenNo=null<%%>branchId=0001<%%>particulars=null<%%>}}, 
//         MASTER_DATA={GROUP_NAME=FirstGroup, SUB_NO=0, GDS_NO=0001GDS04600051}, 
//         BRANCH_CODE=0001, MODULE=null, BANK_CODE=682, 
//         gdsApplicationTO=<%%>command=<%%>class=GDSApplicationTO<%%>
//         package=com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO<%%>
//         null<%%>key=<%%>value=<%%><%%>schemeCount=0<%%>groupName=FirstGroup<%%>gds_No=0001GDS04600051
//         <%%>groupNo=GDS046<%%>chitNo=null<%%>noOfDivisions=0<%%>chittalNo=<%%>subNo=0<%%>
//         chitStartDt=Tue Aug 01 00:00:00 IST 2017<%%>chitEndDt=Fri Sep 01 00:00:00 IST 2017<%%>
//         installmentAmount=4000.0<%%>applnNo=1.0<%%>applnDate=Mon Apr 24 00:00:00 IST 2017<%%>
//         thalayal=N<%%>munnal=N<%%>coChittal=N<%%>membershipNo=AA61<%%>membershipType=AA<%%>
//         membershipName=Lazar C R<%%>houseStNo=Cherukkaran<%%>area=Moospet Road, Chelakkottukara<%%>
//         city=3- Nadathara<%%>state=Kerala<%%>pin=680005.0<%%>standingInstn=N<%%>nominee=N<%%>remarks=null
//                 <%%>prodType=<%%>prodId=<%%>drAccNo=<%%>salaryRecovery=<%%>status=<%%>statusBy=<%%>
//         statusDt=2017-04-24 00:00:00.0<%%>authorizeStatus=<%%>authorizeBy=<%%>authorizeDt=null<%%>
//         branchCode=0001<%%>transId=C0100218<%%>totalBalance=null<%%>clearBalance=null<%%>availableBalance=null
//                 <%%>shadowCredit=null<%%>shadowDebit=null<%%>commencementStatus=<%%>commencementDate=null<%%>
//         commencementAuthStatus=<%%>lastTransDt=null<%%>commencementTransId=<%%>deletedUsed=<%%>instCount=null<%%>,
//                 COMMAND=null}
        HashMap masterMap = new HashMap();
        if (map.containsKey("MASTER_DATA")) {
            masterMap = (HashMap) map.get("MASTER_DATA");
            System.out.println("masterMap" + masterMap);
        }
//        HashMap hmap=new HashMap();
//        hmap.put("CHITTAL_NO",gdsApplicationTO.getChitNo());
        List stsList = sqlMap.executeQueryForList("getStatusGDSApp", masterMap);
        String sts = ((HashMap) stsList.get(0)).get("STATUS").toString();
        System.out.println("gdsApplicationTO....." + gdsApplicationTO.getStatus());
        

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
                HashMap authorizeMap = new HashMap();
                HashMap authorizeDataMap = new HashMap();
                authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                System.out.println("authorizeMap  :: " + authorizeMap);
                List authorizeDataList=(List) authorizeMap.get("AUTHORIZEDATA");
                authorizeDataMap=(HashMap)authorizeDataList.get(0);
                applnAuthMap.put("availableBalance",gdsApplicationTO.getAvailableBalance());
                applnAuthMap.put("TRANS_ID",authorizeDataMap.get("TRANS_ID") );
                String branchCode = CommonUtil.convertObjToStr(authorizeMap.get("BRANCH_CODE"));
                applnAuthMap.put("BRANCH_CODE", branchCode);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                  //  HashMap authorizeMap = new HashMap();
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
                    cashAuthMap = null;
                } else {
                    System.out.println("transactionTO.getTransType() :" + transactionTO.getTransType());
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    ArrayList arrList = new ArrayList();
         //           HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                    authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                    HashMap singleAuthorizeMap = new HashMap();
                    String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                    applnAuthMap.put("AUTHORIZE_STATUS", status);
                    singleAuthorizeMap.put("STATUS", status);
                    singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                    singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                    applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                 //   applnAuthMap.put("TRANS_ID", gdsApplicationTO.getTransId());
                    arrList.add(singleAuthorizeMap);
                    branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
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
//            if(gdsApplicationTO.getCoChittal().equals("N")){
//                updateChittalNo();
//            }else{
//                UpdateCoChittalNo();
//            }
                applnAuthMap.put("AUTHORIZE_DT", currDt);
               // applnAuthMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
                //applnAuthMap.put("SCHEME", gdsApplicationTO.getGroupName());
                applnAuthMap.put("SUB_NO", CommonUtil.convertObjToInt(gdsApplicationTO.getSubNo()));
                applnAuthMap.put("GDS_NO", gdsApplicationTO.getGds_No());
                if (gdsApplicationTO.getCudt_id() != null && gdsApplicationTO.getCudt_id().length() > 0) {
                    applnAuthMap.put("CUST_ID", gdsApplicationTO.getCudt_id());
                }
                System.out.println("0 " + gdsApplicationTO.getCudt_id());
                System.out.println("applnAuthMap :: " + applnAuthMap);
                sqlMap.executeUpdate("updateGDSAuthorizeStatusAppl", applnAuthMap);
                sqlMap.executeUpdate("updateGDSAvailBalance", applnAuthMap);
                applnAuthMap = null;
         //       HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                if (status.equals("AUTHORIZED")) {
                    System.out.println("@$#$@$%@$#masterMap :" + masterMap);
                    sqlMap.executeUpdate("updateGDSMasterMaintenanceTO", masterMap);// INSERT MASTER MAINTANANCE DETAILS
                    List lst = ServerUtil.executeQuery("getApplDetails", masterMap);
                    if (lst != null && lst.size() > 0) {
                        masterMap = (HashMap) lst.get(0);
                        MDSMasterMaintenanceTO MaintenanceTO = new MDSMasterMaintenanceTO();
                        
                        MaintenanceTO.setMemberGDSNo(CommonUtil.convertObjToStr(masterMap.get("GDS_NO")));
                        MaintenanceTO.setSchemeName(CommonUtil.convertObjToStr(masterMap.get("SCHEME_NAME")));
                        MaintenanceTO.setChittalNo(CommonUtil.convertObjToStr(masterMap.get("CHITTAL_NO")));
                        MaintenanceTO.setSubNo(CommonUtil.convertObjToInt(masterMap.get("SUB_NO")));
                        MaintenanceTO.setDivisionNo(CommonUtil.convertObjToInt(masterMap.get("DIVISION_NO")));
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
                        sqlMap.executeUpdate("updateGDSMasterMaintenanceTO", MaintenanceTO);
                    }
                }
                masterMap = null;
                authorizeMap = null;
            }
            
        } else {
            HashMap applnAuthMap = new HashMap();
            HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
            String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
            applnAuthMap.put("AUTHORIZE_STATUS", status);
            applnAuthMap.put("AUTHORIZE_DT", currDt);
            applnAuthMap.put("AUTHORIZE_BY", map.get("USER_ID"));
            applnAuthMap.put("TRANS_ID", gdsApplicationTO.getTransId());
            applnAuthMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
            applnAuthMap.put("SCHEME", gdsApplicationTO.getGroupName());
            applnAuthMap.put("SUB_NO", CommonUtil.convertObjToInt(gdsApplicationTO.getSubNo()));
            applnAuthMap.put("CUST_ID", gdsApplicationTO.getCudt_id());

            sqlMap.executeUpdate("updateGDSAuthorizeStatusAppl", applnAuthMap);
        }
        HashMap authorizeMap1 = (HashMap) map.get("AUTHORIZEMAP");
        String status = CommonUtil.convertObjToStr(authorizeMap1.get("AUTHORIZESTATUS"));
        if (status.equals("REJECTED")) {
            HashMap mapNew = new HashMap();
            HashMap authorizeMap = (HashMap) map.get("REJECTED");
            //String status = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
            mapNew.put("AUTHORIZE_STATUS", status);
            mapNew.put("AUTHORIZE_DT", currDt);
            mapNew.put("AUTHORIZE_BY", map.get("USER_ID"));
            mapNew.put("SCHEME_NAME", gdsApplicationTO.getGroupName());
            mapNew.put("GDS_NO", gdsApplicationTO.getGds_No());
            
          //  List dataList = sqlMap.executeQueryForList("updateGDSAuthorizeStatusAppl", mapNew);
//           List dataList = sqlMap.executeQueryForList("getGDSLastChittalNo", mapNew);
//            int lastno = 0;
//            for (int i = 0; i < dataList.size(); i++) {
//                HashMap newMap = (HashMap) dataList.get(0);
//                if (newMap != null && newMap.get("NEXT_CHITTAL_NO") != null) {
//                    String Last_no = newMap.get("NEXT_CHITTAL_NO").toString();
//                    lastno = CommonUtil.convertObjToInt(Last_no);
//                }
//            }
//            mapNew.put("CHITTAL_NO", lastno - 1);
//            sqlMap.executeUpdate("updateGDSLastChittalNo", mapNew);
        }
        map = null;
       
    }

    private void UpdateCoChittalNo(HashMap map) throws Exception {
        HashMap where = new HashMap();
        HashMap mapData = new HashMap();
        String strPrefix = "";
        double instAmt = 0.0;
        String value = CommonUtil.convertObjToStr(gdsApplicationTO.getGroupName());
        List lst = (List) sqlMap.executeQueryForList("getChittalNO", value);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            instAmt = CommonUtil.convertObjToDouble(mapData.get("INSTALLMENT_AMOUNT")).doubleValue();
            //String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            int nxtID = CommonUtil.convertObjToInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1;
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", value);
            whereMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
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
//                    sqlMap.executeUpdate("updateGDSChittalNxtNum", where);
//                }
                } else {
                    where.put("SCHEME_NAME", value);
                    where.put("VALUE", nxtID);
                    //where.put("SUFFIX", String.valueOf("1"));
                    where.put("SUFFIX", 1);
                    sqlMap.executeUpdate("updateChittalNxtNum", where);
                }
            }
        }
    }

    public static void main(String str[]) {
        try {
            GDSApplicationDAO dao = new GDSApplicationDAO();
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

        if (map.containsKey("gdsApplicationTO")) {
            gdsApplicationTO = new GDSApplicationTO();
            gdsApplicationTO = (GDSApplicationTO) map.get("gdsApplicationTO");
            final String command = gdsApplicationTO.getCommand();
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
                System.out.println("objSMSSubscriptionTO%$^$^$^$^" + objSMSSubscriptionTO);
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map, objLogDAO, objLogTO);
                    execReturnMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
                    execReturnMap.put("TRANS_ID", gdsApplicationTO.getTransId());
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                    execReturnMap.put("CHITTAL_NO", gdsApplicationTO.getChittalNo());
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map, objLogDAO, objLogTO);
                    deleteSMSSubscription(gdsApplicationTO);
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
        gdsApplicationTO = null;
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
    
    private ArrayList applicationListNew(HashMap map, HashMap applnMap,HashMap gdsApplTOMap, String totalAmt, List schemeList,HashMap chillalSchemeMap) throws Exception {
        
        System.out.println("gdsApplTOMap" + gdsApplTOMap);
        System.out.println("applnMap :" + applnMap);
        System.out.println("chillalSchemeMap :: " + chillalSchemeMap);
        HashMap schemeMap = new HashMap();
        ArrayList cashList = new ArrayList();
        int schemeIndex=0; 
        Iterator iterate = gdsApplTOMap.entrySet().iterator();
        if (null != gdsApplTOMap && gdsApplTOMap.size() > 0) {
            ArrayList list = new ArrayList();
            while (iterate.hasNext()) {                
                HashMap individualSchemeMap1 = (HashMap) schemeList.get(schemeIndex);
                System.out.println("schemename" + individualSchemeMap1);                
                GDSApplicationTO objGDSApplicationTO = new GDSApplicationTO();
                Map.Entry entry = (Map.Entry) iterate.next();
                Object key1 = (Object) entry.getKey();
                Object value = (Object) entry.getValue();
                System.out.println("key : " + key1.toString() + " value : " + value.toString());
                objGDSApplicationTO = (GDSApplicationTO) value;
                System.out.println("nithya :: " + objGDSApplicationTO);
                applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                applnMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
                applnMap.put("TRANS_MOD_TYPE", "MDS");
               // schemeMap.put("SCHEME_NAME",objGDSApplicationTO.getSchemeName());      
                schemeMap.put("SCHEME_NAME",CommonUtil.convertObjToStr(chillalSchemeMap.get(key1)));
                List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
                if (headlst != null && headlst.size() > 0) {
                    applnMap = (HashMap) headlst.get(0);
                }                    
                CashTransactionTO objCashTO = new CashTransactionTO();
                objCashTO.setTransId("");
                objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
                 if (CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
                    objCashTO.setProdType("GL");
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_HEAD")));
                } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("RECEIPT_HEAD")));
                } else {
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
                //objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
                objCashTO.setStatusBy((String) map.get(CommonConstants.USER_ID));                
                objCashTO.setStatusDt(gdsApplicationTO.getStatusDt());
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                // objCashTO.setParticulars(CommonUtil.convertObjToStr(schemeMap.get("SCHEME_NAME")) + "-" + CommonUtil.convertObjToStr(applnMap.get("CHIT_NO")));
                objCashTO.setParticulars(CommonUtil.convertObjToStr(key1)+"_1"); // Removed scheme name from particulars by nithya on 03-08-2016 for 4983
                objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
                objCashTO.setInitChannType(CommonConstants.CASHIER);
                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(key1));
                objCashTO.setCommand(gdsApplicationTO.getCommand());
                objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
                System.out.println("objCashTO 1st one:" + objCashTO);
                cashList.add(objCashTO);
                objCashTO = null;
                schemeIndex ++;
            }

        }
        
//        
//        
//        if (gdsApplicationTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
//            for (int i = 0; i < gdsApplicationList.size(); i++) {
//                applnMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
//                applnMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
//                applnMap.put("TRANS_MOD_TYPE", "MDS");
//                schemeMap = (HashMap) gdsApplicationList.get(i);              
//                List headlst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", schemeMap);
//                if (headlst != null && headlst.size() > 0) {
//                    applnMap = (HashMap) headlst.get(0);
//                }                    
//                CashTransactionTO objCashTO = new CashTransactionTO();
//                objCashTO.setTransId("");
//                objCashTO.setTransModType(CommonUtil.convertObjToStr(applnMap.get("TRANS_MOD_TYPE")));
//                if (CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_GL_ACCNO")).equals("GL")) {
//                    objCashTO.setProdType("GL");
//                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_HEAD")));
//                } else if (map.containsKey("PREDEFINED_INSTALL_BONUS")) {
//                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(applnMap.get("RECEIPT_HEAD")));
//                } else {
//                    HashMap suspenseMap = new HashMap();                // Suspense Acc Head
//                    suspenseMap.put("PROD_ID", applnMap.get("SUSPENSE_PROD_ID"));
//                    List suspenseLst = sqlMap.executeQueryForList("getAccountHeadProdSA", suspenseMap);
//                    if (suspenseLst != null && suspenseLst.size() > 0) {
//                        suspenseMap = (HashMap) suspenseLst.get(0);
//                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(suspenseMap.get("AC_HEAD")));
//                        objCashTO.setProdType(TransactionFactory.SUSPENSE);
//                        objCashTO.setProdId(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_PROD_ID")));
//                        objCashTO.setActNum(CommonUtil.convertObjToStr(applnMap.get("SUSPENSE_ACC_NO")));
//                    }
//                }
//                objCashTO.setInpAmount(CommonUtil.convertObjToDouble(totalAmt));
//                objCashTO.setAmount(CommonUtil.convertObjToDouble(totalAmt));
//                objCashTO.setTransType(CommonConstants.CREDIT);
//                objCashTO.setInitTransId(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
//                objCashTO.setBranchId(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
//                objCashTO.setStatusBy(CommonUtil.convertObjToStr(applnMap.get("USER_ID")));
//                objCashTO.setStatusDt(gdsApplicationTO.getStatusDt());
//                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
//                // objCashTO.setParticulars(CommonUtil.convertObjToStr(schemeMap.get("SCHEME_NAME")) + "-" + CommonUtil.convertObjToStr(applnMap.get("CHIT_NO")));
//                objCashTO.setParticulars(CommonUtil.convertObjToStr(schemeMap.get("CHIT_NO"))); // Removed scheme name from particulars by nithya on 03-08-2016 for 4983
//                objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(applnMap.get("BRANCH_CODE")));
//                objCashTO.setInitChannType(CommonConstants.CASHIER);
//                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(schemeMap.get("CHIT_NO")));
//                objCashTO.setCommand(gdsApplicationTO.getCommand());
//                objCashTO.setScreenName((String) map.get(CommonConstants.SCREEN));
//                System.out.println("objCashTO 1st one:" + objCashTO);
//                cashList.add(objCashTO);
//                objCashTO = null;
//            }
//        }
        return cashList;
    }    
   
}
