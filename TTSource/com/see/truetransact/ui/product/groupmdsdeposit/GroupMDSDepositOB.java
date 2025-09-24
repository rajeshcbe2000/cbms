/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FreezeOB.java
 *
 * Created on August 13, 2003, 4:32 PM
 */

package com.see.truetransact.ui.product.groupmdsdeposit;

import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import javax.swing.AbstractButton;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.FreezeTO;
import com.see.truetransact.transferobject.product.loan.loaneligibilitymaintenance.LoanEligibilityTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.product.groupmdsdeposit.GroupMDSDepositTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  Administrator
 * modified by Karthik
 *public class FreezeOB extends java.util.CObservable
 */
public class GroupMDSDepositOB extends CObservable {
    
   
   // private int _result,_actionType;
    private String rdoMds="";
    private String rdoDeposit="";
    private String groupType="";
    private String txtGroupName="";
    private String txtCount="";
    private String txtDepositAmt="";
    private ComboBoxModel cbmProductType;
    private String cboProductType="";
    private ComboBoxModel cbmPrematureIntRecType;
    private String cboPrematureIntRecType="";
    private String txtPrematureIntRecAmt="";
    private ComboBoxModel cbmInterestAmount;
    private ComboBoxModel cbmDueIntCalcType;
    private String cboDueIntCalcType = "";
    private String cboInterestAmount="";
    private String txtInterestAmount="";
    private String txtDueIntCalcType = "";
    private String chkIsIntRecovery = "";
    private String chkIsIntForDue = "";
   

    public String getChkIsIntForDue() {
        return chkIsIntForDue;
    }

    public void setChkIsIntForDue(String chkIsIntForDue) {
        this.chkIsIntForDue = chkIsIntForDue;
    }

    public String getChkIsIntRecovery() {
        return chkIsIntRecovery;
    }

    public void setChkIsIntRecovery(String chkIsIntRecovery) {
        this.chkIsIntRecovery = chkIsIntRecovery;
    }

    public ComboBoxModel getCbmDueIntCalcType() {
        return cbmDueIntCalcType;
    }

    public void setCbmDueIntCalcType(ComboBoxModel cbmDueIntCalcType) {
        this.cbmDueIntCalcType = cbmDueIntCalcType;
    }

    public String getCboDueIntCalcType() {
        return cboDueIntCalcType;
    }

    public void setCboDueIntCalcType(String cboDueIntCalcType) {
        this.cboDueIntCalcType = cboDueIntCalcType;
    }

    public String getTxtDueIntCalcType() {
        return txtDueIntCalcType;
    }

    public void setTxtDueIntCalcType(String txtDueIntCalcType) {
        this.txtDueIntCalcType = txtDueIntCalcType;
    }    
    
    private ComboBoxModel cbmPenalCalculation;
    private String cboPenalCalculation="";
    private String txtPrizedPenal="";
    private String txtNonPrizedPenal="";
    private ComboBoxModel cbmInterestRecovery;
    private String cboInterestRecovery="";
    private String txtInterestRecovery="";
    private String tdtStartDate="";
    private String tdtEndDate="";
    private String groupId = "";

   
    
    private String cboCropType ="";
    private ComboBoxModel cbmCropType=null;
    private String txtEligibileAmount ="";
    private String tdtFromDate="";
    private String tdtToDate="";
    //private EnhancedTableModel tblEligibilityList; 
       

    
    
    private String txtAmount = "";
    private String tdtFreezeDate = "";
    private String cboType = "";
    private String txtRemarks = "";
    private String cboProductID = "";
    private String txtAccountNumber = "";
    private String lblFreezeId = "";
    private String lblAuth = "";
    private String lblFreezeStatus = "";
    private String lblAuthorizeStatus = "";
    private String REMARKS;
    private String TYPE;
    private String DATE;
    
