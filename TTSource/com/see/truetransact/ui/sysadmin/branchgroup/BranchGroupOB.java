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

package com.see.truetransact.ui.sysadmin.branchgroup;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;


import javax.swing.DefaultListModel;

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
import java.util.Date;
import com.see.truetransact.commonutil.CommonConstants;
/**
 *
 * @author  Ashok
 */

public class BranchGroupOB extends CObservable{
    private static BranchGroupOB branchGroupOB;
    private HashMap map, objHashMap;
    private ProxyFactory proxy;
    
    private String txtGroupId = "";
    private String txtGroupName = "";
    private String lstAvailBranch = "";
    private String lstGrantedBranch = "";
    
    private String lblStatus;
    private int actionType;
    private int result,_actionType;
    private ArrayList newData, deletedData,arrayListBranchGroupDetailsTO;
    
    private BranchGroupTO objBranchGroupTO;
    private BranchGroupDetailsTO objBranchGroupDetailsTO;
    private int i;
    
    private DefaultListModel lsmAvailableBranch = new DefaultListModel();
    private DefaultListModel lsmGrantedBranch = new DefaultListModel();;
    private static Date currDt = null;
    
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            branchGroupOB = new BranchGroupOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates new Instance of this class */
    private BranchGroupOB() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BranchGroupJNDI");
        map.put(CommonConstants.HOME, "generalledger.branchgroup.BranchGroupHome");
        map.put(CommonConstants.REMOTE,"generalledger.branchgroup.BranchGroup");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    /** Returns an instance of this Class */
    public static BranchGroupOB getInstance() {
        return branchGroupOB;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            ArrayList arrayBranchGLTO = null;
            mapData = proxy.executeQuery(whereMap, map);
            BranchGroupTO objBranchGroupTO = (BranchGroupTO)  ((ArrayList) (mapData.get("BranchGroupTO"))).get(0);
            System.out.println("BranchGroupTO "+ objBranchGroupTO);
            setTxtGroupId(objBranchGroupTO.getBranchGroupId());
            setTxtGroupName(objBranchGroupTO.getBranchGroupName());
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
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
    }
    
    /** Resets the List in UI to empty */
    private void resetList(){
        lsmGrantedBranch.clear();
        setLsmGrantedBranch(lsmGrantedBranch);
        lsmAvailableBranch.clear();
        setLsmAvailableBranch(lsmAvailableBranch);
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
                    final BranchGroupRB objBranchGroupRB = new BranchGroupRB();
                    throw new TTException(objBranchGroupRB.getString("TOCommandError"));
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
    
}