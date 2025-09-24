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

package com.see.truetransact.ui.directoryboardsetting;

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
import com.see.truetransact.transferobject.directoryboardsetting.DirectoryBoardTO;

/**
 *
 * @author  
 *
 */
public class DirectoryBoardSettingOB extends CObservable {
  
     private String txtid = "";
     private String txtMemno = "";
     private String txtDesig= "";
     private String txtPriority = "";
     private String txtAccHead = "";
   
     private int actionType;
     private String txtStatus = "";
     private int result;
     private HashMap _authorizeMap;
     private  DirectoryBoardTO objDirectoryBoardTO ;
     private String CreatedDt="";
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private String txtStatusBy = "";
     private HashMap map;
     private ProxyFactory proxy;
     private final static Logger log = Logger.getLogger(DirectoryBoardSettingOB.class);
  
       private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
     private  Date tdtSelDate = new Date();
     
    // Added by nithya on 17-03-2017 
    private ComboBoxModel cbmProdType;    
    private String txtActNo = "";
    private HashMap lookUpHash;
    private String cboProdType = "";
    private String txtProdId = "";
  
      private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates a new instance of TDS MiantenanceOB */
      public DirectoryBoardSettingOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DirectoryBoardSettingJNDI");
            map.put(CommonConstants.HOME, "DirectoryBoardSettingHome");
            map.put(CommonConstants.REMOTE, "DirectoryBoardSetting");
            fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
//    private void fillDropdown() throws Exception{
//      lookupMap = new HashMap();
//            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
//            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//            
//            HashMap param = new HashMap();
//            param.put(CommonConstants.MAP_NAME,"getAllBoardMem");
//            HashMap where = new HashMap();
//            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            param.put(CommonConstants.PARAMFORQUERY, where);
//            where = null;
//            keyValue = ClientUtil.populateLookupData(param);
//            fillData((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmBoardMember = new ComboBoxModel(key,value);
//            
//          
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

    public String getTxtAccHead() {
        return txtAccHead;
    }

    public void setTxtAccHead(String txtAccHead) {
        this.txtAccHead = txtAccHead;
    }
   
    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
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
    
