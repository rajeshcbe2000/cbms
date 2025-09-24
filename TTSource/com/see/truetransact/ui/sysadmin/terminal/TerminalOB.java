/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TerminalOB.java
 *
 * Created on January 28, 2004, 3:50 PM
 */

package com.see.truetransact.ui.sysadmin.terminal;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.commonutil.ParseException;
import com.see.truetransact.transferobject.sysadmin.terminal.TerminalTO;
import com.see.truetransact.ui.sysadmin.terminal.TerminalRB;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class TerminalOB extends CObservable {
    
    private  HashMap map = null;
    private ProxyFactory proxy = null;
    private static TerminalOB objTerminalOB; // singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(TerminalOB.class);
    private final ArrayList terminalMasterTabTitle = new ArrayList();
    //    private static TerminalRB objTerminalRB = new TerminalRB();
    java.util.ResourceBundle objTerminalRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.terminal.TerminalRB", ProxyParameters.LANGUAGE);
    
    
    private ArrayList existingData;
    private ArrayList newData = new ArrayList();
    private ArrayList deleteRow;
    private ArrayList columnElement;                            // Column data from the table
    private ArrayList rowData = new ArrayList();                //row data from the Table
    private ArrayList data = new ArrayList();                   //existing data from Table
    private ArrayList insertData;                               //data that has to be inserted
    private ArrayList updateData;                               //data that has to be updated
    private ArrayList deleteData;                               //data that has to be deleted
    private ArrayList terminalMasterTabRow;
//    private String txtBranchCode = "";
    private String txtTerminalId = "";
    private String txtTerminalName = "";
    private String txtIPAddress = "";
    private String txtMachineName = "";
    private String txtTerminalDescription = "";
    private EnhancedTableModel tblTerminalMasterTab;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _actionType;
    private int _result;
    private ComboBoxModel cbmBranchCode;
    private String cboBranchCode ;
     private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    /** Creates a new instance of TerminalOB */
    private TerminalOB() throws Exception{
        
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TerminalJNDI");
        map.put(CommonConstants.HOME, "sysadmin.terminal.TerminalHome");
        map.put(CommonConstants.REMOTE, "sysadmin.terminal.Terminal");
        
        try {
            proxy = ProxyFactory.createProxy();
            setTerminalMasterTabTitle();
            tblTerminalMasterTab = new EnhancedTableModel(null, terminalMasterTabTitle);
            removeTerminalMasterRow();
            fillDropdown();
            resetForm();
//            setTxtBranchCode("");
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            _log.info("Creating TerminalOB...");
            objTerminalOB= new TerminalOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
   
    private void fillDropdown() throws Exception{
       HashMap lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
   
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        param.put(CommonConstants.MAP_NAME,"getOwnBranches");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBranchCode = new ComboBoxModel(key,value);
     
    }
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
  
    
     
//     void setTxtBranchCode(String txtBranchCode){
//        this.txtBranchCode = txtBranchCode;
//        setChanged();
//    }
//    
//    String getTxtBranchCode(){
//        return this.txtBranchCode;
//    }
//    
    void setTxtTerminalId(String txtTerminalId){
        this.txtTerminalId = txtTerminalId;
        setChanged();
    }
    
    String getTxtTerminalId(){
        return this.txtTerminalId;
    }
    void setTxtTerminalName(String txtTerminalName){
        this.txtTerminalName = txtTerminalName;
        setChanged();
    }
    
    String getTxtTerminalName(){
        return this.txtTerminalName;
    }
    
    void setTxtIPAddress(String txtIPAddress){
        this.txtIPAddress = txtIPAddress;
        setChanged();
    }
    
    String getTxtIPAddress(){
        return this.txtIPAddress;
    }
    
    void setTxtMachineName(String txtMachineName){
        this.txtMachineName = txtMachineName;
        setChanged();
    }
    
    String getTxtMachineName(){
        return this.txtMachineName;
    }
    
    void setTxtTerminalDescription(String txtTerminalDescription){
        this.txtTerminalDescription = txtTerminalDescription;
        setChanged();
    }
    
    String getTxtTerminalDescription(){
        return this.txtTerminalDescription;
    }
    
    /**
     * Returns an instance of TerminalOB.
     * @return  TerminalOB
     */
    
    public static TerminalOB getInstance()throws Exception{
        return objTerminalOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    void setTblTerminalMaster(EnhancedTableModel terminalMasterTab){
        this.tblTerminalMasterTab = terminalMasterTab;
        setChanged();
    }
    
    EnhancedTableModel getTblTerminalMaster(){
        return this.tblTerminalMasterTab;
    }
    
    // This method removes the row from the Terminal_Master table in UI
    public void removeTerminalMasterRow(){
        tblTerminalMasterTab = new EnhancedTableModel(null, terminalMasterTabTitle);
        setTblTerminalMaster(tblTerminalMasterTab);
    }
    
    // This method resets the UI Fields
    public void resetForm(){
        setTxtTerminalName("");
        setTxtIPAddress("");
        setTxtMachineName("");
        setTxtTerminalDescription("");
//        setTxtBranchCode("");
//         setCboBranchCode("");
        getCbmBranchCode().setKeyForSelected("");
        setChanged();
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** This method sets the Title for the Terminal_Master Table in the UI **/
    private void setTerminalMasterTabTitle() throws Exception{
        terminalMasterTabTitle.add(objTerminalRB.getString("lblTerminalId"));
        terminalMasterTabTitle.add(objTerminalRB.getString("lblBranchCode"));
        terminalMasterTabTitle.add(objTerminalRB.getString("lblTerminalName"));
        terminalMasterTabTitle.add(objTerminalRB.getString("lblMachineName"));
        terminalMasterTabTitle.add(objTerminalRB.getString("lblIpAddress"));
        terminalMasterTabTitle.add(objTerminalRB.getString("lblTerminalDescription"));
    }
    
    /** this method get the existing data from the Table **/
    public void existingData(){
        existingData = new ArrayList();
        deleteRow = new ArrayList();
        int rowCount = tblTerminalMasterTab.getRowCount();
        for(int i=0;i<rowCount;i++){
            columnElement = new ArrayList();
            columnElement.add(tblTerminalMasterTab.getValueAt(i,0));
            columnElement.add(tblTerminalMasterTab.getValueAt(i,1));
            columnElement.add(tblTerminalMasterTab.getValueAt(i,2));
            columnElement.add(tblTerminalMasterTab.getValueAt(i,3));
            columnElement.add(tblTerminalMasterTab.getValueAt(i,4));
            rowData.add(columnElement);
        }
    }
    
    /** This method gets the map name as argument and executes the query
     * @param whereMap the map name that contains selected BRANCH_CODE to be populated in Text Box and CTable
     */
   public void populateData(HashMap whereMap) {
     HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
      
       }
    
    /** This method populates the selected BRANCH_CODE from the viewAll() to the UI
     * @param mapData the map which contains selected viewAll() BRANCH_CODE to be populated in  textbox
     */
    private void populateOB(HashMap mapData){
        TerminalTO objTerminalTO;
        ArrayList arrayTerminalTO = null;
        HashMap data = (HashMap) ((List) mapData.get("TerminalTO")).get(0);
        arrayTerminalTO =  (ArrayList) (mapData.get("TerminalTO"));
//        setTxtBranchCode(CommonUtil.convertObjToStr((data.get("BRANCH_CODE"))));
         setCboBranchCode(CommonUtil.convertObjToStr(getCbmBranchCode().getDataForKey(CommonUtil.convertObjToStr(data.get("BRANCH_CODE")))));
        setTerminalMasterTabTO(arrayTerminalTO);
        ttNotifyObservers();
    }
    
    private void setTerminalMasterTabTO(ArrayList arrayTerminalTO) {
        int toSize=arrayTerminalTO.size();
        for (int i=0;i<toSize;i++){
            terminalMasterTabRow = new ArrayList();
            HashMap columnData =(HashMap) arrayTerminalTO.get(i);
            terminalMasterTabRow.add(columnData.get("TERMINAL_ID"));
           terminalMasterTabRow.add(columnData.get("BRANCH_CODE"));
            terminalMasterTabRow.add(columnData.get("TERMINAL_NAME"));
            terminalMasterTabRow.add(columnData.get("MACHINE_NAME"));
            terminalMasterTabRow.add(columnData.get("IP_ADDR"));
            terminalMasterTabRow.add(columnData.get("TERMINAL_DESCRIPTION"));
            tblTerminalMasterTab.insertRow(0,terminalMasterTabRow);
        }
    }
    
    public void populateLookupMasterTab(int row){
        if(tblTerminalMasterTab.getDataArrayList()!= null){
            ArrayList data = (ArrayList)tblTerminalMasterTab.getDataArrayList().get(row);
            setTxtTerminalId(CommonUtil.convertObjToStr(data.get(0)));
//            setTxtBranchCode(CommonUtil.convertObjToStr(data.get(1)));
            setTxtTerminalName(CommonUtil.convertObjToStr(data.get(2)));
            setTxtMachineName(CommonUtil.convertObjToStr(data.get(3)));
            setTxtIPAddress(CommonUtil.convertObjToStr(data.get(4)));
            setTxtTerminalDescription(CommonUtil.convertObjToStr(data.get(5)));
//            setCboBranchCode(CommonUtil.convertObjToStr(getCbmBranchCode().getDataForKey(CommonUtil.convertObjToStr(data.get(1)))));
            getCbmBranchCode().setKeyForSelected(CommonUtil.convertObjToStr(data.get(1)));
            setChanged();
            ttNotifyObservers();
        }
    }
    
    public void setTableValueAt(){
        final ArrayList tableData = tblTerminalMasterTab.getDataArrayList();
        final int tableRow = tableData.size();
        for (int i=0;i<tableRow;i++){
            if ((((ArrayList)tableData.get(i)).get(0)).equals(txtTerminalId)){
                if(existingData!=null){
                    existingData.add(tableData.get(i));
                }
                tblTerminalMasterTab.setValueAt(txtTerminalName, i, 2);
                tblTerminalMasterTab.setValueAt(txtMachineName, i, 3);
                tblTerminalMasterTab.setValueAt(txtIPAddress, i, 4);
                tblTerminalMasterTab.setValueAt(txtTerminalDescription, i, 5);
                setChanged();
                ttNotifyObservers();
            }
        }
    }
    
    /** This mehod updates the Data Terminal_Master Table in the UI
     *according to the data entered in the TextFields of the UI **/
    private void updateLookupMasterTab(int row) throws Exception{
        TerminalTO objTerminalMasterTO = new TerminalTO();
        tblTerminalMasterTab.setValueAt(cbmBranchCode.getKeyForSelected(), row, 1);
        tblTerminalMasterTab.setValueAt(txtTerminalName, row, 2);
        tblTerminalMasterTab.setValueAt(txtMachineName, row, 3);
        tblTerminalMasterTab.setValueAt(txtIPAddress, row, 4);
        tblTerminalMasterTab.setValueAt(txtTerminalDescription, row, 5);
        setChanged();
        ttNotifyObservers();
    }
    
    /** This method get the Updated data entered into the Terminal_Master Table in the UI **/
    private void doUpdateData(int row){
        existingData.add(tblTerminalMasterTab.getDataArrayList().get(row));
    }
    
    /** This method get the new data entered **/
    private void doNewData(){
        newData.add(terminalMasterTabRow);
    }
    
    /** Insert into the Terminal_Master table in the UI **/
    private void insertTerminalMasterTab() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tblTerminalMasterTab.getRowCount();
        tblTerminalMasterTab.insertRow(row,terminalMasterTabRow);
        resetForm();
    }
    
    /** This method gets necessary fields and accordingly this data is Inserted,
     * Updated or Deleted
     */
    public int addTerminalMasterTab(){
        int optionSelected = -1;
        String columnData[] = new String[4];
        columnData[0] = getTxtTerminalName();
        columnData[1]= getTxtMachineName();
        columnData[2]= getTxtIPAddress();
        columnData[3]= getTxtTerminalDescription();
        try{
            terminalMasterTabRow = new ArrayList();
            terminalMasterTabRow.add(txtTerminalId);
            terminalMasterTabRow.add(cbmBranchCode.getKeyForSelected());
            terminalMasterTabRow.add(txtTerminalName);
            terminalMasterTabRow.add(txtMachineName);
            terminalMasterTabRow.add(txtIPAddress);
            terminalMasterTabRow.add(txtTerminalDescription);
            ArrayList data = tblTerminalMasterTab.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if ( ((((ArrayList)data.get(i)).get(2)).equals(columnData[0]) && (((ArrayList)data.get(i)).get(3)).equals(columnData[1]) && (((ArrayList)data.get(i)).get(5)).equals(columnData[3]))|| (((ArrayList)data.get(i)).get(4)).equals(columnData[2]) ){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {objTerminalRB.getString("cDialogYes"),objTerminalRB.getString("cDialogNo"),objTerminalRB.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, objTerminalRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // option selected is Yes
                        updateLookupMasterTab(i);
                        doUpdateData(i);
                    }else if(optionSelected == 1){
                        // option selected is No
                        resetForm();
                    }
                    break;
                }
                else{
                    //If none of the option is selected
                    break;
                }
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertTerminalMasterTab();
            }
            setChanged();
            notifyObservers();
            terminalMasterTabRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    
    /** This method deletes the Selected Row in the Terminal_Master tabel in the UI **/
    public void deleteTerminalMasterTab(){
        try{
            final TerminalTO objTerminalTO = new TerminalTO();
            final ArrayList data = tblTerminalMasterTab.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ( (((ArrayList)data.get(i)).get(0)).equals((String)txtTerminalId)){
                    if(deleteRow!=null){
                        deleteRow.add(data.get(i));
                    }
                    tblTerminalMasterTab.removeRow(i);
                    break;
                }
            }
            setChanged();
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** This method is called when the save button is Clicked **/
    public void doSave(){
        initialise();
        insertData();
        updateData();
        deleteData();
        deinitialise();
    }
    
    private void initialise(){
        insertData = new ArrayList();
        updateData = new ArrayList();
        deleteData = new ArrayList();
    }
    
    /** This method gets the data that has to be inserted into the database
     *  data the data that is already existing
     *  newData the data that is newly added
     *  insertData the data that is has to be inserted into the database
     */
    public void insertData(){
        data = tblTerminalMasterTab.getDataArrayList();
        int rowDataSize = data.size();
        int rowNewData = newData.size();
        for(int i=0;i<rowDataSize;i++){
            for(int j=0;j<rowNewData;j++){
                if(((ArrayList)newData.get(j)).get(0).equals(((ArrayList)data.get(i)).get(0))){
                    insertData.add(newData.get(j));
                }
            }
        }
        data = null;
        if(insertData.size() != 0){
            /** Insert data if any **/
            setActionType(ClientConstants.ACTIONTYPE_NEW);
            doAction();
        }
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                // If getCommand() is not equal to null, doing the necessary action
                if( getCommand() != null ){
                    doActionPerform();
                }
                //If getCommand() equals to null throwing TTException
                else{
                    final TerminalRB objTerminalRB = new TerminalRB();
                    throw new TTException(objTerminalRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** This method returns the command  either Insert,Update or Delete according to ActionType **/
    private String getCommand() throws Exception{
        String command = null;
        /** Getting over the _actionType New,Edit or Delete and seting
         * the command variable to status either to Insert,Update or Delete */
        switch (_actionType) {
            /** If action type is new, setting command variable to status Insert */
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
                /** if action type is edit, setting the command variable to status Update */
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
                /** If action type is delete, setting the command variable to status Delete */
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final ArrayList arrayTerminalMasterTabTO = setTerminalMasterData();
        final HashMap data = new HashMap();
        data.put("TerminalTO",arrayTerminalMasterTabTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, map);
        setResult(_actionType);
    }
    
    /* To set common data in the Transfer Object*/
    public ArrayList setTerminalMasterData() {
        terminalMasterTabRow = new ArrayList();
        final int dataSizeInsert = insertData.size();
        final int dataSizeUpdate = updateData.size();
        final int dataSizeDelete = deleteData.size();
        
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            //  If the action type is insert
            setTerminalTO(insertData, dataSizeInsert);
        }else if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            //  If the action type is update
            setTerminalTO(updateData, dataSizeUpdate);
        }else if(getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            //  If the action type is delete
            setTerminalTO(deleteData, dataSizeDelete);
        }
        return terminalMasterTabRow;
    }
    
    private void setTerminalTO(ArrayList data, int dataSize){
        for(int i=0;i<dataSize;i++){
            try{
                setTOobject(data,i);
            }catch(Exception e){
                parseException.logException(e,true);
            }
        }
    }
    
    /** This method sets data to the TO Object
     * @param data the data that has to be set to the TO Object
     * @param row the row of the data that has to be set to the TO Object
     */
    private void setTOobject(ArrayList data, int row)throws Exception{
        TerminalTO objTerminalTO;
        objTerminalTO = new TerminalTO();
        objTerminalTO.setCommand(getCommand());
        objTerminalTO.setTerminalId(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(0)));
        objTerminalTO.setBranchCode(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(1)));
        objTerminalTO.setTerminalName(CommonUtil.convertObjToStr((((ArrayList)data.get(row)).get(2))));
        objTerminalTO.setMachineName(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(3)));
        objTerminalTO.setIpAddr(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(4)));
        objTerminalTO.setTerminalDescription(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(5)));
        terminalMasterTabRow.add(objTerminalTO);
    }
    
    /** This method gets the data that has to be updated into the database
     *  rowData the data that is already existing
     *  existingData the data that is newly added
     *  updateData the data that is has to be updated
     */
    public void updateData(){
        int row = rowData.size();
        int rowExistingData = existingData!=null ? existingData.size() : 0;
        for(int i=0;i<row;i++){
            for(int j=0;j<rowExistingData;j++){
                if(((ArrayList)existingData.get(j)).get(0).equals(((ArrayList)rowData.get(i)).get(0))){
                    updateData.add(existingData.get(j));
                }
            }
        }
        if(updateData.size() != 0){
            /** Update the data if any **/
            setActionType(ClientConstants.ACTIONTYPE_EDIT);
            doAction();
        }
    }
    
    /** This method gets the data that has to be deleted from the database
     *  rowData the data that is already existing
     *  deleteRow the data that is newly added
     *  deleteData the data that is has to be deleted
     */
    public void deleteData(){
        int row = rowData.size();
        int rowDelete = deleteRow!=null ? deleteRow.size() : 0;
        for(int i=0;i<row;i++){
            ArrayList rowArray = (ArrayList) rowData.get(i);
            for(int j=0;j<rowDelete;j++){
                if(((ArrayList)deleteRow.get(j)).get(0).equals(rowArray.get(0))){
                    deleteData.add(deleteRow.get(j));
                }
            }
        }
        if(deleteData.size() != 0){
            /** Delete the data if any **/
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
            doAction();
        }
    }
    
    /* This method is used to make all the arraylist used for insertion,deletion,updation null */
    private void deinitialise(){
        insertData = null;
        updateData =  null;
        deleteData =  null;
    }
    
    /**
     * Getter for property cbmBranchCode.
     * @return Value of property cbmBranchCode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchCode() {
        return cbmBranchCode;
    }
    
    /**
     * Setter for property cbmBranchCode.
     * @param cbmBranchCode New value of property cbmBranchCode.
     */
    public void setCbmBranchCode(com.see.truetransact.clientutil.ComboBoxModel cbmBranchCode) {
        this.cbmBranchCode = cbmBranchCode;
    }
    
    /**
     * Getter for property cboBranchCode.
     * @return Value of property cboBranchCode.
     */
    public java.lang.String getCboBranchCode() {
        return cboBranchCode;
    }
    
    /**
     * Setter for property cboBranchCode.
     * @param cboBranchCode New value of property cboBranchCode.
     */
    public void setCboBranchCode(java.lang.String cboBranchCode) {
        this.cboBranchCode = cboBranchCode;
    }
    
}
    
     
 

 
     