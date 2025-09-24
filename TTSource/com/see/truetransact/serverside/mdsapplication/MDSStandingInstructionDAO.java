/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSStandingInstructionDAO.java
 * 
 * Created on Tue Oct 11 13:18:08 IST 2011
 */
package com.see.truetransact.serverside.mdsapplication;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.MDSDepositTypeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import java.sql.SQLException;
import java.util.*;

/**
 * MDSStandingInstruction DAO.
 *
 */
public class MDSStandingInstructionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    public List standingLst = null;
    private Date currDt = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private String schemeName = "";
    private Map returnMap = null;
    private List returnMapList = null;
    private List returnSingleMapList = null;
    private String generateSingleTransId = null;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    HashMap  drAcMinBal = new HashMap();
    private ArrayList bonusAmountList = null;
    private ArrayList ForfeitebonusAmountList = null;
    private ArrayList penalList = null;
    private ArrayList penalRealList = null;
    private ArrayList instList = null;
    private ArrayList narrationList = null;
    private String isWeeklyOrMonthlyScheme = "";
    private int instFrequency = 0;
    private String txtSchemeName = "";
    HashMap tempDebitAc = new HashMap();
    String isSplitMDSTransaction = "";
    private HashMap splitTransMap;
    private ArrayList finalSplitList = null;
    HashMap MDSbonusMap = new HashMap(); //2093
    HashMap errorMap = new HashMap();
    
    /** Creates a new instance of OperativeAcctProductDAO */
    public MDSStandingInstructionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            MDSStandingInstructionDAO dao = new MDSStandingInstructionDAO();
            HashMap inputMap = new HashMap();
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        generateSingleTransId = generateLinkID();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in MDSStandingInstruction DAO : " + map);
        if (map.containsKey("MDS_STANDING_INSTRUCTION")) {
            try {
//                if(!map.containsKey("FROM_MDS_RECOVERY_SCREEN"))
                //sqlMap.startTransaction();
                returnMapList = new ArrayList();
                returnSingleMapList = new ArrayList();
                returnMap = new HashMap();
                standingLst = (List) map.get("MDS_STANDING_INSTRUCTION");
              //  System.out.println("@##$#$% standingLst #### :" + standingLst);
                map.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                executeTransactionPart(map);
                if (returnMapList != null && returnMapList.size() > 0) {
                    returnMap.put("TRANSACTION_DETAILS", returnMapList);
                    returnMap.put("TRANSACTION_DETAILS_SI", returnSingleMapList);
                }
//                if(!map.containsKey("FROM_MDS_RECOVERY_SCREEN"))
               // sqlMap.commitTransaction();
            } catch (Exception e) {
               // sqlMap.rollbackTransaction();
                e.printStackTrace();
                throw e;
            }
            destroyObjects();
        }
        if (map.containsKey("PENDING_LIST")) {
            returnMap = new HashMap();
            List pendingAuthlst = sqlMap.executeQueryForList("checkPendingForAuthorization", map);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                returnMap.put("PENDING_LIST", pendingAuthlst);
            }
            if(map.containsKey("SI_MAP") && map.get("SI_MAP")!=null){
                HashMap siMap = new HashMap();
                siMap = (HashMap) map.get("SI_MAP");
                if(siMap!=null && siMap.size()>0){
                    HashMap finMap = insertTableData(siMap);
                    returnMap.put("FINAL_MAP",finMap);
                    returnMap.put("FINAL_SPLIT_LIST",finalSplitList);// Added by nithya for KD 504
                    returnMap.put("IS_SPLIT_MDS_TRANSACTION",isSplitMDSTransaction);
                    returnMap.put("MDS_BONUS_MAP",MDSbonusMap);
                }
            }            
        }
        System.out.println("@#$@@$@@@$ returnMap : " + returnMap);
        return (HashMap) returnMap;
    }

    //Added By Kannan AR
    private HashMap insertTableData(HashMap whereMap) {
        HashMap finalMap = new HashMap();
        try {
            String scheme_Name = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
            setTxtSchemeName(scheme_Name);
            int currentInstallment = CommonUtil.convertObjToInt(whereMap.get("CURRENT_INSALL_NO"));
            HashMap productMap = new HashMap();
            long noOfInstPay = 0;
            double totReversalBonusAmt = 0.0; //2093
            double totForfeitBonusAmt = 0.0;
            int instDay = 1;
            int totIns = 0;
            Date startDate = null;
            Date insDate = null;
            int startMonth = 0;
            int insMonth = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            drAcMinBal = new HashMap();
            tempDebitAc = new HashMap();
            boolean bonusAvailabe = true;
            HashMap dataMap = new HashMap();
            isSplitMDSTransaction = "";
            int instFrequency = 0;
            isWeeklyOrMonthlyScheme = "";
            String chittalNo = "";
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap chittalPenalAmtMap =  new HashMap();
            finalSplitList = new ArrayList();
            MDSbonusMap = new HashMap();
            List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
            if (lst != null && lst.size() > 0) {
                productMap = (HashMap) lst.get(0);
                totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                startMonth = insDate.getMonth();
                isSplitMDSTransaction = CommonUtil.convertObjToStr(productMap.get("IS_SPLIT_MDS_TRANSACTION"));
            }
            // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
//            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            // String bonusAdvance = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            String bonusAdvance = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            if (productMap.containsKey("INSTALLMENT_FREQUENCY") && productMap.get("INSTALLMENT_FREQUENCY") != null) {
                if (CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY")) == 7) {
                    instFrequency = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY"));
                    isWeeklyOrMonthlyScheme = "W";
                } else {
                    instFrequency = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_FREQUENCY"));
                    isWeeklyOrMonthlyScheme = "M";
                }
            }

            List instDtLst = sqlMap.executeQueryForList("getMDSNextInstDtForInstNo", whereMap);
            HashMap instDtMap = (HashMap) instDtLst.get(0);
            if (isWeeklyOrMonthlyScheme.equalsIgnoreCase("M")) {
                whereMap.put("CURR_DATE", instDtMap.get("NEXT_INSTALLMENT_DATE"));
            }
            List standInsList = sqlMap.executeQueryForList("getStandingInsDetails", whereMap);
            int startNoForPenal = 0;
            int addNo = 1;
            int firstInst_No = -1;
            if (bonusFirstInst.equals("Y")) {
                startNoForPenal = 1;
                addNo = 0;
                firstInst_No = 0;
            }
            if (standInsList != null && standInsList.size() > 0) {
                for (int i = 0; i < standInsList.size(); i++) {
                    bonusAvailabe = true;
                    dataMap = (HashMap) standInsList.get(i);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(dataMap.get("CHITTAL_NO"));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SUB_NO")));
                    rowList.add(dataMap.get("MEMBER_NAME"));
                    double insAmt = CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")).doubleValue();
                    chittalNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
                    long pendingInst = 0;
                    int divNo = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    }
                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                    }
                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", divNo);
                    insDateMap.put("SCHEME_NAME", scheme_Name);
                    if (isWeeklyOrMonthlyScheme.equalsIgnoreCase("M")) {
                        insDateMap.put("CURR_DATE", instDtMap.get("NEXT_INSTALLMENT_DATE"));
                    } else {
                        insDateMap.put("CURR_DATE", currDt.clone());
                    }
//                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                    List insDateLst = null;
                    boolean monthlyScheme = false;// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                        insDateLst = sqlMap.executeQueryForList("getWeeklyMDSCurrentInsDate", insDateMap);
                    } else {
                        insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
                        monthlyScheme = true;// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                    }
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        if (monthlyScheme) {// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                            curInsNo = currentInstallment;
                        }
                        pendingInst = curInsNo - noOfInsPaid;
                        /* Added by nithya---*/
                        if (bonusFirstInst.equalsIgnoreCase("Y")) {
                            pendingInst -= 1;//nithyaaaa
                        }
                        /* Added by nithya--- end*/
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }
                    noOfInstPay = pendingInst + 1;
                    if (bonusFirstInst.equalsIgnoreCase("Y") /*|| CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")*/) {//Added By Nidhin for Bank advance case installment no taken wrong
                        // noOfInstPay -= 1;nithyaaa /* commented by nithya---*/
                    }
                    //if (bonusAdvance.equals("N") && pendingInst<=0) {  // Bonus For First Installment
                    ///   noOfInstPay=noOfInstPay+1;
                    // }
                    
                    if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                        noOfInstPay -= 1; 
                    }
                    
                    double availableBalance = 0.0;
                    availableBalance = CommonUtil.convertObjToDouble(dataMap.get("AVAILABLE_BALANCE")).doubleValue();
                    HashMap prizedMap = new HashMap();
                    prizedMap.put("SCHEME_NAME", scheme_Name);
                    prizedMap.put("DIVISION_NO", String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    //
                    System.out.println("productMap**" + productMap);
                    if (productMap.containsKey("FROM_AUCTION_ENTRY") && productMap.get("FROM_AUCTION_ENTRY") != null && productMap.get("FROM_AUCTION_ENTRY").equals("Y")) {
                        lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
                        setRdoPrizedMember_Yes(false);
                        setRdoPrizedMember_No(false);
                        System.out.println("lst in FROM_AUCTION_ENTRY" + lst);
                        if (lst != null && lst.size() > 0) {
                            prizedMap = (HashMap) lst.get(0);
                            if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                                setRdoPrizedMember_Yes(true);
                            }
                            if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                                setRdoPrizedMember_Yes(true);
                            }
                        } else {
                            setRdoPrizedMember_No(true);
                        }
                    } else if (productMap.containsKey("AFTER_CASH_PAYMENT") && productMap.get("AFTER_CASH_PAYMENT") != null && productMap.get("AFTER_CASH_PAYMENT").equals("Y")) {
                        lst = sqlMap.executeQueryForList("getSelectPrizedDetailsAfterCashPayment", prizedMap);
                        setRdoPrizedMember_Yes(false);
                        setRdoPrizedMember_No(false);
                        System.out.println("lst in AFTER_CASH_PAYMENT" + lst);
                        if (lst != null && lst.size() > 0) {
                            prizedMap = (HashMap) lst.get(0);
                            System.out.println("SIIIIII" + prizedMap.size());
                            if (prizedMap.size() >= 1) {
                                setRdoPrizedMember_Yes(true);
                            } else {
                                setRdoPrizedMember_No(true);
                            }
                        } else {
                            setRdoPrizedMember_No(true);
                        }
                    } else {
                        lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
                        setRdoPrizedMember_Yes(false);
                        setRdoPrizedMember_No(false);
                        System.out.println("lst in ELSE" + lst);
                        if (lst != null && lst.size() > 0) {
                            prizedMap = (HashMap) lst.get(0);
                            if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                                setRdoPrizedMember_Yes(true);
                            }
                            if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                                setRdoPrizedMember_Yes(true);
                            }
                        } else {
                            setRdoPrizedMember_No(true);
                        }
                    }
                    //
                    if (pendingInst > 0) {              //pending installment calculation starts...
                        insDueAmt = (long) insAmt * pendingInst;
                    }
                    long pendingInstallment = pendingInst;

                    //Calculate MDS
                    double netAmt = 0;
                    double penalAmt = 0;
                    double totDiscAmt = 0;
                    double totBonusAmt = 0;
                    double insAmtPayable = 0;
                    //System.out.println("########### availableBalance 44: " + availableBalance);
                    String sub_No = CommonUtil.convertObjToStr(dataMap.get("SUB_NO"));
                    productMap.put("DR_ACT_NO", dataMap.get("DR_ACT_NO"));
                    HashMap calcMap = calculateMDSStandingInstruction(scheme_Name, chittalNo, sub_No, divNo, noOfInsPaid, instDay, productMap, pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance,currentInstallment);
                    //System.out.println("Calcmapppuuuu " + calcMap);
                    if (calcMap != null && calcMap.size() > 0) {
                        netAmt = CommonUtil.convertObjToDouble(calcMap.get("NET_AMOUNT")).doubleValue();
                        insAmtPayable = CommonUtil.convertObjToDouble(calcMap.get("INST_AMOUNT_PAYABLE")).doubleValue();
                        //done by akhi since ',' in bonus amount it is not getting converted to double value
                        String totbamout = calcMap.get("BONUS").toString();
                        totbamout = totbamout.replace(",", "");
                        totBonusAmt = Double.parseDouble(totbamout);
                        //System.out.println("totobomuuh " + totBonusAmt + "   " + CommonUtil.convertObjToDouble(calcMap.get("BONUS")).doubleValue() + "     " + calcMap.get("BONUS"));                      
                        totDiscAmt = CommonUtil.convertObjToDouble(calcMap.get("DISCOUNT")).doubleValue();
                        penalAmt = CommonUtil.convertObjToDouble(calcMap.get("PENAL")).doubleValue();
                        noOfInstPay = CommonUtil.convertObjToLong(calcMap.get("NO_OF_INST_PAY"));
                        totReversalBonusAmt = CommonUtil.convertObjToDouble(calcMap.get("FORFEIT_BONUS")); //2093
                        totForfeitBonusAmt = CommonUtil.convertObjToDouble(calcMap.get("FORFEIT_RECOVERY"));
                        if (pendingInst == noOfInstPay) //pendingInst+1 commented by Nidhin for bonus transaction is not correct :Mantis ID : 9438
                        {
                            dataMap.put("BONUS_NEW", CommonUtil.convertObjToDouble(calcMap.get("BONUS_NEW")).doubleValue());
                        } else {
                            dataMap.put("BONUS_NEW", "0");
                        }
                    }

