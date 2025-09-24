/*
* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriLoanTransaction.java
 *
 * Created on June 15, 2009, 10:30 AM
 */
package com.see.truetransact.serverside.transaction.common.product.loan;

import com.see.truetransact.serverside.transaction.common.Transaction;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.termloan.agritermloan.AgriLoanDocumentValidationRule;
import com.see.truetransact.businessrule.termloan.agritermloan.AgriLoanLimitCheckingRule;
import com.see.truetransact.businessrule.termloan.agritermloan.AgriLoanRepaymentRule;
import com.see.truetransact.businessrule.transaction.AddressVerificationRule;
import com.see.truetransact.businessrule.transaction.ConfirmThanxRule;
import com.see.truetransact.businessrule.transaction.suspiciousconfig.SuspiciousConfigRule;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import org.apache.log4j.Logger;

import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.ArrayList;
import com.see.truetransact.commonutil.TTException;

//as andwhencustomer interestcalculation
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;

/**
 *
 * @author abi
 */
public class AgriLoanTransaction extends Transaction {

    private String _branchCode = null;
    private final String REPAYMENT_SCHEDULE_NO = "REPAYMENT_SCHEDULE_NO";

    /**
     * Creates a new instance of AgriLoanTransaction
     */
    public AgriLoanTransaction() throws ServiceLocatorException {
        super();

    }

