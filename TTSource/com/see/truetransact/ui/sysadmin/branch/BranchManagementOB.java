/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchManagementOB.java
 *
 * Created on October 2, 2003, 6:21 PM
 * Modified on Feb,09,2 004.
 */

package com.see.truetransact.ui.sysadmin.branch;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sysadmin.branch.BranchMasterTO;
import com.see.truetransact.transferobject.sysadmin.branch.BranchPhoneTO;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import org.apache.log4j.Logger;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
/** To manage BranchManagement UI
 * @author karthik
 * @ Modified By Hemant
 * @ Modified By Prasath
 *  Modifications
 *  1.Method fillDropDown Modified
 *  2. ComboBox Branch Group Added
 */
public class BranchManagementOB extends CObservable{
    
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboState = "";
    private String cboCountry = "";
    private String txtPinCode = "";
    private String txtBranchCode = "";
    private String txtBranchName = "";
    private String txtBranchShortName = "";
    private String tdtOpeningDate = "";
    private String txtBranchManagerID = "";
    private String txtRegionalOffice = "";
    private String txtMICRCode = "";
    private String txtBSRCode = "";
    private String cboFromHrs = "";
    private String cboFromMin = "";
    private String cboToHrs = "";
    private String cboToMin = "";
    private String cboContactType = "";
    private String cboBranchGroup = "";
    private String cboBranchGLGroup = "";
    private String txtContactNo = "";
    private String txtAreaCode = "";
    private String txtMaxCashStockBP = "";
    private String txtAvgCashStockBP = "";
    private boolean rdoBalanceLimitBP_Yes = false;
    private boolean rdoBalanceLimitBP_No = false;
    private String txtIPAppBC = "";
    private String txtPortAppBC = "";
    private String txtIPDataBC = "";
    private String txtPortDataBC = "";
    private String txtDBNameDataBC = "";
    private String txtDriverDataBC = "";
    private String txtURLDataBC = "";
    private String txtUserIDDataBC = "";
    private String txtPasswordDataBC = "";
    private String chkShift = "";
    private boolean rdoTransTime = false;
    private boolean rdoAuthTime = false;
    
    final BranchManagementRB objBranchManagementRB = new BranchManagementRB();
    //This variable is added to maintain the phone Id.
    private int phoneID;
    
    private ComboBoxModel cbmCountry;
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmState;
    private ComboBoxModel cbmContactType;
    private ComboBoxModel cbmWorkingHoursFrom;
    private ComboBoxModel cbmWorkingHoursTo;
    private ComboBoxModel cbmWorkingMinutesFrom;
    private ComboBoxModel cbmWorkingMinutesTo;
    private ComboBoxModel cbmBranchGroup;
    private ComboBoxModel cbmBranchGLGroup;
    private TableModel tblPhoneList;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(BranchManagementOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    //To manipulate Contact Nos table
    private List phoneList;
    
    //To send data
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap data;
    
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String lblDisplayBranMgrName = "";
    private String lblDisplayRegOfficeName = "";
    //To manage phone details & Branch Details
    private BranchPhoneTO objBranchPhoneTO;
    private BranchMasterTO objBranchMasterTO;
    Date curDate = null;
    
    /** Creates a new instance of BranchManagementOB */
    public BranchManagementOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "BranchManagementJNDI");
            map.put(CommonConstants.HOME, "com.see.truetransact.sysadmin.branch.BranchManagementHome");
            map.put(CommonConstants.REMOTE, "com.see.truetransact.sysadmin.branch.BranchManagement");
            initUIComboBoxModel();
            createTable();
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void initUIComboBoxModel(){
        cbmCountry = new ComboBoxModel();
        cbmCity = new ComboBoxModel();
        cbmState = new ComboBoxModel();
        cbmContactType = new ComboBoxModel();
        cbmBranchGroup = new ComboBoxModel();
        cbmBranchGLGroup = new ComboBoxModel();
    }
    
