/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermDepositDAO.java
 *
 * Created on Fri Jan 09 17:49:52 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.transferobject.deposit.TransferInTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
//import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.*;
import com.see.truetransact.serverside.common.powerofattorney.*;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
//import org.apache.log4j.Logger;
import com.see.truetransact.serverside.deposit.closing.DepositClosingDAO;
import java.util.Date;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.interestcalc.Rounding;

import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;

import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;

import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.deposit.*;
import java.sql.SQLException;
import java.util.*;
import javax.resource.spi.CommException;

/**
 * TermDeposit DAO.
 *
 */
public class TermDepositDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private AccInfoTO objTO;
    private TransferInTO transferInTO;
    private DepSubNoAccInfoTO depSubNoAccInfoTO;
    //    private LinkedHashMap mapAuthSignTO;
    private LinkedHashMap mapDepSubNoAccInfoTO;
    //    private LinkedHashMap mapPoATO;
    //    private HashMap closureMap ;
    private LinkedHashMap mapNomTO;
    private LinkedHashMap mapJointAccntTO;
    private JointAccntTO jointAccntTO;
    private AuthorizedSignatoryDAO objAuthorizedSignatoryDAO;
    private PowerOfAttorneyDAO objPowerOfAttorneyDAO;
    private String USERID = "";
    private LogTO objLogTO;
    private LogDAO objLogDAO;
    final String SCREEN = "TD";
    //    private final static Logger log = Logger.getLogger(TermDepositDAO.class);
    private CustomerHistoryTO objCustomerHistoryTO = null;
    private String returnDepositNo = "";
    private double intAmt = 0.0;
    private String withorOut = "";
    private Date currDt = null;
    //    private CashTransactionDAO cashTransaction = null;
    private DepositLienDAO depositLienDAO = null;
    private DepositLienTO depositLienTO = null;
    private InterestBatchTO interestBatchTO = null;
    private boolean sameNo = false;
    private boolean renewalProd = false;
    private boolean extensionProd = false;
    private TransactionDAO transactionDAO = null;
    HashMap depIntMap = new HashMap();
    HashMap extensionDepIntMap = new HashMap();
    private String DEPOSITNO = "DEPOSIT NO";
    private String remitProdId = "";
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
    private Date createdDt = null;
    private int ibrHierarchy = 0;
    private String generateSingleIdDepMultiRenewal=""; 
    private String interBranch = "";
    private ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO; // Added by nithya on 08-03.2016 for 0003920
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    // private Date currDt = null;

    //    private TransferDAO tranferDAO = null;
    /**
     * Creates a new instance of TermDepositDAO
     */
    public TermDepositDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getCashUniqueId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CASH_UNIQUE_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "CASH_UNIQUE_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public HashMap executeQuery(HashMap param) throws Exception {
        if (param.containsKey("TDS_CALCULATION")) {  // Added by nithya on 06-02-2020 for KD-1090          
            HashMap output = calcuateTDS(param, true);
            return output;
        } else {
            return getData(param);
        }
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("####### getData TermDepositDAO map :" + map);
        objAuthorizedSignatoryDAO = new AuthorizedSignatoryDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
        objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
        NomineeDAO objNomineeDAO = new NomineeDAO();

        HashMap returnMap = new HashMap();
        String where = (String) map.get(DEPOSITNO);
        HashMap jointMap = new HashMap();
        List list;
        if (map.containsKey("NEW_DEPOSIT_NO")) {
            jointMap.put("DEPOSIT NO", map.get("NEW_DEPOSIT_NO"));
            list = (List) sqlMap.executeQueryForList("getSelectJointAccntHolderTO", jointMap);
            returnMap.put("JointAcctDetails", list);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectJointAccntHolderTO", map);
            returnMap.put("JointAcctDetails", list);
        }
        list = (List) sqlMap.executeQueryForList("getAccInfoDetails", map);
        returnMap.put("AcctInfoDetails", list);

        HashMap nomineeMap = new HashMap();
        nomineeMap.put("DEPOSIT_NO", where);
        if (map.containsKey("VIEW_TYPE") && map.get("VIEW_TYPE").equals("CLOSED_DEPOSIT")) {
            list = (List) sqlMap.executeQueryForList("getSelectSameDepSubNoAccInfoTO", map);
            returnMap.put("DepSubNoAcctInfoDetails", list);
            nomineeMap.put("STATUS", "EXISTING");
            List lst = (List) sqlMap.executeQueryForList("getSelectRenewNomineeTOTD", nomineeMap);
            if (lst != null && lst.size() > 0) {
                returnMap.put("AccountNomineeList", lst);
            }
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", map);
            returnMap.put("DepSubNoAcctInfoDetails", list);
            if (map.containsKey("NEW_DEPOSIT_NO") && map.get("NEW_DEPOSIT_NO") != null) {
                nomineeMap.put("DEPOSIT_NO", map.get("NEW_DEPOSIT_NO"));
            }
            List lst = (List) sqlMap.executeQueryForList("getSelectRenewalNomineeTOTD", nomineeMap);
            if (lst != null && lst.size() > 0) {
                returnMap.put("AccountNomineeList", lst);
            }
        }
        nomineeMap = null;
        list = (List) sqlMap.executeQueryForList("getTransInDetails", map);
        returnMap.put("TransInDetails", list);

        list = (List) sqlMap.executeQueryForList("getNoOfAuthSign", map);
        returnMap.put("NoOfAuthSign", list);

        list = (List) sqlMap.executeQueryForList("getSelectNomTO", map);
        returnMap.put("NomDetails", list);

        map.put(CommonConstants.MAP_WHERE, where);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        list = transactionDAO.getData(map);
        returnMap.put("TransactionTO", list);

        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);
        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);
        //        returnMap.put("AccountNomineeList", (List)objNomineeDAO.getDataList(where, SCREEN));
        System.out.println("####### getData TermDepositDAO map End Part:" + map);
        
        // Added by nithya on 08-03.2016 for 0003920
        HashMap authNewMap = new HashMap();
        if(map.containsKey("DEPOSIT NO")){
          authNewMap.put("DEPOSIT_NO",map.get("DEPOSIT NO"));
        }
        if(map.containsKey("PRODUCT ID")){
          authNewMap.put("PRODUCT_ID",map.get("PRODUCT ID"));
        }else{
          authNewMap.put("PRODUCT_ID",map.get("Product Id"));  
        }
        if(authNewMap.size() > 0 && authNewMap != null){
          list = (List) sqlMap.executeQueryForList("getDepositAccountAdditionalAmount", authNewMap);
          returnMap.put("DEPO_ACCT_ADDITIONAL_AMT", list);
       }  
        // End
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", TransactionFactory.DEPOSITS);
        checkMap.put("PROD_ID", map.get("PRODUCT ID"));
        checkMap.put("ACT_NUM", map.get("DEPOSIT NO"));
        list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", checkMap);
        if (list != null && list.size() > 0) {
            returnMap.put("SMSSubscriptionTO", list);
        }
        list = null;
        objAuthorizedSignatoryDAO = null;
        objPowerOfAttorneyDAO = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;

    }

    private String getBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BATCH_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    private String getMultipleDepositId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MULTIPLE_DEPOSIT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getIssueId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ISSUE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getVariableNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REMITISSUE.VARIABLE_NO");
        return getPrefixSuffix((String) (dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private String getPrefixSuffix(String autoValue) throws Exception {
        final HashMap where = new HashMap();
        final StringBuffer pattern = new StringBuffer();
        where.put("PROD_ID", remitProdId);
        List prefixAndSuffix = (List) sqlMap.executeQueryForList("getPrefixAndSuffix", where);
        if (prefixAndSuffix.size() > 0) {
            String numberPattern = CommonUtil.convertObjToStr(((HashMap) prefixAndSuffix.get(0)).get("NUMBER_PATTERN"));
            String numberPatternSuffix = CommonUtil.convertObjToStr(((HashMap) prefixAndSuffix.get(0)).get("NUMBER_PATTERN_SUFFIX"));
            pattern.append(numberPattern);
            pattern.append(autoValue);
            pattern.append(numberPatternSuffix);
        }
        return CommonUtil.convertObjToStr(pattern);

    }

    private HashMap depositRenewalTransfer(HashMap depIntMap) throws Exception {
        System.out.println("##### DEP_INTEREST_AMT :" + depIntMap);
        if (depIntMap != null && !depIntMap.isEmpty()) {
            TxTransferTO transferTo = null;
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            double tdsAmount = 0.0; // Added by nithya on 06-02-2020 for KD-1090
            String tdsAcHd = "";
            double balPay = 0.0;
            double balPayable = 0.0;
            double balInt = 0.0;
            double balanceIntAmt = 0.0;
            double SBintAmt = 0.0;
            double interestTransAmt = 0.0;
            double depositTransAmt = 0.0;
            double depositRenewalAddAmt = 0.0;
            double balPayInt = 0.0;
            double remainAmt = 0.0;
            double prevInt = 0.0;
            String fdRenewSanmeNoPrincAmt = "";
            String generateSingleCashId = "";
            String olddepDt="";
            String oldMatDt="";
            String generateSingleTransId ="",generateSingleTransIdForIntrest ="";
            if(generateSingleIdDepMultiRenewal!=null && !generateSingleIdDepMultiRenewal.equals("")){
                generateSingleTransId=generateSingleIdDepMultiRenewal;
            }
            else{
                 generateSingleTransId = generateLinkID();
                 generateSingleTransIdForIntrest=generateLinkID();
            }
            //System.out.println("generateSingleTransId : " + generateSingleTransId);
            List princAmtList = sqlMap.executeQueryForList("getFdRenewSameNoTranForPrincAmt", depIntMap);
            if (princAmtList != null && princAmtList.size() > 0) {
                HashMap princAmtMap = (HashMap) princAmtList.get(0);
                fdRenewSanmeNoPrincAmt = CommonUtil.convertObjToStr(princAmtMap.get("FD_RENEW_SAMENO_TRAN_PRINC_AMT"));
                //System.out.println("fdRenewSanmeNoPrincAmt" + fdRenewSanmeNoPrincAmt);
            }
            if (depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE") != null && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                depIntMap.put("RENEWAL_INT_TRANS_PRODTYPE", "RM");
            }
            String oldProdId = CommonUtil.convertObjToStr(depIntMap.get("OLD_PROD_ID"));
            String newProdId = CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PRODID"));
            String depositSubNo = CommonUtil.convertObjToStr(depIntMap.get("DEPOSIT_NO"));
            String branchID = CommonUtil.convertObjToStr(depIntMap.get("BRANCH_CODE"));
            String initiatedBranchID = CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.INITIATED_BRANCH));
            depositTransAmt = CommonUtil.convertObjToDouble(depIntMap.get("WITDRAWING_DEP_AMT")).doubleValue();

            depositRenewalAddAmt = CommonUtil.convertObjToDouble(depIntMap.get("ADDING_DEP_AMT")).doubleValue();

            interestTransAmt = CommonUtil.convertObjToDouble(depIntMap.get("WITHDRAWING_INT_AMT")).doubleValue();
            balanceIntAmt = CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")).doubleValue();
            SBintAmt = CommonUtil.convertObjToDouble(depIntMap.get("SB_INT_AMT")).doubleValue();
            prevInt = CommonUtil.convertObjToDouble(depIntMap.get("PREV_INT_AMT")).doubleValue();
            if(depIntMap.containsKey("TDS_AMOUNT") && depIntMap.get("TDS_AMOUNT") != null && CommonUtil.convertObjToDouble(depIntMap.get("TDS_AMOUNT")) > 0){ // Added by nithya on 06-02-2020 for KD-1090
                tdsAmount = CommonUtil.convertObjToDouble(depIntMap.get("TDS_AMOUNT"));
                tdsAcHd = CommonUtil.convertObjToStr(depIntMap.get("TDS_AC_HD"));
            }
           //added by chithra on 1-07-14
            if(depIntMap.containsKey("OLD_DEPOSIT_DATE")){
                olddepDt=CommonUtil.convertObjToStr(depIntMap.get("OLD_DEPOSIT_DATE"));
            }
            if(depIntMap.containsKey("OLD_MATURITY_DATE")){
                oldMatDt=CommonUtil.convertObjToStr(depIntMap.get("OLD_MATURITY_DATE"));
            }   
            //end...
            if ((depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE").equals("CASH") && depositRenewalAddAmt > 0) || (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")
                    && depositTransAmt > 0) || (depIntMap.containsKey("RENEWAL_INT_TRANS_MODE") && depIntMap.get("RENEWAL_INT_TRANS_MODE").equals("CASH") && interestTransAmt > 0)) {
                if(generateSingleIdDepMultiRenewal!=null && !generateSingleIdDepMultiRenewal.equals("")){
                     generateSingleCashId=generateSingleIdDepMultiRenewal;
                 }
                else{
                    generateSingleCashId = generateLinkID();
                }
            }
            if (depositSubNo.lastIndexOf("_") != -1) {
                depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
            }
            HashMap balanceMap = new HashMap();
            balanceMap.put("DEPOSIT_NO", depositSubNo);
            List lst = sqlMap.executeQueryForList("getTotalIntBalanceForDeposit", balanceMap);
            if (lst != null && lst.size() > 0) {
                balanceMap = (HashMap) lst.get(0);
                //System.out.println("##### balanceMap :" + balanceMap);
                HashMap oldbehavesMap = new HashMap();
                oldbehavesMap.put("PROD_ID", depIntMap.get("OLD_PROD_ID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
                if (lst != null && lst.size() > 0) {
                    oldbehavesMap = (HashMap) lst.get(0);
                }
                String oldBehaves = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE"));
                oldbehavesMap = null;
                HashMap newbehavesMap = new HashMap();
                newbehavesMap.put("PROD_ID", depIntMap.get("RENEWAL_PRODID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", newbehavesMap);
                if (lst != null && lst.size() > 0) {
                    newbehavesMap = (HashMap) lst.get(0);
                }
                String newBehaves = CommonUtil.convertObjToStr(newbehavesMap.get("BEHAVES_LIKE"));
                newbehavesMap = null;
                if (!depositSubNo.equals(returnDepositNo)) {
                    //System.out.println("New No generating :" + returnDepositNo);
                    double totIntAmt = CommonUtil.convertObjToDouble(balanceMap.get("TOT_INT_AMT")).doubleValue();
                    double totIntCredit = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_CREDIT")).doubleValue();
                    double totIntDrawn = CommonUtil.convertObjToDouble(balanceMap.get("TOTAL_INT_DRAWN")).doubleValue();
                    balInt = totIntAmt - totIntCredit;
                    if (balInt > 0) {
                        balPayInt = balInt + SBintAmt;
                    } else {
                        balPayInt = SBintAmt;
                    }
                    //added by Rishad Mp   13//11/2019
                    if (prevInt > 0) {
                        balPayInt = balPayInt + prevInt;
                    }
                    balPayable = totIntCredit - totIntDrawn;
                    System.out.println("New No generating :" + "depositTransAmt :" + depositTransAmt + "interestTransAmt :" + interestTransAmt
                            + "balanceIntAmt :" + balanceIntAmt + "SBintAmt :" + SBintAmt + "totIntAmt :" + totIntAmt + "totIntCredit :" + totIntCredit
                            + "totIntDrawn :" + totIntDrawn + "Starting balInt :" + balInt + "balPayable :" + balPayable + "Final balPayInt :" + balPayInt
                            + "balPayable :" + balPayable + "oldbehave:" + oldBehaves + "newbehave:" + newBehaves + "newProd:" + newProdId + "OldProd:" + oldProdId);
                } else {
                    System.out.println("Old No generating :" + depositSubNo);
                    HashMap samenoMap = new HashMap();
                    samenoMap.put("DEPOSIT_NO", depositSubNo);
                    lst = sqlMap.executeQueryForList("getTotalIntBalanceForDepositSameNo", samenoMap);
                    if (lst != null && lst.size() > 0) {
                        samenoMap = (HashMap) lst.get(0);
                        double totIntAmt = CommonUtil.convertObjToDouble(samenoMap.get("TOT_INT_AMT")).doubleValue();
                        double totIntCredit = CommonUtil.convertObjToDouble(samenoMap.get("TOTAL_INT_CREDIT")).doubleValue();
                        double totIntDrawn = CommonUtil.convertObjToDouble(samenoMap.get("TOTAL_INT_DRAWN")).doubleValue();
                        balInt = totIntAmt - totIntCredit;
                        if (balInt > 0) {
                            balPayInt = balInt + SBintAmt;
                        } else {
                            balPayInt = SBintAmt;
                        }
                        //added by Rishad Mp   13//11/2019
                        if (prevInt > 0) {
                            balPayInt = balPayInt + prevInt;
                        }
                        balPayable = totIntCredit - totIntDrawn;
                        System.out.println("Old No generating :" + "depositTransAmt :" + depositTransAmt + "interestTransAmt :" + interestTransAmt + "balanceIntAmt :" + balanceIntAmt
                                + "SBintAmt :" + SBintAmt + "totIntAmt :" + totIntAmt + "totIntCredit :" + totIntCredit + "totIntDrawn :" + totIntDrawn + "Starting balInt :" + balInt
                                + "balPayable :" + balPayable + "Final balPayInt :" + balPayInt + "balPayable : " + balPayable);
                        System.out.println("oldBehaves :" + oldBehaves + "newBehaves :" + newBehaves + "new ProdId :" + newProdId + "Old ProdId :" + oldProdId);
                    }
                    //Added By Suresh
                    if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
                        balInt = balanceIntAmt;
                        if (balInt > 0) {
                            balPayInt = balInt + SBintAmt;
                        } else {
                            balPayInt = SBintAmt;
                        }
                    }
                     //added by Rishad Mp   13//11/2019
                        if (prevInt > 0) {
                            balPayInt = balPayInt + prevInt;
                        }
                }
                //modifing from here by rishad 06/04/2014
                HashMap txMap = new HashMap();
                ArrayList transferList = new ArrayList();
                transactionDAO.setLinkBatchID(depositSubNo + "_1");
                transactionDAO.setInitiatedBranch(initiatedBranchID);
                HashMap oldacctHeadMap = new HashMap();
                oldacctHeadMap.put("PROD_ID", depIntMap.get("OLD_PROD_ID"));
                lst = sqlMap.executeQueryForList("getDepositClosingHeads", oldacctHeadMap);
                if (lst != null && lst.size() > 0) {
                    oldacctHeadMap = (HashMap) lst.get(0);
                }
                HashMap newacctHeadMap = new HashMap();
                newacctHeadMap.put("PROD_ID", depIntMap.get("RENEWAL_PRODID"));
                lst = sqlMap.executeQueryForList("getDepositClosingHeads", newacctHeadMap);
                if (lst != null && lst.size() > 0) {
                    newacctHeadMap = (HashMap) lst.get(0);
                }
                //added by rishad 
                transferList = new ArrayList();
                if ((balPayable + balPayInt < 0) || (balPayable + balPayInt > 0)) {
                    //System.out.println("a1111111111111111111111");
                    if (balanceIntAmt < 0 || balInt < 0) {
                        if (balanceIntAmt < 0) {
                            txMap = new HashMap();
                            //commeneted by rishad
                           // transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                              
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                               
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            System.out.println("INT HEAD11 ===" + (String) oldacctHeadMap.get("INT_PAY"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, balanceIntAmt * -1);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, balanceIntAmt * -1);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                        }
                        //Balance Interest Transation
                        if (balInt < 0) {                            
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PROV_ACHD"));
                            }
                            System.out.println("INT HEAD22 ===" + (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, balInt * -1);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, balInt * -1);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                        }
                        balanceIntAmt = CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")).doubleValue();
                        System.out.println("balanceIntAmtbalanceIntAmt" + balanceIntAmt);
                    }
                    System.out.println("balanceIntAmt=====" + balanceIntAmt + " balPayInt ==== " + balPayInt);


                    System.out.println("oldBehaves: " + oldBehaves + " balPayInt====" + balPayInt + " balanceIntAmt" + balanceIntAmt);
                    // if (oldBehaves.equals("CUMMULATIVE") && balanceIntAmt > 0) { 
                    if (oldBehaves.equals("CUMMULATIVE") && (balPayInt > 0 || balanceIntAmt > 0)) { //jiv
                        System.out.println("CUMMULATIVE transaction payable debit credit to accts : " + balPayInt);
                        HashMap txMap1 = new HashMap();
                        String isSplitAll = "";
                        txMap1.put("NEW_PROD_ID", newProdId);
                        List listData = ServerUtil.executeQuery("getDepositProdRenewalVal", txMap1);
                        for (int j = 0; j < listData.size(); j++) {
                            HashMap mapT = (HashMap) listData.get(j);
                            // isSplitAll=mapT.get("PROD_DESC").toString();
                            if (mapT.get("IS_SPLIT_INTEREST") != null) {
                                isSplitAll = mapT.get("IS_SPLIT_INTEREST").toString();
                            }
                        }

                        //transferTo = transactionDAO.addTransferCreditLocal(txMap, balanceIntAmt); 
                        double tot_amtcredit = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT"));
                        //added by rishad 20/05/2014
                        if(depIntMap.containsKey("ADDING_DEP_AMT")&&depIntMap.get("ADDING_DEP_AMT")!=null)
                        {
                            System.out.println("enterd here inside deposing adding amount"+depIntMap.get("ADDING_DEP_AMT"));
                         tot_amtcredit=tot_amtcredit-CommonUtil.convertObjToDouble(depIntMap.get("ADDING_DEP_AMT"));
                        }
                        if (isSplitAll != null && isSplitAll.equals("Y")) {
                            //babu


                            //1/Int Dep Borrow     13500 DEBIT
                            HashMap txMap2 = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();

                            txMap2.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                            txMap2.put("generateSingleTransId", generateSingleTransId);
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap2.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap2.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap2.put(TransferTrans.CR_BRANCH, branchID);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap2, balanceIntAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            //   transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //    transactionDAO.doTransferLocal(transferList, branchID);

                            //


                            //
                            //1/Int Dep Borrow     13500 CREDIT
                            txMap2 = new HashMap();
                            //  transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.CR_ACT_NUM, "");
                            txMap2.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap2.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.CR_BRANCH, branchID);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap2, balanceIntAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);

                            txMap2 = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            System.out.println("NN=-===" + (String) newacctHeadMap.get("INT_PAY"));
                            System.out.println("OOO=-===" + (String) oldacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap2.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap2.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.CR_BRANCH, branchID);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            //txMap2.put("generateSingleTransId",generateSingleTransId);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap2, balanceIntAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            ///  transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //   transactionDAO.doTransferLocal(transferList, branchID);

                            //SVD A/c Head      13500 CREDIT
                            txMap2 = new HashMap();
                            //  transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            txMap2.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap2.put(TransferTrans.CR_ACT_NUM, depIntMap.get("DEPOSIT_NO") + "_1");//returnDepositNo + "_1");
                            txMap2.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap2.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap2.put(TransferTrans.CR_BRANCH, branchID);
                            txMap2.put("generateSingleTransId", generateSingleTransId);
                            //txMap2.put("generateSingleTransId",generateSingleTransId);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap2, balanceIntAmt);  //jiv
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                            System.out.println("AMMTTTT balPayInt===" + balanceIntAmt);//jiv
                            System.out.println("txMap2=====" + txMap2);
                            //FD A/c            27000 CREDIT 
                            txMap2 = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap2.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap2.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap2.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap2.put(TransferTrans.CR_BRANCH, branchID);
                            txMap2.put("generateSingleTransId", generateSingleTransId);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            //txMap2.put("generateSingleTransId",generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap2, tot_amtcredit);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            //transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //  transactionDAO.doTransferLocal(transferList, branchID);
//                            System.out.println("AMMTTTT ===" + (tot_amtcredit - balPayInt));//jiv
//                            System.out.println("txMap21111=====" + txMap2);//jiv


                            txMap2 = new HashMap();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                 
                            } else {
                                txMap2.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        
                            }
                            txMap2.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap2.put(TransferTrans.DR_BRANCH, branchID);
                            txMap2.put(TransferTrans.PARTICULARS, "Amount :" + depositSubNo + "_1");
                            txMap2.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap2.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap2.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                            txMap2.put(TransferTrans.DR_ACT_NUM, depIntMap.get("DEPOSIT_NO") + "_1");
                            txMap2.put("generateSingleTransId", generateSingleTransId);
                            txMap2.put("TRANS_MOD_TYPE", "TD");
                            txMap2.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap2.put("INITIATED_BRANCH", initiatedBranchID);
                            //txMap2.put("generateSingleTransId",generateSingleTransId);
                            double tot_amt = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT"));
                            transferTo = transactionDAO.addTransferDebitLocal(txMap2, tot_amt);  //jiv
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);

                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                        } else {
                            if (balPayInt > 0) {                                
                               /* txMap = new HashMap();
                                transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PROV_ACHD"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                transferTo.setSingleTransId(generateSingleTransId);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                                }
                                System.out.println("INT HEAD33 ===" + (String) oldacctHeadMap.get("INT_PAY") + " NEW ===" + (String) newacctHeadMap.get("INT_PAY"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);

                                transferTo = transactionDAO.addTransferDebitLocal(txMap, balPayInt + prevInt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, balPayInt + prevInt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, branchID);*/
                            }
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                         
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            transferTo.setSingleTransId(generateSingleTransId);
                            System.out.println("oldBehaves ===" + oldBehaves + " newBehaves===" + newBehaves + " oldProdId==" + oldProdId + " newProdId===" + newProdId);
                            //if (oldBehaves.equals(newBehaves) ) {//&& oldProdId.equals(newProdId)
                            txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            //} else {
                            //    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            // }
                            System.out.println("INT HEAD44 ===" + (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD") + " OLD==" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")
                                    && oldBehaves.equals(newBehaves) && oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("CUMMULATIVE")) {
                                if (interestTransAmt != balPayInt) {
                                    double amt3 = balPayInt - interestTransAmt;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, amt3);  //jiv
                                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName("Deposit Account Renewal");
                                    transferList.add(transferTo);
                                }
                            } 
                            //added by rishad 21/05/2014 cc to fd
                           else  if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")
                                    && !oldBehaves.equals(newBehaves) && oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("FIXED")) {
                                if (interestTransAmt != balPayInt) {
                                    double amt3 = balPayInt - interestTransAmt;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, amt3);  //jiv
                                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName("Deposit Account Renewal");
                                    transferList.add(transferTo);
                                }
                            } 
                           //end
                            else {
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                //transferTo = transactionDAO.addTransferDebitLocal(txMap, balanceIntAmt);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, balPayInt);  //jiv
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                            }                            
                             
                            txMap.put(TransferTrans.PARTICULARS, "Amount :" + depositSubNo + "_1");
                            //edited Nidhin
                            if(oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("FIXED")){
                                System.out.println("This is executed");
                                txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            }

                            System.out.println("tot_amtcredit ====" + tot_amtcredit);
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")
                                    && oldBehaves.equals(newBehaves) && oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("CUMMULATIVE")) {
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, tot_amtcredit);  //jiv
                            } else {
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, tot_amtcredit);  //jiv
                            }
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);

                            txMap = new HashMap();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                               
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                               
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, "Amount :" + depositSubNo + "_1");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            //commented by chithra on 12-05-14- debit amount account number should be old number
//                            if (isSplitAll != null && isSplitAll.equals("Y")) {
//                                txMap.put(TransferTrans.DR_ACT_NUM, depIntMap.get("DEPOSIT_NO") + "_1");
//                            } else {
//                                txMap.put(TransferTrans.DR_ACT_NUM, returnDepositNo + "_1");
//                            }
//                            //edited by Nidhin  4-02-2014
//                            if (oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("FIXED")) {
//                                System.out.println("This is executeds");
//                                txMap.put(TransferTrans.DR_ACT_NUM, depIntMap.get("DEPOSIT_NO") + "_1");
//                            }
                             txMap.put(TransferTrans.DR_ACT_NUM, depIntMap.get("DEPOSIT_NO") + "_1");//added by Chithra on 12-05-14
                            transferTo.setSingleTransId(generateSingleTransId);
                            double tot_amt = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT"));
                            //added by rishad 20/05/2014
                            if(depIntMap.containsKey("ADDING_DEP_AMT")&&depIntMap.get("ADDING_DEP_AMT")!=null)
                        {
                         tot_amt=tot_amt-CommonUtil.convertObjToDouble(depIntMap.get("ADDING_DEP_AMT"));
                        }
                            System.out.println("tot_amt=====" + tot_amt + "balPayInt ==== " + balPayInt);
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")
                                    && oldBehaves.equals(newBehaves) && oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("CUMMULATIVE")) {
                                if (interestTransAmt != balPayInt) {
                                    double amt2 = balPayInt - interestTransAmt;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_amt - amt2 + tdsAmount);  // tdsAmount Added by nithya on 06-02-2020 for KD-1090
                                } else {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_amt + tdsAmount);  // tdsAmount Added by nithya on 06-02-2020 for KD-1090
                                }
                            }
                            //added by rishad 21/05/2014
                           else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")
                                    && !oldBehaves.equals(newBehaves) && oldBehaves.equals("CUMMULATIVE") && newBehaves.equals("FIXED")) {
                                if (interestTransAmt != balPayInt) {
                                    double amt2 = balPayInt - interestTransAmt;
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_amt - amt2 + tdsAmount);  // tdsAmount Added by nithya on 06-02-2020 for KD-1090
                                } else {
                                    transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_amt + tdsAmount);  // tdsAmount Added by nithya on 06-02-2020 for KD-1090
                                }
                            }
                            //end
                            else {
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, tot_amt - balPayInt + tdsAmount);  //tdsAmount Added by nithya on 06-02-2020 for KD-1090
                            }
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            
                            // TDS credit
                            if (tdsAmount > 0) { // Added by nithya on 06-02-2020 for KD-1090
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_AC_HD, tdsAcHd);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                //txMap2.put("generateSingleTransId",generateSingleTransId);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmount);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                            }
                            // End -- TDS credit
                            

                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                        }
                    }
                    if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                        System.out.println("INT NO transaction payable debit credit to accts :");
                        if (balInt < 0 && oldBehaves.equals("FIXED")) {
                            balPayInt = balPayInt + balPayable + balInt;
                        } else {
                            if (oldBehaves.equals("CUMMULATIVE") && SBintAmt == 0) {
                                balPayInt = 0.0;
                            } else if (oldBehaves.equals("CUMMULATIVE") && SBintAmt > 0) {
                                balPayInt = SBintAmt;
                            } else {
                                balPayInt = balPayInt + balPayable;
                            }
                        }
                        if (balPayInt > 0) {
                            txMap = new HashMap();
                           // transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                            }
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                          //  txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                            txMap.put(TransferTrans.PARTICULARS, "Interest"+ /*depositSubNo +*/ " : From "+ olddepDt+" To "+ oldMatDt);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            transferTo.setSingleTransId(generateSingleTransId);
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            } else {
                                txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            }
                            System.out.println("INT HEAD55 ===" + (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            if (depIntMap.containsKey("RENEWAL_INT_PAYABLE") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("Y")) {
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, balanceIntAmt + prevInt);//balPayInt
                            }

                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                          	transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                            if (transferTo.getAmount() != null && transferTo.getAmount() > 0) {
                                transferList.add(transferTo);
                            }
                            txMap.put(TransferTrans.PARTICULARS, "Interest on:" + depositSubNo + "_1 W.e.f "+CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT")));
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            if (depIntMap.containsKey("ORIGINAL_DEP_AMT") && depIntMap.get("ORIGINAL_DEP_AMT") != null && !depositSubNo.equals(returnDepositNo)) {
                                double orgAmt = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                                System.out.println("$$$$$$orgAmt" + orgAmt + balPayInt);
                                System.out.println("prevInt " + prevInt + "depositTransAmt =" + depositTransAmt);
                                System.out.println("fffff =" + depIntMap.get("RENEWAL_INT_PAYABLE"));
                                if (depIntMap.containsKey("RENEWAL_INT_PAYABLE") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("Y")) {
                                    balanceIntAmt = CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")).doubleValue();
                                    double x = (balanceIntAmt + orgAmt + prevInt) - depositTransAmt - tdsAmount;//balPayInt // tdsAmount // Added by nithya on 06-02-2020 for KD-1090
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, x);
                                }
                                if (depIntMap.containsKey("RENEWAL_INT_PAYABLE") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("N")) {
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, orgAmt - depositTransAmt - tdsAmount); // tdsAmount // Added by nithya on 06-02-2020 for KD-1090
                                }
                            } ///////////////////////changed by jiby
                            //                            else if (depositSubNo.equals(returnDepositNo) && !fdRenewSanmeNoPrincAmt.equals("N")) {
                            //                               double orgAmt1 = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                            //                               System.out.println("orgAmt1orgAmt1orgAmt1orgAmt1" + orgAmt1);
                            //                                transferTo = transactionDAO.addTransferCreditLocal(txMap, balPayInt + orgAmt1);
                            //                            } 
                            else if (depositSubNo.equals(returnDepositNo)) {                                
                                //edited by Nidhin
                                if (depIntMap.containsKey("RENEWAL_INT_PAYABLE") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("Y")) {
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    //commeneted by rishad 
                                 // transferTo = transactionDAO.addTransferCreditLocal(txMap, balPayInt);//+prevInt 22-Dec-2013
                                }//jiv

                            }
                            ////////////////////////////////////////////////////////
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            if (transferTo.getAmount() != null && transferTo.getAmount() > 0) {
                                transferList.add(transferTo);
                            }                            
                            if (depIntMap.containsKey("ORIGINAL_DEP_AMT") && depIntMap.get("ORIGINAL_DEP_AMT") != null && !depositSubNo.equals(returnDepositNo)) {
                                System.out.println("NEW DEP YES TRANSFER transaction payable debit credit to accts : " + depIntMap.get("ORIGINAL_DEP_AMT"));
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                double originalMatAmt = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                                double renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                                if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                                    if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                                        if (balanceIntAmt < 0 && oldBehaves.equals("FIXED") && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                            originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                            System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                        } else {
                                            originalMatAmt = originalMatAmt - depositTransAmt;
                                        }
                                    } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                        if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                            originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                            System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                        } else {
                                            originalMatAmt = originalMatAmt - depositTransAmt;
                                            System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                        }
                                    }
                                } else if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("NO")) {
                                    renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                                    if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {
                                        if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                            originalMatAmt = originalMatAmt + balanceIntAmt;
                                            System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                        }
                                        System.out.println("RENEWAL_DEP_ADDING YES balanceIntAmt>0 : " + originalMatAmt);
                                    } else if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("NO")) {
                                        if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                            if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                                originalMatAmt = originalMatAmt + balanceIntAmt;
                                                System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                            }
                                        }
                                    }
                                }
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                                        txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                    } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                                        txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                                    }
                                    //                        txMap.put(TransferTrans.DR_AC_HD,(String)newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                } else {
                                    if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                                        txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                    } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                                        txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                                    }
                                }
                                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                                txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.PARTICULARS, returnDepositNo + "_1");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put("TRANS_MOD_TYPE", "TD");    
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo.setSingleTransId(generateSingleTransId);
                                System.out.println("NEW DEP YES TRANSFER : " + txMap+"originalMatAmt :"+originalMatAmt);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, originalMatAmt); 
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                if (transferTo.getAmount() != null && transferTo.getAmount() > 0) {
                                    transferList.add(transferTo);
                                }


                            }
                            // TDS credit
                            if (tdsAmount > 0) { // Added by nithya on 06-02-2020 for KD-1090
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.CR_AC_HD, tdsAcHd);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("generateSingleTransId", generateSingleTransId);
                                //txMap2.put("generateSingleTransId",generateSingleTransId);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmount);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                            }
                            // End -- TDS credit
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //modified by rishad 24-05-2014
                            if (transferList != null && transferList.size() > 0&&!depositSubNo.equals(returnDepositNo)) {                                
                                transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                            }
                        }
                    } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                        if (depIntMap.containsKey("RENEWAL_INT_TRANS_MODE") && depIntMap.get("RENEWAL_INT_TRANS_MODE").equals("CASH")
                                && interestTransAmt > 0) {
                            CashTransactionTO objCashTO = new CashTransactionTO();
                            objCashTO.setTransId("");
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("INT_PAY")));
                            objCashTO.setProdType("GL");
                            objCashTO.setInpAmount(new Double(interestTransAmt));
                            objCashTO.setAmount(new Double(interestTransAmt));
                            objCashTO.setTransType(CommonConstants.DEBIT);
                            objCashTO.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            objCashTO.setBranchId(CommonUtil.convertObjToStr(branchID)); 
                            objCashTO.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            objCashTO.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            objCashTO.setStatusDt((Date)currDt.clone());
                            objCashTO.setTransDt((Date)currDt.clone());
                            //List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                            //if (listData != null && listData.size() > 0) {
                            //    HashMap map1 = (HashMap) listData.get(0);
                            //    if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                           //         objCashTO.setAuthorizeStatus_2("");
                           //     }else{
                            //        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");  
                            //    }
                           // }else{
                             //   objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");        
                            //}
                            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");  
                            objCashTO.setTokenNo(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TOKEN_NO")));
                            //objCashTO.setParticulars("To Pending Interest :" + depositSubNo + "_1");
                            objCashTO.setParticulars("To Interest  "+/* depositSubNo +*/ " From "+ olddepDt +" To "+ oldMatDt);
                            objCashTO.setInitiatedBranch(initiatedBranchID);
                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                            objCashTO.setLinkBatchId(depositSubNo + "_1");
                            objCashTO.setTransModType("TD");
                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                            //akhil@
                            if (objTO.getOpeningMode().equals("Normal")) {
                                objCashTO.setScreenName(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.SCREEN)));
                            } else {
                                objCashTO.setScreenName("Deposit Account Renewal");
                            }
                            ArrayList cashList = new ArrayList();
                            cashList.add(objCashTO);
                           
                            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                            HashMap interestTransMap = new HashMap();
                            interestTransMap.put("RENEWAL_DEPOSIT_SCREEN", "RENEWAL_DEPOSIT_SCREEN");
                            interestTransMap.put("RENEWAL_LIST", cashList);
                            interestTransMap.put("BRANCH_CODE", initiatedBranchID);
                            interestTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                            cashTransactionDAO.execute(interestTransMap, false);                            
                            if (oldBehaves.equals("CUMMULATIVE")) {
                                if (balanceIntAmt > 0) {
                                    remainAmt = balPayable + balPayInt - balanceIntAmt - interestTransAmt;
                                } else {
                                    remainAmt = balPayable + balPayInt - interestTransAmt;
                                }
                            } else if (!oldBehaves.equals("CUMMULATIVE") && balPayInt + balPayable - interestTransAmt > 0) {
                                if (interestTransAmt != balanceIntAmt) {

                                    if (balInt < 0) {
                                        remainAmt = balPayInt + balPayable + balInt - interestTransAmt;
                                    } else {
                                        remainAmt = balPayInt + balPayable - interestTransAmt;
                                    }
                                } else {
                                    //added by Rishad For 13/11/2019 for previous interest Transaction   (only for previous interest)
                                    if (oldBehaves.equals("FIXED") && prevInt > 0) {
                                        if (balInt < 0) {
                                            remainAmt = balPayInt + balPayable + balInt - interestTransAmt;
                                        } else {
                                            remainAmt = balPayInt + balPayable - interestTransAmt;
                                        }
                                    }
                                }
                            }
                                                        
                            if (remainAmt > 0) {
                                txMap = new HashMap();
                                //commeneted by rishad 20/05/2014
                              //  transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                //txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                txMap.put(TransferTrans.PARTICULARS," Interest  "+/* depositSubNo +*/ " From "+ olddepDt +" To "+ oldMatDt);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                }
                                System.out.println("INT HEAD66 ===" + (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                                txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);

                                transferTo.setSingleTransId(generateSingleTransId);
                                System.out.println("****** INT YES CASH : 22 22 22 " + txMap + "Paying remainAmt>0 :" + remainAmt);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, remainAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                //commeneted by rishad 20/05/2014
                                //transferTo = transactionDAO.addTransferCreditLocal(txMap, remainAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                                //commented by rishad 20/052014
//                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                                transactionDAO.doTransferLocal(transferList, branchID);
                                
                                 //tds credit to TDS head -- --- Code change 4
                                if (tdsAmount > 0) { // Added by nithya on 06-02-2020 for KD-1090
                                    txMap = new HashMap();
                                    transferTo = new TxTransferTO();
                                    txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_AC_HD, tdsAcHd);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    //txMap2.put("generateSingleTransId",generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmount);
                                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName("Deposit Account Renewal");
                                    transferList.add(transferTo);
                                }
                                // End
                                
                            }
                        } else if (depIntMap.containsKey("RENEWAL_INT_TRANS_MODE") && depIntMap.get("RENEWAL_INT_TRANS_MODE").equals("TRANSFER")
                                && interestTransAmt > 0) {
                            System.out.println("INT YES TRANSFER transaction payable debit credit to accts : " + balPayInt);
                            if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && !depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                                HashMap operativeIntMap = new HashMap();
                                if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && !depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("")
                                        && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("Y")) {
                                    operativeIntMap.put("ACT_NUM", depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    lst = sqlMap.executeQueryForList("getAccNoProdIdDet", operativeIntMap);
                                    if (lst != null && lst.size() > 0) {
                                        operativeIntMap = (HashMap) lst.get(0);
                                    }
                                }
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                if (!(branchID.equalsIgnoreCase(_branchCode))) {// Added by nithya on 21-03-2019
                                    txMap.put("TERM_DEPOSIT_INTER_BRANCH_TRANS", "TERM_DEPOSIT_INTER_BRANCH_TRANS");
                                    txMap.put("INITIATED_BRANCH", _branchCode);
                                }
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.PARTICULARS, "Interest  "/*+ depositSubNo*/ + "  From "+ olddepDt +" To "+ oldMatDt);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
								//added by sathya or shihad
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestTransAmt + tdsAmount);

                                transferTo.setSingleTransId(generateSingleTransId);
                                if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE") != null
                                        && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("OA")) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) operativeIntMap.get("AC_HD_ID"));
                                    System.out.println("INT HEAD77 ===" + (String) operativeIntMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_INT_TRANS_PRODID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                     txMap.put("TRANS_MOD_TYPE", "OA");
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                } else if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE") != null
                                        && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    System.out.println("INT HEAD88 ===" + depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                     txMap.put("TRANS_MOD_TYPE", "GL");
                                } else if (depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("Y") && depIntMap.containsKey("RENEWAL_DEP_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                    } else {
                                        txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                    }
                                    System.out.println("INT HEAD99 ===" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo + "_1");
                                    txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                }else if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE") != null
                                        && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("AB")) {
                                    HashMap otherBankMap = new HashMap();
                                    otherBankMap.put("ACT_MASTER_ID", depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    lst = sqlMap.executeQueryForList("getOtherAccNoHeaddDet", otherBankMap);
                                    if (lst != null && lst.size() > 0) {
                                        otherBankMap = (HashMap) lst.get(0);
                                        txMap.put(TransferTrans.CR_AC_HD, (String) otherBankMap.get("PRINCIPAL_AC_HD"));
                                        System.out.println("getOtherAccNoHeaddDet ===" + (String) otherBankMap.get("PRINCIPAL_AC_HD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_INT_TRANS_PRODID"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                        txMap.put("TRANS_MOD_TYPE", "AB");
                                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    }
                                }
                                else if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE") != null
                                        && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("SA")) {                                    
                                    HashMap suspenseBankMap = new HashMap();
                                    suspenseBankMap.put("prodId", depIntMap.get("RENEWAL_INT_TRANS_PRODID"));
                                    lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", suspenseBankMap);
                                    txMap.put(TransferTrans.CR_AC_HD, (String) operativeIntMap.get("AC_HD_ID"));
                                    System.out.println("INT HEAD77 ===" + (String) operativeIntMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_INT_TRANS_PRODID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                     txMap.put("TRANS_MOD_TYPE", "SA");
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                }
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                if (generateSingleIdDepMultiRenewal != null && !generateSingleIdDepMultiRenewal.equals("")) {
                                    transferTo.setSingleTransId(generateSingleTransId);
                                } else {
                                    transferTo.setSingleTransId(generateSingleTransIdForIntrest/*generateSingleTransId*/);
                                }
                                transferTo.setInstrumentNo1("INTEREST");
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                                txMap.put(TransferTrans.PARTICULARS, "Interest on: " + depositSubNo + "_1");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, interestTransAmt); // Here the credit amount is interestTransAmt - tds amount - code change 3
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                if (generateSingleIdDepMultiRenewal != null && !generateSingleIdDepMultiRenewal.equals("")) {
                                    transferTo.setSingleTransId(generateSingleTransId);
                                } else {
                                    transferTo.setSingleTransId(generateSingleTransIdForIntrest/*generateSingleTransId*/);
                                }
                                transferTo.setInstrumentNo1("INTEREST");
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                                //tds credit to TDS head -- --- Code change 4
                                if (tdsAmount > 0) { // Added by nithya on 06-02-2020 for KD-1090
                                    txMap = new HashMap();
                                    transferTo = new TxTransferTO();
                                    txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.CR_AC_HD, tdsAcHd);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                    //txMap2.put("generateSingleTransId",generateSingleTransId);
                                    txMap.put("TRANS_MOD_TYPE", "TD");
                                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                    transferTo = transactionDAO.addTransferCreditLocal(txMap, tdsAmount);
                                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName("Deposit Account Renewal");
                                    transferList.add(transferTo);
                                }
                                // End
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                            } else if (depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                                txMap = new HashMap();
                                transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                double intAmount = 0;
                                double depAmount = 0;
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                //txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                txMap.put(TransferTrans.PARTICULARS, "Interest  "/*+ depositSubNo*/ + " From "+ olddepDt +" To "+ oldMatDt);
                                transferTo.setSingleTransId(generateSingleTransId);
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestTransAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
//                                transactionDAO.doTransferLocal(transferList, branchID);
                                if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                                    if (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")) {
                                        if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                            
                                        } else {
                                            txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                           
                                        }
                                        depAmount = depositTransAmt;
                                        txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                        txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                        txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, depAmount);
                                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                        transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                        transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                        transferTo.setSingleTransId(generateSingleTransId);
                                        transferTo.setScreenName("Deposit Account Renewal");
                                        transferList.add(transferTo);
                                    }
                                }
                                txMap = new HashMap();
                                HashMap remitBehavesMap = new HashMap();
                                remitBehavesMap.put("BEHAVES_LIKE", "PO");
                                List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", remitBehavesMap);
                                if (lstRemit != null && lstRemit.size() > 0) {
                                    remitBehavesMap = (HashMap) lstRemit.get(0);
                                }
                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, (String) remitBehavesMap.get("ISSUE_HD"));
                                System.out.println("INT HEAD100 ===" + (String) remitBehavesMap.get("ISSUE_HD"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);

                                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                                transferTo.setSingleTransId(generateSingleTransId);
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, depAmount + interestTransAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                                transferList.add(transferTo);
                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                                transactionDAO.doTransferLocal(transferList, initiatedBranchID);

                                LinkedHashMap notDeleteMap = new LinkedHashMap();
                                LinkedHashMap transferMap = new LinkedHashMap();
                                HashMap remittanceMap = new HashMap();
                                RemittanceIssueDAO remittanceIssueDAO = new RemittanceIssueDAO();
                                RemittanceIssueTO remittanceIssueTO = new RemittanceIssueTO();
                                TransactionTO transactionTODebit = new TransactionTO();
                                String favouringName = CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                transactionTODebit.setApplName(favouringName);
                                transactionTODebit.setTransAmt(new Double(depAmount + interestTransAmt));
                                transactionTODebit.setTransType("TRANSFER");
                                transactionTODebit.setProductType("RM");
                                transactionTODebit.setProductId("");
                                transactionTODebit.setDebitAcctNo(depositSubNo);
                                transactionTODebit.setStatus(CommonConstants.STATUS_CREATED);
                                notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                remittanceMap.put("TransactionTO", transferMap);
                                remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                remittanceMap.put("OPERATION_MODE", "ISSUE");
                                remittanceMap.put("AUTHORIZEMAP", null);
                                remittanceMap.put("USER_ID", depIntMap.get("USER_ID"));
                                remittanceMap.put("MODULE", "Remittance");
                                remittanceMap.put("MODE", "INSERT");
                                remittanceMap.put("SCREEN", "Issue");

                                HashMap draweeMap = new HashMap();
                                if (remitBehavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                    lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                    if (lstRemit != null && lstRemit.size() > 0) {
                                        draweeMap = (HashMap) lstRemit.get(0);
                                    }
                                }
                                remittanceIssueTO.setBatchId(getBatchId());
                                remittanceIssueTO.setIssueId(getIssueId());
                                remittanceIssueTO.setBatchDt((Date)currDt.clone());
                                remitProdId = CommonUtil.convertObjToStr(remitBehavesMap.get("PROD_ID"));
                                remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(remitBehavesMap.get("PROD_ID")));
                                remittanceIssueTO.setCity("560");
                                remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                remittanceIssueTO.setFavouring(favouringName);
                                remittanceIssueTO.setAmount(new Double(depositTransAmt + interestTransAmt));
                                remittanceIssueTO.setTotalAmt(new Double(depositTransAmt + interestTransAmt));
                                remittanceIssueTO.setVariableNo(CommonUtil.convertObjToStr(getVariableNo()));
                                remittanceIssueTO.setDraweeBranchCode(branchID);
                                remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                remittanceIssueTO.setBranchId(branchID);
                                remittanceIssueTO.setRemarks(depositSubNo);
                                remittanceIssueTO.setCommand("INSERT");
                                remittanceIssueTO.setStatus(CommonConstants.STATUS_CREATED);
                                remittanceIssueTO.setExchange(new Double(0.0));
                                remittanceIssueTO.setPostage(new Double(0.0));
                                remittanceIssueTO.setOtherCharges(new Double(0.0));
                                remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                remittanceIssueTO.setInstrumentNo1(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                remittanceIssueTO.setInstrumentNo2(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                                remittanceIssueTO.setStatusDt((Date)currDt.clone());
                                remittanceIssueTO.setStatusBy(String.valueOf(depIntMap.get("USER_ID")));
                                System.out.println(" remittanceMap :" + remittanceIssueTO);
                                transactionTODebit.setBranchId(_branchCode);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTODebit);
                                sqlMap.executeUpdate("insertRemittanceIssueTO", remittanceIssueTO);
                            }
                            if (oldBehaves.equals("CUMMULATIVE")) {
                                if (balanceIntAmt > 0) {
                                    remainAmt = balPayable + balPayInt - balanceIntAmt - interestTransAmt;
                                } else {
                                    remainAmt = balPayable + balPayInt - interestTransAmt;
                                }
                            } else if (!oldBehaves.equals("CUMMULATIVE") && balPayInt + balPayable - interestTransAmt > 0) {
                                if (interestTransAmt != balanceIntAmt) {
                                    if (balInt < 0) {
                                        remainAmt = balPayInt + balPayable + balInt - interestTransAmt;
                                    } else {
                                        remainAmt = balPayInt + balPayable - interestTransAmt;
                                    }
                                }
                                else {
                                    //added by Rishad For 13/11/2019 for previous interest Transaction   (only for previous interest)
                                    if (oldBehaves.equals("FIXED") && prevInt > 0) {
                                        if (balInt < 0) {
                                            remainAmt = balPayInt + balPayable + balInt - interestTransAmt;
                                        } else {
                                            remainAmt = balPayInt + balPayable - interestTransAmt;
                                        }
                                    }
                                }
                            }
                            System.out.println("remainAmt ::**** :: " + remainAmt);      
                            if(tdsAmount > 0){ // Added by nithya on 06-02-2020 for KD-1090
                                remainAmt = remainAmt - tdsAmount;
                            }
                            if (remainAmt > 0) {
                                txMap = new HashMap();
                                //commented by rishad 20/05/2014
//                                transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                                }
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.DR_BRANCH, branchID);
                                txMap.put(TransferTrans.PARTICULARS, " Interest :" + depositSubNo + "_1"); //changed for showing intrest on popup after save by jiv
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                transferTo.setSingleTransId(generateSingleTransId);
                                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                }
                                System.out.println("INT HEAD101 ===" + (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                                txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                                txMap.put("INITIATED_BRANCH", initiatedBranchID);
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, remainAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                              	transferList.add(transferTo);
                                 System.out.println("TransferTrans.PARTICULARS-------4-----"+TransferTrans.PARTICULARS);
                                txMap.put(TransferTrans.PARTICULARS, "Interest on:" + depositSubNo + "_1");//changed for showing intrest on popup after save by jiv
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                //commeneted by rishad 20/05/2014
//                                transferTo = transactionDAO.addTransferCreditLocal(txMap, remainAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Deposit Account Renewal");
                               	transferList.add(transferTo);
                                //commented by rishad 20/05/2014
//                                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                                transactionDAO.doTransferLocal(transferList, branchID);
                            }
                        }
                        //modified by rishad 20/05/2014

                        if (depIntMap.containsKey("ORIGINAL_DEP_AMT") && depIntMap.get("ORIGINAL_DEP_AMT") != null && !depositSubNo.equals(returnDepositNo)&&!oldBehaves.equals("CUMMULATIVE")) {
                            System.out.println("NEW DEP YES TRANSFER transaction payable debit credit to accts : " + depIntMap.get("ORIGINAL_DEP_AMT"));
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            //commeneted by rishad 
                           // transferList = new ArrayList();
                            double originalMatAmt = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                            double renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                            if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                                if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                                    if (balanceIntAmt < 0 && oldBehaves.equals("FIXED") && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                        originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                        System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                    } else {
                                        originalMatAmt = originalMatAmt - depositTransAmt;
                                    }
                                } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                    if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                        originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                        System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                    } else {
                                        originalMatAmt = originalMatAmt - depositTransAmt;
                                    }
                                }
                            } else if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("NO")) {
                                renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                                if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {
                                    if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                        originalMatAmt = originalMatAmt + balanceIntAmt;
                                        System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                    }
                                    System.out.println("RENEWAL_DEP_ADDING YES balanceIntAmt>0 : " + originalMatAmt);
                                } else if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("NO")) {
                                    if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                        if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                            originalMatAmt = originalMatAmt + balanceIntAmt;
                                            System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                        }
                                    }
                                }
                            }
                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                                    txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                                    txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                                }
                                //                        txMap.put(TransferTrans.DR_AC_HD,(String)newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            } else {
                                if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                                    txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                                    txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                                }
                            }
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                            txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, returnDepositNo + "_1");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                            if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            }
                            System.out.println("INT HEAD102 ===" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                            txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.CR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, originalMatAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1 W.e.f "+CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            //added and commented by rishad 27/05/2014
                            //transferTo = transactionDAO.addTransferCreditLocal(txMap, originalMatAmt);
                             if(depIntMap.containsKey("ADDING_DEP_AMT")&&depIntMap.get("ADDING_DEP_AMT")!=null)
                            {
                            renewalAmount=renewalAmount-CommonUtil.convertObjToDouble(depIntMap.get("ADDING_DEP_AMT"));
                            }
                             txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                             txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, renewalAmount);
                            /////////////
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);

                        }

                    }
                } else if (depIntMap.containsKey("ORIGINAL_DEP_AMT") && depIntMap.get("ORIGINAL_DEP_AMT") != null && !depositSubNo.equals(returnDepositNo)) {
                    System.out.println("NEW DEP YES TRANSFER transaction payable debit credit to accts : " + depIntMap.get("ORIGINAL_DEP_AMT"));
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
              //      transferList = new ArrayList();
                    double originalMatAmt = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                    double renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                    if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                        if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                            if (balanceIntAmt < 0 && oldBehaves.equals("FIXED") && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                            } else {
                                originalMatAmt = originalMatAmt - depositTransAmt;
                            }
                        } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                            if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                originalMatAmt = (originalMatAmt - depositTransAmt) + balanceIntAmt;
                                System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                            } else {
                                originalMatAmt = originalMatAmt - depositTransAmt;
                            }
                        }
                    } else if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("NO")) {
                        renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                        if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {
                            if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                originalMatAmt = originalMatAmt + balanceIntAmt;
                                System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                            }
                            System.out.println("RENEWAL_DEP_ADDING YES balanceIntAmt>0 : " + originalMatAmt);
                        } else if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("NO")) {
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                    originalMatAmt = originalMatAmt + balanceIntAmt;
                                    System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                }
                            }
                        }
                    }
                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                        }
                        //                        txMap.put(TransferTrans.DR_AC_HD,(String)newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    } else {
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                        }
                    }
                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                    txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, returnDepositNo + "_1");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    }
                    System.out.println("INT HEAD103 ===" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                    txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put("TRANS_MOD_TYPE", "TD");
                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, originalMatAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName("Deposit Account Renewal");
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1 W.e.f "+CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    txMap.put("TRANS_MOD_TYPE", "TD");
                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, originalMatAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName("Deposit Account Renewal");
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);

                }
