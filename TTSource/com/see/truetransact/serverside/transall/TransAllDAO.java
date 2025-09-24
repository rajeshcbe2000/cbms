/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransAllDAO.java
 *
 * Created on Thu Jan 20 17 : 19 : 05 IST 2005
 */
package com.see.truetransact.serverside.transall;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.termloan.TermLoanPenalWaiveOffTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.termloan.loanrebate.LoanRebateTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transall.TransAllTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import java.util.Date;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.serverside.mdsapplication.mdsreceiptentry.MDSReceiptEntryDAO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import java.util.Iterator;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transall.TransAllTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverside.operativeaccount.AccountClosingDAO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import java.util.Map;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * TransAllDAO.
 *
 */
public class TransAllDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransAllTO objTO;
    Rounding rd = new Rounding();
    ArrayList list1 = new ArrayList();
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    private Date intUptoDt = null, interestUptoDt = null;
    private HashMap finalMap = null;
    private List recoveryList = null;
    private List recoveryRowList = null;
    private final String CASH_TO = "CashTransactionTO";
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private MDSReceiptEntryDAO mdsReceiptEntryDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private Iterator processLstIterator;
    private Iterator processLstIterator1;
    double recoveredAmt = 0.0;
    HashMap otherChargesMap;
    private Date properFormatDate;
    private String strRetired = "";
    private final static double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;
    DecimalFormat df = new DecimalFormat("#.##");
    public HashMap erroMap = new HashMap();

    public TransAllDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("getData map : " + map);
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        System.out.println("getData where : " + map.get("MEMBER_NO"));
        if (map.get("RETIRED") != null) {
            strRetired = map.get("RETIRED").toString();
        }
        List list = null;
        if (map.get("MEMBER_NO") != null && map.containsKey("MEMBER_NO")) {
            if (!map.containsKey("RETIRED")) {
                returnMap = getDataFill(map.get("MEMBER_NO").toString(), "NO");
            } else {
                returnMap = getDataFill(map.get("MEMBER_NO").toString(), map.get("RETIRED").toString());
            }
            System.out.println("returnMap : " + returnMap);
            return returnMap;
        }
        if (where.containsKey("TRANS_ALL_ID") && where.get("TRANS_ALL_ID") != null) {
            list = (List) sqlMap.executeQueryForList("TransAll.getSelectTransAll", where);
            returnMap.put("TransAllTO", list);
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("TRANS_ALL_ID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("getRemitTransMap : " + getRemitTransMap);
            list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        list = null;
        System.out.println("getData returnMap : " + returnMap);
        return returnMap;
    }

    private HashMap getDataFill(String memNo, String retired) {
        System.out.println("getDataFill memNo : " + memNo + " retired : " + retired + "currDt : " + currDt);
        HashMap singleRowMap = new HashMap();
        HashMap recoveryListMap = new HashMap();
        finalMap = new HashMap();
        HashMap recoveryMap = null;
        recoveryList = new ArrayList();
        HashMap where = new HashMap();
        List listTL = null, listRD = null, listMDS = null, listSB = null;
        try {
            where.put("MEMBER_NO", memNo);
            where.put("AUTHORIZED_DT", currDt.clone());
            listTL = sqlMap.executeQueryForList("TransAll.getSelTL", where);
            listRD = sqlMap.executeQueryForList("TransAll.getSelRD", where);
            listMDS = sqlMap.executeQueryForList("TransAll.getSelMDS", where);
            listSB = sqlMap.executeQueryForList("TransAll.getSelSB", where);
            System.out.println("TL Size : " + listTL.size());
            if (listTL != null && listTL.size() > 0) {
                try {
                    String prodType = "";
                    String prodID = "";
                    String actNum = "";
                    for (int i = 0; i < listTL.size(); i++) {
                        recoveryMap = (HashMap) listTL.get(i);
                        singleRowMap = new HashMap();
                        prodType = CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE"));
                        prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                        actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                        singleRowMap = calcLoanPayments(actNum, prodID, prodType, "N", retired);
                        System.out.println("singleRowMap : " + singleRowMap);
                        if (singleRowMap != null && singleRowMap.size() > 0) {
                            double total = CommonUtil.convertObjToDouble((singleRowMap.get("PRINCIPAL")))
                                    + CommonUtil.convertObjToDouble((singleRowMap.get("PENAL")))
                                    + CommonUtil.convertObjToDouble((singleRowMap.get("INTEREST")))
                                    + CommonUtil.convertObjToDouble((singleRowMap.get("CHARGES")));
                            HashMap pendingMap = new HashMap();
                            pendingMap.put("ACT_NUM", actNum);
                            pendingMap.put("INITIATED_BRANCH", _branchCode);
                            List pendingAuthlst = ServerUtil.executeQuery("checkPendingAuthTransaction", pendingMap);
                            System.out.println("TL pendingAuthlst : " + pendingAuthlst.size());
                            String fontstart = "", fontEnd = "";
                            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                                HashMap CountMap = (HashMap) pendingAuthlst.get(0);
                                int countTL = CommonUtil.convertObjToInt(CountMap.get("COUNT"));
                                System.out.println("countTL : " + countTL);
                                if (countTL > 0) {
                                    countTL = 0;
                                    pendingAuthlst = null;
                                    fontstart = "<html><font color=red>";
                                    fontEnd = "</font></html>";
                                    total = 0;
                                }
                            }
                            recoveryRowList = new ArrayList();
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE")) + fontEnd);//0
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")) + fontEnd);//1
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")) + fontEnd);//2
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(total)) + fontEnd);//3
                            double p = CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(p)) + fontEnd);//singleRowMap.get("PRINCIPAL")));//4
                            double pe = CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(pe)) + fontEnd);//singleRowMap.get("PENAL")));//5
                            double in = CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(in)) + fontEnd);//singleRowMap.get("INTEREST")));//6
                            recoveryRowList.add(fontstart + "" + fontEnd);//Bonus 07
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")) + fontEnd);//08
                            recoveryRowList.add(fontstart + "" + fontEnd);//Notice //09
                            recoveryRowList.add(fontstart + "" + fontEnd);//Arbitration 10
                            recoveryRowList.add(fontstart + prodID + fontEnd);//prodid 11
                            recoveryRowList.add(fontstart + total + fontEnd);//CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")));//12
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("CLEAR_BALANCE")) + fontEnd);//total);//total);//total 13
//                            if (singleRowMap.containsKey("LAST_INT_CALC_DT") && singleRowMap.get("LAST_INT_CALC_DT") != null && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRowMap.get("LAST_INT_CALC_DT"))), currDt) > 30) {
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("INSTMT_AMT")) + fontEnd);
//                            } else {
//                                recoveryRowList.add(fontstart + "0.0" + fontEnd);
//                            }
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("LOAN_BALANCE_PRINCIPAL")) + fontEnd);
                            recoveryList.add(recoveryRowList);
                        }
                        singleRowMap = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    recoveryRowList = null;
                    listTL = null;
                }
            }
            System.out.println("TD Size : " + listRD.size());
            if (listRD != null && listRD.size() > 0) {
                try {
                    String prodType = "";
                    String prodID = "";
                    String actNum = "";
                    for (int i = 0; i < listRD.size(); i++) {
                        recoveryMap = (HashMap) listRD.get(i);
                        singleRowMap = new HashMap();
                        prodType = CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE"));
                        prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                        actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                        singleRowMap = calcTermDeposits(actNum, prodID, prodType, retired);
                        if (singleRowMap != null && singleRowMap.size() > 0) {
                            String total = CommonUtil.convertObjToStr(singleRowMap.get("TOTAL"));
                            HashMap pendingMap = new HashMap();
                            pendingMap.put("ACT_NUM", actNum);
                            pendingMap.put("INITIATED_BRANCH", _branchCode);
                            List pendingAuthlst = ServerUtil.executeQuery("checkPendingAuthTransaction", pendingMap);
                            System.out.println("TD pendingAuthlst : " + pendingAuthlst.size());
                            String fontstart = "", fontEnd = "";
                            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                                HashMap CountMap = (HashMap) pendingAuthlst.get(0);
                                int countTD = CommonUtil.convertObjToInt(CountMap.get("COUNT"));
                                System.out.println("countTD : " + countTD);
                                if (countTD > 0) {
                                    countTD = 0;
                                    pendingAuthlst = null;
                                    fontstart = "<html><font color=red>";
                                    fontEnd = "</font></html>";
                                    total = "0";
                                }
                            }
                            recoveryRowList = new ArrayList();
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE")) + fontEnd);
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")) + fontEnd);//1
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")) + fontEnd);
                            recoveryRowList.add(fontstart + total + fontEnd);
                            double p = CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(p)) + fontEnd);//singleRowMap.get("PRINCIPAL")));
                            double pe = CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(pe)) + fontEnd);//singleRowMap.get("PENAL")));
                            double in = CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(in)) + fontEnd);//singleRowMap.get("INTEREST")));
                            recoveryRowList.add(fontstart + "" + fontEnd);//Bonus
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("CHARGES")) + fontEnd);
                            recoveryRowList.add(fontstart + "" + fontEnd);//Notice
                            recoveryRowList.add(fontstart + "" + fontEnd);//Arbitration
                            recoveryRowList.add(fontstart + prodID + fontEnd);//prodid
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("PRINCIPAL_OLD")) + fontEnd);//total
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("TOTAL")) + fontEnd); //11
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("INSTMT_AMT")) + fontEnd);
                            recoveryList.add(recoveryRowList);
                        }
                        singleRowMap = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    recoveryRowList = null;
                    listRD = null;
                }
            }
            System.out.println("MDS Size : " + listMDS.size());
            if (listMDS != null && listMDS.size() > 0) {
                try {
                    String prodType = "";
                    String prodID = "";
                    String actNum = "", chittalNo = "";
                    for (int i = 0; i < listMDS.size(); i++) {
                        recoveryMap = (HashMap) listMDS.get(i);
                        singleRowMap = new HashMap();
                        prodType = CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE"));
                        prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                        actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                        System.out.println("MDS actNum : " + actNum + " prodID : " + prodID + " prodType : " + prodType);
                        singleRowMap = calcMDS(actNum, prodID, prodType);
                        System.out.println("MDS singlerowmap : " + singleRowMap);
                        if (singleRowMap != null && singleRowMap.size() > 0) {
                            String subNo = "";
                            if (actNum.indexOf("_") != -1) {
                                subNo = actNum.substring(actNum.indexOf("_") + 1, actNum.length());
                                chittalNo = actNum.substring(0, actNum.indexOf("_"));
                            }
                            HashMap pendingMap = new HashMap();
                            pendingMap.put("SCHEME_NAME", prodID);
                            pendingMap.put("CHITTAL_NO", chittalNo);
                            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            List pendingAuthlst = ServerUtil.executeQuery("checkPendingForAuthorization", pendingMap);
                            String fontStart = "", fontEnd = "";
                            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                                fontStart = "<html><font color=red>";
                                fontEnd = "</font></html>";
                            }
                            if (singleRowMap != null && singleRowMap.size() > 0) {
                                recoveryRowList = new ArrayList();
                                recoveryRowList.add(fontStart + prodType + fontEnd);//CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE")));//PRODTYPE//0
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")) + fontEnd);//1
                                recoveryRowList.add(fontStart + actNum + fontEnd);//CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")));//2
                                if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                                    recoveryRowList.add(fontStart + "0" + fontEnd);//pendingInst);//added by jithin  //3
                                } else {
                                    recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(singleRowMap.get("PENDING_INST")) + fontEnd);//pendingInst);//added by jithin  //3
                                }
                                double p = CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"));
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(df.format(p)) + fontEnd);//singleRowMap.get("PRINCIPAL"))); //4
                                double pe = CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"));
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(df.format(pe)) + fontEnd);//singleRowMap.get("PENAL")));
                                double in = CommonUtil.convertObjToDouble(singleRowMap.get("INTEREST"));
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(df.format(in)) + fontEnd);//singleRowMap.get("INTEREST")));
                                double bo = CommonUtil.convertObjToDouble(singleRowMap.get("BONUS"));
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(df.format(bo)) + fontEnd);//singleRowMap.get("BONUS")));
                                double vch = CommonUtil.convertObjToDouble(singleRowMap.get("CHARGES"));
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(df.format(vch)) + fontEnd);//singleRowMap.get("CHARGES")));
                                recoveryRowList.add(fontStart + "0" + fontEnd);//Notice
                                recoveryRowList.add(fontStart + "0" + fontEnd);//Arbitration
                                recoveryRowList.add(fontStart + prodID + fontEnd);//prodid
                                recoveryRowList.add(fontStart + "" + fontEnd);//total
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(singleRowMap.get("PENDING_INST")) + fontEnd);//pendingInst);//CommonUtil.convertObjToStr(singleRowMap.get("PENDING_INST"))); 
                                recoveryRowList.add(fontStart + CommonUtil.convertObjToStr(singleRowMap.get("INSTMT_AMT")) + fontEnd);
                                recoveryList.add(recoveryRowList);
                            }
                        }
                        singleRowMap = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    recoveryRowList = null;
                    listMDS = null;
                }
            }
            System.out.println("SB Size : " + listSB.size());
            if (listSB != null && listSB.size() > 0) {
                try {
                    String prodType = "";
                    String prodID = "";
                    String actNum = "";
                    for (int i = 0; i < listSB.size(); i++) {
                        recoveryMap = (HashMap) listSB.get(i);
                        singleRowMap = new HashMap();
                        prodType = CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE"));
                        prodID = CommonUtil.convertObjToStr(recoveryMap.get("PROD_ID"));
                        actNum = CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM"));
                        singleRowMap = calcGL(actNum, prodID, prodType, 0, retired);
                        System.out.println("SB singleRowMap : " + singleRowMap);
                        if (singleRowMap != null && singleRowMap.size() > 0) {
                            double total = 0.0;
                            HashMap pendingMap = new HashMap();
                            pendingMap.put("ACT_NUM", actNum);
                            pendingMap.put("INITIATED_BRANCH", _branchCode);
                            List pendingAuthlst = ServerUtil.executeQuery("checkPendingAuthTransaction", pendingMap);
                            System.out.println("SB pendingAuthlst : " + pendingAuthlst.size());
                            String fontstart = "", fontEnd = "";
                            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                                HashMap CountMap = (HashMap) pendingAuthlst.get(0);
                                int countSB = CommonUtil.convertObjToInt(CountMap.get("COUNT"));
                                System.out.println("countSB : " + countSB);
                                if (countSB > 0) {
                                    countSB = 0;
                                    pendingAuthlst = null;
                                    fontstart = "<html><font color=red>";
                                    fontEnd = "</font></html>";
                                    total = 0;
                                }
                            }
                            double in = 0.0;
                            HashMap interestMap = new HashMap();
                            interestMap.put("ACT_NUM", actNum);
                            interestMap.put("ASONDATE", currDt.clone());
                            List interestList = ServerUtil.executeQuery("getSuspenseCalculationinAccounts", interestMap);
                            if (interestList != null && interestList.size() > 0) {
                                interestMap = (HashMap) interestList.get(0);
                                System.out.println("interestMap : " + interestMap);
                                in = CommonUtil.convertObjToDouble(interestMap.get("INTEREST"));
                            }
                            //double tot = CommonUtil.convertObjToDouble(total);
                            total = CommonUtil.convertObjToDouble((singleRowMap.get("PRINCIPAL")))
                                    + CommonUtil.convertObjToDouble((singleRowMap.get("PENAL")))
                                    + in + CommonUtil.convertObjToDouble((singleRowMap.get("CHARGES")));
                            recoveryRowList = new ArrayList();
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PRODTYPE")) + fontEnd);
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("PROD_DESC")) + fontEnd);//1
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(recoveryMap.get("ACT_NUM")) + fontEnd);
                            recoveryRowList.add(fontstart + df.format(total) + fontEnd);//bbb
                            double p = CommonUtil.convertObjToDouble(singleRowMap.get("PRINCIPAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(p)) + fontEnd);//singleRowMap.get("PRINCIPAL")));
                            double pe = CommonUtil.convertObjToDouble(singleRowMap.get("PENAL"));
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(pe)) + fontEnd);//singleRowMap.get("PENAL")));                            
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(df.format(in)) + fontEnd);
                            recoveryRowList.add(fontstart + "" + fontEnd);//Bonus
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr("0") + fontEnd);
                            recoveryRowList.add(fontstart + "" + fontEnd);//Notice
                            recoveryRowList.add(fontstart + "" + fontEnd);//Arbitration
                            recoveryRowList.add(fontstart + prodID + fontEnd);//prodid
                            double tot = CommonUtil.convertObjToDouble(singleRowMap.get("TOTAL"));
                            recoveryRowList.add(fontstart + df.format(tot) + fontEnd);//total
                            total = total - CommonUtil.convertObjToDouble((singleRowMap.get("PENAL")));
                            recoveryRowList.add(fontstart + df.format(total) + fontEnd);//total
                            recoveryRowList.add(fontstart + CommonUtil.convertObjToStr(singleRowMap.get("INSTMT_AMT")) + fontEnd);//total
                            recoveryList.add(recoveryRowList);
                            singleRowMap = null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    recoveryRowList = null;
                    listSB = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.gc();
            recoveryMap = null;
        }
        System.out.println("recoveryList : " + recoveryList);
        recoveryListMap.put("RECOVERY_LIST_TABLE_DATA", recoveryList);
        return recoveryListMap;
    }

    private String getTransAllNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANS_ALL_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
