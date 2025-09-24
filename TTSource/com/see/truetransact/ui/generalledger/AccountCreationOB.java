/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountCreationOB.java
 *
 * Created on August 13, 2003, 10:03 AM
 */

package com.see.truetransact.ui.generalledger;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.generalledger.AccountCreationTO;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Logger;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author  Annamalai
 */

public class AccountCreationOB extends CObservable {
    private ComboBoxModel _cbmAccountType;
    private ComboBoxModel _cbmMajorHead;
    private ComboBoxModel _cbmSubHead;
    private String _cboAccountType = "";
    private String _cboMajorHead = "";
    private String _cboSubHead = "";
    private String _txtAccountHead = "";
    private String _txtAccountHeadCode = "";
    private String _txtAccountHeadDesc = "";
    private String _lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String rdoReceiveDayBookDetail = "";
    private String rdoPayDayBookDetail = "";
    private String txtAccountHeadOrder = "";
    private HashMap _operationMap;
    private ProxyFactory _proxy;
    
    private HashMap _lookUpHash;
    private HashMap _keyValue;
    private ArrayList _key;
    private ArrayList _value;
    
    private int _actionType;
    private int _result;
    private final String STARTING_NUMBER = "001";
    
//    private final AccountCreationRB objAccountCreationRB = new AccountCreationRB();
    java.util.ResourceBundle objAccountCreationRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.AccountCreationRB", ProxyParameters.LANGUAGE);
    
