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

package com.see.truetransact.ui.boardresolutiondetails;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
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
import com.see.truetransact.transferobject.boardresolutiondetails.BoardResolutionDetailsTO;
import com.see.truetransact.uicomponent.CObservable;



/**
 *
 * @author  
 *
 */
public class BoardResolutionDetailsOB extends CObservable {
   // private String txtOutwardNo = "";
    private String tdtDate="";
    private String txaDetails="";
    private String txaRemarks="";
    //private String txtSubmittedBy= "";
    private String txtReferenceNo="";
    //private String txtActionTaken="";
    //private String txtStatusBy = "";
    private String txtStatus = "";
    //private String CreatedDt="";
    //private String currBranName="";
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(BoardResolutionDetailsOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
  //  private BoardResolutionDetailsOB OutwardOB;
   // BoardResolutionDetailsRB OutwardRB = new BoardResolutionDetailsRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private BoardResolutionDetailsTO objBoardResolutionDetailsTO;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public BoardResolutionDetailsOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
//          
             map.put(CommonConstants.JNDI, "BoardResolutionDetailsJNDI");
            map.put(CommonConstants.HOME, "BoardResolutiondetailsHome");
            map.put(CommonConstants.REMOTE, "BoardResolutiondetails");

            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
//    private void fillDropdown() throws Exception{
//       lookupMap = new HashMap();
//            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
//            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
//            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//            
//            HashMap param = new HashMap();
//            param.put(CommonConstants.MAP_NAME,"getAllBran");
//            HashMap where = new HashMap();
//            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            param.put(CommonConstants.PARAMFORQUERY, where);
//            where = null;
//            keyValue = ClientUtil.populateLookupData(param);
//            fillData((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmTransferBran = new ComboBoxModel(key,value);
//            
//            param.put(CommonConstants.MAP_NAME,"getRoleEmp");
//            HashMap wheres = new HashMap();
//            param.put(CommonConstants.PARAMFORQUERY, wheres);
//            wheres = null;
//            keyValue = ClientUtil.populateLookupData(param);
//            fillData((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmRoleInTranBran = new ComboBoxModel(key,value);
//            cbmRoleInCurrBran = new ComboBoxModel(key,value);
//     
//    }
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
    
  
   
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() 
    {
        try {
          
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
   
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{

        final BoardResolutionDetailsTO objBoardResolutionDetailsTO = new BoardResolutionDetailsTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("BoardResolution",setBoardResolutionData());

         System.out.println("boardresolution dataaaaaaaaaaaaa"+data);
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in Boardresolutiondetail OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
         System.out.println("data in BoardResolutionDetails OB return... : " + proxyResultMap);
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
               ClientUtil.showMessageWindow("Resolution No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("RESOLUTION_ID")));

            }
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
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
       String action = null;
        // System.out.println("actionType : " + actionType);
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
        // System.out.println("command : " + command);
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
             System.out.println("wheremap"+whereMap);
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
         
          objBoardResolutionDetailsTO = (BoardResolutionDetailsTO)((List) data.get("BoardResolutionTO")).get(0);
        
            populateData(objBoardResolutionDetailsTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public BoardResolutionDetailsTO setBoardResolutionData() {
        
        final BoardResolutionDetailsTO objBoardResolutionTO = new BoardResolutionDetailsTO();
        try{
            
            //objInwardRegisterTO.setBranCode(TrueTransactMain.BRANCH_ID);
            //objOutwardRegisterTO.setStatusBy(ProxyParameters.USER_ID);
//            objOutwardRegisterTO.setOutwardNo(getTxtOutwardNo());
           // System.out.println("inward no....."+objOutwardRegisterTO.getOutwardNo());
            //objOutwardRegisterTO.setSubmittedBy(getTxtSubmittedBy());
           objBoardResolutionTO.setOdate(DateUtil.getDateMMDDYYYY(getTdtDate()));
           //objOutwardRegisterTO.setCommand(getCommand());
          objBoardResolutionTO.setStatus(getAction());
           //objInwardRegisterTO.setStatusBy(TrueTransactMain.USER_ID);
//           objOutwardRegisterTO.setDetails(getTxaDetails());
           objBoardResolutionTO.setRemarks(getTxaRemarks());
           objBoardResolutionTO.setReferenceNo(getTxtReferenceNo());
        
          
         
           if(getCommand().equalsIgnoreCase("INSERT")){
               objBoardResolutionTO.setCreatedBy(TrueTransactMain.USER_ID);
          objBoardResolutionTO.setCreatedDt(currDt);

           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
           
            }
        }catch(Exception e){
            log.info("Error In setBoardResolutionData()");
            e.printStackTrace();
        }
        return objBoardResolutionTO;
    }
//    
    private void populateData(BoardResolutionDetailsTO objBoardResolutionDetailsTO) throws Exception{
  
        this.setTxaRemarks(CommonUtil.convertObjToStr(objBoardResolutionDetailsTO.getRemarks()));
     
        this.setTxtReferenceNo(CommonUtil.convertObjToStr(objBoardResolutionDetailsTO.getReferenceNo()));
       
        this.setTdtDate(CommonUtil.convertObjToStr(objBoardResolutionDetailsTO.getOdate()));
      
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
       setTdtDate("");
       setTxaDetails("");
       setTxaRemarks("");
//       setTxtOutwardNo("");
       setTxtReferenceNo("");
       //setTxtSubmittedBy("");
       //setTxtActionTaken("");
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
//    public java.lang.String getTxtStatusBy() {
//        return txtStatusBy;
//    }
    
    /**
     * Setter for property txtStatusBy.
     * @param txtStatusBy New value of property txtStatusBy.
//     */
//    public void setTxtStatusBy(java.lang.String txtStatusBy) {
//        this.txtStatusBy = txtStatusBy;
//    }
    
  
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
//    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
//    
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
//    public java.lang.String getCreatedDt() {
//        return CreatedDt;
//    }
    
    /**
     * Setter for property CreatedDt.
     * @param CreatedDt New value of property CreatedDt.
     */
//    public void setCreatedDt(java.lang.String CreatedDt) {
//        this.CreatedDt = CreatedDt;
//    }

  
    
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
     * Getter for property txaPurposeofVisit.
     * @return Value of property txaPurposeofVisit.
     */
    public String getTxaRemarks() {
        return txaRemarks;
    }
    
    /**
     * Setter for property txaPurposeofVisit.
     * @param txaPurposeofVisit New value of property txaPurposeofVisit.
     */
    public void setTxaRemarks(String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }
    
    /**
     * Getter for property txaCommentsLeft.
     * @return Value of property txaCommentsLeft.
     */
    public String getTxaDetails() {
        return txaDetails;
    }
    
    /**
     * Setter for property txaCommentsLeft.
     * @param txaCommentsLeft New value of property txaCommentsLeft.
     */
    public void setTxaDetails(String txaDetails) {
        this.txaDetails = txaDetails;
    }
    
    /**
     * Getter for property txtDateofVisit.
     * @return Value of property txtDateofVisit.
     */
//    public String getTxtDate() {
//        return txtDate;
//    }    

    /**
     * Setter for property txtDateofVisit.
     * @param txtDateofVisit New value of property txtDateofVisit.
     */
//    public void setTxtDate(String txtDate) {
//        this.txtDate = txtDate;
//    }    
    
    /**
     * Getter for property txtSubmittedBy.
     * @return Value of property txtSubmittedBy.
     */
//    public String getTxtSubmittedBy() {
//        return txtSubmittedBy;
//    }
    
    /**
     * Setter for property txtSubmittedBy.
     * @param txtSubmittedBy New value of property txtSubmittedBy.
     */
//    public void setTxtSubmittedBy(String txtSubmittedBy) {
//        this.txtSubmittedBy = txtSubmittedBy;
//    }
//    
    /**
     * Getter for property txtReferenceNo.
     * @return Value of property txtReferenceNo.
     */
    public String getTxtReferenceNo() {
        return txtReferenceNo;
    }
    
    /**
     * Setter for property txtReferenceNo.
     * @param txtReferenceNo New value of property txtReferenceNo.
     */
    public void setTxtReferenceNo(String txtReferenceNo) {
        this.txtReferenceNo = txtReferenceNo;
    }
    
    /**
     * Getter for property txtActionTaken.
     * @return Value of property txtActionTaken.
     */
//    public String getTxtActionTaken() {
//        return txtActionTaken;
//    }
    
    /**
     * Setter for property txtActionTaken.
     * @param txtActionTaken New value of property txtActionTaken.
     */
//    public void setTxtActionTaken(String txtActionTaken) {
//        this.txtActionTaken = txtActionTaken;
//    }
    
    /**
     * Getter for property txtInwardNo.
     * @return Value of property txtInwardNo.
     */
//    public String getTxtInwardNo() {
//        return txtInwardNo;
//    }
    
    /**
     * Setter for property txtInwardNo.
     * @param txtInwardNo New value of property txtInwardNo.
     */
//    public void setTxtInwardNo(String txtInwardNo) {
//        this.txtInwardNo = txtInwardNo;
//    }
    
    /**
     * Setter for property txtStatus.
     * @param txtStatus New value of property txtStatus.
     */
//    public void setTxtStatus(String txtStatus) {
//        this.txtStatus = txtStatus;
//    }
//    
    /**
     * Getter for property txtOutwardNo.
     * @return Value of property txtOutwardNo.
     */
//    public String getTxtOutwardNo() {
//        return txtOutwardNo;
//    }
//    
    /**
     * Setter for property txtOutwardNo.
     * @param txtOutwardNo New value of property txtOutwardNo.
     */
//    public void setTxtOutwardNo(String txtOutwardNo) {
//        this.txtOutwardNo = txtOutwardNo;
//    }
    
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
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
   
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
  
    
    }
    
  
    
   