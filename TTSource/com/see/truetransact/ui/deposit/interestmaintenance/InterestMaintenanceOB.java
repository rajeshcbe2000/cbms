/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestMaintenanceOB.java
 *
 * Created on May 24, 2004, 11:15 AM
 */

package com.see.truetransact.ui.deposit.interestmaintenance;

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

import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceGroupTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceCategotyTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceProdTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceTypeTO;

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
public class InterestMaintenanceOB extends CObservable{
    
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
    private ArrayList debitInterestList = new ArrayList();
    private ArrayList creditInterestList = new ArrayList();
  
    private ArrayList interestDeleteTabRow = new ArrayList();
    private ArrayList interestDelete;
    private InterestMaintenanceRateTO existInterestMaintenanceRateTO=null;
    private List productTabList;
    
    private int SerialNo;
    Date curDate = null;
    private boolean isDynamic=true;
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
    private LinkedHashMap newInterestRecord=new LinkedHashMap();
    private HashMap nextSlabList=null;
    private String PRODTYPE = "";
    private    boolean minPeriod = false;
    private    boolean maxPeriod = false;
    private    boolean minAmount = false;
    private    boolean maxAmount = false;
    public boolean isTableSet = false;
    
    private final static Logger log = Logger.getLogger(InterestMaintenanceUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** To get the Value of Column Title and Dialogue Box...*/
    //    final InterestMaintenanceRB objInterestMaintenanceRB = new InterestMaintenanceRB();
    java.util.ResourceBundle objInterestMaintenanceRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.interestmaintenance.InterestMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private static InterestMaintenanceOB interestMaintenanceOB;
    static {
        try {
            log.info("In InwardClearingOB Declaration");
            interestMaintenanceOB = new InterestMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static InterestMaintenanceOB getInstance() {
        return interestMaintenanceOB;
    }
    
    /** Creates a new instance of InterestMaintenanceOB */
    public InterestMaintenanceOB() throws Exception {
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
        operationMap.put(CommonConstants.JNDI, "InterestMaintenanceGroupJNDI");
        operationMap.put(CommonConstants.HOME, "deposit.interestmaintenance.InterestMaintenanceGroupHome");
        operationMap.put(CommonConstants.REMOTE, "deposit.interestmaintenance.InterestMaintenanceGroup");
    }
    
    /* To set the Column title in Table(s)...*/
    private void setChargeTabTitle() throws Exception{
        log.info("In setChargeTabTitle...");
        
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd1"));
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd2"));
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd3"));
        
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory1"));
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory2"));
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory3"));
        
        setInterestTabTitle();
    }
    
    public void setInterestTabTitleOA() {
        try{
            interestTabTitle = new ArrayList();
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter1"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter2"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter7"));
            
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
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter1"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter2"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter3"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter4"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter5"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter6"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter7"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter8"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter9"));
            
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
        key.add("BILLS");
        value.add("Bills");
        cbmProdType = new ComboBoxModel(key,value);
        if(cbmProdType.getKeys().contains("MDS")){
          cbmProdType.removeKeyAndElement("MDS");
        }
        
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
            System.out.println("mapData inside populate data="+mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    /** To retrieve the Data from the HashMap, returned from DAO
     */
    private void populateOB(HashMap mapData) throws Exception{
        InterestMaintenanceGroupTO objInterestMaintenanceGroupTO = null;
        
        /** Here the first Row is selected...*/
        objInterestMaintenanceGroupTO = (InterestMaintenanceGroupTO) ((List) mapData.get("InterestMaintenanceGroupTO")).get(0);
        System.out.println("objInterestMaintenanceGroupTO="+objInterestMaintenanceGroupTO);
        setInterestMaintenanceGroupTO(objInterestMaintenanceGroupTO);
        //        setInterestMaintenanceProdTO((List) mapData.get("InterestMaintenanceProdTO"));
        setProductTabList((List) mapData.get("InterestMaintenanceProdTO"));
        setInterestMaintenanceCategotyTO((List) mapData.get("InterestMaintenanceCategotyTO"));
        debitInterestList = (ArrayList)mapData.get("InterestMaintenanceDebitRateTO");
        creditInterestList = (ArrayList)mapData.get("InterestMaintenanceCreditRateTO");
//        interestList = (ArrayList)mapData.get("InterestMaintenanceRateTO");
//        
//        setInterRateType();
        //        ttNotifyObservers();
    }
    
    /** Called from populateOB() to Display data Related to the Group...
     */
    private void setInterestMaintenanceGroupTO(InterestMaintenanceGroupTO objInterestMaintenanceGroupTO) throws Exception{
        log.info("In setInterestMaintenanceGroupTO()");
        //__ To Know, what kind or table model to set..
        setPRODTYPE(CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getProductType()));
        setStatusBy(CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getStatusBy()));
        setLblRoiGroupId(CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getRoiGroupId()));
        setCboProdType((String) getCbmProdType().getDataForKey( CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getProductType())));
        setTxtGroupName(CommonUtil.convertObjToStr(objInterestMaintenanceGroupTO.getRoiGroupName()));
        setAuthorizeStatus(objInterestMaintenanceGroupTO.getAuthorizeStatus());
    }
    
    /** Called from populateOB() to Display data Related to the Category
     * in the Category-Table
     */
    private void setInterestMaintenanceCategotyTO(List resultList) throws Exception{
        log.info("In setInterestMaintenanceCategotyTO()");
        ArrayList data = tblCategory.getDataArrayList();
        final int dataSize = data.size();
        final int length = resultList.size();
        String cateId = "";
        for(int j = 0; j < length; j++){
            cateId = CommonUtil.convertObjToStr(((HashMap)resultList.get(j)).get("CATEGORY_ID"));
            for (int i=0;i<dataSize;i++){
                if(tblCategory.getValueAt(i, 1).equals(cateId) ){
                    tblCategory.setValueAt(new Boolean(true), i, 0);
                }
            }
        }
    }
    
    
    /** To Set the dat from the TransferObjects and set 'em in the OB...
     */
    private void setInterestMaintenanceRateTO(InterestMaintenanceRateTO objInterestMaintenanceRateTO) throws Exception{
        log.info("In setInterestMaintenanceTypeTO()");
        
        setTdtDate(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate()));
        setTdtToDate(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiEndDate()));
        setCboFromAmount((String) getCbmFromAmount().getDataForKey(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount())));
        setCboToAmount((String) getCbmToAmount().getDataForKey(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount())));
        
        resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getFromPeriod());
        String period = setPeriod(resultValue);
        setCboFromPeriod(period);
        setTxtFromPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getToPeriod());
        period = setPeriod(resultValue);
        setCboToPeriod(period);
        setTxtToPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtRateInterest(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoi()));
        setTxtPenalInterest(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getPenalInt()));
        
        setTxtAgainstInterest(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getAgainstClearingInt()));
        setTxtLimitAmt(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getLimitAmount()));
        setTxtInterExpiry(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getIntExpiryLimit()));
        setTxtODI(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getOdIntRate()));
        setTxtStatementPenal(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getStatementPenal()));
        setAuthorizeStatus(objInterestMaintenanceRateTO.getAuthorizedStatus());
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
    private InterestMaintenanceGroupTO setInterestMaintenanceGroup() {
        log.info("In setInterestMaintenanceGroup()");
        
        final InterestMaintenanceGroupTO objInterestMaintenanceGroupTO = new InterestMaintenanceGroupTO();
        try{
            System.out.println("lblRoiGroupId="+lblRoiGroupId);
            System.out.println("txtGroupName="+txtGroupName);
            System.out.println("productType="+(String)cbmProdType.getKeyForSelected());
            System.out.println("userId="+TrueTransactMain.USER_ID);
            objInterestMaintenanceGroupTO.setRoiGroupId(lblRoiGroupId);
            objInterestMaintenanceGroupTO.setRoiGroupName(txtGroupName);
            objInterestMaintenanceGroupTO.setProductType((String)cbmProdType.getKeyForSelected());
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objInterestMaintenanceGroupTO.setCreatedBy(TrueTransactMain.USER_ID);
            }else{
                objInterestMaintenanceGroupTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInterestMaintenanceGroupTO;
    }
    
    
    /** To enter the Data into the Database...Called from doActionPerform()...
     */
    private InterestMaintenanceCategotyTO setInterestMaintenanceCategoty(String category) {
        log.info("In setInterestMaintenanceCategoty()");
        System.out.println("Category="+category);
        final InterestMaintenanceCategotyTO objInterestMaintenanceCategotyTO = new InterestMaintenanceCategotyTO();
        try{
            objInterestMaintenanceCategotyTO.setRoiGroupId(lblRoiGroupId);
            objInterestMaintenanceCategotyTO.setCategoryId(category);
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInterestMaintenanceCategotyTO;
    }
    
    
    /** To enter the Data into the Database...Called from doActionPerform()...
     */
    private InterestMaintenanceProdTO setInterestMaintenanceProd(String prodId) {
        log.info("In setInterestMaintenanceProd()");
        
        final InterestMaintenanceProdTO objInterestMaintenanceProdTO = new InterestMaintenanceProdTO();
        try{
            objInterestMaintenanceProdTO.setRoiGroupId(lblRoiGroupId);
            objInterestMaintenanceProdTO.setProdId(prodId);
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInterestMaintenanceProdTO;
    }
    
    /** To set the Data in the TransferObjects...
     */
    private InterestMaintenanceRateTO setInterestMaintenanceRate() {
        log.info("In setInterestMaintenanceRate()");
        
        final InterestMaintenanceRateTO objInterestMaintenanceRateTO = new InterestMaintenanceRateTO();
        try{
            objInterestMaintenanceRateTO.setRoiGroupId(lblRoiGroupId);
            objInterestMaintenanceRateTO.setRateTypeId(getInterRateType());
            //            objInterestMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
            //            objInterestMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
            Date Dt = DateUtil.getDateMMDDYYYY(tdtDate);
            if(Dt != null){
                Date dtDate = (Date) curDate.clone();
                dtDate.setDate(Dt.getDate());
                dtDate.setMonth(Dt.getMonth());
                dtDate.setYear(Dt.getYear());
                objInterestMaintenanceRateTO.setRoiDate(dtDate);
            }else{
                objInterestMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
            }
            
            Date ToDt = DateUtil.getDateMMDDYYYY(tdtToDate);
            if(ToDt != null){
                Date toDate = (Date) curDate.clone();
                toDate.setDate(ToDt.getDate());
                toDate.setMonth(ToDt.getMonth());
                toDate.setYear(ToDt.getYear());
                objInterestMaintenanceRateTO.setRoiEndDate(toDate);
            }else{
                objInterestMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
            }
            
            objInterestMaintenanceRateTO.setFromAmount(CommonUtil.convertObjToDouble((String)cbmFromAmount.getKeyForSelected()));
            objInterestMaintenanceRateTO.setToAmount(CommonUtil.convertObjToDouble((String)cbmToAmount.getKeyForSelected()));
            
            /** To Convert the Combination of From-Period into Days...
             */
            duration = ((String)cbmFromPeriod.getKeyForSelected());
            periodData = setCombo(duration);
            resultData = periodData * (Long.parseLong(txtFromPeriod));
            objInterestMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            /** To Convert the Combination of To-Period into Days...
             */
            duration = ((String)cbmToPeriod.getKeyForSelected());
            periodData = setCombo(duration);
            resultData = periodData * (Long.parseLong(txtToPeriod));
            objInterestMaintenanceRateTO.setToPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            objInterestMaintenanceRateTO.setRoi(CommonUtil.convertObjToDouble(txtRateInterest));
            objInterestMaintenanceRateTO.setPenalInt(CommonUtil.convertObjToDouble(txtPenalInterest));
            
            objInterestMaintenanceRateTO.setAgainstClearingInt(CommonUtil.convertObjToDouble(txtAgainstInterest));
            objInterestMaintenanceRateTO.setLimitAmount(CommonUtil.convertObjToDouble(txtLimitAmt));
            objInterestMaintenanceRateTO.setIntExpiryLimit(CommonUtil.convertObjToDouble(txtInterExpiry));
            objInterestMaintenanceRateTO.setOdIntRate(CommonUtil.convertObjToDouble(txtODI));
            objInterestMaintenanceRateTO.setStatementPenal(CommonUtil.convertObjToDouble(txtStatementPenal));
            objInterestMaintenanceRateTO.setRoiActiveStatus("R");//Active
            if (isRdoInterestType_Debit())
                objInterestMaintenanceRateTO.setIntType("D");
            else if(isRdoInterestType_Credit())
                objInterestMaintenanceRateTO.setIntType("C");
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInterestMaintenanceRateTO;
    }
    
    public void resetTableValues(){
        tblInterestTable.setDataArrayList(null,interestTabTitle);
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
        /** To Encapsulate the Data as an Object
         */
        final InterestMaintenanceGroupTO objInterestMaintenanceGroupTO = setInterestMaintenanceGroup();
        /** To Encapsulate the Data as an ArrayList
         */
        final ArrayList arrayInterestMaintenanceCategotyTO = getCategorySelected();
        
        final ArrayList arrayInterestMaintenanceProdTO = getProdSelected();
        
        //        final ArrayList arrayInterestMaintenanceTypeTO = getInterRateSelected();
        
        objInterestMaintenanceGroupTO.setCommand(getCommand());
        data.put("InterestMaintenanceGroupTO",objInterestMaintenanceGroupTO);
        /** Data sent as Arraylist, to the DAO...
         */
        data.put("InterestMaintenanceCategotyTO",arrayInterestMaintenanceCategotyTO);
        data.put("InterestMaintenanceProdTO",arrayInterestMaintenanceProdTO);
        
        // The following three lines added by Rajesh
        interestList = new ArrayList();
        interestList.addAll(debitInterestList);
        interestList.addAll(creditInterestList);
        
        data.put("InterestMaintenanceRateTO",interestList);
        
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
        
        if(arrayInterestMaintenanceProdTO.size() <= 0){
            str.append(objInterestMaintenanceRB.getString("PROD_WARNING") + "\n");
        }
        
        if(arrayInterestMaintenanceCategotyTO.size() <= 0){
            str.append(objInterestMaintenanceRB.getString("CATEGORY_WARNING") + "\n");
        }
        
        if(interestList.size() <= 0){
            str.append(objInterestMaintenanceRB.getString("DATA_WARNING") + "\n");
        }
        if((actionType != ClientConstants.ACTIONTYPE_DELETE) && str.toString().length() > 0){
            setWarning(str.toString());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }else{
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
        for (int i=0, j=tblProduct.getRowCount(); i < j; i++) {
            bln = (Boolean) tblProduct.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                arrRow = (ArrayList)tabData.get(i);
                final String PRODID = CommonUtil.convertObjToStr(arrRow.get(1));
                InterestMaintenanceProdTO objInterestMaintenanceProdTO = setInterestMaintenanceProd(PRODID);
                System.out.println("objInterestMaintenanceProdTO="+objInterestMaintenanceProdTO);
                prodId.add(objInterestMaintenanceProdTO);
            }
        }
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
                InterestMaintenanceCategotyTO objInterestMaintenanceCategotyTO = setInterestMaintenanceCategoty(CATEGORY);
                categoryId.add(objInterestMaintenanceCategotyTO);
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
            boolean isRetraspective=false;
            ArrayList   singleList=null;
            ArrayList interestData = new ArrayList();
            
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = setInterestMaintenanceRate();
            newInterestRecord.put(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate()),DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate())+1);
            System.out.println("newInterestRecord####"+newInterestRecord);
            InterestMaintenanceRateTO objIntRateTO;
            
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
            interestTabRow.add("RET");
            interestTabRow.add(null);
            int rowSelected = -1;
            
            // Replacing ... null end dates
            
            Date curr_dt=ClientUtil.getCurrentDateProperFormat();
            System.out.println("Changes Implemented");
            HashMap interestMap =new HashMap();
            Date retraspectiveInactiveFrDt=null;
            ArrayList   objIntRateList=null;
            if(!tdtToDate.equals("")){
                Date toDT=new Date(tdtToDate);
                Date toDate=(Date)curr_dt.clone();
                toDate.setDate(toDT.getDate());
                toDate.setMonth(toDT.getMonth());
                toDate.setYear(toDT.getYear());
                interestMap.put("FROM_DATE",toDate);
            }
            else {
                Date ftDt=DateUtil.getDateMMDDYYYY(tdtDate);
                Date ftDate=(Date)curr_dt.clone();
                ftDate.setDate(ftDt.getDate());
                ftDate.setMonth(ftDt.getMonth());
                ftDate.setYear(ftDt.getYear());
                interestMap.put("FROM_DATE",ftDate);
            }
            interestMap.put("ROI_GROUP_ID",objInterestMaintenanceRateTO.getRoiGroupId());
            List lst= ClientUtil.executeQuery("getSelectProductMaintenanceRetraspectiveInterestTO",interestMap);
            if(lst !=null && lst.size()>0){
                HashMap interest=(HashMap)lst.get(0);
                retraspectiveInactiveFrDt=(Date)interest.get("ROI_DATE");
            }
            if(tblInterestTable !=null && tblInterestTable.getDataArrayList().size()>0){
                objIntRateList =(ArrayList)tblInterestTable.getDataArrayList();
            }
            //            if(dataSize > 0)
            //                if(setRetraspectiveCurrDt(interestList,tdtDate))
            objInterestMaintenanceRateTO.setCreateDt(curDate);
            Date initialRoi=null;
            if(dataSize > 0){
                if(existInterestMaintenanceRateTO!=null)
//                    if(prodType.equalsIgnoreCase("TL") || prodType.equalsIgnoreCase("AD")|| prodType.equalsIgnoreCase("ATL") ||
//                    prodType.equalsIgnoreCase("AAD"))
                        validateSlabRate(objInterestMaintenanceRateTO);
                //slab rate match checking
		if (!getPRODTYPE().equals("OA"))
                if(checkSlabRate(interestList,objIntRateList,true))
                    return 2 ;
                //                slabBasedEnableDisable(interestList,objIntRateList,objInterestMaintenanceRateTO);
                for (int i=0;i<dataSize;i++){
                    
                    objIntRateTO = (InterestMaintenanceRateTO)interestList.get(i);
                    String authStatus= objIntRateTO.getAuthorizedStatus();
                    if(objIntRateList !=null && objIntRateList.size()>0)
                        singleList =(ArrayList)objIntRateList.get(i);
                    if(i==0)
                        initialRoi=objIntRateTO.getRoiDate();
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
                    if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtDate),objIntRateTO.getRoiDate())>0 && authStatus ==null )
                        dateRange = false;
                    if(authStatus !=null && (!authStatus.equals("")))
                        if( retraspectiveInactiveFrDt !=null && DateUtil.dateDiff(retraspectiveInactiveFrDt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))))>=0 &&
                        (!(newInterestRecord.containsKey(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))) &&
                        CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,7)).equals("RET"))) ||(retraspectiveInactiveFrDt ==null &&  DateUtil.dateDiff(initialRoi,DateUtil.getDateMMDDYYYY(tdtDate))<=0)){
                            if(retraspectiveInactiveFrDt !=null && DateUtil.dateDiff(objIntRateTO.getRoiDate(),retraspectiveInactiveFrDt)==0 &&
                            DateUtil.dateDiff(objInterestMaintenanceRateTO.getRoiDate(),retraspectiveInactiveFrDt)!=0){
                                if(isDynamic)
                                    dynamicInterestDetails(objIntRateTO,singleList,retraspectiveInactiveFrDt.clone(),i);
                            }
                            //                        if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i-1,0))),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0)))))
                            if((singleList.contains(objInterestMaintenanceRateTO.getRoiDate()) &&
                            singleList.contains(objInterestMaintenanceRateTO.getToAmount())&&
                            singleList.contains(objInterestMaintenanceRateTO.getToPeriod())) ||(
                            DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(0))),objInterestMaintenanceRateTO.getRoiDate())<0)||
                            (retraspectiveInactiveFrDt !=null && DateUtil.dateDiff(retraspectiveInactiveFrDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))))<=0) &&(!
                            CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,7)).equals("RET"))){
                                singleList.set(7,new String("N"));
                                singleList.set(1,CommonUtil.convertObjToStr(objIntRateTO.getRoiDate()));
                                isRetraspective=true;
                                objIntRateTO.setRoiActiveStatus("N");
                                objIntRateTO.setRoiEndDate(objIntRateTO.getRoiDate());
                                interestList.remove(i);
                                interestList.add(i, objIntRateTO);
                                tblInterestTable.removeRow(i);
                                tblInterestTable.insertRow(i,singleList);
                                dateRange = true;
                            }
                        }else{
                            dateRange = true;
                            //                            interestTabRow.add("R");//Retraspective
                            objInterestMaintenanceRateTO.setRoiActiveStatus("R");
                            //                            interestTabRow.add("");
                        }
                    
                    if(DateUtil.getDateMMDDYYYY(tdtToDate)!=null) {
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
                        objInterestMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(fromPeriod)));
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
                        String[] options = {objInterestMaintenanceRB.getString("cDialogOk"),objInterestMaintenanceRB.getString("cDialogCancel")};
                        option = COptionPane.showOptionDialog(null, objInterestMaintenanceRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
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
                interestList.add(objInterestMaintenanceRateTO);
            }
            /** if the record is just updated,
             * update the valued of the Record...
             */
            if((updateTab == 1) && (rowSelected == -1) && (row !=-1)){   
                interestList.remove(row);
                interestList.add(row, objInterestMaintenanceRateTO);
                tblInterestTable.removeRow(row);
                tblInterestTable.insertRow(row,interestTabRow);
                option = -1;
            }
            
            //__ Code to add the value to the  null ToDates in the Table...
            int yesno = -1;// -1
            if (tblInterestTable.getRowCount() > 0) {
                //                yesno = COptionPane.showConfirmDialog (null, "Make previous schedule end date with currently selected Date.", "End Date", COptionPane.YES_NO_OPTION);
                //                if (yesno == COptionPane.YES_OPTION) {
                //__ If the Table Contains any null value for the toDate, Display Option Pane...
                int intSize=0;
                if(interestList !=null)
                    intSize= interestList.size();
                if(intSize>=2)
                    for (int j=0;j<intSize;j++){
                        if(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j,1)).equals("")) {
                            int a = tblInterestTable.getRowCount();
                            String PREVDATE = CommonUtil.convertObjToStr(tblInterestTable.getValueAt( a-2, 0));
                            String CURRDATE = CommonUtil.convertObjToStr(tblInterestTable.getValueAt( a-1, 0));
                            if (!PREVDATE.equalsIgnoreCase(CURRDATE)) {
                                yesno=0;
                                //                                String[] options = {objInterestMaintenanceRB.getString("cDialogYes"),objInterestMaintenanceRB.getString("cDialogNo")};
                                //                                yesno = COptionPane.showOptionDialog(null, objInterestMaintenanceRB.getString("TODATE_WARNING"), CommonConstants.WARNINGTITLE,
                                //                                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                                //                         String[] options = {objInterestMaintenanceRB.getString("cDialogYes")};
                                //                        yesno = COptionPane.showOptionDialog(null, objInterestMaintenanceRB.getString("TODATE_WARNING"), CommonConstants.WARNINGTITLE,
                                //                        COptionPane.YES_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                                
                                break;
                            }
                        }
                    }
                //__ To Replace the Empty To_Date to the New From_Date minus one Day..
                if(yesno == 0){                //__ if yes is selected...
                    for (int i=0;i<intSize-1;i++){
                        objIntRateTO = (InterestMaintenanceRateTO)interestList.get(i);
                        String fromDate = CommonUtil.convertObjToStr(objIntRateTO.getRoiDate());
                        
                        
                        if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")){
                            
                            
                            //__ To update the To_Date value in the Table...
                            String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1));
                            String fromDt = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0));
                            Date fromDtFormat=DateUtil.getDateMMDDYYYY(fromDt);
                            Date toDateFormat=DateUtil.getDateMMDDYYYY(toDate);
                            //                            tblInterestTable.setValueAt(
                            //                            DateUtil.getStringDate(
                            //                            DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1)), i, 1);
                            if(DateUtil.dateDiff(fromDtFormat,toDateFormat)<0)
                                tblInterestTable.setValueAt(DateUtil.getStringDate(DateUtil.addDays(fromDtFormat,0)), i, 1);
                            else
                                tblInterestTable.setValueAt(toDate, i, 1);
                            
                            
                            //__ To update the Value of the To_Date in the ToObjects...
                            //                            objIntRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(toDate));
                            Date TooDt = DateUtil.getDateMMDDYYYY(toDate);
                            if(DateUtil.dateDiff(fromDtFormat,toDateFormat)<0)
                                objIntRateTO.setRoiEndDate(fromDtFormat);
                            else
                                if(TooDt != null ){
                                    Date tooDate = (Date)curDate.clone();
                                    tooDate.setDate(TooDt.getDate());
                                    tooDate.setMonth(TooDt.getMonth());
                                    tooDate.setYear(TooDt.getYear());
                                    objIntRateTO.setRoiEndDate(tooDate);
                                }else{
                                    objIntRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(toDate));
                                }
                        }
