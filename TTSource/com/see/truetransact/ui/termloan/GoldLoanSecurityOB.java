/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityOB.java
 *
 * Created on July 6, 2004, 12:23 PM
 */

package com.see.truetransact.ui.termloan;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.GoldLoanSecurityTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import javax.swing.table.TableModel;

public class GoldLoanSecurityOB extends CObservable{
    
    /** Creates a new instance of TermLoanSecurityOB */
    public GoldLoanSecurityOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanSecurityOB();
    }
    
    private       static GoldLoanSecurityOB termLoanSecurityOB;
    private       static GoldLoanOB termLoanOB;
    
    private final static Logger log = Logger.getLogger(GoldLoanSecurityOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private double limitAmount = 0.0;
//    private double totalSecurityValue = 0.0;
    private double avgSecurityValue = 0.0;
    private int actionType;
    private final   String  ALL = "ALL";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  ASON = "ASON";
    private final   String  COLLATERAL = "COLLATERAL";
    private final   String  COMMAND = "COMMAND";
    private final   String  COMMODITY_ITEM = "COMMODITY_ITEM";
    
    private final   int  SLNO = 0;
    private final   String  TDTASONDATE = "TDTASONDATE";
    private final   String  GROSS_WEIGHT = "GROSS_WEIGHT";
    private final   String  NET_WEIGHT = "NET_WEIGHT";
    private final   String  PURITY = "PURITY";
    private final   String  MARKET_RATE = "MARKET_RATE";
    private final   String  SECURITY_VALUE = "AVAIL_SECURITY";
    private final   String  MARGIN = "MARGIN";
    private final   String  MARGIN_AMT = "MARGIN_AMT";
    private final   String  ELIGIBLE_AMT = "ELIGIBLE_AMT";
    private final   String  TOTAL_SECURITY_VALUE = "TOTAL_SECURITY_VALUE";
    private final   String  TOTAL_MARGIN_AMT = "TOTAL_MARGIN_AMT";
    private final   String  TOTAL_ELIGIBLE_AMT = "TOTAL_ELIGIBLE_AMT";    
    private final   String  APPRAISER_ID = "APPRAISER_ID";
    private final   String  NOOFPACKET = "NOOFPACKET";
    private final   String  PARTICULARS = "PARTICULARS";

    private final   String  CUSTOMER_NAME = "CUSTOMER NAME";
    private final   String  DATE_CHARGE = "DATE_CHARGE";
    private final   String  DATE_INSPECTION = "DATE_INSPECTION";
    private final   String  INSERT = "INSERT";
    private final   String  LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final   String  MILL_INDUS = "MILL_INDUS";
    private final   String  NATURE_CHARGE = "NATURE_CHARGE";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
//    private final   String  PARTICULARS = "PARTICULARS";
    private final   String  PRIMARY = "PRIMARY";
    //    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  SECURITY_CATEGORY = "SECURITY_CATEGORY";
    private final   String  SECURITY_DETAILS = "SECURITY_DETAILS";
    private final   String  SECURITY_TYPE = "SECURITY_TYPE";
    private final   String  STOCK_STATE_FREQ = "STOCK_STATE_FREQ";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    private final   String  YES = "Y";
    
    private final   ArrayList securityTabTitle = new ArrayList();       //  Table Title of Security
    private final   ArrayList depositSecurityTabTitle = new ArrayList();
    private ArrayList securityTabValues = new ArrayList();
    private ArrayList securityEachTabRecord;
    
    private LinkedHashMap securityAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap securityEachRecord;
    private HashMap oldEligibleLoanAmtMap;
    
    private EnhancedTableModel tblSecurityTab;
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.GoldLoanRB", ProxyParameters.LANGUAGE);
    
    private TableUtil tableUtilSecurity = new TableUtil();
    
    private String borrowerNo = "";
    private String txtSecurityNo = "";
    private String txtCustID_Security = "";
    private String txtSecurityValue = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtEligibleLoan = "";
    private String lblProdId_Disp = "";
    private String lblAccHeadSec_2 = "";
    private String lblAccNoSec_2 = "";
    private String lblCustName_Security_Display = "";
    private String strACNumber = "";
    
    private ComboBoxModel cbmPurityOfGold;
    private ComboBoxModel cbmItem;
    private ComboBoxModel cbmRenewalPurityOfGold;
    private ComboBoxModel cbmRenewalAppraiserId;
    private String cboPurityOfGold = "";
    private String slNo = "";
    private String tdtAson = "";
    private String txtGrossWeight = "";
    private String txtNetWeight = "";
    private String txtMarketRate = "";
    private String securityValue = "";
    private String totalSecurityValue = "";
    private String txtMargin = "";
    private String txtMarginAmt = "";
    private String totalMarginAmt = "";
    private String txtEligibleLoanAmt = "";
    private String totalEligibleLoanAmt = "";
    private String txtAppraiserId = "";
      private String txtRenewalAppraiserId = "";
    private String lblAppraiserNameValue = "";
    private String txtAreaParticular = "";
    private String finalSecurityValue = "";
    private String finalMarginAmt= "";
    private String finalEligibleLoanAmt = "";

//    private String txtQty = "";
//    private String txtSecurityRemarks = "";
    private String txtNoOfPacket = "";
   

    private String txtRenewalGrossWeight = ""; 
    private String txtRenewalNetWeight = ""; 
    private String cboRenewalPurityOfGold = ""; 
    private String txtRenewalMarketRate = ""; 
    private String txtRenewalSecurityValue = ""; 
    private String txtRenewalAreaParticular = ""; 
    private String txtRenewalMargin = ""; 
    private String txtRenewalMarginAmount = ""; 
    private String txtRenewalEligibleLoan="";
    private String cboRenewalAppraiserId="";


    Date curDate = null;
    
//    static {
//        try {
//            termLoanSecurityOB = new GoldLoanSecurityOB();
//        } catch(Exception e) {
//            log.info("try: " + e);
//            parseException.logException(e,true);
//        }
//    }
    
    private void termLoanSecurityOB()  throws Exception{
        setSecurityTabTitle();
        setDepositSecurityTabTitle();
        tableUtilSecurity.setAttributeKey(CommonUtil.convertObjToStr(SLNO));
        tblSecurityTab = new EnhancedTableModel(null, securityTabTitle);
        oldEligibleLoanAmtMap = new HashMap();
    }
    
//    public static GoldLoanSecurityOB getInstance() {
//        return termLoanSecurityOB;
//    }
    
    private void setSecurityTabTitle() throws Exception{
        try{
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecuritySLNO"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityGross"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityEligibleAmt"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityMargin"));
            securityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityEligible"));
        }catch(Exception e){
            log.info("Exception in setSecurityTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getSecurityTabTitle(){
        return this.securityTabTitle;
    }
    
    private void setDepositSecurityTabTitle() throws Exception{
        try{
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnDepositNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnDepositSubNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnLienNo"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnSecurityValue"));
            depositSecurityTabTitle.add(objTermLoanRB.getString("tblColumnLienDt"));
        }catch(Exception e){
            log.info("Exception caught in setDepositSecurityTabTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getDepositSecurityTabTitle(){
        return this.depositSecurityTabTitle;
    }
    
    
    /**
     * Setter for property tblSecurityLoanTab.
     *
     */
    public void setTblDepositSecurityTable() {
        tblSecurityTab = new EnhancedTableModel(null, getDepositSecurityTabTitle());
    }
    
    /**
     * Setter for property tblSecurityLoanTab.
     *
     */
    public void setTblSecurityTable() {
        tblSecurityTab = new EnhancedTableModel(null, getSecurityTabTitle());
    }
    
    public void resetAllSecurityDetails(){
        resetSecurityDetails();
        setLblProdId_Disp("");
        setLblAccHeadSec_2("");
        setLblAccNoSec_2("");
        oldEligibleLoanAmtMap = null;
        oldEligibleLoanAmtMap = new HashMap();
    }
    
    public void resetSecurityDetails(){
        setSlNo("");
        setTdtAson("");
        setTxtGrossWeight("");
        setTxtNetWeight("");
        setSecurityValue("");
//        setTxtMargin("");
//        setEligibleAmt("");
        setCboPurityOfGold("");
        setTxtMarketRate("");
        setTxtAreaParticular("");
    }
    
    public void resetSecurityTableUtil(){
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(CommonUtil.convertObjToStr(SLNO));
    }
    
    public void changeStatusSecurity(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilSecurity.getRemovedValues().clear();
            }
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Security Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    securityAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusSecurity(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private String getEligibleLoanAmtKey(String custID, String strSecurityNo) throws Exception{
        return custID + "#" + strSecurityNo;
    }
    
    public void setTermLoanSecurityTO(ArrayList objSecurityTOList, String act_Num, String strFaciType){
        try{
            GoldLoanSecurityTO objTermLoanSecurityTO;
            HashMap securityRecordMap;
            ArrayList securityRecordList;
            ArrayList securityCTableValues = new ArrayList();
            ArrayList removedValues = new ArrayList();
            LinkedHashMap allLocalRecs = new LinkedHashMap();
            oldEligibleLoanAmtMap = null;
            oldEligibleLoanAmtMap = new HashMap();
            String strKey = "";
            // To retrieve the Security Details from the Database
            for (int i = objSecurityTOList.size() - 1,j = 0;i >= 0;--i,++j){
                objTermLoanSecurityTO = (GoldLoanSecurityTO) objSecurityTOList.get(j);
                securityRecordMap  = new HashMap();
                securityRecordList = new ArrayList();
                
                securityRecordList.add(CommonUtil.convertObjToInt(objTermLoanSecurityTO.getSlNo()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getGrossWeight()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityValue()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMarginAmt()));
                securityRecordList.add(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getEligibleLoanAmt()));
                
                securityCTableValues.add(securityRecordList);
                
                securityRecordMap.put(SLNO, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSlNo()));
                securityRecordMap.put(TDTASONDATE, DateUtil.getStringDate(objTermLoanSecurityTO.getAsOn()));
                securityRecordMap.put(GROSS_WEIGHT, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getGrossWeight()));
                securityRecordMap.put(NET_WEIGHT, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getNetWeight()));
                securityRecordMap.put(PURITY, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getPurity()));
                securityRecordMap.put(MARKET_RATE, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMarketRate()));
                securityRecordMap.put(SECURITY_VALUE, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityValue()));
                securityRecordMap.put(TOTAL_SECURITY_VALUE, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getTotalSecurityValue()));
                securityRecordMap.put(MARGIN, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMargin()));
                securityRecordMap.put(MARGIN_AMT, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getMarginAmt()));
                securityRecordMap.put(ELIGIBLE_AMT, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getEligibleLoanAmt()));
                securityRecordMap.put(APPRAISER_ID, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
                securityRecordMap.put(NOOFPACKET, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getNoofPacket()));
                setTxtAppraiserId(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
                securityRecordMap.put(PARTICULARS, CommonUtil.convertObjToStr(objTermLoanSecurityTO.getParticulars()));
                securityRecordMap.put("STATUS", CommonConstants.STATUS_CREATED);
                
                securityRecordMap.put(COMMAND, UPDATE);
                
                
                
                
//                strKey = getEligibleLoanAmtKey(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getCustId()), CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
//                oldEligibleLoanAmtMap.put(strKey, objTermLoanSecurityTO.getEligibleLoanAmt());
                allLocalRecs.put(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSlNo()), securityRecordMap);  //List of values corresponding to the table
                
                strKey = null;
                securityRecordMap  = null;
                securityRecordList = null;
            }
            securityTabValues.clear();
            securityAll.clear();
            securityTabValues = securityCTableValues;
            securityAll = allLocalRecs;
            if (strFaciType.equals(LOANS_AGAINST_DEPOSITS)){
                setTblDepositSecurityTable();
            }else{
                tblSecurityTab.setDataArrayList(securityTabValues, getSecurityTabTitle());
                tableUtilSecurity.setRemovedValues(removedValues);
                tableUtilSecurity.setAllValues(securityAll);
                tableUtilSecurity.setTableValues(securityTabValues);
                setMax_Del_Security_No(act_Num);
            }
            securityCTableValues = null;
            allLocalRecs = null;
        }catch(Exception e){
            log.info("Error in setTermLoanSecurityTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    private void setMax_Del_Security_No(String act_Num){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("acctNum", act_Num);
            List resultList = ClientUtil.executeQuery("getSelectTermLoanSecurityMaxSLNO", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilSecurity.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_Security_No: "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setTermLoanSecurity(){
        HashMap securityMap = new HashMap();
        try{
            GoldLoanSecurityTO objTermLoanSecurityTO;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            double reducingMarginAmt = 0.0;
            double reducingEligibleAmt = 0.0;
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) securityAll.get(objKeySet[j]);
                objTermLoanSecurityTO = new GoldLoanSecurityTO();
                objTermLoanSecurityTO.setSlNo(CommonUtil.convertObjToInt(oneRecord.get(SLNO)));
//                objTermLoanSecurityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                objTermLoanSecurityTO.setAsOn(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TDTASONDATE))));
                objTermLoanSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(oneRecord.get(GROSS_WEIGHT)));
                objTermLoanSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(oneRecord.get(NET_WEIGHT)));
                objTermLoanSecurityTO.setPurity(CommonUtil.convertObjToStr(oneRecord.get(PURITY)));
                objTermLoanSecurityTO.setMarketRate(CommonUtil.convertObjToStr(oneRecord.get(MARKET_RATE)));
                objTermLoanSecurityTO.setSecurityValue(CommonUtil.convertObjToStr(oneRecord.get(SECURITY_VALUE)));
                objTermLoanSecurityTO.setTotalSecurityValue(CommonUtil.convertObjToStr(oneRecord.get(TOTAL_SECURITY_VALUE)));
                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToStr(oneRecord.get(MARGIN)));
                
                double perGramAmt = CommonUtil.convertObjToDouble(oneRecord.get(MARKET_RATE)).doubleValue();
                double totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(oneRecord.get(NET_WEIGHT)).doubleValue();
                double margin = CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)).doubleValue();
                double totMarginAmt = ((margin * CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)).doubleValue())/100);
                double totEligibleAmt = CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)).doubleValue() - totMarginAmt;
                objTermLoanSecurityTO.setTotalSecurityValue(String.valueOf(totSecurityValue));
                objTermLoanSecurityTO.setMarginAmt(String.valueOf(totMarginAmt));
                objTermLoanSecurityTO.setEligibleLoanAmt(String.valueOf(totEligibleAmt));
                objTermLoanSecurityTO.setAppraiserId(CommonUtil.convertObjToStr(oneRecord.get(APPRAISER_ID)));
                objTermLoanSecurityTO.setParticulars(CommonUtil.convertObjToStr(oneRecord.get(PARTICULARS)));
