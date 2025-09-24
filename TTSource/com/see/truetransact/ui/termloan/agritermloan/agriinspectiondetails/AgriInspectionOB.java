/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInspectionOB.java
 *
 * Created on April 29, 2009, 3:10 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails;
//import com.see.truetransact.serverutil;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails.AgriSubLimitTO;
import com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails.AgriInspectionTO;
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
public class AgriInspectionOB extends CObservable{
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static  AgriInspectionOB agriInspectionOB;
    private static final Logger log = Logger.getLogger(AgriInspectionOB.class);
    java.util.ResourceBundle agriTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails.AgriInspectionRB",ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap lookUpHash=null;
    HashMap keyValue=null;
    ArrayList key=new ArrayList();
    ArrayList value=new ArrayList();
    private ArrayList tableTitlemain=new ArrayList();
    private ArrayList tableTitlesub=new ArrayList();
    ComboBoxModel CbmTypeOfInspection;
    ComboBoxModel CbmInspectionDetails;
    EnhancedTableModel tblSubLimit;
    EnhancedTableModel tblInspectionDetails;
    TableUtil SubLimitUtil=new TableUtil();
    private ArrayList subSingleTab=new ArrayList();
    private HashMap subSingleRecord=new HashMap();
    private String subLimitAmt="";
    private String subLimitFromDt="";
    private String subLimitToDt="";
    private LinkedHashMap allSubLimitMap=new LinkedHashMap();
    private String  mainLimit="";
    private ArrayList     allSubLimitList=new ArrayList();
    //insepection details
    private String cboTypeOfInspection="";
    private String    DateOfInspection="";
    private String    cboInspectionDetails="";
    private String InspectBy="";
    private String InspectObservation="";
    private String subLimitStrNo="";
    private String lblInspectName="";
    private String lblInspectPositions="";
    private LinkedHashMap allInspectionMap=new LinkedHashMap();
    private ArrayList     allInspectionList=new ArrayList();
    private HashMap       inspectionSingleMap=new HashMap();
    private ArrayList     inspectionSingleList=new ArrayList();
    private TableUtil     inspectionUtil=new TableUtil();
    /** Creates a new instance of AgriTermLoanOB */
    public AgriInspectionOB() {
        try{
            fillDropDown();
            setSubLimitTableTitle();
            tblSubLimit=new EnhancedTableModel(null,tableTitlemain);
            SubLimitUtil.setAttributeKey("SLNO");
            inspectionUtil.setAttributeKey("SLNO");
            setInspectionTableTitle();
            tblInspectionDetails=new EnhancedTableModel(null,tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void fillDropDown()throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("TERMLOAN.AGRI_INSPECTION");
        lookup_keys.add("TERMLOAN.AGRI_INSPECTIONDETAILS");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSPECTION"));
        //        setCbmAddrType_PoA(new ComboBoxModel(key, value));
        
        setCbmTypeOfInspection(new ComboBoxModel(key, value));
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSPECTIONDETAILS"));
        setCbmInspectionDetails(new ComboBoxModel(key, value));
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
        tableTitlemain.add(agriTermLoanRB.getString("colum0"));
        tableTitlemain.add(agriTermLoanRB.getString("colum1"));
        tableTitlemain.add(agriTermLoanRB.getString("colum2"));
        tableTitlemain.add(agriTermLoanRB.getString("colum3"));
        return tableTitlemain;
    }
    
    public ArrayList setInspectionTableTitle(){
        tableTitlesub=new ArrayList();
        tableTitlesub.add(agriTermLoanRB.getString("colum8"));
        tableTitlesub.add(agriTermLoanRB.getString("colum4"));
        tableTitlesub.add(agriTermLoanRB.getString("colum5"));
        tableTitlesub.add(agriTermLoanRB.getString("colum6"));
        tableTitlesub.add(agriTermLoanRB.getString("colum7"));
        return tableTitlesub;
    }
    public int addSubLimit(int row,boolean update){
        subSingleTab=new ArrayList();
        subSingleRecord=new HashMap();
        ArrayList data= (ArrayList)tblSubLimit.getDataArrayList();
        tblSubLimit.setDataArrayList(data, tableTitlemain);
        int dataSize=data.size();
        int option =-1;
        if(!update){
            insertSubLimitData(dataSize+1);
            HashMap resultMap = (HashMap)SubLimitUtil.insertTableValues(subSingleTab,subSingleRecord);
            allSubLimitMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubLimitList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubLimit.setDataArrayList(allSubLimitList, tableTitlemain);
        }else{
            insertSubLimitData(row+1);
            option=updateSubLimit(row);
        }
        subSingleTab=null;
        subSingleRecord=null;
        
        return option;
    }
    
    private int updateSubLimit(int row){
        int value=-1;
        try{
            
            HashMap resultMap=(HashMap)SubLimitUtil.updateTableValues(subSingleTab,subSingleRecord,row);
            allSubLimitMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubLimitList=(ArrayList)resultMap.get("TABLE_VALUES");
            value=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubLimit.setDataArrayList(allSubLimitList, tableTitlemain);
        }catch(Exception e){
            e.printStackTrace();
        }
        return(value);
    }
    private void insertSubLimitData(int data){
        subSingleTab.add(String .valueOf(data));
        subSingleTab.add(getSubLimitAmt());
        subSingleTab.add(getSubLimitFromDt());
        subSingleTab.add(getSubLimitToDt());
        
        subSingleRecord.put("SLNO",String.valueOf(data));
        subSingleRecord.put("SUBLIMIT",getSubLimitAmt());
        subSingleRecord.put("SUBLIMIT_FROMDT", getSubLimitFromDt());
        subSingleRecord.put("SUBLIMIT_TODT", getSubLimitToDt());
        subSingleRecord.put("ACCT_NUM",getSubLimitStrNo());
        subSingleRecord.put("COMMAND","");
    }
    
    public boolean validateValues(int selectedRow, boolean tableSelected){
        if(allSubLimitMap !=null){
            java.util.Set keySet=allSubLimitMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            double sublimitAmt=0.0;
            double mainLimits=CommonUtil.convertObjToDouble(getMainLimit()).doubleValue();
            HashMap singleMap=null;
            if(allSubLimitMap.size()>0){
                for(int i=0;i<allSubLimitMap.size();i++){
                    if(i==selectedRow){
                        continue;
                    }else{
                        singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                        Date fromDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
                        Date toDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
                        Date obserFromDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitFromDt()));
                        Date obserToDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitToDt()));
                        //                        if(fromDt.after(obserFromDt) || fromDt.before(obserToDt) ||
                        //                        toDt.after(obserFromDt) || toDt.before(obserToDt)){
                        if(!(fromDt.after(obserFromDt) && obserFromDt.after(toDt)
                        || obserToDt.after(obserFromDt))){  //  ||   toDt.after(obserFromDt) || toDt.before(obserToDt)
                            
                            ClientUtil.showMessageWindow("Period All Ready Covered");
                            return true;
                        }
                        sublimitAmt+= CommonUtil.convertObjToDouble(singleMap.get("SUBLIMIT")).doubleValue();
                    }
                }
            }
            sublimitAmt+=CommonUtil.convertObjToDouble(getSubLimitAmt()).doubleValue();
            if(sublimitAmt>0 && sublimitAmt>mainLimits){
                ClientUtil.showMessageWindow("SubLimit Amt Exceed the Main Limit");
                return true;
            }
        }
        return false;
    }
    
    public  void deleteSubLimitData(int row){
        int value=-1;
        try{
            HashMap resultMap=(HashMap)SubLimitUtil.deleteTableValues(row);
            allSubLimitMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubLimitList=(ArrayList)resultMap.get("TABLE_VALUES");
            value=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubLimit.setDataArrayList(allSubLimitList, tableTitlemain);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public int addInpection(int row ,boolean update){
        ArrayList data=(ArrayList)tblInspectionDetails.getDataArrayList();
        tblInspectionDetails.setDataArrayList(data, tableTitlesub);
        int option =-1;
        int dataSize=data.size();
        inspectionSingleList=new ArrayList();
        inspectionSingleMap=new HashMap();
        
        if(!update){
            insertInspectionData(dataSize+1);
            HashMap resultMap=(HashMap)inspectionUtil.insertTableValues(inspectionSingleList,inspectionSingleMap);
            allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
        }else{
            insertInspectionData(row+1);
            option=updateInspectionDetails( row);
        }
        inspectionSingleList=null;
        inspectionSingleMap=null;
        return option;
    }
    public int updateInspectionDetails(int row){
        int option=-1;
        try{
            HashMap resultMap=(HashMap)inspectionUtil.updateTableValues(inspectionSingleList, inspectionSingleMap, row);
            allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return option;
    }
    public void insertInspectionData(int size){
        
        inspectionSingleList.add(String.valueOf(size));
        inspectionSingleList.add(CommonUtil.convertObjToStr(getCbmTypeOfInspection().getKeyForSelected()));
        inspectionSingleList.add(getDateOfInspection());
        inspectionSingleList.add(CommonUtil.convertObjToStr(getCbmInspectionDetails().getKeyForSelected()));
        inspectionSingleList.add(getInspectBy());
        //        inspectionSingleList.add(getInspectObservation());
        
        inspectionSingleMap.put("SLNO",String .valueOf(size));
        inspectionSingleMap.put("TYPE_OF_INSPECTION",CommonUtil.convertObjToStr(getCbmTypeOfInspection().getKeyForSelected()));
        inspectionSingleMap.put("DATE_OF_INSPECTION",getDateOfInspection());
        inspectionSingleMap.put("INSPECTION_DETAILS",CommonUtil.convertObjToStr(getCbmInspectionDetails().getKeyForSelected()));
        inspectionSingleMap.put("INSPECT_BY",getInspectBy());
        inspectionSingleMap.put("ACCT_NUM",getSubLimitStrNo());
        inspectionSingleMap.put("INSPECT_OBSERVATION",getInspectObservation());
        inspectionSingleMap.put("COMMAND","");
        
    }
    public void deleteInspectionData(int row){
        HashMap resultMap =(HashMap)inspectionUtil.deleteTableValues(row);
        allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
        tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
        
        //        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
    }
    
    
    public void setInspectionTO(ArrayList inspectionList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
//        inspectionUtil=new  TableUtil();
        if(inspectionList !=null){
            for(int i=0;i<inspectionList.size();i++){
                AgriInspectionTO agriInspectionTO=(AgriInspectionTO)inspectionList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriInspectionTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriInspectionTO.getTypeOfInspection()));
                singleList.add(CommonUtil.convertObjToStr(agriInspectionTO.getDateOfInspection()));
                singleList.add(CommonUtil.convertObjToStr(agriInspectionTO.getInspectionDetails()));
                singleList.add(CommonUtil.convertObjToStr(agriInspectionTO.getInspectBy()));
                //                singleList.add(agriInspectionTO.getAreaInspectObservation());
                groupList.add(singleList);
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriInspectionTO.getSlno()));
                singleMap.put("TYPE_OF_INSPECTION",CommonUtil.convertObjToStr(agriInspectionTO.getTypeOfInspection()));
                singleMap.put("DATE_OF_INSPECTION",CommonUtil.convertObjToStr(agriInspectionTO.getDateOfInspection()));
                singleMap.put("INSPECTION_DETAILS",CommonUtil.convertObjToStr(agriInspectionTO.getInspectionDetails()));
                singleMap.put("INSPECT_BY",CommonUtil.convertObjToStr(agriInspectionTO.getInspectBy()));
                singleMap.put("ACCT_NUM",act_num);
                singleMap.put("INSPECT_OBSERVATION",CommonUtil.convertObjToStr(agriInspectionTO.getAreaInspectObservation()));
                singleMap.put("COMMAND","UPDATE");
                finalMap.put(agriInspectionTO.getSlno(),singleMap);
                
            }
            
            
            allInspectionMap=finalMap;
            allInspectionList=groupList;
            inspectionUtil.setAllValues(allInspectionMap);
            inspectionUtil.setTableValues(allInspectionList);
            tblInspectionDetails.setDataArrayList(allInspectionList,setInspectionTableTitle());
            finalMap=null;;
        }
    }
    
    public void setSubLimitTO(ArrayList subLimitList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
//        SubLimitUtil=new TableUtil();
        if(subLimitList !=null){
            for(int i=0;i<subLimitList.size();i++){
                AgriSubLimitTO agriSubLimitTO=(AgriSubLimitTO)subLimitList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimit()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getStartDt()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getEndDt()));
                groupList.add(singleList);
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriSubLimitTO.getSlno()));
                singleMap.put("SUBLIMIT",CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimit()));
                singleMap.put("SUBLIMIT_FROMDT",CommonUtil.convertObjToStr(agriSubLimitTO.getStartDt()));
                singleMap.put("SUBLIMIT_TODT",CommonUtil.convertObjToStr(agriSubLimitTO.getEndDt()));
                singleMap.put("ACCT_NUM",act_num);
                singleMap.put("COMMAND","UPDATE");
                finalMap.put(agriSubLimitTO.getSlno(),singleMap);
            }
            
            allSubLimitMap=finalMap;
            allSubLimitList=groupList;
            SubLimitUtil.setAllValues(allSubLimitMap);
            SubLimitUtil.setTableValues(allSubLimitList);
            tblSubLimit.setDataArrayList(allSubLimitList,setSubLimitTableTitle());
            finalMap=null;
        }
    }
    
    public void setSubLimitDetails(int row){
        ArrayList selectedList=(ArrayList)tblSubLimit.getDataArrayList().get(row);
        java.util.Set keySet=allSubLimitMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allSubLimitMap.size();i++){
            if(((HashMap)allSubLimitMap.get(objKeySet[i])).get("SLNO").equals(selectedList.get(0))){
                HashMap singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                System.out.println("singleMap$$$$$"+singleMap);
                setSubLimitAmt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT")));
                setSubLimitFromDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
                setSubLimitToDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
                break;
            }
        }
        setChanged();
        this.notifyObservers();
    }
    public void setInspectionDetails(int row,String branch_id){
        ArrayList data=(ArrayList)tblInspectionDetails.getDataArrayList().get(row);
        java.util.Set keySet=allInspectionMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allInspectionMap.size();i++){
            if(((HashMap)allInspectionMap.get(objKeySet[i])).get("SLNO").equals(data.get(0))){
                HashMap singleMap=(HashMap)allInspectionMap.get(objKeySet[i]);
                System.out.println("singleMap   #####"+singleMap);
                setCboTypeOfInspection(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_INSPECTION")));
                
                setCboInspectionDetails(CommonUtil.convertObjToStr(singleMap.get("INSPECTION_DETAILS")));
                setDateOfInspection(CommonUtil.convertObjToStr(singleMap.get("DATE_OF_INSPECTION")));
                setInspectBy(CommonUtil.convertObjToStr(singleMap.get("INSPECT_BY")));
                setInspectObservation(CommonUtil.convertObjToStr(singleMap.get("INSPECT_OBSERVATION")));
                 singleMap.put("EMP_ID",CommonUtil.convertObjToStr(singleMap.get("INSPECT_BY")));
                singleMap.put("BRANCH_CODE",branch_id);
                java.util.List lst =(java.util.List)ClientUtil.executeQuery("getEmployeeName", singleMap);
                if(lst!=null && lst.size()>0){
                    HashMap resultMap=(HashMap)lst.get(0);
                    setLblInspectPositions(CommonUtil.convertObjToStr(resultMap.get("DESIGNATION")));
                    setLblInspectName(CommonUtil.convertObjToStr(resultMap.get("EMP NAME")));
                }
                break;
            }
        }
        setChanged();
        this.notifyObservers();
    }
    public void resetFormComponets(){
        setCboInspectionDetails("");
        setCboTypeOfInspection("");
        setInspectBy("");
        setDateOfInspection("");
        setInspectObservation("");
        setSubLimitAmt("");
        setSubLimitFromDt("");
        setSubLimitToDt("");
        setChanged();
    }
    public void resetTable(){
        tblInspectionDetails.setDataArrayList(null,setInspectionTableTitle());
        tblSubLimit.setDataArrayList(null,setSubLimitTableTitle());
    }
    public void destroyObjects(){
        allInspectionList=null;
        allInspectionMap=null;
        allSubLimitList=null;
        allSubLimitMap=null;
        resetTable();
        subSingleTab=null;
        subSingleRecord=null;
        inspectionSingleList=null;
        inspectionSingleMap=null;
        
    }
