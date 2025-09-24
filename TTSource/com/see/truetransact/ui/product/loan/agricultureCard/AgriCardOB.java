/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankOB.java
 *
 * Created on Thu Dec 30 16:06:04 IST 2004
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankBranchTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriCardTo;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  152715
 */

public class AgriCardOB extends CObservable{
    
    //    private String txtOtherBranchCode = "";
    //    private String txtOtherBranchShortName = "";
    //    private String txtBranchName = "";
    //    private String txtAddress = "";
    private String cboAgriCardType = "";
    private String cboAgriCardValidity = "";
    private String cboProdType = "";
    private String cboProdId = "";
    //    private String txtBankName = "";
    //    private String txtBankCode = "";
    //    private String txtBankShortName = "";
    //    private String authorizeStatus1 = "";
    //    private String authorizeBy = "";
    //    private String authorizeDt = "";
    //    private String otherBankBranchStatus = "";
    private String txtNoOfYears="";
    private int resultValue=0;
    
    //        panSBInterest
    //        btnAgriNew
    //        btnAgriSave
    //        btnAgriDelete
    //        panButton
    //        panAgriCard
    //        panAgriCardTable
    //    private String txtBankType="";
    //    private String PhoneNo="";
    //    private boolean cRadio_HVC_Yes=false;
    //    private boolean cRadio_HVC_No=false;
    private boolean cRadio_SB_Yes=false;
    private boolean cRadio_SB_No=false;
    
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmAgriCardValidity;
    private ComboBoxModel cbmAgriCardType;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    private ArrayList rowData;
    private static int serialNo = 1;// To maintain serial No in Other Bank Branch Details Table
    private static int count = 1;// To maintain No of Other Bank Branch Details deleted
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    HashMap operationMap;
    final ArrayList tableTitle = new ArrayList();
    ProxyFactory proxy;
    private EnhancedTableModel tblAgriCard;
    private LinkedHashMap agriCardToMap = null;// Contains Other Bank Branch Details which the Status is not DELETED
    private LinkedHashMap deletedOtherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is DELETED
    private LinkedHashMap totalOtherBankBranchTO = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private LinkedHashMap t;
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    private final int Year=365;
    private final int Month=30;
    private final int Days=1;
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String MONTHS="Months";
    private final String DAYS="Days";
    private final String YEARS="Years";
    private       String prodNo="";
    private       HashMap authorizeMap=new HashMap();
    final AgriCardRB objAgriCardRB = new AgriCardRB();
    
    private static Date currDt = null;
    private static AgriCardOB objAgriCardOB; // singleton object
    
