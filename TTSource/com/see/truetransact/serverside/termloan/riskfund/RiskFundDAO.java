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
package com.see.truetransact.serverside.termloan.riskfund;

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
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
//import com.see.truetransact.transferobject.termloan.TermLoanSecurityMemberTO;
import com.see.truetransact.transferobject.termloan.TermLoanSecuritySalaryTO;
import com.see.truetransact.transferobject.termloan.TermLoanLosTypeTO;
import com.see.truetransact.transferobject.termloan.TermLoanDepositTypeTO;
import com.see.truetransact.transferobject.termloan.riskfund.RiskFundTO;
//import com.see.truetransact.transferobject.termloan.TermLoanSecurityLandTO;
import org.apache.log4j.Logger;
import sun.misc.Cleaner;
/**
 * TermLoan DAO.
 *
 * @author shanmugavel
 *
 */
public class RiskFundDAO extends TTDAO {
    private final static Logger log = Logger.getLogger(RiskFundDAO.class);
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
    private String borrower_No;
    private String acct_No;
    private String lienNo = "";
    public String prod_id = "";
    public String cmd = null;
    // Security Purpose
    private TermLoanSecurityMemberTO objMemberTypeTO;
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
    private String user_id = "";
    private String loanType = "";
    private boolean newLoan = false;
    private String productCategory = "";
    private String keyValue = "";
    public List chargeLst = null;
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
    TxTransferTO transferTo;
    private TransactionDAO transactionDAO = null;
    //ArrayList transferList = new ArrayList();
    private TransferTrans objTransferTrans = null;
    private HashMap loanDataMap = new HashMap();
    private HashMap returnDataMap = new HashMap();
    /**
     * Creates a new instance of TermLoanDAO
     */
    public RiskFundDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception  {
        try{
          System.out.println("map^$^$^$^$^$^$^$^"+map); 
        _branchCode = CommonUtil.convertObjToStr(map.get("INITIATED_BRANCH"));
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        user_id =  CommonUtil.convertObjToStr(map.get("USER_ID")); 
        returnDataMap = new HashMap();
        loanDataMap = new HashMap();
        if (map.containsKey("RISK_FUND_DR_DET") && map.containsKey("CHRG_TYP") && CommonUtil.convertObjToStr(map.get("CHRG_TYP")).equals("Risk Fund") ) {
            ArrayList lstRiskFund = (ArrayList) map.get("RISK_FUND_DR_DET");
            if(map.containsKey("NO_TRANSACTION") && map.get("NO_TRANSACTION") != null && map.get("NO_TRANSACTION").equals("Y")){
                processRiskFundWithoutTransaction(map, lstRiskFund);
            }else{
            processTransaction(map, lstRiskFund);
            }
        }else{
            ArrayList chargesList = (ArrayList) map.get("RISK_FUND_DR_DET");
            if(map.containsKey("NO_TRANSACTION") && map.get("NO_TRANSACTION") != null && map.get("NO_TRANSACTION").equals("Y")){
                processChargesWithoutTransaction(map, chargesList);
            }else{
            processChargesTransaction(map, chargesList);
        }
        }
        }catch(Exception e){
            e.printStackTrace();
            log.error(e);
            throw new Exception(e.getMessage());            
        }
        return returnDataMap;        
    }

