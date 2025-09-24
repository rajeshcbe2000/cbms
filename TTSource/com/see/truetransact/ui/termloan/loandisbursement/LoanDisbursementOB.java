/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDisbursementOB.java
 *
 * Created on April 29, 2009, 3:10 PM
 */

package com.see.truetransact.ui.termloan.loandisbursement;
//import com.see.truetransact.serverutil;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.termloan.TermLoanDisburstTO;
import com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails.AgriSubLimitDetailsTO;
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
public class LoanDisbursementOB extends CObservable{
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static  LoanDisbursementOB agriSubLimitOB;
    private static final Logger log = Logger.getLogger(LoanDisbursementOB.class);
    java.util.ResourceBundle agriTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementRB",ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap lookUpHash=null;
    HashMap keyValue=null;
    ArrayList key=new ArrayList();
    ArrayList value=new ArrayList();
    private ArrayList tableTitlemain=new ArrayList();
    private ArrayList tableTitlesub=new ArrayList();
    ComboBoxModel cbmPurpose;
    ComboBoxModel cbmType;
    EnhancedTableModel tblSubLimit;
    EnhancedTableModel tblInspectionDetails;
    TableUtil SubLimitUtil=new TableUtil();
    private ArrayList subSingleTab=new ArrayList();
    private HashMap subSingleRecord=new HashMap();
    private String subLimitAmt="";
    private String subLimitFromDt="";
    private String subLimitToDt="";
    private int maxSubLimitslno=0;
    private int maxSubLimitDetailslno=0;
    
    private String tdtDisbursementDt="";
    private String cboDisbursementStage="";
    private boolean rdoDisbursementAmtRs=false;
    private boolean rdoDisbursementAmt=false;
    private String txtDisbursementAmtRs="";
    private String txtDisbursementAmt="";
    private String txtRemarks="";
  
  
    private LinkedHashMap allSubLimitMap=new LinkedHashMap();
    //    private LinkedHashMap allSubLimitDetailsMap=new LinkedHashMap();
    private String  mainLimit="";
    private ArrayList     allSubLimitList=new ArrayList();
    //insepection details
    private String cboPurpose="";
    
