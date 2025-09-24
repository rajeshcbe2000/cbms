/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerIdChangeOB.java
 *
 * Created on february , 2009, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.customer.customeridchange;

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
import com.see.truetransact.transferobject.customer.customeridchange.CustomerIdChangeTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  Administrator
 * Modified by Karthik
 */
public class CustomerIdChangeOB extends CObservable {
    private String txtAccountNumber = "";
    private String txtAcctName = "";
    private String txtStatusBy = "";
    private String cboProductId="";
    private String cboProdType="";
    private String oldCustID="";
    private String newCustID="";
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmPermitedBy;
    private ComboBoxModel cbmProdType;
    private HashMap _authorizeMap;
    private String CreatedDt="";
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtNewAcctName = "";
    private String custType="";
    
    private final static Logger log = Logger.getLogger(CustomerIdChangeOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private CustomerIdChangeOB custIdChangeOB;
    CustomerIdChangeRB objCustomerIdChangeRB = new CustomerIdChangeRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private CustomerIdChangeTO objCustomerIdChangeTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public CustomerIdChangeOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "CustomerIdChangeJNDI");
            map.put(CommonConstants.HOME, "CustomerIdChangeHome");
            map.put(CommonConstants.REMOTE, "CustomerIdChange");
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
        param = new java.util.HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKeys = new ArrayList(1);
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        HashMap lookupValues = populateData(param);
        param.put(CommonConstants.MAP_NAME,null);
//        final ArrayList lookupKeys = new ArrayList(1);
        lookupKeys.add("PRODUCTTYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("PRODUCTTYPE"));
        key.remove("GL");
        value.remove("General Ledger");
        cbmProdType = new ComboBoxModel(key,value);
        lookupValues = null;
     
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
    
  public void setCbmProductId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    HashMap lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                   fillData((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProductId = new ComboBoxModel(key,value);
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
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
        final CustomerIdChangeTO objCustomerIdChangeTO = new CustomerIdChangeTO();
        final HashMap data = new HashMap();
            if(get_authorizeMap() == null)  
            data.put("CusomerIDChange",setCusomerIDChangeTransaction());
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap()); 
        data.put("COMMAND",getCommand());
        System.out.println("data in A/c Closing OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        if(actionType !=8)
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
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
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objCustomerIdChangeTO = (CustomerIdChangeTO) ((List) data.get("CustomerIdChangeTO")).get(0);
            populateCustomerIdChangeData(objCustomerIdChangeTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public CustomerIdChangeTO setCusomerIDChangeTransaction() {
        log.info("In setCashTransaction()");
        
        final CustomerIdChangeTO objCustomerIdChangeTO = new CustomerIdChangeTO();
        try{
           
           objCustomerIdChangeTO.setActNum(CommonUtil.convertObjToStr(getTxtAccountNumber()));
           objCustomerIdChangeTO.setActName(CommonUtil.convertObjToStr(getTxtAcctName()));
           objCustomerIdChangeTO.setStatusBy(ProxyParameters.USER_ID);
            objCustomerIdChangeTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
           if (!CommonUtil.convertObjToStr(getCboProductId()).equals(""))
                objCustomerIdChangeTO.setProdId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
           objCustomerIdChangeTO.setBranchCode(TrueTransactMain.BRANCH_ID);
           objCustomerIdChangeTO.setNewCustId(CommonUtil.convertObjToStr(getNewCustID()));
           objCustomerIdChangeTO.setOldCustId(CommonUtil.convertObjToStr(getOldCustID()));
           objCustomerIdChangeTO.setCommand(getCommand());
           objCustomerIdChangeTO.setStatus(getAction());
           objCustomerIdChangeTO.setStatusBy(TrueTransactMain.USER_ID);
           objCustomerIdChangeTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
           objCustomerIdChangeTO.setCreatedBy(TrueTransactMain.USER_ID);
           objCustomerIdChangeTO.setCreatedDate(ClientUtil.getCurrentDateWithTime());
           objCustomerIdChangeTO.setNewActName(CommonUtil.convertObjToStr(getTxtNewAcctName()));
        }catch(Exception e){
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCustomerIdChangeTO;
    }
    
    private void populateCustomerIdChangeData(CustomerIdChangeTO objCustomerIdChangeTO) throws Exception{
        this.setTxtAccountNumber(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getActNum()));
        this.setTxtAcctName(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getActName()));
         this.setTxtStatusBy(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getStatusBy()));
        getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getProdType())); // This line added by Rajesh
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getProdType())));
        if (!CommonUtil.convertObjToStr(objCustomerIdChangeTO.getProdId()).equals("")) {
            setCbmProductId(objCustomerIdChangeTO.getProdType());
            getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getProdId())); // This line added by Rajesh
            setCboProductId((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getProdId())));
        }
        this.setCreatedDt(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getCreatedDate()));
        this.setOldCustID(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getOldCustId()));
        this.setNewCustID(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getNewCustId()));
         this.setTxtNewAcctName(CommonUtil.convertObjToStr(objCustomerIdChangeTO.getNewActName()));
         
    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                if(mapData.get("PROD_TYPE").equals("OA") || mapData.get("PROD_TYPE").equals("AD")){
                    cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                    cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    isExists = true;
                }else{
                    cbmProductId.setKeyForSelected("");
                    cbmProdType.setKeyForSelected("");
                    isExists = false;
                }
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    public void resetForm(){
       txtAccountNumber ="";
       cboProductId="";
       txtAcctName="";
       cboProdType="";
       setChanged();
       ttNotifyObservers();
       setCboProductId("");
       getCbmProdType().setKeyForSelected("");
       CreatedDt="";
       oldCustID="";
       custType="";
       newCustID="";
       txtNewAcctName="";
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
    
    public void updateAuthorizeStatus() {
//        HashMap hash = null;
     String status = null;
//        // System.out.println("Records'll be updated... ");
  if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            status = CommonConstants.STATUS_AUTHORIZED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            status = CommonConstants.STATUS_REJECTED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            status = CommonConstants.STATUS_EXCEPTION;
        }      
    }
    
    /**
     * Getter for property txtAcctName.
     * @return Value of property txtAcctName.
     */
    public java.lang.String getTxtAcctName() {
        return txtAcctName;
    }    
   
    /**
     * Setter for property txtAcctName.
     * @param txtAcctName New value of property txtAcctName.
     */
    public void setTxtAcctName(java.lang.String txtAcctName) {
        this.txtAcctName = txtAcctName;
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
     * Getter for property cboProductId.
     * @return Value of property cboProductId.
     */
    public java.lang.String getCboProductId() {
        return cboProductId;
    }
    
    /**
     * Setter for property cboProductId.
     * @param cboProductId New value of property cboProductId.
     */
    public void setCboProductId(java.lang.String cboProductId) {
        this.cboProductId = cboProductId;
    }

    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /**
     * Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     */
    public void setCbmProductId(com.see.truetransact.clientutil.ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
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
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }
    
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
     * Getter for property oldCustID.
     * @return Value of property oldCustID.
     */
    public java.lang.String getOldCustID() {
        return oldCustID;
    }
    
    /**
     * Setter for property oldCustID.
     * @param oldCustID New value of property oldCustID.
     */
    public void setOldCustID(java.lang.String oldCustID) {
        this.oldCustID = oldCustID;
    }
    
    /**
     * Getter for property newCustID.
     * @return Value of property newCustID.
     */
    public java.lang.String getNewCustID() {
        return newCustID;
    }
    
    /**
     * Setter for property newCustID.
     * @param newCustID New value of property newCustID.
     */
    public void setNewCustID(java.lang.String newCustID) {
        this.newCustID = newCustID;
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
     * Getter for property txtNewAcctName.
     * @return Value of property txtNewAcctName.
     */
    public java.lang.String getTxtNewAcctName() {
        return txtNewAcctName;
    }
    
    /**
     * Setter for property txtNewAcctName.
     * @param txtNewAcctName New value of property txtNewAcctName.
     */
    public void setTxtNewAcctName(java.lang.String txtNewAcctName) {
        this.txtNewAcctName = txtNewAcctName;
    }
    
    /**
     * Getter for property custType.
     * @return Value of property custType.
     */
    public java.lang.String getCustType() {
        return custType;
    }
    
    /**
     * Setter for property custType.
     * @param custType New value of property custType.
     */
    public void setCustType(java.lang.String custType) {
        this.custType = custType;
    }
    
    }
    
  
    
   