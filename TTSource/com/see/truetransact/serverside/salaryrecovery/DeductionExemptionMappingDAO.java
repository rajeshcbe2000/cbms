/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.salaryrecovery;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;


import com.see.truetransact.servicelocator.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.salaryrecovery.SalaryDeductionMappingTO;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;

import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.introducer.IntroducerDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import java.util.List;

/**
 * This is used for AccountHead Data Access.
 *
 * @author Balachandar
 */
public class DeductionExemptionMappingDAO extends TTDAO {

    private SqlMap sqlMap;
    private HashMap data;
    private LogDAO logDAO;
    private LogTO logTO;
    //Used in Executequery
    private List list;
    SalaryDeductionMappingTO objSalaryDeductionMappingTO;

    /**
     * Creates a new instance of CustomerDAO for manipulating Customer data
     */
    public DeductionExemptionMappingDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * To Update customer data
     */
    /**
     * Standard method for insert, update & delete operations
     */
    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        System.out.println("#### DAO execute SalaryDeductionMapping map : " + obj);
        HashMap resultMap = new HashMap();
        try {
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(obj.get(CommonConstants.SELECTED_BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(obj.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(obj.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(obj.get(CommonConstants.SCREEN)));
            objSalaryDeductionMappingTO = (SalaryDeductionMappingTO) obj.get("SalaryDeductionMappingTO");
            final String command = objSalaryDeductionMappingTO.getCommand();

            //Start the transaction
            sqlMap.startTransaction();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                objSalaryDeductionMappingTO.setStatus(CommonConstants.STATUS_CREATED);
                insertData(objSalaryDeductionMappingTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                objSalaryDeductionMappingTO.setStatus(CommonConstants.STATUS_MODIFIED);
                updateData(objSalaryDeductionMappingTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                objSalaryDeductionMappingTO.setStatus(CommonConstants.STATUS_DELETED);
                deleteData(objSalaryDeductionMappingTO);
            } else {

                throw new NoCommandException();
            }
            //Commit the transaction
            sqlMap.commitTransaction();
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
        System.out.println("resultMap" + resultMap);
        return resultMap;
    }

    private void insertData(SalaryDeductionMappingTO objSalaryDeductionMappingTO) throws Exception {
        try {
            logTO.setData(objSalaryDeductionMappingTO.toString());
            logTO.setPrimaryKey(objSalaryDeductionMappingTO.getKeyData());
            logTO.setStatus(objSalaryDeductionMappingTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("insertDeductionExemptionMapping", objSalaryDeductionMappingTO);
            HashMap empRefMap = new HashMap();
            empRefMap.put("EMP_REFNO", CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getEmployerRefNo())); 
            List accountsList = sqlMap.executeQueryForList("getCustomerAccounts", empRefMap);
            if (accountsList != null && accountsList.size() > 0) {
                for (int i = 0; i < accountsList.size(); i++) {
                    HashMap newAccountsMap = (HashMap) accountsList.get(i);
                    if (newAccountsMap != null && newAccountsMap.containsKey("PROD_TYPE") && newAccountsMap.containsKey("ACT_NUM")) {
                        String prodType = CommonUtil.convertObjToStr(newAccountsMap.get("PROD_TYPE"));
                        String actNum = CommonUtil.convertObjToStr(newAccountsMap.get("ACT_NUM"));
                        if (prodType!=null && (prodType.equals("TL") || prodType.equals("AD"))) {
                            HashMap TLMap = new HashMap();
                            TLMap.put("ACT_NUM", actNum);
                            sqlMap.executeUpdate("updateSalRecLoans", TLMap);
                        } else if (prodType!=null && prodType.equals("MDS")) {
                            HashMap MDSMap = new HashMap();
                            MDSMap.put("CHITTAL_NO", actNum);
                            sqlMap.executeUpdate("updateSalRecMDS", MDSMap);
                        } else if (prodType!=null && prodType.equals("TD")) {
                            HashMap TDMap = new HashMap();
                            TDMap.put("DEPOSIT_NO", actNum);
                            sqlMap.executeUpdate("updateSalRecTD", TDMap);
                        } else {
                            HashMap SAMap = new HashMap();
                            SAMap.put("SUSPENSE_ACCT_NUM", actNum);
                            sqlMap.executeUpdate("updateSalRecSA", SAMap);
                        }
                    }
                }              
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void updateData(SalaryDeductionMappingTO objSalaryDeductionMappingTO) throws Exception {
        try {

            objSalaryDeductionMappingTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objSalaryDeductionMappingTO.toString());
            logTO.setPrimaryKey(objSalaryDeductionMappingTO.getKeyData());
            logTO.setStatus(objSalaryDeductionMappingTO.getCommand());

            logDAO.addToLog(logTO);
            // added by Anju Anand for Mantis Id: 10376
            HashMap dataMap = new HashMap();
            dataMap.put("EMP_REF_NO", objSalaryDeductionMappingTO.getEmployerRefNo());
            dataMap.put("BRANCH_CODE", objSalaryDeductionMappingTO.getBranchId());
            List empList = sqlMap.executeQueryForList("getExemptionDetails", dataMap);
            if (empList != null && empList.size() > 0) {
                HashMap resultMap = new HashMap();
                resultMap = (HashMap) empList.get(0);
                String exemptionMode_Temp = "";
                String actNo_Temp = "";
                String prodId_Temp = "";
                String prodType_Temp = "";
                exemptionMode_Temp = CommonUtil.convertObjToStr(resultMap.get("EXEMPTION_MODE"));
                actNo_Temp = CommonUtil.convertObjToStr(resultMap.get("MAP_ACT_NUM"));
                prodId_Temp = CommonUtil.convertObjToStr(resultMap.get("MAP_PROD_ID"));
                prodType_Temp = CommonUtil.convertObjToStr(resultMap.get("MAP_PROD_TYPE"));
                objSalaryDeductionMappingTO.setExemptionMode_Temp(exemptionMode_Temp);
                objSalaryDeductionMappingTO.setActNum_Temp(actNo_Temp);
                objSalaryDeductionMappingTO.setProdId_Temp(prodId_Temp);
                objSalaryDeductionMappingTO.setProdType_Temp(prodType_Temp);
               sqlMap.executeUpdate("updateExemptionDetails_Temp", objSalaryDeductionMappingTO);
            }
            
            sqlMap.executeUpdate("updateDeductionExemptionMapping", objSalaryDeductionMappingTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();

            throw e;
        }
    }

    private void deleteData(SalaryDeductionMappingTO objSalaryDeductionMappingTO) throws Exception {
        try {

            objSalaryDeductionMappingTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objSalaryDeductionMappingTO.toString());
            logTO.setPrimaryKey(objSalaryDeductionMappingTO.getKeyData());
            logTO.setStatus(objSalaryDeductionMappingTO.getCommand());


            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deleteDeductionExemptionMapping", objSalaryDeductionMappingTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();

            throw e;
        }
    }

    /**
     * Standard method to get data for a particular customer based on customer
     * id
     */
    public HashMap executeQuery(HashMap condition) throws Exception {
        System.out.println("###condition" + condition);
        _branchCode = (String) condition.get(CommonConstants.BRANCH_ID);

        if (condition.containsKey(CommonConstants.MAP_WHERE)) //        System.out.println("###whereconditions"+whereConditions);
        {
            if (!condition.containsKey("PHOTOSIGNONLY")) {
            } else {
                data = new HashMap();
                List lst = (List) sqlMap.executeQueryForList("getCustomerCustType", condition);
                String custType = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("CUST_TYPE"));
                if (custType.equals("INDIVIDUAL")) {
                } else {
                }
                lst = null;

            }
        }
        return data;
    }

    /**
     * To make used object in executeQuery method as null
     */
    private void makeQueryNull() {

        list = null;
    }
}
