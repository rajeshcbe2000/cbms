/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSStandingInstructionOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.mdsapplication;


import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Calendar;
import java.util.Collections;

/**
 *
 * @author
 */

public class MDSStandingInstructionOB extends CObservable{
    
    
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private ArrayList bonusAmountList = null;
    private ArrayList penalList = null;
    private ArrayList penalRealList = null;
    private ArrayList instList = null;
    private ArrayList narrationList = null;
    private HashMap splitTransMap;
    private ArrayList finalSplitList = null;
    private String isSplitMDSTransaction = "";
    private EnhancedTableModel tblStandingInstruction;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(MDSStandingInstructionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private List finalList = null;
    private HashMap chittalPenalAmtMap;
    private List finalTableList = null;
    private Date curr_dt=null;
    private String txtSchemeName = "";
    private List pendingList = null;
    private ComboBoxModel cbmProdId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String isWeeklyOrMonthlyScheme = "";
    private int instFrequency = 0;
    private List MDSSplitList ;    
    HashMap tempDebitAc;
    HashMap drAcMinBal;    
    private List priorityList = null; // Added by nithya on 26-08-2016
    private String txtInstNo = "";
    HashMap MDSbonusMap = new HashMap();
    private double forfietBonusAmt = 0.0;    

    public String getTxtInstNo() {
        return txtInstNo;
    }

    public void setTxtInstNo(String txtInstNo) {
        this.txtInstNo = txtInstNo;
    }
    

    public List getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List priorityList) {
        this.priorityList = priorityList;
    }    
    
    public HashMap getDrAcMinBal() {
        return drAcMinBal;
    }

    public void setDrAcMinBal(HashMap drAcMinBal) {
        this.drAcMinBal = drAcMinBal;
    }

    public void setMDSSplitList(List MDSSplitList) {
        this.MDSSplitList = MDSSplitList;
    }

    

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    public MDSStandingInstructionOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSStandingInstructionJNDI");
            map.put(CommonConstants.HOME, "mdsapplication.MDSStandingInstructionHome");
            map.put(CommonConstants.REMOTE, "mdsapplication.MDSStandingInstruction");
            setStandingTableTile();
            tblStandingInstruction = new EnhancedTableModel(null, tableTitle);
            curr_dt=ClientUtil.getCurrentDate();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setStandingTableTile(){
        tableTitle.add("Select");
        tableTitle.add("Chittal No");
        tableTitle.add("Sub No");
        tableTitle.add("Member Name");
        tableTitle.add("InstDue");
        tableTitle.add("Ins Amt");
        tableTitle.add("Bonus");
        tableTitle.add("Discount");
        tableTitle.add("Interest");
        tableTitle.add("Prod Type");
        tableTitle.add("Prod Id");
        tableTitle.add("Dr Act No");
        tableTitle.add("Avai. Balance");
        tableTitle.add("Net Amt");
        tableTitle.add("Forfeit");
        IncVal = new ArrayList();
    }
    
    public void getTableData(HashMap whereMap) {
        try {            
            HashMap dataMap = new HashMap();
            dataMap.put("PENDING_LIST", "PENDING_LIST");
            dataMap.put("SCHEME_NAME", getTxtSchemeName());
            dataMap.put("SI_MAP", whereMap);
            //System.out.println("Calling DAO && PENDING_LIST Data in MDSStandingInstruction OB : " + dataMap);
            dataMap = proxy.execute(dataMap, map);
            //System.out.println("Pending Map from DAO " + dataMap);
            HashMap finMap = new HashMap();
            finMap = (HashMap) dataMap.get("FINAL_MAP");
            finalSplitList = (ArrayList)dataMap.get("FINAL_SPLIT_LIST");
            isSplitMDSTransaction = CommonUtil.convertObjToStr(dataMap.get("IS_SPLIT_MDS_TRANSACTION"));
            if(dataMap.containsKey("MDS_BONUS_MAP") && dataMap.get("MDS_BONUS_MAP") != null){
               MDSbonusMap = (HashMap)dataMap.get("MDS_BONUS_MAP"); //2093         
            }
            //System.out.println(" finMap..... " + finMap);
            if (finMap != null && finMap.size() > 0 && finMap.containsKey("PENDING_LIST")) {
                pendingList = (List) finMap.get("PENDING_LIST");
            } else {
                pendingList = null;
            }
            if (finMap != null && finMap.size() > 0 && finMap.containsKey("STANDING_LIST")) {
                List standInsList = (List) finMap.get("STANDING_LIST");
                if (standInsList.size() > 0) {
                    setFinalList(standInsList);
                }
            }
            
            if (finMap != null && finMap.size() > 0 && finMap.containsKey("CHITTAL_PENAL_AMT_MAP") && finMap.get("CHITTAL_PENAL_AMT_MAP") != null) {
                HashMap chittalPenalAmtMap = (HashMap) finMap.get("CHITTAL_PENAL_AMT_MAP");                
                setChittalPenalAmtMap(chittalPenalAmtMap);                
            }
            
            if (finMap != null && finMap.size() > 0 && finMap.containsKey("TAB_LIST")) {
                List tabList = (List) finMap.get("TAB_LIST");                
                if (tabList.size() > 0) {
                    tblStandingInstruction = new EnhancedTableModel((ArrayList) tabList, tableTitle);
                }
            }

            if (finMap != null && finMap.size() > 0 && finMap.containsKey("DR_AC_MIN_BAL")) {
                HashMap drAccMap = (HashMap) finMap.get("DR_AC_MIN_BAL");
                if (drAccMap != null && drAccMap.size() > 0) {
                    setDrAcMinBal(drAccMap);
                }
            }             
        } catch (Exception e) {
        }
    }
   
