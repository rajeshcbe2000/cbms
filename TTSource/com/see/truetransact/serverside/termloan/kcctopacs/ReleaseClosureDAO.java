/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ReleaseClosureDAO.java
 *
 * Created on Thu Mar 07 18:10:16 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import java.util.Map;
import com.see.truetransact.clientutil.*;

/**
 * ReleaseClosureDAO.
 *
 */
public class ReleaseClosureDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private final static Logger log = Logger.getLogger(ReleaseClosureDAO.class);
    private Map returnMap = null;
    private Date currDt = null;
    public List closingLst = null;
    public List closingAuthLst = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public ReleaseClosureDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getRelCloseNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REL_CLOS_ID");
        String relClosno = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return relClosno;
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List closingLst = (List) sqlMap.executeQueryForList("getNclReleasedCloseTableDetails", map);
        if (closingLst != null && closingLst.size() > 0) {
            System.out.println("@@@closingLst" + closingLst);
            returnMap.put("REL_CLOSED_LIST", closingLst);
        }
        System.out.println("@@@returnMap" + returnMap);
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in ReleaseClosure DAO : " + map);
        try {
            if (map.containsKey("RELEASE_CLOSING_DETAILS")) {
                returnMap = new HashMap();
                closingLst = (List) map.get("RELEASE_CLOSING_DETAILS");
                System.out.println("@##$#$% closingLst #### :" + closingLst);
                final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                    System.out.println("%%%%%%%%%% map" + map);
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP) && map.containsKey("RELEASE_CLOSING_AUTH_DETAILS")) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                closingAuthLst = (List) map.get("RELEASE_CLOSING_AUTH_DETAILS");
                final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
                if (authMap != null) {
                    if (command.equals(CommonConstants.STATUS_REJECTED) || command.equals(CommonConstants.STATUS_EXCEPTION)) {
                        authorizeData(authMap);
                    } else {
                        authorize(authMap, closingAuthLst);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            String relClosno = "";
            relClosno = getRelCloseNo();
            if (relClosno.length() > 0) {
                if (closingLst != null) {
                    for (int i = 0; i < closingLst.size(); i++) {
                        rowList = new ArrayList();
                        dataMap = (HashMap) closingLst.get(i);
                        dataMap.put("REL_CLOSE_ID", relClosno);
                        dataMap.put("STATUS_BY", map.get("STATUS_BY"));
                        System.out.println("$$$$$$dataMap" + dataMap);
                        sqlMap.executeUpdate("insertClosedReleaseDetails", dataMap);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            System.out.println("&&&&&&&&&&&&&&map" + map);
            List closingLst = (List) map.get("RELEASE_CLOSING_DETAILS");
            whereMap = (HashMap) closingLst.get(0);
            sqlMap.executeUpdate("deleteNclReleaseClosedDetails", whereMap);
            if (closingLst != null && closingLst.size() > 0) {
                for (int i = 0; i < closingLst.size(); i++) {
                    dataMap = (HashMap) closingLst.get(i);
                    dataMap.put("REL_CLOSE_ID", dataMap.get("REL_CLOSE_NO"));
                    dataMap.put("STATUS_BY", map.get("STATUS_BY"));
                    System.out.println("&&&&&&&&&&&&&&dataMap" + dataMap);
                    sqlMap.executeUpdate("insertClosedReleaseDetails", dataMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map, List closedLst) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        HashMap whereMap = new HashMap();
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            for (int i = 0; i < closedLst.size(); i++) {
                whereMap = (HashMap) closedLst.get(i);
                sqlMap.executeUpdate("updateNclReleaseClosedStatus", whereMap);
            }
            sqlMap.executeUpdate("authorizeNclReleaseCloseDetails", AuthMap);
            String status = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void authorizeData(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("authorizeNclReleaseCloseDetails", AuthMap);
            String status = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ReleaseClosureDAO dao = new ReleaseClosureDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        closingLst = null;
    }
}