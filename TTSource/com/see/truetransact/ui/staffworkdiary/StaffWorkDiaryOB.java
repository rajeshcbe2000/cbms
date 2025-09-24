/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.staffworkdiary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.staffworkdiary.StaffWorkDiaryTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author  
 *
 */
public class StaffWorkDiaryOB extends CObservable {

    private String txtStaffId = "";
    private String txtTransSummry = "";
    private String txaremarks = "";
    private String tdtDate = "";
    private String txtLoginout="";
   private String txtGeneralBoadyID="";
    
  private static SqlMap sqlMap = null;
  private final static Logger log = Logger.getLogger(StaffWorkDiaryOB.class);    
  private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
     private static StaffWorkDiaryOB objStaffWorkDiaryOB;
 private HashMap map;
 private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];


    
////    private String noOfTrainees = "";
////    final ArrayList tableTitle = new ArrayList();
////    private ArrayList IncVal = new ArrayList();
////    private EnhancedTableModel tblEmpDetails;
//    private static SqlMap sqlMap = null;
//    private boolean newData = false;
//    private LinkedHashMap incParMap;
//    private LinkedHashMap deletedTableMap;
//    private String txtSlNo="";
//    private String subj="";
//    
//    private final static Logger log = Logger.getLogger(GeneralBodyDetailsOB.class);
//    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private HashMap map;
//    private ProxyFactory proxy;
//    //for filling dropdown
//    private HashMap lookupMap;
//    private HashMap param;
//    private HashMap keyValue;
//    private ArrayList key;
//    private ArrayList value;
//    private int actionType;
  private StaffWorkDiaryOB staffWorkDiaryOB;
    StaffWorkDiaryRB objStaffWorkDiaryRB = new StaffWorkDiaryRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private StaffWorkDiaryTO objStaffWorkDiaryTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public StaffWorkDiaryOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "StaffWorkDiaryJNDI");
            map.put(CommonConstants.HOME, "staffworkdiary.StaffWorkDiaryHome");
            map.put(CommonConstants.REMOTE, "staffworkdiary.StaffWorkDiary");