    private String    cboType="";
    private String hectare="";
    private String surveyno="";
    private String remarks="";
    private String subLimitStrNo="";
    private LinkedHashMap allInspectionMap=new LinkedHashMap();
    private LinkedHashMap tempallInspectionMap=new LinkedHashMap();
    private ArrayList     allInspectionList=new ArrayList();
    private HashMap       inspectionSingleMap=new HashMap();
    private ArrayList     inspectionSingleList=new ArrayList();
    private TableUtil     inspectionUtil=new TableUtil();
    private String         mainSlno="";
    private  HashMap resultSubLimitDetailsMap=new HashMap();
    private boolean tblSubLimitSelected=false;
    private int selectedSubLimitRow;
    private String mainSanctionDt="";
    private  String loanExpiryDate=null;
    private String loanOpenDt=null;
    /** Creates a new instance of AgriTermLoanOB */
    public  LoanDisbursementOB() {
        try{
            fillDropDown();
            setSubLimitTableTitle();
            tblSubLimit=new EnhancedTableModel(null,tableTitlemain);
            SubLimitUtil.setAttributeKey("SLNO");
            inspectionUtil.setAttributeKey("SLNO");
//            setInspectionTableTitle();
            tblInspectionDetails=new EnhancedTableModel(null,tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void fillDropDown()throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("DISBURSEMENT_STAGE");
        lookup_keys.add("TERMLOAN.AGRI_INSPECTIONDETAILS");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        setBlankKeyValue();
        getKeyValue((HashMap)keyValue.get("DISBURSEMENT_STAGE"));
        //        setCbmAddrType_PoA(new ComboBoxModel(key, value));
        
        setCbmType(new ComboBoxModel(key, value));
        setBlankKeyValue();
//        getKeyValue((HashMap)keyValue.get("AGRILOAN_PURPOSE_CODE"));
//        setCbmPurpose(new ComboBoxModel(key, value));
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
         tableTitlemain.add(agriTermLoanRB.getString("colum4"));
        return tableTitlemain;
    }
    
//    public ArrayList setInspectionTableTitle(){
//        tableTitlesub=new ArrayList();
//        tableTitlesub.add(agriTermLoanRB.getString("colum8"));
//        tableTitlesub.add(agriTermLoanRB.getString("colum4"));
//        tableTitlesub.add(agriTermLoanRB.getString("colum5"));
//        tableTitlesub.add(agriTermLoanRB.getString("colum6"));
//        tableTitlesub.add(agriTermLoanRB.getString("colum7"));
//        return tableTitlesub;
//    }
    public int addSubLimit(int row,boolean update){
        subSingleTab=new ArrayList();
        subSingleRecord=new HashMap();
        ArrayList data= (ArrayList)tblSubLimit.getDataArrayList();
        tblSubLimit.setDataArrayList(data, tableTitlemain);
        //        int dataSize=data.size();
        int dataSize=maxSubLimitslno;
        int option =-1;
        if(!update){
            insertSubLimitData(dataSize+1);//,allInspectionMap,allInspectionList ,false);
            HashMap resultMap = (HashMap)SubLimitUtil.insertTableValues(subSingleTab,subSingleRecord);
            allSubLimitMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allSubLimitList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            tblSubLimit.setDataArrayList(allSubLimitList, tableTitlemain);
            maxSubLimitslno++;
        }else{
            insertSubLimitData(row+1);//,allInspectionMap,allInspectionList,true);
            option=updateSubLimit(row);
        }
        tblInspectionDetails.setDataArrayList(null,tableTitlesub);
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
     public void setCbmProdId(String subLimitType) {
        if (CommonUtil.convertObjToStr(subLimitType).length()>1) {
           
                key = new ArrayList();
                value = new ArrayList();
         
                try {
                    lookUpHash = new HashMap();
                    HashMap param=new HashMap();
                    param.put(subLimitType,subLimitType);
                    lookUpHash.put(CommonConstants.MAP_NAME,"getAgriLoanSubLimit");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, param);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
      
        cbmType = new ComboBoxModel(key,value);
        this.cbmType = cbmType;
        setChanged();
    }
    
    private void insertSubLimitData(int data){//,LinkedHashMap subLimitDetailsMap,ArrayList subLimitDetailsList,boolean isFlag){
        subSingleTab.add(String .valueOf(data));
        subSingleTab.add(getTdtDisbursementDt());
        subSingleTab.add(getCboDisbursementStage());
        subSingleTab.add(getTxtDisbursementAmtRs());
         subSingleTab.add(getTxtDisbursementAmt());

    subSingleRecord.put("SLNO",String.valueOf(data));
    subSingleRecord.put("DISBURST_DT",getTdtDisbursementDt());
    subSingleRecord.put("DISBURST_STAGE", CommonUtil.convertObjToStr(cbmType.getKeyForSelected()));
    subSingleRecord.put("DISBURST_AMT", getTxtDisbursementAmtRs());
    subSingleRecord.put("DISBURST_AMT%", getTxtDisbursementAmt());
    if(isRdoDisbursementAmtRs())
        subSingleRecord.put("RDO_DISBURST_YES", "Y");
    else
        subSingleRecord.put("RDO_DISBURST_YES", "");
    if(isRdoDisbursementAmt())
        subSingleRecord.put("RDO_DISBURST_NO", "Y");
    else
        subSingleRecord.put("RDO_DISBURST_NO", "");

    subSingleRecord.put("REMARKS", getTxtRemarks());

//        if(isFlag){
//            System.out.println("isFlag"+isFlag);
//            subSingleRecord.put("SUBLIMIT_DETAILS_MAP", allInspectionMap);
//            subSingleRecord.put("SUBLIMIT_TABLE_LIST", allInspectionList);
//        }
        subSingleRecord.put("ACCT_NUM",getSubLimitStrNo());
        subSingleRecord.put("COMMAND","");
    }
    
    public boolean validateValues(int selectedRow, boolean tableSelected,boolean amtRs ,boolean amt){
        if(allSubLimitMap !=null){
            java.util.Set keySet=allSubLimitMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            double totalDisburstAmt=0.0;
            double mainLimits=CommonUtil.convertObjToDouble(getMainLimit()).doubleValue();
            int sublimitSlno=-1;
            if(mainSlno.length()>0)
                sublimitSlno=Integer.parseInt(mainSlno);
            HashMap singleMap=null;
            if(allSubLimitMap.size()>0){
                for(int i=0;i<allSubLimitMap.size();i++){
                    if(i==selectedRow){
                        continue;
                    }else{
                        singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                        double disburstAmt= CommonUtil.convertObjToDouble(singleMap.get("DISBURST_AMT")).doubleValue();
                        Date toDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
                        int tableSlno= CommonUtil.convertObjToInt(singleMap.get("SLNO"));
                        Date obserFromDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitFromDt()));
                        Date obserToDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getSubLimitToDt()));
                        //                        if(fromDt.after(obserFromDt) || fromDt.before(obserToDt) ||
                        //                        toDt.after(obserFromDt) || toDt.before(obserToDt)){
                        
                        //check with main sanction date
//                        if(mainSanctionDt.length()>0){
//                            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(mainSanctionDt),obserFromDt)<0){
//                                ClientUtil.showMessageWindow("SubLimit Sanction Should not Cross Mains Sanction Dt");
//                                return true;
//                            }
//                        }
//                        //making validation for new entry record
//                        if(fromDt !=null && selectedSubLimitRow==-1){
//                            if(!(obserFromDt.after(toDt)))//fromDt.before(obserFromDt) &&
//                                //                            || obserToDt.after(obserFromDt))){  //  ||   toDt.after(obserFromDt) || toDt.before(obserToDt)
//                            {
//                                ClientUtil.showMessageWindow("Period All Ready Covered");
//                            return true;
//                            }
//                        } else if(sublimitSlno>tableSlno){
//                            if(!obserFromDt.after(toDt)) {
//                                ClientUtil.showMessageWindow("Period All Ready Covered");
//                                return true;
//                                
//                            }
//                        }else if(sublimitSlno<tableSlno){
//                            if(!obserFromDt.before(toDt)) {
//                                ClientUtil.showMessageWindow("Period All Ready Covered");
//                                return true;
//                                
//                            }
//                        }
                        totalDisburstAmt+= disburstAmt;//CommonUtil.convertObjToDouble(singleMap.get("SUBLIMIT")).doubleValue();
                    }
                }
            }
            double percentage=0;
             totalDisburstAmt+=CommonUtil.convertObjToDouble(getTxtDisbursementAmtRs()).doubleValue();
            if(amtRs){
                double enteredAmt=CommonUtil.convertObjToDouble(getTxtDisbursementAmtRs()).doubleValue();
                percentage=getNearest((double)enteredAmt*100/mainLimits);
                 if(percentage>0)
                setTxtDisbursementAmt(String.valueOf(percentage));
            }else if(amt){
                double enteredAmt=CommonUtil.convertObjToDouble(getTxtDisbursementAmt()).doubleValue();
                percentage=getNearest((double)(mainLimits*enteredAmt)/100);
                 if(percentage>0)
                setTxtDisbursementAmtRs(String.valueOf(percentage));
            }
            
           
            if(totalDisburstAmt>0 && totalDisburstAmt>mainLimits){
                ClientUtil.showMessageWindow("Disbursement Amt Exceed the Main Limit");
                return true;
            }
        }
        return false;
    }
    