//                    netAmt = (noOfInstPay*insAmt)+penalAmt-(totBonusAmt+totDiscAmt);
//                    insAmtPayable = (noOfInstPay*insAmt) -(totBonusAmt+totDiscAmt);

                    dataMap.put("DIVISION_NO", String.valueOf(divNo));
                    dataMap.put("CHIT_START_DT", startDate);
                    dataMap.put("INSTALLMENT_DATE", insDate);
                    dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
                    dataMap.put("CURR_INST", String.valueOf(curInsNo));
                    dataMap.put("PENDING_INST", String.valueOf(pendingInst));
                    dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
                    dataMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
                    dataMap.put("INST_AMT_PAYABLE", String.valueOf(insAmtPayable));
                    dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                    dataMap.put("PAID_DATE", currDt.clone());
                    dataMap.put("FORFEIT_BONUS",totReversalBonusAmt); // 2093
                    dataMap.put("FORFEIT_RECOVERY",totForfeitBonusAmt);
                    if (isRdoPrizedMember_Yes() == true) {
                        dataMap.put("PRIZED_MEMBER", "Y");
                    } else {
                        dataMap.put("PRIZED_MEMBER", "N");
                    }

                    //Insert Narration
                    whereMap = new HashMap();
                    Date nextInsDt = null;
                    whereMap.put("SCHEME_NAME", scheme_Name);
                    whereMap.put("DIVISION_NO", divNo);
                    whereMap.put("INSTALLMENT_NO", noOfInsPaid);
                    lst = sqlMap.executeQueryForList("getSelectInstUptoPaid", whereMap);
                    if (lst != null && lst.size() > 0) {
                        whereMap = (HashMap) lst.get(0);
                        nextInsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("NEXT_INSTALLMENT_DATE")));
                    } else {
                        startDate.setDate(instDay);
                        int stMonth = startDate.getMonth();
                        startDate.setMonth(stMonth + (int) noOfInsPaid - 1);
                        nextInsDt = startDate;
                    }
                    String narration = "";
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                    narration = "Inst#" + (noOfInsPaid + 1);
                    Date dt = DateUtil.addDays(nextInsDt, 30);
                    narration += " " + sdf.format(dt);
                    //System.out.println("######narration : " + narration);
                    dataMap.put("NARRATION", narration);
                    dataMap.put("BONUS", String.valueOf(totBonusAmt));
                    dataMap.put("DISCOUNT", String.valueOf(totDiscAmt));
                    dataMap.put("PENAL", CommonUtil.convertObjToDouble(penalAmt));
                    chittalPenalAmtMap.put(chittalNo,penalAmt);
                    dataMap.put("NET_AMOUNT", CommonUtil.convertObjToDouble(netAmt));
                    dataMap.put("INITIATED_BRANCH", _branchCode);
                    /* if (bonusFirstInst.equals("N")) {//nithya commented and made this condition common to both advance_collection Y & N
                     pendingInst += 1;
                     }*/
                    pendingInst += 1;//nithyaaa
                    rowList.add(String.valueOf(noOfInstPay + "/" + pendingInst));
                    rowList.add(dataMap.get("INST_AMT"));
                    rowList.add(String.valueOf(totBonusAmt));
                    rowList.add(String.valueOf(totDiscAmt));
                    rowList.add(String.valueOf(penalAmt));
                    rowList.add(dataMap.get("PROD_TYPE"));
                    rowList.add(dataMap.get("PROD_ID"));
                    rowList.add(dataMap.get("DR_ACT_NO"));
                    rowList.add(dataMap.get("AVAILABLE_BALANCE"));
                    rowList.add(CommonUtil.convertObjToDouble(netAmt));
                    rowList.add(CommonUtil.convertObjToDouble(totForfeitBonusAmt));
//                    if(pendingInst != 0){
                    tableList.add(rowList);
