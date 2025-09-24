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
import com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.servicewise.ServiceWiseLoanAmountTO;
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
public class ServiceWiseLoanAmountOB extends CObservable {
    
    private String txtFromServicePeriod = "";
    private String txtToServicePeriod = "";
    private String txtTotServicePeriod = "";
    private String txtTotNoOfSureity = "";
    private String txtMinimumLoanAmount = "";
    private String txtMaximumLoanAmount = "";
    private Date tdtFromDate = null;
    //    private String txaremarks = "";
    //    private String tdtDate = "";
    private String txtPersonalID="";
    //  private String cboProdType="";
    //  private ComboBoxModel cbmProdType;
    private static SqlMap sqlMap = null;
    private final static Logger log = Logger.getLogger(ServiceWiseLoanAmountOB.class);
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static ServiceWiseLoanAmountOB objPersonalOB;
    private HashMap map;
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    // private ArrayList key;
    //    private ArrayList value;
    HashMap lookupMap=null;
    
    //private int pan=0;
    private List selectedList=new ArrayList();
    
    
    ////    private String noOfTrainees = "";
    ////    final ArrayList tableTitle = new ArrayList();
    ////    private ArrayList IncVal = new ArrayList();
    ////    private EnhancedTableModel tblEmpDetails;
    //    private static SqlMap sqlMap = null;
    //    private boolean newData = false;
    //    private LinkedHashMap incParMap;
    //    private LinkedHashMap deletedTableMap;
    //    private String txtSlNo="";
    //    private String subj="";
    //
    //    private final static Logger log = Logger.getLogger(GeneralBodyDetailsOB.class);
    //    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private HashMap map;
    //    private ProxyFactory proxy;
    //    //for filling dropdown
    //    private HashMap lookupMap;
    //    private HashMap param;
    //    private HashMap keyValue;
    //    private ArrayList key;
    //    private ArrayList value;
    //    private int actionType;
    private ServiceWiseLoanAmountOB generalBodyDetailsOB;
    ServiceWiseLoanAmountRB objPersonalRB = new ServiceWiseLoanAmountRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private ServiceWiseLoanAmountTO objPersonalTO = null;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public ServiceWiseLoanAmountOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ServiceWiseLoanAmountJNDI");
            map.put(CommonConstants.HOME, "personalSuretyConfiguration.servicewise.ServiceWiseLoanAmountHome");
            map.put(CommonConstants.REMOTE, "personalSuretyConfiguration.servicewise.ServiceWiseLoanAmount");
            
