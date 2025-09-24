/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RollBackDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.transaction.rollback;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.sql.*;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.GregorianCalendar;

//import org.apache.log4j.Logger;

// For Cash Transaction RollBack
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.transferobject.termloan.TermLoanPenalWaiveOffTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import org.apache.log4j.helpers.DateTimeDateFormat;

/**
 *
 * @author Rajesh
 */
public class RollBackDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private Date currDt = null;
    private final String PROD_ID = "PROD_ID";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private Transaction transModuleBased;
    private String rollBackID;
    private boolean commitFlag = true;
    private HashMap rollBackMap = new HashMap();

    private HashMap getData(HashMap map) throws Exception {
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        HashMap where = null;
        String strWhere = "";
        if (map.containsKey(CommonConstants.MAP_WHERE)) {
            if (map.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                where = (HashMap) map.get(CommonConstants.MAP_WHERE);
            } else {
                strWhere = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
            }
        }

        if (mapName.trim().length() == 0) {
            throw new TTException("Map Name is Null");
        }

        List list = null;
        System.out.println("!!!!! mapName : " + mapName + " / where : " + where + " / strWhere : " + strWhere);
        try {
            if (strWhere.length() > 0) {
                list = (List) sqlMap.executeQueryForList(mapName, strWhere);
            } else {
                list = (List) sqlMap.executeQueryForList(mapName, where);
            }
        } catch (Exception e) {
            // If you get an error at this point, it matters little what it was. It is going to be
            // unrecoverable and we will want the app to blow up good so we are aware of the
            // problem. You should always log such errors and re-throw them in such a way that
            // you can be made immediately aware of the problem.
            e.printStackTrace();

            throw new TTException("Error initializing SqlConfig class. Cause: " + e);
        }
        where = new HashMap();
        where.put(CommonConstants.DATA, list);
        return where;
    }

    /**
     * Creates a new instance of ViewAllDAO
     */
    public RollBackDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            RollBackDAO dao = new RollBackDAO();
            HashMap mapParam = new HashMap();

            dao.execute(mapParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getRollBackID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ROLL_BACK_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    //Common For Account Opening and Closing
    private void accountOpening(HashMap map) throws Exception {
        System.out.println("############## Account Opening Details : " + map);
        map.put("BATCH_ID", map.get("ACT_NUM"));
        if(map.containsKey("singleTrasnId") && map.get("singleTrasnId") != null){
        	   map.put("singleTrasnId",CommonUtil.convertObjToStr(map.get("singleTrasnId")));
        }
        map.put("TRANS_DT", currDt);
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", map);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", map);
        HashMap transMap = new HashMap();
        if (cashList != null && cashList.size() > 0) {
            transMap = (HashMap) cashList.get(0);
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Opening")
			|| CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Closing")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Opening")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Renewal")) {
                map.put("LINK_BATCH_ID", map.get("ACT_NUM"));
                map.put("LINK_BATCH", "LINK_BATCH");
                
            } else {
                map.put("TRANS_ID", transMap.get("TRANS_ID"));
            }
            doCashRollBack(map);
        }
        if (transList != null && transList.size() > 0) {
            transMap = (HashMap) transList.get(0);
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Opening")
			|| CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Closing")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Opening")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")
            || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Renewal")) {
                map.put("LINK_BATCH_ID", map.get("ACT_NUM"));
                map.put("LINK_BATCH", "LINK_BATCH");
            } else {
                map.put("TRANS_ID", transMap.get("BATCH_ID"));
            }
            doTransferRollBack(map);
        }
    }

    private void MDSReceitRollBack(HashMap map) throws Exception {
        System.out.println("############## MDS RollBack Details : " + map);
        map.put("TRANS_DT", currDt);
        map.put("INITIATED_BRANCH", _branchCode);
        List receiptTransList = (List) sqlMap.executeQueryForList("getSelectMDSTransIDList", map);
        HashMap transMap = new HashMap();
        if (receiptTransList != null && receiptTransList.size() > 0) {
            transMap = (HashMap) receiptTransList.get(0);
            transMap.put("SUB_NO", CommonUtil.convertObjToInt(transMap.get("SUB_NO")));
            System.out.println("#################### TransIDList : " + transMap);
            map.put("MDS_DETAILS",transMap);
            if (transMap != null && transMap.get("NET_TRANS_ID") != null && !transMap.get("NET_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("NET_TRANS_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("BONUS_TRANS_ID") != null && !transMap.get("BONUS_TRANS_ID").equals("")) {
                String initiatedBranchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                map.put("ACT_NUM", transMap.get("BONUS_TRANS_ID"));
                map.put("BONUS_TRANSACTION_EXISTS","BONUS_TRANSACTION_EXISTS");
                HashMap dataMap = (HashMap) map.get("MDS_DETAILS");
                List dataList = ServerUtil.executeQuery("getChittalBranchCode", dataMap);
                if (dataList != null && dataList.size() > 0) {
                    dataMap = (HashMap) dataList.get(0);
                    if (dataMap.containsKey("BRANCH_CODE") && dataMap.get("BRANCH_CODE") != null) {
                        map.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    }
                }
                accountOpening(map);
                map.remove("BONUS_TRANSACTION_EXISTS");
                map.put("BRANCH_CODE",initiatedBranchCode);
                
            }
            if (transMap != null && transMap.get("DISCOUNT_TRANS_ID") != null && !transMap.get("DISCOUNT_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("DISCOUNT_TRANS_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("PENAL_TRANS_ID") != null && !transMap.get("PENAL_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("PENAL_TRANS_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("ARBITRATION_ID") != null && !transMap.get("ARBITRATION_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("ARBITRATION_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("NOTICE_ID") != null && !transMap.get("NOTICE_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("NOTICE_ID"));
                accountOpening(map);
            }
            // For Waive - rollback
            if (transMap != null && transMap.get("PENAL_WAIVE_TRANS_ID") != null && !transMap.get("PENAL_WAIVE_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("PENAL_WAIVE_TRANS_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("NOTICE_WAIVE_TRANS_ID") != null && !transMap.get("NOTICE_WAIVE_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("NOTICE_WAIVE_TRANS_ID"));
                accountOpening(map);
            }
            if (transMap != null && transMap.get("ARC_WAIVE_TRANS_ID") != null && !transMap.get("ARC_WAIVE_TRANS_ID").equals("")) {
                map.put("ACT_NUM", transMap.get("ARC_WAIVE_TRANS_ID"));
                accountOpening(map);
            }
            // End
            //Update BankAdvance_Repaid="NO"
            List bankAdvanceLst = sqlMap.executeQueryForList("getSelectRollBackBankAdvanceDetailsData", transMap);
            if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                sqlMap.executeUpdate("updateRollBackMDSBankAdvanceRepaidStatus", transMap);
            }
            map.put("NET_TRANS_ID", transMap.get("NET_TRANS_ID"));
        }
    }
    private String getSelectMaxOfSingleTransId(String acccountNo) {
        String singleTransID = "";
        try {
            HashMap wheremap = new HashMap();
            wheremap.put("ACT_NUM", acccountNo);
            wheremap.put("TRANS_DT",currDt.clone());
            List singleTransList = (List) sqlMap.executeQueryForList("getmaxOfSingleTransID", wheremap);
            if (singleTransList != null && singleTransList.size() > 0) {
                HashMap singleTransmap = (HashMap) singleTransList.get(0);
                if(singleTransmap.containsKey("SINGLE_TRANS_ID") && singleTransmap.get("SINGLE_TRANS_ID") != null){
               	 singleTransID = CommonUtil.convertObjToStr(singleTransmap.get("SINGLE_TRANS_ID"));
                }
            }
         return singleTransID;   
        } catch (Exception e) {
        }
        return singleTransID;
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("RollbackDAOMap : " + map);
        rollBackMap = new HashMap();
        try {
            sqlMap.startTransaction();
            commitFlag = true;
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            rollBackID = getRollBackID();
            rollBackMap.put("ROLL_BACK_ID", rollBackID);
            rollBackMap.put("ROLL_BACK_DT", currDt);
            rollBackMap.put("KEY_FIELD", map.get("TRANS_ID"));
            rollBackMap.put("SCREEN_NAME", map.get("ROLL_BACK_SCREEN"));
            rollBackMap.put("ROLL_BACK_REASON", map.get("ROLL_BACK_REASON"));
            rollBackMap.put("STATUS_BY", map.get("USER_ID"));
            rollBackMap.put("AUTHORIZED_BY", map.get("AUTHORIZED_BY"));
            sqlMap.executeUpdate("insertRollBackDetails", rollBackMap);
            //Transaction Starting
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Cash Transactions") || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Multiple Cash Transaction")) {
                doCashRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Transfer Transactions")) {
                doTransferRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Opening")) {
                accountOpening(map);//Common For Account Opening and Closing
                updateOperativeAccountOpeningDetails(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Closing")) {
                accountOpening(map);//Common For Account Opening and Closing
                map.put("ENTERED_AMOUNT", "ENTERED_AMOUNT");
                updateOperativeAccountClosingDetails(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Opening")) {
                map.put("DEPOSIT NO", map.get("ACT_NUM"));
                map.put("ACT_NUM", map.get("ACT_NUM") + "_1");
                accountOpening(map);//Common For Account Opening and Closing
                updateDepositAccountOpeningDetails(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")
                    || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Renewal")) {
//                 String singleTrasnId = null;
//                 singleTrasnId = getSelectMaxOfSingleTransId(CommonUtil.convertObjToStr(map.get("ACT_NUM") + "_1"));
                if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Renewal")) {
                    map.put("DEPOSIT NO", map.get("RENEWAL_FROM_DEPOSIT"));
                    map.put("ACCOUNTNO", map.get("RENEWAL_FROM_DEPOSIT"));
                    map.put("DEPOSIT_NO", map.get("RENEWAL_FROM_DEPOSIT"));
                    map.put("NEW_DEPOSIT_NO", map.get("ACT_NUM"));
                    map.put("ACT_NUM", map.get("RENEWAL_FROM_DEPOSIT") + "_1");
                    map.put("GL_TRANS_ACT_NUM", map.get("RENEWAL_FROM_DEPOSIT") + "_1");
//                    if(singleTrasnId != null && !singleTrasnId.equals("")){
//                        map.put("singleTrasnId",singleTrasnId);
//                    }
                } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")) {
                    map.put("DEPOSIT NO", map.get("ACT_NUM"));
                    map.put("ACCOUNTNO", map.get("ACT_NUM"));
                    map.put("DEPOSIT_NO", map.get("ACT_NUM"));
                    map.put("ACT_NUM", map.get("ACT_NUM") + "_1");
                    map.put("ENTERED_AMOUNT", "ENTERED_AMOUNT");
                    if(map.containsKey("RENEWAL_FROM_DEPOSIT") && map.get("RENEWAL_FROM_DEPOSIT") != null){//Added by nithya on 02-12-2019 for KD-979 Daily deposit closing rollback issue
                       map.put("GL_TRANS_ACT_NUM", map.get("RENEWAL_FROM_DEPOSIT") + "_1");
                    }
//                    if(singleTrasnId != null && !singleTrasnId.equals("")){
//                        map.put("singleTrasnId", singleTrasnId);
//                    }
                }
                accountOpening(map);//Common For Account Opening and Closing
                if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Renewal")) {
                    updateDepositAccountRenewalDetails(map);
                } else {
                    updateDepositAccountClosingDetails(map);
                    //Lien account roll back code added by Kannan AR Jira: KDSA-163
                    List lienList = sqlMap.executeQueryForList("getDepositLienEntry", map);
                    if (lienList != null && lienList.size() > 0) {
                        for (int i = 0; i < lienList.size(); i++) {
                            DepositLienTO lienTo = (DepositLienTO) lienList.get(i);
                            if (CommonUtil.convertObjToStr(lienTo.getLienAcNo()).length() > 0) {
                                map.put("ACCT_NUM", CommonUtil.convertObjToStr(lienTo.getLienAcNo()));                                
                                doLienRollBack(map);
                            }
                        }
                    }               
                }
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Loans/Advances Account Opening")
                    || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Gold Loan Account Opening")
                    || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Loan Account Opening")) {
                doLoanAccoutOpeningRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Indend Transactions")) {
                IndendRollBack(map);
             } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Trade Expense")) {
                TradeRollBack(map);       
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Accounts with other bank Master")) {
                ABRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Investment Master")) {
                InvMasterRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Loan Account Closing")) {
                doLoanAccoutClosingRollBack(map);
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("MDS Receipts")) {
                MDSReceitRollBack(map);//Common For Account Opening and Closing
                updateMDSReceiptDetails(map);
            }
            updateStatusInRemitIssueTable(map);
            if (commitFlag) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
          //  throw e;
            return rollBackMap;
            
        }
        System.out.println("rollBackMap####### : " + rollBackMap);
        return rollBackMap;
    }
    
    //Added By Kannan AR
    private void doLienRollBack(HashMap map) throws Exception {
        HashMap updatemap = new HashMap();
        HashMap depositMap = new HashMap();
        HashMap lienMap = new HashMap();
        map.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
        map.put("LINK_BATCH", "LINK_BATCH");
        map.put("LIEN_STATUS", "LIEN_STATUS");
        map.put("ENTERED_AMOUNT", "ENTERED_AMOUNT");
        //rollback transaction
        //In case Deposit LTD closing all roll back doing with respective link_batch_id
        if (map.containsKey("singleTrasnId")) {
            map.remove("singleTrasnId");
        }
        doTransferRollBack(map);
        //doCashRollBack(map);
        //update deposit availble balance
        List lst = sqlMap.executeQueryForList("getDepositLienUnlienTOForRollBack", map);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                //UPDATE  DEPOSIT SUB ACINFO AVAILABLE BALANCE
                DepositLienTO lienTo = (DepositLienTO) lst.get(i);
                depositMap.put("LIENAMOUNT", lienTo.getLienAmount());
                depositMap.put("SHADOWLIEN", new Double(0));
                depositMap.put("DEPOSIT_ACT_NUM", lienTo.getDepositNo());
                depositMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                depositMap.put("STATUS", "LIEN");
                sqlMap.executeUpdate("updateSubAcInfoBalForRollBack", depositMap);
            }
            //lien auth status  put as Authorized
            lienMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            lienMap.put("AUTHORIZE_DATE", currDt);
            lienMap.put("STATUS", "CREATED");
            lienMap.put("USER_ID", map.get("USER_ID"));
            lienMap.put("LIENNO", map.get("ACCT_NUM"));
            sqlMap.executeUpdate("authorizeLienTOForRollBack", lienMap);
        }
        //UPDATE  ACT_CLOSING AUTH STATUS AS REJECTED
        lienMap.put("STATUS", "REJECTED");
        lienMap.put("AUTHORIZEDT", currDt);
        lienMap.put("DELETE", "CREATED");
        lienMap.put("USER_ID", map.get("USER_ID"));
        lienMap.put("ACCOUNTNO", map.get("ACCT_NUM"));
        sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", lienMap);
//        lienMap.put("LIENAMOUNT","LIENAMOUNT");
//        lienMap.put("SHADOWLIEN","SHADOWLIEN");
//        lienMap.put("DEPOSIT_ACT_NUM","DEPOSIT_ACT_NUM");
//        lienMap.put("SUBNO","SUBNO");
//        sqlMap.executeUpdate("updateSubAcInfoBal",lienMap);
        
        if (map.containsKey("SERVICE_TAX_ROLLBACK")) {
            HashMap whrMap = new HashMap();
            whrMap.put("USER_ID", map.get("USER_ID"));
            whrMap.put("TRANS_ID", map.get("ACCT_NUM"));
            whrMap.put("TRANS_DT", currDt);
            sqlMap.executeUpdate("rollbackServiceTaxDetails", whrMap);
        }
      
        //loan ACCT STATUS   put as NEW
        updatemap.put("ACCT_STATUS", "NEW");
        updatemap.put("USER_ID", map.get("USER_ID"));
        updatemap.put("AUTHORIZEDT", currDt);
        updatemap.put("ACCT_NUM", map.get("ACCT_NUM"));
        sqlMap.executeUpdate("updateStatusForAccountTL", updatemap);
        HashMap dataMap = new HashMap();
        dataMap.put("USER_ID", map.get("USER_ID"));
        dataMap.put("PENAL_WAIVE_DT", currDt);
        dataMap.put("ACCT_NUM", map.get("ACCT_NUM"));
        List dataList = ServerUtil.executeQuery("getAccountIsPenalWaive", dataMap);
        if (dataList.size() > 0 && dataList.get(0) != null) {
            HashMap mapData = (HashMap) dataList.get(0);
            String penalWaive = CommonUtil.convertObjToStr(mapData.get("PENAL_WAIVE_OFF"));
            if (penalWaive != null && penalWaive.equals("Y")) {
                sqlMap.executeUpdate("updateLoanInterestWaiveOff", dataMap);
            }
        }
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_CLOSED);
        sqlMap.executeUpdate("updateTLRolledBackStatus", map);
    }
    
    
    private void updateMDSReceiptDetails(HashMap map) throws Exception {
        System.out.println("############## MDS Receipt Update Details : " + map);
        map.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
        map.put("AUTHORIZE_BY", map.get("USER_ID"));
        map.put("BRANCH_CODE", _branchCode);
        map.put("TRANS_DT", currDt);
        sqlMap.executeUpdate("updateAuthorizeReceiptEntry", map);
        sqlMap.executeUpdate("updateMDSTransRollBackStatus", map);
        sqlMap.executeUpdate("updatePaidAmount", map);
        // Added by nithya on 07-08-2018 for KD 188 - Mds receipt entry last installment reject/roll back issue        
        String chital = CommonUtil.convertObjToStr(map.get("CHITTAL_NO"));
        String chitalNo = chital.lastIndexOf("_") != -1 ? chital.substring(0, chital.length() - 2) : chital;
        map.put("CHITTAL_NO",chitalNo);
        sqlMap.executeUpdate("updateMDSTransRollBackChitCloseDt", map);    
        // End
        if (map.containsKey("SERVICE_TAX_ROLLBACK")) {
            HashMap whrMap = new HashMap();
            whrMap.put("USER_ID", map.get("USER_ID"));
            whrMap.put("TRANS_ID", map.get("NET_TRANS_ID"));
            whrMap.put("TRANS_DT", map.get("TRANS_DT"));
            sqlMap.executeUpdate("rollbackServiceTaxDetails", whrMap);
        }
    }

    private void ABRollBack(HashMap map) throws Exception {
        map.put("TRANS_DT", currDt);
        map.put("LINK_BATCH_ID", map.get("ACT_MASTER_ID"));
        map.put("LINK_BATCH", "LINK_BATCH");
        doTransferRollBack(map);
        doCashRollBack(map);
        map.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
        map.put("STATUS", CommonConstants.STATUS_DELETED);
        sqlMap.executeUpdate("updateABMaster", map);
    }

    private void InvMasterRollBack(HashMap map) throws Exception {
        map.put("TRANS_DT", currDt);
        map.put("LINK_BATCH_ID", map.get("INVESTMENT_ID"));
        map.put("LINK_BATCH", "LINK_BATCH");
        doTransferRollBack(map);
        doCashRollBack(map);
        map.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
        map.put("STATUS", CommonConstants.STATUS_DELETED);
        sqlMap.executeUpdate("updateInvMaster", map);
        sqlMap.executeUpdate("updateInvDeposit", map);
        sqlMap.executeUpdate("updateInvTransDetails", map);        
    }

    private void IndendRollBack(HashMap map) throws Exception {
        map.put("TRANS_DT", currDt);
        map.put("LINK_BATCH_ID", map.get("IRID"));
        map.put("LINK_BATCH", "LINK_BATCH");
        doTransferRollBack(map);
        doCashRollBack(map);
        map.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
        map.put("STATUS", CommonConstants.STATUS_DELETED);
        sqlMap.executeUpdate("updateIndendRegister", map);

    }
    private void TradeRollBack(HashMap map) throws Exception {
        System.out.println("mapmapmapmapmapmapmapmapmapmapmapmapmap"+map);
        map.put("TRANS_DT", currDt);
        map.put("LINK_BATCH_ID", map.get("TRADEEXEPENSE_ID"));
        map.put("LINK_BATCH", map.get("TRADEEXEPENSE_ID"));
        map.put("ENTERED_AMOUNT",map.get("AMOUNT"));
        doCashRollBackForTrade(map);
        map.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
        map.put("STATUS", CommonConstants.STATUS_DELETED);
        sqlMap.executeUpdate("updateTradeExpenseRegister", map);
    }

    private void updateDepositAccountClosingDetails(HashMap map) throws Exception {
        System.out.println("############## Deposit Account Closing Details : " + map);        
        map.put("STATUS", CommonConstants.STATUS_REJECTED);
        map.put("AUTHORIZEDT", currDt);
        map.put("TRANS_OUT", "N");
        HashMap existingHistoryofCRDRIntMap = new HashMap();
        List existingHistoryofCRDRIntList = sqlMap.executeQueryForList("getExistingHistoryDetailsofCRDR", map);
        if (null != existingHistoryofCRDRIntList && existingHistoryofCRDRIntList.size() > 0) {
            existingHistoryofCRDRIntMap = (HashMap) existingHistoryofCRDRIntList.get(0);
            map.put("TOTAL_INT_CREDIT", CommonUtil.convertObjToDouble(existingHistoryofCRDRIntMap.get("EXISTING_TOTAL_INT_CREDIT")));
            map.put("TOTAL_INT_DRAWN", CommonUtil.convertObjToDouble(existingHistoryofCRDRIntMap.get("EXISTING_TOTAL_INT_DRAWN")));
            //Below three line added by Ajay Sharma for mantis ID 9086 dated 14 May 2014
            map.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(existingHistoryofCRDRIntMap.get("TEMP_CLEAR_BALANCE")));
            map.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(existingHistoryofCRDRIntMap.get("TEMP_AVAILABLE_BALANCE")));
            map.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(existingHistoryofCRDRIntMap.get("TEMP_TOTAL_BALANCE")));
            map.put("DEPOSIT NO", map.get("ACCOUNTNO"));
            System.out.println("existingHistoryofCRDRIntList map : " + map);
            sqlMap.executeUpdate("updateRollbackCRDRDetails", map);
            //RenewDate update by Kannan AR        
            Date oldRenewDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingHistoryofCRDRIntMap.get("TEMP_RENEWED_DT")));            
            map.put("RENEW_DATE", oldRenewDt);
            if (oldRenewDt != null) {
                map.put("RENEW_DATE", oldRenewDt);
            }
            sqlMap.executeUpdate("rollBackDepositRenewDate", map);
        }
         if (map.containsKey("SERVICE_TAX_ROLLBACK")) {
            HashMap whrMap = new HashMap();
            whrMap.put("USER_ID", map.get("USER_ID"));
            whrMap.put("TRANS_ID", map.get("ACCOUNTNO"));
            whrMap.put("TRANS_DT", map.get("TRANS_DT"));
            sqlMap.executeUpdate("rollbackServiceTaxDetails", whrMap);
        }
        sqlMap.executeUpdate("updatePenalInt", map);
        sqlMap.executeUpdate("updateSubDepositCloseNew", map);
        //added by rishad for 	0005080: While Roll Back A Term Deposit Closure 27/09/2016
        sqlMap.executeUpdate("updateDepositCloseDate", map);
        sqlMap.executeUpdate("updateDepositAcinfoCloseNew", map);
        sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", map);
        sqlMap.executeUpdate("updateTransferOutFlag", map);
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_CLOSED);
        sqlMap.executeUpdate("updateTDRolledBackStatus", map);
        //Set of code Added by Jeffin To Change Status of the deposit number which got creted while doing deposit renewal for Mantis : 10106
        String depNo = CommonUtil.convertObjToStr(map.get("ACCOUNTNO"));
        System.out.println("depNo===="+depNo);
        HashMap detailedMap = new HashMap();
        detailedMap.put("TRANS_DT", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TRANS_DT")))));
        detailedMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(depNo));
        List newDeposiNoList = sqlMap.executeQueryForList("getRenewedNewDepositNumber", detailedMap);
        System.out.println("newDeposiNoListnewDeposiNoList"+newDeposiNoList);
        if(newDeposiNoList != null && newDeposiNoList.size()>0){
            HashMap depositNoMap = (HashMap) newDeposiNoList.get(0);
            if(depositNoMap != null && depositNoMap.size()>0 && depositNoMap.containsKey("DEPOSIT_NO")){
                String newDepNo = CommonUtil.convertObjToStr(depositNoMap.get("DEPOSIT_NO"));
                System.out.println("newDepNo"+newDepNo);
                if(newDepNo != null && !newDepNo.equals("")){
                    HashMap detailsMap = new HashMap();
                    detailsMap.put("USER_ID", CommonUtil.convertObjToStr(map.get("USER_ID")));
                    detailsMap.put("STATUS", CommonConstants.STATUS_REJECTED);
                    detailsMap.put("CURR_DATE", currDt);
                    detailsMap.put("DEPOSIT NO", newDepNo);
                    detailsMap.put("DEPOSIT_NO", newDepNo);
                    sqlMap.executeUpdate("authorizeDepositAccInfo", detailsMap);
                    sqlMap.executeUpdate("authorizeDepositSubAccInfo", detailsMap);
                    detailsMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                    sqlMap.executeUpdate("updateRejectionStatusAcinfo", detailsMap);
                    sqlMap.executeUpdate("updateRejectionStatusSubAcinfo", detailsMap);
                    detailsMap.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_NEW);
                    sqlMap.executeUpdate("updateTDRolledBackStatus", detailsMap);
                }
            }
        }
        //Ends here code added by Jeffin John 
   }

    private void updateDepositAccountRenewalDetails(HashMap map) throws Exception {
        System.out.println("############## updateDepositAccountRenewalDetails : " + map);
        map.put("STATUS", CommonConstants.STATUS_REJECTED);
        map.put("AUTHORIZEDT", currDt);
//        map.put("USER_ID",);
        map.put("CURR_DATE", currDt);
        map.put("DEPOSIT_SUB_NO", CommonUtil.convertObjToInt("1"));
        //update old account acct status  set as new 
        List lst = sqlMap.executeQueryForList("getSelectSameDepSubNoAccInfoTO", map);
        if (lst != null && lst.size() > 0) {
            HashMap depositMap = (HashMap) lst.get(0);
            depositMap.put("TOTAL_INT_CREDIT", depositMap.get("EXISTING_TOTAL_INT_CREDIT"));
            depositMap.put("TOTAL_INT_DRAWN", depositMap.get("EXISTING_TOTAL_INT_DRAWN"));
            depositMap.put("DEPOSIT_SUB_NO",CommonUtil.convertObjToInt(depositMap.get("DEPOSIT_SUB_NO")));
            sqlMap.executeUpdate("updateSubDepositTO", depositMap);
        }
        sqlMap.executeUpdate("updateSubDepositCloseNew", map);  //new set as old deposit no
        sqlMap.executeUpdate("updateDepositAcinfoCloseNewRollBack", map);//new set as old deposit no
        sqlMap.executeUpdate("deleteDepositInterest", map);
        //update new account auth status as rejected 
        // need not to update for renew no and deposit no  as same 
        if (!CommonUtil.convertObjToStr(map.get("DEPOSIT_NO")).equals(CommonUtil.convertObjToStr(map.get("NEW_DEPOSIT_NO")))) {
            sqlMap.executeUpdate("rollBackNomineeStatusUpdationTD", map);
            map.put("DEPOSIT_NO", map.get("NEW_DEPOSIT_NO"));
            map.put("ACCOUNTNO", map.get("NEW_DEPOSIT_NO"));
            sqlMap.executeUpdate("authorizeDepositSubClose", map);
            sqlMap.executeUpdate("authorizeDepositClose", map);
            sqlMap.executeUpdate("deleteNomineeTOTD", map.get("DEPOSIT_NO"));
        } else {
            sqlMap.executeUpdate("rollBackDepositSubAcInfoSameNo", map);
        }
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_RENEWED);
        sqlMap.executeUpdate("updateTDRolledBackStatus", map);
