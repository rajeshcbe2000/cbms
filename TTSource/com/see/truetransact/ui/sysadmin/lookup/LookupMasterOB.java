/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LookupMasterOB.java
 *
 * Created on March 4, 2004, 11:01 AM
 */

package com.see.truetransact.ui.sysadmin.lookup;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.transferobject.sysadmin.lookup.LookupMasterTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author  Lohith R.
 */
public class LookupMasterOB extends Observable{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static LookupMasterOB objLookupMasterOB;
//    private static LookupMasterRB objLookupMasterRB = new LookupMasterRB();
    
    java.util.ResourceBundle objLookupMasterRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.lookup.LookupMasterRB", ProxyParameters.LANGUAGE);
    
    private int actionType;
    private int result;
    private int option = -1;
    
    private HashMap operationMap;
    private ProxyFactory proxy;
    
    private final ArrayList lookupMasterTabTitle = new ArrayList();
    private EnhancedTableModel tblLookupMasterTab;
    private ArrayList lookupMasterTabRow;
    
    static private ArrayList rowData = new ArrayList();         //row data in the table
    static private ArrayList columnElement = new ArrayList();   //column data in the table
    private ArrayList existingData;                             //existing data when Button Yes is perssed to Update
    private ArrayList newData = new ArrayList();                //new data when Button New is perssed
    private ArrayList deleteRow;                                //deleted data when Button Delete is perssed
    private ArrayList insertData;                               //data that has to be inserted
    private ArrayList updateData;                               //data that has to be updated
    private ArrayList deleteData;                               //data that has to be deleted
    private ArrayList data = new ArrayList();                   //existing data from Table
    
    private String txtLookupID = "";
    private String txtLookupRef = "";
    private String txtLookupDesc = "";
    private HashMap authorizeMap;
    private boolean editable = false; //Added By Kannan AR
    
