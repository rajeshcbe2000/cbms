/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TermLoanDAO.java
 *
 * Created on Fri Jan 09 18:03:55 CST 2004
 */
package com.see.truetransact.serverside.correctiontool;

import com.see.truetransact.serverside.termloan.kcc.multiplerenewal.*;
import com.see.truetransact.serverside.termloan.kcc.*;
import com.see.truetransact.serverside.termloan.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
//import com.ibatis.db.sqlmap.cache.CacheModel;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.termloan.InterestDetailsValidationRule;
import com.see.truetransact.businessrule.termloan.SecurityDetailsValidationRule;

import com.see.truetransact.serverside.transaction.common.TransactionFactory;

import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
//import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
//import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.clientutil.ClientUtil;
//import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.AuthorizedSignatoryDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.powerofattorney.PowerOfAttorneyDAO;
import com.see.truetransact.serverside.termloan.settlement.SettlementDAO;
//import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.termloan.*;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.GregorianCalendar;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
//import com.see.truetransact.serverside.deposit.lien.DepositLien;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.serverside.operativeaccount.TodAllowedDAO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.termloan.loanapplicationregister.LoanApplicationDAO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.product.gl.GLUpdateDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.correctiontool.DataCorrectionLogTO;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccountParamTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
//import com.see.truetransact.transferobject.termloan.TermLoanSecurityMemberTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecuritySalaryTO;
import com.see.truetransact.transferobject.termloan.TermLoanLosTypeTO;
import com.see.truetransact.transferobject.termloan.TermLoanDepositTypeTO;
import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.see.truetransact.transferobject.termloan.TermLoanSecurityLandTO;

/**
 * TermLoan DAO.
 *
 * @author shanmugavel
 *
 */
public class DataCorrectionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private TermLoanBorrowerTO objTermLoanBorrowerTO;
    private TermLoanCompanyTO objTermLoanCompanyTO;
    private TermLoanClassificationTO objTermLoanClassificationTO;
    private TermLoanOtherDetailsTO objTermLoanOtherDetailsTO;
    private AuthorizedSignatoryDAO objAuthorizedSignatoryDAO;
    private PowerOfAttorneyDAO objPowerOfAttorneyDAO;
    private TermLoanExtenFacilityDetailsTO objTermLoanExtenFacilityDetailsTO;
    private TermLoanCaseDetailTO objCaseDetailTO;
    private TermLoanChargesTO objChargeTO;
    private DailyLoanSanctionDetailsTO objDailyLoanSanctionDetailsTO;
    private HashMap jointAcctMap;
    TransactionTO transactionTO = new TransactionTO();
    // To update the networth details in Customer Table
    private HashMap netWorthDetailsMap;
    private HashMap authorizedMap;
    private HashMap poaMap;
    private HashMap sanctionMap;
    private HashMap dailyLoanMap;
    private HashMap oTSMap;
    private HashMap caseDetailMap;
//    private LinkedHashMap caseDetailMap = null;
//    private LinkedHashMap deletedCaseDetailMap = null;
    private HashMap sanctionFacilityMap;
    private HashMap facilityMap;
    private HashMap securityMap;
    private HashMap repaymentMap;
    private HashMap installmentMap;
    private LinkedHashMap installmentAllMap;
    private HashMap installmentSingleMap;
    private HashMap installmentMultIntMap;
    private HashMap guarantorMap;
    private HashMap guaranInstitMap;
    private HashMap documentMap;
    private HashMap interestMap;
    private HashMap additionalSanMap;
    private HashMap NPA;
    private HashMap DisbursementMap;
    private HashMap depositCustDetMap;
    private LinkedHashMap multipleDeposit;
    private LinkedHashMap deleteLien;
    private LinkedHashMap updateLien;
    private HashMap transMap;
    private HashMap termLoanExtenFacilityMap = new HashMap();
    private KccRenewalTO kccTo = new KccRenewalTO();
    private List kccToList =  new ArrayList();
    private String borrower_No;
    private String acct_No;
    private String lienNo = "";
    public String prod_id = "";
    public String cmd = null;
    HashMap resultMap = new HashMap();
    // Security Purpose
    private TermLoanSecurityMemberTO objMemberTypeTO;
    private HashMap transReturnMap = new HashMap();
    private TermLoanSecurityLandTO objTermLoanSecurityLandTO;
    private LinkedHashMap deletedMemberTableValues = null;
    private LinkedHashMap memberTableDetails = null;
    private LinkedHashMap deletedCollateralTableValues = null;
    private LinkedHashMap collateralTableDetails = null;
    private LinkedHashMap termLoanSecuritySalaryTOMap = null;
    private LinkedHashMap termLoanSecuritySalaryTODeletedMap = null;
    private TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO;
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
    private Date curr_dt = null;
    private String loanType = "";
    private boolean newLoan = false;
    private String productCategory = "";
    private String keyValue = "";
    public List chargeLst = null;
    public List riskFundList;
    public String suspenceAccountNo = null;
    public String suspenceProdId = null;
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    private TermLoanSecurityTO objTermLoanSecurityTO;
    private String behaves_like = null;
    private LinkedHashMap depositTableDetails = null;
    private LinkedHashMap deletedDepositTableValues = null;
    private LinkedHashMap losTableDetails = null;
    private LinkedHashMap deletedLosTableValues = null;
    private double securAmt = 0.0;
    private double balShareAmt = 0.0;
    private String recovery = "";
    private String intGetFromUpdate="";
    private String intDeleteFlag="";
    private int ibrHierarchy = 0;
    private LinkedHashMap deletedVehicleTableValues = null;
    private LinkedHashMap vehicleTableDetails = null;
    private String userId = "";
    private LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO;
    /**
     * Creates a new instance of TermLoanDAO
     */
    public DataCorrectionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("getdata####" + map);
        if (map.containsKey("LOAN_TYPE")) {
            loanType = CommonUtil.convertObjToStr(map.get("LOAN_TYPE"));
//            productCategory = CommonUtil.convertObjToStr(map.get("PRODUCT_CATEGORY"));
        } else {
            loanType = "";
        }

        if (map.containsKey("INT_TRANSACTION_REPAYMENT")) {
            String branchId = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            try {
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", map.get("ACT_NUM"));
                transactionDAO.setLinkBatchID((String) map.get("ACT_NUM"));
                transactionDAO.setInitiatedBranch(branchId);

                txMap.put(TransferTrans.DR_ACT_NUM, map.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_ID, map.get("PROD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                double interestAmt = CommonUtil.convertObjToDouble(map.get("INTEREST_AMT")).doubleValue();
                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                //                transferDao.setAuthorizeRemarks("DI");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                //DELETEING RECORD
                map.put("LINK_BATCH_ID", map.get("ACT_NUM"));
                map.put("TRANS_DT", curr_dt);
                map.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", map);
                TxTransferTO txtransferTO = new TxTransferTO();
                ArrayList batchList = new ArrayList();
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        txtransferTO = new TxTransferTO();
                        TxTransferTO singletxtransferTO = new TxTransferTO();
                        singletxtransferTO = (TxTransferTO) lst.get(i);
                        txtransferTO.setActNum(singletxtransferTO.getActNum());
                        txtransferTO.setAcHdId(singletxtransferTO.getAcHdId());
                        txtransferTO.setAmount(singletxtransferTO.getAmount());
                        txtransferTO.setBatchId(singletxtransferTO.getBatchId());
                        txtransferTO.setBranchId(singletxtransferTO.getBranchId());
                        txtransferTO.setInitiatedBranch(singletxtransferTO.getInitiatedBranch());
                        txtransferTO.setLinkBatchId(singletxtransferTO.getLinkBatchId());
                        txtransferTO.setProdId(singletxtransferTO.getProdId());
                        txtransferTO.setProdType(singletxtransferTO.getProdType());
                        txtransferTO.setStatus("DELETED");
                        txtransferTO.setTransDt(singletxtransferTO.getTransDt());
                        txtransferTO.setTransId(singletxtransferTO.getTransId());
                        txtransferTO.setTransType(singletxtransferTO.getTransType());
                        txtransferTO.setTransMode(singletxtransferTO.getTransMode());
                        batchList.add(txtransferTO);
                    }
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    TransferTrans transfer = new TransferTrans();
                    transfer.setInitiatedBranch((String) map.get("BRANCH_CODE"));
                    transfer.doDebitCredit(batchList, (String) map.get("BRANCH_CODE"), false, "INSERT");


                }
                //
                transferDao = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                System.out.println("transList  ###" + transList + "transferDao   :" + transferDao);
                transferTrans.setLoanDebitInt("DI");
                transferTrans.doDebitCredit(transList, branchId, false);
//                HashMap hash=new HashMap();
                //                hash.put("ACCOUNTNO",map.get("ACT_NUM"));
                //                Date curr_dt=(Date)map.get("CURR_DATE");
                //                hash.put("LAST_CALC_DT",DateUtil.addDays(curr_dt,-1));
                //                sqlMap.executeUpdate("updateclearBal", hash);

                //                transactionDAO.doTransferLocal(transList, branchId);
                //            transactionDAO.setCommandMode(commandMode);
            } catch (Exception e) {
                //                throw new TTException(" A/c Head not set.");
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
            ServiceLocator.flushCache(sqlMap);
            System.gc();
            return returnMap;
        }
        if (map.containsKey("NPA")) {
            //System.out.println("mapNPA####@@@@@" + map);
            List lst = (List) sqlMap.executeQueryForList(CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME)), map);
            //System.out.println("######od" + lst);
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
//            if (map.containsKey("KEY_VALUE")){
//                returnMap.put("KEY_VALUE", map.get("KEY_VALUE"));
//            }else{
//                // Useful in the Client side based on the Account Number
//                // for authorization
//                returnMap.put("KEY_VALUE", "AUTHORIZE");
//            }
//            if (map.containsKey("KEY_VALUE")){
//                if (returnMap.get("KEY_VALUE").equals("BORROWER_NUMBER")){
//                    // To retrieve the values based on Borrower Number
//                    returnMap = executeQueryForBorrNo(returnMap, where);
//                    where=CommonUtil.convertObjToStr("LOAN_NO");
//                    returnMap .putAll(executeQueryForAcctNo(returnMap, where));
//                }else if (returnMap.get("KEY_VALUE").equals("ACCOUNT_NUMBER")){
//                    // To retrieve the values based on Account Number
//                    returnMap = executeQueryForAcctNo(returnMap, where);
//                }
//            }else if (returnMap.get("KEY_VALUE").equals("AUTHORIZE")){
            // To retrieve the values based on Account Number(To populate UI
            // at the time of Authorization)
            returnMap = executeQueryForAuthorize(returnMap, where, CommonUtil.convertObjToStr(map.get("BORROW_NO")));

            HashMap checkMap = new HashMap();
            HashMap behaveMap = (HashMap) ((List) sqlMap.executeQueryForList("getLoansProduct", map)).get(0);
            if (CommonUtil.convertObjToStr(behaveMap.get("BEHAVES_LIKE")).equals("OD")) {
                checkMap.put("PROD_TYPE", "AD");
            } else {
                checkMap.put("PROD_TYPE", "TL");
            }
            checkMap.put("PROD_ID", map.get("PROD_ID"));
            checkMap.put("ACT_NUM", where);
            List list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", checkMap);
            if (list != null && list.size() > 0) {
                returnMap.put("SMSSubscriptionTO", list);
            }
