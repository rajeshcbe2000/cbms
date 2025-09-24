/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AdvancesProductDAO.java
 *
 * Created on December 24, 2003, 6:44 PM
 */
package com.see.truetransact.serverside.product.advances;

/**
 *
 * @author Hemant
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.transferobject.product.advances.*;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import com.see.truetransact.servicelocator.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.product.loan.LoanProductAccountTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
public class AdvancesProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    //    private AdvancesProductTO advancesProductTO;
    private LoanProductAccountTO loanProductAccountTO;
    private AdvancesProductAccHeadTO advancesProductAccHeadTO;
    private AdvancesProductAccParameterTO advancesProductAccParameterTO;
    private AdvancesProductChargesTO advancesProductChargesTO;
    private List advancesProductChqChargesTO;
    private AdvancesProductIPTO advancesProductIPTO;
    private AdvancesProductIRTO advancesProductIRTO;
    private AdvancesProductSITO advancesProductSITO;
    private List advancesProductSplAssetsTO;
    private Date currDt = null;
    /**
     * Creates a new instance of AdvancesProductDAO
     */
    public AdvancesProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);

        //        List list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductTO", where);
        //        returnMap.put("AdvancesProductTO", list);
        List list = null;
        if (map.containsKey("AGRI")) {
            list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductAccountTO", where);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectLoanProductAccountTO", where);
        }
        returnMap.put("LoanProductAccountTO", list);

        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductAccHeadTO", where);
        returnMap.put("AdvancesProductAccHeadTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductAccParameterTO", where);
        returnMap.put("AdvancesProductAccParameterTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductChargesTO", where);
        returnMap.put("AdvancesProductChargesTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductChqChargesTO", where);
        returnMap.put("AdvancesProductChqChargesTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductIPTO", where);
        returnMap.put("AdvancesProductIPTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductIRTO", where);
        returnMap.put("AdvancesProductIRTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductSITO", where);
        returnMap.put("AdvancesProductSITO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAdvancesProductSplAssetsTO", where);
        returnMap.put("AdvancesProductSplAssetsTO", list);
        if (map.containsKey("AGRI")) {
            returnMap.put("AGRI", "AGRI");
        }
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            //            sqlMap.executeUpdate ("insertAdvancesProductTO", advancesProductTO);
            //            sqlMap.executeUpdate ("insertLoanProductAccountTO", loanProductAccountTO);

            sqlMap.executeUpdate("insertAdvancesProductAccHeadTO", advancesProductAccHeadTO);
            objLogTO.setData(advancesProductAccHeadTO.toString());
            objLogTO.setPrimaryKey(advancesProductAccHeadTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAdvancesProductAccParameterTO", advancesProductAccParameterTO);
            objLogTO.setData(advancesProductAccParameterTO.toString());
            objLogTO.setPrimaryKey(advancesProductAccParameterTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAdvancesProductChargesTO", advancesProductChargesTO);
            objLogTO.setData(advancesProductChargesTO.toString());
            objLogTO.setPrimaryKey(advancesProductChargesTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            if (advancesProductChqChargesTO != null && advancesProductChqChargesTO.size() > 0) {
                int entries = advancesProductChqChargesTO.size();
                AdvancesProductChqChargesTO objAdvProdChqCharg;
                //if(!((AdvancesProductChqChargesTO)advancesProductChqChargesTO.get(0)).getChqRetChrgtype().equals("")){
                for (int i = 0; i < entries; i++) {
                    objAdvProdChqCharg = (AdvancesProductChqChargesTO) advancesProductChqChargesTO.get(i);
                    sqlMap.executeUpdate("insertAdvancesProductChqChargesTO", objAdvProdChqCharg);
                    objLogTO.setData(objAdvProdChqCharg.toString());
                    objLogTO.setPrimaryKey(objAdvProdChqCharg.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
            }
            sqlMap.executeUpdate("insertAdvancesProductIPTO", advancesProductIPTO);
            objLogTO.setData(advancesProductIPTO.toString());
            objLogTO.setPrimaryKey(advancesProductIPTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAdvancesProductIRTO", advancesProductIRTO);
            objLogTO.setData(advancesProductIRTO.toString());
            objLogTO.setPrimaryKey(advancesProductIRTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAdvancesProductSITO", advancesProductSITO);
            objLogTO.setData(advancesProductSITO.toString());
            objLogTO.setPrimaryKey(advancesProductSITO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            if (advancesProductSplAssetsTO != null && advancesProductSplAssetsTO.size() > 0) {
                int entries = advancesProductSplAssetsTO.size();
                //if(!((AdvancesProductSplAssetsTO)advancesProductSplAssetsTO.get(0)).getAssetCategory().equals("")){
                AdvancesProductSplAssetsTO objAdvProdSplAsset;
                for (int i = 0; i < entries; i++) {
                    objAdvProdSplAsset = (AdvancesProductSplAssetsTO) advancesProductSplAssetsTO.get(i);
                    sqlMap.executeUpdate("insertAdvancesProductSplAssetsTO", objAdvProdSplAsset);
                    objLogTO.setData(objAdvProdSplAsset.toString());
                    objLogTO.setPrimaryKey(objAdvProdSplAsset.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
            }



            /*
             * sqlMap.executeUpdate ("insertOperativeAcctIntRateParamTO", operativeAcctIntRateParamTO);
             */
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            //            sqlMap.executeUpdate ("updateAdvancesProductTO", advancesProductTO);
            //            sqlMap.executeUpdate ("updateLoanProductAccountTO", loanProductAccountTO);

            sqlMap.executeUpdate("updateAdvancesProductAccHeadTO", advancesProductAccHeadTO);
            objLogTO.setData(advancesProductAccHeadTO.toString());
            objLogTO.setPrimaryKey(advancesProductAccHeadTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAdvancesProductAccParameterTO", advancesProductAccParameterTO);
            objLogTO.setData(advancesProductAccParameterTO.toString());
            objLogTO.setPrimaryKey(advancesProductAccParameterTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAdvancesProductChargesTO", advancesProductChargesTO);
            objLogTO.setData(advancesProductChargesTO.toString());
            objLogTO.setPrimaryKey(advancesProductChargesTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            AdvancesProductChqChargesTO apCCTO;
            if (advancesProductChqChargesTO != null && advancesProductChqChargesTO.size() > 0) {
                apCCTO = (AdvancesProductChqChargesTO) advancesProductChqChargesTO.get(0);
                sqlMap.executeUpdate("deleteAdvancesProductChqChargesTO", apCCTO);
                objLogTO.setData(apCCTO.toString());
                objLogTO.setPrimaryKey(apCCTO.getKeyData());
                objLogDAO.addToLog(objLogTO);

                int entries = advancesProductChqChargesTO.size();
                AdvancesProductChqChargesTO objAdvProdChqCharg;
                for (int i = 0; i < entries; i++) {
                    objAdvProdChqCharg = (AdvancesProductChqChargesTO) advancesProductChqChargesTO.get(i);
                    sqlMap.executeUpdate("insertAdvancesProductChqChargesTO", objAdvProdChqCharg);
                    objLogTO.setData(objAdvProdChqCharg.toString());
                    objLogTO.setPrimaryKey(objAdvProdChqCharg.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }

            } else {
                apCCTO = new AdvancesProductChqChargesTO();
                //                apCCTO.setProdId(advancesProductTO.getProdId());
                apCCTO.setProdId(loanProductAccountTO.getProdId());
                sqlMap.executeUpdate("deleteAdvancesProductChqChargesTO", apCCTO);
                objLogTO.setData(apCCTO.toString());
                objLogTO.setPrimaryKey(apCCTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            sqlMap.executeUpdate("updateAdvancesProductIPTO", advancesProductIPTO);
            objLogTO.setData(advancesProductIPTO.toString());
            objLogTO.setPrimaryKey(advancesProductIPTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAdvancesProductIRTO", advancesProductIRTO);
            objLogTO.setData(advancesProductIRTO.toString());
            objLogTO.setPrimaryKey(advancesProductIRTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAdvancesProductSITO", advancesProductSITO);
            objLogTO.setData(advancesProductSITO.toString());
            objLogTO.setPrimaryKey(advancesProductSITO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            AdvancesProductSplAssetsTO apSATO;
            if (advancesProductSplAssetsTO != null && advancesProductSplAssetsTO.size() > 0) {
                apSATO = (AdvancesProductSplAssetsTO) advancesProductSplAssetsTO.get(0);
                sqlMap.executeUpdate("deleteAdvancesProductSplAssetsTO", apSATO);
                objLogTO.setData(apSATO.toString());
                objLogTO.setPrimaryKey(apSATO.getKeyData());
                objLogDAO.addToLog(objLogTO);

                int entries = advancesProductSplAssetsTO.size();
                for (int i = 0; i < entries; i++) {
                    apSATO = (AdvancesProductSplAssetsTO) advancesProductSplAssetsTO.get(i);
                    sqlMap.executeUpdate("insertAdvancesProductSplAssetsTO", apSATO);

                    objLogTO.setData(apSATO.toString());
                    objLogTO.setPrimaryKey(apSATO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                }
            } else {
                apSATO = new AdvancesProductSplAssetsTO();
                //                apSATO.setProdId(advancesProductTO.getProdId());
                apSATO.setProdId(loanProductAccountTO.getProdId());
                sqlMap.executeUpdate("deleteAdvancesProductSplAssetsTO", apSATO);
                objLogTO.setData(apSATO.toString());
                objLogTO.setPrimaryKey(apSATO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

            //sqlMap.executeUpdate ("updateAdvancesProductSplAssetsTO", advancesProductSplAssetsTO);

            /*
             * sqlMap.executeUpdate ("updateOperativeAcctIntRateParamTO", operativeAcctIntRateParamTO);
             */
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteLoanProductAccountTO", loanProductAccountTO);
            objLogTO.setData(loanProductAccountTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            /*
             * sqlMap.executeUpdate ("deleteAdvancesProductAccHeadTO",
             * advancesProductAccHeadTO); sqlMap.executeUpdate
             * ("deleteAdvancesProductAccParameterTO",
             * advancesProductAccParameterTO); sqlMap.executeUpdate
             * ("deleteAdvancesProductChargesTO", advancesProductChargesTO);
             * sqlMap.executeUpdate ("deleteAdvancesProductChqChargesTO",
             * advancesProductChqChargesTO); sqlMap.executeUpdate
             * ("deleteAdvancesProductIPTO", advancesProductIPTO);
             * sqlMap.executeUpdate ("deleteAdvancesProductIRTO",
             * advancesProductIRTO); sqlMap.executeUpdate
             * ("deleteAdvancesProductSITO", advancesProductSITO);
             * sqlMap.executeUpdate ("deleteAdvancesProductSplAssetsTO",
             * advancesProductSplAssetsTO);
             */

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        loanProductAccountTO = (LoanProductAccountTO) map.get("LoanProductAccountTO");
        advancesProductAccHeadTO = (AdvancesProductAccHeadTO) map.get("AdvancesProductAccHeadTO");
        advancesProductAccParameterTO = (AdvancesProductAccParameterTO) map.get("AdvancesProductAccParameterTO");
        advancesProductChargesTO = (AdvancesProductChargesTO) map.get("AdvancesProductChargesTO");
        advancesProductChqChargesTO = (List) map.get("AdvancesProductChqChargesTO");
        advancesProductIPTO = (AdvancesProductIPTO) map.get("AdvancesProductIPTO");
        advancesProductIRTO = (AdvancesProductIRTO) map.get("AdvancesProductIRTO");
        advancesProductSITO = (AdvancesProductSITO) map.get("AdvancesProductSITO");
        advancesProductSplAssetsTO = (List) map.get("AdvancesProductSplAssetsTO");
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * operativeAcctIntRateParamTO = (OperativeAcctIntRateParamTO) map.get("OperativeAcctIntRateParamTO");
         */

        final String command = loanProductAccountTO.getCommand();
        loanProductAccountTO.setStatusBy((String) map.get(CommonConstants.USER_ID));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            //            loanProductAccountTO.setCreatedBy((String) map.get(CommonConstants.USER_ID));
            //            loanProductAccountTO.setCreatedDt(currDt);
            //            loanProductAccountTO.setStatus(CommonConstants.STATUS_CREATED);
            //            loanProductAccountTO.setStatusDt(currDt);
            insertData(objLogDAO, objLogTO);

        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            //            loanProductAccountTO.setStatus(CommonConstants.STATUS_MODIFIED);
            //            loanProductAccountTO.setStatusDt(currDt);
            updateData(objLogDAO, objLogTO);

        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            //            loanProductAccountTO.setStatus(CommonConstants.STATUS_DELETED);
            loanProductAccountTO.setStatusDt(currDt);
            deleteData(objLogDAO, objLogTO);

        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void destroyObjects() {
        //        advancesProductTO = null;
        loanProductAccountTO = null;
        advancesProductAccHeadTO = null;
        advancesProductAccParameterTO = null;
        advancesProductChargesTO = null;
        advancesProductChqChargesTO = null;
        advancesProductIPTO = null;
        advancesProductIRTO = null;
        advancesProductSITO = null;
        advancesProductSplAssetsTO = null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //            AdvancesProductDAO apDAO = new AdvancesProductDAO();
            //            HashMap hash = new HashMap();
            //            HashMap result;
            //            hash.put(CommonConstants.MAP_WHERE,"AP#0002");
            //            result = apDAO.executeQuery(hash);
            //
            //            System.out.println("Getting AdvancesProductTO List");
            //            List apList = (List) result.get("AdvancesProductTO");
            //            AdvancesProductTO apTO = (AdvancesProductTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductAccHeadTO");
            //            AdvancesProductAccHeadTO apAHTO = (AdvancesProductAccHeadTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductAccParameterTO");
            //            AdvancesProductAccParameterTO apAPTO = (AdvancesProductAccParameterTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductChargesTO");
            //            AdvancesProductChargesTO apCTO = (AdvancesProductChargesTO) apList.get(0);
            //
            //           // apList = (List) result.get("AdvancesProductChqChargesTO");
            //            //AdvancesProductChqChargesTO apCCTO = (AdvancesProductChqChargesTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductIPTO");
            //            AdvancesProductIPTO apIPTO = (AdvancesProductIPTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductIRTO");
            //            AdvancesProductIRTO apIRTO = (AdvancesProductIRTO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductSITO");
            //            AdvancesProductSITO apSITO = (AdvancesProductSITO) apList.get(0);
            //
            //            apList = (List) result.get("AdvancesProductSplAssetsTO");
            //            AdvancesProductSplAssetsTO apSplAssetsTO = (AdvancesProductSplAssetsTO) apList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            //e.printStackTrace();
            //log.error(e);
        }
    }
}
