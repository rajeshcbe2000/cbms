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

package com.see.truetransact.ui.sysadmin.fixedassets;

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
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetsTO;
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
public class FixedAssetsOB extends CObservable {
    private String assetID = "";
    private String assetType = "";
    private String assetDesc = "";
    private String provision = "";
    private String CurValRoundOff = "";
    private String purchaseDebit = "";
    private String provDebit = "";
    private String sellingAcID="";
    private String txtStatus = "";
    private String CreatedDt="";
    private String nullifyingCredit="";
    private String nullifyingDebit="";
    private String txtStatusBy="";
    private String rdoDepYesNo ="";
    private String Depreciation ="";
    private String cboRoundOffType="";
    private ComboBoxModel cbmRoundOffType;
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtNewAcctName = "";
    
    private final static Logger log = Logger.getLogger(FixedAssetsOB.class);
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
    private FixedAssetsOB fixedAssetsOB;
    FixedAssetsRB objFixedAssetsRB = new FixedAssetsRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private FixedAssetsTO objFixedAssetsTO;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public FixedAssetsOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FixedAssetsJNDI");
            map.put(CommonConstants.HOME, "FixedAssetsHome");
            map.put(CommonConstants.REMOTE, "FixedAssets");
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
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        cbmRoundOffType = new ComboBoxModel(key,value);
        
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
        final FixedAssetsTO objFixedAssetsTO = new FixedAssetsTO();
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("FixedAssets",setFixedAssetsData());
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("FIXED_ASSET_ID")));
        }
        setResult(getActionType());
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
            objFixedAssetsTO = (FixedAssetsTO) ((List) data.get("FixedAssetsTO")).get(0);
            populateFixedAssetData(objFixedAssetsTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public FixedAssetsTO setFixedAssetsData() {
        
        final FixedAssetsTO objFixedAssetsTO = new FixedAssetsTO();
        try{
            objFixedAssetsTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objFixedAssetsTO.setStatusBy(ProxyParameters.USER_ID);
            objFixedAssetsTO.setAssetType(getAssetType());
            objFixedAssetsTO.setAssetDesc(getAssetDesc());
            objFixedAssetsTO.setCommand(getCommand());
            objFixedAssetsTO.setStatus(getAction());
            objFixedAssetsTO.setStatusBy(TrueTransactMain.USER_ID);
            objFixedAssetsTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objFixedAssetsTO.setNullifyingCredit(getNullifyingCredit());
            objFixedAssetsTO.setNullifyingDebit(getNullifyingDebit());
            objFixedAssetsTO.setProvision(getProvision());
            objFixedAssetsTO.setCurValRoundOff(getCurValRoundOff());
            objFixedAssetsTO.setRoundOffType(CommonUtil.convertObjToStr(getCbmRoundOffType().getKeyForSelected()));
            objFixedAssetsTO.setProvisionDebit(getProvDebit());
            objFixedAssetsTO.setPurchaseDebit(getPurchaseDebit());
            objFixedAssetsTO.setSellingAcID(getSellingAcID());
            objFixedAssetsTO.setRdoDepYesNo(getRdoDepYesNo());
            
            if(getCommand().equalsIgnoreCase("INSERT")){
                objFixedAssetsTO.setCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetsTO.setCreatedDt(currDt);
            }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
                objFixedAssetsTO.setAssetsID(getAssetID());
            }
        }catch(Exception e){
            log.info("Error In setFixedAssetData()");
            e.printStackTrace();
        }
        return objFixedAssetsTO;
    }
    
    private void populateFixedAssetData(FixedAssetsTO objFixedAssetsTO) throws Exception{
        this.setAssetID(CommonUtil.convertObjToStr(objFixedAssetsTO.getAssetsID()));
        this.setAssetDesc(CommonUtil.convertObjToStr(objFixedAssetsTO.getAssetDesc()));
        this.setAssetType(CommonUtil.convertObjToStr(objFixedAssetsTO.getAssetType()));
        this.setNullifyingCredit(CommonUtil.convertObjToStr(objFixedAssetsTO.getNullifyingCredit()));
        this.setNullifyingDebit(CommonUtil.convertObjToStr(objFixedAssetsTO.getNullifyingDebit()));
        this.setProvDebit(CommonUtil.convertObjToStr(objFixedAssetsTO.getProvisionDebit()));
        this.setSellingAcID(CommonUtil.convertObjToStr(objFixedAssetsTO.getSellingAcID()));
        this.setProvision(CommonUtil.convertObjToStr(objFixedAssetsTO.getProvision()));
        this.setCurValRoundOff(CommonUtil.convertObjToStr(objFixedAssetsTO.getCurValRoundOff()));
        getCbmRoundOffType().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsTO.getRoundOffType()));
        this.setPurchaseDebit(CommonUtil.convertObjToStr(objFixedAssetsTO.getPurchaseDebit()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objFixedAssetsTO.getCreatedDt()));
        this.setStatusBy(CommonUtil.convertObjToStr(objFixedAssetsTO.getStatusBy()));
        this.setRdoDepYesNo(CommonUtil.convertObjToStr(objFixedAssetsTO.getRdoDepYesNo()));
        setChanged();
        notifyObservers();
    }
    
    public void resetForm(){
        setAssetDesc("");
        setAssetID("");
        setAssetType("");
        setNullifyingCredit("");
        setNullifyingDebit("");
        setProvDebit("");
        setProvision("");
        setCurValRoundOff("");
        setCboRoundOffType("");
        setSellingAcID("");
        setRdoDepYesNo("");
        setDepreciation("");
        setPurchaseDebit("");
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
     * Getter for property assetID.
     * @return Value of property assetID.
     */
    public java.lang.String getAssetID() {
        return assetID;
    }
    /**
     * Setter for property assetID.
     * @param assetID New value of property assetID.
     */
    public void setAssetID(java.lang.String assetID) {
        this.assetID = assetID;
    }
    /**
     * Getter for property assetType.
     * @return Value of property assetType.
     */
    public java.lang.String getAssetType() {
        return assetType;
    }
    /**
     * Setter for property assetType.
     * @param assetType New value of property assetType.
     */
    public void setAssetType(java.lang.String assetType) {
        this.assetType = assetType;
    }
    /**
     * Getter for property assetDesc.
     * @return Value of property assetDesc.
     */
    public java.lang.String getAssetDesc() {
        return assetDesc;
    }
    
    /**
     * Setter for property assetDesc.
     * @param assetDesc New value of property assetDesc.
     */
    public void setAssetDesc(java.lang.String assetDesc) {
        this.assetDesc = assetDesc;
    }
    /**
     * Getter for property provision.
     * @return Value of property provision.
     */
    public java.lang.String getProvision() {
        return provision;
    }
    /**
     * Setter for property provision.
     * @param provision New value of property provision.
     */
    public void setProvision(java.lang.String provision) {
        this.provision = provision;
    }
    /**
     * Getter for property purchaseDebit.
     * @return Value of property purchaseDebit.
     */
    public java.lang.String getPurchaseDebit() {
        return purchaseDebit;
    }
    /**
     * Setter for property purchaseDebit.
     * @param purchaseDebit New value of property purchaseDebit.
     */
    public void setPurchaseDebit(java.lang.String purchaseDebit) {
        this.purchaseDebit = purchaseDebit;
    }
    /**
     * Getter for property provDebit.
     * @return Value of property provDebit.
     */
    public java.lang.String getProvDebit() {
        return provDebit;
    }
    /**
     * Setter for property provDebit.
     * @param provDebit New value of property provDebit.
     */
    public void setProvDebit(java.lang.String provDebit) {
        this.provDebit = provDebit;
    }
    /**
     * Getter for property sellingAcID.
     * @return Value of property sellingAcID.
     */
    public java.lang.String getSellingAcID() {
        return sellingAcID;
    }
    /**
     * Setter for property sellingAcID.
     * @param sellingAcID New value of property sellingAcID.
     */
    public void setSellingAcID(java.lang.String sellingAcID) {
        this.sellingAcID = sellingAcID;
    }
    /**
     * Getter for property nullifyingCredit.
     * @return Value of property nullifyingCredit.
     */
    public java.lang.String getNullifyingCredit() {
        return nullifyingCredit;
    }
    /**
     * Setter for property nullifyingCredit.
     * @param nullifyingCredit New value of property nullifyingCredit.
     */
    public void setNullifyingCredit(java.lang.String nullifyingCredit) {
        this.nullifyingCredit = nullifyingCredit;
    }
    /**
     * Getter for property nullifyingDebit.
     * @return Value of property nullifyingDebit.
     */
    public java.lang.String getNullifyingDebit() {
        return nullifyingDebit;
    }
    /**
     * Setter for property nullifyingDebit.
     * @param nullifyingDebit New value of property nullifyingDebit.
     */
    public void setNullifyingDebit(java.lang.String nullifyingDebit) {
        this.nullifyingDebit = nullifyingDebit;
    }
    /**
     * Getter for property rdoDepYesNo.
     * @return Value of property rdoDepYesNo.
     */
    public java.lang.String getRdoDepYesNo() {
        return rdoDepYesNo;
    }
    
    /**
     * Setter for property rdoDepYesNo.
     * @param rdoDepYesNo New value of property rdoDepYesNo.
     */
    public void setRdoDepYesNo(java.lang.String rdoDepYesNo) {
        this.rdoDepYesNo = rdoDepYesNo;
    }
    /**
     * Getter for property Depreciation.
     * @return Value of property Depreciation.
     */
    public java.lang.String getDepreciation() {
        return Depreciation;
    }
    /**
     * Setter for property Depreciation.
     * @param Depreciation New value of property Depreciation.
     */
    public void setDepreciation(java.lang.String Depreciation) {
        this.Depreciation = Depreciation;
    }
    
    /**
     * Getter for property cboRoundOffType.
     * @return Value of property cboRoundOffType.
     */
    public java.lang.String getCboRoundOffType() {
        return cboRoundOffType;
    }
    
    /**
     * Setter for property cboRoundOffType.
     * @param cboRoundOffType New value of property cboRoundOffType.
     */
    public void setCboRoundOffType(java.lang.String cboRoundOffType) {
        this.cboRoundOffType = cboRoundOffType;
    }
    
    /**
     * Getter for property cbmRoundOffType.
     * @return Value of property cbmRoundOffType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoundOffType() {
        return cbmRoundOffType;
    }
    
    /**
     * Setter for property cbmRoundOffType.
     * @param cbmRoundOffType New value of property cbmRoundOffType.
     */
    public void setCbmRoundOffType(com.see.truetransact.clientutil.ComboBoxModel cbmRoundOffType) {
        this.cbmRoundOffType = cbmRoundOffType;
    }
    
    /**
     * Getter for property CurValRoundOff.
     * @return Value of property CurValRoundOff.
     */
    public java.lang.String getCurValRoundOff() {
        return CurValRoundOff;
    }
    
    /**
     * Setter for property CurValRoundOff.
     * @param CurValRoundOff New value of property CurValRoundOff.
     */
    public void setCurValRoundOff(java.lang.String CurValRoundOff) {
        this.CurValRoundOff = CurValRoundOff;
    }
    
}