//                    }
                    //Added by sreekrishnan for min bal checking
                    drAcMinBal.put(dataMap.get("DR_ACT_NO"), CommonUtil.convertObjToDouble(dataMap.get("MIN_BALANCE")));
                }
                finalMap.put("STANDING_LIST", standInsList);
                finalMap.put("TAB_LIST", tableList);
                finalMap.put("DR_AC_MIN_BAL", drAcMinBal);
                finalMap.put("FORFEIT_BONUS",totReversalBonusAmt); //2093
                finalMap.put("CHITTAL_PENAL_AMT_MAP",chittalPenalAmtMap);
            }
        } catch (Exception e) {
        }
        return finalMap;
    }
    
        private HashMap calculateMDSStandingInstruction(String scheme_Name, String chittalNo, String sub_No, int divNo, int noOfInsPaid, int instDay, HashMap productMap,
            long pendingInst, long pendingInstallment, String bonusFirstInst, double insAmt, long noOfInstPay, double availableBalance,int curInstNo) {
        HashMap calcMap = new HashMap();
        int startNoForPenal = 0;
        int addNo = 1;
        bonusAmountList = new ArrayList();
        ForfeitebonusAmountList =  new ArrayList();
        penalRealList = new ArrayList();
        penalList = new ArrayList();
        instList = new ArrayList();
        narrationList = new ArrayList();
        HashMap forfietMap = new HashMap();
        int firstInst_No = -1;
        try {
            if (bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                startNoForPenal = 1;
                addNo = 0;
                firstInst_No = 0;
            }
            Rounding rod = new Rounding();
            String calculateIntOn = "";
            long totDiscAmt = 0;
            long insDueAmt = 0;
            long penalAmt = 0;
            double netAmt = 0;
            double insAmtPayable = 0;
            double totBonusAmt = 0;
            double totForfeitBonusAmt = 0;
            double totReversalBonusAmt = 0.0; //16-07-2020
            double bonusAmt = 0;
            String penalIntType = "";
            //long penalValue = 0;
            double penalValue = 0;
            String penalGraceType = "";
            double penalGraceValue = 0;
            String penalCalcBaseOn = "";
            Date instDate = null;
            long diffDayPending = 0;
            Date currDate = (Date) currDt.clone();
            Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
            boolean bonusAvailabe = true;
            if (pendingInst >= 0) {              //pending installment calculation starts... 
                insDueAmt = (long) insAmt * pendingInst;
                int totPendingInst = (int) pendingInst;
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (isRdoPrizedMember_Yes() == true) {
                    if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                    if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                        pendingInst -= penalGraceValue;
                    }
                } else if (isRdoPrizedMember_No() == true) {
                    if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                        calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                    }
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_GRACE_PERIOD"));
                    if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                        pendingInst -= penalGraceValue;
                    }
                }
                List bonusAmout = new ArrayList();//Added By Nidhin Penal calculation for Installment Amount
                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                    //double instAmount = 0.0;
                    HashMap nextInstMaps = null;
                    for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                        nextInstMaps = new HashMap();
                        nextInstMaps.put("SCHEME_NAME", scheme_Name);
                        nextInstMaps.put("DIVISION_NO", divNo);
                        nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                        List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                        if (listRec != null && listRec.size() > 0) {
                            nextInstMaps = (HashMap) listRec.get(0);
                        }
                        if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                            bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                        }

                    }
                }
                
                //2093 Starts
                // Start
                if (bonusFirstInst.equals("Y")) {
                    HashMap nextInstMaps = null;
                    LinkedHashMap installmentBonusMap = new LinkedHashMap();
                    for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                        int installmentNo = i + noOfInsPaid + addNo + firstInst_No;
                        nextInstMaps = new HashMap();
                        nextInstMaps.put("SCHEME_NAME", scheme_Name);
                        nextInstMaps.put("DIVISION_NO", divNo);
                        nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                        List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                        if (listRec != null && listRec.size() > 0) {
                            nextInstMaps = (HashMap) listRec.get(0);
                        }
                        if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {                            
                                installmentBonusMap.put(installmentNo, CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));                            
                        }
                    }
                    MDSbonusMap.put(chittalNo, installmentBonusMap);
                }
                // End
                // End

                long prependingInst = pendingInst;
                if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                        HashMap nextActMap = new HashMap();
                        nextActMap.put("SCHEME_NAME", scheme_Name);
                        nextActMap.put("DIVISION_NO", divNo);
                        nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(curInstNo));
                        List listAuc = sqlMap.executeQueryForList("getSelectNextAuctDate", nextActMap);
                        if (listAuc != null && listAuc.size() > 0) {
                            nextActMap = (HashMap) listAuc.get(0);
                        }
                        Date drawAuctDate = null;
                        if (nextActMap.containsKey("DRAW_AUCTION_DATE")) {
                            drawAuctDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                        }
                        //System.out.println("DateUtil.dateDiff(drawAuctDate, currDate" + DateUtil.dateDiff(drawAuctDate, currDt));
                         int addingGraceDays = CommonUtil.convertObjToInt(penalGraceValue);
                         //System.out.println("addingGraceDays :: " + addingGraceDays);
                         //System.out.println("DateUtil.dateDiff(DateUtil.addDays(drawAuctDate,addingGraceDays), currDt) :: " + DateUtil.dateDiff(DateUtil.addDays(drawAuctDate,addingGraceDays), currDt));
                         if (DateUtil.dateDiff(DateUtil.addDays(drawAuctDate,addingGraceDays), currDt) <= 0) {
                            prependingInst = prependingInst - 1;
                        }
                         //System.out.println("prepending chittal No :: " + chittalNo);
                         //System.out.println("prependingInst here after nithya :: " + prependingInst);
                    }
                //System.out.println("startNoForPenal -- :: " + startNoForPenal);
                
                double instAmount = 0.0;
                for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                    if (calculateIntOn.equals("Installment Amount")) {// Modified by nithya on 02-01-2018 for 0008272: Advance collection issue_MDS [ for solving array index out of bound exception ]
                        int predefinedIterator = j - 1;
                        instAmount = 0.0;
                        instAmount = CommonUtil.convertObjToDouble(insAmt);
                        if (bonusAmout != null && bonusAmout.size() > 0) {
                            if (bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(predefinedIterator));
                            } else {
                                instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j));
                            }
                        }
                    }
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", scheme_Name);
                    nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                    nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        //Changed by sreekrishnan
                        if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                            instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("DRAW_AUCTION_DATE")));
                        } else {
                            instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        }
                        diffDayPending = DateUtil.dateDiff(instDate, currDate);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        instDate = setProperDtFormat(instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", _branchCode);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            boolean isHoliday = false;
                            boolean isWeekOff = false;
                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                            if (Holiday != null || weeklyOf != null) {
                                if (Holiday != null) {
                                    isHoliday = Holiday.size() > 0 ? true : false;
                                }
                                if (weeklyOf != null) {
                                    isWeekOff = weeklyOf.size() > 0 ? true : false;
                                }
                                if (isHoliday || isWeekOff) {
                                    if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                        diffDayPending -= 1;
                                        instDate.setDate(instDate.getDate() + 1);
                                    } else {
                                        diffDayPending += 1;
                                        instDate.setDate(instDate.getDate() - 1);
                                    }
                                    holidayMap.put("NEXT_DATE", instDate);
                                    checkHoliday = true;
                                } else {
                                    checkHoliday = false;
                                }
                            } else {
                                checkHoliday = false;
                            }
                        }
                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        System.out.println("diffDayPending...." + diffDayPending + "penalValue" + penalValue + "instAMy" + insAmt);
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += (diffDayPending * penalValue * instAmount) / 36500;
                                        } else {
                                            calc += (diffDayPending * penalValue * insAmt) / 36500;
                                        }
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue * instFrequency) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        } else {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        }
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (pendingInst == 0) {
                                        pendingInst = 1;
                                    }
                                    
                                    if (prependingInst == 0) {
                                        prependingInst = 1;
                                    }
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        } else {
                                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                
                                                if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                    calc += ((instAmount * penalValue) / 1200.0) * prependingInst--;
                                                } else {
                                                    calc += ((insAmt * penalValue) / 1200.0) * prependingInst--;
                                                }
                                                
                                        } else {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc += ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                            } else {
                                                calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        }
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue * instFrequency) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc = (double) ((pendingInst * (pendingInst + 1) / 2) * instAmount * penalValue) * 7 / 36500;
                                            } else {
                                                calc = (double) ((pendingInst * (pendingInst + 1) / 2) * insAmt * penalValue) * 7 / 36500;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        } else {
                                            if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                                calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        }
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                        penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                    }
                                }
                            }
                        }
                        //After Scheme End Date Penal Calculating
                        if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, currDt) > 0)) {
                            //Below portion of code added by Jeffin John on 16-05-2014 for Mantis ID- 8858
                            HashMap c_hash = new HashMap();
                            c_hash.put("SCHEME_NAME", getTxtSchemeName());
                            List closedPenal = sqlMap.executeQueryForList("getClosedRate", c_hash);
                            double clodespenalRt = 0.0;
                            if (closedPenal != null && closedPenal.size() > 0) {
                                for (int i = 0; i < closedPenal.size(); i++) {
                                    HashMap penalRate = new HashMap();
                                    penalRate = (HashMap) closedPenal.get(0);
                                    if (penalRate.containsKey("CLOSED_PENAL") && penalRate.get("CLOSED_PENAL") != null) {
                                        clodespenalRt = Double.parseDouble(((HashMap) closedPenal.get(0)).get("CLOSED_PENAL").toString());
                                    }
                                }
                            }
                            //Code ends here
                            //System.out.println("#### endDate : " + endDate);
                            // Commented the block and rewritten by nithya on 25-01-2018 for 8213
//                        if (penalIntType.equals("Percent")){
//                            diffDayPending = DateUtil.dateDiff(endDate, curr_dt)+1;
//                            System.out.println("#### endDate_diffDayPending : " + diffDayPending);
//                            System.out.println("calc... "+ calc);
//                            calc += (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
//                            if(penalList.size()>0){
//                                double calcAmt = (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
//                                double closedAmt = CommonUtil.convertObjToDouble(penalList.get(penalList.size()-1))+calcAmt;
//                                penalList.remove(penalList.size()-1);
//                                penalList.add(rod.getNearest((double) (closedAmt * 100), 100) / 100);
//                            }
//                        }                        
                            if (penalIntType.equals("Percent")) {
                                Date clsDate = new Date();
                                if (bonusFirstInst.equals("Y")) {
                                    clsDate = DateUtil.addDays(endDate, CommonUtil.convertObjToInt(penalGraceValue));
                                } else {
                                    clsDate = DateUtil.addDays(endDate, (30 + CommonUtil.convertObjToInt(penalGraceValue)));
                                }
                                //System.out.println("penalGraceValue :: " + penalGraceValue);
                                diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (30 + CommonUtil.convertObjToInt(penalGraceValue))), currDate);
                                if (bonusFirstInst.equals("Y")) {
                                    //System.out.println("advance collection --- " + bonusFirstInst);
                                    diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (CommonUtil.convertObjToInt(penalGraceValue))), currDate);
                                }
                                HashMap holidayCloseMap = new HashMap();
                                boolean checkHolidayAftrClosure = true;
                                //System.out.println("instDate   " + clsDate);
                                clsDate = setProperDtFormat(clsDate);
                                //System.out.println("instDate   " + clsDate);
                                holidayCloseMap.put("NEXT_DATE", clsDate);
                                holidayCloseMap.put("BRANCH_CODE", _branchCode);
                                while (checkHolidayAftrClosure) {
                                    boolean tholiday = false;
                                    //System.out.println("enterytothecheckholiday" + checkHolidayAftrClosure);
                                    List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayCloseMap);
                                    List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayCloseMap);
                                    boolean isHoliday = Holiday.size() > 0 ? true : false;
                                    boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                    if (isHoliday || isWeekOff) {
                                        //System.out.println("#### diffDay Holiday True : " + diffDayPending);
                                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                            diffDayPending -= 1;
                                            clsDate.setDate(clsDate.getDate() + 1);
                                        } else {
                                            diffDayPending += 1;
                                            clsDate.setDate(clsDate.getDate() - 1);
                                        }
                                        holidayCloseMap.put("NEXT_DATE", clsDate);
                                        checkHolidayAftrClosure = true;
                                        //System.out.println("#### holidayMap : " + holidayCloseMap);
                                    } else {
                                        //System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                        checkHolidayAftrClosure = false;
                                    }
                                }
                                //System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                                //System.out.println("calc... " + calc);
                                if (diffDayPending > penalGraceValue) {
                                    calc += (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
                                }
                                if (penalList.size() > 0) {
                                    double calcAmt = (double) ((((insAmt * noOfInstPay * clodespenalRt) / 100.0) * diffDayPending) / 365);
                                    double closedAmt = CommonUtil.convertObjToDouble(penalList.get(penalList.size() - 1)) + calcAmt;
                                    penalList.remove(penalList.size() - 1);
                                    penalList.add(rod.getNearest((double) (closedAmt * 100), 100) / 100);
                                }
                            }
                            // End
                            // Absolute Not Required...
                        }
                    }
                }
                if (calc > 0) {
                    
                    penalAmt = (long) (calc + 0.5);
                    System.out.println("@#@#@#@#@# penalAmt : "+penalAmt);
                }
            }//pending installment calculation ends...


            //Discount calculation details Starts...
            for (int k = 0; k < noOfInstPay; k++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", scheme_Name);
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                if (listRec == null || listRec.size() == 0) {
                    Date curDate = (Date) currDate.clone();
                    int curMonth = curDate.getMonth();
                    curDate.setMonth(curMonth + k + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    long diffDay = DateUtil.dateDiff(instDate, currDate);
                    //Holiday Checking - Added By Suresh
                    HashMap holidayMap = new HashMap();
                    boolean checkHoliday = true;
                    //System.out.println("instDate   " + instDate);
                    instDate = setProperDtFormat(instDate);
                    //System.out.println("instDate   " + instDate);
                    holidayMap.put("NEXT_DATE", instDate);
                    holidayMap.put("BRANCH_CODE", _branchCode);
                    while (checkHoliday) {
                        boolean tholiday = false;
                        boolean isWeekOff = false;
                        System.out.println("enterytothecheckholiday12" + checkHoliday);
                        boolean isHoliday = false;
                        List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                        List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                        if (Holiday != null || weeklyOf != null) {
                            if (Holiday != null) {
                                isHoliday = Holiday.size() > 0 ? true : false;
                            }
                            if (weeklyOf != null) {
                                isWeekOff = weeklyOf.size() > 0 ? true : false;
                            }
                            if (isHoliday || isWeekOff) {
                                //System.out.println("#### diffDay Holiday True : " + diffDay);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDay -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDay += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                //System.out.println("#### holidayMap : " + holidayMap);
                            } else {
                                //System.out.println("#### diffDay Holiday False : " + diffDay);
                                checkHoliday = false;
                            }
                        } else {
                            checkHoliday = false;
                        }
                    }
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                        if (discount != null && !discount.equals("") && discount.equals("Y")) {
                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                            if (isRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
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
                                } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
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
                            } else if (isRdoPrizedMember_No() == true) {//discount calculation non prized person...
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
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
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
            //Bonus calculation details Starts...
            for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                
                // Check for bonus reversal
                boolean reversalBonusExists = false;
                if (productMap.containsKey("BANK_ADV_FORFIET") && productMap.get("BANK_ADV_FORFIET") != null && productMap.get("BANK_ADV_FORFIET").equals(("N"))) {
                    HashMap reversalMap = new HashMap();
                    reversalMap.put("CHITTAL_NO", chittalNo);
                    reversalMap.put("SCHEME_NAME", scheme_Name);
                    reversalMap.put("DIVISION_NO", divNo);
                    reversalMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                    List reversalList = sqlMap.executeQueryForList("getSelectForfietAmountForChittal", reversalMap);
                    if (reversalList != null && reversalList.size() > 0) {
                        reversalBonusExists = true;
                    }
                }
                // End
                
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", scheme_Name);
                nextInstMap.put("DIVISION_NO", divNo);
                nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                int payInstallNo = l + noOfInsPaid + addNo + firstInst_No + 1;
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.size() == 0) {
                    Date curDate = (Date) currDate.clone();
                    int curMonth = curDate.getMonth();
                    curDate.setMonth(curMonth + l + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                    if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                        HashMap whereMap = new HashMap();
                        int noOfCoChittal = 0;
                        whereMap.put("CHITTAL_NUMBER", chittalNo);
                        whereMap.put("SCHEME_NAMES", scheme_Name);
                        List applicationLst = sqlMap.executeQueryForList("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                        if (applicationLst != null && applicationLst.size() > 0) {
                            noOfCoChittal = applicationLst.size();
                            bonusAmt = bonusAmt / noOfCoChittal;
                        }
                    }
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        //Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        } else {
                            bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                        }
                    }
                    long diffDay = DateUtil.dateDiff(instDate, currDate);
                    //Holiday Checking - Added By Suresh
                    HashMap holidayMap = new HashMap();
                    boolean checkHoliday = true;
                    //System.out.println("instDate   " + instDate);
                    instDate = setProperDtFormat(instDate);
                    //System.out.println("instDate   " + instDate);
                    holidayMap.put("NEXT_DATE", instDate);
                    holidayMap.put("BRANCH_CODE", _branchCode);
                    while (checkHoliday) {
                        boolean tholiday = false;
                        boolean isHoliday = false;
                        boolean isWeekOff = false;
                        //System.out.println("enterytothecheckholiday21" + checkHoliday);
                        List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                        List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                        if (Holiday != null || weeklyOf != null) {
                            if (Holiday != null) {
                                isHoliday = Holiday.size() > 0 ? true : false;
                            }
                            if (weeklyOf != null) {
                                isWeekOff = weeklyOf.size() > 0 ? true : false;
                            }
                            if (isHoliday || isWeekOff) {
                                //System.out.println("#### diffDay Holiday True : " + diffDay);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDay -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDay += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                //System.out.println("#### holidayMap : " + holidayMap);
                            } else {
                                //System.out.println("#### diffDay Holiday False : " + diffDay);
                                checkHoliday = false;
                            }
                        } else {
                            checkHoliday = false;
                        }

                    }
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        String prizedDefaultYesN = "";
                        if (productMap.containsKey("PRIZED_DEFAULTERS") && productMap.get("PRIZED_DEFAULTERS") != null) {
                            prizedDefaultYesN = CommonUtil.convertObjToStr(productMap.get("PRIZED_DEFAULTERS"));
                        }
                        if (bonusAvailabe == true) {
                            HashMap nextActMap = new HashMap();
                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                            String bonusPrzInMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                            if (isRdoPrizedMember_Yes() == true) {
                                nextActMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(scheme_Name));
                                nextActMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                                nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(l + noOfInsPaid));
                                List listAuc = sqlMap.executeQueryForList("getSelectNextAuctDate", nextActMap);
                                if (listAuc != null && listAuc.size() > 0) {
                                    nextActMap = (HashMap) listAuc.get(0);
                                }
                                Date drawAuctionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                                //System.out.println("Date acution d" + drawAuctionDate);
                                Calendar cal = null;//Added By Nidhin holiday checking not correct
                                Date newDate = null;
                                if (bonusPrzInMonth != null && bonusPrzInMonth.equalsIgnoreCase("M")) {
                                    cal = Calendar.getInstance();
                                    cal.setTime(drawAuctionDate);
                                    cal.add(Calendar.MONTH, CommonUtil.convertObjToInt(bonusPrizedValue));
                                    newDate = cal.getTime();
                                } else {
                                    newDate = DateUtil.addDays(drawAuctionDate, CommonUtil.convertObjToInt(bonusPrizedValue));
                                }
                                long dateDiff = DateUtil.dateDiff(currDt, newDate);
                                //Holiday checking Added  By Nidhin 19/11/2014
                                HashMap holidayCheckMap = new HashMap();
                                boolean checkForHoliday = true;
                                newDate = setProperDtFormat(newDate);
                                holidayCheckMap.put("NEXT_DATE", newDate);
                                holidayCheckMap.put("BRANCH_CODE", _branchCode);
                                while (checkForHoliday) {
                                    boolean isHoliday = false;
                                    boolean isWeekOff = false;
                                    //System.out.println("233334");
                                    List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayCheckMap);
                                    List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayCheckMap);
                                    if (Holiday != null || weeklyOf != null) {
                                        if (Holiday != null) {
                                            isHoliday = Holiday.size() > 0 ? true : false;
                                        }
                                        if (weeklyOf != null) {
                                            isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        }
                                        if (isHoliday || isWeekOff) {
                                            //System.out.println("#### diffDay Holiday True : Bonus" + dateDiff);	
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                dateDiff += 1;
                                                newDate.setDate(newDate.getDate() + 1);
                                            } else {
                                                dateDiff -= 1;
                                                newDate.setDate(newDate.getDate() - 1);
                                            }
                                            holidayCheckMap.put("NEXT_DATE", newDate);
                                            checkForHoliday = true;
                                        } else {
                                            checkForHoliday = false;
                                        }
                                    } else {
                                        checkForHoliday = false;
                                    }

                                }
                                //End for HoliDay Checking
                                HashMap whereMap = new HashMap();
                                whereMap.put("CHITTAL_NO", chittalNo);
                                whereMap.put("SUB_NO", CommonUtil.convertObjToInt(sub_No));
                                whereMap.put("SCHEME_NAME", scheme_Name);
                                List paymentList = sqlMap.executeQueryForList("getSelectMDSPaymentDetails", whereMap);
                                if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                    System.out.println("###### NO BONUS FOR PRODUCT PARAMETER");
                                } else if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) { //akhila
                                    System.out.println("in else");
                                    String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                    String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                    String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                    String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                    //long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                    if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                    } else {
                                        if (productMap.containsKey("FORFEITE_HD_Y_N") && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                            //System.out.println("BonusAmount Yes" + bonusAmt);
                                            forfietMap.put(payInstallNo,bonusAmt);
                                        }
                                        if(reversalBonusExists){                                                
                                            totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                        }
                                    }
                                } else if (productMap.get("PRIZED_OWNER_BONUS").equals("Y") || productMap.get("PRIZED_OWNER_BONUS").equals("N"))//is work prized owner yes or no
                                {
                                    String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                        }else{
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                            if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                forfietMap.put(payInstallNo,bonusAmt);
                                            }
                                            if(reversalBonusExists){                                                
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                        }
                                        //bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                    } else {
                                        if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                //System.out.println("BonusAmount Yes23" + bonusAmt);
                                                totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                                forfietMap.put(payInstallNo,bonusAmt);
                                        }
                                        if (reversalBonusExists) {
                                            totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                        }
                                    }

                                } else {
                                }
                                //akhila
                            } else if (isRdoPrizedMember_No() == true) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                } else {
                                    if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                        ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                        //System.out.println("BonusAmount Yes23" + bonusAmt);
                                        totForfeitBonusAmt = totForfeitBonusAmt + bonusAmt;
                                        forfietMap.put(payInstallNo,bonusAmt);
                                    }
                                    if (reversalBonusExists) {
                                        totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                    }
                                }
                            }
                        }

                    }
                }
                //System.out.println("totBonusAmt  ssss  "+totBonusAmt);
                bonusAmt = 0;
            }
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                //Rounding rod = new Rounding();
                if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                    totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                } else {
                    totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                }
            }
            //System.out.println("totBonusAmt  qqqqqq  "+totBonusAmt);
            netAmt = (double) (noOfInstPay * insAmt) + penalAmt - (totBonusAmt + totDiscAmt);
            insAmtPayable = (noOfInstPay * insAmt) - (totBonusAmt + totDiscAmt);
            //System.out.println("####### netAmt        : " + netAmt);
            //System.out.println("####### insAmtPayable : " + insAmtPayable);
            //System.out.println("####### noOfInstPay : " + noOfInstPay);
            if (netAmt > availableBalance) {
                if (noOfInstPay != 1) {
                    noOfInstPay = noOfInstPay - 1;
                    pendingInst = pendingInstallment;
                    HashMap testMap = calculateMDSStandingInstruction(scheme_Name, chittalNo, sub_No, divNo, noOfInsPaid, instDay, productMap,
                            pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance,curInstNo);
                    //System.out.println("test mappp"+testMap);
                    return testMap;
                }
            }
            //Temp for debit account balance checking
            if (tempDebitAc.containsKey(productMap.get("DR_ACT_NO"))) {
                if (CommonUtil.convertObjToDouble(tempDebitAc.get(productMap.get("DR_ACT_NO"))) < netAmt) {
                    if ((noOfInstPay = noOfInstPay - 1) > 0) {
                        pendingInst = pendingInstallment;
                        HashMap testMap2 = calculateMDSStandingInstruction(scheme_Name, chittalNo, sub_No, divNo, noOfInsPaid, instDay, productMap,
                                pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance,curInstNo);
                        return testMap2;
                    } else {                       //If you want set 0 as minimum noOfInst then comment the else part.
                        noOfInstPay = 1;
                    }
                } else {
                    tempDebitAc.put(productMap.get("DR_ACT_NO"), CommonUtil.convertObjToDouble(tempDebitAc.get(productMap.get("DR_ACT_NO"))) - netAmt);
                }
            } else {
                tempDebitAc.put(productMap.get("DR_ACT_NO"), availableBalance - netAmt);
            }
            //Code ends..        
            double bonusN = 0.0;
            for (int h = 0; h < bonusAmountList.size(); h++) {
                bonusN = CommonUtil.convertObjToDouble(bonusAmountList.get(h));
            }
            //For spliting transcations..                
            if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                splitTransMap = new HashMap();
                ArrayList RealBonusAmountList = new ArrayList();
                ArrayList RealPenalList = new ArrayList();
                ArrayList RealInstList = new ArrayList();
                //int noOfInstToPay = CommonUtil.convertObjToInt(txtNoOfInstToPaay.getText());
                //for (int i = 0; i < noOfInstToPay; i++) {
                //   setNarrationToSplitTransaction(i);
                //}
                if (penalList != null && penalList.size() > 0) {
                    double d = 0;
                    double firVal = CommonUtil.convertObjToDouble(penalList.get(0));
                    Collections.reverse(penalList);
                    for (int i = 0; i <= penalList.size(); i++) {
                        if (i + 1 < penalList.size()) {
                            d = CommonUtil.convertObjToDouble(penalList.get(i)) - CommonUtil.convertObjToDouble(penalList.get(i + 1));
                            penalRealList.add(d);
                        }
                    }
                    penalRealList.add(firVal);
                }
                Collections.reverse(penalRealList);
                Collections.reverse(bonusAmountList);
                if (penalRealList.size() != noOfInstPay) {
                    penalRealList.add(0);
                }

                //for (int p = 0; p < penalRealList.size(); p++) {
                if (bonusAmountList.size() != penalRealList.size()) {
                    int count = CommonUtil.convertObjToInt(penalRealList.size() - bonusAmountList.size());
                    for (int i = 0; i < count; i++) {
                        bonusAmountList.add(0);
                    }
                }
                double insAmta = 0.0;
                for (int h = 0; h < bonusAmountList.size(); h++) {
                    double instAmounts = insAmt;
                    instAmounts -= CommonUtil.convertObjToDouble(bonusAmountList.get(h));
                    insAmta = instAmounts;
                    instList.add(insAmta);
                }
                if (instList.size() != penalRealList.size()) {
                    int count = 0;
                    int insSize = instList.size();
                    int penalSize = penalRealList.size();
                    count = insSize - penalSize;
                    for (int i = 0; i < count; i++) {
                        penalRealList.add(0);
                    }

                }
                //}    
                for (int k = 0; k < noOfInstPay; k++) {
                    if (noOfInstPay > 0) {
                        RealBonusAmountList.add(bonusAmountList.get(CommonUtil.convertObjToInt(k)));
                        RealPenalList.add(penalRealList.get(CommonUtil.convertObjToInt(k)));
                        RealInstList.add(instList.get(CommonUtil.convertObjToInt(k)));
                    }
                }
                //System.out.println("before reverse :: " + RealBonusAmountList);
                Collections.reverse(RealBonusAmountList);
                Collections.reverse(RealInstList);
                //System.out.println("after reverse :: " + RealBonusAmountList);
                splitTransMap.put("BONUS_AMT_LIST", RealBonusAmountList);
                splitTransMap.put("INST_AMT_LIST", RealInstList);
                splitTransMap.put("PENAL_AMT_LIST", RealPenalList);
                splitTransMap.put("NARRATION_LIST", narrationList);
                splitTransMap.put("CHITTAL_NO", chittalNo);
                splitTransMap.put("CHITTAL_NO", chittalNo);
                splitTransMap.put("SUB_NO", CommonUtil.convertObjToInt(sub_No));
                splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
                splitTransMap.put("INSTALL_NO", noOfInsPaid);
                splitTransMap.put("FORFIET_AMT_MAP",forfietMap);
                finalSplitList.add(splitTransMap);
            }
            //
            calcMap.put("NET_AMOUNT", String.valueOf(netAmt));
            calcMap.put("INST_AMOUNT_PAYABLE", String.valueOf(insAmtPayable));
            calcMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
            calcMap.put("BONUS", String.valueOf(totBonusAmt));
            calcMap.put("FORFEIT_BONUS", String.valueOf(totReversalBonusAmt)); //2093
            calcMap.put("FORFEIT_RECOVERY", String.valueOf(totForfeitBonusAmt)); //2093
            calcMap.put("DISCOUNT", String.valueOf(totDiscAmt));
            // calcMap.put("PENAL", CurrencyValidation.formatCrore(String.valueOf(penalAmt)));
            calcMap.put("PENAL", String.valueOf(penalAmt));
            calcMap.put("BONUS_NEW", String.valueOf(bonusN));
            //calcMap.put("AVAILABLE_BALANCE",availableBalance-netAmt);
            //System.out.println("####### calcMap iiiii : " + calcMap + "Chittal Numner :"+chittalNo);
        } catch (Exception e) {
        }
        return calcMap;
    }
    
    private void executeTransactionPart(HashMap map) throws Exception {
        try {
            if (standingLst != null && standingLst.size() > 0) {
                String advanceCollection = "";
                String bonusRoundOff = "";
                schemeName = CommonUtil.convertObjToStr(map.get("SCHEME_NAME"));
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                SmsConfigDAO smsDAO = new SmsConfigDAO();
//                System.out.println("@##$#$% BRANCH_ID   #### :" + BRANCH_ID);
//                System.out.println("@##$#$% schemeName  #### :" + schemeName);
                HashMap applicationMap = new HashMap();
                HashMap unMarkleanMap = new HashMap();
                applicationMap.put("SCHEME_NAME", schemeName);
                List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", applicationMap);    // Acc Head
                if (lst != null && lst.size() > 0) {
                    applicationMap = (HashMap) lst.get(0);
                    advanceCollection = CommonUtil.convertObjToStr(applicationMap.get("ADVANCE_COLLECTION"));
                    if(applicationMap.containsKey("BONUS_ROUNDING") && applicationMap.get("BONUS_ROUNDING") != null && applicationMap.get("BONUS_ROUNDING").equals("Y")){
                       bonusRoundOff = CommonUtil.convertObjToStr(applicationMap.get("BONUS_ROUNDOFF")); 
                    }else{
                       bonusRoundOff = "NO_ROUND_OFF";  
                    }                    
                }
            //    System.out.println("@##$#$% applicationMap #### :" + applicationMap);
                HashMap MDSBonusMap = new HashMap(); //2093
                if(map.containsKey("MDS_BONUS_MAP") && map.get("MDS_BONUS_MAP") != null){
                  MDSBonusMap = (HashMap) map.get("MDS_BONUS_MAP");
                }
                for (int i = 0; i < standingLst.size(); i++) {
                    mdsReceiptEntryTO = new MDSReceiptEntryTO();
                    HashMap dataMap = new HashMap();
                    try {
                        sqlMap.startTransaction();
                        mdsReceiptEntryTO = new MDSReceiptEntryTO();
                        double forfeitBonusAmt = 0.0;
                    double normalforfeit = 0.0;
                        double instalAmt = 0.0;
                        double bonusAmt = 0.0;
                        double discountAmt = 0.0;
                        double penalAmt = 0.0;
                        double netAmt = 0.0;
                        double bonusNew = 0.0;
                        String chittalNo = "";
                        String subNo = "";
                        String prodType = "";
                        String prodId = "";
                        String debitAccNo = "";
                        int noOFinstToPay = 0;
                        int paidInst = 0;
                        HashMap leanFlagMap = new HashMap();
                        boolean leanFlag = false;
                        String transId = "";
                        double totalDebitAmt = 0.0;
                        dataMap = (HashMap) standingLst.get(i);
                        //      System.out.println("#####dataMap: " + dataMap);
                        instalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("INST_AMT")).replaceAll(",", "")).doubleValue();
                        bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS")).replaceAll(",", "")).doubleValue();
                        bonusNew = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS_NEW")).replaceAll(",", ""));
                        discountAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")).doubleValue();
                        penalAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")).doubleValue();
                        netAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")).doubleValue();
                        chittalNo = CommonUtil.convertObjToStr(dataMap.get("CHITTAL_NO"));
                        subNo = CommonUtil.convertObjToStr(dataMap.get("SUB_NO"));
                        prodType = CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE"));
                        prodId = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                        debitAccNo = CommonUtil.convertObjToStr(dataMap.get("DR_ACT_NO"));
                        if (dataMap.containsKey("FORFEIT_BONUS") && dataMap.get("FORFEIT_BONUS") != null) {
                            forfeitBonusAmt = CommonUtil.convertObjToDouble(dataMap.get("FORFEIT_BONUS"));
                        }
                    if(dataMap.containsKey("FORFEIT_RECOVERY") && dataMap.get("FORFEIT_RECOVERY") != null){
                        normalforfeit = CommonUtil.convertObjToDouble(dataMap.get("FORFEIT_RECOVERY"));
                    }
                        mdsReceiptEntryTO.setSchemeName(schemeName);
                        mdsReceiptEntryTO.setChittalNo(chittalNo);
                        mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(dataMap.get("SUB_NO")));
                        mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(dataMap.get("MEMBER_NAME")));
                        mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(dataMap.get("DIVISION_NO")));
                        mdsReceiptEntryTO.setChitStartDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("CHIT_START_DT"))));
                        mdsReceiptEntryTO.setChitEndDt(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("INSTALLMENT_DATE"))));
                        mdsReceiptEntryTO.setPaidDate(getProperDateFormat(CommonUtil.convertObjToStr(dataMap.get("PAID_DATE"))));
                        mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INSTALLMENTS")));
                        mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(dataMap.get("CURR_INST")));
                        mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT")));
                        mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(dataMap.get("PENDING_INST")));
                        mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(dataMap.get("PENDING_DUE_AMT")));
                        mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(dataMap.get("PRIZED_MEMBER")));
                        mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble("0"));
                        mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble("0"));
                        mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(dataMap.get("NO_OF_INST_PAY")));
                        mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(dataMap.get("INST_AMT_PAYABLE")));
                        mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PENAL")).replaceAll(",", "")));
                        HashMap bankAdvMap = new HashMap();//Nidhin
                        int instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
                        bankAdvMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSchemeName()));
                        bankAdvMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                        bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                        bankAdvMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(instNo));
                        bankAdvMap.put("TO_INSTALLMENT", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()));
                        List bankAdvanceLst = sqlMap.executeQueryForList("getSelectBankAdvanceBonusData", bankAdvMap);
                        if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) {
                            HashMap bonusMap = (HashMap) bankAdvanceLst.get(0);
                            if (bonusMap.containsKey("BONUS_AMOUNT") && bonusMap.get("BONUS_AMOUNT") != null) {
                                double bankBonusAmt = CommonUtil.convertObjToDouble(bonusMap.get("BONUS_AMOUNT"));
                                bonusAmt = bonusAmt - bankBonusAmt;
                            }
                        }
                        if ((bonusNew > 0 || bonusNew > 0.0) && bonusNew > bonusAmt) {//Jeffin
                            mdsReceiptEntryTO.setBonusAmtPayable(bonusNew);
                        } else {
                            if (dataMap.containsKey("BONUS") && dataMap.get("BONUS") != null) {
                                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("BONUS")).replaceAll(",", "")));
                            } else {
                                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble("0"));
                            }
                        }
                        // If advance collection
                        HashMap bonusDetailsMapifForfiet = new HashMap();
                        double bonusPayable = 0.0;
                        double normalInstallmentAmt = 0.0;
                        double bankAdvInsAmt = 0.0;
                        List advProcessInstList = new ArrayList();
                        if (advanceCollection.equalsIgnoreCase("Y")) {
                            bonusDetailsMapifForfiet = getBonusDetailsForfiet(MDSBonusMap, mdsReceiptEntryTO, dataMap, bonusRoundOff);
                            bonusPayable = CommonUtil.convertObjToDouble(bonusDetailsMapifForfiet.get("BONUS_PAYABLE"));
                            normalInstallmentAmt = CommonUtil.convertObjToDouble(bonusDetailsMapifForfiet.get("NORMAL_INTALLMENT_AMOUNT"));
                            bankAdvInsAmt = CommonUtil.convertObjToDouble(bonusDetailsMapifForfiet.get("BANK_ADVANCE_AMT"));
                            advProcessInstList = (ArrayList) bonusDetailsMapifForfiet.get("BANK_ADV_INST_LST");
                            mdsReceiptEntryTO.setBonusAmtPayable(bonusPayable);
                        }
                        mdsReceiptEntryTO.setForfeitBonusAmtPayable(forfeitBonusAmt);
                    if(normalforfeit > 0){
                      mdsReceiptEntryTO.setForfeitBonusAmtPayable(normalforfeit);  
                    }
                        mdsReceiptEntryTO.setBankAdvanceAmt(bankAdvInsAmt);
                        // End 
                        mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("DISCOUNT")).replaceAll(",", "")));
                        mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("NET_AMOUNT")).replaceAll(",", "")));
                        mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(dataMap.get("PAID_INST")).replaceAll(",", "")));
                        mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                        mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                        mdsReceiptEntryTO.setStatusDt((Date) currDt.clone());
                        mdsReceiptEntryTO.setAuthorizeStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                        mdsReceiptEntryTO.setAuthorizeBy((String) map.get(CommonConstants.USER_ID));
                        mdsReceiptEntryTO.setAuthorizeDt((Date) currDt.clone());
                        mdsReceiptEntryTO.setBranchCode(_branchCode);
                        mdsReceiptEntryTO.setNarration(CommonUtil.convertObjToStr(dataMap.get("NARRATION")));
                        mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("INITIATED_BRANCH")));
