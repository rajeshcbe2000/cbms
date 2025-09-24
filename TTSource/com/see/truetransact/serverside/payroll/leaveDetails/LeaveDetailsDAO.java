/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveDetailsDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.leaveDetails;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * LeaveDetailsDAO.
 *
 */
public class LeaveDetailsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LeaveDetailsTO objLeaveDetailsTo;
    HashMap returnMap;
    private Date currDt;

    /**
     * Creates a new instance of LeaveDetailsDAO
     */
    public LeaveDetailsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getLeaveDetails", map);
        if (list != null && list.size() > 0) {
            returnMap.put("LeaveDetailsTO", list);
            return returnMap;
        }
        return null;
    }

    private void insertData(LeaveDetailsTO objLeaveDetailsTo) throws Exception {
        try {
            System.out.println("@#@##@#@#@###@#@#@ objLeaveDetailsTo : "+objLeaveDetailsTo);
            String leavedetailsId = "";
            //added by rishad 15/12/2015
            Calendar start = Calendar.getInstance();
            start.setTime(objLeaveDetailsTo.getLeaveDate());
            System.out.println("@#@##@#@#@###@#@#@ objLeaveDetailsTo 2 : "+objLeaveDetailsTo);
            Calendar end = Calendar.getInstance();
            System.out.println("@#@##@#@#@###@#@#@ objLeaveDetailsTo 3 : "+objLeaveDetailsTo);
            end.setTime(objLeaveDetailsTo.getLeaveToDate());
            //for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) { // Added by nithya for KD-3477
            leavedetailsId = getLeaveDetailsID();
            objLeaveDetailsTo.setLeaveDate(date);
            System.out.println("@#@##@#@#@###@#@#@ objLeaveDetailsTo 5 : "+leavedetailsId);
            objLeaveDetailsTo.setLeaveDetailsId(leavedetailsId);
            sqlMap.executeUpdate("insertLeaveDetailsTo", objLeaveDetailsTo);
            }
            //end
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LeaveDetailsDAO dao = new LeaveDetailsDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#@#@#@##@#@#@#@#@#@#@#@#   map : "+map);
        returnMap = new HashMap();
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objLeaveDetailsTo = (LeaveDetailsTO) map.get("LeaveDetailsTO");
        final String command = objLeaveDetailsTo.getCommand();
        try {
            sqlMap.startTransaction();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(objLeaveDetailsTo);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(objLeaveDetailsTo);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(objLeaveDetailsTo);
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
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objLeaveDetailsTo = null;
    }

    private void updateData(LeaveDetailsTO objLeaveDetailsTo) throws  Exception {
        try {
            sqlMap.executeUpdate("updateLeaveDetails", objLeaveDetailsTo);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void deleteData(LeaveDetailsTO objLeaveDetailsTo) throws SQLException {
        try {
            sqlMap.executeUpdate("deleteLeaveDetails", objLeaveDetailsTo);
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            throw ex;
        }
    }

    private String getLeaveDetailsID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "LEAVE_DETAILS_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }
}
