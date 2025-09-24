/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterOB.java
 *
 * Created on Mon Jun 20 16:52:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.suspenseaccount;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountProductTO;
import org.apache.log4j.Logger;
/**
 *
 * @author
 */

public class SuspenseAccountProductOB extends CObservable{
    
    
    private String txtSuspenseProductHead;
    private String txtSuspenseProdName;
    private String txtSuspenseProdID;
    private String txtPrefix;
    private String chkNegBalnce;
    
    private static SuspenseAccountProductOB objSuspenseAccountProductOB;
    private ComboBoxModel cbmUnit;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(SuspenseAccountProductOB.class);
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType=0;
    private int result=0;
    HashMap data = new HashMap();
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private HashMap map;
    private ProxyFactory proxy = null;
    
    private Date currDt = null;
    private String chkLoanBehaviour = "N";
    private String txtIntAcHd = "";
    private String txtIntRate = "";
    
    
    private SuspenseAccountProductOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SuspenseAccountProductJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.SuspenseAccountProductHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.SuspenseAccountProduct");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
//            setBillsChargesTab();
//            tblBillsCharges=new EnhancedTableModel(null,billsChargeTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objSuspenseAccountProductOB = new SuspenseAccountProductOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    /**
     * Returns an instance of PaddyLocalityMasterOB.
     * @return  PaddyLocalityMasterOB
     */
    
