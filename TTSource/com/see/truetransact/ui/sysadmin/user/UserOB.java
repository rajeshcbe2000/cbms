/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserOB.java
 *
 * Created on Wed Feb 09 11:43:43 IST 2005
 */

package com.see.truetransact.ui.sysadmin.user;


import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.sysadmin.user.TerminalMasterTO;
import com.see.truetransact.transferobject.sysadmin.user.UserTO;
import com.see.truetransact.transferobject.sysadmin.user.UserTerminalTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CDateField;
import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observable;


/**
 *
 * @author
 */

public class UserOB extends CObservable{
    private final static ClientParseException parseException =
    ClientParseException.getInstance();
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private HashMap map;
    private ProxyFactory proxy;
    
    //To store the User Details
    private HashMap userMap;
    private StringEncrypter encrypt = null;
    
    private TableModel tmlTerminals;
    private ComboBoxModel cbmGroupID;
    private ComboBoxModel cbmRoleID;
    private ComboBoxModel cbmForeignGroupId;
    private ComboBoxModel cbmForeignBranchGroup;
    private ArrayList userTerminalTOs;
    private ArrayList tableHeader=new ArrayList();
    
    private String lblDisplayCustName = "";
    private String lblLastLoginDate = "";
    
    private UserTO objUserTO;
    private UserTerminalTO userTerminalObj;
    private TerminalMasterTO objTerminalMasterTO;
    
    private static UserOB userOB;
    Date curDate = null;
    static {
        try {
            userOB = new UserOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of UserOB */
    public UserOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "UserJNDI");
            map.put(CommonConstants.HOME, "sysadmin.user.UserHome");
            map.put(CommonConstants.REMOTE, "sysadmin.user.User");
            
            encrypt = new StringEncrypter();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        setCoulmnHeader();
        tmlTerminals = new TableModel(new ArrayList(),tableHeader);
        userTerminalTOs=new ArrayList();
        populateCombo();
        populateForeignBranch();
    }
    
    public static UserOB getInstance() {
        return userOB;
    }
    private void setCoulmnHeader() {
        tableHeader.add("User ID");
        tableHeader.add("Terminal ID");
        tableHeader.add("From Date");
        tableHeader.add("To Date");
    }
    private void  populateCombo(){
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        setMap("getAllGroupMasterTO","");
        List dateList=populateData();
        if(dateList!=null){
            int j=dateList.size();
            for(int i=0;i<j;i++){
                userMap=(HashMap)dateList.get(i);
                key.add(userMap.get("groupId"));
                value.add((String)userMap.get("groupName")+" ("+ (String)userMap.get("groupId") + ")");
            }
        }
        cbmGroupID=new ComboBoxModel(key,value);
        cbmForeignGroupId=new ComboBoxModel(key,value);
        key=new ArrayList();
        value=new ArrayList();
        cbmRoleID=new ComboBoxModel(key,value);
    }
    
    private void  populateForeignBranch(){
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        setMap("getBranchGroupResult","");
        LookUpTO lookup ;
        List dateList=populateData();
        if(dateList!=null){
            int j=dateList.size();
            for(int i=0;i<j;i++){
                lookup=(LookUpTO)dateList.get(i);
                key.add(lookup.getLookUpRefID());
                value.add(lookup.getLookUpDesc());
            }
        }
        cbmForeignBranchGroup=new ComboBoxModel(key,value);
    }
    
    public String getBranchName(String branchCode){
        setMap("selectBranchMaster",branchCode);
        List dateList=populateData();
        if(dateList!=null){
            userMap=(HashMap)dateList.get(0);
            return (String)userMap.get("branchName");
        }
        return null;
    }
    
    //added by anjuanand
    public HashMap getBranchDet(HashMap dataMap) throws SQLException {
        HashMap data = new HashMap();
        data.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
        List cust = ClientUtil.executeQuery("getSelectBranch", data);
        HashMap brachData = new HashMap();
		if(cust.size()>0){
	        brachData = (HashMap) cust.get(0);
                if(brachData!=null){
	        setTxtBranchID(CommonUtil.convertObjToStr(brachData.get("BRANCH_CODE")));
	        setTxtBranchName(CommonUtil.convertObjToStr(brachData.get("BRANCH_NAME")));
		}
                }
        return brachData;
    }
    
