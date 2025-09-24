/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanDAO.java
 *
 * Created on Fri Jan 09 18:03:55 CST 2004
 */
package com.see.truetransact.serverside.bills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.cache.CacheModel;

import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.termloan.InterestDetailsValidationRule;
import com.see.truetransact.businessrule.termloan.SecurityDetailsValidationRule;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.AuthorizedSignatoryDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.powerofattorney.PowerOfAttorneyDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.bills.*;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.GregorianCalendar;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.deposit.lien.DepositLien;
import java.util.Date;
/**
 * TermLoan DAO.
 *
 * @author shanmugavel
 *
 */
public class TermLoanDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private TermLoanBorrowerTO objTermLoanBorrowerTO;
    private TermLoanCompanyTO objTermLoanCompanyTO;
    private TermLoanClassificationTO objTermLoanClassificationTO;
    private TermLoanOtherDetailsTO objTermLoanOtherDetailsTO;
    private AuthorizedSignatoryDAO objAuthorizedSignatoryDAO;
    private PowerOfAttorneyDAO objPowerOfAttorneyDAO;
    private HashMap jointAcctMap;
    // To update the networth details in Customer Table
    private HashMap netWorthDetailsMap;
    private HashMap authorizedMap;
    private HashMap poaMap;
    private HashMap sanctionMap;
    private HashMap sanctionFacilityMap;
    private HashMap facilityMap;
    private HashMap securityMap;
    private HashMap repaymentMap;
    private HashMap installmentMap;
    private LinkedHashMap installmentAllMap;
    private HashMap installmentSingleMap;
    private HashMap installmentMultIntMap;
    private HashMap guarantorMap;
    private HashMap documentMap;
    private HashMap interestMap;
    private HashMap NPA;
    private HashMap depositCustDetMap;
    private String borrower_No;
    private String acct_No;
    private String lienNo = "";
    public String prod_id = "";
    private final String ACCT_NUM = "ACCT_NUM";
    private final String ACT = "ACT";
    private final String AMOUNT = "AMOUNT";
    private final String AUTHORIZEDT = "AUTHORIZEDT";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String CATEGORY_ID = "CATEGORY_ID";
    private final String FROM_DATE = "FROM_DATE";
    private final String FULLY_SECURED = "FULLY_SECURED";
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String LIMIT = "LIMIT";
    private final String PROD_ID = "PROD_ID";
    private final String PROD = "PROD";
    private final String RULE_MAP_PATH = "com.see.truetransact.clientutil.exceptionhashmap.termloan.TermLoanRuleHashMap";
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";
    private final String TO_DATE = "TO_DATE";
    private final String VALUE = "value";
    private HashMap delRefMap = null;
    private Date currDt = null;
    /**
     * Creates a new instance of TermLoanDAO
     */
    public TermLoanDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        if (map.containsKey("NPA")) {
            System.out.println("mapNPA####@@@@@" + map);
            List lst = (List) sqlMap.executeQueryForList(CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME)), map);
            System.out.println("######od" + lst);
            if (lst.size() > 0) {
                returnMap.put("NPAProductList", lst);
            }
            return returnMap;
        } else {
            objAuthorizedSignatoryDAO = new AuthorizedSignatoryDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
            objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            //        if(map.containsKey("ALL_EMI_RECORDS"))
            //        {
            //        List lst=(List)map.get("ALL_EMI_RECORDS");
            //        if(lst.size()>0){
            //            for(int i=0;i<lst.size();i++){
            //              List list=(List)lst.get(i);
            //              System.out.println("listonly"+list);
            //              HashMap insert=new HashMap();
            //                      insert.put("INSTALLMENT_NO",list.get(0));
            //                      insert.put("ACT_NUM","A124");
            //                      insert.put("DATE",list.get(1));
            //                      insert.put("PRINCIPLE",list.get(2));
            //                      insert.put("INTEREST_AMOUNT",list.get(3));
            //                      insert.put("TOTAL",list.get(4));
            //                      insert.put("BALANCE",list.get(5));
            ////                      insert.put("PAID_DATE",list.get(7));
            ////                      insert.put("PAID_TOTAL_AMOUNT",list.get(8));
            //                      try{
            //                          sqlMap.executeUpdate("emiInstalmentshedule", insert);
            //                      }
            //                      catch(Exception e)
            //                      {
            //                          sqlMap.rollbackTransaction();
            //                      }
            //                      }
            //
            //            }
            //        }
            if (map.containsKey("KEY_VALUE")) {
                returnMap.put("KEY_VALUE", map.get("KEY_VALUE"));
            } else {
                // Useful in the Client side based on the Account Number
                // for authorization
                returnMap.put("KEY_VALUE", "AUTHORIZE");
            }
            if (map.containsKey("KEY_VALUE")) {
                if (returnMap.get("KEY_VALUE").equals("BORROWER_NUMBER")) {
                    // To retrieve the values based on Borrower Number
                    returnMap = executeQueryForBorrNo(returnMap, where);
                } else if (returnMap.get("KEY_VALUE").equals("ACCOUNT_NUMBER")) {
                    // To retrieve the values based on Account Number
                    returnMap = executeQueryForAcctNo(returnMap, where);
                }
            } else if (returnMap.get("KEY_VALUE").equals("AUTHORIZE")) {
                // To retrieve the values based on Account Number(To populate UI
                // at the time of Authorization)
                returnMap = executeQueryForAuthorize(returnMap, where, CommonUtil.convertObjToStr(map.get("BORROW_NO")));
            }
            map = null;
            where = null;
            ServiceLocator.flushCache(sqlMap);
            objAuthorizedSignatoryDAO = null;
            objPowerOfAttorneyDAO = null;
            return returnMap;
        }
    }

    private HashMap executeQueryForBorrNo(HashMap returnMap, String where) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectBillsJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsCompanyTO", where);
        returnMap.put("TermLoanCompanyTO", list);
        list = null;

        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);

        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;

        return returnMap;
    }

    private HashMap executeQueryForAcctNo(HashMap returnMap, String where) throws Exception {

        List list = (List) sqlMap.executeQueryForList("getSelectBillsSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsGuarantorTO", where);
        returnMap.put("TermLoanGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsOtherDetailsTO", where);
        returnMap.put("TermLoanOtherDetailsTO", list);
        list = null;

        return returnMap;
    }

    private HashMap executeQueryForAuthorize(HashMap returnMap, String where, String borrow_no) throws Exception {

        List list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanGuarantorTO", where);
        returnMap.put("TermLoanGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsOtherDetailsTO", where);
        returnMap.put("TermLoanOtherDetailsTO", list);
        list = null;

        // set the Borrower number to where
        where = borrow_no;
        list = (List) sqlMap.executeQueryForList("getSelectBillsJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsCompanyTO", where);
        returnMap.put("TermLoanCompanyTO", list);
        list = null;

        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);

        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectBillsFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;

        return returnMap;
    }

    private List getInterestDetails(String where, HashMap returnMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getBillsInterestDetailsWhereConditions", where);
        HashMap whereConditionMap = new HashMap();
        HashMap finalMap = new HashMap();
        String strIntGetFrom = "";
        whereConditionMap.put(CATEGORY_ID, "");
        whereConditionMap.put(AMOUNT, "");
        whereConditionMap.put(PROD_ID, "");
        whereConditionMap.put(FROM_DATE, null);
        whereConditionMap.put(TO_DATE, null);
        if (list != null && list.size() > 0) {
            finalMap = (HashMap) list.get(0);
            whereConditionMap.put(CATEGORY_ID, CommonUtil.convertObjToStr(finalMap.get("CATEGORY")));
            whereConditionMap.put(AMOUNT, new java.math.BigDecimal(CommonUtil.convertObjToDouble(finalMap.get("LIMIT")).doubleValue()));
            whereConditionMap.put(PROD_ID, CommonUtil.convertObjToStr(finalMap.get("PROD_ID")));
            whereConditionMap.put(FROM_DATE, finalMap.get("FROM_DATE"));
            whereConditionMap.put(TO_DATE, finalMap.get("TO_DATE"));
            strIntGetFrom = CommonUtil.convertObjToStr(finalMap.get("INT_GET_FROM"));
        }
        list = null;
        list = new ArrayList();
        if (strIntGetFrom.equals(PROD)) {
            list = (List) sqlMap.executeQueryForList("getSelectProductBillsInterestTO", whereConditionMap);
        } else if (strIntGetFrom.equals(ACT)) {
            list = (List) sqlMap.executeQueryForList("getSelectBillsInterestTO", where);
        }
        whereConditionMap = null;
        finalMap = null;
        strIntGetFrom = null;
        return list;
    }

    private void executeAllTabQuery() throws Exception {
        System.out.println("$#$# jointAcctMap : " + jointAcctMap);
        executeJointAcctTabQuery();
        objAuthorizedSignatoryDAO.executeAuthorizedTabQuery(logTO, logDAO, sqlMap);
        objPowerOfAttorneyDAO.executePoATabQuery(logTO, logDAO, sqlMap);
        executeSanctionTabQuery();
        executeSanctionDetailsTabQuery();
        executeFacilityTabQuery();
        executeSecurityTabQuery();
        executeRepaymentQuery();
        executeInstallmentTabQuery();
        executeInstallmentMultIntTabQuery();
        executeGuarantorTabQuery();
        executeDocumentTabQuery();
        executeInterestTabQuery();
        executeClassificationTabQuery();
        executeOtherDetailsTabQuery();
    }

    private void insertData() throws Exception {
        try {
            borrower_No = getBorrower_No();
            objTermLoanBorrowerTO.setBorrowNo(borrower_No);
            objTermLoanCompanyTO.setBorrowNo(borrower_No);
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);

            sqlMap.startTransaction();

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("insertBillsBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

            logTO.setData(objTermLoanCompanyTO.toString());
            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
            logTO.setStatus(objTermLoanCompanyTO.getCommand());
            executeUpdate("insertBillsCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);

            executeAllTabQuery();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void setBorrower_No(String borrower_No) {
        objAuthorizedSignatoryDAO.setBorrower_No(borrower_No);
        objPowerOfAttorneyDAO.setBorrower_No(borrower_No);
        this.borrower_No = borrower_No;
        objAuthorizedSignatoryDAO.setCommand(objTermLoanBorrowerTO.getCommand());
        objPowerOfAttorneyDAO.setCommand(objTermLoanBorrowerTO.getCommand());
    }

    private String getBorrower_No() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BORROWER_NO");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");
        String strBorrower_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        setBorrower_No(strBorrower_No);
        return strBorrower_No;
    }

    private void setAccount_No(String acct_No) {
        this.acct_No = acct_No;
    }

    private String getAccount_No() throws Exception {
//        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE, "LOAN.ACCOUNT_NO");
//        String strAcct_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
//        where = null;
//        dao = null;
//        return strAcct_No;
//        HashMap map = new HashMap();
//        int len=10;
//        map.put("PROD_ID", prod_id);
//        List list = (List) sqlMap.executeQueryForList("getNextActNumForLoan", map);
//        System.out.println("@@@@list"+list);
//        if(list!=null && list.size()>0)
//        {
//            where = (HashMap)list.get(0);
//        }
//        String prefix = CommonUtil.convertObjToStr( where.get("NUMBER_PATTERN"));
//        String lastacno = CommonUtil.convertObjToStr( where.get("LAST_AC_NO"));
//         int numFrom = prefix.trim().length();
//         String id = "";
//        if(lastacno.equals("0")){
//          id = CommonUtil.convertObjToStr( where.get("NUMBER_PATTERN_SUFFIX"));  
//        }else{
//          id = String.valueOf(Integer.parseInt(String.valueOf( where.get("LAST_AC_NO")))+1);  
//        }          
//                String genID = prefix.toUpperCase() + CommonUtil.lpad(id, len - numFrom, '0');
//                sqlMap.executeUpdate("updateNextIdForLoan", map);
//                return genID;

        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        int len = 10;
        where.put("PROD_ID", prod_id);
        List lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
        }
        if (mapData.containsKey("PREFIX")) {
            strPrefix = (String) mapData.get("PREFIX");
        }
        int numFrom = strPrefix.trim().length();
        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
        System.out.println("@@@@@@@@" + newID);
        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
        System.out.println("@@@@@@@@" + nxtID);
        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
        where.put("VALUE", nxtID);
        sqlMap.executeUpdate("updateNextIdForLoan", where);
        return genID;

    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            setBorrower_No(objTermLoanBorrowerTO.getBorrowNo());
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("updateBillsBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

            logTO.setData(objTermLoanCompanyTO.toString());
            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
            logTO.setStatus(objTermLoanCompanyTO.getCommand());
            executeUpdate("updateBillsCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);

            executeAllTabQuery();
            /* add for npa status changes  */
            if (NPA != null && NPA.size() > 0 && CommonUtil.convertObjToStr(NPA.get("MODE")).equals("UPDATE")) {
                updateAssetChanges();
            }

            /*    npa finish  */
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    void updateAssetChanges() {
        if (NPA != null) {

            System.out.println("check npa@@@@###" + NPA);
            try {
                sqlMap.executeUpdate("NPAHISTORY_DAO", NPA);
            } catch (Exception e) {
                e.printStackTrace();
            }
            NPA = null;
        }

    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            setBorrower_No(objTermLoanBorrowerTO.getBorrowNo());
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("deleteTermLoanBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

            logTO.setData(objTermLoanCompanyTO.toString());
            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
            logTO.setStatus(objTermLoanCompanyTO.getCommand());
            executeUpdate("deleteBillsCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);

            executeAllTabQuery();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeJointAcctTabQuery() throws Exception {
        TermLoanJointAcctTO objTermLoanJointAcctTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = jointAcctMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanJointAcctTO from the jointAcctMap
            for (int i = jointAcctMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanJointAcctTO = (TermLoanJointAcctTO) jointAcctMap.get(objKeySet[j]);
                objTermLoanJointAcctTO.setBorrowNo(borrower_No);
                if (objTermLoanBorrowerTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    objTermLoanJointAcctTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_CREATED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                    executeUpdate("insertBillsJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                } else if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_MODIFIED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                    executeUpdate("updateTermLoanJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                } else if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_DELETED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
                    executeUpdate("deleteTermLoanJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                }
                objTermLoanJointAcctTO = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanJointAcctTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    //for remove LTD Lien

    private void lienCancel(HashMap lienCancel) throws Exception {
        HashMap hash = new HashMap();
        System.out.println("lienCancelmap#####" + lienCancel);
        HashMap behaveMap = (HashMap) ((List) sqlMap.executeQueryForList("getLoansProduct", lienCancel)).get(0);
        if (CommonUtil.convertObjToStr(behaveMap.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")) {
            HashMap lienCancelMap = new HashMap();
            String authStatus = "";

            List lst = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancel);
            System.out.println("getDepositLienUnlienLoanTO##" + lst);
            if (lst != null && lst.size() > 0) {
                DepositLienTO depLienTO = (DepositLienTO) lst.get(0);
                lienCancelMap.put("DEPOSIT_ACT_NUM", depLienTO.getDepositNo());
                lienCancelMap.put("SUBNO", CommonUtil.convertObjToInt(depLienTO.getDepositSubNo()));
                lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                lienCancelMap.put("BALANCE", depLienTO.getLienAmount());
                lienCancelMap.put("AMOUNT", depLienTO.getLienAmount());
                lienCancelMap.put("LIENNO", depLienTO.getLienNo());
                lienCancelMap.put("AUTHORIZEDT", currDt.clone());
                lienCancelMap.put("AUTHORIZE_DATE", currDt.clone());
                lienCancelMap.put("COMMAND_STATUS", "CREATED");
                lienCancelMap.put("STATUS", "CREATED");
                DepositLienDAO depositLiendao = new DepositLienDAO();
                lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                lienCancelMap.put("ACTION", CommonConstants.STATUS_AUTHORIZED);
                lienCancelMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                lienCancelMap.put("LIENAMOUNT", depLienTO.getLienAmount());
                lienCancelMap.put("STATUS", "UNLIENED");
                lienCancelMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                depositLiendao.setCallFromOtherDAO(true);
                System.out.println("lienCancelbefore dao" + lienCancelMap);
                depositLiendao.execute(lienCancelMap);

            }
        }
    }

    private void executeSanctionTabQuery() throws Exception {
        TermLoanSanctionTO objTermLoanSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = sanctionMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanSanctionTO from the sanctionMap
            for (int i = sanctionMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanSanctionTO = (TermLoanSanctionTO) sanctionMap.get(objKeySet[j]);
                objTermLoanSanctionTO.setBorrowNo(borrower_No);
                logTO.setData(objTermLoanSanctionTO.toString());
                logTO.setPrimaryKey(objTermLoanSanctionTO.getKeyData());
                logTO.setStatus(objTermLoanSanctionTO.getCommand());
                executeOneTabQueries("BillsSanctionTO", objTermLoanSanctionTO);
                logDAO.addToLog(logTO);
                objTermLoanSanctionTO = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanSanctionTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeSanctionDetailsTabQuery() throws Exception {
        TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = sanctionFacilityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanSanctionFacilityTO from the sanctionFacilityMap
            for (int i = sanctionFacilityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) sanctionFacilityMap.get(objKeySet[j]);
                objTermLoanSanctionFacilityTO.setBorrowNo(borrower_No);
                if (objTermLoanSanctionFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    delRefMap.put(objTermLoanSanctionFacilityTO.getSanctionNo() + objTermLoanSanctionFacilityTO.getSlNo(), "");
                }
                logTO.setData(objTermLoanSanctionFacilityTO.toString());
                logTO.setPrimaryKey(objTermLoanSanctionFacilityTO.getKeyData());
                logTO.setStatus(objTermLoanSanctionFacilityTO.getCommand());
                executeOneTabQueries("BillsSanctionFacilityTO", objTermLoanSanctionFacilityTO);
                logDAO.addToLog(logTO);
                objTermLoanSanctionFacilityTO = null;
            }
            System.out.println("#$#$# inside executeSanctionDetailsTabQuery() delRefMap : " + delRefMap);
            keySet = null;
            objKeySet = null;
            objTermLoanSanctionFacilityTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeFacilityTabQuery() throws Exception {
        TermLoanFacilityTO objTermLoanFacilityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = facilityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanFacilityTO from the facilityMap
            for (int i = facilityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanFacilityTO = (TermLoanFacilityTO) facilityMap.get(objKeySet[j]);
                objTermLoanFacilityTO.setBorrowNo(borrower_No);
                if (delRefMap.containsKey(objTermLoanFacilityTO.getSanctionNo() + objTermLoanFacilityTO.getSlNo())) {
                    delRefMap.put(objTermLoanFacilityTO.getAcctNum(), "");
                    objTermLoanFacilityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    prod_id = objTermLoanFacilityTO.getProdId();
                    acct_No = getAccount_No();
                    objTermLoanFacilityTO.setAcctNum(acct_No);
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("insertBillsFacilityTO", objTermLoanFacilityTO);
                    logDAO.addToLog(logTO);
                } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    System.out.println("objTermLoanFacilityTO#####" + objTermLoanFacilityTO);
                    if (objTermLoanFacilityTO.getAuthorizeStatus1() != null && objTermLoanFacilityTO.getAuthorizeStatus1().length() > 0) {
                        executeUpdate("updateBillsFacilityTOMaterializedView", objTermLoanFacilityTO);//dont want to updte available balance
                    } else {
                        executeUpdate("updateBillsFacilityTO", objTermLoanFacilityTO);
                    }
                    logDAO.addToLog(logTO);
                } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteBillsFacilityTO", objTermLoanFacilityTO);
                    logDAO.addToLog(logTO);
                    HashMap lienCancelMap = new HashMap();
                    lienCancelMap.put("LOAN_NO", objTermLoanFacilityTO.getAcctNum());
                    lienCancelMap.put("PROD_ID", objTermLoanFacilityTO.getProdId());
                    lienCancel(lienCancelMap);
                }
                // To make Lien Marking for LTD loans
                if (depositCustDetMap != null) {
                    if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        DepositLienTO obj = new DepositLienTO();
                        HashMap prodMap = new HashMap();
                        prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                        if (lst.size() > 0) {
                            prodMap = (HashMap) lst.get(0);
                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                        }
                        prodMap = null;
                        obj.setLienAcNo(objTermLoanFacilityTO.getAcctNum());
                        double availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();
                        availBal = (availBal / 85.0 * 100.0);
                        obj.setLienAmount(new Double(availBal));
                        obj.setLienDt(currDt);
                        obj.setRemarks("Lien for LTD");
                        obj.setCreditLienAcct("NA");
                        obj.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
                        obj.setDepositSubNo(CommonUtil.convertObjToInt(depositCustDetMap.get("DEPOSIT_SUB_NO")));
                        obj.setLienProdId(objTermLoanFacilityTO.getProdId());
                        obj.setLienNo("-");
                        obj.setStatus(objTermLoanFacilityTO.getStatus());
                        obj.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                        obj.setStatusDt(objTermLoanFacilityTO.getStatusDt());

                        ArrayList lienTOs = new ArrayList();
                        lienTOs.add(obj);
                        HashMap objHashMap = new HashMap();
                        objHashMap.put("lienTOs", lienTOs);
                        objHashMap.put("SHADOWLIEN", new Double(availBal));
                        objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        System.out.println("### objHashMap : " + objHashMap);
                        DepositLienDAO depLienDAO = new DepositLienDAO();
                        depLienDAO.setCallFromOtherDAO(true);
                        objHashMap = depLienDAO.execute(objHashMap);
                        if (objHashMap != null) {
                            lienNo = CommonUtil.convertObjToStr(objHashMap.get("LIENNO"));
                        }
                        obj = null;
                        lienTOs = null;
                        depLienDAO = null;
                        objHashMap = null;
                    }
                }
                objTermLoanFacilityTO = null;
            }
            System.out.println("#$#$# inside executeFacilityTabQuery() delRefMap : " + delRefMap);
            depositCustDetMap = null;
            keySet = null;
            objKeySet = null;
            objTermLoanFacilityTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeSecurityTabQuery() throws Exception {
        TermLoanSecurityTO objTermLoanSecurityTO;
        Set keySet;
        Object[] objKeySet;
        String strCustSecurityKey = "";
        try {
            HashMap oldAvailSecAmtMap = (HashMap) securityMap.remove("OLD_ELIGIBLE_LOAN_AMT");
            HashMap whereAvailSecAmtMap;
            keySet = securityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            double eligibleAmt = 0.0;

            // To retrieve the TermLoanSecurityTO from the securityMap
            for (int i = securityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                whereAvailSecAmtMap = new HashMap();
                objTermLoanSecurityTO = (TermLoanSecurityTO) securityMap.get(objKeySet[j]);
                if (delRefMap.containsKey(objTermLoanSecurityTO.getAcctNum())) {
                    objTermLoanSecurityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanSecurityTO.toString());
                logTO.setPrimaryKey(objTermLoanSecurityTO.getKeyData());
                logTO.setStatus(objTermLoanSecurityTO.getCommand());
                executeOneTabQueries("BillsSecurityTO", objTermLoanSecurityTO);

                whereAvailSecAmtMap.put("CUST_ID", objTermLoanSecurityTO.getCustId());
                whereAvailSecAmtMap.put("SECURITY_NO", objTermLoanSecurityTO.getSecurityNo());

                strCustSecurityKey = getEligibleLoanAmtKey(objTermLoanSecurityTO.getCustId(), CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
                eligibleAmt = 0.0;
                if (oldAvailSecAmtMap.containsKey(strCustSecurityKey)) {
                    if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        double eligibleOldLoanAmt = CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue();
                        double currentEligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
                        eligibleAmt = currentEligibleAmt - eligibleOldLoanAmt;
                    } else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        eligibleAmt = (-1 * CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue());
                    }
                } else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    eligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
                }

                whereAvailSecAmtMap.put("AVAILABLE_SECURITY_VALUE", new java.math.BigDecimal(eligibleAmt));

                if (eligibleAmt != 0.0) {
                    sqlMap.executeUpdate("updateCustSecurityAvailableAmt", whereAvailSecAmtMap);
                }

                logDAO.addToLog(logTO);
                objTermLoanSecurityTO = null;
                strCustSecurityKey = null;
                whereAvailSecAmtMap = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanSecurityTO = null;
            oldAvailSecAmtMap = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getEligibleLoanAmtKey(String strCustID, String strSecurityNo) throws Exception {
        return strCustID + "#" + strSecurityNo;
    }

    private void executeRepaymentQuery() throws Exception {
        TermLoanRepaymentTO objTermLoanRepaymentTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = repaymentMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            HashMap repayDetailsMap;
            // To retrieve the TermLoanRepaymentTO from the repaymentMap
            for (int i = repaymentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanRepaymentTO = (TermLoanRepaymentTO) repaymentMap.get(objKeySet[j]);
                if (delRefMap.containsKey(objTermLoanRepaymentTO.getAcctNum())) {
                    objTermLoanRepaymentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanRepaymentTO.setStatus(CommonConstants.STATUS_DELETED);
                    delRefMap.put("DELACTNUM", objTermLoanRepaymentTO.getAcctNum());
                    delRefMap.put("DELSCHNUM", objTermLoanRepaymentTO.getScheduleNo());
                }
                logTO.setData(objTermLoanRepaymentTO.toString());
                logTO.setPrimaryKey(objTermLoanRepaymentTO.getKeyData());
                logTO.setStatus(objTermLoanRepaymentTO.getCommand());
                executeOneTabQueries("TermLoanRepaymentTO", objTermLoanRepaymentTO);
                logDAO.addToLog(logTO);
                System.out.println("repaymentmap" + repaymentMap);
                //                if (objTermLoanRepaymentTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                //                    repayDetailsMap = new HashMap();
                //                    repayDetailsMap.put("DISBURSEMENT_ID", objTermLoanRepaymentTO.getDisbursementId());
                //                    repayDetailsMap.put("ACT_NUM", objTermLoanRepaymentTO.getAcctNum());
                //                    logTO.setPrimaryKey(repayDetailsMap.toString());
                //                    repayDetailsMap.put("REPAYMENT_SCHEDULE_NO", objTermLoanRepaymentTO.getScheduleNo());
                //                    logTO.setData(repayDetailsMap.toString());
                //                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                //                    sqlMap.executeUpdate("updateRepayScheduleNoInLoansDisbursement", repayDetailsMap);
                //                }
                //                for (i=0; i<installmentAllMap.size(); i++) {
                //                    installmentSingleMap = (HashMap) installmentAllMap.get(String.valueOf(i));
                //                    System.out.println("allrecords"+installmentSingleMap);
                //                    installmentSingleMap.put("ACT_NUM", objTermLoanRepaymentTO.getAcctNum());
                //                    installmentSingleMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                //                    System.out.println("singlerecor"+installmentSingleMap);
                //                    executeUpdate("TermRepaymentInstallmentAllTO", installmentSingleMap);
                //                    installmentSingleMap = null;
                //                }
                installmentAllMap = null;
                objTermLoanRepaymentTO = null;
                repayDetailsMap = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanRepaymentTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);

        }

    }

    private void executeInstallmentTabQuery() throws Exception {
        TermLoanInstallmentTO objTermLoanInstallmentTO;
        try {
            if (installmentMap != null && installmentMap.size() > 0) {
                Set keySet;
                Object[] objKeySet;
                keySet = installmentMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanInstallmentTO from the installmentMap
                for (int i = installmentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstallmentTO = (TermLoanInstallmentTO) installmentMap.get(objKeySet[j]);
                    if (delRefMap.containsKey(objTermLoanInstallmentTO.getAcctNum())) {
                        objTermLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                    logTO.setData(objTermLoanInstallmentTO.toString());
                    logTO.setPrimaryKey(objTermLoanInstallmentTO.getKeyData());
                    logTO.setStatus(objTermLoanInstallmentTO.getCommand());
                    System.out.println("objTermLoanOtherDetailsTO" + objTermLoanOtherDetailsTO);
                    executeOneTabQueries("TermLoanInstallmentTO", objTermLoanInstallmentTO);
                    logDAO.addToLog(logTO);
                    objTermLoanInstallmentTO = null;
                }
                keySet = null;
                objKeySet = null;
            } else {
                if (delRefMap != null && delRefMap.size() > 0 && delRefMap.containsKey("DELACTNUM")) {
                    List lst = sqlMap.executeQueryForList("getSelectTermLoanInstallmentTO", delRefMap);
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            objTermLoanInstallmentTO = (TermLoanInstallmentTO) lst.get(i);
                            objTermLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                            objTermLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
                            logTO.setData(objTermLoanInstallmentTO.toString());
                            logTO.setPrimaryKey(objTermLoanInstallmentTO.getKeyData());
                            logTO.setStatus(objTermLoanInstallmentTO.getCommand());
                            System.out.println("objTermLoanOtherDetailsTO" + objTermLoanOtherDetailsTO);
                            executeOneTabQueries("TermLoanInstallmentTO", objTermLoanInstallmentTO);
                            logDAO.addToLog(logTO);
                            objTermLoanInstallmentTO = null;
                        }
                    }
                    lst = null;
                }
            }
            objTermLoanInstallmentTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeInstallmentMultIntTabQuery() throws Exception {
        TermLoanInstallMultIntTO objTermLoanInstallMultIntTO;
        Set keySetMultInt;
        Object[] objKeySetMultInt;
        try {
            keySetMultInt = installmentMultIntMap.keySet();
            objKeySetMultInt = (Object[]) keySetMultInt.toArray();
            // To retrieve the TermLoanInstallMultIntTO from the installmentMultIntMap
            for (int i = installmentMultIntMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanInstallMultIntTO = (TermLoanInstallMultIntTO) installmentMultIntMap.get(objKeySetMultInt[j]);
                //                if (delRefMap.containsKey(objTermLoanInstallMultIntTO.getAcctNum())) {
                //                    objTermLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                //                    objTermLoanInstallMultIntTO.setStatus(CommonConstants.STATUS_DELETED);
                //                }
                logTO.setData(objTermLoanInstallMultIntTO.toString());
                logTO.setPrimaryKey(objTermLoanInstallMultIntTO.getKeyData());
                logTO.setStatus(objTermLoanInstallMultIntTO.getCommand());
                executeOneTabQueries("TermLoanInstallMultIntTO", objTermLoanInstallMultIntTO);
                logDAO.addToLog(logTO);
                objTermLoanInstallMultIntTO = null;
            }
            objTermLoanInstallMultIntTO = null;
            keySetMultInt = null;
            objKeySetMultInt = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeGuarantorTabQuery() throws Exception {
        TermLoanGuarantorTO objTermLoanGuarantorTO;
        Set keySet = guarantorMap.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        try {
            keySet = guarantorMap.keySet();
            objKeySet = (Object[]) keySet.toArray();

            // To retrieve the TermLoanGuarantorTO from the guarantorMap
            for (int i = guarantorMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanGuarantorTO = (TermLoanGuarantorTO) guarantorMap.get(objKeySet[j]);
                if (delRefMap.containsKey(objTermLoanGuarantorTO.getAcctNum())) {
                    objTermLoanGuarantorTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanGuarantorTO.toString());
                logTO.setPrimaryKey(objTermLoanGuarantorTO.getKeyData());
                logTO.setStatus(objTermLoanGuarantorTO.getCommand());
                executeOneTabQueries("BillsGuarantorTO", objTermLoanGuarantorTO);
                logDAO.addToLog(logTO);
                objTermLoanGuarantorTO = null;
            }
            objTermLoanGuarantorTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeDocumentTabQuery() throws Exception {
        TermLoanDocumentTO objTermLoanDocumentTO;
        Set keySet = documentMap.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        try {
            keySet = documentMap.keySet();
            objKeySet = (Object[]) keySet.toArray();

            // To retrieve the TermLoanDocumentTO from the documentMap
            for (int i = documentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanDocumentTO = (TermLoanDocumentTO) documentMap.get(objKeySet[j]);
                //                if (delRefMap.containsKey(objTermLoanDocumentTO.getAcctNo())) {
                //                    objTermLoanDocumentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                //                    objTermLoanDocumentTO.setStatus(CommonConstants.STATUS_DELETED);
                //                }
                logTO.setData(objTermLoanDocumentTO.toString());
                logTO.setPrimaryKey(objTermLoanDocumentTO.getKeyData());
                logTO.setStatus(objTermLoanDocumentTO.getCommand());
                executeOneTabQueries("BillsDocumentTO", objTermLoanDocumentTO);
                logDAO.addToLog(logTO);
                objTermLoanDocumentTO = null;
            }
            objTermLoanDocumentTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeInterestTabQuery() throws Exception {
        TermLoanInterestTO objTermLoanInterestTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = interestMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanInterestTO from the interestMap
            for (int i = interestMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanInterestTO = (TermLoanInterestTO) interestMap.get(objKeySet[j]);
                if (delRefMap.containsKey(objTermLoanInterestTO.getAcctNum())) {
                    objTermLoanInterestTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanInterestTO.toString());
                logTO.setPrimaryKey(objTermLoanInterestTO.getKeyData());
                logTO.setStatus(objTermLoanInterestTO.getCommand());
                executeOneTabQueries("BillsInterestTO", objTermLoanInterestTO);
                logDAO.addToLog(logTO);
                objTermLoanInterestTO = null;
            }
            objTermLoanInterestTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeClassificationTabQuery() throws Exception {
        try {
            if (objTermLoanClassificationTO != null && ((this.acct_No != null && this.acct_No.length() > 0) || objTermLoanClassificationTO.getAcctNum().length() > 0)) {
                if (objTermLoanClassificationTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objTermLoanClassificationTO.setAcctNum(this.acct_No);
                }
                if (delRefMap.containsKey(objTermLoanClassificationTO.getAcctNum())) {
                    objTermLoanClassificationTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanClassificationTO.toString());
                logTO.setPrimaryKey(objTermLoanClassificationTO.getKeyData());
                logTO.setStatus(objTermLoanClassificationTO.getCommand());
                executeOneTabQueries("BillsClassificationTO", objTermLoanClassificationTO);
                logDAO.addToLog(logTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeOtherDetailsTabQuery() throws Exception {
        if (objTermLoanOtherDetailsTO != null && ((this.acct_No != null && this.acct_No.length() > 0) || objTermLoanOtherDetailsTO.getActNum().length() > 0)) {
            if (objTermLoanOtherDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                objTermLoanOtherDetailsTO.setActNum(this.acct_No);
            }
            if (delRefMap.containsKey(objTermLoanOtherDetailsTO.getActNum())) {
                objTermLoanOtherDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            logTO.setData(objTermLoanOtherDetailsTO.toString());
            logTO.setPrimaryKey(objTermLoanOtherDetailsTO.getKeyData());
            logTO.setStatus(objTermLoanOtherDetailsTO.getCommand());

            executeOneTabQueries("BillsOtherDetailsTO", objTermLoanOtherDetailsTO);
            logDAO.addToLog(logTO);
        }
    }

    private void executeOneTabQueries(String strTOName, TransferObject termLoanTO) throws Exception {
        try {
            StringBuffer sbMapName = new StringBuffer();
            if (termLoanTO.getCommand() != null) {
                sbMapName.append(termLoanTO.getCommand().toLowerCase());
                System.out.println("insertcheck" + sbMapName);
                sbMapName.append(strTOName);
                System.out.println("tostring" + sbMapName.toString());
                if (termLoanTO.getCommand().equals("INSERT")) {
                    //                    HashMap hash=new HashMap();
                    //                   TermLoanInstallmentTO actNum=(TermLoanInstallmentTO)termLoanTO;
                    //                    hash.put("ACT_NUM",actNum.getAcctNum());
                    //                    lst=sqlMap.executeQueryForList("getAllLoanInstallment",hash);
                    //                    if(lst !=null && lst.size()>0)
                    //                        executeUpdate("delLoanInstallment",hash);
                    ////                    actNum=null;
                    //                    hash=null;
                    //                    lst=null;
                }
                executeUpdate(sbMapName.toString(), termLoanTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeUpdate(String str, Object objTermLoanTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTermLoanTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateNetworthDetails() throws Exception {
        try {
            sqlMap.executeUpdate("updateCustNetworthDetailsTL", netWorthDetailsMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private HashMap getProdBehavesLike(HashMap dataMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("Bills.getBehavesLike", dataMap);
        if (list.size() > 0) {
            HashMap retrieveMap = (HashMap) list.get(0);
            dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
            dataMap.put(LIMIT, retrieveMap.get(LIMIT));
            dataMap.put(BEHAVES_LIKE, retrieveMap.get(BEHAVES_LIKE));
            dataMap.put(INT_GET_FROM, retrieveMap.get(INT_GET_FROM));
            dataMap.put(SECURITY_DETAILS, retrieveMap.get(SECURITY_DETAILS));
            retrieveMap = null;
        }
        list = null;
        return dataMap;
    }

    private void authorize(HashMap map) throws Exception {
        try {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            HashMap dataMap;
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            RuleEngine engine;
            RuleContext context;
            ArrayList list;

            sqlMap.startTransaction();

            for (int i = selectedList.size() - 1, j = 0; i >= 0; --i, ++j) {
                dataMap = (HashMap) selectedList.get(j);

                // Get the Produt's Behaves Like
                dataMap = getProdBehavesLike(dataMap);

//                engine = new RuleEngine();
//                context = new RuleContext();
//                list = new ArrayList();

//                context.addRule(new InterestDetailsValidationRule());
//                if (dataMap.get(SECURITY_DETAILS).equals(FULLY_SECURED) && status.equals("AUTHORIZED")){
//                    context.addRule(new SecurityDetailsValidationRule());
//                }
//                
//                list = (ArrayList) engine.validateAll(context, dataMap);
//                
//                if(list != null ){
//                    HashMap exception = new HashMap();
//                    exception.put(CommonConstants.EXCEPTION_LIST, list);
//                    exception.put(CommonConstants.CONSTANT_CLASS, RULE_MAP_PATH);
//                    System.out.println("Exception List : " + list);
//                    
//                    throw new TTException(exception);
//                }

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(AUTHORIZEDT, currDt.clone());
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                sqlMap.executeUpdate("authorizeBills", dataMap);
                getAuthorizeNPA(dataMap);
                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    // Exisiting status is Created or Modified
                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        //                            sqlMap.executeUpdate("rejectInventoryDetails", dataMap);
                    }
                }
                logTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                logTO.setData(dataMap.toString());
                logTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                logDAO.addToLog(logTO);

                context = null;
                engine = null;

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    void getAuthorizeNPA(HashMap dataMap) throws Exception {
        System.out.println("authorize data###" + dataMap);
        List lst = sqlMap.executeQueryForList("SELECTNPA_HISTORY", dataMap);
        if (lst.size() > 0) {
            sqlMap.executeUpdate("updateNPA_HISTORY", dataMap);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap hash;
        System.out.print("termloandao bills module#######" + map);
        if (map.containsKey("NPAHISTORY")) {
            if (map.get("NPAHISTORY") != null) {
                NPA = (HashMap) map.get("NPAHISTORY");
                System.out.println("NPAHISTORY####" + NPA);
            }
        }
        if (map.containsKey("NPA")) {
            HashMap paramMap = new HashMap();
            System.out.println("#####@@daomap" + map);
            paramMap.put("BRANCH_CODE", _branchCode);
            paramMap.put("BRANCH_ID", _branchCode);
            paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
            if (map.containsKey("PROD_ID") && map.get("PROD_ID") != null) {
                paramMap.put("PROD_ID", map.get("PROD_ID"));
            }


            try {
                hash = new HashMap();
                int i = 0;
                HashMap loans_product = (HashMap) sqlMap.executeQueryForList("getLoansProduct", paramMap).get(0);
                Date curr_dt = DateUtil.getDateWithoutMinitues(currDt);
                String prod_behaves = CommonUtil.convertObjToStr(loans_product.get("BEHAVES_LIKE"));
                List actLst = null;
                if (loans_product != null && prod_behaves.equals("OD")) {
                    actLst = sqlMap.executeQueryForList("getAllLoanRecordOD", paramMap);
                } else {
                    actLst = sqlMap.executeQueryForList("getAllLoanRecord", paramMap);
                }
                for (int s = 0; s < actLst.size(); s++) {
                    HashMap hashMap = (HashMap) actLst.get(s);
                    String behaveLike = CommonUtil.convertObjToStr(hashMap.get("BEHAVES_LIKE"));
                    Date instalDt = new Date();
                    paramMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                    paramMap.putAll(hashMap);
                    instalDt = getInstallmentDate(paramMap);
                    System.out.println("instalDt###" + instalDt);

                    String ASSET_STATUS = null;
                    if (instalDt != null) {
                        paramMap.put("INSTALLMENT_DT", instalDt);
                        String asset_status = CommonUtil.convertObjToStr(hashMap.get("ASSET_STATUS"));

                        System.out.println(asset_status.equals("STANDARD_ASSETS") + "parammap#####" + paramMap);
                        int period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
                        System.out.println(period + "datediff#####" + DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt));
                        if (period > 0) {
                            if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("STANDARD_ASSETS")) {
                                //note when the account status changed std to substd upto date int shoude be calculated
                                TaskHeader header = new TaskHeader();
                                HashMap assetMap = new HashMap();
                                header.setBranchID(_branchCode);
                                assetMap.put("PROD_ID", hashMap.get("PROD_ID"));
                                assetMap.put("PRODUCT_ID", hashMap.get("PROD_ID"));
                                assetMap.put("ACT_TO", hashMap.get("ACCT_NUM"));
                                assetMap.put("ACT_FROM", hashMap.get("ACCT_NUM"));
                                assetMap.put("DATE_FROM", hashMap.get("LAST_INT_CALC_DT"));
                                assetMap.put("DATE_TO", curr_dt);
                                assetMap.put("CHARGESUI", "CHARGESUI");
                                header.setTaskParam(assetMap);
                                System.out.println("assetMap#######" + assetMap);
                                if (hashMap.containsKey("CALENDAR_FREQ") && hashMap.get("CALENDAR_FREQ") != null && hashMap.get("CALENDAR_FREQ").equals("Y")) {
                                    InterestCalculationTask intcalTask = new InterestCalculationTask(header);
                                    intcalTask.executeTask();
                                }
                                ASSET_STATUS = "SUB_STANDARD_ASSETS";
                            }
                        }
                        period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL"));
                        if (period > 0) {
                            if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("SUB_STANDARD_ASSETS")) {
                                ASSET_STATUS = "DOUBTFUL_ASSETS_1";
                            }
                        }

                        period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_2"));
                        if (period > 0) {
                            if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_1")) {
                                ASSET_STATUS = "DOUBTFUL_ASSETS_2";
                            }
                        }

                        period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_3"));
                        if (period > 0) {
                            if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_2")) {
                                ASSET_STATUS = "DOUBTFUL_ASSETS_3";
                            }
                        }

                        period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_LOSS"));
                        if (period > 0) {
                            if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status.equals("DOUBTFUL_ASSETS_3")) {
                                ASSET_STATUS = "LOSS_ASSETS";
                            }
                        }
                        //                              period=CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
                        //                           if(DateUtil.dateDiff(DateUtil.addDays(instalDt,period),curr_dt)<0 && asset_status.equals("LOSS_ASSETS")){
                        //                               ASSET_STATUS="SUB_STANDARD_ASSETS";
                        //                           }


                        //                              List lst=sqlMap.executeQueryForList("NPA_SELECT_STD_TO_SUBSTD",paramMap);


                        //                        for(  i=0;i<lst.size();i++){
                        //                            hash=(HashMap)lst.get(i);


                        if (ASSET_STATUS != null && ASSET_STATUS.length() > 0) {
                            hashMap.put("CURR_STATUS", ASSET_STATUS);
                            hashMap.put("TO_DATE", paramMap.get("TODAY_DT"));
                            hashMap.put("TODAY_DT", paramMap.get("TODAY_DT"));
                            //                        hash.put("STATUS_BY","TTSYSTEM");
                            hashMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                            sqlMap.executeUpdate("INSERT_NPA_HISTORY", hashMap);
                            System.out.println("finalmap####" + hashMap);
                            hashMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                            sqlMap.executeUpdate("NPA_std_to_substdtest", hashMap);
                        }

                        //                        }


                        /*
                         * sqlMap.executeUpdate("NPA_std_to_substd", map);
                         //                        lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_1",paramMap);
                         sqlMap.executeUpdate("NPA_substd_to_doubt_1", map);
                         hash=new HashMap();
                         for( i=0;i<lst.size();i++){
                         hash=(HashMap)lst.get(i);
                         hash.put("CURR_STATUS",ASSET_STATUS);
                         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
                         //                            hash.put("STATUS_BY","TTSYSTEM");
                         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
                         }
                       
                         lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_2",paramMap);
                         sqlMap.executeUpdate("NPA_substd_to_doubt_2",map);
                         hash=new HashMap();
                         for(i=0;i<lst.size();i++){
                         hash=(HashMap)lst.get(i);
                         hash.put("CURR_STATUS","DOUBTFUL_ASSETS_2");
                         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
                         //                            hash.put("STATUS_BY","TTSYSTEM");
                         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
                         }
                       
                         lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_3",paramMap);
                         sqlMap.executeUpdate("NPA_substd_to_doubt_3",map);
                         hash=new HashMap();
                         for(i=0;i<lst.size();i++){
                         hash=(HashMap)lst.get(i);
                         hash.put("CURR_STATUS","DOUBTFUL_ASSETS_3");
                         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
                         //                            hash.put("STATUS_BY","TTSYSTEM");
                         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
                         }
                       
                         lst=sqlMap.executeQueryForList("NPA_SELECT_DOUBT_3_TO_LOSS",paramMap);
                         sqlMap.executeUpdate("NPA_doubt_3_to_loss", paramMap);
                         hash=new HashMap();
                         for( i=0;i<lst.size();i++){
                         hash=(HashMap)lst.get(i);
                         hash.put("CURR_STATUS","LOSS_ASSETS");
                         hash.put("TO_DATE",paramMap.get("TODAY_DT"));
                         //                        hash.put("STATUS_BY","TTSYSTEM");
                         hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                         sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash);
                         }  */
                    }
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
            }
            return null;
        } else {
            logDAO = new LogDAO();
            logTO = new LogTO();
            objAuthorizedSignatoryDAO = new AuthorizedSignatoryDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
            objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));

            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            jointAcctMap = (HashMap) map.get("TermLoanJointAcctTO");
            // To update the networth details in Customer Table
            netWorthDetailsMap = (HashMap) map.get("NETWORTH_DETAILS");
            objTermLoanBorrowerTO = (TermLoanBorrowerTO) map.get("TermLoanBorrowerTO");
            objTermLoanCompanyTO = (TermLoanCompanyTO) map.get("TermLoanCompanyTO");
            objAuthorizedSignatoryDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
            objAuthorizedSignatoryDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
            sanctionMap = (HashMap) map.get("TermLoanSanctionTO");
            sanctionFacilityMap = (HashMap) map.get("TermLoanSanctionFacilityTO");
            facilityMap = (HashMap) map.get("TermLoanFacilityTO");
            securityMap = (HashMap) map.get("TermLoanSecurityTO");
            repaymentMap = (HashMap) map.get("TermLoanRepaymentTO");
            installmentMap = (HashMap) map.get("TermLoanInstallmentTO");
            installmentAllMap = (LinkedHashMap) map.get("TermRepaymentInstallmentAllTO");
            System.out.println("installmentAllmap" + installmentAllMap);
            installmentMultIntMap = (HashMap) map.get("TermLoanInstallMultIntTO");
            guarantorMap = (HashMap) map.get("TermLoanGuarantorTO");
            documentMap = (HashMap) map.get("TermLoanDocumentTO");
            interestMap = (HashMap) map.get("TermLoanInterestTO");
            Object objClassificationTO = map.get("TermLoanClassificationTO");

            if (map.containsKey("LTD")) {
                depositCustDetMap = (HashMap) map.get("LTD");
            }

            if (objClassificationTO != null) {
                objTermLoanClassificationTO = (TermLoanClassificationTO) objClassificationTO;
            } else {
                objTermLoanClassificationTO = null;
            }
            Object objOtherDetailsTO = map.get("TermLoanOtherDetailsTO");
            if (objOtherDetailsTO != null) {
                objTermLoanOtherDetailsTO = (TermLoanOtherDetailsTO) objOtherDetailsTO;
            } else {
                objTermLoanOtherDetailsTO = null;
            }

            final String command = objTermLoanBorrowerTO.getCommand();
            System.out.println("welcometocommand" + command);

            if (netWorthDetailsMap != null && netWorthDetailsMap.keySet().size() > 0) {
                if (netWorthDetailsMap.containsKey("NETWORTH_AS_ON")) {
                    if (netWorthDetailsMap.get("NETWORTH_AS_ON") != null) {
                        updateNetworthDetails();
                    }
                }
            }

            delRefMap = new HashMap();

            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(map);
                }
            } else {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            }

            destroyObjects();
            HashMap returnMap = new HashMap();
            if (lienNo.length() > 0) {
                returnMap.put("LIENNO", lienNo);
            }
            if (acct_No != null) {
                returnMap.put("ACCTNO", acct_No);
            }
            if (returnMap.size() > 0) {
                return returnMap;
            }
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public Date getInstallmentDate(HashMap paramMap) throws Exception {
        HashMap allInstallmentMap = new HashMap();
        Date inst_dt = null;
        System.out.println("paramMap#####" + paramMap);
        String behaveLike = CommonUtil.convertObjToStr(paramMap.get("BEHAVES_LIKE"));
        //        paramMap.put("ACT_NUM",hash.get("ACCT_NUM"));
        if (behaveLike != null && (!behaveLike.equals("OD"))) {
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", paramMap);

            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", paramMap);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            //            if(totPrinciple >0){
            totPrinciple += totExcessAmt;
            List allLst = sqlMap.executeQueryForList("getAllLoanInstallment", paramMap);
            inst_dt = null;
            for (int i = 0; i < allLst.size(); i++) {
                allInstallmentMap = (HashMap) allLst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;

                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);

                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple += CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                    break;
                }

            }

            //            return inst_dt;
            //            }
        } else {
            String asset_status = CommonUtil.convertObjToStr(paramMap.get("ASSET_STATUS"));
            Date curr_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("TODAY_DT")));
            Date previus_dt = null;
            List transDetails = sqlMap.executeQueryForList("getFirstTranDetails", paramMap);
            System.out.println("getFirstTranDetails####" + transDetails);
            if (asset_status.equals("STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_SUBSTANDARD") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_SUBSTANDARD")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("SUB_STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_1") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_1")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);
            if (asset_status.equals("DOUBTFUL_ASSETS_1")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_2") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_2")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS_2")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_3") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_3")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS_3")) {
                if (paramMap.get("PERIOD_TRANS_LOSS") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_LOSS")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            Date transdt = null;
            if (transDetails != null && transDetails.size() > 0) {
                HashMap transDetailsMap = (HashMap) transDetails.get(0);
                transdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDetailsMap.get("TRANS_DT")));
            }
            System.out.println("previusdate" + previus_dt + "transdt" + transdt);
            System.out.println("DateUtil.dateDiff(previus_dt,transdt)<0" + DateUtil.dateDiff(previus_dt, transdt));
            if (transdt != null && previus_dt != null && DateUtil.dateDiff(previus_dt, transdt) < 0) {

                //                         if(asset_status.equals("STANDARD_ASSETS"))
                //                         {
                GregorianCalendar firstdaymonth = new GregorianCalendar(1, previus_dt.getMonth() + 1, previus_dt.getYear() + 1900);
                int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                GregorianCalendar lastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth() + 1, noOfDays);
                paramMap.put("FROM_DT", firstdaymonth.getTime());
                paramMap.put("TO_DATE", lastdaymonth.getTime());
                System.out.println("paramMap###" + paramMap);
                List gettotCredit = sqlMap.executeQueryForList("getTotTranAmt", paramMap);
                System.out.println("getTotTranAmt####" + transDetails);
                if (gettotCredit != null && gettotCredit.size() > 0) {
                    GregorianCalendar debitIntMonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), 1);
                    noOfDays = debitIntMonth.getActualMaximum(debitIntMonth.DAY_OF_MONTH);
                    GregorianCalendar debitlastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), noOfDays);
                    paramMap.put("FROM_DT", debitIntMonth.getTime());
                    paramMap.put("TO_DATE", debitlastdaymonth.getTime());
                    System.out.println("getTotTranAmt####2paramMap" + paramMap);
                    List lst = sqlMap.executeQueryForList("getDebitTranAmt", paramMap);
                    if (lst != null && lst.size() > 0) {
                        HashMap hash = (HashMap) lst.get(0);
                        HashMap totCredit = (HashMap) gettotCredit.get(0);
                        double ibal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double totCreditamt = CommonUtil.convertObjToDouble(totCredit.get("AMOUNT")).doubleValue();
                        if (totCreditamt >= ibal) {
                            inst_dt = null;
                        } else {
                            inst_dt = DateUtil.getDateWithoutMinitues(currDt);
                        }
                    } else {
                        inst_dt = null;
                    }
                }

                //                         }else if(asset_status.equals("SUB_STANDARD_ASSETS")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL")));
                //
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_1")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_2")));
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_2")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_3")));
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_3")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_LOSS")));
                //                         }else if(asset_status.equals("LOSS_ASSETS")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_SUBSTANDARD")));
                //                         }
            } else {
                inst_dt = null;
            }
        }
        return inst_dt;
    }

    private void destroyObjects() {
        logDAO = null;
        logTO = null;
        objTermLoanBorrowerTO = null;
        objTermLoanCompanyTO = null;
        jointAcctMap = null;
        netWorthDetailsMap = null;
        authorizedMap = null;
        poaMap = null;
        sanctionMap = null;
        sanctionFacilityMap = null;
        facilityMap = null;
        securityMap = null;
        repaymentMap = null;
        installmentMap = null;
        installmentMultIntMap = null;
        guarantorMap = null;
        documentMap = null;
        interestMap = null;
        objTermLoanClassificationTO = null;
        objTermLoanOtherDetailsTO = null;
        objAuthorizedSignatoryDAO = null;
        objPowerOfAttorneyDAO = null;
    }

    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            data.put("WHERE", "LA00000000001061");
            data.put("KEY_VALUE", "ACCOUNT_NUMBER");
            new TermLoanDAO().executeQuery(data);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
