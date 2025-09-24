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

package com.see.truetransact.ui.indend.suppliermaster;

import com.see.truetransact.ui.indend.suppliermaster.*;
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
import com.see.truetransact.transferobject.indend.suppliermaster.SupplierTO;

/**
 *
 * @author  
 *
 */
public class SupplierOB extends CObservable {
    private String txtSupplierNo = "";
    private String txtName="";
    private String txtTinNo="";
    private String txaAddress="";
    private String txtStatusBy = "";
    private String txtStatus = "";
    private String CreatedDt="";
    private String currBranName="";
    private String suspenseAcHd = "";

    public String getSuspenseAcHd() {
        return suspenseAcHd;
    }

    public void setSuspenseAcHd(String suspenseAcHd) {
        this.suspenseAcHd = suspenseAcHd;
    }
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(SupplierOB.class);
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
    private SupplierOB InwardOB;
    SupplierRB InwardRB = new SupplierRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private SupplierTO objSupplierTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public SupplierOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "SupplierJNDI");
            map.put(CommonConstants.HOME, "suppliermaster.SupplierHome");
            map.put(CommonConstants.REMOTE, "suppliermaster.Supplier");
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
    
    public String getAcHdDesc(String acHdId){
        HashMap map = new HashMap();
        String head = "";
        map.put("AC_HD_ID", acHdId) ;
        List lstLock = ClientUtil.executeQuery("getSupplierAcHdDesc", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            head = CommonUtil.convertObjToStr(map.get("AC_HD_DESC"));
        }
        map = null;
        lstLock = null;
        return head;
        
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
        final SupplierTO objSupplierTO = new SupplierTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("SupplierMaster",setInwardData());
         System.out.println("supplier dataaaaaaaaaaaaa"+data);
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in SupplierMaster OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
         System.out.println("data in SupplierMaster OB return... : " + proxyResultMap);
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
                ClientUtil.showMessageWindow("SUPPLIER NUMBER : " + CommonUtil.convertObjToStr(proxyResultMap.get("SUPPLIER_NO")));
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
            objSupplierTO = (SupplierTO)((List) data.get("SupplyTO")).get(0);
            System.out.println("in ob...return map"+(SupplierTO)((List) data.get("SupplyTO")).get(0));
            populateData(objSupplierTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public SupplierTO setInwardData() {
        
        final SupplierTO objSupplierTO = new SupplierTO();
        try{
            
            //objSupplierTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objSupplierTO.setStatusBy(ProxyParameters.USER_ID);
            objSupplierTO.setSupplierNo(getTxtSupplierNo());
            System.out.println("supplier no....."+objSupplierTO.getSupplierNo());
            objSupplierTO.setName(getTxtName());
         //  objSupplierTO.setIdate(DateUtil.getDateMMDDYYYY(getTxtDate()));
           objSupplierTO.setCommand(getCommand());
           objSupplierTO.setStatus(getAction());
           objSupplierTO.setStatusBy(TrueTransactMain.USER_ID);
           objSupplierTO.setAddress(getTxaAddress());
           objSupplierTO.setTinNo(getTxtTinNo());
         //  objSupplierTO.setReferenceNo(getTxtReferenceNo());
         //  objSupplierTO.setActionTaken(getTxtActionTaken());
          
          // objSupplierTO.setBranCode(null);
           if(getCommand().equalsIgnoreCase("INSERT")){
           objSupplierTO.setCreatedBy(TrueTransactMain.USER_ID);
           objSupplierTO.setCreatedDt(ClientUtil.getCurrentDate());
           objSupplierTO.setAuthorizeBy(null);
           objSupplierTO.setAuthorizeDt(null);
           objSupplierTO.setAuthorizeStatus(null);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objSupplierTO.setEmpTransferID(getTxtEmpTransferID());  
            }
            objSupplierTO.setSuspenseAcHd(getSuspenseAcHd());
        }catch(Exception e){
            log.info("Error In setInwardData()");
            e.printStackTrace();
        }
        return objSupplierTO;
    }
//    
    private void populateData(SupplierTO objSupplierTO) throws Exception{
        this.setTxtSupplierNo((objSupplierTO.getSupplierNo()));
        System.out.println("populate Supplier no...."+getTxtSupplierNo()+"TO---->"+objSupplierTO.getSupplierNo());
        this.setTxaAddress(CommonUtil.convertObjToStr(objSupplierTO.getAddress()));
      //  this.setTxaRemarks(CommonUtil.convertObjToStr(objSupplierTO.getRemarks()));
        this.setTxtName(CommonUtil.convertObjToStr(objSupplierTO.getName()));
        this.setTxtTinNo(CommonUtil.convertObjToStr(objSupplierTO.getTinNo()));
       // this.setTxtActionTaken(CommonUtil.convertObjToStr(objSupplierTO.getActionTaken()));
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objSupplierTO.getStatusBy()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objSupplierTO.getCreatedDt()));
      //  this.setTxtDate(CommonUtil.convertObjToStr(objSupplierTO.getIdate()));
        //this.setTxt(CommonUtil.convertObjToStr(objSupplierTO.getCurrBran()));
        //this.setCurrBranName(CommonUtil.convertObjToStr(objSupplierTO.getCurrBranName()));
        this.setStatusBy(CommonUtil.convertObjToStr(objSupplierTO.getStatusBy()));
        this.setSuspenseAcHd(CommonUtil.convertObjToStr(objSupplierTO.getSuspenseAcHd()));
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
       setTxtName("");
       setTxaAddress("");
       setTxtSupplierNo("");
       setTxtTinNo("");
       setChanged();
       setSuspenseAcHd("");
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
  
    
   
    
    public String getTxtSupplierNo() {
        return txtSupplierNo;
    }

    public void setTxtSupplierNo(String txtSupplierNo) {
        this.txtSupplierNo = txtSupplierNo;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtTinNo() {
        return txtTinNo;
    }

    public void setTxtTinNo(String txtTinNo) {
        this.txtTinNo = txtTinNo;
    }

    public String getTxaAddress() {
        return txaAddress;
    }

    public void setTxaAddress(String txaAddress) {
        this.txaAddress = txaAddress;
    }
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
   
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
  
    
    }
    
  
    
   