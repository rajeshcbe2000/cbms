/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSChangeofMemberOB.java
 *
 * Created on Sun May 29 12:10:33 IST 2011
 */

package com.see.truetransact.ui.gdsapplication.gdschangeofmember;

import com.see.truetransact.ui.mdsapplication.mdschangeofmember.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.gdsapplication.gdschangeofmember.GDSChangeofMemberTO;
import com.see.truetransact.transferobject.mdsapplication.mdschangeofmember.MDSChangeofMemberTO;



/**
 *
 * @author
 */

public class GDSChangeofMemberOB extends CObservable{
    
    
    private String rdoMunnal="";
    private String txtInstallmentNo = "";
    private String txtRemarks = "";
    private String txtTotalAmount = "";
    private String txtChitNo = "";
    private String tdtEffetiveDt = "";
    private String txtNewMemberName = "";
    private String txtOldMemberName = "";
    private String txtSchemeName = "";
    private String txtNewMemberNo = "";
    private String txtExistingNo = "";
    private String txtDivisionNo = "";
    private String txtSubNo = "";
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(GDSChangeofMemberOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private GDSChangeofMemberTO objChangeofMemberTO;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private String commonScheme = "";
    private int schemeCount = 0;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public GDSChangeofMemberOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GDSChangeofMemberJNDI");
            map.put(CommonConstants.HOME, "GDSChangeofMemberHome");
            map.put(CommonConstants.REMOTE, "GDSChangeofMember");
//            fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("DEVISION_NO");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("DEVISION_NO"));
//        cbmDivisionNo = new ComboBoxModel(key,value);
    }
    
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
    
    
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("ChangeOfMember",setChangeOfMemberData());
            data.put("GDS_NO",getTxtExistingNo());
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    
    
