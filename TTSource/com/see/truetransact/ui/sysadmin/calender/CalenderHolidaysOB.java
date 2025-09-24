/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidaysOB.java
 *
 * Created on January 23, 2004, 3:06 PM
 */


package com.see.truetransact.ui.sysadmin.calender;

import com.see.truetransact.transferobject.sysadmin.calender.CalenderHolidaysTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.sysadmin.calender.WeeklyOffTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.Date;



/**
 *
 * @author  Administrator
 */
public class CalenderHolidaysOB extends CObservable {
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private ComboBoxModel cbmMonth;
    private ComboBoxModel cbmDate;
    private ComboBoxModel cbmWeeklyOff1;
    private ComboBoxModel cbmWeeklyOff2;
    private ComboBoxModel cbmHalfDay1;
    private ComboBoxModel cbmHalfDay2;
    
    private boolean rdoWeeklyOff_Yes = false;
    private boolean rdoWeeklyOff_No = false;
    private String cboWeeklyOff1 = "";
    private String cboWeeklyOff2 = "";
    private String cboHalfDay1 = "";
    private String cboHalfDay2 = "";
    private String cboMonth = "";
    private String cboDate = "";
    private String txtHolidayName = "";
    private String txtRemarks = "";
    private String txtYear = "";
    private String holidayID = "";
    
    private final String dateSeparator = "/";
    private Integer integerSplit;
    
    private ProxyFactory proxy;
    private int actionType;
    
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList calendarHolidayTabRow;
    private HashMap operationMap;
    private EnhancedTableModel tblHolidayTab ;
    private static CalenderHolidaysOB calenderHolidaysOB;
    private final CalenderHolidaysRB objCalenderHolidaysRB = new CalenderHolidaysRB();
    private final ArrayList holidayTabTitle = new ArrayList();
    private int option = -1;
    private int optionCancel = 0;
    private ArrayList arrayCalenderHolidaysTO;
    private String stringDate;
    private StringBuffer stringBuffDate;
    private String stringtableData;
    
    private List weekOffResultList;
    private List monthResultList;
    private ArrayList existingDate;
    private ArrayList newDate;
    private ArrayList existingData;
    private ArrayList newData;
    
    public String weeklyOff1Combo = new String();
    public String weeklyOff2Combo = new String();
    public String halfDay1Combo = new String();
    public String halfDay2Combo = new String();
    public int selectedCombo = 0;
    Date curDate = null;
    
    
    static {
        try {
            calenderHolidaysOB = new CalenderHolidaysOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of CalenderHolidaysOB
     * @throws Exception
     */
    private CalenderHolidaysOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setHolidayTabTitle();
        setOperationMap();
        tblHolidayTab = new EnhancedTableModel(null, holidayTabTitle);
        fillDropdown();
        cbmDate = new ComboBoxModel();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "CalenderHolidaysJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.calender.CalenderHolidaysHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.calender.CalenderHolidays");
    }
    
    private void setHolidayTabTitle() throws Exception{
        holidayTabTitle.add(objCalenderHolidaysRB.getString("lblHolidayDate"));
        holidayTabTitle.add(objCalenderHolidaysRB.getString("lblHolidayName"));
        holidayTabTitle.add(objCalenderHolidaysRB.getString("lblHolidayRemarks"));
    }
    
