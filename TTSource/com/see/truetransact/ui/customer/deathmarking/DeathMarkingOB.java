/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * DeathMarkingOB.java
 *
 * Created on May 26, 2004, 5:36 PM
 */

package com.see.truetransact.ui.customer.deathmarking;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.transferobject.customer.deathmarking.DeathMarkingTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.TrueTransactMain;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.customer.deathmarking.DeathMarkingTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DeathMarkingOB extends CObservable{
    
    private String txtCustomerId = "";
    private String dtdDtOfDeath = "";
    private String dtdReportedOn = "";
    private String txtReportedBy = "";
    private String cboRelationShip = "";
    private String txtReferenceNo = "";
    private String txtRemarks = "";
    private HashMap map,lookupMap,lookupValues,lblMap;
    private ArrayList key,value,tblDeathMarkingInfoRow;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result;
    private int _actionType;
    private final static Logger _log = Logger.getLogger(DeathMarkingOB.class);
    private static DeathMarkingOB deathMarkingOB;//singleton object
    private EnhancedTableModel tblDeathMarkingModel;
    private ComboBoxModel cbmRelationship;
    private HashMap _authorizeMap;
    Date curDate = null;
    private ArrayList tblDeathMarkingTitle = new ArrayList();
    private DeathMarkingRB objDeathMarkingRB = new DeathMarkingRB();
    
    /** Creates a new instance of DeathMarkingOB */
    private DeathMarkingOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DeathMarkingJNDI");
        map.put(CommonConstants.HOME, "customer.deathmarking.DeathMarkingHome");
        map.put(CommonConstants.REMOTE,"customer.deathmarking.DeathMarking");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropDown();
            setTblDeathMarkingTitle();
            tblDeathMarkingModel = new EnhancedTableModel(null, tblDeathMarkingTitle);
            removeTblDeathMarkingRow();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
        
    }
    
    static {
        try {
            deathMarkingOB = new DeathMarkingOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static DeathMarkingOB getInstance(){
        return deathMarkingOB;
    }
    
    /*Setter for tblDeathMarkingModel **/
    private void setTblDeathMarkingModel(EnhancedTableModel tblDeathMarkingModel){
        this.tblDeathMarkingModel = tblDeathMarkingModel;
        setChanged();
    }
    
    /* Getter for tblDeathMarkingModel **/
    public EnhancedTableModel getTblDeathMarkingModel(){
        return this.tblDeathMarkingModel;
    }
    
    /** This method sets the Title for the tblDeathMarking Table in the UI **/
    public void setTblDeathMarkingTitle() throws Exception{
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblProductType"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblAccountNo"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblCreateDate"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblMaturityDate"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblRateOfInterest"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblAvailabaleBalance"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblSettlementMode"));
        tblDeathMarkingTitle.add(objDeathMarkingRB.getString("lblNominee"));
    }
    
    // This method removes the row from the tblDeathMarkingTable in UI
    public void removeTblDeathMarkingRow(){
        int i = tblDeathMarkingModel.getRowCount() - 1;
        while(i >= 0){
            tblDeathMarkingModel.removeRow(i);
            i-=1;
        }
    }
    // Setter method for txtCustomerId
    void setTxtCustomerId(String txtCustomerId){
        this.txtCustomerId = txtCustomerId;
        setChanged();
    }
    // Getter method for txtCustomerId
    String getTxtCustomerId(){
        return this.txtCustomerId;
    }
    
    
    // Setter method for dtdDtOfDeath
    void setDtdDtOfDeath(String dtdDtOfDeath){
        this.dtdDtOfDeath = dtdDtOfDeath;
        setChanged();
    }
    
    
    // Getter method for dtdDtOfDeath
    String getDtdDtOfDeath(){
        return this.dtdDtOfDeath;
    }
    
    // Setter method for dtdReportedOn
    void setDtdReportedOn(String dtdReportedOn){
        this.dtdReportedOn = dtdReportedOn;
        setChanged();
    }
    // Getter method for dtdReportedOn
    String getDtdReportedOn(){
        return this.dtdReportedOn;
    }
    
    // Setter method for txtReportedBy
    void setTxtReportedBy(String txtReportedBy){
        this.txtReportedBy = txtReportedBy;
        setChanged();
    }
    // Getter method for txtReportedBy
    String getTxtReportedBy(){
        return this.txtReportedBy;
    }
    
    // Setter method for txtRelationShip
    void setCboRelationShip(String cboRelationShip){
        this.cboRelationShip = cboRelationShip;
        setChanged();
    }
    // Getter method for txtRelationShip
    String getCboRelationShip(){
        return this.cboRelationShip;
    }
    
    /** Getter for property cbmRelationship.
     * @return Value of property cbmRelationship.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRelationship() {
        return cbmRelationship;
    }
    
    /** Setter for property cbmRelationship.
     * @param cbmRelationship New value of property cbmRelationship.
     *
     */
    public void setCbmRelationship(com.see.truetransact.clientutil.ComboBoxModel cbmRelationship) {
        this.cbmRelationship = cbmRelationship;
    }
    
    // Setter method for txtReferenceNo
    void setTxtReferenceNo(String txtReferenceNo){
        this.txtReferenceNo = txtReferenceNo;
        setChanged();
    }
    // Getter method for txtReferenceNo
    String getTxtReferenceNo(){
        return this.txtReferenceNo;
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
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
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
    
    /** Fills the Dropdown by calling up a query */
    private void fillDropDown(){
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            final HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("RELATIONSHIP");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("RELATIONSHIP"));
            cbmRelationship = new ComboBoxModel(key,value);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Fills the combobox with a model */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    /** Returns the Toobject */
    public DeathMarkingTO getDeathMarkingTO(){
        DeathMarkingTO objDeathMarkingTO = new DeathMarkingTO();
        objDeathMarkingTO.setCustId(getTxtCustomerId());
        
        Date DodDt = DateUtil.getDateMMDDYYYY(getDtdDtOfDeath());
        if(DodDt != null){
        Date dodDate = (Date) curDate.clone();
        dodDate.setDate(DodDt.getDate());
        dodDate.setMonth(DodDt.getMonth());
        dodDate.setYear(DodDt.getYear());
//        objDeathMarkingTO.setDeathDt(DateUtil.getDateMMDDYYYY(getDtdDtOfDeath()));
        objDeathMarkingTO.setDeathDt(dodDate);
        }else{
            objDeathMarkingTO.setDeathDt(DateUtil.getDateMMDDYYYY(getDtdDtOfDeath()));
        }
        
        Date RepDt = DateUtil.getDateMMDDYYYY(getDtdReportedOn());
        if(RepDt != null){
        Date repDate = (Date) curDate.clone();
        repDate.setDate(RepDt.getDate());
        repDate.setMonth(RepDt.getMonth());
        repDate.setYear(RepDt.getYear());
//        objDeathMarkingTO.setReportedOn(DateUtil.getDateMMDDYYYY(getDtdReportedOn()));
        objDeathMarkingTO.setReportedOn(repDate);
        }else{
            objDeathMarkingTO.setReportedOn(DateUtil.getDateMMDDYYYY(getDtdReportedOn()));
        }
         
        objDeathMarkingTO.setReportedBy(getTxtReportedBy());
        objDeathMarkingTO.setRelationship(getCboRelationShip());
        objDeathMarkingTO.setReferenceNo(getTxtReferenceNo());
        objDeathMarkingTO.setRemarks(getTxtRemarks());
        objDeathMarkingTO.setStatusBy(TrueTransactMain.USER_ID);
        objDeathMarkingTO.setStatusDt(curDate);
        objDeathMarkingTO.setBranchId(getSelectedBranchID());
        objDeathMarkingTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        return objDeathMarkingTO;
        
    }
    
    /** Sets the to object */
    public void setDeathMarkingTO(DeathMarkingTO objDeathMarkingTO){
        setTxtCustomerId(objDeathMarkingTO.getCustId());
        setDtdDtOfDeath(DateUtil.getStringDate(objDeathMarkingTO.getDeathDt()));
        setDtdReportedOn(DateUtil.getStringDate(objDeathMarkingTO.getReportedOn()));
        setTxtReportedBy(objDeathMarkingTO.getReportedBy());
        setCboRelationShip(objDeathMarkingTO.getRelationship());
        setTxtReferenceNo(objDeathMarkingTO.getReferenceNo());
        setTxtRemarks(objDeathMarkingTO.getRemarks());
        notifyObservers();
    }
    
    /** Resets all the uifields */
    public void resetForm(){
        setTxtCustomerId("");
        setDtdDtOfDeath("");
        setDtdReportedOn("");
        setTxtReportedBy("");
        setCboRelationShip("");
        setTxtReferenceNo("");
        setTxtRemarks("");
        removeTblDeathMarkingRow();
        notifyObservers();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform();
                }
                else{
                    final DeathMarkingRB objDeathMarkingRB = new DeathMarkingRB();
                    throw new TTException(objDeathMarkingRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To get the value of action performed */
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
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
        }else {
            final DeathMarkingTO objDeathMarkingTO = getDeathMarkingTO();
            objDeathMarkingTO.setCommand(getCommand());
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objDeathMarkingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDeathMarkingTO.setCreatedDt(curDate);
            }
            data.put("DeathMarkingTO",objDeathMarkingTO);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setResult(_actionType);
        _actionType = ClientConstants.ACTIONTYPE_NEW;
        resetForm();
    }
    
    /** To retrive Customer Name based on CustomerId*/
    public HashMap getCustomerName(String custId)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", custId);
            List resultList = ClientUtil.executeQuery("getSelectCustomerName", customerMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    /** This will execute a query and returns then as ArrayList, which contains the infromation
     *regarding the customer products which are to be deathmarked */
    public  ArrayList getDeathMarkingInfo(String where)throws Exception{
        HashMap resultMap = null;
        ArrayList resultList = new ArrayList();
        try {
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", where);
            resultList = (ArrayList) ClientUtil.executeQuery("selectDeathMarkInfo", customerMap);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
        return resultList;
    }
    
    /** This will sets the all the elements of the ArrayList in to TblDeathMarkingModel's row **/
    public  void setDeathMarkingInfoList(ArrayList arrayDeathMarkingInfo) {
        int toSize=arrayDeathMarkingInfo.size();
        for (int i=0;i<toSize;i++){
            tblDeathMarkingInfoRow = new ArrayList();
            HashMap columnData =(HashMap) arrayDeathMarkingInfo.get(i);
            tblDeathMarkingInfoRow.add(columnData.get("PRODUCT TYPE"));
            tblDeathMarkingInfoRow.add(columnData.get("ACCOUNT NO."));
            tblDeathMarkingInfoRow.add(columnData.get("CREATE DT."));
            tblDeathMarkingInfoRow.add(columnData.get("MATURITY DT."));
            tblDeathMarkingInfoRow.add(columnData.get("INTEREST"));
            tblDeathMarkingInfoRow.add(columnData.get("AVAILABLE BALANCE"));
            tblDeathMarkingInfoRow.add(columnData.get("SETTLEMENT"));
            tblDeathMarkingInfoRow.add(columnData.get("NOMINEE"));
            tblDeathMarkingModel.insertRow(0,tblDeathMarkingInfoRow);
        }
        
    }
    
    public  Date getCreatedDate(String custId){
        Date createdDt=null;
        HashMap where = new HashMap();
        where.put("CUST_ID", custId);
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectCreatedDt", where);
        System.out.println("List "+ list);
        if(list!=null){
            if(list.size()!=0){
                HashMap resultMap = (HashMap) list.get(0);
                if(resultMap!=null){
                    createdDt = (Date)resultMap.get("CREATEDDT");
                }
            }
        }
        
       return createdDt; 
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            DeathMarkingTO objDeathMarkingTO =
            (DeathMarkingTO) ((List) mapData.get("DeathMarkingTO")).get(0);
            setDeathMarkingTO(objDeathMarkingTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    
}
