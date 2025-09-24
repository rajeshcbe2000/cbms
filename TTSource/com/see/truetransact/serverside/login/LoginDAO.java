/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoginDAO.java
 *
 * Created on March 2, 2005, 10:08 AM
 */
package com.see.truetransact.serverside.login;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.Random;

/**
 *
 * @author JK
 */
public class LoginDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private java.util.Properties dataBaseProperties = new java.util.Properties();
    private String dbDriverName = "";

    /**
     * Creates a new instance of LoginDAO
     */
    public LoginDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        try {
            dataBaseProperties.load(this.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dbDriverName = dataBaseProperties.getProperty("driver");
        } catch (Exception ex) {
        }
        System.out.println("#$#$#$ sqlMap.getCurrentDataSourceName() : " + dbDriverName);
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        _branchCode = (String) param.get("BRANCHCODE");
        System.out.println("param:" + param);

        if (param.containsKey("USERID")) {
            ServerUtil.verifyLogin(param);
        }

        param.put("LOGINSTATUS", "LOGIN");
        return getData(param);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        map.put("LOGIN_DATE", ServerUtil.getCurrentDate(_branchCode));
        List list = (List) sqlMap.executeQueryForList("loginValidation.getUserIdDetails", map);
        sqlMap.executeUpdate("loginHistory", map);
        List attList = sqlMap.executeQueryForList("getAttendancedetails", map);
        int a = CommonUtil.convertObjToInt(attList.get(0));
        if (a <= 0) {
            sqlMap.executeUpdate("insertAttendance", map);
        }
        returnMap.put("LoginDetails", list);
        returnMap.put(CommonConstants.SESSION_ID, "SI" + getRandomNumberFrom(1,100000));
        returnMap.put(CommonConstants.USER_ID,map.get("USERID"));
        list = null;
        returnMap.put("DB_DRIVER_NAME", dbDriverName);
        return returnMap;
    }

    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return null;
    }
    
    public static int getRandomNumberFrom(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt((max + 1) - min) + min;
        return randomNumber;
    }
}