    public void populateTerminalTO(String terminalID){
        setMap("getSelectTerminalMasterTO",terminalID);
        List dateList=populateData();
        if(dateList!=null){
            objTerminalMasterTO=(TerminalMasterTO)dateList.get(0);
            getTerminalMasterTO();
        }
        setChanged();
        notifyObservers();
    }
    public void populateRoleID(String groupID){
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        setMap("User_getSelectRoleMasterTO",groupID);
        List dateList=populateData();
        if(dateList!=null){
            int j=dateList.size();
            for(int i=0;i<j;i++){
                userMap=(HashMap)dateList.get(i);
                key.add( userMap.get("roleId"));
                value.add((String)userMap.get("roleName") + " (" + (String) userMap.get("roleId")+")");
            }
        }
        cbmRoleID=new ComboBoxModel(key,value);
    }
    private void setMap(String mapName,String mapWhere){
        userMap=new HashMap();
        userMap.put(CommonConstants.MAP_NAME,mapName);
        userMap.put(CommonConstants.MAP_WHERE,mapWhere);
    }
    private List populateData(){
        List list=null;
        try {
            userMap = proxy.executeQuery(userMap, map);
            list = (List) userMap.get(CommonConstants.MAP_NAME);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setResultStatus();
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        setChanged();
        makeNull();
        return list;
    }
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }

    public boolean isChkAppraiserAllowed() {
        return chkAppraiserAllowed;
    }