//        sqlMap.executeUpdate("updateTransferOutFlag", map);
        
        //RenewDate update by Kannan AR        
        List depList = (List) sqlMap.executeQueryForList("getRenewalCountForDep", map);
        if (depList != null && depList.size() > 0) {
            HashMap depMap = (HashMap) depList.get(0);
            Date oldRenewDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depMap.get("TEMP_RENEWED_DT")));            
            map.put("RENEW_DATE", oldRenewDt);
            if (oldRenewDt != null) {
                map.put("RENEW_DATE", oldRenewDt);
            }
            sqlMap.executeUpdate("rollBackDepositRenewDate", map);
        }
    }

    private void updateDepositAccountOpeningDetails(HashMap map) throws Exception {
        System.out.println("############## Deposit Account Opening Details : " + map);
        map.put("STATUS", CommonConstants.STATUS_REJECTED);
        map.put("CURR_DATE", currDt);
        sqlMap.executeUpdate("authorizeDepositAccInfo", map);
        sqlMap.executeUpdate("authorizeDepositSubAccInfo", map);
        map.put("ACCT_STATUS", CommonConstants.CLOSED);
        sqlMap.executeUpdate("updateRejectionStatusAcinfo", map);
        sqlMap.executeUpdate("updateRejectionStatusSubAcinfo", map);
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_NEW);
        sqlMap.executeUpdate("updateTDRolledBackStatus", map);
        //unSubscribe the SMS part added by Kannan AR
        HashMap smsMap = new HashMap();
        smsMap.put("PROD_TYPE", map.get("PROD_TYPE"));
        smsMap.put("PROD_ID", map.get("PROD_ID"));
        smsMap.put("ACT_NUM", map.get("DEPOSIT NO"));
        smsMap.put("STATUS_DT", currDt);
        smsMap.put(CommonConstants.USER_ID, map.get("USER_ID"));        
        unSubscribeSMS(smsMap);
    }

    private void updateOperativeAccountOpeningDetails(HashMap map) throws Exception {
        System.out.println("############## Operative Account Opening Details : " + map);
        map.put("STATUS", CommonConstants.STATUS_REJECTED);
        map.put("AUTHORIZEDT", currDt);
        map.put("ACCOUNTNO", map.get("ACT_NUM"));
        sqlMap.executeUpdate("authorizeAccountMaster", map);
        //sqlMap.executeUpdate("authorizeSMSSubscriptionMap", map);
        map.put("ACT_STATUS_ID", CommonConstants.CLOSED);
        map.put("ACT_STATUS_DT", currDt);
        sqlMap.executeUpdate("updateRollbackAccountMasterStatusId", map);
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_NEW);
        sqlMap.executeUpdate("updateOARolledBackStatus", map);
        //unSubscribe the SMS part added by Kannan AR
        HashMap smsMap = new HashMap();
        smsMap.put("PROD_TYPE", map.get("PROD_TYPE"));
        smsMap.put("PROD_ID", map.get("PROD_ID"));
        smsMap.put("ACT_NUM", map.get("ACT_NUM"));
        smsMap.put("STATUS_DT", currDt);
        smsMap.put(CommonConstants.USER_ID, map.get("USER_ID"));        
        unSubscribeSMS(smsMap);
    }

    private void updateOperativeAccountClosingDetails(HashMap map) throws Exception {
        System.out.println("############## Operative Account Closing Details : " + map);
        map.put("STATUS", CommonConstants.STATUS_REJECTED);
        map.put("AUTHORIZEDT", currDt);
        map.put("ACCOUNTNO", map.get("ACT_NUM"));
        map.put("ACT_STATUS_ID", "NEW");
        map.put("TODAY_DT", currDt);
        sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", map);
        sqlMap.executeUpdate("updateStatusForAccount", map);
        sqlMap.executeUpdate("updateLastTransDateOA", map);
        sqlMap.executeUpdate("updateAcctPaymentLASTCRINTApplDt", map);
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_CLOSED);
        sqlMap.executeUpdate("updateOARolledBackStatus", map);
    }

    private String getCashAcctHead() throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectTransParams", null);
        return (String) ((HashMap) list.get(0)).get("CASH_AC_HD");
    }

    private void doLoanAccoutOpeningRollBack(HashMap map) throws Exception {
        HashMap updatemap = new HashMap();
        HashMap depositMap = new HashMap();
        HashMap lienMap = new HashMap();
        String prodCategory = "";
        //modified by rishad 28/09/2016 solving gold renewal rollback
        if (!map.containsKey("RENEWED_LOAN_NO") ||CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO")) == null || map.get("RENEWED_LOAN_NO").equals("")) {
            map.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
        } else {
            map.put("LINK_BATCH_ID", map.get("RENEWED_LOAN_NO"));
        }
        map.put("LINK_BATCH", "LINK_BATCH");
        map.put("ENTERED_AMOUNT", "ENTERED_AMOUNT");
        //rollback transaction
        doTransferRollBack(map);
        doCashRollBack(map);
        //update deposit availble balance
        List lst = sqlMap.executeQueryForList("getDepositLienUnlienTOForRollBack", map);
        if (lst != null && lst.size() > 0) {
            HashMap behavesMap = new HashMap(); //Added by nithya for KD-2389
            behavesMap.put("ACT_NUM",map.get("ACCT_NUM"));
            List  catogerylist = sqlMap.executeQueryForList("getBehavesLikeTLAD", behavesMap);
            if(catogerylist != null && catogerylist.size() > 0){
                HashMap categoryMap = (HashMap)catogerylist.get(0);
                if(categoryMap.containsKey("AUTHORIZE_REMARK") && categoryMap.get("AUTHORIZE_REMARK") != null && categoryMap.get("AUTHORIZE_REMARK").equals("MDS_LOAN")){
                    prodCategory = "MDS_LOAN";
                }
            }
            for (int i = 0; i < lst.size(); i++) {
                DepositLienTO lienTo = (DepositLienTO) lst.get(i);
                depositMap.put("LIENAMOUNT", lienTo.getLienAmount());
                depositMap.put("SHADOWLIEN", new Double(0));
                depositMap.put("DEPOSIT_ACT_NUM", lienTo.getDepositNo());
                depositMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                depositMap.put("STATUS", "CREATED");
                sqlMap.executeUpdate("updateSubAcInfoBalForDepositLoanRollBack", depositMap);
                if (prodCategory.equals("MDS_LOAN")) {
                    String chittalNo = lienTo.getDepositNo();
                    if (lienTo.getDepositNo().lastIndexOf("_") != -1) {
                        chittalNo = lienTo.getDepositNo().substring(0, lienTo.getDepositNo().lastIndexOf("_"));
                    }
                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    sqlMap.executeUpdate("updateMDSApplDeletedLoanGivenStatus", chittalMap);
                }
            }

        }
        //lien auth status  put as Rejectted
        lienMap.put("AUTHORIZE_STATUS", "REJECTED");
        lienMap.put("AUTHORIZE_DATE", currDt);
        lienMap.put("STATUS", "CREATED");
        lienMap.put("USER_ID", map.get("USER_ID"));
        lienMap.put("LIENNO", map.get("ACCT_NUM"));
        sqlMap.executeUpdate("authorizeLienTOForRollBack", lienMap);

        //loan auth status  put as Rejectted
        updatemap.put("AUTHORIZESTATUS", "REJECTED");
        updatemap.put("USER_ID", map.get("USER_ID"));
        updatemap.put("AUTHORIZEDT", currDt);
        updatemap.put("ACCT_NUM", map.get("ACCT_NUM"));
        updatemap.put("STATUS_DT", currDt);
        sqlMap.executeUpdate("authorizeTermLoan", updatemap);
        //Rollback Loans_security_member table
        sqlMap.executeUpdate("authorizeMemberDetails", updatemap);
        //end
        //Rollback security details
        HashMap aHmap = new HashMap();
        aHmap.put("acctNum", map.get("ACCT_NUM"));
        aHmap.put("authorizeStatus", "REJECTED");
        aHmap.put("authorizeDt", currDt);
        aHmap.put("authorizeBy", map.get("USER_ID"));
        sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", aHmap);
        //end
        updatemap.put("ACCT_STATUS",CommonConstants.CLOSED);
        sqlMap.executeUpdate("updateStatusForAccountTL", updatemap);
        //added by rishad 28/09/2016 old lan status changing at the time renawl rollback
        if (!map.containsKey("RENEWED_LOAN_NO") || map.get("RENEWED_LOAN_NO").toString() == null || map.get("RENEWED_LOAN_NO").equals("")) {
        } else {
            updatemap.put("ACCT_STATUS",CommonConstants.NEW);
            updatemap.put("ACCT_NUM", map.get("RENEWED_LOAN_NO"));
            sqlMap.executeUpdate("updateStatusForAccountTL", updatemap);
        }
        //end
        updatemap.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_NEW);
        sqlMap.executeUpdate("updateTLRolledBackStatus", updatemap);
        //unSubscribe the SMS part added by Kannan AR
        HashMap smsMap = new HashMap();
        smsMap.put("PROD_TYPE", "TL");       
        smsMap.put("PROD_ID", map.get("PROD_ID"));
        smsMap.put("ACT_NUM", map.get("ACCT_NUM"));
        smsMap.put("STATUS_DT", currDt);
        smsMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
        unSubscribeSMS(smsMap);
    }

    private void doLoanAccoutClosingRollBack(HashMap map) throws Exception {
        HashMap updatemap = new HashMap();
        HashMap depositMap = new HashMap();
        HashMap lienMap = new HashMap();

        map.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
        map.put("LINK_BATCH", "LINK_BATCH");
        map.put("LIEN_STATUS", "LIEN_STATUS");
        map.put("ENTERED_AMOUNT", "ENTERED_AMOUNT");
        //rollback transaction
        doTransferRollBack(map);
        doCashRollBack(map);
        //update deposit availble balance
        List lst = sqlMap.executeQueryForList("getDepositLienUnlienTOForRollBack", map);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                //UPDATE  DEPOSIT SUB ACINFO AVAILABLE BALANCE
                DepositLienTO lienTo = (DepositLienTO) lst.get(i);
                depositMap.put("LIENAMOUNT", lienTo.getLienAmount());
                depositMap.put("SHADOWLIEN", new Double(0));
                depositMap.put("DEPOSIT_ACT_NUM", lienTo.getDepositNo());
                depositMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                depositMap.put("STATUS", "LIEN");
                sqlMap.executeUpdate("updateSubAcInfoBalForRollBack", depositMap);
            }
            //lien auth status  put as Authorized
            lienMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            lienMap.put("AUTHORIZE_DATE", currDt);
            lienMap.put("STATUS", "CREATED");
            lienMap.put("USER_ID", map.get("USER_ID"));
            lienMap.put("LIENNO", map.get("ACCT_NUM"));
            sqlMap.executeUpdate("authorizeLienTOForRollBack", lienMap);
        }



        //UPDATE  ACT_CLOSING AUTH STATUS AS REJECTED

        lienMap.put("STATUS", "REJECTED");
        lienMap.put("AUTHORIZEDT", currDt);
        lienMap.put("DELETE", "CREATED");
        lienMap.put("USER_ID", map.get("USER_ID"));
        lienMap.put("ACCOUNTNO", map.get("ACCT_NUM"));
        sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", lienMap);
