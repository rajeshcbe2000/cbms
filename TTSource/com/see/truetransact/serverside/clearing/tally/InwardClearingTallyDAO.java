/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingTallyDAO.java
 *
 * Created on Wed Mar 17 18:11:59 IST 2004
 */
package com.see.truetransact.serverside.clearing.tally;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.clearing.tally.InwardClearingTallyTO;
import com.see.truetransact.transferobject.clearing.tally.InwardTallyDetailsTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.clearing.banklevel.BankClearingParameterTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * InwardClearingTally DAO.
 *
 */
public class InwardClearingTallyDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InwardClearingTallyTO objInwardClearingTallyTO;
    private List objList;
    private Date currDt = null;
    /**
     * Creates a new instance of InwardClearingTallyDAO
     */
    public InwardClearingTallyDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();

        //String where = CommonUtil.convertObjToStr(map.get("SCHEDULE_NO"));
        List list = (List) sqlMap.executeQueryForList("getSelectInwardClearingTallyTO", map);
        returnMap.put("InwardClearingTallyTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectInwardTallyDetailsTO", map);
        returnMap.put("InwardTallyDetailsTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            final String scheduleID = getInwardScheduleNo();
            final String inwardTallyID = getInwardTallyID();
            final String inwardTallyStatus = CommonConstants.STATUS_CREATED;
            InwardTallyDetailsTO objInwardTallyDetailsTO;
            objInwardClearingTallyTO.setTallyId(inwardTallyID);
            objInwardClearingTallyTO.setStatus(inwardTallyStatus);
            objInwardClearingTallyTO.setScheduleNo(scheduleID);

            //            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertInwardClearingTallyTO", objInwardClearingTallyTO);

            int size = objList.size();
            for (int i = 0; i < size; i++) {
                objInwardTallyDetailsTO = (InwardTallyDetailsTO) objList.get(i);
                objInwardTallyDetailsTO.setTallyId(objInwardClearingTallyTO.getTallyId());
                objInwardTallyDetailsTO.setScheduleNo(objInwardClearingTallyTO.getScheduleNo());
                sqlMap.executeUpdate("insertInwardTallyDetailsTO", objInwardTallyDetailsTO);
            }

            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getInwardTallyID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "INWARD.TALLY_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getInwardScheduleNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "INWARD.SCHEDULE_NO");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData() throws Exception {
        try {
            final String inwardTallyStatus = CommonConstants.STATUS_MODIFIED;
            objInwardClearingTallyTO.setStatus(inwardTallyStatus);
            //            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateInwardClearingTallyTO", objInwardClearingTallyTO);

            /* Folowing code 'll delete the existing records from
             Inward_Tally_detail table and insert records in objList
             one by one.
             */
            InwardTallyDetailsTO objInwardTallyDetailsTO = new InwardTallyDetailsTO();
            objInwardTallyDetailsTO.setTallyId(objInwardClearingTallyTO.getTallyId());
            sqlMap.executeUpdate("deleteInwardTallyDetailsTO", objInwardTallyDetailsTO);
            objInwardTallyDetailsTO = null;

            int size = objList.size();
            for (int i = 0; i < size; i++) {
                sqlMap.executeUpdate("insertInwardTallyDetailsTO", (InwardTallyDetailsTO) objList.get(i));
            }

            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            final String inwardTallyStatus = CommonConstants.STATUS_DELETED;
            objInwardClearingTallyTO.setStatus(inwardTallyStatus);
            //            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteInwardClearingTallyTO", objInwardClearingTallyTO);
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            InwardClearingTallyDAO dao = new InwardClearingTallyDAO();
            InwardClearingTallyTO objInwardClearingTallyTO = new InwardClearingTallyTO();
            InwardTallyDetailsTO objInwardTallyDetailsTO = new InwardTallyDetailsTO();
            List objList = new ArrayList();
            /*
             TOHeader toHeader = new TOHeader();
             toHeader.setCommand(CommonConstants.TOSTATUS_UPDATE);//To tell what to do... Insert, Update, Delete...
             
             objInwardClearingTallyTO.setTOHeader(toHeader);
             objInwardClearingTallyTO.setTallyId("I001001");
             objInwardClearingTallyTO.setClearingType("HIGH_VALUE");
             objInwardClearingTallyTO.setScheduleNo("S1001");
             objInwardClearingTallyTO.setClearingDt(currDt);
             
             objInwardTallyDetailsTO.setTallyId("I001001");
             objInwardTallyDetailsTO.setScheduleNo("S1001");
             objInwardTallyDetailsTO.setCurrency("USD");
             objInwardTallyDetailsTO.setServInstruments(new Double(10));
             objInwardTallyDetailsTO.setServAmount(new Double(10));
             objInwardTallyDetailsTO.setSysBookedInstruments(new Double(10));
             objInwardTallyDetailsTO.setSysBookedAmount(new Double(10));
             objInwardTallyDetailsTO.setSysOutretInstruments(new Double(10));
             objInwardTallyDetailsTO.setSysOutretAmount(new Double(10));
             objInwardTallyDetailsTO.setPhyInstruments(new Double(10));
             objInwardTallyDetailsTO.setPhyAmount(new Double(10));
             objInwardTallyDetailsTO.setDiffInstruments(new Double(10));
             objInwardTallyDetailsTO.setDiffAmount(new Double(10));
             objList.add(objInwardTallyDetailsTO);
             
             objInwardTallyDetailsTO = new InwardTallyDetailsTO();
             objInwardTallyDetailsTO.setTallyId("I001001");
             objInwardTallyDetailsTO.setScheduleNo("S1001");
             objInwardTallyDetailsTO.setCurrency("INR");
             objInwardTallyDetailsTO.setServInstruments(new Double(20));
             objInwardTallyDetailsTO.setServAmount(new Double(20));
             objInwardTallyDetailsTO.setSysBookedInstruments(new Double(20));
             objInwardTallyDetailsTO.setSysBookedAmount(new Double(20));
             objInwardTallyDetailsTO.setSysOutretInstruments(new Double(20));
             objInwardTallyDetailsTO.setSysOutretAmount(new Double(20));
             objInwardTallyDetailsTO.setPhyInstruments(new Double(20));
             objInwardTallyDetailsTO.setPhyAmount(new Double(20));
             objInwardTallyDetailsTO.setDiffInstruments(new Double(20));
             objInwardTallyDetailsTO.setDiffAmount(new Double(20));
             objList.add(objInwardTallyDetailsTO);
             */
            HashMap hash = new HashMap();
            //hash.put("InwardClearingTallyTO",objInwardClearingTallyTO);
            //hash.put("InwardTallyDetailsTO",objList);
            hash.put(CommonConstants.MAP_WHERE, "S1003");
            hash = dao.executeQuery(hash);
            //dao.execute(hash);
            System.out.println(hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            if (map.containsKey("Mode") && map.get("Mode").equals("CLOSE")) {
                doShortExcessTrans(map);
                return null;
            } else {

                // Log DAO
                LogDAO objLogDAO = new LogDAO();

                // Log Transfer Object
                LogTO objLogTO = new LogTO();
                objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
                objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
                objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
                objLogTO.setModule((String) map.get(CommonConstants.MODULE));
                objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));



                objInwardClearingTallyTO = (InwardClearingTallyTO) map.get("InwardClearingTallyTO");
                objList = (List) map.get("InwardTallyDetailsTO");

                final String command = objInwardClearingTallyTO.getCommand();

                sqlMap.startTransaction();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
                objLogTO.setData(objInwardClearingTallyTO.toString());
                objLogTO.setPrimaryKey(objInwardClearingTallyTO.getKeyData());
                objLogTO.setStatus(command);

                objLogDAO.addToLog(objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

        destroyObjects();
        return null;

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInwardClearingTallyTO = null;
        objList = null;
    }

    public void doShortExcessTrans(HashMap Map) throws Exception {
        ArrayList trfLst = new ArrayList();
        double intTrfAmt = 0.0;

        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(CommonUtil.convertObjToStr(Map.get("BRANCH_ID")));
        String clearingType = CommonUtil.convertObjToStr(Map.get("SCHEDULE_TYPE"));
        List lst = sqlMap.executeQueryForList("getSelectBankClearingParameterTO", clearingType);
        BankClearingParameterTO bankClrPara = new BankClearingParameterTO();
        if (lst != null && lst.size() > 0) {
            bankClrPara = (BankClearingParameterTO) lst.get(0);
            HashMap txMap = new HashMap();
            //                txMap.put(TransferTrans.DR_PROD_ID,"GL");
            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(Map.get("BRANCH_ID")));
            txMap.put(TransferTrans.DR_PROD_TYPE, "GL");
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(Map.get("SCHEDULE_NO")) + "ExcessShort");
            txMap.put(TransferTrans.CURRENCY, "INR");
            //       txMap.put(TransferTrans.DR_BRANCH,branch);


            if (Map.containsKey("TRNEXCESS") && Map.get("TRNEXCESS").equals("EXCESS")) {
                if (bankClrPara.getExcessClaimHd() != null && bankClrPara.getExcessClaimHd().length() > 0) {
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(bankClrPara.getExcessClaimHd()));
                    intTrfAmt = CommonUtil.convertObjToDouble(Map.get("Excess")).doubleValue();
                } else {
                    throw new TTException("EXcess AccountHead is not set");
                }
            } else {
                if (bankClrPara.getExcessClaimHd() != null && bankClrPara.getClearingHd().length() > 0) {
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(bankClrPara.getClearingHd()));
                    intTrfAmt = CommonUtil.convertObjToDouble(Map.get("Short")).doubleValue();
                } else {
                    throw new TTException("Clearing AccountHead is not set");
                }
            }
            trfLst.add(trans.getDebitTransferTO(txMap, intTrfAmt));
            txMap = new HashMap();
            //                txMap.put(TransferTrans.CR_AC_HD,"GL");
            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(Map.get("BRANCH_ID")));
            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(Map.get("SCHEDULE_NO")) + "ExcessShort");
            txMap.put(TransferTrans.CURRENCY, "INR");
            if (Map.containsKey("TRNEXCESS") && Map.get("TRNEXCESS").equals("EXCESS")) {
                if (bankClrPara.getExcessClaimHd() != null && bankClrPara.getClearingHd().length() > 0) {
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(bankClrPara.getClearingHd()));
                    intTrfAmt = CommonUtil.convertObjToDouble(Map.get("Excess")).doubleValue();
                } else {
                    throw new TTException("Clearing AccountHead is not set");
                }
            } else {
                if (bankClrPara.getExcessClaimHd() != null && bankClrPara.getShortClaimHd().length() > 0) {
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(bankClrPara.getShortClaimHd()));
                    intTrfAmt = CommonUtil.convertObjToDouble(Map.get("Short")).doubleValue();
                } else {
                    throw new TTException("Short AccountHead is not set");
                }
            }
            trfLst.add(trans.getCreditTransferTO(txMap, intTrfAmt));
            trans.doDebitCredit(trfLst, CommonUtil.convertObjToStr(Map.get("BRANCH_ID")));
        } else {
            throw new TTException("Clearing Parameter is Not Set");
        }

    }
}
