/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookTODAO.java
 *
 * Created on Fri Jan 23 12:22:26 IST 2004
 */
package com.see.truetransact.serverside.supporting.chequebook;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.NoCommandException;

import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookTO;
import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookStopPaymentTO;
import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookLooseLeafTO;
import com.see.truetransact.transferobject.supporting.chequebook.ECSStopPaymentTO;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.operativeaccount.ChequeBookRule;
import com.see.truetransact.businessrule.operativeaccount.ChequeLooseLeafRule;
import com.see.truetransact.businessrule.operativeaccount.ChequeStopPaymentRule;
import com.see.truetransact.businessrule.operativeaccount.BooksDangerLevelRule;
import com.see.truetransact.businessrule.operativeaccount.ChequeBooksAvailableRule;

import com.see.truetransact.serverside.supporting.inventory.InventoryDetailsDAO;
import com.see.truetransact.transferobject.supporting.inventory.InventoryDetailsTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

import com.see.truetransact.serverside.supporting.generateseries.GenerateSeries;
import java.util.Date;
/**
 * ChequeBookTO DAO.
 *
 */
public class ChequeBookDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ChequeBookTO objChequeBookTO;
    private ChequeBookStopPaymentTO objChequeBookStopPaymentTO;
    private ChequeBookLooseLeafTO objChequeBookLooseLeafTO;
    private ECSStopPaymentTO objECSStopPaymentTO;
    private final String TRANSID = "TRANSACTION_ID";
    private String stopPaymentRule = "";
    private String looseLeafRule = "";
    private String BRANCH_ID = "";
    private String USER_ID = "";
    private String TRAN_ID = "";
    private String ecsStopPaymentRule = "";
    final String YES = "YES";
    final String NO = "NO";
    String command = "";
    private final String CHEQUE_BOOK = "CB";
    private final String STOP_PAYMENT = "SP";
    private final String ECS_STOP = "ESP";
    HashMap inventoryMap = new HashMap();
    RuleContext context;
    RuleEngine engine;
    HashMap inputMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of ChequeBookTODAO
     */
    public ChequeBookDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
        String Mapped = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME));
        List list = (List) sqlMap.executeQueryForList(Mapped, where);
        returnMap.put("DATA", list);

        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO, HashMap map) throws Exception {
        if (objChequeBookTO != null) {
            getResult();
            final String chequeID = getChequeID();
            objChequeBookTO.setChqIssueId(chequeID);
            objChequeBookTO.setChqIssueDt(currDt);
            objChequeBookTO.setStatus(CommonConstants.STATUS_CREATED);
            objChequeBookTO.setStatusBy(USER_ID);
            objChequeBookTO.setStatusDt(currDt);
            objChequeBookTO.setCreatedBy(USER_ID);
            objChequeBookTO.setCreatedDt(currDt);
            sqlMap.executeUpdate("insertChequeBookTO", objChequeBookTO);
            authChequeBook(map, "INSERT");
            objLogTO.setData(objChequeBookTO.toString());
            objLogTO.setPrimaryKey(objChequeBookTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            getChequeBooksDanger();

        } else if (objChequeBookStopPaymentTO != null) {
            getResult();
            final String stopID = getStopID();
            objChequeBookStopPaymentTO.setChqStopId(stopID);
            objChequeBookStopPaymentTO.setChqStopDt(currDt);
            objChequeBookStopPaymentTO.setStopStatus(CommonConstants.STOPPED);
            objChequeBookStopPaymentTO.setStatus(CommonConstants.STATUS_CREATED);
            objChequeBookStopPaymentTO.setStatusBy(USER_ID);
            objChequeBookStopPaymentTO.setStatusDt(currDt);
            objChequeBookStopPaymentTO.setCreatedBy(USER_ID);
            objChequeBookStopPaymentTO.setCreatedDt(currDt);
            sqlMap.executeUpdate("insertChequeBookStopPaymentTO", objChequeBookStopPaymentTO);
            objLogTO.setData(objChequeBookStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objChequeBookStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objChequeBookLooseLeafTO != null) {
            final String leafID = getLeafID();
            objChequeBookLooseLeafTO.setChqLeafId(leafID);
            getResult();
            objChequeBookLooseLeafTO.setStatus(CommonConstants.STATUS_CREATED);
            objChequeBookLooseLeafTO.setStatusBy(USER_ID);
            objChequeBookLooseLeafTO.setStatusDt(currDt);
            objChequeBookLooseLeafTO.setCreatedBy(USER_ID);
            objChequeBookLooseLeafTO.setCreatedDt(currDt);
            sqlMap.executeUpdate("insertChequeBookLooseLeafTO", objChequeBookLooseLeafTO);
            objLogTO.setData(objChequeBookLooseLeafTO.toString());
            objLogTO.setPrimaryKey(objChequeBookLooseLeafTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        } else if (objECSStopPaymentTO != null) {
            getResult();
            final String stopID = getEcsStopID();
            objECSStopPaymentTO.setEcsStopId(stopID);
            objECSStopPaymentTO.setEcsStopDt(currDt);
            objECSStopPaymentTO.setEcsStopStatus(CommonConstants.STOPPED);
            objECSStopPaymentTO.setEcsStatus(CommonConstants.STATUS_CREATED);
            objECSStopPaymentTO.setEcsStatusBy(USER_ID);
            objECSStopPaymentTO.setEcsStatusDt(currDt);
            objECSStopPaymentTO.setEcsCreatedBy(USER_ID);
            objECSStopPaymentTO.setEcsCreatedDt(currDt);
            sqlMap.executeUpdate("insertEcsStopPaymentTO", objECSStopPaymentTO);
            objLogTO.setData(objECSStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objECSStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        }
    }

    // To Put the data for the Business Rules...
    private void getResult() throws Exception {
        String ACCOUNTNO = "";

        engine = new RuleEngine();
        context = new RuleContext();
        //        context.addRule(new ChequeBookRule());

        if (objChequeBookTO != null) {
            ACCOUNTNO = objChequeBookTO.getAcctNo();
            context.addRule(new ChequeBooksAvailableRule());

            inputMap.put("ITEM_SUB_TYPE", CommonUtil.convertObjToStr(objChequeBookTO.getChequeSubType()));
            inputMap.put("LEAVES_PER_BOOK", CommonUtil.convertObjToStr(objChequeBookTO.getNoLeaves()));
            inputMap.put("BRANCH_ID", BRANCH_ID);
            inputMap.put("CHEQUE_BOOKS", CommonUtil.convertObjToInt(objChequeBookTO.getNoChqBooks()));
        } else if (objChequeBookStopPaymentTO != null) {
            ACCOUNTNO = objChequeBookStopPaymentTO.getAcctNo();

            inputMap.put("BRANCH_CODE", BRANCH_ID);
            inputMap.put("INSTRUMENT1", objChequeBookStopPaymentTO.getStartChqNo1());
            inputMap.put("INSTRU1", objChequeBookStopPaymentTO.getStartChqNo2());
            inputMap.put("INSTRU2", objChequeBookStopPaymentTO.getEndChqNo2());
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                inputMap.put("CHQSTOPID", CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getChqStopId()));
            }
            if (stopPaymentRule.equalsIgnoreCase(YES)) {
                context.addRule(new ChequeStopPaymentRule());
            }

        } else if (objChequeBookLooseLeafTO != null) {
            ACCOUNTNO = objChequeBookLooseLeafTO.getAcctNo();

            inputMap.put("INSTRUMENT1", objChequeBookLooseLeafTO.getLeafNo1());
            inputMap.put("INSTRUMENT2", objChequeBookLooseLeafTO.getLeafNo2());
            inputMap.put("BRANCH_CODE", BRANCH_ID);
            if (looseLeafRule.equalsIgnoreCase(YES)) {
                context.addRule(new ChequeLooseLeafRule());
            }

        } else if (objECSStopPaymentTO != null) {
            ACCOUNTNO = objECSStopPaymentTO.getEcsAcctNo();

            inputMap.put("BRANCH_CODE", BRANCH_ID);
            inputMap.put("INSTRU1", objECSStopPaymentTO.getEcsEndChqNo1());
            inputMap.put("INSTRU2", objECSStopPaymentTO.getEcsEndChqNo2());
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                inputMap.put("ECSSTOPID", CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStopId()));
            }
            if (ecsStopPaymentRule.equalsIgnoreCase(YES)) {
//                context.addRule(new ChequeStopPaymentRule());
            }

        }

        inputMap.put("ACCOUNTNO", ACCOUNTNO);
        System.out.println("InputMap in DAO: " + inputMap);

        ArrayList list = (ArrayList) engine.validateAll(context, inputMap);
        if (list != null) {
            System.out.println("List Size > 0");
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.operativeaccount.OperativeAccountRuleHashMap");
            throw new TTException(exception);
            //sqlMap.rollbackTransaction();
        }
    }

    private void getChequeBooksDanger() throws Exception {
        engine = new RuleEngine();
        context = new RuleContext();
        context.addRule(new BooksDangerLevelRule());

        HashMap dangerMap = new HashMap();
        dangerMap.put("ITEM_SUB_TYPE", CommonUtil.convertObjToStr(objChequeBookTO.getChequeSubType()));
        dangerMap.put("LEAVES_PER_BOOK", CommonUtil.convertObjToStr(objChequeBookTO.getNoLeaves()));
        dangerMap.put("BRANCH_ID", BRANCH_ID);

        ArrayList list = (ArrayList) engine.validateAll(context, dangerMap);
        if (list != null) {
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.operativeaccount.OperativeAccountRuleHashMap");
            throw new TTException(exception);
            //sqlMap.rollbackTransaction();
        }
    }

    //__ To Generate the Cheque Issue Id...
    private String getChequeID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "CHEQUE_ISSUE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    //__ to Generate the Stop Cheque Id...

    private String getStopID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "CHEQUE_STOP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    //__ To Generate the Loose LEaf Id...

    private String getLeafID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "CHEQUE_LEAF_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getEcsStopID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "ECS_STOP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (objChequeBookTO != null) {
            getResult();
            objChequeBookTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objChequeBookTO.setStatusBy(USER_ID);
            objChequeBookTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateChequeBookTO", objChequeBookTO);
            objLogTO.setData(objChequeBookTO.toString());
            objLogTO.setPrimaryKey(objChequeBookTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objChequeBookStopPaymentTO != null) {
            getResult();
            objChequeBookStopPaymentTO.setStopStatus(CommonConstants.STOPPED);
            objChequeBookStopPaymentTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objChequeBookStopPaymentTO.setStatusBy(USER_ID);
            objChequeBookStopPaymentTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateChequeBookStopPaymentTO", objChequeBookStopPaymentTO);
            objLogTO.setData(objChequeBookStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objChequeBookStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objChequeBookLooseLeafTO != null) {
            getResult();
            objChequeBookLooseLeafTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objChequeBookLooseLeafTO.setStatusBy(USER_ID);
            objChequeBookLooseLeafTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateChequeBookLooseLeafTO", objChequeBookLooseLeafTO);
            objLogTO.setData(objChequeBookLooseLeafTO.toString());
            objLogTO.setPrimaryKey(objChequeBookLooseLeafTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objECSStopPaymentTO != null) {
            getResult();
            objECSStopPaymentTO.setEcsStopStatus(CommonConstants.STOPPED);
            objECSStopPaymentTO.setEcsStatus(CommonConstants.STATUS_MODIFIED);
            objECSStopPaymentTO.setEcsStatusBy(USER_ID);
            objECSStopPaymentTO.setEcsStatusDt(currDt);

            sqlMap.executeUpdate("updateEcsStopPaymentTO", objECSStopPaymentTO);
            objLogTO.setData(objECSStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objECSStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (objChequeBookTO != null) {
            objChequeBookTO.setStatus(CommonConstants.STATUS_DELETED);
            objChequeBookTO.setStatusBy(USER_ID);
            objChequeBookTO.setStatusDt(currDt);

            deleteInventoryDetailsData(CommonUtil.convertObjToStr(objChequeBookTO.getTransOutId()), objLogTO);

            sqlMap.executeUpdate("deleteChequeBookTO", objChequeBookTO);
            objLogTO.setData(objChequeBookTO.toString());
            objLogTO.setPrimaryKey(objChequeBookTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objChequeBookStopPaymentTO != null) {
            objChequeBookStopPaymentTO.setStatus(CommonConstants.STATUS_DELETED);
            objChequeBookStopPaymentTO.setStatusBy(USER_ID);
            objChequeBookStopPaymentTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteChequeBookStopPaymentTO", objChequeBookStopPaymentTO);
            objLogTO.setData(objChequeBookStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objChequeBookStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objChequeBookLooseLeafTO != null) {
            objChequeBookLooseLeafTO.setStatus(CommonConstants.STATUS_DELETED);
            objChequeBookLooseLeafTO.setStatusBy(USER_ID);
            objChequeBookLooseLeafTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteChequeBookLooseLeafTO", objChequeBookLooseLeafTO);
            objLogTO.setData(objChequeBookLooseLeafTO.toString());
            objLogTO.setPrimaryKey(objChequeBookLooseLeafTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } else if (objECSStopPaymentTO != null) {
            objECSStopPaymentTO.setEcsStatus(CommonConstants.STATUS_DELETED);
            objECSStopPaymentTO.setEcsStatusBy(USER_ID);
            objECSStopPaymentTO.setEcsStatusDt(currDt);

            sqlMap.executeUpdate("deleteEcsStopPaymentTO", objECSStopPaymentTO);
            objLogTO.setData(objECSStopPaymentTO.toString());
            objLogTO.setPrimaryKey(objECSStopPaymentTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        }
    }

    private void deleteInventoryDetailsData(String transID, LogTO objLogTO) throws Exception {
        System.out.println("transID: " + transID);

        InventoryDetailsTO objInventoryDetailsTO = new InventoryDetailsTO();
        objInventoryDetailsTO.setTransId(transID);

        TOHeader toHeader = new TOHeader();
        toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
        objInventoryDetailsTO.setTOHeader(toHeader);

        HashMap hash = new HashMap();
        hash.put(CommonConstants.USER_ID, objLogTO.getUserId());
        hash.put(CommonConstants.BRANCH_ID, objLogTO.getBranchId());

        hash.put("InventoryDetailsTO", objInventoryDetailsTO);
        InventoryDetailsDAO dao = new InventoryDetailsDAO();
        dao.execute(hash, false);
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        System.out.println("Map cheque book@@@" + map);
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        try {
            sqlMap.startTransaction();

            if (map.containsKey("ChequeBookTO") || map.containsKey("ChequeBookStopPaymentTO") || map.containsKey("ChequeBookLooseLeafTO") || map.containsKey("ECSStopPaymentTO")) {
                objChequeBookTO = (ChequeBookTO) map.get("ChequeBookTO");
                objChequeBookStopPaymentTO = (ChequeBookStopPaymentTO) map.get("ChequeBookStopPaymentTO");
                objChequeBookLooseLeafTO = (ChequeBookLooseLeafTO) map.get("ChequeBookLooseLeafTO");
                objECSStopPaymentTO = (ECSStopPaymentTO) map.get("ECSStopPaymentTO");
                BRANCH_ID = CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID));
                USER_ID = CommonUtil.convertObjToStr(map.get("USER_ID"));
                command = "";


                if (objChequeBookTO != null) {
                    command = objChequeBookTO.getCommand();
                    objLogTO.setStatus(command);
                    TRAN_ID = CommonUtil.convertObjToStr(map.get("TRANS_ID"));
                } else if (objChequeBookStopPaymentTO != null) {
                    command = objChequeBookStopPaymentTO.getCommand();
                    objLogTO.setStatus(command);
                    stopPaymentRule = CommonUtil.convertObjToStr(map.get("StopPaymentRule"));
                } else if (objChequeBookLooseLeafTO != null) {
                    command = objChequeBookLooseLeafTO.getCommand();
                    objLogTO.setStatus(command);
                    looseLeafRule = CommonUtil.convertObjToStr(map.get("LooseLeafRule"));
                } else if (objECSStopPaymentTO != null) {
                    command = objECSStopPaymentTO.getCommand();
                    objLogTO.setStatus(command);
                    ecsStopPaymentRule = CommonUtil.convertObjToStr(map.get("EcsStopPaymentRule"));
                } else {
                    throw new NoCommandException();
                }

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO, map);
                    if (objChequeBookTO != null) {
                        returnMap = null;
                        returnMap = new HashMap();
                        returnMap.put("CHQ_ISSUE_ID", objChequeBookTO.getChqIssueId());
                    } else if (objChequeBookStopPaymentTO != null) {
                        returnMap = new HashMap();
                        returnMap.put("CHQ_STOP_ID", objChequeBookStopPaymentTO.getChqStopId());
                    } else if (objChequeBookLooseLeafTO != null) {
                        returnMap = new HashMap();
                        returnMap.put("CHQ_LEAF_ID", objChequeBookLooseLeafTO.getChqLeafId());
                    } else if (objChequeBookStopPaymentTO != null) {
                        returnMap = new HashMap();
                        returnMap.put("ECS_STOP_ID", objECSStopPaymentTO.getEcsStopId());
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }


                objLogDAO = null;
                objLogTO = null;
                destroyObjects();
            }

            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(map);
                }
            }

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            if (e instanceof TTException) {
                TTException tte = (TTException) e;
                HashMap exceptionMap = tte.getExceptionHashMap();
                throw new TTException(exceptionMap);

            } else {
                e.printStackTrace();
                throw e;
            }
        }
        System.out.println("@@@@@@@@returnMap" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objChequeBookTO = null;
        objChequeBookStopPaymentTO = null;
        objChequeBookLooseLeafTO = null;
    }

    private void authorize(HashMap map) throws Exception {

        HashMap returnMap = new HashMap();
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        if (authMap != null) {
            HashMap dataMap;
            USER_ID = CommonUtil.convertObjToStr(map.get("USER_ID"));
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);

            for (int i = 0, j = selectedList.size(); i < j; i++) {
                dataMap = (HashMap) selectedList.get(i);

                System.out.println("DataMap in DAO: " + dataMap);
                //__ To auth the data regarding the ChequeBook Isue...
                if (dataMap.containsKey("CHQ_ISSUE_ID")) {
                    dataMap.put("TRANSACTION TYPE", "TRANS_OUT");
                    dataMap.put("ITEM_TYPE", "CHEQUES");
                    authChequeBook(dataMap, status);
                }

                //__ To auth the data regarding the ChequeStop Payment...
                if (dataMap.containsKey("CHQ_STOP_ID")) {
                    authStopPayment(dataMap, status);
                }
                if (dataMap.containsKey("ECS_STOP_ID")) {
                    authEcsStopPayment(dataMap, status);
                }
            }
        }
    }

    //__ To Authorize the ChequeBook data...
//    private HashMap authChequeBook(HashMap dataMap, String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    private void authChequeBook(HashMap dataMap, String status) throws Exception {

        HashMap cheq = new HashMap();
        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
        dataMap.put(CommonConstants.USER_ID, USER_ID);
        System.out.println("STATUS123: " + status);
        if (status.equalsIgnoreCase("INSERT")) {

            dataMap.put("NO OF LEAVES", objChequeBookTO.getNoLeaves());
//           dataMap.put("USAGE", CommonUtil.convertObjToStr(dataMap.get("chequeSubType")));
//           dataMap.put("ITEM_TYPE", "CHEQUES");
//           dataMap.put("INSTRUMENT_PREFIX", CommonUtil.convertObjToStr(dataMap.get("startChqNo1")));
//           dataMap.put("BK_SERIES_FROM", CommonUtil.convertObjToDouble(dataMap.get("chqBkSeriesFrom")));
//           dataMap.put("BK_SERIES_TO", CommonUtil.convertObjToDouble(dataMap.get("chqBkSeriesTo")));
//           dataMap.put("NO OF BOOKS", CommonUtil.convertObjToDouble(dataMap.get("noChqBooks")));
//           dataMap.put("START_LEAF_NO2", CommonUtil.convertObjToStr(dataMap.get("startChqNo2")));
//           dataMap.put("END_LEAF_NO2", CommonUtil.convertObjToStr(dataMap.get("endChqNo2")));
//           dataMap.put("PRODUCT TYPE", CommonUtil.convertObjToStr(dataMap.get("prodType")));
//                 System.out.println("Account Number==="+CommonUtil.convertObjToStr(dataMap.get("acctNo")));
//           dataMap.put("ACCOUNT NUMBER", CommonUtil.convertObjToStr(dataMap.get("acctNo")));

            dataMap.put("USAGE", objChequeBookTO.getChequeSubType());
            dataMap.put("ITEM_TYPE", "CHEQUES");
            dataMap.put("INSTRUMENT_PREFIX", objChequeBookTO.getStartChqNo1());
            dataMap.put("BK_SERIES_FROM", objChequeBookTO.getChqBkSeriesFrom());
            dataMap.put("BK_SERIES_TO", objChequeBookTO.getChqBkSeriesTo());
            dataMap.put("NO OF BOOKS", objChequeBookTO.getNoChqBooks());
            dataMap.put("START_LEAF_NO2", objChequeBookTO.getStartChqNo2());
            dataMap.put("END_LEAF_NO2", objChequeBookTO.getEndChqNo2());
            dataMap.put("PRODUCT TYPE", objChequeBookTO.getProdType());
            System.out.println("Account Number===" + objChequeBookTO.getAcctNo());
            dataMap.put("ACCOUNT NUMBER", objChequeBookTO.getAcctNo());
            //               System.out.println("dataMap%%%%: " +dataMap);
            cheq.put("USAGE", CommonUtil.convertObjToStr(dataMap.get("chequeSubType")));
            cheq.put("LEAVESNUM", CommonUtil.convertObjToDouble(dataMap.get("noLeaves")));
            cheq.put("BRANCH_ID", CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
            long availBooks = CommonUtil.convertObjToLong(dataMap.get("noChqBooks"));
            availBooks = -availBooks;
            //               System.out.println("cheq%%%%: " +cheq);
            List lst = (List) sqlMap.executeQueryForList("getItemID", cheq);
            //               System.out.println("@@@@lst"+lst);

//               if(lst!=null)
//                   if(lst.size() > 0){
//                       cheq = new HashMap();
//                       cheq = (HashMap) lst.get(0);
//                       cheq.put("ITEM ID", CommonUtil.convertObjToStr(cheq.get("ITEM_ID")));
//                       cheq.put("BOOK QUANTITY", String.valueOf(availBooks));
//                       //                       System.out.println("@@@@cheq"+cheq);
//                       sqlMap.executeUpdate("updateInventoryMasterAvailBooks", cheq);
//                       
//                   }

            HashMap resultMap = new HashMap();
            resultMap = getBookSeriesData(dataMap);
            //jiby
//               final HashMap inventoryMap = setInventoryDetailsData(dataMap,resultMap);
            //
            inventoryMap = setInventoryDetailsData(dataMap, resultMap);
//               final String transID = CommonUtil.convertObjToStr(inventoryMap.get(TRANSID));
//               final String itemID = CommonUtil.convertObjToStr(inventoryMap.get("ITEM ID"));
//               resultMap.put("TRANS_OUT_ID",transID);
//               resultMap.put("TRANSACTION ID",transID);
//               resultMap.put("ITEM ID",itemID);
//               resultMap.put(CommonConstants.AUTHORIZESTATUS, status);
//               resultMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
//               resultMap.put(CommonConstants.AUTHORIZEDT, (ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)))));
//               dataMap.put("TRANS_OUT_ID",transID);
            //__ To Authorize the data entered in the inventory Detail...

//               sqlMap.executeUpdate("authInventoryDetails", resultMap);
//               
//               sqlMap.executeUpdate("authChequeBookIssue", dataMap);

            //updateAccountHeads(dataMap, CHEQUE_BOOK) ;

        } // if AuthorizeStatus is Authorized
        else if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
            //               System.out.println("dataMap%%%%: " +dataMap);
            cheq.put("USAGE", CommonUtil.convertObjToStr(dataMap.get("USAGE")));
            cheq.put("LEAVESNUM", CommonUtil.convertObjToDouble(dataMap.get("NO OF LEAVES")));
            cheq.put("BRANCH_ID", CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
            long availBooks = CommonUtil.convertObjToLong(dataMap.get("NO OF BOOKS"));
            availBooks = -availBooks;
            //               System.out.println("cheq%%%%: " +cheq);
            List lst = (List) sqlMap.executeQueryForList("getItemID", cheq);
            //               System.out.println("@@@@lst"+lst);

            if (lst != null) {
                if (lst.size() > 0) {
                    cheq = new HashMap();
                    cheq = (HashMap) lst.get(0);
                    cheq.put("ITEM ID", CommonUtil.convertObjToStr(cheq.get("ITEM_ID")));
                    cheq.put("BOOK QUANTITY", availBooks);
                    //                       System.out.println("@@@@cheq"+cheq);
                    sqlMap.executeUpdate("updateInventoryMasterAvailBooks", cheq);

                }
            }

//               HashMap resultMap = new HashMap();
            //resultMap = getBookSeriesData(dataMap);

            // final HashMap inventoryMap = setInventoryDetailsData(dataMap,resultMap);

            String transID = CommonUtil.convertObjToStr(dataMap.get("TRANSACTION ID"));
            System.out.println("transID===++++" + transID);
//               final String itemID = CommonUtil.convertObjToStr(inventoryMap.get("ITEM ID"));
//               resultMap.put("TRANS_OUT_ID",transID);
//               resultMap.put("TRANSACTION ID",transID);
//               resultMap.put("ITEM ID",itemID);
//               resultMap.put(CommonConstants.AUTHORIZESTATUS, status);
//               resultMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
//               resultMap.put(CommonConstants.AUTHORIZEDT, (ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)))));
            dataMap.put("TRANS_OUT_ID", transID);
            //__ To Authorize the data entered in the inventory Detail...

            sqlMap.executeUpdate("authInventoryDetails", dataMap);

            sqlMap.executeUpdate("authChequeBookIssue", dataMap);

            updateAccountHeads(dataMap, CHEQUE_BOOK);

        } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
            // Exisiting status is Created or Modified
            if (!(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.STATUS)).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                    || CommonUtil.convertObjToStr(dataMap.get(CommonConstants.STATUS)).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {
                dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
            }
            sqlMap.executeUpdate("rejectChequeBookIssue", dataMap);
            sqlMap.executeUpdate("rejectInventoryDet", dataMap);
        } else if (status.equalsIgnoreCase(CommonConstants.STATUS_EXCEPTION)) {
            //__ if the Exception is selected...
            sqlMap.executeUpdate("rejectChequeBookIssue", dataMap);
            sqlMap.executeUpdate("rejectInventoryDet", dataMap);
        }

    }

    private HashMap setInventoryDetailsData(HashMap dataMap, HashMap resultData) throws Exception {
        InventoryDetailsTO objInventoryDetailsTO = new InventoryDetailsTO();
        String itemId = "";
        HashMap itemMap = new HashMap();
        itemMap.put("ITEM_SUB_TYPE", CommonUtil.convertObjToStr(dataMap.get("USAGE")));
        itemMap.put("LEAVES", CommonUtil.convertObjToDouble(dataMap.get("NO OF LEAVES")));
        itemMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
        final List list = (List) sqlMap.executeQueryForList("getInventoryMasterItemID", itemMap);
        if (list.size() > 0) {
            HashMap resultMap = (HashMap) list.get(0);
            itemId = CommonUtil.convertObjToStr(resultMap.get("ITEM_ID"));
        }

        objInventoryDetailsTO.setItemId(itemId);
        objInventoryDetailsTO.setTransType("TRANS_OUT");
        objInventoryDetailsTO.setTransDt(currDt);
        objInventoryDetailsTO.setBookQuantity(CommonUtil.convertObjToDouble(dataMap.get("NO OF BOOKS")));
        objInventoryDetailsTO.setBookSlnoFrom(CommonUtil.convertObjToDouble(dataMap.get("BK_SERIES_FROM")));
        objInventoryDetailsTO.setBookSlnoTo(CommonUtil.convertObjToDouble(dataMap.get("BK_SERIES_TO")));
        objInventoryDetailsTO.setLeavesSlnoFrom(CommonUtil.convertObjToDouble(dataMap.get("START_LEAF_NO2")));
        objInventoryDetailsTO.setLeavesSlnoTo(CommonUtil.convertObjToDouble(dataMap.get("END_LEAF_NO2")));
        objInventoryDetailsTO.setInstPrefix(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_PREFIX")));
        objInventoryDetailsTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
        objInventoryDetailsTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PRODUCT TYPE")));
        objInventoryDetailsTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
        //        objInventoryDetailsTO.setTransInId(TRAN_ID);
        objInventoryDetailsTO.setTransInId(CommonUtil.convertObjToStr(resultData.get("TRANS_ID")));

        objInventoryDetailsTO.setCreatedBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
        objInventoryDetailsTO.setCreatedDt(currDt);

        objInventoryDetailsTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
        objInventoryDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objInventoryDetailsTO.setStatusDt(currDt);

        TOHeader toHeader = new TOHeader();
        toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
        objInventoryDetailsTO.setTOHeader(toHeader);

        HashMap hash = new HashMap();
        hash.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
        hash.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
        hash.put("InventoryDetailsTO", objInventoryDetailsTO);
        InventoryDetailsDAO dao = new InventoryDetailsDAO();
        itemMap = dao.execute(hash, false);

        final String TRANSACTIONID = CommonUtil.convertObjToStr(itemMap.get(TRANSID));
        itemMap.put("ITEM ID", itemId);
        System.out.println("itemMap: " + itemMap);

        hash = null;
        return itemMap;
    }

    private HashMap getBookSeriesData(HashMap dataMap) throws Exception {
        HashMap result = new HashMap();
        System.out.println("dataMap in getBookSeriesData():  " + dataMap);

        final List transOut = sqlMap.executeQueryForList("GS.getTransSeriesOut", dataMap);
        if (transOut.size() > 0) {
            final List transBoth = sqlMap.executeQueryForList("GS.getTransSeries", dataMap);
            int size = transBoth.size();

            //__ if some series is in Use...
            if (size > 0) {
                HashMap transBothMap = (HashMap) transBoth.get(0);
                System.out.println("Data Map : " + transBothMap);

                HashMap transOutMap = (HashMap) transOut.get(0);

//                   long bothBookFrom = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_FROM"));
//                   long bothBookTo = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_TO"));
//                   
//                   long outBookTo = CommonUtil.convertObjToLong(transOutMap.get("BOOK_SLNO_TO"));
//                   long outLeavesFrom = CommonUtil.convertObjToLong(transOutMap.get("LEAVES_SLNO_FROM"));
//                   
//                   System.out.println("bothBookFrom: "+ bothBookFrom);
//                   System.out.println("bothBookTo: "+ bothBookTo);
//                   System.out.println("outBookTo: "+ outBookTo);
//                   
//                   //__ Checking is already done in the Query...
//                   //__ If the Current Series, still has some Books to be issued...
//                   
//                   //__ if the current Series is already in use...
//                   if( (outBookTo >= bothBookFrom) && (outBookTo <= bothBookTo)){
//                       result.put("BOOK_SLNO_FROM",String.valueOf(outBookTo + 1));
//                       result.put("LEAVES_SLNO_FROM",String.valueOf(outLeavesFrom+1));
//                       
//                   }else{
//                       result.put("BOOK_SLNO_FROM",String.valueOf(bothBookFrom));
//                       result.put("LEAVES_SLNO_FROM",String.valueOf(transBothMap.get("LEAVES_SLNO_FROM")));
//                   }

//                   result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
//                   result.put("INSTRUMENT_PREFIX",CommonUtil.convertObjToStr(transBothMap.get("INSTRUMENT_PREFIX")));
                result.put("TRANS_ID", CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
            }
        } else { //__ Data from New Series is to be taken...
            final List transIn = sqlMap.executeQueryForList("GS.getTransSeriesIn", dataMap);
            if (transIn.size() > 0) {
                result = (HashMap) transIn.get(0);
                System.out.println("Trans Id: " + CommonUtil.convertObjToStr(result.get("TRANS_ID")));
            }
        }
        System.out.println("ResultMap Generated: " + result);

        return result;
    }

    //__ To Authorize the StopCheque data...
    private void authStopPayment(HashMap dataMap, String status) throws Exception {
//         private void authStopPayment(HashMap dataMap, String status, LogDAO objLogDAO, LogTO objLogTO) throws Exception {

        if ((dataMap.get("AUTHSTATUS").equals("")) && (dataMap.get("STOP_STATUS").equals("REVOKED")) && (status.equals(CommonConstants.STATUS_REJECTED))) {

//            dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
//            sqlMap.executeUpdate("authStopPaymentIssue", dataMap);
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STOP_STATUS", "STOPPED");
            singleAuthorizeMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
            singleAuthorizeMap.put("CHQ_STOP_ID", dataMap.get("CHQ_STOP_ID"));
//                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            sqlMap.executeUpdate("rejectChqStopPayment", singleAuthorizeMap);
            singleAuthorizeMap = null;
        } else {
            dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
            sqlMap.executeUpdate("authStopPaymentIssue", dataMap);
        }

        // if AuthorizeStatus is Authorized
        if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
            //__ To put the Charges in the transaction Table...
            updateAccountHeads(dataMap, STOP_PAYMENT);
        }

    }

    private void authEcsStopPayment(HashMap dataMap, String status) throws Exception {
        if ((dataMap.get("AUTHSTATUS").equals("")) && (dataMap.get("STOP_STATUS").equals("REVOKED")) && (status.equals(CommonConstants.STATUS_REJECTED))) {

            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STOP_STATUS", "STOPPED");
            singleAuthorizeMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
            singleAuthorizeMap.put("ECS_STOP_ID", dataMap.get("ECS_STOP_ID"));
            sqlMap.executeUpdate("rejectChqStopPayment", singleAuthorizeMap);
            singleAuthorizeMap = null;
        } else {
            dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
            sqlMap.executeUpdate("authEcsStopPaymentIssue", dataMap);
        }
        if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
            //__ To put the Charges in the transaction Table...
            updateAccountHeads(dataMap, ECS_STOP);
        }
    }

    private void updateAccountHeads(HashMap dataMap, String screen) throws Exception {
        TransferTrans trans = new TransferTrans();
        double charges = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();

        //__ if the Charge(s) to be applied is/are greater than 0
        if (charges > 0) {
            final String PRODTYPE = CommonUtil.convertObjToStr(dataMap.get("PRODUCT TYPE"));
            List list = sqlMap.executeQueryForList("ChequeIssue.getAcctHeadParam" + PRODTYPE, CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID")));
            int length = list.size();
            if (list.size() > 0) {
                HashMap resultMap = (HashMap) list.get(0);
                HashMap map = new HashMap();
                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID")));
                map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
                String act_num = CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER"));

                if (screen.equalsIgnoreCase(CHEQUE_BOOK)) {
                    map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("AC_HD")));
                    map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("CHQ_ISSUE")));
                    map.put(TransferTrans.PARTICULARS, "Cheque book Issue charges");

                } else {
                    map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("AC_HD")));
                    map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("CHQ_STOP")));
                    if (screen.equalsIgnoreCase(STOP_PAYMENT)) {
                        map.put(TransferTrans.PARTICULARS, "Cheque Stop Payment charges" + CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
                    } else {
                        map.put(TransferTrans.PARTICULARS, "Ecs Stop Payment charges" + CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
                    }

                }

                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(dataMap.get("PRODUCT TYPE")));