//        lienMap.put("LIENAMOUNT","LIENAMOUNT");
//        lienMap.put("SHADOWLIEN","SHADOWLIEN");
//        lienMap.put("DEPOSIT_ACT_NUM","DEPOSIT_ACT_NUM");
//        lienMap.put("SUBNO","SUBNO");
//        sqlMap.executeUpdate("updateSubAcInfoBal",lienMap);

        //added by rishad  23/06/2015 for SERVICE_TAX details table roll back purpose
        if (map.containsKey("SERVICE_TAX_ROLLBACK")) {
            HashMap whrMap = new HashMap();
            whrMap.put("USER_ID", map.get("USER_ID"));
            whrMap.put("TRANS_ID", map.get("ACCT_NUM"));
            whrMap.put("TRANS_DT", currDt);
            sqlMap.executeUpdate("rollbackServiceTaxDetails", whrMap);
        }
        //end rishad
        
        //loan ACCT STATUS   put as NEW
        updatemap.put("ACCT_STATUS", "NEW");
        updatemap.put("USER_ID", map.get("USER_ID"));
        updatemap.put("AUTHORIZEDT", currDt);
        updatemap.put("ACCT_NUM", map.get("ACCT_NUM"));
        sqlMap.executeUpdate("updateStatusForAccountTL", updatemap);
        //Update release status
        sqlMap.executeUpdate("updateRollBackSecurityReleaseStatus", updatemap);
        //End
        HashMap dataMap = new HashMap();
        dataMap.put("USER_ID", map.get("USER_ID"));
        dataMap.put("PENAL_WAIVE_DT", currDt);
        dataMap.put("ACCT_NUM", map.get("ACCT_NUM"));
        List dataList = ServerUtil.executeQuery("getAccountIsPenalWaive", dataMap);
        if (dataList.size() > 0 && dataList.get(0) != null) {
            HashMap mapData = (HashMap) dataList.get(0);
            String penalWaive = CommonUtil.convertObjToStr(mapData.get("PENAL_WAIVE_OFF"));
            if (penalWaive != null && penalWaive.equals("Y")) {
                sqlMap.executeUpdate("updateLoanInterestWaiveOff", dataMap);
            }
        }
        
        HashMap waiveMap = new HashMap();
        waiveMap.put("ACCT_NUM",CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
        waiveMap.put("WAIVE_OFF_DT",currDt.clone());
        List rejectWaiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanAuthorizedWaivingOffTO", waiveMap);
        if (rejectWaiveOffList != null && rejectWaiveOffList.size() > 0) {
            for (int i = 0; i < rejectWaiveOffList.size(); i++) {
                System.out.println("enterd inside rejectWaiveOffList rollback - loan closing");
                TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
                obj = (TermLoanPenalWaiveOffTO) rejectWaiveOffList.get(i);
                obj.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
                obj.setAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                obj.setAuthorizedDt(currDt);
                sqlMap.executeUpdate("updateTermLoanWaiveOffTO", obj);
            }
        }
        
        
        map.put("ROLLED_BACK_STATUS", CommonConstants.ROLLED_BACK_CLOSED);
        sqlMap.executeUpdate("updateTLRolledBackStatus", map);

    }

    public HashMap doTransferRollBack(HashMap map) throws Exception {
        String prodType = "";
        String acctNum = "";
        long transSlNo = 0;
        Double amount = null;
        HashMap insetMap = new HashMap();
        HashMap insertMap = new HashMap();
        HashMap transDetailsMap = new HashMap();
        HashMap transTempDtls = new HashMap();
        transTempDtls.put("DEPOSIT_NO", map.get("ACCOUNT NO"));
        System.out.println("Value of transTempDtls : " + transTempDtls);
        List tempLst = sqlMap.executeQueryForList("getDepositAccountDetails", transTempDtls);
        if (tempLst != null && tempLst.size() > 0) {
            HashMap updateMap = (HashMap) tempLst.get(0);
            updateMap.put("DEPOSIT_NO", updateMap.get("DEPOSIT_NO"));
            updateMap.put("LAST_INT_APPL_DT", updateMap.get("TEMP_LAST_INT_APPL_DT"));
            updateMap.put("INT_CREDIT", updateMap.get("TEMP_INT_CREDIT"));
            updateMap.put("INT_DRAWN", updateMap.get("TEMP_INT_DRAWN"));
            updateMap.put("NEXT_INT_APPL_DT", updateMap.get("TEMP_NEXT_INT_APPL_DT"));
            System.out.println("Value of updateMap : " + updateMap);
            sqlMap.executeUpdate("updateRollBackDepositTempDate", updateMap);
        }
        LogTO objLogTO = new LogTO();
        objLogTO.setBranchId(_branchCode);
        TxTransferTO transferTo = null;
        System.out.println("###### TransferRollbackMap:" + map);
        HashMap transMap = new HashMap();
        //        transMap.put("TRANS_ID",map.get("TRANS_ID"));
        if (map.containsKey("LINK_BATCH_ID") && map.get("LINK_BATCH_ID") != null) {
            transMap.put("LINK_BATCH_ID", map.get("LINK_BATCH_ID"));
            transMap.put("LINK_BATCH", map.get("LINK_BATCH"));
            //commrnted by rishad 01-01-2014
           // transMap.put("ENTERED_AMOUNT", map.get("ENTERED_AMOUNT"));
        } else {
            transMap.put("BATCHID", map.get("BATCH_ID"));
        }

        transMap.put("TRANS_DT", currDt);
        if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("MDS Receipts") && map.containsKey("BONUS_TRANSACTION_EXISTS")) {
            transMap.put("INITIATED_BRANCH", map.get("BRANCH_CODE"));
        } else {
        transMap.put("INITIATED_BRANCH", _branchCode);
        }
        //modified by rishad 13/dec/2018
