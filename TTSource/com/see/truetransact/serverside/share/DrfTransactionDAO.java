/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureDAO.java
 *
 * Created on Wed May 26 10:59:57 GMT+05:30 2004
 */
package com.see.truetransact.serverside.share;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.share.DrfTransactionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import java.sql.*;
import oracle.sql.*;
import org.apache.log4j.Logger;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;

/**
 * DrfTransaction DAO.
 *
 * @author
 *
 */
public class DrfTransactionDAO extends TTDAO {

    private String branchId;
    private SqlMap sqlMap;
    private HashMap data;
    private Iterator addressIterator;
    private DrfTransactionTO objDrfMasterTO;
    private DrfTransactionTO objDrfTransactionTO;
    private TransactionDAO transactionDAO = null;
    private String command;
    private String _userId = "";
    private HashMap drfMasterMap;
    private HashMap deletedDrfMasterMap;
    private String key;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(DrfTransactionDAO.class);
    private String addressKey = new String();
    HashMap resultMap = new HashMap();
    Date currDt = null;
    private String whereCondition;
    private HashMap whereConditions;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String tableCondition;
    private int isMore = -1;
    private String addCondition;
    final String SCREEN = "CUS";
    private final String YES = "Y";
    private List list;
    private TransactionTO objTransactionTO;
    private TransactionTO objTransactionTO1;
    private HashMap returnMap;
    private List bufferList = new ArrayList();
    private String drfInterestTransId = "";
    private String generateSingleTransId = "";
    /**
     * Creates a new instance of DeductionDAO
     */
    public DrfTransactionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void updateData() throws Exception {

        getAllTOs();
        logTO.setData(objDrfMasterTO.toString());
        logTO.setPrimaryKey(objDrfMasterTO.getKeyData());
        logTO.setStatus(objDrfMasterTO.getCommand());
        sqlMap.executeUpdate("updateDrfTransaction", objDrfMasterTO);
        processDrfMasterData(objDrfMasterTO.getCommand());
        final String USERID = logTO.getBranchId();
        logDAO.addToLog(logTO);
        makeDataNull();
        makeNull();

    }