//            }
            map = null;
            where = null;
            ServiceLocator.flushCache(sqlMap);
            objAuthorizedSignatoryDAO = null;
            objPowerOfAttorneyDAO = null;
            System.out.println("returnmap#######" + returnMap);
            return returnMap;
        }
    }

    private HashMap executeQueryForBorrNo(HashMap returnMap, String where) throws Exception {
        System.out.println(where + "borrownoreturnMap#####" + returnMap);
        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanCompanyTO", where);
        returnMap.put("TermLoanCompanyTO", list);
        list = null;

        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);

        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;


        return returnMap;
    }

    private HashMap executeQueryForAcctNo(HashMap returnMap, String where) throws Exception {
       // System.out.println(where + "acctnumberreturnMap#####" + returnMap);
        _branchCode = CommonUtil.convertObjToStr(returnMap.get(CommonConstants.BRANCH_ID));
        curr_dt = curr_dt;
        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;


        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanGuarantorTO", where);
        returnMap.put("TermLoanGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanInstitGuarantorTO", where);
        returnMap.put("TermLoanInstitGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanOtherDetailsTO", where);
        returnMap.put("TermLoanOtherDetailsTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanAdditionalSanctionTO", where);
        returnMap.put("TermLoanAdditionalSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDisburstTO", where);
        returnMap.put("TermLoanDisburstTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityExtnTO", where);
        returnMap.put("TermLoanFacilityExtnTO", list);
        list = null;

//                     System.out.println("obj^^^^^^^^^^^^"+obj);
        HashMap dataMap = new HashMap();
        dataMap.put("ACCT_NUM", where);
        SettlementDAO objSetDao = new SettlementDAO("common");

        HashMap tempReturnMap = objSetDao.getDataSet(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);

        tempReturnMap = objSetDao.getDataActTrans(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);


        return returnMap;
    }

    private HashMap executeQueryForAuthorize(HashMap returnMap, String where, String borrow_no) throws Exception {

        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanCaseDetailTO", where);
        returnMap.put("TermLoanCaseDetailTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanGuarantorTO", where);
        returnMap.put("TermLoanGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanOtherDetailsTO", where);
        returnMap.put("TermLoanOtherDetailsTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanAdditionalSanctionTO", where);
        returnMap.put("TermLoanAdditionalSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDisburstTO", where);
        returnMap.put("TermLoanDisburstTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityExtnTO", where);
        returnMap.put("TermLoanFacilityExtnTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanInstitGuarantorTO", where);
        returnMap.put("TermLoanInstitGuarantorTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("selectDailyLoanSanctionTO", where);
        returnMap.put("TermLoanDailyLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("selectTermLoanCourtDetailsTO", where);
        returnMap.put("TermLoanCourtDetailsTO", list);
        list = null;




        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSecuritySalaryTO", where);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = CommonUtil.convertObjToStr(((TermLoanSecuritySalaryTO) list.get(j)).getSlNo());
                ParMap.put(((TermLoanSecuritySalaryTO) list.get(j)).getSlNo(), list.get(j));
            }
            returnMap.put("TermLoanSecuritySalaryTO", ParMap);

        }
        list = null;

        list = (List) sqlMap.executeQueryForList("getDepositLoanInEdit", where);
        if (list != null && list.size() > 0) {
            returnMap.put("getDepositLoanInEdit", list);
        }
        list = null;

//        if(loanType !=null && loanType.equals("LTD")){
//        list = (List) sqlMap.executeQueryForList("getDepositLienDetails", where);
//        if(list!=null && list.size() > 0){
//        returnMap.put("TermLoanDepositLienTO", list);
//        }
//        list = null;
//        }
        List memberList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityMemberTO", where);
        if (memberList != null && memberList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            //System.out.println("@@@memberList" + memberList);
            for (int i = memberList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityMemberTO) memberList.get(j)).getMemberNo();
                ParMap.put(((TermLoanSecurityMemberTO) memberList.get(j)).getMemberNo(), memberList.get(j));
            }
            returnMap.put("memberListTO", ParMap);
        }
        memberList = null;
         List vehicleList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityVehicleTO", where);
        if (vehicleList != null && vehicleList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = vehicleList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityVehicleTO) vehicleList.get(j)).getMemberNo();
                ParMap.put(((TermLoanSecurityVehicleTO) vehicleList.get(j)).getMemberNo(), vehicleList.get(j));
            }
            returnMap.put("TermLoanSecurityvehicleListTO", ParMap);
        }
        vehicleList = null;
        List collateralList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityLandTO", where);
        if (collateralList != null && collateralList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            //System.out.println("@@@collateralList" + collateralList);
            for (int i = collateralList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityLandTO) collateralList.get(j)).getMemberNo();
                //ParMap.put(((TermLoanSecurityLandTO) collateralList.get(j)).getMemberNo(), collateralList.get(j));
                ParMap.put(((TermLoanSecurityLandTO) collateralList.get(j)).getMemberNo() + "_" + (j + 1), collateralList.get(j));
            }
            returnMap.put("CollateralListTO", ParMap);
        }
        collateralList = null;
        
         // Added by nithya on 07-03-2020 for KD-1379
        List goldSlockList = (List) sqlMap.executeQueryForList("getSelectLoanSecurityGoldStockTO", where);
        if (goldSlockList != null && goldSlockList.size() > 0) {
            LoansSecurityGoldStockTO objLoansSecurityGoldStockTO = (LoansSecurityGoldStockTO)goldSlockList.get(0);
            returnMap.put("CustomerGoldStockSecurityTO", objLoansSecurityGoldStockTO); 
        }
        //End

        List depositTypeList = (List) sqlMap.executeQueryForList("EditTermLoanDepositTypeDetaisTO", borrow_no);

        if (depositTypeList != null && depositTypeList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            //System.out.println("@@@memberList" + memberList);
            for (int i = depositTypeList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanDepositTypeTO) depositTypeList.get(j)).getTxtDepNo();
                ParMap.put(((TermLoanDepositTypeTO) depositTypeList.get(j)).getTxtDepNo(), depositTypeList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("DeositTypeDetails", ParMap);
        }
        depositTypeList = null;

        List losTypeList = (List) sqlMap.executeQueryForList("EditTermLoanLosTypeDetaisTO", borrow_no);

        if (losTypeList != null && losTypeList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();

            for (int i = losTypeList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanLosTypeTO) losTypeList.get(j)).getLosSecurityNo();
                ParMap.put(((TermLoanLosTypeTO) losTypeList.get(j)).getLosSecurityNo(), losTypeList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("LosTypeDetails", ParMap);
        }
        losTypeList = null;

        HashMap dataMap = new HashMap();
        dataMap.put("ACCT_NUM", where);
        SettlementDAO objSetDao = new SettlementDAO("common");

        HashMap tempReturnMap = objSetDao.getDataSet(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);
        tempReturnMap = objSetDao.getDataActTrans(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);

        if (loanType.equals("LTD")) {
//            if (productCategory.equals("PADDY_LOAN")) {
            list = (List) sqlMap.executeQueryForList("getPaddyCustDetailsForEditAndAuthorize", where);
            if (list != null && list.size() > 0) {
                returnMap.put("PaddyTO", list.get(0));
            }
            list = (List) sqlMap.executeQueryForList("getMDSCustDetailsForEditAndAuthorize", where);
            if (list != null && list.size() > 0) {
                returnMap.put("MDSTO", list);
            }
            list = null;
//            }
        }

        // set the Borrower number to where
        where = borrow_no;
        list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanCompanyTO", where);
        returnMap.put("TermLoanCompanyTO", list);
        list = null;

        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);

        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;

        return returnMap;
    }
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private List getInterestDetails(String where, HashMap returnMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getInterestDetailsWhereConditions", where);
        HashMap whereConditionMap = new HashMap();
        HashMap finalMap = new HashMap();
        String strIntGetFrom = "";
        String interstType = "";
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
            whereConditionMap.put(FROM_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("FROM_DATE")))));
            whereConditionMap.put(TO_DATE, setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(finalMap.get("TO_DATE")))));
            strIntGetFrom = CommonUtil.convertObjToStr(finalMap.get("INT_GET_FROM"));
            interstType = CommonUtil.convertObjToStr(finalMap.get("INTEREST_TYPE"));
        }
        list = null;
        list = new ArrayList();
        if (strIntGetFrom.equals(PROD)) {
            if (interstType.equals("FLOATING_RATE")) {
                list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestFloatTO", whereConditionMap);
            } else {
                list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestTO", whereConditionMap);
            }
        } else if (strIntGetFrom.equals(ACT)) {
            list = (List) sqlMap.executeQueryForList("getSelectTermLoanInterestTO", where);
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
        executeDailyLoanSanctionTabQuery();
        executeOTSLoanSanctionTabQuery();
//        executeSecurityTabQuery();
        executeRepaymentQuery();
        executeInstallmentTabQuery();
        executeCaseTabQuery();
        executeInstallmentMultIntTabQuery();
        executeGuarantorTabQuery();
        executeInsititGuarantorTabQuery();
        executeDocumentTabQuery();
        executeInterestTabQuery();
        executeClassificationTabQuery();
        executeOtherDetailsTabQuery();
        executeAdditionalSanctionTabQuery();
        executeDisbursementTabQuery();
        executeExtenFacilityDetailsTabQuery();
        executeSalaryDetailsQuery();
    }

    private void insertData(HashMap map, HashMap transDetailMap,String generateCashId) throws Exception {
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
            executeUpdate("insertTermLoanBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

            logTO.setData(objTermLoanCompanyTO.toString());
            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
            logTO.setStatus(objTermLoanCompanyTO.getCommand());
            executeUpdate("insertTermLoanCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);
            executeAllTabQuery();
            if(vehicleTableDetails!=null){
            insertVehicleTableDetails();}
            insertMemberTableDetails();
            insertCollateralTableDetails();
            insertDepositTableDetails();
            insertLosTableDetails();
            
            
            if (objTermLoanSecuritySalaryTO != null) {
                logTO.setData(objTermLoanSecuritySalaryTO.toString());
                logTO.setPrimaryKey(objTermLoanSecuritySalaryTO.getKeyData());
                logTO.setStatus(objTermLoanSecuritySalaryTO.getCommand());
                objTermLoanSecuritySalaryTO.setAcctNum(acct_No);
//                executeUpdate("insertTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                logDAO.addToLog(logTO);
            }
            executeTransactionPart(map, transDetailMap,generateCashId);
            if (chargeLst != null && chargeLst.size() > 0) {
                if (map.containsKey("serviceTaxDetailsTO")) {
                    ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                    insertServiceTaxDetails(objserviceTaxDetailsTO);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    // added by nithya on 01-04-2016 for 3930 
    private void updateRepaymentSchedule(HashMap map) throws Exception{        
        try{
           HashMap updateMap =new HashMap() ;
           //HashMap newMap = new HashMap();           
           if(map!=null && map.containsKey("UPDATE_REPAY_SCHEDULE_TABLE")){
             List newRepayList = (List)map.get("UPDATE_REPAY_SCHEDULE_TABLE");             
             for(int i=0; i< newRepayList.size(); i++){
               HashMap newMap = (HashMap)newRepayList.get(i);
               updateMap.put("ACCT_NUM",newMap.get("ACCT_NUM"));  
               updateMap.put("SCHEDULE_NO",newMap.get("SCHEDULE_NO")); 
               sqlMap.executeUpdate("updateRepayScheduleForCreation", updateMap);
             }
           }                    
        }catch(Exception e){
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;   
        }
    }
    
    
    // added by nithya on 01-04-2016 for 3930 
    private void insertRepaymentSchedule() throws Exception{     
    
        TermLoanRepaymentTO objTermLoanRepaymentTO;
        Set keySet;
        Object[] objKeySet;    
        try{            
            if (repaymentMap != null) {
                keySet = repaymentMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap repayDetailsMap;
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanRepaymentTO from the repaymentMap
                System.out.println("repaymentMap.size() :: " + repaymentMap.size());
                for (int i = repaymentMap.size() - 1, j = 0; i >= 0; --i, ++j) {                    
                   objTermLoanRepaymentTO = (TermLoanRepaymentTO) repaymentMap.get(objKeySet[j]); 
                   executeOneTabQueries("TermLoanRepaymentTO", objTermLoanRepaymentTO);                    
                   updateMap.put("ACCT_NUM",objTermLoanRepaymentTO.getAcctNum());
                   updateMap.put("TO_DT",getProperDateFormat(objTermLoanRepaymentTO.getLastInstallDt()));
                   updateMap.put("REPAYMENT_DT",getProperDateFormat(objTermLoanRepaymentTO.getFirstInstallDt()));
                   updateMap.put("REPAYMENT_FREQUENCY",objTermLoanRepaymentTO.getRepaymentType());
                   updateMap.put("NO_INSTALL",objTermLoanRepaymentTO.getNoInstallments());
                   if(objTermLoanRepaymentTO.getRepayMorotorium() > 0){
                     updateMap.put("NO_MORATORIUM",objTermLoanRepaymentTO.getRepayMorotorium());  
                     updateMap.put("MORATORIUM_GIVEN","Y");
                   }else{
                     updateMap.put("NO_MORATORIUM",objTermLoanRepaymentTO.getRepayMorotorium()); 
                     updateMap.put("MORATORIUM_GIVEN","N"); 
                   }
                   sqlMap.executeUpdate("updateSanctionDetailsForCreation", updateMap);
                   //REPAY_MOROTORIUM
                }
                updateMap = null;
            }             
            objTermLoanRepaymentTO = null;
    }catch(Exception e){
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;   
     }    
  }
    
   // added by nithya on 01-04-2016 for 3930  
   private void insertInstallmentSchedule() throws Exception{
       TermLoanInstallmentTO objTermLoanInstallmentTO;
        try {           
            if (installmentMap != null && installmentMap.size() > 0) {
                Set keySet;
                Object[] objKeySet;
                keySet = installmentMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanInstallmentTO from the installmentMap
                for (int i = installmentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstallmentTO = (TermLoanInstallmentTO) installmentMap.get(objKeySet[j]);
                    executeOneTabQueries("TermLoanInstallmentTO", objTermLoanInstallmentTO);
                }
            }        
            objTermLoanInstallmentTO = null;
        }catch(Exception e){
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;   
     }       
   }
    
    
    // added by nithya on 01-04-2016 for 3930 
    private void updateScheduleInstallment(HashMap map) throws Exception{ 
      
        try{
           HashMap updateMap =new HashMap() ;          
           if(map!= null && map.containsKey("UPDATE_LOANS_INSTALLMENT_TABLE")){
             List newInstallList = (List)map.get("UPDATE_LOANS_INSTALLMENT_TABLE"); 
             for(int i=0; i< newInstallList.size(); i++){
               HashMap newMap = (HashMap)newInstallList.get(i);
               updateMap.put("ACCT_NUM",newMap.get("ACCT_NUM"));  
               updateMap.put("SCHEDULE_NO",newMap.get("SCHEDULE_ID")); 
               sqlMap.executeUpdate("updateRepayInstallmentForCreation", updateMap);
             } 
           }                  
        }catch(Exception e){
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;   
        }
    }
    
    private void updateCaseDetails(HashMap map) throws Exception {
        System.out.println("dataMap @@@@@@" + map);
        sqlMap.executeUpdate("updateCaseAuthDetails", map);
    }

    private void executeSalaryDetailsQuery() throws Exception {
        if (termLoanSecuritySalaryTOMap != null && termLoanSecuritySalaryTOMap.size() > 0) {
            Set keyset = termLoanSecuritySalaryTOMap.keySet();
            Object obj[] = (Object[]) keyset.toArray();
            for (int i = 0; i < keyset.size(); i++) {
                TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) termLoanSecuritySalaryTOMap.get(obj[i]);
                if (objTermLoanSecuritySalaryTO != null && objTermLoanSecuritySalaryTO.getStatus().equals("CREATED")) {
                    //objTermLoanSecuritySalaryTO.setAcctNum(acct_No);
                objTermLoanSecuritySalaryTO.setStatus("CREATED");  
                objTermLoanSecuritySalaryTO.setRetirementDt(setProperDtFormat(objTermLoanSecuritySalaryTO.getRetirementDt()));
                executeUpdate("insertTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                } else {
                    executeUpdate("updateTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                }
            }
        }
        if (termLoanSecuritySalaryTODeletedMap != null && termLoanSecuritySalaryTODeletedMap.size() > 0) {
            Set keyset = termLoanSecuritySalaryTODeletedMap.keySet();
            Object obj[] = (Object[]) keyset.toArray();
            for (int i = 0; i < keyset.size(); i++) {
                TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) termLoanSecuritySalaryTODeletedMap.get(obj[i]);
                executeUpdate("updateTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
            }
        }
    }
   private void insertVehicleTableDetails() throws Exception {
        if (vehicleTableDetails != null &&vehicleTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(vehicleTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTableDetails.get(addList.get(i));
                objVehicleTypeTO.setAcctNum(acct_No);
                objVehicleTypeTO.setBranchCode(_branchCode);
                objVehicleTypeTO.setStatusDt((Date)curr_dt.clone());//added to avoid class cast Exception
                sqlMap.executeUpdate("insertTermLoanSecurityVehicleTO", objVehicleTypeTO);
                logTO.setData(objVehicleTypeTO.toString());
                logTO.setPrimaryKey(objVehicleTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objVehicleTypeTO = null;
            }
        }
    }
    private void insertMemberTableDetails() throws Exception {
        if (memberTableDetails != null && memberTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTableDetails.get(addList.get(i));
                objMemberTypeTO.setAcctNum(acct_No);
                objMemberTypeTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertTermLoanSecurityMemberTO", objMemberTypeTO);
                logTO.setData(objMemberTypeTO.toString());
                logTO.setPrimaryKey(objMemberTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objMemberTypeTO = null;
            }
        }
    }

    private void insertCollateralTableDetails() throws Exception {
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralTableDetails.get(addList.get(i));
                objTermLoanSecurityLandTO.setPledgeDt(getProperDateFormat(objTermLoanSecurityLandTO.getPledgeDt()));
                // Added by nithya on 28-07-2016 for 0004953
                objTermLoanSecurityLandTO.setDocumentDt(getProperDateFormat(objTermLoanSecurityLandTO.getDocumentDt()));
                // End
                objTermLoanSecurityLandTO.setAcctNum(acct_No);
                objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                System.out.println("objTermLoanSecurityLandTO222>>>>" + objTermLoanSecurityLandTO);
                sqlMap.executeUpdate("insertTermLoanSecurityLandTO", objTermLoanSecurityLandTO);
                logTO.setData(objTermLoanSecurityLandTO.toString());
                logTO.setPrimaryKey(objTermLoanSecurityLandTO.getKeyData());
                logDAO.addToLog(logTO);
                objTermLoanSecurityLandTO = null;
            }
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curr_dt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    private void executeTransactionPart(HashMap map, HashMap transDetailMap,String generateCashId) throws Exception {
        try {
            transDetailMap.put("ACCT_NUM", acct_No);
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            System.out.println("@##$#$% map #### :" + map);
            System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                if (transDetailMap.containsKey("TRANSACTION_PART")) {
                    transDetailMap = getProdBehavesLike(transDetailMap);
                    insertTimeTransaction(map, transDetailMap,generateCashId);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void editTransaction(HashMap transMap) throws Exception {
        transMap.put(CommonConstants.BRANCH_ID, _branchCode);
        if (transMap.containsKey("CashTransactionTO")) {

            CashTransactionDAO objCashTransactionDAO = new CashTransactionDAO();
            transMap.put("DEBIT_LOAN_TYPE", "DP");
            objCashTransactionDAO.execute(transMap, false);

        } else if (transMap.containsKey("TxTransferTO")) {
            TransferDAO objTransferDAO = new TransferDAO();
            objTransferDAO.execute(transMap, false);

        }
    }
    
    //Added by Jeffin John on 12-02-2015 for Mantis - 10281
    //Function to Calculate interest amount initially while loan amount disbursement
    private HashMap getInterestFirst(HashMap map, HashMap dataMap) {
        HashMap completeMap = new HashMap();
        double interest = 0.0;
        try {
            HashMap prodMap = new HashMap();
            HashMap actHdMap = null;
            List acctHdList = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
            if (acctHdList != null && acctHdList.size() > 0) {
                actHdMap = (HashMap) acctHdList.get(0);
                if (actHdMap != null && actHdMap.size() > 0 && actHdMap.containsKey("AC_DEBIT_INT")) {
                    completeMap.put("AC_DEBIT_INT", CommonUtil.convertObjToStr(actHdMap.get("AC_DEBIT_INT")));
                }
            }
            LoanProductAccountParamTO objLoanProductAccountParamTO = null;
            prodMap.put("value", CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            List prodList = sqlMap.executeQueryForList("getSelectLoanProductAccountParamTO", prodMap);
            if (prodList != null && prodList.size() > 0) {
                objLoanProductAccountParamTO = (LoanProductAccountParamTO) prodList.get(0);
                if (objLoanProductAccountParamTO != null) {
                    String isInterestPaidFirst = CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getIsInterestFirst());
                    if (isInterestPaidFirst != null && !isInterestPaidFirst.equals("") && isInterestPaidFirst.equals("Y")) {
                        if (map.containsKey("TermLoanRepaymentTO")) {
                            HashMap newMap = (HashMap) map.get("TermLoanRepaymentTO");
                            if (newMap != null && newMap.size() > 0 && newMap.containsKey("1")) {
                                TermLoanRepaymentTO objTermLoanRepaymentTO = (TermLoanRepaymentTO) newMap.get("1");
                                if (objTermLoanRepaymentTO != null) {
                                    Date fromDate = CommonUtil.getProperDate(curr_dt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTermLoanRepaymentTO.getFromDate())));
                                    String acctNum = CommonUtil.convertObjToStr(objTermLoanRepaymentTO.getAcctNum());
                                    Date toDate = CommonUtil.getProperDate(curr_dt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTermLoanRepaymentTO.getLastInstallDt())));
                                    completeMap.put("ACCT_NUM", acctNum);
                                    completeMap.put("TO_DATE", toDate);
                                    HashMap getInterestMap = new HashMap();
                                    getInterestMap.put("ACCT_NUM", acctNum);
                                    getInterestMap.put("FROM_DT", fromDate);
                                    getInterestMap.put("TO_DT", toDate);
                                    HashMap interestMap = null;
                                    List interestList = sqlMap.executeQueryForList("getInitInterestTL", getInterestMap);
                                    if (interestList != null && interestList.size() > 0) {
                                        interestMap = (HashMap) interestList.get(0);
                                        if (interestMap != null && interestMap.size() > 0 && interestMap.containsKey("INTEREST")) {
                                            interest = CommonUtil.convertObjToDouble(interestMap.get("INTEREST"));
                                            if (interest > 0) {
                                                completeMap.put("INTEREST", interest);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        completeMap = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completeMap;
    }
    //Code added by Jeffin John ends here

    private void insertTimeTransaction(HashMap map, HashMap dataMap,String generateCashId) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("mapTRANSACTION_PART " + map + "dataMap :" + dataMap);
        if (dataMap.containsKey("TRANSACTION_PART")) {
            HashMap reserchMap = new HashMap();
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                dataMap.put("ACT_NUM", dataMap.get("ACCT_NUM"));
                dataMap.put("USER_ID", map.get("USER_ID"));
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", dataMap.get("ACT_NUM"));
                //System.out.println("acHeads " + acHeads);
                double appraiserAmt = CommonUtil.convertObjToDouble(dataMap.get("APPRAISER_AMT")).doubleValue();
                double serviceTaxAmt = CommonUtil.convertObjToDouble(dataMap.get("SERVICE_TAX_AMT")).doubleValue(); 
                
                if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                    ArrayList loanAuthTransList =  new ArrayList();
                    HashMap loanAuthTransMap = new HashMap();
                    loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                    loanAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                    loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACCT_HEAD"));
                    loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                    loanAuthTransMap.put("LIMIT", dataMap.get("LIMIT"));
                    loanAuthTransMap.put("LOANDEBIT", "LOANDEBIT");
                    loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    loanAuthTransMap.put("BEHAVES_LIKE", dataMap.get("BEHAVES_LIKE"));
                    if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                        loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                    } else {
                        loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                    }
                    if (dataMap.containsKey("OLD_LOAN_NARRATION") && dataMap.get("OLD_LOAN_NARRATION") != null) {
                        loanAuthTransMap.put("OLD_LOAN_NARRATION", dataMap.get("OLD_LOAN_NARRATION"));
                    }
                    loanAuthTransList= loanAuthorizeTimeTransaction(loanAuthTransMap);
                    loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                    loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                    loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                    System.out.println("cashMap :" + cashMap);
                    HashMap shareBalMap = new HashMap();
                    String shareAchd = "";
                    //System.out.println("objTermLoanBorrowerTO.getCustId()>>>" + objTermLoanBorrowerTO.getCustId());
                    shareBalMap.put("CUSTID", objTermLoanBorrowerTO.getCustId());
                    List casteList = ServerUtil.executeQuery("getSelectShareFeeAchdForShareBal", shareBalMap);
                    if (casteList != null && casteList.size() > 0) {
                        shareBalMap = (HashMap) casteList.get(0);
                        shareAchd = CommonUtil.convertObjToStr(shareBalMap.get("SHARE_FEE_ACHD"));
                    }
//                    System.out.println("shareAchd000111>>>" + shareAchd);
//                    System.out.println("recovery000111>>>" + recovery);
                    cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    
                    //Added by Jeffin John on 12-02-2015 for Mantis - 10281
                    HashMap interestMap = getInterestFirst(map, dataMap);
                    if(interestMap != null && interestMap.size()>0){
                        double interest = 0.0;
                        Date toDate = new Date();
                        String acctNum = "";
                        String acctHdId = "";
                        if(interestMap.containsKey("INTEREST")){
                            interest = CommonUtil.convertObjToDouble(interestMap.get("INTEREST"));
                        }
                        if (interestMap.containsKey("ACCT_NUM")) {
                            acctNum = CommonUtil.convertObjToStr(interestMap.get("ACCT_NUM"));
                        }
                        if (interestMap.containsKey("TO_DATE")) {
                            toDate = CommonUtil.getProperDate(curr_dt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("TO_DATE"))));
                        }
                        if (interestMap.containsKey("AC_DEBIT_INT")) {
                            acctHdId = CommonUtil.convertObjToStr(interestMap.get("AC_DEBIT_INT"));
                        }
                        if (interest > 0) {
                            CashTransactionTO objCashTO = new CashTransactionTO();
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setSingleTransId(generateCashId);
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(acctHdId));
                            objCashTO.setLinkBatchId(acctNum);
                            objCashTO.setAuthorizeRemarks("INTEREST");
                            objCashTO.setParticulars(acctNum + ":" + "INTEREST");
                            objCashTO.setLoanHierarchy(String.valueOf("3")); // "3" For Interest
                            objCashTO.setIbrHierarchy("15");
                            objCashTO.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            objCashTO.setInstrumentNo2("LOAN_INTEREST");
                            objCashTO.setTransType(CommonConstants.CREDIT);
                            objCashTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                                objCashTO.setTransModType(TransactionFactory.ADVANCES);
                            } else {
                                objCashTO.setTransModType(TransactionFactory.LOANS);
                            }
                            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                            if (listData != null && listData.size() > 0) {
                                HashMap map1 = (HashMap) listData.get(0);
                                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")
                                        && objCashTO.getTransType().equals(CommonConstants.CREDIT)) {
                                    objCashTO.setAuthorizeStatus_2("");
                                } else {
                                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                }
                            } else {
                                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            }
                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(interest));
                            objCashTO.setAmount(CommonUtil.convertObjToDouble(interest));
                            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                            objCashTO.setTransDt((Date) curr_dt.clone());
                            objCashTO.setStatusDt((Date) curr_dt.clone());
                            objCashTO.setGlTransActNum(acctNum);
                            ArrayList loanTransList = new ArrayList();
                            loanTransList.add(objCashTO);
                            loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanTransList);
                            loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                            cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                            System.out.println("cashMap here:" + cashMap);
                            map.put("LAST_CALC_DT", toDate);
                            map.put("ACCOUNTNO", acctNum);
                            sqlMap.executeUpdate("updateclearBal", map);
                            map.remove("LAST_CALC_DT");
                            map.remove("ACCOUNTNO");
                        }
                    }
                    //Code added by Jeffin John ends here

                    //Added By Suresh
                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                        loanAuthTransList = new ArrayList();
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            String chargeDesc = "";
                            double chargeAmt = 0;
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            chargeDesc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            System.out.println("$#@@$ accHead" + accHead);
                            System.out.println("$#@@$ chargeAmt" + chargeAmt);
                            System.out.println("$#@@$ chargeDesc" + chargeDesc);

                            if (chargeAmt > 0) {
                                if (chargeDesc != null && !chargeDesc.equals("") && chargeDesc.equals("Estimation Fee")) {
                                    loanAuthTransMap.put("SUSPENCE_ACCT_NO", suspenceAccountNo);
                                    loanAuthTransMap.put("SUSPENCE_PROD_ID", suspenceProdId);
                                }
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_HEAD", accHead);
                                loanAuthTransMap.put("LIMIT", String.valueOf(chargeAmt));
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                                    loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    loanAuthTransMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                               // cashTransactionDAO = new CashTransactionDAO();
                                loanAuthTransMap.put("CREDIT_CHARGES", "CREDIT_CHARGES");  
                                
                                //chargeType + " - : "                         
                                loanAuthTransMap.put(CommonConstants.PARTICULARS, chargeDesc + " - : " + CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(loanAuthTransMap));
                               // loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                               // cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                               // System.out.println("cashMap :" + cashMap);
                                //cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            }
                        }
                        if (map.containsKey("serviceTaxDetails")) {
                            HashMap Taxdetails = (HashMap) map.get("serviceTaxDetails");
                            //modifyied by rishad 10/04/2017
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt=0.0;
                            double normalServiceTax = 0.0;
                            // taxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                            if (Taxdetails.containsKey("TOT_TAX_AMT") && Taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(Taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            taxAmt-=(swachhCess+krishikalyanCess);
                            if(normalServiceTax > 0){
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get("TAX_HEAD_ID"));
                                loanAuthTransMap.put("LIMIT", normalServiceTax);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", curr_dt);
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                                loanAuthTransMap.put("INSTRUMENT_NO2", "SERVICETAX_CHARGE");
                                loanAuthTransMap.put("PARTICULARS", "SERVICETAX CHARGE");
                                loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(loanAuthTransMap));
                            }                            
                            if(swachhCess>0){
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID));
                                loanAuthTransMap.put("LIMIT", swachhCess);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", curr_dt);
                                loanAuthTransMap.put("INSTRUMENT_NO2", "CGST");
                                loanAuthTransMap.put("PARTICULARS", "CGST");
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                                loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(loanAuthTransMap));
                            }
                            if(krishikalyanCess>0){
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID));
                                loanAuthTransMap.put("LIMIT", krishikalyanCess);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", curr_dt);
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateCashId);
                                loanAuthTransMap.put("INSTRUMENT_NO2", "SGST");
                                loanAuthTransMap.put("PARTICULARS", "SGST");
                                loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(loanAuthTransMap));}
                            
                        }
                            cashTransactionDAO = new CashTransactionDAO();
                            loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                            loanAuthTransMap.put("BRANCH_CODE", _branchCode);
                            cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                            cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    }
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    loanAuthTransMap = null;
                } else if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    double TotchargeAmt = 0;
                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            double chargeAmt = 0;
                            String chargeType = "";
                            chargeMap = (HashMap) chargeLst.get(i);
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            TotchargeAmt += chargeAmt;
                        }
                    }
                    if (map.containsKey("serviceTaxDetails")) {
                        HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                        double serchargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                        TotchargeAmt += serchargeAmt;
                    }

                    //Added By Jeffin John on 12-02-2015 for Mantis - 10281
                    HashMap interestMap = getInterestFirst(map, dataMap);
                    double interest = 0.0;
                    String acctNum = "";
                    Date toDate = new Date();
                    String acctHdId = "";
                    if (interestMap != null && interestMap.size() > 0) {
                        if (interestMap.containsKey("ACCT_NUM")) {
                            acctNum = CommonUtil.convertObjToStr(interestMap.get("ACCT_NUM"));
                        }
                        if (interestMap.containsKey("TO_DATE")) {
                            toDate = CommonUtil.getProperDate(curr_dt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("TO_DATE"))));
                        }
                        if (interestMap.containsKey("AC_DEBIT_INT")) {
                            acctHdId = CommonUtil.convertObjToStr(interestMap.get("AC_DEBIT_INT"));
                        }
                        if (interestMap.containsKey("INTEREST")) {
                            interest = CommonUtil.convertObjToDouble(interestMap.get("INTEREST"));
                        }
                        if (interest > 0) {
                            TotchargeAmt += interest;
                        }
                    }
                    //Code added by Jeffin John ends here
                    
                    transactionDAO.setLinkBatchID((String) dataMap.get("ACT_NUM"));
                    transactionDAO.setInitiatedBranch(branchId);
                    HashMap crMap = new HashMap();
                    crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                        List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    } else if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                        List oaAcctHead = sqlMap.executeQueryForList("getSAAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    }else if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.ADVANCES)) {
                        List oaAcctHead = sqlMap.executeQueryForList("getADAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    }
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                      //  System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TxTransferTO transferTo = new TxTransferTO();
                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
                     //below block added by rishad for loan renewal purpose 02/09/16 req arised from rbi
                    if (dataMap.containsKey("LOAN_RENEWAL") && CommonUtil.convertObjToStr(dataMap.get("LOAN_RENEWAL")).equals("LOAN_RENEWAL")) {
                     //   interestAmt += TotchargeAmt;
                      double oldLoanAmt=  CommonUtil.convertObjToDouble(dataMap.get("OLD_LOAN_AMT")).doubleValue();
                        
                    }
                    //end
                    txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                    if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                    }
                    txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.PARTICULARS, "disbursement - " + dataMap.get("CR_ACT_NUM"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put("AUTHORIZEREMARKS", "DP");
                    txMap.put("DEBIT_LOAN_TYPE", "DP");
                    System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                    txMap.put("SINGLE_TRANS_ID", generateCashId);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
                    transferTo.setSingleTransId(generateCashId);
                    transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                    txMap.put("SINGLE_TRANS_ID", generateCashId);
                    //txMap.put("TRANS_MOD_TYPE","OA");
                    if (dataMap.containsKey("OLD_LOAN_NARRATION") && dataMap.get("OLD_LOAN_NARRATION") != null) {
                        txMap.put(TransferTrans.NARRATION, CommonUtil.convertObjToStr(dataMap.get("OLD_LOAN_NARRATION")));
                    }
                      interestAmt = interestAmt - TotchargeAmt;
                    
                     if (dataMap.containsKey("LOAN_RENEWAL") && CommonUtil.convertObjToStr(dataMap.get("LOAN_RENEWAL")).equals("LOAN_RENEWAL")) {
                     //   interestAmt += TotchargeAmt;
                      double oldLoanAmt=  CommonUtil.convertObjToDouble(dataMap.get("OLD_LOAN_AMT")).doubleValue();
                      interestAmt=interestAmt-oldLoanAmt;
                     HashMap renewalcrMap = new HashMap();
                    renewalcrMap.put("ACT_NUM", dataMap.get("RENWAL_ACT_NUM"));
                    if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                        List oaAcctHead1 = sqlMap.executeQueryForList("getAccNoProdIdDet", renewalcrMap);
                        if (oaAcctHead1 != null && oaAcctHead1.size() > 0) {
                            renewalcrMap = (HashMap) oaAcctHead1.get(0);
                        }
                    } else if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                        List oaAcctHead1 = sqlMap.executeQueryForList("getSAAccNoProdIdDet", renewalcrMap);
                        if (oaAcctHead1 != null && oaAcctHead1.size() > 0) {
                            renewalcrMap = (HashMap) oaAcctHead1.get(0);
                        }
                    }else if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.ADVANCES)) {
                        List oaAcctHead1 = sqlMap.executeQueryForList("getADAccNoProdIdDet", renewalcrMap);
                        if (oaAcctHead1 != null && oaAcctHead1.size() > 0) {
                           renewalcrMap= (HashMap) oaAcctHead1.get(0);
                        }
                    }
                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("RENWAL_ACT_NUM"));
                    txMap.put(TransferTrans.CR_AC_HD, renewalcrMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, renewalcrMap.get("PROD_ID"));
                         
                    if (interBranchCodeMap != null && !interBranchCodeMap.isEmpty()) {
                        txMap.put(TransferTrans.CR_BRANCH, interBranchCodeMap.get("BRANCH_CODE"));
                    } else {
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                    }
//                      txMap.put("AUTHORIZEREMARKS", "DP");
//                      txMap.put("DEBIT_LOAN_TYPE", "DP");
                       if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("RENWAl_PROD_TYPE")) {
                        if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        } else if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                        }else if (CommonUtil.convertObjToStr(dataMap.get("RENWAl_PROD_TYPE")).equals(TransactionFactory.ADVANCES)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        }
                    }
//                    txMap.put("AUTHORIZEREMARKS", "DP");
//                    txMap.put("DEBIT_LOAN_TYPE", "DP");
                   
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, oldLoanAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
                    transferTo.setSingleTransId(generateCashId);
                    transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    }
                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));                    
                    if (interBranchCodeMap != null && !interBranchCodeMap.isEmpty()) {
                        txMap.put(TransferTrans.CR_BRANCH, interBranchCodeMap.get("BRANCH_CODE"));
                    } else {
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                    }
                    txMap.put("AUTHORIZEREMARKS", "DP");
                    txMap.put("DEBIT_LOAN_TYPE", "DP");
                    if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("PROD_TYPE")) {
                        if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        } else if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                        }else if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.ADVANCES)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        }
                    }
                    if(!((CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"))).equalsIgnoreCase(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"))))){                        
                        txMap.put("TERM_LOANS_INTER_BRANCH_TRANS", "TERM_LOANS_INTER_BRANCH_TRANS");  
                        txMap.put("INITIATED_BRANCH",_branchCode);
                    }                     
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
                    transferTo.setSingleTransId(generateCashId);
                    transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                    //Added By Jeffin John on 12-02-2015 for Mantis - 10281
                    if (interest > 0) {
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        if (dataMap.containsKey("BEHAVES_LIKE") && dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                        } else {
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                        }
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acctHdId));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                        txMap.put(TransferTrans.PARTICULARS, acctNum + ":" + "INTEREST");
                        txMap.put("SINGLE_TRANS_ID", generateCashId);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interest);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                        transferTo.setAuthorizeRemarks("INTEREST");
                        transferTo.setSingleTransId(generateCashId);
                        transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        map.put("LAST_CALC_DT", toDate);
                        map.put("ACCOUNTNO", acctNum);
                        sqlMap.executeUpdate("updateclearBal", map);
                        map.remove("LAST_CALC_DT");
                        map.remove("ACCOUNTNO");
                    }
                    //Code added by Jeffin John ends here

                    if (TotchargeAmt <= 0) {
                        transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                    }

                    //Added By Suresh

                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            double chargeAmt = 0;
                            String chargeType = "";
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            System.out.println("$#@@$ accHead" + accHead);
                            System.out.println("$#@@$ chargeAmt" + chargeAmt);
                            if (chargeAmt > 0) {
                                txMap = new HashMap();
                                //commented by rishad 23/08/2014
                                // transferList = new ArrayList();
                                TotchargeAmt += chargeAmt;
                                // transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                                txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                if (interBranchCodeMap != null && !interBranchCodeMap.isEmpty()) {
                                    txMap.put(TransferTrans.DR_BRANCH, interBranchCodeMap.get("BRANCH_CODE"));
                                } else {
                                    txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                                }
                                txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("DR_INST_TYPE", "VOUCHER");
                                //System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                                txMap.put(TransferTrans.CR_AC_HD, accHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                                txMap.put("TRANS_MOD_TYPE", "OA");
                                if (dataMap.containsKey("BEHAVES_LIKE") && dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                txMap.put("SINGLE_TRANS_ID", generateCashId);
                                if (chargeType != null && !chargeType.equals("") && chargeType.equals("Estimation Fee")) {
                                    txMap.put(TransferTrans.CR_ACT_NUM, suspenceAccountNo);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                                    txMap.put(transferTrans.CR_PROD_ID, suspenceProdId);
                                }
                                //commented by rishad for blocking debiting charge from operative on 23/08/2014
                                //  transferTo = transactionDAO.addTransferDebitLocal(txMap, chargeAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
                                transferTo.setSingleTransId(generateCashId);
                                transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                //    transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, chargeType + " - : " + dataMap.get("ACT_NUM"));
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
                                transferTo.setSingleTransId(generateCashId);
                                transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            }
                        }
                        if (map.containsKey("serviceTaxDetails")) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            //modified by rishad 10/04/2017
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt=0.0;
                            double normalServiceTax = 0.0;
                            // taxAmt = CommonUtil.convertObjToDouble(serTaxMap.get("TOT_TAX_AMT"));
                            if (taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            taxAmt-=(swachhCess+krishikalyanCess);
                            String seraccHead = "";
                            double serchargeAmt = 0;
                            seraccHead = CommonUtil.convertObjToStr(taxdetails.get("TAX_HEAD_ID"));
                            double chargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.CR_AC_HD, seraccHead);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                            txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                            txMap.put(TransferTrans.PARTICULARS, "Service Tax" + " - : " + dataMap.get("ACT_NUM"));
                            if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            } else {
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            transferTo.setSingleTransId(generateCashId);
                            transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            transferList.add(transferTo);
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.CR_AC_HD,  CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                            txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                            txMap.put(TransferTrans.PARTICULARS, "CGST" + " - : " + dataMap.get("ACT_NUM"));
                            if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            } else {
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            transferTo.setSingleTransId(generateCashId);
                            transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            transferList.add(transferTo);
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                            txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                            txMap.put(TransferTrans.PARTICULARS, "SGST" + " - : " + dataMap.get("ACT_NUM"));
                            if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            } else {
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                            }
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            transferTo.setSingleTransId(generateCashId);
                            transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        }
                        transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                    }


                    transList = null;
                    transactionDAO = null;
                    transferDao = null;
                    txMap = null;
                    acHeads = null;
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }

    private ArrayList loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                if(dataMap.get("BEHAVES_LIKE")!=null && dataMap.get("BEHAVES_LIKE").equals("OD"))
                {
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.ADVANCES));
                   // if(dataMap.get("INSTRUMENTTYPE")==null && dataMap.get("INSTRUMENTTYPE").equals(""))
                       objCashTO.setInstType("VOUCHER");
                    //objCashTO.setTransModType(CommonConstants.ADVANCES);
                }
                else{
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                //objCashTO.setTransModType(CommonConstants.LOANS);
                }
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setParticulars("To Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                if(dataMap.containsKey("SUSPENCE_ACCT_NO") && dataMap.containsKey("SUSPENCE_PROD_ID")){
                    objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("SUSPENCE_ACCT_NO")));
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.SUSPENSE));
                    objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("SUSPENCE_PROD_ID")));
                    objCashTO.setTransModType(TransactionFactory.SUSPENSE);
                }
           }
             //Added by Sreekrishnan for Cashier
             List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
             if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y") 
                        && objCashTO.getTransType().equals(CommonConstants.CREDIT) ){
                    objCashTO.setAuthorizeStatus_2("");
                }else
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }else{
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(curr_dt);

            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            if(dataMap.get("BEHAVES_LIKE")!=null && dataMap.get("BEHAVES_LIKE").equals("OD"))
                {
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.ADVANCES));
                   // if(dataMap.get("INSTRUMENTTYPE")==null && dataMap.get("INSTRUMENTTYPE").equals(""))
                       objCashTO.setInstType("VOUCHER");
                    //objCashTO.setTransModType("");
                }
            else{
            //objCashTO.setTransModType(CommonConstants.LOANS);
            }
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            //akhil@
            objCashTO.setScreenName("Loans/Advances Account Opening");
            if(dataMap.containsKey("OLD_LOAN_NARRATION")&&dataMap.get("OLD_LOAN_NARRATION")!=null){
            	objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("OLD_LOAN_NARRATION")));
            }
            //objCashTO.setNarration(acct_No);
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }
//Added By Chithra
    private CashTransactionTO loanAuthorizeTimeTransactionForCashObj(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.ADVANCES));
                    objCashTO.setInstType("VOUCHER");
                } else {
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                }
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setParticulars("To Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            } else {
             
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransType(CommonConstants.CREDIT);
                if (dataMap.containsKey(CommonConstants.PARTICULARS) && dataMap.get(CommonConstants.PARTICULARS) != null) {
                    objCashTO.setParticulars("By Cash : " + dataMap.get(CommonConstants.PARTICULARS));
                } else {
                    objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
                }
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                if (dataMap.containsKey("SUSPENCE_ACCT_NO") && dataMap.containsKey("SUSPENCE_PROD_ID")) {
                    objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("SUSPENCE_ACCT_NO")));
                    objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.SUSPENSE));
                    objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("SUSPENCE_PROD_ID")));
                    objCashTO.setTransModType(TransactionFactory.SUSPENSE);
                }
            }
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")
                        && objCashTO.getTransType().equals(CommonConstants.CREDIT)) {
                    objCashTO.setAuthorizeStatus_2("");
                } else {
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                }
            } else {
                objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            }
            if (dataMap.containsKey("MANUAL_AUTHORIZE")) {//Added By Suresh R 23-Aug-2019 Refer By Jithesh
                objCashTO.setAuthorizeStatus_2("");
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(curr_dt);
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            //if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) { Commended by sreekrishnan
             //   objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.ADVANCES));
             //   objCashTO.setInstType("VOUCHER");
            //} else {
           // }
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            //objCashTO.setScreenName("Loans/Advances Account Opening");
            objCashTO.setScreenName("KCC Renewal");
            if (dataMap.containsKey("INSTRUMENT_NO2") && dataMap.get("INSTRUMENT_NO2") != null) {
             objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_NO2")));   
            }
            if (dataMap.containsKey("OLD_LOAN_NARRATION") && dataMap.get("OLD_LOAN_NARRATION") != null) {
                objCashTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("OLD_LOAN_NARRATION")));
            }
        }
        return objCashTO;
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
//        int len=10;
//        where.put("PROD_ID", prod_id);
//        List lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
//        if(lst != null && lst.size() > 0){
//            mapData = (HashMap)lst.get(0);
//        }
//        if (mapData.containsKey("PREFIX")) {
//            strPrefix = (String) mapData.get("PREFIX");
//        }
//        int numFrom = strPrefix.trim().length();
//        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//        System.out.println("@@@@@@@@"+newID);
//        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE")))+1);
//        System.out.println("@@@@@@@@"+nxtID);
//        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//        where.put("VALUE", nxtID);
//        sqlMap.executeUpdate("updateNextIdForLoan", where);
//        newLoan = true;
        String genID = null;
        int len = 13;
        where.put("PROD_ID", prod_id);
        where.put(CommonConstants.BRANCH_ID, _branchCode);
