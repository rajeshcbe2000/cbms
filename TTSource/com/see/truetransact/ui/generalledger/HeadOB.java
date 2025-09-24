/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadOB.java
 *
 * Created on August 13, 2003, 2:52 PM
 */

package com.see.truetransact.ui.generalledger;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.uicomponent.CObservable;

import com.see.truetransact.clientutil.EnhancedTableModel;

import com.see.truetransact.transferobject.generalledger.HeadTO;
import com.see.truetransact.transferobject.generalledger.SubHeadTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.CMandatoryDialog;

import org.apache.log4j.Logger;

/**
 *
 * @author  Annamalai
 */
public class HeadOB extends CObservable {
    
    private ComboBoxModel _cbmAccountType;
    private ComboBoxModel _cbmFinalActType;
    private ComboBoxModel _cbmSubAccountType; 
    private String _cboAccountType = "";
    private String _cboFinalActType="";
    private String _cboSubAccountType="";
    private String _txtMajorHeadCode1 = "";
    private String _txtMajorHeadCode2 = "";
    private String _txtMajorHeadDesc = "";
    private String _txtSubHeadCode = "";
    private String _txtSubHeadDesc = "";
    private boolean chkGLHeadConsolidated = false;
    private EnhancedTableModel _tblSubHeadList;
    private String _lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    String authStatus = "";
    
    private int _actionType;
    private int _result;
    
    SubHeadTO _subHeadTO;
    ArrayList _subHeadRow;
    
    HashMap _operationMap;
    final ArrayList _subHeadTitle = new ArrayList();
    ProxyFactory _proxy;
    HeadTO _headTO;
    final HeadRB _objHeadRB = new HeadRB();
    
    private ArrayList _key;
    private ArrayList _value;
    
    private static HeadOB _objHeadOB; // singleton object
    