    private EnhancedTableModel tblFreezeListModel;
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmType;
    private String accountHeadDesc;
    private String accountHeadCode;
    private int actionType;
    private String customerName;
    private String clearBalance;
    private String clearBalance1;
    private String freezeSum;
    private String LienSum;
    private int result;
    private String advances="";
    private HashMap _authorizeMap;
    private int row;
    
    private ArrayList cropList = new ArrayList();
    private ArrayList freezeDeleteList = new ArrayList();
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    
    private final static Logger log = Logger.getLogger(GroupMDSDepositOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  ArrayList freezeListColumn = new ArrayList();
    java.util.ResourceBundle freezeRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    //To store the Freeze Details
    private HashMap freezeMap;
    private LoanEligibilityTO ObjLoanEligibilityTO;
    
    private final String COMP_FREEZE = "COMPLETE";
    Date curDate = null;
    private int slno=-1;
    
     
    /** Creates a new instance of FreezeOB */
    public GroupMDSDepositOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();        
   
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GroupMDSDepositJNDI");
            map.put(CommonConstants.HOME, "product.groupmdsdeposit.GroupMDSDepositBean");
            map.put(CommonConstants.REMOTE, "product.groupmdsdeposit.GroupMDSDeposit");
            
            fillDropdown();
            cropList = new ArrayList();
            freezeDeleteList = new ArrayList();
           
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }

    
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        lookupMap.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("ADVANCESPRODUCT.CHARGETYPE");
        lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
        getKeyValue((HashMap) keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmInterestAmount = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmPenalCalculation = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmInterestRecovery = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmPrematureIntRecType = new ComboBoxModel(key, value);
        getKeyValue((HashMap) keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmDueIntCalcType = new ComboBoxModel(key, value);
//        lookupMap.put(CommonConstants.MAP_NAME, "deposit_getProdId");
//        lookupMap.put(CommonConstants.PARAMFORQUERY, null);
//        keyValue = ClientUtil.populateLookupData(lookupMap);
//        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
//        cbmProductType = new ComboBoxModel(key, value);
    }
    
     private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
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
    
    public void resetVariables(){
        cropList = null;
        ObjLoanEligibilityTO = null;
    }
    
    
    public void resetProductPanel(){
      
        cbmCropType.setKeyForSelected("");
        setTxtEligibileAmount("");
        setTdtFromDate("");
        setTdtToDate("");
    }
    
    public void resetAccountDetails(){
        this.getCbmCropType().setKeyForSelected("");
        this.setTdtFromDate("");
        this.setTdtToDate("");
        this.setTxtEligibileAmount("");
    }
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     */
    public void getAccountHeadForProduct() {
        
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        param.put(CommonConstants.MAP_NAME,"getAccHead");
        param.put(CommonConstants.PARAMFORQUERY,CommonUtil.convertObjToStr(cbmProductID.getKeyForSelected()));
        try {
            final HashMap lookupValues = populateData(param);
            fillData((HashMap)lookupValues.get("DATA"));
             //If the returned ArrayList has got proper value, then set the variables
            if( value.size() > 1 ){
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** based on the account number, customer details will be fetched from database and
     * displayed on screen ;same LookUp bean will be used for this purpose
     */
    public void getCustomerDetails() {
        
        final HashMap whereMap = new HashMap();
        List resultList=null;
        try {
            if(advances.equals("ADVANCES"))
                resultList = ClientUtil.executeQuery("getFreezeCustomerDetailsOD", whereMap);
            else
                resultList = ClientUtil.executeQuery("getFreezeCustomerDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
            if(advances.equals("ADVANCES"))
                if(resultMap !=null && resultMap.containsKey("CLEAR_BALANCE"))
                    if(CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue()<=0){
                        ClientUtil.showMessageWindow("Cant Freeze amt  :"+CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue());
//                        setTxtAccountNumber("");
                        ttNotifyObservers();
                        return;
                    }
            
            setPartialCustomerInfo(resultMap);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    /** based on the selection from the product combo box, Product Details will be
     * fetched from database and displayed on screen;same LookUp bean will be used for this purpose
     */
    public void getProductDetails(HashMap whereMap) {
        
        try {
            List resultList=null;
                 resultList = ClientUtil.executeQuery("getFreezeProductDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To set the mentioned display fileds */
    private void setPartialCustomerInfo(HashMap resultMap) throws Exception{
    }
    
    
    
    public void updateToDate(int updateTab ){
        ArrayList singleList =new ArrayList();
        ArrayList totList =new ArrayList();
        if(updateTab ==-1){
                totList.set(totList.size()-1,singleList);            
        }else{
            ObjLoanEligibilityTO.setToDate(getProperDateFormat(getTdtToDate()));
        }
    }
    
     public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
     /*
      * To Update the Row in the Table...
      */
    private void updateFreezeTab(int row, FreezeTO ObjLoanEligibilityTO){
        cropList.remove(row);
        cropList.add(row,ObjLoanEligibilityTO);
        tblFreezeListModel.setValueAt(txtAmount, row, 2);
    }
    
     /*
      * To delete the Row(s) in the Interest Table...
      */
    public void deleteFreezeTab(int selectedRow){
        log.info("In deleteInterestTab...");       
        
        if((getActionType() == ClientConstants.ACTIONTYPE_EDIT)){
            LoanEligibilityTO ObjLoanEligibilityTO =   (LoanEligibilityTO)cropList.get(selectedRow);
            freezeDeleteList.add(ObjLoanEligibilityTO);
        }
        cropList.remove(selectedRow);
    }
    
    
    /** To set the status of UnLien Lien object in the lienMap */
    public void unFreeze(String unfreezeRemarks, int selectedRow){
        FreezeTO ObjLoanEligibilityTO = (FreezeTO)cropList.get(selectedRow);
        if( ObjLoanEligibilityTO != null ){
            cropList.remove(selectedRow);
            
            ObjLoanEligibilityTO.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
            ObjLoanEligibilityTO.setUnfreezeRemarks(unfreezeRemarks);
            ObjLoanEligibilityTO.setUnfreezeDt(curDate);
            
            ObjLoanEligibilityTO.setStatusBy(ProxyParameters.USER_ID);
            ObjLoanEligibilityTO.setStatusDt(curDate);
            
            ObjLoanEligibilityTO.setAuthorizeStatus(null);
            cropList.add(selectedRow,ObjLoanEligibilityTO);
        }
        
            }
    
    
    /** To get the data for a specific Lien details */
    public void getData(HashMap whereMap) {
        try {
            ArrayList groupMDSDepositList = new ArrayList();
            ArrayList gdsProdList = new ArrayList();
            GroupMDSDepositTO groupMDSDepositTO;
            whereMap.put(CommonConstants.STATUS, new Integer(getActionType()));
            HashMap resultMap = (HashMap) proxy.executeQuery(whereMap, map);
            if (resultMap != null && resultMap.containsKey("GroupMDSDepositTO")) {
                groupMDSDepositTO = (GroupMDSDepositTO) resultMap.get("GroupMDSDepositTO");
                System.out.println("inside getData groupMDSDepositTO :: " + groupMDSDepositTO);
                populateGroupMDSDepositProductMaster(groupMDSDepositTO);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populateGroupMDSDepositProductMaster(GroupMDSDepositTO groupMDSDepositTO){       
    
      setGroupType(groupMDSDepositTO.getGroupType());
      setTxtGroupName(groupMDSDepositTO.getGroupName());
      setTxtCount(String.valueOf(groupMDSDepositTO.getSchemeCount()));
      setCboProductType(groupMDSDepositTO.getProductType());
      setCboInterestAmount(groupMDSDepositTO.getInterestAmountType());
      setTxtInterestAmount(String.valueOf(groupMDSDepositTO.getInterestAmount()));
      setCboPenalCalculation(groupMDSDepositTO.getPenalCalculationType());
      setTxtPrizedPenal(String.valueOf(groupMDSDepositTO.getPrizedPenal()));
      setTxtNonPrizedPenal(String.valueOf(groupMDSDepositTO.getNonPrizedPenal()));
      setCboInterestRecovery((groupMDSDepositTO.getInterestRecoveryType()));
      setTxtInterestRecovery(String.valueOf(groupMDSDepositTO.getInterestRecovery()));
      setTdtStartDate(DateUtil.getStringDate(groupMDSDepositTO.getStartDate())); 
      setTdtEndDate(DateUtil.getStringDate(groupMDSDepositTO.getEndDate()));
      setGroupId(groupMDSDepositTO.getGroupNo());
      setCboPrematureIntRecType((groupMDSDepositTO.getPrematureIntRecType()));
      setTxtPrematureIntRecAmt(String.valueOf(groupMDSDepositTO.getPrematureIntRecAmt()));
      setTxtDepositAmt(String.valueOf(groupMDSDepositTO.getDepositAmt()));
      setCboDueIntCalcType(groupMDSDepositTO.getIsIntCalcIfdueType());
      setTxtDueIntCalcType(String.valueOf(groupMDSDepositTO.getIntCalcRateForDue()));
      setChkIsIntRecovery(groupMDSDepositTO.getIsIntRecovery());
      setChkIsIntForDue(groupMDSDepositTO.getIsIntCalcIfdue());
      notifyObservers();
    }
    
    /** While selecting a row from the table, the corresponding Freeze detail will be
     * populated in the fields
     */
    public void populateFreezeDetails(int row){
        try{
            ObjLoanEligibilityTO = (LoanEligibilityTO)cropList.get(row);
            populateFreezeData(ObjLoanEligibilityTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To set the corresponding Freeze observable instance variables */
    private void populateFreezeData(LoanEligibilityTO ObjLoanEligibilityTO) throws Exception{
        this.getCbmCropType().setKeyForSelected(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getCropType()));
        this.setTxtEligibileAmount(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getEligibleAmt()));
        this.setTdtFromDate(DateUtil.getStringDate(ObjLoanEligibilityTO.getFromDate()));
        this.setTdtToDate(DateUtil.getStringDate(ObjLoanEligibilityTO.getToDate()));
    }
    
    /**
     * To fill the Data for the record, Selected for Authorization.
     */
    public void  setAuthRowData(String freezeId)throws Exception{
        ArrayList freezeDataList = new ArrayList();
        freezeDataList = tblFreezeListModel.getDataArrayList();
        int length = freezeDataList.size();
        for(int i =0; i< length; i++){
            if( ((String)tblFreezeListModel.getValueAt(i, 1)).equalsIgnoreCase(freezeId)){
                populateFreezeData((LoanEligibilityTO)cropList.get(i));
                setAuthRow(i);
                break;
            }
        }
        ttNotifyObservers();
    }
    
    public void resetFreezeDetails(){
    }
    
    
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
    
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public GroupMDSDepositTO getGroupMDSDepositTO(String command){
        GroupMDSDepositTO objgetGroupMDSDepositTO = new GroupMDSDepositTO();    
        objgetGroupMDSDepositTO.setCommand(command);
        if (command == "INSERT") {
            objgetGroupMDSDepositTO.setNextGDSNo(CommonUtil.convertObjToInt(1));
        }
        objgetGroupMDSDepositTO.setGroupNo(CommonUtil.convertObjToStr(getGroupId()));
        objgetGroupMDSDepositTO.setGroupType(getGroupType());         
        objgetGroupMDSDepositTO.setGroupName(getTxtGroupName());
        objgetGroupMDSDepositTO.setSchemeCount(CommonUtil.convertObjToInt(getTxtCount()));
        objgetGroupMDSDepositTO.setProductType(CommonUtil.convertObjToStr(getCbmProductType().getKeyForSelected()));        
        objgetGroupMDSDepositTO.setInterestAmountType(CommonUtil.convertObjToStr(getCbmInterestAmount().getKeyForSelected()));         
        objgetGroupMDSDepositTO.setInterestAmount(CommonUtil.convertObjToDouble(getTxtInterestAmount()));
        objgetGroupMDSDepositTO.setPenalCalculationType(CommonUtil.convertObjToStr(getCbmPenalCalculation().getKeyForSelected()));     
        objgetGroupMDSDepositTO.setPrizedPenal(CommonUtil.convertObjToDouble(getTxtPrizedPenal()));
        objgetGroupMDSDepositTO.setNonPrizedPenal(CommonUtil.convertObjToDouble(getTxtNonPrizedPenal()));
        objgetGroupMDSDepositTO.setInterestRecoveryType(CommonUtil.convertObjToStr(getCbmInterestRecovery().getKeyForSelected()));    
        objgetGroupMDSDepositTO.setInterestRecovery(CommonUtil.convertObjToDouble(getTxtInterestRecovery()));
        objgetGroupMDSDepositTO.setPrematureIntRecType(CommonUtil.convertObjToStr(getCbmPrematureIntRecType().getKeyForSelected()));
        objgetGroupMDSDepositTO.setPrematureIntRecAmt(CommonUtil.convertObjToDouble(getTxtPrematureIntRecAmt()));
        objgetGroupMDSDepositTO.setDepositAmt(CommonUtil.convertObjToDouble(getTxtDepositAmt()));
        objgetGroupMDSDepositTO.setStartDate(getProperDateFormat(getTdtStartDate()));
        objgetGroupMDSDepositTO.setEndDate(DateUtil.getDateMMDDYYYY(getTdtEndDate()));
        objgetGroupMDSDepositTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetGroupMDSDepositTO.setStatusDt(curDate);    
        objgetGroupMDSDepositTO.setGroupNo(groupId);
        objgetGroupMDSDepositTO.setIsIntRecovery(getChkIsIntRecovery());
        objgetGroupMDSDepositTO.setIsIntCalcIfdue(getChkIsIntForDue());
        objgetGroupMDSDepositTO.setIntCalcRateForDue(CommonUtil.convertObjToDouble(getTxtDueIntCalcType()));
        objgetGroupMDSDepositTO.setIsIntCalcIfdueType(getCboDueIntCalcType());
        return objgetGroupMDSDepositTO;
        
    }
    
    
    
    public void execute(String command) {
        try {
             HashMap term = new HashMap();
            if (getAuthorizeMap() == null) {
               
                term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                term.put("groupMDSDepositTO", getGroupMDSDepositTO(command));
                term.put("COMMAND", command);
                term.put(CommonConstants.MODULE, getModule());
                term.put(CommonConstants.SCREEN, getScreen());
                term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                System.out.println("term ::: " + term);
            }
            term.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            int countRec=0;
            HashMap proxyResultMap=null;
            if(countRec==0){
                proxyResultMap = proxy.execute(term, map);
                if(proxyResultMap != null && proxyResultMap.containsKey("GROUP_ID") && proxyResultMap.get("GROUP_ID") != null){
                   ClientUtil.showMessageWindow("Group No :: " + proxyResultMap.get("GROUP_ID")) ;
                }                
            }
            else
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
            setResult(getActionType());
            
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public void resetForm(){
      setRdoMds("");
      setRdoDeposit("");
      setTxtGroupName("");
      setTxtCount("");
      setCboProductType("");
      setCboInterestAmount("");
      setTxtInterestAmount("");
      setCboPenalCalculation("");
      setTxtPrizedPenal("");
      setTxtNonPrizedPenal("");
      setCboInterestRecovery("");
      setTxtInterestRecovery("");
      setCboPrematureIntRecType("");
      setTxtPrematureIntRecAmt("");
      setTxtDepositAmt("");
      setTdtStartDate(""); 
      setTdtEndDate("");
      setCboDueIntCalcType("");
      setTxtDueIntCalcType("");
      setChkIsIntForDue("N");
      setChkIsIntRecovery("N");
    }
    
   
    
    /** Called by CellRenderer UI to find whether a freeze data is deleted or not */
    public boolean isDeleted(int row){
                return false;
    }
    
    public void resetData() {
        this.getCbmCropType().setKeyForSelected("");
        this.setTxtEligibileAmount("");
        this.setTdtFromDate("");
        this.setTdtToDate("");
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
    
    
    public String getLblFreezeId() {
        return this.lblFreezeId;
    }
    
    public void setLblFreezeId(String lblFreezeId) {
        this.lblFreezeId = lblFreezeId;
        setChanged();
    }
    
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    public void setAuthRow(int row){
        this.row = row;
    }
    
    public int getAuthRow(){
        return this.row;
    }
    
    public String getLblAuth() {
        return this.lblAuth;
    }
    
    public void setLblAuth(String lblAuth) {
        this.lblAuth = lblAuth;
        setChanged();
    }
    
    public String getLblFreezeStatus() {
        return this.lblFreezeStatus;
    }
    
    public void setLblFreezeStatus(String lblFreezeStatus) {
        this.lblFreezeStatus = lblFreezeStatus;
        setChanged();
    }
    
    public void setNewObj(){
        cropList = new ArrayList();
        freezeDeleteList = new ArrayList();
    }
    
    
    public boolean verifyAccountDate(HashMap dataMap){
        boolean val = false;
        try {            
            final List resultList = ClientUtil.executeQuery("Freeze.getAccountOpeningDate", dataMap);
            if(resultList != null && resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);               
                if(CommonUtil.convertObjToStr(resultMap.get("ACCT_NAME")).length() > 0){
                    val = true;
                }
            }
        } catch (Exception e) {            
            parseException.logException(e,true);
        }
        
        return val;
    }
    
    
    
    /**
     * Getter for property lblAuthorizeStatus.
     * @return Value of property lblAuthorizeStatus.
     */
    public java.lang.String getLblAuthorizeStatus() {
        return lblAuthorizeStatus;
    }
    
    /**
     * Setter for property lblAuthorizeStatus.
     * @param lblAuthorizeStatus New value of property lblAuthorizeStatus.
     */
    public void setLblAuthorizeStatus(java.lang.String lblAuthorizeStatus) {
        this.lblAuthorizeStatus = lblAuthorizeStatus;
    }
    
    public double FreezeTabSum(){
        double amt = 0;
        try{
            int rowCount = tblFreezeListModel.getRowCount();
            for(int i=0; i< rowCount ; i++){
                String FreezeNo = CommonUtil.convertObjToStr(tblFreezeListModel.getValueAt(i, 1));
                if (FreezeNo.length()>=0)
                    amt = amt + CommonUtil.convertObjToDouble(tblFreezeListModel.getValueAt(i, 2)).doubleValue();
            }
        }catch(Exception e){
            System.out.println("error in FreezeTabSum()");
            e.printStackTrace();
        }
        
        return amt;
    }
    
    /**
     * Getter for property advances.
     * @return Value of property advances.
     */
    public java.lang.String getAdvances() {
        return advances;
    }
    
    /**
     * Setter for property advances.
     * @param advances New value of property advances.
     */
    public void setAdvances(java.lang.String advances) {
        this.advances = advances;
    }
    
    /**
     * Getter for property clearBalance1.
     * @return Value of property clearBalance1.
     */
    public java.lang.String getClearBalance1() {
        return clearBalance1;
    }
    
    /**
     * Setter for property clearBalance1.
     * @param clearBalance1 New value of property clearBalance1.
     */
    public void setClearBalance1(java.lang.String clearBalance1) {
        this.clearBalance1 = clearBalance1;
    }
    
    /**
     * Getter for property LienSum.
     * @return Value of property LienSum.
     */
    public java.lang.String getLienSum() {
        return LienSum;
    }
    
    /**
     * Setter for property LienSum.
     * @param LienSum New value of property LienSum.
     */
    public void setLienSum(java.lang.String LienSum) {
        this.LienSum = LienSum;
    }
    
    /**
     * Getter for property cboCropType.
     * @return Value of property cboCropType.
     */
    public java.lang.String getCboCropType() {
        return cboCropType;
    }
    
    /**
     * Setter for property cboCropType.
     * @param cboCropType New value of property cboCropType.
     */
    public void setCboCropType(java.lang.String cboCropType) {
        this.cboCropType = cboCropType;
    }
    
    /**
     * Getter for property cbmCropType.
     * @return Value of property cbmCropType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCropType() {
        return cbmCropType;
    }
    
    /**
     * Setter for property cbmCropType.
     * @param cbmCropType New value of property cbmCropType.
     */
    public void setCbmCropType(com.see.truetransact.clientutil.ComboBoxModel cbmCropType) {
        this.cbmCropType = cbmCropType;
    }
    
    /**
     * Getter for property txtEligibileAmount.
     * @return Value of property txtEligibileAmount.
     */
    public java.lang.String getTxtEligibileAmount() {
        return txtEligibileAmount;
    }
    
    /**
     * Setter for property txtEligibileAmount.
     * @param txtEligibileAmount New value of property txtEligibileAmount.
     */
    public void setTxtEligibileAmount(java.lang.String txtEligibileAmount) {
        this.txtEligibileAmount = txtEligibileAmount;
        setChanged();
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
     * Getter for property freezeListColumn.
     * @return Value of property freezeListColumn.
     */
    public ArrayList getFreezeListColumn() {
        return freezeListColumn;
    }
    
    /**
     * Setter for property freezeListColumn.
     * @param freezeListColumn New value of property freezeListColumn.
     */
    public void setFreezeListColumn(ArrayList freezeListColumn) {
        this.freezeListColumn = freezeListColumn;
    }
    
    /**
     * Getter for property cropList.
     * @return Value of property cropList.
     */
    public ArrayList getCropList() {
        return cropList;
    }
    
    /**
     * Setter for property cropList.
     * @param cropList New value of property cropList.
     */
    public void setCropList(ArrayList cropList) {
        this.cropList = cropList;
    }

    public ComboBoxModel getCbmInterestAmount() {
        return cbmInterestAmount;
    }

    public void setCbmInterestAmount(ComboBoxModel cbmInterestAmount) {
        this.cbmInterestAmount = cbmInterestAmount;
    }

    public ComboBoxModel getCbmInterestRecovery() {
        return cbmInterestRecovery;
    }

    public void setCbmInterestRecovery(ComboBoxModel cbmInterestRecovery) {
        this.cbmInterestRecovery = cbmInterestRecovery;
    }

    public ComboBoxModel getCbmPenalCalculation() {
        return cbmPenalCalculation;
    }

    public void setCbmPenalCalculation(ComboBoxModel cbmPenalCalculation) {
        this.cbmPenalCalculation = cbmPenalCalculation;
    }

    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }

    public java.lang.String getRdoDeposit() {
        return rdoDeposit;
    }

    public void setRdoDeposit(java.lang.String rdoDeposit) {
        this.rdoDeposit = rdoDeposit;
    }

    public java.lang.String getRdoMds() {
        return rdoMds;
    }

    public void setRdoMds(java.lang.String rdoMds) {
        this.rdoMds = rdoMds;
    }

    public java.lang.String getTxtCount() {
        return txtCount;
    }

    public void setTxtCount(java.lang.String txtCount) {
        this.txtCount = txtCount;
    }

    public java.lang.String getTxtGroupName() {
        return txtGroupName;
    }

    public void setTxtGroupName(java.lang.String txtGroupName) {
        this.txtGroupName = txtGroupName;
    }

    public java.lang.String getTxtInterestAmount() {
        return txtInterestAmount;
    }

    public void setTxtInterestAmount(java.lang.String txtInterestAmount) {
        this.txtInterestAmount = txtInterestAmount;
    }

    public java.lang.String getTxtInterestRecovery() {
        return txtInterestRecovery;
    }

    public void setTxtInterestRecovery(java.lang.String txtInterestRecovery) {
        this.txtInterestRecovery = txtInterestRecovery;
    }

    public String getTxtNonPrizedPenal() {
        return txtNonPrizedPenal;
    }

    public void setTxtNonPrizedPenal(String txtNonPrizedPenal) {
        this.txtNonPrizedPenal = txtNonPrizedPenal;
    }

    public String getTxtPrizedPenal() {
        return txtPrizedPenal;
    }

    public void setTxtPrizedPenal(String txtPrizedPenal) {
        this.txtPrizedPenal = txtPrizedPenal;
    }

   
    public java.lang.String getTdtEndDate() {
        return tdtEndDate;
    }

    public void setTdtEndDate(java.lang.String tdtEndDate) {
        this.tdtEndDate = tdtEndDate;
    }

    public java.lang.String getTdtStartDate() {
        return tdtStartDate;
    }

    public void setTdtStartDate(java.lang.String tdtStartDate) {
        this.tdtStartDate = tdtStartDate;
    }

    public java.lang.String getCboInterestAmount() {
        return cboInterestAmount;
    }

    public void setCboInterestAmount(java.lang.String cboInterestAmount) {
        this.cboInterestAmount = cboInterestAmount;
    }

    public String getCboPenalCalculation() {
        return cboPenalCalculation;
    }

    public void setCboPenalCalculation(String cboPenalCalculation) {
        this.cboPenalCalculation = cboPenalCalculation;
    }

    public String getCboProductType() {
        return cboProductType;
    }

    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }

    public String getCboInterRecovery() {
        return cboInterestRecovery;
    }

    public void setCboInterestRecovery(String cboInterestRecovery) {
        this.cboInterestRecovery = cboInterestRecovery;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTxtDepositAmt() {
        return txtDepositAmt;
    }

    public void setTxtDepositAmt(String txtDepositAmt) {
        this.txtDepositAmt = txtDepositAmt;
    }

    public ComboBoxModel getCbmPrematureIntRecType() {
        return cbmPrematureIntRecType;
    }

    public void setCbmPrematureIntRecType(ComboBoxModel cbmPrematureIntRecType) {
        this.cbmPrematureIntRecType = cbmPrematureIntRecType;
    }

    public String getCboPrematureIntRecType() {
        return cboPrematureIntRecType;
    }

    public void setCboPrematureIntRecType(String cboPrematureIntRecType) {
        this.cboPrematureIntRecType = cboPrematureIntRecType;
    }

    public String getTxtPrematureIntRecAmt() {
        return txtPrematureIntRecAmt;
    }

    public void setTxtPrematureIntRecAmt(String txtPrematureIntRecAmt) {
        this.txtPrematureIntRecAmt = txtPrematureIntRecAmt;
    }    
    
    public boolean populateProdTypeCombo(String prodId) {
        boolean dataExists = false;
        HashMap groupData = new HashMap();
        groupData.put("PROD_ID", prodId);
        groupData.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        List groupDataList = new ArrayList();
        if (prodId.equalsIgnoreCase("DEPOSIT")) {
            groupDataList = ClientUtil.executeQuery("getAllGroupDepositProducts", null);
        } else if (prodId.equalsIgnoreCase("GDS")) {
            groupDataList = ClientUtil.executeQuery("getAllGDSProdIds", null);
        }
        key = new ArrayList();
        value = new ArrayList();
        if (groupDataList != null && groupDataList.size() > 0) {
            dataExists = true;
            for (int i = 0; i < groupDataList.size(); i++) {
                groupData = (HashMap) groupDataList.get(i);
                String key1 = CommonUtil.convertObjToStr(groupData.get("PROD_ID"));
                String val1 = CommonUtil.convertObjToStr(groupData.get("PROD_DESC"));
                cbmProductType = new ComboBoxModel(key, value);
                if (i == 0) {
                    cbmProductType.addKeyAndElement("", "");
                }
                cbmProductType.addKeyAndElement(key1, val1);
            }
        }
        return dataExists;
    }    
    
}