//            list1.clear();
            list1 = new ArrayList();
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            returnMap = new HashMap();
            System.out.println("insertData map : " + map);
            if (map.containsKey("RECOVERY_PROCESS_LIST")) {
                intUptoDt = currDt;
                String retVal = insertTransAll(map);
                processTransactionPart(map);
                if (returnMap == null) {
                    returnMap = new HashMap();
                }
                if (retVal != null) {
                    returnMap.put("STATUS", retVal);
                } else {
                    returnMap.put("STATUS", "");
                }
            }
            sqlMap.commitTransaction();
        } catch (TTException t) {
            list1 = (ArrayList) t.getExceptionHashMap().get(CommonConstants.EXCEPTION_LIST);
            returnMap.put("list", list1);
            list1 = new ArrayList();
            sqlMap.rollbackTransaction();
            System.out.println("Exception while inserting time : " + t);
            throw new TransRollbackException(t);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateTransAllTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteTransAllTO", objTO);
            //System.out.println("CommonConsobjTOobjTO-" + objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            TransAllDAO dao = new TransAllDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap checkOPValidation(TransactionTO transactionTo) throws SQLException {
        System.out.println("checkOPValidation transactionTo" + transactionTo);
        HashMap chqMap = new HashMap();
        HashMap inputMap = new HashMap();
        String prodId = CommonUtil.convertObjToStr(transactionTo.getProductId());
        String prodType = CommonUtil.convertObjToStr(transactionTo.getProductType());
        String accNo = CommonUtil.convertObjToStr(transactionTo.getDebitAcctNo());
        double amount = CommonUtil.convertObjToDouble(transactionTo.getTransAmt());
        double availBal = 0.0;
        double shadowCredit = 0.0;
        double shadowDebit = 0.0;
        double minBalWitChq = 0.0;
        double minBalWitOutChq = 0.0;
        double clearBal = 0.0;
        String chqAllowed = null;
        HashMap detailedActMap = null;
        System.out.println("prodId" + prodId + "prodType" + prodType + "accNo" + accNo + "amount" + amount);
        inputMap.put("ACC_NO", accNo);
        try {
            List actDetails = (List) sqlMap.executeQueryForList("getOpActBalDetails", inputMap);
            System.out.println("chq size------" + actDetails.size());
            if (actDetails == null || actDetails.size() <= 0) {
                chqMap.put("ERRORLIST", "This is not issued/valid. A/c No:" + transactionTo.getDebitAcctNo());
                return chqMap;
            }
            if (actDetails != null && actDetails.size() > 0) {
                detailedActMap = (HashMap) actDetails.get(0);
                System.out.println("detailedActMap" + detailedActMap);
                if (detailedActMap != null && detailedActMap.size() > 0) {
                    availBal = CommonUtil.convertObjToDouble(detailedActMap.get("AVAILABLE_BALANCE"));
                    clearBal = CommonUtil.convertObjToDouble(detailedActMap.get("CLEAR_BALANCE"));
                    shadowCredit = CommonUtil.convertObjToDouble(detailedActMap.get("SHADOW_CREDIT"));
                    shadowDebit = CommonUtil.convertObjToDouble(detailedActMap.get("SHADOW_DEBIT"));
                    minBalWitChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_W_CHK"));
                    minBalWitOutChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_WT_CHK"));
                    chqAllowed = CommonUtil.convertObjToStr(detailedActMap.get("CHK_ALLOWED"));
                    if (shadowCredit > 0 || shadowDebit > 0) {
                        chqMap.put("ERRORLIST", "This account sent for Clearing, Pending for Authorization..." + transactionTo.getDebitAcctNo());
                        return chqMap;
                    } else if (amount > availBal) {
                        chqMap.put("ERRORLIST", "Balance Insufficient for A/C No :" + transactionTo.getDebitAcctNo());
                        return chqMap;
                    } else if (availBal > amount) {
                        if (chqAllowed != null && !chqAllowed.equals("")) {
                            double aftDebitAmt = availBal - amount;
                            if (chqAllowed.equals("Y")) {

                                if (aftDebitAmt < minBalWitChq) {
                                    chqMap.put("ERRORLIST", "Balance will fall under min balance, cannot do transaction with A/C No: " + transactionTo.getDebitAcctNo());
                                    return chqMap;
                                }
                            } else {
                                if (aftDebitAmt < minBalWitOutChq) {
                                    chqMap.put("ERRORLIST", "Balance will fall under min balance, cannot do transaction with A/C No: " + transactionTo.getDebitAcctNo());
                                    return chqMap;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chqMap;
    }

    public HashMap checkSAValidation(TransactionTO transactionTo) throws SQLException {
        System.out.println("checkSAValidation transactionTo" + transactionTo);
        HashMap chqMap = new HashMap();
        HashMap inputMap = new HashMap();
        String prodId = CommonUtil.convertObjToStr(transactionTo.getProductId());
        String prodType = CommonUtil.convertObjToStr(transactionTo.getProductType());
        String accNo = CommonUtil.convertObjToStr(transactionTo.getDebitAcctNo());
        double amount = CommonUtil.convertObjToDouble(transactionTo.getTransAmt());
        double availBal = 0.0;
        double shadowCredit = 0.0;
        double shadowDebit = 0.0;
        double minBalWitChq = 0.0;
        double minBalWitOutChq = 0.0;
        double clearBal = 0.0;
        String chqAllowed = null;
        HashMap detailedActMap = null;
        System.out.println("prodId" + prodId + "prodType" + prodType + "accNo" + accNo + "amount" + amount);
        inputMap.put("ACC_NO", accNo);
        try {
            List actDetails = (List) sqlMap.executeQueryForList("getOpActBalDetails", inputMap);
            System.out.println("chq size------" + actDetails.size());
            if (actDetails == null || actDetails.size() <= 0) {
                chqMap.put("ERRORLIST", "This is not issued/valid. A/c No:" + transactionTo.getDebitAcctNo());
                return chqMap;
            }
            if (actDetails != null && actDetails.size() > 0) {
                detailedActMap = (HashMap) actDetails.get(0);
                System.out.println("detailedActMap" + detailedActMap);
                if (detailedActMap != null && detailedActMap.size() > 0) {
                    availBal = CommonUtil.convertObjToDouble(detailedActMap.get("AVAILABLE_BALANCE"));
                    clearBal = CommonUtil.convertObjToDouble(detailedActMap.get("CLEAR_BALANCE"));
                    shadowCredit = CommonUtil.convertObjToDouble(detailedActMap.get("SHADOW_CREDIT"));
                    shadowDebit = CommonUtil.convertObjToDouble(detailedActMap.get("SHADOW_DEBIT"));
                    minBalWitChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_W_CHK"));
                    minBalWitOutChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_WT_CHK"));
                    chqAllowed = CommonUtil.convertObjToStr(detailedActMap.get("CHK_ALLOWED"));
                    if (shadowCredit > 0 || shadowDebit > 0) {
                        chqMap.put("ERRORLIST", "This account sent for Clearing, Pending for Authorization..." + transactionTo.getDebitAcctNo());
                        return chqMap;
                    } else if (amount > availBal) {
                        chqMap.put("ERRORLIST", "Balance Insufficient for A/C No :" + transactionTo.getDebitAcctNo());
                        return chqMap;
                    } else if (availBal > amount) {
                        if (chqAllowed != null && !chqAllowed.equals("")) {
                            double aftDebitAmt = availBal - amount;
                            if (chqAllowed.equals("Y")) {

                                if (aftDebitAmt < minBalWitChq) {
                                    chqMap.put("ERRORLIST", "Balance will fall under min balance, cannot do transaction with A/C No: " + transactionTo.getDebitAcctNo());
                                    return chqMap;
                                }
                            } else {
                                if (aftDebitAmt < minBalWitOutChq) {
                                    chqMap.put("ERRORLIST", "Balance will fall under min balance, cannot do transaction with A/C No: " + transactionTo.getDebitAcctNo());
                                    return chqMap;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chqMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        //block
        intUptoDt = currDt;
        System.out.println("TransAllMap DAO===" + map);
        System.out.println("execute _branchCode : " + _branchCode + "intUptoDt1 : " + intUptoDt);
        interestUptoDt = intUptoDt;
        returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        returnMap = new HashMap();
        try {
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                objTO = (TransAllTO) map.get("TransAllTO");
                final String command = objTO.getCommand();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    if (map.containsKey("TransactionTO")) {
                        TransactionTO transactionTO = null;
                        HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                        if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                        }
                        transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                        String transType = CommonUtil.convertObjToStr(transactionTO.getTransType());
                        String prodType = CommonUtil.convertObjToStr(transactionTO.getProductType());
                        System.out.println("prodType" + prodType + "transType" + transType);
                        if (transType != null && !transType.equals("") && transType.equals(CommonConstants.TX_TRANSFER)) {
                            if (prodType != null && !prodType.equals("") && prodType.equals(CommonConstants.TXN_PROD_TYPE_OPERATIVE)) {
                                erroMap = checkOPValidation(transactionTO);
                            }
//                            if (prodType != null && !prodType.equals("") && prodType.equals(CommonConstants.TXN_PROD_TYPE_SUSPENSE)){
//                                erroMap = checkSAValidation(transactionTO);
//                            }
                            if (erroMap != null && erroMap.containsKey("ERRORLIST")) {
                                return erroMap;
                            }
                        }
                    }
                    insertData(map);
                    returnMap.put("list", list1);
                    returnMap.put("SUCCESS_STATUS", "SUCCESS");
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            } else {
                System.out.println("Authorize time map : " + map);
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                System.out.println("authMap : " + authMap);
                if (authMap != null) {
                    authorize(authMap, map);
                }
            }
        } catch (TTException t) {
            System.out.println("inside command insert : ");
            list1 = null;
            returnMap.put("list", list1);
            returnMap.put("SUCCESS_STATUS", "FAILURE");
        } finally {
            destroyObjects();
        }
        System.out.println("execute returnMap : " + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map, HashMap transAllMap) throws Exception {
        String status = (String) CommonUtil.convertObjToStr(map.get("STATUS"));
        //status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        HashMap selectedMap = (HashMap) transAllMap.get(CommonConstants.AUTHORIZEMAP);
        System.out.println("transAllMap : " + transAllMap + "map : " + map + " selectedMap : " + selectedMap);
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransAllTO transAllTo = new TransAllTO();
        TransactionTO objTransactionTO = null;
        if (transAllMap.containsKey("TransAllTO")) {
            transAllTo = (TransAllTO) transAllMap.get("TransAllTO");
        }
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("transAllMap : " + transAllMap);
            TransactionTO transactionTO = new TransactionTO();
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt.clone());
            HashMap dataMap = new HashMap();
            if (transAllMap.containsKey("TransactionTO")) {
                String actNum = "";
                String schemeName = "";
                if (transAllMap.containsKey("MODE") && (transAllMap.get("MODE").equals("AUTHORIZED") || transAllMap.get("MODE").equals("REJECTED"))) {
                    HashMap transMap = new HashMap();
                    transMap.put("TRANS_ALL_ID", selectedMap.get("TRANS_ALL_ID"));
                    List transList = sqlMap.executeQueryForList("TransAll.getSelectTransAll", transMap);
                    System.out.println("####### selectedList###Map :" + transList + "SIZE" + transList.size());
                    if (transList != null && transList.size() > 0) {
                        for (int i = 0; i < transList.size(); i++) {
                            transAllTo = (TransAllTO) transList.get(i);
                            System.out.println("transAllTo :" + transAllTo);
                            schemeName = CommonUtil.convertObjToStr(transAllTo.getSchName());
                            actNum = CommonUtil.convertObjToStr(transAllTo.getAcNo());
                            double principalAmt = CommonUtil.convertObjToDouble(transAllTo.getPayingAmt());
                            double loanprincipalAmt = CommonUtil.convertObjToDouble(transAllTo.getPrincipal());
                            if ((schemeName != null && !schemeName.equals("") && schemeName.length() > 0 && schemeName.equals("SA")) && transAllMap.get("MODE").equals("AUTHORIZED")) {
                                HashMap inputMap = new HashMap();
                                double interest = CommonUtil.convertObjToDouble(transAllTo.getInterest());
//                                recoveredAmt = CommonUtil.convertObjToDouble(transAllTo.getTotprincipal());
                                String clockNo = CommonUtil.convertObjToStr(transAllTo.getClockNo());
                                String transAllId = CommonUtil.convertObjToStr(transAllTo.getTransallId());
                                String prodId = CommonUtil.convertObjToStr(transAllTo.getProdId());
                                if (principalAmt > 0 || interest > 0) {
                                    inputMap.put("ACCT_NUM", actNum);
                                    inputMap.put("PAID_AMOUNT", principalAmt);
                                    inputMap.put("INTEREST", interest);
                                    inputMap.put("CLOCK_NO", clockNo);
                                    inputMap.put("TRANS_ALL_ID", transAllId);
                                    inputMap.put("PRODUCT_ID", prodId);
                                    inputMap.put("PAID_DATE", CommonUtil.getProperDate(currDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transAllTo.getStatusDt()))));
                                    inputMap.put("TRANS_TYPE", CommonConstants.CREDIT);
                                    updateSABalance(inputMap);
                                    if (principalAmt > 0) {
                                        //Update suspense_last_int_calc_dt for particular suspense account if prinicipal amount has been paid
                                        HashMap lastIntCalcMap = new HashMap();
                                        lastIntCalcMap.put("SUSPENSE_LAST_INT_CALC_DT", CommonUtil.getProperDate(currDt, DateUtil.addDays(currDt, -1)));
                                        lastIntCalcMap.put("SUSPENSE_ACCT_NUM", actNum);
                                        sqlMap.executeUpdate("updateSuspenseLastIntCalcDt", lastIntCalcMap);
                                    }
                                }
                            } else if (schemeName != null && !schemeName.equals("") && schemeName.length() > 0 && (schemeName.equals("TL") || schemeName.equals("AD")) && transAllMap.get("MODE").equals("REJECTED")) {
                                HashMap rejectMap = new HashMap();
                                rejectMap.put("ACCT_STATUS", CommonConstants.NEW);
                                rejectMap.put("ACCT_NUM", actNum);
                                sqlMap.executeUpdate("updateStatusForAccountTL", rejectMap);
                            } else if (loanprincipalAmt > 0 && schemeName != null && !schemeName.equals("") && schemeName.length() > 0 && (schemeName.equals("TL") || schemeName.equals("AD")) && transAllMap.get("MODE").equals("AUTHORIZED")) {
                                HashMap authorizeMap = new HashMap();
                                authorizeMap.put("LAST_CALC_DT",DateUtil.addDays((Date)currDt.clone(), -1));
                                authorizeMap.put("ACCOUNTNO",actNum);
                                sqlMap.executeUpdate("updateclearBal", authorizeMap);
                            }
                        }
                    }
                }
                System.out.println("transactionTO inside : " + transactionTO);
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) transAllMap.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                System.out.println("transactionTO after : " + transactionTO);
                transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                HashMap renewalTransactionMap = new HashMap();
                renewalTransactionMap.put(CommonConstants.BRANCH_ID, transAllMap.get(CommonConstants.BRANCH_ID));
                renewalTransactionMap.put(CommonConstants.USER_ID, transAllMap.get(CommonConstants.USER_ID));
                renewalTransactionMap.put("DEPOSIT_CLOSING", "DEPOSIT_CLOSING");
                renewalTransactionMap.put("SALARY_RECOVERY_TRANSACTION", "SALARY_RECOVERY_TRANSACTION");
                renewalTransactionMap.put("NON_MDS_TRANSACTIOIN", "NON_MDS_TRANSACTIOIN");
                renewalTransactionMap.put(CommonConstants.INITIATED_BRANCH, transAllMap.get(CommonConstants.BRANCH_ID));
                renewalTransactionMap.put("TRANS_ALL_ID", map.get("TRANS_ALL_ID"));
                renewalTransactionMap.put("TRANS_DT", currDt);
                renewalTransactionMap.put("AC_NO", CommonUtil.convertObjToStr(transAllTo.getAcNo()));
                System.out.println("renewalTransactionMap : " + renewalTransactionMap);
                // transactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")), status, renewalTransactionMap);
                if (transAllMap.containsKey("MODE") && (transAllMap.get("MODE").equals("REJECTED"))) {
                    objTransactionTO = new TransactionTO();
                    objTransactionTO.setBatchId(CommonUtil.convertObjToStr(transAllTo.getTransallId()));
                    objTransactionTO.setTransId(CommonUtil.convertObjToStr(transactionTO.getTransId()));
                    objTransactionTO.setBranchId(_branchCode);
                    sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                }
                List MDSTxnAvailableorNot = sqlMap.executeQueryForList("getMDSTransactionAvailableorNot", renewalTransactionMap);
                if (MDSTxnAvailableorNot != null && MDSTxnAvailableorNot.size() > 0) {
                    System.out.println(" MDSTxnAvailableorNot : " + MDSTxnAvailableorNot);
                    HashMap MDSMap = new HashMap();
                    HashMap MDSMap1 = new HashMap();
                    for (int i = 0; i < MDSTxnAvailableorNot.size(); i++) {
                        MDSMap1 = (HashMap) MDSTxnAvailableorNot.get(i);
                        System.out.println(" MDSmap : " + MDSMap1);
                        MDSMap1.put("FROM_RECOVERY_TALLY", "FROM_RECOVERY_TALLY");
                        MDSMap1.put("VOUCHER_DETAILS", "VOUCHER_DETAILS");
                        mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                        MDSMap = mdsReceiptEntryDAO.executeQuery(MDSMap1);   // INSERT_TRANSACTION
                        System.out.println(" MDSmap after executequery: " + MDSMap);
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        authDataMap.put("TRANS_ID", MDSMap1.get("NET_TRANS_ID"));
                        arrList.add(authDataMap);
                        singleAuthorizeMap.put("NET_TRANS_ID", MDSMap1.get("NET_TRANS_ID"));
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, transAllMap.get(CommonConstants.BRANCH_ID));
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        MDSMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                        mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                        MDSMap.put(CommonConstants.BRANCH_ID, transAllMap.get(CommonConstants.BRANCH_ID));
                        MDSMap.put(CommonConstants.USER_ID, transAllMap.get(CommonConstants.USER_ID));
                        MDSMap.put("MDS_TRANS_ALL_AUTHORIZATION", "MDS_TRANS_ALL_AUTHORIZATION");
                        System.out.println(" MDSmap before execute : " + MDSMap);
                        mdsReceiptEntryDAO.execute(MDSMap);
                    }
                }
                sqlMap.executeUpdate("authorizeTransAll", map);
                transactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(map.get("TRANS_ALL_ID")), status, renewalTransactionMap);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            System.out.println("Exception in Authorize time : " + e);
        } finally {
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        intUptoDt = currDt;
        System.out.println("executeQuery obj : " + obj + "intUptoDt : " + intUptoDt);
        interestUptoDt = intUptoDt;
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }

    //Added By Akhila
    public static double monthsBetween(Date date1, Date date2) {
        Rounding rod = new Rounding();
        //System.out.println("(date2.getTime() - date1.getTime())" + (date2.getTime() - date1.getTime()));
        //System.out.println("AVERAGE_MILLIS_PER_MONTH" + AVERAGE_MILLIS_PER_MONTH);
        //System.out.println("bbb" + (int) ((date2.getTime() - date1.getTime()) / AVERAGE_MILLIS_PER_MONTH));
        double d = ((date2.getTime() - date1.getTime()) / AVERAGE_MILLIS_PER_MONTH);
        //System.out.println("dateeee.." + (int) rod.getNearest((long) (d * 100), 100) / 100);
        return (int) rod.getNearest((long) (d * 100), 100) / 100;
    }

    public HashMap calcGL(String actNo, String schemeName, String prodType, double glAmount, String retired) throws Exception {
        Double penal = 0.0;
        if (prodType.equals(TransactionFactory.SUSPENSE)) {
            glAmount = 0.0;
            HashMap map1 = new HashMap();
            map1.put("CLOCK_NO", actNo);
            map1.put("AUTHORIZED_DT", currDt.clone());
            List aList = sqlMap.executeQueryForList("GetSuspenseDuedetails3", map1);
            if (aList.size() > 0) {
                for (int j = 0; j < aList.size(); j++) {
                    HashMap newMap = (HashMap) aList.get(j);
                    glAmount += CommonUtil.convertObjToDouble(newMap.get("AMT"));
                }
            }

            map1.put("ACT_NUM", actNo);
            map1.put("ASONDATE", currDt.clone());
            HashMap lstDueMap = new HashMap();
            List lstDue = sqlMap.executeQueryForList("getSuspenseCalculationinAccountsTrans", map1);
            if (lstDue != null && lstDue.size() > 0) {
                penal = 0.0;
                lstDueMap = (HashMap) lstDue.get(0);
                if (lstDueMap != null && lstDueMap.size() > 0 && lstDueMap.containsKey("INTEREST")) {
                    penal = CommonUtil.convertObjToDouble(lstDueMap.get("INTEREST"));
                }
            }
        }
        if (retired != null && retired.equals("YES")) {
            HashMap map2 = new HashMap();
            HashMap newMap1 = new HashMap();
            map2.put("ACT_NUM", actNo);
            List aList1 = sqlMap.executeQueryForList("GetSuspenseDueDetailsRetiredBalance", map2);
            for (int j = 0; j < aList1.size(); j++) {
                newMap1 = (HashMap) aList1.get(j);
                if (newMap1 != null && newMap1.size() > 0) {
                    glAmount = CommonUtil.convertObjToDouble(newMap1.get("AMOUNT"));
                }
            }
        }
        HashMap dataMap = new HashMap();
        if (glAmount > 0) {
            dataMap.put("PRINCIPAL", new Double((glAmount)));
            dataMap.put("TOTAL_DEMAND", new Double((glAmount + penal)));
        } else {
            dataMap.put("PRINCIPAL", new Double((0)));
            dataMap.put("TOTAL_DEMAND", new Double((0)));
        }
        dataMap.put("INTEREST", new Double(penal));
        dataMap.put("PENAL", new Double(0));
        dataMap.put("CHARGES", new Double(0));
        return dataMap;
    }

//    public HashMap calcGL(String actNo, String schemeName, String prodType, double glAmount, String retired) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap map1 = new HashMap();
//        HashMap newMap = new HashMap();
//        Calendar cal = null;
//        cal = Calendar.getInstance();
//        cal.setTime(currDt);
//        cal.add(Calendar.MONTH, 0);
//        cal.set(Calendar.DATE, 1);
//        Date currDt1 = cal.getTime();
//        map1.put("CLOCK_NO", actNo);
//        map1.put("AUTHORIZED_DT", setProperDtFormat(currDt1));
//        map1.put("CURR_DT",currDt);
//        System.out.println("calcGL actNo : " + actNo + " currDt : " + currDt);
//        List aList = sqlMap.executeQueryForList("GetSuspenseDuedetails", map1);
//        if (aList != null && aList.size() > 0) {
//            for (int i = 0; i < aList.size(); i++) {
//                newMap = (HashMap) aList.get(i);
//                glAmount += CommonUtil.convertObjToDouble(newMap.get("DUE"));
//            }
//        }
//        System.out.println("glAmount : " + glAmount);
//        if (glAmount > 0) {
//            Double penal = 0.0;
//            String penaln = "";
//            String actNum = "";
//            newMap = new HashMap();
//            HashMap lstDueMap = new HashMap();
//            actNum = actNo;
//            map1 = new HashMap();
//            cal = Calendar.getInstance();
//            cal.setTime(currDt);
//            cal = null;
//            cal = Calendar.getInstance();
//            cal.setTime(currDt);
//            cal.add(Calendar.MONTH, -1);
//            cal.set(Calendar.DATE, 1);
//            currDt1 = cal.getTime();
//            map1.put("ACT_NUM", actNum);
//            map1.put("INSTALLMENT_DATE", setProperDtFormat(currDt1));
//            List lstDue = sqlMap.executeQueryForList("getDueDetails2", map1);
//            if (lstDue != null && lstDue.size() > 0) {
//                penal = 0.0;
//                HashMap shpMastMode = new HashMap();
//                List intRtList = sqlMap.executeQueryForList("getSelIntRt", shpMastMode);
//                HashMap intRtMap = new HashMap();
//                intRtMap = (HashMap) intRtList.get(0);
//                int intRate = CommonUtil.convertObjToInt(intRtMap.get("INTEREST_RATE").toString());
//                int grPd = CommonUtil.convertObjToInt(intRtMap.get("GRACE_PERIOD_DAYS").toString());
//                for (int j = 0; j < lstDue.size(); j++) {
//                    lstDueMap = (HashMap) lstDue.get(j);
//                    Date date2 = DateUtil.getDateMMDDYYYY(lstDueMap.get("INSTALLMENT_DATE").toString());
//                    Double instAmt = CommonUtil.convertObjToDouble(lstDueMap.get("INSTALLMENT_AMOUNT"));
//                    Date tempDate = (Date) currDt1.clone();
//                    System.out.println("tempDate--Jeff--"+tempDate);
//                    cal = null;
//                    cal = Calendar.getInstance();
//                    cal.setTime(date2);
//                    cal.add(Calendar.MONTH, 1);
//                    date2 = cal.getTime();
//                    cal.setTime(tempDate);
//                    cal.add(Calendar.MONTH, 1);
//                    tempDate = cal.getTime();
//                    int dd = (int) DateUtil.dateDiff(date2, tempDate) + 1;
//                    penal = penal + ((dd - grPd) * instAmt * intRate / 36500);
//                    penaln = String.valueOf(df.format(penal));
//                    penal = CommonUtil.convertObjToDouble(penaln);
//                }
//                penal = Math.round(penal * 100.0) / 100.0;
//                penal = CommonUtil.convertObjToDouble(Math.round(penal));
//            }
//            if (retired != null && retired.equals("YES")) {
//                HashMap map2 = new HashMap();
//                HashMap newMap1 = new HashMap();
//                map2.put("ACT_NUM", actNo);
//                List aList1 = sqlMap.executeQueryForList("TransAll.getSumSuspence", map2);
//                for (int j = 0; j < aList1.size(); j++) {
//                    newMap1 = (HashMap) aList1.get(j);
//                    dataMap.put("PRINCIPAL", new Double(newMap1.get("AMOUNT").toString()));
//                    dataMap.put("TOTAL", new Double(newMap1.get("AMOUNT").toString()) + new Double(penal));
//                }
//            } else {
//                dataMap.put("PRINCIPAL", new Double(glAmount));
//                dataMap.put("TOTAL", new Double(glAmount + penal));
//            }
//            dataMap.put("INTEREST", new Double(penal));
//            dataMap.put("PENAL", new Double(0));
//            dataMap.put("CHARGES", new Double(0));
//            dataMap.put("CHARGES", new Double(0));
//            dataMap.put("INSTMT_AMT", glAmount);
//        }
//        return dataMap;
//    }
    public HashMap calcMDS(String chittal_No, String schemeName, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap productMap = new HashMap();
        HashMap installmentMap = new HashMap();
        String chittalNo = "";
        String calculateIntOn = "";
        String subNo = "";
        if (chittal_No.indexOf("_") != -1) {
            chittalNo = chittal_No.substring(0, chittal_No.indexOf("_"));
            subNo = chittal_No.substring(chittal_No.indexOf("_") + 1, chittal_No.length());
        }
        dataMap.put("CHITTAL_NO", chittal_No);
        dataMap.put("SCHEME_NAME", schemeName);
        System.out.println("calcMDS Chittal_No : " + chittal_No + " Sun BNo : " + subNo + " SCHEMENAME : " + dataMap.get("SCHEME_NAME"));
        double insAmt = 0.0;
        long pendingInst = 0;
        int divNo = 0;
        long insDueAmt = 0;
        int noOfInsPaid = 0;
        int instDay = 1;
        boolean prized = false;
        int curInsNo = 0;
        Date instDate = null;
        boolean bonusAvailabe = true;
        ArrayList penalList = new ArrayList();
        ArrayList bonusList = new ArrayList();
        ArrayList discountList = new ArrayList();
        long noOfInstPay = 0;
        int totIns = 0;
        Date startDate = null;
        Date insDate = null;
        int startMonth = 0;
        int insMonth = 0;
        int penalGracePeriod = 0;
        String penalGracePeriodType = "";
        boolean notPaidThisMonth = false;
        HashMap mdsMap = new HashMap();
        mdsMap.put("CHITTAL_NO", chittalNo);
        List depositMaxList = sqlMap.executeQueryForList("getSelectMDSMaxtransDt", mdsMap);
        if (depositMaxList != null && depositMaxList.size() > 0) {
            mdsMap = (HashMap) depositMaxList.get(0);
            Date date = (Date) currDt.clone();
            Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(mdsMap.get("TRANS_DT")));
            int currentMonth = (int)date.getMonth()+1;
            int maxTxnMonth = (int)maxDate.getMonth()+1;
            int currentYear = (int)date.getYear()+1900;
            int maxTxnYear = (int)maxDate.getYear()+1900;
            System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth +" curr year : " + currentYear + " last transaction year : " + maxTxnYear);
            if ((maxTxnMonth != currentMonth) && (maxTxnYear == currentYear)) {
                notPaidThisMonth = true;
                System.out.println("Please Continue make payment nextmonth : " + chittalNo);
            }else{
                notPaidThisMonth = false;
            }
//        }else{
//            notPaidThisMonth = true;
        }
        whereMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        whereMap.put("CHITTAL_NO", chittalNo);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
        System.out.println("lst : " + lst);
        String bonusFirstInst = "";
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
            totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            startMonth = insDate.getMonth();
            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            penalGracePeriod = CommonUtil.convertObjToInt(productMap.get("PENAL_GRACE_PERIOD"));
            penalGracePeriodType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
        }
        int startNoForPenal = 0;
        int addNo = 1;
        int firstInst_No = -1;
        if (bonusFirstInst.equals("Y")) {
            startNoForPenal = 1;
            addNo = 0;
            firstInst_No = 0;
        }
        List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
        if (insList != null && insList.size() > 0) {
            whereMap = (HashMap) insList.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
        }
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", chittalNo);
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (chitLst != null && chitLst.size() > 0) {
            chittalMap = (HashMap) chitLst.get(0);
            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
        }
        HashMap insDateMap = new HashMap();
        System.out.println("DIVISION_NO : " + CommonUtil.convertObjToStr(String.valueOf(divNo)));
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(String.valueOf(divNo)));
        insDateMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        insDateMap.put("CURR_DATE", CommonUtil.getProperDate(currDt, intUptoDt));
        insDateMap.put("ADD_MONTHS", "-1");
        List insDateLst = sqlMap.executeQueryForList("getTransAllMDSCurrentInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);
            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
            System.out.println("insDateMap" + insDateMap + " curInsNo=" + curInsNo + "noOfInsPaid=" + noOfInsPaid);
            if (curInsNo == 1 && notPaidThisMonth) {
                pendingInst = 0;
            } else {
                pendingInst = curInsNo - noOfInsPaid;
            }
            if (pendingInst < 0) {
                pendingInst = 0;
            }
            System.out.println("startMonth : " + startMonth + " curInsNo : " + curInsNo + " insDate : " + insDate);
            insMonth = startMonth + curInsNo;
            insDate.setMonth(insMonth);
            if (pendingInst > 0) {
                noOfInstPay = pendingInst;
            }
        }
        System.out.println("NoOfInstToPay : " + noOfInstPay+" notPaidThisMonth : " + notPaidThisMonth);
        if(noOfInstPay>0){
            notPaidThisMonth = true;
        }
        if (noOfInstPay > 0 && notPaidThisMonth) {
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
            prizedMap.put("DIVISION_NO", String.valueOf(divNo));
            prizedMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
            prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
            if (lst != null && lst.size() > 0) {
                prizedMap = (HashMap) lst.get(0);
                if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                    prized = true;
                }
                if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                    prized = true;
                }
            } else {
                prized = false;
            }
            long totDiscAmt = 0;
            long penalAmt = 0;
            double netAmt = 0;
            double insAmtPayable = 0;
            double totBonusAmt = 0;
            double bonusAmt = 0;
            String penalIntType = "";
            long penalValue = 0;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            long diffDayPending = 0;
            if (pendingInst > 0) {   //pending installment calculation starts...
                insDueAmt = (long) insAmt * pendingInst;
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (prized == true) {
                    if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                } else if (prized == false) {
                    if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                }
                List bonusAmout = new ArrayList();
                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                    //double instAmount = 0.0;
                    HashMap nextInstMaps = null;
                    for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                        nextInstMaps = new HashMap();
                        nextInstMaps.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                        nextInstMaps.put("DIVISION_NO", divNo);
                        nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                        List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                        if (listRec != null && listRec.size() > 0) {
                            nextInstMaps = (HashMap) listRec.get(0);
                        }
                        if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                            bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                        } else {
                            bonusAmout.add(CommonUtil.convertObjToDouble(0));
                        }
                    }
                }
                for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                    if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                        insAmt = 0.0;
                        double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                        if (bonusAmout != null && bonusAmout.size() > 0) {
                            instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j-1));
                        }
                        insAmt = instAmount;
                    }
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                    nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                    nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        System.out.println("instDate : " + instDate + " penalGracePeriod : " + penalGracePeriod + " penalGracePeriodType : " + penalGracePeriodType);
//                        if (penalGracePeriod > 0) {
//                            System.out.println("instDate.getDate() : " + instDate.getDate() + " instDate.getDay() : " + instDate.getDay() + " instDate.getMonth() : " + instDate.getMonth() + " instDate.getYear() : " + instDate.getYear());
//                            if (penalGracePeriodType != null && penalGracePeriodType.length() > 0 && penalGracePeriodType.equals("Days")) {
//                                instDate.setDate(instDate.getDate() + penalGracePeriod - 1);
//                                //System.out.println("instDate.getDay() : " + instDate.getDay());
//                            } else if (penalGracePeriodType != null && penalGracePeriodType.length() > 0 && penalGracePeriodType.equals("Months")) {
//                                instDate.setMonth(instDate.getMonth() + penalGracePeriod);
//                            } else if (penalGracePeriodType != null && penalGracePeriodType.length() > 0 && penalGracePeriodType.equals("Years")) {
//                                instDate.setYear(instDate.getYear() + penalGracePeriod);
//                            }
//                        }
                        if(instDay > 0){
                            instDate.setDate(instDate.getDate() + instDay - 1);
                        }
                        //   diffDayPending = DateUtil.dateDiff(instDate, currDt);
                        diffDayPending = DateUtil.dateDiff(instDate, intUptoDt);
                        System.out.println("First diffDay : " + diffDayPending + " instDate : " + instDate + " intUptoDt : " + intUptoDt);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        //System.out.println("instDate " + instDate);
                        instDate = CommonUtil.getProperDate(currDt, instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", _branchCode);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                            boolean isHoliday = Holiday.size() > 0 ? true : false;
                            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                //System.out.println("diffDayPending Holiday True : " + diffDayPending);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDayPending -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDayPending += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                //System.out.println("holidayMap : " + holidayMap);
                            } else {
                                //System.out.println("diffDay Holiday False : " + diffDayPending);
                                checkHoliday = false;
                            }
                        }
                        System.out.println("diffDayPending Final : " + diffDayPending + " penalCalcBaseOn : " + penalCalcBaseOn + " penalIntType : " + penalIntType);

                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += (diffDayPending * penalValue * insAmt) / 36500;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //    calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //    calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        }
                        penal = (long) (calc + 0.5) - penal;
                        penalList.add(String.valueOf(penal));
                        installmentMap.put("PENAL", penalList);
                        penal = calc + 0.5;
                    }
                }
                System.out.println("calc : " + calc);
                if (calc > 0) {
                    penalAmt = (long) (calc + 0.5);
                }
                System.out.println("penalAmt : " + penalAmt);
            }//pending installment calculation ends...
            //Discount calculation details Starts...
            for (int k = 0; k < noOfInstPay; k++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    Date curDate = (Date) currDt.clone();
                    int curMonth = curDate.getMonth();
                    //System.out.println("instDay : " + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    //  long diffDay = DateUtil.dateDiff(instDate, currDt);
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //  System.out.println("First diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                        if (discount != null && !discount.equals("") && discount.equals("Y")) {
                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                            if (prized == true) {//discount calculation for prized prerson...
                                String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    }
                                } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && intUptoDt.getDate() <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            } else if (prized == false) {//discount calculation non prized person...
                                String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    } else {
                                        totDiscAmt = 0;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && intUptoDt.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            }
                        } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                            totDiscAmt = 0;
                        }
                    }
                }
            }
            System.out.println("startNoForPenal" + startNoForPenal + "_noOfInstPay" + noOfInstPay + "_addNo" + addNo + "_noOfInsPaid" + noOfInsPaid + "_firstInst_No" + firstInst_No);
            //Bonus calculation details Starts...
            for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", divNo);
                System.out.println("noOfInsPaid_" + noOfInsPaid + "addNo_" + addNo + "firstInst_No_" + firstInst_No);
                nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    //  Date curDate = (Date)currDt.clone();
                    Date curDate = (Date) intUptoDt.clone();
                    int curMonth = curDate.getMonth();
                    System.out.println("instDay : " + instDay);
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT")));
                    //  long diffDay = DateUtil.dateDiff(instDate, currDt);
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        Rounding rod = new Rounding();
                        bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                    }
                    long diffDay = DateUtil.dateDiff(instDate, intUptoDt);
                    //  System.out.println("First diffDay : " + diffDay);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        if (bonusAvailabe == true) {
                            if (prized == true) {
                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && intUptoDt.getDate() <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                } else {
                                }
                            } else if (prized == false) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && intUptoDt.getDate() <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                } else {
                                }
                            }
                        }
                        //Added By Suresh
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && bonusAmt > 0) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        }
                        bonusList.add(String.valueOf(bonusAmt));
                        installmentMap.put("BONUS", bonusList);
                    }
                }
                bonusAmt = 0;
            }
            //Arbitration Amount
            double arbitrationAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List caseChargeList = sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
            if (caseChargeList != null && caseChargeList.size() > 0) {
                for (int i = 0; i < caseChargeList.size(); i++) {
                    whereMap = (HashMap) caseChargeList.get(i);
                    arbitrationAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT"));
                }
            }
            //Notice Amount
            double noticeAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List noticeList = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
            if (noticeList != null && noticeList.size() > 0) {
                for (int i = 0; i < noticeList.size(); i++) {
                    whereMap = (HashMap) noticeList.get(i);
                    noticeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT"));
                }
            }
            //System.out.println(" installmentMap" + installmentMap);
            //System.out.println(" maxLength" + noOfInstPay);
            //System.out.println(" bonusList" + bonusList);
            //System.out.println(" penalList" + penalList);
            for (int s = 0; s < noOfInstPay; s++) {
                HashMap insertMap = new HashMap();
                insertMap.put("INT_CALC_DT", CommonUtil.getProperDate(currDt, intUptoDt));
                insertMap.put("CHITTAL_NO", chittalNo);
                insertMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                insertMap.put("DIVISION_NO", String.valueOf(divNo));
                if (bonusList.size() > s) {
                    insertMap.put("BONUS", CommonUtil.convertObjToDouble(bonusList.get(s)));
                } else {
                    insertMap.put("BONUS", new Double(0));
                }
                if (penalList.size() > s) {
                    insertMap.put("PENAL", CommonUtil.convertObjToDouble(penalList.get(s)));
                } else {
                    insertMap.put("PENAL", new Double(0));
                }
                if (discountList.size() > s) {
                    insertMap.put("DISCOUNT", CommonUtil.convertObjToDouble(discountList.get(s)));
                } else {
                    insertMap.put("DISCOUNT", new Double(0));
                }
                insertMap.put("ARBITRATION_AMT", String.valueOf(arbitrationAmount));
                insertMap.put("NOTICE_AMT", String.valueOf(noticeAmount));
                noOfInsPaid += 1;
                insertMap.put("INSTALLMENT_NO", String.valueOf(noOfInsPaid));
                System.out.println(" insertMap" + insertMap);
                // sqlMap.executeUpdate("insertSalaryRecoveryMDSDetails",insertMap);
            }

            //Charges
            double chargeAmt = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            chargeAmt = getChargeAmount(whereMap, prodType);
            if (chargeAmt > 0) {
                dataMap.put("CHARGES", String.valueOf(chargeAmt));
            } else {
                dataMap.put("CHARGES", "0");
            }
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                Rounding rod = new Rounding();
                totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
            }
            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
            }
            double instAmt = insAmt * CommonUtil.convertObjToDouble(pendingInst);
            double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
            netAmt = (insAmt * noOfInstPay) + penalAmt - (totBonusAmt + totDiscAmt) + chargeAmt;
            insAmtPayable = (insAmt * noOfInstPay) - (totBonusAmt + totDiscAmt);
            String totalPayableAmount = String.valueOf(totalPayable);
            totalPayableAmount = totalPayableAmount.replaceAll(",", "");
            String penalAmount = String.valueOf(penalAmt);
            penalAmount = penalAmount.replaceAll(",", "");
            dataMap.put("DIVISION_NO", String.valueOf(divNo));
            dataMap.put("CHIT_START_DT", startDate);
            dataMap.put("INSTALLMENT_DATE", insDate);
            dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
            dataMap.put("CURR_INST", String.valueOf(curInsNo));
            dataMap.put("PENDING_INST", String.valueOf(pendingInst));
            dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
            dataMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
            dataMap.put("PRINCIPAL", String.valueOf(totalPayableAmount));//insAmtPayable)); // Principal Amount
            dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
            dataMap.put("PAID_DATE", currDt);
            dataMap.put("INTEREST", penalAmount);
            dataMap.put("INSTMT_AMT", insAmt);
            if (prized == true) {
                dataMap.put("PRIZED_MEMBER", "Y");
            } else {
                dataMap.put("PRIZED_MEMBER", "N");
            } //dataMap.put("BONUS",String.valueOf(totBonusAmt));
            dataMap.put("BONUS", new Double(totBonusAmt));
            dataMap.put("DISCOUNT", new Double(totDiscAmt));
            dataMap.put("PENAL", "0.0");   // Penal Amount//new Double(penalAmt)
            dataMap.put("TOTAL_DEMAND", new Double(netAmt));
        } else {
            System.out.println("This MDS already paid this month, please make payment nextmonth : " + chittalNo);
            dataMap = null;
        }
        System.out.println("Single Row DataMap : " + dataMap);
        return dataMap;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) { //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT"));
                    double chrgAmt = 0.0;
                    chrgAmt = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT"));
                    if (chrgAmt > 0) {
                        recoverChrgMap = new HashMap();
                        recoverChrgMap.put("INT_CALC_UPTO_DT", intUptoDt);
                        recoverChrgMap.put("ACT_NUM", actNo);
                        recoverChrgMap.put("CHARGE_TYPE", CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE")));
                        recoverChrgMap.put("AMOUNT", CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")));
                        System.out.println("recoverChrgMap : " + recoverChrgMap);
                        sqlMap.executeUpdate("insertRecoveryChargesList", recoverChrgMap);
                    }
                }
            }
            chargeList = null;
            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }

    public HashMap interestCalculationTLAD(Object accountNo, Object prod_id, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", prodType);
            hash.put("PROD_ID", prod_id);
            hash.put("TRANS_DT", interestUptoDt);
            hash.put("INITIATED_BRANCH", _branchCode);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
            System.out.println(accountNo + "," + prod_id + "," + "LIST 1" + lst);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = CommonUtil.convertObjToStr(hash.get(key));
                    System.out.println(key + " " + value);
                }
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                hash.put("TRANS_DT", intUptoDt);
                hash.put("INITIATED_BRANCH", _branchCode);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", _branchCode);
                hash.put("BRANCH_CODE", _branchCode);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                hash.put("CURR_DATE", interestUptoDt);
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                System.out.println("map before intereest" + dataMap);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
                System.out.println("hashinterestoutput" + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

//    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo, String retired) throws Exception {
//        HashMap dataMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        whereMap.put("ACT_NUM", actNum);
//        List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
//        String clear_balance = "";
//        String loanBalanacePrinciple = "";
//        boolean flag = false;
//        if (parameterList != null && parameterList.size() > 0) {
//            int firstDay = 0;
//            Date inst_dt = null;
//            Date checkDate = (Date) currDt.clone();
//            Date sanctionDt = null;
//            whereMap = (HashMap) parameterList.get(0);
//                firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
//            sanctionDt = (Date) whereMap.get("FROM_DT");
//            long diffDay;
//            diffDay = DateUtil.dateDiff((Date) sanctionDt, (Date) checkDate);
//            System.out.println(" First Day : " + firstDay + "  checkDat : " + checkDate + "  sanctionDt : " + sanctionDt + "  checkDat : " + checkDate + " diffDay : " + diffDay);
//            if (diffDay >= 0.0) {
//                if (recoveryYesNo.equals("N")) {
//                    HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
//                    System.out.println("asAndWhenMap is " + asAndWhenMap);
//                    HashMap insertPenal = new HashMap();
//                    List chargeList = null;
//                    String moratorium = "";
//                    HashMap loanInstall = new HashMap();
//                    loanInstall.put("ACT_NUM", actNum);
//                    loanInstall.put("BRANCH_CODE", _branchCode);
//                    List MoraList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                    if (MoraList.size() > 0 && MoraList.get(0) != null) {
//                        HashMap mapop = (HashMap) MoraList.get(0);
//                        if (mapop.get("MORATORIUM_GIVEN") != null) {
//                            moratorium = CommonUtil.convertObjToStr(mapop.get("MORATORIUM_GIVEN"));
//                        }
//                        System.out.println("soutMort" + moratorium);
//                    }
//                    double instalAmt = 0.0;
//                    double paidAmount = 0.0;
//                    double penal = 0.0;
//                    HashMap emiMap = new HashMap();
//                    String installtype = "";
//                    String emi_uniform = "";
//                    
//                    //Start of TL Condition
//                    
//                    if (prodType != null && prodType.equals("TL")) { //Only TL
//                        double totPrinciple = 0.0;
//                        List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
//                        if (emiList.size() > 0) {
//                            emiMap = (HashMap) emiList.get(0);
//                            installtype = emiMap.get("INSTALL_TYPE").toString();
//                            emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
//                        }
//                        HashMap allInstallmentMap = null;
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall); 
//                            allInstallmentMap = (HashMap) paidAmt.get(0);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            System.out.println("asAndWhenMap : " + asAndWhenMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE"));
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE"));
//                            System.out.println("totPrinciple11 : " + totPrinciple + " : : " + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmt != null && paidAmt.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmt.get(0);
//                                }
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT"));
//                                totPrinciple += totExcessAmt;
//                                System.out.println("in as cust comexx" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap...,mmm" + allInstallmentMap);
//                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
//                                System.out.println("instalAmtt..." + instalAmt + "toooottt" + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("totPrinciple-=instalAmt==" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                    String moret = "";
//                                    if (aList.size() > 0 && aList.get(0) != null) {
//                                        HashMap mapop = (HashMap) aList.get(0);
//                                        if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                            moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                        }
//                                    }
//                                    if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                        totPrinciple = 0;
//                                        flag = true;
//                                        break;
//                                    } else {
//                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")) - totPrinciple;
//                                        System.out.println("before Breakkkk" + totPrinciple);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
//                            allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                            System.out.println("asAndWhenMap : " + asAndWhenMap);
//                            System.out.println("allInstallmentMap..." + allInstallmentMap);
//                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE"));
//                            paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE"));
//                            System.out.println("totPrinciple22 : " + totPrinciple + " : : " + paidAmount);
//                            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
//                                paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                                if (paidAmtemi != null && paidAmtemi.size() > 0) {
//                                    allInstallmentMap = (HashMap) paidAmtemi.get(0);
//                                }
//                                System.out.println("allInstallmentMap444" + allInstallmentMap);
//                                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT"));
//                                totPrinciple += totExcessAmt;
//                                System.out.println("totPrinciple" + totPrinciple);
//                            }
//                            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
//                            for (int i = 0; i < lst.size(); i++) {
//                                allInstallmentMap = (HashMap) lst.get(i);
//                                System.out.println("allInstallmentMap34243" + allInstallmentMap);
//                                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT"));
//                                System.out.println("111222instalAmt" + instalAmt + " : : : " + totPrinciple);
//                                if (instalAmt <= totPrinciple) {
//                                    totPrinciple -= instalAmt;
//                                    System.out.println("chhhhnnn" + totPrinciple);
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                } else {
//                                    inst_dt = new Date();
//                                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
//                                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
//                                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")) - totPrinciple;
//                                    System.out.println("bbbkkk" + totPrinciple);
//                                    break;
//                                }
//                                System.out.println("totPrinciple " + totPrinciple);
//                            }
//                        }   
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            dataMap.put("INSTMT_AMT", instalAmt);
//                        } else {
//                            dataMap.put("INSTMT_AMT", (allInstallmentMap.get("TOTAL_AMT")));
//                        }
//                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                        String moret = "";
//                        if (aList.size() > 0 && aList.get(0) != null) {
//                            HashMap mapop = (HashMap) aList.get(0);
//                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                moret = CommonUtil.convertObjToStr(mapop.get("MORATORIUM_GIVEN"));
//                            }
//                        }
//                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                            dataMap.put("INSTMT_AMT", 0);
//                            dataMap.put("PRINCIPAL", String.valueOf("0"));
//                        }
//                        Date addDt = (Date) currDt.clone();
//                        Date instDt = DateUtil.addDays(inst_dt, 1);
//                        addDt.setDate(instDt.getDate());
//                        addDt.setMonth(instDt.getMonth());
//                        addDt.setYear(instDt.getYear());
//                        loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
//                        loanInstall.put("TO_DATE", interestUptoDt);
//                        System.out.println("!! getTotalamount" + loanInstall);
//                        List lst1 = null;
//                        if (inst_dt != null && (totPrinciple > 0)) {
//                            lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
//                            System.out.println("listsize" + lst1);
//                        }
//                        double principle = 0;
//                        if (lst1 != null && lst1.size() > 0) {
//                            HashMap map = (HashMap) lst1.get(0);
//                            principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT"));
//                        }
//                        totPrinciple += principle;
//                        System.out.println("totPrinciple 1111" + totPrinciple);
//                        if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT"));
//                            }
//                            totPrinciple += principle;
//                        } else {
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                //System.out.println("snnnnnn" + map);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")) + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT"));
//                                //System.out.println("sdasdsd" + principle);
//                                if (principle == 0) {
//                                    List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
//                                    if (advList.size() > 0 && advList != null) {
//                                        map = (HashMap) advList.get(0);
//                                        if (map.get("TOTAL_AMT") != null) {
//                                            principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT"));
//                                        }
//                                        totPrinciple = principle;
//                                    }
//                                } else {
//                                    totPrinciple = principle;
//                                }
//                                //System.out.println("totPrinciple66666" + totPrinciple);
//                            } else {
//                                //System.out.println("innn eeelllsss333" + totPrinciple);
//                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"));
//                                //System.out.println("innn eeelllsss333" + totPrinciple);
//                            }
//                        }
//                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
//                        insertPenal.put("INSTALL_DT", inst_dt);
//                        if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
//                            insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
//                        }
//                        if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                            double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST" ));
//                            penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                            if (facilitylst != null && facilitylst.size() > 0) {
//                                HashMap hash = (HashMap) facilitylst.get(0);
//                                if (hash.get("CLEAR_BALANCE") != null) {
//                                    clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                }
//                                if (hash.get("LOAN_BALANCE_PRINCIPAL") != null) {
//                                    loanBalanacePrinciple = hash.get("LOAN_BALANCE_PRINCIPAL").toString();
//                                }
//                                System.out.println("clear_balance =" + clear_balance + " aaa" + loanInstall.get("ACT_NUM"));
//                                hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                if (asAndWhenMap.containsKey("PREMATURE")) {
//                                    insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
//                                }
//                                if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
//                                        && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
//                                    hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
//                                } else {
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                }
//                                hash.put("TO_DATE", interestUptoDt.clone());
//                                if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
//                                } else {
//                                    facilitylst = null;
//                                    if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
//                                    }
//                                }
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    hash = (HashMap) facilitylst.get(0);
//                                    interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST"));
//                                    penal -= CommonUtil.convertObjToDouble(hash.get("PENAL"));
//                                    insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
//                                }
//                            }
//                            System.out.println("paid interest : " + interest + " paid penal : " + penal);
//                            if (interest > 0) {
//                                insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                            } else {
//                                insertPenal.put("CURR_MONTH_INT", new Double(0));
//                            }
//                            if (penal > 0) {
//                                insertPenal.put("PENAL_INT", new Double(penal));
//                            } else {
//                                insertPenal.put("PENAL_INT", new Double(0));
//                            }
//                            insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                            insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                            insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
//                            insertPenal.put("ROI", asAndWhenMap.get("ROI"));
//                            chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                        } else {
//                            List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
//                            HashMap hash = null;
//                            if (getIntDetails != null) {
//                                for (int i = 0; i < getIntDetails.size(); i++) {    
//                                    hash = (HashMap) getIntDetails.get(i);
//                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                    double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL"));
//                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL"));
//                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL"));
//                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT"));
//                                    System.out.println("pBalcc99oogytf" + pBal + " : : " + iBal + " : : " + pibal + " : : " + excess);
//                                    pBal -= excess;
//                                    //System.out.println("pBalrrr" + pBal);
//                                    if (pBal < totPrinciple) {
//                                        insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
//                                    }
//                                    //System.out.println("insertPenal5555" + insertPenal);
//                                    if (trn_mode.equals("C*")) {
//                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        break;
//                                    } else {
//                                        if (!trn_mode.equals("DP")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                        }
//                                        insertPenal.put("EBAL", hash.get("EBAL"));
//                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
//                                    }
//                                    //System.out.println("int principel detailsINSIDE LOAN" + insertPenal);
//                                }
//                            }
//                            getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
//                            hash = (HashMap) getIntDetails.get(0);
//                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                        }
//                        System.out.println("insertPenalnnnnnnshdcgasdg" + insertPenal);//abbbbb
//                    }
//                    
//                    //End of TL Condition
//                    //Start of AD Condition
//                    
//                    if (prodType != null && prodType.equals("AD")) { // Only  AD   
//                        if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
//                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
//                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"));
//                                penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                                if (facilitylst != null && facilitylst.size() > 0) {
//                                    HashMap hash = (HashMap) facilitylst.get(0);
//                                    if (hash.get("CLEAR_BALANCE") != null) {
//                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
//                                    }
//                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
//                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
//                                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
//                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
//                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
//                                    if (facilitylst != null && facilitylst.size() > 0) {
//                                        hash = (HashMap) facilitylst.get(0);
//                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST"));
//                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL"));
//                                    }
//                                }
//                                if (interest > 0) {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
//                                } else {
//                                    insertPenal.put("CURR_MONTH_INT", new Double(0));
//                                }
//                                if (penal > 0) {
//                                    insertPenal.put("PENAL_INT", new Double(penal));
//                                } else {
//                                    insertPenal.put("PENAL_INT", new Double(0));
//                                }
//                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
//                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
//                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
//                            } else {
//                                if (prodType != null && prodType.equals("AD")) {
//                                    List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
//                                    HashMap hash = null;
//                                    for (int i = 0; i < getIntDetails.size(); i++) {
//                                        hash = (HashMap) getIntDetails.get(i);
//                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
//                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL"));
//                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL"));
//                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT"));
//                                        if (trn_mode.equals("C*")) {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")) - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                            break;
//                                        } else {
//                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
//                                            insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")) - excess));
//                                            insertPenal.put("EBAL", hash.get("EBAL"));
//                                        }
//                                        //System.out.println("int principel detailsINSIDE OD" + insertPenal);
//                                    }
//                                    getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
//                                    if (getIntDetails.size() > 0) {
//                                        hash = (HashMap) getIntDetails.get(0);
//                                        insertPenal.put("PENAL_INT", hash.get("PIBAL"));
//                                    }
//                                    insertPenal.remove("PRINCIPLE_BAL");
//                                }
//                            }
//                        }
//                        double pBalance = 0.0;
//                        Date expDt = null;
//                        List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
//                        if (expDtList != null && expDtList.size() > 0) {
//                            whereMap = new HashMap();
//                            whereMap = (HashMap) expDtList.get(0);
//                            pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL"));
//                            expDt = (Date) whereMap.get("TO_DT");
//                            long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
//                            System.out.println(" Insert PBalance" + pBalance + "diffDayPending : " + diffDayPending);
//                            if (diffDayPending > 0 && pBalance > 0) {
//                                insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
//                            }
//                        }
//                    }
//                    
//                    //End of AD Condition
//                    //System.out.println(" insertPenal : " + insertPenal);
//                    //Charges
//                    double chargeAmt = 0.0;
//                    whereMap = new HashMap();
//                    whereMap.put("ACT_NUM", actNum);
//                    chargeAmt = getChargeAmount(whereMap, prodType);
//                    if (chargeAmt > 0) {
//                        dataMap.put("CHARGES", String.valueOf(chargeAmt));
//                    } else {
//                        dataMap.put("CHARGES", "0");
//                    }
//                    //System.out.println(" Single Row insertPenal : " + insertPenal);
//                    double totalDemand = 0.0;
//                    double principalAmount = 0.0;
//                    if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE"));  // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"))
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")) + chargeAmt;
//                        System.out.println("totalDemand 111=" + totalDemand);
//                    } else {
//                        principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL"));  // Principal Amount
//                        totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"))
//                                + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")) + chargeAmt;
//                        System.out.println("totalDemand 222=" + totalDemand);
//                    }
//                    if (inst_dt != null && prodType.equals("TL")) {
//                        if (retired != null && retired.equals("YES")) {
//                            dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        } else {
//                            System.out.println("intUptoDt" + intUptoDt + "inst_dt" + inst_dt);
//                            if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0) {
//                                if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                                    System.out.println("inside this");
//                                    principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                } else {
//                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                }
//                            } else {
//                                if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
//                                    System.out.println("inside out");
//                                    principalAmount = principalAmount - CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                } else {
//                                    System.out.println("zero");
//                                    dataMap.put("PRINCIPAL", "0.0");
//                                    principalAmount = 0.0;
//                                }
//                            }
//                        }
//                        // IF SALARY_RECOVERY = 'YES' ONLY
//                        if ((principalAmount == 0 && (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")))) || (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                            System.out.println(" instalAmt : " + instalAmt);
//                            HashMap balanceMap = new HashMap();
//                            double balanceLoanAmt = 0.0;
//                            double finalDemandAmt = 0.0;
//                            balanceMap.put("ACCOUNTNO", actNum);
//                            List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
//                            if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
//                                balanceMap = (HashMap) balannceAmtLst.get(0);
//                                balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL"));
//                                System.out.println("balance loan amount"+balanceLoanAmt);
//                                double checkAmt = 0.0;
//                                double totalPrincAmount = 0.0;
//                                checkAmt = balanceLoanAmt - instalAmt;
//                                if (checkAmt > 0) {
//                                    if (!(installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y"))) {
//                                        balanceMap.put("ACCT_NUM", actNum);
//                                        balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
//                                        List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
//                                        if (sumInstLst != null && sumInstLst.size() > 0) {
//                                            balanceMap = (HashMap) sumInstLst.get(0);
//                                            totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT"));
//                                            totalPrincAmount += instalAmt;
//                                            finalDemandAmt = totalPrincAmount - paidAmount;
//                                            if (balanceLoanAmt > finalDemandAmt) {
//                                                System.out.println("ashdkasd"+finalDemandAmt);
//                                                dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
//                                                principalAmount = finalDemandAmt;
//                                            } else {
//                                                System.out.println("bcmbsmcas"+balanceLoanAmt);
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        }
//                                        //bbau11
//                                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
//                                        String moret = "";
//                                        if (aList.size() > 0 && aList.get(0) != null) {
//                                            HashMap mapop = (HashMap) aList.get(0);
//                                            if (mapop.get("MORATORIUM_GIVEN") != null) {
//                                                moret = mapop.get("MORATORIUM_GIVEN").toString();
//                                            }
//                                        }
//                                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0 && (moret != null && moret.equals("Y"))) {
//                                            finalDemandAmt = 0;
//                                            principalAmount = 0;
//                                        }
//                                        System.out.println(" finalDemandAmt : " + finalDemandAmt);
//                                    }
//                                } else {
//                                    System.out.println("else part");
//                                    HashMap transMap = new HashMap();
//                                    transMap.put("ACT_NUM", actNum);
//                                    transMap.put("BRANCH_CODE", _branchCode);
//                                    List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
//                                    System.out.println("sanction list"+sanctionLst);
//                                    if (sanctionLst != null && sanctionLst.size() > 0) {
//                                        HashMap recordMap = (HashMap) sanctionLst.get(0);
//                                        int repayFreq = 0;
//                                        repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
//                                        System.out.println("repayyyyy"+repayFreq);
////                                        moratorium
//                                        if (repayFreq == 1) {
//                                            Date expiry_dt = null;
//                                            expiry_dt = (Date) recordMap.get("TO_DT");
//                                            expiry_dt = (Date) expiry_dt.clone();
//                                            //System.out.println(" expiry_dt : " + expiry_dt);
//                                            if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0) {
//                                                System.out.println("dateeeee"+DateUtil.dateDiff(intUptoDt, expiry_dt));
//                                                principalAmount = 0.0;
//                                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                                            } else {
//                                                System.out.println("n,cbs,cbsc"+balanceLoanAmt);
//                                                dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                                principalAmount = balanceLoanAmt;
//                                            }
//                                        } else {
//                                            System.out.println("sldncdbsc"+balanceLoanAmt);
//                                            dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
//                                            principalAmount = balanceLoanAmt;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (prodType.equals("AD")) {
//                        if (retired != null && retired.equals("YES")) {
//                            dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
//                        } else {
//                            if (principalAmount > 0) {
//                                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
//                            } else {
//                                dataMap.put("PRINCIPAL", "0");
//                            }
//                        }
//                    }
//                    if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
//                        double dueamount = 0;
//                        double totEmi = 0;
//                        double paidEmi = 0;
//                        double principle = 0;
//                        double interst = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
//                        System.out.println("interst" + interst);
//                        HashMap emi = new HashMap();
//                        Date upto = (Date) currDt.clone();
//                        emi.put("ACC_NUM", actNum);
//                        emi.put("UP_TO", upto);
//                        List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
//                        if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
//                            HashMap aMap = new HashMap();
//                            aMap = (HashMap) totalEmiList.get(0);
//                            totEmi = CommonUtil.convertObjToDouble(aMap.get("TOTAL_AMOUNT"));
//                            //System.out.println("TOTAL_AMOUNTv" + totEmi);
//                        } else {
//                            totEmi = 0;
//                        }
//                        HashMap paid = new HashMap();
//                        paid.put("ACT_NUM", actNum);
//                        paid.put("BRANCH_CODE", _branchCode);   
//                        List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
//                        if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
//                            paid = (HashMap) paidAmtemi.get(0);
//                            //System.out.println("asAndWhenMap : " + paid);
//                            paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE"));
//                            //System.out.println("paidEmi" + paidEmi);
//                        } else {
//                            paidEmi = 0;
//                        }
//                        System.out.println("totEmi" + totEmi + "paidEmi" + paidEmi);
//                        dueamount = totEmi - paidEmi;
//                        double paidamount = paidEmi;
//                        if (dueamount <= 0) {
//                            dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")) + interst;
//                        }
//                        //System.out.println("totalDemandsdas" + dueamount);
//                        //System.out.println("+PENAL STARTS+");
//                        List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
//                        Date penalStrats = new Date();
//                        if (scheduleList != null && scheduleList.size() > 0) {
//                            for (int k = 0; k < scheduleList.size(); k++) {
//                                HashMap eachInstall = new HashMap();
//                                eachInstall = (HashMap) scheduleList.get(k);
//                                //System.out.println("eachInstall" + eachInstall);
//                                double scheduledEmi = CommonUtil.convertObjToDouble(eachInstall.get("TOTAL_AMT"));
//                                if (paidamount >= scheduledEmi) {
//                                    System.out.println("111paidamount" + paidamount + "scheduledEmi" + scheduledEmi);
//                                    paidamount = paidamount - scheduledEmi;
//                                    //System.out.println("paidamount" + paidamount);
//                                } else {
//                                    String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
//                                    penalStrats = DateUtil.getDateMMDDYYYY(in_date);
//                                    System.out.println("getSchedules penalStrats : " + penalStrats);
//                                    break;
//                                }
//                            }
//                            emi.put("FROM_DATE", penalStrats);
//                            List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
//                            List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
//                            double interstPenal = 0;
//                            double garce = 0;
//                            List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
//                            if (graceDays != null && graceDays.size() > 0) {
//                                HashMap map = new HashMap();
//                                map = (HashMap) graceDays.get(0);
//                                if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
//                                    garce = CommonUtil.convertObjToDouble(map.get("GRACE_PERIOD_DAYS"));
//                                } else {
//                                    garce = 0;
//                                }
//                            } else {
//                                garce = 0;
//                            }
//                            long gracedy = (long) garce;
//                            int graceint = (int) garce;
//                            if (penalInterstRate != null && penalInterstRate.size() > 0) {
//                                HashMap test = new HashMap();
//                                test = (HashMap) penalInterstRate.get(0);
//                                if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
//                                    interstPenal = CommonUtil.convertObjToDouble(test.get("PENAL_INTEREST"));
//                                } else {
//                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                    double limit = CommonUtil.convertObjToDouble(((HashMap) limitList.get(0)).get("LIMIT"));
//                                    emi.put("LIMIT", limit);
//                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                    if (penalFromROI != null && penalFromROI.size() > 0) {
//                                        test = new HashMap();
//                                        test = (HashMap) penalFromROI.get(0);
//                                        //System.out.println("testttt" + test);
//                                        interstPenal = CommonUtil.convertObjToDouble(test.get("PENAL_INT"));
//                                    }
//                                }
//                            } else {
//                                List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
//                                double limit = CommonUtil.convertObjToDouble(((HashMap) limitList.get(0)).get("LIMIT"));
//                                emi.put("LIMIT", limit);
//                                List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
//                                if (penalFromROI != null && penalFromROI.size() > 0) {
//                                    HashMap test = new HashMap();
//                                    test = (HashMap) penalFromROI.get(0);
//                                    interstPenal = CommonUtil.convertObjToDouble(test.get("PENAL_INT"));
//                                }
//                            }
//                            //System.out.println("interstPenal...." + interstPenal);
////   if (getPenalData != null && getPenalData.size() > 0) {
////    for (int k = 0; k < getPenalData.size(); k++) {
////   HashMap amap = new HashMap();
////   amap = (HashMap) getPenalData.get(k);
////   String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
////   Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
////   currntDate = DateUtil.addDays(currntDate, graceint);
////   System.out.println("5555currntDate.." + currntDate);
////   // InterestCalculationTask incalc=new InterestCalculationTask(); 
////   HashMap holidayMap = new HashMap();
////   holidayMap.put("CURR_DATE", currntDate);
////   holidayMap.put("BRANCH_CODE", _branchCode);
////   currntDate = setProperDtFormat(currntDate);
////   holidayMap = new HashMap();
////   boolean checkHoliday = true;
////   String str = "any next working day";
////   System.out.println("instDate " + currntDate);
////   currntDate = setProperDtFormat(currntDate);
////   holidayMap.put("NEXT_DATE", currntDate);
////   holidayMap.put("BRANCH_CODE", _branchCode);
////   while (checkHoliday) {
////    boolean tholiday = false;
////    List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
////    List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
////    boolean isHoliday = Holiday.size() > 0 ? true : false;
////    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
////    if (isHoliday || isWeekOff) {
////     if (str.equals("any next working day")) {
////    currntDate.setDate(currntDate.getDate() + 1);
////     } else {
////    currntDate.setDate(currntDate.getDate() - 1);
////     }
////     holidayMap.put("NEXT_DATE", currntDate);
////     checkHoliday = true;
////     System.out.println(" holidayMap : " + holidayMap);
////    } else {
////     checkHoliday = false;
////    }
////   }
////   System.out.println("currntDatemmm" + currntDate);
////   System.out.println("DateUtil.dateDiff(currntDate,upto)" + DateUtil.dateDiff(currntDate, upto));
////   long difference = DateUtil.dateDiff(currntDate, upto) - 1;
////   if (difference < 0) {
////    difference = 0;
////   }
////   System.out.println("difference..." + difference);
////   double installment = CommonUtil.convertObjToDouble(amap.get("TOTAL_AMT"));
////   System.out.println("installmentsadeasdasd" + installment);
////   //penal = penal + ((installment * difference * interstPenal) / 36500);
////   System.out.println("penallcalcuuu" + penal);
////    }
////   }
//                        }
//                        principle = dueamount - interst;
//
//                        totalDemand = principle + penal + interst;
//                        totalDemand = Math.round(totalDemand);
//                        principle = Math.round(principle);
//                        penal = Math.round(penal);
//                        interst = Math.round(interst);
//                        System.out.println("Before putting datamap principle : " + principle + " penal : " + penal + " interst : " + interst + " totalDemand : " + totalDemand);
//                        dataMap.put("INTEREST", Math.round(CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"))));
//                        dataMap.put("PENAL", CommonUtil.convertObjToStr(penal));
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                        dataMap.put("LOAN_BALANCE_PRINCIPAL", loanBalanacePrinciple);
//                        dataMap.put("LAST_INT_CALC_DT", insertPenal.get("LAST_INT_CALC_DT"));
//                        System.out.println("dataMap : " + dataMap);
//                    } else {
//                        totalDemand += principalAmount;
//                        System.out.println("totalDemand : " + totalDemand);
//                        dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
//                        dataMap.put("PENAL", CommonUtil.convertObjToStr(penal));
//                        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
//                        dataMap.put("CLEAR_BALANCE", clear_balance);
//                        dataMap.put("LOAN_BALANCE_PRINCIPAL", loanBalanacePrinciple);
//                        dataMap.put("LAST_INT_CALC_DT", insertPenal.get("LAST_INT_CALC_DT"));
//                    }
//                    if (flag) {
//                        dataMap.put("PRINCIPAL", 0);
//                        flag = false;
//                    }
//                }
//            } else {
//                System.out.println(" Not Allow : " + checkDate);
//                dataMap = null;
//            }
//        }
//        System.out.println(" Single Row DataMap : " + dataMap);
//        return dataMap;
//    }
    public HashMap calcLoanPayments(String actNum, String prodId, String prodType, String recoveryYesNo, String retired) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        HashMap LoanMap = new HashMap();
        LoanMap.put("ACT_NUM", actNum);
        boolean alreadyPaidThisMonth = false;
        whereMap.put("CURR_DATE",getProperDateFormat(currDt.clone()));
        double intVal = 0, princVal =0;
        String remittedStatus = "";
        List resltList = null;
        if (prodType != null && prodType.equals("TL")) {
            System.out.println("whereMap :" + whereMap);
            HashMap hmap = new HashMap();
            hmap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
            hmap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
            hmap.put("REC_TYPE", "Direct");
            System.out.println("intUptoDt ----------------------- :" + intUptoDt);
            resltList = sqlMap.executeQueryForList("getPenalIntDetailsTL", hmap);
            if (resltList != null && resltList.size() > 0) {
                HashMap ansMap = (HashMap) resltList.get(0);
                if (ansMap != null && ansMap.containsKey("RESULT")) {
                    String ans = CommonUtil.convertObjToStr(ansMap.get("RESULT"));
                    if (ans != null && ans.length() > 0) {
                        String[] ansArr = ans.split(":");
                        String intStr = "0", princStr = "0";
                        if (ansArr.length > 5) {
                            System.out.println("ansArr[4] :" + ansArr[5]);
                            if (ansArr[5].contains("=")) {
                                String[] splArr = ansArr[5].split("=");
                                if (splArr.length > 1) {
                                    intStr = splArr[1].trim();
                                }
                            }
                        }
                        if (ansArr.length > 4) {
                            System.out.println("ansArr[4] :" + ansArr[4]);
                            if (ansArr[4].contains("=")) {
                                String[] splArr = ansArr[4].split("=");
                                if (splArr.length > 1) {
                                    princStr = splArr[1].trim();
                                }
                            }
                        }
                         if (ansArr.length > 7) {
                            System.out.println("ansArr[7] :" + ansArr[7]);
                            if (ansArr[7].contains("=")) {
                                String[] splArr = ansArr[7].split("=");
                                if (splArr.length > 1) {
                                    remittedStatus = splArr[1].trim();
                                }
                            }
                        }
                        intVal = CommonUtil.convertObjToDouble(intStr);
                        princVal = CommonUtil.convertObjToDouble(princStr);
                    }
                }
            }
        }
        boolean isChkFlag = false;
         List pList = null;
        if (prodType != null && prodType.equals("TL")) {
            if (princVal > 0) {
                isChkFlag = true;
            } else {
                if (princVal == 0) {
                     pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
                    if (pList.isEmpty()) {
                        isChkFlag = true;
                    } else {
                        isChkFlag = false;
                    }
                }
            }
        } else {
            List notProceesedList = sqlMap.executeQueryForList("getSelectNotProcessRecordcurrMonth", whereMap);
            if (notProceesedList.isEmpty()) {
                isChkFlag = true;
            }
            if (prodType != null && prodType.equals("AD")) {
                 pList = sqlMap.executeQueryForList("getNotProcessedCurrentMonthRecords", whereMap);
                if (pList.isEmpty()) {
                    isChkFlag = true;
                } else {
                    isChkFlag = false;
                }
            }
        }
        if(isChkFlag){
	    List parameterList = sqlMap.executeQueryForList("getRecoveryParameter", whereMap);
        String clear_balance = "";
        boolean flag = false;
        if (parameterList != null && parameterList.size() > 0) {
            int firstDay = 0;
            java.util.Date repayDate = null;
            java.util.Date inst_dt = null;
            java.util.Date checkDate = (java.util.Date) currDt.clone();
            java.util.Date sanctionDt = null;
            String EMIINSIMPLEINTREST = "";
            String MORATORIUMGIVEN = "";
            String INSTALLTYPE = "0";
            whereMap = (HashMap) parameterList.get(0);
            firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
            repayDate = (java.util.Date) whereMap.get("REPAYMENT_DT");
            sanctionDt = (java.util.Date) whereMap.get("FROM_DT");
            EMIINSIMPLEINTREST = CommonUtil.convertObjToStr(whereMap.get("EMI_IN_SIMPLEINTREST"));
            MORATORIUMGIVEN = CommonUtil.convertObjToStr(whereMap.get("MORATORIUM_GIVEN"));
            INSTALLTYPE = CommonUtil.convertObjToStr(whereMap.get("INSTALL_TYPE"));
            long diffDay = DateUtil.dateDiff(sanctionDt, checkDate);
            long diffRepayDay = DateUtil.dateDiff(repayDate, checkDate);
            System.out.println("kusdkjvbsdv" + diffRepayDay);
            System.out.println("### diffDay : " + diffDay);
            if (INSTALLTYPE.equals("UNIFORM_PRINCIPLE_EMI") && diffRepayDay < 0 && MORATORIUMGIVEN.equals("N")) {
                System.out.println("inside something");
                dataMap = null;
            } else {
                if ((double) diffDay >= 0.0D) {
                    System.out.println("### Allow : " + checkDate);
                    if (recoveryYesNo.equals("N")) {
                        HashMap asAndWhenMap = interestCalculationTLAD(actNum, prodId, prodType);
                        System.out.println((new StringBuilder()).append("@#@ asAndWhenMap is >>>>").append(asAndWhenMap).toString());
                        System.out.println((new StringBuilder()).append("transDetail").append(actNum).append(_branchCode).toString());
                        HashMap insertPenal = new HashMap();
                        List chargeList = null;
                        HashMap loanInstall = new HashMap();
                        loanInstall.put("ACT_NUM", actNum);
                        loanInstall.put("BRANCH_CODE", _branchCode);
                        String moratorium = "";
                        List MoraList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                        if (MoraList.size() > 0 && MoraList.get(0) != null) {
                            HashMap mapop = (HashMap) MoraList.get(0);
                            if (mapop.get("MORATORIUM_GIVEN") != null) {
                                moratorium = CommonUtil.convertObjToStr(mapop.get("MORATORIUM_GIVEN"));
                            }
                            System.out.println("soutMort" + moratorium);
                        }
                        double instalAmt = 0.0D;
                        double paidAmount = 0.0D;
                        HashMap emiMap = new HashMap();
                        String installtype = "";
                        String emi_uniform = "";
                        if (prodType != null && prodType.equals("TL")) {
                            double totPrinciple = 0.0D;
                            List emiList = sqlMap.executeQueryForList("getEmiTypeDetail", loanInstall);
                            if (emiList.size() > 0) {
                                emiMap = (HashMap) emiList.get(0);
                                installtype = emiMap.get("INSTALL_TYPE").toString();
                                emi_uniform = emiMap.get("EMI_IN_SIMPLEINTREST").toString();
                            }
                            HashMap allInstallmentMap = null;
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
                                allInstallmentMap = (HashMap) paidAmt.get(0);
                                System.out.println("allInstallmentMap..." + allInstallmentMap);
                                System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                System.out.println("totPrinciple11:" + totPrinciple + " ::" + paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmt != null && paidAmt.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmt.get(0);
                                    }
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("in as cust comexx" + totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
                                    System.out.println("allInstallmentMap...,mmm>>" + allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                    System.out.println("instalAmtt..." + instalAmt + "toooottt" + totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
                                        System.out.println("totPrinciple-=instalAmt==" + totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                                        String moret = "";
                                        double clearBal = 0.0;
                                        if (aList.size() > 0 && aList.get(0) != null) {
                                            HashMap mapop = (HashMap) aList.get(0);
                                            if (mapop.get("MORATORIUM_GIVEN") != null) {
                                                moret = mapop.get("MORATORIUM_GIVEN").toString();
                                                clearBal = CommonUtil.convertObjToDouble(mapop.get("CLEAR_BALANCE")) * -1;
                                            }
                                        }
                                        System.out.println("inst_dt=22====" + inst_dt + "currDt=22======" + currDt);
                                        System.out.println("totPrrr22rrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
                                        if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
//                                            totPrinciple = 0.0D;
//                                            flag = true;
                                            if (DateUtil.dateDiff(repayDate, currDt) < 0) {
                                                totPrinciple = 0.0D;
                                                flag = true;
                                                System.out.println("not here");
                                            } else {
                                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                                if (totPrinciple > clearBal) {
                                                    totPrinciple = clearBal;
                                                }
                                            }
                                        } else {
                                            totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                                            System.out.println("before Breakkkk" + totPrinciple);
                                        }
                                        break;
                                    }
                                    i++;
                                } while (true);
                            } else {
                                List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI1", loanInstall);
                                allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                System.out.println("!!!!asAndWhenMap:" + asAndWhenMap);
                                System.out.println("allInstallmentMap..." + allInstallmentMap);
                                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                paidAmount = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
                                System.out.println("totPrinciple22:" + totPrinciple + " ::" + paidAmount);
                                if (asAndWhenMap == null || asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N")) {
                                    paidAmtemi = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                    if (paidAmtemi != null && paidAmtemi.size() > 0) {
                                        allInstallmentMap = (HashMap) paidAmtemi.get(0);
                                    }
                                    System.out.println("allInstallmentMap444" + allInstallmentMap);
                                    double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                                    totPrinciple += totExcessAmt;
                                    System.out.println("totPrinciple" + totPrinciple);
                                }
                                List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
                                int i = 0;
                                do {
                                    if (i >= lst.size()) {
                                        break;
                                    }
                                    allInstallmentMap = (HashMap) lst.get(i);
                                    System.out.println("allInstallmentMap34243" + allInstallmentMap);
                                    instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                                    System.out.println("111222instalAmt" + instalAmt + ":::" + totPrinciple);
                                    if (instalAmt <= totPrinciple) {
                                        totPrinciple -= instalAmt;
                                        System.out.println("chhhhnnn" + totPrinciple);
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                    } else {
                                        inst_dt = new java.util.Date();
                                        String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                                        inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                                        totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("TOTAL_AMT")).doubleValue() - totPrinciple;
                                        System.out.println("bbbkkk" + totPrinciple);
                                        break;
                                    }
                                    System.out.println("totPrinciple@@@@@@@@@@@@" + totPrinciple);
                                    i++;
                                } while (true);
                            }
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Double.valueOf(instalAmt));
                            } else {
                                dataMap.put("INSTMT_AMT", allInstallmentMap.get("TOTAL_AMT"));
                            }
                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                            String moret = "";
                            if (aList.size() > 0 && aList.get(0) != null) {
                                HashMap mapop = (HashMap) aList.get(0);
                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                }
                            }
                            System.out.println("inst_dt=33====" + inst_dt + "currDt===33====" + currDt);
                            System.out.println("totPrrrrr333rrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
                            System.out.println("totPrrrrrrrrrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
                            if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
                                dataMap.put("INSTMT_AMT", Integer.valueOf(0));
                                dataMap.put("PRINCIPAL", String.valueOf("0"));
                            }
                            java.util.Date addDt = (java.util.Date) currDt.clone();
                            java.util.Date instDt = DateUtil.addDays(inst_dt, 1);
                            addDt.setDate(instDt.getDate());
                            addDt.setMonth(instDt.getMonth());
                            addDt.setYear(instDt.getYear());
                            loanInstall.put("FROM_DATE", addDt);
                            loanInstall.put("TO_DATE", interestUptoDt);
                            System.out.println("!! getTotalamount#####" + loanInstall);
                            List lst1 = null;
                            if (inst_dt != null && totPrinciple > 0.0D) {
                                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
                                System.out.println("listsize####" + lst1);
                            }
                            double principle = 0.0D;
//                            if (lst1 != null && lst1.size() > 0) {
//                                HashMap map = (HashMap) lst1.get(0);
//                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
//                            }
//                            totPrinciple += principle;
                            System.out.println("totPrinciple 1111####" + totPrinciple);
                            if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                if (lst1 != null && lst1.size() > 0) {
                                    HashMap map = (HashMap) lst1.get(0);
                                    principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
                                    System.out.println("principleprinciple" + principle);
                                }
                                totPrinciple += principle;
                            } else if (lst1 != null && lst1.size() > 0) {
                                HashMap map = (HashMap) lst1.get(0);
                                System.out.println("snnnnnn" + map);
                                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue() + CommonUtil.convertObjToDouble(map.get("INTEREST_AMOUNT")).doubleValue();
                                System.out.println("sdasdsd" + principle);
                                if (principle == 0.0D) {
                                    List advList = sqlMap.executeQueryForList("getAdvAmt", loanInstall);
                                    if (advList.size() > 0 && advList != null) {
                                        map = (HashMap) advList.get(0);
                                        if (map.get("TOTAL_AMT") != null) {
                                            principle = CommonUtil.convertObjToDouble(map.get("TOTAL_AMT")).doubleValue();
                                        }
                                        totPrinciple = principle;
                                    }
                                } else {
                                    totPrinciple = principle;
                                }
                                System.out.println("totPrinciple66666" + totPrinciple);
                            } else {
                                System.out.println("innn eeelllsss333" + totPrinciple);
                                totPrinciple -= CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                System.out.println("innn eeelllsss333" + totPrinciple);
                            }
                            System.out.println("herererererererererreerererere" + totPrinciple);
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                            insertPenal.put("INSTALL_DT", inst_dt);
                            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
                            }
                            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES") != null && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
                                    System.out.println("clear_balance =" + clear_balance + " aaa---" + CommonUtil.convertObjToStr(loanInstall.get("ACT_NUM")));
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    if (asAndWhenMap.containsKey("PREMATURE")) {
                                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                                    }
                                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT") && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                                    } else {
                                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                        hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    }                                 
                                    hash.put("TO_DATE",currDt.clone());
                                    hash.put("SALARY_RECOVERY","SALARY_RECOVERY");
                                    if (asAndWhenMap == null || !asAndWhenMap.containsKey("INSTALL_TYPE") || asAndWhenMap.get("INSTALL_TYPE") == null || !asAndWhenMap.get("INSTALL_TYPE").equals("EMI")) {
                                        facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                                        hash.remove("SALARY_RECOVERY");
                                    } else {
                                        facilitylst = null;
                                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                                        }
                                    }
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                                    }
                                }
                                System.out.println("####interest:" + interest);
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                                HashMap hash = null;
                                if (getIntDetails != null) {
                                    for (int i = 0; i < getIntDetails.size(); i++) {
                                        hash = (HashMap) getIntDetails.get(i);
                                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                        System.out.println("pBalcc99oogytf" + pBal + "::" + iBal + "::" + pibal + "::" + excess);
                                        pBal -= excess;
                                        System.out.println("pBalrrr" + pBal);
                                        if (pBal < totPrinciple) {
                                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                                        }
                                        System.out.println("insertPenal5555" + insertPenal);
                                        if (trn_mode.equals("C*")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                                            insertPenal.put("EBAL", hash.get("EBAL"));
                                            break;
                                        }
                                        if (!trn_mode.equals("DP")) {
                                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        }
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                                        System.out.println("int principel detailsINSIDE LOAN##" + insertPenal);
                                    }

                                }
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                                hash = (HashMap) getIntDetails.get(0);
                                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                            }
                            System.out.println("insertPenalnnnnnnshdcgasdg" + insertPenal);
                        }
                        if (prodType != null && prodType.equals("AD") && asAndWhenMap != null && asAndWhenMap.size() > 0) {
                            if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                                if (facilitylst != null && facilitylst.size() > 0) {
                                    HashMap hash = (HashMap) facilitylst.get(0);
                                    if (hash.get("CLEAR_BALANCE") != null) {
                                        clear_balance = hash.get("CLEAR_BALANCE").toString();
                                    }
                                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                                    hash.put("FROM_DT", DateUtil.addDays((java.util.Date) hash.get("FROM_DT"), 2));
                                    hash.put("TO_DATE", DateUtil.addDaysProperFormat(interestUptoDt, -1));
                                    facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
                                    if (facilitylst != null && facilitylst.size() > 0) {
                                        hash = (HashMap) facilitylst.get(0);
                                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                                    }
                                }
                                if (interest > 0.0D) {
                                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                                } else {
                                    insertPenal.put("CURR_MONTH_INT", new Double(0.0D));
                                }
                                if (penal > 0.0D) {
                                    insertPenal.put("PENAL_INT", new Double(penal));
                                } else {
                                    insertPenal.put("PENAL_INT", new Double(0.0D));
                                }
                                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                            } else if (prodType != null && prodType.equals("AD")) {
                                List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
                                HashMap hash = null;
                                int i = 0;
                                do {
                                    if (i >= getIntDetails.size()) {
                                        break;
                                    }
                                    hash = (HashMap) getIntDetails.get(i);
                                    String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                                    double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                                    double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                                    double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                                    if (trn_mode.equals("C*")) {
                                        insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                        insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                        insertPenal.put("EBAL", hash.get("EBAL"));
                                        break;
                                    }
                                    insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                    insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                    insertPenal.put("EBAL", hash.get("EBAL"));
                                    System.out.println("int principel detailsINSIDE OD" + insertPenal);
                                    i++;
                                } while (true);
                                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
                                if (getIntDetails.size() > 0) {
                                    hash = (HashMap) getIntDetails.get(0);
                                    insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                                }
                                insertPenal.remove("PRINCIPLE_BAL");
                            }
                        }
                        if (prodType != null && prodType.equals("AD")) {
                            double pBalance = 0.0D;
                            java.util.Date expDt = null;
                            List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
                            if (expDtList != null && expDtList.size() > 0) {
                                whereMap = new HashMap();
                                whereMap = (HashMap) expDtList.get(0);
                                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                                expDt = (java.util.Date) whereMap.get("TO_DT");
                                long diffDayPending = DateUtil.dateDiff(expDt, intUptoDt);
                                System.out.println("############# Insert PBalance" + pBalance + "######diffDayPending :" + diffDayPending);
                                if (diffDayPending > 0L && pBalance > 0.0D) {
                                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                                }
                            }
                        }
                        System.out.println("####### insertPenal : " + insertPenal);
                        double chargeAmt = 0.0D;
                        whereMap = new HashMap();
                        whereMap.put("ACT_NUM", actNum);
                        chargeAmt = getChargeAmount(whereMap, prodType);
                        if (chargeAmt > 0.0D) {
                            dataMap.put("CHARGES", String.valueOf(chargeAmt));
                        } else {
                            dataMap.put("CHARGES", "0");
                        }
                        System.out.println("####### Single Row insertPenal : " + insertPenal);
                        double totalDemand = 0.0D;
                        double principalAmount = 0.0D;
                        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 111====" + totalDemand);
                        } else {
                            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();
                            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue() + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
                            System.out.println("totalDemand 222====" + totalDemand);
                        }
                        if (inst_dt != null && prodType.equals("TL")) {
                            if (retired != null && retired.equals("YES")) {
                                System.out.println("11111111clear_balance" + clear_balance);
                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
                            } else {
                                System.out.println("intUptoDt" + intUptoDt + "inst_dt" + inst_dt);
                                System.out.println("jeffin----" + DateUtil.dateDiff(intUptoDt, inst_dt));
                                if (DateUtil.dateDiff(intUptoDt, inst_dt) <= 0L) {
                                    if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                                        principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
                                        System.out.println("222222222222222principalAmount" + principalAmount);
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    } else {
                                        System.out.println("33333333333principalAmount" + principalAmount);
                                        dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                    }
                                } //                                else if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y") && moratorium.equals("N")) {
                                //                                    principalAmount -= CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue();
                                //                                    System.out.println("44444444principalAmount" + principalAmount);
                                //                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                //                                }
                                else {
                                    System.out.println("5555555");
                                    dataMap.put("PRINCIPAL", "0");
                                    principalAmount = 0.0;
                                }
                            }
                            if (principalAmount == 0.0 && (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) || installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                                System.out.println("############ instalAmt : " + instalAmt);
                                HashMap balanceMap = new HashMap();
                                double balanceLoanAmt = 0.0D;
                                double finalDemandAmt = 0.0D;
                                System.out.println("############ actNum : " + actNum);
                                balanceMap.put("ACCOUNTNO", actNum);
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    balanceLoanAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                    System.out.println("############ instalAmt : " + instalAmt);
                                    System.out.println("############ LoanBalancePrincAmt : " + balanceLoanAmt);
                                    System.out.println("######## paidAmount" + paidAmount);
                                    double checkAmt = 0.0D;
                                    double totalPrincAmount = 0.0D;
                                    checkAmt = balanceLoanAmt - instalAmt;
                                    System.out.println("checkAmtcheckAmtcheckAmt" + checkAmt);
                                    if (checkAmt > 0.0D) {
                                        if (!installtype.equals("UNIFORM_PRINCIPLE_EMI") || !emi_uniform.equals("Y")) {
                                            balanceMap.put("ACCT_NUM", actNum);
                                            //balanceMap.put("BALANCE_AMT", String.valueOf(checkAmt));
                                            balanceMap.put("BALANCE_AMT", checkAmt);
                                            List sumInstLst = sqlMap.executeQueryForList("getPrincipalAmtGreaterThanBalAmt", balanceMap);
                                            if (sumInstLst != null && sumInstLst.size() > 0) {
                                                balanceMap = (HashMap) sumInstLst.get(0);
                                                totalPrincAmount = CommonUtil.convertObjToDouble(balanceMap.get("PRINCIPAL_AMOUNT")).doubleValue();
                                                System.out.println("totalPrincAmounttotalPrincAmount" + totalPrincAmount);
                                                totalPrincAmount += instalAmt;
                                                System.out.println("here totalPrincAmount" + totalPrincAmount);
                                                finalDemandAmt = totalPrincAmount - paidAmount;
                                                if (balanceLoanAmt > finalDemandAmt) {
                                                    System.out.println("66666666finalDemandAmt" + finalDemandAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(finalDemandAmt));
                                                    principalAmount = finalDemandAmt;
                                                } else {
                                                    System.out.println("7777777777balanceLoanAmt" + balanceLoanAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            }
                                            List aList = ServerUtil.executeQuery("getMorotorium", loanInstall);
                                            String moret = "";
                                            if (aList.size() > 0 && aList.get(0) != null) {
                                                HashMap mapop = (HashMap) aList.get(0);
                                                if (mapop.get("MORATORIUM_GIVEN") != null) {
                                                    moret = mapop.get("MORATORIUM_GIVEN").toString();
                                                }
                                            }
                                            System.out.println("inst_dt==11===" + inst_dt + "currDt=11======" + currDt);
                                            System.out.println("totPrrrrrrr11rrrrrrrrrr=" + DateUtil.dateDiff(inst_dt, currDt) + "ggggg=" + moret);
                                            if (DateUtil.dateDiff(inst_dt, currDt) <= 0L && moret != null && moret.equals("Y")) {
                                                finalDemandAmt = 0.0D;
                                                principalAmount = 0.0D;
                                            }
                                            System.out.println("############ finalDemandAmt : " + finalDemandAmt);
                                        }
                                    } else {
                                        HashMap transMap = new HashMap();
                                        transMap.put("ACT_NUM", actNum);
                                        transMap.put("BRANCH_CODE", _branchCode);
                                        List sanctionLst = sqlMap.executeQueryForList("getNoOfDaysinLoan", transMap);
                                        if (sanctionLst != null && sanctionLst.size() > 0) {
                                            HashMap recordMap = (HashMap) sanctionLst.get(0);
                                            int repayFreq = 0;
                                            repayFreq = CommonUtil.convertObjToInt(recordMap.get("REPAYMENT_FREQUENCY"));
                                            if (repayFreq == 1) {
                                                java.util.Date expiry_dt = null;
                                                expiry_dt = (java.util.Date) recordMap.get("TO_DT");
                                                expiry_dt = (java.util.Date) expiry_dt.clone();
                                                System.out.println("########## expiry_dt : " + expiry_dt);
                                                if (DateUtil.dateDiff(intUptoDt, expiry_dt) >= 0L) {
                                                    principalAmount = 0.0D;
                                                    System.out.println("888888principalAmount" + principalAmount);
                                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                                } else {
                                                    System.out.println("999999balanceLoanAmt" + balanceLoanAmt);
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                }
                                            } else {
                                                if (repayDate.compareTo((Date) currDt.clone()) < 1) {
                                                    dataMap.put("PRINCIPAL", String.valueOf(balanceLoanAmt));
                                                    principalAmount = balanceLoanAmt;
                                                } else {
                                                    dataMap.put("PRINCIPAL", String.valueOf("0"));
                                                    principalAmount = 0.0;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (prodType.equals("AD")) {
                            if (retired != null && retired.equals("YES")) {
                                System.out.println("111222333444clear_balance" + clear_balance);
                                dataMap.put("PRINCIPAL", String.valueOf(Math.abs(Integer.parseInt(clear_balance))));
                            } else {
                                if (principalAmount > 0.0D) {
                                    System.out.println("222333444555principalAmount" + principalAmount);
                                    dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
                                } else {
                                    System.out.println("333444555666");
                                    dataMap.put("PRINCIPAL", "0");
                                }
                            }
                        }
                        if (installtype.equals("UNIFORM_PRINCIPLE_EMI") && emi_uniform.equals("Y")) {
                            double dueamount = 0.0D;
                            double penal = 0.0D;
                            double totEmi = 0.0D;
                            double paidEmi = 0.0D;
                            double principle = 0.0D;
                            double interst = Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString());
                            System.out.println("interst" + interst);
                            HashMap emi = new HashMap();
                            java.util.Date upto = (java.util.Date) currDt.clone();
                            emi.put("ACC_NUM", actNum);
                            emi.put("UP_TO", upto);
                            List totalEmiList = sqlMap.executeQueryForList("TotalEmi", emi);
                            if (totalEmiList != null && totalEmiList.size() > 0 && ((HashMap) totalEmiList.get(0)).get("TOTAL_AMOUNT") != null) {
                                HashMap aMap = new HashMap();
                                aMap = (HashMap) totalEmiList.get(0);
                                totEmi = Double.parseDouble(aMap.get("TOTAL_AMOUNT").toString());
                                System.out.println("TOTAL_AMOUNTv" + totEmi);
                            } else {
                                totEmi = 0.0D;
                            }
                            HashMap paid = new HashMap();
                            paid.put("ACT_NUM", actNum);
                            paid.put("BRANCH_CODE", _branchCode);
                            List paidAmtemi = sqlMap.executeQueryForList("getPaidPrincipleEMI", loanInstall);
                            if (paidAmtemi != null && paidAmtemi.size() > 0 && ((HashMap) paidAmtemi.get(0)).get("PRINCIPLE") != null) {
                                paid = (HashMap) paidAmtemi.get(0);
                                System.out.println("!!!!asAndWhenMap:" + paid);
                                paidEmi = CommonUtil.convertObjToDouble(paid.get("PRINCIPLE")).doubleValue();
                                System.out.println("paidEmi" + paidEmi);
                            } else {
                                paidEmi = 0.0D;
                            }
                            System.out.println("totEmi" + totEmi + "paidEmi" + paidEmi);
                            dueamount = totEmi - paidEmi;
                            double paidamount = paidEmi;
                            if (dueamount <= 0.0D) {
                                dueamount = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL")).doubleValue() + interst;
                            }
                            System.out.println("totalDemandsdas" + dueamount);
                            System.out.println("+==========PENAL STARTS==============================+");
                            List scheduleList = sqlMap.executeQueryForList("getSchedules", emi);
                            java.util.Date penalStrats = new java.util.Date();
                            if (scheduleList != null && scheduleList.size() > 0) {
                                int k = 0;
                                do {
                                    if (k >= scheduleList.size()) {
                                        break;
                                    }
                                    HashMap eachInstall = new HashMap();
                                    eachInstall = (HashMap) scheduleList.get(k);
                                    System.out.println("eachInstall" + eachInstall);
                                    double scheduledEmi = Double.parseDouble(eachInstall.get("TOTAL_AMT").toString());
                                    if (paidamount >= scheduledEmi) {
                                        System.out.println("111paidamount" + paidamount + "scheduledEmi" + scheduledEmi);
                                        paidamount -= scheduledEmi;
                                        System.out.println("paidamount" + paidamount);
                                    } else {
                                        String in_date = CommonUtil.convertObjToStr(eachInstall.get("INSTALLMENT_DT"));
                                        penalStrats = DateUtil.getDateMMDDYYYY(in_date);
                                        System.out.println("penalStrats....." + penalStrats);
                                        break;
                                    }
                                    k++;
                                } while (true);
                                emi.put("FROM_DATE", penalStrats);
                                List getPenalData = sqlMap.executeQueryForList("getPenalData", emi);
                                List penalInterstRate = sqlMap.executeQueryForList("getPenalIntestRatefromMaintenance", emi);
                                double interstPenal = 0.0D;
                                double garce = 0.0D;
                                List graceDays = sqlMap.executeQueryForList("getGracePeriodDays", emi);
                                if (graceDays != null && graceDays.size() > 0) {
                                    HashMap map = new HashMap();
                                    map = (HashMap) graceDays.get(0);
                                    if (map != null && map.containsKey("GRACE_PERIOD_DAYS") && map.get("GRACE_PERIOD_DAYS") != null) {
                                        garce = Double.parseDouble(map.get("GRACE_PERIOD_DAYS").toString());
                                    } else {
                                        garce = 0.0D;
                                    }
                                } else {
                                    garce = 0.0D;
                                }
                                long gracedy = (long) garce;
                                int graceint = (int) garce;
                                if (penalInterstRate != null && penalInterstRate.size() > 0) {
                                    HashMap test = new HashMap();
                                    test = (HashMap) penalInterstRate.get(0);
                                    if (test != null && test.containsKey("PENAL_INTEREST") && test.get("PENAL_INTEREST") != null) {
                                        interstPenal = Double.parseDouble(test.get("PENAL_INTEREST").toString());
                                    } else {
                                        List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                        double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                        emi.put("LIMIT", Double.valueOf(limit));
                                        List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                        if (penalFromROI != null && penalFromROI.size() > 0) {
                                            test = new HashMap();
                                            test = (HashMap) penalFromROI.get(0);
                                            System.out.println("testttt" + test);
                                            interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                        }
                                    }
                                } else {
                                    List limitList = sqlMap.executeQueryForList("getLimitFromLoanSanc", emi);
                                    double limit = Double.parseDouble(((HashMap) limitList.get(0)).get("LIMIT").toString());
                                    emi.put("LIMIT", Double.valueOf(limit));
                                    List penalFromROI = sqlMap.executeQueryForList("getPenalIntestRatefromROI", emi);
                                    if (penalFromROI != null && penalFromROI.size() > 0) {
                                        HashMap test = new HashMap();
                                        test = (HashMap) penalFromROI.get(0);
                                        interstPenal = Double.parseDouble(test.get("PENAL_INT").toString());
                                    }
                                }
                                System.out.println("interstPenal...." + interstPenal);
                                if (getPenalData != null && getPenalData.size() > 0) {
                                    for (k = 0; k < getPenalData.size(); k++) {
                                        HashMap amap = new HashMap();
                                        amap = (HashMap) getPenalData.get(k);
                                        String in_date = CommonUtil.convertObjToStr(amap.get("INSTALLMENT_DT"));
                                        java.util.Date currntDate = DateUtil.getDateMMDDYYYY(in_date);
                                        currntDate = DateUtil.addDays(currntDate, graceint);
                                        System.out.println("5555currntDate.." + currntDate);
                                        HashMap holidayMap = new HashMap();
                                        holidayMap.put("CURR_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        holidayMap = new HashMap();
                                        boolean checkHoliday = true;
                                        String str = "any next working day";
                                        System.out.println("instDate   " + currntDate);
                                        currntDate = CommonUtil.getProperDate(currDt, currntDate);
                                        holidayMap.put("NEXT_DATE", currntDate);
                                        holidayMap.put("BRANCH_CODE", _branchCode);
                                        while (checkHoliday) {
                                            boolean tholiday = false;
                                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                                            boolean isHoliday = Holiday.size() > 0;
                                            boolean isWeekOff = weeklyOf.size() > 0;
                                            if (isHoliday || isWeekOff) {
                                                if (str.equals("any next working day")) {
                                                    currntDate.setDate(currntDate.getDate() + 1);
                                                } else {
                                                    currntDate.setDate(currntDate.getDate() - 1);
                                                }
                                                holidayMap.put("NEXT_DATE", currntDate);
                                                checkHoliday = true;
                                                System.out.println("#### holidayMap : " + holidayMap);
                                            } else {
                                                checkHoliday = false;
                                            }
                                        }
                                        System.out.println("currntDatemmm" + currntDate);
                                        System.out.println("DateUtil.dateDiff(currntDate,upto)" + DateUtil.dateDiff(currntDate, upto));
                                        long difference = DateUtil.dateDiff(currntDate, upto) - 1L;
                                        if (difference < 0L) {
                                            difference = 0L;
                                        }
                                        System.out.println("difference..." + difference);
                                        double installment = Double.parseDouble(amap.get("TOTAL_AMT").toString());
                                        System.out.println("installmentsadeasdasd" + installment);
                                        penal += (installment * (double) difference * interstPenal) / 36500D;
                                        System.out.println("penallcalcuuu" + penal);
                                    }

                                }
                            }
                            principle = dueamount - interst;
                            System.out.println("mmmprinciple" + principle + "::penal" + penal + "::interst" + interst);
                            totalDemand = principle + penal + interst;
                            totalDemand = Math.round(totalDemand);
                            principle = Math.round(principle);
                            penal = rd.getNearestHigher(penal, 1);
                            interst = Math.round(interst);
                            System.out.println("tttttoooo" + totalDemand);
                            dataMap.put("INTEREST", Long.valueOf(Math.round(Double.parseDouble(insertPenal.get("CURR_MONTH_INT").toString()))));
                            dataMap.put("PENAL", Double.valueOf(penal));
//                            if (totalDemand > 0) {
//                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
//                            } else {
//                                dataMap.put("TOTAL_DEMAND", "0");
//                                dataMap = null;
//                                return dataMap;
//                            }
//                            dataMap.put("CLEAR_BALANCE", clear_balance);
//                            System.out.println("mmmmmmiiinnneee"+dataMap);
                            if (principle > 0) {
                                dataMap.put("PRINCIPAL", principle);
                            }
                            HashMap detailedMap = new HashMap();
                            detailedMap.put("ACCT_NUM", actNum);
                            detailedMap.put("ASONDT", (Date) intUptoDt.clone());
                            detailedMap.put("INSTALL_TYPE", installtype);
                            detailedMap.put("EMI_IN_SIMPLE_INTEREST", emi_uniform);
                            List advanceEmiList = sqlMap.executeQueryForList("getAdvAmtEmi", detailedMap);
                            if (advanceEmiList != null && advanceEmiList.size() > 0) {
                                HashMap advMap = (HashMap) advanceEmiList.get(0);
                                if (advMap != null && advMap.size() > 0 && advMap.containsKey("BALANCE")) {
                                    double balance = CommonUtil.convertObjToDouble(advMap.get("BALANCE"));
                                    System.out.println("balance" + balance);
                                    if (balance <= 0) {
                                        double interest = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT"));
                                        double insAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                                        double princi = insAmt - interest;
                                        double princip = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));

                                        System.out.println("interest" + interest + "insAmt" + insAmt + "princi" + princi + "princip" + princip);

                                        if (princi > 0) {
                                            if (princip > 0) {
                                                totalDemand -= princip;
                                                System.out.println("jefff");
                                            }
                                            totalDemand += princi;
                                            dataMap.put("PRINCIPAL", princi);
                                            System.out.println("prasnth" + totalDemand);
                                        }
                                    }
                                }
                            }

                            double insttAmt = CommonUtil.convertObjToDouble(dataMap.get("INSTMT_AMT"));
                            double tempPrin = CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL"));
                            double tempInt = CommonUtil.convertObjToDouble(dataMap.get("INTEREST"));

                            System.out.println("insttAmt" + insttAmt + "tempPrin" + tempPrin + "tempInt" + tempInt);

                            double temmmpPrinc = insttAmt - tempInt;
                            if (tempPrin < temmmpPrinc) {
                                totalDemand -= tempPrin;
                                totalDemand += temmmpPrinc;
                                dataMap.put("PRINCIPAL", temmmpPrinc);
                                System.out.println("prasnth" + totalDemand);
                            }else{
                                HashMap balanceMap = new HashMap(); 
                                balanceMap.put("ACCOUNTNO",actNum);
                                double outstandingAmt = 0;
                                List balannceAmtLst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", balanceMap);
                                if (balannceAmtLst != null && balannceAmtLst.size() > 0) {
                                    balanceMap = (HashMap) balannceAmtLst.get(0);
                                    outstandingAmt = CommonUtil.convertObjToDouble(balanceMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                                }
                                if(insttAmt<tempInt){
                                    temmmpPrinc = (insttAmt*1) - tempInt;
                                    totalDemand = insttAmt*1+penal;
                                    if(temmmpPrinc<0){
                                        temmmpPrinc = (insttAmt*2) - tempInt;
                                        totalDemand = (insttAmt*2) +penal;                                        
                                    }
                                    dataMap.put("PRINCIPAL", temmmpPrinc);
                                    System.out.println("condition temmmpPrinc : "+temmmpPrinc);
                                }else{
                                    System.out.println("outstandingAmt : "+outstandingAmt);
                                    if(outstandingAmt<insttAmt){
                                        temmmpPrinc = insttAmt - tempInt;
                                        totalDemand = insttAmt+penal;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                        System.out.println("outstandingAmt condition temmmpPrinc : "+temmmpPrinc);
                                    }else{
                                        temmmpPrinc = insttAmt - tempInt;
                                        totalDemand = insttAmt+penal;
                                        dataMap.put("PRINCIPAL", temmmpPrinc);
                                        System.out.println("else outstandingAmt temmmpPrinc : "+temmmpPrinc);
                                    }
                                }
                            }

                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                if( prodType!=null && !prodType.equals("TL")){
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                                }
                            }
                            dataMap.put("CLEAR_BALANCE", clear_balance);
                        } else {
                            totalDemand += principalAmount;
                            System.out.println("totalDemand 333====" + totalDemand);
                            dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
                            dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
                            if (totalDemand > 0) {
                                dataMap.put("TOTAL_DEMAND", Math.round(CommonUtil.convertObjToDouble(totalDemand)));
                            } else {
                                if( prodType!=null && !prodType.equals("TL")){
                                dataMap.put("TOTAL_DEMAND", "0");
                                dataMap = null;
                                return dataMap;
                                }
                            }
                            dataMap.put("CLEAR_BALANCE", clear_balance);
                        }
                        if (flag) {
                            System.out.println("444555666777");
                            dataMap.put("PRINCIPAL", Integer.valueOf(0));
                            flag = false;
                        }
                    }
                    if (prodType != null && !prodType.equals("") && prodType.equals("AD")) {
                        HashMap intMap = new HashMap();
                        intMap.put("AS_ON_DATE", currDt.clone());
                        intMap.put("ACCT_NUM", CommonUtil.convertObjToStr(actNum));
                        List penalADList = sqlMap.executeQueryForList("TransAll.getPenalAmountForAD", intMap);
                        if (penalADList != null && penalADList.size() > 0) {
                            HashMap intDetailMap = (HashMap) penalADList.get(0);
                            System.out.println("intDetailMap" + intDetailMap);
                            if (intDetailMap != null && intDetailMap.size() > 0 && intDetailMap.containsKey("PENAL")) {
                                double penal = CommonUtil.convertObjToDouble(intDetailMap.get("PENAL"));
                                if (penal > 0) {
                                    dataMap.put("PENAL", penal);
                                } else {
                                    dataMap.put("PENAL", "0");
                                }
                            }
                        }
                    }
                    
                    if (dataMap != null && prodType != null && prodType.equals("TL")) {
                       if (resltList != null && resltList.size() > 0) {
                           System.out.println("dataMap ***SSSS******* ZZZ :" + dataMap);
                           double penalVal = CommonUtil.convertObjToDouble(dataMap.get("PENAL"));
                           double demandVal = 0;
                           if (princVal >= 0) {
                               demandVal = princVal + penalVal + intVal;
                           } else {
                               demandVal = penalVal + intVal;
                           }
                           System.out.println("totalDemand : " + demandVal);
                           dataMap.put("INTEREST", intVal);
                           if (princVal >= 0) {
                               dataMap.put("PRINCIPAL", princVal);
                           }
                           if (demandVal > 0) {
                               dataMap.put("TOTAL_DEMAND", demandVal);
                           } else {
                               dataMap.put("TOTAL_DEMAND", "0");
                               dataMap = null;
                               return dataMap;
                           }
                           System.out.println("dataMap ****ZZZZZZZZZZZZZ****** aaa :" + dataMap);
                       }
                    }
                    if(prodType != null && prodType.equals("TL") && remittedStatus.equals("PAID")){
                        dataMap = null;
                    }
                    if (prodType != null && prodType.equals("AD")) {
                        HashMap hamap = new HashMap();
                        hamap.put("ACT_NUM", CommonUtil.convertObjToStr(actNum));
                        hamap.put("TRANS_DT", getProperDateFormat(intUptoDt.clone()));
                        System.out.println("intUptoDt ---ZQ-------------------- :" + intUptoDt);
                        List reList = sqlMap.executeQueryForList("getRemittedStatus", hamap);
                        if (reList != null && reList.size() > 0) {
                            HashMap newMap = (HashMap) reList.get(0);
                            String stat = CommonUtil.convertObjToStr(newMap.get("STATUS"));
                            if (stat != null && stat.equals("ADVANCE")) {
                                List rList = sqlMap.executeQueryForList("getRemittedRecordCount", hamap);
                                if (rList != null && rList.size() > 0) {
                                    HashMap pMap = (HashMap) rList.get(0);
                                    int countN = CommonUtil.convertObjToInt(pMap.get("SAT_NUM"));
                                    if (countN > 0) {
                                        dataMap = null;
                                    }
                                }
                            }
                        }

                    }
                    if(alreadyPaidThisMonth){
                        dataMap = new HashMap();
                    }
                } else {
                    System.out.println("### Not Allow : " + checkDate);
                    dataMap = null;
                }
            }
        }
        }
        System.out.println("####### Single Row DataMap : " + dataMap);
        System.out.println("dataMapdataMapdataMapdataMap" + dataMap);
        return dataMap;
    }

    public HashMap calcTermDeposits(String actNum, String prodId, String prodType, String retired) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        String behavesLike = "";
        long actualDelay = 0;
        String acNoSubNo = "";
        //System.out.println(" actNum" + actNum);
        acNoSubNo = actNum;
        if (actNum.indexOf("_") != -1) {
            actNum = actNum.substring(0, actNum.indexOf("_"));
        }
        whereMap.put("ACT_NUM", actNum);
        List behavesLikeList = sqlMap.executeQueryForList("getBehavesLikeForDepositNo", whereMap);
        if (behavesLikeList != null && behavesLikeList.size() > 0) {
            whereMap = (HashMap) behavesLikeList.get(0);
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (!behavesLike.equals("") && behavesLike != null && behavesLike.equals("RECURRING")) {
                System.out.println(" behavesLike" + behavesLike + " actNum" + actNum);
                HashMap accountMap = new HashMap();
                HashMap lastMap = new HashMap();
                HashMap rdDataMap = new HashMap();
                rdDataMap.put("DEPOSIT_NO", actNum);
                accountMap.put("DEPOSIT_NO", actNum);
                accountMap.put("BRANCH_ID", _branchCode);
                HashMap depositMaxMap = new HashMap();
                depositMaxMap.put("DEPOSIT_NO", acNoSubNo);
                System.out.println("depositMaxMap : " + depositMaxMap);
                List depositMaxList = sqlMap.executeQueryForList("getSelectRDDepositMaxtransDt", depositMaxMap);
                if (depositMaxList != null && depositMaxList.size() > 0) {
                    depositMaxMap = (HashMap) depositMaxList.get(0);
                    Date date = (Date) currDt.clone();
                    Date maxDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("TRANS_DT")));
                    Date transDueDate = getProperDateFormat(CommonUtil.convertObjToStr(depositMaxMap.get("DUE_DATE")));
                    System.out.println("curr date : " + date + " last transaction date : " + maxDate);
                    int transDueMonth = (int)transDueDate.getMonth()+1;
                    int currentMonth = (int)date.getMonth()+1;
                    int maxTxnMonth = (int)maxDate.getMonth()+1;
                    int currentYear = (int)date.getYear()+1900;
                    int maxTxnYear = (int)maxDate.getYear()+1900;
                    System.out.println("curr date month : " + currentMonth + " last transaction maxDate : " + maxTxnMonth +"curr year : " + currentYear + " last transaction year : " + maxTxnYear);
//                    if ((maxTxnMonth != currentMonth) && (maxTxnYear == currentYear)) {
                        List lst = sqlMap.executeQueryForList("getProductIdForDeposits", accountMap);
                        if (lst != null && lst.size() > 0) {
                            accountMap = (HashMap) lst.get(0);
                            Date currDate = (Date) intUptoDt.clone();
                            //  Date currDate = (Date) currDt.clone();
                            String insBeyondMaturityDat = "";
                            List recurringLst = sqlMap.executeQueryForList("getRecurringDepositDetails", accountMap);
                            if (recurringLst != null && recurringLst.size() > 0) {
                                HashMap recurringMap = new HashMap();
                                recurringMap = (HashMap) recurringLst.get(0);
                                insBeyondMaturityDat = CommonUtil.convertObjToStr(recurringMap.get("INST_BEYOND_MATURITY_DATE"));
                            }
                            long totalDelay = 0;
                            double delayAmt = 0.0;
                            double tot_Inst_paid = 0.0;
                            double depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                            dataMap.put("INSTMT_AMT", depAmt);
                            Date matDt = new Date();
                            matDt.setTime(currDate.getTime());
                            Date depDt = new Date();
                            depDt.setTime(currDate.getTime());
                            //System.out.println("&&&&&&&&&&&& CurrentDate11111" + currDate);
                            lastMap.put("DEPOSIT_NO", actNum);
                            lst = sqlMap.executeQueryForList("getInterestDeptIntTable", lastMap);
                            if (lst != null && lst.size() > 0) {
                                lastMap = (HashMap) lst.get(0);
                                System.out.println(" lastMap>" + lastMap);
                                rdDataMap.put("DEPOSIT_AMT", lastMap.get("DEPOSIT_AMT"));
                                rdDataMap.put("MATURITY_DT", lastMap.get("MATURITY_DT"));
                                tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID"));
                                HashMap prematureDateMap = new HashMap();
                                double monthPeriod = 0.0;
                                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("MATURITY_DT")));
                                //System.out.println(" MATURITY_DT" + matDate);
                                //System.out.println(" CurrentDate" + currDate);
                                if (matDate.getDate() > 0) {
                                    matDt.setDate(matDate.getDate());
                                    matDt.setMonth(matDate.getMonth());
                                    matDt.setYear(matDate.getYear());
                                }
                                Date depDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lastMap.get("DEPOSIT_DT")));
                                if (depDate.getDate() > 0) {
                                    depDt.setDate(depDate.getDate());
                                    depDt.setMonth(depDate.getMonth());
                                    depDt.setYear(depDate.getYear());
                                }
                                //System.out.println(" MATURITY_DT" + matDate);
                                //System.out.println(" CurrentDate" + currDate);
                                if (DateUtil.dateDiff((Date) matDt, (Date) currDate) > 0) {
                                    matDt = CommonUtil.getProperDate(currDt, matDt);
                                    prematureDateMap.put("TO_DATE", matDt);
                                    prematureDateMap.put("FROM_DATE", lastMap.get("DEPOSIT_DT"));
                                    lst = sqlMap.executeQueryForList("periodRunMap", prematureDateMap);
                                    if (lst != null && lst.size() > 0) {
                                        prematureDateMap = (HashMap) lst.get(0);
                                        System.out.println(" prematureDateMap" + prematureDateMap);
                                        monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS"));
                                        actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
                                        System.out.println("Matured Deposit actualDelay : " + actualDelay + " monthPeriod : " + monthPeriod + " tot_Inst_paid : " + tot_Inst_paid);
                                    }
                                    lst = null;
                                } else {
//                                int dep = depDt.getMonth() + 1;
//                                int curr = currDate.getMonth() + 1;
//                                int depYear = depDt.getYear() + 1900;
//                                int currYear = currDate.getYear() + 1900;
//                                if (depYear == currYear) {
//                                    monthPeriod = curr - dep;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                } else {
//                                    int diffYear = currYear - depYear;
//                                    monthPeriod = (diffYear * 12 - dep) + curr;
//                                    actualDelay = (long) monthPeriod - (long) tot_Inst_paid;
//                                }
//                                System.out.println(" else actualDelay" + actualDelay + " monthPeriod" + monthPeriod + " tot_Inst_paid" + tot_Inst_paid);
                                    System.out.println("currDate" + currDate);
                                    java.util.GregorianCalendar gactualCurrDateCalendar = new java.util.GregorianCalendar();
                                    gactualCurrDateCalendar.setGregorianChange(currDate);
                                    gactualCurrDateCalendar.setTime(currDate);
                                    int curDay = gactualCurrDateCalendar.get(gactualCurrDateCalendar.DAY_OF_MONTH);
                                    System.out.println("curDaycurDay" + curDay);
                                    List recoveryList = sqlMap.executeQueryForList("getRecoveryParameters", whereMap);
                                    int gracePeriod = 0;
                                    int firstDay = 0;
                                    int depositDay = (int) depDate.getDate();
                                    if (recoveryList != null && recoveryList.size() > 0) {
                                        HashMap recoveryDetailsMap = (HashMap) recoveryList.get(0);
                                        if (recoveryDetailsMap != null && recoveryDetailsMap.size() > 0 && recoveryDetailsMap.containsKey("GRACE_PERIOD")) {
                                            gracePeriod = CommonUtil.convertObjToInt(recoveryDetailsMap.get("GRACE_PERIOD"));
                                            firstDay = CommonUtil.convertObjToInt(recoveryDetailsMap.get("FIRST_DAY"));
                                        }
                                    }
                                    System.out.println("curDay : "+curDay+" gracePeriod : "+gracePeriod+" depositDay : "+depositDay);
                                    if (curDay > gracePeriod && transDueMonth > currentMonth && transDueMonth != currentMonth) {
                                        gactualCurrDateCalendar.add(gactualCurrDateCalendar.MONTH, 1);
                                        gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                    } else {
                                        gactualCurrDateCalendar.set(gactualCurrDateCalendar.DAY_OF_MONTH, gracePeriod);
                                    }
                                    Date tempcurrDate = new Date();
                                    tempcurrDate = CommonUtil.getProperDate(currDt, gactualCurrDateCalendar.getTime());
                                    Date dueDate = new Date();
                                    HashMap detailedMap = new HashMap();
                                    detailedMap.put("DEPOSIT_NO", actNum + "_1");
                                    List newList = sqlMap.executeQueryForList("getBalnceDepositDetails", detailedMap);
                                    if (newList != null && newList.size() > 0) {
                                        for (int i = 0; i < newList.size(); i++) {
                                            HashMap newMap = (HashMap) newList.get(i);
                                            if (newMap != null && newMap.size() > 0) {
                                                dueDate = getProperDateFormat(CommonUtil.convertObjToStr(newMap.get("DUE_DATE")));
                                                System.out.println("tempcurrDate" + tempcurrDate + "dueDate" + dueDate);
                                                if (dueDate.compareTo(tempcurrDate) == 0 || dueDate.compareTo(tempcurrDate) < 0) {
                                                    actualDelay = actualDelay + 1;
                                                }
                                            }
                                        }
                                    }
                                    System.out.println("Live Deposit actualDelay : " + actualDelay + " monthPeriod : " + monthPeriod + " tot_Inst_paid : " + tot_Inst_paid);
                                }
                            }
                            lst = null;
                            if ((DateUtil.dateDiff((Date) matDt, (Date) currDt) > 0) && !insBeyondMaturityDat.equals("") && insBeyondMaturityDat.equals("N")) {
                                dataMap = new HashMap();
                                return dataMap;
                            }
                            String penalCalcType = "DAYS";//INSTALMENT
                            HashMap dailyMap = new HashMap();
                            dailyMap.put("ROI_GROUP_ID", prodId);
                            List list = (List) sqlMap.executeQueryForList("getSelectDepositsCommision", dailyMap);
                            if (list != null && list.size() > 0) {
                                System.out.println("list list list" + list);
                                InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO) list.get(0);
                                if (objInterestMaintenanceRateTO != null) {
                                    penalCalcType = CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType());
                                }
                            }
                            if (DateUtil.dateDiff((Date) matDt, (Date) currDate) < 0 || insBeyondMaturityDat.equals("Y")) {
                                if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Installments")) {
                                    depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                    dataMap.put("INSTMT_AMT", depAmt);
                                    double chargeAmt = depAmt / 100;//100
                                    HashMap delayMap = new HashMap();
                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                    lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                    if (lst != null && lst.size() > 0) {
                                        delayMap = (HashMap) lst.get(0);
                                        System.out.println("delayMapdelayMapdelayMap" + delayMap);
                                        delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                        delayAmt = delayAmt * chargeAmt;
                                        System.out.println("recurring delayAmt : " + delayAmt);
                                    }
                                    lst = null;
                                    HashMap depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                    List lstRec = sqlMap.executeQueryForList("getDepTransactionRecurring", depRecMap);
                                    if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                        for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                            depRecMap = (HashMap) lstRec.get(i);
                                            Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            int transMonth = transDt.getMonth() + 1;
                                            int dueMonth = dueDate.getMonth() + 1;
                                            int dueYear = dueDate.getYear() + 1900;
                                            int transYear = transDt.getYear() + 1900;
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            System.out.println("JEFFFFFFFFFFFFF" + "transDt" + transDt + "dueDate" + dueDate + "transMonth" + transMonth + "dueMonth" + dueMonth + "transYear" + transYear + "transMonth" + transMonth);
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                            System.out.println("here totalDelay is now:" + totalDelay);
                                        }
                                    }
                                    lstRec = null;
                                    depRecMap = new HashMap();
                                    depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                    depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                    depRecMap.put("CURR_DT", currDate);
                                    depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                    lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                    if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                                        for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                            depRecMap = (HashMap) lstRec.get(i);
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            int transMonth = currDate.getMonth() + 1;//2
                                            int dueMonth = dueDate.getMonth() + 1;//2
                                            int dueYear = dueDate.getYear() + 1900;//2015
                                            int transYear = currDate.getYear() + 1900;//2015
                                            int delayedInstallment;// = transMonth - dueMonth;
                                            System.out.println("JEFFFFFFFFFFFFF" + "dueDate" + dueDate + "transMonth" + transMonth + "dueMonth" + dueMonth + "transYear" + transYear + "transMonth" + transMonth);
                                            if (dueYear == transYear) {
                                                delayedInstallment = transMonth - dueMonth;
                                            } else {
                                                int diffYear = transYear - dueYear;
                                                delayedInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                            }
                                            if (delayedInstallment < 0) {
                                                delayedInstallment = 0;
                                            }
                                            totalDelay = totalDelay + delayedInstallment;
                                            System.out.println("there totalDelay is now:" + totalDelay);
                                        }
                                    }
                                    lstRec = null;
                                    //System.out.println(" totalDelay>" + totalDelay);
                                    delayAmt = delayAmt * totalDelay;
                                    System.out.println("delayAmt calculated = :" + delayAmt);
                                    //delayAmt = (double) delayAmt * 100), 100) / 100;
                                    double oldPenalAmt = CommonUtil.convertObjToDouble(accountMap.get("DELAYED_AMOUNT"));
                                    long oldPenalMonth = CommonUtil.convertObjToLong(accountMap.get("DELAYED_MONTH"));
                                    System.out.println("oldPenalAmt" + oldPenalAmt + "oldPenalMonth" + oldPenalMonth);
                                    double balanceAmt = 0.0;
                                    if (oldPenalAmt > 0) {
                                        balanceAmt = delayAmt - oldPenalAmt;
                                        totalDelay = totalDelay - oldPenalMonth;
                                        System.out.println("here now is :" + "balanceAmt" + balanceAmt + "totalDelay" + totalDelay);
                                    } else {
                                        balanceAmt = delayAmt;
                                        System.out.println("balanceAmt = delayAmt" + balanceAmt);
                                    }
                                    System.out.println("calculation actualDelay" + actualDelay);
                                    System.out.println("calculating CommonUtil.convertObjToDouble(rdDataMap.get(DEPOSIT_AMT)" + CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT")));
                                    double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                    System.out.println("calculating balanceAmt" + balanceAmt);
                                    double totalDemand = principal + balanceAmt;
                                    System.out.println("calculated totalDemand" + totalDemand);
                                    rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                    rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(balanceAmt));
                                    rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                    rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                    rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                    System.out.println(" balanceAmt>" + balanceAmt + " totalDelay" + totalDelay);
                                }
                                //Set of code added by Jeffin John on 28-12-2014 fro Mantis : 9969
                                if (penalCalcType != null && !penalCalcType.equals("") && penalCalcType.equals("Days")) {
                                    depAmt = CommonUtil.convertObjToDouble(accountMap.get("DEPOSIT_AMT"));
                                    dataMap.put("INSTMT_AMT", depAmt);
                                    HashMap delayMap = new HashMap();
                                    double roi = 0.0;
                                    delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
                                    delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
                                    lst = sqlMap.executeQueryForList("getSelectDelayedRate", delayMap);
                                    if (lst != null && lst.size() > 0) {
                                        delayMap = (HashMap) lst.get(0);
                                        roi = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
                                    }
                                    List lstRec = null;
                                    HashMap depRecMap = new HashMap();
                                    depRecMap = new HashMap();
                                    double penal = 0.0;
                                    depRecMap.put("DEPOSIT_NO", actNum + "_1");
                                    depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
                                    depRecMap.put("CURR_DT", currDate);
                                    depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
                                    lstRec = sqlMap.executeQueryForList("getDepTransRecurr", depRecMap);
                                    int size = CommonUtil.convertObjToInt(lstRec.size());
                                    if (lstRec != null && lstRec.size() > 0) {
                                        for (int i = 0; i < size; i++) {
                                            depRecMap = (HashMap) lstRec.get(i);
                                            double amount = depAmt * (i + 1);
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                            if (DateUtil.dateDiff(dueDate, currDt) > 0) {
                                                double diff = DateUtil.dateDiff(dueDate, currDt) - 1;
                                                if (diff > 0) {
                                                    penal = penal + ((amount * roi * diff) / 36500);
                                                }
                                            }
                                        }
                                    }
                                    penal = Math.round(penal);
                                    double principal = actualDelay * CommonUtil.convertObjToDouble(rdDataMap.get("DEPOSIT_AMT"));
                                    double totalDemand = principal + penal;
                                    rdDataMap.put("DELAYED_MONTH", accountMap.get("DELAYED_MONTH"));
                                    rdDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penal));
                                    rdDataMap.put("PRINCIPAL", String.valueOf(principal));
                                    rdDataMap.put("TOTAL_DEMAND", String.valueOf(totalDemand));
                                    rdDataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf(totalDelay));
                                }
                                //Ends Here
                            }
                        }
                        System.out.println(" rdDataMap>" + rdDataMap);
                        if (retired != null && retired.equals("YES")) {
                            HashMap accountMap1 = new HashMap();
                            accountMap1.put("DEPOSIT_NO", acNoSubNo);
                            List lst1 = sqlMap.executeQueryForList("TransAll.getSumDeposit", accountMap1);
                            if (lst1 != null && lst1.size() > 0) {
                                accountMap1 = (HashMap) lst1.get(0);
                                dataMap.put("PRINCIPAL", accountMap1.get("AMOUNT"));//+" : "+rdDataMap.get("PRINCIPAL"));
                                dataMap.put("TOTAL", CommonUtil.convertObjToDouble(accountMap1.get("COUNT")));
                            }
                        } else {
                            dataMap.put("PRINCIPAL", rdDataMap.get("PRINCIPAL"));
                            dataMap.put("TOTAL", actualDelay);
                        }
                        dataMap.put("PRINCIPAL_OLD", rdDataMap.get("PRINCIPAL"));
                        dataMap.put("PENAL", rdDataMap.get("DEPOSIT_PENAL_AMT"));
                        dataMap.put("TOTAL_DEMAND", rdDataMap.get("TOTAL_DEMAND"));
                        dataMap.put("DEPOSIT_PENAL_MONTH", rdDataMap.get("DEPOSIT_PENAL_MONTH"));
                        dataMap.put("INTEREST", new Double(0));
                        dataMap.put("CHARGES", new Double(0));
                        if (CommonUtil.convertObjToDouble(rdDataMap.get("TOTAL_DEMAND")) <= 0.0) {
                            dataMap = null;
                        }