//                objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                objTermLoanSecurityTO.setStatusDt((Date)curDate.clone());
                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    objTermLoanSecurityTO.setCommand(INSERT);
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (oneRecord.get(COMMAND).equals(UPDATE)){
                    objTermLoanSecurityTO.setCommand(UPDATE);
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityTO.setStatusDt(curDate);
                securityMap.put(String.valueOf(j+1), objTermLoanSecurityTO);
                oneRecord = null;
                objTermLoanSecurityTO = null;
            }
            
            // To set the values for Security Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilSecurity.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) tableUtilSecurity.getRemovedValues().get(j);
                objTermLoanSecurityTO = new GoldLoanSecurityTO();
                objTermLoanSecurityTO.setSlNo(CommonUtil.convertObjToInt(oneRecord.get(SLNO)));
//                objTermLoanSecurityTO.setAcctNum(CommonUtil.convertObjToStr(oneRecord.get(ACCOUNT_NO)));
//                objTermLoanSecurityTO.setBorrowNo(getBorrowerNo());
//                objTermLoanSecurityTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUSTOMER_ID)));
                objTermLoanSecurityTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setFromDt((Date)oneRecord.get(FROM_DATE));
//                objTermLoanSecurityTO.setFromDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(FROM_DATE))));
//                objTermLoanSecurityTO.setSecurityNo(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_NO)));
//                objTermLoanSecurityTO.setSlno(CommonUtil.convertObjToDouble(oneRecord.get(SLNO)));
//                objTermLoanSecurityTO.setSecurityAmt(CommonUtil.convertObjToDouble(oneRecord.get(SECURITY_VALUE)));
//                objTermLoanSecurityTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
//                objTermLoanSecurityTO.setToDt((Date)oneRecord.get(TO_DATE));
//                objTermLoanSecurityTO.setToDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get(TO_DATE))));
//                objTermLoanSecurityTO.setMargin(CommonUtil.convertObjToDouble(oneRecord.get(MARGIN)));
//                objTermLoanSecurityTO.setEligibleLoanAmt(CommonUtil.convertObjToDouble(oneRecord.get(ELIGIBLE_LOAN)));
                objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanSecurityTO.setStatusDt(curDate);
                securityMap.put(String.valueOf(securityMap.size()+1), objTermLoanSecurityTO);
                oneRecord = null;
                objTermLoanSecurityTO = null;
            }
            
            securityMap.put("OLD_ELIGIBLE_LOAN_AMT", oldEligibleLoanAmtMap);
        }catch(Exception e){
            log.info("Error In setTermLoanSecurity()..."+e);
            parseException.logException(e,true);
        }
        return securityMap;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
     public GoldLoanSecurityTO setGoldLoanSecurityTO() {
         
         final GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
         try{
             objTermLoanSecurityTO.setAsOn((Date)curDate.clone());
             objTermLoanSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(getTxtGrossWeight()));
             objTermLoanSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(getTxtNetWeight()));
             objTermLoanSecurityTO.setPurity(getCboPurityOfGold());
             objTermLoanSecurityTO.setMarketRate(getTxtMarketRate());
             objTermLoanSecurityTO.setSecurityValue(getSecurityValue());
             objTermLoanSecurityTO.setMargin(getTxtMargin());
             objTermLoanSecurityTO.setMarginAmt(getTotalMarginAmt());
             objTermLoanSecurityTO.setEligibleLoanAmt(getTotalEligibleLoanAmt());
             objTermLoanSecurityTO.setAppraiserId(getTxtAppraiserId());
             objTermLoanSecurityTO.setNoofPacket(CommonUtil.convertObjToInt(getTxtNoOfPacket()));
             objTermLoanSecurityTO.setParticulars(getTxtAreaParticular());
             objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
             objTermLoanSecurityTO.setStatusDt((Date)curDate.clone());
             
         }catch(Exception e){
             log.info("Error In setGoldLoanSecurityTO()");
             e.printStackTrace();
         }
         return objTermLoanSecurityTO;
     }
      public GoldLoanSecurityTO setGoldLoanRenewalSecurityTO() {
         
         final GoldLoanSecurityTO objTermLoanSecurityTO = new GoldLoanSecurityTO();
         try{
             objTermLoanSecurityTO.setAsOn((Date)curDate.clone());
             objTermLoanSecurityTO.setGrossWeight(CommonUtil.convertObjToDouble(getTxtRenewalGrossWeight()));
             objTermLoanSecurityTO.setNetWeight(CommonUtil.convertObjToDouble(getTxtRenewalNetWeight()));
             objTermLoanSecurityTO.setPurity(getCboRenewalPurityOfGold());
             objTermLoanSecurityTO.setMarketRate(getTxtRenewalMarketRate());
             objTermLoanSecurityTO.setSecurityValue(getTxtRenewalSecurityValue());   ///check
             objTermLoanSecurityTO.setMargin(getTxtRenewalMargin());
             objTermLoanSecurityTO.setMarginAmt(getTxtRenewalMarginAmount());
             objTermLoanSecurityTO.setEligibleLoanAmt(getTxtRenewalEligibleLoan());
             objTermLoanSecurityTO.setAppraiserId(CommonUtil.convertObjToStr(getCboRenewalAppraiserId())); //getCbmRenewalAppraiserId().getKeyForSelected()));
             objTermLoanSecurityTO.setParticulars(getTxtRenewalAreaParticular());
             objTermLoanSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
             objTermLoanSecurityTO.setStatusDt((Date)curDate.clone());
             objTermLoanSecurityTO.setNoofPacket(CommonUtil.convertObjToInt(getTxtNoOfPacket()));
         }catch(Exception e){
             log.info("Error In setGoldLoanSecurityTO()");
             e.printStackTrace();
         }
         return objTermLoanSecurityTO;
     }
      
      public void populateRenewalSecurityData(GoldLoanSecurityTO objTermLoanSecurityTO) {
         setTxtRenewalGrossWeight(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getGrossWeight()));
         setTxtRenewalNetWeight(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getNetWeight()));
         setCboRenewalPurityOfGold(objTermLoanSecurityTO.getPurity());
         setTxtRenewalMarketRate(objTermLoanSecurityTO.getMarketRate());
         setTxtRenewalSecurityValue(objTermLoanSecurityTO.getSecurityValue());
         setTxtRenewalMargin(objTermLoanSecurityTO.getMargin());
         setTxtRenewalMarginAmount(objTermLoanSecurityTO.getMarginAmt());
         setTxtRenewalEligibleLoan(objTermLoanSecurityTO.getEligibleLoanAmt());
         System.out.println("swecid==="+objTermLoanSecurityTO.getAppraiserId());
         System.out.println("getCbmRenewalAppraiserId()==="+getCbmRenewalAppraiserId());
         if(objTermLoanSecurityTO.getAppraiserId()!=null && getCbmRenewalAppraiserId()!=null)
         getCbmRenewalAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
         setCboRenewalAppraiserId(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAppraiserId()));
         setTxtRenewalAreaParticular(objTermLoanSecurityTO.getParticulars());
         HashMap appraiserMap = new HashMap();
         appraiserMap.put("EMPLOYEE_CODE",getTxtAppraiserId());