    private void deleteData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        objDrfTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
        try {
            sqlMap.startTransaction();
            objDrfTransactionTO.setStatusDate(currDt);
            System.out.println("#@%#$%#$objDrfTransactionTO:" + objDrfTransactionTO);
            sqlMap.executeUpdate("deleteDrfTransInfoTO", objDrfTransactionTO);
            insertDrfTransDetails(objLogDAO, objLogTO, map);
            // updateShareAccDetails() method call changed to insertShareAcctDetails()
            sqlMap.commitTransaction();
            objLogTO.setData(objDrfTransactionTO.toString());
            objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            //            throw new TransRollbackException(e);
            throw e;
        }
    }

    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        drfMasterMap = null;
        deletedDrfMasterMap = null;
        objDrfMasterTO = null;

    }

    private void processDrfMasterData(String command) throws Exception {

        if (deletedDrfMasterMap != null) {
            addressIterator = deletedDrfMasterMap.keySet().iterator();
            for (int i = deletedDrfMasterMap.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                System.out.println("entering deleted drf map!!" + deletedDrfMasterMap);
                objDrfTransactionTO = (DrfTransactionTO) deletedDrfMasterMap.get(key);
                logTO.setData(objDrfTransactionTO.toString());
                logTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                logTO.setStatus(objDrfTransactionTO.getCommand());
                sqlMap.executeUpdate("deleteDrfTransaction", objDrfTransactionTO);
                logDAO.addToLog(logTO);
            }
            deletedDrfMasterMap = null;
        }
        if (drfMasterMap != null) {
            addressIterator = drfMasterMap.keySet().iterator();

            for (int i = drfMasterMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objDrfTransactionTO = (DrfTransactionTO) drfMasterMap.get(addressKey);
                logTO.setData(objDrfTransactionTO.toString());
                logTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                logTO.setStatus(objDrfTransactionTO.getCommand());
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("#$%objDrfTransactionTO:" + objDrfTransactionTO);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objDrfTransactionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertDrfTransactionDetails", objDrfTransactionTO);
                    } else {
                        sqlMap.executeUpdate("updateDrfMasterDetails", objDrfTransactionTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {

                    sqlMap.executeUpdate("insertDrfTransactionDetails", objDrfTransactionTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    sqlMap.executeUpdate("deleteDrfTransaction", objDrfTransactionTO);

                }

                logDAO.addToLog(logTO);
            }
            drfMasterMap = null;
        }

    }

    private void getAllTOs() throws Exception {
        objDrfMasterTO = (DrfTransactionTO) data.get("DRFMASTERTO");

        if (data.containsKey("DELETEDDRFMASTER")) {
            deletedDrfMasterMap = (HashMap) data.get("DELETEDDRFMASTER");
        }
        if (data.containsKey("DRFMASTER")) {
            drfMasterMap = (HashMap) data.get("DRFMASTER");
        }

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

    public static void main(String str[]) {
        try {
            DrfTransactionDAO dao = new DrfTransactionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap transMap = new HashMap();
        String where = (String) obj.get("DRF_TRANS_ID");
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", obj.get("DRF_TRANS_ID"));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
        //        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
        }
        return transMap;
    }

    private void getDrfTableData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        list = (List) sqlMap.executeQueryForList("getSelectDrfProuctTO", whereConditions);
        if (list.size() > 0) {
            drfMasterMap = new HashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                //                drfMasterMap.put( ((DrfTransactionTO)list.get(j)).getDrfSlNo(),list.get(j));
            }
            data.put("DRFPRODUCT", drfMasterMap);
        }
    }

    private void getDrfMasterData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        whereConditions.put("CURRENT_DT", currDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        list = (List) sqlMap.executeQueryForList("getSelectDrfMasterTO", whereConditions);
        System.out.println("@#$@#$@#4list :" + list);
        objDrfMasterTO = new DrfTransactionTO();
        if (list.size() > 0) {
            objDrfMasterTO = (DrfTransactionTO) list.get(0);
            data.put("DRFMASTERTO", objDrfMasterTO);
        }


    }

    private void makeQueryNull() {
        whereCondition = null;
        list = null;
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

    public HashMap execute(HashMap map) throws Exception {
        //CommonUtil.serializeObjWrite("D:\\share.txt", map);
        System.out.println("#### inside drfTransactionDAO execute() map : " + map);
        System.out.println("#### FROM drfDAO " + generateSingleTransId);
        System.out.println("#### FROM shareDAO  " + map.get("generateSingleTransId"));
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        LogDAO objLogDAO = new LogDAO();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        branchId = objLogTO.getBranchId();
        System.out.println("#### branchId : " + branchId);
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        generateSingleTransId = generateLinkID();
        returnMap = new HashMap();
        if (map.containsKey("DrfTransactionTO")) {
            objDrfTransactionTO = (DrfTransactionTO) map.get("DrfTransactionTO");
            final TOHeader toHeader = objDrfTransactionTO.getTOHeader();
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            System.out.println("@#$@#$#@command:" + command);
            //--- Selects the method according to the Command type
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map, objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                //                updateData(map , objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map, objLogDAO, objLogTO);

            } else {
                throw new NoCommandException();
            }
            destroyObjects();
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        return returnMap;
    }

    private void getDrfTrans_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DRF_TRANSACTION");
        String drfTransID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        objDrfTransactionTO.setDrfTransID(drfTransID);
        returnMap.put("DRF_TRAN_ID", drfTransID);
    }

    public String getDrfTrans_No_For_Share_Resolution() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DRF_TRANSACTION");
        String drfTransID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return drfTransID;
    }

    private String getGeneralBoadyID() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        ///////  where.put(CommonConstants.MAP_WHERE, "GBID");
        where.put(CommonConstants.MAP_WHERE, "DRFIT_ID");
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
            where.put("ID_KEY", "DRFI_ID"); //Here u have to pass BORROW_ID or something else
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

    public CashTransactionTO setCashTransaction(HashMap cashMap) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonUtil.convertObjToStr(cashMap.get("TRANS_TYPE")));
            if(cashMap.containsKey("BRANCH_CODE")){
                objCashTransactionTO.setBranchId(CommonUtil.convertObjToStr(cashMap.get("BRANCH_CODE")));
            }else{
                objCashTransactionTO.setBranchId(_branchCode);
            }
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            //objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            if(cashMap.containsKey(CommonConstants.PARTICULARS) && cashMap.get(CommonConstants.PARTICULARS) != null){ // Added by nithya on 08-08-2017 for 0007362: DRF PAYMENT VOUCHER ISSUE
                objCashTransactionTO.setParticulars(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.PARTICULARS)));
            }else{
               objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));  
            }            
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            if(cashMap.containsKey("FROM_SHARE_DAO") && cashMap.containsKey("SHARE_ACCT_NUM")){
                objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("SHARE_ACCT_NUM")));
            }else{
                objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            }
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("TRANS_MOD_TYPE")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTransactionTO.setSingleTransId(CommonUtil.convertObjToStr(cashMap.get("generateSingleTransId")));
            if(cashMap.containsKey("SCREEN_NAME") && cashMap.get("SCREEN_NAME") != null){ // Added by nithya on 07/09/2017
              objCashTransactionTO.setScreenName(CommonUtil.convertObjToStr(cashMap.get("SCREEN_NAME")));  
            }          
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void insertDrfTransDetails(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception, Exception {


        try {
            System.out.println("!@#$@#$objDrfTransactionTO:" + objDrfTransactionTO);
            System.out.println("!@#$@#generateSingleTransId:" + map.get("generateSingleTransId"));
            if (objDrfTransactionTO.getCommand() != null) {
                if (objDrfTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    getDrfTrans_No();
                    System.out.println("!@#!@#!ID GENERATED:" + objDrfTransactionTO.getDrfTransID());
                    objDrfTransactionTO.setResolutionDate(setProperDtFormat(objDrfTransactionTO.getResolutionDate()));
                    sqlMap.executeUpdate("insertDrfTransDetailsTO", objDrfTransactionTO);
//                    changed by nikhil for Drf Reciept for Share account Opening
                    if (map.containsKey("SHARE_ACCT_OPEN")) {
                        map.put("DRF_STATUS", "UNAUTHORIZED");
                        map.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDrfTransactionTO.getTxtDrfTransMemberNo()));
                        System.out.println("$#@$@#$@#$@#$map:" + map);
                        sqlMap.executeUpdate("updateShareDrfStatus", map);
                    }
                    //                    drfAmt IS THE AMOUNT THAT IS COMING FROM THE TRANSACTION UI
                    double drfAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getTxtDrfTransAmount()).doubleValue();
//                    productAmt IS THE RECIEPT AMOUNT
                    double productAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProductAmount()).doubleValue();
//                    paymentAmt IS THE PAYMENT AMOUNT
                    double paymentAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProdPaymentAmt()).doubleValue();
