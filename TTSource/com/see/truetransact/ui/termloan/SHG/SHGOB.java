/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGOB.java
 *
 * Created on Sat Oct 15 11:56:39 IST 2011
 */

package com.see.truetransact.ui.termloan.SHG;

import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.termloan.SHG.SHGTO;

/**
 *
 * @author
 */

public class SHGOB extends Observable{
    
    private String txtMemberNo = "";
    private String txtGroupId = "";
    private String lblMemberNameVal = "";
    private String lblStreetVal = "";
    private String lblAreaVal = "";
    private String lblCityVal = "";
    private String lblStateVal = "";
    private String txtGroupName = "";
    private String cboProdId = "";
    private String txtAccountNo = "";
    private ComboBoxModel cbmProdId;
    
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblSHGDetails;
    
    private boolean newData = false;
    private LinkedHashMap shgMap;
    private LinkedHashMap deletedShgMap;
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(SHGOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private int actionType;
    private SHGTO shgMemberTo = null;
    
    
    /** Creates a new instance of TDS MiantenanceOB */
    public SHGOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "SHGJNDI");
            map.put(CommonConstants.HOME, "SHGJNDIHome");
            map.put(CommonConstants.REMOTE, "SHG");
            setTableTile();
            tblSHGDetails= new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("Member No");
        tableTitle.add("Member Name");
        IncVal = new ArrayList();
    }
    
    private void fillDropdown() throws Exception{
        try{
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"Cash.getAccProductAD");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProdId= new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            if(data.containsKey("SHGTO_DATA")){
                shgMap = (LinkedHashMap)data.get("SHGTO_DATA");
                ArrayList addList =new ArrayList(shgMap.keySet());
                for(int i=0;i<addList.size();i++){
                    SHGTO  objSHGTO = (SHGTO)  shgMap.get(addList.get(i));
                    objSHGTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objSHGTO.setAuthorizedStatus("");
                    objSHGTO.setAuthorizedDt(DateUtil.getDateMMDDYYYY(""));
                    objSHGTO.setAuthorizedBy("");
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(objSHGTO.getMemberNo());
                    incTabRow.add(objSHGTO.getMemberName());
                    tblSHGDetails.addRow(incTabRow);
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
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
    
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final SHGTO shgMemberTo = new SHGTO();
            if( shgMap== null ){
                shgMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    shgMemberTo.setStatusDt(ClientUtil.getCurrentDate());
                    shgMemberTo.setStatusBy(TrueTransactMain.USER_ID);
                    shgMemberTo.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    shgMemberTo.setStatusDt(ClientUtil.getCurrentDate());
                    shgMemberTo.setStatusBy(TrueTransactMain.USER_ID);
                    shgMemberTo.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                shgMemberTo.setStatusDt(ClientUtil.getCurrentDate());
                shgMemberTo.setStatusBy(TrueTransactMain.USER_ID);
                shgMemberTo.setStatus(CommonConstants.STATUS_CREATED);
            }
            
            if(isNewData()){
                ArrayList data = tblSHGDetails.getDataArrayList();
            }
            shgMemberTo.setShgId(getTxtGroupId());
            shgMemberTo.setGroupName(getTxtGroupName());
            shgMemberTo.setAccountNo(getTxtAccountNo());
            shgMemberTo.setProdID(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
            shgMemberTo.setMemberNo(getTxtMemberNo());
            shgMemberTo.setMemberName(getLblMemberNameVal());
            shgMemberTo.setStreet(getLblStreetVal());
            shgMemberTo.setArea(getLblAreaVal());
            shgMemberTo.setCity(getLblCityVal());
            shgMemberTo.setState(getLblStateVal());
            shgMap.put(shgMemberTo.getMemberNo(),shgMemberTo);
            updateScheduleDetails(rowSel,shgMemberTo);
//            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateScheduleDetails(int rowSel, SHGTO shgMemberTo)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblSHGDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblSHGDetails.getDataArrayList().get(j)).get(0);
            if(shgMemberTo.getMemberNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblSHGDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtMemberNo());
                IncParRow.add(getLblMemberNameVal());
                tblSHGDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtMemberNo());
            IncParRow.add(getLblMemberNameVal());
            tblSHGDetails.insertRow(tblSHGDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateSHGDetails(String row){
        try{
            resetSHGDetails();
            final SHGTO shgMemberTo = (SHGTO)shgMap.get(row);
            populateTableData(shgMemberTo);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteTableData(String val, int row){
        if(deletedShgMap== null){
            deletedShgMap = new LinkedHashMap();
        }
        SHGTO shgMemberTo = (SHGTO) shgMap.get(val);
        shgMemberTo.setStatus(CommonConstants.STATUS_DELETED);
        deletedShgMap.put(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(row,0)),shgMap.get(val));
        Object obj;
        obj=val;
        shgMap.remove(val);
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
        incDataList = new ArrayList(shgMap.keySet());
        ArrayList addList =new ArrayList(shgMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            SHGTO shgMemberTo = (SHGTO) shgMap.get(addList.get(i));
            IncVal.add(shgMemberTo);
            if(!shgMemberTo.getStatus().equals("DELETED")){
                incTabRow.add(shgMemberTo.getMemberNo());
                incTabRow.add(shgMemberTo.getMemberName());
                tblSHGDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    private void populateTableData(SHGTO shgMemberTo)  throws Exception{
        setTxtGroupId(CommonUtil.convertObjToStr(shgMemberTo.getShgId()));
        setTxtGroupName(CommonUtil.convertObjToStr(shgMemberTo.getGroupName()));
        setTxtAccountNo(CommonUtil.convertObjToStr(shgMemberTo.getAccountNo()));
        setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(shgMemberTo.getProdID())));
        setTxtMemberNo(CommonUtil.convertObjToStr(shgMemberTo.getMemberNo()));
        setLblMemberNameVal(CommonUtil.convertObjToStr(shgMemberTo.getMemberName()));
        setLblStreetVal(CommonUtil.convertObjToStr(shgMemberTo.getStreet()));
        setLblAreaVal(CommonUtil.convertObjToStr(shgMemberTo.getArea()));
        setLblCityVal(CommonUtil.convertObjToStr(shgMemberTo.getCity()));
        setLblStateVal(CommonUtil.convertObjToStr(shgMemberTo.getState()));
        setChanged();
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
        
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("SHGTableDetails",shgMap);
            if(deletedShgMap!=null && deletedShgMap.size()>0 ){
                data.put("deletedSHGTableDetails",deletedShgMap);
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        if(getTxtGroupId().length()>0){
            data.put("SHG_ID",getTxtGroupId());
        }
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        System.out.println("data in SHG OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
//        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    
    protected void getCustomerAddressDetails(String value){
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("CUST_ID",value);
        custAddressMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAccInfoDisplay",custAddressMap);
        if(lst!=null && lst.size()>0){
            custAddressMap = (HashMap)lst.get(0);
            setLblStreetVal(CommonUtil.convertObjToStr(custAddressMap.get("STREET")));
            setLblAreaVal(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCityVal(CommonUtil.convertObjToStr(custAddressMap.get("CITY1")));
            setLblStateVal(CommonUtil.convertObjToStr(custAddressMap.get("STATE1")));
        }
    }
    
    public void resetForm(){
        shgMap = null;
        deletedShgMap= null;
        resetTableValues();
        setTxtGroupId("");
        setTxtGroupName("");
        setCboProdId("");
        setTxtAccountNo("");
        resetSHGDetails();
        setChanged();
    }
    
    public void resetSHGDetails(){
        setTxtMemberNo("");
        setLblAreaVal("");
        setLblCityVal("");
        setLblMemberNameVal("");
         setLblStateVal("");
         setLblStreetVal("");
     }
    
    public void resetTableValues(){
        tblSHGDetails.setDataArrayList(null,tableTitle);
    }
    
    // Setter method for txtMemberNo
    void setTxtMemberNo(String txtMemberNo){
        this.txtMemberNo = txtMemberNo;
        setChanged();
    }
    // Getter method for txtMemberNo
    String getTxtMemberNo(){
        return this.txtMemberNo;
    }
    
    // Setter method for txtGroupId
    void setTxtGroupId(String txtGroupId){
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtGroupId
    String getTxtGroupId(){
        return this.txtGroupId;
    }
    
    /**
     * Getter for property tblSHGDetails.
     * @return Value of property tblSHGDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSHGDetails() {
        return tblSHGDetails;
    }
    
    /**
     * Setter for property tblSHGDetails.
     * @param tblSHGDetails New value of property tblSHGDetails.
     */
    public void setTblSHGDetails(com.see.truetransact.clientutil.EnhancedTableModel tblSHGDetails) {
        this.tblSHGDetails = tblSHGDetails;
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
     * Getter for property lblMemberNameVal.
     * @return Value of property lblMemberNameVal.
     */
    public java.lang.String getLblMemberNameVal() {
        return lblMemberNameVal;
    }
    
    /**
     * Setter for property lblMemberNameVal.
     * @param lblMemberNameVal New value of property lblMemberNameVal.
     */
    public void setLblMemberNameVal(java.lang.String lblMemberNameVal) {
        this.lblMemberNameVal = lblMemberNameVal;
    }
    
    /**
     * Getter for property lblStreetVal.
     * @return Value of property lblStreetVal.
     */
    public java.lang.String getLblStreetVal() {
        return lblStreetVal;
    }
    
    /**
     * Setter for property lblStreetVal.
     * @param lblStreetVal New value of property lblStreetVal.
     */
    public void setLblStreetVal(java.lang.String lblStreetVal) {
        this.lblStreetVal = lblStreetVal;
    }
    
    /**
     * Getter for property lblAreaVal.
     * @return Value of property lblAreaVal.
     */
    public java.lang.String getLblAreaVal() {
        return lblAreaVal;
    }
    
    /**
     * Setter for property lblAreaVal.
     * @param lblAreaVal New value of property lblAreaVal.
     */
    public void setLblAreaVal(java.lang.String lblAreaVal) {
        this.lblAreaVal = lblAreaVal;
    }
    
    /**
     * Getter for property lblCityVal.
     * @return Value of property lblCityVal.
     */
    public java.lang.String getLblCityVal() {
        return lblCityVal;
    }
    
    /**
     * Setter for property lblCityVal.
     * @param lblCityVal New value of property lblCityVal.
     */
    public void setLblCityVal(java.lang.String lblCityVal) {
        this.lblCityVal = lblCityVal;
    }
    
    /**
     * Getter for property lblStateVal.
     * @return Value of property lblStateVal.
     */
    public java.lang.String getLblStateVal() {
        return lblStateVal;
    }
    
    /**
     * Setter for property lblStateVal.
     * @param lblStateVal New value of property lblStateVal.
     */
    public void setLblStateVal(java.lang.String lblStateVal) {
        this.lblStateVal = lblStateVal;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /**
     * Getter for property txtGroupName.
     * @return Value of property txtGroupName.
     */
    public java.lang.String getTxtGroupName() {
        return txtGroupName;
    }    
    
    /**
     * Setter for property txtGroupName.
     * @param txtGroupName New value of property txtGroupName.
     */
    public void setTxtGroupName(java.lang.String txtGroupName) {
        this.txtGroupName = txtGroupName;
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
     * Getter for property txtAccountNo.
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccountNo() {
        return txtAccountNo;
    }
    
    /**
     * Setter for property txtAccountNo.
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccountNo(java.lang.String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
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
    
}