    public void performOtherBalanceAdd(java.util.HashMap addShadow) throws Exception {
        printLog("performOtherBalanceAdd:" + addShadow);
        if (addShadow.containsKey("BRANCH_CODE") && addShadow.get("BRANCH_CODE") != null) {
            _branchCode = CommonUtil.convertObjToStr(addShadow.get("BRANCH_CODE"));
        }
        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        String repayment_Schedule_No = "";
        if (!addShadow.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(addShadow.get(REPAYMENT_SCHEDULE_NO));
        }

        addShadow.put(TransactionDAOConstants.ACCT_NO, act_num);
        addShadow.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            System.out.println("condition false");
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
            // Updates other Balances  (Clear balance and Total Balance)
            if (CommonUtil.convertObjToStr(addShadow.get("TRANS_TYPE")).equals(CommonConstants.DEBIT)) {
                sqlMap.executeUpdate("updateOtherBalancesATL", addShadow);
            } else {
                HashMap addshadow = new HashMap();
                addshadow.putAll(addShadow);
                double amount = CommonUtil.convertObjToDouble(addShadow.get("AMOUNT")).doubleValue();
                addshadow.put("AMOUNT", new Double(-amount));
                sqlMap.executeUpdate("updateOtherBalancesATL", addShadow);
            }
            if (CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
//                addShadow.put("TRANS_DT",objCashTransactionTO.getTransDt());
//                addShadow.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                java.util.List lst = sqlMap.executeQueryForList("getAgriTermLoanPaidDetails", addShadow);
                if (CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
                    HashMap intValue = new HashMap();
                    intValue = getAdvIntDetails(addShadow);
                    addShadow.putAll(intValue);
                    if (lst == null || lst.size() == 0) {
                        addShadow.put("TRN_CODE", "CLG");
                        addShadow.put("PRINCIPLE", intValue.get("PRINCIPAL_AMOUNT"));
                        addShadow.put("PBAL", intValue.get("BAL_PRINCIPAL"));
                        addShadow.put("INTEREST", intValue.get("INTEREST_AMOUNT"));
                        addShadow.put("IBAL", intValue.get("INTEREST_AMOUNT"));
                        addShadow.put("EXPENCE", new Double(0));
                        addShadow.put("EBAL", new Double(0));
                        //                        sqlMap.executeUpdate("insertintoloanTransDetails",addShadow);
                    }
                }
            }
        }
        addShadow = null;

    }

    public HashMap getAdvIntDetails(HashMap addShadow) throws Exception {
        addShadow.put("ACT_NUM", addShadow.get(TransactionDAOConstants.ACCT_NO));
        java.util.List getIntDetails = sqlMap.executeQueryForList("getIntDetailsATL", addShadow);
        HashMap hash = null;
        HashMap insertPenal = new HashMap();
        for (int i = 0; i < getIntDetails.size(); i++) {
            hash = (HashMap) getIntDetails.get(i);
            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_MODE"));
            if (trn_mode.equals("DI")) {
                //                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                //                       double prevAmt=iBal;
                insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                break;
            } else {
                insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
            }
        }
        double paidPrincipal = 0;
        double interest = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
        double Totprincipal = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
        double clearingAmt = CommonUtil.convertObjToDouble(addShadow.get(TransactionDAOConstants.CLEARING)).doubleValue();
        if (clearingAmt != 0) {
            if (clearingAmt > interest) {
                clearingAmt -= interest;
                paidPrincipal = clearingAmt;
                Totprincipal -= clearingAmt;
            } else {
                clearingAmt -= interest;
            }
        }
        insertPenal.put("PRINCIPAL_AMOUNT", new Double(paidPrincipal));
        insertPenal.put("INTEREST_AMOUNT", new Double(interest));
        insertPenal.put("BAL_PRINCIPAL", new Double(Totprincipal));
        System.out.println("interestDetails####" + insertPenal);


        return insertPenal;
    }

    public void performOtherBalanceMinus(java.util.HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        String repayment_Schedule_No = "";
        if (!addShadow.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(addShadow.get(REPAYMENT_SCHEDULE_NO));
        }

        addShadow.put(TransactionDAOConstants.ACCT_NO, act_num);
        addShadow.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.UNCLEAR_AMT).toString())));
            }

            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }


            addShadow.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
            printLog("performOtherBalanceMinus:after:" + addShadow);
            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesATL", addShadow);

        }
        addShadow = null;

    }

    public void performShadowAdd(java.util.HashMap addShadow) throws Exception {
        printLog("performShadowAdd:before:" + addShadow);
        boolean debit_int = false;
        if (addShadow.containsKey("DEBIT_INT")) {
            Boolean debit_int1 = (Boolean) addShadow.get("DEBIT_INT");
            debit_int = debit_int1.booleanValue();
        }

        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        String repayment_Schedule_No = "";
        if (!addShadow.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(addShadow.get(REPAYMENT_SCHEDULE_NO));
        }

        addShadow.put(TransactionDAOConstants.ACCT_NO, act_num);
        addShadow.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if ((CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                if (addShadow.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_CREATED)) {
                    if (addShadow.containsKey(TransactionDAOConstants.CHARGES)) {
                        if (addShadow.get(TransactionDAOConstants.CHARGES) != null) {
                            System.out.println("processing charges  $########" + addShadow.get(TransactionDAOConstants.CHARGES));
                            updateProcessingChargeinDisbursement(addShadow);
                        }
                    } else if (debit_int) {  //for debit int
                    } else if (addShadow.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_CREATED) && debit_int == false) {
                        insertLoanDisbursementDetails(addShadow);
                    }
                } else if (addShadow.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_MODIFIED)) {
                    updateLoanDisbursementDetails(addShadow);
                }
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                sqlMap.executeUpdate("updateShadowDebitATL", addShadow);
                //                if (addShadow.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REJECTED)){
                //                    authorizeDisbursementDetails(addShadow);
                //                }
            } else {
                sqlMap.executeUpdate("updateShadowCreditATL", addShadow);
                //                creditLoanAdvancesDetail(addShadow);
                //                if(addShadow.containsKey("PIBAL")) testing
                //                    sqlMap.executeUpdate("insertintoloanTransDetails",addShadow);
            }
            //        printLog ("performShadowAdd:after:" + addShadow);
        }
        addShadow = null;

    }

    public void performShadowMinus(java.util.HashMap minusMap) throws Exception {
        printLog("performShadowMinus:before:" + minusMap);

        String act_num = CommonUtil.convertObjToStr(minusMap.get(TransactionDAOConstants.ACCT_NO));
        String repayment_Schedule_No = "";
        if (!minusMap.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(minusMap.get(REPAYMENT_SCHEDULE_NO));
        }

        minusMap.put(TransactionDAOConstants.ACCT_NO, act_num);
        minusMap.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if ((CommonUtil.convertObjToStr(minusMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitATL", minusMap);
            } else {
                sqlMap.executeUpdate("updateShadowCreditATL", minusMap);
                //                 doIntTransaction(minusMap);   //for transfer int recivable to received
            }

            if (minusMap.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_DELETED)) {
                deleteLoanDisbursementDetails(minusMap);
            }
            //            if (minusMap.get(TransactionDAOConstants.AUTHORIZE_STATUS)!=null)
            //                if (minusMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REJECTED) ||
            //                minusMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_AUTHORIZED)){
            //                    authorizeDisbursementDetails(minusMap);
            //                }

            printLog("performShadowMinus:after:" + minusMap);
        }
        minusMap = null;

    }

    void doIntTransaction(HashMap hash) {
        System.out.println("doIntTransaction#####" + hash);
        try {
            HashMap txMap = new HashMap();
            double paidIntAmt = CommonUtil.convertObjToDouble(hash.get("PAID_INT")).doubleValue();
            _branchCode = CommonUtil.convertObjToStr(hash.get("BRANCH_CODE"));
            ArrayList transferList = new ArrayList();
            if (CommonUtil.convertObjToStr(hash.get("INT_PAYABLE_ACHD")).length() > 0 && CommonUtil.convertObjToStr(hash.get("AC_DEBIT_INT")).length() > 0) {
                txMap.put(TransferTrans.DR_AC_HD, hash.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, hash.get("BRANCH_CODE"));
                txMap.put(TransferTrans.CR_AC_HD, hash.get("AC_DEBIT_INT"));
                txMap.put(TransferTrans.CR_BRANCH, hash.get("BRANCH_CODE"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CURRENCY, "INR");
                //                txMap.put(TransferTrans.PARTICULARS,"Interest Paid As On" + ServerUtil.getCurrentDate(_branchCode));
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));

                TransferTrans trans = new TransferTrans();
                trans.setLinkBatchId(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                trans.setTransMode(CommonConstants.TX_TRANSFER);
                trans.setInitiatedBranch(_branchCode);

                transferList.add(trans.getDebitTransferTO(txMap, paidIntAmt));
                transferList.add(trans.getCreditTransferTO(txMap, paidIntAmt));
                trans.doDebitCredit(transferList, _branchCode, true);
            } else {
                throw new TTException("INT PAYABLE OR INT RECIVED ACCOUNT NOT SET ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void doNPATransaction(HashMap hash) {
        System.out.println("doIntTransaction#####" + hash);
        try {
            HashMap txMap = new HashMap();
            double paidIntAmt = CommonUtil.convertObjToDouble(hash.get("NPA_AMOUNT")).doubleValue();
            //            if(paidIntAmt==0)
            //                paidIntAmt=CommonUtil.convertObjToDouble(hash.get("NPA_PENAL")).doubleValue();
            _branchCode = CommonUtil.convertObjToStr(hash.get("BRANCH_CODE"));
            ArrayList transferList = new ArrayList();
            if (paidIntAmt != 0) {
                if (CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")).length() > 0 && CommonUtil.convertObjToStr(hash.get("INT_PAYABLE_ACHD")).length() > 0) {
                    txMap.put(TransferTrans.DR_AC_HD, hash.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.DR_ACT_NUM, hash.get("ACCOUNTNO"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.AGRILOANS);
                    txMap.put(TransferTrans.DR_BRANCH, hash.get("BRANCH_CODE"));

                    txMap.put(TransferTrans.CR_AC_HD, hash.get("INT_PAYABLE_ACHD"));
                    txMap.put(TransferTrans.CR_BRANCH, hash.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    //                txMap.put(TransferTrans.PARTICULARS,"Interest Paid As On" + ServerUtil.getCurrentDate(_branchCode));
                    txMap.put(TransferTrans.PARTICULARS, "INTEREST UPTO" + ServerUtil.getCurrentDate(_branchCode));//CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));

                    TransferTrans trans = new TransferTrans();
                    trans.setLinkBatchId(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    trans.setTransMode(CommonConstants.TX_TRANSFER);
                    trans.setInitiatedBranch(_branchCode);

                    if (hash.containsKey("NPA_DI")) {
                        trans.setLoanDebitInt("NPA_DI");
                    } else {
                        trans.setLoanDebitInt("NPA_DPI");
                    }

                    transferList.add(trans.getDebitTransferTO(txMap, paidIntAmt));
                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    transferList.add(trans.getCreditTransferTO(txMap, paidIntAmt));
                    trans.doDebitCredit(transferList, _branchCode, true);
                } else {
                    throw new TTException("INT PAYABLE OR INT RECIVED ACCOUNT NOT SET ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAvailableBalance(java.util.HashMap updateMap) throws Exception {
        printLog("updateAvailableBalance:" + updateMap);
        _branchCode = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.BRANCH_CODE));
        String act_num = CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.ACCT_NO));
        String repayment_Schedule_No = "";
        if (!updateMap.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(updateMap.get(REPAYMENT_SCHEDULE_NO));
        }

        updateMap.put(TransactionDAOConstants.ACCT_NO, act_num);
        updateMap.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                updateMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
            if ((CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                java.util.List lst = sqlMap.executeQueryForList("getATLBalance", updateMap);
                if (lst != null && lst.size() > 0) {
                    HashMap hash = (HashMap) lst.get(0);
                    double availableBalance = CommonUtil.convertObjToDouble(hash.get("AVAILABLE_BALANCE")).doubleValue();
                    double amount = CommonUtil.convertObjToDouble(updateMap.get("AMOUNT")).doubleValue();
                    amount *= -1;
                    if (updateMap.containsKey("DEBIT_LOAN_TYPE") && updateMap.get("DEBIT_LOAN_TYPE") != null && updateMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
                        sqlMap.executeUpdate("updateAvailBalanceATL", updateMap);
                    } else {
                        updateMap.put("LAST_INT_CALC_DT", updateMap.get("TODAY_DT"));
                        System.out.println("updateavailbal##" + updateMap);
                        sqlMap.executeUpdate("updateAvailableBalanceATL", updateMap);
                    }
                }
                //                else
                //                    updateMap.put("LAST_INT_CALC_DT",updateMap.get("TODAY_DT"));
                //                System.out.println("updateMap#####"+updateMap);
                //                sqlMap.executeUpdate("updateAvailBalanceTL", updateMap);
            } else {
                sqlMap.executeUpdate("updateAvailableBalanceATL", updateMap);//by abi for notneed availablebala updatation for credit
            }
            if (updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS) != null) {
                if (//updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REJECTED) ||
                        updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_AUTHORIZED)
                        || updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REALIZED)) {
                    if (!updateMap.containsKey("CLEARING_BOUNCED")) {
                        authorizeDisbursementDetails(updateMap);
                    }
                    System.out.println("updatemap####" + updateMap);
                }
            }

            updateLoanBalancePrincipal(updateMap);

        }
        updateMap = null;

    }

    public void validateRules(java.util.HashMap validateMap, boolean isException) throws Exception {
        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.AGRILOANS);

        String act_num = CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.ACCT_NO));

        String repayment_Schedule_No = "";
        if (!validateMap.containsKey(REPAYMENT_SCHEDULE_NO) && act_num.indexOf("_") >= 0) {
            repayment_Schedule_No = act_num.substring(act_num.indexOf("_") + 1, act_num.length());
            act_num = act_num.substring(0, act_num.indexOf("_"));
        } else {
            repayment_Schedule_No = CommonUtil.convertObjToStr(validateMap.get(REPAYMENT_SCHEDULE_NO));
        }

        validateMap.put(TransactionDAOConstants.ACCT_NO, act_num);
        validateMap.put(REPAYMENT_SCHEDULE_NO, repayment_Schedule_No);

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        ArrayList list = new ArrayList();


        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
            context.addRule(new SuspiciousConfigRule());
        }

        if (CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            // Validation only for Debit
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                if (!CommonUtil.convertObjToStr(validateMap.get("PARTICULARS")).equals("DEBIT_INT")) {
                    context.addRule(new AgriLoanLimitCheckingRule());
                }
                context.addRule(new AddressVerificationRule());
                context.addRule(new ConfirmThanxRule());
                //context.addRule(new LoanDocumentValidationRule());
            }

            list = (ArrayList) engine.validateAll(context, validateMap);

        } else if (CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
            // Validation only for Credit
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                //                context.addRule(new LoanLimitCheckingRule());
                context.addRule(new AgriLoanRepaymentRule());
                //context.addRule(new LoanDocumentValidationRule());
            }

            // Validate the rules here
            list = (ArrayList) engine.validateAll(context, validateMap);

        }

        context = null;
        engine = null;

        if (list != null) {
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            System.out.println("Exception List : " + list);

            throw new TTException(exception);
        }


    }

    public void updateGL(String acctHead, Double amount, LogTO objLogTO, HashMap ruleMap) throws Exception {
        System.out.println("glupdate#####" + ruleMap);
        if (ruleMap != null) {
            if (CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                super.updateGL(acctHead, amount, objLogTO, ruleMap);
            } else if (CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                if (ruleMap.containsKey(TransactionDAOConstants.PROD_TYPE) && ruleMap.get(TransactionDAOConstants.PROD_TYPE).equals(TransactionFactory.GL)) {
                    super.updateGL(acctHead, amount, objLogTO, ruleMap);
                } else {

                    //                    RepaymentCalculator repaymentCalculator = new RepaymentCalculator(ruleMap); not need interest calculation for authorize time collect information from loan installment table
                    //                    HashMap interestDetailsMap = repaymentCalculator.calculateRepaymentInterestAmt();
                    HashMap interestDetailsMap = null;
                    //                    if(CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)){
                    //                        interestDetailsMap.put("PRINCIPAL_AMOUNT",ruleMap.get(TransactionDAOConstants.AMT));
                    //
                    //                    }
                    //                    else
                    //                    interestDetailsMap =getLoanPaidAmount(ruleMap);
                    //NPA
                    //                     double amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("NPA_INTEREST")).doubleValue();
                    //                     if(amt>0){
                    //                         interestDetailsMap.put("DEBIT_ACHD",acctHead);
                    //                     doNPATransaction(interestDetailsMap);
                    //                     }
                    //                       amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("NPA_PENAL")).doubleValue();
                    //                     if(amt>0){
                    //                         interestDetailsMap.put("DEBIT_ACHD",acctHead);
                    //                     doNPATransaction(interestDetailsMap);
                    //                     }
                    //END NPA
                    interestDetailsMap = getLoanPaidAmount(ruleMap);

                    interestDetailsMap.put("PRINCIPAL_AMOUNT", ruleMap.get(TransactionDAOConstants.AMT));
                    double amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("PRINCIPAL_AMOUNT")).doubleValue();

                    if (amt != 0) {
                        // Main Account Head
                        super.updateGL(acctHead, new Double(amt), objLogTO, ruleMap);
                    }

                    amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("INTEREST_AMOUNT")).doubleValue();
                    if (amt != 0) {
                        // Debit Interest Account Head
                        //                        super.updateGL(CommonUtil.convertObjToStr(interestDetailsMap.get("AC_DEBIT_INT")), new Double(amt), objLogTO, ruleMap);  not need ed amount transfer from int recivable to int received
                        interestDetailsMap.put("PAID_INT", new Double(amt));
                        System.out.println("interestDetailsMap######" + interestDetailsMap);
                        interestDetailsMap.put("CREDIT_ACHD", interestDetailsMap.get("AC_CREDIT_INT"));
                        if ((interestDetailsMap.containsKey("BEHAVES_LIKE") && interestDetailsMap.get("BEHAVES_LIKE") != null && CommonUtil.convertObjToStr(
                                interestDetailsMap.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")) || (interestDetailsMap.containsKey("AS_CUSTOMER_COMES") && interestDetailsMap.get("AS_CUSTOMER_COMES") != null && interestDetailsMap.get("AS_CUSTOMER_COMES").equals("Y"))) {
                        } else {
                            doIntTransaction(interestDetailsMap);
                        }
                        interestDetailsMap.remove("PAID_INT");
                        interestDetailsMap.remove("CREDIT_ACHD");
                    }

                    amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("PENUL_INTEREST_AMOUNT")).doubleValue();
                    if (amt != 0) {
                        // Penalty Interest Account Head
                        //                        super.updateGL(CommonUtil.convertObjToStr(interestDetailsMap.get("PENAL_INT")), new Double(amt), objLogTO, ruleMap); int receivable to received
                        interestDetailsMap.put("PAID_INT", new Double(amt));
                        interestDetailsMap.put("CREDIT_ACHD", interestDetailsMap.get("PENAL_INT"));
                        if ((interestDetailsMap.containsKey("BEHAVES_LIKE") && interestDetailsMap.get("BEHAVES_LIKE") != null && CommonUtil.convertObjToStr(
                                interestDetailsMap.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")) || (interestDetailsMap.containsKey("AS_CUSTOMER_COMES") && interestDetailsMap.get("AS_CUSTOMER_COMES") != null && interestDetailsMap.get("AS_CUSTOMER_COMES").equals("Y"))) {
                        } else {
                            doIntTransaction(interestDetailsMap);
                        }
                        interestDetailsMap.remove("PENAL_INT");
                    }
                }
            }
        } else {
            super.updateGL(acctHead, amount, objLogTO, ruleMap);
        }
    }

    private String getLoanDisbursementKey(HashMap validateMap) throws Exception {
        java.util.List keyList = sqlMap.executeQueryForList("getDisbursementDetailsKeyATL", validateMap);
        HashMap keyMap = (HashMap) keyList.get(0);

        int lastDisbursementID = CommonUtil.convertObjToInt(keyMap.get("MAX_DISBURSEMENT_ID"));
        return String.valueOf(lastDisbursementID + 1);
    }

    private void insertLoanDisbursementDetails(HashMap validateMap) throws Exception {
        System.out.println("validateMap####" + validateMap);
        if (validateMap.containsKey("LOANPARTICULARS") && validateMap.get("LOANPARTICULARS") != null && CommonUtil.convertObjToStr(validateMap.get("LOANPARTICULARS")).length() > 0) {
            validateMap.put("DEBIT_LOAN_TYPE", validateMap.get("LOANPARTICULARS"));
        }
        HashMap repayDetailsMap = getDisbursementDetails(validateMap);
        if (validateMap.containsKey("DEBIT_LOAN_TYPE") && validateMap.get("DEBIT_LOAN_TYPE") != null && validateMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
            sqlMap.executeUpdate("insertLoansDisbursementDetailsATL", repayDetailsMap);
            sqlMap.executeUpdate("updateRepayScheduleNoInLoansDisbursementATL", repayDetailsMap);
        }
        validateMap.put("EXPENSE", new Long(0));
        validateMap.put("EBAL", new Long(0));
        validateMap.put("DISBURSEMENT_AMT", repayDetailsMap.get("DISBURSEMENT_AMT"));
        if (validateMap.containsKey("DEBIT_LOAN_TYPE") && validateMap.get("DEBIT_LOAN_TYPE") != null && validateMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
            validateMap.put("TRN_CODE", validateMap.get("DEBIT_LOAN_TYPE"));
        }
        if (validateMap.containsKey("DEBIT_LOAN_TYPE") && validateMap.get("DEBIT_LOAN_TYPE") != null && validateMap.get("DEBIT_LOAN_TYPE").equals("DI")) {
            validateMap.put("TRN_CODE", validateMap.get("DEBIT_LOAN_TYPE"));
        }
        if (validateMap.containsKey("DEBIT_LOAN_TYPE") && validateMap.get("DEBIT_LOAN_TYPE") != null && validateMap.get("DEBIT_LOAN_TYPE").equals("DPI")) {
            validateMap.put("TRN_CODE", validateMap.get("DEBIT_LOAN_TYPE"));
        }
        if (validateMap.containsKey("DEBIT_LOAN_TYPE") && validateMap.get("DEBIT_LOAN_TYPE") != null && validateMap.get("DEBIT_LOAN_TYPE").equals("OTHERCHARGES")) {
            validateMap.put("EXPENSE", validateMap.get("DISBURSEMENT_AMT"));
            validateMap.put("EBAL", validateMap.get("DISBURSEMENT_AMT"));
            validateMap.put("DISBURSEMENT_AMT", new Long(0));
        }
        validateMap.put("TRN_CODE", validateMap.get("DEBIT_LOAN_TYPE"));
        //        sqlMap.executeUpdate("insertLoansDisbursementDetailsCumLoan", validateMap);
        repayDetailsMap = null;
    }

    private void updateLoanDisbursementDetails(HashMap validateMap) throws Exception {
        sqlMap.executeUpdate("updateAgriLoansDisbursementDetails", getDisbursementDetails(validateMap));
    }

    private void deleteLoanDisbursementDetails(HashMap validateMap) throws Exception {
        sqlMap.executeUpdate("deleteAgriLoansDisbursementDetails", getDisbursementDetails(validateMap));
    }

    private void authorizeDisbursementDetails(HashMap authorizeMap) throws Exception {
        if ((CommonUtil.convertObjToStr(authorizeMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            sqlMap.executeUpdate("authorizeLoansDisbursementDetailsATL", authorizeMap);
        }
        if (authorizeMap.get(TransactionDAOConstants.TRANS_TYPE).equals("CREDIT")) {
            HashMap map = creditLoanAdvancesDetail(authorizeMap);
            map.put(TransactionDAOConstants.TRANS_TYPE, authorizeMap.get(TransactionDAOConstants.TRANS_TYPE));
            map = insertLoanCredit(map);
            authorizeMap.putAll(map);
        } else {
            insertAuthorizeLoanDebit(authorizeMap);
        }
        //        sqlMap.executeUpdate("authorizeLoansDisbursementCumLoanDetails", authorizeMap);//commented for testing
        //        asAnWhenCustomer(authorizeMap);
    }

    private void updateLoanBalancePrincipal(HashMap updateMap) throws Exception {
        HashMap balancePrincipalMap = new HashMap();
        balancePrincipalMap.putAll(updateMap);
        _branchCode = CommonUtil.convertObjToStr(balancePrincipalMap.get(TransactionDAOConstants.BRANCH_CODE));
        double amount = Double.parseDouble(CommonUtil.convertObjToStr(balancePrincipalMap.get(TransactionDAOConstants.AMT)));
        double modifiedAmount = 0.0;
        String transType = CommonUtil.convertObjToStr(balancePrincipalMap.get("TRANSTYPE"));
        if (transType.equals("CREDIT")) {
            //            java.util.List lst=sqlMap.executeQueryForList("getIntAmtForGLUpdate",balancePrincipalMap);
            //            System.out.println("beforegetIntforgl###"+lst);
            //            if(lst != null && lst.size()>0){}
            //            else
            //                lst=sqlMap.executeQueryForList("getTermLoanminusPrincipal",balancePrincipalMap);
            //            if(lst !=null && lst.size()>0){
            //                HashMap hash=(HashMap)lst.get(0);
            double principal = CommonUtil.convertObjToDouble(updateMap.get("PRINCIPAL_AMOUNT")).doubleValue();

            if (principal > 0) {
                balancePrincipalMap.put("LAST_REPAY_DT", ServerUtil.getCurrentDate(_branchCode));
                balancePrincipalMap.remove("AMOUNT");
                balancePrincipalMap.put("AMOUNT", new Double(-principal));
                sqlMap.executeUpdate("updateAgriLoanBalancePrincipalDetails", balancePrincipalMap);

            }
            //            }
        } else {
            if (updateMap.containsKey("DEBIT_LOAN_TYPE") && updateMap.get("DEBIT_LOAN_TYPE") != null && updateMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
                if (amount < 0) {
                    modifiedAmount = -amount;
                    balancePrincipalMap.put(TransactionDAOConstants.AMT, new Double(modifiedAmount));
                } else {
                    modifiedAmount = amount;
                    balancePrincipalMap.put(TransactionDAOConstants.AMT, new Double(modifiedAmount));
                }
                balancePrincipalMap.put("LAST_REPAY_DT", ServerUtil.getCurrentDate(_branchCode));
                sqlMap.executeUpdate("updateAgriLoanBalancePrincipalDetails", balancePrincipalMap);
                if (amount < 0) {
                    amount = (-1) * modifiedAmount;
                    balancePrincipalMap.put(TransactionDAOConstants.AMT, new Double(amount));
                }
            }
        }
    }
    //dont delete the methode if as an when customer comes last int caldate updation need tally  (principal  + interest)
    //means enable this methode and disable transfer dao cash dao

    private void asAnWhenCustomer(HashMap authorizeMap) throws Exception {
        System.out.println("authorizeMap#####" + authorizeMap);
        HashMap getDateMap = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        getDateMap.put("ACT_NUM", authorizeMap.get(TransactionDAOConstants.ACCT_NO));
        getDateMap.put("ACCT_NUM", authorizeMap.get(TransactionDAOConstants.ACCT_NO));
        getDateMap.put("PROD_ID", authorizeMap.get(TransactionDAOConstants.PROD_ID));
        getDateMap.put("BRANCH_ID", _branchCode);
        HashMap depositcummap = (HashMap) ((List) sqlMap.executeQueryForList("getDepositBehavesforLoan", getDateMap));
        if (depositcummap != null && depositcummap.size() > 0 && depositcummap.containsKey("BEHAVES_LIKE") && depositcummap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            getDateMap.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
            getDateMap.put("INITIATED_BRANCH", _branchCode);
            List list = sqlMap.executeQueryForList("IntCalculationDetail", getDateMap);
            getDateMap = (HashMap) list.get(0);
            if (getDateMap.get("AS_CUSTOMER_COMES") != null && getDateMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                InterestCalculationTask interestCalTask = new InterestCalculationTask(header);
                authorizeMap.put("WHERE", authorizeMap.get(TransactionDAOConstants.ACCT_NO));
                List lst = (List) sqlMap.executeQueryForList("getLastIntCalDate", authorizeMap);
                getDateMap = (HashMap) lst.get(0);
                String prod_id = CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
                authorizeMap.put("PROD_ID", prod_id);
                Date CURR_DATE = new Date();
                CURR_DATE = ServerUtil.getCurrentDateProperFormat(_branchCode);
                System.out.println("curr_date###1" + CURR_DATE);
                getDateMap.put("CURR_DATE", CURR_DATE);
                getDateMap.put("BRANCH_ID", _branchCode);
                getDateMap.put("BRANCH_CODE", _branchCode);
                HashMap behaveLike = (HashMap) (sqlMap.executeQueryForList("getLoanBehaves", authorizeMap).get(0));
                getDateMap.put("ACT_NUM", authorizeMap.get(TransactionDAOConstants.ACCT_NO));
                getDateMap.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
                getDateMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                System.out.println("before od interest calculation####" + getDateMap);
                HashMap resultMap = new HashMap();
                resultMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
                double penalInt = 0;
                double interest = 0;
                if (resultMap != null && resultMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                    penalInt = CommonUtil.convertObjToDouble(resultMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    interest = CommonUtil.convertObjToDouble(resultMap.get("INTEREST")).doubleValue();
                }
                //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
                //                getDateMap=(HashMap)lst.get(0);
                //        System.out.println("OD  #####!"+getDateMap);
                //                    returnMap.put("AccountInterest",getDateMap.get("INTEREST"));
                //        returnMap.put("AccountInterest",new Double(interest));
                //        returnMap.put("AccountPenalInterest",new Double(penalInt));
                //calculated interest
                double totCalInterest = penalInt + interest;
                HashMap accDetailMap = new HashMap();
                accDetailMap.put("ACT_NUM", getDateMap.get("ACT_NUM"));
                accDetailMap.put("FROM_DT", getDateMap.get("LAST_INT_CALC_DT"));
                accDetailMap.put("TO_DATE", getDateMap.get("CURR_DATE"));
                //paid interest
                lst = sqlMap.executeQueryForList("getPaidPrinciple", accDetailMap);
                accDetailMap = (HashMap) lst.get(0);
                double paidInt = CommonUtil.convertObjToDouble(accDetailMap.get("INTEREST")).doubleValue();
                double paidPenalInt = CommonUtil.convertObjToDouble(accDetailMap.get("PENAL")).doubleValue();
                accDetailMap.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(CURR_DATE, -1));
                accDetailMap.put(TransactionDAOConstants.ACCT_NO, getDateMap.get("ACT_NUM"));
                if (totCalInterest <= (paidInt + paidPenalInt)) {
                    sqlMap.executeUpdate("updateclearBal", accDetailMap);
                }
            }
        }
    }

    private void insertAuthorizeLoanDebit(HashMap authorizeMap) throws Exception {
        System.out.println("insertauthorizeloandebit####" + authorizeMap);
        authorizeMap.put("ACT_NUM", authorizeMap.get("ACCOUNTNO"));
        if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && (!authorizeMap.get("DEBIT_LOAN_TYPE").equals("CLEARING_BOUNCED"))) {
            List maxLoanTrans = sqlMap.executeQueryForList("getIntDetailsATL", authorizeMap);
            HashMap maxLoanTransmap = null;
            int trans_slno = 1;
            if (maxLoanTrans != null && maxLoanTrans.size() > 0) {
                maxLoanTransmap = (HashMap) maxLoanTrans.get(maxLoanTrans.size() - 1);
                trans_slno = CommonUtil.convertObjToInt(maxLoanTransmap.get("TRANS_SLNO"));
                trans_slno++;
            }

            System.out.println("maxloantransMap##########" + maxLoanTransmap);
            if (maxLoanTransmap != null) {
                double amount = CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue();
                if (amount < 0) {
                    amount *= (-1);
                }
                //            authorizeMap.putAll(maxLoanTransmap);

                authorizeMap.put("TRN_CODE", authorizeMap.get("DEBIT_LOAN_TYPE"));
                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && (authorizeMap.get("DEBIT_LOAN_TYPE").equals("DP") || authorizeMap.get("DEBIT_LOAN_TYPE").equals("CLEARING_BOUNCED"))) {
                    authorizeMap.put("PBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue()));
                    authorizeMap.put("PRINCIPAL", new Double(amount));
                    if (authorizeMap.get("DEBIT_LOAN_TYPE").equals("CLEARING_BOUNCED")) {
                        authorizeMap.put("TRN_CODE", "OLG");
                    }
                    maxLoanTransmap.remove("PBAL");
                    maxLoanTransmap.remove("PRINCIPLE");
                } else {
                    authorizeMap.put("PBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue()));
                    authorizeMap.put("PRINCIPLE", new Double(0));
                }
                if ((authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("DI"))
                        || (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("NPA_DI"))) {
                    authorizeMap.put("IBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue()));
                    authorizeMap.put("INTEREST", new Double(amount));
                    if (authorizeMap.get("DEBIT_LOAN_TYPE").equals("NPA_DI")) {
                        authorizeMap.put("TRN_CODE", "DI");
                        authorizeMap.put("NPA_INTEREST", new Double(amount));
                        double npa = CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INT_BAL")).doubleValue();
                        authorizeMap.put("NPA_INT_BAL", new Double(npa - amount));
                    }
                    //                maxLoanTransmap.remove("IBAL");
                    //                maxLoanTransmap.remove("INTEREST");
                } else {
                    authorizeMap.put("IBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue()));
                    authorizeMap.put("INTEREST", new Double(0));
                    authorizeMap.put("NPA_INT_BAL", maxLoanTransmap.get("NPA_INT_BAL"));
                }
                if ((authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("DPI"))
                        || (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("NPA_DPI"))) {
                    authorizeMap.put("PIBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue()));
                    authorizeMap.put("PENAL", new Double(amount));
                    if (authorizeMap.get("DEBIT_LOAN_TYPE").equals("NPA_DPI")) {
                        authorizeMap.put("TRN_CODE", "DPI");
                        authorizeMap.put("NPA_PENAL", new Double(amount));
                        double npa = CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL")).doubleValue();
                        authorizeMap.put("NPA_PENAL_BAL", new Double(npa - amount));
                    }
                    //                maxLoanTransmap.remove("PIBAL");
                    //                maxLoanTransmap.remove("PENAL");
                } else {
                    authorizeMap.put("PIBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue()));
                    authorizeMap.put("PENAL", new Double(0));
                    authorizeMap.put("NPA_PENAL_BAL", maxLoanTransmap.get("NPA_PENAL_BAL"));

                }
                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("OTHERCHARGES")) {
                    authorizeMap.put("EBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue()));
                    authorizeMap.put("EXPENSE", new Double(amount));
                    //                maxLoanTransmap.remove("EBAL");
                    //                maxLoanTransmap.remove("EXPENSE");
                } else {
                    authorizeMap.put("EBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue()));
                    authorizeMap.put("EXPENSE", new Double(0));
                }
                if (authorizeMap.get("DEBIT_LOAN_TYPE").equals("OTHERCHARGES") || authorizeMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
                    authorizeMap.put("NPA_INT_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INT_BAL")).doubleValue()));
                    authorizeMap.put("NPA_INTEREST", new Double(0));
                    authorizeMap.put("NPA_PENAL_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL")).doubleValue()));
                    authorizeMap.put("NPA_PENAL", new Double(0));
                }
                authorizeMap.put("EXCESS_AMT", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("EXCESS_AMT")).doubleValue()));
                //            maxLoanTransmap.remove("TRN_CODE");
                //            maxLoanTransmap.remove("TRANS_DT");
                //            authorizeMap.putAll(maxLoanTransmap);
            } else {
                authorizeMap.put("TRN_CODE", authorizeMap.get("DEBIT_LOAN_TYPE"));
                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && (authorizeMap.get("DEBIT_LOAN_TYPE").equals("DP") || authorizeMap.get("DEBIT_LOAN_TYPE").equals("CLEARING_BOUNCED"))) {
                    authorizeMap.put("PBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                    authorizeMap.put("PRINCIPAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                    authorizeMap.put("TRN_CODE", "DP");
                } else {
                    authorizeMap.put("PBAL", new Double(0));
                    authorizeMap.put("PRINCIPAL", new Double(0));
                }

                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("DI")) {
                    authorizeMap.put("IBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                    authorizeMap.put("INTEREST", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                } else {
                    authorizeMap.put("IBAL", new Double(0));
                    authorizeMap.put("INTEREST", new Double(0));

                }

                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("DPI")) {
                    authorizeMap.put("PIBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                    authorizeMap.put("PENAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                } else {
                    authorizeMap.put("PIBAL", new Double(0));
                    authorizeMap.put("PENAL", new Double(0));
                }

                if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && authorizeMap.get("DEBIT_LOAN_TYPE").equals("OTHERCHARGES")) {
                    authorizeMap.put("EBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                    authorizeMap.put("EXPENSE", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue() * (-1)));
                } else {
                    authorizeMap.put("EBAL", new Double(0));
                    authorizeMap.put("EXPENSE", new Double(0));
                }
                authorizeMap.put("NPA_INT_BAL", new Double(0));
                authorizeMap.put("NPA_INTEREST", new Double(0));
                authorizeMap.put("NPA_PENAL_BAL", new Double(0));
                authorizeMap.put("NPA_PENAL", new Double(0));
                authorizeMap.put("EXCESS_AMT", new Double(0));
            }
            authorizeMap.put("ARBITARY_CHARGE", new Double(0));
            authorizeMap.put("ARBITARY_CHARGE_BAL", new Double(0));
            authorizeMap.put("POSTAGE_CHARGE", new Double(0));
            authorizeMap.put("POSTAGE_CHARGE_BAL", new Double(0));
            authorizeMap.put("INSURANCE_CHARGE", new Double(0));
            authorizeMap.put("INSURANCE_CHARGE_BAL", new Double(0));
            authorizeMap.put("EXE_DEGREE", new Double(0));
            authorizeMap.put("EXE_DEGREE_BAL", new Double(0));
            authorizeMap.put("MISC_CHARGES", new Double(0));
            authorizeMap.put("MISC_CHARGES_BAL", new Double(0));
            authorizeMap.put("LEGAL_CHARGE", new Double(0));
            authorizeMap.put("LEGAL_CHARGE_BAL", new Double(0));
            authorizeMap.put("UPTO_DT_INT", String.valueOf("N"));

            //        if(authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") !=null
            //        && CommonUtil.convertObjToStr(authorizeMap.get("DEBIT_LOAN_TYPE")).equals("CLEARING_BOUNCED")){
            //            authorizeMap.put("AUTHORIZE_STATUS", "REJECTED");
            //        }

            authorizeMap.put("TRANS_SLNO", new Long(trans_slno));
            System.out.println("authorizeMap####befor insert" + authorizeMap);
            sqlMap.executeUpdate("insertAgriLoansDisbursementDetailsCumLoan", authorizeMap);
        }
    }

    private HashMap insertLoanCredit(HashMap insertmap) throws Exception {
        HashMap inputMap = new HashMap();
        System.out.println("insertmap####" + insertmap);
        inputMap.putAll(insertmap);
        double amount = CommonUtil.convertObjToDouble(insertmap.get(TransactionDAOConstants.AMT)).doubleValue();
        if (amount < 0) {
            amount = amount * (-1);
        }
        //                if(insertmap.get("LOANPARTICULARS")!=null ){
        String debitLoanType = CommonUtil.convertObjToStr(insertmap.get("DEBIT_LOAN_TYPE"));
        double pInt = CommonUtil.convertObjToDouble(insertmap.get("PENAL_INT")).doubleValue();
        double Int = CommonUtil.convertObjToDouble(insertmap.get("CURR_MONTH_INT")).doubleValue();
        double principelBal = CommonUtil.convertObjToDouble(insertmap.get("PRINCIPLE_BAL")).doubleValue();
        double payableprincipelBal = CommonUtil.convertObjToDouble(insertmap.get("PAYABLE_PRINCIPLE_BAL")).doubleValue();
        double ebal = CommonUtil.convertObjToDouble(insertmap.get("EBAL")).doubleValue();
        double pbal = CommonUtil.convertObjToDouble(insertmap.get("PBAL")).doubleValue();
        double npa_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_INT_BAL")).doubleValue();
        double npa_Penal_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_PENAL_BAL")).doubleValue();
        double totInt = pInt + Int + ebal;
        double principle = 0;
        double paidPenal = pInt;
        double paidInt = Int;
        double paidebal = ebal;
        double paid_npa_bal = 0;//npa_bal;
        double paid_npa_penal_bal = 0;//npa_Penal_bal;
        double excessAmt = 0;
        HashMap asAndWhenMap = new HashMap();
        String asAnWhenCustomer = "";
        asAndWhenMap.put("ACT_NUM", insertmap.get("ACCOUNTNO"));
        asAndWhenMap.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
        asAndWhenMap.put("INITIATED_BRANCH", _branchCode);
        List lstAsAndWhen = sqlMap.executeQueryForList("IntCalculationDetail", asAndWhenMap);
        if (lstAsAndWhen != null && lstAsAndWhen.size() > 0) {
            asAndWhenMap = (HashMap) lstAsAndWhen.get(0);
            System.out.println("map ####" + asAndWhenMap);
            asAnWhenCustomer = CommonUtil.convertObjToStr(asAndWhenMap.get("AS_CUSTOMER_COMES"));
        }
        if (asAnWhenCustomer != null && asAnWhenCustomer.length() > 0 && asAnWhenCustomer.equals("Y")) {
            principle = amount;
            principelBal -= amount;
            amount = 0;
        }
        if (amount >= totInt) {
            amount -= ebal;
            amount -= pInt;
            amount -= Int;
            paidPenal = 0;
            paidInt = 0;
            paidebal = 0;
            if (amount > 0) {
                if (amount >= payableprincipelBal) {
                    if (principelBal < 0) {
                        principle = amount;
                    } else {
                        principle = payableprincipelBal;
                        System.out.println("amountfirst###" + amount);
                        amount -= payableprincipelBal;
                        System.out.println("amountsecond###" + amount);
                        principelBal = principelBal - principle;
                        System.out.println("principelBal  second###" + principelBal);
                        if (debitLoanType != null && debitLoanType.equals("ACT_CLOSE") && amount > 0) {
                            if (amount >= principelBal) {
                                System.out.println("principelBalfirst###" + principelBal);
                                //                                 principelBal=principelBal-principle;
                                //                                principle=principelBal-principle;
                                System.out.println("principelBalfirst###" + amount);
                                amount -= principelBal;
                                System.out.println("principelBalsecond  ###" + amount);
                                principelBal = 0;
                            }
                        }
                        //                    }
                        //                    else    {
                        //                       principle=principelBal;
                        //                        amount -=principelBal;
                        //                        principelBal=0;
                    }

                } else {
                    principle = amount;
                    principelBal -= amount;
                    amount = 0;
                }

                if (amount > 0) {
                    if (npa_Penal_bal <= amount) {
                        paid_npa_penal_bal = npa_Penal_bal;
                        amount -= npa_Penal_bal;
                        npa_Penal_bal = 0;
                    } else {
                        paid_npa_penal_bal = amount;
                        npa_Penal_bal -= amount;
                        amount = 0;
                    }
                    if (amount > 0) {
                        if (npa_bal <= amount) {
                            paid_npa_bal = npa_bal;
                            amount -= npa_bal;
                            npa_bal = 0;
                        } else {
                            paid_npa_bal = amount;
                            npa_bal -= amount;
                            amount = 0;
                        }
                    }
                    //                            paid_npa_penal_bal=amount;
                    //                            amount-=npa_Penal_bal;

                    if (amount > 0) {
                        excessAmt = amount;
                    }
                }

            }

        } else if (amount >= pInt + ebal) {
            paidPenal = 0;
            paidebal = 0;
            amount -= pInt;

            amount -= ebal;
            if (amount != 0.0) {
                paidInt -= amount;
                Int = amount;
            }
        } else {
            //                    pInt-=amount;
            Int = 0;
            pInt = 0;
            ebal = paidebal;
            //            amount-=paidebal;
            if (amount > 0.0) {
                if (amount >= paidebal) {
                    paidebal = 0;
                    amount -= ebal;
                    if (amount > 0) {
                        paidPenal -= amount;
                    }
                } else {
                    paidebal -= amount;
                    ebal = amount;

                }
            }
        }
        //                    if(insertmap.get("LOANPARTICULARS") != null){
        //                        inputMap.put("EXPENSE",insertmap.get("LOANPARTICULARS"));
        //                        inputMap.put("EBAL",insertmap.get("LOANPARTICULARS"));
        //                    }else{
        inputMap.put("PRINCIPLE", new Double(principle));
        inputMap.put("PBAL", new Double(principelBal));
        inputMap.put("INTEREST", new Double(Int));
        inputMap.put("IBAL", new Double(paidInt));
        inputMap.put("PENAL", new Double(pInt));
        inputMap.put("PIBAL", new Double(paidPenal));
        inputMap.put("EBAL", new Double(paidebal));
        inputMap.put("EXPENSE", new Double(ebal));
        inputMap.put("NPA_INTEREST", new Double(paid_npa_bal));
        inputMap.put("NPA_INT_BAL", new Double(npa_bal));
        inputMap.put("NPA_PENAL", new Double(paid_npa_penal_bal));
        inputMap.put("NPA_PENAL_BAL", new Double(npa_Penal_bal));
        inputMap.put("EXCESS_AMT", new Double(excessAmt));
        if (CommonUtil.convertObjToStr(insertmap.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
            inputMap.put("TRN_CODE", new String("OLG"));
        } else {
            inputMap.put("TRN_CODE", new String("C*"));
        }
        inputMap.put("PRINCIPAL_AMOUNT", inputMap.get("PRINCIPLE"));
        inputMap.put("INTEREST_AMOUNT", inputMap.get("INTEREST"));
        inputMap.put("PENUL_INTEREST_AMOUNT", inputMap.get("PENAL"));
        inputMap.put("EFFECTIVE_DT", insertmap.get("INSTALL_DT"));
        inputMap.put("TRANS_SLNO", insertmap.get("TRANS_SLNO"));
        //npapenal interest debit
        if (paid_npa_penal_bal > 0) {
            List lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHeadATL", inputMap);
            if (lst != null && lst.size() > 0) {
                HashMap diMap = null;
                HashMap map = (HashMap) lst.get(0);
                map.putAll(inputMap);
                map.put("NPA_AMOUNT", new Double(paid_npa_penal_bal));
                map.put("NPA_DPI", "NPA_DPI");
                doNPATransaction(map);
                //GETLAST DEBIT TRANSACTION
                map.put("ACT_NUM", insertmap.get("ACCOUNTNO"));
                List lastlist = sqlMap.executeQueryForList("getIntDetailsATL", map);
                if (lastlist != null && lastlist.size() > 0) {
                    diMap = (HashMap) lastlist.get(0);
                    double npaInt = CommonUtil.convertObjToDouble(diMap.get("INTEREST")).doubleValue();
                    double npaPenalInt = CommonUtil.convertObjToDouble(diMap.get("PENAL")).doubleValue();
                    inputMap.put("INTEREST", new Double(Int + npaInt));
                    inputMap.put("PENAL", new Double(pInt + npaPenalInt));
                    inputMap.put("NPA_PENAL", new Double(0));
                }
                inputMap.put("TRANS_SLNO", new Long(CommonUtil.convertObjToInt(diMap.get("TRANS_SLNO")) + 1));
            }
        }
        //npa interest debit
        if (paid_npa_bal > 0) {
            System.out.println("paidnpa   bal####" + paid_npa_bal);
            List lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHeadATL", inputMap);
            if (lst != null && lst.size() > 0) {
                HashMap diMap = null;
                HashMap map = (HashMap) lst.get(0);
                map.putAll(inputMap);
                map.put("NPA_AMOUNT", new Double(paid_npa_bal));
                map.put("NPA_DI", "NPA_DI");
                doNPATransaction(map);
                //GETLAST DEBIT TRANSACTION
                map.put("ACT_NUM", insertmap.get("ACCOUNTNO"));
                List lastlist = sqlMap.executeQueryForList("getIntDetailsATL", map);
                if (lastlist != null && lastlist.size() > 0) {
                    diMap = (HashMap) lastlist.get(0);
                    double npaInt = CommonUtil.convertObjToDouble(diMap.get("INTEREST")).doubleValue();
                    double npaPenalInt = CommonUtil.convertObjToDouble(diMap.get("PENAL")).doubleValue();
                    inputMap.put("INTEREST", new Double(Int + npaInt));
                    inputMap.put("PENAL", new Double(pInt + npaPenalInt));
                    inputMap.put("NPA_INTEREST", new Double(0));
                }
                inputMap.put("TRANS_SLNO", new Long(CommonUtil.convertObjToInt(diMap.get("TRANS_SLNO")) + 1));
            }
        }
        inputMap.put("ARBITARY_CHARGE", new Double(0));
        inputMap.put("ARBITARY_CHARGE_BAL", new Double(0));
        inputMap.put("POSTAGE_CHARGE", new Double(0));
        inputMap.put("POSTAGE_CHARGE_BAL", new Double(0));
        inputMap.put("INSURANCE_CHARGE", new Double(0));
        inputMap.put("INSURANCE_CHARGE_BAL", new Double(0));
        inputMap.put("EXE_DEGREE", new Double(0));
        inputMap.put("EXE_DEGREE_BAL", new Double(0));
        inputMap.put("MISC_CHARGES", new Double(0));
        inputMap.put("MISC_CHARGES_BAL", new Double(0));
        inputMap.put("LEGAL_CHARGE", new Double(0));
        inputMap.put("LEGAL_CHARGE_BAL", new Double(0));
        //                    }

        //                }else{
        //            inputMap.put("PBAL",new Long(0));
        //            inputMap.put("PRINCIPLE",new Double(amount));
        //            inputMap.put("PROD_ID",insertmap.get(TransactionDAOConstants.PROD_ID));
        //                }
        if (inputMap.containsKey("DEBIT_LOAN_TYPE") && inputMap.get("DEBIT_LOAN_TYPE") != null
                && CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("CLEARING_BOUNCED")) {
            inputMap.put("AUTHORIZE_STATUS", "REJECTED");
        }
        System.out.println("inputmap#####update" + inputMap);
        sqlMap.executeUpdate("insertintoloanTransAuthDetailsATL", inputMap);
        System.out.println("getRuleMap######" + inputMap);
        return inputMap;

    }

    private HashMap getDisbursementDetails(HashMap validateMap) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.BRANCH_CODE));

        HashMap disbursementMap = new HashMap();

        disbursementMap.put(TransactionDAOConstants.ACCT_NO, validateMap.get(TransactionDAOConstants.ACCT_NO));
        if (validateMap.containsKey(REPAYMENT_SCHEDULE_NO)) {
            disbursementMap.put(REPAYMENT_SCHEDULE_NO, validateMap.get(REPAYMENT_SCHEDULE_NO));
        } else {
            disbursementMap.put(REPAYMENT_SCHEDULE_NO, "");
        }
        disbursementMap.put("DISBURSEMENT_ID", getLoanDisbursementKey(validateMap));
        double amount = Double.parseDouble(CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.AMT)));
        if (validateMap.get(TransactionDAOConstants.TO_STATUS).equals(CommonConstants.STATUS_CREATED) && amount < 0) {
            amount = -amount;
        }
        disbursementMap.put("DISBURSEMENT_AMT", new Double(amount));
        disbursementMap.put("DISBURSEMENT_DT", ServerUtil.getCurrentDate(_branchCode));
        disbursementMap.put("STATUS", validateMap.get(TransactionDAOConstants.TO_STATUS));
        disbursementMap.put("STATUS_DT", ServerUtil.getCurrentDate(_branchCode));
        disbursementMap.put("STATUS_BY", null);
        disbursementMap.put(TransactionDAOConstants.TRANS_ID, validateMap.get(TransactionDAOConstants.TRANS_ID));
        disbursementMap.put("ACCOUNTNO", validateMap.get("ACCOUNTNO"));
        disbursementMap.put("BRANCH_ID", _branchCode);
        return disbursementMap;
    }

    private HashMap getLoanPaidAmount(HashMap hash) throws Exception {
        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        hash.put("ACCT_NUM", hash.get("ACCOUNTNO"));
        System.out.println("loantransaction ##" + hash);
        HashMap result = new HashMap();
        hash.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
        hash.put("INITIATED_BRANCH", _branchCode);
        java.util.List lst = sqlMap.executeQueryForList("AgriIntCalculationDetail", hash);
        if (lst != null && lst.size() > 0) {
            result = (HashMap) lst.get(0);
            hash.putAll(result);
        }

        hash.put("START_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));
        hash.put("CURR_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));
        //        java.util.List lst =sqlMap.executeQueryForList("getIntAmtForGLUpdate",hash);getPrincipalforCalInt
        lst = sqlMap.executeQueryForList("getAgriPrincipalforCalInt", hash);
        if (lst != null && lst.size() > 0) {
            result = (HashMap) lst.get(0);
            result.put("INTEREST_AMOUNT", result.get("INTEREST"));
            result.put("PENUL_INTEREST_AMOUNT", result.get("PENAL"));
            hash.putAll(result);
        }
        lst = sqlMap.executeQueryForList("AgriTermLoan.getBehavesLike", hash);
        result = (HashMap) lst.get(0);
        hash.putAll(result);
        java.util.List lst1 = sqlMap.executeQueryForList("getProductATL", hash);
        if (lst1 != null && lst1.size() > 0) {
            result = (HashMap) lst1.get(0);
        }
        hash.putAll(result);

        //        java.util.List lst =sqlMap.executeQueryForList("getLastInstallmentPaid",hash);
        //        if(lst.size()>0)
        //        result=(HashMap)lst.get(0); for act level installment amount
        //        hash.putAll(result);
        //        java.util.List lst1=sqlMap.executeQueryForList("getProductTL",hash);
        //         if(lst1.size()>0)
        //        result=(HashMap)lst1.get(0);
        //        hash.putAll(result);
        return hash;

    }

    private void updateProcessingChargeinDisbursement(HashMap addShadow) {
        try {
            java.util.List lst = sqlMap.executeQueryForList("getDisbursementDetailsATL", addShadow);
            if (lst.size() > 0) {
                sqlMap.executeUpdate("updateLoanDisbursementChargesATL", addShadow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap creditLoanAdvancesDetail(HashMap shadow) throws Exception {
        System.out.println("creditLoanAdvancesDetails###" + shadow);
        HashMap addShadow = new HashMap();
        HashMap insertPenal = new HashMap();
        addShadow.putAll(shadow);
        HashMap allInstallmentMap = new HashMap();
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM", addShadow.get(TransactionDAOConstants.ACCT_NO));
        List paidAmt = sqlMap.executeQueryForList("getPaidPrincipleATL", loanInstall);
        double totPrinciple = 0;
        if (paidAmt != null && paidAmt.size() > 0) {
            allInstallmentMap = (HashMap) paidAmt.get(0);
        }
        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
        paidAmt = sqlMap.executeQueryForList("getIntDetailsATL", loanInstall);

        double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
        totPrinciple += totExcessAmt;//paid principal
        List lst = null;
        //        if(shadow.containsKey("DEBIT_LOAN_TYPE") && shadow.get("DEBIT_LOAN_TYPE").equals("ACT_CLOSE"))
        lst = sqlMap.executeQueryForList("getAllLoanInstallmentATL", loanInstall);
        //        else{
        //            loanInstall.put("CURR_DATE",ServerUtil.getCurrentDateProperFormat(_branchCode));
        //             lst=sqlMap.executeQueryForList("getPrincipalDueDetails", loanInstall);
        //        }
        Date inst_dt = null;
        int trans_slno = 1;
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    //                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = (Date) allInstallmentMap.get("INSTALLMENT_DT");
                    //                        }
                } else {
                    inst_dt = new Date();
                    //                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = (Date) allInstallmentMap.get("INSTALLMENT_DT");
                    //                    totPrinciple+=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                    double Principle = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                    Principle -= totPrinciple;
                    totPrinciple = Principle;
                    System.out.println("TOT_PRINCIPAL#####" + totPrinciple);
                    break;
                }

            }
            loanInstall.put("FROM_DATE", DateUtil.addDaysProperFormat(inst_dt, 1));
            loanInstall.put("TO_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));//shadow.get("TODAY_DT"));//ServerUtil.getCurrentDate(_branchCode));//cDate);
            System.out.println("getTotalamount#####" + loanInstall);
            List lst1 = null;
            if (inst_dt != null && DateUtil.dateDiff((Date) loanInstall.get("FROM_DATE"), (Date) loanInstall.get("TO_DATE")) >= 0) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDueATL", loanInstall);
            }
            System.out.println("listsize####" + lst1);
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            //FOR BANKDATABASE INTEREST AND PENAL

            List getIntDetails = sqlMap.executeQueryForList("getIntDetailsATL", loanInstall);
            HashMap hash = null;
            if (getIntDetails != null) {
                for (int i = 0; i < getIntDetails.size(); i++) {
                    hash = (HashMap) getIntDetails.get(i);
                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                    //                if(trn_mode.equals("C*") ||trn_mode.equals("OLG")){  TEST
                    //                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                    //                       double prevAmt=iBal;
                    insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                    //                     if(shadow.containsKey("DEBIT_LOAN_TYPE") && shadow.get("DEBIT_LOAN_TYPE").equals("ACT_CLOSE"))
                    insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue()));//insertPenal.get("CURR_MONTH_PRINCEPLE")); // hash.get("PBAL"));
                    //                     else
                    insertPenal.put("PAYABLE_PRINCIPLE_BAL", new Double(totPrinciple));
                    insertPenal.put("PBAL", hash.get("PBAL"));
                    insertPenal.put("PENAL_INT", hash.get("PIBAL"));;
                    insertPenal.put("EBAL", hash.get("EBAL"));
                    insertPenal.put("NPA_INT_BAL", hash.get("NPA_INT_BAL"));
                    insertPenal.put("NPA_PENAL_BAL", hash.get("NPA_PENAL_BAL"));
                    trans_slno = CommonUtil.convertObjToInt(hash.get("TRANS_SLNO"));
                    trans_slno++;
                    insertPenal.put("TRANS_SLNO", new Long(trans_slno));
                    //////                }
                    //////                else {
                    //////                    if(!trn_mode.equals("DP"))
                    //////                        //                            insertPenal.put("CURR_MONTH_INT",new Double (0.0));
                    //////                        //                        else
                    //////                        insertPenal.put("CURR_MONTH_INT",hash.get("IBAL"));
                    //////
                    //////                    insertPenal.put("PRINCIPLE_BAL",hash.get("PBAL"));
                    //////                }

                    //                    if(isDebitSelect)
                    //                    insertPenal.remove("CURR_MONTH_PRINCEPLE");
                    //                    isDebitSelect=false;
                    System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
                }
            } else {
                insertPenal.put("TRANS_SLNO", new Long(trans_slno));
            }
            //        getIntDetails=sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
            //        hash=(HashMap)getIntDetails.get(0);
            //        insertPenal.put("PENAL_INT",hash.get("PIBAL"));
        }
        addShadow.putAll(insertPenal);
        insertPenal = null;
        loanInstall = null;
        allInstallmentMap = null;

        return addShadow;

    }
}