//                added by nikhil
                if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {

                    HashMap newbehavesMap1 = new HashMap();
                    newbehavesMap1.put("PROD_ID", depIntMap.get("RENEWAL_PRODID"));
                    lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", newbehavesMap1);
                    String newProdHead = "";
                    if (lst != null && lst.size() > 0) {
                        newbehavesMap1 = (HashMap) lst.get(0);
                        newProdHead = CommonUtil.convertObjToStr(newbehavesMap1.get("ACCT_HEAD"));

                    }

                    newbehavesMap1 = null;
                    if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE").equals("CASH")
                            && depositRenewalAddAmt > 0) {
                        System.out.println("DEP YES CASH transaction payable debit credit to accts : " + depositRenewalAddAmt);
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        objCashTO.setTransId("");
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            // objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("FIXED_DEPOSIT_ACHD")));
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(newProdHead));
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("MATURITY_DEPOSIT")));
                        }
                        objCashTO.setProdType(SCREEN);
                        objCashTO.setActNum(CommonUtil.convertObjToStr(returnDepositNo + "_1"));
                        objCashTO.setProdId(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PRODID")));
                        objCashTO.setInpAmount(new Double(depositRenewalAddAmt));
                        objCashTO.setAmount(new Double(depositRenewalAddAmt));
                        objCashTO.setTransType(CommonConstants.CREDIT);
                        objCashTO.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        objCashTO.setBranchId(CommonUtil.convertObjToStr(branchID));
                        objCashTO.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        objCashTO.setStatusDt((Date)currDt.clone());
                        objCashTO.setTransDt((Date)currDt.clone());
                        objCashTO.setInstrumentNo2("DEPOSIT_TRANS");
                        //akhil@
                        //akhil@
                        if (objTO.getOpeningMode().equals("Normal")) {
                            objCashTO.setScreenName(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.SCREEN)));
                        } else {
                            objCashTO.setScreenName("Deposit Account Renewal");
                        }
//                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                        objCashTO.setParticulars("To Deposit Amount during renewal:" + depositSubNo + "_1");
                        objCashTO.setInitiatedBranch(initiatedBranchID);
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setLinkBatchId(depositSubNo + "_1");
                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objCashTO.setTransModType("TD");
                        objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                        ArrayList cashList = new ArrayList();
                        cashList.add(objCashTO);
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        HashMap interestTransMap = new HashMap();
//                        interestTransMap.put("RENEWAL_DEPOSIT_SCREEN","RENEWAL_DEPOSIT_SCREEN");
//                        interestTransMap.put("RENEWAL_LIST",cashList);
                        interestTransMap.put("BRANCH_CODE", initiatedBranchID);
                        interestTransMap.put("DAILYDEPOSITTRANSTO", cashList);
                        interestTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                        cashTransactionDAO.execute(interestTransMap, false);
                    } else if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE").equals("TRANSFER")
                            && depositRenewalAddAmt > 0) {
//                        if(!depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE").equals("") && !depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE").equals("RM")){
                        System.out.println("DEP YES TRANSFER NOT RM transaction payable debit credit to accts : " + depositRenewalAddAmt);
                        HashMap operativeDepMap = new HashMap();
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        transferTo = new TxTransferTO();
                        operativeDepMap.put("ACT_NUM", depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO"));
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", operativeDepMap);
                        if (lst != null && lst.size() > 0) {
                            operativeDepMap = (HashMap) lst.get(0);
                        }
                        //                        txMap.put(TransferTrans.DR_AC_HD, (String)oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            //txMap.put(TransferTrans.CR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            txMap.put(TransferTrans.CR_AC_HD, newProdHead);
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            txMap.put(TransferTrans.CR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                        }
                        System.out.println("INT HEAD103 ===" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                        txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO"));
                        if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE").equals("OA")) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) operativeDepMap.get("AC_HD_ID"));

                            txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODID"));
                            txMap.put(TransferTrans.DR_ACT_NUM, depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "OA");
                        } else if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE").equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "GL");
                        } else if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE").equals("AD")) {
                            txMap.put(TransferTrans.DR_AC_HD, depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put("TRANS_MOD_TYPE", "AD");
                        }
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        transferTo.setSingleTransId(generateSingleTransId);
                        txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                        txMap.put("INITIATED_BRANCH", initiatedBranchID);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, depositRenewalAddAmt);
                        transferTo.setInstrumentNo2("DEPOSIT_TRANS");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName("Deposit Account Renewal");
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1");
                        txMap.put("TRANS_MOD_TYPE", "TD");
                        txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                        txMap.put("INITIATED_BRANCH", initiatedBranchID);
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, depositRenewalAddAmt);
                        transferTo.setInstrumentNo2("DEPOSIT_TRANS");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName("Deposit Account Renewal");
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, initiatedBranchID);
//                        }
                    }
                }


//                TILL HERE

                if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                    if (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")
                            && depositTransAmt > 0) {
                        System.out.println("DEP YES CASH transaction payable debit credit to accts : " + depositTransAmt);
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        objCashTO.setTransId("");
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("FIXED_DEPOSIT_ACHD")));
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("MATURITY_DEPOSIT")));
                        }
                        objCashTO.setProdType(SCREEN);
                        objCashTO.setActNum(CommonUtil.convertObjToStr(depositSubNo + "_1"));
                        objCashTO.setProdId(CommonUtil.convertObjToStr(depIntMap.get("OLD_PROD_ID")));
                        objCashTO.setInpAmount(new Double(depositTransAmt));
                        objCashTO.setAmount(new Double(depositTransAmt));
                        objCashTO.setTransType(CommonConstants.DEBIT);
                        objCashTO.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        objCashTO.setBranchId(CommonUtil.convertObjToStr(branchID));
                        objCashTO.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                        objCashTO.setStatusDt((Date)currDt.clone());
                        objCashTO.setTransDt((Date)currDt.clone());
                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                        objCashTO.setParticulars("To Deposit Amount withdrawing :" + depositSubNo + "_1");
                        objCashTO.setInitiatedBranch(initiatedBranchID);
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setLinkBatchId(depositSubNo + "_1");
                        objCashTO.setTransModType("TD");
                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                        //akhil@
                        if (objTO.getOpeningMode().equals("Normal")) {
                            objCashTO.setScreenName(CommonUtil.convertObjToStr(depIntMap.get(CommonConstants.SCREEN)));
                        } else {
                            objCashTO.setScreenName("Deposit Account Renewal");
                        }
                        ArrayList cashList = new ArrayList();
                        cashList.add(objCashTO);
                        CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                        HashMap interestTransMap = new HashMap();
                        interestTransMap.put("RENEWAL_DEPOSIT_SCREEN", "RENEWAL_DEPOSIT_SCREEN");
                        interestTransMap.put("RENEWAL_LIST", cashList);
                        interestTransMap.put("BRANCH_CODE", initiatedBranchID);
                        interestTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                        cashTransactionDAO.execute(interestTransMap, false);
                    } else if (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")
                            && depositTransAmt > 0) {
                        if (!depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                            System.out.println("DEP YES TRANSFER NOT RM transaction payable debit credit to accts : " + depositTransAmt);
                            HashMap operativeDepMap = new HashMap();
                            txMap = new HashMap();
                            transferList = new ArrayList();
                            transferTo = new TxTransferTO();
                            operativeDepMap.put("ACT_NUM", depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                            lst = sqlMap.executeQueryForList("getAccNoProdIdDet", operativeDepMap);
                            if (lst != null && lst.size() > 0) {
                                operativeDepMap = (HashMap) lst.get(0);
                            }
                            //                        txMap.put(TransferTrans.DR_AC_HD, (String)oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                                txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                            } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                                txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                            }
                            txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                            txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.PARTICULARS, depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put("TRANS_MOD_TYPE", "TD");
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, depositTransAmt);

                            if (depIntMap.containsKey("RENEWAL_DEP_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("OA")) {
                                txMap.put(TransferTrans.CR_AC_HD, (String) operativeDepMap.get("AC_HD_ID"));
                                System.out.println("INT HEAD104 ===" + (String) operativeDepMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_DEP_TRANS_PRODID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("TRANS_MOD_TYPE", "OA");
                            } else if (depIntMap.containsKey("RENEWAL_DEP_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("GL")) {
                                txMap.put(TransferTrans.CR_AC_HD, depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                System.out.println("INT HEAD105 ===" + depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            } else if (depIntMap.containsKey("RENEWAL_DEP_TRANS_PRODTYPE") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("AD")) {
                                txMap.put(TransferTrans.CR_AC_HD, depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                System.out.println("INT HEAD106 ===" + depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                                txMap.put(TransferTrans.CR_BRANCH, branchID);
                                txMap.put("TRANS_MOD_TYPE", "AD");
                            }
                           
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1");
                            txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                            txMap.put("INITIATED_BRANCH", initiatedBranchID);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, depositTransAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setScreenName("Deposit Account Renewal");
                            transferList.add(transferTo);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                        } else if (depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE") != null && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                            double intTransAmt = 0;
                            System.out.println("DEP YES TRANSFER RM transaction payable debit credit to accts : " + depositTransAmt+"intTransAmt:"+intTransAmt);
//                            if(oldBehaves.equals("FIXED") && depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")){
//                                intTransAmt = interestTransAmt + depositTransAmt;
//                            }else if(oldBehaves.equals("CUMMULATIVE") && depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")){
//                                intTransAmt = remainAmt + depositTransAmt;
//                            }
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                intTransAmt = interestTransAmt + depositTransAmt;
                            } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                                intTransAmt = remainAmt + depositTransAmt;
                            }
                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                LinkedHashMap notDeleteMap = new LinkedHashMap();
                                LinkedHashMap transferMap = new LinkedHashMap();
                                HashMap remittanceMap = new HashMap();
                                RemittanceIssueDAO remittanceIssueDAO = new RemittanceIssueDAO();
                                RemittanceIssueTO remittanceIssueTO = new RemittanceIssueTO();
                                TransactionTO transactionTODebit = new TransactionTO();
                                String favouringName = CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                                transactionTODebit.setApplName(favouringName);
                                transactionTODebit.setTransAmt(new Double(intTransAmt));
                                transactionTODebit.setTransType("TRANSFER");
                                transactionTODebit.setProductId(CommonUtil.convertObjToStr(depIntMap.get("OLD_PROD_ID")));
                                transactionTODebit.setProductType("TD");
                                transactionTODebit.setDebitAcctNo(depositSubNo + "_1");
                                remittanceIssueDAO.setFromotherDAo(false);
                                notDeleteMap.put(String.valueOf(1), transactionTODebit);
                                transferMap.put("NOT_DELETED_TRANS_TOs", notDeleteMap);
                                remittanceMap.put("TransactionTO", transferMap);
                                remittanceMap.put(CommonConstants.BRANCH_ID, branchID);
                                remittanceMap.put("OPERATION_MODE", "ISSUE");
                                remittanceMap.put("AUTHORIZEMAP", null);
                                remittanceMap.put("USER_ID", depIntMap.get("USER_ID"));
                                remittanceMap.put("MODULE", "Remittance");
                                remittanceMap.put("MODE", "INSERT");
                                remittanceMap.put("SCREEN", "depositRenewalIssue");
                                HashMap behavesMap = new HashMap();
                                behavesMap.put("BEHAVES_LIKE", "PO");
                                List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                                if (lstRemit != null && lstRemit.size() > 0) {
                                    behavesMap = (HashMap) lstRemit.get(0);
                                }
                                HashMap draweeMap = new HashMap();
                                if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                                    lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                                    if (lstRemit != null && lstRemit.size() > 0) {
                                        draweeMap = (HashMap) lstRemit.get(0);
                                    }
                                }
                                remittanceIssueTO.setDraweeBranchCode(branchID);
                                remittanceIssueTO.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                                remittanceIssueTO.setCategory("GENERAL_CATEGORY");
                                remittanceIssueTO.setCity("560");
                                remittanceIssueTO.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                                remittanceIssueTO.setFavouring(favouringName);
                                remittanceIssueTO.setBranchId(branchID);
                                remittanceIssueTO.setRemarks(depositSubNo);
                                remittanceIssueTO.setCommand("INSERT");
                                remittanceIssueTO.setAmount(new Double(intTransAmt));
                                remittanceIssueTO.setTotalAmt(new Double(intTransAmt));
                                remittanceIssueTO.setExchange(new Double(0.0));
                                remittanceIssueTO.setPostage(new Double(0.0));
                                remittanceIssueTO.setOtherCharges(new Double(0.0));
                                remittanceIssueTO.setAuthorizeRemark("DEPOSIT_PAY_ORDER");
                                remittanceIssueTO.setInstrumentNo1(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                remittanceIssueTO.setInstrumentNo2(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                                remittanceIssueTO.setStatusDt((Date)currDt.clone());
                                remittanceIssueTO.setStatusBy(String.valueOf(depIntMap.get("USER_ID")));
                                LinkedHashMap remitMap = new LinkedHashMap();
                                LinkedHashMap remMap = new LinkedHashMap();
                                remMap.put(String.valueOf(1), remittanceIssueTO);
                                remitMap.put("NOT_DELETED_ISSUE_TOs", remMap);
                                remittanceMap.put("RemittanceIssueTO", remitMap);
                                System.out.println(" remittanceMap :" + remittanceMap);
                                remittanceIssueDAO.execute(remittanceMap);
                            }
                        }
                    }
                }
                if (depositSubNo.equals(returnDepositNo) && newBehaves.equals("FIXED")) {
                    txMap = new HashMap();
                  //transferList = new ArrayList();
                    transferTo = new TxTransferTO();
                    double originalMatAmt = 0.0;
                    double renewalAmount = 0.0;
                    if(fdRenewSanmeNoPrincAmt!=null && fdRenewSanmeNoPrincAmt.length()>0 && fdRenewSanmeNoPrincAmt.equals("Y")){    
	                    originalMatAmt = CommonUtil.convertObjToDouble(depIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
	                    renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                    }
                    if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("NO")) {
                        renewalAmount = CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")).doubleValue();
                        if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {
                            if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                originalMatAmt = originalMatAmt + balanceIntAmt;
                                originalMatAmt =  (double) getNearest((long) (originalMatAmt * 100), 100) / 100;
                                System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                            }
                            System.out.println("RENEWAL_DEP_ADDING YES balanceIntAmt>0 : " + originalMatAmt);
                        } else if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("NO")) {

                            if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                                if (balanceIntAmt < 0 && oldBehaves.equals("FIXED")) {
                                    originalMatAmt = originalMatAmt + balanceIntAmt;
                                    originalMatAmt =  (double) getNearest((long) (originalMatAmt * 100), 100) / 100;
                                    System.out.println("RENEWAL_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                                }
                            }
                        }
                    }

                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        if (balanceMap.get("ACCT_STATUS").equals("NEW")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        } else if (balanceMap.get("ACCT_STATUS").equals("MATURED")) {
                            txMap.put(TransferTrans.DR_AC_HD, oldacctHeadMap.get("MATURITY_DEPOSIT"));
                        }
                    }

                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                    txMap.put(TransferTrans.DR_PROD_ID, depIntMap.get("OLD_PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, returnDepositNo + "_1");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    }
                    System.out.println("INT HEAD107 ===" + (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                    txMap.put(TransferTrans.CR_PROD_ID, depIntMap.get("RENEWAL_PRODID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.CR_BRANCH, branchID);
                    txMap.put("TRANS_MOD_TYPE", "TD");
                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, originalMatAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName("Deposit Account Renewal");
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1");
                    //comm by jiby
                    txMap.put("TRANS_MOD_TYPE", "TD");
                    txMap.put("DR_INSTRUMENT_2", "Deposit Account Renewal");
                    txMap.put("INITIATED_BRANCH", initiatedBranchID);
                    //modified by rishad 07/04/2014 
                    double transamt = 0;
                    if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                        transamt = originalMatAmt + balPayInt - interestTransAmt;
                    } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("N")) {
                        transamt = originalMatAmt;
                    } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO") && depIntMap.get("RENEWAL_INT_PAYABLE").equals("Y")) {
                        //added by rishad 19/04/2020 for avoiding issue at time of  without transaction 
                        if (fdRenewSanmeNoPrincAmt != null && fdRenewSanmeNoPrincAmt.length() > 0 && fdRenewSanmeNoPrincAmt.equals("Y")) {
                            transamt = renewalAmount;
                        } else {
                            transamt = originalMatAmt + balPayInt;
                        }
                    } else {
                        transamt = originalMatAmt + balPayInt;
                    }
                    
                    if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                        if (tdsAmount > 0) { // Added by nithya on 06-02-2020 for KD-1090
                            transamt = transamt - tdsAmount;
                        }
                    }
                    
                   // transferTo = transactionDAO.addTransferCreditLocal(txMap, originalMatAmt+balPayInt);
                    transamt =  (double) getNearest((long) (transamt * 100), 100) / 100;
                    transferTo = transactionDAO.addTransferCreditLocal(txMap,transamt);
                    //
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    transferTo.setScreenName("Deposit Account Renewal");
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, initiatedBranchID);
                }
                System.out.println("completed all transactions : " + txMap);
                depIntMap.put("DEPOSIT_NO", depositSubNo + "_1");
                depIntMap.put("BRANCH_CODE", branchID);
                balPay = balPay + balPayable;
                depIntMap.put("BALANCE_AMT", new Double(balPay));
            }
        }
        return depIntMap;
    }
    //--- Inserts the data Record by record in DepSubNoAccInfoTO Table

    private void insertDepSubAccInfoDetails(HashMap map, HashMap transDetailMap, String cash_unique_id) throws Exception {
        
        try
        {
        DepSubNoAccInfoTO depSubNoAccInfoTO;
        String behavesLilike = ""; //08-05-2020        
        int depSubNoSize = mapDepSubNoAccInfoTO.size();
        for (int i = 0; i < depSubNoSize; i++) {
            depSubNoAccInfoTO = (DepSubNoAccInfoTO) mapDepSubNoAccInfoTO.get(String.valueOf(i));
            if ((objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT) ||(depSubNoAccInfoTO.getDepositDt()==null))){//Added By Suresh KDSA-143 Some times Deposit Opening Deposit Date wrongly inserting.
                depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
            }
            System.out.println("depSubNoAccInfoTO"+depSubNoAccInfoTO);
            depSubNoAccInfoTO.setDepositNo(objTO.getDepositNo());
            depSubNoAccInfoTO.setSubstatusBy(objLogTO.getUserId());
            depSubNoAccInfoTO.setMultipleDepositId(objTO.getMultipleDepositId());
            HashMap oldbehavesMap = new HashMap();
            oldbehavesMap.put("PROD_ID", objTO.getProdId());
            List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
            if (lstProd != null && lstProd.size() > 0) {
                oldbehavesMap = (HashMap) lstProd.get(0);
                depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(oldbehavesMap.get("ACCT_HEAD")));
                transDetailMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(oldbehavesMap.get("ACCT_HEAD")));
                behavesLilike = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE")); //08-05-2020
            }
            oldbehavesMap = null;
            System.out.println("#### depSubNoAccInfoTO : " + depSubNoAccInfoTO);
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            if (!objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)
                    && !objTO.getCommand().equals(CommonConstants.TOSTATUS_EXTENSION)) {
                if (depSubNoAccInfoTO.getTotalIntCredit() == null) {
                    depSubNoAccInfoTO.setTotalIntCredit(new Double(0));
                }
                if (depSubNoAccInfoTO.getTotalIntDrawn() == null) {
                    depSubNoAccInfoTO.setTotalIntDrawn(new Double(0));
                }
                depSubNoAccInfoTO.setPaymentDay((Date)currDt.clone());
                depSubNoAccInfoTO.setRenewedDt((Date)currDt.clone());//31-05-2019
                if(behavesLilike.equalsIgnoreCase("RECURRING") && depSubNoAccInfoTO.getIntpayMode().equalsIgnoreCase("TRANSFER")){ //08-05-2020
                    depSubNoAccInfoTO.setIntPayProdType("TD");
                    depSubNoAccInfoTO.setIntPayProdId(objTO.getProdId());
                    depSubNoAccInfoTO.setIntPayAcNo(objTO.getDepositNo()+"_1");                   
                }
                sqlMap.executeUpdate("insertDepSubNoAccInfoTO", depSubNoAccInfoTO);
                if(behavesLilike.equalsIgnoreCase("RECURRING") && depSubNoAccInfoTO.getIntpayMode().equalsIgnoreCase("TRANSFER")){ //08-05-2020
                    sqlMap.executeUpdate("updateCalendarDayForSpecialRD", depSubNoAccInfoTO);
                }
//                for executing transaction part added by NIKHIL
                if (transDetailMap != null && transDetailMap.size() > 0) {
                    executeTransactionPart(map, transDetailMap);
                }
                if (map.containsKey("TransactionTO")) {
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);

                    transactionDAO.setBatchId(depSubNoAccInfoTO.getDepositNo());
                    transactionDAO.setBatchDate((Date)currDt.clone());
                    transactionDAO.execute(map);
                }

            } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {//--- If renewal Accnt, then clos the old accnt
                DepSubNoAccInfoTO depSubNoAccInfoTOSame = depSubNoAccInfoTO;
                depSubNoAccInfoTOSame.setDepositNo(objTO.getDepositNo());
                depSubNoAccInfoTOSame.setSubstatusBy(objLogTO.getUserId());
                depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                HashMap statusMap = new HashMap();
                double remain = 0;
                statusMap.put(DEPOSITNO, objTO.getRenewalFromDeposit());
                List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", statusMap);
                if (lst != null && lst.size() > 0) {
                    statusMap = (HashMap) lst.get(0);
                    depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_NO")));
                    depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(statusMap.get("DEPOSIT_SUB_NO")));
                    depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_DT"))));
                    depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_DD")));
                    depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_MM")));
                    depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_YY")));
                    depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_AMT")));
                    depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(statusMap.get("INTPAY_MODE")));
                    depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(statusMap.get("INTPAY_FREQ")));
                    depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("MATURITY_DT"))));
                    depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(statusMap.get("RATE_OF_INT")));
                    depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(statusMap.get("MATURITY_AMT")));
                    depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(statusMap.get("TOT_INT_AMT")));
                    depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(statusMap.get("PERIODIC_INT_AMT")));
                    depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(statusMap.get("STATUS")));
                    depSubNoAccInfoTO.setClearBalance(CommonUtil.convertObjToDouble(statusMap.get("CLEAR_BALANCE")));
                    depSubNoAccInfoTO.setAvailableBalance(CommonUtil.convertObjToDouble(statusMap.get("AVAILABLE_BALANCE")));
                    depSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(statusMap.get("POSTAGE_AMT")));
                    depSubNoAccInfoTO.setCloseDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setCloseBy(objTO.getCreatedBy());
                    depSubNoAccInfoTO.setSubstatusBy(objTO.getCreatedBy());
                    depSubNoAccInfoTO.setSubstatusDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setAuthorizeDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setPenalInt("");
                    depSubNoAccInfoTO.setAcctStatus(CommonConstants.CLOSED);
                    depSubNoAccInfoTO.setLastTransDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_TRANS_DT"))));
                    depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_INT_APPL_DT"))));
                    depSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_CREDIT")));
                    double totIntAmt = CommonUtil.convertObjToDouble(statusMap.get("TOT_INT_AMT")).doubleValue();
                    double totCredit = CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_CREDIT")).doubleValue();
                    remain = totIntAmt - totCredit;
                    depSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_DRAWN")));
                    depSubNoAccInfoTO.setTotalBalance(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_BALANCE")));
                    depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(statusMap.get("INSTALL_TYPE")));
                    depSubNoAccInfoTO.setPaymentDay((Date)currDt.clone());