    private void doActionPerform() throws Exception{
        final DirectoryBoardTO objDirectoryBoardTO = new DirectoryBoardTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("DirectoryboardSetting",setDirectoryBoardTO());
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in DirectoryBoard OB : " + data);
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
           objDirectoryBoardTO = (DirectoryBoardTO)((List) data.get("DirectoryTO")).get(0);
            populateData(objDirectoryBoardTO);
            ttNotifyObservers();
        }catch(Exception e){
           parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    //-----------------------------------------------------------------------------------------------------------------------------------------------
    public DirectoryBoardTO setDirectoryBoardTO() {
        
        final DirectoryBoardTO objDirectoryBoardTO = new DirectoryBoardTO();
        try{
          
           objDirectoryBoardTO.setCommand(getCommand());
           objDirectoryBoardTO.setStatus(getAction());
           objDirectoryBoardTO.setStatusBy(TrueTransactMain.USER_ID);
           

           objDirectoryBoardTO.setMemno(getTxtMemno());
           System.out.println("getMemno"+objDirectoryBoardTO.getMemno());
           objDirectoryBoardTO.setDesig(getTxtDesig());
           System.out.println("getDesig"+objDirectoryBoardTO.getDesig());
           objDirectoryBoardTO.setPriority(CommonUtil.convertObjToInt(getTxtPriority()));
           objDirectoryBoardTO.setId(getTxtid());
           objDirectoryBoardTO.setBranCode(null);
           objDirectoryBoardTO.setSelDate(getTdtSelDate());   
           objDirectoryBoardTO.setAccHead(getTxtAccHead());
           
             if(getCommand().equalsIgnoreCase("INSERT")){
                objDirectoryBoardTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDirectoryBoardTO.setCreatedDt(ClientUtil.getCurrentDate());
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
            
            // Added by nithya on 17-03-2017
            objDirectoryBoardTO.setProdId(getTxtProdId());
            objDirectoryBoardTO.setProdType(getCboProdType());
            objDirectoryBoardTO.setActNo(getTxtActNo());
            
       }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDirectoryBoardTO;
    }
    
    private void populateData(DirectoryBoardTO objDirectoryBoardTO) throws Exception{
        
         this.setTxtMemno(CommonUtil.convertObjToStr(objDirectoryBoardTO.getMemno()));
         this.setTxtDesig(CommonUtil.convertObjToStr(objDirectoryBoardTO.getDesig()));
         this.setTxtPriority(CommonUtil.convertObjToStr(objDirectoryBoardTO.getPriority()));
         this.setTxtid(CommonUtil.convertObjToStr(objDirectoryBoardTO.getId()));
         this.setTdtSelDate(objDirectoryBoardTO.getSelDate());
         this.setTxtAccHead(objDirectoryBoardTO.getAccHead());
         // Added by nithya on 17-03-2017
         this.setCboProdType(CommonUtil.convertObjToStr(objDirectoryBoardTO.getProdType()));
         this.setTxtProdId(CommonUtil.convertObjToStr(objDirectoryBoardTO.getProdId()));
         this.setTxtActNo(CommonUtil.convertObjToStr(objDirectoryBoardTO.getActNo()));
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
       setTxtMemno("");
       setTxtDesig("");
       setTxtPriority("");
       setTdtSelDate(null);
       setTxtAccHead("");
       // Added by nithya on 17-03-2017
       setCboProdType("");
       setTxtProdId("");
       setTxtActNo("");
       // End
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

    public Date getTdtSelDate() {
        return tdtSelDate;
    }

    public void setTdtSelDate(Date tdtSelDate) {
        this.tdtSelDate = tdtSelDate;
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
     * Getter for property txtid.
     * @return Value of property txtid.
     */
    public String getTxtid() {
        return txtid;
    }
    
    /**
     * Setter for property txtid.
     * @param txtid New value of property txtid.
     */
    public void setTxtid(String txtid) {
        this.txtid = txtid;
    }
    
    /**
     * Getter for property txtMemno.
     * @return Value of property txtMemno.
     */
    public String getTxtMemno() {
        return txtMemno;
    }
    
    /**
     * Setter for property txtMemno.
     * @param txtMemno New value of property txtMemno.
     */
    public void setTxtMemno(String txtMemno) {
        this.txtMemno = txtMemno;
    }
    
    /**
     * Getter for property txtDesig.
     * @return Value of property txtDesig.
     */
    public String getTxtDesig() {
        return txtDesig;
    }
    
    /**
     * Setter for property txtDesig.
     * @param txtDesig New value of property txtDesig.
     */
    public void setTxtDesig(String txtDesig) {
        this.txtDesig = txtDesig;
    }
    
    /**
     * Getter for property txtPriority.
     * @return Value of property txtPriority.
     */
    public String getTxtPriority() {
        return txtPriority;
    }
    
    /**
     * Setter for property txtPriority.
     * @param txtPriority New value of property txtPriority.
     */
    public void setTxtPriority(String txtPriority) {
        this.txtPriority = txtPriority;
    }
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,map);
        }catch(Exception e){
            accountHead.setText("");
            parseException.logException(e,true);
        }
    } 
    
    public void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();           
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            fillData((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);    
            cbmProdType.removeKeyAndElement("AB");    
            cbmProdType.removeKeyAndElement("AD");
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");  
            cbmProdType.removeKeyAndElement("MDS");  
            cbmProdType.removeKeyAndElement("GL");  
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCboProdType() {
        return cboProdType;
    }

    public void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
    }

    public String getTxtActNo() {
        return txtActNo;
    }

    public void setTxtActNo(String txtActNo) {
        this.txtActNo = txtActNo;
    }

    public String getTxtProdId() {
        return txtProdId;
    }

    public void setTxtProdId(String txtProdId) {
        this.txtProdId = txtProdId;
    }
    
    
}
    
   
   
    
    