    private void processChargesTransaction(HashMap mapRiskFnd, List lstRisk) throws Exception {
        TransferTrans objTransferTrans = new TransferTrans();
        ArrayList transferList = new ArrayList();
        HashMap txMap = new HashMap();
        String accHead = "";
        ArrayList dataList = new ArrayList();
        int count = 0;
           try {
            transferTo = new TxTransferTO();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            HashMap mapSubRisk1 = new HashMap();
            HashMap mapSubRisk = new HashMap();            
            double transAmt = 0.0;
            String narration = "";
            ArrayList lstSubRsk = new ArrayList();
            int size = lstRisk.size();
            for (int i = 0; i < size; i++) {
                count ++;
                lstSubRsk = new ArrayList();
                lstSubRsk = (ArrayList) lstRisk.get(i);
                if(lstSubRsk.size() > 7){ //KD-3483
                 narration = CommonUtil.convertObjToStr(lstSubRsk.get(7));
                }
                List listRisk = null;
                mapSubRisk1.put("PROD_ID", mapRiskFnd.get("PROD_ID_DR"));
                if(mapRiskFnd.containsKey("PROD_NAME_DR") && CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")).equals("MDS")){
                    listRisk = sqlMap.executeQueryForList("getMDSChargesHead", mapSubRisk1);
                }else{
                    listRisk = sqlMap.executeQueryForList("getLoanChargesHead", mapSubRisk1);
                }
                if (listRisk != null && listRisk.size() > 0) {
                        mapSubRisk1 = (HashMap) listRisk.get(0);
                    if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("ARBITRARY CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("ARBITRARY_CHARGES"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("EXECUTION DECREE CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("EXECUTION_DECREE_CHARGES"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("INSURANCE CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("INSURANCE_CHARGES"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("LEGAL CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("LEGAL_CHARGES"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("MISCELLANEOUS CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("MISC_SERV_CHRG"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("ADVERTISE CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("ADVERTISE_ACHEAD"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("OTHER CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("OTHRCHRGS_HD"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("RECOVERY CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("RECOVERY_CHARGES"));
                    }else if(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")).equalsIgnoreCase("MEASUREMENT CHARGES")){
                        accHead =  CommonUtil.convertObjToStr(mapSubRisk1.get("MEASUREMENT_CHARGES"));
                    }
                }
                txMap.put(TransferTrans.DR_AC_HD, accHead);
                 if(mapRiskFnd.containsKey("PROD_NAME_DR") && CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")).equals("MDS")){
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.MDS_TRANSMODE_TYPE);
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);  
                 }else{
//                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_TYP_DR")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);  
                    txMap.put(TransferTrans.DR_PROD_ID, mapRiskFnd.get("PROD_ID_DR"));
                    txMap.put(TransferTrans.DR_ACT_NUM, lstSubRsk.get(0));
                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                 }
                txMap.put("SCREEN_NAME" ,"Risk Fund and Loan Charges");
                txMap.put(TransferTrans.DR_BRANCH, mapRiskFnd.get("ACCOUNT_BRANCH"));
                txMap.put("INITIATED_BRANCH", _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(CommonConstants.USER_ID, mapRiskFnd.get("USER_ID"));
                txMap.put("LINK_BATCH_ID", lstSubRsk.get(0));
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")));
                txMap.put(TransferTrans.NARRATION ,narration);
                transAmt = CommonUtil.convertObjToDouble(lstSubRsk.get(6));
                //transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmt));
                ///transferList.add(transferTo);  
                
                //To lOANS_ACCT_CHARGE_DETAILS
                TermLoanChargesTO termLoanChargeTO = new TermLoanChargesTO();
               if(mapRiskFnd.containsKey("PROD_NAME_DR") && CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")).equals("MDS")){
                   termLoanChargeTO.setProd_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")));
                   String mdsActNum = CommonUtil.convertObjToStr(lstSubRsk.get(0))+"_1";
                   System.out.println("mdsActNum : "+mdsActNum);
                   termLoanChargeTO.setAct_num(mdsActNum);
                }else{
                   termLoanChargeTO.setProd_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_TYP_DR")));
                    termLoanChargeTO.setAct_num(CommonUtil.convertObjToStr(lstSubRsk.get(0)));
                }
                termLoanChargeTO.setProd_Id(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_ID_DR")));
                termLoanChargeTO.setCharge_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")));
                termLoanChargeTO.setChargeDt(curr_dt);
                termLoanChargeTO.setAmount(transAmt);
                termLoanChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                termLoanChargeTO.setStatus_By(CommonUtil.convertObjToStr(mapRiskFnd.get("USER_ID")));
                termLoanChargeTO.setStatus_Dt(curr_dt);
                termLoanChargeTO.setAuthorize_Dt(curr_dt);
                termLoanChargeTO.setAuthorize_by(CommonUtil.convertObjToStr(mapRiskFnd.get("USER_ID")));
                termLoanChargeTO.setAuthorize_Status(CommonConstants.STATUS_AUTHORIZED);
                termLoanChargeTO.setPaidAmount(0.0);
                termLoanChargeTO.setNarration(narration);
                sqlMap.executeUpdate("insertTermLoanChargeTO", termLoanChargeTO);                
            }
            txMap = new HashMap();
            txMap.put(TransferTrans.CR_AC_HD, mapRiskFnd.get("AC_HD"));
            if(mapRiskFnd.containsKey("PROD_TYP_CR") && mapRiskFnd.get("PROD_TYP_CR").equals(TransactionFactory.GL)){
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);    
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL); 
            }else{
                txMap.put(TransferTrans.CR_PROD_TYPE, mapRiskFnd.get("PROD_TYP_CR"));    
                txMap.put(TransferTrans.CR_ACT_NUM, mapRiskFnd.get("AC_NO"));
                txMap.put(TransferTrans.CR_PROD_ID, mapRiskFnd.get("PROD_ID_CR"));
                txMap.put("TRANS_MOD_TYPE",mapRiskFnd.get("PROD_TYP_CR"));
            }
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put("INITIATED_BRANCH", _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");  
            txMap.put("LINK_BATCH_ID",lstSubRsk.get(0));        
            txMap.put(CommonConstants.USER_ID, mapRiskFnd.get("USER_ID"));
            if(count>1){
                txMap.put("LINK_BATCH_ID", "");    
            }            
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")));
            transAmt = CommonUtil.convertObjToDouble(mapRiskFnd.get("RISK_FUND_TOT"));
            txMap.put("SCREEN_NAME" ,"Risk Fund and Loan Charges");
            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmt));
            if (transferList != null && transferList.size() > 0) {
                doDebitCredit(transferList, _branchCode, false);
            }
            getTransDetails(CommonUtil.convertObjToStr(loanDataMap.get("SINGLE_TRANS_ID")));
            returnDataMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
      private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", "INSERT");
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, user_id);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        System.out.println("doDebitCredit : " + data);
        loanDataMap = transferDAO.execute(data, false);    
        
     }   
      
      private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", curr_dt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnDataMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsPayRoll", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnDataMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsPayRoll", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnDataMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
      
    private void insertLoanAcctCharges(List lstRisk) {
        
    }
    
    private void processTransaction(HashMap mapRiskFnd, List lstRisk)  throws Exception {
        try {
            transferTo = new TxTransferTO();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            objTransferTrans = new TransferTrans();
            HashMap mapSubRisk1 = new HashMap();
            HashMap mapSubRisk = new HashMap();
            double transAmt = 0.0;
            String narration = "";
            ArrayList lstSubRsk = new ArrayList();
            ArrayList transferList = new ArrayList();
            int count = 0;
            int size = lstRisk.size();
            for (int i = 0; i < size; i++) {
                count++;
                lstSubRsk = new ArrayList();
                lstSubRsk = (ArrayList) lstRisk.get(i);
                mapSubRisk1.put("PROD_ID", mapRiskFnd.get("PROD_ID_DR"));
                List listRisk = sqlMap.executeQueryForList("getLoansProdHead", mapSubRisk1);
                if (listRisk != null && listRisk.size() > 0) {
                    mapSubRisk1 = (HashMap) listRisk.get(0);
                }
                if(lstSubRsk.size() > 7){
                 narration = CommonUtil.convertObjToStr(lstSubRsk.get(7));
                }
                mapSubRisk.put(TransferTrans.DR_AC_HD, mapSubRisk1.get("ACCT_HEAD"));
                mapSubRisk.put(TransferTrans.DR_PROD_ID, mapRiskFnd.get("PROD_ID_DR"));
                mapSubRisk.put(TransferTrans.DR_ACT_NUM, lstSubRsk.get(0));
                mapSubRisk.put(TransferTrans.DR_BRANCH, mapRiskFnd.get("ACCOUNT_BRANCH"));
                mapSubRisk.put(TransferTrans.NARRATION ,narration);
                mapSubRisk.put("INITIATED_BRANCH", _branchCode);
                mapSubRisk.put(TransferTrans.CURRENCY, "INR");
                mapSubRisk.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                mapSubRisk.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                mapSubRisk.put(CommonConstants.USER_ID, mapRiskFnd.get("USER_ID"));
                mapSubRisk.put("LINK_BATCH_ID", lstSubRsk.get(0));
                mapSubRisk.put("SCREEN_NAME" ,"LOAN_CHARGE");
                transAmt = CommonUtil.convertObjToDouble(lstSubRsk.get(6));
                RiskFundTO objRiskfndTo = new RiskFundTO();
                objRiskfndTo.setActNum(CommonUtil.convertObjToStr(lstSubRsk.get(0)));
                objRiskfndTo.setRiskFundAmt(CommonUtil.convertObjToDouble(lstSubRsk.get(6)));
                objRiskfndTo.setMemNo(CommonUtil.convertObjToStr(lstSubRsk.get(1)));
                objRiskfndTo.setProdId(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_ID_DR")));
                objRiskfndTo.setBranchId(_branchCode);
                objRiskfndTo.setNarration(narration); //KD-3483
                sqlMap.executeUpdate("insRiskfundDetails", objRiskfndTo);
                transferList.add(objTransferTrans.getDebitTransferTO(mapSubRisk, transAmt));
            }
            mapSubRisk.put(TransferTrans.CR_AC_HD, mapRiskFnd.get("AC_HD"));
            if(mapRiskFnd.containsKey("PROD_TYP_CR") && mapRiskFnd.get("PROD_TYP_CR").equals(TransactionFactory.GL)){
                mapSubRisk.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);   
                mapSubRisk.put("TRANS_MOD_TYPE",TransactionFactory.GL);
            }else{
                mapSubRisk.put(TransferTrans.CR_PROD_TYPE, mapRiskFnd.get("PROD_TYP_CR"));    
                mapSubRisk.put(TransferTrans.CR_ACT_NUM, mapRiskFnd.get("AC_NO"));
                mapSubRisk.put(TransferTrans.CR_PROD_ID, mapRiskFnd.get("PROD_ID_CR"));
                mapSubRisk.put("TRANS_MOD_TYPE",mapRiskFnd.get("PROD_TYP_CR"));
            }
            mapSubRisk.put(TransferTrans.CR_BRANCH, _branchCode);
            mapSubRisk.put("INITIATED_BRANCH", _branchCode);
            mapSubRisk.put(TransferTrans.CURRENCY, "INR");
            mapSubRisk.put("SCREEN_NAME" ,"LOAN_CHARGE");
            if(count>1){
                mapSubRisk.put("LINK_BATCH_ID", "");    
            }            
            transAmt = CommonUtil.convertObjToDouble(mapRiskFnd.get("RISK_FUND_TOT"));
            transferList.add(objTransferTrans.getCreditTransferTO(mapSubRisk, transAmt));
            if (transferList != null && transferList.size() > 0) {
                doDebitCredit(transferList, _branchCode, false);
            }
            getTransDetails(CommonUtil.convertObjToStr(loanDataMap.get("SINGLE_TRANS_ID")));
            returnDataMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
    private void destroyObjects() {
        returnDataMap = null;
        objTransferTrans = null;
        loanDataMap = null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {

        return null;
    }
    
   private void processChargesWithoutTransaction(HashMap mapRiskFnd, List lstRisk) throws Exception {  
       System.out.println("inside processChargesWithoutTransaction");
           try { 
            double transAmt = 0.0;
            String narration = "";
            ArrayList lstSubRsk = new ArrayList();
            int size = lstRisk.size();
            for (int i = 0; i < size; i++) {               
                lstSubRsk = new ArrayList();
                lstSubRsk = (ArrayList) lstRisk.get(i);
                transAmt = CommonUtil.convertObjToDouble(lstSubRsk.get(6));
                if(lstSubRsk.size() > 7){ //KD-3483
                 narration = CommonUtil.convertObjToStr(lstSubRsk.get(7));
                }            
                //To lOANS_ACCT_CHARGE_DETAILS
                TermLoanChargesTO termLoanChargeTO = new TermLoanChargesTO();
               if(mapRiskFnd.containsKey("PROD_NAME_DR") && CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")).equals("MDS")){
                   termLoanChargeTO.setProd_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_NAME_DR")));
                   String mdsActNum = CommonUtil.convertObjToStr(lstSubRsk.get(0))+"_1";
                   System.out.println("mdsActNum : "+mdsActNum);
                   termLoanChargeTO.setAct_num(mdsActNum);
                }else{
                   termLoanChargeTO.setProd_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_TYP_DR")));
                    termLoanChargeTO.setAct_num(CommonUtil.convertObjToStr(lstSubRsk.get(0)));
                }
                termLoanChargeTO.setProd_Id(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_ID_DR")));
                termLoanChargeTO.setCharge_Type(CommonUtil.convertObjToStr(mapRiskFnd.get("CHRG_TYP")));
                termLoanChargeTO.setChargeDt(curr_dt);
                termLoanChargeTO.setAmount(transAmt);
                termLoanChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                termLoanChargeTO.setStatus_By(CommonUtil.convertObjToStr(mapRiskFnd.get("USER_ID")));
                termLoanChargeTO.setStatus_Dt(curr_dt);
                termLoanChargeTO.setAuthorize_Dt(curr_dt);
                termLoanChargeTO.setAuthorize_by(CommonUtil.convertObjToStr(mapRiskFnd.get("USER_ID")));
                termLoanChargeTO.setAuthorize_Status(CommonConstants.STATUS_AUTHORIZED);
                termLoanChargeTO.setPaidAmount(0.0);
                termLoanChargeTO.setNarration(narration);
                sqlMap.executeUpdate("insertTermLoanChargeTO", termLoanChargeTO);      
                returnDataMap.put("STATUS", "SUCCESS");
            }          
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
    
     private void processRiskFundWithoutTransaction(HashMap mapRiskFnd, List lstRisk)  throws Exception {
         System.out.println("inside processRiskFundWithoutTransaction");
        try {           
            HashMap mapSubRisk1 = new HashMap();
            HashMap mapSubRisk = new HashMap();
            double transAmt = 0.0;
            String narration = "";
            ArrayList lstSubRsk = new ArrayList();
            ArrayList transferList = new ArrayList();
            int count = 0;
            int size = lstRisk.size();
            for (int i = 0; i < size; i++) {
                count++;
                lstSubRsk = new ArrayList();
                lstSubRsk = (ArrayList) lstRisk.get(i);                
                if(lstSubRsk.size() > 7){
                 narration = CommonUtil.convertObjToStr(lstSubRsk.get(7));
                }         
                transAmt = CommonUtil.convertObjToDouble(lstSubRsk.get(6));
                RiskFundTO objRiskfndTo = new RiskFundTO();
                objRiskfndTo.setActNum(CommonUtil.convertObjToStr(lstSubRsk.get(0)));
                objRiskfndTo.setRiskFundAmt(CommonUtil.convertObjToDouble(lstSubRsk.get(6)));
                objRiskfndTo.setMemNo(CommonUtil.convertObjToStr(lstSubRsk.get(1)));
                objRiskfndTo.setProdId(CommonUtil.convertObjToStr(mapRiskFnd.get("PROD_ID_DR")));
                objRiskfndTo.setBranchId(_branchCode);
                objRiskfndTo.setNarration(narration); //KD-3483
                sqlMap.executeUpdate("insRiskfundDetails", objRiskfndTo);
                returnDataMap.put("STATUS", "SUCCESS");
            }         
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
    
    
}