     public double getNearest(double roundAmount)  {
         long number = (long)(roundAmount*100);
         int roundingFactor = (int) ((roundAmount*100 - number) * 10);
         if (roundingFactor==0)
             return roundAmount;
         if (roundingFactor<5)
             return ((number)/100.0);//cnumber-1
         else 
             return ((number+1)/100.0);
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
    public int addInpection(int row ,boolean update,boolean sublimitMain){
        ArrayList data=(ArrayList)tblInspectionDetails.getDataArrayList();
        tblInspectionDetails.setDataArrayList(data, tableTitlesub);
        int option =-1;
        //           int dataSize=data.size();
        int dataSize=maxSubLimitDetailslno;
        inspectionSingleList=new ArrayList();
        inspectionSingleMap=new HashMap();
        HashMap subLimitMainRec=new HashMap();
        if(!update){
            insertInspectionData(dataSize+1);
            HashMap resultMap=(HashMap)inspectionUtil.insertTableValues(inspectionSingleList,inspectionSingleMap);
            allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            if (sublimitMain == true){
                int mainInt=Integer.parseInt(mainSlno);
                //                if(allSubLimitList.size()!=mainInt-1)
                subLimitMainRec = (HashMap) allSubLimitMap.get(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0));
                
                subLimitMainRec.put("SUBLIMIT_DETAILS_MAP", allInspectionMap);
                subLimitMainRec.put("SUBLIMIT_TABLE_LIST", allInspectionList);
                allSubLimitMap.put(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0), subLimitMainRec);
            }
            tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
            maxSubLimitDetailslno++;//maxSubLimitslno++;
        }else{
            insertInspectionData(row+1);
            option=updateInspectionDetails( row,sublimitMain);
        }
        inspectionSingleList=null;
        inspectionSingleMap=null;
        return option;
    }
    public int updateInspectionDetails(int row,boolean sublimitMain){
        int option=-1;
        HashMap subLimitMainRec=new HashMap();
        try{
            HashMap resultMap=(HashMap)inspectionUtil.updateTableValues(inspectionSingleList, inspectionSingleMap, row);
            allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
            allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
            option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
            if (sublimitMain == true){
                int mainInt=Integer.parseInt(mainSlno);
                subLimitMainRec = (HashMap) allSubLimitMap.get(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0));
                
                subLimitMainRec.put("SUBLIMIT_DETAILS_MAP", allInspectionMap);
                subLimitMainRec.put("SUBLIMIT_TABLE_LIST", allInspectionList);
                allSubLimitMap.put(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0), subLimitMainRec);
            }
            tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return option;
    }
    
    public void insertInspectionData(int size){
        
        
        inspectionSingleList.add(String.valueOf(size));
        inspectionSingleList.add(CommonUtil.convertObjToStr(getCbmPurpose().getKeyForSelected()));
         inspectionSingleList.add(CommonUtil.convertObjToStr(getCbmType().getKeyForSelected()));
        inspectionSingleList.add(getHectare());
        //        inspectionSingleList.add(get));
        inspectionSingleList.add(CommonUtil.convertObjToStr(mainSlno));
        
        //        inspectionSingleList.add(getSurveyno());
        
        inspectionSingleMap.put("SLNO",String .valueOf(size));
        inspectionSingleMap.put("PURPOSE",CommonUtil.convertObjToStr(getCbmPurpose().getKeyForSelected()));
        //        inspectionSingleMap.put("DATE_OF_INSPECTION",getDateOfInspection());
        inspectionSingleMap.put("TYPE",CommonUtil.convertObjToStr(getCbmType().getKeyForSelected()));
        inspectionSingleMap.put("HECTARE",getHectare());
        inspectionSingleMap.put("ACCT_NUM",getSubLimitStrNo());
        inspectionSingleMap.put("SURVEYNO",getSurveyno());
        inspectionSingleMap.put("MAIN_LIMIT_SLNO",mainSlno);
//        inspectionSingleMap.put("REMARKS",getRemarks());
        inspectionSingleMap.put("COMMAND","");
        
    }
    
    public void deleteInspectionData(int row,boolean sublimitMain){
        HashMap resultMap =(HashMap)inspectionUtil.deleteTableValues(row);
        allInspectionMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        allInspectionList=(ArrayList)resultMap.get("TABLE_VALUES");
        HashMap subLimitMainRec=new HashMap();
        if (sublimitMain == true){
            int mainInt=Integer.parseInt(mainSlno);
            subLimitMainRec = (HashMap) allSubLimitMap.get(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0));
            
            subLimitMainRec.put("SUBLIMIT_DETAILS_MAP", allInspectionMap);
            subLimitMainRec.put("SUBLIMIT_TABLE_LIST", allInspectionList);
            allSubLimitMap.put(((ArrayList) allSubLimitList.get(selectedSubLimitRow)).get(0), subLimitMainRec);
        }
        tblInspectionDetails.setDataArrayList(allInspectionList, tableTitlesub);
        
        //        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
    }
    
    //BEFORE INSPECTION DETAILS NOW AGRI SUBLIMIT DETILS
    public void setSubLimitDetailsTO(ArrayList inspectionList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
        if(allInspectionMap !=null)
            allInspectionMap.clear();
        else
            allInspectionMap=new LinkedHashMap();
        //        inspectionUtil=new  TableUtil();
        if(inspectionList !=null){
            for(int i=0;i<inspectionList.size();i++){
                AgriSubLimitDetailsTO agriSubLimitDetailsTO=(AgriSubLimitDetailsTO)inspectionList.get(i);
                singleList=new ArrayList();
                singleMap=new HashMap();
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getPurpose()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getHectare()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSurveryNo()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getRemarks()));
                //                singleList.add(agriInspectionTO.getAreaInspectObservation());
                groupList.add(singleList);
                singleMap.put("SLNO",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSlno()));
                singleMap.put("PURPOSE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getPurpose()));
                singleMap.put("TYPE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getType()));
                singleMap.put("HECTARE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getHectare()));
                singleMap.put("SURVEYNO",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSurveryNo()));
                singleMap.put("REMARKS",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getRemarks()));
                
                singleMap.put("COMMAND","UPDATE");
                finalMap.put(agriSubLimitDetailsTO.getSlno(),singleMap);
                
                
            }
            
            
            
            
            allInspectionMap=finalMap;
            allInspectionList=groupList;
            tempallInspectionMap=finalMap;
            inspectionUtil.setAllValues(allInspectionMap);
            inspectionUtil.setTableValues(allInspectionList);
            //            tblInspectionDetails.setDataArrayList(allInspectionList,setInspectionTableTitle());
            finalMap=null;;
        }
    }
    
    public void setSubLimitTO(ArrayList subLimitList,String act_num){
        LinkedHashMap finalMap=new LinkedHashMap();
        LinkedHashMap finalDetailsMap=new LinkedHashMap();
        HashMap       singleMap=new HashMap();
        HashMap       singleSubLimitMap=new HashMap();
        ArrayList singleList=new ArrayList();
        ArrayList groupList=new ArrayList();
        ArrayList groupListDetails=new ArrayList();
        allSubLimitMap=new LinkedHashMap();
        HashMap subLimitSingleMap=new HashMap();
        
        //        SubLimitUtil=new TableUtil();
        if(subLimitList !=null){
            for(int i=0;i<subLimitList.size();i++){
                TermLoanDisburstTO agriSubLimitTO=(TermLoanDisburstTO)subLimitList.get(i);
                singleList=new ArrayList();
                singleSubLimitMap=new HashMap();
                finalDetailsMap=new  LinkedHashMap();
                groupListDetails=new ArrayList();
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSlno()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstDt()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstStage()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimitAmtRs()));
                singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimitAmt()));
                groupList.add(singleList);
                
                singleSubLimitMap.put("SLNO",CommonUtil.convertObjToStr(agriSubLimitTO.getSlno()));
                singleSubLimitMap.put("DISBURST_DT",CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstDt()));
                singleSubLimitMap.put("DISBURST_STAGE",CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstStage()));
                singleSubLimitMap.put("DISBURST_AMT",CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimitAmtRs()));
                singleSubLimitMap.put("DISBURST_AMT%",CommonUtil.convertObjToStr(agriSubLimitTO.getSubLimitAmt()));
                if(CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstAmt_yes_no()).equals("Y"))
                    singleSubLimitMap.put("RDO_DISBURST_YES", "Y");
                else
                    singleSubLimitMap.put("RDO_DISBURST_YES", "");
                if(CommonUtil.convertObjToStr(agriSubLimitTO.getDisburstAmt_yes_no()).equals("N"))
                    singleSubLimitMap.put("RDO_DISBURST_NO", "N");
                singleSubLimitMap.put("ACCT_NUM", CommonUtil.convertObjToStr(agriSubLimitTO.getAcctNum()));
                singleSubLimitMap.put("REMARKS", CommonUtil.convertObjToStr(agriSubLimitTO.getRemarks()));
                singleSubLimitMap.put("COMMAND","UPDATE");
                singleSubLimitMap.put("FLAG", "TRUE");
                
                
                //sublimit details