    public static SuspenseAccountProductOB getInstance()throws Exception{
        return objSuspenseAccountProductOB;
    }
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PADDY.UNITS");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PADDY.UNITS"));
            cbmUnit= new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetForm(){
        setTxtSuspenseProdID("");
        setTxtSuspenseProdName("");
        setTxtSuspenseProductHead("");
        setTxtPrefix("");
        setChkNegBalnce("N");
        setChkLoanBehaviour("N");
        setTxtIntAcHd("");
        setTxtIntRate("");
        ttNotifyObservers();
    }
    public void ttNotifyObservers(){
        notifyObservers();
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
      /* Executes Query using the TO object */
    public void doAction() {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.MODULE, getModule()); 
            dataMap.put(CommonConstants.SCREEN, getScreen());
            dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            String command = getCommand();
            dataMap.put("SuspenseAccountProductTO", getSuspenseAccountProductTO(command));
            System.out.println("@#$@#$dataMap:"+dataMap);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            setResult(actionType);
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
            dataMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* To get the type of command */
    private String getCommand() throws Exception{
        String command = null;
        switch (getActionType()) {
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
        return command;
    }
    public void authorizeSuspenseMaster(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    private SuspenseAccountProductTO getSuspenseAccountProductTO (String command) {
        SuspenseAccountProductTO objSuspenseAccountProductTO = new SuspenseAccountProductTO();
        objSuspenseAccountProductTO.setTxtSuspenseProdID(getTxtSuspenseProdID());
        objSuspenseAccountProductTO.setTxtSuspenseProdName(getTxtSuspenseProdName());
        objSuspenseAccountProductTO.setTxtSuspenseProductHead(getTxtSuspenseProductHead());
        objSuspenseAccountProductTO.setTxtPrefix(getTxtPrefix());
        objSuspenseAccountProductTO.setCommand (command);
        objSuspenseAccountProductTO.setCreatedBy(TrueTransactMain.USER_ID);
        objSuspenseAccountProductTO.setCreatedDt(currDt);
        objSuspenseAccountProductTO.setStatusBy(TrueTransactMain.USER_ID);
        objSuspenseAccountProductTO.setStatusDt(currDt);
        objSuspenseAccountProductTO.setChkNegBalnce(getChkNegBalnce());
        objSuspenseAccountProductTO.setLoanBehaviour(getChkLoanBehaviour());
        objSuspenseAccountProductTO.setIntAcHd(getTxtIntAcHd());
        objSuspenseAccountProductTO.setIntRate(CommonUtil.convertObjToDouble(getTxtIntRate()));
        System.out.println("@#$@#$@#objSuspenseAccountProductTO"+objSuspenseAccountProductTO);
        return objSuspenseAccountProductTO;
    }
    
    private void setSuspenseAccountProductTO(SuspenseAccountProductTO objSuspenseAccountProductTO) {
        setTxtSuspenseProdID(objSuspenseAccountProductTO.getTxtSuspenseProdID());
        setTxtSuspenseProdName(objSuspenseAccountProductTO.getTxtSuspenseProdName());
        setTxtSuspenseProductHead(objSuspenseAccountProductTO.getTxtSuspenseProductHead());
        setTxtPrefix(objSuspenseAccountProductTO.getTxtPrefix());
        setChkNegBalnce(objSuspenseAccountProductTO.getChkNegBalnce());
        setChkLoanBehaviour(objSuspenseAccountProductTO.getLoanBehaviour());
        setTxtIntAcHd(objSuspenseAccountProductTO.getIntAcHd());
        setTxtIntRate(CommonUtil.convertObjToStr(objSuspenseAccountProductTO.getIntRate()));
    }
    
    public void  getData(HashMap whereMap){
        try{
            System.out.println("#@$%#$%#$%map:"+map);
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$@#data : "+data+ " : "+whereMap+ " : "+map);
            if(data.containsKey("SuspenseAccountProductTO")){
                SuspenseAccountProductTO objSuspenseAccountProductTO = (SuspenseAccountProductTO)data.get("SuspenseAccountProductTO");
                System.out.println("@#$@#SuspenseAccountProductTO:"+objSuspenseAccountProductTO);
                setSuspenseAccountProductTO(objSuspenseAccountProductTO);
                ttNotifyObservers();
            }
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }

     public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
    
         /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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
     * Getter for property txtSuspenseProductHead.
     * @return Value of property txtSuspenseProductHead.
     */
    public java.lang.String getTxtSuspenseProductHead() {
        return txtSuspenseProductHead;
    }
    
    /**
     * Setter for property txtSuspenseProductHead.
     * @param txtSuspenseProductHead New value of property txtSuspenseProductHead.
     */
    public void setTxtSuspenseProductHead(java.lang.String txtSuspenseProductHead) {
        this.txtSuspenseProductHead = txtSuspenseProductHead;
    }
    
    /**
     * Getter for property txtSuspenseProdName.
     * @return Value of property txtSuspenseProdName.
     */
    public java.lang.String getTxtSuspenseProdName() {
        return txtSuspenseProdName;
    }
    
    /**
     * Setter for property txtSuspenseProdName.
     * @param txtSuspenseProdName New value of property txtSuspenseProdName.
     */
    public void setTxtSuspenseProdName(java.lang.String txtSuspenseProdName) {
        this.txtSuspenseProdName = txtSuspenseProdName;
    }
    
    /**
     * Getter for property txtSuspenseProdID.
     * @return Value of property txtSuspenseProdID.
     */
    public java.lang.String getTxtSuspenseProdID() {
        return txtSuspenseProdID;
    }
    
    /**
     * Setter for property txtSuspenseProdID.
     * @param txtSuspenseProdID New value of property txtSuspenseProdID.
     */
    public void setTxtSuspenseProdID(java.lang.String txtSuspenseProdID) {
        this.txtSuspenseProdID = txtSuspenseProdID;
    }
    
    /**
     * Getter for property txtPrefix.
     * @return Value of property txtPrefix.
     */
    public String getTxtPrefix() {
        return txtPrefix;
    }
    
    /**
     * Setter for property txtPrefix.
     * @param txtPrefix New value of property txtPrefix.
     */
    public void setTxtPrefix(String txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public String getChkNegBalnce() {
        return chkNegBalnce;
    }

    public void setChkNegBalnce(String chkNegBalnce) {
        this.chkNegBalnce = chkNegBalnce;
    }

    public String getChkLoanBehaviour() {
        return chkLoanBehaviour;
    }

    public void setChkLoanBehaviour(String chkLoanBehaviour) {
        this.chkLoanBehaviour = chkLoanBehaviour;
    }

    public String getTxtIntAcHd() {
        return txtIntAcHd;
    }

    public void setTxtIntAcHd(String txtIntAcHd) {
        this.txtIntAcHd = txtIntAcHd;
    }

    public String getTxtIntRate() {
        return txtIntRate;
    }

    public void setTxtIntRate(String txtIntRate) {
        this.txtIntRate = txtIntRate;
    }
    
}