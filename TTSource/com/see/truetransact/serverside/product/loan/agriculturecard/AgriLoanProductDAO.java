/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriLoanProductDAO.java
 *
 * Created on December 8, 2003, 11:01 AM
 */
package com.see.truetransact.serverside.product.loan.agriculturecard;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.product.loan.agriculturecard.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;


// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.generalledger.AccountMaintenanceRule;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 *
 * @author rahul
 */
public class AgriLoanProductDAO extends TTDAO {

    static SqlMap sqlMap = null;
    //declaration of the TO obj in transferobject-->product-->loan
    private AgriLoanProductAccountTO loanProductAccountTO;
    private AgriLoanProductAccountParamTO loanProductAccountParamTO;
    private AgriLoanProductInterReceivableTO loanProductInterReceivableTO;
    private AgriLoanProductChargesTO loanProductChargesTO;
    private AgriLoanProductChargesTabTO loanProductChargesTabTO;
    private AgriLoanProductSpeItemsTO loanProductSpeItemsTO;
    private AgriLoanProductAccHeadTO loanProductAccHeadTO;
    private AgriLoanProductNonPerAssetsTO loanProductNonPerAssetsTO;
    private AgriLoanProductInterCalcTO loanProductInterCalcTO;
    private AgriLoanProductDocumentsTO loanProductDocumentsTO;
    private AgriLoanProductClassificationsTO loanProductClassificationsTO;
    private ArrayList loanProductchrgTabTO;
    private HashMap agriLoanProductInsuranceMap;
    private LinkedHashMap documentsTO = null;// Contains Only the Documents which the Status is not DELETED
    private LinkedHashMap deletedDocumentsTO = null;// Contains Only the Documents which the Status is DELETED
    private LinkedHashMap mainDocumentsTO = null;// Contains Both the Documents
    private final String STATUS_WITH_DELETE = "STATUS_WITH_DELETE";
    private final String STATUS_WITHOUT_DELETE = "STATUS_WITHOUT_DELETE";
    private String userID = "";
    RuleContext context;
    RuleEngine engine;
    private Date currDt = null;
    /**
     * Creates a new instance of LoanProductDAO
     */
    public AgriLoanProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();//To get the Sql map (LoanProductMap...
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductAccountTO", where);
        returnMap.put("AgriLoanProductAccountTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductAccountParamTO", where);
        returnMap.put("AgriLoanProductAccountParamTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductInterReceivableTO", where);
        returnMap.put("AgriLoanProductInterReceivableTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductChargesTO", where);
        returnMap.put("AgriLoanProductChargesTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductChargesTabTO", where);
        returnMap.put("AgriLoanProductChargesTabTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductSpeItemsTO", where);
        returnMap.put("AgriLoanProductSpeItemsTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductAccHeadTO", where);
        returnMap.put("AgriLoanProductAccHeadTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductNonPerAssetsTO", where);
        returnMap.put("AgriLoanProductNonPerAssetsTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductInterCalcTO", where);
        returnMap.put("AgriLoanProductInterCalcTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductDocumentsTO", where);
        returnMap.put("AgriLoanProductDocumentsTO", list);
        System.out.println("#### Document List in DAO : " + list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductClassificationsTO", where);
        returnMap.put("AgriLoanProductClassificationsTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectAgriLoanProductInsuranceTO", where);
        returnMap.put("AgriLoanProductInsuranceTO", list);
        System.out.println("#### Classification List in DAO : " + list);

        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            loanProductAccountTO.setCreatedBy(userID);
            loanProductAccountTO.setCreatedDt(currDt);
            loanProductAccountTO.setStatus(CommonConstants.STATUS_CREATED);
            loanProductAccountTO.setStatusDt(currDt);
            loanProductAccountTO.setStatusBy(userID);
            //            sqlMap.startTransaction();

            sqlMap.executeUpdate("insertAgriLoanProductAccountTO", loanProductAccountTO);
            objLogTO.setData(loanProductAccountTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAgriLoanProductAccountParamTO", loanProductAccountParamTO);
            objLogTO.setData(loanProductAccountParamTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountParamTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            sqlMap.executeUpdate("insertAgriLoanProductInterReceivableTO", loanProductInterReceivableTO);
            objLogTO.setData(loanProductInterReceivableTO.toString());
            objLogTO.setPrimaryKey(loanProductInterReceivableTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAgriLoanProductChargesTO", loanProductChargesTO);
            updateChargeMaintenance(loanProductChargesTO, loanProductAccountTO.getCommand());
            objLogTO.setData(loanProductChargesTO.toString());
            objLogTO.setPrimaryKey(loanProductChargesTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //sqlMap.executeUpdate ("insertLoanProductChargesTabTO", loanProductChargesTabTO);
            sqlMap.executeUpdate("insertAgriLoanProductSpeItemsTO", loanProductSpeItemsTO);
            objLogTO.setData(loanProductSpeItemsTO.toString());
            objLogTO.setPrimaryKey(loanProductSpeItemsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAgriLoanProductAccHeadTO", loanProductAccHeadTO);
            objLogTO.setData(loanProductAccHeadTO.toString());
            objLogTO.setPrimaryKey(loanProductAccHeadTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAgriLoanProductNonPerAssetsTO", loanProductNonPerAssetsTO);
            objLogTO.setData(loanProductNonPerAssetsTO.toString());
            objLogTO.setPrimaryKey(loanProductNonPerAssetsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("insertAgriLoanProductInterCalcTO", loanProductInterCalcTO);
            objLogTO.setData(loanProductInterCalcTO.toString());
            objLogTO.setPrimaryKey(loanProductInterCalcTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            insertDataTab(objLogDAO, objLogTO);
            //            sqlMap.commitTransaction();
            // To insert documents
            insertDocuments(objLogDAO, objLogTO);

            loanProductClassificationsTO.setStatus(CommonConstants.STATUS_CREATED);
            loanProductClassificationsTO.setStatusBy(userID);
            loanProductClassificationsTO.setStatusDt(currDt);

            sqlMap.executeUpdate("insertAgriLoanProductClassificationsTO", loanProductClassificationsTO);
            objLogTO.setData(loanProductClassificationsTO.toString());
            objLogTO.setPrimaryKey(loanProductClassificationsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            insertInsuranceDetails(objLogDAO, objLogTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void insertInsuranceDetails(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (agriLoanProductInsuranceMap != null && agriLoanProductInsuranceMap.size() > 0) {
            Iterator interator = agriLoanProductInsuranceMap.keySet().iterator();
            //            for(int i=0;i<agriLoanProductInsuranceMap.size();i++){
            String slno = "";
            while (interator.hasNext()) {
                slno = CommonUtil.convertObjToStr(interator.next());
                AgriLoanProductInsuranceTO agriLoanProductInsuranceTO = (AgriLoanProductInsuranceTO) agriLoanProductInsuranceMap.get(slno);
                objLogTO.setData(agriLoanProductInsuranceTO.toString());
                objLogTO.setPrimaryKey(agriLoanProductInsuranceTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
                System.out.println("agriLoanProductInsuranceTO  ###" + agriLoanProductInsuranceTO);
                if (agriLoanProductInsuranceTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    sqlMap.executeUpdate("insertAgriLoanProductInsuranceTO", agriLoanProductInsuranceTO);
                } else if (agriLoanProductInsuranceTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    sqlMap.executeUpdate("updateAgriLoanProductInsuranceTO", agriLoanProductInsuranceTO);
                } else if (agriLoanProductInsuranceTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    sqlMap.executeUpdate("updateAgriLoanProductInsuranceTO", agriLoanProductInsuranceTO);
                }
            }
        }
    }

    /**
     * To Insert Documents
     */
    private void insertDocuments(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (documentsTO != null) {
            for (int i = 1, j = documentsTO.size(); i <= j; i++) {
                AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.get(String.valueOf(i));
                loanProductDocumentsTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertAgriLoanProductDocumentsTO", loanProductDocumentsTO);
                objLogTO.setData(loanProductDocumentsTO.toString());
                objLogTO.setPrimaryKey(loanProductDocumentsTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
                loanProductDocumentsTO = null;
            }
        }
    }

    /**
     * To Delete Documents
     */
    private void deleteDocuments(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (documentsTO != null) {
            for (int i = 1, j = documentsTO.size(); i <= j; i++) {
                AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.get(String.valueOf(i));
                loanProductDocumentsTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteAgriLoanProductDocumentsTO", loanProductDocumentsTO);
                objLogTO.setData(loanProductDocumentsTO.toString());
                objLogTO.setPrimaryKey(loanProductDocumentsTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
                loanProductDocumentsTO = null;
            }
        }
    }

    /**
     * To Update Documents
     */
    private void updateDocuments(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (deletedDocumentsTO != null) {
            for (int i = 1, j = deletedDocumentsTO.size(); i <= j; i++) {
                AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) deletedDocumentsTO.get(String.valueOf(i));
                loanProductDocumentsTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteAgriLoanProductDocumentsTO", loanProductDocumentsTO);
                objLogTO.setData(loanProductDocumentsTO.toString());
                objLogTO.setPrimaryKey(loanProductDocumentsTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
                loanProductDocumentsTO = null;
            }
        }
        if (documentsTO != null) {
            for (int i = 1, j = documentsTO.size(); i <= j; i++) {
                AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.get(String.valueOf(i));
                if ((loanProductDocumentsTO.getStatus().length() > 0) && (loanProductDocumentsTO.getStatus() != null)) {
                    loanProductDocumentsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    sqlMap.executeUpdate("updateAgriLoanProductDocumentsTO", loanProductDocumentsTO);
                    objLogTO.setData(loanProductDocumentsTO.toString());
                    objLogTO.setPrimaryKey(loanProductDocumentsTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                    loanProductDocumentsTO = null;
                } else {
                    loanProductDocumentsTO.setStatus(CommonConstants.STATUS_CREATED);
                    sqlMap.executeUpdate("insertAgriLoanProductDocumentsTO", loanProductDocumentsTO);
                    objLogTO.setData(loanProductDocumentsTO.toString());
                    objLogTO.setPrimaryKey(loanProductDocumentsTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                    loanProductDocumentsTO = null;
                }
            }

        }
    }

    private void updateChargeMaintenance(AgriLoanProductChargesTO loanProductChargesTO, String log) throws Exception {
        System.out.println(log + "loansproductchargeto###" + loanProductChargesTO);
        HashMap map = new HashMap();
        map.put("CHECK_STATUS", "AUTH_STATUS");
        map.put("PROD_ID", loanProductChargesTO.getProdId());
        List allbranch = sqlMap.executeQueryForList("getAllBranches", "");
        if (allbranch != null && allbranch.size() > 0) {
            for (int i = 0; i < allbranch.size(); i++) {
                HashMap hash = (HashMap) allbranch.get(i);
                List loanproduct = sqlMap.executeQueryForList("getAgriLoansProduct", map);
                if (loanproduct != null && loanproduct.size() > 0) {
                    for (int j = 0; j < loanproduct.size(); j++) {
                        HashMap loanmap = (HashMap) loanproduct.get(j);
                        System.out.println(log + "loanmap####" + loanmap);
                        if (CommonUtil.convertObjToStr(loanmap.get("BEHAVES_LIKE")).equals("OD")) {

                            if (log.equals("INSERT")) {

                                map.put("APPLIED_DATE", currDt.clone());
                                map.put("PROD_TYPE", "OD");
                                map.put("PROD_ID", loanmap.get("PROD_ID"));
                                map.put("BRANCH_ID", hash.get("BRANCH_ID"));
                                map.put("CHARGE_TYPE", "FolioChargesTask");
                                map.put("LAST_CHARG_CALC_DT", loanProductChargesTO.getLastFolioChrgon());
                                map.put("NEXT_CHARG_CALC_DT", loanProductChargesTO.getNextFolioDuedate());
                                System.out.println("insertallcharges###" + map);
                                sqlMap.executeUpdate("insertallcharges", map);

                            }
                            if (log.equals("UPDATE")) {
                                map.put("PROD_ID", loanmap.get("PROD_ID"));
                                map.put("LAST_CHARG_CALC_DT", loanProductChargesTO.getLastFolioChrgon());
                                map.put("NEXT_CHARG_CALC_DT", loanProductChargesTO.getNextFolioDuedate());
                                map.put("BRANCH_ID", hash.get("BRANCH_ID"));
                                map.put("CHARGE_TYPE", "FolioChargesTask");
                                System.out.println("updateFolioCharges###" + map);
                                sqlMap.executeUpdate("updateFolioCharges", map);
                            }

                        }
                    }
                }
            }
        }
    }

    private void insertDataTab(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        for (int i = 0; i < loanProductchrgTabTO.size(); i++) {
            try {
                loanProductChargesTabTO = (AgriLoanProductChargesTabTO) loanProductchrgTabTO.get(i);
                sqlMap.executeUpdate("insertAgriLoanProductChargesTabTO", loanProductChargesTabTO);
                objLogTO.setData(loanProductChargesTabTO.toString());
                objLogTO.setPrimaryKey(loanProductChargesTabTO.getKeyData());
                objLogDAO.addToLog(objLogTO);

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            loanProductAccountTO.setStatus(CommonConstants.STATUS_MODIFIED);
            loanProductAccountTO.setStatusDt(currDt);
            loanProductAccountTO.setStatusBy(userID);
            //            sqlMap.startTransaction();

            sqlMap.executeUpdate("updateAgriLoanProductAccountTO", loanProductAccountTO);
            objLogTO.setData(loanProductAccountTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductAccountParamTO", loanProductAccountParamTO);
            objLogTO.setData(loanProductAccountParamTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountParamTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductInterReceivableTO", loanProductInterReceivableTO);
            objLogTO.setData(loanProductInterReceivableTO.toString());
            objLogTO.setPrimaryKey(loanProductInterReceivableTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductChargesTO", loanProductChargesTO);
            updateChargeMaintenance(loanProductChargesTO, loanProductAccountTO.getCommand());
            objLogTO.setData(loanProductChargesTO.toString());
            objLogTO.setPrimaryKey(loanProductChargesTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //sqlMap.executeUpdate ("updateLoanProductChargesTabTO", loanProductChargesTabTO);
            sqlMap.executeUpdate("updateAgriLoanProductSpeItemsTO", loanProductSpeItemsTO);
            objLogTO.setData(loanProductSpeItemsTO.toString());
            objLogTO.setPrimaryKey(loanProductSpeItemsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductAccHeadTO", loanProductAccHeadTO);
            objLogTO.setData(loanProductAccHeadTO.toString());
            objLogTO.setPrimaryKey(loanProductAccHeadTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductNonPerAssetsTO", loanProductNonPerAssetsTO);
            objLogTO.setData(loanProductNonPerAssetsTO.toString());
            objLogTO.setPrimaryKey(loanProductNonPerAssetsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.executeUpdate("updateAgriLoanProductInterCalcTO", loanProductInterCalcTO);
            objLogTO.setData(loanProductInterCalcTO.toString());
            objLogTO.setPrimaryKey(loanProductInterCalcTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            updateDataTab(objLogDAO, objLogTO);
            //            sqlMap.commitTransaction();

            // To update documents
            updateDocuments(objLogDAO, objLogTO);

            //            loanProductClassificationsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            loanProductClassificationsTO.setStatusBy(userID);
            loanProductClassificationsTO.setStatusDt(currDt);
            if (loanProductClassificationsTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                sqlMap.executeUpdate("updateAgriLoanProductClassificationsTO", loanProductClassificationsTO);
            } else {
                sqlMap.executeUpdate("insertAgriLoanProductClassificationsTO", loanProductClassificationsTO);
            }
            objLogTO.setData(loanProductClassificationsTO.toString());
            objLogTO.setPrimaryKey(loanProductClassificationsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            insertInsuranceDetails(objLogDAO, objLogTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateDataTab(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        if (loanProductchrgTabTO.size() > 0) {
            loanProductChargesTabTO = (AgriLoanProductChargesTabTO) loanProductchrgTabTO.get(0);
            sqlMap.executeUpdate("deleteAgriLoanProductChargesTabTO", loanProductChargesTabTO);
        }
        final int loanProductchrgTabTOSize = loanProductchrgTabTO.size();
        for (int i = 0; i < loanProductchrgTabTOSize; i++) {
            try {
                loanProductChargesTabTO = (AgriLoanProductChargesTabTO) loanProductchrgTabTO.get(i);
                sqlMap.executeUpdate("insertAgriLoanProductChargesTabTO", loanProductChargesTabTO);
                objLogTO.setData(loanProductChargesTabTO.toString());
                objLogTO.setPrimaryKey(loanProductChargesTabTO.getKeyData());
                objLogDAO.addToLog(objLogTO);

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            loanProductAccountTO.setStatus(CommonConstants.STATUS_DELETED);
            loanProductAccountTO.setStatusDt(currDt);
            loanProductAccountTO.setStatusBy(userID);
            //            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteAgriLoanProductAccountTO", loanProductAccountTO);
            objLogTO.setData(loanProductAccountTO.toString());
            objLogTO.setPrimaryKey(loanProductAccountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            // To delete Documents
            deleteDocuments(objLogDAO, objLogTO);


            loanProductClassificationsTO.setStatus(CommonConstants.STATUS_DELETED);
            loanProductClassificationsTO.setStatusBy(userID);
            loanProductClassificationsTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteAgriLoanProductClassificationsTO", loanProductClassificationsTO);
            objLogTO.setData(loanProductClassificationsTO.toString());
            objLogTO.setPrimaryKey(loanProductClassificationsTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            //            sqlMap.commitTransaction();
            insertInsuranceDetails(objLogDAO, objLogTO);


        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { LoanProductDAO dao = new
     * LoanProductDAO(); LoanProductAccountTO loanProductAccountTO = new
     * LoanProductAccountTO(); LoanProductAccountParamTO
     * loanProductAccountParamTO = new LoanProductAccountParamTO();
     * LoanProductInterReceivableTO loanProductInterReceivableTO = new
     * LoanProductInterReceivableTO(); LoanProductChargesTO loanProductChargesTO
     * = new LoanProductChargesTO(); LoanProductChargesTabTO
     * loanProductChargesTabTO = new LoanProductChargesTabTO();
     * LoanProductSpeItemsTO loanProductSpeItemsTO = new
     * LoanProductSpeItemsTO(); LoanProductAccHeadTO loanProductAccHeadTO = new
     * LoanProductAccHeadTO(); LoanProductNonPerAssetsTO
     * loanProductNonPerAssetsTO = new LoanProductNonPerAssetsTO();
     * LoanProductInterCalcTO loanProductInterCalcTO = new
     * LoanProductInterCalcTO();
     *
     * TOHeader toHeader = new TOHeader();//To tell what to do... Insert,
     * Update, Delete... toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * loanProductAccountTO.setTOHeader(toHeader);
     *
     * //----------ACCOUNTS---------- loanProductAccountTO.setProdId("RAHUL");
     * loanProductAccountTO.setProdDesc("UPDATE");
     * loanProductAccountTO.setAcctHead("RAHUL");
     * loanProductAccountTO.setBehavesLike("RAHUL");
     *
     * loanProductAccountParamTO.setProdId("A");
     * loanProductAccountParamTO.setNumberPattern("A");
     * loanProductAccountParamTO.setLastAcNo("A");
     * loanProductAccountParamTO.setLimitDefAllowed("A");
     * loanProductAccountParamTO.setStaffAcOpened("A");
     * loanProductAccountParamTO.setDebitIntClearingappl("A");
     * //----------INTEREST RECEIVABLE----------
     * loanProductInterReceivableTO.setProdId("A");
     * loanProductInterReceivableTO.setDebitIntCharged("A");
     * loanProductInterReceivableTO.setMinDebitintRate(new Double(12.0));
     * loanProductInterReceivableTO.setMaxDebitintRate (new Double(12.0));
     * loanProductInterReceivableTO.setMinDebitintAmt(new Double(12.0));
     * loanProductInterReceivableTO.setMaxDebitintAmt(new Double(12.0));
     * loanProductInterReceivableTO.setDebitintCalcFreq(new Double(180));
     * loanProductInterReceivableTO.setDebitintApplFreq(new Double(180));
     * loanProductInterReceivableTO.setDebitintCompFreq(new Double(180));
     * loanProductInterReceivableTO.setDebitProdRoundoff("A");
     * loanProductInterReceivableTO.setDebitIntRoundoff("A");
     * loanProductInterReceivableTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(null));
     * loanProductInterReceivableTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(null));
     * //loanProductInterReceivableTO.setNextIntapplDuedt(DateUtil.getDateMMDDYYYY(null));
     * loanProductInterReceivableTO.setProdFreq (new Double(180));
     * loanProductInterReceivableTO.setPlrRate(new Double(12));
     * loanProductInterReceivableTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(null));
     * loanProductInterReceivableTO.setPlrApplNewac("A");
     * loanProductInterReceivableTO.setPlrApplExistac("A");
     * loanProductInterReceivableTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(null));
     * loanProductInterReceivableTO.setPenalAppl("A");
     * loanProductInterReceivableTO.setLimitExpiryInt("A");
     * loanProductInterReceivableTO.setPenalIntRate(new Double(12));
     * loanProductInterReceivableTO.setExpoLmtPrudentialamt(new Double(12));
     * loanProductInterReceivableTO.setExpoLmtPolicyamt(new Double(12));
     * loanProductInterReceivableTO.setExpoLmtPrudentialper(new Double(12));
     * loanProductInterReceivableTO.setExpoLmtPolicyper(new Double(12));
     * //----------CHARGES---------- loanProductChargesTO.setProdId("A");
     * loanProductChargesTO.setAcClosingChrg(new Double(12));
     * loanProductChargesTO.setMiscServChrg(new Double(12));
     * loanProductChargesTO.setStatChrg("A");
     * loanProductChargesTO.setStatChrgRate(new Double(12));
     * loanProductChargesTO.setFolioChrgAppl("A");
     * loanProductChargesTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(null));
     * loanProductChargesTO.setNoEntriesPerFolio(new Double(12));
     * loanProductChargesTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(null));
     * loanProductChargesTO.setRatePerFolio(new Double(12));
     * loanProductChargesTO.setFolioChrgApplfreq(new Double(180));
     * loanProductChargesTO.setToCollectFoliochrg("A");
     * loanProductChargesTO.setToCollectChrgOn("A");
     * loanProductChargesTO.setIncompFolioRoundoff(new Double(15));
     * loanProductChargesTO.setProcChrg("A");
     * loanProductChargesTO.setProcChrgPer(new Double(12));
     * loanProductChargesTO.setProcChrgAmt(new Double(12));
     * loanProductChargesTO.setCommitChrg("A");
     * loanProductChargesTO.setCommitChrgPer(new Double(12));
     * loanProductChargesTO.setCommitChrgAmt(new Double(12));
     *
     * loanProductChargesTabTO.setProdId("A");
     * loanProductChargesTabTO.setChqReturnChrg("A");
     * loanProductChargesTabTO.setChqReturnChrgRate(new Double(12));
     * //----------SPECIAL ITEMS---------- loanProductSpeItemsTO.setProdId("A");
     * loanProductSpeItemsTO.setAtmCardIssued("A");
     * loanProductSpeItemsTO.setCrCardIssued("A");
     * loanProductSpeItemsTO.setMobileBankClient("A");
     * loanProductSpeItemsTO.setBranchBankingAllowed("A"); //----------ACCOUNT
     * HEAD---------- loanProductAccHeadTO.setProdId("A");
     * loanProductAccHeadTO.setAcClosingChrg("A");
     * loanProductAccHeadTO.setMiscServChrg("A");
     * loanProductAccHeadTO.setStatChrg("A");
     * loanProductAccHeadTO.setAcDebitInt("A");
     * loanProductAccHeadTO.setPenalInt("A");
     * loanProductAccHeadTO.setAcCreditInt("A");
     * loanProductAccHeadTO.setExpiryInt("A");
     * loanProductAccHeadTO.setChqRetChrgOutward("A");
     * loanProductAccHeadTO.setChqRetChrgInward("A");
     * loanProductAccHeadTO.setFolioChrgAc("A");
     * loanProductAccHeadTO.setCommitmentChrg("A");
     * loanProductAccHeadTO.setProcChrg("A");
     * loanProductAccHeadTO.setIntRecGl("A"); //----------NON-PERFORMING
     * ASSETS---------- loanProductNonPerAssetsTO.setProdId("A");
     * loanProductNonPerAssetsTO.setMinPeriodArrears(new Double(12));
     * loanProductNonPerAssetsTO.setPeriodTransSubstandard(new Double(12));
     * loanProductNonPerAssetsTO.setProvisionSubstandard("A");
     * loanProductNonPerAssetsTO.setPeriodTransDoubtful(new Double(12));
     * loanProductNonPerAssetsTO.setProvisionDoubtful("A");
     * loanProductNonPerAssetsTO.setLastIntAppldtCr(DateUtil.getDateMMDDYYYY(null));
     * loanProductNonPerAssetsTO.setPeriodTransLoss(new Double(12));
     * loanProductNonPerAssetsTO.setPeriodTransNoperforming(new Double(12));
     * //----------INTEREST CALCULATION----------
     * loanProductInterCalcTO.setProdId("A");
     * loanProductInterCalcTO.setCalcType("A");
     * loanProductInterCalcTO.setMinPeriod(new Double(180));
     * loanProductInterCalcTO.setMaxPeriod(new Double(180));
     * loanProductInterCalcTO.setMinAmtLoan(new Double(12));
     * loanProductInterCalcTO.setMaxAmtLoan(new Double(12));
     * loanProductInterCalcTO.setMinInterest(new Double(12));
     * loanProductInterCalcTO.setMaxInterest(new Double(12));
     * loanProductInterCalcTO.setMinIntDebit(new Double(12));
     * loanProductInterCalcTO.setSubsidy("A");
     * loanProductInterCalcTO.setLoanPeriodsMultiples(new Double(12));
     * //------------------------------------------------------- HashMap hash =
     * new HashMap(); hash.put("LoanProductAccountTO",loanProductAccountTO);
     * hash.put("LoanProductAccountParamTO",loanProductAccountParamTO);
     * hash.put("LoanProductInterReceivableTO",loanProductInterReceivableTO);
     * hash.put("LoanProductChargesTO",loanProductChargesTO);
     * hash.put("LoanProductChargesTabTO",loanProductChargesTabTO);
     * hash.put("LoanProductSpeItemsTO",loanProductSpeItemsTO);
     * hash.put("LoanProductAccHeadTO",loanProductAccHeadTO);
     * hash.put("LoanProductNonPerAssetsTO",loanProductNonPerAssetsTO);
     * hash.put("LoanProductInterCalcTO",loanProductInterCalcTO);
     *
     * System.out.println("Before execute"); dao.execute(hash);
     *
     * } catch (Exception ex) { ex.printStackTrace(); }
    }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        /*
         * To Verify The Account Head data...
         */
        System.out.println("Map in DAO: " + map);
        if (map.containsKey("ACCT_HD")) {
            verifyAccountHead(map);
        }

        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        /*
         * data fot the Normal operations like Insert, Update, and/or Delete...
         */
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        if (map.containsKey("LoanProductAccountTO")) {
            loanProductAccountTO = (AgriLoanProductAccountTO) map.get("LoanProductAccountTO");
            loanProductAccountParamTO = (AgriLoanProductAccountParamTO) map.get("LoanProductAccountParamTO");
            loanProductInterReceivableTO = (AgriLoanProductInterReceivableTO) map.get("LoanProductInterReceivableTO");
            loanProductChargesTO = (AgriLoanProductChargesTO) map.get("LoanProductChargesTO");
            //loanProductChargesTabTO = (LoanProductChargesTabTO) map.get("LoanProductChargesTabTO");
            loanProductchrgTabTO = (ArrayList) map.get("LoanProductChargesTabTO");
            loanProductSpeItemsTO = (AgriLoanProductSpeItemsTO) map.get("LoanProductSpeItemsTO");
            loanProductAccHeadTO = (AgriLoanProductAccHeadTO) map.get("LoanProductAccHeadTO");
            loanProductNonPerAssetsTO = (AgriLoanProductNonPerAssetsTO) map.get("LoanProductNonPerAssetsTO");
            loanProductInterCalcTO = (AgriLoanProductInterCalcTO) map.get("LoanProductInterCalcTO");
            loanProductClassificationsTO = (AgriLoanProductClassificationsTO) map.get("LoanProductClassificationsTO");
            agriLoanProductInsuranceMap = (HashMap) map.get("AgriLoanProductInsuranceTO");
            mainDocumentsTO = (LinkedHashMap) map.get("LoanProductDocumentsTO");
            documentsTO = (LinkedHashMap) mainDocumentsTO.get("STATUS_WITHOUT_DELETE");
            deletedDocumentsTO = (LinkedHashMap) mainDocumentsTO.get("STATUS_WITH_DELETE");


            final String command = loanProductAccountTO.getCommand();
            try {
                sqlMap.startTransaction();
                objLogTO.setStatus(command);

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }
                sqlMap.commitTransaction();

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            objLogDAO = null;
            objLogTO = null;
            destroyObjects();
        }
        return null;
    }

    private void verifyAccountHead(HashMap inputMap) throws Exception {
        System.out.println("In verifyAccountHead");
        engine = new RuleEngine();
        context = new RuleContext();
        context.addRule(new AccountMaintenanceRule());

        ArrayList list = (ArrayList) engine.validateAll(context, inputMap);
        if (list != null) {
            System.out.println("list in DAO: " + list);
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.generalledger.GeneralLedgerRuleHashMap");
            throw new TTException(exception);
            //sqlMap.rollbackTransaction();
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        loanProductAccountTO = null;
        loanProductAccountParamTO = null;
        loanProductInterReceivableTO = null;
        loanProductChargesTO = null;
        loanProductChargesTabTO = null;
        loanProductSpeItemsTO = null;
        loanProductAccHeadTO = null;
        loanProductNonPerAssetsTO = null;
        loanProductInterCalcTO = null;
        // Documents
        mainDocumentsTO = null;
        documentsTO = null;
        deletedDocumentsTO = null;

        loanProductClassificationsTO = null;
    }
}
