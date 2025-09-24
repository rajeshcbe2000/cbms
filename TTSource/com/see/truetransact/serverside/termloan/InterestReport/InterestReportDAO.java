/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductDAO.java
 *
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.termloan.InterestReport;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.serverside.transaction.common;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.termloan.charges.TermLoanChargesDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * ShareProduct DAO.
 *
 */
public class InterestReportDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private Date curDate = null;

    /**
     * Creates a new instance of ShareProductDAO
     */
    public InterestReportDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            InterestReportDAO dao = new InterestReportDAO();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called to do desired operations in the Table
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        try {
            destroyObjects();
            return null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    /*
     * This method is used to execute a query to get all the inserted datas in
     * the table and retrun the resultset as a HashMap *
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj------------------->" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curDate = ServerUtil.getCurrentDate(_branchCode);
        where = (String) obj.get(CommonConstants.MAP_WHERE);
        TermLoanChargesDAO dao = new TermLoanChargesDAO();
        HashMap data = new HashMap();
        if (obj.containsKey("ACT_STATUS") && CommonUtil.convertObjToStr(obj.get("ACT_STATUS")).equals("BOTH_ACCT")) {
            List transList = (List) sqlMap.executeQueryForList("getSelectIntRepFromNdTo", obj);
            for (int i = 0; i < transList.size(); i++) {
                HashMap transListMap = new HashMap();
                transListMap = (HashMap) transList.get(i);
                String actNum = (String) transListMap.get("ACT_NUM");
                System.out.println("#$%#$%#$%actNum : "+actNum);
                obj.put("ACCOUNTNO", actNum);
                obj.put("ACCT_NUM", actNum);
                obj.put("ACT_NUM", actNum);
                //System.out.println("#$%#$%#$%obj"+obj);
                dao.executeQuery(obj);
                System.out.println("#@$@#$@#$passed TermLoancharges"+i);
            }
        }else if (obj.containsKey("ACT_STATUS") && CommonUtil.convertObjToStr(obj.get("ACT_STATUS")).equals("FROM_ACCT")) {
                //System.out.println("#$%#$%#$%obj"+obj);
                dao.executeQuery(obj);
        }else{
            List transList = (List) sqlMap.executeQueryForList("getSelectInterestReportTO", obj);
            for (int i = 0; i < transList.size(); i++) {
                HashMap transListMap = new HashMap();
                transListMap = (HashMap) transList.get(i);
                String actNum = (String) transListMap.get("ACT_NUM");
                System.out.println("#$%#$%#$%actNum : "+actNum);
                obj.put("ACCOUNTNO", actNum);
                obj.put("ACCT_NUM", actNum);
                obj.put("ACT_NUM", actNum);
                //System.out.println("#$%#$%#$%obj"+obj);
                dao.executeQuery(obj);
                System.out.println("#@$@#$@#$passed TermLoancharges"+i);
            }
        }
        return data;
    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        loanDataMap = null;
        deletedLoanDataMap = null;
    }
}

