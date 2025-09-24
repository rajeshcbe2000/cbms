/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogOB.java
 *
 * Created on April 19, 2004, 11:39 AM
 */
package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.sysadmin.view.ViewLogRB;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.DateUtil;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author  Lohith R.
 */
public class ViewLogOB extends Observable {
    private static ViewLogOB objViewLogOB; // singleton object
    private static ViewLogRB objViewLogRB = new ViewLogRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger log = Logger.getLogger(ViewLogOB.class);
    private ComboBoxModel cbmFindbyHistory;
    private ComboBoxModel cbmUserID;
    private ComboBoxModel cbmModule;
    private ComboBoxModel cbmBranchID;
    private ComboBoxModel cbmScreen;
    private ComboBoxModel cbmActivity;
    private ComboBoxModel cbmIPAddress;
    
    private EnhancedTableModel tblViewLog;
    private final ArrayList tblViewLogTitle = new ArrayList();
    
    private String cboFindbyHistory = "";
    private String cboUserID = "";
    private String dateFromDate = "";
    private String cboModule = "";
    private String cboIPAddress = "";
    private String dateToDate = "";
    private String txtLatestEntries = "";
    private boolean rdoNew = false;
    private boolean rdoEdit = false;
    private boolean rdoDelete = false;
    private boolean rdoAll = false;
    private String cboBranchID = "";
    private String cboScreen = "";
    private String cboActivity = "";
    private int option = -1;
    
    private boolean optionAfterDate = false;
    
    private final String LAST_YEAR = "since_last_year";
    private final String LAST_FINANCIAL_YEAR = "since_last_financial_year";
    private final String LAST_MONTH = "since_last_month";
    private final String LAST_WEEK = "since_last_week";
    private final String TIME = "23:59:59";
    private final String STRING_SPACE = " ";
    
    private final int SET_DATE = 1;
    private final int SET_APRIL_MONTH = 4;
    
