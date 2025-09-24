/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.locker.lockerconfig;

import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.locker.lockerconfig.LockerConfigTO;
import com.see.truetransact.transferobject.locker.lockerconfig.LockerConfigDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.LinkedHashMap;
import java.util.Date;

/**
 *
 * @author Ashok Vijayakumar
 */

public class LockerConfigOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
//    private String txtTokenConfigId = "";
//    private String cboTokenType = "";
//    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
//    private String txtSeriesNo = "";
//    private String txtStartingTokenNo = "";
//    private String txtEndingTokenNo = "";
//    private String txtNoOfTokens = "";
    /** Other Varibales Declartion */
    private final String PRODID = "PRODID";
    private final   String  SLNO = "SLNO";
    private final   String  FROMLOCNO = "FROMLOCNO";
    private final   String  TOLOCNO = "TOLOCNO";
    private final   String  TOTLOCNO = "TOTLOCNO";
    private final   String  MASTRKEY = "MASTRKEY";
    private final   String  LOCKEY = "LOCKEY";
    private final   String  COMMAND = "COMMAND";
    
    private final   String  OPTION = "OPTION";
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    
     private final   String  KEY = "KEY";

    
    private String txtProdId = "";
    private String txtFromLocNo= "";
    private String txtToLocNo= "";
    private String txtNoOfLockers= "";
    private String txtMasterKey= "";
    private String txtLockerKey= "";
    private String lblProdDescVal= "";
    
//    private  LockerConfigTO objLockerConfigTO;
    
    private boolean isTableClicked = false;
    
    private ArrayList poaEachTabRecords;
    private ArrayList poaAllTabRecords; 
     private HashMap poaEachRecords;
     private EnhancedTableModel tblPoATab;
     private final   ArrayList poaTabTitle = new ArrayList();
     private TableUtil tableUtilPoA = new TableUtil();
      private LinkedHashMap poaAllRecords = new LinkedHashMap();  

     private boolean isEditMode = false;
     private ArrayList tblRowList;