//                    System.out.println(i + " -" + "chittalNo   #### : " + chittalNo);
//                    System.out.println(i + " -" + "instalAmt   #### : " + instalAmt);
//                    System.out.println(i + " -" + "bonusAmt    #### : " + bonusAmt);
//                    System.out.println(i + " -" + "discountAmt #### : " + discountAmt);
//                    System.out.println(i + " -" + "penalAmt    #### : " + penalAmt);
//                    System.out.println(i + " -" + "netAmt      #### : " + netAmt);
//                    System.out.println(i + " -" + "prodType    #### : " + prodType);
//                    System.out.println(i + " -" + "prodId      #### : " + prodId);
//                    System.out.println(i + " -" + "debitAccNo  #### : " + debitAccNo);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                        TransactionTO transactionTO = new TransactionTO();
                        HashMap txMap = new HashMap();
                        HashMap transactionListMap = new HashMap();
                        HashMap transMap = new HashMap();
                        transactionTO.setProductType(prodType);
                        transactionTO.setDebitAcctNo(debitAccNo);
                        transactionTO.setProductId(prodId);
                        HashMap debitMap = new HashMap();
//                    if(!prodType.equals("") && prodType.equals("OA")){
//                        debitMap.put("ACT_NUM", debitAccNo);
//                        lst = sqlMap.executeQueryForList("getAccNoProdIdDet",debitMap);
//                        if(lst!=null && lst.size()>0){
//                            debitMap = (HashMap)lst.get(0);
//                            System.out.println("$#%$%#%$#$% debitMap $%#%## : "+debitMap);
//                        }
//                    }
                        if (!prodType.equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
                            debitMap.put("PROD_ID", prodId);
                            lst = sqlMap.executeQueryForList("getAccountHeadProd" + prodType, debitMap);
                            if (lst != null && lst.size() > 0) {
                                debitMap = (HashMap) lst.get(0);
                                // System.out.println("$#%$%#%$#$% debitMap $%#%## : " + debitMap);
                            }
                            HashMap debitBranchCodeMap = new HashMap();
                            debitBranchCodeMap.put("ACT_NUM", debitAccNo);
                            List lstdebitBranchCode = sqlMap.executeQueryForList("getSelectInterBranchCode", debitBranchCodeMap);
                            if (lstdebitBranchCode != null && lstdebitBranchCode.size() > 0) {
                                debitBranchCodeMap = (HashMap) lstdebitBranchCode.get(0);
                                transferTo.setBranchId(CommonUtil.convertObjToStr(debitBranchCodeMap.get("BRANCH_CODE")));
                                // System.out.println("debitBranchCodeMap : " + debitBranchCodeMap);
                            }
                        }

                        // Commented the code by nithya on 27-07-2020 for KD-2093