//        List lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
        List lst = (List) sqlMap.executeQueryForList("getCoreBankNextActNum", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            //        sqlMap.executeUpdate("updateNextId", where);   
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        } else {
            len = 10;
            lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
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
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            sqlMap.executeUpdate("updateNextIdForLoan", where);
        }
        newLoan = true;
        return genID;

    }
    
    
     private String getRenewalId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "KCC_RENEWAL_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);       
        String standingId = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        return standingId;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            
            userId = CommonUtil.convertObjToStr(map.get("USER"));
            String renewalId = getRenewalId();
            sqlMap.startTransaction(); 
            if(kccToList != null && kccToList.size() > 0){
                for(int i=0; i<kccToList.size(); i++){
                  KccRenewalTO objKccRenewalTO = (KccRenewalTO)kccToList.get(i);
                  objKccRenewalTO.setRenewalId(renewalId);
                  objKccRenewalTO.setRiskFundProcessStatus("N");
                  HashMap updatemap = new HashMap();
                    updatemap.put("ACCT_NUM", objKccRenewalTO.getActNum());
                    updatemap.put("FROM_DT", objKccRenewalTO.getFromDt());
                    updatemap.put("TO_DT", objKccRenewalTO.getToDt());
                    updatemap.put("LIMIT", objKccRenewalTO.getLimit());
                    updatemap.put("BORROW_NO", objKccRenewalTO.getBorrowNo());
                    updatemap.put("RENEW_DT", curr_dt.clone());
                    sqlMap.executeUpdate("insertKCCMultipleRenewalSanctionDetails", updatemap);
                    sqlMap.executeUpdate("insertKCCMultipleRenewalFacilityDetails", updatemap);
                    sqlMap.executeUpdate("updateTermLoanSanctionDataForKCC", updatemap);
                    //sqlMap.executeUpdate("updateAcctAvailableBalForKCCRenewal", updatemap);     
                    sqlMap.executeUpdate("insertKccRenewalDetailsTO", objKccRenewalTO);
                }
            }
            resultMap.put("RENEWAL_ID",renewalId);   
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
private void updateVehicleTableDetails() throws Exception {
        System.out.println("updateVehicleTableDetails!!!!!!!!!!!" + vehicleTableDetails);
        if (vehicleTableDetails != null && vehicleTableDetails.size() > 0) {

            ArrayList addList = new ArrayList(vehicleTableDetails.keySet());
            //System.out.println("vehicleleDetails!!!!!!!!!!!!!!" + addList);
            TermLoanSecurityVehicleTO objVehicleTypeTO = null;
            for (int i = 0; i < vehicleTableDetails.size(); i++) {
                objVehicleTypeTO = new TermLoanSecurityVehicleTO();
                objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTableDetails.get(addList.get(i));

                //objMemberTypeTO.setStatusDt(new java.sql.Date(curr_dt.getTime()));
                if (objVehicleTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    //System.out.println("xvxdfgdg" + "objTO.getApplNo()!!!!!!!!!!!!" +acct_No);
                    objVehicleTypeTO.setAcctNum(acct_No);
                    objVehicleTypeTO.setBranchCode(_branchCode);
                    objVehicleTypeTO.setStatusDt((Date)curr_dt.clone());
                    //System.out.println("insertobjobjVehicleTypeTO111>>>" + objVehicleTypeTO);
                    sqlMap.executeUpdate("insertTermLoanSecurityVehicleTO", objVehicleTypeTO);
                } else {
                    objVehicleTypeTO.setBranchCode(_branchCode);
                    objVehicleTypeTO.setAcctNum(acct_No);
                   // System.out.println("updateobjobjVehicleTypeTOTO111>>>" + objVehicleTypeTO);
                    sqlMap.executeUpdate("updateTermLoanSecurityVehicleTO", objVehicleTypeTO);
                }
            }
        }
        if (deletedVehicleTableValues!= null && deletedVehicleTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedVehicleTableValues.keySet());
            TermLoanSecurityVehicleTO objVehicleTO = null;
            for (int i = 0; i < deletedVehicleTableValues.size(); i++) {
                objVehicleTO = new TermLoanSecurityVehicleTO();
                objVehicleTO = (TermLoanSecurityVehicleTO) deletedVehicleTableValues.get(addList.get(i));
                objVehicleTO.setAcctNum(acct_No);
                objVehicleTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("deleteTermLoanSecurityVehicleTO", objVehicleTO);
            }
        }
    }
    private void updateMemberTableDetails() throws Exception {

        if (memberTableDetails != null && memberTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            TermLoanSecurityMemberTO objMemberTypeTO = null;
            for (int i = 0; i < memberTableDetails.size(); i++) {
                objMemberTypeTO = new TermLoanSecurityMemberTO();
                objMemberTypeTO = (TermLoanSecurityMemberTO) memberTableDetails.get(addList.get(i));
                objMemberTypeTO.setStatusDt(curr_dt);
                if (objMemberTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    //objMemberTypeTO.setAcctNum(acct_No);
                    objMemberTypeTO.setBranchCode(_branchCode);
                    objMemberTypeTO.setAuthorizedBy(userId);
                    objMemberTypeTO.setAuthorizedStatus("AUTHORIZED");
                    objMemberTypeTO.setAuthorizedDt(curr_dt);
                    objMemberTypeTO.setStatus("CREATED");
                    objMemberTypeTO.setStatusBy(userId);
                    objMemberTypeTO.setStatusDt(curr_dt);
                    sqlMap.executeUpdate("insertTermLoanSecurityMemberTO", objMemberTypeTO);
                    HashMap getActNoMap = new HashMap();
                    getActNoMap.put("AUTHORIZESTATUS","AUTHORIZED");
                    getActNoMap.put("USER_ID",userId);
                    getActNoMap.put("AUTHORIZEDT",curr_dt);
                    getActNoMap.put("ACCT_NUM",objMemberTypeTO.getAcctNum());
                    sqlMap.executeUpdate("authorizeMemberDetails", getActNoMap);
                } else {
                    objMemberTypeTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("updateTermLoanSecurityMemberTO", objMemberTypeTO);
                }
            }
        }
        if (deletedMemberTableValues != null && deletedMemberTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedMemberTableValues.keySet());
            TermLoanSecurityMemberTO objMemberTypeTO = null;
            for (int i = 0; i < deletedMemberTableValues.size(); i++) {
                objMemberTypeTO = new TermLoanSecurityMemberTO();
                objMemberTypeTO = (TermLoanSecurityMemberTO) deletedMemberTableValues.get(addList.get(i));
                sqlMap.executeUpdate("deleteTermLoanSecurityMemberTO", objMemberTypeTO);
            }
        }
    }

    private void updateCollateralTableDetails() throws Exception {
        System.out.println("collateralTableDetails :: "+ collateralTableDetails);
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = null;
            for (int i = 0; i < collateralTableDetails.size(); i++) {
                objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
                objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralTableDetails.get(addList.get(i));
                objTermLoanSecurityLandTO.setStatusDt(curr_dt);
                if (objTermLoanSecurityLandTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    //objTermLoanSecurityLandTO.setAcctNum(acct_No);
                    //objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                    //System.out.println("objTermLoanSecurityLandTO111>>>>" + objTermLoanSecurityLandTO);
                    objTermLoanSecurityLandTO.setStatus("CREATED");
                    objTermLoanSecurityLandTO.setStatusBy(userId);
                    objTermLoanSecurityLandTO.setStatusDt(curr_dt);
                    objTermLoanSecurityLandTO.setAuthorizedStatus("AUTHORIZED");
                    objTermLoanSecurityLandTO.setAuthorizedBy(userId);
                    objTermLoanSecurityLandTO.setAuthorizedDt(curr_dt);
                    sqlMap.executeUpdate("insertTermLoanSecurityLandTO", objTermLoanSecurityLandTO);
                    HashMap getActNoMap = new HashMap();
                    getActNoMap.put("AUTHORIZESTATUS","AUTHORIZED");
                    getActNoMap.put("USER_ID",userId);
                    getActNoMap.put("AUTHORIZEDT",curr_dt);
                    getActNoMap.put("ACCT_NUM",objTermLoanSecurityLandTO.getAcctNum());
                    sqlMap.executeUpdate("authorizeCollateralDetails", getActNoMap);
                } else {
                    objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                    objTermLoanSecurityLandTO.setDocumentDt(setProperDtFormat(objTermLoanSecurityLandTO.getDocumentDt()));
                    sqlMap.executeUpdate("updateTermLoanSecurityLandTO1", objTermLoanSecurityLandTO);
                }
            }
        }
        if (deletedCollateralTableValues != null && deletedCollateralTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedCollateralTableValues.keySet());
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = null;
            for (int i = 0; i < deletedCollateralTableValues.size(); i++) {
                objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
                objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) deletedCollateralTableValues.get(addList.get(i));
                sqlMap.executeUpdate("deleteTermLoanSecurityLandTO1", objTermLoanSecurityLandTO);
            }
        }
    }

    

    private void updateLosTypeDetails() throws Exception {

        if (losTableDetails != null && losTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(losTableDetails.keySet());
            TermLoanLosTypeTO objTermLoanLosTypeTO = null;
            for (int i = 0; i < losTableDetails.size(); i++) {
                objTermLoanLosTypeTO = new TermLoanLosTypeTO();
                objTermLoanLosTypeTO = (TermLoanLosTypeTO) losTableDetails.get(addList.get(i));
                objTermLoanLosTypeTO.setStatusDt(curr_dt);
                if (objTermLoanLosTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
//                    objTermLoanDepositTypeTO.setAcctNum(acct_No);
                    objTermLoanLosTypeTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("insertLosSecurityTableDetails", objTermLoanLosTypeTO);
                } else {

                    sqlMap.executeUpdate("updateTermLoanLosTypeTO", objTermLoanLosTypeTO);
                }
            }
        }
        if (deletedLosTableValues != null && deletedLosTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedLosTableValues.keySet());
            TermLoanLosTypeTO objLosTypeTO = null;
            for (int i = 0; i < deletedLosTableValues.size(); i++) {
                objLosTypeTO = new TermLoanLosTypeTO();
                objLosTypeTO = (TermLoanLosTypeTO) deletedLosTableValues.get(addList.get(i));
                sqlMap.executeUpdate("deleteTermLoanSecurityLosTO", objLosTypeTO);
            }
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
            executeUpdate("deleteTermLoanCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);



            executeAllTabQuery();



            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
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
                    executeUpdate("insertTermLoanJointAcctTO", objTermLoanJointAcctTO);
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
            throw e;
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
           // System.out.println("getDepositLienUnlienLoanTO##" + lst);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    DepositLienTO depLienTO = (DepositLienTO) lst.get(i);
                    lienCancelMap.put("DEPOSIT_ACT_NUM", depLienTO.getDepositNo());
                    lienCancelMap.put("SUBNO", depLienTO.getDepositSubNo());
                    lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                    lienCancelMap.put("BALANCE", depLienTO.getLienAmount());
                    lienCancelMap.put("AMOUNT", depLienTO.getLienAmount());
                    lienCancelMap.put("LIENNO", depLienTO.getLienNo());
                    lienCancelMap.put("AUTHORIZEDT", curr_dt);//curr_dt);
                    lienCancelMap.put("AUTHORIZE_DATE", curr_dt);//curr_dt);
                    lienCancelMap.put("COMMAND_STATUS", "CREATED");
                    lienCancelMap.put("STATUS", "CREATED");
                    DepositLienDAO depositLiendao = new DepositLienDAO();
                    lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    lienCancelMap.put("ACTION", CommonConstants.STATUS_AUTHORIZED);
                    lienCancelMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                    lienCancelMap.put("LIENAMOUNT", new Double(0));//depLienTO.getLienAmount()
                    lienCancelMap.put("STATUS", "UNLIENED");
                    lienCancelMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                    depositLiendao.setCallFromOtherDAO(true);
                    System.out.println("lienCancelbefore dao" + lienCancelMap);
                    depositLiendao.execute(lienCancelMap);

                }
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
                executeOneTabQueries("TermLoanSanctionTO", objTermLoanSanctionTO);
                logDAO.addToLog(logTO);
                objTermLoanSanctionTO = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanSanctionTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeDailyLoanSanctionTabQuery() throws Exception {
        DailyLoanSanctionDetailsTO objDailyLoanSanctionDetailsTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (dailyLoanMap != null) {
                keySet = dailyLoanMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanSanctionTO from the dailyLoanMap
                for (int i = dailyLoanMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objDailyLoanSanctionDetailsTO = (DailyLoanSanctionDetailsTO) dailyLoanMap.get(objKeySet[j]);
                    objDailyLoanSanctionDetailsTO.setLoanAcctNum(acct_No);
                    logTO.setData(objDailyLoanSanctionDetailsTO.toString());
                    logTO.setPrimaryKey(objDailyLoanSanctionDetailsTO.getKeyData());
                    logTO.setStatus(objDailyLoanSanctionDetailsTO.getCommand());
                    executeOneTabQueries("DailyLoanSanctionDetails", objDailyLoanSanctionDetailsTO);
                    logDAO.addToLog(logTO);
                    objDailyLoanSanctionDetailsTO = null;
                }
                keySet = null;
                objKeySet = null;
                objDailyLoanSanctionDetailsTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeOTSLoanSanctionTabQuery() throws Exception {
        TermLoanCourtDetailsTO objTermLoanCourtDetailsTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (oTSMap != null) {
                keySet = oTSMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanSanctionTO from the oTSMap
                for (int i = oTSMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanCourtDetailsTO = (TermLoanCourtDetailsTO) oTSMap.get(objKeySet[j]);
                    objTermLoanCourtDetailsTO.setAcctNum(acct_No);
                    logTO.setData(objTermLoanCourtDetailsTO.toString());
//                logTO.setPrimaryKey(objTermLoanCourtDetailsTO.getKeyData());
                    logTO.setStatus(objTermLoanCourtDetailsTO.getStatus());
                    createCourtInstallmentDetails(objTermLoanCourtDetailsTO);
                    executeOneTabQueries("TermLoanCourtDetailsTO", objTermLoanCourtDetailsTO);
                    logDAO.addToLog(logTO);
                    objTermLoanCourtDetailsTO = null;
                }
                keySet = null;
                objKeySet = null;
                objTermLoanCourtDetailsTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //amount validation also need to add
    private void createCourtInstallmentDetails(TermLoanCourtDetailsTO objTermLoanCourtDetailsTO) throws Exception {
        System.out.println("objTermLoanCourtDetailsTO###" + objTermLoanCourtDetailsTO);
        double settlementAmt = CommonUtil.convertObjToDouble(objTermLoanCourtDetailsTO.getSettlementAmt()).doubleValue();
        double installmentAmt = CommonUtil.convertObjToDouble(objTermLoanCourtDetailsTO.getInstallmentAmt()).doubleValue();
        String command = CommonUtil.convertObjToStr(objTermLoanCourtDetailsTO.getCommand());
        TermLoanOTSInstallmentTO obj = new TermLoanOTSInstallmentTO();
        TermLoanOTSInstallmentTO singleInstallment = new TermLoanOTSInstallmentTO();
        if (command.equals("DELETE") || command.equals("UPDATE")) {
            singleInstallment.setAcctNum(objTermLoanCourtDetailsTO.getAcctNum());
            singleInstallment.setStatus("DELETE");
            singleInstallment.setSlno(objTermLoanCourtDetailsTO.getSlno());
            sqlMap.executeUpdate("updateTermLoanOTSInstallmentTO", singleInstallment);
        }
        if (command.equals("INSERT") || command.equals("UPDATE")) {
            int noInstallment = CommonUtil.convertObjToInt(objTermLoanCourtDetailsTO.getInstallmentNo());
            Date instDate = objTermLoanCourtDetailsTO.getFirstInstallmentDt();
            for (int i = 0; i < noInstallment; i++) {
                singleInstallment = new TermLoanOTSInstallmentTO();
                singleInstallment.setAcctNum(objTermLoanCourtDetailsTO.getAcctNum());

                if (i == noInstallment - 1) {
                    singleInstallment.setInstallmentAmt(new Double(settlementAmt));
                } else {
                    singleInstallment.setInstallmentAmt(objTermLoanCourtDetailsTO.getInstallmentAmt());
                }
                settlementAmt -= installmentAmt;
                singleInstallment.setInstllmentNo(objTermLoanCourtDetailsTO.getInstallmentNo());
                singleInstallment.setInstallmentDate(instDate);
                singleInstallment.setSlno(objTermLoanCourtDetailsTO.getSlno());
                singleInstallment.setStatus(objTermLoanCourtDetailsTO.getStatus());
                if (settlementAmt < 0) {
                    settlementAmt = 0;
                }

                sqlMap.executeUpdate("insertTermLoanOTSInstallmentTO", singleInstallment);
                instDate = DateUtil.addDays(instDate, CommonUtil.convertObjToInt(objTermLoanCourtDetailsTO.getFreq()));
            }
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
                if (objTermLoanSanctionFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && objTermLoanSanctionFacilityTO.getOdRenewal().length() > 0) {
                    insertODSanctionDetails(objTermLoanSanctionFacilityTO);
                }
                executeOneTabQueries("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTO);
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
            throw e;
        }
    }

    private void insertODSanctionDetails(TermLoanSanctionFacilityTO obj) throws Exception {
        HashMap map = new HashMap();
        map.put("BORROW_NO", obj.getBorrowNo());
        sqlMap.executeUpdate("insertLoansRenewalSanctionDetails", map);
    }

    private boolean checkGoldSecurity(TermLoanFacilityTO objTermLoanFacilityTO) throws Exception {
        HashMap map = new HashMap();
        boolean flag = false;
        map.put("GROSS_WEIGHT", CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtGrossWeight()));
        map.put("NET_WEIGHT", CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtNetWeight()));
        map.put("PARTICULARS", CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtJewelleryDetails()));
        map.put("ACCT_NUM", CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum()));
        List lst = sqlMap.executeQueryForList("getGoldSecurityExist", map);
       // System.out.println("checkGoldSecurity##" + lst);
        if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);  
                flag = true;
        }else{
            flag = false;
        }
        return flag;
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
                    Date tempDt = (Date) curr_dt.clone();
                    Date lastIntCalcDt = DateUtil.addDays(getProperDateFormat(objTermLoanFacilityTO.getAccOpenDt()), -1);
                    tempDt.setDate(lastIntCalcDt.getDate());
                    tempDt.setMonth(lastIntCalcDt.getMonth());
                    tempDt.setYear(lastIntCalcDt.getYear());
                    objTermLoanFacilityTO.setLastIntCalcDt(tempDt);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("insertTermLoanFacilityTO", objTermLoanFacilityTO);
                    insertCustomerHistory(objTermLoanFacilityTO);
                    if (objTermLoanFacilityTO.getTxtValueOfGold() != null && objTermLoanFacilityTO.getTxtValueOfGold() > 0) {
                        GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
                        //objTermLoanSecurityTO.setSlNo("0");
                        objTermLoanSecurityTO.setAcctNum(acct_No);
                        objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                        objTermLoanSecurityTO.setAsOn((Date)curr_dt.clone());
                      
                        objTermLoanSecurityTO.setPurity("");
                        objTermLoanSecurityTO.setMarketRate("");
                        objTermLoanSecurityTO.setSecurityValue(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtValueOfGold()));   ///check
                        objTermLoanSecurityTO.setMargin("");
                        objTermLoanSecurityTO.setMarginAmt("");
                        objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAvailableBalance()));
                        objTermLoanSecurityTO.setAppraiserId(""); //getCbmRenewalAppraiserId().getKeyForSelected()));
                        objTermLoanSecurityTO.setParticulars(objTermLoanFacilityTO.getTxtJewelleryDetails());
                        objTermLoanSecurityTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                        objTermLoanSecurityTO.setStatusDt(getProperDateFormat(objTermLoanFacilityTO.getStatusDt()));
                        //objTermLoanSecurityTO.setNoofPacket("1");
                        executeUpdate("insertGoldLoanSecurityTO",objTermLoanSecurityTO);
                        System.out.println("objTermLoanSecurityTO ----------- >>"+objTermLoanSecurityTO);
                    }
//                    logDAO.addToLog(logTO);
                } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    acct_No = objTermLoanFacilityTO.getAcctNum();
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    if (objTermLoanFacilityTO.getAuthorizeStatus2().equals("ENHANCE") || objTermLoanFacilityTO.getAuthorizeStatus2().equals("ODRENEWAL")) {
                        insertRenewalRecords(objTermLoanFacilityTO);
                    }
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    System.out.println("objTermLoanFacilityTO#####" + objTermLoanFacilityTO);
                    if (objTermLoanFacilityTO.getAuthorizeStatus1() != null && objTermLoanFacilityTO.getAuthorizeStatus1().length() > 0) {
                        executeUpdate("updateTermLoanFacilityTOMaterializedView", objTermLoanFacilityTO);//dont want to updte available balance
                    } else {
                        executeUpdate("updateTermLoanFacilityTO", objTermLoanFacilityTO);
                    }
                    insertCustomerHistory(objTermLoanFacilityTO);
                    //Added by sreekrishnan
                    if (objTermLoanFacilityTO.getTxtValueOfGold() != null && objTermLoanFacilityTO.getTxtValueOfGold() > 0) {
                        if(checkGoldSecurity(objTermLoanFacilityTO)){
                            GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
                          
                            objTermLoanSecurityTO.setPurity("");
                            objTermLoanSecurityTO.setMarketRate("");
                            objTermLoanSecurityTO.setSecurityValue(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getTxtValueOfGold()));   ///check
                            objTermLoanSecurityTO.setMargin("");
                            objTermLoanSecurityTO.setMarginAmt("");
                            objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAvailableBalance()));
                            objTermLoanSecurityTO.setAppraiserId(""); //getCbmRenewalAppraiserId().getKeyForSelected()));
                            objTermLoanSecurityTO.setParticulars(objTermLoanFacilityTO.getTxtJewelleryDetails());
                            objTermLoanSecurityTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                            objTermLoanSecurityTO.setStatusDt(getProperDateFormat(objTermLoanFacilityTO.getStatusDt()));
//                            objTermLoanSecurityTO.setNoofPacket("1");
                            executeUpdate("insertGoldLoanSecurityTO",objTermLoanSecurityTO);                            
                            System.out.println("objTermLoanSecurityTO ----------- >>"+objTermLoanSecurityTO);
                            //Added by sreekrishnan
                            objTermLoanSecurityTO.setIsRelease("Y");
                            objTermLoanSecurityTO.setReleaseDt((Date)curr_dt.clone());
                            executeUpdate("updateGoldReleaseStatus",objTermLoanSecurityTO); 
                        }
                    }
//                    logDAO.addToLog(logTO);
                } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityDepositDetails", objTermLoanFacilityTO);

                    executeUpdate("deleteTermLoanSecurityLosDetails", objTermLoanFacilityTO);
                    executeUpdate("deleteTermLoanFacilityTO", objTermLoanFacilityTO);

                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityMemberDetails", objTermLoanFacilityTO);

                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityCollateralDetails", objTermLoanFacilityTO);

                    logDAO.addToLog(logTO);
                    HashMap lienCancelMap = new HashMap();
                    lienCancelMap.put("LOAN_NO", objTermLoanFacilityTO.getAcctNum());
                    lienCancelMap.put("PROD_ID", objTermLoanFacilityTO.getProdId());
                    lienCancel(lienCancelMap);
                    insertCustomerHistory(objTermLoanFacilityTO);

                }
                if (objSMSSubscriptionTO != null) {
                    updateSMSSubscription(objTermLoanFacilityTO);
                }


                if (productCategory.equals("PADDY_LOAN")) {
                    HashMap paddyUpdateMap = new HashMap();
                    paddyUpdateMap.put("ACT_NUM", objTermLoanFacilityTO.getAcctNum());
                    paddyUpdateMap.put("LOAN_STATUS", "NEW");
                    paddyUpdateMap.put("LOAN_GIVEN", "Y");
                    if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        paddyUpdateMap.put("LOAN_STATUS", "");
                        paddyUpdateMap.put("LOAN_GIVEN", "N");
                    }
                    paddyUpdateMap.put("CND_NO", keyValue);
                    executeUpdate("updatePaddyLoanStatus", paddyUpdateMap);
                }

                if (productCategory.equals("MDS_LOAN")) {
                    HashMap mdsUpdateMap = new HashMap();
                    mdsUpdateMap.put("ACT_NUM", objTermLoanFacilityTO.getAcctNum());
                    mdsUpdateMap.put("LOAN_STATUS", "NEW");
                    mdsUpdateMap.put("LOAN_GIVEN", "Y");
                    if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        mdsUpdateMap.put("LOAN_STATUS", "");
                        mdsUpdateMap.put("LOAN_GIVEN", "N");
                    }
                    String chittalNo = "";
                    String subNo = "";
                    if (keyValue.indexOf("_") != -1) {
                        chittalNo = keyValue.substring(0, keyValue.indexOf("_"));
                        subNo = keyValue.substring(keyValue.indexOf("_") + 1, keyValue.length());
                    }

//                    mdsUpdateMap.put("SUB_NO", subNo);
                    System.out.println("multipleDeposit>>>11???>>" + multipleDeposit);
                    Set set = multipleDeposit.keySet();
                    Object objKeySetMulitiDep[] = (Object[]) set.toArray();
                    String chittalNo2 = "";
                    String subNo2 = "";
                    for (int z = 0; z < multipleDeposit.size(); z++) {
                        depositCustDetMap = (HashMap) multipleDeposit.get(objKeySetMulitiDep[z]);
                        System.out.println("depositCustDetMap1112@@@@####" + depositCustDetMap);
                        System.out.println("depositCustDetMap.get(DEPOSIT_NO)###!!##>>>>" + depositCustDetMap.get("DEPOSIT_NO"));
//                        mdsUpdateMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
                        String keyValue1 = CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO"));
                        if (keyValue1.indexOf("_") != -1) {
                            chittalNo2 = keyValue1.substring(0, keyValue1.indexOf("_"));
                            subNo2 = keyValue1.substring(keyValue1.indexOf("_") + 1, keyValue1.length());
                        }
                        mdsUpdateMap.put("CHITTAL_NO", chittalNo2);
                        mdsUpdateMap.put("SUB_NO", subNo2);
//                      mdsUpdateMap.put("CHITTAL_NO", keyValue);
                        executeUpdate("updateMDSLoanStatus", mdsUpdateMap);
                    }

