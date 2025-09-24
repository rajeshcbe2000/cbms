/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigDAO.java
 *
 * Created on Mon Jun 24 17:19:05 IST 2019
 */
package com.see.truetransact.serverside.indend.reserve;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

/**
 *
 * @author Suresh R
 */
public class ReserveDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public ReserveDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        //System.out.println("@@@@@@@map" + map);
        returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectIndendCloseTO", map);
        returnMap.put("IndendClosingTO", list);
        return returnMap;
    }

    private String getReserveID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RESERVE_DEP_ID");
        String closeID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return closeID;
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

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String reserveID = "";
            List finalList = null;
            reserveID = getReserveID();
            String closingType = CommonUtil.convertObjToStr(map.get("CLOSE_TYPE"));
            if (closingType.equals("RESERVE")) {
                closingType = "R";
            } else if (closingType.equals("DEPRECIATION")) {
                closingType = "D";
            }
            HashMap singleMap = new HashMap();
            if (reserveID.length() > 0) {
                boolean isTrans = false;
                finalList = (List) map.get("RESERVE_DEP_LIST");
                if (finalList != null && finalList.size() > 0) {
                    for (int i = 0; i < finalList.size(); i++) {
                        singleMap = (HashMap) finalList.get(i);
                        if (CommonUtil.convertObjToDouble(singleMap.get("AMOUNT")) > 0) {
                            singleMap.put("CLOSE_TYPE", closingType);
                            singleMap.put("CLOSE_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CLOSE_DATE")))));
                            singleMap.put("RD_ID", reserveID);
                            singleMap.put("AMOUNT", CommonUtil.convertObjToDouble(singleMap.get("AMOUNT")));
                            //System.out.println("########## Single Map : " + singleMap);
                            sqlMap.executeUpdate("insertReserveDepDetails", singleMap);
                            isTrans = true;
                        }
                    }
                    if (isTrans) {
                        insertTransactionData(map, finalList, reserveID, closingType);
                    }
                    returnMap.put("RESERVE_ID", reserveID);
                }
            } else {
                System.out.println("Please Create ID generation for - RESERVE_DEP_ID");
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void insertTransactionData(HashMap map, List finalList, String reserveID, String closingType) throws Exception {
        try {
            System.out.println("########### RESERVE DEPRECIATION TRANSACTION PART ####### ");
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap prodMap = new HashMap();
            HashMap singleMap = new HashMap();
            HashMap whereMap = new HashMap();
            double transAmt = 0.0;
            double lastYearCloseAmt = 0.0;
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            transferTrans.setInitiatedBranch(BRANCH_ID);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            transactionDAO.setLinkBatchID(reserveID);
            HashMap tansactionMap = new HashMap();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            String branchID = "";
            String closeType = "";
            Date closingDate = null;
            Date reserveTranDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CLOSE_DATE"))));
            if (closingType.equals("R")) {
                closeType = "RESERVE";
            } else if (closingType.equals("D")) {
                closeType = "DEPRECIATION";
            }
            if (finalList != null && finalList.size() > 0) {
                for (int i = 0; i < finalList.size(); i++) {
                    singleMap = (HashMap) finalList.get(i);
                    transAmt = CommonUtil.convertObjToDouble(singleMap.get("AMOUNT"));
                    branchID = CommonUtil.convertObjToStr(singleMap.get("BRANCH_CODE"));
                    if (transAmt > 0) {
                        List prodAcHDLst = (List) sqlMap.executeQueryForList("getReserveDepAccountHead", singleMap);
                        if (prodAcHDLst != null && prodAcHDLst.size() > 0) {
                            prodMap = (HashMap) prodAcHDLst.get(0);
                        } else {
                            throw new TTException("Please Enter the Product Account Heads !!! ");
                        }
                        transferList = new ArrayList();

                        //DEBIT PART
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, prodMap.get("EXP_AC_HD"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, closeType + " - " + reserveID);
                        txMap.put("AUTHORIZEREMARKS", closeType);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                        txMap.put("FROM_RESERVE_SCREEN","FROM_RESERVE_SCREEN");//Added by nithya on 18-11-2021 for KD-3124
                        txMap.put("RESERVE_TRANS_DATE",reserveTranDt);
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmt));

                        //CREDIT PART                        
                        txMap = new HashMap();
                        txMap.put(TransferTrans.CR_AC_HD, prodMap.get("AC_HD_ID"));
                        txMap.put("AUTHORIZEREMARKS", closeType);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, closeType + " - " + reserveID);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                        txMap.put("FROM_RESERVE_SCREEN","FROM_RESERVE_SCREEN");//Added by nithya on 18-11-2021 for KD-3124
                        txMap.put("RESERVE_TRANS_DATE",reserveTranDt);
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmt));

                        if (closingType.equals("R")) {
                            whereMap = new HashMap();
                            closingDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CLOSE_DATE"))));
                            System.out.println("############ Closing date : " + closingDate);
                            closingDate.setYear(closingDate.getYear() - 1);
                            System.out.println("############ After Changes Closing date : " + closingDate);
                            whereMap.put("CLOSE_DT", setProperDtFormat(closingDate));
                            whereMap.put("AC_HD_ID", prodMap.get("AC_HD_ID"));
                            whereMap.put("BRANCH_CODE", branchID);
                            List lastYearClosingLst = (List) sqlMap.executeQueryForList("getReserveLastYearCloseAmount", whereMap);
                            if (lastYearClosingLst != null && lastYearClosingLst.size() > 0) {
                                whereMap = (HashMap) lastYearClosingLst.get(0);
                                lastYearCloseAmt = CommonUtil.convertObjToDouble(whereMap.get("AMOUNT"));
                                if (lastYearCloseAmt > 0) {
                                    //DEBIT PART
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.DR_AC_HD, prodMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, closeType + " - " + reserveID);
                                    txMap.put("AUTHORIZEREMARKS", closeType);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("FROM_RESERVE_SCREEN","FROM_RESERVE_SCREEN");//Added by nithya on 18-11-2021 for KD-3124
                                    txMap.put("RESERVE_TRANS_DATE",reserveTranDt);
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, lastYearCloseAmt));

                                    //CREDIT PART
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_AC_HD, prodMap.get("INC_AC_HD"));
                                    txMap.put("AUTHORIZEREMARKS", closeType);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.PARTICULARS, closeType + " - " + reserveID);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    txMap.put("FROM_RESERVE_SCREEN","FROM_RESERVE_SCREEN");//Added by nithya on 18-11-2021 for KD-3124
                                    txMap.put("RESERVE_TRANS_DATE",reserveTranDt);
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, lastYearCloseAmt));
                                }
                            }
                        }
                    }
                    if (transferList != null && transferList.size() > 0) {
                        doDebitCredit(transferList, _branchCode, map, reserveID);
                    }
                }
                //AUTHORIZE PART
                HashMap transAuthMap = new HashMap();
                transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                transAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transAuthMap.put("DAILY", "DAILY");
                // Added by nithya for KD-3124
                transAuthMap.put("FROM_RESERVE_SCREEN","FROM_RESERVE_SCREEN");
                transAuthMap.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
                transAuthMap.put("TRANS_DATE", reserveTranDt);
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransfer(reserveID, "AUTHORIZED", transAuthMap);
                transAuthMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, HashMap map, String reserveID) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        // Added by nithya for KD-3124
        Date reserveTranDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CLOSE_DATE"))));            
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", map.get("COMMAND"));
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        data.put(CommonConstants.MODULE, "Indend");
        data.put(CommonConstants.SCREEN, "Reserve Depreciation Entry");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", reserveID);
        HashMap transMap = new HashMap();
        data.put("NO_PHOTO_SIGN_VALIDATION", "NO_PHOTO_SIGN_VALIDATION");
        data.put("NO_AADHAR_VALIDATION", "NO_AADHAR_VALIDATION");
        data.put("TRANS_DATE", reserveTranDt);
        data.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
        transMap = transferDAO.execute(data, false);
    }

    public static void main(String str[]) {
        try {
            ReserveDAO dao = new ReserveDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("##### Reserve Depreciation Transaction DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("RESERVE_DEP_LIST")) {
            //objTO = (IndendClosingTO) map.get("IndendCloseDetails");
            logDAO = new LogDAO();
            insertData(map);
            destroyObjects();
        }
        System.out.println("#### returnMap :" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
    }
}
