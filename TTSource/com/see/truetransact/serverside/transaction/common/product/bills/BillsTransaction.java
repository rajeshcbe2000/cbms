/*
 * BillsTransaction.java
 *
 * Created on March 4, 2009, 8:06 PM
 */
package com.see.truetransact.serverside.transaction.common.product.bills;

import com.see.truetransact.serverside.transaction.common.product.loan.LoanTransaction;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;


import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

// For the Business Rules...
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.businessrule.bills.ExpiryDateRule;
import com.see.truetransact.businessrule.termloan.LoanLimitCheckingRule;
import com.see.truetransact.businessrule.transaction.ChequeInstrumentRule;
import com.see.truetransact.businessrule.transaction.DraftInstrumentRule;
import com.see.truetransact.businessrule.transaction.DateCheckingRule;
import com.see.truetransact.businessrule.transaction.DeathMarkedRule;
import com.see.truetransact.businessrule.transaction.WithdrawlSlipRule;
import com.see.truetransact.businessrule.transaction.suspiciousconfig.SuspiciousConfigRule;
import com.see.truetransact.businessrule.transaction.AddressVerificationRule;
import com.see.truetransact.businessrule.transaction.ConfirmThanxRule;
import com.see.truetransact.businessrule.bills.LimitCheckingRule;
import com.see.truetransact.transferobject.common.log.LogTO;

