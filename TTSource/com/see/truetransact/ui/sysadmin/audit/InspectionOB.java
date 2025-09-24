/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * InspectionOB.java
 *
 * Created on June 9, 2004, 11:03 AM
 */

package com.see.truetransact.ui.sysadmin.audit;

/**
 *
 * @author  Ashok
 */

import java.util.HashMap;
import java.util.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.sysadmin.audit.BranchInspectionTO;
import com.see.truetransact.transferobject.sysadmin.audit.PerformanceInspectionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.COptionPane;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.sysadmin.audit.InspectionRB;
import com.see.truetransact.transferobject.sysadmin.audit.JobInspectionTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.sysadmin.audit.PerformanceInspectionTO;
import com.see.truetransact.transferobject.sysadmin.audit.BranchInspectionTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;


public class InspectionOB extends CObservable {
    
    private HashMap map;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static InspectionOB inspectionOB;
    private String txtBranchCode = "";
    private String tdtInspCommOn = "";
    private String tdtInspConcOn = "";
    private String tdtPositionAsOn = "";
    private String txaOtherInfo = "";
    private String txtManDays = "";
    private String cboBranchRating = "";
    private String txaInspectingOfficials = "";
    private String cboClassification = "";
    private String cboCategory = "";
    private String cboWeeklyHoliday = "";
    private String txtStaffPosition = "";
    private String cboJobCategory = "";
    private String txtActualPosition = "";
    private String lblValueAuditId = "";
    private ComboBoxModel cbmBranchRating,cbmCategory,cbmClassification,cbmWeeklyHoliday,cbmJobCategory;
    private final static Logger _log = Logger.getLogger(InspectionOB.class);
    private HashMap lookUpHash,lookupMap,lookupValues,keyValue;
    private ArrayList key,value,tblAuditJobMasterRow,tblAppraisalInfoRow,tblDevelopmentRow;
    private ArrayList colHead = new ArrayList();
    private ArrayList tblAppraisalHead = new ArrayList();
    private ArrayList tblDevelopmentHead = new ArrayList();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private InspectionRB resourceBundle = new InspectionRB();
    private EnhancedTableModel tblAppraisalInfoModel,tblAuditJobMasterModel,tblDevelopmentModel;
    private ArrayList columnElement,insertData,updateData,deleteData;
    private ArrayList rowData;
    private ArrayList newData = new ArrayList();
    private ArrayList data = new ArrayList();
    private ArrayList existingData;
    private ArrayList  deleteRow;
    private ArrayList  jobInspectionData;
    Date curDate = null;
    
    
    /** Creates a new instance of InspectionOB */
    private InspectionOB()throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InspectionJNDI");
        map.put(CommonConstants.HOME, "sysadmin.audit.InspectionHome");
        map.put(CommonConstants.REMOTE,"sysadmin.audit.Inspection");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setTblAuditJobMasterTitle();
            tblAuditJobMasterModel = new EnhancedTableModel(null,colHead);
            setTblAppraisalInfoTitle();
            tblAppraisalInfoModel = new EnhancedTableModel(null, tblAppraisalHead);
            setTblDevelopmentTitle();
            tblDevelopmentModel = new EnhancedTableModel(null, tblDevelopmentHead);
            removeTblAuditJobMasterRow();
            removeTblAppraisalInfoRow();
            removeTblDevelopmentRow();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            inspectionOB = new InspectionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /**Retuns an instance of ob */
    public static InspectionOB getInstance(){
        return inspectionOB;
    }
    
    /** Getter for property lblValueAuditId.
     * @return Value of property lblValueAuditId.
     *
     */
    public java.lang.String getLblValueAuditId() {
        return lblValueAuditId;
    }
    