    private final static Logger _log = Logger.getLogger(HeadOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            _log.info("Creating HeadOB...");
            _objHeadOB = new HeadOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of HeadOB */
    private HeadOB() throws Exception{
        _proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setSubHeadTitle();
        _tblSubHeadList = new EnhancedTableModel(null, _subHeadTitle);
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        _operationMap = new HashMap();
        _operationMap.put(CommonConstants.JNDI, "HeadJNDI");
        _operationMap.put(CommonConstants.HOME, "generalledger.HeadHome");
        _operationMap.put(CommonConstants.REMOTE, "generalledger.Head");
    }
    
    /* Sets _subHeadTitle with table column headers */
    private void setSubHeadTitle() throws Exception{
        _subHeadTitle.add(_objHeadRB.getString("tblColumn1"));
        _subHeadTitle.add(_objHeadRB.getString("tblColumn2"));
    }
    
    /*To set data for all dropdowns*/
    private void fillDropdown(){
        final HashMap lookUpHash = new HashMap();
        final HashMap keyValue;
        
        lookUpHash.put("mapname",null);
        
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("ACCOUNTTYPE");
        lookup_keys.add("GL_FINAL_ACC_TYPE");
        lookup_keys.add("GL_SUB_ACC_TYPE");
        
        lookUpHash.put("paramforquery", lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("ACCOUNTTYPE"));
        
        _cbmAccountType = new ComboBoxModel(_key,_value);
        
         getKeyValue((HashMap)keyValue.get("GL_FINAL_ACC_TYPE"));
        _cbmFinalActType = new ComboBoxModel(_key,_value);
        
         getKeyValue((HashMap)keyValue.get("GL_SUB_ACC_TYPE"));
        _cbmSubAccountType = new ComboBoxModel(_key,_value);
        makeNull();
    }
    
    /* To make the class variables null*/
    private void makeNull(){
        _key = null;
        _value = null;
    }
    
    /* Splits the keyValue HashMap into _key and _value arraylists*/
    private void getKeyValue(HashMap keyValue){
        _key = (ArrayList)keyValue.get(CommonConstants.KEY);
        _value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Returns an instance of HeadOB.
     * @return  HeadOB
     */
    public static HeadOB getInstance() {
        return _objHeadOB;
    }
    
    void setCbmAccountType(ComboBoxModel cbmAccountType){
        this._cbmAccountType = cbmAccountType;
        setChanged();
    }
    
    ComboBoxModel getCbmAccountType(){
        return this._cbmAccountType;
    }
    
    void setCboAccountType(String cboAccountType){
        this._cboAccountType = cboAccountType;
        setChanged();
    }
    String getCboAccountType(){
        return this._cboAccountType;
    }
    
    void setTxtMajorHeadCode1(String txtMajorHeadCode1){
        this._txtMajorHeadCode1 = txtMajorHeadCode1;
        setChanged();
    }
    String getTxtMajorHeadCode1(){
        return this._txtMajorHeadCode1;
    }
    
    void setTxtMajorHeadCode2(String txtMajorHeadCode2){
        this._txtMajorHeadCode2 = txtMajorHeadCode2;
        setChanged();
    }
    String getTxtMajorHeadCode2(){
        return this._txtMajorHeadCode2;
    }
    
    void setTxtMajorHeadDesc(String txtMajorHeadDesc){
        this._txtMajorHeadDesc = CommonUtil.convertObjToStr(txtMajorHeadDesc).toUpperCase();
        setChanged();
    }
    String getTxtMajorHeadDesc(){
        return this._txtMajorHeadDesc;
    }
    
    void setTxtSubHeadCode(String txtSubHeadCode){
        this._txtSubHeadCode = txtSubHeadCode;
        setChanged();
    }
    String getTxtSubHeadCode(){
        return this._txtSubHeadCode;
    }
    
    void setTxtSubHeadDesc(String txtSubHeadDesc){
        this._txtSubHeadDesc = CommonUtil.convertObjToStr(txtSubHeadDesc).toUpperCase();
        setChanged();
    }
    String getTxtSubHeadDesc(){
        return this._txtSubHeadDesc;
    }
    
    void setTblSubHeadList(EnhancedTableModel tblSubHeadList){
        this._tblSubHeadList = tblSubHeadList;
        setChanged();
    }
    EnhancedTableModel getTblSubHeadList(){
        return this._tblSubHeadList;
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

    public String getCboFinalActType() {
        return _cboFinalActType;
    }

    public void setCboFinalActType(String _cboFinalActType) {
        this._cboFinalActType = _cboFinalActType;
    }

    public String getCboSubAccountType() {
        return _cboSubAccountType;
    }

    public void setCboSubAccountType(String _cboSubAccountType) {
        this._cboSubAccountType = _cboSubAccountType;
    }

   
    
    /* To populate the screen */
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = (HashMap) _proxy.executeQuery(whereMap, _operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /*Major Head Data are set in TO object*/
    public void setMajorHeadData() {
        if(_headTO == null){
            _headTO = new HeadTO();
        }
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW){
            _headTO.setMjrAcHdType(CommonUtil.convertObjToStr(_cbmAccountType.getKeyForSelected()));
           // _headTO.setFinalActType(_cboFinalActType);
          //  _headTO.setSubActType(_cboSubAccountType);
          
        }
        _headTO.setFinalActType(CommonUtil.convertObjToStr(_cbmFinalActType.getKeyForSelected()));
        _headTO.setSubActType(CommonUtil.convertObjToStr(_cbmSubAccountType.getKeyForSelected()));
        _headTO.setMjrAcHdId(CommonUtil.convertObjToInt(_txtMajorHeadCode1+_txtMajorHeadCode2));
        _headTO.setMjrAcHdDesc(_txtMajorHeadDesc);
       
        //Added By Suresh
        if(getChkGLHeadConsolidated() == true){
            _headTO.setGlConsolidated("Y");
        }else{
            _headTO.setGlConsolidated("N");
        }
    }
    
    /* To insert or update Sub Head data in Table and HeadTO object*/
    public int addSubHeadList(boolean subHeadNew){
        int option = -1;
        try{
            _subHeadRow = new ArrayList();
            _subHeadRow.add(_txtSubHeadCode);
            _subHeadRow.add(_txtSubHeadDesc);
            ArrayList data = _tblSubHeadList.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            _log.info("data:"+data);
            _log.info("_txtSubHeadCode:"+_txtSubHeadCode);
            for (int i=0;i<dataSize;i++){
                if ((((ArrayList)data.get(i)).get(0)).equals(_txtSubHeadCode)){
                    // Checking whether _txtSubHeadCode is equal to (i) row's SubHead Code
                    exist = true;
                    if (subHeadNew){
                        //Checks the condition whether subHeadCode is entered in the Textbox by typing
                        String[] options = {_objHeadRB.getString("cDialogYes"),_objHeadRB.getString("cDialogNo"),_objHeadRB.getString("cDialogCancel")};
                        option = COptionPane.showOptionDialog(null, _objHeadRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                        if (option == 0){
                            // option selected is Yes
                            updateSubHeadDesc(i);
                        }
                    }else{
                        updateSubHeadDesc(i);
                    }
                    break;
                }
            }
            if (!exist){
                //The condition that the SubHead Code is not in the table
                insertSubHeadDesc();
            }
            _subHeadRow = null;
            
            if (option != 2){
                //The option selected is Cancel
                resetSubHeadDetails();
            }
            ttNotifyObservers();
        }catch(Exception e){
            //_log.error(e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    /* To add a SubHeadTO object in HeadTO object*/
    private void insertSubHeadDesc() throws Exception{
        _subHeadTO = new SubHeadTO();
        if(_headTO == null){
            _headTO = new HeadTO();
        }
        
        _tblSubHeadList.insertRow(0,_subHeadRow);
        setSubHeadData();
        _headTO.addSubHead(_subHeadTO,CommonConstants.TOSTATUS_INSERT);
        _subHeadTO = null;
    }
    
    /* To update a SubHeadTO object in HeadTO object*/
    private void updateSubHeadDesc(int row) throws Exception{
        _subHeadTO = new SubHeadTO();
        if(_headTO == null){
            _headTO = new HeadTO();
        }
        _tblSubHeadList.setValueAt(_txtSubHeadDesc, row, 1);
        
        setSubHeadData();
        _headTO.addSubHead(_subHeadTO,CommonConstants.TOSTATUS_UPDATE);
        _subHeadTO = null;
    }
    
    /* To change the status of SubHeadTO object to DELETE in HeadTO object */
    public void deleteSubHeadDesc(){
        System.out.println("deleteSubHeadDesc()");
        
        try{
            _subHeadTO = new SubHeadTO();
            if(_headTO == null){
                _headTO = new HeadTO();
            }
            
            final ArrayList data = _tblSubHeadList.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ((((ArrayList)data.get(i)).get(0)).equals(_txtSubHeadCode)){                    
                    _tblSubHeadList.removeRow(i);
                    setSubHeadData();
                    _headTO.addSubHead(_subHeadTO,CommonConstants.TOSTATUS_DELETE);
                    break;
                }
            }
        }catch(Exception e){
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    private void setSubHeadData() throws Exception{
//        _subHeadTO.setMjrAcHdId(_txtMajorHeadCode1+_txtMajorHeadCode2);
        _subHeadTO.setMjrAcHdId(_txtMajorHeadCode1+ CommonUtil.lpad(_txtMajorHeadCode2, 3, '0'));
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            _txtSubHeadCode = CommonUtil.lpad(_txtSubHeadCode, 3, '0');
        }
        _subHeadTO.setSubAcHdId(_txtSubHeadCode);
        _subHeadTO.setSubAcHdDesc(_txtSubHeadDesc);
        
        System.out.println("MjrAcHdId: " + _subHeadTO.getMjrAcHdId());
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void resetSubHeadDetails(){
        setTxtSubHeadCode("");
        setTxtSubHeadDesc("");
    }
    
    /*Get the data required for the Screen */
    private void populateOB(HashMap mapData) {
        HeadTO objHeadTO = null;
        SubHeadTO objSubHeadTO = null;
        List allSubHeadList = null;
        ArrayList subHeadList = new ArrayList();
        ArrayList subHead = null;
        
        objHeadTO = (HeadTO) ((List) mapData.get("MajorHeadTO")).get(0);
        setMajorHeadTO(objHeadTO);
        allSubHeadList = (List) mapData.get("SubHeadTO");
        final int allSubHeadListSize = allSubHeadList.size();
        for (int i = 0;i<allSubHeadListSize;i++){
            objSubHeadTO = (SubHeadTO) ((List) mapData.get("SubHeadTO")).get(i);
            subHead = new ArrayList();
            subHead.add(objSubHeadTO.getSubAcHdId());
            subHead.add(objSubHeadTO.getSubAcHdDesc());
            subHeadList.add(subHead);
            subHead = null;
            objSubHeadTO = null;
        }
        _tblSubHeadList.setDataArrayList(subHeadList,_subHeadTitle);
        setTblSubHeadList(_tblSubHeadList);
        subHeadList = null;
    }
    
    private void setMajorHeadTO(HeadTO objHeadTO) {
        setCboAccountType(CommonUtil.convertObjToStr(getCbmAccountType().getDataForKey(objHeadTO.getMjrAcHdType())));
        String mjrHd = CommonUtil.convertObjToStr(objHeadTO.getMjrAcHdId());
        // Next two lines are for seperating the Major Head Code into two parts
        // The first part indicates the Account Type
       
        
        setTxtMajorHeadCode1(mjrHd.substring(0,1));
        setTxtMajorHeadCode2(mjrHd.substring(1));
        setTxtMajorHeadDesc(CommonUtil.convertObjToStr(objHeadTO.getMjrAcHdDesc()));
      //  setCboFinalActType(CommonUtil.convertObjToStr(objHeadTO.getFinalActType()));
     //  setCboSubAccountType(CommonUtil.convertObjToStr(objHeadTO.getSubActType()));
         setCboFinalActType(CommonUtil.convertObjToStr(getCbmFinalActType().getDataForKey(objHeadTO.getFinalActType())));
         setCboSubAccountType(CommonUtil.convertObjToStr(getCbmSubAccountType().getDataForKey(objHeadTO.getSubActType())));
        if(objHeadTO.getGlConsolidated()!=null && objHeadTO.getGlConsolidated().equals("Y")){
            this.setChkGLHeadConsolidated(true);
        }else{
            this.setChkGLHeadConsolidated(false);
        }
        setAuthStatus(CommonUtil.convertObjToStr(objHeadTO.getAuthorizeStatus()));
        
        ttNotifyObservers();
    }
    
    /* Set the Sub Head data in OB variables*/
    public void populateSubHead(int row){
        ArrayList selectedSubHead = (ArrayList)_tblSubHeadList.getDataArrayList().get(row);
        setTxtSubHeadCode((String)selectedSubHead.get(0));
        setTxtSubHeadDesc((String)selectedSubHead.get(1));
        ttNotifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            //If actionType such as NEW, EDIT, DELETE , then proceed
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final AccountCreationRB objAccountCreationRB = new AccountCreationRB();
                    throw new TTException(objAccountCreationRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        setMajorHeadData();
        final HeadTO objHeadTO = _headTO;
        objHeadTO.setCommand(getCommand());
        final HashMap data = new HashMap();
        data.put("HEAD",objHeadTO);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        _log.info("objHeadTO:"+objHeadTO);
        HashMap proxyResultMap = _proxy.execute(data, _operationMap);
        
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
    }
    
    private String getCommand(){
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
    
    public void resetForm() {
        setCboAccountType("");
        setCboFinalActType("");
        setCboSubAccountType("");
        setTxtMajorHeadCode1("");
        setTxtMajorHeadCode2("");
        setTxtMajorHeadDesc("");
        setChkGLHeadConsolidated(false);
        final int tblSubHeadListSize = _tblSubHeadList.getRowCount();
        for (int i = tblSubHeadListSize;i>0;i--){
            _tblSubHeadList.removeRow(i-1);
        }
        resetSubHeadDetails();
        _headTO = new HeadTO();
        notifyObservers();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
   
    public String getAuthStatus() {
        return authStatus;
    }
    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
    
    /**
     * Getter for property chkGLHeadConsolidated.
     * @return Value of property chkGLHeadConsolidated.
     */
    public boolean getChkGLHeadConsolidated() {
        return chkGLHeadConsolidated;
    }
    
    /**
     * Setter for property chkGLHeadConsolidated.
     * @param chkGLHeadConsolidated New value of property chkGLHeadConsolidated.
     */
    public void setChkGLHeadConsolidated(boolean chkGLHeadConsolidated) {
        this.chkGLHeadConsolidated = chkGLHeadConsolidated;
    }

    public ComboBoxModel getCbmFinalActType() {
        return _cbmFinalActType;
    }

    public void setCbmFinalActType(ComboBoxModel _cbmFinalActType) {
        this._cbmFinalActType = _cbmFinalActType;
        setChanged();
    }

    public ComboBoxModel getCbmSubAccountType() {
        return _cbmSubAccountType;
    }

    public void setCbmSubAccountType(ComboBoxModel _cbmSubAccountType) {
        this._cbmSubAccountType = _cbmSubAccountType;
        setChanged();
    }
    
    /**
     * Getter for property cboFinalActType.
     * @return Value of property cboFinalActType.
     */
      
   
    /**
     * Setter for property cboFinalActType.
     * @param cboFinalActType New value of property cboFinalActType.
     */
       
   
    
}