//                    ITS THE DIFFERENCE BETWEEN THE RECIEPT AMOUNT AND PAYMENT AMOUNT
                    double balanceAmount = drfAmt - productAmt;
                    System.out.println("@#$@drfAmt:" + drfAmt + "  " + "@!#!@#productAmt: " + productAmt + "  " + "@!#!@#paymentAmt:" + paymentAmt);
                    HashMap txMap;
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getDrfProdectHead", objDrfTransactionTO.getCboDrfTransProdID());

                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objDrfTransactionTO.getDrfTransID());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    if (CommonUtil.convertObjToStr(objDrfTransactionTO.getRdoDrfTransaction()).equals("RECIEPT")) {
                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                        if (TransactionDetailsMap.size() > 0) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    //                                   System.out.println("objTransactionTO---->"+objTransactionTO);


                                    double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                        txMap.put(TransferTrans.PARTICULARS, objDrfTransactionTO.getDrfTransID());
                                        txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                        HashMap interBranchCodeMap = new HashMap();
                                        interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                                        if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                            interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                            System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                                        } else {
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //txMap.put("TRANS_MOD_TYPE", CommonConstants.SHARE);
                                        if (objTransactionTO.getProductType().equals("OA")){
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        }else if(objTransactionTO.getProductType().equals("AB")){
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        }else if(objTransactionTO.getProductType().equals("SA")){
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        }else if(objTransactionTO.getProductType().equals("TL")){
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        }else if(objTransactionTO.getProductType().equals("AD")){
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        }else
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        } else {
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        }
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        if (!map.containsKey("generateSingleTransId")) {
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                        }else
                                            txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                        if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                            txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                        } else {
                                            txMap.put("SCREEN_NAME", "DRF");
                                        }
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                        //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                        //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        if (drfAmt > 0.0) {
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("LIABILITY_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "LIABILITY_HEAD");
                                            //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            if (!map.containsKey("generateSingleTransId")) {
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                            }else
                                                txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                            if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                            } else {
                                                txMap.put("SCREEN_NAME", "DRF");
                                            }
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, drfAmt));
                                            //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    } else {
                                        double transAmt;
                                        TransactionTO transTO = new TransactionTO();
                                        ArrayList cashList = new ArrayList();
                                        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                            System.out.println("line no 465^^^^^^^");
                                            txMap = new HashMap();
                                            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                            txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                            txMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS" + CommonConstants.SHARE);
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            if (drfAmt > 0.0) {
                                                txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("LIABILITY_HEAD"));
                                                txMap.put("AUTHORIZEREMARKS", "LIABILITY_HEAD");
                                                txMap.put("AMOUNT", new Double(drfAmt));
                                                txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                                if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                    txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                                } else {
                                                    txMap.put("SCREEN_NAME", "DRF");
                                                }
                                            }
                                            if (!map.containsKey("generateSingleTransId")) {
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                            }else
                                                txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                            HashMap shareAcctNumberMap = new HashMap();
                                            shareAcctNumberMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDrfTransactionTO.getTxtDrfTransMemberNo()));
                                            List interBranchCodeList = sqlMap.executeQueryForList("getShareBranchCode", shareAcctNumberMap);
                                            if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                                shareAcctNumberMap = (HashMap) interBranchCodeList.get(0);
                                                txMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(shareAcctNumberMap.get("BRANCH_CODE")));
                                            }
                                            if(map.containsKey("FROM_SHARE_DAO") && map.containsKey("SHARE_ACCT_NUM")){
                                                txMap.put("SHARE_ACCT_NUM", map.get("SHARE_ACCT_NUM"));
                                                txMap.put("FROM_SHARE_DAO","FROM_SHARE_DAO");
                                            }
                                            if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                            } else {
                                                txMap.put("SCREEN_NAME", "DRF");
                                            }
                                            cashList.add(setCashTransaction(txMap));
                                            System.out.println("cashList---------------->" + cashList);
                                            HashMap tranMap = new HashMap();
                                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                            CashTransactionDAO cashDao;
                                            cashDao = new CashTransactionDAO();
                                            tranMap = cashDao.execute(tranMap, false);
                                            cashDao = null;
                                            tranMap = null;
                                        }
                                    }
                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                                    objTransactionTO.setBatchDt(currDt);
                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                    objTransactionTO.setBranchId(_branchCode);
                                    System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                }
                            }
                        }
                        drfAmt = 0.0;
                        objTransferTrans = null;
                        transferList = null;
                        txMap = null;
                        // Code End
                    } else if (CommonUtil.convertObjToStr(objDrfTransactionTO.getRdoDrfTransaction()).equals("PAYMENT")) {
                        //                        //                            System.out.println("#### inside shareDAO execute() map : " + map);
                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        //                        //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
                        if (TransactionDetailsMap.size() > 0) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    //                                    //                                   System.out.println("objTransactionTO---->"+objTransactionTO);
                                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("LIABILITY_HEAD"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    //
                                    //
                                    drfAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    //_____________________________________________________________________________________________________/mINE
                                    if (objDrfTransactionTO.getRdoDrfTransaction().equals("PAYMENT")) {  //ArrayList<>

                                        HashMap drfProdDetailsMap = new HashMap();
                                        drfProdDetailsMap.put("PROD_ID", objDrfTransactionTO.getCboDrfTransProdID());

                                        List recoveryAmount = sqlMap.executeQueryForList("getrecoverAmount", drfProdDetailsMap);

                                        System.out.println("Amountttttt " + recoveryAmount.get(0));

                                        HashMap memberDetMap = new HashMap();
                                        memberDetMap = (HashMap) recoveryAmount.get(0);
                                        //(!memberDetMap.get("RECOVERY_AMOUNT").toString().equals(""))||
                                        if (memberDetMap.get("RECOVERY_AMOUNT") != null) {
                                            String amtt = memberDetMap.get("RECOVERY_AMOUNT").toString();
                                            System.out.println("Exact amounttt" + memberDetMap.get("RECOVERY_AMOUNT"));


                                            HashMap hmap = new HashMap();
                                            hmap.put("DRF_PROD_ID", objDrfTransactionTO.getCboDrfTransProdID());

                                            //  List memberDrfTransDetails = ClientUtil.executeQuery("getMemberDrfTransDetails1",hmap);
                                            List memberDrfTransDetails = sqlMap.executeQueryForList("getMemberDrfTransDetails1", hmap);
                                            System.out.println("LIST SIZE OF EMPP " + memberDrfTransDetails.size());


                                            for (int z = 0; z < memberDrfTransDetails.size(); z++) {
                                                HashMap memberDetMap1 = new HashMap();
                                                memberDetMap1 = (HashMap) memberDrfTransDetails.get(z);
                                                DrfTransactionTO DTO = new DrfTransactionTO();
                                                DTO.setDrfTransID(objDrfTransactionTO.getDrfTransID());
                                                DTO.setCboDrfTransProdID(objDrfTransactionTO.getCboDrfTransProdID());
                                                DTO.setTxtDrfTransMemberNo(memberDetMap1.get("MEMBER_NO").toString());
                                                DTO.setTxtDrfTransAmount(CommonUtil.convertObjToDouble(amtt));
                                                sqlMap.executeUpdate("insertRecoverTO", DTO);
                                            }

                                        }

                                        // sqlMap.executeUpdate("insertDrfRecoveryTO",objDrfTransactionTO);
                                    }

                                    //________________________________________________________________________________________________________//mINE

                                    //                                    DOIN TRANSFER FROM HERE

                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {

                                        //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));


                                        txMap.put(TransferTrans.PARTICULARS, objDrfTransactionTO.getDrfTransID());
                                        txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                        transactionDAO.setTransType(CommonConstants.DEBIT);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                        HashMap shareAcctNumberMap = new HashMap();
                                        shareAcctNumberMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDrfTransactionTO.getTxtDrfTransMemberNo()));
                                        List interBranchCodeList = sqlMap.executeQueryForList("getShareBranchCode", shareAcctNumberMap);
                                        if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                            shareAcctNumberMap = (HashMap) interBranchCodeList.get(0);
                                            System.out.println("interBranchCodeMap : " + shareAcctNumberMap);
                                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(shareAcctNumberMap.get("BRANCH_CODE")));
                                        } else {
                                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        }
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        if (objTransactionTO.getParticulars() != null && objTransactionTO.getParticulars().length() > 0) { // Added by nithya on 08-08-2017 for 0007362: DRF PAYMENT VOUCHER ISSUE
                                            txMap.put(CommonConstants.PARTICULARS, objTransactionTO.getParticulars());
                                        } else {
                                            System.out.println("else part executing");
                                        }
                                        //txMap.put("TRANS_MOD_TYPE", CommonConstants.SHARE);
                                        //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                        //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        //DEBIT LIABILITY GL
                                        if (balanceAmount > 0.0) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("EXPENDITURE_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "LIABILITY_HEAD");
                                            //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            if (!map.containsKey("generateSingleTransId")) {
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                            }else
                                                txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                            // Added by nithya on 07-09-2017
                                            if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                            } else {
                                                txMap.put("SCREEN_NAME", "DRF");
                                            }
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, balanceAmount));
                                            //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                        //DEBIT EXPENSSE GL
                                        if (productAmt > 0.0) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("LIABILITY_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "EXPENDITURE_HEAD");
                                            //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            if (!map.containsKey("generateSingleTransId")) {
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                            }else
                                                txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                            // Added by nithya on 07/09/2017
                                             if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                            } else {
                                                txMap.put("SCREEN_NAME", "DRF");
                                            }
                                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, productAmt));
                                            //                                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                        //                                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);

                                        //                                        CREDIT
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                                        } else {
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        
                                       if (objTransactionTO.getProductType().equals("OA")){
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        }else if(objTransactionTO.getProductType().equals("AB")){
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        }else if(objTransactionTO.getProductType().equals("SA")){
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        }else if(objTransactionTO.getProductType().equals("TL")){
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        }else if(objTransactionTO.getProductType().equals("AD")){
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        }else
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        System.out.println("TRANS_MOD_TYPETRANS_MOD_TYPE##Payment"+txMap.get("TRANS_MOD_TYPE"));
                                        if (!map.containsKey("generateSingleTransId")) {
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                            }else
                                                txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                        //Added by nithya on 07/09/2017
                                        if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                            txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                        } else {
                                            txMap.put("SCREEN_NAME", "DRF");
                                        }
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, paymentAmt));
                                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);


                                    } else {
                                        double transAmt;
                                        TransactionTO transTO = new TransactionTO();
                                        ArrayList cashList = new ArrayList();
                                        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                            System.out.println("line no 465^^^^^^^");
                                            txMap = new HashMap();
                                            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                            txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                                            txMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());
                                            txMap.put("TRANS_MOD_TYPE", "SH");
                                            if(objTransactionTO.getParticulars() != null && objTransactionTO.getParticulars().length() > 0){ // Added by nithya on 08-08-2017 for 0007362: DRF PAYMENT VOUCHER ISSUE
                                               txMap.put(CommonConstants.PARTICULARS,objTransactionTO.getParticulars());
                                            }else{
                                                System.out.println("else part executing");
                                            }   
                                            if (balanceAmount > 0.0) {
                                                txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("EXPENDITURE_HEAD"));
                                                txMap.put("AUTHORIZEREMARKS", "LIABILITY_HEAD");
                                                txMap.put("AMOUNT", new Double(balanceAmount));
                                                if (!map.containsKey("generateSingleTransId")) {
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                }else
                                                    txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                                HashMap shareAcctNumberMap = new HashMap();
                                                shareAcctNumberMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDrfTransactionTO.getTxtDrfTransMemberNo()));
                                                List interBranchCodeList = sqlMap.executeQueryForList("getShareBranchCode", shareAcctNumberMap);
                                                if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                                    shareAcctNumberMap = (HashMap) interBranchCodeList.get(0);
                                                    txMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(shareAcctNumberMap.get("BRANCH_CODE")));
                                                }
                                                // Added by nithya on 07/09/2017
                                                if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                    txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                                } else {
                                                    txMap.put("SCREEN_NAME", "DRF");
                                                }
                                                cashList.add(setCashTransaction(txMap));
                                                System.out.println("@#$@#$@#$txMap:" + txMap);
                                                System.out.println("@#$@#$@#balanceAmount:" + balanceAmount);
                                            }

                                            if (productAmt > 0.0) {
                                                txMap.put(CommonConstants.AC_HD_ID, (String) acHeads.get("LIABILITY_HEAD"));
                                                txMap.put("AUTHORIZEREMARKS", "EXPENDITURE_HEAD");
                                                txMap.put("AMOUNT", new Double(productAmt));
                                                if (!map.containsKey("generateSingleTransId")) {
                                                    txMap.put("generateSingleTransId",generateSingleTransId);
                                                }else
                                                    txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                                                HashMap shareAcctNumberMap = new HashMap();
                                                shareAcctNumberMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(objDrfTransactionTO.getTxtDrfTransMemberNo()));
                                                List interBranchCodeList = sqlMap.executeQueryForList("getShareBranchCode", shareAcctNumberMap);
                                                if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                                    shareAcctNumberMap = (HashMap) interBranchCodeList.get(0);
                                                    txMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(shareAcctNumberMap.get("BRANCH_CODE")));
                                                }
                                                // Added by nithya on 07/09/2017
                                                if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                                                    txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                                                } else {
                                                    txMap.put("SCREEN_NAME", "DRF");
                                                }
                                                cashList.add(setCashTransaction(txMap));
                                                System.out.println("@#$@#$sdfsdf@#$txMap:" + txMap);
                                            }
                                            System.out.println("cashList---------------->" + cashList);
                                            HashMap tranMap = new HashMap();
                                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                            CashTransactionDAO cashDao;
                                            cashDao = new CashTransactionDAO();
                                            tranMap = cashDao.execute(tranMap, false);
                                            cashDao = null;
                                            tranMap = null;

                                        }
                                    }
                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                                    objTransactionTO.setBatchDt(currDt);
                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                    objTransactionTO.setBranchId(_branchCode);
                                    System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                    //
                                }
                                //
                            }
                        }
                    }
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double drfAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getTxtDrfTransAmount()).doubleValue();
                    double productAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProductAmount()).doubleValue();
                    double paymentAmt = CommonUtil.convertObjToDouble(objDrfTransactionTO.getDrfProdPaymentAmt()).doubleValue();
                    double balanceAmount = paymentAmt - drfAmt;
                    if (objDrfTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        shareAcctNoMap = new HashMap();
                        shareAcctNoMap.put("LINK_BATCH_ID", objDrfTransactionTO.getDrfTransID());
                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
                        TxTransferTO txTransferTO = null;
                        double oldAmount = 0;
                        HashMap oldAmountMap = new HashMap();
                        ArrayList transferList = new ArrayList();
                        if (lst != null && lst.size() > 0) {
                            for (int j = 0; j < lst.size(); j++) {
                                txTransferTO = (TxTransferTO) lst.get(j);
                                System.out.println("#@$@#$@#$lst:" + lst);
                            }

                        } else {
                            System.out.println("In Cash Edit");
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0) {
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));

                                        //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                        HashMap tempMap = new HashMap();
                                        //                                        if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                                        if (cLst1 != null && cLst1.size() > 0) {
                                            CashTransactionTO txTransferTO1 = null;
                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            txTransferTO1.setInpAmount(new Double(newAmount));
                                            txTransferTO1.setAmount(new Double(newAmount));
                                            txTransferTO1.setCommand(command);
                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO1.setStatusDt(currDt);

                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
                                            map.put("OLDAMOUNT", new Double(oldAmount));
                                            map.put("CashTransactionTO", txTransferTO1);
                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                            cashTransDAO.execute(map, false);
                                        }
                                        cLst1 = null;
                                        //                                        }

                                        //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){

                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
                                        objTransactionTO.setTransId(objDrfTransactionTO.getDrfTransID());
                                        objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                                        objTransactionTO.setBranchId(_branchCode);
                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);

                                    }

                                }

                                //                                }


                            }
                            lst = null;
                            oldAmountMap = null;
                            transferList = null;
                            shareAcctNoMap = null;
                            txTransferTO = null;
                        }
                    }
                    objLogTO.setData(objDrfTransactionTO.toString());
                    objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
