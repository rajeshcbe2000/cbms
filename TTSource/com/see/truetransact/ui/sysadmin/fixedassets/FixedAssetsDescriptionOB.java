/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetsDescriptionTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;

/**
 *
 * @author
 *
 */
public class FixedAssetsDescriptionOB extends CObservable {
    private String assetType ="";
    private String assetSubType ="";
    private String txtStatus = "";
    private String assetDescID = "";
    private String CreatedDt="";
    private String txtStatusBy = "";
    private ComboBoxModel cbmAssetType;
    private String cboAssetType="";
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblAssetDetails;
    
    private boolean newData = false;
    private LinkedHashMap incParMap;
    private LinkedHashMap deletedTableMap;
    private String txtSlNo="";
    
    private final static Logger log = Logger.getLogger(FixedAssetsDescriptionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private FixedAssetsDescriptionOB fadOB;
    //    EmpTrainingRB objEmpTrainingRB = new EmpTrainingRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private FixedAssetsDescriptionTO objfadTO;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public FixedAssetsDescriptionOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FixedAssetsDescriptionJNDI");
            map.put(CommonConstants.HOME, "FixedAssetsDescriptionHome");
            map.put(CommonConstants.REMOTE, "FixedAssetsDescription");
            setTableTile();
            tblAssetDetails = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("SL No");
        tableTitle.add("Asset_Type");
        tableTitle.add("Asset_Sub");
        tableTitle.add("Auth_Status");
        IncVal = new ArrayList();
    }
    
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,"getFixedAssetsProd");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmAssetType = new ComboBoxModel(key,value);
        
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    public HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final FixedAssetsDescriptionTO objfadTO = new FixedAssetsDescriptionTO();
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("FixedAssetsDescription",setFixedAssetsDescriptionData());
            data.put("FixedAssetsDescriptionTableDetails",incParMap);
            data.put("deletedFixedAssetsDescription",deletedTableMap);
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            data.put("FixedAssetsDescriptionTableDetails",incParMap);
        }
        System.out.println("data in EmpTransfer OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("FIXED_ASSET_DESC")));
        }
        setResult(getActionType());
        setResult(actionType);
    }
    
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            if(data.containsKey("FixedAssetsDescriptionTO")){
                incParMap = (LinkedHashMap)data.get("FixedAssetsDescriptionTO");
                ArrayList addList =new ArrayList(incParMap.keySet());
                for(int i=0;i<addList.size();i++){
                    FixedAssetsDescriptionTO  objfadTO = (FixedAssetsDescriptionTO)  incParMap.get(addList.get(i));
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objfadTO.getSlNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objfadTO.getAssetType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objfadTO.getAssetSubType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objfadTO.getAssetAuthorizedStatus()));
                    this.setAssetDescID(CommonUtil.convertObjToStr(objfadTO.getAssetDescID()));
                    tblAssetDetails.addRow(incTabRow);
                }
            }
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public FixedAssetsDescriptionTO setFixedAssetsDescriptionData() {
        
        final FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = new FixedAssetsDescriptionTO();
        try{
            objFixedAssetsDescriptionTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objFixedAssetsDescriptionTO.setAssetStatusBy(TrueTransactMain.USER_ID);
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
                objFixedAssetsDescriptionTO.setAssetDescID(getAssetDescID());
            }
        }catch(Exception e){
            log.info("Error In setEmpTranferData()");
            e.printStackTrace();
        }
        return objFixedAssetsDescriptionTO;
    }
    
    public void resetForm(){
        tblAssetDetails.setDataArrayList(null,tableTitle);
        setAssetDescID("");
        setAssetSubType("");
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetEmpDetails() {
        setAssetSubType("");
        setChanged();
        ttNotifyObservers();
    }
    
    public void deleteTableData(String val, int row){
        if(deletedTableMap == null){
            deletedTableMap = new LinkedHashMap();
        }
        FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) incParMap.get(val);
        objFixedAssetsDescriptionTO.setAssetStatus(CommonConstants.STATUS_DELETED);
        deletedTableMap.put(CommonUtil.convertObjToStr(tblAssetDetails.getValueAt(row,0)),incParMap.get(val));
        Object obj;
        obj=val;
        incParMap.remove(val);
        resetTableValues();
        try{
            populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(incParMap.keySet());
        ArrayList addList =new ArrayList(incParMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO) incParMap.get(addList.get(i));
            
            IncVal.add(objFixedAssetsDescriptionTO);
            if(!objFixedAssetsDescriptionTO.getAssetStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getAssetType()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getAssetSubType()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getAssetAuthorizedStatus()));
                incTabRow.add("");
                tblAssetDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblAssetDetails.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = new FixedAssetsDescriptionTO();
            if( incParMap == null ){
                incParMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objFixedAssetsDescriptionTO.setAssetCreatedBy(TrueTransactMain.USER_ID);
                    objFixedAssetsDescriptionTO.setAssetStatusDt(currDt);
                    objFixedAssetsDescriptionTO.setAssetStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetsDescriptionTO.setAssetCreatedDt(currDt);
                    objFixedAssetsDescriptionTO.setAssetStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objFixedAssetsDescriptionTO.setAssetStatusDt(currDt);
                    objFixedAssetsDescriptionTO.setAssetStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetsDescriptionTO.setAssetStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objFixedAssetsDescriptionTO.setAssetCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetsDescriptionTO.setAssetStatusDt(currDt);
                objFixedAssetsDescriptionTO.setAssetStatusBy(TrueTransactMain.USER_ID);
                objFixedAssetsDescriptionTO.setAssetCreatedDt(currDt);
                objFixedAssetsDescriptionTO.setAssetStatus(CommonConstants.STATUS_CREATED);
            }
            int  slno=0;
            int nums[]= new int[150];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblAssetDetails.getDataArrayList();
                slno=serialNo(data);
            }
            else{
                if(isNewData()){
                    ArrayList data = tblAssetDetails.getDataArrayList();
                    slno=serialNo(data);
                }
                else{
                    int b=CommonUtil.convertObjToInt(tblAssetDetails.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            
            objFixedAssetsDescriptionTO.setSlNo(String.valueOf(slno));
            objFixedAssetsDescriptionTO.setAssetType(getAssetType());
            objFixedAssetsDescriptionTO.setAssetSubType(getAssetSubType());
            incParMap.put(objFixedAssetsDescriptionTO.getSlNo(),objFixedAssetsDescriptionTO);
            String sno=String.valueOf(slno);
            updateEmpDetails(rowSel,sno,objFixedAssetsDescriptionTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void updateEmpDetails(int rowSel, String sno, FixedAssetsDescriptionTO objFixedAssetsDescriptionTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblAssetDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblAssetDetails.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblAssetDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getAssetType());
                IncParRow.add(getAssetSubType());
                IncParRow.add("");
                tblAssetDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getAssetType());
            IncParRow.add(getAssetSubType());
            IncParRow.add("");
            tblAssetDetails.insertRow(tblAssetDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    
    public void populateLeaveDetails(String row){
        try{
            resetEmpDetails();
            final FixedAssetsDescriptionTO objFixedAssetsDescriptionTO = (FixedAssetsDescriptionTO)incParMap.get(row);
            populateTableData(objFixedAssetsDescriptionTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    private void populateTableData(FixedAssetsDescriptionTO objFixedAssetsDescriptionTO)  throws Exception{
        getCbmAssetType().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getAssetType()));
        setTxtSlNo(objFixedAssetsDescriptionTO.getSlNo());
        setAssetSubType(CommonUtil.convertObjToStr(objFixedAssetsDescriptionTO.getAssetSubType()));
        setChanged();
        notifyObservers();
    }
    
    public void resetTableValues(){
        tblAssetDetails.setDataArrayList(null,tableTitle);
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
    
    /**
     * Getter for property txtStatusBy.
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }
    
    /**
     * Setter for property txtStatusBy.
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }
    
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     *
     *
     * /**
     * Getter for property CreatedDt.
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }
    
    /**
     * Setter for property CreatedDt.
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
    }
    
    
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    
    
    /**
     * Getter for property txtStatus.
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }
    
    /**
     * Setter for property txtStatus.
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }
    
    
    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }
    
    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }
    
    /**
     * Getter for property txtSlNo.
     * @return Value of property txtSlNo.
     */
    public java.lang.String getTxtSlNo() {
        return txtSlNo;
    }
    
    /**
     * Setter for property txtSlNo.
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(java.lang.String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }
    
    
    /**
     * Getter for property cbmAssetType.
     * @return Value of property cbmAssetType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAssetType() {
        return cbmAssetType;
    }
    
    /**
     * Setter for property cbmAssetType.
     * @param cbmAssetType New value of property cbmAssetType.
     */
    public void setCbmAssetType(com.see.truetransact.clientutil.ComboBoxModel cbmAssetType) {
        this.cbmAssetType = cbmAssetType;
    }
    
    /**
     * Getter for property cboAssetType.
     * @return Value of property cboAssetType.
     */
    public java.lang.String getCboAssetType() {
        return cboAssetType;
    }
    
    /**
     * Setter for property cboAssetType.
     * @param cboAssetType New value of property cboAssetType.
     */
    public void setCboAssetType(java.lang.String cboAssetType) {
        this.cboAssetType = cboAssetType;
    }
    
    /**
     * Getter for property assetType.
     * @return Value of property assetType.
     */
    public java.lang.String getAssetType() {
        return assetType;
    }
    
    /**
     * Setter for property assetType.
     * @param assetType New value of property assetType.
     */
    public void setAssetType(java.lang.String assetType) {
        this.assetType = assetType;
    }
    
    /**
     * Getter for property assetSubType.
     * @return Value of property assetSubType.
     */
    public java.lang.String getAssetSubType() {
        return assetSubType;
    }
    
    /**
     * Setter for property assetSubType.
     * @param assetSubType New value of property assetSubType.
     */
    public void setAssetSubType(java.lang.String assetSubType) {
        this.assetSubType = assetSubType;
    }
    
    /**
     * Getter for property tblAssetDetails.
     * @return Value of property tblAssetDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblAssetDetails() {
        return tblAssetDetails;
    }
    
    /**
     * Setter for property tblAssetDetails.
     * @param tblAssetDetails New value of property tblAssetDetails.
     */
    public void setTblAssetDetails(com.see.truetransact.clientutil.EnhancedTableModel tblAssetDetails) {
        this.tblAssetDetails = tblAssetDetails;
    }
    
    /**
     * Getter for property assetDescID.
     * @return Value of property assetDescID.
     */
    public java.lang.String getAssetDescID() {
        return assetDescID;
    }
    
    /**
     * Setter for property assetDescID.
     * @param assetDescID New value of property assetDescID.
     */
    public void setAssetDescID(java.lang.String assetDescID) {
        this.assetDescID = assetDescID;
    }
    
}