//                    mdsUpdateMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
////                    mdsUpdateMap.put("CHITTAL_NO", keyValue);
//                    executeUpdate("updateMDSLoanStatus", mdsUpdateMap);
                }

                // To make Lien Marking for LTD loans

                if (multipleDeposit != null && objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {

                    Set set = multipleDeposit.keySet();
                    Object objKeySetMulitiDep[] = (Object[]) set.toArray();
                    HashMap objHashMap = new HashMap();
                    DepositLienDAO depLienDAO = new DepositLienDAO();
                    DepositLienTO obj = new DepositLienTO();
                    ArrayList lienTOs = new ArrayList();
                    for (int z = 0; z < multipleDeposit.size(); z++) {
                        depositCustDetMap = (HashMap) multipleDeposit.get(objKeySetMulitiDep[z]);
                        System.out.println("depositCustDetMap####" + depositCustDetMap);
                        double lienAmt = 0;
                        obj = new DepositLienTO();
                        HashMap prodMap = new HashMap();
                        String depositLienRoundOff = "";
                        prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                        if (lst.size() > 0) {
                            prodMap = (HashMap) lst.get(0);
                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
                        }
                        obj.setLienAcNo(objTermLoanFacilityTO.getAcctNum());
                        double eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                        prodMap = null;

                        double availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();


                        availBal = (availBal / eligibleMargin * 100.0);  //85.0
                        availBal = roundOffDepositLien(availBal, depositLienRoundOff);
                        if (multipleDeposit.size() == 1) {
                            obj.setLienAmount(new Double(availBal));
                        } else {
                            obj.setLienAmount(CommonUtil.convertObjToDouble(depositCustDetMap.get("DEPOSIT_AMT")));
                        }
                        obj.setLienDt(curr_dt);//curr_dt);
                        obj.setRemarks("Lien for LTD");
                        obj.setCreditLienAcct("NA");
                        obj.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
//                        obj.setDepositSubNo(String.valueOf((1)));
                        obj.setLienProdId(objTermLoanFacilityTO.getProdId());
                        obj.setLienNo("-");
                        obj.setStatus(objTermLoanFacilityTO.getStatus());
                        obj.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                        obj.setStatusDt(objTermLoanFacilityTO.getStatusDt());

                        lienTOs = new ArrayList();
                        lienTOs.add(obj);
                        objHashMap = new HashMap();
                        objHashMap.put("lienTOs", lienTOs);
                        if (multipleDeposit.size() == 1) {
                            objHashMap.put("SHADOWLIEN", new Double(availBal));
                        } else {
                            objHashMap.put("SHADOWLIEN", CommonUtil.convertObjToDouble(depositCustDetMap.get("DEPOSIT_AMT")));
                        }
                        objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        System.out.println("### objHashMap : " + objHashMap);
                        depLienDAO = new DepositLienDAO();
                        depLienDAO.setCallFromOtherDAO(true);
                        objHashMap = depLienDAO.execute(objHashMap);
                        if (objHashMap != null) {
                            lienNo = CommonUtil.convertObjToStr(objHashMap.get("LIENNO"));
                        }
                        //                    if(multipleDeposit !=null && multipleDeposit.size()>0){

                        //                        for(int z=0;z<multipleDeposit.size();z++){
                        //                            depositCustDetMap=(HashMap)multipleDeposit.get(it.toArray());
                        //                            lienAmt=CommonUtil.convertObjToDouble(depositCustDetMap.get("DEPOSIT_AMT")).doubleValue();
                        //                            multipleDeposit.remove(it.toArray());
                        //                            break;
                        //                        }
                        //                    }

                    }
                    obj = null;
                    lienTOs = null;
                    depLienDAO = null;
                    objHashMap = null;
                } else if (loanType.equals("LTD") && objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)
                        && productCategory.equals("OTHER_LOAN")) {
                    ArrayList lienTOs = new ArrayList();
                    HashMap lienCancelMap = new HashMap();
                    lienCancelMap.put("LOAN_NO", objTermLoanFacilityTO.getAcctNum());
                    HashMap prodMap = new HashMap();
                    String depositLienRoundOff = "";
                    double eligibleMargin = 0;

                    prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                    List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                    if (lst.size() > 0) {
                        prodMap = (HashMap) lst.get(0);
                        //                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                        depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
                        eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                    }

                    List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancelMap);
                    //System.out.println(additionalSanMap + "getdepositliento#####" + list);





                    if (additionalSanMap == null) {
                        additionalSanMap = new HashMap();
                    }
                    DepositLienDAO depLienDAO = new DepositLienDAO();
                    if (list != null && list.size() > 0 && additionalSanMap.isEmpty()) {
                        for (int x = 0; x < list.size(); x++) {
                            DepositLienTO depLienTO = (DepositLienTO) list.get(x);
                            HashMap deletedMap = null;
                            HashMap updateMap = null;
                            if (deleteLien != null && deleteLien.size() > 0) {
                                deletedMap = (HashMap) deleteLien.get(depLienTO.getDepositNo());
                            }
                            if (updateLien != null && updateLien.size() > 0) {
                                updateMap = (HashMap) updateLien.get(depLienTO.getDepositNo());
                            }

                            System.out.println("deletedMap$#####" + deletedMap + "updateMap####" + updateMap);
                            double availBal = 0;
                            if (list.size() == 1) {
                                availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();
                                availBal = (availBal / eligibleMargin * 100.0);  //85.0
                                availBal = roundOffDepositLien(availBal, depositLienRoundOff);
                                depLienTO.setLienAmount(new Double(availBal));
                            } else {

                                if (deletedMap != null) {

//                                 depLienTO.setLienAmount(CommonUtil.convertObjToDouble(deletedMap.get("DEPOSIT_AMT")));
//                                 depLienTO.setStatus("UNLIENED");

                                    HashMap cancelMap = new HashMap();
                                    cancelMap.put("PROD_ID", objTermLoanFacilityTO.getProdId());
                                    cancelMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(deletedMap.get("DEPOSIT_NO")));

                                    lienCancel(cancelMap);
                                    deleteLien.remove(depLienTO.getDepositNo());
                                    continue;

                                }
//                             if(updateMap !=null){
//                                 if(CommonUtil.convertObjToStr(updateMap.get("STATUS")).equals("CREATED")){
//                                     lienCancelMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT);
////                                      DepositLienTO obj = new DepositLienTO();
////                               
//                    depLienTO.setLienAcNo(objTermLoanFacilityTO.getAcctNum());
////                    double eligibleMargin=CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
//                    prodMap = null;
//                                     
//                                        depLienTO.setLienAmount(CommonUtil.convertObjToDouble(updateMap.get("DEPOSIT_AMT")));
//                                        depLienTO.setLienDt(curr_dt);//curr_dt);
//                                        depLienTO.setRemarks("Lien for LTD");
//                                        depLienTO.setCreditLienAcct("NA");
//                                        depLienTO.setDepositNo(CommonUtil.convertObjToStr(updateMap.get("DEPOSIT_NO")));
//                                        depLienTO.setDepositSubNo(String.valueOf((1)));
//                                        depLienTO.setLienProdId(objTermLoanFacilityTO.getProdId());
//                                        depLienTO.setLienNo("-");
//                                        depLienTO.setStatus(objTermLoanFacilityTO.getStatus());
//                                        depLienTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
//                                        depLienTO.setStatusDt(objTermLoanFacilityTO.getStatusDt());
//
//                                        lienTOs = new ArrayList();
//                                        lienTOs.add(depLienTO);
//                                        HashMap objHashMap=new HashMap();
//                                        objHashMap.put("lienTOs",lienTOs);
//                                        objHashMap.put("SHADOWLIEN",depLienTO.getLienAmount());//new Double(availBal));
//                                        objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
//                                        objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
//                                        System.out.println("### objHashMap : "+objHashMap);
//                                        depLienDAO = new DepositLienDAO();
//                                        depLienDAO.setCallFromOtherDAO(true);
//                                        
//                                         objHashMap = depLienDAO.execute(objHashMap);
//                                        if (objHashMap!=null) {
//                                        lienNo = CommonUtil.convertObjToStr(objHashMap.get("LIENNO"));
//                                        }
//                                     continue;
//                                     
//                                 }
//                                 
                                if (updateMap != null) {
                                    if (CommonUtil.convertObjToStr(updateMap.get("STATUS")).equals("MODIFIED")) {


                                        depLienTO.setLienAmount(CommonUtil.convertObjToDouble(updateMap.get("DEPOSIT_AMT")));
                                        depLienTO.setStatus("MODIFIED");
                                        lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);


//                         else
//                              availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();

//                        DepositLienTO depLienTO = (DepositLienTO) list.get(0);


                                        depLienTO.setStatusDt(curr_dt);
                                        depLienTO.setRemarks("Lien for LTD");
                                        depLienTO.setAuthorizeBy(depLienTO.getAuthorizeBy());
                                        depLienTO.setAuthorizeDt(depLienTO.getAuthorizeDt());
                                        depLienTO.setAuthorizeStatus(depLienTO.getAuthorizeStatus());
                                        lienTOs.add(depLienTO);
                                        DepositLienDAO depositLiendao = new DepositLienDAO();
                                        lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
//                        if(deletedMap !=null && deletedMap.size()>0)
//                             lienCancelMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE);
//                        else
//                            lienCancelMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE);
                                        lienCancelMap.put("lienTOs", lienTOs);
                                        lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                                        depositLiendao.setCallFromOtherDAO(true);
                                        System.out.println("lienCancelbefore UPDATE ******FACILITYTO" + lienCancelMap);
                                        depositLiendao.execute(lienCancelMap);
                                        updateLien.remove(depLienTO.getDepositNo());

                                    }
                                }
                            }


                        }
                    }

                    if (updateLien != null) {
                        Set keySetDep;
                        Object[] objKeySetDep;

                        keySetDep = updateLien.keySet();
                        objKeySetDep = (Object[]) keySetDep.toArray();
                        // To retrieve the TermLoanJointAcctTO from the jointAcctMap
                        for (int t = updateLien.size() - 1, u = 0; t >= 0; --t, ++u) {
                            HashMap updateMap = (HashMap) updateLien.get(objKeySetDep[u]);
                            System.out.println("updateMap######" + updateMap);
                            if (CommonUtil.convertObjToStr(updateMap.get("STATUS")).equals("CREATED")) {
                                lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                DepositLienTO depLienTO = new DepositLienTO();
                                //                                    ArrayList lienTOs = new ArrayList();
                                //                                     
                                //                                     obj = new DepositLienTO();
                                //                    HashMap prodMap = new HashMap();
                                //                    String depositLienRoundOff="";
                                //                    prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                                //                    List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                                //                    if (lst.size()>0) {
                                //                    prodMap = (HashMap)lst.get(0);
                                //                    obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                                //                    depositLienRoundOff=CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
                                //                    }
                                depLienTO.setLienAcNo(objTermLoanFacilityTO.getAcctNum());
                                //                    double eligibleMargin=CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                                //   prodMap = null;
                                depLienTO.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                                depLienTO.setLienAmount(CommonUtil.convertObjToDouble(updateMap.get("DEPOSIT_AMT")));
                                depLienTO.setLienDt(curr_dt);//curr_dt);
                                depLienTO.setRemarks("Lien for LTD");
                                depLienTO.setCreditLienAcct("NA");
                                depLienTO.setDepositNo(CommonUtil.convertObjToStr(updateMap.get("DEPOSIT_NO")));
//                                depLienTO.setDepositSubNo(String.valueOf((1)));
                                depLienTO.setLienProdId(objTermLoanFacilityTO.getProdId());
                                depLienTO.setLienNo("-");
                                depLienTO.setStatus(objTermLoanFacilityTO.getStatus());
                                depLienTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                                depLienTO.setStatusDt(objTermLoanFacilityTO.getStatusDt());

                                lienTOs = new ArrayList();
                                lienTOs.add(depLienTO);
                                HashMap objHashMap = new HashMap();
                                objHashMap.put("lienTOs", lienTOs);
                                objHashMap.put("SHADOWLIEN", depLienTO.getLienAmount());//new Double(availBal));
                                objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                System.out.println("### objHashMap : " + objHashMap);
                                depLienDAO = new DepositLienDAO();
                                depLienDAO.setCallFromOtherDAO(true);

                                objHashMap = depLienDAO.execute(objHashMap);
                                if (objHashMap != null) {
                                    lienNo = CommonUtil.convertObjToStr(objHashMap.get("LIENNO"));
                                }
                            }
                        }

                    }
                } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    HashMap dataMap = new HashMap();
                    dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    dataMap.put("LINK_BATCH_ID", objTermLoanFacilityTO.getAcctNum());
                    dataMap.put("TRANS_DT", objTermLoanFacilityTO.getAccOpenDt());
                    String linkBatchId = CommonUtil.convertObjToStr(objTermLoanFacilityTO.getAcctNum());
                    dataMap.put(CommonConstants.USER_ID, objTermLoanFacilityTO.getStatusBy());






                    //

//                           tempMap = (HashMap) chargesMap.get("NET_TRANSACTION_CASH");
//                System.out.println("#### inside CASH tempMap : "+tempMap);
//                oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    CashTransactionDAO cashDAO = new CashTransactionDAO();
                    CashTransactionTO cashTO = new CashTransactionTO();
                    HashMap cashMap = new HashMap();

                    cashMap.put("LINK_BATCH_ID", objTermLoanFacilityTO.getAcctNum());
                    cashMap.put("TRANS_DT", objTermLoanFacilityTO.getAccOpenDt());
                    System.out.println("cashMap 1st :" + cashMap);
                    List lstCash = (List) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", cashMap);
                    if (lstCash != null && lstCash.size() > 0) {
                        for (int a = 0; a < lstCash.size(); a++) {

                            cashTO = (CashTransactionTO) lstCash.get(a);
                            cashTO.setCommand("DELETE");
                            cashTO.setStatus(CommonConstants.STATUS_DELETED);
                            cashMap.put("CashTransactionTO", cashTO);
                            cashMap.put("BRANCH_CODE", _branchCode);
                            cashMap.put("USER_ID", objTermLoanFacilityTO.getStatusBy());
                            cashMap.put("OLDAMOUNT", new Double(0));
                            cashMap.put("SELECTED_BRANCH_ID", _branchCode);
                            System.out.println("cashMap :" + cashMap);
                            cashDAO.execute(cashMap, false);
                        }
                    } else {

                        HashMap transferMap = new HashMap();
                        transferMap.put("LINK_BATCH_ID", objTermLoanFacilityTO.getAcctNum());
//                     tempMap.put("BATCHID", tempMap.get("BATCH_ID"));

//                double newAmount = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNetAmt()).doubleValue();
                        ArrayList batchList = new ArrayList();
                        TxTransferTO txTransferTO = null;
                        HashMap oldAmountMap = new HashMap();


                        List lstTransfer = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferMap);
                        if (lstTransfer != null) {
                            for (int b = 0; b < lstTransfer.size(); b++) {
                                txTransferTO = (TxTransferTO) lstTransfer.get(b);

                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                txTransferTO.setStatusDt(curr_dt);
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(0));
                                batchList.add(txTransferTO);
                            }
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(_branchCode);
                            transferTrans.doDebitCredit(batchList, _branchCode, false, CommonConstants.STATUS_DELETED);

                            lstTransfer = null;
                            transferTrans = null;
                        }
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
            throw e;
            //            throw e;
        }
    }

    //--- Inserts the data Record in SMS_SUBSCRIPTION Table
    private void updateSMSSubscription(TermLoanFacilityTO objTermLoanFacilityTO) throws Exception {
        HashMap checkMap = new HashMap();
        objSMSSubscriptionTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(objTermLoanFacilityTO.getStatusDt());
        objSMSSubscriptionTO.setCreatedDt(objTermLoanFacilityTO.getStatusDt());
        objSMSSubscriptionTO.setCustId(objTermLoanBorrowerTO.getCustId());
        objSMSSubscriptionTO.setActNum(objTermLoanFacilityTO.getAcctNum());
        if (objSMSSubscriptionTO.getMobileNo() != null && objSMSSubscriptionTO.getMobileNo().length() > 0 && objSMSSubscriptionTO.getSubscriptionDt() != null) {
            List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
            if (list != null && list.size() > 0) {
                sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
            } else {
                sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
            }
            checkMap.clear();
            checkMap = null;
        }
    }

    //we should update customer history
    private void insertCustomerHistory(TermLoanFacilityTO termLoanFacilityTO) throws Exception {
        // Inserting into Customer History Table
        HashMap map = new HashMap();
        behaves_like = null;
        map.put("PROD_ID", termLoanFacilityTO.getProdId());
        List lst = sqlMap.executeQueryForList("getLoansProduct", map);
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            behaves_like = CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
        }
        CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
        objCustomerHistoryTO.setAcctNo(termLoanFacilityTO.getAcctNum());
        objCustomerHistoryTO.setCustId(objTermLoanBorrowerTO.getCustId());
        if (behaves_like.equals("OD") || behaves_like.equals("CC")) {
            objCustomerHistoryTO.setProductType("AD");
        } else {
            objCustomerHistoryTO.setProductType("TL");
        }
        objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
        objCustomerHistoryTO.setRelationship("ACCT_HOLDER");
        map = setFromToDate(termLoanFacilityTO);
        if (map != null && map.size() > 0) {
            objCustomerHistoryTO.setFromDt((Date) map.get("FROM_DT"));
            objCustomerHistoryTO.setToDt((Date) map.get("TO_DT"));

        }
        objCustomerHistoryTO.setCommand(termLoanFacilityTO.getCommand());
        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
        objCustomerHistoryTO = null;

    }

    private void insertRenewalRecords(TermLoanFacilityTO objTermLoanFacilityTO) throws Exception {
        HashMap map = new HashMap();
        map.put("ACCT_NUM", objTermLoanFacilityTO.getAcctNum());

        sqlMap.executeUpdate("insertLoansRenewalFacilityDetails", map);
    }

    private void executeDisbursementTabQuery() throws Exception {
        if (DisbursementMap != null) {
            Set set = DisbursementMap.keySet();
            TermLoanDisburstTO agriSubLimitTO = new TermLoanDisburstTO();
            Object objKeySet[] = (Object[]) set.toArray();
            try {
                System.out.println("DisbursementMap$$$$" + DisbursementMap);
                for (int i = 0; i < DisbursementMap.size(); i++) {
                    agriSubLimitTO = (TermLoanDisburstTO) DisbursementMap.get(objKeySet[i]);
                    agriSubLimitTO.setAcctNum(acct_No);
                    if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        logTO.setData(agriSubLimitTO.toString());
                        //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
                        logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                        sqlMap.executeUpdate("insertTermLoanDisburstTO", agriSubLimitTO);
                        logDAO.addToLog(logTO);
                    } else if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        logTO.setData(agriSubLimitTO.toString());
                        //                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                        logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                        sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
                        logDAO.addToLog(logTO);
                    } else if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        logTO.setData(agriSubLimitTO.toString());
                        //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
                        logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
                        sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
                        logDAO.addToLog(logTO);
                    }
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    private void executeExtenFacilityDetailsTabQuery() throws Exception {
        if (termLoanExtenFacilityMap != null) {
            Set set = termLoanExtenFacilityMap.keySet();
            TermLoanExtenFacilityDetailsTO agriSubLimitTO = new TermLoanExtenFacilityDetailsTO();
            Object objKeySet[] = (Object[]) set.toArray();
            try {
                if (set.size() > 0) {
                    System.out.println("termLoanExtenFacilityMap#####" + termLoanExtenFacilityMap);
                    agriSubLimitTO = (TermLoanExtenFacilityDetailsTO) termLoanExtenFacilityMap.get("TermLoanExtenFacilityDetailsTO");
                    if (agriSubLimitTO != null) {
                        if (agriSubLimitTO.getCommand() == null) {
                            sqlMap.executeUpdate("insertTermLoanFacilityExtnTO", agriSubLimitTO);
                        }
                        if (agriSubLimitTO.getCommand() != null && agriSubLimitTO.getCommand().equals("UPDATE")) {
                            sqlMap.executeUpdate("updateTermLoanFacilityExtnTO", agriSubLimitTO);
                        }
                    }
                }
//            for(int i=0;i<DisbursementMap.size();i++){
//                agriSubLimitTO=(TermLoanDisburstTO)DisbursementMap.get(objKeySet[i]);
//                if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
//                    logTO.setData(agriSubLimitTO.toString());
//                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
//                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
//                    sqlMap.executeUpdate("insertTermLoanDisburstTO", agriSubLimitTO);
//                    logDAO.addToLog(logTO);
//                }else if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
//                    logTO.setData(agriSubLimitTO.toString());
//                    //                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
//                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
//                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
//                    logDAO.addToLog(logTO);
//                }else if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
//                    logTO.setData(agriSubLimitTO.toString());
//                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
//                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
//                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
//                    logDAO.addToLog(logTO);
//                }
//            }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw new TransRollbackException(e);
            }
        }
    }

    //    //we should update customer history
    //    private void insertGuarantorCustomerHistory(TermLoanGuarantorTO termLoanGuarantorTO) throws Exception{
    //                    // Inserting into Customer History Table
    //        HashMap map=new HashMap();
    //        String behaves_like=null;
    //        map.put("PROD_ID",termLoanFacilityTO.getProdId());
    //        List lst =sqlMap.executeQueryForList("getLoansProduct",map);
    //        if(lst !=null &&lst.size()>0){
    //            map=(HashMap)lst.get(0);
    //            behaves_like=CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
    //        }
    //            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
    //            objCustomerHistoryTO.setAcctNo(termLoanGuarantorTO.getAcctNum());
    //            objCustomerHistoryTO.setCustId(termLoanGuarantorTO.getCustId());
    //            if(behaves_like.equals("OD") || behaves_like.equals("CC"))
    //                objCustomerHistoryTO.setProductType("AD");
    //            else
    //                objCustomerHistoryTO.setProductType("TL");
    ////            objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
    //            objCustomerHistoryTO.setRelationship(AcctStatusConstants.GUARANTOR);
    ////             map=setFromToDate(termLoanFacilityTO);
    ////            if(map !=null &&  map.size()>0){
    ////            objCustomerHistoryTO.setFromDt((Date)map.get("FROM_DT"));
    ////            objCustomerHistoryTO.setToDt((Date)map.get("TO_DT"));
    ////
    ////            }
    //            objCustomerHistoryTO.setCommand(termLoanGuarantorTO.getCommand());
    //            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
    //            objCustomerHistoryTO = null;
    //
    //    }
    //
    //
    //        //we should update customer history
    //    private void insertSecurityCustomerHistory(TermLoanFacilityTO termLoanFacilityTO) throws Exception{
    //                    // Inserting into Customer History Table
    //        HashMap map=new HashMap();
    //        String behaves_like=null;
    //        map.put("PROD_ID",termLoanFacilityTO.getProdId());
    //        List lst =sqlMap.executeQueryForList("getLoansProduct",map);
    //        if(lst !=null &&lst.size()>0){
    //            map=(HashMap)lst.get(0);
    //            behaves_like=CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
    //        }
    //            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
    //            objCustomerHistoryTO.setAcctNo(termLoanFacilityTO.getAcctNum());
    //            objCustomerHistoryTO.setCustId(objTermLoanBorrowerTO.getCustId());
    //            if(behaves_like.equals("AOD") || behaves_like.equals("CC"))
    //                objCustomerHistoryTO.setProductType("AD");
    //            else
    //                objCustomerHistoryTO.setProductType("TL");
    //            objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
    //            objCustomerHistoryTO.setRelationship("ACCT_HOLDER");
    //             map=setFromToDate(termLoanFacilityTO);
    //            if(map !=null &&  map.size()>0){
    //            objCustomerHistoryTO.setFromDt((Date)map.get("FROM_DT"));
    //            objCustomerHistoryTO.setToDt((Date)map.get("TO_DT"));
    //
    //            }
    //            objCustomerHistoryTO.setCommand(termLoanFacilityTO.getCommand());
    //            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
    //            objCustomerHistoryTO = null;
    //
    //    }
    private HashMap setFromToDate(TermLoanFacilityTO termLoanFacilityTO) {
        Set set = sanctionFacilityMap.keySet();
        HashMap resultMap = new HashMap();
        Object objKeySet[] = (Object[]) set.toArray();
        if (sanctionFacilityMap != null) {
            for (int i = 0; i < sanctionFacilityMap.size(); i++) {
                TermLoanSanctionFacilityTO termLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) sanctionFacilityMap.get(objKeySet[i]);
                if (termLoanFacilityTO.getSanctionNo() == termLoanSanctionFacilityTO.getSanctionNo()
                        && (termLoanSanctionFacilityTO.getSlNo() == termLoanFacilityTO.getSlNo())) {
                    resultMap.put("FROM_DT", termLoanSanctionFacilityTO.getFromDt());
                    resultMap.put("TO_DT", termLoanSanctionFacilityTO.getToDt());
                    return resultMap;
                }
            }
        }
        return resultMap;
    }

    private double roundOffDepositLien(double lienAmt, String roundOff) throws Exception {
        //        roundOff=CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_ROUNDOFF"));
        long roundOffValue = 0;
        double finalLien = 0.0;
        //        long finalLien=0;
        //        long longLien=(long)lienAmt;

        //            if(roundOff.length()>0){
        //                if(roundOff .equals("NEAREST_TENS")){
        //                    roundOffValue=10;
        //                }
        //                else   if(roundOff .equals("NEAREST_HUNDREDS")){
        //                    roundOffValue=100;
        //                }
        //                else  if(roundOff .equals("NEAREST_VALUE")){
        roundOffValue = 1;
        //                } else  if(roundOff .equals("NO_ROUND_OFF")){
        //                    return finalLien;
        //                }
        //            }

        if (roundOff.length() == 0) {
            sqlMap.rollbackTransaction();
            throw new TTException("Eligible Deposit Rounding of  Parameter Value Not Set In product Level");
        }

        //         long lienAmt=(long)(enterAmt/eligibleMargin);
        Rounding rd = new Rounding();
        finalLien = rd.getNearestHigher(lienAmt, roundOffValue);
        return finalLien;
    }