//         List lst = ClientUtil.executeQuery("getSelectAppraiserId", appraiserMap);
//         if(lst!=null && lst.size()>0){
//             appraiserMap = (HashMap)lst.get(0);
////             setLblRAppraiserNameValue(CommonUtil.convertObjToStr(appraiserMap.get("EMPLOYEE_NAME")));
//         }
    }
     public void populateSecurityData(GoldLoanSecurityTO objTermLoanSecurityTO) {
         setTxtGrossWeight(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getGrossWeight()));
         setTxtNetWeight(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getNetWeight()));
         setCboPurityOfGold(objTermLoanSecurityTO.getPurity());
         setTxtMarketRate(objTermLoanSecurityTO.getMarketRate());
         setSecurityValue(objTermLoanSecurityTO.getSecurityValue());
         setTxtMargin(objTermLoanSecurityTO.getMargin());
         setTotalMarginAmt(objTermLoanSecurityTO.getMarginAmt());
         setTotalEligibleLoanAmt(objTermLoanSecurityTO.getEligibleLoanAmt());
         setTxtAppraiserId(objTermLoanSecurityTO.getAppraiserId());
         setTxtAreaParticular(objTermLoanSecurityTO.getParticulars());
         setTxtNoOfPacket(CommonUtil.convertObjToStr(objTermLoanSecurityTO.getNoofPacket()));
         HashMap appraiserMap = new HashMap();
         appraiserMap.put("EMPLOYEE_CODE",getTxtAppraiserId());
         List lst = ClientUtil.executeQuery("getSelectAppraiserId", appraiserMap);
         if(lst!=null && lst.size()>0){
             appraiserMap = (HashMap)lst.get(0);
             setLblAppraiserNameValue(CommonUtil.convertObjToStr(appraiserMap.get("EMPLOYEE_NAME")));
         }
    }

    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    public void setLblAccHeadSec_2(String lblAccHeadSec_2){
        this.lblAccHeadSec_2 = lblAccHeadSec_2;
        setChanged();
    }
    
    public String getLblAccHeadSec_2(){
        return this.lblAccHeadSec_2;
    }
    
    public void setLblAccNoSec_2(String lblAccNoSec_2){
        this.lblAccNoSec_2 = lblAccNoSec_2;
        setChanged();
    }
    
    public String getLblAccNoSec_2(){
        return this.lblAccNoSec_2;
    }
    
    void setTblSecurityTab(EnhancedTableModel tblSecurityTab){
        log.info("In setTblSecurityTab()...");
        
        this.tblSecurityTab = tblSecurityTab;
        setChanged();
    }
    
    EnhancedTableModel getTblSecurityTab(){
        return this.tblSecurityTab;
    }
    
    void setLblProdId_Disp(String lblProdId_Disp){
        this.lblProdId_Disp = lblProdId_Disp;
        setChanged();
    }
    String getLblProdId_Disp(){
        return this.lblProdId_Disp;
    }
    
    void setTxtSecurityNo(String txtSecurityNo){
        this.txtSecurityNo = txtSecurityNo;
        setChanged();
    }
    String getTxtSecurityNo(){
        return this.txtSecurityNo;
    }
    
    void setTxtSecurityValue(String txtSecurityValue){
        this.txtSecurityValue = txtSecurityValue;
        setChanged();
    }
    String getTxtSecurityValue(){
        return this.txtSecurityValue;
    }
    
    void setTdtFromDate(String tdtFromDate){
        this.tdtFromDate = tdtFromDate;
        setChanged();
    }
    String getTdtFromDate(){
        return this.tdtFromDate;
    }
    
    void setTdtToDate(String tdtToDate){
        this.tdtToDate = tdtToDate;
        setChanged();
    }
    String getTdtToDate(){
        return this.tdtToDate;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    public int addSecurityDetails(int recordPosition, boolean update){
        int option = -1;
        try{
            double tempTotSecVal = 0.0;
            termLoanOB = new GoldLoanOB();
            
            securityEachTabRecord = new ArrayList();
            securityEachRecord = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblSecurityTab.getDataArrayList();
            tblSecurityTab.setDataArrayList(data, securityTabTitle);
            final int dataSize = data.size();
            insertSecurity(dataSize+1);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilSecurity.insertTableValues(securityEachTabRecord, securityEachRecord);
                
                securityTabValues = (ArrayList) result.get(TABLE_VALUES);
                securityAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
//                String securityNo = ((String) ((ArrayList) tblSecurityTab.getDataArrayList().get(tblSecurityTab.getRowCount() - 1)).get(0));
                
            }else{
                option = updateSecurityTab(recordPosition);
            }            
            setChanged();            
            securityEachTabRecord = null;
            securityEachRecord = null;
            result = null;
            data = null;
            termLoanOB = null;
        }catch(Exception e){
            log.info("Error in addSecurityDetails()..."+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    private void insertSecurity(int recordPosition){
        securityEachTabRecord.add(String.valueOf(recordPosition));
        securityEachTabRecord.add(getTxtGrossWeight());
        securityEachTabRecord.add(getSecurityValue());
        securityEachTabRecord.add(getTxtMarginAmt());
        securityEachTabRecord.add(getTxtEligibleLoanAmt());

        securityEachRecord.put(SLNO, String.valueOf(recordPosition));
        securityEachRecord.put(TDTASONDATE,getTdtAson());
        securityEachRecord.put(GROSS_WEIGHT,getTxtGrossWeight());
        securityEachRecord.put(NET_WEIGHT,getTxtNetWeight());
        securityEachRecord.put(PURITY,getCboPurityOfGold());
        securityEachRecord.put(MARKET_RATE,getTxtMarketRate());
        securityEachRecord.put(SECURITY_VALUE,getSecurityValue());
        securityEachRecord.put(TOTAL_SECURITY_VALUE,getTotalSecurityValue());
        securityEachRecord.put(MARGIN,getTxtMargin());
        securityEachRecord.put(MARGIN_AMT, getTotalMarginAmt());
        securityEachRecord.put(ELIGIBLE_AMT,getTotalEligibleLoanAmt());
        securityEachRecord.put(TOTAL_MARGIN_AMT, getTotalMarginAmt());
        securityEachRecord.put(TOTAL_ELIGIBLE_AMT,getTotalEligibleLoanAmt());
        securityEachRecord.put(APPRAISER_ID,getTxtAppraiserId());
        securityEachRecord.put(PARTICULARS,getTxtAreaParticular());
        securityEachRecord.put(NOOFPACKET,getTxtNoOfPacket());
        securityEachRecord.put(COMMAND, "");
    }
    
    private int updateSecurityTab(int recordPosition){
        int option = -1;
        HashMap result = new HashMap();
        try{
            double secValueBeforeUpdate = CommonUtil.convertObjToDouble(((ArrayList) securityTabValues.get(recordPosition)).get(2)).doubleValue();
            result = tableUtilSecurity.updateTableValues(securityEachTabRecord, securityEachRecord, recordPosition);
            
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            
        }catch(Exception e){
            log.info("Error in updateSecurityTab()..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
    
    public void populateSecurityDetails(int recordPosition){
        try{
            log.info(securityAll);
            HashMap eachRecs;
            java.util.Set keySet =  securityAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = (String) ((ArrayList) (tblSecurityTab.getDataArrayList().get(recordPosition))).get(0);
            // To populate the corresponding record from the Security Details
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if (((String) ((HashMap) securityAll.get(objKeySet[j])).get(SLNO)).equals(strRecordKey)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) securityAll.get(objKeySet[j]);
                    setSlNo(CommonUtil.convertObjToStr(eachRecs.get(SLNO)));
                    setTdtAson(CommonUtil.convertObjToStr(eachRecs.get(TDTASONDATE)));
                    setTxtGrossWeight(CommonUtil.convertObjToStr(eachRecs.get(GROSS_WEIGHT)));
                    setTxtNetWeight(CommonUtil.convertObjToStr(eachRecs.get(NET_WEIGHT)));
                    setCboPurityOfGold(CommonUtil.convertObjToStr(eachRecs.get(PURITY)));
                    setTxtMarketRate(CommonUtil.convertObjToStr(eachRecs.get(MARKET_RATE)));
                    setSecurityValue(CommonUtil.convertObjToStr(eachRecs.get(SECURITY_VALUE)));
                    setTotalSecurityValue(CommonUtil.convertObjToStr(eachRecs.get(TOTAL_SECURITY_VALUE)));
                    setTxtMargin(CommonUtil.convertObjToStr(eachRecs.get(MARGIN)));
                    setTxtMarginAmt(CommonUtil.convertObjToStr(eachRecs.get(MARGIN_AMT)));
                    setTxtEligibleLoanAmt(CommonUtil.convertObjToStr(eachRecs.get(ELIGIBLE_AMT)));
                    setTotalMarginAmt(CommonUtil.convertObjToStr(eachRecs.get(TOTAL_MARGIN_AMT)));
                    setTotalEligibleLoanAmt(CommonUtil.convertObjToStr(eachRecs.get(TOTAL_ELIGIBLE_AMT)));
                    setTxtNoOfPacket(CommonUtil.convertObjToStr(eachRecs.get(NOOFPACKET)));
                    if(eachRecs.get(APPRAISER_ID)!=null){
                    setTxtAppraiserId(CommonUtil.convertObjToStr(eachRecs.get(APPRAISER_ID)));
                        HashMap appraiserMap = new HashMap();
                        appraiserMap.put("EMPLOYEE_CODE",eachRecs.get(APPRAISER_ID));
                        List lst = ClientUtil.executeQuery("getSelectAppraiserId", appraiserMap);
                        if(lst!=null && lst.size()>0){
                            appraiserMap = (HashMap)lst.get(0);
                            setLblAppraiserNameValue(CommonUtil.convertObjToStr(appraiserMap.get("EMPLOYEE_NAME")));
                        }
                    }
                   setTxtAreaParticular(CommonUtil.convertObjToStr(eachRecs.get(PARTICULARS)));
                    break;
                }
                eachRecs = null;
            }
            keySet = null;
            
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error in populateSecurityDetails()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteSecurityTabRecord(int recordPosition){
        HashMap result = new HashMap();
        try{
            // To remove the security No. from the combo box in Insurance Details
            String securityNo = (String) ((ArrayList) securityTabValues.get(recordPosition)).get(0);
            
            result = tableUtilSecurity.deleteTableValues(recordPosition);
            
            securityTabValues = (ArrayList) result.get(TABLE_VALUES);
            securityAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            tblSecurityTab.setDataArrayList(securityTabValues, securityTabTitle);
            // Remove the Security number in Insurance Detail's comboboxmodel
            
        }catch(Exception e){
            log.info("Error in deleteSecurityTabRecord()..."+e);
            parseException.logException(e,true);
        }
        result = null;
    }
    
    private String getCustomerName(String strCustID){
        String strCustName = "";
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("CUST_ID", strCustID);
            List resultList = ClientUtil.executeQuery("getSelectSecurityCustName", transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                strCustName = CommonUtil.convertObjToStr(retrieve.get(CUSTOMER_NAME));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in getCustomerName: "+e);
            parseException.logException(e,true);
        }
        return strCustName;
    }
    
    public void populateCustDetails(HashMap map){
        try{
//            setTxtCustID_Security(CommonUtil.convertObjToStr(map.get(CUSTOMER_ID)));
            setLblCustName_Security_Display(CommonUtil.convertObjToStr(map.get(CUSTOMER_NAME)));
        }catch(Exception e){
            log.info("Exception caught in populateCustDetails: "+e);
            parseException.logException(e,true);
        }
    }
    
    public String populateEligibleLoanAgainstSecurity(String strSecAmt, String strMargin, String strLoanAmt){
        String strEligibleLoan = "";
        try{
            double realSecurityValue = CommonUtil.convertObjToDouble(strSecAmt).doubleValue();
            double margin = CommonUtil.convertObjToDouble(strMargin).doubleValue();
            double marginAmt = calculateMarginValue(realSecurityValue, margin);
            strEligibleLoan = String.valueOf(marginAmt);
        }catch(Exception e){
            log.info("Exception caught in populateEligibleLoanAgainstSecurity: "+e);
            parseException.logException(e,true);
        }
        return strEligibleLoan;
    }
    
    private double calculateMarginValue(double realSecurityValue, double margin){
        double securityAmt = 0.0;
        try{
             securityAmt = (realSecurityValue * ((100.0 - margin) / 100.0));
        }catch(Exception e){
            log.info("Exception caught in calculateMarginValue: "+e);
            parseException.logException(e,true);
        }
        return securityAmt;
    }
    
    public void populateSecurityID_Value(HashMap hash, String strLoanAmt){
        try{
//            setTxtSecurityNo(CommonUtil.convertObjToStr(hash.get(SECURITY_NO)));
            double realSecurityValue = CommonUtil.convertObjToDouble(hash.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            double loanAmt = CommonUtil.convertObjToDouble(strLoanAmt).doubleValue();
            if (loanAmt > realSecurityValue){
                int option = -1;
                String[] options = {objTermLoanRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("securityValueMsg"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            }
            setTxtSecurityValue(CommonUtil.convertObjToStr(hash.get("AVAILABLE_SECURITY_VALUE")));
            setTxtEligibleLoan("");
        }catch(Exception e){
            log.info("Exception caught in populateSecurityID_Value: "+e);
            parseException.logException(e,true);
        }
    }
    
    public boolean chkForSecValLessThanLimiVal(String strLimitAmt){
        return chkForSecValLessThanLimiVal(strLimitAmt, null,false);
    }
    
    public boolean chkForSecValLessThanLimiVal(String strLimitAmt, TableModel tblSecTab,boolean isPartially){
        try{
            double limitAmt = CommonUtil.convertObjToDouble(strLimitAmt).doubleValue();
            double secAmt = 0.0;
            if (tblSecTab == null){
                ArrayList secValues = tableUtilSecurity.getTableValues();
                ArrayList oneRec;
                for (int i = secValues.size() - 1, j = 0;i >= 0;--i,++j){
                    oneRec = (ArrayList) secValues.get(j);
                    secAmt += CommonUtil.convertObjToDouble(oneRec.get(3)).doubleValue();
                    oneRec = null;
                }
                secValues = null;
            }else{
                int noOfRecords = tblSecTab.getRowCount();
                for (int i = noOfRecords - 1, j = 0;i >= 0;--i,++j){
                    secAmt += CommonUtil.convertObjToDouble(tblSecTab.getValueAt(j, 3)).doubleValue();
                }
            }
            if(isPartially && secAmt>0.0)
                return false;
                
            if (secAmt < limitAmt){
                return true;
            }
        }catch(Exception e){
            log.info("Exception caught in chkForSecurityVal: "+e);
            parseException.logException(e,true);
        }
        return false;
    }
    
    // To create objects
    public void createObject(){
        securityTabValues = new ArrayList();
        securityAll = new LinkedHashMap();
        tableUtilSecurity = new TableUtil();
        tableUtilSecurity.setAttributeKey(CommonUtil.convertObjToStr(SLNO));
        oldEligibleLoanAmtMap = new HashMap();
        tblSecurityTab = new EnhancedTableModel(null, getSecurityTabTitle());
    }
    
    // To destroy Objects
    public void destroyObjects(){
        securityTabValues = null;
        securityAll = null;
        tableUtilSecurity = null;
        oldEligibleLoanAmtMap = null;
        tblSecurityTab = null;
    }
    
    /**
     * Getter for property txtCustID_Security.
     * @return Value of property txtCustID_Security.
     */
    public java.lang.String getTxtCustID_Security() {
        return txtCustID_Security;
    }
    
    /**
     * Setter for property txtCustID_Security.
     * @param txtCustID_Security New value of property txtCustID_Security.
     */
    public void setTxtCustID_Security(java.lang.String txtCustID_Security) {
        this.txtCustID_Security = txtCustID_Security;
    }
    
    /**
     * Getter for property lblCustName_Security_Display.
     * @return Value of property lblCustName_Security_Display.
     */
    public java.lang.String getLblCustName_Security_Display() {
        return lblCustName_Security_Display;
    }
    
    /**
     * Setter for property lblCustName_Security_Display.
     * @param lblCustName_Security_Display New value of property lblCustName_Security_Display.
     */
    public void setLblCustName_Security_Display(java.lang.String lblCustName_Security_Display) {
        this.lblCustName_Security_Display = lblCustName_Security_Display;
    }
    
    /**
     * Getter for property strACNumber.
     * @return Value of property strACNumber.
     */
    public java.lang.String getStrACNumber() {
        return strACNumber;
    }
    
    /**
     * Setter for property strACNumber.
     * @param strACNumber New value of property strACNumber.
     */
    public void setStrACNumber(java.lang.String strACNumber) {
        this.strACNumber = strACNumber;
    }
    
    /**
     * Getter for property txtMargin.
     * @return Value of property txtMargin.
     */
    public java.lang.String getTxtMargin() {
        return txtMargin;
    }
    
    /**
     * Setter for property txtMargin.
     * @param txtMargin New value of property txtMargin.
     */
    public void setTxtMargin(java.lang.String txtMargin) {
        this.txtMargin = txtMargin;
    }
    
    /**
     * Getter for property txtEligibleLoan.
     * @return Value of property txtEligibleLoan.
     */
    public java.lang.String getTxtEligibleLoan() {
        return txtEligibleLoan;
    }
    
    /**
     * Setter for property txtEligibleLoan.
     * @param txtEligibleLoan New value of property txtEligibleLoan.
     */
    public void setTxtEligibleLoan(java.lang.String txtEligibleLoan) {
        this.txtEligibleLoan = txtEligibleLoan;
    }
    
    /**
     * Getter for property tdtAson.
     * @return Value of property tdtAson.
     */
    public java.lang.String getTdtAson() {
        return tdtAson;
    }
    
    /**
     * Setter for property tdtAson.
     * @param tdtAson New value of property tdtAson.
     */
    public void setTdtAson(java.lang.String tdtAson) {
        this.tdtAson = tdtAson;
    }
    
    /**
     * Getter for property txtGrossWeight.
     * @return Value of property txtGrossWeight.
     */
    public java.lang.String getTxtGrossWeight() {
        return txtGrossWeight;
    }
    
    /**
     * Setter for property txtGrossWeight.
     * @param txtGrossWeight New value of property txtGrossWeight.
     */
    public void setTxtGrossWeight(java.lang.String txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
    }
    
    /**
     * Getter for property txtNetWeight.
     * @return Value of property txtNetWeight.
     */
    public java.lang.String getTxtNetWeight() {
        return txtNetWeight;
    }
    
    /**
     * Setter for property txtNetWeight.
     * @param txtNetWeight New value of property txtNetWeight.
     */
    public void setTxtNetWeight(java.lang.String txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
    }
    
    /**
     * Getter for property txtMarketRate.
     * @return Value of property txtMarketRate.
     */
    public java.lang.String getTxtMarketRate() {
        return txtMarketRate;
    }
    
    /**
     * Setter for property txtMarketRate.
     * @param txtMarketRate New value of property txtMarketRate.
     */
    public void setTxtMarketRate(java.lang.String txtMarketRate) {
        this.txtMarketRate = txtMarketRate;
    }
    
    /**
     * Getter for property txtAppraiserId.
     * @return Value of property txtAppraiserId.
     */
    public java.lang.String getTxtAppraiserId() {
        return txtAppraiserId;
    }
    
    /**
     * Setter for property txtAppraiserId.
     * @param txtAppraiserId New value of property txtAppraiserId.
     */
    public void setTxtAppraiserId(java.lang.String txtAppraiserId) {
        this.txtAppraiserId = txtAppraiserId;
    }
     public java.lang.String getTxtRenewalAppraiserId() {
        return txtRenewalAppraiserId;
    }
    
    public void setTxtRenewalAppraiserId(java.lang.String txtAppraiserId) {
        this.txtRenewalAppraiserId = txtAppraiserId;
    }
    /**
     * Getter for property txtAreaParticular.
     * @return Value of property txtAreaParticular.
     */
    public java.lang.String getTxtAreaParticular() {
        return txtAreaParticular;
    }
    
    /**
     * Setter for property txtAreaParticular.
     * @param txtAreaParticular New value of property txtAreaParticular.
     */
    public void setTxtAreaParticular(java.lang.String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }
    
    /**
     * Getter for property slNo.
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }
    
    /**
     * Setter for property slNo.
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }
    
    /**
     * Getter for property cbmPurityOfGold.
     * @return Value of property cbmPurityOfGold.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPurityOfGold() {
        return cbmPurityOfGold;
    }
    
    /**
     * Setter for property cbmPurityOfGold.
     * @param cbmPurityOfGold New value of property cbmPurityOfGold.
     */
    public void setCbmPurityOfGold(com.see.truetransact.clientutil.ComboBoxModel cbmPurityOfGold) {
        this.cbmPurityOfGold = cbmPurityOfGold;
    }
    
    /**
     * Getter for property cboPurityOfGold.
     * @return Value of property cboPurityOfGold.
     */
    public java.lang.String getCboPurityOfGold() {
        return cboPurityOfGold;
    }
    
    /**
     * Setter for property cboPurityOfGold.
     * @param cboPurityOfGold New value of property cboPurityOfGold.
     */
    public void setCboPurityOfGold(java.lang.String cboPurityOfGold) {
        this.cboPurityOfGold = cboPurityOfGold;
    }
    
    /**
     * Getter for property txtMarginAmt.
     * @return Value of property txtMarginAmt.
     */
    public java.lang.String getTxtMarginAmt() {
        return txtMarginAmt;
    }
    
    /**
     * Setter for property txtMarginAmt.
     * @param txtMarginAmt New value of property txtMarginAmt.
     */
    public void setTxtMarginAmt(java.lang.String txtMarginAmt) {
        this.txtMarginAmt = txtMarginAmt;
    }
    
    /**
     * Getter for property securityValue.
     * @return Value of property securityValue.
     */
    public java.lang.String getSecurityValue() {
        return securityValue;
    }
    
    /**
     * Setter for property securityValue.
     * @param securityValue New value of property securityValue.
     */
    public void setSecurityValue(java.lang.String securityValue) {
        this.securityValue = securityValue;
    }
    
    /**
     * Getter for property totalSecurityValue.
     * @return Value of property totalSecurityValue.
     */
    public java.lang.String getTotalSecurityValue() {
        return totalSecurityValue;
    }
    
    /**
     * Setter for property totalSecurityValue.
     * @param totalSecurityValue New value of property totalSecurityValue.
     */
    public void setTotalSecurityValue(java.lang.String totalSecurityValue) {
        this.totalSecurityValue = totalSecurityValue;
    }
    
    /**
     * Getter for property txtEligibleLoanAmt.
     * @return Value of property txtEligibleLoanAmt.
     */
    public java.lang.String getTxtEligibleLoanAmt() {
        return txtEligibleLoanAmt;
    }
    
    /**
     * Setter for property txtEligibleLoanAmt.
     * @param txtEligibleLoanAmt New value of property txtEligibleLoanAmt.
     */
    public void setTxtEligibleLoanAmt(java.lang.String txtEligibleLoanAmt) {
        this.txtEligibleLoanAmt = txtEligibleLoanAmt;
    }
    
    /**
     * Getter for property finalSecurityValue.
     * @return Value of property finalSecurityValue.
     */
    public java.lang.String getFinalSecurityValue() {
        return finalSecurityValue;
    }
    
    /**
     * Setter for property finalSecurityValue.
     * @param finalSecurityValue New value of property finalSecurityValue.
     */
    public void setFinalSecurityValue(java.lang.String finalSecurityValue) {
        this.finalSecurityValue = finalSecurityValue;
    }
    
    /**
     * Getter for property finalMarginAmt.
     * @return Value of property finalMarginAmt.
     */
    public java.lang.String getFinalMarginAmt() {
        return finalMarginAmt;
    }
    
    /**
     * Setter for property finalMarginAmt.
     * @param finalMarginAmt New value of property finalMarginAmt.
     */
    public void setFinalMarginAmt(java.lang.String finalMarginAmt) {
        this.finalMarginAmt = finalMarginAmt;
    }
    
    /**
     * Getter for property finalEligibleLoanAmt.
     * @return Value of property finalEligibleLoanAmt.
     */
    public java.lang.String getFinalEligibleLoanAmt() {
        return finalEligibleLoanAmt;
    }
    
    /**
     * Setter for property finalEligibleLoanAmt.
     * @param finalEligibleLoanAmt New value of property finalEligibleLoanAmt.
     */
    public void setFinalEligibleLoanAmt(java.lang.String finalEligibleLoanAmt) {
        this.finalEligibleLoanAmt = finalEligibleLoanAmt;
    }
    
    /**
     * Getter for property lblAppraiserNameValue.
     * @return Value of property lblAppraiserNameValue.
     */
    public java.lang.String getLblAppraiserNameValue() {
        return lblAppraiserNameValue;
    }
    
    /**
     * Setter for property lblAppraiserNameValue.
     * @param lblAppraiserNameValue New value of property lblAppraiserNameValue.
     */
    public void setLblAppraiserNameValue(java.lang.String lblAppraiserNameValue) {
        this.lblAppraiserNameValue = lblAppraiserNameValue;
    }
    
    /**
     * Getter for property totalMarginAmt.
     * @return Value of property totalMarginAmt.
     */
    public java.lang.String getTotalMarginAmt() {
        return totalMarginAmt;
    }
    
    /**
     * Setter for property totalMarginAmt.
     * @param totalMarginAmt New value of property totalMarginAmt.
     */
    public void setTotalMarginAmt(java.lang.String totalMarginAmt) {
        this.totalMarginAmt = totalMarginAmt;
    }
    
    /**
     * Getter for property totalEligibleLoanAmt.
     * @return Value of property totalEligibleLoanAmt.
     */
    public java.lang.String getTotalEligibleLoanAmt() {
        return totalEligibleLoanAmt;
    }
    
    /**
     * Setter for property totalEligibleLoanAmt.
     * @param totalEligibleLoanAmt New value of property totalEligibleLoanAmt.
     */
    public void setTotalEligibleLoanAmt(java.lang.String totalEligibleLoanAmt) {
        this.totalEligibleLoanAmt = totalEligibleLoanAmt;
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
     * Getter for property cbmRenewalPurityOfGold.
     * @return Value of property cbmRenewalPurityOfGold.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalPurityOfGold() {
        return cbmRenewalPurityOfGold;
    }
    
    /**
     * Setter for property cbmRenewalPurityOfGold.
     * @param cbmRenewalPurityOfGold New value of property cbmRenewalPurityOfGold.
     */
    public void setCbmRenewalPurityOfGold(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalPurityOfGold) {
        this.cbmRenewalPurityOfGold = cbmRenewalPurityOfGold;
    }
    
    /**
     * Getter for property txtRenewalGrossWeight.
     * @return Value of property txtRenewalGrossWeight.
     */
    public java.lang.String getTxtRenewalGrossWeight() {
        return txtRenewalGrossWeight;
    }
    
    /**
     * Setter for property txtRenewalGrossWeight.
     * @param txtRenewalGrossWeight New value of property txtRenewalGrossWeight.
     */
    public void setTxtRenewalGrossWeight(java.lang.String txtRenewalGrossWeight) {
        this.txtRenewalGrossWeight = txtRenewalGrossWeight;
    }
    
    /**
     * Getter for property txtRenewalNetWeight.
     * @return Value of property txtRenewalNetWeight.
     */
    public java.lang.String getTxtRenewalNetWeight() {
        return txtRenewalNetWeight;
    }
    
    /**
     * Setter for property txtRenewalNetWeight.
     * @param txtRenewalNetWeight New value of property txtRenewalNetWeight.
     */
    public void setTxtRenewalNetWeight(java.lang.String txtRenewalNetWeight) {
        this.txtRenewalNetWeight = txtRenewalNetWeight;
    }
    
    /**
     * Getter for property cboRenewalPurityOfGold.
     * @return Value of property cboRenewalPurityOfGold.
     */
    public java.lang.String getCboRenewalPurityOfGold() {
        return cboRenewalPurityOfGold;
    }
    
    /**
     * Setter for property cboRenewalPurityOfGold.
     * @param cboRenewalPurityOfGold New value of property cboRenewalPurityOfGold.
     */
    public void setCboRenewalPurityOfGold(java.lang.String cboRenewalPurityOfGold) {
        this.cboRenewalPurityOfGold = cboRenewalPurityOfGold;
    }
    
    /**
     * Getter for property txtRenewalMarketRate.
     * @return Value of property txtRenewalMarketRate.
     */
    public java.lang.String getTxtRenewalMarketRate() {
        return txtRenewalMarketRate;
    }
    
    /**
     * Setter for property txtRenewalMarketRate.
     * @param txtRenewalMarketRate New value of property txtRenewalMarketRate.
     */
    public void setTxtRenewalMarketRate(java.lang.String txtRenewalMarketRate) {
        this.txtRenewalMarketRate = txtRenewalMarketRate;
    }
    
    /**
     * Getter for property txtRenewalSecurityValue.
     * @return Value of property txtRenewalSecurityValue.
     */
    public java.lang.String getTxtRenewalSecurityValue() {
        return txtRenewalSecurityValue;
    }
    
    /**
     * Setter for property txtRenewalSecurityValue.
     * @param txtRenewalSecurityValue New value of property txtRenewalSecurityValue.
     */
    public void setTxtRenewalSecurityValue(java.lang.String txtRenewalSecurityValue) {
        this.txtRenewalSecurityValue = txtRenewalSecurityValue;
    }
    
    /**
     * Getter for property txtRenewalAreaParticular.
     * @return Value of property txtRenewalAreaParticular.
     */
    public java.lang.String getTxtRenewalAreaParticular() {
        return txtRenewalAreaParticular;
    }
    
    /**
     * Setter for property txtRenewalAreaParticular.
     * @param txtRenewalAreaParticular New value of property txtRenewalAreaParticular.
     */
    public void setTxtRenewalAreaParticular(java.lang.String txtRenewalAreaParticular) {
        this.txtRenewalAreaParticular = txtRenewalAreaParticular;
    }
    
    /**
     * Getter for property txtRenewalMargin.
     * @return Value of property txtRenewalMargin.
     */
    public java.lang.String getTxtRenewalMargin() {
        return txtRenewalMargin;
    }
    
    /**
     * Setter for property txtRenewalMargin.
     * @param txtRenewalMargin New value of property txtRenewalMargin.
     */
    public void setTxtRenewalMargin(java.lang.String txtRenewalMargin) {
        this.txtRenewalMargin = txtRenewalMargin;
    }
    
    /**
     * Getter for property txtRenewalEligibleLoan.
     * @return Value of property txtRenewalEligibleLoan.
     */
    public java.lang.String getTxtRenewalEligibleLoan() {
        return txtRenewalEligibleLoan;
    }
    
    /**
     * Setter for property txtRenewalEligibleLoan.
     * @param txtRenewalEligibleLoan New value of property txtRenewalEligibleLoan.
     */
    public void setTxtRenewalEligibleLoan(java.lang.String txtRenewalEligibleLoan) {
        this.txtRenewalEligibleLoan = txtRenewalEligibleLoan;
    }
    
    /**
     * Getter for property cboRenewalAppraiserId.
     * @return Value of property cboRenewalAppraiserId.
     */
    public java.lang.String getCboRenewalAppraiserId() {
        return cboRenewalAppraiserId;
    }
    
    /**
     * Setter for property cboRenewalAppraiserId.
     * @param cboRenewalAppraiserId New value of property cboRenewalAppraiserId.
     */
    public void setCboRenewalAppraiserId(java.lang.String cboRenewalAppraiserId) {
        this.cboRenewalAppraiserId = cboRenewalAppraiserId;
    }
    
    /**
     * Getter for property cbmRenewalAppraiserId.
     * @return Value of property cbmRenewalAppraiserId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalAppraiserId() {
        return cbmRenewalAppraiserId;
    }
    
    /**
     * Setter for property cbmRenewalAppraiserId.
     * @param cbmRenewalAppraiserId New value of property cbmRenewalAppraiserId.
     */
    public void setCbmRenewalAppraiserId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalAppraiserId) {
        this.cbmRenewalAppraiserId = cbmRenewalAppraiserId;
    }
    
    /**
     * Getter for property txtRenewalMarginAmount.
     * @return Value of property txtRenewalMarginAmount.
     */
    public java.lang.String getTxtRenewalMarginAmount() {
        return txtRenewalMarginAmount;
    }
    
    /**
     * Setter for property txtRenewalMarginAmount.
     * @param txtRenewalMarginAmount New value of property txtRenewalMarginAmount.
     */
    public void setTxtRenewalMarginAmount(java.lang.String txtRenewalMarginAmount) {
        this.txtRenewalMarginAmount = txtRenewalMarginAmount;
    }
    
    /**
     * Getter for property cbmItem.
     * @return Value of property cbmItem.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmItem() {
        return cbmItem;
    }
    
    /**
     * Setter for property cbmItem.
     * @param cbmItem New value of property cbmItem.
     */
    public void setCbmItem(com.see.truetransact.clientutil.ComboBoxModel cbmItem) {
        this.cbmItem = cbmItem;
    }
    
    /**
     * Getter for property txtNoOfPacket.
     * @return Value of property txtNoOfPacket.
     */
    public java.lang.String getTxtNoOfPacket() {
        return txtNoOfPacket;
    }
    
    /**
     * Setter for property txtNoOfPacket.
     * @param txtNoOfPacket New value of property txtNoOfPacket.
     */
    public void setTxtNoOfPacket(java.lang.String txtNoOfPacket) {
        this.txtNoOfPacket = txtNoOfPacket;
    }
    
}
