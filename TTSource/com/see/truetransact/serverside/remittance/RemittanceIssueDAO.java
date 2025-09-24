/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueDAO.java
 *
 * Created on Fri Jan 09 17:36:32 IST 2004
 */
package com.see.truetransact.serverside.remittance;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.transferobject.common.log.*;
import com.see.truetransact.serverside.common.log.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.commonutil.TTException;
import java.util.Date;
/**
 * @author Prasath.T
 *
 */
public class RemittanceIssueDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String operationMode = null;
    TransferTrans objTrans = new TransferTrans();
    private LogDAO logDAO;
    private LogTO logTO;
    private RemittanceIssueTO objRemittanceIssueTO;
    private RemittanceIssueTO objRemitDupIssueTO;
    private RemittanceIssueTO objRemitRevIssueTO;
    private TransactionDAO transactionDAO = null;
    private LinkedHashMap issueMap; // Contains all the Issue Details TOs
    private LinkedHashMap deletedIssueTOs;
    private LinkedHashMap allowedIssueTOs;
    private LinkedHashMap duplicateIssueTOs;
    LinkedHashMap newAllowedTransDetailsTO;
    //    private TransferDAO transferDAO;
    // Auto Generated values which is not present in UI
    private String batchId = "";// batchId is used to store batch id used for both Issue & Transaction details
    private String issueId = "";// issueId is used to store issue id for Issue details
    private String variableNo = "";// variableNo is used to store the variable no in Issue details
    private String newVariableNo = "";
    private String instNo1 = "";
    private String instNo2 = "";
    private String status = "";
    private boolean fromotherDAo = true;
    private boolean isNewMode = false;
    private Date batchDate;// batchDate is used to store the date of batch created
    private HashMap remitIssueTransMap;
    // Used for Transfer Issue amount to Payment Head
    private ArrayList transferRemitPayList = null;
    private String branchRemitPay = null;
    private TransactionTO objTransactionTO;
    private TransactionTO objDupTransactionTO;
    private TransactionTO objRevTransactionTO;
    private boolean NoDupCharge = false;
    private boolean NoRevCharge = false;
    private boolean DupEditMode = false;
    private boolean DupDeleteMode = false;
    private boolean RevEditMode = false;
    private boolean RevDeleteMode = false;
    private boolean DupInsertUpdateMode = false;
    private boolean RevInsertUpdateMode = false;
    private Date currDt = null;
    /**
     * Creates a new instance of RemittanceIssueDAO
     */
    public RemittanceIssueDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    /*
     * To retrive the data from the database (table REMIT_ISSUE)
     */

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        //changes made by vinay
        String data = (String) map.get(CommonConstants.MAP_WHERE);
        String isDuplicate = (String) map.get("REMARKS");
        String isRevalidate = (String) map.get("REMARKS");
        HashMap returnMap = new HashMap();
        HashMap tempReturnMap = new HashMap();
        List tempLst = null;
        List list = null;
        List tmplst = null;
        List listData = null;
        List lst = null, lst2 = null, lst3 = null, lst4 = null, lst5 = null, lst6 = null, lst7 = null, lst8 = null, lst9 = null;
        HashMap where = new HashMap();
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        TransactionTO dataTO = new TransactionTO();
        String batchID = "";

        List lstData = null;
        RemittanceIssueTO objRemitIssueTO;
        if (map.containsKey(CommonConstants.MAP_NAME)) {
            String mapName = (String) map.get(CommonConstants.MAP_NAME);
            lstData = (List) sqlMap.executeQueryForList(mapName, where);
            returnMap.put("DOCUMENT_LIST", lstData);
            //System.out.println("### returnMap : "+returnMap);
        } else {
            HashMap tempMap;
            listData = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTO", data);
            ArrayList arrayList = new ArrayList();
            if (listData != null) {
                if (listData.size() > 0) {
                    returnMap.put("RemittanceIssueTO", listData);
                }
            }
            for (int i = 0; i < listData.size(); i++) {
                tempMap = new HashMap();
                objRemitIssueTO = (RemittanceIssueTO) listData.get(i);
                System.out.println("@@@@@@@objRemitIssueTO" + objRemitIssueTO);
                list = transactionDAO.getData(map);
                if (list.size() == 1) {
                    dataTO = (TransactionTO) list.get(0);
                } else {
                    dataTO = (TransactionTO) list.get(i);
                }
                if (isDuplicate != null) {
                    if ((isDuplicate.equals(CommonConstants.REMIT_DUPLICATE)) && (list != null) && (list.size() > 0)) {
                        System.out.println("isDuplicate" + isDuplicate);
                        dataTO = (TransactionTO) list.get(list.size() - 1);
                        isDuplicate = null;
                    }
                }

                if (isRevalidate != null) {
                    if ((isRevalidate.equals("REVALIDATED")) && (list != null) && (list.size() > 0)) {
                        System.out.println("isRevalidate" + isRevalidate);
                        dataTO = (TransactionTO) list.get(list.size() - 1);
                        isRevalidate = null;
                    }
                }

                if (dataTO.getTransType().equals("CASH")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objRemitIssueTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", objRemitIssueTO.getAmount());
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            //System.out.println("@@@@@@@amount"+amount);
                            getTransMap.put("LINK_BATCH_ID", objRemitIssueTO.getVariableNo());
                            getTransMap.put("TODAY_DT", currDt.clone());
                            getTransMap.put("INITIATED_BRANCH", _branchCode);
                            if (amount > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("ISSUE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                System.out.println("@@@@@@@lst1*******" + lst);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("AMT_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("EXCHANGE", objRemitIssueTO.getExchange());
                            //System.out.println("@@@@@@***@dataMap"+dataMap);
                            double exchange = CommonUtil.convertObjToDouble(dataMap.get("EXCHANGE")).doubleValue();
                            if (exchange > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("EXCHANGE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("EXCHANGE"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("EXH_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("OTHER_CHARGES", objRemitIssueTO.getOtherCharges());
                            double othercharges = CommonUtil.convertObjToDouble(dataMap.get("OTHER_CHARGES")).doubleValue();
                            if (othercharges > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("OTHER_CHARGES"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("OTHERCHRG_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("POSTAGE", objRemitIssueTO.getPostage());
                            double postage = CommonUtil.convertObjToDouble(dataMap.get("POSTAGE")).doubleValue();
                            if (postage > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("POSTAGE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("POSTAGE"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("POSTAGE_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("DUP_CHARGE", objRemitIssueTO.getDuplicateCharge());
                            double dupCharge = CommonUtil.convertObjToDouble(dataMap.get("DUP_CHARGE")).doubleValue();
                            if (dupCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("DUPL_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("DUP_CHARGE"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("DUP_CHARGE_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("DUP_SERV_TAX", objRemitIssueTO.getDupServTax());
                            double dupServCharge = CommonUtil.convertObjToDouble(dataMap.get("DUP_SERV_TAX")).doubleValue();
                            if (dupServCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("DUP_SERV_TAX"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("DUP_SERV_TAX_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("REV_CHARGE", objRemitIssueTO.getRevalidateCharge());
                            double revCharge = CommonUtil.convertObjToDouble(dataMap.get("REV_CHARGE")).doubleValue();
                            if (revCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("REVAL_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("REV_CHARGE"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("REV_CHARGE_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("REV_SERV_TAX", objRemitIssueTO.getRevServTax());
                            double revServCharge = CommonUtil.convertObjToDouble(dataMap.get("REV_SERV_TAX")).doubleValue();
                            if (revServCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("REV_SERV_TAX"));
                                lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("REV_SERV_TAX_CASH_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }
                        }
                    }
                }
                if (dataTO.getTransType().equals("TRANSFER")) {
                    if (returnMap != null) {
                        if (returnMap.size() > 0) {
                            HashMap dupMap = new HashMap();
                            dupMap.put("DUP_CHRG", objRemitIssueTO.getDuplicateCharge());
                            dupMap.put("DUP_SERV_TX", objRemitIssueTO.getDupServTax());
                            double dupChrg = CommonUtil.convertObjToDouble(dupMap.get("DUP_CHRG")).doubleValue();
                            double dupSerTx = CommonUtil.convertObjToDouble(dupMap.get("DUP_SERV_TX")).doubleValue();
                            double totDupAmt = dupChrg + dupSerTx;
                            dupMap = null;

                            HashMap revMap = new HashMap();
                            revMap.put("REV_CHRG", objRemitIssueTO.getRevalidateCharge());
                            revMap.put("REV_SERV_TX", objRemitIssueTO.getRevServTax());
                            double revChrg = CommonUtil.convertObjToDouble(revMap.get("REV_CHRG")).doubleValue();
                            double revSerTx = CommonUtil.convertObjToDouble(revMap.get("REV_SERV_TX")).doubleValue();
                            double totRevAmt = revChrg + revSerTx;
                            revMap = null;

                            HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objRemitIssueTO.getProdId());
                            HashMap getTransMap = new HashMap();
                            dataMap.put("AMOUNT", objRemitIssueTO.getAmount());
                            double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                            getTransMap.put("LINK_BATCH_ID", objRemitIssueTO.getVariableNo());
                            getTransMap.put("TODAY_DT", currDt.clone());
                            getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                            if (amount > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("ISSUE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("AMT_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }

                                lst = null;
                            }

                            dataMap.put("EXCHANGE", objRemitIssueTO.getExchange());
                            System.out.println("@@@@@@***@dataMap" + dataMap);
                            double exchange = CommonUtil.convertObjToDouble(dataMap.get("EXCHANGE")).doubleValue();
                            if (exchange > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("EXCHANGE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("EXCHANGE"));

                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("EXH_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("OTHER_CHARGES", objRemitIssueTO.getOtherCharges());
                            double othercharges = CommonUtil.convertObjToDouble(dataMap.get("OTHER_CHARGES")).doubleValue();
                            if (othercharges > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("OTHER_CHARGES"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("OTHERCHRG_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }
                            dataMap.put("POSTAGE", objRemitIssueTO.getPostage());
                            double postage = CommonUtil.convertObjToDouble(dataMap.get("POSTAGE")).doubleValue();
                            if (postage > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("POSTAGE_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("POSTAGE"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("POSTAGE_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("DUP_CHARGE", objRemitIssueTO.getDuplicateCharge());
                            double dupCharge = CommonUtil.convertObjToDouble(dataMap.get("DUP_CHARGE")).doubleValue();
                            if (dupCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("DUPL_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("DUP_CHARGE"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("DUP_CHARGE_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("DUP_SERV_TAX", objRemitIssueTO.getDupServTax());
                            double dupServTax = CommonUtil.convertObjToDouble(dataMap.get("DUP_SERV_TAX")).doubleValue();
                            if (dupServTax > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("DUP_SERV_TAX"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("DUP_SERV_TAX_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("REV_CHARGE", objRemitIssueTO.getRevalidateCharge());
                            double revCharge = CommonUtil.convertObjToDouble(dataMap.get("REV_CHARGE")).doubleValue();
                            if (revCharge > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("REVAL_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("REV_CHARGE"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("REV_CHARGE_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            dataMap.put("REV_SERV_TAX", objRemitIssueTO.getRevServTax());
                            double revServTax = CommonUtil.convertObjToDouble(dataMap.get("REV_SERV_TAX")).doubleValue();
                            if (revServTax > 0) {
                                getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                                getTransMap.put("AMOUNT", dataMap.get("REV_SERV_TAX"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("REV_SERV_TAX_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }
                                lst = null;
                            }

                            getTransMap.remove("AC_HD_ID");
                            dataMap.put("TOTAL_AMT", objRemitIssueTO.getTotalAmt());
                            double tltamt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_AMT")).doubleValue();
                            if (tltamt > 0) {
                                getTransMap.put("AMOUNT", dataMap.get("TOTAL_AMT"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }

                                lst = null;
                            }
                            dataMap.put("TOTAL_DUP_AMT", new Double(totDupAmt));
                            double tltDupAmt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DUP_AMT")).doubleValue();
                            if (tltDupAmt > 0) {
                                getTransMap.put("AMOUNT", dataMap.get("TOTAL_DUP_AMT"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("TOTAL_DUP_AMT_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }

                                lst = null;
                            }
                            dataMap.put("TOTAL_REV_AMT", new Double(totRevAmt));
                            double tltRevAmt = CommonUtil.convertObjToDouble(dataMap.get("TOTAL_REV_AMT")).doubleValue();
                            if (tltRevAmt > 0) {
                                getTransMap.put("AMOUNT", dataMap.get("TOTAL_REV_AMT"));
                                lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                                if (lst != null) {
                                    if (lst.size() > 0) {
                                        tempMap.put("TOTAL_REV_AMT_TRANSFER_TRANS_DETAILS", lst.get(0));
                                    }
                                }

                                lst = null;
                            }
                        }
                    }
                }
                tempReturnMap.put(objRemitIssueTO.getVariableNo(), tempMap);
                objRemitIssueTO = null;
            }
        }
        returnMap.put("TransactionDetails", tempReturnMap);
        returnMap.put("TransactionTO", list);
        System.out.println(">>> returnMap : " + returnMap);
        return returnMap;
    }

    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objRemittanceIssueTO = null;
        issueMap = null;
        deletedIssueTOs = null;
        allowedIssueTOs = null;
        transactionDAO = null;
        remitIssueTransMap = null;
    }

    /**
     * Generates BatchId
     */
    private String getBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BATCH_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * Generates variableNo for issue details
     */
    private String getVariableNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REMITISSUE.VARIABLE_NO");
        return getPrefixSuffix((String) (dao.executeQuery(where)).get(CommonConstants.DATA));
    }

    private void setInstrumentNo() throws Exception {
        HashMap map;
        double start = 0;
        double end = 0;
        HashMap whereMap = new HashMap();
        HashMap where = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
        whereMap.put("PROD_ID", objRemittanceIssueTO.getProdId());
        //System.out.println("whereMap*****"+whereMap);
        List lst = sqlMap.executeQueryForList("getBehavesLikeFromRemitProduct", whereMap);
        //System.out.println("$$$$$$$*&*&& List : " +  lst);

        if (lst.size() > 0) {
            where = (HashMap) lst.get(0);
            whereMap.put("BEHAVES_LIKE", where.get("BEHAVES_LIKE"));
            //System.out.println("$$$$$$$*&*&& whereMap : " +  whereMap);
            lst = null;
        }
        lst = sqlMap.executeQueryForList("getInstrumentNo", whereMap);
        whereMap = null;
        //System.out.println("$$$$$$$$$$ List : " +  lst);
        int lstSize = lst.size();
        if (lstSize > 0) {
            map = (HashMap) lst.get(0);
            //System.out.println("@@@@@@@@@ map : " +  map);
            setInstNo1(CommonUtil.convertObjToStr(map.get("INS1")));
            setInstNo2(CommonUtil.convertObjToStr(map.get("INS2")));
            //System.out.println("getInstNo2 : " + getInstNo2());
        } else {
            throw new TTException("Inventory not set up");
        }
    }

    // To get the prefix and suffix from the table REMITTANCE_PRODUCT
    private String getPrefixSuffix(String autoValue) throws Exception {
        final HashMap where = new HashMap();
        final StringBuffer pattern = new StringBuffer();
        where.put("PROD_ID", objRemittanceIssueTO.getProdId());
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

    /*
     * To Generate the auto generated field issue id at the time of insertion
     */
    private String getIssueId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ISSUE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * Sets the autogenerated fields in the Remit Issue Details TO
     */
    private void setAutoGeneratedFieldsForIssueTOs() throws Exception {
        objRemittanceIssueTO.setBatchDt(batchDate = currDt);
        objRemittanceIssueTO.setBatchId(batchId);
        objRemittanceIssueTO.setIssueId(issueId);

        objRemittanceIssueTO.setVariableNo(variableNo);
//        objRemittanceIssueTO.setInstrumentNo1(getInstNo1());
//        objRemittanceIssueTO.setInstrumentNo2(getInstNo2());
    }

    private void doAccountHeadCredit(HashMap obj, String command, HashMap txMap, String branchID, HashMap tempHashMap) throws Exception {

        //changes made by vinay
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objRemittanceIssueTO.getProdId());
        System.out.println("Ac heads in doAccountHeadCredit : " + acHeads);
        System.out.println("operationMode : " + operationMode);
        System.out.println("command" + command);
        txMap = createMap(txMap, objRemittanceIssueTO, branchID);
        double totDupAmount = 0.0;

        if (operationMode.equals(CommonConstants.REMIT_ISSUE)) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertRemitIssueDetails(command);
                transactionDAO.setBatchId(batchId);
                transactionDAO.setBatchDate(batchDate = currDt);
                transactionDAO.setLinkBatchID(variableNo);
            }
            if (!fromotherDAo) {
                String fromBills = "";
                fromBills = CommonUtil.convertObjToStr(objRemittanceIssueTO.getAuthorizeRemark());
                if ((fromBills.length() > 0) && (fromBills.equals("FROM_BILLS_MODULE"))) {
                    objTransactionTO.setTransType("");
                    operationMode = "";
                }
            }

            if (objTransactionTO.getTransType().equals("TRANSFER")) {
                System.out.println("##### TRANSFER MODE.........");
                double transAmt;
                TxTransferTO transferTo;
                ArrayList transferList = new ArrayList(); // for local transfer
                Double tempAmt = objRemittanceIssueTO.getTotalAmt();

                //edit mode
                if (tempHashMap.containsKey("TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getTotalAmt()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();

                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null && lst.size() > 0) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                            } else {
                                txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            }
                            txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                            txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                            oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                            batchList.add(txTransferTO);
                            TransferTrans transferTrans = new TransferTrans();
                            transferTrans.setOldAmount(oldAmountMap);
                            transferTrans.setInitiatedBranch(branchID);
                            transferTrans.doDebitCredit(batchList, branchID, false, command);
                            lst = null;
                            transferTrans = null;
                        }
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setTotalAmt(new Double(0));
                }

                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getTotalAmt()).doubleValue() > 0) {
                    isNewMode = true;
                    int newSize = newAllowedTransDetailsTO.size();
                    System.out.println("!!!!!!newSize" + newSize);
                    for (int i = 1; i <= newSize; i++) {
                        System.out.println("!!!!!!newAllowedTransDetailsTO" + newAllowedTransDetailsTO);
                        objTransactionTO = (TransactionTO) newAllowedTransDetailsTO.get(String.valueOf(i));
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        if (!objTransactionTO.getProductType().equals("GL")) {
                            txMap.put(TransferTrans.DR_AC_HD, null);
                            txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());
                            txMap.put(TransferTrans.DR_PROD_ID, objTransactionTO.getProductId());
                        } else {
                            txMap.put(TransferTrans.DR_AC_HD, objTransactionTO.getDebitAcctNo());
                        }
//                    txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());// Debit deposit interest account head......
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
//                    txMap.put(TransferTrans.DR_PROD_ID, objTransactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, objTransactionTO.getProductType());
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ISSUE_HD")); // credit to Remittance Issue account head......
//                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getTotalAmt()).doubleValue(); //changed 21-oct-09
                        transAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                        transferList.add(transferTo);

                        txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objTransactionTO.getDebitAcctNo());
                    }
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    transferList.add(transferTo);
                    transAmt = 0;
//                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);//now added
//                    transactionDAO.doTransferLocal(transferList, branchID);//now added
                    if (objTransactionTO.getProductType().equals("TL") || objTransactionTO.getProductType().equals("ATL")) {
                        transactionDAO.setLoanDebitInt("DP");
                    }

                    transactionDAO.setCommandMode(command);
                }
                objRemittanceIssueTO.setTotalAmt(tempAmt);

                tempAmt = objRemittanceIssueTO.getAmount();
                //edit mode
                if (tempHashMap.containsKey("AMT_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("AMT_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setAmount(new Double(0));
                }
                objRemittanceIssueTO.setAmount(tempAmt);

                tempAmt = objRemittanceIssueTO.getExchange();
                //edit mode
                if (tempHashMap.containsKey("EXH_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("EXH_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
//                        transferTrans.setLoanDebitInt("DP");
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setExchange(new Double(0));
                }

                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue() > 0) {
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue();
                    if (transAmt > 0) {
                        isNewMode = true;
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("EXCHANGE_HD"));  // credit to Exchange Charge account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Comm & Exchnage" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferList.add(transferTo);
//                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);//now added
//                        transactionDAO.doTransferLocal(transferList, branchID);//now added
                        transactionDAO.setCommandMode(command);
                    }
                }
                transAmt = 0;
                objRemittanceIssueTO.setExchange(tempAmt);

                tempAmt = objRemittanceIssueTO.getOtherCharges();
                //edit mode
                if (tempHashMap.containsKey("OTHERCHRG_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("OTHERCHRG_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setOtherCharges(new Double(0));
                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue() > 0) {
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue();
                    if (transAmt > 0) {
                        isNewMode = true;
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Other Charges account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferList.add(transferTo);
//                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);//now added
//                        transactionDAO.doTransferLocal(transferList, branchID);//now added
                        transactionDAO.setCommandMode(command);
                    }
                }
                transAmt = 0;
                objRemittanceIssueTO.setOtherCharges(tempAmt);

                tempAmt = objRemittanceIssueTO.getPostage();
                //edit mode
                if (tempHashMap.containsKey("POSTAGE_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("POSTAGE_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue();
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setPostage(new Double(0));
                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue() > 0) {
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue();
                    if (transAmt > 0) {
                        isNewMode = true;
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_HD"));  // credit to Postage Charge account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Postage" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                        transferList.add(transferTo);
//                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);//now added
//                        transactionDAO.doTransferLocal(transferList, branchID);//now added
                        transactionDAO.setCommandMode(command);
                    }
                }
                objRemittanceIssueTO.setPostage(tempAmt);

                //DUP EDIT
                if (tempHashMap.containsKey("DUP_CHARGE_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("DUP_CHARGE_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDuplicateCharge()).doubleValue();
                    totDupAmount = newAmount;
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setDuplicateCharge(new Double(0));
                }

                if (tempHashMap.containsKey("DUP_SERV_TAX_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("DUP_SERV_TAX_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDupServTax()).doubleValue();
                    totDupAmount = totDupAmount + newAmount;
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setDupServTax(new Double(0));
                }

                if (tempHashMap.containsKey("TOTAL_DUP_AMT_TRANSFER_TRANS_DETAILS")) {

                    HashMap tempMap = (HashMap) tempHashMap.get("TOTAL_DUP_AMT_TRANSFER_TRANS_DETAILS");
                    tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    tempMap.put("TRANS_DT", currDt.clone());
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = totDupAmount;
//                    totDupAmount = totDupAmount + newAmount;
                    ArrayList batchList = new ArrayList();
                    TxTransferTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                        if (lst != null) {
                            txTransferTO = (TxTransferTO) lst.get(0);
                        }
                        txTransferTO.setInpAmount(new Double(newAmount));
                        txTransferTO.setAmount(new Double(newAmount));
                        if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        } else {
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                        txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                        txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                        oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                        batchList.add(txTransferTO);
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setOldAmount(oldAmountMap);
                        transferTrans.setInitiatedBranch(branchID);
                        transferTrans.doDebitCredit(batchList, branchID, false, command);
                        lst = null;
                        transferTrans = null;
                    }
                    txTransferTO = null;
                    batchList = null;
                    oldAmountMap = null;
                    tempMap = null;
                    totDupAmount = 0.0;
                }

                if ((command.equals(CommonConstants.TOSTATUS_INSERT)) || (isNewMode == true)) {
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, branchID);
                    isNewMode = false;
                }

            } else if (objTransactionTO.getTransType().equals("CASH")) {
                System.out.println("##### CASH MODE.........");
                double transAmt;
                TransactionTO transTO = new TransactionTO();
                ArrayList cashList = new ArrayList();

                //edit mode
                Double tempAmount = objRemittanceIssueTO.getAmount();
                if (tempHashMap.containsKey("AMT_CASH_TRANS_DETAILS")) {
                    System.out.println("objRemittanceIssueTO^^^^^^^" + obj);
                    HashMap tempMap = (HashMap) tempHashMap.get("AMT_CASH_TRANS_DETAILS");
                    tempMap.put("TRANS_DT", currDt.clone());
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
                    CashTransactionTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            txTransferTO.setCommand(command);
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                            txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                            obj.put("PRODUCTTYPE", TransactionFactory.GL);
                            obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            obj.put("CashTransactionTO", txTransferTO);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(obj, false);
//                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setAmount(new Double(0));
                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue() > 0) {
                    System.out.println("line no 753^^^^^^^");
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ISSUE_HD"));  // credit to Exchange Charge account head......
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getAmount()).doubleValue();
                    txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                    transactionDAO.addTransferCredit(txMap, transAmt);
                    transactionDAO.deleteTxList();
                    transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    transTO.setTransType("CASH");
                    transTO.setBatchId(variableNo);
                    transTO.setTransAmt(new Double(transAmt));
                    cashList.add(transTO);
                    transactionDAO.addCashList(cashList);
                    transactionDAO.doTransfer();
                    transactionDAO.setCommandMode(command);
                }
                objRemittanceIssueTO.setAmount(tempAmount);

                tempAmount = objRemittanceIssueTO.getExchange();
                //edit mode
                if (tempHashMap.containsKey("EXH_CASH_TRANS_DETAILS")) {
                    System.out.println("line no 773^^^^^^^");
                    HashMap tempMap = (HashMap) tempHashMap.get("EXH_CASH_TRANS_DETAILS");
                    tempMap.put("TRANS_DT", currDt.clone());
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue();
                    CashTransactionTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            txTransferTO.setCommand(command);
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                            txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                            obj.put("PRODUCTTYPE", TransactionFactory.GL);
                            obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            obj.put("CashTransactionTO", txTransferTO);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(obj, false);
//                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setExchange(new Double(0));

                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue() > 0) {
                    System.out.println("line no 806 new mode^^^^^^^");
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getExchange()).doubleValue();
                    if (transAmt > 0) {
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("EXCHANGE_HD"));  // credit to Exchange Charge account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Comm & Exchnage" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transactionDAO.addTransferCredit(txMap, transAmt);
                        transactionDAO.deleteTxList();
//                        if(command.equals(CommonConstants.TOSTATUS_UPDATE))
                        transTO.setBatchId(objRemittanceIssueTO.getVariableNo());
                        transTO.setTransAmt(new Double(transAmt));
                        cashList = new ArrayList();
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                        transactionDAO.setCommandMode(command);
                    }
                }
                objRemittanceIssueTO.setExchange(tempAmount);

                tempAmount = objRemittanceIssueTO.getOtherCharges();
                //edit mode
                if (tempHashMap.containsKey("OTHERCHRG_CASH_TRANS_DETAILS")) {
                    System.out.println("OTHERCHRG_CASH_TRANS_DETAILS^^^^^^^");
                    HashMap tempMap = (HashMap) tempHashMap.get("OTHERCHRG_CASH_TRANS_DETAILS");
                    tempMap.put("TRANS_DT", currDt.clone());
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue();
                    CashTransactionTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            txTransferTO.setCommand(command);
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                            txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                            obj.put("PRODUCTTYPE", TransactionFactory.GL);
                            obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            obj.put("CashTransactionTO", txTransferTO);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(obj, false);
//                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setOtherCharges(new Double(0));
                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue() > 0) {
                    System.out.println("line no 860^^^^^^^");
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getOtherCharges()).doubleValue();
                    if (transAmt > 0) {
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Exchange Charge account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Comm & Exchange Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transactionDAO.addTransferCredit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setBatchId(objRemittanceIssueTO.getVariableNo());
                        transTO.setTransAmt(new Double(transAmt));
                        cashList = new ArrayList();
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                        transactionDAO.setCommandMode(command);
                    }
                }
                objRemittanceIssueTO.setOtherCharges(tempAmount);

                tempAmount = objRemittanceIssueTO.getPostage();
                //edit mode
                if (tempHashMap.containsKey("POSTAGE_CASH_TRANS_DETAILS")) {
                    System.out.println("POSTAGE_CASH_TRANS_DETAILS^^^^^^^");
                    HashMap tempMap = (HashMap) tempHashMap.get("POSTAGE_CASH_TRANS_DETAILS");
                    tempMap.put("TRANS_DT", currDt.clone());
                    tempMap.put("INITIATED_BRANCH", _branchCode);
                    double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                    double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue();
                    CashTransactionTO txTransferTO = null;
                    HashMap oldAmountMap = new HashMap();
                    if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                        if (lst != null) {
                            txTransferTO = (CashTransactionTO) lst.get(0);
                            txTransferTO.setInpAmount(new Double(newAmount));
                            txTransferTO.setAmount(new Double(newAmount));
                            txTransferTO.setCommand(command);
                            txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                            txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                            obj.put("PRODUCTTYPE", TransactionFactory.GL);
                            obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                            obj.put("CashTransactionTO", txTransferTO);
                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                            cashTransDAO.execute(obj, false);
//                            txMap = null;
                        }
                        lst = null;
                    }
                    txTransferTO = null;
                    oldAmountMap = null;
                    tempMap = null;
                    objRemittanceIssueTO.setPostage(new Double(0));
                }
                //new mode
                if (CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue() > 0) {
                    System.out.println("line no 913 new^^^^^^^");
                    transAmt = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getPostage()).doubleValue();
                    if (transAmt > 0) {
                        //                                    System.out.println("$$$$$acHeads"+acHeads);
                        //                                    System.out.println("$$$$$txMap"+txMap);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_HD"));  // credit to Exchange Charge account head......
                        txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Postage Chrg" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                        transactionDAO.addTransferCredit(txMap, transAmt);
                        transactionDAO.deleteTxList();
                        transTO.setBatchId(objRemittanceIssueTO.getVariableNo());
                        transTO.setTransAmt(new Double(transAmt));
                        cashList = new ArrayList();
                        cashList.add(transTO);
                        transactionDAO.addCashList(cashList);
                        transactionDAO.doTransfer();
                        transactionDAO.setCommandMode(command);
                    }
                }
                objRemittanceIssueTO.setPostage(tempAmount);
                System.out.println("##### FIXED CASH PAYABLE.........");
            } else {
                //DO NOTHING
            }

        } else if (operationMode.equals(CommonConstants.REMIT_REVALIDATE)) {
//            System.out.println("operationMode : " + operationMode) ;
//            if(objRemittanceIssueTO.getRevalidateCharge() != null){
//                if(CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevalidateCharge()).doubleValue() != 0){
//                    txMap.put(TransferTrans.CR_AC_HD, (String)acHeads.get("REVAL_CHRG_HD"));
//                    transactionDAO.addTransferCredit(txMap, CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevalidateCharge()).doubleValue()) ;
//                }
//            }
            if (NoRevCharge == false) {
                if (RevEditMode || RevDeleteMode) {
                    System.out.println("######@@@@#!!!!@objTransactionTO" + objTransactionTO);
                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                        //edit

                        if (tempHashMap.containsKey("REV_CHARGE_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("REV_CHARGE_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevalidateCharge()).doubleValue();
                            totDupAmount = newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            //                    if(objRemitDupIssueTO != null){
                            //                    objRemitDupIssueTO.setDuplicateCharge(new Double(0));
                            //                    }
                        }
                        //edit
                        if (tempHashMap.containsKey("REV_SERV_TAX_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("REV_SERV_TAX_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevServTax()).doubleValue();
                            totDupAmount = totDupAmount + newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            //                    if(objRemitDupIssueTO != null){
//                        objRemitDupIssueTO.setDupServTax(new Double(0));
                            //                    }
                        }
                        if (tempHashMap.containsKey("TOTAL_REV_AMT_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("TOTAL_REV_AMT_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = totDupAmount;
                            //                    totDupAmount = totDupAmount + newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            totDupAmount = 0.0;
                        }
                    }
                    if (objTransactionTO.getTransType().equals("CASH")) {

                        if (tempHashMap.containsKey("REV_CHARGE_CASH_TRANS_DETAILS")) {
                            System.out.println("REV_CHARGE_CASH_TRANS_DETAILS^^^^^^^");
                            HashMap tempMap = (HashMap) tempHashMap.get("REV_CHARGE_CASH_TRANS_DETAILS");
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevalidateCharge()).doubleValue();
                            CashTransactionTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                                if (lst != null) {
                                    txTransferTO = (CashTransactionTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    txTransferTO.setCommand(command);
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                                    obj.put("PRODUCTTYPE", TransactionFactory.GL);
                                    obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                                    obj.put("CashTransactionTO", txTransferTO);
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(obj, false);
                                }
                                lst = null;
                            }
                            txTransferTO = null;
                            oldAmountMap = null;
                            tempMap = null;
//                            objRemittanceIssueTO.setOtherCharges(new Double(0));
                        }
                        if (tempHashMap.containsKey("REV_SERV_TAX_CASH_TRANS_DETAILS")) {
                            System.out.println("REV_SERV_TAX_CASH_TRANS_DETAILS^^^^^^^");
                            HashMap tempMap = (HashMap) tempHashMap.get("REV_SERV_TAX_CASH_TRANS_DETAILS");
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getRevServTax()).doubleValue();
                            CashTransactionTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                                if (lst != null) {
                                    txTransferTO = (CashTransactionTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    txTransferTO.setCommand(command);
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                                    obj.put("PRODUCTTYPE", TransactionFactory.GL);
                                    obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                                    obj.put("CashTransactionTO", txTransferTO);
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(obj, false);
                                }
                                lst = null;
                            }
                            txTransferTO = null;
                            oldAmountMap = null;
                            tempMap = null;
//                            objRemittanceIssueTO.setOtherCharges(new Double(0));
                        }
                    }
                } else {
                    System.out.println("##### TRANSFER REMIT_revalidate.........");
                    double transAmt;
                    TxTransferTO transferTo;
                    //                 Double tempAmt = null;
                    ArrayList transferList = new ArrayList(); // for local transfer
                    //                 if(objRemitDupIssueTO != null){
                    Double tempAmt = objRemitRevIssueTO.getRevalidateCharge();
                    //                 }
                    System.out.println("operationMode : " + operationMode);
                    if (objRevTransactionTO.getTransType().equals("TRANSFER")) {
                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevalidateCharge()).doubleValue() > 0) {
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            if (!objRevTransactionTO.getProductType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objRevTransactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, objRevTransactionTO.getProductId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objRevTransactionTO.getDebitAcctNo());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objRevTransactionTO.getProductType());
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("REVAL_CHRG_HD")); // credit to Remittance Issue account head......
                            transAmt = ((CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevalidateCharge()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevServTax()).doubleValue()));
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Revalidation Chrg" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objRevTransactionTO.getDebitAcctNo());
                            transAmt = CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevalidateCharge()).doubleValue();
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setCommandMode(command);
                        }
                        //                     if(objRemitDupIssueTO != null){
                        //                        objRemitDupIssueTO.setDuplicateCharge(tempAmt);
                        //                        tempAmt = objRemitDupIssueTO.getDupServTax();
                        //                     }



                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevServTax()).doubleValue() > 0) {
                            transAmt = CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevServTax()).doubleValue();
                            if (transAmt > 0) {
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Exchange Charge account head......
                                txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Rev Chrg Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferList.add(transferTo);
                                transactionDAO.setCommandMode(command);
                            }
                        }
                        //                    if(objRemitDupIssueTO != null){
                        //                        objRemitDupIssueTO.setDupServTax(tempAmt);
                        //                    }

                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                        }
                    } else if (objRevTransactionTO.getTransType().equals("CASH")) {
                        //new mode
                        System.out.println("##### CASH MODE.........");
                        //                double transAmt;
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevalidateCharge()).doubleValue() > 0) {
                            System.out.println("line no 753^^^^^^^");
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("REVAL_CHRG_HD"));  // credit to Exchange Charge account head......
                            transAmt = CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevalidateCharge()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Revalidation Chrg" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transactionDAO.addTransferCredit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setBatchId(variableNo);
                            transTO.setTransAmt(new Double(transAmt));
                            cashList.add(transTO);
                            System.out.println("@@@@@@@@@@@@@@sdf" + cashList);
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            transactionDAO.setCommandMode(command);
                        }
                        if (CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevServTax()).doubleValue() > 0) {
                            System.out.println("line no 753^^^^^^^");
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Exchange Charge account head......
                            transAmt = CommonUtil.convertObjToDouble(objRemitRevIssueTO.getRevServTax()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Rev Chrg Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transactionDAO.addTransferCredit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setBatchId(variableNo);
                            transTO.setTransAmt(new Double(transAmt));
                            cashList = new ArrayList();
                            cashList.add(transTO);
                            System.out.println("@@@@@@@@@@@@@@sdf" + cashList);
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            transactionDAO.setCommandMode(command);
                        }
                    } else {
                        //do nothing
                    }
                }
            }
        } else if (operationMode.equals(CommonConstants.REMIT_DUPLICATE)) {
//            if(!DupDeleteMode){
            if (NoDupCharge == false) {
                if (DupEditMode || DupDeleteMode) {
                    System.out.println("######@@@@#!!!!@objTransactionTO" + objTransactionTO);
                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                        //edit

                        if (tempHashMap.containsKey("DUP_CHARGE_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("DUP_CHARGE_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDuplicateCharge()).doubleValue();
                            totDupAmount = newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            //                    if(objRemitDupIssueTO != null){
                            //                    objRemitDupIssueTO.setDuplicateCharge(new Double(0));
                            //                    }
                        }
                        //edit
                        if (tempHashMap.containsKey("DUP_SERV_TAX_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("DUP_SERV_TAX_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDupServTax()).doubleValue();
                            totDupAmount = totDupAmount + newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            //                    if(objRemitDupIssueTO != null){
//                        objRemitDupIssueTO.setDupServTax(new Double(0));
                            //                    }
                        }
                        if (tempHashMap.containsKey("TOTAL_DUP_AMT_TRANSFER_TRANS_DETAILS")) {

                            HashMap tempMap = (HashMap) tempHashMap.get("TOTAL_DUP_AMT_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            tempMap.put("TRANS_DT", currDt.clone());
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = totDupAmount;
                            //                    totDupAmount = totDupAmount + newAmount;
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                }
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            totDupAmount = 0.0;
                        }
                    }
                    if (objTransactionTO.getTransType().equals("CASH")) {

                        if (tempHashMap.containsKey("DUP_CHARGE_CASH_TRANS_DETAILS")) {
                            System.out.println("DUP_CHARGE_CASH_TRANS_DETAILS^^^^^^^");
                            HashMap tempMap = (HashMap) tempHashMap.get("DUP_CHARGE_CASH_TRANS_DETAILS");
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDuplicateCharge()).doubleValue();
                            CashTransactionTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                                if (lst != null) {
                                    txTransferTO = (CashTransactionTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    txTransferTO.setCommand(command);
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                                    obj.put("PRODUCTTYPE", TransactionFactory.GL);
                                    obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                                    obj.put("CashTransactionTO", txTransferTO);
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(obj, false);
                                }
                                lst = null;
                            }
                            txTransferTO = null;
                            oldAmountMap = null;
                            tempMap = null;
//                            objRemittanceIssueTO.setOtherCharges(new Double(0));
                        }
                        if (tempHashMap.containsKey("DUP_SERV_TAX_CASH_TRANS_DETAILS")) {
                            System.out.println("DUP_SERV_TAX_CASH_TRANS_DETAILS^^^^^^^");
                            HashMap tempMap = (HashMap) tempHashMap.get("DUP_SERV_TAX_CASH_TRANS_DETAILS");
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objRemittanceIssueTO.getDupServTax()).doubleValue();
                            CashTransactionTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                                if (lst != null) {
                                    txTransferTO = (CashTransactionTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    txTransferTO.setCommand(command);
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setProdId(objRemittanceIssueTO.getProdId());
                                    obj.put("PRODUCTTYPE", TransactionFactory.GL);
                                    obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                                    obj.put("CashTransactionTO", txTransferTO);
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(obj, false);
                                }
                                lst = null;
                            }
                            txTransferTO = null;
                            oldAmountMap = null;
                            tempMap = null;
//                            objRemittanceIssueTO.setOtherCharges(new Double(0));
                        }
                    }
                } else {
                    System.out.println("##### TRANSFER REMIT_DUPLICATE.........");
                    double transAmt;
                    TxTransferTO transferTo;
                    //                 Double tempAmt = null;
                    ArrayList transferList = new ArrayList(); // for local transfer
                    //                 if(objRemitDupIssueTO != null){
                    Double tempAmt = objRemitDupIssueTO.getDuplicateCharge();
                    //                 }
                    System.out.println("operationMode : " + operationMode);
                    if (objDupTransactionTO.getTransType().equals("TRANSFER")) {
                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDuplicateCharge()).doubleValue() > 0) {
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            if (!objDupTransactionTO.getProductType().equals("GL")) {
                                txMap.put(TransferTrans.DR_AC_HD, null);
                                txMap.put(TransferTrans.DR_ACT_NUM, objDupTransactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, objDupTransactionTO.getProductId());
                            } else {
                                txMap.put(TransferTrans.DR_AC_HD, objDupTransactionTO.getDebitAcctNo());
                            }
                            txMap.put(TransferTrans.DR_BRANCH, branchID);
                            txMap.put(TransferTrans.CURRENCY, "INR");
                            txMap.put(TransferTrans.DR_PROD_TYPE, objDupTransactionTO.getProductType());
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DUPL_CHRG_HD")); // credit to Remittance Issue account head......
                            transAmt = ((CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDuplicateCharge()).doubleValue())
                                    + (CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDupServTax()).doubleValue()));
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Duplicate Chrg" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                            transferList.add(transferTo);
                            txMap.put(TransferTrans.PARTICULARS, "A/c No-" + " " + objDupTransactionTO.getDebitAcctNo());
                            transAmt = CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDuplicateCharge()).doubleValue();
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                            transferList.add(transferTo);
                            transAmt = 0;
                            transactionDAO.setCommandMode(command);
                        }
                        //                     if(objRemitDupIssueTO != null){
                        //                        objRemitDupIssueTO.setDuplicateCharge(tempAmt);
                        //                        tempAmt = objRemitDupIssueTO.getDupServTax();
                        //                     }



                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDupServTax()).doubleValue() > 0) {
                            transAmt = CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDupServTax()).doubleValue();
                            if (transAmt > 0) {
                                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Exchange Charge account head......
                                txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Dup Chrg Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                                transferList.add(transferTo);
                                transactionDAO.setCommandMode(command);
                            }
                        }
                        //                    if(objRemitDupIssueTO != null){
                        //                        objRemitDupIssueTO.setDupServTax(tempAmt);
                        //                    }

                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.doTransferLocal(transferList, branchID);
                        }
                    } else if (objDupTransactionTO.getTransType().equals("CASH")) {
                        //new mode
                        System.out.println("##### CASH MODE.........");
                        //                double transAmt;
                        TransactionTO transTO = new TransactionTO();
                        ArrayList cashList = new ArrayList();
                        //new mode
                        if (CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDuplicateCharge()).doubleValue() > 0) {
                            System.out.println("line no 753^^^^^^^");
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("DUPL_CHRG_HD"));  // credit to Exchange Charge account head......
                            transAmt = CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDuplicateCharge()).doubleValue();
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Duplicate Chrg" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transactionDAO.addTransferCredit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setBatchId(variableNo);
                            transTO.setTransAmt(new Double(transAmt));
                            cashList.add(transTO);
                            System.out.println("@@@@@@@@@@@@@@sdf" + cashList);
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            transactionDAO.setCommandMode(command);
                        }
                        if (CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDupServTax()).doubleValue() > 0) {
                            System.out.println("line no 753^^^^^^^");
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));  // credit to Exchange Charge account head......
                            txMap.put(TransferTrans.PARTICULARS, objRemittanceIssueTO.getProdId() + " Dup Chrg Service Tax" + " " + objRemittanceIssueTO.getInstrumentNo1() + "" + objRemittanceIssueTO.getInstrumentNo2());
                            transAmt = CommonUtil.convertObjToDouble(objRemitDupIssueTO.getDupServTax()).doubleValue();
