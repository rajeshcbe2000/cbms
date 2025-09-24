/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierDAO.java
 *
 * Created on Wed Jan 05 14:59:17 IST 2005
 */
package com.see.truetransact.serverside.batchprocess.yearendprocess;

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

import com.see.truetransact.transferobject.bills.CarrierTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * Carrier DAO.
 *
 */
public class YearEndProcessDAO extends TTDAO {

    private static SqlMap sqlMap = null;
//    private CarrierTO objCarrierTO;
    private String command;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;
    /**
     * Creates a new instance of CarrierDAO
     */
    public YearEndProcessDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put("ACCT_TYPE", "INCOME");
        List list = sqlMap.executeQueryForList("YearEndProcess.getSelectAcctHeadTableList", whereMap);
        returnMap.put("INCOME_LIST", list);
        whereMap.put("ACCT_TYPE", "EXPENDITURE");
        list = sqlMap.executeQueryForList("YearEndProcess.getSelectAcctHeadTableList", whereMap);
        returnMap.put("EXPENDITURE_LIST", list);
        list = sqlMap.executeQueryForList("YearEndProcess.getProfitOrLoss", whereMap);
        returnMap.put("PROFIT_OR_LOSS", list.get(0));
//        System.out.println("#$#$# returnMap from YearEndProcessDAO:"+returnMap);
        list = null;
        return returnMap;
    }

    public static void main(String str[]) {
        try {
            YearEndProcessDAO dao = new YearEndProcessDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
//        logDAO = new LogDAO();
//        setInitialValuesForLogTO(map);
        try {
            sqlMap.startTransaction();
            map.put("PROCESS_DT", currDt.clone());
            map.put("STATUS_BY", map.get(CommonConstants.USER_ID));
            sqlMap.executeUpdate("insertYearEndProcessMaster", map);

            if (map.get("INCOME_LIST") != null && ((List) map.get("INCOME_LIST")).size() > 0) {
                List incomeList = (List) map.get("INCOME_LIST");
                List yearEndProcessAcHdList = null;
                HashMap yearEndProcessAcHdMap = null;
                for (int i = 0; i < incomeList.size(); i++) {
                    yearEndProcessAcHdList = (List) incomeList.get(i);
                    yearEndProcessAcHdMap = new HashMap();
                    yearEndProcessAcHdMap.put("PROCESS_DT", map.get("PROCESS_DT"));
                    yearEndProcessAcHdMap.put("AC_HD_ID", yearEndProcessAcHdList.get(0));
                    yearEndProcessAcHdMap.put("MJR_AC_HD_TYPE", "INCOME");
                    String amt = CommonUtil.convertObjToStr(yearEndProcessAcHdList.get(2)).replaceAll(",", "");
                    yearEndProcessAcHdMap.put("AMOUNT", new Double(amt));
                    yearEndProcessAcHdMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    sqlMap.executeUpdate("insertYearEndProcessAcHeads", yearEndProcessAcHdMap);
                    yearEndProcessAcHdMap.put("AMOUNT", new Double(0));
                    sqlMap.executeUpdate("updateYearEndGLBalance", yearEndProcessAcHdMap);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", yearEndProcessAcHdMap);
                }
            }
            if (map.get("EXPENDITURE_LIST") != null && ((List) map.get("EXPENDITURE_LIST")).size() > 0) {
                List expenditureList = (List) map.get("EXPENDITURE_LIST");
                List yearEndProcessAcHdList = null;
                HashMap yearEndProcessAcHdMap = null;
                for (int i = 0; i < expenditureList.size(); i++) {
                    yearEndProcessAcHdList = (List) expenditureList.get(i);
                    yearEndProcessAcHdMap = new HashMap();
                    yearEndProcessAcHdMap.put("PROCESS_DT", map.get("PROCESS_DT"));
                    yearEndProcessAcHdMap.put("AC_HD_ID", yearEndProcessAcHdList.get(0));
                    yearEndProcessAcHdMap.put("MJR_AC_HD_TYPE", "EXPENDITURE");
                    String amt = CommonUtil.convertObjToStr(yearEndProcessAcHdList.get(2)).replaceAll(",", "");
                    yearEndProcessAcHdMap.put("AMOUNT", new Double(amt));
                    yearEndProcessAcHdMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    sqlMap.executeUpdate("insertYearEndProcessAcHeads", yearEndProcessAcHdMap);
                    yearEndProcessAcHdMap.put("AMOUNT", new Double(0));
                    sqlMap.executeUpdate("updateYearEndGLBalance", yearEndProcessAcHdMap);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", yearEndProcessAcHdMap);
                }
            }

            if (CommonUtil.convertObjToStr(map.get("PROFIT_AC_HD_ID")).equals(CommonUtil.convertObjToStr(map.get("LOSS_AC_HD_ID")))) {
                map.put("AC_HD_ID", map.get("PROFIT_AC_HEAD"));
                map.put("AMOUNT", map.get("PROFIT_OR_LOSS_VALUE"));
                sqlMap.executeUpdate("updateYearEndGLBalance", map);
                sqlMap.executeUpdate("updateYearEndGLAbstactBalance", map);
            } else {
                if (CommonUtil.convertObjToStr(map.get("PROFIT_AC_HD_ID")).equals("PROFIT")) {
                    map.put("AC_HD_ID", map.get("PROFIT_AC_HEAD"));
                    map.put("AMOUNT", map.get("PROFIT_OR_LOSS_VALUE"));
                    sqlMap.executeUpdate("updateYearEndGLBalance", map);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", map);
                    map.put("AC_HD_ID", map.get("LOSS_AC_HEAD"));
                    map.put("AMOUNT", new Double(0));
                    sqlMap.executeUpdate("updateYearEndGLBalance", map);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", map);
                } else {
                    map.put("AC_HD_ID", map.get("LOSS_AC_HEAD"));
                    map.put("AMOUNT", map.get("PROFIT_OR_LOSS_VALUE"));
                    sqlMap.executeUpdate("updateYearEndGLBalance", map);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", map);
                    map.put("AC_HD_ID", map.get("PROFIT_AC_HEAD"));
                    map.put("AMOUNT", new Double(0));
                    sqlMap.executeUpdate("updateYearEndGLBalance", map);
                    sqlMap.executeUpdate("updateYearEndGLAbstactBalance", map);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
//        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
//        objCarrierTO = null;
        logTO = null;
        logDAO = null;
        command = null;
    }

    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }
}
