/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTallyOB.java
 *
 * Created on March 23, 2004, 5:19 PM
 */
package com.see.truetransact.ui.clearing.outwardtally;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.clearing.outwardtally.OutwardClearingTallyTO;
import com.see.truetransact.transferobject.clearing.outwardtally.OutwardTallyDetailsTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientproxy.ProxyParameters;
/**
 *
 * @author  Prasath.T
 */
public class OutwardClearingTallyOB  extends Observable {
    // Variables declaration - do not modify
   Date curDate = null;
    private String cboClearingType = "";
    private String txtScheduleNo = "";
    private String tdtClearingDate = "";
    private String tdtClosingDate = "";
    private static OutwardClearingTallyOB objoutwardClearingTallyOB; // singleton object
    private final static Logger log = Logger.getLogger(OutwardClearingTallyOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private OutwardClearingTallyRB objOutwardClearingTallyRB = new OutwardClearingTallyRB();
    private ProxyFactory proxy;
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ComboBoxModel cbmClearingType;
    private ArrayList key;
    private ArrayList value;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private String lblDisplayCurrentClearingDate = "";
    private String txtSBNumberofInstruments  = "";
    private String txtSBAmount  = "";
    private double excessAmt=0.0;
    private double shortAmt=0.0;
    private final int CLOSING_DATE=0,CLEARG_TYPE=1;
    
    private EnhancedTableModel tbmDifference;
    private EnhancedTableModel tbmInstDet;
    
    // End of variables declaration
    
    /** Creates a new instance of OutwardClearingTallyOB */
    public OutwardClearingTallyOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initTableModel();
            fillDropdown();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating OutwardClearingTallyOB...");
            objoutwardClearingTallyOB = new OutwardClearingTallyOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    //Initializing table models.
    private void initTableModel() {
        tbmDifference = new EnhancedTableModel(
        new Object [][] {
            
        },
        new String [] {
            "Currency", "No of Instruments", "Instrument Amount"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
        tbmInstDet =  new EnhancedTableModel(
        new Object [][] {
            
        },
        new String [] {
            "Currency", "Outward (S)", "Outward Amt (S)", "Inward Return (S)", "Inward Return Amt (S)", "No of Inst-Service", "Inst Amt-Service"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
    }
    /* To fill the ComboBoxes with data */
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown method:");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"getSelectClearingType");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, ProxyParameters.BRANCH_ID);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClearingType = new ComboBoxModel(key,value);
    }
    
    /* To get the key and values required for combobox clearingType*/
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    // Getter method for cbmClearingType
    public void setCbmClearingType(ComboBoxModel cbmClearingType){
        this.cbmClearingType = cbmClearingType;
        setChanged();
    }
    // Setter method for cbmClearingType
    ComboBoxModel getCbmClearingType(){
        return cbmClearingType;
    }
    // Setter method for lblStatus
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    // Getter method for lblStatus
    public String getLblStatus(){
        return this.lblStatus;
    }
    // To set the status based on ActionType, either New, Edit, etc.,
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    // To reset the status
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    // Getter method for result
    public int getResult(){
        return this.result;
    }
    // Setter method for actionType
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    // Getter method for actionType
    public int getActionType(){
        return this.actionType;
    }
    // internally calls update in UI
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    // Setter method for cboClearingType
    void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
        setChanged();
    }
    // Getter method for cboClearingType
    String getCboClearingType(){
        return this.cboClearingType;
    }
    
    // Setter method for txtScheduleNo
    void setTxtScheduleNo(String txtScheduleNo){
        this.txtScheduleNo = txtScheduleNo;
        setChanged();
    }
    // Getter method for txtScheduleNo
    String getTxtScheduleNo(){
        return this.txtScheduleNo;
    }
    
    // Setter method for tdtClearingDate
    void setTdtClearingDate(String tdtClearingDate){
        this.tdtClearingDate = tdtClearingDate;
        setChanged();
    }
    // Getter method for tdtClearingDate
    String getTdtClearingDate(){
        return this.tdtClearingDate;
    }
    
    // Setter method for tdtClosingDate
    void setTdtClosingDate(String tdtClosingDate){
        this.tdtClosingDate = tdtClosingDate;
        setChanged();
    }
    // Getter method for tdtClosingDate
    String getTdtClosingDate(){
        return this.tdtClosingDate;
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "OutwardClearingTallyJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.clearing.outwardtally.OutwardClearingTallyHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.clearing.outwardtally.OutwardClearingTally");
    }
    public static OutwardClearingTallyOB getInstance() {
        return objoutwardClearingTallyOB;
    }
    
    // To reset the fields present in the Outward Clearing Tally
    public void resetForm() {
        setCboClearingType("");
        setTxtScheduleNo("");
        setTdtClearingDate("");
        setTdtClosingDate("");
        resetTabelModel(tbmDifference);
        resetTabelModel(tbmInstDet);
        setTxtSBAmount("");
        setTxtSBNumberofInstruments("");
    }
    /* To set the lblDisplayCurrentClearingDate to display Issue_HD from REMITTANCE_PRODUCT */
    public void setLblCurrentClearingDateDisplay(String lblDisplayCurrentClearingDate){
        this.lblDisplayCurrentClearingDate = lblDisplayCurrentClearingDate;
        setChanged();
    }
    /* To get the value in lblDisplayCurrentClearingDate */
    public String getLblCurrentClearingDateDisplay(){
        return this.lblDisplayCurrentClearingDate;
    }
    
    /** To retrive  Issue_Id  based on Product Id to display for lblAccHead*/
    public void getCurrentClearingDateForClearingType() {
        log.info("Inside getCurrentClearingDateForClearingType()");
        try {
            final HashMap clearingTypeMap = new HashMap();
            clearingTypeMap.put("CLEARING_TYPE",CommonUtil.convertObjToStr((String)getCbmClearingType().getKeyForSelected()));
            clearingTypeMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            final List resultList = (List) ClientUtil.executeQuery("getCurrentClearingDateForClearingType", clearingTypeMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            if (resultMap.get("CURRENT_CLEARING_DATE") != null){
                setLblCurrentClearingDateDisplay(CommonUtil.convertObjToStr(DateUtil.getStringDate((java.util.Date)resultMap.get("CURRENT_CLEARING_DATE"))));
            } else {
                setLblCurrentClearingDateDisplay("");
            }
        }catch(Exception e){
            parseException.logException(e,true);
            
        }
    }
    
    /* The data required for System is retrived from table OUTWARD CLEARING
     * No of Instruments and sum of amount
     * /
    public void getForSystem() {
        log.info("In getForSystem method:");
        try{
            final HashMap where  = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("SCHEDULE_NO",getTxtScheduleNo());
            if(where.get("SCHEDULE_NO") != null ){
                final List resultList = (List) ClientUtil.executeQuery("sumOutwardClearingTally", where);
                final HashMap resultMap = (HashMap)resultList.get(0);
                if(resultMap.get("NO_OF_INSTRUMENT") != null){
                    setBookedNoOfInstruments(CommonUtil.convertObjToStr(resultMap.get("NO_OF_INSTRUMENT")));
                }else{
                    setBookedNoOfInstruments("0");
                }
                if(resultMap.get("SUM_OF_AMOUNT") != null){
                    setBookedAmount(CommonUtil.convertObjToStr(resultMap.get("SUM_OF_AMOUNT")));
                }else {
                    setBookedAmount("0");
                }
                if(resultMap.get("NO_OF_INSTRUMENT") != null){
                    setTotalNoOfInstruments(CommonUtil.convertObjToStr(resultMap.get("NO_OF_INSTRUMENT")));
                } else{
                    setTotalNoOfInstruments("0");
                }
                if(resultMap.get("SUM_OF_AMOUNT") != null){
                    setTotalAmount(CommonUtil.convertObjToStr(resultMap.get("SUM_OF_AMOUNT")));
                } else {
                    setTotalAmount("0");
                }
            }
        }catch (Exception e){
            parseException.logException(e,true);
        }
     
    }*/
    
    
    // To set the data in the UI
    private void setOutwardClearingTallyTO(OutwardClearingTallyTO objOutwardClearingTallyTO) throws Exception {
        log.info("In setOutwardClearingTallyTO method:");
        setCboClearingType((String) getCbmClearingType().getDataForKey(CommonUtil.convertObjToStr(objOutwardClearingTallyTO.getClearingType())));
        setTxtScheduleNo(CommonUtil.convertObjToStr(objOutwardClearingTallyTO.getScheduleNo()));
        setTdtClearingDate(CommonUtil.convertObjToStr(DateUtil.getStringDate((java.util.Date)objOutwardClearingTallyTO.getClearingDt())));
        setTdtClosingDate(CommonUtil.convertObjToStr(DateUtil.getStringDate((java.util.Date)objOutwardClearingTallyTO.getClosingDt())));
    }
    
    // To set the data in the UI
    private void setOutwardTallyDetailsTO(java.util.List objList) throws Exception {
        log.info("In setOutwardClearingTallyTO method:");
        OutwardTallyDetailsTO objOutwardTallyDetailsTO;
        List add2InstTbl;
        List add2DiffTbl;
        
        if(objList == null) {
            return;
        }
        int size = objList.size();
        for(int i=0; i < size ; i++ ) {
            objOutwardTallyDetailsTO = (OutwardTallyDetailsTO)objList.get(i);
            
            add2InstTbl = new ArrayList();
            add2InstTbl.add(objOutwardTallyDetailsTO.getCurrency());
//            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysBookedInstruments()));
             add2InstTbl.add(
                Double.valueOf(
                    Integer.toString(
                        (CommonUtil.convertObjToInt(objOutwardTallyDetailsTO.getSysBookedInstruments()))
                         -
                         (CommonUtil.convertObjToInt(objOutwardTallyDetailsTO.getSysInretInstruments())))));
             
//            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysBookedAmount()));
             add2InstTbl.add(
                Double.valueOf(
                    Double.toString(
                        (CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysBookedAmount()).doubleValue())
                        - 
                        (CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysInretAmount()).doubleValue()))));
             
            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysInretInstruments()));
            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysInretAmount()));
            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getServInstruments()));
            add2InstTbl.add(CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getServAmount()));
            
            
            add2DiffTbl = new ArrayList();
            add2DiffTbl.add(objOutwardTallyDetailsTO.getCurrency());
            add2DiffTbl.add(Double.valueOf(
                Integer.toString(
                    (CommonUtil.convertObjToInt(objOutwardTallyDetailsTO.getSysBookedInstruments()))
//                    + 
//                     CommonUtil.convertObjToInt(objOutwardTallyDetailsTO.getSysInretInstruments()))
                    -
                    CommonUtil.convertObjToInt(objOutwardTallyDetailsTO.getServInstruments()))));
            add2DiffTbl.add(Double.valueOf(
                Double.toString(
                    (CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysBookedAmount()).doubleValue())
//                    +
//                     CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getSysInretAmount()).doubleValue())
                    -
                     CommonUtil.convertObjToDouble(objOutwardTallyDetailsTO.getServAmount()).doubleValue())));
            tbmInstDet.addRow((ArrayList)add2InstTbl);
            tbmDifference.addRow((ArrayList)add2DiffTbl);
        }
    }
    
    
    /* To set data in the Transfer Object*/
    public OutwardClearingTallyTO setOutwardClearingTallyData() {
        log.info("Inside setRemittanceIssueData()");
        final OutwardClearingTallyTO objOutwardClearingTallyTO = new OutwardClearingTallyTO();
        try{
            objOutwardClearingTallyTO.setClearingType(CommonUtil.convertObjToStr((String)getCbmClearingType().getKeyForSelected()));
            objOutwardClearingTallyTO.setScheduleNo(CommonUtil.convertObjToStr(getTxtScheduleNo()));
//            objOutwardClearingTallyTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtClearingDate())));
//            objOutwardClearingTallyTO.setClosingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtClosingDate())));
            Date TdClrDt = DateUtil.getDateMMDDYYYY(getTdtClearingDate());
            if(TdClrDt != null){
            Date tdclrDate = (Date)curDate.clone();
            tdclrDate.setDate(TdClrDt.getDate());
            tdclrDate.setMonth(TdClrDt.getMonth());
            tdclrDate.setYear(TdClrDt.getYear());
            objOutwardClearingTallyTO.setClearingDt(tdclrDate);
            }else{
                objOutwardClearingTallyTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtClearingDate())));
            }
            
            Date TdClDt = DateUtil.getDateMMDDYYYY(getTdtClosingDate());
            if(TdClDt != null){
            Date tdclDate = (Date)curDate.clone();
            tdclDate.setDate(TdClDt.getDate());
            tdclDate.setMonth(TdClDt.getMonth());
            tdclDate.setYear(TdClDt.getYear());
            objOutwardClearingTallyTO.setClosingDt(tdclDate);
            }else{
              objOutwardClearingTallyTO.setClosingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtClosingDate()))); 
            }
            objOutwardClearingTallyTO.setStatus(CommonUtil.convertObjToStr(getCommand()));
            objOutwardClearingTallyTO.setBranchId(ProxyParameters.BRANCH_ID);
            objOutwardClearingTallyTO.setStatusBy(ProxyParameters.USER_ID);
            objOutwardClearingTallyTO.setStatusDt(curDate);
            if( actionType == ClientConstants.ACTIONTYPE_NEW ){
                objOutwardClearingTallyTO.setTallyStatus(CommonConstants.OPEN);
            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return objOutwardClearingTallyTO;
    }
    
    private List setOutwardTallyDetailsData() throws Exception {
        log.info("In setOutwardClearingTallyTO method:");
        OutwardTallyDetailsTO objOutwardTallyDetailsTO;
        List data = new ArrayList();
        
        int rows = tbmInstDet.getRowCount();
        for(int i=0; i < rows ; i++ ) {
            objOutwardTallyDetailsTO = new OutwardTallyDetailsTO();
            
            objOutwardTallyDetailsTO.setScheduleNo(getTxtScheduleNo());
            objOutwardTallyDetailsTO.setCurrency((String)tbmInstDet.getValueAt(i,0));
            objOutwardTallyDetailsTO.setSysBookedInstruments((Double)tbmInstDet.getValueAt(i,1));
            objOutwardTallyDetailsTO.setSysBookedAmount((Double)tbmInstDet.getValueAt(i,2));
            objOutwardTallyDetailsTO.setSysInretInstruments((Double)tbmInstDet.getValueAt(i,3));
            objOutwardTallyDetailsTO.setSysInretAmount((Double)tbmInstDet.getValueAt(i,4));
            objOutwardTallyDetailsTO.setServInstruments((Double)tbmInstDet.getValueAt(i,5));
            objOutwardTallyDetailsTO.setServAmount((Double)tbmInstDet.getValueAt(i,6));
            
            objOutwardTallyDetailsTO.setDiffInstruments((Double)tbmDifference.getValueAt(i,1));
            objOutwardTallyDetailsTO.setDiffAmount((Double)tbmDifference.getValueAt(i,2));
            
            data.add(objOutwardTallyDetailsTO);
        }
        return data;
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
            default:
        }
        return command;
    }
    /** To perform the appropriate operation */
    public void doAction(String mode) {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform(mode);
                }
                else{
                    final OutwardClearingTallyRB objOutwardClearingTallyRB = new OutwardClearingTallyRB();
                    throw new TTException(objOutwardClearingTallyRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform(String mode) throws Exception{
        log.info("Inside doActionPerform()");
        OutwardClearingTallyTO objOutwardClearingTallyTO = setOutwardClearingTallyData();
        java.util.List objList = setOutwardTallyDetailsData();
        
        HashMap data = new HashMap();
        if (mode.equals("CLOSE")) {
            data.put("SCHEDULE_NO", getTxtScheduleNo());
            data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
             if(getExcessAmt()<0 || getShortAmt()>0){
                data.put("SCHEDULE_NO",getTxtScheduleNo());
                data.put("SCHEDULE_TYPE",(String)cbmClearingType.getKeyForSelected());
                data.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                data.put("Mode","CLOSE");
                if(getExcessAmt()<0){
                     double shortAmt=getExcessAmt();
                    shortAmt=shortAmt*-1;
                 data.put("Excess",new Double(shortAmt));
                 data.put("TRNEXCESS","EXCESS");

                }else{
                    double shortAmt=getShortAmt();
//                    shortAmt=shortAmt*-1;
                    data.put("Short",new Double(shortAmt));
                    data.put("TRNSHORT","SHORT");
                }
        } 
        }
        data.put("OutwardClearingTallyTO",objOutwardClearingTallyTO);
        data.put("OutwardTallyDetailsTO",objList);
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            ClientUtil.showMessageWindow ("Schedule No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }

        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    // Setter method for result
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        log.info("Inside populateData()");
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("mapData:"+mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    // To populate  the data in UI retrived from the database
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In PopulateOB method:");
        OutwardClearingTallyTO objOutwardClearingTallyTO = (OutwardClearingTallyTO)((List) mapData.get("OutwardClearingTallyTO")).get(0);
        java.util.List objList = (List) mapData.get("OutwardTallyDetailsTO");
        setOutwardClearingTallyTO(objOutwardClearingTallyTO);
        setOutwardTallyDetailsTO(objList);
        ttNotifyObservers();
    }
    public String checkForDate() {
        log.info("In checkForDate method:");
        String returnStr = null;
        try{
            returnStr = checkDate(setOutwardClearingTallyData());
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnStr;
    }
    
    private String  checkDate(OutwardClearingTallyTO outwardClearingTallyTO) throws Exception {
        log.info("In checkDate method:");
        String returnStr = null;
        Date closingDate = (java.util.Date) outwardClearingTallyTO.getClosingDt();
        Date clearingDate = (java.util.Date) outwardClearingTallyTO.getClearingDt();
        if(closingDate != null && closingDate.compareTo(clearingDate) < 0){
            returnStr = setWaringMessage(CLOSING_DATE);
        }
        return returnStr;
    }
    /* To Check for duplicaton of Clearing Type */
    public String checkForClearingTypeAndClearingDate(int selectedIndex,String clearingDt) {
        log.info("In checkForClearingTypeAndClearingDate method:");
        int COUNTCT = 0,COUNTDT = 0;
        String returnStr = null;
        final int ZERO = 0;
        String selectedClearingType = CommonUtil.convertObjToStr((String)cbmClearingType.getKey(selectedIndex));
        try {
            /* If the selected value in the combobox is not null proceed */
            if ( !(selectedClearingType.equals("") ) ) {
                HashMap where = new HashMap();
                where.put("CLEARING_TYPE",selectedClearingType);
                where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                
                Date ClDt = DateUtil.getDateMMDDYYYY(clearingDt);
                if(ClDt != null){
                Date clDate = (Date)curDate.clone();
                clDate.setDate(ClDt.getDate());
                clDate.setMonth(ClDt.getMonth());
                clDate.setYear(ClDt.getYear());
//                where.put("CLEARING_DATE", DateUtil.getDateMMDDYYYY(clearingDt));
                where.put("CLEARING_DATE", clDate);
                }else{
                    where.put("CLEARING_DATE", DateUtil.getDateMMDDYYYY(clearingDt));
                }
                List clearingTypeCount = ClientUtil.executeQuery("countOutwardClearingTally", where);
                where = null;
                COUNTCT = Integer.parseInt(((HashMap)clearingTypeCount.get(0)).get("COUNTCT").toString());
                /* If count is greater than zero then there is a duplication of clearingtype */
                if( (COUNTCT > ZERO )) {
                    returnStr = setWaringMessage(CLEARG_TYPE);
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return returnStr;
    }
    /* To set the Warning Message */
    private String setWaringMessage(int mode) throws Exception {
        int MODE = mode;
        String warningMessage = new String();
        if(MODE == CLOSING_DATE){
            warningMessage = objOutwardClearingTallyRB.getString("WarningForDate");
        }else if (MODE == CLEARG_TYPE){
            warningMessage = objOutwardClearingTallyRB.getString("WarningMessage");
        }
        return warningMessage;
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
    
    public void populateBIDFromTable(int rowIndex) {
        this.setTxtSBNumberofInstruments(((Double)tbmInstDet.getValueAt(rowIndex,5)).toString());
        this.setTxtSBAmount(((Double)tbmInstDet.getValueAt(rowIndex,6)).toString());
        notifyObservers();
    }
    
    public boolean saveSBDetails2Table(int rowIndex) {
        boolean saved = false;
        try {
            tbmInstDet.setValueAt(new Double(getTxtSBNumberofInstruments()), rowIndex, 5);
            tbmInstDet.setValueAt(new Double(getTxtSBAmount()), rowIndex, 6);
            int i=0, rows = tbmDifference.getRowCount();
            for(i=0;i<rows;i++){
                if((CommonUtil.convertObjToStr(tbmDifference.getValueAt(i,0))).equals(CommonUtil.convertObjToStr(tbmInstDet.getValueAt(rowIndex,0)))) {
                    break;
                }
            }
            if(i<rows) {
                int diffInst = (((Double)tbmInstDet.getValueAt(rowIndex, 1)).intValue() 
                                + 
                                ((Double)tbmInstDet.getValueAt(rowIndex, 3)).intValue()) 
                                - ((Double)tbmInstDet.getValueAt(rowIndex, 5)).intValue();
                double diffAmt = (((Double)tbmInstDet.getValueAt(rowIndex, 2)).doubleValue()
                                + 
                                ((Double)tbmInstDet.getValueAt(rowIndex, 4)).doubleValue())
                                - ((Double)tbmInstDet.getValueAt(rowIndex, 6)).doubleValue();
                tbmDifference.setValueAt(new Double(diffInst),i, 1);
                tbmDifference.setValueAt(new Double(diffAmt),i, 2);
            }
            saved = true;
        } catch (Exception e) {
            saved = false;
            e.printStackTrace();
        }
        return saved;
    }
    
    private void resetTabelModel(EnhancedTableModel tbm) {
        int rows = tbm.getRowCount();
        for(int i =0;i<rows;i++) {
            tbm.removeRow(0);
        }
    }
    
    /** Getter for property txtSBNumberofInstruments.
     * @return Value of property txtSBNumberofInstruments.
     *
     */
    public java.lang.String getTxtSBNumberofInstruments() {
        return txtSBNumberofInstruments;
    }
    
    /** Setter for property txtSBNumberofInstruments.
     * @param txtSBNumberofInstruments New value of property txtSBNumberofInstruments.
     *
     */
    public void setTxtSBNumberofInstruments(java.lang.String txtSBNumberofInstruments) {
        this.txtSBNumberofInstruments = txtSBNumberofInstruments;
        setChanged();
    }
    
    /** Getter for property txtSBAmount.
     * @return Value of property txtSBAmount.
     *
     */
    public java.lang.String getTxtSBAmount() {
        return txtSBAmount;
    }
    
    /** Setter for property txtSBAmount.
     * @param txtSBAmount New value of property txtSBAmount.
     *
     */
    public void setTxtSBAmount(java.lang.String txtSBAmount) {
        this.txtSBAmount = txtSBAmount;
        setChanged();
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
    
}