//                added by Nikhil for DRF Applicable in Share
                if (!map.containsKey("FROM_SHARE_DAO")) {
                    getTransDetails(objDrfTransactionTO.getDrfTransID());
                } else {
                    returnMap.put("DRF_LINKED_BATCH", objDrfTransactionTO.getDrfTransID());
                }

            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    ////sdafdasr
    private void insertDrfInterestDetails(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception, Exception {


        try {

            System.out.println("!@#$@#$objDrfTransactionTO:" + objDrfTransactionTO);
            if (objDrfTransactionTO.getCommand() != null) {
                if (objDrfTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objDrfTransactionTO.setDrfInterestTransID(getGeneralBoadyID());

                    double totalAmount = Double.parseDouble(map.get("TOTAL").toString());
                    System.out.println("TOTAL>>>>> " + totalAmount);
                    // System.out.println("@#$@drfAmt:"+drfAmt + "  " + "@!#!@#productAmt: "+productAmt+ "  " + "@!#!@#paymentAmt:"+paymentAmt);
                    HashMap txMap;
                    // HashMap acHeads = (HashMap)sqlMap.executeQueryForObject("getDrfProdectHead", objDrfTransactionTO.getCboDrfTransProdID());

                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objDrfTransactionTO.getDrfInterestTransID());
                    System.out.println("MMYY IIIIDIIIDID..........." + objDrfTransactionTO.getDrfInterestTransID());

                    drfInterestTransId = objDrfTransactionTO.getDrfInterestTransID();
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();



                    //Gettingg Amounttt
                    String pid = objDrfTransactionTO.getCboDrfTransProdID();
                    String dHead = "";
                    String CHead = "";
                    HashMap DebitH = new HashMap();
                    DebitH.put("PROD_ID", pid);
                    // List DbitHL= ClientUtil.executeQuery("getDrhead",DebitH);
                    List DbitHL = sqlMap.executeQueryForList("getDrhead", DebitH);

                    if (DbitHL.size() > 0) {

                        HashMap DebitH1 = new HashMap();
                        DebitH1 = (HashMap) DbitHL.get(0);
                        dHead = DebitH1.get("INTEREST_DEBIT_HEAD").toString();
                        System.out.println("DHEADDDDD" + dHead);
                    }

                    //  List CbitHL= ClientUtil.executeQuery("getCrhead",DebitH);
                    List CbitHL = sqlMap.executeQueryForList("getCrhead", DebitH);
                    if (CbitHL.size() > 0) {

                        HashMap DebitH1 = new HashMap();
                        DebitH1 = (HashMap) CbitHL.get(0);
                        CHead = DebitH1.get("DRF_PAYMENT_ACHD").toString();
                        System.out.println("DHEADDDDD" + CHead);
                    }



                    //                                 if(objTransactionTO.getTransType().equals("TRANSFER")){
                    //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                    txMap.put(TransferTrans.PARTICULARS, objDrfTransactionTO.getDrfInterestTransID());
                    txMap.put(CommonConstants.USER_ID, objDrfTransactionTO.getStatusBy());
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    //                                    if (objTransactionTO.getProductType().equals("GL")) {
                    txMap.put(TransferTrans.DR_AC_HD, dHead);
                    System.out.println("TTXX MAPPP11" + txMap);

                    txMap.put(TransferTrans.DR_PROD_TYPE, "GL");
                    System.out.println("SHAREEEEEEE" + CommonConstants.SHARE);
                    txMap.put("TRANS_MOD_TYPE", "SH");
                    if (!map.containsKey("generateSingleTransId")) {
                        txMap.put("generateSingleTransId",generateSingleTransId);
                    }else
                        txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                    if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                        txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                    } else {
                        txMap.put("SCREEN_NAME", "DRF");
                    }
                    System.out.println("TTXX MAPPP22" + txMap);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalAmount));
                    System.out.println("TransferLLLIIISTTTTT,,,,," + transferList);
                    //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                    //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                    if (totalAmount > 0.0) {
                        txMap.put(TransferTrans.CR_AC_HD, CHead);
                        txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                        //
                        System.out.println("TTXX MAPPP33" + txMap);
                        // transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                        if (!map.containsKey("generateSingleTransId")) {
                            txMap.put("generateSingleTransId",generateSingleTransId);
                        }else
                            txMap.put("generateSingleTransId",map.get("generateSingleTransId"));
                        if (map.containsKey("SCREEN_NAME") && map.get("SCREEN_NAME") != null) {
                            txMap.put("SCREEN_NAME", map.get("SCREEN_NAME"));
                        } else {
                            txMap.put("SCREEN_NAME", "DRF");
                        }
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, totalAmount));
                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                        System.out.println("TransferLLLIIISTTTTT1111,,,,," + transferList);
                    }
                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                    //                                }

                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                    objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
                    objTransactionTO.setBatchDt(currDt);
                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                    objTransactionTO.setBranchId(_branchCode);
                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);

                }

            }

        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }

    }
    ////sadasdas

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
//        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
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
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
//            added by nikhil 
            if (!map.containsKey("FROM_SHARE_DAO")) {
                sqlMap.startTransaction();
            }
            objDrfTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
            objLogTO.setData(objDrfTransactionTO.toString());
            objLogTO.setPrimaryKey(objDrfTransactionTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            insertDrfTransDetails(objLogDAO, objLogTO, map);
            final String USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            // objTransactionTO1.setStatus(CommonConstants.STATUS_CREATED);


            if (map.containsKey("INTEREST_DETAILS_LIST")) {
                bufferList = (List) map.get("INTEREST_DETAILS_LIST");
            }
            if (!bufferList.isEmpty()) {

                insertDrfInterestDetails(objLogDAO, objLogTO, map);
                DrfTransactionTO objDrfTransactionTO = (DrfTransactionTO) map.get("DrfTransactionTO");
//            if(map.containsKey("INTEREST_DETAILS_LIST")){
//            bufferList = (List)map.get("INTEREST_DETAILS_LIST");
//        }
                System.out.println("Buffer in Dao>>>" + bufferList.size());
                String prod_Id = objDrfTransactionTO.getCboDrfProdId();
                //  String drfInterestId=objDrfTransactionTO.getCboDrfProdId();
                String status = objDrfTransactionTO.getStatus();
                Date status_Dt = objDrfTransactionTO.getStatusDate();
                String status_by = objDrfTransactionTO.getStatusBy();
                String authorizBy = objDrfTransactionTO.getAuthorizeBy();
                Date authoriz_Dt = objDrfTransactionTO.getAuthorizeDate();
                String authoriz = objDrfTransactionTO.getAuthorizeStatus();

                DrfTransactionTO DrfTO = new DrfTransactionTO();
                DrfTO.setCboDrfProdId(prod_Id);
                DrfTO.setStatusDate(status_Dt);
                for (int k = 0; k < bufferList.size(); k++) {
                    DrfTransactionTO dTO = new DrfTransactionTO();
                    HashMap interestIndividualDts = new HashMap();
                    interestIndividualDts = (HashMap) bufferList.get(k);
                    dTO.setDrfInterestTransID(drfInterestTransId);
                    dTO.setCboDrfProdId(prod_Id);
                    dTO.setDrfInterestId(interestIndividualDts.get("DRF_INTEREST_ID").toString());
                    dTO.setInterestMemberNo(interestIndividualDts.get("MEMBER_NO").toString());
                    //  dTO.setInterestMemberNo(interestIndividualDts.get("MEMBER_NAME").toString());
                    dTO.setInterestBalanceAmount(interestIndividualDts.get("AMOUNT").toString());
                    dTO.setInterest(interestIndividualDts.get("INTEREST").toString());
                    dTO.setStatus(status);
                    dTO.setStatusBy(status_by);
                    dTO.setStatusDate(status_Dt);
                    dTO.setAuthorizeBy(authorizBy);
                    dTO.setAuthorizeStatus(authoriz);
                    System.out.println("DTO>>>>" + dTO);
                    sqlMap.executeUpdate("insertDrfIndividual", dTO);
                    sqlMap.executeUpdate("updateShareAcct", dTO);
                    //DRF_PAID_DATE
                }

                ///update lastCalculated Date....
                System.out.println("DRFFFFTOOO>>" + DrfTO);
                sqlMap.executeUpdate("updateDrfInterestDetail", DrfTO);

            }



            if (!map.containsKey("FROM_SHARE_DAO")) {

                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            log.error(e);
            System.out.println("e : " + e);
            throw e;
        }
    }

    private void authorize(HashMap map) throws Exception {

        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String DrfTransID = null;
        DrfTransactionTO objTO = null;
        String linkBatchId = null;
        HashMap cashAuthMap;
        String templinkBatchId = null;
        try {
            if (!map.containsKey("FROM_SHARE_DAO")) {
                sqlMap.startTransaction();
            }
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                System.out.println("dataMap:" + dataMap);
                DrfTransID = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANS_ID"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", currDt);
                linkBatchId = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANS_ID"));
                HashMap whrMap = dataMap;
                if (map.containsKey("FROM_SHARE_DAO")) {
                    whrMap.put("DRF_TRANS_ID",dataMap.get("DRF_TRANSACTION_ID"));
                }
                System.out.println("status------------>" + status);
                sqlMap.executeUpdate("authorizeDrfTransInfo", whrMap);
                if(status.equals("AUTHORIZED")){
                    HashMap drfMap = new HashMap();
                    drfMap.put("DRF_TRANS_ID",dataMap.get("DRF_TRANS_ID"));
                    List drfList = (ArrayList) sqlMap.executeQueryForList("getDRFReceiptDetails", drfMap);
                    if(drfList != null && drfList.size() > 0){
                        drfMap = (HashMap)drfList.get(0);
                        if(drfMap.containsKey("RECIEPT_OR_PAYMENT") && drfMap.get("RECIEPT_OR_PAYMENT") != null && drfMap.get("RECIEPT_OR_PAYMENT").equals("RECIEPT")){
                            sqlMap.executeUpdate("updateShareDRFDetailsAfterReceipt", drfMap);
                        }
                    }
                }
                //linkBatchId = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANS_ID"));//Transaction Batch Id
                //Separation of Authorization for Cash and Transfer
                //Call this in all places that need Authorization for Transaction
                cashAuthMap = new HashMap();
                System.out.println("@#$@zxcvzx#$dataMap:" + dataMap);
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("DAILY", "DAILY");
                cashAuthMap.put("DRF_TRANSACTION_ID",dataMap.get("DRF_TRANSACTION_ID"));
                System.out.println("map:" + map);
                System.out.println("cashAuthMap:" + cashAuthMap);
                // The following line commented because no need of doing interbranch transaction in GL Transaction
                //                cashAuthMap.put("PRODUCT", "SHARE");
                System.out.println("linkBatchId-------------- ::"+linkBatchId);
                if (map.containsKey("FROM_SHARE_DAO")) {
                    HashMap transferTransParam = new HashMap();
                    transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                    transferTransParam.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    transferTransParam.put("TRANS_DT", currDt);
                    List transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    if (transferTransList != null && transferTransList.size() > 0) {
                        templinkBatchId = linkBatchId;
                    } else {
                        transferTransParam.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("DRF_TRANSACTION_ID")));
                        List transferTransList1 = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                        if (transferTransList1 != null && transferTransList1.size() > 0) {
                            templinkBatchId = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANSACTION_ID"));
                        }
                    }
                    transferTransParam.put("LINK_BATCH_ID", linkBatchId);
                    List transferTransList2 = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", transferTransParam);
                    if (transferTransList2 != null && transferTransList2.size() > 0) {
                        templinkBatchId = linkBatchId;
                    } else {
                        transferTransParam.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("DRF_TRANSACTION_ID")));
                        List transferTransList3 = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", transferTransParam);
                        if (transferTransList3 != null && transferTransList3.size() > 0) {
                            templinkBatchId = CommonUtil.convertObjToStr(dataMap.get("DRF_TRANSACTION_ID"));
                        }
                    }
                    linkBatchId =templinkBatchId;
                }
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                 
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
                System.out.println("DrfTransID----------------->" + DrfTransID);
                //                System.out.println();
                objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(DrfTransID));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                System.out.println("objTransactionTO----------------->" + objTransactionTO);
                objTransactionTO.setBranchId(_branchCode);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            }
            if (!status.equals("REJECTED")) {
                //                sqlMap.executeUpdate("upDateNoOfShareAndAmount",map);
            }
            selectedList = null;
            dataMap = null;
            if (!map.containsKey("FROM_SHARE_DAO")) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
        objTransactionTO = null;
        objDrfTransactionTO = null;
        drfMasterMap = null;
        deletedDrfMasterMap = null;
    }
    
}