    public void insertTableData(HashMap whereMap){
        try{
            String scheme_Name = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
            int currentInstallment = CommonUtil.convertObjToInt(whereMap.get("CURRENT_INSALL_NO"));// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap removePendingChittalMap = new HashMap();
            HashMap productMap = new HashMap();
            String chittalNo = "";
            long diffDayPending = 0;
            int noOfInsPaid = 0;
            Date currDate = (Date) curr_dt.clone();
            Date instDate = null;
            boolean bonusAvailabe = true;
            long noOfInstPay = 0;
            int instDay = 1;
            int totIns = 0;
            Date startDate = null;
            Date insDate = null;
            int startMonth = 0;
            int insMonth = 0;
            int curInsNo = 0;
            finalSplitList = new ArrayList();
            tempDebitAc = new HashMap();
            drAcMinBal = new HashMap();           
           
            List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",whereMap);
            if(lst!=null && lst.size()>0){
                productMap = (HashMap)lst.get(0);
                totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                startMonth = insDate.getMonth();
                isSplitMDSTransaction = CommonUtil.convertObjToStr(productMap.get("IS_SPLIT_MDS_TRANSACTION")); 
            }
            // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
//            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            System.out.println("bonusfirstoutsideloop"+bonusFirstInst);
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
            
            List instDtLst = ClientUtil.executeQuery("getMDSNextInstDtForInstNo", whereMap);
            HashMap instDtMap = (HashMap)instDtLst.get(0);
            if(isWeeklyOrMonthlyScheme.equalsIgnoreCase("M")){
                whereMap.put("CURR_DATE",instDtMap.get("NEXT_INSTALLMENT_DATE"));
            }            
            List standInsList = ClientUtil.executeQuery("getStandingInsDetails", whereMap);
            System.out.println("#$@$@$#@$@#$@ List : "+standInsList);
            
            
            int startNoForPenal = 0;
            int addNo = 1;
            int firstInst_No = -1;
            List newStandingList = new ArrayList();
            if (bonusFirstInst.equals("Y")) {
                startNoForPenal = 1;
                addNo = 0;
                firstInst_No = 0;
            }
            
            if(standInsList!=null && standInsList.size()>0){                
                for (int i = 0;i<standInsList.size();i++){
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
                    whereMap.put("CHITTAL_NO",dataMap.get("CHITTAL_NO"));
                    whereMap.put("SUB_NO",CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    if(insList!=null && insList.size()>0){
                        whereMap = (HashMap)insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    }
                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO",dataMap.get("CHITTAL_NO"));
                    chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    if(chitLst!=null &&chitLst.size()>0){
                        chittalMap = (HashMap)chitLst.get(0);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                    }
                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", divNo);
                    insDateMap.put("SCHEME_NAME", scheme_Name);
                    if(isWeeklyOrMonthlyScheme.equalsIgnoreCase("M")){
                        insDateMap.put("CURR_DATE", instDtMap.get("NEXT_INSTALLMENT_DATE"));
                    }else{
                        insDateMap.put("CURR_DATE", curr_dt.clone());
                    }                    
//                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                    List insDateLst = null;
                    boolean monthlyScheme = false;// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                        insDateLst = ClientUtil.executeQuery("getWeeklyMDSCurrentInsDate", insDateMap);
                    } else {
                        insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
                        monthlyScheme = true;// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                    }
                    if(insDateLst!=null && insDateLst.size()>0){
                        insDateMap = (HashMap)insDateLst.get(0);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        if(monthlyScheme){// Added by nithya on 15-12-2017 for 0008300: Mds_standing_instruction Screen -Installment number selection needed.
                            curInsNo = currentInstallment;
                        }
                        pendingInst = curInsNo - noOfInsPaid;
                        /* Added by nithya---*/
                        if (bonusFirstInst.equalsIgnoreCase("Y")){
                            pendingInst -= 1;//nithyaaaa
                        }
                        /* Added by nithya--- end*/
                        if(pendingInst<0){
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }
                    noOfInstPay = pendingInst+1;
                    if (bonusFirstInst.equalsIgnoreCase("Y") /*|| CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")*/) {//Added By Nidhin for Bank advance case installment no taken wrong
                       // noOfInstPay -= 1;nithyaaa /* commented by nithya---*/
                    }
                    //if (bonusAdvance.equals("N") && pendingInst<=0) {  // Bonus For First Installment
                     ///   noOfInstPay=noOfInstPay+1;
                   // }
                    double availableBalance = 0.0;
                    availableBalance = CommonUtil.convertObjToDouble(dataMap.get("AVAILABLE_BALANCE")).doubleValue();
                    HashMap prizedMap = new HashMap();
                    prizedMap.put("SCHEME_NAME",scheme_Name);
                    prizedMap.put("DIVISION_NO",String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO",dataMap.get("CHITTAL_NO"));
                    prizedMap.put("SUB_NO",CommonUtil.convertObjToInt(dataMap.get("SUB_NO")));
                    //
                    System.out.println("productMap**"+productMap);
                    if (productMap.containsKey("FROM_AUCTION_ENTRY") && productMap.get("FROM_AUCTION_ENTRY") != null && productMap.get("FROM_AUCTION_ENTRY").equals("Y")) {
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
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
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsAfterCashPayment", prizedMap);
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
                        lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
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
                    if(pendingInst>0){              //pending installment calculation starts...
                        insDueAmt = (long)insAmt * pendingInst;
                    }
                    long pendingInstallment = pendingInst;
                    
                    //Calculate MDS
                    double netAmt = 0;
                    double penalAmt = 0;
                    double totDiscAmt = 0;
                    double totBonusAmt = 0;
                    double insAmtPayable = 0;
                    System.out.println("########### availableBalance 44: "+availableBalance);
                    String sub_No = CommonUtil.convertObjToStr(dataMap.get("SUB_NO"));
                    productMap.put("DR_ACT_NO",dataMap.get("DR_ACT_NO"));
                    HashMap calcMap = calculateMDSStandingInstruction(scheme_Name, chittalNo,sub_No, divNo, noOfInsPaid, instDay, productMap, pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance);
                    System.out.println("Calcmapppuuuu "+calcMap);
                    if(calcMap!=null && calcMap.size()>0){
                        netAmt = CommonUtil.convertObjToDouble(calcMap.get("NET_AMOUNT")).doubleValue();
                        insAmtPayable = CommonUtil.convertObjToDouble(calcMap.get("INST_AMOUNT_PAYABLE")).doubleValue();
                       //done by akhi since ',' in bonus amount it is not getting converted to double value
                        String totbamout=calcMap.get("BONUS").toString();
                        totbamout=totbamout.replace(",","");
                        totBonusAmt = Double.parseDouble(totbamout);
                        System.out.println("totobomuuh "+totBonusAmt+"   "+CommonUtil.convertObjToDouble(calcMap.get("BONUS")).doubleValue()+"     "+calcMap.get("BONUS"));
                        
                        System.out.println("netAmt  tt "+netAmt);
                        totDiscAmt = CommonUtil.convertObjToDouble(calcMap.get("DISCOUNT")).doubleValue();
                        penalAmt = CommonUtil.convertObjToDouble(calcMap.get("PENAL")).doubleValue();
                        noOfInstPay = CommonUtil.convertObjToLong(calcMap.get("NO_OF_INST_PAY"));
                        if(pendingInst == noOfInstPay) //pendingInst+1 commented by Nidhin for bonus transaction is not correct :Mantis ID : 9438
                        {
                           dataMap.put("BONUS_NEW",CommonUtil.convertObjToDouble(calcMap.get("BONUS_NEW")).doubleValue());
                        }
                        else
                        {
                            dataMap.put("BONUS_NEW","0");
                        }
                    }
                    
//                    netAmt = (noOfInstPay*insAmt)+penalAmt-(totBonusAmt+totDiscAmt);
//                    insAmtPayable = (noOfInstPay*insAmt) -(totBonusAmt+totDiscAmt);

                    dataMap.put("DIVISION_NO",String.valueOf(divNo));
                    dataMap.put("CHIT_START_DT",startDate);
                    dataMap.put("INSTALLMENT_DATE",insDate);
                    dataMap.put("NO_OF_INSTALLMENTS",String.valueOf(totIns));
                    dataMap.put("CURR_INST",String.valueOf(curInsNo));
                    dataMap.put("PENDING_INST",String.valueOf(pendingInst));
                    dataMap.put("PENDING_DUE_AMT",String.valueOf(insDueAmt));
                    dataMap.put("NO_OF_INST_PAY",String.valueOf(noOfInstPay));
                    dataMap.put("INST_AMT_PAYABLE",String.valueOf(insAmtPayable));
                    dataMap.put("PAID_INST",String.valueOf(noOfInsPaid));
                    dataMap.put("PAID_DATE",currDate);
                    if(getRdoPrizedMember_Yes() == true){
                        dataMap.put("PRIZED_MEMBER","Y");
                    }else{
                        dataMap.put("PRIZED_MEMBER","N");
                    }
                    
                    //Insert Narration
                    whereMap = new HashMap();
                    Date nextInsDt = null;
                    whereMap.put("SCHEME_NAME",scheme_Name);
                    whereMap.put("DIVISION_NO", divNo);
                    whereMap.put("INSTALLMENT_NO",noOfInsPaid);
                    lst = ClientUtil.executeQuery("getSelectInstUptoPaid", whereMap);
                    if(lst!=null && lst.size()>0){
                        whereMap = (HashMap)lst.get(0);
                        nextInsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("NEXT_INSTALLMENT_DATE")));
                    }else{
                        startDate.setDate(instDay);
                        int stMonth = startDate.getMonth();
                        startDate.setMonth(stMonth+(int)noOfInsPaid-1);
                        nextInsDt = startDate;
                    }
                    String narration = "";
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                    narration = "Inst#"+(noOfInsPaid+1);
                    Date dt = DateUtil.addDays(nextInsDt, 30);
                    narration+=" "+sdf.format(dt);
                    System.out.println("######narration : "+narration);
                    dataMap.put("NARRATION",narration);
                    dataMap.put("BONUS",CurrencyValidation.formatCrore(String.valueOf(totBonusAmt)));
                    dataMap.put("DISCOUNT",CurrencyValidation.formatCrore(String.valueOf(totDiscAmt)));
                    dataMap.put("PENAL",CommonUtil.convertObjToDouble(penalAmt));
                    dataMap.put("NET_AMOUNT",CommonUtil.convertObjToDouble(netAmt));
                    dataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                   /* if (bonusFirstInst.equals("N")) {//nithya commented and made this condition common to both advance_collection Y & N
                        pendingInst += 1;
                    }*/
                    pendingInst += 1;//nithyaaa
                    rowList.add(String.valueOf(noOfInstPay+"/"+pendingInst));
                    rowList.add(dataMap.get("INST_AMT"));
                    rowList.add(CurrencyValidation.formatCrore(String.valueOf(totBonusAmt)));
                    rowList.add(CurrencyValidation.formatCrore(String.valueOf(totDiscAmt)));
                    rowList.add(CurrencyValidation.formatCrore(String.valueOf(penalAmt)));
                    rowList.add(dataMap.get("PROD_TYPE"));
                    rowList.add(dataMap.get("PROD_ID"));
                    rowList.add(dataMap.get("DR_ACT_NO"));
                    rowList.add(dataMap.get("AVAILABLE_BALANCE"));
                    rowList.add(CommonUtil.convertObjToDouble(netAmt));
//                    if(pendingInst != 0){
                    tableList.add(rowList);
//                    }
                    //Added by sreekrishnan for min bal checking
                    drAcMinBal.put(dataMap.get("DR_ACT_NO"),CommonUtil.convertObjToDouble(dataMap.get("MIN_BALANCE")));                    
                }
//                if (tableList == null) {
//                    standInsList = null;
//                } else {
//
//                    int sizes = standInsList.size();
//                    for (int j = 0; j < tableList.size(); j++) {
//                        List lsts = (List) tableList.get(j);
//                        for (int i = 0; i < standInsList.size(); i++) {
//                            removePendingChittalMap = (HashMap) standInsList.get(i);
//                            String s = CommonUtil.convertObjToStr(removePendingChittalMap.get("CHITTAL_NO"));
//                            if (s.equals(CommonUtil.convertObjToStr(lsts.get(1)))) {
//                                newStandingList.add(standInsList.get(i));
//                            }
//                        }
//
//                    }
//                }
                setFinalList(standInsList);
                tblStandingInstruction= new EnhancedTableModel((ArrayList)tableList, tableTitle);
                //Pending For Authorize List
                HashMap pendingMap = new HashMap();
                pendingMap.put("PENDING_LIST","PENDING_LIST");
                pendingMap.put("SCHEME_NAME",getTxtSchemeName());
                System.out.println("PENDING_LIST Data in MDSStandingInstruction OB : " + pendingMap);
                pendingMap = proxy.execute(pendingMap, map);
                if(pendingMap!=null && pendingMap.size()>0 && pendingMap.containsKey("PENDING_LIST")){
                    pendingList = (List) pendingMap.get("PENDING_LIST");
                }else{
                    pendingList =null;
                }
                System.out.println("############ pendingList :"+pendingList);
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void updateInterestData(){
        tblStandingInstruction= new EnhancedTableModel((ArrayList)finalList, tableTitle);
    }
    
    private HashMap calculateMDSStandingInstruction(String scheme_Name, String chittalNo, String sub_No, int divNo, int noOfInsPaid, int instDay, HashMap productMap,
            long pendingInst, long pendingInstallment, String bonusFirstInst, double insAmt, long noOfInstPay, double availableBalance) {
        HashMap calcMap = new HashMap();        
        int startNoForPenal = 0;
        int addNo = 1;
        bonusAmountList = new ArrayList();
        penalRealList = new ArrayList();
        penalList =new ArrayList();
        instList = new ArrayList();
        narrationList = new ArrayList();
        int firstInst_No = -1;
        if (bonusFirstInst.equals("Y")/* || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")*/) {
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
        double bonusAmt = 0;
        String penalIntType = "";
        //long penalValue = 0;
        double penalValue = 0;
        String penalGraceType = "";
        long penalGraceValue = 0;
        String penalCalcBaseOn = "";
        Date instDate = null;
        long diffDayPending = 0;
        Date currDate = (Date) curr_dt.clone();
        Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
        boolean bonusAvailabe = true;
        if (pendingInst >= 0) {              //pending installment calculation starts... 
            insDueAmt = (long) insAmt * pendingInst;
            int totPendingInst = (int) pendingInst;
            double calc = 0;
            long totInst = pendingInst;
            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
            if (getRdoPrizedMember_Yes() == true) {
                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                }
                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_INT_AMT"));
                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                if (penalGraceType.equals("Installments") && (penalIntType != null && penalIntType.equals("Percent"))) {
                    pendingInst -= penalGraceValue;
                }
            } else if (getRdoPrizedMember_No() == true) {
                if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                }
                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_INT_AMT"));
                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
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
                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                    if (listRec != null && listRec.size() > 0) {
                        nextInstMaps = (HashMap) listRec.get(0);
                    }
                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                    }

                }
           }

            double instAmount = 0.0;
            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                if (calculateIntOn.equals("Installment Amount")) {// Modified by nithya on 02-01-2018 for 0008272: Advance collection issue_MDS [ for solving array index out of bound exception ]
                    int predefinedIterator = j - 1;
                    instAmount = 0.0;
                     instAmount = CommonUtil.convertObjToDouble(insAmt);
                    if (bonusAmout != null && bonusAmout.size() > 0) {
                        if(bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")){
                            instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(predefinedIterator)); 
                        }else{
                            instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j)); 
                        }                        
                    }
                }
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", scheme_Name);
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                if (listRec != null && listRec.size() > 0) {
                     double penal = 0;
                    nextInstMap = (HashMap) listRec.get(0);
                    //Changed by sreekrishnan
                    if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("DRAW_AUCTION_DATE")));
                    }else{
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    }
                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                    //Holiday Checking - Added By Suresh
                    HashMap holidayMap = new HashMap();
                    boolean checkHoliday = true;
                    instDate = setProperDtFormat(instDate);
                    holidayMap.put("NEXT_DATE", instDate);
                    holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    while (checkHoliday) {
                        boolean tholiday = false;
                         boolean isHoliday=false;
                         boolean isWeekOff =false;
                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                        if(Holiday!=null||weeklyOf!=null){
                            if(Holiday!=null){
                         isHoliday = Holiday.size() > 0 ? true : false;}
                            if(weeklyOf!=null){
                         isWeekOff = weeklyOf.size() > 0 ? true : false;}
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
                        }
                        else
                        {
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
                                    }else{
                                        calc += (diffDayPending * penalValue * insAmt) / 36500;                                        
                                    }
                                    penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                    penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                }
                            }
                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                            // To be written
                            if (diffDayPending > penalGraceValue *instFrequency) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;
                                        }else{
                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    } else {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;
                                        }else{
                                            calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;                                            
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    }
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                    penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                }
                            }
                        }
                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                            if (diffDayPending > penalGraceValue) {
                                if(pendingInst == 0){
                                    pendingInst = 1;
                                }
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += (double) ((instAmount * penalValue) * 7 / 36500.0) * pendingInst--;                                            
                                        }else{
                                            calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    } else {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += ((instAmount * penalValue) / 1200.0) * pendingInst--; 
                                        }else{
                                            calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    }
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                    calc += penalValue;
                                    penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                }
                            }
                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                            // To be written
                            if (diffDayPending > penalGraceValue *instFrequency) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                    if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc = (double) ((pendingInst *(pendingInst+1)/2)*instAmount*penalValue)*7/36500 ;     
                                        }else{
                                            calc = (double) ((pendingInst *(pendingInst+1)/2)*insAmt*penalValue)*7/36500 ; 
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    } else {
                                        if ((calculateIntOn != null && !calculateIntOn.equals("")) && calculateIntOn.equals("Installment Amount")) {
                                            calc += (double) ((instAmount * penalValue) / 1200.0) * pendingInst--;    
                                        }else{
                                            calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                        }
                                        penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                    }
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                    penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                }
                            }
                        }
                    }
                    //After Scheme End Date Penal Calculating
                    if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, curr_dt) > 0)) {
                        //Below portion of code added by Jeffin John on 16-05-2014 for Mantis ID- 8858
                        HashMap c_hash = new HashMap();
                        c_hash.put("SCHEME_NAME", getTxtSchemeName());
                        List closedPenal=ClientUtil.executeQuery("getClosedRate", c_hash);
                        double clodespenalRt=0.0;
                        if(closedPenal!=null && closedPenal.size()>0){
                            for(int i = 0 ; i<closedPenal.size() ; i++){
                                HashMap penalRate = new HashMap();
                                penalRate =(HashMap) closedPenal.get(0);
                                if(penalRate.containsKey("CLOSED_PENAL")&& penalRate.get("CLOSED_PENAL")!=null){
                                    clodespenalRt=Double.parseDouble(((HashMap)closedPenal.get(0)).get("CLOSED_PENAL").toString());
                                }
                            }
                        }
                        //Code ends here
                        System.out.println("#### endDate : " + endDate);
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
                        if (penalIntType.equals("Percent")){
                            Date clsDate = new Date();
                            if (bonusFirstInst.equals("Y")) { 
                                clsDate = DateUtil.addDays(endDate,CommonUtil.convertObjToInt(penalGraceValue));                          
                            }else{
                                clsDate=DateUtil.addDays(endDate,(30+CommonUtil.convertObjToInt(penalGraceValue)));
                            }
                            System.out.println("penalGraceValue :: " + penalGraceValue);
                            diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (30 + CommonUtil.convertObjToInt(penalGraceValue))), currDate);
                            if (bonusFirstInst.equals("Y")) {
                                System.out.println("advance collection --- " + bonusFirstInst);
                                diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (CommonUtil.convertObjToInt(penalGraceValue))), currDate);
                            }
                            HashMap holidayCloseMap = new HashMap();
                            boolean checkHolidayAftrClosure = true;
                            System.out.println("instDate   " + clsDate);
                            clsDate = setProperDtFormat(clsDate);
                            System.out.println("instDate   " + clsDate);
                            holidayCloseMap.put("NEXT_DATE", clsDate);
                            holidayCloseMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                            while (checkHolidayAftrClosure) {
                                boolean tholiday = false;
                                System.out.println("enterytothecheckholiday" + checkHolidayAftrClosure);
                                List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayCloseMap);
                                List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayCloseMap);
                                boolean isHoliday = Holiday.size() > 0 ? true : false;
                                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                if (isHoliday || isWeekOff) {
                                    System.out.println("#### diffDay Holiday True : " + diffDayPending);
                                    if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                        diffDayPending -= 1;
                                        clsDate.setDate(clsDate.getDate() + 1);
                                    } else {
                                        diffDayPending += 1;
                                        clsDate.setDate(clsDate.getDate() - 1);
                                    }
                                    holidayCloseMap.put("NEXT_DATE", clsDate);
                                    checkHolidayAftrClosure = true;
                                    System.out.println("#### holidayMap : " + holidayCloseMap);
                                } else {
                                    System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                    checkHolidayAftrClosure = false;
                                }
                            }
                            System.out.println("#### endDate_diffDayPending : " + diffDayPending);
                            System.out.println("calc... " + calc);
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
            }
        }//pending installment calculation ends...


        //Discount calculation details Starts...
        for (int k = 0; k < noOfInstPay; k++) {
            HashMap nextInstMap = new HashMap();
            nextInstMap.put("SCHEME_NAME", scheme_Name);
            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
            if (listRec == null || listRec.size() == 0) {
                Date curDate = (Date) curr_dt.clone();
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
                System.out.println("instDate   " + instDate);
                instDate = setProperDtFormat(instDate);
                System.out.println("instDate   " + instDate);
                holidayMap.put("NEXT_DATE", instDate);
                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                while (checkHoliday) {
                    boolean tholiday = false;
                     boolean isWeekOff=false;
                    System.out.println("enterytothecheckholiday12" + checkHoliday);
                      boolean isHoliday=false;
                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                    if(Holiday!=null ||weeklyOf!=null){
                        if(Holiday!=null){
                     isHoliday = Holiday.size() > 0 ? true : false;
                        }
                        if(weeklyOf!=null){
                     isWeekOff = weeklyOf.size() > 0 ? true : false;}
                    if (isHoliday || isWeekOff) {
                        System.out.println("#### diffDay Holiday True : " + diffDay);
                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                            diffDay -= 1;
                            instDate.setDate(instDate.getDate() + 1);
                        } else {
                            diffDay += 1;
                            instDate.setDate(instDate.getDate() - 1);
                        }
                        holidayMap.put("NEXT_DATE", instDate);
                        checkHoliday = true;
                        System.out.println("#### holidayMap : " + holidayMap);
                    } else {
                        System.out.println("#### diffDay Holiday False : " + diffDay);
                        checkHoliday = false;
                    }
                    }
                    else
                    {
                    checkHoliday = false;
                    }
                }
                String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
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
                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
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
            HashMap nextInstMap = new HashMap();
            nextInstMap.put("SCHEME_NAME", scheme_Name);
            nextInstMap.put("DIVISION_NO", divNo);
            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
            if (listRec == null || listRec.size() == 0) {
                Date curDate = (Date) curr_dt.clone();
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
                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
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
                System.out.println("instDate   " + instDate);
                instDate = setProperDtFormat(instDate);
                System.out.println("instDate   " + instDate);
                holidayMap.put("NEXT_DATE", instDate);
                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                while (checkHoliday) {
                    boolean tholiday = false;
                     boolean isHoliday=false;
                     boolean isWeekOff=false;
                    System.out.println("enterytothecheckholiday21" + checkHoliday);
                    List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                    List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                    if(Holiday!=null||weeklyOf!=null){
                        if(Holiday!=null){
                     isHoliday = Holiday.size() > 0 ? true : false;}
                     if(weeklyOf!=null){
                    isWeekOff = weeklyOf.size() > 0 ? true : false;}
                    if (isHoliday || isWeekOff) {
                        System.out.println("#### diffDay Holiday True : " + diffDay);
                        if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                            diffDay -= 1;
                            instDate.setDate(instDate.getDate() + 1);
                        } else {
                            diffDay += 1;
                            instDate.setDate(instDate.getDate() - 1);
                        }
                        holidayMap.put("NEXT_DATE", instDate);
                        checkHoliday = true;
                        System.out.println("#### holidayMap : " + holidayMap);
                    } else {
                        System.out.println("#### diffDay Holiday False : " + diffDay);
                        checkHoliday = false;
                    }
                    }
                    else{
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
                        if (getRdoPrizedMember_Yes() == true) {
                            nextActMap.put("SCHEME_NAME",CommonUtil.convertObjToStr(scheme_Name));
                            nextActMap.put("DIVISION_NO", CommonUtil.convertObjToStr(divNo));
                            nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(l + noOfInsPaid));
                            List listAuc = ClientUtil.executeQuery("getSelectNextAuctDate", nextActMap);
                            if (listAuc != null && listAuc.size() > 0) {
                                nextActMap = (HashMap) listAuc.get(0);
                            }
                            Date drawAuctionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                            System.out.println("Date acution d" + drawAuctionDate);
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
                            long dateDiff = DateUtil.dateDiff(curr_dt, newDate);
                            //Holiday checking Added  By Nidhin 19/11/2014
                            HashMap holidayCheckMap = new HashMap();
                            boolean checkForHoliday = true;
                            newDate = setProperDtFormat(newDate);
                            holidayCheckMap.put("NEXT_DATE", newDate);
                            holidayCheckMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                            while (checkForHoliday) {
                                 boolean isHoliday=false;
                                 boolean isWeekOff=false;
                                 System.out.println("233334");
                                List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayCheckMap);
                                List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayCheckMap);
                                if(Holiday!=null||weeklyOf!=null){
                                    if(Holiday!=null){
                                 isHoliday = Holiday.size() > 0 ? true : false;}
                                 if(weeklyOf!=null){
                                 isWeekOff = weeklyOf.size() > 0 ? true : false;}
                                if (isHoliday || isWeekOff) {
                                    System.out.println("#### diffDay Holiday True : Bonus" + dateDiff);	
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
                            }
                            else
                        {
                            checkForHoliday = false;
                        }
                                
                            }
                            //End for HoliDay Checking
                            HashMap whereMap = new HashMap();
                            whereMap.put("CHITTAL_NO", chittalNo);
                            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(sub_No));
                            whereMap.put("SCHEME_NAME", scheme_Name);
                            List paymentList = ClientUtil.executeQuery("getSelectMDSPaymentDetails", whereMap);
                            if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                System.out.println("###### NO BONUS FOR PRODUCT PARAMETER");
                            } else  if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) { //akhila
                                System.out.println("in else");
                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                //long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                    }
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    }
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                    }
                                    bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                } else {
                                }
                            }
                            else if(productMap.get("PRIZED_OWNER_BONUS").equals("Y") || productMap.get("PRIZED_OWNER_BONUS").equals("N"))//is work prized owner yes or no
                            {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                }
                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                totBonusAmt = totBonusAmt + bonusAmt;
                                }
                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                totBonusAmt = totBonusAmt + bonusAmt;
                                }
                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                            } else {
                            }
                           
                            }
                            else
                            {
                                
                            }
                             //akhila
                        } else if (getRdoPrizedMember_No() == true) {
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
                            }
                        }
                    }

                }
            }
            System.out.println("totBonusAmt  ssss  "+totBonusAmt);
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
         System.out.println("totBonusAmt  qqqqqq  "+totBonusAmt);
        netAmt = (double) (noOfInstPay * insAmt) + penalAmt - (totBonusAmt + totDiscAmt);
        insAmtPayable = (noOfInstPay * insAmt) - (totBonusAmt + totDiscAmt);
        System.out.println("####### netAmt        : " + netAmt);
        System.out.println("####### insAmtPayable : " + insAmtPayable);
        System.out.println("####### noOfInstPay : " + noOfInstPay);
       if (netAmt > availableBalance) {
           if (noOfInstPay != 1) {
               noOfInstPay = noOfInstPay - 1;
                pendingInst = pendingInstallment;
                HashMap testMap = calculateMDSStandingInstruction(scheme_Name, chittalNo, sub_No, divNo, noOfInsPaid, instDay, productMap,
                        pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance);
                System.out.println("test mappp"+testMap);
                return testMap;
            }
        }       
        //Temp for debit account balance checking
        if(tempDebitAc.containsKey(productMap.get("DR_ACT_NO"))){
            if (CommonUtil.convertObjToDouble(tempDebitAc.get(productMap.get("DR_ACT_NO")))<netAmt) {
                if((noOfInstPay = noOfInstPay - 1)>0)  {                    
                   pendingInst = pendingInstallment;
                   HashMap testMap2 = calculateMDSStandingInstruction(scheme_Name, chittalNo, sub_No, divNo, noOfInsPaid, instDay, productMap,
                        pendingInst, pendingInstallment, bonusFirstInst, insAmt, noOfInstPay, availableBalance);
                    return testMap2;    
                }else{                       //If you want set 0 as minimum noOfInst then comment the else part.
                    noOfInstPay = 1;
                } 
            }else{
                tempDebitAc.put(productMap.get("DR_ACT_NO"),CommonUtil.convertObjToDouble(tempDebitAc.get(productMap.get("DR_ACT_NO")))-netAmt);
            }
        }else{
            tempDebitAc.put(productMap.get("DR_ACT_NO"),availableBalance-netAmt); 
        }
        //Code ends..
        
            double bonusN=0.0;
            for(int h=0;h<bonusAmountList.size();h++)
            {
               bonusN=CommonUtil.convertObjToDouble(bonusAmountList.get(h)); 
            }
            
            //For spliting transcations..
                
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
              splitTransMap = new HashMap();
              ArrayList RealBonusAmountList = new ArrayList();
              ArrayList RealPenalList =new ArrayList();
              ArrayList RealInstList = new ArrayList();
                //int noOfInstToPay = CommonUtil.convertObjToInt(txtNoOfInstToPaay.getText());
                //for (int i = 0; i < noOfInstToPay; i++) {
                 //   setNarrationToSplitTransaction(i);
                //}
                if(penalList != null && penalList.size() >0){
                    double d = 0;
                    double firVal = CommonUtil.convertObjToDouble(penalList.get(0));
                    Collections.reverse(penalList);
                    for (int i = 0; i <= penalList.size(); i++) {
                        if(i+1 < penalList.size()){
                            d = CommonUtil.convertObjToDouble(penalList.get(i))-CommonUtil.convertObjToDouble(penalList.get(i+1));
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
                for(int k = 0; k < noOfInstPay; k++){
                    if (noOfInstPay > 0) {
                        RealBonusAmountList.add(bonusAmountList.get(CommonUtil.convertObjToInt(k)));
                        RealPenalList.add(penalRealList.get(CommonUtil.convertObjToInt(k)));
                        RealInstList.add(instList.get(CommonUtil.convertObjToInt(k)));
                    }
                } 
                splitTransMap.put("BONUS_AMT_LIST", RealBonusAmountList);
                splitTransMap.put("INST_AMT_LIST", RealInstList);
                splitTransMap.put("PENAL_AMT_LIST", RealPenalList);
                splitTransMap.put("NARRATION_LIST", narrationList);  
                splitTransMap.put("CHITTAL_NO", chittalNo);  
                splitTransMap.put("CHITTAL_NO", chittalNo);  
                splitTransMap.put("SUB_NO", sub_No);  
                splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
                splitTransMap.put("INSTALL_NO", noOfInsPaid);
                finalSplitList.add(splitTransMap); 
            }
            //
        calcMap.put("NET_AMOUNT", String.valueOf(netAmt));
        calcMap.put("INST_AMOUNT_PAYABLE", String.valueOf(insAmtPayable));
        calcMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
        calcMap.put("BONUS", CurrencyValidation.formatCrore(String.valueOf(totBonusAmt)));
        calcMap.put("DISCOUNT", CurrencyValidation.formatCrore(String.valueOf(totDiscAmt)));
       // calcMap.put("PENAL", CurrencyValidation.formatCrore(String.valueOf(penalAmt)));
        calcMap.put("PENAL",String.valueOf(penalAmt));
        calcMap.put("BONUS_NEW",String.valueOf(bonusN));             
        //calcMap.put("AVAILABLE_BALANCE",availableBalance-netAmt);
        System.out.println("####### calcMap iiiii : " + calcMap + "Chittal Numner :"+chittalNo);
        return calcMap;
    }    
    
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)curr_dt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    
    /** To perform the necessary operation */
    public void doAction(List finalTableList) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform(finalTableList);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }
    
    public void fillProductId() throws Exception{
        setCbmProdId();
    }
    public void setCbmProdId() {
        //if (prodType.equals("GL")) {
        //    key = new ArrayList();
        //    value = new ArrayList();
        //} else {
           try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProductOASA");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        //}
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
    /** To perform the necessary action */
    private void doActionPerform(List finalTableList) throws Exception{
        final HashMap data = new HashMap();
        if(finalTableList!= null && finalTableList.size()>0){
            data.put("MDS_STANDING_INSTRUCTION",finalTableList);
            data.put("SCHEME_NAME",getTxtSchemeName());
            if(MDSbonusMap != null){
             data.put("MDS_BONUS_MAP",MDSbonusMap);
            }                      
        }
        if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
            data.put("MDS_STANDING_INSTRUCTION_SPLIT_LIST", setFinalSplitList());  
            data.put("MDS_SPLIT_TRANSACTION", isSplitMDSTransaction);    
        }   
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("Data in MDSStandingInstruction OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }
    
    public HashMap setFinalSplitList(){// Modified the function by nithya for KD 504 -  MDS Split transaction issue
         HashMap finalSplitChitalMap =  new HashMap();
       if(finalSplitList!=null && finalSplitList.size()>0){
           HashMap splitMap = new HashMap();           
           for(int i = 0;i<finalSplitList.size();i++){
               splitMap = (HashMap) finalSplitList.get(i);
               System.out.println("splitMap%$$^^$^$^$^"+splitMap);
               for (int j = 0; j < tblStandingInstruction.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 1)).equals(CommonUtil.convertObjToStr(splitMap.get("CHITTAL_NO"))) 
                       && CommonUtil.convertObjToStr(tblStandingInstruction.getValueAt(j, 2)).equals(CommonUtil.convertObjToStr(splitMap.get("SUB_NO")))
                            && !((Boolean) tblStandingInstruction.getValueAt(j, 0)).booleanValue()) {
                        finalSplitList.remove(i--);
                    }
               }
           }
           System.out.println("finalSplitList$^^$^$^$"+finalSplitList);          
           for(int l=0; l<finalSplitList.size();l++){
               HashMap finalMap = (HashMap) finalSplitList.get(l);
               finalSplitChitalMap.put(finalMap.get("CHITTAL_NO"),finalMap);
           }
       }
       return  finalSplitChitalMap;
    }
    
    public void resetForm(){
        pendingList = null;
        resetTableValues();
        setChanged();
    }
     
     public void resetTableValues(){
        tblStandingInstruction.setDataArrayList(null,tableTitle);
    }
    
    /**
     * Getter for property tblStandingInstruction.
     * @return Value of property tblStandingInstruction.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblStandingInstruction() {
        return tblStandingInstruction;
    }    
      
    /**
     * Setter for property tblStandingInstruction.
     * @param tblStandingInstruction New value of property tblStandingInstruction.
     */
    public void setTblStandingInstruction(com.see.truetransact.clientutil.EnhancedTableModel tblStandingInstruction) {
        this.tblStandingInstruction = tblStandingInstruction;
    }    
    
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }
    
    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }
    
    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }
    
    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
    
    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }    
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }    
    
    /**
     * Getter for property txtSchemeName.
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }
    
    /**
     * Setter for property txtSchemeName.
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }
       
    /**
     * Getter for property pendingList.
     * @return Value of property pendingList.
     */
    public java.util.List getPendingList() {
        return pendingList;
    }
    
    /**
     * Setter for property pendingList.
     * @param pendingList New value of property pendingList.
     */
    public void setPendingList(java.util.List pendingList) {
        this.pendingList = pendingList;
    }

    public HashMap getChittalPenalAmtMap() {
        return chittalPenalAmtMap;
    }

    public void setChittalPenalAmtMap(HashMap chittalPenalAmtMap) {
        this.chittalPenalAmtMap = chittalPenalAmtMap;
    }
    
    
    
}