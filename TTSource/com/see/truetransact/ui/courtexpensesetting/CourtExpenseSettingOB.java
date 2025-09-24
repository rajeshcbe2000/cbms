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

package com.see.truetransact.ui.courtexpensesetting;

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

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
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
import com.see.truetransact.transferobject.courtexpensesetting.CourtExpenseSettingTO;
import java.util.Date;
/**
 *
 * @author  
 *
 */
public class CourtExpenseSettingOB extends CObservable {
  
     private String txtId = "";
     private String tdtDate = "";
     private String txtFromAmt = "";
     private String txtToAmt= "";
     private String txtPid="";
     private String txtPercentage = "";
     private List tblList= new ArrayList(); 
     private int actionType;
     private String txtStatus = "";
     private int result;
     private HashMap _authorizeMap;
     private  CourtExpenseSettingTO objCourtExpenseSettingTO;
     
     private String CreatedDt="";
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private String txtStatusBy = "";
     private HashMap map;
     private ProxyFactory proxy;
     private final static Logger log = Logger.getLogger(CourtExpenseSettingOB.class);
     private HashMap lookupMap;
     private HashMap param;
     private HashMap keyValue;
     private ArrayList key;
     private ArrayList value;
     private ArrayList tblData;
     final ArrayList prodColoumn = new ArrayList();
     List selectedList=new ArrayList();
     private final static ClientParseException parseException = ClientParseException.getInstance();
     private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
      public CourtExpenseSettingOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "CourtExpenseSettingJNDI");
            map.put(CommonConstants.HOME, "CourtExpenseSettingHome");
            map.put(CommonConstants.REMOTE, "CourtExpenseSetting");
           
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
      
 
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
     
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
         return keyValue;
   }

    

    /** To perform the necessary operation */
    public void doAction() {
        try {
          
           if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           parseException.logException(e,true);
        }
    }
    

    private void doActionPerform() throws Exception{
        final CourtExpenseSettingTO objCourtExpenseSettingTO = new CourtExpenseSettingTO();
        
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        System.out.println("tblDatatblDatatblData"+tblData);
        if(get_authorizeMap() == null){
         data.put("CourtExpenseSetting",setCourtExpenseSettingTO());
      
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in CourtExpenseSetting OB : " + data);
         System.out.println("data data"+data+" map map map  "+map);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
        setResult(actionType);
    }
       
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
       
        return command;
    }
    
    private String getAction(){
       String action = null;
        
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objCourtExpenseSettingTO = (CourtExpenseSettingTO)((List) data.get("CourtTO")).get(0);
            objCourtExpenseSettingTO.setList((List)data.get("CourtProductTO"));
            populateData(objCourtExpenseSettingTO);
            ttNotifyObservers();
        }catch(Exception e){
           parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    //-----------------------------------------------------------------------------------------------------------------------------------------------
    public CourtExpenseSettingTO setCourtExpenseSettingTO() {
        
        final CourtExpenseSettingTO objCourtExpenseSettingTO = new CourtExpenseSettingTO();
        try{
            objCourtExpenseSettingTO.setDate(DateUtil.getDateMMDDYYYY(getTdtDate()));
            objCourtExpenseSettingTO.setFromamt(getTxtFromAmt());
            objCourtExpenseSettingTO.setToamt(getTxtToAmt());
            objCourtExpenseSettingTO.setPercentage(getTxtPercentage());
            objCourtExpenseSettingTO.setId(getTxtId());
            objCourtExpenseSettingTO.setList(getTblList());
            objCourtExpenseSettingTO.setBranCode(null);
            objCourtExpenseSettingTO.setCommand(getCommand());
            objCourtExpenseSettingTO.setStatus(getAction());
            objCourtExpenseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
                  
             if(getCommand().equalsIgnoreCase("INSERT")){
                objCourtExpenseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objCourtExpenseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
             
            }
       }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objCourtExpenseSettingTO;
    }

    
    
    private void populateData(CourtExpenseSettingTO objCourtExpenseSettingTO) throws Exception{
         
      
         this.setTxtFromAmt(CommonUtil.convertObjToStr(objCourtExpenseSettingTO.getFromamt()));
         this.setTxtToAmt(CommonUtil.convertObjToStr(objCourtExpenseSettingTO.getToamt()));
         this.setTxtPercentage(CommonUtil.convertObjToStr(objCourtExpenseSettingTO.getPercentage()));
         this.setTxtId(CommonUtil.convertObjToStr(objCourtExpenseSettingTO.getId()));
         this.setTdtDate(CommonUtil.convertObjToStr(objCourtExpenseSettingTO.getDate()));
         this.setSelectedList(objCourtExpenseSettingTO.getList());
         System.out.println("setTxttoamtsetTxttoamtsetTxttoamt"+ getTxtToAmt());
         System.out.println("setTxtpercentagesetTxtpercentage"+ getTxtPercentage());
         System.out.println("setTxtId"+getTxtId());
         System.out.println("setTdtDatesetTdtDate"+ getTdtDate());
         setChanged();
         notifyObservers();
    }
    

        
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void resetForm(){
       setTxtFromAmt("");
       setTxtToAmt("");
       setTxtPercentage("");
       setTdtDate("");
       setChanged();
       ttNotifyObservers();
    
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
       this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
       this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /**
     * Getter for property txtStatusBy.
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }
    
    /**
     * Setter for property txtStatusBy.
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
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
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
   
    
    /**
     * Getter for property CreatedDt.
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }
    
    /**
     * Setter for property CreatedDt.
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
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
     * Getter for property txtStatus.
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }
    
    /**
     * Setter for property txtStatus.
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }

    
 
  
    
    /**
     * Getter for property txtId.
     * @return Value of property txtId.
     */
    public String getTxtId() {
        return txtId;
    }
    
    /**
     * Setter for property txtId.
     * @param txtId New value of property txtId.
     */
    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }
 
    /**
     * Getter for property tdtDate.
     * @return Value of property tdtDate.
     */
    public String getTdtDate() {
        return tdtDate;
    }    
    
    /**
     * Setter for property tdtDate.
     * @param tdtDate New value of property tdtDate.
     */
    public void setTdtDate(String tdtDate) {
        this.tdtDate = tdtDate;
    }
    
    /**
     * Getter for property txtFromAmt.
     * @return Value of property txtFromAmt.
     */
    public String getTxtFromAmt() {
        return txtFromAmt;
    }
    
    /**
     * Setter for property txtFromAmt.
     * @param txtFromAmt New value of property txtFromAmt.
     */
    public void setTxtFromAmt(String txtFromAmt) {
        this.txtFromAmt = txtFromAmt;
    }
    
    /**
     * Getter for property txtToAmt.
     * @return Value of property txtToAmt.
     */
    public String getTxtToAmt() {
        return txtToAmt;
    }
    
    /**
     * Setter for property txtToAmt.
     * @param txttoamt New value of property txtToAmt.
     */
    public void setTxtToAmt(String txtToAmt) {
        this.txtToAmt = txtToAmt;
    }
    
    /**
     * Getter for property txtPercentage.
     * @return Value of property txtPercentage.
     */
    public String getTxtPercentage() {
        return txtPercentage;
    }
    
    /**
     * Setter for property txtPercentage.
     * @param txtPercentage New value of property txtPercentage.
     */
    public void setTxtPercentage(String txtPercentage) {
        this.txtPercentage = txtPercentage;
    }
    
    /**
     * Getter for property tblProduct.
     * @return Value of property tblProduct.
     */
//    public EnhancedTableModel getTblProduct() {
//        return tblProduct;
//    }
//    
    /**
     * Setter for property tblProduct.
     * @param tblProduct New value of property tblProduct.
     */
//    public void setTblProduct(EnhancedTableModel tblProduct) {
//        this.tblProduct = tblProduct;
//    }
//    
    /**
     * Getter for property tblList.
     * @return Value of property tblList.
     */
    public List getTblList() {
        return tblList;
    }
    
    /**
     * Setter for property tblList.
     * @param tblList New value of property tblList.
     */
    public void setTblList(List tblList) {
        this.tblList = tblList;
    }
    
    /**
     * Getter for property tblData.
     * @return Value of property tblData.
     */
    public ArrayList getTblData() {
        return tblData;
    }
    
    /**
     * Setter for property tblData.
     * @param tblData New value of property tblData.
     */
    public void setTblData(ArrayList tblData) {
        this.tblData = tblData;
    }
    
    /**
     * Getter for property txtPid.
     * @return Value of property txtPid.
     */
    public String getTxtPid() {
        return txtPid;
    }    
  
    /**
     * Setter for property txtPid.
     * @param txtPid New value of property txtPid.
     */
    public void setTxtPid(String txtPid) {
        this.txtPid = txtPid;
    }    
    
    /**
     * Getter for property selectedList.
     * @return Value of property selectedList.
     */
    public List getSelectedList() {
        return selectedList;
    }
    
    /**
     * Setter for property selectedList.
     * @param selectedList New value of property selectedList.
     */
    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }
    
}
    
   
   
    
    