//                    } else {
//                        System.out.println("This deposit already paid this month, please make payment nextmonth : " + actNum);
//                    }
                }
                System.out.println("Deposit dataMap : " + dataMap);
            }
        }
        return dataMap;
    }

    public String insertTransAll(HashMap map) throws Exception {
        String retVal = null;
        try {
            HashMap finalMap = new HashMap();
            finalMap = (HashMap) map.get("RECOVERY_PROCESS_LIST");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "", key2 = "";
            String actNum = "";
            String prodType = "";
            HashMap m1 = new HashMap();
            System.out.println("finalMap : " + finalMap + " finalMap.size() : " + finalMap.size());
            objTO.setTransallId(getTransAllId());
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                //System.out.println("key1 : " + key1);
                ArrayList singleRecoveryList = new ArrayList();
                m1 = (HashMap) finalMap.get(key1);
                //System.out.println("IN INSERT : " + m1 + "IN INSERTdgdfgdf== : " + m1.get("AMTORNOOFINST"));
                for (int j = 0; j < m1.size(); j++) {
                    HashMap singleAccountMap = new HashMap();
                    processLstIterator1 = m1.keySet().iterator();
                    key2 = (String) processLstIterator1.next();
                    if (key2.equals("MDS")) {
                        ArrayList mdsList = new ArrayList();
                        singleAccountMap = (HashMap) m1.get(key2);
                        if (singleAccountMap.get("SPLIT_DETAILS") != null) {
                            mdsList = (ArrayList) singleAccountMap.get("SPLIT_DETAILS");
                            //System.out.println("mdsList size : " + mdsList.size());
                            double totint = 0;
                            for (int m = 0; m < mdsList.size(); m++) {
                                List aList = (List) mdsList.get(m);
                                System.out.println("mdsList size2 : " + mdsList.size());
                                System.out.println("singleAccountMap singleAccountMap : " + singleAccountMap);
                                if (singleAccountMap.get("CLOCK_NO") != null) {
                                    objTO.setClockNo(singleAccountMap.get("CLOCK_NO").toString());
                                }
                                if (singleAccountMap.get("PROD_DESCRIPTION") != null) {
                                    objTO.setDescription(singleAccountMap.get("PROD_DESCRIPTION").toString());
                                }
                                if (singleAccountMap.get("MEMBER_NO") != null) {
                                    objTO.setMemberNo(singleAccountMap.get("MEMBER_NO").toString());
                                }
                                if (singleAccountMap.get("CUST_NAME") != null) {
                                    objTO.setCustName(singleAccountMap.get("CUST_NAME").toString());
                                }
                                if (singleAccountMap.get("PROD_TYPE") != null) {
                                    objTO.setSchName(singleAccountMap.get("PROD_TYPE").toString());
                                }
                                if (singleAccountMap.get("ACT_NUM") != null) {
                                    objTO.setAcNo(singleAccountMap.get("ACT_NUM").toString());
                                }
                                if (aList.get(0) != null) {
                                    objTO.setPayingAmt(CommonUtil.convertObjToDouble(1));
                                }
                                if (aList.get(3) != null) {
                                    objTO.setPrincipal(CommonUtil.convertObjToDouble(aList.get(3)));
                                }
                                if (aList.get(5) != null) {
                                    objTO.setPenel(CommonUtil.convertObjToDouble(0.0));
                                }
                                if (aList.get(4) != null) {
                                    if (m != (mdsList.size() - 1)) {
                                        totint += CommonUtil.convertObjToDouble(aList.get(4));
                                        objTO.setInterest(CommonUtil.convertObjToDouble(aList.get(4)));
                                    } else {
                                        objTO.setInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_INT")) - totint);
                                    }
                                }
                                if (aList.get(6) != null) {
                                    objTO.setBonus(CommonUtil.convertObjToDouble(aList.get(6)));
                                }
                                if (singleAccountMap.get("CHARGES") != null) {
                                    objTO.setOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("CHARGES")));
                                }
                                if (aList.get(8) != null) {
                                    objTO.setNotice(CommonUtil.convertObjToDouble(aList.get(8)));
                                }
                                if (aList.get(7) != null) {
                                    objTO.setArbitration(CommonUtil.convertObjToDouble(aList.get(7)));
                                }
                                if (singleAccountMap.get("PROD_ID") != null) {
                                    objTO.setProdId(singleAccountMap.get("PROD_ID").toString());
                                }
                                if (singleAccountMap.get("TOT_PRIN") != null) {
                                    //objTO.setTotprincipal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PRIN").toString()).doubleValue());
                                }
                                if (singleAccountMap.get("TOT_INT") != null) {
                                    //objTO.setTotInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_INT").toString()).doubleValue());
                                }
                                if (singleAccountMap.get("TOT_PENAL") != null) {
                                    //objTO.setTotPenel(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PENAL").toString()).doubleValue());
                                }
                                if (singleAccountMap.get("TOT_OTHERS") != null) {
                                    //objTO.setTotOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_OTHERS").toString()).doubleValue());
                                }
                                if (singleAccountMap.get("TOT_GRAND") != null) {
                                    //objTO.setGrandTotal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_GRAND").toString()).doubleValue());
                                }
                                //System.out.println("aList.get(7) aList.get(7) : " + aList.get(0).toString());
                                objTO.setAuthorizeStatus("");
                                objTO.setAuthorizeBy("");
                                sqlMap.executeUpdate("insertTransAllTO", objTO);
                            }
                        } else {
                            singleAccountMap = (HashMap) m1.get(key2);
                            //System.out.println("IN singleAccountMap : " + singleAccountMap);
                            if (singleAccountMap.get("CLOCK_NO") != null) {
                                objTO.setClockNo(singleAccountMap.get("CLOCK_NO").toString());
                            }
                            if (singleAccountMap.get("PROD_DESCRIPTION") != null) {
                                objTO.setDescription(singleAccountMap.get("PROD_DESCRIPTION").toString());
                            }
                            if (singleAccountMap.get("MEMBER_NO") != null) {
                                objTO.setMemberNo(singleAccountMap.get("MEMBER_NO").toString());
                            }
                            if (singleAccountMap.get("CUST_NAME") != null) {
                                objTO.setCustName(singleAccountMap.get("CUST_NAME").toString());
                            }
                            if (singleAccountMap.get("PROD_TYPE") != null) {
                                objTO.setSchName(singleAccountMap.get("PROD_TYPE").toString());
                            }
                            if (singleAccountMap.get("ACT_NUM") != null) {
                                objTO.setAcNo(singleAccountMap.get("ACT_NUM").toString());
                            }
                            if (singleAccountMap.get("AMTORNOOFINST") != null) {
                                objTO.setPayingAmt(CommonUtil.convertObjToDouble(singleAccountMap.get("AMTORNOOFINST")));
                            }
                            if (singleAccountMap.get("PRINCIPAL") != null) {
                                objTO.setPrincipal(CommonUtil.convertObjToDouble(singleAccountMap.get("PRINCIPAL")));
                            }
                            if (singleAccountMap.get("PENAL") != null) {
                                objTO.setPenel(CommonUtil.convertObjToDouble(0.0));
                            }
                            if (singleAccountMap.get("INTEREST") != null) {
                                objTO.setInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("INTEREST")));
                            }
                            if (singleAccountMap.get("BONUS") != null) {
                                objTO.setBonus(CommonUtil.convertObjToDouble(singleAccountMap.get("BONUS")));
                            }
                            if (singleAccountMap.get("CHARGES") != null) {
                                objTO.setOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("CHARGES")));
                            }
                            if (singleAccountMap.get("NOTICE_AMOUNT") != null) {
                                objTO.setNotice(CommonUtil.convertObjToDouble(singleAccountMap.get("NOTICE_AMOUNT")));
                            }
                            if (singleAccountMap.get("ARBITRATION_AMOUNT") != null) {
                                objTO.setArbitration(CommonUtil.convertObjToDouble(singleAccountMap.get("ARBITRATION_AMOUNT")));
                            }
                            if (singleAccountMap.get("PROD_ID") != null) {
                                objTO.setProdId(singleAccountMap.get("PROD_ID").toString());
                            }
                            if (singleAccountMap.get("TOT_PRIN") != null) {
                                //objTO.setTotprincipal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PRIN").toString()).doubleValue());
                            }
                            if (singleAccountMap.get("TOT_INT") != null) {
                                //objTO.setTotInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_INT").toString()).doubleValue());
                            }
                            if (singleAccountMap.get("TOT_PENAL") != null) {
                                //objTO.setTotPenel(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PENAL").toString()).doubleValue());
                            }
                            if (singleAccountMap.get("TOT_OTHERS") != null) {
                                //objTO.setTotOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_OTHERS").toString()).doubleValue());
                            }
                            if (singleAccountMap.get("TOT_GRAND") != null) {
                                //objTO.setGrandTotal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_GRAND").toString()).doubleValue());
                            }
                            objTO.setAuthorizeStatus("");
                            objTO.setAuthorizeBy("");
                            sqlMap.executeUpdate("insertTransAllTO", objTO);
                        }
                    } else {
                        singleAccountMap = (HashMap) m1.get(key2);
                        //System.out.println("IN singleAccountMap : " + singleAccountMap);
                        if (singleAccountMap.get("CLOCK_NO") != null) {
                            objTO.setClockNo(singleAccountMap.get("CLOCK_NO").toString());
                        }
                        if (singleAccountMap.get("PROD_DESCRIPTION") != null) {
                            objTO.setDescription(singleAccountMap.get("PROD_DESCRIPTION").toString());
                        }
                        if (singleAccountMap.get("MEMBER_NO") != null) {
                            objTO.setMemberNo(singleAccountMap.get("MEMBER_NO").toString());
                        }
                        if (singleAccountMap.get("CUST_NAME") != null) {
                            objTO.setCustName(singleAccountMap.get("CUST_NAME").toString());
                        }
                        if (singleAccountMap.get("PROD_TYPE") != null) {
                            if (singleAccountMap.get("PROD_TYPE").equals("GL")) {
                                objTO.setParticulars(CommonUtil.convertObjToStr(singleAccountMap.get("PARTICULARS")));
                            }
                            objTO.setSchName(singleAccountMap.get("PROD_TYPE").toString());
                        }
                        if (singleAccountMap.get("ACT_NUM") != null) {
                            objTO.setAcNo(singleAccountMap.get("ACT_NUM").toString());
                        }
                        if (singleAccountMap.get("AMTORNOOFINST") != null) {
                            objTO.setPayingAmt(CommonUtil.convertObjToDouble(singleAccountMap.get("AMTORNOOFINST")));
                        }
                        if (singleAccountMap.get("PRINCIPAL") != null) {
                            objTO.setPrincipal(CommonUtil.convertObjToDouble(singleAccountMap.get("PRINCIPAL")));
                        }
                        if (singleAccountMap.get("PENAL") != null) {
                            objTO.setPenel(CommonUtil.convertObjToDouble(singleAccountMap.get("PENAL")));
                        }
                        if (singleAccountMap.get("INTEREST") != null) {
                            objTO.setInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("INTEREST")));
                        }
                        if (singleAccountMap.get("BONUS") != null) {
                            objTO.setBonus(CommonUtil.convertObjToDouble(singleAccountMap.get("BONUS")));
                        }
                        if (singleAccountMap.get("CHARGES") != null) {
                            objTO.setOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("CHARGES")));
                        }
                        if (singleAccountMap.get("NOTICE_AMOUNT") != null) {
                            objTO.setNotice(CommonUtil.convertObjToDouble(singleAccountMap.get("NOTICE_AMOUNT")));
                        }
                        if (singleAccountMap.get("ARBITRATION_AMOUNT") != null) {
                            objTO.setArbitration(CommonUtil.convertObjToDouble(singleAccountMap.get("ARBITRATION_AMOUNT")));
                        }
                        if (singleAccountMap.get("PROD_ID") != null) {
                            objTO.setProdId(singleAccountMap.get("PROD_ID").toString());
                        }
                        if (singleAccountMap.get("TOT_PRIN") != null) {
                            //objTO.setTotprincipal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PRIN").toString()).doubleValue());
                        }
                        if (singleAccountMap.get("TOT_INT") != null) {
                            //objTO.setTotInterest(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_INT").toString()).doubleValue());
                        }
                        if (singleAccountMap.get("TOT_PENAL") != null) {
                            //objTO.setTotPenel(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_PENAL").toString()).doubleValue());
                        }
                        if (singleAccountMap.get("TOT_OTHERS") != null) {
                            //objTO.setTotOthers(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_OTHERS").toString()).doubleValue());
                        }
                        if (singleAccountMap.get("TOT_GRAND") != null) {
                            //objTO.setGrandTotal(CommonUtil.convertObjToDouble(singleAccountMap.get("TOT_GRAND").toString()).doubleValue());
                        }
                        objTO.setAuthorizeStatus("");
                        objTO.setAuthorizeBy("");
                        sqlMap.executeUpdate("insertTransAllTO", objTO);
                    }
                    //   retVal = retVal + " -" + objTO.getTransallId();
                }
                retVal = objTO.getTransallId();
            }
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();
            if (map.containsKey("TransactionTO")) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                //System.out.println("transactionTO : " + transactionTO);
                objTO.setTransType(transactionTO.getTransType());
                transactionDAO.setBatchId(objTO.getTransallId());
                transactionDAO.setBatchDate(currDt);
                transactionDAO.execute(map);
            }
        } catch (Exception e) {
            System.out.println("EEE+" + e);
        }
        return retVal;
    }

    private String getTransAllId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANS_ALL_ID");
        String memberTransId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return memberTransId;
    }

    public void processTransactionPart(HashMap map) throws Exception {
        try {
            HashMap finalMap = new HashMap();
            HashMap iterationMap = new HashMap();
            System.out.println("processTransactionPart map : " + map);
            finalMap = (HashMap) map.get("RECOVERY_PROCESS_LIST");
            System.out.println("finalMap : " + finalMap + "finalMap size : " + finalMap.size());
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "", key2 = "";
            String actNum = "";
            String prodType = "";
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                //System.out.println("key1 : " + key1);
                ArrayList singleRecoveryList = new ArrayList();
                iterationMap = (HashMap) finalMap.get(key1);
                //System.out.println("m1 : " + iterationMap);
                for (int j = 0; j < iterationMap.size(); j++) {
                    HashMap singleAccountMap = new HashMap();
                    processLstIterator1 = iterationMap.keySet().iterator();
                    key2 = (String) processLstIterator1.next();
                    singleAccountMap = (HashMap) iterationMap.get(key2);
                    System.out.println("singleAccountMap : " + singleAccountMap + "key2 : " + key2);
                    prodType = CommonUtil.convertObjToStr(singleAccountMap.get("PROD_TYPE"));
                    actNum = CommonUtil.convertObjToStr(singleAccountMap.get("ACT_NUM"));
                    if (key2.equals("MDS")) {
                        transactionPartMDS(singleAccountMap, map);
                    } else if (key2.equals("TD")) {
                        transactionPartDeposits(singleAccountMap, map);
                    } else if (key2.equals("TL")) {
                        HashMap totalMap = new HashMap();
                        if (objTO.getRetired() != null && objTO.getRetired().equals("YES")) {
                            totalMap = (HashMap) iterationMap.get("TOTAL_AMOUNT");
                            System.out.println("processTransactionPart totalMap TL : " + totalMap);
                        }
                        transactionPartLoans(singleAccountMap, map, totalMap);
                    } else if (key2.equals("SA")) {
                        transactionPartOA(singleAccountMap, map, key2);
                    } else if (key2.equals("GL")) {
                        transactionPartGL(singleAccountMap, map);
                    }
                    if (!key2.equals("SA") && !key2.equals("GL")) {  //Update LOCK_STATUS NO&& !key1.equals("TD") && !key1.equals("TL") 
                        HashMap updateMap = new HashMap();
                        if (!key2.equals("MDS")) {
                            if (actNum.indexOf("_") != -1) {
                                actNum = actNum.substring(0, actNum.indexOf("_"));
                            }
                            updateMap.put("ACCT_NUM", actNum);
                        } else {
                            String chittalNo = "";
                            String subNo = "";
                            if (actNum.indexOf("_") != -1) {
                                chittalNo = actNum.substring(0, actNum.indexOf("_"));
                                subNo = actNum.substring(actNum.indexOf("_") + 1, actNum.length());
                                updateMap.put("CHITTAL_NO", chittalNo);
                                updateMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            }
                        }
                        updateMap.put("LOCK_STATUS", "N");
                        System.out.println("key2 : " + key2 + " updateMap : " + updateMap);
                        sqlMap.executeUpdate("updateLockStatus" + key2, updateMap);
                    }
//                    if (key2.equals("SA")) {  // UPDATE PAID Amd BALANCE AMOUNT
//                        HashMap inputMap = new HashMap();
//                        recoveredAmt = 0.0;
//                        recoveredAmt = CommonUtil.convertObjToDouble(singleAccountMap.get("PRINCIPAL"));
//                        while (recoveredAmt > 0) {
//                            inputMap.put("ACCT_NUM", actNum);
//                            updateSABalance(inputMap);
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        } finally {
            finalMap = null;
            //iterationMap = null;
        }
    }

    private void updateSABalance(HashMap whereMap) throws Exception {
        System.out.println("entered here .......updateSABalance" + whereMap);
        sqlMap.executeUpdate("insertAuthCreditSales", whereMap);
//        List installmentList = sqlMap.executeQueryForList("getSuspendCurrInstDetails1", whereMap);
//        if (installmentList != null && installmentList.size() > 0 && recoveredAmt > 0) {
//            for (int k = 0; k < installmentList.size(); k++) {
//                double currInstAmt = 0.0;
//                whereMap = (HashMap) installmentList.get(k);
//                System.out.println(" whereMap" + whereMap);
//                currInstAmt = CommonUtil.convertObjToDouble(whereMap.get("BALANCE_AMOUNT"));
//                whereMap.put("AMOUNT", String.valueOf(currInstAmt));
//                whereMap.put("PAID_DATE", intUptoDt);
//                if (currInstAmt > 0 && recoveredAmt >= currInstAmt && recoveredAmt > 0) {
//                    //System.out.println("inside updategetSuspendCurrInstDetails1" + whereMap);
//                    recoveredAmt -= currInstAmt;
//                    sqlMap.executeUpdate("updateSABalanceAmount1", whereMap);
//                } else if (recoveredAmt > 0 && currInstAmt > 0) {
//                    //System.out.println("inside updategetSuspendCurrInstDetails1else" + whereMap);
//                    if (recoveredAmt < currInstAmt) {
//                        whereMap.put("AMOUNT", String.valueOf(recoveredAmt));
//                        recoveredAmt = 0.0;
//                    }
//                    recoveredAmt -= currInstAmt;
//                    if (recoveredAmt <= 0) {
//                        k = installmentList.size();
//                    }
//                    sqlMap.executeUpdate("updateSABalanceAmount1", whereMap);
//                }
//            }
//        }
//        installmentList = null;
        recoveredAmt = 0;
    }

    private void transactionPartOA(HashMap operativeMap, HashMap map, String prodType) throws Exception {  // OA and SA
        try {
            System.out.println("transactionPartOA operativeMap : " + operativeMap);
            HashMap txMap = new HashMap();
            HashMap operativeDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();
            double recoveredAmount = 0;
            recoveredAmount = CommonUtil.convertObjToDouble(operativeMap.get("RECOVERED_AMOUNT"));
            double interest = CommonUtil.convertObjToDouble(operativeMap.get("INTEREST"));
            double principal = CommonUtil.convertObjToDouble(operativeMap.get("PRINCIPAL"));
            System.out.println("recoveredAmount : " + recoveredAmount + " interest : " + interest + " principal : " + principal);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", operativeMap.get("PROD_ID"));
                List lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodType, acHeadMap);
                if (lst != null && lst.size() > 0) {
                    acHeadMap = (HashMap) lst.get(0);
                }
                //AC HEAD INT
                HashMap acHeadSA = (HashMap) sqlMap.executeQueryForObject("TransAll.getACCHeadSA", new HashMap());
                if (acHeadSA == null || acHeadSA.size() == 0) {
                    throw new TTException("Account heads not set properly...");
                }
                String acH = (String) acHeadSA.get("ACCOUNTHEAD_ID");
                acHeadMap.put("ACCOUNT_HEAD_ID", acH);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                        }
                    }
                    //DEBIT
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.GL) && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferTo.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        transferTo.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        transferTo.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                    TxTransferTO.add(transferTo);
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, operativeMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, operativeMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, principal);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    transferTo.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    TxTransferTO.add(transferTo);
                    if (interest > 0) {
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.CR_PROD_TYPE, operativeMap.get("PROD_TYPE"));
                        txMap.put(TransferTrans.CR_AC_HD, acH);
                        txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, interest);
                        transferTo.setTransId("-");
                        transferTo.setBatchId("-");
                        transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                        transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                        transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                        TxTransferTO.add(transferTo);
                    }
                    transferDAO = new TransferDAO();
                    operativeDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    operativeDataMap.put("COMMAND", operativeDataMap.get("MODE"));
                    operativeDataMap.put("TxTransferTO", TxTransferTO);
                    operativeDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    HashMap transMap = transferDAO.execute(operativeDataMap, false);
                    operativeDataMap = null;
                    operativeMap = null;
                    acHeadMap = null;
                    txMap = null;
                } else if (transactionTO.getTransType().equals("CASH")) {
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    HashMap cashTransMap = new HashMap();
                    cashTransMap.put("SELECTED_BRANCH_ID", operativeMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_NUM", operativeMap.get("ACT_NUM"));
                    cashTransMap.put("PROD_ID", operativeMap.get("PROD_ID"));
                    cashTransMap.put("BRANCH_CODE", operativeMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_HEAD", operativeMap.get("AC_HEAD"));
                    cashTransMap.put("TOKEN_NO", "");
                    cashTransMap.put("LIMIT", recoveredAmount);
                    cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                    cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    cashTransMap.put("USER_ID", operativeMap.get("USER_ID"));
                    ArrayList cashList = new ArrayList();
                    operativeMap.put("OPERATIVE_CASH_TRANSACTION", "OPERATIVE_CASH_TRANSACTION");
                    cashList = setCashTransactionValue(operativeMap, acHeadMap, cashTransMap);
                    objCashTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        } finally {
        }
    }

    private void transactionPartLoans(HashMap loanMap, HashMap map, HashMap totalMap) throws Exception {
        try {
            System.out.println("loanMap : " + loanMap + " map : " + map + " totalMap : " + totalMap);
            HashMap txMap = new HashMap();
            HashMap loanDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            HashMap ALL_LOAN_AMOUNT = new HashMap();
            TransactionTO transactionTO = new TransactionTO();
            double penalAmount = CommonUtil.convertObjToDouble(loanMap.get("PENAL"));
            double chargesAmount = CommonUtil.convertObjToDouble(loanMap.get("CHARGES"));
            double interestAmount = CommonUtil.convertObjToDouble(loanMap.get("INTEREST"));
            double principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL"));
            double recoveredAmount = CommonUtil.convertObjToDouble(loanMap.get("RECOVERED_AMOUNT"));
            HashMap acHeadMap = new HashMap();
            if (loanMap.containsKey("ACT_NUM")) {
                acHeadMap.put("ACCT_NUM", loanMap.get("ACT_NUM"));
            }
            if (loanMap.containsKey("PROD_ID")) {
                acHeadMap.put("PROD_ID", loanMap.get("PROD_ID"));
            }
            List lst = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", acHeadMap);
            if (lst != null && lst.size() > 0) {
                acHeadMap = (HashMap) lst.get(0);
            }
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                //ALL_LOAN_AMOUNT Map Start
                if (penalAmount > 0) {
                    ALL_LOAN_AMOUNT.put("PENAL_INT", String.valueOf(penalAmount));
                    ALL_LOAN_AMOUNT.put("LOAN_CLOSING_PENAL_INT", String.valueOf(penalAmount));
                }
                if (principalAmount > 0) {
                    ALL_LOAN_AMOUNT.put("INSTALL_DT", String.valueOf(penalAmount));
                    ALL_LOAN_AMOUNT.put("CURR_MONTH_PRINCEPLE", String.valueOf(principalAmount));
                    ALL_LOAN_AMOUNT.put("LOAN_BALANCE_PRINCIPAL", String.valueOf(principalAmount));
                }
                if (interestAmount > 0) {
                    ALL_LOAN_AMOUNT.put("CURR_MONTH_INT", String.valueOf(interestAmount));
                    ALL_LOAN_AMOUNT.put("INTEREST", String.valueOf(interestAmount));
                }
                // Charges Start
                if (chargesAmount > 0) {
                    Map otherChargesMap = new HashMap();
                    HashMap chargeMap = new HashMap();
                    chargeMap.put("INT_CALC_UPTO_DT", intUptoDt);
                    chargeMap.put("ACT_NUM", loanMap.get("ACT_NUM"));
                    List chargeList = sqlMap.executeQueryForList("getRecoveryChargeList", chargeMap);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            chargeMap = (HashMap) chargeList.get(i);
                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("AMOUNT"));
                            if (chargeAmt > 0) {
                                if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES")
                                        || chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") || chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES")
                                        || chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") || chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES")) {
                                    ALL_LOAN_AMOUNT.put(chargeMap.get("CHARGE_TYPE"), String.valueOf(chargeAmt));
                                } else {
                                    otherChargesMap.put(chargeMap.get("CHARGE_TYPE"), String.valueOf(chargeAmt));
                                }
                            }
                        }
                        if (otherChargesMap.size() > 0) {
                            ALL_LOAN_AMOUNT.put("OTHER_CHARGES", otherChargesMap);
                        }
                    }
                }
                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.size() > 0) {
                    loanDataMap.put("ALL_AMOUNT", ALL_LOAN_AMOUNT);
                }
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                        }
                    }
                    //DEBIT
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.GL) && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferTo.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        transferTo.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        transferTo.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, penalAmount + interestAmount + principalAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //System.out.println("objTO.getTransallId() L==" + objTO.getTransallId());
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    //transferTo.setTransModType(CommonConstants.LOAN_TRANSMODE_TYPE);
                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    TxTransferTO.add(transferTo);
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, loanMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, loanMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("AC_HEAD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, loanMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    //System.out.println("objTO.getTransallId() L11==" + objTO.getTransallId());
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    if (loanMap.containsKey("PROD_TYPE") && CommonUtil.convertObjToStr(loanMap.get("PROD_TYPE")).equals(TransactionFactory.LOANS)) {
                        transferTo.setTransModType(CommonConstants.LOAN_TRANSMODE_TYPE);
                    } else {
                        transferTo.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    TxTransferTO.add(transferTo);
                    transferDAO = new TransferDAO();
                    loanDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    loanDataMap.put("COMMAND", loanDataMap.get("MODE"));
                    loanDataMap.put("TxTransferTO", TxTransferTO);
                    loanDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    loanDataMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    loanDataMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                    System.out.println(" Before Transfer Dao loanDataMap : " + loanDataMap);
                    HashMap transMap = transferDAO.execute(loanDataMap, false);
                    loanDataMap = null;
                    acHeadMap = null;
//                    loanMap = null;
                    txMap = null;
                } else if (transactionTO.getTransType().equals("CASH")) {
                    HashMap cashTransMap = new HashMap();
                    cashTransMap.put("SELECTED_BRANCH_ID", loanMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_NUM", loanMap.get("ACT_NUM"));
                    cashTransMap.put("PROD_ID", loanMap.get("PROD_ID"));
                    cashTransMap.put("BRANCH_CODE", loanMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_HEAD", acHeadMap.get("AC_HEAD"));
                    cashTransMap.put("TOKEN_NO", "");
                    cashTransMap.put("LIMIT", recoveredAmount);
                    cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                    cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    cashTransMap.put("USER_ID", loanMap.get("USER_ID"));
                    ArrayList cashList = new ArrayList();
                    loanMap.put("LOAN_CASH_TRANSACTION", "LOAN_CASH_TRANSACTION");
                    cashList = setCashTransactionValue(loanMap, acHeadMap, cashTransMap);
                    cashTransMap = null;
                }
            }
            HashMap loanClosingMap = new HashMap();
            loanClosingMap.put("ACCOUNTNO", loanMap.get("ACT_NUM"));
            lst = sqlMap.executeQueryForList("getBalanceLoanPrincipalAmt", loanClosingMap);
            if (lst != null && lst.size() > 0) {
                loanClosingMap = (HashMap) lst.get(0);
                double loanBalancePrincipal = CommonUtil.convertObjToDouble(loanClosingMap.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                principalAmount = CommonUtil.convertObjToDouble(loanMap.get("PRINCIPAL")).doubleValue();
                System.out.println("loanBalancePrincipal : " + loanBalancePrincipal + " principalAmount : " + principalAmount);
                if (principalAmount == loanBalancePrincipal) {
                    loanClosingMap.put("STATUS_DT", currDt.clone());
                    loanClosingMap.put("STATUS_BY", loanMap.get("USER_ID"));
                    loanClosingMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                    loanClosingMap.put("ACCT_CLOSE_DT", currDt.clone());
                    loanClosingMap.put("ACCT_NUM", loanMap.get("ACT_NUM"));
                    sqlMap.executeUpdate("updateLoanActClosingDetailSalRecovery", loanClosingMap);
                } else {
                    System.out.println("Not Closed : " + loanMap.get("ACT_NUM"));
                }
            }

            //This part is for loan closing
            //System.out.println("RETIRED ON" + objTO.getRetired());
            if (objTO.getRetired() != null && objTO.getRetired().equals("YES")) {
                double accountClosingCharge = 0.0;
                final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
                objAccountClosingTO.setCommand("INSERT");
                objAccountClosingTO.setStatus("");
                objAccountClosingTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                objAccountClosingTO.setStatusDt(currDt);
                objAccountClosingTO.setActNum(loanMap.get("ACT_NUM").toString());
                objAccountClosingTO.setUnusedChk(0.0);
                HashMap chargesMap = new HashMap();
                HashMap temp = new HashMap();
                temp.put(CommonConstants.PRODUCT_ID, loanMap.get("PROD_ID").toString());
                List chargeList = ServerUtil.executeQuery("getLoanAccCloseCharges", temp);//getTermLoanAccountClosingCharge
                if (chargeList.size() > 0) {
                    chargesMap = (HashMap) chargeList.get(0);
                    accountClosingCharge = CommonUtil.convertObjToDouble(chargesMap.get("AC_CLOSING_CHRG"));
                }
                objAccountClosingTO.setActClosingChrg(CommonUtil.convertObjToDouble(accountClosingCharge));
                objAccountClosingTO.setIntPayable(CommonUtil.convertObjToDouble(interestAmount));
                objAccountClosingTO.setChrgDetails(CommonUtil.convertObjToDouble(chargesAmount));
                objAccountClosingTO.setPayableBal(CommonUtil.convertObjToDouble(recoveredAmount));
                double insuranceCharge = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES"));
                objAccountClosingTO.setInsuranceCharges(CommonUtil.convertObjToDouble(insuranceCharge));
                objAccountClosingTO.setCreditIntAD(new Double(0));
                objAccountClosingTO.setSusbsidyAmt(new Double(0));
                objAccountClosingTO.setRebateInterest(new Double(0));
                objAccountClosingTO.setProdId(loanMap.get("PROD_ID").toString());
                objAccountClosingTO.setVariableNo("");
                final HashMap data = new HashMap();
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                data.put("accountclosing", objAccountClosingTO);
                data.put("Charge List Data", chargeAmount(loanMap.get("PROD_ID").toString(), loanMap.get("ACT_NUM").toString()));
                data.put("BEHAVE_LIKE", getLoanBehaves(loanMap.get("PROD_ID").toString()));
                data.put("PROD_TYPE", "TermLoan");
                data.put("PROD_ID", loanMap.get("PROD_ID").toString());
                data.put("LIEN", new Double(0)); //Get this from the common screen
                data.put("FREEZE", new Double(0)); //Get this from the common screen
                data.put(CommonConstants.AUTHORIZEMAP, null); //For Authorization added 28 Apr 2005
                data.put("MODE", "INSERT");
                data.put(CommonConstants.MODULE, null);
                data.put(CommonConstants.SCREEN, map.get(CommonConstants.SCREEN));
                data.put(CommonConstants.SELECTED_BRANCH_ID, loanMap.get("BRANCH_CODE").toString());
                data.put(CommonConstants.BRANCH_ID, loanMap.get("BRANCH_CODE").toString());
                data.put("stMap", getCalMap(CommonUtil.convertObjToDouble(accountClosingCharge), CommonUtil.convertObjToStr(loanMap.get("BRANCH_CODE"))));
                data.put("TOTAL_AMOUNT", totalMap);
                AccountClosingDAO accDAO = new AccountClosingDAO();
                accDAO.execute(data, false);
            }
            loanMap = null;
            //End loan closing
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public HashMap getCalMap(double accClosingChrg, String brCode) {
        HashMap retMap = new HashMap();
        retMap = callServiceTaxCalculation(String.valueOf(accClosingChrg), brCode);
        return retMap;
    }

    private HashMap callServiceTaxCalculation(String calAmt, String brCode) {
        HashMap calMap = new HashMap();
        calMap.put(CommonConstants.BRANCH_ID, brCode);
        calMap.put("CAL_AMT", calAmt);
        calMap.put("ST_CAL", "ST_CAL");
        calMap.put("CHARGE_TYPE", "ACT_CLOSING_CHG");
        HashMap viewMap = new HashMap();
        viewMap.put("SERVICE_TAX", calMap);
        List clLst = ServerUtil.executeQuery("", viewMap);//ServerUtil.executeQuery(calAmt, viewMap)
        System.out.println("clLst" + clLst);
        if (clLst != null && clLst.size() > 0) {
            calMap = (HashMap) clLst.get(0);
            if (CommonUtil.convertObjToDouble(calMap.get("SERVICE_TAX")) > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS1_TAX")) > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS2_TAX")) > 0) {
            }
            //System.out.println("calMap" + calMap);
        }
        return calMap;
    }

    public String getLoanBehaves(String prodId) {
        HashMap hash = new HashMap();
        hash.put("PROD_ID", prodId);
        List lst = ServerUtil.executeQuery("getLoanBehaves", hash);//ServerUtil.executeQuery(prodId, returnMap)
        if (lst.size() > 0) {
            hash = (HashMap) lst.get(0);
            return CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE"));
        }
        return "";
    }

    private List chargeAmount(String prod_id, String acc_no) {
        HashMap hash = new HashMap();
        hash.put("SCHEME_ID", CommonUtil.convertObjToStr(prod_id));
        hash.put("DEDUCTION_ACCU", "C");
        List chargelst = ServerUtil.executeQuery("getAllChargeDetailsData", hash);
        HashMap chargeMap = new HashMap();
        double sanctionAmt = 0.0;
        sanctionAmt = getSanctionAmount(acc_no);
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String desc = "";
                chargeMap = (HashMap) chargelst.get(i);
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                double chargeAmt = 0;
                if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                    chargeAmt = sanctionAmt * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))) / 100;
                    long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                    if (roundOffType != 0) {
                        chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                    }
                    double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT"));
                    double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT"));
                    if (chargeAmt < minAmt) {
                        chargeAmt = minAmt;
                    }
                    if (chargeAmt > maxAmt) {
                        chargeAmt = maxAmt;
                    }
                } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                    HashMap slabMap = new HashMap();
                    double sancAmt = sanctionAmt;
                    slabMap.put("CHARGE_ID", chargeMap.get("CHARGE_ID"));
                    slabMap.put("AMOUNT", sancAmt);
                    List slablst = ServerUtil.executeQuery("getSlabAmount", slabMap);
                    if (slablst != null && slablst.size() > 0) {
                        slabMap = (HashMap) slablst.get(0);
                        chargeAmt = sanctionAmt
                                * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(slabMap.get("CHARGE_RATE"))) / 100;
                        long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(slabMap.get("ROUND_OFF_TYPE")));
                        if (roundOffType != 0) {
                            chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                        }
                        double minAmt = CommonUtil.convertObjToDouble(slabMap.get("MIN_CHARGE_AMOUNT"));
                        double maxAmt = CommonUtil.convertObjToDouble(slabMap.get("MAX_CHARGE_AMOUNT"));
                        if (chargeAmt < minAmt) {
                            chargeAmt = minAmt;
                        }
                        if (chargeAmt > maxAmt) {
                            chargeAmt = maxAmt;
                        }
                    }
                } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                    chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE")));
                }
                chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
            }
        }
        System.out.println("chargeMap :  " + chargeMap + " chargelst : " + chargelst);
        return chargelst;
    }

    public double getSanctionAmount(String acc_no) {
        HashMap hash = new HashMap();
        double sanctionAmt = 0.0;
        hash.put("ACCT_NUM", acc_no);
        List sanctionLst = ServerUtil.executeQuery("getSanctionAmount", hash);
        if (sanctionLst != null && sanctionLst.size() > 0) {
            hash = (HashMap) sanctionLst.get(0);
            sanctionAmt = CommonUtil.convertObjToDouble(hash.get("SANCTION_AMOUNT"));
            System.out.println("sanctionAmt :  " + sanctionAmt);
        }
        return sanctionAmt;
    }

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }
    //-
    /////CASH LOANS