            initUIComboBoxModel();
            //  fillDropdown();
            //            setTableTile();
            //            tblEmpDetails = new EnhancedTableModel(null, tableTitle);
            //            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void initUIComboBoxModel() {
        //        cbmProdType = new ComboBoxModel();
    }
    
     /* private void fillDropdown() throws Exception
    {
        key=new ArrayList();
        value= new ArrayList();
//        lookupMap = new HashMap();
//        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
//        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
//        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//
//               log.info("Inside FillDropDown");
//               lookUpHash = new HashMap();
//               lookUpHash.put(CommonConstants.MAP_NAME, "getComboValues");
      
//               final ArrayList lookup_keys = new ArrayList();
//               lookup_keys.add("BRANCH_SHIFT");
//               lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        HashMap m=new HashMap();
               List comboValues=ClientUtil.executeQuery("getComboValues",m);
               System.out.println("Combo VAluuuee List...."+comboValues.size());
               if(!comboValues.isEmpty())
               {key.add("");
                value.add("");
                   for(int k=0;k<comboValues.size();k++)
                   {
                       HashMap keys=new HashMap();
                       keys=(HashMap)comboValues.get(k);
                       System.out.println("Each Value..."+keys);
                       key.add(keys.get("AUTHORIZE_REMARK"));
                       value.add(keys.get("AUTHORIZE_REMARK"));
      
                   }
      
               }
             // HashMap lookupValues = ClientUtil.populateLookupData(lookUpHash);
      
//        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
//        cbmProdType=new ComboBoxModel(key,value);
      
    }
      */
    
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
            objPersonalOB = new ServiceWiseLoanAmountOB();
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
            term.put("ServiceWiseLoanAmountTO", getPersonalTO(command));
            System.out.println("GET term IN EDIT :="+term);
            System.out.println("GET term IN EDIT :="+term.get("ServiceWiseLoanAmountTO"));
            System.out.println("GET map IN EDIT :="+map);
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            System.out.println("ACTIONN TYPEEE==="+getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():"+e);
        }
    }
    
    
    
    private ServiceWiseLoanAmountTO getPersonalTO(String command){
        ServiceWiseLoanAmountTO objTO = new ServiceWiseLoanAmountTO();
        objTO.setCommand(command);
        //  if(getPan()==1)
        //  {
        objTO.setGbid(getTxtPersonalID());
        //        objTO.setMaxSurety(getTxtMaxSurety());
        //        objTO.setCloseBefore(getTxtCloseBefore());
        //  objTO.setPan(1);
        // }
        // if(getPan()==2)
        // {
        //           objTO.setProdType(getCboProdType());
        
        objTO.setMaximumLoanAmount(CommonUtil.convertObjToDouble(getTxtMaximumLoanAmount()));
        objTO.setMinimumLoanAmount(CommonUtil.convertObjToDouble(getTxtMinimumLoanAmount()));
        objTO.setFromDate(getTdtFromDate());
        objTO.setFromServicePeriod(CommonUtil.convertObjToDouble(getTxtFromServicePeriod()));
        objTO.setToServicePeriod(CommonUtil.convertObjToDouble(getTxtToServicePeriod()));
        objTO.setTotServicePeriodRequired(CommonUtil.convertObjToDouble(getTxtTotServicePeriod()));
        objTO.setTotSecurityRequired(CommonUtil.convertObjToDouble(getTxtTotNoOfSureity()));       
        System.out.println("getSelectedList()"+getSelectedList());
        objTO.setSelectedList(getSelectedList());
        //   objTO.setPan(2);
        //   }
        
        
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            //                objTO.setAuthorizeStatus("");
            //                objTO.setAuthorizeBy("");
            //                objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
        }
        
        return objTO;
    }
    
    
    public void setPersonalTO(ServiceWiseLoanAmountTO objTO) {
        System.out.println("obbbb iddd>>"+objTO.getGbid());
        setTxtPersonalID(objTO.getGbid());
        setTdtFromDate(objTO.getFromDate());
        setTxtMaximumLoanAmount(CommonUtil.convertObjToStr(objTO.getMaximumLoanAmount()));
        setTxtMinimumLoanAmount(CommonUtil.convertObjToStr(objTO.getMinimumLoanAmount()));
        setTxtFromServicePeriod(CommonUtil.convertObjToStr(objTO.getFromServicePeriod()));
        setTxtToServicePeriod(CommonUtil.convertObjToStr(objTO.getToServicePeriod()));
        setTxtTotNoOfSureity(CommonUtil.convertObjToStr(objTO.getTotSecurityRequired()));
        setTxtTotServicePeriod(CommonUtil.convertObjToStr(objTO.getTotServicePeriodRequired()));
        setSelectedList(objTO.getSelectedList());
        //        setTxtMaxSurety(objTO.getMaxSurety());
        //        setTxtCloseBefore(objTO.getCloseBefore());
        //        int k=objTO.getTotalAttendance();
        //        Integer K=k;
        //        setTxttotalAttendance(K.toString());
        //        setTxtremarks(objTO.getRemarks());
        //            objGeneralBodyDetailsTO.setgDate(gettdtDate());
        //            objGeneralBodyDetailsTO.setVenu(getTxtVenu());
        //            objGeneralBodyDetailsTO.setTotalAttendance(getTxttotalAttendance());
        //            objGeneralBodyDetailsTO.setRemarks(getTxtremarks());
        //    //
        //            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
        //              objEmpTrainingTO.setEmpTrainingID(getTxtEmpTrainingID());
        //            }
        
        //  setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
        //       setTxtCloseBefore("");
        //       setTxtMaxSurety("");
        setTxtFromServicePeriod("");
        setTxtMaximumLoanAmount("");
        setTxtToServicePeriod("");
        setTdtFromDate(null);
        setTxtMinimumLoanAmount("");
    }
    
    
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    
    
    
    public static ServiceWiseLoanAmountOB getInstance()throws Exception{
        return objPersonalOB;
    }
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
    public void setStatus(){
        // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
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
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
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
            System.out.println("objmapData=="+((List) mapData.get("ServiceWiseLoanAmountTO")).get(0));
            
            objPersonalTO =(ServiceWiseLoanAmountTO)((List) mapData.get("ServiceWiseLoanAmountTO")).get(0);
            System.out.println("objTOobjTOobjTOobjTO=="+objPersonalTO);
            
         //   mapData = proxy.executeQuery(whereMap, map);
         //   System.out.println("objmapData=="+((List) mapData.get("PersonalSuretyConfigurationTO")).get(0));
          //  PersonalSuretyConfigurationTO objTO =(PersonalSuretyConfigurationTO) ((List) mapData.get("PersonalSuretyConfigurationTO")).get(0);
        //   System.out.println("objTOobjTOobjTOobjTO=="+objTO);
           if(mapData.containsKey("selectedList"))
              objPersonalTO.setSelectedList((List)mapData.get("selectedList"));
           System.out.println("  objTO.setSelectedList"+  objPersonalTO.getSelectedList());
           // setPersonalTO(objTO);
            setPersonalTO(objPersonalTO);
            ttNotifyObservers();
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():"+e);
            
        }
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /**
     * Getter for property txtPersonalID.
     * @return Value of property txtPersonalID.
     */
    public String getTxtPersonalID() {
        return txtPersonalID;
    }
    
    /**
     * Setter for property txtPersonalID.
     * @param txtPersonalID New value of property txtPersonalID.
     */
    public void setTxtPersonalID(String txtPersonalID) {
        this.txtPersonalID = txtPersonalID;
    }
    
    
    /**
     * Getter for property txtMaximumLoanAmount.
     * @return Value of property txtMaximumLoanAmount.
     */
    public String getTxtMaximumLoanAmount() {
        return txtMaximumLoanAmount;
    }
    
    /**
     * Setter for property txtMaximumLoanAmount.
     * @param txtMaximumLoanAmount New value of property txtMaximumLoanAmount.
     */
    public void setTxtMaximumLoanAmount(String txtMaximumLoanAmount) {
        this.txtMaximumLoanAmount = txtMaximumLoanAmount;
    }

    public String getTxtTotNoOfSureity() {
        return txtTotNoOfSureity;
    }

    public void setTxtTotNoOfSureity(String txtTotNoOfSureity) {
        this.txtTotNoOfSureity = txtTotNoOfSureity;
    }

    public String getTxtTotServicePeriod() {
        return txtTotServicePeriod;
    }

    public void setTxtTotServicePeriod(String txtTotServicePeriod) {
        this.txtTotServicePeriod = txtTotServicePeriod;
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
     * Getter for property txtFromServicePeriod.
     * @return Value of property txtFromServicePeriod.
     */
    public String getTxtFromServicePeriod() {
        return txtFromServicePeriod;
    }
    
    /**
     * Setter for property txtFromServicePeriod.
     * @param txtFromServicePeriod New value of property txtFromServicePeriod.
     */
    public void setTxtFromServicePeriod(String txtFromServicePeriod) {
        this.txtFromServicePeriod = txtFromServicePeriod;
    }
    
    /**
     * Getter for property txtToServicePeriod.
     * @return Value of property txtToServicePeriod.
     */
    public String getTxtToServicePeriod() {
        return txtToServicePeriod;
    }
    
    /**
     * Setter for property txtToServicePeriod.
     * @param txtToServicePeriod New value of property txtToServicePeriod.
     */
    public void setTxtToServicePeriod(String txtToServicePeriod) {
        this.txtToServicePeriod = txtToServicePeriod;
    }
    
    /**
     * Getter for property txtMinimumLoanAmount.
     * @return Value of property txtMinimumLoanAmount.
     */
    public String getTxtMinimumLoanAmount() {
        return txtMinimumLoanAmount;
    }
    
    /**
     * Setter for property txtMinimumLoanAmount.
     * @param txtMinimumLoanAmount New value of property txtMinimumLoanAmount.
     */
    public void setTxtMinimumLoanAmount(String txtMinimumLoanAmount) {
        this.txtMinimumLoanAmount = txtMinimumLoanAmount;
    }
    
    /**
     * Getter for property tdtFromDate.
     * @return Value of property tdtFromDate.
     */
    public Date getTdtFromDate() {
        return tdtFromDate;
    }
    
    /**
     * Setter for property tdtFromDate.
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    
}