//                map.put("PARTICULARS","By Stop Payment Charges");
                ArrayList batchList = new ArrayList();
                batchList.add(trans.getDebitTransferTO(map, charges));
                batchList.add(trans.getCreditTransferTO(map, charges));
                trans.setInitiatedBranch(_branchCode);
                trans.doDebitCredit(batchList, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                batchList = null;
                if (dataMap.containsKey("SERVICE_TAX")) {
                    ServiceTaxMaintenanceGroupDAO serTaxChg = new ServiceTaxMaintenanceGroupDAO();
                    HashMap stMap = new HashMap();
                    if (screen.equalsIgnoreCase(CHEQUE_BOOK)) {
                        map.put(TransferTrans.PARTICULARS, "Cheque book Issue charges ServiceTax");
                    } else if (screen.equalsIgnoreCase(STOP_PAYMENT)) {
                        map.put(TransferTrans.PARTICULARS, "Cheque StopPayment charges ServiceTax" + CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
                    } else {
                        map.put(TransferTrans.PARTICULARS, "Ecs StopPayment charges ServiceTax" + CommonUtil.convertObjToStr(dataMap.get("ACCOUNT NUMBER")));
                    }
                    map.put("CHARGE_TYPE", "CHEQUE_BOOK_ISSUE_CHG");
                    map.put("CAL_AMT", new Double(charges));
                    map.put("CALCULATION_TYPE", "AFTER_AUTH");
                    map.put("ACT_NUM", map.get(TransferTrans.DR_ACT_NUM));
                    map.put("ST_CAL", "ST_CAL");
                    map.put("PROD_TYPE", CommonUtil.convertObjToStr(map.get(TransferTrans.DR_PROD_TYPE)));
                    map.put("PROD_ID", CommonUtil.convertObjToStr(map.get(TransferTrans.DR_PROD_ID)));
                    map.put("AC_HD_ID", CommonUtil.convertObjToStr(map.get(TransferTrans.DR_AC_HD)));
                    System.out.println("CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)    " + CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                    map.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                    map.put("BRANCH_ID", CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID)));
                    System.out.println("inside SERVICE_TAX" + map);
                    serTaxChg.execute(map);
//                    map=null;

                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        HashMap map = CommonUtil.serializeObjRead("D:\\t1.txt");
        ChequeBookDAO cb = new ChequeBookDAO();
        cb.execute(map);
        cb = null;
        map = null;
    }
}
