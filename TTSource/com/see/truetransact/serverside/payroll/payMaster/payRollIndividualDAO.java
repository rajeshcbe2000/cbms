/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.payroll.payMaster;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.payroll.PayRollTo;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;

import com.see.truetransact.transferobject.payroll.pfMaster.PFMasterTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is used for OperativeAcctProductDAO Data Access.
 *
 * @author SreeKrishnan
 *
 *
 */
public class payRollIndividualDAO extends TTDAO {

    private final static Logger log = Logger.getLogger(payRollIndividualDAO.class);
    private EarnDeduPayTO earnDeduPayTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private LinkedHashMap tablePayrollDetails = null;
    private LinkedHashMap payrollMap = null;
    private MDSProductAcctHeadTO acctHeadTo;
    private MDSProductSchemeTO schemeTo;
    private static SqlMap sqlMap = null;
    private Date CurrDt = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap returnDataMap = null;
    private HashMap returnPfCashMap = null;
    private HashMap returnPfTransMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private String CASHIER_AUTH_ALLOWED="N";

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public payRollIndividualDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private String getSHGId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHG_ID");
        String shgId = CommonUtil.convertObjToStr((dao.executeQuery(where)).get(CommonConstants.DATA));
        return shgId;
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        if (map.containsKey("REMITT_ISSUE_TRANS")) {
            List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", map);
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
                list = null;
            }
        }
        List shgList = (List) sqlMap.executeQueryForList("getSelectPayMasterTO", map);
        if (shgList != null && shgList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@shgList" + shgList);
            for (int i = shgList.size(), j = 0; i > 0; i--, j++) {
                String cust = ((EarnDeduPayTO) shgList.get(j)).getPayCode();
                ParMap.put(((EarnDeduPayTO) shgList.get(j)).getPayCode(), shgList.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("PayMasterTO_DATA", ParMap);
        }
        return returnMap;

    }

    public static void main(String str[]) {
        try {
            payRollIndividualDAO dao = new payRollIndividualDAO();
            HashMap inputMap = new HashMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("Map in payrollindividual DAO : " + map);
        try {
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            CurrDt = ServerUtil.getCurrentDate(_branchCode);
            HashMap returnMap = new HashMap();
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            returnDataMap = new HashMap();
            returnPfTransMap = new HashMap();
            returnPfCashMap = new HashMap();
             if(map.containsKey("CASHIER_AUTH_ALLOWED") && map.get("CASHIER_AUTH_ALLOWED").equals("Y")){
                 CASHIER_AUTH_ALLOWED="Y";
             }
            tableDetails = (LinkedHashMap) map.get("PayMasterTableDetails");
            if (map != null && map.containsKey("PayRollMap")) {
                tablePayrollDetails = (LinkedHashMap) map.get("PayRollMap");
            }
      
            deletedTableValues = (LinkedHashMap) map.get("deletedPayMasterMapTableDetails");
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                if (tablePayrollDetails != null) {
                    String payrollId=getPayRollID();
                    ArrayList addPayrollList = new ArrayList(tablePayrollDetails.keySet());
                    for (int i = 0; i < addPayrollList.size(); i++) {
 
                        PayRollTo payRollTo = (PayRollTo) tablePayrollDetails.get(addPayrollList.get(i));
                        payRollTo.setPayrollId(payrollId);
                        payRollTo.setSrlNo(CommonUtil.convertObjToInt(getMaxSlNo(payRollTo)));
                        payRollTo.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
                        sqlMap.executeUpdate("insertPayRollTo", payRollTo);
                        //To PAY_PF_MASTER
                        if (map != null && map.containsKey("PAY_MODULE_TYPE") && map.get("PAY_MODULE_TYPE") != null && map.get("PAY_MODULE_TYPE").equals("PF")) {
                            HashMap pfMap = new HashMap();
                            
                            pfMap.put("PF_NO", getPFBAccount(payRollTo.getEmployeeId(), payRollTo.getPayCode()));
                            pfMap.put("TRAN_DT", CurrDt.clone());
                            if (returnPfTransMap != null && returnPfTransMap.get("TRANS_ID") != null && !returnPfTransMap.get("TRANS_ID").equals("")) {
                                pfMap.put("TRANS_ID", CommonUtil.convertObjToStr(returnPfTransMap.get("TRANS_ID")));
                                pfMap.put("BATCH_ID", CommonUtil.convertObjToStr(returnPfTransMap.get("TRANS_ID")));
                                pfMap.put("TRANS_MODE", CommonConstants.TX_TRANSFER);
                            }
                            if (returnPfCashMap != null && returnPfCashMap.get("TRANS_ID") != null && !returnPfCashMap.get("TRANS_ID").equals("")) {
                                pfMap.put("TRANS_ID", CommonUtil.convertObjToStr(returnPfCashMap.get("TRANS_ID")));
                                pfMap.put("TRANS_MODE", CommonConstants.TX_CASH);
                            }
                            pfMap.put("AMOUNT", payRollTo.getAmount());
                            pfMap.put("PROD_TYPE", "C");
                            pfMap.put("CREATED_BY", payRollTo.getCreatedBy());                            
                            pfMap.put("CREATED_DATE", CurrDt.clone());
                            pfMap.put("AUTHORIZED_BY", "");
                            pfMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                            pfMap.put("BRANCHID", _branchCode);
                            pfMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            pfMap.put("REMARK", "");
                            pfMap.put("PF_TRANS_TYPE", "");
                            pfMap.put("TRANS_TYPE", payRollTo.getTransType());
                            sqlMap.executeUpdate("InsertPfTransTo", pfMap);
                            sqlMap.executeUpdate("updateprnbalcashforPayRollIndividual", pfMap);
                        }
                    }
                    HashMap updateMap = new HashMap();
                    updateMap.put("PAYROLLID", payrollId);
                    updateMap.put("generateSingleTransId", "INDIVIDUAL");
                    sqlMap.executeUpdate("updatePayrollData", updateMap);
                }
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map);
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                if (map != null) {
                    authorize(map);
                    //To PayRoll ....
                    HashMap statusMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                    String status = CommonUtil.convertObjToStr(statusMap.get(CommonConstants.AUTHORIZESTATUS));
                    if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        if (map.containsKey("PayRollMap")) {
                            payrollMap = (LinkedHashMap) map.get("PayRollMap");
                            PayRollTo payRollTo = (PayRollTo) payrollMap.get("PayRollTo");
                            payRollTo.setPayrollId(getPayRollID());
                            sqlMap.executeUpdate("insertPayRollTo", payRollTo);
                            HashMap updateMap = new HashMap();
                            updateMap.put("PAYROLLID", payRollTo.getPayrollId());
                            updateMap.put("generateSingleTransId", "INDIVIDUAL");
                            sqlMap.executeUpdate("updatePayrollData", updateMap);                            
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
        destroyObjects();
        return returnDataMap;
    }
    public String getPFBAccount(String employeeId, String ayCode) {
        HashMap pfMap = new HashMap();
        boolean checkFlag = false;
        pfMap.put("EMPLOYEEID", employeeId);
        pfMap.put("PAY_CODE", ayCode);
        List lst = ServerUtil.executeQuery("getPFBalance", pfMap);
        if (lst != null && lst.size() > 0) {
            pfMap = (HashMap) lst.get(0);
            if(CommonUtil.convertObjToStr(pfMap.get("PF_ACT_NO"))!=null)
            return CommonUtil.convertObjToStr(pfMap.get("PF_ACT_NO"));
        }
        return  null;
    }
    private String getPayRollID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PAYROLL_ENTRY_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (map.containsKey("TAB_DATA")&& !map.get("TAB_DATA").equals("BULK_DATA")) {
                double recoveryAmount = 0;
                DateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                ArrayList addList = new ArrayList(tableDetails.keySet());
                if (map.containsKey("TAB_DATA") && map.get("TAB_DATA").equals("TRANSACTION_DATA")) {
                    doRecoveryTransaction(map);
                }
                if (map.containsKey("TAB_DATA") && !map.get("TAB_DATA").equals("TRANSACTION_DATA")) {
                    for (int i = 0; i < addList.size(); i++) {
                        EarnDeduPayTO earnDeduPayTO = (EarnDeduPayTO) tableDetails.get(addList.get(i));
//                    //if (earnDeduPayTO.getPayTrans() != null && earnDeduPayTO.getPayTrans().equals("Y")) {
//                    if (map.containsKey("TAB_DATA") && map.get("TAB_DATA").equals("TRANSACTION_DATA")) {
//                        //To Tansaction part....
//                      //  doRecoveryTransaction(map, earnDeduPayTO);
//                    } else {
                        //recoveryAmount = CommonUtil.convertObjToDouble(earnDeduPayTO.getAmount());
                        //earnDeduPayTO.setAmount(Math.round(CommonUtil.convertObjToDouble(recoveryAmount / CommonUtil.convertObjToDouble(earnDeduPayTO.getRecovryMnth()))));
                        cal.setTime(earnDeduPayTO.getFromDate());
                        cal.add(Calendar.MONTH, earnDeduPayTO.getRecovryMnth());
                        System.out.println("earnDeduPayTO.getToDate" + formatter.parse(formatter.format(cal.getTime())));
                        earnDeduPayTO.setToDate(formatter.parse(formatter.format(cal.getTime())));
                        earnDeduPayTO.setCreatedDate(CurrDt);
                        earnDeduPayTO.setSrlNo(CommonUtil.convertObjToInt("1"));
                        //}
                        sqlMap.executeUpdate("insertPayMaster", earnDeduPayTO);
                        logTO.setData(earnDeduPayTO.toString());
                        logTO.setPrimaryKey(earnDeduPayTO.getKeyData());
                        logDAO.addToLog(logTO);
                        earnDeduPayTO = null;
                        // }
                    }
                }
            }
            if(map != null && map.containsKey("TAB_DATA") && map.get("TAB_DATA").equals("BULK_DATA")&&map.containsKey("PAYROLL_BULK_UPDATE_LIST"))
            {
                 EarnDeduPayTO earnDeduPayTO = null; 
                ArrayList payrollBulkList=new ArrayList();
                payrollBulkList = (ArrayList) map.get("PAYROLL_BULK_UPDATE_LIST");
                System.out.println("rishad is ###############" + payrollBulkList);
                for (int i = 0; i < payrollBulkList.size(); i++) {
                    ArrayList list = new ArrayList();
                    earnDeduPayTO = new EarnDeduPayTO();
                    list = (ArrayList) payrollBulkList.get(i);
                    earnDeduPayTO.setEmployeeId(CommonUtil.convertObjToStr(list.get(0)));
                    earnDeduPayTO.setPayCode(CommonUtil.convertObjToStr(list.get(1)));
                    earnDeduPayTO.setAmount(CommonUtil.convertObjToDouble(list.get(2)));
                    earnDeduPayTO.setSrlNo(CommonUtil.convertObjToInt(list.get(3)));
                    earnDeduPayTO.setStatusDate(CurrDt);
                    earnDeduPayTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    earnDeduPayTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                    System.out.println("rishad" + earnDeduPayTO);
                    sqlMap.executeUpdate("updateBulkPaymaster", earnDeduPayTO);
                }

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private void doRecoveryTransaction(HashMap map) throws Exception {
        //For Head Selection
        HashMap achdMap = new HashMap();
        List lst = null;
        achdMap.put("PAY_CODE", CommonUtil.convertObjToStr(map.get("PAY_CODE")));
        if (map.containsKey("CONTRA_TRANS_TYPE")) {
            achdMap.put("CONTRA_TRANS_TYPE", CommonUtil.convertObjToStr(map.get("CONTRA_TRANS_TYPE")));
        }
        List achdLst = ServerUtil.executeQuery("getselectAcHeadsForTransaction", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            achdMap = (HashMap) achdLst.get(0);
        }
        TransactionTO transactionTO = new TransactionTO();
        HashMap dataMap = new HashMap();
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(CommonUtil.convertObjToStr(1));
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            //    transactionDAO.setLinkBatchID(earnDeduPayTO.getEmployeeId());
            TransferTrans objTransferTrans = new TransferTrans();
            ArrayList transferList = new ArrayList();
            ArrayList cashList = new ArrayList();
            HashMap txMap;
            double tranAmt = 0.0;
            double transAmt = transactionTO.getTransAmt();
            double totalPayAmount = 0;
            //For getting account heads
            HashMap transMap = new HashMap();
            HashMap debitMap = new HashMap();
            ArrayList addTransactionList = new ArrayList(tableDetails.keySet());
            if (map.containsKey("PAY_CODE_TYPE") && !map.get("PAY_CODE_TYPE").equals("")) {
                if (transactionTO.getTransType().equals(CommonConstants.TX_TRANSFER)) {
                    for (int i = 0; i < addTransactionList.size(); i++) {
                        EarnDeduPayTO earnDeduPayTO = (EarnDeduPayTO) tableDetails.get(addTransactionList.get(i));
                        if (!earnDeduPayTO.getProdType().equals("") && earnDeduPayTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                            String value = CommonUtil.convertObjToStr(earnDeduPayTO.getAccNo());
                            List lstOA = sqlMap.executeQueryForList("getAccountClosingHeads", value);
                            if (lstOA != null && lstOA.size() > 0) {
                                debitMap = (HashMap) lstOA.get(0);
                            }
                        }
                        if (!earnDeduPayTO.getProdType().equals("") && earnDeduPayTO.getProdType().equals(TransactionFactory.SUSPENSE)) {
                            debitMap.put("prodId", earnDeduPayTO.getProdId());
                            lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                        }
                        if (!earnDeduPayTO.getProdType().equals("") && earnDeduPayTO.getProdType().equals(TransactionFactory.DEPOSITS)) {
                            debitMap.put("prodId", earnDeduPayTO.getProdId());
                            lst = sqlMap.executeQueryForList("getDepositClosingHeadsPayroll", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                        }

                        if (!earnDeduPayTO.getProdType().equals("") && earnDeduPayTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                            debitMap.put("PROD_ID", earnDeduPayTO.getProdId());
                            lst = sqlMap.executeQueryForList("getAccountHeadProdADHeadPayroll", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }

                        }
                        if (!earnDeduPayTO.getProdType().equals("") && earnDeduPayTO.getProdType().equals(TransactionFactory.OTHERBANKACTS)) {
                            debitMap.put("PROD_ID", earnDeduPayTO.getProdId());
                            lst = sqlMap.executeQueryForList("getAccountHeadProdForPayRoll", debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                            }
                        }
                        //transaction start 
                        if (map.get("TRANS_TYPE").equals(CommonConstants.CREDIT)) {
                            txMap = new HashMap();
                            System.out.println("before starting  " + transAmt);
                            if (transAmt > 0 && earnDeduPayTO.getAmount() > 0) {
                                if (earnDeduPayTO.getProdType().equals(TransactionFactory.GL)) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("ACC_HD")));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                } else {
                                    txMap.put(TransferTrans.CR_AC_HD, debitMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_ACT_NUM, earnDeduPayTO.getAccNo());
                                    txMap.put(TransferTrans.CR_PROD_TYPE, earnDeduPayTO.getProdType());
                                    txMap.put(TransferTrans.CR_PROD_ID, earnDeduPayTO.getProdId());
                                    if (earnDeduPayTO.getProdType().equals("TD") && getProductBehavesLike(earnDeduPayTO.getProdId()).equals("RECURRING")) {
                                        txMap.put(TransferTrans.CR_ACT_NUM, earnDeduPayTO.getAccNo());
                                        if (earnDeduPayTO.getAccNo().indexOf("_") != -1) {
                                            txMap.put(TransferTrans.CR_ACT_NUM, earnDeduPayTO.getAccNo());
                                        } else {
                                            txMap.put(TransferTrans.CR_ACT_NUM, earnDeduPayTO.getAccNo() + "_1");
                                        }
                                    }
                                }
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put("AUTHORIZEREMARKS", map.get("PAY_CODE_TYPE"));
                             //   txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                if(transactionTO.getParticulars().length() > 0){ // Added by nithya for 6094 on 29-03-2017
                                   txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());   
                                }else{
                                   txMap.put(TransferTrans.PARTICULARS,  "" + earnDeduPayTO.getEmployeeName()); 
                                }                                
                                txMap.put("LINK_BATCH_ID", earnDeduPayTO.getEmployeeId());
                                if (earnDeduPayTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.OTHERBANKACTS)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.OTHER_BANK_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.SUSPENSE)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.LOANS)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.DEPOSITS)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.DEPOSIT_TRANSMODE_TYPE);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                                }
                                txMap.put("GL_TRANS_ACT_NUM", earnDeduPayTO.getEmployeeId());
                                txMap.put("SCREEN_NAME","Payroll Individual");
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(earnDeduPayTO.getAmount())));
                                transAmt = transAmt - earnDeduPayTO.getAmount();
                                totalPayAmount = totalPayAmount + earnDeduPayTO.getAmount();
                            }
                        } else if (map.get("TRANS_TYPE").equals(CommonConstants.DEBIT)) {
                            txMap = new HashMap();
                            objTransferTrans.setInitiatedBranch(_branchCode);
                            if (transAmt > 0 && earnDeduPayTO.getAmount() > 0) {
                                if (earnDeduPayTO.getProdType().equals(TransactionFactory.GL)) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achdMap.get("ACC_HD")));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                } else {
                                    txMap.put(TransferTrans.DR_AC_HD, debitMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.DR_ACT_NUM, earnDeduPayTO.getAccNo());
                                    txMap.put(TransferTrans.DR_PROD_TYPE, earnDeduPayTO.getProdType());
                                    txMap.put(TransferTrans.DR_PROD_ID, earnDeduPayTO.getProdId());
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put("AUTHORIZEREMARKS", map.get("PAY_CODE_TYPE"));
                           //     txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                if(transactionTO.getParticulars().length() > 0){ // Added by nithya for 6094 on 29-03-2017
                                   txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());   
                                }else{
                                   txMap.put(TransferTrans.PARTICULARS, "" +earnDeduPayTO.getEmployeeName());
                                }                                
                                txMap.put("GL_TRANS_ACT_NUM", earnDeduPayTO.getEmployeeId());
                                txMap.put("SCREEN_NAME","Payroll Individual");
                                if (earnDeduPayTO.getProdType().equals(TransactionFactory.OPERATIVE)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.OTHERBANKACTS)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.OTHER_BANK_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.SUSPENSE)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.LOANS)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                                } else if (earnDeduPayTO.getProdType().equals(TransactionFactory.ADVANCES)) {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                                }
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, earnDeduPayTO.getAmount()));
                                transAmt = transAmt - earnDeduPayTO.getAmount();
                                totalPayAmount = totalPayAmount + earnDeduPayTO.getAmount();
                            }
                        }
                    }
                    if (map.get("TRANS_TYPE").equals(CommonConstants.CREDIT)) {
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        txMap = new HashMap();
                        if (transactionTO.getTransAmt().doubleValue() > 0 && totalPayAmount == transactionTO.getTransAmt()) {
                            if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals(TransactionFactory.GL)) {
                                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            } else {
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            if (transactionTO.getParticulars().length() > 0) { // Added by nithya for 6094 on 29-03-2017
                                txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());
                            } else {
                                  txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(map.get("PAY_CODE"))); 
                            }                          
                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                             txMap.put("SCREEN_NAME","Payroll Individual");
                           //txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            if (transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.OTHER_BANK_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.LOANS)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                            } else {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                            }

                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, transactionTO.getTransAmt().doubleValue()));
                        }
                    } else if (map.get("TRANS_TYPE").equals(CommonConstants.DEBIT)) {
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        txMap = new HashMap();
                        if (transactionTO.getTransAmt().doubleValue() > 0 && totalPayAmount == transactionTO.getTransAmt()) {
                            if (CommonUtil.convertObjToStr(transactionTO.getProductType()).equals(TransactionFactory.GL)) {
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            } else {
                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                            }
                            if(transactionTO.getParticulars().length() > 0){ // Added by nithya for 6094 on 29-03-2017
                              txMap.put(TransferTrans.PARTICULARS, transactionTO.getParticulars());   
                            }else{
                               // txMap.put(TransferTrans.PARTICULARS, earnDeduPayTO.getEmployeeName()); 
                            //   txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(map.get("PAY_CODE"))); 
                             txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(map.get("PAY_CODE")));  
                            }                            
                            txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                        //    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                             txMap.put("SCREEN_NAME","Payroll Individual");
                            if (transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.OTHERBANKACTS)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.OTHER_BANK_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.LOANS)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.LOAN_TRANSMODE_TYPE);
                            } else if (transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.ADVANCE_TRANSMODE_TYPE);
                            } else {
                                txMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                            }
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transactionTO.getTransAmt().doubleValue()));
                        }
                    }
                } else {
                    for (int i = 0; i < addTransactionList.size(); i++) {
                        EarnDeduPayTO earnDeduPayTO = (EarnDeduPayTO) tableDetails.get(addTransactionList.get(i));
                        HashMap PayrollTransMap = new HashMap();
                        if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()) > 0) {
                            PayrollTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                            PayrollTransMap.put("BRANCH_CODE", _branchCode);
                            PayrollTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            PayrollTransMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                            PayrollTransMap.put("INVESTMENT_NO", earnDeduPayTO.getPayCode());
                            if (earnDeduPayTO.getProdType().equals(TransactionFactory.GL)) {
                                PayrollTransMap.put("ACCT_HEAD", achdMap.get("ACC_HD"));
                                PayrollTransMap.put("PROD_TYPE", TransactionFactory.GL);
                                PayrollTransMap.put("TRANS_MOD_TYPE", CommonConstants.GL_TRANSMODE_TYPE);
                            }
                            //account level needed un coment this portion
//                    else {
//                        PayrollTransMap.put("ACCT_HEAD", debitMap.get("AC_HD_ID"));
//                        PayrollTransMap.put("ACCT_NO", earnDeduPayTO.getAccNo());
//                        PayrollTransMap.put("PROD_TYPE", earnDeduPayTO.getProdType());
//                        PayrollTransMap.put("PROD_ID", earnDeduPayTO.getProdId());
//                        PayrollTransMap.put("TRANS_MOD_TYPE", earnDeduPayTO.getProdType());
//                    }
                            PayrollTransMap.put("TRANS_TYPE", map.get("TRANS_TYPE"));
                            PayrollTransMap.put("TRANS_AMOUNT", earnDeduPayTO.getAmount());
                            PayrollTransMap.put("AUTHORIZEREMARKS", "DEDUCTIONS");
                            PayrollTransMap.put("LINK_BATCH_ID", earnDeduPayTO.getEmployeeId());
                            if(transactionTO.getParticulars().length() > 0){ // Added by nithya for 6094 on 29-03-2017                              
                              PayrollTransMap.put("PARTICULARS", transactionTO.getParticulars());
                            }else{
                              PayrollTransMap.put("PARTICULARS", "BY" + "" + earnDeduPayTO.getEmployeeName()); 
                            }                            
                            PayrollTransMap.put("SCREEN_NAME","Payroll Individual");
                            cashList.add(createCashDebitTransactionTO(PayrollTransMap));
                        }

                    }
                }
            }
            //To Transaction Posting
            if (transferList != null && transferList.size() > 0) {
                doDebitCredit(transactionTO, transferList, _branchCode, true);
            }
            if (cashList != null && cashList.size() > 0) {
                doCashTrans(transactionTO, cashList, _branchCode, true);
            }
            transferList = null;
            cashList = null;
            getTransDetails(CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
            returnDataMap.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(returnDataMap.get("SINGLE_TRANS_ID")));
        }
}
private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", CurrDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode); 
        String transID=CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID"));
        returnDataMap = new HashMap();
        returnDataMap.put("TRANS_ID",transID);
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

    private CashTransactionTO createCashDebitTransactionTO(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NO")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
            objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objCashTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
            objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("PARTICULARS")));
            objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.USER_ID)));
            objCashTO.setStatusDt(CurrDt);
            //objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            if (objCashTO.getTransType().equals(CommonConstants.DEBIT)) {
                objCashTO.setInstType("VOUCHER");
            }
            if (dataMap.containsKey("SCREEN_NAME") && dataMap.get("SCREEN_NAME") != null) {
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
            }
            objCashTO.setCommand("INSERT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("LINK_BATCH_ID")));
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private void doCashTrans(TransactionTO transactionTO, ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        returnDataMap = cashDAO.execute(data, false); 
        returnPfCashMap = new HashMap();
        returnPfCashMap.putAll(returnDataMap);
        HashMap map = new HashMap();
        String status = CommonConstants.STATUS_AUTHORIZED;
        ArrayList arrList = new ArrayList();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", status);
        singleAuthorizeMap.put("TRANS_ID", returnDataMap.get("TRANS_ID"));
        singleAuthorizeMap.put("USER_ID",logTO.getUserId());
        arrList.add(singleAuthorizeMap);
        map = new HashMap();
        map.put("SCREEN", "Cash");
        map.put("USER_ID", logTO.getUserId());
        map.put("SELECTED_BRANCH_ID", _branchCode);
        map.put("BRANCH_CODE", _branchCode);
        map.put("MODULE", "Transaction");
        map.put("SCREEN_NAME", "PAYROLL");
        HashMap dataMap = new HashMap();
        dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
        dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
        dataMap.put("DAILY", "DAILY");
        map.put(CommonConstants.AUTHORIZEMAP, dataMap);
        cashDAO.execute(map, false);
        if(CASHIER_AUTH_ALLOWED.equals("Y")){
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.USER_ID, logTO.getUserId());
        whereMap.put("TRANS_DT", CurrDt);
        whereMap.put(CommonConstants.TRANS_ID,returnDataMap.get("TRANS_ID"));
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        sqlMap.executeUpdate("approveCashier", whereMap);}
        //
        transactionTO.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setTransId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setBatchDt(CurrDt);
        transactionTO.setStatus(CommonConstants.TOSTATUS_INSERT);
        transactionTO.setBranchId(_branchCode);
        if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() > 0) {
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
        }
        transactionTO = null;
    }

    private void doDebitCredit(TransactionTO transactionTO, ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        data.put("DEBIT_LOAN_TYPE", "DP");//Added By Revathi.L reff By Mr.Abi
        //For auto authorization        
        returnDataMap = transferDAO.execute(data, false); 
        returnPfTransMap = new HashMap();
        returnPfTransMap.putAll(returnDataMap);
        transactionTO.setBatchId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setTransId(CommonUtil.convertObjToStr(returnDataMap.get("TRANS_ID")));
        transactionTO.setBatchDt(CurrDt);
        transactionTO.setStatus(CommonConstants.TOSTATUS_INSERT);
        transactionTO.setBranchId(_branchCode);
        if (CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue() > 0) {
            sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
        }
        transactionTO = null;
    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = transactionDAO.getLinkBatchID();
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                cashAuthMap.put("BATCH_ID",transMap.get("TRANS_ID"));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, cashAuthMap);
                cashAuthMap = null;
                transMap = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    
    
     private Date setProperDtFormat(Date dt) {   
        Date tempDt = (Date) CurrDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    
    
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (tableDetails != null) {
                ArrayList addList = new ArrayList(tableDetails.keySet());
                EarnDeduPayTO earnDeduPayTO = null;
                double recoveryAmount = 0.0;
                DateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                for (int i = 0; i < tableDetails.size(); i++) {
                    earnDeduPayTO = new EarnDeduPayTO();
                    earnDeduPayTO = (EarnDeduPayTO) tableDetails.get(addList.get(i));
                    earnDeduPayTO.setStatusDate(CurrDt);
                    System.out.println("objGroupLoanCustomerTO####" + earnDeduPayTO);
                    //recoveryAmount = CommonUtil.convertObjToDouble(earnDeduPayTO.getAmount());
                    //earnDeduPayTO.setAmount(Math.round(CommonUtil.convertObjToDouble(recoveryAmount / CommonUtil.convertObjToDouble(earnDeduPayTO.getRecovryMnth()))));
                    cal.setTime(earnDeduPayTO.getFromDate());
                    cal.add(Calendar.MONTH, CommonUtil.convertObjToInt(earnDeduPayTO.getRecovryMnth()));
                    earnDeduPayTO.setToDate(formatter.parse(formatter.format(cal.getTime())));
                    earnDeduPayTO.setCreatedDate(CurrDt);
                    if (earnDeduPayTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        earnDeduPayTO.setSrlNo(CommonUtil.convertObjToInt("1"));
                        sqlMap.executeUpdate("insertPayMaster", earnDeduPayTO);
                    } else {
                        earnDeduPayTO.setFromDate(setProperDtFormat(earnDeduPayTO.getFromDate())); //KD-3545
                        sqlMap.executeUpdate("updatePayMaster", earnDeduPayTO);
                    }
                }
            }
            if (deletedTableValues != null) {
                ArrayList addList = new ArrayList(deletedTableValues.keySet());
                EarnDeduPayTO earnDeduPayTO = null;
                for (int i = 0; i < deletedTableValues.size(); i++) {
                    earnDeduPayTO = new EarnDeduPayTO();
                    earnDeduPayTO = (EarnDeduPayTO) deletedTableValues.get(addList.get(i));
                    sqlMap.executeUpdate("deletePayMaster", earnDeduPayTO);
                }
            }

            sqlMap.commitTransaction();
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
            map.put("STATUS", CommonConstants.STATUS_DELETED);
            map.put("STATUS_DT", CurrDt);
            sqlMap.executeUpdate("deletePayMaster", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("######### map : " + map);
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap authMap = new HashMap();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
        transferDAO = new TransferDAO();
        double totalExcessTransAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            TransactionTO transactionTO = new TransactionTO();
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            totalExcessTransAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt());
            String linkBatchId = "";
            if (transactionTO.getTransType().equals(CommonConstants.TX_TRANSFER)) {
                if (totalExcessTransAmt > 0) {
                    HashMap transferTransParam = new HashMap();
                    transferDAO = new TransferDAO();
                    transferTransParam.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                    transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    transferTransParam.put("DEBIT_LOAN_TYPE", "DP");
                    transferTransParam.put("LINK_BATCH_ID", AuthorizeMap.get("ACT_NUM"));
                    ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
                    if (transferTransList != null) {
                        String batchId = ((TxTransferTO) transferTransList.get(0)).getBatchId();
                        HashMap transAuthMap = new HashMap();
                        transAuthMap.put("BATCH_ID", batchId);
                        transAuthMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        transAuthMap.put(CommonConstants.AUTHORIZEDATA, transferTransList);
                        transferTransParam.put(CommonConstants.AUTHORIZEMAP, transAuthMap);
                        transferDAO.execute(transferTransParam, false);
                    }
                }
            } else if (transactionTO.getTransType().equals(CommonConstants.TX_CASH)) {
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                HashMap singleAuthorizeMap = new HashMap();
                authorizeMap.put("LINK_BATCH_ID", AuthorizeMap.get("ACT_NUM"));
                authorizeMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                authorizeMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                authorizeMap.put("TRANS_DT", CurrDt);
                ArrayList cashTransList = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", authorizeMap);
                if (cashTransList != null) {
                    String transId = ((CashTransactionTO) cashTransList.get(0)).getTransId();
                    singleAuthorizeMap.put("AUTHORIZE_STATUS", status);
                    singleAuthorizeMap.put("STATUS", status);
                    singleAuthorizeMap.put("TRANS_ID", CommonUtil.convertObjToStr(transactionTO.getTransId()));
                    singleAuthorizeMap.put("USER_ID", map.get("USER_ID"));
                    singleAuthorizeMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                    singleAuthorizeMap.put("TRANS_ID", transId);
                    arrList.add(singleAuthorizeMap);
                    String branchCode = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                    String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
                    map = new HashMap();
                    map.put("SCREEN", "Cash");
                    map.put("USER_ID", userId);
                    map.put("SELECTED_BRANCH_ID", branchCode);
                    map.put("BRANCH_CODE", branchCode);
                    map.put("MODULE", "Transaction");
                    HashMap dataMap = new HashMap();
                    dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                    dataMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                    dataMap.put("DAILY", "DAILY");
                    map.put(CommonConstants.AUTHORIZEMAP, dataMap);
                    map.put("DEBIT_LOAN_TYPE", "DP");
                    System.out.println("before entering DAO map :" + map);
                    cashTransactionDAO.execute(map, false);
                    cashTransactionDAO = null;
                    dataMap = null;
                }
            }

        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        CurrDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private String getMaxSlNo(PayRollTo payRollTo){
        HashMap slMap = new HashMap();
        String slNo="";
        try{
            List slList = (List) sqlMap.executeQueryForList("getMaxSlNoFromPayRoll", payRollTo);
            if(slList!=null && slList.size()>0){
                slMap = (HashMap) slList.get(0);  
                slNo = CommonUtil.convertObjToStr(slMap.get("SL_NO"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return slNo;
    }
    
    
   private String getProductBehavesLike(String prodId) throws SQLException {
        String behavesLike = "";;
            HashMap prodIdMap = new HashMap();
            prodIdMap.put("PROD_ID", prodId);
            List behavesLikeList = sqlMap.executeQueryForList("getProductBehavesLike", prodIdMap);
            if (behavesLikeList != null && behavesLikeList.size() > 0) {
                prodIdMap = (HashMap) behavesLikeList.get(0);
                if (prodIdMap != null && prodIdMap.size() > 0 && prodIdMap.containsKey("BEHAVES_LIKE")) {
                    behavesLike = CommonUtil.convertObjToStr(prodIdMap.get("BEHAVES_LIKE"));

                }
            }        
        return behavesLike;
    }
    
    
    
    private void destroyObjects() {
        earnDeduPayTO = null;
        payrollMap = null;
        transactionDAO = null;
        //returnDataMap.clear();

    }
}
