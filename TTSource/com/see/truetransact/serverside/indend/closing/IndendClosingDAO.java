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
package com.see.truetransact.serverside.indend.closing;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

/**
 *
 * @author Suresh R
 */
public class IndendClosingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    HashMap returnMap = new HashMap();
    private Date currDt = null;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public IndendClosingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectIndendCloseTO", map);
        returnMap.put("IndendClosingTO", list);
        return returnMap;
    }

    private String getIndendCloseID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INDEND_CLOSE_ID");
        String closeID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return closeID;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            List finalList = null;
            HashMap singleMap = new HashMap();
            finalList = (List) map.get("INDEND_CLOSING_LIST");
            if (finalList != null && finalList.size() > 0) {
                for (int i = 0; i < finalList.size(); i++) {
                    singleMap = (HashMap) finalList.get(i);
                    //if (CommonUtil.convertObjToDouble(singleMap.get("CLOSING_AMT")) > 0) { //GIBI Said on 07-Aug-2019, If Closing Amt Zero alo Insert
                        singleMap.put("STOCK_TYPE", CommonUtil.convertObjToStr(map.get("STOCK_TYPE")));
                        singleMap.put("CLOSE_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("CLOSE_DATE")))));
                        singleMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                        singleMap.put("STATUS", CommonConstants.STATUS_CREATED);
                        //System.out.println("########## Single Map : " + singleMap);
                        sqlMap.executeUpdate("insertIndendClosingDetails", singleMap);
                    //}
                }
                returnMap.put("SUCCESS", "SUCCESS");
            }
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            IndendClosingDAO dao = new IndendClosingDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("##### Indend Closing DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("INDEND_CLOSING_LIST")) {
            //objTO = (IndendClosingTO) map.get("IndendCloseDetails");
            logDAO = new LogDAO();
            insertData(map);
            destroyObjects();
        }
        System.out.println("#### returnMap :" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
    }
}
