/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseDAO.java
 *
 * Created on Wed Apr 07 18:38:06 GMT+05:30 2004
 */
package com.see.truetransact.serverside.clearing.bouncing;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.clearing.outward.OutwardClearingDAO;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.clearing.bouncing.BouncingInstrumentwiseTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.transferobject.transaction.clearing.outward.*;
import com.see.truetransact.transferobject.transaction.clearing.InwardClearingTO;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.HashMap;
import java.util.Date;

/**
 * BouncingInstrumentwise DAO.
 *
 */
public class BouncingInstrumentwiseDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BouncingInstrumentwiseTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private String inward_Bouncing_ID;
    private IDGenerateDAO dao;
    private HashMap where;
    private String userID = "";
    private String branchID = "";
    private HashMap returnRemarksMap;
    private Date currDt = null;

    /**
     * Creates a new instance of BouncingInstrumentwiseDAO
     */
    public BouncingInstrumentwiseDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList("getSelectBouncingInstrumentwiseTO", where);
        returnMap.put("BouncingInstrumentwiseTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        inward_Bouncing_ID = getInwardBouncingID();
        objTO.setBouncingId(inward_Bouncing_ID);

        objTO.setCreatedBy(userID);
        objTO.setCreatedDt(currDt);

        objTO.setStatus(CommonConstants.STATUS_CREATED);
        objTO.setStatusBy(userID);
        objTO.setStatusDt(currDt);

        sqlMap.executeUpdate("insertBouncingInstrumentwiseTO", objTO);

        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());

        logDAO.addToLog(logTO);
    }

    private String getInwardBouncingID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "INWARD.BOUNCING_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData() throws Exception {
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objTO.setStatusBy(userID);
        objTO.setStatusDt(currDt);

        sqlMap.executeUpdate("updateBouncingInstrumentwiseTO", objTO);

        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());

        logDAO.addToLog(logTO);
    }

    private void deleteData() throws Exception {
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        objTO.setStatusBy(userID);
        objTO.setStatusDt(currDt);

        sqlMap.executeUpdate("deleteBouncingInstrumentwiseTO", objTO);

        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());

        logDAO.addToLog(logTO);
    }

    /*
     public static void main(String str[]) {
     
     try {
     BouncingInstrumentwiseDAO objBouncingInstrumentwiseDAO = new BouncingInstrumentwiseDAO();
     BouncingInstrumentwiseTO objTO = new BouncingInstrumentwiseTO();
     TOHeader toHeader = new TOHeader();
     toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     objTO.setTOHeader(toHeader);
     
     objTO.setBouncingId("IB001026");
     objTO.setBouncingType("ee");
     objTO.setClearingSlNo("ee");
     objTO.setInwardScheduleNo("ee");
     objTO.setBouncingReason("ee");
     objTO.setPresentAgain("e");
     objTO.setClearingType("ee");
     objTO.setClearingDate(DateUtil.getDateMMDDYYYY(null));
     
     HashMap hash = new HashMap();
     hash.put("BouncingInstrumentwiseTO",objTO);
     objBouncingInstrumentwiseDAO.execute(hash);
     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }
     */
    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {

            if (isTransaction) {
                sqlMap.startTransaction();
            }

            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
            branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));

            //__ if the Map Contains the BouncingInstrumentwiseTO Obj...
            if (map.containsKey("BouncingInstrumentwiseTO")) {
                objTO = (BouncingInstrumentwiseTO) map.get("BouncingInstrumentwiseTO");
                final String command = objTO.getCommand();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            }

            if (isTransaction) {
                sqlMap.commitTransaction();
            }

        } catch (Exception e) {
            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            e.printStackTrace();
            throw new TransRollbackException(e);
        }

        //__ if the Map Contains the Data for the Authorization...
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            //__ It the AuthMap is not Null...
            if (!authMap.containsKey(CommonConstants.AUTHORIZEDATA)) {
                ArrayList lstBounce = new ArrayList();
                HashMap lstMap = new HashMap();
                lstMap.put("BOUNCING ID", inward_Bouncing_ID);
                lstMap.put("INWARD ID", objTO.getInwardId());
                lstMap.put("BRANCH_CODE", objTO.getBranchId());
                lstBounce.add(lstMap);
                authMap.put(CommonConstants.AUTHORIZEDATA, lstBounce);
            }

            if (authMap != null) {
                authorize(authMap, logDAO, logTO, isTransaction);
            }
        }

        map = null;
        destroyObjects();
        return returnRemarksMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO, boolean isTransaction) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
