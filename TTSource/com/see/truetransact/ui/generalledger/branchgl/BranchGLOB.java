/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLOB.java
 *
 * Created on Mon Dec 27 17:07:03 IST 2004
 */

package com.see.truetransact.ui.generalledger.branchgl;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;


import javax.swing.DefaultListModel;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.generalledger.branchgl.BranchGLTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.generalledger.branchgl.BranchGLGroupTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Date;
import com.see.truetransact.commonutil.CommonConstants;
/**
 *
 * @author  Bala
 */

public class BranchGLOB extends CObservable{
    private static BranchGLOB branchGLOB;
    private HashMap map, objHashMap;
    private ProxyFactory proxy;
    
    private String txtGroupId = "";
    private String txtGroupName = "";
    private String lstAvailGL = "";
    private String lstGrantGL = "";
    
    private String lblStatus;
    private int actionType;
    private int result,_actionType;
    private ArrayList newData, deletedData,arrayListGLTO;
    
    private BranchGLTO objBranchGLTO;
    private BranchGLGroupTO objBranchGLGroupTO;
    private int i;
    
    private DefaultListModel lsmAvailableGLHead = new DefaultListModel();
    private DefaultListModel lsmGrantGLHead = new DefaultListModel();;
    
    
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static Date currDt = null;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            branchGLOB = new BranchGLOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates new Instance of this class */
    private BranchGLOB() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BranchGLJNDI");
        map.put(CommonConstants.HOME, "generalledger.branchgl.BranchGLHome");
        map.put(CommonConstants.REMOTE,"generalledger.branchgl.BranchGL");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    /** Returns an instance of this Class */
    public static BranchGLOB getInstance() {
        return branchGLOB;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            ArrayList arrayBranchGLTO = null;
            mapData = proxy.executeQuery(whereMap, map);
            BranchGLGroupTO objBranchGLGroupTO = (BranchGLGroupTO)  ((ArrayList) (mapData.get("BranchGLGroupTO"))).get(0);
            setTxtGroupId(objBranchGLGroupTO.getGroupId());
            setTxtGroupName(objBranchGLGroupTO.getGroupDesc());
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    
    /**Method used to filldown a List in the UI by seeting its model */
    public void fillAvailableAcHdIds() {
        HashMap acctHead = new HashMap();
        ArrayList lstData = (ArrayList) ClientUtil.executeQuery("getNewAcctHeadIDs", acctHead);
        lsmAvailableGLHead = new DefaultListModel();
        if (lstData.size() > 0) {
            for (int i =0, j=lstData.size(); i < j; i++) {
                lsmAvailableGLHead.addElement(((HashMap)lstData.get(i)).get("AC_HD_CODES"));
            }
        }
        
    }
    /**Method used to filldown a List in the UI by seeting its model */
    public void fillDropdown(String groupId) {
        HashMap acctHead = new HashMap();
        acctHead.put("GROUP_ID", groupId);
        
        ArrayList lstData = (ArrayList) ClientUtil.executeQuery("getAcctHeadIDs", acctHead);
        lsmAvailableGLHead = new DefaultListModel();
        if (lstData.size() > 0) {
            for (int i =0, j=lstData.size(); i < j; i++) {
                lsmAvailableGLHead.addElement(((HashMap)lstData.get(i)).get("AC_HD_CODES"));
            }
        }
        
        ArrayList lstGrantData = (ArrayList) ClientUtil.executeQuery("getSelectBranchGL", acctHead);
        lsmGrantGLHead = new DefaultListModel();
        if (lstGrantData.size() > 0) {
            for (int i =0, j=lstGrantData.size(); i < j; i++) {
                lsmGrantGLHead.addElement(((HashMap)lstGrantData.get(i)).get("AC_HD_ID"));
            }
        }
    }
    
    /** This method used to fillup an ArrayList which contains the daata which is newly filled up in a List IN UI*/
    public void newGLHeads(Object[] newGLHeads){
        if (newData == null)
            newData = new ArrayList();
        
        for(int i=0; i<newGLHeads.length;i++){
            newData.add(i,newGLHeads[i]);
        }
    }
    
    /** This method is used to fill up an arraylist which contains data removed from the List in the UI */
    public void removedGLHeads(Object[] removedGLHeads){
        if (deletedData == null)
            deletedData = new ArrayList();
        
        for(int i=0; i<removedGLHeads.length;i++){
            deletedData.add(i,removedGLHeads[i]);
        }
    }
    
    /** Getter for lsmAvailableGLHead */
    public DefaultListModel getLsmAvailableGLHead() {
        return lsmAvailableGLHead;
    }
    
    /** Setter for lsmAvailableGLHead */
    public void setLsmAvailableGLHead(DefaultListModel lsmAvailableGLHead) {
        this.lsmAvailableGLHead = lsmAvailableGLHead;
        setChanged();
    }
    
    /** Getter for lsmGrantGLHead */
    public DefaultListModel getLsmGrantGLHead() {
        return lsmGrantGLHead;
    }
    /** Setter for lsmGrantGLHead */
    public void setLsmGrantGLHead(DefaultListModel lsmGrantGLHead) {
        this.lsmGrantGLHead = lsmGrantGLHead;
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
    
    // Setter method for txtBranchID
    void setTxtGroupId(String txtGroupId){
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtBranchID
    String getTxtGroupId(){
        return this.txtGroupId;
    }
    
    // Setter method for txtBranchName
    void setTxtGroupName(String txtGroupName){
        this.txtGroupName = txtGroupName;
        setChanged();
    }
    // Getter method for txtBranchName
    String getTxtGroupName(){
        return this.txtGroupName;
    }
    
    // Setter method for lstAvailGL
    void setLstAvailGL(String lstAvailGL){
        this.lstAvailGL = lstAvailGL;
        setChanged();
    }
    // Getter method for lstAvailGL
    String getLstAvailGL(){
        return this.lstAvailGL;
    }
    
    // Setter method for lstGrantGL
    void setLstGrantGL(String lstGrantGL){
        this.lstGrantGL = lstGrantGL;
        setChanged();
    }
    // Getter method for lstGrantGL
    String getLstGrantGL(){
        return this.lstGrantGL;
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** Sets the to object */
    public void setBranchGLTO(BranchGLTO objBranchGLTO){
        setTxtGroupId(objBranchGLTO.getGroupId());
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
        lsmGrantGLHead.clear();
        setLsmGrantGLHead(lsmGrantGLHead);
        lsmAvailableGLHead.clear();
        setLsmAvailableGLHead(lsmAvailableGLHead);
    }
    
    /** Method which do appropriate operation either Insertion or updation or deletion Based on The Action Type */
    public void doSave()throws Exception{
        objBranchGLGroupTO = getBranchGLGroupTO();
        arrayListGLTO = new ArrayList();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            newData();
            deletedData();
        }else{
            DefaultListModel lsmGrant = getLsmGrantGLHead();
            for(i=0; i<lsmGrant.size();i++){
                objBranchGLTO = new BranchGLTO();
                if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    objBranchGLTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objBranchGLTO.setGroupId(getTxtGroupId());
                    objBranchGLTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                String element = CommonUtil.convertObjToStr(lsmGrant.get(i));
                objBranchGLTO.setAcHdId(element.substring(0,element.indexOf("(")).trim());
                objBranchGLTO.setCommand(getCommand());
                arrayListGLTO.add(i,objBranchGLTO);
            }
        }
        doAction();
        deinitialise();
    }
    
    /* Clear up the Objects used */
    private void deinitialise(){
        newData = null;
        deletedData = null;
        arrayListGLTO = null;
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
                    final BranchGLRB objBranchGLRB = new BranchGLRB();
                    throw new TTException(objBranchGLRB.getString("TOCommandError"));
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
                setGLTO(newData);
            }
        }
    }
    
    /** Adds up the Excluded Data in a ArrayList */
    private void  deletedData()throws Exception{
        if(deletedData != null){
            setActionType(ClientConstants.ACTIONTYPE_DELETE);
            if(deletedData.size() > 0){
                setGLTO(deletedData);
            }
        }
    }
    
    /* Sets the BranchGLTO variables */
    private void setGLTO(ArrayList data)throws Exception{
        for(i=0; i<data.size(); i++){
            objBranchGLTO = new BranchGLTO();
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
                objBranchGLTO.setStatus(CommonConstants.STATUS_CREATED);
            }else{
                objBranchGLTO.setGroupId(getTxtGroupId());
                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    objBranchGLTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objBranchGLTO.setStatus(CommonConstants.STATUS_DELETED);
                }
            }
            String element = CommonUtil.convertObjToStr(data.get(i));
            objBranchGLTO.setAcHdId(element.substring(0,element.indexOf("(")).trim());
            objBranchGLTO.setCommand(getCommand());
            arrayListGLTO.add(objBranchGLTO);
            objBranchGLTO = null;
        }
    }
    
    private BranchGLGroupTO getBranchGLGroupTO()throws Exception{
        BranchGLGroupTO objGroupTO = new BranchGLGroupTO();
        objGroupTO.setGroupId(getTxtGroupId());
        objGroupTO.setGroupDesc(getTxtGroupName());
        objGroupTO.setCommand(getCommand());
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            objGroupTO.setStatus(CommonConstants.STATUS_CREATED);
        }else{
            objGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }
        objGroupTO.setStatusBy(TrueTransactMain.USER_ID);
        objGroupTO.setStatusDt(currDt);
        return objGroupTO;
    }
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(objBranchGLGroupTO != null){
            data.put("BranchGLGroupTO", objBranchGLGroupTO);
        }
        if(arrayListGLTO != null){
            data.put("BranchGLTO",arrayListGLTO);
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