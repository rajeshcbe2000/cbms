/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * DeathMarkingOB.java
 *
 * Created on May 26, 2004, 5:36 PM
 */

package com.see.truetransact.ui.sysadmin.noticereportparameters;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.transferobject.sysadmin.noticereportparameters.NoticePeriodTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.TrueTransactMain;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.sysadmin.noticereportparameters.NoticePeriodTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class NoticePeriodOB extends CObservable{
    
    private String txtCustomerId = "";
    private String txtEnteredData="";
    private String txtReportName="";
    private HashMap map,lookupMap,lookupValues,lblMap;
    private ArrayList key,value,tblDeathMarkingInfoRow;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result;
    private int _actionType;
    private final static Logger _log = Logger.getLogger(NoticePeriodOB.class);
    private static NoticePeriodOB noticePeriodOB;//singleton object
    private EnhancedTableModel tblDeathMarkingModel;
    private ComboBoxModel cbmColName;
    private String cboColName="";
    private ComboBoxModel cbmLan;
    private String cboLan="";
    private ComboBoxModel cbmGrDetails;
    private String cboGrDetails="";
    private String reportHeading="";
    private HashMap _authorizeMap;
    Date curDate = null;
    private ArrayList tblDeathMarkingTitle = new ArrayList();
    private NoticePeriodRB objNoticePeriodRB = new NoticePeriodRB();
    
    /** Creates a new instance of DeathMarkingOB */
    private NoticePeriodOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "NoticePeriodJNDI");
        map.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.noticereportparameters.NoticePeriodHome");
        map.put(CommonConstants.REMOTE,"com.see.truetransact.serverside.sysadmin.noticereportparameters.NoticePeriod");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropDown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
        
    }
    
    static {
        try {
            noticePeriodOB = new NoticePeriodOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static NoticePeriodOB getInstance(){
        return noticePeriodOB;
    }
    
    
    // Setter method for txtCustomerId
    void setTxtCustomerId(String txtCustomerId){
        this.txtCustomerId = txtCustomerId;
        setChanged();
    }
    // Getter method for txtCustomerId
    String getTxtCustomerId(){
        return this.txtCustomerId;
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
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
    
    public int getResult(){
        return _result;
    }
    
    /** Fills the Dropdown by calling up a query */
    private void fillDropDown(){
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            final HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("NOTICE_PERIOD");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("NOTICE_PERIOD"));
            cbmColName = new ComboBoxModel(key,value);
            lookupKey.add("NOTICE_PERIOD_LANGAUGE");
//            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
//            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("NOTICE_PERIOD_LANGAUGE"));
            cbmLan = new ComboBoxModel(key,value);
            lookupKey.add("NOTICE_PERIOD_GUARANTOR");
//            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
//            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("NOTICE_PERIOD_GUARANTOR"));
            cbmGrDetails = new ComboBoxModel(key,value);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Fills the combobox with a model */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    /** Returns the Toobject */
    public NoticePeriodTO getNoticePeriodTO(){
        NoticePeriodTO objNoticePeriodTO = new NoticePeriodTO();
        objNoticePeriodTO.setStatusDt(curDate);
        objNoticePeriodTO.setBranCode(getSelectedBranchID());
        objNoticePeriodTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        objNoticePeriodTO.setDataEntered(getTxtEnteredData());
        objNoticePeriodTO.setReportName(getTxtReportName());
        objNoticePeriodTO.setStatusBy(TrueTransactMain.USER_ID);
        objNoticePeriodTO.setReportCode(getTxtCustomerId());
        objNoticePeriodTO.setLang(CommonUtil.convertObjToStr(getCbmLan().getKeyForSelected()));
        objNoticePeriodTO.setGrDetails(CommonUtil.convertObjToStr(getCbmGrDetails().getKeyForSelected()));
        objNoticePeriodTO.setRepHeading(getReportHeading());
        return objNoticePeriodTO;
        
    }
    
    /** Sets the to object */
    public void setNoticePeriodTO(NoticePeriodTO objNoticePeriodTO){
        setTxtEnteredData(objNoticePeriodTO.getDataEntered());
        setTxtReportName(objNoticePeriodTO.getReportName());
        setTxtCustomerId(objNoticePeriodTO.getReportCode());
        getCbmLan().setKeyForSelected(CommonUtil.convertObjToStr(objNoticePeriodTO.getLang())); 
        setCboLan((String) getCbmLan().getDataForKey(CommonUtil.convertObjToStr(objNoticePeriodTO.getLang())));
        getCbmGrDetails().setKeyForSelected(CommonUtil.convertObjToStr(objNoticePeriodTO.getGrDetails())); 
        setCboGrDetails((String) getCbmGrDetails().getDataForKey(CommonUtil.convertObjToStr(objNoticePeriodTO.getGrDetails())));
        setReportHeading(objNoticePeriodTO.getRepHeading());
        notifyObservers();
    }
    
    /** Resets all the uifields */
    public void resetForm(){
        setTxtCustomerId("");
        setTxtEnteredData("");
        setReportHeading("");
        notifyObservers();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
//                    final DeathMarkingRB objDeathMarkingRB = new DeathMarkingRB();
                    throw new TTException(objNoticePeriodRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To get the value of action performed */
    private String getCommand() throws Exception{
        String command = null;
        switch (_actionType) {
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
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
        }else {
            final NoticePeriodTO objNoticePeriodTO = getNoticePeriodTO();
            objNoticePeriodTO.setCommand(getCommand());
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objNoticePeriodTO.setCreatedBy(TrueTransactMain.USER_ID);
                objNoticePeriodTO.setCreatedDt(curDate);
            }
            data.put("NoticePeriodTO",objNoticePeriodTO);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_NEW;
        resetForm();
    }
    
    /** To retrive Customer Name based on CustomerId*/
    public HashMap getCustomerName(String custId)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", custId);
            List resultList = ClientUtil.executeQuery("getSelectCustomerName", customerMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    /** This will execute a query and returns then as ArrayList, which contains the infromation
     *regarding the customer products which are to be deathmarked */
    public  ArrayList getDeathMarkingInfo(String where)throws Exception{
        HashMap resultMap = null;
        ArrayList resultList = new ArrayList();
        try {
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", where);
            resultList = (ArrayList) ClientUtil.executeQuery("selectDeathMarkInfo", customerMap);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
        return resultList;
    }
    
    
    public  Date getCreatedDate(String custId){
        Date createdDt=null;
        HashMap where = new HashMap();
        where.put("CUST_ID", custId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectCreatedDt", where);
        System.out.println("List "+ list);
        if(list!=null){
            if(list.size()!=0){
                HashMap resultMap = (HashMap) list.get(0);
                if(resultMap!=null){
                    createdDt = (Date)resultMap.get("CREATEDDT");
                }
            }
        }
        
       return createdDt; 
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            NoticePeriodTO objNoticePeriodTO =
            (NoticePeriodTO) ((List) mapData.get("NoticePeriodTO")).get(0);
            setNoticePeriodTO(objNoticePeriodTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /**
     * Getter for property txtEnteredData.
     * @return Value of property txtEnteredData.
     */
    public java.lang.String getTxtEnteredData() {
        return txtEnteredData;
    }    
    
    /**
     * Setter for property txtEnteredData.
     * @param txtEnteredData New value of property txtEnteredData.
     */
    public void setTxtEnteredData(java.lang.String txtEnteredData) {
        this.txtEnteredData = txtEnteredData;
    }
    
    /**
     * Getter for property txtReportName.
     * @return Value of property txtReportName.
     */
    public java.lang.String getTxtReportName() {
        return txtReportName;
    }
    
    /**
     * Setter for property txtReportName.
     * @param txtReportName New value of property txtReportName.
     */
    public void setTxtReportName(java.lang.String txtReportName) {
        this.txtReportName = txtReportName;
    }
    
    /**
     * Getter for property cbmColName.
     * @return Value of property cbmColName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmColName() {
        return cbmColName;
    }
    
    /**
     * Setter for property cbmColName.
     * @param cbmColName New value of property cbmColName.
     */
    public void setCbmColName(com.see.truetransact.clientutil.ComboBoxModel cbmColName) {
        this.cbmColName = cbmColName;
    }
    
    /**
     * Getter for property cboColName.
     * @return Value of property cboColName.
     */
    public java.lang.String getCboColName() {
        return cboColName;
    }    
    
    /**
     * Setter for property cboColName.
     * @param cboColName New value of property cboColName.
     */
    public void setCboColName(java.lang.String cboColName) {
        this.cboColName = cboColName;
    }    
    
    /**
     * Getter for property cbmLan.
     * @return Value of property cbmLan.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLan() {
        return cbmLan;
    }
    
    /**
     * Setter for property cbmLan.
     * @param cbmLan New value of property cbmLan.
     */
    public void setCbmLan(com.see.truetransact.clientutil.ComboBoxModel cbmLan) {
        this.cbmLan = cbmLan;
    }
    
    /**
     * Getter for property cboLan.
     * @return Value of property cboLan.
     */
    public java.lang.String getCboLan() {
        return cboLan;
    }
    
    /**
     * Setter for property cboLan.
     * @param cboLan New value of property cboLan.
     */
    public void setCboLan(java.lang.String cboLan) {
        this.cboLan = cboLan;
    }
    
    /**
     * Getter for property cbmGrDetails.
     * @return Value of property cbmGrDetails.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmGrDetails() {
        return cbmGrDetails;
    }
    
    /**
     * Setter for property cbmGrDetails.
     * @param cbmGrDetails New value of property cbmGrDetails.
     */
    public void setCbmGrDetails(com.see.truetransact.clientutil.ComboBoxModel cbmGrDetails) {
        this.cbmGrDetails = cbmGrDetails;
    }
    
    /**
     * Getter for property cboGrDetails.
     * @return Value of property cboGrDetails.
     */
    public java.lang.String getCboGrDetails() {
        return cboGrDetails;
    }
    
    /**
     * Setter for property cboGrDetails.
     * @param cboGrDetails New value of property cboGrDetails.
     */
    public void setCboGrDetails(java.lang.String cboGrDetails) {
        this.cboGrDetails = cboGrDetails;
    }
    
    /**
     * Getter for property reportHeading.
     * @return Value of property reportHeading.
     */
    public java.lang.String getReportHeading() {
        return reportHeading;
    }
    
    /**
     * Setter for property reportHeading.
     * @param reportHeading New value of property reportHeading.
     */
    public void setReportHeading(java.lang.String reportHeading) {
        this.reportHeading = reportHeading;
    }
    
}
