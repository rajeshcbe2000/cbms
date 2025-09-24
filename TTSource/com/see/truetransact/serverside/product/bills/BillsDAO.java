/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsDAO.java
 *
 * Created on Mon Mar 15 16:08:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.product.bills;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.bills.BillsTO;
import com.see.truetransact.transferobject.product.bills.BillsChargesTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

/**
 * @author Ashok
 *
 */
public class BillsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BillsTO objTO;
    private final static Logger _log = Logger.getLogger(BillsDAO.class);
    private LogDAO logDAO;
    private LogTO logTO;
    private ArrayList billChargeTabTO;
    private ArrayList billChargesTabTO;
    private ArrayList billDelChargeTabTO;
    private BillsChargesTO billChargeTO;
    private BillsChargesTO billChargeTabSelectTO;
    private BillsChargesTO billDelChargesTO;

    /**
     * Creates a new instance of BillsDAO
     */
    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException se) {
            _log.error(se);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", where);
        List list = (List) sqlMap.executeQueryForList("getSelectBillsTO", whereMap);
        System.out.println("getBillsTO#####" + list);
        returnMap.put("BillsTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectBillsChargesTO", whereMap);
        System.out.println("getBillschargesTO#####" + list);
        returnMap.put("BillsChargesTO", list);
        list = null;
        where = null;
        return returnMap;
    }

    //    private String getProductId() throws Exception{
    //        final IDGenerateDAO dao = new IDGenerateDAO();
    //        final HashMap where = new HashMap();
    //        where.put(CommonConstants.MAP_WHERE, "BILLS.PROD_ID");
    //        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    //    }
    private void insertData(BillsTO objTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            //            String productId = getProductId();
            //            objTO.setProdId(productId);
            //            productId = null;
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertBillsTO", objTO);
            logDAO.addToLog(logTO);
            billChargeTO = new BillsChargesTO();
            for (int i = 0; i < billChargeTabTO.size(); i++) {

                System.out.println("billchargetabto######" + billChargeTabTO);
                billChargeTO = (BillsChargesTO) billChargeTabTO.get(i);
                billChargeTO.setProdID(objTO.getProdId());
                billChargeTO.setStatus(objTO.getStatus());
                System.out.println("billchargeto######" + billChargeTO);
                sqlMap.executeUpdate("insertBillsChargesTO", billChargeTO);

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }

    private void updateData(BillsTO objTO, HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateBillsTO", objTO);
            logDAO.addToLog(logTO);
            if (map.containsKey("BillsChargesTO")) {

                billChargesTabTO = new ArrayList();
                billChargesTabTO = (ArrayList) map.get("BillsChargesTO");
                System.out.println("billChargesTabTO&&&&&&&" + billChargesTabTO);
                int size = billChargesTabTO.size();
                System.out.println("size#&&&&&&&&" + size);
                for (int i = 0; i < size; i++) {
                    billChargeTabSelectTO = (BillsChargesTO) billChargesTabTO.get(i);
                    //billChargesTabTO.add(objTO.getProdId());
                    if ((billChargeTabSelectTO.getStatus().length() > 0) && (billChargeTabSelectTO.getStatus() != null)) {
                        billChargeTabSelectTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        billChargeTabSelectTO.setProdID(objTO.getProdId());
                        System.out.println("billChargesTabTO##******#&&&&&&&&" + billChargeTabSelectTO);
                        sqlMap.executeUpdate("updateBillsChargesTO", billChargeTabSelectTO);
                    } else {
                        billChargeTabSelectTO.setStatus(CommonConstants.STATUS_CREATED);
                        billChargeTabSelectTO.setProdID(objTO.getProdId());
                        sqlMap.executeUpdate("insertBillsChargesTO", billChargeTabSelectTO);
                    }
                }
            }
            if (map.containsKey("DeletedChargesTO")) {
//                billDelChargeTabTO=new ArrayList();
//                billDelChargeTabTO=(ArrayList)map.get("DeletedChargesTO");
                int size = billDelChargeTabTO.size();
                System.out.println("size#&&&&&&&&" + size);
                for (int i = 0; i < size; i++) {
                    billDelChargesTO = (BillsChargesTO) billDelChargeTabTO.get(i);
                    billDelChargesTO.setStatus(CommonConstants.STATUS_DELETED);
                    billDelChargesTO.setProdID(objTO.getProdId());
                    System.out.println("billDelChargesTO##******#&&&&&&&&" + billDelChargesTO);
                    sqlMap.executeUpdate("deleteBillsChargesTO", billDelChargesTO);

                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }

    private void deleteData(BillsTO objTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteBillsTO", objTO);
            if (billChargeTabSelectTO != null) {
                billChargeTabSelectTO.setStatus(objTO.getStatus());
                billChargeTabSelectTO.setProdID(objTO.getProdId());
                sqlMap.executeUpdate("deleteBillsChargesTO", billChargeTabSelectTO);
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }
    /*
     * private void updateCharges(HashMap map) throws Exception { try {
     * sqlMap.startTransaction(); // logTO.setData(billChargeTabTO.toString());
     * // logTO.setPrimaryKey(billChargeTabTO.getKeyData()); //
     * logTO.setStatus(billChargeTabTO.getCommand());
     * if(map.containsKey("BillsChargesTabTO")){
     *
     * billChargesTabTO=new ArrayList();
     * billChargesTabTO=(ArrayList)map.get("BillsChargesTabTO");
     * //billChargesTabTO.add(objTO.getProdId());
     * System.out.println("billChargesTabTO##******#&&&&&&&&"+billChargesTabTO);
     * sqlMap.executeUpdate("updateBillsChargesTO", billChargesTabTO); }
     * sqlMap.commitTransaction(); } catch (Exception e) {
     * sqlMap.rollbackTransaction(); _log.error(e); throw e; }
    }
     */

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("execute map##******#" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (BillsTO) map.get("BillsTO");
        System.out.println("execute map#####" + map);
        final String command = objTO.getCommand();
        System.out.println("execute methode####" + map);
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        objTO.setCreatedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objTO.setAuthorizeUser(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

        if (map.containsKey("BillsChargesTO")) {
            billChargeTabTO = new ArrayList();
            billChargeTabTO = (ArrayList) map.get("BillsChargesTO");
            System.out.println("billchargetabto######" + billChargeTabTO);
        }
        if (map.containsKey("DeletedChargesTO")) {
            billDelChargeTabTO = new ArrayList();
            billDelChargeTabTO = (ArrayList) map.get("DeletedChargesTO");
            System.out.println("billchargetabto######" + billDelChargeTabTO);
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objTO);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objTO, map);
            //updateCharges(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objTO);
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        System.out.println("billdao executequery#######" + obj);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}