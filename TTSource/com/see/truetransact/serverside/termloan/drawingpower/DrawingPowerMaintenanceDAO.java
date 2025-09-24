/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DrawingPowerMaintenanceDAO.java
 *
 * Created on Fri Jul 16 16:45:20 GMT+05:30 2004
 */
package com.see.truetransact.serverside.termloan.drawingpower;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.termloan.drawingpower.DrawingPowerMaintenanceTO;
import com.see.truetransact.transferobject.termloan.drawingpower.DrawingPowerMaintenanceDetailsTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.serverside.operativeaccount.TodAllowedDAO;

/**
 * DrawingPowerMaintenance DAO.
 *
 */
public class DrawingPowerMaintenanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO;
    private DrawingPowerMaintenanceDetailsTO objDrawingPowerMaintenanceDetailsTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private ArrayList drawingPowerMaintenanceDetailsTab;
    private ArrayList drawingPowerMaintenanceTab;
    private TodAllowedTO todAllowedTO = null;
    private TodAllowedDAO todAllowedDAO = null;
    private Date currDt = null;

    /**
     * Creates a new instance of DrawingPowerMaintenanceDAO
     */
    public DrawingPowerMaintenanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        list = (List) sqlMap.executeQueryForList("getSelectDrawingPowerMaintenanceTO", map);
        returnMap.put("DrawingPowerMaintenanceTO", list);
        list = (List) sqlMap.executeQueryForList("DrawingPowerMaintenanceDetails", map);
        returnMap.put("DrawingPowerMaintenanceDetailsTO", list);
        map = null;
        list = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
//            objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
//            logTO.setData(objDrawingPowerMaintenanceTO.toString());
//            logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
//            logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
////            sqlMap.executeUpdate("insertDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
//            logDAO.addToLog(logTO);
            executeDrawingPowerMaintenanceTO();
            executeDrawingPowerMaintenanceDetailsTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
//            objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            logTO.setData(objDrawingPowerMaintenanceTO.toString());
//            logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
//            logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
//            sqlMap.executeUpdate("updateDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
//            logDAO.addToLog(logTO);
            executeDrawingPowerMaintenanceTO();
            executeDrawingPowerMaintenanceDetailsTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
