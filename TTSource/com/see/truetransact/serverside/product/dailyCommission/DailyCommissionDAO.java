/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 15-06-2015
 */
package com.see.truetransact.serverside.product.dailyCommission;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.product.commission.CommissionMainTO;

import com.see.truetransact.transferobject.product.commission.CommissionTO;
import java.util.LinkedHashMap;

/**
 * TDSConfig DAO.
 *
 */
public class DailyCommissionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CommissionMainTO objTO;
    private LogTO logTO;
    private LogDAO logDAO;

    /**
     * Creates a new instance of TDSConfigDAO
     */
    public DailyCommissionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        if (map.containsKey("ACTION") && CommonUtil.convertObjToStr(map.get("ACTION")).equals("NEW")) {
            String type = CommonUtil.convertObjToStr(map.get("PROCESS_TYPE"));
            String commId = "";
            if (type != null && type.equals("COMMISSION")) {
                commId = getCommissionId();
            } else if (type != null && type.equals("ROI")) {
                commId = getROIId();
            }
            returnMap.put("COMM_ID", commId);
        } else if(map.containsKey("GET_DATA") ){
             String type = CommonUtil.convertObjToStr(map.get("PROCESS_TYPE"));
             System.out.println("map----------- :"+map);
            if (type != null && type.equals("COMMISSION")) {
               List list = (List) sqlMap.executeQueryForList("getSelectDailyCommissionTo", map);
               List detList = (List) sqlMap.executeQueryForList("getSelectDailyCommissionDetTo", map);
               returnMap.put("COMMISSION_MAIN",list);
               returnMap.put("COMMISSION_DET",detList);
            } else if (type != null && type.equals("ROI")) {
                List list = (List) sqlMap.executeQueryForList("getSelectDailyROITo", map);
               List detList = (List) sqlMap.executeQueryForList("getSelectDailyROIDetTo", map);
                returnMap.put("COMMISSION_MAIN",list);
               returnMap.put("COMMISSION_DET",detList);
            }
        }
        else {
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            List list = (List) sqlMap.executeQueryForList("getSelectTDSConfigTO", where);
            returnMap.put("TDSConfigTO", list);
        }
        return returnMap;

    }

    private String getCommissionId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "COMMISSION_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private String getROIId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DAILY_ROI_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private void insertCommissionData(LinkedHashMap detailsTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            
            sqlMap.executeUpdate("insertDailyCommissionMainTO", objTO);
            logDAO.addToLog(logTO);
            System.out.println("detailsTO$$$$$$$$$$$$$$$ :"+detailsTO);
            if(detailsTO!=null){
                for(int i=1;i<=detailsTO.size();i++){
                      System.out.println("i  >>:"+i);
                    System.out.println("detailsTO.get(i) ::"+detailsTO.get(i));
                    System.out.println("BBBBBBBbb :"+detailsTO.containsKey(i));
                    CommissionTO objComTo = (CommissionTO)detailsTO.get(i+"");
                  
                    System.out.println("objComTo :::::::;;"+objComTo);
                    sqlMap.executeUpdate("insertDailyCommissionDetTO", objComTo);
                }
            }
            System.out.println("completed.................................");  
            
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
 private void insertROIData(LinkedHashMap detailsTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            
            sqlMap.executeUpdate("insertDailyROIMainTO", objTO);
            logDAO.addToLog(logTO);
            System.out.println("detailsTO$$$$$$$Roiiiii$$$$$$$$ :"+detailsTO);
              if(detailsTO!=null){
                for(int i=1;i<=detailsTO.size();i++){
                      System.out.println("i  >>:"+i);
                    System.out.println("detailsTO.get(i)c ::"+detailsTO.get(i));
                    System.out.println("BBBBBBBbb c:"+detailsTO.containsKey(i));
                    CommissionTO objComTo = (CommissionTO)detailsTO.get(i+"");
                  
                    System.out.println("objComTo :::::::;;"+objComTo);
                    sqlMap.executeUpdate("insertDailyROIDetTO", objComTo);
                }
            }
            
            System.out.println("completed.............ROiiiii....................");  
            
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
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateTDSConfigTO", objTO);
            logDAO.addToLog(logTO);
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
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteTDSConfigTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            DailyCommissionDAO dao = new DailyCommissionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        String type="";
         LinkedHashMap detailsTO = new LinkedHashMap();
        if(map!=null && map.containsKey("TYPE_ID")){
            type = CommonUtil.convertObjToStr(map.get("TYPE_ID"));
        }
        System.out.println("map KKKKKKKKKKKKKK :"+map);
        System.out.println("type --------- :"+type);
        if(type.equals("COMMISSION")){
        objTO = (CommissionMainTO) map.get("COMMISSION_TO");
        detailsTO = (LinkedHashMap)map.get("COMMISSION_TO_DET");
        }else if(type.equals("ROI")){
             objTO = (CommissionMainTO) map.get("ROI_TO");
              detailsTO = (LinkedHashMap)map.get("ROI_TO_DET");
        }
        final String command = objTO.getCommand();
        System.out.println("command-------- :"+command);
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
              if(type.equals("COMMISSION")){
                  insertCommissionData(detailsTO);
              }
              else if(type.equals("ROI")){
                  insertROIData(detailsTO);
              }
            
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