//    private boolean isTableClicked = false;
    
    
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static LockerConfigOB objLockerConfigOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(LockerConfigOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  LockerConfigTO objLockerConfigTO;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private Date currDt = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final   java.util.ResourceBundle objLockerConfigRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockerconfig.LockerConfigRB", ProxyParameters.LANGUAGE);
    /** Consturctor Declaration  for  TokenConfigOB */
    private LockerConfigOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
//            initUIComboBoxModel();
//            fillDropdown();
            setPoATabTitle();
             tableUtilPoA.setAttributeKey(KEY);
            tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objLockerConfigOB = new LockerConfigOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setPoATabTitle() throws Exception{
        try {
            poaTabTitle.add(objLockerConfigRB.getString("tblColumn1"));
            poaTabTitle.add(objLockerConfigRB.getString("tblColumn2"));
            poaTabTitle.add(objLockerConfigRB.getString("tblColumn3"));
            poaTabTitle.add(objLockerConfigRB.getString("tblColumn4"));
            poaTabTitle.add(objLockerConfigRB.getString("tblColumn5"));
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA6"));
        }catch(Exception e) {
            System.out.println("Exception in setPoATabTitle()..."+e);
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LockerConfigJNDI");
        map.put(CommonConstants.HOME, "locker.lockerconfig.LockerConfigHome");
        map.put(CommonConstants.REMOTE, "locker.lockerconfig.LockerConfig");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
//        cbmTokenType = new ComboBoxModel();
    }
    
    /* Filling up the the ComboBox in the UI*/
//    private void fillDropdown() throws Exception{
//        try{
//            log.info("Inside FillDropDown");
//            lookUpHash = new HashMap();
//            lookUpHash.put(CommonConstants.MAP_NAME, null);
//            final ArrayList lookup_keys = new ArrayList();
//            lookup_keys.add("TOKEN_TYPE");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get("TOKEN_TYPE"));
//            cbmTokenType = new ComboBoxModel(key,value);
//        }catch(NullPointerException e){
//            parseException.logException(e,true);
//        }catch(Exception e){
//            parseException.logException(e,true);
//        }
//    }
    
    /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static LockerConfigOB getInstance()throws Exception{
        return objLockerConfigOB;
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    private void resetPowerOfAttorneyTable(){
        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        ttNotifyObservers();
    }
    
     public int addPoATab(int row, boolean update){ // update is the flag to update the records
        String temp = new String();
        int option = -1;
        try{
              int qty = CommonUtil.convertObjToInt(txtNoOfLockers);
              int locFrm = CommonUtil.convertObjToInt(txtFromLocNo);
//              final String chqdate = tdtChqDate;
              
        for(int i=0 ; i < qty ; i++){
            poaEachTabRecords = new ArrayList();
            poaEachRecords = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblPoATab.getDataArrayList();
            tblPoATab.setDataArrayList(data,poaTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            boolean found = false;
            insertPoA(dataSize+1,row);
            if (!update) {
                // If the table is not in Edit Mode
                result = tableUtilPoA.insertTableValues(poaEachTabRecords,poaEachRecords);
                
                poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                
                tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
            }else{
                option = updatePoATable(row);
            }
            
            setChanged();
            result = null;
            data = null;
            poaEachTabRecords = null;
            poaEachRecords = null;
        }
//              tdtChqDate = chqdate;
              txtFromLocNo = String.valueOf(locFrm);
        }catch(Exception e){
            System.out.println("The error in addPoATab()"+e);
            parseException.logException(e,true);
        }
        return option;
    }
       public void setTblPoA(EnhancedTableModel tblPoATab){
        this.tblPoATab = tblPoATab;
        setChanged();
    }
      public void createObject(){
        poaAllRecords = new LinkedHashMap();
        poaAllTabRecords = new ArrayList();
        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        tableUtilPoA = new TableUtil();
        tableUtilPoA.setAttributeKey(KEY);
    }
    
    // To destroy Objects
    public void destroyObjects(){
        poaAllRecords = null;
        poaAllTabRecords = null;
        tblPoATab = null;
        poaEachRecords= null;
        poaEachTabRecords= null;
        tableUtilPoA = null;
    }  
    public void resetAllFieldsInPoA(){
        resetForm();
        destroyObjects();
        createObject();
//        clearCboPoACust_ID();
        resetPowerOfAttorneyTable();
    }
    
        public void populatePoATable(int row){
        try{
//            if(!isEditMode){
            HashMap eachRecs = new HashMap();
            java.util.Set keySet =  poaAllRecords.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            ArrayList poaTableValue = (ArrayList)tblPoATab.getDataArrayList().get(row);
            // To populate the corresponding PoA record in UI
            for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
                int val = 0;
//                if(isIsRetChq())
//                     val = 1;
                if (((HashMap) poaAllRecords.get(objKeySet[j])).get(KEY).equals(poaTableValue.get(val))){
                    // To populate the Corresponding record from CTable
                    eachRecs = (HashMap) poaAllRecords.get(objKeySet[j]);
//                    setTxtPoANo(CommonUtil.convertObjToStr(eachRecs.get(POANO)));
//                    setCboPoACust(CommonUtil.convertObjToStr(getCbmPoACust().getDataForKey(eachRecs.get(ONBEHALFOF))));
//                    setTxtCustID_PoA(CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID)));
//                    setCboCity_PowerAttroney(CommonUtil.convertObjToStr(getCbmCity_PowerAttroney().getDataForKey(eachRecs.get(CITY))));
//                    setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(getCbmCountry_PowerAttroney().getDataForKey(eachRecs.get(COUNTRY))));
//                    setTdtPeriodFrom_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(FROM)));
//                    setTdtPeriodTo_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(TO)));
//                    setTxtArea_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(AREA)));
//                    setTxtPhone_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PHONE)));
//                    setTxtPin_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PIN)));
//                    setTxtPoaHolderName(CommonUtil.convertObjToStr(eachRecs.get(HOLDERNAME)));
//                    setTxtRemark_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
//                    setCboAddrType_PoA(CommonUtil.convertObjToStr(getCbmAddrType_PoA().getDataForKey(eachRecs.get(ADDR_TYPE))));
//                    setCboState_PowerAttroney(CommonUtil.convertObjToStr(getCbmState_PowerAttroney().getDataForKey(eachRecs.get(STATE))));
//                    setTxtStreet_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(STREET)));
                    setTxtProdId(CommonUtil.convertObjToStr(eachRecs.get(PRODID)));
                    setTxtFromLocNo(CommonUtil.convertObjToStr(eachRecs.get(FROMLOCNO)));
                    setTxtToLocNo(CommonUtil.convertObjToStr(eachRecs.get(TOLOCNO)));
                    setTxtNoOfLockers(CommonUtil.convertObjToStr(eachRecs.get(TOTLOCNO)));
                    setTxtMasterKey(CommonUtil.convertObjToStr(eachRecs.get(MASTRKEY)));
                    setTxtLockerKey(CommonUtil.convertObjToStr(eachRecs.get(LOCKEY)));
//                    setTdtClearingDt(CommonUtil.convertObjToStr(eachRecs.get(CLRDT)));
//                    setCboBounReason(CommonUtil.convertObjToStr(getCbmState_PowerAttroney().getDataForKey(eachRecs.get(STATE))));
//                    setTxtRemarks(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
                    setChanged();
                    ttNotifyObservers();
                    break;
                }
            }
            eachRecs = null;
            objKeySet = null;
            poaTableValue = null;
            keySet = null;
//            }else{
//                System.out.println("##########lst"+lst);
//                    SettlementTO objSettlementTO = (SettlementTO)lst.get(row);
//                    setTxtFromChqNo(CommonUtil.convertObjToStr(objSettlementTO.getFromChqNo()));
//                    setTxtToChqNo(CommonUtil.convertObjToStr(objSettlementTO.getToChqNo()));
//                    setTxtQty(CommonUtil.convertObjToStr(objSettlementTO.getQty()));
//                    setTdtChqDate(CommonUtil.convertObjToStr(objSettlementTO.getChqDate()));
//                    setTxtChqAmt(CommonUtil.convertObjToStr(objSettlementTO.getChqAmt()));
//                    setTdtClearingDt(CommonUtil.convertObjToStr(objSettlementTO.getClearingDt()));
//                    setTxtRemarks(CommonUtil.convertObjToStr(objSettlementTO.getRemark()));
//                    setChanged();
//                    ttNotifyObservers();
//            }
        }catch(Exception e){
            System.out.println("Error in populatePoATable..."+e);
            parseException.logException(e,true);
        }
    }
        
    /**
     * Getter for property isTableClicked.
     * @return Value of property isTableClicked.
     */
    public boolean isIsTableClicked() {
        return isTableClicked;
    }
    
    /**
     * Setter for property isTableClicked.
     * @param isTableClicked New value of property isTableClicked.
     */
    public void setIsTableClicked(boolean isTableClicked) {
        this.isTableClicked = isTableClicked;
    }
    
    public EnhancedTableModel getTblPoA(){
        return this.tblPoATab;
    }
    
     public void deletePoATable(int row){
        try{
            ArrayList tempList1 = new ArrayList();
            HashMap result = new HashMap();
            
            result = tableUtilPoA.deleteTableValues(row);
            
            poaAllTabRecords = (ArrayList)result.get(TABLE_VALUES);
            poaAllRecords = (LinkedHashMap)result.get(ALL_VALUES);
            
            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
            result = null;
        }catch(Exception e){
            System.out.println("Exception caught in deletePoATable: "+e);
            parseException.logException(e,true);
        }
    }
     
     private int updatePoATable(int row){
        HashMap result = new HashMap();
        int option = -1;
        int count = 0;
        try{
            
            result = tableUtilPoA.updateTableValues(poaEachTabRecords, poaEachRecords, row);
            
            poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
            poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
            option = CommonUtil.convertObjToInt(result.get(OPTION));
            
            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
        }catch(Exception e){
            System.out.println("Error in updatePoA..."+e);
            parseException.logException(e,true);
        }
        result = null;
        return option;
    }
     private void insertPoA(int slno,int row){
        
//        int qty = CommonUtil.convertObjToInt(txtQty);
//        for(int i=0 ; i < qty ; i++){
//        int chqNo = CommonUtil.convertObjToInt(txtFromChqNo);
//        int finalChq = 0;
        if(isIsEditMode()){
            String slNo = CommonUtil.convertObjToStr(tblPoATab.getValueAt(row,1));   
            slno = CommonUtil.convertObjToInt(slNo);
         }
        if(!isTableClicked)
        if(slno == 1){
            
        }else{
//            Date chqDt = DateUtil.getDateMMDDYYYY(tdtChqDate);
//            Date finalDt = DateUtil.addDaysProperFormat(chqDt, 30);
//            tdtChqDate = CommonUtil.convertObjToStr(finalDt);
            int locNo = CommonUtil.convertObjToInt(txtFromLocNo);
            int locFrm = locNo+1;
            txtFromLocNo = String.valueOf(locFrm);
        }
        poaEachTabRecords.add(String.valueOf(slno));
//        poaEachTabRecords.add(txtFromChqNo);
//        poaEachTabRecords.add(txtFromLocNo);
         poaEachTabRecords.add(txtFromLocNo);
         poaEachTabRecords.add(txtMasterKey);
         poaEachTabRecords.add(txtLockerKey);
         poaEachTabRecords.add("AVAILABLE");
//        poaEachTabRecords.add(tdtChqDate);
//        poaEachTabRecords.add(txtChqAmt);
        
     
//        if(tdtClearingDt.equals("") || tdtClearingDt == null){
//            poaEachTabRecords.add("UNUSED");
//            poaEachRecords.put(CHQSTATUS,"UNUSED");
//        }
//        else{
//            poaEachTabRecords.add("CLEARED");
//            poaEachRecords.put(CHQSTATUS,"CLEARED");
//        }
//        if(isRdoChqBounce_Yes()){
//              poaEachRecords.put(CHQ_BOUNC , "Y");
//              poaEachTabRecords.remove("UNUSED");
//              poaEachTabRecords.add("BOUNCED");
//              poaEachRecords.put(CHQSTATUS,"BOUNCED");
//        }else{
//              poaEachRecords.put(CHQ_BOUNC , "N");
////              poaEachTabRecords.add("BOUNCED");
////              poaEachRecords.put(CHQSTATUS,"BOUNCED");
//        }
            
        poaEachRecords.put(PRODID,String.valueOf(txtProdId));
        poaEachRecords.put(SLNO,String.valueOf(slno));
        poaEachRecords.put(FROMLOCNO ,txtFromLocNo);
        poaEachRecords.put(TOLOCNO ,txtToLocNo);
        poaEachRecords.put(TOTLOCNO ,txtNoOfLockers);
        poaEachRecords.put(MASTRKEY ,txtMasterKey);
        poaEachRecords.put(LOCKEY ,txtLockerKey);
          
//        poaEachRecords.put(CHQDT ,tdtChqDate);
//        poaEachRecords.put(CHQAMT ,txtChqAmt);
//        poaEachRecords.put(CLRDT , tdtClearingDt);
       
//        poaEachRecords.put(REMARK ,CommonUtil.convertObjToStr(cbmCity_PowerAttroney.getKeyForSelected()));
//        poaEachRecords.put(STATE,CommonUtil.convertObjToStr(cbmState_PowerAttroney.getKeyForSelected()));
//        poaEachRecords.put(COUNTRY,CommonUtil.convertObjToStr(cbmCountry_PowerAttroney.getKeyForSelected()));
//        poaEachRecords.put(PIN,txtPin_PowerAttroney);
//        poaEachRecords.put(PHONE,txtPhone_PowerAttroney);
//        poaEachRecords.put(FROM,tdtPeriodFrom_PowerAttroney);
//        poaEachRecords.put(TO,tdtPeriodTo_PowerAttroney);
//        poaEachRecords.put()
//        poaEachRecords.put(REMARK,txtRemarks);
        
        
//        if(isRdoReturnChq_Yes()){
//            poaEachRecords.put(RETCHQ, "Y");
//        }else{
//                poaEachRecords.put(RETCHQ, "N");
//        }
//        if(isRdoChqBounce_Yes()){
//          poaEachRecords.put(CHQ_BOUNC, "Y");
//        }else{
//            poaEachRecords.put(CHQ_BOUNC, "N");
//        }
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            poaEachRecords.put(COMMAND, "UPDATE");
        }else{
            poaEachRecords.put(COMMAND, "INSERT");
        }
//        return chqFrm;
//        }
    }
     
      public boolean isIsEditMode() {
        return isEditMode;
    }
    
    /**
     * Setter for property isEditMode.
     * @param isEditMode New value of property isEditMode.
     */
    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }
    
     
    /**
     * Getter for property txtTokenConfigId.
     * @return Value of property txtTokenConfigId.
     */
//    public java.lang.String getTxtTokenConfigId() {
//        return txtTokenConfigId;
//    }
    
    /**
     * Setter for property txtTokenConfigId.
     * @param txtTokenConfigId New value of property txtTokenConfigId.
     */
//    public void setTxtTokenConfigId(java.lang.String txtTokenConfigId) {
//        this.txtTokenConfigId = txtTokenConfigId;
//        setChanged();
//    }
    
    // Setter method for cboTokenType
//    void setCboTokenType(String cboTokenType){
//        this.cboTokenType = cboTokenType;
//        setChanged();
//    }
//    // Getter method for cboTokenType
//    String getCboTokenType(){
//        return this.cboTokenType;
//    }
//    
//    /**
//     * Getter for property cbmTokenType.
//     * @return Value of property cbmTokenType.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmTokenType() {
//        return cbmTokenType;
//    }
//    
    /**
     * Setter for property cbmTokenType.
     * @param cbmTokenType New value of property cbmTokenType.
     */
//    public void setCbmTokenType(com.see.truetransact.clientutil.ComboBoxModel cbmTokenType) {
//        this.cbmTokenType = cbmTokenType;
//    }
//    
//    // Setter method for txtSeriesNo
//    void setTxtSeriesNo(String txtSeriesNo){
//        this.txtSeriesNo = txtSeriesNo;
//        setChanged();
//    }
//    // Getter method for txtSeriesNo
//    String getTxtSeriesNo(){
//        return this.txtSeriesNo;
//    }
//    
//    // Setter method for txtStartingTokenNo
//    void setTxtStartingTokenNo(String txtStartingTokenNo){
//        this.txtStartingTokenNo = txtStartingTokenNo;
//        setChanged();
//    }
//    // Getter method for txtStartingTokenNo
//    String getTxtStartingTokenNo(){
//        return this.txtStartingTokenNo;
//    }
//    
//    // Setter method for txtEndingTokenNo
//    void setTxtEndingTokenNo(String txtEndingTokenNo){
//        this.txtEndingTokenNo = txtEndingTokenNo;
//        setChanged();
//    }
//    // Getter method for txtEndingTokenNo
//    String getTxtEndingTokenNo(){
//        return this.txtEndingTokenNo;
//    }
//    
//    // Setter method for txtNoOfTokens
//    void setTxtNoOfTokens(String txtNoOfTokens){
//        this.txtNoOfTokens = txtNoOfTokens;
//        setChanged();
//    }
    // Getter method for txtNoOfTokens
//    String getTxtNoOfTokens(){
//        return this.txtNoOfTokens;
//    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
    private LockerConfigTO getLockerConfigTO(String command){
        LockerConfigTO objLockerConfigTO = new LockerConfigTO();
        objLockerConfigTO.setCommand(command);
        objLockerConfigTO.setProdId(getTxtProdId());
//        objTokenConfigTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
//        objTokenConfigTO.setSeriesNo(getTxtSeriesNo());
        objLockerConfigTO.setFromLocNo(CommonUtil.convertObjToInt(getTxtFromLocNo()));
        objLockerConfigTO.setToLocNo(CommonUtil.convertObjToInt(getTxtToLocNo()));
        objLockerConfigTO.setTotLockers(CommonUtil.convertObjToInt(getTxtNoOfLockers()));
        objLockerConfigTO.setMasterKeyNo(CommonUtil.convertObjToInt(getTxtMasterKey()));
        objLockerConfigTO.setBranchId(TrueTransactMain.BRANCH_ID);
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objLockerConfigTO.setCreatedBy(TrueTransactMain.USER_ID);
            objLockerConfigTO.setCreatedDt(currDt);
            objLockerConfigTO.setStatus("CREATED");
//            objLockerConfigTO.setLocStatus("AVAILABLE");
        }else if(command.equals(CommonConstants.TOSTATUS_UPDATE)){
            objLockerConfigTO.setStatus("UPDATED");
        }else{
            objLockerConfigTO.setStatus("DELETED");
        }
        objLockerConfigTO.setStatusBy(TrueTransactMain.USER_ID);
        objLockerConfigTO.setStatusDt(currDt);
        return objLockerConfigTO;
    }
    
    private void setLockerConfigTO(LockerConfigTO objLockerConfigTO){
//        setTxtTokenConfigId(objTokenConfigTO.getConfigId());
//        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objTokenConfigTO.getTokenType())));
//        setTxtSeriesNo(objTokenConfigTO.getSeriesNo());
//        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenStartNo()));
//        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenEndNo()));
        setTxtProdId(objLockerConfigTO.getProdId());
        setTxtFromLocNo(CommonUtil.convertObjToStr(objLockerConfigTO.getFromLocNo()));
        setTxtToLocNo(CommonUtil.convertObjToStr(objLockerConfigTO.getToLocNo()));
        setTxtMasterKey(CommonUtil.convertObjToStr(objLockerConfigTO.getMasterKeyNo()));
        setTxtNoOfLockers(CommonUtil.convertObjToStr(objLockerConfigTO.getTotLockers()));
        setStatusBy(objLockerConfigTO.getStatusBy());
         setAuthorizeStatus(objLockerConfigTO.getAuthorizeStatus());
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
//        setTxtTokenConfigId("");
//        setCboTokenType("");
//        setTxtSeriesNo("");
//        setTxtStartingTokenNo("");
//        setTxtEndingTokenNo("");
        setTxtProdId("");
        setTxtFromLocNo("");
        setTxtToLocNo("");
        setTxtNoOfLockers("");
        setTxtMasterKey("");
        setTxtLockerKey("");
//        setTblPoA(new EnhancedTableModel());
      
      
        notifyObservers();
    }
   public void resetTable(){
        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
        ttNotifyObservers();
    } 
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            HashMap objLockerDetailsConfigTOMap = getLockerDetailsConfigTO(command);
            term.put("LockerConfigTO", getLockerConfigTO(command));
            term.put("LockerConfigDetailsTO", objLockerDetailsConfigTOMap);
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
 public HashMap getLockerDetailsConfigTO(String saveMode){
        LockerConfigDetailsTO objLockerConfigDetailsTO = new LockerConfigDetailsTO();
        HashMap poaTOList = new HashMap();
        HashMap poaEachRecord;
        try {
            java.util.Set keySet =  poaAllRecords.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the values for Power of Attorney Transfer Object
            if(poaAllRecords!=null)
            for (int i = poaAllRecords.size() - 1, j = 0;i >= 0;--i,++j){
                poaEachRecord = (HashMap) poaAllRecords.get(objKeySet[j]);
                objLockerConfigDetailsTO = new LockerConfigDetailsTO();
                objLockerConfigDetailsTO.setProdId(CommonUtil.convertObjToStr( poaEachRecord.get(PRODID)));
                objLockerConfigDetailsTO.setSlNo(CommonUtil.convertObjToInt(poaEachRecord.get(SLNO)));
//                objLockerConfigDetailsTO.setFromLocNo(CommonUtil.convertObjToStr((String)cbmBankName.getKeyForSelected()));
//                objLockerConfigDetailsTO.setFromLocNo(CommonUtil.convertObjToStr((String)cbmBranchName.getKeyForSelected()));
                objLockerConfigDetailsTO.setFromLocNo(CommonUtil.convertObjToInt(poaEachRecord.get(FROMLOCNO)));
                objLockerConfigDetailsTO.setToLocNo(CommonUtil.convertObjToInt(poaEachRecord.get(TOLOCNO)));
                objLockerConfigDetailsTO.setTotLockers(CommonUtil.convertObjToInt(poaEachRecord.get(TOTLOCNO)));
//                objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(poaEachRecord.get(CHQDT))));
                objLockerConfigDetailsTO.setMasterKeyNo(CommonUtil.convertObjToInt(poaEachRecord.get(MASTRKEY)));
                objLockerConfigDetailsTO.setLockerKeyNo(CommonUtil.convertObjToStr( poaEachRecord.get(LOCKEY)));
////                objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(poaEachRecord.get(CLRDT))));
//                objSettlementTO.setBounReason(CommonUtil.convertObjToStr((String)cbmBounReason.getKeyForSelected()));
//                objSettlementTO.setRemark(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objSettlementTO.setChqBoun(CommonUtil.convertObjToStr( poaEachRecord.get(CHQ_BOUNC)));
//                objSettlementTO.setReturnChq(CommonUtil.convertObjToStr( poaEachRecord.get(RETCHQ)));
//                objSettlementTO.setChqStatus(CommonUtil.convertObjToStr( poaEachRecord.get(CHQSTATUS)));
                
//                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
                
//                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CHQDT)));
//                if(IsDt != null){
//                Date isDate = (Date)curDate.clone();
//                isDate.setDate(IsDt.getDate());
//                isDate.setMonth(IsDt.getMonth());
//                isDate.setYear(IsDt.getYear());
//                objSettlementTO.setChqDate(isDate);  
//                }else{
//                  objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CHQDT))));  
//                }
                
//                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CLRDT)));
//                if(ToDt != null){
//                Date toDate = (Date)curDate.clone();
//                toDate.setDate(ToDt.getDate());
//                toDate.setMonth(ToDt.getMonth());
//                toDate.setYear(ToDt.getYear());
//                objSettlementTO.setClearingDt(toDate);  
//                }else{
//                  objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CLRDT))));
//                }
                
                
//                objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
//                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
//                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
//                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
//                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
//                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
//                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
//                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
//                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
//                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
                
                objLockerConfigDetailsTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
//                if(saveMode==1){
//                    objSettlementTO.setCommand("INSERT");
//                }else{
//                    objSettlementTO.setCommand("UPDATE");
//                }
//                if (poaEachRecord.get(COMMAND).equals(INSERT)){
//                    objSettlementTO.setStatus(CommonConstants.STATUS_CREATED);
//                }else if (poaEachRecord.get(COMMAND).equals(UPDATE)){
//                    objSettlementTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                }
//                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanPowerAttorneyTO.setStatusDt(currDt);
                poaTOList.put(objLockerConfigDetailsTO.getFromLocNo(), objLockerConfigDetailsTO);
                poaEachRecord = null;
                objLockerConfigDetailsTO = null;
            }
            // To set the values for Power of Attorney Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilPoA.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                poaEachRecord = (HashMap) tableUtilPoA.getRemovedValues().get(j);
                objLockerConfigDetailsTO = new LockerConfigDetailsTO();
//                objSettlementTO.setBankNameSet(CommonUtil.convertObjToStr(getCbmBankName()));
//                objSettlementTO.setBranchNameSet(CommonUtil.convertObjToStr(getCbmBranchName()));
//                objSettlementTO.setFromChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                
////                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                
//                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CHQDT)));
//                if(IsDt != null){
//                Date isDate = (Date)curDate.clone();
//                isDate.setDate(IsDt.getDate());
//                isDate.setMonth(IsDt.getMonth());
//                isDate.setYear(IsDt.getYear());
//                objSettlementTO.setChqDate(isDate);  
//                }else{
//                  objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CHQDT))));  
//                }
//                
//                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CLRDT)));
//                if(ToDt != null){
//                Date toDate = (Date)curDate.clone();
//                toDate.setDate(ToDt.getDate());
//                toDate.setMonth(ToDt.getMonth());
//                toDate.setYear(ToDt.getYear());
//                objSettlementTO.setClearingDt(toDate);  
//                }else{
//                  objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(CLRDT))));
//                }
                
                
//                objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
//                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
//                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
//                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
//                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
//                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
//                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
//                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
//                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
//                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
                 objLockerConfigDetailsTO.setProdId(CommonUtil.convertObjToStr( poaEachRecord.get(PRODID)));
                objLockerConfigDetailsTO.setSlNo(CommonUtil.convertObjToInt(poaEachRecord.get(SLNO)));
                objLockerConfigDetailsTO.setFromLocNo(CommonUtil.convertObjToInt(poaEachRecord.get(FROMLOCNO)));
                objLockerConfigDetailsTO.setToLocNo(CommonUtil.convertObjToInt(poaEachRecord.get(TOLOCNO)));
                objLockerConfigDetailsTO.setTotLockers(CommonUtil.convertObjToInt(poaEachRecord.get(TOTLOCNO)));
                objLockerConfigDetailsTO.setMasterKeyNo(CommonUtil.convertObjToInt(poaEachRecord.get(MASTRKEY)));
                objLockerConfigDetailsTO.setLockerKeyNo(CommonUtil.convertObjToStr( poaEachRecord.get(LOCKEY)));
                objLockerConfigDetailsTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
                
                objLockerConfigDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
//                objTermLoanPowerAttorneyTO.setStatusDt(currDt);
                poaTOList.put(objLockerConfigDetailsTO.getFromLocNo(), objLockerConfigDetailsTO);
                poaEachRecord = null;
                objLockerConfigDetailsTO = null;
            }
        }catch(Exception e){
            System.out.println("Error In setTermLoanPowerAttorney..."+e);
            parseException.logException(e,true);
        }
        objLockerConfigDetailsTO = null;
        return poaTOList;
    }
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        LockerConfigTO objLockerConfigTO  = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            List lstData = (List)mapData.get("LockerConfigTO");
             for(int i=0; i < lstData.size(); i++){
                objLockerConfigTO = (LockerConfigTO) ((List) mapData.get("LockerConfigTO")).get(i);
            }