//        if (map != null && map.containsKey("SINGLE_TRANS_ID")) {
//            transMap.put("SINGLE_TRANS_ID", map.get("SINGLE_TRANS_ID"));
//        }  
        if (map.containsKey("singleTrasnId") && map.get("singleTrasnId") != null) {
            transMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(map.get("singleTrasnId")));
        }
        List transList = null;
        if (map != null && map.containsKey("TANS_LIST_DATA")) {
            transList = (List) map.get("TANS_LIST_DATA");
            objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
        } else {
            transList = sqlMap.executeQueryForList("getBatchTxTransferTOsForRollBack", transMap);
        }
        //        String cashAcctHd = getCashAcctHead();
        String oldTransId = "";
        String newTransId = "";
        String screenName="";
        String transAllId="";
        //"transList.size()" is added in below condition by Ajay Sharma on 16-May-2014
        if (transList != null && transList.size() > 0) {
            for (int j = 0; j < transList.size(); j++) {
                acctNum=""; //LinkBatchId May Vary
                transferTo = new TxTransferTO();
                transferTo = (TxTransferTO) transList.get(j);
                if (j == 0) {
                    oldTransId = transferTo.getTransId();
                    screenName=transferTo.getScreenName();
                    transAllId=transferTo.getTransAllId();
               } else {
                    newTransId = transferTo.getTransId();
                }
                HashMap interestMap = new HashMap();
                HashMap getDateMap = new HashMap();
                System.out.println("j " + j + " transferTo####" + transferTo + " newTransId : " + newTransId + " oldTransId : " + oldTransId);
                HashMap ruleMap = getTransferRuleMap(transferTo);
                if (transferTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                    amount = new Double(-1 * transferTo.getAmount().doubleValue());
                } else {
                    amount = transferTo.getAmount();
                }
                System.out.println("ruleMap " + ruleMap + " amount : " + amount);
                if (transferTo != null && !oldTransId.equals("") && oldTransId.length() > 0 && !newTransId.equals("") && newTransId.length() > 0 && 
                !CommonUtil.convertObjToStr(transferTo.getBatchId()).equals(CommonUtil.convertObjToStr(transferTo.getInitiatedBranch()))                        ) {
                    HashMap transRefMap = new HashMap();
                    transRefMap.put("TRANS_ID", transferTo.getBatchId() + "_" + oldTransId);
                    transRefMap.put("STATUS", CommonConstants.STATUS_DELETED);
                    transRefMap.put("INITIATED_BRANCH", transferTo.getInitiatedBranch());
                    transRefMap.put("TRANS_DT", transferTo.getTransDt());
                    System.out.println("transRefMap ### : " + transRefMap);
                    sqlMap.executeUpdate("updateTransRefGLStatus", transRefMap);
                    transRefMap.put("TRANS_ID", transferTo.getBatchId() + "_" + newTransId);
                    sqlMap.executeUpdate("updateTransRefGLStatus", transRefMap);
                }
                //Added By Suresh
                if (transferTo.getProdType().equals(TransactionFactory.OPERATIVE) && transferTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                    HashMap inputMap = new HashMap();
                    inputMap.put("ACCOUNTNO", transferTo.getActNum());
                    List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
                    HashMap resultMap = (HashMap) list.get(0);
                    double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
                    double limit = CommonUtil.convertObjToDouble(resultMap.get("TOD_LIMIT")).doubleValue();
                    System.out.println("RollbackAmount#######" + transferTo.getAmount().doubleValue());
                    System.out.println("availableBalance#######" + availableBalance);
                    if ((availableBalance + limit) < transferTo.getAmount().doubleValue()) {
                        rollBackMap = new HashMap();
                        System.out.println("INSUFFICIENT_BALANCE#######");
                        rollBackMap.put("INSUFFICIENT_BALANCE", "INSUFFICIENT_BALANCE");
                        commitFlag = false;
                        sqlMap.rollbackTransaction();
                     // added by rishad 07/mar/2018 for throwing IB Exception
                       throw new ValidationRuleException(TransactionConstants.INSUFFICIENT_BALANCE);
                        //return rollBackMap;
                    }
                }
                transModuleBased = TransactionFactory.createTransaction(transferTo.getProdType());
                ruleMap.put("INTER_BRANCH_ROLL_BACK_TRANSACTION","INTER_BRANCH_ROLL_BACK_TRANSACTION");
                transModuleBased.updateAvailableBalance(ruleMap);
                transModuleBased.performOtherBalanceAdd(ruleMap);
                transModuleBased.updateGL(transferTo.getAcHdId(), amount, objLogTO, ruleMap);
                ruleMap.put(TransactionDAOConstants.TRANS_TYPE, transferTo.getTransType());
                ruleMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.GL);

//                if (ruleMap.containsKey("INTER_BRANCH_TRANS")
//                        && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("INTER_BRANCH_TRANS"))).booleanValue() == true) {
//                    //transModuleBased.updateGL(transferTo.getAcHdId(), amount, objLogTO, ruleMap);                    
//                } else {
                //transModuleBased.updateGL(transferTo.getAcHdId(), amount, objLogTO, ruleMap, true);
                //}
                updatepassbookFromTransfer(transferTo, ruleMap);
                if (transferTo.getProdType().equals(TransactionFactory.LOANS) || transferTo.getProdType().equals(TransactionFactory.ADVANCES) || transferTo.getProdType().equals(TransactionFactory.GL)) {

                    // IN CASE OF LOANS UPDATE LOAN_TRANS_DETAILS TABLE USING FOLLOWING MAPPED STATEMENT
                    // authorizeLoansDisbursementCumLoanDetails
                    List list = null;
                    if (acctNum != null && acctNum.length() > 0) {
                        interestMap.put(CommonConstants.ACT_NUM, acctNum);
                    } else {
                        interestMap.put(CommonConstants.ACT_NUM, transferTo.getLinkBatchId());
                        acctNum = (String) transferTo.getLinkBatchId();
                    }
                    HashMap lastCalDateMap = new HashMap();
                    lastCalDateMap.put(TransactionDAOConstants.ACCT_NO, interestMap.get(CommonConstants.ACT_NUM));
                    if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                        list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                    }
                    //unncomenteed by rishad
                    if (list != null && list.size() > 0) {
                        HashMap behaves = (HashMap) list.get(0);
                        prodType = CommonUtil.convertObjToStr(behaves.get(BEHAVES_LIKE));

                        interestMap.put(PROD_ID, behaves.get(PROD_ID));
                        prodType = prodType.equals("OD") ? TransactionFactory.ADVANCES : TransactionFactory.LOANS;
                        interestMap.put("TRANS_DT", currDt);
                        interestMap.put("INITIATED_BRANCH", _branchCode);
                        String mapNameForCalcInt = "IntCalculationDetail";
                        if (prodType.equals(TransactionFactory.ADVANCES)) {
                            mapNameForCalcInt = "IntCalculationDetailAD";
                        }
                        List intList = sqlMap.executeQueryForList(mapNameForCalcInt, interestMap);
                        if (intList != null && intList.size() > 0) {
                            getDateMap = (HashMap) intList.get(0);
                        }
                        getDateMap.put(CommonConstants.ACT_NUM, interestMap.get(CommonConstants.ACT_NUM));
                        getDateMap.put(PROD_ID, interestMap.get(PROD_ID));
                        getDateMap.put(BEHAVES_LIKE, behaves.get(BEHAVES_LIKE));

                    }
                    //end
                    if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && (getDateMap.get("AS_CUSTOMER_COMES").equals("Y") || getDateMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                        List lst = null;
                        if (list != null && list.size() > 0) {
                            if (prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.GL)) {
                                lst = (List) sqlMap.executeQueryForList("getIntDetails", interestMap);
                                //                            prodType=TransactionFactory.LOANS;
                            }
                            if (prodType.equals(TransactionFactory.ADVANCES) || prodType.equals(TransactionFactory.GL)) {
                                if (lst == null || lst.isEmpty()) {
                                    lst = sqlMap.executeQueryForList("getIntDetailsAD", interestMap);
                                }
                                //                            prodType=TransactionFactory.ADVANCES;
                            }
                            if (lst != null && lst.size() > 0) {
                                interestMap = (HashMap) lst.get(0);
                                insetMap.put("ACCOUNTNO", acctNum);
                                insetMap.put("TRANSTYPE", TransactionDAOConstants.CREDIT);
                                insetMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                transSlNo = CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                            }
                            if (transferTo.getProdType().equals(TransactionFactory.LOANS) || transferTo.getProdType().equals(TransactionFactory.ADVANCES) || transferTo.getProdType().equals(TransactionFactory.GL)) {
                                //EMI ONLY UPDATE STATUS
                                if (transferTo.getAuthorizeRemarks() != null && (CommonUtil.convertObjToLong(transferTo.getAuthorizeRemarks())) > 0) {
                                    HashMap objMap = new HashMap();
                                    long count = CommonUtil.convertObjToLong(transferTo.getAuthorizeRemarks());
                                    objMap.put(CommonConstants.ACT_NUM, transferTo.getLinkBatchId());
                                    List installmentLst = sqlMap.executeQueryForList("getMinimaminstallmentTL", objMap);
                                    if (installmentLst != null && installmentLst.size() > 0) {
                                        objMap = (HashMap) installmentLst.get(0);
                                        int instalmentNo = CommonUtil.convertObjToInt(objMap.get("INSTALLMENT_SLNO"));
                                        HashMap updateMap = new HashMap();
                                        for (int i = 0; i < count; i++) {
                                            updateMap = new HashMap();
                                            updateMap.put("ACCT_NUM", transferTo.getLinkBatchId());
                                            updateMap.put("INSTALLMENT_NO", new Integer(instalmentNo));
                                            System.out.println("updateMap###" + updateMap);
                                            sqlMap.executeUpdate("updateloanInstallment", updateMap);
                                            instalmentNo++;
                                        }
                                        sqlMap.executeUpdate("updateEMIlastIntCalc", updateMap);
                                        updateMap = null;
                                    }
                                    objMap = null;
                                }
                                //END

                                //shift trans details to roolback trans details
                                transDetailsMap.put("ACCOUNTNO", transferTo.getLinkBatchId());
                                transDetailsMap.put("TRANS_ID", String.valueOf(transferTo.getBatchId() + "_" + transferTo.getTransId()));
                                List transListTL = null;
                                if (transferTo.getProdType().equals(TransactionFactory.ADVANCES) || transferTo.getProdType().equals(TransactionFactory.GL)) {
                                    transListTL = (List) sqlMap.executeQueryForList("selectTransDetailsAD", transDetailsMap);
                                }
                                if (transferTo.getProdType().equals(TransactionFactory.LOANS) || transferTo.getProdType().equals(TransactionFactory.GL)) {
                                    if (transListTL == null || transListTL.isEmpty()) {
                                        transListTL = (List) sqlMap.executeQueryForList("selectTransDetailsTL", transDetailsMap);
                                    }
                                } 
                                if (transListTL != null && transListTL.size() > 0) {
                                    for (int i = 0; i < transListTL.size(); i++) {
                                        insertMap = (HashMap) transListTL.get(i);
                                        System.out.println(insertMap + "transDetailsMap####" + transDetailsMap);
                                        insertMap.put("ACCOUNTNO", transDetailsMap.get("ACCOUNTNO"));
                                        if (CommonUtil.convertObjToStr(insertMap.get("TRANS_ID")).equals(String.valueOf(transferTo.getBatchId() + "_" + transferTo.getTransId()))) {
                                            insertMap.put("ROLL_BACK_ID", rollBackID);
                                            System.out.println("insertMap####" + insertMap);
                                            // insertMap.put("PBAL",CommonUtil.convertObjToDouble(insertMap.get("PBAL")));
                                            sqlMap.executeUpdate("insertLoanRollBackTransactionDetails", insertMap);
                                            HashMap chargeTypeMap = checkChargeType(insertMap);
                                            if (chargeTypeMap != null && chargeTypeMap.size() > 0) {
                                                transDetailsMap.put("ACT_NUM", transDetailsMap.get("ACCOUNTNO"));
                                                transDetailsMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                                transDetailsMap.put("CHARGE_AMT", chargeTypeMap.get("CHARGE_AMT"));
                                                double totChargeAmt = CommonUtil.convertObjToDouble(chargeTypeMap.get("CHARGE_AMT")).doubleValue();
                                                List chargeList = (List) sqlMap.executeQueryForList("getTermLoanChargePaidAmount", transDetailsMap);
                                                if (chargeList != null && chargeList.size() > 0) {
                                                    for (int k = 0; k < chargeList.size(); k++) {
                                                        HashMap chargeMap = (HashMap) chargeList.get(k);
                                                        double paidAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                                                        if (paidAmt >= totChargeAmt) {
                                                            paidAmt = totChargeAmt;
                                                            totChargeAmt = 0;
                                                        } else {
                                                            totChargeAmt -= paidAmt;
                                                        }
                                                        insertMap.put("ACT_NUM", transDetailsMap.get("ACT_NUM"));
                                                        //                                                        insertMap.put("PAID_AMT",cashTo.getAmount());
                                                        insertMap.put("CHARGE_NO", chargeMap.get("CHARGE_NO"));
                                                        //Added By Suresh
                                                        insertMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                                        insertMap.put("PAID_AMT", new Double(paidAmt));
                                                        sqlMap.executeUpdate("updateChargeRollBackDetails", insertMap);
                                                    }
                                                }
                                            }
                                            System.out.println("insertMap####" + insertMap);
                                            //                                        sqlMap.executeUpdate("updateclearBal", insertMap);
                                        }
                                    }
                                    //Added By Suresh
                                    HashMap lastIntCalcMap = new HashMap();
                                    lastIntCalcMap.put("ACT_NUM", transferTo.getLinkBatchId());
                                    lastIntCalcMap.put("TRANS_DT", currDt);
                                    Date lastIntCalDt = null;
                                    Date lastCalcDt = null;//added line by Ajay Sharma for Mantis ID 9086
                                    List maxTransDtList = null;
                                    //modified by rishad for loan rollback issue
                                    lastIntCalcMap.put("ACCT_NUM", transferTo.getLinkBatchId());
                                    maxTransDtList = (List) sqlMap.executeQueryForList("getLoanacctOpenDt", lastIntCalcMap);
                                    if (maxTransDtList != null && maxTransDtList.size() > 0) {
                                        lastIntCalcMap = (HashMap) maxTransDtList.get(0);
                                        //below added line by Ajay Sharma for Mantis ID 9086 
                                        //as last Int calculation date was going wrong dated 15-May-2014
                                        lastCalcDt = (Date) lastIntCalcMap.get("ACCT_OPEN_DT");
                                        lastIntCalDt = (Date) lastIntCalcMap.get("TEMP_LAST_INT_CALC_DT");
                                    }
                                    lastIntCalDt = CommonUtil.getProperDate(currDt, lastIntCalDt);
                                    lastIntCalcMap.put("ACCOUNTNO", transferTo.getLinkBatchId());
                                    lastIntCalcMap.put("LAST_CALC_DT", lastIntCalDt);
                                    lastIntCalcMap.put("LAST_INT_CALC_DT", lastIntCalDt);
                                    lastIntCalcMap.put("ACT_NUM", transferTo.getLinkBatchId());
                                    sqlMap.executeUpdate("updateclearBal", lastIntCalcMap);
                                    sqlMap.executeUpdate("deleteLoanInterest", lastIntCalcMap);
                                }
                                transMap.put("BATCHID", transferTo.getBatchId());
                                transDetailsMap.put("TRANS_DT", currDt.clone());
                                if (screenName.equalsIgnoreCase("Gold Loan Account Renewal")) {
                                    transDetailsMap.put("ACCOUNTNO", transferTo.getGlTransActNum());
                                }
                                if (transferTo.getProdType().equals(TransactionFactory.LOANS) || transferTo.getProdType().equals(TransactionFactory.GL)) {
                                    sqlMap.executeUpdate("deleteTransDetailsTL", transDetailsMap);
                                }
                                if (transferTo.getProdType().equals(TransactionFactory.ADVANCES) || transferTo.getProdType().equals(TransactionFactory.GL)) {
                                    sqlMap.executeUpdate("deleteTransDetailsAD", transDetailsMap);
                                }
                            }
                            //Commented By Suresh
                            //                        sqlMap.executeUpdate("updateinterestYes",insetMap);
                            interestMap = null;
                        }
                        lst = null;
                    }
                }
                interestMap = null;
                if (transList != null && transList.size() > 0 && !oldTransId.equals(newTransId)) {
                    if (map != null && map.containsKey("TANS_LIST_DATA")) {
                        transMap.put("INITIATED_BRANCH", map.get("BRANCH_ID"));
                        transMap.put("TRANS_DT", map.get("TRANS_DT"));
                    }
                    transMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                    transMap.put("BATCHID", transferTo.getBatchId());
                    sqlMap.executeUpdate("rollbackTransferTransactionTO", transMap);
                }
                if (screenName != null && !screenName.equals("") && screenName.equalsIgnoreCase("DepositInterestApp")) {
                    map.put("ACT_NUM", map.get("ACCOUNT NO"));
                    map.put("CURR_DATE", currDt);
                    map.put("TRANS_ID", transferTo.getBatchId());
                    sqlMap.executeUpdate("deleteDepositInterestWithTransId", map);
                }
                //added by rishad 28/05/2020
                if (screenName != null && !screenName.equals("") && (screenName.equalsIgnoreCase("Deposit Closing/Transfer to Matured Deposit") || screenName.equalsIgnoreCase("Deposit Account Closing"))) {
                    map.put("ACT_NUM", transferTo.getLinkBatchId());
                    map.put("CURR_DATE", currDt);
                    map.put("TRANS_ID", transferTo.getBatchId());
                    sqlMap.executeUpdate("deleteDepositInterestWithTransId", map);
                }
                 //added by rishad 09/02/2022 for group deposit
            if (transferTo.getProdType().equals(TransactionFactory.DEPOSITS) && isDailyDeposit(transferTo.getActNum())) {
                HashMap checkMap = new HashMap();
                checkMap.put(PROD_ID, transferTo.getProdId());
                List groupDepositProdList = sqlMap.executeQueryForList("getDailyDepositTransEntryRequiredForProd", checkMap);
                if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
                    HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
                    if (groupDepositProdMap != null && groupDepositProdMap.containsKey("DD_TRANS_ENTRY") && groupDepositProdMap.get("DD_TRANS_ENTRY") != null) {
                        if (CommonUtil.convertObjToStr(groupDepositProdMap.get("DD_TRANS_ENTRY")).equalsIgnoreCase("Y")) {
                            HashMap authMap = new HashMap();
                            authMap.put("BATCH_ID", transferTo.getBatchId());
                            authMap.put("ACC_NUM", transferTo.getActNum());
                            authMap.put("TRANS_DT", currDt.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            authMap.put("AUTHORIZE_BY", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            authMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                            sqlMap.executeUpdate("RollBackDailyDeposit", authMap);
                        }

                        }
                    }
                }
            
                String accountNum = "";
                HashMap countMap = new HashMap();
                List countList = new ArrayList();
                boolean recurring = false;
                if (transferTo.getProdType().equals(TransactionFactory.DEPOSITS)
                        && transferTo.getLinkBatchId() != null && !transferTo.getLinkBatchId().equals("")) {
                    int index = CommonUtil.convertObjToStr(transferTo.getLinkBatchId()).indexOf("_");
                    accountNum = CommonUtil.convertObjToStr(transferTo.getLinkBatchId()).substring(0, index);
                    countMap = new HashMap();
                    countMap.put("TRANS_DT", currDt);
                    countMap.put("DEPOSIT_NO_SUB", CommonUtil.convertObjToStr(transferTo.getLinkBatchId()));
                    countList = sqlMap.executeQueryForList("getCurrDepositReccCount", countMap);
                    recurring = getRDAcct(accountNum);
                }
                if (recurring == true) {
                    recurring = false;
                    HashMap acctMap = new HashMap();
                    acctMap.put("TRANS_DT", currDt);
                    acctMap.put("DEPOSIT_NO", accountNum);
                    acctMap.put("DEPOSIT_NO_SUB", CommonUtil.convertObjToStr(transferTo.getLinkBatchId()));
                    int count = 0;
                    acctMap.put("COUNT", count);
                    if (countList != null && countList.size() > 0) {
                        countMap = (HashMap) countList.get(0);
                        if (countMap != null && countMap.size() > 0 && countMap.containsKey("COUNT_NO")) {
                            count = CommonUtil.convertObjToInt(countMap.get("COUNT_NO"));
                            acctMap.put("COUNT", count);
                        }
                    }
                    sqlMap.executeUpdate("updateDepositTransDateAmountNull", acctMap);
                    acctMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(transferTo.getLinkBatchId()));
                    List lstDep = sqlMap.executeQueryForList("getPenalAmtForReverse", acctMap);
                    if (lstDep != null && lstDep.size() > 0) {
                        HashMap PenalMapReverse = (HashMap) lstDep.get(0);
                        acctMap.put("DELAYED_MONTH", PenalMapReverse.get("DELAYED_MONTH"));
                        acctMap.put("DELAYED_AMOUNT", PenalMapReverse.get("DELAYED_AMOUNT"));
                        sqlMap.executeUpdate("UpdatePenalAmtAfterReverse", acctMap);
                    }
                }

                if (transferTo != null) {
                    insertDayEndBalanceTransfer(transferTo);
                }
                
                if (j == 0 && transferTo.getParticulars().indexOf("WaiveOff") != -1) {
                    System.out.println("Execute waive Off rejection");
                    HashMap waiveMap = new HashMap();
                    waiveMap.put("ACCT_NUM",transferTo.getLinkBatchId());
                    waiveMap.put("WAIVE_OFF_DT",currDt.clone());
                    waiveMap.put("REMARKS",transferTo.getInstrumentNo2());
                    List rejectWaiveOffList = (List) sqlMap.executeQueryForList("getSelectTermLoanAuthorizedWaivingOffTO", waiveMap);
                    if (rejectWaiveOffList != null && rejectWaiveOffList.size() > 0) {
                        for (int i = 0; i < rejectWaiveOffList.size(); i++) {
                            System.out.println("enterd inside rejectWaiveOffList rollback");
                            TermLoanPenalWaiveOffTO obj = new TermLoanPenalWaiveOffTO();
                            obj = (TermLoanPenalWaiveOffTO) rejectWaiveOffList.get(i);
                            obj.setAuthorizeStatus(CommonConstants.STATUS_REJECTED);
                            obj.setAuthorizedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            obj.setAuthorizedDt(currDt);
                            sqlMap.executeUpdate("updateTermLoanWaiveOffTO", obj);
                        }
                    }
                }               
                
            }
            if (map.containsKey("SERVICE_TAX_ROLLBACK") && transMap != null && transMap.size() > 0 && transMap.containsKey("BATCHID")) {
                HashMap whrMap = new HashMap();
                whrMap.put("USER_ID", transMap.get("USER_ID"));
                whrMap.put("TRANS_ID", transMap.get("BATCHID"));
                whrMap.put("TRANS_DT", transMap.get("TRANS_DT"));
                sqlMap.executeUpdate("rollbackServiceTaxDetails", whrMap);
            }
            //added by rishad for updating payroll table in case of payroll individula roll back through transfer ---06/feb/2018 --jirra id KDSA-259
            if (screenName != null && !screenName.equals("") &&  screenName.equalsIgnoreCase("Payroll Individual")) {
                HashMap whereMap = new HashMap();
                whereMap.put("USER_ID", transMap.get("USER_ID"));
                whereMap.put("TRANS_ID", transMap.get("BATCHID"));
                whereMap.put("TRANS_DT", transMap.get("TRANS_DT"));
                sqlMap.executeUpdate("updateRollBackPayrollData", whereMap);
            }
            
             if (screenName != null && !screenName.equals("") &&  screenName.equalsIgnoreCase("Standing Instruction")) {
                HashMap whereMap = new HashMap();
                whereMap.put("SI_ID", transAllId);
                whereMap.put("CURR_DT",DateUtil.addDays(currDt, -1));
                sqlMap.executeUpdate("updateStandingInstrRollback", whereMap);
            }
             //added by sreekrishnan for transfer transaction
            if ((transferTo.getProdType().equals(TransactionFactory.OTHERBANKACTS))) {
                List ABlist = null;
                int ABAmount = 0;
                String ABType = null;
                HashMap abmap = new HashMap();
                abmap.put("TRANS_DT", currDt);
                abmap.put("BATCH_ID", transferTo.getBatchId());
                ABlist = sqlMap.executeQueryForList("getABTransferTransDetails", abmap);
                if (ABlist != null && ABlist.size() > 0) {
                    HashMap ABData = (HashMap) ABlist.get(0);
                    //ABAmount=CommonUtil.convertObjToInt(ABData.get("AMOUNT"));
                    //ABType=CommonUtil.convertObjToStr(ABData.get("TRANS_TYPE"));
                    if (transferTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                        //ABAmount = -1 * cashTo.getAmount();
                        ABAmount = -1 * CommonUtil.convertObjToInt(ABData.get("AMOUNT"));
                    } else {
                        //ABAmount = cashTo.getAmount();
                        ABAmount = CommonUtil.convertObjToInt(ABData.get("AMOUNT"));
                    }
                    HashMap upAb = new HashMap();
                    upAb.put("AMOUNT", ABAmount);
                    upAb.put("ACT_MASTER_ID", ABData.get("ACT_MASTER_ID"));
                    sqlMap.executeUpdate("updateABMaster", upAb);
                    //sqlMap.executeUpdate("deleteMaster",upAb);
                }
            }
        }
        transList = null;
        transMap = null;
        insetMap = null;
        transferTo = null;
        return rollBackMap;
    }
    
    private boolean getRDAcct(String accNo) {
        boolean reccurring = false;
        HashMap acctMap = new HashMap();
        String behavesLike = "";
        try {
            acctMap.put("DEPOSIT_NO", accNo);
            List resultList = sqlMap.executeQueryForList("getSelectRecalculateDetails", acctMap);
            if(resultList!=null && resultList.size()>0){
                acctMap = (HashMap) resultList.get(0);
                if(acctMap != null && acctMap.size()>0 && acctMap.containsKey("BEHAVES_LIKE")){
                    behavesLike = CommonUtil.convertObjToStr(acctMap.get("BEHAVES_LIKE"));
                    if (behavesLike.equals("RECURRING")) {
                        reccurring = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reccurring;
    }

    private HashMap doCashRollBack(HashMap map) throws Exception {
        String prodType = "";
        HashMap rollbackMap = new HashMap();
        String acctNum = "";
        long transSlNo = 0;
        Double amount = null;
        HashMap insetMap = new HashMap();
        HashMap insertMap = new HashMap();
        HashMap transDetailsMap = new HashMap();
        LogTO objLogTO = new LogTO();
        objLogTO.setBranchId(_branchCode);
        CashTransactionTO cashTo = null;
        System.out.println("###### CashRollBackMap:" + map);
        if(map.containsKey("MDS_DETAILS")&&map.get("MDS_DETAILS") != null ){
        rollbackMap = (HashMap)map.get("MDS_DETAILS");}
        HashMap transMap = new HashMap();
        if (map.containsKey("LINK_BATCH_ID") && map.get("LINK_BATCH_ID") != null) {
            transMap.put("LINK_BATCH_ID", map.get("LINK_BATCH_ID"));
            transMap.put("LINK_BATCH", map.get("LINK_BATCH"));
            transMap.put("ENTERED_AMOUNT", map.get("ENTERED_AMOUNT"));
        } else {
            transMap.put("TRANS_ID", map.get("TRANS_ID"));
        }
        HashMap insDateMap = new HashMap();
        HashMap noInsMap = new HashMap();
        int noOfInsPaid = 0;
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(rollbackMap.get("SUB_NO")));
        insDateMap.put("SUB_NO", CommonUtil.convertObjToInt(rollbackMap.get("SUB_NO")));
        insDateMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(rollbackMap.get("SCHEME_NAME")));
        insDateMap.put("CURR_DATE", currDt);
        insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
        insDateMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(rollbackMap.get("CHITTAL_NO")));
        List noOfInstPaid = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", insDateMap);
        if(noOfInstPaid != null && noOfInstPaid.size() > 0){
            noInsMap = (HashMap)noOfInstPaid.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(noInsMap.get("NO_OF_INST"));
        }
        List noOfInst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", insDateMap); 
        if(noOfInst != null && noOfInst.size() > 0){
        HashMap noOFinstMap = (HashMap)noOfInst.get(0);
        int noOfinstallment = CommonUtil.convertObjToInt(noOFinstMap.get("NO_OF_INSTALLMENTS"));
        System.out.println("noOFinstMap"+noOfinstallment);
        List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
        System.out.println("My Mappppssss"+insDateLst);
        System.out.println("no of isnt paid "+noOfInsPaid);
        if(noOfinstallment == noOfInsPaid){
            HashMap unMarkleanMap = new HashMap();
            unMarkleanMap.put("SUB_NO", CommonUtil.convertObjToInt(rollbackMap.get("SUB_NO")));
            HashMap availClearMap = new HashMap();
            unMarkleanMap.put("LIEN_ACCT_NO", CommonUtil.convertObjToStr(rollbackMap.get("CHITTAL_NO")));
            List lienAmountList = sqlMap.executeQueryForList("selectLienAmountInDepositLien", unMarkleanMap);
            double clearBal = 0;
            double availBal = 0;
                if (lienAmountList != null && lienAmountList.size() > 0) {
                double lienAmount = 0;
                HashMap lienAmountMap = (HashMap) lienAmountList.get(0);
                if(lienAmountMap.containsKey("LIENAMOUNT") && lienAmountMap.get("LIENAMOUNT") != null){
                lienAmount = CommonUtil.convertObjToDouble(lienAmountMap.get("LIENAMOUNT"));
                }
                String depositNo = CommonUtil.convertObjToStr(lienAmountMap.get("DEPOSIT_NO"));
                unMarkleanMap.put("DEPOSIT_NO", depositNo);
                List clrAvailBalList = sqlMap.executeQueryForList("getClearAvailbalance", unMarkleanMap);
                if (clrAvailBalList != null && clrAvailBalList.size() > 0) {
                    availClearMap = (HashMap) clrAvailBalList.get(0);
                    if(availClearMap.containsKey("CLEAR_BALANCE") && availClearMap.containsKey("AVAILABLE_BALANCE") && availClearMap.get("CLEAR_BALANCE") != null && availClearMap.get("AVAILABLE_BALANCE") != null){
                    clearBal = CommonUtil.convertObjToDouble(availClearMap.get("CLEAR_BALANCE"));
                    availBal = CommonUtil.convertObjToDouble(availClearMap.get("AVAILABLE_BALANCE"));
                    availBal -= lienAmount;
                    }
//                  availBal = lienAmount; UpdateLienAmountInDeposit 
                    System.out.println("avialable balance " + availBal);
                }
                sqlMap.executeUpdate("getClearAvailbalance", unMarkleanMap);
                unMarkleanMap.put("REMARK_STATUS", "Lien From MDS");//
                unMarkleanMap.put("STATUS", "CREATED");//
                sqlMap.executeUpdate("UpdateRemarksInDepositLien", unMarkleanMap);
                unMarkleanMap.put("LIENAMOUNT", availBal);
                sqlMap.executeUpdate("UpdateLienAmountInDeposit", unMarkleanMap);
                unMarkleanMap.put("LIEN_STATUS", "LIEN");
                sqlMap.executeUpdate("UpadteLienForMDSDepositType", unMarkleanMap);
                //update available balance to deposit sub ac info and mark as LIEN
                // update deposit Lein and mds deposit type tables
            }
        }
        }
        transMap.put("TRANS_DT", currDt);
        transMap.put("INITIATED_BRANCH", _branchCode);
        List cashList = sqlMap.executeQueryForList("getSelectCashTransactionTOForRollBack", transMap);
        String cashAcctHd = getCashAcctHead();
        String oldTransId = "";
        String newTransId = "";
        String screenName="";
        for (int j = 0; j < cashList.size(); j++) {
            cashTo = new CashTransactionTO();
            cashTo = (CashTransactionTO) cashList.get(j);
            if (j == 0) {
                oldTransId = cashTo.getTransId();
                screenName=cashTo.getScreenName();
            } else {
                newTransId = cashTo.getTransId();
            }
            HashMap interestMap = new HashMap();
            HashMap getDateMap = new HashMap();
            System.out.println("cashTo####" + cashTo);
            HashMap ruleMap = getRuleMap(cashTo);
            if (cashTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                amount = new Double(-1 * cashTo.getAmount().doubleValue());
            } else {
                amount = cashTo.getAmount();
            }

            if (cashTo != null && !cashTo.getBranchId().equals(cashTo.getInitiatedBranch())) {
                HashMap transRefMap = new HashMap();
                transRefMap.put("TRANS_ID", cashTo.getTransId());
                transRefMap.put("STATUS", CommonConstants.STATUS_DELETED);
                transRefMap.put("INITIATED_BRANCH", cashTo.getInitiatedBranch());
                transRefMap.put("TRANS_DT", cashTo.getTransDt());
                System.out.println("transRefMap ### : " + transRefMap);
                sqlMap.executeUpdate("updateTransRefGLStatus", transRefMap);
            }
            //Added By Suresh
            if (cashTo.getProdType().equals(TransactionFactory.OPERATIVE) && cashTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                HashMap inputMap = new HashMap();
                inputMap.put("ACCOUNTNO", cashTo.getActNum());
                List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
                HashMap resultMap = (HashMap) list.get(0);
                double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
                double limit = CommonUtil.convertObjToDouble(resultMap.get("TOD_LIMIT")).doubleValue();
                System.out.println("RollbackAmount#######" + cashTo.getAmount().doubleValue());
                System.out.println("availableBalance#######" + availableBalance);
                if ((availableBalance + limit) < cashTo.getAmount().doubleValue()) {
                    rollBackMap = new HashMap();
                    System.out.println("INSUFFICIENT_BALANCE#######");
                    rollBackMap.put("INSUFFICIENT_BALANCE", "INSUFFICIENT_BALANCE");
                    commitFlag = false;
                    sqlMap.rollbackTransaction();
                  // added by rishad 07/mar/2018 for throwing IB Exception
                    throw new ValidationRuleException(TransactionConstants.INSUFFICIENT_BALANCE);
                  //  return rollBackMap;
                }
            }
            transModuleBased = TransactionFactory.createTransaction(cashTo.getProdType());
            ruleMap.put("INTER_BRANCH_ROLL_BACK_TRANSACTION","INTER_BRANCH_ROLL_BACK_TRANSACTION");
            transModuleBased.updateAvailableBalance(ruleMap);
            transModuleBased.performOtherBalanceAdd(ruleMap);
            transModuleBased.updateGL(cashTo.getAcHdId(), amount, objLogTO, ruleMap);
            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, cashTo.getTransType());
            ruleMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.GL);

            if (ruleMap.containsKey("INTER_BRANCH_TRANS")
                    && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("INTER_BRANCH_TRANS"))).booleanValue() == true) {
                //transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap);                
            } else {
                transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap, true);
            }
            updatepassbook(cashTo, ruleMap);
            String accountNum = "";
            HashMap countMap = new HashMap();
            List countList = new ArrayList();
            boolean recurring = false;
            if (cashTo.getProdType().equals(TransactionFactory.DEPOSITS) &&
                cashTo.getLinkBatchId() != null && !cashTo.getLinkBatchId().equals("")) {
                int index = CommonUtil.convertObjToStr(cashTo.getLinkBatchId()).indexOf("_");
                accountNum = CommonUtil.convertObjToStr(cashTo.getLinkBatchId()).substring(0, index);
                countMap = new HashMap();
                countMap.put("TRANS_DT", currDt);
                countMap.put("DEPOSIT_NO_SUB", CommonUtil.convertObjToStr(cashTo.getLinkBatchId()));
                countList = sqlMap.executeQueryForList("getCurrDepositReccCount", countMap);
                recurring = getRDAcct(accountNum);
            }
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Cash Transactions") && recurring==false) {
              //commented by rishad 09/07/2015 because of wrong updation in deposit_sub_acc_info refered by rajesh
