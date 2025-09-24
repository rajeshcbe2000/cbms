/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.payroll.pfMaster;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.payroll.pfMaster.PFMasterTO;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * PFMaster DAO.
 *
 * @author anjuanand
 *
 */
public class PFMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    HashMap resultMap = new HashMap();
    private PFMasterTO objPfMasterTo;
    private Date curr_dt = null;

    /**
     * Creates a new instance of PFMasterDAO
     */
    public PFMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            curr_dt = ServerUtil.getCurrentDate(_branchCode);
            objPfMasterTo = (PFMasterTO) map.get("PF_MASTER_DATA");
            final String command = CommonUtil.convertObjToStr(objPfMasterTo.getCommand());
            if (objPfMasterTo != null) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertPFMaster(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updatePFMaster(objPfMasterTo);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deletePFMaster(objPfMasterTo);
                } else {
                    throw new NoCommandException();
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
//        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private String getPfID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "PF_ID");
        return CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
    }
    
    private void insertPFMaster(HashMap map) throws Exception {
        try {
            PFMasterTO objPfMastTo = (PFMasterTO) map.get("PF_MASTER_DATA");
            String pfId = getPfID();
            objPfMastTo.setPfId(pfId);
            objPfMastTo.setBranchId(_branchCode);
            objPfMastTo.setStatus("CREATED");
            sqlMap.executeUpdate("insertPFMasterTO", objPfMastTo);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private void updatePFMaster(PFMasterTO objPfMasterTo) throws SQLException {
        try {
            objPfMasterTo.setBranchId(_branchCode);
            objPfMasterTo.setStatus("UPDATED");
            sqlMap.executeUpdate("updatePFMasterTO", objPfMasterTo);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private void deletePFMaster(PFMasterTO objPfMasterTo) throws Exception {
        try {
            sqlMap.executeUpdate("deletePFMasterTO", objPfMasterTo);
        } catch (SQLException ex) {
            throw ex;
        }
    }
}