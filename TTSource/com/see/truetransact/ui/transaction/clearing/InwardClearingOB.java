/*
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingOB.java
 *
 * Created on January 6, 2004, 1:39 PM
 */

package com.see.truetransact.ui.transaction.clearing;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.transferobject.transaction.clearing.InwardClearingTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.exceptionhashmap.transaction.TransactionRuleHashMap;
import  com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @author  rahul
 */
public class InwardClearingOB extends CObservable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    
    private HashMap _authorizeMap;
    
    private ProxyFactory proxy = null;
    
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmClearingType;
    private ComboBoxModel cbmInstrumentTypeID;
    private ComboBoxModel cbmCurrency;
    private ComboBoxModel cbmBankCode;
    private ComboBoxModel cbmBranchCode;
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmScheduleNo;
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final String CURRENCY = "INR";
    private String prodTypeTO = "";
    
    // String fields for the Lables in the UI...
    private String lblAccountHeadProdId;
    private String lblAccountHeadDesc;
    private String lblProdCurrency;
    private String lblAccountNumberName;
    private String lblClearingDate;
    
    
    private String lblTransactionId;
    private String lblTransactionDate;
    
    
    private String lblTotalInstrument;
    private String lblTotalAmount;
    private String lblBookedInstrument;
    private String lblBookedAmount;
    private String lblCountWarning;
    private String countWarning = "";
    private String authStatus = "";
    
    private final int EXCEPTION = 100;
    private final int BOUNCE = 200;
    private final int CANCEL = 300;
    private final int POST = 500;
    private int taskSelected = 0;
    private Object[] objReasons = null;
    private Object[] objBouncingReasons = null;
    boolean min_bal_check=true;
    
    //    final String BRANCHID = TrueTransactMain.BRANCH_ID;
    
    private final static Logger log = Logger.getLogger(InwardClearingUI.class);
    
    private double oldamount = 0.0;
    Date curDate = null;
    
    //    InwardClearingRB resourceBundle = new InwardClearingRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.clearing.InwardClearingRB", ProxyParameters.LANGUAGE);
    
    //    private static InwardClearingOB inwardClearingOB;
    //    static {
    //        try {
    //            log.info("In InwardClearingOB Declaration");
    //            inwardClearingOB = new InwardClearingOB();
    //        } catch(Exception e) {
    //            parseException.logException(e,true);
    //        }
    //    }
    //
    //    public static InwardClearingOB getInstance() {
    //        return inwardClearingOB;
    //    }
    
    /** Creates a new instance of InwardClearingOB */
    public InwardClearingOB(String selectedBranch) throws Exception {
        curDate = ClientUtil.getCurrentDate();
        setSelectedBranchID(selectedBranch);
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "InwardClearingJNDI");
        operationMap.put(CommonConstants.HOME, "transaction.clearing.InwardClearingHome");
        operationMap.put(CommonConstants.REMOTE, "transaction.clearing.InwardClearing");
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        /*lookup_keys.add("OP_AC_PRODUCT");*/
        //        lookup_keys.add("PROD_ID");
        lookup_keys.add("INSTRUMENTTYPE");
        //        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("INWARD_PROD_TYPE");
        lookup_keys.add("INWARD.BOUNCING_REASON");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        /*keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.BEHAVES"));
        cbmProdId = new ComboBoxModel(key,value);*/
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("INSTRUMENTTYPE"));
        /** To Remove the Fields like "Online Transfer", "Withdrawl Slip", "Voucher" from the
         * Selected Combobox Model...
         */
        int idx=key.indexOf("ONLINE_TRANSFER");
        key.remove(idx);
        value.remove(idx);
        
        idx=key.indexOf("WITHDRAW_SLIP");
        key.remove(idx);
        value.remove(idx);
        
        idx=key.indexOf("VOUCHER");
        key.remove(idx);
        value.remove(idx);
        
        key.add("ECS");
        value.add("ECS");
        cbmInstrumentTypeID= new ComboBoxModel(key,value);
        
        //        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        //        cbmCurrency= new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INWARD_PROD_TYPE"));
        //        //System.out.println("key :"+key+"VALUE"+value);
        //        key.clear();
        //        value.clear();
        //        key.add("");
        //        key.add("OA");
        //        value.add("");
        //        value.add("Operative Account");
        cbmProductType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INWARD.BOUNCING_REASON"));
        setBouncingReasons(value.toArray());
        
        // To obtain the Product id from the Table "OP_AC_PRODUCT"...
        // here "getAccProduct" is the mapped Statement name, defined in InwardClearingMap...
        //        lookUpHash.put(CommonConstants.MAP_NAME,"getAccProduct");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        //        cbmProdId = new ComboBoxModel(key,value);
        
        lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBank");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBankCode = new ComboBoxModel(key,value);
        
        
        System.out.println("@@@#### Branch: "+ getSelectedBranchID());
        
        //__ Data for the ClearingType Combo-Box...
        lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getInwardClearingType");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY, TrueTransactMain.BRANCH_ID);
        lookUpHash.put(CommonConstants.PARAMFORQUERY, getSelectedBranchID());
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmClearingType = new ComboBoxModel(key,value);
        key=new ArrayList();
        value=new ArrayList();
        cbmScheduleNo= new ComboBoxModel();

    }
    
    public void getProdIdData()throws Exception{
        if(getCboProductType().length()>0){
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getProductData" + getCboProductType());
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmProdId = new ComboBoxModel(key,value);
        }else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
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
    
    
    /** To retrive Schedule No based on Clearing Type*/
    public String getScheduleNoForProd() {
        String scheduleNumber = "";
        try {
            final HashMap schedNoMap = new HashMap();
            schedNoMap.put("CLEARING_TYPE", getCbmClearingType().getKeyForSelected());
            
            System.out.println("BranchID in GetSchedule: " + getSelectedBranchID());
            
            schedNoMap.put("BRANCH_ID", getSelectedBranchID());
            final List resultList = ClientUtil.executeQuery("inwardClearing.getScheduleNo", schedNoMap);
            //--- If the selected Clearing Type have the record, then set the Schedule No
            //--- appropriate to that.
           key=new ArrayList();
           value =new ArrayList();
           key.add("");
           value.add("");
            if(!(resultList.size()==0)){
                for (int i=0;i<resultList.size();i++){
                 HashMap resultMap = (HashMap)resultList.get(i);
                key.add(resultMap.get("SCHEDULE_NO"));
                value.add(resultMap.get("SCHEDULE_NO"));
                }
            }// else if(resultList.size()==0){ //--- Else if Clearing Type does not have the
                scheduleNumber = "";   //--- record, reset the Schedule No.
            //}
           cbmScheduleNo= new ComboBoxModel(key,value);
           setChanged();
//           notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
//        setTxtScheduleNumber(scheduleNumber);
        return scheduleNumber;
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public InwardClearingTO setInwardClearing() {
        log.info("In setInwardClearing()");
        
        final InwardClearingTO objInwardClearingTO = new InwardClearingTO();
        try{
            objInwardClearingTO.setInwardId(lblTransactionId);
            objInwardClearingTO.setProdId((String)cbmProdId.getKeyForSelected());
            objInwardClearingTO.setAcctNo(txtAccountNumber);
            objInwardClearingTO.setClearingType((String)cbmClearingType.getKeyForSelected());
            objInwardClearingTO.setScheduleNo((String)cbmScheduleNo.getKeyForSelected());
            objInwardClearingTO.setInstrumentType((String)cbmInstrumentTypeID.getKeyForSelected());
            objInwardClearingTO.setInstrumentNo1(txtInstrumentNo1);
            objInwardClearingTO.setInstrumentNo2(txtInstrumentNo2);
            
            Date IsDt = DateUtil.getDateMMDDYYYY(tdtInstrumentDate);
            if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            objInwardClearingTO.setInstrumentDt(isDate);
            }else{
                objInwardClearingTO.setInstrumentDt(DateUtil.getDateMMDDYYYY(tdtInstrumentDate));
            }
//            objInwardClearingTO.setInstrumentDt(DateUtil.getDateMMDDYYYY(tdtInstrumentDate));
            
            objInwardClearingTO.setAmount(CommonUtil.convertObjToDouble(txtAmount));
            objInwardClearingTO.setPayeeName(txtPayeeName);
//            objInwardClearingTO.setBankCode((String)cbmBankCode.getKeyForSelected());
            objInwardClearingTO.setBankCode(getTxtBankCodeID());
            
//            objInwardClearingTO.setBranchCode((String)cbmBranchCode.getKeyForSelected());
            objInwardClearingTO.setBranchCode(getTxtBranchCodeID());
//            System.out.println("Branch :" + (String)cbmBranchCode.getKeyForSelected());
//            System.out.println("BranchCode1 :" + getCboBranchCode());
            
            Date ClrDt = DateUtil.getDateMMDDYYYY(lblClearingDate);
            if(ClrDt != null){
            Date clrDate = (Date)curDate.clone();
            clrDate.setDate(ClrDt.getDate());
            clrDate.setMonth(ClrDt.getMonth());
            clrDate.setYear(ClrDt.getYear());
            objInwardClearingTO.setClearingDt(clrDate);
            }else{
                objInwardClearingTO.setClearingDt(DateUtil.getDateMMDDYYYY(lblClearingDate));
            }
//            objInwardClearingTO.setClearingDt(DateUtil.getDateMMDDYYYY(lblClearingDate));
            
            //            objInwardClearingTO.setInCurrency((String)cbmCurrency.getKeyForSelected());
            objInwardClearingTO.setInCurrency(CURRENCY);
            objInwardClearingTO.setOutputAmount(CommonUtil.convertObjToDouble(txtCovtAmount));
            
            objInwardClearingTO.setProdType(this.getCboProductType());
            objInwardClearingTO.setAcHdId(getLblAccountHeadProdId());
            
            objInwardClearingTO.setBranchId(getSelectedBranchID());
            
            objInwardClearingTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objInwardClearingTO;
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        InwardClearingTO objInwardClearingTO = null;
        //Taking the Value of Prod_Id from each Table...
        // Here the first Row is selected...
        objInwardClearingTO = (InwardClearingTO) ((List) mapData.get("InwardClearingTO")).get(0);
        setInwardClearingTO(objInwardClearingTO);
        this.oldamount=(CommonUtil.convertObjToDouble(objInwardClearingTO.getAmount())).doubleValue();
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    private void setInwardClearingTO(InwardClearingTO objInwardClearingTO) throws Exception{
        log.info("In setInwardClearingTO()");
        setCboProductType(objInwardClearingTO.getProdType());
        getProdIdData();
        setStatusBy(objInwardClearingTO.getSuserId());
        setCboProdId(CommonUtil.convertObjToStr(getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getProdId()))));
        setTxtAccountNumber(CommonUtil.convertObjToStr(objInwardClearingTO.getAcctNo()));
        setCboClearingType((String) getCbmClearingType().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getClearingType())));
        setCboScheduleNo(CommonUtil.convertObjToStr(objInwardClearingTO.getScheduleNo()));
        setCboInstrumentTypeID((String) getCbmInstrumentTypeID().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getInstrumentType())));
        setTxtInstrumentNo1(CommonUtil.convertObjToStr(objInwardClearingTO.getInstrumentNo1()));
        setTxtInstrumentNo2(CommonUtil.convertObjToStr(objInwardClearingTO.getInstrumentNo2()));
        setTdtInstrumentDate(DateUtil.getStringDate(objInwardClearingTO.getInstrumentDt()));
        setTxtAmount(CommonUtil.convertObjToStr(objInwardClearingTO.getAmount()));
        setTxtPayeeName(CommonUtil.convertObjToStr(objInwardClearingTO.getPayeeName()));
