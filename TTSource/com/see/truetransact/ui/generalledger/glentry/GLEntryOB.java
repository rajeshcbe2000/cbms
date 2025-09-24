/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryOB.java
 *
 * Created on Tue Jan 04 10:33:32 IST 2005
 */

package com.see.truetransact.ui.generalledger.glentry;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.serverside.transaction.common.product.gl.GLUpdateDAO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author  Ashok Vijayakumar
 */

public class GLEntryOB extends CObservable{
    
    private String txtAmount = "";
    private String txtAcHead = "";
    private String lblDescription = "";
    private String lblStatus;
    private String cboAccountHeadStatus = "";
    private HashMap authorizeMap;
    private ProxyFactory proxy;
    private HashMap map;
    private static GLEntryOB objGLEntryOB;
    private int actionType,result;
    private ComboBoxModel cbmAccountHeadStatus;
    private ArrayList _key;
    private ArrayList _value;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final String DEBIT = "DEBIT";
    private final String CREDIT = "CREDIT";
    private final String AUTHORIZE = "Authorize";
    private Date currDt = null;
    /** Creates new Instance of this class */
    private GLEntryOB() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "GLEntryJNDI");
        map.put(CommonConstants.HOME, "generalledger.glentry.GLEntryHome");
        map.put(CommonConstants.REMOTE,"generalledger.glentry.GLEntry");
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            fillDropdown();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    static {
        try {
            objGLEntryOB = new GLEntryOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Returns an instance of this Class */
    public static GLEntryOB getInstance() {
        return objGLEntryOB;
    }
    
    /*To set data for all dropdowns*/
    private void fillDropdown() throws Exception{
        final HashMap lookUpHash = new HashMap();
        final HashMap keyValue;
        
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("ACHDPARAM.STATUS");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("ACHDPARAM.STATUS"));
        cbmAccountHeadStatus = new ComboBoxModel(_key,_value);
        
        
        makeNull();
    }
    
    /* To make the class variables null*/
    private void makeNull(){
        _key = null;
        _value = null;
    }
    
    /* Splits the keyValue HashMap into _key and _value arraylists*/
    private void getKeyValue(HashMap keyValue) throws Exception{
        _key = (ArrayList)keyValue.get(CommonConstants.KEY);
        _value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // Setter method for txtAmount
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        setChanged();
    }
    // Getter method for txtAmount
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    // Setter method for txtAcHead
    void setTxtAcHead(String txtAcHead){
        this.txtAcHead = txtAcHead;
        setChanged();
    }
    // Getter method for txtAcHead
    String getTxtAcHead(){
        return this.txtAcHead;
    }
    
    /**
     * Getter for property lblDescription.
     * @return Value of property lblDescription.
     */
    public java.lang.String getLblDescription() {
        return lblDescription;
    }
    
    /**
     * Setter for property lblDescription.
     * @param lblDescription New value of property lblDescription.
     */
    public void setLblDescription(java.lang.String lblDescription) {
        this.lblDescription = lblDescription;
        setChanged();
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /** Retrun actionType wheter new,edit or delete */
    public int getActionType(){
        return actionType;
    }
    
    /**
     * Getter for property cboAccountHeadStatus.
     * @return Value of property cboAccountHeadStatus.
     */
    public java.lang.String getCboAccountHeadStatus() {
        return cboAccountHeadStatus;
    }
    
    /**
     * Setter for property cboAccountHeadStatus.
     * @param cboAccountHeadStatus New value of property cboAccountHeadStatus.
     */
    public void setCboAccountHeadStatus(java.lang.String cboAccountHeadStatus) {
        this.cboAccountHeadStatus = cboAccountHeadStatus;
    }
    
    /**
     * Getter for property cbmAccountHeadStatus.
     * @return Value of property cbmAccountHeadStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAccountHeadStatus() {
        return cbmAccountHeadStatus;
    }
    
    /**
     * Setter for property cbmAccountHeadStatus.
     * @param cbmAccountHeadStatus New value of property cbmAccountHeadStatus.
     */
    public void setCbmAccountHeadStatus(com.see.truetransact.clientutil.ComboBoxModel cbmAccountHeadStatus) {
        this.cbmAccountHeadStatus = cbmAccountHeadStatus;
    }
    
    /* Sets the ActionType either to New, Edit, or Delete */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
       /** Resets all the Fields in the UI */
    public void resetForm(){
        setTxtAcHead("");
        setTxtAmount("");
        setLblDescription("");
        setLblStatus("");
        setCboAccountHeadStatus("");
        notifyObservers();
    }
    
    /* Returns an Instance of GLTO */
    private GLTO getObjGLTO(String command)throws Exception{
        GLTO objGLTO = new GLTO();
        objGLTO.setAcHdId(getTxtAcHead());
        objGLTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        if(getBalanceType(getTxtAcHead()).equals(DEBIT)){
            objGLTO.setShadowDebit(CommonUtil.convertObjToDouble(getTxtAmount()));
        }else if(getBalanceType(getTxtAcHead()).equals(CREDIT)){
            objGLTO.setShadowCredit(CommonUtil.convertObjToDouble(getTxtAmount()));
        }
        objGLTO.setLastTransDt(currDt);
        objGLTO.setImplStatus(CommonUtil.convertObjToStr(getCbmAccountHeadStatus().getKeyForSelected()));
        objGLTO.setCommand(command);
        objGLTO.setStatusBy(TrueTransactMain.USER_ID);
        objGLTO.setStatusDt(currDt);
        return objGLTO;
    }
    
    private String getBalanceType(String acHdId)throws Exception{
        HashMap map = new HashMap();
        map.put("AC_HD_ID", acHdId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectBalanceType", map);
        String balanceType =  CommonUtil.convertObjToStr(((HashMap)list.get(0)).get("BALANCETYPE"));
        return balanceType;
    }
    
    
    /** Do the Necessary operation when Save button is clicked in the ui */
    public void doSave(String command){
        try{
            final HashMap data = new HashMap();
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            }else{
               data.put("GLTO", getObjGLTO(command));
            }
            HashMap proxyResultMap = proxy.execute(data,map);
             setResult(actionType);
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    
}