//                Date lastIntCalDt1 = null;
//                Date nextIntCalDt1 = null;
//                Date lastIntCalDtNew = null;
//                Date nextIntCalDtNew = null;
//                Date depDt = null;
//                double intAmt;
//                int frq = 0;
//                List plist = null;
//                if (acctNum != null && acctNum.length() > 0) {
//                    interestMap.put(CommonConstants.ACT_NUM, acctNum);
//                } else {
//                    interestMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
//                    acctNum = (String) cashTo.getLinkBatchId();
//                }
//                HashMap prodId = new HashMap();
//                prodId.put("DEPOSIT_NO", acctNum);
//                prodId.put("TRANS_ID", cashTo.getTransId());
//                prodId.put("TRANS_DT", currDt);
//                if (acctNum != null) {
//                    plist = sqlMap.executeQueryForList("getProd", prodId);
//                    if (plist != null && plist.size() > 0) {
//                        HashMap prd = (HashMap) plist.get(0);
//                        System.out.println("@@@@@@@@@@@@@@@" + prd);
//                        List dlist = null;
//                        DateFormat formatter;
//                        Date currentdate;
//                        formatter = new SimpleDateFormat("dd/MM/yyyy");
//                        if (prd.containsKey("FRM") && prd.get("FRM") != null && !prd.get("FRM").equals("null") && !prd.get("FRM").equals("")) {
//                            lastIntCalDt1 = formatter.parse(prd.get("FRM").toString());
//                        }
//                        HashMap map1 = new HashMap();
//                        map1.put("DEPOSIT_NO", acctNum);
//                        if (acctNum != null) {
//                            dlist = sqlMap.executeQueryForList("getDepIntFrq", map1);
//                            if (dlist != null && dlist.size() > 0) {
//                                HashMap intfrq = (HashMap) dlist.get(0);
//                                frq = CommonUtil.convertObjToInt(intfrq.get("INTPAY_FREQ"));
//                                depDt = (Date) intfrq.get("DEPOSIT_DT");
//                                if (lastIntCalDt1 != null && depDt.compareTo(lastIntCalDt1) == 0) {
//                                    lastIntCalDtNew = null;
//                                    nextIntCalDtNew = null;
//                                } else {
//                                    if (lastIntCalDt1 != null) {
//                                        lastIntCalDtNew = lastIntCalDt1;
//                                        nextIntCalDtNew = DateUtil.addDays(lastIntCalDtNew, frq);
//                                    }
//                                }
//                                HashMap update = new HashMap();
//                                update.put("DEPOSIT_NO", acctNum);
//                                update.put("AMOUNT", amount);
//                                update.put("LAST_INT_APPL_DT", lastIntCalDtNew);
//                                update.put("NEXT_INT_APPL_DT", nextIntCalDtNew);
//                                sqlMap.executeUpdate("updateDepositSubInfo", update);
//                            }
//                        }
//                    }
//                }
     //end rishad
            }else{
                recurring = false;
                HashMap acctMap = new HashMap();
                acctMap.put("TRANS_DT", currDt);
                acctMap.put("DEPOSIT_NO",accountNum);
                acctMap.put("DEPOSIT_NO_SUB", CommonUtil.convertObjToStr(cashTo.getLinkBatchId()));
                int count = 0;
                acctMap.put("COUNT", count);
                if(countList!=null && countList.size()>0){
                    countMap = (HashMap) countList.get(0);
                    if(countMap!=null && countMap.size()>0 && countMap.containsKey("COUNT_NO")){
                        count = CommonUtil.convertObjToInt(countMap.get("COUNT_NO"));
                        acctMap.put("COUNT", count);
                    }
                }
                sqlMap.executeUpdate("updateDepositTransDateAmountNull", acctMap);
                //sqlMap.executeUpdate("updateDepositBalance", acctMap);
                acctMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(cashTo.getLinkBatchId()));
                List lstDep = sqlMap.executeQueryForList("getPenalAmtForReverse", acctMap);
                if (lstDep != null && lstDep.size() > 0) {
                    HashMap PenalMapReverse = (HashMap) lstDep.get(0);
                    acctMap.put("DELAYED_MONTH", PenalMapReverse.get("DELAYED_MONTH"));
                    acctMap.put("DELAYED_AMOUNT", PenalMapReverse.get("DELAYED_AMOUNT"));
                    sqlMap.executeUpdate("UpdatePenalAmtAfterReverse", acctMap);
                }

            }
            
            if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {

                // IN CASE OF LOANS UPDATE LOAN_TRANS_DETAILS TABLE USING FOLLOWING MAPPED STATEMENT
                // authorizeLoansDisbursementCumLoanDetails
                List list = null;
                if (acctNum != null && acctNum.length() > 0) {
                    interestMap.put(CommonConstants.ACT_NUM, acctNum);
                } else {
                    interestMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                    acctNum = (String) cashTo.getLinkBatchId();
                }
                HashMap lastCalDateMap = new HashMap();
                lastCalDateMap.put(TransactionDAOConstants.ACCT_NO, interestMap.get(CommonConstants.ACT_NUM));
                if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                    list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                }
                if (list != null && list.size() > 0) {
                    HashMap behaves = (HashMap) list.get(0);
                    prodType = CommonUtil.convertObjToStr(behaves.get(BEHAVES_LIKE));

                    interestMap.put(PROD_ID, behaves.get(PROD_ID));
                    prodType = prodType.equals("OD") ? TransactionFactory.ADVANCES : TransactionFactory.LOANS;
                    interestMap.put("TRANS_DT", currDt);
                    interestMap.put("INITIATED_BRANCH", _branchCode);
                    String mapNameForCalcInt = "IntCalculationDetail";
                    if (prodType.equals(TransactionFactory.ADVANCES)) {
                        mapNameForCalcInt = "IntCalculationDetailAD";
                    }
                    List intList = sqlMap.executeQueryForList(mapNameForCalcInt, interestMap);
                    if (intList != null && intList.size() > 0) {
                        getDateMap = (HashMap) intList.get(0);
                    }
                    getDateMap.put(CommonConstants.ACT_NUM, interestMap.get(CommonConstants.ACT_NUM));
                    getDateMap.put(PROD_ID, interestMap.get(PROD_ID));
                    getDateMap.put(BEHAVES_LIKE, behaves.get(BEHAVES_LIKE));

                }
                if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && (getDateMap.get("AS_CUSTOMER_COMES").equals("Y") || getDateMap.get("AS_CUSTOMER_COMES").equals("N"))) {

                    List lst = null;
                    if (list != null && list.size() > 0) {
                        if (prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.GL)) {
                            lst = (List) sqlMap.executeQueryForList("getIntDetails", interestMap);
                            //                            prodType=TransactionFactory.LOANS;
                        }
                        if (prodType.equals(TransactionFactory.ADVANCES) || prodType.equals(TransactionFactory.GL)) {
                            if (lst == null || lst.isEmpty()) {
                                lst = sqlMap.executeQueryForList("getIntDetailsAD", interestMap);
                            }
                            //                            prodType=TransactionFactory.ADVANCES;
                        }
                        if (lst != null && lst.size() > 0) {
                            interestMap = (HashMap) lst.get(0);
                            insetMap.put("ACCOUNTNO", acctNum);
                            insetMap.put("TRANSTYPE", TransactionDAOConstants.CREDIT);
                            insetMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            transSlNo = CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                        }
                        if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                            //EMI ONLY UPDATE STATUS
                            if (cashTo.getInstrumentNo1() != null && (CommonUtil.convertObjToLong(cashTo.getInstrumentNo1())) > 0) {
                                HashMap objMap = new HashMap();
                                long count = CommonUtil.convertObjToLong(cashTo.getInstrumentNo1());
//                                objMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
//                                List installmentLst = sqlMap.executeQueryForList("getMaximaminstallmentTL", objMap);
                                if (count > 0) {
                                    HashMap updateMap = new HashMap();
                                    for (int i = 0; i < count; i++) {
                                        objMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                                        List installmentLst = sqlMap.executeQueryForList("getMaximaminstallmentTL", objMap);
                                        if (installmentLst != null && installmentLst.size() > 0) {
                                            objMap = (HashMap) installmentLst.get(0);
                                            int instalmentNo = CommonUtil.convertObjToInt(objMap.get("INSTALLMENT_SLNO"));
                                            updateMap = new HashMap();
                                            updateMap.put("ACCT_NUM", cashTo.getLinkBatchId());
                                            updateMap.put("INSTALLMENT_NO", new Integer(instalmentNo));
                                            System.out.println("updateMap###" + updateMap);
                                            sqlMap.executeUpdate("updateloanInstallmentRollBack", updateMap);
                                            //instalmentNo++;
                                        }
                                    }
                                    sqlMap.executeUpdate("updateEMIlastIntCalc", updateMap);
                                    updateMap = null;
                                }
                                objMap = null;
                            }
                            //END

                            //shift trans details to roolback trans details
                            transDetailsMap.put("ACCOUNTNO", cashTo.getLinkBatchId());
                            transDetailsMap.put("TRANS_ID", cashTo.getTransId());
                            List transList = null;
                            if (cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                transList = (List) sqlMap.executeQueryForList("selectTransDetailsAD", transDetailsMap);
                            }
                            if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                if (transList == null || transList.isEmpty()) {
                                    transList = (List) sqlMap.executeQueryForList("selectTransDetailsTL", transDetailsMap);
                                }
                            }

                            if (transList != null && transList.size() > 0) {
                                for (int i = 0; i < transList.size(); i++) {
                                    insertMap = (HashMap) transList.get(i);
                                    System.out.println(insertMap + "transDetailsMap####" + transDetailsMap);
                                    insertMap.put("ACCOUNTNO", transDetailsMap.get("ACCOUNTNO"));
                                    if (CommonUtil.convertObjToStr(insertMap.get("TRANS_ID")).equals(cashTo.getTransId())) {
                                        insertMap.put("ROLL_BACK_ID", rollBackID);
                                        System.out.println("insertMap####" + insertMap);
                                        sqlMap.executeUpdate("insertLoanRollBackTransactionDetails", insertMap);
                                        HashMap chargeTypeMap = checkChargeType(insertMap);
                                        if (chargeTypeMap != null && chargeTypeMap.size() > 0) {
                                            transDetailsMap.put("ACT_NUM", transDetailsMap.get("ACCOUNTNO"));
                                            transDetailsMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                            double totChargeAmt = CommonUtil.convertObjToDouble(chargeTypeMap.get("CHARGE_AMT")).doubleValue();
                                            List chargeList = (List) sqlMap.executeQueryForList("getTermLoanChargePaidAmount", transDetailsMap);
                                            if (chargeList != null && chargeList.size() > 0) {
                                                for (int k = 0; k < chargeList.size(); k++) {
                                                    HashMap chargeMap = (HashMap) chargeList.get(k);
                                                    double paidAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                                                    if (paidAmt >= totChargeAmt) {
                                                        paidAmt = totChargeAmt;
                                                        totChargeAmt = 0;
                                                    } else {
                                                        totChargeAmt -= paidAmt;
                                                    }
                                                    insertMap.put("ACT_NUM", transDetailsMap.get("ACT_NUM"));
                                                    //                                                        insertMap.put("PAID_AMT",cashTo.getAmount());
                                                    insertMap.put("CHARGE_NO", chargeMap.get("CHARGE_NO"));
                                                    //Added By Suresh
                                                    insertMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                                    insertMap.put("PAID_AMT", new Double(paidAmt));
                                                    sqlMap.executeUpdate("updateChargeRollBackDetails", insertMap);
                                                }
                                            }
                                        }
                                        System.out.println("insertMap####" + insertMap);
                                        //                                        sqlMap.executeUpdate("updateclearBal", insertMap);
                                    }
                                }
                                //Added By Suresh
                                HashMap lastIntCalcMap = new HashMap();
                                lastIntCalcMap.put("ACT_NUM", cashTo.getLinkBatchId());
                                lastIntCalcMap.put("TRANS_DT", currDt);
                                Date lastIntCalDt = null;
                                Date lastCalcDt = null;
                                List maxTransDtList = null;
                                //modified by rishad due to loan rollback issue 22/05/2015
                                lastIntCalcMap.put("ACCT_NUM", cashTo.getLinkBatchId());
                                maxTransDtList = (List) sqlMap.executeQueryForList("getLoanacctOpenDt", lastIntCalcMap);
                                if (maxTransDtList != null && maxTransDtList.size() > 0) {
                                    lastIntCalcMap = (HashMap) maxTransDtList.get(0);
                                    //below added line by Ajay Sharma for Mantis ID 9086 
                                    //as last Int calculation date was going wrong dated 15-May-2014
                                    lastCalcDt = (Date) lastIntCalcMap.get("ACCT_OPEN_DT");
                                    lastIntCalDt = (Date) lastIntCalcMap.get("TEMP_LAST_INT_CALC_DT");
                                }
                                lastCalcDt = DateUtil.addDaysProperFormat(lastCalcDt, -1);
                                lastIntCalDt = CommonUtil.getProperDate(currDt, lastIntCalDt);
                                lastIntCalcMap.put("ACCOUNTNO", cashTo.getLinkBatchId());
                                lastIntCalcMap.put("LAST_CALC_DT", lastIntCalDt);
                                lastIntCalcMap.put("LAST_INT_CALC_DT", lastIntCalDt);
                                lastIntCalcMap.put("ACT_NUM", cashTo.getLinkBatchId());
                                System.out.println("lastIntCalcMap #######" + lastIntCalcMap);
                                if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && getDateMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                    //sqlMap.executeUpdate("updateclearBal", lastIntCalcMap);
                                   //added by rishad for mantis 0009851: Issues occured while loan Roll Back
                                     sqlMap.executeUpdate("updateLastCalcDate", lastIntCalcMap);
                                }
                                sqlMap.executeUpdate("deleteLoanInterest", lastIntCalcMap);

                            }
                            transMap.put("TRANS_ID", cashTo.getTransId());
                            transDetailsMap.put("TRANS_DT", currDt.clone());
                            if (screenName!= null && screenName.equalsIgnoreCase("Gold Loan Account Renewal")) {
                                transDetailsMap.put("ACCOUNTNO", cashTo.getGlTransActNum());
                            }
                            if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                sqlMap.executeUpdate("deleteTransDetailsTL", transDetailsMap);
                            }
                            if (cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                sqlMap.executeUpdate("deleteTransDetailsAD", transDetailsMap);
                            }
                        }
                        //Commented By Suresh
                        //                        sqlMap.executeUpdate("updateinterestYes",insetMap);
                        interestMap = null;
                    }
                    lst = null;
                }
            }
            interestMap = null;
            //added by rishad 08/07/2015
            HashMap transTempDtls = new HashMap();
            transTempDtls.put("DEPOSIT_NO", cashTo.getLinkBatchId());
            System.out.println("Value of transTempDtls : " + transTempDtls);
            List tempLst = sqlMap.executeQueryForList("getDepositAccountDetails", transTempDtls);
            if (tempLst != null && tempLst.size() > 0) {
                HashMap updateMap = (HashMap) tempLst.get(0);
                updateMap.put("DEPOSIT_NO", updateMap.get("DEPOSIT_NO"));
                updateMap.put("LAST_INT_APPL_DT", updateMap.get("TEMP_LAST_INT_APPL_DT"));
                updateMap.put("INT_CREDIT", updateMap.get("TEMP_INT_CREDIT"));
                updateMap.put("INT_DRAWN", updateMap.get("TEMP_INT_DRAWN"));
                updateMap.put("NEXT_INT_APPL_DT", updateMap.get("TEMP_NEXT_INT_APPL_DT"));
                System.out.println("Value of updateMap : " + updateMap);
                sqlMap.executeUpdate("updateRollBackDepositTempDate", updateMap);
            }
            //added by rishad 09/02/2022 for group deposit
            if (cashTo.getProdType().equals(TransactionFactory.DEPOSITS) && isDailyDeposit(cashTo.getActNum())) {
                HashMap checkMap = new HashMap();
                checkMap.put(PROD_ID, cashTo.getProdId());
                List groupDepositProdList = sqlMap.executeQueryForList("getDailyDepositTransEntryRequiredForProd", checkMap);
                if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
                    HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
                    if (groupDepositProdMap != null && groupDepositProdMap.containsKey("DD_TRANS_ENTRY") && groupDepositProdMap.get("DD_TRANS_ENTRY") != null) {
                        if (CommonUtil.convertObjToStr(groupDepositProdMap.get("DD_TRANS_ENTRY")).equalsIgnoreCase("Y")) {
                            HashMap authMap = new HashMap();
                            authMap.put("BATCH_ID", cashTo.getTransId());
                            authMap.put("ACC_NUM", cashTo.getActNum());
                            authMap.put("TRANS_DT", currDt.clone());
                            authMap.put("INITIATED_BRANCH", _branchCode);
                            authMap.put("AUTHORIZE_BY", CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                            authMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_REJECTED);
                            sqlMap.executeUpdate("RollBackDailyDeposit", authMap);
                        }

                        }
                    }
                }
            
            // added by rishad 08/07/2015 //modified at 28/05/2020 rishad
            if (screenName!=null && !screenName.equals("") && (screenName.equalsIgnoreCase("DepositInterestApp")||screenName.equalsIgnoreCase("Deposit Closing/Transfer to Matured Deposit")||screenName.equalsIgnoreCase("Deposit Account Closing"))) {
                map.put("ACT_NUM", cashTo.getLinkBatchId());
                map.put("CURR_DATE", currDt);
                map.put("TRANS_ID", cashTo.getTransId());
                sqlMap.executeUpdate("deleteDepositInterestWithTransId", map);
            }
            if (cashList != null && cashList.size() > 0 && !oldTransId.equals(newTransId)) {
                transMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                transMap.put("TRANS_ID", cashTo.getTransId());
                sqlMap.executeUpdate("rollbackCashTransactionTO", transMap);
            }
            //added by rishad for updating payroll table in case of payroll individula roll back through transfer ---06/feb/2018 --jirra id KDSA-259
            if (screenName != null && !screenName.equals("") && screenName.equalsIgnoreCase("Payroll Individual")) {
                HashMap whereMap = new HashMap();
                whereMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                whereMap.put("TRANS_ID", cashTo.getTransId());
                whereMap.put("TRANS_DT", currDt);
                sqlMap.executeUpdate("updateRollBackPayrollData", whereMap);
            }
            System.out.println("cashTo : "+cashTo);
            if(cashTo != null){
                insertDayEndBalanceCash(cashTo);
            }
        }
 
        if (map.containsKey("SERVICE_TAX_ROLLBACK") && transMap != null && transMap.size() > 0 && transMap.containsKey("TRANS_ID")) {
            sqlMap.executeUpdate("rollbackServiceTaxDetails", transMap);
        }
        cashList = null;
        transMap = null;
        insetMap = null;
        cashTo = null;
        return rollBackMap;
    }

    private HashMap doCashRollBackForTrade(HashMap map) throws Exception {
        String prodType = "";
        String acctNum = "";
        long transSlNo = 0;
        Double amount = null;
        HashMap insetMap = new HashMap();
        HashMap insertMap = new HashMap();
        HashMap transDetailsMap = new HashMap();
        LogTO objLogTO = new LogTO();
        objLogTO.setBranchId(_branchCode);
        CashTransactionTO cashTo = null;
        System.out.println("###### CashRollBackMap:" + map);
        HashMap transMap = new HashMap();
        if (map.containsKey("LINK_BATCH_ID") && map.get("LINK_BATCH_ID") != null) {
            transMap.put("LINK_BATCH_ID", map.get("LINK_BATCH_ID"));
            transMap.put("LINK_BATCH", map.get("LINK_BATCH"));
            transMap.put("ENTERED_AMOUNT", map.get("ENTERED_AMOUNT"));
        } else {
            transMap.put("TRANS_ID", map.get("TRANS_ID"));
        }
        HashMap insDateMap = new HashMap();
        insDateMap.put("CURR_DATE", currDt);
        insDateMap.put("ADD_MONTHS", "-1");
        transMap.put("TRANS_DT", currDt);
        transMap.put("TRANS_ID", map.get("LINK_BATCH_ID"));
        List cashList = sqlMap.executeQueryForList("getSelectCashTransactionTOForTradeRollBack", transMap);
        System.out.println("cashListcashListcashListcashList"+cashList);
        String cashAcctHd = getCashAcctHead();
        String oldTransId = "";
        String newTransId = "";
        for (int j = 0; j < cashList.size(); j++) {
            cashTo = new CashTransactionTO();
            cashTo = (CashTransactionTO) cashList.get(j);
            if (j == 0) {
                oldTransId = cashTo.getTransId();
            } else {
                newTransId = cashTo.getTransId();
            }
            HashMap interestMap = new HashMap();
            HashMap getDateMap = new HashMap();
            System.out.println("cashTo####" + cashTo);
            HashMap ruleMap = getRuleMap(cashTo);
            if (cashTo.getTransType().equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
                amount = new Double(-1 * cashTo.getAmount().doubleValue());
            } else {
                amount = cashTo.getAmount();
            }

            if (cashTo != null && !cashTo.getBranchId().equals(cashTo.getInitiatedBranch())) {
                HashMap transRefMap = new HashMap();
                transRefMap.put("TRANS_ID", cashTo.getTransId());
                transRefMap.put("STATUS", CommonConstants.STATUS_DELETED);
                transRefMap.put("INITIATED_BRANCH", cashTo.getInitiatedBranch());
                transRefMap.put("TRANS_DT", cashTo.getTransDt());
                System.out.println("transRefMap ### : " + transRefMap);
                sqlMap.executeUpdate("updateTransRefGLStatus", transRefMap);
            }
            //Added By Suresh
           
            transModuleBased = TransactionFactory.createTransaction(cashTo.getProdType());
            ruleMap.put("INTER_BRANCH_ROLL_BACK_TRANSACTION","INTER_BRANCH_ROLL_BACK_TRANSACTION");
            transModuleBased.updateAvailableBalance(ruleMap);
            transModuleBased.performOtherBalanceAdd(ruleMap);
            transModuleBased.updateGL(cashTo.getAcHdId(), amount, objLogTO, ruleMap);
            ruleMap.put(TransactionDAOConstants.TRANS_TYPE, cashTo.getTransType());
            ruleMap.put(TransactionDAOConstants.PROD_TYPE, TransactionFactory.GL);

            if (ruleMap.containsKey("INTER_BRANCH_TRANS")
                    && new Boolean(CommonUtil.convertObjToStr(ruleMap.get("INTER_BRANCH_TRANS"))).booleanValue() == true) {
                //transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap);                
            } else {
                transModuleBased.updateGL(cashAcctHd, amount, objLogTO, ruleMap, true);
            }
            updatepassbook(cashTo, ruleMap);
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Cash Transactions")) {
                Date lastIntCalDt1 = null;
                Date nextIntCalDt1 = null;
                Date lastIntCalDtNew = null;
                Date nextIntCalDtNew = null;
                Date depDt = null;
                double intAmt;
                int frq = 0;
                List plist = null;
                if (acctNum != null && acctNum.length() > 0) {
                    interestMap.put(CommonConstants.ACT_NUM, acctNum);
                } else {
                    interestMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                    acctNum = (String) cashTo.getLinkBatchId();
                }
                HashMap prodId = new HashMap();
                prodId.put("DEPOSIT_NO", acctNum);
                prodId.put("TRANS_ID", cashTo.getTransId());
                prodId.put("TRANS_DT", currDt);
                if (acctNum != null) {
                    plist = sqlMap.executeQueryForList("getProd", prodId);
                    if (plist != null && plist.size() > 0) {
                        HashMap prd = (HashMap) plist.get(0);
                        System.out.println("@@@@@@@@@@@@@@@" + prd);
                        List dlist = null;
                        DateFormat formatter;
                        Date currentdate;
                        formatter = new SimpleDateFormat("dd/MM/yyyy");
                        if (prd.containsKey("FRM") && prd.get("FRM") != null && !prd.get("FRM").equals("null")) {
                            lastIntCalDt1 = formatter.parse(prd.get("FRM").toString());
                        }
                        HashMap map1 = new HashMap();
                        map1.put("DEPOSIT_NO", acctNum);
                        if (acctNum != null) {
                            dlist = sqlMap.executeQueryForList("getDepIntFrq", map1);
                            if (dlist != null && dlist.size() > 0) {
                                HashMap intfrq = (HashMap) dlist.get(0);
                                frq = CommonUtil.convertObjToInt(intfrq.get("INTPAY_FREQ"));
                                depDt = (Date) intfrq.get("DEPOSIT_DT");
                                if (lastIntCalDt1 != null && depDt.compareTo(lastIntCalDt1) == 0) {
                                    lastIntCalDtNew = null;
                                    nextIntCalDtNew = null;
                                } else {
                                    if (lastIntCalDt1 != null) {
                                        lastIntCalDtNew = lastIntCalDt1;
                                        nextIntCalDtNew = DateUtil.addDays(lastIntCalDtNew, frq);
                                    }
                                }
                                HashMap update = new HashMap();
                                update.put("DEPOSIT_NO", acctNum);
                                update.put("AMOUNT", amount);
                                update.put("LAST_INT_APPL_DT", lastIntCalDtNew);
                                update.put("NEXT_INT_APPL_DT", nextIntCalDtNew);
                                sqlMap.executeUpdate("updateDepositSubInfo", update);
                            }
                        }
                    }
                }
            }
            if (cashTo.getProdType().equals(TransactionFactory.GL)) {
                // IN CASE OF LOANS UPDATE LOAN_TRANS_DETAILS TABLE USING FOLLOWING MAPPED STATEMENT
                // authorizeLoansDisbursementCumLoanDetails
                List list = null;
                if (acctNum != null && acctNum.length() > 0) {
                    interestMap.put(CommonConstants.ACT_NUM, acctNum);
                } else {
                    interestMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                    acctNum = (String) cashTo.getLinkBatchId();
                }
                HashMap lastCalDateMap = new HashMap();
                lastCalDateMap.put(TransactionDAOConstants.ACCT_NO, interestMap.get(CommonConstants.ACT_NUM));
                if (interestMap.get(CommonConstants.ACT_NUM) != null) {
                    list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
                }
                if (list != null && list.size() > 0) {
                    HashMap behaves = (HashMap) list.get(0);
                    prodType = CommonUtil.convertObjToStr(behaves.get(BEHAVES_LIKE));

                    interestMap.put(PROD_ID, behaves.get(PROD_ID));
                    prodType = prodType.equals("OD") ? TransactionFactory.ADVANCES : TransactionFactory.LOANS;
                    interestMap.put("TRANS_DT", currDt);
                    interestMap.put("INITIATED_BRANCH", _branchCode);
                    String mapNameForCalcInt = "IntCalculationDetail";
                    if (prodType.equals(TransactionFactory.ADVANCES)) {
                        mapNameForCalcInt = "IntCalculationDetailAD";
                    }
                    List intList = sqlMap.executeQueryForList(mapNameForCalcInt, interestMap);
                    if (intList != null && intList.size() > 0) {
                        getDateMap = (HashMap) intList.get(0);
                    }
                    getDateMap.put(CommonConstants.ACT_NUM, interestMap.get(CommonConstants.ACT_NUM));
                    getDateMap.put(PROD_ID, interestMap.get(PROD_ID));
                    getDateMap.put(BEHAVES_LIKE, behaves.get(BEHAVES_LIKE));
                }
                if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && (getDateMap.get("AS_CUSTOMER_COMES").equals("Y") || getDateMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                    List lst = null;
                    if (list != null && list.size() > 0) {
                        if (prodType.equals(TransactionFactory.LOANS) || prodType.equals(TransactionFactory.GL)) {
                            lst = (List) sqlMap.executeQueryForList("getIntDetails", interestMap);
                            //                            prodType=TransactionFactory.LOANS;
                        }
                        if (prodType.equals(TransactionFactory.ADVANCES) || prodType.equals(TransactionFactory.GL)) {
                            if (lst == null || lst.isEmpty()) {
                                lst = sqlMap.executeQueryForList("getIntDetailsAD", interestMap);
                            }
                            //                            prodType=TransactionFactory.ADVANCES;
                        }
                        if (lst != null && lst.size() > 0) {
                            interestMap = (HashMap) lst.get(0);
                            insetMap.put("ACCOUNTNO", acctNum);
                            insetMap.put("TRANSTYPE", TransactionDAOConstants.CREDIT);
                            insetMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            transSlNo = CommonUtil.convertObjToLong(interestMap.get("TRANS_SLNO"));
                        }
                        if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                            //EMI ONLY UPDATE STATUS
                            if (cashTo.getAuthorizeRemarks() != null && (CommonUtil.convertObjToLong(cashTo.getAuthorizeRemarks())) > 0) {
                                HashMap objMap = new HashMap();
                                long count = CommonUtil.convertObjToLong(cashTo.getAuthorizeRemarks());
                                objMap.put(CommonConstants.ACT_NUM, cashTo.getLinkBatchId());
                                List installmentLst = sqlMap.executeQueryForList("getMinimaminstallmentTL", objMap);
                                if (installmentLst != null && installmentLst.size() > 0) {
                                    objMap = (HashMap) installmentLst.get(0);
                                    int instalmentNo = CommonUtil.convertObjToInt(objMap.get("INSTALLMENT_SLNO"));
                                    HashMap updateMap = new HashMap();
                                    for (int i = 0; i < count; i++) {
                                        updateMap = new HashMap();
                                        updateMap.put("ACCT_NUM", cashTo.getLinkBatchId());
                                        updateMap.put("INSTALLMENT_NO", new Integer(instalmentNo));
                                        System.out.println("updateMap###" + updateMap);
                                        sqlMap.executeUpdate("updateloanInstallment", updateMap);
                                        instalmentNo++;
                                    }
                                    sqlMap.executeUpdate("updateEMIlastIntCalc", updateMap);
                                    updateMap = null;
                                }
                                objMap = null;
                            }
                            //END
                            //shift trans details to roolback trans details
                            transDetailsMap.put("ACCOUNTNO", cashTo.getLinkBatchId());
                            transDetailsMap.put("TRANS_ID", cashTo.getTransId());
                            List transList = null;
                            if (cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                transList = (List) sqlMap.executeQueryForList("selectTransDetailsAD", transDetailsMap);
                            }
                            if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                if (transList == null || transList.isEmpty()) {
                                    transList = (List) sqlMap.executeQueryForList("selectTransDetailsTL", transDetailsMap);
                                }
                            }
                            if (transList != null && transList.size() > 0) {
                                for (int i = 0; i < transList.size(); i++) {
                                    insertMap = (HashMap) transList.get(i);
                                    System.out.println(insertMap + "transDetailsMap####" + transDetailsMap);
                                    insertMap.put("ACCOUNTNO", transDetailsMap.get("ACCOUNTNO"));
                                    if (CommonUtil.convertObjToStr(insertMap.get("TRANS_ID")).equals(cashTo.getTransId())) {
                                        insertMap.put("ROLL_BACK_ID", rollBackID);
                                        System.out.println("insertMap####" + insertMap);
                                        sqlMap.executeUpdate("insertLoanRollBackTransactionDetails", insertMap);
                                        HashMap chargeTypeMap = checkChargeType(insertMap);
                                        if (chargeTypeMap != null && chargeTypeMap.size() > 0) {
                                            transDetailsMap.put("ACT_NUM", transDetailsMap.get("ACCOUNTNO"));
                                            transDetailsMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                            double totChargeAmt = CommonUtil.convertObjToDouble(chargeTypeMap.get("CHARGE_AMT")).doubleValue();
                                            List chargeList = (List) sqlMap.executeQueryForList("getTermLoanChargePaidAmount", transDetailsMap);
                                            if (chargeList != null && chargeList.size() > 0) {
                                                for (int k = 0; k < chargeList.size(); k++) {
                                                    HashMap chargeMap = (HashMap) chargeList.get(k);
                                                    double paidAmt = CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                                                    if (paidAmt >= totChargeAmt) {
                                                        paidAmt = totChargeAmt;
                                                        totChargeAmt = 0;
                                                    } else {
                                                        totChargeAmt -= paidAmt;
                                                    }
                                                    insertMap.put("ACT_NUM", transDetailsMap.get("ACT_NUM"));
                                                    //                                                        insertMap.put("PAID_AMT",cashTo.getAmount());
                                                    insertMap.put("CHARGE_NO", chargeMap.get("CHARGE_NO"));
                                                    //Added By Suresh
                                                    insertMap.put("CHARGE_TYPE", chargeTypeMap.get("CHARGE_TYPE"));
                                                    insertMap.put("PAID_AMT", new Double(paidAmt));
                                                    sqlMap.executeUpdate("updateChargeRollBackDetails", insertMap);
                                                }
                                            }
                                        }
                                        System.out.println("insertMap####" + insertMap);
                                        //                                        sqlMap.executeUpdate("updateclearBal", insertMap);
                                    }
                                }
                                //Added By Suresh
                                HashMap lastIntCalcMap = new HashMap();
                                lastIntCalcMap.put("ACT_NUM", cashTo.getLinkBatchId());
                                lastIntCalcMap.put("TRANS_DT", currDt);
                                Date lastIntCalDt = null;
                                Date lastCalcDt = null;
                                List maxTransDtList = null;
                                if (cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                    maxTransDtList = (List) sqlMap.executeQueryForList("getSelectLoanMaxTransDtAD", transDetailsMap);
                                } else if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                    if (maxTransDtList == null || maxTransDtList.isEmpty()) {
                                        maxTransDtList = (List) sqlMap.executeQueryForList("getSelectLoanMaxTransDt", lastIntCalcMap);
                                    }
                                }
                                System.out.println("maxTransDtList$$$$$" + maxTransDtList);
                                if (maxTransDtList != null && maxTransDtList.size() > 0) {
                                    HashMap lastIntMap = (HashMap) maxTransDtList.get(0);
                                    lastIntCalDt = (Date) lastIntMap.get("TRANS_DT");
                                }
                                if (lastIntCalDt != null && CommonUtil.convertObjToStr(lastIntCalDt).length() > 0) {
                                    //                                    lastIntCalcMap=(HashMap)maxTransDtList.get(0);
                                    //                                    lastIntCalDt=(Date)maxTransDtList.get(0);
                                    //                                    if (lastIntCalcMap!=null && lastIntCalcMap.size()>0 && lastIntCalcMap.get("TRANS_DT")!=null) {
                                    //                                        lastIntCalDt = (Date)lastIntCalcMap.get("TRANS_DT");
                                    //                                    }
                                } else {
                                    lastIntCalcMap.put("ACCT_NUM", cashTo.getLinkBatchId());
                                    maxTransDtList = (List) sqlMap.executeQueryForList("getLoanacctOpenDt", lastIntCalcMap);
                                    if (maxTransDtList != null && maxTransDtList.size() > 0) {
                                        lastIntCalcMap = (HashMap) maxTransDtList.get(0);
                                        //below added line by Ajay Sharma for Mantis ID 9086 
                                        //as last Int calculation date was going wrong dated 15-May-2014
                                        lastCalcDt = (Date) lastIntCalcMap.get("ACCT_OPEN_DT");
                                        lastIntCalDt = (Date) lastIntCalcMap.get("TEMP_LAST_INT_CALC_DT");
                                    }
                                }
                                lastCalcDt = DateUtil.addDays(lastCalcDt, -1);
                                lastCalcDt = setProperDtFormat(lastCalcDt);
                                lastIntCalDt = setProperDtFormat(lastIntCalDt);
                                lastIntCalcMap.put("ACCOUNTNO", cashTo.getLinkBatchId());
                                lastIntCalcMap.put("LAST_CALC_DT", lastCalcDt);
                                lastIntCalcMap.put("LAST_INT_CALC_DT", lastIntCalDt);
                                lastIntCalcMap.put("ACT_NUM", cashTo.getLinkBatchId());
                                System.out.println("lastIntCalcMap #######" + lastIntCalcMap);
                                if (getDateMap != null && getDateMap.get("AS_CUSTOMER_COMES") != null && getDateMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                    sqlMap.executeUpdate("updateclearBal", lastIntCalcMap);
                                }
                                sqlMap.executeUpdate("deleteLoanInterest", lastIntCalcMap);
                            }
                            transMap.put("TRANS_ID", cashTo.getTransId());
                            transDetailsMap.put("TRANS_DT", currDt);
                            if (cashTo.getProdType().equals(TransactionFactory.LOANS) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                sqlMap.executeUpdate("deleteTransDetailsTL", transDetailsMap);
                            }
                            if (cashTo.getProdType().equals(TransactionFactory.ADVANCES) || cashTo.getProdType().equals(TransactionFactory.GL)) {
                                sqlMap.executeUpdate("deleteTransDetailsAD", transDetailsMap);
                            }
                        }
                        //Commented By Suresh
                        //                        sqlMap.executeUpdate("updateinterestYes",insetMap);
                        interestMap = null;
                    }
                    lst = null;
                }
            }
            interestMap = null;
            if (cashList != null && cashList.size() > 0 && !oldTransId.equals(newTransId)) {
                transMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                transMap.put("TRANS_ID", cashTo.getTransId());
                transMap.put("LINK_BATCH_ID", map.get("LINK_BATCH_ID"));
                sqlMap.executeUpdate("rollbackCashTransactionToForTrade", transMap);
            }
        }
        cashList = null;
        transMap = null;
        insetMap = null;
        cashTo = null;
        return rollBackMap;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public HashMap checkChargeType(HashMap insertMap) {
        HashMap resultMap = new HashMap();
        String chargeType = "";
        double amt = 0;
        if (insertMap.containsKey("POSTAGE_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("POSTAGE_CHARGE")).doubleValue() > 0) {
            chargeType = "POSTAGE CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("POSTAGE_CHARGE")).doubleValue();

        } else if (insertMap.containsKey("EXPENSE") && CommonUtil.convertObjToDouble(insertMap.get("EXPENSE")).doubleValue() > 0) {
            if (insertMap.containsKey("PARTICULARS")) {
                if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("EA_COST")) {
                    chargeType = "EA_COST";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("EP_COST")) {
                    chargeType = "EP_COST";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("ARC")) {
                    chargeType = "ARC_COST";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("ACT CLOSING CHARGE")) {
                    chargeType = "ACT CLOSING CHARGE";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("ACT CLOSING MISC CHARGE")) {
                    chargeType = "ACT CLOSING MISC CHARGE";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("RECOVERY CHARGES") || CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("RECOVERY_WAIVE")) {
                    chargeType = "RECOVERY CHARGES";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("MEASUREMENT CHARGES") || CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("MEASUREMENT_WAIVE")) {
                    chargeType = "MEASUREMENT CHARGES";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("KOLEFIELD EXPENSE") || CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("KOLEFIELD EXPENSE")) {
                    chargeType = "KOLEFIELD EXPENSE";
                } else if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("KOLEFIELD OPERATION") || CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("KOLEFIELD OPERATION")) {
                    chargeType = "KOLEFIELD OPERATION";
                }
            }
            amt = CommonUtil.convertObjToDouble(insertMap.get("EXPENSE")).doubleValue();
        } else if (insertMap.containsKey("LEGAL_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("LEGAL_CHARGE")).doubleValue() > 0) {
            chargeType = "LEGAL CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("LEGAL_CHARGE")).doubleValue();
        } else if (insertMap.containsKey("ARBITARY_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("ARBITARY_CHARGE")).doubleValue() > 0) {
            chargeType = "ARBITRARY CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("ARBITARY_CHARGE")).doubleValue();
        } else if (insertMap.containsKey("INSURANCE_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("INSURANCE_CHARGE")).doubleValue() > 0) {
            chargeType = "INSURANCE CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("INSURANCE_CHARGE")).doubleValue();
        } else if (insertMap.containsKey("EXE_DEGREE") && CommonUtil.convertObjToDouble(insertMap.get("EXE_DEGREE")).doubleValue() > 0) {
            chargeType = "EXECUTION DECREE CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("EXE_DEGREE")).doubleValue();
        } else if (insertMap.containsKey("MISC_CHARGES") && CommonUtil.convertObjToDouble(insertMap.get("MISC_CHARGES")).doubleValue() > 0) {
             if (CommonUtil.convertObjToStr(insertMap.get("PARTICULARS")).contains("MISCELLANEOUS CHARGES")) {
            chargeType = "MISCELLANEOUS CHARGES";
             }
            amt = CommonUtil.convertObjToDouble(insertMap.get("MISC_CHARGES")).doubleValue();
        } else if (insertMap.containsKey("ADVERTISE_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("ADVERTISE_CHARGE")).doubleValue() > 0) {
            chargeType = "ADVERTISE CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("ADVERTISE_CHARGE")).doubleValue();
        }//Below condition is added by Ajay Sharma for Mantis ID 9086 on 23-May-2014 as after rollback notice charges was not reversing
        else if (insertMap.containsKey("NOTICE_CHARGES") && CommonUtil.convertObjToDouble(insertMap.get("NOTICE_CHARGES")).doubleValue() > 0) {
            chargeType = "NOTICE CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("NOTICE_CHARGES")).doubleValue();
        }else if (insertMap.containsKey("RECOVERY_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("RECOVERY_CHARGE")).doubleValue() > 0) {
            chargeType = "RECOVERY CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("RECOVERY_CHARGE")).doubleValue();
        }else if (insertMap.containsKey("MEASUREMENT_CHARGE") && CommonUtil.convertObjToDouble(insertMap.get("MEASUREMENT_CHARGE")).doubleValue() > 0) {
            chargeType = "MEASUREMENT CHARGES";
            amt = CommonUtil.convertObjToDouble(insertMap.get("MEASUREMENT_CHARGE")).doubleValue();
        }else if (insertMap.containsKey("KOLEFIELD EXPENSE") && CommonUtil.convertObjToDouble(insertMap.get("KOLEFIELD EXPENSE")).doubleValue() > 0) {
            chargeType = "KOLEFIELD EXPENSE";
            amt = CommonUtil.convertObjToDouble(insertMap.get("KOLEFIELD EXPENSE")).doubleValue();
        }else if (insertMap.containsKey("KOLEFIELD OPERATION") && CommonUtil.convertObjToDouble(insertMap.get("KOLEFIELD OPERATION")).doubleValue() > 0) {
            chargeType = "KOLEFIELD OPERATION";
            amt = CommonUtil.convertObjToDouble(insertMap.get("KOLEFIELD OPERATION")).doubleValue();
        }
        resultMap.put("CHARGE_TYPE", chargeType);
        resultMap.put("CHARGE_AMT", new Double(amt));
        System.out.println("resultMap####" + resultMap);
        return resultMap;
    }

    public void updatepassbook(CashTransactionTO objCashTransactionTOUpd, HashMap ruleMap) throws Exception {
        HashMap map = new HashMap();
        if (objCashTransactionTOUpd.getProdType().equals(TransactionFactory.OPERATIVE) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.ADVANCES) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
            List lst = (List) sqlMap.executeQueryForList("getPassBookEntryForTransID", ruleMap);
            map = (HashMap) lst.get(0);
            double balAmt = CommonUtil.convertObjToDouble(map.get("BALANCE")).doubleValue();
            double debitAmt = CommonUtil.convertObjToDouble(map.get("DEBIT")).doubleValue();
            double creditAmt = CommonUtil.convertObjToDouble(map.get("CREDIT")).doubleValue();
            balAmt = balAmt - creditAmt + debitAmt;
            map.put("ROLL_BACK_ID", rollBackID);
            sqlMap.executeUpdate("insertPassBookRollBack", map);
            sqlMap.executeUpdate("deletePassBookEntry", map);
            lst = (List) sqlMap.executeQueryForList("getPassBookEntryForSlNo", map);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (HashMap) lst.get(i);
                    debitAmt = CommonUtil.convertObjToDouble(map.get("DEBIT")).doubleValue();
                    creditAmt = CommonUtil.convertObjToDouble(map.get("CREDIT")).doubleValue();
                    balAmt = balAmt + creditAmt - debitAmt;
                    map.put("BALANCE", new Double(balAmt));
                    map.put("SLNO_NEW", CommonUtil.convertObjToInt(map.get("SLNO"))-1);
                    sqlMap.executeUpdate("updatePassBook", map);
                }
            }
            if (map != null) {
                map.clear();
            }
            if (lst != null) {
                lst.clear();
            }
        }
        map = null;
    }

    public void updatepassbookFromTransfer(TxTransferTO objCashTransactionTOUpd, HashMap ruleMap) throws Exception {
        HashMap map = new HashMap();
        if (objCashTransactionTOUpd.getProdType().equals(TransactionFactory.OPERATIVE) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.ADVANCES) || objCashTransactionTOUpd.getProdType().equals(TransactionFactory.AGRIADVANCES)) {
            List lst = (List) sqlMap.executeQueryForList("getPassBookEntryForTransID", ruleMap);
            map = (HashMap) lst.get(0);
            double balAmt = CommonUtil.convertObjToDouble(map.get("BALANCE")).doubleValue();
            double debitAmt = CommonUtil.convertObjToDouble(map.get("DEBIT")).doubleValue();
            double creditAmt = CommonUtil.convertObjToDouble(map.get("CREDIT")).doubleValue();
            balAmt = balAmt - creditAmt + debitAmt;
            map.put("ROLL_BACK_ID", rollBackID);
            sqlMap.executeUpdate("insertPassBookRollBack", map);
            sqlMap.executeUpdate("deletePassBookEntry", map);
            lst = (List) sqlMap.executeQueryForList("getPassBookEntryForSlNo", map);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (HashMap) lst.get(i);
                    debitAmt = CommonUtil.convertObjToDouble(map.get("DEBIT")).doubleValue();
                    creditAmt = CommonUtil.convertObjToDouble(map.get("CREDIT")).doubleValue();
                    balAmt = balAmt + creditAmt - debitAmt;
                    map.put("BALANCE", new Double(balAmt));
                    map.put("SLNO_NEW", CommonUtil.convertObjToInt(map.get("SLNO"))-1);
                    sqlMap.executeUpdate("updatePassBook", map);
                }
            }
            if (map != null) {
                map.clear();
            }
            if (lst != null) {
                lst.clear();
            }
        }
        map = null;
    }

    private HashMap getRuleMap(CashTransactionTO objCashTransactionTORuleMap) throws Exception {
        HashMap inputMap = new HashMap();
        System.out.println("objCashTransactionTORuleMap:" + objCashTransactionTORuleMap);
        double amount = objCashTransactionTORuleMap.getAmount().doubleValue();
        String acctNo = objCashTransactionTORuleMap.getActNum();
        if (objCashTransactionTORuleMap.getTransType().equals(TransactionDAOConstants.DEBIT)) {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.CREDIT);
            inputMap.put("NORMAL", "");
        } else {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.DEBIT);
            inputMap.put("NORMALDEBIT", "");
            amount = -1 * amount;
        }
        if (objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.GL)) {
            objCashTransactionTORuleMap.setActNum(null);  // This condition added to prevent actnum entries in GL
        }
        if ((acctNo == null || acctNo.equals("")) && (objCashTransactionTORuleMap.getAcHdId() != null)) {
            acctNo = objCashTransactionTORuleMap.getAcHdId();
        }
        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put("ACT_NUM", acctNo);
        inputMap.put("TRANS_DT", objCashTransactionTORuleMap.getTransDt());
        inputMap.put("AMT", objCashTransactionTORuleMap.getAmount());
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, objCashTransactionTORuleMap.getInstType());
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(objCashTransactionTORuleMap.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(objCashTransactionTORuleMap.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.DATE, objCashTransactionTORuleMap.getInstDt());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, objCashTransactionTORuleMap.getBranchId());
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, objCashTransactionTORuleMap.getInitiatedBranch());
        inputMap.put(TransactionDAOConstants.TO_STATUS, objCashTransactionTORuleMap.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, objCashTransactionTORuleMap.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, objCashTransactionTORuleMap.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.TRANS_ID, objCashTransactionTORuleMap.getTransId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDt);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CASH);
        inputMap.put("ACTUAL_AMT", objCashTransactionTORuleMap.getAmount()); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        if (objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.LOANS) || objCashTransactionTORuleMap.getProdType().equals(TransactionFactory.ADVANCES) || objCashTransactionTORuleMap.getProdType().equals("ATL")) {
            inputMap.put(PROD_ID, objCashTransactionTORuleMap.getProdId());
            inputMap.put("PROD_TYPE", objCashTransactionTORuleMap.getProdType());
            inputMap.put("ROLLBACKTRANSACTION", "ROLLBACKTRANSACTION");
            inputMap.put("PRINCIPAL_AMOUNT", objCashTransactionTORuleMap.getAmount());
        }
        if (objCashTransactionTORuleMap.getParticulars() != null) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, objCashTransactionTORuleMap.getParticulars());
        }
        //System.out.println ("inputMap:"+inputMap);
        return inputMap;
    }

    private HashMap getTransferRuleMap(TxTransferTO objTxTransferTO) throws Exception {
        HashMap inputMap = new HashMap();
        System.out.println("objobjTxTransferTO:" + objTxTransferTO);
        double amount = objTxTransferTO.getAmount().doubleValue();
        String acctNo = objTxTransferTO.getActNum();
        if (objTxTransferTO.getTransType().equals(TransactionDAOConstants.DEBIT)) {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.CREDIT);
            inputMap.put("NORMAL", "");
        } else {
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.DEBIT);
            inputMap.put("NORMALDEBIT", "");
            amount = -1 * amount;
        }
        if (objTxTransferTO.getProdType().equals(TransactionFactory.GL)) {
            objTxTransferTO.setActNum(null);  // This condition added to prevent actnum entries in GL
            acctNo = "";
        }
        if ((acctNo == null || acctNo.equals("")) && (objTxTransferTO.getAcHdId() != null)) {
            acctNo = objTxTransferTO.getAcHdId();
        }
        inputMap.put(TransactionDAOConstants.ACCT_NO, acctNo);
        inputMap.put("ACT_NUM", acctNo);
        inputMap.put("TRANS_DT", objTxTransferTO.getTransDt());
        inputMap.put("AMT", objTxTransferTO.getAmount());
        inputMap.put(TransactionDAOConstants.AMT, CommonUtil.convertObjToDouble(String.valueOf(amount)));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_TYPE, objTxTransferTO.getInstType());
        inputMap.put(TransactionDAOConstants.INSTRUMENT_1, CommonUtil.convertObjToStr(objTxTransferTO.getInstrumentNo1()));
        inputMap.put(TransactionDAOConstants.INSTRUMENT_2, CommonUtil.convertObjToStr(objTxTransferTO.getInstrumentNo2()));
        inputMap.put(TransactionDAOConstants.DATE, objTxTransferTO.getInstDt());
        inputMap.put(TransactionDAOConstants.BRANCH_CODE, objTxTransferTO.getBranchId());
        inputMap.put(TransactionDAOConstants.INITIATED_BRANCH, objTxTransferTO.getInitiatedBranch());
        inputMap.put(TransactionDAOConstants.TO_STATUS, objTxTransferTO.getStatus());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_BY, objTxTransferTO.getAuthorizeBy());
        inputMap.put(TransactionDAOConstants.AUTHORIZE_STATUS, objTxTransferTO.getAuthorizeStatus());
        inputMap.put(TransactionDAOConstants.TRANS_ID, objTxTransferTO.getTransId());
        inputMap.put("BATCH_ID", objTxTransferTO.getBatchId());
        inputMap.put(TransactionDAOConstants.TODAY_DT, currDt);
        inputMap.put(TransactionDAOConstants.TRANS_MODE, TransactionDAOConstants.CASH);
        inputMap.put("ACTUAL_AMT", objTxTransferTO.getAmount()); // Added by Rajesh for LimitCheckingRule. No need to deduct the previous amount for Operative. Because the system should check the actual amount with Minimum Balance (on product level)
        if (objTxTransferTO.getProdType().equals(TransactionFactory.LOANS) || objTxTransferTO.getProdType().equals(TransactionFactory.ADVANCES) || objTxTransferTO.getProdType().equals("ATL")) {
            inputMap.put(PROD_ID, objTxTransferTO.getProdId());
            inputMap.put("PROD_TYPE", objTxTransferTO.getProdType());
            inputMap.put("ROLLBACKTRANSACTION", "ROLLBACKTRANSACTION");
            inputMap.put("PRINCIPAL_AMOUNT", objTxTransferTO.getAmount());
        }
        if (objTxTransferTO.getParticulars() != null) {
            inputMap.put(TransactionDAOConstants.PARTICULARS, objTxTransferTO.getParticulars());
        }
        //System.out.println ("inputMap:"+inputMap);
        return inputMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("!!!!!obj : " + obj);
        HashMap returnMap = new HashMap();
        HashMap loanInterest = new HashMap();
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        if (obj.get("WHERE") != null && obj.get("WHERE") instanceof HashMap) {
            loanInterest = (HashMap) obj.get("WHERE");
        }
        returnMap = getData(obj);
        return returnMap;
    }

    private void updateStatusInRemitIssueTable(HashMap map) throws Exception {
        System.out.println("############## all closing details removing into remitissuetable : " + map);
        HashMap remitStatusUpdateMap = new HashMap();
        if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")
                || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("SB/Current Account Closing")
                || CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Loan Account Closing")) {
            if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Deposit Account Closing")) {
                remitStatusUpdateMap.put("ACT_NUM", map.get("ACT_NUM"));
            } else if (CommonUtil.convertObjToStr(map.get("ROLL_BACK_SCREEN")).equals("Loan Account Closing")) {
                remitStatusUpdateMap.put("ACT_NUM", map.get("ACCT_NUM"));
            }
            sqlMap.executeUpdate("rollbackedAccountsStatusUpdation", remitStatusUpdateMap);
            remitStatusUpdateMap = null;
        }
    }
    
    /*
     * Day end completed in 0001 branch after completing day end 0002 branch has to debit the 8001 branch account. 
     * Day end balance already inserted while doing day end of 0001 branch so again we need to insert the dayend once again 
     * because of schedule should be tally on that day
     */
     private void insertDayEndBalanceCash(CashTransactionTO objCashTransactionTO) throws Exception {
         System.out .println("objCashTransactionTO insertDayEndBalanceCash : "+objCashTransactionTO);
        if(!CommonUtil.convertObjToStr(objCashTransactionTO.getProdType()).equals("GL") && !CommonUtil.convertObjToStr(objCashTransactionTO.getProdType()).equals("AB")){
            if(objCashTransactionTO.getBranchId() != null && objCashTransactionTO.getInitiatedBranch() != null && !objCashTransactionTO.getBranchId().equals(objCashTransactionTO.getInitiatedBranch())){
                HashMap map =new HashMap();
                map.put("ACT_NUM",objCashTransactionTO.getActNum());
                map.put("APP_DT",getProperFormatDate(objCashTransactionTO.getTransDt()));
                System.out .println("map##### insertDayEndBalanceCash : "+map);
                System.out.println("objCashTransactionTO.getScreenName() : "+objCashTransactionTO.getScreenName());
                if (CommonUtil.convertObjToStr(objCashTransactionTO.getProdType()).equals("AD")) {
                    double tod_lim = 0.0;
                    double advLimit = 0.0;
                    double overAllLimit = 0.0;
                    List todlimit = sqlMap.executeQueryForList("getTODLimit", map);
                    if (todlimit != null && todlimit.size() > 0) {
                        HashMap where = (HashMap) todlimit.get(0);
                        tod_lim = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                    }
                    List dpList = sqlMap.executeQueryForList("getDrawPowOrLim", map);
                    if (dpList != null && dpList.size() > 0) {
                        HashMap advLimitMap = new HashMap();
                        advLimitMap = (HashMap) dpList.get(0);
                        advLimit = CommonUtil.convertObjToDouble(advLimitMap.get("LIMIT")).doubleValue();
                        double dp = CommonUtil.convertObjToDouble(advLimitMap.get("DRAWING_POWER")).doubleValue();
                        if (dp > 0 && dp < advLimit) {
                            advLimit = dp;
                        }
                        overAllLimit = tod_lim + advLimit;
                    } else {
    //                    advLimit = CommonUtil.convertObjToDouble(((HashMap) accountList.get(i)).get("LIMIT")).doubleValue();
                        overAllLimit = tod_lim + advLimit;
                    }
                    map.put("TOD_LIMIT", new Double(tod_lim));
                    map.put("DPORLIMIT", new Double(advLimit));
                    map.put("OVERALL_LIMIT", new Double(overAllLimit));
                }
                System.out.println("objCashTransactionTO.getScreenName() : "+objCashTransactionTO.getScreenName());
                String depositSubNo = objCashTransactionTO.getActNum();
                if (objCashTransactionTO.getProdType() != null && objCashTransactionTO.getProdType().equals("TD") && depositSubNo.lastIndexOf("_") != -1) {
                    depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                    map.put("ACT_NUM",depositSubNo);
                    if(objCashTransactionTO.getScreenName() != null && (CommonUtil.convertObjToStr(objCashTransactionTO.getScreenName()).equals("Deposit Account Renewal") || 
                    CommonUtil.convertObjToStr(objCashTransactionTO.getScreenName()).equals("Deposit Account Closing"))){
                        map.put("AMOUNT",objCashTransactionTO.getAmount());                
                    }else{
                        map.put("AMOUNT",new Double(0));                
                    }

                }
                map.put("ANY_DATE_IBR_ROLLBACK","ANY_DATE_IBR_ROLLBACK");
                map.put("TRANS_ID",objCashTransactionTO.getTransId());
                System.out.println("map : "+map);
                sqlMap.executeUpdate("updateDayendBalance" + objCashTransactionTO.getProdType(), map);
            }
        }
        if(objCashTransactionTO.getBranchId() != null && objCashTransactionTO.getInitiatedBranch() != null && !objCashTransactionTO.getBranchId().equals(objCashTransactionTO.getInitiatedBranch())){
            HashMap deleteIBRMap = new HashMap();
                deleteIBRMap.put("TRANS_ID",objCashTransactionTO.getTransId());
                deleteIBRMap.put("BRANCH_ID",objCashTransactionTO.getBranchId());
                deleteIBRMap.put("INITIATED_BRANCH",objCashTransactionTO.getInitiatedBranch());
                deleteIBRMap.put("APP_DT",getProperFormatDate(objCashTransactionTO.getTransDt()));
                System.out.println("deleteIBRMap : "+deleteIBRMap);
                sqlMap.executeUpdate("deleteIBRRollbackRecords" ,deleteIBRMap);
        }
    }
     
     /*
     * Day end completed in 0001 branch but after completing day end 0002 branch has to debit the 8001 branch account. 
     * Day end balance already inserted while doing day end of 0001 branch so again we need to insert the dayend once again 
     * because of schedule should be tally on that day
     */
    private void insertDayEndBalanceTransfer(TxTransferTO objTxTransferTO) throws Exception{
        if(!CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("GL") && !CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("AB")){
            if(objTxTransferTO.getBranchId() != null && objTxTransferTO.getInitiatedBranch() != null && !objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())){
                HashMap map =new HashMap();
                map.put("ACT_NUM",objTxTransferTO.getActNum());
                map.put("APP_DT",getProperFormatDate(objTxTransferTO.getTransDt()));
                if (CommonUtil.convertObjToStr(objTxTransferTO.getProdType()).equals("AD")) {
                    double tod_lim = 0.0;
                    double advLimit = 0.0;
                    double overAllLimit = 0.0;
                    List todlimit = sqlMap.executeQueryForList("getTODLimit", map);
                    if (todlimit != null && todlimit.size() > 0) {
                        HashMap where = (HashMap) todlimit.get(0);
                        tod_lim = CommonUtil.convertObjToDouble(where.get("TOD_AMOUNT")).doubleValue();
                    }
                    List dpList = sqlMap.executeQueryForList("getDrawPowOrLim", map);
                    if (dpList != null && dpList.size() > 0) {
                        HashMap advLimitMap = new HashMap();
                        advLimitMap = (HashMap) dpList.get(0);
                        advLimit = CommonUtil.convertObjToDouble(advLimitMap.get("LIMIT")).doubleValue();
                        double dp = CommonUtil.convertObjToDouble(advLimitMap.get("DRAWING_POWER")).doubleValue();
                        if (dp > 0 && dp < advLimit) {
                            advLimit = dp;
                        }
                        overAllLimit = tod_lim + advLimit;
                    } else {
    //                    advLimit = CommonUtil.convertObjToDouble(((HashMap) accountList.get(i)).get("LIMIT")).doubleValue();
                        overAllLimit = tod_lim + advLimit;
                    }
                    map.put("TOD_LIMIT", new Double(tod_lim));
                    map.put("DPORLIMIT", new Double(advLimit));
                    map.put("OVERALL_LIMIT", new Double(overAllLimit));
                }
                System.out .println("map#####"+map);
                String depositSubNo = objTxTransferTO.getActNum();
                if (objTxTransferTO.getProdType()!=null && objTxTransferTO.getProdType().equals("TD") && depositSubNo.lastIndexOf("_") != -1) {
                    depositSubNo = depositSubNo.substring(0, depositSubNo.lastIndexOf("_"));
                    map.put("ACT_NUM",depositSubNo);
                    if(objTxTransferTO.getScreenName() != null && (CommonUtil.convertObjToStr(objTxTransferTO.getScreenName()).equals("Deposit Account Renewal") || 
                    CommonUtil.convertObjToStr(objTxTransferTO.getScreenName()).equals("Deposit Account Closing"))){
                        map.put("AMOUNT",objTxTransferTO.getAmount());                
                    }else{
                        map.put("AMOUNT",new Double(0));                
                    }
                }
                map.put("ANY_DATE_IBR_ROLLBACK","ANY_DATE_IBR_ROLLBACK");
                map.put("TRANS_ID",objTxTransferTO.getTransId());
                System.out.println("map : "+map);
                sqlMap.executeUpdate("updateDayendBalance" + objTxTransferTO.getProdType(), map);
            }
        }        
        if(objTxTransferTO.getBranchId() != null && objTxTransferTO.getInitiatedBranch() != null && !objTxTransferTO.getBranchId().equals(objTxTransferTO.getInitiatedBranch())){
            HashMap deleteIBRMap = new HashMap();
                deleteIBRMap.put("TRANS_ID",objTxTransferTO.getTransId());
                deleteIBRMap.put("BRANCH_ID",objTxTransferTO.getBranchId());
                deleteIBRMap.put("INITIATED_BRANCH",objTxTransferTO.getInitiatedBranch());
                deleteIBRMap.put("APP_DT",getProperFormatDate(objTxTransferTO.getTransDt()));
                System.out.println("deleteIBRMap : "+deleteIBRMap);
                sqlMap.executeUpdate("deleteIBRRollbackRecords" ,deleteIBRMap);
        }
    }
    
    private void unSubscribeSMS(HashMap smsMap) throws Exception {
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", smsMap);
        smsMap.clear();
        smsMap = null;
    }
    
    public Date getProperFormatDate(Object obj) {
        Date curDt = null;
        // currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
    
      private boolean isDailyDeposit(String actNum) throws Exception {
        boolean daily = false;
        int cnt = CommonUtil.convertObjToInt(sqlMap.executeQueryForObject("getDailyDeposit", actNum));
        if (cnt > 0) {
            daily = true;
        }
        return daily;
    }
}
