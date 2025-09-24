/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SanctionMasterDAO.java
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
import com.see.truetransact.transferobject.mdsapplication.mdschangeofmember.MDSChangeofMemberTO;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclSanctionDetailsTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclSubLimitTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

/**
 * SanctionMaster DAO.
 *
 */
public class SanctionMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private NclSubLimitTO objSublimitDetailsrTO;
    private NclSanctionDetailsTO objSanctionDetailsrTO;
    private NclClassificationTO objClasficationDetailsTO;
    private NclAmtSlabWiseDetTO objLoanSlabDetailsTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(SanctionMasterDAO.class);
    private LinkedHashMap sublimittableDetails = null;
    private LinkedHashMap membertableDetails = null;
    private LinkedHashMap loantableDetails = null;
    private Date currDt = null;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public SanctionMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getSanctionNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SANCTION_NO");
        String sancNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return sancNo;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectNclSanctionDetailsTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("objSanctionDetailsrTO", list);
        }
        List sublist = (List) sqlMap.executeQueryForList("getSelectNclSubLimitTO", map);
        if (sublist != null && sublist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = sublist.size(), j = 0; i > 0; i--, j++) {
                String st = ((NclSubLimitTO) sublist.get(j)).getNclSanctionNo();
                ParMap.put(CommonUtil.convertObjToStr(((NclSubLimitTO) sublist.get(j)).getStartFinYear()), sublist.get(j));
            }
            returnMap.put("SUB_LIMIT", ParMap);
        }
        List Memlist = (List) sqlMap.executeQueryForList("getClassificationMemberTO", map);
        if (Memlist != null && Memlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = Memlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((NclClassificationTO) Memlist.get(j)).getNclSanctionNo();
                ParMap.put(j + 1, Memlist.get(j));
            }
            returnMap.put("MEMBER_DATA", ParMap);
        }

        List loanlist = (List) sqlMap.executeQueryForList("getSelectNclAmtSlabWiseDetTO", map);
        if (loanlist != null && loanlist.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = loanlist.size(), j = 0; i > 0; i--, j++) {
                String st = ((NclAmtSlabWiseDetTO) loanlist.get(j)).getNclSanctionNo();
//                ParMap.put(CommonUtil.convertObjToStr(((NclAmtSlabWiseDetTO) loanlist.get(j)).getFromAmt()), loanlist.get(j));
                ParMap.put(((NclAmtSlabWiseDetTO) loanlist.get(j)).getFromAmt(), loanlist.get(j));
            }
            returnMap.put("LOAN_DATA", ParMap);
        }
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("##################### Sanction Map in DAO: " + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objSanctionDetailsrTO = (NclSanctionDetailsTO) map.get("NclSanctionDetails");
        sublimittableDetails = (LinkedHashMap) map.get("SublimitDetails");
        membertableDetails = (LinkedHashMap) map.get("MemberDetails");
        loantableDetails = (LinkedHashMap) map.get("LoanSlabDetails");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        returnMap.put("NCL_SANCTION_NO", objSanctionDetailsrTO.getNclSanctionNo());
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            String sancNo = "";
            sancNo = getSanctionNo();
            if (sancNo.length() > 0) {
                String year1 = DateUtil.getStringDate(currDt);
                year1 = year1.replace("/", "");
                year1 = year1.substring(6, 8);
                int end = CommonUtil.convertObjToInt(map.get("END_YEAR"));
                String sanctionNo = "N";
//                System.out.println("########## Sanction Start Year : " + year1);
//                System.out.println("########## Sanction End   Year : " + end);
                sanctionNo = sanctionNo + year1;
                sanctionNo = sanctionNo + end;
                sanctionNo = sanctionNo + sancNo;
                System.out.println("########## Sanction Number : " + sanctionNo);
                objSanctionDetailsrTO.setNclSanctionNo(sanctionNo);
                sqlMap.executeUpdate("insertNclSanctionDetailsTO", objSanctionDetailsrTO);
                insertSublimitData(sanctionNo);
                insertMemberData(sanctionNo);
                insertLoanData(sanctionNo);
            }
            logTO.setData(objSanctionDetailsrTO.toString());
            logTO.setPrimaryKey(objSanctionDetailsrTO.getKeyData());
            logTO.setStatus(objSanctionDetailsrTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertMemberData(String sancNo) throws Exception {
        try {
            if (membertableDetails != null) {
                ArrayList addList = new ArrayList(membertableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO objClasficationDetailsTO = (NclClassificationTO) membertableDetails.get(addList.get(i));
                    objClasficationDetailsTO.setNclSanctionNo(sancNo);
                    sqlMap.executeUpdate("insertClassificationMemberTO", objClasficationDetailsTO);
                    logTO.setData(objClasficationDetailsTO.toString());
                    logTO.setPrimaryKey(objClasficationDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objClasficationDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        sancNo = null;
    }

    private void insertLoanData(String sancNo) throws Exception {
        try {
            if (loantableDetails != null) {
                ArrayList addList = new ArrayList(loantableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO objLoanSlabDetailsTO = (NclAmtSlabWiseDetTO) loantableDetails.get(addList.get(i));
                    objLoanSlabDetailsTO.setNclSanctionNo(sancNo);
                    sqlMap.executeUpdate("insertNclAmtSlabWiseDetTO", objLoanSlabDetailsTO);
                    logTO.setData(objLoanSlabDetailsTO.toString());
                    logTO.setPrimaryKey(objLoanSlabDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objLoanSlabDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        sancNo = null;
    }

    private void insertSublimitData(String sancNo) throws Exception {
        try {
            if (sublimittableDetails != null) {
                ArrayList addList = new ArrayList(sublimittableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclSubLimitTO objSublimitDetailsrTO = (NclSubLimitTO) sublimittableDetails.get(addList.get(i));
                    objSublimitDetailsrTO.setNclSanctionNo(sancNo);
                    sqlMap.executeUpdate("insertNclSubLimitTO", objSublimitDetailsrTO);
                    logTO.setData(objSublimitDetailsrTO.toString());
                    logTO.setPrimaryKey(objSublimitDetailsrTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objSublimitDetailsrTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        sancNo = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateNclSanctionDetailsTO", objSanctionDetailsrTO);
            updateSublimitData(map);
            updateMemberData(map);
            updateLoanData(map);
            logTO.setData(objSanctionDetailsrTO.toString());
            logTO.setPrimaryKey(objSanctionDetailsrTO.getKeyData());
            logTO.setStatus(objSanctionDetailsrTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateSublimitData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteSublimitData", objSanctionDetailsrTO);
            if (sublimittableDetails != null) {
                ArrayList addList = new ArrayList(sublimittableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclSubLimitTO objSublimitDetailsrTO = (NclSubLimitTO) sublimittableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertNclSubLimitTO", objSublimitDetailsrTO);
                    logTO.setData(objSublimitDetailsrTO.toString());
                    logTO.setPrimaryKey(objSublimitDetailsrTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objSublimitDetailsrTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateMemberData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteMemberData", objSanctionDetailsrTO);
            if (membertableDetails != null) {
                ArrayList addList = new ArrayList(membertableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO objClasficationDetailsTO = (NclClassificationTO) membertableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertClassificationMemberTO", objClasficationDetailsTO);
                    logTO.setData(objClasficationDetailsTO.toString());
                    logTO.setPrimaryKey(objClasficationDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objClasficationDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateLoanData(HashMap map) throws Exception {
        try {
            sqlMap.executeUpdate("deleteLoanData", objSanctionDetailsrTO);
            if (loantableDetails != null) {
                ArrayList addList = new ArrayList(loantableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO objLoanSlabDetailsTO = (NclAmtSlabWiseDetTO) loantableDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertNclAmtSlabWiseDetTO", objLoanSlabDetailsTO);
                    logTO.setData(objLoanSlabDetailsTO.toString());
                    logTO.setPrimaryKey(objLoanSlabDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objLoanSlabDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteNclSanctionDetailsTO", objSanctionDetailsrTO);
            logTO.setData(objSanctionDetailsrTO.toString());
            logTO.setPrimaryKey(objSanctionDetailsrTO.getKeyData());
            logTO.setStatus(objSanctionDetailsrTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeNclSanctionDetails", AuthMap);
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
            SanctionMasterDAO dao = new SanctionMasterDAO();

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
        objSublimitDetailsrTO = null;
        objSanctionDetailsrTO = null;
        objClasficationDetailsTO = null;
        membertableDetails = null;
        loantableDetails = null;
        objLoanSlabDetailsTO = null;
    }
}