    /** Setter for property lblValueAuditId.
     * @param lblValueAuditId New value of property lblValueAuditId.
     *
     */
    public void setLblValueAuditId(java.lang.String lblValueAuditId) {
        this.lblValueAuditId = lblValueAuditId;
        setChanged();
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
    
    // Setter method for tdtInspCommOn
    void setTdtInspCommOn(String tdtInspCommOn){
        this.tdtInspCommOn = tdtInspCommOn;
        setChanged();
    }
    // Getter method for tdtInspCommOn
    String getTdtInspCommOn(){
        return this.tdtInspCommOn;
    }
    
    // Setter method for tdtInspConcOn
    void setTdtInspConcOn(String tdtInspConcOn){
        this.tdtInspConcOn = tdtInspConcOn;
        setChanged();
    }
    // Getter method for tdtInspConcOn
    String getTdtInspConcOn(){
        return this.tdtInspConcOn;
    }
    
    // Setter method for tdtPositionAsOn
    void setTdtPositionAsOn(String tdtPositionAsOn){
        this.tdtPositionAsOn = tdtPositionAsOn;
        setChanged();
    }
    // Getter method for tdtPositionAsOn
    String getTdtPositionAsOn(){
        return this.tdtPositionAsOn;
    }
    
    // Setter method for txaOtherInfo
    void setTxaOtherInfo(String txaOtherInfo){
        this.txaOtherInfo = txaOtherInfo;
        setChanged();
    }
    // Getter method for txaOtherInfo
    String getTxaOtherInfo(){
        return this.txaOtherInfo;
    }
    
    // Setter method for txtManDays
    void setTxtManDays(String txtManDays){
        this.txtManDays = txtManDays;
        setChanged();
    }
    // Getter method for txtManDays
    String getTxtManDays(){
        return this.txtManDays;
    }
    
    // Setter method for cboBranchRating
    void setCboBranchRating(String cboBranchRating){
        this.cboBranchRating = cboBranchRating;
        setChanged();
    }
    // Getter method for cboBranchRating
    String getCboBranchRating(){
        return this.cboBranchRating;
    }
    
    /** Getter for property cbmBranchRating.
     * @return Value of property cbmBranchRating.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchRating() {
        return cbmBranchRating;
    }
    
    /** Setter for property cbmBranchRating.
     * @param cbmBranchRating New value of property cbmBranchRating.
     *
     */
    public void setCbmBranchRating(com.see.truetransact.clientutil.ComboBoxModel cbmBranchRating) {
        this.cbmBranchRating = cbmBranchRating;
    }
    
    
    // Setter method for txaInspectingOfficials
    void setTxaInspectingOfficials(String txaInspectingOfficials){
        this.txaInspectingOfficials = txaInspectingOfficials;
        setChanged();
    }
    // Getter method for txaInspectingOfficials
    String getTxaInspectingOfficials(){
        return this.txaInspectingOfficials;
    }
    
    // Setter method for cboClassification
    void setCboClassification(String cboClassification){
        this.cboClassification = cboClassification;
        setChanged();
    }
    // Getter method for cboClassification
    String getCboClassification(){
        return this.cboClassification;
    }
    
    /** Getter for property cbmClassification.
     * @return Value of property cbmClassification.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmClassification() {
        return cbmClassification;
    }
    
    /** Setter for property cbmClassification.
     * @param cbmClassification New value of property cbmClassification.
     *
     */
    public void setCbmClassification(com.see.truetransact.clientutil.ComboBoxModel cbmClassification) {
        this.cbmClassification = cbmClassification;
    }
    
    // Setter method for cboCategory
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    // Getter method for cboCategory
    String getCboCategory(){
        return this.cboCategory;
    }
    
    /** Getter for property cbmCategory.
     * @return Value of property cbmCategory.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }
    
    /** Setter for property cbmCategory.
     * @param cbmCategory New value of property cbmCategory.
     *
     */
    public void setCbmCategory(com.see.truetransact.clientutil.ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }
    
    // Setter method for cboWeeklyHoliday
    void setCboWeeklyHoliday(String cboWeeklyHoliday){
        this.cboWeeklyHoliday = cboWeeklyHoliday;
        setChanged();
    }
    // Getter method for cboWeeklyHoliday
    String getCboWeeklyHoliday(){
        return this.cboWeeklyHoliday;
    }
    
    /** Getter for property cbmWeeklyHoliday.
     * @return Value of property cbmWeeklyHoliday.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmWeeklyHoliday() {
        return cbmWeeklyHoliday;
    }
    
    /** Setter for property cbmWeeklyHoliday.
     * @param cbmWeeklyHoliday New value of property cbmWeeklyHoliday.
     *
     */
    public void setCbmWeeklyHoliday(com.see.truetransact.clientutil.ComboBoxModel cbmWeeklyHoliday) {
        this.cbmWeeklyHoliday = cbmWeeklyHoliday;
    }
    
    // Setter method for txtStaffPosition
    void setTxtStaffPosition(String txtStaffPosition){
        this.txtStaffPosition = txtStaffPosition;
        setChanged();
    }
    // Getter method for txtStaffPosition
    String getTxtStaffPosition(){
        return this.txtStaffPosition;
    }
    
    // Setter method for cboJobCategory
    void setCboJobCategory(String cboJobCategory){
        this.cboJobCategory = cboJobCategory;
        setChanged();
    }
    // Getter method for cboJobCategory
    String getCboJobCategory(){
        return this.cboJobCategory;
    }
    
    /** Getter for property cbmJobCategory.
     * @return Value of property cbmJobCategory.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmJobCategory() {
        return cbmJobCategory;
    }
    
    /** Setter for property cbmJobCategory.
     * @param cbmJobCategory New value of property cbmJobCategory.
     *
     */
    public void setCbmJobCategory(com.see.truetransact.clientutil.ComboBoxModel cbmJobCategory) {
        this.cbmJobCategory = cbmJobCategory;
    }
    
    // Setter method for txtActualPosition
    void setTxtActualPosition(String txtActualPosition){
        this.txtActualPosition = txtActualPosition;
        setChanged();
    }
    // Getter method for txtActualPosition
    String getTxtActualPosition(){
        return this.txtActualPosition;
    }
    
    void setTblAuditJobMasterModel(EnhancedTableModel tblAuditJobMasterModel){
        this.tblAuditJobMasterModel = tblAuditJobMasterModel;
        setChanged();
    }
    
    EnhancedTableModel getTblAuditJobMasterModel(){
        return this.tblAuditJobMasterModel;
    }
    
    void setTblAppraisalInfoModel(EnhancedTableModel tblAppraisalInfoModel){
        this.tblAppraisalInfoModel = tblAppraisalInfoModel;
        setChanged();
    }
    
    EnhancedTableModel getTblAppraisalInfoModel(){
        return this.tblAppraisalInfoModel;
    }
    
    void setTblDevelopmentModel(EnhancedTableModel tblDevelopmentModel){
        this.tblDevelopmentModel = tblDevelopmentModel;
        setChanged();
    }
    
    EnhancedTableModel getTblDevelopmentModel(){
        return this.tblDevelopmentModel;
    }
    
    
    
    // This method removes the row from the Terminal_Master table in UI
    public void removeTblAuditJobMasterRow(){
        int i = tblAuditJobMasterModel.getRowCount() - 1;
        while(i >= 0){
            tblAuditJobMasterModel.removeRow(i);
            i-=1;
        }
    }
    
    /**  This method removes the row from the AppraisalInfo table in UI */
    public void removeTblAppraisalInfoRow(){
        int i = tblAppraisalInfoModel.getRowCount() - 1;
        while(i >= 0){
            tblAppraisalInfoModel.removeRow(i);
            i-=1;
        }
    }
    
    /**  This method removes the row from the AppraisalInfo table in UI */
    public void removeTblDevelopmentRow(){
        int i = tblDevelopmentModel.getRowCount() - 1;
        while(i >= 0){
            tblDevelopmentModel.removeRow(i);
            i-=1;
        }
    }
    
    
    
    /** Sets the PerformanceInspectionTO fields thru OB */
    public void setPerformanceInspectionTO(PerformanceInspectionTO objPerformanceInspectionTO){
        setLblValueAuditId(objPerformanceInspectionTO.getAuditId());
        setTxtBranchCode(objPerformanceInspectionTO.getBranchCode());
        setTdtInspCommOn(DateUtil.getStringDate(objPerformanceInspectionTO.getInspectionCommenced()));
        setTdtInspConcOn(DateUtil.getStringDate(objPerformanceInspectionTO.getInspectionConcluded()));
        setTdtPositionAsOn(DateUtil.getStringDate(objPerformanceInspectionTO.getPositionAsOn()));
        setTxtManDays(CommonUtil.convertObjToStr(objPerformanceInspectionTO.getNoOfManDays()));
        setCboBranchRating(objPerformanceInspectionTO.getBranchRating());
        setTxaInspectingOfficials(objPerformanceInspectionTO.getInspectingOfficers());
        setTxaOtherInfo(objPerformanceInspectionTO.getOtherInfo());
    }
    
    /** Retruns an instance of PerforrmanceInspectionTO */
    public PerformanceInspectionTO getPerformanceInspectionTO(String command){
        PerformanceInspectionTO objPerformanceInspectionTO = new PerformanceInspectionTO();
        objPerformanceInspectionTO.setCommand(command);
        if(objPerformanceInspectionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objPerformanceInspectionTO.setStatus(CommonConstants.STATUS_CREATED);
            objPerformanceInspectionTO.setCreatedBy(TrueTransactMain.USER_ID);
            objPerformanceInspectionTO.setCreatedDt(curDate);
        }else if(objPerformanceInspectionTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
            objPerformanceInspectionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objPerformanceInspectionTO.setAuditId(getLblValueAuditId());
        }else if(objPerformanceInspectionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
            objPerformanceInspectionTO.setStatus(CommonConstants.STATUS_DELETED);
            objPerformanceInspectionTO.setAuditId(getLblValueAuditId());
        }
        objPerformanceInspectionTO.setStatusBy(TrueTransactMain.USER_ID);
        objPerformanceInspectionTO.setStatusDt(curDate);
        objPerformanceInspectionTO.setBranchCode(getTxtBranchCode());
//        objPerformanceInspectionTO.setInspectionCommenced(DateUtil.getDateMMDDYYYY(getTdtInspCommOn()));
//        objPerformanceInspectionTO.setInspectionConcluded(DateUtil.getDateMMDDYYYY(getTdtInspConcOn()));
//        objPerformanceInspectionTO.setPositionAsOn(DateUtil.getDateMMDDYYYY(getTdtPositionAsOn()));
        
        Date IcomDt = DateUtil.getDateMMDDYYYY(getTdtInspCommOn());
        if(IcomDt != null){
        Date icomDate = (Date)curDate.clone();
        icomDate.setDate(IcomDt.getDate());
        icomDate.setMonth(IcomDt.getMonth());
        icomDate.setYear(IcomDt.getYear());
        objPerformanceInspectionTO.setInspectionCommenced(icomDate);
        }else{
            objPerformanceInspectionTO.setInspectionCommenced(DateUtil.getDateMMDDYYYY(getTdtInspCommOn()));
        }
        
        Date ConcDt = DateUtil.getDateMMDDYYYY(getTdtInspConcOn());
        if(ConcDt != null){
        Date concDate = (Date)curDate.clone();
        concDate.setDate(ConcDt.getDate());
        concDate.setMonth(ConcDt.getMonth());
        concDate.setYear(ConcDt.getYear());
        objPerformanceInspectionTO.setInspectionConcluded(concDate);
        }else{
            objPerformanceInspectionTO.setInspectionConcluded(DateUtil.getDateMMDDYYYY(getTdtInspConcOn()));
        }
        
        Date PosDt = DateUtil.getDateMMDDYYYY(getTdtPositionAsOn());
        if(PosDt != null){
        Date posDate = (Date)curDate.clone();
        posDate.setDate(PosDt.getDate());
        posDate.setMonth(PosDt.getMonth());
        posDate.setYear(PosDt.getYear());
        objPerformanceInspectionTO.setPositionAsOn(posDate);
        }else{
            objPerformanceInspectionTO.setPositionAsOn(DateUtil.getDateMMDDYYYY(getTdtPositionAsOn()));
        }
        
        objPerformanceInspectionTO.setNoOfManDays(new Double(getTxtManDays()));
        objPerformanceInspectionTO.setBranchRating(getCboBranchRating());
        objPerformanceInspectionTO.setInspectingOfficers(getTxaInspectingOfficials());
        objPerformanceInspectionTO.setOtherInfo(getTxaOtherInfo());
        return objPerformanceInspectionTO;
    }
    
    /* Return an instance of the class BranchInspectionTO **/
    public BranchInspectionTO getBranchInspectionTO(String command){
        BranchInspectionTO objBranchInspectionTO = new BranchInspectionTO();
        objBranchInspectionTO.setCommand(command);
        if(!objBranchInspectionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objBranchInspectionTO.setAuditId(getLblValueAuditId());
        }
        objBranchInspectionTO.setClassification(getCboClassification());
        objBranchInspectionTO.setCategory(getCboCategory());
        objBranchInspectionTO.setWeeklyHoliday(getCboWeeklyHoliday());
        objBranchInspectionTO.setStaffPosition(new Double(getTxtStaffPosition()));
        return objBranchInspectionTO;
    }
    
    /** Sets the Fields of BranchInspectionTO class thur OB */
    public void setBranchInspectionTO(BranchInspectionTO objBranchInspectionTO){
        setCboClassification(objBranchInspectionTO.getClassification());
        setCboCategory(objBranchInspectionTO.getCategory());
        setCboWeeklyHoliday(objBranchInspectionTO.getWeeklyHoliday());
        setTxtStaffPosition(CommonUtil.convertObjToStr(objBranchInspectionTO.getStaffPosition()));
    }
    
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            final HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getDesignation");
            param.put(CommonConstants.PARAMFORQUERY, null);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get(CommonConstants.DATA));
            cbmJobCategory = new ComboBoxModel(key,value);
            