    static {
        try {
            objLookupMasterOB = new LookupMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of LookupMasterOB */
    public LookupMasterOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        setLookupMasterTabTitle();
        tblLookupMasterTab = new EnhancedTableModel(null, lookupMasterTabTitle);
        removeLookupMasterRow();
        resetFields();
        setTxtLookupID("");
        setEditable(false);
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LookupMasterJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.lookup.LookupMasterHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.lookup.LookupMaster");
    }
    
    private void setLookupMasterTabTitle() throws Exception{
        lookupMasterTabTitle.add(objLookupMasterRB.getString("lblLookupID"));
        lookupMasterTabTitle.add(objLookupMasterRB.getString("lblLookupRef"));
        lookupMasterTabTitle.add(objLookupMasterRB.getString("lblLookupDesc"));
    }
    
    public static LookupMasterOB getInstance() {
        return objLookupMasterOB;
    }
    
    void setTxtLookupID(String txtLookupID){
        this.txtLookupID = txtLookupID;
        setChanged();
    }
    String getTxtLookupID(){
        return this.txtLookupID;
    }
    
    void setTxtLookupRef(String txtLookupRef){
        this.txtLookupRef = txtLookupRef;
        setChanged();
    }
    String getTxtLookupRef(){
        return this.txtLookupRef;
    }
    
    void setTxtLookupDesc(String txtLookupDesc){
        this.txtLookupDesc = txtLookupDesc;
        setChanged();
    }
    String getTxtLookupDesc(){
        return this.txtLookupDesc;
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
        this.actionType = action;
        setChanged();
    }
    public int getActionType(){
        return this.actionType;
    }
    
    void setTblLookupMaster(EnhancedTableModel lookupMasterTab){
        this.tblLookupMasterTab = lookupMasterTab;
        setChanged();
    }
    EnhancedTableModel getTblLookupMaster(){
        return this.tblLookupMasterTab;
    }
    
    /** This method gets the map name as argument and executes the query
     * @param whereMap the map name that contains selected LOOKUP_ID to be populated in Text Box and CTable
     */
    public void populateData(HashMap whereMap) {
        try {
            final HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    /** This method populates the selected LOOKUP_ID from the viewAll() to the UI
     * @param mapData the map which contains selected viewAll() LOOKUP_ID to be populated in  textbox
     */
    private void populateOB(HashMap mapData) throws Exception{
        LookupMasterTO objLookupMasterTO;
        ArrayList arrayLookupMasterTO = null;
         HashMap data =null;
        if(((List) mapData.get("LookupMasterTO")).size()>0){
         data = (HashMap) ((List) mapData.get("LookupMasterTO")).get(0);
        }
        arrayLookupMasterTO =  (ArrayList) (mapData.get("LookupMasterTO"));
        setTxtLookupID(data.get("LOOKUP_ID").toString());
        setLookupMasterTabTO(arrayLookupMasterTO);
        //Added By Kannan AR       
        if(mapData.containsKey("LookupMasterDesc")){
            data = (HashMap) ((List) mapData.get("LookupMasterDesc")).get(0);
            setEditable(false);
            if(CommonUtil.convertObjToStr(data.get("EDITABLE")).equals("Y")){
              setEditable(true);
            }            
        }        
        notifyObservers();
    }
    
    /** This method inserts the data to the Table **/
    private void setLookupMasterTabTO(ArrayList arrayLookupMasterTO) {
        for (int i=0;i<arrayLookupMasterTO.size();i++){
            lookupMasterTabRow = new ArrayList();
            lookupMasterTabRow.add(((HashMap)arrayLookupMasterTO.get(i)).get("LOOKUP_ID"));
            lookupMasterTabRow.add(((HashMap)arrayLookupMasterTO.get(i)).get("LOOKUP_REF_ID"));
            lookupMasterTabRow.add(((HashMap)arrayLookupMasterTO.get(i)).get("LOOKUP_DESC"));
            tblLookupMasterTab.insertRow(0,lookupMasterTabRow);
        }
    }
    
    /** this method get the existing data from the Table **/
    public void existingData(){
        existingData = new ArrayList();
        deleteRow = new ArrayList();
        int rowCount = tblLookupMasterTab.getRowCount();
        for(int i=0;i<rowCount;i++){
            columnElement = new ArrayList();
            columnElement.add(tblLookupMasterTab.getValueAt(i,0));
            columnElement.add(tblLookupMasterTab.getValueAt(i,1));
            columnElement.add(tblLookupMasterTab.getValueAt(i,2));
            rowData.add(columnElement);
        }
    }
    
    /** This method gets necessary fields and accordingly this data is Inserted,
     * Updated or Deleted
     */
    public int addLookupMasterTab(){
        int option = -1;
        String columnData;
        columnData = new String();
        columnData = getTxtLookupRef();
        try{
            lookupMasterTabRow = new ArrayList();
            lookupMasterTabRow.add(txtLookupID);
            lookupMasterTabRow.add(txtLookupRef);
            lookupMasterTabRow.add(txtLookupDesc);
            ArrayList data = tblLookupMasterTab.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if ((((ArrayList)data.get(i)).get(1)).equals(columnData)){
                    // Checking whether existing LOOKUP_REF_ID is equal new LOOKUP_REF_ID
                    exist = true;
                    String[] options = {objLookupMasterRB.getString("cDialogYes"),objLookupMasterRB.getString("cDialogNo"),objLookupMasterRB.getString("cDialogCancel")};
                    option = COptionPane.showOptionDialog(null, objLookupMasterRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (option == 0){
                        // option selected is Yes
                        updateLookupMasterTab(i);
                        doUpdateData(i);
                        resetFields();
                    }else if( option == 1){
                        // option selected is No
                        resetFields();
                    }
                    break;
                }
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertLookupMasterTab();
            }
            setChanged();
            notifyObservers();
            lookupMasterTabRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    /** This method get the new data entered **/
    private void doNewData(){
        newData.add(lookupMasterTabRow);
    }
    
    // Insert into the Table in Lookup Master...
    private void insertLookupMasterTab() throws Exception{
        final LookupMasterTO objLookupMasterTO = new LookupMasterTO();
        int row = tblLookupMasterTab.getRowCount();
        tblLookupMasterTab.insertRow(row,lookupMasterTabRow);
        resetFields();
    }
    
    /** This method get the update data entered (with option YES) **/
    private void doUpdateData(int row){
        existingData.add(tblLookupMasterTab.getDataArrayList().get(row));
    }
    
    // Updates the CTable....
    private void updateLookupMasterTab(int row) throws Exception{
        LookupMasterTO objLookupMasterTO = new LookupMasterTO();
        tblLookupMasterTab.setValueAt(txtLookupDesc, row, 2);
        setChanged();
        notifyObservers();
    }
    
    public void setTableValueAt(){
        final ArrayList tableData = tblLookupMasterTab.getDataArrayList();
        final int tableRow = tableData.size();
        for (int i=0;i<tableRow;i++){
            if ((((ArrayList)tableData.get(i)).get(1)).equals(txtLookupRef)){
                existingData.add(tableData.get(i));
                tblLookupMasterTab.setValueAt(txtLookupDesc, i, 2);
                setChanged();
                notifyObservers();
            }
        }
    }
    
    
    // Delete from CTable ....
    public void deleteLookupMasterTab(){
        try{
            final LookupMasterTO objLookupMasterTO = new LookupMasterTO();
            final ArrayList data = tblLookupMasterTab.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ( (((ArrayList)data.get(i)).get(1)).equals((String)txtLookupRef)){
                    deleteRow.add(data.get(i));
                    tblLookupMasterTab.removeRow(i);
                    break;
                }
            }
            setChanged();
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void doSave(){
        initialise();
        updateData();
        deleteData();
        insertData();
        deinitialise();
    }
    
    /** This method gets the data that has to be inserted into the database
     *  data the data that is already existing
     *  newData the data that is newly added
     *  insertData the data that is has to be inserted into the database
     */
    public void insertData(){
        data = tblLookupMasterTab.getDataArrayList();
        int rowData = data.size();
        int rowNewData = newData.size();
        for(int i=0;i<rowData;i++){
            for(int j=0;j<rowNewData;j++){
                if(((ArrayList)newData.get(j)).get(1).equals(((ArrayList)data.get(i)).get(1))){
                    insertData.add(newData.get(j));
                }
            }
        }
        data = null;
        newData.clear();
        if(insertData.size() != 0){
            /** Insert data if any **/
            setActionType(ClientConstants.ACTIONTYPE_NEW);
            doAction();
        }
    }
    
    /** This method gets the data that has to be updated into the database
     *  rowData the data that is already existing
     *  existingData the data that is newly added
     *  updateData the data that is has to be updated
     */
    public void updateData(){
        int row = rowData.size();
        int rowExistingData = existingData.size();
        for(int i=0;i<row;i++){
            for(int j=0;j<rowExistingData;j++){
                if(((ArrayList)existingData.get(j)).get(1).equals(((ArrayList)rowData.get(i)).get(1))){
                    updateData.add(existingData.get(j));
                }
            }
        }
        existingData.clear();
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
        int rowDelete = deleteRow.size();
        for(int i=0;i<row;i++){
            for(int j=0;j<rowDelete;j++){
                if(((ArrayList)deleteRow.get(j)).get(1).equals(((ArrayList)rowData.get(i)).get(1))){
                    deleteData.add(deleteRow.get(j));
                }
            }
        }
        deleteRow.clear();
        if(deleteData.size() != 0){
            /** Delete the data if any **/
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
            doAction();
        }
    }
    
    private void initialise(){
        insertData = new ArrayList();
        updateData = new ArrayList();
        deleteData = new ArrayList();
    }
    private void deinitialise(){
        insertData = null;
        updateData =  null;
        deleteData =  null;
        
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
//                    final LookupMasterRB objLookupMasterRB = new LookupMasterRB();
                    throw new TTException(objLookupMasterRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(getAuthorizeMap() == null){
            final ArrayList arrayLookupMasterTabTO = setLookupMasterData();
            data.put("LookupMasterTO",arrayLookupMasterTabTO);
            ClientUtil.addLookupValues(arrayLookupMasterTabTO);
        }
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        
        System.out.println("data in OB: " + data);
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
    }
    
    /* To set common data in the Transfer Object*/
    public ArrayList setLookupMasterData() {
        lookupMasterTabRow = new ArrayList();
        final int dataSizeInsert = insertData.size();
        final int dataSizeUpdate = updateData.size();
        final int dataSizeDelete = deleteData.size();
        
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            //  If the action type in insert
            for (int i=0;i<dataSizeInsert;i++){
                try{
                    setTOobject(insertData,i);
                }catch(Exception e){
                    parseException.logException(e,true);
                    
                }
            }
        }
        
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            //  If the action type in update
            for (int i=0;i<dataSizeUpdate;i++){
                try{
                    setTOobject(updateData,i);
                }catch(Exception e){
                    parseException.logException(e,true);
                }
            }
        }
        if(getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            //  If the action type in delete
            for (int i=0;i<dataSizeDelete;i++){
                try{
                    setTOobject(deleteData,i);
                }catch(Exception e){
                    parseException.logException(e,true);
                }
            }
        }
        return lookupMasterTabRow;
    }
    
    /** This method sets data to the TO Object
     * @param data the data that has to be set to the TO Object
     * @param row the row of the data that has to be set to the TO Object
     */
    private void setTOobject(ArrayList data, int row)throws Exception{
        LookupMasterTO objLookupMasterTO;
        objLookupMasterTO = new LookupMasterTO();
        objLookupMasterTO.setCommand(getCommand());
        objLookupMasterTO.setLookupId(((ArrayList)data.get(row)).get(0).toString());
        objLookupMasterTO.setLookupRefId(((ArrayList)data.get(row)).get(1).toString());
        objLookupMasterTO.setLookupDesc(((ArrayList)data.get(row)).get(2).toString());
        lookupMasterTabRow.add(objLookupMasterTO);
    }
    
    /** Gets the command issued Insert , Upadate or Delete **/
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
    
    // To get the Date from the Table into the Amount Combo box and Rate...
    public void populateLookupMasterTab(int row){
        ArrayList data = (ArrayList)tblLookupMasterTab.getDataArrayList().get(row);
        setTxtLookupID((String)data.get(0));
        setTxtLookupRef((String)data.get(1));
        setTxtLookupDesc((String)data.get(2));
        setChanged();
        notifyObservers();
    }
    
    /** This deletes existing row from the Table */
    public void removeLookupMasterRow(){
        int row = tblLookupMasterTab.getRowCount();
        for(int i=0;i<row;i++) {
            tblLookupMasterTab.removeRow(0);
        }
        tblLookupMasterTab = new EnhancedTableModel(null, lookupMasterTabTitle);
        setTblLookupMaster(tblLookupMasterTab);
    }
    
    /** This resets the necessary fields */
    public void resetFields(){
        setTxtLookupRef("");
        setTxtLookupDesc("");
        setChanged();
        notifyObservers();
    }
    
    
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    public HashMap getAuthorizeMap() {
        return this.authorizeMap;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
}