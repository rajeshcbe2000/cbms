/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigDAO.java
 *
 * Created on Mon Jun 24 17:19:05 IST 2019
 */
package com.see.truetransact.serverside.indend.reservedepreciationachd;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Suresh R
 */
public class ReserveDepreciationAcHdDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    HashMap returnMap = new HashMap();

    public ReserveDepreciationAcHdDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        returnMap = new HashMap();
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            List finalList = null;
            HashMap singleMap = new HashMap();
            finalList = (List) map.get("RESERVE_DEP_ACHD");
            String acHD = "";
            String incHD = "";
            String expHD = "";
            if (finalList != null && finalList.size() > 0) {
                for (int i = 0; i < finalList.size(); i++) {
                    singleMap = (HashMap) finalList.get(i);
                    acHD = CommonUtil.convertObjToStr(singleMap.get("AC_HD_ID"));
                    if (acHD.indexOf(" ") != -1) {
                        acHD = acHD.substring(0, acHD.indexOf(" "));
                    }
                    singleMap.put("AC_HD_ID",acHD);
                    
                    incHD = CommonUtil.convertObjToStr(singleMap.get("INC_AC_HD"));
                    if (incHD.indexOf(" ") != -1) {
                        incHD = incHD.substring(0, incHD.indexOf(" "));
                    }
                    singleMap.put("INC_AC_HD",incHD);
                    
                    expHD = CommonUtil.convertObjToStr(singleMap.get("EXP_AC_HD"));
                    if (expHD.indexOf(" ") != -1) {
                        expHD = expHD.substring(0, expHD.indexOf(" "));
                    }
                    singleMap.put("EXP_AC_HD",expHD);

                    System.out.println("########## Single Map : " + singleMap);
                    sqlMap.executeUpdate("insertReserveDepAcHdDetails", singleMap);
                }
                returnMap.put("SUCCESS", "SUCCESS");
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            ReserveDepreciationAcHdDAO dao = new ReserveDepreciationAcHdDAO();
        } catch (Exception ex) {
        }
    }

    @Override
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("##### Reserve Depreciation  DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("RESERVE_DEP_ACHD")) {
            logDAO = new LogDAO();
            insertData(map);
        }
        System.out.println("#### returnMap :" + returnMap);
        return returnMap;
    }

    @Override
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
}
