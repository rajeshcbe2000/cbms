/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IDGenerateDAO.java
 *
 * Created on September 16, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.common.idgenerate;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

/**
 * @author Balachandar
 */
public class IDGenerateDAO extends TTDAO {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of IDGenerateDAO
     */
    public IDGenerateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap result = null;
        try {
            HashMap hash = null;
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", map.get(CommonConstants.MAP_WHERE));
            if (map.containsKey("INIT_TRANS_ID")) {
                where.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            }

//            java.util.Properties serverProperties = new java.util.Properties();
//            Dummy cons = new Dummy();
//            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
//            String dataBaseURL = serverProperties.getProperty("url");
//            String userName = serverProperties.getProperty("username");
//            String passWord = serverProperties.getProperty("password");
//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//            Connection conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
//            Statement stmt = conn.createStatement();
//            String st = "SELECT GET_CURR_ID('"+where+"') FROM DUAL";
////            String st = "SELECT GET_CURR_ID(where) FROM DUAL";
//            System.out.println("st  $$$"+st);
//            ResultSet rs = stmt.executeQuery(st);
//            rs.next();
//            String genID = rs.getString(1);
//                result = new HashMap();
//                result.put(CommonConstants.DATA, genID);
//            serverProperties = null;
//            cons = null;
//            conn = null;
//            stmt = null;
//            rs = null;
//        } catch (Exception ex) {
//            System.out.println("#$#$ Exception in SelectAllDAO getReportData() : "+ex);
//            ex.printStackTrace();
//            }

//            List list = null;
            //sqlMap.startTransaction();
//            sqlMap.executeUpdate("updateIDGenerated", where);
//            list = (List) sqlMap.executeQueryForList(mapName, where);
            //sqlMap.commitTransaction();

//            if (list.size() > 0) {
//                hash = (HashMap) list.get(0);
//                String strPrefix = "", strLen = "";
//
//                // Prefix for the ID.
//                if (hash.containsKey("PREFIX")) {
//                    strPrefix = (String) hash.get("PREFIX");
//                    if (strPrefix == null || strPrefix.trim().length() == 0) {
//                        strPrefix = "";
//                    }
//                }
//
//                // Maximum Length for the ID
//                int len = 10;
//                if (hash.containsKey("ID_LENGTH")) {
//                    strLen = String.valueOf(hash.get("ID_LENGTH"));
//                    if (strLen == null || strLen.trim().length() == 0) {
//                        len = 10;
//                    } else {
//                        len = Integer.parseInt(strLen.trim());
//                    }
//                }
//
//                int numFrom = strPrefix.trim().length();
//
////                String newID = String.valueOf(Integer.parseInt(String.valueOf(hash.get("CURR_VALUE"))) + 1);
//                String newID = String.valueOf(hash.get("CURR_VALUE"));
//
//                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
//                result = new HashMap();
//                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            String genID = CommonUtil.convertObjToStr(sqlMap.executeQueryForObject("getUpdatedCurrID", where));
            System.out.println("genID="+genID);
            result = new HashMap();
            result.put(CommonConstants.DATA, genID);
//            }
        } catch (Exception exc) {
            //sqlMap.rollbackTransaction();
            throw new TransRollbackException(exc);
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            IDGenerateDAO dao = new IDGenerateDAO();
            HashMap where = new HashMap();
            where.put("WHERE", "TRANSFER.BATCH_ID");
            System.out.println("1");
            String rMap = (String) dao.executeQuery(where).get("DATA");
            System.out.println("String : " + rMap.toString());
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
}
