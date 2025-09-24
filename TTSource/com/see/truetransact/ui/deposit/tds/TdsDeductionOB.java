/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsDeductionOB.java
 *
 * Created on March 22, 2004, 2:07 PM
 */

package com.see.truetransact.ui.deposit.tds;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.deposit.tds.TdsDeductionRB;
import com.see.truetransact.transferobject.deposit.tds.TdsDeductionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 *
 * @author  Lohith R.
 */
public class TdsDeductionOB extends CObservable{
    Date curDate = null;
    private static TdsDeductionOB objTdsDeductionOB; // singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value,tblInterestDetColTitle;
    private EnhancedTableModel tblInterestDet;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmDepositSubNo;
    private ComboBoxModel cbmCollectionType;
    private ComboBoxModel cbmIntPayFreq;
    private String cboProductID = "";
    private String cboDepositSubNo = "";
    private String txtDebitAccNum = "";
    private String txtDepositNo = "";
    private String txtRemarks = "";
    private String txtTdsAmount = "";
    private String txtDepositAmount = "";
    private String datelDepositDate = "";
    private String dateTDSStartDate = "";
    private String dateMaturityDate = "";
    private String dateTDSEndDate = "";
    private String txtDebitAccHead = "";
    private String cboCollectionType = "";
    private String cboIntPayFreq = "";
    
    private String txtInterestPayable = "";
    private String txtInterestAccrued = "";
    private String txtInterestPaid = "";
    private String tdsID = "";
    private String cboDpBehiveLike = "";
    private ComboBoxModel cbmDpBehiveLike;
    private String totIntAmt="";
    private String finIntAmt="";
    private String cutAmt ="";
    private String tdsRate="";
    private String tdaId="";
    private String modified="";
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private Date authorizeDt= null;
    private HashMap rejMap=null;
    java.util.ResourceBundle objTdsDeductionRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.tds.TdsDeductionRB", ProxyParameters.LANGUAGE);
    
    static {
        try {
            objTdsDeductionOB = new TdsDeductionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    /** Creates a new instance of TdsDeductionOB */
    public TdsDeductionOB()throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        settblInterestDetColTitleCol();
        tblInterestDet = new EnhancedTableModel(null, tblInterestDetColTitle);
        cbmDepositSubNo = new ComboBoxModel();
    }
    private void   settblInterestDetColTitleCol(){
        tblInterestDetColTitle = new ArrayList();
        tblInterestDetColTitle.add("Deposit No");
        tblInterestDetColTitle.add("Deposit Dt");
        tblInterestDetColTitle.add("Maturity Dt");
        tblInterestDetColTitle.add("Deposit Amt");
        tblInterestDetColTitle.add("ROI");
        tblInterestDetColTitle.add("Financial Int");
        tblInterestDetColTitle.add("ClosedROI");
        tblInterestDetColTitle.add("Closed Dt");
        
        tblInterestDetColTitle.add("IntPayFreq");
        //        tblnvestmentTransDetColTitle.add("AmortizationAmount");
    }
    
    private void setFinancialTable(ArrayList transList){
        tblInterestDet=new EnhancedTableModel();
        ArrayList dataList = new ArrayList();
        for(int i=0 ;i<transList.size();i++){
            ArrayList invAmrDetRow=new ArrayList();
            HashMap intMpa=(HashMap)transList.get(i);
            invAmrDetRow.add(0, CommonUtil.convertObjToStr(intMpa.get("DEPOSIT_NO")));
            invAmrDetRow.add(1, CommonUtil.convertObjToStr(intMpa.get("DEPOSIT_DT")));
            invAmrDetRow.add(2, CommonUtil.convertObjToStr(intMpa.get("MATURITY_DT")));
            invAmrDetRow.add(3, CommonUtil.convertObjToStr(intMpa.get("DEPOSIT_AMT")));
            invAmrDetRow.add(4, CommonUtil.convertObjToStr(intMpa.get("RATE_OF_INT")));
            invAmrDetRow.add(5, CommonUtil.convertObjToStr(intMpa.get("INTER")));
            invAmrDetRow.add(6, CommonUtil.convertObjToStr(intMpa.get("CLOSE_RATE_OF_INT")));
            invAmrDetRow.add(7, CommonUtil.convertObjToStr(intMpa.get("CLOSE_DT")));
            invAmrDetRow.add(8, CommonUtil.convertObjToStr(intMpa.get("INTPAY_FREQ")));
            //            tblInterestDet.addRow(invAmrDetRow);
            dataList.add(invAmrDetRow);
            invAmrDetRow=null;
            
        }
        tblInterestDet.setDataArrayList(dataList, tblInterestDetColTitle);
        tblInterestDet.fireTableDataChanged();
        setChanged();
        notifyObservers();
    }
    
    public void resetTable(){
        try{
            ArrayList data = tblInterestDet.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblInterestDet.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //            log.info("Error in resetTable():");
        }
    }
    /** Sets the HashMap to required JNDI, Home and Remote */
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TdsDeductionJNDI");
        operationMap.put(CommonConstants.HOME, "deposit.tds.TdsDeductionHome");
        operationMap.put(CommonConstants.REMOTE, "deposit.tds.TdsDeduction");
    }
    
