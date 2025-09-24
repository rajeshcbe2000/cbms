/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-07-2015
 */
package com.see.truetransact.ui.payroll.pfInterestApplication;

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

/**
 *
 * @author  
 *
 */
public class PFInterestApplicationOB extends CObservable {
  
     private String txtFromNo = "";
     private String txtToNo = "";
     private String txtFromDate= "";
     private String txtToDate = "";
     private String txtIntRate = "";
   
     private int actionType;
     private String txtStatus = "";
     private int result;
     private HashMap _authorizeMap;
     private String CreatedDt="";
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private String txtStatusBy = "";
     private HashMap map;
     private ProxyFactory proxy;
     private final static Logger log = Logger.getLogger(PFInterestApplicationOB.class);
  
       private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList tableValue =new ArrayList();
    Date CurDate = null;
    private ComboBoxModel cbmPfType;
      private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates a new instance of TDS MiantenanceOB */
      public PFInterestApplicationOB() {
        try {
            proxy = ProxyFactory.createProxy();
              CurDate = ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "PFInterestApplicationJNDI");
            map.put(CommonConstants.HOME, "PFInterestApplicationHome");
            map.put(CommonConstants.REMOTE, "PFInterestApplication");
            fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");            
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"getAllPFTypeDeductions");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmPfType = new ComboBoxModel(key,value);   
    }
    
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
     
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
         final HashMap data = new HashMap();
        System.out.println("ggggggggggggggg");
        data.put("COMMAND", getCommand());
        data.put("PF_INT_CALC", "PF_INT_CALC");
        data.put("FROM_ACC_NO", getTxtFromNo());
        data.put("TO_ACC_NO", getTxtToNo());
        data.put("FROM_DATE", getTxtFromDate());
        data.put("TO_DATE", getTxtToDate());
        data.put("INT_RATE", getTxtIntRate());
        System.out.println("data in DirectoryBoard OB : " + data);
        System.out.println("data data" + data + " map map map  " + map);
        HashMap resultData = proxy.executeQuery(data,lookupMap);
        log.info("Got HashMap");
        return resultData;
   }
    
  
   
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction(HashMap map) {
        try {
          
           if( actionType != ClientConstants.ACTIONTYPE_CANCEL){
                doActionPerform(map);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           parseException.logException(e,true);
        }
    }
    
    
     public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            System.out.println("data -------- :"+data);
            HashMap proxyResultMap = proxy.execute(data,map);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }

    private void doActionPerform(HashMap obj) throws Exception {
        final HashMap data = obj;
        System.out.println("g data >> :"+data);
        data.put("COMMAND", getCommand());
        data.put("PF_INT_CALC", "PF_INT_CALC");
        data.put("FROM_ACC_NO", getTxtFromNo());
        data.put("TO_ACC_NO", getTxtToNo());
        data.put("FROM_DATE", getTxtFromDate());
        data.put("TO_DATE", getTxtToDate());
        data.put("INT_RATE", getTxtIntRate());
        data.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
         data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        System.out.println("data in DirectoryBoarddddddddddddd OB : " + data);
        System.out.println("data data" + data + " ddddddddmap map map  " + map);
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
    public void getData() {
         try{
             final HashMap whereMap = new HashMap();
             System.out.println("ggggggggggggggg");
             whereMap.put("COMMAND", getCommand());
             whereMap.put("PF_INT_CALC", "PF_INT_CALC");
             whereMap.put("FROM_ACC_NO", getTxtFromNo());
             whereMap.put("TO_ACC_NO", getTxtToNo());
             whereMap.put("FROM_DATE",CommonUtil.getProperDate( ClientUtil.getCurrentDate(), DateUtil.getDateMMDDYYYY(getTxtFromDate())) );
             whereMap.put("TO_DATE", CommonUtil.getProperDate( ClientUtil.getCurrentDate(), DateUtil.getDateMMDDYYYY(getTxtToDate())) );
             whereMap.put("INT_RATE", CommonUtil.convertObjToDouble(getTxtIntRate()));
             whereMap.put("PF_TYPE",CommonUtil.convertObjToStr(getCbmPfType().getKeyForSelected()));
             final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
             
             System.out.println("dataVVVVVvv :"+data);
             tableValue= (ArrayList)data.get("INT_DETAILS");
             
         //  objDirectoryBoardTO = (DirectoryBoardTO)((List) data.get("DirectoryTO")).get(0);
          //  populateData(objDirectoryBoardTO);
            ttNotifyObservers();
        }catch(Exception e){
           parseException.logException(e,true);
        }
    }

    public ArrayList getTableValue() {
        return tableValue;
    }

    public void setTableValue(ArrayList tableValue) {
        this.tableValue = tableValue;
    }
    
    /** To populate data into the screen */
    //-----------------------------------------------------------------------------------------------------------------------------------------------
   
    
    private void populateData() throws Exception{
        
         
         
        setChanged();
        notifyObservers();
    }
    
       public void resetForm() {
        setTxtFromDate("");        
        setTxtFromNo("");
        setTxtToDate("");
        setTxtToNo("");
        setTxtIntRate("");
        setTableValue(null);
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

    public String getTxtIntRate() {
        return txtIntRate;
    }

    public void setTxtIntRate(String txtIntRate) {
        this.txtIntRate = txtIntRate;
    }

    

    public String getTxtFromDate() {
        return txtFromDate;
    }

    public void setTxtFromDate(String txtFromDate) {
        this.txtFromDate = txtFromDate;
    }

    public String getTxtFromNo() {
        return txtFromNo;
    }

    public void setTxtFromNo(String txtFromNo) {
        this.txtFromNo = txtFromNo;
    }

    public String getTxtToDate() {
        return txtToDate;
    }

    public void setTxtToDate(String txtToDate) {
        this.txtToDate = txtToDate;
    }

    public String getTxtToNo() {
        return txtToNo;
    }

    public void setTxtToNo(String txtToNo) {
        this.txtToNo = txtToNo;
    }

    public ComboBoxModel getCbmPfType() {
        return cbmPfType;
    }

    public void setCbmPfType(ComboBoxModel cbmPfType) {
        this.cbmPfType = cbmPfType;
    } 
    
}
    
   
   
    
    