    /** To create Phone List table */
    private void createTable() throws Exception{
        final BranchManagementRB branchManagementRB = new BranchManagementRB();
        tblPhoneList = new TableModel(
        new Object [][] {
            
        },
        new String [] {
            branchManagementRB.getString("tblColumn1"), branchManagementRB.getString("tblColumn2"), branchManagementRB.getString("tblColumn3")
        }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
    }
    /*private void createTable() throws Exception{
        final BranchManagementRB branchManagementRB = new BranchManagementRB();
        final ArrayList phoneListColumn = new ArrayList();
        phoneListColumn.add(branchManagementRB.getString("tblColumn1"));
        phoneListColumn.add(branchManagementRB.getString("tblColumn2"));
        phoneListColumn.add(branchManagementRB.getString("tblColumn3"));
        tblPhoneList = new TableModel(null, phoneListColumn);
    }/**/
    
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    /**
     * To fill the comboBox with values
     */
    public void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.PHONETYPE");
        lookup_keys.add("HOURS");
        lookup_keys.add("MINUTES");
        lookup_keys.add("BRANCH_GROUP");
        lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("CUSTOMER.PHONETYPE"));
        cbmContactType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("HOURS"));
        cbmWorkingHoursFrom = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("HOURS"));
        cbmWorkingHoursTo = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("MINUTES"));
        cbmWorkingMinutesFrom = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get("MINUTES"));
        cbmWorkingMinutesTo = new ComboBoxModel(key,value);
        
        
        /**
         * Data for Branch Group ComboBox is taken from the GROUP_MASTER TABLE
         * SELECTS GROUP_ID AND GROUP_NAME FROM THE TABLE WHERE BRANCH_GROUP = Yes
         */
        lookupMap.put(CommonConstants.MAP_NAME,"getBranchGroup");
        lookupMap.put(CommonConstants.PARAMFORQUERY, null);
        
        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBranchGroup = new ComboBoxModel(key,value);
        
        
        lookupMap.put(CommonConstants.MAP_NAME,"getSelectGLGroups");
        lookupMap.put(CommonConstants.PARAMFORQUERY, null);
        
        keyValue = ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBranchGLGroup = new ComboBoxModel(key,value);
        
        makeComboBoxKeyValuesNull();
    }
    /**
     * To make ComboBox key values Null
     */
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookupMap = null;
        keyValue = null;
    }
    // Setter method for txtStreet
    void setTxtStreet(String txtStreet){
        this.txtStreet = txtStreet;
        setChanged();
    }
    // Getter method for txtStreet
    String getTxtStreet(){
        return this.txtStreet;
    }
    
    // Setter method for txtArea
    void setTxtArea(String txtArea){
        this.txtArea = txtArea;
        setChanged();
    }
    // Getter method for txtArea
    String getTxtArea(){
        return this.txtArea;
    }
    
    // Setter method for cboCity
    void setCboCity(String cboCity){
        this.cboCity = cboCity;
        setChanged();
    }
    // Getter method for cboCity
    String getCboCity(){
        return this.cboCity;
    }
    
    // Setter method for cboState
    void setCboState(String cboState){
        this.cboState = cboState;
        setChanged();
    }
    // Getter method for cboState
    String getCboState(){
        return this.cboState;
    }
    // Setter method for cboBranchGroup
    void setCboBranchGroup(String cboBranchGroup){
        this.cboBranchGroup = cboBranchGroup;
        setChanged();
    }
    // Getter method for cboBranchGroup
    String getCboBranchGroup(){
        return this.cboBranchGroup;
    }
    
    // Setter method for cboBranchGLGroup
    void setCboBranchGLGroup(String cboBranchGLGroup){
        this.cboBranchGLGroup = cboBranchGLGroup;
        setChanged();
    }
    // Getter method for cboBranchGroup
    String getCboBranchGLGroup(){
        return this.cboBranchGLGroup;
    }
    
    // Setter method for cboCountry
    void setCboCountry(String cboCountry){
        this.cboCountry = cboCountry;
        setChanged();
    }
    // Getter method for cboCountry
    String getCboCountry(){
        return this.cboCountry;
    }
    
    // Setter method for txtPinCode
    void setTxtPinCode(String txtPinCode){
        this.txtPinCode = txtPinCode;
        setChanged();
    }
    // Getter method for txtPinCode
    String getTxtPinCode(){
        return this.txtPinCode;
    }
    
    // Setter method for txtBranchCode
    void setTxtBranchCode(String txtBranchCode){
        this.txtBranchCode = txtBranchCode;
        setChanged();
    }
    // Getter method for txtBranchCode
    String getTxtBranchCode(){
        return this.txtBranchCode;
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
    
    // Setter method for txtBranchShortName
    void setTxtBranchShortName(String txtBranchShortName){
        this.txtBranchShortName = txtBranchShortName;
        setChanged();
    }
    // Getter method for txtBranchShortName
    String getTxtBranchShortName(){
        return this.txtBranchShortName;
    }
    
    // Setter method for tdtOpeningDate
    void setTdtOpeningDate(String tdtOpeningDate){
        this.tdtOpeningDate = tdtOpeningDate;
        setChanged();
    }
    // Getter method for tdtOpeningDate
    String getTdtOpeningDate(){
        return this.tdtOpeningDate;
    }
    
    // Setter method for txtBranchManagerID
    void setTxtBranchManagerID(String txtBranchManagerID){
        this.txtBranchManagerID = txtBranchManagerID;
        setChanged();
    }
    // Getter method for txtBranchManagerID
    String getTxtBranchManagerID(){
        return this.txtBranchManagerID;
    }
    
    // Setter method for txtRegionalOffice
    void setTxtRegionalOffice(String txtRegionalOffice){
        this.txtRegionalOffice = txtRegionalOffice;
        setChanged();
    }
    // Getter method for txtRegionalOffice
    String getTxtRegionalOffice(){
        return this.txtRegionalOffice;
    }
    
    // Setter method for txtMICRCode
    void setTxtMICRCode(String txtMICRCode){
        this.txtMICRCode = txtMICRCode;
        setChanged();
    }
    // Getter method for txtMICRCode
    String getTxtMICRCode(){
        return this.txtMICRCode;
    }
    
    // Setter method for txtBSRCode
    void setTxtBSRCode(String txtBSRCode){
        this.txtBSRCode = txtBSRCode;
        setChanged();
    }
    // Getter method for txtBSRCode
    String getTxtBSRCode(){
        return this.txtBSRCode;
    }
    
    // Setter method for cboFromHrs
    void setCboFromHrs(String cboFromHrs){
        this.cboFromHrs = cboFromHrs;
        setChanged();
    }
    // Getter method for cboFromHrs
    String getCboFromHrs(){
        return this.cboFromHrs;
    }
    
    // Setter method for cboFromMin
    void setCboFromMin(String cboFromMin){
        this.cboFromMin = cboFromMin;
        setChanged();
    }
    // Getter method for cboFromMin
    String getCboFromMin(){
        return this.cboFromMin;
    }
    
    // Setter method for cboToHrs
    void setCboToHrs(String cboToHrs){
        this.cboToHrs = cboToHrs;
        setChanged();
    }
    // Getter method for cboToHrs
    String getCboToHrs(){
        return this.cboToHrs;
    }
    
    // Setter method for cboToMin
    void setCboToMin(String cboToMin){
        this.cboToMin = cboToMin;
        setChanged();
    }
    // Getter method for cboToMin
    String getCboToMin(){
        return this.cboToMin;
    }
    
    // Setter method for cboContactType
    void setCboContactType(String cboContactType){
        this.cboContactType = cboContactType;
        setChanged();
    }
    // Getter method for cboContactType
    String getCboContactType(){
        return this.cboContactType;
    }
    
    // Setter method for txtContactNo
    void setTxtContactNo(String txtContactNo){
        this.txtContactNo = txtContactNo;
        setChanged();
    }
    // Getter method for txtContactNo
    String getTxtContactNo(){
        return this.txtContactNo;
    }
    
    // Setter method for txtAreaCode
    void setTxtAreaCode(String txtAreaCode){
        this.txtAreaCode = txtAreaCode;
        setChanged();
    }
    // Getter method for txtAreaCode
    String getTxtAreaCode(){
        return this.txtAreaCode;
    }
    
    // Setter method for txtMaxCashStockBP
    void setTxtMaxCashStockBP(String txtMaxCashStockBP){
        this.txtMaxCashStockBP = txtMaxCashStockBP;
        setChanged();
    }
    // Getter method for txtMaxCashStockBP
    String getTxtMaxCashStockBP(){
        return this.txtMaxCashStockBP;
    }
    
    // Setter method for txtAvgCashStockBP
    void setTxtAvgCashStockBP(String txtAvgCashStockBP){
        this.txtAvgCashStockBP = txtAvgCashStockBP;
        setChanged();
    }
    // Getter method for txtAvgCashStockBP
    String getTxtAvgCashStockBP(){
        return this.txtAvgCashStockBP;
    }
    
    // Setter method for rdoBalanceLimitBP_Yes
    void setRdoBalanceLimitBP_Yes(boolean rdoBalanceLimitBP_Yes){
        this.rdoBalanceLimitBP_Yes = rdoBalanceLimitBP_Yes;
        setChanged();
    }
    // Getter method for rdoBalanceLimitBP_Yes
    boolean getRdoBalanceLimitBP_Yes(){
        return this.rdoBalanceLimitBP_Yes;
    }
    
    // Setter method for rdoBalanceLimitBP_No
    void setRdoBalanceLimitBP_No(boolean rdoBalanceLimitBP_No){
        this.rdoBalanceLimitBP_No = rdoBalanceLimitBP_No;
        setChanged();
    }
    // Getter method for rdoBalanceLimitBP_No
    boolean getRdoBalanceLimitBP_No(){
        return this.rdoBalanceLimitBP_No;
    }
    
    // Setter method for txtIPAppBC
    void setTxtIPAppBC(String txtIPAppBC){
        this.txtIPAppBC = txtIPAppBC;
        setChanged();
    }
    // Getter method for txtIPAppBC
    String getTxtIPAppBC(){
        return this.txtIPAppBC;
    }
    
    // Setter method for txtPortAppBC
    void setTxtPortAppBC(String txtPortAppBC){
        this.txtPortAppBC = txtPortAppBC;
        setChanged();
    }
    // Getter method for txtPortAppBC
    String getTxtPortAppBC(){
        return this.txtPortAppBC;
    }
    
    // Setter method for txtIPDataBC
    void setTxtIPDataBC(String txtIPDataBC){
        this.txtIPDataBC = txtIPDataBC;
        setChanged();
    }
    // Getter method for txtIPDataBC
    String getTxtIPDataBC(){
        return this.txtIPDataBC;
    }
    
    // Setter method for txtPortDataBC
    void setTxtPortDataBC(String txtPortDataBC){
        this.txtPortDataBC = txtPortDataBC;
        setChanged();
    }
    // Getter method for txtPortDataBC
    String getTxtPortDataBC(){
        return this.txtPortDataBC;
    }
    
    // Setter method for txtDBNameDataBC
    void setTxtDBNameDataBC(String txtDBNameDataBC){
        this.txtDBNameDataBC = txtDBNameDataBC;
        setChanged();
    }
    // Getter method for txtDBNameDataBC
    String getTxtDBNameDataBC(){
        return this.txtDBNameDataBC;
    }
    
    // Setter method for txtDriverDataBC
    void setTxtDriverDataBC(String txtDriverDataBC){
        this.txtDriverDataBC = txtDriverDataBC;
        setChanged();
    }
    // Getter method for txtDriverDataBC
    String getTxtDriverDataBC(){
        return this.txtDriverDataBC;
    }
    
    // Setter method for txtURLDataBC
    void setTxtURLDataBC(String txtURLDataBC){
        this.txtURLDataBC = txtURLDataBC;
        setChanged();
    }
    // Getter method for txtURLDataBC
    String getTxtURLDataBC(){
        return this.txtURLDataBC;
    }
    
    // Setter method for txtUserIDDataBC
    void setTxtUserIDDataBC(String txtUserIDDataBC){
        this.txtUserIDDataBC = txtUserIDDataBC;
        setChanged();
    }
    // Getter method for txtUserIDDataBC
    String getTxtUserIDDataBC(){
        return this.txtUserIDDataBC;
    }
    
    // Setter method for txtPasswordDataBC
    void setTxtPasswordDataBC(String txtPasswordDataBC){
        this.txtPasswordDataBC = txtPasswordDataBC;
        setChanged();
    }
    // Getter method for txtPasswordDataBC
    String getTxtPasswordDataBC(){
        return this.txtPasswordDataBC;
    }
    
    void setCbmCountry(ComboBoxModel cbmCountry){
        this.cbmCountry = cbmCountry;
        setChanged();
    }
    ComboBoxModel getCbmCountry(){
        return this.cbmCountry;
    }
    void setCbmBranchGroup(ComboBoxModel cbmBranchGroup){
        this.cbmBranchGroup = cbmBranchGroup;
        setChanged();
    }
    ComboBoxModel getCbmBranchGroup(){
        return this.cbmBranchGroup;
    }
    void setCbmBranchGLGroup(ComboBoxModel cbmBranchGLGroup){
        this.cbmBranchGLGroup = cbmBranchGLGroup;
        setChanged();
    }
    ComboBoxModel getCbmBranchGLGroup(){
        return this.cbmBranchGLGroup;
    }
    
    void setCbmWorkingHrsFrom(ComboBoxModel cbmWorkingHoursFrom){
        this.cbmWorkingHoursFrom = cbmWorkingHoursFrom;
        setChanged();
    }
    ComboBoxModel getCbmWorkingHrsFrom(){
        return this.cbmWorkingHoursFrom;
    }
    void setCbmWorkingHrsTo(ComboBoxModel cbmWorkingHoursTo){
        this.cbmWorkingHoursTo = cbmWorkingHoursTo;
        setChanged();
    }
    ComboBoxModel getCbmWorkingHrsTo(){
        return this.cbmWorkingHoursTo;
    }
    void setCbmWorkingMinFrom(ComboBoxModel cbmWorkingMinutesFrom){
        this.cbmWorkingMinutesFrom = cbmWorkingMinutesFrom;
        setChanged();
    }
    ComboBoxModel getCbmWorkingMinFrom(){
        return this.cbmWorkingMinutesFrom;
    }
    
    void setCbmWorkingMinTo(ComboBoxModel cbmWorkingMinutesTo){
        this.cbmWorkingMinutesTo = cbmWorkingMinutesTo;
        setChanged();
    }
    ComboBoxModel getCbmWorkingMinTo(){
        return this.cbmWorkingMinutesTo;
    }
    
    void setCbmCity(ComboBoxModel cbmCity){
        this.cbmCity = cbmCity;
        setChanged();
    }
    ComboBoxModel getCbmCity(){
        return this.cbmCity;
    }
    
    void setCbmState(ComboBoxModel cbmState){
        this.cbmState = cbmState;
        setChanged();
    }
    ComboBoxModel getCbmState(){
        return this.cbmState;
    }
    
    void setCbmContactType(ComboBoxModel cbmContactType){
        this.cbmContactType = cbmContactType;
        setChanged();
    }
    ComboBoxModel getCbmContactType(){
        return this.cbmContactType;
    }
    
    void setTblPhoneList(TableModel tblPhoneList){
        this.tblPhoneList = tblPhoneList;
        setChanged();
    }
    TableModel getTblPhoneList(){
        return this.tblPhoneList;
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
    
    /** To add phone data */
    public void addPhoneList(){
        try{
            //If phoneList not exist already, create one
            if(phoneList == null){
                phoneList = new ArrayList();
            }
            
            objBranchPhoneTO = new BranchPhoneTO();
            objBranchPhoneTO.setPhoneId(phoneID);
            objBranchPhoneTO.setBranchCode(this.getTxtBranchCode());
            objBranchPhoneTO.setContactType(CommonUtil.convertObjToStr((String)this.cbmContactType.getKeyForSelected()));
            objBranchPhoneTO.setContactNo(this.getTxtContactNo());
            
            //phoneList.put(new Integer(objBranchPhoneTO.getPhoneId()),objBranchPhoneTO);
            updateTblPhoneList(objBranchPhoneTO);
            //objBranchPhoneTO = null;
            
            resetPhoneDetails();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate phone data to phone list table */
    private void updateTblPhoneList(BranchPhoneTO objBranchPhoneTO) throws Exception{
        //To update the phone list table if a phone data is changed
        int rows = 0;
        
        if(phoneList!=null)
            rows = phoneList.size();
        
        for(int i=0; i < rows;i++){
            if( objBranchPhoneTO.getPhoneId() == ((BranchPhoneTO)phoneList.get(i)).getPhoneId()){
                tblPhoneList.setValueAt(objBranchPhoneTO.getContactType(), i, 1);
                tblPhoneList.setValueAt(objBranchPhoneTO.getContactNo(), i, 2);
                phoneList.set(i,objBranchPhoneTO);
                return;
            }
        }
        ArrayList phoneRow = new ArrayList();
        phoneRow.add(Integer.toString(objBranchPhoneTO.getPhoneId()));
        phoneRow.add(objBranchPhoneTO.getContactType());
        phoneRow.add(objBranchPhoneTO.getContactNo());
        tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
        phoneList.add(objBranchPhoneTO);
        
    }
    
    /*
    private void updateTblPhoneList(BranchPhoneTO objBranchPhoneTO) throws Exception{
        Object selectedRow = null;
        boolean rowExists = false;
        //To update the phone list table if a phone data is changed
     
        for(int i = tblPhoneList.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblPhoneList.getDataArrayList().get(j)).get(0);
            if( objBranchPhoneTO.getPhoneId() == Integer.parseInt(selectedRow.toString()) ){
                tblPhoneList.setValueAt(objBranchPhoneTO.getContactNo(), j, 2);
                rowExists = true;
            }
        }
        //To add a new phone data to the phone list table
        if(!rowExists){
            final ArrayList phoneRow = new ArrayList();
            phoneRow.add(new Integer(objBranchPhoneTO.getPhoneId()));
            phoneRow.add(objBranchPhoneTO.getContactType());
            phoneRow.add(objBranchPhoneTO.getContactNo());
            tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
        }
    }/**/
    
    /** To Set the branch details */
    private void setBranchDetails() throws Exception{
        objBranchMasterTO = new BranchMasterTO();
        objBranchMasterTO.setBranchCode(getTxtBranchCode());
        objBranchMasterTO.setBranchName(getTxtBranchName());
        objBranchMasterTO.setStreet(getTxtStreet());
        objBranchMasterTO.setArea(getTxtArea());
        objBranchMasterTO.setCity(CommonUtil.convertObjToStr((String)this.cbmCity.getKeyForSelected()));
        objBranchMasterTO.setState(CommonUtil.convertObjToStr((String)this.cbmState.getKeyForSelected()));
        objBranchMasterTO.setPinCode(getTxtPinCode());
        objBranchMasterTO.setCountryCode(CommonUtil.convertObjToStr((String)getCbmCountry().getKeyForSelected()));
        objBranchMasterTO.setAreaCode(getTxtAreaCode());
        
        //Added components.
        objBranchMasterTO.setIpAddr(getTxtIPAppBC());
        objBranchMasterTO.setPort(CommonUtil.convertObjToDouble(getTxtPortAppBC()));
        if(getRdoBalanceLimitBP_Yes())
            objBranchMasterTO.setChkBalanceLimit("Y");
        else
            objBranchMasterTO.setChkBalanceLimit("N");
        if(isRdoAuthTime())
        {
            objBranchMasterTO.setRdoTransAuth("A");
        }
        if(isRdoTransTime())
        {
            objBranchMasterTO.setRdoTransAuth("T");
        }
        if(getChkShift().equals("Y"))
        {
            objBranchMasterTO.setChkShift("Y");
        }
        else
        {
            objBranchMasterTO.setChkShift("N");
        }
        objBranchMasterTO.setMaxCashStock(CommonUtil.convertObjToDouble(getTxtMaxCashStockBP()));
        objBranchMasterTO.setAvgCashStock(CommonUtil.convertObjToDouble(getTxtAvgCashStockBP()));
        objBranchMasterTO.setBranchDbName(CommonUtil.convertObjToStr(getTxtDBNameDataBC()));
        objBranchMasterTO.setBranchDbIp(CommonUtil.convertObjToStr(getTxtIPDataBC()));
        objBranchMasterTO.setBranchDbPort(CommonUtil.convertObjToDouble(getTxtPortDataBC()));
        objBranchMasterTO.setDbUserId(CommonUtil.convertObjToStr(getTxtUserIDDataBC()));
        objBranchMasterTO.setDbPassword(CommonUtil.convertObjToStr(getTxtPasswordDataBC()));
        objBranchMasterTO.setDbDriver(CommonUtil.convertObjToStr(getTxtDriverDataBC()));
        objBranchMasterTO.setDbUrl(CommonUtil.convertObjToStr(getTxtURLDataBC()));
        
        objBranchMasterTO.setBranchShortName(CommonUtil.convertObjToStr(getTxtBranchShortName()));
        Date OpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtOpeningDate()));
        if(OpDt != null){
        Date opdtDate = (Date)curDate.clone();
        opdtDate.setDate(OpDt.getDate());
        opdtDate.setMonth(OpDt.getMonth());
        opdtDate.setYear(OpDt.getYear());
        
//        objBranchMasterTO.setOpeningDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtOpeningDate())));
        objBranchMasterTO.setOpeningDt(opdtDate);
        }else{
            objBranchMasterTO.setOpeningDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtOpeningDate())));
        }
        
        objBranchMasterTO.setBranchManagerNo(CommonUtil.convertObjToStr(getTxtBranchManagerID()));
        objBranchMasterTO.setRo(CommonUtil.convertObjToStr(getTxtRegionalOffice()));
        objBranchMasterTO.setMicrCode(CommonUtil.convertObjToStr(getTxtMICRCode()));
        objBranchMasterTO.setBsrCode(CommonUtil.convertObjToStr(getTxtBSRCode()));
        objBranchMasterTO.setBranchGroup(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmBranchGroup.getKeyForSelected())));
        objBranchMasterTO.setGlGroupId(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmBranchGLGroup.getKeyForSelected())));
        objBranchMasterTO.setWorkingHoursFrom(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmWorkingHoursFrom.getKeyForSelected())));
        objBranchMasterTO.setWorkingMinsFrom(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmWorkingMinutesFrom.getKeyForSelected())));
        objBranchMasterTO.setWorkingMinsTo(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmWorkingMinutesTo.getKeyForSelected())));
        objBranchMasterTO.setWorkingHoursTo(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(this.cbmWorkingHoursTo.getKeyForSelected())));
        objBranchMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objBranchMasterTO.setStatusDt(curDate);
    }
    
    /** To perform the necessary tasks */
    public void doAction() {
        try {
            //To perform INSERT, UPDATE & DELETE operations
            if( this.getActionType() != ClientConstants.ACTIONTYPE_CANCEL ){
                setBranchDetails();
                setTOStatus();
                setData();
                System.out.println("data======================="+data);
                HashMap proxyResultMap = proxy.execute(data, map);
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
                resetForm();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To set the Transfer object status & command variables based on action type */
    private void setTOStatus() throws Exception{
        switch (this.getActionType()) {
            case ClientConstants.ACTIONTYPE_NEW:
                setTOStatus(CommonConstants.STATUS_CREATED, CommonConstants.TOSTATUS_INSERT);
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                setTOStatus(CommonConstants.STATUS_MODIFIED, CommonConstants.TOSTATUS_UPDATE);
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                setTOStatus(CommonConstants.STATUS_DELETED, CommonConstants.TOSTATUS_DELETE);
                break;
            default:
                
        }
    }
    
    /** To set the Transfer object status & command variables based on parameters */
    private void setTOStatus(String status,String command) throws Exception{
        objBranchMasterTO.setStatus(status);
        objBranchMasterTO.setCommand(command);
    }
    
    /** To set Branch details & Phone details to the HashMap which will be sent to
     * server for further processing
     */
    private void setData() throws Exception{
        data = new HashMap();
        data.put("BranchMasterTO", objBranchMasterTO);
        data.put("BranchPhoneTO", phoneList);
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
    }
    
    private void displayPhoneList(){
        if(phoneList!=null){
            int size = phoneList.size();
            for(int i=0;i<size;i++){
                BranchPhoneTO objBP = (BranchPhoneTO)phoneList.get(i);                
            }
        }
    }
    
    /** To get the data from server for a particular Branch & populate */
    public void getBranchData(HashMap whereMap) {
        try{
            data = (HashMap)proxy.executeQuery(whereMap,map);
            objBranchMasterTO = (BranchMasterTO)data.get("BranchMasterTO");
            populateBranchData();
            
            phoneList = (List)data.get("BranchPhoneTO");
            populatePhoneTable();
            
            data = null;
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    /** To populate Branch Data to the fields in the screen */
    private void populateBranchData() throws Exception{
        setTxtBranchCode(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchCode()));
        setTxtBranchName(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchName()));
        setTxtStreet(CommonUtil.convertObjToStr(objBranchMasterTO.getStreet()));
        setTxtArea(CommonUtil.convertObjToStr(objBranchMasterTO.getArea()));
        setCboCountry(CommonUtil.convertObjToStr(getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getCountryCode()))));
        setCboState(CommonUtil.convertObjToStr(getCbmState().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getState()))));
        setCboCity(CommonUtil.convertObjToStr(getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getCity()))));
        setTxtPinCode(CommonUtil.convertObjToStr(objBranchMasterTO.getPinCode()));
        setTxtAreaCode(CommonUtil.convertObjToStr(objBranchMasterTO.getAreaCode()));
        
        setTxtIPAppBC(CommonUtil.convertObjToStr(objBranchMasterTO.getIpAddr()));
        setTxtPortAppBC(CommonUtil.convertObjToStr(Integer.toString(objBranchMasterTO.getPort().intValue())));
        if(objBranchMasterTO.getChkBalanceLimit().equals("Y"))
            setRdoBalanceLimitBP_Yes(true);
        else
            setRdoBalanceLimitBP_No(true);
        
        setTxtMaxCashStockBP(CommonUtil.convertObjToStr(objBranchMasterTO.getMaxCashStock().toString()));
        setTxtAvgCashStockBP(CommonUtil.convertObjToStr(objBranchMasterTO.getAvgCashStock().toString()));
        setTxtDBNameDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchDbName()));
        setTxtIPDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchDbIp()));
        setTxtPortDataBC(CommonUtil.convertObjToStr(Integer.toString(objBranchMasterTO.getBranchDbPort().intValue())));
        setTxtUserIDDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getDbUserId()));
        setTxtPasswordDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getDbPassword()));
        setTxtDriverDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getDbDriver()));
        setTxtURLDataBC(CommonUtil.convertObjToStr(objBranchMasterTO.getDbUrl()));
        
        setTxtBranchShortName(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchShortName()));
        setTdtOpeningDate(CommonUtil.convertObjToStr(DateUtil.getStringDate((java.util.Date)objBranchMasterTO.getOpeningDt())));
        setTxtBranchManagerID(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchManagerNo()));
        setTxtRegionalOffice(CommonUtil.convertObjToStr(objBranchMasterTO.getRo()));
        setTxtMICRCode(CommonUtil.convertObjToStr(objBranchMasterTO.getMicrCode()));
        setTxtBSRCode(CommonUtil.convertObjToStr(objBranchMasterTO.getBsrCode()));
        setCboBranchGroup(CommonUtil.convertObjToStr(getCbmBranchGroup().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getBranchGroup()))));
        setCboBranchGLGroup(CommonUtil.convertObjToStr(getCbmBranchGLGroup().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getGlGroupId()))));
        setCboFromHrs(CommonUtil.convertObjToStr(getCbmWorkingHrsFrom().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getWorkingHoursFrom()))));
        setCboFromMin(CommonUtil.convertObjToStr(getCbmWorkingMinFrom().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getWorkingMinsFrom()))));
        setCboToMin(CommonUtil.convertObjToStr(getCbmWorkingMinTo().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getWorkingMinsTo()))));
        setCboToHrs(CommonUtil.convertObjToStr(getCbmWorkingHrsTo().getDataForKey(CommonUtil.convertObjToStr(objBranchMasterTO.getWorkingHoursTo()))));
    }
    // To validate the Working Time
    public String checkForTime(){
        int from,to,start,end;
        String returnStr = new String("Yes");
        try{
            if( ( !( getCboFromHrs().equals("") ) && !( getCboToHrs().equals("") ) ) ){
                from = Integer.parseInt(getCboFromHrs());
                to = Integer.parseInt(getCboToHrs());
                if(from > to){
                    returnStr =  setWaringMessage();
                }else if(from == to){
                    if( (! ( getCboFromMin().equals("") ) && !( getCboToMin().equals("") ) ) ){
                        start = Integer.parseInt(getCboFromMin());
                        end = Integer.parseInt(getCboToMin());
                        if(start > end){
                            returnStr =  setWaringMessage();
                        }else if(start == end){
                            returnStr = setWaringMessage();
                        }
                    }
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnStr;
    }
    
    /* To set the Warning Message */
    private String setWaringMessage() throws Exception {
        String warningMessage = new String();
        warningMessage = objBranchManagementRB.getString("WarningMessage");
        return warningMessage;
    }
    public HashMap checkingDuplicationForBranchCode(String branchCode){
        final int ZERO =0;
        int COUNT = 0;
        HashMap returnMap = new HashMap();
        returnMap.put("IntValue", "1");
        try {
            /* If the selected value in the combobox is not null proceed */
            if ( !(branchCode.equals("") ) ) {
                HashMap where = new HashMap();
                where.put("BRANCH_CODE",branchCode);
                List branchCodeCount = ClientUtil.executeQuery("countBranchCode", where);
                where = null;
                COUNT = CommonUtil.convertObjToDouble(((HashMap)branchCodeCount.get(0)).get("COUNT")).intValue();
                /* If count is greater than zero then there is a duplication of branchCode */
                if( COUNT > ZERO ) {
                    returnMap = showAlertWindow();
                }else{
                    returnMap.put("IntValue", "1");
                }
            }
            
        }catch (Exception e){
            parseException.logException(e,true);
        }
        
        return returnMap;
    }
    /* If there is duplication Alert Message */
    private HashMap showAlertWindow() throws Exception {
        HashMap returnMap = new HashMap();
        int option = 1;
        String[] options = {objBranchManagementRB.getString("cDialogOK")};
        option = COptionPane.showOptionDialog(null, objBranchManagementRB.getString("WarningBranchCode"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        returnMap.put("IntValue", String.valueOf(option));
        returnMap.put("WarningMessage", (String)objBranchManagementRB.getString("WarningBranchCode"));
        return returnMap;
    }
    /*private void populatePhoneTableNew(){
        ArrayList phoneRow = null;
        final int phoneListSize = phoneList.size();
        for(int i = 1; i <= phoneListSize; i++){
            objBranchPhoneTO = (BranchPhoneTO)phoneList.get(new Integer(i));
            phoneRow = new ArrayList();
            phoneRow.add(new Integer(i));
            phoneRow.add(objBranchPhoneTO.getContactType());
            phoneRow.add(objBranchPhoneTO.getContactNo());
            tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
            phoneRow = null;
        }
        objBranchPhoneTO = null;
    }*/
    
    /** To populate phone data into the Phone List table */
    private void populatePhoneTable() throws Exception{
        ArrayList phoneRow = null;
        if(phoneList != null && phoneList.size()>0){
            final int phoneListSize = phoneList.size();
            for(int i = 0; i < phoneListSize; i++){
                objBranchPhoneTO = (BranchPhoneTO)phoneList.get(i);
                objBranchPhoneTO.setPhoneId(i+1);
                phoneRow = new ArrayList();
                phoneRow.add(Integer.toString(objBranchPhoneTO.getPhoneId()));
                phoneRow.add(objBranchPhoneTO.getContactType());
                phoneRow.add(objBranchPhoneTO.getContactNo());
                tblPhoneList.insertRow(tblPhoneList.getRowCount(),phoneRow);
            }
        }
    }
    
    /** To delete the selected phone row */
    // New implementation to delete contact No detail from the phoneList.
    public void deletePhoneDetails(){
        try{
            if(phoneList!=null && phoneList.size()>0){
                int size = phoneList.size();
                int i=0;
                while(i<size){
                    if(((BranchPhoneTO)phoneList.get(i)).getPhoneId()==phoneID){
                        phoneList.remove(i);
                        tblPhoneList.removeRow(i);
                        break;
                    }
                    i++;
                }
                
            }
            rearrangePhID();
            resetOnDeletePhone();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    /*public void deletePhoneDetails(int row){
        try{
     
            final Object selectedRow = ((ArrayList)tblPhoneList.getDataArrayList().get(row)).get(0);
            phoneList.remove(new Integer(selectedRow.toString()));
            resetOnDeletePhone();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }/**/
    
    private void resetOnDeletePhone() throws Exception{
        resetPhoneDetails();
        resetPhoneDetailsTable();
        populatePhoneTable();
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void resetPhoneDetails() {
        this.setCboContactType("");
        this.setTxtContactNo("");
    }
    
    private void resetPhoneDetailsTable() throws Exception{
        for(int i = tblPhoneList.getRowCount(); i > 0; i--){
            tblPhoneList.removeRow(0);
        }
    }
    
    private void resetBranchDetails() throws Exception{
        this.setTxtBranchCode("");
        this.setTxtBranchName("");
        this.setTxtStreet("");
        this.setTxtArea("");
        this.setCboCountry("");
        this.setCboState("");
        this.setCboBranchGroup("");
        this.setCboBranchGLGroup("");
        this.setCboCity("");
        this.setTxtPinCode("");
        this.setTxtAreaCode("");
        //to Add some additional components.
        setTxtIPAppBC("");
        setTxtPortAppBC("");
        setRdoBalanceLimitBP_Yes(false);
        setRdoBalanceLimitBP_No(false);
        setTxtMaxCashStockBP("");
        setTxtAvgCashStockBP("");
        setTxtDBNameDataBC("");
        setTxtIPDataBC("");
        setTxtPortDataBC("");
        setTxtUserIDDataBC("");
        setTxtPasswordDataBC("");
        setTxtDriverDataBC("");
        setTxtURLDataBC("");
        // newly added components
        setTxtBranchShortName("");
        setTdtOpeningDate("");
        setTxtBranchManagerID("");
        setTxtRegionalOffice("");
        setTxtMICRCode("");
        setTxtBSRCode("");
        setCboFromHrs("");
        setCboFromMin("");
        setCboToMin("");
        setCboToHrs("");
        setChkShift("");
        setRdoAuthTime(false);
        setRdoTransTime(false);
        this.lblDisplayBranMgrName = "";
        this.lblDisplayRegOfficeName = "";
    }
    
    public void resetForm(){
        try{
            resetPhoneDetails();
            resetPhoneDetailsTable();
            resetBranchDetails();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate the selected phone row to the phone data fields */
    public void populatePhoneDetails(int row){
        try{
            objBranchPhoneTO = (BranchPhoneTO)phoneList.get(row);
            /*
            final Object selectedRow = ((ArrayList)tblPhoneList.getDataArrayList().get(row)).get(0);
            final int phoneId = Integer.parseInt(selectedRow.toString());
            objBranchPhoneTO = (BranchPhoneTO)phoneList.get(new Integer(phoneId));
            /**/
            populatePhoneData(objBranchPhoneTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate the given phone object to the phone data fields */
    private void populatePhoneData(BranchPhoneTO objBranchPhoneTO) throws Exception{
        this.setPhoneID(objBranchPhoneTO.getPhoneId());
        this.setCboContactType((String) getCbmContactType().getDataForKey(CommonUtil.convertObjToStr(objBranchPhoneTO.getContactType())));
        this.setTxtContactNo(CommonUtil.convertObjToStr(objBranchPhoneTO.getContactNo()));
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
    // Setter method for lblDisplayBranMgrName
    void setBranchManagerName(String branchManagerId){
        if (branchManagerId != null && branchManagerId.length()>0 && !branchManagerId.equals("")) {
            HashMap where = new HashMap();
            where.put("EMPLOYEE_CODE",branchManagerId);
            List list = (List)ClientUtil.executeQuery("getDisplayBranchManagerID",where);
            if (list != null) {
                if(list.size()>0) {
                    this.lblDisplayBranMgrName = (String)((HashMap)list.get(0)).get("NAME");
                }
            }
        } else {
            this.lblDisplayBranMgrName = "";
        }
        setChanged();
    }
    // Getter method for lblDisplayBranMgrName
    String getBranchManagerName(){
        return this.lblDisplayBranMgrName;
    }
    // Setter method for lblDisplayRegOfficeName
    void setRegionalOfficerName(String regOfficeName){
        if (regOfficeName != null && regOfficeName.length()>0 && !regOfficeName.equals("")) {
            HashMap where = new HashMap();
            where.put("BRANCH_CODE",regOfficeName);
            List list = (List)ClientUtil.executeQuery("getDisplayRegionalOffice",where);
            if (list != null) {
                if(list.size()>0) {
                    this.lblDisplayRegOfficeName = (String)((HashMap)list.get(0)).get("BRANCH NAME");                    
                }
            }
        } else {
            this.lblDisplayRegOfficeName = "";
        }
        setChanged();
    }
    // Getter method for lblDisplayRegOfficeName
    String getRegionalOfficerName(){
        return this.lblDisplayRegOfficeName;
    }
    
    public String getBankOpeningDate(){
        String strBankOpeningDate = "";
        try{
            java.util.List list = ClientUtil.executeQuery("getBankOpeningDate", new HashMap());
            HashMap resultMap = null;
            if (list != null && list.size() > 0){
                resultMap = (HashMap) list.get(0);
                strBankOpeningDate = DateUtil.getStringDate((java.util.Date) resultMap.get("OPENING_DT"));
            }
            resultMap = null;
            list = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return strBankOpeningDate;
    }
    
    public void displayOpeningDateWarnMsg(){
        int option = 1;
        String[] options = {objBranchManagementRB.getString("cDialogOK")};
        option = COptionPane.showOptionDialog(null, objBranchManagementRB.getString("WarningOpeningDate"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
    }
    
    /**
     * Contact Type and Contact No Should Not be Duplicated
     */
    public boolean validateContactDetails(String contactType, String contactNo, int selectedRow) {
        boolean valid = false;
        if ((contactType != null && !contactType.equals("") && contactType.length() >0)
        && (contactNo != null && !contactNo.equals("") && contactNo.length() >0)) {
            if (phoneList != null) {
                int size = phoneList.size();
                if (size >0) {
                    for (int i=0;i<size;i++) {
                        String typeKey = ((BranchPhoneTO)phoneList.get(i)).getContactType();
                        String number = ((BranchPhoneTO)phoneList.get(i)).getContactNo();
                        String objTOContactType = (String) getCbmContactType().getDataForKey(CommonUtil.convertObjToStr(typeKey));
                        if ( objTOContactType.equals(contactType) && 
                                number.equals(contactNo) &&  
                                (selectedRow == -1 || (selectedRow != -1 && selectedRow != i))) {
                            valid = true;
                            break;
                        }
                        typeKey = null;
                        number = null;
                        objTOContactType = null;
                    }
                }
            }
        }
        return valid;
    }
    // This method 'll generate new PhoneID every time the new contact no button is clicked.
    public void newPhoneID(){
        try{
            resetPhoneDetails();
            if(phoneList!=null){
                int size = phoneList.size();
                if(size > 0 ){
                    setPhoneID(((BranchPhoneTO)phoneList.get(size-1)).getPhoneId() + 1);
                }else{
                    setPhoneID(1);
                }
            }else{
                setPhoneID(1);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Getter for property phoneID.
     * @return Value of property phoneID.
     *
     */
    public int getPhoneID() {
        return phoneID;
    }
    
    /** Setter for property phoneID.
     * @param phoneID New value of property phoneID.
     *
     */
    public void setPhoneID(int phoneID) {
        this.phoneID = phoneID;
    }
    
    private void rearrangePhID(){
        int size =0;
        if(phoneList != null)
            size = phoneList.size();
        
        int i=0;
        while(i<size){
            ((BranchPhoneTO)phoneList.get(i)).setPhoneId(i+1);
            tblPhoneList.setValueAt(Integer.toString(i+1), i, 0);
            i++;
        }
    }
    public void nullifyPhoneList(){
        phoneList = null;
        
    }
    
    /**
     * Getter for property chkShift.
     * @return Value of property chkShift.
     */
    public String getChkShift() {
        return chkShift;
    }
    
    /**
     * Setter for property chkShift.
     * @param chkShift New value of property chkShift.
     */
    public void setChkShift(String chkShift) {
        this.chkShift = chkShift;
    }
    
    /**
     * Getter for property rdoTransTime.
     * @return Value of property rdoTransTime.
     */
    public boolean isRdoTransTime() {
        return rdoTransTime;
    }
    
    /**
     * Setter for property rdoTransTime.
     * @param rdoTransTime New value of property rdoTransTime.
     */
    public void setRdoTransTime(boolean rdoTransTime) {
        this.rdoTransTime = rdoTransTime;
    }
    
    /**
     * Getter for property rdoAuthTime.
     * @return Value of property rdoAuthTime.
     */
    public boolean isRdoAuthTime() {
        return rdoAuthTime;
    }
    
    /**
     * Setter for property rdoAuthTime.
     * @param rdoAuthTime New value of property rdoAuthTime.
     */
    public void setRdoAuthTime(boolean rdoAuthTime) {
        this.rdoAuthTime = rdoAuthTime;
    }
    
    /**
     * Getter for property rdoTransTime.
     * @return Value of property rdoTransTime.
     */
   
    
}