//                            txMap.put(TransferTrans.PARTICULARS,"Cash");
                            transactionDAO.addTransferCredit(txMap, transAmt);
                            transactionDAO.deleteTxList();
                            transTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            transTO.setTransType("CASH");
                            transTO.setBatchId(variableNo);
                            transTO.setTransAmt(new Double(transAmt));
                            cashList = new ArrayList();
                            cashList.add(transTO);
                            System.out.println("@@@@@@@@@@@@@@sdf" + cashList);
                            transactionDAO.addCashList(cashList);
                            transactionDAO.doTransfer();
                            transactionDAO.setCommandMode(command);
                        }
                    } else {
                        //do nothing
                    }
                }
            }
//        }
        }
    }

    private void transToRemitPayment(HashMap acHeads, String branchID, double amt) throws Exception {
        System.out.println("acHeads : " + acHeads);
        HashMap txMap = new HashMap();
        TxTransferTO transferTo = null;

        transferRemitPayList = new ArrayList(); // for local transfer

        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("ISSUE_HD"));
        txMap.put(TransferTrans.DR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PAY_HD"));
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.CR_BRANCH, branchID);

        transferTo = transactionDAO.addTransferDebitLocal(txMap, amt);
        transferRemitPayList.add(transferTo);
        transferTo = transactionDAO.addTransferCreditLocal(txMap, amt);
        transferRemitPayList.add(transferTo);
    }

    private String doInsert(HashMap obj, String command, String branchID) throws Exception {
        batchId = getBatchId();

        HashMap txMap = new HashMap();
        HashMap tempHashMap = new HashMap();
        System.out.println("@@@@@@@@@allowedIssueTOs" + allowedIssueTOs);
        System.out.println("@@@@@@@@@allowedIssueTOsSIZE" + allowedIssueTOs.size());
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) remitIssueTransMap.get("TransactionTO");
        double transTotAmt = 0;
        int transNo = 1;
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                transactionDAO.setBatchId(batchId);
                transactionDAO.setBatchDate(batchDate = currDt);
                transactionDAO.setLinkBatchID(variableNo);
                transactionDAO.execute(remitIssueTransMap);
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                newAllowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                System.out.println("!!!!!!allowedTransDetailsTO.size()" + allowedTransDetailsTO.size());
                System.out.println("!!!!!!newAllowedTransDetailsTO.size()" + newAllowedTransDetailsTO.size());
                System.out.println("!!!!!!allowedTransDetailsTO" + allowedTransDetailsTO);
                System.out.println("!!!!!!newAllowedTransDetailsTO" + newAllowedTransDetailsTO);
                double tempTotAmt = 0;
                for (int i = 1, j = allowedIssueTOs.size(); i <= j; i++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transNo));
                    transTotAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    objRemittanceIssueTO = (RemittanceIssueTO) allowedIssueTOs.get(String.valueOf(i));
                    tempTotAmt += objRemittanceIssueTO.getTotalAmt().doubleValue();
                    if (tempTotAmt == transTotAmt) {
                        tempTotAmt = 0;
                        transNo++;
                    }
                    if (obj.containsKey("TransactionDetails")) {
                        HashMap dataHash = new HashMap();
                        dataHash.put("TransMap", (((HashMap) obj.get("TransactionDetails")).get(objRemittanceIssueTO.getVariableNo())));
                        tempHashMap = (HashMap) dataHash.get("TransMap");
                    }
                    doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    objTransactionTO = null;
                }
                allowedTransDetailsTO = null;
            }
        }
        TransactionDetailsMap = null;
        return batchId;
    }

    private HashMap createMap(HashMap map, RemittanceIssueTO obj, String branchID) {
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.CR_BRANCH, branchID);
        map.put(TransferTrans.CURRENCY, "INR");
        return map;
    }

    private void doUpdate(HashMap obj, String command, String branchID) throws Exception {
        //changed by vinay
        HashMap txMap = new HashMap();
        HashMap tempHashMap = new HashMap();
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) remitIssueTransMap.get("TransactionTO");
        double transTotAmt = 0;
        int transNo = 1;
        int tempNum = 0;
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                double tempTotAmt = 0;
                for (int i = 1, j = allowedIssueTOs.size(); i <= j; i++) {
                    transactionDAO.setCommandMode(command);
                    transactionDAO.execute(obj);
                    if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) || (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
                        transNo = allowedTransDetailsTO.size();
                    }
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transNo));
                    transTotAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    objRemittanceIssueTO = (RemittanceIssueTO) allowedIssueTOs.get(String.valueOf(i));
                    tempTotAmt += objRemittanceIssueTO.getTotalAmt().doubleValue();
                    if (tempTotAmt == transTotAmt) {
                        tempTotAmt = 0;
                        transNo++;
                    }
                    if (obj.containsKey("TransactionDetails")) {
                        HashMap dataHash = new HashMap();
                        dataHash.put("TransMap", (((HashMap) obj.get("TransactionDetails")).get(objRemittanceIssueTO.getVariableNo())));
                        tempHashMap = (HashMap) dataHash.get("TransMap");
                    }
                    System.out.println("#####linkbvatchid" + objRemittanceIssueTO.getVariableNo());
                    transactionDAO.setLinkBatchID(objRemittanceIssueTO.getVariableNo());
                    transactionDAO.setBatchId(objRemittanceIssueTO.getBatchId());
                    System.out.println("#####linkchid" + transactionDAO.getLinkBatchID());
                    System.out.println("#####getBatchId" + transactionDAO.getBatchId());
                    String billsPayorder = CommonUtil.convertObjToStr(objRemittanceIssueTO.getAuthorizeRemark());
                    if ((billsPayorder.length() > 0) && (billsPayorder.equals("FROM_BILLS_MODULE"))) {
                        objTransactionTO.setTransType("");
                    }
                    doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    updateRemitIssueDetails(command);

