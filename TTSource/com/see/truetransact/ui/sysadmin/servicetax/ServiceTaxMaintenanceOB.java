/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ServiceTaxMaintenanceOB.java
 *
 * Created on May 24, 2004, 11:15 AM
 */

package com.see.truetransact.ui.sysadmin.servicetax;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.clientutil.CMandatoryDialog;

import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceGroupTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceCategotyTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceProdTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceRateTO;
import com.see.truetransact.transferobject.sysadmin.servicetax.ServiceTaxMaintenanceTypeTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import java.util.Set;

import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  rahul
 */
public class ServiceTaxMaintenanceOB extends CObservable{
    
    private ProxyFactory proxy = null;
    
    /* To Ste the Titles for the Tables...*/
    ArrayList productTabTitle = new ArrayList();
    ArrayList categoryTabTitle = new ArrayList();
    ArrayList interestTabTitle = new ArrayList();
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private EnhancedTableModel tblProduct;
    private EnhancedTableModel tblCategory;
    private EnhancedTableModel tblInterestTable;
    
    private ComboBoxModel cbmFromPeriod;
    private ComboBoxModel cbmToPeriod;
    private ComboBoxModel cbmFromAmount;
    private ComboBoxModel cbmToAmount;
    private ComboBoxModel cbmProdType;
    
    /* for setting data depending on period comboboxes...*/
    private String duration = "";
    private long periodData = 0;
    private long resultData = 0;
    // for retrieving data from the period comboboxes...
    private long resultValue=0;
    
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    
    private String NO = "NO";
    private String YES = "YES";
    
    
    private ArrayList productTabRow;
    private ArrayList categoryTabRow;
    private ArrayList interestRateTabRow;
    private ArrayList interestTabRow;
    private ArrayList interestTempTabRow;
    
    private ArrayList prodList;
    private ArrayList interestList = new ArrayList();
    private ArrayList interestDeleteTabRow = new ArrayList();
    private ArrayList interestDelete;
    
    private List productTabList;
    private int SerialNo;
    Date curDate = null;
    
    /** Used to check if the Record is Duplicated or not in Interest-Table*/
    private String DATE;
    private String FROMAMOUNT;
    private String TOAMOUNT;
    private String FROMPERIOD;
    private String TOPERIOD;
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private String lblRoiGroupId = "";
    private String interRateType = "";
    private int interRateTabCount;
    private String AuthorizeStatus;
    private String stAcHdId="";
    private String cess1HdId="";
    private String cess2HdId="";
    private String PRODTYPE = "";
    
    public boolean isTableSet = false;
    
