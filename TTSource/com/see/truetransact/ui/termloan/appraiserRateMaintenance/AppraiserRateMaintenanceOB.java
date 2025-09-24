/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupOB.java
 *
 * Created on Thu Aug 25 11:07:03 IST 2005
 */

package com.see.truetransact.ui.termloan.appraiserRateMaintenance;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;


import javax.swing.DefaultListModel;
import java.util.Date;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.sysadmin.branchgroup.BranchGroupTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.sysadmin.branchgroup.BranchGroupDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.termloan.appraiserRateMaintenance.AppraiserRateMaintenanceTO;
/**
 *
 * @author  Ashok
 */

public class AppraiserRateMaintenanceOB extends CObservable{
    private static AppraiserRateMaintenanceOB appraiserRateMaintenanceOB;
    private HashMap map, objHashMap;
    private ProxyFactory proxy;
    
    private String txtGroupId = "";
    private String txtGroupName = "";
    private String lstAvailBranch = "";
    private String lstGrantedBranch = "";
    
    
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtFromAmt = "";
    private String txtToAmt = "";
    private boolean rdoAbsolute = false;
    private boolean rdoPercentage = false;
    private String txtAmt = "";
    private String txtMaxAmt = "";
    private String txtPercentage = "";
    private String txtServiceTax = "";
    private String lblStatus;
    private int actionType;
    private int result,_actionType;
    private ArrayList newData, deletedData,arrayListBranchGroupDetailsTO;
    
    private AppraiserRateMaintenanceTO objTO;
    private EnhancedTableModel tblInterestTable;
    private ArrayList overAllList = new ArrayList();
    private ArrayList deletedOverAllList = new ArrayList();
    private final ArrayList interestList = new ArrayList();//, deletedData,arrayListBranchGroupDetailsTO;
    
    private BranchGroupTO objBranchGroupTO;
    private BranchGroupDetailsTO objBranchGroupDetailsTO;
    private int i;
    private Date currDt = ClientUtil.getCurrentDate();
    private DefaultListModel lsmAvailableBranch = new DefaultListModel();
    private DefaultListModel lsmGrantedBranch = new DefaultListModel();;
    
