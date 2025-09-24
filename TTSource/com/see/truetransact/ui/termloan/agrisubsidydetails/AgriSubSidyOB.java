/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyOB.java
 *
 * Created on April 29, 2009, 3:10 PM
 */

package com.see.truetransact.ui.termloan.agrisubsidydetails;
//import com.see.truetransact.serverutil;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
//import com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails.AgriSubLimitTO;
//import com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails.AgriInspectionTO;
//import com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails.AgriSubSidyTO;
//import com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails.AgriValutionTO;
import  com.see.truetransact.transferobject.termloan.TermLoanExtenFacilityDetailsTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class AgriSubSidyOB extends CObservable{
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static  AgriSubSidyOB agriSubSidyOB;
    private static final Logger log = Logger.getLogger(AgriSubSidyOB.class);
    java.util.ResourceBundle AgriSubSidyRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyRB",ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap lookUpHash=null;
    HashMap keyValue=null;
    ArrayList key=new ArrayList();
    ArrayList value=new ArrayList();
    private ArrayList tableTitlemain=new ArrayList();
    private ArrayList tableTitlesub=new ArrayList();
    
    //subsidy details
    ComboBoxModel cbmDisbursement_mode;
    ComboBoxModel cbmSource_Code;
    
    private String command=null;
    
    EnhancedTableModel tblValution;//tblValution
    EnhancedTableModel tblSubsidyDetails;//tblSubsidyDetails
    TableUtil valutionUtil=new TableUtil();//valutionUtil
    private ArrayList subSingleTab=new ArrayList();
    private HashMap valutionSingleRecord=new HashMap();
    private LinkedHashMap allValutionMap=new LinkedHashMap();
    private ArrayList     allValutionList=new ArrayList();
    //VALUTION details
    private String      propertyType="";
    private String	tdtValutionDt="";
    private String	txtValutionAmt="";
    private String	txtValuatedBy="";
    private String	lblDisPlayName="";
    private String	lbDisplaylDesig="";
    private String	ValutionRemarks="";
    //subsidy details
    private String	cboDisbursement_mode="";
    private String	cboSource_Code="";
  
    private String	lblshowSolicitor_Name="";
    private String	lblshowValuerName="";
    private String	lblshowDeveloper_Name="";
    private String	lblshowProject_Name="";
    private String	txtReferalCode="";
//    private String	AmtRefunded="";
//    private String	RefundDate="";
//    private String	OutStandingAmt="";
    
    private LinkedHashMap allSubSidyMap=new LinkedHashMap();
    private ArrayList     allSubSidyList=new ArrayList();
    private HashMap       subSidySingleMap=new HashMap();
    private ArrayList     subSidySingleList=new ArrayList();
    private TableUtil     subSidyUtil=new TableUtil();//subSidyUtil
    private String        strAcctNo="";
    /** Creates a new instance of AgriTermLoanOB */
    public AgriSubSidyOB() {
        try{
            fillDropDown();
//            setSubLimitTableTitle();
            tblValution=new EnhancedTableModel(null,tableTitlemain);
            valutionUtil.setAttributeKey("SLNO");
            subSidyUtil.setAttributeKey("SLNO");
//            setSubSidyTableTitle();
            tblSubsidyDetails=new EnhancedTableModel(null,tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void fillDropDown()throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
//        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("TERM_LOAN.SOURCE_CODE");
        lookup_keys.add("TERM_LOAN.DISBURSEMENT_MODE");
//        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.DISBURSEMENT_MODE"));
//        //        setCbmAddrType_PoA(new ComboBoxModel(key, value));
//        
        setCbmDisbursement_mode(new ComboBoxModel(key, value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SOURCE_CODE"));
        setCbmSource_Code(new ComboBoxModel(key, value));
//        setBlankKeyValue();
//        lookUpHash = new HashMap();
//        lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProductTD");
//        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//        
//            setCbmDepositProdId(new ComboBoxModel(key, value));
        
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
//    public  ArrayList setSubLimitTableTitle(){
//        tableTitlemain=new ArrayList();
//        tableTitlemain.add(AgriSubSidyRB.getString("colum0"));
//        tableTitlemain.add(AgriSubSidyRB.getString("colum1"));
//        tableTitlemain.add(AgriSubSidyRB.getString("colum2"));
//        tableTitlemain.add(AgriSubSidyRB.getString("colum3"));
//        return tableTitlemain;
//    }
    
//    public ArrayList setSubSidyTableTitle(){
//        tableTitlesub=new ArrayList();
//        tableTitlesub.add(AgriSubSidyRB.getString("colum8"));
//        tableTitlesub.add(AgriSubSidyRB.getString("colum4"));
//        tableTitlesub.add(AgriSubSidyRB.getString("colum5"));
//        tableTitlesub.add(AgriSubSidyRB.getString("colum6"));
//        tableTitlesub.add(AgriSubSidyRB.getString("colum7"));
//        return tableTitlesub;
//    }
    public int addValution(int row,boolean update){
        subSingleTab=new ArrayList();
        valutionSingleRecord=new HashMap();
        ArrayList data= (ArrayList)tblValution.getDataArrayList();
        tblValution.setDataArrayList(data, tableTitlemain);
        int dataSize=data.size();
        int option =-1;
        if(!update){
            insertValutionData(dataSize+1);
            HashMap resultMap = (HashMap)valutionUtil.insertTableValues(subSingleTab,valutionSingleRecord);
            allValutionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allValutionList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblValution.setDataArrayList(allValutionList, tableTitlemain);
        }else{
            insertValutionData(row+1);
            option=updateValution(row);
        }
        subSingleTab=null;
        valutionSingleRecord=null;
        
        return option;
    }
    
    private int updateValution(int row){
        int value=-1;
        try{
            
            HashMap resultMap=(HashMap)valutionUtil.updateTableValues(subSingleTab,valutionSingleRecord,row);
            allValutionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allValutionList=(ArrayList)resultMap.get("TABLE_VALUES");
            value=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblValution.setDataArrayList(allValutionList, tableTitlemain);
        }catch(Exception e){
            e.printStackTrace();
        }
        return(value);
    }
    
    
    private void insertValutionData(int data){
        subSingleTab.add(String .valueOf(data));
        subSingleTab.add(getPropertyType());
        subSingleTab.add(getTdtValutionDt());
        subSingleTab.add(getTxtValutionAmt());
        
        valutionSingleRecord.put("SLNO",String.valueOf(data));
        valutionSingleRecord.put("PROPERTY_TYPE",getPropertyType());
        valutionSingleRecord.put("VALUTION_DT",getTdtValutionDt());
        valutionSingleRecord.put("VALUTION_AMT", getTxtValutionAmt());
        valutionSingleRecord.put("VALUTION_BY", getTxtValuatedBy());
        valutionSingleRecord.put("VALUTION_REMARKS", getValutionRemarks());
        valutionSingleRecord.put("ACCT_NUM",getStrAcctNo());
        valutionSingleRecord.put("COMMAND","");
    }
    
    //    public boolean validateValues(int selectedRow, boolean tableSelected){
    //        if(allValutionMap !=null){
    //            java.util.Set keySet=allValutionMap.keySet();
    //            Object objKeySet[]=(Object[])keySet.toArray();
    //            double sublimitAmt=0.0;
    //            //            double mainLimits=CommonUtil.convertObjToDouble(getMainLimit()).doubleValue();
    //            HashMap singleMap=null;
    //            if(allValutionMap.size()>0){
    //                for(int i=0;i<allValutionMap.size();i++){
    //                    if(i==selectedRow){
    //                        continue;
    //                    }else{
    //                        singleMap=(HashMap)allValutionMap.get(objKeySet[i]);
    //                        Date fromDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
    //                        Date toDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
    //                        //                        Date obserFromDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitFromDt()));
    //                        //                        Date obserToDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitToDt()));
    //                        //                        if(fromDt.after(obserFromDt) || fromDt.before(obserToDt) ||
    //                        //                        toDt.after(obserFromDt) || toDt.before(obserToDt)){
    //                        //                        if(!(fromDt.after(obserFromDt) && obserFromDt.after(toDt)
    //                        //                        || obserToDt.after(obserFromDt))){  //  ||   toDt.after(obserFromDt) || toDt.before(obserToDt)
    //                        //
    //                        //                            ClientUtil.showMessageWindow("Period All Ready Covered");
    //                        //                            return true;
    //                        //                        }
    //                        sublimitAmt+= CommonUtil.convertObjToDouble(singleMap.get("SUBLIMIT")).doubleValue();
    //                    }
    //                }
    //            }
    //            //            sublimitAmt+=CommonUtil.convertObjToDouble(getSubLimitAmt()).doubleValue();
    //            //            if(sublimitAmt>0 && sublimitAmt>mainLimits){
    //            //                ClientUtil.showMessageWindow("SubLimit Amt Exceed the Main Limit");
    //            //                return true;
    //            //            }
    //        }
    //        return false;
    //    }
    
    public  void deleteSubLimitData(int row){
        int value=-1;
        try{
            HashMap resultMap=(HashMap)valutionUtil.deleteTableValues(row);
            allValutionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allValutionList=(ArrayList)resultMap.get("TABLE_VALUES");
            value=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblValution.setDataArrayList(allValutionList, tableTitlemain);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public int addSubsidy(int row ,boolean update){
        ArrayList data=(ArrayList)tblSubsidyDetails.getDataArrayList();
        tblSubsidyDetails.setDataArrayList(data, tableTitlesub);
        int option =-1;
        int dataSize=data.size();
        subSidySingleList=new ArrayList();
        subSidySingleMap=new HashMap();
        
        if(!update){
            insertsubSidyData(dataSize+1);
            HashMap resultMap=(HashMap)subSidyUtil.insertTableValues(subSidySingleList,subSidySingleMap);
            allSubSidyMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubSidyList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubsidyDetails.setDataArrayList(allSubSidyList, tableTitlesub);
        }else{
            insertsubSidyData(row+1);
            option=updatesubSidyDetails( row);
        }
        subSidySingleList=null;
        subSidySingleMap=null;
        return option;
    }
    public int updatesubSidyDetails(int row){
        int option=-1;
        try{
            HashMap resultMap=(HashMap)subSidyUtil.updateTableValues(subSidySingleList, subSidySingleMap, row);
            allSubSidyMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubSidyList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubsidyDetails.setDataArrayList(allSubSidyList, tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return option;
    }
    public void insertsubSidyData(int size){
        
//        subSidySingleList.add(String.valueOf(size));
//        subSidySingleList.add(CommonUtil.convertObjToStr(getCbmTypeOfSubSidy().getKeyForSelected()));
//        subSidySingleList.add(getDepositNo());
//        subSidySingleList.add(CommonUtil.convertObjToStr(getSubSidyDate()));
//        subSidySingleList.add(getSubSidyAmt());
//        //        subSidySingleList.add(getInspectObservation());
//        //
//        subSidySingleMap.put("SLNO",String .valueOf(size));
////        subSidySingleMap.put("TYPE_OF_SUBSIDY",CommonUtil.convertObjToStr(getCbmTypeOfSubSidy().getKeyForSelected()));
////        subSidySingleMap.put("PROD_ID",CommonUtil.convertObjToStr(getCbmDepositProdId().getKeyForSelected()));
//        subSidySingleMap.put("DEPOSIT_NO",getDepositNo());
//        subSidySingleMap.put("SUBSIDY_DT",getSubSidyDate());
////        subSidySingleMap.put("RECIVED_FROM",CommonUtil.convertObjToStr(getCbmRecivedFrom().getKeyForSelected()));
//        subSidySingleMap.put("SUBSIDY_AMT",getSubSidyAmt());
//        subSidySingleMap.put("ADJUSTED_AMT",getAmtAdjusted());
//        subSidySingleMap.put("REFUNDED_AMT",getAmtRefunded());
//        subSidySingleMap.put("REFUNDED_DT",getRefundDate());
//        subSidySingleMap.put("OUTSTANDING_AMT",getOutStandingAmt());
//        subSidySingleMap.put("ACCT_NUM",getStrAcctNo());
//        subSidySingleMap.put("COMMAND","");
        
    }
    public void deleteSubSidyData(int row){
        HashMap resultMap =(HashMap)subSidyUtil.deleteTableValues(row);
        allSubSidyMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allSubSidyList=(ArrayList)resultMap.get("TABLE_VALUES");
        tblSubsidyDetails.setDataArrayList(allSubSidyList, tableTitlesub);
        
        //        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
    }
    
    //while populating data
    public void setSubSidyTO(ArrayList extenList,String act_num){
        System.out.println("extenList####"+extenList);
        if(extenList!=null &&  extenList.size()>0){
            TermLoanExtenFacilityDetailsTO   termLoanExtenFacilityDetailsTO=(TermLoanExtenFacilityDetailsTO)extenList.get(0);
        setTxtReferalCode(CommonUtil.convertObjToStr(termLoanExtenFacilityDetailsTO.getReferalCode()));
        setCboDisbursement_mode(CommonUtil.convertObjToStr(termLoanExtenFacilityDetailsTO.getDisbursementMode()));
        setCboSource_Code(CommonUtil.convertObjToStr(termLoanExtenFacilityDetailsTO.getSourceCode()));
        setCommand("UPDATE");
       
        }
//        LinkedHashMap finalMap=new LinkedHashMap();
//        HashMap       singleMap=new HashMap();
//        ArrayList singleList=new ArrayList();
//        ArrayList groupList=new ArrayList();
////        subSidyUtil=new TableUtil();
//        if(subSidyList !=null){
//            for(int i=0;i<subSidyList.size();i++){
//                AgriSubSidyTO agriSubSidyTO=(AgriSubSidyTO)subSidyList.get(i);
//                singleList=new ArrayList();
//                singleMap=new HashMap();
//                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSlno()));
//                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getTypeOfSubSidy()));
//                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getDepositNo()));
//                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
//                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyAmt()));
//                //                singleList.add(agriInspectionTO.getAreaInspectObservation());
//                groupList.add(singleList);
//                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriSubSidyTO.getSlno()));
//                singleMap.put("TYPE_OF_SUBSIDY",CommonUtil.convertObjToStr(agriSubSidyTO.getTypeOfSubSidy()));
//                singleMap.put("PROD_ID",CommonUtil.convertObjToStr(agriSubSidyTO.getDepositProdId()));
//                singleMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(agriSubSidyTO.getDepositNo()));
//                singleMap.put("SUBSIDY_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
//                singleMap.put("RECIVED_FROM",CommonUtil.convertObjToStr(agriSubSidyTO.getRecivedFrom()));
//                singleMap.put("SUBSIDY_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
//                singleMap.put("SUBSIDY_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyAmt()));
//                singleMap.put("ADJUSTED_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getAmtAdjusted()));
//                singleMap.put("REFUNDED_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getRefundDate()));
//                singleMap.put("REFUNDED_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getAmtRefunded()));
//                singleMap.put("OUTSTANDING_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getOutStandingAmt()));
//                singleMap.put("ACCT_NUM",act_num);
//                singleMap.put("COMMAND","UPDATE");
//                finalMap.put(agriSubSidyTO.getSlno(),singleMap);
//                
//            }
//            
//            
//            allSubSidyMap=finalMap;
//            allSubSidyList=groupList;
//            subSidyUtil.setAllValues(allSubSidyMap);
//            subSidyUtil.setTableValues(allSubSidyList);
//            tblSubsidyDetails.setDataArrayList(allSubSidyList,setSubSidyTableTitle());
//            finalMap=null;;
//        }
    }
    ///populate data from db
//    public void setValutionTO(ArrayList subLimitList,String act_num){
//        LinkedHashMap finalMap=new LinkedHashMap();
//        HashMap       singleMap=new HashMap();
//        ArrayList singleList=new ArrayList();
//        ArrayList groupList=new ArrayList();
////        valutionUtil=new TableUtil();
//        if(subLimitList !=null){
//            for(int i=0;i<subLimitList.size();i++){
//                AgriValutionTO agriValutionTO=(AgriValutionTO)subLimitList.get(i);
//                singleList=new ArrayList();
//                singleMap=new HashMap();
//                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getSlno()));
//                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getPropertyType()));
//                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getValutionDt()));
//                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getValutionAmt()));
//                groupList.add(singleList);
//                
//                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriValutionTO.getSlno()));
//                singleMap.put("PROPERTY_TYPE",CommonUtil.convertObjToStr(agriValutionTO.getPropertyType()));
//                singleMap.put("VALUTION_DT",CommonUtil.convertObjToStr(agriValutionTO.getValutionDt()));
//                singleMap.put("VALUTION_AMT",CommonUtil.convertObjToStr(agriValutionTO.getValutionAmt()));
//                singleMap.put("VALUTION_BY",CommonUtil.convertObjToStr(agriValutionTO.getValuatedBy()));
//                singleMap.put("VALUTION_REMARKS",CommonUtil.convertObjToStr(agriValutionTO.getValutionRemarks()));
//                singleMap.put("ACCT_NUM",act_num);
//                singleMap.put("COMMAND","UPDATE");
//                finalMap.put(agriValutionTO.getSlno(),singleMap);
//            }
//            
//            allValutionMap=finalMap;
//            allValutionList=groupList;
//            valutionUtil.setAllValues(allValutionMap);
//            valutionUtil.setTableValues(allValutionList);
//            tblValution.setDataArrayList(allValutionList,setSubLimitTableTitle());
//            finalMap=null;
//        }
//    }
    
//    public void setValutionDetails(int row,String branch_id){
//        ArrayList selectedList=(ArrayList)tblValution.getDataArrayList().get(row);
//        java.util.Set keySet=allValutionMap.keySet();
//        Object objKeySet[]=(Object[])keySet.toArray();
//        for(int i=0;i<allValutionMap.size();i++){
//            if(((HashMap)allValutionMap.get(objKeySet[i])).get("SLNO").equals(selectedList.get(0))){
//                HashMap singleMap=(HashMap)allValutionMap.get(objKeySet[i]);
//                System.out.println("singleMap$$$$$"+singleMap);
//                setPropertyType(CommonUtil.convertObjToStr(singleMap.get("PROPERTY_TYPE")));
//                setTdtValutionDt(CommonUtil.convertObjToStr(singleMap.get("VALUTION_DT")));
//                setTxtValutionAmt(CommonUtil.convertObjToStr(singleMap.get("VALUTION_AMT")));
//                setTxtValuatedBy(CommonUtil.convertObjToStr(singleMap.get("VALUTION_BY")));
//                setValutionRemarks(CommonUtil.convertObjToStr(singleMap.get("VALUTION_REMARKS")));
//                singleMap.put("EMP_ID",singleMap.get("VALUTION_BY"));
//                singleMap.put("BRANCH_CODE",branch_id);
//                java.util.List lst =(java.util.List)ClientUtil.executeQuery("getEmployeeName", singleMap);
//                if(lst!=null && lst.size()>0){
//                    HashMap resultMap=(HashMap)lst.get(0);
//                    setLbDisplaylDesig(CommonUtil.convertObjToStr(resultMap.get("DESIGNATION")));
//                    setLblDisPlayName(CommonUtil.convertObjToStr(resultMap.get("EMP NAME")));
//                }
//                //                setSubLimitAmt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT")));
//                //                setSubLimitFromDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
//                //                setSubLimitToDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
//                break;
//            }
//        }
//        setChanged();
//        this.notifyObservers();
//    }
    public void setSubSidyDetails(int row){
        ArrayList data=(ArrayList)tblSubsidyDetails.getDataArrayList().get(row);
        java.util.Set keySet=allSubSidyMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allSubSidyMap.size();i++){
            if(((HashMap)allSubSidyMap.get(objKeySet[i])).get("SLNO").equals(data.get(0))){
                HashMap singleMap=(HashMap)allSubSidyMap.get(objKeySet[i]);
                System.out.println("singleMap   #####"+singleMap);
                setCboDisbursement_mode(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_SUBSIDY")));
                setCboSource_Code(CommonUtil.convertObjToStr(singleMap.get("PROD_ID")));
                setLblshowSolicitor_Name(CommonUtil.convertObjToStr(singleMap.get("DEPOSIT_NO")));
                setLblshowValuerName(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_DT")));
                setLblshowProject_Name(CommonUtil.convertObjToStr(singleMap.get("RECIVED_FROM")));
                setLblshowDeveloper_Name(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_AMT")));
//                setAmtAdjusted(CommonUtil.convertObjToStr(singleMap.get("ADJUSTED_AMT")));
//                setAmtRefunded(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_AMT")));
//                setRefundDate(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_DT")));
//                setOutStandingAmt(CommonUtil.convertObjToStr(singleMap.get("OUTSTANDING_AMT")));
                
                
                
                //                setCboTypeOfInspection(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_INSPECTION")));
                //
                //                setCboInspectionDetails(CommonUtil.convertObjToStr(singleMap.get("INSPECTION_DETAILS")));
                //                setDateOfInspection(CommonUtil.convertObjToStr(singleMap.get("DATE_OF_INSPECTION")));
                //                setInspectBy(CommonUtil.convertObjToStr(singleMap.get("INSPECT_BY")));
                //                setInspectObservation(CommonUtil.convertObjToStr(singleMap.get("INSPECT_OBSERVATION")));
                break;
            }
        }
        setChanged();
        this.notifyObservers();
    }
    public void destoryObjects(){
        allSubSidyList=null;
        allSubSidyMap=null;
//        tblSubsidyDetails.setDataArrayList(null,setSubLimitTableTitle());
//        tblValution.setDataArrayList(null,setSubSidyTableTitle());
        subSidySingleList=null;
        subSingleTab=null;
        valutionSingleRecord=null;
        
        
    }
    public void resetFormComponets(){
        setPropertyType("");
        //        setStrAcctNo("");
        setTdtValutionDt("");
        setTxtValutionAmt("");
        setTxtValuatedBy("");
        setLblDisPlayName("");
        setLbDisplaylDesig("");
        setValutionRemarks("");
        setChanged();
        
    }
    //acc
    public void resetSubsidyDetails(){
        setCboDisbursement_mode("");
        setCboSource_Code("");
        setLblshowSolicitor_Name("");
        setLblshowValuerName("");
        setLblshowProject_Name("");
        setLblshowDeveloper_Name("");
        setTxtReferalCode("");
        setCommand(null);
//        setAmtAdjusted("");
//        setAmtRefunded("");
//        setRefundDate("");
//        setOutStandingAmt("");
        setChanged();
        notifyObservers();
    }
    public void resetTable(){
//        tblSubsidyDetails.setDataArrayList(null,setSubSidyTableTitle());
//        tblValution.setDataArrayList(null,setSubLimitTableTitle());
    }
    public void destroyObjects(){
        allSubSidyList=null;
        allSubSidyMap=null;
        allValutionList=null;
        allValutionMap=null;
        resetTable();
        subSingleTab=null;
        valutionSingleRecord=null;
        subSidySingleList=null;
        subSidySingleMap=null;
        
    }
    
    //While saving time
//    public HashMap  setAgriValutionTo(){
//        AgriValutionTO agriValutionTO=new AgriValutionTO();
//        HashMap resultValutionMap=new HashMap();
//        ArrayList removedValues=new ArrayList();
//        removedValues=valutionUtil.getRemovedValues();
//        if(allValutionMap !=null){
//            System.out.println("allValutionMap$$$$"+allValutionMap);
//            java.util.Set keySet=allValutionMap.keySet();
//            Object objKeySet[]=(Object[])keySet.toArray();
//            for(int i=0;i<allValutionMap.size();i++){
//                HashMap singleMap=(HashMap)allValutionMap.get(objKeySet[i]);
//                agriValutionTO=new AgriValutionTO();
//                agriValutionTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
//                agriValutionTO.setPropertyType(CommonUtil.convertObjToStr(singleMap.get("PROPERTY_TYPE")));
//                agriValutionTO.setValutionAmt(CommonUtil.convertObjToDouble(singleMap.get("VALUTION_AMT")));
//                agriValutionTO.setValuatedBy(CommonUtil.convertObjToStr(singleMap.get("VALUTION_BY")));
//                agriValutionTO.setValutionRemarks(CommonUtil.convertObjToStr(singleMap.get("VALUTION_REMARKS")));
//                agriValutionTO.setValutionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("VALUTION_DT"))));
//                
//                agriValutionTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
//                agriValutionTO.setCommand(CommonUtil.convertObjToStr( singleMap.get("COMMAND")));
//                if (singleMap.get("COMMAND").equals("INSERT")){
//                    agriValutionTO.setStatus(CommonConstants.STATUS_CREATED);
//                }else if (singleMap.get("COMMAND").equals("UPDATE")){
//                    agriValutionTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                }
//                resultValutionMap.put(agriValutionTO.getSlno(),agriValutionTO);
//            }
//            HashMap deletedMap=null;
//            //delete record also we shoude update
//            if(removedValues !=null)
//                for(int i=0;i<removedValues.size();i++){
//                    deletedMap=(HashMap)removedValues.get(i);
//                    agriValutionTO=new AgriValutionTO();
//                    agriValutionTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
//                    agriValutionTO.setPropertyType(CommonUtil.convertObjToStr(deletedMap.get("PROPERTY_TYPE")));
//                    agriValutionTO.setValutionAmt(CommonUtil.convertObjToDouble(deletedMap.get("VALUTION_AMT")));
//                    agriValutionTO.setValuatedBy(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_BY")));
//                    agriValutionTO.setValutionRemarks(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_REMARKS")));
//                    agriValutionTO.setValutionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_DT"))));
//                    agriValutionTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
//                    agriValutionTO.setCommand(CommonUtil.convertObjToStr( deletedMap.get("COMMAND")));
//                    resultValutionMap.put(agriValutionTO.getSlno(),agriValutionTO);
//                }
//        }
//        return resultValutionMap;
//        
//    }
    
    //while   saving time calll  from do action
    public HashMap  setAgriSubSidyTo(){
     
//        AgriSubSidyTO agriSubSidyTO=new AgriSubSidyTO();
        HashMap resultInspectionMap=new HashMap();
        if(getStrAcctNo().length()>0){
        TermLoanExtenFacilityDetailsTO termLoanExtenFacilityDetailsTO=new TermLoanExtenFacilityDetailsTO();
        termLoanExtenFacilityDetailsTO.setAcctNum(getStrAcctNo());
        termLoanExtenFacilityDetailsTO.setReferalCode(getTxtReferalCode());
        termLoanExtenFacilityDetailsTO.setSourceCode(CommonUtil.convertObjToStr(getCbmSource_Code().getKeyForSelected()));
        termLoanExtenFacilityDetailsTO.setDisbursementMode(CommonUtil.convertObjToStr(getCbmDisbursement_mode().getKeyForSelected()));
        if(getCommand()!=null && getCommand().equals("UPDATE"))
            termLoanExtenFacilityDetailsTO.setCommand(getCommand());
        else
            termLoanExtenFacilityDetailsTO.setCommand("INSERT");
        resultInspectionMap.put("TermLoanExtenFacilityDetailsTO",termLoanExtenFacilityDetailsTO);
        }else
            resultInspectionMap=null;
        return resultInspectionMap;
    }
    
    
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    
    /**
     * Getter for property propertyType.
     * @return Value of property propertyType.
     */
    public java.lang.String getPropertyType() {
        return propertyType;
    }
    
    /**
     * Setter for property propertyType.
     * @param propertyType New value of property propertyType.
     */
    public void setPropertyType(java.lang.String propertyType) {
        this.propertyType = propertyType;
    }
    
    /**
     * Getter for property txtValutionAmt.
     * @return Value of property txtValutionAmt.
     */
    public java.lang.String getTxtValutionAmt() {
        return txtValutionAmt;
    }
    
    /**
     * Setter for property txtValutionAmt.
     * @param txtValutionAmt New value of property txtValutionAmt.
     */
    public void setTxtValutionAmt(java.lang.String txtValutionAmt) {
        this.txtValutionAmt = txtValutionAmt;
    }
    
    /**
     * Getter for property txtValuatedBy.
     * @return Value of property txtValuatedBy.
     */
    public java.lang.String getTxtValuatedBy() {
        return txtValuatedBy;
    }
    
    /**
     * Setter for property txtValuatedBy.
     * @param txtValuatedBy New value of property txtValuatedBy.
     */
    public void setTxtValuatedBy(java.lang.String txtValuatedBy) {
        this.txtValuatedBy = txtValuatedBy;
    }
    
    /**
     * Getter for property lblDisPlayName.
     * @return Value of property lblDisPlayName.
     */
    public java.lang.String getLblDisPlayName() {
        return lblDisPlayName;
    }
    
    /**
     * Setter for property lblDisPlayName.
     * @param lblDisPlayName New value of property lblDisPlayName.
     */
    public void setLblDisPlayName(java.lang.String lblDisPlayName) {
        this.lblDisPlayName = lblDisPlayName;
    }
    
    /**
     * Getter for property lbDisplaylDesig.
     * @return Value of property lbDisplaylDesig.
     */
    public java.lang.String getLbDisplaylDesig() {
        return lbDisplaylDesig;
    }
    
    /**
     * Setter for property lbDisplaylDesig.
     * @param lbDisplaylDesig New value of property lbDisplaylDesig.
     */
    public void setLbDisplaylDesig(java.lang.String lbDisplaylDesig) {
        this.lbDisplaylDesig = lbDisplaylDesig;
    }
    
    /**
     * Getter for property ValutionRemarks.
     * @return Value of property ValutionRemarks.
     */
    public java.lang.String getValutionRemarks() {
        return ValutionRemarks;
    }
    
    /**
     * Setter for property ValutionRemarks.
     * @param ValutionRemarks New value of property ValutionRemarks.
     */
    public void setValutionRemarks(java.lang.String ValutionRemarks) {
        this.ValutionRemarks = ValutionRemarks;
    }
    
    /**
     * Getter for property tdtValutionDt.
     * @return Value of property tdtValutionDt.
     */
    public java.lang.String getTdtValutionDt() {
        return tdtValutionDt;
    }
    
    /**
     * Setter for property tdtValutionDt.
     * @param tdtValutionDt New value of property tdtValutionDt.
     */
    public void setTdtValutionDt(java.lang.String tdtValutionDt) {
        this.tdtValutionDt = tdtValutionDt;
    }
    
    /**
     * Getter for property strAcctNo.
     * @return Value of property strAcctNo.
     */
    public java.lang.String getStrAcctNo() {
        return strAcctNo;
    }
    
    /**
     * Setter for property strAcctNo.
     * @param strAcctNo New value of property strAcctNo.
     */
    public void setStrAcctNo(java.lang.String strAcctNo) {
        this.strAcctNo = strAcctNo;
    }
   
    
    /**
     * Getter for property tblValution.
     * @return Value of property tblValution.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblValution() {
        return tblValution;
    }
    
    /**
     * Setter for property tblValution.
     * @param tblValution New value of property tblValution.
     */
    public void setTblValution(com.see.truetransact.clientutil.EnhancedTableModel tblValution) {
        this.tblValution = tblValution;
    }
    
    /**
     * Getter for property tblSubsidyDetails.
     * @return Value of property tblSubsidyDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSubsidyDetails() {
        return tblSubsidyDetails;
    }
    
    /**
     * Setter for property tblSubsidyDetails.
     * @param tblSubsidyDetails New value of property tblSubsidyDetails.
     */
    public void setTblSubsidyDetails(com.see.truetransact.clientutil.EnhancedTableModel tblSubsidyDetails) {
        this.tblSubsidyDetails = tblSubsidyDetails;
    }
    
    /**
     * Getter for property cbmDisbursement_mode.
     * @return Value of property cbmDisbursement_mode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDisbursement_mode() {
        return cbmDisbursement_mode;
    }
    
    /**
     * Setter for property cbmDisbursement_mode.
     * @param cbmDisbursement_mode New value of property cbmDisbursement_mode.
     */
    public void setCbmDisbursement_mode(com.see.truetransact.clientutil.ComboBoxModel cbmDisbursement_mode) {
        this.cbmDisbursement_mode = cbmDisbursement_mode;
            setChanged();
    }
    
    /**
     * Getter for property cbmSource_Code.
     * @return Value of property cbmSource_Code.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSource_Code() {
        return cbmSource_Code;
    }
    
    /**
     * Setter for property cbmSource_Code.
     * @param cbmSource_Code New value of property cbmSource_Code.
     */
    public void setCbmSource_Code(com.see.truetransact.clientutil.ComboBoxModel cbmSource_Code) {
        this.cbmSource_Code = cbmSource_Code;
            setChanged();
    }
    
    /**
     * Getter for property cboDisbursement_mode.
     * @return Value of property cboDisbursement_mode.
     */
    public java.lang.String getCboDisbursement_mode() {
        return cboDisbursement_mode;
    }
    
    /**
     * Setter for property cboDisbursement_mode.
     * @param cboDisbursement_mode New value of property cboDisbursement_mode.
     */
    public void setCboDisbursement_mode(java.lang.String cboDisbursement_mode) {
        this.cboDisbursement_mode = cboDisbursement_mode;
    setChanged();        
    }
    
    /**
     * Getter for property cboSource_Code.
     * @return Value of property cboSource_Code.
     */
    public java.lang.String getCboSource_Code() {
        return cboSource_Code;
    }
    
    /**
     * Setter for property cboSource_Code.
     * @param cboSource_Code New value of property cboSource_Code.
     */
    public void setCboSource_Code(java.lang.String cboSource_Code) {
        this.cboSource_Code = cboSource_Code;
                setChanged();

    }
    
    /**
     * Getter for property lblshowSolicitor_Name.
     * @return Value of property lblshowSolicitor_Name.
     */
    public java.lang.String getLblshowSolicitor_Name() {
        return lblshowSolicitor_Name;
    
        
    }
    
    /**
     * Setter for property lblshowSolicitor_Name.
     * @param lblshowSolicitor_Name New value of property lblshowSolicitor_Name.
     */
    public void setLblshowSolicitor_Name(java.lang.String lblshowSolicitor_Name) {
        this.lblshowSolicitor_Name = lblshowSolicitor_Name;
        setChanged();
        
    }
    
    /**
     * Getter for property lblshowValuerName.
     * @return Value of property lblshowValuerName.
     */
    public java.lang.String getLblshowValuerName() {
        return lblshowValuerName;
    }
    
    /**
     * Setter for property lblshowValuerName.
     * @param lblshowValuerName New value of property lblshowValuerName.
     */
    public void setLblshowValuerName(java.lang.String lblshowValuerName) {
        this.lblshowValuerName = lblshowValuerName;
        setChanged();
        
    }
    
    /**
     * Getter for property lblshowDeveloper_Name.
     * @return Value of property lblshowDeveloper_Name.
     */
    public java.lang.String getLblshowDeveloper_Name() {
        return lblshowDeveloper_Name;
    }
    
    /**
     * Setter for property lblshowDeveloper_Name.
     * @param lblshowDeveloper_Name New value of property lblshowDeveloper_Name.
     */
    public void setLblshowDeveloper_Name(java.lang.String lblshowDeveloper_Name) {
        this.lblshowDeveloper_Name = lblshowDeveloper_Name;
        setChanged();
        
    }
    
    /**
     * Getter for property lblshowProject_Name.
     * @return Value of property lblshowProject_Name.
     */
    public java.lang.String getLblshowProject_Name() {
        return lblshowProject_Name;
    }
    
    /**
     * Setter for property lblshowProject_Name.
     * @param lblshowProject_Name New value of property lblshowProject_Name.
     */
    public void setLblshowProject_Name(java.lang.String lblshowProject_Name) {
        this.lblshowProject_Name = lblshowProject_Name;
        setChanged();
        
    }
    
    /**
     * Getter for property txtReferalCode.
     * @return Value of property txtReferalCode.
     */
    public java.lang.String getTxtReferalCode() {
        return txtReferalCode;
    }
    
    /**
     * Setter for property txtReferalCode.
     * @param txtReferalCode New value of property txtReferalCode.
     */
    public void setTxtReferalCode(java.lang.String txtReferalCode) {
        this.txtReferalCode = txtReferalCode;
        setChanged();
    }
    
    /**
     * Getter for property command.
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }
    
    /**
     * Setter for property command.
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
                setChanged();

    }
    
}
