/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InwardClearingTallyOB.java
 *
 * Created on March 17, 2004, 5:31 PM
 */

package com.see.truetransact.ui.clearing.tally;

import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.clearing.tally.InwardClearingTallyRB;
import com.see.truetransact.transferobject.clearing.tally.InwardClearingTallyTO;
import com.see.truetransact.transferobject.clearing.tally.InwardTallyDetailsTO;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @author  rahul
 * modified by Annamalai
 * @modified Bala
 */
public class InwardClearingTallyOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmClearingType;
    private ComboBoxModel cbmCurrencyPC;
    private ComboBoxModel cbmCurrencyBID;
    
    private EnhancedTableModel tbmInstDet;
    private EnhancedTableModel tbmDifference;
    
    private ProxyFactory proxy = null;
    
    private String lblTallyId;
    private String lblBookedAmount;
    private String lblBookedInstrument;
    private String lblReturnAmount;
    private String lblReturnInstrument;
    private double excessAmt=0.0;
    private double shortAmt=0.0;
    private String mode="";
    
    private int option = -1;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private static InwardClearingTallyRB objInwardClearingTallyRB = new InwardClearingTallyRB();
    private final static Logger log = Logger.getLogger(InwardClearingTallyUI.class);
    
    final String INR = "Indian Rupees";
    
    private static InwardClearingTallyOB inwardClearingTallyOB;
    static {
        try {
            log.info("In InwardClearingTallyOB Declaration");
            inwardClearingTallyOB = new InwardClearingTallyOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static InwardClearingTallyOB getInstance() {
        return inwardClearingTallyOB;
    }
    
    /** Creates a new instance of InwardClearingTallyOB */
    public InwardClearingTallyOB() throws Exception {
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
        fillDropdown();// To Fill all the Combo Boxes
        initTableModel();
    }
    /*
     *Initializing table models for the Tables in UI
     */    
    private void initTableModel() {
        final ArrayList instColHead = new ArrayList();
        final ArrayList diffColHead = new ArrayList();
        
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol1"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol2"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol3"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol4"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol5"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol6"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol7"));        
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol8"));
        instColHead.add(objInwardClearingTallyRB.getString("tblInstCol9"));        
        tbmInstDet = new EnhancedTableModel(null,instColHead);
        
       diffColHead.add(objInwardClearingTallyRB.getString("tblDiffCol1"));
       diffColHead.add(objInwardClearingTallyRB.getString("tblDiffCol2"));
       diffColHead.add(objInwardClearingTallyRB.getString("tblDiffCol3"));
       tbmDifference = new EnhancedTableModel(null,diffColHead);        
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "InwardClearingTallyJNDI");
        operationMap.put(CommonConstants.HOME, "clearing.tally.InwardClearingTallyHome");
        operationMap.put(CommonConstants.REMOTE, "clearing.tally.InwardClearingTally");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        
        lookup_keys.add("FOREX.CURRENCY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmCurrencyPC = new ComboBoxModel(key,value);
        cbmCurrencyBID = new ComboBoxModel(key,value);
        
        //__ Data for the ClearingType Combo-Box...
        lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getInwardClearingType");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, TrueTransactMain.BRANCH_ID);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClearingType = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
//            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        
        InwardClearingTallyTO objInwardClearingTallyTO = null;
        //Taking the Value of Prod_Id from each Table...
        // Here the first Row is selected...
        objInwardClearingTallyTO = (InwardClearingTallyTO) ((List) mapData.get("InwardClearingTallyTO")).get(0);
        setInwardClearingTallyTO(objInwardClearingTallyTO);
        setInwardTallyDetailsTO((List)mapData.get("InwardTallyDetailsTO"));
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setInwardClearingTallyTO(InwardClearingTallyTO objInwardClearingTallyTO) throws Exception{
        log.info("In setInwardClearingTallyTO()");
        
        setLblTallyId (objInwardClearingTallyTO.getTallyId ());
        setCboClearingType((String) getCbmClearingType().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTallyTO.getClearingType())));
        setTxtScheduleNo(CommonUtil.convertObjToStr(objInwardClearingTallyTO.getScheduleNo()));
        setTdtClearingDate(DateUtil.getStringDate(objInwardClearingTallyTO.getClearingDt()));
    }
    
    /*
     *Setting the OB fields with the TO object. 
     */
    private void setInwardTallyDetailsTO(List objList) throws Exception{
        log.info("In setInwardTallyDetailsTO()");
        if(objList == null) {
            return;
        }
        
        InwardTallyDetailsTO objInwardTallyDetailsTO;
        int size = objList.size();
        List add2TblInst;
        List add2TblDiff;
        
        for(int i =0;i<size;i++) {
            objInwardTallyDetailsTO = (InwardTallyDetailsTO)objList.get(i);
            
            add2TblInst = new java.util.ArrayList();
//            add2TblInst.add(objInwardTallyDetailsTO.getCurrency());
            cbmCurrencyBID.setSelectedItem(INR);
            add2TblInst.add(cbmCurrencyBID.getKeyForSelected());
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysBookedInstruments()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysBookedAmount()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysOutretInstruments()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysOutretAmount()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getServInstruments()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getServAmount()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getPhyInstruments()));
            add2TblInst.add(CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getPhyAmount()));
            
            add2TblDiff = new java.util.ArrayList();