//                    depSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(statusMap.get("CALENDER_DAY")));
                    depSubNoAccInfoTO.setFlexi_status("NR");
                    //Changed By Suresh
                    depSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CALENDER_FREQ")));
                    if (depIntMap.get("RENEWAL_CALENDER_FREQ_DAY") != null) {
                        depSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                    } else {
                        depSubNoAccInfoTO.setCalender_day(new Double(0));
                    }
                    if (depIntMap.get("RENEWAL_INT_PAYABLE") != null && depIntMap.get("RENEWAL_INT_PAYABLE").equals("N")) {
                        depSubNoAccInfoTO.setRenewintpayable(CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")).doubleValue());
                    } else {
                        depSubNoAccInfoTO.setRenewintpayable(0.0);
                    }
                    depSubNoAccInfoTO.setRenewintpaid(0.0);
                    depSubNoAccInfoTO.setCreatedDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setRenewedDt(getProperDateFormat(statusMap.get("RENEWED_DT")));//31-05-2019
                    if (map.containsKey("SAME_DEPOSIT_NO")) {
                        sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
                    }
                    String newNo = objTO.getDepositNo();
                    String oldNo = objTO.getRenewalFromDeposit();
                    double count = CommonUtil.convertObjToDouble(objTO.getRenewalCount()).doubleValue();
                    HashMap slNoMap = new HashMap();
                    HashMap renewalMap = new HashMap();
                    slNoMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                    renewalMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                    lst = sqlMap.executeQueryForList("getSelectMaxSLNo", renewalMap);
                    if (lst != null && lst.size() > 0) {
                        renewalMap = (HashMap) lst.get(0);
                        double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                        renewalMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                        renewalMap.put("COUNT", new Double(maxNo + 1));
                        sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                    } else {
                        renewalMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                        renewalMap.put("COUNT", new Double(1));
                        sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                    }
                    renewalMap = null;
                    if (depSubNoAccInfoTO.getIntpayMode().equals("TRANSFER")) {
                        if (statusMap.get("INT_PAY_PROD_TYPE").equals("GL") || statusMap.get("INT_PAY_PROD_TYPE").equals("RM")) {
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTONoProdIdSameNo", slNoMap);
                        } else {
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_ID", statusMap.get("INT_PAY_PROD_ID"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTOSameNo", slNoMap);
                        }
                    }
                    slNoMap = null;
                }
                statusMap = null;
                sqlMap.executeUpdate("updateFlexiStatus", objTO.getRenewalFromDeposit());
                depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                depSubNoAccInfoTO = depSubNoAccInfoTOSame;
                depSubNoAccInfoTO.setPostageAmt(depSubNoAccInfoTOSame.getRenewPostageAmt());
                depSubNoAccInfoTO.setDepositDt(depSubNoAccInfoTOSame.getDepositDt());// 
                sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                //Changed By Suresh
                depSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CALENDER_FREQ")));
                if (depIntMap.get("RENEWAL_CALENDER_FREQ_DAY") != null) {
                    depSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                } else {
                    depSubNoAccInfoTO.setCalender_day(new Double(0));
                }
                HashMap intMap = new HashMap();
                intMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", intMap);
                if (lst != null && lst.size() > 0) {
                    intMap = (HashMap) lst.get(0);
                    intMap.put("ACT_NUM", objTO.getRenewalFromDeposit());
                    intMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                    intMap.put("CURR_RATE_OF_INT", CommonUtil.convertObjToDouble(intMap.get("CURR_RATE_OF_INT")));
                    intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")));
                    intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")));
                    intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(remain));
                    intMap.put("INT", CommonUtil.convertObjToDouble(intMap.get("PENAL_RATE")));
                    sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                }
                //updating the previous balint payable if balInt > 0
                if (depIntMap.containsKey("PREV_INT_AMT")) {
                    double amt = 0.0;
                    amt = CommonUtil.convertObjToDouble(depIntMap.get("PREV_INT_AMT")).doubleValue();
                    if (amt > 0) {
                        intMap.put("PREV_INT_AMT", amt);
                        intMap.put("PAID_DATE", currDt.clone());
                        sqlMap.executeUpdate("updateIntpaidSameNo", intMap);//sameno int paid
                    }
                }
                intMap = null;
                oldbehavesMap = new HashMap();
                oldbehavesMap.put("PROD_ID", depIntMap.get("OLD_PROD_ID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
                if (lst != null && lst.size() > 0) {
                    oldbehavesMap = (HashMap) lst.get(0);
                }
                String oldBehaves = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE"));
                oldbehavesMap = null;
                HashMap newbehavesMap = new HashMap();
                newbehavesMap.put("PROD_ID", depIntMap.get("RENEWAL_PRODID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", newbehavesMap);
                if (lst != null && lst.size() > 0) {
                    newbehavesMap = (HashMap) lst.get(0);
                    depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(newbehavesMap.get("ACCT_HEAD")));
                }
                String newBehaves = CommonUtil.convertObjToStr(newbehavesMap.get("BEHAVES_LIKE"));
                newbehavesMap = null;
                double matAmt=CommonUtil.convertObjToDouble(depSubNoAccInfoTO.getMaturityAmt());
                matAmt=(double) getNearest((long) (matAmt * 100), 100) / 100;
                depIntMap.put("ORIGINAL_DEP_AMT", CommonUtil.convertObjToStr(matAmt));
                depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT"))));
                depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_DAYS")));
                depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_MONTHS")));
                depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_YEARS")));
                depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")));
                depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_MODE")));
                if (newBehaves.equals("CUMMULATIVE")) {
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                    depSubNoAccInfoTO.setIntpayMode("");
                    depSubNoAccInfoTO.setIntpayFreq(new Double(0));
                } else if (newBehaves.equals("FIXED")) {
                    if (CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_FREQ")).equals("Quarterly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(90));
                    } else if (CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_FREQ")).equals("Half Yearly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(180));
                    } else if (CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_FREQ")).equals("Yearly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(360));
                    } else if (CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_FREQ")).equals("Monthly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(30));
                    } else {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(0));
                    }
                }
                depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_MATURITY_DT"))));
                depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_RATE_OF_INT")));
                depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_MATURITY_AMT")));
                depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_TOT_INTAMT")));
                depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_PERIODIC_INT")));
                depSubNoAccInfoTO.setSubstatusBy(CommonUtil.convertObjToStr(depIntMap.get("USER_ID")));
                depSubNoAccInfoTO.setSubstatusDt((Date)currDt.clone());
                depSubNoAccInfoTO.setFlexi_status("NR");
                depSubNoAccInfoTO.setTotalIntCredit(new Double(0));
                depSubNoAccInfoTO.setTotalIntDrawn(new Double(0));
                depSubNoAccInfoTO.setPaymentDay((Date)currDt.clone());

                System.out.println("depIntMap : " + depIntMap);
                HashMap renewalDetails = new HashMap();
                renewalDetails.put("DEPOSIT_NO", objTO.getDepositNo());
                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT")));
                Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_MATURITY_DT")));
                renewalDetails.put("RENEWAL_DEPOSIT_DT", depDt);
                renewalDetails.put("RENEWAL_MATURITY_DT", matDt);
                renewalDetails.put("RENEWAL_DEPOSIT_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")));
                renewalDetails.put("RENEWAL_MATURITY_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_MATURITY_AMT")));
                renewalDetails.put("RENEWAL_DEPOSIT_DAYS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_DAYS")));
                renewalDetails.put("RENEWAL_DEPOSIT_MONTHS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_MONTHS")));
                renewalDetails.put("RENEWAL_DEPOSIT_YEARS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_YEARS")));
                renewalDetails.put("RENEWAL_RATE_OF_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_RATE_OF_INT")));
                renewalDetails.put("RENEWAL_PERIODIC_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_PERIODIC_INT")));
                renewalDetails.put("RENEWAL_TOT_INTAMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_TOT_INTAMT")));
                renewalDetails.put("STATUS", CommonConstants.STATUS_CREATED);
                renewalDetails.put("OLD_DEPOSIT_NO", objTO.getRenewalFromDeposit());
                if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES")) {
                    if (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")) {
                        renewalDetails.put("RENEWAL_DEP_TOKEN_NO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODTYPE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODID", String.valueOf(""));
                        renewalDetails.put("RENEWAL_DEP_TRANS_ACCNO", String.valueOf(""));
                    } else if (depIntMap.containsKey("RENEWAL_DEP_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")) {
                        renewalDetails.put("RENEWAL_DEP_TOKEN_NO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TOKEN_NO")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODTYPE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODID", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_PRODID")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_ACCNO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                    }
                    renewalDetails.put("RENEWAL_DEP_TRANS_MODE", depIntMap.get("RENEWAL_DEP_TRANS_MODE"));
                    double withdrawDepAmt = CommonUtil.convertObjToDouble(depIntMap.get("WITDRAWING_DEP_AMT")).doubleValue();
                    renewalDetails.put("WITDRAWING_DEP_AMT",withdrawDepAmt);
                    renewalDetails.put("RENEWAL_DEP_WITHDRAWING", "Y");
                } else if (depIntMap.containsKey("RENEWAL_DEP_WITHDRAWING") && depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("NO")) {
                    renewalDetails.put("RENEWAL_DEP_WITHDRAWING", "N");
                    renewalDetails.put("RENEWAL_DEP_TRANS_MODE", String.valueOf(""));
                    renewalDetails.put("WITDRAWING_DEP_AMT", CommonUtil.convertObjToDouble("0"));
                    renewalDetails.put("RENEWAL_DEP_TOKEN_NO", String.valueOf(""));
                    renewalDetails.put("RENEWAL_DEP_TRANS_PRODTYPE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_DEP_TRANS_PRODID", String.valueOf(""));
                    renewalDetails.put("RENEWAL_DEP_TRANS_ACCNO", String.valueOf(""));
                }