//                if(subLimitDetails !=null){
//                    for(int a=0;a<subLimitDetails.size();a++){
//                        AgriSubLimitDetailsTO agriSubLimitDetailsTO=(AgriSubLimitDetailsTO)subLimitDetails.get(a);
//                        singleList=new ArrayList();
//                        singleMap=new HashMap();
//                        if(agriSubLimitTO.getSlno().equals(agriSubLimitDetailsTO.getSubLimitslno())){
//                            
//                            singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSlno()));
//                            singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getPurpose()));
//                            singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getHectare()));
//                            singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSurveryNo()));
//                            singleList.add(CommonUtil.convertObjToStr(agriSubLimitTO.getSlno()));
//                            //                              singleList.add(CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getRemarks()));
//                            //                singleList.add(agriInspectionTO.getAreaInspectObservation());
//                            groupListDetails.add(singleList);
//                            singleMap.put("SLNO",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSlno()));
//                            singleMap.put("MAIN_LIMIT_SLNO",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSubLimitslno()));
//                            singleMap.put("PURPOSE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getPurpose()));
//                            singleMap.put("TYPE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getType()));
//                            singleMap.put("HECTARE",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getHectare()));
//                            singleMap.put("SURVEYNO",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getSurveryNo()));
//                            singleMap.put("REMARKS",CommonUtil.convertObjToStr(agriSubLimitDetailsTO.getRemarks()));
//                            singleMap.put("ACCT_NUM",act_num);
//                            singleMap.put("COMMAND","UPDATE");
//                            finalDetailsMap.put(agriSubLimitDetailsTO.getSlno(),singleMap);
//                            
//                        }
//                    }
//                }
                
                //                singleSubLimitMap.put("SUBLIMIT_MAP",finalDetailsMap);
                //                singleSubLimitMap.put("SUBLIMIT_LIST",groupListDetails);
                //                System.out.println("before insert"+allInspectionMap);
                //                allInspectionMap.putAll(finalDetailsMap);
                //                System.out.println("after insert"+allInspectionMap);