    private static AccountCreationOB _accountCreationOB; // singleton object
    private final static Logger _log = Logger.getLogger(AccountCreationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            _log.info("Creating AccountCreationOB...");
            _accountCreationOB = new AccountCreationOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of AccountCreationOB */
    private AccountCreationOB() throws Exception{
        _proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        _cbmMajorHead = new ComboBoxModel();
        _cbmSubHead = new ComboBoxModel();
        makeNull();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        _operationMap = new HashMap();
        _operationMap.put(CommonConstants.JNDI, "AccountCreationJNDI");
        _operationMap.put(CommonConstants.HOME, "generalledger.AcctCreationHome");
        _operationMap.put(CommonConstants.REMOTE, "generalledger.AcctCreation");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        _key = (ArrayList)keyValue.get(CommonConstants.KEY);
        _value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void fillDropdown() throws Exception{
        _lookUpHash = new HashMap();
        _lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("ACCOUNTTYPE");
        _lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        _keyValue = ClientUtil.populateLookupData(_lookUpHash);
        getKeyValue((HashMap)_keyValue.get("ACCOUNTTYPE"));
        _cbmAccountType = new ComboBoxModel(_key,_value);
    }
    
    /**
     * Returns an instance of AccountCreationOB.
     * @return  AccountCreationOB
     */
    public static AccountCreationOB getInstance() {
        return _accountCreationOB;
    }
    
    /* To set combo box with the major heads*/
    public void populateMajorHead(String accountType){
        try{
            _lookUpHash = new HashMap();
            _lookUpHash.put(CommonConstants.MAP_NAME,"getMajorHead");
            _lookUpHash.put(CommonConstants.PARAMFORQUERY, accountType);
            _keyValue = ClientUtil.populateLookupData(_lookUpHash);
            getKeyValue((HashMap)_keyValue.get(CommonConstants.DATA));
            _cbmMajorHead = new ComboBoxModel(_key,_value);
            setCbmMajorHead(_cbmMajorHead);
            makeNull();
        }catch(Exception e){
            //e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    /* To set combo box with the sub heads corresponding to the major head given*/
    public void populateSubHead(String majorHead){
        try{
            _lookUpHash = new HashMap();
            _lookUpHash.put(CommonConstants.MAP_NAME,"getSubHead");
            _lookUpHash.put(CommonConstants.PARAMFORQUERY, majorHead);
            _keyValue = ClientUtil.populateLookupData(_lookUpHash);
            getKeyValue((HashMap)_keyValue.get(CommonConstants.DATA));
            _cbmSubHead = new ComboBoxModel(_key,_value);
            setCbmSubHead(_cbmSubHead);
            makeNull();
        }catch(Exception e){
            //e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        try {
            HashMap mapData = _proxy.executeQuery(whereMap, _operationMap);
            _log.info("mapData:"+mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    /* To make the class variables null*/
    private void makeNull(){
        _key = null;
        _value = null;
        _lookUpHash = null;
        _keyValue = null;
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        HashMap accountCreationMap = (HashMap) ((List) mapData.get("AccountCreationDetails")).get(0);
        System.out.println("accountCreationMap:"+accountCreationMap);
        populateMajorHead((String)accountCreationMap.get("MJR_AC_HD_TYPE"));
        populateSubHead((String)accountCreationMap.get("MJR_AC_HD_ID"));
        setCboAccountType(CommonUtil.convertObjToStr(getCbmAccountType().getDataForKey(accountCreationMap.get("MJR_AC_HD_TYPE"))));
        setCboMajorHead(CommonUtil.convertObjToStr(getCbmMajorHead().getDataForKey(accountCreationMap.get("MJR_AC_HD_ID"))));
        setCboSubHead(CommonUtil.convertObjToStr(getCbmSubHead().getDataForKey(accountCreationMap.get("SUB_AC_HD_ID"))));
        setTxtAccountHeadCode(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_CODE")));
        setTxtAccountHeadDesc(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_DESC")));
        setTxtAccountHead(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ID")));
        setRdoReceiveDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("REC_DETAILED_IN_DAYBOOK")));
        setRdoPayDayBookDetail(CommonUtil.convertObjToStr(accountCreationMap.get("PAY_DETAILED_IN_DAYBOOK")));
        setTxtAccountHeadOrder(CommonUtil.convertObjToStr(accountCreationMap.get("AC_HD_ORDER")));
        ttNotifyObservers();
    }
    
    public void setCbmAccountType(ComboBoxModel cbmAccountType){
        _cbmAccountType = cbmAccountType;
        setChanged();
    }
    
    ComboBoxModel getCbmAccountType(){
        return _cbmAccountType;
    }
    
    public void setCboAccountType(String cboAccountType){
        _cboAccountType = cboAccountType;
        setChanged();
    }
    String getCboAccountType(){
        return _cboAccountType;
    }
    
    public void setCbmMajorHead(ComboBoxModel cbmMajorHead){
        _cbmMajorHead = cbmMajorHead;
        setChanged();
        
    }
    ComboBoxModel getCbmMajorHead(){
        return _cbmMajorHead;
    }
    
    public void setCboMajorHead(String cboMajorHead){
        _cboMajorHead = cboMajorHead;
        setChanged();
    }
    String getCboMajorHead(){
        return _cboMajorHead;
    }
    
    public void setCbmSubHead(ComboBoxModel cbmSubHead){
        _cbmSubHead = cbmSubHead;
        setChanged();
        
    }
    ComboBoxModel getCbmSubHead(){
        return _cbmSubHead;
    }
    
    public void setCboSubHead(String cboSubHead){
        _cboSubHead = cboSubHead;
        setChanged();
    }
    String getCboSubHead(){
        return _cboSubHead;
    }
    
    public void setTxtAccountHead(String txtAccountHead){
        _txtAccountHead = txtAccountHead;
        setChanged();
    }
    String getTxtAccountHead(){
        return _txtAccountHead;
    }
    
    public void setTxtAccountHeadCode(String txtAccountHeadCode){
        _txtAccountHeadCode = txtAccountHeadCode;
        setChanged();
    }
    String getTxtAccountHeadCode(){
        return _txtAccountHeadCode;
    }
    
    public void setTxtAccountHeadDesc(String txtAccountHeadDesc){
        _txtAccountHeadDesc = txtAccountHeadDesc;
        setChanged();
    }
    String getTxtAccountHeadDesc(){
        return _txtAccountHeadDesc;
    }
    
    public String getLblStatus(){
        return _lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        _lblStatus = lblStatus;
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return _result;
    }
    
    public void setResult(int result) {
        _result = result;
        setChanged();
    }
    
    /* To set common data in the Transfer Object*/
    public AccountCreationTO setAcctCreationData() {
        final AccountCreationTO objAccountCreationTO = new AccountCreationTO();
        try{
            objAccountCreationTO.setMjrAcHdId((String)_cbmMajorHead.getKeyForSelected());
            objAccountCreationTO.setSubAcHdId((String)_cbmSubHead.getKeyForSelected());
            objAccountCreationTO.setAcHdCode(_txtAccountHeadCode);
            objAccountCreationTO.setAcHdDesc(_txtAccountHeadDesc);
            objAccountCreationTO.setAcHdId(_txtAccountHead);
            objAccountCreationTO.setRecDayBook(getRdoReceiveDayBookDetail());
            objAccountCreationTO.setPayDayBook(getRdoPayDayBookDetail());
            objAccountCreationTO.setAcHeadOrder(CommonUtil.convertObjToDouble(getTxtAccountHeadOrder()));
        }catch(Exception e){
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objAccountCreationTO;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            _log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }else{
                    throw new TTException(objAccountCreationRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final AccountCreationTO objAccountCreationTO = setAcctCreationData();
        objAccountCreationTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put("ACTCREATION",objAccountCreationTO);
        _log.info("objAccountCreationTO:"+objAccountCreationTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = _proxy.execute(data, _operationMap);
        
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    
    private String getCommand() throws Exception{
        String command = null;
        switch (_actionType) {
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void resetForm(){
        setCboAccountType("");
        setCboMajorHead("");
        setCboSubHead("");
        setTxtAccountHead("");
        setTxtAccountHeadCode("");
        setTxtAccountHeadDesc("");
        setRdoReceiveDayBookDetail("");
        setRdoPayDayBookDetail("");
        setTxtAccountHeadOrder("");
        ttNotifyObservers();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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
     * Method to get the last 3 digits of Account Head Code
     */
    public String getMaxAcHdCode(){
        String maxAcHdCode = null;
        try{
            final HashMap acHdMap = new HashMap();
            acHdMap.put("AC_HD_CODE", (String)getCbmMajorHead().getKeyForSelected()+getCbmSubHead().getKeyForSelected());
            //final String acHdCode = CommonUtil.convertObjToStr(((HashMap)ClientUtil.executeQuery("getMaxAcHdCode", acHdMap).get(0)).get("MAX(AC_HD_CODE)"));
            final String acHdCode = CommonUtil.convertObjToStr(((HashMap)ClientUtil.executeQuery("getMaxAcHdCode", acHdMap).get(0)).get("AC_HD_CODE"));
            if (acHdCode.length() == 0 ){
                maxAcHdCode = STARTING_NUMBER;
            }else{
                // If the data is wrong i.e the major Head and Sub Head are not of specified length, error can occur in the following line of code
                int codeVal = 0;
                codeVal = CommonUtil.convertObjToInt(acHdCode.substring(7));
                maxAcHdCode = CommonUtil.lpad(String.valueOf(codeVal+1),3,'0');
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return maxAcHdCode;
    }
    
    public DefaultTreeModel getAcHdTree(){        
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(objAccountCreationRB.getString("treeHeading"));
        DefaultMutableTreeNode accTypeNode = null;
        DefaultMutableTreeNode mjrHdNode = null;
        DefaultMutableTreeNode subHdNode = null;
        DefaultMutableTreeNode acHdNode = null;
        
        final List objList = ClientUtil.executeQuery("acHdCreation.acHdTree", null);        
        final int objListSize = objList.size();
        
        for (int i = 0;i<objListSize;i++){
            accTypeNode = addNode(root,accTypeNode,((HashMap)objList.get(i)).get("MJR_AC_HD_TYPE"));
            mjrHdNode = addNode(accTypeNode,mjrHdNode,((HashMap)objList.get(i)).get("MJR_AC_HD"));
            subHdNode = addNode(mjrHdNode,subHdNode,((HashMap)objList.get(i)).get("SUB_AC_HD"));
            acHdNode = addNode(subHdNode,acHdNode,((HashMap)objList.get(i)).get("AC_HD_ID"));
        }
        final DefaultTreeModel treemodel = new DefaultTreeModel(root);
        
        root = null;
        accTypeNode = null;
        mjrHdNode = null;
        subHdNode = null;
        acHdNode = null;  
        
        return treemodel;
    }
    
    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child,Object obj){ 
        if (!chkExistance(parent,obj.toString())){
            child = new DefaultMutableTreeNode(obj);
        }            
        if (!parent.isNodeChild(child)){
            parent.add(child);
        }      
        return child;
    }
    
    private boolean chkExistance(DefaultMutableTreeNode parent,String chkStr){
        final Enumeration objEnumeration = parent.children();
        while (objEnumeration.hasMoreElements()){
            if (objEnumeration.nextElement().toString().equals(chkStr)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Getter for property rdoReceiveDayBookDetail.
     * @return Value of property rdoReceiveDayBookDetail.
     */
    public java.lang.String getRdoReceiveDayBookDetail() {
        return rdoReceiveDayBookDetail;
    }
    
    /**
     * Setter for property rdoReceiveDayBookDetail.
     * @param rdoReceiveDayBookDetail New value of property rdoReceiveDayBookDetail.
     */
    public void setRdoReceiveDayBookDetail(java.lang.String rdoReceiveDayBookDetail) {
        this.rdoReceiveDayBookDetail = rdoReceiveDayBookDetail;
    }
    
    /**
     * Getter for property rdoPayDayBookDetail.
     * @return Value of property rdoPayDayBookDetail.
     */
    public java.lang.String getRdoPayDayBookDetail() {
        return rdoPayDayBookDetail;
    }
    
    /**
     * Setter for property rdoPayDayBookDetail.
     * @param rdoPayDayBookDetail New value of property rdoPayDayBookDetail.
     */
    public void setRdoPayDayBookDetail(java.lang.String rdoPayDayBookDetail) {
        this.rdoPayDayBookDetail = rdoPayDayBookDetail;
    }
    
    /**
     * Getter for property txtAccountHeadOrder.
     * @return Value of property txtAccountHeadOrder.
     */
    public java.lang.String getTxtAccountHeadOrder() {
        return txtAccountHeadOrder;
    }
    
    /**
     * Setter for property txtAccountHeadOrder.
     * @param txtAccountHeadOrder New value of property txtAccountHeadOrder.
     */
    public void setTxtAccountHeadOrder(java.lang.String txtAccountHeadOrder) {
        this.txtAccountHeadOrder = txtAccountHeadOrder;
    }
    
}