    /** To populate data into the screen */
    public GDSChangeofMemberTO setChangeOfMemberData() {
        
        final GDSChangeofMemberTO objChangeofMemberTO = new GDSChangeofMemberTO();
        try{
            objChangeofMemberTO.setOldMemberNo(getTxtExistingNo());
            objChangeofMemberTO.setNewMemberNo(getTxtNewMemberNo());
            objChangeofMemberTO.setNewMemberName(getTxtNewMemberName());
            objChangeofMemberTO.setOldMemberName(getTxtOldMemberName());
            objChangeofMemberTO.setSchemeName(getTxtSchemeName());
            objChangeofMemberTO.setDivisionNo(CommonUtil.convertObjToInt(getTxtDivisionNo()));
            objChangeofMemberTO.setSubNo(CommonUtil.convertObjToInt(getTxtSubNo()));
            objChangeofMemberTO.setChitNo(getTxtChitNo());
            objChangeofMemberTO.setInstallmentNo(CommonUtil.convertObjToInt(getTxtInstallmentNo()));
            objChangeofMemberTO.setTotalAmount(CommonUtil.convertObjToDouble(getTxtTotalAmount()));
            objChangeofMemberTO.setChangeEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtEffetiveDt()));
            objChangeofMemberTO.setRemarks(getTxtRemarks());
            objChangeofMemberTO.setNewMemberMunnal(getRdoMunnal());
            objChangeofMemberTO.setStatus(getAction());
            objChangeofMemberTO.setStatusBy(TrueTransactMain.USER_ID);
            objChangeofMemberTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objChangeofMemberTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objChangeofMemberTO.setGdsNo(getTxtExistingNo());
        }catch(Exception e){
            log.info("Error In setMDSChangeofMemberTOData()");
            e.printStackTrace();
        }
        return objChangeofMemberTO;
    }
    
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objChangeofMemberTO = (GDSChangeofMemberTO) ((List) data.get("ChangeOfMemberTO")).get(0);
            populateFixedAssetData(objChangeofMemberTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateFixedAssetData(GDSChangeofMemberTO objChangeofMemberTO) throws Exception{
        this.setTxtExistingNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getGdsNo()));
        this.setTxtNewMemberNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getNewMemberNo()));
        this.setTxtNewMemberName(CommonUtil.convertObjToStr(objChangeofMemberTO.getNewMemberName()));
        this.setTxtOldMemberName(CommonUtil.convertObjToStr(objChangeofMemberTO.getOldMemberName()));
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objChangeofMemberTO.getSchemeName()));
        this.setTxtDivisionNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getDivisionNo()));
        this.setTxtSubNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getSubNo()));
        this.setTxtChitNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getChitNo()));
        this.setTxtInstallmentNo(CommonUtil.convertObjToStr(objChangeofMemberTO.getInstallmentNo()));
        this.setTxtTotalAmount(CommonUtil.convertObjToStr(objChangeofMemberTO.getTotalAmount()));
        this.setTdtEffetiveDt(CommonUtil.convertObjToStr(objChangeofMemberTO.getChangeEffectiveDate()));
        this.setTxtRemarks(CommonUtil.convertObjToStr(objChangeofMemberTO.getRemarks()));
        this.setRdoMunnal(CommonUtil.convertObjToStr(objChangeofMemberTO.getNewMemberMunnal()));
        setChanged();
        notifyObservers();
    }
    
    public void setReceiptDetails(HashMap map){
        HashMap chittalMap = new HashMap();
        chittalMap.put("GDS_NO",getTxtChitNo());
        //chittalMap.put("SUB_NO",CommonUtil.convertObjToStr(getTxtSubNo()));
        chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo())); // Added by nithya on 16-01-2020 for KD-1279
        //List lst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
        List lst = ClientUtil.executeQuery("getGDSSelctApplnReceiptDetails", chittalMap);
        if(lst!=null && lst.size()>0){
            chittalMap = (HashMap)lst.get(0);
            setTxtDivisionNo(CommonUtil.convertObjToStr(chittalMap.get("DIVISION_NO")));
            setTxtOldMemberName(CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NAME")));    
            setTxtExistingNo(CommonUtil.convertObjToStr(chittalMap.get("CHITTAL_NO")));    
            setTxtChitNo(CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NO")));
//            setTxtDivisionNo(CommonUtil.convertObjToStr(chittalMap.get("DIVISION_NO")));
//            setTxtChitNo(CommonUtil.convertObjToStr(chittalMap.get("CHITTAL_NO")));    
//            setTxtExistingNo(CommonUtil.convertObjToStr(chittalMap.get("MEMBER_NAME")));    
            
            
//            setTdtChitStartDt(CommonUtil.convertObjToStr(chittalMap.get("CHIT_START_DT")));
//            setTxtNoOfInst(CommonUtil.convertObjToStr(chittalMap.get("NO_OF_INSTALLMENTS")));
//            setTxtCurrentInstNo(CommonUtil.convertObjToStr(chittalMap.get("")));
//            setTxtInstAmt(CommonUtil.convertObjToStr(chittalMap.get("INST_AMT")));
//            setTxtPendingInst(CommonUtil.convertObjToStr(chittalMap.get("")));
//            setTxtTotalInstAmt(CommonUtil.convertObjToStr(chittalMap.get("")));
//            HashMap prizedMap = new HashMap();
//            prizedMap.put("SCHEME_NAME",getTxtSchemeName());
//            prizedMap.put("DIVISION_NO",chittalMap.get("DIVISION_NO"));
//            lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
//            if(lst!=null && lst.size()>0){
//                prizedMap = (HashMap)lst.get(0);
//                setTxtBonusAmtAvail(CommonUtil.convertObjToStr(prizedMap.get("NEXT_BONUS_AMOUNT")));
//                setTdtChitEndDt(CommonUtil.convertObjToStr(prizedMap.get("NEXT_INSTALLMENT_DATE")));
//            }
//            prizedMap = new HashMap();
//            prizedMap.put("SCHEME_NAME",getTxtSchemeName());
//            prizedMap.put("DIVISION_NO",chittalMap.get("DIVISION_NO"));
//            prizedMap.put("CHITTAL_NO",getTxtChittalNo());
//            lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
//            if(lst!=null && lst.size()>0){
//                prizedMap = (HashMap)lst.get(0);
//                if(prizedMap.get("DRAW")!=null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")){
//                    setRdoPrizedMember_Yes(true);
//                }
//                if(prizedMap.get("AUCTION")!=null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")){
//                    setRdoPrizedMember_Yes(true);
//                }
//            }else{
//                setRdoPrizedMember_No(true);
//            }
////            if(chittalMap.get("AUCTION").equals("Y")){
////                setRdoPrizedMember_Yes(true);
////            }else{
////                setRdoPrizedMember_No(true);
////            }
//            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
//            int stYear = startDate.getYear()+1900;
//            int currYear = currDate.getYear()+1900;
//            int stMonth = startDate.getMonth();
//            int currMonth = currDate.getMonth();
//            int value = 0;
//            if(stYear == currYear){
//                value = currMonth - stMonth+1;
//            }else{
//                int year = currYear - stYear;
//                value = (year * 12) + currMonth - stMonth;
//            }
//            setTxtCurrentInstNo(String.valueOf(value));
//            int totInst = CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS"));
//            int pending = totInst - value;
//            setTxtPendingInst(String.valueOf(pending));
//            double instAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
//            setTxtTotalInstAmt(String.valueOf(instAmt * pending));
        }
    }
    
    // Setter method for txtInstallmentNo
    void setTxtInstallmentNo(String txtInstallmentNo){
        this.txtInstallmentNo = txtInstallmentNo;
        setChanged();
    }
    // Getter method for txtInstallmentNo
    String getTxtInstallmentNo(){
        return this.txtInstallmentNo;
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
    
    // Setter method for txtTotalAmount
    void setTxtTotalAmount(String txtTotalAmount){
        this.txtTotalAmount = txtTotalAmount;
        setChanged();
    }
    // Getter method for txtTotalAmount
    String getTxtTotalAmount(){
        return this.txtTotalAmount;
    }
    
    // Setter method for txtChitNo
    void setTxtChitNo(String txtChitNo){
        this.txtChitNo = txtChitNo;
        setChanged();
    }
    // Getter method for txtChitNo
    String getTxtChitNo(){
        return this.txtChitNo;
    }
    
    // Setter method for tdtEffetiveDt
    void setTdtEffetiveDt(String tdtEffetiveDt){
        this.tdtEffetiveDt = tdtEffetiveDt;
        setChanged();
    }
    // Getter method for tdtEffetiveDt
    String getTdtEffetiveDt(){
        return this.tdtEffetiveDt;
    }
    
    // Setter method for txtNewMemberName
    void setTxtNewMemberName(String txtNewMemberName){
        this.txtNewMemberName = txtNewMemberName;
        setChanged();
    }
    // Getter method for txtNewMemberName
    String getTxtNewMemberName(){
        return this.txtNewMemberName;
    }
    
    // Setter method for txtSchemeName
    void setTxtSchemeName(String txtSchemeName){
        this.txtSchemeName = txtSchemeName;
        setChanged();
    }
    // Getter method for txtSchemeName
    String getTxtSchemeName(){
        return this.txtSchemeName;
    }
    
    // Setter method for txtNewMemberNo
    void setTxtNewMemberNo(String txtNewMemberNo){
        this.txtNewMemberNo = txtNewMemberNo;
        setChanged();
    }
    // Getter method for txtNewMemberNo
    String getTxtNewMemberNo(){
        return this.txtNewMemberNo;
    }
    
    // Setter method for txtExistingNo
    void setTxtExistingNo(String txtExistingNo){
        this.txtExistingNo = txtExistingNo;
        setChanged();
    }
    // Getter method for txtExistingNo
    String getTxtExistingNo(){
        return this.txtExistingNo;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
       
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property rdoMunnal.
     * @return Value of property rdoMunnal.
     */
    public java.lang.String getRdoMunnal() {
        return rdoMunnal;
    }
    
    /**
     * Setter for property rdoMunnal.
     * @param rdoMunnal New value of property rdoMunnal.
     */
    public void setRdoMunnal(java.lang.String rdoMunnal) {
        this.rdoMunnal = rdoMunnal;
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
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property txtDivisionNo.
     * @return Value of property txtDivisionNo.
     */
    public java.lang.String getTxtDivisionNo() {
        return txtDivisionNo;
    }
    
    /**
     * Setter for property txtDivisionNo.
     * @param txtDivisionNo New value of property txtDivisionNo.
     */
    public void setTxtDivisionNo(java.lang.String txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
    }
    
    /**
     * Getter for property txtOldMemberName.
     * @return Value of property txtOldMemberName.
     */
    public java.lang.String getTxtOldMemberName() {
        return txtOldMemberName;
    }
    
    /**
     * Setter for property txtOldMemberName.
     * @param txtOldMemberName New value of property txtOldMemberName.
     */
    public void setTxtOldMemberName(java.lang.String txtOldMemberName) {
        this.txtOldMemberName = txtOldMemberName;
    }
    
    /**
     * Getter for property txtSubNo.
     * @return Value of property txtSubNo.
     */
    public java.lang.String getTxtSubNo() {
        return txtSubNo;
    }
    
    /**
     * Setter for property txtSubNo.
     * @param txtSubNo New value of property txtSubNo.
     */
    public void setTxtSubNo(java.lang.String txtSubNo) {
        this.txtSubNo = txtSubNo;
    }
    
    public String getCommonScheme() {
        return commonScheme;
    }

    public void setCommonScheme(String commonScheme) {
        this.commonScheme = commonScheme;
    }

    public int getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(int schemeCount) {
        this.schemeCount = schemeCount;
    }
    
}