//as andwhencustomer interestcalculation
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.commonutil.TTException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class BillsTransaction extends Transaction {

    private String _branchCode = null;

    /**
     * Creates a new instance of BillsTransaction
     */
    public BillsTransaction() throws ServiceLocatorException {
        super();
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceAdd(HashMap addShadow) throws Exception {
        System.out.println("performotherbalanceaddd###" + addShadow);
        if (addShadow.containsKey("BRANCH_CODE") && addShadow.get("BRANCH_CODE") != null) {
            _branchCode = CommonUtil.convertObjToStr(addShadow.get("BRANCH_CODE"));
        }
        java.util.List lst = null;
        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
        if (act_num != null && !act_num.equalsIgnoreCase("")) {
            if (!addShadow.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                addShadow.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }

            // Updates other Balances  (Clear balance and Total Balance)
            sqlMap.executeUpdate("updateOtherBalancesBills", addShadow);
//            if ( CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.AUTHORIZE_STATUS)).length()==0 ){
//                //            HashMap intValue= getAdvIntDetails(addShadow);
//                //            addShadow.putAll(intValue);
//                //
//                //            lst=sqlMap.executeQueryForList("getAdvPaidDetails",addShadow);
//                //            if( CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING))
//                //                if(lst == null || lst.size()==0 ){
//                //                    addShadow.put("TRN_CODE","CLG");
//                //                    addShadow.put("PRINCIPLE",intValue.get("PRINCIPAL_AMOUNT"));
//                //                    addShadow.put("PBAL",intValue.get("BAL_PRINCIPAL"));
//                //                    addShadow.put("INTEREST",intValue.get("INTEREST_AMOUNT"));
//                //                    addShadow.put("IBAL",intValue.get("INTEREST_AMOUNT"));
//                //                    addShadow.put("EXPENCE",new Double(0));
//                //                    addShadow.put("EBAL",new Double(0));
//                //                    System.out.println("performotherbalanceaddd  ###end"+addShadow);
//                //                    sqlMap.executeUpdate("insertAdvTransDetails",addShadow);
//                //                }
//            }
//            if(CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.AUTHORIZE_STATUS))!=null && CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.AUTHORIZE_STATUS)).equals(CommonConstants.STATUS_REALIZED)){
//                HashMap intValue= getAdvIntDetails(addShadow);
//                addShadow.putAll(intValue);
//                
//                lst=sqlMap.executeQueryForList("getAdvPaidDetails",addShadow);
//                if( CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING))
//                    if(lst == null || lst.size()==0 ){
//                        addShadow.put("TRN_CODE","CLG");
//                        addShadow.put("PRINCIPLE",intValue.get("PRINCIPAL_AMOUNT"));
//                        addShadow.put("PBAL",intValue.get("BAL_PRINCIPAL"));
//                        addShadow.put("INTEREST",intValue.get("INTEREST_AMOUNT"));
//                        addShadow.put("IBAL",intValue.get("INTEREST_AMOUNT"));
//                        addShadow.put("EXPENCE",new Double(0));
//                        addShadow.put("EBAL",new Double(0));
//                        System.out.println("performotherbalanceaddd  ###end"+addShadow);
//                        //                        sqlMap.executeUpdate("insertAdvTransDetails",addShadow);
//                    }
//                //                sqlMap.executeUpdate("updateAdvTransDetails",addShadow);
//                HashMap interestDetailsMap=getPaidDetails(addShadow);
//                
//            }
            //            else

            lst = null;
        }
        addShadow = null;
    }

    /**
     * Updating Clear & Total Balance
     */
    public void performOtherBalanceMinus(HashMap addShadow) throws Exception {
        printLog("performOtherBalanceMinus:before:" + addShadow);

        String act_num = CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.ACCT_NO));
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
            sqlMap.executeUpdate("updateOtherBalancesBills", addShadow);


        }
        addShadow = null;
    }

    public HashMap getAdvIntDetails(HashMap addShadow) throws Exception {
        addShadow.put("ACT_NUM", addShadow.get(TransactionDAOConstants.ACCT_NO));
        java.util.List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", addShadow);
        HashMap hash = null;
        HashMap insertPenal = new HashMap();
        for (int i = 0; i < getIntDetails.size(); i++) {
            hash = (HashMap) getIntDetails.get(i);
            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_MODE"));
            if (trn_mode.equals("C*")) {
                //                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                //                       double prevAmt=iBal;
                insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                break;
            } else {
                insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
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
            addShadow.put("PRINCIPAL_AMOUNT", new Double(paidPrincipal));  //insertPenal
            addShadow.put("INTEREST_AMOUNT", new Double(interest));
            addShadow.put("BAL_PRINCIPAL", new Double(Totprincipal));
            System.out.println("interestDetails####" + insertPenal);

        }
        return insertPenal;
    }

    /**
     * update interstGL and loan account head GL also
     *
     *
     */
    public void updateGL(String accHead, Double amount, LogTO objLogTO, HashMap ruleMap) {
        System.out.println("rulemap####" + ruleMap);
        try {
            if (ruleMap != null) {
                if (CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE)).equals(CommonConstants.DEBIT)) {
                    super.updateGL(accHead, amount, objLogTO, ruleMap);
                } else if (CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_TYPE)).equals(CommonConstants.CREDIT)) {
                    if (ruleMap.containsKey(TransactionDAOConstants.PROD_TYPE) && ruleMap.get(TransactionDAOConstants.PROD_TYPE).equals(TransactionFactory.GL)) {
                        super.updateGL(accHead, amount, objLogTO, ruleMap);
                    } else {
                        HashMap interestDetailsMap = new HashMap();
                        if (CommonUtil.convertObjToStr(ruleMap.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
                            interestDetailsMap.put("PRINCIPAL_AMOUNT", ruleMap.get(TransactionDAOConstants.AMT));

                        } else {
                            interestDetailsMap = getPaidDetails(ruleMap);
                        }
                        interestDetailsMap.put("PRINCIPAL_AMOUNT", ruleMap.get(TransactionDAOConstants.AMT));
                        //                       //NPA
                        //                     double amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("NPA_INTEREST")).doubleValue();
                        //                     if(amt>0){
                        //                         interestDetailsMap.put("DEBIT_ACHD",accHead);
                        //                     doNPATransaction(interestDetailsMap);
                        //                     }
                        //                       amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("NPA_PENAL")).doubleValue();
                        //                     if(amt>0){
                        //                         interestDetailsMap.put("DEBIT_ACHD",accHead);
                        //                     doNPATransaction(interestDetailsMap);
                        //                     }
                        //                    //END NPA
                        double amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("PRINCIPAL_AMOUNT")).doubleValue();

                        if (amt != 0) {
                            // Main Account Head
                            super.updateGL(accHead, new Double(amt), objLogTO, ruleMap);
                            System.out.println("ruleMapacchead   #### " + ruleMap);
                        }

                        amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("INTEREST_AMOUNT")).doubleValue();
                        if (amt != 0) {
                            // Debit Interest Account Head
                            //                           super.updateGL(CommonUtil.convertObjToStr(interestDetailsMap.get("AC_DEBIT_INT")), new Double(amt), objLogTO, ruleMap);
                            interestDetailsMap.put("PAID_INT", new Double(amt));
                            interestDetailsMap.put("CREDIT_ACHD", interestDetailsMap.get("AC_CREDIT_INT"));
                            if (interestDetailsMap.containsKey("AS_CUSTOMER_COMES") && interestDetailsMap.get("AS_CUSTOMER_COMES") != null && interestDetailsMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                            } else {
                                doIntTransaction(interestDetailsMap);
                            }
                            interestDetailsMap.remove("PAID_INT");
                            interestDetailsMap.remove("CREDIT_ACHD");
                        }

                        amt = CommonUtil.convertObjToDouble(interestDetailsMap.get("PENUL_INTEREST_AMOUNT")).doubleValue();
                        if (amt != 0) {
                            // Penalty Interest Account Head
                            //                           super.updateGL(CommonUtil.convertObjToStr(interestDetailsMap.get("PENAL_INT")), new Double(amt), objLogTO, ruleMap);
                            interestDetailsMap.put("PAID_INT", new Double(amt));
                            interestDetailsMap.put("CREDIT_ACHD", interestDetailsMap.get("PENAL_INT"));
                            if (interestDetailsMap.containsKey("AS_CUSTOMER_COMES") && interestDetailsMap.get("AS_CUSTOMER_COMES") != null && interestDetailsMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                            } else {
                                doIntTransaction(interestDetailsMap);
                            }
                            interestDetailsMap.remove("PENAL_INT");
                        }
                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void doNPATransaction(HashMap hash) {
        System.out.println("doIntTransaction#####" + hash);
        try {
            HashMap txMap = new HashMap();
            double paidIntAmt = CommonUtil.convertObjToDouble(hash.get("NPA_INTEREST")).doubleValue();
            if (paidIntAmt == 0) {
                paidIntAmt = CommonUtil.convertObjToDouble(hash.get("NPA_PENAL")).doubleValue();
            }
            _branchCode = CommonUtil.convertObjToStr(hash.get("BRANCH_CODE"));
            ArrayList transferList = new ArrayList();
            if (CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")).length() > 0 && CommonUtil.convertObjToStr(hash.get("INT_PAYABLE_ACHD")).length() > 0) {
                txMap.put(TransferTrans.DR_AC_HD, hash.get("ACCT_HEAD"));
                txMap.put(TransferTrans.DR_ACT_NUM, hash.get("ACCOUNTNO"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
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
                if (CommonUtil.convertObjToDouble(hash.get("NPA_INTEREST")).doubleValue() > 0) {
                    if (hash.containsKey("NPA_DI")) {
                        trans.setLoanDebitInt("NPA_DI");
                    } else {
                        trans.setLoanDebitInt("NPA_DPI");
                    }
                }
                transferList.add(trans.getDebitTransferTO(txMap, paidIntAmt));
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                transferList.add(trans.getCreditTransferTO(txMap, paidIntAmt));
                trans.doDebitCredit(transferList, _branchCode, true);
            } else {
                throw new TTException("INT PAYABLE OR INT RECIVED ACCOUNT NOT SET ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap getPaidDetails(HashMap hash) throws Exception {
        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        System.out.println("loantransaction ##" + hash);
        HashMap result = new HashMap();
        hash.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
        hash.put("INITIATED_BRANCH", _branchCode);
        java.util.List lst = sqlMap.executeQueryForList("IntCalculationDetail", hash);
        if (lst != null && lst.size() > 0) {
            result = (HashMap) lst.get(0);
            hash.putAll(result);
        }
        lst = sqlMap.executeQueryForList("getIntDetailsAD", hash);//getIntAmtForGLUpdateAD
        if (lst != null && lst.size() > 0) {
            result = (HashMap) lst.get(0);
            result.put("INTEREST_AMOUNT", result.get("INTEREST"));
            result.put("PENUL_INTEREST_AMOUNT", result.get("PENAL"));
            hash.putAll(result);
        }
        java.util.List lst1 = sqlMap.executeQueryForList("getProductTL", hash);
        if (lst1.size() > 0) {
            result = (HashMap) lst1.get(0);
        }
        hash.putAll(result);
        return (hash);

    }
    /*
     *
     * update interest recivable to received account
     */

    void doIntTransaction(HashMap hash) throws Exception {
        System.out.println("doIntTransaction#####" + hash);
        try {
            HashMap txMap = new HashMap();
            double paidIntAmt = CommonUtil.convertObjToDouble(hash.get("PAID_INT")).doubleValue();
            String _branchCode = CommonUtil.convertObjToStr(hash.get("BRANCH_CODE"));
            ArrayList transferList = new ArrayList();
            //            txMap.put(TransferTrans.DR_ACT_NUM,hash.get("ACCOUNTNO"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, hash.get("BRANCH_CODE"));
            txMap.put(TransferTrans.DR_AC_HD, hash.get("INT_PAYABLE_ACHD"));

            TransferTrans trans = new TransferTrans();
            txMap.put(TransferTrans.CR_BRANCH, hash.get("BRANCH_CODE"));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_AC_HD, hash.get("AC_DEBIT_INT"));
            txMap.put(TransferTrans.CURRENCY, "INR");
            trans.setLinkBatchId(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            transferList.add(trans.getDebitTransferTO(txMap, paidIntAmt));
            transferList.add(trans.getCreditTransferTO(txMap, paidIntAmt));
            trans.doDebitCredit(transferList, _branchCode, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowAdd(HashMap addShadow) throws Exception {
        printLog("performShadowAdd:before:" + addShadow);
        boolean debit_int = false;
        String debitLonaType = "";
        HashMap getIntDetails = new HashMap();
        if (addShadow.containsKey("DEBIT_LOAN_TYPE")) {
            debitLonaType = (String) addShadow.get("DEBIT_LOAN_TYPE");
        }
        if (addShadow.containsKey("DEBIT_INT")) {
            Boolean debitInt = (Boolean) addShadow.get("DEBIT_INT");
            debit_int = debitInt.booleanValue();
        }
        String act_num = (String) addShadow.get(TransactionDAOConstants.ACCT_NO);
        getIntDetails.put("ACT_NUM", act_num);
        java.util.List lst = sqlMap.executeQueryForList("getIntDetailsAD", getIntDetails);
        if (lst != null && lst.size() > 0) {
            getIntDetails = (HashMap) lst.get(0);
        }
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if ((CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                addShadow.put(TransactionDAOConstants.AMT,
                        new Double(-1 * Double.parseDouble(addShadow.get(TransactionDAOConstants.AMT).toString())));
                sqlMap.executeUpdate("updateShadowDebitBills", addShadow);
                if (debit_int || debitLonaType.equals("DI") && debitLonaType.length() > 0) {
                    addShadow.put("TRN_CODE", "DI");
                    addShadow.put("INTEREST", addShadow.get(TransactionDAOConstants.AMT));

                    if (getIntDetails != null && getIntDetails.containsKey("PBAL")) {
                        addShadow.put("PRINCIPLE", new Double(0 + CommonUtil.convertObjToDouble(getIntDetails.get("PBAL")).doubleValue()));
                        addShadow.put("IBAL", new Double(CommonUtil.convertObjToDouble(addShadow.get(TransactionDAOConstants.AMT)).doubleValue() + CommonUtil.convertObjToDouble(getIntDetails.get("IBAL")).doubleValue()));
                    } else {
                        addShadow.put("PRINCIPLE", new Double(0));
                        addShadow.put("IBAL", addShadow.get(TransactionDAOConstants.AMT));
                    }
                    addShadow.put("PBAL", addShadow.get("PRINCIPLE"));
                    addShadow.put("EXPENCE", new Double(0));
                    addShadow.put("EBAL", new Double(0));
                } //                else if(CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)){
                //                    addShadow.put("TRN_CODE","CLG");
                //                    addShadow.put("PRINCIPLE",addShadow.get(TransactionDAOConstants.AMT));
                //                    addShadow.put("PBAL",new Double(0));
                //                    addShadow.put("INTEREST",new Double(0));
                //                    addShadow.put("IBAL",new Double(0));
                //                    addShadow.put("EXPENCE",new Double(0));
                //                    addShadow.put("EBAL",new Double(0));
                //                }
                else if (debitLonaType.equals("DP") && debitLonaType.length() > 0) {
                    addShadow.put("TRN_CODE", "DP");
                    addShadow.put("INTEREST", new Double(0));
                    addShadow.put("IBAL", new Double(0));
                    addShadow.put("PRINCIPLE", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("PBAL", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("EXPENCE", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("EBAL", addShadow.get(TransactionDAOConstants.AMT));
                } else if (debitLonaType.equals("DPI") && debitLonaType.length() > 0) {
                    addShadow.put("TRN_CODE", "DPI");
                    addShadow.put("INTEREST", new Double(0));
                    addShadow.put("IBAL", new Double(0));
                    addShadow.put("PRINCIPLE", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("PBAL", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("EXPENCE", new Double(0));
                    addShadow.put("EBAL", new Double(0));
                } else if (debitLonaType.equals("OTHERCHARGES") && debitLonaType.length() > 0) {
                    addShadow.put("TRN_CODE", "OTHERCHARGES");
                    addShadow.put("INTEREST", new Double(0));
                    addShadow.put("IBAL", new Double(0));
                    addShadow.put("PRINCIPLE", new Double(0));
                    addShadow.put("PBAL", new Double(0));
                    addShadow.put("EXPENCE", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("EBAL", addShadow.get(TransactionDAOConstants.AMT));
                }

            } else {
                sqlMap.executeUpdate("updateShadowCreditBills", addShadow);
                if (CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
                    addShadow.put("TRN_CODE", "CLG");
                    addShadow.put("PRINCIPLE", addShadow.get(TransactionDAOConstants.AMT));
                    addShadow.put("PBAL", new Double(0));
                    addShadow.put("INTEREST", new Double(0));
                    addShadow.put("IBAL", new Double(0));
                    addShadow.put("EXPENCE", new Double(0));
                    addShadow.put("EBAL", new Double(0));
                }
            }
            //            if(! (CommonUtil.convertObjToStr(addShadow.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)))
            ////                sqlMap.executeUpdate("insertAdvTransDetails",addShadow);
        }
        addShadow = null;
    }

    /**
     * Updating Shadow debit or shadow credit based on the parameter
     */
    public void performShadowMinus(HashMap minusMap) throws Exception {
        printLog("performShadowMinus:before:" + minusMap);

        String act_num = (String) minusMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            minusMap.put(TransactionDAOConstants.AMT,
                    new Double(-1 * Double.parseDouble(minusMap.get(TransactionDAOConstants.AMT).toString())));
            if ((CommonUtil.convertObjToStr(minusMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                sqlMap.executeUpdate("updateShadowDebitBills", minusMap);
            } else {
                sqlMap.executeUpdate("updateShadowCreditBills", minusMap);
            }
            //            sqlMap.executeUpdate("updateAdvTransDetails", minusMap);
            printLog("performShadowMinus:after:" + minusMap);
        }
        minusMap = null;
    }

    /**
     * Updating Available Balance
     */
    public void updateAvailableBalance(HashMap updateMap) throws Exception {
        printLog("updateAvailableBalance:" + updateMap);

        String act_num = (String) updateMap.get(TransactionDAOConstants.ACCT_NO);
        if (act_num != null && !act_num.equalsIgnoreCase("")) {

            if (!updateMap.containsKey(TransactionDAOConstants.UNCLEAR_AMT)) {
                updateMap.put(TransactionDAOConstants.UNCLEAR_AMT, new Double(0));
            }
            if ((CommonUtil.convertObjToStr(updateMap.get(TransactionDAOConstants.TRANS_TYPE))).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                if (updateMap.containsKey("DEBIT_LOAN_TYPE") && updateMap.get("DEBIT_LOAN_TYPE") != null && updateMap.get("DEBIT_LOAN_TYPE").equals("DP")) {
                    sqlMap.executeUpdate("updateAvailBalanceBills", updateMap);
                } else {
                    updateMap.put("LAST_INT_CALC_DT", updateMap.get("TODAY_DT"));
                    System.out.println("updateavailbal##" + updateMap);
                    sqlMap.executeUpdate("updateAvailBalanceBills", updateMap);
                }

            } else {
                sqlMap.executeUpdate("updateAvailBalanceBills", updateMap);
            }
        }
//        if (updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS)!=null)
//            if (//updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REJECTED) ||
//            updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_AUTHORIZED)||
//            updateMap.get(TransactionDAOConstants.AUTHORIZE_STATUS).equals(CommonConstants.STATUS_REALIZED)){
//                if(! updateMap.containsKey("CLEARING_BOUNCED"))
//                    authorizeDisbursementDetails(updateMap);
//                System.out.println("updatemap####"+updateMap);
//                updateLoanBalancePrincipal(updateMap);
//            }
        updateMap = null;
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
                sqlMap.executeUpdate("updateBillsBalancePrincipalDetails", balancePrincipalMap);

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
                sqlMap.executeUpdate("updateBillsBalancePrincipalDetails", balancePrincipalMap);
                if (amount < 0) {
                    amount = (-1) * modifiedAmount;
                    balancePrincipalMap.put(TransactionDAOConstants.AMT, new Double(amount));
                }
            }
        }
    }

    public void validateRules(HashMap validateMap, boolean isException) throws Exception {
        String act_num = CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.ACCT_NO));

        validateMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.ADVANCES);

        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        ArrayList list = new ArrayList();

        if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
            context.addRule(new DeathMarkedRule());
            context.addRule(new SuspiciousConfigRule());
        }

        if (CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            // Validation only for Debit
            if (act_num != null && !act_num.equalsIgnoreCase("") && !isException) {
                context.addRule(new LimitCheckingRule());
                context.addRule(new AddressVerificationRule());
                context.addRule(new ConfirmThanxRule());
                context.addRule(new ExpiryDateRule());
            }

            //To apply business rule
            if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DD)) {
                context.addRule(new DraftInstrumentRule());
                context.addRule(new DateCheckingRule());
            } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CHEQUE)) {
                context.addRule(new ChequeInstrumentRule());
                context.addRule(new DateCheckingRule());
            } else if (((String) validateMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)).equalsIgnoreCase(TransactionDAOConstants.WITHDRAWLSLIP)) {
                context.addRule(new WithdrawlSlipRule());
            }
        }

        list = (ArrayList) engine.validateAll(context, validateMap);

        //        else if(CommonUtil.convertObjToStr(validateMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
        //            // Validation only for Credit
        //            if(act_num!=null && !act_num.equalsIgnoreCase("") && !isException){
        //                context.addRule(new LoanLimitCheckingRule());
        //                context.addRule(new LoanRepaymentRule());
        //            }
        //            // Validate the rules here
        //            list = (ArrayList) engine.validateAll(context,validateMap);
        //        }

        context = null;
        engine = null;

        if (list != null) {
            System.out.println("Exception List in AdvancesTransaction: " + list);
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS, TransactionDAOConstants.TRANS_RULE_MAP);
            System.out.println("Exception List : " + list);

            throw new TTException(exception);
        }
    }

    private void insertAuthorizeLoanDebit(HashMap authorizeMap) throws Exception {
        System.out.println("insertauthorizeloandebit####" + authorizeMap);
        if(authorizeMap.containsKey("TRANSMODE"))
         {
           authorizeMap.put("TRANS_MODE", authorizeMap.get("TRANSMODE"));
         }
        if (authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") != null && (!authorizeMap.get("DEBIT_LOAN_TYPE").equals("CLEARING_BOUNCED"))) {
            authorizeMap.put("ACT_NUM", authorizeMap.get("ACCOUNTNO"));
            List maxLoanTrans = sqlMap.executeQueryForList("getIntDetailsAD", authorizeMap);
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
                    maxLoanTransmap.remove("PRINCIPAL");
                } else {
                    authorizeMap.put("PBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue()));
                    authorizeMap.put("PRINCIPAL", new Double(0));
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

            authorizeMap.put("TRANS_SLNO", new Long(trans_slno));
            if (maxLoanTransmap != null) {
                authorizeMap.put("EXCESS_AMT", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("EXCESS_AMT")).doubleValue()));
            }
            System.out.println("authorizeMap####befor insert" + authorizeMap);
            //          if(authorizeMap.containsKey("DEBIT_LOAN_TYPE") && authorizeMap.get("DEBIT_LOAN_TYPE") !=null
            //        && CommonUtil.convertObjToStr(authorizeMap.get("DEBIT_LOAN_TYPE")).equals("CLEARING_BOUNCED")){
            //            authorizeMap.put("AUTHORIZE_STATUS", "REJECTED");
            //        }

            authorizeMap.put("TRANS_SLNO", new Long(trans_slno));
            sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", authorizeMap);
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
        double pInt = CommonUtil.convertObjToDouble(insertmap.get("PENAL_INT")).doubleValue();
        double Int = CommonUtil.convertObjToDouble(insertmap.get("IBAL")).doubleValue();
        double principelBal = CommonUtil.convertObjToDouble(insertmap.get("PRINCIPLE_BAL")).doubleValue();
        double ebal = CommonUtil.convertObjToDouble(insertmap.get("EBAL")).doubleValue();
        double npa_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_INT_BAL")).doubleValue();
        double npa_Penal_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_PENAL_BAL")).doubleValue();
        double npatotal = npa_Penal_bal + npa_bal;
        double totInt = pInt + Int + ebal;
        double principle = 0;
        double paidPenal = pInt;
        double paidInt = Int;
        double paidebal = ebal;
        double paid_npa_bal = 0;//npa_bal;
        double paid_npa_penal_bal = 0;//npa_Penal_bal;
        if (amount >= totInt) {
            amount -= ebal;
            amount -= pInt;
            amount -= Int;
            paidPenal = 0;
            paidInt = 0;
            paidebal = 0;
            if (amount > 0.0) {
                if (amount >= principelBal) {
                    if (npatotal > 0) {
                        principle = principelBal;
                    } else {
                        principle = amount;
                    }
                    amount -= principelBal;
                    principelBal = 0;
                } else {
                    principle = amount;
                    principelBal -= amount;
                    amount = 0;
                }
                System.out.println("principelBal" + principelBal + "principle" + principle + "amount" + amount);
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
                        principelBal = amount * -1;
                    }
                }

            }
        } else if (amount >= pInt + ebal) {
            paidPenal = 0;
            paidebal = 0;
            amount -= pInt;

            amount -= ebal;
            if (amount > 0.0) {
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


                    amount -= ebal;
                    paidebal = 0;
                    System.out.println("ebal" + ebal + "paidebal####" + paidebal + "amount" + amount);
                    if (amount > 0) {

                        paidPenal -= amount;
                        pInt = amount;
                        System.out.println("eba123l" + ebal + "paidebal####" + paidebal + "amount" + amount + "paidpenal" + paidPenal + "pInt###" + pInt);
                    }
                } else {
                    paidebal -= amount;
                    ebal = amount;

                }
            }
        }
        System.out.println("last all balance principle" + principle + "principelBal" + principelBal + "Int" + Int + "paidInt" + paidInt);
        //                    if(insertmap.get("LOANPARTICULARS") != null){
        //                        inputMap.put("EXPENSE",insertmap.get("LOANPARTICULARS"));
        //                        inputMap.put("EBAL",insertmap.get("LOANPARTICULARS"));
        //                    }else{
        inputMap.put("PRINCIPAL", new Double(principle));
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
        inputMap.put("TRANS_MODE", CommonUtil.convertObjToStr(insertmap.get(TransactionDAOConstants.TRANS_MODE)));
        //        inputMap.put(TransactionDAOConstants.PARTICULARS,insertmap.get(TransactionDAOConstants.PARTICULARS));
        if (CommonUtil.convertObjToStr(insertmap.get(TransactionDAOConstants.TRANS_MODE)).equals(TransactionDAOConstants.CLEARING)) {
            inputMap.put("TRN_CODE", new String("OLG"));
        } else {
            inputMap.put("TRN_CODE", new String("C*"));
        }
        inputMap.put("PRINCIPAL_AMOUNT", inputMap.get("PRINCIPLE"));
        inputMap.put("INTEREST_AMOUNT", inputMap.get("INTEREST"));
        inputMap.put("PENUL_INTEREST_AMOUNT", inputMap.get("PENAL"));
        inputMap.put("EFFECTIVE_DT", insertmap.get("INSTALL_DT"));
        inputMap.put("TRANS_SLNO", new Long(CommonUtil.convertObjToStr(insertmap.get("TRANS_SLNO"))));
        //npapenal interest debit
        if (paid_npa_penal_bal > 0) {
            List lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", inputMap);
            if (lst != null && lst.size() > 0) {
                HashMap diMap = null;
                HashMap map = (HashMap) lst.get(0);
                map.putAll(inputMap);
                map.put("NPA_AMOUNT", new Double(paid_npa_penal_bal));
                map.put("NPA_DPI", "NPA_DPI");
                doNPATransaction(map);
                //GETLAST DEBIT TRANSACTION
                map.put("ACT_NUM", insertmap.get("ACCOUNTNO"));
                List lastlist = sqlMap.executeQueryForList("getIntDetailsAD", map);
                if (lastlist != null && lastlist.size() > 0) {
                    diMap = (HashMap) lastlist.get(0);
                    double npaInt = CommonUtil.convertObjToDouble(diMap.get("INTEREST")).doubleValue();
                    double npaPenalInt = CommonUtil.convertObjToDouble(diMap.get("PENAL")).doubleValue();
                    inputMap.put("INTEREST", new Double(Int + npaInt));
                    inputMap.put("PENAL", new Double(pInt + npaPenalInt));
                    inputMap.put("NPA_PENAL", new Double(paid_npa_penal_bal));
                }
                inputMap.put("TRANS_SLNO", new Long(CommonUtil.convertObjToInt(diMap.get("TRANS_SLNO")) + 1));
            }
        }
        //npa interest debit
        if (paid_npa_bal > 0) {
            System.out.println("paidnpa   bal####" + paid_npa_bal);
            List lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", inputMap);
            if (lst != null && lst.size() > 0) {
                HashMap diMap = null;
                HashMap map = (HashMap) lst.get(0);
                map.putAll(inputMap);
                map.put("NPA_AMOUNT", new Double(paid_npa_bal));
                map.put("NPA_DI", "NPA_DI");
                doNPATransaction(map);
                //GETLAST DEBIT TRANSACTION
                map.put("ACT_NUM", insertmap.get("ACCOUNTNO"));
                List lastlist = sqlMap.executeQueryForList("getIntDetailsAD", map);
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
        //                    }

        //                }else{
        //            inputMap.put("PBAL",new Long(0));
        //            inputMap.put("PRINCIPLE",new Double(amount));
        //            inputMap.put("PROD_ID",insertmap.get(TransactionDAOConstants.PROD_ID));
        //                }
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
        System.out.println("inputmap#####update" + inputMap);


        sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", inputMap);
        System.out.println("getRuleMap######" + inputMap);
        return inputMap;

    }

    private void authorizeDisbursementDetails(HashMap authorizeMap) throws Exception {

        if (authorizeMap.get(TransactionDAOConstants.TRANS_TYPE).equals("CREDIT")) {
            HashMap map = creditLoanAdvancesDetail(authorizeMap);
            map.put(TransactionDAOConstants.TRANS_TYPE, authorizeMap.get(TransactionDAOConstants.TRANS_TYPE));
            map.put(TransactionDAOConstants.PARTICULARS, authorizeMap.get(TransactionDAOConstants.PARTICULARS));
            map = insertLoanCredit(map);
            authorizeMap.putAll(map);
        } else {
            insertAuthorizeLoanDebit(authorizeMap);
        }
        //        sqlMap.executeUpdate("authorizeLoansDisbursementCumLoanDetails", authorizeMap);//commented for testing
//        asAnWhenCustomer(authorizeMap);
    }

    //dont delete the methode if as an when customer comes last int caldate updation need tally  (principal  + interest)
    //means enable this methode and disable transfer dao cash dao
    private void asAnWhenCustomer(HashMap authorizeMap) throws Exception {
        System.out.println("authorizeMap#####" + authorizeMap);
        HashMap getDateMap = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        getDateMap.put("ACT_NUM", authorizeMap.get(TransactionDAOConstants.ACCT_NO));
        getDateMap.put("PROD_ID", authorizeMap.get(TransactionDAOConstants.PROD_ID));
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
            getDateMap = interestCalTask.interestCalcTermLoanAD(getDateMap); // we need same used for TL also
            double penalInt = 0;
            if (getDateMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                penalInt = CommonUtil.convertObjToDouble(getDateMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
            }
            double interest = CommonUtil.convertObjToDouble(getDateMap.get("INTEREST")).doubleValue();
            //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
            //                getDateMap=(HashMap)lst.get(0);
            System.out.println("OD  #####!" + getDateMap);
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

    private HashMap creditLoanAdvancesDetail(HashMap shadow) throws Exception {
        System.out.println("creditLoanAdvancesDetails###" + shadow);
        HashMap addShadow = new HashMap();
        HashMap insertPenal = null;
        addShadow.putAll(shadow);
        HashMap allInstallmentMap = new HashMap();
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM", addShadow.get(TransactionDAOConstants.ACCT_NO));
        loanInstall.put("AUTHORIZE_STATUS", shadow.get(TransactionDAOConstants.AUTHORIZE_STATUS));
        int trans_slno = 1;
        //        List paidAmt=sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
        //        double totPrinciple=0;
        //        if(paidAmt !=null && paidAmt.size()>0)
        //            allInstallmentMap=(HashMap)paidAmt.get(0);
        //        totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
        //        List lst=sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
        //        Date inst_dt=null;
        //        if(lst !=null && lst.size()>0){
        //            for(int i=0;i<lst.size();i++) {
        //                allInstallmentMap=(HashMap)lst.get(i);
        //                double instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
        //                if(instalAmt<=totPrinciple) {
        //                    totPrinciple-=instalAmt;
        //                    //                        if(lst.size()==1){
        //                    inst_dt=new Date();
        //                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
        //                    inst_dt=DateUtil.getDateMMDDYYYY(in_date);
        //                    //                        }
        //                }
        //                else{
        //                    inst_dt=new Date();
        //                    String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
        //                    inst_dt=DateUtil.getDateMMDDYYYY(in_date);
        //                    totPrinciple+=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
        //                    break;
        //                }
        //
        //            }
        //            loanInstall.put("FROM_DATE",inst_dt);
        //            loanInstall.put("TO_DATE",shadow.get("TODAY_DT"));//ServerUtil.getCurrentDate(_branchCode));//cDate);
        //            System.out.println("getTotalamount#####"+loanInstall);
        //            List lst1=null;
        //            if(inst_dt !=null)
        //                lst1=sqlMap.executeQueryForList("getTotalAmountOverDue",loanInstall);
        //            System.out.println("listsize####"+lst1);
        //            double principle=0;
        //            if(lst1 !=null && lst1.size()>0){
        //                HashMap map=(HashMap)lst1.get(0);
        //                principle=CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMT")).doubleValue();
        //            }
        //            totPrinciple+=principle;
        insertPenal = new HashMap();
        //            insertPenal.put("CURR_MONTH_PRINCEPLE",new Double(totPrinciple));
        //            insertPenal.put("INSTALL_DT",inst_dt);
        //FOR BANKDATABASE INTEREST AND PENAL

        List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
        HashMap hash = new HashMap();
        if (getIntDetails != null) {
            for (int i = 0; i < getIntDetails.size(); i++) {
                hash = (HashMap) getIntDetails.get(i);
                System.out.println("last Trans balance details###" + hash);
                String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                //                if(trn_mode.equals("C*") ||trn_mode.equals("OLG")){  TEST
                //                        double iBal=CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                //                       double prevAmt=iBal;
                insertPenal.put("IBAL", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));//insertPenal.get("CURR_MONTH_PRINCEPLE")); // hash.get("PBAL"));
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));;
                insertPenal.put("EBAL", hash.get("EBAL"));
                trans_slno = CommonUtil.convertObjToInt(hash.get("TRANS_SLNO"));
                insertPenal.put("NPA_INT_BAL", hash.get("NPA_INT_BAL"));
                insertPenal.put("NPA_PENAL_BAL", hash.get("NPA_PENAL_BAL"));
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
        //        }
        addShadow.putAll(insertPenal);
        insertPenal = null;
        loanInstall = null;
        allInstallmentMap = null;

        return addShadow;

    }
}