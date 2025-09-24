/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanChargesDAO.java
 * 
 * Created on Mon Aug 29 16:56:27 IST 2011
 */
package com.see.truetransact.serverside.common.charges;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.charges.LoanChargesTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;

/**
 * This is used for LoanChargesDAO Data Access.
 *
 * @author
 *
 * @modified
 * @modified
 */
public class LoanChargesDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LoanChargesTO chargesTo = new LoanChargesTO();
    private LoanSlabChargesTO objLoanSlabChargesTO = new LoanSlabChargesTO();
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(LoanChargesDAO.class);
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private Date CurrDt = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public LoanChargesDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectLoanChargesTO", map);
        returnMap.put("ChargeDetailsTO", list);

        List slabAmountList = (List) sqlMap.executeQueryForList("getSelectLoanSlabChargesTO", map);
        if (slabAmountList != null && slabAmountList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@slabAmountList" + slabAmountList);
            for (int i = slabAmountList.size(), j = 0; i > 0; i--, j++) {
                int st = ((LoanSlabChargesTO) slabAmountList.get(j)).getSlNo();
                ParMap.put(((LoanSlabChargesTO) slabAmountList.get(j)).getSlNo(), slabAmountList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("SlabAmountListTO", ParMap);
        }

        return returnMap;
    }

    private String getCharge_Id() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CHARGE_ID");
        String chargeId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return chargeId;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in DAO: " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        chargesTo = (LoanChargesTO) map.get("LoanChargesDetails");
        if (map.containsKey("SlabAmountTableDetails")) {
            tableDetails = (LinkedHashMap) map.get("SlabAmountTableDetails");
        }
        if (map.containsKey("deletedSlabAmountTableDetails")) {
            deletedTableValues = (LinkedHashMap) map.get("deletedSlabAmountTableDetails");
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("@@@@@@@command" + command);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String charge_Id = "";
            charge_Id = getCharge_Id();
            chargesTo.setChargeId(charge_Id);
            if(CommonUtil.convertObjToStr(chargesTo.getPrematureRate()).equals("")){
                chargesTo.setPrematureRate(CommonUtil.convertObjToDouble('0'));
                
            }
            sqlMap.executeUpdate("insertLoanChargesTO", chargesTo);
            if (tableDetails != null) {
                insertSlabAmountTableDetails(charge_Id);
            }
            logTO.setData(chargesTo.toString());
            logTO.setPrimaryKey(chargesTo.getKeyData());
            logTO.setStatus(chargesTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertSlabAmountTableDetails(String charge_Id) throws Exception {
        ArrayList addList = new ArrayList(tableDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) tableDetails.get(addList.get(i));
            objLoanSlabChargesTO.setChargeId(charge_Id);
            sqlMap.executeUpdate("insertLoanSlabChargesTO", objLoanSlabChargesTO);
            logTO.setData(objLoanSlabChargesTO.toString());
            logTO.setPrimaryKey(objLoanSlabChargesTO.getKeyData());
            logDAO.addToLog(logTO);
            objLoanSlabChargesTO = null;
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateLoanChargesTO", chargesTo);
            if (tableDetails != null || deletedTableValues != null) {
                updateSlabAmountTableDetails(chargesTo);
            }
            logTO.setData(chargesTo.toString());
            logTO.setPrimaryKey(chargesTo.getKeyData());
            logTO.setStatus(chargesTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateSlabAmountTableDetails(LoanChargesTO chargesTo) throws Exception {

        if (tableDetails != null) {
            System.out.println("######## tableDetails :" + tableDetails);
            ArrayList addList = new ArrayList(tableDetails.keySet());
            LoanSlabChargesTO objLoanSlabChargesTO = null;
            for (int i = 0; i < tableDetails.size(); i++) {
                objLoanSlabChargesTO = new LoanSlabChargesTO();
                objLoanSlabChargesTO = (LoanSlabChargesTO) tableDetails.get(addList.get(i));
                objLoanSlabChargesTO.setStatusDt(CurrDt);
                System.out.println("#######objLoanSlabChargesTO" + objLoanSlabChargesTO);
                if (objLoanSlabChargesTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    objLoanSlabChargesTO.setChargeId(chargesTo.getChargeId());
                    sqlMap.executeUpdate("insertLoanSlabChargesTO", objLoanSlabChargesTO);
                } else {
                    sqlMap.executeUpdate("updateLoanSlabChargesTO", objLoanSlabChargesTO);
                }
            }
        }
        if (deletedTableValues != null) {
            System.out.println("######## deletedTableValues :" + deletedTableValues);
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            LoanSlabChargesTO objLoanSlabChargesTO = null;
            for (int i = 0; i < deletedTableValues.size(); i++) {
                objLoanSlabChargesTO = new LoanSlabChargesTO();
                objLoanSlabChargesTO = (LoanSlabChargesTO) deletedTableValues.get(addList.get(i));
                System.out.println("########objLoanSlabChargesTO" + objLoanSlabChargesTO);
                sqlMap.executeUpdate("deleteLoanSlabChargesTO", objLoanSlabChargesTO);
            }
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteLoanChargesTO", chargesTo);
            sqlMap.executeUpdate("deleteLoanSlabChargeDetails", chargesTo);
            logTO.setData(chargesTo.toString());
            logTO.setPrimaryKey(chargesTo.getKeyData());
            logTO.setStatus(chargesTo.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeLoanChargesDetails", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LoanChargesDAO dao = new LoanChargesDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        chargesTo = null;
        objLoanSlabChargesTO = null;
        deletedTableValues = null;
        tableDetails = null;
    }
}