    /** Creates a new instance of TdsDeductionOB */
    public static TdsDeductionOB getInstance() {
        return objTdsDeductionOB;
    }
    
    /** Method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        /** The data to be show in Combo Box from LOOKUP_MASTER table */
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("DEPOSITSPRODUCT.OPERATESLIKE");
        lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
        
        //        lookup_keys.add("");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmCollectionType = new ComboBoxModel(key,value);
        
        cbmCollectionType.removeKeyAndElement("TD");
        cbmCollectionType.removeKeyAndElement("TL");
        cbmCollectionType.removeKeyAndElement("GL");
        if(cbmCollectionType.containsElement("ATL")){
        cbmCollectionType.removeKeyAndElement("ATL");
        }
        if(cbmCollectionType.containsElement("AAD")){
        cbmCollectionType.removeKeyAndElement("AAD");
        }
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.OPERATESLIKE"));
        
        cbmDpBehiveLike = new ComboBoxModel(key,value);
        cbmDpBehiveLike.removeKeyAndElement("DAILY");
        cbmDpBehiveLike.removeKeyAndElement("RECURRING");
        cbmDpBehiveLike.removeKeyAndElement("UNITS");
        cbmDpBehiveLike.removeKeyAndElement("FCNR");
        cbmDpBehiveLike.removeKeyAndElement("NO_INT_DEPT");
        cbmDpBehiveLike.removeKeyAndElement("FLOATING_RATE");
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
        cbmIntPayFreq=new ComboBoxModel(key,value);
        