//        setCboBankCode((String) getCbmBankCode().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getBankCode())));
        setTxtBankCodeID(CommonUtil.convertObjToStr(objInwardClearingTO.getBankCode()));
        /*
         * Combo box model for the Branch Code is to be added because its not Called in the
         * fillDropdown() method; as its dependent on the Value of Bank Code.
         */
        
        this.cbmBankCode.setKeyForSelected(objInwardClearingTO.getBankCode());
        
        getBranchData();
        
//        setCboBranchCode((String) getCbmBranchCode().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getBranchCode())));
        setTxtBranchCodeID(CommonUtil.convertObjToStr(objInwardClearingTO.getBranchCode()));
        System.out.println("Date: " + DateUtil.getStringDate(objInwardClearingTO.getInwardDt()));
        
        setLblTransactionDate(DateUtil.getStringDate(objInwardClearingTO.getInwardDt()));
        //        setCboCurrency((String) getCbmCurrency().getDataForKey(CommonUtil.convertObjToStr(objInwardClearingTO.getInCurrency())));
        setTxtCovtAmount(CommonUtil.convertObjToStr(objInwardClearingTO.getOutputAmount()));
        
        this.setProdTypeTO(objInwardClearingTO.getProdType());
        this.setLblAccountHeadDesc(objInwardClearingTO.getAcHdId());
        
        setAuthStatus(CommonUtil.convertObjToStr(objInwardClearingTO.getAuthorizeStatus()));
    }
    
    /*
     * Setter and Getter methods for the Authorization.
     */
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * To perform Appropriate operation... Insert, Update, Delete...
     */
    public void doAction(){
        log.info("In doAction()");
        try {
            
            // The following block added by Rajesh to avoid Save operation after Authorization.
            // If one person opened a transaction for Edit and another person opened the same 
            // transaction for Authorization, the system is allowing to save after Authorization also.  
            // So, after authorization again the GL gets updated and a/c level shadow credit/debit goes negative.
            // In this case the should not allow to save or some error message should display.  
            if ((!getLblTransactionId().equals("")) && getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE && getActionType()!=ClientConstants.ACTIONTYPE_REJECT) {
                HashMap whereMap = new HashMap();
                whereMap.put("INWARD_ID", getLblTransactionId());
                whereMap.put("CLEARING_DT", curDate.clone());
                whereMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                List lst = ClientUtil.executeQuery("getInwardClearingAuthorizeStatus", whereMap);
                if (lst!=null && lst.size()>0) {
                    whereMap = (HashMap) lst.get(0);
                    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                    if (!authStatus.equals("")) {
                        setActionType(ClientConstants.ACTIONTYPE_FAILED);
                        throw new TTException("This transaction already "+authStatus.toLowerCase()+" by "+authBy);
                    }
                }
            }
            // End
        
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform(-1);
                }
            }
            else
                log.info("Action Type Not Defined In setInwardClearingTO()");
        } catch (Exception e) {
            try{
                log.info("Error In doAction()");
                setResult(ClientConstants.ACTIONTYPE_FAILED);
                
                /** If the Exception is TTException */
                if(e instanceof TTException){
                    exceptionHandle(e);
                } else {
                    parseException.logException(e,true);
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        min_bal_check=true;
    }
    
    private void exceptionHandle(Exception e) throws Exception {
        HashMap expMap = ((TTException) e).getExceptionHashMap();
        if (expMap != null) {
            ArrayList list = (ArrayList)expMap.get("EXCEPTIONLIST");

            int task;
            String doTask = "";

            /** if the TTException is of the Typr
             * "Available Balance" and/or "Tally Count"
             *  Exception and bounce Options Should be Provided...
             *
             * Else it should go to Bounce...
             */
            if( ((list.size() == 2) && list.contains("TC") && list.contains("IB"))
            || ((list.size() == 1) && (list.contains("TC")))){

                System.out.println("Exception: " + list);

                String[] obj = {resourceBundle.getString("OK")};
                parseException.setDialogOptions(obj);
                task = parseException.logException(e,true);
                doTask = obj[task];
            }
            else if(list.contains("MIN")){
                String[] obj = {resourceBundle.getString("CONTINUE"), resourceBundle.getString("CANCEL")};
                parseException.setDialogOptions(obj);
                task = parseException.logException(e,true);
                doTask = obj[task];
            }
            else{
//                if(list!=null && list.size()>0  ){
//                    TransactionRuleHashMap exp= new TransactionRuleHashMap();
//                    String err="";
//                    String messages="";
//                    for (int i=0; i<list.size(); i++) {
//                        err = CommonUtil.convertObjToStr(list.get(i));
//                        messages += CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(err)) + "\n";
//                    }
//                    ClientUtil.showAlertWindow(messages);
////                    if(list.contains(TransactionConstants.INSTRUMENT_NOT_ISSUED)){
////                        ClientUtil.showAlertWindow(CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(TransactionConstants.INSTRUMENT_NOT_ISSUED)));
////
////                    }
////
////                    if(list.contains(TransactionConstants.INSTRUMENT_CLEARED)){
////                        ClientUtil.showAlertWindow(CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(TransactionConstants.INSTRUMENT_CLEARED)));
////
////                    }
////                    if(list.contains(TransactionConstants.INSTRUMENT_CLEARED_NA)){
////                        ClientUtil.showAlertWindow(CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(TransactionConstants.INSTRUMENT_CLEARED_NA)));
////
////                    }
////                    if(list.contains(TransactionConstants.INSTRUMENT_STOP_PAY)){
////                        ClientUtil.showAlertWindow(CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(TransactionConstants.INSTRUMENT_STOP_PAY)));
////
////                    }
////                    if(list.contains(TransactionConstants.INSTRUMENT_STOP_PAY_NA )){
////                        ClientUtil.displayAlert(CommonUtil.convertObjToStr(exp.getExceptionHashMap().get(TransactionConstants.INSTRUMENT_STOP_PAY_NA )));
////
////                    }
//
//                }
                
                String[] obj = {resourceBundle.getString("BOUNCE"), resourceBundle.getString("CANCEL")};
                parseException.setDialogOptions(obj);
                task = parseException.logException(e,true);
                doTask = obj[task];
                
            }
            parseException.setDialogOptions(null);
        /** To do the Selected Task...*/
//                    if(doTask.equalsIgnoreCase(resourceBundle.getString("EXCEPTION"))){
//                        taskSelected = EXCEPTION;
//                        doActionPerform();
//                    }else 
            if(doTask.equalsIgnoreCase(resourceBundle.getString("BOUNCE"))){
                taskSelected = BOUNCE;
                doActionPerform(1);
            }
            if(doTask.equalsIgnoreCase(resourceBundle.getString("CONTINUE"))){
                taskSelected = POST;
                min_bal_check=false;
                doActionPerform(0);
            }
        } else {
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform(int option) throws Exception{
        //__ To get the action to be performed from the user...
        
        
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        int yes_no = 0;
        //__ If its not the Authorization...
          List lst =null;
            HashMap where=new HashMap();
            String msg="";
//                 String mapName="getAccountStatus"+getProdTypeTO(); 
//                 where.put("ACT_NUM", getTxtAccountNumber());
////                 where.put("PROD_ID", getCboProductType());
//                if(getProdTypeTO().equals("OA") || getProdTypeTO().equals("AD"))
//                   lst=ClientUtil.executeQuery(mapName, where);
//                if(lst!=null && lst.size()>0){
//                    HashMap satatusMap=new HashMap();
//                    satatusMap=(HashMap)lst.get(0);
//                    if(CommonUtil.convertObjToInt(satatusMap.get("FREEZE_AMT"))>0)
//                        msg=msg+"Account is FREEZED"+"\n";
//                    
//                 if(CommonUtil.convertObjToInt(satatusMap.get("LIEN_AMT"))>0)
//                     msg=msg+"Account is LIEN"+"\n";
//                   if((CommonUtil.convertObjToStr(satatusMap.get("ACT_STATUS_ID")).equals("CLOSED")))
//                     msg=msg+"Account is Closed"+"\n";
//                    if (msg.length()>0)
//                     ClientUtil.displayAlert(msg);
//                }
         
        if(getAuthorizeMap() == null){
                 //__ If the Command is not Delete...Ask the option...
            //getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){ BEFORE 
            if(option == -1 && !getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
                String[] obj = {resourceBundle.getString("POST"),
                resourceBundle.getString("BOUNCE"), resourceBundle.getString("CANCEL")};
                option = COptionPane.showOptionDialog(null, resourceBundle.getString("ACTION_SELECTION"),resourceBundle.getString("ACTION_TITLE"),
                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                
                //__ Assigning the Task...
                
                if(option == 0){
                    taskSelected = POST;
                    
                }else if(option == 1){
                    taskSelected = BOUNCE;
                    
                }else if(option == 2){
                    taskSelected = CANCEL;
                    System.out.println("Tak Assigned to CANCEl...");
                } else if(option==COptionPane.CLOSED_OPTION){
                    taskSelected=CANCEL;
//                    return;
                }
            }
            
            final InwardClearingTO objInwardClearingTO = setInwardClearing();
            objInwardClearingTO.setCommand(getCommand());
            data.put("InwardClearingTO",objInwardClearingTO);
            data.put("OLDAMOUNT", new Double(this.oldamount));
            
            data.put("BounceRemarks", "");
        } else {
            HashMap authmeg =new HashMap();
            ArrayList hash=(ArrayList)_authorizeMap.get("AUTHORIZEDATA");
            authmeg=(HashMap)hash.get(0);
            authmeg.put("INWARD_ID", authmeg.get("INWARD ID"));
            if(getAuthStatus().equals("BOUNCED")){
            authmeg.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            authmeg.put("TRANS_DT",curDate.clone());
            authmeg=(HashMap)((List)ClientUtil.executeQuery("getBouncingDetails", authmeg)).get(0);
//            ClientUtil.showMessageWindow(""+authmeg.get("BOUNCING_REASON")+"\n "+authmeg.get("CLEARING_TYPE")+"\n"+authmeg.get("SCHEDULE_NO"));
            yes_no=ClientUtil.confirmationAlert("Bouncing Reason : "+authmeg.get("BOUNCING_REASON")+
                                                   "\nClearing Type  : "+authmeg.get("CLEARING_TYPE")+
                                                   "\nOutward Schedule No. : "+authmeg.get("SCHEDULE_NO")+
                                                   "\n\nDo you want to Proceed?"); 
        }  } 
        if (yes_no == 0) {
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        
        /** Put the Type of the task to be performed in DAO...*/
        data.put("TaskSelected", String.valueOf(taskSelected));
        
        if((min_bal_check) && ((taskSelected == POST) || getAuthorizeMap()!= null)) {
           data.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER"); 
        }
        
        /** If the Task Selected is Bounce, Take the Reason and Pass as Remark to the DAO...*/
        if( (taskSelected) == BOUNCE){
            String message = (String) COptionPane.showInputDialog(null, resourceBundle.getString("BOUNCEREASON"), "Bouncing Reason", COptionPane.PLAIN_MESSAGE, null, getBouncingReasons(), "");
            data.put("BounceRemarks", message);

            System.out.println("Bounce Reason21212121: " + message);
            
            //__ To Enter the Bounce Clearing Type...
            if(message != null && message.length() > 0){
                
                System.out.println("Bounce Reason not null...");
                // To load Bounce Clearing Type -- by Rajesh
                populateClearingType();
                //__ To Check if the Bounce Clearing type Exists or not...
                if(getBouncingClearingType()!= null && getBouncingClearingType().length > 0){
                    String bClearingType = (String) COptionPane.showInputDialog(null, resourceBundle.getString("BOUNCECLEARINGTYPE"), resourceBundle.getString("B_C_TYPE"), COptionPane.PLAIN_MESSAGE, null, getBouncingClearingType(), getBouncingClearingType()[0]);
                    data.put("BounceClearingType", bClearingType);
                    
                }else{ //__ Display Alert...
                    
                    ClientUtil.showAlertWindow(resourceBundle.getString("BOUNCETYPE"));
                    
                    taskSelected = CANCEL; //__ Do Nothing...
                }
            }
        }
        
        System.out.println("Task performed..." + taskSelected);
        System.out.println("Task: " + CANCEL);
        
        
        //__ If the taskSelected is not Cancel...
        if(taskSelected != CANCEL && taskSelected>-1 ){
            HashMap proxyReturnMap = proxy.execute(data,operationMap);
            if (proxyReturnMap != null) {
                setProxyReturnMap(proxyReturnMap);
                if (proxyReturnMap.containsKey(CommonConstants.TRANS_ID)) {
                    ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
                } else if (proxyReturnMap.containsKey("REMARKS")) {
                    ClientUtil.showMessageWindow("Insufficient Balance to Debit the Cheque Return Charges...");
                }
            }
            
            setResult(actionType);
            
            //__ Reset the option...
            option = -1;
        }else{
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
        }
        //        resetForm();
    }
    
    private void populateClearingType() throws Exception {
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_NAME,"InwardClearing.getBounceClearingType");
        where.put(CommonConstants.PARAMFORQUERY, getSelectedBranchID());
        keyValue = ClientUtil.populateLookupData(where);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        setBouncingClearingType(value.toArray());        
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
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        
        setCboProdId("");
        setTxtAccountNumber("");
        setCboClearingType("");
        setCboScheduleNo("");
        setCboInstrumentTypeID("");
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setTdtInstrumentDate("");
        setTxtAmount("");
        setTxtCovtAmount("");
        setTxtPayeeName("");
        setCboBankCode("");
        setTxtBankCodeID("");
        setTxtBranchCodeID("");
        setCboBranchCode("");
        //        setCboCurrency("");
        
        this.setAuthorizeMap(null);
        this.oldamount = 0.0;
        
        this.taskSelected = 0;
        
        this.setCboProductType("");
        this.setProdTypeTO("");
        
        
        setAuthStatus("");
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        setCbmProdId(new ComboBoxModel(key,value));
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        System.out.println("reset setLblTransactionId() called...");
        
        resetProdIdDepLable();
        this.setLblTransactionId("");
        this.setLblTransactionDate("");
        resetClearingDetailsLable();
        setLblAccountNumberName("");
    }
    
    // To reset lables which are dependent on choosing of product id
    public void resetProdIdDepLable(){
        this.setLblAccountHeadProdId("");
        this.setLblAccountHeadDesc("");
        //        this.setLblProdCurrency("");
        //this.setLblAccountNumberName("");
    }
    
    // To reset lables which are dependent on choosing of product id
    public void resetClearingDetailsLable(){
        this.setLblClearingDate("");
        this.setLblTotalInstruments("");
        this.setLblTotalAmount("");
        this.setLblBookedInstrument("");
        this.setLblBookedAmount("");
        this.setLblCountWarning("");
        
        this.setCountWarning("");
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
        setChanged();
        notifyObservers();
    }
    
    //===================================
    private String cboProdId = "";
    private String txtAccountNumber = "";
    private String cboClearingType = "";
    private String cboScheduleNo = "";
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private String tdtInstrumentDate = "";
    private String txtAmount = "";
    private String txtCovtAmount = "";
    private String txtPayeeName = "";
    private String cboCurrency = "";
    private String cboBankCode = "";
    private String txtBankCodeID="";
    private String cboBranchCode = "";
    private String txtBranchCodeID="";
    private String cboInstrumentTypeID = "";
    private String cboProductType = "";
    
    //-----------------------------------------------
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        //setChanged();
    }
    String getCboProdId(){
        return this.cboProdId;
    }
    public void setCbmProdId(ComboBoxModel cbmProdId){
        this.cbmProdId = cbmProdId;
        //setChanged();
    }
    
    ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
    //-----------------------------------------------
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        //setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
    
    //-----------------------------------------------
    void setCboClearingType(String cboClearingType){
        this.cboClearingType = cboClearingType;
        //setChanged();
    }
    String getCboClearingType(){
        return this.cboClearingType;
    }
    public void setCbmClearingType(ComboBoxModel cbmClearingType){
        this.cbmClearingType = cbmClearingType;
        //setChanged();
    }
    
    ComboBoxModel getCbmClearingType(){
        return cbmClearingType;
    }
    //-----------------------------------------------
    
//    void setTxtScheduleNumber(String txtScheduleNumber){
//        this.txtScheduleNumber = txtScheduleNumber;
//        //setChanged();
//    }
//    String getTxtScheduleNumber(){
//        return this.txtScheduleNumber;
//    }
    
    void setCboInstrumentTypeID(String cboInstrumentTypeID){
        this.cboInstrumentTypeID = cboInstrumentTypeID;
        //setChanged();
    }
    String getCboInstrumentTypeID(){
        return this.cboInstrumentTypeID;
    }
    public void setCbmInstrumentTypeID(ComboBoxModel cbmInstrumentTypeID){
        this.cbmInstrumentTypeID = cbmInstrumentTypeID;
        //setChanged();
    }
    
    ComboBoxModel getCbmInstrumentTypeID(){
        return cbmInstrumentTypeID;
    }
    
    void setTxtInstrumentNo1(String txtInstrumentNo1){
        this.txtInstrumentNo1 = txtInstrumentNo1;
        //setChanged();
    }
    String getTxtInstrumentNo1(){
        return this.txtInstrumentNo1;
    }
    
    void setTxtInstrumentNo2(String txtInstrumentNo2){
        this.txtInstrumentNo2 = txtInstrumentNo2;
        //setChanged();
    }
    String getTxtInstrumentNo2(){
        return this.txtInstrumentNo2;
    }
    
    
    void setTdtInstrumentDate(String tdtInstrumentDate){
        this.tdtInstrumentDate = tdtInstrumentDate;
        //setChanged();
    }
    String getTdtInstrumentDate(){
        return this.tdtInstrumentDate;
    }
    
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        //setChanged();
    }
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    void setTxtCovtAmount(String txtCovtAmount){
        this.txtCovtAmount = txtCovtAmount;
        //setChanged();
    }
    String getTxtCovtAmount(){
        return this.txtCovtAmount;
    }
    
    void setTxtPayeeName(String txtPayeeName){
        this.txtPayeeName = txtPayeeName;
        //setChanged();
    }
    String getTxtPayeeName(){
        return this.txtPayeeName;
    }
    //==============================================
    void setCboBankCode(String cboBankCode){
        this.cboBankCode = cboBankCode;
        //setChanged();
    }
    String getCboBankCode(){
        return this.cboBankCode;
    }
    
    public void setCbmBankCode(ComboBoxModel cbmBankCode){
        this.cbmBankCode = cbmBankCode;
        //setChanged();
    }
    
    ComboBoxModel getCbmBankCode(){
        return cbmBankCode;
    }
    
    void setCboBranchCode(String cboBranchCode){
        this.cboBranchCode = cboBranchCode;
        setChanged();
    }
    String getCboBranchCode(){
        return this.cboBranchCode;
    }
    
    public void setCbmBranchCode(ComboBoxModel cbmBranchCode){
        this.cbmBranchCode = cbmBranchCode;
        setChanged();
    }
    
    public ComboBoxModel getCbmBranchCode(){
        return cbmBranchCode;
    }
    //------------------------------------
    void setCboCurrency(String cboCurrency){
        this.cboCurrency = cboCurrency;
        //setChanged();
    }
    String getCboCurrency(){
        return this.cboCurrency;
    }
    
    public void setCbmCurrency(ComboBoxModel cbmCurrency){
        this.cbmCurrency = cbmCurrency;
        //setChanged();
    }
    
    ComboBoxModel getCbmCurrency(){
        return cbmCurrency;
    }
    //==========================================
    public void setLblAccountHeadProdId(String lblAccountHeadProdId){
        this.lblAccountHeadProdId = lblAccountHeadProdId;
        //setChanged();
    }
    public String getLblAccountHeadProdId(){
        return this.lblAccountHeadProdId;
    }
    
    public void setLblAccountHeadDesc(String lblAccountHeadDesc){
        this.lblAccountHeadDesc = lblAccountHeadDesc;
        //setChanged();
    }
    public String getLblAccountHeadDesc(){
        return this.lblAccountHeadDesc;
    }
    
    public void setLblProdCurrency(String lblProdCurrency){
        this.lblProdCurrency = lblProdCurrency;
        //setChanged();
    }
    public String getLblProdCurrency(){
        return this.lblProdCurrency;
    }
    
    public void setAccountHead() {
        try {
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",(String)cbmProdId.getKeyForSelected());
            System.out.println("setAccountHead " + this.getCboProductType());
            
            if(this.getCboProductType().length() > 0){
                final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+this.getCboProductType(), accountHeadMap);
                if (resultList != null && resultList.size() > 0){
                    final HashMap resultMap = (HashMap)resultList.get(0);
                    setLblAccountHeadProdId(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
                    setLblAccountHeadDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD_DESC")));
                /*
                 * To Get the Lookup_Desc (Currency Name) from Lookup_Master
                 */
                    //                final String PRODCURRENCY = CommonUtil.convertObjToStr(resultMap.get("PROD_CURRENCY"));
                    //                accountHeadMap.put("PRODCURRENCY",PRODCURRENCY);
                    //                final List CurrencyList = ClientUtil.executeQuery("getProdCurrency", accountHeadMap);
                    //                if (CurrencyList != null && CurrencyList.size() > 0){
                    //                    final HashMap currencyMap = (HashMap)CurrencyList.get(0);
                    //                    setLblProdCurrency(PRODCURRENCY+"["+CommonUtil.convertObjToStr(currencyMap.get("LOOKUP_DESC"))+"]");
                    //                }
                }else{
                    resetProdIdDepLable();
                    setTxtAccountNumber("");
                }
            }
            //            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    // Fo setting the Name of the Account Number Holder...
    public void setLblAccountNumberName(String lblAccountNumberName){
        this.lblAccountNumberName = lblAccountNumberName;
    }
    public String getLblAccountNumberName(){
        return this.lblAccountNumberName;
    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProductType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setProdTypeTO(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                getProdIdData();
//                getProducts();
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
                getProdIdData();
                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }

    
    public boolean getClearingDetails(){
        final HashMap whereMap = new HashMap();
        boolean existVal = false;
        if (getCboScheduleNo().length() >0){
            whereMap.put("SCHEDULE_NO", getCboScheduleNo());
            whereMap.put("BRANCH_ID", getSelectedBranchID());
            existVal = scheduleNoExistance();
            if (existVal){
                List objList = ClientUtil.executeQuery("clearing.getTallyDetail", whereMap);
                HashMap objMap = (HashMap)objList.get(0);
                setLblClearingDate(DateUtil.getStringDate((Date)objMap.get("CLEARING_DT")));
                
                /**To Select the Min of Service and Physical Count...*/
                int servCount = CommonUtil.convertObjToInt(objMap.get("SERV_INSTRUMENTS"));
                int phyCount = CommonUtil.convertObjToInt(objMap.get("PHY_INSTRUMENTS"));
                
                setLblTotalInstruments(String.valueOf(phyCount));
                setLblTotalAmount(CommonUtil.convertObjToStr(objMap.get("PHY_AMOUNT")));
                
                String message = "";
                if(servCount > phyCount){
                    //                    InwardClearingRB objRb = new InwardClearingRB();
                    message = resourceBundle.getString("COUNTWARNING");
                }
                
                /**Warning Message To Be Displayed...*/
                setLblCountWarning(message);
                
                objList = ClientUtil.executeQuery("clearing.getClearingDetail", whereMap);
                if (objList.size() >0){
                    objMap = (HashMap)objList.get(0);
                    
                    int bookedCount = CommonUtil.convertObjToInt(objMap.get("COUNT"));
                    //__ Booked Instrument Exceeds Physical Count...
                    if(bookedCount > phyCount){
                        if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
                        System.out.println("Booked Instrument Exceeds Physical Count...");
                        setCountWarning(resourceBundle.getString("BOOKEDWARNING"));
                        existVal = false;
                        }
                    }else{
                        System.out.println("Booked Instrument less than Physical Count...");
                        setLblBookedInstrument(CommonUtil.convertObjToStr(objMap.get("COUNT")));
                        if (getLblBookedInstrument().equals("0")){
                            setLblBookedAmount(getLblBookedInstrument());
                        }else{
                            setLblBookedAmount(CommonUtil.convertObjToStr(objMap.get("SUM")));
                        }
                        
                    }
                }
            }else{
                resetClearingDetailsLable();
            }
//            ttNotifyObservers();
        }
        return existVal;
    }
    
    public void setCountWarning(String countWarning){
        this.countWarning = countWarning;
    }
    public String getCountWarning(){
        return this.countWarning;
    }
    public void getSchudeledate(){
        HashMap hash=new HashMap();
        hash.put("SCHEDULE_NO",cbmScheduleNo.getKeyForSelected());
        hash.put("CLEARING_TYPE",cbmClearingType.getKeyForSelected());
        hash.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst=ClientUtil.executeQuery("inwardClearing.getScheduleNo", hash);
       
        if(lst !=null && lst.size()>0){
            hash=(HashMap)lst.get(0);
            setLblClearingDate(CommonUtil.convertObjToStr(hash.get("CLEARING_DT")));
            
        }
//        ttNotifyObservers();
         hash =null;
    }
    public boolean scheduleNoExistance(){
        boolean existVal = false;
        final HashMap whereMap = new HashMap();
        whereMap.put("SCHEDULE_NO", cbmScheduleNo.getKeyForSelected());
        List clearingList = ClientUtil.executeQuery("clearing.getScheduleNoExistance", whereMap);
        if (CommonUtil.convertObjToDouble(((HashMap)clearingList.get(0)).get("COUNT")).intValue() > 0){
            existVal = true;
        }
        return existVal;
    }
    
    public void setAccountName(String AccountNo){
        try {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM",AccountNo);
            if(this.getProdTypeTO().length() > 0 && AccountNo.length() > 0 && !this.getProdTypeTO().equals("RM")){
                final List resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdTypeTO(), accountNameMap);
                if(resultList !=null && resultList.size()>0) {
                    final HashMap resultMap = (HashMap)resultList.get(0);
                     String pID =  getCbmProdId().getKeyForSelected().toString() ;
                     HashMap dataMap = new HashMap();
                     accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                     List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+this.getProdTypeTO(),accountNameMap);
                     if(lst!=null && lst.size()>0){
                         dataMap=(HashMap)lst.get(0);
                         if(dataMap.get("PROD_ID").equals(pID)){
                        setLblAccountNumberName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
                         }else{
                             setLblAccountNumberName("");
                         }
                     }else{
                         setLblAccountNumberName("");
                     }
                }else{
                    setLblAccountNumberName("");
                }
            }else{
                setLblAccountNumberName("");
            }
            //            this.ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    // Fo setting the transaction Id in UI at the Time of Edit or Delete...
    public void setLblTransactionId(String lblTransactionId){
        this.lblTransactionId = lblTransactionId;
    }
    public String getLblTransactionId(){
        return this.lblTransactionId;
    }
    
    // Fo setting the Name of the Transaction  Date in Ui at the time of Edit and Delete...
    public void setLblTransactionDate(String lblTransactionDate){
        this.lblTransactionDate = lblTransactionDate;
    }
    public String getLblTransactionDate(){
        return this.lblTransactionDate;
    }
    
    // Fo setting the Name of the Clearing Date in Ui at the time of Edit and Delete...
    public void setLblClearingDate(String lblClearingDate){
        this.lblClearingDate = lblClearingDate;
    }
    public String getLblClearingDate(){
        return this.lblClearingDate;
    }
    
    /*public void setClearingDate(String INWARDID){
        try {
            final HashMap clearingDateMap = new HashMap();
            clearingDateMap.put("INWARD_ID",INWARDID);
            final List resultList = ClientUtil.executeQuery("getClearingDate", clearingDateMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            setLblClearingDate(DateUtil.getStringDate((Date)resultMap.get("CLEARING_DT")));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }*/
    
    public void setLblTotalInstruments(String lblTotalInstrument){
        this.lblTotalInstrument = lblTotalInstrument;
    }
    public String getLblTotalInstrument(){
        return this.lblTotalInstrument;
    }
    
    public void setLblTotalAmount(String lblTotalAmount){
        this.lblTotalAmount = lblTotalAmount;
    }
    public String getLblTotalAmount(){
        return this.lblTotalAmount;
    }
    
    public void setLblBookedInstrument(String lblBookedInstrument){
        this.lblBookedInstrument = lblBookedInstrument;
    }
    public String getLblBookedInstrument(){
        return this.lblBookedInstrument;
    }
    
    public void setLblBookedAmount(String lblBookedAmount){
        this.lblBookedAmount = lblBookedAmount;
    }
    public String getLblBookedAmount(){
        return this.lblBookedAmount;
    }
    
    public void setLblCountWarning(String lblCountWarning){
        this.lblCountWarning = lblCountWarning;
    }
    public String getLblCountWarning(){
        return this.lblCountWarning;
    }
    
    
    
    //    public void setBalance(String AccountNo){
    //        try {
    //            final HashMap balanceMap = new HashMap();
    //            balanceMap.put("ACT_NUM",AccountNo);
    //            final List resultList = ClientUtil.executeQuery("getBalance"+this.getCboProductType(), balanceMap);
    //            final HashMap resultMap = (HashMap)resultList.get(0);
    //            String availableBalance = null;
    //            String clearBalance = null;
    //            String shadowCredit = null;
    //            String shadowDebit = null;
    //            String totalBalance = null;
    //
    //            availableBalance = (resultMap.get("AVAILABLE_BALANCE")==null) ? "0" : resultMap.get("AVAILABLE_BALANCE").toString();
    //            clearBalance = (resultMap.get("CLEAR_BALANCE")==null) ? "0" : resultMap.get("CLEAR_BALANCE").toString();
    //            shadowCredit  = (resultMap.get("SHADOW_CREDIT")==null) ? "0" : resultMap.get("SHADOW_CREDIT").toString();
    //            shadowDebit = (resultMap.get("SHADOW_DEBIT")==null) ? "0" : resultMap.get("SHADOW_DEBIT").toString();
    //            totalBalance = (resultMap.get("TOTAL_BALANCE")==null) ? "0" : resultMap.get("TOTAL_BALANCE").toString();
    //
    //        }catch(Exception e){
    //            e.printStackTrace();
    //            parseException.logException(e,true);
    //        }
    //    }
    
    /**
     * To get the Value of Branch Code, Depending on the Bank Code
     */
    
    public void getBranchData(){
        System.out.println("getBranchData()");
        try {
            lookUpHash = new HashMap();
            final String bankCode = CommonUtil.convertObjToStr(cbmBankCode.getKeyForSelected());
            System.out.println("BankSelected: " + bankCode);
            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBranch");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, bankCode);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmBranchCode = new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    //==========================================
    
    
    //    public void setActData(String AccountNo){
    //        try {
    //            final HashMap accDataMap = new HashMap();
    //            accDataMap.put("ACT_NUM",AccountNo);
    //            final List resultList = ClientUtil.executeQuery("getActData"+this.getCboProductType(), accDataMap);
    //            if(resultList.size() > 0){
    //                final HashMap resultMap = (HashMap)resultList.get(0);
    //            }
    //        }catch(Exception e){
    //            System.out.println("Error in setActData()");
    //        }
    //    }
    
    /** Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     *
     */
    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /** Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     *
     */
    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /** Getter for property cboProductType.
     * @return Value of property cboProductType.
     *
     */
    public String getCboProductType() {
        return cboProductType;
    }
    
    /** Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     *
     */
    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }
    public void setProdTypeTO(String prodTypeTO) {
        this.prodTypeTO = prodTypeTO;
        setChanged();
    }
    public String getProdTypeTO() {
        return this.prodTypeTO;
    }
    
    public Object[] getBouncingReasons() {
        return objReasons;
    }
    
    public void setBouncingReasons(Object[] objReason) {
        this.objReasons = objReason;
    }
    
    
    //__ To set the Bouncing ClearingType...
    public Object[] getBouncingClearingType() {
        return objBouncingReasons;
    }
    
    public void setBouncingClearingType(Object[] objBouncingReasons) {
        this.objBouncingReasons = objBouncingReasons;
    }
    
    
    public String getAuthStatus() {
        return this.authStatus;
    }
    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
    
    /**
     * Getter for property cbmScheduleNo.
     * @return Value of property cbmScheduleNo.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmScheduleNo() {
        return cbmScheduleNo;
    }
    
    /**
     * Setter for property cbmScheduleNo.
     * @param cbmScheduleNo New value of property cbmScheduleNo.
     */
    public void setCbmScheduleNo(com.see.truetransact.clientutil.ComboBoxModel cbmScheduleNo) {
        this.cbmScheduleNo = cbmScheduleNo;
    }
    
    /**
     * Getter for property cboScheduleNo.
     * @return Value of property cboScheduleNo.
     */
    public java.lang.String getCboScheduleNo() {
        return cboScheduleNo;
    }
    
    /**
     * Setter for property cboScheduleNo.
     * @param cboScheduleNo New value of property cboScheduleNo.
     */
    public void setCboScheduleNo(java.lang.String cboScheduleNo) {
        this.cboScheduleNo = cboScheduleNo;
    }
    
    /**
     * Getter for property txtBankCodeID.
     * @return Value of property txtBankCodeID.
     */
    public java.lang.String getTxtBankCodeID() {
        return txtBankCodeID;
    }
    
    /**
     * Setter for property txtBankCodeID.
     * @param txtBankCodeID New value of property txtBankCodeID.
     */
    public void setTxtBankCodeID(java.lang.String txtBankCodeID) {
        this.txtBankCodeID = txtBankCodeID;
    }
    
    /**
     * Getter for property txtBranchCodeID.
     * @return Value of property txtBranchCodeID.
     */
    public java.lang.String getTxtBranchCodeID() {
        return txtBranchCodeID;
    }
    
    /**
     * Setter for property txtBranchCodeID.
     * @param txtBranchCodeID New value of property txtBranchCodeID.
     */
    public void setTxtBranchCodeID(java.lang.String txtBranchCodeID) {
        this.txtBranchCodeID = txtBranchCodeID;
    }
    
     public String getScheduleNoForView() {
        String scheduleNumber = "";
        try {
            final HashMap schedNoMap = new HashMap();
            schedNoMap.put("CLEARING_TYPE", getCbmClearingType().getKeyForSelected());
            
            System.out.println("BranchID in GetSchedule: " + getSelectedBranchID());
            
            schedNoMap.put("BRANCH_ID", getSelectedBranchID());
            List lstSchNo=null;
             lstSchNo= ClientUtil.executeQuery("inwardClearing.getScheduleNoForView", schedNoMap);
            //--- If the selected Clearing Type have the record, then set the Schedule No
            //--- appropriate to that.
           key=new ArrayList();
           value =new ArrayList();
           key.add("");
           value.add("");
            if(!(lstSchNo.size()==0)){
                for (int i=0;i<lstSchNo.size();i++){
                 HashMap resultMap = (HashMap)lstSchNo.get(i);
                key.add(resultMap.get("SCHEDULE_NO"));
                value.add(resultMap.get("SCHEDULE_NO"));
                }
            }// else if(resultList.size()==0){ //--- Else if Clearing Type does not have the
                scheduleNumber = "";   //--- record, reset the Schedule No.
            //}
           cbmScheduleNo= new ComboBoxModel(key,value);
           setChanged();
//           notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
//        setTxtScheduleNumber(scheduleNumber);
        return scheduleNumber;
    }
}