//                    transactionDAO.setCommandMode(command);
//                    transactionDAO.execute(obj);
                    objTransactionTO = null;
                }
                allowedTransDetailsTO = null;
            }
        }
        TransactionDetailsMap = null;
    }

    private void doDelete(HashMap obj, String command, String branchID) throws Exception {
        if (allowedIssueTOs != null) {
            HashMap txMap = new HashMap();
            HashMap tempHashMap = new HashMap();
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) remitIssueTransMap.get("TransactionTO");
            double transTotAmt = 0;
            int transNo = 1;
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    double tempTotAmt = 0;
                    for (int i = 1, j = allowedIssueTOs.size(); i <= j; i++) {
                        if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) || (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
                            transNo = allowedTransDetailsTO.size();
                        }
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transNo));
                        transTotAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                        if (!fromotherDAo) {
                            objRemittanceIssueTO = new RemittanceIssueTO();
                        }
                        objRemittanceIssueTO = (RemittanceIssueTO) allowedIssueTOs.get(String.valueOf(i));
                        System.out.println("$$@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
                        tempTotAmt += objRemittanceIssueTO.getTotalAmt().doubleValue();
                        if (tempTotAmt == transTotAmt) {
                            tempTotAmt = 0;
                            transNo++;
                        }
                        if (obj.containsKey("TransactionDetails")) {
                            HashMap dataHash = new HashMap();
                            dataHash.put("TransMap", (((HashMap) obj.get("TransactionDetails")).get(objRemittanceIssueTO.getVariableNo())));
                            tempHashMap = (HashMap) dataHash.get("TransMap");
                        }
                        doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                        System.out.println("$$@@@@***@@objRemittanceIssueTO" + objRemittanceIssueTO);
                        deleteRemitIssueDetails(command);
                        transactionDAO.setCommandMode(command);
                        transactionDAO.execute(obj);
                        objTransactionTO = null;
                    }
                    allowedTransDetailsTO = null;
                }
            }
            TransactionDetailsMap = null;
        }
    }

    private String doDuplicate(HashMap obj, String command, String branchID) throws Exception {
        batchId = getBatchId();
        String oldVariableNum = "";
        HashMap txMap = new HashMap();
        HashMap tempHashMap = new HashMap();
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) remitIssueTransMap.get("TransactionTO");
        double transTotAmt = 0;
        int transNo = 1;
        int transSize = 0;
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                transactionDAO.setBatchId(batchId);
                transactionDAO.setBatchDate(batchDate = currDt);
                transactionDAO.setLinkBatchID(variableNo);
                transactionDAO.execute(remitIssueTransMap);
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                transSize = allowedTransDetailsTO.size();
                double tempTotAmt = 0;
                for (int i = 1, j = allowedIssueTOs.size(); i <= j; i++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transNo));
                    transTotAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    objRemittanceIssueTO = (RemittanceIssueTO) allowedIssueTOs.get(String.valueOf(i));
                    tempTotAmt += objRemittanceIssueTO.getTotalAmt().doubleValue();
                    oldVariableNum = objRemittanceIssueTO.getVariableNo();
                    if (!oldVariableNum.equals("")) {
                        if (!DupInsertUpdateMode) {
                            newVariableNo = getVariableNo();
                            HashMap dupData = new HashMap();
                            dupData.put("VARIABLE_NO", oldVariableNum);
                            if (NoDupCharge) {
                                dupData.put("DUPLICATE_DT", currDt.clone());
                                dupData.put("DUPLICATE_CHARGE", new Double(0));
                                dupData.put("DUPLICATE_REMARKS", "");
                                dupData.put("DUP_SERV_TAX", new Double(0));
                            } else {
                                dupData.put("DUPLICATE_DT", currDt.clone());
                                dupData.put("DUPLICATE_CHARGE", objRemitDupIssueTO.getDuplicateCharge());
                                dupData.put("DUPLICATE_REMARKS", objRemitDupIssueTO.getDuplicateRemarks());
                                dupData.put("DUP_SERV_TAX", objRemitDupIssueTO.getDupServTax());
                            }
                            dupData.put("NEW_VARIABLE_NO", newVariableNo);
                            dupData.put("PAID_STATUS", "DUPLICATE_ISSUED");
                            System.out.println("@@@@@@@@@@@@@@@@@@dupData" + dupData);
                            sqlMap.executeUpdate("updateRemittanceDupIssueTO", dupData);
                        }
                    }
                    if (tempTotAmt == transTotAmt) {
                        tempTotAmt = 0;
                        transNo++;
                    }
                    if (obj.containsKey("TransactionDetails")) {
                        HashMap dataHash = new HashMap();
                        dataHash.put("TransMap", (((HashMap) obj.get("TransactionDetails")).get(objRemittanceIssueTO.getVariableNo())));
                        tempHashMap = (HashMap) dataHash.get("TransMap");
                    }
                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