//                    if (netAmt > 0.0) {
//                      //  System.out.println("Transfer Started debit : ");
//                        transferTo.setInstrumentNo2("APPL_GL_TRANS");
//                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
//                            txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
//                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                            // added installment number at the end of particulars for 8366 on 29-11-2017
//                            //txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
//                            txMap.put(TransferTrans.PARTICULARS, mdsReceiptEntryTO.getNarration()+":"+schemeName + "-" + chittalNo + "_" + subNo);
//                        } else if (!transactionTO.getProductType().equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
////                            txMap.put(TransferTrans.DR_AC_HD, (String)debitMap.get("AC_HD_ID"));
//                            txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
//                            txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
//                            txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
//                            txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
//                            // added installment number at the end of particulars for 8366 on 29-11-2017
//                            //txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
//                            txMap.put(TransferTrans.PARTICULARS,mdsReceiptEntryTO.getNarration()+":"+schemeName + "-" + chittalNo + "_" + subNo);
//                        }
//                        txMap.put(TransferTrans.DR_BRANCH, transferTo.getBranchId());
//                        if (transactionTO.getProductType().equals("OA")) {
//                            txMap.put("TRANS_MOD_TYPE", "OA");
//                        } else if (transactionTO.getProductType().equals("AB")) {
//                            txMap.put("TRANS_MOD_TYPE", "AB");
//                        } else if (transactionTO.getProductType().equals("TD")) {
//                            txMap.put("TRANS_MOD_TYPE", "TD");
//                        } else if (transactionTO.getProductType().equals("SA")) {
//                            txMap.put("TRANS_MOD_TYPE", "SA");
//                        } else if (transactionTO.getProductType().equals("TL")) {
//                            txMap.put("TRANS_MOD_TYPE", "TL");
//                        } else if (transactionTO.getProductType().equals("AD")) {
//                            txMap.put("TRANS_MOD_TYPE", "AD");
//                        } else {
//                            txMap.put("TRANS_MOD_TYPE", "GL");
//                        }
//                        System.out.println("txMap1 : " + txMap + "netAmt :" + netAmt);
//                        if (transactionTO.getProductType().equals("AD")) {
//                            txMap.put("AUTHORIZEREMARKS", "DP");
//                        }
//                        txMap.put("generateSingleTransId", generateSingleTransId);
//                        transferTo = transactionDAO.addTransferDebitLocal(txMap, netAmt);
//                        transferTo.setInstrumentNo1("SI");
//                        transferTo.setTransId("-");
//                        transferTo.setBatchId("-");
//                        transferTo.setTransDt(currDt);
//                        //transferTo.setBranchId(BRANCH_ID);
//                        transferTo.setInitiatedBranch(BRANCH_ID);
//                        transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
//                        transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
//                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
//                        transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
//                        transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
//                        TxTransferTO.add(transferTo);
//                    }
                        //  System.out.println("TxTransferTO List 1 : " + TxTransferTO);
                        if (penalAmt > 0.0) {
                            System.out.println("penal Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("PENAL_INTEREST_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Penal " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
                            //     System.out.println("txMap2 : " + txMap + "penalAmt :" + penalAmt);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, penalAmt);
                        if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                            transferTo.setRec_mode("RP");
                        }
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transactionTO.setChequeNo("SERVICE_TAX");
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("PENAL_AMT");//31-10-2019
                            TxTransferTO.add(transferTo);
                            totalDebitAmt += penalAmt;
                        }
                        //  System.out.println("transferTo List 2 : " + TxTransferTO);

                        // 2093
                        if (bankAdvInsAmt > 0) {
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("BANKING_REP_PAY_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo + "  From : " + debitAccNo);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, bankAdvInsAmt);
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(chittalNo)));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("INSTALMENT_AMT");// Added by nithya on 31-10-2019 for KD-680
                            TxTransferTO.add(transferTo);
                            totalDebitAmt += bankAdvInsAmt;
                        }

                        if (forfeitBonusAmt > 0.0) {
                            System.out.println("Forfeit transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, (String) applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Bonus Reversal :" + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo + ":BANK ADVANCE");
                            //     System.out.println("txMap2 : " + txMap + "penalAmt :" + penalAmt);                        
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, forfeitBonusAmt);
                        if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                            transferTo.setRec_mode("RP");
                        }
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transactionTO.setChequeNo("SERVICE_TAX");
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("BONUS_REVERSAL");
                            TxTransferTO.add(transferTo);
                            totalDebitAmt += forfeitBonusAmt;
                        }

                        // End                    
                        double finalReceivingAmt = 0.0;
                        if (advanceCollection.equalsIgnoreCase("Y")) {
                            finalReceivingAmt = normalInstallmentAmt - mdsReceiptEntryTO.getBonusAmtPayable();
                        } else {
                            finalReceivingAmt = netAmt - penalAmt;
                        }
                        if (finalReceivingAmt > 0) {
                            //    System.out.println("Final Amount Transaction Started");
                            transferTo = new TxTransferTO();
                            txMap = new HashMap();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                            txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo + "  From : " + debitAccNo);
                            //  System.out.println("txMap3 : " + txMap + "finalReceivingAmt :" + finalReceivingAmt);
                            txMap.put("TRANS_MOD_TYPE", "MDS");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, finalReceivingAmt);
                        if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                            transferTo.setRec_mode("RP");
                        }
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            transferTo.setInstrumentNo2("INSTALMENT_AMT");//31-10-2019
                            TxTransferTO.add(transferTo);
                            totalDebitAmt += finalReceivingAmt;
                        }

                        // Moving netamount transaction to end by nithya for KD-2093
                        if (netAmt > 0.0) {
                            //  System.out.println("Transfer Started debit : ");
                            transferTo = new TxTransferTO();
                            transferTo.setInstrumentNo2("APPL_GL_TRANS");
                            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                // added installment number at the end of particulars for 8366 on 29-11-2017
                                //txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
                                txMap.put(TransferTrans.PARTICULARS, mdsReceiptEntryTO.getNarration() + ":" + schemeName + "-" + chittalNo + "_" + subNo);
                            } else if (!transactionTO.getProductType().equals("") && (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AB") || prodType.equals("TD"))) {
//                            txMap.put(TransferTrans.DR_AC_HD, (String)debitMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_AC_HD, (String) debitMap.get("AC_HEAD"));
                                txMap.put(TransferTrans.DR_ACT_NUM, transactionTO.getDebitAcctNo());
                                txMap.put(TransferTrans.DR_PROD_ID, transactionTO.getProductId());
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                                // added installment number at the end of particulars for 8366 on 29-11-2017
                                //txMap.put(TransferTrans.PARTICULARS, "Amount Trans to " + schemeName + "-" + chittalNo + "_" + subNo);
                                txMap.put(TransferTrans.PARTICULARS, mdsReceiptEntryTO.getNarration() + ":" + schemeName + "-" + chittalNo + "_" + subNo);
                            }
                            txMap.put(TransferTrans.DR_BRANCH, transferTo.getBranchId());
                            if (transactionTO.getProductType().equals("OA")) {
                                txMap.put("TRANS_MOD_TYPE", "OA");
                            } else if (transactionTO.getProductType().equals("AB")) {
                                txMap.put("TRANS_MOD_TYPE", "AB");
                            } else if (transactionTO.getProductType().equals("TD")) {
                                txMap.put("TRANS_MOD_TYPE", "TD");
                                HashMap interBranchCodeMap = new HashMap();
                                interBranchCodeMap.put("ACT_NUM", transactionTO.getDebitAcctNo());
                                List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                                if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                                    System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE")));
                                } else {
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                }
                            } else if (transactionTO.getProductType().equals("SA")) {
                                txMap.put("TRANS_MOD_TYPE", "SA");
                            } else if (transactionTO.getProductType().equals("TL")) {
                                txMap.put("TRANS_MOD_TYPE", "TL");
                            } else if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("TRANS_MOD_TYPE", "AD");
                            } else {
                                txMap.put("TRANS_MOD_TYPE", "GL");
                            }
                            System.out.println("txMap1 : " + txMap + "netAmt :" + netAmt);
                            if (transactionTO.getProductType().equals("AD")) {
                                txMap.put("AUTHORIZEREMARKS", "DP");
                            }
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            if (advanceCollection.equalsIgnoreCase("Y")) {
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalDebitAmt);
                            } else {
                                transferTo = transactionDAO.addTransferDebitLocal(txMap, netAmt);
                            }
                        if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                            transferTo.setRec_mode("RP");
                        }
                            transferTo.setInstrumentNo1("SI");
                            transferTo.setTransId("-");
                            transferTo.setBatchId("-");
                            transferTo.setTransDt(currDt);
                            //transferTo.setBranchId(BRANCH_ID);
                            transferTo.setInitiatedBranch(BRANCH_ID);
                            transferTo.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
                            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
                            TxTransferTO.add(transferTo);
                        }
                        // End


                        // System.out.println("TxTransferTO List 3 : " + TxTransferTO);
                        HashMap applnMap = new HashMap();
                        transferDAO = new TransferDAO();
                        map.put("MODE", map.get("COMMAND"));
                        map.put("COMMAND", map.get("MODE"));
                        map.put("TxTransferTO", TxTransferTO);
                    if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                        transMap = transferDAO.execute(map, false);
                    }else{
                        try {
                            transMap = transferDAO.execute(map, false);
                        } catch (Exception e) {
                            System.out.println("#$#$ Error :" + e);
                            returnMap.put(chittalNo, e);
                            continue;
                        }
                    }
                        transId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                        mdsReceiptEntryTO.setNetTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setPenalTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                        transactionDAO.setBatchId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                        transactionDAO.setBatchDate(currDt);
                        transactionDAO.execute(map);
                        HashMap linkBatchMap = new HashMap();
                        if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                                && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                            linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
                        } else {
                            linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
                        }
                        linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
                        linkBatchMap.put("TRANS_DT", currDt);
                        linkBatchMap.put("INITIATED_BRANCH", _branchCode);
                        sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
                        authorizeTransaction(transMap, map);
                        if (bonusNew > 0 || bonusNew > 0.0) {
                            bonusNew = bonusNew;
                        }
                        double bonous_Amt_trans = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getBonusAmtPayable());
                        /*if (bankAdvanceLst != null && bankAdvanceLst.size() > 0) { // Commented by nithya for KD-2093
                         HashMap bonusMap = (HashMap) bankAdvanceLst.get(0);
                         if (bonusMap.containsKey("BONUS_AMOUNT") && bonusMap.get("BONUS_AMOUNT") != null) {
                         bonous_Amt_trans = bonusNew;
                         }
                         }*/
                        System.out.println("bonous_Amt_trans :: " + bonous_Amt_trans);
                        commonTransactionCashandTransfer(map, debitMap, applicationMap, bonous_Amt_trans, discountAmt, chittalNo, subNo);//Jeffin
                        //System.out.println("####### mdsReceiptEntryTO : " + mdsReceiptEntryTO);
                   if(dataMap.containsKey("FORFEIT_RECOVERY") && dataMap.get("FORFEIT_RECOVERY") != null){
                       commonForfeitTransactionCashandTransfer(map, debitMap, applicationMap, mdsReceiptEntryTO.getForfeitBonusAmtPayable(), discountAmt, chittalNo, subNo);
                   }
                        paidInst = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPaidInst());
                        noOFinstToPay = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay());
                        int totalNoOfInstPay = paidInst + noOFinstToPay;
                        if (totalNoOfInstPay == CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInst())) {
                            //  System.out.println("Payment amount is equal....");
                            unMarkleanMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                            unMarkleanMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            List depositTypeList = sqlMap.executeQueryForList("getSelectMDSDepositTypeTO", unMarkleanMap);
                            if (depositTypeList != null && depositTypeList.size() > 0) {
                                String depositNo = CommonUtil.convertObjToStr(((MDSDepositTypeTO) depositTypeList.get(0)).getDepositNo());
                                unMarkleanMap.put("DEPOSIT_NO", depositNo);
                                List noOfDepositList = sqlMap.executeQueryForList("getNoOfLienDeposit", unMarkleanMap);
                                if (noOfDepositList != null && noOfDepositList.size() > 0) {
                                    leanFlagMap = (HashMap) noOfDepositList.get(0);
                                    if (leanFlagMap.get("STATUS") != null && leanFlagMap.containsKey("STATUS")) {
                                        if (leanFlagMap.get("STATUS").equals("LIEN")) {
                                            leanFlag = true;
                                        }
                                    }
                                }
                                if (leanFlag) {
                                    HashMap availClearMap = new HashMap();
                                    double clearBal = 0;
                                    double availBal = 0;
                                    List clrAvailBalList = sqlMap.executeQueryForList("getClearAvailbalance", unMarkleanMap);
                                    if (clrAvailBalList != null && clrAvailBalList.size() > 0) {
                                        availClearMap = (HashMap) clrAvailBalList.get(0);
                                        if (availClearMap.containsKey("CLEAR_BALANCE") && availClearMap.get("CLEAR_BALANCE") != null && availClearMap.containsKey("AVAILABLE_BALANCE") && availClearMap.get("AVAILABLE_BALANCE") != null) {
                                            clearBal = CommonUtil.convertObjToDouble(availClearMap.get("CLEAR_BALANCE"));
                                            availBal = CommonUtil.convertObjToDouble(availClearMap.get("AVAILABLE_BALANCE"));
                                        }
                                    }
                                    unMarkleanMap.put("LIEN_ACCT_NO", chittalNo);
                                    List lienAmountList = sqlMap.executeQueryForList("selectLienAmountInDepositLien", unMarkleanMap);
                                    if (lienAmountList != null && lienAmountList.size() > 0) {
                                        double lienAmount = 0;
                                        HashMap lienAmountMap = (HashMap) lienAmountList.get(0);
                                        if (lienAmountMap.containsKey("LIENAMOUNT") && lienAmountMap.get("LIENAMOUNT") != null) {
                                            lienAmount = CommonUtil.convertObjToDouble(lienAmountMap.get("LIENAMOUNT"));
                                        }
                                        lienAmount += availBal;
                                        availBal = lienAmount;
                                        unMarkleanMap.put("LIENAMOUNT", lienAmount);
//                                    System.out.println("Lien Amount " + lienAmount);
//                                    System.out.println("availa" + availBal);
                                        sqlMap.executeUpdate("UpdateLienAmountInDeposit", unMarkleanMap);
                                        unMarkleanMap.put("REMARK_STATUS", "Closed MDS");//
                                        unMarkleanMap.put("STATUS", "UNLIENED");//
                                        sqlMap.executeUpdate("UpdateRemarksInDepositLien", unMarkleanMap);
                                        if (availBal >= clearBal) {
                                            unMarkleanMap.put("LIEN_STATUS", "CREATED");
                                            sqlMap.executeUpdate("UpadteLienForMDSDepositType", unMarkleanMap);
                                        }
                                    }
                                }
                                leanFlag = false;
                            }
                        }
                        if (map.containsKey("MDS_SPLIT_TRANSACTION") && map.get("MDS_SPLIT_TRANSACTION") != null && map.get("MDS_SPLIT_TRANSACTION").equals("Y")) {
                            List splitList = null;
                            List splitTransInstList = new ArrayList();
                            List bonusAmountList = new ArrayList();
                            List penalList = new ArrayList();
                            List narrationList = new ArrayList();
                        HashMap forfietMap = new HashMap();
                            HashMap splitMap = new HashMap();
                            int curInst = 0;
                            //MDSReceiptEntryTO mdsSplitReceiptEntryTO = null;
                            if (map.containsKey("MDS_STANDING_INSTRUCTION_SPLIT_LIST") && map.get("MDS_STANDING_INSTRUCTION_SPLIT_LIST") != null) {
                                //splitList = (List) map.get("MDS_STANDING_INSTRUCTION_SPLIT_LIST");
                                //System.out.println("splitList^%^%^%^$^" + splitList);
                                // for(int j = 0;j<splitList.size();j++){
                                // splitMap = (HashMap) splitList.get(i); // Commented add the below lines of code by nithya for KD 504 - MDS Split transaction issue

                                HashMap splitLstMap = (HashMap) map.get("MDS_STANDING_INSTRUCTION_SPLIT_LIST");
                                //System.out.println("splitLstMap^%^%^%^$^" + splitLstMap);
                                splitMap = (HashMap) splitLstMap.get(chittalNo);
                                //System.out.println("splitMap^%^%^%^$^" + splitMap);

                                if (splitMap.containsKey("INST_AMT_LIST") && splitMap.get("INST_AMT_LIST") != null) {
                                    splitTransInstList = (List) splitMap.get("INST_AMT_LIST");
                                }
                                if (splitMap.containsKey("BONUS_AMT_LIST") && splitMap.get("BONUS_AMT_LIST") != null) {
                                    bonusAmountList = (List) splitMap.get("BONUS_AMT_LIST");
                                }
                                if (splitMap.containsKey("NARRATION_LIST") && splitMap.get("NARRATION_LIST") != null) {
                                    narrationList = (List) splitMap.get("NARRATION_LIST");
                                }
                                if (splitMap.containsKey("PENAL_AMT_LIST") && splitMap.get("PENAL_AMT_LIST") != null) {
                                    penalList = (List) splitMap.get("PENAL_AMT_LIST");
                                }
                                if (splitMap.containsKey("INSTALL_NO") && splitMap.get("INSTALL_NO") != null) {
                                    curInst = CommonUtil.convertObjToInt(splitMap.get("INSTALL_NO"));
                                }
                            if(splitMap.containsKey("FORFIET_AMT_MAP") && splitMap.get("FORFIET_AMT_MAP") != null){
                                forfietMap =(HashMap) splitMap.get("FORFIET_AMT_MAP");
                            }
                          
                                for (int k = 0; k < splitTransInstList.size(); k++) {
                                    mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(bonusAmountList.get(k)));
                                    mdsReceiptEntryTO.setCurrInst(new Double(curInst));
                                    mdsReceiptEntryTO.setNarration(setNarrationToSplitTransaction(k,
                                            CommonUtil.convertObjToInt(splitMap.get("INSTALL_NO")),
                                            CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitStartDt()),
                                            CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChitEndDt())));
                                    mdsReceiptEntryTO.setNoOfInstPay(1);
                                    mdsReceiptEntryTO.setPaidInst(new Double(curInst));
                                    mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(penalList.get(k)));
                                    mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(splitTransInstList.get(k)));
                                    mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(penalList.get(k)) + CommonUtil.convertObjToDouble(splitTransInstList.get(k)));
                                if(forfietMap != null && forfietMap.containsKey(curInst+1) && forfietMap.get(curInst+1) != null){
                                    mdsReceiptEntryTO.setForfeitBonusAmtPayable(CommonUtil.convertObjToDouble(forfietMap.get(curInst+1)));
                                }else{
                                    mdsReceiptEntryTO.setForfeitBonusAmtPayable(0.0); //Added by nithya for KD-2093
                                }
                                    mdsReceiptEntryTO.setForfeitBonusTransId("");
                                    mdsReceiptEntryTO.setBankAdvanceAmt(0.0);
                                    sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
                                    curInst++;
                                    //To mds trans details..
                                    HashMap transactionaApplnMap = new HashMap();
                                    transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                                    transactionaApplnMap.put("SHADOW_CREDIT", mdsReceiptEntryTO.getInstAmt());
                                    transactionaApplnMap.put("SHADOW_DEBIT", mdsReceiptEntryTO.getInstAmt());

                                    transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    transactionaApplnMap.put("BRANCH_CODE", _branchCode);
                                    transactionaApplnMap.put("TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                    //     System.out.println("############" + transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                                    sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);

                                    HashMap mdsTransMap = new HashMap();
                                    mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay()));
                                    mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                                    mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());
                                    mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                                    mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                                    mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                                    mdsTransMap.put("NET_AMT", mdsReceiptEntryTO.getNetAmt());
                                //mdsTransMap.put("FORFEITED_AMT", new Double(0.0)); // Added by nithya for KD-2093
                                mdsTransMap.put("FORFEITED_AMT", mdsReceiptEntryTO.getForfeitBonusAmtPayable());
                                    mdsTransMap.put("BANK_ADV_AMTCR", new Double(0.0));
                                    //mdsTransMap.put("SUBSCRIPTION_AMT", mdsReceiptEntryTO.getInstAmtPayable());//01-11-2019
                                    mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    mdsTransMap.put("STATUS_DT", currDt);
                                    mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_STATUS", CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                                    mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                                    mdsTransMap.put("AUTHORIZE_DT", currDt);
                                    mdsTransMap.put("TRANS_DT", currDt);
                                    mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                                    mdsTransMap.put("NARRATION", mdsReceiptEntryTO.getNarration());
                                    HashMap maxListMap = new HashMap();
                                    maxListMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                                    maxListMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    maxListMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    List list = sqlMap.executeQueryForList("getSelectTransMaxRec", maxListMap);
                                    if (list != null && list.size() > 0) {
                                        maxListMap = (HashMap) list.get(0);
                                        long instCount = CommonUtil.convertObjToLong(maxListMap.get("INST_COUNT")) + 1;
                                        mdsTransMap.put("INST_COUNT", new Long(instCount));
                                    } else {
                                        mdsTransMap.put("INST_COUNT", new Long(1));
                                    }
                                    //                 System.out.println("############" + mdsTransMap);
                                    sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);

                                }
                                // }
                                //To Transaction

                            }

                        } else {
                            /*mdsReceiptEntryTO.setForfeitBonusAmtPayable(0.0); //Added by nithya for KD-2093
                             mdsReceiptEntryTO.setForfeitBonusTransId("");
                             mdsReceiptEntryTO.setBankAdvanceAmt(0.0);*/
                        if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                            sqlMap.executeUpdate("insertReceiptEntryTO", mdsReceiptEntryTO);
                        }
                            HashMap transactionaApplnMap = new HashMap();
                            transactionaApplnMap.put("TOTAL_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                            transactionaApplnMap.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                            transactionaApplnMap.put("AVAILABLE_BALANCE", CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getInstAmt()));
                            transactionaApplnMap.put("SHADOW_CREDIT", mdsReceiptEntryTO.getInstAmt());
                            transactionaApplnMap.put("SHADOW_DEBIT", mdsReceiptEntryTO.getInstAmt());

                            transactionaApplnMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                            transactionaApplnMap.put("BRANCH_CODE", _branchCode);
                            transactionaApplnMap.put("TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                            //       System.out.println("############" + transactionaApplnMap);
                            sqlMap.executeUpdate("updateMDSAvailBalanceMap", transactionaApplnMap);
                            sqlMap.executeUpdate("updateMDSClearBalanceMap", transactionaApplnMap);
                            sqlMap.executeUpdate("updateMDSTotalBalanceMap", transactionaApplnMap);

                            HashMap mdsTransMap = new HashMap();
                            mdsTransMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                            mdsTransMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                            mdsTransMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            mdsTransMap.put("NO_OF_INST", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getNoOfInstPay()));
                            mdsTransMap.put("INST_AMT", mdsReceiptEntryTO.getInstAmt());
                            mdsTransMap.put("PENAL_AMT", mdsReceiptEntryTO.getPenalAmtPayable());
                            mdsTransMap.put("BONUS_AMT", mdsReceiptEntryTO.getBonusAmtPayable());
                            mdsTransMap.put("DISCOUNT_AMT", mdsReceiptEntryTO.getDiscountAmt());
                            mdsTransMap.put("MDS_INTEREST", mdsReceiptEntryTO.getMdsInterset());
                            mdsTransMap.put("NET_AMT", mdsReceiptEntryTO.getNetAmt());
                            mdsTransMap.put("FORFEITED_AMT", mdsReceiptEntryTO.getForfeitBonusAmtPayable()); // 2093
                            mdsTransMap.put("BANK_ADV_AMTCR", mdsReceiptEntryTO.getBankAdvanceAmt());
                            //mdsTransMap.put("SUBSCRIPTION_AMT", mdsReceiptEntryTO.getInstAmtPayable());//01-11-2019
                            mdsTransMap.put("STATUS", CommonConstants.STATUS_CREATED);
                            mdsTransMap.put("STATUS_DT", currDt);
                            mdsTransMap.put("STATUS_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_STATUS", CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED));
                            mdsTransMap.put("AUTHORIZE_BY", map.get("USER_ID"));
                            mdsTransMap.put("AUTHORIZE_DT", currDt);
                            mdsTransMap.put("TRANS_DT", currDt);
                            mdsTransMap.put("NET_TRANS_ID", mdsReceiptEntryTO.getNetTransId());
                            mdsTransMap.put("NARRATION", mdsReceiptEntryTO.getNarration());
                            HashMap maxListMap = new HashMap();
                            maxListMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                            maxListMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                            maxListMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                            List list = sqlMap.executeQueryForList("getSelectTransMaxRec", maxListMap);
                            if (list != null && list.size() > 0) {
                                maxListMap = (HashMap) list.get(0);
                                long instCount = CommonUtil.convertObjToLong(maxListMap.get("INST_COUNT")) + 1;
                                mdsTransMap.put("INST_COUNT", new Long(instCount));
                            } else {
                                mdsTransMap.put("INST_COUNT", new Long(1));
                            }
                            //     System.out.println("############" + mdsTransMap);
                        if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                            sqlMap.executeUpdate("updateMDSTransDetailsEachRec", mdsTransMap);          // Insert MDS_TRANS_DETAILS
                        }
