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
package com.see.truetransact.serverside.gdsapplication.gdsbankadvance;

import com.see.truetransact.serverside.mdsapplication.mdsbankadvance.*;
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
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.transferobject.gdsapplication.gdsbankadvance.GDSBankAdvanceTO;
import java.sql.SQLException;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author Suresh
 *
 *
 */
public class GDSBankAdvanceDAO extends TTDAO {

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
    public GDSBankAdvanceDAO() throws ServiceLocatorException {
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
        /*Data in MDS BAND ADVANCE OB: {BONUS_AMOUNT=250.0, SCHEME_NAME=881, TOTAL_AMOUNT=5000.0, 
         * MDS_BANK_ADVANCE=[{DIVISION_NO=1, 
         * CHITTAL_NO=0001GDS05000010, 
         * SUB_NO=1, MEMBER_NAME=Devassy P T, 
         * INST_AMT=5000, INST_DATE=2017-06-24 00:00:00.0, 
         * MEMBER_NO=AA240, CURR_INST_NO=2, 
         * BONUS=250, GROUP_NO=GDS050}], 
         * COMMAND=INSERT, GROUP_NO=GDS050}
         #$#$$ yesNo : 1*/
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
                    String bankAdvForfiet = checkBankAdvanceForfietNeeded(map);
                    if(bankAdvForfiet.equalsIgnoreCase("Y")) {
                        insertDataForfiet(map, objLogDAO, objLogTO);
                    } else {
                        insertData(map, objLogDAO, objLogTO);
                    }
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
                String isBonusTrans = "";
                HashMap splitTransMap = new HashMap();
                HashMap groupMap = new HashMap();
                groupMap.put("GROUP_NO", map.get("GROUP_NO"));
                List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);
                HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
                String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                HashMap checkMap = new HashMap();
                checkMap.put("SCHEME_NAME", commonScheme);
                List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
                if (list != null && list.size() > 0) {
                    checkMap = (HashMap) list.get(0);
                    if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                        isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                    }
                }
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
                HashMap  applicationMap = new HashMap();
                HashMap headMap = new HashMap();
                headMap.put("SCHEME_NAME", map.get("GROUP_NO"));
                List lst = sqlMap.executeQueryForList("getSelectGDSGroupAcctHead", headMap);
                if(lst!= null && lst.size() > 0){
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    int schemeCount = lst.size();
                    for (int i = 0; i < lst.size(); i++) {
                        applicationMap = (HashMap) lst.get(i);
                        txMap = new HashMap();
                        System.out.println("Transfer Started Credit MDS Receipt: ");
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        System.out.println("$#$$$#$#$# Prod Type GL " + applicationMap.get("RECEIPT_HEAD"));
                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Receipt : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                           txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }  
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        System.out.println("txMap Debit : " + txMap + "totalAmt :" + totalAmt);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        System.out.println("Transfer Started debit Bank Advance: ");
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        System.out.println("$#$$$#$#$# Prod Type Debit GL advance :" + applicationMap.get("BANKING_REP_PAY_HEAD"));
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Bank Advance : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                           txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }  
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        System.out.println("txMap Debit : " + txMap + "bankAdvanceAmt :" + bankAdvanceAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, bankAdvanceAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();
                        System.out.println("Transfer Started debit Bonus: ");
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
                        System.out.println("$#$$$#$#$# Prod Type Debit GL advance :" + applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Bonus : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put("TRANS_MOD_TYPE", "MDS");
                        if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) {// Added by nithya on 20-12-2019 for KD-887
                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        }
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        System.out.println("txMap Debit : " + txMap + "bonusAmt :" + bonusAmt);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);
                    }
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
                }
                GDSBankAdvanceTO objMDSBankAdvanceTO = null;
                HashMap dataMap = null;
                for (int k = 0; k < bankAdvanceList.size(); k++) {
                    dataMap = new HashMap();
                    dataMap = (HashMap) bankAdvanceList.get(k);                
                    int schemeCount = 0;
                    HashMap applicationDetailsMap = new HashMap();
                    applicationDetailsMap.put("GDS_NO", dataMap.get("CHITTAL_NO"));
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationDetailsMap);
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        schemeCount = allApplnDetails.size();
                        for (int t = 0; t < allApplnDetails.size(); t++) {
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(t);
                            dataMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            dataMap.put("SHG_TRANS_ID", bankAdvID);
                            dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            dataMap.put("STATUS_DT", currDt);
                            dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));
                            System.out.println("############## dataMap " + dataMap);
                            objMDSBankAdvanceTO = new GDSBankAdvanceTO();
                            objMDSBankAdvanceTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            objMDSBankAdvanceTO.setBankAdvId(bankAdvID);
                            objMDSBankAdvanceTO.setBankAdvDt(currDt);
                            objMDSBankAdvanceTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                            objMDSBankAdvanceTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                            objMDSBankAdvanceTO.setSubNo(CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                            objMDSBankAdvanceTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                            objMDSBankAdvanceTO.setInstallmentNo(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST_NO")));
                            objMDSBankAdvanceTO.setInstallmentDt((Date) dataMap.get("INST_DATE"));                            
                            if(isBonusTrans.equalsIgnoreCase("N")){// wrt the discussion with giby sir 
                               double instAmount =  (CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")) - CommonUtil.convertObjToDouble(dataMap.get("BONUS")))/schemeCount;
                               double bonus = 0.0;
                               objMDSBankAdvanceTO.setInstAmt(instAmount);
                               objMDSBankAdvanceTO.setBonusAmt(bonus);
                            }else{
                               objMDSBankAdvanceTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT"))/schemeCount);
                               objMDSBankAdvanceTO.setBonusAmt(CommonUtil.convertObjToDouble(dataMap.get("BONUS"))/schemeCount); 
                            }  
                            objMDSBankAdvanceTO.setRepaid("N");
                            objMDSBankAdvanceTO.setStatus(CommonConstants.STATUS_CREATED);
                            objMDSBankAdvanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            objMDSBankAdvanceTO.setStatusDt(currDt);
                            objMDSBankAdvanceTO.setGdsNo(CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                            sqlMap.executeUpdate("insertGDSBankAdvanceTO", objMDSBankAdvanceTO);
                            objLogTO.setData(objMDSBankAdvanceTO.toString());
                            objLogDAO.addToLog(objLogTO);                            
                            objMDSBankAdvanceTO = null;
                        }
                    }
                    dataMap.clear();
                    dataMap = null;
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
            GDSBankAdvanceDAO dao = new GDSBankAdvanceDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String checkBankAdvanceForfietNeeded(HashMap map) throws SQLException {
        String forfietAllowed = "N";
        HashMap groupMap = new HashMap();
        groupMap.put("GROUP_NO", map.get("GROUP_NO"));
        List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);
        HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
        String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
        HashMap checkMap = new HashMap();
        checkMap.put("SCHEME_NAME", commonScheme);
        List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
        if (list != null && list.size() > 0) {
            checkMap = (HashMap) list.get(0);
            if (checkMap.containsKey("BANK_ADV_FORFIET") && checkMap.get("BANK_ADV_FORFIET") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("BANK_ADV_FORFIET")))) {
                forfietAllowed = CommonUtil.convertObjToStr(checkMap.get("BANK_ADV_FORFIET"));
            }
        }
        return forfietAllowed;
    }
    
    private void insertDataForfiet(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (bankAdvanceList != null && bankAdvanceList.size() > 0) {
                String isBonusTrans = "";
                double amountTobeAdded = 0.0; 
                HashMap forfeitAmtMap = new HashMap();
                HashMap groupMap = new HashMap();
                groupMap.put("GROUP_NO", map.get("GROUP_NO"));
                List commonSchemeDetailsLst = sqlMap.executeQueryForList("getDetailsForOneSchemeInGroup", groupMap);
                HashMap commonSchemeMap = (HashMap) commonSchemeDetailsLst.get(0);
                String commonScheme = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
                HashMap checkMap = new HashMap();
                checkMap.put("SCHEME_NAME", commonScheme);
                List list = sqlMap.executeQueryForList("getifBonusTransactionRequired", checkMap);
                if (list != null && list.size() > 0) {
                    checkMap = (HashMap) list.get(0);
                    if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                        isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                    }
                }                
                HashMap prizeChittalMap = new HashMap();
                HashMap prizeMap = new HashMap();
                prizeMap.put("SCHEME_NAME", commonScheme);
                List prizeChittalList =  sqlMap.executeQueryForList("getPrizedChitalListForfiet", prizeMap);
                if(prizeChittalList != null && prizeChittalList.size() > 0){
                    for(int i=0; i < prizeChittalList.size(); i++){
                       HashMap ChittalListMap = (HashMap)prizeChittalList.get(i);
                       prizeChittalMap.put(ChittalListMap.get("GDS_NO"), "Y");
                    }
                }
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
                HashMap headMap = new HashMap();
                headMap.put("SCHEME_NAME", map.get("GROUP_NO"));
                List lst = sqlMap.executeQueryForList("getSelectGDSGroupAcctHead", headMap);
                amountTobeAdded = getAllAmountsFor_Forfiet(prizeChittalMap);                
                bankAdvanceAmt = bankAdvanceAmt + amountTobeAdded;               
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
                HashMap  applicationMap = new HashMap();               
                if(lst!= null && lst.size() > 0){
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    int schemeCount = lst.size();
                    for (int i = 0; i < lst.size(); i++) {
                        applicationMap = (HashMap) lst.get(i);
                        txMap = new HashMap();                      
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");                       
                        txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Receipt : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                           txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }  
                        txMap.put("TRANS_MOD_TYPE", "MDS");     
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, totalAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();                       
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");                       
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Bank Advance : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        if(map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null){
                           txMap.put("SCREEN_NAME",(String) map.get(CommonConstants.SCREEN)); 
                        }  
                        txMap.put("TRANS_MOD_TYPE", "MDS"); 
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, bankAdvanceAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);
                        transferTo = new TxTransferTO();
                        txMap = new HashMap();                       
                        transferTo.setInstrumentNo2("APPL_GL_TRANS");                        
                        txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Bonus : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                        txMap.put("TRANS_MOD_TYPE", "MDS");  
                        if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) {
                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                        }
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusAmt/schemeCount);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setTransDt(currDt);
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setAuthorizeRemarks("BANK ADVANCE");
                        transferTo.setLinkBatchId(bankAdvID);
                        TxTransferTO.add(transferTo);       
                        forfeitAmtMap = getAllAmountsFor_ForfietMap(prizeChittalMap);
                        System.out.println("forfeitAmtMap :: " + forfeitAmtMap);
                        if (amountTobeAdded > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();                           
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");                            
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Receipt : SCHEME - " + applicationMap.get("MP_MDS_CODE"));
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            if (map.containsKey(CommonConstants.SCREEN) && map.get(CommonConstants.SCREEN) != null) {
                                txMap.put("SCREEN_NAME", (String) map.get(CommonConstants.SCREEN));
                            }
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");// Added by nithya on 20-12-2019 for KD-887
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, amountTobeAdded / schemeCount);
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setAuthorizeRemarks("BANK ADVANCE");
                            transferTo.setLinkBatchId(bankAdvID);
                            TxTransferTO.add(transferTo);
                        }                      
                    }
                    transferDAO = new TransferDAO();
                    HashMap transactionMap = new HashMap();
                    transactionMap.put("TxTransferTO", TxTransferTO);
                    transactionMap.put("MODE", map.get("COMMAND"));
                    transactionMap.put("COMMAND", map.get("COMMAND"));
                    transactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                    transactionMap.put("LINK_BATCH_ID", bankAdvID);
                    transactionMap.put("INITIATED_BRANCH", _branchCode);                   
                    transMap = transferDAO.execute(transactionMap, false);
                }
                GDSBankAdvanceTO objMDSBankAdvanceTO = null;
                HashMap dataMap = null;
                for (int k = 0; k < bankAdvanceList.size(); k++) {
                    dataMap = new HashMap();
                    dataMap = (HashMap) bankAdvanceList.get(k);                
                    int schemeCount = 0;
                    HashMap applicationDetailsMap = new HashMap();
                    applicationDetailsMap.put("GDS_NO", dataMap.get("CHITTAL_NO"));
                    List allApplnDetails = sqlMap.executeQueryForList("getAllApplcationDetailsForGDS", applicationDetailsMap);
                    if (allApplnDetails != null && allApplnDetails.size() > 0) {
                        schemeCount = allApplnDetails.size();
                        for (int t = 0; t < allApplnDetails.size(); t++) {
                            GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(t);
                            dataMap.put("SCHEME_NAME", objGDSApplicationTO.getSchemeName());
                            dataMap.put("SHG_TRANS_ID", bankAdvID);
                            dataMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            dataMap.put("STATUS_DT", currDt);
                            dataMap.put("STATUS_BY", (String) map.get(CommonConstants.USER_ID));                            
                            objMDSBankAdvanceTO = new GDSBankAdvanceTO();
                            objMDSBankAdvanceTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            objMDSBankAdvanceTO.setBankAdvId(bankAdvID);
                            objMDSBankAdvanceTO.setBankAdvDt(currDt);
                            objMDSBankAdvanceTO.setSchemeName(objGDSApplicationTO.getSchemeName());
                            objMDSBankAdvanceTO.setChittalNo(objGDSApplicationTO.getChittalNo());
                            objMDSBankAdvanceTO.setSubNo(CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                            objMDSBankAdvanceTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                            objMDSBankAdvanceTO.setInstallmentNo(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST_NO")));
                            objMDSBankAdvanceTO.setInstallmentDt((Date) dataMap.get("INST_DATE"));                                                      
                            objMDSBankAdvanceTO.setRepaid("N");
                            objMDSBankAdvanceTO.setStatus(CommonConstants.STATUS_CREATED);
                            objMDSBankAdvanceTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            objMDSBankAdvanceTO.setStatusDt(currDt);
                            objMDSBankAdvanceTO.setGdsNo(CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO")));
                            if(forfeitAmtMap != null && forfeitAmtMap.containsKey(objGDSApplicationTO.getGds_No()) && forfeitAmtMap.get(objGDSApplicationTO.getGds_No()) != null){
                                objMDSBankAdvanceTO.setForfeitAmt(CommonUtil.convertObjToDouble(forfeitAmtMap.get(objGDSApplicationTO.getGds_No())) / schemeCount);
                            }else{
                                objMDSBankAdvanceTO.setForfeitAmt(CommonUtil.convertObjToDouble(0.0));
                            }
                            if (isBonusTrans.equalsIgnoreCase("N")) {// wrt the discussion with giby sir 
                                double instAmount = 0.0;
                                double bonus = 0.0;
                                if (prizeChittalMap != null && prizeChittalMap.containsKey(objMDSBankAdvanceTO.getGdsNo()) && prizeChittalMap.get(objMDSBankAdvanceTO.getGdsNo()).equals("Y")) {
                                    instAmount = (CommonUtil.convertObjToDouble(dataMap.get("INST_AMT"))) / schemeCount;
                                    bonus = 0.0;
                                } else {
                                    instAmount = (CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")) - CommonUtil.convertObjToDouble(dataMap.get("BONUS"))) / schemeCount;
                                    bonus = 0.0;
                                }
                                objMDSBankAdvanceTO.setInstAmt(instAmount);
                                objMDSBankAdvanceTO.setBonusAmt(bonus);
                            } else {
                                objMDSBankAdvanceTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")) / schemeCount);
                                objMDSBankAdvanceTO.setBonusAmt(CommonUtil.convertObjToDouble(dataMap.get("BONUS")) / schemeCount);
                                if (prizeChittalMap != null && prizeChittalMap.containsKey(objMDSBankAdvanceTO.getGdsNo()) && prizeChittalMap.get(objMDSBankAdvanceTO.getGdsNo()).equals("Y")) {
                                    objMDSBankAdvanceTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")) / schemeCount);
                                    objMDSBankAdvanceTO.setBonusAmt(0.0);
                                }
                            }
                            sqlMap.executeUpdate("insertGDSBankAdvanceTO", objMDSBankAdvanceTO);
                            objLogTO.setData(objMDSBankAdvanceTO.toString());
                            objLogDAO.addToLog(objLogTO);                            
                            objMDSBankAdvanceTO = null;
                        }
                    }
                    dataMap.clear();
                    dataMap = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
	 
    private Double getAllAmountsFor_Forfiet (HashMap prizeChittalMap){
        System.out.println("prizeChittalMap :: "+ prizeChittalMap);
        double amountTobeAdded = 0.0;
        for(int i=0; i < bankAdvanceList.size() ; i++ ){
            HashMap bankAdvanceMap = (HashMap) bankAdvanceList.get(i);
            String chital = CommonUtil.convertObjToStr(bankAdvanceMap.get("CHITTAL_NO"));
            if(prizeChittalMap != null && prizeChittalMap.containsKey(chital) && prizeChittalMap.get(chital).equals("Y")){
                amountTobeAdded = amountTobeAdded + CommonUtil.convertObjToDouble(bankAdvanceMap.get("BONUS"));
            }            
        }
        return amountTobeAdded;
    }
    
    private HashMap getAllAmountsFor_ForfietMap (HashMap prizeChittalMap){
        System.out.println("prizeChittalMap :: "+ prizeChittalMap);
        System.out.println("bankAdvanceList :: " + bankAdvanceList);
       HashMap forfeitAmtMap = new HashMap();
        for(int i=0; i < bankAdvanceList.size() ; i++ ){
            HashMap bankAdvanceMap = (HashMap) bankAdvanceList.get(i);
            String chital = CommonUtil.convertObjToStr(bankAdvanceMap.get("CHITTAL_NO"));
            if(prizeChittalMap != null && prizeChittalMap.containsKey(chital) && prizeChittalMap.get(chital).equals("Y")){
              forfeitAmtMap.put(chital,CommonUtil.convertObjToDouble(bankAdvanceMap.get("BONUS")));
            }           
        }
        return forfeitAmtMap;
    }
    
    
}