//                        System.out.println("objIntRateTO[ " + i + " ]: " + interestList.get(i));
                    }
                }
            }
            
            
            interestTabRow = null;
            isDynamic=false;
        }catch(Exception e){
            log.info("The error in addChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        
        System.out.println("Changes Implemented 2");
        return option;
    }
    public  boolean maxAmtPeriodCheck(int row){
        InterestMaintenanceRateTO tempTo =new InterestMaintenanceRateTO();
        Double p1 = new Double(1);
        Double p2 = new Double(364635);
        Double a1 = new Double(1);
        Double a2 = new Double(9999999999L);
        HashMap keyMap = new HashMap();
        tempTo=(InterestMaintenanceRateTO)interestList.get(row);
        Date FromDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tempTo.getRoiDate()));
        for(int i=0;i<interestList.size();i++){
            tempTo=(InterestMaintenanceRateTO)interestList.get(i);
            String roiDt=tempTo.getRoiDate().getDate()+"/"+
            (tempTo.getRoiDate().getMonth()+1)+"/"+
            (tempTo.getRoiDate().getYear()+1900);
            if(DateUtil.dateDiff(tempTo.getRoiDate(),FromDt)==0)
                keyMap.put(roiDt+"#"+tempTo.getFromAmount()+"#"+tempTo.getToAmount(), "");
        }
        boolean minPeriod = false;
        boolean maxPeriod = false;
        double fromAmt=0;
        double toAmt=0;
        Object keyList[] = keyMap.keySet().toArray();
        for(int i=0;i<keyList.length;i++){
            String s1 = CommonUtil.convertObjToStr(keyList[i]);
            String s2[] = s1.split("#");
            Date fromDt = DateUtil.getDateMMDDYYYY(s2[0]);
            fromAmt = CommonUtil.convertObjToDouble(s2[1]).doubleValue();
            toAmt = CommonUtil.convertObjToDouble(s2[2]).doubleValue();
            minPeriod = false;
            maxPeriod = false;
            for (int j=0; j<interestList.size(); j++) {
                tempTo=(InterestMaintenanceRateTO)interestList.get(j);
                if (DateUtil.dateDiff(fromDt,tempTo.getRoiDate())==0 &&
                fromAmt==tempTo.getFromAmount().doubleValue() && toAmt==tempTo.getToAmount().doubleValue()) {
                    if(tempTo.getFromPeriod().doubleValue()==p1.doubleValue())
                        minPeriod = true;
                    if(tempTo.getToPeriod().doubleValue()==p2.doubleValue())
                        maxPeriod = true;
                }
                //               if (!(minPeriod && maxPeriod))
                //                   break;
            }
        }
        if (minPeriod && maxPeriod) {
            resetComponents();
            return false;
        }
        nextSlabList = new HashMap();
        nextSlabList.put("tdtDt", tempTo.getRoiDate());
        nextSlabList.put("tdtToDt", tempTo.getRoiEndDate());
        nextSlabList.put("FromAmt", tempTo.getFromAmount());
        nextSlabList.put("ToAmt", tempTo.getToAmount());
        nextSlabList.put("FromPeriod", new Double(tempTo.getToPeriod().doubleValue()+1));
        nextSlabList.put("ToPeriod", new Double(0));
        return true;
        //            if(DateUtil.dateDiff(tempTo.getRoiDate(),FromDt)==0 &&
        //               tempTo.getToAmount().doubleValue()==a2.doubleValue()){
        //               if(tempTo.getFromAmount().doubleValue()==a1.doubleValue())
        //
        //                    if(tempTo.getToAmount().doubleValue()==a2.doubleValue())
        //                         if(tempTo.getFromPeriod().doubleValue()==p1.doubleValue())
        //                             if(tempTo.getToAmount().doubleValue()==p2.doubleValue())
        //
        //
        //            }
        
    }
    
    /* if to period or toamount change into one slab rate it will be affect all period or all amount
     *basing on from date
     **/
    public void validateSlabRate(InterestMaintenanceRateTO objInterestMaintenanceRateTO)throws Exception{
        double existToPeriod=0;
        double changeToPeriod=0;
        double existingToAmt=0;
        double changeToAmt=0;
        //        boolean period=false;
        //        boolean amt=false;
        if(existInterestMaintenanceRateTO !=null){
            if(interestList!=null){
                existToPeriod=existInterestMaintenanceRateTO.getToPeriod().doubleValue();
                changeToPeriod=objInterestMaintenanceRateTO.getToPeriod().doubleValue();
                existingToAmt=existInterestMaintenanceRateTO.getToAmount().doubleValue();
                changeToAmt=objInterestMaintenanceRateTO.getToAmount().doubleValue();
                if(existToPeriod !=changeToPeriod){
                    modifyList(existInterestMaintenanceRateTO.getRoiDate(),changeToPeriod,existToPeriod,existingToAmt,true,false);//existToPeriod
                }if(existingToAmt!=changeToAmt){
                    modifyList(existInterestMaintenanceRateTO.getRoiDate(),changeToAmt,existingToAmt,0,false,true);
                }
            }
        }
    }
    
    public void  modifyList(Date roiDate,double PeriodAmt,double existPeriodAmt,double existingToAmt ,boolean isValueperiod,boolean isValueamt)throws Exception{
        if(interestList !=null ){
            ArrayList singleList=null;
            System.out.println("interestList###1111"+interestList);
            for(int i=0;i<interestList.size();i++){
                InterestMaintenanceRateTO tempTo=(InterestMaintenanceRateTO)interestList.get(i);
                singleList=new ArrayList();
                if(DateUtil.dateDiff(tempTo.getRoiDate(), roiDate)==0){
                    if(isValueperiod){
                        //                        if(existPeriodAmt==tempTo.getToAmount().doubleValue() && PeriodAmt<=tempTo.getToAmount().doubleValue()){
                        if((existPeriodAmt+1)==tempTo.getFromPeriod().doubleValue() && existingToAmt==tempTo.getToAmount().doubleValue()){
                            tempTo.setFromPeriod(new Double(PeriodAmt+1));
                            interestList.set(i,tempTo);
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getRoiDate()));
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getRoiEndDate()));
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getFromAmount()));
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getToAmount()));
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getFromPeriod()));
                            //                            singleList.add(CommonUtil.convertObjToStr(tempTo.getToPeriod()));
                            //                            tblInterestTable.removeRow(i);
                            //                            tblInterestTable.insertRow(i,singleList);
                            setTableList(tempTo,i);
                        }
                    }
                    if(isValueamt){
                        if(existPeriodAmt == tempTo.getToAmount().doubleValue()){
                            tempTo.setToAmount(new Double(PeriodAmt));
                            interestList.set(i,tempTo);
                            setTableList(tempTo,i);
                        }
                        if(existPeriodAmt+1 == tempTo.getFromAmount().doubleValue()){
                            tempTo.setFromAmount(new Double(PeriodAmt+1));
                            interestList.set(i,tempTo);
                            setTableList(tempTo,i);
                        }
                    }
                }
                
            }
        }
        System.out.println("interestList###"+interestList);
    }
    public void setTableList(InterestMaintenanceRateTO tempTo,int i)throws Exception{
        ArrayList singleList=new ArrayList();
        singleList.add(CommonUtil.convertObjToStr(tempTo.getRoiDate()));
        singleList.add(CommonUtil.convertObjToStr(tempTo.getRoiEndDate()));
        singleList.add(CommonUtil.convertObjToStr(tempTo.getFromAmount()));
        singleList.add(CommonUtil.convertObjToStr(tempTo.getToAmount()));
        //        singleList.add(CommonUtil.convertObjToStr(tempTo.getFromPeriod()));
        resultValue = CommonUtil.convertObjToInt(tempTo.getFromPeriod());
        String period = setPeriod(resultValue);
        //        String.valueOf(resultValue)
        singleList.add(resultValue+" "+period);
        resetPeriod();
        //        singleList.add(CommonUtil.convertObjToStr(tempTo.getToPeriod()));
        resultValue = CommonUtil.convertObjToInt(tempTo.getToPeriod());
        period = setPeriod(resultValue);
        singleList.add(resultValue+" "+period);
        resetPeriod();
        singleList.add(CommonUtil.convertObjToStr(tempTo.getRoi()));
        singleList.add(CommonUtil.convertObjToStr(tempTo.getRoiActiveStatus()));
        singleList.add(CommonUtil.convertObjToStr(tempTo.getAuthorizedStatus()));
        tblInterestTable.removeRow(i);
        tblInterestTable.insertRow(i,singleList);
    }
    public boolean checkSlabRateUI(){
        ArrayList list=null;
        boolean checkslabRate = checkSlabRate(interestList, list, false);
        System.out.println("checkslabRate="+checkslabRate);
        if(checkslabRate)
            return true;
        else
            return false;
    }
    private boolean checkSlabRate(ArrayList intersetList,ArrayList tabList,boolean isNewEntry){
        InterestMaintenanceRateTO tempto=new InterestMaintenanceRateTO();
        String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
        System.out.println("prodType="+prodType);
        HashMap roiDateMap=new HashMap();
        ArrayList checkList=new ArrayList();
        ArrayList allList =new ArrayList();
        Date prevDt=null;
        Date EmptyDt=null;
        double prevToAmt=0;
        for(int i=0;i<intersetList.size();i++){
            tempto=(InterestMaintenanceRateTO)intersetList.get(i);
          //  if(!tempto.getAuthorizedBy().equals("MIGRATED")){
            Date currRoi=(Date)tempto.getRoiDate();
            double currToAmt=CommonUtil.convertObjToDouble(tempto.getToAmount()).doubleValue();
            checkList=new ArrayList();
            if(isNewEntry)
                if(tempto.getRoiEndDate()==null){
                    Date firstDt=tempto.getRoiDate();
                    if((EmptyDt!=null && DateUtil.dateDiff(EmptyDt,firstDt)==0 ))
                        continue;
                    if((EmptyDt!=null && DateUtil.dateDiff(EmptyDt,DateUtil.getDateMMDDYYYY(tdtDate))==0 ))
                        continue;
                    EmptyDt=firstDt;
                }
            if(tempto.getRoiActiveStatus().equals("R") ){
                if(prevDt !=null && DateUtil.dateDiff(prevDt,currRoi)!=0 ) {  //||(prevToAmt!=0 && prevToAmt!=currToAmt)
                    if(allList !=null && (!allList.isEmpty())){
                        roiDateMap.put(prevDt,allList);
                        //                        roiDateMap.put(new String(""+prevDt+prevToAmt),allList);
                    }
                    allList=new ArrayList();
                }
                checkList.add(tempto.getFromAmount());
                checkList.add(tempto.getToAmount());
                checkList.add(tempto.getFromPeriod());
                checkList.add(tempto.getToPeriod());
                checkList.add(tempto.getAuthorizedBy()); //Jeffin John
                allList.add(checkList);
            }
            prevDt=currRoi;
            prevToAmt=currToAmt;
        }
        //}
        System.out.println("allList="+allList); 
        if(allList.isEmpty())
            prevDt=null;
        if (!isNewEntry)
            if(allList !=null && (!allList.isEmpty()))
                roiDateMap.put(prevDt,allList);
        if (isNewEntry)
            if((prevDt!=null && DateUtil.dateDiff(prevDt,DateUtil.getDateMMDDYYYY(tdtDate))!=0 )) {
                //                allList=new ArrayList();
                //                allList.add(checkList);
                checkList = new ArrayList();
                checkList.add(tempto.getFromAmount());
                checkList.add(tempto.getToAmount());
                checkList.add(tempto.getFromPeriod());
                checkList.add(tempto.getToPeriod());
                checkList.add(tempto.getAuthorizedBy()); //Jeffin John
                allList.add(checkList);
                roiDateMap.put(prevDt,allList);
                //                roiDateMap.put(prevDt,allList);
            }
        System.out.println("roiDateMap 33"+roiDateMap);
        Double p1 = new Double(1);
        Double p2 = new Double(364635);
        Double a1 = new Double(1);
        Double a2 = new Double(9999999999L);
        if(roiDateMap!=null && (!roiDateMap.isEmpty()))
            for(int r=0;r<roiDateMap.size();r++){
                Set key_set=  roiDateMap.keySet();
                Object objKey[] = roiDateMap.keySet().toArray();
                //             Object objVal[] = ((ArrayList)roiDateMap.get(key_set[i])).toArray();
                System.out.println("objKey[]"+objKey);
                Object objVal[] = ((ArrayList)roiDateMap.get(objKey[r])).toArray();
                System.out.println("objVal[]"+objVal);
                ArrayList arr1;
                StringBuffer ps1 = new StringBuffer();
                StringBuffer ps2 = new StringBuffer();
                StringBuffer ac1 = new StringBuffer();
                StringBuffer ac2 = new StringBuffer();
                for(int j=0;j<objVal.length;j++){
                    arr1 = (ArrayList)objVal[j];
                    if(!arr1.contains("MIGRATED")){ // Jeffin John
                        
                        // Migrated data will have AUTHORIZED_BY value as MIGRATED
                        
                        minPeriod = false;
                        maxPeriod = false;
                        minAmount = false;
                        maxAmount = false;
                        
                        Double amount1 = CommonUtil.convertObjToDouble(arr1.get(0));
                        Double amount2 = CommonUtil.convertObjToDouble(arr1.get(1));
                        Double period1 = CommonUtil.convertObjToDouble(arr1.get(2));
                        Double period2= CommonUtil.convertObjToDouble(arr1.get(3));
                    
                         System.out.println("amount1="+amount1);
                         System.out.println("amount2="+amount2);
                         System.out.println("period1="+period1);
                         System.out.println("period2="+period2);
                    
                        if (p1.doubleValue()==period1.doubleValue() || prodType.equals("TD")||prodType.equals("TL")){
                            System.out.println("inside 1st if");
                            minPeriod=true;
                            ps1 = new StringBuffer();
                        } else if(minPeriod==false && (!prodType.equals("TD")||!prodType.equals("TL"))){
                            System.out.println("inside 1st else if");
                            ps1 = new StringBuffer("Minimum Period not given...");
                        }
                    
                        if (p2.doubleValue()==period2.doubleValue()){
                            System.out.println("inside 2nd if");
                            maxPeriod=true;
                            ps2 = new StringBuffer();
                        } else if(maxPeriod==false){
                            System.out.println("inside 2nd else if");
                            ps2 = new StringBuffer("Max Period not given...");
                        }
                    
                        if (a1.doubleValue()==amount1.doubleValue()|| prodType.equals("TD")||prodType.equals("TL")){
                            System.out.println("inside 3rd if");
                            minAmount=true;
                            ac1 = new StringBuffer();
                        } else if(minAmount==false){
                            System.out.println("inside 3rd else if");
                            ac1 = new StringBuffer("Minimum Amt not given...");
                        }
                    
                        if (a2.doubleValue()==amount2.doubleValue()){
                            System.out.println("inside 4th if");
                            maxAmount=true;
                            ac2 = new StringBuffer();
                        } else if(maxAmount==false){
                            System.out.println("inside 4th else if");
                            ac2 = new StringBuffer("Max Amt not given...");
                        }
                    }
                    
                    // else condition coded by Jeffin John
                    
                    else{         
                        minPeriod = true;
                        maxPeriod = true;
                        minAmount = true;
                        maxAmount = true;
                    }
                }
                //                System.out.println("objVal[0] 33"+objVal[0]);
                System.out.println("minPeriod="+minPeriod);
                System.out.println("maxPeriod="+maxPeriod);
                System.out.println("minAmount="+minAmount);
                System.out.println("maxAmount="+maxAmount);
                
                if(!(minPeriod && maxPeriod && minAmount && maxAmount)){
                    ClientUtil.showMessageWindow(" "+ps1+ps2+ac1+ac2);
                    return true;
                }
                
                //            Object ob[]=(Object[])key_set.toArray();
            }
        
        return false;
    }
    public  boolean checkSlabRateMainSave(){
        System.out.println("inside checkSlabRateMainSave()");
        InterestMaintenanceRateTO tempto=new InterestMaintenanceRateTO();
        HashMap roiDateMap=new HashMap();
        ArrayList checkList=new ArrayList();
        ArrayList allList =new ArrayList();
        Date prevDt=null;
        Date EmptyDt=null;
        double prevToAmt=0;
        double prevPeriod=0;
      
         String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
         System.out.println("prodType="+prodType);
            for(int i=0;i<interestList.size();i++){
            tempto=(InterestMaintenanceRateTO)interestList.get(i);
            Date currRoi=(Date)tempto.getRoiDate();
            double currToAmt=CommonUtil.convertObjToDouble(tempto.getToAmount()).doubleValue();
            checkList=new ArrayList();
            //            if(isNewEntry)
            //                if(tempto.getRoiEndDate()==null){
            //                    Date firstDt=tempto.getRoiDate();
            //                    if((EmptyDt!=null && DateUtil.dateDiff(EmptyDt,firstDt)==0 ))
            //                        continue;
            //                    if((EmptyDt!=null && DateUtil.dateDiff(EmptyDt,DateUtil.getDateMMDDYYYY(tdtDate))==0 ))
            //                        continue;
            //                    EmptyDt=firstDt;
            //                }
                if (tempto.getRoiActiveStatus().equals("R")) {
                    if (isRdoRateType_Spl()) {
                        if ((prevDt != null && DateUtil.dateDiff(prevDt, currRoi) != 0)) {
                            if (allList != null && (!allList.isEmpty())) {
                                //                        roiDateMap.put(prevDt,allList);
                                roiDateMap.put(new String("" + prevDt + prevToAmt), allList);
                                allList = new ArrayList();
                            }
                        }
                    } else {
                        if ((prevDt != null && DateUtil.dateDiff(prevDt, currRoi) != 0) || (prevToAmt != 0 && prevToAmt != currToAmt)) {  //||(prevToAmt!=0 && prevToAmt!=currToAmt)
                            //  if((prevDt !=null && DateUtil.dateDiff(prevDt,currRoi)!=0 ) ) {
                            if (allList != null && (!allList.isEmpty())) {
                                //                        roiDateMap.put(prevDt,allList);
                                roiDateMap.put(new String("" + prevDt + prevToAmt), allList);
                                allList = new ArrayList();
                            }

                        }
                    }
                checkList.add(tempto.getFromAmount());
                checkList.add(tempto.getToAmount());
                checkList.add(tempto.getFromPeriod());
                checkList.add(tempto.getToPeriod());
                checkList.add(tempto.getAuthorizedBy());  //Jeffin John
                allList.add(checkList);
                System.out.println("NewAllLIst="+allList);
                prevDt=currRoi;
                prevToAmt=currToAmt;
                
            }
            
        }
        if(allList.isEmpty()){
            prevDt=null;
            prevToAmt=0;
        }
        //        if (!isNewEntry)
        if(allList !=null && (!allList.isEmpty()))
            roiDateMap.put(new String(""+prevDt+prevToAmt),allList);
        //                roiDateMap.put(prevDt,allList);
        //        if (isNewEntry)
        //            if((prevDt!=null && DateUtil.dateDiff(prevDt,DateUtil.getDateMMDDYYYY(tdtDate))!=0 )) {
        //                //                allList=new ArrayList();
        //                //                allList.add(checkList);
        //                checkList = new ArrayList();
        //                checkList.add(tempto.getFromAmount());
        //                checkList.add(tempto.getToAmount());
        //                checkList.add(tempto.getFromPeriod());
        //                checkList.add(tempto.getToPeriod());
        //                allList.add(checkList);
        ////                roiDateMap.put(prevDt,allList);
        //                roiDateMap.put(new String(""+prevDt+prevToAmt),allList);
        //                //                roiDateMap.put(prevDt,allList);
        //            }
        System.out.println("NewroiDateMap 33"+roiDateMap);
        Double p1 = new Double(1);
        Double p2 = new Double(364635);
        Double a1 = new Double(1);
        Double a2 = new Double(9999999999L);
        if(roiDateMap!=null && (!roiDateMap.isEmpty()))
            for(int r=0;r<roiDateMap.size();r++){
                Set key_set=  roiDateMap.keySet();
                Object objKey[] = roiDateMap.keySet().toArray();
                //             Object objVal[] = ((ArrayList)roiDateMap.get(key_set[i])).toArray();
                Object objVal[] = ((ArrayList)roiDateMap.get(objKey[r])).toArray();
                ArrayList arr1;
                
                StringBuffer ps1 = new StringBuffer();
                StringBuffer ps2 = new StringBuffer();
                StringBuffer ac1 = new StringBuffer();
                StringBuffer ac2 = new StringBuffer();
                for(int j=0;j<objVal.length;j++){
                    arr1 = (ArrayList)objVal[j];
                    if(!arr1.contains("MIGRATED")){  //Jeffin John
                        
                        // Migrated data will have AUTHORIZED_BY value as MIGRATED
                        
                        minPeriod = false;
                        maxPeriod = false;
                        minAmount = false;
                        maxAmount = false;
                        
                        Double amount1 = CommonUtil.convertObjToDouble(arr1.get(0));
                        Double amount2 = CommonUtil.convertObjToDouble(arr1.get(1));
                        Double period1 = CommonUtil.convertObjToDouble(arr1.get(2));
                        Double period2= CommonUtil.convertObjToDouble(arr1.get(3));

                        if (p1.doubleValue()==period1.doubleValue() || prodType.equals("TD")|| prodType.equals("TL")){
                            minPeriod=true;
                            ps1 = new StringBuffer();
                        } else if(minPeriod==false && (!prodType.equals("TD"))||(!prodType.equals("TL"))){
                            ps1 = new StringBuffer("Minimum Period not given...");
                        }

                        if (p2.doubleValue()==period2.doubleValue()){
                            maxPeriod=true;
                            ps2 = new StringBuffer();
                        } else if(maxPeriod==false){
                            ps2 = new StringBuffer("Max Period not given...");
                        }
                    }
                    
                    //else condition coded by Jeffin John
                    
                    else{
                        minPeriod = true;
                        maxPeriod = true;
                        minAmount = true;
                        maxAmount = true;
                    }
                    
                    
                    //                    if (a1.doubleValue()==amount1.doubleValue()){
                    //                        minAmount=true;
                    //                        ac1 = new StringBuffer();
                    //                    } else if(minAmount==false){
                    //                        ac1 = new StringBuffer("Minimum Amt not given...");
                    //                    }
                    
                    //                    if (a2.doubleValue()==amount2.doubleValue()){
                    //                        maxAmount=true;
                    //                        ac2 = new StringBuffer();
                    //                    } else if(maxAmount==false){
                    //                        ac2 = new StringBuffer("Max Amt not given...");
                    //                    }
                    
                }
                //                System.out.println("objVal[0] 33"+objVal[0]);
                if(!(minPeriod && maxPeriod)){
                    ClientUtil.showMessageWindow(" "+ps1+ps2+ac1+ac2);
                    return true;
                }
                
                //            Object ob[]=(Object[])key_set.toArray();
            }
        
        return false;
    }
    public boolean slabBasedEnableDisable()throws Exception{
        boolean maxSlabAvailable=false;
        nextSlabList=new HashMap();
        InterestMaintenanceRateTO objInterestMaintenanceRateTO = setInterestMaintenanceRate();
        InterestMaintenanceRateTO tempto=new InterestMaintenanceRateTO();
        Date fromDt=DateUtil.getDateMMDDYYYY(tdtDate);
        double setToPeriod=364635;
        double setMaxamount=9999999999l;
        double toPeriod=CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToPeriod()).doubleValue();
        double maxAmt=CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToAmount()).doubleValue();
        if(interestList !=null && interestList.size()>=1){
            for(int i=0;i<interestList.size();i++){
                tempto=(InterestMaintenanceRateTO)interestList.get(i);
                if( fromDt !=null && DateUtil.dateDiff(fromDt,tempto.getRoiDate())==0){
                    if(setToPeriod==toPeriod && maxAmt==setMaxamount){
                        nextSlabList=new HashMap();
                        break;
                    }
                    //                    setTdtDate(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiEndDate()));
                    //                    setCboFromAmount(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
                    //                    setCboToAmount(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
                    //                    resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getFromPeriod());
                    //                    String period = setPeriod(resultValue);
                    //                    setCboFromPeriod(period);
                    //                    setTxtFromPeriod(String.valueOf(resultValue));
                    //                    resetPeriod();
                    if(toPeriod==setToPeriod){
                        nextSlabList.put("FromAmt",  new Double(maxAmt+1));
                        nextSlabList.put("FromPeriod",new Double(1));
                        nextSlabList.put("ToAmt",null);
                    }else{
                        nextSlabList.put("FromAmt",objInterestMaintenanceRateTO.getFromAmount());
                        nextSlabList.put("FromPeriod",objInterestMaintenanceRateTO.getToPeriod());
                        nextSlabList.put("ToAmt",objInterestMaintenanceRateTO.getToAmount());
                    }
                    nextSlabList.put("tdtDt",objInterestMaintenanceRateTO.getRoiDate());
                    nextSlabList.put("tdtToDt",objInterestMaintenanceRateTO.getRoiEndDate());
                    //                    nextSlabList.put("FromAmt",objInterestMaintenanceRateTO.getFromAmount());
                    
                    maxSlabAvailable=true;
                    //                    if(maxAmt==)
                }
            }
        }
        return maxSlabAvailable;
    }
    public  boolean setNextValue()throws Exception{
        boolean dataEnable=false;
        if(nextSlabList!=null){
            setTdtDate(CommonUtil.convertObjToStr(nextSlabList.get("tdtDt")));
            setTdtToDate(CommonUtil.convertObjToStr(nextSlabList.get("tdtToDt")));
        }
        //        setCboFromAmount(CommonUtil.convertObjToStr(nextSlabList.get("FromAmt")));
        if(nextSlabList!=null && nextSlabList.containsKey("ToAmt") && nextSlabList.get("ToAmt")!=null){
            setCboToAmount((String) getCbmToAmount().getDataForKey(CommonUtil.convertObjToStr(nextSlabList.get("ToAmt"))));//CommonUtil.convertObjToStr(nextSlabList.get("ToAmt")));
            dataEnable=false;
        }else
            dataEnable=true;
        setCboFromAmount((String) getCbmFromAmount().getDataForKey(CommonUtil.convertObjToStr(nextSlabList.get("FromAmt"))));
        
        resultValue = CommonUtil.convertObjToInt(nextSlabList.get("FromPeriod"));
        String period = setPeriod(resultValue);
        setCboFromPeriod(period);
        setTxtFromPeriod(String.valueOf(resultValue));
        double maxAmt=CommonUtil.convertObjToDouble(nextSlabList.get("ToAmt")).doubleValue();
        if(CommonUtil.convertObjToInt(nextSlabList.get("FromPeriod"))==364636 && 9999999999l==maxAmt)
            resetComponents();
        resetPeriod();
        if(nextSlabList==null || nextSlabList.isEmpty()){
            resultValue = 1;
            period = setPeriod(resultValue);
            setCboFromPeriod(period);
            setCboFromAmount((String) getCbmFromAmount().getDataForKey("1"));
        }
        resetPeriod();
        return dataEnable;
    }
    void dynamicInterestDetails(InterestMaintenanceRateTO intMainTo,ArrayList oneList,Object dynamicStart_dt,int k){
        if(!(intMainTo.getRoiActiveStatus()).equals("N")){
            boolean dynamicEntry_yesNo=true;
            
            
            InterestMaintenanceRateTO tempto=new InterestMaintenanceRateTO();
            ArrayList newList=new ArrayList();
            newList.add(DateUtil.getStringDate((Date)dynamicStart_dt));
            newList.add(DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate),-1)));
            newList.add(oneList.get(2));
            newList.add(oneList.get(3));
            newList.add(oneList.get(4));
            newList.add(oneList.get(5));
            newList.add(oneList.get(6));
            newList.add("RET");
            newList.add("");
            System.out.println("newList"+newList+"\n");
            tblInterestTable.addRow(newList);
            
            tempto.setRoiDate(intMainTo.getRoiDate());
            tempto.setRoiEndDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate),-1));
            //            tempto.setRoiEndDate(intMainTo.getRoiEndDate());
            tempto.setRoiGroupId(intMainTo.getRoiGroupId());
            tempto.setFromPeriod(intMainTo.getFromPeriod());
            tempto.setToPeriod(intMainTo.getToPeriod());
            tempto.setPenalInt(intMainTo.getPenalInt());
            tempto.setRoi(intMainTo.getRoi());
            tempto.setFromAmount(intMainTo.getFromAmount());
            tempto.setToAmount(intMainTo.getToAmount());
            tempto.setRoiActiveStatus(intMainTo.getRoiActiveStatus());
            tempto.setAuthorizedStatus("");
            System.out.println("tempto"+tempto+"\n");
            interestList.add(tempto);
            
        }
    }
    //current date set as one record
    private boolean setRetraspectiveCurrDt(ArrayList intList,String tdtDate){
        InterestMaintenanceRateTO objIntRateTO=null;
        for(int i=intList.size()-1;i>=0;i--){
            objIntRateTO = (InterestMaintenanceRateTO)intList.get(i);
            if(DateUtil.dateDiff(objIntRateTO.getRoiDate(),DateUtil.getDateMMDDYYYY(tdtDate))<=0)
                return true;
            return false;
            
        }
        return false;
    }
    /** To Update the Row in thew Table...
     */
    private void updateInterTab(int row, InterestMaintenanceRateTO objInterestMaintenanceRateTO){
        interestList.remove(row);
        interestList.add(row, objInterestMaintenanceRateTO);
        tblInterestTable.setValueAt(txtRateInterest, row, 5);
    }
    
    public void populateInterestTable(String intType) {
        if (intType.equals("CREDIT"))
            interestList = creditInterestList;
        else if (intType.equals("DEBIT"))
            interestList = debitInterestList;
        else
            interestList = new ArrayList();
        try {
            setInterRateType();
        } catch( Exception e ) {
            log.info("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    /** To set the Data of the Particula Record selected from the Table
     * into the Respective Fields...
     */
    void setInterRateType() throws Exception{
        log.info("setInterRateType");
        int size = interestList.size();
//        if (size==0)
        tblInterestTable.setDataArrayList(null, interestTabTitle);
        for(int j=0; j < size; j++){
            interestTabRow = new ArrayList();
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO)interestList.get(j);
            interestTabRow.add(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate()));
            interestTabRow.add(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiEndDate()));
            
            if(!getPRODTYPE().equalsIgnoreCase("OA")){
                interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
                interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
                
                /** To set the value of From and To Period in the Table int the refined form ...
                 */
                resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getFromPeriod());
                String period = setPeriod(resultValue);
                interestTabRow.add(String.valueOf(resultValue) + " " + period);
                resetPeriod();
                
                resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getToPeriod());
                period = setPeriod(resultValue);
                interestTabRow.add(String.valueOf(resultValue) + " " +period);
                resetPeriod();
            }
            
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoi()));
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiActiveStatus()));
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getAuthorizedStatus()));
            