//    public HashMap  setAgriSubLimitTo(){
//        AgriSubLimitTO agriSubLimitTO=new AgriSubLimitTO();
//        HashMap resultSubLimitMap=new HashMap();
//        ArrayList removedValues=new ArrayList();
//        removedValues=SubLimitUtil.getRemovedValues();
//        if(allSubLimitMap !=null){
//            System.out.println("allSubLimitMap$$$$"+allSubLimitMap);
//            java.util.Set keySet=allSubLimitMap.keySet();
//            Object objKeySet[]=(Object[])keySet.toArray();
//            
//            for(int i=0;i<allSubLimitMap.size();i++){
//                HashMap singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
//                agriSubLimitTO=new AgriSubLimitTO();
//                agriSubLimitTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
//                agriSubLimitTO.setSubLimit(CommonUtil.convertObjToDouble(singleMap.get("SUBLIMIT")));
//                agriSubLimitTO.setStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT"))));
//                agriSubLimitTO.setEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT"))));
//                agriSubLimitTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
//                agriSubLimitTO.setCommand(CommonUtil.convertObjToStr( singleMap.get("COMMAND")));
//                if (singleMap.get("COMMAND").equals("INSERT")){
//                    agriSubLimitTO.setStatus(CommonConstants.STATUS_CREATED);
//                }else if (singleMap.get("COMMAND").equals("UPDATE")){
//                    agriSubLimitTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                }
//                resultSubLimitMap.put(agriSubLimitTO.getSlno(),agriSubLimitTO);
//            }
//            HashMap deletedMap=null;
//            //delete record also we shoude update
//            if(removedValues !=null)
//                for(int i=0;i<removedValues.size();i++){
//                    deletedMap=(HashMap)removedValues.get(i);
//                    agriSubLimitTO=new AgriSubLimitTO();
//                    agriSubLimitTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
//                    agriSubLimitTO.setSubLimit(CommonUtil.convertObjToDouble(deletedMap.get("SUBLIMIT")));
//                    agriSubLimitTO.setStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("SUBLIMIT_FROMDT"))));
//                    agriSubLimitTO.setEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("SUBLIMIT_TODT"))));
//                    agriSubLimitTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
//                    agriSubLimitTO.setCommand(CommonUtil.convertObjToStr( deletedMap.get("COMMAND")));
//                    agriSubLimitTO.setStatus(CommonConstants.STATUS_DELETED);
//                    resultSubLimitMap.put(agriSubLimitTO.getSlno(),agriSubLimitTO);
//                }
//        }
//        return resultSubLimitMap;
//        
//    }
    public HashMap  setAgriInspectionTo(){
        AgriInspectionTO agriInspectionTO=new AgriInspectionTO();
        HashMap resultInspectionMap=new HashMap();
        ArrayList removedValues=inspectionUtil.getRemovedValues();
        if(allInspectionMap !=null){
            System.out.println("allSubLimitMap$$$$"+allInspectionMap);
            java.util.Set keySet=allInspectionMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            
            for(int i=0;i<allInspectionMap.size();i++){
                HashMap singleMap=(HashMap)allInspectionMap.get(objKeySet[i]);
                agriInspectionTO=new AgriInspectionTO();
                agriInspectionTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriInspectionTO.setTypeOfInspection(CommonUtil.convertObjToStr(singleMap.get("TYPE_OF_INSPECTION")));
                agriInspectionTO.setDateOfInspection(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("DATE_OF_INSPECTION"))));
                agriInspectionTO.setInspectionDetails(CommonUtil.convertObjToStr(singleMap.get("INSPECTION_DETAILS")));
                agriInspectionTO.setInspectBy(CommonUtil.convertObjToStr(singleMap.get("INSPECT_BY")));
                agriInspectionTO.setAreaInspectObservation(CommonUtil.convertObjToStr(singleMap.get("INSPECT_OBSERVATION")));
                agriInspectionTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriInspectionTO.setCommand(CommonUtil.convertObjToStr(singleMap.get("COMMAND")));
                if (singleMap.get("COMMAND").equals("INSERT")){
                    agriInspectionTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (singleMap.get("COMMAND").equals("UPDATE")){
                    agriInspectionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                resultInspectionMap.put(agriInspectionTO.getSlno(),agriInspectionTO);
            }
            HashMap deletedMap=null;
            //delete record also we shoude update
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    deletedMap=(HashMap)removedValues.get(i);
                    agriInspectionTO=new AgriInspectionTO();
                    agriInspectionTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    agriInspectionTO.setTypeOfInspection(CommonUtil.convertObjToStr(deletedMap.get("TYPE_OF_INSPECTION")));
                    agriInspectionTO.setDateOfInspection(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(deletedMap.get("DATE_OF_INSPECTION"))));
                    agriInspectionTO.setInspectionDetails(CommonUtil.convertObjToStr(deletedMap.get("INSPECTION_DETAILS")));
                    agriInspectionTO.setInspectBy(CommonUtil.convertObjToStr(deletedMap.get("INSPECT_BY")));
                    agriInspectionTO.setAreaInspectObservation(CommonUtil.convertObjToStr(deletedMap.get("INSPECT_OBSERVATION")));
                    agriInspectionTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
                    agriInspectionTO.setCommand(CommonUtil.convertObjToStr(deletedMap.get("COMMAND")));
                    agriInspectionTO.setStatus(CommonConstants.STATUS_DELETED);
                    resultInspectionMap.put(agriInspectionTO.getSlno(),agriInspectionTO);
                }
          
        }
          return resultInspectionMap;
    }
    
    /**
     * Getter for property CbmTypeOfInspection.
     * @return Value of property CbmTypeOfInspection.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTypeOfInspection() {
        return CbmTypeOfInspection;
    }
    
    /**
     * Setter for property CbmTypeOfInspection.
     * @param CbmTypeOfInspection New value of property CbmTypeOfInspection.
     */
    public void setCbmTypeOfInspection(com.see.truetransact.clientutil.ComboBoxModel CbmTypeOfInspection) {
        this.CbmTypeOfInspection = CbmTypeOfInspection;
    }
    
    /**
     * Getter for property CbmInspectionDetails.
     * @return Value of property CbmInspectionDetails.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInspectionDetails() {
        return CbmInspectionDetails;
    }
    
    /**
     * Setter for property CbmInspectionDetails.
     * @param CbmInspectionDetails New value of property CbmInspectionDetails.
     */
    public void setCbmInspectionDetails(com.see.truetransact.clientutil.ComboBoxModel CbmInspectionDetails) {
        this.CbmInspectionDetails = CbmInspectionDetails;
    }
    
    /**
     * Getter for property tblSubLimit.
     * @return Value of property tblSubLimit.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSubLimit() {
        return tblSubLimit;
    }
    
    /**
     * Setter for property tblSubLimit.
     * @param tblSubLimit New value of property tblSubLimit.
     */
    public void setTblSubLimit(com.see.truetransact.clientutil.EnhancedTableModel tblSubLimit) {
        this.tblSubLimit = tblSubLimit;
    }
    
    /**
     * Getter for property tblInspectionDetails.
     * @return Value of property tblInspectionDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInspectionDetails() {
        return tblInspectionDetails;
    }
    
    /**
     * Setter for property tblInspectionDetails.
     * @param tblInspectionDetails New value of property tblInspectionDetails.
     */
    public void setTblInspectionDetails(com.see.truetransact.clientutil.EnhancedTableModel tblInspectionDetails) {
        this.tblInspectionDetails = tblInspectionDetails;
    }
    
    /**
     * Getter for property subLimitAmt.
     * @return Value of property subLimitAmt.
     */
    public java.lang.String getSubLimitAmt() {
        return subLimitAmt;
    }
    
    /**
     * Setter for property subLimitAmt.
     * @param subLimitAmt New value of property subLimitAmt.
     */
    public void setSubLimitAmt(java.lang.String subLimitAmt) {
        this.subLimitAmt = subLimitAmt;
    }
    
    /**
     * Getter for property subLimitFromDt.
     * @return Value of property subLimitFromDt.
     */
    public java.lang.String getSubLimitFromDt() {
        return subLimitFromDt;
    }
    
    /**
     * Setter for property subLimitFromDt.
     * @param subLimitFromDt New value of property subLimitFromDt.
     */
    public void setSubLimitFromDt(java.lang.String subLimitFromDt) {
        this.subLimitFromDt = subLimitFromDt;
    }
    
    /**
     * Getter for property subLimitToDt.
     * @return Value of property subLimitToDt.
     */
    public java.lang.String getSubLimitToDt() {
        return subLimitToDt;
    }
    
    /**
     * Setter for property subLimitToDt.
     * @param subLimitToDt New value of property subLimitToDt.
     */
    public void setSubLimitToDt(java.lang.String subLimitToDt) {
        this.subLimitToDt = subLimitToDt;
    }
    
    /**
     * Getter for property cboTypeOfInspection.
     * @return Value of property cboTypeOfInspection.
     */
    public java.lang.String getCboTypeOfInspection() {
        return cboTypeOfInspection;
    }
    
    /**
     * Setter for property cboTypeOfInspection.
     * @param cboTypeOfInspection New value of property cboTypeOfInspection.
     */
    public void setCboTypeOfInspection(java.lang.String cboTypeOfInspection) {
        this.cboTypeOfInspection = cboTypeOfInspection;
    }
    
    /**
     * Getter for property DateOfInspection.
     * @return Value of property DateOfInspection.
     */
    public java.lang.String getDateOfInspection() {
        return DateOfInspection;
    }
    
    /**
     * Setter for property DateOfInspection.
     * @param DateOfInspection New value of property DateOfInspection.
     */
    public void setDateOfInspection(java.lang.String DateOfInspection) {
        this.DateOfInspection = DateOfInspection;
    }
    
    /**
     * Getter for property cboInspectionDetails.
     * @return Value of property cboInspectionDetails.
     */
    public java.lang.String getCboInspectionDetails() {
        return cboInspectionDetails;
    }
    
    /**
     * Setter for property cboInspectionDetails.
     * @param cboInspectionDetails New value of property cboInspectionDetails.
     */
    public void setCboInspectionDetails(java.lang.String cboInspectionDetails) {
        this.cboInspectionDetails = cboInspectionDetails;
    }
    
    /**
     * Getter for property InspectBy.
     * @return Value of property InspectBy.
     */
    public java.lang.String getInspectBy() {
        return InspectBy;
    }
    
    /**
     * Setter for property InspectBy.
     * @param InspectBy New value of property InspectBy.
     */
    public void setInspectBy(java.lang.String InspectBy) {
        this.InspectBy = InspectBy;
    }
    
    /**
     * Getter for property InspectObservation.
     * @return Value of property InspectObservation.
     */
    public java.lang.String getInspectObservation() {
        return InspectObservation;
    }
    
    /**
     * Setter for property InspectObservation.
     * @param InspectObservation New value of property InspectObservation.
     */
    public void setInspectObservation(java.lang.String InspectObservation) {
        this.InspectObservation = InspectObservation;
    }
    
    /**
     * Getter for property subLimitStrNo.
     * @return Value of property subLimitStrNo.
     */
    public java.lang.String getSubLimitStrNo() {
        return subLimitStrNo;
    }
    
    /**
     * Setter for property subLimitStrNo.
     * @param subLimitStrNo New value of property subLimitStrNo.
     */
    public void setSubLimitStrNo(java.lang.String subLimitStrNo) {
        this.subLimitStrNo = subLimitStrNo;
    }
    
    /**
     * Getter for property mainLimit.
     * @return Value of property mainLimit.
     */
    public java.lang.String getMainLimit() {
        return mainLimit;
    }
    
    /**
     * Setter for property mainLimit.
     * @param mainLimit New value of property mainLimit.
     */
    public void setMainLimit(java.lang.String mainLimit) {
        this.mainLimit = mainLimit;
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
     * Getter for property lblInspectName.
     * @return Value of property lblInspectName.
     */
    public java.lang.String getLblInspectName() {
        return lblInspectName;
    }
    
    /**
     * Setter for property lblInspectName.
     * @param lblInspectName New value of property lblInspectName.
     */
    public void setLblInspectName(java.lang.String lblInspectName) {
        this.lblInspectName = lblInspectName;
    }
    
    /**
     * Getter for property lblInspectPositions.
     * @return Value of property lblInspectPositions.
     */
    public java.lang.String getLblInspectPositions() {
        return lblInspectPositions;
    }
    
    /**
     * Setter for property lblInspectPositions.
     * @param lblInspectPositions New value of property lblInspectPositions.
     */
    public void setLblInspectPositions(java.lang.String lblInspectPositions) {
        this.lblInspectPositions = lblInspectPositions;
    }
    
}