//            add2TblDiff.add(objInwardTallyDetailsTO.getCurrency());
            add2TblDiff.add(cbmCurrencyBID.getKeyForSelected());
            
            add2TblDiff.add(Double.valueOf(
                Integer.toString(CommonUtil.convertObjToInt(
                    objInwardTallyDetailsTO.getServInstruments())
                    -
                    (CommonUtil.convertObjToInt(objInwardTallyDetailsTO.getSysBookedInstruments()) +
                     CommonUtil.convertObjToInt(objInwardTallyDetailsTO.getSysOutretInstruments()))
                    )));
            add2TblDiff.add(Double.valueOf(
                Double.toString(
                    CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getServAmount()).doubleValue() 
                    -
                    (CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysBookedAmount()).doubleValue() + 
                     CommonUtil.convertObjToDouble(objInwardTallyDetailsTO.getSysOutretAmount()).doubleValue()))));
            
            tbmInstDet.addRow((java.util.ArrayList)add2TblInst);
            tbmDifference.addRow((java.util.ArrayList)add2TblDiff);
        }    
    }
    
    /*
     *Setting the TO object with the OB Fields. 
     */
    private List setInwardTallyDetails() throws Exception{
        log.info("In setInwardTallyDetailsTO()");
        InwardTallyDetailsTO objInwardTallyDetailsTO;
        List objList =  new java.util.ArrayList();
        int rows =  tbmInstDet.getRowCount();
        int rowIndex = 0; 
        for(int i =0;i<rows;i++) {
            objInwardTallyDetailsTO = new InwardTallyDetailsTO();
            
            objInwardTallyDetailsTO.setTallyId(lblTallyId);
            objInwardTallyDetailsTO.setScheduleNo(txtScheduleNo);
            objInwardTallyDetailsTO.setCurrency((String)tbmInstDet.getValueAt(i,0));
            objInwardTallyDetailsTO.setSysBookedInstruments(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,1)));
            objInwardTallyDetailsTO.setSysBookedAmount(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,2)));
            objInwardTallyDetailsTO.setSysOutretInstruments(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,3)));
            objInwardTallyDetailsTO.setSysOutretAmount(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,4)));
            objInwardTallyDetailsTO.setServInstruments(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,5)));
            objInwardTallyDetailsTO.setServAmount(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,6)));
            objInwardTallyDetailsTO.setPhyInstruments(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,7)));
            objInwardTallyDetailsTO.setPhyAmount(CommonUtil.convertObjToDouble(tbmInstDet.getValueAt(i,8)));

            rowIndex = currencyIndexInTbmDifference(objInwardTallyDetailsTO.getCurrency());
            if(rowIndex != -1) {
                objInwardTallyDetailsTO.setDiffInstruments(CommonUtil.convertObjToDouble(tbmDifference.getValueAt(rowIndex,1)));
                objInwardTallyDetailsTO.setDiffAmount(CommonUtil.convertObjToDouble(tbmDifference.getValueAt(rowIndex,2)));
            }   
            objList.add(objInwardTallyDetailsTO);
        }
        return objList;
    }
    
    //return the index of tbmDifference for currency.   
    private int currencyIndexInTbmDifference(String currency) {
        int rows = tbmDifference.getRowCount();
        int index = -1;
        for( int i=0;i<rows;i++) {
            if(currency.equals((String)tbmDifference.getValueAt(i,0))) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    //return the index of tbmDifference for currency. or -1   
    private int currencyIndexInTbmInstrDet(String currency) {
        int rows = tbmInstDet.getRowCount();
        int index = -1;
        for( int i=0;i<rows;i++) {
            if(currency.equals((String)tbmInstDet.getValueAt(i,0))) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public InwardClearingTallyTO setInwardClearingTally() {
        log.info("In setInwardClearingTally()");
        
        final InwardClearingTallyTO objInwardClearingTallyTO = new InwardClearingTallyTO();
        try{
            //objInwardClearingTO.setInwardId(lblTransactionId);
            //objInwardClearingTO.setAmount(CommonUtil.convertObjToDouble(txtAmount));
            objInwardClearingTallyTO.setTallyId(lblTallyId);
            objInwardClearingTallyTO.setClearingType((String)cbmClearingType.getKeyForSelected());
            objInwardClearingTallyTO.setScheduleNo(getTxtScheduleNo());
            Date IsDt = DateUtil.getDateMMDDYYYY(getTdtClearingDate());
            if(IsDt != null){
            Date isDate = ClientUtil.getCurrentDate();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            objInwardClearingTallyTO.setClearingDt(isDate);
            }else{
                objInwardClearingTallyTO.setClearingDt(DateUtil.getDateMMDDYYYY(getTdtClearingDate()));
            }
//            objInwardClearingTallyTO.setClearingDt(DateUtil.getDateMMDDYYYY(getTdtClearingDate()));
            objInwardClearingTallyTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objInwardClearingTallyTO.setStatusBy(TrueTransactMain.USER_ID);
            objInwardClearingTallyTO.setStatusDt(ClientUtil.getCurrentDate());
            
            if (actionType == ClientConstants.ACTIONTYPE_NEW) {
                objInwardClearingTallyTO.setTallyStatus(CommonConstants.OPEN);
            }
        }catch(Exception e){
            log.info("Error In setInwardClearingTally()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInwardClearingTallyTO;
    }
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setInwardClearingTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        System.out.println("MODE   "+getMode());
        final HashMap data = new HashMap();
        if(!getMode().equals("CLOSE")){
        final InwardClearingTallyTO objInwardClearingTallyTO = setInwardClearingTally();
        
        objInwardClearingTallyTO.setCommand(getCommand());
//        final HashMap data = new HashMap();
        data.put("InwardClearingTallyTO",objInwardClearingTallyTO);
        data.put("InwardTallyDetailsTO", setInwardTallyDetails());
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
         HashMap proxyResultMap = proxy.execute(data,operationMap);
        }else if(getMode().equals("CLOSE")){
             if(getExcessAmt()>0 || getShortAmt()<0){
                data.put("SCHEDULE_NO",getTxtScheduleNo());
                data.put("SCHEDULE_TYPE",(String)cbmClearingType.getKeyForSelected());
                data.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                data.put("Mode","CLOSE");
                if(getExcessAmt()>0){
                 data.put("Excess",new Double(getExcessAmt()));
                 data.put("TRNEXCESS","EXCESS");

                }else{
                    double shortAmt=getShortAmt();
                    shortAmt=shortAmt*-1;
                    data.put("Short",new Double(shortAmt));
                    data.put("TRNSHORT","SHORT");
                }
                 HashMap proxyResultMap = proxy.execute(data,operationMap);
            }
                HashMap closeMap = new HashMap();
                String tallyId = CommonUtil.convertObjToStr(getLblTallyId());
                closeMap.put("TALLY_ID", tallyId);
                closeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                ClientUtil.execute("closeInwardClearingTallyTO", closeMap);
        }
       
        setResult(actionType);
        resetForm();
    }
    
    // to decide which action Should be performed...
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
    
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        setCboClearingType("");
        setTxtScheduleNo("");
        setTdtClearingDate("");
        setTxtServiceInstruments("");
        setTxtServiceAmount("");
        setTxtDifferenveInstrument("");
        setTxtDifferenceAmount("");
        setTxtPhysicalInstruments("");
        setTxtPhysicalAmount("");
        setCboCurrencyBID("");
        setCboCurrencyPC("");
        setMode("");
        resetTabelModel(tbmInstDet); 
        resetTabelModel(tbmDifference); 
        ttNotifyObservers();
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        this.setLblBookedAmount("");
        this.setLblBookedInstrument("");
        this.setLblReturnAmount("");
        this.setLblReturnInstrument("");
    }
    
    private String cboCurrencyBID = "";
    private String cboCurrencyPC = "";
    private String cboClearingType = "";
    private String txtScheduleNo = "";
    private String tdtClearingDate = "";
    private String txtServiceInstruments = "";
    private String txtServiceAmount = "";
    private String txtDifferenveInstrument = "";
    private String txtDifferenceAmount = "";
    private String txtPhysicalInstruments = "";
    private String txtPhysicalAmount = "";
    
    void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
        setChanged();
    }
    String getCboClearingType(){
        return this.cboClearingType;
    }
    
    public void setCbmClearingType(ComboBoxModel cbmClearingType){
        this.cbmClearingType = cbmClearingType;
        setChanged();
    }
    
    ComboBoxModel getCbmClearingType(){
        return cbmClearingType;
    }
    
    void setTxtScheduleNo(String txtScheduleNo){
        this.txtScheduleNo = txtScheduleNo;
        setChanged();
    }
    String getTxtScheduleNo(){
        return this.txtScheduleNo;
    }
    
    void setTdtClearingDate(String tdtClearingDate){
        this.tdtClearingDate = tdtClearingDate;
        setChanged();
    }
    String getTdtClearingDate(){
        return this.tdtClearingDate;
    }
    
    void setTxtServiceInstruments(String txtServiceInstruments){
        this.txtServiceInstruments = txtServiceInstruments;
        setChanged();
    }
    String getTxtServiceInstruments(){
        return this.txtServiceInstruments;
    }
    
    void setTxtServiceAmount(String txtServiceAmount){
        this.txtServiceAmount = txtServiceAmount;
        setChanged();
    }
    String getTxtServiceAmount(){
        return this.txtServiceAmount;
    }
    
    void setTxtDifferenveInstrument(String txtDifferenveInstrument){
        this.txtDifferenveInstrument = txtDifferenveInstrument;
        setChanged();
    }
    String getTxtDifferenveInstrument(){
        return this.txtDifferenveInstrument;
    }
    
    void setTxtDifferenceAmount(String txtDifferenceAmount){
        this.txtDifferenceAmount = txtDifferenceAmount;
        setChanged();
    }
    String getTxtDifferenceAmount(){
        return this.txtDifferenceAmount;
    }
    
    void setTxtPhysicalInstruments(String txtPhysicalInstruments){
        this.txtPhysicalInstruments = txtPhysicalInstruments;
        setChanged();
    }
    String getTxtPhysicalInstruments(){
        return this.txtPhysicalInstruments;
    }
    
    void setTxtPhysicalAmount(String txtPhysicalAmount){
        this.txtPhysicalAmount = txtPhysicalAmount;
        setChanged();
    }
    String getTxtPhysicalAmount(){
        return this.txtPhysicalAmount;
    }
    
    //==========================================================
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To set and change the Status of the lable STATUS
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
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    //=======================================================================
    // Fo setting the transaction Id in UI at the Time of Edit or Delete...lblTallyId
    public void setLblTallyId(String lblTallyId){
        this.lblTallyId = lblTallyId;
        setChanged();
    }
    public String getLblTallyId(){
        return this.lblTallyId;
    }
    
    public void setLblBookedAmount(String lblBookedAmount){
        this.lblBookedAmount = lblBookedAmount;
        setChanged();
    }
    public String getLblBookedAmount(){
        return this.lblBookedAmount;
    }
    
    public void setLblBookedInstrument(String lblBookedInstrument){
        this.lblBookedInstrument = lblBookedInstrument;
        setChanged();
    }
    public String getLblBookedInstrument(){
        return this.lblBookedInstrument;
    }
    
    public void setLblReturnAmount(String lblReturnAmount){
        this.lblReturnAmount = lblReturnAmount;
        setChanged();
    }
    public String getLblReturnAmount(){
        return this.lblReturnAmount;
    }
    
    public void setLblReturnInstrument(String lblReturnInstrument){
        this.lblReturnInstrument = lblReturnInstrument;
        setChanged();
    }
    public String getLblReturnInstrument(){
        return this.lblReturnInstrument;
    }
        
    public HashMap setClearingData(String clearing, String Schedule){
        HashMap resultMap = new HashMap();
        try {
            final HashMap clearingDataMap = new HashMap();
            clearingDataMap.put("CLEARINGTYPE",clearing);
            clearingDataMap.put("SCHEDULENO",Schedule);
            final List resultList = ClientUtil.executeQuery("getSelectClearingDataTO", clearingDataMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
//            e.printStackTrace();
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    public boolean checkScheduleNumber(){
        HashMap hash = new HashMap();
        boolean ScheduleNumber = true;
        hash.put("SCHEDULE_NO", getTxtScheduleNo());
        hash.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        final List resultList = ClientUtil.executeQuery("viewScheduleNumberInwardClearingTally", hash);
        if(!resultList.isEmpty()){
            ScheduleNumber = false;
                String[] options = {objInwardClearingTallyRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objInwardClearingTallyRB.getString("dateWarning"), CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                setTxtScheduleNo("");
                notifyObservers();
        }
        return ScheduleNumber;
    }
    
    /** Getter for property cbmCurrencyPC.
     * @return Value of property cbmCurrencyPC.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCurrencyPC() {
        return cbmCurrencyPC;
    }
    
    /** Setter for property cbmCurrencyPC.
     * @param cbmCurrencyPC New value of property cbmCurrencyPC.
     *
     */
    public void setCbmCurrencyPC(com.see.truetransact.clientutil.ComboBoxModel cbmCurrencyPC) {
        this.cbmCurrencyPC = cbmCurrencyPC;
    }
    
    /** Getter for property cbmCurrencyBID.
     * @return Value of property cbmCurrencyBID.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCurrencyBID() {
        return cbmCurrencyBID;
    }
    
    /** Setter for property cbmCurrencyBID.
     * @param cbmCurrencyBID New value of property cbmCurrencyBID.
     *
     */
    public void setCbmCurrencyBID(com.see.truetransact.clientutil.ComboBoxModel cbmCurrencyBID) {
        this.cbmCurrencyBID = cbmCurrencyBID;
    }
    
    /** Getter for property tbmDifference.
     * @return Value of property tbmDifference.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmDifference() {
        return tbmDifference;
    }
    
    /** Setter for property tbmDifference.
     * @param tbmDifference New value of property tbmDifference.
     *
     */
    public void setTbmDifference(com.see.truetransact.clientutil.EnhancedTableModel tbmDifference) {
        this.tbmDifference = tbmDifference;
    }
    
    /** Getter for property tbmInstDet.
     * @return Value of property tbmInstDet.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstDet() {
        return tbmInstDet;
    }
    
    /** Setter for property tbmInstDet.
     * @param tbmInstDet New value of property tbmInstDet.
     *
     */
    public void setTbmInstDet(com.see.truetransact.clientutil.EnhancedTableModel tbmInstDet) {
        this.tbmInstDet = tbmInstDet;
    }
    
    /*
     * This method will check if the currency exist in tbmInstDet and set the 
     * BranchInstruments Details components.  
     */
    public void populateSBData4Currency(String currency) {
        int rowIndex = currencyIndexInTbmInstrDet(currency);
        if(rowIndex!=-1) {
            txtServiceInstruments = CommonUtil.convertObjToStr(tbmInstDet.getValueAt(rowIndex, 5));
            txtServiceAmount = CommonUtil.convertObjToStr(tbmInstDet.getValueAt(rowIndex, 6));
        } else {
            txtServiceInstruments = "";
            txtServiceAmount = "";
        }
        
    }
    
    /*
     * This method will check if the currency exist in tbmInstDet and set the 
     * Physical count Instruments Details components.  
     */
    public void populatePCData4Currency(String currency) {
        int rowIndex = currencyIndexInTbmInstrDet(currency);
        if(rowIndex!=-1) {
            txtPhysicalInstruments = CommonUtil.convertObjToStr(tbmInstDet.getValueAt(rowIndex, 7));
            txtPhysicalAmount = CommonUtil.convertObjToStr(tbmInstDet.getValueAt(rowIndex, 8));
        } else {
            txtPhysicalInstruments = "";
            txtPhysicalAmount = "";
        }
        
    }
    /** Getter for property cboCurrencyBID.
     * @return Value of property cboCurrencyBID.
     *
     */
    public java.lang.String getCboCurrencyBID() {
        return cboCurrencyBID;
    }
    
    /** Setter for property cboCurrencyBID.
     * @param cboCurrencyBID New value of property cboCurrencyBID.
     *
     */
    public void setCboCurrencyBID(java.lang.String cboCurrencyBID) {
        this.cboCurrencyBID = cboCurrencyBID;
    }
    
    /** Getter for property cboCurrencyPC.
     * @return Value of property cboCurrencyPC.
     *
     */
    public java.lang.String getCboCurrencyPC() {
        return cboCurrencyPC;
    }
    
    /** Setter for property cboCurrencyPC.
     * @param cboCurrencyPC New value of property cboCurrencyPC.
     *
     */
    public void setCboCurrencyPC(java.lang.String cboCurrencyPC) {
        this.cboCurrencyPC = cboCurrencyPC;
    }
    
    /*
     *Saving the Service Branch details 2 table Instrumments Details.
     */
    public void save2TbmInstDet() {
        try{
            int rowIndex = currencyIndexInTbmInstrDet(cboCurrencyBID);
            if(rowIndex != -1) {
                tbmInstDet.setValueAt(CommonUtil.convertObjToDouble(txtServiceInstruments), rowIndex, 5);
                tbmInstDet.setValueAt(CommonUtil.convertObjToDouble(txtServiceAmount), rowIndex, 6);
            } else if(cboCurrencyBID!=null && !cboCurrencyBID.equals("")){
                List objList = new ArrayList();
                objList.add(cboCurrencyBID);
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(CommonUtil.convertObjToDouble(txtServiceInstruments));
                objList.add(CommonUtil.convertObjToDouble(txtServiceAmount));
                objList.add(new Double(0));
                objList.add(new Double(0));
                tbmInstDet.addRow((ArrayList)objList);
            }
        }catch(Exception e){
//            e.printStackTrace();
            parseException.logException(e,true);
        }   
    }
    
    /*
     *Saving the Physical COunt details 2 table Instrumments Details.
     */
    public void savePC2TbmInstDet() {
        try{
            int rowIndex = currencyIndexInTbmInstrDet(cboCurrencyPC);
            if(rowIndex != -1) {
                tbmInstDet.setValueAt(CommonUtil.convertObjToDouble(txtPhysicalInstruments), rowIndex, 7);
                tbmInstDet.setValueAt(CommonUtil.convertObjToDouble(txtPhysicalAmount), rowIndex, 8);
            } else if(cboCurrencyPC!=null && !cboCurrencyPC.equals("")){
                List objList = new ArrayList();
                objList.add(cboCurrencyPC);
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(new Double(0));
                objList.add(CommonUtil.convertObjToDouble(txtPhysicalInstruments));
                objList.add(CommonUtil.convertObjToDouble(txtPhysicalAmount));
                tbmInstDet.addRow((ArrayList)objList);
            }
        }catch(Exception e){
//            e.printStackTrace();
            parseException.logException(e,true);
        }   
    }
    
    public void resetBIDPanel() {
        cboCurrencyBID = "";
        txtServiceInstruments="";
        txtServiceAmount="";
    }
    
    public void resetPCPanel() {
        cboCurrencyPC = "";
        txtPhysicalInstruments="";
        txtPhysicalAmount="";
    }
    
    public void setDifference(String currency) {
        int rowIndex = currencyIndexInTbmInstrDet(currency);
        List objList = new ArrayList();
        Double doubleVal;
        if(rowIndex==-1) {
            return;
        } 
        int rowIndexDiff =  currencyIndexInTbmDifference(cboCurrencyBID);
        if(rowIndexDiff != -1) {
            doubleVal = new Double( ((Double)tbmInstDet.getValueAt(rowIndex,5)).intValue() 
                    - 
                    (((Double)tbmInstDet.getValueAt(rowIndex,1)).intValue() 
                    + ((Double)tbmInstDet.getValueAt(rowIndex,3)).intValue()));
            tbmDifference.setValueAt(doubleVal,rowIndexDiff,1);
            doubleVal = new Double(((Double)tbmInstDet.getValueAt(rowIndex,6)).intValue() 
                    - 
                    (((Double)tbmInstDet.getValueAt(rowIndex,2)).intValue()
                    + ((Double)tbmInstDet.getValueAt(rowIndex,4)).intValue()));
            tbmDifference.setValueAt(doubleVal,rowIndexDiff,2);
        } else {
            objList.add(currency); 
            objList.add(new Double( ((Double)tbmInstDet.getValueAt(rowIndex,7)).intValue() 
                - 
                (((Double)tbmInstDet.getValueAt(rowIndex,1)).intValue() + 
                ((Double)tbmInstDet.getValueAt(rowIndex,3)).intValue())));
            objList.add(new Double( ((Double)tbmInstDet.getValueAt(rowIndex,8)).intValue() 
                - (((Double)tbmInstDet.getValueAt(rowIndex,2)).intValue() + 
                ((Double)tbmInstDet.getValueAt(rowIndex,4)).intValue())));
            tbmDifference.addRow((ArrayList)objList);
        }
    }
    
    private void resetTabelModel(EnhancedTableModel tbm) {
        int rows = tbm.getRowCount();
        for(int i =0;i<rows;i++) {
            tbm.removeRow(0);
        }
    }
    
    /**
     * Getter for property excessAmt.
     * @return Value of property excessAmt.
     */
    public double getExcessAmt() {
        return excessAmt;
    }
    
    /**
     * Setter for property excessAmt.
     * @param excessAmt New value of property excessAmt.
     */
    public void setExcessAmt(double excessAmt) {
        this.excessAmt = excessAmt;
    }
    
    /**
     * Getter for property shortAmt.
     * @return Value of property shortAmt.
     */
    public double getShortAmt() {
        return shortAmt;
    }
    
    /**
     * Setter for property shortAmt.
     * @param shortAmt New value of property shortAmt.
     */
    public void setShortAmt(double shortAmt) {
        this.shortAmt = shortAmt;
    }
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public java.lang.String getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    
}