        /** The data to be show in Combo Box other than LOOKUP_MASTER table  */
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"Deposit_TDS.getProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductID = new ComboBoxModel(key,value);
        
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    /** This method get deposit number as parameter and accordingly set combo model of Deposit
     * Sum Number.
     * @param depositNumber text from deposit number is passed as an argument.
     */
    public void populateComboDepositSubNumber(String depositNumber){
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"Deposit_TDS.getProdSubNo");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, depositNumber);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmDepositSubNo = new ComboBoxModel(key,value);
            setCbmDepositSubNo(cbmDepositSubNo);
            makeNull();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    void setTdsId(String tdsID){
        this.tdsID = tdsID;
        setChanged();
    }
    String getTdsId(){
        return this.tdsID;
    }
    
    void setCboProductID(String cboProductID){
        this.cboProductID = cboProductID;
        setChanged();
    }
    String getCboProductID(){
        return this.cboProductID;
    }
    
    void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    ComboBoxModel getCbmProductID(){
        return cbmProductID;
    }
    
    void setCboDepositSubNo(String cboDepositSubNo){
        this.cboDepositSubNo = cboDepositSubNo;
        setChanged();
    }
    String getCboDepositSubNo(){
        return this.cboDepositSubNo;
    }
    void setCbmDepositSubNo(ComboBoxModel cbmDepositSubNo){
        this.cbmDepositSubNo = cbmDepositSubNo;
        setChanged();
    }
    ComboBoxModel getCbmDepositSubNo(){
        return cbmDepositSubNo;
    }
    
    void setTxtDebitAccNum(String txtDebitAccNum){
        this.txtDebitAccNum = txtDebitAccNum;
        setChanged();
    }
    String getTxtDebitAccNum(){
        return this.txtDebitAccNum;
    }
    
    void setTxtDepositNo(String txtDepositNo){
        this.txtDepositNo = txtDepositNo;
        setChanged();
    }
    String getTxtDepositNo(){
        return this.txtDepositNo;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    void setTxtTdsAmount(String txtTdsAmount){
        this.txtTdsAmount = txtTdsAmount;
        setChanged();
    }
    String getTxtTdsAmount(){
        return this.txtTdsAmount;
    }
    
    void setTxtDepositAmount(String txtDepositAmount){
        this.txtDepositAmount = txtDepositAmount;
        setChanged();
    }
    String getTxtDepositAmount(){
        return this.txtDepositAmount;
    }
    
    void setDateDepositDate(String datelDepositDate){
        this.datelDepositDate = datelDepositDate;
        setChanged();
    }
    String getDateDepositDate(){
        return this.datelDepositDate;
    }
    
    void setDateTDSStartDate(String dateTDSStartDate){
        this.dateTDSStartDate = dateTDSStartDate;
        setChanged();
    }
    String getDateTDSStartDate(){
        return this.dateTDSStartDate;
    }
    
    void setDateMaturityDate(String dateMaturityDate){
        this.dateMaturityDate = dateMaturityDate;
        setChanged();
    }
    String getDateMaturityDate(){
        return this.dateMaturityDate;
    }
    
    void setDateTDSEndDate(String dateTDSEndDate){
        this.dateTDSEndDate = dateTDSEndDate;
        setChanged();
    }
    String getDateTDSEndDate(){
        return this.dateTDSEndDate;
    }
    
    void setTxtDebitAccHead(String txtDebitAccHead){
        this.txtDebitAccHead = txtDebitAccHead;
        setChanged();
    }
    String getTxtDebitAccHead(){
        return this.txtDebitAccHead;
    }
    
    void setCboCollectionType(String cboCollectionType){
        this.cboCollectionType = cboCollectionType;
        setChanged();
    }
    String getCboCollectionType(){
        return this.cboCollectionType;
    }
    void setCbmCollectionType(ComboBoxModel cbmCollectionType){
        this.cbmCollectionType = cbmCollectionType;
        setChanged();
    }
    ComboBoxModel getCbmCollectionType(){
        return cbmCollectionType;
    }
    
    void setTxtInterestPayable(String txtInterestPayable){
        this.txtInterestPayable = txtInterestPayable;
        setChanged();
    }
    String getTxtInterestPayable(){
        return this.txtInterestPayable;
    }
    
    void setTxtInterestAccrued(String txtInterestAccrued){
        this.txtInterestAccrued = txtInterestAccrued;
        setChanged();
    }
    String getTxtInterestAccrued(){
        return this.txtInterestAccrued;
    }
    
    void setTxtInterestPaid(String txtInterestPaid){
        this.txtInterestPaid = txtInterestPaid;
        setChanged();
    }
    String getTxtInterestPaid(){
        return this.txtInterestPaid;
    }
    
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public int getResult(){
        return result;
    }
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return actionType;
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
                    //                    final TdsDeductionRB objTdsDeductionRB = new TdsDeductionRB();
                    throw new TTException(objTdsDeductionRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
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
                case ClientConstants.ACTIONTYPE_REJECT:
                command ="REJECT";
                break;
            default:
        }
        return command;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        
        
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        if(actionType!=ClientConstants.ACTIONTYPE_REJECT){
        final TdsDeductionTO objTdsDeductionTO = setTdsDeductionData();
        objTdsDeductionTO.setCommand(getCommand());
        data.put("TdsDeductionTO",objTdsDeductionTO);
        }else{
            data.put("REJECTMAP",getRejMap());
        }
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        if(proxyResultMap !=null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            ClientUtil.showMessageWindow("TDS Deduction No." +CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_NEW;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /* To set common data in the Transfer Object*/
    public TdsDeductionTO setTdsDeductionData() {
        final TdsDeductionTO objTdsDeductionTO = new TdsDeductionTO();
        try{
            if (actionType== ClientConstants.ACTIONTYPE_NEW)
                objTdsDeductionTO.setStatus(CommonConstants.STATUS_CREATED);
            else if (actionType== ClientConstants.ACTIONTYPE_EDIT)
                objTdsDeductionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            else if (actionType== ClientConstants.ACTIONTYPE_DELETE)
                objTdsDeductionTO.setStatus(CommonConstants.STATUS_DELETED);
            objTdsDeductionTO.setTdsId(CommonUtil.convertObjToStr(getTdsId()));
            
            
            objTdsDeductionTO.setProdId(CommonUtil.convertObjToStr(getCboProductID()));
            objTdsDeductionTO.setDepositNo(CommonUtil.convertObjToStr(getTxtDepositNo()));
            Date DepDt = DateUtil.getDateMMDDYYYY(datelDepositDate);
            if(DepDt != null){
                Date depDate = (Date)curDate.clone();
                depDate.setDate(DepDt.getDate());
                depDate.setMonth(DepDt.getMonth());
                depDate.setYear(DepDt.getYear());
                objTdsDeductionTO.setDepositDt(DateUtil.getDateMMDDYYYY(datelDepositDate));
                objTdsDeductionTO.setDepositDt(depDate);
            }else{
                objTdsDeductionTO.setDepositDt(DateUtil.getDateMMDDYYYY(datelDepositDate));
            }
            
            objTdsDeductionTO.setDepositAmt(CommonUtil.convertObjToDouble(txtDepositAmount));
            objTdsDeductionTO.setRemarks(txtRemarks);
            Date TdsStDt = DateUtil.getDateMMDDYYYY(dateMaturityDate);
            if(TdsStDt != null){
                Date tdsStDate = (Date)curDate.clone();
                tdsStDate.setDate(TdsStDt.getDate());
                tdsStDate.setMonth(TdsStDt.getMonth());
                tdsStDate.setYear(TdsStDt.getYear());
                objTdsDeductionTO.setMaturityDt(tdsStDate);
            }else{
                objTdsDeductionTO.setMaturityDt(DateUtil.getDateMMDDYYYY(dateTDSStartDate));
            }
            objTdsDeductionTO.setDebitAcctNo(txtDebitAccNum);
            objTdsDeductionTO.setDebitProdType(CommonUtil.convertObjToStr(getCboCollectionType()));
            objTdsDeductionTO.setDebitProdId(CommonUtil.convertObjToStr(getCboDepositSubNo()));
            
            
            
            objTdsDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
            objTdsDeductionTO.setModified(getModified());
            objTdsDeductionTO.setAuthorizeBy(getAuthorizeBy());
            objTdsDeductionTO.setAuthorizeStatus(getAuthorizeStatus());
            objTdsDeductionTO.setAuthorizeDt(getAuthorizeDt());
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objTdsDeductionTO;
    }
    
    /** This method helps in getting CommonConstants.MAP_WHERE codition and executing the query */
    public void populateData(HashMap whereMap) {
        try {
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            
            //            populateOB(mapData);
            if (whereMap.containsKey("ALLTDSACCOUNT") && whereMap.get("ALLTDSACCOUNT").equals("ALLTDSACCOUNT")){
                ArrayList intList= (ArrayList)mapData.get("INTERESTALL");
                if(intList!=null && intList.size()>0){
                    setFinancialTable(intList);
                }
                
                ArrayList tdsParameter= (ArrayList)mapData.get("TDSPARAMETER");
                if(tdsParameter!=null && tdsParameter.size()>0){
                    HashMap tdsParameterMap= new HashMap();
                    tdsParameterMap=(HashMap)tdsParameter.get(0);
                    setCutAmt(CommonUtil.convertObjToStr(tdsParameterMap.get("CUT_OF_AMT")));
                    setTdsRate(CommonUtil.convertObjToStr(tdsParameterMap.get("TDS_PERCENTAGE")));
                }
                
                setTotIntAmt(CommonUtil.convertObjToStr(mapData.get("TOTALINTAMT")));
                setFinIntAmt(CommonUtil.convertObjToStr(mapData.get("FININTAMT")));
                setChanged();
                notifyObservers();
                
            }else{
                populateOB(mapData);
            }
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    
    /** This method helps in populating the data from the View All table to the respective Fields */
    private void populateOB(HashMap mapData) throws Exception{
        TdsDeductionTO objTdsDeductionTO;
        objTdsDeductionTO = (TdsDeductionTO) ((List) mapData.get("TdsDeductionTO")).get(0);
        setTxtDepositNo(objTdsDeductionTO.getDepositNo());
        setCboProductID(CommonUtil.convertObjToStr(getCbmProductID().getDataForKey(objTdsDeductionTO.getProdId())));
        setCboCollectionType(CommonUtil.convertObjToStr(getCbmCollectionType().getDataForKey(objTdsDeductionTO.getDebitProdType())));
        setDateDepositDate(DateUtil.getStringDate(objTdsDeductionTO.getDepositDt()));
        setTdsId(CommonUtil.convertObjToStr(objTdsDeductionTO.getTdsId()));
        //        setDateTDSStartDate(DateUtil.getStringDate(objTdsDeductionTO.getTdsStartDt()));
        //        setDateTDSEndDate(DateUtil.getStringDate(objTdsDeductionTO.getTdsEndDt()));
        setDateMaturityDate(DateUtil.getStringDate(objTdsDeductionTO.getMaturityDt()));
        setTxtDepositAmount(CommonUtil.convertObjToStr(objTdsDeductionTO.getDepositAmt()));
        //        setTxtInterestPayable(CommonUtil.convertObjToStr(objTdsDeductionTO.getInterestPayable()));
        //        setTxtDebitAccHead(CommonUtil.convertObjToStr(objTdsDeductionTO.getDebitAcHd()));
        //        setTxtRemarks(CommonUtil.convertObjToStr(objTdsDeductionTO.getRemarks()));
        //        setTxtInterestAccrued(CommonUtil.convertObjToStr(objTdsDeductionTO.getInterestAccrued()));
        //        setTxtInterestPaid(CommonUtil.convertObjToStr(objTdsDeductionTO.getInterestPaid()));
        //        setTxtTdsAmount(CommonUtil.convertObjToStr(objTdsDeductionTO.getTdsAmt()));
        setTxtDebitAccNum(CommonUtil.convertObjToStr(objTdsDeductionTO.getDebitAcctNo()));
        //        setStatusBy(objTdsDeductionTO.getStatusBy());
        //        setAuthorizeStatus(objTdsDeductionTO.getAuthorizeStatus());
        //        setCboDpBehiveLike(CommonUtil.convertObjToStr(getCbmDpBehiveLike().getDataForKey(objTdsDeductionTO.getDpBehiveLike())));
        //        notifyObservers();
        setModified(CommonUtil.convertObjToStr(objTdsDeductionTO.getModified()));
        callProdIdsForProductType(CommonUtil.convertObjToStr(objTdsDeductionTO.getDebitProdType()));
        setCboDepositSubNo(CommonUtil.convertObjToStr(getCbmDepositSubNo().getDataForKey(objTdsDeductionTO.getDebitProdId())));
        setAuthorizeBy(CommonUtil.convertObjToStr(objTdsDeductionTO.getAuthorizeBy()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objTdsDeductionTO.getAuthorizeStatus()));
        setAuthorizeDt(objTdsDeductionTO.getAuthorizeDt());
    }
    
    /** To reset the txt fileds to null */
    public void resetForm(){
        setCboProductID("");
        setCboCollectionType("");
        setCboDepositSubNo("");
        setDateDepositDate("");
        setDateTDSStartDate("");
        setDateTDSEndDate("");
        setDateMaturityDate("");
        setTxtDepositNo("");
        setTxtDepositAmount("");
        setTxtInterestPayable("");
        setTxtDebitAccHead("");
        setTxtRemarks("");
        setTxtInterestAccrued("");
        setTxtInterestPaid("");
        setTxtTdsAmount("");
        setTxtDebitAccNum("");
        setCboDpBehiveLike("");
        setTotIntAmt("");
        setFinIntAmt("");
        setTdsId("");
        
        
        notifyObservers();
    }
    
    /**
     * Getter for property cbmDpBehiveLike.
     * @return Value of property cbmDpBehiveLike.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDpBehiveLike() {
        return cbmDpBehiveLike;
    }
    
    /**
     * Setter for property cbmDpBehiveLike.
     * @param cbmDpBehiveLike New value of property cbmDpBehiveLike.
     */
    public void setCbmDpBehiveLike(com.see.truetransact.clientutil.ComboBoxModel cbmDpBehiveLike) {
        this.cbmDpBehiveLike = cbmDpBehiveLike;
    }
    
    /**
     * Getter for property cboDpBehiveLike.
     * @return Value of property cboDpBehiveLike.
     */
    public java.lang.String getCboDpBehiveLike() {
        return cboDpBehiveLike;
    }
    
    /**
     * Setter for property cboDpBehiveLike.
     * @param cboDpBehiveLike New value of property cboDpBehiveLike.
     */
    public void setCboDpBehiveLike(java.lang.String cboDpBehiveLike) {
        this.cboDpBehiveLike = cboDpBehiveLike;
    }
    public void callProdIdsForProductType(String prodType){
        try {
            if(!prodType.equals("")){
                
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmDepositSubNo= new ComboBoxModel(key,value);
            }else{
                key.add("");
                value.add("");
                cbmDepositSubNo= new ComboBoxModel(key,value);
            }
            
        } catch(Exception e){
            System.out.println("Error in cboProductIdActionPerformed()");
        }
        
    }
    
    /**
     * Getter for property cbmIntPayFreq.
     * @return Value of property cbmIntPayFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntPayFreq() {
        return cbmIntPayFreq;
    }
    
    /**
     * Setter for property cbmIntPayFreq.
     * @param cbmIntPayFreq New value of property cbmIntPayFreq.
     */
    public void setCbmIntPayFreq(com.see.truetransact.clientutil.ComboBoxModel cbmIntPayFreq) {
        this.cbmIntPayFreq = cbmIntPayFreq;
    }
    
    /**
     * Getter for property cboIntPayFreq.
     * @return Value of property cboIntPayFreq.
     */
    public java.lang.String getCboIntPayFreq() {
        return cboIntPayFreq;
    }
    
    /**
     * Setter for property cboIntPayFreq.
     * @param cboIntPayFreq New value of property cboIntPayFreq.
     */
    public void setCboIntPayFreq(java.lang.String cboIntPayFreq) {
        this.cboIntPayFreq = cboIntPayFreq;
    }
    
    /**
     * Getter for property tblInterestDet.
     * @return Value of property tblInterestDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInterestDet() {
        return tblInterestDet;
    }
    
    /**
     * Setter for property tblInterestDet.
     * @param tblInterestDet New value of property tblInterestDet.
     */
    public void setTblInterestDet(com.see.truetransact.clientutil.EnhancedTableModel tblInterestDet) {
        this.tblInterestDet = tblInterestDet;
    }
    
    /**
     * Getter for property totIntAmt.
     * @return Value of property totIntAmt.
     */
    public java.lang.String getTotIntAmt() {
        return totIntAmt;
    }
    
    /**
     * Setter for property totIntAmt.
     * @param totIntAmt New value of property totIntAmt.
     */
    public void setTotIntAmt(java.lang.String totIntAmt) {
        this.totIntAmt = totIntAmt;
    }
    
    /**
     * Getter for property finIntAmt.
     * @return Value of property finIntAmt.
     */
    public java.lang.String getFinIntAmt() {
        return finIntAmt;
    }
    
    /**
     * Setter for property finIntAmt.
     * @param finIntAmt New value of property finIntAmt.
     */
    public void setFinIntAmt(java.lang.String finIntAmt) {
        this.finIntAmt = finIntAmt;
    }
    
    /**
     * Getter for property cutAmt.
     * @return Value of property cutAmt.
     */
    public java.lang.String getCutAmt() {
        return cutAmt;
    }
    
    /**
     * Setter for property cutAmt.
     * @param cutAmt New value of property cutAmt.
     */
    public void setCutAmt(java.lang.String cutAmt) {
        this.cutAmt = cutAmt;
    }
    
    /**
     * Getter for property tdsRate.
     * @return Value of property tdsRate.
     */
    public java.lang.String getTdsRate() {
        return tdsRate;
    }
    
    /**
     * Setter for property tdsRate.
     * @param tdsRate New value of property tdsRate.
     */
    public void setTdsRate(java.lang.String tdsRate) {
        this.tdsRate = tdsRate;
    }
    
    /**
     * Getter for property tdaId.
     * @return Value of property tdaId.
     */
    public java.lang.String getTdaId() {
        return tdaId;
    }
    
    /**
     * Setter for property tdaId.
     * @param tdaId New value of property tdaId.
     */
    public void setTdaId(java.lang.String tdaId) {
        this.tdaId = tdaId;
    }
    
    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public java.lang.String getModified() {
        return modified;
    }
    
    /**
     * Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(java.lang.String modified) {
        this.modified = modified;
    }
    
    /**
     * Getter for property authorizeStatus.
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }
    
    /**
     * Setter for property authorizeStatus.
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
    /**
     * Getter for property authorizeBy.
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }
    
    /**
     * Setter for property authorizeBy.
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }
    
    /**
     * Getter for property authorizeDt.
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }
    
    /**
     * Setter for property authorizeDt.
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }
    
    /**
     * Getter for property rejMap.
     * @return Value of property rejMap.
     */
    public java.util.HashMap getRejMap() {
        return rejMap;
    }
    
    /**
     * Setter for property rejMap.
     * @param rejMap New value of property rejMap.
     */
    public void setRejMap(java.util.HashMap rejMap) {
        this.rejMap = rejMap;
    }
    
}