//                changed by nikhil
                if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("YES")) {
                    renewalDetails.put("RENEWAL_DEP_ADDING", "Y");
                    double addingDepAmt = CommonUtil.convertObjToDouble(depIntMap.get("ADDING_DEP_AMT")).doubleValue();
                    renewalDetails.put("ADDING_DEP_AMT", addingDepAmt);
                    if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE").equals("CASH")) {
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODTYPE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODID", String.valueOf(""));
                        renewalDetails.put("RENEWAL_DEP_TRANS_ACCNO", String.valueOf(""));
                    } else if (depIntMap.containsKey("RENEWAL_DEP_ADD_TRANS_MODE") && depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE").equals("TRANSFER")) {
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODTYPE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODTYPE")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_PRODID", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_ADD_TRANS_PRODID")));
                        renewalDetails.put("RENEWAL_DEP_TRANS_ACCNO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_ADD_TRANS_ACCNO")));
                    }
                    renewalDetails.put("RENEWAL_DEP_TRANS_MODE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEP_ADD_TRANS_MODE")));
                } else if (depIntMap.containsKey("RENEWAL_DEP_ADDING") && depIntMap.get("RENEWAL_DEP_ADDING").equals("NO")) {
                    renewalDetails.put("RENEWAL_DEP_ADDING", "N");
                    renewalDetails.put("ADDING_DEP_AMT", CommonUtil.convertObjToDouble("0"));
                }

                if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES")) {
                    if (depIntMap.containsKey("RENEWAL_INT_TRANS_MODE") && depIntMap.get("RENEWAL_INT_TRANS_MODE").equals("CASH")) {
                        renewalDetails.put("RENEWAL_INT_TOKEN_NO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TOKEN_NO")));
                        renewalDetails.put("RENEWAL_INT_TRANS_PRODTYPE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_INT_TRANS_PRODID", String.valueOf(""));
                        renewalDetails.put("RENEWAL_INT_TRANS_ACCNO", String.valueOf(""));
                    } else if (depIntMap.containsKey("RENEWAL_INT_TRANS_MODE") && depIntMap.get("RENEWAL_INT_TRANS_MODE").equals("TRANSFER")) {
                        renewalDetails.put("RENEWAL_INT_TOKEN_NO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TOKEN_NO")));
                        renewalDetails.put("RENEWAL_INT_TRANS_PRODTYPE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE")));
                        renewalDetails.put("RENEWAL_INT_TRANS_PRODID", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TRANS_PRODID")));
                        renewalDetails.put("RENEWAL_INT_TRANS_ACCNO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_INT_TRANS_ACCNO")));
                    }
                    double intAmt = CommonUtil.convertObjToDouble(depIntMap.get("WITHDRAWING_INT_AMT")).doubleValue();
                    renewalDetails.put("WITHDRAWING_INT_AMT",intAmt);
                    renewalDetails.put("RENEWAL_INT_TRANS_MODE", depIntMap.get("RENEWAL_INT_TRANS_MODE"));
                    renewalDetails.put("RENEWAL_INT_WITHDRAWING", "Y");
                } else if (depIntMap.containsKey("RENEWAL_INT_WITHDRAWING") && depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("NO")) {
                    renewalDetails.put("RENEWAL_INT_WITHDRAWING", "N");
                    renewalDetails.put("WITHDRAWING_INT_AMT", CommonUtil.convertObjToDouble("0"));
                    renewalDetails.put("RENEWAL_INT_TRANS_MODE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_INT_TOKEN_NO", String.valueOf(""));
                    renewalDetails.put("RENEWAL_INT_TRANS_PRODTYPE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_INT_TRANS_PRODID", String.valueOf(""));
                    renewalDetails.put("RENEWAL_INT_TRANS_ACCNO", String.valueOf(""));
                }
                if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("CASH")) {
                    renewalDetails.put("RENEWAL_PAY_MODE", "CASH");
                    renewalDetails.put("RENEWAL_PAY_PRODTYPE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_PAY_PRODID", String.valueOf(""));
                    renewalDetails.put("RENEWAL_PAY_ACCNO", String.valueOf(""));
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                } else if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("TRANSFER")) {
                    renewalDetails.put("RENEWAL_PAY_MODE", "TRANSFER");
                    renewalDetails.put("RENEWAL_PAY_PRODTYPE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_PRODTYPE")));
                    renewalDetails.put("RENEWAL_PAY_PRODID", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_PRODID")));
                    renewalDetails.put("RENEWAL_PAY_ACCNO", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_ACCNO")));
                    depSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_PRODTYPE")));
                    depSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_PRODID")));
                    depSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_ACCNO")));
                } else if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("")) {
                    renewalDetails.put("RENEWAL_PAY_MODE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_PAY_PRODTYPE", String.valueOf(""));
                    renewalDetails.put("RENEWAL_PAY_PRODID", String.valueOf(""));
                    renewalDetails.put("RENEWAL_PAY_ACCNO", String.valueOf(""));
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                }
                if (depIntMap.containsKey("FLD_DEP_RENEWAL_SUB_PRINTINGNO") && depIntMap.get("FLD_DEP_RENEWAL_SUB_PRINTINGNO").equals("")) {
                    renewalDetails.put("RENEWAL_PRINTING_NO", String.valueOf(""));
                } else {
                    renewalDetails.put("RENEWAL_PRINTING_NO", depIntMap.get("FLD_DEP_RENEWAL_SUB_PRINTINGNO"));
                }
                //added by rishad at 26/12/2019 for previous interest consideration
                double prevInt = 0;
                if (depIntMap.containsKey("PREV_INT_AMT")) {
                    prevInt = CommonUtil.convertObjToDouble(depIntMap.get("PREV_INT_AMT"));
                }
                renewalDetails.put("PREVIOUS_INT", prevInt);
                renewalDetails.put("BALANCE_INT_AMT", CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")));
                renewalDetails.put("SB_INT_AMT", CommonUtil.convertObjToDouble(depIntMap.get("SB_INT_AMT")));
                renewalDetails.put("SB_PERIOD_RUN", CommonUtil.convertObjToStr(depIntMap.get("SB_PERIOD_RUN")));
                renewalDetails.put("RENEWAL_PRODID", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PRODID")));
                renewalDetails.put("RENEWAL_CATEGORY", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CATEGORY")));
                renewalDetails.put("RENEWAL_PAY_FREQ", depIntMap.get("RENEWAL_PAY_FREQ"));
                renewalDetails.put("BRANCH_CODE", CommonUtil.convertObjToStr(depIntMap.get("BRANCH_CODE")));
                renewalDetails.put("RENEWAL_CALENDER_FREQ", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CALENDER_FREQ")));
                if (depIntMap.get("RENEWAL_CALENDER_FREQ_DAY") != null) {
                    renewalDetails.put("RENEWAL_CALENDER_DAY", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                } else {
                    renewalDetails.put("RENEWAL_CALENDER_DAY", CommonUtil.convertObjToDouble(String.valueOf("0")));
                }
                System.out.println("renewalDetails : " + renewalDetails);
                HashMap renewalMap = new HashMap();
                renewalMap.put("OLD_DEPOSIT_NO", objTO.getRenewalFromDeposit());
                lst = sqlMap.executeQueryForList("getSelectMaxTempSLNo", renewalMap);
                if (lst != null && lst.size() > 0) {
                    renewalMap = (HashMap) lst.get(0);
                    double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                    renewalDetails.put("SL_NO", new Double(maxNo + 1));
                } else {
                    renewalDetails.put("SL_NO", new Double(1));
                }
                renewalDetails.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEW_POSTAGE_AMT")));
                renewalDetails.put("PENDING_AMT_RATE", depIntMap.get("PENDING_AMT_RATE"));
                renewalDetails.put("WITDRAWING_DEP_AMT", CommonUtil.convertObjToDouble(renewalDetails.get("WITDRAWING_DEP_AMT")));
                sqlMap.executeUpdate("insertRenewalDetails", renewalDetails);
                if (sameNo == true) {
                    if (renewalDetails.get("RENEWAL_PAY_FREQ").equals("Date of Maturity")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(0));
                    } else if (renewalDetails.get("RENEWAL_PAY_FREQ").equals("Monthly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(30));
                    } else if (renewalDetails.get("RENEWAL_PAY_FREQ").equals("Quaterly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(90));
                    } else if (renewalDetails.get("RENEWAL_PAY_FREQ").equals("Half Yearly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(180));
                    } else if (renewalDetails.get("RENEWAL_PAY_FREQ").equals("Yearly")) {
                        depSubNoAccInfoTO.setIntpayFreq(new Double(360));
                    }
                    sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                } else {
                    depSubNoAccInfoTO.setRenewedDt((Date)currDt.clone());//31-05-2019
                    sqlMap.executeUpdate("insertDepSubNoAccInfoTO", depSubNoAccInfoTO);
                }
                HashMap renewalNewUpdate = new HashMap();
                if (depIntMap.containsKey("RENEWAL_NOTICE")) {
                    renewalNewUpdate.put("MAT_ALERT_REPORT", depIntMap.get("RENEWAL_NOTICE"));
                }
                if (depIntMap.containsKey("AUTO_RENEWAL")) {
                    renewalNewUpdate.put("AUTO_RENEWAL", depIntMap.get("AUTO_RENEWAL"));
                }
                if (depIntMap.containsKey("AUTO_RENEW_WITH")) {
                    renewalNewUpdate.put("RENEW_WITH_INT", depIntMap.get("AUTO_RENEW_WITH"));
                }
                renewalNewUpdate.put("DEPOSIT_NO", renewalDetails.get("DEPOSIT_NO"));
                System.out.println("renewalNewUpdate bbbbbbbbbbbbbbbbbbbbbbb: " + renewalNewUpdate);
                sqlMap.executeUpdate("updateRenewalStatusBefAuth", renewalNewUpdate);
                HashMap renewalSameNoTableMap = new HashMap();
                renewalSameNoTableMap.put("DEPOSIT_NO", depIntMap.get("DEPOSIT_NO"));
                renewalSameNoTableMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(depIntMap.get("SB_INT_AMT")));
                renewalSameNoTableMap.put("INTEREST_AMT", CommonUtil.convertObjToDouble(depIntMap.get("BALANCE_INT_AMT")));
                renewalSameNoTableMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(depIntMap.get("SB_PERIOD_RUN")));
                sqlMap.executeUpdate("updateSBInterestSameNoTable", renewalSameNoTableMap);
                renewalSameNoTableMap.put("ACT_NUM", depIntMap.get("DEPOSIT_NO"));
                renewalSameNoTableMap.put("RENEWED_DT", currDt.clone());
                //sqlMap.executeUpdate("updateRenewalDateinSubacinfo", renewalSameNoTableMap);//Ref. by Sathya commented by kannan Ar
                renewalDetails = null;
            } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_EXTENSION)) {
                DepSubNoAccInfoTO depSubNoAccInfoTOSame = depSubNoAccInfoTO;
                depSubNoAccInfoTOSame.setDepositNo(objTO.getDepositNo());
                depSubNoAccInfoTOSame.setSubstatusBy(objLogTO.getUserId());
                depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                HashMap statusMap = new HashMap();
                statusMap.put(DEPOSITNO, extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", statusMap);
                if (lst != null && lst.size() > 0) {
                    statusMap = (HashMap) lst.get(0);
                    extensionDepIntMap.put("ORIGINAL_DEP_AMT", CommonUtil.convertObjToStr(statusMap.get("TOTAL_BALANCE")));
                    depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_NO")));
                    depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(statusMap.get("DEPOSIT_SUB_NO")));
                    depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("DEPOSIT_DT"))));
                    depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_DD")));
                    depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_MM")));
                    depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_PERIOD_YY")));
                    depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(statusMap.get("DEPOSIT_AMT")));
                    depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(statusMap.get("INTPAY_MODE")));
                    depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(statusMap.get("INTPAY_FREQ")));
                    depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("MATURITY_DT"))));
                    depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(statusMap.get("RATE_OF_INT")));
                    depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(statusMap.get("MATURITY_AMT")));
                    depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(statusMap.get("TOT_INT_AMT")));
                    depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(statusMap.get("PERIODIC_INT_AMT")));
                    depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(statusMap.get("STATUS")));
                    depSubNoAccInfoTO.setClearBalance(CommonUtil.convertObjToDouble(statusMap.get("CLEAR_BALANCE")));
                    depSubNoAccInfoTO.setAvailableBalance(CommonUtil.convertObjToDouble(statusMap.get("AVAILABLE_BALANCE")));
                    depSubNoAccInfoTO.setCloseDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setCloseBy(objTO.getCreatedBy());
                    depSubNoAccInfoTO.setSubstatusBy(objTO.getCreatedBy());
                    depSubNoAccInfoTO.setSubstatusDt((Date)currDt.clone());
                    depSubNoAccInfoTO.setAuthorizeStatus("");
                    depSubNoAccInfoTO.setPenalInt("");
                    depSubNoAccInfoTO.setAcctStatus(CommonConstants.CLOSED);
                    depSubNoAccInfoTO.setLastTransDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_TRANS_DT"))));
                    depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_INT_APPL_DT"))));
                    depSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_CREDIT")));
                    depSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_INT_DRAWN")));
                    depSubNoAccInfoTO.setTotalBalance(CommonUtil.convertObjToDouble(statusMap.get("TOTAL_BALANCE")));
                    depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(statusMap.get("INSTALL_TYPE")));
                    depSubNoAccInfoTO.setPaymentDay((Date)currDt.clone());
                    depSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(statusMap.get("CALENDER_DAY")));
                    depSubNoAccInfoTO.setFlexi_status("EX");
                    depSubNoAccInfoTO.setRenewedDt(getProperDateFormat(statusMap.get("RENEWED_DT")));//31-05-2019
                    sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
                    //                    String newNo = objTO.getDepositNo();
                    //                    String oldNo = CommonUtil.convertObjToStr(extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                    //                    double count = CommonUtil.convertObjToDouble(objTO.getRenewalCount()).doubleValue();
                    HashMap slNoMap = new HashMap();
                    HashMap extensionMap = new HashMap();
                    slNoMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                    extensionMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                    lst = sqlMap.executeQueryForList("getSelectMaxSLNo", extensionMap);
                    if (lst != null && lst.size() > 0) {
                        extensionMap = (HashMap) lst.get(0);
                        double maxNo = CommonUtil.convertObjToDouble(extensionMap.get("MAX_NO")).doubleValue();
                        extensionMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                        extensionMap.put("COUNT", new Double(maxNo + 1));
                        sqlMap.executeUpdate("updateMaxSLNo", extensionMap);
                    } else {
                        extensionMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                        extensionMap.put("COUNT", new Double(1));
                        sqlMap.executeUpdate("updateMaxSLNo", extensionMap);
                    }
                    extensionMap = null;
                    if (depSubNoAccInfoTO.getIntpayMode().equals("TRANSFER")) {
                        if (statusMap.get("INT_PAY_PROD_TYPE").equals("GL") || statusMap.get("INT_PAY_PROD_TYPE").equals("RM")) {
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTONoProdIdSameNo", slNoMap);
                        } else {
                            slNoMap.put("INT_PAY_ACC_NO", statusMap.get("INT_PAY_ACC_NO"));
                            slNoMap.put("INT_PAY_PROD_ID", statusMap.get("INT_PAY_PROD_ID"));
                            slNoMap.put("INT_PAY_PROD_TYPE", statusMap.get("INT_PAY_PROD_TYPE"));
                            sqlMap.executeUpdate("updateDepSubNoAccInfoTOSameNo", slNoMap);
                        }
                    }
                    slNoMap = null;
                }
                statusMap = null;
                sqlMap.executeUpdate("updateExtensionFlexiStatus", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                depSubNoAccInfoTO = depSubNoAccInfoTOSame;
                sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                HashMap intMap = new HashMap();
                intMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", intMap);
                if (lst != null && lst.size() > 0) {
                    intMap = (HashMap) lst.get(0);
                    intMap.put("ACT_NUM", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                    intMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                    intMap.put("CURR_RATE_OF_INT", CommonUtil.convertObjToDouble(intMap.get("CURR_RATE_OF_INT")));
                    intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")));
                    intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")));
                    intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(intMap.get("INTEREST_AMT")));
                    intMap.put("INT", CommonUtil.convertObjToDouble(intMap.get("PENAL_RATE")));
                    sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                }
                intMap = null;
                depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_DEPOSIT_DT"))));
                depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_DEPOSIT_DAYS")));
                depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_DEPOSIT_MONTHS")));
                depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_DEPOSIT_YEARS")));
                depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_DEPOSIT_AMT")));
                depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_MODE")));
                depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_PAY_FREQ")));
                depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_MATURITY_DT"))));
                depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_RATE_OF_INT")));
                depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_MATURITY_AMT")));
                depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_TOT_INTAMT")));
                depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_PERIODIC_INT")));
                depSubNoAccInfoTO.setSubstatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                depSubNoAccInfoTO.setSubstatusDt((Date)currDt.clone());
                depSubNoAccInfoTO.setFlexi_status("EX");
                depSubNoAccInfoTO.setTotalIntCredit(new Double(0));
                depSubNoAccInfoTO.setTotalIntDrawn(new Double(0));
                depSubNoAccInfoTO.setPaymentDay((Date)currDt.clone());
                oldbehavesMap = new HashMap();
                oldbehavesMap.put("PROD_ID", extensionDepIntMap.get("OLD_PROD_ID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
                if (lst != null && lst.size() > 0) {
                    oldbehavesMap = (HashMap) lst.get(0);
                }
                String oldBehaves = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE"));
                oldbehavesMap = null;
                HashMap newbehavesMap = new HashMap();
                newbehavesMap.put("PROD_ID", extensionDepIntMap.get("EXTENSION_PRODID"));
                lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", newbehavesMap);
                if (lst != null && lst.size() > 0) {
                    newbehavesMap = (HashMap) lst.get(0);
                }
                String newBehaves = CommonUtil.convertObjToStr(newbehavesMap.get("BEHAVES_LIKE"));
                newbehavesMap = null;
                if (newBehaves.equals("CUMMULATIVE")) {
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                    depSubNoAccInfoTO.setIntpayMode("");
                    depSubNoAccInfoTO.setIntpayFreq(new Double(0));
                }
                System.out.println("extensionDepIntMap : " + extensionDepIntMap);
                HashMap extensionDetails = new HashMap();
                extensionDetails.put("EXTENSION_DEPOSIT_NO", objTO.getDepositNo());
                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_DEPOSIT_DT")));
                Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_MATURITY_DT")));
                extensionDetails.put("EXTENSION_DEPOSIT_DT", depDt);
                extensionDetails.put("EXTENSION_MATURITY_DT", matDt);
                extensionDetails.put("EXTENSION_DEPOSIT_AMT", extensionDepIntMap.get("EXTENSION_DEPOSIT_AMT"));
                extensionDetails.put("EXTENSION_MATURITY_AMT", extensionDepIntMap.get("EXTENSION_MATURITY_AMT"));
                extensionDetails.put("EXTENSION_DEPOSIT_DAYS", extensionDepIntMap.get("EXTENSION_DEPOSIT_DAYS"));
                extensionDetails.put("EXTENSION_DEPOSIT_MONTHS", extensionDepIntMap.get("EXTENSION_DEPOSIT_MONTHS"));
                extensionDetails.put("EXTENSION_DEPOSIT_YEARS", extensionDepIntMap.get("EXTENSION_DEPOSIT_YEARS"));
                extensionDetails.put("EXTENSION_RATE_OF_INT", extensionDepIntMap.get("EXTENSION_RATE_OF_INT"));
                extensionDetails.put("EXTENSION_PERIODIC_INT", extensionDepIntMap.get("EXTENSION_PERIODIC_INT"));
                extensionDetails.put("EXTENSION_TOT_INTAMT", extensionDepIntMap.get("EXTENSION_TOT_INTAMT"));
                extensionDetails.put("EXTENSION_STATUS", CommonConstants.STATUS_CREATED);
                if ((extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("YES"))
                        || (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("YES"))) {
                    if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("CASH")) {
                        extensionDetails.put("EXTENSION_TOKEN_NO", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_TOKEN_NO")));
                        extensionDetails.put("EXTENSION_TRANS_PRODTYPE", String.valueOf(""));
                        extensionDetails.put("EXTENSION_TRANS_PRODID", String.valueOf(""));
                        extensionDetails.put("EXTENSION_TRANS_ACCNO", String.valueOf(""));
                    } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("TRANSFER")) {
                        extensionDetails.put("EXTENSION_TOKEN_NO", String.valueOf(""));
                        extensionDetails.put("EXTENSION_TRANS_PRODTYPE", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE")));
                        extensionDetails.put("EXTENSION_TRANS_PRODID", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_TRANS_PRODID")));
                        extensionDetails.put("EXTENSION_TRANS_ACCNO", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_TRANS_ACCNO")));
                    }
                    extensionDetails.put("EXTENSION_TRANS_MODE", extensionDepIntMap.get("EXTENSION_TRANS_MODE"));
                    double withdrawDepAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("WITDRAWING_AMT")).doubleValue();
                    extensionDetails.put("EXTENSION_WITDRAWING_DEPAMT", String.valueOf(withdrawDepAmt));
                } else if ((extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("NO"))
                        || (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("NO"))) {
                    extensionDetails.put("EXTENSION_TRANS_MODE", String.valueOf(""));
                    extensionDetails.put("EXTENSION_WITDRAWING_DEPAMT", String.valueOf("0"));
                    extensionDetails.put("EXTENSION_TOKEN_NO", String.valueOf(""));
                    extensionDetails.put("EXTENSION_TRANS_PRODTYPE", String.valueOf(""));
                    extensionDetails.put("EXTENSION_TRANS_PRODID", String.valueOf(""));
                    extensionDetails.put("EXTENSION_TRANS_ACCNO", String.valueOf(""));
                }
                if (extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("YES")) {
                    extensionDetails.put("EXTENSION_DEP_WITHDRAWING", "Y");
                } else if (extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("NO")) {
                    extensionDetails.put("EXTENSION_DEP_WITHDRAWING", "N");
                }
                if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("YES")) {
                    extensionDetails.put("EXTENSION_INT_WITHDRAWING", "Y");
                } else if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("NO")) {
                    extensionDetails.put("EXTENSION_INT_WITHDRAWING", "N");
                }
                if (extensionDepIntMap.containsKey("EXTENSION_ADDING") && extensionDepIntMap.get("EXTENSION_ADDING").equals("YES")) {
                    double intAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("WITHDRAWING_INT_AMT")).doubleValue();
                    extensionDetails.put("EXTENSION_WITDRAWING_INTAMT", String.valueOf(intAmt));
                    extensionDetails.put("EXTENSION_PENALTY", "Y");
                } else if (extensionDepIntMap.containsKey("EXTENSION_ADDING") && extensionDepIntMap.get("EXTENSION_ADDING").equals("NO")) {
                    extensionDetails.put("EXTENSION_PENALTY", "N");
                    extensionDetails.put("EXTENSION_WITDRAWING_INTAMT", String.valueOf("0"));
                }
                if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("YES")) {
                    double intAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("WITHDRAWING_INT_AMT")).doubleValue();
                    extensionDetails.put("EXTENSION_WITDRAWING_INTAMT", String.valueOf(intAmt));
                    extensionDetails.put("EXTENSION_INT_WITHDRAWING", "Y");
                } else if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("NO")) {
                    extensionDetails.put("EXTENSION_INT_WITHDRAWING", "N");
                    extensionDetails.put("EXTENSION_WITDRAWING_INTAMT", String.valueOf("0"));
                }
                if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("CASH")) {
                    extensionDetails.put("EXTENSION_PAY_MODE", "CASH");
                    extensionDetails.put("EXTENSION_PAY_PRODTYPE", String.valueOf(""));
                    extensionDetails.put("EXTENSION_PAY_PRODID", String.valueOf(""));
                    extensionDetails.put("EXTENSION_PAY_ACCNO", String.valueOf(""));
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                } else if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("TRANSFER")) {
                    extensionDetails.put("EXTENSION_PAY_MODE", "TRANSFER");
                    extensionDetails.put("EXTENSION_PAY_PRODTYPE", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_PRODTYPE")));
                    extensionDetails.put("EXTENSION_PAY_PRODID", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_PRODID")));
                    extensionDetails.put("EXTENSION_PAY_ACCNO", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_ACCNO")));
                    depSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_PRODTYPE")));
                    depSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_PRODID")));
                    depSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PAY_ACCNO")));
                } else if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("")) {
                    extensionDetails.put("EXTENSION_PAY_MODE", String.valueOf(""));
                    extensionDetails.put("EXTENSION_PAY_PRODTYPE", String.valueOf(""));
                    extensionDetails.put("EXTENSION_PAY_PRODID", String.valueOf(""));
                    extensionDetails.put("EXTENSION_PAY_ACCNO", String.valueOf(""));
                    depSubNoAccInfoTO.setIntPayProdType("");
                    depSubNoAccInfoTO.setIntPayProdId("");
                    depSubNoAccInfoTO.setIntPayAcNo("");
                }
                if (extensionDepIntMap.containsKey("FLD_DEP_EXTENSION_SUB_PRINTINGNO") && extensionDepIntMap.get("FLD_DEP_EXTENSION_SUB_PRINTINGNO").equals("")) {
                    extensionDetails.put("EXTENSION_PRINTING_NO", String.valueOf(""));
                } else {
                    extensionDetails.put("EXTENSION_PRINTING_NO", extensionDepIntMap.get("EXTENSION_PRINTINGNO"));
                }
                //                extensionDetails.put("EXTENSION_PENALTY",extensionDepIntMap.get("EXTENSION_DEP_ADDING"));
                extensionDetails.put("WITHDRAW_AMT_CALC", extensionDepIntMap.get("WITHDRAW_AMT_CALC"));
                extensionDetails.put("BALANCE_AMT_CALC", extensionDepIntMap.get("BALANCE_AMT_CALC"));
                extensionDetails.put("BALANCE_INTEREST_AMT", extensionDepIntMap.get("BALANCE_INT_AMT"));
                extensionDetails.put("EXTENSION_PRODID", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PRODID")));
                extensionDetails.put("EXTENSION_CATEGORY", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_CATEGORY")));
                extensionDetails.put("EXTENSION_PAY_FREQ", extensionDepIntMap.get("EXTENSION_PAY_FREQ"));
                extensionDetails.put("BRANCH_CODE", CommonUtil.convertObjToStr(extensionDepIntMap.get("BRANCH_CODE")));
                extensionDetails.put("EXTENSION_CALENDER_FREQ", CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_CALENDER_FREQ")));
                extensionDetails.put("EXTENSION_CALENDER_DAY", extensionDepIntMap.get("EXTENSION_CALENDER_FREQ_DAY"));
                extensionDetails.put("OLD_FREQUENCY", extensionDepIntMap.get("OLD_FREQUENCY"));
                extensionDetails.put("OLD_PERIOD_RUN", extensionDepIntMap.get("OLD_PERIOD_RUN"));
                extensionDetails.put("OLD_RATE_OF_INT", extensionDepIntMap.get("OLD_RATE_OF_INT"));
                extensionDetails.put("RECALCUALTE_INTEREST_AMT", extensionDepIntMap.get("ORIGINAL_INT_AMT"));
                HashMap extensionMap = new HashMap();
                extensionMap.put("OLD_DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                lst = sqlMap.executeQueryForList("getSelectMaxTempExtensionSLNo", extensionMap);
                if (lst != null && lst.size() > 0) {
                    extensionMap = (HashMap) lst.get(0);
                    double maxNo = CommonUtil.convertObjToDouble(extensionMap.get("MAX_NO")).doubleValue();
                    extensionDetails.put("SL_NO", new Double(maxNo + 1));
                } else {
                    extensionDetails.put("SL_NO", new Double(1));
                }
                extensionDetails.put("OLD_DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                System.out.println("extensionDetails : " + extensionDetails);
                System.out.println("before inserting extensionDepIntMap : " + extensionDepIntMap);
                sqlMap.executeUpdate("insertExtensionlDetails", extensionDetails);
                depSubNoAccInfoTO.setRenewedDt((Date)currDt.clone());//31-05-2019
                sqlMap.executeUpdate("insertDepSubNoAccInfoTO", depSubNoAccInfoTO);
                HashMap extensionUpdateMap = new HashMap();
                extensionUpdateMap.put("CLOSE_DT", currDt.clone());
                extensionUpdateMap.put("CLOSE_BY", extensionDepIntMap.get("USER_ID"));
                extensionUpdateMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                extensionUpdateMap.put("DEPOSIT_NO", extensionDepIntMap.get("OLD_DEPOSIT_NO"));
                sqlMap.executeUpdate("updateExtensionStatusSubAcinfo", extensionUpdateMap);
                sqlMap.executeUpdate("updateExtensionStatusAcinfo", extensionUpdateMap);
                extensionUpdateMap = null;
                extensionDetails = null;
            }
        }
        }
        catch(Exception e)
        {
        e.printStackTrace();
        sqlMap.rollbackTransaction();
        throw e;
        }
    }

    private void addToCustomerHistory(JointAccntTO jointAccntTO) throws Exception {
        // Inserting into Customer History Table
        try
        {
        objCustomerHistoryTO = new CustomerHistoryTO();
        objCustomerHistoryTO.setAcctNo(objTO.getDepositNo());
        objCustomerHistoryTO.setCustId(jointAccntTO.getCustId());
        objCustomerHistoryTO.setProductType(TransactionFactory.DEPOSITS);
        objCustomerHistoryTO.setProdId(objTO.getProdId());
        objCustomerHistoryTO.setRelationship(AcctStatusConstants.JOINT);
        objCustomerHistoryTO.setFromDt((Date)currDt.clone());
        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
        objCustomerHistoryTO = null;
        }
        catch(Exception e)
        {
        sqlMap.rollbackTransaction();
        e.printStackTrace();
        throw e;
        }
        
    }

    //--- Inserts the data Record by record in JointAccntTO Table
    private void insertJointAccntDetails() throws Exception {
        JointAccntTO jointAccntTO;
        int jointAccntSize = mapJointAccntTO.size();

        for (int i = 0; i < jointAccntSize; i++) {
            jointAccntTO = (JointAccntTO) mapJointAccntTO.get(String.valueOf(i));
            jointAccntTO.setDepositNo(objTO.getDepositNo());
            sqlMap.executeUpdate("insertJointAccntTO", jointAccntTO);

            addToCustomerHistory(jointAccntTO);

            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        }
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO, HashMap transDetailMap) throws Exception {
        //        try {
        //--- If "New" is selected in the DEPOSITNO combobox,
        //--- then generate the new DEPOSIT NO
        //if (objTO.getDepositNo() == null) {
        try{
        String cash_unique_id = getCashUniqueId();
        extensionDepIntMap = new HashMap();
        extensionDepIntMap = (HashMap) map.get("EXTENSIONMAP");
        depIntMap = new HashMap();
        depIntMap = (HashMap) map.get("RENEWALMAP");
        HashMap intMap = (HashMap) map.get("INTMAP");
        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
            renewalProd = true;
            if (map.containsKey("SAME_DEPOSIT_NO")) {
                objTO.setDepositNo(CommonUtil.convertObjToStr(intMap.get("ACT_NUM")));
                returnDepositNo = CommonUtil.convertObjToStr(intMap.get("ACT_NUM"));
            } else {
                getDeposit_No();
            }
        } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_EXTENSION)) {
            extensionProd = true;
            getDeposit_No();
        } else {
            if(map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.get("FROM_MOBILE_APP_CUST_CREATION") != null && map.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION")){
                if(map.get("BEHAVES_LIKE").equals("DAILY") || map.get("BEHAVES_LIKE").equals("RECURRING")){
                    objTO.setReferenceNo(generateReferenceId(objTO));
                }
            }
            getDeposit_No();
        }
        if (intMap != null && intMap.size() > 0 && objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
            double count = 0.0;

            withorOut = CommonUtil.convertObjToStr(intMap.get("INT"));
            intAmt = CommonUtil.convertObjToDouble(intMap.get("BAL_INT_AMT")).doubleValue();
            intMap.put("USER_ID", (String) map.get(CommonConstants.USER_ID));
            intMap.put("CLOSE_DT", currDt.clone());
            intMap.put("BAL_INT_AMT", new Double(CommonUtil.convertObjToStr(intMap.get("BAL_INT_AMT"))));
            HashMap statusMap = new HashMap();
            statusMap.put("DEPOSITNO", intMap.get("ACT_NUM"));
            System.out.println("&&&&&&&TermDeposit Dao intMap :" + intMap);
            List lst = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", statusMap);
            if (lst != null && lst.size() > 0) {
                statusMap = (HashMap) lst.get(0);
                if(objTO.getCustId().length() == 0){
                    objTO.setCustId(CommonUtil.convertObjToStr(statusMap.get("CUST_ID")));
	                System.out.println("objTO.getCustId() : " + objTO.getCustId());
                }
                if (map.containsKey("SAME_DEPOSIT_NO")) {
                    intMap.put("ACCT_STATUS", "NEW");
                } else {
                    if (statusMap.get("ACCT_STATUS").equals("NEW")) {//||statusMap.get("ACCT_STATUS").equals("LIENED"))
                        intMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                    } else if (statusMap.get("ACCT_STATUS").equals("MATURED")) {
                        intMap.put("ACCT_STATUS", "MATURED");
                    }
                    intMap.put("ACT_NUM", statusMap.get("DEPOSIT_NO"));
                    intMap.put("CLOSE_DT", currDt.clone());
                    sqlMap.executeUpdate("updateDepositAcinfoCloseRenewal", intMap);
                }
//                sqlMap.executeUpdate("updateSbInterestAmount", intMap);
                //                HashMap penalMap = new HashMap();
                //                penalMap.put("INT",intMap.get("INT"));
                //		penalMap.put("ACT_NUM",intMap.get("ACT_NUM"));
                //                sqlMap.executeUpdate("updateSameDeposit", penalMap);
                if (map.containsKey("SAME_DEPOSIT_NO")) {
                    HashMap sameMap = new HashMap();
                    sameMap.put("ACT_NUM", statusMap.get("DEPOSIT_NO"));
                    sameMap.put("TOTAL_INT_DEBIT", new Double(1));
                    sameMap.put("RENEWAL_FROM_DEPOSIT", statusMap.get("DEPOSIT_NO"));
                    sameMap.put("STATUS_BY", map.get("USER_ID"));
                    if (objTO.getRenewalCount() == null) {
                        sameMap.put("RENEWAL_COUNT", new Double(1));
                    } else {
                        HashMap renewMap = new HashMap();
                        renewMap.put("DEPOSIT_NO", statusMap.get("DEPOSIT_NO"));
                        List lstRenewal = sqlMap.executeQueryForList("getCustIdForDeposits", renewMap);
                        if (lstRenewal != null && lstRenewal.size() > 0) {
                            renewMap = (HashMap) lstRenewal.get(0);
                            count = CommonUtil.convertObjToDouble(renewMap.get("RENEWAL_COUNT")).doubleValue();
                            count = count + 1;

                            sameMap.put("RENEWAL_COUNT", new Double(count));
                        }
                    }
                    List list = sqlMap.executeQueryForList("getCustIdForDeposits", statusMap);
                    if (list != null && list.size() > 0) {
                        HashMap dtMap = (HashMap) list.get(0);
                        createdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dtMap.get("CREATED_DT")));
                    }

                    sameMap.put("SETTLEMENT_MODE", objTO.getSettlementMode());
                    sameMap.put("CATEGORY", objTO.getCategory());
                    sameMap.put("CONSTITUTION", objTO.getConstitution());
                    sameMap.put("DEATH_CLAIM", objTO.getDeathClaim());
                    sameMap.put("AUTO_RENEWAL", objTO.getAutoRenewal());
                    sameMap.put("RENEW_WITH_INT", objTO.getRenewWithInt());
                    sameMap.put("MAT_ALERT_REPORT", objTO.getMatAlertRep());
                    sameMap.put("MEMBER", objTO.getMember());
                    sameMap.put("TRANS_OUT", objTO.getTransOut());
                    sameMap.put("PRINTING_NO", objTO.getPrintingNo());
                    sqlMap.executeUpdate("updateSameDepositAcinfo", sameMap);
                }
            }
            System.out.println("&&&&&&&TermDeposit Dao intMap :" + intMap);
            if (depIntMap != null && !depIntMap.isEmpty()) {
                System.out.println("&&&&&&&TermDeposit Dao depIntMap :" + depIntMap);
                depIntMap.put("OLD_PROD_ID", objTO.getProdId());
                objTO.setProdId(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PRODID")));
                objTO.setCategory(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CATEGORY")));
                objTO.setPrintingNo(CommonUtil.convertObjToInt(depIntMap.get("FLD_DEP_RENEWAL_SUB_PRINTINGNO")));
            }
            if (map.containsKey("SAME_DEPOSIT_NO")) {
                objTO.setRenewalFromDeposit(CommonUtil.convertObjToStr(returnDepositNo));
                objTO.setRenewalCount(new Double(count));
            }else{
                //Added by nithya on 14-12-2022  for KD-3524            
                if (!(objTO.getRenewalFromDeposit().equalsIgnoreCase(objTO.getDepositNo()))) {
                  objTO.setRenewalCount(new Double(0.0));
                }
            }
            sqlMap.executeUpdate("updateAccInfoTO", objTO);
            HashMap accountMap = new HashMap();
            accountMap.put("SETTLEMENT_MODE", objTO.getSettlementMode());
            accountMap.put("CONSTITUTION", objTO.getConstitution());
            accountMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
            sqlMap.executeUpdate("updateSettlementMode", accountMap);
        }
        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_EXTENSION) && extensionDepIntMap != null && !extensionDepIntMap.isEmpty()) {
            System.out.println("extensionDepIntMap :" + extensionDepIntMap);
            extensionDepIntMap.put("OLD_PROD_ID", objTO.getProdId());
            objTO.setProdId(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PRODID")));
            objTO.setCategory(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_CATEGORY")));
            HashMap accountMap = new HashMap();
            accountMap.put("SETTLEMENT_MODE", objTO.getSettlementMode());
            accountMap.put("CONSTITUTION", objTO.getConstitution());
            accountMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
            sqlMap.executeUpdate("updateSettlementMode", accountMap);
        }
        System.out.println("insert data:" + map + "insert objLogDAO:" + objLogDAO + "insert objLogTO:" + objLogTO + "insert intMap:" + intMap);
        // Inserting into Customer History Table

        CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
        objCustomerHistoryTO.setAcctNo(objTO.getDepositNo());
        objCustomerHistoryTO.setCustId(objTO.getCustId());
        objCustomerHistoryTO.setProductType(TransactionFactory.DEPOSITS);
        objCustomerHistoryTO.setProdId(objTO.getProdId());
        objCustomerHistoryTO.setRelationship(AcctStatusConstants.ACCT_HOLDER);
        objCustomerHistoryTO.setFromDt((Date)currDt.clone());
        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
        objCustomerHistoryTO = null;

        objTO.setStatus(CommonConstants.STATUS_CREATED);
        if(objTO != null && CommonUtil.convertObjToStr(objTO.getCommand()) != null && CommonUtil.convertObjToStr(objTO.getCommand()).length()>0 && 
        CommonUtil.convertObjToStr(objTO.getCommand()).equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setBranchId(_branchCode);
            objTO.setInitiatedBranch(_branchCode);
            objSMSSubscriptionTO = null;            
        }
        if (map.containsKey("SMSSubscriptionTO")) {
            objSMSSubscriptionTO = (SMSSubscriptionTO) map.get("SMSSubscriptionTO");
            if (objSMSSubscriptionTO != null) {
                updateSMSSubscription(objTO);
            }
        }
        sameNo = false;
        if (map.containsKey("SAME_DEPOSIT_NO")) {
            sameNo = true;
        } else {
            if(CommonUtil.convertObjToStr(objTO.getMdsGroup()).equals("")){
                objTO.setMdsGroup(null);
            }
            sqlMap.executeUpdate("insertAccInfoTO", objTO);
        }
        objLogTO.setData(objTO.toString());
        objLogTO.setPrimaryKey(objTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
        System.out.println("insert " + objTO);
        insertDepSubAccInfoDetails(map, transDetailMap, cash_unique_id);

        //--- Inserts the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
        //Modified by sreekrishnan
        if (objTO.getConstitution().equals("JOINT_ACCOUNT") && objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            if(mapJointAccntTO!=null && mapJointAccntTO.size()>0){
                insertJointAccntDetails();
            }
        }
        
        if (objTO.getConstitution().equals("JOINT_ACCOUNT") && objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {            
            if(mapJointAccntTO!=null && mapJointAccntTO.size()>0){
                updateJointAccountDetails();
            }
        }
        
        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
            depositRenewalTransfer(depIntMap);
            intMap.put("CLOSE_DT",null);
            intMap.put("USER_ID","");
            sqlMap.executeUpdate("updateSbInterestAmount", intMap);
            //Added by sreekrishnan for 0004149(Update multiple deposit id null on renewal)
            sqlMap.executeUpdate("updateDepositMultipleId", depIntMap);
        }
        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_EXTENSION)) {
            depositExtensionTransfer(extensionDepIntMap);
        }
        //--- Inserts the Opening mode data into database if Opening mode is TransferIn
        if (objTO.getOpeningMode().equals("TransferIn")) {
            transferInTO.setDepositNo(objTO.getDepositNo());
            sqlMap.executeUpdate("insertTransferInTO", transferInTO);
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        }

        //--- Inserts the Authorized Signatory data into database if Authorized Signatory is Checked.
        if (objTO.getAuthorizedSignatory().equals("Y")) {
            System.out.println("AuthorizeSignatory...");
            List lstNominee = sqlMap.executeQueryForList("selectalreadyExistingSignatory", objTO.getDepositNo());
            if (lstNominee != null && lstNominee.size() > 0) {
                System.out.println("Already Exist AuthorizeSignatory...");
            } else {
                objAuthorizedSignatoryDAO.setBorrower_No(objTO.getDepositNo());
                objAuthorizedSignatoryDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);
            }
        }

        //--- Inserts the PoA data into database if PoA is checked
        if (objTO.getPoa().equals("Y")) {
            List lstNominee = sqlMap.executeQueryForList("selectalreadyExistingPOA", objTO.getDepositNo());
            if (lstNominee != null && lstNominee.size() > 0) {
                System.out.println("Already Exist POA...");
            } else {
                objPowerOfAttorneyDAO.setBorrower_No(objTO.getDepositNo());
                objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);
            }
        }

        //--- Inserts the NomineeDetails data into database if NomineeDetails is checked
        if (objTO.getNomineeDetails().equals("Y")) {
            ArrayList nomineeTOList = new ArrayList();
            if (map.containsKey("AccountNomineeTO")) {
                nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            }
            ArrayList nomineeDeleteList = new ArrayList();
            if (map.containsKey("AccountNomineeDeleteTO")) {
                nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
            }
            if (nomineeTOList != null && nomineeTOList.size() > 0) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
                    HashMap nomineeMap = new HashMap();
                    nomineeMap.put("DEPOSIT_NO", objTO.getRenewalFromDeposit());
                    nomineeMap.put("STATUS", "EXISTING");
                    nomineeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    nomineeMap.put("CURR_DATE", currDt.clone());
                    sqlMap.executeUpdate("StatusUpdationinTD", nomineeMap);
                    nomineeMap = null;
                    // Added by nithya on 17-09-2018 for KD 240 - Nominee details updation issue in deposit renewal screen  
                    HashMap statusMap = new HashMap();
                    statusMap.put("DEPOSIT_NO",objTO.getRenewalFromDeposit());
                    sqlMap.executeUpdate("UpdateCurrentNomineeStatusTD", statusMap);
                    statusMap = null;
                    // End KD 240 - Nominee details updation issue in deposit renewal screen  
                }                
                NomineeDAO objNomineeDAO = new NomineeDAO();
                System.out.println("nomineeTOList List " + nomineeTOList);
                System.out.println("nomineeDeleteList List " + nomineeDeleteList);
                //                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
                objNomineeDAO.insertData(nomineeTOList, objTO.getDepositNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
                if (nomineeDeleteList != null) {
                    objNomineeDAO.insertData(nomineeDeleteList, objTO.getDepositNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
                }
            }
            //            List lstNominee = sqlMap.executeQueryForList("selectalreadyExistingNominee",objTO.getDepositNo());
            //            if(lstNominee != null && lstNominee.size()>0){
            //                System.out.println("Already Exist Nominee...");
            //            }else{
            //                NomineeDAO objNomineeDAO = new NomineeDAO();
            //                ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            //                objNomineeDAO.insertData(nomineeTOList, objTO.getDepositNo(), false, "", SCREEN, objLogTO, objLogDAO);
            //                objNomineeDAO = null;
            //                nomineeTOList=null;
            //            }
            
            
        }        
        //  Added by nithya on 08-03.2016 for 0003920
        if(objThriftBenevolentAdditionalAmtTO != null){
            objThriftBenevolentAdditionalAmtTO.setDepositNo(objTO.getDepositNo());
            sqlMap.executeUpdate("insertThriftBenevolentAdditionalAmountTO", objThriftBenevolentAdditionalAmtTO);
            HashMap updateAdtAmtMap = new HashMap();
            updateAdtAmtMap.put("DEPOSIT_NO",objThriftBenevolentAdditionalAmtTO.getDepositNo());
            updateAdtAmtMap.put("ADT_AMT",objThriftBenevolentAdditionalAmtTO.getAdditionalAmount());
            sqlMap.executeUpdate("updateThriftBenevolentAdditionalAmnt", updateAdtAmtMap);
            // updating ADT_AMT in DEPOSIT_SUB_ACINFO
            objLogTO.setData(objThriftBenevolentAdditionalAmtTO.toString());
            objLogTO.setPrimaryKey(objThriftBenevolentAdditionalAmtTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            objThriftBenevolentAdditionalAmtTO = null; // Added by nithya
            updateAdtAmtMap = null;
        }
        // End
        }
        catch(Exception e)
        {
        sqlMap.rollbackTransaction();
        e.printStackTrace();
        throw  e;
        }
    }

    private void getDeposit_No() throws Exception {
        //        final IDGenerateDAO dao = new IDGenerateDAO();
        //        final HashMap where = new HashMap();
        //        where.put(CommonConstants.MAP_WHERE, "DEPOSIT_NO");
        //        String DepNo =  (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        //        objTO.setDepositNo(DepNo);
        //
        //        //        //--- sets the depositNo if Authorised Signatory is checked
        //        //        if(objTO.getAuthorizedSignatory().equals("Y")){
        //        //        objAuthorizedSignatoryDAO.setBorrower_No(DepNo);
        //        //        }
        //        //        //--- sets the depositNo if PoA is checked
        //        //        if(objTO.getPoa().equals("Y")){
        //        //	objPowerOfAttorneyDAO.setBorrower_No(DepNo);
        //        //        }
        //
        //        //--- sets the depositNo if TransferIn is checked
        //        if(objTO.getOpeningMode().equals("TransferIn")){
        //            transferInTO.setDepositNo(DepNo);
        //
        HashMap where = new HashMap();
        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        if (renewalProd == true && depIntMap != null && !depIntMap.isEmpty() && depIntMap.containsKey("RENEWAL_PRODID")) {
            where.put("PROD_ID", depIntMap.get("RENEWAL_PRODID"));
        } else if (extensionProd == true && extensionDepIntMap != null && !extensionDepIntMap.isEmpty()
                && extensionDepIntMap.containsKey("EXTENSION_PRODID")) {
            where.put("PROD_ID", extensionDepIntMap.get("EXTENSION_PRODID"));
        } else {
            where.put("PROD_ID", objTO.getProdId());
            if(isGroupDepositAcct(where)){ // Added by nithya for group deposit changes
                System.out.println("Group Deposit Product");
                where.put("GROUP_NO", objTO.getDepositGroup());
            }
        }
        String genID = null;
        int len = 13;
        //Modified by sreekrishnan
        where.put(CommonConstants.BRANCH_ID, interBranch);        
        List lst = (List) sqlMap.executeQueryForList("getCoreBankNextActNum", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));            
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);            
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            //        sqlMap.executeUpdate("updateNextId", where);   
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        } else {
            len = 10;
            lst = (List) sqlMap.executeQueryForList("getIdForProd", where);
            if (lst != null && lst.size() > 0) {
                mapData = (HashMap) lst.get(0);
            }
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("SUFFIX"))));
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("SUFFIX"))) + 1);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            sqlMap.executeUpdate("updateNxtDpNum", where);
        }
        objTO.setDepositNo(genID);
        returnDepositNo = genID;
        renewalProd = false;
        extensionProd = false;
    }

    private void updateJointAccountDetails() throws Exception {
        try{
        System.out.println("updateJointAccountDetails");
        JointAccntTO jointAccntTO;
        HashMap depNo = new HashMap();
        depNo.put("DEPOSIT_NO", objTO.getDepositNo());
        List list = (List) sqlMap.executeQueryForList("getCountJointAccntHolders", depNo);
        System.out.println("updateJointAccountDetails");
        HashMap jntMapData = new HashMap();
        jntMapData.put("JointAccountHoldersData", list);
        HashMap JointAccntMap;
        JointAccntMap = (HashMap) ((List) jntMapData.get("JointAccountHoldersData")).get(0);
        int isMore = (int) Integer.parseInt(JointAccntMap.get("COUNT").toString());
        int jntAccntHolderSize = mapJointAccntTO.size();
        for (int i = 0; i < jntAccntHolderSize; i++) {
            //            try {
            jointAccntTO = (JointAccntTO) mapJointAccntTO.get(String.valueOf(i));
            jointAccntTO.setDepositNo(objTO.getDepositNo());
            if (i < isMore) { //--- If the data is exisiting , update the data
                sqlMap.executeUpdate("updateJointAccntTO", jointAccntTO);
                objLogTO.setData(jointAccntTO.toString());
                objLogTO.setPrimaryKey(jointAccntTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } else { //--- Else insert the data.
                sqlMap.executeUpdate("insertJointAccntTO", jointAccntTO);
                objLogTO.setData(jointAccntTO.toString());
                addToCustomerHistory(jointAccntTO);
                objLogTO.setPrimaryKey(jointAccntTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }

        }
        }
        catch(Exception e)
        {
        sqlMap.rollbackTransaction();
         e.printStackTrace();
         throw e;
        }
    }

    private void updateDepSubNoAccInfoDetails() throws Exception {
        try {
            DepSubNoAccInfoTO depSubNoAccInfoTO;
            HashMap depNoandDepSubNo = new HashMap();
            depNoandDepSubNo.put("DEPOSIT_NO", objTO.getDepositNo());
            List list = (List) sqlMap.executeQueryForList("getCountDepSubNo", depNoandDepSubNo);
            HashMap depMapData = new HashMap();
            depMapData.put("DepositSubNoData", list);
            HashMap DepositMap;
            DepositMap = (HashMap) ((List) depMapData.get("DepositSubNoData")).get(0);
            int isMore = (int) Integer.parseInt(DepositMap.get("COUNT").toString());
            int depSubNoAccInfoSize = mapDepSubNoAccInfoTO.size();
            for (int i = 0; i < depSubNoAccInfoSize; i++) {
                //            try {
                depSubNoAccInfoTO = (DepSubNoAccInfoTO) mapDepSubNoAccInfoTO.get(String.valueOf(i));
                depSubNoAccInfoTO.setDepositNo(objTO.getDepositNo());
                if (depIntMap != null && !depIntMap.isEmpty()) {
                    HashMap renewalUpdateMap = new HashMap();
                    System.out.println("depIntMap : " + depIntMap);
                    HashMap renewalDetails = new HashMap();
                    renewalDetails.put("DEPOSIT_NO", objTO.getDepositNo());
                    Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_DEPOSIT_DT")));
                    Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_MATURITY_DT")));
                    renewalDetails.put("RENEWAL_DEPOSIT_DT", depDt);
                    renewalDetails.put("RENEWAL_MATURITY_DT", matDt);
                    renewalDetails.put("RENEWAL_DEPOSIT_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_DEPOSIT_AMT")));
                    renewalDetails.put("RENEWAL_MATURITY_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_MATURITY_AMT")));
                    renewalDetails.put("RENEWAL_DEPOSIT_DAYS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_DAYS")));
                    renewalDetails.put("RENEWAL_DEPOSIT_MONTHS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_MONTHS")));
                    renewalDetails.put("RENEWAL_DEPOSIT_YEARS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_YEARS")));
                    renewalDetails.put("RENEWAL_RATE_OF_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_RATE_OF_INT")));
                    renewalDetails.put("RENEWAL_PERIODIC_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_PERIODIC_INT")));
                    renewalDetails.put("RENEWAL_TOT_INTAMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_TOT_INTAMT")));
                    renewalDetails.put("STATUS", CommonConstants.STATUS_CREATED);
                    renewalDetails.put("OLD_DEPOSIT_NO", objTO.getDepositNo());
                    if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("CASH")) {
                        renewalDetails.put("RENEWAL_PAY_MODE", "CASH");
                        renewalDetails.put("RENEWAL_PAY_PRODTYPE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_PAY_PRODID", String.valueOf(""));
                        renewalDetails.put("RENEWAL_PAY_ACCNO", String.valueOf(""));
                        renewalUpdateMap.put("PAY_PRODTYPE", String.valueOf(""));
                        renewalUpdateMap.put("PAY_PRODID", String.valueOf(""));
                        renewalUpdateMap.put("PAY_ACCNO", String.valueOf(""));
                    } else if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("TRANSFER")) {
                        renewalDetails.put("RENEWAL_PAY_MODE", "TRANSFER");
                        renewalDetails.put("RENEWAL_PAY_PRODTYPE", depIntMap.get("RENEWAL_PAY_PRODTYPE"));
                        renewalDetails.put("RENEWAL_PAY_PRODID", depIntMap.get("RENEWAL_PAY_PRODID"));
                        renewalDetails.put("RENEWAL_PAY_ACCNO", depIntMap.get("RENEWAL_PAY_ACCNO"));
                        renewalUpdateMap.put("PAY_PRODTYPE", depIntMap.get("RENEWAL_PAY_PRODTYPE"));
                        renewalUpdateMap.put("PAY_PRODID", depIntMap.get("RENEWAL_PAY_PRODID"));
                        renewalUpdateMap.put("PAY_ACCNO", depIntMap.get("RENEWAL_PAY_ACCNO"));
                    } else if (depIntMap.containsKey("RENEWAL_PAY_MODE") && depIntMap.get("RENEWAL_PAY_MODE").equals("")) {
                        renewalDetails.put("RENEWAL_PAY_MODE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_PAY_PRODTYPE", String.valueOf(""));
                        renewalDetails.put("RENEWAL_PAY_PRODID", String.valueOf(""));
                        renewalDetails.put("RENEWAL_PAY_ACCNO", String.valueOf(""));
                        renewalUpdateMap.put("PAY_PRODTYPE", String.valueOf(""));
                        renewalUpdateMap.put("PAY_PRODID", String.valueOf(""));
                        renewalUpdateMap.put("PAY_ACCNO", String.valueOf(""));
                    }
                    renewalDetails.put("RENEWAL_PRINTING_NO", depIntMap.get("FLD_DEP_RENEWAL_SUB_PRINTINGNO"));
                    renewalDetails.put("RENEWAL_PRODID", depIntMap.get("RENEWAL_PRODID"));
                    renewalDetails.put("RENEWAL_CATEGORY", depIntMap.get("RENEWAL_CATEGORY"));
                    renewalDetails.put("RENEWAL_PAY_FREQ", depIntMap.get("RENEWAL_PAY_FREQ"));
                    renewalDetails.put("BRANCH_CODE", depIntMap.get("BRANCH_CODE"));
                    renewalDetails.put("RENEWAL_CALENDER_FREQ", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CALENDER_FREQ")));
                    renewalDetails.put("RENEWAL_CALENDER_DAY", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                    renewalDetails.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEW_POSTAGE_AMT")));
                    System.out.println("renewalDetails : " + renewalDetails);
                    sqlMap.executeUpdate("updateRenewalDetails", renewalDetails);
                    renewalUpdateMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(depIntMap.get("DEPOSIT_NO")));
                    renewalUpdateMap.put("DEPOSIT_DT", depDt);
                    renewalUpdateMap.put("MATURITY_DT", matDt);
                    renewalUpdateMap.put("MATURITY_AMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_MATURITY_AMT")));
                    renewalUpdateMap.put("DEPOSIT_DAYS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_DAYS")));
                    renewalUpdateMap.put("DEPOSIT_MONTHS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_MONTHS")));
                    renewalUpdateMap.put("DEPOSIT_YEARS", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_DEPOSIT_YEARS")));
                    renewalUpdateMap.put("RATE_OF_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_RATE_OF_INT")));
                    renewalUpdateMap.put("PERIODIC_INT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_PERIODIC_INT")));
                    renewalUpdateMap.put("TOT_INTAMT", CommonUtil.convertObjToDouble(depIntMap.get("RENEWAL_TOT_INTAMT")));
                    renewalUpdateMap.put("CALENDER_FREQ", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_CALENDER_FREQ")));
                    renewalUpdateMap.put("CALENDER_DAY", CommonUtil.convertObjToInt(depIntMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                    renewalUpdateMap.put("PRINTING_NO", CommonUtil.convertObjToStr(depIntMap.get("FLD_DEP_RENEWAL_SUB_PRINTINGNO")));
                    renewalUpdateMap.put("INTPAY_MODE", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_PAY_MODE")));
                    if (depIntMap.containsKey("RENEWAL_PAY_FREQ") && depIntMap.get("RENEWAL_PAY_FREQ").equals("Date of Maturity")) {
                        //renewalUpdateMap.put("INTPAY_FREQ", String.valueOf("0"));
                        renewalUpdateMap.put("INTPAY_FREQ", 0);
                    } else if (depIntMap.containsKey("RENEWAL_PAY_FREQ") && depIntMap.get("RENEWAL_PAY_FREQ").equals("Monthly")) {
                        //renewalUpdateMap.put("INTPAY_FREQ", String.valueOf("30"));
                        renewalUpdateMap.put("INTPAY_FREQ", 30);
                    } else if (depIntMap.containsKey("RENEWAL_PAY_FREQ") && depIntMap.get("RENEWAL_PAY_FREQ").equals("Quaterly")) {
                        //renewalUpdateMap.put("INTPAY_FREQ", String.valueOf("90"));
                        renewalUpdateMap.put("INTPAY_FREQ", 90);
                    } else if (depIntMap.containsKey("RENEWAL_PAY_FREQ") && depIntMap.get("RENEWAL_PAY_FREQ").equals("Half Yearly")) {
                        //renewalUpdateMap.put("INTPAY_FREQ", String.valueOf("180"));
                        renewalUpdateMap.put("INTPAY_FREQ", 180);
                    } else if (depIntMap.containsKey("RENEWAL_PAY_FREQ") && depIntMap.get("RENEWAL_PAY_FREQ").equals("Yearly")) {
                        //renewalUpdateMap.put("INTPAY_FREQ", String.valueOf("360"));
                        renewalUpdateMap.put("INTPAY_FREQ", 360);
                    }
                    System.out.println("renewalUpdateMap : " + renewalUpdateMap);
                    sqlMap.executeUpdate("updateRenewalSubAcinfo", renewalUpdateMap);
                    HashMap renewalNewUpdate = new HashMap();
                    if (depIntMap.containsKey("RENEWAL_NOTICE")) {
                        renewalNewUpdate.put("MAT_ALERT_REPORT", CommonUtil.convertObjToStr(depIntMap.get("RENEWAL_NOTICE")));
                    }
                    if (depIntMap.containsKey("AUTO_RENEWAL")) {
                        renewalNewUpdate.put("AUTO_RENEWAL", CommonUtil.convertObjToStr(depIntMap.get("AUTO_RENEWAL")));
                    }
                    if (depIntMap.containsKey("AUTO_RENEW_WITH")) {
                        renewalNewUpdate.put("RENEW_WITH_INT", CommonUtil.convertObjToStr(depIntMap.get("AUTO_RENEW_WITH")));
                    }
                    renewalNewUpdate.put("DEPOSIT_NO", CommonUtil.convertObjToStr(depIntMap.get("DEPOSIT_NO")));
                    System.out.println("renewalNewUpdate : " + renewalNewUpdate);
                    sqlMap.executeUpdate("updateRenewalStatusBefAuth", renewalNewUpdate);
                    boolean flag = false;
                    if (depIntMap.get("RENEWAL_DEP_WITHDRAWING").equals("YES") && !depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && depIntMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                        HashMap payorderMap = new HashMap();
                        payorderMap.put("RENEWAL_DEP_TRANS_ACCNO", depIntMap.get("RENEWAL_DEP_TRANS_ACCNO"));
                        payorderMap.put("RENEWAL_DEP_TOKEN_NO", depIntMap.get("RENEWAL_DEP_TOKEN_NO"));
                        payorderMap.put("DEPOSIT_NO", depIntMap.get("OLD_DEPOSIT_NO"));
                        sqlMap.executeUpdate("updateRemitIssueForRenewal", payorderMap);
                        payorderMap.remove("DEPOSIT_NO");
                        payorderMap.put("DEPOSIT_NO", depIntMap.get("DEPOSIT_NO"));
                        sqlMap.executeUpdate("updateRemitIssueForRenewalTempdetails", payorderMap);
                        flag = true;
                    }
                    if (depIntMap.get("RENEWAL_INT_WITHDRAWING").equals("YES") && depIntMap.containsKey("RENEWAL_INT_TRANS_PRODTYPE")) {
                        if (!depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("") && depIntMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                            HashMap payorderMap = new HashMap();
                            payorderMap.put("RENEWAL_DEP_TRANS_ACCNO", depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                            payorderMap.put("RENEWAL_DEP_TOKEN_NO", depIntMap.get("RENEWAL_INT_TOKEN_NO"));
                            payorderMap.put("DEPOSIT_NO", depIntMap.get("OLD_DEPOSIT_NO"));
                            sqlMap.executeUpdate("updateRemitIssueForRenewal", payorderMap);
                            payorderMap = new HashMap();
                            payorderMap.put("RENEWAL_INT_TRANS_ACCNO", depIntMap.get("RENEWAL_INT_TRANS_ACCNO"));
                            payorderMap.put("RENEWAL_INT_TOKEN_NO", depIntMap.get("RENEWAL_INT_TOKEN_NO"));
                            payorderMap.put("DEPOSIT_NO", depIntMap.get("DEPOSIT_NO"));
                            sqlMap.executeUpdate("updateRemitIssueForRenewalTempdetailsInt", payorderMap);
                        }
                    }
                    renewalUpdateMap = null;
                    renewalDetails = null;
                    renewalNewUpdate = null;
                } else if (extensionDepIntMap != null && !extensionDepIntMap.isEmpty()) {
                    System.out.println("extensionDepIntMap : " + extensionDepIntMap);
                    HashMap extensionDetails = new HashMap();
                    extensionDetails.put("EXTENSION_DEPOSIT_NO", objTO.getDepositNo());
                    Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_DEPOSIT_DT")));
                    Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_MATURITY_DT")));
                    extensionDetails.put("EXTENSION_DEPOSIT_DT", depDt);
                    extensionDetails.put("EXTENSION_MATURITY_DT", matDt);
                    extensionDetails.put("EXTENSION_DEPOSIT_AMT", extensionDepIntMap.get("EXTENSION_DEPOSIT_AMT"));
                    extensionDetails.put("EXTENSION_MATURITY_AMT", extensionDepIntMap.get("EXTENSION_MATURITY_AMT"));
                    extensionDetails.put("EXTENSION_DEPOSIT_DAYS", extensionDepIntMap.get("EXTENSION_DEPOSIT_DAYS"));
                    extensionDetails.put("EXTENSION_DEPOSIT_MONTHS", extensionDepIntMap.get("EXTENSION_DEPOSIT_MONTHS"));
                    extensionDetails.put("EXTENSION_DEPOSIT_YEARS", extensionDepIntMap.get("EXTENSION_DEPOSIT_YEARS"));
                    extensionDetails.put("EXTENSION_RATE_OF_INT", extensionDepIntMap.get("EXTENSION_RATE_OF_INT"));
                    extensionDetails.put("EXTENSION_PERIODIC_INT", extensionDepIntMap.get("EXTENSION_PERIODIC_INT"));
                    extensionDetails.put("EXTENSION_TOT_INTAMT", extensionDepIntMap.get("EXTENSION_TOT_INTAMT"));
                    extensionDetails.put("STATUS", CommonConstants.STATUS_CREATED);
                    extensionDetails.put("OLD_DEPOSIT_NO", objTO.getDepositNo());
                    if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("CASH")) {
                        extensionDetails.put("EXTENSION_PAY_MODE", "CASH");
                        extensionDetails.put("EXTENSION_PAY_PRODTYPE", String.valueOf(""));
                        extensionDetails.put("EXTENSION_PAY_PRODID", String.valueOf(""));
                        extensionDetails.put("EXTENSION_PAY_ACCNO", String.valueOf(""));
                    } else if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("TRANSFER")) {
                        extensionDetails.put("EXTENSION_PAY_MODE", "TRANSFER");
                        extensionDetails.put("EXTENSION_PAY_PRODTYPE", extensionDepIntMap.get("EXTENSION_PAY_PRODTYPE"));
                        extensionDetails.put("EXTENSION_PAY_PRODID", extensionDepIntMap.get("EXTENSION_PAY_PRODID"));
                        extensionDetails.put("EXTENSION_PAY_ACCNO", extensionDepIntMap.get("EXTENSION_PAY_ACCNO"));
                    } else if (extensionDepIntMap.containsKey("EXTENSION_PAY_MODE") && extensionDepIntMap.get("EXTENSION_PAY_MODE").equals("")) {
                        extensionDetails.put("EXTENSION_PAY_MODE", String.valueOf(""));
                        extensionDetails.put("EXTENSION_PAY_PRODTYPE", String.valueOf(""));
                        extensionDetails.put("EXTENSION_PAY_PRODID", String.valueOf(""));
                    }
                    extensionDetails.put("EXTENSION_PRINTING_NO", extensionDepIntMap.get("EXTENSION_PRINTINGNO"));
                    extensionDetails.put("EXTENSION_PRODID", extensionDepIntMap.get("EXTENSION_PRODID"));
                    extensionDetails.put("EXTENSION_CATEGORY", extensionDepIntMap.get("EXTENSION_CATEGORY"));
                    extensionDetails.put("EXTENSION_PAY_FREQ", extensionDepIntMap.get("EXTENSION_PAY_FREQ"));
                    extensionDetails.put("BRANCH_CODE", extensionDepIntMap.get("BRANCH_CODE"));
                    extensionDetails.put("EXTENSION_CALENDER_FREQ", extensionDepIntMap.get("EXTENSION_CALENDER_FREQ"));
                    extensionDetails.put("EXTENSION_CALENDER_DAY", extensionDepIntMap.get("EXTENSION_CALENDER_FREQ_DAY"));
                    System.out.println("extensionDetails : " + extensionDetails);
                    sqlMap.executeUpdate("updateextensionDetails", extensionDetails);
                    HashMap extensionNewUpdate = new HashMap();
                    if (extensionDepIntMap.containsKey("EXTENSION_NOTICE")) {
                        extensionNewUpdate.put("MAT_ALERT_REPORT", extensionDepIntMap.get("EXTENSION_NOTICE"));
                    }
                    if (extensionDepIntMap.containsKey("EXTENSION_AUTO_RENEWAL")) {
                        extensionNewUpdate.put("AUTO_RENEWAL", extensionDepIntMap.get("EXTENSION_AUTO_RENEWAL"));
                    }
                    if (extensionDepIntMap.containsKey("EXTENSION_AUTO_RENEW_WITH")) {
                        extensionNewUpdate.put("RENEW_WITH_INT", extensionDepIntMap.get("EXTENSION_AUTO_RENEW_WITH"));
                    }
                    extensionNewUpdate.put("DEPOSIT_NO", extensionDetails.get("EXTENSION_DEPOSIT_NO"));
                    extensionNewUpdate.put("EXTENSION_CATEGORY", extensionDepIntMap.get("EXTENSION_CATEGORY"));
                    System.out.println("extensionNewUpdate : " + extensionNewUpdate);
                    sqlMap.executeUpdate("updateExtensionStatusBefAuth", extensionNewUpdate);
                    extensionDetails = null;
                    extensionNewUpdate = null;
                    System.out.println(" Extension depSubNoAccInfoTO " + depSubNoAccInfoTO);
                    sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                    objLogTO.setData(depSubNoAccInfoTO.toString());
                    objLogTO.setPrimaryKey(depSubNoAccInfoTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                } else {
                    System.out.println(" else depSubNoAccInfoTO " + depSubNoAccInfoTO); 
                    HashMap oldbehavesMap = new HashMap(); //08-05-2020
                    String behavesLilike = "";                    
                    oldbehavesMap.put("PROD_ID", objTO.getProdId());
                    List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
                    if (lstProd != null && lstProd.size() > 0) {
                        oldbehavesMap = (HashMap) lstProd.get(0);
                        behavesLilike = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE")); //08-05-2020
                    }
                    if (behavesLilike.equalsIgnoreCase("RECURRING") && depSubNoAccInfoTO.getIntpayMode().equalsIgnoreCase("TRANSFER")) { //08-05-2020
                        depSubNoAccInfoTO.setIntPayProdType("TD");
                        depSubNoAccInfoTO.setIntPayProdId(objTO.getProdId());
                        depSubNoAccInfoTO.setIntPayAcNo(objTO.getDepositNo() + "_1");                      
                    }
                    if (i < isMore) { //--- If the data is exisiting , update the data
                        sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                        objLogTO.setData(depSubNoAccInfoTO.toString());
                        objLogTO.setPrimaryKey(depSubNoAccInfoTO.getKeyData());
                        objLogDAO.addToLog(objLogTO);
                    } else { //--- Else insert the data.
                        //                if(objTO.getCommand().equals(CommonConstants.TOSTATUS_RENEW)){
                        //                    depSubNoAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
                        //                }
                        depSubNoAccInfoTO.setRenewedDt((Date)currDt.clone());//31-05-2019
                        sqlMap.executeUpdate("insertDepSubNoAccInfoTO", depSubNoAccInfoTO);
                        objLogTO.setData(depSubNoAccInfoTO.toString());
                        objLogTO.setPrimaryKey(depSubNoAccInfoTO.getKeyData());
                        objLogDAO.addToLog(objLogTO);
                    }
                    if (behavesLilike.equalsIgnoreCase("RECURRING") && depSubNoAccInfoTO.getIntpayMode().equalsIgnoreCase("TRANSFER")) { //08-05-2020
                        sqlMap.executeUpdate("updateCalendarDayForSpecialRD", depSubNoAccInfoTO);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try
            {
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        if (!objTO.getOpeningMode().equals("Normal")) {
            HashMap panandPrintingMap = new HashMap();
            panandPrintingMap.put("DEPOSIT_NO", objTO.getDepositNo());
            List lst = sqlMap.executeQueryForList("getSelectPanNoandPrintingNo", panandPrintingMap);
            if (lst != null && lst.size() > 0) {
                panandPrintingMap = (HashMap) lst.get(0);
                if (objTO.getPanNumber().length() == 0) {
                    objTO.setPanNumber(CommonUtil.convertObjToStr(panandPrintingMap.get("PAN_NUMBER")));
                }
                if (CommonUtil.convertObjToStr(objTO.getPrintingNo()).length() == 0) {
                    objTO.setPrintingNo(CommonUtil.convertObjToInt(panandPrintingMap.get("PRINTING_NO")));
                }
                panandPrintingMap = null;
            }
        }
        if(CommonUtil.convertObjToStr(objTO.getMdsGroup()).equals("")){
                objTO.setMdsGroup(null);
        }
        sqlMap.executeUpdate("updateAccInfoTO", objTO);
        objLogTO.setData(objTO.toString());
        objLogTO.setPrimaryKey(objTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
        depIntMap = new HashMap();
        depIntMap = (HashMap) map.get("RENEWALMAP");
        HashMap intMap = (HashMap) map.get("INTMAP");
        if (intMap != null && !intMap.isEmpty()) {
            depIntMap.put("OLD_DEPOSIT_NO", intMap.get("ACT_NUM"));
        }
        extensionDepIntMap = new HashMap();
        extensionDepIntMap = (HashMap) map.get("EXTENSIONMAP");
        updateDepSubNoAccInfoDetails();

        //--- Updates the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
        if (mapJointAccntTO != null) {
            updateJointAccountDetails();
        }

        //--- updates if Opening Mode is TransferIn
        if (transferInTO != null) {
            updateTransferInDetails();
        }
        
        if (map.containsKey("SMSSubscriptionTO")) {
            objSMSSubscriptionTO = (SMSSubscriptionTO) map.get("SMSSubscriptionTO");
            if (objSMSSubscriptionTO != null) {
                updateSMSSubscription(objTO);
            }
        }

        //--- updates if Authorized Signatory is Checked
        if (objTO.getAuthorizedSignatory().equals("Y")) {
            objAuthorizedSignatoryDAO.setBorrower_No(objTO.getDepositNo());
            objAuthorizedSignatoryDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);
        }

        //--- updates if PoA is checked
        if (objTO.getPoa().equals("Y")) {
            objPowerOfAttorneyDAO.setBorrower_No(objTO.getDepositNo());
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);
        }

        System.out.println("TD DAO mapNomTO:" + mapNomTO);

        ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
        ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");

        // Update the data regarding the NomineeTable...
        if (nomineeTOList != null) {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            if (nomineeTOList.size() > 0) {
                System.out.println("Nominee List " + nomineeTOList);
                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
                objNomineeDAO.insertData(nomineeTOList, objTO.getDepositNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
            }
            if (nomineeDeleteList != null) {
                objNomineeDAO.insertData(nomineeDeleteList, objTO.getDepositNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
            }
        }


        //        //--- updates if Nominee Details is checked
        //        if(mapNomTO != null){
        //            NomineeDAO objNomineeDAO = new NomineeDAO();
        //            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
        //            ArrayList nomineeDeleteList = (ArrayList)map.get("AccountNomineeDeleteTO");
        //            System.out.println("TD DAO nomineeTOList:" + nomineeTOList);
        //            if(nomineeTOList.size()>0){
        //                System.out.println("inside SIZE > 0:");
        //                objNomineeDAO.deleteData(objTO.getDepositNo(), SCREEN);
        //                objNomineeDAO.insertData(nomineeTOList, objTO.getDepositNo(), false, USERID, SCREEN, objLogTO, objLogDAO);
        //            }
        //            if(nomineeDeleteList != null){
        //                objNomineeDAO.insertData(nomineeDeleteList, objTO.getDepositNo(), true, USERID, SCREEN, objLogTO, objLogDAO);
        //            }
        //            nomineeTOList=null;
        //            nomineeDeleteList = null;
        //            objNomineeDAO = null;
        //        }
        
        //  Added by nithya on 08-03.2016 for 0003920
        
        if(objThriftBenevolentAdditionalAmtTO != null){
            objThriftBenevolentAdditionalAmtTO.setDepositNo(objTO.getDepositNo());
            sqlMap.executeUpdate("insertThriftBenevolentAdditionalAmountTO", objThriftBenevolentAdditionalAmtTO);
            // updating ADT_AMT in DEPOSIT_SUB_ACINFO
            HashMap updateAdtAmtMap = new HashMap();
            updateAdtAmtMap.put("DEPOSIT_NO",objThriftBenevolentAdditionalAmtTO.getDepositNo());
            updateAdtAmtMap.put("ADT_AMT",objThriftBenevolentAdditionalAmtTO.getAdditionalAmount());
            sqlMap.executeUpdate("updateThriftBenevolentAdditionalAmnt", updateAdtAmtMap);
            objLogTO.setData(objThriftBenevolentAdditionalAmtTO.toString());
            objLogTO.setPrimaryKey(objThriftBenevolentAdditionalAmtTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            objThriftBenevolentAdditionalAmtTO = null; // Added by nithya
            updateAdtAmtMap = null;
        }
        
        // End
        
            }
        catch(Exception e)
        {
        sqlMap.rollbackTransaction();
        e.printStackTrace();
        throw e;
        
        }
    }

    private void updateTransferInDetails() throws Exception {
        try{
        sqlMap.executeUpdate("delTransferInTO", objTO.getDepositNo());
        //        if (mapJointAccntTO.!=null ){// && mapJointAccntTO.size() > 0){
        //            objLogTO.setData(jointAccntTO.toString());
        //            objLogTO.setPrimaryKey(jointAccntTO.getKeyData());
        //            objLogDAO.addToLog(objLogTO);
        //        }
        sqlMap.executeUpdate("insertTransferInTO", transferInTO);
        objLogTO.setData(transferInTO.toString());
        objLogTO.setPrimaryKey(transferInTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
        }
        catch(Exception e )
        {
        sqlMap.rollbackTransaction();
        e.printStackTrace();
        throw e;
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteAccInfoTO", objTO);
            objLogTO.setData(objTO.toString());
            objLogTO.setPrimaryKey(objTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            objAuthorizedSignatoryDAO.setBorrower_No(objTO.getDepositNo());
            objPowerOfAttorneyDAO.setBorrower_No(objTO.getDepositNo());
            objAuthorizedSignatoryDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public HashMap execute(HashMap map, boolean isTrans) throws Exception {
  _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        System.out.println(" execute executeMap " + map +"_branchCode -"+_branchCode);
        currDt = (Date) ServerUtil.getCurrentDate(_branchCode);
	System.out.println(" currDt---- " +currDt);
        HashMap execReturnMap = new HashMap();
        HashMap intMap = (HashMap) map.get("INTMAP");
        objLogDAO = new LogDAO();
        objLogTO = new LogTO();
        LogTO objLogTO = new LogTO();
        USERID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        System.out.println(" execute objLogTO " + objLogTO);
        ibrHierarchy = 1;
        if (map.containsKey("TERMDEPOSIT")) {
            try {
                if (isTrans) {
                    System.out.println("#@#@#@#@#@#@#@#@#@#@  Start Transaction : ");
                    sqlMap.startTransaction();
                }
                if(map.containsKey("MULTI_REN_SINGLE_TRANS_ID")){
                   generateSingleIdDepMultiRenewal=CommonUtil.convertObjToStr(map.get("MULTI_REN_SINGLE_TRANS_ID"));
                }
                objTO = (AccInfoTO) map.get("TERMDEPOSIT");
                if (objTO.getAuthorizedSignatory().equals("Y")) {
                    objAuthorizedSignatoryDAO = new AuthorizedSignatoryDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
                    objAuthorizedSignatoryDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
                    objAuthorizedSignatoryDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
                }

                if (objTO.getPoa().equals("Y")) {
                    objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
                    objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PoATO"));
                }

                mapDepSubNoAccInfoTO = (LinkedHashMap) map.get("DepSubNoAccInfoTO");
                transferInTO = (TransferInTO) map.get("TransferInTO");

                mapNomTO = (LinkedHashMap) map.get("NomTO");
                //Added by sreekrishnan
                if(map.containsKey("JointAccntTO") && map.get("JointAccntTO")!=null && !map.get("JointAccntTO").equals("")){
                    mapJointAccntTO = (LinkedHashMap) map.get("JointAccntTO");
                }      
                
                //  Added by nithya on 08-03.2016 for 0003920
                if(map.containsKey("THRIFT_BENEVOLENT_ADDITIONAL_AMT")){ 
                    // objTO.setDepositNo(genID);
                    objThriftBenevolentAdditionalAmtTO = (ThriftBenevolentAdditionalAmtTO) map.get("THRIFT_BENEVOLENT_ADDITIONAL_AMT");                    
                    //objThriftBenevolentAdditionalAmtTO.setDepositNo(objTO.getDepositNo());
                }
                // End
                
                final TOHeader toHeader = objTO.getTOHeader();
                final String command = objTO.getCommand();
                HashMap transDetailMap = new HashMap();
                if (map.containsKey("Transaction Details Data")) {
                    transDetailMap = (HashMap) map.get("Transaction Details Data");
                    System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
//                    chargeLst = (List) map.get("Charge List Data");
//                    System.out.println("@##$#$% chargeLst #### :"+chargeLst);
                }
                //--- Selects the method according to the Command type
                if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.TOSTATUS_RENEW)
                        || command.equals(CommonConstants.TOSTATUS_EXTENSION)) {
                    if (command.equals(CommonConstants.TOSTATUS_RENEW) && map.containsKey("RENEWALMAP")) {//Added By Kannan AR
                        //To Avoid Deposit renewal has generated twice vouchers
                        HashMap whereMap = new HashMap();
                        depIntMap = (HashMap) map.get("RENEWALMAP");
                        List pendingTransLst = sqlMap.executeQueryForList("getPendingTransactionForDeposit", depIntMap);
                        if (pendingTransLst != null && pendingTransLst.size() > 0) {
                            whereMap = (HashMap) pendingTransLst.get(0);
                            throw new TTException("Account Renewal not allowed. Account Closure/Renewal authorization for " + whereMap.get("DEPOSIT_NO")
                                    + " pending. Please Authorize or Reject...!!!");
                        }
                    }

                    if(map!=null && map.containsKey("MULTIPLE_DEPOSIT")){
                        System.out.println("inside multiplke deposoit");
                        String mulDepId = getMultipleDepositId();
                        objTO.setMultipleDepositId(mulDepId);
                        map.put("MULTIPLE_DEPOSIT_ID", CommonUtil.convertObjToStr(mulDepId));
                        double noOfAcounts = 0.0; 
                        if(map.containsKey("NO_OF_ACCOUNTS")){
                            noOfAcounts = CommonUtil.convertObjToDouble(map.get("NO_OF_ACCOUNTS"));
                        }
                        for(int i = 0 ; i<noOfAcounts ; i++){
                            insertData(map, objLogDAO, objLogTO, transDetailMap);
                            execReturnMap.put("MULTIPLE_DEPOSIT_ID",CommonUtil.convertObjToStr(objTO.getMultipleDepositId()));
                            execReturnMap.put(DEPOSITNO+CommonUtil.convertObjToStr(i), returnDepositNo);
                        }
                    }else{
                        insertData(map, objLogDAO, objLogTO, transDetailMap);
                        execReturnMap.put(CommonConstants.TRANS_ID, returnDepositNo);
                        execReturnMap.put(DEPOSITNO, returnDepositNo);
                    }
                    System.out.println("execReturnMap insert :" + execReturnMap);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map, objLogDAO, objLogTO);
                    System.out.println("execReturnMap update :" + execReturnMap);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }
                destroyObjects();

                if (isTrans) {
                    sqlMap.commitTransaction();
                }
                if (map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.get("FROM_MOBILE_APP_CUST_CREATION") != null && map.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION") && returnDepositNo.length() > 0) {
                    List authList = new ArrayList();
                    HashMap authMap = new HashMap();
                    authMap.put("NORMAL_MODE","NORMAL_MODE");
                    authMap.put("FROM_MOBILE_APP_CUST_CREATION","FROM_MOBILE_APP_CUST_CREATION");
                    authMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    authMap.put("BRANCH_CODE", _branchCode);
                    authMap.put("UI_PRODUCT_TYPE", "TD");
                    authMap.put("AUTHORIZESTATUS", "AUTHORIZED");
                    HashMap authDataMap = new HashMap();
                    authDataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    authDataMap.put("DEPOSIT NO", returnDepositNo);
                    authList.add(authDataMap);
                    authMap.put("AUTHORIZEDATA", authList);
                    authMap.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN","CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
                    authorize(authMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isTrans) {
                    sqlMap.rollbackTransaction();
                }
                if (e instanceof TTException) {
                    throw e;
                } else {
                    throw new TTException(e);
                }
            }
        } else {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (map.containsKey("INV")) {
                authMap.put("INV", "INV");
            }
            if (authMap != null) {
                if (isTrans) {
                    sqlMap.startTransaction();
                }
                if(map.containsKey("MULTIPLE_DEPOSIT")){
                    List authorizeDataList = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
                    HashMap multipleDepositAcctNoMap = (HashMap) authorizeDataList.get(0);
                    List multipleDepositAcctNoList = (List)multipleDepositAcctNoMap.get("multipleDepositAcctNoList");
                    for(int i = 0 ; i < multipleDepositAcctNoList.size(); i++){
                        HashMap depositAcctMap = (HashMap)multipleDepositAcctNoList.get(i);
                        multipleDepositAcctNoMap.put("DEPOSIT NO",CommonUtil.convertObjToStr(depositAcctMap.get("DEPOSIT_NO")));
                        List arrayList = new ArrayList();
                        arrayList.add(multipleDepositAcctNoMap);
                        authMap.put(CommonConstants.AUTHORIZEDATA, arrayList); 
                        authorize(authMap);
                    }
                }else{
                    authorize(authMap);
                }
                if (isTrans) {
                   sqlMap.commitTransaction(); 
                }
            }
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    private void executeTransactionPart(HashMap map, HashMap transDetailMap) throws Exception {
        try {
            transDetailMap.put("DEPOSIT_NO", objTO.getDepositNo());
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            System.out.println("@##$#$% map #### :" + map);
            System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                if (transDetailMap.containsKey("TRANSACTION_PART")) {
//                    transDetailMap = getProdBehavesLike(transDetailMap);
                    insertTimeTransaction(map, transDetailMap);

                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void insertTimeTransaction(HashMap map, HashMap dataMap) throws Exception {
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
             String generateSingleTransId ="";
            if(generateSingleIdDepMultiRenewal!=null && !generateSingleIdDepMultiRenewal.equals("")){
                generateSingleTransId=generateSingleIdDepMultiRenewal;
            }
            else{
             generateSingleTransId = generateLinkID();
            }
            HashMap reserchMap = new HashMap();
            //            reserchMap = (HashMap)map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                dataMap.put("DEPOSIT_NO", objTO.getDepositNo() + "_1");
                LinkedHashMap transactionDetailsMap = new LinkedHashMap();
                TransactionTO transactionTO = null;
                if (map.containsKey("TransactionTO")) {
                    transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    LinkedHashMap allowedTransDetailsTO;
                    if (transactionDetailsMap.size() > 0) {
                        if (transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            if (allowedTransDetailsTO != null) {
                                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                            }
                        }
                    }
                }
//                acHeads = (HashMap)sqlMap.executeQueryForObject("getLoanAccountClosingHeads", dataMap.get("ACT_NUM"));
//                System.out.println("acHeads "+acHeads);
                if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                    HashMap depositAuthTransMap = new HashMap();
                    depositAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    depositAuthTransMap.put("DEPOSIT_NO", dataMap.get("DEPOSIT_NO"));
                    depositAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                    depositAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    depositAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    depositAuthTransMap.put("ACCT_HEAD", dataMap.get("ACCT_HEAD"));
                    depositAuthTransMap.put("DEPOSIT_AMOUNT", dataMap.get("DEPOSIT_AMOUNT"));
                    depositAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    depositAuthTransMap.put("TRANS_MOD_TYPE", "TD");
                    if(map.containsKey("MULTIPLE_DEPOSIT_ID")){
                        depositAuthTransMap.put("MULTIPLE_DEPOSIT_ID", map.get("MULTIPLE_DEPOSIT_ID"));
                    }
                    depositAuthTransMap.put("INSTRUMENT_NO1",CommonUtil.convertObjToStr(transactionTO.getChequeNo()));
                    depositAuthTransMap.put("INSTRUMENT_NO2",CommonUtil.convertObjToStr(transactionTO.getChequeNo2()));

                    depositAuthTransMap.put(CommonConstants.SCREEN,CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    ArrayList depositAuthTransList = depositAuthorizeTimeTransaction(depositAuthTransMap);
                    depositAuthTransMap.put("DAILYDEPOSITTRANSTO", depositAuthTransList);
                    depositAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                    depositAuthTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);
                    depositAuthTransMap.put("TRANS_MOD_TYPE", CommonConstants.DEPOSITS);
                    
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = cashTransactionDAO.execute(depositAuthTransMap, false);
                    System.out.println("cashMap :" + cashMap);
                    cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap authStatusMap = new HashMap();
                    arrList = null;
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    depositAuthTransMap = null;
                    authDataMap = null;
                    authStatusMap = null;
                } else if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    transactionDAO.setLinkBatchID((String) dataMap.get("DEPOSIT_NO"));
                    transactionDAO.setInitiatedBranch(branchId);
//                    HashMap crMap = new HashMap();
//                    crMap.put("ACT_NUM",dataMap.get("CR_ACT_NUM"));
//                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet",crMap);
//                    if(oaAcctHead!=null && oaAcctHead.size()>0){
//                        crMap = (HashMap)oaAcctHead.get(0);
//                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TxTransferTO transferTo = new TxTransferTO();
                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMOUNT")).doubleValue();
                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("DEPOSIT_NO"));
                    txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.PARTICULARS, "depositOpening - " + dataMap.get("DEPOSIT_NO"));
                    txMap.put("AUTHORIZEREMARKS", "DP");
//                    txMap.put("DEBIT_LOAN_TYPE", "DP");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    if (!CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("DR_PROD_ID"));
                    } else {
                        txMap.put(TransferTrans.DR_AC_HD, dataMap.get("CR_ACT_NUM"));
                    }
                    if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("INV")) {
                        InvestmentsTransTO objTO = new InvestmentsTransTO();
                        dataMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(dataMap.get("CR_ACT_NUM")));
                        objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, interestAmt, dataMap, dataMap);
                        HashMap achdMap = new HashMap();
                        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
                        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                        if (achdLst != null && achdLst.size() > 0) {
                            achdMap = new HashMap();
                            achdMap = (HashMap) achdLst.get(0);
                            txMap.put(TransferTrans.DR_AC_HD, achdMap.get("IINVESTMENT_AC_HD"));
                            acHeads.put("IINVESTMENT_AC_HD", achdMap.get("IINVESTMENT_AC_HD"));
                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_DEPOSIT");
                            //Set Trans ID
                            objTO.setBatchID(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                        } else {
                            throw new TTException("Account heads not set properly...");
                        }
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    } else {
                        txMap.put(TransferTrans.DR_PROD_TYPE, dataMap.get("PROD_TYPE"));
                    }
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    // Added producttype GL checking by nithya for KD 317 - BRANCH ID UPDATED INCORRECT.
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                        txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                    } else {
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    }
                    if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("OA")) {
                        txMap.put("TRANS_MOD_TYPE", "OA");
                    }else if(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("AB")) {
                        txMap.put("TRANS_MOD_TYPE", "AB");
                    }else if(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("SA")) {
                        txMap.put("TRANS_MOD_TYPE", "SA");
                    }else if(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("TL")) {
                        txMap.put("TRANS_MOD_TYPE", "TL");
                    }else if(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("AD")) {
                        txMap.put("TRANS_MOD_TYPE", "AD");
                    }else
                        txMap.put("TRANS_MOD_TYPE", "GL");
                    
                    if (objTO.getOpeningMode().equals("Normal")) {
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    } else {
                      txMap.put("SCREEN_NAME","Deposit Account Renewal");
                    }
                    if(!((CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"))).equalsIgnoreCase(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"))))){                        
                        txMap.put("TERM_DEPOSIT_INTER_BRANCH_TRANS", "TERM_DEPOSIT_INTER_BRANCH_TRANS");  
                        txMap.put("INITIATED_BRANCH",_branchCode);
                    }
                    if (map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.get("FROM_MOBILE_APP_CUST_CREATION") != null && map.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION")) {
                        txMap.put("NARRATION", "MOBILE_APP");
                        txMap.put("SCREEN_NAME", "Deposit Auto Creation");
                    }
                    System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("DEPOSIT_NO"));
                    transferTo.setSingleTransId(generateSingleTransId);
                    if(CommonUtil.convertObjToStr(transactionTO.getInstType()).equals("CHEQUE")){
                        transferTo.setInstType(CommonUtil.convertObjToStr(transactionTO.getInstType()));
                        transferTo.setInstrumentNo1(CommonUtil.convertObjToStr(transactionTO.getChequeNo()));
                        transferTo.setInstrumentNo2(CommonUtil.convertObjToStr(transactionTO.getChequeNo2()));
                    }
                    transferList.add(transferTo);
                    if (objTO.getOpeningMode().equals("Normal")) { // Added by nithya on 13-01-2021 for KD-2461
                        txMap.put(TransferTrans.PARTICULARS, dataMap.get("CR_ACT_NUM"));
                    }else{
                    txMap.put(TransferTrans.PARTICULARS, dataMap.get("DEPOSIT_NO"));
                    }
                    txMap.put("TRANS_MOD_TYPE", "TD");
                    if (objTO.getOpeningMode().equals("Normal")) {
                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                    } else {
                      txMap.put("SCREEN_NAME","Deposit Account Renewal");
                    }
                    if (map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.get("FROM_MOBILE_APP_CUST_CREATION") != null && map.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION")) {
                        txMap.put("NARRATION", "MOBILE_APP");
                        txMap.put("SCREEN_NAME", "Deposit Auto Creation");
                    }
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("DEPOSIT_NO"));
                    transferTo.setSingleTransId(generateSingleTransId);
                    if(map.containsKey("MULTIPLE_DEPOSIT_ID")){
                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(map.get("MULTIPLE_DEPOSIT_ID")));
                    }
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));