    java.util.ResourceBundle objInterestRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.appraiserRateMaintenance.AppraiserRateMaintenanceRB", ProxyParameters.LANGUAGE);
        
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            appraiserRateMaintenanceOB = new AppraiserRateMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates new Instance of this class */
    private AppraiserRateMaintenanceOB() throws Exception{
        setTableTitle();
        tblInterestTable = new EnhancedTableModel(null,interestList);
        map = new HashMap();
        map.put(CommonConstants.JNDI, "AppraiserRateMaintenanceJNDI");
        map.put(CommonConstants.HOME, "termloan.appraiserRateMaintenance.AppraiserRateMaintenanceHome");
        map.put(CommonConstants.REMOTE,"termloan.appraiserRateMaintenance.AppraiserRateMaintenance");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    /** Returns an instance of this Class */
    public static AppraiserRateMaintenanceOB getInstance() {
        return appraiserRateMaintenanceOB;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            ArrayList arrayBranchGLTO = null;
            mapData = proxy.executeQuery(whereMap, map);
            BranchGroupTO objBranchGroupTO = (BranchGroupTO)  ((ArrayList) (mapData.get("BranchGroupTO"))).get(0);
            System.out.println("BranchGroupTO "+ objBranchGroupTO);
            overAllList = (ArrayList)(mapData.get("AppraiserCommisionTO"));
            setTableRecords(overAllList);
            setTxtGroupId(objBranchGroupTO.getBranchGroupId());
            setTxtGroupName(objBranchGroupTO.getBranchGroupName());
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    private void setTableRecords(ArrayList overAllList){
        ArrayList totalList = new ArrayList();
        int size = overAllList.size();
        for (int i = 0;i<size;i++){
            ArrayList row = new ArrayList();
            AppraiserRateMaintenanceTO objTOs = (AppraiserRateMaintenanceTO) overAllList.get(i);
            row.add(CommonUtil.convertObjToStr(objTOs.getFromDate()));
            row.add(objTOs.getToDate());
            row.add(objTOs.getFromAmt());
            row.add(objTOs.getToAmt());
            row.add(objTOs.getAmount());
            row.add(objTOs.getServiceTax());
            if(CommonUtil.convertObjToDouble(objTOs.getPercentage()).doubleValue() == 0){                
                row.add("");
            }else {
                row.add(objTOs.getPercentage());
            }
            tblInterestTable.insertRow(i,row);
        }
    }
    
    /**Method used to filldown a List in the UI by seeting its model */
    public void fillAvailableBranchIds() {
        ArrayList lstData = (ArrayList) ClientUtil.executeQuery("getNewBranchIDs", null);
        lsmAvailableBranch = new DefaultListModel();
        if (lstData.size() > 0) {
            for (int i =0, j=lstData.size(); i < j; i++) {
                lsmAvailableBranch.addElement(((HashMap)lstData.get(i)).get("BRANCH_ID"));
            }
        }
        
    }
    /**Method used to filldown a List in the UI by seeting its model */
    public void fillDropdown(String groupId) {
        HashMap condition = new HashMap();
        condition.put("BRANCH_GROUP_ID", groupId);
        
        ArrayList lstData = (ArrayList) ClientUtil.executeQuery("getBranchIds", condition);
        lsmAvailableBranch = new DefaultListModel();
        if (lstData.size() > 0) {
            for (int i =0, j=lstData.size(); i < j; i++) {
                lsmAvailableBranch.addElement(((HashMap)lstData.get(i)).get("BRANCH_ID"));
            }
        }
        
        ArrayList lstGrantData = (ArrayList) ClientUtil.executeQuery("getSelectBranchGroupDetails", condition);
        lsmGrantedBranch = new DefaultListModel();
        if (lstGrantData.size() > 0) {
            for (int i =0, j=lstGrantData.size(); i < j; i++) {
                lsmGrantedBranch.addElement(((HashMap)lstGrantData.get(i)).get("BRANCH_ID"));
            }
        }
    }
    
    /** This method used to fillup an ArrayList which contains the daata which is newly filled up in a List IN UI*/
    public void newBranchIds(Object[] newBranchIds){
        if (newData == null)
            newData = new ArrayList();
        
        for(int i=0; i<newBranchIds.length;i++){
            newData.add(i,newBranchIds[i]);
        }
    }
    
    /** This method is used to fill up an arraylist which contains data removed from the List in the UI */
    public void removedBranchIds(Object[] removedBranchIds){
        if (deletedData == null)
            deletedData = new ArrayList();
        
        for(int i=0; i<removedBranchIds.length;i++){
            deletedData.add(i,removedBranchIds[i]);
        }
    }
    
    /** Getter for lsmAvailableBranch */
    public DefaultListModel getLsmAvailableBranch() {
        return lsmAvailableBranch;
    }
    
    /** Setter for lsmAvailableBranch */
    public void setLsmAvailableBranch(DefaultListModel lsmAvailableBranch) {
        this.lsmAvailableBranch = lsmAvailableBranch;
        setChanged();
    }
    
    /** Getter for lsmGrantedBranch */
    public DefaultListModel getLsmGrantedBranch() {
        return lsmGrantedBranch;
    }
    /** Setter for lsmGrantedBranch */
    public void setLsmGrantedBranch(DefaultListModel lsmGrantedBranch) {
        this.lsmGrantedBranch = lsmGrantedBranch;
        setChanged();
    }
    
    /** Retrun actionType wheter new,edit or delete */
    public int getActionType(){
        return actionType;
    }
    
    /* Sets the ActionType either to New, Edit, or Delete */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    // Setter method for txtGroupID
    void setTxtGroupId(String txtGroupId){
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtGroupID
    String getTxtGroupId(){
        return this.txtGroupId;
    }
    
    // Setter method for txtGroupName
    void setTxtGroupName(String txtGroupName){
        this.txtGroupName = txtGroupName;
        setChanged();
    }
    // Getter method for txtGroupName
    String getTxtGroupName(){
        return this.txtGroupName;
    }
    
    // Setter method for lstAvailBranch
    void setLstAvailBranch(String lstAvailBranch){
        this.lstAvailBranch = lstAvailBranch;
        setChanged();
    }
    // Getter method for lstAvailBranch
    String getLstAvailBranch(){
        return this.lstAvailBranch;
    }
    
    // Setter method for lstGrantedBranch
    void setLstGrantedBranch(String lstGrantedBranch){
        this.lstGrantedBranch = lstGrantedBranch;
        setChanged();
    }
    // Getter method for lstGrantedBranch
    String getLstGrantedBranch(){
        return this.lstGrantedBranch;
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** Sets the to object */
    public void setBranchGroupDetailsTO(BranchGroupDetailsTO objBranchGroupDetailsTO){
        setTxtGroupId(objBranchGroupDetailsTO.getBranchGroupId());
        notifyObservers();
    }
    
    /** Resets the UI Fields */
    public void resetForm(){
        setTxtGroupId("");
        setTxtGroupName("");
        setLblStatus("");
        resetList();
        resetTableValues();
    }
    private void resetTableValues(){
        tblInterestTable.setDataArrayList(null,interestList);
    }
    public void resetFormDownSave(){
        setTdtFromDate("");
        setTdtToDate("");
        setTxtFromAmt("");
        setTxtToAmt("");
        setTxtAmt("");
        setTxtServiceTax("");
        setTxtPercentage("");
    }
    
    /** Resets the List in UI to empty */
    private void resetList(){
        lsmGrantedBranch.clear();
        setLsmGrantedBranch(lsmGrantedBranch);
        lsmAvailableBranch.clear();
        setLsmAvailableBranch(lsmAvailableBranch);
    }
    private void setTableTitle() {
        try{
            interestList.add(objInterestRB.getString("tblColumnInterest1"));
            interestList.add(objInterestRB.getString("tblColumnInterest2"));
            interestList.add(objInterestRB.getString("tblColumnInterest3"));
            interestList.add(objInterestRB.getString("tblColumnInterest4"));
            interestList.add(objInterestRB.getString("tblColumnInterest5"));
            interestList.add(objInterestRB.getString("tblColumnInterest6"));
            interestList.add(objInterestRB.getString("tblColumnInterest7"));
        }catch (Exception e){
            
        }
    }
    /** Method which do appropriate operation either Insertion or updation or deletion Based on The Action Type */
    public void doSave()throws Exception{
        objBranchGroupTO = getBranchGroupTO();
        arrayListBranchGroupDetailsTO = new ArrayList();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            newData();
            deletedData();
        }else{
            DefaultListModel lsmGrant = getLsmGrantedBranch();
            for(i=0; i<lsmGrant.size();i++){
                objBranchGroupDetailsTO = new BranchGroupDetailsTO();
                if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    objBranchGroupDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objBranchGroupDetailsTO.setBranchGroupId(getTxtGroupId());
                    objBranchGroupDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                String element = CommonUtil.convertObjToStr(lsmGrant.get(i));
                objBranchGroupDetailsTO.setBranchId(element.substring(0,element.indexOf("(")).trim());
                objBranchGroupDetailsTO.setCommand(getCommand());
                arrayListBranchGroupDetailsTO.add(i,objBranchGroupDetailsTO);
            }
        }
        doAction();
        deinitialise();
    }
    
    /* Clear up the Objects used */
    private void deinitialise(){
        newData = null;
        deletedData = null;
        arrayListBranchGroupDetailsTO = null;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    //                    final AppraiserRateMaintenanceRB objBranchGroupRB = new AppraiserRateMaintenanceRB();
                    //                    throw new TTException(objBranchGroupRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** Adds up the Newly included data in a arrayList */
    private void newData()throws Exception{
        if(newData != null){
            if(getActionType() != ClientConstants.ACTIONTYPE_EDIT)
                setActionType(ClientConstants.ACTIONTYPE_NEW);
            else
                setActionType(getActionType());
            if(newData.size() > 0){
                setBranchGroupDetailsTO(newData);
            }
        }
    }
    
    /** Adds up the Excluded Data in a ArrayList */
    private void  deletedData()throws Exception{
        if(deletedData != null){
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
            if(deletedData.size() > 0){
                setBranchGroupDetailsTO(deletedData);
            }
        }
    }
    
    /* Sets the BranchGroupDetailsTO variables */
    private void setBranchGroupDetailsTO(ArrayList data)throws Exception{
        for(i=0; i<data.size(); i++){
            objBranchGroupDetailsTO = new BranchGroupDetailsTO();
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objBranchGroupDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            }else{
                objBranchGroupDetailsTO.setBranchGroupId(getTxtGroupId());
                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    objBranchGroupDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objBranchGroupDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                }
            }
            String element = CommonUtil.convertObjToStr(data.get(i));
            objBranchGroupDetailsTO.setBranchId(element.substring(0,element.indexOf("(")).trim());
            objBranchGroupDetailsTO.setCommand(getCommand());
            arrayListBranchGroupDetailsTO.add(objBranchGroupDetailsTO);
            objBranchGroupDetailsTO = null;
        }
    }
    
    /** This method returns an Instane of BranchGroupTO object **/
    private BranchGroupTO getBranchGroupTO()throws Exception{
        BranchGroupTO objBranchGroupTO = new BranchGroupTO();
        objBranchGroupTO.setBranchGroupId(getTxtGroupId());
        objBranchGroupTO.setBranchGroupName(getTxtGroupName());
        objBranchGroupTO.setCommand(getCommand());
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            objBranchGroupTO.setStatus(CommonConstants.STATUS_CREATED);
        }else{
            objBranchGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
        objBranchGroupTO.setStatusBy(TrueTransactMain.USER_ID);
        objBranchGroupTO.setStatusDt(currDt);
        return objBranchGroupTO;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(objBranchGroupTO != null){
            data.put("BranchGroupTO", objBranchGroupTO);
        }
        if(arrayListBranchGroupDetailsTO != null){
            data.put("BranchGroupDetailsTO",arrayListBranchGroupDetailsTO);
        }
        if(overAllList != null){
            data.put("OVERALLLIST",overAllList);
        }
        if(deletedOverAllList != null){
            data.put("DELETEDOVERALLLIST",deletedOverAllList);
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, map);
        setResult(actionType);
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
//    private ArrayList recordInsertingRow(int rowNo){
//        ArrayList obj = new ArrayList();
//        double startingAmt = 0.0;
//        if(rowNo == -1){
//            ArrayList data = tblInterestTable.getDataArrayList();
//            tblInterestTable.setDataArrayList(data,interestList);
//            AppraiserRateMaintenanceTO toList = appraiserRateMaintenanceTO();
//            ArrayList irRow = tableRecords();;
//            overAllList.add(tblInterestTable.getRowCount(),toList);
//            tblInterestTable.insertRow(tblInterestTable.getRowCount(),irRow);
//        }else{
//            ArrayList irRow = tableRecords();
//            AppraiserRateMaintenanceTO toList = appraiserRateMaintenanceTO();
//            overAllList.set(rowNo,toList);
//            tblInterestTable.removeRow(rowNo);
//            tblInterestTable.insertRow(rowNo,irRow);
//        }
//        tblInterestTable.fireTableDataChanged();
//        return obj;
//    }
    private AppraiserRateMaintenanceTO appraiserRateMaintenanceTO(){
        objTO = new AppraiserRateMaintenanceTO();
        objTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
        objTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
        objTO.setFromAmt(getTxtFromAmt());
        objTO.setToAmt(getTxtToAmt());
        objTO.setAmount(getTxtAmt());
        objTO.setServiceTax(getTxtServiceTax());
        objTO.setCreatedBy(TrueTransactMain.USER_ID);
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            objTO.setStatus(CommonConstants.STATUS_CREATED);
        }else if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
//        objTO.setCreatedDt(ClientUtil.getDateMMDDYYYY(currDt.clone()));
        if(getRdoAbsolute() == true){
            objTO.setPercentage("");
        }else if(getRdoPercentage() == true){
            objTO.setPercentage(getTxtPercentage());
        }
        return objTO;
    }
    private ArrayList tableRecords(){
        ArrayList row = new ArrayList();
        row.add(getTdtFromDate());
        row.add(getTdtToDate());
        row.add(getTxtFromAmt());
        row.add(getTxtToAmt());
        row.add(getTxtAmt());
        row.add(getTxtServiceTax());
        if(getRdoAbsolute() == true){
            row.add("");
        }else if(getRdoPercentage() == true){
            row.add(getTxtPercentage());
        }
        return row;
    }
    
    public void addEnteredValuesintoTable(int count){
//        recordInsertingRow(count);
        double startingAmt = 0.0;
        if(count == -1){
            ArrayList data = tblInterestTable.getDataArrayList();
            tblInterestTable.setDataArrayList(data,interestList);
            AppraiserRateMaintenanceTO toList = appraiserRateMaintenanceTO();
            ArrayList irRow = tableRecords();;
            overAllList.add(tblInterestTable.getRowCount(),toList);
            tblInterestTable.insertRow(tblInterestTable.getRowCount(),irRow);
        }else{
            ArrayList irRow = tableRecords();
            AppraiserRateMaintenanceTO toList = appraiserRateMaintenanceTO();
            overAllList.set(count,toList);
            tblInterestTable.removeRow(count);
            tblInterestTable.insertRow(count,irRow);
        }
        tblInterestTable.fireTableDataChanged();
    }
    
    public void populateSelectedRow(int count){
        ArrayList charges = (ArrayList)tblInterestTable.getDataArrayList().get(count);
        setTdtFromDate((String)charges.get(0));
        setTdtToDate((String)charges.get(1));
        setTxtFromAmt((String)charges.get(2));
        setTxtToAmt((String)charges.get(3));
        setTxtAmt((String)charges.get(4));
        setTxtServiceTax((String)charges.get(5));
        setTxtPercentage((String)charges.get(6));
        if(CommonUtil.convertObjToStr(getTxtPercentage()).equals("")){
            setRdoAbsolute(true);
            setRdoPercentage(false);
        }else{
            setRdoAbsolute(false);
            setRdoPercentage(true);
        }
        setChanged();
    }
    
    public void selectedRowDeleting(int count){
        deletedOverAllList.add(overAllList.get(count));
        overAllList.remove(count);
        tblInterestTable.removeRow(count);
        ArrayList data = tblInterestTable.getDataArrayList();
        tblInterestTable.setDataArrayList(data,interestList);
    }
    /**
     * Getter for property tdtFromDate.
     * @return Value of property tdtFromDate.
     */
    public java.lang.String getTdtFromDate() {
        return tdtFromDate;
    }
    
    /**
     * Setter for property tdtFromDate.
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.lang.String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    
    /**
     * Getter for property tdtToDate.
     * @return Value of property tdtToDate.
     */
    public java.lang.String getTdtToDate() {
        return tdtToDate;
    }
    
    /**
     * Setter for property tdtToDate.
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.lang.String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }
    
    /**
     * Getter for property txtFromAmt.
     * @return Value of property txtFromAmt.
     */
    public java.lang.String getTxtFromAmt() {
        return txtFromAmt;
    }
    
    /**
     * Setter for property txtFromAmt.
     * @param txtFromAmt New value of property txtFromAmt.
     */
    public void setTxtFromAmt(java.lang.String txtFromAmt) {
        this.txtFromAmt = txtFromAmt;
    }
    
    /**
     * Getter for property txtToAmt.
     * @return Value of property txtToAmt.
     */
    public java.lang.String getTxtToAmt() {
        return txtToAmt;
    }
    
    /**
     * Setter for property txtToAmt.
     * @param txtToAmt New value of property txtToAmt.
     */
    public void setTxtToAmt(java.lang.String txtToAmt) {
        this.txtToAmt = txtToAmt;
    }
    
    /**
     * Getter for property txtAmt.
     * @return Value of property txtAmt.
     */
    public java.lang.String getTxtAmt() {
        return txtAmt;
    }
    
    /**
     * Setter for property txtAmt.
     * @param txtAmt New value of property txtAmt.
     */
    public void setTxtAmt(java.lang.String txtAmt) {
        this.txtAmt = txtAmt;
    }
    
    /**
     * Getter for property txtMaxAmt.
     * @return Value of property txtMaxAmt.
     */
    public java.lang.String getTxtMaxAmt() {
        return txtMaxAmt;
    }
    
    /**
     * Setter for property txtMaxAmt.
     * @param txtMaxAmt New value of property txtMaxAmt.
     */
    public void setTxtMaxAmt(java.lang.String txtMaxAmt) {
        this.txtMaxAmt = txtMaxAmt;
    }
    
    /**
     * Getter for property txtPercentage.
     * @return Value of property txtPercentage.
     */
    public java.lang.String getTxtPercentage() {
        return txtPercentage;
    }
    
    /**
     * Setter for property txtPercentage.
     * @param txtPercentage New value of property txtPercentage.
     */
    public void setTxtPercentage(java.lang.String txtPercentage) {
        this.txtPercentage = txtPercentage;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property rdoAbsolute.
     * @return Value of property rdoAbsolute.
     */
    public boolean getRdoAbsolute() {
        return rdoAbsolute;
    }
    
    /**
     * Setter for property rdoAbsolute.
     * @param rdoAbsolute New value of property rdoAbsolute.
     */
    public void setRdoAbsolute(boolean rdoAbsolute) {
        this.rdoAbsolute = rdoAbsolute;
    }
    
    /**
     * Getter for property rdoPercentage.
     * @return Value of property rdoPercentage.
     */
    public boolean getRdoPercentage() {
        return rdoPercentage;
    }
    
    /**
     * Setter for property rdoPercentage.
     * @param rdoPercentage New value of property rdoPercentage.
     */
    public void setRdoPercentage(boolean rdoPercentage) {
        this.rdoPercentage = rdoPercentage;
    }
    
    /**
     * Getter for property tblInterestTable.
     * @return Value of property tblInterestTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInterestTable() {
        return tblInterestTable;
    }
    
    /**
     * Setter for property tblInterestTable.
     * @param tblInterestTable New value of property tblInterestTable.
     */
    public void setTblInterestTable(com.see.truetransact.clientutil.EnhancedTableModel tblInterestTable) {
        this.tblInterestTable = tblInterestTable;
    }
    
}