//                    if(i==0 || i == standingLst.size()-1 ){
//                        returnMapList.add(transMap.get("TRANS_ID"));
                        }
                        if (!transMap.get("SINGLE_TRANS_ID").equals("")) {
                            returnSingleMapList.add(transMap.get("SINGLE_TRANS_ID"));
                        }
                        if (!transMap.get("TRANS_ID").equals("")) {
                            returnMapList.add(transMap.get("TRANS_ID"));
                        }

                        //2093 starts
                        if (advanceCollection.equalsIgnoreCase("Y")) {
//                        HashMap whereMap = new HashMap();
//                        double instNoForBankAdv = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getCurrInst()).doubleValue()
//                                - CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getPendingInst()).doubleValue()
//                                + CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoOfInstPay()).doubleValue();
//                        whereMap.put("INSTALLMENT_NO", String.valueOf(instNoForBankAdv));
//                        whereMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
//                        whereMap.put("SUB_NO", mdsReceiptEntryTO.getSubNo());
//                        whereMap.put("AUTH_STATUS", "AUTHORIZED");
//                        whereMap.put("REPAID", "Y");
//                        whereMap.put("REPAID_DT", currDt.clone());
//                        double number = CommonUtil.convertObjToDouble(mdsReceiptEntryTO.getNoOfInstPay()).doubleValue();
//                        List getBankAd = sqlMap.executeQueryForList("getPendingBankAdv", whereMap);
//                        if (getBankAd != null && getBankAd.size() > 0) {
//                            int countLength = getBankAd.size();
//                            if (number >= countLength) {
//                                sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatus", whereMap);
//                            } else {
//                                int k = 0;
//                                for (int l = 0; l < number; l++) {0
//                                    HashMap aMap = new HashMap();
//                                    aMap = (HashMap) getBankAd.get(l);
//                                    whereMap.put("BANK_ADV_ID", aMap.get("BANK_ADV_ID").toString());
//                                    sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatusSelected", whereMap);
//                                }
//                            }
//                        }
//                        
                            if (advProcessInstList != null && advProcessInstList.size() > 0) {
                                System.out.println(" advProcessInstList ::  " + advProcessInstList);
                                for (int l = 0; l < advProcessInstList.size(); l++) {
                                    HashMap adIinstMap = new HashMap();
                                    adIinstMap.put("INSTALLMENT_NO", String.valueOf(advProcessInstList.get(l)));
                                    adIinstMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                                    adIinstMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                                    adIinstMap.put("AUTH_STATUS", "AUTHORIZED");
                                    adIinstMap.put("REPAID", "Y");
                                    adIinstMap.put("REPAID_DT", currDt.clone());
                                    sqlMap.executeUpdate("updateMDSBankAdvanceRepaidStatus", adIinstMap);
                                }
                            }

                        }
                        // End

                        //Added by sreekrishnan for sms alert..
                        if (returnMapList != null && returnMapList.size() > 0) {
                            HashMap smsAlertMap = new HashMap();
                            smsAlertMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                            smsAlertMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                            //   System.out.println("smsAlertMap%#%#%#%#%#%  " + smsAlertMap);
                            List MdsSmsList = sqlMap.executeQueryForList("getMDsDetailsForSMS", smsAlertMap);
                            if (MdsSmsList != null && MdsSmsList.size() > 0) {
                                HashMap MdsSmsMap = (HashMap) MdsSmsList.get(0);
                                //     System.out.println("MdsSmsMap%#%#%#^#^#^#^" + MdsSmsMap);
                                if (MdsSmsMap != null && !MdsSmsMap.equals("")) {
                                    MdsSmsMap.put(CommonConstants.TRANS_ID, transId);
                                    MdsSmsMap.put("TRANS_DT", currDt);
                                    List MdsTransList = sqlMap.executeQueryForList("getMdsTransDetailsForSms", MdsSmsMap);
                                    if (MdsTransList != null && MdsTransList.size() > 0) {
                                        HashMap MdsTransMap = (HashMap) MdsTransList.get(0);
                                        //           System.out.println("MdsTransMap %#%#%#^#^#^#^" + MdsTransMap);
                                        MdsTransMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                        smsDAO.MdsReceiptsSmsConfiguration(MdsTransMap);
                                        MdsTransList = null;
                                    }
                                }
                                MdsSmsList = null;
                            }
                        }
                        //Added for Mantis 10732
                        HashMap lastInstMap = new HashMap();
                        lastInstMap.put("SCHEME_NAME", mdsReceiptEntryTO.getSchemeName());
                        lastInstMap.put("CHITTAL_NO", mdsReceiptEntryTO.getChittalNo());
                        List LastInstList = sqlMap.executeQueryForList("getChittalLastInstDetails", lastInstMap);
                        if (LastInstList != null && LastInstList.size() > 0) {
                            lastInstMap.put("LAST_INST_DT", currDt.clone());
                            sqlMap.executeUpdate("updateMasterMaintenceCloseDt", lastInstMap);
                        }
                        sqlMap.commitTransaction();
                    } catch (Exception err) {
                        sqlMap.rollbackTransaction();
                        err.printStackTrace();
                        String errMsg = "";
                        TTException tte = null;
                        HashMap exceptionMap = null;
                        HashMap excMap = null;
                        String strExc = null;
                        String errClassName = "";
                        if (err instanceof TTException) {
                            System.out.println("#$#$ if TTException part..." + err);
                            tte = (TTException) err;
                            if (tte != null) {
                                exceptionMap = tte.getExceptionHashMap();
                                System.out.println("#$#$ if TTException part exceptionMap ..." + exceptionMap);
                                if (exceptionMap != null) {
                                    ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                    errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                    System.out.println("#$#$ if TTException part EXCEPTION_LIST ..." + list);
                                    if (list != null && list.size() > 0) {
                                        for (int a = 0; a < list.size(); a++) {
                                            if (list.get(a) instanceof HashMap) {
                                                excMap = (HashMap) list.get(a);
                                                System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                                strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                        + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                            } else {
                                                strExc = (String) list.get(a);
                                                System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                            }
                                            errorMap = new HashMap();
                                            errorMap.put("ERROR_DATE", currDt);
                                            errorMap.put("TASK_NAME", "MDS_STANDNG_INSTRUCTION");
                                            errorMap.put("ERROR_MSG", strExc);
                                            errorMap.put("ERROR_CLASS", errClassName);
                                            errorMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
                                            errorMap.put("BRANCH_ID", _branchCode);
                                            sqlMap.startTransaction();
                                            sqlMap.executeUpdate("insertError_showing", errorMap);
                                            sqlMap.commitTransaction();
                                            errorMap = null;
                                        }
                                    }
                                } else {
                                    System.out.println("#$#$ if not TTException part..." + err);
                                    errMsg = err.getMessage();
                                    errorMap = new HashMap();
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", "MDS_STANDNG_INSTRUCTION");
                                    errorMap.put("ERROR_MSG", errMsg);
                                    errorMap.put("ERROR_CLASS", errClassName);
                                    errorMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
                                    errorMap.put("BRANCH_ID", _branchCode);
                                    sqlMap.startTransaction();
                                    sqlMap.executeUpdate("insertError_showing", errorMap);
                                    sqlMap.commitTransaction();
                                    errorMap = null;
                                }
                            }
                        } else {
                            System.out.println("#$#$ if not TTException part..." + err);
                            errMsg = err.getMessage();
                            errorMap = new HashMap();
                            errorMap.put("ERROR_DATE", currDt);
                            errorMap.put("TASK_NAME", "MDS_STANDNG_INSTRUCTION");
                            errorMap.put("ERROR_MSG", errMsg);
                            errorMap.put("ERROR_CLASS", errClassName);
                            errorMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
                            errorMap.put("BRANCH_ID", _branchCode);
                            System.out.println("#$#$ if not TTException part... errorMap:" + errorMap);
                            sqlMap.startTransaction();
                            sqlMap.executeUpdate("insertError_showing", errorMap);
                            sqlMap.commitTransaction();
                            errorMap = null;
                        }
                        //  status.setStatus(BatchConstants.ERROR);
                        //status.setStatus(BatchConstants.COMPLETED);
                        tte = null;
                        exceptionMap = null;
                        excMap = null;
                        err.printStackTrace();

                    }
                }
                //Added By Suresh
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", map);
                if (closureList != null && closureList.size() > 0) {
                    double totalSchemeAmount = 0.0;
                    double totalAmtReceived = 0.0;
                    double totalAmtPaid = 0.0;
                    HashMap whereMap = new HashMap();
                    List totalSchemeAmtLst = (List) sqlMap.executeQueryForList("getTotalAmountPerScheme", map);
                    if (totalSchemeAmtLst != null && totalSchemeAmtLst.size() > 0) {
                        whereMap = (HashMap) totalSchemeAmtLst.get(0);
                        totalSchemeAmount = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_SCHEME_AMOUNT"))).doubleValue();
                    }
                    List totalReceived = (List) sqlMap.executeQueryForList("getTotalReceivedAmount", map);
                    if (totalReceived != null && totalReceived.size() > 0) {
                        whereMap = (HashMap) totalReceived.get(0);
                        totalAmtReceived = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_RECEIVED"))).doubleValue();
                    }
                    List totalPaid = (List) sqlMap.executeQueryForList("getTotalPaidAmount", map);
                    if (totalPaid != null && totalPaid.size() > 0) {
                        whereMap = (HashMap) totalPaid.get(0);
                        totalAmtPaid = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(whereMap.get("TOTAL_PAID"))).doubleValue();
                    }
                    if (totalSchemeAmount == totalAmtReceived && totalSchemeAmount == totalAmtPaid) {
                        map.put("STATUS", "CLOSED");
                        sqlMap.executeUpdate("updateMDSProductCloseStatus", map);
                        List lst1 = sqlMap.executeQueryForList("getMDSLienDetailsForClosing", map);
                        if (lst1 != null && lst1.size() > 0) {
                            for (int i = 0; i < lst1.size(); i++) {
                                HashMap hmap = (HashMap) lst1.get(i);
                                List lst2 = sqlMap.executeQueryForList("getLienDEtailsForDelete", hmap);
                                if (lst2 != null && lst2.size() > 0) {
                                    hmap = (HashMap) lst2.get(0);
                                    double lienamt = CommonUtil.convertObjToDouble(hmap.get("LIEN_AMOUNT")).doubleValue();
                                    hmap.put("STATUS", "UNLIENED");
                                    hmap.put("UNLIEN_DT", currDt);
                                    hmap.put("DEPOSIT_ACT_NUM", hmap.get("DEPOSIT_NO"));
                                    hmap.put("CHITTAL_NO", hmap.get("LIEN_AC_NO"));
                                    hmap.put("LIENAMOUNT", CommonUtil.convertObjToDouble(hmap.get("LIEN_AMOUNT")));
                                    hmap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                    hmap.put("SHADOWLIEN", new Double(0.0));
                                    sqlMap.executeUpdate("updateSubAcInfoAvlBal", hmap);
                                    sqlMap.executeUpdate("updateUnlienForMDS", hmap);
                                }

                            }
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

    public Date getProperDateFormat(Object obj) {
        Date curDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt=(Date)currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)currDt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    private String setNarrationToSplitTransaction(int i, int paidInstallments, String strDt, String endDt) {
        String narration = "";
        ArrayList narrationList = new ArrayList();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
        int paidInst = paidInstallments + 1;
        paidInstallments += i;
        paidInst += i;
        narration = "Inst#" + (paidInst);
        Date dt1 = DateUtil.addDays(DateUtil.getDateMMDDYYYY(strDt), 30 * paidInstallments);
        Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(endDt), 30 * (1));
        narration += " " + sdf.format(dt1);
        //narrationList.add(narration);
        return narration;
    }

    public void authorizeTransaction(HashMap transMap, HashMap map) throws Exception {
        try {
            if (transMap != null && transMap.get("TRANS_ID") != null && !transMap.get("TRANS_ID").equals("")) {
                System.out.println("TRANSFER TRANS_ID :" + transMap.get("TRANS_ID"));
                String authorizeStatus = CommonUtil.convertObjToStr(CommonConstants.STATUS_AUTHORIZED);
                String linkBatchId = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                HashMap cashAuthMap = new HashMap();
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("MDS_STANDING_AD", "MDS_STANDING_AD");
                cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");// Added by nithya on 11-12-2020 for KD-2509
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

    public void commonTransactionCashandTransfer(HashMap map, HashMap debitMap, HashMap applicationMap, double bonusAmt, double discountAmt, String chittalNo, String subNo) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        //System.out.println("bonusAmt#%#%%#%# trans "+bonusAmt);
        if (bonusAmt > 0) {
            System.out.println("Bonus Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
            txMap.put(TransferTrans.PARTICULARS, "Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
          //  System.out.println("txMap : " + txMap + "discountAmt :" + discountAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, bonusAmt);
            if (map.containsKey("FROM_MDS_RECOVERY_SCREEN")) {
                transferTo.setRec_mode("RP");
            }
            transferTo.setInstrumentNo1("SI");
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("BONUS");//31-10-2019
            TxTransferTO.add(transferTo);
        }
        //System.out.println("TxTransferTO List 4 : " + TxTransferTO);
        if (discountAmt > 0) {
            System.out.println("Discount Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("DISCOUNT_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
            txMap.put(TransferTrans.PARTICULARS, "Discount " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
          //  System.out.println("txMap : " + txMap + "discountAmt :" + discountAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, discountAmt);
            transferTo.setInstrumentNo1("SI");
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("DISCOUNT_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("DISCOUNT");//31-10-2019
            TxTransferTO.add(transferTo);
        }
        double receivingGLAmt = discountAmt + bonusAmt;
    //    System.out.println("transferTo List 5 : " + TxTransferTO + "receivingGLAmt : " + receivingGLAmt);
        if (receivingGLAmt > 0) {
      //      System.out.println("GL Final Transaction Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("RECEIPT_HEAD"));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
            txMap.put(TransferTrans.PARTICULARS, "Installment Amt " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
            System.out.println("txMap : " + txMap + "receivingGLAmt :" + receivingGLAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt);
            if (map.containsKey("FROM_MDS_RECOVERY_SCREEN")) {
                transferTo.setRec_mode("RP");
            }
            transferTo.setInstrumentNo1("SI");
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("RECEIPT_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("BONUS");//31-10-2019
            TxTransferTO.add(transferTo);
           // System.out.println("transferTo List 6 : " + transferTo);

          //  System.out.println("transferTo List 6 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
         //   System.out.println("transferDAO List Last : " + map);
            HashMap transMap = transferDAO.execute(map, false);
           // System.out.println("transferDAO List transMap : " + transMap);
            mdsReceiptEntryTO.setBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            HashMap linkBatchMap = new HashMap();
            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
            } else {
                linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
            }
            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("TRANS_DT", currDt);
            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
            authorizeTransaction(transMap, map);
            linkBatchMap = null;
            transMap = null;
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
//        currDt = ServerUtil.getCurrentDate(_branchCode);
        return null;
    }

    private void destroyObjects() {
        standingLst = null;
    }

    public boolean isRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    public boolean isRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }

    public HashMap getDrAcMinBal() {
        return drAcMinBal;
    }

    public void setDrAcMinBal(HashMap drAcMinBal) {
        this.drAcMinBal = drAcMinBal;
    }

    public String getTxtSchemeName() {
        return txtSchemeName;
    }

    public void setTxtSchemeName(String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }
    
    private HashMap getBonusDetailsForfiet(HashMap mdsBonusMap, MDSReceiptEntryTO objMDSReceiptEntryTO, HashMap dataMap, String bonusRoundOff) throws SQLException {
        ArrayList advProcessInstList = new ArrayList();
                    double bankAdvInsAmt = 0.0;
                    double bankBonusAmt = 0.0;
                    double instalmentPayAmt = 0.0;
                    double futureAdvbonusAmt = 0.0;
                    double totalDebitAmt = 0.0;
        HashMap bonusDetailsMap = new HashMap();
        LinkedHashMap instMap = new LinkedHashMap();
        List instMapLst = new ArrayList();
        int noOfInstPay = CommonUtil.convertObjToInt(dataMap.get("NO_OF_INST_PAY"));
        int noOfInstPaid = CommonUtil.convertObjToInt(dataMap.get("PAID_INST"));
        for (int t = 1; t <= noOfInstPay; t++) {
           // System.out.println("t..." + (noOfInstPaid + t));
            instMap.put(String.valueOf(noOfInstPaid + t), noOfInstPaid + t);
            instMapLst.add(noOfInstPaid + t);
        }
        LinkedHashMap chitalBonusMap = (LinkedHashMap) mdsBonusMap.get(dataMap.get("CHITTAL_NO"));
        LinkedHashMap finalChitalBonusMap = new LinkedHashMap();
       // System.out.println("chitalBonusMap :: " + chitalBonusMap);
        if (instMapLst != null && instMapLst.size() > 0) {
            for (int inst = 0; inst < instMapLst.size(); inst++) {
                if (chitalBonusMap.get(instMapLst.get(inst)) != null) {
                    finalChitalBonusMap.put(String.valueOf(instMapLst.get(inst)), chitalBonusMap.get(instMapLst.get(inst)));
                } else {
                    finalChitalBonusMap.put(String.valueOf(instMapLst.get(inst)), 0.0);
                }
            }
        }
        //System.out.println("finalChitalBonusMap :: " + finalChitalBonusMap);
        double bonusN = 0.0;
        for (int h = 0; h < instMapLst.size(); h++) {
           // System.out.println("h :: " + instMapLst.get(h));
            bonusN = CommonUtil.convertObjToDouble(finalChitalBonusMap.get(instMapLst.get(h)));
        }
      //  System.out.println("bonusN ::" + bonusN);
        
        HashMap bankAdvMap = new HashMap();//Nidhin
                    int instNo = CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()) - CommonUtil.convertObjToInt(mdsReceiptEntryTO.getPendingInst());
                    bankAdvMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getSchemeName()));
                    bankAdvMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(mdsReceiptEntryTO.getChittalNo()));
                    bankAdvMap.put("SUB_NO", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getSubNo()));
                    bankAdvMap.put("INSTALLMENT_NO", instNo);
                    bankAdvMap.put("TO_INSTALLMENT", CommonUtil.convertObjToInt(mdsReceiptEntryTO.getCurrInst()));
                    List bankAdvLst = sqlMap.executeQueryForList("getSelectBankAdvanceDetailsData", bankAdvMap);
                    Iterator addressIterator1 = finalChitalBonusMap.keySet().iterator();
                    LinkedHashMap normalInstMap = new LinkedHashMap();
                    if (bankAdvLst != null && bankAdvLst.size() > 0) {
                        LinkedHashMap bankMap = new LinkedHashMap();
                        bankAdvMap = new LinkedHashMap();
                        for (int b = 0; b < bankAdvLst.size(); b++) {
                            bankMap = (LinkedHashMap) bankAdvLst.get(b);
                            bankAdvMap.put(String.valueOf(bankMap.get("INSTALLMENT_NO")), bankMap);
                        }
                     //   System.out.println("bankAdvMap :: " + bankAdvMap);
                        String key1 = "";
                        for (int f = 0; f < finalChitalBonusMap.size(); f++) {
                            key1 = (String) addressIterator1.next();
                         //   System.out.println("###### key1 ###### : " + key1);
                            if (bankAdvMap.containsKey(key1)) {
                                HashMap installMap = new HashMap();
                                bankAdvInsAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("INST_AMT")).doubleValue();
                                bankBonusAmt += CommonUtil.convertObjToDouble(((HashMap) bankAdvMap.get(key1)).get("BONUS_AMT")).doubleValue();
                                advProcessInstList.add(key1);
                            } else {
                                HashMap installMap = new HashMap();
                                futureAdvbonusAmt += CommonUtil.convertObjToDouble(finalChitalBonusMap.get(key1)).doubleValue();//     
                                normalInstMap.put(key1, finalChitalBonusMap.get(key1));
                            }
                        }
                        bankAdvInsAmt = bankAdvInsAmt - bankBonusAmt;
                       // System.out.println("bankAdvInsAmt :: " + bankAdvInsAmt);
                     //   System.out.println("bankBonusAmt :: " + bankBonusAmt);
                      //  System.out.println("instalmentPayAmt :: " + instalmentPayAmt);
                     //   System.out.println("futureAdvbonusAmt :: " + futureAdvbonusAmt);
                    } else {
                        String key1 = "";
                        for (int f = 0; f < finalChitalBonusMap.size(); f++) {
                            key1 = (String) addressIterator1.next();
                            //System.out.println("###### key1 ###### : " + key1);
                            HashMap installMap = new HashMap();
                            futureAdvbonusAmt += CommonUtil.convertObjToDouble(finalChitalBonusMap.get(key1)).doubleValue();//     
                            normalInstMap.put(key1, finalChitalBonusMap.get(key1));
                        }
                    }
                 //   System.out.println("normalInstMap :: " + normalInstMap);
                    Iterator bonusKeyIterator = normalInstMap.keySet().iterator();
                    String bonusKey = "";
                    double bonusPayable = 0.0;
                    double normalInstallmentAmt = 0.0;
                    for (int f = 0; f < normalInstMap.size(); f++) {
                        bonusKey = (String) bonusKeyIterator.next();
                        bonusPayable = bonusPayable + CommonUtil.convertObjToDouble(normalInstMap.get(bonusKey));
                        normalInstallmentAmt += mdsReceiptEntryTO.getInstAmt();
                    }
                    Rounding rod = new Rounding();
                    if(!bonusRoundOff.equals("NO_ROUND_OFF")){
                    if (bonusRoundOff.equalsIgnoreCase("NEAREST_VALUE")) {
                            bonusPayable = (double) rod.getNearest((long) (bonusPayable * 100), 100) / 100;
                        } else {
                            bonusPayable = (double) rod.lower((long) (bonusPayable * 100), 100) / 100;
                        }
                    }
         bonusDetailsMap.put("BONUS_PAYABLE",bonusPayable);
         bonusDetailsMap.put("NORMAL_INTALLMENT_AMOUNT",normalInstallmentAmt);
         bonusDetailsMap.put("BANK_ADVANCE_AMT",bankAdvInsAmt);
         bonusDetailsMap.put("BANK_ADV_INST_LST",advProcessInstList);
        return bonusDetailsMap;
    }
                 
    public void commonForfeitTransactionCashandTransfer(HashMap map, HashMap debitMap, HashMap applicationMap, double bonusAmt, double discountAmt, String chittalNo, String subNo) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList TxTransferTO = new ArrayList();
        TransactionTO transactionTO = new TransactionTO();
        HashMap txMap = new HashMap();
        HashMap transactionListMap = new HashMap();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        //System.out.println("bonusAmt#%#%%#%# trans "+bonusAmt);
        if (bonusAmt > 0) {
            System.out.println("Forfeit Bonus Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.DR_AC_HD, applicationMap.get("BONUS_RECEIVABLE_HEAD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.PARTICULARS, "Bonus " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
          //  System.out.println("txMap : " + txMap + "discountAmt :" + discountAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, bonusAmt);
            if (map.containsKey("FROM_MDS_RECOVERY_SCREEN")) {
                transferTo.setRec_mode("RP");
            }
            transferTo.setInstrumentNo1("SI");
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.DEBIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("BONUS_RECEIVABLE_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("FORFEITE_BONUS");//31-10-2019
            TxTransferTO.add(transferTo);
        }
        //System.out.println("TxTransferTO List 4 : " + TxTransferTO);
        
        double receivingGLAmt =  bonusAmt;
    //    System.out.println("transferTo List 5 : " + TxTransferTO + "receivingGLAmt : " + receivingGLAmt);
        if (receivingGLAmt > 0) {
      //      System.out.println("GL Final Transaction Started");
            transferTo = new TxTransferTO();
            txMap = new HashMap();
            transferTo.setInstrumentNo2("APPL_GL_TRANS");
            txMap.put(TransferTrans.CR_AC_HD, applicationMap.get("MDS_FORFEITED_ACCT_HEAD"));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.PARTICULARS, "Forfeit Amt " + applicationMap.get("MP_MDS_CODE") + "-" + chittalNo + "_" + subNo);
            System.out.println("txMap : " + txMap + "receivingGLAmt :" + receivingGLAmt);
            txMap.put("TRANS_MOD_TYPE", "MDS");
            transferTo = transactionDAO.addTransferCreditLocal(txMap, receivingGLAmt);
            if(map.containsKey("FROM_MDS_RECOVERY_SCREEN")){
                            transferTo.setRec_mode("RP");
                        }
            transferTo.setInstrumentNo1("SI");
            transferTo.setTransId("-");
            transferTo.setBatchId("-");
            transferTo.setTransType(CommonConstants.CREDIT);
            transferTo.setProdType(TransactionFactory.GL);
            transferTo.setAcHdId(CommonUtil.convertObjToStr(applicationMap.get("MDS_FORFEITED_ACCT_HEAD")));
            transferTo.setTransDt(currDt);
            transferTo.setBranchId(BRANCH_ID);
            transferTo.setInitiatedBranch(BRANCH_ID);
            transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(chittalNo));
            transferTo.setGlTransActNum(CommonUtil.convertObjToStr(chittalNo));
            transactionTO.setChequeNo("SERVICE_TAX");
            transferTo.setScreenName((String) map.get(CommonConstants.SCREEN));
            transferTo.setInstrumentNo2("FORFEITE_BONUS");//31-10-2019
            TxTransferTO.add(transferTo);
           // System.out.println("transferTo List 6 : " + transferTo);

          //  System.out.println("transferTo List 6 : " + TxTransferTO);
            HashMap applnMap = new HashMap();
            map.put("MODE", map.get("COMMAND"));
            map.put("COMMAND", map.get("MODE"));
            map.put("TxTransferTO", TxTransferTO);
         //   System.out.println("transferDAO List Last : " + map);
            HashMap transMap = transferDAO.execute(map, false);
           // System.out.println("transferDAO List transMap : " + transMap);
            mdsReceiptEntryTO.setForfeitBonusTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            //mdsReceiptEntryTO.setDiscountTransId(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            HashMap linkBatchMap = new HashMap();
            if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL")
                    && transactionTO.getProductId().equals("") && !map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            } else if (!map.containsKey("LOCKER_SURRENDER_DAO")) {
                linkBatchMap.put("LINK_BATCH_ID", transactionTO.getDebitAcctNo());
            } else {
                linkBatchMap.put("LINK_BATCH_ID", map.get("LOCKER_SURRENDER_ID"));
            }
            linkBatchMap.put("LINK_BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("BATCH_ID", transMap.get("TRANS_ID"));
            linkBatchMap.put("TRANS_DT", currDt);
            linkBatchMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("updateLinkBatchIdTransfer", linkBatchMap);
            authorizeTransaction(transMap, map);
            linkBatchMap = null;
            transMap = null;
        }
    }
}