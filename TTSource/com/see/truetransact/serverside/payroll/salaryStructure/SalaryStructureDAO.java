/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.salaryStructure;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.payroll.salaryStructure.ScaleDetailsTO;
import com.see.truetransact.transferobject.payroll.salaryStructure.ScaleMasterTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * SalaryStructureDAO.
 *
 * @author anjuanand
 *
 */
public class SalaryStructureDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ScaleMasterTO objScaleMasterTO;
    private ScaleDetailsTO objScaleDetailsTO;
    HashMap resultMap = new HashMap();
    Date currDt = null;
    private ArrayList scaleList;
    HashMap returnMap;

    /**
     * Creates a new instance of SalaryStructureDAO
     */
    public SalaryStructureDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            returnMap=new HashMap();
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            currDt = ServerUtil.getCurrentDate(_branchCode);
            objScaleMasterTO = (ScaleMasterTO) map.get("SCALEMASTERTO");
            objScaleDetailsTO = (ScaleDetailsTO) map.get("SCALEDETAILSTO");
            final String command = CommonUtil.convertObjToStr(objScaleMasterTO.getCommand());
            if (objScaleMasterTO != null && objScaleDetailsTO != null) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    if (map.containsKey("SCALEDATA")) {
                        scaleList = (ArrayList) map.get("SCALEDATA");
                        InsertintoScaleTable(scaleList, map);
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    updateDeleteStatusData(map);
                } else {
                    throw new NoCommandException();
                }
            }
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private String getScaleID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "SCALE_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objScaleMasterTO = null;
        objScaleDetailsTO = null;
    }

    private void InsertintoScaleTable(ArrayList scaleList, HashMap map) throws Exception {
        int scaleSize = scaleList.size();
        int count = 1;
        if (objScaleMasterTO.getScaleId() != 0) {
            objScaleMasterTO.setScaleId(objScaleMasterTO.getScaleId());
        } else {
            String scaleId = getScaleID();
            objScaleMasterTO.setScaleId(CommonUtil.convertObjToInt(scaleId));
        }
        objScaleMasterTO.setStatus("CREATED");
        sqlMap.executeUpdate("insertScaleMasterTO", objScaleMasterTO);
        if (scaleList.size() > 0) {
            for (int i = 0; i < scaleSize; i++) {
                objScaleDetailsTO = (ScaleDetailsTO) scaleList.get(i);
                objScaleDetailsTO.setScale_id(objScaleMasterTO.getScaleId());
                if (CommonUtil.convertObjToStr(objScaleDetailsTO.getIncr_amount()) != null && CommonUtil.convertObjToStr(objScaleDetailsTO.getIncr_count()) != null) {
                    objScaleDetailsTO.setSrl_no(count);
                    count = count + 1;
                    objScaleDetailsTO.setIncr_amount(objScaleDetailsTO.getIncr_amount());
                    objScaleDetailsTO.setIncr_count(objScaleDetailsTO.getIncr_count());
                    sqlMap.executeUpdate("insertScaleDetailsTO", objScaleDetailsTO);
                }
            }
        }
         returnMap.put("SCALE_ID",objScaleMasterTO.getScaleId());
    }

    private void updateDeleteStatusData(HashMap map) throws Exception {
        try {
            objScaleMasterTO = (ScaleMasterTO) map.get("SCALEMASTERTO");
            objScaleDetailsTO = (ScaleDetailsTO) map.get("SCALEDETAILSTO");
            sqlMap.executeUpdate("deleteScaleDetailsTO", objScaleDetailsTO);
            sqlMap.executeUpdate("deleteScaleMasterTO", objScaleMasterTO);
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
}