            param.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("BRANCH_RATING");
            lookupKey.add("BRANCH_CLASSIFICATION");
            lookupKey.add("BRANCH_CATEGORY");
            lookupKey.add("WEEK_DAYS");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("BRANCH_RATING"));
            cbmBranchRating = new ComboBoxModel(key,value);
            fillData((HashMap)lookupValues.get("BRANCH_CLASSIFICATION"));
            cbmClassification = new ComboBoxModel(key,value);
            fillData((HashMap)lookupValues.get("BRANCH_CATEGORY"));
            cbmCategory = new ComboBoxModel(key,value);
            fillData((HashMap)lookupValues.get("WEEK_DAYS"));
            cbmWeeklyHoliday = new ComboBoxModel(key,value);
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
    
    /** Resets the Fields of OB to empty String */
    public void resetForm(){
        setLblValueAuditId("");
        setTxtBranchCode("");
        setTdtInspCommOn("");
        setTdtInspConcOn("");
        setTdtPositionAsOn("");
        setTxtManDays("");
        setCboBranchRating("");
        setTxaInspectingOfficials("");
        setTxaOtherInfo("");
        setCboClassification("");
        setCboCategory("");
        setCboWeeklyHoliday("");
        setTxtStaffPosition("");
        ttNotifyObservers();
    }
    
    /** To retrive BranchName,City,OpeningDate and WorkingHours based on BranchCode from BranchMaster Table */
    public HashMap getBranchDetails(String branchCode)throws Exception {
        HashMap resultMap = null;
        try {
            HashMap branchMap = new HashMap();
            branchMap.put("BRANCH_CODE", branchCode);
            List resultList = ClientUtil.executeQuery("getBranchDetailsForAudit", branchMap);
            if(resultList.size() > 0){
                resultMap = (HashMap)resultList.get(0);
            }
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /** To retrieve NoOfManDays, BranchRating, InspectingOfficials from Audit_Performance Table */
    public  ArrayList getAuditPerformance(String where)throws Exception{
        HashMap resultMap = null;
        ArrayList resultList = new ArrayList();
        try {
            HashMap branchMap = new HashMap();
            branchMap.put("BRANCH_CODE", where);
            resultList = (ArrayList) ClientUtil.executeQuery("getSelectAuditPerformance", branchMap);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
        return resultList;
    }
    
    
    /** To retun an Arraylist by executing three queries to fill up the TblDevelopmentModel */
    private  ArrayList getDevelopmentTblArrayList(String from,String to)throws Exception{
        HashMap resultMap = null;
        ArrayList resultList = new ArrayList();
        ArrayList rowData = new ArrayList();
        try {
            HashMap inspectionMap = new HashMap();
            inspectionMap.put("INSPECTION_FROM", from);
            inspectionMap.put("INSPECTION_TO", to);
            String period = from + "-" + to;
            rowData.add(period);
            resultList = (ArrayList) ClientUtil.executeQuery("getSelectAcInfo", inspectionMap);
            if(resultList!=null){
                if(resultList.size()!=0){
                    resultMap =(HashMap) resultList.get(0);
                    rowData.add(CommonUtil.convertObjToStr(resultMap.get("TOTALDEPOSITS")));
                }else{
                    rowData.add("");
                }
            }else{
                rowData.add("");
            }
            resultList = null;
            resultList = (ArrayList) ClientUtil.executeQuery("getSelectAdvances", inspectionMap);
            if(resultList!=null){
                if(resultList.size()!=0){
                    resultMap =(HashMap) resultList.get(0);
                    rowData.add(CommonUtil.convertObjToStr(resultMap.get("TOTALADVANCES")));
                }else{
                    rowData.add("");
                }
            }else{
                rowData.add("");
            }
            resultList = null;
            resultList = (ArrayList) ClientUtil.executeQuery("getSelectPriority", inspectionMap);
            if(resultList!=null){
                if(resultList.size()!=0){
                    resultMap =(HashMap) resultList.get(0);
                    rowData.add(CommonUtil.convertObjToStr(resultMap.get("PRIORITYSECTORADVANCES")));
                }else{
                    rowData.add("");
                }
            }else{
                rowData.add("");
            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return rowData;
        
    }
    
    /** This will insert rows into TblDevelopmentModel */
    public void setTblModel(String from ,String to){
        try{
            ArrayList data = getDevelopmentTblArrayList(from,to);
            tblDevelopmentModel.insertRow(0,data);
        }catch(Exception e){
            
        }
    }
    
    /** This will insert rows into TblDevelopmentModel by executing  a query based on
     * the condition of previous inserted Date values */
    public void fillTblDevelopmentModel(){
        ArrayList resultList = new ArrayList();
        HashMap resultMap = null;
        resultList= (ArrayList) ClientUtil.executeQuery("getSelectDates", null);
        for(int i=0; i<resultList.size(); i++){
            resultMap = (HashMap) resultList.get(i);
            String commencedOn =CommonUtil.convertObjToStr(resultMap.get("INSPECTION_COMMENCED"));
            String concludedOn  = CommonUtil.convertObjToStr(resultMap.get("INSPECTION_CONCLUDED"));
            setTblModel(commencedOn,concludedOn);
        }
        
    }
    
    
    /** Setting the Title to the tblAuditJobMaster */
    private void setTblAuditJobMasterTitle(){
        colHead.add(resourceBundle.getString("lblJobCategory"));
        colHead.add(resourceBundle.getString("lblActualPosition"));
    }
    
    /** Setting the Title to the tblAppraisalInfo */
    private  void setTblAppraisalInfoTitle(){
        tblAppraisalHead.add(resourceBundle.getString("lblManDays"));
        tblAppraisalHead.add(resourceBundle.getString("lblBranchRating"));
        tblAppraisalHead.add(resourceBundle.getString("lblInspectingOfficials"));
    }
    
    /** Setting the Title to the tblDevelopment */
    private  void setTblDevelopmentTitle(){
        tblDevelopmentHead.add(resourceBundle.getString("columnHeading1"));
        tblDevelopmentHead.add(resourceBundle.getString("columnHeading2"));
        tblDevelopmentHead.add(resourceBundle.getString("columnHeading3"));
        tblDevelopmentHead.add(resourceBundle.getString("columnHeading4"));
    }
    
    
    
    /** Setting the values of the row selected in the tblAuditJobMaster */
    public void populateTblAuditJobMasterModel(int row){
        ArrayList data = (ArrayList)tblAuditJobMasterModel.getDataArrayList().get(row);
        setCboJobCategory(CommonUtil.convertObjToStr(data.get(0)));
        setTxtActualPosition(CommonUtil.convertObjToStr(data.get(1)));
        setChanged();
        ttNotifyObservers();
    }
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** Setting the updated values of the row selected in the table */
    public void setTableValueAt(){
        final ArrayList tableData = tblAuditJobMasterModel.getDataArrayList();
        final int tableRow = tableData.size();
        for (int i=0;i<tableRow;i++){
            if ((((ArrayList)tableData.get(i)).get(0)).equals(cboJobCategory)){
                if(existingData!=null){
                    existingData.add(tableData.get(i));
                }
                tblAuditJobMasterModel.setValueAt(cboJobCategory, i, 0);
                tblAuditJobMasterModel.setValueAt(new Double(txtActualPosition), i, 1);
                setChanged();
                ttNotifyObservers();
            }
        }
    }
    
    
    /** This method gets necessary fields and accordingly this data is Inserted,
     * Updated or Deleted
     */
    public int addAuditJobMaster(){
        int optionSelected = -1;
        String columnData = getCboJobCategory();
        try{
            tblAuditJobMasterRow = new ArrayList();
            tblAuditJobMasterRow.add(cboJobCategory);
            tblAuditJobMasterRow.add(new Double(getTxtActualPosition()));
            ArrayList data = tblAuditJobMasterModel.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if ((((ArrayList)data.get(i)).get(0)).equals(columnData)){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo"),resourceBundle.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // option selected is Yes
                        updateLookupMasterTab(i);
                        doUpdateData(i);
                    }else if(optionSelected == 1){
                        // option selected is No
                        setCboJobCategory("");
                        setTxtActualPosition("");
                    }
                    break;
                }
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertTerminalMasterTab();
            }
            setChanged();
            ttNotifyObservers();
            tblAuditJobMasterRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    
    /** This mehod updates the Data tblAuditJobMaster Table in the UI
     *according to the data entered in the TextFields of the UI **/
    private void updateLookupMasterTab(int row) throws Exception{
        //TerminalTO objTerminalMasterTO = new TerminalTO();
        tblAuditJobMasterModel.setValueAt(cboJobCategory, row, 0);
        tblAuditJobMasterModel.setValueAt(txtActualPosition, row, 1);
        setChanged();
        ttNotifyObservers();
    }
    
    /** This method get the Updated data entered into the tblAuditJobMaster Table in the UI **/
    private void doUpdateData(int row){
        if(existingData!=null){
            existingData.add(tblAuditJobMasterModel.getDataArrayList().get(row));
        }
    }
    
    /** this method get the existing data from the Table **/
    public void existingData(){
        existingData = new ArrayList();
        deleteRow = new ArrayList();
        rowData = new ArrayList();
        int rowCount = tblAuditJobMasterModel.getRowCount();
        for(int i=0;i<rowCount;i++){
            columnElement = new ArrayList();
            columnElement.add(tblAuditJobMasterModel.getValueAt(i,0));
            columnElement.add(tblAuditJobMasterModel.getValueAt(i,1));
            rowData.add(columnElement);
        }
    }
    
    /** This method get the new data entered **/
    private void doNewData(){
        newData.add(tblAuditJobMasterRow);
    }
    
    /** Insert into the tblAuditJobMaster table in the UI **/
    private void insertTerminalMasterTab() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tblAuditJobMasterModel.getRowCount();
        tblAuditJobMasterModel.insertRow(row,tblAuditJobMasterRow);
        setCboJobCategory("");
        setTxtActualPosition("");
    }
    
    /** This method is called when the save button is Clicked **/
    private void doSave(){
        initialise();
        if(getActionType()!= ClientConstants.ACTIONTYPE_DELETE){
            insertData();
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                updateData();
                deleteData();
            }
            deinitialise();
        }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            if(rowData!=null){
                if(rowData.size()>0){
                    for(int i=0;i<rowData.size();i++){
                        JobInspectionTO objJobInspectionTO;
                        objJobInspectionTO = new JobInspectionTO();
                        objJobInspectionTO.setStatus(CommonConstants.STATUS_DELETED);
                        objJobInspectionTO.setAuditId(getLblValueAuditId());
                        objJobInspectionTO.setJobCategory(CommonUtil.convertObjToStr(((ArrayList)rowData.get(i)).get(0)));
                        objJobInspectionTO.setActualPosition(CommonUtil.convertObjToDouble(((ArrayList)rowData.get(i)).get(1)));
                        jobInspectionData.add(objJobInspectionTO);
                    }
                }
            }
        }
    }
    
    /** Initialises the ArrayLists for insertion,deletion,updation */
    private void initialise(){
        insertData = new ArrayList();
        updateData = new ArrayList();
        deleteData = new ArrayList();
        jobInspectionData = new ArrayList();
    }
    
    /** This method gets the data that has to be inserted into the database
     *  data the data that is already existing
     *  newData the data that is newly added
     *  insertData the data that is has to be inserted into the database
     */
    public void insertData(){
        data = tblAuditJobMasterModel.getDataArrayList();
        int rowData = data.size();
        int rowNewData = newData.size();
        for(int i=0;i<rowData;i++){
            for(int j=0;j<rowNewData;j++){
                if(((ArrayList)newData.get(j)).get(0).equals(((ArrayList)data.get(i)).get(0))){
                    insertData.add(newData.get(j));
                }
            }
        }
        data = null;
        newData.clear();
        if(insertData != null && insertData.size()>0){
            for(int i=0; i<insertData.size();i++){
                JobInspectionTO objJobInspectionTO;
                objJobInspectionTO = new JobInspectionTO();
                if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    objJobInspectionTO.setAuditId(getLblValueAuditId());
                }
                objJobInspectionTO.setStatus(CommonConstants.STATUS_CREATED);
                objJobInspectionTO.setJobCategory(CommonUtil.convertObjToStr(((ArrayList)insertData.get(i)).get(0)));
                objJobInspectionTO.setActualPosition(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(1)));
                jobInspectionData.add(objJobInspectionTO);
            }
        }
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                // If getCommand() is not equal to null, doing the necessary action
                if( getCommand() != null ){
                    doActionPerform();
                }
                //If getCommand() equals to null throwing TTException
                else{
                    final InspectionRB resourceBundle = new InspectionRB();
                    throw new TTException(resourceBundle.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** This method returns the command  either Insert,Update or Delete according to ActionType **/
    private String getCommand() throws Exception{
        String command = null;
        /** Getting over the _actionType New,Edit or Delete and seting
         * the command variable to status either to Insert,Update or Delete */
        switch (_actionType) {
            /** If action type is new, setting command variable to status Insert */
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
                /** if action type is edit, setting the command variable to status Update */
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
                /** If action type is delete, setting the command variable to status Delete */
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }
    
    /** To perform the necessary action */
    public void doActionPerform() throws Exception{
        try{
            // final ArrayList arrayAuditJobMasterTO = setTblAuditJobMasterData();
            final HashMap data = new HashMap();
            data.put("PerformanceInspectionTO", getPerformanceInspectionTO(getCommand()));
            data.put("BranchInspectionTO" , getBranchInspectionTO(getCommand()));
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            doSave();
            data.put("JobInspectionTO", jobInspectionData);
            HashMap proxyResultMap = proxy.execute(data,map);
            jobInspectionData = null;
            setResult(_actionType);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* To set common data in the Transfer Object*/
    public ArrayList setTblAuditJobMasterData() {
        tblAuditJobMasterRow = new ArrayList();
        final int dataSizeInsert = insertData.size();
        final int dataSizeUpdate = updateData.size();
        final int dataSizeDelete = deleteData.size();
        
        if(insertData.size() > 0){
            //  If the action type is insert
            setJobInspectionTO(insertData, dataSizeInsert);
        }
        if(updateData.size() > 0){
            //  If the action type is update
            setJobInspectionTO(updateData, dataSizeUpdate);
        }
        if(deleteData.size() > 0){
            //  If the action type is delete
            setJobInspectionTO(deleteData, dataSizeDelete);
        }
        return tblAuditJobMasterRow;
    }
    
    /** Setting the Fields of JobInspectionTO */
    private void setJobInspectionTO(ArrayList data, int dataSize){
        for(int i=0;i<dataSize;i++){
            try{
                setTOobject(data,i);
            }catch(Exception e){
                parseException.logException(e,true);
            }
        }
    }
    
    /** This method sets data to the TO Object
     * @param data the data that has to be set to the TO Object
     * @param row the row of the data that has to be set to the TO Object
     */
    private void setTOobject(ArrayList data, int row)throws Exception{
        JobInspectionTO objJobInspectionTO;
        objJobInspectionTO = new JobInspectionTO();
        objJobInspectionTO.setCommand(getCommand());
        if(!objJobInspectionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objJobInspectionTO.setAuditId(getLblValueAuditId());
        }
        objJobInspectionTO.setJobCategory(CommonUtil.convertObjToStr(((ArrayList)data.get(row)).get(0)));
        objJobInspectionTO.setActualPosition(CommonUtil.convertObjToDouble(((ArrayList)data.get(row)).get(1)));
        tblAuditJobMasterRow.add(objJobInspectionTO);
    }
    
    /** This method gets the data that has to be updated into the database
     *  rowData the data that is already existing
     *  existingData the data that is newly added
     *  updateData the data that is has to be updated
     */
    public void updateData(){
        int row = rowData.size();
        int rowExistingData = existingData.size();
        for(int i=0;i<row;i++){
            for(int j=0;j<rowExistingData;j++){
                if(((ArrayList)existingData.get(j)).get(0).equals(((ArrayList)rowData.get(i)).get(0))){
                    updateData.add(existingData.get(j));
                }
            }
        }
        existingData = null;
        if(updateData != null && updateData.size()>0){
            for(int i=0; i<updateData.size();i++){
                JobInspectionTO objJobInspectionTO;
                objJobInspectionTO = new JobInspectionTO();
                objJobInspectionTO.setAuditId(getLblValueAuditId());
                objJobInspectionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objJobInspectionTO.setJobCategory(CommonUtil.convertObjToStr(((ArrayList)updateData.get(i)).get(0)));
                objJobInspectionTO.setActualPosition(CommonUtil.convertObjToDouble(((ArrayList)updateData.get(i)).get(1)));
                jobInspectionData.add(objJobInspectionTO);
            }
        }
    }
    
    /** This method gets the data that has to be deleted from the database
     *  rowData the data that is already existing
     *  deleteRow the data that is newly added
     *  deleteData the data that is has to be deleted
     */
    public void deleteData(){
        int row = rowData.size();
        int rowDelete = deleteRow.size();
        for(int i=0;i<row;i++){
            ArrayList rowArray = (ArrayList) rowData.get(i);
            for(int j=0;j<rowDelete;j++){
                if(((ArrayList)deleteRow.get(j)).get(0).equals(rowArray.get(0))){
                    deleteData.add(deleteRow.get(j));
                }
            }
        }
        deleteRow = null;
        if(deleteData != null && deleteData.size()>0){
            for(int i=0; i<deleteData.size();i++){
                JobInspectionTO objJobInspectionTO;
                objJobInspectionTO = new JobInspectionTO();
                objJobInspectionTO.setAuditId(getLblValueAuditId());
                objJobInspectionTO.setStatus(CommonConstants.STATUS_DELETED);
                objJobInspectionTO.setJobCategory(CommonUtil.convertObjToStr(((ArrayList)deleteData.get(i)).get(0)));
                objJobInspectionTO.setActualPosition(CommonUtil.convertObjToDouble(((ArrayList)deleteData.get(i)).get(1)));
                jobInspectionData.add(objJobInspectionTO);
            }
        }
    }
    
    /* This method is used to make all the arraylist used for insertion,deletion,updation null */
    private void deinitialise(){
        insertData = null;
        updateData =  null;
        deleteData =  null;
    }
    
    /** Sets the Fields of AuditJobMasterTO */
    private void setAuditJobMasterTO(ArrayList arrayAuditJobMaster) {
        int toSize=arrayAuditJobMaster.size();
        for (int i=0;i<toSize;i++){
            tblAuditJobMasterRow = new ArrayList();
            JobInspectionTO objJobInspectionTO;
            objJobInspectionTO =(JobInspectionTO) arrayAuditJobMaster.get(i);
            objJobInspectionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            tblAuditJobMasterRow.add(objJobInspectionTO.getJobCategory());
            tblAuditJobMasterRow.add(objJobInspectionTO.getActualPosition());
            tblAuditJobMasterModel.insertRow(0,tblAuditJobMasterRow);
        }
    }
    
    /** This method sets the model for the tblAppraisalInfo table in the UI */
    public  void setAppraisalInfoList(ArrayList arrayAppraisalInfo) {
        int toSize=arrayAppraisalInfo.size();
        for (int i=0;i<toSize;i++){
            tblAppraisalInfoRow = new ArrayList();
            HashMap columnData =(HashMap) arrayAppraisalInfo.get(i);
            tblAppraisalInfoRow.add(columnData.get("No. Of ManDays"));
            tblAppraisalInfoRow.add(columnData.get("Branch Rating"));
            tblAppraisalInfoRow.add(columnData.get("Inspecting Officials"));
            tblAppraisalInfoModel.insertRow(0,tblAppraisalInfoRow);
        }
    }
    
    
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            PerformanceInspectionTO objPerformanceInspectionTO =
            (PerformanceInspectionTO) ((List) mapData.get("PerformanceInspectionTO")).get(0);
            setPerformanceInspectionTO(objPerformanceInspectionTO);
            BranchInspectionTO objBranchInspectionTO =
            (BranchInspectionTO) ((List) mapData.get("BranchInspectionTO")).get(0);
            setBranchInspectionTO(objBranchInspectionTO);
            tblAuditJobMasterRow = (ArrayList) mapData.get("JobInspectionTO");
            if(tblAuditJobMasterRow!=null){
                for(int i=0;i<tblAuditJobMasterRow.size();i++){
                    JobInspectionTO objJobInspectionTO;
                    objJobInspectionTO = (JobInspectionTO)tblAuditJobMasterRow.get(i);
                    objJobInspectionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }
            
            setAuditJobMasterTO(tblAuditJobMasterRow);
            ttNotifyObservers();
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /** This method deletes the Selected Row in the Terminal_Master tabel in the UI **/
    public void deleteAuditJobMaster(){
        try{
            final ArrayList data = tblAuditJobMasterModel.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ( (((ArrayList)data.get(i)).get(0)).equals((String)cboJobCategory)){
                    if(deleteRow!=null){
                        deleteRow.add(data.get(i));
                    }
                    tblAuditJobMasterModel.removeRow(i);
                    break;
                }
            }
            setChanged();
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public String fillTxtStaffPosition(){
        int rowCount = tblAuditJobMasterModel.getRowCount();
        double posCount = 0;
        double position = 0;
        if(rowCount != 0){
            for(int i=0; i<rowCount; i++){
                position = CommonUtil.convertObjToInt(tblAuditJobMasterModel.getValueAt(i,1));
                posCount+= position;
            }
        }
        String staffPostion = CommonUtil.convertObjToStr(new Double(posCount));
        return staffPostion;
    }
    
    
}