    private int actionType;
    private int result;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    
    static {
        try {
            objViewLogOB = new ViewLogOB();
        } catch(Exception e) {
            log.info("Exception in viewLogTable(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    
    /** Creates a new instance of ViewLogOB */
    private ViewLogOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        fillDropdown();
        setViewLogTabTitle();
        tblViewLog = new EnhancedTableModel(null, tblViewLogTitle);
        
    }
    
    /** Creates a new instance of ViewLogOB */
    public static ViewLogOB getInstance() {
        return objViewLogOB;
    }
    
    public void setViewLogTabTitle(){
        tblViewLogTitle.add(objViewLogRB.getString("lblDate"));
        tblViewLogTitle.add(objViewLogRB.getString("lblBranchID"));
        tblViewLogTitle.add(objViewLogRB.getString("lblUserid"));
        tblViewLogTitle.add(objViewLogRB.getString("lblIPAddress"));
        tblViewLogTitle.add(objViewLogRB.getString("lblScreen"));
        tblViewLogTitle.add(objViewLogRB.getString("lblModule"));
        tblViewLogTitle.add(objViewLogRB.getString("lblPrimaryKey"));
        tblViewLogTitle.add(objViewLogRB.getString("lblActivity"));
        tblViewLogTitle.add(objViewLogRB.getString("lblData"));
        tblViewLogTitle.add(objViewLogRB.getString("lblRemarks"));
    }
    
    /** Method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("VIEWLOG.HISTORY");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("VIEWLOG.HISTORY"));
        cbmFindbyHistory = new ComboBoxModel(key,value);
        
        /** The data to be show in Combo Box other than LOOKUP_MASTER table  */
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.UserID");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmUserID = new ComboBoxModel(key,value);
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.BranchID");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBranchID = new ComboBoxModel(key,value);
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Screen");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmScreen = new ComboBoxModel(key,value);
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.IPAddress");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmIPAddress = new ComboBoxModel(key,value);
        
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Module");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmModule = new ComboBoxModel(key,value);
        
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"ViewLog.Activity");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmActivity = new ComboBoxModel(key,value);
        
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
    
    // Setter method for cboFindbyHistory
    void setCboFindbyHistory(String cboFindbyHistory){
        this.cboFindbyHistory = cboFindbyHistory;
        setChanged();
    }
    // Getter method for cboFindbyHistory
    String getCboFindbyHistory(){
        return this.cboFindbyHistory;
    }
    
    // Setter method for setcbmFindbyHistory
    void setCbmFindbyHistory(ComboBoxModel cbmFindbyHistory){
        this.cbmFindbyHistory = cbmFindbyHistory;
        setChanged();
    }
    // Getter method for getcbmFindbyHistory
    ComboBoxModel getCbmFindbyHistory(){
        return cbmFindbyHistory;
    }
    
    
    // Setter method for cboUserID
    void setCboUserID(String cboUserID){
        this.cboUserID = cboUserID;
        setChanged();
    }
    // Getter method for cboUserID
    String getCboUserID(){
        return this.cboUserID;
    }
    
    // Setter method for setCbmUserID
    void setCbmUserID(ComboBoxModel cbmUserID){
        this.cbmUserID = cbmUserID;
        setChanged();
    }
    // Getter method for getCbmUserID
    ComboBoxModel getCbmUserID(){
        return cbmUserID;
    }
    
    // Setter method for dateFromDate
    void setDateFromDate(String dateFromDt){
        this.dateFromDate = dateFromDt;
        setChanged();
    }
    // Getter method for dateFromDate
    String getDateFromDate(){
        return dateFromDate;
    }
    
    // Setter method for cboModule
    void setCboModule(String cboModule){
        this.cboModule = cboModule;
        setChanged();
    }
    // Getter method for cboModule
    String getCboModule(){
        return this.cboModule;
    }
    
    // Setter method for setCbmModule
    void setCbmModule(ComboBoxModel cbmModule){
        this.cbmModule = cbmModule;
        setChanged();
    }
    // Getter method for getCbmModule
    ComboBoxModel getCbmModule(){
        return cbmModule;
    }
    
    // Setter method for cboIPAddress
    void setCboIPAddress(String cboIPAddress){
        this.cboIPAddress = cboIPAddress;
        setChanged();
    }
    // Getter method for cboIPAddress
    String getCboIPAddress(){
        return this.cboIPAddress;
    }
    
    // Setter method for setCbmIPAddress
    void setCbmIPAddress(ComboBoxModel cbmIPAddress){
        this.cbmIPAddress = cbmIPAddress;
        setChanged();
    }
    // Getter method for getCbmIPAddress
    ComboBoxModel getCbmIPAddress(){
        return cbmIPAddress;
    }
    
    // Setter method for dateToDate
    void setDateToDate(String dateToDt){
        this.dateToDate = dateToDt;
        setChanged();
    }
    // Getter method for dateToDate
    String getDateToDate(){
        return dateToDate;
    }
    
    // Setter method for txtLatestEntries
    void setTxtLatestEntries(String txtLatestEntries){
        this.txtLatestEntries = txtLatestEntries;
        setChanged();
    }
    // Getter method for txtLatestEntries
    String getTxtLatestEntries(){
        return this.txtLatestEntries;
    }
    
    // Setter method for rdoNew
    void setRdoNew(boolean rdoNew){
        this.rdoNew = rdoNew;
        setChanged();
    }
    // Getter method for rdoNew
    boolean getRdoNew(){
        return this.rdoNew;
    }
    
    // Setter method for rdoEdit
    void setRdoEdit(boolean rdoEdit){
        this.rdoEdit = rdoEdit;
        setChanged();
    }
    // Getter method for rdoEdit
    boolean getRdoEdit(){
        return this.rdoEdit;
    }
    
    // Setter method for rdoDelete
    void setRdoDelete(boolean rdoDelete){
        this.rdoDelete = rdoDelete;
        setChanged();
    }
    // Getter method for rdoDelete
    boolean getRdoDelete(){
        return this.rdoDelete;
    }
    
    // Setter method for rdoAll
    void setRdoAll(boolean rdoAll){
        this.rdoAll = rdoAll;
        setChanged();
    }
    // Getter method for rdoAll
    boolean getRdoAll(){
        return this.rdoAll;
    }
    
    // Setter method for cboBranchID
    void setCboBranchID(String cboBranchID){
        this.cboBranchID = cboBranchID;
        setChanged();
    }
    // Getter method for cboBranchID
    String getCboBranchID(){
        return this.cboBranchID;
    }
    
    // Setter method for setCbmBranchID
    void setCbmBranchID(ComboBoxModel cbmBranchID){
        this.cbmBranchID = cbmBranchID;
        setChanged();
    }
    // Getter method for getCbmBranchID
    ComboBoxModel getCbmBranchID(){
        return cbmBranchID;
    }
    
    // Setter method for cboScreen
    void setCboScreen(String cboScreen){
        this.cboScreen = cboScreen;
        setChanged();
    }
    // Getter method for cboScreen
    String getCboScreen(){
        return this.cboScreen;
    }
    
    // Setter method for setCbmScreen
    void setCbmScreen(ComboBoxModel cbmScreen){
        this.cbmScreen = cbmScreen;
        setChanged();
    }
    // Getter method for getCbmScreen
    ComboBoxModel getCbmScreen(){
        return cbmScreen;
    }
    
    // Setter method for cboActivity
    void setCboActivity(String cboActivity){
        this.cboActivity = cboActivity;
        setChanged();
    }
    // Getter method for cboActivity
    String getCboActivity(){
        return this.cboActivity;
    }
    
    // Setter method for setCbmActivity
    void setCbmActivity(ComboBoxModel cbmActivity){
        this.cbmActivity = cbmActivity;
        setChanged();
    }
    // Getter method for getCbmActivity
    ComboBoxModel getCbmActivity(){
        return cbmActivity;
    }
    
    
    void setTblViewLog(EnhancedTableModel tblViewLog){
        this.tblViewLog = tblViewLog;
        setChanged();
    }
    EnhancedTableModel getTblViewLog(){
        return this.tblViewLog;
    }
    
    
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
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
    
    
    /** Removes the rows from the View Log Table*/
    public void removeViewLogRow(){
        int row = tblViewLog.getRowCount();
        for(int i=0;i<row;i++) {
            tblViewLog.removeRow(0);
        }
    }
    
    public void viewLogTable(){
        try{
            HashMap whereMap = new HashMap();
            
            whereMap.put("USER_ID",getCbmUserID().getKeyForSelected());
            whereMap.put("BRANCH_ID",getCbmBranchID().getKeyForSelected());
            whereMap.put("IP_ADDR",getCbmIPAddress().getKeyForSelected());
            whereMap.put("SCREEN",getCbmScreen().getKeyForSelected());
            whereMap.put("MODULE",getCbmModule().getKeyForSelected());
            whereMap.put("STATUS",getCbmActivity().getKeyForSelected());
            whereMap.put("FROM_DT",getDateFromDate());
            whereMap.put("TO_DT",getDateToDate());
            whereMap.put("ROW_COUNT",getTxtLatestEntries());
            
            if (!getDateToDate().equalsIgnoreCase("")){
                appendToDate(whereMap, getDateToDate());
            }
            checkFindByHistoryNotNull(whereMap);
            //            optionAfterDate = checkDateNotNull(whereMap);
            checkRadioButtonSelected(whereMap);
            
            //            if(!optionAfterDate){
            doQuery(whereMap, "ViewLog.ViewConditionalLog");
            //            }
        }catch(Exception e){
            log.info("Exception in viewLogTable(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    /** Executes the query using Where Codition and Map Name
     * @param whereMap Where Codition is passed as Hash Map
     * @param mapNameString Map Name is passed as string
     */
    private void doQuery(HashMap whereMap, String mapNameString){
        try{
            HashMap resultMap = new HashMap();
            ArrayList vewiLogData ;
            final List resultList = ClientUtil.executeQuery(mapNameString, whereMap);
            int rowData = resultList.size();
            
            if(resultList.isEmpty()){
                // If result List is empty, display warning message
                String[] options = {objViewLogRB.getString("cDialogOk")};
                option = COptionPane.showOptionDialog(null, objViewLogRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            }else{
                // If result List is not empty, add the datas into the table
                for(int i=0;i<rowData;i++){
                    vewiLogData = new ArrayList();
                    vewiLogData.add(((HashMap)resultList.get(i)).get("TIME_STAMP"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("BRANCH_ID"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("USER_ID"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("IP_ADDR"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("SCREEN"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("MODULE"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("PRIMARY_KEY"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("STATUS"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("DATA"));
                    vewiLogData.add(((HashMap)resultList.get(i)).get("REMARKS"));
                    tblViewLog.insertRow(0,vewiLogData);
                }
            }
            notifyObservers();
        }catch(Exception e){
            log.info("Exception in doQuery(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    /** A method to set STATUS to whereMap.
     * @param whereMap Hash Map that contains the where condition is passed as argument.
     */
    public void checkRadioButtonSelected(HashMap whereMap){
        try{
            if (getRdoNew() == true){
                whereMap.put("STATUS",CommonConstants.TOSTATUS_INSERT);
                setCboActivity(" ");
            } else if (getRdoEdit() == true){
                whereMap.put("STATUS",CommonConstants.TOSTATUS_UPDATE);
            } else if (getRdoDelete() == true){
                whereMap.put("STATUS",CommonConstants.TOSTATUS_DELETE);
            }else if (getRdoAll() == true){
                whereMap.put("STATUS","");
            }
        }catch(Exception e){
            log.info("Exception in checkRadioButtonSelected(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    private void checkFindByHistoryNotNull(HashMap whereMap){
        try{
            if(!getCbmFindbyHistory().getSelectedItem().equals("")){
                
                StringBuffer stringBufferFromDate = new StringBuffer();
                Date currentDate = ClientUtil.getCurrentDate();
                
                String stringFindHistory = getCbmFindbyHistory().getKeyForSelected().toString();
                appendToDate(whereMap, DateUtil.getStringDate(currentDate));
                if(stringFindHistory.equalsIgnoreCase(LAST_YEAR)){
                    currentDate.setYear(currentDate.getYear() - 1);
                    currentDate.setDate(SET_DATE);
                    whereMap.put("FROM_DT",DateUtil.getStringDate(currentDate));
                }else if(stringFindHistory.equalsIgnoreCase(LAST_FINANCIAL_YEAR)){
                    currentDate.setMonth(SET_APRIL_MONTH);
                    currentDate.setDate(SET_DATE);
                    currentDate.setYear(currentDate.getYear() - 1);
                    whereMap.put("FROM_DT",DateUtil.getStringDate(currentDate));
                }else if(stringFindHistory.equalsIgnoreCase(LAST_MONTH)){
                    currentDate.setMonth(currentDate.getMonth() - 1);
                    currentDate.setDate(SET_DATE);
                    whereMap.put("FROM_DT",DateUtil.getStringDate(currentDate));
                }else if(stringFindHistory.equalsIgnoreCase(LAST_WEEK)){
                    currentDate.setDate(currentDate.getDate() - (currentDate.getDay() + 6));
                    whereMap.put("FROM_DT",DateUtil.getStringDate(currentDate));
                }
            }
        }catch(Exception e){
            log.info("Exception in checkFindByHistoryNotNull(): "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * A method to set FROM_DT and TO_DT to whereMap.
     * @param whereMap Hash Map that contains the where condition is passed as argument.
     * @return true if To Date is greater than From Date
     * @deprecated from v1.8
     */
    public boolean checkDateNotNull(HashMap whereMap){
        boolean opt = false;
        try{
            if(!getDateFromDate().equalsIgnoreCase("") && !getDateToDate().equals("")){
                Date fromDate = DateUtil.getDateMMDDYYYY(getDateFromDate());
                Date toDate = DateUtil.getDateMMDDYYYY(getDateToDate());
                if(fromDate.after(toDate)){
                    opt = true;
                    String[] options = {objViewLogRB.getString("cDialogOk")};
                    option = COptionPane.showOptionDialog(null, objViewLogRB.getString("dateWarning"), CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                }
            }
        }catch(Exception e){
            log.info("Exception in checkDateNotNull(): "+e);
            parseException.logException(e,true);
        }
        return opt;
    }
    
    // To get the Data from the Table into the View Log Dialog
    public HashMap populateViewLogDialog(int row){
        HashMap whereMap = new HashMap();
        HashMap resultMap = new HashMap();
        try{
            getWhereMap(whereMap,row);
            final List resultList = ClientUtil.executeQuery("ViewLog.ViewLogSelectedRow", whereMap);
            putResultMap(resultList, resultMap);
        }catch(Exception e){
            log.info("Exception in populateViewLogDialog(): "+e);
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    //    public HashMap hashMapForMultipleIPAddr(){
    //        HashMap whereMap = new HashMap();
    //        try{
    //            whereMap.put("USER_ID",getCbmUserID().getKeyForSelected());
    //            whereMap.put("BRANCH_ID",getCbmBranchID().getKeyForSelected());
    //            whereMap.put("IP_ADDR",getCbmIPAddress().getKeyForSelected());
    //            whereMap.put("SCREEN",getCbmScreen().getKeyForSelected());
    //            whereMap.put("MODULE",getCbmModule().getKeyForSelected());
    //            whereMap.put("STATUS",getCbmActivity().getKeyForSelected());
    //            whereMap.put("FROM_DT",getDateFromDate());
    //            whereMap.put("TO_DT",getDateToDate());
    //
    //            if (!getDateToDate().equalsIgnoreCase("")){
    //                appendToDate(whereMap, getDateToDate());
    //            }
    //            checkFindByHistoryNotNull(whereMap);
    //            optionAfterDate = checkDateNotNull(whereMap);
    //            checkRadioButtonSelected(whereMap);
    //
    //        }catch(Exception e){
    //            log.info("Exception in hashMapForMultipleIPAddr(): "+e);
    //            parseException.logException(e,true);
    //        }
    //        return whereMap;
    //    }
    
    public HashMap hashMapForMultipleRecords(){
        HashMap whereMap = new HashMap();
        try{
            whereMap.put("USER_ID",getCbmUserID().getKeyForSelected());
            whereMap.put("BRANCH_ID",getCbmBranchID().getKeyForSelected());
            whereMap.put("IP_ADDR",getCbmIPAddress().getKeyForSelected());
            whereMap.put("SCREEN",getCbmScreen().getKeyForSelected());
            whereMap.put("MODULE",getCbmModule().getKeyForSelected());
            whereMap.put("STATUS",getCbmActivity().getKeyForSelected());
            whereMap.put("FROM_DT",getDateFromDate());
            whereMap.put("TO_DT",getDateToDate());
            
            if (!getDateToDate().equalsIgnoreCase("")){
                appendToDate(whereMap, getDateToDate());
            }
            checkFindByHistoryNotNull(whereMap);
            //            optionAfterDate = checkDateNotNull(whereMap);
            checkRadioButtonSelected(whereMap);
            //            if(optionAfterDate){
            //                whereMap = null;
            //            }
        }catch(Exception e){
            log.info("Exception in hashMapForMultipleRecords(): "+e);
            parseException.logException(e,true);
        }
        return whereMap;
    }
    
    // To get the Data from the Table into the View Log Dialog
    public HashMap populateViewLogDialogForOneRow(int row){
        HashMap resultMap = new HashMap();
        try{
            HashMap editorPaneMap = new HashMap();
            HashMap lblDispMap = new HashMap();
            HashMap oneRecord = new HashMap();
            oneRecord.put("TIME_STAMP", tblViewLog.getValueAt(row, 0));
            lblDispMap.put("BRANCH_ID", tblViewLog.getValueAt(row, 1));
            oneRecord.put("USER_ID", tblViewLog.getValueAt(row, 2));
            oneRecord.put("IP_ADDR", tblViewLog.getValueAt(row, 3));
            lblDispMap.put("SCREEN", tblViewLog.getValueAt(row, 4));
            lblDispMap.put("MODULE", tblViewLog.getValueAt(row, 5));
            lblDispMap.put("PRIMARY_KEY", tblViewLog.getValueAt(row, 6));
            oneRecord.put("STATUS", tblViewLog.getValueAt(row, 7));
            oneRecord.put("DATA", tblViewLog.getValueAt(row, 8));
            oneRecord.put("REMARKS", tblViewLog.getValueAt(row, 9));
            editorPaneMap.put("1", oneRecord);
            oneRecord = null;
            resultMap.put("LABEL_DISP", lblDispMap);
            resultMap.put("EDITOR_PANE", editorPaneMap);
        }catch(Exception e){
            log.info("Exception in populateViewLogDialogForOneRow(): "+e);
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    
    /** To set Where Map from the appropriate fields
     * @param whereMap Sets Where Map.
     * @param row Row count
     */
    private void getWhereMap(HashMap whereMap, int row){
        try{
            whereMap.put("BRANCH_ID", tblViewLog.getValueAt(row, 1));
            whereMap.put("SCREEN",  tblViewLog.getValueAt(row, 4));
            whereMap.put("MODULE",  tblViewLog.getValueAt(row, 5));
            whereMap.put("PRIMARY_KEY",  tblViewLog.getValueAt(row, 6));
        }catch(Exception e){
            log.info("Exception in getWhereMap(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    /** Sets the result Map to executed query from result List
     * @param resultList
     * @param resultMap
     */
    private void putResultMap(List resultList, HashMap resultMap){
        try{
            HashMap oneRecord;
            HashMap editorPaneMap = new HashMap();
            HashMap lblDispMap = new HashMap();
            if (resultList.size() > 0){
                lblDispMap.put("BRANCH_ID",((HashMap)resultList.get(0)).get("BRANCH_ID"));
                lblDispMap.put("MODULE",((HashMap)resultList.get(0)).get("MODULE"));
                lblDispMap.put("SCREEN",((HashMap)resultList.get(0)).get("SCREEN"));
                lblDispMap.put("PRIMARY_KEY",((HashMap)resultList.get(0)).get("PRIMARY_KEY"));
            }
            for (int i = resultList.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = new HashMap();
                oneRecord.put("TIME_STAMP",((HashMap)resultList.get(j)).get("TIME_STAMP"));
                oneRecord.put("USER_ID",((HashMap)resultList.get(j)).get("USER_ID"));
                oneRecord.put("IP_ADDR",((HashMap)resultList.get(j)).get("IP_ADDR"));
                oneRecord.put("STATUS",((HashMap)resultList.get(j)).get("STATUS"));
                oneRecord.put("DATA",((HashMap)resultList.get(j)).get("DATA"));
                oneRecord.put("REMARKS",((HashMap)resultList.get(j)).get("REMARKS"));
                editorPaneMap.put(String.valueOf(j+1), oneRecord);
                oneRecord = null;
            }
            resultMap.put("LABEL_DISP", lblDispMap);
            resultMap.put("EDITOR_PANE", editorPaneMap);
        }catch(Exception e){
            log.info("Exception in putResultMap(): "+e);
            parseException.logException(e,true);
        }
    }
    
    
    /** To date is appended with the time 23:59:59 -> 11:59:59 pm
     * @param whereMap with the key TO_DT is taken
     * @param getStringDate Map with the key value TO_DT has to be set from getStringDate
     */
    private void appendToDate(HashMap whereMap,String getStringDate){
        try{
            StringBuffer toDate = new StringBuffer();
            toDate.append(getStringDate);
            toDate.append(STRING_SPACE);
            toDate.append(TIME);
            whereMap.put("TO_DT",toDate.toString());
        }catch(Exception e){
            log.info("Exception in appendToDate(): "+e);
            parseException.logException(e,true);
        }
    }
    
    /** Resets all the Radio Buttons to false.*/
    public void resetRadioButton(){
        setRdoNew(false);
        setRdoEdit(false);
        setRdoDelete(false);
        setRdoAll(false);
        notifyObservers();
    }
    
    /** Resets all the Fields to null. */
    public void resetFields(){
        setDateFromDate("");
        setDateToDate("");
        setCboFindbyHistory("");
        setCboUserID("");
        setCboBranchID("");
        setCboScreen("");
        setCboIPAddress("");
        setCboModule("");
        setCboActivity("");
        setRdoNew(false);
        setRdoEdit(false);
        setRdoDelete(false);
        setRdoAll(false);
        notifyObservers();
    }
    
    /** Resets Activity combo box to null. */
    public void resetComboActivity(){
        setCboActivity("");
        notifyObservers();
    }
    
    /** Resets Activity combo box to null. */
    public void resetComboFindByHistory(){
        setCboFindbyHistory("");
        notifyObservers();
    }
}