//    private void executeSecurityTabQuery() throws Exception{
//        TermLoanSecurityTO objTermLoanSecurityTO;
//        Set keySet;
//        Object[] objKeySet;
//        String strCustSecurityKey = "";
//        try{
//            HashMap oldAvailSecAmtMap = (HashMap)securityMap.remove("OLD_ELIGIBLE_LOAN_AMT");
//            HashMap whereAvailSecAmtMap;
//            keySet =  securityMap.keySet();
//            objKeySet = (Object[]) keySet.toArray();
//            double eligibleAmt = 0.0;
//            
//            // To retrieve the TermLoanSecurityTO from the securityMap
//            for (int i = securityMap.size() - 1, j = 0;i >= 0;--i,++j){
//                whereAvailSecAmtMap = new HashMap();
//                objTermLoanSecurityTO = (TermLoanSecurityTO) securityMap.get(objKeySet[j]);
//                if (CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAcctNum()).length()<1) {
//                    objTermLoanSecurityTO.setAcctNum(acct_No);
//                }
//                if (delRefMap.containsKey(objTermLoanSecurityTO.getAcctNum())) {
//                    objTermLoanSecurityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
//                }
//                logTO.setData(objTermLoanSecurityTO.toString());
//                logTO.setPrimaryKey(objTermLoanSecurityTO.getKeyData());
//                logTO.setStatus(objTermLoanSecurityTO.getCommand());
//                executeOneTabQueries("TermLoanSecurityTO", objTermLoanSecurityTO);
//                
//                whereAvailSecAmtMap.put("CUST_ID", objTermLoanSecurityTO.getCustId());
//                whereAvailSecAmtMap.put("SECURITY_NO", objTermLoanSecurityTO.getSecurityNo());
//                
//                strCustSecurityKey = getEligibleLoanAmtKey(objTermLoanSecurityTO.getCustId(), CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
//                eligibleAmt = 0.0;
//                if (oldAvailSecAmtMap.containsKey(strCustSecurityKey)){
//                    if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
//                        double eligibleOldLoanAmt = CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue();
//                        double currentEligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
//                        eligibleAmt = currentEligibleAmt - eligibleOldLoanAmt;
//                    }else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
//                        eligibleAmt = (-1 * CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue());
//                    }
//                }else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
//                    eligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
//                }
//                
//                whereAvailSecAmtMap.put("AVAILABLE_SECURITY_VALUE", new java.math.BigDecimal(eligibleAmt));
//                
//                if (eligibleAmt != 0.0){
//                    sqlMap.executeUpdate("updateCustSecurityAvailableAmt", whereAvailSecAmtMap);
//                }
//                
//                logDAO.addToLog(logTO);
//                objTermLoanSecurityTO = null;
//                strCustSecurityKey = null;
//                whereAvailSecAmtMap = null;
//            }
//            keySet = null;
//            objKeySet = null;
//            objTermLoanSecurityTO = null;
//            oldAvailSecAmtMap = null;
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//    }

    private String getEligibleLoanAmtKey(String strCustID, String strSecurityNo) throws Exception {
        return strCustID + "#" + strSecurityNo;
    }

    private void executeRepaymentQuery() throws Exception {
        TermLoanRepaymentTO objTermLoanRepaymentTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (repaymentMap != null) {
                keySet = repaymentMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap repayDetailsMap;
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanRepaymentTO from the repaymentMap
                for (int i = repaymentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanRepaymentTO = (TermLoanRepaymentTO) repaymentMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanRepaymentTO.getAcctNum()).length() < 1) {
                        objTermLoanRepaymentTO.setAcctNum(acct_No);
                    }
                    if (delRefMap.containsKey(objTermLoanRepaymentTO.getAcctNum())) {
                        objTermLoanRepaymentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanRepaymentTO.setStatus(CommonConstants.STATUS_DELETED);
                        delRefMap.put("DELACTNUM", objTermLoanRepaymentTO.getAcctNum());
                        delRefMap.put("DELSCHNUM", objTermLoanRepaymentTO.getScheduleNo());
                    }

                    updateMap.put("ACCT_NUM", objTermLoanRepaymentTO.getAcctNum());
                    updateMap.put("CURR_DT", curr_dt);
                    if (objTermLoanRepaymentTO.getRepayActive().equals("N")) {
                        updateMap.put("SCHEDULE_ID", objTermLoanRepaymentTO.getScheduleNo());
                        System.out.println("updateMap#####" + updateMap +"TYPE_EMI--"+objTermLoanRepaymentTO.getInstallType());
                        if(objTermLoanRepaymentTO.getInstallType()!=null && 
                                (objTermLoanRepaymentTO.getInstallType().equals("EMI") || 
                                objTermLoanRepaymentTO.getInstallType().equals("UNIFORM_PRINCIPLE_EMI"))){
                            sqlMap.executeUpdate("updateLoansInstallmentInTermLoanEMI", updateMap);
                        }
                        else{
                            sqlMap.executeUpdate("updateLoansInstallmentTL", updateMap);
                        }
                       
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
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;

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
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanInstallmentTO from the installmentMap
                for (int i = installmentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstallmentTO = (TermLoanInstallmentTO) installmentMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanInstallmentTO.getAcctNum()).length() < 1) {
                        objTermLoanInstallmentTO.setAcctNum(acct_No);
                    }
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
            throw e;
        }
    }

    private void executeCaseTabQuery() throws Exception {
        TermLoanCaseDetailTO objCaseDetailTO;
        try {
            if (caseDetailMap != null && caseDetailMap.size() > 0) {
                Set keySet;
                Object[] objKeySet;
                keySet = caseDetailMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanCaseDetailTO from the caseDetailMap
                for (int i = caseDetailMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objCaseDetailTO = (TermLoanCaseDetailTO) caseDetailMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objCaseDetailTO.getActNum()).length() < 1) {
                        objCaseDetailTO.setActNum(acct_No);
                    }
                    if (cmd.equals(CommonConstants.TOSTATUS_DELETE)) {
                        objCaseDetailTO.setCommand(cmd);
                    }
                    logTO.setData(objCaseDetailTO.toString());
                    logTO.setPrimaryKey(objCaseDetailTO.getKeyData());
                    logTO.setStatus(objCaseDetailTO.getCommand());
                    System.out.println("$#%$%&%&objCaseDetailTO" + objCaseDetailTO);
                    if (objCaseDetailTO != null && CommonUtil.convertObjToStr(objCaseDetailTO.getCommand()).equals("INSERT")) {
                        executeCaseTransaction(objCaseDetailTO);
                    }
                    if (objCaseDetailTO != null && CommonUtil.convertObjToStr(objCaseDetailTO.getCommand()).equals("DELETE")) {
                        HashMap dataMap = new HashMap();
                        dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        dataMap.put(CommonConstants.USER_ID, logTO.getUserId());
                        dataMap.put("ACCT_NUM", objCaseDetailTO.getActNum());
//                        String linkBatchId=CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));



//                      getAuthorizeTransaction(null,"REJECTED",dataMap);
                        getRejactCaseTransaction(dataMap, objCaseDetailTO);
                    }


                    executeOneTabQueries("TermLoanCaseDetailTO", objCaseDetailTO);
                    logDAO.addToLog(logTO);
                    objCaseDetailTO = null;
                }

                keySet = null;
                objKeySet = null;
            }
            objCaseDetailTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeCaseTransaction(TermLoanCaseDetailTO objCaseDetailTO) throws Exception {

        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        double totCreditAmt = 0;
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);

        transactionDAO.setLinkBatchID(objCaseDetailTO.getActNum());
        transactionDAO.setInitiatedBranch(_branchCode);
        HashMap crMap = new HashMap();
        crMap.put("ACCT_NUM", objCaseDetailTO.getActNum());
        List oaAcctHead = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", crMap);
        if (oaAcctHead != null && oaAcctHead.size() > 0) {
            crMap = (HashMap) oaAcctHead.get(0);
        }
        txMap = new HashMap();
        ArrayList transferList = new ArrayList();
        TxTransferTO transferTo = new TxTransferTO();
        //                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
        //                    txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM"));

        // debit mischarges

        if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("ARC")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("ARC Cost"));
            txMap.put("AUTHORIZEREMARKS", "ARC");
            txMap.put("HIERARCHY", "1");
        } else if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("EP")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("EP Cost"));
            txMap.put("AUTHORIZEREMARKS", "EP");
            txMap.put("HIERARCHY", "2");
        } else if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("EA")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("EA Cost"));
            txMap.put("AUTHORIZEREMARKS", "EA");
            txMap.put("HIERARCHY", "3");
        }

        //                    txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
        txMap.put(TransferTrans.PARTICULARS, "Case Charges - " + objCaseDetailTO.getActNum());
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//        txMap.put("AUTHORIZEREMARKS", "Case Charges");
        //                    txMap.put("DEBIT_LOAN_TYPE", "DP");
        if (CommonUtil.convertObjToDouble(objCaseDetailTO.getMiscCharges()).doubleValue() > 0) {
            totCreditAmt += CommonUtil.convertObjToDouble(objCaseDetailTO.getMiscCharges()).doubleValue();
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objCaseDetailTO.getMiscCharges()).doubleValue());
        }
        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
        transferTo.setStatusBy(CommonUtil.convertObjToStr(logTO.getUserId()));
        transferTo.setInitTransId(CommonUtil.convertObjToStr(logTO.getUserId()));
        transferTo.setLinkBatchId(objCaseDetailTO.getActNum());
        transferTo.setInitiatedBranch(_branchCode);
        transferTo.setScreenName("Loans/Advances Account Opening");
        transferList.add(transferTo);

        txMap = new HashMap();
        //debit filling fees
        if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("ARC")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("ARC Expense"));
            txMap.put("AUTHORIZEREMARKS", "ARC");
            txMap.put("HIERARCHY", "1");
        } else if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("EP")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("EP Expense"));
            txMap.put("AUTHORIZEREMARKS", "EP");
            txMap.put("HIERARCHY", "2");
        } else if (CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()).equals("EA")) {
            txMap.put(TransferTrans.DR_AC_HD, crMap.get("EA Expense"));
            txMap.put("AUTHORIZEREMARKS", "EA");
            txMap.put("HIERARCHY", "3");

        }
        //                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
        txMap.put(TransferTrans.PARTICULARS, "Case Charges - " + objCaseDetailTO.getActNum());
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//        txMap.put("AUTHORIZEREMARKS", "Case Charges");
        //                    txMap.put("DEBIT_LOAN_TYPE", "DP");
        if (CommonUtil.convertObjToDouble(objCaseDetailTO.getFillingFees()).doubleValue() > 0) {
            totCreditAmt += CommonUtil.convertObjToDouble(objCaseDetailTO.getFillingFees()).doubleValue();
            transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objCaseDetailTO.getFillingFees()).doubleValue());
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(logTO.getUserId()));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(logTO.getUserId()));
            transferTo.setLinkBatchId(objCaseDetailTO.getActNum());
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setScreenName("Loans/Advances Account Opening");
            transferList.add(transferTo);
        }

        txMap.put(TransferTrans.CR_AC_HD, crMap.get("ARC_EP_SUSPENCE_ACHD"));
        //                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
        System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
        if (totCreditAmt > 0) {
            transferTo = transactionDAO.addTransferCreditLocal(txMap, totCreditAmt);
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(logTO.getUserId()));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(logTO.getUserId()));
            transferTo.setLinkBatchId(objCaseDetailTO.getActNum());
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setScreenName("Loans/Advances Account Opening");
            transferList.add(transferTo);
        }
        //                    txMap.put(TransferTrans.PARTICULARS,dataMap.get("ACT_NUM"));
        //                    transferTo =  transactionDAO.addTransferCreditLocal(txMap, interestAmt) ;
        //                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
        //                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        //                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        //                    transferTo.setLinkBatchId((String)dataMap.get("ACT_NUM"));
        //                    transferList.add(transferTo);
        //                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        if (totCreditAmt > 0) {
            transactionDAO.doTransferLocal(transferList, _branchCode);
        }
    }

    private void executeInstallmentMultIntTabQuery() throws Exception {
        TermLoanInstallMultIntTO objTermLoanInstallMultIntTO;
        Set keySetMultInt;
        Object[] objKeySetMultInt;
        try {
            if (installmentMultIntMap != null) {
                keySetMultInt = installmentMultIntMap.keySet();
                objKeySetMultInt = (Object[]) keySetMultInt.toArray();
                // To retrieve the TermLoanInstallMultIntTO from the installmentMultIntMap
                for (int i = installmentMultIntMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstallMultIntTO = (TermLoanInstallMultIntTO) installmentMultIntMap.get(objKeySetMultInt[j]);
                    //                if (delRefMap.containsKey(objTermLoanInstallMultIntTO.getAcctNum())) {
                    //                    objTermLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    //                    objTermLoanInstallMultIntTO.setStatus(CommonConstants.STATUS_DELETED);
                    //                }
                    if (CommonUtil.convertObjToStr(objTermLoanInstallMultIntTO.getAcctNum()).length() < 1) {
                        objTermLoanInstallMultIntTO.setAcctNum(acct_No);
                    }
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
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeGuarantorTabQuery() throws Exception {
        TermLoanGuarantorTO objTermLoanGuarantorTO;
        if (guarantorMap != null) {
            Set keySet = guarantorMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            try {
                keySet = guarantorMap.keySet();
                objKeySet = (Object[]) keySet.toArray();

                // To retrieve the TermLoanGuarantorTO from the guarantorMap
                for (int i = guarantorMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanGuarantorTO = (TermLoanGuarantorTO) guarantorMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanGuarantorTO.getAcctNum()).length() < 1) {
                        objTermLoanGuarantorTO.setAcctNum(acct_No);
                    }
                    if (delRefMap.containsKey(objTermLoanGuarantorTO.getAcctNum())) {
                        objTermLoanGuarantorTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                    logTO.setData(objTermLoanGuarantorTO.toString());
                    logTO.setPrimaryKey(objTermLoanGuarantorTO.getKeyData());
                    logTO.setStatus(objTermLoanGuarantorTO.getCommand());
                    executeOneTabQueries("TermLoanGuarantorTO", objTermLoanGuarantorTO);
                    logDAO.addToLog(logTO);
                    objTermLoanGuarantorTO = null;
                }
                objTermLoanGuarantorTO = null;
                keySet = null;
                objKeySet = null;
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void executeInsititGuarantorTabQuery() throws Exception {
        TermLoanInstitGuarantorTO objTermLoanInstitGuarantorTO;
        if (guaranInstitMap != null) {
            Set keySet = guaranInstitMap.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            try {
                keySet = guaranInstitMap.keySet();
                objKeySet = (Object[]) keySet.toArray();

                // To retrieve the TermLoanGuarantorTO from the guarantorMap
                for (int i = guaranInstitMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstitGuarantorTO = (TermLoanInstitGuarantorTO) guaranInstitMap.get(objKeySet[j]);
                    if (delRefMap.containsKey(objTermLoanInstitGuarantorTO.getAcctNum())) {
                        objTermLoanInstitGuarantorTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanInstitGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                    logTO.setData(objTermLoanInstitGuarantorTO.toString());
                    logTO.setPrimaryKey(objTermLoanInstitGuarantorTO.getKeyData());
                    logTO.setStatus(objTermLoanInstitGuarantorTO.getCommand());
                    executeOneTabQueries("TermLoanInstitGuarantorTO", objTermLoanInstitGuarantorTO);
                    logDAO.addToLog(logTO);
                    objTermLoanInstitGuarantorTO = null;
                }
                objTermLoanInstitGuarantorTO = null;
                keySet = null;
                objKeySet = null;
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void executeDocumentTabQuery() throws Exception {
        TermLoanDocumentTO objTermLoanDocumentTO;
        if (documentMap != null) {
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
                    if (CommonUtil.convertObjToStr(objTermLoanDocumentTO.getAcctNo()).length() < 1) {
                        objTermLoanDocumentTO.setAcctNo(acct_No);
                    }
                    logTO.setData(objTermLoanDocumentTO.toString());
                    logTO.setPrimaryKey(objTermLoanDocumentTO.getKeyData());
                    logTO.setStatus(objTermLoanDocumentTO.getCommand());
                    executeOneTabQueries("TermLoanDocumentTO", objTermLoanDocumentTO);
                    logDAO.addToLog(logTO);
                    objTermLoanDocumentTO = null;
                }
                objTermLoanDocumentTO = null;
                keySet = null;
                objKeySet = null;
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void executeInterestTabQuery() throws Exception {
        TermLoanInterestTO objTermLoanInterestTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (interestMap != null) {
                keySet = interestMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                //first delete all existing recordss...
                //System.out.println("objKeySet --->"+objKeySet+"interestMap --->"+interestMap +" cmd-->"+cmd+"intGetFromUpdate -->"+intGetFromUpdate);
                
                if (cmd!=null && cmd.equals(CommonConstants.TOSTATUS_UPDATE) && intGetFromUpdate!=null && intGetFromUpdate.equals("Account")
                        && (intDeleteFlag!=null && intDeleteFlag.equals("Y"))){
                    HashMap intMaintMap=new HashMap();
                    intMaintMap.put("ACT_NUM", acct_No);
                    intMaintMap.put("STATUS", CommonConstants.STATUS_DELETED);
                    intMaintMap.put("STATUS_BY", CommonUtil.convertObjToStr(logTO.getUserId()));
                    intMaintMap.put("STATUS_DT", curr_dt);
                    sqlMap.executeUpdate("deleteTermLoanInterestMaintance", intMaintMap) ;
                }
                // To retrieve the TermLoanInterestTO from the interestMap
                for (int i = interestMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInterestTO = (TermLoanInterestTO) interestMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanInterestTO.getAcctNum()).length() < 1) {
                        objTermLoanInterestTO.setAcctNum(acct_No);
                    }
                    if (delRefMap.containsKey(objTermLoanInterestTO.getAcctNum())) {
                        objTermLoanInterestTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
                        sqlMap.executeUpdate("updateIntMaintenanceTL", objTermLoanInterestTO);

                    }
                    logTO.setData(objTermLoanInterestTO.toString());
                    logTO.setPrimaryKey(objTermLoanInterestTO.getKeyData());
                    logTO.setStatus(objTermLoanInterestTO.getCommand());
                    executeOneTabQueries("TermLoanInterestTO", objTermLoanInterestTO);
                    logDAO.addToLog(logTO);
                    objTermLoanInterestTO = null;
                }
                objTermLoanInterestTO = null;
                keySet = null;
                objKeySet = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeAdditionalSanctionTabQuery() throws Exception {
        TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (additionalSanMap != null) {
                keySet = additionalSanMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanInterestTO from the interestMap
                for (int i = additionalSanMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAcctNum()).length() < 1) {
                        objTermLoanAdditionalSanctionTO.setAcctNum(acct_No);
                    }
                    if (delRefMap.containsKey(objTermLoanAdditionalSanctionTO.getAcctNum())) {
                        objTermLoanAdditionalSanctionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanAdditionalSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                    }

                    logTO.setData(objTermLoanAdditionalSanctionTO.toString());
                    logTO.setPrimaryKey(objTermLoanAdditionalSanctionTO.getKeyData());
                    logTO.setStatus(objTermLoanAdditionalSanctionTO.getCommand());
                    executeOneTabQueries("TermLoanAdditionalSanctionTO", objTermLoanAdditionalSanctionTO);
                    System.out.println("loanType   ####" + loanType);
                    if (loanType.equals("LTD") && productCategory.equals("OTHER_LOAN")) {
                        additionalSanctionLienMark(objTermLoanAdditionalSanctionTO);
                    }

                    logDAO.addToLog(logTO);
                    objTermLoanAdditionalSanctionTO = null;
                }
                objTermLoanAdditionalSanctionTO = null;
                keySet = null;
                objKeySet = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void authorizeAdditionalSanction(String authStatus) throws Exception {
        TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (additionalSanMap != null) {
                keySet = additionalSanMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanInterestTO from the interestMap
                for (int i = additionalSanMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(objKeySet[j]);
                    System.out.println("objTermLoanAdditionalSanctionTO ####" + objTermLoanAdditionalSanctionTO);
                    if (objTermLoanAdditionalSanctionTO.getAuthorizeStatus().length() == 0) {
                        objTermLoanAdditionalSanctionTO.setAuthorizeStatus(authStatus);
                        sqlMap.executeUpdate("updateTermLoanAdditionalSanctionTO", objTermLoanAdditionalSanctionTO);
                        if (authStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                            sqlMap.executeUpdate("updateAvailableBalanceLTD", objTermLoanAdditionalSanctionTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
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
                executeOneTabQueries("TermLoanClassificationTO", objTermLoanClassificationTO);
                logDAO.addToLog(logTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
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

            executeOneTabQueries("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
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
            throw e;
        }
    }
    //Additional sanction details

    private void additionalSanctionLienMark(TermLoanAdditionalSanctionTO termLoanTO) throws Exception {
        // To make Lien Marking for LTD loans
        double eligibleMargin = 0;
        String depositLienRoundOff = "";
        System.out.println("termLoanT   O" + termLoanTO);
        HashMap prodMap = new HashMap();
        prodMap.put("ACCT_NUM", termLoanTO.getAcctNum());
        String authStatus = CommonUtil.convertObjToStr(termLoanTO.getAuthorizeStatus());

        List lst = sqlMap.executeQueryForList("TermLoan.getBehavesLike", prodMap);
        if (lst != null && lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
        }
        String prodId = CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
        prodMap.put("prodId", prodMap.get("PROD_ID"));

        lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
        if (lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
            eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
        }

        if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            DepositLienTO obj = new DepositLienTO();

            //                        List lst = sqlMap.executeQueryForList("TermLoan.getBehavesLike", prodMap);
            //                        if(lst !=null && lst.size()>0)
            //                            prodMap=(HashMap)lst.get(0);
            //                        String prodId=CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
            //            prodMap.put("prodId", prodMap.get("PROD_ID"));
            //            lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
            //            if (lst.size()>0) {
            //                prodMap = (HashMap)lst.get(0);
            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
            //                eligibleMargin=CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            //                depositLienRoundOff=CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
            //            }
            prodMap.put("ACCT_NUM", termLoanTO.getAcctNum());
            lst = sqlMap.executeQueryForList("getDepositbeforeAuthDetails", prodMap);//getDepositDetails
            if (lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                //                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
            }


            obj.setLienAcNo(termLoanTO.getAcctNum());
            double availBal = termLoanTO.getAdditionalLimit().doubleValue();
            availBal = (availBal / eligibleMargin * 100.0);  //85.0
            availBal = roundOffDepositLien(availBal, depositLienRoundOff);
            obj.setLienAmount(new Double(availBal));
            obj.setLienDt(curr_dt);//curr_dt);
            obj.setRemarks("Lien for LTD");
            obj.setCreditLienAcct("NA");
            obj.setDepositNo(CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_NO")));
//            obj.setDepositSubNo(CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_SUB_NO")));
            obj.setLienProdId(prodId);
            obj.setLienNo("-");
            obj.setStatus(termLoanTO.getStatus());
            //                        obj.setStatusBy(termLoanTO.getStatusBy());
            obj.setStatusDt(curr_dt);

            ArrayList lienTOs = new ArrayList();
            lienTOs.add(obj);
            HashMap objHashMap = new HashMap();
            objHashMap.put("lienTOs", lienTOs);
            objHashMap.put("SHADOWLIEN", new Double(availBal));
            objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
            objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            //System.out.println("### objHashMap : " + objHashMap);
            DepositLienDAO depLienDAO = new DepositLienDAO();
            depLienDAO.setCallFromOtherDAO(true);
           // System.out.println("objHashMap before  ####" + objHashMap);
            objHashMap = depLienDAO.execute(objHashMap);
            System.out.println("objHashMap   ####" + objHashMap);
            if (objHashMap != null) {
                HashMap updateMap = new HashMap();
                lienNo = CommonUtil.convertObjToStr(((ArrayList) objHashMap.get("LIENNO")).get(0));
                updateMap.put("ACCT_NUM", termLoanTO.getAcctNum());
                updateMap.put("SLNO", termLoanTO.getSlno());
                updateMap.put("LIEN_NO", lienNo);
                //System.out.println("updateMap####" + updateMap);
                sqlMap.executeUpdate("additionalSanctionLien", updateMap);

            }
        } else if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && authStatus.length() == 0) {
            ArrayList lienTOs = new ArrayList();
            HashMap lienCancelMap = new HashMap();
            lienCancelMap.put("LOAN_NO", termLoanTO.getAcctNum());
            List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancelMap);
            //System.out.println("getdepositliento#####" + lst);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    double availBal = termLoanTO.getAdditionalLimit().doubleValue();
                    availBal = (availBal / eligibleMargin * 100.0);  //85.0
                    availBal = roundOffDepositLien(availBal, depositLienRoundOff);


                    //                lienCancelMap=(HashMap)lst.get(0);
                    DepositLienTO depLienTO = (DepositLienTO) list.get(i);
                    depLienTO.setLienAmount(new Double(availBal));
                    depLienTO.setStatus("MODIFIED");
                    depLienTO.setStatusDt(curr_dt);
                    depLienTO.setRemarks("Lien for LTD");
                    //                    depLienTO.setUnlienDt(curr_dt);
                    depLienTO.setAuthorizeBy(depLienTO.getAuthorizeBy());
                    depLienTO.setAuthorizeDt(depLienTO.getAuthorizeDt());
                    depLienTO.setAuthorizeStatus(depLienTO.getAuthorizeStatus());
                    lienTOs.add(depLienTO);
                    DepositLienDAO depositLiendao = new DepositLienDAO();
                    lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
                    lienCancelMap.put("lienTOs", lienTOs);
                    lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                    depositLiendao.setCallFromOtherDAO(true);
                    //System.out.println("lienCancelbefore UPDATE ******" + lienCancelMap);
                    depositLiendao.execute(lienCancelMap);
                }
            }
        } else if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            HashMap lienCancelMap = new HashMap();
            lienCancelMap.put("LOAN_NO", termLoanTO.getAcctNum());
            lienCancelMap.put("PROD_ID", prodId);
            lienCancel(lienCancelMap);

        }
    }
    //regarding LTD

    private void lienAuthorize(HashMap authMap) throws Exception {
        HashMap lienAuthMap = new HashMap();
        if (authMap != null && authMap.size() > 0) {
            authMap.put("LOAN_NO", authMap.get("ACCT_NUM"));
            List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", authMap);
           // System.out.println("getdepositliento#####" + list);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    DepositLienTO depositLienTO = (DepositLienTO) list.get(i);
                    lienAuthMap.put("DEPOSIT_ACT_NUM", depositLienTO.getDepositNo());
                    lienAuthMap.put("ACCOUNT_NO", depositLienTO.getDepositNo());
                    List intlist = sqlMap.executeQueryForList("getIntRateforDeposit", lienAuthMap);
                    if (intlist != null && intlist.size() > 0) {
                        HashMap singleMap = (HashMap) intlist.get(0);
                        lienAuthMap.put("BALANCE", singleMap.get("AVAILABLE_BALANCE"));
                    }
                    lienAuthMap.put("LIENNO", depositLienTO.getLienNo());
                    lienAuthMap.put("SUBNO", depositLienTO.getDepositSubNo());
                    lienAuthMap.put("AMOUNT", depositLienTO.getLienAmount());
                    if (authMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED)) {
                        lienAuthMap.put("LIENAMOUNT", new Double(depositLienTO.getLienAmount().doubleValue()));
                    }
                    if (authMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_REJECTED)) {
                        lienAuthMap.put("LIENAMOUNT", new Double(0));
                        lienAuthMap.put("REMARKS", "LOANCLOSING"); //added by shihad
                    }
                    lienAuthMap.put("ACTION", authMap.get(CommonConstants.AUTHORIZESTATUS));
                    lienAuthMap.put("AUTHORIZE_DATE", (Date) curr_dt.clone());
                    //                lienAuthMap.put("HIERARCHY_ID",)
                    lienAuthMap.put("STATUS", depositLienTO.getStatus());
                    lienAuthMap.put("BRANCH_CODE", _branchCode);
                    lienAuthMap.put("AUTHORIZEDT", (Date) curr_dt.clone());
                    lienAuthMap.put("SHADOWLIEN", String.valueOf(depositLienTO.getLienAmount().doubleValue()));
                    lienAuthMap.put("USER_ID", authMap.get(CommonConstants.USER_ID));
                    lienAuthMap.put("AUTHORIZE_STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                    lienAuthMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                    lienAuthMap.put("COMMAND_STATUS", depositLienTO.getStatus());

                    DepositLienDAO depositLiendao = new DepositLienDAO();
                    depositLiendao.setCallFromOtherDAO(true);
                    //System.out.println("lienAuthMap  ###" + lienAuthMap);
                    depositLiendao.execute(lienAuthMap);
                }

            }
        }
    }

    /*
     * authorize time need not reject wole live account only reject additional
     * sanction only
     */
    private boolean additionalSanctionLTD() throws Exception {
        if (additionalSanMap != null && additionalSanMap.size() > 0) {
            System.out.println("additionalSanMap###" + additionalSanMap);
            Iterator iterator = additionalSanMap.keySet().iterator();
            for (int i = 0; i < additionalSanMap.size(); i++) {
                if (iterator.hasNext()) {
                    TermLoanAdditionalSanctionTO termLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(CommonUtil.convertObjToStr(iterator.next()));
                    System.out.println("termLoanAdditionalSanctionTO###" + termLoanAdditionalSanctionTO);
                    if (termLoanAdditionalSanctionTO.getAuthorizeStatus() == null || termLoanAdditionalSanctionTO.getAuthorizeStatus().length() == 0) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    private void executeUpdate(String str, Object objTermLoanTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTermLoanTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateNetworthDetails() throws Exception {
        try {
            sqlMap.executeUpdate("updateCustNetworthDetailsTL", netWorthDetailsMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private HashMap getProdBehavesLike(HashMap dataMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("TermLoan.getBehavesLike", dataMap);
        if (list.size() > 0) {
            HashMap retrieveMap = (HashMap) list.get(0);
            dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
            dataMap.put("prodId", retrieveMap.get(PROD_ID));
            if (CommonUtil.convertObjToStr(retrieveMap.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")
                    || CommonUtil.convertObjToStr(retrieveMap.get(PROD_ID)).equals("OD") || (dataMap.containsKey("MULTIDISBURSE") && CommonUtil.convertObjToStr(dataMap.get("MULTIDISBURSE")).equals("Y"))) {
              
                dataMap.put(LIMIT, dataMap.get(LIMIT));
            } else {
                dataMap.put(LIMIT, retrieveMap.get(LIMIT));
            }
            dataMap.put(BEHAVES_LIKE, retrieveMap.get(BEHAVES_LIKE));
            dataMap.put(INT_GET_FROM, retrieveMap.get(INT_GET_FROM));
            dataMap.put(SECURITY_DETAILS, retrieveMap.get(SECURITY_DETAILS));
            System.out.println("dataMap  ##" + dataMap);
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
            HashMap getActNoMap = new HashMap();
            getActNoMap = (HashMap) selectedList.get(0);

            RuleEngine engine;
            RuleContext context;
            ArrayList list;
            String partillyReject = "";
            if (map.containsKey("PARTIALLY_REJECT")) {
                partillyReject = CommonUtil.convertObjToStr(map.get("PARTIALLY_REJECT"));
            }

            sqlMap.startTransaction();


            if (getActNoMap.containsKey("LIEN_DETAILS")) {
                ArrayList lienList = (ArrayList) getActNoMap.get("LIEN_DETAILS");
                if (lienList != null && lienList.size() > 0) {
                    for (int i = 0; i < lienList.size(); i++) {
                        HashMap lienMap = (HashMap) lienList.get(i);
                        lienMap.put("TODAY_DT", curr_dt);
                       
                        lienMap.put("SHADOWLIEN", new Double(CommonUtil.convertObjToDouble(lienMap.get("SHADOWLIEN")).doubleValue()));
                        if (lienMap.get("STATUS").equals("AUTHORIZED")) {
                           
                            sqlMap.executeUpdate("updateSubAcInfoBal", lienMap);
                            sqlMap.executeUpdate("updateLienForMDS", lienMap);
                            sqlMap.executeUpdate("updateSubACInfoStatusToLien", lienMap);
                        } else {
                            sqlMap.executeUpdate("updateLienForMDS", lienMap);
                            sqlMap.executeUpdate("updateSubAcInfoBal", lienMap);
                        }
                    }
                }

            }

            for (int i = selectedList.size() - 1, j = 0; i >= 0; --i, ++j) {
                dataMap = (HashMap) selectedList.get(j);

                // Get the Produt's Behaves Like
                dataMap = getProdBehavesLike(dataMap);

                engine = new RuleEngine();
                context = new RuleContext();
                list = new ArrayList();
                if (status != null && status.equals("AUTHORIZED")) {
                    context.addRule(new InterestDetailsValidationRule());
                }
                if (dataMap.containsKey(SECURITY_DETAILS) && dataMap.get(SECURITY_DETAILS).equals(FULLY_SECURED) && status.equals("AUTHORIZED")) {
                    context.addRule(new SecurityDetailsValidationRule());
                }

                /*
                 * LOANS INTEREST DETAILS AUTHORIZATION STATUS UPDATION
                 */
                if (status != null && status.length() > 0) {
                    authorizeLoanInterestDetails(status);
                }

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(AUTHORIZEDT, curr_dt);//curr_dt);
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
                /**
                 * LTD AUTHORZE LIEN *
                 */
                if (!getActNoMap.containsKey("LIEN_DETAILS")) {
                    lienAuthorize(dataMap);
                }
                /**
                 * validating all rule map *
                 */
                list = (ArrayList) engine.validateAll(context, dataMap);

                if (list != null) {
                    HashMap exception = new HashMap();
                    exception.put(CommonConstants.EXCEPTION_LIST, list);
                    exception.put(CommonConstants.CONSTANT_CLASS, RULE_MAP_PATH);
                    System.out.println("Exception List : " + list);

                    throw new TTException(exception);
                }
                dataMap.put("ACT_NUM", dataMap.get("ACCT_NUM"));

                sqlMap.executeUpdate("authorizeTermLoanCourtDetailsTO", dataMap);
                updateCaseDetails(dataMap);

                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                if (additionalSanctionLTD()) {
                    dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                }

                System.out.println("dataMap  ###" + dataMap);
                int transCount = checkTransaction(dataMap);
                updateMobilebanking(dataMap);
                if (status.length() > 0) {
                    if (rejectLiveAccount(dataMap, partillyReject)) {
                        if (transCount > 0) {
                            dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                            sqlMap.executeUpdate("updateLoansSanctionDetails", dataMap);
                        }
                    }
                }

                sqlMap.executeUpdate("authorizeTermLoan", dataMap);
                HashMap aHmap = new HashMap();
                aHmap.put("acctNum", dataMap.get("ACCOUNTNO"));
                aHmap.put("authorizeStatus", dataMap.get("AUTHORIZESTATUS"));
                aHmap.put("authorizeDt", dataMap.get("AUTHORIZEDT"));
                aHmap.put("authorizeBy", dataMap.get("USER_ID"));
               	sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", aHmap);

                getAuthorizeNPA(dataMap);
                if (status != null) {
                    authorizeAdditionalSanction(status);
                }
                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    getAuthorizeTransaction(map, status, dataMap);
                    sqlMap.executeUpdate("updateRestructringInstallment", dataMap);
                    //Addded By Suresh
                    HashMap appMap = new HashMap();
                    System.out.println("################ objTermLoanBorrowerTO : " + objTermLoanBorrowerTO);
                    if (objTermLoanBorrowerTO.getCustId().length() > 0 && objTermLoanBorrowerTO.getApplicationNo() != null && objTermLoanBorrowerTO.getApplicationNo().length() > 0) {
                        appMap.put("CUSTOMER_ID", CommonUtil.convertObjToStr(objTermLoanBorrowerTO.getCustId()));
                        appMap.put("APPLICATION_NO", CommonUtil.convertObjToStr(objTermLoanBorrowerTO.getApplicationNo()));
                        sqlMap.executeUpdate("updateLoanAppRegisterStatus", appMap);
                    }
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    getAuthorizeTransaction(map, status, dataMap);
                    sqlMap.executeUpdate("updateReschduleStatus", dataMap);
                    sqlMap.executeUpdate("updateInstallmentStatus", dataMap);
                    if (dataMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("OD") || CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("CC") && transCount > 0) {
                        rejectODAccount(dataMap);
                    }
                    // Exisiting status is Created or Modified
                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        //                            sqlMap.executeUpdate("rejectInventoryDetails", dataMap);
                    }
                }

                if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    if (productCategory.equals("PADDY_LOAN")) {
                        HashMap paddyUpdateMap = new HashMap();
                        paddyUpdateMap.put("ACT_NUM", getActNoMap.get("ACCT_NUM"));
                        paddyUpdateMap.put("LOAN_STATUS", "");
                        paddyUpdateMap.put("LOAN_GIVEN", "N");
                        paddyUpdateMap.put("CND_NO", keyValue);
                        executeUpdate("updatePaddyLoanStatus", paddyUpdateMap);
                    }

                    if (productCategory.equals("MDS_LOAN")) {
                        HashMap mdsUpdateMap = new HashMap();
                        mdsUpdateMap.put("ACT_NUM", getActNoMap.get("ACCT_NUM"));
                        List mdsList = null;
                        mdsList = (List) sqlMap.executeQueryForList("getMDSApplicationDetails", mdsUpdateMap);
                        if(mdsList!=null && mdsList.size()>0){
                        HashMap mdsMap = new HashMap();
                        mdsMap = (HashMap) mdsList.get(0); 
                        mdsUpdateMap.put("LOAN_STATUS", "");
                        mdsUpdateMap.put("LOAN_GIVEN", "N");
                        String chittalNo = "";
                        chittalNo = CommonUtil.convertObjToStr(mdsMap.get("CHITTAL_NO"));
                        String subNo = "";
                        subNo = CommonUtil.convertObjToStr(mdsMap.get("SUB_NO"));
//                        if (keyValue.indexOf("_") != -1) {
//                            chittalNo = keyValue.substring(0, keyValue.indexOf("_"));
//                            subNo = keyValue.substring(keyValue.indexOf("_") + 1, keyValue.length());
//                        }
                        mdsUpdateMap.put("CHITTAL_NO", chittalNo);
                        mdsUpdateMap.put("SUB_NO", subNo);
//                        mdsUpdateMap.put("CHITTAL_NO", keyValue);
                        executeUpdate("updateMDSLoanStatus", mdsUpdateMap);
                        }
                    }
                }

                // CASE AUTHORIZE
                System.out.println("#@$#@@%@$%@getActNoMap : " + getActNoMap);
                List lst = (List) sqlMap.executeQueryForList("getSelectTermLoanCaseDetail", getActNoMap);
                //System.out.println("#@$#@@%@$%@CaseDetailTO" + lst);
                //System.out.println("#@$#@@%@$%@ListSize : " + lst.size());

                if (lst != null && lst.size() > 0) {
                    TermLoanChargesTO objChargeTO = null;
                    for (int k = 0; k < lst.size(); k++) {
                        objChargeTO = new TermLoanChargesTO();
                        HashMap CaseMap = (HashMap) lst.get(k);
                        String caseStatus = CommonUtil.convertObjToStr(CaseMap.get("CASE_STATUS"));
                        String fileCharge = CommonUtil.convertObjToStr(CaseMap.get("FILING_FEES"));
                        String miscCharge = CommonUtil.convertObjToStr(CaseMap.get("MISC_CHARGES"));
                        String prod_Id = CommonUtil.convertObjToStr(dataMap.get(PROD_ID));
                        //System.out.println("$#@$#@$@@dataMap" + dataMap);
                        //System.out.println("$#@$#@$@@prod_id" + prod_Id);
                        objChargeTO.setProd_Id(prod_Id);
                        objChargeTO.setProd_Type("TL");
                        objChargeTO.setAct_num(CommonUtil.convertObjToStr(CaseMap.get("ACT_NUM")));
                        objChargeTO.setChargeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(CaseMap.get("FILING_DT"))));
                        objChargeTO.setStatus_Dt(curr_dt);
                        objChargeTO.setStatus_By(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        objChargeTO.setAuthorize_Dt(curr_dt);
                        objChargeTO.setAuthorize_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        objChargeTO.setAuthorize_Status(status);
                        String costType = "";
                        String expType = "";
                        System.out.println("$#@$#@$@@caseStatus" + caseStatus);
                        if (caseStatus.equals("Arbitration Case")) {
                            costType = "ARC Cost";
                            expType = "ARC Expense";
                        } else if (caseStatus.equals("Execution of Award")) {
                            costType = "EA Cost";
                            expType = "EA Expense";
                        } else if (caseStatus.equals("Execution Process")) {
                            costType = "EP Cost";
                            expType = "EP Expense";
                        }
                        objChargeTO.setCharge_Type(costType);
                        List chargeCostList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                        System.out.println("$#@$#@$@@ chargeCostList : " + chargeCostList);
                        objChargeTO.setCharge_Type(expType);
                        List chargeExpList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                        System.out.println("$#@$#@$@@chargeExpList : " + chargeExpList);
                        if (chargeCostList != null && chargeCostList.size() > 0) {
                            HashMap chargeCostMap = (HashMap) chargeCostList.get(0);
                            String chargeNo = CommonUtil.convertObjToStr(chargeCostMap.get("CHARGE_NO"));
                            objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            objChargeTO.setCharge_Type(costType);
                            objChargeTO.setAmount(CommonUtil.convertObjToDouble(fileCharge));
                            objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                            sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                        } else {
                            objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                            objChargeTO.setCharge_Type(costType);
                            objChargeTO.setAmount(CommonUtil.convertObjToDouble(fileCharge));
                            sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                        }
                        if (chargeExpList != null && chargeExpList.size() > 0) {
                            HashMap chargeExpMap = (HashMap) chargeExpList.get(0);
                            String chargeNo = CommonUtil.convertObjToStr(chargeExpMap.get("CHARGE_NO"));
                            objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            objChargeTO.setCharge_Type(expType);
                            objChargeTO.setAmount((Double) CommonUtil.convertObjToDouble(miscCharge));
                            objChargeTO.setChargeGenerateNo(new Long(CommonUtil.convertObjToLong(chargeNo)));
                            sqlMap.executeUpdate("updateTermLoanChargeTO", objChargeTO);
                        } else {
                            objChargeTO.setCharge_Type(expType);
                            objChargeTO.setAmount((Double) CommonUtil.convertObjToDouble(miscCharge));
                            sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                        }

                        objChargeTO.setCharge_Type(costType);
                        chargeCostList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                        System.out.println("$#@$#@$@@ chargeCostList : " + chargeCostList);
                        objChargeTO.setCharge_Type(expType);
                        chargeExpList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                        System.out.println("$#@$#@$@@chargeExpList : " + chargeExpList);
                        if (chargeCostList != null && chargeCostList.size() > 0) {
                            HashMap chargeCostMap = (HashMap) chargeCostList.get(0);
                            sqlMap.executeUpdate("updateChargeNoCostFromTermLoanAcctCharge", chargeCostMap);
                        }
                        if (chargeExpList != null && chargeExpList.size() > 0) {
                            HashMap chargeExpMap = (HashMap) chargeExpList.get(0);
                            sqlMap.executeUpdate("updateChargeNoExpFromTermLoanAcctCharge", chargeExpMap);
                        }
                    }
                }
               
                //SECURITY DETAILS
                if (i == 0) {
                    sqlMap.executeUpdate("authorizeMemberDetails", getActNoMap);
                    sqlMap.executeUpdate("authorizeCollateralDetails", getActNoMap);
                    sqlMap.executeUpdate("authorizeTermLoanDepositType", getActNoMap);
                    sqlMap.executeUpdate("authorizeTermLoanLosType", getActNoMap);

                }
                logTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                logTO.setData(dataMap.toString());
                logTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                logDAO.addToLog(logTO);

                context = null;
                engine = null;
            }
            if (authMap.containsKey("SERVICE_TAX_AUTH")) {
                HashMap serAuth_Map = new HashMap();
                serAuth_Map.put("STATUS",authMap.get(CommonConstants.AUTHORIZESTATUS));
                serAuth_Map.put("USER_ID", map.get(CommonConstants.USER_ID));
                serAuth_Map.put("AUTHORIZEDT", curr_dt);
                serAuth_Map.put("ACCT_NUM", objTermLoanBorrowerTO.getBorrowNo());
                sqlMap.executeUpdate("authorizeServiceTaxDetails", serAuth_Map);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateMobilebanking(HashMap dataMap) throws Exception {
        if (dataMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("OD") || CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("CC")) {
            dataMap.put("PROD_TYPE", "AD");
        } else {
            dataMap.put("PROD_TYPE", "TL");
        }
        dataMap.put("ACCOUNTNO", dataMap.get("LOAN_NO"));
        dataMap.put("STATUS", dataMap.get("AUTHORIZESTATUS"));
        sqlMap.executeUpdate("authorizeSMSSubscriptionMap", dataMap);        
    }
//    private void gahanSecurityValue()throws Exception{
//         HashMap resultMap=new HashMap();
//             if(collateralTableDetails!=null && collateralTableDetails.size()>0){
//            ArrayList addList =new ArrayList(collateralTableDetails.keySet());
//            for(int i=0;i<addList.size();i++){
//                TermLoanSecurityLandTO objMemberTypeTO = (TermLoanSecurityLandTO) collateralTableDetails.get(addList.get(i));
//                 resultMap=new HashMap();
//                 resultMap.put("AMOUNT",objMemberTypeTO.getPledgeAmount());
//                 resultMap.put("DOC_NO",objMemberTypeTO.getDocumentNo());
//                 sqlMap.executeUpdate("minusSecurityDetails", resultMap);
//            }
//        }
//    }
    /*
     * check whether the transaction happend or not for particulor account
     */

    private int checkTransaction(HashMap dataMap) throws Exception {

        List lst = null;
        int transCount = 0;
        HashMap resultCountMap = new HashMap();
        if (dataMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("OD") || CommonUtil.convertObjToStr(dataMap.get("BEHAVES_LIKE")).equals("CC")) {
            lst = (List) sqlMap.executeQueryForList("checkTransactionAD", dataMap);
        } else {
            lst = (List) sqlMap.executeQueryForList("checkTransaction", dataMap);
        }
        if (lst != null && lst.size() > 0) {
            resultCountMap = (HashMap) lst.get(0);
            transCount = CommonUtil.convertObjToInt(resultCountMap.get("CNT"));
        }
        return transCount;
    }

    private void rejectODAccount(HashMap map) throws Exception {
        System.out.println("map    ###" + map);
        TermLoanFacilityTO objTermLoanFacilityTO = (TermLoanFacilityTO) sqlMap.executeQueryForObject("getSelectTermLoanRenewalFacilityTO", map);
        if (objTermLoanFacilityTO != null) {
            sqlMap.executeUpdate("updateTermLoanFacilityTO", objTermLoanFacilityTO);
        }
        TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) sqlMap.executeQueryForObject("getSelectTermLoanSanctionRenewalFacilityTO", map);
        if (objTermLoanSanctionFacilityTO != null) {
            sqlMap.executeUpdate("updateTermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTO);
        }

    }
    /*
     * live account going to making rejection means it s called partial
     * rejection that time loan authorize status should be authorized
     */

    private boolean rejectLiveAccount(HashMap dataMap, String partial) throws Exception {
        HashMap updateMap = new HashMap();
//        System.out.println("dataMap#####"+dataMap);
        updateMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
        updateMap.put("CURR_DT", curr_dt);
        if (partial.equals("PARTIALLY_REJECT")) {
            sqlMap.executeUpdate("updateLoansInstallmentRejectTL", updateMap);
            sqlMap.executeUpdate("updateRepaymentRejectTL", updateMap);
            sqlMap.executeUpdate("updateLoansInstallmentNewRejectTL", updateMap);
            sqlMap.executeUpdate("updateRepaymentNewRejectTL", updateMap);
            return true;
        }
        return false;

    }

    private void getAuthorizeTransaction(HashMap map, String status, HashMap dataMap) throws Exception {
        HashMap cashAuthMap = new HashMap();
        cashAuthMap.put(CommonConstants.BRANCH_ID, dataMap.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put(CommonConstants.USER_ID, dataMap.get(CommonConstants.USER_ID));
        cashAuthMap.put("INITIATED_BRANCH", dataMap.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
        cashAuthMap.put("TODAY_DT", curr_dt);
//        cashAuthMap.put("DAILY", "DAILY");
        String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));
        System.out.println(cashAuthMap + "  linkBatchId####" + linkBatchId);
        //added by sreekrishnan for cashier authorization
        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
        if (listData != null && listData.size() > 0) {
            HashMap map1 = (HashMap) listData.get(0);
            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                List lst = sqlMap.executeQueryForList("getCashTransTransID", cashAuthMap);
                if (lst != null && lst.size() > 0) {
                    HashMap obj = (HashMap) lst.get(0);
                    cashAuthMap.put("LOAN_OPENING_PAYMENT", "LOAN_OPENING_PAYMENT");
                    cashAuthMap.put("TRANS_ID", obj.get("TRANS_ID"));
                }
            }
        }
        System.out.println("linkBatchId or TRANS_ID"+cashAuthMap.get("TRANS_ID"));
        cashAuthMap.put("TERM_LOAN_DAO", "TERM_LOAN_DAO");
        TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
        cashAuthMap = null;

    }

    private void getRejactCaseTransaction(HashMap dataMap, TermLoanCaseDetailTO objTermLoanCaseDetailTO) throws Exception {
        HashMap cashAuthMap = new HashMap();
        cashAuthMap.put("INITIATED_BRANCH", dataMap.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put(CommonConstants.USER_ID, dataMap.get(CommonConstants.USER_ID));
        cashAuthMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
        cashAuthMap.put("TODAY_DT", curr_dt);
        cashAuthMap.put(CommonConstants.BRANCH_ID, dataMap.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put("DAILY", "DAILY");
        String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));

        System.out.println(cashAuthMap + "  linkBatchId####" + linkBatchId);

        //        if(CommonUtil.convertObjToDouble(objTermLoanCaseDetailTO.getFillingFees()).doubleValue()>0){
        cashAuthMap.put("AUTHORIZE_REMARKS", objTermLoanCaseDetailTO.getCaseStatus());
        //        }else if (CommonUtil.convertObjToDouble(objTermLoanCaseDetailTO.getMiscCharges()).doubleValue()>0){
        //            cashAuthMap.put("AMOUNT",objTermLoanCaseDetailTO.getMiscCharges());
        //        }
        List lst = sqlMap.executeQueryForList("getTransferTransBatchID", cashAuthMap);
        if (lst != null && lst.size() > 0) {
            HashMap obj = (HashMap) lst.get(0);
            cashAuthMap.put("BATCH_ID", obj.get("BATCH_ID"));
        }
        TransactionDAO.authorizeCashAndTransfer(linkBatchId, "REJECTED", cashAuthMap);
        cashAuthMap = null;

    }

    void getAuthorizeNPA(HashMap dataMap) throws Exception {
        System.out.println("authorize data###" + dataMap);
        List lst = sqlMap.executeQueryForList("SELECTNPA_HISTORY", dataMap);
        if (lst.size() > 0) {
            sqlMap.executeUpdate("updateNPA_HISTORY", dataMap);
        }
    }

    private void authorizeLoanInterestDetails(String status) throws Exception {
        if (interestMap != null) {
            Set set = interestMap.keySet();
            Object obj[] = (Object[]) set.toArray();
            for (int i = 0; i < obj.length; i++) {
                TermLoanInterestTO objTermLoanInterestTO = (TermLoanInterestTO) interestMap.get(obj[i]);
                if (objTermLoanInterestTO.getAuthorizeStatus().equals("")) {
                    objTermLoanInterestTO.setAuthorizeStatus(status);
                    if (status.equals(CommonConstants.STATUS_REJECTED)) {
                        sqlMap.executeUpdate("updateIntMaintenanceTL", objTermLoanInterestTO);
                    }

                    System.out.println("objTermLoanInterestTO   " + objTermLoanInterestTO);
                    sqlMap.executeUpdate("deleteTermLoanInterestTO", objTermLoanInterestTO);
                }

            }
        }
    }
    private String generateLinkID() throws Exception
    {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        newLoan = false;
        HashMap hash;
        ibrHierarchy = 1;
        
        System.out.println("inside  DataCorrectionDAO####### nithyaaa" + map);

        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
      
       String correctionType = CommonUtil.convertObjToStr(map.get("CORRECTION_TYPE"));
        if(correctionType.equals("NARRATION_CHANGE")){
            resultMap = doNarrationChange(map);
        }else if(correctionType.equals("HEAD_CHANGE")){
            resultMap = doActHeadChange(map);
        }else if(correctionType.equals("GOLD_ITEM_CHANGE")){
            resultMap = doGoldItemChange(map);
        }else if(correctionType.equals("GOLD_WEIGHT_CHANGE")){
            resultMap = doGoldWeightChange(map);
        }else if(correctionType.equals("INDEND_LIABILITY_AMT_CHANGE")){
            
        }else if(correctionType.equals("TRANS_TYPE_INTERCHANGE")){
            resultMap = doTransactionTypeInterChange(map);
        }else if(correctionType.equals("OA_ACTNO_CHANGE")){
            resultMap = doOperativeAccountNumberChange(map);
        }else if(correctionType.equals("OTHERBANK_ACTNO_CHANGE")){
            resultMap = doOtherBankAccountNumberChange(map);
        }else if(correctionType.equals("SUSPENSE_ACTNO_CHANGE")){
            resultMap = doSuspenseAccountNumberChange(map);
        }else if(correctionType.equals("TRANS_AMT_CHANGE")){
            resultMap = doTransactionAmountChange(map);
        }else if(correctionType.equals("OA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("SA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")){
            resultMap = doAllAcctNumberChange(map);
        }else if(correctionType.equals("GL_TO_ACTNO_MAPPING")){
            resultMap = doGLToAcctNumberChange(map);
        }else if(correctionType.equals("TRANS_NARRATION_CHANGE")){
            resultMap = doTransactionNarrationChange(map);
        }
        destroyObjects();
        if (returnMap.size() > 0) {
            return returnMap;
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return resultMap;
    }
    
    private void chargesTransactionPart(HashMap dataMap) throws Exception {//Added By Suresh 23-Aug-2019
        try {
            String accHead = "";
            String chargeDesc = "";
            double chargeAmt = 0;
            HashMap serviceTaxDetails = new HashMap();
            double swachhCess = 0.0;
            double krishikalyanCess = 0.0;
            double serTaxAmt = 0.0;
            double totalServiceTaxAmt = 0.0;
            double normalServiceTax = 0.0;
            HashMap chargeMap = new HashMap();
            HashMap chargeTransMap = new HashMap();
            chargeTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
            chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
            chargeTransMap.put("BRANCH_CODE", _branchCode);
            chargeTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
            String generateCashId = generateLinkID();//Charge Details Transaction
            if (dataMap.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) dataMap.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    // Added by nithya on 30-12-2019 for KD-1131
                    if (dataMap.containsKey("serviceTax_Details") && dataMap.get("serviceTax_Details") != null) {
                        serviceTaxDetails = (HashMap) dataMap.get("serviceTax_Details");
                    }
                    if (serviceTaxDetails != null && serviceTaxDetails.size() > 0) {                      
                        if (serviceTaxDetails.containsKey("TOT_TAX_AMT") && serviceTaxDetails.get("TOT_TAX_AMT") != null) {
                            serTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                            totalServiceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxDetails.get("TOT_TAX_AMT"));
                        }
                        if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                            swachhCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SWACHH_CESS));
                        }
                        if (serviceTaxDetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                            krishikalyanCess = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                        }
                        if (serviceTaxDetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                            normalServiceTax = CommonUtil.convertObjToDouble(serviceTaxDetails.get(ServiceTaxCalculation.SERVICE_TAX));
                        }
                    }
                    // END - 1131
                    if (CommonUtil.convertObjToStr(transactionTO.getTransType()).equals("CASH")) {
                        ArrayList loanAuthTransList = new ArrayList();
                        for (int i = 0; i < chargeLst.size(); i++) {
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            chargeDesc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            System.out.println("$#@@$ chargeMap : " + chargeMap);
                            //System.out.println("$#@@$ chargeAmt" + chargeAmt);
                            //System.out.println("$#@@$ chargeDesc" + chargeDesc);
                            if (chargeAmt > 0) {
                                chargeTransMap.put("ACCT_HEAD", accHead);
                                chargeTransMap.put("LIMIT", String.valueOf(chargeAmt));
                                chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                chargeTransMap.put("SINGLE_TRANS_ID", generateCashId);
                                //chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                chargeTransMap.put("CREDIT_CHARGES", "CREDIT_CHARGES");
                                chargeTransMap.put("MANUAL_AUTHORIZE", "MANUAL_AUTHORIZE");
                                chargeTransMap.put(CommonConstants.PARTICULARS, chargeDesc + " - : " + CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(chargeTransMap));
                            }
                        }
                        
                        // Added by nithya on 30-12-2019 for KD-1131
                        if (swachhCess > 0) {
                            chargeTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID")));
                            chargeTransMap.put("LIMIT", String.valueOf(swachhCess));
                            chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
                            chargeTransMap.put("SINGLE_TRANS_ID", generateCashId);
                            //chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            chargeTransMap.put("CREDIT_CHARGES", "CREDIT_CHARGES");
                            chargeTransMap.put("MANUAL_AUTHORIZE", "MANUAL_AUTHORIZE");
                            chargeTransMap.put(CommonConstants.PARTICULARS, "CGST - : " + CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(chargeTransMap));
                        }
                        if (krishikalyanCess > 0) {
                            chargeTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID")));
                            chargeTransMap.put("LIMIT", String.valueOf(krishikalyanCess));
                            chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
                            chargeTransMap.put("SINGLE_TRANS_ID", generateCashId);
                            //chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            chargeTransMap.put("CREDIT_CHARGES", "CREDIT_CHARGES");
                            chargeTransMap.put("MANUAL_AUTHORIZE", "MANUAL_AUTHORIZE");
                            chargeTransMap.put(CommonConstants.PARTICULARS, "SGST - : " + CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(chargeTransMap));
                        }
                        if (normalServiceTax > 0) {
                            chargeTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID")));
                            chargeTransMap.put("LIMIT", String.valueOf(normalServiceTax));
                            chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
                            chargeTransMap.put("SINGLE_TRANS_ID", generateCashId);
                            //chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                            chargeTransMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            chargeTransMap.put("CREDIT_CHARGES", "CREDIT_CHARGES");
                            chargeTransMap.put("MANUAL_AUTHORIZE", "MANUAL_AUTHORIZE");
                            chargeTransMap.put(CommonConstants.PARTICULARS, "SERVICE TAX - : " + CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            loanAuthTransList.add(loanAuthorizeTimeTransactionForCashObj(chargeTransMap));
                        }
                        //End - 1131                        
                        if (loanAuthTransList != null && loanAuthTransList.size() > 0) {
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            chargeTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                            chargeTransMap.put("BRANCH_CODE", _branchCode);
                            HashMap returnMap = cashTransactionDAO.execute(chargeTransMap, false);
                        }
                    } else if (CommonUtil.convertObjToStr(transactionTO.getTransType()).equals("TRANSFER")) {
                        double totalChargeAmt = 0.0;
                        HashMap txMap = new HashMap();
                        HashMap crMap = new HashMap();
                        TransactionDAO transactionDAO = null;
                        ArrayList transferList = new ArrayList();
                        TxTransferTO txTransferTO = new TxTransferTO();
                        TransferTrans transferTrans = new TransferTrans();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setLinkBatchID((String) dataMap.get("ACCT_NUM"));
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        String chargeType = "";
                        for (int i = 0; i < chargeLst.size(); i++) {
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            System.out.println("  $#@@$ accHead : " + accHead);
                            System.out.println("$#@@$ chargeAmt : " + chargeAmt);
                            //CREDIT PART CHARGES
                            if (chargeAmt > 0) {
                                totalChargeAmt = totalChargeAmt + chargeAmt;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, accHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, chargeType + " - : " + dataMap.get("ACCT_NUM"));
                                txTransferTO = transactionDAO.addTransferCreditLocal(txMap, chargeAmt);
                                txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                txTransferTO.setLinkBatchId((String) dataMap.get("ACCT_NUM"));
                                txTransferTO.setSingleTransId(generateCashId);
                                txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                                transferList.add(txTransferTO);
                            }
                        }                        
                        
                          // Added by nithya on 30-12-2019 for KD-1131
                          if (swachhCess > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("SWACHH_HEAD_ID")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "CGST - : " + dataMap.get("ACCT_NUM"));
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId((String) dataMap.get("ACCT_NUM"));
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        if (krishikalyanCess > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("KRISHIKALYAN_HEAD_ID")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "SGST - : " + dataMap.get("ACCT_NUM"));
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId((String) dataMap.get("ACCT_NUM"));
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        if (normalServiceTax > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(serviceTaxDetails.get("TAX_HEAD_ID")));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX - : " + dataMap.get("ACCT_NUM"));
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId((String) dataMap.get("ACCT_NUM"));
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        totalChargeAmt = totalChargeAmt + totalServiceTaxAmt;    
                        // End - 1131
                        
                        //DEBIT PART
                        if (totalChargeAmt > 0) {
                            if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            } else {
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                txMap.put("TRANS_MOD_TYPE", transactionTO.getProductType());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACCT_NUM"));
                            txMap.put("DR_INST_TYPE", "VOUCHER");
                            txMap.put("SINGLE_TRANS_ID", generateCashId);
                            if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("AUTHORIZEREMARKS", "DP");
                            }
                            txTransferTO = transactionDAO.addTransferDebitLocal(txMap, totalChargeAmt);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId((String) dataMap.get("ACCT_NUM"));
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        if (transferList != null && transferList.size() > 0) {
                            transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    private HashMap riskFundTransactionPart(HashMap dataMap) throws Exception {
        try {
            
            String riskFundAchd = "";
            String ServiceTaxAchd = "";
            String cgstAchd = "";
            String sgstAcHd = "";
            double riskFundAmt = 0.0;
            double floodCessAmt = 0.0;
            double cgstAmt = 0.0;
            double sgstAmt = 0.0;
            String acctNum = "";
            double totalAmt = 0.0;  
            TransferDAO transferDAO = new TransferDAO();
            HashMap chargeTransMap = new HashMap();
            chargeTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
            chargeTransMap.put("USER_ID", dataMap.get("USER_ID"));
            chargeTransMap.put("BRANCH_CODE", _branchCode);
            chargeTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));            
            String prodDesc =  CommonUtil.convertObjToStr(dataMap.get("PROD_DESC"));
            riskFundAchd =  CommonUtil.convertObjToStr(dataMap.get("RISK_FUND_AC_HD"));
            HashMap headMap = new HashMap();
            headMap.put("SCHEME_ID",prodDesc);
            List list = (List) sqlMap.executeQueryForList("getSelectRiskFundAndTaxHeadIds", headMap);
            if(list != null && list.size() > 0){
                headMap = (HashMap)list.get(0);
                cgstAchd = CommonUtil.convertObjToStr(headMap.get("SWACHH_HEAD_ID"));
                sgstAcHd = CommonUtil.convertObjToStr(headMap.get("KRISHI_HEAD_ID"));
                ServiceTaxAchd = CommonUtil.convertObjToStr(headMap.get("TAX_HEAD_ID"));
            }
            if (dataMap.containsKey("KCC_RISK_FUND_LIST")) {
                if (riskFundList!= null &&  riskFundList.size() > 0 ) {                    
                    for(int i=0; i <riskFundList.size(); i++){    
                         sqlMap.startTransaction();
                       HashMap riskFunMap = (HashMap)riskFundList.get(i);  
                        System.out.println("riskfundMap :: " + riskFunMap);
                        acctNum = CommonUtil.convertObjToStr(riskFunMap.get("ACCT_NUM"));
                        riskFundAmt = CommonUtil.convertObjToDouble(riskFunMap.get("RISK_FUND"));
                        floodCessAmt = CommonUtil.convertObjToDouble(riskFunMap.get("FLOOD_CESS"));
                        cgstAmt = CommonUtil.convertObjToDouble(riskFunMap.get("CGST"));
                        sgstAmt = CommonUtil.convertObjToDouble(riskFunMap.get("SGST"));
                        totalAmt = riskFundAmt + floodCessAmt + sgstAmt + cgstAmt ;
                       // {RISK_FUND=1000, ACCT_NUM=0001294000570, PROD_ID=294, FLOOD_CESS=10, PROD_DESC=Member OD, SGST=90, CGST=90}
                        //getSelectRiskFundAndTaxHeadIds
                        HashMap txMap = new HashMap();
                        HashMap crMap = new HashMap();
                        TransactionDAO transactionDAO = null;
                        ArrayList transferList = new ArrayList();
                        TxTransferTO txTransferTO = new TxTransferTO();
                        TransferTrans transferTrans = new TransferTrans();
                        String generateCashId = generateLinkID();//Charge Details Transaction
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setLinkBatchID((String) dataMap.get("ACCT_NUM"));
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        if (riskFundAmt > 0) {
                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, riskFundAchd);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                                
                                txMap.put(TransferTrans.PARTICULARS, "Risk Fund" + " - : " + acctNum);
                                txTransferTO = transactionDAO.addTransferCreditLocal(txMap, riskFundAmt);
                                txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                txTransferTO.setLinkBatchId(acctNum);
                                txTransferTO.setInitiatedBranch(_branchCode);
                                txTransferTO.setSingleTransId(generateCashId);
                                txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                                transferList.add(txTransferTO);
                            }
                         if (cgstAmt > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, cgstAchd);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "CGST - : " + acctNum);
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, cgstAmt);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId(acctNum);
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setInitiatedBranch(_branchCode);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        if (sgstAmt > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, sgstAcHd);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "SGST - : " + acctNum);
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, sgstAmt);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId((String) acctNum);
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setInitiatedBranch(_branchCode);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }
                        if (floodCessAmt > 0) {
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, ServiceTaxAchd);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "SERVICE TAX - : " +acctNum);
                            txTransferTO = transactionDAO.addTransferCreditLocal(txMap, floodCessAmt);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId(acctNum);
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setInitiatedBranch(_branchCode);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }                        
                        if (totalAmt > 0) {
                            txMap.put(TransferTrans.DR_PROD_TYPE, "AD");
                            txMap.put(TransferTrans.DR_ACT_NUM, acctNum);
                            txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                            txMap.put("TRANS_MOD_TYPE", "AD");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.PARTICULARS, acctNum);
                            txMap.put("DR_INST_TYPE", "VOUCHER");
                            txMap.put("SINGLE_TRANS_ID", generateCashId);
                            txMap.put("AUTHORIZEREMARKS", "DP");
                            txTransferTO = transactionDAO.addTransferDebitLocal(txMap, totalAmt);
                            txTransferTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            txTransferTO.setLinkBatchId(acctNum);
                            txTransferTO.setSingleTransId(generateCashId);
                            txTransferTO.setInitiatedBranch(_branchCode);
                            txTransferTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                            transferList.add(txTransferTO);
                        }                        
                        HashMap data = new HashMap();
                        data.put("TxTransferTO", transferList);
                        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        data.put("MODE", CommonConstants.TOSTATUS_INSERT);
                        data.put(CommonConstants.BRANCH_ID, _branchCode);
                        data.put(CommonConstants.SCREEN,CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
                        data.put("DEBIT_LOAN_TYPE", "DP");
                        //AUTHORIZE_MAP                                                                      
//                        HashMap authorizeMap = new HashMap();
//                        authorizeMap.put("BATCH_ID", null);
//                        authorizeMap.put("USER_ID", logTO.getUserId());
//                        authorizeMap.put("DEBIT_LOAN_TYPE", "DP");
//                        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
//                        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                        System.out.println("data... :: " + data);
                        try {
                            transReturnMap = transferDAO.execute(data, false);
                            dotransferAuthorize(CommonUtil.convertObjToStr(transReturnMap.get("TRANS_ID")),CommonConstants.STATUS_AUTHORIZED, data);
                            System.out.println("transReturnMap :: " + transReturnMap);
                            HashMap updateMap = new HashMap();
                            updateMap.put("ACT_NUM", acctNum);
                            sqlMap.executeUpdate("updateRiskFundProcessStatus", updateMap);
                        } 
                        catch (Exception e) {
                            sqlMap.rollbackTransaction();
                            if (e instanceof TTException) {
                                TTException exception = (TTException) e;
                                HashMap exceptionHashMap = exception.getExceptionHashMap();
                                exceptionHashMap.put("ACCT_NUM",acctNum);
                                throw e;
                            } else {
                                throw new TTException(e);
                            }
                        } 
                        sqlMap.commitTransaction();
                    }
                }
                 
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return transReturnMap;
    }
    
   private void dotransferAuthorize(String batchid, String status, HashMap authMap) throws Exception {
        TransactionTO transactionTO;
        HashMap cashAuthMap, tempMap, transIdMap, transferTransParam;
        ArrayList cashTransList, transferTransList, transactionList;
        transactionList = new ArrayList();
        transferTransParam = new HashMap();
        transferTransParam.put(CommonConstants.BRANCH_ID, authMap.get(CommonConstants.BRANCH_ID));
        transferTransParam.put(CommonConstants.USER_ID, authMap.get(CommonConstants.USER_ID));
        transferTransParam.put("BATCHID", batchid);
        transferTransParam.put("BATCH_ID", batchid);
        transferTransParam.put(CommonConstants.AUTHORIZESTATUS, status);
        transferTransParam.put("DEBIT_LOAN_TYPE","DP");
        transferTransParam.put("TRANS_DT", curr_dt.clone());
        transferTransParam.put("TRANS_DATE", curr_dt.clone());
        transferTransParam.put("INITIATED_BRANCH", _branchCode);
        transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
        if (transferTransList != null && (!transferTransList.isEmpty())) {
            TransferTrans objTrans = new TransferTrans();            
            objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            transferTransParam.put(CommonConstants.STATUS, status);                          
        } else {
            throw new TTException("Transfer List Is Empty");
        }
        transferTransList = null;
        transferTransParam = null;
    }

    private void insertLosTableDetails() throws Exception {

        if (losTableDetails != null) {
            ArrayList addList = new ArrayList(losTableDetails.keySet());

            for (int i = 0; i < addList.size(); i++) {
                TermLoanLosTypeTO objTermLoanLosTypeTO = (TermLoanLosTypeTO) losTableDetails.get(addList.get(i));

                objTermLoanLosTypeTO.setBorrowNo(borrower_No);
                objTermLoanLosTypeTO.setStatusDt(curr_dt);
                objTermLoanLosTypeTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertLosSecurityTableDetails", objTermLoanLosTypeTO);

                objTermLoanLosTypeTO = null;
            }
        }
    }

    private void insertDepositTableDetails() throws Exception {

        if (depositTableDetails != null) {
            ArrayList addList = new ArrayList(depositTableDetails.keySet());
            double prizedAmount = 0.0;
            double avialbleBal = 0.0;
            for (int i = 0; i < addList.size(); i++) {
                TermLoanDepositTypeTO objTermLoanDepositTypeTO = (TermLoanDepositTypeTO) depositTableDetails.get(addList.get(i));

                objTermLoanDepositTypeTO.setBorrowNo(borrower_No);
                objTermLoanDepositTypeTO.setStatusDt(curr_dt);
                objTermLoanDepositTypeTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertDepositSecurityTableDetails", objTermLoanDepositTypeTO);

                HashMap hmap = new HashMap();
                ArrayList alist = new ArrayList();
                System.out.println("objDepositTypeTO@@@@" + objTermLoanDepositTypeTO);
                hmap.put("DEPOSIT_NO", objTermLoanDepositTypeTO.getTxtDepNo());
                List list = sqlMap.executeQueryForList("getAvailableBalForDep", hmap);

                if (objTermLoanDepositTypeTO.getProdType().equals("TD") || objTermLoanDepositTypeTO.getProdType().equals("Deposits")) {
                    if (securAmt > 0.0) {
                        hmap = (HashMap) list.get(0);
                        avialbleBal = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        DepositLienTO objDepositLienTO = new DepositLienTO();
                        objDepositLienTO.setLienAcNo(acct_No);
                        objDepositLienTO.setDepositNo(objTermLoanDepositTypeTO.getTxtDepNo());
                        if (securAmt > avialbleBal) {
                            objDepositLienTO.setLienAmount(new Double(avialbleBal));
                            securAmt = securAmt - avialbleBal;
                        } else {
                            objDepositLienTO.setLienAmount(new Double(securAmt));
                        }
                        objDepositLienTO.setLienDt(curr_dt);
                        objDepositLienTO.setStatus(objTermLoanDepositTypeTO.getStatus());
                        objDepositLienTO.setStatusBy(objTermLoanDepositTypeTO.getStatusBy());
                        objDepositLienTO.setStatusDt(objTermLoanDepositTypeTO.getStatusDt());
                        objDepositLienTO.setRemarks("Lien From Loan");
//                        objDepositLienTO.setDepositSubNo("1");
                        objDepositLienTO.setLienNo("-");
                        DepositLienDAO depositLienDao = new DepositLienDAO();
                        depositLienDao.setCallFromOtherDAO(true);
                        alist.add(objDepositLienTO);
                        hmap.put("lienTOs", alist);
                        hmap.put("SHADOWLIEN", objDepositLienTO.getLienAmount());
                        hmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        hmap.put("BRANCH_CODE", _branchCode);
                        depositLienDao.execute(hmap);
                    }
                } else {
                    securAmt = securAmt - CommonUtil.convertObjToDouble(objTermLoanDepositTypeTO.getTxtMaturityValue()).doubleValue();
                }



                objTermLoanDepositTypeTO = null;

            }
        }
    }

    /**
     * upto date interest calculate and to the outstanding amount and create new
     * repay schdule existing schdule become inactive newly declared only active
     * *
     */
    private void repayschduleTransaction(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("map######" + map);
        if (map.containsKey("INT_TRANSACTION_REPAYMENT")) {
            HashMap reserchMap = new HashMap();
            reserchMap = (HashMap) map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(reserchMap.get("BRANCH_CODE"));
            try {
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", reserchMap.get("ACT_NUM"));
                transactionDAO.setLinkBatchID((String) reserchMap.get("ACT_NUM"));
                transactionDAO.setInitiatedBranch(branchId);
                transferTrans.setLinkBatchId((String) reserchMap.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_ACT_NUM, reserchMap.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_ID, reserchMap.get("PROD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put(TransferTrans.DR_BRANCH, reserchMap.get("BRANCH_CODE"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("AUTHORIZEREMARKS", "DI");
                double interestAmt = CommonUtil.convertObjToDouble(reserchMap.get("INTEREST_AMT")).doubleValue();

                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                //                transferDao.setAuthorizeRemarks("DI");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, reserchMap.get("BRANCH_CODE"));
                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                //DELETEING RECORD
                reserchMap.put("LINK_BATCH_ID", reserchMap.get("ACT_NUM"));
                reserchMap.put("TRANS_DT", curr_dt);
                reserchMap.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", reserchMap);
                TxTransferTO txtransferTO = new TxTransferTO();
                ArrayList batchList = new ArrayList();
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        txtransferTO = new TxTransferTO();
                        TxTransferTO singletxtransferTO = new TxTransferTO();
                        singletxtransferTO = (TxTransferTO) lst.get(i);
                        txtransferTO.setActNum(singletxtransferTO.getActNum());
                        txtransferTO.setAcHdId(singletxtransferTO.getAcHdId());
                        txtransferTO.setAmount(singletxtransferTO.getAmount());
                        txtransferTO.setBatchId(singletxtransferTO.getBatchId());
                        txtransferTO.setBranchId(singletxtransferTO.getBranchId());
                        txtransferTO.setInitiatedBranch(singletxtransferTO.getInitiatedBranch());
                        txtransferTO.setLinkBatchId(singletxtransferTO.getLinkBatchId());
                        txtransferTO.setProdId(singletxtransferTO.getProdId());
                        txtransferTO.setProdType(singletxtransferTO.getProdType());
                        txtransferTO.setStatus("DELETED");
                        txtransferTO.setTransDt(singletxtransferTO.getTransDt());
                        txtransferTO.setTransId(singletxtransferTO.getTransId());
                        txtransferTO.setTransType(singletxtransferTO.getTransType());
                        txtransferTO.setTransMode(singletxtransferTO.getTransMode());
                        batchList.add(txtransferTO);
                    }
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    TransferTrans transfer = new TransferTrans();
                    transfer.setInitiatedBranch((String) reserchMap.get("BRANCH_CODE"));
                    transfer.doDebitCredit(batchList, (String) reserchMap.get("BRANCH_CODE"), false, "INSERT");


                }
                //
                transferDao = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                System.out.println("transList  ###" + transList + "transferDao   :" + transferDao);
                transferTrans.setLoanDebitInt(new String("DI"));
                transferTrans.setInitiatedBranch((String) reserchMap.get("BRANCH_CODE"));
                transferTrans.doDebitCredit(transList, branchId, false);
                HashMap hash = new HashMap();
                //                hash.put("ACCOUNTNO",map.get("ACT_NUM"));
                //                Date curr_dt=(Date)map.get("CURR_DATE");
                //                hash.put("LAST_CALC_DT",DateUtil.addDays(curr_dt,-1));
                //                sqlMap.executeUpdate("updateclearBal", hash);

                //                transactionDAO.doTransferLocal(transList, branchId);
                //            transactionDAO.setCommandMode(commandMode);
            } catch (Exception e) {
                //                throw new TTException(" A/c Head not set.");
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
            //            return returnMap;
        }

    }

    private void todCreation(HashMap AdvancesLiablityExceedLimit) throws Exception {
        TodAllowedTO todAllowed = new TodAllowedTO();
        TodAllowedTO todAllowedExist = new TodAllowedTO();
        TodAllowedDAO todallowedDao = new TodAllowedDAO();
        HashMap todMap = new HashMap();
        String mode = null;
        System.out.println("AdvancesLiablityExceedLimit@@@@" + AdvancesLiablityExceedLimit);
        AdvancesLiablityExceedLimit.put("CURR_DT", curr_dt);
        List lst = sqlMap.executeQueryForList("getSelectExistAccountList", AdvancesLiablityExceedLimit);
        todAllowed.setAccountNumber(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("ACCT_NUM")));
        if (!(AdvancesLiablityExceedLimit.containsKey("MODE") && AdvancesLiablityExceedLimit.get("MODE") == null)) {
            if (lst != null && lst.size() > 0) {
                todAllowedExist = (TodAllowedTO) lst.get(0);
                mode = "UPDATE";
                todAllowed.setTrans_id(todAllowedExist.getTrans_id());
                //           todAllowed.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
                todAllowed.setFromDate(curr_dt);
                todAllowed.setToDate(curr_dt);
                todAllowed.setPermittedDt(curr_dt);
                //           todAllowed.setStatusBy(ProxyParameters.USER_ID);
                //           todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setRemarks(todAllowed.getRemarks());
                todAllowed.setProductType(todAllowedExist.getProductType());
                todAllowed.setProductId(todAllowedExist.getProductId());
                todAllowed.setProductId(todAllowedExist.getProductId());
                todAllowed.setPermitedBy(todAllowedExist.getPermitedBy());
                todAllowed.setTodAllowed(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("DIFFERENT_AMT")));
                todAllowed.setPermissionRefNo(todAllowedExist.getPermissionRefNo());
                todAllowed.setBranchCode(_branchCode);
                todAllowed.setTypeOfTOD("SINGLE");
                todMap.put("MODE", mode);
                todMap.put("BRANCH_CODE", _branchCode);
                todMap.put("TodAllowed", todAllowed);
                todMap.put("AUTHORIZEMAP", null);
                System.out.println("todMap#######" + todMap);
                todallowedDao.execute(todMap);

            } else {
                mode = "INSERT";
                todAllowed.setTrans_id(todAllowedExist.getTrans_id());
                //           todAllowed.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
                todAllowed.setFromDate(curr_dt);
                todAllowed.setToDate(curr_dt);
                todAllowed.setPermittedDt(curr_dt);
                //           todAllowed.setStatusBy(ProxyParameters.USER_ID);
                //           todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setProductType("AD");
                todAllowed.setProductId(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("PROD_ID")));
                todAllowed.setPermitedBy(todAllowedExist.getPermitedBy());
                todAllowed.setTodAllowed(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("DIFFERENT_AMT")));
                todAllowed.setPermissionRefNo("");
                todAllowed.setBranchCode(_branchCode);
                todAllowed.setTypeOfTOD("SINGLE");
                todMap.put("MODE", mode);
                todMap.put("BRANCH_CODE", _branchCode);
                todMap.put("TodAllowed", todAllowed);
                todMap.put("AUTHORIZEMAP", null);
                System.out.println("todMap#######" + todMap);
                todallowedDao.execute(todMap);

            }
        } else {
            AdvancesLiablityExceedLimit.put(CommonConstants.BRANCH_ID, _branchCode);
            System.out.println("AdvancesLiablityExceedLimit#######" + AdvancesLiablityExceedLimit);
            todallowedDao.execute(AdvancesLiablityExceedLimit);
        }
    }

    public void decalreAssetStatus(Date instalDt, HashMap paramMap, HashMap hashMap, Date curr_dt, String behaves_like) throws Exception {
        String ASSET_STATUS = null;
        try {
            List lst = sqlMap.executeQueryForList("getAssetStatusNo", paramMap);
            if (lst != null && lst.size() > 0) {
                HashMap resultMap = (HashMap) lst.get(0);
                hashMap.put("ASSET_STATUS", resultMap.get("CURR_STATUS"));
            }
            if (instalDt != null) {
                paramMap.put("INSTALLMENT_DT", instalDt);
                int asset_status = CommonUtil.convertObjToInt(hashMap.get("ASSET_STATUS"));

                //                System.out.println(asset_status.equals("STANDARD_ASSETS")+"parammap#####"+paramMap);
                int period = 0;

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_LOSS"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 6) {
                        ASSET_STATUS = "LOSS_ASSETS";
                        asset_status = 6;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_3"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 5) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_3";
                        asset_status = 5;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_2"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 4) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_2";
                        asset_status = 4;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 3) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_1";
                        asset_status = 3;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
                System.out.println(period + "datediff#####" + DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 2) {
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
                        asset_status = 2;
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
//                    hashMap.put("TODAY_DT", paramMap.get("TODAY_DT"));
                    hashMap.put("TODAY_DT",CommonUtil.getProperDate(curr_dt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("TODAY_DT")))));
                    //                        hash.put("STATUS_BY","TTSYSTEM");
                    hashMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                    sqlMap.executeUpdate("INSERT_NPA_HISTORY", hashMap);
                    System.out.println("finalmap####" + hashMap);
                    hashMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                    sqlMap.executeUpdate("NPA_std_to_substdtest", hashMap);
                }

                //                        }


                /*
                 * sqlMap.executeUpdate("NPA_std_to_substd", map); //
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_1",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_1", map); hash=new
                 * HashMap(); for( i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS",ASSET_STATUS);
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_2",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_2",map); hash=new
                 * HashMap(); for(i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","DOUBTFUL_ASSETS_2");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_3",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_3",map); hash=new
                 * HashMap(); for(i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","DOUBTFUL_ASSETS_3");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_DOUBT_3_TO_LOSS",paramMap);
                 * sqlMap.executeUpdate("NPA_doubt_3_to_loss", paramMap);
                 * hash=new HashMap(); for( i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","LOSS_ASSETS");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 */
            }
            //            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("inside kccrenewaldao executequery...");
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
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
            //System.out.println("getFirstTranDetails####" + transDetails);
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
                            inst_dt = DateUtil.getDateWithoutMinitues(ServerUtil.getCurrentDate(_branchCode));
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
        dailyLoanMap = null;
        oTSMap = null;
        caseDetailMap = null;
        sanctionFacilityMap = null;
        facilityMap = null;
        securityMap = null;
        repaymentMap = null;
        installmentMap = null;
        installmentMultIntMap = null;
        guarantorMap = null;
        guaranInstitMap = null;
        documentMap = null;
        interestMap = null;
        additionalSanMap = null;
        objTermLoanClassificationTO = null;
        objTermLoanOtherDetailsTO = null;
        objAuthorizedSignatoryDAO = null;
        objPowerOfAttorneyDAO = null;
        memberTableDetails = null;
        deletedMemberTableValues = null;
        collateralTableDetails = null;
        deletedCollateralTableValues = null;
        chargeLst = null;
        behaves_like = null;
        kccTo = null;
        objCustomerGoldStockSecurityTO = null; // Added by nithya on 07-03-2020 for KD-1379
    }
 private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setAcct_Num(objTermLoanBorrowerTO.getBorrowNo());
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanApplicationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
  private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            data.put("WHERE", "LA00000000001061");
            data.put("KEY_VALUE", "ACCOUNT_NUMBER");
            new KCCMultipleRenewalDAO().executeQuery(data);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
     private HashMap doOperativeAccountNumberChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newActNo =  objDataCorrectionLogTO.getNewFieldValue();
        String oldActNo = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("ACT_NUM",newActNo);
        actNoUpdateMap.put("OLD_ACT_NUM",oldActNo);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }
            if (objDataCorrectionLogTO.getInterBranchAcct().equals("Y")) {
                HashMap transRefGLUpdateMap = new HashMap();
                transRefGLUpdateMap.put("TRANS_DT", transDt);
                if (transMod.equals("CASH")) {
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",objDataCorrectionLogTO.getTransId());
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getTransId());
                } else if (transMod.equals("TRANSFER")) {
                    String batchTransId = objDataCorrectionLogTO.getBatchId()+"_"+objDataCorrectionLogTO.getTransId();
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",batchTransId);
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getBatchId());
                }                
                sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
            }
            actNoUpdateMap.put("ACCOUNT_NO",oldActNo);
            sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);
            actNoUpdateMap.put("ACCOUNT_NO",newActNo);
            sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
     
     private HashMap doTransactionAmountChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        double newAmount =  CommonUtil.convertObjToDouble(objDataCorrectionLogTO.getNewFieldValue());
        double oldAmount = CommonUtil.convertObjToDouble(objDataCorrectionLogTO.getOldFieldValue());
        double changeAmt = 0.0;
        String transId = objDataCorrectionLogTO.getTransId();
        String batchId = objDataCorrectionLogTO.getBatchId();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("AMOUNT",newAmount);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("BATCH_ID",batchId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("BRANCH_ID",objDataCorrectionLogTO.getBranchCode());
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
        try {
           sqlMap.startTransaction();
           HashMap headUpdateMap =  new HashMap();
           sqlMap.executeUpdate("updateTransferTransAmountAfterCorrection", actNoUpdateMap); 
           HashMap transDataParamMap =  new HashMap();
           transDataParamMap.put("BRANCH_ID",objDataCorrectionLogTO.getBranchCode());
           transDataParamMap.put("TRANS_DT",objDataCorrectionLogTO.getTransDt());
           transDataParamMap.put("BATCH_ID", objDataCorrectionLogTO.getBatchId());
           List transDataList = (ArrayList) sqlMap.executeQueryForList("getAllTransferBatchDataForUpdation", transDataParamMap);
           if(transDataList != null && transDataList.size() > 0){
               for (int i = 0; i < transDataList.size(); i++) {
                   TxTransferTO objTxTransferTO = (TxTransferTO) transDataList.get(i);
                   System.out.println("objTxTransferTO :: " + objTxTransferTO);
                   if (objTxTransferTO.getProdType().equals("GL")) {
                       if (objTxTransferTO.getTransType().equals("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = oldAmount - newAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equals("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = newAmount - oldAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                   } else if (objTxTransferTO.getProdType().equals("OA")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());
                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equals("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = oldAmount - newAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equals("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = newAmount - oldAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                       
                   } else if (objTxTransferTO.getProdType().equals("SA")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());
                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);

                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equals("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = oldAmount - newAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equals("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = newAmount - oldAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                       
                       
                   } else if (objTxTransferTO.getProdType().equals("AB")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());

                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);

                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equals("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = oldAmount - newAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equals("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = newAmount - oldAmount;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                   }
               }
               
           }           
           
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
     private HashMap doTransactionTypeInterChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        HashMap transIdMap = (HashMap)dataMap.get("TRANS_ID_MAP");
        double newAmount =  CommonUtil.convertObjToDouble(objDataCorrectionLogTO.getNewFieldValue());
        double oldAmount = CommonUtil.convertObjToDouble(objDataCorrectionLogTO.getOldFieldValue());
        double changeAmt = 0.0;
        String transId = objDataCorrectionLogTO.getTransId();
        String batchId = objDataCorrectionLogTO.getBatchId();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("AMOUNT",newAmount);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("BATCH_ID",batchId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("BRANCH_ID",objDataCorrectionLogTO.getBranchCode());
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
        try {
           sqlMap.startTransaction();
           HashMap headUpdateMap =  new HashMap();
           HashMap transDataParamMap =  new HashMap();
           transDataParamMap.put("BRANCH_ID",objDataCorrectionLogTO.getBranchCode());
           transDataParamMap.put("TRANS_DT",objDataCorrectionLogTO.getTransDt());
           transDataParamMap.put("BATCH_ID", objDataCorrectionLogTO.getBatchId());
           List transDataList = (ArrayList) sqlMap.executeQueryForList("getAllTransferBatchDataForUpdation", transDataParamMap);
            System.out.println("transIdMap :: " + transIdMap);
            String transType = "";
            if (transDataList != null && transDataList.size() > 0) {
                for (int k = 0; k < transDataList.size(); k++) {
                   TxTransferTO objTxTransferTO = (TxTransferTO) transDataList.get(k);
                   HashMap updateMap =  new HashMap();
                   updateMap.put("TRANS_ID",objTxTransferTO.getTransId());
                   updateMap.put("TRANS_DT",objTxTransferTO.getTransDt());
                   if(CommonUtil.convertObjToStr(transIdMap.get(objTxTransferTO.getTransId())).equalsIgnoreCase("Debit")){
                       transType = "DEBIT";
                   }else if(CommonUtil.convertObjToStr(transIdMap.get(objTxTransferTO.getTransId())).equalsIgnoreCase("Credit")){
                       transType = "CREDIT";
                   }
                   updateMap.put("TRANS_TYPE",transType);
                   updateMap.put("BRANCH_ID",objDataCorrectionLogTO.getBranchCode());
                   sqlMap.executeUpdate("updateTransferTransTypeAfterCorrection", updateMap);
                  }
            }
           transDataList = (ArrayList) sqlMap.executeQueryForList("getAllTransferBatchDataForUpdation", transDataParamMap);
           if(transDataList != null && transDataList.size() > 0){
               for (int i = 0; i < transDataList.size(); i++) {
                   TxTransferTO objTxTransferTO = (TxTransferTO) transDataList.get(i);
                   System.out.println("objTxTransferTO :: " + objTxTransferTO);
                   if (objTxTransferTO.getProdType().equals("GL")) {
                       if (objTxTransferTO.getTransType().equalsIgnoreCase("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = -2* objTxTransferTO.getAmount();
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equalsIgnoreCase("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = 2*objTxTransferTO.getAmount() ;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                   } else if (objTxTransferTO.getProdType().equals("OA")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());
                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equalsIgnoreCase("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = -2* objTxTransferTO.getAmount();
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equalsIgnoreCase("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = 2*objTxTransferTO.getAmount() ;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                       
                   } else if (objTxTransferTO.getProdType().equals("SA")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());
                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);

                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equalsIgnoreCase("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = -2* objTxTransferTO.getAmount();
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equalsIgnoreCase("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = 2*objTxTransferTO.getAmount() ;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                       
                       
                   } else if (objTxTransferTO.getProdType().equals("AB")) {
                       if (!objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())) {
                           HashMap transRefGLUpdateMap = new HashMap();
                           transRefGLUpdateMap.put("TRANS_DT", transDt);
                           String batchTransId = objTxTransferTO.getBatchId() + "_" + objTxTransferTO.getTransId();
                           transRefGLUpdateMap.put("BATCH_TRANS_ID", batchTransId);
                           transRefGLUpdateMap.put("TRANS_ID", objTxTransferTO.getBatchId());

                           sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                           sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);

                       }
                       actNoUpdateMap.put("ACCOUNT_NO", objTxTransferTO.getActNum());
                       sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);
                       
                       String oldAcctBranchCode = "";
                       HashMap interBranchCodeMap = new HashMap();
                       interBranchCodeMap.put("ACT_NUM", objTxTransferTO.getActNum());
                       List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                       if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                           interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                           oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                       }
                       
                       
                       if (objTxTransferTO.getTransType().equalsIgnoreCase("DEBIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = -2* objTxTransferTO.getAmount();
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       } else if (objTxTransferTO.getTransType().equalsIgnoreCase("CREDIT")) {
                           headUpdateMap.put("BRANCH_CODE", oldAcctBranchCode);
                           headUpdateMap.put("AC_HD_ID", objTxTransferTO.getAcHdId());
                           changeAmt = 2*objTxTransferTO.getAmount() ;
                           headUpdateMap.put("AMOUNT", changeAmt);
                           sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                       }
                       
                       
                   }
               }
               
           }           
           
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
      
    
     private HashMap doSuspenseAccountNumberChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newActNo =  objDataCorrectionLogTO.getNewFieldValue();
        String oldActNo = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();;
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("ACT_NUM",newActNo);
        actNoUpdateMap.put("OLD_ACT_NUM",oldActNo);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }
            if(objDataCorrectionLogTO.getInterBranchAcct().equals("Y")){
               HashMap transRefGLUpdateMap = new HashMap();
                transRefGLUpdateMap.put("TRANS_DT", transDt);
                if (transMod.equals("CASH")) {
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",objDataCorrectionLogTO.getTransId());
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getTransId());
                } else if (transMod.equals("TRANSFER")) {
                    String batchTransId = objDataCorrectionLogTO.getBatchId()+"_"+objDataCorrectionLogTO.getTransId();
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",batchTransId);
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getBatchId());
                }                
                sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
            }
            actNoUpdateMap.put("ACCOUNT_NO",oldActNo);
            sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);
            actNoUpdateMap.put("ACCOUNT_NO",newActNo);
            sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
     private HashMap doOtherBankAccountNumberChange(HashMap dataMap) throws Exception{
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        HashMap headUpdateMap = new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newActNo =  objDataCorrectionLogTO.getNewFieldValue();
        String oldActNo = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();;
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("ACT_NUM",newActNo);
        actNoUpdateMap.put("OLD_ACT_NUM",oldActNo);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
        actNoUpdateMap.put("AC_HD_ID",objDataCorrectionLogTO.getOtherBankAcctHead());
        actNoUpdateMap.put("AB_PROD_ID",objDataCorrectionLogTO.getOtherBankAcctProdId());
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransOtherBankActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransOtherBankActNumAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }
            if (objDataCorrectionLogTO.getInterBranchAcct().equals("Y")) {
               HashMap transRefGLUpdateMap = new HashMap();
                transRefGLUpdateMap.put("TRANS_DT", transDt);
                if (transMod.equals("CASH")) {
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",objDataCorrectionLogTO.getTransId());
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getTransId());
                } else if (transMod.equals("TRANSFER")) {
                    String batchTransId = objDataCorrectionLogTO.getBatchId()+"_"+objDataCorrectionLogTO.getTransId();
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",batchTransId);
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getBatchId());
                }                
                sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
            }
            actNoUpdateMap.put("ACCOUNT_NO",oldActNo);
            sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);
            actNoUpdateMap.put("ACCOUNT_NO",newActNo);
            sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);
            
            //GL updations in case of different prodid
            HashMap acHeads = new HashMap();
            acHeads.put("INVESTMENT_ACC_NO", oldActNo);
            List acHdLst = sqlMap.executeQueryForList("getOthrBankAccountAcHdAndProdId", acHeads);
            if (acHdLst != null && acHdLst.size() > 0) {
                acHeads = (HashMap) acHdLst.get(0);
                String oldProdId = CommonUtil.convertObjToStr(acHeads.get("PROD_ID"));
                if (!oldProdId.equals(objDataCorrectionLogTO.getOtherBankAcctProdId())) {
                    String oldOtherBankHeadId = CommonUtil.convertObjToStr(acHeads.get("PRINCIPAL_AC_HD"));
                    String newOtherBankHeadId = objDataCorrectionLogTO.getOtherBankAcctHead();
                    System.out.println("TransType :: " + objDataCorrectionLogTO.getTransType());
                    headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
                    if (objDataCorrectionLogTO.getTransType().equals("DEBIT")) {
                        headUpdateMap.put("AC_HD_ID", oldOtherBankHeadId);
                        headUpdateMap.put("AMOUNT", objDataCorrectionLogTO.getAmount());
                        checkGL(oldOtherBankHeadId, objDataCorrectionLogTO.getBranchCode(), objDataCorrectionLogTO.getTransType());
                        sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                        headUpdateMap.put("AC_HD_ID", newOtherBankHeadId);
                        headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount() * -1));
                        checkGL(newOtherBankHeadId, objDataCorrectionLogTO.getBranchCode(), objDataCorrectionLogTO.getTransType());
                        sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                    } else if (objDataCorrectionLogTO.getTransType().equals("CREDIT")) {
                        headUpdateMap.put("AC_HD_ID", oldOtherBankHeadId);
                        headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount() * -1));
                        checkGL(oldOtherBankHeadId, objDataCorrectionLogTO.getBranchCode(), objDataCorrectionLogTO.getTransType());
                        sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                        headUpdateMap.put("AC_HD_ID", newOtherBankHeadId);
                        headUpdateMap.put("AMOUNT", objDataCorrectionLogTO.getAmount());
                        checkGL(newOtherBankHeadId, objDataCorrectionLogTO.getBranchCode(), objDataCorrectionLogTO.getTransType());
                        sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                    }
                }

            }
            
            
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    } 
     
     
     
    
     private HashMap doNarrationChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap narrationUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newNarration =  objDataCorrectionLogTO.getNewFieldValue();
        String oldNarration = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();
        String actNum = objDataCorrectionLogTO.getActNum();
        String prodType = objDataCorrectionLogTO.getProdType();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        narrationUpdateMap.put("NARRATION",newNarration);
        narrationUpdateMap.put("TRANS_ID",transId);
        narrationUpdateMap.put("TRANS_DT",transDt);
        narrationUpdateMap.put("ACT_NUM", actNum);
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransactionNarration", narrationUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransactionNarration", narrationUpdateMap);
            }            
            if(prodType.equals("OA") || prodType.equals("AD")){
              sqlMap.executeUpdate("updateTransactionPassBookNarration", narrationUpdateMap);
            }
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
    private HashMap doTransactionNarrationChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap narrationUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newNarration =  objDataCorrectionLogTO.getNewFieldValue();
        String oldNarration = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();
        String actNum = objDataCorrectionLogTO.getActNum();
        String prodType = objDataCorrectionLogTO.getProdType();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        narrationUpdateMap.put("NARRATION",newNarration);
        narrationUpdateMap.put("TRANS_ID",transId);
        narrationUpdateMap.put("TRANS_DT",transDt);
        narrationUpdateMap.put("ACT_NUM", actNum);
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransNarration", narrationUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransNarration", narrationUpdateMap);
            }
            if(prodType.equals("OA") || prodType.equals("AD")){
              sqlMap.executeUpdate("updateTransactionPassBookNarrationCorrectedData", narrationUpdateMap);
            }
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    } 
    
     private HashMap doGoldWeightChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap narrationUpdateMap =  new HashMap();
        HashMap goldDataMap =  (HashMap)dataMap.get("GOLD_DATA_MAP"); 
         System.out.println("goldDataMap inside doGoldWeightChange :: " + goldDataMap);
        String newgrossWeigt =  CommonUtil.convertObjToStr(goldDataMap.get("NEW_GROSS_WEIGHT"));
        String oldGrossWeight =  CommonUtil.convertObjToStr(goldDataMap.get("OLD_GROSS_WEIGT"));
        String newNetWeigt =  CommonUtil.convertObjToStr(goldDataMap.get("NEW_NET_WEIGHT"));
        String oldNetWeight =  CommonUtil.convertObjToStr(goldDataMap.get("OLD_NET_WEIGHT"));
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        objDataCorrectionLogTO.setOldFieldValue("GrossWt:"+oldGrossWeight+" NetWt:"+oldNetWeight);
        objDataCorrectionLogTO.setNewFieldValue("GrossWt:"+newgrossWeigt+" NetWt:"+newNetWeigt);
        goldDataMap.put("ACT_NUM",objDataCorrectionLogTO.getActNum());
        String transId = objDataCorrectionLogTO.getTransId();
        String actNum = objDataCorrectionLogTO.getActNum();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateGoldWeightAfterCorrection", goldDataMap);
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
      private HashMap doGoldItemChange(HashMap dataMap){
        HashMap returnMap = new HashMap();
        HashMap goldItemUpdateMap =  new HashMap();
        HashMap goldDataMap =  (HashMap)dataMap.get("GOLD_DATA_MAP"); 
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        System.out.println("dataMap inside doGoldItemChange :: " + dataMap);
        String particulars = objDataCorrectionLogTO.getNewFieldValue();
        goldItemUpdateMap.put("ACT_NUM",objDataCorrectionLogTO.getActNum());
        goldItemUpdateMap.put("PARTICULARS",particulars);

        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateGoldItemsAfterCorrection", goldItemUpdateMap);
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return returnMap;
    }
    
    
    private HashMap doActHeadChange(HashMap dataMap) throws Exception{
        HashMap returnMap = new HashMap();
        HashMap headUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String newHeadId =  objDataCorrectionLogTO.getNewFieldValue();
        String oldHeadId = objDataCorrectionLogTO.getOldFieldValue();
        String transId = objDataCorrectionLogTO.getTransId();
        String actNum = objDataCorrectionLogTO.getActNum();
        String prodType = objDataCorrectionLogTO.getProdType();
        String transMod = objDataCorrectionLogTO.getTransMode();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        double amount = objDataCorrectionLogTO.getAmount();
        String transType = objDataCorrectionLogTO.getTransType();
        
        headUpdateMap.put("AC_HD_ID",newHeadId);
        headUpdateMap.put("TRANS_ID",transId);
        headUpdateMap.put("TRANS_DT",transDt);
        headUpdateMap.put("ACT_NUM", actNum);
        headUpdateMap.put("BRANCH_CODE", objDataCorrectionLogTO.getBranchCode());
        try {
            sqlMap.startTransaction();
            if(transMod.equals("CASH")){
                sqlMap.executeUpdate("updateCashTransactionHeadId", headUpdateMap);
            }else if(transMod.equals("TRANSFER")){
                sqlMap.executeUpdate("updateTransferTransactionHeadId", headUpdateMap);
            }
            
           if(transType.equals("DEBIT")){
                headUpdateMap.put("AC_HD_ID",oldHeadId);
                headUpdateMap.put("AMOUNT", amount);
                checkGL(oldHeadId,objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                headUpdateMap.put("AC_HD_ID",newHeadId);
                headUpdateMap.put("AMOUNT", (amount*-1));  
                checkGL(newHeadId,objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }else if(transType.equals("CREDIT")){
                headUpdateMap.put("AC_HD_ID",oldHeadId);
                headUpdateMap.put("AMOUNT", (amount*-1));
                checkGL(oldHeadId,objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                headUpdateMap.put("AC_HD_ID",newHeadId);
                headUpdateMap.put("AMOUNT", amount);
                checkGL(newHeadId,objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }
            
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            // GL - GL_ABSTRACT changes pending
            
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
    
    
     private void checkGL(String act_num, String branchCode, String transType) throws Exception {
        GLUpdateDAO objDAO = new GLUpdateDAO();
        GLTO objGLTO = new GLTO();
        objGLTO.setAcHdId(act_num);
        objGLTO.setBranchCode(branchCode);
        if (!objDAO.isGLEntryExists(objGLTO)) {
            objGLTO.setBalanceType(transType);
            objGLTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objGLTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertGLTO", objGLTO);
        }
    }
     
     
     
     // Data Correction tool - Version 2.0 changes
     
      private HashMap doAllAcctNumberChange(HashMap dataMap) throws Exception{
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String transId = objDataCorrectionLogTO.getTransId();
        String transMod = objDataCorrectionLogTO.getTransMode();
        String transType = objDataCorrectionLogTO.getTransType();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("ACT_NUM",objDataCorrectionLogTO.getNewAcctNum());
        actNoUpdateMap.put("OLD_ACT_NUM",objDataCorrectionLogTO.getOldAcctNum());
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());
          if (objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)) {
              actNoUpdateMap.put("NEW_ACT_NUM", null);
              actNoUpdateMap.put("NEW_PROD_ID", null);
              actNoUpdateMap.put("NEW_PROD_TYPE", objDataCorrectionLogTO.getNewProdType());
              actNoUpdateMap.put("NEW_AC_HD_ID", objDataCorrectionLogTO.getNewHeadId());
              actNoUpdateMap.put("OLD_PROD_TYPE", objDataCorrectionLogTO.getOldProdType());
          } else {
              actNoUpdateMap.put("NEW_ACT_NUM", objDataCorrectionLogTO.getNewAcctNum());
              actNoUpdateMap.put("NEW_PROD_ID", objDataCorrectionLogTO.getNewProdId());
              actNoUpdateMap.put("NEW_PROD_TYPE", objDataCorrectionLogTO.getNewProdType());
              actNoUpdateMap.put("NEW_AC_HD_ID", objDataCorrectionLogTO.getNewHeadId());
              actNoUpdateMap.put("OLD_PROD_TYPE", objDataCorrectionLogTO.getOldProdType());
          }
        try {
            sqlMap.startTransaction();
            
            //Transaction table updations - Start
            
            if (transMod.equals("CASH")) {
                sqlMap.executeUpdate("updateCashTransActNoProductHeadDetailsAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransModeTypeAfterCorrection", actNoUpdateMap);
                if (objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)) {
                    actNoUpdateMap.put("ACT_NUM", null);
                }
                sqlMap.executeUpdate("updateCashTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransGLTransActNumAfterCorrection", actNoUpdateMap);

            } else if (transMod.equals("TRANSFER")) {
                sqlMap.executeUpdate("updateTransferTransActNoProductHeadDetailsAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransModeTypeAfterCorrection", actNoUpdateMap);
                if (objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)) {
                    actNoUpdateMap.put("ACT_NUM", null);
                }
                sqlMap.executeUpdate("updateTransferTransLinkBatchIdAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransGLTransActNumAfterCorrection", actNoUpdateMap);
            }
            
            //Transaction table updations - Ends
            
            //Transref GL - insertion in case of interbranch transactions - start
            
            if (objDataCorrectionLogTO.getInterBranchAcct().equals("Y")) {
                HashMap transRefGLUpdateMap = new HashMap();
                transRefGLUpdateMap.put("TRANS_DT", transDt);
                if (transMod.equals("CASH")) {
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",objDataCorrectionLogTO.getTransId());
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getTransId());
                } else if (transMod.equals("TRANSFER")) {
                    String batchTransId = objDataCorrectionLogTO.getBatchId()+"_"+objDataCorrectionLogTO.getTransId();
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",batchTransId);
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getBatchId());
                }                
                sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
            }
            
            //Transref GL - insertion in case of interbranch transactions - End
            
            //Master updation for old account numbers - start
            
            actNoUpdateMap.put("ACCOUNT_NO",objDataCorrectionLogTO.getOldAcctNum());
            HashMap headUpdateMap = new HashMap();
            headUpdateMap.put("AC_HD_ID",objDataCorrectionLogTO.getOldHeadId());
            if(objDataCorrectionLogTO.getTransType().equals("DEBIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()));
            }else if(objDataCorrectionLogTO.getTransType().equals("CREDIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()*-1));  
            }
           
            if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.OPERATIVE)){
              sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap); 
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.SUSPENSE)){
              sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);              
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.OTHERBANKACTS)){
              sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);             
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.GL)){
                headUpdateMap.put("BRANCH_CODE",objDataCorrectionLogTO.getBranchCode());
                checkGL(objDataCorrectionLogTO.getOldHeadId(),objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }
            
            //Master updation for old account numbers - end
            
            //GL balance updations of old account head in case of product change - start
            
            if(!objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.GL)){
                if(!objDataCorrectionLogTO.getOldHeadId().equals(objDataCorrectionLogTO.getNewHeadId())){                    
                    /////
                    String oldAcctBranchCode = "";
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", objDataCorrectionLogTO.getOldAcctNum());
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    }                    
                    ////
                    checkGL(objDataCorrectionLogTO.getOldHeadId(),oldAcctBranchCode,transType);
                    headUpdateMap.put("BRANCH_CODE",oldAcctBranchCode);
                    sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                }
            }
            
            //GL balance updations of old account head in case of product change - end
            
            //Master updation for new account numbers - start
            
            actNoUpdateMap.put("ACCOUNT_NO",objDataCorrectionLogTO.getNewAcctNum());
            headUpdateMap.put("AC_HD_ID",objDataCorrectionLogTO.getNewHeadId());
            if(objDataCorrectionLogTO.getTransType().equals("DEBIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount() * -1));
            }else if(objDataCorrectionLogTO.getTransType().equals("CREDIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()));  
            }
            if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.OPERATIVE)){
              sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);                
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.SUSPENSE)){
              sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);             
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.OTHERBANKACTS)){
              sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);               
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)){
              checkGL(objDataCorrectionLogTO.getNewHeadId(),objDataCorrectionLogTO.getBranchCode(),transType);
              headUpdateMap.put("BRANCH_CODE",objDataCorrectionLogTO.getBranchCode());
              sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }
            
             //Master updation for new account numbers - end
             
             
            //Daily Deposit trans tables
            HashMap depositTransUpdateMap = new HashMap();           
            depositTransUpdateMap.put("NEW_ACT_NO", objDataCorrectionLogTO.getNewAcctNum());
            depositTransUpdateMap.put("OLD_ACT_NO", objDataCorrectionLogTO.getOldAcctNum());
            depositTransUpdateMap.put("TRANS_DT", objDataCorrectionLogTO.getTransDt());
            if (transMod.equals("CASH")) {
                depositTransUpdateMap.put("BATCH_ID", objDataCorrectionLogTO.getTransId());               
            } else if (transMod.equals("TRANSFER")){
                depositTransUpdateMap.put("BATCH_ID", objDataCorrectionLogTO.getBatchId());              
            }
            sqlMap.executeUpdate("updateDailyDepositTransAfterCorrection", depositTransUpdateMap);
            //End
             
            
            //GL balance updations of new account head in case of product change - start
            
            if (!objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)) {
                if (!objDataCorrectionLogTO.getOldHeadId().equals(objDataCorrectionLogTO.getNewHeadId())) {
                    String newAcctBranchCode = "";
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", objDataCorrectionLogTO.getNewAcctNum());
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    }
                    checkGL(objDataCorrectionLogTO.getNewHeadId(), newAcctBranchCode, transType);
                    headUpdateMap.put("BRANCH_CODE", newAcctBranchCode);
                    sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                }
            }
            //GL balance updations of new account head in case of product change - end
            
            //Correction Log insertion - start
            
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            
            //Correction Log insertion - end
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    }
     
     private HashMap doGLToAcctNumberChange(HashMap dataMap) throws Exception{
        HashMap returnMap = new HashMap();
        HashMap actNoUpdateMap =  new HashMap();
        DataCorrectionLogTO objDataCorrectionLogTO = (DataCorrectionLogTO)(dataMap.get("DataCorrectionLogTO"));
        String transId = objDataCorrectionLogTO.getTransId();
        String transMod = objDataCorrectionLogTO.getTransMode();
        String transType = objDataCorrectionLogTO.getTransType();
        Date transDt = objDataCorrectionLogTO.getTransDt();
        actNoUpdateMap.put("ACT_NUM",objDataCorrectionLogTO.getNewAcctNum());
        actNoUpdateMap.put("OLD_ACT_NUM",null);
        actNoUpdateMap.put("TRANS_ID",transId);
        actNoUpdateMap.put("TRANS_DT",transDt);
        actNoUpdateMap.put("ACT_BRANCH_ID",objDataCorrectionLogTO.getAcctBranchId());       
        actNoUpdateMap.put("NEW_ACT_NUM", objDataCorrectionLogTO.getNewAcctNum());
        actNoUpdateMap.put("NEW_PROD_ID", objDataCorrectionLogTO.getNewProdId());
        actNoUpdateMap.put("NEW_PROD_TYPE", objDataCorrectionLogTO.getNewProdType());
        actNoUpdateMap.put("NEW_AC_HD_ID", objDataCorrectionLogTO.getNewHeadId());
        actNoUpdateMap.put("OLD_PROD_TYPE", objDataCorrectionLogTO.getOldProdType());

        try {
            sqlMap.startTransaction();
            
            //Transaction table updations - Start
            
            if (transMod.equals("CASH")) {
                actNoUpdateMap.put("OLD_ACT_NUM",null);
                sqlMap.executeUpdate("updateCashTransActNoProductHeadDetailsGLToActNoMapping", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransModeTypeAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransLinkBatchIdGLToActNoMapping", actNoUpdateMap);
                sqlMap.executeUpdate("updateCashTransGLTransActNumGLToActNoMapping", actNoUpdateMap);

            } else if (transMod.equals("TRANSFER")) {
                actNoUpdateMap.put("OLD_ACT_NUM",null);
                sqlMap.executeUpdate("updateTransferTransActNoProductHeadDetailsGLToActNoMapping", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransModeTypeAfterCorrection", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransLinkBatchIdGLToActNoMapping", actNoUpdateMap);
                sqlMap.executeUpdate("updateTransferTransGLTransActNumGLToActNoMapping", actNoUpdateMap);
            }
            
            //Transaction table updations - Ends
            
            //Transref GL - insertion in case of interbranch transactions - start
            
            if (objDataCorrectionLogTO.getInterBranchAcct().equals("Y")) {
                HashMap transRefGLUpdateMap = new HashMap();
                transRefGLUpdateMap.put("TRANS_DT", transDt);
                if (transMod.equals("CASH")) {
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",objDataCorrectionLogTO.getTransId());
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getTransId());
                } else if (transMod.equals("TRANSFER")) {
                    String batchTransId = objDataCorrectionLogTO.getBatchId()+"_"+objDataCorrectionLogTO.getTransId();
                    transRefGLUpdateMap.put("BATCH_TRANS_ID",batchTransId);
                    transRefGLUpdateMap.put("TRANS_ID", objDataCorrectionLogTO.getBatchId());
                }                
                sqlMap.executeUpdate("deleteTransRefGLEntriesAfterDataCorrection", transRefGLUpdateMap);
                sqlMap.executeUpdate("callAcctNoChangeTransRefGLUpdate", transRefGLUpdateMap);
            }
            
            //Transref GL - insertion in case of interbranch transactions - End
            
            //Master updation for old account numbers - start
            
            actNoUpdateMap.put("ACCOUNT_NO",objDataCorrectionLogTO.getOldAcctNum());
            HashMap headUpdateMap = new HashMap();
            headUpdateMap.put("AC_HD_ID",objDataCorrectionLogTO.getOldHeadId());
            if(objDataCorrectionLogTO.getTransType().equals("DEBIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()));
            }else if(objDataCorrectionLogTO.getTransType().equals("CREDIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()*-1));  
            }
           
            if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.OPERATIVE)){
              sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap); 
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.SUSPENSE)){
              sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);              
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.OTHERBANKACTS)){
              sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);             
            }else if(objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.GL)){
                headUpdateMap.put("BRANCH_CODE",objDataCorrectionLogTO.getBranchCode());
                checkGL(objDataCorrectionLogTO.getOldHeadId(),objDataCorrectionLogTO.getBranchCode(),transType);
                sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }
            
            //Master updation for old account numbers - end
            
            //GL balance updations of old account head in case of product change - start
            
            if(!objDataCorrectionLogTO.getOldProdType().equals(TransactionFactory.GL)){
                if(!objDataCorrectionLogTO.getOldHeadId().equals(objDataCorrectionLogTO.getNewHeadId())){                    
                    /////
                    String oldAcctBranchCode = "";
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", objDataCorrectionLogTO.getOldAcctNum());
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        oldAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    }                    
                    ////
                    checkGL(objDataCorrectionLogTO.getOldHeadId(),oldAcctBranchCode,transType);
                    headUpdateMap.put("BRANCH_CODE",oldAcctBranchCode);
                    sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                }
            }
            
            //GL balance updations of old account head in case of product change - end
            
            //Master updation for new account numbers - start
            
            actNoUpdateMap.put("ACCOUNT_NO",objDataCorrectionLogTO.getNewAcctNum());
            headUpdateMap.put("AC_HD_ID",objDataCorrectionLogTO.getNewHeadId());
            if(objDataCorrectionLogTO.getTransType().equals("DEBIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount() * -1));
            }else if(objDataCorrectionLogTO.getTransType().equals("CREDIT")){
              headUpdateMap.put("AMOUNT", (objDataCorrectionLogTO.getAmount()));  
            }
            if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.OPERATIVE)){
              sqlMap.executeUpdate("OperativeAccountMasterUpdation", actNoUpdateMap);                
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.SUSPENSE)){
              sqlMap.executeUpdate("SuspenseAccountMasterUpdation", actNoUpdateMap);             
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.OTHERBANKACTS)){
              sqlMap.executeUpdate("OtherBankAccountMasterUpdation", actNoUpdateMap);               
            }else if(objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)){
              checkGL(objDataCorrectionLogTO.getNewHeadId(),objDataCorrectionLogTO.getBranchCode(),transType);
              headUpdateMap.put("BRANCH_CODE",objDataCorrectionLogTO.getBranchCode());
              sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
            }
            
             //Master updation for new account numbers - end
            
            //GL balance updations of new account head in case of product change - start
            
            if (!objDataCorrectionLogTO.getNewProdType().equals(TransactionFactory.GL)) {
                if (!objDataCorrectionLogTO.getOldHeadId().equals(objDataCorrectionLogTO.getNewHeadId())) {
                    String newAcctBranchCode = "";
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", objDataCorrectionLogTO.getNewAcctNum());
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    }
                    checkGL(objDataCorrectionLogTO.getNewHeadId(), newAcctBranchCode, transType);
                    headUpdateMap.put("BRANCH_CODE", newAcctBranchCode);
                    sqlMap.executeUpdate("updateHeadCurBalanceAfterCorrection", headUpdateMap);
                }
            }
            //GL balance updations of new account head in case of product change - end
            
            //Correction Log insertion - start
            
            sqlMap.executeUpdate("insertDataCorrectionLogTO", objDataCorrectionLogTO);
            
            //Correction Log insertion - end
            sqlMap.commitTransaction();
            returnMap.put("STATUS","SUCCESS");
        } catch (SQLException ex) {
            Logger.getLogger(DataCorrectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return returnMap;
    } 
    
}