//                singleSubLimitMap.put("SUBLIMIT_DETAILS_MAP",finalDetailsMap);
//                singleSubLimitMap.put("SUBLIMIT_TABLE_LIST",groupListDetails);
                finalMap.put(agriSubLimitTO.getSlno(),singleSubLimitMap);
            }
            System.out.println("finalMap  @@@@@"+finalMap);
            allSubLimitMap=finalMap;
            //            allSubLimitMap.putAll(subLimitSingleMap);
            allSubLimitList=groupList;
            SubLimitUtil.setAllValues(allSubLimitMap);
            SubLimitUtil.setTableValues(allSubLimitList);
            tblSubLimit.setDataArrayList(allSubLimitList,setSubLimitTableTitle());
            getMaxNumber(act_num);
            finalMap=null;
            singleSubLimitMap=null;
            finalDetailsMap=null;
            
        }
    }
    
    public  StringBuffer checkSubLimitDetails(String mainLimit){
        StringBuffer buf=new StringBuffer();
        if(allSubLimitMap !=null){
            double totSubLimit=0;
            double mainLimitAmt=CommonUtil.convertObjToDouble(mainLimit).doubleValue();
            java.util.Set set=allSubLimitMap.keySet();
            java.lang.Object  []objKeySet=(Object[])set.toArray();
            for(int i=0;i<allSubLimitMap.size();i++){
                HashMap singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                String amt=CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT"));
                totSubLimit+=CommonUtil.convertObjToDouble(singleMap.get("SUBLIMIT")).doubleValue();
                String mainslno=CommonUtil.convertObjToStr(singleMap.get("SLNO"));
                LinkedHashMap allDetailMap=(LinkedHashMap)singleMap.get("SUBLIMIT_DETAILS_MAP");
                if(allDetailMap==null)
                    allDetailMap=new LinkedHashMap();
                java.util.Set sets=allDetailMap.keySet();
                java.lang.Object  []objKeySets=(Object[])sets.toArray();
                if(allDetailMap !=null && allDetailMap.size()>0){
                    for(int a=0;a<allDetailMap.size();a++){
                        HashMap singleDetailMap=(HashMap)allDetailMap.get(objKeySets[a]);
                        String detailMainslno=CommonUtil.convertObjToStr(singleDetailMap.get("MAIN_LIMIT_SLNO"));
                        if(mainslno.equals(detailMainslno))
                            break;
                        if(allDetailMap.size()-1 == a)
                            buf.append("Atleast One SubLimit Details Should entered SLNO   :"+mainslno+"\n" +"SUBLIMIT AMT :"+amt+"\n");
                    }
                }else
                    buf.append("Atleast One SubLimit Details Should entered SLNO    :"+mainslno+"\n" +"SUBLIMIT AMT :"+amt+"\n");
            }
            if(mainLimitAmt<totSubLimit)
                buf.append("Main Sanction Limit Should not Less then SubLimit :");
        }
        return buf;
    }
    
    private void getMaxNumber(String act_num){
        if(act_num !=null && act_num.length()>0){
            HashMap singleMap=new HashMap();
            HashMap resultMap=new HashMap();
            singleMap.put("ACT_NUM",act_num);
            java.util.List lst = ClientUtil.executeQuery("getAgriSublimitCount",singleMap);
            maxSubLimitslno=CommonUtil.convertObjToInt(lst.get(0));
            //        maxSubLimitslno=CommonUtil.convertObjToInt(resultMap.get("NO"));
            
            lst =ClientUtil.executeQuery("getAgriSubLimitDetailCount",singleMap);
            //          resultMap=(HashMap)lst.get(0);
            maxSubLimitDetailslno=CommonUtil.convertObjToInt(lst.get(0));
            
        }
    }
    public void setSubLimitDetails(int row){
        ArrayList selectedList=(ArrayList)tblSubLimit.getDataArrayList().get(row);
        System.out.println("allSubLimitMap"+allSubLimitMap);
        java.util.Set keySet=allSubLimitMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allSubLimitMap.size();i++){
            if(((HashMap)allSubLimitMap.get(objKeySet[i])).get("SLNO").equals(selectedList.get(0))){
                HashMap singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                System.out.println("singleMap$$$$$"+singleMap);
            setTdtDisbursementDt(CommonUtil.convertObjToStr(singleMap.get("DISBURST_DT")));
            setCboDisbursementStage(CommonUtil.convertObjToStr(singleMap.get("DISBURST_STAGE")));
            setTxtDisbursementAmtRs(CommonUtil.convertObjToStr(singleMap.get("DISBURST_AMT")));
            setTxtDisbursementAmt(CommonUtil.convertObjToStr(singleMap.get("DISBURST_AMT%")));
            if(CommonUtil.convertObjToStr(singleMap.get("RDO_DISBURST_YES")).equals("Y"))
                setRdoDisbursementAmtRs(true);
            if(CommonUtil.convertObjToStr(singleMap.get("RDO_DISBURST_NO")).equals("N"))
                setRdoDisbursementAmt(true);
            setRemarks(CommonUtil.convertObjToStr(singleMap.get("REMARKS")));
            break;
//setTdtDisbursementDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT")));
//                          
//                setSubLimitFromDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_FROMDT")));
//                setSubLimitToDt(CommonUtil.convertObjToStr(singleMap.get("SUBLIMIT_TODT")));
//                mainSlno=CommonUtil.convertObjToStr(singleMap.get("SLNO"));
//                tempallInspectionMap=(LinkedHashMap)singleMap.get("SUBLIMIT_DETAILS_MAP");
//                //                if(tempallInspectionMap !=null || (!tempallInspectionMap.isEmpty())){
//                allInspectionMap=(LinkedHashMap)singleMap.get("SUBLIMIT_DETAILS_MAP");
//                if(allInspectionMap ==null)
//                    allInspectionMap=new LinkedHashMap();
//                allInspectionList=(ArrayList)singleMap.get("SUBLIMIT_TABLE_LIST");
//                if(allInspectionList ==null)
//                    allInspectionList=new ArrayList();
//                inspectionUtil.setAllValues(allInspectionMap);
//                inspectionUtil.setTableValues(allInspectionList);
////                tblInspectionDetails.setDataArrayList(allInspectionList,setInspectionTableTitle());
//                //                }
//                populateSublimitTable(mainSlno);
//            
            }
        }
        setChanged();
        this.notifyObservers();
    }
    private void populateSublimitTable(String slno){
        int sl=Integer.parseInt(slno);
        java.util.Set keySet=allSubLimitMap.keySet();
        Object objKeySet[] =(Object[])keySet.toArray();
        
        for(int i=0;i<allSubLimitMap.size();i++){
            
            //            String mainLimitslno=CommonUtil.convertObjToStr(eachMap.get("MAIN_LIMIT_SLNO"));
            //             if ((CommonUtil.convertObjToStr(((HashMap) sanctionMainAll.get(objKeySet[j])).get(SANCTION_SL_NO))).equals(String.valueOf(strRecordKey))){
            if((CommonUtil.convertObjToStr(((HashMap)allSubLimitMap.get(objKeySet[i])).get("SLNO")).equals(String.valueOf(slno)))){
                //            if(mainLimitslno.equals(slno)){
                HashMap eachMap=(HashMap)((HashMap)allSubLimitMap.get(objKeySet[i])).get("SUBLIMIT_DETAILS_MAP");
                //                 if( eachMap  !=null && eachMap.size()>0 && CommonUtil.convertObjToStr(((HashMap)eachMap.get(objKeySet[i])).get("MAIN_LIMIT_SLNO")).equals(String.valueOf(slno))){
                //                     ArrayList  val=(ArrayList)((HashMap)allSubLimitMap.get(objKeySet[i])).get("SUBLIMIT_TABLE_LIST");
                
                ArrayList existList=(ArrayList)((HashMap)allSubLimitMap.get(objKeySet[i])).get("SUBLIMIT_TABLE_LIST");
                LinkedHashMap existMap=(LinkedHashMap)((HashMap)allSubLimitMap.get(objKeySet[i])).get("SUBLIMIT_DETAILS_MAP");
                allInspectionMap=existMap;
                allInspectionList=existList;
                //                inspectionUtil.setAllValues(allInspectionMap);
                //                inspectionUtil.setTableValues(allInspectionList);
//                tblInspectionDetails.setDataArrayList(allInspectionList,setInspectionTableTitle());
                break;
            }
//            else
//                tblInspectionDetails.setDataArrayList(new ArrayList(),setInspectionTableTitle());
            //        }
        }
    }
    
    public void setInspectionDetails(int row){
        ArrayList data=(ArrayList)tblInspectionDetails.getDataArrayList().get(row);
        if(allInspectionMap ==null)
            allInspectionMap=new LinkedHashMap();
        java.util.Set keySet=allInspectionMap.keySet();
        Object objKeySet[]=(Object[])keySet.toArray();
        for(int i=0;i<allInspectionMap.size();i++){
            if(((HashMap)allInspectionMap.get(objKeySet[i])).get("SLNO").equals(data.get(0))){
                HashMap singleMap=(HashMap)allInspectionMap.get(objKeySet[i]);
                System.out.println("singleMap   #####"+singleMap);
                setCboType(CommonUtil.convertObjToStr(singleMap.get("TYPE")));
                setCbmProdId(CommonUtil.convertObjToStr(singleMap.get("PURPOSE")));
                setCboPurpose(CommonUtil.convertObjToStr(singleMap.get("PURPOSE")));
                //                setDateOfInspection(CommonUtil.convertObjToStr(singleMap.get("DATE_OF_INSPECTION")));
                setHectare(CommonUtil.convertObjToStr(singleMap.get("HECTARE")));
                setSurveyno(CommonUtil.convertObjToStr(singleMap.get("SURVEYNO")));
                setRemarks(CommonUtil.convertObjToStr(singleMap.get("REMARKS")));
                break;
            }
        }
        setChanged();
        this.notifyObservers();
    }
    public void resetFormComponets(){
        setCboPurpose("");
        setCboType("");
        setHectare("");
        setSurveyno("");
        setRemarks("");
        setChanged();
        this.notifyObservers();
    }
    public void resetFormComponetsSubLimit(){
        setTdtDisbursementDt("");
        setCboDisbursementStage("");
        setTxtDisbursementAmt("");
        setTxtDisbursementAmtRs("");
        setRdoDisbursementAmtRs(false);
        setRdoDisbursementAmt(false);
        setChanged();
        this.notifyObservers();
    }
    public void resetTable(){
//        tblInspectionDetails.setDataArrayList(null,setInspectionTableTitle());
        tblSubLimit.setDataArrayList(null,setSubLimitTableTitle());
        SubLimitUtil= new TableUtil();
        SubLimitUtil.setAttributeKey("SLNO");
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
        setTblSubLimitSelected(false);
        maxSubLimitslno=0;
        
    }
    /* calling from doaction perform
     */
    public HashMap  setAgriSubLimitTo(){
        TermLoanDisburstTO agriSubLimitTO=new TermLoanDisburstTO();
//        AgriSubLimitDetailsTO agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
        HashMap resultSubLimitMap=new HashMap();
        resultSubLimitDetailsMap=new HashMap();
        ArrayList removedValues=new ArrayList();
        removedValues=SubLimitUtil.getRemovedValues();
        int key=0;
        if(allSubLimitMap !=null){
            System.out.println("allSubLimitMap$$$$"+allSubLimitMap);
            java.util.Set keySet=allSubLimitMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            
            for(int i=0;i<allSubLimitMap.size();i++){
                HashMap singleMap=(HashMap)allSubLimitMap.get(objKeySet[i]);
                agriSubLimitTO=new TermLoanDisburstTO();
                agriSubLimitTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriSubLimitTO.setDisburstDt(DateUtil.getDateMMDDYYYY((String)singleMap.get("DISBURST_DT")));
                
                agriSubLimitTO.setDisburstStage(CommonUtil.convertObjToStr(singleMap.get("DISBURST_STAGE")));
                agriSubLimitTO.setSubLimitAmtRs(CommonUtil.convertObjToDouble(singleMap.get("DISBURST_AMT")));
                agriSubLimitTO.setSubLimitAmt(CommonUtil.convertObjToDouble(singleMap.get("DISBURST_AMT%")));
                agriSubLimitTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriSubLimitTO.setRemarks(CommonUtil.convertObjToStr(singleMap.get("REMARKS")));
                if(CommonUtil.convertObjToStr(singleMap.get("RDO_DISBURST_YES")).equals("Y"))
                    agriSubLimitTO.setDisburstAmt_yes_no("Y");
               if(CommonUtil.convertObjToStr(singleMap.get("RDO_DISBURST_NO")).equals("N"))
                    agriSubLimitTO.setDisburstAmt_yes_no("N");
                agriSubLimitTO.setCommand(CommonUtil.convertObjToStr( singleMap.get("COMMAND")));
                if (singleMap.get("COMMAND").equals("INSERT")){
                    agriSubLimitTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (singleMap.get("COMMAND").equals("UPDATE")){
                    agriSubLimitTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                resultSubLimitMap.put(agriSubLimitTO.getSlno(),agriSubLimitTO);
                
                //sublimit details
//                LinkedHashMap finalMap=(LinkedHashMap)singleMap.get("SUBLIMIT_DETAILS_MAP");
//                if(finalMap !=null && finalMap.size()>0){
//                    java.util.Set detailskeySet=finalMap.keySet();
//                    HashMap oneMap=null;
//                    Object objDetailsKeySet[]=(Object[])detailskeySet.toArray();
//                    for(int a=0;a<finalMap.size();a++){
//                        oneMap=(HashMap)finalMap.get(objDetailsKeySet[a]);
//                        agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
//                        agriSubLimitDetailsTO.setAcctNum(CommonUtil.convertObjToStr(oneMap.get("ACCT_NUM")));
//                        agriSubLimitDetailsTO.setPurpose(CommonUtil.convertObjToStr(oneMap.get("PURPOSE")));
//                        agriSubLimitDetailsTO.setHectare(CommonUtil.convertObjToStr(oneMap.get("HECTARE")));
//                        agriSubLimitDetailsTO.setSurveryNo(CommonUtil.convertObjToStr(oneMap.get("SURVEYNO")));
//                        agriSubLimitDetailsTO.setSubLimitslno(CommonUtil.convertObjToStr(oneMap.get("MAIN_LIMIT_SLNO")));
//                        agriSubLimitDetailsTO.setSlno(CommonUtil.convertObjToStr(oneMap.get("SLNO")));
//                        agriSubLimitDetailsTO.setType(CommonUtil.convertObjToStr(oneMap.get("TYPE")));
//                        agriSubLimitDetailsTO.setRemarks(CommonUtil.convertObjToStr(oneMap.get("REMARKS")));
//                        agriSubLimitDetailsTO.setCommand(CommonUtil.convertObjToStr(oneMap.get("COMMAND")));
//                        if (oneMap.get("COMMAND").equals("INSERT")){
//                            agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
//                        }else if (oneMap.get("COMMAND").equals("UPDATE")){
//                            agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                        }
//                        resultSubLimitDetailsMap.put(String.valueOf(key),agriSubLimitDetailsTO);
//                        key++;
//                    }
//                }
                //
            }
            
            HashMap deletedMap=null;
            //delete record also we shoude update  sublimit
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    deletedMap=(HashMap)removedValues.get(i);
                    agriSubLimitTO=new TermLoanDisburstTO();
                    agriSubLimitTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    agriSubLimitTO.setDisburstDt(DateUtil.getDateMMDDYYYY((String)deletedMap.get("DISBURST_DT")));
                    agriSubLimitTO.setDisburstStage(CommonUtil.convertObjToStr(deletedMap.get("DISBURST_STAGE")));
                    agriSubLimitTO.setSubLimitAmtRs(CommonUtil.convertObjToDouble(deletedMap.get("DISBURST_AMT")));
                    agriSubLimitTO.setSubLimitAmt(CommonUtil.convertObjToDouble(deletedMap.get("DISBURST_AMT%")));
                    agriSubLimitTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
                    agriSubLimitTO.setCommand(CommonUtil.convertObjToStr( deletedMap.get("COMMAND")));
                    agriSubLimitTO.setStatus(CommonConstants.STATUS_DELETED);
                    resultSubLimitMap.put(agriSubLimitTO.getSlno(),agriSubLimitTO);
                }
            deletedMap=null;
            //delete record also we shoude update  sublimit details
//            removedValues =inspectionUtil.getRemovedValues();
//            if(removedValues !=null)
//                for(int i=0;i<removedValues.size();i++){
//                    deletedMap=(HashMap)removedValues.get(i);
//                    agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
//                    agriSubLimitDetailsTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
//                    agriSubLimitDetailsTO.setPurpose(CommonUtil.convertObjToStr(deletedMap.get("PURPOSE")));
//                    agriSubLimitDetailsTO.setHectare(CommonUtil.convertObjToStr(deletedMap.get("HECTARE")));
//                    agriSubLimitDetailsTO.setSurveryNo(CommonUtil.convertObjToStr(deletedMap.get("SURVEYNO")));
//                    agriSubLimitDetailsTO.setSubLimitslno(CommonUtil.convertObjToStr(deletedMap.get("MAIN_LIMIT_SLNO")));
//                    agriSubLimitDetailsTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
//                    agriSubLimitDetailsTO.setType(CommonUtil.convertObjToStr(deletedMap.get("TYPE")));
//                    agriSubLimitDetailsTO.setRemarks(CommonUtil.convertObjToStr(deletedMap.get("REMARKS")));
//                    agriSubLimitDetailsTO.setCommand(CommonUtil.convertObjToStr(deletedMap.get("COMMAND")));
//                    agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//                    resultSubLimitDetailsMap.put(String.valueOf(key),agriSubLimitDetailsTO);
//                    key++;
//                }
            
        }
        return resultSubLimitMap;
        
    }
    public HashMap  setAgriSubLimitDetailsTO(){
        AgriSubLimitDetailsTO agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
        HashMap resultInspectionMap=new HashMap();
        ArrayList removedValues=inspectionUtil.getRemovedValues();
        allInspectionMap = inspectionUtil.getAllValues();
        if(allInspectionMap !=null){
            System.out.println("allSubLimitMap$$$$"+allInspectionMap);
            java.util.Set keySet=allInspectionMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
            
            for(int i=0;i<allInspectionMap.size();i++){
                HashMap singleMap=(HashMap)allInspectionMap.get(objKeySet[i]);
                agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
                agriSubLimitDetailsTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
                agriSubLimitDetailsTO.setPurpose(CommonUtil.convertObjToStr(singleMap.get("PURPOSE")));
                agriSubLimitDetailsTO.setHectare(CommonUtil.convertObjToStr(singleMap.get("HECTARE")));
                
                agriSubLimitDetailsTO.setType(CommonUtil.convertObjToStr(singleMap.get("TYPE")));
                agriSubLimitDetailsTO.setSurveryNo(CommonUtil.convertObjToStr(singleMap.get("SURVEYNO")));
                agriSubLimitDetailsTO.setSubLimitslno(CommonUtil.convertObjToStr(singleMap.get("MAIN_LIMIT_SLNO")));
                agriSubLimitDetailsTO.setRemarks(CommonUtil.convertObjToStr(singleMap.get("REMARKS")));
                agriSubLimitDetailsTO.setAcctNum(CommonUtil.convertObjToStr(singleMap.get("ACCT_NUM")));
                agriSubLimitDetailsTO.setCommand(CommonUtil.convertObjToStr(singleMap.get("COMMAND")));
                if (singleMap.get("COMMAND").equals("INSERT")){
                    agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (singleMap.get("COMMAND").equals("UPDATE")){
                    agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                resultInspectionMap.put(agriSubLimitDetailsTO.getSlno(),agriSubLimitDetailsTO);
            }
            HashMap deletedMap=null;
            //delete record also we shoude update
            if(removedValues !=null)
                for(int i=0;i<removedValues.size();i++){
                    
                    deletedMap=(HashMap)removedValues.get(i);
                    agriSubLimitDetailsTO=new AgriSubLimitDetailsTO();
                    agriSubLimitDetailsTO.setSlno(CommonUtil.convertObjToStr(deletedMap.get("SLNO")));
                    agriSubLimitDetailsTO.setPurpose(CommonUtil.convertObjToStr(deletedMap.get("PURPOSE")));
                    agriSubLimitDetailsTO.setHectare(CommonUtil.convertObjToStr(deletedMap.get("HECTARE")));
                    agriSubLimitDetailsTO.setSubLimitslno(CommonUtil.convertObjToStr(deletedMap.get("MAIN_LIMIT_SLNO")));
                    agriSubLimitDetailsTO.setType(CommonUtil.convertObjToStr(deletedMap.get("TYPE")));
                    agriSubLimitDetailsTO.setSurveryNo(CommonUtil.convertObjToStr(deletedMap.get("SURVEYNO")));
                    agriSubLimitDetailsTO.setRemarks(CommonUtil.convertObjToStr(deletedMap.get("REMARKS")));
                    agriSubLimitDetailsTO.setAcctNum(CommonUtil.convertObjToStr(deletedMap.get("ACCT_NUM")));
                    agriSubLimitDetailsTO.setCommand(CommonUtil.convertObjToStr(deletedMap.get("COMMAND")));
                    agriSubLimitDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                    resultInspectionMap.put(agriSubLimitDetailsTO.getSlno(),agriSubLimitDetailsTO);
                }
            
        }
        return resultInspectionMap;
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
     * Getter for property cbmPurpose.
     * @return Value of property cbmPurpose.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPurpose() {
        return cbmPurpose;
    }
    
    /**
     * Setter for property cbmPurpose.
     * @param cbmPurpose New value of property cbmPurpose.
     */
    public void setCbmPurpose(com.see.truetransact.clientutil.ComboBoxModel cbmPurpose) {
        this.cbmPurpose = cbmPurpose;
    }
    
    /**
     * Getter for property cbmType.
     * @return Value of property cbmType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmType() {
        return cbmType;
    }
    
    /**
     * Setter for property cbmType.
     * @param cbmType New value of property cbmType.
     */
    public void setCbmType(com.see.truetransact.clientutil.ComboBoxModel cbmType) {
        this.cbmType = cbmType;
    }
    
    /**
     * Getter for property cboType.
     * @return Value of property cboType.
     */
    public java.lang.String getCboType() {
        return cboType;
    }
    
    /**
     * Setter for property cboType.
     * @param cboType New value of property cboType.
     */
    public void setCboType(java.lang.String cboType) {
        this.cboType = cboType;
    }
    
    /**
     * Getter for property cboPurpose.
     * @return Value of property cboPurpose.
     */
    public java.lang.String getCboPurpose() {
        return cboPurpose;
    }
    
    /**
     * Setter for property cboPurpose.
     * @param cboPurpose New value of property cboPurpose.
     */
    public void setCboPurpose(java.lang.String cboPurpose) {
        this.cboPurpose = cboPurpose;
    }
    
    /**
     * Getter for property hectare.
     * @return Value of property hectare.
     */
    public java.lang.String getHectare() {
        return hectare;
    }
    
    /**
     * Setter for property hectare.
     * @param hectare New value of property hectare.
     */
    public void setHectare(java.lang.String hectare) {
        this.hectare = hectare;
    }
    
    /**
     * Getter for property surveyno.
     * @return Value of property surveyno.
     */
    public java.lang.String getSurveyno() {
        return surveyno;
    }
    
    /**
     * Setter for property surveyno.
     * @param surveyno New value of property surveyno.
     */
    public void setSurveyno(java.lang.String surveyno) {
        this.surveyno = surveyno;
    }
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Getter for property resultSubLimitDetailsMap.
     * @return Value of property resultSubLimitDetailsMap.
     */
    public java.util.HashMap getResultSubLimitDetailsMap() {
        return resultSubLimitDetailsMap;
    }
    
    /**
     * Setter for property resultSubLimitDetailsMap.
     * @param resultSubLimitDetailsMap New value of property resultSubLimitDetailsMap.
     */
    public void setResultSubLimitDetailsMap(java.util.HashMap resultSubLimitDetailsMap) {
        this.resultSubLimitDetailsMap = resultSubLimitDetailsMap;
    }
    
    /**
     * Getter for property tblSubLimitSelected.
     * @return Value of property tblSubLimitSelected.
     */
    public boolean isTblSubLimitSelected() {
        return tblSubLimitSelected;
    }
    
    /**
     * Setter for property tblSubLimitSelected.
     * @param tblSubLimitSelected New value of property tblSubLimitSelected.
     */
    public void setTblSubLimitSelected(boolean tblSubLimitSelected) {
        this.tblSubLimitSelected = tblSubLimitSelected;
    }
    
    /**
     * Getter for property selectedSubLimitRow.
     * @return Value of property selectedSubLimitRow.
     */
    public int getSelectedSubLimitRow() {
        return selectedSubLimitRow;
    }
    
    /**
     * Setter for property selectedSubLimitRow.
     * @param selectedSubLimitRow New value of property selectedSubLimitRow.
     */
    public void setSelectedSubLimitRow(int selectedSubLimitRow) {
        this.selectedSubLimitRow = selectedSubLimitRow;
    }
    
    /**
     * Getter for property mainSlno.
     * @return Value of property mainSlno.
     */
    public java.lang.String getMainSlno() {
        return mainSlno;
    }
    
    /**
     * Setter for property mainSlno.
     * @param mainSlno New value of property mainSlno.
     */
    public void setMainSlno(java.lang.String mainSlno) {
        this.mainSlno = mainSlno;
    }
    
    /**
     * Getter for property mainSanctionDt.
     * @return Value of property mainSanctionDt.
     */
    public java.lang.String getMainSanctionDt() {
        return mainSanctionDt;
    }
    
    /**
     * Setter for property mainSanctionDt.
     * @param mainSanctionDt New value of property mainSanctionDt.
     */
    public void setMainSanctionDt(java.lang.String mainSanctionDt) {
        this.mainSanctionDt = mainSanctionDt;
    }
    
    /**
     * Getter for property loanExpiryDate.
     * @return Value of property loanExpiryDate.
     */
    public java.lang.String getLoanExpiryDate() {
        return loanExpiryDate;
    }
    
    /**
     * Setter for property loanExpiryDate.
     * @param loanExpiryDate New value of property loanExpiryDate.
     */
    public void setLoanExpiryDate(java.lang.String loanExpiryDate) {
        this.loanExpiryDate = loanExpiryDate;
    }
    
    /**
     * Getter for property loanOpenDt.
     * @return Value of property loanOpenDt.
     */
    public java.lang.String getLoanOpenDt() {
        return loanOpenDt;
    }
    
    /**
     * Setter for property loanOpenDt.
     * @param loanOpenDt New value of property loanOpenDt.
     */
    public void setLoanOpenDt(java.lang.String loanOpenDt) {
        this.loanOpenDt = loanOpenDt;
    }
    
    /**
     * Getter for property tdtDisbursementDt.
     * @return Value of property tdtDisbursementDt.
     */
    public java.lang.String getTdtDisbursementDt() {
        return tdtDisbursementDt;
    }
    
    /**
     * Setter for property tdtDisbursementDt.
     * @param tdtDisbursementDt New value of property tdtDisbursementDt.
     */
    public void setTdtDisbursementDt(java.lang.String tdtDisbursementDt) {
        this.tdtDisbursementDt = tdtDisbursementDt;
    }
    
    /**
     * Getter for property cboDisbursementStage.
     * @return Value of property cboDisbursementStage.
     */
    public java.lang.String getCboDisbursementStage() {
        return cboDisbursementStage;
    }
    
    /**
     * Setter for property cboDisbursementStage.
     * @param cboDisbursementStage New value of property cboDisbursementStage.
     */
    public void setCboDisbursementStage(java.lang.String cboDisbursementStage) {
        this.cboDisbursementStage = cboDisbursementStage;
    }
    
    /**
     * Getter for property rdoDisbursementAmtRs.
     * @return Value of property rdoDisbursementAmtRs.
     */
    public boolean isRdoDisbursementAmtRs() {
        return rdoDisbursementAmtRs;
    }
    
    /**
     * Setter for property rdoDisbursementAmtRs.
     * @param rdoDisbursementAmtRs New value of property rdoDisbursementAmtRs.
     */
    public void setRdoDisbursementAmtRs(boolean rdoDisbursementAmtRs) {
        this.rdoDisbursementAmtRs = rdoDisbursementAmtRs;
    }
    
    /**
     * Getter for property rdoDisbursementAmt.
     * @return Value of property rdoDisbursementAmt.
     */
    public boolean isRdoDisbursementAmt() {
        return rdoDisbursementAmt;
    }
    
    /**
     * Setter for property rdoDisbursementAmt.
     * @param rdoDisbursementAmt New value of property rdoDisbursementAmt.
     */
    public void setRdoDisbursementAmt(boolean rdoDisbursementAmt) {
        this.rdoDisbursementAmt = rdoDisbursementAmt;
    }
    
    /**
     * Getter for property txtDisbursementAmtRs.
     * @return Value of property txtDisbursementAmtRs.
     */
    public java.lang.String getTxtDisbursementAmtRs() {
        return txtDisbursementAmtRs;
    }
    
    /**
     * Setter for property txtDisbursementAmtRs.
     * @param txtDisbursementAmtRs New value of property txtDisbursementAmtRs.
     */
    public void setTxtDisbursementAmtRs(java.lang.String txtDisbursementAmtRs) {
        this.txtDisbursementAmtRs = txtDisbursementAmtRs;
    }
    
    /**
     * Getter for property txtDisbursementAmt.
     * @return Value of property txtDisbursementAmt.
     */
    public java.lang.String getTxtDisbursementAmt() {
        return txtDisbursementAmt;
    }
    
    /**
     * Setter for property txtDisbursementAmt.
     * @param txtDisbursementAmt New value of property txtDisbursementAmt.
     */
    public void setTxtDisbursementAmt(java.lang.String txtDisbursementAmt) {
        this.txtDisbursementAmt = txtDisbursementAmt;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
}
