/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveMasterDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.leaveMaster;

/*
 * @author anjuanand
 *
 */
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.payroll.leaveDetails.LeaveDetailsTO;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * LeaveMasterDAO.
 *
 */
public class LeaveMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LeaveDetailsTO objLeaveMasterTo;
    HashMap returnMap;
    private Date currDt;

    /**
     * Creates a new instance of LeaveMasterDAO
     */
    public LeaveMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        return null;
    }

    private void insertData(LeaveDetailsTO objLeaveMasterTo) throws Exception {
        try {
            String leaveId = "";
            leaveId = getLeaveID();
            objLeaveMasterTo.setLeaveID(leaveId);
            sqlMap.executeUpdate("insertLeaveMasterTo", objLeaveMasterTo);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LeaveMasterDAO dao = new LeaveMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getLeaveID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "LEAVE_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    public HashMap execute(HashMap map) throws Exception {
        returnMap = new HashMap();
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objLeaveMasterTo = (LeaveDetailsTO) map.get("LeaveMasterTO");
        final String command = objLeaveMasterTo.getCommand();
        try {
            sqlMap.startTransaction();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLeaveMasterTo);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(objLeaveMasterTo);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLeaveMasterTo);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objLeaveMasterTo = null;
    }

    private void updateData(LeaveDetailsTO objLeaveMasterTo) throws Exception {
        try {
            sqlMap.executeUpdate("updateLeaveMaster", objLeaveMasterTo);
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
    }

    private void deleteData(LeaveDetailsTO objLeaveMasterTo) throws Exception {
        try {
            sqlMap.executeUpdate("deleteLeaveMaster", objLeaveMasterTo);
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
    }
}
