/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerDAO.java
 *
 * Created on Fri Dec 31 11:53:03 IST 2004
 */
package com.see.truetransact.serverside.common.introducer;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.common.introducer.IntroDocDetailsTO;
import com.see.truetransact.transferobject.common.introducer.IntroIdentityTO;
import com.see.truetransact.transferobject.common.introducer.IntroOtherBankTO;
import com.see.truetransact.transferobject.common.introducer.IntroOthersTO;
import com.see.truetransact.transferobject.common.introducer.IntroSelfTO;
//__ To Log the Data...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import java.util.Date;
/**
 * Introducer DAO.
 *
 */
public class IntroducerDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private IntroDocDetailsTO objIntroDocDetailsTO;
    private IntroIdentityTO objIntroIdentityTO;
    private IntroOtherBankTO objIntroOtherBankTO;
    private IntroOthersTO objIntroOthersTO;
    private IntroSelfTO objIntroSelfTO;
    private Date currDt = null;
    /**
     * Creates a new instance of IntroducerDAO
     */
    public IntroducerDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //String where = (String) map.get(CommonConstants.MAP_WHERE);
        //List list = (List) sqlMap.executeQueryForList("select query map", where);
        //returnMap.put("return key ", list);
        return returnMap;
    }

    public void insertData(String introType, String acctNo, Object obj, String statusBy, String screen, LogTO objLogTO, LogDAO objLogDAO) throws Exception {
        try {
            _branchCode = objLogTO.getBranchId();
            if (introType.equals("SELF_CUSTOMER")) {
                objIntroSelfTO = (IntroSelfTO) obj;
                objIntroSelfTO.setActNum(acctNo);
                objIntroSelfTO.setStatus(CommonConstants.STATUS_CREATED);
                objIntroSelfTO.setStatusBy(statusBy);
                objIntroSelfTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertIntroSelf" + screen, objIntroSelfTO);

                // Inserting into Customer History Table
                CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
                objCustomerHistoryTO.setAcctNo(acctNo);
                objCustomerHistoryTO.setCustId(objIntroSelfTO.getActNumIntro());
                objCustomerHistoryTO.setProductType("");
                objCustomerHistoryTO.setProdId("");
                objCustomerHistoryTO.setRelationship(AcctStatusConstants.INTRODUCER);
                objCustomerHistoryTO.setFromDt(currDt);
                CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
                objCustomerHistoryTO = null;

                objLogTO.setData(objIntroSelfTO.toString());
                objLogTO.setPrimaryKey(objIntroSelfTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("IDENTITY")) {
                objIntroIdentityTO = (IntroIdentityTO) obj;
                objIntroIdentityTO.setActNum(acctNo);
                objIntroIdentityTO.setStatus(CommonConstants.STATUS_CREATED);
                objIntroIdentityTO.setStatusBy(statusBy);
                objIntroIdentityTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertIntroIdentity" + screen, objIntroIdentityTO);

                objLogTO.setData(objIntroIdentityTO.toString());
                objLogTO.setPrimaryKey(objIntroIdentityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("OTHERS")) {
                objIntroOthersTO = (IntroOthersTO) obj;
                objIntroOthersTO.setActNum(acctNo);
                objIntroOthersTO.setStatus(CommonConstants.STATUS_CREATED);
                objIntroOthersTO.setStatusBy(statusBy);
                objIntroOthersTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertIntroOthers" + screen, objIntroOthersTO);

                objLogTO.setData(objIntroOthersTO.toString());
                objLogTO.setPrimaryKey(objIntroOthersTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("DOC_DETAILS")) {
                objIntroDocDetailsTO = (IntroDocDetailsTO) obj;
                objIntroDocDetailsTO.setActNum(acctNo);
                objIntroDocDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                objIntroDocDetailsTO.setStatusBy(statusBy);
                objIntroDocDetailsTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertIntroDocDetails" + screen, objIntroDocDetailsTO);

                objLogTO.setData(objIntroDocDetailsTO.toString());
                objLogTO.setPrimaryKey(objIntroDocDetailsTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("OTHER_BANK")) {
                objIntroOtherBankTO = (IntroOtherBankTO) obj;
                objIntroOtherBankTO.setActNum(acctNo);
                objIntroOtherBankTO.setStatus(CommonConstants.STATUS_CREATED);
                objIntroOtherBankTO.setStatusBy(statusBy);
                objIntroOtherBankTO.setStatusDt(currDt);
                sqlMap.executeUpdate("insertIntroOtherBank" + screen, objIntroOtherBankTO);

                objLogTO.setData(objIntroOtherBankTO.toString());
                objLogTO.setPrimaryKey(objIntroOtherBankTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            //			sqlMap.startTransaction();
            //			sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void updateData(String introType, String acctNo, Object obj, String statusBy, String screen, LogTO objLogTO, LogDAO objLogDAO) throws Exception {
        try {
            _branchCode = objLogTO.getBranchId();
            if (introType.equals("SELF_CUSTOMER")) {
                objIntroSelfTO = (IntroSelfTO) obj;
                objIntroSelfTO.setActNum(acctNo);
                objIntroSelfTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objIntroSelfTO.setStatusBy(statusBy);
                objIntroSelfTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateIntroSelf" + screen, objIntroSelfTO);

                objLogTO.setData(objIntroSelfTO.toString());
                objLogTO.setPrimaryKey(objIntroSelfTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("IDENTITY")) {
                objIntroIdentityTO = (IntroIdentityTO) obj;
                objIntroIdentityTO.setActNum(acctNo);
                objIntroIdentityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objIntroIdentityTO.setStatusBy(statusBy);
                objIntroIdentityTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateIntroIdentity" + screen, objIntroIdentityTO);

                objLogTO.setData(objIntroIdentityTO.toString());
                objLogTO.setPrimaryKey(objIntroIdentityTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("OTHERS")) {
                objIntroOthersTO = (IntroOthersTO) obj;
                objIntroOthersTO.setActNum(acctNo);
                objIntroOthersTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objIntroOthersTO.setStatusBy(statusBy);
                objIntroOthersTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateIntroOthers" + screen, objIntroOthersTO);

                objLogTO.setData(objIntroOthersTO.toString());
                objLogTO.setPrimaryKey(objIntroOthersTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("DOC_DETAILS")) {
                objIntroDocDetailsTO = (IntroDocDetailsTO) obj;
                objIntroDocDetailsTO.setActNum(acctNo);
                objIntroDocDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objIntroDocDetailsTO.setStatusBy(statusBy);
                objIntroDocDetailsTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateIntroDocDetails" + screen, objIntroDocDetailsTO);

                objLogTO.setData(objIntroDocDetailsTO.toString());
                objLogTO.setPrimaryKey(objIntroDocDetailsTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else if (introType.equals("OTHER_BANK")) {
                objIntroOtherBankTO = (IntroOtherBankTO) obj;
                objIntroOtherBankTO.setActNum(acctNo);
                objIntroOtherBankTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objIntroOtherBankTO.setStatusBy(statusBy);
                objIntroOtherBankTO.setStatusDt(currDt);
                sqlMap.executeUpdate("updateIntroOtherBank" + screen, objIntroOtherBankTO);

                objLogTO.setData(objIntroOtherBankTO.toString());
                objLogTO.setPrimaryKey(objIntroOtherBankTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            //            sqlMap.startTransaction();
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void deleteData(String introType, String acctNo, String statusBy, String screen) throws Exception {
        try {
            if (introType.equals("SELF_CUSTOMER")) {
                objIntroSelfTO = new IntroSelfTO();
                objIntroSelfTO.setActNum(acctNo);
                objIntroSelfTO.setStatus(CommonConstants.STATUS_DELETED);
                objIntroSelfTO.setStatusBy(statusBy);
                objIntroSelfTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteIntroSelf" + screen, objIntroSelfTO);
                objIntroSelfTO = null;
            } else if (introType.equals("IDENTITY")) {
                objIntroIdentityTO = new IntroIdentityTO();
                objIntroIdentityTO.setActNum(acctNo);
                objIntroIdentityTO.setStatus(CommonConstants.STATUS_DELETED);
                objIntroIdentityTO.setStatusBy(statusBy);
                objIntroIdentityTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteIntroIdentity" + screen, objIntroIdentityTO);
                objIntroIdentityTO = null;
            } else if (introType.equals("OTHERS")) {
                objIntroOthersTO = new IntroOthersTO();
                objIntroOthersTO.setActNum(acctNo);
                objIntroOthersTO.setStatus(CommonConstants.STATUS_DELETED);
                objIntroOthersTO.setStatusBy(statusBy);
                objIntroOthersTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteIntroOthers" + screen, objIntroOthersTO);
                objIntroOthersTO = null;
            } else if (introType.equals("DOC_DETAILS")) {
                objIntroDocDetailsTO = new IntroDocDetailsTO();
                objIntroDocDetailsTO.setActNum(acctNo);
                objIntroDocDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                objIntroDocDetailsTO.setStatusBy(statusBy);
                objIntroDocDetailsTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteIntroDocDetails" + screen, objIntroDocDetailsTO);
                objIntroDocDetailsTO = null;
            } else if (introType.equals("OTHER_BANK")) {
                objIntroOtherBankTO = new IntroOtherBankTO();
                objIntroOtherBankTO.setActNum(acctNo);
                objIntroOtherBankTO.setStatus(CommonConstants.STATUS_DELETED);
                objIntroOtherBankTO.setStatusBy(statusBy);
                objIntroOtherBankTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteIntroOtherBank" + screen, objIntroOtherBankTO);
                objIntroOtherBankTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            IntroducerDAO dao = new IntroducerDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List getIntroData(String introType, String acctNo, String screen) throws Exception {
        List list = new ArrayList();
        if (introType.equals("SELF_CUSTOMER")) {
            list = (List) sqlMap.executeQueryForList("getSelectIntroSelf" + screen, acctNo);

        } else if (introType.equals("IDENTITY")) {
            list = (List) sqlMap.executeQueryForList("getSelectIntroIdentity" + screen, acctNo);

        } else if (introType.equals("OTHERS")) {
            list = (List) sqlMap.executeQueryForList("getSelectIntroOthers" + screen, acctNo);

        } else if (introType.equals("DOC_DETAILS")) {
            list = (List) sqlMap.executeQueryForList("getSelectIntroDocDetails" + screen, acctNo);

        } else if (introType.equals("OTHER_BANK")) {
            list = (List) sqlMap.executeQueryForList("getSelectIntroOtherBank" + screen, acctNo);
        }

        return list;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objIntroSelfTO = (IntroSelfTO) map.get("IntroSelfTO");
        final String command = objIntroSelfTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            //            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            //            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            //            deleteData();
        } else {
            throw new NoCommandException();
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
        objIntroSelfTO = null;
        objIntroDocDetailsTO = null;
        objIntroIdentityTO = null;
        objIntroOtherBankTO = null;
        objIntroOthersTO = null;

    }
}