    private final static Logger log = Logger.getLogger(ServiceTaxMaintenanceUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** To get the Value of Column Title and Dialogue Box...*/
    //    final ServiceTaxMaintenanceRB objServiceTaxMaintenanceRB = new ServiceTaxMaintenanceRB();
    java.util.ResourceBundle objServiceTaxMaintenanceRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.servicetax.ServiceTaxMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private static ServiceTaxMaintenanceOB serviceTaxMaintenanceOB;
    static {
        try {
            log.info("In InwardClearingOB Declaration");
            serviceTaxMaintenanceOB = new ServiceTaxMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static ServiceTaxMaintenanceOB getInstance() {
        return serviceTaxMaintenanceOB;
    }
    
    /** Creates a new instance of ServiceTaxMaintenanceOB */
    public ServiceTaxMaintenanceOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        fillDropdown();     //__ To Fill all the Combo Boxes
        
        setChargeTabTitle();    //__ To set the Title of Table in Charges Tab...
        
        interestList = new ArrayList();
        interestDeleteTabRow = new ArrayList();
        
        tblProduct = new EnhancedTableModel(null, productTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        tblCategory = new EnhancedTableModel(null, categoryTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        //        tblInterestRate = new EnhancedTableModel(null, interRateTabTitle);
        tblInterestTable = new EnhancedTableModel(null, interestTabTitle);
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ServiceTaxMaintenanceGroupJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.servicetax.ServiceTaxMaintenanceGroupHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.servicetax.ServiceTaxMaintenanceGroup");
    }
    
    /* To set the Column title in Table(s)...*/
    private void setChargeTabTitle() throws Exception{
        log.info("In setChargeTabTitle...");
        
        productTabTitle.add(objServiceTaxMaintenanceRB.getString("tblProd1"));
        productTabTitle.add(objServiceTaxMaintenanceRB.getString("tblProd2"));
        productTabTitle.add(objServiceTaxMaintenanceRB.getString("tblProd3"));
        
        categoryTabTitle.add(objServiceTaxMaintenanceRB.getString("tblCategory1"));
        categoryTabTitle.add(objServiceTaxMaintenanceRB.getString("tblCategory2"));
        categoryTabTitle.add(objServiceTaxMaintenanceRB.getString("tblCategory3"));
        categoryTabTitle.add("Status");
        
        setInterestTabTitle();
    }
    
    public void setInterestTabTitleOA() {
        try{
            interestTabTitle = new ArrayList();
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter1"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter2"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter7"));
            
            tblInterestTable = new EnhancedTableModel(null, interestTabTitle);
            
            setIsTableSet(true);
        }catch(Exception e){
            System.out.println("Error in setChargeTabTitleOA()");
            e.printStackTrace();
        }
    }
    
    public void setInterestTabTitle() {
        try{
            interestTabTitle = new ArrayList();
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter1"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter2"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter3"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter4"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter5"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter6"));
            interestTabTitle.add(objServiceTaxMaintenanceRB.getString("tblInter7"));
            
            tblInterestTable = new EnhancedTableModel(null, interestTabTitle);
            
            setIsTableSet(true);
        }catch(Exception e){
            System.out.println("Error in setChargeTabTitleOA()");
            e.printStackTrace();
        }
        
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PERIOD");
        lookup_keys.add("DEPOSIT.AMT_RANGE_FROM");
        lookup_keys.add("DEPOSIT.AMT_RANGE");
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmFromPeriod = new ComboBoxModel(key,value);
        cbmToPeriod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE_FROM"));
        cbmFromAmount = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE"));
        /** To Remove a particular Value From the Combox Model
         */
        int idx=key.indexOf("0");
        key.remove(idx);
        value.remove(idx);
        cbmToAmount = new ComboBoxModel(key,value);
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue...");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    /** Do display the Data from the Database, in UI
     */
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    /** To retrieve the Data from the HashMap, returned from DAO
     */
    private void populateOB(HashMap mapData) throws Exception{
        ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO = null;
        
        /** Here the first Row is selected...*/
//        objServiceTaxMaintenanceGroupTO = (ServiceTaxMaintenanceGroupTO) ((List) mapData.get("ServiceTaxMaintenanceGroupTO")).get(0);
//        setServiceTaxMaintenanceGroupTO(objServiceTaxMaintenanceGroupTO);
//        setServiceTaxMaintenanceProdTO((List) mapData.get("ServiceTaxMaintenanceProdTO"));
        setProductTabList((List) mapData.get("ServiceTaxMaintenanceProdTO"));
        List achdList=(List)mapData.get("ServiceTaxMaintenanceProdTO");
        HashMap prodMap=new HashMap();
        if(achdList!=null && achdList.size()>0){
        prodMap=(HashMap)achdList.get(0);
        System.out.println("prodMap----------"+prodMap);
        setStAcHdId(CommonUtil.convertObjToStr(prodMap.get("SERVICE_TAX_HD_ID")));
        setCess1HdId(CommonUtil.convertObjToStr(prodMap.get("CESS1_TAX_HD_ID")));
        setCess2HdId(CommonUtil.convertObjToStr(prodMap.get("CESS2_TAX_HD_ID")));
        setLblRoiGroupId(CommonUtil.convertObjToStr(prodMap.get("ROI_GROUP_ID")));
        }
        setServiceTaxMaintenanceCategotyTO((List) mapData.get("ServiceTaxMaintenanceCategotyTO"));
        interestList = (ArrayList)mapData.get("ServiceTaxMaintenanceRateTO");
        
        setInterRateType();
        //        ttNotifyObservers();
    }
    
    /** Called from populateOB() to Display data Related to the Group...
     */
    private void setServiceTaxMaintenanceGroupTO(ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO) throws Exception{
        log.info("In setServiceTaxMaintenanceGroupTO()");
        //__ To Know, what kind or table model to set..
        setPRODTYPE(CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getProductType()));
        setStatusBy(CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getStatusBy()));
        setLblRoiGroupId(CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getRoiGroupId()));
        setCboProdType((String) getCbmProdType().getDataForKey( CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getProductType())));
        setTxtGroupName(CommonUtil.convertObjToStr(objServiceTaxMaintenanceGroupTO.getRoiGroupName()));
        setAuthorizeStatus(objServiceTaxMaintenanceGroupTO.getAuthorizeStatus());
    }
    
    /** Called from populateOB() to Display data Related to the Category
     * in the Category-Table
     */
    private void setServiceTaxMaintenanceCategotyTO(List resultList) throws Exception{
        log.info("In setServiceTaxMaintenanceCategotyTO()");
        ArrayList data = tblCategory.getDataArrayList();
        final int dataSize = data.size();
        final int length = resultList.size();
        String cateId = "";
        for(int j = 0; j < length; j++){
            cateId = CommonUtil.convertObjToStr(((HashMap)resultList.get(j)).get("CATEGORY_ID"));
            for (int i=0;i<dataSize;i++){
                if(tblCategory.getValueAt(i, 1).equals(cateId) ){
                    tblCategory.setValueAt(new Boolean(true), i, 0);
                    tblCategory.setValueAt(new String("MODIFIED"), i, 3);
                }
            }
        }
    }
    
    
    /** To Set the dat from the TransferObjects and set 'em in the OB...
     */
    private void setServiceTaxMaintenanceRateTO(ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO) throws Exception{
        log.info("In setServiceTaxMaintenanceTypeTO()");
       
        setTdtDate(DateUtil.getStringDate(objServiceTaxMaintenanceRateTO.getRoiDate()));
        setTdtToDate(DateUtil.getStringDate(objServiceTaxMaintenanceRateTO.getRoiEndDate()));
        setCboFromAmount((String) getCbmFromAmount().getDataForKey(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getFromAmount())));
        setCboToAmount((String) getCbmToAmount().getDataForKey(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getToAmount())));
        
        resultValue = CommonUtil.convertObjToInt(objServiceTaxMaintenanceRateTO.getFromPeriod());
        String period = setPeriod(resultValue);
        setCboFromPeriod(period);
        setTxtFromPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objServiceTaxMaintenanceRateTO.getToPeriod());
        period = setPeriod(resultValue);
        setCboToPeriod(period);
        setTxtToPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtServiceTax(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getServiceTax()));
        setTxtCess1Tax(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getCess1Tax()));
        
        setTxtAgainstInterest(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getAgainstClearingInt()));
        setTxtLimitAmt(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getLimitAmount()));
        setTxtInterExpiry(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getIntExpiryLimit()));
        setTxtCess2Tax(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getCess2Tax()));
        setTxtTotTax(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getTotTax()));
        setAuthorizeStatus(objServiceTaxMaintenanceRateTO.getAuthorizedStatus());
    }
    
    /** Method to calculate  Years, Months or Days
     * depending on the No. of Days(numerical) value from DataBase...
     * to be displayed in UI
     */
    private String setPeriod(long rV) throws Exception{
        String periodValue;
        if ((rV >= year) && ((rV % year == 0) || (rV % year == 1))) {
            periodValue = YEAR1;
            rV = rV/year;
        } else if (((rV >= month) && ((rV % month == 0)) || ((rV % month == 1))) && rV!=1) {
            periodValue=MONTH1;
            rV = rV/month;
        } else if ((rV >= day) && (rV % day == 0)) {
            periodValue=DAY1;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }
    
    private void resetPeriod(){
        resultValue = 0;
    }
    
    /** To enter the Data into the Database...Called from doActionPerform()...
     */
    private ServiceTaxMaintenanceGroupTO setServiceTaxMaintenanceGroup() {
        log.info("In setServiceTaxMaintenanceGroup()");
        
        final ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO = new ServiceTaxMaintenanceGroupTO();
        try{
//            objServiceTaxMaintenanceGroupTO.setRoiGroupId(lblRoiGroupId);
            objServiceTaxMaintenanceGroupTO.setRoiGroupName(txtGroupName);
//            objServiceTaxMaintenanceGroupTO.setProductType((String)cbmProdType.getKeyForSelected());
                      if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                          objServiceTaxMaintenanceGroupTO.setCreatedBy(TrueTransactMain.USER_ID);
                      }else{
                          objServiceTaxMaintenanceGroupTO.setStatusBy(TrueTransactMain.USER_ID);
                       }
            
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objServiceTaxMaintenanceGroupTO;
    }
    
    
    /** To enter the Data into the Database...Called from doActionPerform()...
     */
    private ServiceTaxMaintenanceCategotyTO setServiceTaxMaintenanceCategoty(String category) {
        log.info("In setServiceTaxMaintenanceCategoty()");
        
        final ServiceTaxMaintenanceCategotyTO objServiceTaxMaintenanceCategotyTO = new ServiceTaxMaintenanceCategotyTO();
        try{
            objServiceTaxMaintenanceCategotyTO.setRoiGroupId(lblRoiGroupId);
            objServiceTaxMaintenanceCategotyTO.setCategoryId(category);
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objServiceTaxMaintenanceCategotyTO;
    }
    
    
    /** To enter the Data into the Database...Called from doActionPerform()...
     */
    private ServiceTaxMaintenanceProdTO setServiceTaxMaintenanceProd(String prodId) {
        log.info("In setServiceTaxMaintenanceProd()");
        
        final ServiceTaxMaintenanceProdTO objServiceTaxMaintenanceProdTO = new ServiceTaxMaintenanceProdTO();
        try{
            objServiceTaxMaintenanceProdTO.setRoiGroupId(lblRoiGroupId);
//            objServiceTaxMaintenanceProdTO.setProdId(prodId);
            objServiceTaxMaintenanceProdTO.setStAcHdId(getStAcHdId());
            objServiceTaxMaintenanceProdTO.setCess1HdId(getCess1HdId());
            objServiceTaxMaintenanceProdTO.setCess2HdId(getCess2HdId());
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objServiceTaxMaintenanceProdTO;
    }
    
    /** To set the Data in the TransferObjects...
     */
    private ServiceTaxMaintenanceRateTO setServiceTaxMaintenanceRate() {
        log.info("In setServiceTaxMaintenanceRate()");
        
        final ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO = new ServiceTaxMaintenanceRateTO();
        try{
            objServiceTaxMaintenanceRateTO.setRoiGroupId(lblRoiGroupId);
            objServiceTaxMaintenanceRateTO.setRateTypeId(getInterRateType());
//            objServiceTaxMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
//            objServiceTaxMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
            Date Dt = DateUtil.getDateMMDDYYYY(tdtDate);
            if(Dt != null){
            Date dtDate = (Date) curDate.clone();
            dtDate.setDate(Dt.getDate());
            dtDate.setMonth(Dt.getMonth());
            dtDate.setYear(Dt.getYear());
            objServiceTaxMaintenanceRateTO.setRoiDate(dtDate);
            }else{
                objServiceTaxMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
            }
            
            Date ToDt = DateUtil.getDateMMDDYYYY(tdtToDate);
            if(ToDt != null){
            Date toDate = (Date) curDate.clone();
            toDate.setDate(ToDt.getDate());
            toDate.setMonth(ToDt.getMonth());
            toDate.setYear(ToDt.getYear());
            objServiceTaxMaintenanceRateTO.setRoiEndDate(toDate);
            }else{
                objServiceTaxMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
            }
            
            objServiceTaxMaintenanceRateTO.setFromAmount(CommonUtil.convertObjToDouble((String)cbmFromAmount.getKeyForSelected()));
            objServiceTaxMaintenanceRateTO.setToAmount(CommonUtil.convertObjToDouble((String)cbmToAmount.getKeyForSelected()));
            
            /** To Convert the Combination of From-Period into Days...
             */
            duration = ((String)cbmFromPeriod.getKeyForSelected());
            periodData = setCombo(duration);
            resultData = periodData * (Long.parseLong(txtFromPeriod));
            objServiceTaxMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            /** To Convert the Combination of To-Period into Days...
             */
            duration = ((String)cbmToPeriod.getKeyForSelected());
            periodData = setCombo(duration);
            resultData = periodData * (Long.parseLong(txtToPeriod));
            objServiceTaxMaintenanceRateTO.setToPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            objServiceTaxMaintenanceRateTO.setServiceTax(CommonUtil.convertObjToDouble(txtServiceTax));
            objServiceTaxMaintenanceRateTO.setCess1Tax(CommonUtil.convertObjToDouble(txtCess1Tax));
            
            objServiceTaxMaintenanceRateTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(txtAgainstInterest));
            objServiceTaxMaintenanceRateTO.setLimitAmount(CommonUtil.convertObjToDouble(txtLimitAmt));
            objServiceTaxMaintenanceRateTO.setIntExpiryLimit(CommonUtil.convertObjToDouble(txtInterExpiry));
            objServiceTaxMaintenanceRateTO.setCess2Tax(CommonUtil.convertObjToDouble(txtCess2Tax));
            objServiceTaxMaintenanceRateTO.setTotTax(CommonUtil.convertObjToDouble(txtTotTax));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objServiceTaxMaintenanceRateTO;
    }
    
    
    
    /** To find the value of multiplier on the basis of period...
     */
    private int setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if(duration.equals(YEAR))
                period = year;
            else if(duration.equals(MONTH))
                period = month;
            else if( duration.equals(DAY))
                period = day;
        }
        duration = "";
        return period;
    }
    /** To check, if the Period Range is proper...
     */
    public int period() {
        int yes = 0;
        try{
            String fromRange = (String)cbmFromPeriod.getKeyForSelected();
            double fromDays = setCombo(fromRange);
            double fromData = fromDays * (Double.parseDouble(txtFromPeriod));
            
            String toRange = (String)cbmToPeriod.getKeyForSelected();
            double toDays = setCombo(toRange);
            double toData = toDays * (Double.parseDouble(txtToPeriod));
            
            if(fromData > toData){
                yes = 1;
            }
        }catch(Exception e){
            log.info("Error in period()");
        }
        return yes;
    }
    
    /** To perform Appropriate operation... Insert, Update, Delete...
     */
    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        try {
            /**If actionType such as NEW, EDIT, DELETE, then proceed
             */
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                /** If actionType has got proper value then doActionPerform, else throw error
                 */
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setInwardClearingTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    
    }
    
     
    
    /** To perform the necessary action
     */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        StringBuffer ctgNames=new StringBuffer();
        /** To Encapsulate the Data as an Object
         */
        final ServiceTaxMaintenanceGroupTO objServiceTaxMaintenanceGroupTO = setServiceTaxMaintenanceGroup();
        /** To Encapsulate the Data as an ArrayList
         */
        final ArrayList arrayServiceTaxMaintenanceCategotyTO = getCategorySelected();
        
        final ArrayList arrayServiceTaxMaintenanceProdTO = getProdSelected();
        
//        final ArrayList arrayServiceTaxMaintenanceTypeTO = getInterRateSelected();
        
        objServiceTaxMaintenanceGroupTO.setCommand(getCommand());
        data.put("ServiceTaxMaintenanceGroupTO",objServiceTaxMaintenanceGroupTO);
        /** Data sent as Arraylist, to the DAO...
         */
        data.put("ServiceTaxMaintenanceCategotyTO",arrayServiceTaxMaintenanceCategotyTO);
       data.put("ServiceTaxMaintenanceProdTO",arrayServiceTaxMaintenanceProdTO);
        
        data.put("ServiceTaxMaintenanceRateTO",interestList);
        
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            data.put("InterestDeleteTabRow",interestDeleteTabRow);
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        //__ Testing...
        sortData();
        //        boolean val = sortData();
        //       if(!val){
        //           System.out.println("Hi! Go for the Lunch...");
        //       }else{
        //           System.out.println("You Should Work Hard...");
        //       }
        
        StringBuffer str = new StringBuffer();
        
//        if(arrayServiceTaxMaintenanceProdTO.size() <= 0){
//            str.append(objServiceTaxMaintenanceRB.getString("PROD_WARNING") + "\n");
//        }
        
        if(arrayServiceTaxMaintenanceCategotyTO.size() <= 0){
            str.append(objServiceTaxMaintenanceRB.getString("CATEGORY_WARNING") + "\n");
        }
        
        if(interestList.size() <= 0){
            str.append(objServiceTaxMaintenanceRB.getString("DATA_WARNING") + "\n");
        }
         if((arrayServiceTaxMaintenanceCategotyTO.size() != 0 && getActionType() == ClientConstants.ACTIONTYPE_EDIT) ||
         (arrayServiceTaxMaintenanceCategotyTO.size() != 0 && getActionType() == ClientConstants.ACTIONTYPE_NEW)){
          int ctgSize=arrayServiceTaxMaintenanceCategotyTO.size();
          List alreadyAvilLst=ClientUtil.executeQuery("getAlreadyAvailCatgoryList",null);
          int alreadyAvilSize=alreadyAvilLst.size();
          
          
          for (int i=0;i<ctgSize;i++){
              ServiceTaxMaintenanceCategotyTO objServiceTaxMaintenanceCategotyTO =new ServiceTaxMaintenanceCategotyTO();
              objServiceTaxMaintenanceCategotyTO=(ServiceTaxMaintenanceCategotyTO)arrayServiceTaxMaintenanceCategotyTO.get(i);
              String CtgNm=CommonUtil.convertObjToStr(objServiceTaxMaintenanceCategotyTO.getCategoryId());
              String CtgStatus=CommonUtil.convertObjToStr(objServiceTaxMaintenanceCategotyTO.getStatus());
              for(int j=0;j<alreadyAvilSize;j++){
                  HashMap ctgNameMap=new HashMap();
                  ctgNameMap=(HashMap)alreadyAvilLst.get(j);
                  if(CtgNm.equals(CommonUtil.convertObjToStr(ctgNameMap.get("CATEGORY_ID"))) && CtgStatus.equals("")){
                       ctgNames.append(objServiceTaxMaintenanceCategotyTO.getCategoryId()+"\n");
                  }
              }

//              ctgNames.append(objServiceTaxMaintenanceCategotyTO.getCategoryId());
          }
          
         }
        
        
        if((actionType != ClientConstants.ACTIONTYPE_DELETE) && str.toString().length() > 0){
            setWarning(str.toString());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
        else if(ctgNames.toString().length()>0){
             setWarning("Already These Charges Service Tax Available "+"\n"+ctgNames.toString());
        }
        
        
        else{
            HashMap proxyResultMap = proxy.execute(data,operationMap);
            setProxyReturnMap(proxyResultMap);
            setResult(actionType);
        }
        //        resetForm();
    }
    
    /** To decide which action Should be performed...
     */
    private String getCommand() throws Exception{
        log.info("In getCommand()");
        
        String command = null;
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
        return command;
    }
    
    private void setWarning(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    /** To Get the Values from the Product Table,
     * to be added into the TO objects...
     */
    public ArrayList getProdSelected() {
        Boolean bln;
        ArrayList arrRow;
        ArrayList prodId = new ArrayList();
        ArrayList tabData  =  tblProduct.getDataArrayList();
        
        ArrayList selectedList = new ArrayList();
//        for (int i=0, j=tblProduct.getRowCount(); i < j; i++) {
//            bln = (Boolean) tblProduct.getValueAt(i, 0);
//            if (bln.booleanValue() == true) {
//                arrRow = (ArrayList)tabData.get(i);
                final String PRODID = "";// CommonUtil.convertObjToStr(arrRow.get(1));
                ServiceTaxMaintenanceProdTO objServiceTaxMaintenanceProdTO = setServiceTaxMaintenanceProd(PRODID);
                prodId.add(objServiceTaxMaintenanceProdTO);
//            }
//        }
        return prodId;
    }
    
    /** To Get the Values from the Category Table,
     * to be added into the TO objects...
     */
    public ArrayList getCategorySelected() {
        Boolean bln;
        ArrayList arrRow;
        ArrayList categoryId = new ArrayList();
        ArrayList tabData  =  tblCategory.getDataArrayList();
        
        ArrayList selectedList = new ArrayList();
        for (int i=0, j=tblCategory.getRowCount(); i < j; i++) {
            bln = (Boolean) tblCategory.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                arrRow = (ArrayList)tabData.get(i);
                final String CATEGORY = CommonUtil.convertObjToStr(arrRow.get(1));
                ServiceTaxMaintenanceCategotyTO objServiceTaxMaintenanceCategotyTO = setServiceTaxMaintenanceCategoty(CATEGORY);
                objServiceTaxMaintenanceCategotyTO.setStatus(CommonUtil.convertObjToStr(arrRow.get(3)));
                categoryId.add(objServiceTaxMaintenanceCategotyTO);
            }
        }
        return categoryId;
    }
    
    /** Reset the Data in the Respective Tables...
     */
    public void resetInterTab(){
        try{
            ArrayList data = tblInterestTable.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblInterestTable.removeRow(i-1);
            
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    /** Reset the Data in the Respective Tables...
     */
    public void resetProductTab(){
        try{
            ArrayList data = tblProduct.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblProduct.removeRow(i-1);
            setChanged();
            setStAcHdId("");
            setCess1HdId("");
            setCess2HdId("");
            //            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetProductTab():");
            parseException.logException(e,true);
        }
        
    }
    /** Reset the Data in the Respective Tables...
     */
    public void resetCategoryTab(){
        try{
            ArrayList data = tblCategory.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblCategory.removeRow(i-1);
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetCategoryTab():");
            parseException.logException(e,true);
        }
    }
    
     /*
      * To Decide what(Insert or Update) function to perform at
      * the time of Save for the Insert Table...
      */
    public int addTabData(int updateTab, int row){
        int option = -1;
        try{
            final int dataSize = interestList.size();
            
            SerialNo = dataSize;
            boolean addRow = false;
            
            boolean dateRange = true;
            boolean amountRange = true;
            boolean periodRange = true;
            
            ArrayList interestData = new ArrayList();
            
            ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO = setServiceTaxMaintenanceRate();
            ServiceTaxMaintenanceRateTO objIntRateTO;
            
            String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());


            
            interestTabRow = new ArrayList();
            
            interestTabRow.add(tdtDate);
            interestTabRow.add(tdtToDate);
            if(!prodType.equalsIgnoreCase("OA")){
                interestTabRow.add((String)cbmFromAmount.getKeyForSelected());
                interestTabRow.add((String)cbmToAmount.getKeyForSelected());
                /** To Store the From and To period Values...
                 */
                String from  = CommonUtil.convertObjToInt(txtFromPeriod) + " " + getCboFromPeriod();
                String to  = CommonUtil.convertObjToInt(txtToPeriod) + " " + getCboToPeriod();
                
                interestTabRow.add(from);
                interestTabRow.add(to);
            }
            interestTabRow.add(txtRateInterest);
            int rowSelected = -1;
            
            // Replacing ... null end dates
            
            
            System.out.println("Changes Implemented");
            
            if(dataSize > 0){
                for (int i=0;i<dataSize;i++){
                    objIntRateTO = (ServiceTaxMaintenanceRateTO)interestList.get(i);
                    //ArrayList range = new ArrayList();
                    //range = (ArrayList)data.get(i);
                    //__ Check for the Duplication of Date
//                    if (!( DateUtil.getDateMMDDYYYY(tdtDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))))
//                    && DateUtil.getDateMMDDYYYY(tdtToDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0)))))) {
//                        if (!CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")) {
//                            if (!(DateUtil.getDateMMDDYYYY(tdtDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)))) &&
//                            DateUtil.getDateMMDDYYYY(tdtToDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1))))))
//                                dateRange = false;
//                        }
//                    }
                    
                    if (!( DateUtil.getDateMMDDYYYY(tdtDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0)))))) {
                        if (!CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")) {
                            if (!(DateUtil.getDateMMDDYYYY(tdtDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1))))))
                            dateRange = false;
                        }
                    }
                    
                    if(DateUtil.getDateMMDDYYYY(tdtToDate)!=null) 
                    {
                    if (DateUtil.getDateMMDDYYYY(tdtToDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))))) {
                        if (!CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")) {
                            if(DateUtil.getDateMMDDYYYY(tdtToDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)))))
                                dateRange = false;
                        }
                    }
                    }
                     
                    // setFromAmount, setToAmount, setFromPeriod, setToPeriod
                    if(((CommonUtil.convertObjToDouble(cbmFromAmount.getKeyForSelected()).doubleValue() < CommonUtil.convertObjToDouble(objIntRateTO.getFromAmount()).doubleValue())
                    && (CommonUtil.convertObjToDouble(cbmToAmount.getKeyForSelected()).doubleValue() < CommonUtil.convertObjToDouble(objIntRateTO.getFromAmount()).doubleValue()))
                    ||((CommonUtil.convertObjToDouble(cbmFromAmount.getKeyForSelected()).doubleValue() > CommonUtil.convertObjToDouble(objIntRateTO.getToAmount()).doubleValue())
                    && (CommonUtil.convertObjToDouble(cbmToAmount.getKeyForSelected()).doubleValue() > CommonUtil.convertObjToDouble(objIntRateTO.getToAmount()).doubleValue()))){
                        amountRange = true;
                    }else{
                        amountRange = false;
                    }
                    
                    String fromRange = (String)cbmFromPeriod.getKeyForSelected();
                    int fromDays = setCombo(fromRange);
                    long fromPeriod = fromDays * (Long.parseLong(txtFromPeriod));
                    
                    String toRange = (String)cbmToPeriod.getKeyForSelected();
                    int toDays = setCombo(toRange);
                    long toPeriod = toDays * (Long.parseLong(txtToPeriod));
                    
                    long FROM = CommonUtil.convertObjToLong(objIntRateTO.getFromPeriod());
                    long TO = CommonUtil.convertObjToLong(objIntRateTO.getToPeriod());
                    
                    //__ If the FromPeriod is in terms of Months and/or Year; and is equal to the ToPeriod
                    //__ of the Already entered Record, Increase the value of the FromPeriod by 1 day...
                    if((CommonUtil.convertObjToStr(cbmFromPeriod.getKeyForSelected()).equalsIgnoreCase(MONTH)
                    || CommonUtil.convertObjToStr(cbmFromPeriod.getKeyForSelected()).equalsIgnoreCase(YEAR))
                    && (fromPeriod == TO)){
                        
                        fromPeriod ++;
                        //__ Modify the Data in the Object...
                        objServiceTaxMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(fromPeriod)));
                    }
                    
                    //__ Check for the Duplication of Period Range
                    if((fromPeriod < FROM && toPeriod < FROM)
                    ||(fromPeriod > TO && toPeriod > TO)){
                        periodRange = true;
                    }else{
                        periodRange = false;
                    }
                    
                    /** If the Record(s) Already Exists in the Table, Display the Alert...
                     */
                    if(!dateRange && !amountRange && !periodRange && i!= row){
                        System.out.println(dateRange + "&&" + amountRange + "&&" + periodRange+ " && ");
                        rowSelected = i;
                        String[] options = {objServiceTaxMaintenanceRB.getString("cDialogOk"),objServiceTaxMaintenanceRB.getString("cDialogCancel")};
                        option = COptionPane.showOptionDialog(null, objServiceTaxMaintenanceRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        //__ If Ok is Pressed...
                        if (option == 0){
                            resetTable();
                            break;
                        }else if( option == 1){ //__ if Cancel is Pressed...
                            break;
                        }
                    }
                }
            }else{//__ If there is no row in the Table, Add the Row...
                dateRange   = true;
                amountRange  = true;
                periodRange = true;
            }
            //__ Non Duplicated record Can be entered...
            if ((dateRange || amountRange || periodRange) && updateTab!=1){
                tblInterestTable.addRow(interestTabRow);
                interestList.add(objServiceTaxMaintenanceRateTO);
            }
            /** if the record is just updated,
             * update the valued of the Record...
             */
            if((updateTab == 1) && (rowSelected == -1) && (row !=-1)){
                interestList.remove(row);
                interestList.add(row, objServiceTaxMaintenanceRateTO);
                tblInterestTable.removeRow(row);
                tblInterestTable.insertRow(row,interestTabRow);
                option = -1;
            }
            
            //__ Code to add the value to the  null ToDates in the Table...
                 int yesno = -1;
            if (tblInterestTable.getRowCount() > 0) {
                //                yesno = COptionPane.showConfirmDialog (null, "Make previous schedule end date with currently selected Date.", "End Date", COptionPane.YES_NO_OPTION);
                //                if (yesno == COptionPane.YES_OPTION) {
                //__ If the Table Contains any null value for the toDate, Display Option Pane...
                for (int j=0;j<dataSize;j++){
                   if(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j,1)).equals("")) {
                        int a = tblInterestTable.getRowCount();
                        String PREVDATE = CommonUtil.convertObjToStr(tblInterestTable.getValueAt( a-2, 0));
                        String CURRDATE = CommonUtil.convertObjToStr(tblInterestTable.getValueAt( a-1, 0));
                        if (!PREVDATE.equalsIgnoreCase(CURRDATE))
                        {
                        String[] options = {objServiceTaxMaintenanceRB.getString("cDialogYes"),objServiceTaxMaintenanceRB.getString("cDialogNo")};
                        yesno = COptionPane.showOptionDialog(null, objServiceTaxMaintenanceRB.getString("TODATE_WARNING"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
//                         String[] options = {objServiceTaxMaintenanceRB.getString("cDialogYes")};
//                        yesno = COptionPane.showOptionDialog(null, objServiceTaxMaintenanceRB.getString("TODATE_WARNING"), CommonConstants.WARNINGTITLE,
//                        COptionPane.YES_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        
                        break;
                   }
               }
                } 
                //__ To Replace the Empty To_Date to the New From_Date minus one Day..
                if(yesno == 0){                //__ if yes is selected...
                    for (int i=0;i<dataSize;i++){
                        objIntRateTO = (ServiceTaxMaintenanceRateTO)interestList.get(i);
                        String fromDate = CommonUtil.convertObjToStr(objIntRateTO.getRoiDate());
                       
                        
                       if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")){
                        
                          
                            //__ To update the To_Date value in the Table...
                            String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1));
                            //                            tblInterestTable.setValueAt(
                            //                            DateUtil.getStringDate(
                            //                            DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1)), i, 1);
                            
                          tblInterestTable.setValueAt(toDate, i, 1);
                          
                            
                            //__ To update the Value of the To_Date in the ToObjects...
//                            objIntRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(toDate));
                            Date TooDt = DateUtil.getDateMMDDYYYY(toDate);
                            if(TooDt != null){
                            Date tooDate = (Date)curDate.clone();
                            tooDate.setDate(TooDt.getDate());
                            tooDate.setMonth(TooDt.getMonth());
                            tooDate.setYear(TooDt.getYear());
                            objIntRateTO.setRoiEndDate(tooDate);
                            }else{
                                objIntRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(toDate));
                            }
                        }
                        System.out.println("objIntRateTO[ " + i + " ]: " + interestList.get(i));
                    }
                }
            }


            interestTabRow = null;
        }catch(Exception e){
            log.info("The error in addChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        
        System.out.println("Changes Implemented 2");
        return option;
    }
    
    
    /** To Update the Row in thew Table...
     */
    private void updateInterTab(int row, ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO){
        interestList.remove(row);
        interestList.add(row, objServiceTaxMaintenanceRateTO);
        tblInterestTable.setValueAt(txtRateInterest, row, 5);
    }
    
    /** To set the Data of the Particula Record selected from the Table
     * into the Respective Fields...
     */
    void setInterRateType() throws Exception{
        log.info("setInterRateType");
        int size = interestList.size();
        for(int j=0; j < size; j++){
            interestTabRow = new ArrayList();
            ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO)interestList.get(j);
            interestTabRow.add(DateUtil.getStringDate(objServiceTaxMaintenanceRateTO.getRoiDate()));
            interestTabRow.add(DateUtil.getStringDate(objServiceTaxMaintenanceRateTO.getRoiEndDate()));
                 
            if(!getPRODTYPE().equalsIgnoreCase("OA")){
                interestTabRow.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getFromAmount()));
                interestTabRow.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getToAmount()));
                
                /** To set the value of From and To Period in the Table int the refined form ...
                 */
                resultValue = CommonUtil.convertObjToInt(objServiceTaxMaintenanceRateTO.getFromPeriod());
                String period = setPeriod(resultValue);
                interestTabRow.add(String.valueOf(resultValue) + " " + period);
                resetPeriod();
                
                resultValue = CommonUtil.convertObjToInt(objServiceTaxMaintenanceRateTO.getToPeriod());
                period = setPeriod(resultValue);
                interestTabRow.add(String.valueOf(resultValue) + " " +period);
                resetPeriod();
            }
            
            interestTabRow.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getServiceTax()));
            
            System.out.println("*****@@@@@@@@@*******#######: " + interestTabRow);
            tblInterestTable.addRow(interestTabRow);
        }
        //        }
    }
    
    String getInterRateType(){
        return this.interRateType;
    }
    
    /** To display the data in the Table when Some row is selected...
     */
    public void populateInterestTab(int row){
        try{
            log.info("populateInterestTab row: "+row);
            //            setServiceTaxMaintenanceRateTO((ServiceTaxMaintenanceRateTO)getRateTypeDetails().get(row));
            setServiceTaxMaintenanceRateTO((ServiceTaxMaintenanceRateTO)interestList.get(row));
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** To delete the Row(s) in the Interest Table...
     */
    
    public void deleteInterestTab(int row){
        try{
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                interestDeleteTabRow.add(interestList.get(row));
            }
            interestList.remove(row);
            
            tblInterestTable.removeRow(row);
        }catch(Exception e){
            System.out.println("Error in deleteInterestTab()");
            parseException.logException(e,true);
        }
    }
    
    EnhancedTableModel getTblInterest(){
        return this.tblInterestTable;
    }
    
    /** To reset fields associated with the Table...
     */
    public void resetTable(){
        log.info("In resetTable...");
        setTdtDate("");
        setCboFromAmount("");
        setCboToAmount("");
        setTxtFromPeriod("");
        setCboFromPeriod("");
        setTxtToPeriod("");
        setCboToPeriod("");
        setTdtToDate("");
        setTxtAgainstInterest("");
        setTxtLimitAmt("");
        setTxtInterExpiry("");
        setTxtServiceTax("");
        setTxtCess1Tax("");
        setTxtCess2Tax("");
        setTxtTotTax("");
        
       
    }
    
    /** To reset all the fields in the UI
     */
    public void resetForm(){
        log.info("In resetForm()");
        setCboProdType("");
        setTxtGroupName("");
        setTdtDate("");
        setCboFromAmount("");
        setCboToAmount("");
        setTxtFromPeriod("");
        setCboFromPeriod("");
        setTxtToPeriod("");
        setCboToPeriod("");
         
        
        setTdtToDate("");
        setStAcHdId("");
        setCess1HdId("");
        setCess2HdId("");
        setTxtCess1Tax("");
        setTxtCess2Tax("");
        setTxtTotTax("");
        setTxtServiceTax("");
        
        
    }
    
    public void resetLable(){
        setLblRoiGroupId("");
    }
    
    /** To set and change the Status of the lable Status
     */
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    /**To reset the Value of lblStatus after each save action...
     */
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc.
     */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method
     */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    private String txtGroupName = "";
    private String tdtDate = "";
    private String cboFromAmount = "";
    private String cboToAmount = "";
    private String txtFromPeriod = "";
    private String cboFromPeriod = "";
    private String txtToPeriod = "";
    private String cboToPeriod = "";
    private String txtRateInterest = "";
    private String txtPenalInterest = "";
    private String cboProdType = "";
    private String tdtToDate = "";
    private String txtAgainstInterest = "";
    private String txtLimitAmt = "";
    private String txtInterExpiry = "";
    private String txtODI = "";
    private String txtStatementPenal = "";
    private String txtServiceTax = "";
    private String txtCess1Tax= "";
    private String txtCess2Tax= "";
    private String txtTotTax = "";
    // Setter method for txtGroupName
    void setTxtGroupName(String txtGroupName){
        this.txtGroupName = txtGroupName;
        setChanged();
    }
    // Getter method for txtGroupName
    String getTxtGroupName(){
        return this.txtGroupName;
    }
    
    // Setter method for tdtDate
    void setTdtDate(String tdtDate){
        this.tdtDate = tdtDate;
        setChanged();
    }
    // Getter method for tdtDate
    String getTdtDate(){
        return this.tdtDate;
    }
    
    // Setter method for cboFromAmount
    void setCboFromAmount(String cboFromAmount){
        this.cboFromAmount = cboFromAmount;
        setChanged();
    }
    // Getter method for cboFromAmount
    String getCboFromAmount(){
        return this.cboFromAmount;
    }
    
    public void setCbmFromAmount(ComboBoxModel cbmFromAmount){
        this.cbmFromAmount = cbmFromAmount;
        setChanged();
    }
    
    ComboBoxModel getCbmFromAmount(){
        return cbmFromAmount;
    }
    
    // Setter method for cboToAmount
    void setCboToAmount(String cboToAmount){
        this.cboToAmount = cboToAmount;
        setChanged();
    }
    // Getter method for cboToAmount
    String getCboToAmount(){
        return this.cboToAmount;
    }
    
    public void setCbmToAmount(ComboBoxModel cbmToAmount){
        this.cbmToAmount = cbmToAmount;
        setChanged();
    }
    
    ComboBoxModel getCbmToAmount(){
        return cbmToAmount;
    }
    
    // Setter method for txtFromPeriod
    void setTxtFromPeriod(String txtFromPeriod){
        this.txtFromPeriod = txtFromPeriod;
        setChanged();
    }
    // Getter method for txtFromPeriod
    String getTxtFromPeriod(){
        return this.txtFromPeriod;
    }
    
    // Setter method for cboFromPeriod
    void setCboFromPeriod(String cboFromPeriod){
        this.cboFromPeriod = cboFromPeriod;
        setChanged();
    }
    // Getter method for cboFromPeriod
    String getCboFromPeriod(){
        return this.cboFromPeriod;
    }
    
    public void setCbmFromPeriod(ComboBoxModel cbmFromPeriod){
        this.cbmFromPeriod = cbmFromPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmFromPeriod(){
        return cbmFromPeriod;
    }
    
    // Setter method for txtToPeriod
    void setTxtToPeriod(String txtToPeriod){
        this.txtToPeriod = txtToPeriod;
        setChanged();
    }
    // Getter method for txtToPeriod
    String getTxtToPeriod(){
        return this.txtToPeriod;
    }
    
    // Setter method for cboToPeriod
    void setCboToPeriod(String cboToPeriod){
        this.cboToPeriod = cboToPeriod;
        setChanged();
    }
    // Getter method for cboToPeriod
    String getCboToPeriod(){
        return this.cboToPeriod;
    }
    
    public void setCbmToPeriod(ComboBoxModel cbmToPeriod){
        this.cbmToPeriod = cbmToPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmToPeriod(){
        return cbmToPeriod;
    }
    
    
   
    
    // Setter method for txtPenalInterest
    void setLblRoiGroupId(String lblRoiGroupId){
        this.lblRoiGroupId = lblRoiGroupId;
        setChanged();
    }
    // Getter method for txtPenalInterest
    String getLblRoiGroupId(){
        return this.lblRoiGroupId;
    }
    
    /** To get the data for the Product table
     * "InterMaintenance.getProductData" is in InterMaintenanceMap...
     */
    public void getProductData(String prodId){
        try {
            log.info("getProductData()");
            Iterator iterator = null;
            HashMap map = new HashMap();
            if(prodId.length() > 0){
                final List resultList = ClientUtil.executeQuery("InterMaintenance.getProductData"+prodId, null);
                int length = resultList.size();
                if(length > 0){
                    for(int i =0; i<length; i++){
                        map = (HashMap) resultList.get(i);
                        productTabRow = new ArrayList();
                        productTabRow.add(new Boolean(false));
                        iterator = map.values().iterator();
                        while (iterator.hasNext()) {
                            productTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                        }
                        tblProduct.insertRow(0,productTabRow);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getProductData()");
        }
        //        ttNotifyObservers();
    }
    
    void setTblProduct(EnhancedTableModel tblProduct){
        this.tblProduct = tblProduct;
        setChanged();
    }
    
    EnhancedTableModel getTblProduct(){
        return this.tblProduct;
    }
    
    
    /**
     * To get the data for the Category table
     * "InterMaintenance.getProductData" is in InterMaintenanceMap...
     */
    public void getCategoryData(){
        try {
            
            HashMap chgMap=new HashMap();

            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("CHARGES");
            lookUpHash=new HashMap();
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            System.out.println("keyValue"+keyValue);
            chgMap = (HashMap) keyValue.get("CHARGES");
            if(chgMap!=null && chgMap.size() > 0){
                key = (ArrayList) chgMap.get("KEY");
                value = (ArrayList) chgMap.get("VALUE");
                if (key!=null && value!=null && key.size()>0 && value.size()>0) {
                    key.remove("");
                    value.remove("");
                    int length = key.size();
                    for(int i =0; i<length; i++){
                        categoryTabRow = new ArrayList();
                        categoryTabRow.add(new Boolean(false));
                        categoryTabRow.add(key.get(i));
                        categoryTabRow.add(value.get(i));
                        categoryTabRow.add("");
                        tblCategory.insertRow(0,categoryTabRow);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getCategoryData()");
        }
        //        ttNotifyObservers();
    }
    
    void setTblCategory(EnhancedTableModel tblCategory){
        this.tblCategory = tblCategory;
        setChanged();
    }
    
    EnhancedTableModel getTblCategory(){
        return this.tblCategory;
    }
    
    /** To get the data for the InterestRate table
     * "InterMaintenance.getRateType" is in InterMaintenanceMap...
     */
    //    public void getInterestRateData(){
    //        try {
    //            log.info("getInterestRateData()");
    //            Iterator iterator = null;
    //            HashMap map = new HashMap();
    //            final List resultList = ClientUtil.executeQuery("InterMaintenance.getRateType", null);
    //            int length = resultList.size();
    //            if(length > 0){
    //                for(int i =0; i<length; i++){
    //                    map = (HashMap) resultList.get(i);
    //                    interestRateTabRow = new ArrayList();
    //                    iterator = map.values().iterator();
    //                    while (iterator.hasNext()) {
    //                        interestRateTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
    //                    }
    //                    interestRateTabRow.add(NO);
    //                    tblInterestRate.insertRow(0,interestRateTabRow);
    //                }
    //            }
    //        }catch(Exception e){
    //            e.printStackTrace();
    //            log.info("Error in getInterestRateData()");
    //        }
    //    }
    
    //    void setTblInterestRate(EnhancedTableModel tblInterestRate){
    //        this.tblInterestRate = tblInterestRate;
    //        setChanged();
    //    }
    //
    //    EnhancedTableModel getTblInterestRate(){
    //        return this.tblInterestRate;
    //    }
    
    void setInterRateTabCount(int interRateTabCount){
        this.interRateTabCount = interRateTabCount;
        setChanged();
    }
    
    int getInterRateTabCount(){
        return this.interRateTabCount;
    }
    
    
    // Setter method for cbmProdType
    public void setCbmProdType(ComboBoxModel cbmProdType){
        this.cbmProdType = cbmProdType;
        setChanged();
    }
    
    // Getter method for cbmProdType
    ComboBoxModel getCbmProdType(){
        return cbmProdType;
    }
    
    // Setter method for cboProdType
    void setCboProdType(String cboProdType){
        this.cboProdType = cboProdType;
        setChanged();
    }
    // Getter method for cboProdType
    String getCboProdType(){
        return this.cboProdType;
    }
    
    // Setter method for tdtToDate
    void setTdtToDate(String tdtToDate){
        this.tdtToDate = tdtToDate;
        setChanged();
    }
    // Getter method for tdtToDate
    String getTdtToDate(){
        return this.tdtToDate;
    }
    
    // Setter method for txtAgainstInterest
    void setTxtAgainstInterest(String txtAgainstInterest){
        this.txtAgainstInterest = txtAgainstInterest;
        setChanged();
    }
    // Getter method for txtAgainstInterest
    String getTxtAgainstInterest(){
        return this.txtAgainstInterest;
    }
    
    // Setter method for txtLimitAmt
    void setTxtLimitAmt(String txtLimitAmt){
        this.txtLimitAmt = txtLimitAmt;
        setChanged();
    }
    // Getter method for txtLimitAmt
    String getTxtLimitAmt(){
        return this.txtLimitAmt;
    }
    
    // Setter method for txtInterExpiry
    void setTxtInterExpiry(String txtInterExpiry){
        this.txtInterExpiry = txtInterExpiry;
        setChanged();
    }
    // Getter method for txtInterExpiry
    String getTxtInterExpiry(){
        return this.txtInterExpiry;
    }
    
    // Setter method for txtPLR
    
    
   
    
    // Setter method for productTabList
    void setProductTabList(List productTabList){
        this.productTabList = productTabList;
        setChanged();
    }
    // Getter method for productTabList
    List getProductTabList(){
        return this.productTabList;
    }
    
    public void resetDataList(){
        interestList = null;
        interestDeleteTabRow = null;
        
        interestList = new ArrayList();
        interestDeleteTabRow = new ArrayList();
    }
    
    public boolean sortData(){
        boolean addData = true;
        int option = -1;
        ArrayList keyList;
        ArrayList valueList;
        HashMap comparisonMap = new HashMap();
        int dataSize = interestList.size();
        for(int i=0; i<dataSize; i++){
            ServiceTaxMaintenanceRateTO objServiceTaxMaintenanceRateTO = (ServiceTaxMaintenanceRateTO)interestList.get(i);
            
            keyList = new ArrayList();
            keyList.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getRoiDate()));
            keyList.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getRoiEndDate()));
            keyList.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getFromAmount()));
            keyList.add(CommonUtil.convertObjToStr(objServiceTaxMaintenanceRateTO.getToAmount()));
            
            valueList = new ArrayList();
            //__ if the Map Contains the Key, add the Values to the existing values...
            if(comparisonMap.containsKey(keyList)){
                valueList = (ArrayList)comparisonMap.get(keyList);
                valueList.add(CommonUtil.convertObjToDouble(objServiceTaxMaintenanceRateTO.getFromPeriod()));
                valueList.add(CommonUtil.convertObjToDouble(objServiceTaxMaintenanceRateTO.getToPeriod()));
                
            }
            //__ if the New Key, add the data in the Comparison Map..
            else{
                valueList.add(CommonUtil.convertObjToDouble(objServiceTaxMaintenanceRateTO.getFromPeriod()));
                valueList.add(CommonUtil.convertObjToDouble(objServiceTaxMaintenanceRateTO.getToPeriod()));
            }
            
            comparisonMap.put(keyList,valueList);
        }
        System.out.println("comparisonMap: " + comparisonMap);
        
        Object objKey[] = comparisonMap.keySet().toArray();
        int keylen = objKey.length;
        System.out.println("keylen: " + keylen);
        
        //__ for allt he Keys in the ComparisonMap...
        for(int i=0; i< keylen; i++){
            System.out.println("In Key Loop");
            Object objVal[] = ((ArrayList)comparisonMap.get(objKey[i])).toArray();
            
            System.out.println("Before Sort" + objVal);
            java.util.Arrays.sort(objVal);
            for (int z=0; z < objVal.length; z++)
                System.out.println("z:" + z + ":" +  objVal[z]);
            
            int valLen = objVal.length;
            System.out.println("valLen: " + valLen);
            
            //__ For all the Values in the array...
            for(int j=1; j+1 < valLen;){
                System.out.println("In Val Loop");
                System.out.println("To: "+ (CommonUtil.convertObjToInt(objVal[j])+1));
                System.out.println("From: " + CommonUtil.convertObjToInt(objVal[j+1]));
                if( (CommonUtil.convertObjToInt(objVal[j])+1) !=  CommonUtil.convertObjToInt(objVal[j+1])){
                    addData = false;
                    
                    String warning = objServiceTaxMaintenanceRB.getString("SYNC_WARNING1") + objKey[i] + objServiceTaxMaintenanceRB.getString("SYNC_WARNING2");
                    String[] options = {objServiceTaxMaintenanceRB.getString("cDialogOk")};
                    option = COptionPane.showOptionDialog(null, warning, CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    
                    break;
                }
                j+=2;
            }
        }
        
        System.out.println("addData: " + addData);
        return addData;
    }
    
    public static void main(String st[]){
        ArrayList list = new ArrayList();
        list.add(new Integer("23"));
        list.add(new Integer("2"));
        list.add(new Integer("12"));
        
        Object obj[] = list.toArray();
        
        java.util.Arrays.sort(obj);
        
        System.out.println(obj[0].toString() + ":" + obj[1].toString());
    }
    
    
    public String getPRODTYPE() {
        return PRODTYPE;
    }
    
    public void setPRODTYPE(String PRODTYPE) {
        this.PRODTYPE = PRODTYPE;
    }
    
    
    public boolean getIsTableSet() {
        return isTableSet;
    }
    
    
    public void setIsTableSet(boolean isTableSet) {
        this.isTableSet = isTableSet;
    }
    
    public int getRowCount(){
        int rowCount = 0;
        rowCount = tblInterestTable.getRowCount();
        
        return rowCount;
    }
    
    /**
     * Getter for property AuthorizeStatus.
     * @return Value of property AuthorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return AuthorizeStatus;
    }
    
    /**
     * Setter for property AuthorizeStatus.
     * @param AuthorizeStatus New value of property AuthorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String AuthorizeStatus) {
        this.AuthorizeStatus = AuthorizeStatus;
    }
    
    /**
     * Getter for property stAcHdId.
     * @return Value of property stAcHdId.
     */
    public java.lang.String getStAcHdId() {
        return stAcHdId;
    }
    
    /**
     * Setter for property stAcHdId.
     * @param stAcHdId New value of property stAcHdId.
     */
    public void setStAcHdId(java.lang.String stAcHdId) {
        this.stAcHdId = stAcHdId;
    }
    
    /**
     * Getter for property cess1HdId.
     * @return Value of property cess1HdId.
     */
    public java.lang.String getCess1HdId() {
        return cess1HdId;
    }
    
    /**
     * Setter for property cess1HdId.
     * @param cess1HdId New value of property cess1HdId.
     */
    public void setCess1HdId(java.lang.String cess1HdId) {
        this.cess1HdId = cess1HdId;
    }
    
    /**
     * Getter for property cess2HdId.
     * @return Value of property cess2HdId.
     */
    public java.lang.String getCess2HdId() {
        return cess2HdId;
    }
    
    /**
     * Setter for property cess2HdId.
     * @param cess2HdId New value of property cess2HdId.
     */
    public void setCess2HdId(java.lang.String cess2HdId) {
        this.cess2HdId = cess2HdId;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property txtCess1Tax.
     * @return Value of property txtCess1Tax.
     */
    public java.lang.String getTxtCess1Tax() {
        return txtCess1Tax;
    }
    
    /**
     * Setter for property txtCess1Tax.
     * @param txtCess1Tax New value of property txtCess1Tax.
     */
    public void setTxtCess1Tax(java.lang.String txtCess1Tax) {
        this.txtCess1Tax = txtCess1Tax;
    }
    
    /**
     * Getter for property txtCess2Tax.
     * @return Value of property txtCess2Tax.
     */
    public java.lang.String getTxtCess2Tax() {
        return txtCess2Tax;
    }
    
    /**
     * Setter for property txtCess2Tax.
     * @param txtCess2Tax New value of property txtCess2Tax.
     */
    public void setTxtCess2Tax(java.lang.String txtCess2Tax) {
        this.txtCess2Tax = txtCess2Tax;
    }
    
    /**
     * Getter for property txtTotTax.
     * @return Value of property txtTotTax.
     */
    public java.lang.String getTxtTotTax() {
        return txtTotTax;
    }
    
    /**
     * Setter for property txtTotTax.
     * @param txtTotTax New value of property txtTotTax.
     */
    public void setTxtTotTax(java.lang.String txtTotTax) {
        this.txtTotTax = txtTotTax;
    }
    
}
