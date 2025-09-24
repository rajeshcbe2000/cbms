 /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.courtexpensesetting;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.courtexpensesetting.CourtExpenseSettingTO;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class CourtExpenseSettingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CourtExpenseSettingTO ObjCourtExpenseSettingTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(CourtExpenseSettingDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public CourtExpenseSettingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectcourtexpenseTO", map);
        returnMap.put("CourtTO", list);
        List listt = (List) sqlMap.executeQueryForList("getSelectcourtexpenseproductTO", map);
        List selectedList = new ArrayList();
        for (int i = 0; i < listt.size(); i++) {
            HashMap mapp = new HashMap();
            HashMap mapp1 = new HashMap();
            mapp = (HashMap) listt.get(i);
            mapp1.put("PROD_ID", mapp.get("PROD_ID"));
            mapp1.put("PROD_DESC", mapp.get("PROD_DESC"));
            selectedList.add(mapp1);

        }
        returnMap.put("CourtProductTO", selectedList);
        return returnMap;
    }

    private String getid() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEE");
        where.put(CommonConstants.MAP_WHERE, "CE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjCourtExpenseSettingTO.setId(getid());
            System.out.println("%%%%%%%%%" + ObjCourtExpenseSettingTO.getId());
            sqlMap.executeUpdate("insertcourtexpenseTO", ObjCourtExpenseSettingTO);
            List iList = ObjCourtExpenseSettingTO.getList();
            for (int i = 0; i < iList.size(); i++) {
                HashMap m2 = new HashMap();
                CourtExpenseSettingTO ObjCourtExpenseSettingTO1 = new CourtExpenseSettingTO();
                m2 = (HashMap) iList.get(i);
                ObjCourtExpenseSettingTO1.setPid(m2.get("PROD_ID").toString());
                ObjCourtExpenseSettingTO1.setId(ObjCourtExpenseSettingTO.getId());
                sqlMap.executeUpdate("insertcourtexpenseproductTO", ObjCourtExpenseSettingTO1);

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
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updatecourtexpenseTO", ObjCourtExpenseSettingTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = ObjCourtExpenseSettingTO.getStatusBy();
            ObjCourtExpenseSettingTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deletecourtexpenseTO", ObjCourtExpenseSettingTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        ObjCourtExpenseSettingTO = (CourtExpenseSettingTO) map.get("CourtExpenseSetting");
        ObjCourtExpenseSettingTO.setBranCode(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            System.out.println("mapmapmapmapmap" + map);
            insertData(map);
            System.out.println("objTO.getid()4444" + ObjCourtExpenseSettingTO.getId());
            returnMap = new HashMap();
            returnMap.put("CE_ID", ObjCourtExpenseSettingTO.getId());

        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);

        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjCourtExpenseSettingTO = null;
        logTO = null;
        logDAO = null;
    }
}