//                    if(chargeLst != null && chargeLst.size()>0 ){   //Charge Details Transaction
//                        System.out.println("@##$#$% chargeLst #### :"+chargeLst);
//                        for(int i=0; i<chargeLst.size(); i++){
//                            HashMap chargeMap = new HashMap();
//                            String accHead="";
//                            double chargeAmt = 0;
//                            String chargeType="";
//                            chargeMap = (HashMap)chargeLst.get(i);
//                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
//                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
//                            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
//                            System.out.println("$#@@$ accHead"+accHead);
//                            System.out.println("$#@@$ chargeAmt"+chargeAmt);
//                            
//                            txMap = new HashMap();
//                            transferList = new ArrayList();
//                            transferTo = new TxTransferTO();
//                            txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
//                            txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
//                            txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
//                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
//                            txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
//                            txMap.put(TransferTrans.PARTICULARS, dataMap.get("DEPOSIT_NO"));
//                            System.out.println("txMap  ###"+txMap+"transferDao   :"+transferDao);
//                            
//                            txMap.put(TransferTrans.CR_AC_HD, accHead);
//                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//                            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
//                            txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
//                            System.out.println("txMap  ###"+txMap+"transferDao   :"+transferDao);
//                            
//                            transferTo =  transactionDAO.addTransferDebitLocal(txMap, chargeAmt);
//                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                            transferTo.setLinkBatchId((String)dataMap.get("ACT_NUM"));
//                            transferList.add(transferTo);
//                            txMap.put(TransferTrans.PARTICULARS, chargeType+ " - : "+dataMap.get("ACT_NUM"));
//                            transferTo =  transactionDAO.addTransferCreditLocal(txMap, chargeAmt) ;
//                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
//                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
//                            transferTo.setLinkBatchId((String)dataMap.get("ACT_NUM"));
//                            transferList.add(transferTo);
//                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
//                            transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
//                        }
//                    }
                    transList = null;
                    transactionDAO = null;
                    transferDao = null;
                    txMap = null;
                    acHeads = null;
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw  e;
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }

    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT((Date)currDt.clone());
            objgetInvestmentsTransTO.setTransType("CREDIT");
            objgetInvestmentsTransTO.setTrnCode("Withdrawal");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt((Date)currDt.clone());
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt((Date)currDt.clone());
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate((Date)currDt.clone());
            objgetInvestmentsTransTO.setInitiatedBranch(_branchCode);
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }

    private ArrayList depositAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
            objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.DEPOSITS));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setParticulars("To Cash : " + dataMap.get("DEPOSIT_NO"));
            objCashTO.setAuthorizeRemarks("DP");
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt((Date)currDt.clone());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT_NO")));
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            if(dataMap.containsKey("MULTIPLE_DEPOSIT_ID")){
                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("MULTIPLE_DEPOSIT_ID")));
            }
            //akhil@
            //akhil@
            if (objTO.getOpeningMode().equals("Normal")) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
            } else {
                objCashTO.setScreenName("Deposit Account Renewal");
            }
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }

    private HashMap getProdBehavesLike(HashMap dataMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("TermLoan.getBehavesLike", dataMap);
        if (list.size() > 0) {
            HashMap retrieveMap = (HashMap) list.get(0);
            dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
            dataMap.put("prodId", retrieveMap.get(PROD_ID));
            dataMap.put(LIMIT, retrieveMap.get(LIMIT));
            dataMap.put(BEHAVES_LIKE, retrieveMap.get(BEHAVES_LIKE));
            dataMap.put(INT_GET_FROM, retrieveMap.get(INT_GET_FROM));
            dataMap.put(SECURITY_DETAILS, retrieveMap.get(SECURITY_DETAILS));
            System.out.println("dataMap  ##" + dataMap);
            retrieveMap = null;
        }
        list = null;
        return dataMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("executeMap : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        //interBranch = (String) map.get(CommonConstants.SELECTED_BRANCH_ID); //Commented By Suresh R 17-Jun-2019 (KDSA-526 : ERROR IN FIXED DEPOSIT A/C NUMBER)
        //Discussed with Jithesj, He told there is no concept for InterBranch Deposit Creation.
        interBranch = _branchCode;
        return execute(map, true);
    }

    private AccInfoTO getAccInfoData(String depNo) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getAccInfoTo", depNo);
        return ((AccInfoTO) list.get(0));
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("map :" + map + "status :" + status + "selectedList :" + selectedList);
        HashMap execReturnMap;
        HashMap dataMap;
        String depNo;
        AccInfoTO objAccInfoTO = null;
        DepositClosingDAO objDepositClosingDAO = new DepositClosingDAO();
        try {
            System.out.println("#@#@#@#@#@#@#@#@#@#@  Start Transaction 1 : ");
            //sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                System.out.println("*******dataMap :" + dataMap);
                depNo = CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO));
                //                HashMap txMap = new HashMap();
                //                ArrayList transferList = new ArrayList();
                //                TransferTrans transferTrans = new TransferTrans();
                //                TxTransferTO transferTo = new TxTransferTO();
                HashMap depositMap = new HashMap();
                String oldDepNo = null;
                String newDepNo = null;
                String subNo = null;
                double totIntAmt = 0.0;
                double balIntAmt = 0.0;
                double sbIntAmt = 0.0;
                String oldStatus = ""; //Added By Kannan AR
                Date oldRenewDt = null; //Added By Kannan AR
                HashMap subMap = new HashMap();
                HashMap accountHeadMap = new HashMap();
                interestBatchTO = new InterestBatchTO();
                HashMap balanceMap = new HashMap();
                HashMap oldprodMap = new HashMap();
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", (Date)currDt.clone());
                dataMap.put("STATUS", map.get(CommonConstants.AUTHORIZESTATUS));
                depositMap.put(DEPOSITNO, dataMap.get(DEPOSITNO));
                double prevInt=0;
                List list = (List) sqlMap.executeQueryForList("getRenewalCountForDep", depositMap);
                if (list != null && list.size() > 0) {
                    System.out.println("Renewal Deposits :" + dataMap);
                    depositMap = (HashMap) list.get(0);
                    String branchID = CommonUtil.convertObjToStr(depositMap.get("BRANCH_ID"));
                    System.out.println("****** depositMap : " + depositMap);
                    int count = CommonUtil.convertObjToInt(depositMap.get("RENEWAL_COUNT"));
                    oldDepNo = CommonUtil.convertObjToStr(depositMap.get("RENEWAL_FROM_DEPOSIT"));
                    newDepNo = CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_NO"));
                    subNo = CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_SUB_NO"));
                    oldStatus = CommonUtil.convertObjToStr(depositMap.get("STATUS"));
                    oldRenewDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositMap.get("RENEWED_DT")));
                    String oldDepositNo = oldDepNo + "_" + subNo;
                    String newDepositNo = newDepNo + "_" + subNo;
                    System.out.println("newDepositNo:" + newDepositNo + "oldDepositNo:" + oldDepositNo + "subNo:" + subNo);
                    subMap.put(DEPOSITNO, depositMap.get("RENEWAL_FROM_DEPOSIT"));
                    if (oldDepNo.equals(newDepNo)) {
                        list = (List) sqlMap.executeQueryForList("getShadowDebitAmtSameNo", subMap);
                    } else {
                        list = (List) sqlMap.executeQueryForList("getShadowDebitAmt", subMap);
                    }
                    HashMap renewalTempMap = new HashMap();
                    renewalTempMap.put("DEPOSIT_NO", newDepNo);
                    List lstTemp = sqlMap.executeQueryForList("getSelectRenewalDetails", renewalTempMap);
                    if (lstTemp != null && lstTemp.size() > 0) {
                        System.out.println("getSelectRenewalDetails :" + renewalTempMap);
                        renewalTempMap = (HashMap) lstTemp.get(0);
                        prevInt=CommonUtil.convertObjToDouble(renewalTempMap.get("PREVIOUS_INT"));
                        if (status != null && status.equals("AUTHORIZED")) {
                            //updateRenewalDateinSubacinfo,updateTempRenewalDateinSubacinfo Query added here by Kannan AR Jira : KDSA-191
                            HashMap renewMap = new HashMap();
                            renewMap.put("ACT_NUM", renewalTempMap.get("OLD_DEPOSIT_NO"));
                            renewMap.put("RENEWED_DT", currDt.clone());
                            if (oldRenewDt != null) {
                                renewMap.put("OLD_RENEWED_DT", oldRenewDt);
                                sqlMap.executeUpdate("updateTempRenewalDateinSubacinfo", renewMap);
                            }
                            sqlMap.executeUpdate("updateRenewalDateinSubacinfo", renewMap);
                            if (depositMap.get("RENEWAL_FROM_DEPOSIT") != null) {//&& count >0
                                if (list != null && list.size() > 0) {
                                    subMap = (HashMap) list.get(0);
                                    String oldBehaves = null;
                                    String oldProdId = CommonUtil.convertObjToStr(subMap.get("PROD_ID"));
                                    String newProdId = CommonUtil.convertObjToStr(depositMap.get("PROD_ID"));
                                    String newBehaves = CommonUtil.convertObjToStr(depositMap.get("BEHAVES_LIKE"));
                                    oldprodMap.put("PROD_ID", subMap.get("PROD_ID"));
                                    List lstProd = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldprodMap);
                                    if (lstProd != null && lstProd.size() > 0) {
                                        oldprodMap = (HashMap) lstProd.get(0);
                                        System.out.println("****** getBehavesLikeForDeposit : " + oldprodMap);
                                        oldBehaves = CommonUtil.convertObjToStr(oldprodMap.get("BEHAVES_LIKE"));
                                        oldprodMap.put("PROD_ID", subMap.get("PROD_ID"));
                                        lstProd = sqlMap.executeQueryForList("getDepositClosingHeads", oldprodMap);
                                        if (lstProd != null && lstProd.size() > 0) {
                                            oldprodMap = (HashMap) lstProd.get(0);
                                            System.out.println("****** getDepositClosingHeads oldprodMap : " + oldprodMap);
                                        }
                                    }
                                    System.out.println("oldBehaves:" + oldBehaves + "newBehaves:" + newBehaves);
                                    balIntAmt = CommonUtil.convertObjToDouble(subMap.get("INTEREST_AMT")).doubleValue();
                                    sbIntAmt = CommonUtil.convertObjToDouble(subMap.get("SB_INT_AMT")).doubleValue();
                                    totIntAmt = CommonUtil.convertObjToDouble(subMap.get("TOT_INT_AMT")).doubleValue();
                                    totIntAmt = (double) getNearest((long) (totIntAmt * 100), 100) / 100;
                                    double paid = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    double debitInt = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                    //                                    transferTrans.setInitiatedBranch(super._branchCode);
                                    //                                    transferTrans.setLinkBatchId(oldDepositNo);
                                    depositMap.put("RENEWAL_FROM_DEPOSIT", oldDepositNo);
                                    depositMap.put("DEPOSIT_NO", newDepositNo);
                                    System.out.println("****** subMap : " + subMap + "****** balIntAmt : " + balIntAmt + "****** sbIntAmt : " + sbIntAmt
                                            + "****** sbIntAmtafter add : " + depositMap);
                                    accountHeadMap.put("PROD_ID", depositMap.get("PROD_ID"));
                                    List lst = sqlMap.executeQueryForList("getDepositClosingHeads", accountHeadMap);
                                    if (lst != null && lst.size() > 0) {
                                        accountHeadMap = (HashMap) lst.get(0);
                                        System.out.println("****** accountHeadMap : " + accountHeadMap);
                                        if (balIntAmt > 0 && CommonUtil.convertObjToStr(subMap.get("LAST_INT_APPL_DT")) != null) {
                                            Date transdt = (Date) currDt.clone();
                                            Date intdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("LAST_INT_APPL_DT")));
                                            if (intdt != null) {
                                                transdt.setDate(intdt.getDate());
                                                transdt.setMonth(intdt.getMonth());
                                                transdt.setYear(intdt.getYear());
                                                interestBatchTO.setIntDt(transdt);
                                            }
//                                        interestBatchTO.setIntDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("LAST_INT_APPL_DT"))));
                                        } else if (balIntAmt > 0 && CommonUtil.convertObjToStr(subMap.get("LAST_INT_APPL_DT")).equals("")) {
                                            Date transdt = (Date) currDt.clone();
                                            Date intdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("DEPOSIT_DT")));
                                            if (intdt != null) {
                                                transdt.setDate(intdt.getDate());
                                                transdt.setMonth(intdt.getMonth());
                                                transdt.setYear(intdt.getYear());
                                                interestBatchTO.setIntDt(transdt);
                                            }
//                                            interestBatchTO.setIntDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("DEPOSIT_DT"))));
                                        } else {
                                            Date transdt = (Date) currDt.clone();
                                            Date intdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("MATURITY_DT")));
                                            if (intdt != null) {
                                                transdt.setDate(intdt.getDate());
                                                transdt.setMonth(intdt.getMonth());
                                                transdt.setYear(intdt.getYear());
                                                interestBatchTO.setIntDt(transdt);
                                            }