//                        if(!DupInsertUpdateMode){
                        objRemittanceIssueTO.setAuthorizeStatus("");
                        objRemittanceIssueTO.setAuthorizeDt(null);
                        objRemittanceIssueTO.setAuthorizeRemark("");
                        objRemittanceIssueTO.setAuthorizeUser("");
                        objRemittanceIssueTO.setDuplicateDt(currDt);
                        objRemittanceIssueTO.setRemarks(CommonConstants.REMIT_DUPLICATE);
                        insertRemitIssueDetails(command);
                        transactionDAO.setBatchId(batchId);
                        transactionDAO.setBatchDate(batchDate = currDt);
                        transactionDAO.setLinkBatchID(variableNo);
//                        }
                        objDupTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transSize));
                        doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    }
                    if (DupInsertUpdateMode) {
                        objDupTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transSize));
                        doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    }
                    objTransactionTO = null;
                }
                allowedTransDetailsTO = null;
            }
        }
        TransactionDetailsMap = null;
        NoDupCharge = false;
        transSize = 0;
        return batchId;
    }
    //REVALIDATE
    private String doRevalidate(HashMap obj, String command, String branchID) throws Exception {
        batchId = getBatchId();
        String oldVariableNum = "";
        HashMap txMap = new HashMap();
        HashMap tempHashMap = new HashMap();
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) remitIssueTransMap.get("TransactionTO");
        double transTotAmt = 0;
        int transNo = 1;
        int transSize = 0;
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                transactionDAO.setBatchId(batchId);
                transactionDAO.setBatchDate(batchDate = currDt);
                transactionDAO.setLinkBatchID(variableNo);
                transactionDAO.execute(remitIssueTransMap);
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                transSize = allowedTransDetailsTO.size();
                double tempTotAmt = 0;
                for (int i = 1, j = allowedIssueTOs.size(); i <= j; i++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transNo));
                    transTotAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                    objRemittanceIssueTO = (RemittanceIssueTO) allowedIssueTOs.get(String.valueOf(i));
                    tempTotAmt += objRemittanceIssueTO.getTotalAmt().doubleValue();
                    oldVariableNum = objRemittanceIssueTO.getVariableNo();
                    if (!oldVariableNum.equals("")) {
                        if (!RevInsertUpdateMode) {
                            newVariableNo = getVariableNo();
                            HashMap revData = new HashMap();
                            revData.put("VARIABLE_NO", oldVariableNum);
                            if (NoRevCharge) {
                                revData.put("REVALIDATE_DT", currDt.clone());
                                revData.put("REVALIDATE_CHARGE", new Double(0));
                                revData.put("REVALIDATE_REMARKS", "");
                                revData.put("REV_SERV_TAX", new Double(0));
                            } else {
                                revData.put("REVALIDATE_DT", currDt.clone());
                                revData.put("REVALIDATE_CHARGE", objRemitRevIssueTO.getRevalidateCharge());
                                revData.put("REVALIDATE_REMARKS", objRemitRevIssueTO.getRevalidateRemarks());
                                revData.put("REV_SERV_TAX", objRemitRevIssueTO.getRevServTax());
                            }
                            revData.put("NEW_VARIABLE_NO", newVariableNo);
                            revData.put("PAID_STATUS", "REVALIDATED");
                            System.out.println("@@@@@@@@@@@@@@@@@@revData" + revData);
                            sqlMap.executeUpdate("updateRemittanceRevIssueTO", revData);
                        }
                    }
                    if (tempTotAmt == transTotAmt) {
                        tempTotAmt = 0;
                        transNo++;
                    }
                    if (obj.containsKey("TransactionDetails")) {
                        HashMap dataHash = new HashMap();
                        dataHash.put("TransMap", (((HashMap) obj.get("TransactionDetails")).get(objRemittanceIssueTO.getVariableNo())));
                        tempHashMap = (HashMap) dataHash.get("TransMap");
                    }
                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