//            setTableTile();
//            tblEmpDetails = new EnhancedTableModel(null, tableTitle);
//            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
   
    
     static {
        try {
            log.info("Creating ParameterOB...");
            objStaffWorkDiaryOB = new StaffWorkDiaryOB();
        } catch(Exception e) {
           // parseException.logException(e,true);
               System.out.println("Error in static():"+e);
        }
    }
    
      public java.lang.String getTxtGeneralBoadyID() {
        return txtGeneralBoadyID;
    }
    
   
  public void setTxtGeneralBoadyID(java.lang.String txtGeneralBoadyID) {
      this.txtGeneralBoadyID = txtGeneralBoadyID;
   }
     
     
     
     
     public java.lang.String gettdtDate() {
        return tdtDate;
    }
    
   
    public void settdtDate(java.lang.String tdtDate) {
        this.tdtDate = tdtDate;
    }

    
     public java.lang.String getTxtStaffId() {
        return txtStaffId;
    }
    
   
    public void setTxtStaffId(java.lang.String txtStaffId) {
        this.txtStaffId = txtStaffId;
    }
    
    
     public java.lang.String getTxtLoginout() {
        return txtLoginout;
    }
    
   
    public void setTxtLoginout(java.lang.String txtLoginout) {
        this.txtLoginout = txtLoginout;
    }
    
     public java.lang.String getTxtTransSummry() {
        return txtTransSummry;
    }
    
   
    public void setTxtTransSummry(java.lang.String txtTransSummry) {
        this.txtTransSummry = txtTransSummry;
    }
    
     public java.lang.String getTxtremarks() {
        return txaremarks;
    }
    
    
    public void setTxtremarks(java.lang.String txtremarks) {
        this.txaremarks = txtremarks;
    }
  
    
    
    
    
     
     
     public void execute(String command) {
        try {
         //   System.out.println("GET BOPRRR NO IN EDIT :="+geBorrowingTO(command));
           
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("StaffWorkDiaryTO", getStaffWorkDiaryTO(command));
             System.out.println("GET term IN EDIT :="+term);
              System.out.println("GET map IN EDIT :="+map);
            HashMap proxyReturnMap = proxy.execute(term, map);
//            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            System.out.println("ACTIONN TYPEEE==="+getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
              System.out.println("Error in execute():"+e);
        }
    }
     
     
     
     private StaffWorkDiaryTO getStaffWorkDiaryTO(String command){
        StaffWorkDiaryTO objTO = new StaffWorkDiaryTO();
        objTO.setCommand(command);
        objTO.setGbid(getTxtGeneralBoadyID());
        objTO.setgDate(DateUtil.getDateMMDDYYYY(gettdtDate()));
        objTO.setStaffId(getTxtStaffId());
        objTO.setLoginout(getTxtLoginout());
        System.out.println("TOOO"+objTO.getLoginout()+"OOOBB"+getTxtLoginout());
        objTO.setTransSummry(Integer.parseInt(getTxtTransSummry()));
        objTO.setRemarks(getTxtremarks());
        
       if(command.equals(CommonConstants.TOSTATUS_INSERT)){
//                objTO.setAuthorizeStatus("");
//                objTO.setAuthorizeBy("");
//                objTO.setAuthorizeDte(null);
                 objTO.setStatus("CREATED");
        }
     
        return objTO;
    }
     
     
     public void setStaffWorkDiaryTO(StaffWorkDiaryTO objTO) {
        setTxtGeneralBoadyID(objTO.getGbid());
        settdtDate(objTO.getgDate().toString());
        setTxtStaffId(objTO.getStaffId());
        int k=objTO.getTransSummry();
        Integer K=k;
        setTxtTransSummry(K.toString());
        setTxtremarks(objTO.getRemarks());
        setTxtLoginout(objTO.getLoginout());
//            objGeneralBodyDetailsTO.setgDate(gettdtDate());
//            objGeneralBodyDetailsTO.setVenu(getTxtVenu());
//            objGeneralBodyDetailsTO.setTotalAttendance(getTxttotalAttendance());
//            objGeneralBodyDetailsTO.setRemarks(getTxtremarks());
//    //
//            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
//              objEmpTrainingTO.setEmpTrainingID(getTxtEmpTrainingID());
//            }
       
        
         notifyObservers();
    }
    
    
   public void resetForm(){
        settdtDate(null);
        setTxtStaffId("");
        setTxtLoginout("");
        setTxtTransSummry("");
        setTxtremarks("");

    } 
    
    
     public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    
    
    
     public static StaffWorkDiaryOB getInstance()throws Exception{
        return objStaffWorkDiaryOB;
    }
      public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
      //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
    public void setStatus(){
       // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
     private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
     
     public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
       public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
      /** To update the Status based on result performed by doAction() method */
   
       public int getResult(){
        return _result;
    }
       
       
        public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
             System.out.println("whereMap=="+whereMap);
             System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+((List) mapData.get("StaffWorkDiaryTO")).get(0));
           StaffWorkDiaryTO objTO =(StaffWorkDiaryTO) ((List) mapData.get("StaffWorkDiaryTO")).get(0);
           System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setStaffWorkDiaryTO(objTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           // parseException.logException(e,true);
               System.out.println("Error in populateData():"+e);
            
        }
    }
}
    
     
      
//    private void setTableTile() throws Exception{
//        tableTitle.add("SL No");
//        tableTitle.add("Emp Name");
//        tableTitle.add("Emp ID");
//        IncVal = new ArrayList();
//    }
   
    
    /** To fill the comboboxes */
