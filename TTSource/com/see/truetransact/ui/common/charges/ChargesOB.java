/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesOB.java
 *
 * Created on Thu Dec 23 12:39:09 IST 2004
 */

package com.see.truetransact.ui.common.charges;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.charges.ChargesTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;  // added by Rajesh
import java.util.Date;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author
 */

public class ChargesOB extends CObservable{
    private static ChargesOB objChargesOB; // singleton object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private ComboBoxModel cbmChargeType;
    private ComboBoxModel cbmRateType;
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmProductId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap operationMap;
    private ArrayList chargeList=new ArrayList();
    private LinkedHashMap folioTo=new LinkedHashMap();
    private ArrayList tableCharges = new ArrayList();
    private ArrayList databaseOp = new ArrayList();
    
    private EnhancedTableModel tblData;
    private ArrayList tblRow;
    
    private String tdtFromDt = "" ;
    private String tdtToDt = "" ;
    
    private String cboChargeType = "";
    private String cboProductType = "";
    private String cboProductId = "";
    private String txtPercent = "";
    private String txtFixRate = "";
    private String txtFromAmt = "";
    private String txtToAmt = "";
    private String txtForEvery = "";
    private String txtRateVal = "";
    private String cboRateType = "";
    private int actionType = 0 ;
    private int result = 0 ;
    private ProxyFactory proxy;
    private static int  folioNo=1;
    private ChargesTO chargesto=new ChargesTO();
    ArrayList tabDataTitle = new ArrayList();
    private int SerialNo;
    private Date curDate = null;
    
    static {
        try {
            objChargesOB = new ChargesOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    private ChargesOB()throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        
        setChargeTabTitle();    //__ To set the Title of Table in Charges Tab...
        
        tblData = new EnhancedTableModel(null, tabDataTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }
    
    
    /* To set the Column title in Table(s)...*/
    private void setChargeTabTitle() throws Exception{
        tabDataTitle.add("From Amt");
        tabDataTitle.add("To Amt");
        tabDataTitle.add("Start Dt");
        tabDataTitle.add("End Dt");
        tabDataTitle.add("Fixed Amt");
        tabDataTitle.add("Percent");
        tabDataTitle.add("For Every Amt");
        tabDataTitle.add("Rate");
        tabDataTitle.add("Rate Type");
        
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ChargesJNDI");
        operationMap.put(CommonConstants.HOME, "common.charges.ChargesHome");
        operationMap.put(CommonConstants.REMOTE, "common.charges.Charges");
    }
    
    /** Creates a new instance of ChargesOB */
    public static ChargesOB getInstance() {
        return objChargesOB;
    }
    /** A method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("CHARGES");
        lookup_keys.add("FOREX.RATE_TYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CHARGES"));
        cbmChargeType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("FOREX.RATE_TYPE")); // For Amount/Percentage dropdown
        cbmRateType = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProductType = new ComboBoxModel(key,value);
    }
    
    
    public void getSavedCharges(){
        try{
            HashMap getTableDataMap = new HashMap();
            getTableDataMap.put(CommonConstants.PRODUCT_TYPE, cboProductType);
            getTableDataMap.put(CommonConstants.PRODUCT_ID, cboProductId);
            getTableDataMap.put(CommonConstants.CHRG_TYPE, cboChargeType);
            getTableDataMap.put("TODAY_DT", curDate);
            
            HashMap paramMap = new HashMap();
            paramMap.put(CommonConstants.MAP_NAME, "Charges.getSavedCharges");
            paramMap.put(CommonConstants.MAP_WHERE, getTableDataMap);
            
            HashMap outputMap = proxy.executeQuery(paramMap, operationMap);
            setFields(outputMap);
        }catch(Exception error){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            error.printStackTrace();
        }
    }
    
    private void setFields(HashMap outputMap) throws Exception{
        List outList = (List)outputMap.get("ChargesTO");
        ChargesTO outTO = null ;
        int listSize = outList.size();
        for(int i = 0; i < listSize ; i++){
            outTO = (ChargesTO)outList.get(i);
            setTxtFromAmt(CommonUtil.convertObjToStr(outTO.getFromAmt()));
            setTxtToAmt(CommonUtil.convertObjToStr(outTO.getToAmt()));
            setTxtFixRate(CommonUtil.convertObjToStr(outTO.getFixedRate()));
            setTdtFromDt(DateUtil.getStringDate(outTO.getStartDate()));
            setTdtToDt(DateUtil.getStringDate(outTO.getEndDate()));
            setTxtPercent(CommonUtil.convertObjToStr(outTO.getPercentage()));
            setTxtForEvery(CommonUtil.convertObjToStr(outTO.getForEveryAmt()));
            setTxtRateVal(CommonUtil.convertObjToStr(outTO.getForEveryRate()));
            setCboRateType(CommonUtil.convertObjToStr(outTO.getForEveryType()));
            addTableRow(outTO);
        }
    }
    
    public void getProductIdByType() throws Exception{//String productType)
        /** The data to be show in Combo Box other than LOOKUP_MASTER table
         * Show Product Id */
        System.out.println("PRODUCT TYPE = " + getCboProductType());
        HashMap lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + (String) getCbmProductType().getKeyForSelected());
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key,value);
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    // Setter method for cboChargeType
    void setCboChargeType(String cboChargeType){
        this.cboChargeType = cboChargeType;
        setChanged();
    }
    // Getter method for cboChargeType
    String getCboChargeType(){
        return this.cboChargeType;
    }
    