//    private ArrayList setTransactionDetailTLAD(HashMap dataMap) throws Exception {
//        ArrayList cashList = new ArrayList();
//        double paidInterest = 0;
//        double paidprincipal = 0;
//        double transAmt = 0.0;
//        double rebateInterest = 0;
//        double waiveOffInterest = 0;
//        boolean isTLAvailable = false;
//        String penalWaiveOff = "";
//        String rebateAllowed = "";
//        long no_of_installment = 0;
//        String asAnWhenCustomer = "";
//        HashMap map = new HashMap();
//        System.out.println("dataMap " + dataMap);
//        HashMap ALL_LOAN_AMOUNT = new HashMap();
//        TransactionTO objCashTransactionTO = (TransactionTO) dataMap.get(CASH_TO);
//        System.out.println("objCashTransactionTO.getTransType()" + objCashTransactionTO.getTransType());
//        System.out.println("CommonConstants.CREDIT" + CommonConstants.CREDIT);
//        if (objCashTransactionTO != null && objCashTransactionTO.getTransType().equals("CASH")) {
//            if (dataMap.get("ACCT_NUM") != null) {//&& CommonUtil.convertObjToDouble(dataMap.get("ACCT_NUM")).length()>0
//                map.put(CommonConstants.ACT_NUM, dataMap.get("ACCT_NUM"));
//                ALL_LOAN_AMOUNT = (HashMap) dataMap.get("ALL_AMOUNT");
//                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")) > 0) {
//                    rebateInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue();
//                }
//                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("WAIVE_OFF_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST")) > 0) {
//                    waiveOffInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST"));
//                }
//                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("PENAL_WAIVE_OFF") && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("PENAL_WAIVE_OFF")).equals("Y")) {
//                    penalWaiveOff = "Y";
//                } else {
//                    penalWaiveOff = "N";
//                }
//                if (dataMap.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToStr(dataMap.get("REBATE_INTEREST")).equals("Y")) {
//                    rebateAllowed = "Y";
//                } else {
//                    rebateAllowed = "N";
//                }
//                map.put("TRANS_DT", currDt);
//                map.put("INITIATED_BRANCH", dataMap.get("SELECTED_BRANCH_ID"));
//                List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
//                if (lst == null || lst.isEmpty()) {
//                    lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
//                }
//                if (lst != null && lst.size() > 0) {
//                    map = (HashMap) lst.get(0);
//                    System.out.println("map " + map);
//                    asAnWhenCustomer = CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES"));
//                    map.put(CommonConstants.ACT_NUM, dataMap.get("ACCT_NUM"));
//                    map.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
//                    map.put("PROD_ID", dataMap.get("PROD_ID"));
//                }
//                lst = null;
//            }
//        }
//        if (asAnWhenCustomer != null && asAnWhenCustomer.length() > 0 && asAnWhenCustomer.equals("Y")) {// && actionType==ClientConstants.ACTIONTYPE_NEW){
//            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
//                HashMap whereMap = new HashMap();
//                whereMap.put(CommonConstants.MAP_WHERE, objCashTransactionTO.getTransId());
//                List list = sqlMap.executeQueryForList("getCashTransactionTOForAuthorzationTransId", whereMap.get(CommonConstants.MAP_WHERE));
//                if (list != null && list.size() > 0) {
//                    cashList = new ArrayList();
//                    for (int i = 0; i < list.size(); i++) {
//                        objCashTransactionTO = (TransactionTO) list.get(i);
//                        objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                        cashList.add(objCashTransactionTO);
//                        if (i == 0) {
//                            deleteRebateInterestTransaction(dataMap.get("ACCT_NUM").toString(), CommonConstants.TOSTATUS_DELETE);
//                        }
//                    }
//                }
//                System.out.println("cashListdelete " + cashList);
//                whereMap = null;
//                list = null;
//                return cashList;
//            }
//            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
//                List lstachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", map);
//                if (lstachd != null && lstachd.size() > 0) {
//                    map = (HashMap) lstachd.get(0);
//                }
//                transAmt = objCashTransactionTO.getTransAmt().doubleValue();
//                if (ALL_LOAN_AMOUNT.containsKey("NO_OF_INSTALLMENT") && ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT") != null) {
//                    no_of_installment = CommonUtil.convertObjToLong(ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT"));
//                }
//                List appList = sqlMap.executeQueryForList("selectAppropriatTransaction", map.get("PROD_ID"));
//                HashMap appropriateMap = new HashMap();
//                if (appList != null && appList.size() > 0) {
//                    appropriateMap = (HashMap) appList.get(0);
//                    appropriateMap.remove("PROD_ID");
//                } else {
//                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
//                }
//                System.out.println("appropriateMap" + appropriateMap);
//                java.util.Collection collectedValues = appropriateMap.values();
//                java.util.Iterator it = collectedValues.iterator();
//                CashTransactionTO objCashTO = new CashTransactionTO();
//                int appTranValue = 0;
//                while (it.hasNext()) {
//                    appTranValue++;
//                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
//                    System.out.println("hierachyValue" + hierachyValue);
//                    objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                    if (hierachyValue.equals("CHARGES")) {
//                        if (ALL_LOAN_AMOUNT.containsKey("ACT_CLOSING_CHARGES")) {
//                            List chargeLst = (List) ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGES");
//                            if (chargeLst != null && chargeLst.size() > 0) {
//                                System.out.println("chargeLst  : " + chargeLst);
//                                for (int i = 0; i < chargeLst.size(); i++) {
//                                    objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                                    HashMap chargeMap = new HashMap();
//                                    String accHead = "";
//                                    double chargeAmt = 0;
//                                    chargeMap = (HashMap) chargeLst.get(i);
//                                    accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
//                                    chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
//                                    System.out.println("accHead" + accHead);
//                                    System.out.println("chargeAmt" + chargeAmt);
//                                    if (transAmt > 0 && chargeAmt > 0) {
//                                        if (transAmt >= chargeAmt) {
//                                            transAmt -= chargeAmt;
//                                            paidInterest = chargeAmt;
//                                        } else {
//                                            paidInterest = transAmt;
//                                            transAmt -= chargeAmt;
//                                        }
//                                        objCashTO.setAmount(new Double(paidInterest));
//                                        objCashTO.setActNum("");
//                                        objCashTO.setProdId("");
//                                        objCashTO.setProdType(TransactionFactory.GL);
//                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(accHead));
//                                        objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                                        //objCashTO.setLinkBatchId(objTO.getTransallId());
//                                        objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                                        objCashTO.setAuthorizeRemarks("ACT CLOSING CHARGE");
//                                        objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "ACT CLOSING CHARGE");
//                                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                                        objCashTO.setNarration("");
//                                        objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_CHARGE");
//                                        cashList.add(objCashTO);
//                                    }
//                                    paidInterest = 0;
//                                    chargeAmt = 0;
//                                }
//                            }
//                        }
//                        //account clsoing misc charges
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double accountClosingMisc = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_MISC_CHARGE"));
//                        if (transAmt > 0 && accountClosingMisc > 0) {
//                            if (transAmt >= accountClosingMisc) {
//                                transAmt -= accountClosingMisc;
//                                paidInterest = accountClosingMisc;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= accountClosingMisc;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("ACT CLOSING MISC CHARGE");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "ACT CLOSING MISC CHARGE");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_MISC_CHARGE");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //postage charges
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double postageCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("POSTAGE CHARGES"));
//                        if (transAmt > 0 && postageCharges > 0) {
//                            if (transAmt >= postageCharges) {
//                                transAmt -= postageCharges;
//                                paidInterest = postageCharges;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= postageCharges;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("POSTAGE_CHARGES")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("POSTAGE CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "POSTAGE_CHARGES");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_POSTAGE_CHARGES");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //arbitary charges
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double orbitaryCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARBITRARY CHARGES"));
//                        if (transAmt > 0 && orbitaryCharges > 0) {
//                            if (transAmt >= orbitaryCharges) {
//                                transAmt -= orbitaryCharges;
//                                paidInterest = orbitaryCharges;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= orbitaryCharges;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ARBITRARY_CHARGES")));//MISC_SERV_CHRG
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("ARBITRARY CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "ARBITRARY CHARGES");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_ARBITRARY_CHARGES");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //legal charges
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double legalCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LEGAL CHARGES"));
//                        if (transAmt > 0 && legalCharges > 0) {
//                            if (transAmt >= legalCharges) {
//                                transAmt -= legalCharges;
//                                paidInterest = legalCharges;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= legalCharges;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("LEGAL_CHARGES")));//NOTICE_CHARGES
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("LEGAL CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "LEGAL CHARGES");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_LEGAL_CHARGES");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //insurance charges
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double insuranceCharge = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES"));
//                        if (transAmt > 0 && insuranceCharge > 0) {
//                            if (transAmt >= insuranceCharge) {
//                                transAmt -= insuranceCharge;
//                                paidInterest = insuranceCharge;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= insuranceCharge;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("INSURANCE_CHARGES")));//MISC_SERV_CHRG
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("INSURANCE CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "INSURANCE CHARGES");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_INSURANCE_CHARGES");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //missleneous
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double miscellous = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MISCELLANEOUS CHARGES"));
//                        if (transAmt > 0 && miscellous > 0) {
//                            if (transAmt >= miscellous) {
//                                transAmt -= miscellous;
//                                paidInterest = miscellous;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= miscellous;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "MISCELLANEOUS CHARGES");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_MISC_SERV_CHRG");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //execution degree
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double executionDegree = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EXECUTION DECREE CHARGES"));
//                        if (transAmt > 0 && executionDegree > 0) {
//                            if (transAmt >= executionDegree) {
//                                transAmt -= executionDegree;
//                                paidInterest = executionDegree;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= executionDegree;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("EXECUTION_DECREE_CHARGES")));//MISC_SERV_CHRG
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("EXECUTION DECREE CHARGES");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "EXECUTION DECREE CHARGES");
//                            objCashTO.setLoanHierarchy("4"); // "4" For Charges
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_EXECUTION_DECREE_CHARGES");
//                            cashList.add(objCashTO);
//                        }
//                        paidInterest = 0;
//                        //CASE DETAILS Changed By Suresh
//                        if (ALL_LOAN_AMOUNT.containsKey("OTHER_CHARGES")) {
//                            if (otherChargesMap == null) {
//                                otherChargesMap = new HashMap();
//                            }
//                            otherChargesMap = (HashMap) ALL_LOAN_AMOUNT.get("OTHER_CHARGES");
//                            System.out.println("otherChargesMap : " + otherChargesMap);
//                            Object keys[] = otherChargesMap.keySet().toArray();
//                            for (int i = 0; i < otherChargesMap.size(); i++) {
//                                objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                                double otherCharge = CommonUtil.convertObjToDouble(otherChargesMap.get(keys[i]));
//                                System.out.println("otherCharge : " + otherCharge + " : " + keys[i]);
//                                if (transAmt > 0 && otherCharge > 0) {
//                                    if (transAmt >= otherCharge) {
//                                        transAmt -= otherCharge;
//                                        paidInterest = otherCharge;
//                                    } else {
//                                        paidInterest = transAmt;
//                                        transAmt -= otherCharge;
//                                    }
//                                    objCashTO.setActNum(dataMap.get("ACCT_NUM").toString());
//                                    objCashTO.setInpAmount(new Double(paidInterest));
//                                    objCashTO.setAmount(new Double(paidInterest));
//                                    objCashTO.setActNum("");
//                                    objCashTO.setProdId("");
//                                    objCashTO.setProdType(TransactionFactory.GL);
//                                    if (CommonUtil.convertObjToStr(keys[i]).equals("NOTICE CHARGES")) {
//                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES"))));
//                                    } else {
//                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(keys[i])));
//                                    }
//                                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                                    //objCashTO.setLinkBatchId(objTO.getTransallId());
//                                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                                    objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(keys[i]));
//                                    objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + keys[i]);
//                                    objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
//                                    objCashTO.setNarration("");
//                                    System.out.println("objCashTO : " + objCashTO);
//                                    objCashTO.setInstrumentNo2("LOAN_OTHER_CHARGES");
//                                    cashList.add(objCashTO);
//                                }
//                                paidInterest = 0;
//                                otherCharge = 0;
//                            }
//                            continue;
//                        }
//
//                    }
//                    if (hierachyValue.equals("PENALINTEREST")) {
//                        //penal interest
//                        double penalInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT"));
//                        if (penalWaiveOff.equals("Y") || waiveOffInterest > 0) {
//                            // penalWaiveOff(objCashTransactionTO,penalInterest,waiveOffInterest);
//                            continue;
//                        }
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
////  double penalInterest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
//                        if (transAmt > 0 && penalInterest > 0) {
//                            if (transAmt >= penalInterest) {
//                                transAmt -= penalInterest;
//                                paidInterest = penalInterest;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= penalInterest;
//
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("PENAL_INT")));
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("PENAL_INT");
//                            objCashTO.setParticulars(dataMap.get("ACCT_NUM") + " : " + "PENAL_INT");
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "3" For Penal Interest
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_PENAL_INT");
//                            cashList.add(objCashTO);
//                            continue;
//                        }
//                    }
//                    if (hierachyValue.equals("INTEREST")) {
//                        //interest
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        double interest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT"));
//                        if (rebateInterest > 0 && interest > 0) {
//                            if (rebateAllowed.equals("Y")) {
//                                if (interest >= rebateInterest) {
//                                    interest -= rebateInterest;
//                                } else {
//                                    interest = 0;
//                                }
//                            }
//                        }
//                        if (waiveOffInterest > 0 && interest > 0) {
//                            if (interest >= waiveOffInterest) {
//                                interest -= waiveOffInterest;
//                            }
//                        }
//                        if (transAmt > 0 && interest > 0) {
//                            if (transAmt >= interest) {
//                                transAmt -= interest;
//                                paidInterest = interest;
//                            } else {
//                                paidInterest = transAmt;
//                                transAmt -= interest;
//                            }
//                            objCashTO.setAmount(new Double(paidInterest));
//                            objCashTO.setActNum("");
//                            objCashTO.setProdId("");
//                            objCashTO.setProdType(TransactionFactory.GL);
//                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks("INTEREST");
//                            objCashTO.setParticulars("By Interest : "+CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Penal Interest
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_INTEREST");
//                            cashList.add(objCashTO);
//                            continue;
//                        }
//                    }
//                    if (hierachyValue.equals("PRINCIPAL")) {
//                        double principal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_PRINCEPLE"));
//                        //principal
//                        if (transAmt > 0.0 && principal > 0) {
//                            if (transAmt >= principal) {
//                                transAmt -= principal;
//                                paidprincipal = principal;
//                            } else {
//                                paidprincipal = transAmt;
//                                transAmt -= principal;
//                            }
//                            objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                            objCashTO.setAmount(new Double(paidprincipal));
//                            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            //objCashTO.setLinkBatchId(objTO.getTransallId());
//                            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                            objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
//                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
//                            objCashTO.setNarration("");
//                            objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
//                            cashList.add(objCashTO);
//                            continue;
//                        }
//                    }
//                }
//                if (transAmt > 0.0) {
//                    for (int i = 0; i < cashList.size(); i++) {
//                        CashTransactionTO obj = (CashTransactionTO) cashList.get(i);
//                        if (obj.getProdType().equals("TL")) {
//                            double principal = obj.getAmount().doubleValue();
//                            principal += transAmt;
//                            obj.setAmount(new Double(principal));
//                            cashList.set(i, obj);
//                            isTLAvailable = true;
//                            break;
//                        }
//                    }
//                    if (!isTLAvailable) {
//                        objCashTO = setCashTransaction(objCashTransactionTO, dataMap);
//                        objCashTO.setAmount(new Double(transAmt));
//                        objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                        //objCashTO.setLinkBatchId(objTO.getTransallId());
//                        objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//                        objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
//                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
//                        objCashTO.setNarration("");
//                        objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
//                        cashList.add(objCashTO);
//                    }
//                }
//                lstachd = null;
//                objCashTO = null;
//            }
//        }
//        map = null;
//        ALL_LOAN_AMOUNT = null;
//        objCashTransactionTO = null;
//        System.out.println("cashlist" + cashList);
//        return cashList;
//    }
    private void interestWaiveoffTransaction(CashTransactionTO cashTO, double waivePenal, double waiveInterest) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        int count = 0;
        if (waivePenal > 0) {
            transMap.put("WAIVE_PENAL", new Double(waivePenal));
            count++;
        }
        if (waiveInterest > 0) {
            transMap.put("WAIVE_INTEREST", new Double(waiveInterest));
            count++;
        }
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double penal = 0;
            double interest = 0, transAmt = 0;
            for (int i = 0; i < count; i++) {
                penal = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PENAL"));
                if (penal == 0) {
                    interest = CommonUtil.convertObjToDouble(transMap.get("WAIVE_INTEREST"));
                    transAmt = interest;
                } else {
                    transAmt = penal;
                }
                txMap = new HashMap();
                transferList = new ArrayList(); // for local transfer
                trans = new TransferTrans();
                trans.setTransMode(CommonConstants.TX_TRANSFER);
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_DISCOUNT_ACHD"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                if (penal > 0) {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal for " + cashTO.getActNum());
                    txMap.put("AUTHORIZEREMARKS", "PENAL_WAIVEOFF");
                    txMap.put("DR_INST_TYPE", "PENAL_WAIVEOFF");
                    txMap.put("DR_INSTRUMENT_2", "PENAL_WAIVEOFF");
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest for " + cashTO.getActNum());
                    txMap.put("AUTHORIZEREMARKS", "INTEREST_WAIVEOFF");
                    txMap.put("DR_INST_TYPE", "INTEREST_WAIVEOFF");
                    txMap.put("DR_INSTRUMENT_2", "INTEREST_WAIVEOFF");
                }
                transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                if (penal > 0) {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Penal upto" + CommonUtil.convertObjToStr(properFormatDate));
                } else {
                    txMap.put(TransferTrans.PARTICULARS, "WaiveOff Interest upto" + CommonUtil.convertObjToStr(properFormatDate));
                }
                System.out.println(" insertAccHead txMap " + txMap);
                trans.setInitiatedBranch(_branchCode);
                trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
                //trans.setLinkBatchId(objTO.getTransallId());
                //trans.setGlTransActNum(CommonUtil.convertObjToStr(cashTO.getActNum()));
                trans.setBreakLoanHierachy("Y");
                transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
                trans.doDebitCredit(transferList, _branchCode, false);
                transMap.remove("WAIVE_PENAL");
            }
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
    }

    private void penalWaiveOff(CashTransactionTO obj, double waivePenalAmt, double waiveInterestAmt, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objpenalWaive = new TermLoanPenalWaiveOffTO();
        interestWaiveoffTransaction(obj, waivePenalAmt, waiveInterestAmt);
        if (obj != null) {
            objpenalWaive.setAcctNum(obj.getLinkBatchId());
            objpenalWaive.setWaiveDt((Date) currDt.clone());
            objpenalWaive.setInterestAmt(new Double(waiveInterestAmt));
            objpenalWaive.setPenalAmt(new Double(waivePenalAmt));
            objpenalWaive.setStatus(CommonConstants.STATUS_CREATED);
            objpenalWaive.setStatusBy(obj.getStatusBy());
            objpenalWaive.setStatusDt((Date) currDt.clone());
            objpenalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            sqlMap.executeUpdate("insertTermLoanInterestWaiveOffTO", objpenalWaive);
        }
    }

    private String generateWaiveOffBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOAN.WAIVE_OFF_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private void rebateInterestTransaction(CashTransactionTO cashTO, double rebateInterest, double interest) throws Exception {
        LoanRebateTO rebateTo = new LoanRebateTO();
        HashMap dataMap = new HashMap();
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            HashMap txMap = new HashMap();
            txMap = new HashMap();
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getLinkBatchId()));
            trans.setInitiatedBranch(_branchCode);
            txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("REBATE_INTEREST_ACHD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest  " + cashTO.getActNum());
            txMap.put("AUTHORIZEREMARKS", "REBATE_INTEREST");
            txMap.put("DR_INST_TYPE", "REBATE_INTEREST");
            txMap.put("DR_INSTRUMENT_2", "REBATE_INTEREST");
            transferList.add(trans.getDebitTransferTO(txMap, rebateInterest));

            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, "Rebate interest upto " + CommonUtil.convertObjToStr(currDt));
            if (interest > 0) {
                transferList.add(trans.getCreditTransferTO(txMap, interest));
            } else {
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest));
            }
            if (interest > 0) {
                txMap.put(TransferTrans.CR_ACT_NUM, cashTO.getActNum());
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_ID, cashTO.getProdId());
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put("DR_INST_TYPE", "REBATE_PRINCIPAL");
                txMap.put("DR_INSTRUMENT_2", "REBATE_PRINCIPAL");
                txMap.put("AUTHORIZEREMARKS", "REBATE_PRINCIPAL");
                txMap.put(TransferTrans.PARTICULARS, "Rebate interest paid to principal upto " + CommonUtil.convertObjToStr(currDt));
                transferList.add(trans.getCreditTransferTO(txMap, rebateInterest - interest));
            }
            System.out.println(" insertAccHead txMap " + txMap);
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
            trans.setBreakLoanHierachy("Y");
            trans.doDebitCredit(transferList, _branchCode, false);
            rebateTo.setAccNo(CommonUtil.convertObjToStr(cashTO.getActNum()));
            rebateTo.setBranchCode(_branchCode);
            rebateTo.setIntAmount(String.valueOf(rebateInterest));
            rebateTo.setStatus(CommonConstants.STATUS_CREATED);
            rebateTo.setStatusDt((Date) currDt.clone());
            rebateTo.setStatusBy("0001");
            rebateTo.setRebateId(generateRebateBatchID());
            sqlMap.executeUpdate("insertLoanRebate", rebateTo);
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
    }

    private String generateRebateBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "REBATE_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private void deleteRebateInterestTransaction(String linkBatchId, String status) throws Exception {
        TransactionDAO objTransactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap cashAuthMap = new HashMap();
        HashMap interestMap = new HashMap();
        ArrayList totList = new ArrayList();
        interestMap.put("ACT_NUM", linkBatchId);
        TransferTrans trans = new TransferTrans();
        trans.setTransMode(CommonConstants.TX_TRANSFER);
        trans.setInitiatedBranch(_branchCode);
        trans.setLinkBatchId(linkBatchId);
        trans.setInitiatedBranch(_branchCode);
        List list = sqlMap.executeQueryForList("getBehavesLikeTLAD", interestMap);
        if (list != null && list.size() > 0) {
            cashAuthMap.put("INITIATED_BRANCH", _branchCode);
            cashAuthMap.put("LINK_BATCH_ID", linkBatchId);
            cashAuthMap.put("TODAY_DT", currDt);
            List lst = sqlMap.executeQueryForList("getTransferTransdistinctBatchID", cashAuthMap);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    HashMap transMap = (HashMap) lst.get(i);
                    List txList = sqlMap.executeQueryForList("getBatchTxTransferTOs", transMap);
                    for (int j = 0; j < txList.size(); j++) {
                        TxTransferTO txTransTO = (TxTransferTO) txList.get(j);
                        txTransTO.setCommand(status);
                        txTransTO.setStatus("DELETED");
                        totList.add(txTransTO);
                    }
                }
            }
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            trans.setInitiatedBranch(_branchCode);
            if (totList != null && totList.size() > 0) {
                trans.doDebitCredit(totList, _branchCode, false);
            }
        }
    }

    public CashTransactionTO setCashTransaction(TransactionTO cashTo, HashMap dataMap) {
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setTransId(cashTo.getTransId());
            objCashTransactionTO.setAcHdId(dataMap.get("ACCT_HEAD").toString());
            objCashTransactionTO.setProdId(dataMap.get("PROD_ID").toString());
            objCashTransactionTO.setProdType("TL");
            objCashTransactionTO.setActNum(dataMap.get("ACCT_NUM").toString());
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTransactionTO.setInpCurr("");
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTransactionTO.setTransType("CASH");
            objCashTransactionTO.setInstType("");
            objCashTransactionTO.setBranchId(dataMap.get("BRANCH_CODE").toString());
            objCashTransactionTO.setStatusBy(cashTo.getStatus());
            objCashTransactionTO.setInstrumentNo1("");
            objCashTransactionTO.setInstrumentNo2("");
            Date InsDt = currDt;
            if (InsDt != null) {
                Date insDate = (Date) curDate.clone();
                insDate.setDate(InsDt.getDate());
                insDate.setMonth(InsDt.getMonth());
                insDate.setYear(InsDt.getYear());
                objCashTransactionTO.setInstDt(insDate);
            } else {
                objCashTransactionTO.setInstDt(currDt);
            }
            objCashTransactionTO.setTokenNo("");
            objCashTransactionTO.setInitTransId(cashTo.getTransId());
            objCashTransactionTO.setInitChannType("");
            objCashTransactionTO.setParticulars("");
            objCashTransactionTO.setInitiatedBranch(dataMap.get("BRANCH_CODE").toString());
            objCashTransactionTO.setCommand(cashTo.getCommand());
            objCashTransactionTO.setAuthorizeStatus_2("");
            System.out.println("objCashTransactionTO : " + objCashTransactionTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private ArrayList loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setParticulars("To Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("0");
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(ServerUtil.getCurrentDate(dataMap.get("BRANCH_CODE").toString()));
            objCashTO.setAuthorizeStatus_2("");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            //objCashTO.setLinkBatchId(objTO.getTransallId());
            objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            System.out.println("objCashTO 1st one : " + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }

    private void transactionPartGL(HashMap GLMap, HashMap map) throws Exception {
        try {
            System.out.println("transactionPartGL GLMap : " + GLMap);
            HashMap txMap = new HashMap();
            HashMap GLDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();
            double recoveredAmount = 0;
            recoveredAmount = CommonUtil.convertObjToDouble(GLMap.get("RECOVERED_AMOUNT"));
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //transferTo.setGlTransActNum(CommonUtil.convertObjToStr(GLMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //transferTo.setGlTransActNum(CommonUtil.convertObjToStr(GLMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //transferTo.setGlTransActNum(CommonUtil.convertObjToStr(GLMap.get("ACT_NUM")));
                        }
                    }
                    //DEBIT
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.GL) && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferTo.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        transferTo.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        transferTo.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    TxTransferTO.add(transferTo);
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_AC_HD, GLMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                    transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    TxTransferTO.add(transferTo);

                    transferDAO = new TransferDAO();
                    GLDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    GLDataMap.put("COMMAND", GLDataMap.get("MODE"));
                    GLDataMap.put("TxTransferTO", TxTransferTO);
                    GLDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    HashMap transMap = transferDAO.execute(GLDataMap, false);
                    GLMap = null;
                    GLDataMap = null;
                    txMap = null;
                } else if (transactionTO.getTransType().equals("CASH")) {
                    double transAmt;
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    HashMap cashTransMap = new HashMap();
                    HashMap acHeadMap = new HashMap();
                    cashTransMap.put("SELECTED_BRANCH_ID", GLMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_NUM", GLMap.get("ACT_NUM"));
                    cashTransMap.put("PROD_ID", GLMap.get("PROD_ID"));
                    cashTransMap.put("BRANCH_CODE", GLMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_HEAD", GLMap.get("AC_HEAD"));
                    cashTransMap.put("TOKEN_NO", "");
                    cashTransMap.put("LIMIT", recoveredAmount);
                    cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                    cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    cashTransMap.put("USER_ID", GLMap.get("USER_ID"));
                    ArrayList cashList = new ArrayList();
                    GLMap.put("GL_CASH_TRANSACTION", "GL_CASH_TRANSACTION");
                    cashList = setCashTransactionValue(GLMap, acHeadMap, cashTransMap);
                    cashTransMap = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartDeposits(HashMap depositMap, HashMap map) throws Exception {
        try {
            System.out.println("transactionPartDeposits depositMap : " + depositMap);
            HashMap txMap = new HashMap();
            HashMap depositDataMap = new HashMap();
            ArrayList TxTransferTO = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            TransactionTO transactionTO = new TransactionTO();

            double penalAmount = 0;
            double recoveredAmount = 0;
            double principal = 0.0;
            recoveredAmount = CommonUtil.convertObjToDouble(depositMap.get("RECOVERED_AMOUNT"));
            principal = CommonUtil.convertObjToDouble(depositMap.get("PRINCIPAL"));
            penalAmount = CommonUtil.convertObjToDouble(depositMap.get("PENAL"));
            System.out.println("recoveredAmount : " + recoveredAmount + " penalAmount : " + penalAmount + " principal    :   " + principal);
            if (map.containsKey("TransactionTO") && recoveredAmount > 0) {
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                HashMap acHeadMap = new HashMap();
                acHeadMap.put("PROD_ID", depositMap.get("PROD_ID"));
                //List lst = null;//sqlMap.executeQueryForList("getAccountHeadProdTD", acHeadMap);
//                if (lst != null && lst.size() > 0) {
//                    acHeadMap = (HashMap) lst.get(0);
//                }
                acHeadMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", acHeadMap);
                System.out.println("insertAccHead acHeads " + acHeadMap);
                if (transactionTO.getTransType().equals("TRANSFER")) {
                    HashMap debitMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        debitMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                        List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                        }
                    }
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                        }
                    }
                    System.out.println("transactionTO.getProductType() == " + transactionTO.getProductType());
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        //System.out.println("entered inside SA ");
                        debitMap.put("prodId", transactionTO.getProductId());
                        List lst = sqlMap.executeQueryForList("getAccountHeadProdSAHead", debitMap);
                        if (lst != null && lst.size() > 0) {
                            debitMap = (HashMap) lst.get(0);
                            //System.out.println("debitmap" + debitMap);
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                        }
                    }
                    //DEBIT
                    txMap = new HashMap();
                    if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.GL) && transactionTO.getProductId().equals("")) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        transferTo.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.OPERATIVE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        transferTo.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.SUSPENSE)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        transferTo.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    } else if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals(TransactionFactory.ADVANCES)) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                        txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
                        transferTo.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo());
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                    //transferTo.setTransModType(CommonConstants.SUSPENSE_TRANSMODE_TYPE);
                    TxTransferTO.add(transferTo);
                    //CREDIT
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.CR_PROD_TYPE, depositMap.get("PROD_TYPE"));
                    txMap.put(TransferTrans.CR_PROD_ID, depositMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("FIXED_DEPOSIT_ACHD"));
                    txMap.put(TransferTrans.CR_ACT_NUM, depositMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.PARTICULARS, "By Principal " + transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, recoveredAmount);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                    transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(depositMap.get("ACT_NUM")));
                    transferTo.setProdId(CommonUtil.convertObjToStr(depositMap.get("PROD_ID")));
                    transferTo.setTransModType(CommonConstants.DEPOSIT_TRANSMODE_TYPE);
                    TxTransferTO.add(transferTo);

                    transferDAO = new TransferDAO();
                    depositDataMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    depositDataMap.put("COMMAND", depositDataMap.get("MODE"));
                    depositDataMap.put("TxTransferTO", TxTransferTO);
                    depositDataMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                    depositDataMap.put("TRANS_ALL_ID", CommonUtil.convertObjToStr(objTO.getTransallId()));
                    if (penalAmount > 0) {
                        depositDataMap.put("DEPOSIT_PENAL_AMT", String.valueOf(penalAmount));
                        depositDataMap.put("DEPOSIT_PENAL_MONTH", CommonUtil.convertObjToDouble(depositMap.get("DEPOSIT_PENAL_MONTH")));
                    }
                    System.out.println(" Before Transfer Dao depositDataMap : " + depositDataMap);
                    HashMap transMap = transferDAO.execute(depositDataMap, false);
                    depositDataMap = null;
                    depositMap = null;
                    acHeadMap = null;
                    txMap = null;
                } else if (transactionTO.getTransType().equals("CASH")) {
                    double transAmt;
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    HashMap cashTransMap = new HashMap();
                    cashTransMap.put("SELECTED_BRANCH_ID", depositMap.get("BRANCH_CODE"));
                    cashTransMap.put("ACCT_NUM", depositMap.get("ACT_NUM"));
                    cashTransMap.put("PROD_ID", depositMap.get("PROD_ID"));
                    cashTransMap.put("BRANCH_CODE", _branchCode);
                    cashTransMap.put("ACCT_HEAD", depositMap.get("AC_HEAD"));
                    cashTransMap.put("TOKEN_NO", "");
                    cashTransMap.put("LIMIT", recoveredAmount);
                    cashTransMap.put("LOANDEBIT", "LOANCREDIT");
                    cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    cashTransMap.put("USER_ID", depositMap.get("USER_ID"));
                    ArrayList cashList = new ArrayList();
                    depositMap.put("DEPOSIT_CASH_TRANSACTION", "DEPOSIT_CASH_TRANSACTION");
                    cashList = setCashTransactionValue(depositMap, acHeadMap, cashTransMap);
                    cashTransMap = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void transactionPartMDS(HashMap chittalMap, HashMap map) throws Exception {
        try {
            System.out.println("transactionPartMDS chittalMap : " + chittalMap);
            HashMap MDSmap = new HashMap();
            HashMap bonusMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap chittalDetailMap = new HashMap();
            HashMap installmentMap = new HashMap();
            String actNo = "";
            String subNo = "";
            String chittalNo = "";
            long count = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            long pendingInst = 0;
            long noOfInstPay = 0;
            double totBonusAmt = 0;
            double totPenalAmt = 0;
            double totNoticeAmt = 0;
            double totDiscountAmt = 0;
            double totArbitrationAmt = 0;
            actNo = CommonUtil.convertObjToStr(chittalMap.get("ACT_NUM"));
            if (actNo.indexOf("_") != -1) {
                chittalNo = actNo.substring(0, actNo.indexOf("_"));
                subNo = actNo.substring(actNo.indexOf("_") + 1, actNo.length());
            }
            whereMap.put("CHITTAL_NO", chittalNo);
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List chittalList = (List) sqlMap.executeQueryForList("getMDSChittalDetails", whereMap);
            if (chittalList != null && chittalList.size() > 0) {
                chittalDetailMap = (HashMap) chittalList.get(0);
                whereMap.put("INT_CALC_UPTO_DT", CommonUtil.getProperDate(currDt, intUptoDt));
                List bonusList = sqlMap.executeQueryForList("getRecoveryListInstallmentMapDetails", whereMap);
                if (bonusList != null && bonusList.size() > 0) {
                    for (int i = 0; i < bonusList.size(); i++) {
                        HashMap instMap = new HashMap();
                        bonusMap = (HashMap) bonusList.get(i);
                        totBonusAmt += CommonUtil.convertObjToDouble(bonusMap.get("BONUS_AMT"));
                        totPenalAmt += CommonUtil.convertObjToDouble(bonusMap.get("PENAL_AMT"));
                        totDiscountAmt += CommonUtil.convertObjToDouble(bonusMap.get("DISCOUNT_AMT"));
                        if (i == 0) {
                            totNoticeAmt = CommonUtil.convertObjToDouble(bonusMap.get("NOTICE_AMT"));
                            totArbitrationAmt = CommonUtil.convertObjToDouble(bonusMap.get("ARBITRATION_AMT"));
                        }
                        instMap.put("BONUS", bonusMap.get("BONUS_AMT"));
                        instMap.put("DISCOUNT", bonusMap.get("DISCOUNT_AMT"));
                        instMap.put("PENAL", bonusMap.get("PENAL_AMT"));
                        instMap.put("INST_AMT", chittalDetailMap.get("INST_AMT"));
                        installmentMap.put(CommonUtil.convertObjToStr(bonusMap.get("INSTALLMENT_NO")), instMap);
                    }
                }
                System.out.println("bonusList" + bonusList);
                System.out.println("totPenalAmt" + totPenalAmt);
                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                if (insList != null && insList.size() > 0) {
                    whereMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                }
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToStr(chittalDetailMap.get("DIVISION_NO")));
                insDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                insDateMap.put("CURR_DATE", CommonUtil.getProperDate(currDt, intUptoDt));
                insDateMap.put("ADD_MONTHS", "-1");
                List insDateLst = sqlMap.executeQueryForList("getTransAllMDSCurrentInsDate", insDateMap);
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                    pendingInst = curInsNo - noOfInsPaid;
                    if (pendingInst < 0) {
                        pendingInst = 0;
                    }
                    if (pendingInst > 0) {
                        noOfInstPay = pendingInst;
                    }
                    System.out.println(" NoOfInstToPay  : " + noOfInstPay);
                }
                //Narration
                int insDay = 0;
                Date paidUpToDate = null;
                HashMap instDateMap = new HashMap();
                instDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                instDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                List insLst = sqlMap.executeQueryForList("getSelectInstUptoPaid", instDateMap);
                if (insLst != null && insLst.size() > 0) {
                    instDateMap = (HashMap) insLst.get(0);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                } else {
                    Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_START_DT")));
                    insDay = CommonUtil.convertObjToInt(chittalDetailMap.get("INSTALLMENT_DAY"));
                    startedDate.setDate(insDay);
                    int stMonth = startedDate.getMonth();
                    startedDate.setMonth(stMonth + (int) count - 1);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                }
                String narration = "";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(noOfInstPay));
                if (noInstPay == 1) {
                    narration = "Inst" + (noOfInsPaid + 1);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                } else if (noInstPay > 1) {
                    narration = "Inst" + (noOfInsPaid + 1);
                    narration += "-" + (noOfInsPaid + noInstPay);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                    dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                    narration += " To " + sdf.format(dt);
                }
                //System.out.println(" narration : " + narration);
                //SET RECEIPT_ENTRY_TO
                mdsReceiptEntryTO = new MDSReceiptEntryTO();
                if (chittalMap.containsKey("SPLIT_DETAILS")) {
                    ArrayList mdsList = new ArrayList();
                    mdsList = (ArrayList) chittalMap.get("SPLIT_DETAILS");
                    double totdemand = 0.0;
                    double princ = 0.0;
                    double intere = 0.0;
                    double penal = 0.0;
                    double bonus = 0.0;
                    double not = 0.0;
                    double arbit = 0.0;
                    for (int k = 0; k < mdsList.size(); k++) {
                        List aList = (List) mdsList.get(k);
                        princ = princ + CommonUtil.convertObjToDouble(aList.get(3));
                        intere = intere + CommonUtil.convertObjToDouble(aList.get(4));
                        penal = penal + CommonUtil.convertObjToDouble(aList.get(5));
                        bonus = bonus + CommonUtil.convertObjToDouble(aList.get(6));
                        not = not + CommonUtil.convertObjToDouble(aList.get(8));
                        arbit = arbit + CommonUtil.convertObjToDouble(aList.get(7));
                    }
                    totdemand = princ + intere + not + arbit;
                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(chittalMap.get("TOT_GRAND")));
                    mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(String.valueOf(mdsList.size())));
                    mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt((String.valueOf(mdsList.size()))));
                    mdsReceiptEntryTO.setInstAmtPayable(princ);
                    mdsReceiptEntryTO.setBonusAmtPayable(bonus);
                } else {
                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(chittalMap.get("TOTAL_DEMAND")));
                    mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS")));
                    mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(chittalMap.get("TOTAL")));
                    mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                    mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("BONUS")));
                }
                mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                mdsReceiptEntryTO.setChittalNo(chittalNo);
                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(subNo));
                mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(chittalDetailMap.get("MEMBER_NAME")));
                mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(chittalDetailMap.get("DIVISION_NO")));
                mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_START_DT"))));
                mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_END_DT"))));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chittalMap.get("TOT_INT"))));
                mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(String.valueOf(totNoticeAmt)));
                mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(String.valueOf(totArbitrationAmt)));
                mdsReceiptEntryTO.setNarration(CommonUtil.convertObjToStr(chittalDetailMap.get("NARRATION")));
                mdsReceiptEntryTO.setBankPay("N");
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setStatusDt(currDt);
                mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(chittalDetailMap.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(chittalMap.get("PRIZED_MEMBER")));
                mdsReceiptEntryTO.setPaidDate(currDt);
                mdsReceiptEntryTO.setChangedDt(currDt);//mdsReceiptEntryTO.set
                mdsReceiptEntryTO.setThalayal("N");
                mdsReceiptEntryTO.setMunnal("N");
                mdsReceiptEntryTO.setMemberChanged("N");
                mdsReceiptEntryTO.setCallingScreen("OTHER_TRANSACTIONS");
                mdsReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(chittalMap.get("BONUS_AVAL")));//PROBLEM
                mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                mdsReceiptEntryTO.setChangedInstNo(CommonUtil.convertObjToDouble(String.valueOf("0")));
                //System.out.println(" mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                //System.out.println(" installmentMap : " + installmentMap);

                MDSmap.put("INSTALLMENT_MAP", installmentMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", chittalDetailMap);
                if (closureList != null && closureList.size() > 0) {
                    MDSmap.put("MDS_CLOSURE", "MDS_CLOSURE");
                } else {
                    MDSmap.remove("MDS_CLOSURE");
                }
                MDSmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                MDSmap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                MDSmap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                MDSmap.put("TransactionTO", map.get("TransactionTO"));
                MDSmap.put("INT_CALC_UPTO_DT", intUptoDt);
                MDSmap.put("FROM_RECOVERY_TALLY", "FROM_RECOVERY_TALLY");
                MDSmap.put("TRANS_ALL_ID", objTO.getTransallId());
                MDSmap.put("LINK_BATCH_ID", objTO.getTransallId());
                int val = noOfInsPaid + CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInst());
                MDSmap.put("FROM_PARTICULARS_TRANSALL", "Chittal No : -" + chittalNo + "_" + subNo + " " + noOfInsPaid + " to " + val + "Installments");
                System.out.println(" MDSmap : " + MDSmap);
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transMap = new HashMap();
                transMap = mdsReceiptEntryDAO.execute(MDSmap, false);   // INSERT_TRANSACTION
                System.out.println(" transMap : " + transMap);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();

        }
    }

    public ArrayList setCashTransactionValue(HashMap txnMap, HashMap accountHeadMap, HashMap transMap) {
        ArrayList cashTransactionList = new ArrayList();
        try {
            System.out.println("setCashTransactionValue txnMap : " + txnMap + " accountHeadMap : " + accountHeadMap);
            if (txnMap.containsKey("LOAN_CASH_TRANSACTION")) {
                double penalAmount = CommonUtil.convertObjToDouble(txnMap.get("PENAL"));
                double chargesAmount = CommonUtil.convertObjToDouble(txnMap.get("CHARGES"));
                double interestAmount = CommonUtil.convertObjToDouble(txnMap.get("INTEREST"));
                double principalAmount = CommonUtil.convertObjToDouble(txnMap.get("PRINCIPAL"));
                double recoveredAmount = CommonUtil.convertObjToDouble(txnMap.get("RECOVERED_AMOUNT"));
                if (penalAmount > 0) {
                    cashTransactionList = new ArrayList();
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("PENAL_INT")));
                    objCashTO.setProdType(TransactionFactory.GL);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Penal : " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(penalAmount);
                    objCashTO.setAmount(penalAmount);
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    objCashTO.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    objCashTO.setAuthorizeRemarks("PENAL_INT");
                    cashTransactionList.add(objCashTO);
                    System.out.println("LOAN_CASH_TRANSACTION objCashTO Penal : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
                if (interestAmount > 0) {
                    cashTransactionList = new ArrayList();
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("PENAL_INT")));
                    objCashTO.setProdType(TransactionFactory.GL);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Interest : " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(interestAmount);
                    objCashTO.setAmount(interestAmount);
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    objCashTO.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    objCashTO.setAuthorizeRemarks("INTEREST");
                    cashTransactionList.add(objCashTO);
                    System.out.println("LOAN_CASH_TRANSACTION objCashTO Interest : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
                if (principalAmount > 0) {
                    cashTransactionList = new ArrayList();
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("ACCT_HEAD")));
                    objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    if (txnMap.containsKey("PROD_TYPE") && CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")).equals(TransactionFactory.LOANS)) {
                        objCashTO.setProdType(TransactionFactory.LOANS);
                    } else {
                        objCashTO.setProdType(TransactionFactory.ADVANCES);
                    }
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Principle : " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setInpAmount(principalAmount);
                    objCashTO.setAmount(principalAmount);
                    objCashTO.setActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    if (txnMap.containsKey("PROD_TYPE") && CommonUtil.convertObjToStr(txnMap.get("PROD_TYPE")).equals(TransactionFactory.LOANS)) {
                        objCashTO.setTransModType(CommonConstants.LOAN_TRANSMODE_TYPE);
                    } else {
                        objCashTO.setTransModType(CommonConstants.ADVANCE_TRANSMODE_TYPE);
                    }
                    objCashTO.setAuthorizeRemarks("0");
                    cashTransactionList.add(objCashTO);
                    System.out.println("LOAN_CASH_TRANSACTION objCashTO Principal : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
                // if(recoveredAmount>0){
                //  CashTransactionTO objCashTO = new CashTransactionTO();
                //  objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("AC_HEAD")));
                //  objCashTO.setProdType(TransactionFactory.GL);
                //  objCashTO.setTransType(CommonConstants.CREDIT);
                //  objCashTO.setInitTransId(logTO.getUserId());
                //  objCashTO.setBranchId(CommonUtil.convertObjToStr(loanMap.get("BRANCH_CODE")));
                //  objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                //  objCashTO.setStatusBy(logTO.getUserId());
                //  objCashTO.setStatusDt(currDt);
                //  objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                //  objCashTO.setParticulars("By " + CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")) + " ");
                //  objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(loanMap.get("BRANCH_CODE")));
                //  objCashTO.setInitChannType(CommonConstants.CASHIER);
                //  objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                //  objCashTO.setInpAmount(recoveredAmount);
                //  objCashTO.setAmount(recoveredAmount);
                //  objCashTO.setParticulars("BY CASH");
                //  objCashTO.setActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                //  objCashTO.setLinkBatchId(objTO.getTransallId());
                //  objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(loanMap.get("ACT_NUM")));
                //  objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                //  objCashTO.setTransModType(CommonConstants.LOAN_TRANSMODE_TYPE);
                //  cashTransactionList.add(objCashTO);
                //  System.out.println("LOAN_CASH_TRANSACTION objCashTO : " + objCashTO+" cashTransactionList : " + cashTransactionList);
                // }
                if (chargesAmount > 0) {
                    //  CashTransactionTO objCashTO = new CashTransactionTO();
                    //  objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("AC_HEAD")));
                    //  objCashTO.setProdType(TransactionFactory.GL);
                    //  objCashTO.setTransType(CommonConstants.CREDIT);
                    //  objCashTO.setInitTransId(logTO.getUserId());
                    //  objCashTO.setBranchId(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    //  objCashTO.setStatus(CommonConstants.STATUS_CREATED);
                    //  objCashTO.setStatusBy(logTO.getUserId());
                    //  objCashTO.setStatusDt(currDt);
                    //  objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    //  objCashTO.setParticulars("By Charges : " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    //  objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(txnMap.get("BRANCH_CODE")));
                    //  objCashTO.setInitChannType(CommonConstants.CASHIER);
                    //  objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    //  objCashTO.setInpAmount(chargesAmount);
                    //  objCashTO.setAmount(chargesAmount);
                    //  objCashTO.setParticulars("BY CASH");
                    //  objCashTO.setLinkBatchId(objTO.getTransallId());
                    //  objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    //  objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    //  objCashTO.setTransModType(CommonConstants.LOAN_TRANSMODE_TYPE);
                    //  cashTransactionList.add(objCashTO);
                    //  System.out.println("LOAN_CASH_TRANSACTION objCashTO : " + objCashTO+" cashTransactionList : " + cashTransactionList);
                }
            } else if (txnMap.containsKey("GL_CASH_TRANSACTION")) {
                cashTransactionList = new ArrayList();
                CashTransactionTO objCashTO = new CashTransactionTO();
                if (CommonUtil.convertObjToDouble(txnMap.get("RECOVERED_AMOUNT")) > 0) {
                    objCashTO.setTransId("");
                    objCashTO.setProdType(TransactionFactory.GL);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(_branchCode);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Cash " + objTO.getTransallId() + " ");
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setAcHdId(txnMap.get("ACT_NUM").toString());
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(txnMap.get("RECOVERED_AMOUNT")));
                    objCashTO.setAmount((CommonUtil.convertObjToDouble(txnMap.get("RECOVERED_AMOUNT"))));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    objCashTO.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    cashTransactionList.add(objCashTO);
                    System.out.println("GL_CASH_TRANSACTION objCashTO : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
            } else if (txnMap.containsKey("OPERATIVE_CASH_TRANSACTION")) {
                double recoveredAmount = CommonUtil.convertObjToDouble(txnMap.get("RECOVERED_AMOUNT"));
                double interest = CommonUtil.convertObjToDouble(txnMap.get("INTEREST"));
                double principal = CommonUtil.convertObjToDouble(txnMap.get("PRINCIPAL"));
                CashTransactionTO objCashTO = new CashTransactionTO();
                if (CommonUtil.convertObjToDouble(principal) > 0) {
                    cashTransactionList = new ArrayList();
                    objCashTO.setProdType(TransactionFactory.SUSPENSE);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(_branchCode);
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("AC_HEAD")));
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(principal));
                    objCashTO.setAmount((CommonUtil.convertObjToDouble(principal)));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    objCashTO.setTransModType(CommonConstants.OPERATIVE_TRANSMODE_TYPE);
                    cashTransactionList.add(objCashTO);
                    System.out.println("OPERATIVE_CASH_TRANSACTION objCashTO : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
                if (CommonUtil.convertObjToDouble(interest) > 0) {
                    cashTransactionList = new ArrayList();
                    objCashTO = new CashTransactionTO();
                    objCashTO.setProdType(TransactionFactory.GL);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(_branchCode);
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("ACCOUNT_HEAD_ID")));
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(interest));
                    objCashTO.setAmount((CommonUtil.convertObjToDouble(interest)));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(CommonUtil.convertObjToStr(objTO.getTransallId()));
                    objCashTO.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    cashTransactionList.add(objCashTO);
                    System.out.println("OPERATIVE_CASH_TRANSACTION objCashTO : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
            } else if (txnMap.containsKey("DEPOSIT_CASH_TRANSACTION")) {
                if (CommonUtil.convertObjToDouble(txnMap.get("PRINCIPAL")) > 0) {
                    cashTransactionList = new ArrayList();
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setProdType(TransactionFactory.DEPOSITS);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(_branchCode);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Principal " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("FIXED_DEPOSIT_ACHD")));
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(txnMap.get("PRINCIPAL")));
                    objCashTO.setAmount((CommonUtil.convertObjToDouble(txnMap.get("PRINCIPAL"))));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransAllId(objTO.getTransallId());
                    objCashTO.setProdId(CommonUtil.convertObjToStr(txnMap.get("PROD_ID")));
                    objCashTO.setTransModType(CommonConstants.DEPOSIT_TRANSMODE_TYPE);
                    cashTransactionList.add(objCashTO);
                    System.out.println("DEPOSIT_CASH_TRANSACTION objCashTO : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
                if (CommonUtil.convertObjToDouble(txnMap.get("PENAL")) > 0) {
                    cashTransactionList = new ArrayList();
                    CashTransactionTO objCashTO = new CashTransactionTO();
                    objCashTO.setProdType(TransactionFactory.GL);
                    objCashTO.setTransType(CommonConstants.CREDIT);
                    objCashTO.setInitTransId(logTO.getUserId());
                    objCashTO.setBranchId(_branchCode);
                    objCashTO.setStatusBy(logTO.getUserId());
                    objCashTO.setStatusDt(currDt);
                    objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    objCashTO.setParticulars("By Penal " + CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")) + " ");
                    objCashTO.setInitiatedBranch(_branchCode);
                    objCashTO.setInitChannType(CommonConstants.CASHIER);
                    objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(accountHeadMap.get("DELAYED_ACHD")));
                    objCashTO.setInpAmount(CommonUtil.convertObjToDouble(txnMap.get("PENAL")));
                    objCashTO.setAmount((CommonUtil.convertObjToDouble(txnMap.get("PENAL"))));
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(txnMap.get("ACT_NUM")));
                    objCashTO.setTransModType(CommonConstants.GL_TRANSMODE_TYPE);
                    objCashTO.setTransAllId(objTO.getTransallId());
                    cashTransactionList.add(objCashTO);
                    System.out.println("DEPOSIT_CASH_TRANSACTION objCashTO : " + objCashTO);
                    transMap.put("DAILYDEPOSITTRANSTO", cashTransactionList);
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    System.out.println("transMap : " + transMap);
                    HashMap cashMap = cashTransactionDAO.execute(transMap, false);
                    objCashTO = null;
                    cashTransactionList = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return cashTransactionList;
    }

    private ArrayList setTransactionDetailTLAD(HashMap dataMap) throws Exception {
        String generateSingleTransId = "";//generateLinkID();
        ArrayList cashList = new ArrayList();
        List appList = null;
        String ots = "";
        double paidInterest = 0;
        double paidprincipal = 0;
        double transAmt = 0.0;
        double rebateInterest = 0;
        double waiveOffInterest = 0;
        boolean isTLAvailable = false;
        String penalWaiveOff = "";
        String rebateAllowed = "";
        long no_of_installment = 0;
        String asAnWhenCustomer = "";
        HashMap map = new HashMap();
        //added by rishad 14/03/2014 for waiving
        double waiveOffPenal = 0.0;
        double waiveOffNotice = 0.0;
        double waiveOffPrincipal = 0.0;
        String generateWaiveId = CommonUtil.convertObjToStr(generateWaiveOffBatchID());
        System.out.println("dataMap ####" + dataMap);
        HashMap ALL_LOAN_AMOUNT = new HashMap();
        CashTransactionTO objCashTransactionTO = (CashTransactionTO) dataMap.get(CASH_TO);
        if (objCashTransactionTO != null && objCashTransactionTO.getTransType().equals(CommonConstants.CREDIT)) {
            if (objCashTransactionTO.getLinkBatchId() != null && objCashTransactionTO.getLinkBatchId().length() > 0) {
                map.put(CommonConstants.ACT_NUM, objCashTransactionTO.getLinkBatchId());
                ALL_LOAN_AMOUNT = (HashMap) dataMap.get("ALL_AMOUNT");

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue() > 0) {
                    rebateInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("REBATE_INTEREST")).doubleValue();
                }

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("WAIVE_OFF_INTEREST") && CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST")).doubleValue() > 0) {
                    waiveOffInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("WAIVE_OFF_INTEREST")).doubleValue();
                }

                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("PENAL_WAIVE_OFF") && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("PENAL_WAIVE_OFF")).equals("Y")) {
                    penalWaiveOff = "Y";
                    waiveOffPenal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_WAIVE_AMT"));
                } else {
                    penalWaiveOff = "N";
                }
                //added by rishad 
                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("NOTICE_WAIVE_OFF") && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("NOTICE_WAIVE_OFF")).equals("Y")) {
                    waiveOffNotice = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("NOTICE_WAIVE_AMT"));
                }
                if (ALL_LOAN_AMOUNT != null && ALL_LOAN_AMOUNT.containsKey("PRINCIPAL_WAIVE_OFF") && CommonUtil.convertObjToStr(ALL_LOAN_AMOUNT.get("PRINCIPAL_WAIVE_OFF")).equals("Y")) {
                    waiveOffPrincipal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PRINCIPAL_WAIVE_AMT"));
                }
                if (dataMap.containsKey("REBATE_INTEREST") && CommonUtil.convertObjToStr(dataMap.get("REBATE_INTEREST")).equals("Y")) {
                    rebateAllowed = "Y";
                } else {
                    rebateAllowed = "N";
                }
                map.put("TRANS_DT", currDt);
                map.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
                if (lst == null || lst.isEmpty()) {
                    lst = sqlMap.executeQueryForList("IntCalculationDetailAD", map);
                }
                if (lst != null && lst.size() > 0) {
                    map = (HashMap) lst.get(0);
                    System.out.println("map ####" + map);
                    asAnWhenCustomer = CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES"));
                    ots = CommonUtil.convertObjToStr(map.get("OTS"));
                    map.put(CommonConstants.ACT_NUM, objCashTransactionTO.getLinkBatchId());
                    map.put("ACCT_NUM", objCashTransactionTO.getLinkBatchId());
                    map.put("PROD_ID", objCashTransactionTO.getProdId());
                }
                lst = null;
            }
        }
        if (asAnWhenCustomer != null && asAnWhenCustomer.length() > 0 && asAnWhenCustomer.equals("Y")) {// && actionType==ClientConstants.ACTIONTYPE_NEW){

            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                HashMap whereMap = new HashMap();
                //                    whereMap.put(CommonConstants.MAP_WHERE,map.get(CommonConstants.ACT_NUM));
                whereMap.put(CommonConstants.MAP_WHERE, objCashTransactionTO.getTransId());
                List list = sqlMap.executeQueryForList("getCashTransactionTOForAuthorzationTransId", whereMap.get(CommonConstants.MAP_WHERE));
                if (list != null && list.size() > 0) {
                    cashList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        objCashTransactionTO = (CashTransactionTO) list.get(i);
                        objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        cashList.add(objCashTransactionTO);
                        if (i == 0) {
                            deleteRebateInterestTransaction(objCashTransactionTO.getLinkBatchId(), CommonConstants.TOSTATUS_DELETE);
                        }
                    }
                }
                System.out.println("cashListdelete ####" + cashList);
                whereMap = null;
                list = null;
                return cashList;
            }
            if (objCashTransactionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                //                map.put(PROD_ID,CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                List lstachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", map);
                if (lstachd != null && lstachd.size() > 0) {
                    map = (HashMap) lstachd.get(0);
                }
                transAmt = objCashTransactionTO.getAmount().doubleValue();
                if (ALL_LOAN_AMOUNT.containsKey("NO_OF_INSTALLMENT") && ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT") != null) {
                    no_of_installment = CommonUtil.convertObjToLong(ALL_LOAN_AMOUNT.get("NO_OF_INSTALLMENT"));
                }
//                //account closing charges
//                CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);
//                double account_closing_charge=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGE")).doubleValue();
//                if(transAmt>0 && account_closing_charge>0 ){
//                    if( transAmt>=account_closing_charge){
//                        transAmt-=account_closing_charge;
//                        paidInterest=account_closing_charge;
//                    }else{
//                        paidInterest=transAmt;
//                        transAmt-=account_closing_charge;
//                        
//                    }
//                    objCashTO.setAmount(new Double(paidInterest));
//                    objCashTO.setActNum("");
//                    objCashTO.setProdId("");
//                    objCashTO.setProdType(TransactionFactory.GL);
//                    objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_CLOSING_CHRG")));
//                    objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
//                    objCashTO.setAuthorizeRemarks("ACT CLOSING CHARGE");
//                    objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId()+":"+"ACT CLOSING CHARGE");
//                    cashList.add(objCashTO);
//                }
//                paidInterest=0;
                if (ots.equals("Y")) {
                    appList = sqlMap.executeQueryForList("selectAppropriatTransaction_OTS", map.get("PROD_ID"));
                } else {
                    appList = sqlMap.executeQueryForList("selectAppropriatTransaction", map.get("PROD_ID"));
                }
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                } else {
                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                }
                System.out.println("appropriateMap####" + appropriateMap);
                java.util.Collection collectedValues = appropriateMap.values();
                java.util.Iterator it = collectedValues.iterator();
                CashTransactionTO objCashTO = new CashTransactionTO();
                int appTranValue = 0;
                while (it.hasNext()) {
                    appTranValue++;
                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
                    System.out.println("hierachyValue####" + hierachyValue);
                    objCashTO = setCashTransaction(objCashTransactionTO);
                    if (hierachyValue.equals("CHARGES")) {
                        //Account Closing Charges
//                CashTransactionTO objCashTO = setCashTransaction(objCashTransactionTO);
                        if (ALL_LOAN_AMOUNT.containsKey("ACT_CLOSING_CHARGES")) {
                            List chargeLst = (List) ALL_LOAN_AMOUNT.get("ACT_CLOSING_CHARGES");
                            if (chargeLst != null && chargeLst.size() > 0) {
                                System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                                for (int i = 0; i < chargeLst.size(); i++) {
                                    objCashTO = setCashTransaction(objCashTransactionTO);
                                    HashMap chargeMap = new HashMap();
                                    String accHead = "";
                                    double chargeAmt = 0;
                                    chargeMap = (HashMap) chargeLst.get(i);
                                    accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                                    chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                                    //System.out.println("$#@@$ accHead" + accHead);
                                    //System.out.println("$#@@$ chargeAmt" + chargeAmt);
                                    if (transAmt > 0 && chargeAmt > 0) {
                                        if (transAmt >= chargeAmt) {
                                            transAmt -= chargeAmt;
                                            paidInterest = chargeAmt;
                                        } else {
                                            paidInterest = transAmt;
                                            transAmt -= chargeAmt;
                                        }
                                        objCashTO.setAmount(new Double(paidInterest));
                                        objCashTO.setActNum("");
                                        objCashTO.setProdId("");
                                        objCashTO.setProdType(TransactionFactory.GL);
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(accHead));
                                        objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                                        objCashTO.setAuthorizeRemarks("ACT CLOSING CHARGE");
                                        objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ACT CLOSING CHARGE");
                                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                                        objCashTO.setIbrHierarchy("1");
                                        objCashTO.setNarration(objCashTransactionTO.getNarration());
                                        objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_CHARGE");
                                        objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                                        cashList.add(objCashTO);
                                    }
                                    paidInterest = 0;
                                    chargeAmt = 0;
                                }
                            }
                        }
                        //account clsoing misc charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double epchg = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EP_COST")).doubleValue();
                        if (transAmt > 0 && epchg > 0) {
                            if (transAmt >= epchg) {
                                transAmt -= epchg;
                                paidInterest = epchg;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= epchg;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("EP Cost")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("EP_COST");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "EP CHARGE");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("2");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_EP_CHARGE");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double arcchg = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARC_COST")).doubleValue();
                        if (transAmt > 0 && arcchg > 0) {
                            if (transAmt >= arcchg) {
                                transAmt -= arcchg;
                                paidInterest = arcchg;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= arcchg;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ARC Cost")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ARC_COST");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ARC CHARGE");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("3");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ARC_CHARGE");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double accountClosingMisc = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ACT_CLOSING_MISC_CHARGE")).doubleValue();
                        if (transAmt > 0 && accountClosingMisc > 0) {
                            if (transAmt >= accountClosingMisc) {
                                transAmt -= accountClosingMisc;
                                paidInterest = accountClosingMisc;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= accountClosingMisc;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ACT CLOSING MISC CHARGE");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ACT CLOSING MISC CHARGE");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("4");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_MISC_CHARGE");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //postage charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double postageCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("POSTAGE CHARGES")).doubleValue();
                        if (transAmt > 0 && postageCharges > 0) {
                            if (transAmt >= postageCharges) {
                                transAmt -= postageCharges;
                                paidInterest = postageCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= postageCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("POSTAGE_CHARGES")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("POSTAGE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "POSTAGE_CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("5");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_POSTAGE_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //notice charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double noticeCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("NOTICE CHARGES")).doubleValue();
                        if (noticeCharges > 0 && waiveOffNotice > 0) {
                            noticeWaiveOff(objCashTO, waiveOffNotice, generateSingleTransId, generateWaiveId);
                            //returntransid.put("SINGLE_TRANS_WAIVEID", generateSingleTransId);
                            noticeCharges -= waiveOffNotice;
                            //  continue;
                        }
                        if (transAmt > 0 && noticeCharges > 0) {
                            if (transAmt >= noticeCharges) {
                                transAmt -= noticeCharges;
                                paidInterest = noticeCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= noticeCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("NOTICE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "NOTICE_CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("6");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_NOTICE_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //advertisement charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double advertiseCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ADVERTISE CHARGES")).doubleValue();
                        if (transAmt > 0 && advertiseCharges > 0) {
                            if (transAmt >= advertiseCharges) {
                                transAmt -= advertiseCharges;
                                paidInterest = advertiseCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= advertiseCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ADVERTISE_ACHEAD")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ADVERTISE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ADVERTISE_CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("7");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ADVERTISE_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //arbitary charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double orbitaryCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARBITRARY CHARGES")).doubleValue();
                        if (transAmt > 0 && orbitaryCharges > 0) {
                            if (transAmt >= orbitaryCharges) {
                                transAmt -= orbitaryCharges;
                                paidInterest = orbitaryCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= orbitaryCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("ARBITRARY_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("ARBITRARY CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "ARBITRARY CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("8");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_ARBITRARY_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //legal charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double legalCharges = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LEGAL CHARGES")).doubleValue();
                        if (transAmt > 0 && legalCharges > 0) {
                            if (transAmt >= legalCharges) {
                                transAmt -= legalCharges;
                                paidInterest = legalCharges;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= legalCharges;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("LEGAL_CHARGES")));//NOTICE_CHARGES
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("LEGAL CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "LEGAL CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("9");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_LEGAL_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //insurance charges
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double insuranceCharge = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES")).doubleValue();
                        if (transAmt > 0 && insuranceCharge > 0) {
                            if (transAmt >= insuranceCharge) {
                                transAmt -= insuranceCharge;
                                paidInterest = insuranceCharge;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= insuranceCharge;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("INSURANCE_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("INSURANCE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "INSURANCE CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("10");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_INSURANCE_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //missleneous
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double miscellous = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MISCELLANEOUS CHARGES")).doubleValue();
                        if (transAmt > 0 && miscellous > 0) {
                            if (transAmt >= miscellous) {
                                transAmt -= miscellous;
                                paidInterest = miscellous;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= miscellous;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "MISCELLANEOUS CHARGES");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                            objCashTO.setIbrHierarchy("11");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_MISC_SERV_CHRG");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;
                        //execution degree
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double executionDegree = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EXECUTION DECREE CHARGES")).doubleValue();
                        if (transAmt > 0 && executionDegree > 0) {
                            if (transAmt >= executionDegree) {
                                transAmt -= executionDegree;
                                paidInterest = executionDegree;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= executionDegree;
                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("EXECUTION_DECREE_CHARGES")));//MISC_SERV_CHRG
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("EXECUTION DECREE CHARGES");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "EXECUTION DECREE CHARGES");
                            objCashTO.setLoanHierarchy("4"); // "4" For Charges
                            objCashTO.setIbrHierarchy("12");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_EXECUTION_DECREE_CHARGES");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                        }
                        paidInterest = 0;

                        //CASE DETAILS Changed By Suresh
                        if (ALL_LOAN_AMOUNT.containsKey("OTHER_CHARGES")) {
                            if (otherChargesMap == null) {
                                otherChargesMap = new HashMap();
                            }
                            otherChargesMap = (HashMap) ALL_LOAN_AMOUNT.get("OTHER_CHARGES");
                            System.out.println("@#$@#otherChargesMap:" + otherChargesMap);
                            Object keys[] = otherChargesMap.keySet().toArray();
                            for (int i = 0; i < otherChargesMap.size(); i++) {
                                objCashTO = setCashTransaction(objCashTransactionTO);
                                double otherCharge = CommonUtil.convertObjToDouble(otherChargesMap.get(keys[i])).doubleValue();
                                System.out.println("$#@$#@$@otherCharge : " + otherCharge + ":" + keys[i]);
                                if (transAmt > 0 && otherCharge > 0) {
                                    if (transAmt >= otherCharge) {
                                        transAmt -= otherCharge;
                                        paidInterest = otherCharge;
                                    } else {
                                        paidInterest = transAmt;
                                        transAmt -= otherCharge;
                                    }
                                    objCashTO.setActNum(objCashTransactionTO.getLinkBatchId());
                                    objCashTO.setInpAmount(new Double(paidInterest));
                                    objCashTO.setAmount(new Double(paidInterest));
                                    objCashTO.setActNum("");
                                    objCashTO.setProdId("");
                                    objCashTO.setProdType(TransactionFactory.GL);
                                    if (CommonUtil.convertObjToStr(keys[i]).equals("NOTICE CHARGES")) {
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES"))));
                                    } else if (CommonUtil.convertObjToStr(keys[i]).equals("OTHER CHARGES")) {
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(map.get("OTHER_CHARGES"))));
                                    } else {
                                        objCashTO.setAcHdId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(keys[i])));
                                    }
                                    objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                                    objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(keys[i]));
                                    objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + keys[i]);
                                    objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "4" For Charges
                                    objCashTO.setIbrHierarchy("13");
                                    objCashTO.setNarration(objCashTransactionTO.getNarration());
                                    System.out.println("@#$@#objCashTO:" + objCashTO);
                                    objCashTO.setInstrumentNo2("LOAN_OTHER_CHARGES");
                                    objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                                    cashList.add(objCashTO);
                                }
                                paidInterest = 0;
                                otherCharge = 0;
                            }
                            continue;
                        }

                    }
                    if (hierachyValue.equals("PENALINTEREST")) {
                        //altered by rishad 14/03/2014 for partial and full penal waiving
                        //penal interest
                        double penalInterest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
                        if (penalWaiveOff.equals("Y") || waiveOffPenal > 0) {
                            // penalWaiveOff(objCashTransactionTO, waiveOffPenal, waiveOffInterest, generateSingleTransId);, String generateWaiveId
                            penalWaiveOff(objCashTransactionTO, waiveOffPenal, 0.0, generateWaiveId);
                            //returntransid.put("SINGLE_TRANS_WAIVEID", generateSingleTransId);
                            penalInterest -= waiveOffPenal;
                            // continue;
                        }
                        objCashTO = setCashTransaction(objCashTransactionTO);
//          double penalInterest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("PENAL_INT")).doubleValue();
                        if (transAmt > 0 && penalInterest > 0) {
                            if (transAmt >= penalInterest) {
                                transAmt -= penalInterest;
                                paidInterest = penalInterest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= penalInterest;

                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("PENAL_INT")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("PENAL_INT");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "PENAL_INT");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "3" For Penal Interest
                            objCashTO.setIbrHierarchy("14");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_PENAL_INT");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                    if (hierachyValue.equals("INTEREST")) {
                        //interest
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        double interest = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_INT")).doubleValue();
                        if (rebateInterest > 0 && interest > 0) {
                            if (rebateAllowed.equals("Y")) {
                                if (interest >= rebateInterest) {
                                    interest -= rebateInterest;
                                    rebateInterestTransaction(objCashTransactionTO, rebateInterest, 0.0);

                                } else {
                                    rebateInterestTransaction(objCashTransactionTO, rebateInterest, interest);
                                    interest = 0;
                                }

                            }

                        }
                        if (waiveOffInterest > 0 && interest > 0) {
                            if (interest >= waiveOffInterest) {
                                interestWaiveOff(objCashTransactionTO, waiveOffInterest, generateSingleTransId, generateWaiveId);
                                //returntransid.put("SINGLE_TRANS_WAIVEID", generateSingleTransId);
                                interest -= waiveOffInterest;
                            }
                        }
                        if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;

                            }
                            objCashTO.setAmount(new Double(paidInterest));
                            objCashTO.setActNum("");
                            objCashTO.setProdId("");
                            objCashTO.setProdType(TransactionFactory.GL);
                            objCashTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            objCashTO.setAuthorizeRemarks("INTEREST");
                            objCashTO.setParticulars(objCashTransactionTO.getLinkBatchId() + ":" + "INTEREST");
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Penal Interest
                            objCashTO.setIbrHierarchy("15");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_INTEREST");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                    if (hierachyValue.equals("PRINCIPAL")) {
                        double principal = CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                        //principal
                        if (principal > 0 && waiveOffPrincipal > 0) {
                            principalWaiveOff(objCashTO, waiveOffPrincipal, "", generateWaiveId);
                            //returntransid.put("SINGLE_TRANS_WAIVEID", generateSingleTransId);
                            principal -= waiveOffPrincipal;
                        }
                        if (transAmt > 0.0 && principal > 0) {
                            if (transAmt >= principal) {
                                transAmt -= principal;
                                paidprincipal = principal;
                            } else {
                                paidprincipal = transAmt;
                                transAmt -= principal;

                            }
                            //    System.out.println("paidprincipal===="+paidprincipal);
                            objCashTO = setCashTransaction(objCashTransactionTO);
                            objCashTO.setAmount(new Double(paidprincipal));
                            objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                            //                    if(no_of_installment>0)
                            objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
                            objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
                            objCashTO.setIbrHierarchy("16");
                            objCashTO.setNarration(objCashTransactionTO.getNarration());
                            objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
                            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                            cashList.add(objCashTO);
                            continue;
                        }
                    }
                }
                if (transAmt > 0.0) {
                    for (int i = 0; i < cashList.size(); i++) {
                        CashTransactionTO obj = (CashTransactionTO) cashList.get(i);
                        if (obj.getProdType().equals("TL")) {
                            double principal = obj.getAmount().doubleValue();
                            principal += transAmt;
                            obj.setAmount(new Double(principal));
                            cashList.set(i, obj);
                            isTLAvailable = true;
                            break;
                        }

                    }
                    if (!isTLAvailable) {
                        objCashTO = setCashTransaction(objCashTransactionTO);
                        objCashTO.setAmount(new Double(transAmt));
                        objCashTO.setLinkBatchId(objCashTransactionTO.getLinkBatchId());
                        //                    if(no_of_installment>0)
                        objCashTO.setAuthorizeRemarks(String.valueOf(no_of_installment));
                        objCashTO.setLoanHierarchy(String.valueOf(appTranValue)); // "2" For Principal
                        objCashTO.setNarration(objCashTransactionTO.getNarration());
                        objCashTO.setInstrumentNo2("LOAN_PRINCIPAL");
                        objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                        cashList.add(objCashTO);
                    }
                }
                lstachd = null;
                objCashTO = null;
            }
        }
        //            else{
        //                final CashTransactionTO objCashTransactionTO = setCashTransaction();
        //                cashList.add(objCashTransactionTO);
        //            }
        map = null;
        ALL_LOAN_AMOUNT = null;
        objCashTransactionTO = null;
        System.out.println("cashlist####" + cashList);
        return cashList;
    }

    public CashTransactionTO setCashTransaction(CashTransactionTO cashTo) {
        //log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setTransId(cashTo.getTransId());
            objCashTransactionTO.setAcHdId(cashTo.getAcHdId());
            objCashTransactionTO.setProdId(cashTo.getProdId());
            objCashTransactionTO.setProdType(cashTo.getProdType());
            objCashTransactionTO.setActNum(cashTo.getActNum());
            objCashTransactionTO.setInpAmount(cashTo.getInpAmount());
            objCashTransactionTO.setInpCurr(cashTo.getInpCurr());
            objCashTransactionTO.setAmount(cashTo.getAmount());
            objCashTransactionTO.setTransType(cashTo.getTransType());
            objCashTransactionTO.setInstType(cashTo.getInstType());
            objCashTransactionTO.setBranchId(cashTo.getBranchId());
            objCashTransactionTO.setStatusBy(cashTo.getStatusBy());
            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setInstrumentNo2(cashTo.getInstrumentNo2());
            Date InsDt = cashTo.getInstDt();
            if (InsDt != null) {
                Date insDate = (Date) curDate.clone();
                insDate.setDate(InsDt.getDate());
                insDate.setMonth(InsDt.getMonth());
                insDate.setYear(InsDt.getYear());
                //            objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
                objCashTransactionTO.setInstDt(insDate);
            } else {
                objCashTransactionTO.setInstDt(cashTo.getInstDt());
            }
            objCashTransactionTO.setTokenNo(cashTo.getTokenNo());
            objCashTransactionTO.setInitTransId(cashTo.getInitTransId());
            objCashTransactionTO.setInitChannType(cashTo.getInitChannType());
            objCashTransactionTO.setParticulars(cashTo.getParticulars());
            objCashTransactionTO.setInitiatedBranch(cashTo.getInitiatedBranch());
            objCashTransactionTO.setCommand(cashTo.getCommand());
            objCashTransactionTO.setAuthorizeStatus_2(cashTo.getAuthorizeStatus_2());
            objCashTransactionTO.setTransAllId(cashTo.getTransAllId());
            objCashTransactionTO.setSingleTransId(cashTo.getSingleTransId());
            objCashTransactionTO.setTransModType(cashTo.getTransModType());
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            //log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void noticeWaiveOff(CashTransactionTO obj, double waiveNoticeAmt, String generateSingleTransId, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objnoticeWaive = new TermLoanPenalWaiveOffTO();
        try {
            noticeWaiveoffTransaction(obj, waiveNoticeAmt, generateSingleTransId);
            if (obj != null) {
                objnoticeWaive.setAcctNum(obj.getActNum());
                objnoticeWaive.setRemarks("NOTICE_WAIVEOFF");
                //objnoticeWaive.setWaivnoticeWaiveoffTransactioneDt((Date) currDt.clone());
                objnoticeWaive.setWaiveAmt(new Double(waiveNoticeAmt));
                objnoticeWaive.setStatus(CommonConstants.STATUS_CREATED);
                objnoticeWaive.setStatusBy(obj.getStatusBy());
                objnoticeWaive.setStatusDt((Date) currDt.clone());
                //  objnoticeWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
                objnoticeWaive.setWaiveOffId(generateWaiveId);
                sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objnoticeWaive);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void noticeWaiveoffTransaction(CashTransactionTO cashTO, double noticeWaive, String generateSingleTransId) throws Exception {

        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        int count = 0;
        if (noticeWaive > 0) {
            transMap.put("WAIVE_NOTICE", new Double(noticeWaive));
            count++;
        }
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double notice = 0;
            double transAmt = 0;
            notice = CommonUtil.convertObjToDouble(transMap.get("WAIVE_NOTICE")).doubleValue();
            transAmt = notice;
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            if (notice > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_NOTICE_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            if (notice > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff notice for " + cashTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "NOTICE_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "NOTICE_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "NOTICE_WAIVEOFF");
            }
            String fromCashModuleProdType = "TL";
            txMap.put("generateSingleTransId", generateSingleTransId);
            if (cashTO.getTransModType().equals("")) {
                if (fromCashModuleProdType.equals("TL")) {
                    txMap.put("TRANS_MOD_TYPE", "TL");
                } else {
                    txMap.put("TRANS_MOD_TYPE", "AD");
                }
            } else {
                txMap.put("TRANS_MOD_TYPE", cashTO.getTransModType());
            }
            transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            if (notice > 0) {
                txMap.put("AUTHORIZEREMARKS", "NOTICE_WAIVEOFF");
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NOTICE_CHARGES"));
            }
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            if (notice > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff notice upto" + CommonUtil.convertObjToStr(properFormatDate));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
            trans.setBreakLoanHierachy("Y");
            //String fromCashModuleProdType = "TL";
            txMap.put("generateSingleTransId", generateSingleTransId);
            if (cashTO.getTransModType().equals("")) {
                if (fromCashModuleProdType.equals("TL")) {
                    txMap.put("TRANS_MOD_TYPE", "TL");
                } else {
                    txMap.put("TRANS_MOD_TYPE", "AD");
                }
            } else {
                txMap.put("TRANS_MOD_TYPE", cashTO.getTransModType());
            }
            transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_NOTICE");
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
    }

    private void interestWaiveOff(CashTransactionTO obj, double waiveInterestAmt, String generateSingleTransId, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objinterestWaive = new TermLoanPenalWaiveOffTO();
        interestWaiveoffTransaction(obj, waiveInterestAmt, 0);

        if (obj != null) {
            objinterestWaive.setAcctNum(obj.getActNum());
            objinterestWaive.setRemarks("INTEREST_WAIVEOFF");
            objinterestWaive.setWaiveDt((Date) currDt.clone());
            objinterestWaive.setWaiveAmt(new Double(waiveInterestAmt));
            objinterestWaive.setStatus(CommonConstants.STATUS_CREATED);
            objinterestWaive.setStatusBy(obj.getStatusBy());
            objinterestWaive.setStatusDt((Date) currDt.clone());
            //objinterestWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
            objinterestWaive.setWaiveOffId(generateWaiveId);
            sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objinterestWaive);

        }

    }

    private void principalWaiveOff(CashTransactionTO obj, double waivePrincipalAmt, String generateSingleTransId, String generateWaiveId) throws Exception {
        TermLoanPenalWaiveOffTO objprincipalWaive = new TermLoanPenalWaiveOffTO();
        try {
            principalWaiveoffTransaction(obj, waivePrincipalAmt, generateSingleTransId);
            if (obj != null) {
                objprincipalWaive.setAcctNum(obj.getActNum());
                objprincipalWaive.setWaiveDt((Date) currDt.clone());
                objprincipalWaive.setWaiveAmt(new Double(waivePrincipalAmt));
                objprincipalWaive.setRemarks("PRINCIPAL_WAIVEOFF");
                objprincipalWaive.setStatus(CommonConstants.STATUS_CREATED);
                objprincipalWaive.setStatusBy(obj.getStatusBy());
                objprincipalWaive.setStatusDt((Date) currDt.clone());
                objprincipalWaive.setWaiveOffId(CommonUtil.convertObjToStr(generateWaiveOffBatchID()));
                objprincipalWaive.setWaiveOffId(generateWaiveId);
                sqlMap.executeUpdate("insertTermLoanWaiveOffTO", objprincipalWaive);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    private void principalWaiveoffTransaction(CashTransactionTO cashTO, double waivePrincipalAmt, String generateSingleTransId) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap transMap = new HashMap();
        int count = 0;
        if (waivePrincipalAmt > 0) {
            transMap.put("WAIVE_PRINCIPAL", new Double(waivePrincipalAmt));
            count++;
        }
        dataMap.put("ACCT_NUM", cashTO.getActNum());
        List lst = (List) sqlMap.executeQueryForList("getInterestAndPenalIntActHead", dataMap);
        if (lst != null && lst.size() > 0) {
            HashMap acHeads = (HashMap) lst.get(0);
            ArrayList transferList = new ArrayList(); // for local transfer
            TransferTrans trans = new TransferTrans();
            HashMap txMap = new HashMap();
            double principal = 0;
            double transAmt = 0;
            principal = CommonUtil.convertObjToDouble(transMap.get("WAIVE_PRINCIPAL")).doubleValue();
            transAmt = principal;
            txMap = new HashMap();
            transferList = new ArrayList(); // for local transfer
            trans = new TransferTrans();
            trans.setTransMode(CommonConstants.TX_TRANSFER);
            if (principal > 0) {
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_PRINCIPAL_HEAD"));
            }
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            if (principal > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff principal for " + cashTO.getActNum());
                txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_WAIVEOFF");
                txMap.put("DR_INST_TYPE", "PRINCIPAL_WAIVEOFF");
                txMap.put("DR_INSTRUMENT_2", "PRINCIPAL_WAIVEOFF");
            }
            txMap.put("generateSingleTransId", generateSingleTransId);
            String fromCashModuleProdType = "TL";
            if (cashTO.getTransModType().equals("")) {
                if (fromCashModuleProdType.equals("TL")) {
                    txMap.put("TRANS_MOD_TYPE", "TL");
                } else {
                    txMap.put("TRANS_MOD_TYPE", "AD");
                }
            } else {
                txMap.put("TRANS_MOD_TYPE", cashTO.getTransModType());
            }
            transferList.add(trans.getDebitTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            if (principal > 0) {
                txMap.put(TransferTrans.CR_AC_HD, cashTO.getAcHdId());
                txMap.put(TransferTrans.CR_ACT_NUM, cashTO.getActNum());
            }
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, cashTO.getProdType());
            if (principal > 0) {
                txMap.put(TransferTrans.PARTICULARS, "WaiveOff principal upto" + CommonUtil.convertObjToStr(properFormatDate));
            }
            trans.setInitiatedBranch(_branchCode);
            trans.setLinkBatchId(CommonUtil.convertObjToStr(cashTO.getActNum()));
            trans.setBreakLoanHierachy("Y");
            txMap.put("generateSingleTransId", generateSingleTransId);
            //String fromCashModuleProdType = "TL";
            if (cashTO.getTransModType().equals("")) {
                if (fromCashModuleProdType.equals("TL")) {
                    txMap.put("TRANS_MOD_TYPE", "TL");
                } else {
                    txMap.put("TRANS_MOD_TYPE", "AD");
                }
            } else {
                txMap.put("TRANS_MOD_TYPE", cashTO.getTransModType());
            }
            transferList.add(trans.getCreditTransferTO(txMap, transAmt));//CommonUtil.convertObjToDouble(obj.getPenalAmt()).doubleValue()));
            trans.doDebitCredit(transferList, _branchCode, false);
            transMap.remove("WAIVE_PRINCIPAL");
            transferList = null;
            trans = null;
            acHeads = null;
            lst = null;
            txMap = null;
        }
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
}