//    private void fillDropdown() throws Exception{
////       lookupMap = new HashMap();
////            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
////            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
////            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
////            log.info("Inside FillDropDown");
////            HashMap param = new HashMap();
////            param.put(CommonConstants.MAP_NAME, null);
////            final ArrayList lookup_keys = new ArrayList();
////            lookup_keys.add("EMP_TRAINING_DESTINATION");
////            param.put(CommonConstants.PARAMFORQUERY, lookup_keys);
////            keyValue = ClientUtil.populateLookupData(param);
////            fillData((HashMap)keyValue.get("EMP_TRAINING_DESTINATION"));
////            cbmTrainingDest = new ComboBoxModel(key,value);
////     
//    }
//    
//     private void fillData(HashMap keyValue)  throws Exception{
//        key = (ArrayList)keyValue.get("KEY");
//        value = (ArrayList)keyValue.get("VALUE");
//    }
//    
//    /** To get data for comboboxes */
//    public HashMap populateData(HashMap obj)  throws Exception{
//        keyValue = proxy.executeQuery(obj,lookupMap);
//        log.info("Got HashMap");
//        return keyValue;
//   }
//    
//  
//   
//  
//    public void ttNotifyObservers(){
//        notifyObservers();
//    }
//    
//    /** To perform the necessary operation */
//    public void doAction() {
//        try {
//          
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
//                doActionPerform();
//            }
//        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            parseException.logException(e,true);
//        }
//    }
//    
//        
//    /** To perform the necessary action */
//    private void doActionPerform() throws Exception{
//        final GeneralBodyDetailsTO objGeneralBodyDetailsTO = new GeneralBodyDetailsTO();
//        final HashMap data = new HashMap();
//         data.put("COMMAND",getCommand());
//        if(get_authorizeMap() == null){
//         data.put("EmpTraining",setEmpTranferData());
//         data.put("EmpTableDetails",incParMap);
//         data.put("deletedEmpTableDetails",deletedTableMap);
//        }
//        else{
//        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
//        data.put("EmpTableDetails",incParMap);
//        }
//        System.out.println("data in EmpTransfer OB : " + data);
//        HashMap proxyResultMap = proxy.execute(data, map);
//        setProxyReturnMap(proxyResultMap);
//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
//                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("EMP_TRAINING_ID")));
//            }
//        setResult(getActionType());
//        setResult(actionType);
//    }
//    
//    private String getCommand(){
//        String command = null;
//        System.out.println("actionType : " + actionType);
//        switch (actionType) {
//            case ClientConstants.ACTIONTYPE_NEW:
//                command = CommonConstants.TOSTATUS_INSERT;
//                break;
//            case ClientConstants.ACTIONTYPE_EDIT:
//                command = CommonConstants.TOSTATUS_UPDATE;
//                break;
//            case ClientConstants.ACTIONTYPE_DELETE:
//                command = CommonConstants.TOSTATUS_DELETE;
//                break;
//            case ClientConstants.ACTIONTYPE_AUTHORIZE:
//                command = CommonConstants.STATUS_AUTHORIZED;
//                break;
//            case ClientConstants.ACTIONTYPE_REJECT:
//                command = CommonConstants.STATUS_REJECTED;
//                break;
//            case ClientConstants.ACTIONTYPE_EXCEPTION:
//                command = CommonConstants.STATUS_EXCEPTION;
//            default:
//        }
//        // System.out.println("command : " + command);
//        return command;
//    }
//    
//    private String getAction(){
//       String action = null;
//        // System.out.println("actionType : " + actionType);
//        switch (actionType) {
//            case ClientConstants.ACTIONTYPE_NEW:
//                action = CommonConstants.STATUS_CREATED;
//                break;
//            case ClientConstants.ACTIONTYPE_EDIT:
//                action = CommonConstants.STATUS_MODIFIED;
//                break;
//            case ClientConstants.ACTIONTYPE_DELETE:
//                action = CommonConstants.STATUS_DELETED;
//                break;
//            case ClientConstants.ACTIONTYPE_AUTHORIZE:
//                action = CommonConstants.STATUS_AUTHORIZED;
//                break;
//            case ClientConstants.ACTIONTYPE_REJECT:
//                action = CommonConstants.STATUS_REJECTED;
//                break;
//            case ClientConstants.ACTIONTYPE_EXCEPTION:
//                action = CommonConstants.STATUS_EXCEPTION;
//                break;
//            default:
//        }
//        // System.out.println("command : " + command);
//        return action;
//    }
//    
//   
//    
//    /** To retrieve a particular customer's accountclosing record */
//    public void getData(HashMap whereMap) {
////        try{
////            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
////            objEmpTrainingTO = (EmpTrainingTO) ((List) data.get("EmpTrainingTO")).get(0);
////            populateEmpTrainingData(objEmpTrainingTO);
////            if(data.containsKey("EmpDetailsTO")){
////                incParMap = (LinkedHashMap)data.get("EmpDetailsTO");
////                ArrayList addList =new ArrayList(incParMap.keySet());
////                for(int i=0;i<addList.size();i++){
////                    objEmpTrainingTO = (EmpTrainingTO)  incParMap.get(addList.get(i));
////                    ArrayList incTabRow = new ArrayList();
////                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getSlNo()));
////                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
////                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
////                    tblEmpDetails.addRow(incTabRow);
////                }
////            }
////            ttNotifyObservers();
////        }catch(Exception e){
////            parseException.logException(e,true);
////        }
//    }
//    
//    /** To populate data into the screen */
//   
//    
//    private void populateEmpTrainingData(GeneralBodyDetailsTO objGeneralBodyDetailsTO) throws Exception{
//        this.settdtDate(CommonUtil.convertObjToStr(objGeneralBodyDetailsTO.getgDate()));
//        this.setTxtVenu(CommonUtil.convertObjToStr( objGeneralBodyDetailsTO.getVenu()));
//        this.setTxttotalAttendance(CommonUtil.convertObjToStr( objGeneralBodyDetailsTO. getTotalAttendance()));
//        this.setTxtremarks(CommonUtil.convertObjToStr( objGeneralBodyDetailsTO. getRemarks()));
//        
////        this.setTxtEmpID(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
////        this.setTxtStatusBy(CommonUtil.convertObjToStr(objEmpTrainingTO.getStatusBy()));
////        getCbmTrainingDest().setKeyForSelected(CommonUtil.convertObjToStr(objEmpTrainingTO.getDestination()));
////
////        this.setCreatedDt(CommonUtil.convertObjToStr(objEmpTrainingTO.getCreatedDt()));
////        this.setLocation(CommonUtil.convertObjToStr(objEmpTrainingTO.getLocation()));
////        this.setTeam(CommonUtil.convertObjToStr(objEmpTrainingTO.getTeam()));
////        this.setTeamSize(CommonUtil.convertObjToStr(objEmpTrainingTO.getTeamSize()));
////        this.setTrainingFrom(CommonUtil.convertObjToStr(objEmpTrainingTO.getTrainingFrom()));
////        this.setTrainingTo(CommonUtil.convertObjToStr(objEmpTrainingTO.getTrainingTo()));
////        this.setStatusBy(CommonUtil.convertObjToStr(objEmpTrainingTO.getStatusBy()));
////        this.setNoOfTrainees(CommonUtil.convertObjToStr(objEmpTrainingTO.getNoOfEmp()));
////        this.setSubj(CommonUtil.convertObjToStr(objEmpTrainingTO.getSubj()));
//        setChanged();
//        notifyObservers();
//    }
//    
//    
//    public void resetForm(){
//        setTdtDate(null);
//        setTxtVenu("");
//        setTxttotalAttendance("");
//        setTxtremarks("");
////       setCboTrainingDest("");
////       cboTrainingDest="";
////       setTxtEmpID("");
////       setTxtEmpTrainingID("");
////       setTeam("");
////       setTeamSize("");
////       setTrainingFrom("");
////       setTrainingTo("");
////       setEmpName("");
////       tblEmpDetails.setDataArrayList(null,tableTitle);
////       setNoOfTrainees("");
////       setLocation("");
////       setSubj("");
////       setChanged();
////       ttNotifyObservers();
//    }
//    public void resetEmpDetails() {
////        setTxtEmpID("");
////        setChanged();
////       ttNotifyObservers(); 
//    }
//    
//      public void deleteTableData(String val, int row){
////        if(deletedTableMap == null){
////            deletedTableMap = new LinkedHashMap();
////        }
////        EmpTrainingTO objEmpTrainingTO = (EmpTrainingTO) incParMap.get(val);
////        objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_DELETED);
////        deletedTableMap.put(CommonUtil.convertObjToStr(tblEmpDetails.getValueAt(row,0)),incParMap.get(val));
////        Object obj;
////        obj=val;
////        incParMap.remove(val);
////        resetTableValues();
////        try{
////             populateTable();
////        }
////        catch(Exception e){
////            parseException.logException(e,true);
////        }
//    }
//      
//      private void populateTable()  throws Exception{
////          ArrayList incDataList = new ArrayList();
////          incDataList = new ArrayList(incParMap.keySet());
////          ArrayList addList =new ArrayList(incParMap.keySet());
////          int length = incDataList.size();
////          for(int i=0; i<length; i++){
////              ArrayList incTabRow = new ArrayList();
////              EmpTrainingTO objEmpTrainingTO = (EmpTrainingTO) incParMap.get(addList.get(i));
////              
////              IncVal.add(objEmpTrainingTO);
////              if(!objEmpTrainingTO.getEmpStatus().equals("DELETED")){
////                  incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getSlNo()));
////                  incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
////                  incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
////                  incTabRow.add("");
////                  tblEmpDetails.addRow(incTabRow);
////              }
////          }
////          notifyObservers();
//      }
//    public int serialNo(ArrayList data){
////        final int dataSize = data.size();
////        int nums[]= new int[150];
////        int max=nums[0];
////        int slno=0;
////        int a=0;
////        slno=dataSize+1;
////        for(int i=0;i<data.size();i++){
////            a=CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(i,0));
////            nums[i]=a;
////            if(nums[i]>max)
////                max=nums[i];
////            slno=max+1;
////        }
////        return slno;
//    }
//      public void addToTable(int rowSelected, boolean updateMode){
////        try{
////            int rowSel=rowSelected;
////            final EmpTrainingTO objEmpTrainingTO = new EmpTrainingTO();
////            if( incParMap == null ){
////                incParMap = new LinkedHashMap();
////            }
////            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
////                if(isNewData()){
////                        objEmpTrainingTO.setEmpCreatedBy(TrueTransactMain.USER_ID);
////                        objEmpTrainingTO.setEmpStatusDt(ClientUtil.getCurrentDate());
////                        objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
////                        objEmpTrainingTO.setEmpCreatedDt(ClientUtil.getCurrentDate());
////                        objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_CREATED);
////                }else{
////                        objEmpTrainingTO.setEmpStatusDt(ClientUtil.getCurrentDate());
////                        objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
////                        objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_MODIFIED);
////                }
////            }else{
////                        objEmpTrainingTO.setEmpCreatedBy(TrueTransactMain.USER_ID);
////                        objEmpTrainingTO.setEmpStatusDt(ClientUtil.getCurrentDate());
////                        objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
////                        objEmpTrainingTO.setEmpCreatedDt(ClientUtil.getCurrentDate());
////                        objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_CREATED);
////                }
////            int  slno=0;
////            int nums[]= new int[150];
////            int max=nums[0];
////            if(!updateMode){
////                ArrayList data = tblEmpDetails.getDataArrayList();
////                slno=serialNo(data);
////            }
////            else{
////                if(isNewData()){
////                    ArrayList data = tblEmpDetails.getDataArrayList();
////                    slno=serialNo(data);
////                }
////                else{
////                    int b=CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(rowSelected,0));
////                    slno=b;
////                }
////            }
////            
////            objEmpTrainingTO.setSlNo(String.valueOf(slno));
////            objEmpTrainingTO.setEmpName(getEmpName());
////            objEmpTrainingTO.setEmpID(getTxtEmpID());
////            incParMap.put(objEmpTrainingTO.getSlNo(),objEmpTrainingTO);
////            String sno=String.valueOf(slno);
////            updateEmpDetails(rowSel,sno,objEmpTrainingTO);
////            notifyObservers();
////        }catch(Exception e){
////            parseException.logException(e,true);
////        }
////    }
////      private void updateEmpDetails(int rowSel, String sno, EmpTrainingTO objEmpTrainingTO)  throws Exception{
////        Object selectedRow;
////        boolean rowExists = false;
////        //If row already exists update it, else create a new row & append
////        for(int i = tblEmpDetails.getRowCount(), j = 0; i > 0; i--,j++){
////            selectedRow = ((ArrayList)tblEmpDetails.getDataArrayList().get(j)).get(0);
////            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
////                rowExists = true;
////                ArrayList IncParRow = new ArrayList();
////                ArrayList data = tblEmpDetails.getDataArrayList();
////                data.remove(rowSel);
////                IncParRow.add(sno);
////                IncParRow.add(getEmpName());
////                IncParRow.add(getTxtEmpID());
////                tblEmpDetails.insertRow(rowSel,IncParRow);
////                IncParRow = null;
////            }
////        }
////        if(!rowExists){
////            ArrayList IncParRow = new ArrayList();
////                IncParRow.add(sno);
////                IncParRow.add(getEmpName());
////                IncParRow.add(getTxtEmpID());
////            tblEmpDetails.insertRow(tblEmpDetails.getRowCount(),IncParRow);
////            IncParRow = null;
////        }
////    }
//    
//      
//       public void populateLeaveDetails(String row){
////        try{
////                resetEmpDetails();
////                final EmpTrainingTO objEmpTrainingTO = (EmpTrainingTO)incParMap.get(row);
////                populateTableData(objEmpTrainingTO);
////            
////        }catch(Exception e){
////            parseException.logException(e,true);
////        }
//    }
//    
//    
////    private void populateTableData(EmpTrainingTO objEmpTrainingTO)  throws Exception{    
////        setEmpName(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
////        setTxtSlNo(objEmpTrainingTO.getSlNo());
////        setTxtEmpID(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
////        setChanged();
////        notifyObservers();
////    }
//    
//    public void resetTableValues(){
////        tblEmpDetails.setDataArrayList(null,tableTitle);
//    }
//    
//    /** To set the status based on ActionType, either New, Edit, etc., */
//    public void setStatus(){
//       this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
//        ttNotifyObservers();
//    }
//    
//    /** To update the Status based on result performed by doAction() method */
//    public void setResultStatus(){
//       this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
//        ttNotifyObservers();
//    }
//    
//    /**
//     * Getter for property txtStatusBy.
//     * @return Value of property txtStatusBy.
//     */
//   
//    /**
//     * Getter for property actionType.
//     * @return Value of property actionType.
//     */
//    public int getActionType() {
//        return actionType;
//    }
//    
//    /**
//     * Setter for property actionType.
//     * @param actionType New value of property actionType.
//     */
//   
//    
//    /**
//     * Getter for property lblStatus.
//     * @return Value of property lblStatus.
//     */
//    public java.lang.String getLblStatus() {
//        return lblStatus;
//    }
//    
//    /**
//     * Setter for property lblStatus.
//     * @param lblStatus New value of property lblStatus.
//     */
//    public void setLblStatus(java.lang.String lblStatus) {
//        this.lblStatus = lblStatus;
//    }
//    
//    /**
//     * Getter for property _authorizeMap.
//     * @return Value of property _authorizeMap.
//     */
//    public java.util.HashMap get_authorizeMap() {
//        return _authorizeMap;
//    }
//    
//    /**
//     * Setter for property _authorizeMap.
//     * @param _authorizeMap New value of property _authorizeMap.
//     */
//    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
//        this._authorizeMap = _authorizeMap;
//    }
//    
//    /**
//   
//    
//    /**
//     * Getter for property CreatedDt.
//     * @return Value of property CreatedDt.
//     */
//   
//  
//    
//    /**
//     * Getter for property result.
//     * @return Value of property result.
//     */
////    public int getResult() {
////        return result;
////    }
////    
//    /**
//     * Setter for property result.
//     * @param result New value of property result.
//     */
////    public void setResult(int result) {
////        this.result = result;
////    }
////    
//    
//    /**
//     * Getter for property txtEmpID.
//     * @return Value of property txtEmpID.
//     */
////    public java.lang.String getTxtEmpID() {
////        return txtEmpID;
////    }
////    
//    /**
//     * Setter for property txtEmpID.
//     * @param txtEmpID New value of property txtEmpID.
//     */
////    public void setTxtEmpID(java.lang.String txtEmpID) {
////        this.txtEmpID = txtEmpID;
////    }
////    
////   
////  
////    /**
////     * Getter for property txtStatus.
////     * @return Value of property txtStatus.
////     */
////    public java.lang.String getTxtStatus() {
////        return txtStatus;
////    }
////    
////    /**
////     * Setter for property txtStatus.
////     * @param txtStatus New value of property txtStatus.
////     */
////    public void setTxtStatus(java.lang.String txtStatus) {
////        this.txtStatus = txtStatus;
////    }
////        
////    /**
////     * Getter for property empName.
////     * @return Value of property empName.
////     */
////    public java.lang.String getEmpName() {
////        return empName;
////    }
////    
////    /**
////     * Setter for property empName.
////     * @param empName New value of property empName.
////     */
////    public void setEmpName(java.lang.String empName) {
////        this.empName = empName;
////    }
////    
////    
////    /**
////     * Getter for property txtEmpTrainingID.
////     * @return Value of property txtEmpTrainingID.
////     */
////    public java.lang.String getTxtEmpTrainingID() {
////        return txtEmpTrainingID;
////    }
////    
////    /**
////     * Setter for property txtEmpTrainingID.
////     * @param txtEmpTrainingID New value of property txtEmpTrainingID.
////     */
////    public void setTxtEmpTrainingID(java.lang.String txtEmpTrainingID) {
////        this.txtEmpTrainingID = txtEmpTrainingID;
////    }
////    
////    /**
////     * Getter for property destination.
////     * @return Value of property destination.
////     */
////    public java.lang.String getDestination() {
////        return destination;
////    }
////    
////    /**
////     * Setter for property destination.
////     * @param destination New value of property destination.
////     */
////    public void setDestination(java.lang.String destination) {
////        this.destination = destination;
////    }
////    
////    /**
////     * Getter for property location.
////     * @return Value of property location.
////     */
////    public java.lang.String getLocation() {
////        return location;
////    }
////    
////    /**
////     * Setter for property location.
////     * @param location New value of property location.
////     */
////    public void setLocation(java.lang.String location) {
////        this.location = location;
////    }
////    
////    /**
////     * Getter for property team.
////     * @return Value of property team.
////     */
////    public java.lang.String getTeam() {
////        return team;
////    }
////    
////    /**
////     * Setter for property team.
////     * @param team New value of property team.
////     */
////    public void setTeam(java.lang.String team) {
////        this.team = team;
////    }
////    
////    /**
////     * Getter for property teamSize.
////     * @return Value of property teamSize.
////     */
////    public java.lang.String getTeamSize() {
////        return teamSize;
////    }
////    
////    /**
////     * Setter for property teamSize.
////     * @param teamSize New value of property teamSize.
////     */
////    public void setTeamSize(java.lang.String teamSize) {
////        this.teamSize = teamSize;
////    }
////    
////    /**
////     * Getter for property trainingFrom.
////     * @return Value of property trainingFrom.
////     */
////    public java.lang.String getTrainingFrom() {
////        return trainingFrom;
////    }
////    
////    /**
////     * Setter for property trainingFrom.
////     * @param trainingFrom New value of property trainingFrom.
////     */
////    public void setTrainingFrom(java.lang.String trainingFrom) {
////        this.trainingFrom = trainingFrom;
////    }
////    
////    /**
////     * Getter for property trainingTo.
////     * @return Value of property trainingTo.
////     */
////    public java.lang.String getTrainingTo() {
////        return trainingTo;
////    }
////    
////    /**
////     * Setter for property trainingTo.
////     * @param trainingTo New value of property trainingTo.
////     */
////    public void setTrainingTo(java.lang.String trainingTo) {
////        this.trainingTo = trainingTo;
////    }
////    
////    /**
////     * Getter for property empBran.
////     * @return Value of property empBran.
////     */
////    public java.lang.String getEmpBran() {
////        return empBran;
////    }
////    
////    /**
////     * Setter for property empBran.
////     * @param empBran New value of property empBran.
////     */
////    public void setEmpBran(java.lang.String empBran) {
////        this.empBran = empBran;
////    }
////    
////    /**
////     * Getter for property cbmTrainingDest.
////     * @return Value of property cbmTrainingDest.
////     */
////    public com.see.truetransact.clientutil.ComboBoxModel getCbmTrainingDest() {
////        return cbmTrainingDest;
////    }
////    
////    /**
////     * Setter for property cbmTrainingDest.
////     * @param cbmTrainingDest New value of property cbmTrainingDest.
////     */
////    public void setCbmTrainingDest(com.see.truetransact.clientutil.ComboBoxModel cbmTrainingDest) {
////        this.cbmTrainingDest = cbmTrainingDest;
////    }
////    
////    /**
////     * Getter for property cboTrainingDest.
////     * @return Value of property cboTrainingDest.
////     */
////    public java.lang.String getCboTrainingDest() {
////        return cboTrainingDest;
////    }
////    
////    /**
////     * Setter for property cboTrainingDest.
////     * @param cboTrainingDest New value of property cboTrainingDest.
////     */
////    public void setCboTrainingDest(java.lang.String cboTrainingDest) {
////        this.cboTrainingDest = cboTrainingDest;
////    }
////    
////    /**
////     * Getter for property noOfTrainees.
////     * @return Value of property noOfTrainees.
////     */
////    public java.lang.String getNoOfTrainees() {
////        return noOfTrainees;
////    }
////    
////    /**
////     * Setter for property noOfTrainees.
////     * @param noOfTrainees New value of property noOfTrainees.
////     */
////    public void setNoOfTrainees(java.lang.String noOfTrainees) {
////        this.noOfTrainees = noOfTrainees;
////    }
////    
////    /**
////     * Getter for property newData.
////     * @return Value of property newData.
////     */
////    public boolean isNewData() {
////        return newData;
////    }
////    
////    /**
////     * Setter for property newData.
////     * @param newData New value of property newData.
////     */
////    public void setNewData(boolean newData) {
////        this.newData = newData;
////    }
////    
////    /**
////     * Getter for property txtSlNo.
////     * @return Value of property txtSlNo.
////     */
////    public java.lang.String getTxtSlNo() {
////        return txtSlNo;
////    }
////    
////    /**
////     * Setter for property txtSlNo.
////     * @param txtSlNo New value of property txtSlNo.
////     */
////    public void setTxtSlNo(java.lang.String txtSlNo) {
////        this.txtSlNo = txtSlNo;
////    }
////    
////    
//    /**
//     * Getter for property tblEmpDetails.
//     * @return Value of property tblEmpDetails.
//     */
////    public com.see.truetransact.clientutil.EnhancedTableModel getTblEmpDetails() {
////        return tblEmpDetails;
////    }
////    
////    /**
////     * Setter for property tblEmpDetails.
////     * @param tblEmpDetails New value of property tblEmpDetails.
////     */
////    public void setTblEmpDetails(com.see.truetransact.clientutil.EnhancedTableModel tblEmpDetails) {
////        this.tblEmpDetails = tblEmpDetails;
////    }
//    
//    /**
//     * Getter for property subj.
//     * @return Value of property subj.
//     */
////    public java.lang.String getSubj() {
////        return subj;
////    }
////    
////    /**
////     * Setter for property subj.
////     * @param subj New value of property subj.
////     */
////    public void setSubj(java.lang.String subj) {
////        this.subj = subj;
////    }
////    
//    }
    
  
    
   