    // Setter method for txtPercent
    void setTxtPercent(String txtPercent){
        this.txtPercent = txtPercent;
        setChanged();
    }
    // Getter method for txtPercent
    String getTxtPercent(){
        return this.txtPercent;
    }
    
    // Setter method for txtFixRate
    void setTxtFixRate(String txtFixRate){
        this.txtFixRate = txtFixRate;
        setChanged();
    }
    // Getter method for txtFixRate
    String getTxtFixRate(){
        return this.txtFixRate;
    }
    
    // Setter method for txtFromAmt
    void setTxtFromAmt(String txtFromAmt){
        this.txtFromAmt = txtFromAmt;
        setChanged();
    }
    // Getter method for txtFromAmt
    String getTxtFromAmt(){
        return this.txtFromAmt;
    }
    
    // Setter method for txtToAmt
    void setTxtToAmt(String txtToAmt){
        this.txtToAmt = txtToAmt;
        setChanged();
    }
    // Getter method for txtToAmt
    String getTxtToAmt(){
        return this.txtToAmt;
    }
    
    // Setter method for txtForEvery
    void setTxtForEvery(String txtForEvery){
        this.txtForEvery = txtForEvery;
        setChanged();
    }
    // Getter method for txtForEvery
    String getTxtForEvery(){
        return this.txtForEvery;
    }
    
    // Setter method for txtRateVal
    void setTxtRateVal(String txtRateVal){
        this.txtRateVal = txtRateVal;
        setChanged();
    }
    // Getter method for txtRateVal
    String getTxtRateVal(){
        return this.txtRateVal;
    }
    
    // Setter method for cboRateType
    void setCboRateType(String cboRateType){
        this.cboRateType = cboRateType;
        setChanged();
    }
    // Getter method for cboRateType
    String getCboRateType(){
        return this.cboRateType;
    }
    