//                                        interestBatchTO.setIntDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(subMap.get("MATURITY_DT"))));
                                        }
                                        interestBatchTO.setActNum(oldDepositNo);
                                        interestBatchTO.setProductId(CommonUtil.convertObjToStr(depositMap.get("PROD_ID")));
                                        interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(depositMap.get("DEPOSIT_AMT")));
                                        interestBatchTO.setCustId(CommonUtil.convertObjToStr(depositMap.get("CUST_ID")));
                                        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(depositMap.get("ROI")));
                                        interestBatchTO.setApplDt((Date)currDt.clone());
                                        if (depositMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                            interestBatchTO.setIntType("SIMPLE");
                                        } else {
                                            interestBatchTO.setIntType("COMPOUND");
                                        }
                                        interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("INT_PAY")));
                                        interestBatchTO.setProductType(SCREEN);
                                        interestBatchTO.setTrnDt((Date)currDt.clone());
                                        double interest = paid - debitInt;
                                        
                                        double balance = totIntAmt - paid;
                                        System.out.println("interest :" + interest + "balance :" + balance);
                                        if (prevInt > 0) {
                                            balance = balance + prevInt;
                                        }
                                        double bal = 0.0;
                                        HashMap renewalCountMap = new HashMap();
                                        renewalCountMap.put("ACT_NUM", dataMap.get(DEPOSITNO));
                                        List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                                        if (lstCount != null && lstCount.size() > 0) {
                                            renewalCountMap = (HashMap) lstCount.get(0);
                                            interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                                        }
                                        if (balance == 0) {
                                            if (sbIntAmt > 0) {
                                                interestBatchTO.setDrCr("CREDIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(sbIntAmt));
                                                bal = interest + sbIntAmt;
                                                interestBatchTO.setTot_int_amt(new Double(bal));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            }
                                            
                                            if (sbIntAmt > 0) {
                                                System.out.println("***** interest>0 if con :" + sbIntAmt);
                                                interestBatchTO.setDrCr("DEBIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(sbIntAmt));
                                                bal = bal - sbIntAmt;
                                                interestBatchTO.setTot_int_amt(new Double(bal));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            }
                                            if (interest > 0 || bal > 0) {  
                                                System.out.println("***** balance if:" + balance);
                                                interestBatchTO.setDrCr("DEBIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                if (bal == 0) {
                                                    interestBatchTO.setIntAmt(new Double(interest));
                                                } else {
                                                    interestBatchTO.setIntAmt(new Double(bal));
                                                }
                                                interestBatchTO.setTot_int_amt(new Double(0.0));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            }
                                        } else {
                                            if (balance > 0) {
                                                System.out.println("***** interest>0 else con :" + balIntAmt);
                                                interestBatchTO.setDrCr("CREDIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(balance));
                                                bal = balance + interest;
                                                interestBatchTO.setTot_int_amt(new Double(bal));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            } else if (balance < 0) {
                                                balance = balance * -1;
                                                interestBatchTO.setDrCr("CREDIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(balance));
                                                //                                                bal = balance +interest;
                                                interestBatchTO.setTot_int_amt(new Double(balance));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);

                                                interestBatchTO.setDrCr("DEBIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(balance));
                                                interestBatchTO.setTot_int_amt(new Double(0.0));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                                balance = 0;
                                            }
                                            if (balance > 0) {
                                                if (sbIntAmt > 0) {
                                                    System.out.println("***** sbIntAmt>0 else con :" + sbIntAmt);
                                                    interestBatchTO.setDrCr("CREDIT");
                                                    interestBatchTO.setTransLogId("Payable");
                                                    interestBatchTO.setIntAmt(new Double(sbIntAmt));
                                                    bal = bal + sbIntAmt;
                                                    interestBatchTO.setTot_int_amt(new Double(bal));
                                                    sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                                }
                                                System.out.println("***** sbIntAmt>0 else con :" + balIntAmt);
                                                interestBatchTO.setDrCr("DEBIT");
                                                interestBatchTO.setTransLogId("Payable");
                                                interestBatchTO.setIntAmt(new Double(bal));
                                                interestBatchTO.setTot_int_amt(new Double(0.0));
                                                sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            }
                                        }
                                    }
                                    balanceMap = new HashMap();
                                    balanceMap.put("DEPOSIT_NO", oldDepNo);
                                    totIntAmt = totIntAmt;
                                    balanceMap.put("INT_AMT", new Double(totIntAmt));
//                                    String depNoN = "";
                                    if (oldDepNo.equals(newDepNo)) {
                                        subMap.put(DEPOSITNO, oldDepNo);
//                                        depNoN = CommonUtil.convertObjToStr(oldDepNo);
                                        balanceMap.put("PAYMENT_DAY",currDt.clone());
                                        sqlMap.executeUpdate("updateClosingSameTotInd", balanceMap);
                                        sqlMap.executeUpdate("updateClosingTotInd", balanceMap);
                                        //                                    sqlMap.executeUpdate("updateClosingOldRec",balanceMap);
                                    } else {
                                        subMap.put(DEPOSITNO, newDepNo);
//                                        depNoN = CommonUtil.convertObjToStr(newDepNo);
                                        sqlMap.executeUpdate("updateClosingTotIndAmount", balanceMap);
                                        sqlMap.executeUpdate("updateClosingBalIntAmount", balanceMap);
                                    }
                                    System.out.println("paid : " + paid + " subMap : " + subMap + " debitInt : " + debitInt);
                                    double clPaidInt = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_CREDIT"));
                                    double clIntdrawn = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_DRAWN"));
                                    HashMap tempMap = new HashMap();
                                    tempMap.put("DEPOSIT NO", oldDepNo);
                                    HashMap tmpHashMap = new HashMap();
                                    HashMap statusMap = new HashMap();
                                    tmpHashMap.put("DEPOSITNO", oldDepNo);
                                    List listNew = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", tempMap);
                                    if (listNew != null && listNew.size() > 0) {
                                        statusMap = (HashMap) listNew.get(0);
                                        tmpHashMap.put("CLEAR_BALANCE", statusMap.get("CLEAR_BALANCE"));
                                        tmpHashMap.put("AVAILABLE_BALANCE", statusMap.get("AVAILABLE_BALANCE"));
                                        tmpHashMap.put("TOTAL_BALANCE", statusMap.get("TOTAL_BALANCE"));
                                        sqlMap.executeUpdate("updateTEMPBalanceTD", tmpHashMap); 
                                    }
                                    HashMap replaceMap = new HashMap();
                                    replaceMap.put("DEPOSIT NO", oldDepNo);
                                    replaceMap.put("TOTAL_INT_CREDIT", clPaidInt);
                                    replaceMap.put("TOTAL_INT_DRAWN", clIntdrawn);
                                    sqlMap.executeUpdate("creditDrawnHistory", replaceMap);
                                    sqlMap.executeUpdate("creditDrawnHistorySameNO", replaceMap);
                                    HashMap renewTempMap = new HashMap();
                                    renewTempMap.put("DEPOSIT_NO", newDepNo);
                                    sqlMap.executeUpdate("updateTempStatus", renewTempMap);
                                    renewTempMap = null;
                                    if (oldDepNo.equals(newDepNo)) {
                                        HashMap closingMap = new HashMap();
                                        closingMap.put("STATUS", status);
                                        closingMap.put("USER_ID", dataMap.get("USER_ID"));
                                        closingMap.put("AUTHORIZE_DT", currDt.clone());
                                        closingMap.put("AUTHORIZEDT", currDt.clone());
                                        closingMap.put("ACCOUNTNO", oldDepositNo);
                                        System.out.println("closingMap#####" + closingMap);
                                        sqlMap.executeUpdate("authorizeDepositClose", closingMap);
                                        closingMap.put("ACCOUNTNO", oldDepNo);
                                        closingMap.put("DEPOSIT_NO", oldDepNo);
                                        sqlMap.executeUpdate("authorizeDepositSubClose", closingMap);
                                        HashMap newDep = new HashMap();
                                        if(oldStatus.length()>0){ //Added By Kannan AR KDSA : 316
                                            newDep.put("NORM_STATUS",oldStatus);
                                        }                                                                            
                                        newDep.put(DEPOSITNO, oldDepNo);
                                        newDep.put("PAYMENT_DAY", currDt.clone());
                                        sqlMap.executeUpdate("authorizeDepositNewSubAccInfo", newDep);
                                        closingMap = null;
                                        newDep = null;
                                        //                                    sqlMap.executeUpdate("authorizeSameDepositClose", closingMap);
                                    } else {
                                        System.out.println("equals oldDepNo : " + oldDepNo);
                                        if (oldDepNo != null && oldDepNo.length() > 0) {
                                            HashMap closingMap = new HashMap();
                                            closingMap.put("STATUS", status);
                                            closingMap.put("USER_ID", dataMap.get("USER_ID"));
                                            closingMap.put("AUTHORIZE_DT", currDt.clone());
                                            closingMap.put("ACCOUNTNO", oldDepositNo);
                                            closingMap.put("CLOSE_DT", currDt.clone());
                                            sqlMap.executeUpdate("authorizeDepositAccountClose", closingMap);
                                            closingMap.put("ACCOUNTNO", oldDepNo);
                                            sqlMap.executeUpdate("authorizeDepositSubAccountClose", closingMap);
                                            closingMap = null;
                                        }
                                        System.out.println("****** newDepNo : " + newDepNo);
                                        if (newDepNo != null && newDepNo.length() > 0) {
                                            balanceMap.put(CommonConstants.STATUS, status);
                                            balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            balanceMap.put("CURR_DATE", currDt.clone());
                                            balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                            balanceMap.put("STATUS", status);
                                            balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                                            sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                                            HashMap newDep = new HashMap();
                                            newDep.put("NORM_STATUS", CommonConstants.STATUS_CREATED);
                                            newDep.put(DEPOSITNO, newDepNo);
                                            newDep.put("PAYMENT_DAY", currDt.clone());
                                            sqlMap.executeUpdate("authorizeDepositNewSubAccInfo", newDep);
                                            newDep = null;
                                        }
                                    }
                                } else {
                                    System.out.println("****** newDepNo : " + newDepNo);
                                    if (newDepNo != null && newDepNo.length() > 0) {
                                        balanceMap.put(CommonConstants.STATUS, status);
                                        balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        balanceMap.put("CURR_DATE", currDt.clone());
                                        balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                        balanceMap.put("STATUS", status);
                                        balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                        sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                                        sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                                    }
                                }
                                HashMap lienDetMap = new HashMap();
                                depositLienDAO = new DepositLienDAO();
                                if (oldDepNo != null && oldDepNo.length() > 0) {
                                    double totAmt = 0.0;
                                    ArrayList lienTOs = new ArrayList();
                                    lienDetMap.put("DEPOSIT_NO", oldDepNo);
                                    branchID = CommonUtil.convertObjToStr(depositMap.get("BRANCH_ID"));
                                    List getList = sqlMap.executeQueryForList("getDepositLienDetInsert", lienDetMap);
                                    if (getList != null && getList.size() > 0) {
                                        System.out.println("getList : " + getList);
                                        for (int j = 0; j < getList.size(); j++) {
                                            depositLienTO = (DepositLienTO) getList.get(j);
                                            HashMap lienMap = new HashMap();
                                            lienMap.put("PROD_ID", depositLienTO.getLienProdId());
                                            List lstHead = sqlMap.executeQueryForList("getAccountHeadForLoansLien", lienMap);
                                            if (lstHead != null && lstHead.size() > 0) {
                                                lienMap = (HashMap) lstHead.get(0);
                                                System.out.println("lienMap : " + lienMap);
                                                if (!lienMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS") || !(oldDepNo.equals(newDepNo))) {
                                                    double amount = CommonUtil.convertObjToDouble(depositLienTO.getLienAmount()).doubleValue();
                                                    totAmt = amount + totAmt;
                                                    depositLienTO.setLienNo("-");
                                                    depositLienTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO)));
                                                    depositLienTO.setLienDt((Date)currDt.clone());
                                                    depositLienTO.setStatus(CommonConstants.STATUS_CREATED);
                                                    depositLienTO.setUnlienDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
                                                    depositLienTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                                    depositLienTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                                    depositLienTO.setStatusDt((Date)currDt.clone());
                                                    depositLienTO.setAuthorizeDt((Date)currDt.clone());
                                                    lienTOs.add(depositLienTO);
                                                    System.out.println("lienTOs: " + lienTOs);
                                                    depositLienTO = null;
                                                }
                                            }
                                            lienMap = null;
                                        }
                                        //                                    sqlMap.executeUpdate("InsForTempDepositLienTable", "");
                                        HashMap oldLienMap = new HashMap();
                                        if (!oldDepNo.equals(newDepNo)) {
                                            map.put(CommonConstants.BRANCH_ID, branchID);
                                            map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                            map.put("lienTOs", lienTOs);
                                            map.put("SHADOWLIEN", new Double(totAmt));
                                            System.out.println("map : " + map);
                                            depositLienDAO.setCallFromOtherDAO(true);
                                            depositLienDAO.execute(map);
                                            HashMap updateMap = new HashMap();
                                            updateMap.put("LIENAMOUNT", new Double(totAmt));
                                            updateMap.put("DEPOSIT_ACT_NUM", newDepNo);
                                            updateMap.put("SHADOWLIEN", new Double(totAmt));
                                            updateMap.put("SUBNO", CommonUtil.convertObjToInt(subNo));
                                            sqlMap.executeUpdate("updateSubAcInfoBal", updateMap);
                                            updateMap.put("TODAY_DT",(Date)currDt.clone());
                                            updateMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("updateSubACInfoStatusToLien", updateMap);
                                            HashMap zeroMap = new HashMap();
                                            zeroMap.put("DEPOSIT_NO", oldDepNo);
                                            zeroMap.put("AVAILABLE_BALANCE", new Double(0.0));
                                            zeroMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                            sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                            oldLienMap.put("DEPOSIT_NO", oldDepNo);
                                            oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                            oldLienMap.put("STATUS", CommonConstants.STATUS_UNLIEN);
                                            oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                            oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                            oldLienMap = new HashMap();
                                            oldLienMap.put("DEPOSIT_NO", newDepNo);
                                            oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                            oldLienMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                            oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                            oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                            zeroMap = new HashMap();
                                            zeroMap.put("DEPOSIT_NO", newDepNo);
                                            zeroMap.put("AVAILABLE_BALANCE", new Double(totAmt));
                                            zeroMap.put("STATUS", "LIEN");
                                            sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                            updateMap = null;
                                            zeroMap = null;
                                        } else {
                                            oldLienMap = new HashMap();
                                            oldLienMap.put("DEPOSIT_NO", oldDepNo);
                                            oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                            oldLienMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                            oldLienMap.put("AUTHORIZE_DT", (Date)currDt.clone());
                                            oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                        }
                                        oldLienMap = null;
                                    }
                                }
                                lienDetMap = null;
                            }
                            System.out.println("oldDepNo=================PPP :"+oldDepNo);
                            if (oldDepNo != null && oldDepNo.length() > 0) {//this is storing for deposit_sub_acinfo_sameno table.
                                HashMap statusMap = new HashMap();
                                statusMap.put(DEPOSITNO, oldDepNo);
                                statusMap.put("ACCOUNTNO", oldDepNo);
                                statusMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                statusMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                statusMap.put("AUTHORIZEDT", currDt.clone());
                                System.out.println("statusMap oldDepNo:" + statusMap);
                                statusMap.put("TOTAL_BALANCE", new Double(0));
                                statusMap.put("CLEAR_BALANCE", new Double(0));
                                statusMap.put("AVAILABLE_BALANCE", new Double(0));
                                sqlMap.executeUpdate("authorizeSameDepositCloseNoBal", statusMap);
                                statusMap = null;
                            }