    /** A method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("CALENDAR.MONTH");
        lookup_keys.add("WEEK_DAYS");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CALENDAR.MONTH"));
        cbmMonth = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("WEEK_DAYS"));
        cbmWeeklyOff1 = new ComboBoxModel(key,value);
        cbmWeeklyOff2 = new ComboBoxModel(key,value);
        cbmHalfDay1 = new ComboBoxModel(key,value);
        cbmHalfDay2 = new ComboBoxModel(key,value);
        
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    // Setter method for holidayID
    void setHolidayID(String holidayID){
        this.holidayID = holidayID;
        setChanged();
    }
    // Getter method for holidayID
    String getHolidayID(){
        return this.holidayID;
    }
    
    // Setter method for rdoWeeklyOff_Yes
    void setRdoWeeklyOff_Yes(boolean rdoWeeklyOff_Yes){
        this.rdoWeeklyOff_Yes = rdoWeeklyOff_Yes;
        setChanged();
    }
    // Getter method for rdoWeeklyOff_Yes
    boolean getRdoWeeklyOff_Yes(){
        return this.rdoWeeklyOff_Yes;
    }
    
    // Setter method for rdoWeeklyOff_No
    void setRdoWeeklyOff_No(boolean rdoWeeklyOff_No){
        this.rdoWeeklyOff_No = rdoWeeklyOff_No;
        setChanged();
    }
    // Getter method for rdoWeeklyOff_No
    boolean getRdoWeeklyOff_No(){
        return this.rdoWeeklyOff_No;
    }
    
    // Setter method for cboWeeklyOff1
    void setCboWeeklyOff1(String cboWeeklyOff1){
        this.cboWeeklyOff1 = cboWeeklyOff1;
        setChanged();
    }
    // Getter method for cboWeeklyOff1
    String getCboWeeklyOff1(){
        return this.cboWeeklyOff1;
    }
    
    // Setter method for cbmWeeklyOff1
    void setCbmWeeklyOff1(ComboBoxModel cbmWeeklyOff1){
        this.cbmWeeklyOff1 = cbmWeeklyOff1;
        setChanged();
    }
    // Getter method for cbmWeeklyOff1
    ComboBoxModel getCbmWeeklyOff1(){
        return cbmWeeklyOff1;
    }
    
    // Setter method for cboWeeklyOff2
    void setCboWeeklyOff2(String cboWeeklyOff2){
        this.cboWeeklyOff2 = cboWeeklyOff2;
        setChanged();
    }
    // Getter method for cboWeeklyOff2
    String getCboWeeklyOff2(){
        return this.cboWeeklyOff2;
    }
    
    // Setter method for cbmWeeklyOff2
    void setCbmWeeklyOff2(ComboBoxModel cbmWeeklyOff2){
        this.cbmWeeklyOff2 = cbmWeeklyOff2;
        setChanged();
    }
    // Getter method for cbmWeeklyOff1
    ComboBoxModel getCbmWeeklyOff2(){
        return cbmWeeklyOff2;
    }
    
    // Setter method for cboHalfDay1
    void setCboHalfDay1(String cboHalfDay1){
        this.cboHalfDay1 = cboHalfDay1;
        setChanged();
    }
    // Getter method for cboHalfDay1
    String getCboHalfDay1(){
        return this.cboHalfDay1;
    }
    
    // Setter method for cbmHalfDay1
    void setCbmHalfDay1(ComboBoxModel cbmHalfDay1){
        this.cbmHalfDay1 = cbmHalfDay1;
        setChanged();
    }
    // Getter method for cbmHalfDay1
    ComboBoxModel getCbmHalfDay1(){
        return cbmHalfDay1;
    }
    
    // Setter method for cboHalfDay2
    void setCboHalfDay2(String cboHalfDay2){
        this.cboHalfDay2 = cboHalfDay2;
        setChanged();
    }
    // Getter method for cboHalfDay2
    String getCboHalfDay2(){
        return this.cboHalfDay2;
    }
    
    // Setter method for cbmHalfDay2
    void setCbmHalfDay2(ComboBoxModel cbmHalfDay2){
        this.cbmHalfDay2 = cbmHalfDay2;
        setChanged();
    }
    // Getter method for cbmHalfDay2
    ComboBoxModel getCbmHalfDay2(){
        return cbmHalfDay2;
    }
    
    // Setter method for cboMonth
    void setCboMonth(String cboMonth){
        this.cboMonth = cboMonth;
        setChanged();
    }
    // Getter method for cboMonth
    String getCboMonth(){
        return this.cboMonth;
    }
    
    // Setter method for cbmMonth
    void setCbmMonth(ComboBoxModel cbmMonth){
        this.cbmMonth = cbmMonth;
        setChanged();
    }
    // Getter method for cbmMonth
    ComboBoxModel getCbmMonth(){
        return cbmMonth;
    }
    
    // Setter method for cboDate
    void setCboDate(String cboDate){
        this.cboDate = cboDate;
        setChanged();
    }
    // Getter method for cboDate
    String getCboDate(){
        return this.cboDate;
    }
    
    // Setter method for cbmDate
    void setCbmDate(ComboBoxModel cbmDate){
        this.cbmDate = cbmDate;
        setChanged();
    }
    // Getter method for cbmDate
    ComboBoxModel getCbmDate(){
        return cbmDate;
    }
    
    // Setter method for txtHolidayName
    void setTxtHolidayName(String txtHolidayName){
        this.txtHolidayName = txtHolidayName;
        setChanged();
    }
    // Getter method for txtHolidayName
    String getTxtHolidayName(){
        return this.txtHolidayName;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for txtYear
    void setTxtYear(String txtYear){
        this.txtYear = txtYear;
        setChanged();
    }
    // Getter method for txtYear
    String getTxtYear(){
        return this.txtYear;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    
    public void setResult(int statusresult) {
        result = statusresult;
        setChanged();
    }
    public int getResult(){
        return result;
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return actionType;
    }
    
    
    
    void setTblHolidays(EnhancedTableModel tblHolidayTab){
        this.tblHolidayTab = tblHolidayTab;
        setChanged();
    }
    EnhancedTableModel getTblHolidays(){
        return this.tblHolidayTab;
    }
    
    public static CalenderHolidaysOB getInstance() {
        return calenderHolidaysOB;
    }
    
    public void setComboDateSetModel(int row){
        ArrayList rowData = new ArrayList();
        rowData.add("");
        for(int i=1;i<=row;i++){
            rowData.add(String.valueOf(i));
        }
        cbmDate = new ComboBoxModel(rowData);
        setCbmDate(cbmDate);
    }
    
    /** To Insert or Update the Data entered  **/
    public void tableInsertUpdate(ArrayList tableDate){
        newDate = new ArrayList();
        newData = new ArrayList();
        optionCancel = 0;
        setDate();
        boolean newdata = false;
        
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            /** If it is UPDATE */
            newdata = false;
            setActionType(ClientConstants.ACTIONTYPE_EDIT);
            /** New Date */
            newDate.add(getCboDate());
            newDate.add(getTxtYear());
            newDate.add(getCboMonth());
            /** New Data (Holday Data) */
            newData.add(getCboDate());
            newData.add(getTxtYear());
            newData.add(getCboMonth());
            newData.add(getTxtRemarks());
            newData.add(getTxtHolidayName());
            /** New Data (Weekly Off Data) */
            if(getRdoWeeklyOff_Yes() == true){
                newData.add("Y");
            }else{
                newData.add("N");
            }
            newData.add(getCboWeeklyOff1());
            newData.add(getCboWeeklyOff2());
            newData.add(getCboHalfDay1());
            newData.add(getCboHalfDay2());
            
            if(newDate.equals(existingDate)){
                if(newData.equals(existingData)){
                    setResult(ClientConstants.ACTIONTYPE_EDIT);
                }else{
                    doAction();
                }
                resetForm();
                removeHolidayRow();
                setResultStatus();
            }else{;
            newdata = checkDuplicateData(tableDate);
            if(newdata){
                doAction();
                resetForm();
                removeHolidayRow();
                setResultStatus();
            }
            }
        }else if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            /** If it is INSERT */
            newdata = checkDuplicateData(tableDate);
            if(newdata == true){
                /** Insert the Data into the DataBase **/
                setActionType(ClientConstants.ACTIONTYPE_NEW);
                doAction();
                resetForm();
                removeHolidayRow();
                setResultStatus();
            }
        }
    }
    
    public int doCancel(){
        return optionCancel;
    }
    
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final CalenderHolidaysRB objcalenderHolidaysRB = new CalenderHolidaysRB();
                    throw new TTException(objcalenderHolidaysRB.getString("TOCommandError"));
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
    
    /** Method to Check the duplication */
    private boolean checkDuplicateData(ArrayList tableDate ){
        boolean newdata = false;
        int dataSize = 0;
        dataSize = tableDate.size();
        for(int i=0;i<dataSize;i++){
            stringtableData = ((String)((ArrayList)tableDate.get(i)).get(0));
            if (stringtableData.equals(stringBuffDate.toString())){
                /** If the Date entered in the field matches the table data **/
                newdata = false;
                String[] options = {objCalenderHolidaysRB.getString("cDialogYes"),objCalenderHolidaysRB.getString("cDialogNo"),objCalenderHolidaysRB.getString("cDialogCancel")};
                option = COptionPane.showOptionDialog(null, objCalenderHolidaysRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                checkCondition(option);
                break;
            }else{
                /** If the Date entered in the field doesn't matches the table data **/
                newdata = true;
            }
        }
        return newdata;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final CalenderHolidaysTO objcalenderHolidaysTO = setCalenderHolidaysData();
        boolean sameData = false;
        objcalenderHolidaysTO.setCommand(getCommand());
        
        HashMap data = new HashMap();
        
        if (objcalenderHolidaysTO.getHolidayDt() != null && objcalenderHolidaysTO.getHolidayName() != null) {
            data.put("CalenderHolidaysTO",objcalenderHolidaysTO);
        }
        
        System.out.println("objcalenderHolidaysTO" + objcalenderHolidaysTO);
        /** Check (sameData) if the the Data in the Weekly Off is same as the combo datas (Weekly Off Combo) */
        sameData = checkWeeklyOffUpdate();
        if(!sameData){
            /** If data in Weekly Off is not same it has to INSERT / UPDATE */
            final WeeklyOffTO objWeeklyOffTO = setWeeklyOffData();
            objWeeklyOffTO.setCommand(getCommand());
            if(objWeeklyOffTO != null){
                data.put("WeeklyOffTO",objWeeklyOffTO);
            }
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_NEW;
        data = null;
    }
    
    /** Helps in putting the Data from UI to the Data Base**/
    public CalenderHolidaysTO setCalenderHolidaysData(){
        final CalenderHolidaysTO objcalenderHolidaysTO = new CalenderHolidaysTO();
        try{
            setDate();
            System.out.println("stringBuffDate: " + stringBuffDate.toString());
            objcalenderHolidaysTO.setStatusBy(TrueTransactMain.USER_ID);
            objcalenderHolidaysTO.setStatusDt(curDate);
            objcalenderHolidaysTO.setHolidayId(holidayID);
            
            Date Dt = DateUtil.getDateMMDDYYYY(stringBuffDate.toString());
            if(Dt != null){
            Date dtDate = (Date) curDate.clone();
            dtDate.setDate(Dt.getDate());
            dtDate.setMonth(Dt.getMonth());
            dtDate.setYear(Dt.getYear());
//            objcalenderHolidaysTO.setHolidayDt(DateUtil.getDateMMDDYYYY(stringBuffDate.toString()));
            objcalenderHolidaysTO.setHolidayDt(dtDate);
            }else{
                objcalenderHolidaysTO.setHolidayDt(DateUtil.getDateMMDDYYYY(stringBuffDate.toString()));
            }
            
            objcalenderHolidaysTO.setHolidayName(txtHolidayName);
            objcalenderHolidaysTO.setRemarks(txtRemarks);
            objcalenderHolidaysTO.setBranchId(TrueTransactMain.BRANCH_ID);
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objcalenderHolidaysTO.setCreatedBy(TrueTransactMain.USER_ID);
                objcalenderHolidaysTO.setCreatedDt(curDate);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objcalenderHolidaysTO;
    }
    
    
    /** Helps in putting the Data from UI to the Data Base**/
    public WeeklyOffTO setWeeklyOffData(){
        final WeeklyOffTO objWeeklyOffTO = new WeeklyOffTO();
        try{
            if(weekOffResultList.isEmpty()){
                /** If the Weekly Off List is Empty thn, INSERT  */
                objWeeklyOffTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            }else{
                /** If the Weekly Off List is Not Empty thn, UPDATE  */
                objWeeklyOffTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            }
            if(getRdoWeeklyOff_Yes()){
                objWeeklyOffTO.setWeeklyOff("Y");
            }else{
                objWeeklyOffTO.setWeeklyOff("N");
            }
            objWeeklyOffTO.setWeeklyOff1(CommonUtil.convertObjToStr(cbmWeeklyOff1.getKeyForSelected()));
            objWeeklyOffTO.setWeeklyOff2(CommonUtil.convertObjToStr(cbmWeeklyOff2.getKeyForSelected()));
            objWeeklyOffTO.setHalfDay1(CommonUtil.convertObjToStr(cbmHalfDay1.getKeyForSelected()));
            objWeeklyOffTO.setHalfDay2(CommonUtil.convertObjToStr(cbmHalfDay2.getKeyForSelected()));
            objWeeklyOffTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objWeeklyOffTO.setStatusBy(TrueTransactMain.USER_ID);
            objWeeklyOffTO.setStatusDt(curDate);
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objWeeklyOffTO.setCreatedBy(TrueTransactMain.USER_ID);
                objWeeklyOffTO.setCreatedDt(curDate);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objWeeklyOffTO;
    }
    
    /** This method helps in get the CommonConstants.MAP_WHERE codition and executing the query **/
    public void populateData(HashMap whereMap) {
        try {
            if(whereMap.containsKey("HOLIDAY_ID")){
                whereMap.put(CommonConstants.MAP_WHERE, whereMap.get("HOLIDAY_ID"));
            } else {
                //--- In AutorizationMode, key is HOLIDAY ID
                whereMap.put(CommonConstants.MAP_WHERE, whereMap.get("HOLIDAY ID"));
            }
            
            whereMap.put("BRANCH ID", TrueTransactMain.BRANCH_ID);
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** This method helps in populating the data from the View All table to the respective Fileds **/
    private void populateOB(HashMap mapData) throws Exception{
        existingDate = new ArrayList();
        existingData = new ArrayList();
        CalenderHolidaysTO objcalenderHolidaysTO;
        objcalenderHolidaysTO = (CalenderHolidaysTO) ((List) mapData.get("CalenderHolidaysTO")).get(0);
        populateWeeklyOff();
        stringDate = new String();
        stringBuffDate = new StringBuffer();
        stringDate = CommonUtil.convertObjToStr(objcalenderHolidaysTO.getHolidayDt());
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(objcalenderHolidaysTO.getHolidayDt());
        
        setTxtYear(String.valueOf(c.get(GregorianCalendar.YEAR)));
        setCboDate(String.valueOf(c.get(GregorianCalendar.DATE)));
        setTxtRemarks(CommonUtil.convertObjToStr(objcalenderHolidaysTO.getRemarks()));
        setTxtHolidayName(CommonUtil.convertObjToStr(objcalenderHolidaysTO.getHolidayName()));
        setHolidayID(CommonUtil.convertObjToStr(objcalenderHolidaysTO.getHolidayId()));
        setCboMonth(CommonUtil.convertObjToStr(getCbmMonth().getDataForKey(String.valueOf(c.get(GregorianCalendar.MONTH)+1))));
        notifyObservers();
        
        /** Existing Date */
        existingDate.add(getCboDate());
        existingDate.add(getTxtYear());
        existingDate.add(getCboMonth());
        /** Existing Data (Holday Data) */
        existingData.add(getCboDate());
        existingData.add(getTxtYear());
        existingData.add(getCboMonth());
        existingData.add(getTxtRemarks());
        existingData.add(getTxtHolidayName());
        /** Existing Data (Weekly Off Data) */
        if(getRdoWeeklyOff_Yes() == true){
            existingData.add("Y");
        }else{
            existingData.add("N");
        }
        existingData.add(getCboWeeklyOff1());
        existingData.add(getCboWeeklyOff2());
        existingData.add(getCboHalfDay1());
        existingData.add(getCboHalfDay2());
    }
    
    /** Gets the values from the neccessary fields and sets it to the TO object **/
    //    public void setDate(){
    //        stringDate =  new String();
    //        stringBuffDate = new StringBuffer();
    //        stringDate = getCbmMonth().getKeyForSelected().toString();
    //        stringBuffDate.append(stringDate);
    //        stringBuffDate.append(dateSeparator);
    //        stringBuffDate.append(getCboDate().toString());
    //        stringBuffDate.append(dateSeparator);
    //        stringBuffDate.append(getTxtYear().toString());
    //    }
    
    /** Gets the values from the neccessary fields and sets it to the TO object **/
    public void setDate(){
        stringDate =  new String();
        stringBuffDate = new StringBuffer();
        stringDate = getCbmMonth().getKeyForSelected().toString();
        stringBuffDate.append(getCboDate().toString());
        stringBuffDate.append(dateSeparator);
        stringBuffDate.append(stringDate);
        stringBuffDate.append(dateSeparator);
        stringBuffDate.append(getTxtYear().toString());
    }
    
    /** Method that decides YES, NO and CANCEL options */
    private void checkCondition(int option){
        switch(option){
            case 0 :
                /** Option YES **/
                /** If Action Type is NEW and the data Exists in the table, it has to UPDATE the data */
                getHolidayListID();
                setActionType(ClientConstants.ACTIONTYPE_EDIT);
                doAction();
                resetForm();
                removeHolidayRow();
                setResultStatus();
                break;
            case 1 :
                /** Option NO **/
                resetForm();
                removeHolidayRow();
                resetStatus();
                break;
            case 2 :
                /** Option CANCEL **/
                optionCancel = 1;
                break;
        }
    }
    
    /** Method to split the date (Table Date) and append it to compareDate (Integer Format)
     *  like :- 01/03/2004 - to -> 1/3/2004
     */
    private String dateSplit(String tableData){
        StringBuffer compareDate = new StringBuffer();
        int dateValue;
        String stringSplit[] = tableData.split(dateSeparator);
        
        System.out.println("stringSplit: " + stringSplit[0] + " " + stringSplit[1] + " " + stringSplit[2]);
        
        integerSplit = new Integer(Integer.parseInt(stringSplit[0]));
        dateValue = integerSplit.intValue();
        compareDate.append(dateValue);
        compareDate.append(dateSeparator);
        integerSplit = new Integer(Integer.parseInt(stringSplit[1]));
        dateValue = integerSplit.intValue();
        compareDate.append(dateValue);
        compareDate.append(dateSeparator);
        integerSplit = new Integer(Integer.parseInt(stringSplit[2]));
        dateValue= integerSplit.intValue();
        compareDate.append(dateValue);
        
        System.out.println("compareDate: " + compareDate.toString());
        return compareDate.toString();
    }
    
    /** Method to get Holiday List ID (holidayID) */
    private void getHolidayListID(){
        String tableData;
        String compareDate;
        for(int i=0;i<monthResultList.size();i++){
            tableData = new String();
            compareDate = new String();
            tableData = CommonUtil.convertObjToStr(((HashMap)monthResultList.get(i)).get("Date"));
            System.out.println("tableData: " + tableData);
            compareDate = dateSplit(tableData);
            if(stringBuffDate.toString().equals(compareDate)){
                /** set the Holiday List ID (holidayID) according to the data that has to be UPDATE(ed)  */
                holidayID = ((HashMap)monthResultList.get(i)).get("Holiday ID").toString();
                break;
            }
        }
    }
    
    /** Method to populate the datas in Holiday List Table */
    public /*static*/ boolean  setHolidayTableModel() {
        boolean dataExist = true;
        final HashMap whereMap = new HashMap();
        final String viewMap = "ViewMonthAndDate";
        whereMap.put("MONTH", CommonUtil.convertObjToInt(getCbmMonth().getKeyForSelected().toString()));
        whereMap.put("YEAR", getTxtYear());
        whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList holidayNameTabRow = new ArrayList();
        monthResultList = ClientUtil.executeQuery(viewMap, whereMap);
        int listRow = monthResultList.size();
        tblHolidayTab = new EnhancedTableModel(null, holidayTabTitle);
        for(int i=0;i<listRow;i++){
            final HashMap resultMap = (HashMap)monthResultList.get(i);
            holidayNameTabRow = new ArrayList();
            holidayNameTabRow.add(CommonUtil.convertObjToStr(resultMap.get("DATE")));
            holidayNameTabRow.add(resultMap.get("NAME"));
            holidayNameTabRow.add(resultMap.get("REMARKS"));
            tblHolidayTab.insertRow(i,holidayNameTabRow);
        }
        return dataExist;
    }
    
    /** Set Table Model with the Header (Title))*/
    public void setTableModel(){
        tblHolidayTab = new EnhancedTableModel(null, holidayTabTitle);
        notifyObservers();
    }
    
    public boolean checkWeeklyOff(){
        boolean dataExists = true;
        HashMap inputMap = new HashMap();
        inputMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        weekOffResultList = ClientUtil.executeQuery("ViewWeekOff", inputMap);
        if(weekOffResultList.isEmpty()){
            dataExists = false;
        }
        return dataExists;
    }
    
    /** Populates the Weekly Off fields */
    public void populateWeeklyOff(){
        if(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF").equals("Y")){
            setRdoWeeklyOff_Yes(true);
        }else{
            setRdoWeeklyOff_No(true);
        }
        setCboWeeklyOff1(CommonUtil.convertObjToStr(getCbmWeeklyOff1().getDataForKey(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF1"))));
        setCboWeeklyOff2(CommonUtil.convertObjToStr(getCbmWeeklyOff2().getDataForKey(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF2"))));
        setCboHalfDay1(CommonUtil.convertObjToStr(getCbmHalfDay1().getDataForKey(((HashMap)weekOffResultList.get(0)).get("HALF_DAY1"))));
        setCboHalfDay2(CommonUtil.convertObjToStr(getCbmHalfDay2().getDataForKey(((HashMap)weekOffResultList.get(0)).get("HALF_DAY2"))));
    }
    
    /** Method that Checks Weekly Off is to be INSERT(ed) / UPDATE(ed)  */
    private boolean checkWeeklyOffUpdate(){
        ArrayList tableData = new ArrayList();
        ArrayList comboData = new ArrayList();
        boolean sameData = false;
        if(weekOffResultList.size() > 0){
            tableData.add(CommonUtil.convertObjToStr(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF")));
            tableData.add(CommonUtil.convertObjToStr(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF1")));
            tableData.add(CommonUtil.convertObjToStr(((HashMap)weekOffResultList.get(0)).get("WEEKLY_OFF2")));
            tableData.add(CommonUtil.convertObjToStr(((HashMap)weekOffResultList.get(0)).get("HALF_DAY1")));
            tableData.add(CommonUtil.convertObjToStr(((HashMap)weekOffResultList.get(0)).get("HALF_DAY2")));
        }
        
        if(getRdoWeeklyOff_Yes() == true){
            comboData.add("Y");
        }else{
            comboData.add("N");
        }
        comboData.add(CommonUtil.convertObjToStr(cbmWeeklyOff1.getKeyForSelected()));
        comboData.add(CommonUtil.convertObjToStr(cbmWeeklyOff2.getKeyForSelected()));
        comboData.add(CommonUtil.convertObjToStr(cbmHalfDay1.getKeyForSelected()));
        comboData.add(CommonUtil.convertObjToStr(cbmHalfDay2.getKeyForSelected()));
        if(tableData.equals(comboData)){
            sameData = true;
        }
        return sameData;
    }
    
    public boolean checkComboUnique(){
        boolean uniqueCombo = true;
        switch(selectedCombo){
            case 1 :  if((weeklyOff1Combo.equalsIgnoreCase(weeklyOff2Combo)) || (weeklyOff1Combo.equalsIgnoreCase(halfDay1Combo)) || (weeklyOff1Combo.equalsIgnoreCase(halfDay2Combo)) ){
                String[] options = {objCalenderHolidaysRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objCalenderHolidaysRB.getString("comboUniqueWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                uniqueCombo = false;
            }
            break;
            
            case 2 :  if((weeklyOff2Combo.equalsIgnoreCase(weeklyOff1Combo)) || (weeklyOff2Combo.equalsIgnoreCase(halfDay1Combo)) || (weeklyOff2Combo.equalsIgnoreCase(halfDay2Combo)) ){
                String[] options = {objCalenderHolidaysRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objCalenderHolidaysRB.getString("comboUniqueWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                uniqueCombo = false;
            }
            break;
            
            case 3 :     if((halfDay1Combo.equalsIgnoreCase(weeklyOff1Combo)) || (halfDay1Combo.equalsIgnoreCase(weeklyOff2Combo)) || (halfDay1Combo.equalsIgnoreCase(halfDay2Combo)) ){
                String[] options = {objCalenderHolidaysRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objCalenderHolidaysRB.getString("comboUniqueWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                uniqueCombo = false;
            }
            break;
            
            case 4 :  if((halfDay2Combo.equalsIgnoreCase(weeklyOff1Combo)) || (halfDay2Combo.equalsIgnoreCase(weeklyOff2Combo)) || (halfDay2Combo.equalsIgnoreCase(halfDay1Combo)) ){
                String[] options = {objCalenderHolidaysRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objCalenderHolidaysRB.getString("comboUniqueWarning"), CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                uniqueCombo = false;
            }
            break;
        }
        
        return uniqueCombo;
    }
    
    /** Method to remove all the rows from Holiday List Table */
    public void removeHolidayRow(){
        int row = tblHolidayTab.getRowCount();
        for(int i=0;i<row;i++) {
            tblHolidayTab.removeRow(0);
        }
        tblHolidayTab = new EnhancedTableModel(null, holidayTabTitle);
        setTblHolidays(tblHolidayTab);
    }
    
    
    
    /** Method to reset Holiday List Fields */
    public void resetFields(){
        setCboMonth("");
        setTxtHolidayName("");
        setTxtRemarks("");
        setCboDate("");
        setChanged();
        notifyObservers();
    }
    
    /** Method to reset Form */
    public void resetForm(){
        // To reset Holiday List Fields
        setCboMonth("");
        setTxtHolidayName("");
        setTxtRemarks("");
        setCboDate("");
        setChanged();
        // To reset Weekly Off combo Fields
        setCboWeeklyOff1("");
        setCboWeeklyOff2("");
        setCboHalfDay1("");
        setCboHalfDay2("");
        //Resets all the Radio Buttons to false.
        setRdoWeeklyOff_Yes(false);
        setRdoWeeklyOff_No(false);
        notifyObservers();
    }
}