//            objBillsChargesTO = (BillsChargesTO) ((List) mapData.get("BillsChargesTO")).get(i);
//            LockerConfigTO objLockerConfigTO = 
//            (LockerConfigTO) ((List) mapData.get("LockerConfigTO")).get(0);
             if(null != objLockerConfigTO){
            setLockerConfigTO(objLockerConfigTO);
             }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
       public void setTbmLockerTab(ArrayList list){
        int size=list.size();
        int option = -1;
       
        for (int i=0;i<size;i++){
            tblRowList = new ArrayList();
             poaEachTabRecords = new ArrayList();
            poaEachRecords = new HashMap();
            HashMap result = new HashMap();
            HashMap columnData =(HashMap) list.get(i);
//            tblRowList.add(columnData.get("SL_NO"));
//            tblRowList.add(columnData.get("FROM_LOC_NO"));
//            tblRowList.add(columnData.get("MASTER_KEY_NO"));
//            tblRowList.add(columnData.get("LOCKER_KEY_NO"));
//            tblRowList.add(columnData.get("STATUS"));
//            tblPoATab.insertRow(0,tblRowList);
            poaEachTabRecords.add(columnData.get("SL_NO"));
            poaEachTabRecords.add(columnData.get("FROM_LOC_NO"));
             poaEachTabRecords.add(columnData.get("MASTER_KEY_NO"));
             poaEachTabRecords.add(columnData.get("LOCKER_KEY_NO"));
             poaEachTabRecords.add(columnData.get("STATUS"));
             
             poaEachRecords.put(PRODID,String.valueOf(txtProdId));
            poaEachRecords.put(SLNO,columnData.get("SL_NO"));
            poaEachRecords.put(FROMLOCNO ,columnData.get("FROM_LOC_NO"));
            poaEachRecords.put(TOLOCNO ,columnData.get("TO_LOC_NO"));
            poaEachRecords.put(TOTLOCNO ,columnData.get("TOT_LOCKERS"));
            poaEachRecords.put(MASTRKEY ,columnData.get("MASTER_KEY_NO"));
            poaEachRecords.put(LOCKEY ,columnData.get("LOCKER_KEY_NO"));
             System.out.println("@@#$@#$$poaEachTabRecords "+poaEachTabRecords);  
        System.out.println("@@#$@#poaEachRecords "+poaEachRecords);
        result = tableUtilPoA.insertTableValues(poaEachTabRecords,poaEachRecords);
        poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
                poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
                  setChanged();
            result = null;
//            data = null;
            poaEachTabRecords = null;
            poaEachRecords = null;
        }
       
    }
        public ArrayList getResultList(HashMap map){
        HashMap resultMap = null;
        ArrayList resultList = null;
        String stmtName = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME));
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        resultList =(ArrayList) ClientUtil.executeQuery(stmtName, where);
        return resultList;
        
    }
    /*Checks for the duplication of TokenType if so retuns a boolean type vairable as true */
    public boolean isTokenTypeExists(String tokenType){
        boolean exists = false;
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenType", where);
        where = null;
        if(resultList.size() > 0){
            for(int i=0; i<resultList.size(); i++){
                resultMap= (HashMap)resultList.get(i);
                if(resultMap.get("TOKEN_TYPE").equals(tokenType)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /*Checks for the duplication of SeriesNo if so retuns a boolean type vairable as true */
    public boolean isSeriesNoExists(String tokenType,String seriesNo){
        boolean exists = false;
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("TOKEN_TYPE", tokenType);
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenSeries", where);
        where = null;
        if(resultList.size() > 0){
            for(int i=0; i<resultList.size(); i++){
                resultMap= (HashMap)resultList.get(i);
                if(resultMap.get("SERIES_NO").equals(seriesNo)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /**
     * Getter for property txtProdId.
     * @return Value of property txtProdId.
     */
    public java.lang.String getTxtProdId() {
        return txtProdId;
    }
    
    /**
     * Setter for property txtProdId.
     * @param txtProdId New value of property txtProdId.
     */
    public void setTxtProdId(java.lang.String txtProdId) {
        this.txtProdId = txtProdId;
    }
    
    /**
     * Getter for property txtFromLocNo.
     * @return Value of property txtFromLocNo.
     */
    public java.lang.String getTxtFromLocNo() {
        return txtFromLocNo;
    }
    
    /**
     * Setter for property txtFromLocNo.
     * @param txtFromLocNo New value of property txtFromLocNo.
     */
    public void setTxtFromLocNo(java.lang.String txtFromLocNo) {
        this.txtFromLocNo = txtFromLocNo;
    }
    
    /**
     * Getter for property txtToLocNo.
     * @return Value of property txtToLocNo.
     */
    public java.lang.String getTxtToLocNo() {
        return txtToLocNo;
    }
    
    /**
     * Setter for property txtToLocNo.
     * @param txtToLocNo New value of property txtToLocNo.
     */
    public void setTxtToLocNo(java.lang.String txtToLocNo) {
        this.txtToLocNo = txtToLocNo;
    }
    
    /**
     * Getter for property txtNoOfLockers.
     * @return Value of property txtNoOfLockers.
     */
    public java.lang.String getTxtNoOfLockers() {
        return txtNoOfLockers;
    }
    
    /**
     * Setter for property txtNoOfLockers.
     * @param txtNoOfLockers New value of property txtNoOfLockers.
     */
    public void setTxtNoOfLockers(java.lang.String txtNoOfLockers) {
        this.txtNoOfLockers = txtNoOfLockers;
    }
    
    /**
     * Getter for property txtMasterKey.
     * @return Value of property txtMasterKey.
     */
    public java.lang.String getTxtMasterKey() {
        return txtMasterKey;
    }
    
    /**
     * Setter for property txtMasterKey.
     * @param txtMasterKey New value of property txtMasterKey.
     */
    public void setTxtMasterKey(java.lang.String txtMasterKey) {
        this.txtMasterKey = txtMasterKey;
    }
    
    /**
     * Getter for property txtLockerKey.
     * @return Value of property txtLockerKey.
     */
    public java.lang.String getTxtLockerKey() {
        return txtLockerKey;
    }
    
    /**
     * Setter for property txtLockerKey.
     * @param txtLockerKey New value of property txtLockerKey.
     */
    public void setTxtLockerKey(java.lang.String txtLockerKey) {
        this.txtLockerKey = txtLockerKey;
    }
    
}