//                            depositMap = null;
                            subMap = null;
                            accountHeadMap = null;
                            interestBatchTO = null;
                            balanceMap = null;
                            oldprodMap = null;
                        } else {
                            System.out.println("****** depositMap Reject: " + depositMap);
                            oldDepNo = CommonUtil.convertObjToStr(depositMap.get("RENEWAL_FROM_DEPOSIT"));
                            newDepNo = CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_NO"));
                            subNo = CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_SUB_NO"));
                            oldDepositNo = oldDepNo + "_" + subNo;
                            newDepositNo = newDepNo + "_" + subNo;
                            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            dataMap.put("CURR_DATE", currDt.clone());
                            System.out.println("****** dataMap else:" + dataMap);
                            HashMap renewalAuthMap = new HashMap();
                            renewalAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                            renewalAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            renewalAuthMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                            System.out.println("renewalAuthMap : " + renewalAuthMap + "oldDepositNo" + oldDepositNo + "status" + status);
                            renewalAuthMap = null;
                            HashMap statusMap = new HashMap();
                            if (oldDepNo != null && oldDepNo.length() > 0) {
                                statusMap.put("DEPOSITNO", oldDepNo);
                                if (oldDepNo.equals(newDepNo)) {
                                    depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                                    HashMap statusSameMap = new HashMap();
                                    statusSameMap.put(DEPOSITNO, oldDepNo);
                                    List lst = (List) sqlMap.executeQueryForList("getSelectSameDepSubNoAccInfoTO", statusSameMap);
                                    if (lst != null && lst.size() > 0) {
                                        statusSameMap = (HashMap) lst.get(0);
                                        System.out.println("getSelectSameDepSubNoAccInfoTO :" + statusSameMap );
                                        depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(statusSameMap.get("DEPOSIT_NO")));
                                        depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(statusSameMap.get("DEPOSIT_SUB_NO")));
                                        depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusSameMap.get("DEPOSIT_DT"))));
                                        depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(statusSameMap.get("DEPOSIT_PERIOD_DD")));
                                        depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(statusSameMap.get("DEPOSIT_PERIOD_MM")));
                                        depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(statusSameMap.get("DEPOSIT_PERIOD_YY")));
                                        depSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(statusSameMap.get("DEPOSIT_AMT")));
                                        depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(statusSameMap.get("INTPAY_MODE")));
                                        depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(statusSameMap.get("INTPAY_FREQ")));
                                        depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusSameMap.get("MATURITY_DT"))));
                                        depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(statusSameMap.get("RATE_OF_INT")));
                                        depSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(statusSameMap.get("MATURITY_AMT")));
                                        depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(statusSameMap.get("TOT_INT_AMT")));
                                        depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(statusSameMap.get("PERIODIC_INT_AMT")));
                                        depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(statusSameMap.get("STATUS")));
                                        depSubNoAccInfoTO.setClearBalance(CommonUtil.convertObjToDouble(statusSameMap.get("CLEAR_BALANCE")));
                                        depSubNoAccInfoTO.setAvailableBalance(CommonUtil.convertObjToDouble(statusSameMap.get("AVAILABLE_BALANCE")));
                                        depSubNoAccInfoTO.setCloseDt((Date)currDt.clone());
                                        depSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                        depSubNoAccInfoTO.setAuthorizeDt((Date)currDt.clone());
                                        depSubNoAccInfoTO.setLastTransDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_TRANS_DT"))));
                                        depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(statusMap.get("LAST_INT_APPL_DT"))));
                                        depSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(statusSameMap.get("TOTAL_INT_CREDIT")));
                                        depSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(statusSameMap.get("TOTAL_INT_DRAWN")));
                                        depSubNoAccInfoTO.setTotalBalance(CommonUtil.convertObjToDouble(statusSameMap.get("TOTAL_BALANCE")));
                                        depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(statusSameMap.get("INSTALL_TYPE")));
                                        depSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(statusSameMap.get("POSTAGE_AMT")));
                                        sqlMap.executeUpdate("updateDepSubNoAccInfoTO", depSubNoAccInfoTO);
                                        HashMap intMap = new HashMap();
                                        intMap.put("DEPOSIT_NO", oldDepNo);
                                        lst = sqlMap.executeQueryForList("getSelectDepSubNoIntDetailsSameNo", intMap);
                                        if (lst != null && lst.size() > 0) {
                                            intMap = (HashMap) lst.get(0);
                                            System.out.println("getSelectDepSubNoIntDetailsSameNo :" + intMap);
                                            HashMap slNoMap = new HashMap();
                                            slNoMap.put("DEPOSIT_NO", oldDepNo);
                                            if (depSubNoAccInfoTO.getIntpayMode().equals("TRANSFER")) {
                                                slNoMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(intMap.get("INT_PAY_ACC_NO")));
                                                slNoMap.put("INT_PAY_PROD_ID", CommonUtil.convertObjToStr(intMap.get("INT_PAY_PROD_ID")));
                                                slNoMap.put("INT_PAY_PROD_TYPE", CommonUtil.convertObjToStr(intMap.get("INT_PAY_PROD_TYPE")));
                                                sqlMap.executeUpdate("updateDepSubNoAccInfoTONo", slNoMap);
                                            } else if (depSubNoAccInfoTO.getIntpayMode().equals("CASH")) {
                                                System.out.println("slNoMap :" + slNoMap);
                                                HashMap nullMap = new HashMap();
                                                nullMap.put("DEPOSIT_NO", oldDepNo);
                                                sqlMap.executeUpdate("updateDepSubNoAccInfoTONoInterestNull", nullMap);
                                                nullMap = null;
                                            }
                                        }
                                        intMap = null;
                                    }
                                    statusSameMap = null;
                                    if (oldDepNo != null && oldDepNo.length() > 0) {
                                        System.out.println("****** dataMap oldDepNo:" + dataMap);
                                        statusMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                        statusMap.put(DEPOSITNO, oldDepNo);
                                        statusMap.put("ACCOUNTNO", oldDepNo);
                                        statusMap.put("ACCT_STATUS", "NEW");
                                        statusMap.put("DEP_STATUS", "NEW");
                                        statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        statusMap.put("AUTHORIZEDT", currDt.clone());
                                        System.out.println("statusMap oldDepNo:" + statusMap);
                                        sqlMap.executeUpdate("authorizeSameDepositCloseBal", statusMap);
                                    }
                                    statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    statusMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                    if (count == 0) {
                                        statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt("0"));
                                    } else {
                                        //statusMap.put("RENEWAL_COUNT", String.valueOf(count - 1));
                                        statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt(count - 1));
                                    }
                                    sqlMap.executeUpdate("updateNewDepositAuthorizeAcInfo", statusMap);
                                    if (oldDepNo != null && oldDepNo.length() > 0) {//this is storing for deposit_sub_acinfo_sameno table.
                                        statusMap = new HashMap();
                                        statusMap.put(DEPOSITNO, oldDepNo);
                                        statusMap.put("ACCOUNTNO", oldDepNo);
                                        statusMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                                        statusMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                        statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                        statusMap.put("AUTHORIZEDT", currDt.clone());
                                        System.out.println("statusMap oldDepNo:" + statusMap);
                                        statusMap.put("TOTAL_BALANCE", new Double(0));
                                        statusMap.put("CLEAR_BALANCE", new Double(0));
                                        statusMap.put("AVAILABLE_BALANCE", new Double(0));
                                        sqlMap.executeUpdate("authorizeSameDepositCloseNoBal", statusMap);
                                        statusMap = null;
                                        HashMap renewalNewUpdate = new HashMap();
                                        renewalNewUpdate.put("MAT_ALERT_REPORT", "Y");
                                        renewalNewUpdate.put("AUTO_RENEWAL", "N");
                                        renewalNewUpdate.put("RENEW_WITH_INT", "N");
                                        renewalNewUpdate.put("DEPOSIT_NO", oldDepNo);
                                        System.out.println("renewalNewUpdate : " + renewalNewUpdate);
                                        sqlMap.executeUpdate("updateRenewalStatusBefAuth", renewalNewUpdate);
                                    }
                                } else {
                                    List lst = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", statusMap);
                                    if (lst != null && lst.size() > 0) {
                                        statusMap = (HashMap) lst.get(0);
                                        if (newDepNo != null && newDepNo.length() > 0) {
                                            dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_REJECTED);
                                            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            dataMap.put("CURR_DATE", currDt.clone());
                                            dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                            dataMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                                            dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                            System.out.println("lst output oldDepNo.equals:" + dataMap);
                                            sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                            sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                            statusMap.put(DEPOSITNO, newDepNo);
                                            statusMap.put("DEP_STATUS", "NEW");
                                            statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                            statusMap.put("CLOSE_DT", currDt.clone());
                                            statusMap.put("CLOSE_BY", map.get(CommonConstants.USER_ID));
                                            sqlMap.executeUpdate("updateNewDepositAuthorizeSubAcinfo", statusMap);
                                            statusMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                                            if (count == 0) {
                                                statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt("0"));
                                            } else {
                                                statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt(count));
                                            }
                                            sqlMap.executeUpdate("updateNewDepositAuthorizeAcInfo", statusMap);
                                            HashMap nomineeMap = new HashMap();
                                            nomineeMap.put("DEPOSIT_NO", newDepNo);
                                            nomineeMap.put("STATUS", CommonConstants.STATUS_DELETED);
                                            nomineeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                            nomineeMap.put("CURR_DATE", currDt.clone());
                                            sqlMap.executeUpdate("StatusUpdationinTD", nomineeMap);
                                            nomineeMap = null;
                                        }
                                        if (oldDepNo != null && oldDepNo.length() > 0) {
                                            dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
                                            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            dataMap.put("CURR_DATE", currDt.clone());
                                            dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                            dataMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                            dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                            dataMap.put(DEPOSITNO, oldDepNo);
                                            System.out.println("statusMap oldDepNo.equals:" + dataMap);
                                            sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                            sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                            statusMap.put("DEP_STATUS", "NEW");
                                            statusMap.put(DEPOSITNO, oldDepNo);
                                            statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                            sqlMap.executeUpdate("updateOldDepositAuthorizeSubAcinfo", statusMap);
                                            sqlMap.executeUpdate("updateOldDepositAuthorizeAcInfo", statusMap);
                                        }
                                        if (oldDepNo != null && oldDepNo.length() > 0) {//this is storing for deposit_sub_acinfo_sameno table.
                                            statusMap = new HashMap();
                                            statusMap.put(DEPOSITNO, oldDepNo);
                                            statusMap.put("ACCOUNTNO", oldDepNo);
                                            statusMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                                            statusMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                            statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                            statusMap.put("AUTHORIZEDT", currDt.clone());
                                            System.out.println("statusMap oldDepNo:" + statusMap);
                                            statusMap.put("TOTAL_BALANCE", new Double(0));
                                            statusMap.put("CLEAR_BALANCE", new Double(0));
                                            statusMap.put("AVAILABLE_BALANCE", new Double(0));
                                            sqlMap.executeUpdate("authorizeSameDepositCloseNoBal", statusMap);
                                            statusMap = null;
                                        }
                                    }
                                }
                            }
                            HashMap lienDetMap = new HashMap();
                            depositLienDAO = new DepositLienDAO();
                            if (oldDepNo != null && oldDepNo.length() > 0) {
                                lienDetMap.put("DEPOSIT_NO", oldDepNo);
                                branchID = CommonUtil.convertObjToStr(depositMap.get("BRANCH_ID"));
                                List getList = sqlMap.executeQueryForList("getDepositLienDetInsert", lienDetMap);
                                if (getList != null && getList.size() > 0) {
                                    double totAmt = 0.0;
                                    System.out.println("getList : " + getList);
                                    for (int j = 0; j < getList.size(); j++) {
                                        depositLienTO = (DepositLienTO) getList.get(j);
                                        HashMap lienMap = new HashMap();
                                        lienMap.put("PROD_ID", depositLienTO.getLienProdId());
                                        List lstHead = sqlMap.executeQueryForList("getAccountHeadForLoansLien", lienMap);
                                        if (lstHead != null && lstHead.size() > 0) {
                                            lienMap = (HashMap) lstHead.get(0);
                                            System.out.println("lienMap : " + lienMap);
                                            if (!lienMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS") || !(oldDepNo.equals(newDepNo))) {
                                                HashMap oldLienMap = new HashMap();
                                                double amount = CommonUtil.convertObjToDouble(depositLienTO.getLienAmount()).doubleValue();
                                                totAmt = amount + totAmt;
                                                oldLienMap.put("DEPOSIT_NO", oldDepNo);
                                                oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                                oldLienMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                                oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                                oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                                sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                                HashMap zeroMap = new HashMap();
                                                zeroMap.put("DEPOSIT_NO", oldDepNo);
                                                zeroMap.put("AVAILABLE_BALANCE", new Double(totAmt));
                                                zeroMap.put("STATUS", "LIEN");
                                                sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                                oldLienMap = null;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (oldDepositNo != null && status != null) {
                            boolean passBookflag = false;
                            HashMap passBookMap = new HashMap();
                            if (status != null && status.equals("AUTHORIZED")) {
                                passBookMap.put("LINK_BATCH_ID", oldDepositNo);
                                List lst = sqlMap.executeQueryForList("getselectTransFromDeposit", passBookMap);
                                if (lst != null && lst.size() > 0) {
                                    passBookMap = (HashMap) lst.get(0);
                                    passBookflag = true;
                                }
                            }
                            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                            HashMap renewalTransactionMap = new HashMap();
                            renewalTransactionMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                            renewalTransactionMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            renewalTransactionMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                            //Commented by sreekrishnan
                            //Added by Sreekrishnan
                            //List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                            //if (listData != null && listData.size() > 0) {
                           //     HashMap map1 = (HashMap) listData.get(0);
                           //     if(map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                           //         renewalTransactionMap.put("GOLD_LOAN_CLOSING_TRANSACTION", "GOLD_LOAN_CLOSING_TRANSACTION");  
                           //     }
                           // }
                            if (map.containsKey("DEPOSIT_MULTIPLE_RENEWAL")) { //Added By Kannan AR
                                renewalTransactionMap.put("DEPOSIT_MULTIPLE_RENEWAL", "DEPOSIT_MULTIPLE_RENEWAL");
                            }
                            transactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(oldDepositNo, status, renewalTransactionMap);
                            HashMap transMap = new HashMap();
                            transMap.put("LINK_BATCH_ID", oldDepositNo);
                            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                            if (passBookflag == true) {
                                passBookMap.put("TRANS_DT", currDt.clone());
                                sqlMap.executeUpdate("updateNullAsTransFromDeposit", passBookMap);
                                passBookflag = false;
                            }
//                            renewalTempMap = new HashMap();
//                            renewalTempMap.put("DEPOSIT_NO",newDepNo);
//                            sqlMap.executeUpdate("updateTempStatusExtension", renewalTempMap);
                            HashMap renewTempMap = new HashMap();
                            renewTempMap.put("DEPOSIT_NO", newDepNo);
                            renewTempMap.put("OLD_DEPOSIT_NO", oldDepNo);
                            sqlMap.executeUpdate("updateTempStatus", renewTempMap);
                            renewTempMap = null;
                            HashMap payOrderMap = new HashMap();
                            payOrderMap.put("REMARKS", oldDepNo);
                            List lstPay = sqlMap.executeQueryForList("getSelectDepositPayOrder", payOrderMap);
                            if (lstPay != null && lstPay.size() > 0) {
                                HashMap selectMap = (HashMap) lstPay.get(0);
                                String variableNo = CommonUtil.convertObjToStr(selectMap.get("VARIABLE_NO"));
                                renewalTransactionMap = new HashMap();
                                renewalTransactionMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                                renewalTransactionMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                renewalTransactionMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                                if(map.containsKey("DEPOSIT_MULTIPLE_RENEWAL")){ //Added By Kannan AR
                                    renewalTransactionMap.put("DEPOSIT_MULTIPLE_RENEWAL", "DEPOSIT_MULTIPLE_RENEWAL");
                                }                                
                                transactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(variableNo, status, renewalTransactionMap);
                                HashMap remitAuthMap = new HashMap();
                                remitAuthMap.put("STATUS", status);
                                remitAuthMap.put("AUTHORIZE_USER", map.get(CommonConstants.USER_ID));
                                remitAuthMap.put("TODAY_DT", currDt.clone());
                                remitAuthMap.put("BATCH_ID", selectMap.get("BATCH_ID"));
                                sqlMap.executeUpdate("authorizeRemitIssue", remitAuthMap);
                            }
                            transMap = null;
                            renewalTempMap = null;
                            renewalTransactionMap = null;
                        }
                    } else {
                        System.out.println("else part ****** newDepNo : " + newDepNo);
                        if (newDepNo != null && newDepNo.length() > 0) {
                            balanceMap.put(CommonConstants.STATUS, status);
                            balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            balanceMap.put("CURR_DATE", currDt.clone());
                            balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                            balanceMap.put("STATUS", status);
                            balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                            sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                            sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                        }
                    }
                    /*
                     * ------------------------------------extension deposit authorization-------------------------------------------------------------------
                     */
                    HashMap extensionAuthMap = new HashMap();
                    extensionAuthMap.put("DEPOSIT_NO", dataMap.get(DEPOSITNO));
                    list = (List) sqlMap.executeQueryForList("getSelectOldDepositExtensionDetails", extensionAuthMap);
                    System.out.println("#@#@#@#@#@#@#@#@#@#@  listlist1  "+list);
                System.out.println("#@#@#@#@#@#@#@#@#@#@  list size1  "+list.size());
                    if (list != null && list.size() > 0) {
                        extensionAuthMap = (HashMap) list.get(0);
                        System.out.println("getSelectRenewalDetails :" + extensionAuthMap);
                        branchID = CommonUtil.convertObjToStr(extensionAuthMap.get("BRANCH_CODE"));
                        oldDepNo = CommonUtil.convertObjToStr(extensionAuthMap.get("OLD_DEPOSIT_NO"));
                        newDepNo = CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_DEPOSIT_NO"));
                        subNo = CommonUtil.convertObjToStr(extensionAuthMap.get("SL_NO"));
                        oldDepositNo = oldDepNo + "_" + 1;
                        newDepositNo = newDepNo + "_" + 1;
                        System.out.println("newDepositNo:" + newDepositNo + "oldDepositNo:" + oldDepositNo + "subNo:" + subNo);
                        if (status != null && status.equals("AUTHORIZED")) {
                            subMap = new HashMap();
                            subMap.put(DEPOSITNO, oldDepNo);
                            list = (List) sqlMap.executeQueryForList("getShadowDebitAmtSameNo", subMap);
                            if (list != null && list.size() > 0) {
                                subMap = (HashMap) list.get(0);
                                String oldBehaves = null;
                                String newBehaves = null;
                                String oldProdId = CommonUtil.convertObjToStr(subMap.get("PROD_ID"));
                                String newProdId = CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_PROD_ID"));
                                oldprodMap = new HashMap();
                                oldprodMap.put("PROD_ID", subMap.get("PROD_ID"));
                                List lstProd = sqlMap.executeQueryForList("getDepositClosingHeads", oldprodMap);
                                if (lstProd != null && lstProd.size() > 0) {
                                    oldprodMap = (HashMap) lstProd.get(0);
                                    newBehaves = CommonUtil.convertObjToStr(depositMap.get("BEHAVES_LIKE"));
                                    System.out.println("oldprodMap :" + oldprodMap + "oldBehaves :" + oldBehaves + "newBehaves :" + newBehaves);
                                    double closingAmt = CommonUtil.convertObjToDouble(extensionAuthMap.get("RECALCUALTE_INTEREST_AMT")).doubleValue();
                                    double paid = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    double debitInt = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                    interestBatchTO.setApplDt((Date)currDt.clone());
                                    interestBatchTO.setActNum(oldDepositNo);
                                    interestBatchTO.setProductId(CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_PROD_ID")));
                                    interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(extensionAuthMap.get("EXTENSION_DEPOSIT_AMT")));
                                    interestBatchTO.setCustId(CommonUtil.convertObjToStr(extensionAuthMap.get("CUST_ID")));
                                    interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(extensionAuthMap.get("EXTENSION_RATE_OF_INT")));
                                    interestBatchTO.setIntDt((Date)currDt.clone());
                                    if (oldprodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                        interestBatchTO.setIntType("SIMPLE");
                                    } else {
                                        interestBatchTO.setIntType("COMPOUND");
                                    }
                                    interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(oldprodMap.get("INT_PAY")));
                                    interestBatchTO.setProductType(SCREEN);
                                    interestBatchTO.setTrnDt((Date)currDt.clone());
                                    double balance = paid - debitInt;
                                    HashMap renewalCountMap = new HashMap();
                                    renewalCountMap.put("ACT_NUM", dataMap.get(DEPOSITNO));
                                    List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                                    if (lstCount != null && lstCount.size() > 0) {
                                        renewalCountMap = (HashMap) lstCount.get(0);
                                        interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                                    }
                                    System.out.println("paid :" + paid + "debitInt :" + debitInt + "closingAmt" + closingAmt + "balance :" + balance);
                                    double bal = 0.0;
                                    if (closingAmt < 0) {
                                        System.out.println("ClosingAmt<0 if :" + closingAmt);
                                        if (closingAmt > balance) {
                                            System.out.println("closingAmt<balance IF :" + closingAmt);
                                            interestBatchTO.setDrCr("DEBIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                            interestBatchTO.setTot_int_amt(new Double(closingAmt * -1));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            interestBatchTO.setDrCr("CREDIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                            interestBatchTO.setTot_int_amt(new Double(0));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        } else {
                                            System.out.println("closingAmt<balance ELSE :" + closingAmt);
                                            interestBatchTO.setDrCr("CREDIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                            interestBatchTO.setTot_int_amt(new Double(closingAmt * -1));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            interestBatchTO.setDrCr("DEBIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            if (balance == 0) {
                                                interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                            } else {
                                                interestBatchTO.setIntAmt(new Double(balance + (closingAmt * -1)));
                                            }
                                            interestBatchTO.setTot_int_amt(new Double(0));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        }
                                    } else if (closingAmt > 0) {
                                        if (closingAmt > balance) {
                                            System.out.println("closingAmt>balance IF :" + closingAmt);
                                            interestBatchTO.setDrCr("DEBIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(closingAmt));
                                            interestBatchTO.setTot_int_amt(new Double(balance));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            interestBatchTO.setDrCr("CREDIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(balance));
                                            interestBatchTO.setTot_int_amt(new Double(0));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        } else {
                                            System.out.println("closingAmt>balance ELSE :" + closingAmt);
                                            interestBatchTO.setDrCr("CREDIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(closingAmt));
                                            interestBatchTO.setTot_int_amt(new Double(balance));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                            interestBatchTO.setDrCr("DEBIT");
                                            interestBatchTO.setTransLogId("Payable");
                                            interestBatchTO.setIntAmt(new Double(balance - closingAmt));
                                            interestBatchTO.setTot_int_amt(new Double(0));
                                            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        }
                                    }
                                }
                                balanceMap = new HashMap();
                                balanceMap.put("DEPOSIT_NO", oldDepNo);
                                totIntAmt = totIntAmt;
                                balanceMap.put("INT_AMT", new Double(totIntAmt));
                                sqlMap.executeUpdate("updateClosingTotIndAmount", balanceMap);
                                sqlMap.executeUpdate("updateClosingBalIntAmount", balanceMap);
                                if (oldDepNo != null && oldDepNo.length() > 0) {
                                    HashMap closingMap = new HashMap();
                                    closingMap.put("STATUS", status);
                                    closingMap.put("USER_ID", dataMap.get("USER_ID"));
                                    closingMap.put("AUTHORIZE_DT", (Date)currDt.clone());
                                    closingMap.put("ACCOUNTNO", oldDepositNo);
                                    sqlMap.executeUpdate("authorizeDepositAccountClose", closingMap);
                                    closingMap.put("ACCOUNTNO", oldDepNo);
                                    sqlMap.executeUpdate("authorizeDepositSubAccountClose", closingMap);
                                    closingMap = null;
                                }
                                if (newDepNo != null && newDepNo.length() > 0) {
                                    balanceMap.put(CommonConstants.STATUS, status);
                                    balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    balanceMap.put("CURR_DATE", (Date)currDt.clone());
                                    balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                    balanceMap.put("STATUS", status);
                                    balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                    sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                                    sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                                    HashMap newDep = new HashMap();
                                    newDep.put("NORM_STATUS", CommonConstants.STATUS_CREATED);
                                    newDep.put(DEPOSITNO, newDepNo);
                                    newDep.put("PAYMENT_DAY", (Date)currDt.clone());
                                    sqlMap.executeUpdate("authorizeDepositNewSubAccInfo", newDep);
                                    newDep = null;
                                }
                            }
                            if (oldDepNo != null && oldDepNo.length() > 0) {
                                HashMap lienDetMap = new HashMap();
                                lienDetMap.put("DEPOSIT_NO", oldDepNo);
                                List getList = sqlMap.executeQueryForList("getDepositLienDetInsert", lienDetMap);
                                if (getList != null && getList.size() > 0) {
                                    double totAmt = 0.0;
                                    depositLienDAO = new DepositLienDAO();
                                    ArrayList lienTOs = new ArrayList();
                                    branchID = CommonUtil.convertObjToStr(extensionAuthMap.get("BRANCH_CODE"));
                                    System.out.println("getList : " + getList);
                                    for (int j = 0; j < getList.size(); j++) {
                                        depositLienTO = (DepositLienTO) getList.get(j);
                                        HashMap lienMap = new HashMap();
                                        lienMap.put("LIEN_AC_HD", depositLienTO.getLienAcHd());
                                        lienMap.put("PROD_ID", depositLienTO.getLienProdId());
                                        List lstHead = sqlMap.executeQueryForList("getAccountHeadForLoansLien", lienMap);
                                        if (lstHead != null && lstHead.size() > 0) {
                                            lienMap = (HashMap) lstHead.get(0);
                                            System.out.println("lienMap : " + lienMap);
                                            if (!lienMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS") || !(oldDepNo.equals(newDepNo))) {
                                                double amount = CommonUtil.convertObjToDouble(depositLienTO.getLienAmount()).doubleValue();
                                                totAmt = amount + totAmt;
                                                depositLienTO.setLienNo("-");
                                                depositLienTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO)));
                                                depositLienTO.setLienDt((Date)currDt.clone());
                                                depositLienTO.setStatus(CommonConstants.STATUS_CREATED);
                                                depositLienTO.setUnlienDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
                                                depositLienTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                                depositLienTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                                depositLienTO.setStatusDt((Date)currDt.clone());
                                                depositLienTO.setAuthorizeDt((Date)currDt.clone());
                                                lienTOs.add(depositLienTO);
                                                System.out.println("lienTOs: " + lienTOs);
                                                depositLienTO = null;
                                            }
                                        }
                                        lienMap = null;
                                    }
                                    map.put(CommonConstants.BRANCH_ID, branchID);
                                    map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                    map.put("lienTOs", lienTOs);
                                    map.put("SHADOWLIEN", new Double(totAmt));
                                    System.out.println("map : " + map);
                                    depositLienDAO.setCallFromOtherDAO(true);
                                    depositLienDAO.execute(map);
                                    HashMap updateMap = new HashMap();
                                    updateMap.put("LIENAMOUNT", new Double(totAmt));
                                    updateMap.put("DEPOSIT_ACT_NUM", newDepNo);
                                    updateMap.put("SHADOWLIEN", new Double(totAmt));
                                    updateMap.put("SUBNO", CommonUtil.convertObjToInt(subNo));
                                    sqlMap.executeUpdate("updateSubAcInfoBal", updateMap);
                                    updateMap.put("TODAY_DT", currDt.clone());
                                    updateMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                    sqlMap.executeUpdate("updateSubACInfoStatusToLien", updateMap);
                                    HashMap zeroMap = new HashMap();
                                    zeroMap.put("DEPOSIT_NO", oldDepNo);
                                    zeroMap.put("AVAILABLE_BALANCE", new Double(0.0));
                                    zeroMap.put("STATUS", "LIEN");
                                    sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                    HashMap oldLienMap = new HashMap();
                                    oldLienMap.put("DEPOSIT_NO", oldDepNo);
                                    oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                    oldLienMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                    oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                    sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                    oldLienMap = new HashMap();
                                    oldLienMap.put("DEPOSIT_NO", newDepNo);
                                    oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                                    oldLienMap.put("STATUS", CommonConstants.STATUS_UNLIEN);
                                    oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                    oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));

                                    sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                    zeroMap = new HashMap();
                                    zeroMap.put("DEPOSIT_NO", newDepNo);
                                    zeroMap.put("AVAILABLE_BALANCE", new Double(0.0));
                                    zeroMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                    updateMap = null;
                                    zeroMap = null;
                                    lienDetMap = null;
                                    oldLienMap = null;
                                    updateMap = null;
                                }
                                getList = null;
                            }
                            if (oldDepNo != null && oldDepNo.length() > 0) {//this is updating for deposit_sub_acinfo_sameno table.
                                HashMap statusMap = new HashMap();
                                statusMap.put(DEPOSITNO, oldDepNo);
                                statusMap.put("ACCOUNTNO", oldDepNo);
                                statusMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                statusMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                statusMap.put("AUTHORIZEDT", currDt.clone());
                                System.out.println("statusMap  oldDepNo:" + statusMap);
                                statusMap.put("TOTAL_BALANCE", new Double(0));
                                statusMap.put("CLEAR_BALANCE", new Double(0));
                                statusMap.put("AVAILABLE_BALANCE", new Double(0));
                                sqlMap.executeUpdate("authorizeSameDepositCloseNoBal", statusMap);
                                statusMap = null;
                            }
                            depositMap = null;
                            subMap = null;
                            accountHeadMap = null;
                            interestBatchTO = null;
                            balanceMap = null;
                            oldprodMap = null;
                            subMap = null;
                        } else {
                            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            dataMap.put("CURR_DATE", currDt.clone());
                            System.out.println("depositMap Reject:" + depositMap + "dataMap :" + dataMap);
                            HashMap statusMap = new HashMap();
                            statusMap.put("DEPOSITNO", oldDepNo);
                            List lst = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", statusMap);
                            if (lst != null && lst.size() > 0) {
                                statusMap = (HashMap) lst.get(0);
                                if (oldDepNo != null && oldDepNo.length() > 0) {//this is storing for deposit_sub_acinfo_sameno table.
                                    statusMap = new HashMap();
                                    statusMap.put(DEPOSITNO, oldDepNo);
                                    sqlMap.executeUpdate("updateExtensionDeleteSamenoRec", statusMap);
                                    sqlMap.executeUpdate("updateExtensionDeleteTempTable", statusMap);
                                }
                                if (newDepNo != null && newDepNo.length() > 0) {
                                    dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_REJECTED);
                                    dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    dataMap.put("CURR_DATE", currDt.clone());
                                    dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                    dataMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                                    dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                    System.out.println("lst output oldDepNo.equals:" + dataMap);
                                    sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                    sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                    statusMap.put(DEPOSITNO, newDepNo);
                                    statusMap.put("DEP_STATUS", CommonConstants.CLOSED);
                                    statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    statusMap.put("CLOSE_DT", currDt.clone());
                                    statusMap.put("CLOSE_BY", map.get(CommonConstants.USER_ID));
                                    sqlMap.executeUpdate("updateNewDepositAuthorizeSubAcinfo", statusMap);
                                    statusMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                                    statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt("0"));
                                    sqlMap.executeUpdate("updateNewDepositAuthorizeAcInfo", statusMap);
                                }
                                if (oldDepNo != null && oldDepNo.length() > 0) {
                                    dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
                                    dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                    dataMap.put("CURR_DATE", currDt.clone());
                                    dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                    dataMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                    dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                    dataMap.put(DEPOSITNO, oldDepNo);
                                    System.out.println("statusMap oldDepNo.equals:" + dataMap);
                                    sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                    sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                    statusMap.put("DEP_STATUS", "NEW");
                                    statusMap.put(DEPOSITNO, oldDepNo);
                                    statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    sqlMap.executeUpdate("updateOldDepositAuthorizeSubAcinfo", statusMap);
                                    sqlMap.executeUpdate("updateOldDepositAuthorizeAcInfo", statusMap);
                                }
                            }
                            statusMap = null;
                        }
                        if (oldDepositNo != null && status != null) {
                            boolean passBookflag = false;
                            HashMap passBookMap = new HashMap();
                            if (status != null && status.equals("AUTHORIZED")) {
                                passBookMap.put("LINK_BATCH_ID", oldDepositNo);
                                List lst = sqlMap.executeQueryForList("getselectTransFromDeposit", passBookMap);
                                if (lst != null && lst.size() > 0) {
                                    passBookMap = (HashMap) lst.get(0);
                                    passBookflag = true;
                                }
                            }
                            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                            HashMap extensionTransactionMap = new HashMap();
                            extensionTransactionMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                            extensionTransactionMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            extensionTransactionMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                            System.out.println("extensionTransactionMap@@@" + extensionTransactionMap);
                            System.out.println("extensionTransactionMap@@@map" + map);
                            transactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(oldDepositNo, status, extensionTransactionMap);
                            HashMap transMap = new HashMap();
                            transMap.put("LINK_BATCH_ID", oldDepositNo);
                            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                            if (passBookflag == true) {
                                passBookMap.put("TRANS_DT", currDt.clone());
                                sqlMap.executeUpdate("updateNullAsTransFromDeposit", passBookMap);
                                passBookflag = false;
                            }
                            renewalTempMap = new HashMap();
                            renewalTempMap.put("DEPOSIT_NO", newDepNo);
                            sqlMap.executeUpdate("updateTempStatusExtension", renewalTempMap);
                            transMap = null;
                        }
                        renewalTempMap = null;
                    }
                    
                    HashMap mobileMap = new HashMap();
                    mobileMap.put("ACCOUNTNO",depositMap.get("DEPOSIT_NO"));
                    mobileMap.put("PROD_TYPE",TransactionFactory.DEPOSITS);
                    mobileMap.put("PROD_ID",depositMap.get("PROD_ID"));
                    mobileMap.put("USER_ID",map.get("USER_ID"));
                    mobileMap.put("AUTHORIZEDT",currDt.clone());
                    mobileMap.put("STATUS",status);
                    sqlMap.executeUpdate("authorizeSMSSubscriptionMap", mobileMap);
                    objAccInfoTO = null;
                    list = null;
                    extensionAuthMap = null;
                } else {
                    System.out.println("else part ****** newDepNo : " + newDepNo);
                    if (newDepNo != null && newDepNo.length() > 0) {
                        balanceMap.put(CommonConstants.STATUS, status);
                        balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        balanceMap.put("CURR_DATE", currDt.clone());
                        balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                        balanceMap.put("STATUS", status);
                        balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                        sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                        sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                    }
                }
                /*
                 * ------------------------------------extension deposit authorization-------------------------------------------------------------------
                 */
                HashMap extensionAuthMap = new HashMap();
                HashMap renewalTempMap = null;
                extensionAuthMap.put("DEPOSIT_NO", dataMap.get(DEPOSITNO));
                list = (List) sqlMap.executeQueryForList("getSelectOldDepositExtensionDetails", extensionAuthMap);
                System.out.println("#@#@#@#@#@#@#@#@#@#@  listlist  "+list);
                System.out.println("#@#@#@#@#@#@#@#@#@#@  list size  "+list.size());
                if (list != null && list.size() > 0) {
                    extensionAuthMap = (HashMap) list.get(0);
                    System.out.println("getSelectRenewalDetails :" + extensionAuthMap);
                    String branchID = CommonUtil.convertObjToStr(extensionAuthMap.get("BRANCH_CODE"));
                    oldDepNo = CommonUtil.convertObjToStr(extensionAuthMap.get("OLD_DEPOSIT_NO"));
                    newDepNo = CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_DEPOSIT_NO"));

                    subNo = CommonUtil.convertObjToStr(extensionAuthMap.get("SL_NO"));
                    String oldDepositNo = oldDepNo + "_" + 1;
                    String newDepositNo = newDepNo + "_" + 1;
                    System.out.println("newDepositNo:" + newDepositNo + "oldDepositNo:" + oldDepositNo + "subNo:" + subNo);
                    if (status != null && status.equals("AUTHORIZED")) {
                        subMap = new HashMap();
                        subMap.put(DEPOSITNO, oldDepNo);
                        list = (List) sqlMap.executeQueryForList("getShadowDebitAmtSameNo", subMap);
                        if (list != null && list.size() > 0) {
                            subMap = (HashMap) list.get(0);
                            String oldBehaves = null;
                            String newBehaves = null;
                            String oldProdId = CommonUtil.convertObjToStr(subMap.get("PROD_ID"));
                            String newProdId = CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_PROD_ID"));
                            oldprodMap = new HashMap();
                            oldprodMap.put("PROD_ID", subMap.get("PROD_ID"));
                            List lstProd = sqlMap.executeQueryForList("getDepositClosingHeads", oldprodMap);
                            if (lstProd != null && lstProd.size() > 0) {
                                oldprodMap = (HashMap) lstProd.get(0);
                                newBehaves = CommonUtil.convertObjToStr(depositMap.get("BEHAVES_LIKE"));
                                System.out.println("oldprodMap :" + oldprodMap + "oldBehaves :" + oldBehaves + "newBehaves :" + newBehaves);
                                double closingAmt = CommonUtil.convertObjToDouble(extensionAuthMap.get("RECALCUALTE_INTEREST_AMT")).doubleValue();
                                double paid = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                double debitInt = CommonUtil.convertObjToDouble(subMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                interestBatchTO.setApplDt((Date)currDt.clone());
                                interestBatchTO.setActNum(oldDepositNo);
                                interestBatchTO.setProductId(CommonUtil.convertObjToStr(extensionAuthMap.get("EXTENSION_PROD_ID")));
                                interestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(extensionAuthMap.get("EXTENSION_DEPOSIT_AMT")));
                                interestBatchTO.setCustId(CommonUtil.convertObjToStr(extensionAuthMap.get("CUST_ID")));
                                interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(extensionAuthMap.get("EXTENSION_RATE_OF_INT")));
                                interestBatchTO.setIntDt((Date)currDt.clone());
                                if (oldprodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                    interestBatchTO.setIntType("SIMPLE");
                                } else {
                                    interestBatchTO.setIntType("COMPOUND");
                                }
                                interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(oldprodMap.get("INT_PAY")));
                                interestBatchTO.setProductType(SCREEN);
                                interestBatchTO.setTrnDt((Date)currDt.clone());
                                HashMap renewalCountMap = new HashMap();
                                renewalCountMap.put("ACT_NUM", dataMap.get(DEPOSITNO));
                                List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                                if (lstCount != null && lstCount.size() > 0) {
                                    renewalCountMap = (HashMap) lstCount.get(0);
                                    interestBatchTO.setSlNo(CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT")));
                                }
                                double balance = paid - debitInt;
                                System.out.println("paid :" + paid + "debitInt :" + debitInt + "closingAmt" + closingAmt + "balance :" + balance);
                                double bal = 0.0;
                                if (closingAmt < 0) {
                                    System.out.println("ClosingAmt<0 if :" + closingAmt);
                                    if (closingAmt > balance) {
                                        System.out.println("closingAmt<balance IF :" + closingAmt);
                                        interestBatchTO.setDrCr("DEBIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                        interestBatchTO.setTot_int_amt(new Double(closingAmt * -1));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        interestBatchTO.setDrCr("CREDIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                        interestBatchTO.setTot_int_amt(new Double(0));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                    } else {
                                        System.out.println("closingAmt<balance ELSE :" + closingAmt);
                                        interestBatchTO.setDrCr("CREDIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                        interestBatchTO.setTot_int_amt(new Double(closingAmt * -1));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        interestBatchTO.setDrCr("DEBIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        if (balance == 0) {
                                            interestBatchTO.setIntAmt(new Double(closingAmt * -1));
                                        } else {
                                            interestBatchTO.setIntAmt(new Double(balance + (closingAmt * -1)));
                                        }
                                        interestBatchTO.setTot_int_amt(new Double(0));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                    }
                                } else if (closingAmt > 0) {
                                    if (closingAmt > balance) {
                                        System.out.println("closingAmt>balance IF :" + closingAmt);
                                        interestBatchTO.setDrCr("DEBIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(closingAmt));
                                        interestBatchTO.setTot_int_amt(new Double(balance));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        interestBatchTO.setDrCr("CREDIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(balance));
                                        interestBatchTO.setTot_int_amt(new Double(0));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                    } else {
                                        System.out.println("closingAmt>balance ELSE :" + closingAmt);
                                        interestBatchTO.setDrCr("CREDIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(closingAmt));
                                        interestBatchTO.setTot_int_amt(new Double(balance));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                        interestBatchTO.setDrCr("DEBIT");
                                        interestBatchTO.setTransLogId("Payable");
                                        interestBatchTO.setIntAmt(new Double(balance - closingAmt));
                                        interestBatchTO.setTot_int_amt(new Double(0));
                                        sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
                                    }
                                }
                            }
                            balanceMap = new HashMap();
                            balanceMap.put("DEPOSIT_NO", oldDepNo);
                            totIntAmt = totIntAmt;
                            balanceMap.put("INT_AMT", new Double(totIntAmt));
                            sqlMap.executeUpdate("updateClosingTotIndAmount", balanceMap);
                            sqlMap.executeUpdate("updateClosingBalIntAmount", balanceMap);
                            if (oldDepNo != null && oldDepNo.length() > 0) {
                                HashMap closingMap = new HashMap();
                                closingMap.put("STATUS", status);
                                closingMap.put("USER_ID", dataMap.get("USER_ID"));
                                closingMap.put("AUTHORIZE_DT", currDt.clone());
                                closingMap.put("ACCOUNTNO", oldDepositNo);
                                sqlMap.executeUpdate("authorizeDepositAccountClose", closingMap);
                                closingMap.put("ACCOUNTNO", oldDepNo);
                                sqlMap.executeUpdate("authorizeDepositSubAccountClose", closingMap);
                                closingMap = null;
                            }
                            if (newDepNo != null && newDepNo.length() > 0) {
                                balanceMap.put(CommonConstants.STATUS, status);
                                balanceMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                balanceMap.put("CURR_DATE", currDt.clone());
                                balanceMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                balanceMap.put("STATUS", status);
                                balanceMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                sqlMap.executeUpdate("authorizeDepositAccInfo", balanceMap);
                                sqlMap.executeUpdate("authorizeDepositSubAccInfo", balanceMap);
                                HashMap newDep = new HashMap();
                                newDep.put("NORM_STATUS", CommonConstants.STATUS_CREATED);
                                newDep.put(DEPOSITNO, newDepNo);
                                newDep.put("PAYMENT_DAY", currDt.clone());
                                sqlMap.executeUpdate("authorizeDepositNewSubAccInfo", newDep);
                                newDep = null;
                            }
                        }
                        if (oldDepNo != null && oldDepNo.length() > 0) {
                            HashMap lienDetMap = new HashMap();
                            lienDetMap.put("DEPOSIT_NO", oldDepNo);
                            List getList = sqlMap.executeQueryForList("getDepositLienDetInsert", lienDetMap);
                            if (getList != null && getList.size() > 0) {
                                double totAmt = 0.0;
                                depositLienDAO = new DepositLienDAO();
                                ArrayList lienTOs = new ArrayList();
                                branchID = CommonUtil.convertObjToStr(extensionAuthMap.get("BRANCH_CODE"));
                                System.out.println("getList : " + getList);
                                for (int j = 0; j < getList.size(); j++) {
                                    depositLienTO = (DepositLienTO) getList.get(j);
                                    HashMap lienMap = new HashMap();
                                    lienMap.put("LIEN_AC_HD", depositLienTO.getLienAcHd());
                                    lienMap.put("PROD_ID", depositLienTO.getLienProdId());
                                    List lstHead = sqlMap.executeQueryForList("getAccountHeadForLoansLien", lienMap);
                                    if (lstHead != null && lstHead.size() > 0) {
                                        lienMap = (HashMap) lstHead.get(0);
                                        System.out.println("lienMap : " + lienMap);
                                        if (!lienMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS") || !(oldDepNo.equals(newDepNo))) {
                                            double amount = CommonUtil.convertObjToDouble(depositLienTO.getLienAmount()).doubleValue();
                                            totAmt = amount + totAmt;
                                            depositLienTO.setLienNo("-");
                                            depositLienTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO)));
                                            depositLienTO.setLienDt((Date)currDt.clone());
                                            depositLienTO.setStatus(CommonConstants.STATUS_CREATED);
                                            depositLienTO.setUnlienDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
                                            depositLienTO.setAuthorizeBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                            depositLienTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                            depositLienTO.setStatusDt((Date)currDt.clone());
                                            depositLienTO.setAuthorizeDt((Date)currDt.clone());
                                            lienTOs.add(depositLienTO);
                                            System.out.println("lienTOs: " + lienTOs);
                                            depositLienTO = null;
                                        }
                                    }
                                    lienMap = null;
                                }
                                map.put(CommonConstants.BRANCH_ID, branchID);
                                map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                                map.put("lienTOs", lienTOs);
                                map.put("SHADOWLIEN", new Double(totAmt));
                                System.out.println("map : " + map);
                                depositLienDAO.setCallFromOtherDAO(true);
                                depositLienDAO.execute(map);
                                HashMap updateMap = new HashMap();
                                updateMap.put("LIENAMOUNT", new Double(totAmt));
                                updateMap.put("DEPOSIT_ACT_NUM", newDepNo);
                                updateMap.put("SHADOWLIEN", new Double(totAmt));
                                updateMap.put("SUBNO", CommonUtil.convertObjToInt(subNo));
                                sqlMap.executeUpdate("updateSubAcInfoBal", updateMap);
                                updateMap.put("TODAY_DT", currDt.clone());
                                updateMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                sqlMap.executeUpdate("updateSubACInfoStatusToLien", updateMap);
                                HashMap zeroMap = new HashMap();
                                zeroMap.put("DEPOSIT_NO", oldDepNo);
                                zeroMap.put("AVAILABLE_BALANCE", new Double(0.0));
                                zeroMap.put("STATUS", "LIEN");
                                sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                HashMap oldLienMap = new HashMap();
                                oldLienMap.put("DEPOSIT_NO", oldDepNo);
                                oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                oldLienMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));
                                sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                oldLienMap = new HashMap();
                                oldLienMap.put("DEPOSIT_NO", newDepNo);
                                oldLienMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                                oldLienMap.put("STATUS", CommonConstants.STATUS_UNLIEN);
                                oldLienMap.put("AUTHORIZE_DT", currDt.clone());
                                oldLienMap.put("AUTHORIZE_BY", map.get(CommonConstants.USER_ID));

                                sqlMap.executeUpdate("updateoldDepositLienStatus", oldLienMap);
                                zeroMap = new HashMap();
                                zeroMap.put("DEPOSIT_NO", newDepNo);
                                zeroMap.put("AVAILABLE_BALANCE", new Double(0.0));
                                zeroMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                sqlMap.executeUpdate("updateAvailableBalanceZero", zeroMap);
                                updateMap = null;
                                zeroMap = null;
                                lienDetMap = null;
                                oldLienMap = null;
                                updateMap = null;
                            }
                            getList = null;
                        }
                        if (oldDepNo != null && oldDepNo.length() > 0) {//this is updating for deposit_sub_acinfo_sameno table.
                            HashMap statusMap = new HashMap();
                            statusMap.put(DEPOSITNO, oldDepNo);
                            statusMap.put("ACCOUNTNO", oldDepNo);
                            statusMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                            statusMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                            statusMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            statusMap.put("AUTHORIZEDT", currDt.clone());
                            System.out.println("statusMap oldDepNo:" + statusMap);
                            statusMap.put("TOTAL_BALANCE", new Double(0));
                            statusMap.put("CLEAR_BALANCE", new Double(0));
                            statusMap.put("AVAILABLE_BALANCE", new Double(0));
                            sqlMap.executeUpdate("authorizeSameDepositCloseNoBal", statusMap);
                            statusMap = null;
                        }
                        depositMap = null;
                        subMap = null;
                        accountHeadMap = null;
                        interestBatchTO = null;
                        balanceMap = null;
                        oldprodMap = null;
                        subMap = null;
                    } else {
                        dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        dataMap.put("CURR_DATE", currDt.clone());
                        System.out.println("depositMap Reject:" + depositMap + "dataMap :" + dataMap);
                        HashMap statusMap = new HashMap();
                        statusMap.put("DEPOSITNO", oldDepNo);
                        List lst = sqlMap.executeQueryForList("getLastIntApplDtForDeposit", statusMap);
                        if (lst != null && lst.size() > 0) {
                            statusMap = (HashMap) lst.get(0);
                            if (oldDepNo != null && oldDepNo.length() > 0) {//this is storing for deposit_sub_acinfo_sameno table.
                                statusMap = new HashMap();
                                statusMap.put(DEPOSITNO, oldDepNo);
                                sqlMap.executeUpdate("updateExtensionDeleteSamenoRec", statusMap);
                                sqlMap.executeUpdate("updateExtensionDeleteTempTable", statusMap);
                            }
                            if (newDepNo != null && newDepNo.length() > 0) {
                                dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_REJECTED);
                                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                dataMap.put("CURR_DATE", currDt.clone());
                                dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                dataMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                                dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                System.out.println("lst output oldDepNo.equals:" + dataMap);
                                sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                statusMap.put(DEPOSITNO, newDepNo);
                                statusMap.put("DEP_STATUS", CommonConstants.CLOSED);
                                statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                statusMap.put("CLOSE_DT", (Date)currDt.clone());
                                statusMap.put("CLOSE_BY", map.get(CommonConstants.USER_ID));
                                sqlMap.executeUpdate("updateNewDepositAuthorizeSubAcinfo", statusMap);
                                statusMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                                statusMap.put("RENEWAL_COUNT", CommonUtil.convertObjToInt("0"));
                                sqlMap.executeUpdate("updateNewDepositAuthorizeAcInfo", statusMap);
                            }
                            if (oldDepNo != null && oldDepNo.length() > 0) {
                                dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
                                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                                dataMap.put("CURR_DATE", currDt.clone());
                                dataMap.put(DEPOSITNO, String.valueOf(newDepNo));
                                dataMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                                dataMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                                dataMap.put(DEPOSITNO, oldDepNo);
                                System.out.println("statusMap oldDepNo.equals:" + dataMap);
                                sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
                                sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
                                statusMap.put("DEP_STATUS", "NEW");
                                statusMap.put(DEPOSITNO, oldDepNo);
                                statusMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                sqlMap.executeUpdate("updateOldDepositAuthorizeSubAcinfo", statusMap);
                                sqlMap.executeUpdate("updateOldDepositAuthorizeAcInfo", statusMap);
                            }
                        }
                        statusMap = null;
                    }
                    if (oldDepositNo != null && status != null) {
                        boolean passBookflag = false;
                        HashMap passBookMap = new HashMap();
                        if (status != null && status.equals("AUTHORIZED")) {
                            passBookMap.put("LINK_BATCH_ID", oldDepositNo);
                            List lst = sqlMap.executeQueryForList("getselectTransFromDeposit", passBookMap);
                            if (lst != null && lst.size() > 0) {
                                passBookMap = (HashMap) lst.get(0);
                                passBookflag = true;
                            }
                        }
                        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                        HashMap extensionTransactionMap = new HashMap();
                        extensionTransactionMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        extensionTransactionMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        extensionTransactionMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                        System.out.println("extensionTransactionMap@@@" + extensionTransactionMap);
                        System.out.println("extensionTransactionMap@@@map" + map);
                        transactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(oldDepositNo, status, extensionTransactionMap);
                        HashMap transMap = new HashMap();
                        transMap.put("LINK_BATCH_ID", oldDepositNo);
                        //comented by rishad 06/08/2015 0010862: FD interest application -instrument2 field disappearing while renewing
//                        sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
//                        sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                        if (passBookflag == true) {
                            passBookMap.put("TRANS_DT", currDt.clone());
                            sqlMap.executeUpdate("updateNullAsTransFromDeposit", passBookMap);
                            passBookflag = false;
                        }
                        renewalTempMap = new HashMap();
                        renewalTempMap.put("DEPOSIT_NO", newDepNo);
                        sqlMap.executeUpdate("updateTempStatusExtension", renewalTempMap);
                        transMap = null;
                    }
                    renewalTempMap = null;
                }
                list = null;
                extensionAuthMap = null;
                if (map.containsKey("NORMAL_MODE")) {//INCASE OF NEW DEPOSIT CREATED ONLY IT WILL COME INSIDE...
                    if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                        System.out.println("Normal Deposits Authorization :" + dataMap);
                        objAccInfoTO = getAccInfoData(depNo);
                        if (objAccInfoTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objAccInfoTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            HashMap behavesMap = new HashMap();
                            HashMap standingMap = new HashMap();
                            behavesMap.put("ACT_NUM", dataMap.get(DEPOSITNO));
                            List lstBehaves = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", behavesMap);
                            if (lstBehaves != null && lstBehaves.size() > 0) {
                                behavesMap = (HashMap) lstBehaves.get(0);
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    standingMap.put("DEPOSIT_NO", dataMap.get(DEPOSITNO) + "_1");
                                    List lst = sqlMap.executeQueryForList("getStandingInstrnDetailsAuthIsNull", standingMap);
                                    if (lst != null && lst.size() > 0) {
                                        standingMap = (HashMap) lst.get(0);
                                        standingMap.put("ACT_NUM", dataMap.get(DEPOSITNO) + "_1");
                                        lst = sqlMap.executeQueryForList("getSelectCreditAccNo", standingMap);
                                        if (lst != null && lst.size() > 0) {
                                            standingMap = (HashMap) lst.get(0);
                                            standingMap.put("SI_ID", standingMap.get("SI_ID"));
                                            standingMap.put("STATUS", dataMap.get("STATUS"));
                                            standingMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
                                            standingMap.put("AUTHORIZEDT", currDt.clone());
                                            standingMap.put("BRANCH_CODE", map.get(CommonConstants.BRANCH_ID));
                                            sqlMap.executeUpdate("authorizeStandingInstruction", standingMap);
                                        }
                                    }
                                }
                            }
                            //                        if(objTO.getOpeningMode().equals("TransferIn")){
                            //                            HashMap transferInMap = new HashMap();
                            //                            transferInMap.put("DEPOSIT_NO",returnDepositNo);
                            //                            List lst = sqlMap.executeQueryForList("getSelectOriginalAcNo",transferInMap);
                            //                            if(lst!=null && lst.size()>0){
                            //                                transferInMap = (HashMap)lst.get(0);
                            //                                transferInMap.put("TRANS_OUT_FLAG","Y");
                            //                                sqlMap.executeUpdate("updateTransferOutFlagDeposits",transferInMap);
                            //                            }
                            //                        }
                            authorizeUpdate(objAccInfoTO, dataMap);

                            getAuthorizeTransaction(map, status, dataMap);
                            //Added By Jeffin John For Mantis - 9969
                            if (CommonConstants.SAL_REC_MODULE != null && CommonConstants.SAL_REC_MODULE.equals("Y")) {
                                if (map.containsKey(CommonConstants.AUTHORIZEDATA)) {
                                    List authList = (List) map.get(CommonConstants.AUTHORIZEDATA);
                                    if (authList != null && authList.size() > 0) {
                                        HashMap authMap = (HashMap) authList.get(0);
                                        String accNo = CommonUtil.convertObjToStr(authMap.get("DEPOSIT NO"));
                                        HashMap acctDetailsMap = new HashMap();
                                        acctDetailsMap.put("ACNO", accNo);
                                        List acctDetailsList = sqlMap.executeQueryForList("getIntfreqDetails", acctDetailsMap);
                                        if (acctDetailsList != null && acctDetailsList.size() > 0) {
                                            HashMap behavesLikeMap = (HashMap) acctDetailsList.get(0);
                                            if (behavesLikeMap != null && behavesLikeMap.size() > 0) {
                                                String behavesLike = CommonUtil.convertObjToStr(behavesLikeMap.get(BEHAVES_LIKE));
                                                int noOfInstallments = CommonUtil.convertObjToInt(behavesLikeMap.get("TOTAL_INSTALLMENTS"));
                                                Date depositDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(behavesLikeMap.get("DEPOSIT_DT")));
                                                Date parameterDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(behavesLikeMap.get("DEPOSIT_DT")));
                                                java.util.GregorianCalendar sanCal = new java.util.GregorianCalendar();
                                                sanCal.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(parameterDate)));
                                                sanCal.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(parameterDate)));
                                                int currdat = parameterDate.getDate();
                                                double depAmt = CommonUtil.convertObjToDouble(behavesLikeMap.get("DEPOSIT_AMT"));
                                                if (behavesLike != null && !behavesLike.equals("") && behavesLike.equals("RECURRING")) {
                                                    HashMap transMap = new HashMap();
                                                    transMap.put("BATCH_ID", accNo + "_1");
                                                    transMap.put("TRANS_DT", currDt.clone());
                                                    transMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                                    List transList = sqlMap.executeQueryForList("getDepositAccountTransferDetails", transMap);
                                                    List cashList = sqlMap.executeQueryForList("getCashDetails", transMap);
                                                    if (cashList.isEmpty() == true
                                                            && transList.isEmpty() == true) {
                                                        HashMap dummyMap = new HashMap();
                                                        Date fromDate = new Date();
                                                        List recoveryParametersList = sqlMap.executeQueryForList("getRecoveryParameters", dummyMap);
                                                        int firstDay = 0;
                                                        int lastDay = 0;
                                                        int gracePeriod = 0;
                                                        HashMap recoveryParametersMap = null;
                                                        if (recoveryParametersList != null && recoveryParametersList.size() > 0) {
                                                            recoveryParametersMap = (HashMap) recoveryParametersList.get(0);
                                                        }
                                                        if (recoveryParametersMap != null && recoveryParametersMap.size() > 0) {
                                                            firstDay = CommonUtil.convertObjToInt(recoveryParametersMap.get("FIRST_DAY"));
                                                            lastDay = CommonUtil.convertObjToInt(recoveryParametersMap.get("LAST_DAY"));
                                                            gracePeriod = CommonUtil.convertObjToInt(recoveryParametersMap.get("GRACE_PERIOD"));
                                                        }
                                                        parameterDate.setDate(lastDay);
                                                        Date firstInstDt = new Date();
                                                        // Commented and added new block of code by nithya on 20-10-2018 for KD 294 - RBI ECS- Deposit Recurring Table data insertion is wrong.
                                                        /*if(depositDt.compareTo(parameterDate)<0){
                                                            firstInstDt = depositDt;
                                                            firstInstDt.setDate(gracePeriod);
                                                            firstInstDt.setMonth(depositDt.getMonth() + 1);
                                                        }
                                                        else {
                                                            firstInstDt = depositDt;
                                                            firstInstDt.setDate(gracePeriod);
                                                            firstInstDt.setMonth(depositDt.getMonth() + 2);
                                                        }
                                                        int num = 0;
                                                        HashMap recurrMap = new HashMap();
                                                        for (int j = 0; j < noOfInstallments; j++) {
                                                            num = j + 1;
                                                            recurrMap.put("SL_NO", new Double(num));
                                                            recurrMap.put("DUE_DATE", firstInstDt);
                                                            recurrMap.put("TODAY_DT", null);
                                                            recurrMap.put("DELAYED_COUNT", new Double(0));
                                                            recurrMap.put("AMOUNT", depAmt);
                                                            recurrMap.put("ACCOUNTNO", accNo + "_1");
                                                            recurrMap.put("ADT_AMT",new Double(0)); // Added by nithya on 28-09-2016 for 5158
                                                            sqlMap.executeUpdate("insertRecurringDeposit", recurrMap);
                                                            firstInstDt.setMonth(firstInstDt.getMonth() + 1);
                                                        }*/
                                                        // End
                                                        // New block of code starts
                                                        if (currdat <= firstDay) {
                                                            sanCal.set(sanCal.DAY_OF_MONTH, sanCal.getActualMaximum(sanCal.DAY_OF_MONTH));
                                                            fromDate = sanCal.getTime();
                                                        } else {
                                                            sanCal.set(sanCal.DAY_OF_MONTH, 1);
                                                            sanCal.set(sanCal.MONTH, sanCal.get(sanCal.MONTH) + 1);
                                                            int lastDayOfMonth = sanCal.getActualMaximum(sanCal.DAY_OF_MONTH);
                                                            fromDate = sanCal.getTime();
                                                            fromDate.setDate(lastDayOfMonth);
                                                        }
                                                        int num = 0;
                                                        HashMap recurrMap = new HashMap();
                                                        for (int j = 0; j < noOfInstallments; j++) {
                                                            num = j + 1;
                                                            recurrMap.put("SL_NO", new Double(num));
                                                            recurrMap.put("DUE_DATE", fromDate);
                                                            recurrMap.put("TODAY_DT", null);
                                                            recurrMap.put("DELAYED_COUNT", new Double(0));
                                                            recurrMap.put("AMOUNT", depAmt);
                                                            recurrMap.put("ACCOUNTNO", accNo + "_1");
                                                            recurrMap.put("ADT_AMT", new Double(0)); // Added by nithya on 28-09-2016 for 5158
                                                            sqlMap.executeUpdate("insertRecurringDeposit", recurrMap);
                                                            //firstInstDt.setMonth(firstInstDt.getMonth() + 1);
                                                            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                                                            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                                                            gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                                                            gCalendar.set(gCalendar.DATE, 1);
                                                            int curMonth = gCalendar.get(gCalendar.MONTH);
                                                            int nxtMonth = curMonth + 1;
                                                            gCalendar.set(gCalendar.MONTH, nxtMonth);
                                                            gCalendar.set(gCalendar.DATE, gCalendar.getActualMaximum(gCalendar.DATE));
                                                            fromDate = gCalendar.getTime();
                                                        }
                                                        // End
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //Code Ends Here For Mantis - 9969
                                dataMap = null;
                                behavesMap = null;
                                standingMap = null;
//                                objAccInfoTO = null;
                            }
                        }
                    } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                        System.out.println("Normal Deposits Rejection :" + dataMap);
                        objAccInfoTO = getAccInfoData(depNo);
                        if (objAccInfoTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objAccInfoTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            HashMap rejectionMap = new HashMap();
                            rejectionMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                            rejectionMap.put(DEPOSITNO, dataMap.get(DEPOSITNO));
                            sqlMap.executeUpdate("updateRejectionStatusAcinfo", rejectionMap);
                            sqlMap.executeUpdate("updateRejectionStatusSubAcinfo", rejectionMap);
                            HashMap behavesMap = new HashMap();
                            HashMap standingMap = new HashMap();
                            behavesMap.put("ACT_NUM", dataMap.get(DEPOSITNO));
                            List lstBehaves = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", behavesMap);
                            if (lstBehaves != null && lstBehaves.size() > 0) {
                                behavesMap = (HashMap) lstBehaves.get(0);
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    standingMap.put("DEPOSIT_NO", dataMap.get(DEPOSITNO) + "_1");
                                    List lst = sqlMap.executeQueryForList("getStandingInstrnDetailsAuthIsNull", standingMap);
                                    if (lst != null && lst.size() > 0) {
                                        standingMap = (HashMap) lst.get(0);
                                        standingMap.put("ACT_NUM", dataMap.get(DEPOSITNO) + "_1");
                                        lst = sqlMap.executeQueryForList("getSelectCreditAccNo", standingMap);
                                        if (lst != null && lst.size() > 0) {
                                            standingMap = (HashMap) lst.get(0);
                                            standingMap.put("SI_ID", standingMap.get("SI_ID"));
                                            standingMap.put("STATUS", dataMap.get("STATUS"));
                                            standingMap.put(CommonConstants.USER_ID, dataMap.get("USER_ID"));
                                            standingMap.put("AUTHORIZEDT", currDt.clone());
                                            standingMap.put("BRANCH_CODE", map.get(CommonConstants.BRANCH_ID));
                                            sqlMap.executeUpdate("authorizeStandingInstruction", standingMap);
                                            behavesMap.put("DEPOSIT_NO", dataMap.get(DEPOSITNO));
                                            behavesMap.put("STANDING_INSTRUCT", "N");
                                            sqlMap.executeUpdate("updateStandingInstnNo", behavesMap);
                                        }
                                    }
                                }
                            }
                            if (objAccInfoTO.getOpeningMode().equals("TransferIn")) {
                                HashMap transferInMap = new HashMap();
                                transferInMap.put("DEPOSIT_NO", objAccInfoTO.getDepositNo());
                                List lst = sqlMap.executeQueryForList("getSelectOriginalAcNo", transferInMap);
                                if (lst != null && lst.size() > 0) {
                                    transferInMap = (HashMap) lst.get(0);
                                    sqlMap.executeUpdate("updateTransferOutFlagDeposits", transferInMap);
                                }
                            }
                            authorizeUpdate(objAccInfoTO, dataMap);
                            getAuthorizeTransaction(map, status, dataMap);
                            dataMap = null;
                            behavesMap = null;
                            standingMap = null;
                            rejectionMap = null;
                        }
                    }                                       
                    HashMap mobileMap = new HashMap();
                    mobileMap.put("ACCOUNTNO",objAccInfoTO.getDepositNo());
                    mobileMap.put("PROD_TYPE",TransactionFactory.DEPOSITS);
                    mobileMap.put("PROD_ID",objAccInfoTO.getProdId());
                    mobileMap.put("USER_ID",map.get("USER_ID"));
                    mobileMap.put("AUTHORIZEDT",currDt.clone());
                    mobileMap.put("STATUS",status);
                    sqlMap.executeUpdate("authorizeSMSSubscriptionMap", mobileMap);
                    objAccInfoTO = null;
                }
            }
            System.out.println("#@#@#@#@#@#@#@#@#@#@  commit Transaction 111: ");
            //sqlMap.commitTransaction();
            System.out.println("#@#@#@#@#@#@#@#@#@#@  commit Transaction : ");
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
            //throw new TransRollbackException();
        }
    }

    private void authorizeUpdate(AccInfoTO objAccInfoTO, HashMap dataMap) throws Exception {
        sqlMap.executeUpdate("authorizeDepositAccInfo", dataMap);
        sqlMap.executeUpdate("authorizeDepositSubAccInfo", dataMap);
    }

    private void getAuthorizeInvTransaction(HashMap map, String status, HashMap dataMap) throws Exception {

        String linkBatchId = CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO)) + "_1";
        HashMap whereMap = new HashMap();
        String investmentBatchId = "";
        whereMap.put("BATCH_ID", linkBatchId);
        whereMap.put("TRANS_DT", currDt.clone());
        whereMap.put("INITIATED_BRANCH", _branchCode);
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        InvestmentsTransTO objgetInvestmentsTransTO;
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
        if (transList != null && transList.size() > 0) {
            HashMap transhashMap = (HashMap) transList.get(0);
            investmentBatchId = CommonUtil.convertObjToStr(transhashMap.get("BATCH_ID"));

            List investmentList = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTO", whereMap);

            if (investmentList != null && investmentList.size() > 0) {
                for (int m = 0; m < investmentList.size(); m++) {
                    objgetInvestmentsTransTO = new InvestmentsTransTO();
                    objgetInvestmentsTransTO = (InvestmentsTransTO) investmentList.get(m);
                    System.out.println("%%%%objgetInvestmentsTransTO" + objgetInvestmentsTransTO);
                    //                                investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                    objgetInvestmentsTransTO.setBatchID(investmentBatchId);
                    double dividendAmount = 0.0;
                    whereMap = new HashMap();
                    dividendAmount = CommonUtil.convertObjToDouble(objgetInvestmentsTransTO.getAmount()).doubleValue();
                    System.out.println("####Investment Transaction");
                    //Authorization
                    whereMap.put("INVESTMENT_ACC_NO", map.get("OTHER_BANK_HEAD"));
                    whereMap.put("BATCH_ID", investmentBatchId);
                    map.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap singleAuthorizeMap = new HashMap();
                    if (m == 1) {
                        singleAuthorizeMap.put("NOT_ALLOW_TRANS", "NOT_ALLOW_TRANS");
                    }
                    authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    authDataMap.put("BATCH_ID", objgetInvestmentsTransTO.getBatchID());
                    arrList.add(authDataMap);
                    singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                    singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    //                                singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(authorizeStatus, dividendAmount, whereMap, map));
                    singleAuthorizeMap.put("InvestmentsTransTO", objgetInvestmentsTransTO);
                    singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    map.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                    // Set TransactionTO
                    LinkedHashMap notDelMap = new LinkedHashMap();
                    LinkedHashMap transMap = new LinkedHashMap();
                    TransactionTO transfer = new TransactionTO();
                    transfer = new TransactionTO();
                    transfer.setTransType("TRANSFER");
                    transfer.setTransAmt(new Double(dividendAmount));
                    transfer.setProductType("GL");
                    transfer.setInstType("VOUCHER");
                    transfer.setBatchDt((Date)currDt.clone());
                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                    transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                    map.put("TransactionTO", transMap);
                    map.put("BILLS_LINK_BATCH_ID", linkBatchId);
                    InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                    whereMap = investmentDAO.execute(map);

                }
            }
        }


    }

    private void getAuthorizeTransaction(HashMap map, String status, HashMap dataMap) throws Exception {
        if (map.containsKey("INV")) {
            getAuthorizeInvTransaction(map, status, dataMap);
        } else {
            HashMap cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, dataMap.get(CommonConstants.USER_ID));
            cashAuthMap.put("LOAN_TRANS_OUT", "LOAN_TRANS_OUT");
            if (map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.containsKey("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN")) {
                cashAuthMap.put("FROM_MOBILE_APP_CUST_CREATION","FROM_MOBILE_APP_CUST_CREATION");
                cashAuthMap.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN", "CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
            }
            if (dataMap != null && dataMap.containsKey("DAILY_DEPOSIT_AGENT_ID") && dataMap.get("DAILY_DEPOSIT_AGENT_ID") != null) {
                cashAuthMap.put("DAILY_DEPOSIT_AGENT_ID", dataMap.get("DAILY_DEPOSIT_AGENT_ID"));
                cashAuthMap.put("CREDIT_TO_DEPOSIT_TRANSFER_SCREEN", "CREDIT_TO_DEPOSIT_TRANSFER_SCREEN");
            }
            String linkBatchId = CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO)) + "_1";