//            objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_DELETED);
//            logTO.setData(objDrawingPowerMaintenanceTO.toString());
//            logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
//            logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
//            sqlMap.executeUpdate("deleteDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
//            logDAO.addToLog(logTO);
            executeDrawingPowerMaintenanceTO();
            executeDrawingPowerMaintenanceDetailsTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeDrawingPowerMaintenanceDetailsTO() throws Exception {
        try {
            DrawingPowerMaintenanceDetailsTO objDrawingPowerMaintenanceDetailsTO;
            // To retrieve the DrawingPowerMaintenanceDetailsTO from the drawingPowerMaintenanceDetailsTab (ArrayList)
            if (drawingPowerMaintenanceDetailsTab != null) {
                int drawingPowerMaintenanceDetailsTabSize = drawingPowerMaintenanceDetailsTab.size();
                for (int i = 0; i < drawingPowerMaintenanceDetailsTabSize; i++) {
                    objDrawingPowerMaintenanceDetailsTO = (DrawingPowerMaintenanceDetailsTO) drawingPowerMaintenanceDetailsTab.get(i);

                    if (objDrawingPowerMaintenanceDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        objDrawingPowerMaintenanceDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                        logTO.setData(objDrawingPowerMaintenanceDetailsTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceDetailsTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceDetailsTO.getCommand());
                        drawingPowerMaintenanceDetailsTOQuery("insertDrawingPowerMaintenanceDetailsTO", objDrawingPowerMaintenanceDetailsTO);
                        logDAO.addToLog(logTO);
                    } else if (objDrawingPowerMaintenanceDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        objDrawingPowerMaintenanceDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objDrawingPowerMaintenanceDetailsTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceDetailsTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceDetailsTO.getCommand());
                        drawingPowerMaintenanceDetailsTOQuery("updateDrawingPowerMaintenanceDetailsTO", objDrawingPowerMaintenanceDetailsTO);
                        logDAO.addToLog(logTO);
                    } else if (objDrawingPowerMaintenanceDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        objDrawingPowerMaintenanceDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                        logTO.setData(objDrawingPowerMaintenanceDetailsTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceDetailsTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceDetailsTO.getCommand());
                        drawingPowerMaintenanceDetailsTOQuery("deleteDrawingPowerMaintenanceDetailsTO", objDrawingPowerMaintenanceDetailsTO);
                        logDAO.addToLog(logTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeDrawingPowerMaintenanceTO() throws Exception {
        try {
            DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO;
            // To retrieve the DrawingPowerMaintenanceDetailsTO from the drawingPowerMaintenanceDetailsTab (ArrayList)
            if (drawingPowerMaintenanceTab != null) {
                int drawingPowerMaintenanceTabSize = drawingPowerMaintenanceTab.size();
                for (int i = 0; i < drawingPowerMaintenanceTabSize; i++) {
                    objDrawingPowerMaintenanceTO = (DrawingPowerMaintenanceTO) drawingPowerMaintenanceTab.get(i);
                    if (objDrawingPowerMaintenanceTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                        logTO.setData(objDrawingPowerMaintenanceTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
                        drawingPowerMaintenanceTOQuery("insertDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
                        logDAO.addToLog(logTO);
                    } else if (objDrawingPowerMaintenanceTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        logTO.setData(objDrawingPowerMaintenanceTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
                        drawingPowerMaintenanceTOQuery("updateDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
                        logDAO.addToLog(logTO);
                    } else if (objDrawingPowerMaintenanceTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        objDrawingPowerMaintenanceTO.setStatus(CommonConstants.STATUS_DELETED);
                        logTO.setData(objDrawingPowerMaintenanceTO.toString());
                        logTO.setPrimaryKey(objDrawingPowerMaintenanceTO.getKeyData());
                        logTO.setStatus(objDrawingPowerMaintenanceTO.getCommand());
                        drawingPowerMaintenanceTOQuery("deleteDrawingPowerMaintenanceTO", objDrawingPowerMaintenanceTO);
                        logDAO.addToLog(logTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawingPowerMaintenanceDetailsTOQuery(String map, DrawingPowerMaintenanceDetailsTO objDrawingPowerMaintenanceDetailsTO) throws Exception {
        try {
            sqlMap.executeUpdate(map, objDrawingPowerMaintenanceDetailsTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawingPowerMaintenanceTOQuery(String map, DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO) throws Exception {
        try {
            sqlMap.executeUpdate(map, objDrawingPowerMaintenanceTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public static void main(String str[]) { try { DrawingPowerMaintenanceDAO
     * dao = new DrawingPowerMaintenanceDAO(); DrawingPowerMaintenanceTO dpmTO =
     * new DrawingPowerMaintenanceTO(); DrawingPowerMaintenanceDetailsTO dpmdTO
     * = new DrawingPowerMaintenanceDetailsTO(); TOHeader toHeader = new
     * TOHeader(); toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * dpmTO.setTOHeader(toHeader);
     *
     * dpmTO.setBorrowNo("TT"); dpmTO.setProdId("TT"); dpmTO.setAcctNo("TT");
     * dpmTO.setStockStatFreq(new Double(5));
     * dpmTO.setPrevDpvalueCalcdt(DateUtil.getDateMMDDYYYY(null));
     * dpmTO.setPrevDpMonth("TT"); dpmTO.setPrevDpValue(new Double(5));
     * dpmTO.setCurrDpMonth("TT");
     * dpmTO.setDueDt(DateUtil.getDateMMDDYYYY(null));
     * dpmTO.setStockSubmitDt(DateUtil.getDateMMDDYYYY(null));
     * dpmTO.setGoodsParticulars("TT");
     * dpmTO.setInspectionDt(DateUtil.getDateMMDDYYYY(null));
     * dpmTO.setOpeningStockValue(new Double(5)); dpmTO.setPurchase(new
     * Double(5)); dpmTO.setClosingStockValue(new Double(5)); dpmTO.setSales(new
     * Double(5)); dpmTO.setAuthorizeRemarks("TT");
     * dpmTO.setAuthorizeStatus("TT"); dpmTO.setSecurityNo("TT");
     * dpmTO.setStatus("TT"); dpmTO.setStatusBy("TT");
     * dpmTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
     * dpmTO.setAuthorizeBy("TT");
     * dpmTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(null));
     *
     * toHeader = null;
     *
     * toHeader = new TOHeader();
     * toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
     * dpmdTO.setTOHeader(toHeader);
     *
     * dpmdTO.setBorrowNo("01332"); dpmdTO.setProdId("010212BC");
     * dpmdTO.setAcctNo("21755EC"); dpmdTO.setSecurityNo("532321HD");
     * dpmdTO.setSlNo("5"); dpmdTO.setPresentStockValue(new Double(5));
     * dpmdTO.setMargin(new Double(5)); dpmdTO.setLastStockValue(new Double(5));
     * dpmdTO.setCalcDrawingPower(new Double(5)); dpmdTO.setStatus("TT");
     * dpmdTO.setStatusBy("TT");
     * dpmdTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
     *
     * HashMap hash = new HashMap();
     * hash.put("DrawingPowerMaintenanceTO",dpmTO);
     * hash.put("DrawingPowerMaintenanceDetailsTO",dpmdTO); System.out.println("
     * the hash is --> "+hash.get("DrawingPowerMaintenanceDetailsTO"));
     * dao.execute(hash); } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDateProperFormat(_branchCode);
        drawingPowerMaintenanceDetailsTab = new ArrayList();
        drawingPowerMaintenanceTab = new ArrayList();
//        objDrawingPowerMaintenanceTO = (DrawingPowerMaintenanceTO) map.get("DrawingPowerMaintenanceTO");
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (map.get("DrawingPowerMaintenanceTO") != null) {
            drawingPowerMaintenanceTab = (ArrayList) map.get("DrawingPowerMaintenanceTO");
        }

        if (map.get("DrawingPowerMaintenanceDetailsTO") != null) {
            drawingPowerMaintenanceDetailsTab = (ArrayList) map.get("DrawingPowerMaintenanceDetailsTO");
        }
//        final String command = objDrawingPowerMaintenanceTO.getCommand();
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey("AUTHORIZE_MAP")) {
            authorize(map);
        } else {
            throw new NoCommandException();
        }
        map = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objDrawingPowerMaintenanceTO = null;
        objDrawingPowerMaintenanceDetailsTO = null;
        drawingPowerMaintenanceDetailsTab = null;
        logDAO = null;
        logTO = null;
    }

    private void authorize(HashMap map) throws Exception {
        HashMap drawingPowerMap = new HashMap();
        drawingPowerMap = (HashMap) map.get("AUTHORIZE_MAP");
        String actNo = CommonUtil.convertObjToStr(drawingPowerMap.get("ACCOUNT NUMBER"));
        HashMap tamMaintenanceCreateMap = new HashMap();
        HashMap balanceMap = new HashMap();
        String authorizeStatus = CommonUtil.convertObjToStr(drawingPowerMap.get("AUTHORIZE_STATUS"));
        tamMaintenanceCreateMap.put("STATUS", authorizeStatus);
        tamMaintenanceCreateMap.put("USER_ID", drawingPowerMap.get("USER_ID"));
        tamMaintenanceCreateMap.put("AUTHORIZEDT", currDt);
        tamMaintenanceCreateMap.put("ACCOUNT NUMBER", actNo);
        List lst = null;
        if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
            balanceMap.put("ACT_NUM", actNo);
            lst = sqlMap.executeQueryForList("getBalanceAD", balanceMap);
            if (lst != null && lst.size() > 0) {
                balanceMap = (HashMap) lst.get(0);
                double dpAmt = 0.0;
                double todUtilizedAmt = 0.0;
                double todAmount = 0.0;
                double todAllowedAmt = 0.0;
                double balance = 0.0;
                HashMap drawingPowerAmtMap = new HashMap();
                drawingPowerAmtMap.put("ACT_NUM", actNo);
                lst = sqlMap.executeQueryForList("getdrawingPowerAmtAD", drawingPowerAmtMap);
                if (lst != null && lst.size() > 0) {
                    drawingPowerAmtMap = (HashMap) lst.get(0);
                    dpAmt = CommonUtil.convertObjToDouble(drawingPowerAmtMap.get("DRAWING_POWER")).doubleValue();
                }
                HashMap todAllowedMap = new HashMap();
                todAllowedMap.put("ACT_NUM", actNo);
                todAllowedMap.put("TODAY_DATE", currDt);
                List lstTod = sqlMap.executeQueryForList("getSelectSumOfTODAmount", todAllowedMap);
                if (lstTod != null && lstTod.size() > 0) {
                    todAllowedMap = (HashMap) lstTod.get(0);
                    todUtilizedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_UTILIZED")).doubleValue();
                    todAllowedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_AMOUNT")).doubleValue();
                }
                double limit = CommonUtil.convertObjToDouble(balanceMap.get("LIMIT")).doubleValue();
                double availBal = CommonUtil.convertObjToDouble(balanceMap.get("AVAILABLE_BALANCE")).doubleValue();
                double totBal = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_BALANCE")).doubleValue();
                double clearBal = CommonUtil.convertObjToDouble(balanceMap.get("CLEAR_BALANCE")).doubleValue();
                double todAmt = 0.0;
                if (limit > dpAmt) {
                    todAmt = clearBal * -1 - dpAmt;
                    System.out.println("TOD Amount if :" + todAmt);
                    todAmt = todAmt - todUtilizedAmt;
                    System.out.println("TOD Amount 2nd time if :" + todAmt);
                } else {
                    todAmt = 0;
                    System.out.println("TOD Amount else :" + todAmt);
                }
//                boolean flag = false;
                if ((lstTod == null || lstTod.isEmpty()) || (!todAllowedMap.get("TYPE_OF_TOD").equals("")
                        && todAllowedMap.get("TYPE_OF_TOD").equals("SINGLE"))) {
                    HashMap singleAuthorizeMap = new HashMap();
                    if (todAmt > 0) {
//                        flag = true;
                        todAllowedTO = new TodAllowedTO();
                        todAllowedDAO = new TodAllowedDAO();
                        todAllowedTO.setTrans_id("");
                        todAllowedTO.setAccountNumber(CommonUtil.convertObjToStr(actNo));
                        todAllowedTO.setAcctName(CommonUtil.convertObjToStr(drawingPowerMap.get("CUST_NAME")));
                        todAllowedTO.setFromDate(currDt);
                        todAllowedTO.setToDate(currDt);
                        todAllowedTO.setPermittedDt(currDt);
                        todAllowedTO.setStatusBy(CommonUtil.convertObjToStr(drawingPowerMap.get("USER_ID")));
                        todAllowedTO.setRemarks("DP Reduced");
                        todAllowedTO.setProductType("AD");
                        todAllowedTO.setProductId(CommonUtil.convertObjToStr(drawingPowerMap.get("PRODUCT ID")));
                        todAllowedTO.setPermitedBy(CommonUtil.convertObjToStr(drawingPowerMap.get("USER_ID")));
                        todAllowedTO.setTodAllowed(String.valueOf(todAmt));
                        todAllowedTO.setPermissionRefNo("DP Reduced");
                        todAllowedTO.setBranchCode(CommonUtil.convertObjToStr(drawingPowerMap.get("BRANCH_CODE")));
                        todAllowedTO.setCommand("INSERT");
                        todAllowedTO.setStatus(CommonConstants.STATUS_CREATED);
                        todAllowedTO.setStatusBy(CommonUtil.convertObjToStr(drawingPowerMap.get("USER_ID")));
                        todAllowedTO.setStatusDt(currDt);
                        todAllowedTO.setTypeOfTOD("SINGLE");
                        todAllowedTO.setRepayPeriod(new Double(1));
                        todAllowedTO.setRepayPeriodDDMMYY("DAYS");
                        todAllowedTO.setRepayDt(currDt);
                        map.put("TodAllowed", todAllowedTO);
                        map.put("MODE", "INSERT");
                        map.put("BRANCH_CODE", drawingPowerMap.get("BRANCH_CODE"));
                        map.put("USER_ID", drawingPowerMap.get("USER_ID"));
                        map.put("DRAWING_POWER", "DRAWING_POWER");
                        map.put("AUTHORIZEMAP", null);
                        balanceMap = todAllowedDAO.execute(map, true, true);
                    }
                    if (balanceMap.containsKey("TRANS_ID")) {
                        tamMaintenanceCreateMap.put("SECURITY_NO", balanceMap.get("TRANS_ID"));
//                        singleAuthorizeMap.put("TRANS_ID", balanceMap.get("TRANS_ID"));
                    } else {
                        tamMaintenanceCreateMap.put("SECURITY_NO", "");
                    }
//                    singleAuthorizeMap.put("ACT_NUM", actNo);
//                    if(flag == true){
//                        singleAuthorizeMap.put("STATUS", authorizeStatus);
//                        singleAuthorizeMap.put("USER_ID", drawingPowerMap.get("USER_ID"));
//                        singleAuthorizeMap.put("AUTHORIZED_DT", currDt);
//                        singleAuthorizeMap.put("TOD_AMOUNT", String.valueOf(todAmt));
//                        singleAuthorizeMap.put("PROD_TYPE", "AD");
//                        sqlMap.executeUpdate("authorizeUpdateTodTO", singleAuthorizeMap); 
//                        HashMap updateMap = new HashMap();
//                        updateMap.put("ACT_NUM",actNo);
//                        updateMap.put("TOD_UTILIZED", new Double(todAmt+todUtilizedAmt));
//                        updateMap.put("TODAY_DT",currDt);
//                        sqlMap.executeUpdate("updateTODUtilizedWhileAuth",updateMap);
//                    }
//                    todAllowedDAO.execute(singleAuthorizeMap);
//                    updataAccountData(singleAuthorizeMap,todAmt+todUtilizedAmt,limit,dpAmt,clearBal,todUtilizedAmt,todAllowedAmt);
                    sqlMap.executeUpdate("authorizeDrawingPowerMaintenanceDetails", tamMaintenanceCreateMap);
                    sqlMap.executeUpdate("authorizeDrawingPowerMaintenance", tamMaintenanceCreateMap);
                    singleAuthorizeMap = null;
                } else if (!todAllowedMap.get("TYPE_OF_TOD").equals("") && todAllowedMap.get("TYPE_OF_TOD").equals("RUNNING")) {
                    throw new TTException("Already Running account is created...\n");
                }
            }
        } else if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
            sqlMap.executeUpdate("authorizeDrawingPowerMaintenanceDetails", tamMaintenanceCreateMap);
            sqlMap.executeUpdate("authorizeDrawingPowerMaintenance", tamMaintenanceCreateMap);
        }
        drawingPowerMap = null;
        tamMaintenanceCreateMap = null;
        balanceMap = null;
        map = null;
        lst = null;
        todAllowedTO = null;
        todAllowedDAO = null;
    }
//    private void updataAccountData(HashMap dataMap, double amount,double limit,double dpAmt,
//    double clearBal,double todUtilizedAmt,double todAllowedAmt) throws Exception {
//        HashMap todAllowedMap = new HashMap();
//        todAllowedMap.put("ACT_NUM",dataMap.get("ACT_NUM"));
//        todAllowedMap.put("TODAY_DATE",currDt);
//        List lst = sqlMap.executeQueryForList("getSelectSumOfTODAmount",todAllowedMap);
//        if(lst!=null && lst.size()>0){
//            todAllowedMap = (HashMap)lst.get(0);
//            todUtilizedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_UTILIZED")).doubleValue();
//            todAllowedAmt = CommonUtil.convertObjToDouble(todAllowedMap.get("TOD_AMOUNT")).doubleValue();
//        }                                                
//        double balance = todAllowedAmt - todUtilizedAmt;            
//        if(limit>dpAmt){
//            if((clearBal * -1)>dpAmt){
//                System.out.println("if condition part :");
//                dataMap.put("AMOUNT",String.valueOf(balance));
//                dataMap.put("LOAN_PAID_INT",String.valueOf(balance * -1));
//            }else{
//                System.out.println("else part :");
//                double balanceAmt = dpAmt + clearBal;
//                dataMap.put("AMOUNT",String.valueOf(balanceAmt+balance));
//                dataMap.put("LOAN_PAID_INT",String.valueOf(balanceAmt));
//            }
//            sqlMap.executeUpdate("Tod.updateAvailableBalanceAD", dataMap);
//        }else{          
//            double balanceAmt = limit + clearBal;
//            double balancePaid = limit + clearBal;
//            balanceAmt = (balanceAmt + todAllowedAmt) - todUtilizedAmt;
//            dataMap.put("AMOUNT",String.valueOf(balanceAmt));
//            dataMap.put("LOAN_PAID_INT", String.valueOf(balancePaid));
//            sqlMap.executeUpdate("Tod.updateAvailableBalanceAD", dataMap);
//        }
//    }
}