//                        if(!DupInsertUpdateMode){
                        objRemittanceIssueTO.setAuthorizeStatus("");
                        objRemittanceIssueTO.setAuthorizeDt(null);
                        objRemittanceIssueTO.setAuthorizeRemark("");
                        objRemittanceIssueTO.setAuthorizeUser("");
                        objRemittanceIssueTO.setRevalidateDt(currDt);
                        objRemittanceIssueTO.setRemarks("REVALIDATED");
                        insertRemitIssueDetails(command);
                        transactionDAO.setBatchId(batchId);
                        transactionDAO.setBatchDate(batchDate = currDt);
                        transactionDAO.setLinkBatchID(variableNo);
//                        }
                        objRevTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transSize));
                        doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    }
                    if (DupInsertUpdateMode) {
                        objDupTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(transSize));
                        doAccountHeadCredit(obj, command, txMap, branchID, tempHashMap);
                    }
                    objTransactionTO = null;
                }
                allowedTransDetailsTO = null;
            }
        }
        TransactionDetailsMap = null;
        NoDupCharge = false;
        transSize = 0;
        return batchId;
    }

    /**
     * Executes Insert, Update, Delete commands
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        //Test Code

        /*
         * java.io.FileOutputStream out; out = new
         * java.io.FileOutputStream("D:\\myfile1.txt");
         * java.io.ObjectOutputStream dataOut = new
         * java.io.ObjectOutputStream(out); dataOut.writeObject(map);
         * dataOut.flush(); dataOut.close();
         */
        //End of Test Code
        System.out.println("Map in DAO : " + map);
        //        if(map.containsKey("PROCCHARGEHASH")){
        //            transferDAO=new TransferDAO();
        //        HashMap procMap=new HashMap();
        //        procMap.put("COMMAND",map.get("MODE"));
        //        procMap.put("PROCCHARGEHASH",map.get("PROCCHARGEHASH"));
        //        System.out.println("####@@@@Remitance dao  :"+procMap);
        //        transferDAO.execute(procMap);
        //        map.remove("PROCCHARGEHASH");
        //        }
        /*
         * HashMap data = new HashMap(); data = (HashMap)
         * map.get("TransactionTO");
        System.out.println("%%%TransactionTO"+data);
         */
        HashMap returnData = null;
        String linkBatchId = null;
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        branchRemitPay = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        logDAO = new LogDAO();
        setInitialValuesForLogTO(map);
        remitIssueTransMap = map;
        final String command = CommonUtil.convertObjToStr(map.get("MODE"));
        operationMode = CommonUtil.convertObjToStr(map.get("OPERATION_MODE"));
        System.out.println("#############" + command + "%%%%%%%%%%%%%%%%%" + operationMode);
        if (map.get("AUTHORIZEMAP") == null) {
            issueMap = (LinkedHashMap) map.get("RemittanceIssueTO");
            System.out.println("command = " + command);
            allowedIssueTOs = (LinkedHashMap) issueMap.get("NOT_DELETED_ISSUE_TOs");
            System.out.println("allowedIssueTOs = " + allowedIssueTOs.toString());
        }

        //for duplicate coding
        if ((map.containsKey("updateTOWithDuplicateDetails")) && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))) {
            objRemitDupIssueTO = (RemittanceIssueTO) map.get("updateTOWithDuplicateDetails");
            System.out.println("objRemitDupIssueTO&&&&&&&&&&& = " + objRemitDupIssueTO.toString());
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                DupInsertUpdateMode = true;
            }
        } else if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) && (command.equals(CommonConstants.TOSTATUS_INSERT))) {
            NoDupCharge = true;
        } else {
        }
        if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) && (command.equals(CommonConstants.TOSTATUS_UPDATE))) {
            DupEditMode = true;
        }
        if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) && (command.equals(CommonConstants.TOSTATUS_DELETE))) {
            DupDeleteMode = true;
        }
        System.out.println("NoDupCharge*********** = " + NoDupCharge);

        //  for revalidate coding
        if ((map.containsKey("updateTOWithRevalidateDetails")) && (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
            objRemitRevIssueTO = (RemittanceIssueTO) map.get("updateTOWithRevalidateDetails");
            System.out.println("objRemitRevIssueTO*&*&&&&&&&&&&& = " + objRemitRevIssueTO.toString());
            if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                RevInsertUpdateMode = true;
            }
        } else if ((operationMode.equals(CommonConstants.REMIT_REVALIDATE)) && (command.equals(CommonConstants.TOSTATUS_INSERT))) {
            NoRevCharge = true;
        } else {
        }
        if ((operationMode.equals(CommonConstants.REMIT_REVALIDATE)) && (command.equals(CommonConstants.TOSTATUS_UPDATE))) {
            RevEditMode = true;
        }
        if ((operationMode.equals(CommonConstants.REMIT_REVALIDATE)) && (command.equals(CommonConstants.TOSTATUS_DELETE))) {
            RevDeleteMode = true;
        }
        System.out.println("NoRevCharge*********** = " + NoRevCharge);