//        String linkBatchId=CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO));
            System.out.println(cashAuthMap + "  linkBatchId####" + linkBatchId);
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            transactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            cashAuthMap = null;
        }

    }

    private void destroyObjects() {
        objTO = null;
        transferInTO = null;
        mapNomTO = null;
        mapDepSubNoAccInfoTO = null;
        depSubNoAccInfoTO = null;
        mapJointAccntTO = null;
        jointAccntTO = null;
        objAuthorizedSignatoryDAO = null;;
        objPowerOfAttorneyDAO = null;
        depIntMap = null;
        extensionDepIntMap = null;
        interBranch = "";
    }

    private HashMap depositExtensionTransfer(HashMap extensionDepIntMap) throws Exception {
        System.out.println("##### DEP_INTEREST_AMT :" + extensionDepIntMap);
        if (extensionDepIntMap != null && !extensionDepIntMap.isEmpty()) {
            TxTransferTO transferTo = null;
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            String oldProdId = CommonUtil.convertObjToStr(extensionDepIntMap.get("OLD_PROD_ID"));
            String newProdId = CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_PRODID"));
            String depositSubNo = CommonUtil.convertObjToStr(extensionDepIntMap.get("OLD_DEPOSIT_NO"));
            String branchID = CommonUtil.convertObjToStr(extensionDepIntMap.get("BRANCH_CODE"));
            double depositTransAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("WITDRAWING_AMT")).doubleValue();
            double interestTransAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("WITHDRAWING_INT_AMT")).doubleValue();
            double balanceIntAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("BALANCE_INT_AMT")).doubleValue();
            double alreadydrawan = CommonUtil.convertObjToDouble(extensionDepIntMap.get("ALREADY_WITHDRAWN")).doubleValue();
            double alreadyCredited = CommonUtil.convertObjToDouble(extensionDepIntMap.get("ALREADY_CREDITED")).doubleValue();
            double totIntAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("TOTAL_INT_AMOUNT")).doubleValue();
            double originalIntAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("ORIGINAL_INT_AMT")).doubleValue();
            double creditedInt = balanceIntAmt - alreadyCredited;
            double drawnInt = balanceIntAmt - alreadydrawan;
            double payableBalance = alreadyCredited - alreadydrawan;
            System.out.println("New No generating :" + "depositTransAmt :" + depositTransAmt + "interestTransAmt :" + interestTransAmt
                    + "totIntAmt :" + totIntAmt + "alreadydrawan :" + alreadydrawan + "alreadyCredited :" + alreadyCredited + "originalIntAmt :" + originalIntAmt
                    + "creditedInt :" + creditedInt + "payableBalance :" + payableBalance + "drawnInt :" + drawnInt);

            if (depositSubNo.lastIndexOf("_") != -1) {
                depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
            }
            HashMap oldbehavesMap = new HashMap();
            oldbehavesMap.put("PROD_ID", extensionDepIntMap.get("OLD_PROD_ID"));
            List lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", oldbehavesMap);
            if (lst != null && lst.size() > 0) {
                oldbehavesMap = (HashMap) lst.get(0);
            }
            String oldBehaves = CommonUtil.convertObjToStr(oldbehavesMap.get("BEHAVES_LIKE"));
            System.out.println("oldBehaves" + oldBehaves);
            oldbehavesMap = null;
            HashMap newbehavesMap = new HashMap();
            newbehavesMap.put("PROD_ID", extensionDepIntMap.get("EXTENSION_PRODID"));
            lst = sqlMap.executeQueryForList("getBehavesLikeForDeposit", newbehavesMap);
            if (lst != null && lst.size() > 0) {
                newbehavesMap = (HashMap) lst.get(0);
            }
            String newBehaves = CommonUtil.convertObjToStr(newbehavesMap.get("BEHAVES_LIKE"));
            newbehavesMap = null;
            HashMap txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            transactionDAO.setLinkBatchID(depositSubNo + "_1");
            transactionDAO.setInitiatedBranch(branchID);
            HashMap oldacctHeadMap = new HashMap();
            oldacctHeadMap.put("PROD_ID", extensionDepIntMap.get("OLD_PROD_ID"));
            lst = sqlMap.executeQueryForList("getDepositClosingHeads", oldacctHeadMap);
            if (lst != null && lst.size() > 0) {
                oldacctHeadMap = (HashMap) lst.get(0);
            }
            HashMap newacctHeadMap = new HashMap();
            newacctHeadMap.put("PROD_ID", extensionDepIntMap.get("EXTENSION_PRODID"));
            lst = sqlMap.executeQueryForList("getDepositClosingHeads", newacctHeadMap);
            if (lst != null && lst.size() > 0) {
                newacctHeadMap = (HashMap) lst.get(0);
            }
            HashMap operativeDepMap = new HashMap();
            if (extensionDepIntMap.containsKey("EXTENSION_TRANS_ACCNO") && extensionDepIntMap.get("EXTENSION_TRANS_ACCNO") != null) {
                operativeDepMap.put("ACT_NUM", extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                lst = sqlMap.executeQueryForList("getAccNoProdIdDet", operativeDepMap);
                if (lst != null && lst.size() > 0) {
                    operativeDepMap = (HashMap) lst.get(0);
                }
            }
            double drawn = 0.0;
            if (drawnInt < 0) {
                drawn = drawnInt * -1;
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTo = new TxTransferTO();
                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                txMap.put(TransferTrans.DR_PROD_ID, extensionDepIntMap.get("OLD_PROD_ID"));
                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                System.out.println("****** balanceIntAmt txMap : " + txMap + "Reverse drawn<0 :" + drawn);

                transferTo = transactionDAO.addTransferDebitLocal(txMap, drawn);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, drawn);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
            }
            if (creditedInt < 0) {
                double credited = creditedInt * -1;
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTo = new TxTransferTO();
                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PROV_ACHD"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                System.out.println("****** balInt txMap : " + txMap + "Reverse credited<0 :" + credited + "Both :" + (credited + payableBalance));

                transferTo = transactionDAO.addTransferDebitLocal(txMap, credited);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, "Excess Interest :" + depositSubNo + "_1");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, credited);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
            } else if (creditedInt > 0) {
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTo = new TxTransferTO();
                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PROV_ACHD"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PROV_ACHD"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                }
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                System.out.println("****** balPayInt txMap : " + txMap + "Paying creditedInt>0 :" + creditedInt);

                transferTo = transactionDAO.addTransferDebitLocal(txMap, creditedInt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, creditedInt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
            }
            if (oldBehaves.equals("CUMMULATIVE") && creditedInt > 0) {
                System.out.println("CUMMULATIVE transaction payable debit credit to accts : " + balanceIntAmt);
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTo = new TxTransferTO();
                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                }
                txMap.put(TransferTrans.CR_ACT_NUM, depositSubNo + "_1");
                txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("OLD_PROD_ID"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                System.out.println("****** CUMMULATIVE : " + txMap + "Paying creditedInt>0 :" + creditedInt);

                transferTo = transactionDAO.addTransferDebitLocal(txMap, creditedInt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, creditedInt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
            }
            if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("NO")) {
                System.out.println("INT NO transaction payable debit credit to accts :" + creditedInt + payableBalance);
                if (creditedInt > 0) {
                    drawnInt = drawnInt + creditedInt;
                }
                if (creditedInt > 0) {
                    txMap = new HashMap();
                    transferList = new ArrayList();
                    transferTo = new TxTransferTO();
                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                    } else {
                        txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    } else {
                        txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    }
                    txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                    txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_PRODID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.CR_BRANCH, branchID);

                    System.out.println("****** INT NO : " + txMap + "Paying balPayInt>0" + creditedInt);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, creditedInt + payableBalance);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);
                     System.out.println("TransferTrans.PARTICULARS--------5-----"+TransferTrans.PARTICULARS);
                    txMap.put(TransferTrans.PARTICULARS, "Interest on:" + depositSubNo + "_1");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, creditedInt + payableBalance);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                }
            } else if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("YES")) {
                if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("CASH")
                        && originalIntAmt > 0 && interestTransAmt > 0 && (oldBehaves.equals("FIXED") || oldBehaves.equals("CUMMULATIVE"))) {
                    System.out.println("INT YES CASH transaction payable debit credit to accts : " + interestTransAmt);
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setTransId("");
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("INT_PAY")));
                    objCashTO.setProdType("GL");
                    objCashTO.setInpAmount(new Double(interestTransAmt));
                    objCashTO.setAmount(new Double(interestTransAmt));
                    objCashTO.setTransType(CommonConstants.DEBIT);
                    objCashTO.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(branchID));
                    objCashTO.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    objCashTO.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    objCashTO.setStatusDt((Date)currDt.clone());
                    objCashTO.setTransDt((Date)currDt.clone());
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setTokenNo(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_DEP_TOKEN_NO")));
                    objCashTO.setParticulars("To Pending Interest :" + depositSubNo + "_1");
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(branchID));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setLinkBatchId(depositSubNo + "_1");
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                    //akhil@
                    if (objTO.getOpeningMode().equals("Normal")) {
                        objCashTO.setScreenName(CommonUtil.convertObjToStr(extensionDepIntMap.get(CommonConstants.SCREEN)));
                    } else {
                        objCashTO.setScreenName("Deposit Account Renewal");
                    }
                    ArrayList cashList = new ArrayList();
                    cashList.add(objCashTO);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap interestTransMap = new HashMap();
                    interestTransMap.put("RENEWAL_DEPOSIT_SCREEN", "RENEWAL_DEPOSIT_SCREEN");
                    interestTransMap.put("RENEWAL_LIST", cashList);
                    interestTransMap.put("BRANCH_CODE", branchID);
                    cashTransactionDAO.execute(interestTransMap, false);
                    double remainAmt = 0.0;
                    if (originalIntAmt > 0) {
                        remainAmt = originalIntAmt - interestTransAmt;
                    }
                    System.out.println("****** INT YES CASH : " + txMap + "remainAmt :" + remainAmt);
                    if (remainAmt > 0) {
                        txMap = new HashMap();
                        transferList = new ArrayList();
                        transferTo = new TxTransferTO();
                        if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                        }
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                        if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        }
                        txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                        txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_PRODID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        System.out.println("****** INT YES CASH : " + txMap + "Paying remainAmt>0 :" + remainAmt);

                        transferTo = transactionDAO.addTransferDebitLocal(txMap, remainAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, remainAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, branchID);
                    }
                } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("TRANSFER")
                        && interestTransAmt > 0 && (oldBehaves.equals("FIXED") || oldBehaves.equals("CUMMULATIVE"))) {
                    System.out.println("INT YES TRANSFER transaction payable debit credit to accts : " + interestTransAmt);
                    txMap = new HashMap();
                    transferList = new ArrayList();
                    transferTo = new TxTransferTO();
                    if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("INT_PAY"));
                    } else {
                        txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("INT_PAY"));
                    }
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, "Interest :" + depositSubNo + "_1");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                    if (extensionDepIntMap.containsKey("EXTENSION_TRANS_PRODTYPE") && extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE").equals("OA")) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) operativeDepMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_TRANS_PRODID"));
                        txMap.put(TransferTrans.CR_ACT_NUM, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                    } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_PRODTYPE") && extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE").equals("GL")) {
                        txMap.put(TransferTrans.CR_AC_HD, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                    }
                    System.out.println("****** INT YES TRANSFER : " + txMap + "interestTransAmt>0 :" + interestTransAmt);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, originalIntAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);
                     System.out.println("TransferTrans.PARTICULARS--------6------"+TransferTrans.PARTICULARS);
                    txMap.put(TransferTrans.PARTICULARS, "Interest on:" + depositSubNo + "_1");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interestTransAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);

                    double remainAmt = 0.0;
                    if (originalIntAmt > 0) {
                        remainAmt = originalIntAmt - interestTransAmt;
                    }
                    if (remainAmt > 0) {
                        if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        } else {
                            txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                        }
                        txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                        txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_PRODID"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        System.out.println("****** INT YES TRANSFER : " + txMap + "Paying remainAmt>0 :" + remainAmt);


                        transferList.add(transferTo);
                        txMap.put(TransferTrans.PARTICULARS, "Pending Interest :" + depositSubNo + "_1");
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, remainAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                        transferList.add(transferTo);
                    }
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    //                    double remainAmt = 0.0;
                    //                    if(originalIntAmt>0){
                    //                        remainAmt = originalIntAmt - interestTransAmt;
                    //                    }
                    //                    System.out.println("****** INT YES TRANSFER : "+txMap+"remainAmt :"+remainAmt);
                    //                    if(remainAmt>0){
                    //                        txMap = new HashMap();
                    //                        transferList = new ArrayList();
                    //                        transferTo = new TxTransferTO();
                    //                        if(oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)){
                    //                            txMap.put(TransferTrans.DR_AC_HD, (String)newacctHeadMap.get("INT_PAY"));
                    //                        }else{
                    //                            txMap.put(TransferTrans.DR_AC_HD, (String)oldacctHeadMap.get("INT_PAY"));
                    //                        }
                    //                        txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.GL);
                    //                        txMap.put(TransferTrans.DR_BRANCH,branchID);
                    //                        txMap.put(TransferTrans.PARTICULARS,"Pending Interest :"+depositSubNo+"_1");
                    //                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2,"ENTERED_AMOUNT");
                    //                        txMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
                    //
                    //                        if(oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)){
                    //                            txMap.put(TransferTrans.CR_AC_HD,(String)oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    //                        }else{
                    //                            txMap.put(TransferTrans.CR_AC_HD,(String)newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    //                        }
                    //                        txMap.put(TransferTrans.CR_ACT_NUM,returnDepositNo+"_1");
                    //                        txMap.put(TransferTrans.CR_PROD_ID,extensionDepIntMap.get("EXTENSION_PRODID"));
                    //                        txMap.put(TransferTrans.CR_PROD_TYPE,TransactionFactory.DEPOSITS);
                    //                        txMap.put(TransferTrans.CR_BRANCH,branchID);
                    //                        System.out.println("****** INT YES TRANSFER : "+txMap+"Paying remainAmt>0 :"+remainAmt);
                    //
                    //                        transferTo =  transactionDAO.addTransferDebitLocal(txMap, remainAmt);
                    //                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    //                        transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    //                        transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    //                        transferList.add(transferTo);
                    //                        txMap.put(TransferTrans.PARTICULARS,"Pending Interest :"+depositSubNo+"_1");
                    //                        transferTo =  transactionDAO.addTransferCreditLocal(txMap, remainAmt);
                    //                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    //                        transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    //                        transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    //                        transferList.add(transferTo);
                    //                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    //                        transactionDAO.doTransferLocal(transferList, branchID);
                    //                    }
                }
            }
            if (extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("YES")) {
                if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("CASH")
                        && depositTransAmt > 0) {
                    System.out.println("DEP YES CASH transaction payable debit credit to accts : " + depositTransAmt);
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setTransId("");
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(oldacctHeadMap.get("FIXED_DEPOSIT_ACHD")));
                    objCashTO.setProdType(SCREEN);
                    objCashTO.setActNum(CommonUtil.convertObjToStr(depositSubNo + "_1"));
                    objCashTO.setProdId(CommonUtil.convertObjToStr(extensionDepIntMap.get("OLD_PROD_ID")));
                    objCashTO.setInpAmount(new Double(depositTransAmt));
                    objCashTO.setAmount(new Double(depositTransAmt));
                    objCashTO.setTransType(CommonConstants.DEBIT);
                    objCashTO.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(branchID));
                    objCashTO.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    objCashTO.setStatusDt((Date)currDt.clone());
                    objCashTO.setTransDt((Date)currDt.clone());
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setTokenNo(CommonUtil.convertObjToStr(extensionDepIntMap.get("EXTENSION_DEP_TOKEN_NO")));
                    objCashTO.setParticulars("To Deposit Amount withdrawing :" + depositSubNo + "_1");
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(branchID));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setLinkBatchId(depositSubNo + "_1");
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
                    //akhil@
                    if (objTO.getOpeningMode().equals("Normal")) {
                        objCashTO.setScreenName(CommonUtil.convertObjToStr(extensionDepIntMap.get(CommonConstants.SCREEN)));
                    } else {
                        objCashTO.setScreenName("Deposit Account Renewal");
                    }
                    ArrayList cashList = new ArrayList();
                    cashList.add(objCashTO);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap interestTransMap = new HashMap();
                    interestTransMap.put("RENEWAL_DEPOSIT_SCREEN", "RENEWAL_DEPOSIT_SCREEN");
                    interestTransMap.put("RENEWAL_LIST", cashList);
                    interestTransMap.put("BRANCH_CODE", branchID);
                    cashTransactionDAO.execute(interestTransMap, false);
                } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_MODE") && extensionDepIntMap.get("EXTENSION_TRANS_MODE").equals("TRANSFER")
                        && depositTransAmt > 0) {
                    System.out.println("DEP YES TRANSFER transaction payable debit credit to accts : " + depositTransAmt);
                    txMap = new HashMap();
                    transferList = new ArrayList();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                    txMap.put(TransferTrans.DR_PROD_ID, extensionDepIntMap.get("OLD_PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.PARTICULARS, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    if (extensionDepIntMap.containsKey("EXTENSION_TRANS_PRODTYPE") && extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE").equals("OA")) {
                        txMap.put(TransferTrans.CR_AC_HD, (String) operativeDepMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_DEP_TRANS_PRODID"));
                        txMap.put(TransferTrans.CR_ACT_NUM, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                    } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_PRODTYPE") && extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE").equals("GL")) {
                        txMap.put(TransferTrans.CR_AC_HD, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                    } else if (extensionDepIntMap.containsKey("EXTENSION_TRANS_PRODTYPE") && extensionDepIntMap.get("EXTENSION_TRANS_PRODTYPE").equals("AD")) {
                        txMap.put(TransferTrans.CR_AC_HD, extensionDepIntMap.get("EXTENSION_TRANS_ACCNO"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                    }
                    System.out.println("DEP YES TRANSFER :" + txMap);
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, depositTransAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, depositTransAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                }
            }
            if (extensionDepIntMap.containsKey("ORIGINAL_DEP_AMT") && extensionDepIntMap.get("ORIGINAL_DEP_AMT") != null && !depositSubNo.equals(returnDepositNo)) {
                System.out.println("NEW DEP YES TRANSFER transaction payable debit credit to accts : " + extensionDepIntMap.get("ORIGINAL_DEP_AMT"));
                txMap = new HashMap();
                transferList = new ArrayList();
                transferTo = new TxTransferTO();
                double originalMatAmt = CommonUtil.convertObjToDouble(extensionDepIntMap.get("ORIGINAL_DEP_AMT")).doubleValue();
                double renewalAmount = CommonUtil.convertObjToDouble(extensionDepIntMap.get("EXTENSION_DEPOSIT_AMT")).doubleValue();
                if (originalIntAmt != 0 && oldBehaves.equals("CUMMULATIVE")) {
                    originalMatAmt = originalMatAmt + originalIntAmt;
                }
                if (extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("YES")) {
                    if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("YES")) {
                        if (originalIntAmt < 0 && oldBehaves.equals("FIXED")) {
                            originalMatAmt = (originalMatAmt - depositTransAmt) + originalIntAmt;
                            System.out.println("EXTENSION_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                        } else {
                            originalMatAmt = originalMatAmt - depositTransAmt;
                        }
                    } else if (extensionDepIntMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_INT_WITHDRAWING").equals("NO")) {
                        if (originalIntAmt < 0 && oldBehaves.equals("FIXED")) {
                            originalMatAmt = (originalMatAmt - depositTransAmt) + originalIntAmt;
                            System.out.println("EXTENSION_DEP_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                        } else {
                            originalMatAmt = originalMatAmt - depositTransAmt;
                        }
                    }
                } else if (extensionDepIntMap.containsKey("EXTENSION_WITHDRAWING") && extensionDepIntMap.get("EXTENSION_WITHDRAWING").equals("NO")) {
                    if (originalIntAmt < 0 && oldBehaves.equals("FIXED")) {
                        originalMatAmt = (originalMatAmt - depositTransAmt) + originalIntAmt;
                        System.out.println("EXTENSION_WITHDRAWING YES balanceIntAmt<0 : " + originalMatAmt);
                    } else {
                        originalMatAmt = originalMatAmt - depositTransAmt;
                    }
                }
                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                }
                txMap.put(TransferTrans.DR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                txMap.put(TransferTrans.DR_ACT_NUM, depositSubNo + "_1");
                txMap.put(TransferTrans.DR_PROD_ID, extensionDepIntMap.get("OLD_PROD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.PARTICULARS, returnDepositNo + "_1");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");

                if (oldBehaves.equals(newBehaves) && oldProdId.equals(newProdId)) {
                    txMap.put(TransferTrans.CR_AC_HD, (String) oldacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                } else {
                    txMap.put(TransferTrans.CR_AC_HD, (String) newacctHeadMap.get("FIXED_DEPOSIT_ACHD"));
                }
                txMap.put(TransferTrans.CR_ACT_NUM, returnDepositNo + "_1");
                txMap.put(TransferTrans.CR_PROD_ID, extensionDepIntMap.get("EXTENSION_PRODID"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                System.out.println("NEW DEP YES TRANSFER : " + txMap);
                transferTo = transactionDAO.addTransferDebitLocal(txMap, originalMatAmt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, " " + depositSubNo + "_1");
                transferTo = transactionDAO.addTransferCreditLocal(txMap, originalMatAmt);
                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(extensionDepIntMap.get("USER_ID")));
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, branchID);
            }
            System.out.println("completed all transactions : " + txMap);
            extensionDepIntMap.put("DEPOSIT_NO", depositSubNo + "_1");
            extensionDepIntMap.put("BRANCH_CODE", branchID);
            //            balPay = balPay + balPayable;
            //            extensionDepIntMap.put("BALANCE_AMT",new Double(balPay));
        }
        return extensionDepIntMap;
    }
    
    private void updateSMSSubscription(AccInfoTO objTO) throws Exception {
        objSMSSubscriptionTO.setStatusBy(objTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(objTO.getStatusDt());
        objSMSSubscriptionTO.setCreatedBy(objTO.getStatusBy());
        objSMSSubscriptionTO.setActNum(objTO.getDepositNo());
        objSMSSubscriptionTO.setCustId(objTO.getCustId());
        objSMSSubscriptionTO.setProdId(objTO.getProdId());//KD-3742 : VALIDATION ERROR IN DEPOSIT RENEWAL
        if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() > 0 && objSMSSubscriptionTO.getSubscriptionDt() != null) {
            List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
            if (list != null && list.size() > 0) {
                sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
            } else {
                sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
            }
        }
    }
    
    // Added by nithya on 07-10-2017 for group deposit changes
    
    private boolean isGroupDepositAcct(HashMap where) throws Exception{
        boolean groupDepositFlag = false;
        List groupDepositProdList = (List) sqlMap.executeQueryForList("getIsGroupDepositProduct", where);
        if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
            HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
            if (groupDepositProdMap != null && groupDepositProdMap.containsKey("IS_GROUP_DEPOSIT") && groupDepositProdMap.get("IS_GROUP_DEPOSIT") != null) {
                if (CommonUtil.convertObjToStr(groupDepositProdMap.get("IS_GROUP_DEPOSIT")).equalsIgnoreCase("Y")) {
                    groupDepositFlag = true;
                }
            }
        } 
        return groupDepositFlag;
    }
    
    private void deleteSMSSubscription(AccInfoTO objTO) throws Exception {
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", "TD");
        checkMap.put("PROD_ID", objTO.getProdId());
        checkMap.put("ACT_NUM", objTO.getDepositNo());
        checkMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", checkMap);
        checkMap.clear();
        checkMap = null;
    }
    
    
    public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) currDt.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }
    
    private HashMap calcuateTDS(HashMap obj, boolean enableTrans) throws Exception { // Added by nithya on 06-02-2020 for KD-1090
        //System.out.println("####### doDepositClose tdsMap " + obj);
        String depositBranch = (String) obj.get(CommonConstants.BRANCH_ID);
        HashMap tdsCalcMap = new HashMap();
        tdsCalcMap = obj;
        //System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        interestBatchTO = new InterestBatchTO();
        //System.out.println("excecuting here....1");
        TdsCalc tdsCalculator = new TdsCalc(depositBranch);
        //System.out.println("executing here2");
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String Prod_type = "TD";
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        intTrfAmt = CommonUtil.convertObjToDouble(tdsCalcMap.get("TDS_AMOUNT")).doubleValue();
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        closeMap.put("CLOSING_TYPE", tdsCalcMap.get("CLOSING_TYPE"));
        //System.out.println("executing here8888888888888888888");
        Date tdsDate = (Date) ServerUtil.getCurrentDate(depositBranch);;
        tdsMap.put("INT_DATE", tdsDate.clone());        
        //System.out.println("executing here========================");
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        HashMap whereMap = new HashMap();//TDS product Level Checking Calculate Yes/No
        whereMap.put("PROD_ID", prod_id);
        //System.out.println("******************** where" + whereMap);
        List prodTDSList = (List) sqlMap.executeQueryForList("icm.getDepositMaturityIntRate", whereMap);        //Product Level Checking
        if (prodTDSList != null && prodTDSList.size() > 0) {
            whereMap = (HashMap) prodTDSList.get(0);
            if (CommonUtil.convertObjToStr(whereMap.get("CALCULATE_TDS")).equals("Y")) {
                whereMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
                List depAccList = (List) sqlMap.executeQueryForList("getTDSAccountLevel", whereMap);            //Account Level Checking
                if (depAccList != null && depAccList.size() > 0) {
                    List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
                    if (exceptionList == null || exceptionList.size() <= 0) {
                        whereMap.put("CUST_ID", CustId);
                        List custList = (List) sqlMap.executeQueryForList("getCustData", whereMap);             //Checking Customer having PAN or not.
                        if (custList != null && custList.size() > 0) {
                            whereMap = (HashMap) custList.get(0);
                            if (CommonUtil.convertObjToStr(whereMap.get("PAN_NUMBER")).length() > 0) {
                                tdsCalculator.setPan(true);
                            } else {
                                tdsCalculator.setPan(false);
                            }
                        }
                        tdsCalculator.setIsTransaction(false);
                        tdsMap = new HashMap();
                        tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
                        //System.out.println("####### Final Debenture Transfer tdsMap " + tdsMap);
                        if (tdsMap != null) {
                            interestBatchTO.setIsTdsApplied("Y");
                            interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
                        }
                    }
                }
            }
        }
        return tdsMap;
    }
    
    public String generateReferenceId(AccInfoTO accInfoTO) throws SQLException {
        String referNo = "", strPrefix = "", nextAccno = "";
        HashMap hash = null;
        HashMap where = new HashMap();
        String productid = accInfoTO.getProdId();
        if (productid != null && productid.length() > 0) {
            where.put("PROD_TYPE", productid);
            where.put("BRANCH_ID", _branchCode);
            List list = (List) sqlMap.executeQueryForList("getReferenceId", where);
            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }
                nextAccno = (String) hash.get("NEXT_AC_NO");
                referNo = strPrefix + nextAccno;
            }
        }
        return referNo;
    }
    
}
