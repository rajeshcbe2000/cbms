/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanAdditionalSanctionOB.java
 *
 * Created on February 17, 2009, 12:04 PM
 */

package com.see.truetransact.ui.termloan;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.termloan.TermLoanAdditionalSanctionTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.transferobject.termloan.TermLoanDocumentTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.interestcalc.Rounding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;




/**
 *
 * @author  Administrator
 */
public class TermLoanAdditionalSanctionOB extends  CObservable {
    
    /** Creates a new instance of TermLoanAdditionalSanctionOB */
    public TermLoanAdditionalSanctionOB() {
        curDate = ClientUtil.getCurrentDate();
        termLoanAdditionalSanctionOB();
    }
    
    
    
    private       static TermLoanAdditionalSanctionOB termLoanAdditionalSanctionOB;
    
    private final static Logger log = Logger.getLogger(TermLoanAdditionalSanctionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private final   String  ACCT_NO = "ACCT_NO";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  BORROW_NO = "BORROW_NO";
    private final   String  COMMAND = "COMMAND";
    private final   String  DOC_DESC = "DOC_DESC";
    private final   String  DOC_EXEC_DT = "DOC_EXEC_DT";
    private final   String  DOC_EXP_DT = "DOC_EXP_DT";
    private final   String  DOC_FORM_NO = "DOC_FORM_NO";
    private final   String  DOC_TYPE = "DOC_TYPE";
    private final   String  INSERT = "INSERT";
    private final   String  IS_EXECUTED = "IS_EXECUTED";
    private final   String  IS_MANDATORY = "IS_MANDATORY";
    private final   String  IS_SUBMITTED = "IS_SUBMITTED";
    private final   String  NO = "N";
    private final   String  OPTION = "OPTION";
    private final   String  PRODUCT_ID = "PRODUCT_ID";
    private final   String  REMARKS = "REMARKS";
    private final   String  SUBMITTED_DT = "SUBMITTED_DT";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  UPDATE = "UPDATE";
    private final   String  YES = "Y";
    
    private final   ArrayList additionalSanctionTabTitle = new ArrayList();       //  Table Title of document
    private ArrayList addSanctionEachTabRecord;
    private ArrayList additionalSanctionTabValues = new ArrayList();             // ArrayList to display in Guarantor Table
    private TableUtil  additionalSanUtil=new TableUtil();
    private EnhancedTableModel tblPeakSanctionTab;
    
    private ArrayList removedValues=new ArrayList();
    private LinkedHashMap additionalSanctionAll = new LinkedHashMap();           // Both displayed and hidden values in the table
    private HashMap eachRecordMap;
    private HashMap existStatusMap;
    
    private String additionalSanctionDt=null;
    private String permittedBy=null;
    private String permittedName=null;
    private String designatedName=null;
    private String additionalLimit=null;
    private String strACNumber = "";
    private static int tblRecordCount=0;
    
    
    
    Date curDate = null;
    
    static {
        try {
            termLoanAdditionalSanctionOB = new TermLoanAdditionalSanctionOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * To create the singleton object TermLoanDocumentDetailsOB
     * @return the instance of TermLoanDocumentDetailsOB
     */
    public static TermLoanAdditionalSanctionOB getInstance() {
        return termLoanAdditionalSanctionOB;
    }
    
    private void termLoanAdditionalSanctionOB(){
        setadditionalSanctionTabTitle();
        additionalSanUtil.setAttributeKey("SLNO");
        tblPeakSanctionTab = new EnhancedTableModel(null, additionalSanctionTabTitle);
        
    }
    
    private void setadditionalSanctionTabTitle(){
        try{
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakSanSlno"));
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakSanFromDate"));
            //            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakToDate"));
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPeakAmt"));
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnPermitted By"));
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnStatus"));
            additionalSanctionTabTitle.add(objTermLoanRB.getString("tblColumnAuthStatus"));
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public ArrayList getadditionalSanctionTabTitle(){
        return this.additionalSanctionTabTitle;
    }
    
    public void setTermLoanAdditionalSanctionTO(ArrayList additionalSanctionList,String getStrAcNumber){
        try{
            TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
            HashMap documentRecordMap;
            
            
            LinkedHashMap allSanctionMap = new LinkedHashMap();
            ArrayList   allSanctionList = new ArrayList();
            ArrayList documentRecordList;
            ArrayList tabDocumentRecords = new ArrayList();
            // To retrieve the Guarantor Details from the Serverside
            for (int i = additionalSanctionList.size() - 1,j = 0;i >= 0;--i,++j){
                objTermLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanctionList.get(j);
                documentRecordMap = new HashMap();
                documentRecordList = new ArrayList();
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getSlno()));
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAdditionalSanctionDt()));
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAdditionalLimit()));
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getPermittedBy()));
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getStatus()));
                documentRecordList.add(CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAuthorizeStatus()));
                
                allSanctionList.add(documentRecordList);
                
                
                documentRecordMap.put("ACCT NO", getStrAcNumber);
                documentRecordMap.put("SLNO", CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getSlno()));
                documentRecordMap.put("SANCTION DT", CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAdditionalSanctionDt()));
                documentRecordMap.put("ADDITIONAL LIMIT",CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAdditionalLimit()));
                documentRecordMap.put("PERMITTED BY", CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getPermittedBy()));
                documentRecordMap.put("AUTHORIZE STATUS", CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAuthorizeStatus()));