//        if(map.get("updateTOWithDuplicateDetails") != null && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))){
//            objRemitDupIssueTO = (RemittanceIssueTO)map.get("updateTOWithDuplicateDetails");
//            System.out.println("objRemitDupIssueTO&&&&&&&&&&& = " + objRemitDupIssueTO.toString());
//        }
//        if((map.get("updateTOWithDuplicateDetails").equals(null)) && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))){
//            NoDupCharge = true;
//        }
        try {
            if (fromotherDAo) {
                sqlMap.startTransaction();
            }
            if ((command.equals(CommonConstants.TOSTATUS_INSERT)) && (operationMode.equals(CommonConstants.REMIT_ISSUE))) {
                linkBatchId = doInsert(map, command, branchRemitPay);
                transactionDAO.setBatchId(batchId);
                System.out.println("Batch Id = " + batchId);
                transactionDAO.setBatchDate(batchDate);
                returnData = new HashMap();
                returnData.put("VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                returnData.put("INST_NO1", objRemittanceIssueTO.getInstrumentNo1());
                returnData.put("INST_NO2", objRemittanceIssueTO.getInstrumentNo2());
                System.out.println("@@@@@@@@returnData" + returnData);
            } else if ((command.equals(CommonConstants.TOSTATUS_UPDATE)) && (operationMode.equals(CommonConstants.REMIT_ISSUE))) {
                doUpdate(map, command, branchRemitPay);
            } else if ((command.equals(CommonConstants.TOSTATUS_DELETE)) && (operationMode.equals(CommonConstants.REMIT_ISSUE))) {
                doDelete(map, command, branchRemitPay);
            } else if (map.containsKey("AUTHORIZEMAP") && map.get("AUTHORIZEMAP") != null) {
                System.out.println("In Authorize .... ");
                doAuthorize(map);
            } else if ((command.equals(CommonConstants.TOSTATUS_INSERT)) && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))) {
                linkBatchId = doDuplicate(map, command, branchRemitPay);
                transactionDAO.setBatchId(batchId);
                System.out.println("Batch Id = " + batchId);
                transactionDAO.setBatchDate(batchDate);
                returnData = new HashMap();
                returnData.put("VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                returnData.put("INST_NO1", objRemittanceIssueTO.getInstrumentNo1());
                returnData.put("INST_NO2", objRemittanceIssueTO.getInstrumentNo2());
                System.out.println("@@@@@@@@returnData" + returnData);
            } else if ((command.equals(CommonConstants.TOSTATUS_UPDATE)) && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))) {
                doUpdate(map, command, branchRemitPay);
//                doDuplicate(map, command, branchRemitPay);
                if (DupEditMode) {
                    HashMap newDupMap = new HashMap();
                    newDupMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                    List dupList = sqlMap.executeQueryForList("getDupOldVariableNo", newDupMap);
                    newDupMap = null;
                    if (dupList != null && dupList.size() > 0) {
                        newDupMap = (HashMap) dupList.get(0);
                        System.out.println("@@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
                        newDupMap.put("DUPLICATE_DT", objRemittanceIssueTO.getDuplicateDt());
                        newDupMap.put("DUPLICATE_CHARGE", objRemittanceIssueTO.getDuplicateCharge());
                        newDupMap.put("DUPLICATE_REMARKS", objRemittanceIssueTO.getDuplicateRemarks());
                        newDupMap.put("DUP_SERV_TAX", objRemittanceIssueTO.getDupServTax());
                        newDupMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                        newDupMap.put("VARIABLE_NO", newDupMap.get("VARIABLE_NO"));
                        newDupMap.put("PAID_STATUS", "DUPLICATE_ISSUED");
                        System.out.println("@@@@@@@@newDupMap" + newDupMap);
                        sqlMap.executeUpdate("updateRemittanceDupIssueTO", newDupMap);
                    }
                    DupEditMode = false;
                }
                if (DupInsertUpdateMode) {
                    DupEditMode = false;
                    DupDeleteMode = false;
                    linkBatchId = doDuplicate(map, command, branchRemitPay);
                    DupInsertUpdateMode = false;
                }
            } else if ((command.equals(CommonConstants.TOSTATUS_DELETE)) && (operationMode.equals(CommonConstants.REMIT_DUPLICATE))) {
                doDelete(map, command, branchRemitPay);
                if (DupDeleteMode) {
                    HashMap newDupMap = new HashMap();
                    newDupMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                    List dupList = sqlMap.executeQueryForList("getDupOldVariableNo", newDupMap);
                    newDupMap = null;
                    if (dupList != null && dupList.size() > 0) {
                        newDupMap = (HashMap) dupList.get(0);
                        System.out.println("@@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
//                         newDupMap.put("DUPLICATE_DT", "");
//                         newDupMap.put("DUPLICATE_CHARGE", "");
//                         newDupMap.put("DUPLICATE_REMARKS", "");
//                         newDupMap.put("DUP_SERV_TAX", "");
//                         newDupMap.put("NEW_VARIABLE_NO", "");
                        newDupMap.put("VARIABLE_NO", newDupMap.get("VARIABLE_NO"));
//                         newDupMap.put("PAID_STATUS", "");
//                         System.out.println("@@@@@@@@newDupMap"+newDupMap);
                        sqlMap.executeUpdate("updateRejectRemittanceDupIssueTO", newDupMap);
                    }
                    DupDeleteMode = false;
                }
            } //REVALIDATE CODING
            else if ((command.equals(CommonConstants.TOSTATUS_INSERT)) && (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
                linkBatchId = doRevalidate(map, command, branchRemitPay);
                transactionDAO.setBatchId(batchId);
                System.out.println("Batch Id = " + batchId);
                transactionDAO.setBatchDate(batchDate);
                returnData = new HashMap();
                returnData.put("VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                returnData.put("INST_NO1", objRemittanceIssueTO.getInstrumentNo1());
                returnData.put("INST_NO2", objRemittanceIssueTO.getInstrumentNo2());
                System.out.println("@@@@@@@@returnData" + returnData);
            } else if ((command.equals(CommonConstants.TOSTATUS_UPDATE)) && (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
                doUpdate(map, command, branchRemitPay);
//                doDuplicate(map, command, branchRemitPay);
                if (RevEditMode) {
                    HashMap newRevMap = new HashMap();
                    newRevMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                    List RevList = sqlMap.executeQueryForList("getRevOldVariableNo", newRevMap);
                    newRevMap = null;
                    if (RevList != null && RevList.size() > 0) {
                        newRevMap = (HashMap) RevList.get(0);
                        System.out.println("@@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
                        newRevMap.put("REVALIDATE_DT", objRemittanceIssueTO.getRevalidateDt());
                        newRevMap.put("REVALIDATE_CHARGE", objRemittanceIssueTO.getRevalidateCharge());
                        newRevMap.put("REVALIDATE_REMARKS", objRemittanceIssueTO.getRevalidateRemarks());
                        newRevMap.put("REV_SERV_TAX", objRemittanceIssueTO.getRevServTax());
                        newRevMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                        newRevMap.put("VARIABLE_NO", newRevMap.get("VARIABLE_NO"));
                        newRevMap.put("PAID_STATUS", "REVALIDATED");
                        System.out.println("@@@@@@@@newDupMap" + newRevMap);
                        sqlMap.executeUpdate("updateRemittanceRevIssueTO", newRevMap);
                    }
                    RevEditMode = false;
                }
                if (RevInsertUpdateMode) {
                    RevEditMode = false;
                    RevDeleteMode = false;
                    linkBatchId = doRevalidate(map, command, branchRemitPay);
                    RevInsertUpdateMode = false;
                }
            } else if ((command.equals(CommonConstants.TOSTATUS_DELETE)) && (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
                doDelete(map, command, branchRemitPay);
                if (RevDeleteMode) {
                    HashMap newRevMap = new HashMap();
                    newRevMap.put("NEW_VARIABLE_NO", objRemittanceIssueTO.getVariableNo());
                    List revList = sqlMap.executeQueryForList("getRevOldVariableNo", newRevMap);
                    newRevMap = null;
                    if (revList != null && revList.size() > 0) {
                        newRevMap = (HashMap) revList.get(0);
                        System.out.println("@@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
//                         newDupMap.put("DUPLICATE_DT", "");
//                         newDupMap.put("DUPLICATE_CHARGE", "");
//                         newDupMap.put("DUPLICATE_REMARKS", "");
//                         newDupMap.put("DUP_SERV_TAX", "");
//                         newDupMap.put("NEW_VARIABLE_NO", "");
                        newRevMap.put("VARIABLE_NO", newRevMap.get("VARIABLE_NO"));
//                         newDupMap.put("PAID_STATUS", "");
//                         System.out.println("@@@@@@@@newDupMap"+newDupMap);
                        sqlMap.executeUpdate("updateRejectRemittanceRevIssueTO", newRevMap);
                    }
                    RevDeleteMode = false;
                }
            } else {
                throw new NoCommandException();
            }
            System.out.println("map after putting LINK_ID (INSERT CASE ONLY): " + map);

            if (command.equals(CommonConstants.TOSTATUS_INSERT) || command.equals(CommonConstants.TOSTATUS_UPDATE)) {

                /*
                 * transactionDAO.setCommandMode(command) ;
                 * transactionDAO.execute(map);
                transactionDAO.doTransfer();
                 */
                if (operationMode.equals(CommonConstants.REMIT_ISSUE) && transferRemitPayList != null) {
                    System.out.println("transferRemitPayList:" + transferRemitPayList);
                    System.out.println("branchRemitPay:" + branchRemitPay);
                    transactionDAO.doTransferLocal(transferRemitPayList, branchRemitPay);
                }
            }
            if (map.containsKey("PROCCHARGEHASH")) {        //This for transfer termloan processingcharge
                HashMap insertMap = new HashMap();
                insertMap.put("COMMAND", map.get("MODE"));
                insertMap.put("PROCCHARGEHASH", map.get("PROCCHARGEHASH"));
                System.out.println("beforegoing to operative#### :" + insertMap);
                updateProcessingChargesFromOperative(insertMap);
            }
            if (fromotherDAo) {
                sqlMap.commitTransaction();
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        destroyObjects();
        return returnData;
    }

    private void updateProcessingChargesFromOperative(HashMap procFullHash) throws Exception {
        System.out.println("#####collectfromoperative" + procFullHash);
        HashMap procHash = (HashMap) procFullHash.get("PROCCHARGEHASH");
        //        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_AMT")).doubleValue();
        double procAmt = CommonUtil.convertObjToDouble(procHash.get("PROC_CHRG")).doubleValue();
        HashMap txMap = new HashMap();
        if (procAmt > 0) {
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            txMap.put(TransferTrans.DR_PROD_ID, (String) procHash.get("OA_PROD_ID"));
            txMap.put(TransferTrans.DR_ACT_NUM, (String) procHash.get("OA_ACT_NUM"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            List lst = (List) sqlMap.executeQueryForList("getProcChargeAcHd", procHash);
            HashMap acHeads = (HashMap) lst.get(0);
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PROC_CHRG"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            System.out.println("####### insertAccHead txMap " + txMap);
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setForProcCharge(true);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(procHash.get("LINK_BATCH_ID")));
            transferList.add(trans.getDebitTransferTO(txMap, procAmt));
            transferList.add(trans.getCreditTransferTO(txMap, procAmt));
            String cmd = CommonUtil.convertObjToStr(procFullHash.get("COMMAND"));
            trans.doDebitCredit(transferList, _branchCode, false, cmd);
        }
    }

    private void doAuthorize(HashMap map) throws Exception {
        HashMap singleAuthorizeMap;
        HashMap cashAuthMap = new HashMap();

        String authorizeStatus = null;
        String linkBatchId = null;
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        System.out.println("DAO #### AUTHMAP" + authMap);
        List authData = (List) authMap.get(CommonConstants.AUTHORIZEDATA);
        System.out.println("DAO #### AUTHlist" + authData);
        for (int i = 0, j = authData.size(); i < j; i++) {
            linkBatchId = CommonUtil.convertObjToStr(((HashMap) authData.get(i)).get("BATCH_ID"));//Transaction Batch Id
            singleAuthorizeMap = new HashMap();
            authorizeStatus = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            singleAuthorizeMap.put("STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
            singleAuthorizeMap.put("BATCH_ID", linkBatchId);
            singleAuthorizeMap.put("TODAY_DT", currDt.clone());
            singleAuthorizeMap.put("AUTHORIZE_USER", map.get(CommonConstants.USER_ID));
            sqlMap.executeUpdate("authorizeRemitIssue", singleAuthorizeMap);
            singleAuthorizeMap = null;
            String linkBatchID = "";
            String remarks = "";
            HashMap hash = new HashMap();
            List lst = (List) sqlMap.executeQueryForList("getVariableNumber", linkBatchId);
            if (lst != null) {
                if (lst.size() > 0) {
                    for (int m = 0, n = lst.size(); m < n; m++) {
                        hash = (HashMap) lst.get(m);
                        linkBatchID = CommonUtil.convertObjToStr(hash.get("VARIABLE_NO"));
                        remarks = CommonUtil.convertObjToStr(hash.get("REMARKS"));
                        String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
                        if (status.equals("REJECTED")) {
                            HashMap newDupMap = new HashMap();
                            newDupMap.put("NEW_VARIABLE_NO", linkBatchID);
                            List dupList = sqlMap.executeQueryForList("getDupOldVariableNo", newDupMap);
                            List RevList = sqlMap.executeQueryForList("getRevOldVariableNo", newDupMap);
                            newDupMap = null;
                            if (dupList != null && dupList.size() > 0) {
                                newDupMap = (HashMap) dupList.get(0);
                                newDupMap.put("VARIABLE_NO", newDupMap.get("VARIABLE_NO"));
                                System.out.println("@@@@@@***@@newDupMap" + newDupMap);
                                sqlMap.executeUpdate("updateRejectRemittanceDupIssueTO", newDupMap);
                            }
                            if (RevList != null && RevList.size() > 0) {
                                newDupMap = (HashMap) RevList.get(0);
                                newDupMap.put("VARIABLE_NO", newDupMap.get("VARIABLE_NO"));
                                System.out.println("@@@@@@***@@newDupMap" + newDupMap);
                                sqlMap.executeUpdate("updateRejectRemittanceRevIssueTO", newDupMap);
                            }
                        }

                        //Separation of Authorization for Cash and Transfer
                        //Call this in all places that need Authorization for Transaction
                        cashAuthMap = new HashMap();
                        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
//                        transactionDAO.setLoanDebitInt("DP");
                        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
                        TransactionDAO.authorizeCashAndTransfer(linkBatchID, authorizeStatus, cashAuthMap);
                        if ((status.equals("AUTHORIZED"))) {
                            if ((!remarks.equals("REVALIDATED")) && (!remarks.equals("DUPLICATE"))) {
                                ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
                                HashMap whereMap = new HashMap();
                                whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(hash.get("VARIABLE_NO")));
                                List exgStLst = (List) sqlMap.executeQueryForList("getSelectExgST", whereMap);
                                whereMap = null;
                                if (exgStLst != null && exgStLst.size() > 0) {
                                    whereMap = (HashMap) exgStLst.get(0);
                                    objChargesServiceTaxTO.setAmount(CommonUtil.convertObjToDouble(whereMap.get("EXCHANGE")));
                                    objChargesServiceTaxTO.setService_tax_amt(CommonUtil.convertObjToDouble(whereMap.get("SERVICE_TAX")));
                                    objChargesServiceTaxTO.setTotal_amt(CommonUtil.convertObjToDouble(whereMap.get("TOT_AMT")));
                                }
                                exgStLst = null;
                                exgStLst = (List) sqlMap.executeQueryForList("getSelectSTHeadRemittance", whereMap);
                                whereMap = null;
                                if (exgStLst != null && exgStLst.size() > 0) {
                                    whereMap = (HashMap) exgStLst.get(0);
                                    whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(hash.get("VARIABLE_NO")));
                                    whereMap.put("SERVICE_TAX", objChargesServiceTaxTO.getService_tax_amt());
                                    String exgHd = CommonUtil.convertObjToStr(whereMap.get("EXCHANGE_HD"));
                                    exgStLst = null;
                                    exgStLst = (List) sqlMap.executeQueryForList("getSelectTransCashRemitSTDetails", whereMap);
                                    whereMap = null;
                                    if (exgStLst != null && exgStLst.size() > 0) {
                                        whereMap = (HashMap) exgStLst.get(0);
                                        Date curDate = (Date) currDt.clone();
                                        objChargesServiceTaxTO.setAc_Head(exgHd);
                                        objChargesServiceTaxTO.setAcct_num(CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));
                                        objChargesServiceTaxTO.setAuthorize_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                        objChargesServiceTaxTO.setAuthorize_dt(curDate);
                                        objChargesServiceTaxTO.setAuthorize_status(status);
                                        objChargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                        objChargesServiceTaxTO.setProd_id(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")));
                                        objChargesServiceTaxTO.setProd_type(CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")));
                                        objChargesServiceTaxTO.setStatus(CommonConstants.STATUS_CREATED);
                                        objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(whereMap.get("TRAN_ID")));
                                        objChargesServiceTaxTO.setCreated_dt(curDate);
                                        objChargesServiceTaxTO.setTrans_dt(curDate);
                                        objChargesServiceTaxTO.setParticulars(CommonUtil.convertObjToStr(whereMap.get("PARTICULARS")));
                                        objChargesServiceTaxTO.setBranchCode(CommonUtil.convertObjToStr(whereMap.get("BRANCH_ID")));
                                        sqlMap.executeUpdate("insertChargesServiceTaxTO", objChargesServiceTaxTO);
                                        whereMap = null;
                                        exgStLst = null;
                                    }

                                }
                            }
                            if ((remarks.equals("REVALIDATED")) || (remarks.equals("DUPLICATE"))) {
                                ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
                                HashMap whereMap = new HashMap();
                                whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(hash.get("VARIABLE_NO")));
                                List exgStLst = null;
                                if (remarks.equals("REVALIDATED")) {
                                    exgStLst = (List) sqlMap.executeQueryForList("getSelectExgSTRev", whereMap);
                                } else {
                                    exgStLst = (List) sqlMap.executeQueryForList("getSelectExgSTDup", whereMap);
                                }
                                whereMap = null;
                                if (exgStLst != null && exgStLst.size() > 0) {
                                    whereMap = (HashMap) exgStLst.get(0);
                                    if (remarks.equals("REVALIDATED")) {
                                        objChargesServiceTaxTO.setAmount(CommonUtil.convertObjToDouble(whereMap.get("REVALIDATE_CHARGE")));
                                    } else {
                                        objChargesServiceTaxTO.setAmount(CommonUtil.convertObjToDouble(whereMap.get("DUPLICATE_CHARGE")));
                                    }
                                    objChargesServiceTaxTO.setService_tax_amt(CommonUtil.convertObjToDouble(whereMap.get("SERVICE_TAX")));
                                    objChargesServiceTaxTO.setTotal_amt(CommonUtil.convertObjToDouble(whereMap.get("TOT_AMT")));
                                }
                                exgStLst = null;
                                exgStLst = (List) sqlMap.executeQueryForList("getSelectSTHeadRemittance", whereMap);
                                whereMap = null;
                                if (exgStLst != null && exgStLst.size() > 0) {
                                    whereMap = (HashMap) exgStLst.get(0);
                                    whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(hash.get("VARIABLE_NO")));
                                    whereMap.put("SERVICE_TAX", objChargesServiceTaxTO.getService_tax_amt());
                                    String exgHd = CommonUtil.convertObjToStr(whereMap.get("EXCHANGE_HD"));
                                    exgStLst = null;
                                    exgStLst = (List) sqlMap.executeQueryForList("getSelectTransCashRemitSTDetails", whereMap);
                                    whereMap = null;
                                    if (exgStLst != null && exgStLst.size() > 0) {
                                        whereMap = (HashMap) exgStLst.get(0);
                                        Date curDate = (Date) currDt.clone();
                                        objChargesServiceTaxTO.setAc_Head(exgHd);
                                        objChargesServiceTaxTO.setAcct_num(CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));
                                        objChargesServiceTaxTO.setAuthorize_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                        objChargesServiceTaxTO.setAuthorize_dt(curDate);
                                        objChargesServiceTaxTO.setAuthorize_status(status);
                                        objChargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                                        objChargesServiceTaxTO.setProd_id(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")));
                                        objChargesServiceTaxTO.setProd_type(CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")));
                                        objChargesServiceTaxTO.setStatus(CommonConstants.STATUS_CREATED);
                                        objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(whereMap.get("TRAN_ID")));
                                        objChargesServiceTaxTO.setCreated_dt(curDate);
                                        objChargesServiceTaxTO.setTrans_dt(curDate);
                                        objChargesServiceTaxTO.setParticulars(CommonUtil.convertObjToStr(whereMap.get("PARTICULARS")));
                                        objChargesServiceTaxTO.setBranchCode(CommonUtil.convertObjToStr(whereMap.get("BRANCH_ID")));
                                        sqlMap.executeUpdate("insertChargesServiceTaxTO", objChargesServiceTaxTO);
                                        whereMap = null;
                                        exgStLst = null;
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
        if (map.containsKey("LINK_BATCH_ID")) {
            System.out.println("#####MAPCONTAINSKEY" + map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("LINK_BATCH_ID"));
            System.out.println(authorizeStatus + "authrize status" + cashAuthMap + "#####LINKBATCHID $$$$$" + linkBatchId);
            ArrayList transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", map);
            if (transferTransList != null && transferTransList.size() > 0) {
                for (int x = 0, y = transferTransList.size(); x < y; x++) {
                    String prodType = "OA";
                    if (prodType.equals(((TxTransferTO) transferTransList.get(x)).getProdType())) {
                        batchId = ((TxTransferTO) transferTransList.get(x)).getBatchId();
                    }
                }
                HashMap transferTransParam = new HashMap();
                transferTransParam.put("BATCH_ID", batchId);
                transferTransParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                transferTransParam.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                //            transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                //            transferTransParam.put("LINK_BATCH_ID", linkBatchId) ;
                transferTransParam.put("REMARKS", "DUE TO PROCSSING CHARGE AUTHORIZE");
                TransferTrans objTrans = new TransferTrans();
                System.out.println("CO_CROSSING" + transferTransParam + "TRANSFERLIST@@@@" + transferTransList);
                objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            }
            //            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
        }
        cashAuthMap = null;
    }

    /**
     * To Insert Remittance Issue Details into REMIT_ISSUE table
     */
    private void insertRemitIssueDetails(String command) throws Exception {
        objRemittanceIssueTO.setStatus(CommonConstants.STATUS_CREATED);
        // Generates issueId
        issueId = getIssueId();
        // Generates variableNo
        if ((operationMode.equals(CommonConstants.REMIT_DUPLICATE)) || (operationMode.equals(CommonConstants.REMIT_REVALIDATE))) {
            variableNo = newVariableNo;
        } else {
            variableNo = getVariableNo();
        }
        //generate the Inst No 1 and Inst No 2
//        setInstrumentNo();
        // Sets autogenerated fields in Remit Issue TO Object
        setAutoGeneratedFieldsForIssueTOs();

        logTO.setData(objRemittanceIssueTO.toString());
        logTO.setPrimaryKey(objRemittanceIssueTO.getKeyData());
        logTO.setStatus(command);
        System.out.println("====objRemittanceIssueTO : " + objRemittanceIssueTO);
        sqlMap.executeUpdate("insertRemittanceIssueTO", objRemittanceIssueTO);
        logDAO.addToLog(logTO);
    }

    /**
     * Updates the Remit Issue Details
     */
    private void updateRemitIssueDetails(String command) throws Exception {
        objRemittanceIssueTO.setStatus(CommonConstants.STATUS_MODIFIED);

        logTO.setData(objRemittanceIssueTO.toString());
        logTO.setPrimaryKey(objRemittanceIssueTO.getKeyData());
        logTO.setStatus(command);
        if (!fromotherDAo) {
            HashMap BatchMap = new HashMap();
            List batchLst = (List) sqlMap.executeQueryForList("getBatchId", objRemittanceIssueTO.getVariableNo());
            if (batchLst != null) {
                if (batchLst.size() > 0) {
                    BatchMap = (HashMap) batchLst.get(0);
                    objRemittanceIssueTO.setBatchId(CommonUtil.convertObjToStr(BatchMap.get("BATCH_ID")));
                    objRemittanceIssueTO.setIssueId(CommonUtil.convertObjToStr(BatchMap.get("ISSUE_ID")));
                }
            }
        }
        Date curDate = (Date) currDt.clone();
        Date AutDt = objRemittanceIssueTO.getAuthorizeDt();
        if (AutDt != null) {
            Date atDate = (Date) curDate.clone();
            atDate.setDate(AutDt.getDate());
            atDate.setMonth(AutDt.getMonth());
            atDate.setYear(AutDt.getYear());
            objRemittanceIssueTO.setAuthorizeDt(atDate);
        } else {
            objRemittanceIssueTO.setAuthorizeDt(objRemittanceIssueTO.getAuthorizeDt());
        }
        Date RevDt = objRemittanceIssueTO.getRevalidateDt();
        if (RevDt != null) {
            Date rvDate = (Date) curDate.clone();
            rvDate.setDate(RevDt.getDate());
            rvDate.setMonth(RevDt.getMonth());
            rvDate.setYear(RevDt.getYear());
            objRemittanceIssueTO.setRevalidateDt(rvDate);
        } else {
            objRemittanceIssueTO.setRevalidateDt(objRemittanceIssueTO.getRevalidateDt());
        }
        Date RevExDt = objRemittanceIssueTO.getRevalidateExpiryDt();
        if (RevExDt != null) {
            Date rvExDate = (Date) curDate.clone();
            rvExDate.setDate(RevExDt.getDate());
            rvExDate.setMonth(RevExDt.getMonth());
            rvExDate.setYear(RevExDt.getYear());
            objRemittanceIssueTO.setRevalidateExpiryDt(rvExDate);
        } else {
            objRemittanceIssueTO.setRevalidateExpiryDt(objRemittanceIssueTO.getRevalidateExpiryDt());
        }
        Date DupDt = objRemittanceIssueTO.getDuplicateDt();
        if (DupDt != null) {
            Date dupDate = (Date) curDate.clone();
            dupDate.setDate(DupDt.getDate());
            dupDate.setMonth(DupDt.getMonth());
            dupDate.setYear(DupDt.getYear());
            objRemittanceIssueTO.setDuplicateDt(dupDate);
        } else {
            objRemittanceIssueTO.setDuplicateDt(objRemittanceIssueTO.getDuplicateDt());
        }
        Date CanDt = objRemittanceIssueTO.getCancelDt();
        if (CanDt != null) {
            Date canDate = (Date) curDate.clone();
            canDate.setDate(CanDt.getDate());
            canDate.setMonth(CanDt.getMonth());
            canDate.setYear(CanDt.getYear());
            objRemittanceIssueTO.setCancelDt(canDate);
        } else {
            objRemittanceIssueTO.setCancelDt(objRemittanceIssueTO.getCancelDt());
        }
        Date StDt = objRemittanceIssueTO.getStatusDt();
        if (StDt != null) {
            Date stDate = (Date) curDate.clone();
            stDate.setDate(StDt.getDate());
            stDate.setMonth(StDt.getMonth());
            stDate.setYear(StDt.getYear());
            objRemittanceIssueTO.setStatusDt(stDate);
        } else {
            objRemittanceIssueTO.setStatusDt(objRemittanceIssueTO.getStatusDt());
        }
        sqlMap.executeUpdate("updateRemittanceIssueTO", objRemittanceIssueTO);
        logDAO.addToLog(logTO);
    }

    /**
     * Update the Status as DELETED in Remit Trans Details
     */
    private void deleteRemitIssueDetails(String command) throws Exception {
        objRemittanceIssueTO.setStatus(CommonConstants.STATUS_DELETED);

        logTO.setData(objRemittanceIssueTO.toString());
        logTO.setPrimaryKey(objRemittanceIssueTO.getKeyData());
        logTO.setStatus(command);
        // Update the Status as DELETED
        System.out.println("$$@@@@!!!@#@@@@@@@objRemittanceIssueTO" + objRemittanceIssueTO);
        if (!fromotherDAo) {
            HashMap BatchMap = new HashMap();
            List batchLst = (List) sqlMap.executeQueryForList("getBatchId", objRemittanceIssueTO.getVariableNo());
            if (batchLst != null) {
                if (batchLst.size() > 0) {
                    BatchMap = (HashMap) batchLst.get(0);
                    objRemittanceIssueTO.setBatchId(CommonUtil.convertObjToStr(BatchMap.get("BATCH_ID")));
                    objRemittanceIssueTO.setIssueId(CommonUtil.convertObjToStr(BatchMap.get("ISSUE_ID")));
                }
            }
        }
        sqlMap.executeUpdate("deleteRemittanceIssueTO", objRemittanceIssueTO);
        logDAO.addToLog(logTO);
    }

    public static void main(String str[]) throws Exception {
        java.io.FileInputStream fstream = new java.io.FileInputStream("D:\\myfile1.txt");
        java.io.ObjectInputStream input = new java.io.ObjectInputStream(fstream);
        HashMap inputMap = (HashMap) input.readObject();
        System.out.println("input Map : " + inputMap);
        RemittanceIssueDAO acobj = new RemittanceIssueDAO();
        acobj.execute(inputMap);
    }

    /**
     * Getter for property instNo1.
     *
     * @return Value of property instNo1.
     */
    public java.lang.String getInstNo1() {
        return instNo1;
    }

    /**
     * Setter for property instNo1.
     *
     * @param instNo1 New value of property instNo1.
     */
    public void setInstNo1(java.lang.String instNo1) {
        this.instNo1 = instNo1;
    }

    /**
     * Getter for property instNo2.
     *
     * @return Value of property instNo2.
     */
    public java.lang.String getInstNo2() {
        return instNo2;
    }

    /**
     * Setter for property instNo2.
     *
     * @param instNo2 New value of property instNo2.
     */
    public void setInstNo2(java.lang.String instNo2) {
        this.instNo2 = instNo2;
    }

    /**
     * Getter for property fromotherDAo.
     *
     * @return Value of property fromotherDAo.
     */
    public boolean isFromotherDAo() {
        return fromotherDAo;
    }

    /**
     * Setter for property fromotherDAo.
     *
     * @param fromotherDAo New value of property fromotherDAo.
     */
    public void setFromotherDAo(boolean fromotherDAo) {
        this.fromotherDAo = fromotherDAo;
    }
}
