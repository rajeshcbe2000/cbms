/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TAMMaintenanceCreateOB.java
 *
 * Created on July 12, 2004, 3:55 PM
 */

package com.see.truetransact.ui.privatebanking.tammaintenance;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.privatebanking.tammaintenance.TAMMaintenanceCreateRB;
import com.see.truetransact.transferobject.privatebanking.tammaintenance.TAMMaintenanceCreateTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;

import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author  Lohith R.
 */
public class TAMMaintenanceCreateOB extends CObservable {
    private static TAMMaintenanceCreateOB objTAMMaintenanceCreateOB; // singleton object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private String pvtTamID = "";
    private String cboAssetClassID = "";
    private String cboAssetSubclassID = "";
    private String cboTAMOrderType = "";
    private boolean rdoTAMDefaultType_Yes = false;
    private boolean rdoTAMDefaultType_No = false;
    private String cboTAMStatus = "";
    
    private ComboBoxModel cbmAssetClassID;
    private ComboBoxModel cbmAssetSubclassID;
    private ComboBoxModel cbmTAMOrderType;
    private ComboBoxModel cbmTAMStatus;
    
    private int actionType;
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    
    static {
        try {
            objTAMMaintenanceCreateOB = new TAMMaintenanceCreateOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of TAMMaintenanceCreatOB */
    public TAMMaintenanceCreateOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }
    
    /** Creates a new instance of TAMMaintenanceCreatOB */
    public static TAMMaintenanceCreateOB getInstance() {
        return objTAMMaintenanceCreateOB;
    }
    
    /** Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TAMMaintenanceCreateJNDI");
        operationMap.put(CommonConstants.HOME, "privatebanking.tammaintenance.TAMMaintenanceCreateHome");
        operationMap.put(CommonConstants.REMOTE, "privatebanking.tammaintenance.TAMMaintenanceCreate");
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PVT_TAM.ASSET_CLS");
        lookup_keys.add("PVT_TAM.ASSET_SUB_CLS");
        lookup_keys.add("PVT_TAM.ORDER_TYPE");
        lookup_keys.add("PVT_TAM.STATUS");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("PVT_TAM.ASSET_CLS"));
        cbmAssetClassID = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT_TAM.ASSET_SUB_CLS"));
        cbmAssetSubclassID = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT_TAM.ORDER_TYPE"));
        cbmTAMOrderType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PVT_TAM.STATUS"));
        cbmTAMStatus = new ComboBoxModel(key,value);
        
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
    
    /** Setter method for pvtTamID */
    void setPvtTamID(String pvtTamID){
        this.pvtTamID = pvtTamID;
        setChanged();
    }
    /** Getter method for pvtTamID */
    String getPvtTamID(){
        return this.pvtTamID;
    }
    
    /** Setter method for cboAssetClassID */
    void setCboAssetClassID(String cboAssetClassID){
        this.cboAssetClassID = cboAssetClassID;
        setChanged();
    }
    /** Getter method for cboAssetClassID */
    String getCboAssetClassID(){
        return this.cboAssetClassID;
    }
    
    /** Setter method for cbmAssetClassID */
    void setCbmAssetClassID(ComboBoxModel cbmAssetClassID){
        this.cbmAssetClassID = cbmAssetClassID;
        setChanged();
    }
    /** Getter method for cbmAssetClassID */
    ComboBoxModel getCbmAssetClassID(){
        return cbmAssetClassID;
    }
    
    /** Setter method for cboAssetSubclassID */
    void setCboAssetSubclassID(String cboAssetSubclassID){
        this.cboAssetSubclassID = cboAssetSubclassID;
        setChanged();
    }
    /** Getter method for cboAssetSubclassID */
    String getCboAssetSubclassID(){
        return this.cboAssetSubclassID;
    }
    
    /** Setter method for cbmAssetSubclassID */
    void setCbmAssetSubclassID(ComboBoxModel cbmAssetSubclassID){
        this.cbmAssetSubclassID = cbmAssetSubclassID;
        setChanged();
    }
    /** Getter method for cbmAssetSubclassID */
    ComboBoxModel getCbmAssetSubclassID(){
        return cbmAssetSubclassID;
    }
    
    /** Setter method for cboTAMOrderType */
    void setCboTAMOrderType(String cboTAMOrderType){
        this.cboTAMOrderType = cboTAMOrderType;
        setChanged();
    }
    /** Getter method for cboTAMOrderType */
    String getCboTAMOrderType(){
        return this.cboTAMOrderType;
    }
    
    /** Setter method for cbmTAMOrderType */
    void setCbmTAMOrderType(ComboBoxModel cbmTAMOrderType){
        this.cbmTAMOrderType = cbmTAMOrderType;
        setChanged();
    }
    /** Getter method for cbmTAMOrderType */
    ComboBoxModel getCbmTAMOrderType(){
        return cbmTAMOrderType;
    }
    
    /** Setter method for rdoTAMDefaultType_Yes */
    void setRdoTAMDefaultType_Yes(boolean rdoTAMDefaultType_Yes){
        this.rdoTAMDefaultType_Yes = rdoTAMDefaultType_Yes;
        setChanged();
    }
    /** Getter method for rdoTAMDefaultType_Yes */
    boolean getRdoTAMDefaultType_Yes(){
        return this.rdoTAMDefaultType_Yes;
    }
    
    /** Setter method for rdoTAMDefaultType_No */
    void setRdoTAMDefaultType_No(boolean rdoTAMDefaultType_No){
        this.rdoTAMDefaultType_No = rdoTAMDefaultType_No;
        setChanged();
    }
    /** Getter method for rdoTAMDefaultType_No */
    boolean getRdoTAMDefaultType_No(){
        return this.rdoTAMDefaultType_No;
    }
    
    /** Setter method for cboTAMStatus */
    void setCboTAMStatus(String cboTAMStatus){
        this.cboTAMStatus = cboTAMStatus;
        setChanged();
    }
    /** Getter method for cboTAMStatus */
    String getCboTAMStatus(){
        return this.cboTAMStatus;
    }
    
    /** Setter method for cbmTAMStatus */
    void setCbmTAMStatus(ComboBoxModel cbmTAMStatus){
        this.cbmTAMStatus = cbmTAMStatus;
        setChanged();
    }
    /** Getter method for cbmTAMStatus */
    ComboBoxModel getCbmTAMStatus(){
        return cbmTAMStatus;
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
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    public void setResult(int result) {
        this.result = result;
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
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final TAMMaintenanceCreateRB objTAMMaintenanceCreateRB = new TAMMaintenanceCreateRB();
                    throw new TTException(objTAMMaintenanceCreateRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
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
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final TAMMaintenanceCreateTO objTO = setTAMMaintenanceCreateTOData();
        objTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("TAMMaintenanceCreateTO",objTO);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
    }
    
    /** Gets the datas from the Fields and sets to TAM Maintenance Create TO */
    public TAMMaintenanceCreateTO setTAMMaintenanceCreateTOData() {
        final TAMMaintenanceCreateTO objTO = new TAMMaintenanceCreateTO();
        try{
            objTO.setTamId(getPvtTamID());
            objTO.setAssetClsId(CommonUtil.convertObjToStr(cbmAssetClassID.getKeyForSelected()));
            objTO.setAssetSubClsId(CommonUtil.convertObjToStr(cbmAssetSubclassID.getKeyForSelected()));
            objTO.setTamOrderType(CommonUtil.convertObjToStr(cbmTAMOrderType.getKeyForSelected()));
            objTO.setTamStatus(CommonUtil.convertObjToStr(cbmTAMStatus.getKeyForSelected()));
            objTO.setStatusBy(TrueTransactMain.USER_ID);
            if(getRdoTAMDefaultType_Yes()){
                objTO.setTamDefType("Y");
            }else{
                objTO.setTamDefType("N");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objTO;
    }
    
    /** To populate to the screen */
    public void populateData(HashMap whereMap) {
        HashMap mapData = new HashMap() ;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) {
        TAMMaintenanceCreateTO objTO;
        objTO = (TAMMaintenanceCreateTO) ((List) mapData.get("TAMMaintenanceCreateTO")).get(0);
        getTAMMaintenanceCreateTOData(objTO);
        objTO = null;
        notifyObservers();
    }
    
    /** Gets datas from DepositRollover TO and sets to Fields*/
    public void getTAMMaintenanceCreateTOData(TAMMaintenanceCreateTO objTO){
        setCboAssetClassID(CommonUtil.convertObjToStr(getCbmAssetClassID().getDataForKey(objTO.getAssetClsId())));
        setCboAssetSubclassID(CommonUtil.convertObjToStr(getCbmAssetSubclassID().getDataForKey(objTO.getAssetSubClsId())));
        setCboTAMOrderType(CommonUtil.convertObjToStr(getCbmTAMOrderType().getDataForKey(objTO.getTamOrderType())));
        setCboTAMStatus(CommonUtil.convertObjToStr(getCbmTAMStatus().getDataForKey(objTO.getTamStatus())));
        if(objTO.getTamDefType().equals("Y")){
            setRdoTAMDefaultType_Yes(true);
        }else{
            setRdoTAMDefaultType_No(true);
        }
    }
    
    /** Resets all the Fields to Null  */
    public void resetFields(){
        setCboAssetClassID("");
        setCboAssetSubclassID("");
        setCboTAMOrderType("");
        setCboTAMStatus("");
        setRdoTAMDefaultType_Yes(false);
        setRdoTAMDefaultType_No(false);
        notifyObservers();
    }
    
}
