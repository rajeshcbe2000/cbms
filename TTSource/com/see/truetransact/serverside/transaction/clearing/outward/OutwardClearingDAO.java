/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingDAO.java
 *
 * Created on Mon Jan 12 16:26:34 IST 2004
 */
package com.see.truetransact.serverside.transaction.clearing.outward;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.clearing.outward.*;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.clearing.banklevel.BankClearingParameterTO;

import com.see.truetransact.serverside.clearing.returns.ReturnOfInstrumentsDAO;
import com.see.truetransact.transferobject.clearing.returns.ReturnOfInstrumentsTO;
import com.see.truetransact.commonutil.DateUtil;
//Added
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import org.apache.log4j.Logger;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.clearing.tally.InwardClearingTallyDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import java.util.*;

/**
 * OutwardClearing DAO.
 *
 */
public class OutwardClearingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private List listID;
    private List listPISD;
    private String batchID = "";
    private final Logger log = Logger.getLogger(OutwardClearingDAO.class);
    private Transaction transModuleBased;
    private LogDAO objLogDAO;
    private LogTO objLogTO;
    private String command;
    private HashMap execInputMap = null;
    private String[] bounceClearingType;
    private Date currDt = null;

    /**
     * Creates a new instance of OutwardClearingDAO
     */
    public OutwardClearingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap result = new HashMap();
        if (obj.containsKey("IDGenerate") && ((String) obj.get("IDGenerate")).equals("Y")) {
            try {
                result.put("NewID", getBatchID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("getDataMap" + map);
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);

        if (map.containsKey(CommonConstants.MAP_NAME)) {
            String mapName = (String) map.get(CommonConstants.MAP_NAME);
            List list = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap.put(mapName, list);
            System.out.println(list.size());
            map = null;
            return returnMap;
        }

        String mapNameOutwardClearingTO = "getSelectOutwardClearingTO";
        String mapNamePISTO = "getSelectOutwardClearingPISTO";

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            mapNameOutwardClearingTO = "getSelectAuthorizeOutwardClearingTO";
            mapNamePISTO = "getAuthorizeSelectOutwardClearingPISTO";
        } else if (map.containsKey(CommonConstants.REALIZEMAP)) {
            mapNameOutwardClearingTO = "getSelectRealizeOutwardClearingTO";
            mapNamePISTO = "getSelectRealizeOutwardClearingPISTO";
        } else if (map.containsKey("VIEW")) {
            mapNameOutwardClearingTO = "getViewforOutwardClearing";
            mapNamePISTO = "getViewfoPayinslip";

        }
        HashMap param = new HashMap();
        param.put("BATCH_ID", where);
        param.put("BRANCH_ID", _branchCode);
        param.put("PAY_IN_SLIP_DT", currDt.clone());
        List list = (List) sqlMap.executeQueryForList(mapNameOutwardClearingTO, param);
        returnMap.put("OutwardClearingTO", list);
        list = (List) sqlMap.executeQueryForList(mapNamePISTO, param);
        returnMap.put("OutwardClearingPISTO", list);
        map = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        System.out.println("execute - HashMap map " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap execReturnMap = new HashMap();
        execInputMap = map;
        System.out.println(map);
        // Log DAO
        objLogDAO = new LogDAO();

        // Log Transfer Object
        objLogTO = new LogTO();

        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        bounceClearingType = null;
        HashMap ralizMap = new HashMap();
        if (map.containsKey("REALIZEMAP") && map.get("REALIZEMAP") != null) {
            ralizMap = (HashMap) map.get("REALIZEMAP");
            if (ralizMap.containsKey("BounceClearingType")) {
                System.out.println("BounceClearingType    " + ralizMap.get("BounceClearingType"));

                bounceClearingType = CommonUtil.convertObjToStr(ralizMap.get("BounceClearingType")).split(" ");
                System.out.println("bounceClearingType 0--------" + bounceClearingType[0]);
                System.out.println("bounceClearingType1--------" + bounceClearingType[1]);
            }
        }
//        if (map.containsKey("BounceClearingType")) {
//            bounceClearingType = CommonUtil.convertObjToStr(map.get("BounceClearingType")).split(" ");
//            System.out.println("bounceClearingType 0--------"+ bounceClearingType[0]);
//            System.out.println("bounceClearingType1--------"+ bounceClearingType[1]);          
//        }

        try {
            if (isTransaction) {
                sqlMap.startTransaction();
            }
            if (map.containsKey("CLOSE_SCHEDULE")) {
                doScheduleCloser(map, objLogTO, isTransaction);
            } else {
                transModuleBased = TransactionFactory.createTransaction(TransactionFactory.OPERATIVE);

                HashMap oldAmountMap = (HashMap) map.get(TransactionDAOConstants.OLDAMT);

                if (map.containsKey("OutwardClearingTO")) {
                    listID = (List) map.get("OutwardClearingTO");
                }
                if (map.containsKey("OutwardClearingPISTO")) {
                    listPISD = (List) map.get("OutwardClearingPISTO");
                }
                if (!map.containsKey("Command")) {
                    System.out.println("No Command exception");
                    throw new NoCommandException();
                }
                command = (String) map.get("Command");


                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(oldAmountMap);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                }
                oldAmountMap = null;

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    execReturnMap.put(CommonConstants.TRANS_ID, batchID);
                }
            }

            if (isTransaction) {
                sqlMap.commitTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            throw e;
        }
        if (map.containsKey(CommonConstants.REALIZEMAP)) {
            System.out.println(map);
            HashMap realMap = (HashMap) map.get(CommonConstants.REALIZEMAP);
            if (realMap != null) {
                System.out.println("realMap" + realMap);
                authorize(realMap, isTransaction);
            }
        }

        objLogDAO = null;
        objLogTO = null;
        map = null;
        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    private void doScheduleCloser(HashMap map, LogTO objLogTO, boolean isTransaction) throws Exception {
        OutwardClearingPISTO pisBalanceUpdateTO = null;

        List list = (List) sqlMap.executeQueryForList("getListAuthorizePISBalanceTO", map);

        double totalAmt = 0.0;
        HashMap ruleMap = new HashMap();
        for (int i = 0, j = list.size(); i < j; i++) {
            pisBalanceUpdateTO = (OutwardClearingPISTO) list.get(i);

            ruleMap = getRuleMap(pisBalanceUpdateTO, 0.0, true);

            transModuleBased = TransactionFactory.createTransaction(pisBalanceUpdateTO.getProdType());
            transModuleBased.performOtherBalanceAdd(ruleMap);

            transModuleBased.updateGL(pisBalanceUpdateTO.getAcHdId(), pisBalanceUpdateTO.getAmount(), objLogTO, ruleMap);

            totalAmt += CommonUtil.convertObjToDouble(pisBalanceUpdateTO.getAmount()).doubleValue();
            String status = "";
            updatepassbook(pisBalanceUpdateTO, status);
        }
        executeTransactionPart(map);
        if (pisBalanceUpdateTO != null) {
            String outwardAcctHd = getOutwardAcctHead(pisBalanceUpdateTO);
            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
            totalAmt = totalAmt * -1;
            ruleMap.put(TransactionDAOConstants.TRANS_ID, CommonUtil.convertObjToStr(map.get("SCHEDULE_NO")));
            transModuleBased.updateGL(outwardAcctHd, new Double(totalAmt), objLogTO, ruleMap, true);
        }
//        HashMap transMap = (HashMap) sqlMap.executeQueryForObject("BouncingAdjustment", CommonUtil.convertObjToStr(map.get("SCHEDULE_NO")));
//        updateClearingSuspense(transMap);
        if (map.containsKey("Mode") && map.get("Mode").equals("CLOSE")) {
            InwardClearingTallyDAO inward = new InwardClearingTallyDAO();
            inward.doShortExcessTrans(map);
        }
    }

    private void executeTransactionPart(HashMap map) throws Exception {
        try {
            OutwardClearingPISTO pisBalanceUpdateTO = null;
            TransferTrans objTransferTrans = new TransferTrans();
            HashMap txMap;
            List list = (List) sqlMap.executeQueryForList("getListAuthorizePISBalanceTO", map);
            double totalAmt = 0.0;
            double totalCreditAmt = 0.0;
            ArrayList transferList = new ArrayList();
            double count = 0.0;
            double charge = 0.0;
            HashMap ruleMap = new HashMap();
            HashMap acHeads = new HashMap();
            String chkInstrumentCharges = "";
            for (int i = 0, j = list.size(); i < j; i++) {
                pisBalanceUpdateTO = (OutwardClearingPISTO) list.get(i);
                List COUNTlIST = (List) sqlMap.executeQueryForList("getAccountNoCount", pisBalanceUpdateTO.getBatchId());
                acHeads = (HashMap) sqlMap.executeQueryForObject("getOutwardChargeHead", pisBalanceUpdateTO.getBatchId());
                chkInstrumentCharges = CommonUtil.convertObjToStr(acHeads.get("OUTWARD_INSTRUMENT_CHARGES"));
                charge = CommonUtil.convertObjToDouble(acHeads.get("OUTWARD_INSTRUMENT_CHARGES")).doubleValue();
                if (chkInstrumentCharges.equals("Y")) {
                    if (COUNTlIST != null && COUNTlIST.size() > 0) {
                        HashMap hmap = (HashMap) COUNTlIST.get(0);
                        count = CommonUtil.convertObjToDouble(hmap.get("COUNT")).doubleValue();
                    }
                    totalAmt = charge * count;
                    txMap = new HashMap();
                    totalCreditAmt = totalCreditAmt + totalAmt;
                    objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    txMap.put(TransferTrans.PARTICULARS, "O/W Clg Charges");
                    txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put("AUTHORIZEREMARKS", "OUTWARD_INSTRUMENT_CHARGES_HD");
                    if (pisBalanceUpdateTO.getProdType().equals("GL")) {
                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(pisBalanceUpdateTO.getAcctNo()));
                    } else {
                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(pisBalanceUpdateTO.getAcctNo()));
                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(pisBalanceUpdateTO.getProdId()));
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(pisBalanceUpdateTO.getProdType()));
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, totalAmt));
                }
            }
            if (chkInstrumentCharges.equals("Y")) {
                txMap = new HashMap();
//           ArrayList transferList = new ArrayList();
                objTransferTrans = new TransferTrans();
                objTransferTrans.setInitiatedBranch(_branchCode);
                txMap.put(TransferTrans.PARTICULARS, "O/W Clg Charges");
                txMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put("AUTHORIZEREMARKS", "OUTWARD_INSTRUMENT_CHARGES_HD");
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);//_branchCode
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OUTWARD_INSTRUMENT_CHARGES_HD"));
                transferList.add(objTransferTrans.getCreditTransferTO(txMap, totalCreditAmt));
                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void updateClearingSuspense(HashMap dataMap) throws Exception {
        if (dataMap != null) {
            double amt = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
            if (amt > 0) {
                HashMap map = new HashMap();
                map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_HD"))); // prod a/c head
                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))); // ic a/c no branch
                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))); // local logged branch
                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))); // charge a/c get it from clearing_bank_param
                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl

                TransferTrans trans = new TransferTrans();
                trans.setTransMode(CommonConstants.TX_CLEARING);
                ArrayList batchList = new ArrayList();
                batchList.add(trans.getDebitTransferTO(map, amt));
                batchList.add(trans.getCreditTransferTO(map, amt));

                trans.doDebitCredit(batchList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
                batchList = null;
            }
        }
    }

    private void insertData() throws Exception {
        if ((listID != null && listID.size() > 0) || (listPISD != null && listPISD.size() > 0)) {
            batchID = getBatchID();
        }

        if (listID != null && listID.size() > 0) {
            int size = listID.size();
            int i = 0;
            OutwardClearingTO objOutwardClearingTO = null;
            while (i < size) {
                objOutwardClearingTO = ((OutwardClearingTO) listID.get(i));

                objOutwardClearingTO.setStatus(CommonConstants.STATUS_CREATED);
                objOutwardClearingTO.setOutwardId(String.valueOf(i + 1));
                objOutwardClearingTO.setBatchId(batchID);
                if (objOutwardClearingTO.getStatusBy() == null || objOutwardClearingTO.getStatusBy().length() == 0) {
                    objOutwardClearingTO.setStatusBy(objLogTO.getUserId());
                }
                objOutwardClearingTO.setStatusDt(currDt);
                objOutwardClearingTO.setBranchId(objLogTO.getBranchId());
                objOutwardClearingTO.setCreatedBy(objLogTO.getUserId());
                objOutwardClearingTO.setCreatedDt(currDt);
                sqlMap.executeUpdate("insertOutwardClearingTO", objOutwardClearingTO);
                objLogTO.setData(objOutwardClearingTO.toString());
                objLogTO.setPrimaryKey(objOutwardClearingTO.getKeyData());
                objLogTO.setStatus(command);

                objLogDAO.addToLog(objLogTO);
                i++;
            }
        }

        if (listPISD != null && listPISD.size() > 0) {
            int size = listPISD.size();
            int i = 0;
            OutwardClearingPISTO objOutwardClearingPISTO;
            while (i < size) {
                objOutwardClearingPISTO = (OutwardClearingPISTO) listPISD.get(i);

                objOutwardClearingPISTO.setStatus(CommonConstants.STATUS_CREATED);
                objOutwardClearingPISTO.setPayInSlipId(String.valueOf(i + 1));
                objOutwardClearingPISTO.setBatchId(batchID);
                if (objOutwardClearingPISTO.getStatusBy() == null || objOutwardClearingPISTO.getStatusBy().length() == 0) {
                    objOutwardClearingPISTO.setStatusBy(objLogTO.getUserId());
                }
                objOutwardClearingPISTO.setBranchId(objLogTO.getBranchId());
                objOutwardClearingPISTO.setStatusDt(currDt);
                objOutwardClearingPISTO.setPayInSlipDt(currDt);
                sqlMap.executeUpdate("insertOutwardClearingPISTO", objOutwardClearingPISTO);
//                if(objOutwardClearingPISTO.getProdType().equals("OA") ||objOutwardClearingPISTO.getProdType().equals("AD")) {
//                    HashMap map = new HashMap();
//                    map.put("ACT_NUM",objOutwardClearingPISTO.getAcctNo());
//                    
////                    System.out.println("@@@@@@@@@@@@$$%^^%map"+map);
//                    List lst = (List)sqlMap.executeQueryForList("chkForPassBook",map);
////                    System.out.println("@@@@@@@@@@@@$$%^^%list"+lst);
////                    System.out.println("@@@@@@@@@@@@$$%^^%list"+lst.size());
//                    map=null;
//                    if(lst.size()>0) {
//                        map=(HashMap)lst.get(0);
////                        System.out.println("@@@@@@@@@@@@$$%^^%maptochecky"+map);
//                        if(map.containsValue("Y")){
//                            double finamt=0;
//                            HashMap where = new HashMap();
//                            where.put("CREDIT",objOutwardClearingPISTO.getAmount());
//                            double amt = CommonUtil.convertObjToDouble(where.get("CREDIT")).doubleValue();
////                            System.out.println("@@@@@@@@@@@@$$%^^%amtcredit"+amt);
//                            HashMap map1 = new HashMap();
//                            map1.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
//                            List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objOutwardClearingPISTO.getProdType(),map1);
////                            System.out.println("@@@@@@@@@@@@$$%^^%listcredit"+lst1);
//                            map1=null;
//                            map1=(HashMap)lst1.get(0);
//                            double amt1 = CommonUtil.convertObjToDouble(map1.get("CLEAR_BALANCE")).doubleValue();
//                            double amt2 = CommonUtil.convertObjToDouble(map1.get("SHADOW_DEBIT")).doubleValue();
//                            double amt3 = CommonUtil.convertObjToDouble(map1.get("SHADOW_CREDIT")).doubleValue();
//                            String st=CommonUtil.convertObjToStr(map1.get("SHADOW_DEBIT"));
//                            if(st.startsWith("-"))
//                                finamt=amt+(amt1+amt2+amt3);
//                            else{
//                                finamt=amt+(amt1-amt2+amt3);
//                            }
////                            System.out.println("@@@@@@@@@@@@$$%^^%finamtcredit"+finamt);
////                            System.out.println("@@@@@@@@@@@@$$%^^%finamtdebit"+finamt);
//                            where.put("BALANCE", new Double(finamt));
//                            where.put("BATCH_ID",objOutwardClearingPISTO.getBatchId());
//                            where.put("ACT_NUM",objOutwardClearingPISTO.getAcctNo());
//                            where.put("TRANS_DT", objOutwardClearingPISTO.getPayInSlipDt());
//                            where.put("PBOOKFLAG", "0");
//                            //                      where.put("SLNO", "0");
//                            where.put("STATUS",objOutwardClearingPISTO.getStatus());
////                            System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
//                            sqlMap.executeUpdate("insertPassBook",where);
//                        }
//                    }
//                }
//            

                objLogTO.setData(objOutwardClearingPISTO.toString());
                objLogTO.setPrimaryKey(objOutwardClearingPISTO.getKeyData());
                objLogTO.setStatus(command);
                objLogDAO.addToLog(objLogTO);
                i++;
            }
        }
    }

    private void updateData(HashMap oldAmount) throws Exception {
        Integer maxCount;
        int mxCt = 0;
        int i = 0;
        int size = 0;
        if (listID != null && listID.size() > 0) {
            maxCount = (Integer) sqlMap.executeQueryForObject("getMaxOutwardClearingID", (OutwardClearingTO) listID.get(0));
            mxCt = maxCount.intValue();
            size = listID.size();
            i = 0;
            OutwardClearingTO objOutwardClearingTO;
            while (i < size) {
                objOutwardClearingTO = (OutwardClearingTO) listID.get(i);

                objOutwardClearingTO.setStatusBy(objLogTO.getUserId());

                if (objOutwardClearingTO.getStatus() == null
                        || objOutwardClearingTO.getStatus().length() == 0) {
                    objOutwardClearingTO.setOutwardId(String.valueOf(mxCt + 1));
                    objOutwardClearingTO.setStatus(CommonConstants.STATUS_CREATED);
                    objOutwardClearingTO.setBranchId(objLogTO.getBranchId());
                    objOutwardClearingTO.setCreatedBy(objLogTO.getUserId());
                    objOutwardClearingTO.setCreatedDt(currDt);
                    objOutwardClearingTO.setStatusDt(currDt);

                    sqlMap.executeUpdate("insertOutwardClearingTO", objOutwardClearingTO);
                    mxCt += 1;
                } else {
                    if (!objOutwardClearingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                        objOutwardClearingTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objOutwardClearingTO.setStatusDt(currDt);
                    }
                    objOutwardClearingTO.setInitiatedBranch(_branchCode);
                    objOutwardClearingTO.setOutwardDt(currDt);
                    sqlMap.executeUpdate("updateOutwardClearingTO", objOutwardClearingTO);
                }
                objLogTO.setData(objOutwardClearingTO.toString());
                objLogTO.setPrimaryKey(objOutwardClearingTO.getKeyData());
                objLogTO.setStatus(command);

                objLogDAO.addToLog(objLogTO);

                i++;
            }

        }
        if (listPISD != null && listPISD.size() > 0) {
            maxCount = (Integer) sqlMap.executeQueryForObject("getMaxPayInSlipID", (OutwardClearingPISTO) listPISD.get(0));
            mxCt = maxCount.intValue();
            size = listPISD.size();
            i = 0;
            double prevAmount;
            OutwardClearingPISTO objOutwardClearingPISTO;
            while (i < size) {
                prevAmount = 0;
                objOutwardClearingPISTO = (OutwardClearingPISTO) listPISD.get(i);

                objOutwardClearingPISTO.setStatusBy(objLogTO.getUserId());

                if (objOutwardClearingPISTO.getStatus() == null
                        || objOutwardClearingPISTO.getStatus().length() == 0) {
                    objOutwardClearingPISTO.setPayInSlipId(String.valueOf(mxCt + 1));
                    objOutwardClearingPISTO.setStatus(CommonConstants.STATUS_CREATED);
                    objOutwardClearingPISTO.setBranchId(objLogTO.getBranchId());
                    objOutwardClearingPISTO.setStatusDt(currDt);
                    sqlMap.executeUpdate("insertOutwardClearingPISTO", objOutwardClearingPISTO);
//                    if(objOutwardClearingPISTO.getProdType().equals("OA") ||objOutwardClearingPISTO.getProdType().equals("AD")) {
//                        HashMap map = new HashMap();
//                        map.put("ACT_NUM",objOutwardClearingPISTO.getAcctNo());
//                        
////                        System.out.println("@@@@@@@@@@@@$$%^^%map"+map);
//                        List lst = (List)sqlMap.executeQueryForList("chkForPassBook",map);
////                        System.out.println("@@@@@@@@@@@@$$%^^%list"+lst);
////                        System.out.println("@@@@@@@@@@@@$$%^^%list"+lst.size());
//                        map=null;
//                        if(lst.size()>0) {
//                            map=(HashMap)lst.get(0);
////                            System.out.println("@@@@@@@@@@@@$$%^^%maptochecky"+map);
//                            if(map.containsValue("Y")){
//                                double finamt=0;
//                                HashMap where = new HashMap();
//                                where.put("CREDIT",objOutwardClearingPISTO.getAmount());
//                                double amt = CommonUtil.convertObjToDouble(where.get("CREDIT")).doubleValue();
////                                System.out.println("@@@@@@@@@@@@$$%^^%amtcredit"+amt);
//                                HashMap map1 = new HashMap();
//                                map1.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
//                                List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objOutwardClearingPISTO.getProdType(),map1);
////                                System.out.println("@@@@@@@@@@@@$$%^^%listcredit"+lst1);
//                                map1=null;
//                                map1=(HashMap)lst1.get(0);
//                                double amt1 = CommonUtil.convertObjToDouble(map1.get("CLEAR_BALANCE")).doubleValue();
//                                double amt2 = CommonUtil.convertObjToDouble(map1.get("SHADOW_DEBIT")).doubleValue();
//                                double amt3 = CommonUtil.convertObjToDouble(map1.get("SHADOW_CREDIT")).doubleValue();
//                                String st=CommonUtil.convertObjToStr(map1.get("SHADOW_DEBIT"));
//                                if(st.startsWith("-"))
//                                    finamt=amt+(amt1+amt2+amt3);
//                                else{
//                                    finamt=amt+(amt1-amt2+amt3);
//                                }
////                                System.out.println("@@@@@@@@@@@@$$%^^%finamtcredit"+finamt);
////                                System.out.println("@@@@@@@@@@@@$$%^^%finamtdebit"+finamt);
//                                where.put("BALANCE", new Double(finamt));
//                                where.put("BATCH_ID",objOutwardClearingPISTO.getBatchId());
//                                where.put("ACT_NUM",objOutwardClearingPISTO.getAcctNo());
//                                where.put("TRANS_DT", objOutwardClearingPISTO.getPayInSlipDt());
//                                where.put("PBOOKFLAG", "0");
//                                //                      where.put("SLNO", "0");
//                                where.put("STATUS",objOutwardClearingPISTO.getStatus());
////                                System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
//                                sqlMap.executeUpdate("insertPassBook",where);
//                            }
//                        }
//                    }
                    mxCt += 1;
                } else {
                    if (!objOutwardClearingPISTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED)) {
                        objOutwardClearingPISTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objOutwardClearingPISTO.setStatusDt(currDt);
                        objOutwardClearingPISTO.setBranchId(_branchCode);
                        objOutwardClearingPISTO.setPayInSlipDt(currDt);
                    }
                    sqlMap.executeUpdate("updateOutwardClearingPISTO", objOutwardClearingPISTO);
//                    if(objOutwardClearingPISTO.getProdType().equals("OA") ||objOutwardClearingPISTO.getProdType().equals("AD")) {
//                        double finamt=0;
//                        HashMap where = new HashMap();
//                        where.put("CREDIT",objOutwardClearingPISTO.getAmount());
//                        double amt = CommonUtil.convertObjToDouble(where.get("CREDIT")).doubleValue();
//    //                    System.out.println("@@@@@@@@@@@@$$%^^%amtcredit"+amt);
//                        HashMap map1 = new HashMap();
//                        map1.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
//                        List lst1 = (List)sqlMap.executeQueryForList("getClearBalance"+objOutwardClearingPISTO.getProdType(),map1);
//    //                    System.out.println("@@@@@@@@@@@@$$%^^%listcredit"+lst1);
//                        map1=null;
//                        map1=(HashMap)lst1.get(0);
//                        double amt1 = CommonUtil.convertObjToDouble(map1.get("CLEAR_BALANCE")).doubleValue();
//                        finamt=amt1+amt;
////                        System.out.println("@@@@@@@@@@@@$$%^^%finamtcredit"+finamt);
////                        System.out.println("@@@@@@@@@@@@$$%^^%finamtdebit"+finamt);
//                        where.put("BALANCE", new Double(finamt));
//                        where.put("BATCH_ID",objOutwardClearingPISTO.getBatchId());
//                        where.put("ACT_NUM",objOutwardClearingPISTO.getAcctNo());
//                        where.put("TRANS_DT", objOutwardClearingPISTO.getPayInSlipDt());
//                        where.put("STATUS",objOutwardClearingPISTO.getStatus());
////                        System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
//                        sqlMap.executeUpdate("updatePassBookOC",where);
//                }
                }
                objLogTO.setData(objOutwardClearingPISTO.toString());
                objLogTO.setPrimaryKey(objOutwardClearingPISTO.getKeyData());
                objLogTO.setStatus(command);
                objLogDAO.addToLog(objLogTO);
                i++;
            }
        }
    }

    private void deleteData() throws Exception {
        if (listID != null && listID.size() > 0) {
            ((OutwardClearingTO) listID.get(0)).setStatus("DELETED");
            ((OutwardClearingTO) listID.get(0)).setStatusBy(objLogTO.getUserId());
            ((OutwardClearingTO) listID.get(0)).setStatusDt(currDt);
            ((OutwardClearingTO) listID.get(0)).setOutwardDt(currDt);
            ((OutwardClearingTO) listID.get(0)).setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("deleteStatusOutwardClearingTOStatus", (OutwardClearingTO) listID.get(0));
        }

        if (listPISD != null && listPISD.size() > 0) {
            ((OutwardClearingPISTO) listPISD.get(0)).setStatus("DELETED");
            ((OutwardClearingPISTO) listPISD.get(0)).setStatusBy(objLogTO.getUserId());
            ((OutwardClearingPISTO) listPISD.get(0)).setStatusDt(currDt);
            ((OutwardClearingPISTO) listPISD.get(0)).setBranchId(_branchCode);
            ((OutwardClearingPISTO) listPISD.get(0)).setPayInSlipDt(currDt);
            sqlMap.executeUpdate("deleteStatusOutwardClearingPISTOStatus", (OutwardClearingPISTO) listPISD.get(0));
            System.out.println("listvalues" + listPISD);
            OutwardClearingPISTO hash = (OutwardClearingPISTO) listPISD.get(0);
            HashMap map = new HashMap();
            map.put("STATUS", hash.getStatus());
            map.put("BATCH_ID", hash.getBatchId());
//            sqlMap.executeUpdate("deletePassBookOC",map);

            int size = listPISD.size();
            int i = 0;
            OutwardClearingPISTO objOutwardClearingPISTO;
            while (i < size) {
                objOutwardClearingPISTO = (OutwardClearingPISTO) listPISD.get(i);

                objLogTO.setData(objOutwardClearingPISTO.toString());
                objLogTO.setPrimaryKey(objOutwardClearingPISTO.getKeyData());
                objLogTO.setStatus(command);
                objLogDAO.addToLog(objLogTO);

                i++;
            }
        }
    }

    private void authorize(HashMap map, boolean isTransaction) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        String prvStatus = (String) map.get("PREVIOUS_STATUS");
        System.out.println("prvStatus-------------" + prvStatus);
        List outList = new ArrayList();
        System.out.println("selectedList.size()-------------" + selectedList.size());
        if (prvStatus == null) {

            for (int i = 0; i < selectedList.size(); i++) {
                OutwardClearingTO oct = new OutwardClearingTO();
                HashMap SelMap = new HashMap();
                SelMap = (HashMap) selectedList.get(i);
                oct.setInstrumentNo1(CommonUtil.convertObjToStr(SelMap.get("INSTRUMENT_NO1")));
                oct.setInstrumentNo2(CommonUtil.convertObjToStr(SelMap.get("INSTRUMENT_NO2")));
                oct.setBatchId(CommonUtil.convertObjToStr(SelMap.get("BID")));
                oct.setScheduleNo(CommonUtil.convertObjToStr(SelMap.get("SCHEDULE_NO")));
                oct.setAuthorizeDt(currDt);
                oct.setAuthorizeStatus(status);
                oct.setAuthorizeBy(CommonUtil.convertObjToStr(SelMap.get("USER_ID")));
                oct.setAmount(CommonUtil.convertObjToDouble(SelMap.get("AMOUNT")));
                if (listID == null) {
                    listID = new ArrayList();
                }
                listID.add(oct);
                oct = null;
                System.out.println("****listID****" + listID);
            }
//            HashMap octMap=new HashMap();
//             System.out.println("****listID****"+oct);
//            octMap.put("OutwardClearingTO",outList);
//             listID = (List) octMap.get("OutwardClearingTO");
//             System.out.println("****listID****"+listID);
        }

        System.out.println(status);
        System.out.println(selectedList);
        System.out.println("****listID****" + listID);

        HashMap dataMap = new HashMap();
        HashMap amtAndAcctNo;
        if (listID != null && listID.size() > 0) {
            for (int i = 0, j = listID.size(); i < j; i++) {
                try {
                    if (isTransaction) {
                        sqlMap.startTransaction();
                    }
                    OutwardClearingTO oct = new OutwardClearingTO();
                    oct = (OutwardClearingTO) listID.get(i);
                    dataMap.put("INSTRUMENT_NO1", oct.getInstrumentNo1());
                    dataMap.put("INSTRUMENT_NO2", oct.getInstrumentNo2());
                    dataMap.put("BID", oct.getBatchId());
                    dataMap.put("SCHEDULE_NO", oct.getBatchId());
                    dataMap.put("AUTHORIZEDT", currDt);
                    dataMap.put("AUTHORIZE_STATUS", oct.getStatus());
                    dataMap.put("AUTHORIZE_BY", oct.getAuthorizeBy());
                    dataMap.put("AUTHORIZE_REMARKS", oct.getAuthorizeRemarks());

                    // Updating the status
                    System.out.println(dataMap);
//                bounceClearingType=null;
//                if (dataMap.containsKey("BounceClearingType")) {
//                    bounceClearingType = CommonUtil.convertObjToStr(dataMap.get("BounceClearingType")).split(" ");
//                    System.out.println("bounceClearingType 0--------"+ bounceClearingType[0]);
//                    System.out.println("bounceClearingType1--------"+ bounceClearingType[1]);          
//                }

                    // Update status in Payin Slip based on Batch ID and instrument No
                    dataMap.put("STATUS", status);
                    dataMap.put("REMARKS", map.get("REMARKS"));
                    dataMap.put("USER_ID", objLogTO.getUserId());
                    dataMap.put("TODAY_DT", currDt);
                    dataMap.put("OUTWARD_AMOUNT", oct.getAmount());
                    HashMap outcountMap = new HashMap();
                    outcountMap.put("BATCH_ID", dataMap.get("BID"));
                    outcountMap.put("INITIATED_BRANCH", _branchCode);
                    outcountMap.put("OUTWARD_DT", currDt.clone());
                    List countOCRel = sqlMap.executeQueryForList("getOutwardBatchCount", outcountMap);
                    outcountMap = new HashMap();
                    outcountMap = (HashMap) countOCRel.get(0);
                    int count = CommonUtil.convertObjToInt(outcountMap.get("COUNT"));
                    if ((status.equals(CommonConstants.STATUS_REALIZED) || status.equals(CommonConstants.STATUS_REJECTED)) && count > 1) {
                        dataMap.put("OUT_AMOUNT", oct.getAmount());
                        System.out.println(dataMap.get("OUT_AMOUNT") + "^^^^^^^^^^^^^^^^^^^^^dataMap.");
                        formorethanoneRealizationANDRejection(dataMap, status);
                        System.out.println("dataMap+++++++++++++++" + dataMap);
                    } else {

//                if(!status.equals(CommonConstants.STATUS_REALIZED) && !status.equals(CommonConstants.STATUS_REJECTED))     // changed by suresh
                        dataMap.put("INITIATED_BRANCH", _branchCode);
                        dataMap.put("OUTWARD_DT", currDt.clone());
                        sqlMap.executeUpdate("authorizePayInSlip", dataMap);
                        sqlMap.executeUpdate("authorizeOutwardClearing", dataMap);

//                HashMap where = new HashMap();
//                where.put("STATUS",dataMap.get("STATUS"));
//                where.put("BATCH_ID", dataMap.get("BID"));
//                sqlMap.executeUpdate("authorizePassBookTT", where);
//                // Update status in Outward clearing based on Payin Slip status
//                List lst = sqlMap.executeQueryForList("countPayInSlip", dataMap);
//                
//                boolean execute = true;
//                HashMap countMap;
//                String countStatus;
//                for (int i2=0, j2=lst.size(); i2 < j2; i2++) {
//                    countMap = (HashMap) lst.get(i2);
//                    countStatus = CommonUtil.convertObjToStr(countMap.get("AUTHORIZE_STATUS"));
//                    if (countStatus.equals("")) {
//                        execute = false;
//                        break;
//                    } else if (countStatus.equals("EXCEPTION")) {
//                        execute = false;
//                        break;
//                    }
//                }
//                
//                if (execute) {
//                }
//              if(!prvStatus.equals(CommonConstants.STATUS_AUTHORIZED) && !status.equals(CommonConstants.STATUS_REJECTED)){
                        if (status.equals(CommonConstants.STATUS_AUTHORIZED) || status.equals(CommonConstants.STATUS_REALIZED) || status.equals(CommonConstants.STATUS_REJECTED)) {
                            HashMap bidMap = new HashMap();
                            bidMap.put("BID", dataMap.get("BID"));
//                    if(status.equals(CommonConstants.STATUS_REALIZED) || status.equals(CommonConstants.STATUS_REJECTED)) 
//                         bidMap.put("REALIZESTATUS","" );
                            bidMap.put("BRANCH_ID", _branchCode);
                            bidMap.put("PAY_IN_SLIP_DT", currDt.clone());
                            List payInSlipList = sqlMap.executeQueryForList("getListAuthorizeOutwardClearingPISTO", bidMap);
                            int j1 = payInSlipList.size();
                            if (payInSlipList != null && payInSlipList.size() > 0) {
                                boolean isMulti = false;
                                if (j1 > 1) {
                                    isMulti = true;
                                }
                                for (int i1 = 0; i1 < j1; i1++) {

                                    updateAuthorize((OutwardClearingPISTO) payInSlipList.get(i1), status, dataMap, isMulti);
                                }
                                sqlMap.executeUpdate("authorizePayInSlip", dataMap);
                            }
                        }

                    }
                    if (isTransaction) {
                        sqlMap.commitTransaction();
                    }
                } catch (Exception e) {
                    if (isTransaction) {
                        sqlMap.rollbackTransaction();
                    }
                    throw e;
                }
            }
        }
        status = null;
        selectedList = null;
        dataMap = null;
    }

    private String getBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put("WHERE", "OUTWARD.BATCH_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private HashMap getRuleMap(OutwardClearingPISTO outwardClearingPISTORuleMap, double prevAmount, boolean makeNegative) throws Exception {
        HashMap inputMap = new HashMap();
        double amount = outwardClearingPISTORuleMap.getAmount().doubleValue();

        String transType = TransactionDAOConstants.CREDIT;

        if (transType.equals(TransactionDAOConstants.DEBIT) && makeNegative) {
            amount = -amount + prevAmount;
        } else {
            amount -= prevAmount;
        }

        String acctNo = outwardClearingPISTORuleMap.getAcctNo();

        if ((acctNo == null || acctNo.equals("")) && (outwardClearingPISTORuleMap.getAcHdId() != null)) {
            acctNo = outwardClearingPISTORuleMap.getAcHdId();
        }

        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, null);
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, null);
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, null);
        inputMap.put(TransactionDAOConstants.DATE, outwardClearingPISTORuleMap.getPayInSlipDt());
        inputMap.put(TransactionDAOConstants.TRANS_TYPE, transType);
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, outwardClearingPISTORuleMap.getBranchId());
        inputMap.put(TransactionDAOConstants.TO_STATUS, outwardClearingPISTORuleMap.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, outwardClearingPISTORuleMap.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, outwardClearingPISTORuleMap.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.UNCLEAR_AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, outwardClearingPISTORuleMap.getBranchId());
        inputMap.put(TransactionDAOConstants.TRANS_ID, outwardClearingPISTORuleMap.getBatchId() + "_" + outwardClearingPISTORuleMap.getPayInSlipId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDt);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CLEARING);
        if (outwardClearingPISTORuleMap.getProdType().equals("AD") || outwardClearingPISTORuleMap.getProdType().equals("AAD") || outwardClearingPISTORuleMap.getProdType().equals("TL") || outwardClearingPISTORuleMap.getProdType().equals("ATL")) {
            inputMap.put("PROD_ID", outwardClearingPISTORuleMap.getProdId());
            inputMap.put("PROD_TYPE", outwardClearingPISTORuleMap.getProdType());
        }
        return inputMap;
    }

    private void updateAuthorize(OutwardClearingPISTO obj, String status, HashMap authMap, boolean isMulti) throws Exception {
        String outwardAcctHd = getOutwardAcctHead(obj);
        Double dblAmount = null;
        transModuleBased = TransactionFactory.createTransaction(obj.getProdType());

        if (isMulti) {
            dblAmount = obj.getAmount();
        } else {
            dblAmount = CommonUtil.convertObjToDouble(authMap.get("OUTWARD_AMOUNT")); //AMOUNT
        }

        HashMap ruleMap = getRuleMap(obj, 0.0, true);
        ruleMap.put(TransactionDAOConstants.AMT, dblAmount);
        ruleMap.put(TransactionDAOConstants.UNCLEAR_AMT, dblAmount);

        if (status.equalsIgnoreCase(CommonConstants.STATUS_REALIZED)) {
            // Exisiting status is Created or Modified
            if (obj.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                    || obj.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                System.out.println(obj);

                String ACT_NUM = obj.getAcctNo();
                String prod = obj.getProdType();
                HashMap where = new HashMap();
                where.put("ACT_NUM", ACT_NUM);
                double limit = 0.0;
                double dp = 0.0;
                if (obj.getProdType().equals("OA") || obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {
                    String type_of_tod = "";
                    List lstBal = sqlMap.executeQueryForList("getBalance" + prod, where);
                    where = null;
                    if (lstBal != null && lstBal.size() > 0) {
                        where = (HashMap) lstBal.get(0);
                        double clBal = CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue();
                        if (obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {
                            clBal = CommonUtil.convertObjToDouble(where.get("LOAN_PAID_INT")).doubleValue();
                            limit = CommonUtil.convertObjToDouble(where.get("LIMIT")).doubleValue();
                            dp = CommonUtil.convertObjToDouble(where.get("DRAWING_POWER")).doubleValue();
                            if (dp > 0 && dp < limit) {
                                limit = dp;
                            }
                        }
                        ruleMap.put("LIMIT", String.valueOf(limit));
                        double avBal = CommonUtil.convertObjToDouble(where.get("AVAILABLE_BALANCE")).doubleValue();
                        double totBal = CommonUtil.convertObjToDouble(where.get("TOTAL_BALANCE")).doubleValue();
                        double amt = CommonUtil.convertObjToDouble(obj.getAmount()).doubleValue();
                        double tod_amt = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                        double tod_util = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                        double tod_left = tod_amt - tod_util;
                        where.put("AMOUNT", obj.getAmount());
                        Date toDt = (Date) currDt.clone();
                        if (obj.getPayInSlipDt() != null) {
                            toDt.setDate(obj.getPayInSlipDt().getDate());
                            toDt.setMonth(obj.getPayInSlipDt().getMonth());
                            toDt.setYear(obj.getPayInSlipDt().getYear());
                            where.put("TODAY_DT", toDt);
                        }
                        //                                        where.put("TODAY_DT",objCashTrans.getTransDt());
                        where.put("ACT_NUM", ACT_NUM);
                        System.out.println("For Updating Balances" + where);
                        double posBal = Math.abs(clBal);
                        System.out.println("posBal" + posBal);
                        List lst = sqlMap.executeQueryForList("getTypeOfTod", where);
                        HashMap hash = new HashMap();
                        if (lst != null && lst.size() > 0) {
                            hash = (HashMap) lst.get(0);
                            type_of_tod = CommonUtil.convertObjToStr(hash.get("TYPE_OF_TOD"));

                            System.out.println(" list size " + lst.size());
                            if (type_of_tod.equals("SINGLE")) {
                                ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                ruleMap.put("LIMIT", String.valueOf(limit));
                                System.out.println(" Inside SINGLE1 " + ruleMap);
                                where.put("TODUTILIZEDCBMORE", "");
                                System.out.println(" Inside CREDIT " + ruleMap);
                                if (clBal < 0.0 && amt >= posBal) {
                                    ruleMap.put("GREATERAMTCREDIT", "");
                                } else if (clBal < 0.0 && amt < posBal) {
                                    ruleMap.put("LESSERAMTCREDIT", "");
                                } else if (clBal >= 0.0) {
                                    if (lst != null && lst.size() > 0) {
                                        if (obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {
                                            ruleMap.put("NORMALAD", "");
                                        } else {
                                            ruleMap.put("NORMAL", null);
                                        }
                                    } else {
                                        ruleMap.put("NORMAL", null);
                                    }
                                    System.out.println("ruleMap Inside CREDIT" + ruleMap);
                                }

                            } else if (type_of_tod.equals("RUNNING")) {
                                ruleMap.put("TOD_AMOUNT", String.valueOf(tod_amt));
                                ruleMap.put("TOD_UTILIZED", String.valueOf(tod_util));
                                ruleMap.put("TOD_LEFT", String.valueOf(tod_left));
                                ruleMap.put("LIMIT", String.valueOf(limit));
                                System.out.println(" Inside Running " + ruleMap);
                                where.put("TODUTILIZEDCBMORERUNNING", "");
                                if (clBal < 0.0) {
                                    ruleMap.put("GREATERAMTCREDITRUNNING", "");
                                } else if (clBal >= 0.0) {
                                    if (lst != null && lst.size() > 0) {
                                        if (obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {
                                            ruleMap.put("NORMALAD", "");
                                        } else {
                                            ruleMap.put("NORMAL", "");
                                        }
                                    } else {
                                        ruleMap.put("NORMAL", "");
                                    }
                                }
                                System.out.println("ruleMap Inside CREDIT" + ruleMap);

                            }
                        } else {
                            System.out.println(" Inside Common " + ruleMap);
                            if (obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {  //|| obj.getProdType().equals("AAD")
                                ruleMap.put("LIMIT", String.valueOf(limit));
                                ruleMap.put("NORMALWOTOD", "");
                            } else {
                                ruleMap.put("NORMAL", "");
                            }

                        }
                        System.out.println("updateTODUtilized" + where);
                        if (lst != null && lst.size() > 0) {
                            if (obj.getProdType().equals("OA") || obj.getProdType().equals("AD") || obj.getProdType().equals("AAD")) {
                                sqlMap.executeUpdate("updateTODUtilized", where);
                            }
                        }
                    }
                } else {
                    System.out.println(" Inside CommonMain " + ruleMap);
                    ruleMap.put("NORMAL", "");

                }
                //interbranch code
                objLogTO.setInitiatedBranch(CommonUtil.convertObjToStr(ruleMap.get("BRANCH_CODE")));
                ruleMap.put(TransactionDAOConstants.BATCH_ID, CommonUtil.convertObjToStr(obj.getBatchId()));
                ruleMap.put("OUTWARD_CLEARING", "OUTWARD_CLEARING");
                ruleMap.put(TransactionDAOConstants.TRANS_ID, CommonUtil.convertObjToStr(authMap.get("SCHEDULENO")));
                //end
                transModuleBased.authorizeUpdate(ruleMap, dblAmount);
                transModuleBased.performShadowAdd(ruleMap);
                transModuleBased.performOtherBalanceMinus(ruleMap);

//                 updatepassbook(obj);
            }

            // Checking for Rejected Status
        } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
            if (obj.getAuthorizeStatus() == null) {
                // Exisiting status is Created or Modified
                if (obj.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                        || obj.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                    // do nothing
                } else {
                    obj.setStatus(CommonConstants.STATUS_MODIFIED);
                    obj.setBranchId(_branchCode);
                    obj.setPayInSlipDt(currDt);
                    sqlMap.executeUpdate("rejectPayInSlipTO", obj);
                }
            } else {
                // Outward Return. This will happen after closing the schedule
                authMap.put("AMOUNT", dblAmount);
                doOutwardReturn(authMap, ruleMap);
                System.out.println("authMap++++++++++++++updateAuthorize" + authMap);
                obj.setAmount(CommonUtil.convertObjToDouble(dblAmount));
//                updatepassbook(obj,status);
            }

        } else if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
            // do nothing
        }

        objLogTO.setData(obj.toString());
        objLogTO.setPrimaryKey(obj.getKeyData());
        objLogTO.setStatus(status);

        objLogDAO.addToLog(objLogTO);

        status = null;
        obj = null;
    }

    private void doOutwardReturn(HashMap authMap, HashMap ruleMap) throws Exception {
//        ruleMap.put(TransactionDAOConstants.TRANS_TYPE, CommonConstants.DEBIT);
        //REJECT TIME CREDIT LOAN AND ADVANCES ACCOUNT NOT NEED BECAUSE ALREADY UPDATED WHEN SCHDULE CLOSED
        System.out.println("rulemap###########" + ruleMap);
//        if(ruleMap.containsKey("PROD_TYPE") && ruleMap.get("PROD_TYPE").equals("TL") || ruleMap.get("PROD_TYPE").equals("AD")){}
//        else
        ruleMap.put("CLEARING_BOUNCED", "CLEARING_BOUNCED");
        ruleMap.put("NORMALDEBIT", "");
        transModuleBased.authorizeUpdate(ruleMap, CommonUtil.convertObjToDouble(authMap.get("AMOUNT")));
        transModuleBased.performShadowAdd(ruleMap);
        transModuleBased.performOtherBalanceMinus(ruleMap);

        ReturnOfInstrumentsDAO objReturnDao = new ReturnOfInstrumentsDAO();

        ReturnOfInstrumentsTO objReturn = new ReturnOfInstrumentsTO();
        objReturn.setReturnType("INSTRUMENTWISE");
        objReturn.setBatchId(CommonUtil.convertObjToStr(authMap.get("BID")));
        objReturn.setPresentAgain("Y");
        System.out.println("authMap++++++++++++++doOutwardReturn" + authMap);
        objReturn.setClearingType(CommonUtil.convertObjToStr(authMap.get("CLEARING_TYPE")));
        objReturn.setInstrumentNo1(CommonUtil.convertObjToStr(authMap.get("INSTRUMENT_NO1")));
        objReturn.setInstrumentNo2(CommonUtil.convertObjToStr(authMap.get("INSTRUMENT_NO2")));

        HashMap whereMap = new HashMap();
        System.out.println("bounceClearingType--------" + bounceClearingType);
//        if(bounceClearingType!=null){
        System.out.println("bounceClearingType[0]    " + bounceClearingType[0] + "   bounceClearingType[1]     " + bounceClearingType[1]);
        whereMap.put("CLEARING_TYPE", bounceClearingType[0]);
        whereMap.put("BRANCH_ID", CommonUtil.convertObjToStr(authMap.get("BRANCH_ID")));
        whereMap.put("BRANCH_ID", _branchCode);
        if (bounceClearingType != null && bounceClearingType.length > 0) {
            whereMap.put("CLEARING_DATE", bounceClearingType[1]);
        }
        HashMap inwardClearingMap = (HashMap) sqlMap.executeQueryForObject("inwardClearing.getScheduleNo", whereMap);

        if (inwardClearingMap != null) {
            objReturn.setClearingDate((Date) inwardClearingMap.get("CLEARING_DT"));
            objReturn.setScheduleNo(CommonUtil.convertObjToStr(inwardClearingMap.get("SCHEDULE_NO")));
        } else {
            throw new TTException("inward is not selected Clearing is not configured.");
        }
//        }
        objReturn.setStatus(CommonConstants.STATUS_CREATED);
        objReturn.setBranchId(CommonUtil.convertObjToStr(authMap.get("BRANCH_ID")));
        objReturn.setAmount(CommonUtil.convertObjToDouble(authMap.get("AMOUNT")));
        objReturn.setAcctNo(CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.ACCT_NO)));
        objReturn.setCommand(CommonConstants.TOSTATUS_INSERT);
        System.out.println("objReturn++++++++++++++" + objReturn);

        HashMap retDaoMap = new HashMap();
        retDaoMap.put(CommonConstants.USER_ID, (String) execInputMap.get(CommonConstants.USER_ID));
        retDaoMap.put(CommonConstants.BRANCH_ID, (String) execInputMap.get(CommonConstants.BRANCH_ID));
        retDaoMap.put(CommonConstants.IP_ADDR, (String) execInputMap.get(CommonConstants.IP_ADDR));
        retDaoMap.put("ReturnOfInstrumentsTO", objReturn);

        HashMap retAuthMap = new HashMap();
        retAuthMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        retDaoMap.put(CommonConstants.AUTHORIZEMAP, retAuthMap);
        retDaoMap.put(TransactionDAOConstants.TRANS_ID, ruleMap.get(TransactionDAOConstants.TRANS_ID)); //by abi for adv TL
        objReturnDao.execute(retDaoMap, false);
    }

    private String getOutwardAcctHead(OutwardClearingPISTO obj) throws Exception {
        HashMap param = new HashMap();
//        param.put("TRANS_ID", transID);
        param.put("INITIATED_BRANCH", _branchCode);
        param.put("OUTWARD_DT", currDt.clone());
        param.put("BATCH_ID", obj.getBatchId());
        String clearingType = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getOutwardClearingType", param));

        BankClearingParameterTO objClearingParam = (BankClearingParameterTO) sqlMap.executeQueryForObject("getSelectBankClearingParameterTO", clearingType);

        return objClearingParam.getClearingHd();
    }

    public static void main(String[] str) {
        try {
            OutwardClearingDAO dao = new OutwardClearingDAO();
            OutwardClearingTO ocTO = new OutwardClearingTO();
            OutwardClearingPISTO ocPISTO = new OutwardClearingPISTO();

            ocTO.setAmount(Double.valueOf("10"));
            ocTO.setBankCode("UTI");
            ocTO.setBatchId("157");
            ocTO.setBranchCode("A");
            ocTO.setClearingType("CT");
            ocTO.setCommand("UPDATE");
            ocTO.setDrawer("D");
            ocTO.setDrawerAcctNo("SB001");
            ocTO.setInstrumentType("IT");
            ocTO.setOutwardId("OI001");
            ocTO.setPayeeName("ABC");
            ocTO.setRemarks("Remarks");
            List listID = new ArrayList();
            listID.add(ocTO);

            ocPISTO.setAcctNo("AC0001");
            ocPISTO.setAmount(Double.valueOf("10"));
            ocPISTO.setBatchId("157");
            ocPISTO.setPayInSlipId("PIS001");
            ocPISTO.setProdId("CA");
            ocPISTO.setRemarks("Remarks");
            List listPISD = new ArrayList();
            listPISD.add(ocPISTO);

            HashMap hash = new HashMap();

            hash.put("OutwardClearingTO", listID);
            hash.put("OutwardClearingPISTO", listPISD);
            hash.put("Command", CommonConstants.TOSTATUS_DELETE);
            dao.execute(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyObjects() {
        batchID = null;
        listID = null;
        listPISD = null;
        execInputMap = null;
    }

    public void updatepassbook(OutwardClearingPISTO objOutwardClearingPISTO, String status) throws Exception {
        OutwardClearingTO oct = new OutwardClearingTO();
//            if(status.equals(CommonConstants.STATUS_REJECTED))    
//                oct = (OutwardClearingTO) listID.get(0);
        HashMap map = new HashMap();
        if (objOutwardClearingPISTO.getProdType().equals("OA") || objOutwardClearingPISTO.getProdType().equals("AD") || objOutwardClearingPISTO.getProdType().equals("AAD")) {
            map.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
            double finamt = 0;
            HashMap where = new HashMap();
            if (!status.equals(CommonConstants.STATUS_REJECTED)) {
                where.put("CREDIT", objOutwardClearingPISTO.getAmount());
                where.put("PARTICULARS", "Oclg");
            } else {
                where.put("DEBIT", objOutwardClearingPISTO.getAmount());
                where.put("PARTICULARS", "ReturnOclg");
                oct = (OutwardClearingTO) listID.get(0);
                where.put("INSTRUMENT_NO2", oct.getInstrumentNo2());
            }
            HashMap map1 = new HashMap();
            map1.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
            List lst1 = (List) sqlMap.executeQueryForList("getClearBalance" + objOutwardClearingPISTO.getProdType(), map1);
            //                                System.out.println("@@@@@@@@@@@@$$%^^%listcredit"+lst1);
            map1 = null;
            map1 = (HashMap) lst1.get(0);
            finamt = CommonUtil.convertObjToDouble(map1.get("TOTAL_BALANCE")).doubleValue();
            where.put("BALANCE", new Double(finamt));
            where.put("BATCH_ID", objOutwardClearingPISTO.getBatchId());
            where.put("ACT_NUM", objOutwardClearingPISTO.getAcctNo());
            Date Dt = (Date) currDt.clone();
            if (objOutwardClearingPISTO.getPayInSlipDt() != null && objOutwardClearingPISTO.getPayInSlipDt().getDate() > 0) {
                Dt.setDate(objOutwardClearingPISTO.getPayInSlipDt().getDate());
                Dt.setMonth(objOutwardClearingPISTO.getPayInSlipDt().getMonth());
                Dt.setYear(objOutwardClearingPISTO.getPayInSlipDt().getYear());
            }
            objOutwardClearingPISTO.setPayInSlipDt(Dt);
            Dt = null;
            where.put("TRANS_DT", objOutwardClearingPISTO.getPayInSlipDt());
            where.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
            where.put("PBOOKFLAG", "0");
//             where.put("PARTICULARS","Oclg");
            where.put("INSTRUMENT_NO2", oct.getInstrumentNo2());
            //                      where.put("SLNO", "0");
            where.put("STATUS", objOutwardClearingPISTO.getStatus());
            //                                System.out.println("@@@@@@@@@@@@$$%^^%where"+where);
            sqlMap.executeUpdate("insertPassBook", where);
            where = null;
            map1 = null;
            lst1 = null;
            //                }
            //            }

        }



    }

    private void formorethanoneRealizationANDRejection(HashMap dataMap, String status) throws Exception {
        System.out.println("insid formorethanoneRealizationANDRejection");
        OutwardClearingTO oct = new OutwardClearingTO();
        OutwardClearingPISTO PISTO = new OutwardClearingPISTO();
        dataMap.put("INITIATED_BRANCH", _branchCode);
        dataMap.put("OUTWARD_DT", currDt.clone());
        sqlMap.executeUpdate("authorizeOutwardClearing", dataMap);
        HashMap bidMap = new HashMap();
        bidMap.put("BID", dataMap.get("BID"));
        List payInSlipList = sqlMap.executeQueryForList("getListAuthorizeOutwardClearingPISTO", bidMap);
        if (payInSlipList != null && payInSlipList.size() > 0) {
            PISTO = (OutwardClearingPISTO) payInSlipList.get(0);
        }
        PISTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("OUT_AMOUNT")));
        System.out.println("PISTO&&&&&&&&&&&&&&&&&&&&&&&" + PISTO);
        boolean isMulti = false;
        double minusAmt = CommonUtil.convertObjToDouble(dataMap.get("OUT_AMOUNT")).doubleValue();
        minusAmt = minusAmt * -1;
        dataMap.put("MINUSAMOUNT", new Double(minusAmt));
        dataMap.put("BATCH_ID", dataMap.get("BID"));
        dataMap.put("OUTWARD_AMOUNT", dataMap.get("OUT_AMOUNT"));
        dataMap.put("BRANH_ID", _branchCode);
        updateAuthorize(PISTO, status, dataMap, isMulti);
        sqlMap.executeUpdate("updatePartialreailzAmt", dataMap);
        System.out.println("formorethanoneRealizationANDRejection========" + dataMap);
        bidMap = new HashMap();
        bidMap.put("BID", dataMap.get("BID"));
        List conAmtLst = sqlMap.executeQueryForList("getListAuthorizeOutwardClearingPISTO", bidMap);
        if (conAmtLst != null && conAmtLst.size() > 0) {
            PISTO = new OutwardClearingPISTO();
            PISTO = (OutwardClearingPISTO) conAmtLst.get(0);
            double amt = CommonUtil.convertObjToDouble(PISTO.getAmount()).doubleValue();
            if (amt == 0) {
                minusAmt = 0.0;
                minusAmt = CommonUtil.convertObjToDouble(PISTO.getConvertAmt()).doubleValue();
                minusAmt = minusAmt * -1;
                dataMap.put("RESTAMT", new Double(minusAmt));
                dataMap.put("REALIZEDAMT", CommonUtil.convertObjToDouble(PISTO.getConvertAmt()));
                sqlMap.executeUpdate("realizePartialMap", dataMap);
            }
        }

    }
}