    private final static Logger log = Logger.getLogger(AgriCardUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            log.info("Creating NewLoanProductCardOB...");
            objAgriCardOB= new AgriCardOB();
            System.out.println("welcome to observable");
            currDt = ClientUtil.getCurrentDate();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Returns an instance of OtherBankOB.
     * @return  OtherBankOB
     */
    public static AgriCardOB getInstance() {
        return objAgriCardOB;
    }
    /** Creates a new instance of CashManagementOB */
    public AgriCardOB() throws Exception{
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setTableTile();
        tblAgriCard = new EnhancedTableModel(null, tableTitle);
        serialNo = 1;
        count =1;
    }
    
    /* Sets _subHeadTitle with table column headers */
    private void setTableTile() throws Exception{
        tableTitle.add(objAgriCardRB.getString("tblColumn1"));
        tableTitle.add(objAgriCardRB.getString("tblColumn2"));
        //        tableTitle.add(objAgriCardRB.getString("tblColumn3"));
        tableTitle.add(objAgriCardRB.getString("tblColumn4"));
        tableTitle.add(objAgriCardRB.getString("tblColumn5"));
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AgriCardJNDI");
        operationMap.put(CommonConstants.HOME, "product.loan.agriculturecard.AgriCardHome");
        operationMap.put(CommonConstants.REMOTE, "product.loan.agriculturecard.AgriCard");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("TERMLOAN.CARD_TYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("PERIOD");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("TERMLOAN.CARD_TYPE"));
        cbmAgriCardType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmAgriCardValidity = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdId = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    /* To get the type of command */
    private String getCommand() throws Exception{
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = "AUTHORIZE";
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = "REJECT";
                break;
            default:
        }
        return command;
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /**
     * Set Other Bank fields in the OB
     */
    private void setAgriCardOB(AgriCardTo agriCardTo) throws Exception {
        log.info("Inside setOtherBankOB()");
        
        setCboAgriCardType(CommonUtil.convertObjToStr(agriCardTo.getAgriCardType()));
        setCboAgriCardValidity(CommonUtil.convertObjToStr(agriCardTo.getAgriCardValidity()));
        setTxtNoOfYears(CommonUtil.convertObjToStr(agriCardTo.getTxtNoOfYears()));
        resultValue = CommonUtil.convertObjToInt(agriCardTo.getTxtNoOfYears());
        resultValue=resultValue* setCurrentValue(agriCardTo.getAgriCardValidity());
        String period = setPeriod(resultValue);
        setCboAgriCardValidity(period);
        setTxtNoOfYears(String.valueOf(resultValue));
        resetPeriod();
        setCboProdType(CommonUtil.convertObjToStr(agriCardTo.getProdType()));
        //        setCbmProdId(CommonUtil.convertObjToStr(agriCardTo.getProdId()));
        setCboProdId(CommonUtil.convertObjToStr(agriCardTo.getProdId()));
        setProdNo(CommonUtil.convertObjToStr(agriCardTo.getProdNo()));
        if(CommonUtil.convertObjToStr(agriCardTo.getCRadio_SB_Yes()).equals("Y")){
            setCRadio_SB_Yes(true);
            setCRadio_SB_No(false);
        }
        else{
            setCRadio_SB_No(true);
            setCRadio_SB_Yes(false);
        }
        setChanged();
    }
    
    private int setCurrentValue(String ddmmyy){
        if(ddmmyy.equals(YEAR))
            return 365;
        if(ddmmyy.equals(MONTH))
            return 30;
        if(ddmmyy.equals(DAY))
            return 1;
        return 0;
    }
    private void resetPeriod(){
        resultValue = 0;
    }
    /**
     * To set the data in OtherBankTO TO
     */
    //    public OtherBankTO setOtherBankTO() {
    //        log.info("In setOtherBankTO...");
    //
    //        final AgriCardTo agriCardTo = new AgriCardTo();
    //        try{
    //            agriCardTo.setAgriCardType(CommonUtil.convertObjToStr(getTxtBankCode()
    //            objOtherBankTO.setBankCode(CommonUtil.convertObjToStr(getTxtBankCode()));
    //            objOtherBankTO.setBankName(CommonUtil.convertObjToStr(getTxtBankName()));
    //            objOtherBankTO.setBankShortName(CommonUtil.convertObjToStr(getTxtBankShortName()));
    //            objOtherBankTO.setStatus(CommonUtil.convertObjToStr(getCommand()));
    ////            objOtherBankTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
    //            Date dtDate = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt()));
    //            if(dtDate != null){
    //            Date Dt = currDt.clone();
    //            Dt.setDate(dtDate.getDate());
    //            Dt.setMonth(dtDate.getMonth());
    //            Dt.setYear(dtDate.getYear());
    //            objOtherBankTO.setAuthorizeDt(Dt);
    //            }else{
    //               objOtherBankTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
    //            }
    //            objOtherBankTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus1()));
    //            objOtherBankTO.setAuthorizeBy(CommonUtil.convertObjToStr(getAuthorizeBy()));
    //
    //        }catch(Exception e){
    //            log.info("Error in setOtherBankTO()");
    //            parseException.logException(e,true);
    //        }
    //        return objOtherBankTO;
    //    }
    /**
     * Set Other Bank Branch fields in the OB
     */
    //    private void setOtherBankBranchOB(OtherBankBranchTO objOtherBankBranchTO) throws Exception {
    //        log.info("Inside setOtherBankOB()");
    //        setTxtBankCode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBankCode()));
    //        setTxtOtherBranchCode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchCode()));
    //        setTxtBranchName(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchName()));
    //        setTxtOtherBranchShortName(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBranchShortName()));
    //        setTxtAddress(CommonUtil.convertObjToStr(objOtherBankBranchTO.getAddress()));
    //        setTxtPincode(CommonUtil.convertObjToStr(objOtherBankBranchTO.getPincode()));
    //        setCboCity((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCity())));
    //        setCboState((String) getCbmState().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getState())));
    //        setCboCountry((String) getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCountry())));
    //        setOtherBankBranchStatus(CommonUtil.convertObjToStr(objOtherBankBranchTO.getStatus()));
    //        setPhoneNo(CommonUtil.convertObjToStr(objOtherBankBranchTO.getPhoneNo()));
    ////        setTxtBankType(CommonUtil.convertObjToStr(objOtherBankBranchTO.getBankType()));
    //        if(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCRadio_HVC_Yes()).equals("Y")){
    //            setCRadio_HVC_Yes(true);
    //            setCRadio_HVC_No(false);
    //        }
    //        else{
    //            setCRadio_HVC_No(true);
    //            setCRadio_HVC_Yes(false);
    //        }
    //        if(CommonUtil.convertObjToStr(objOtherBankBranchTO.getCRadio_SB_Yes()).equals("Y")){
    //            setCRadio_SB_Yes(true);
    //            setCRadio_SB_No(false);
    //
    //        }
    //        else{
    //            setCRadio_SB_No(true);
    //            setCRadio_SB_Yes(false);
    //        }
    //
    //        ttNotifyObservers();
    //    }
    /**
     * To set the data in OtherBankTO TO
     */
    public void setCbmProdIds(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    public AgriCardTo setAgriCardTO() {
        log.info("In setAgriCardTO...");
        
        final AgriCardTo objAgriCardTO = new AgriCardTo();
        try{
            objAgriCardTO.setAgriCardType(CommonUtil.convertObjToStr((String)cbmAgriCardType.getKeyForSelected()));
            objAgriCardTO.setAgriCardValidity(CommonUtil.convertObjToStr((String)cbmAgriCardValidity.getKeyForSelected()));
            objAgriCardTO.setTxtNoOfYears(CommonUtil.convertObjToDouble(getTxtNoOfYears()));
            objAgriCardTO.setProdType(CommonUtil.convertObjToStr((String)cbmProdType.getKeyForSelected()));
            objAgriCardTO.setProdId(CommonUtil.convertObjToStr((String)cbmProdId.getKeyForSelected()));
            objAgriCardTO.setStatus(CommonConstants.STATUS_CREATED);
            objAgriCardTO.setStatusDt(currDt);
            objAgriCardTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));
            objAgriCardTO.setProdNo(CommonUtil.convertObjToDouble(getProdNo()));
            if(isCRadio_SB_Yes()==true)
                objAgriCardTO.setCRadio_SB_Yes("Y");
            else
                objAgriCardTO.setCRadio_SB_Yes("N");
            //            objAgriCardTO.setPincode(CommonUtil.convertObjToStr(getTxtPincode()));
            //            objAgriCardTO.setCity(CommonUtil.convertObjToStr((String)cbmCity.getKeyForSelected()));
            //            objAgriCardTO.setState(CommonUtil.convertObjToStr((String)cbmState.getKeyForSelected()));
            //            objOtherBankBranchTO.setCountry(CommonUtil.convertObjToStr((String)cbmCountry.getKeyForSelected()));
            //            objOtherBankBranchTO.setStatus(CommonUtil.convertObjToStr(getOtherBankBranchStatus()));
            //            objOtherBankBranchTO.setBankType(CommonUtil.convertObjToStr(getTxtBankType()));
            //            objOtherBankBranchTO.setPhoneNo(CommonUtil.convertObjToStr(getPhoneNo()));
            //            if(isCRadio_SB_No())
            //                objAgriCardTO.setCRadio_SB_Yes(NewLoanProductCardRB.getString("radio_db_no"));
            //            else
            //                objAgriCardTO.setCRadio_SB_Yes(NewLoanProductCardRB.getString("radio_db_yes"));
        }catch(Exception e){
            log.info("Error in setAgriCardTO()");
            parseException.logException(e,true);
        }
        return objAgriCardTO;
    }
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    //                    throw new TTException(NewLoanProductCardRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        //        final OtherBankTO objOtherBankTO = setOtherBankTO();
        //        objOtherBankTO.setCommand(getCommand());
        
        if (totalOtherBankBranchTO == null)
            totalOtherBankBranchTO = new LinkedHashMap();
        final HashMap data = new HashMap();
        
        if(getActionType() == ClientConstants.ACTIONTYPE_REJECT || getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            data.putAll(authorizeMap);
        }else{
            //       TherBankBranchTO.put(TO_NOT_DELETED_AT_UPDATE_MODE, otherBankBranchTO);
            
            data.put("AgriCardTos",agriCardToMap);
            if (deletedOtherBankBranchTO != null) {
                data.put(TO_DELETED_AT_UPDATE_MODE, deletedOtherBankBranchTO);
            }
        }
        //        data.put("OtherBankBranchTO",totalOtherBankBranchTO);
        data.put("MODE",getCommand());// To Maintain the Status CREATED, MODIFIED, DELETED
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("doactionperform######"+data);
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    /**
     * To reset all fields
     */
    public void resetForm() {
        //        resetOtherBank();
        resetOtherBankBranch();
        serialNo = 1;
        count =1;
        rowData = null;
        //        otherBankBranchTO = null;
        deletedOtherBankBranchTO = null;
        totalOtherBankBranchTO = null;
        agriCardToMap=null;
        authorizeMap=null;
        tblAgriCard.setDataArrayList(null,tableTitle);
        ttNotifyObservers();
    }
    /**
     * To Reset Other Bank details
     */
    //    public void resetOtherBank() {
    //        setTxtBankCode("");
    //        setTxtBankName("");
    //        setTxtBankShortName("");
    //    }
    public void resetOtherBankBranch() {
        
        setTxtNoOfYears("");
        
        setCboAgriCardType("");
        setCboAgriCardValidity("");
        setCboProdType("");
        setCboProdId("");
        setCRadio_SB_Yes(false);
        setCRadio_SB_No(false);
        //        setTxtBankType("");
    }
    /**
     * Set Column values in the Other Bank Branch details table
     */
    private ArrayList setColumnValues(int rowClicked,AgriCardTo objAgriCardTo) {
        ArrayList row = new ArrayList();
        //        row.add(String.valueOf(rowClicked));
        row.add(CommonUtil.convertObjToStr(objAgriCardTo.getAgriCardType()));
        row.add(CommonUtil.convertObjToStr(objAgriCardTo.getTxtNoOfYears()));
        row.add(CommonUtil.convertObjToStr(objAgriCardTo.getProdType()));
        row.add(CommonUtil.convertObjToStr(objAgriCardTo.getProdId()));
        return row;
        
    }
    
    /**
     * Action Performed when Delete is pressed in OtherBankBranch
     */
    public void deleteOtherBankBranch(int index) {
        log.info("deleteOtherBankBranch Invoked...");
        try {
            if (agriCardToMap != null) {
                AgriCardTo objAgriCardTo = (AgriCardTo) agriCardToMap.remove(String.valueOf(index+1));
                if( ( objAgriCardTo.getStatus().length()>0 ) && ( objAgriCardTo.getStatus() != null ) && !(objAgriCardTo.getStatus().equals(""))) {
                    if (deletedOtherBankBranchTO == null)
                        deletedOtherBankBranchTO = new LinkedHashMap();
                    deletedOtherBankBranchTO.put(String.valueOf(count++), objAgriCardTo);
                } else if (agriCardToMap != null) {
                    //                        for(int i = index+1,j=otherBankBranchTO.size();i<=j;i++) {
                    //                            agriCardToMap.put(String.valueOf(i),(AgriCardTo)otherBankBranchTO.remove(String.valueOf((i+1))));
                    //                        }
                    
                }
                //                    objOtherBankBranchTO = null;
                serialNo--;
                // Reset table data
                rowData.remove(index);
                     /* Orders the serial no in the arraylist (tableData) after the removal
                   of selected Row in the table */
                for(int i=0,j = rowData.size();i<j;i++){
                    ( (ArrayList) rowData.get(i)).set(0,String.valueOf(i+1));
                }
                tblAgriCard.setDataArrayList(rowData,tableTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        final HashMap mapData;
        try {
            whereMap.put("MODE",getCommand());
            mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("The error in populateData");
            parseException.logException(e,true);
        }
    }
    /**
     * populate Other Bank & Other Bank Branch Details
     */
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB...");
        AgriCardTo agriCardTo = null;
        agriCardTo = (AgriCardTo) ((List) mapData.get("AgriCardList")).get(0);
        setAgriCardOB(agriCardTo);
        if (rowData == null)
            rowData = new ArrayList();
        if(agriCardToMap==null)
            agriCardToMap=new LinkedHashMap();
        if (deletedOtherBankBranchTO == null) {
            deletedOtherBankBranchTO=new LinkedHashMap();
        }
        for (int i=0,j=((List) mapData.get("AgriCardList")).size();i<j;i++) {
            agriCardTo = (AgriCardTo) ((List) mapData.get("AgriCardList")).get(i);
            rowData.add(setColumnValues(serialNo, agriCardTo));
            if(getActionType()==ClientConstants.ACTIONTYPE_DELETE)
                deletedOtherBankBranchTO.put(String.valueOf(serialNo),agriCardTo);
            
            else
                agriCardToMap.put(String.valueOf(serialNo),agriCardTo);
            serialNo++;
            agriCardTo = null;
        }
        tblAgriCard.setDataArrayList(rowData,tableTitle);
        
        //        placeValuesInOtherBankBranchTable(list);
        ttNotifyObservers();
    }
    /**
     * To Populate OtherBankBranch Details
     */
    //    private void placeValuesInOtherBankBranchTable(List list) throws Exception {
    //        log.info("In populateOBDocuments...");
    //        if (list != null) {
    //            if (otherBankBranchTO == null)
    //                otherBankBranchTO = new LinkedHashMap();
    //            if (rowData == null)
    //                rowData = new ArrayList();
    //            for (int i=0,j=list.size();i<j;i++) {
    //                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) list.get(i);
    //                rowData.add(setColumnValues(serialNo, objOtherBankBranchTO));
    //                otherBankBranchTO.put(String.valueOf(serialNo),objOtherBankBranchTO);
    //                serialNo++;
    //                objOtherBankBranchTO = null;
    //            }
    //            tblOtherBankBranch.setDataArrayList(rowData,tableTitle);
    //        }
    //
    //    }
    /**
     * If all the fields contains value then enable New button
     */
    public boolean enableNew(String bankCode, String bankShortName) {
        boolean flag = false;
        try {
            if (bankCode != null && bankCode.length() > 0 && bankShortName != null && bankShortName.length() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * populate Other Bank Branch Details when table is pressed
     */
    public boolean populateOtherBankBranch(int row) {
        boolean flag = false;
        log.info("populateOtherBankBranch Invoked...");
        try {
            AgriCardTo objAgriCardTo = (AgriCardTo) agriCardToMap.get(String.valueOf(row+1));
            
            setAgriCardOB(objAgriCardTo);
            
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * To check whether Bank Code is duplicated or not
     */
    public boolean isBankCodeAlreadyExist(String bankCode){
        boolean exist = false;
        try{
            if (bankCode != null && bankCode.length() >0 && !bankCode.equals("")) {
                HashMap where = new HashMap();
                where.put("BANK_CODE", bankCode );
                List bankCodeCount = (List) ClientUtil.executeQuery("countBankCode", where);
                where = null;
                if ( bankCodeCount.size() > 0 && bankCodeCount != null) {
                    String count = CommonUtil.convertObjToStr(((LinkedHashMap) bankCodeCount.get(0)).get("COUNT"));
                    if ( Integer.parseInt(count) > 0 ) {
                        exist = true;
                    } else {
                        exist = false;
                    }
                    count = null;
                }
                bankCodeCount = null;
            }
        }
        catch ( Exception e ) {
            parseException.logException(e,true);
        }
        return exist;
    }
    /**
     * Action to be performed when Save Button in Other Bank Branch Screen is pressed
     */
    public void saveOtherBankBranch(boolean tableClick,int rowClicked) {
        log.info("saveOtherBankBranch Invoked...");
        try {
            //            setProdNo("0.0");
            AgriCardTo objAgriCardTo = setAgriCardTO();
            if (agriCardToMap == null)
                agriCardToMap = new LinkedHashMap();
            if (rowData == null)
                rowData =  new ArrayList();
            if (tableClick) {
                rowData.set(rowClicked, setColumnValues(rowClicked+1, objAgriCardTo));
                agriCardToMap.put(String.valueOf(rowClicked+1), objAgriCardTo);
                setAgriCardOB(objAgriCardTo);
            } else {
                rowData.add(setColumnValues(serialNo, objAgriCardTo));
                agriCardToMap.put(String.valueOf(serialNo),objAgriCardTo);
                serialNo++;
            }
            //            objOtherBankBranchTO = null;
            setProdNo("0.0");
            tblAgriCard.setDataArrayList(rowData,tableTitle);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    public String  setPeriod(int rV){
        String periodValue;
        if(rV>Year && rV%Year==0){
            rV=rV/Year;
            periodValue=YEARS;
        } else if(rV>Month && rV%Month==0){
            rV=rV/Month;
            periodValue=MONTHS;
        } else if(rV>Days && rV%Days==0){
            rV=rV/Days;
            periodValue=DAYS;
        }else{
            periodValue="";
            rV = 0;
        }
        resultValue=rV;
        return periodValue;
    }
    public double setCombo(String duration) throws Exception{
        //        periodData=0;
        //        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if( duration.equals(DAY))
                period = Days;
            else if(duration.equals(MONTH))
                period = Month;
            else if(duration.equals(YEAR))
                period = Year;
        }
        
        duration = "";
        return period;
    }
    
    /**
     * Getter for property cRadio_DB_Yes.
     * @return Value of property cRadio_DB_Yes.
     */
    public boolean isCRadio_SB_Yes() {
        return cRadio_SB_Yes;
    }
    
    /**
     * Setter for property cRadio_DB_Yes.
     * @param cRadio_DB_Yes New value of property cRadio_DB_Yes.
     */
    public void setCRadio_SB_Yes(boolean cRadio_SB_Yes) {
        this.cRadio_SB_Yes = cRadio_SB_Yes;
    }
    
    /**
     * Getter for property cRadio_DB_No.
     * @return Value of property cRadio_DB_No.
     */
    public boolean isCRadio_SB_No() {
        return cRadio_SB_No;
    }
    
    /**
     * Setter for property cRadio_DB_No.
     * @param cRadio_DB_No New value of property cRadio_DB_No.
     */
    public void setCRadio_SB_No(boolean cRadio_SB_No) {
        this.cRadio_SB_No = cRadio_SB_No;
    }
    
    /**
     * Getter for property cboAgriCardType.
     * @return Value of property cboAgriCardType.
     */
    public java.lang.String getCboAgriCardType() {
        return cboAgriCardType;
    }
    
    /**
     * Setter for property cboAgriCardType.
     * @param cboAgriCardType New value of property cboAgriCardType.
     */
    public void setCboAgriCardType(java.lang.String cboAgriCardType) {
        this.cboAgriCardType = cboAgriCardType;
    }
    
    /**
     * Getter for property cboAgriCardValidity.
     * @return Value of property cboAgriCardValidity.
     */
    public java.lang.String getCboAgriCardValidity() {
        return cboAgriCardValidity;
    }
    
    /**
     * Setter for property cboAgriCardValidity.
     * @param cboAgriCardValidity New value of property cboAgriCardValidity.
     */
    public void setCboAgriCardValidity(java.lang.String cboAgriCardValidity) {
        this.cboAgriCardValidity = cboAgriCardValidity;
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
     * Getter for property cboProdId.
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }
    
    /**
     * Setter for property cboProdId.
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }
    
    /**
     * Getter for property txtNoOfYears.
     * @return Value of property txtNoOfYears.
     */
    public java.lang.String getTxtNoOfYears() {
        return txtNoOfYears;
    }
    
    /**
     * Setter for property txtNoOfYears.
     * @param txtNoOfYears New value of property txtNoOfYears.
     */
    public void setTxtNoOfYears(java.lang.String txtNoOfYears) {
        this.txtNoOfYears = txtNoOfYears;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
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
     * Getter for property cbmAgriCardValidity.
     * @return Value of property cbmAgriCardValidity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgriCardValidity() {
        return cbmAgriCardValidity;
    }
    
    /**
     * Getter for property cbmAgriCardType.
     * @return Value of property cbmAgriCardType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgriCardType() {
        return cbmAgriCardType;
    }
    
    /**
     * Setter for property cbmAgriCardType.
     * @param cbmAgriCardType New value of property cbmAgriCardType.
     */
    public void setCbmAgriCardType(com.see.truetransact.clientutil.ComboBoxModel cbmAgriCardType) {
        this.cbmAgriCardType = cbmAgriCardType;
    }
    
    /**
     * Setter for property cbmAgriCardValidity.
     * @param cbmAgriCardValidity New value of property cbmAgriCardValidity.
     */
    public void setCbmAgriCardValidity(com.see.truetransact.clientutil.ComboBoxModel cbmAgriCardValidity) {
        this.cbmAgriCardValidity = cbmAgriCardValidity;
    }
    
    /**
     * Getter for property tblAgriCard.
     * @return Value of property tblAgriCard.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblAgriCard() {
        return tblAgriCard;
    }
    
    /**
     * Setter for property tblAgriCard.
     * @param tblAgriCard New value of property tblAgriCard.
     */
    public void setTblAgriCard(com.see.truetransact.clientutil.EnhancedTableModel tblAgriCard) {
        this.tblAgriCard = tblAgriCard;
    }
    
    /**
     * Getter for property prodNo.
     * @return Value of property prodNo.
     */
    public java.lang.String getProdNo() {
        return prodNo;
    }
    
    /**
     * Setter for property prodNo.
     * @param prodNo New value of property prodNo.
     */
    public void setProdNo(java.lang.String prodNo) {
        this.prodNo = prodNo;
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
    
}