    /**
     * Getter for property cbmChargeType.
     * @return Value of property cbmChargeType.
     */
    public ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    
    /**
     * Setter for property cbmChargeType.
     * @param cbmChargeType New value of property cbmChargeType.
     */
    public void setCbmChargeType(ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    
    /**
     * Getter for property cbmRateType.
     * @return Value of property cbmRateType.
     */
    public ComboBoxModel getCbmRateType() {
        return cbmRateType;
    }
    
    /**
     * Setter for property cbmRateType.
     * @param cbmRateType New value of property cbmRateType.
     */
    public void setCbmRateType(ComboBoxModel cbmRateType) {
        this.cbmRateType = cbmRateType;
    }
    
    
    private ChargesTO setChargesTOData() {
        ChargesTO objChargesTO = new ChargesTO();
        try{
            objChargesTO.setProductType((String)getCbmProductType().getKeyForSelected());
            objChargesTO.setProductId((String)getCbmProductId().getKeyForSelected());
            objChargesTO.setChargeType((String)getCbmChargeType().getKeyForSelected());
            objChargesTO.setPercentage(CommonUtil.convertObjToDouble(getTxtPercent()));
            
            Date FrDt = DateUtil.getDateMMDDYYYY(getTdtFromDt());
            if(FrDt != null){
            Date frDate = (Date)curDate.clone();
            frDate.setDate(FrDt.getDate());
            frDate.setMonth(FrDt.getMonth());
            frDate.setYear(FrDt.getYear());
//            objChargesTO.setStartDate(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
            objChargesTO.setStartDate(frDate);
            }else{
                objChargesTO.setStartDate(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
            }
            
            Date ToDt = DateUtil.getDateMMDDYYYY(getTdtToDt());
            if(ToDt != null){
            Date toDate = (Date)curDate.clone();
            toDate.setDate(ToDt.getDate());
            toDate.setMonth(ToDt.getMonth());
            toDate.setYear(ToDt.getYear());
//            objChargesTO.setEndDate(DateUtil.getDateMMDDYYYY(getTdtToDt()));
            objChargesTO.setEndDate(toDate);
            }else{
                 objChargesTO.setEndDate(DateUtil.getDateMMDDYYYY(getTdtToDt()));
            }
            
            objChargesTO.setFixedRate(CommonUtil.convertObjToDouble(getTxtFixRate()));
            objChargesTO.setFromAmt(CommonUtil.convertObjToDouble(getTxtFromAmt()));
            objChargesTO.setToAmt(CommonUtil.convertObjToDouble(getTxtToAmt()));
            objChargesTO.setForEveryAmt(CommonUtil.convertObjToDouble(getTxtForEvery()));
            objChargesTO.setForEveryRate(CommonUtil.convertObjToDouble(getTxtRateVal()));
            
            if(getCbmRateType().getKeyForSelected()!=null)
                objChargesTO.setForEveryType((String)getCbmRateType().getKeyForSelected());
            objChargesTO.setStatusBy(ProxyParameters.USER_ID) ;
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objChargesTO;
    }
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        try {
            HashMap map = new HashMap();
            map.put(CommonConstants.MAP_WHERE, whereMap);
            HashMap mapData = proxy.executeQuery(map, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        ChargesTO objChargesTO;
        chargeList=new ArrayList();
        tableCharges=new ArrayList();
        System.out.println("populateob###"+mapData);
        for(int i=0;i<((List)mapData.get("ChargesTO")).size();i++){
        objChargesTO = (ChargesTO) ((List) mapData.get("ChargesTO")).get(i);
        getData(objChargesTO);
        chargeList.add(getTableArrayList(objChargesTO,1));
        tableCharges.add(getTableArrayList(objChargesTO,1));
        objChargesTO.setStatus(CommonConstants.STATUS_MODIFIED);
        databaseOp.add(objChargesTO);
//         chargeList.add(objChargesTO);
//        tableCharges.add(objChargesTO);
//        databaseOp.add(objChargesTO,1);
        }
        tblData.setDataArrayList(chargeList, tabDataTitle);
        notifyObservers();
    }
    
    public void deleteTableRow(int selectedRow){
        tblData.removeRow(selectedRow);
        ArrayList templist=new ArrayList();
        ChargesTO chargesTO =new ChargesTO();
          chargesTO =setChargesTo();
          databaseOp.remove(selectedRow);
//        ChargesTO chargesTO = (ChargesTO)tableCharges.get(selectedRow) ;
        chargesTO.setStatus(CommonConstants.STATUS_DELETED);
//        databaseOp.set(selectedRow,getTableArrayList(chargesTO,1));
        templist.add(chargesTO);
        databaseOp.add(templist);
////        databaseOp.add(chargesTO);
        System.out.println("Charges TO after delete : " + chargesTO);
        tableCharges.remove(selectedRow);
        
    }
    
    public void getData(ChargesTO objChargesTO) {
        System.out.println(objChargesTO);
        try{
            setCboProductType(CommonUtil.convertObjToStr(getCbmProductType().getDataForKey(objChargesTO.getProductType())));
            
            setCboChargeType(CommonUtil.convertObjToStr(getCbmChargeType().getDataForKey(objChargesTO.getChargeType())));
            setCboRateType(CommonUtil.convertObjToStr(getCbmRateType().getDataForKey(objChargesTO.getForEveryType())));
            setTdtFromDt(DateUtil.getStringDate( objChargesTO.getStartDate()));
            setTdtToDt(DateUtil.getStringDate( objChargesTO.getEndDate()));
            setTxtFixRate(CommonUtil.convertObjToStr( objChargesTO.getFixedRate()));
            setTxtForEvery(CommonUtil.convertObjToStr(objChargesTO.getForEveryAmt()));
            setTxtFromAmt(CommonUtil.convertObjToStr(objChargesTO.getFromAmt()));
            setTxtToAmt(CommonUtil.convertObjToStr(objChargesTO.getToAmt()));
            setTxtPercent(CommonUtil.convertObjToStr(objChargesTO.getPercentage()));
            setTxtRateVal(CommonUtil.convertObjToStr(objChargesTO.getForEveryRate()));
            
            this.getCbmProductType().setKeyForSelected(objChargesTO.getProductType());
            getProductIdByType();
            setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(objChargesTO.getProductId())));
            
        }catch(Exception E){
            E.printStackTrace();
        }
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public String getCboProductType() {
        return cboProductType;
    }
    
    /**
     * Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     */
    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /**
     * Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     */
    public void setCbmProductId(String prodType) {
       try{
            if(prodType !=null && (! prodType.equals(""))){
            HashMap keyValue=new HashMap();
                if(!prodType.equals("GL")){
                    HashMap lookup_hash=new HashMap();
                    lookup_hash.put(CommonConstants.PARAMFORQUERY,null);
                    lookup_hash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    keyValue=ClientUtil.populateLookupData(lookup_hash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmProductId = new ComboBoxModel(key,value);
                }
            }
       }catch(Exception e){
           e.printStackTrace();
       }
//        this.cbmProductId = cbmProductId;
    }
    
    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public String getCboProductId() {
        return cboProductId;
    }
    
    /**
     * Setter for property cboProductId.
     * @param cboProductId New value of property cboProductId.
     */
    public void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return actionType;
    }
    
    public void doAction() {
        System.out.println("In doAction ...." + ClientConstants.ACTIONTYPE_CANCEL + "Action Type=" + actionType);
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    System.out.println("To perform action now....");
                    doActionPerform();
                    setLblStatus(ClientConstants.RESULT_STATUS[actionType]);
                    notifyObservers();
                }
                else{
                    final ChargesRB objChargesRB = new ChargesRB();
                    throw new TTException(objChargesRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            ClientParseException cpe = ClientParseException.getInstance();
            cpe.logException(e, true);
        }
    }
    
    /** To get the value of action performed */
    private String getCommand() throws Exception{
        String command = null;
        System.out.println("Action Type in getCommand() = " + actionType);
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
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("ChargesTO",databaseOp);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("chargesob doAction##"+data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        System.out.println("Exit from doActionPerform...");
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    public int getResult(){
        return result;
    }
    public void setObservableValues(int selectedRow){
        ArrayList arrayList=(ArrayList)tblData.getDataArrayList().get(selectedRow);
        System.out.println("arraylist####"+arrayList);
//        setCboProductType(CommonUtil.convertObjToStr(arrayList.get(0)));
//        setCboProductId(CommonUtil.convertObjToStr(arrayList.get(1)));
//        setCboChargeType(CommonUtil.convertObjToStr(arrayList.get(2)));
        setTxtFromAmt(CommonUtil.convertObjToStr(arrayList.get(0)));
        setTxtToAmt(CommonUtil.convertObjToStr(arrayList.get(1)));
         setTdtFromDt(CommonUtil.convertObjToStr(arrayList.get(2)));
        setTdtToDt(CommonUtil.convertObjToStr(arrayList.get(3)));
        
        setTxtPercent(CommonUtil.convertObjToStr(arrayList.get(6)));
        setTxtFixRate(CommonUtil.convertObjToStr(arrayList.get(5)));
//        setTxtForEvery(CommonUtil.convertObjToStr(arrayList.get(9)));
       
        System.out.println("chargestoo@@@@@###"+chargesto);
//        getData(chargesto);
        ttNotifyObservers();
        
    }
    public void resetMaster(){
        setCboChargeType("");
        setCboProductId("");
        setCboProductType("");
        tblData = new EnhancedTableModel(null,tabDataTitle) ;
        databaseOp = new ArrayList() ;
        tableCharges = new ArrayList() ;
        chargeList=new ArrayList();
        folioTo=new LinkedHashMap();
    }
    public void setChargeTableValues(boolean tableCliked,int selectedRow ){
        if(chargeList== null)
            chargeList=new ArrayList();
        if(folioTo==null)
            folioTo=new LinkedHashMap();
//        if(chargesto==null)
            chargesto=new ChargesTO();
        try{
        chargesto=setChargesTo();
        if(tableCliked){
            chargeList.set(selectedRow,getTableArrayList( chargesto,selectedRow));
            folioTo.put(String.valueOf(selectedRow+1), chargesto);
            updateTableRow(chargesto,selectedRow);
        }else{
            chargeList.add(getTableArrayList(chargesto,selectedRow));
            addTableRow(chargesto);
            folioTo.put(String.valueOf(folioNo), chargesto);
            
            folioNo++;
        }
//        tblData.setDataArrayList(chargeList,tabDataTitle); 
        tableCliked=false;
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
    }
    public ArrayList getTableArrayList(ChargesTO chargesto,int selectedRow){
        ArrayList row=new ArrayList();
//        row.add((String)cbmProductType.getDataForKey(chargesto.getProductType()));
//        row.add((String)cbmProductId.getDataForKey(chargesto.getProductId()));
//        row.add((String)cbmChargeType.getDataForKey(chargesto.getChargeType()));
      
        row.add(CommonUtil.convertObjToStr(chargesto.getFromAmt()));
        row.add(CommonUtil.convertObjToStr(chargesto.getToAmt()));
        row.add(CommonUtil.convertObjToStr(chargesto.getStartDate()));
        row.add(CommonUtil.convertObjToStr(chargesto.getEndDate()));
        row.add(CommonUtil.convertObjToStr(chargesto.getFixedRate()));
        row.add(CommonUtil.convertObjToStr(chargesto.getPercentage()));
        row.add(CommonUtil.convertObjToStr(chargesto.getForEveryRate()));
         row.add(CommonUtil.convertObjToStr(chargesto.getForEveryAmt()));
        row.add(CommonUtil.convertObjToStr(chargesto.getForEveryType()));
//          row.add(CommonUtil.convertObjToStr(chargesto.getForEveryType()orEveryRate()));
        row.add(CommonUtil.convertObjToStr(chargesto.getAuthorised()));
        row.add(CommonUtil.convertObjToStr(chargesto.getAuthorisedBy()));
        row.add(CommonUtil.convertObjToStr(chargesto.getAuthorisedDate()));
        
        return row;
    }
    public ChargesTO setChargesTo(){
        ChargesTO charges=new ChargesTO();
        charges.setProductType((String)cbmProductType.getKeyForSelected());
        charges.setProductId((String)cbmProductId.getKeyForSelected());
        charges.setChargeType((String)cbmChargeType.getKeyForSelected());
        
        Date FrDt = DateUtil.getDateMMDDYYYY(getTdtFromDt());
        if(FrDt != null){
        Date frDate = (Date)curDate.clone();
        frDate.setDate(FrDt.getDate());
        frDate.setMonth(FrDt.getMonth());
        frDate.setYear(FrDt.getYear());
//        charges.setStartDate(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
        charges.setStartDate(frDate);
        }else{
            charges.setStartDate(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
        }
        
        Date ToDt = DateUtil.getDateMMDDYYYY(getTdtToDt());
        if(ToDt != null){
        Date toDate = (Date)curDate.clone();
        toDate.setDate(ToDt.getDate());
        toDate.setMonth(ToDt.getMonth());
        toDate.setYear(ToDt.getYear());
//        charges.setEndDate(DateUtil.getDateMMDDYYYY(getTdtToDt()));
        charges.setEndDate(toDate);
        }else{
            charges.setEndDate(DateUtil.getDateMMDDYYYY(getTdtToDt()));
        }
        
        charges.setFromAmt(CommonUtil.convertObjToDouble(getTxtFromAmt()));
        charges.setToAmt(CommonUtil.convertObjToDouble(getTxtToAmt()));
        charges.setPercentage(CommonUtil.convertObjToDouble(getTxtPercent()));
        charges.setFixedRate(CommonUtil.convertObjToDouble(getTxtFixRate()));
        charges.setForEveryAmt(CommonUtil.convertObjToDouble(getTxtForEvery()));
        charges.setForEveryRate(CommonUtil.convertObjToDouble(getTxtFixRate()));
        charges.setForEveryType("");
        //        charges.setAuthorised(get
        //        charges.setAuthorisedBy(
        //        charges.setAuthorisedDate(
        System.out.println("setChargesTO####"+charges);
        return charges;
    }
    /** To reset the txt fileds to null */
    public void resetForm(){
        setCboRateType("");
        setTdtFromDt("");
        setTdtToDt("");
        setTxtFixRate("");
        setTxtForEvery("");
        setTxtFromAmt("");
        setTxtPercent("");
        setTxtRateVal("");
        setTxtToAmt("");
        notifyObservers();
    }
    
    
    void setTblData(EnhancedTableModel tblData){
        this.tblData = tblData;
        setChanged();
    }
    
    EnhancedTableModel getTblData(){
        return this.tblData;
    }
    
    private boolean validateRows(String tdtFromDt, String tdtToDt, int i){
        boolean returnStatus = false ;
        //  ******************* VALIDATE THE DATES FIRST ***************
        //Dates lies between an existing range
        if(DateUtil.getDateMMDDYYYY(tdtFromDt).before(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,3))) &&
        DateUtil.getDateMMDDYYYY(tdtToDt).before(DateUtil.getDateMMDDYYYY(tdtFromDt))
        ){
            System.out.println("return from 1");
            returnStatus = false ;
        }
        
        //Assumptions below are that the end date is validated to be greater than start date
        //Above check to be done in UI
        
        //Dates lies before the given range
        else if(DateUtil.getDateMMDDYYYY(tdtFromDt).before(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,2))) &&
        DateUtil.getDateMMDDYYYY(tdtToDt).before(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,2)))){
            System.out.println("return from 2");
            returnStatus = true ;
        }
        
        //.................
        
        //Dates lies after the given range
        else if(DateUtil.getDateMMDDYYYY(tdtFromDt).after(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,3))) &&
        DateUtil.getDateMMDDYYYY(tdtToDt).after(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,3)))){
            returnStatus = true ;
        }
        
        else if (DateUtil.getDateMMDDYYYY(tdtFromDt).equals(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,2))) &&
        DateUtil.getDateMMDDYYYY(tdtToDt).equals(DateUtil.getDateMMDDYYYY((String)tblData.getValueAt(i,3)))){
            System.out.println("return from 3");
            returnStatus = validateAmt(i) ;
        }
        
        //  ******************* END OF DATES VALIDATION ***************
        
        
        System.out.println("returnStatus in validateRows " + returnStatus);
        //AFTER ALL VALIDATIONS
        return returnStatus ;
    }
    
    private boolean validateAmt(int i){
        //  ******************* START OF AMOUNT VALIDATION ***************
        boolean returnStatus = false ;
        //Amt lies within range
        if(CommonUtil.convertObjToDouble(txtFromAmt).doubleValue() >= CommonUtil.convertObjToDouble(tblData.getValueAt(i,0)).doubleValue() &&
        CommonUtil.convertObjToDouble(txtFromAmt).doubleValue() <= CommonUtil.convertObjToDouble(tblData.getValueAt(i,1)).doubleValue()){
            returnStatus = false ;
        }
        
        //Amt lies before given range
        else if(CommonUtil.convertObjToDouble(txtFromAmt).doubleValue() < CommonUtil.convertObjToDouble(tblData.getValueAt(i,0)).doubleValue() &&
        CommonUtil.convertObjToDouble(txtToAmt).doubleValue() < CommonUtil.convertObjToDouble(tblData.getValueAt(i,0)).doubleValue()){
            returnStatus = true ;
        }
        
        //Amt lies after given range
        else if(CommonUtil.convertObjToDouble(txtFromAmt).doubleValue() > CommonUtil.convertObjToDouble(tblData.getValueAt(i,1)).doubleValue() &&
        CommonUtil.convertObjToDouble(txtToAmt).doubleValue() > CommonUtil.convertObjToDouble(tblData.getValueAt(i,1)).doubleValue()){
            returnStatus = true ;
        }
        
        //  ******************* END OF AMOUNTS VALIDATION ***************
        System.out.println("returnStatus in validateAmt " + returnStatus);
        return returnStatus;
    }
    
    private void addTableRow(ChargesTO chargesTO) throws Exception{
        //Verify if row exists ....
         String chargetype=chargesTO.getChargeType();
            System.out.println("chargetype####"+chargetype);
        int rowCount = tblData.getRowCount();
        boolean dataValid = true ;
        if(chargesTO.getStatus().equals("")){ //Do this only for new data
            for(int i = 0 ; i < rowCount; i++){
                if(! chargetype.equals("FOLIOCHG"))
                if(!validateRows(tdtFromDt, tdtToDt, i)){
                    dataValid= false ;
                    System.out.println("Row already exists");
                    throw new TTException("Row already exists"); // checking purpose abi
                    //break ;
                }
            }
            if(dataValid== true)
                addRow(chargesTO);
        }
        else
            addRow(chargesTO);
        //If row does not exist, add it
        
    }
    
    private void updateTableRow(ChargesTO chargesTO, int selectedRow){
        tblData.setValueAt(txtFixRate,selectedRow, 4);
        tblData.setValueAt(txtPercent,selectedRow, 5);
        tblData.setValueAt(txtForEvery,selectedRow, 6);
        tblData.setValueAt(txtRateVal,selectedRow, 7);
        tblData.setValueAt(cbmRateType.getKeyForSelected(),selectedRow, 8);
        tableCharges.set(selectedRow, chargesTO);
        chargesTO.setStatus(CommonConstants.STATUS_MODIFIED);
        databaseOp.set(selectedRow, chargesTO) ;
    }
    
    private void addRow(ChargesTO chargesTO){
        tblRow = new ArrayList();
        tblRow.add(txtFromAmt);
        tblRow.add(txtToAmt);
        tblRow.add(tdtFromDt);
        tblRow.add(tdtToDt);
        tblRow.add(txtFixRate);
        tblRow.add(txtPercent);
        tblRow.add(txtForEvery);
        tblRow.add(txtRateVal);
        tblRow.add(cboRateType);
        tblData.addRow(tblRow);
        System.out.println(chargesTO.toString());
        tableCharges.add(chargesTO);
        if(chargesTO.getStatus().equals("")){
            chargesTO.setStatus(CommonConstants.STATUS_CREATED);
            chargesTO.setStatusBy(TrueTransactMain.USER_ID);
            databaseOp.add(chargesTO) ;
        }
        System.out.println("Added to Table and to TO....");
    }
    
    public void addTabData(int row){
        try{
            ChargesTO chargesTO = setChargesTOData();
            if(getActionType() != ClientConstants.ACTIONTYPE_EDIT)
                addTableRow(chargesTO);
            else
                updateTableRow(chargesTO, row) ;
            
            setChanged();
            tblRow = null;
        }catch(Exception E){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            ClientParseException cpe = ClientParseException.getInstance();
            cpe.logException(E, true);
            
        }
    }
    
    /**
     * Getter for property tdtFromDt.
     * @return Value of property tdtFromDt.
     */
    public java.lang.String getTdtFromDt() {
        return tdtFromDt;
    }
    
    public void resetTable(){
        setTxtFromAmt("");
        setTxtToAmt("");
        setTdtFromDt("");
        setTdtToDt("");
        setTxtFixRate("") ;
    }
    
    /**
     * Setter for property tdtFromDt.
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(java.lang.String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }
    
    /**
     * Getter for property tdtToDt.
     * @return Value of property tdtToDt.
     */
    public java.lang.String getTdtToDt() {
        return tdtToDt;
    }
    
    /**
     * Setter for property tdtToDt.
     * @param tdtToDt New value of property tdtToDt.
     */
    public void setTdtToDt(java.lang.String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public ArrayList getTableCharges(){
        return tableCharges ;
    }
    
    /**
     * Getter for property databaseOp.
     * @return Value of property databaseOp.
     */
    public ArrayList getDatabaseOp() {
        return databaseOp;
    }
    
    
}