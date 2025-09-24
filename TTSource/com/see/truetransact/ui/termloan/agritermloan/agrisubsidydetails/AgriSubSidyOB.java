/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyOB.java
 *
 * Created on April 29, 2009, 3:10 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails;
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
import com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails.AgriSubSidyTO;
import com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails.AgriValutionTO;
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
    java.util.ResourceBundle AgriSubSidyRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails.AgriSubSidyRB",ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap lookUpHash=null;
    HashMap keyValue=null;
    ArrayList key=new ArrayList();
    ArrayList value=new ArrayList();
    private ArrayList tableTitlemain=new ArrayList();
    private ArrayList tableTitlesub=new ArrayList();
    
    //subsidy details
    ComboBoxModel CbmTypeOfSubSidy;
    ComboBoxModel CbmDepositProdId;
    ComboBoxModel CbmRecivedFrom;
    
    
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
    private String	cboTypeOfSubSidy="";
    private String	cboDepositProdId="";
    private String	DepositNo="";
    private String	SubSidyDate="";
    private String	cboRecivedFrom="";
    private String	SubSidyAmt="";
    private String	AmtAdjusted="";
    private String	AmtRefunded="";
    private String	RefundDate="";
    private String	OutStandingAmt="";
    
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
            setSubLimitTableTitle();
            tblValution=new EnhancedTableModel(null,tableTitlemain);
            valutionUtil.setAttributeKey("SLNO");
            subSidyUtil.setAttributeKey("SLNO");
            setSubSidyTableTitle();
            tblSubsidyDetails=new EnhancedTableModel(null,tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void fillDropDown()throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("TERM_LOAN.SUBSIDY");
        lookup_keys.add("TERM_LOAN.SUBSIDY_REC_FROM");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SUBSIDY"));
        //        setCbmAddrType_PoA(new ComboBoxModel(key, value));
        
        setCbmTypeOfSubSidy(new ComboBoxModel(key, value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SUBSIDY_REC_FROM"));
        setCbmRecivedFrom(new ComboBoxModel(key, value));
        setBlankKeyValue();
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProductTD");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        
        setCbmDepositProdId(new ComboBoxModel(key, value));
        
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
    public  ArrayList setSubLimitTableTitle(){
        tableTitlemain=new ArrayList();
        tableTitlemain.add(AgriSubSidyRB.getString("colum0"));
        tableTitlemain.add(AgriSubSidyRB.getString("colum1"));
        tableTitlemain.add(AgriSubSidyRB.getString("colum2"));
        tableTitlemain.add(AgriSubSidyRB.getString("colum3"));
        return tableTitlemain;
    }
    
    public ArrayList setSubSidyTableTitle(){
        tableTitlesub=new ArrayList();
        tableTitlesub.add(AgriSubSidyRB.getString("colum8"));
        tableTitlesub.add(AgriSubSidyRB.getString("colum4"));
        tableTitlesub.add(AgriSubSidyRB.getString("colum5"));
        tableTitlesub.add(AgriSubSidyRB.getString("colum6"));
        tableTitlesub.add(AgriSubSidyRB.getString("colum7"));
        return tableTitlesub;
    }
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
        
        subSidySingleList.add(String.valueOf(size));
        subSidySingleList.add(CommonUtil.convertObjToStr(getCbmTypeOfSubSidy().getKeyForSelected()));
        subSidySingleList.add(getDepositNo());
        subSidySingleList.add(CommonUtil.convertObjToStr(getSubSidyDate()));
        subSidySingleList.add(getSubSidyAmt());
        //        subSidySingleList.add(getInspectObservation());
        //
        subSidySingleMap.put("SLNO",String .valueOf(size));
        subSidySingleMap.put("TYPE_OF_SUBSIDY",CommonUtil.convertObjToStr(getCbmTypeOfSubSidy().getKeyForSelected()));
        subSidySingleMap.put("PROD_ID",CommonUtil.convertObjToStr(getCbmDepositProdId().getKeyForSelected()));
        subSidySingleMap.put("DEPOSIT_NO",getDepositNo());
        subSidySingleMap.put("SUBSIDY_DT",getSubSidyDate());
        subSidySingleMap.put("RECIVED_FROM",CommonUtil.convertObjToStr(getCbmRecivedFrom().getKeyForSelected()));
        subSidySingleMap.put("SUBSIDY_AMT",getSubSidyAmt());
        subSidySingleMap.put("ADJUSTED_AMT",getAmtAdjusted());
        subSidySingleMap.put("REFUNDED_AMT",getAmtRefunded());
        subSidySingleMap.put("REFUNDED_DT",getRefundDate());
        subSidySingleMap.put("OUTSTANDING_AMT",getOutStandingAmt());
        subSidySingleMap.put("ACCT_NUM",getStrAcctNo());
        subSidySingleMap.put("COMMAND","");
        
    }
    public void deleteSubSidyData(int row){
        HashMap resultMap =(HashMap)subSidyUtil.deleteTableValues(row);
        allSubSidyMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allSubSidyList=(ArrayList)resultMap.get("TABLE_VALUES");
        tblSubsidyDetails.setDataArrayList(allSubSidyList, tableTitlesub);
        
        //        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
    }
    
    //while populating data
    public void setSubSidyTO(ArrayList subSidyList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
//        subSidyUtil=new TableUtil();
        if(subSidyList !=null){
            for(int i=0;i<subSidyList.size();i++){
                AgriSubSidyTO agriSubSidyTO=(AgriSubSidyTO)subSidyList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getTypeOfSubSidy()));
                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getDepositNo()));
                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
                singleList.add(CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyAmt()));
                //                singleList.add(agriInspectionTO.getAreaInspectObservation());
                groupList.add(singleList);
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriSubSidyTO.getSlno()));
                singleMap.put("TYPE_OF_SUBSIDY",CommonUtil.convertObjToStr(agriSubSidyTO.getTypeOfSubSidy()));
                singleMap.put("PROD_ID",CommonUtil.convertObjToStr(agriSubSidyTO.getDepositProdId()));
                singleMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(agriSubSidyTO.getDepositNo()));
                singleMap.put("SUBSIDY_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
                singleMap.put("RECIVED_FROM",CommonUtil.convertObjToStr(agriSubSidyTO.getRecivedFrom()));
                singleMap.put("SUBSIDY_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyDt()));
                singleMap.put("SUBSIDY_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getSubSidyAmt()));
                singleMap.put("ADJUSTED_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getAmtAdjusted()));
                singleMap.put("REFUNDED_DT",CommonUtil.convertObjToStr(agriSubSidyTO.getRefundDate()));
                singleMap.put("REFUNDED_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getAmtRefunded()));
                singleMap.put("OUTSTANDING_AMT",CommonUtil.convertObjToStr(agriSubSidyTO.getOutStandingAmt()));
                singleMap.put("ACCT_NUM",act_num);
                singleMap.put("COMMAND","UPDATE");
                finalMap.put(agriSubSidyTO.getSlno(),singleMap);
                
            }
            
            
            allSubSidyMap=finalMap;
            allSubSidyList=groupList;
            subSidyUtil.setAllValues(allSubSidyMap);
            subSidyUtil.setTableValues(allSubSidyList);
            tblSubsidyDetails.setDataArrayList(allSubSidyList,setSubSidyTableTitle());
            finalMap=null;;
        }
    }
    ///populate data from db
    public void setValutionTO(ArrayList subLimitList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
//        valutionUtil=new TableUtil();
        if(subLimitList !=null){
            for(int i=0;i<subLimitList.size();i++){
                AgriValutionTO agriValutionTO=(AgriValutionTO)subLimitList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getPropertyType()));
                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getValutionDt()));
                singleList.add(CommonUtil.convertObjToStr(agriValutionTO.getValutionAmt()));
                groupList.add(singleList);
                
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriValutionTO.getSlno()));
                singleMap.put("PROPERTY_TYPE",CommonUtil.convertObjToStr(agriValutionTO.getPropertyType()));
                singleMap.put("VALUTION_DT",CommonUtil.convertObjToStr(agriValutionTO.getValutionDt()));
                singleMap.put("VALUTION_AMT",CommonUtil.convertObjToStr(agriValutionTO.getValutionAmt()));
                singleMap.put("VALUTION_BY",CommonUtil.convertObjToStr(agriValutionTO.getValuatedBy()));
                singleMap.put("VALUTION_REMARKS",CommonUtil.convertObjToStr(agriValutionTO.getValutionRemarks()));
                singleMap.put("ACCT_NUM",act_num);
                singleMap.put("COMMAND","UPDATE");
                finalMap.put(agriValutionTO.getSlno(),singleMap);
            }
            
            allValutionMap=finalMap;
            allValutionList=groupList;
            valutionUtil.setAllValues(allValutionMap);
            valutionUtil.setTableValues(allValutionList);
            tblValution.setDataArrayList(allValutionList,setSubLimitTableTitle());
            finalMap=null;
        }
    }
    
    public void setValutionDetails(int row,String branch_id){
        ArrayList selectedList=(ArrayList)tblValution.getDataArrayList().get(row);
        java.util.Set keySet=allValutionMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allValutionMap.size();i++){
            if(((HashMap)allValutionMap.get(objKeySet[i])).get("SLNO").equals(selectedList.get(0))){
                HashMap singleMap=(HashMap)allValutionMap.get(objKeySet[i]);
                System.out.println("singleMap$$$$$"+singleMap);
                setPropertyType(CommonUtil.convertObjToStr(singleMap.get("PROPERTY_TYPE")));
                setTdtValutionDt(CommonUtil.convertObjToStr(singleMap.get("VALUTION_DT")));
                setTxtValutionAmt(CommonUtil.convertObjToStr(singleMap.get("VALUTION_AMT")));
                setTxtValuatedBy(CommonUtil.convertObjToStr(singleMap.get("VALUTION_BY")));
                setValutionRemarks(CommonUtil.convertObjToStr(singleMap.get("VALUTION_REMARKS")));
                singleMap.put("EMP_ID",singleMap.get("VALUTION_BY"));
                singleMap.put("BRANCH_CODE",branch_id);
                java.util.List lst =(java.util.List)ClientUtil.executeQuery("getEmployeeName", singleMap);
                if(lst!=null && lst.size()>0){
                    HashMap resultMap=(HashMap)lst.get(0);
                    setLbDisplaylDesig(CommonUtil.convertObjToStr(resultMap.get("DESIGNATION")));
                    setLblDisPlayName(CommonUtil.convertObjToStr(resultMap.get("EMP NAME")));
                }
                //                setSubLimitAmt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT")));
                //                setSubLimitFromDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
                //                setSubLimitToDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
                break;
            }
        }
        setChanged();
        this.notifyObservers();
    }
    public void setSubSidyDetails(int row){
        ArrayList data=(ArrayList)tblSubsidyDetails.getDataArrayList().get(row);
        java.util.Set keySet=allSubSidyMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allSubSidyMap.size();i++){
            if(((HashMap)allSubSidyMap.get(objKeySet[i])).get("SLNO").equals(data.get(0))){
                HashMap singleMap=(HashMap)allSubSidyMap.get(objKeySet[i]);
                System.out.println("singleMap   #####"+singleMap);
                setCboTypeOfSubSidy(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_SUBSIDY")));
                setCboDepositProdId(CommonUtil.convertObjToStr(singleMap.get("PROD_ID")));
                setDepositNo(CommonUtil.convertObjToStr(singleMap.get("DEPOSIT_NO")));
                setSubSidyDate(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_DT")));
                setCboRecivedFrom(CommonUtil.convertObjToStr(singleMap.get("RECIVED_FROM")));
                setSubSidyAmt(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_AMT")));
                setAmtAdjusted(CommonUtil.convertObjToStr(singleMap.get("ADJUSTED_AMT")));
                setAmtRefunded(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_AMT")));
                setRefundDate(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_DT")));
                setOutStandingAmt(CommonUtil.convertObjToStr(singleMap.get("OUTSTANDING_AMT")));
                
                
                
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
        tblSubsidyDetails.setDataArrayList(null,setSubLimitTableTitle());
        tblValution.setDataArrayList(null,setSubSidyTableTitle());
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
    public void resetSubsidyDetails(){
        setCboTypeOfSubSidy("");
        setCboDepositProdId("");
        setDepositNo("");
        setSubSidyDate("");
        setCboRecivedFrom("");
        setSubSidyAmt("");
        setAmtAdjusted("");
        setAmtRefunded("");
        setRefundDate("");
        setOutStandingAmt("");
        setChanged();
    }
    public void resetTable(){
        tblSubsidyDetails.setDataArrayList(null,setSubSidyTableTitle());
        tblValution.setDataArrayList(null,setSubLimitTableTitle());
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
    public HashMap  setAgriValutionTo(){
        AgriValutionTO agriValutionTO=new AgriValutionTO();
        HashMap resultValutionMap=new HashMap();
        ArrayList removedValues=new ArrayList();
        removedValues=valutionUtil.getRemovedValues();
        if(allValutionMap !=null){
            System.out.println("allValutionMap$$$$"+allValutionMap);
            java.util.Set keySet=allValutionMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            for(int i=0;i<allValutionMap.size();i++){
                HashMap singleMap=(HashMap)allValutionMap.get(objKeySet[i]);
                agriValutionTO=new AgriValutionTO();
                agriValutionTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriValutionTO.setPropertyType(CommonUtil.convertObjToStr(singleMap.get("PROPERTY_TYPE")));
                agriValutionTO.setValutionAmt(CommonUtil.convertObjToDouble(singleMap.get("VALUTION_AMT")));
                agriValutionTO.setValuatedBy(CommonUtil.convertObjToStr(singleMap.get("VALUTION_BY")));
                agriValutionTO.setValutionRemarks(CommonUtil.convertObjToStr(singleMap.get("VALUTION_REMARKS")));
                agriValutionTO.setValutionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("VALUTION_DT"))));
                
                agriValutionTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriValutionTO.setCommand(CommonUtil.convertObjToStr( singleMap.get("COMMAND")));
                if (singleMap.get("COMMAND").equals("INSERT")){
                    agriValutionTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (singleMap.get("COMMAND").equals("UPDATE")){
                    agriValutionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                resultValutionMap.put(agriValutionTO.getSlno(),agriValutionTO);
            }
            HashMap deletedMap=null;
            //delete record also we shoude update
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    deletedMap=(HashMap)removedValues.get(i);
                    agriValutionTO=new AgriValutionTO();
                    agriValutionTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    agriValutionTO.setPropertyType(CommonUtil.convertObjToStr(deletedMap.get("PROPERTY_TYPE")));
                    agriValutionTO.setValutionAmt(CommonUtil.convertObjToDouble(deletedMap.get("VALUTION_AMT")));
                    agriValutionTO.setValuatedBy(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_BY")));
                    agriValutionTO.setValutionRemarks(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_REMARKS")));
                    agriValutionTO.setValutionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("VALUTION_DT"))));
                    agriValutionTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
                    agriValutionTO.setCommand(CommonUtil.convertObjToStr( deletedMap.get("COMMAND")));
                    resultValutionMap.put(agriValutionTO.getSlno(),agriValutionTO);
                }
        }
        return resultValutionMap;
        
    }
    
    //while   saving time calll  from do action
    public HashMap  setAgriSubSidyTo(){
        
        AgriSubSidyTO agriSubSidyTO=new AgriSubSidyTO();
        HashMap resultInspectionMap=new HashMap();
        ArrayList removedValues=subSidyUtil.getRemovedValues();
        if(allSubSidyMap !=null){
            System.out.println("allSubSidyMap$$$$"+allSubSidyMap);
            java.util.Set keySet=allSubSidyMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            
            for(int i=0;i<allSubSidyMap.size();i++){
                HashMap singleMap=(HashMap)allSubSidyMap.get(objKeySet[i]);
                agriSubSidyTO=new AgriSubSidyTO();
                agriSubSidyTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriSubSidyTO.setTypeOfSubSidy(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_SUBSIDY")));
                agriSubSidyTO.setDepositProdId(CommonUtil.convertObjToStr(singleMap.get("PROD_ID")));
                agriSubSidyTO.setDepositNo(CommonUtil.convertObjToStr(singleMap.get("DEPOSIT_NO")));
                agriSubSidyTO.setSubSidyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_DT"))));
                agriSubSidyTO.setRecivedFrom(CommonUtil.convertObjToStr(singleMap.get("RECIVED_FROM")));
                agriSubSidyTO.setSubSidyAmt(CommonUtil.convertObjToStr(singleMap.get("SUBSIDY_AMT")));
                agriSubSidyTO.setAmtAdjusted(CommonUtil.convertObjToStr(singleMap.get("ADJUSTED_AMT")));
                agriSubSidyTO.setAmtRefunded(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_AMT")));
                agriSubSidyTO.setRefundDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("REFUNDED_DT"))));
                agriSubSidyTO.setOutStandingAmt(CommonUtil.convertObjToStr(singleMap.get("OUTSTANDING_AMT")));
                agriSubSidyTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriSubSidyTO.setCommand(CommonUtil.convertObjToStr(singleMap.get("COMMAND")));
                if (singleMap.get("COMMAND").equals("INSERT")){
                    agriSubSidyTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (singleMap.get("COMMAND").equals("UPDATE")){
                    agriSubSidyTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                resultInspectionMap.put(agriSubSidyTO.getSlno(),agriSubSidyTO);
            }
            HashMap deletedMap=null;
            //delete record also we shoude update
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    deletedMap=(HashMap)removedValues.get(i);
                    agriSubSidyTO=new AgriSubSidyTO();
                    agriSubSidyTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    agriSubSidyTO.setTypeOfSubSidy(CommonUtil.convertObjToStr(deletedMap.get("TYPE_OF_SUBSIDY")));
                    agriSubSidyTO.setDepositProdId(CommonUtil.convertObjToStr(deletedMap.get("PROD_ID")));
                    agriSubSidyTO.setDepositNo(CommonUtil.convertObjToStr(deletedMap.get("DEPOSIT_NO")));
                    agriSubSidyTO.setSubSidyDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("SUBSIDY_DT"))));
                    agriSubSidyTO.setRecivedFrom(CommonUtil.convertObjToStr(deletedMap.get("RECIVED_FROM")));
                    agriSubSidyTO.setSubSidyAmt(CommonUtil.convertObjToStr(deletedMap.get("SUBSIDY_AMT")));
                    agriSubSidyTO.setAmtAdjusted(CommonUtil.convertObjToStr(deletedMap.get("ADJUSTED_AMT")));
                    agriSubSidyTO.setAmtRefunded(CommonUtil.convertObjToStr(deletedMap.get("REFUNDED_AMT")));
                    agriSubSidyTO.setRefundDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("REFUNDED_DT"))));
                    agriSubSidyTO.setOutStandingAmt(CommonUtil.convertObjToStr(deletedMap.get("OUTSTANDING_AMT")));
                    agriSubSidyTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
                    agriSubSidyTO.setCommand(CommonUtil.convertObjToStr(deletedMap.get("COMMAND")));
                    agriSubSidyTO.setStatus(CommonConstants.STATUS_DELETED);
                    resultInspectionMap.put(agriSubSidyTO.getSlno(),agriSubSidyTO);
                }
            
        }
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
     * Getter for property CbmTypeOfSubSidy.
     * @return Value of property CbmTypeOfSubSidy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTypeOfSubSidy() {
        return CbmTypeOfSubSidy;
    }
    
    /**
     * Setter for property CbmTypeOfSubSidy.
     * @param CbmTypeOfSubSidy New value of property CbmTypeOfSubSidy.
     */
    public void setCbmTypeOfSubSidy(com.see.truetransact.clientutil.ComboBoxModel CbmTypeOfSubSidy) {
        this.CbmTypeOfSubSidy = CbmTypeOfSubSidy;
    }
    
    /**
     * Getter for property CbmDepositProdId.
     * @return Value of property CbmDepositProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepositProdId() {
        return CbmDepositProdId;
    }
    
    /**
     * Setter for property CbmDepositProdId.
     * @param CbmDepositProdId New value of property CbmDepositProdId.
     */
    public void setCbmDepositProdId(com.see.truetransact.clientutil.ComboBoxModel CbmDepositProdId) {
        this.CbmDepositProdId = CbmDepositProdId;
    }
    
    /**
     * Getter for property CbmRecivedFrom.
     * @return Value of property CbmRecivedFrom.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRecivedFrom() {
        return CbmRecivedFrom;
    }
    
    /**
     * Setter for property CbmRecivedFrom.
     * @param CbmRecivedFrom New value of property CbmRecivedFrom.
     */
    public void setCbmRecivedFrom(com.see.truetransact.clientutil.ComboBoxModel CbmRecivedFrom) {
        this.CbmRecivedFrom = CbmRecivedFrom;
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
     * Getter for property cboTypeOfSubSidy.
     * @return Value of property cboTypeOfSubSidy.
     */
    public java.lang.String getCboTypeOfSubSidy() {
        return cboTypeOfSubSidy;
    }
    
    /**
     * Setter for property cboTypeOfSubSidy.
     * @param cboTypeOfSubSidy New value of property cboTypeOfSubSidy.
     */
    public void setCboTypeOfSubSidy(java.lang.String cboTypeOfSubSidy) {
        this.cboTypeOfSubSidy = cboTypeOfSubSidy;
    }
    
    /**
     * Getter for property cboDepositProdId.
     * @return Value of property cboDepositProdId.
     */
    public java.lang.String getCboDepositProdId() {
        return cboDepositProdId;
    }
    
    /**
     * Setter for property cboDepositProdId.
     * @param cboDepositProdId New value of property cboDepositProdId.
     */
    public void setCboDepositProdId(java.lang.String cboDepositProdId) {
        this.cboDepositProdId = cboDepositProdId;
    }
    
    /**
     * Getter for property DepositNo.
     * @return Value of property DepositNo.
     */
    public java.lang.String getDepositNo() {
        return DepositNo;
    }
    
    /**
     * Setter for property DepositNo.
     * @param DepositNo New value of property DepositNo.
     */
    public void setDepositNo(java.lang.String DepositNo) {
        this.DepositNo = DepositNo;
    }
    
    /**
     * Getter for property SubSidyDate.
     * @return Value of property SubSidyDate.
     */
    public java.lang.String getSubSidyDate() {
        return SubSidyDate;
    }
    
    /**
     * Setter for property SubSidyDate.
     * @param SubSidyDate New value of property SubSidyDate.
     */
    public void setSubSidyDate(java.lang.String SubSidyDate) {
        this.SubSidyDate = SubSidyDate;
    }
    
    /**
     * Getter for property cboRecivedFrom.
     * @return Value of property cboRecivedFrom.
     */
    public java.lang.String getCboRecivedFrom() {
        return cboRecivedFrom;
    }
    
    /**
     * Setter for property cboRecivedFrom.
     * @param cboRecivedFrom New value of property cboRecivedFrom.
     */
    public void setCboRecivedFrom(java.lang.String cboRecivedFrom) {
        this.cboRecivedFrom = cboRecivedFrom;
    }
    
    /**
     * Getter for property SubSidyAmt.
     * @return Value of property SubSidyAmt.
     */
    public java.lang.String getSubSidyAmt() {
        return SubSidyAmt;
    }
    
    /**
     * Setter for property SubSidyAmt.
     * @param SubSidyAmt New value of property SubSidyAmt.
     */
    public void setSubSidyAmt(java.lang.String SubSidyAmt) {
        this.SubSidyAmt = SubSidyAmt;
    }
    
    /**
     * Getter for property AmtAdjusted.
     * @return Value of property AmtAdjusted.
     */
    public java.lang.String getAmtAdjusted() {
        return AmtAdjusted;
    }
    
    /**
     * Setter for property AmtAdjusted.
     * @param AmtAdjusted New value of property AmtAdjusted.
     */
    public void setAmtAdjusted(java.lang.String AmtAdjusted) {
        this.AmtAdjusted = AmtAdjusted;
    }
    
    /**
     * Getter for property AmtRefunded.
     * @return Value of property AmtRefunded.
     */
    public java.lang.String getAmtRefunded() {
        return AmtRefunded;
    }
    
    /**
     * Setter for property AmtRefunded.
     * @param AmtRefunded New value of property AmtRefunded.
     */
    public void setAmtRefunded(java.lang.String AmtRefunded) {
        this.AmtRefunded = AmtRefunded;
    }
    
    /**
     * Getter for property RefundDate.
     * @return Value of property RefundDate.
     */
    public java.lang.String getRefundDate() {
        return RefundDate;
    }
    
    /**
     * Setter for property RefundDate.
     * @param RefundDate New value of property RefundDate.
     */
    public void setRefundDate(java.lang.String RefundDate) {
        this.RefundDate = RefundDate;
    }
    
    /**
     * Getter for property OutStandingAmt.
     * @return Value of property OutStandingAmt.
     */
    public java.lang.String getOutStandingAmt() {
        return OutStandingAmt;
    }
    
    /**
     * Setter for property OutStandingAmt.
     * @param OutStandingAmt New value of property OutStandingAmt.
     */
    public void setOutStandingAmt(java.lang.String OutStandingAmt) {
        this.OutStandingAmt = OutStandingAmt;
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
    
}