//                documentRecordMap.put("STATUS", CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getStatus()));
                documentRecordMap.put("COMMAND", "UPDATE");
                allSanctionMap.put(objTermLoanAdditionalSanctionTO.getSlno(), documentRecordMap);
                
            }
            additionalSanctionAll.clear();
            additionalSanctionTabValues.clear();
            additionalSanctionAll = allSanctionMap;
            additionalSanctionTabValues = allSanctionList;
            additionalSanUtil.setAllValues(allSanctionMap);
            additionalSanUtil.setTableValues(allSanctionList);
            tblPeakSanctionTab.setDataArrayList(additionalSanctionTabValues, getadditionalSanctionTabTitle());
            if(allSanctionList!=null && allSanctionList.size()>0)
                 tblRecordCount=allSanctionList.size();
           
            allSanctionMap = null;
            allSanctionList = null;
        }catch(Exception e){
            log.info("Exception in setDocumentTabTitle(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public HashMap setAdditionalSanction(){
        HashMap addSanctionMap = new HashMap();
        try{
            ArrayList removedValues = new ArrayList();
            removedValues=additionalSanUtil.getRemovedValues();
            TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
            java.util.Set keySet =  additionalSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            log.info(additionalSanctionAll);
            log.info(additionalSanctionAll.keySet());
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) additionalSanctionAll.get(objKeySet[j]);
                
                if (CommonUtil.convertObjToStr(oneRecord.get(COMMAND)).length() != 0){
                    log.info("Command"+oneRecord.get(COMMAND));
                    objTermLoanAdditionalSanctionTO = new TermLoanAdditionalSanctionTO();
                    objTermLoanAdditionalSanctionTO.setAcctNum(getStrACNumber());
                    objTermLoanAdditionalSanctionTO.setAdditionalLimit(CommonUtil.convertObjToDouble(oneRecord.get("ADDITIONAL LIMIT")));
                    objTermLoanAdditionalSanctionTO.setAdditionalSanctionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oneRecord.get("SANCTION DT"))));
                    objTermLoanAdditionalSanctionTO.setSlno(CommonUtil.convertObjToStr(oneRecord.get("SLNO")));
                    objTermLoanAdditionalSanctionTO.setPermittedBy(CommonUtil.convertObjToStr(oneRecord.get("PERMITTED BY")));
                    objTermLoanAdditionalSanctionTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oneRecord.get("AUTHORIZE STATUS")));
                    objTermLoanAdditionalSanctionTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(COMMAND)));
                    if (CommonUtil.convertObjToStr(oneRecord.get(COMMAND)).equals("INSERT"))
                        objTermLoanAdditionalSanctionTO.setStatus(CommonConstants.STATUS_CREATED);
                    else
                        objTermLoanAdditionalSanctionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    
                    addSanctionMap.put(objTermLoanAdditionalSanctionTO.getSlno(), objTermLoanAdditionalSanctionTO);
                }
                oneRecord = null;
                objTermLoanAdditionalSanctionTO = null;
            }
            //delete record also we shoude update
            HashMap  deletedMap=null;
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    deletedMap=(HashMap)removedValues.get(i);
                    objTermLoanAdditionalSanctionTO=new TermLoanAdditionalSanctionTO();
                    objTermLoanAdditionalSanctionTO.setAcctNum(getStrACNumber());
                    objTermLoanAdditionalSanctionTO.setAdditionalLimit(CommonUtil.convertObjToDouble(deletedMap.get("ADDITIONAL LIMIT")));
                    objTermLoanAdditionalSanctionTO.setAdditionalSanctionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("SANCTION DT"))));
                    objTermLoanAdditionalSanctionTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    objTermLoanAdditionalSanctionTO.setCommand(CommonUtil.convertObjToStr(deletedMap.get(COMMAND)));
                    objTermLoanAdditionalSanctionTO.setPermittedBy(CommonUtil.convertObjToStr(deletedMap.get("PERMITTED BY")));
                    objTermLoanAdditionalSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                    objTermLoanAdditionalSanctionTO.setAuthorizeStatus(CommonUtil.convertObjToStr(deletedMap.get("AUTHORIZE STATUS")));
                    
                    addSanctionMap.put(objTermLoanAdditionalSanctionTO.getSlno(),objTermLoanAdditionalSanctionTO);
                    
                }
            log.info(addSanctionMap.keySet());
            
        }catch(Exception e){
            log.info("Exception in setTermLoanDocument(): "+e);
            parseException.logException(e,true);
        }
        return addSanctionMap;
    }
    
    public void changeStatusDocument(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                //                tableUtilDocument.getRemovedValues().clear();
            }
            java.util.Set keySet =  additionalSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // Document Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) additionalSanctionAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    additionalSanctionAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception in changeStatusDocument(): "+e);
            parseException.logException(e,true);
        }
    }
    
    public void deleteAdditionalSanctionDetails(int recordPosition){
        try{
            HashMap resultMap=(HashMap)additionalSanUtil.deleteTableValues(recordPosition);
            additionalSanctionAll=(LinkedHashMap)resultMap.get("ALL_VALUES");
            additionalSanctionTabValues=(ArrayList)resultMap.get("TABLE_VALUES");
            int value=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            
            tblPeakSanctionTab.setDataArrayList(additionalSanctionTabValues, additionalSanctionTabTitle);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private String getDocumentKey(String docType, String docNo){
        String strFacilityKey = "";
        StringBuffer strbufKey = new StringBuffer();
        try{
            strbufKey.append(docType);
            strbufKey.append("#");
            strbufKey.append(docNo);
            strFacilityKey = new String(strbufKey);
        }catch(Exception e){
            log.info("Exception caught in getDocumentKey: "+e);
            parseException.logException(e,true);
        }
        strbufKey = null;
        return strFacilityKey;
    }
    
    public boolean isDocumentCompleted(){
        boolean isCompleted = false;
        try{
            java.util.Set keySet =  additionalSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            int i = keySet.size() - 1, j = 0;
            /*
             * To check mandatory records are executed
             */
            for (;i >= 0;--i,++j){
                oneRecord = null;
                oneRecord = (HashMap) additionalSanctionAll.get(objKeySet[j]);
                if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(YES) && CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).equals(YES)){
                    // If the document is mandatory then it should be executed
                    continue;
                }else if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(NO)){
                    // If the document is not mandatory then continue to next record
                    continue;
                }else if (CommonUtil.convertObjToStr(oneRecord.get(IS_MANDATORY)).equals(YES)&& (CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).equals(NO) || CommonUtil.convertObjToStr(oneRecord.get(IS_EXECUTED)).length() <= 0)){
                    break;
                }
            }
            
            if (j != 0 && j == keySet.size()){
                // Atleast one document should be there and all the mandatory documents should be executed
                isCompleted = true;
            }
            oneRecord = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception caught in isDocumentCompleted: "+e);
            parseException.logException(e,true);
        }
        return isCompleted;
    }
    
    public void resetAdditionalSanctionDetails(){
        createObject();
        
    }
    
    public void resetPeakSanctionDetails(){
        setAdditionalSanctionDt(null);
        setPermittedBy("");
        setDesignatedName("");
        setAdditionalLimit("");
    }
    
    public void resetPeakSanctionCTable(){
        tblPeakSanctionTab.setDataArrayList(null, additionalSanctionTabTitle);
    }
    public boolean marginValidation(HashMap depositMap){
        HashMap resultMap=new HashMap();
        double depositAvailableBal=0.0;
        double eligibleMargin=0;
        String roundOff="";
        long roundOffValue=0;
        double enterAmt=CommonUtil.convertObjToDouble(depositMap.get("NEW_SANCTION_AMT")).doubleValue();
//        resultMap.put("ACCOUNT_NO",depositNo);
        List lst =(List)ClientUtil.executeQuery("getIntRateforDeposit",depositMap);
        if(lst !=null && lst.size()>0){
            resultMap=(HashMap)lst.get(0);
            depositAvailableBal=CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
        }
         lst =(List)ClientUtil.executeQuery("TermLoan.getProdHead",depositMap);
        if(lst !=null && lst.size()>0){
            resultMap=(HashMap)lst.get(0);
            eligibleMargin=CommonUtil.convertObjToDouble(resultMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            eligibleMargin=eligibleMargin/100;
            roundOff=CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_ROUNDOFF"));
//            if(roundOff.length()>0){
//                if(roundOff .equals("NEAREST_TENS")){
//                    roundOffValue=10;
//            }
//                else  if(roundOff .equals("NEAREST_HUNDREDS")){
//                     roundOffValue=100;
//            }
//                else  if(roundOff .equals("NEAREST_VALUE")){
                     roundOffValue=1;
//            }
//        }
         double lienAmt=(enterAmt/eligibleMargin);
         Rounding rd=new Rounding();
         if(!roundOff .equals("NO_ROUND_OFF"))
         lienAmt= rd.getNearestHigher(lienAmt,roundOffValue); 
         System.out.println("lienAmt####"+lienAmt);
         if(lienAmt>depositAvailableBal)
         {
             ClientUtil.showMessageWindow("Deposit Available Balance is     :"+ depositAvailableBal+"\n"+
             "Lien Amount is       :"+lienAmt);
             return true;
         }
           
         ClientUtil.showMessageWindow("Lien Amount is       :"+lienAmt);
    }
         return false;
    }
         
    public int addAdditionalSanctionDetails(int recordPosition,boolean update){
        int option=-1;
        ArrayList recordList=(ArrayList)tblPeakSanctionTab.getDataArrayList();
        tblPeakSanctionTab.setDataArrayList(recordList,additionalSanctionTabTitle);
        recordList=(ArrayList)tblPeakSanctionTab.getDataArrayList();
//        int dataSize=recordList.size();
        int dataSize=tblRecordCount;
        eachRecordMap=new HashMap();
        HashMap resultMap=new HashMap();
        addSanctionEachTabRecord=new ArrayList();
        insertAdditinalSanction(dataSize+1);
        if(!update){
            addSanctionEachTabRecord.add("");
            addSanctionEachTabRecord.add("");
            resultMap= additionalSanUtil.insertTableValues(addSanctionEachTabRecord,eachRecordMap);
            
        }else{
            addSanctionEachTabRecord.add(existStatusMap.get("STATUS"));
            addSanctionEachTabRecord.add(existStatusMap.get("AUTHORIZE STATUS"));
            eachRecordMap.put(CommonConstants.STATUS,CommonUtil.convertObjToStr(existStatusMap.get("STATUS")));
            eachRecordMap.put("AUTHORIZE STATUS",existStatusMap.get("AUTHORIZE STATUS"));
            resultMap= additionalSanUtil.updateTableValues(addSanctionEachTabRecord,eachRecordMap,recordPosition);
        }
        additionalSanctionAll = (LinkedHashMap) resultMap.get(ALL_VALUES);
        additionalSanctionTabValues=(ArrayList)resultMap.get(TABLE_VALUES);
        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        tblPeakSanctionTab.setDataArrayList(additionalSanctionTabValues,additionalSanctionTabTitle);
        recordList=null;
        recordList=null;
        eachRecordMap=null;
        resultMap=null;
        addSanctionEachTabRecord=null;
        return option;
    }
    private void insertAdditinalSanction(int position){
        
        eachRecordMap=new HashMap();
        addSanctionEachTabRecord=new ArrayList();
        addSanctionEachTabRecord.add(String.valueOf(position));
        addSanctionEachTabRecord.add(getAdditionalSanctionDt());
        addSanctionEachTabRecord.add(getAdditionalLimit());
        addSanctionEachTabRecord.add(getPermittedBy());
        
        eachRecordMap.put("ACCT NO",getStrACNumber());
        eachRecordMap.put("SLNO",String.valueOf(position));
        eachRecordMap.put("SANCTION DT",getAdditionalSanctionDt());
        eachRecordMap.put("ADDITIONAL LIMIT",getAdditionalLimit());
        eachRecordMap.put("PERMITTED BY",getPermittedBy());
        eachRecordMap.put("PERMITTED NAME",getPermittedName());
        eachRecordMap.put("DESIGNATION",getDesignatedName());
        eachRecordMap.put("COMMAND","");
        
        
    }
    // To create objects
    public void createObject(){
        additionalSanctionTabValues = new ArrayList();
        tblPeakSanctionTab.setDataArrayList(null, additionalSanctionTabTitle);
        additionalSanctionAll = new LinkedHashMap();
        additionalSanUtil=new TableUtil();
        additionalSanUtil.setAttributeKey("SLNO");
    }
    public boolean checktblRecordValidation(){
        ArrayList recordList =(ArrayList)tblPeakSanctionTab.getDataArrayList();
        if(recordList !=null && recordList.size()>0){
            for(int i=0;i<recordList.size();i++){
                ArrayList singleList=(ArrayList)recordList.get(i);
                String status =CommonUtil.convertObjToStr(singleList.get(4));
                String authStatus =CommonUtil.convertObjToStr(singleList.get(5));
                if(status.equals("DELETED"))
                    continue;
                if(authStatus.equals(""))
                {
                    ClientUtil.showMessageWindow("Previous sanction Shoude be Authorized than new Sanction Possible");
                    return false;
                }
            }
        }
        return true;
    }
    public void populateDocumentDetails(int recordPosition){
        try{
            HashMap eachRecs;
            java.util.Set keySet =  additionalSanctionAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String slno = CommonUtil.convertObjToStr(((ArrayList) (tblPeakSanctionTab.getDataArrayList().get(recordPosition))).get(0));
            // To populate the corresponding record from the additional sanction details
            ArrayList recordList=(ArrayList)tblPeakSanctionTab.getDataArrayList().get(recordPosition);
            existStatusMap=new HashMap();
            existStatusMap.put("STATUS",CommonUtil.convertObjToStr(recordList.get(4)));
            existStatusMap.put("AUTHORIZE STATUS",CommonUtil.convertObjToStr(recordList.get(5)));
            existStatusMap.put("SLNO",CommonUtil.convertObjToStr(recordList.get(5)));
            
            for (int i = keySet.size() - 1, j = 0;i >= 0;--i, ++j){
                if ((CommonUtil.convertObjToStr(((HashMap) additionalSanctionAll.get(objKeySet[j])).get("SLNO"))).equals(CommonUtil.convertObjToStr(recordList.get(0)))){
                    
                    //                if (objKeySet[j].equals(slno)){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) additionalSanctionAll.get(objKeySet[j]);
                      existStatusMap.put("SLNO",CommonUtil.convertObjToStr(eachRecs.get("SLNO")));
                    setAdditionalLimit(CommonUtil.convertObjToStr(eachRecs.get("ADDITIONAL LIMIT")));
                    setAdditionalSanctionDt(CommonUtil.convertObjToStr(eachRecs.get("SANCTION DT")));
                    setPermittedBy(CommonUtil.convertObjToStr(eachRecs.get("PERMITTED BY")));
                    setPermittedName(CommonUtil.convertObjToStr(eachRecs.get("PERMITTED NAME")));
                    setDesignatedName(CommonUtil.convertObjToStr(eachRecs.get("DESIGNATION")));
                     HashMap empMap=getEmpDetails(getPermittedBy());
                     if(empMap !=null && empMap.size()>0){
                        setPermittedName(CommonUtil.convertObjToStr(empMap.get("EMP NAME")));
                        setDesignatedName(CommonUtil.convertObjToStr(empMap.get("DESIGNATION")));

                     }
                    System.out.println("eachRecs   ###"+eachRecs);
                    
                    eachRecs = null;
                }
            }
            keySet = null;
            objKeySet = null;
            setChanged();
            ttNotifyObservers();
            
        }catch(Exception e){
            log.info("Error in populateDocumentDetails(): "+e);
            parseException.logException(e,true);
        }
        
    }
    
     private HashMap getEmpDetails(String empId){
        HashMap whereMap=new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        whereMap.put("EMP_ID",empId);
        List lst=ClientUtil.executeQuery("getEmployeeName", whereMap);
        if(lst !=null && lst.size()>0){
            whereMap=(HashMap)lst.get(0);
        }else
            whereMap=new HashMap();
       return whereMap;
    }
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    
    
    
    
    /**
     * Getter for property additionalSanctionTabValues.
     * @return Value of property additionalSanctionTabValues.
     */
    public java.util.ArrayList getadditionalSanctionTabValues() {
        return additionalSanctionTabValues;
    }
    
    /**
     * Setter for property additionalSanctionTabValues.
     * @param additionalSanctionTabValues New value of property additionalSanctionTabValues.
     */
    public void setadditionalSanctionTabValues(java.util.ArrayList additionalSanctionTabValues) {
        this.additionalSanctionTabValues = additionalSanctionTabValues;
    }
    
    /**
     * Getter for property tblPeakSanctionTab.
     * @return Value of property tblPeakSanctionTab.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPeakSanctionTab() {
        return tblPeakSanctionTab;
    }
    
    /**
     * Setter for property tblPeakSanctionTab.
     * @param tblPeakSanctionTab New value of property tblPeakSanctionTab.
     */
    public void setTblPeakSanctionTab(com.see.truetransact.clientutil.EnhancedTableModel tblPeakSanctionTab) {
        this.tblPeakSanctionTab = tblPeakSanctionTab;
    }
    
    
    /**
     * Getter for property additionalSanctionAll.
     * @return Value of property additionalSanctionAll.
     */
    public java.util.LinkedHashMap getadditionalSanctionAll() {
        return additionalSanctionAll;
    }
    
    /**
     * Setter for property additionalSanctionAll.
     * @param additionalSanctionAll New value of property additionalSanctionAll.
     */
    public void setadditionalSanctionAll(java.util.LinkedHashMap additionalSanctionAll) {
        this.additionalSanctionAll = additionalSanctionAll;
    }
    
    //    /**
    //     * Getter for property additionalSanctionAll.
    //     * @return Value of property additionalSanctionAll.
    //     */
    //    public java.util.LinkedHashMap getAdditionalSanctionAll() {
    //        return additionalSanctionAll;
    //    }
    //
    //    /**
    //     * Setter for property additionalSanctionAll.
    //     * @param additionalSanctionAll New value of property additionalSanctionAll.
    //     */
    //    public void setAdditionalSanctionAll(java.util.LinkedHashMap additionalSanctionAll) {
    //        this.additionalSanctionAll = additionalSanctionAll;
    //    }
    
    /**
     * Getter for property additionalSanctionTabValues.
     * @return Value of property additionalSanctionTabValues.
     */
    public java.util.ArrayList getAdditionalSanctionTabValues() {
        return additionalSanctionTabValues;
    }
    
    /**
     * Setter for property additionalSanctionTabValues.
     * @param additionalSanctionTabValues New value of property additionalSanctionTabValues.
     */
    public void setAdditionalSanctionTabValues(java.util.ArrayList additionalSanctionTabValues) {
        this.additionalSanctionTabValues = additionalSanctionTabValues;
    }
    
    /**
     * Getter for property additionalSanctionTabTitle.
     * @return Value of property additionalSanctionTabTitle.
     */
    public java.util.ArrayList getAdditionalSanctionTabTitle() {
        return additionalSanctionTabTitle;
    }
    
    /**
     * Getter for property additionalLimit.
     * @return Value of property additionalLimit.
     */
    public java.lang.String getAdditionalLimit() {
        return additionalLimit;
    }
    
    /**
     * Setter for property additionalLimit.
     * @param additionalLimit New value of property additionalLimit.
     */
    public void setAdditionalLimit(java.lang.String additionalLimit) {
        this.additionalLimit = additionalLimit;
    }
    
    /**
     * Getter for property designatedName.
     * @return Value of property designatedName.
     */
    public java.lang.String getDesignatedName() {
        return designatedName;
    }
    
    /**
     * Setter for property designatedName.
     * @param designatedName New value of property designatedName.
     */
    public void setDesignatedName(java.lang.String designatedName) {
        this.designatedName = designatedName;
    }
    
    /**
     * Getter for property permittedName.
     * @return Value of property permittedName.
     */
    public java.lang.String getPermittedName() {
        return permittedName;
    }
    
    /**
     * Setter for property permittedName.
     * @param permittedName New value of property permittedName.
     */
    public void setPermittedName(java.lang.String permittedName) {
        this.permittedName = permittedName;
    }
    
    /**
     * Getter for property permittedBy.
     * @return Value of property permittedBy.
     */
    public java.lang.String getPermittedBy() {
        return permittedBy;
    }
    
    /**
     * Setter for property permittedBy.
     * @param permittedBy New value of property permittedBy.
     */
    public void setPermittedBy(java.lang.String permittedBy) {
        this.permittedBy = permittedBy;
    }
    
    /**
     * Getter for property additionalSanctionDt.
     * @return Value of property additionalSanctionDt.
     */
    public java.lang.String getAdditionalSanctionDt() {
        return additionalSanctionDt;
    }
    
    /**
     * Setter for property additionalSanctionDt.
     * @param additionalSanctionDt New value of property additionalSanctionDt.
     */
    public void setAdditionalSanctionDt(java.lang.String additionalSanctionDt) {
        this.additionalSanctionDt = additionalSanctionDt;
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
     * Getter for property existStatusMap.
     * @return Value of property existStatusMap.
     */
    public java.util.HashMap getExistStatusMap() {
        return existStatusMap;
    }
    
    /**
     * Setter for property existStatusMap.
     * @param existStatusMap New value of property existStatusMap.
     */
    public void setExistStatusMap(java.util.HashMap existStatusMap) {
        this.existStatusMap = existStatusMap;
    }
    
    /**
     * Getter for property additionalSanUtil.
     * @return Value of property additionalSanUtil.
     */
    public com.see.truetransact.clientutil.TableUtil getAdditionalSanUtil() {
        return additionalSanUtil;
    }
    
    /**
     * Setter for property additionalSanUtil.
     * @param additionalSanUtil New value of property additionalSanUtil.
     */
    public void setAdditionalSanUtil(com.see.truetransact.clientutil.TableUtil additionalSanUtil) {
        this.additionalSanUtil = additionalSanUtil;
    }
    
    /**
     * Setter for property additionalSanctionTabTitle.
     * @param additionalSanctionTabTitle New value of property additionalSanctionTabTitle.
     */
    //    public void setAdditionalSanctionTabTitle(java.util.ArrayList additionalSanctionTabTitle) {
    //        this.additionalSanctionTabTitle = additionalSanctionTabTitle;
    //    }
    //
}