//        Date currDt = currDt;

        for (int i = 0, j = selectedList.size(); i < j; i++) {
            try {

                if (isTransaction) {
                    sqlMap.startTransaction();
                }

                dataMap = (HashMap) selectedList.get(i);
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.AUTHORIZEDT, objLogTO.getApplDt());
                dataMap.put(CommonConstants.USER_ID, objLogTO.getUserId());
                System.out.println("Data Map in Clearing DAO: " + dataMap);
                sqlMap.executeUpdate("authorizeBouncingInstrument", dataMap);

                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    dataMap.put("INITIATED_BRANCH", _branchCode);
                    dataMap.put("TRANS_DT", currDt.clone());
                    List transList = sqlMap.executeQueryForList("Bouncing.getTransactionData", dataMap);
                    System.out.println("transList: " + transList);
                    if (transList.size() > 0) {
                        HashMap resultMap = (HashMap) transList.get(0);
                        System.out.println("resultMap: " + resultMap);
                        String prodType = CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE"));
                        double inwardAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
                        double balAmt = 0;
                        if (!prodType.equals("RM")) {
                            HashMap actMap = new HashMap();
                            actMap.put("ACT_NUM", resultMap.get("ACCT_NO"));
                            List lst = sqlMap.executeQueryForList("getClearBalanceForChg" + prodType, actMap);
                            if (lst != null && lst.size() > 0) {
                                actMap = (HashMap) lst.get(0);
                                balAmt = CommonUtil.convertObjToDouble(actMap.get("TOTAL_BALANCE")).doubleValue();
                            }
                        }
                        updateClearingSuspense(resultMap);
                        if (!prodType.equals("RM") && balAmt < inwardAmt) {
                            updateTranCharges(resultMap, dataMap);
                        }

                        HashMap returnMap = new HashMap();
                        HashMap where = new HashMap();
                        where.put("BRANCH_ID", objLogTO.getBranchId());
                        where.put("INWARD_ID", dataMap.get("INWARD ID"));
                        where.put("TRANS_DT", currDt.clone());
                        where.put("INITIATED_BRANCH", _branchCode);
                        list = (List) sqlMap.executeQueryForList("getSelectInwardClearingTO", where);
                        if (list.size() > 0) {
                            InwardClearingTO res = (InwardClearingTO) list.get(0);
                            OutwardClearingTO objOutwardClearingTO = new OutwardClearingTO();
                            objOutwardClearingTO.setBatchId(CommonUtil.convertObjToStr(res.getInwardId()));
                            Date Dt = (Date) currDt.clone();
                            if (res.getInwardDt() != null && res.getInwardDt().getDate() > 0) {
                                Dt.setDate(res.getInwardDt().getDate());
                                Dt.setMonth(res.getInwardDt().getMonth());
                                Dt.setYear(res.getInwardDt().getYear());
                            }
                            res.setInwardDt(Dt);
                            objOutwardClearingTO.setOutwardDt(res.getInwardDt());

                            objOutwardClearingTO.setInstrumentType(CommonUtil.convertObjToStr(res.getInstrumentType()));
                            objOutwardClearingTO.setInstrumentNo1(CommonUtil.convertObjToStr(res.getInstrumentNo1()));
                            objOutwardClearingTO.setInstrumentNo2(CommonUtil.convertObjToStr(res.getInstrumentNo2()));
                            objOutwardClearingTO.setInstrumentDt(res.getInstrumentDt());
                            objOutwardClearingTO.setAmount(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(res.getAmount())));
                            objOutwardClearingTO.setPayeeName(CommonUtil.convertObjToStr(res.getPayeeName()));
                            objOutwardClearingTO.setDrawer(null);
                            objOutwardClearingTO.setDrawerAcctNo(null);
                            objOutwardClearingTO.setBankCode(CommonUtil.convertObjToStr(res.getBankCode()));
                            objOutwardClearingTO.setBranchCode(CommonUtil.convertObjToStr(res.getBranchCode()));
                            objOutwardClearingTO.setRemarks(CommonUtil.convertObjToStr(res.getAuthorizeRemarks()));
                            objOutwardClearingTO.setCreatedDt(currDt);
                            objOutwardClearingTO.setStatusDt(currDt);
                            objOutwardClearingTO.setStatusBy(res.getSuserId());
                            objOutwardClearingTO.setInitiatedBranch(CommonUtil.convertObjToStr(res.getInitiatedBranch()));

                            list = (List) sqlMap.executeQueryForList("getSelectBouncingInstrumentwiseTO", dataMap.get("BOUNCING ID"));
                            if (list.size() > 0) {
                                BouncingInstrumentwiseTO bounMap = (BouncingInstrumentwiseTO) list.get(0);
                                objOutwardClearingTO.setScheduleNo(CommonUtil.convertObjToStr(bounMap.getScheduleNo()));
                                objOutwardClearingTO.setClearingType(CommonUtil.convertObjToStr(bounMap.getClearingType()));
                            }

                            ArrayList listID = new ArrayList();
                            listID.add(objOutwardClearingTO);
                            OutwardClearingPISTO objOutwardClearingPISTO = new OutwardClearingPISTO();
                            objOutwardClearingPISTO.setBatchId(CommonUtil.convertObjToStr(res.getInwardId()));
                            objOutwardClearingPISTO.setPayInSlipDt(currDt);
                            objOutwardClearingPISTO.setProdId(null);
                            objOutwardClearingPISTO.setAcctNo(null);
                            objOutwardClearingPISTO.setAmount(CommonUtil.convertObjToDouble(res.getAmount()));
                            objOutwardClearingPISTO.setRemarks(CommonUtil.convertObjToStr(res.getAuthorizeRemarks()));
                            objOutwardClearingPISTO.setAcHdId(CommonUtil.convertObjToStr(resultMap.get("CLEARING_SUSPENSE_HD")));
                            objOutwardClearingPISTO.setProdType("GL");
                            objOutwardClearingPISTO.setStatusBy(res.getSuserId());
                            ArrayList listPISD = new ArrayList();
                            listPISD.add(objOutwardClearingPISTO);

                            HashMap outwardMap = new HashMap();
                            outwardMap.put("OutwardClearingTO", listID);
                            outwardMap.put("OutwardClearingPISTO", listPISD);
                            outwardMap.put("Command", "INSERT");
                            outwardMap.put("USER_ID", objLogTO.getUserId());
                            outwardMap.put("OLDAMOUNT", null);
                            outwardMap.put("IP_ADDR", objLogTO.getIpAddr());
                            outwardMap.put("REALIZEMAP", null);
                            outwardMap.put("BRANCH_CODE", objLogTO.getBranchId());
                            OutwardClearingDAO outward = new OutwardClearingDAO();
                            System.out.println("-----outwardmap-----" + outwardMap);

                            outward.execute(outwardMap, false);
                        }
                    }
                }

                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("BOUNCING ID")));
                objLogTO.setStatus(status);

                objLogDAO.addToLog(objLogTO);

                if (isTransaction) {
                    sqlMap.commitTransaction();
                }

            } catch (Exception e) {
                if (isTransaction) {
                    sqlMap.rollbackTransaction();
                }
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void updateClearingSuspense(HashMap dataMap) throws Exception {
        HashMap map = new HashMap();
        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))); // prod a/c head
        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))); // ic a/c no branch
        map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))); // local logged branch
        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
        map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_HD"))); // charge a/c get it from clearing_bank_param
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
        map.put(TransferTrans.PARTICULARS, "ChequeReturn ");

        TransferTrans trans = new TransferTrans();
        trans.setTransMode(CommonConstants.TX_CLEARING);
        trans.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID")));
        trans.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NO")));
        ArrayList batchList = new ArrayList();
        double amt = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
        batchList.add(trans.getDebitTransferTO(map, amt));
        batchList.add(trans.getCreditTransferTO(map, amt));
        trans.doDebitCredit(batchList, branchID);
        batchList = null;
    }

    private void updateTranCharges(HashMap resultMap, HashMap dataMap) throws Exception {
        returnRemarksMap = new HashMap();
        Double chrg = CommonUtil.convertObjToDouble(resultMap.get("INWARD_RETURN_CHRG"));
        double charges = chrg.doubleValue();
        String actNum = CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"));
        resultMap.put("ACCOUNTNO", actNum);
        String prodType = CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE"));
        if (actNum.length() > 0) {
            List list = (List) sqlMap.executeQueryForList("get" + prodType + "Balance", resultMap);
            HashMap outputMap = (HashMap) list.get(0);

            double availableBalance = CommonUtil.convertObjToDouble(outputMap.get("AVAILABLE_BALANCE")).doubleValue();
            double limit = CommonUtil.convertObjToDouble(outputMap.get("TOD_LIMIT")).doubleValue();
            String status = CommonUtil.convertObjToStr(outputMap.get("STATUS"));
            Date lastTransDt = (Date) outputMap.get("LAST_TRANS_DT");

            if (status.equals("COMP_FREEZE")) {
                returnRemarksMap.put("REMARKS", TransactionConstants.COMP_FREEZE);
                charges = 0;
            } else {
                if ((availableBalance + limit) < charges) {
                    returnRemarksMap.put("REMARKS", TransactionConstants.INSUFFICIENT_BALANCE);
                    charges = 0;
                }
            }

        }
        if (charges > 0 && !actNum.equals("")) {
            if (resultMap.containsKey("AC_HD_ID") && resultMap.get("AC_HD_ID") != null) {
                HashMap map = new HashMap();
                map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))); // Inward clearing a/c productid
                map.put(TransferTrans.DR_ACT_NUM, actNum); // ic a/c no
                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID"))); // prod a/c head
                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // ic a/c no branch
                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // local logged branch
                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("INWARD_RETURN_HD"))); // charge a/c get it from clearing_bank_param
                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
                map.put(TransferTrans.PARTICULARS, "ChequeReturn Charges");

                TransferTrans trans = new TransferTrans();
                trans.setInitiatedBranch(CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID")));
                trans.setLinkBatchId(actNum);
                ArrayList batchList = new ArrayList();
                batchList.add(trans.getDebitTransferTO(map, charges));
                batchList.add(trans.getCreditTransferTO(map, charges));

                trans.doDebitCredit(batchList, branchID);
                batchList = null;
                ServiceTaxMaintenanceGroupDAO serTaxChg = new ServiceTaxMaintenanceGroupDAO();
                HashMap stMap = new HashMap();
                resultMap.put("CHARGE_TYPE", "INWARD_RETURN_CHRG");
                resultMap.put("CAL_AMT", new Double(charges));
                resultMap.put("CALCULATION_TYPE", "AFTER_AUTH");
                resultMap.put("ACT_NUM", map.get(TransferTrans.DR_ACT_NUM));
                resultMap.put("ST_CAL", "ST_CAL");
                resultMap.put(TransferTrans.PARTICULARS, "Chq Return Charges ServiceTax");
                resultMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID")));
                serTaxChg.execute(resultMap);

                dataMap.put("CHRG", chrg);
                sqlMap.executeUpdate("updateBounchingCharge", dataMap);
            }
        }
    }

    private void destroyObjects() {
        objTO = null;
        logDAO = null;
        logTO = null;
        inward_Bouncing_ID = null;
    }
}