    public void setChkAppraiserAllowed(boolean chkAppraiserAllowed) {
        this.chkAppraiserAllowed = chkAppraiserAllowed;
    }

   
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    // Setter method for lblDisplayCustName
    void setLblDisplayCustName(String lblDisplayCustName){
        this.lblDisplayCustName = lblDisplayCustName;
        setChanged();
    }
    // Getter method for lblDisplayCustName
    String getLblDisplayCustName(){
        return this.lblDisplayCustName;
    }
    // Setter method for lblLastLoginDate
    void setLblLastLoginDate(String lblLastLoginDate){
        this.lblLastLoginDate = lblLastLoginDate;
        setChanged();
    }
    // Getter method for lblLastLoginDate
    String getLblLastLoginDate(){
        return this.lblLastLoginDate;
    }
    
    
    public void resetForm(){
        txtCustomerId = "";
        txtUserId = "";
        txtEmailId = "";
        chkAppraiserAllowed=false;
        tdtUserIdExpiresOn = "";
        txtRemarks = "";
        pwdPassword = "";
        pwdConfirmPassword = "";
        txtBranchID = "";
        txtBranchName = "";
        chkSmartCard = false;
        chkUserSuspend = false;
        chkSuspend = false;
        tdtActivateUser = "";
        txtTerminalId = "";
        txtTerminalName = "";
        txtIPAddress = "";
        txtMachineName = "";
        txtTerminalDescription = "";
        tdtAccessToDate = "";
        tdtAccessFromDate = "";
        tdtSuspendTo = "";
        tdtSuspendFrom = "";
        lblDisplayCustName = "";
        
        lblLastLoginDate = "";
        txtReasonForSuspend = "";
        
        tmlTerminals.setData(new ArrayList());
        tmlTerminals.fireTableDataChanged();
        
        cbmGroupID.setKeyForSelected("");
        cbmRoleID.removeAllElements();
        cbmForeignGroupId.setKeyForSelected("");
        cbmForeignBranchGroup.setKeyForSelected("");
        cboGroupID = "";
        cboRoleID = "";
        cboForeignGroupId = "";
        cboForeignBranchGroup = "";
        
        setChanged();
        notifyObservers();
    }
    private void makeNull(){
        userMap = null;
    }
    /** To do the necessary action */
    public void doAction() {
       try {
            //If actionType such as NEW, EDIT, DELETE , then proceed
            if( getActionType() != ClientConstants.ACTIONTYPE_CANCEL ){
                setUser();
                switch(getActionType()){
                    case ClientConstants.ACTIONTYPE_NEW:
                        setTOStatus(CommonConstants.TOSTATUS_INSERT,CommonConstants.STATUS_CREATED);
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        setTOStatus(CommonConstants.TOSTATUS_UPDATE,CommonConstants.STATUS_MODIFIED);
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        setTOStatus(CommonConstants.TOSTATUS_DELETE,CommonConstants.STATUS_DELETED);
                        break;
                }
                userMap = new HashMap();
                userMap.put("UserTO", objUserTO);
                this.getUserTerminalTOs();
                userMap.put("UserTerminalTOs",userTerminalTOs);
                HashMap proxyResultMap = proxy.execute(userMap, map);
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL ;
//                resetForm();
            }
            makeNull();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    /** To set the User data */
    private void setUser() throws Exception{
        objUserTO = new UserTO();
        String strEncrypt = encrypt.encrypt(this.getPwdPassword());
        
       // objUserTO.setEmployeeId(this.getTxtEmployeeId());
        objUserTO.setCustomerId(this.getTxtCustomerId());
        objUserTO.setUserId(this.getTxtUserId());
        //objUserTO.setAppraiserAllowed(this.chkAppraiserAllowed);
        objUserTO.setPwd(strEncrypt);
        
        objUserTO.setEmailId(this.getTxtEmailId());
        Date UserDt = DateUtil.getDateMMDDYYYY(this.getTdtUserIdExpiresOn());
        if(UserDt != null){
        Date userDate = (Date)curDate.clone();
        userDate.setDate(UserDt.getDate());
        userDate.setMonth(UserDt.getMonth());
        userDate.setYear(UserDt.getYear());
        objUserTO.setExpiryDate(userDate);
        }else{
           objUserTO.setExpiryDate( DateUtil.getDateMMDDYYYY(this.getTdtUserIdExpiresOn()) ); 
        }
//        objUserTO.setExpiryDate( DateUtil.getDateMMDDYYYY(this.getTdtUserIdExpiresOn()) );
        objUserTO.setRemarks(this.getTxtRemarks());
        objUserTO.setBranchCode(this.getTxtBranchID());
        objUserTO.setUserGroup((String)cbmGroupID.getKeyForSelected());
        objUserTO.setUserRole((String)cbmRoleID.getKeyForSelected());
        objUserTO.setForeignGroupId((String)cbmForeignGroupId.getKeyForSelected());
        objUserTO.setForeignBranchGroup((String)cbmForeignBranchGroup.getKeyForSelected());
        objUserTO.setCreatedBy(TrueTransactMain.USER_ID);
        objUserTO.setStatusBy(TrueTransactMain.USER_ID);
        objUserTO.setAuthorizedBy(TrueTransactMain.USER_ID);
        objUserTO.setAuthorizedDt(curDate);
        objUserTO.setCreatedDt(curDate);
        objUserTO.setStatusDt(curDate);
//        objUserTO.setLastLoginDt(DateUtil.getDateMMDDYYYY(this.getLblLastLoginDate()));
//        objUserTO.setSuspendFromDt(DateUtil.getDateMMDDYYYY(this.getTdtSuspendFrom()));
//        objUserTO.setSuspendToDt(DateUtil.getDateMMDDYYYY(this.getTdtSuspendTo()));
        Date LogInDt = DateUtil.getDateMMDDYYYY(this.getLblLastLoginDate());
        if(LogInDt != null){
        Date loginDate = (Date)curDate.clone();
        loginDate.setDate(LogInDt.getDate());
        loginDate.setMonth(LogInDt.getMonth());
        loginDate.setYear(LogInDt.getYear());
        objUserTO.setLastLoginDt(loginDate);
        }else{
            objUserTO.setLastLoginDt(DateUtil.getDateMMDDYYYY(this.getLblLastLoginDate()));
        }
        
        Date SuspDt = DateUtil.getDateMMDDYYYY(this.getTdtSuspendFrom());
        if(SuspDt != null){
        Date suspDate = (Date)curDate.clone();
        suspDate.setDate(SuspDt.getDate());
        suspDate.setMonth(SuspDt.getMonth());
        suspDate.setYear(SuspDt.getYear());
        objUserTO.setSuspendFromDt(suspDate);
        }else{
            objUserTO.setSuspendFromDt(DateUtil.getDateMMDDYYYY(this.getTdtSuspendFrom()));
        }
        
        Date SusToDt = DateUtil.getDateMMDDYYYY(this.getTdtSuspendTo());
        if(SusToDt != null){
        Date susToDate = (Date)curDate.clone();
        susToDate.setDate(SusToDt.getDate());
        susToDate.setMonth(SusToDt.getMonth());
        susToDate.setYear(SusToDt.getYear());
        objUserTO.setSuspendToDt(susToDate);
        }else{
           objUserTO.setSuspendToDt(DateUtil.getDateMMDDYYYY(this.getTdtSuspendTo())); 
        }
        objUserTO.setSuspendReason(this.getTxtReasonForSuspend());
        
        final UserRB objUserRB=new UserRB();
        
        if( this.getChkSmartCard() ){
            objUserTO.setPinLogin(objUserRB.getString("booleanYes"));
        }
        else{
            objUserTO.setPinLogin(objUserRB.getString("booleanNo"));
        }
        
        if( this.getChkUserSuspend() ){
            objUserTO.setSuspendByUser(objUserRB.getString("booleanYes"));
        }
        else{
            objUserTO.setSuspendByUser(objUserRB.getString("booleanNo"));
        }
         if(this.isChkAppraiserAllowed()){
           objUserTO.setAppraiserAllowed(CommonUtil.convertObjToStr(objUserRB.getString("booleanYes")));
        } else{
           objUserTO.setAppraiserAllowed(CommonUtil.convertObjToStr(objUserRB.getString("booleanNo"))); 
        }
        if( this.getChkSuspend() ){
            objUserTO.setSuspendUser(objUserRB.getString("booleanYes"));
        } else{
            objUserTO.setSuspendUser(objUserRB.getString("booleanNo"));
        }
        Date ActDt = DateUtil.getDateMMDDYYYY(this.getTdtActivateUser());
        if(ActDt != null){
        Date actDate = (Date)curDate.clone();
        actDate.setDate(ActDt.getDate());
        actDate.setMonth(ActDt.getMonth());
        actDate.setYear(ActDt.getYear());
        objUserTO.setActivationDate(actDate);
        }else{
            objUserTO.setActivationDate( DateUtil.getDateMMDDYYYY(this.getTdtActivateUser()) );
        }
//        objUserTO.setActivationDate( DateUtil.getDateMMDDYYYY(this.getTdtActivateUser()) );
        System.out.println("objUserTO : " + objUserTO);
    }
    /** To set the command & status variables of TO */
    private void setTOStatus(String command,String status){
        objUserTO.setCommand(command);
        objUserTO.setStatus(status);
    }
    
    /** To get the User data for the given userid. This method will be called from UI */
    public void getData(HashMap whereMap){
        setMap("User.getSelectUserTO",(String)whereMap.get("USER ID"));
        List list=this.populateData();
        if(list!=null){
            objUserTO=(UserTO)list.get(0);
            getUserTO();
        }
        setMap("getSelectUserTerminalTO",(String)whereMap.get("USER ID"));
        userTerminalTOs=(ArrayList)this.populateData();
        this.setUserTerminalTOs();
        setChanged();
        notifyObservers();
    }
    public String getCustomerName(String custId) {
        String custName = "";
        try {
            if (custId != null && custId.length()>0) {
                HashMap where = new HashMap()  ;
                where.put("CUST_ID",custId);
                List list = (List)ClientUtil.executeQuery("getUserMasterCustName",where);
                if (list != null && list.size()>0) {
                    custName = (String)((HashMap)list.get(0)).get("CUST_NAME");
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return custName;
    }
    public void getUserTO(){
        if(objUserTO!=null){
            try {
                this.setTxtCustomerId(CommonUtil.convertObjToStr(objUserTO.getCustomerId()));
                this.setTxtUserId(CommonUtil.convertObjToStr(objUserTO.getUserId()));
               // this.setChkAppraiserAllowed(objUserTO.isAppraiserAllowed());
                this.setPwdPassword(CommonUtil.convertObjToStr(encrypt.decrypt(objUserTO.getPwd())));
                this.setPwdConfirmPassword(CommonUtil.convertObjToStr(encrypt.decrypt(objUserTO.getPwd())));
                this.setTxtEmailId(CommonUtil.convertObjToStr(objUserTO.getEmailId()));
                this.setTdtUserIdExpiresOn(DateUtil.getStringDate(objUserTO.getExpiryDate()));
                this.setTxtRemarks(CommonUtil.convertObjToStr(objUserTO.getRemarks()));
                this.setTxtBranchID(objUserTO.getBranchCode());
                this.setLblDisplayCustName(CommonUtil.convertObjToStr(getLblDisplayCustName()));
                this.setLblLastLoginDate(DateUtil.getStringDate(objUserTO.getLastLoginDt()));
                this.setTdtSuspendFrom(DateUtil.getStringDate(objUserTO.getSuspendFromDt()));
                this.setTdtSuspendTo(DateUtil.getStringDate(objUserTO.getSuspendToDt()));
                this.setTxtReasonForSuspend(CommonUtil.convertObjToStr(objUserTO.getSuspendReason()));

                //To populate checkboxes with proper values from objUserTO object
                final UserRB objUserRB=new UserRB();

                if( CommonUtil.convertObjToStr( objUserTO.getPinLogin()).equals(objUserRB.getString("booleanYes")) ){
                    this.setChkSmartCard(true);
                } else{
                    this.setChkSmartCard(false);
                }                

                if( CommonUtil.convertObjToStr( objUserTO.getSuspendByUser()).equals(objUserRB.getString("booleanYes")) ){
                    this.setChkUserSuspend(true);
                } else{
                    this.setChkUserSuspend(false);
                }

                if( CommonUtil.convertObjToStr(objUserTO.getSuspendUser()).equals(objUserRB.getString("booleanYes")) ){
                    this.setChkSuspend(true);
                }else{
                    this.setChkSuspend(false);
                }

                 if( CommonUtil.convertObjToStr( objUserTO.isAppraiserAllowed()).equals(CommonUtil.convertObjToStr(objUserRB.getString("booleanYes"))) ){
                    this.setChkAppraiserAllowed(true);
                }else{
                    this.setChkAppraiserAllowed(false);
                }
                
                this.setTdtActivateUser(DateUtil.getStringDate(objUserTO.getActivationDate()));
                //this.populateCombo();
                this.setTxtBranchName(this.getBranchName(CommonUtil.convertObjToStr(objUserTO.getBranchCode())));
                this.populateRoleID(CommonUtil.convertObjToStr(objUserTO.getUserGroup()));
                cbmGroupID.setKeyForSelected(CommonUtil.convertObjToStr(objUserTO.getUserGroup()));
                cbmRoleID.setKeyForSelected(CommonUtil.convertObjToStr(objUserTO.getUserRole()));
                cbmForeignGroupId.setKeyForSelected(CommonUtil.convertObjToStr(objUserTO.getForeignGroupId()));
                cbmForeignBranchGroup.setKeyForSelected(CommonUtil.convertObjToStr(objUserTO.getForeignBranchGroup()));
                this.setCboGroupID((String) getCbmGroupID().getDataForKey(CommonUtil.convertObjToStr(objUserTO.getUserGroup())));
                this.setCboRoleID((String) getCbmRoleID().getDataForKey(CommonUtil.convertObjToStr(objUserTO.getUserRole())));
                this.setCboForeignGroupId((String) getCbmForeignGroupId().getDataForKey(CommonUtil.convertObjToStr(objUserTO.getForeignGroupId())));
                this.setCboForeignBranchGroup((String) getCbmForeignBranchGroup().getDataForKey(CommonUtil.convertObjToStr(objUserTO.getForeignBranchGroup())));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            objUserTO=null;
        }
        
    }
    public void getTerminalMasterTO(){
        setTxtTerminalName(CommonUtil.convertObjToStr(objTerminalMasterTO.getTerminalName()));
        setTxtIPAddress(CommonUtil.convertObjToStr(objTerminalMasterTO.getIpAddr()));
        setTxtMachineName(CommonUtil.convertObjToStr(objTerminalMasterTO.getMachineName()));
        setTxtTerminalDescription(CommonUtil.convertObjToStr(objTerminalMasterTO.getTerminalDescription()));
        setTxtTerminalId(CommonUtil.convertObjToStr(objTerminalMasterTO.getTerminalId()));
        setChanged();
    }
    
    /** Getter for property cbmGroupID.
     * @return Value of property cbmGroupID.
     *
     */
    public ComboBoxModel getCbmGroupID() {
        return cbmGroupID;
    }
    
    /** Setter for property cbmGroupID.
     * @param cbmGroupID New value of property cbmGroupID.
     *
     */
    public void setCbmGroupID(ComboBoxModel cbmGroupID) {
        this.cbmGroupID = cbmGroupID;
    }
    
    /** Getter for property tmlTerminals.
     * @return Value of property tmlTerminals.
     *
     */
    public TableModel getTmlTerminals() {
        return tmlTerminals;
    }
    
    /** Setter for property tmlTerminals.
     * @param tmlTerminals New value of property tmlTerminals.
     *
     */
    public void setTmlTerminals(TableModel tmlTerminals) {
        this.tmlTerminals = tmlTerminals;
    }
    
    /** Getter for property cbmRoleID.
     * @return Value of property cbmRoleID.
     *
     */
    public ComboBoxModel getCbmRoleID() {
        return cbmRoleID;
    }
    
    /** Setter for property cbmRoleID.
     * @param cbmRoleID New value of property cbmRoleID.
     *
     */
    public void setCbmRoleID(ComboBoxModel cbmRoleID) {
        this.cbmRoleID = cbmRoleID;
    }
    private void setUserTerminalTOs() {
        ArrayList arr;
        tmlTerminals.setData(new ArrayList());
        int j=userTerminalTOs.size();
        for ( int i=0; i<j;i++) {
            userTerminalObj = (UserTerminalTO)userTerminalTOs.get(i);
            arr= new ArrayList();
            arr.add(userTerminalObj.getUserId());
            arr.add(userTerminalObj.getTerminalId());
            arr.add(DateUtil.getStringDate(userTerminalObj.getAccessFromDt()));
            arr.add(DateUtil.getStringDate(userTerminalObj.getAccessToDt()));
            tmlTerminals.insertRow(tmlTerminals.getRowCount(),arr);
            tmlTerminals.fireTableDataChanged();
        }
    }
    private void getUserTerminalTOs(){
        ArrayList arr=tmlTerminals.getDataArrayList();
        ArrayList userTerminals=new ArrayList();
        ArrayList row;
        int j=arr.size();
        for(int i=0;i<j;i++){
            row=(ArrayList)arr.get(i);
            userTerminalObj=new UserTerminalTO();
            userTerminalObj.setTerminalId((String)row.get(1));
            userTerminalObj.setUserId((String)row.get(0));
            userTerminalObj.setAccessFromDt(DateUtil.getDateMMDDYYYY((String)row.get(2)));
            userTerminalObj.setAccessToDt(DateUtil.getDateMMDDYYYY((String)row.get(3)));
            userTerminalObj.setStatus(CommonConstants.STATUS_CREATED);
            userTerminals.add(userTerminalObj);
        }
        mergeTOs(userTerminals);
    }
    private void mergeTOs(ArrayList arr){
        int j=userTerminalTOs.size();
        int k=arr.size();
        UserTerminalTO toCompare;
        int l;
        for(int i=0;i<j;i++){
            toCompare=(UserTerminalTO)userTerminalTOs.get(i);
            for(l=0;l<k;l++){
                userTerminalObj=(UserTerminalTO)arr.get(l);
                if(userTerminalObj.getTerminalId().equals(toCompare.getTerminalId())){
                    userTerminalTOs.remove(i);
                    i=i-1;
                    break;
                }
            }
            j=userTerminalTOs.size();
        }
        j=userTerminalTOs.size();
        for(int i=0;i<j;i++){
            toCompare=(UserTerminalTO)userTerminalTOs.get(i);
            toCompare.setStatus(CommonConstants.STATUS_DELETED);
            userTerminalTOs.set(i,toCompare);
        }
        userTerminalTOs.addAll(arr);
    }
    public ArrayList populateTerminals(int rowNum) {
        return tmlTerminals.getRow(rowNum);
    }
    public void deleteTerminalData(int rowNum) {
        tmlTerminals.removeRow(rowNum);
    }
    public int insertTerminalData(ArrayList irRow) {
        ArrayList arr = tmlTerminals.getDataArrayList();
        ArrayList arr1;
        int j=arr.size();
        for (int i=0;i<j;i++) {
            arr1 = (ArrayList)arr.get(i);
            if ( ((String)arr1.get(1)).equalsIgnoreCase((String)(irRow.get(1)))){
                deleteTerminalData(i);
                tmlTerminals.insertRow(i,irRow);
                return 0;
            }
        }
        tmlTerminals.insertRow(tmlTerminals.getRowCount(), irRow);
        tmlTerminals.fireTableDataChanged();
        return 0;
    }
    /**
     * check the uniqueness of user id
     */
    public boolean userIdUniqueness(String userId) {
        boolean idExist = false;
        try {
            if (userId != null && !userId.equals("") && userId.length()>0) {
                HashMap where = new HashMap();
                where.put("USER_ID", userId);
                List list = (List)ClientUtil.executeQuery("checkUserIdUniqueness",where);
                where = null;
                if (list != null && list.size()>0) {
                    int count = Integer.parseInt(CommonUtil.convertObjToStr(((HashMap)list.get(0)).get("USER_ID_COUNT")));
                    if (count>0) {
                        idExist = true;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return idExist;
    }
    private String txtCustomerId="";
    private String txtUserId = "";
    private String txtEmailId = "";
    private String tdtUserIdExpiresOn = "";
    private String txtRemarks = "";
    private String pwdPassword = "";
    private String pwdConfirmPassword = "";
    private String txtBranchID = "";
    private String txtBranchName = "";
    private String cboRoleID = "";
    private String cboForeignGroupId = "";
    private String cboForeignBranchGroup = "";
    private String cboGroupID = "";
    private String tdtActivateUser = "";
    private boolean chkSmartCard = false;
    private boolean chkAppraiserAllowed = false;       
    private boolean chkUserSuspend = false;
    private boolean chkSuspend = false;
    private String txtReasonForSuspend = "";
    private String tdtSuspendFrom = "";
    private String tdtSuspendTo = "";
    private String txtTerminalId = "";
    private String txtTerminalName = "";
    private String txtIPAddress = "";
    private String txtMachineName = "";
    private String txtTerminalDescription = "";
    private String tdtAccessToDate = "";
    private String tdtAccessFromDate = "";
  
    
    
    // Setter method for txtUserId
    void setTxtUserId(String txtUserId){
        this.txtUserId = txtUserId;
        setChanged();
    }
    // Getter method for txtUserId
    String getTxtUserId(){
        return this.txtUserId;
    }
    
    // Setter method for txtEmailId
    void setTxtEmailId(String txtEmailId){
        this.txtEmailId = txtEmailId;
        setChanged();
    }
    // Getter method for txtEmailId
    String getTxtEmailId(){
        return this.txtEmailId;
    }
    
    // Setter method for tdtUserIdExpiresOn
    void setTdtUserIdExpiresOn(String tdtUserIdExpiresOn){
        this.tdtUserIdExpiresOn = tdtUserIdExpiresOn;
        setChanged();
    }
    // Getter method for tdtUserIdExpiresOn
    String getTdtUserIdExpiresOn(){
        return this.tdtUserIdExpiresOn;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for pwdPassword
    void setPwdPassword(String pwdPassword){
        this.pwdPassword = pwdPassword;
        setChanged();
    }
    // Getter method for pwdPassword
    String getPwdPassword(){
        return this.pwdPassword;
    }
    
    // Setter method for pwdConfirmPassword
    void setPwdConfirmPassword(String pwdConfirmPassword){
        this.pwdConfirmPassword = pwdConfirmPassword;
        setChanged();
    }
    // Getter method for pwdConfirmPassword
    String getPwdConfirmPassword(){
        return this.pwdConfirmPassword;
    }
    
    // Setter method for txtBranchID
    void setTxtBranchID(String txtBranchID){
        this.txtBranchID = txtBranchID;
        setChanged();
    }
    // Getter method for txtBranchID
    String getTxtBranchID(){
        return this.txtBranchID;
    }
    
    // Setter method for txtBranchName
    void setTxtBranchName(String txtBranchName){
        this.txtBranchName = txtBranchName;
        setChanged();
    }
    // Getter method for txtBranchName
    String getTxtBranchName(){
        return this.txtBranchName;
    }
    
    // Setter method for cboRoleID
    void setCboRoleID(String cboRoleID){
        this.cboRoleID = cboRoleID;
        setChanged();
    }
    // Getter method for cboRoleID
    String getCboRoleID(){
        return this.cboRoleID;
    }
    
    // Setter method for cboGroupID
    void setCboGroupID(String cboGroupID){
        this.cboGroupID = cboGroupID;
        setChanged();
    }
    // Getter method for cboGroupID
    String getCboGroupID(){
        return this.cboGroupID;
    }
    
    // Setter method for tdtActivateUser
    void setTdtActivateUser(String tdtActivateUser){
        this.tdtActivateUser = tdtActivateUser;
        setChanged();
    }
    // Getter method for tdtActivateUser
    String getTdtActivateUser(){
        return this.tdtActivateUser;
    }
    
    // Setter method for chkSmartCard
    void setChkSmartCard(boolean chkSmartCard){
        this.chkSmartCard = chkSmartCard;
        setChanged();
    }
    // Getter method for chkSmartCard
    boolean getChkSmartCard(){
        return this.chkSmartCard;
    }
    
    // Setter method for chkUserSuspend
    void setChkUserSuspend(boolean chkUserSuspend){
        this.chkUserSuspend = chkUserSuspend;
        setChanged();
    }
    // Getter method for chkUserSuspend
    boolean getChkUserSuspend(){
        return this.chkUserSuspend;
    }
    
    // Setter method for chkSuspend
    void setChkSuspend(boolean chkSuspend){
        this.chkSuspend = chkSuspend;
        setChanged();
    }
    // Getter method for chkSuspend
    boolean getChkSuspend(){
        return this.chkSuspend;
    }
    
    // Setter method for txtReasonForSuspend
    void setTxtReasonForSuspend(String txtReasonForSuspend){
        this.txtReasonForSuspend = txtReasonForSuspend;
        setChanged();
    }
    // Getter method for txtReasonForSuspend
    String getTxtReasonForSuspend(){
        return this.txtReasonForSuspend;
    }
    
    // Setter method for tdtSuspendFrom
    void setTdtSuspendFrom(String tdtSuspendFrom){
        this.tdtSuspendFrom = tdtSuspendFrom;
        setChanged();
    }
    // Getter method for tdtSuspendFrom
    String getTdtSuspendFrom(){
        return this.tdtSuspendFrom;
    }
    
    // Setter method for tdtSuspendTo
    void setTdtSuspendTo(String tdtSuspendTo){
        this.tdtSuspendTo = tdtSuspendTo;
        setChanged();
    }
    // Getter method for tdtSuspendTo
    String getTdtSuspendTo(){
        return this.tdtSuspendTo;
    }
    
    // Setter method for txtTerminalId
    void setTxtTerminalId(String txtTerminalId){
        this.txtTerminalId = txtTerminalId;
        setChanged();
    }
    // Getter method for txtTerminalId
    String getTxtTerminalId(){
        return this.txtTerminalId;
    }
    
    // Setter method for txtTerminalName
    void setTxtTerminalName(String txtTerminalName){
        this.txtTerminalName = txtTerminalName;
        setChanged();
    }
    // Getter method for txtTerminalName
    String getTxtTerminalName(){
        return this.txtTerminalName;
    }
    
    // Setter method for txtIPAddress
    void setTxtIPAddress(String txtIPAddress){
        this.txtIPAddress = txtIPAddress;
        setChanged();
    }
    // Getter method for txtIPAddress
    String getTxtIPAddress(){
        return this.txtIPAddress;
    }
    
    // Setter method for txtMachineName
    void setTxtMachineName(String txtMachineName){
        this.txtMachineName = txtMachineName;
        setChanged();
    }
    // Getter method for txtMachineName
    String getTxtMachineName(){
        return this.txtMachineName;
    }
    
    // Setter method for txtTerminalDescription
    void setTxtTerminalDescription(String txtTerminalDescription){
        this.txtTerminalDescription = txtTerminalDescription;
        setChanged();
    }
    // Getter method for txtTerminalDescription
    String getTxtTerminalDescription(){
        return this.txtTerminalDescription;
    }
    
    // Setter method for tdtAccessToDate
    void setTdtAccessToDate(String tdtAccessToDate){
        this.tdtAccessToDate = tdtAccessToDate;
        setChanged();
    }
    // Getter method for tdtAccessToDate
    String getTdtAccessToDate(){
        return this.tdtAccessToDate;
    }
    
    // Setter method for tdtAccessFromDate
    void setTdtAccessFromDate(String tdtAccessFromDate){
        this.tdtAccessFromDate = tdtAccessFromDate;
        setChanged();
    }
    // Getter method for tdtAccessFromDate
    String getTdtAccessFromDate(){
        return this.tdtAccessFromDate;
    }
    
    /**
     * Getter for property cboForeignGroupId.
     * @return Value of property cboForeignGroupId.
     */
    public java.lang.String getCboForeignGroupId() {
        return cboForeignGroupId;
    }    
    
    /**
     * Setter for property cboForeignGroupId.
     * @param cboForeignGroupId New value of property cboForeignGroupId.
     */
    public void setCboForeignGroupId(java.lang.String cboForeignGroupId) {
        this.cboForeignGroupId = cboForeignGroupId;
    }
    
    /**
     * Getter for property cboForeignBranchGroup.
     * @return Value of property cboForeignBranchGroup.
     */
    public java.lang.String getCboForeignBranchGroup() {
        return cboForeignBranchGroup;
    }
    
    /**
     * Setter for property cboForeignBranchGroup.
     * @param cboForeignBranchGroup New value of property cboForeignBranchGroup.
     */
    public void setCboForeignBranchGroup(java.lang.String cboForeignBranchGroup) {
        this.cboForeignBranchGroup = cboForeignBranchGroup;
    }
    
    /**
     * Getter for property cbmForeignGroupId.
     * @return Value of property cbmForeignGroupId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmForeignGroupId() {
        return cbmForeignGroupId;
    }
    
    /**
     * Setter for property cbmForeignGroupId.
     * @param cbmForeignGroupId New value of property cbmForeignGroupId.
     */
    public void setCbmForeignGroupId(com.see.truetransact.clientutil.ComboBoxModel cbmForeignGroupId) {
        this.cbmForeignGroupId = cbmForeignGroupId;
    }
    
    /**
     * Getter for property cbmForeignBranchGroup.
     * @return Value of property cbmForeignBranchGroup.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmForeignBranchGroup() {
        return cbmForeignBranchGroup;
    }
    
    /**
     * Setter for property cbmForeignBranchGroup.
     * @param cbmForeignBranchGroup New value of property cbmForeignBranchGroup.
     */
    public void setCbmForeignBranchGroup(com.see.truetransact.clientutil.ComboBoxModel cbmForeignBranchGroup) {
        this.cbmForeignBranchGroup = cbmForeignBranchGroup;
    }
     public String getTxtCustomerId() {
        return txtCustomerId;
    }

    public void setTxtCustomerId(String txtCustomerId) {
        this.txtCustomerId = txtCustomerId;
    }
}