//            System.out.println("*****@@@@@@@@@*******#######: " + interestTabRow);
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
            //            setInterestMaintenanceRateTO((InterestMaintenanceRateTO)getRateTypeDetails().get(row));
            setInterestMaintenanceRateTO((InterestMaintenanceRateTO)interestList.get(row));
            existInterestMaintenanceRateTO=(InterestMaintenanceRateTO)interestList.get(row);
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** To delete the Row(s) in the Interest Table...
     */
    
    public void deleteInterestTab(int row){
        updateToDate(row);
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
    
    /* deleteing record we should update todate to previous record
     */
    private void updateToDate(int selectedRow){
        ArrayList singleList=(ArrayList)tblInterestTable.getDataArrayList().get(selectedRow);
        ArrayList totTabList=(ArrayList) tblInterestTable.getDataArrayList();
        Date from_dt=null;
        from_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(0)));
        from_dt=DateUtil.addDays(from_dt, -1);
        for(int i=totTabList.size()-1;i>=0;i--){
            singleList=(ArrayList)totTabList.get(i);
            InterestMaintenanceRateTO oneList=(InterestMaintenanceRateTO)interestList.get(i);
            
            if(selectedRow>i){
               Date toDate=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(1)));
               String activeStatus=CommonUtil.convertObjToStr(singleList.get(7));
                if( toDate !=null && DateUtil.dateDiff(from_dt,toDate)==0 ){
                    tblInterestTable.setValueAt("",i , 1);
               oneList.setRoiEndDate(null);
               interestList.set(i,oneList);
                }
            }
        }
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
        setTxtRateInterest("");
        setTxtPenalInterest("");
        setTdtToDate("");
        setTxtAgainstInterest("");
        setTxtLimitAmt("");
        setTxtInterExpiry("");
        setTxtODI("");
        setTxtStatementPenal("");
        existInterestMaintenanceRateTO=null;
    }
    
    /** To reset all the fields in the UI
     */
    public void resetForm(){
        log.info("In resetForm()");
        setCboProdType("");
        setTxtGroupName("");
        setRdoInterestType_Debit(false);
        setRdoInterestType_Credit(false);
        setTdtDate("");
        setCboFromAmount("");
        setCboToAmount("");
        setTxtFromPeriod("");
        setCboFromPeriod("");
        setTxtToPeriod("");
        setCboToPeriod("");
        setTxtRateInterest("");
        setTxtPenalInterest("");
        
        setTdtToDate("");
        setTxtAgainstInterest("");
        setTxtLimitAmt("");
        setTxtInterExpiry("");
        setTxtODI("");
        setTxtStatementPenal("");
        setNewInterestRecord(new LinkedHashMap());
        isDynamic=true;
    }
    
    public void resetLable(){
        setLblRoiGroupId("");
    }
    
    public void resetComponents() {
        setTdtDate("");
        setCboFromAmount("");
        setCboToAmount("");
        setTxtFromPeriod("");
        setCboFromPeriod("");
        setTxtToPeriod("");
        setCboToPeriod("");
        setTxtRateInterest("");
        setTxtPenalInterest("");
        
        setTdtToDate("");
        setTxtAgainstInterest("");
        setTxtLimitAmt("");
        setTxtInterExpiry("");
        setTxtODI("");
        setTxtStatementPenal("");
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
    
    private boolean rdoInterestType_Debit = false;
    private boolean rdoInterestType_Credit = false;
    private boolean rdoRateType_Normal = false;
    private boolean rdoRateType_Spl = false;

    public boolean isRdoRateType_Spl() {
        return rdoRateType_Spl;
    }

    public void setRdoRateType_Spl(boolean rdoRateType_Spl) {
        this.rdoRateType_Spl = rdoRateType_Spl;
    }

    public boolean isRdoRateType_Normal() {
        return rdoRateType_Normal;
    }

    public void setRdoRateType_Normal(boolean rdoRateType_Normal) {
        this.rdoRateType_Normal = rdoRateType_Normal;
    }
    
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
    
    // Setter method for txtRateInterest
    void setTxtRateInterest(String txtRateInterest){
        this.txtRateInterest = txtRateInterest;
        setChanged();
    }
    // Getter method for txtRateInterest
    String getTxtRateInterest(){
        return this.txtRateInterest;
    }
    
    // Setter method for txtPenalInterest
    void setTxtPenalInterest(String txtPenalInterest){
        this.txtPenalInterest = txtPenalInterest;
        setChanged();
    }
    // Getter method for txtPenalInterest
    String getTxtPenalInterest(){
        return this.txtPenalInterest;
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
                HashMap prodMap = new HashMap();
                prodMap.put("PROD",prodId);
                final List resultList = ClientUtil.executeQuery("InterMaintenance.getProductData"+prodId, prodMap);
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
            log.info("getCategoryData()");
            Iterator iterator = null;
            HashMap map = new HashMap();
            final List resultList = ClientUtil.executeQuery("InterMaintenance.getCategoryData", null);
            int length = resultList.size();
            if(length > 0){
                for(int i =0; i<length; i++){
                    map = (HashMap) resultList.get(i);
                    categoryTabRow = new ArrayList();
                    categoryTabRow.add(new Boolean(false));
                    iterator = map.values().iterator();
                    while (iterator.hasNext()) {
                        categoryTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblCategory.insertRow(0,categoryTabRow);
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
    void setTxtODI(String txtODI){
        this.txtODI = txtODI;
        setChanged();
    }
    // Getter method for txtPLR
    String getTxtODI(){
        return this.txtODI;
    }
    
    // Setter method for txtFloatingRate
    void setTxtStatementPenal(String txtStatementPenal){
        this.txtStatementPenal = txtStatementPenal;
        setChanged();
    }
    // Getter method for txtFloatingRate
    String getTxtStatementPenal(){
        return this.txtStatementPenal;
    }
    
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
        creditInterestList=new ArrayList();
        debitInterestList=new ArrayList();
    }
    
    public boolean sortData(){
        boolean addData = true;
        int option = -1;
        ArrayList keyList;
        ArrayList valueList;
        HashMap comparisonMap = new HashMap();
        
        int dataSize = interestList.size();
        System.out.println("interestList###"+interestList);
        for(int i=0; i<dataSize; i++){
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO)interestList.get(i);
           if(!objInterestMaintenanceRateTO.getAuthorizedBy().equals("MIGRATED")){
            if(objInterestMaintenanceRateTO.getRoiActiveStatus()!="N"){
                keyList = new ArrayList();
                keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiDate()));
                keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiEndDate()));
               
         
               keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
             //  keyList.add(CommonUtil.convertObjToStr(1));
                keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
                
                valueList = new ArrayList();
                //__ if the Map Contains the Key, add the Values to the existing values...
                if(comparisonMap.containsKey(keyList)){
                    valueList = (ArrayList)comparisonMap.get(keyList);
                    valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getFromPeriod()));
                    valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToPeriod()));
                    
                }
                //__ if the New Key, add the data in the Comparison Map..
                else{
                    valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getFromPeriod()));
                    valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToPeriod()));
                }
                
                comparisonMap.put(keyList,valueList);
            }}
        }
        System.out.println("comparisonMap: " + comparisonMap);        
        Object objKey[] = comparisonMap.keySet().toArray();
        int keylen = objKey.length;
        
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
            //this block commenting only for special rates
            //__ For all the Values in the array...
    if(isRdoRateType_Normal()){
            for(int j=1; j+1 < valLen;){
                System.out.println("In Val Loop");
                System.out.println("To: "+ (CommonUtil.convertObjToInt(objVal[j])+1));
                System.out.println("From: " + CommonUtil.convertObjToInt(objVal[j+1]));
                if( (CommonUtil.convertObjToInt(objVal[j])+1) !=  CommonUtil.convertObjToInt(objVal[j+1])){
                    addData = false;
                    
                    String warning = objInterestMaintenanceRB.getString("SYNC_WARNING1") + objKey[i] + objInterestMaintenanceRB.getString("SYNC_WARNING2");
                    String[] options = {objInterestMaintenanceRB.getString("cDialogOk")};
                    option = COptionPane.showOptionDialog(null, warning, CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    
                    break;
                }
                j+=2;
            }
        
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
     * Getter for property newInterestRecord.
     * @return Value of property newInterestRecord.
     */
    public java.util.LinkedHashMap getNewInterestRecord() {
        return newInterestRecord;
    }
    
    /**
     * Setter for property newInterestRecord.
     * @param newInterestRecord New value of property newInterestRecord.
     */
    public void setNewInterestRecord(java.util.LinkedHashMap newInterestRecord) {
        this.newInterestRecord = newInterestRecord;
    }
    
    /**
     * Getter for property nextSlabList.
     * @return Value of property nextSlabList.
     */
    public java.util.HashMap getNextSlabList() {
        return nextSlabList;
    }
    
    /**
     * Setter for property nextSlabList.
     * @param nextSlabList New value of property nextSlabList.
     */
    public void setNextSlabList(java.util.HashMap nextSlabList) {
        this.nextSlabList = nextSlabList;
    }
    
    /**
     * Getter for property minPeriod.
     * @return Value of property minPeriod.
     */
    public boolean isMinPeriod() {
        return minPeriod;
    }
    
    /**
     * Setter for property minPeriod.
     * @param minPeriod New value of property minPeriod.
     */
    public void setMinPeriod(boolean minPeriod) {
        this.minPeriod = minPeriod;
    }
    
    /**
     * Getter for property maxPeriod.
     * @return Value of property maxPeriod.
     */
    public boolean isMaxPeriod() {
        return maxPeriod;
    }
    
    /**
     * Setter for property maxPeriod.
     * @param maxPeriod New value of property maxPeriod.
     */
    public void setMaxPeriod(boolean maxPeriod) {
        this.maxPeriod = maxPeriod;
    }
    
    /**
     * Getter for property minAmount.
     * @return Value of property minAmount.
     */
    public boolean isMinAmount() {
        return minAmount;
    }
    
    /**
     * Setter for property minAmount.
     * @param minAmount New value of property minAmount.
     */
    public void setMinAmount(boolean minAmount) {
        this.minAmount = minAmount;
    }
    
    /**
     * Getter for property maxAmount.
     * @return Value of property maxAmount.
     */
    public boolean isMaxAmount() {
        return maxAmount;
    }
    
    /**
     * Setter for property maxAmount.
     * @param maxAmount New value of property maxAmount.
     */
    public void setMaxAmount(boolean maxAmount) {
        this.maxAmount = maxAmount;
    }
    
    /**
     * Getter for property rdoInterestType_Debit.
     * @return Value of property rdoInterestType_Debit.
     */
    public boolean isRdoInterestType_Debit() {
        return rdoInterestType_Debit;
    }
    
    /**
     * Setter for property rdoInterestType_Debit.
     * @param rdoInterestType_Debit New value of property rdoInterestType_Debit.
     */
    public void setRdoInterestType_Debit(boolean rdoInterestType_Debit) {
        this.rdoInterestType_Debit = rdoInterestType_Debit;
        setChanged();
    }
    
    /**
     * Getter for property rdoInterestType_Credit.
     * @return Value of property rdoInterestType_Credit.
     */
    public boolean isRdoInterestType_Credit() {
        return rdoInterestType_Credit;
    }
    
    /**
     * Setter for property rdoInterestType_Credit.
     * @param rdoInterestType_Credit New value of property rdoInterestType_Credit.
     */
    public void setRdoInterestType_Credit(boolean rdoInterestType_Credit) {
        this.rdoInterestType_Credit = rdoInterestType_Credit;
        setChanged();
    } 
}
