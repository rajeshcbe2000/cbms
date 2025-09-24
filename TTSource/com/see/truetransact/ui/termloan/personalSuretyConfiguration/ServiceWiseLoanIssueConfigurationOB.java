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

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

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
import com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.serviceWiseLoanIssue.ServiceWiseLoanIssueConfigurationTO;
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
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author
 *
 */
public class ServiceWiseLoanIssueConfigurationOB extends CObservable {
    
    private static SqlMap sqlMap = null;
    private final static Logger log = Logger.getLogger(ServiceWiseLoanIssueConfigurationOB.class);
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static ServiceWiseLoanIssueConfigurationOB objPersonalOB;
    private HashMap map;
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private int txtPastServicePeriod= 0;
    private int txtFromAmt = 0;
    private int txtToAmt = 0;
    private int txtNoOfSuretiesRequired = 0;
    private Date tdtEffectFrom = null;
    private String elsi_id="";
    
    
    
    HashMap lookupMap=null;
    private int pan=0;
    private List selectedList=new ArrayList();
    
    private ServiceWiseLoanIssueConfigurationOB generalBodyDetailsOB;
    ServiceWiseLoanIssueConfigurationRB objPersonalRB = new ServiceWiseLoanIssueConfigurationRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private ServiceWiseLoanIssueConfigurationTO objPersonalTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public ServiceWiseLoanIssueConfigurationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ServiceWiseLoanIssueConfigurationJNDI");
            map.put(CommonConstants.HOME, "personalSuretyConfiguration.serviceWiseLoanIssue.ServiceWiseLoanIssueConfigurationHome");
            map.put(CommonConstants.REMOTE, "personalSuretyConfiguration.serviceWiseLoanIssue.ServiceWiseLoanIssueConfiguration");
            //fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    //    private void fillDropdown() throws Exception {
    //        key=new ArrayList();
    //        value= new ArrayList();
    //        //        lookupMap = new HashMap();
    //        //        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
    //        //        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
    //        //        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
    //        //
    //        //               log.info("Inside FillDropDown");
    //        //               lookUpHash = new HashMap();
    //        //               lookUpHash.put(CommonConstants.MAP_NAME, "getComboValues");
    //
    //        //               final ArrayList lookup_keys = new ArrayList();
    //        //               lookup_keys.add("BRANCH_SHIFT");
    //        //               lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
    //        HashMap m=new HashMap();
    //        List comboValues=ClientUtil.executeQuery("getComboValues",m);
    //        System.out.println("Combo VAluuuee List...."+comboValues.size());
    //        if(!comboValues.isEmpty())
    //        {key.add("");
    //         value.add("");
    //         for(int k=0;k<comboValues.size();k++) {
    //             HashMap keys=new HashMap();
    //             keys=(HashMap)comboValues.get(k);
    //             System.out.println("Each Value..."+keys);
    //             key.add(keys.get("AUTHORIZE_REMARK"));
    //             value.add(keys.get("AUTHORIZE_REMARK"));
    //
    //         }
    //
    //        }
    //
    //    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    private HashMap populateDataLocal(HashMap obj)  throws Exception {
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objPersonalOB = new ServiceWiseLoanIssueConfigurationOB();
        } catch(Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():"+e);
        }
    }
    
    
    
    
    ////       public void fillDropdown() throws Exception{
    ////        log.info("In fillDropdown()");
    ////
    ////        lookUpHash = new HashMap();
    ////        lookUpHash.put(CommonConstants.MAP_NAME,null);
    ////        final ArrayList lookup_keys = new ArrayList();
    ////
    //////        lookup_keys.add("PERIOD");
    //////        lookup_keys.add("DEPOSIT.AMT_RANGE_FROM");
    //////        lookup_keys.add("DEPOSIT.AMT_RANGE");
    ////        lookup_keys.add("PRODUCTTYPE");
    ////        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
    ////
    ////        keyValue = ClientUtil.populateLookupData(lookUpHash);
    ////        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
    ////        key.add("BILLS");
    ////        value.add("Bills");
    ////        cbmProdType = new ComboBoxModel(key,value);
    ////
    //////        getKeyValue((HashMap)keyValue.get("PERIOD"));
    //////        cbmFromPeriod = new ComboBoxModel(key,value);
    //////        cbmToPeriod = new ComboBoxModel(key,value);
    //////
    //////        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE_FROM"));
    //////        cbmFromAmount = new ComboBoxModel(key,value);
    //////
    //////        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE"));
    //////        /** To Remove a particular Value From the Combox Model
    //////         */
    //////        int idx=key.indexOf("0");
    //////        key.remove(idx);
    //////        value.remove(idx);
    //////        cbmToAmount = new ComboBoxModel(key,value);
    ////    }
    ////
    
    
    
    
    
    
    public void execute(String command) {
        try {
            //   System.out.println("GET BOPRRR NO IN EDIT :="+geBorrowingTO(command));
            
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("ServiceWiseLoanIssueConfigurationTO", getPersonalTO(command));
            System.out.println("GET term IN EDIT :="+term);
            System.out.println("GET map IN EDIT :="+map);
            HashMap proxyReturnMap = proxy.execute(term, map);
            //            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            System.out.println("ACTIONN TYPEEE==="+getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():"+e);
        }
    }
    
    
    
    private ServiceWiseLoanIssueConfigurationTO getPersonalTO(String command){
        ServiceWiseLoanIssueConfigurationTO objTO = new ServiceWiseLoanIssueConfigurationTO();
        objTO.setCommand(command);
        
        objTO.setEffectFrom(getTdtEffectFrom());
        objTO.setPastServicePeriod(getTxtPastServicePeriod());
        objTO.setToAmt(getTxtToAmt());
        objTO.setFromAmt(getTxtFromAmt());
        objTO.setNoOfsuretiesReq(getTxtNoOfSuretiesRequired());
        objTO.setSelectedList(getSelectedList());
        objTO.setElsi_id(getElsi_id());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objTO.setStatus("CREATED");
        }
        
        return objTO;
    }
    
    
    public void setPersonalTO(ServiceWiseLoanIssueConfigurationTO objTO) {
        // setTxtPersonalID(objTO.getGbid());
        System.out.println("objTO.getEffectFrom()objTO.getEffectFrom()"+objTO.getEffectFrom());
        setTdtEffectFrom(objTO.getEffectFrom());
        setTxtPastServicePeriod(objTO.getPastServicePeriod());
        setTxtFromAmt(objTO.getFromAmt());
        setTxtToAmt(objTO.getToAmt());
        setTxtNoOfSuretiesRequired(objTO.getNoOfsuretiesReq());
        //stateChanged();
        notifyObservers();
        
    }
    
    
    public void resetForm(){
        //  setTxtCloseBefore("");
        setTxtPastServicePeriod(0);
        setTxtFromAmt(0);
        setTxtToAmt(0);
        setTxtNoOfSuretiesRequired(0);
        setTdtEffectFrom(null);
        
    }
    
    
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    
    
    
    public static ServiceWiseLoanIssueConfigurationOB getInstance()throws Exception{
        return objPersonalOB;
    }
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    //     private void getKeyValue(HashMap keyValue)  throws Exception{
    //        key = (ArrayList)keyValue.get(CommonConstants.KEY);
    //        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    //    }
    //
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    /** To update the Status based on result performed by doAction() method */
    
    public int getResult(){
        return _result;
    }
    
    
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            System.out.println("whereMap=="+whereMap);
            System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+((List) mapData.get("ServiceWiseLoanIssueConfigurationTO")).get(0));
            ServiceWiseLoanIssueConfigurationTO objTO =(ServiceWiseLoanIssueConfigurationTO) ((List) mapData.get("ServiceWiseLoanIssueConfigurationTO")).get(0);
            System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setSelectedList((List)mapData.get("list1"));
            setPersonalTO(objTO);
            System.out.println("getSelectedList()getSelectedList()"+getSelectedList());
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():"+e);
            
        }
    }
    
    
    /**
     * Getter for property selectedList.
     * @return Value of property selectedList.
     */
    public List getSelectedList() {
        return selectedList;
    }
    
    /**
     * Setter for property selectedList.
     * @param selectedList New value of property selectedList.
     */
    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }
    
    /**
     * Getter for property pan.
     * @return Value of property pan.
     */
    public int getPan() {
        return pan;
    }
    
    /**
     * Setter for property pan.
     * @param pan New value of property pan.
     */
    public void setPan(int pan) {
        this.pan = pan;
    }
    
    /**
     * Getter for property txtPastServicePeriod.
     * @return Value of property txtPastServicePeriod.
     */
    
    /**
     * Setter for property txtPastServicePeriod.
     * @param txtPastServicePeriod New value of property txtPastServicePeriod.
     */
    //    public void setTxtPastServicePeriod(int txtPastServicePeriod) {
    //        this.txtPastServicePeriod = txtPastServicePeriod;
    //    }
    
    /**
     * Getter for property txtFromAmt.
     * @return Value of property txtFromAmt.
     */
    public int getTxtFromAmt() {
        return txtFromAmt;
    }
    
    /**
     * Setter for property txtFromAmt.
     * @param txtFromAmt New value of property txtFromAmt.
     */
    public void setTxtFromAmt(int txtFromAmt) {
        this.txtFromAmt = txtFromAmt;
    }
    
    /**
     * Getter for property txtToAmt.
     * @return Value of property txtToAmt.
     */
    public int getTxtToAmt() {
        return txtToAmt;
    }
    
    /**
     * Setter for property txtToAmt.
     * @param txtToAmt New value of property txtToAmt.
     */
    public void setTxtToAmt(int txtToAmt) {
        this.txtToAmt = txtToAmt;
    }
    
    /**
     * Getter for property tdtEffectFrom.
     * @return Value of property tdtEffectFrom.
     */
    public Date getTdtEffectFrom() {
        return tdtEffectFrom;
    }
    
    /**
     * Setter for property tdtEffectFrom.
     * @param tdtEffectFrom New value of property tdtEffectFrom.
     */
    public void setTdtEffectFrom(Date tdtEffectFrom) {
        this.tdtEffectFrom = tdtEffectFrom;
    }
    
    /**
     * Getter for property txtPastServicePeriod.
     * @return Value of property txtPastServicePeriod.
     */
    public int getTxtPastServicePeriod() {
        return txtPastServicePeriod;
    }
    
    /**
     * Setter for property txtPastServicePeriod.
     * @param txtPastServicePeriod New value of property txtPastServicePeriod.
     */
    public void setTxtPastServicePeriod(int txtPastServicePeriod) {
        this.txtPastServicePeriod = txtPastServicePeriod;
    }
    
    /**
     * Getter for property txtNoOfSuretiesRequired.
     * @return Value of property txtNoOfSuretiesRequired.
     */
    public int getTxtNoOfSuretiesRequired() {
        return txtNoOfSuretiesRequired;
    }
    
    /**
     * Setter for property txtNoOfSuretiesRequired.
     * @param txtNoOfSuretiesRequired New value of property txtNoOfSuretiesRequired.
     */
    public void setTxtNoOfSuretiesRequired(int txtNoOfSuretiesRequired) {
        this.txtNoOfSuretiesRequired = txtNoOfSuretiesRequired;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property elsi_id.
     * @return Value of property elsi_id.
     */
    public String getElsi_id() {
        return elsi_id;
    }
    
    /**
     * Setter for property elsi_id.
     * @param elsi_id New value of property elsi_id.
     */
    public void setElsi_id(String elsi_id) {
        this.elsi_id = elsi_id;
    }
    
}





