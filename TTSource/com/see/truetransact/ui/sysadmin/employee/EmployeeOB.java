/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeOB.java
 *
 * Created on February 13, 2004, 11:22 AM
 */

package com.see.truetransact.ui.sysadmin.employee;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.sysadmin.employee.EmployeeDetailsTO;
import com.see.truetransact.transferobject.sysadmin.employee.EmployeeAddrTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.apache.log4j.Logger;
import java.util.Date;

/**
 *
 * @author  rahul
 */
public class EmployeeOB extends Observable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    
    private ComboBoxModel cbmTitle;
    private ComboBoxModel cbmEmployeeType;
    private ComboBoxModel cbmMartialStatus;
    private ComboBoxModel cbmJobTitle;
    private ComboBoxModel cbmDepartment;
    private ComboBoxModel cbmManager;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    private ComboBoxModel cbmCountry;
    
    private int actionType;
    private int result;
    
    private String lblValBranchName = "";
    private String lblPhoto = "";
    private String photoFile = "";    
    Date curDate = null;
    
    private File selFile;
    private byte[] photoByteArray;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger log = Logger.getLogger(EmployeeUI.class);
    
    private static EmployeeOB employeeOB;
    static {
        try {
            employeeOB = new EmployeeOB();
        } catch(Exception e) {
            log.info("Error in EmployeeOB:");
        }
    }
    
    public static EmployeeOB getInstance() {
        return employeeOB;
    }
    
    
    
    /** Creates a new instance of EmployeeOB */
    public EmployeeOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("initianSetup");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "EmployeeJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.employee.EmployeeHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.employee.Employee");
    }
    
    public void fillDropdown() throws Exception{
        log.info("fillDropdown");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("CUSTOMER.TITLE");
        lookup_keys.add("EMPLOYEETYPE");
        lookup_keys.add("MARTIAL_STATUS");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.TITLE"));
        cbmTitle = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("EMPLOYEETYPE"));
        cbmEmployeeType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("MARTIAL_STATUS"));
        cbmMartialStatus = new ComboBoxModel(key,value);
        
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key,value);
        //-------------------------------------------------
        
        //-------------------------------------------------
        lookUpHash.put(CommonConstants.MAP_NAME,"getDesignation");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmJobTitle = new ComboBoxModel(key,value);
        
        lookUpHash.put(CommonConstants.MAP_NAME,"getDepartment");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmDepartment = new ComboBoxModel(key,value);
        
        lookUpHash.put(CommonConstants.MAP_NAME,"getManager");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmManager = new ComboBoxModel(key,value);
        
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("getKeyValue");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // Set and the get methods for ComboBox models...
    public void setCbmTitle(ComboBoxModel cbmTitle){
        this.cbmTitle = cbmTitle;
        setChanged();
    }
    
    ComboBoxModel getCbmTitle(){
        return cbmTitle;
    }
    
    //
    public void setCbmEmployeeType(ComboBoxModel cbmEmployeeType){
        this.cbmEmployeeType = cbmEmployeeType;
        setChanged();
    }
    
    ComboBoxModel getCbmEmployeeType(){
        return cbmEmployeeType;
    }
    //
    public void setCbmCity(ComboBoxModel cbmCity){
        this.cbmCity = cbmCity;
        setChanged();
    }
    
    ComboBoxModel getCbmCity(){
        return cbmCity;
    }
    //
    public void setCbmState(ComboBoxModel cbmState){
        this.cbmState = cbmState;
        setChanged();
    }
    
    ComboBoxModel getCbmState(){
        return cbmState;
    }
    //
    public void setCbmCountry(ComboBoxModel cbmCountry){
        this.cbmCountry = cbmCountry;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry(){
        return cbmCountry;
    }
    //
    public void setCbmJobTitle(ComboBoxModel cbmJobTitle){
        this.cbmJobTitle = cbmJobTitle;
        setChanged();
    }
    
    ComboBoxModel getCbmJobTitle(){
        return cbmJobTitle;
    }
    //
    public void setCbmDepartment(ComboBoxModel cbmDepartment){
        this.cbmDepartment = cbmDepartment;
        setChanged();
    }
    
    ComboBoxModel getCbmDepartment(){
        return cbmDepartment;
    }
    //
    public void setCbmManager(ComboBoxModel cbmManager){
        this.cbmManager = cbmManager;
        setChanged();
    }
    
    ComboBoxModel getCbmManager(){
        return cbmManager;
    }
    
    
     //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        
        EmployeeDetailsTO objEmployeeDetailsTO = null;
        EmployeeAddrTO objEmployeeAddrTO = null;
        //Taking the Value of Emp_Id from each Table...
        // Here the first Row is selected...
        objEmployeeDetailsTO = (EmployeeDetailsTO) ((List) mapData.get("EmployeeDetailsTO")).get(0);
        setEmployeeDetailsTO(objEmployeeDetailsTO);
        
        objEmployeeAddrTO = (EmployeeAddrTO) ((List) mapData.get("EmployeeAddrTO")).get(0);
        setEmployeeAddrTO(objEmployeeAddrTO);
        
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setEmployeeDetailsTO(EmployeeDetailsTO objEmployeeDetailsTO) throws Exception{
        log.info("In setEmployeeDetailsTO()");
        setTxtBranchCode (objEmployeeDetailsTO.getBranchCode ());
        //--- To display the Branch name
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", objEmployeeDetailsTO.getBranchCode ());
        List list = (List)ClientUtil.executeQuery("getBranchData", whereMap);
        HashMap hash = (HashMap)list.get(0);
        String branchName = CommonUtil.convertObjToStr(hash.get("BRANCH_NAME"));
        setLblValBranchName(branchName);
        
        setTxtEmployeeId (objEmployeeDetailsTO.getEmployeeCode ());
        setCboTitle((String) getCbmTitle().getDataForKey(objEmployeeDetailsTO.getTitle ())); 
        
        if (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getGender()).equals("MALE")) setRdoGender_Male(true);
        else setRdoGender_Female(true);
        
//        if (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMaritalStatus()).equals("SINGLE")) setRdoMartialStatus_Single(true);
//        else setRdoMartialStatus_Married(true);
       
        setTxtLastName (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getLname ()));
        setTxtFirstName (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getFname ()));
        setCboJobTitle ((String) getCbmJobTitle().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getDesigId ())));
        setTdtBirthDate (DateUtil.getStringDate (objEmployeeDetailsTO.getDob ()));
        setTdtJoiningDate (DateUtil.getStringDate (objEmployeeDetailsTO.getDoj ()));
        setTdtLeavingDate (DateUtil.getStringDate (objEmployeeDetailsTO.getDol ()));
        setTdtWeddindDate (DateUtil.getStringDate (objEmployeeDetailsTO.getDow ()));
        setCboDepartment ((String) getCbmDepartment().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getDepttId ())));
        setCboManager ((String) getCbmManager().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getManagerCode ())));
        setTxtOfficialEmail (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getOfficialEmail ()));
        setTxtAlternateEmail (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getAlternateEmail ()));
        setTxtOfficePhone (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getOfficePhone ()));
        setTxtHomePhone (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getHomePhone ()));
        setTxtCellular (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getCellular ()));
        setTxtPanNo (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPanNo ()));
        setTxtSsnNo (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSsn ()));
        setTxtPassPortNo (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPassportNo ()));
        setTxaSkills (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSkills ()));
        setTxaEducation (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getEducation ()));
        setTxaExperience (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getExperience ()));
        //setTxtPhotoFile (objEmployeeDetailsTO.getPhotoFile ());
        // To insert the picture from Database into the UI...
         setPhotoFile(objEmployeeDetailsTO.getPhotoFile());
         
        setTxaResponsibility (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getResponsibility ()));
        setTxaPerformance (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPerformance ()));
        setTxaComments (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getComments ()));
        setCboEmployeeType ((String) getCbmEmployeeType().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getEmployeeType ())));
        setCboMartialStatus((String) getCbmMartialStatus().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMaritalStatus())));
        log.info("End of setEmployeeDetailsTO()");
        
    }
    
    
    private void setEmployeeAddrTO(EmployeeAddrTO objEmployeeAddrTO) throws Exception{
        log.info("In setEmployeeAddrTO()");
        setTxtEmployeeId (CommonUtil.convertObjToStr(objEmployeeAddrTO.getEmployeeId ())); 
        setTxtStreet (CommonUtil.convertObjToStr(objEmployeeAddrTO.getStreet ()));
        setTxtArea (CommonUtil.convertObjToStr(objEmployeeAddrTO.getArea ()));
        setCboCity ((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objEmployeeAddrTO.getCity ())));
        setCboState ((String) getCbmState().getDataForKey(CommonUtil.convertObjToStr(objEmployeeAddrTO.getState ())));
        setCboCountry ((String) getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objEmployeeAddrTO.getCountryCode ())));
        setTxtPinCode (CommonUtil.convertObjToStr(objEmployeeAddrTO.getPinCode ()));
    }
    
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public EmployeeDetailsTO setEmployeeDetails() {
        log.info("In setEmployeeDetails()");
        
        final EmployeeDetailsTO objEmployeeDetailsTO = new EmployeeDetailsTO();
        try{
            objEmployeeDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objEmployeeDetailsTO.setStatusDt(curDate);
            objEmployeeDetailsTO.setBranchCode(txtBranchCode);
            objEmployeeDetailsTO.setEmployeeCode(txtEmployeeId);
            objEmployeeDetailsTO.setTitle((String)cbmTitle.getKeyForSelected());
            objEmployeeDetailsTO.setLname(txtLastName);
            objEmployeeDetailsTO.setFname(txtFirstName);
            
//            if (getRdoMartialStatus_Single() == true) {
//                objEmployeeDetailsTO.setMaritalStatus("SINGLE");
//            } else if (getRdoMartialStatus_Married() == true) {
//                objEmployeeDetailsTO.setMaritalStatus("MARRIED");
//            }
            
            if (getRdoGender_Male() == true) {
                objEmployeeDetailsTO.setGender("MALE");
            } else if (getRdoGender_Female() == true) {
                objEmployeeDetailsTO.setGender("FEMALE");
            }
            
            objEmployeeDetailsTO.setDesigId((String)cbmJobTitle.getKeyForSelected());
            objEmployeeDetailsTO.setEmployeeType((String)cbmEmployeeType.getKeyForSelected());
            objEmployeeDetailsTO.setMaritalStatus((String)cbmMartialStatus.getKeyForSelected());
            objEmployeeDetailsTO.setDepttId((String)cbmDepartment.getKeyForSelected());
            objEmployeeDetailsTO.setManagerCode((String)cbmManager.getKeyForSelected());
            objEmployeeDetailsTO.setOfficialEmail(txtOfficialEmail);
            objEmployeeDetailsTO.setAlternateEmail(txtAlternateEmail);
            objEmployeeDetailsTO.setOfficePhone(txtOfficePhone);
            objEmployeeDetailsTO.setHomePhone(txtHomePhone);
            objEmployeeDetailsTO.setCellular(txtCellular);
            
            objEmployeeDetailsTO.setPhotoFile(getPhotoFile());
            
            Date BdDt = DateUtil.getDateMMDDYYYY(tdtBirthDate);
             if(BdDt != null){
            Date bdDate = (Date)curDate.clone();
            bdDate.setDate(BdDt.getDate());
            bdDate.setMonth(BdDt.getMonth());
            bdDate.setYear(BdDt.getYear());
//            objEmployeeDetailsTO.setDob(DateUtil.getDateMMDDYYYY(tdtBirthDate));
            objEmployeeDetailsTO.setDob(DateUtil.getDateMMDDYYYY(tdtBirthDate));
             }else{
                 objEmployeeDetailsTO.setDob(DateUtil.getDateMMDDYYYY(tdtBirthDate));
             }
            
            Date JdDt = DateUtil.getDateMMDDYYYY(tdtJoiningDate);
             if(JdDt != null){
            Date jdDate = (Date)curDate.clone();
            jdDate.setDate(JdDt.getDate());
            jdDate.setMonth(JdDt.getMonth());
            jdDate.setYear(JdDt.getYear());
//            objEmployeeDetailsTO.setDoj(DateUtil.getDateMMDDYYYY(tdtJoiningDate));
            objEmployeeDetailsTO.setDoj(jdDate);
             }else{
                 objEmployeeDetailsTO.setDoj(DateUtil.getDateMMDDYYYY(tdtJoiningDate));
             }
            
            Date LdDt = DateUtil.getDateMMDDYYYY(tdtLeavingDate);
             if(LdDt != null){
            Date ldDate = (Date)curDate.clone();
            ldDate.setDate(LdDt.getDate());
            ldDate.setMonth(LdDt.getMonth());
            ldDate.setYear(LdDt.getYear());
//            objEmployeeDetailsTO.setDol(DateUtil.getDateMMDDYYYY(tdtLeavingDate));
            objEmployeeDetailsTO.setDol(DateUtil.getDateMMDDYYYY(tdtLeavingDate));
             }else{
                 objEmployeeDetailsTO.setDol(DateUtil.getDateMMDDYYYY(tdtLeavingDate));
             }
            
            Date WdDt = DateUtil.getDateMMDDYYYY(tdtWeddindDate);
             if(WdDt != null){
            Date wdtDate = (Date)curDate.clone();
            wdtDate.setDate(WdDt.getDate());
            wdtDate.setMonth(WdDt.getMonth());
            wdtDate.setYear(WdDt.getYear());
//            objEmployeeDetailsTO.setDow(DateUtil.getDateMMDDYYYY(tdtWeddindDate));
            objEmployeeDetailsTO.setDow(wdtDate);
             }else{
                 objEmployeeDetailsTO.setDow(DateUtil.getDateMMDDYYYY(tdtWeddindDate));
             }
            
            objEmployeeDetailsTO.setPanNo(txtPanNo);
            objEmployeeDetailsTO.setSsn(txtSsnNo);
            objEmployeeDetailsTO.setPassportNo(txtPassPortNo);
            objEmployeeDetailsTO.setSkills(txaSkills);
            objEmployeeDetailsTO.setEducation(txaEducation);
            objEmployeeDetailsTO.setExperience(txaExperience);
            
            objEmployeeDetailsTO.setResponsibility(txaResponsibility);
            objEmployeeDetailsTO.setPerformance(txaPerformance);
            objEmployeeDetailsTO.setComments(txaComments);
            objEmployeeDetailsTO.setCreatedBy("HUMAN");
            
        }catch(Exception e){
            log.info("Error In setEmployeeDetails()");
            e.printStackTrace();
        }
        return objEmployeeDetailsTO;
    }
    
    public EmployeeAddrTO setEmployeeAddr() {
        log.info("In setEmployeeAddr()");
        
        final EmployeeAddrTO objEmployeeAddrTO = new EmployeeAddrTO();
        try{
            objEmployeeAddrTO.setEmployeeId(txtEmployeeId);
            objEmployeeAddrTO.setStreet(txtStreet);
            objEmployeeAddrTO.setArea(txtArea);
            objEmployeeAddrTO.setCity((String)cbmCity.getKeyForSelected());
            objEmployeeAddrTO.setState((String)cbmState.getKeyForSelected());
            objEmployeeAddrTO.setPinCode(txtPinCode);
            objEmployeeAddrTO.setCountryCode((String)cbmCountry.getKeyForSelected());
        }catch(Exception e){
            log.info("Error In setEmployeeAddr()");
            e.printStackTrace();
        }
        return objEmployeeAddrTO;
    }
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setInwardClearingTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        
        //final InwardClearingTO objInwardClearingTO = setInwardClearing();
        final EmployeeDetailsTO objEmployeeDetailsTO = setEmployeeDetails();
        final EmployeeAddrTO objEmployeeAddrTO = setEmployeeAddr();
        
        objEmployeeDetailsTO.setCommand(getCommand());
        
        final HashMap data = new HashMap();
        data.put("EmployeeDetailsTO",objEmployeeDetailsTO);
        data.put("EmployeeAddrTO",objEmployeeAddrTO);
        data.put("photo",this.photoByteArray);
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        
        setResult(actionType);
        resetForm();
    }
    
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
        log.info("In getCommand()");
        
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
    
    public void resetForm(){
        log.info("In resetForm()");
        
        setTxtEmployeeId("");
        setTxtBranchCode("");
        setCboTitle("");
        setTxtFirstName("");
        setTxtLastName("");
        setRdoGender_Male(false);
        setRdoGender_Female(false);
        setCboEmployeeType("");
        setCboMartialStatus("");
        setCboJobTitle("");
        setCboDepartment("");
        setCboManager("");
        setTxtOfficialEmail("");
        setTxtAlternateEmail("");
        setTxtOfficePhone("");
        setTxtHomePhone("");
        setTxtCellular("");
        setTxtStreet("");
        setTxtArea("");
        setCboCity("");
        setCboState("");
        setCboCountry("");
        setTxtPinCode("");
        setTdtBirthDate("");
        setTdtWeddindDate("");
        setTdtJoiningDate("");
        setTdtLeavingDate("");
        setTxtPanNo("");
        setTxtSsnNo("");
        setTxtPassPortNo("");
        setTxaSkills("");
        setTxaEducation("");
        setTxaExperience("");
        setTxaResponsibility("");
        setTxaPerformance("");
        setTxaComments("");
        setLblValBranchName("");
        setLblPhoto("");
        
        ttNotifyObservers();
    }
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    private String txtEmployeeId = "";
    private String txtBranchCode = "";
    private String cboTitle = "";
    private String txtFirstName = "";
    private String txtLastName = "";
    
    private boolean rdoGender_Male = false;
    private boolean rdoGender_Female = false;
    private String cboEmployeeType = "";
    private String cboMartialStatus = "";
    private String cboJobTitle = "";
    private String cboDepartment = "";
    private String cboManager = "";
    private String txtOfficialEmail = "";
    private String txtAlternateEmail = "";
    private String txtOfficePhone = "";
    private String txtHomePhone = "";
    private String txtCellular = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboState = "";
    private String cboCountry = "";
    private String txtPinCode = "";
    private String tdtBirthDate = "";
    private String tdtWeddindDate = "";
    private String tdtJoiningDate = "";
    private String tdtLeavingDate = "";
    private String txtPanNo = "";
    private String txtSsnNo = "";
    private String txtPassPortNo = "";
    private String txaSkills = "";
    private String txaEducation = "";
    private String txaExperience = "";
    private String txaResponsibility = "";
    private String txaPerformance = "";
    private String txaComments = "";
    
    void setTxtEmployeeId(String txtEmployeeId){
        this.txtEmployeeId = txtEmployeeId;
        setChanged();
    }
    String getTxtEmployeeId(){
        return this.txtEmployeeId;
    }
    
    void setTxtBranchCode(String txtBranchCode){
        this.txtBranchCode = txtBranchCode;
        setChanged();
    }
    String getTxtBranchCode(){
        return this.txtBranchCode;
    }
    
    void setCboTitle(String cboTitle){
        this.cboTitle = cboTitle;
        setChanged();
    }
    String getCboTitle(){
        return this.cboTitle;
    }
    
    void setTxtFirstName(String txtFirstName){
        this.txtFirstName = txtFirstName;
        setChanged();
    }
    String getTxtFirstName(){
        return this.txtFirstName;
    }
    
    void setTxtLastName(String txtLastName){
        this.txtLastName = txtLastName;
        setChanged();
    }
    String getTxtLastName(){
        return this.txtLastName;
    }
    
    void setRdoGender_Male(boolean rdoGender_Male){
        this.rdoGender_Male = rdoGender_Male;
        setChanged();
    }
    boolean getRdoGender_Male(){
        return this.rdoGender_Male;
    }
    
    void setRdoGender_Female(boolean rdoGender_Female){
        this.rdoGender_Female = rdoGender_Female;
        setChanged();
    }
    boolean getRdoGender_Female(){
        return this.rdoGender_Female;
    }
    
    void setCboEmployeeType(String cboEmployeeType){
        this.cboEmployeeType = cboEmployeeType;
        setChanged();
    }
    String getCboEmployeeType(){
        return this.cboEmployeeType;
    }
    
    void setCboJobTitle(String cboJobTitle){
        this.cboJobTitle = cboJobTitle;
        setChanged();
    }
    String getCboJobTitle(){
        return this.cboJobTitle;
    }
    
    void setCboDepartment(String cboDepartment){
        this.cboDepartment = cboDepartment;
        setChanged();
    }
    String getCboDepartment(){
        return this.cboDepartment;
    }
    void setCboManager(String cboManager){
        this.cboManager = cboManager;
        setChanged();
    }
    String getCboManager(){
        return this.cboManager;
    }
    
    void setTxtOfficialEmail(String txtOfficialEmail){
        this.txtOfficialEmail = txtOfficialEmail;
        setChanged();
    }
    String getTxtOfficialEmail(){
        return this.txtOfficialEmail;
    }
    
    void setTxtAlternateEmail(String txtAlternateEmail){
        this.txtAlternateEmail = txtAlternateEmail;
        setChanged();
    }
    String getTxtAlternateEmail(){
        return this.txtAlternateEmail;
    }
    
    void setTxtOfficePhone(String txtOfficePhone){
        this.txtOfficePhone = txtOfficePhone;
        setChanged();
    }
    String getTxtOfficePhone(){
        return this.txtOfficePhone;
    }
    
    void setTxtHomePhone(String txtHomePhone){
        this.txtHomePhone = txtHomePhone;
        setChanged();
    }
    String getTxtHomePhone(){
        return this.txtHomePhone;
    }
    
    void setTxtCellular(String txtCellular){
        this.txtCellular = txtCellular;
        setChanged();
    }
    String getTxtCellular(){
        return this.txtCellular;
    }
    
    void setTxtStreet(String txtStreet){
        this.txtStreet = txtStreet;
        setChanged();
    }
    String getTxtStreet(){
        return this.txtStreet;
    }
    
    void setTxtArea(String txtArea){
        this.txtArea = txtArea;
        setChanged();
    }
    String getTxtArea(){
        return this.txtArea;
    }
    
    void setCboCity(String cboCity){
        this.cboCity = cboCity;
        setChanged();
    }
    String getCboCity(){
        return this.cboCity;
    }
    
    void setCboState(String cboState){
        this.cboState = cboState;
        setChanged();
    }
    String getCboState(){
        return this.cboState;
    }
    
    void setCboCountry(String cboCountry){
        this.cboCountry = cboCountry;
        setChanged();
    }
    String getCboCountry(){
        return this.cboCountry;
    }
    
    void setTxtPinCode(String txtPinCode){
        this.txtPinCode = txtPinCode;
        setChanged();
    }
    String getTxtPinCode(){
        return this.txtPinCode;
    }
    
    void setTdtBirthDate(String tdtBirthDate){
        this.tdtBirthDate = tdtBirthDate;
        setChanged();
    }
    String getTdtBirthDate(){
        return this.tdtBirthDate;
    }
    
    void setTdtWeddindDate(String tdtWeddindDate){
        this.tdtWeddindDate = tdtWeddindDate;
        setChanged();
    }
    String getTdtWeddindDate(){
        return this.tdtWeddindDate;
    }
    
    void setTdtJoiningDate(String tdtJoiningDate){
        this.tdtJoiningDate = tdtJoiningDate;
        setChanged();
    }
    String getTdtJoiningDate(){
        return this.tdtJoiningDate;
    }
    
    void setTdtLeavingDate(String tdtLeavingDate){
        this.tdtLeavingDate = tdtLeavingDate;
        setChanged();
    }
    String getTdtLeavingDate(){
        return this.tdtLeavingDate;
    }
    
    void setTxtPanNo(String txtPanNo){
        this.txtPanNo = txtPanNo;
        setChanged();
    }
    String getTxtPanNo(){
        return this.txtPanNo;
    }
    
    void setTxtSsnNo(String txtSsnNo){
        this.txtSsnNo = txtSsnNo;
        setChanged();
    }
    String getTxtSsnNo(){
        return this.txtSsnNo;
    }
    
    void setTxtPassPortNo(String txtPassPortNo){
        this.txtPassPortNo = txtPassPortNo;
        setChanged();
    }
    String getTxtPassPortNo(){
        return this.txtPassPortNo;
    }
    
    void setTxaSkills(String txaSkills){
        this.txaSkills = txaSkills;
        setChanged();
    }
    String getTxaSkills(){
        return this.txaSkills;
    }
    
    void setTxaEducation(String txaEducation){
        this.txaEducation = txaEducation;
        setChanged();
    }
    String getTxaEducation(){
        return this.txaEducation;
    }
    
    void setTxaExperience(String txaExperience){
        this.txaExperience = txaExperience;
        setChanged();
    }
    String getTxaExperience(){
        return this.txaExperience;
    }
    
    void setTxaResponsibility(String txaResponsibility){
        this.txaResponsibility = txaResponsibility;
        setChanged();
    }
    String getTxaResponsibility(){
        return this.txaResponsibility;
    }
    
    void setTxaPerformance(String txaPerformance){
        this.txaPerformance = txaPerformance;
        setChanged();
    }
    String getTxaPerformance(){
        return this.txaPerformance;
    }
    
    void setTxaComments(String txaComments){
        this.txaComments = txaComments;
        setChanged();
    }
    String getTxaComments(){
        return this.txaComments;
    }
    
    //Setter and Getter for the Picture...
    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
        setChanged();
    }
    
    public String getPhotoFile(){
        return this.photoFile;
    }
    
    public void setLblPhoto(String lblPhoto) {
        this.lblPhoto = lblPhoto;
        setChanged();
    }
    
    public String getLblPhoto(){
        return this.lblPhoto;
    }
    
    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
        setChanged();
    }
    
    public byte[] getPhotoByteArray(){
        return this.photoByteArray;
    }
    
    /**
     * Getter for property lblValBranchName.
     * @return Value of property lblValBranchName.
     */
    public java.lang.String getLblValBranchName() {
        return lblValBranchName;
    }
    
    /**
     * Setter for property lblValBranchName.

     * @param lblValBranchName New value of property lblValBranchName.
     */
    public void setLblValBranchName(java.lang.String lblValBranchName) {
        this.lblValBranchName = lblValBranchName;
    }
    
    /**
     * Getter for property cboMartialStatus.
     * @return Value of property cboMartialStatus.
     */
    public java.lang.String getCboMartialStatus() {
        return cboMartialStatus;
    }
    
    /**
     * Setter for property cboMartialStatus.
     * @param cboMartialStatus New value of property cboMartialStatus.
     */
    public void setCboMartialStatus(java.lang.String cboMartialStatus) {
        this.cboMartialStatus = cboMartialStatus;
    }
    
    /**
     * Getter for property cbmMartialStatus.
     * @return Value of property cbmMartialStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMartialStatus() {
        return cbmMartialStatus;
    }
    
    /**
     * Setter for property cbmMartialStatus.
     * @param cbmMartialStatus New value of property cbmMartialStatus.
     */
    public void setCbmMartialStatus(com.see.truetransact.clientutil.ComboBoxModel cbmMartialStatus) {
        this.cbmMartialStatus = cbmMartialStatus;
    }
    
}
