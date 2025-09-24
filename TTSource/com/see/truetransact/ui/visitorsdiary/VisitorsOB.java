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

package com.see.truetransact.ui.visitorsdiary;

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
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.visitorsdiary.VisitorsDiaryTO;

/**
 *
 * @author  
 *
 */
public class VisitorsOB extends CObservable {
    private String txtVisitorsid = "";
    private String txtDateofVisit="";
    private String txaInstNameAddress="";
    private String txaNameAddress="";
    private String txaPurposeofVisit= "";
    private String txaCommentsLeft="";
    private String txtStatusBy = "";
    private String txtStatus = "";
    private String CreatedDt="";
    private String currBranName="";
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(VisitorsOB.class);
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
    private VisitorsOB visitorsOB;
    VisitorsRB visitorsRB = new VisitorsRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private VisitorsDiaryTO objVisitorsDiaryTO;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public VisitorsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            currDt = ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "VisitorsDiaryJNDI");
            map.put(CommonConstants.HOME, "visitorsdiary.visitorsHome");
            map.put(CommonConstants.REMOTE, "visitorsdiary.visitors");
            //fillDropdown();
            
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
    
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
       // final VisitorsDiaryTO objVisitorsDiaryTO = new VisitorsDiaryTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("VisitorsDiary",setVisitorsData());
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in VisitorsDiary OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
         System.out.println("data in VisitorsDiary OB return... : " + proxyResultMap);
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
                ClientUtil.showMessageWindow("Visitors Id : " + CommonUtil.convertObjToStr(proxyResultMap.get("VISIT_ID")));
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
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objVisitorsDiaryTO = (VisitorsDiaryTO)((List) data.get("VisitorsTO")).get(0);
            populateData(objVisitorsDiaryTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public VisitorsDiaryTO setVisitorsData() {
        
        final VisitorsDiaryTO objVisitorsDiaryTO = new VisitorsDiaryTO();
        try{
            
            objVisitorsDiaryTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objVisitorsDiaryTO.setStatusBy(ProxyParameters.USER_ID);
            objVisitorsDiaryTO.setVisitorsid(getTxtVisitorsid());
         
           objVisitorsDiaryTO.setDateofVisit(DateUtil.getDateMMDDYYYY(getTxtDateofVisit()));
           objVisitorsDiaryTO.setCommand(getCommand());
           objVisitorsDiaryTO.setStatus(getAction());
           objVisitorsDiaryTO.setStatusBy(TrueTransactMain.USER_ID);
           objVisitorsDiaryTO.setInstNameAddress(getTxaInstNameAddress());
           objVisitorsDiaryTO.setNameAddress(getTxaNameAddress());
           objVisitorsDiaryTO.setPurposeofVisit(getTxaPurposeofVisit());
           objVisitorsDiaryTO.setCommentsLeft(getTxaCommentsLeft());
           objVisitorsDiaryTO.setBranCode(null);
           if(getCommand().equalsIgnoreCase("INSERT")){
           objVisitorsDiaryTO.setCreatedBy(TrueTransactMain.USER_ID);
           objVisitorsDiaryTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setVisitorsData()");
            e.printStackTrace();
        }
        return objVisitorsDiaryTO;
    }
//    
    private void populateData(VisitorsDiaryTO objVisitorsDiaryTO) throws Exception{
        this.setTxtVisitorsid(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getVisitorsid()));
        this.setTxaInstNameAddress(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getInstNameAddress()));
        this.setTxaNameAddress(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getNameAddress()));
        this.setTxaPurposeofVisit(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getPurposeofVisit()));
        this.setTxaCommentsLeft(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getCommentsLeft()));
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getStatusBy()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getCreatedDt()));
        this.setTxtDateofVisit(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getDateofVisit()));
        //this.setTxt(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getCurrBran()));
        this.setCurrBranName(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getCurrBranName()));
        this.setStatusBy(CommonUtil.convertObjToStr(objVisitorsDiaryTO.getStatusBy()));
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
       setTxtDateofVisit("");
       setTxaInstNameAddress("");
       setTxaNameAddress("");
       setTxaPurposeofVisit("");
       setTxaCommentsLeft("");
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
     * Getter for property currBranName.
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }
    
    /**
     * Setter for property currBranName.
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }
    
    /**
     * Getter for property txtVisitorsid.
     * @return Value of property txtVisitorsid.
     */
    public String getTxtVisitorsid() {
        return txtVisitorsid;
    }
    
    /**
     * Setter for property txtVisitorsid.
     * @param txtVisitorsid New value of property txtVisitorsid.
     */
    public void setTxtVisitorsid(String txtVisitorsid) {
        this.txtVisitorsid = txtVisitorsid;
    }
    
    
    /**
     * Getter for property txaInstNameAddress.
     * @return Value of property txaInstNameAddress.
     */
    public String getTxaInstNameAddress() {
        return txaInstNameAddress;
    }
    
    /**
     * Setter for property txaInstNameAddress.
     * @param txaInstNameAddress New value of property txaInstNameAddress.
     */
    public void setTxaInstNameAddress(String txaInstNameAddress) {
        this.txaInstNameAddress = txaInstNameAddress;
    }
    
    /**
     * Getter for property txaNameAddress.
     * @return Value of property txaNameAddress.
     */
    public String getTxaNameAddress() {
        return txaNameAddress;
    }
    
    /**
     * Setter for property txaNameAddress.
     * @param txaNameAddress New value of property txaNameAddress.
     */
    public void setTxaNameAddress(String txaNameAddress) {
        this.txaNameAddress = txaNameAddress;
    }
    
    /**
     * Getter for property txaPurposeofVisit.
     * @return Value of property txaPurposeofVisit.
     */
    public String getTxaPurposeofVisit() {
        return txaPurposeofVisit;
    }
    
    /**
     * Setter for property txaPurposeofVisit.
     * @param txaPurposeofVisit New value of property txaPurposeofVisit.
     */
    public void setTxaPurposeofVisit(String txaPurposeofVisit) {
        this.txaPurposeofVisit = txaPurposeofVisit;
    }
    
    /**
     * Getter for property txaCommentsLeft.
     * @return Value of property txaCommentsLeft.
     */
    public String getTxaCommentsLeft() {
        return txaCommentsLeft;
    }
    
    /**
     * Setter for property txaCommentsLeft.
     * @param txaCommentsLeft New value of property txaCommentsLeft.
     */
    public void setTxaCommentsLeft(String txaCommentsLeft) {
        this.txaCommentsLeft = txaCommentsLeft;
    }
    
    /**
     * Getter for property txtDateofVisit.
     * @return Value of property txtDateofVisit.
     */
    public String getTxtDateofVisit() {
        return txtDateofVisit;
    }    

    /**
     * Setter for property txtDateofVisit.
     * @param txtDateofVisit New value of property txtDateofVisit.
     */
    public void setTxtDateofVisit(String txtDateofVisit) {
        this.txtDateofVisit = txtDateofVisit;
    }    
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
   
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
  
    
    }
    
  
    
   