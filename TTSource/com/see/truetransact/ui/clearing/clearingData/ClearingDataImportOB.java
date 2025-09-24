/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ClearingDataImportOB.java
 *
 * Created on February 25, 2004, 2:48 PM
 */

package com.see.truetransact.ui.clearing.clearingData;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.transferobject.clearing.clearingData.ClearingDataImportTO;
import com.see.truetransact.transferobject.clearing.clearingData.ClearingDataImportDebitTO;
import com.see.truetransact.transferobject.clearing.clearingData.ClearingDataImportCreditTO;

import org.apache.log4j.Logger;

//import com.see.truetransact.serverside.tds.tdscalc;
/**
 *
 * @author  rahul
 */
public class ClearingDataImportOB extends CObservable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    
    private HashMap _authorizeMap;
    
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(ClearingDataImportUI.class);
    
    final String DEBIT = "DEBIT";
    final String CREDIT = "CREDIT";
    final String INITIATORTYPE = "CASHIER";
    
    //    private String lblAccHdDesc;
    private String txtAccHdId;
    private String lblAccName;
    private String transStatus = "";
    
    
    private String lblTransactionId;
    private String lblTransDate;
    private String lblInitiatorId;
    
    private static CInternalFrame frame;
    private String transactionId;
    
    private double oldAmount = 0.0;
    
    private String cr_cash;
    private String dr_cash;
    private ArrayList denominationList;
    private ArrayList recordData,deleteData;
    private TableModel tbmClearingData;
    //    private LinkedHashMap record;
    private String txtAccNo = "";
    private String txtInitiatorChannel = "";
    private boolean rdoTransactionType_Credit = false;
    private String txtAmount = "";
    private String balance = "";
    private String depSubNoStatus ="";
    private String prodId="";
    private String prod_desc="";
    private String depDailybNoK = "";
    private String depSubNoMode ="";
    private final int YES = 0;
    private final int NO = 1;
    private final int CANCEL = 2;
    //    private TableModel tbmTransfer;
    
    private int operation;
    
    private String cboAgentType;
    private ComboBoxModel cbmAgentType;
    private Date tdtInstrumentDate = null;
    private double totalAmt;
    private double totalGlAmt;
    private LinkedHashMap dataMap = new LinkedHashMap();
    private Object[] objReasons = null;
    private Object[] objBouncingReasons = null;
    
    private static ClearingDataImportOB clearingDataImportOB;
    
    private String lblTransactionTypeValue = "";
    private String lblUserNumberValue = "";
    private String lblUserNameValue = "";
    private String lblUserCreditRefNoValue = "";
    private String lblECSTapeInputNoValue = "";
    private String lblSponsorValue = "";
    private String lblUserBankAcNoValue = "";
    private String lblLedgerFolioNoValue = "";
    private String lblUserLimitValue = "";
    private String lblTotalAmountValue = "";
    private String lblSettlementDateValue = "";
    private String lblECSItemNoValue = "";
    private String lblCheckSumValue = "";
    private String lblFilterValue = "";
    private boolean rdoUploadingFile = false;
    private boolean rdoDownLoadingFile = false;
    public static ClearingDataImportOB getInstance() throws Exception {
        clearingDataImportOB = new ClearingDataImportOB();
        return clearingDataImportOB;
    }
    
    /** Creates a new instance of DailyDepositTransOB */
    public ClearingDataImportOB() throws Exception {
        initianSetup();
        setTable();
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        fillDropDown();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        //        setProductLbl();
    }
    
    private void setProductLbl(){
        HashMap hash=new HashMap();
        hash.put("DAILY","DAILY");
        List lst=ClientUtil.executeQuery("getProductDescription", hash);
        hash=(HashMap)lst.get(0);
        setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
        setProd_desc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
        setTxtAccHd(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ClearingDataImportJNDI");
        operationMap.put(CommonConstants.HOME, "clearing.clearingData.ClearingDataImportHome");
        operationMap.put(CommonConstants.REMOTE, "clearing.clearingData.ClearingDataImport");
    }
    
    // To Fill the Combo boxes in the UI
    private void fillDropDown() throws Exception {
        //        HashMap where = new HashMap();
        //        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        //        List lst = (List)ClientUtil.executeQuery("getAgentIdName", where);
        //        System.out.println("########ListForAgent : "+lst);
        //        getMap(lst);
        //        setCbmAgentType(new ComboBoxModel(key,value));
        //
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("INWARD.BOUNCING_REASON");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("INWARD.BOUNCING_REASON"));
        setBouncingReasons(value.toArray());
        
    }
    
    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        
        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
        //        keyValue = new HashMap();
        //        keyValue.put("KEY", key);
        //        keyValue.put("VALUE", value);
        //        return keyValue;
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        try {            
            HashMap mapData = proxy.executeQuery(whereMap, operationMap); //, frame);
            List lstData = ((List)mapData.get("CelaringDataTO"));
            HashMap ecsMap = new HashMap();
            if(lstData!=null && lstData.size()>0){
                ClearingDataImportTO obj = (ClearingDataImportTO)lstData.get(0);
                if(obj.getTransCode()!=null && CommonUtil.convertObjToDouble(obj.getTransCode()).doubleValue() == 11){
                    setLblTransactionTypeValue("DEBIT");
                    setRdoUploadingFile(true);
                    setRdoDownLoadingFile(false);
                }else{
                    setLblTransactionTypeValue("CREDIT");
                    setRdoDownLoadingFile(true);
                    setRdoUploadingFile(false);
                }
                setLblUserNumberValue(CommonUtil.convertObjToStr(obj.getUserNumber()));
                setLblUserNameValue(CommonUtil.convertObjToStr(obj.getUserName()));
                setLblUserCreditRefNoValue(CommonUtil.convertObjToStr(obj.getUserCreditRef()));
                setLblECSTapeInputNoValue(CommonUtil.convertObjToStr(obj.getTapeInputNo()));
                setLblSponsorValue(CommonUtil.convertObjToStr(obj.getSponsor()));
                setLblUserBankAcNoValue(CommonUtil.convertObjToStr(obj.getUsersAcNo()));
                setLblLedgerFolioNoValue(CommonUtil.convertObjToStr(obj.getLedgerNo()));
                setLblUserLimitValue(CommonUtil.convertObjToStr(obj.getUserLimit()));
                setLblTotalAmountValue(CommonUtil.convertObjToStr(obj.getTotalAmt()));
                setLblSettlementDateValue(CommonUtil.convertObjToStr(obj.getSetellementDt()));
                setLblECSItemNoValue(CommonUtil.convertObjToStr(obj.getSequenceNo()));
                setLblCheckSumValue(CommonUtil.convertObjToStr(obj.getCheckSum()));                
                setLblFilterValue(CommonUtil.convertObjToStr(obj.getFilter()));
                
//                setLblTransactionTypeValue(CommonUtil.convertObjToStr(ecsMap.get("TRANS_CODE")));
//                setLblUserNumberValue(CommonUtil.convertObjToStr(ecsMap.get("USER_NUMBER")));
//                setLblUserNameValue(CommonUtil.convertObjToStr(ecsMap.get("USER_NAME")));
//                setLblUserCreditRefNoValue(CommonUtil.convertObjToStr(ecsMap.get("USER_CREDIT_REF")));
//                setLblECSTapeInputNoValue(CommonUtil.convertObjToStr(ecsMap.get("TAPE_INPUT_NO")));
//                setLblSponsorValue(CommonUtil.convertObjToStr(ecsMap.get("SPONSOR")));
//                setLblUserBankAcNoValue(CommonUtil.convertObjToStr(ecsMap.get("USERS_AC_NO")));
//                setLblLedgerFolioNoValue(CommonUtil.convertObjToStr(ecsMap.get("LEDGER_NO")));
//                setLblUserLimitValue(CommonUtil.convertObjToStr(ecsMap.get("USER_LIMIT")));
//                setLblTotalAmountValue(CommonUtil.convertObjToStr(ecsMap.get("TOTAL_AMT")));
//                setLblSettlementDateValue(CommonUtil.convertObjToStr(ecsMap.get("SETLLEMENT_DT")));
//                setLblECSItemNoValue(CommonUtil.convertObjToStr(ecsMap.get("SEQUENCE_NO")));
//                setLblCheckSumValue(CommonUtil.convertObjToStr(ecsMap.get("CHECK_SUM")));                
//                setLblFilterValue(CommonUtil.convertObjToStr(ecsMap.get("FILTER")));
            }

            lstData = ((List)mapData.get("CelaringDataDebitTO"));
            ecsMap = new HashMap();
            ArrayList row;
            ArrayList rows = new ArrayList();
            int size = lstData.size();
            if(lstData!=null && lstData.size()>0){
                for(int i=0;i<lstData.size();i++){
                    ClearingDataImportDebitTO obj = (ClearingDataImportDebitTO)lstData.get(i);
                    row = populatesetRow(obj);
                    tbmClearingData.insertRow(tbmClearingData.getRowCount(), row);
                    rows.add(row);
                }
                setTable();
                tbmClearingData.setData(rows);
                tbmClearingData.fireTableDataChanged();                
            }
//            lstData = ClientUtil.executeQuery("getClearingImportDataCreditTO", whereMap);
//            lstData = ClientUtil.executeQuery("getClearingDataImportDebitTOResult", whereMap);
//            System.out.println("### mapData.... : "+lstData);
//            System.out.println("### wheremap.... : "+whereMap);
//            setLblTransactionId(CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
//            Date coll_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("COLL_DT")));
//            //            coll_dt=new Date(whereMap.get("COLL_DT")));
//            setTdtInstrumentDate(coll_dt);
//            setCboAgentType(CommonUtil.convertObjToStr(whereMap.get("AGENT_ID")));
//            setLblInitiatorId(CommonUtil.convertObjToStr(whereMap.get("STATUS_BY")));
//            
//            setTotalGlAmt(CommonUtil.convertObjToDouble(whereMap.get("AMOUNT")).doubleValue());
//            setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("COLL_DT"))));
                    ttNotifyObservers();

//            populateOB(lstData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }
    
    private void populateOB(List lstData) throws Exception{
        log.info("In populateOB()");
        //Taking the Value of Transaction Id from each Table...
        // Here the first Row is selected...
        //        setDenominationList((ArrayList) lstData.get("DENOMINATION_LIST"));
        ttNotifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    // To perform Appropriate operation... Insert, Update, Delete...
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        //        columnHeader.add("TransCode");
        columnHeader.add("Destination AcNo");
        columnHeader.add("Destination AcHolders Name");
        columnHeader.add("TotalAmount");
        columnHeader.add("UserName");
        columnHeader.add("Post or Bounce");
        ArrayList data = new ArrayList();
        tbmClearingData = new TableModel(data,columnHeader);
    }
    
    private ArrayList setRow(HashMap obj){
        ArrayList row = new ArrayList();
        //        row.add(obj.get("TRANS_CODE"));
        row.add(obj.get("DESTINATION_AC_NO"));
        row.add(obj.get("DESTINATION_AC_HOLDERS_NAME"));
        row.add(String.valueOf(CommonUtil.convertObjToDouble(obj.get("TOTAL_AMT")).doubleValue()));
        row.add(obj.get("USER_NAME"));
        row.add(" ");
        return row;
    }
    
    private ArrayList setUpdateRow(int selectedRow,String values){
        ArrayList row = new ArrayList();
        row.remove(selectedRow);
        row.set(selectedRow,values);
        return row;
    }
    
    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    //                    doActionPerform("");
                }
            } else {
                log.info("Action Type Not Defined In setDailyDepositTrans)");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        
        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                //(list.get(0) instanceof String && "IB".equalsIgnoreCase((String)list.get(0))) ||
                if(list.size()==1 && list.get(0) instanceof String && ((String)list.get(0)).startsWith("SUSPECIOUS")) {
                    Object[] dialogOption = {"Exception","Cancel"};
                    parseException.setDialogOptions(dialogOption);
                    if(parseException.logException(exception, true)==0) {
                        try{
                            setResult(actionType);
                            //                            doActionPerform("EXCEPTION");
                        } catch(Exception e1) {
                            log.info("Error In doAction()");
                            e1.printStackTrace();
                            if(e1 instanceof TTException) {
                                Object[] dialogOption1 = {"OK"};
                                parseException.setDialogOptions(dialogOption1);
                                exception = (TTException) e1;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else {
                    parseException.logException(exception, true);
                }
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(actionType);
            }
        }
    }
    
    public void resetDepSubNo() {
        setTxtAccNo("");
        setTxtAmount("");
        setTxtInitiatorChannel("");
        setLblAccName("");
        setBalance("");
        setAccountName("");
    }
    /** To perform the necessary action */
    private void doActionPerform(LinkedHashMap dataMap) throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        TTException exception = null;
        try {
            if(getAuthorizeMap()!=null) {
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                _authorizeMap.put("DAILY","DAILY");
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                System.out.println("recordData^^^^^^^^^^^$$$"+recordData);
                data.put("DAILYDEPOSITTRANSTO", recordData);
                data.put("BATCH_ID",getLblTransactionId());
                proxy.execute(data,operationMap);
                //                if(parameter!=null && "EXCEPTION".equalsIgnoreCase(parameter))
                data.put("EXCEPTION","EXCEPTION");
                System.out.println("Excpetion"+data);
            } else {
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                data.put("TOTALRECORDS",dataMap);
                System.out.println("data :"+data);
                HashMap proxyReturnMap = proxy.execute(data,operationMap);
            }
            setResult(getOperation());
            operation = ClientConstants.ACTIONTYPE_CANCEL;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                parseException.logException(exception, true);
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(getOperation());
            }
        }
        
        resetForm();
    }
    
    public void resetOBFields(){
        this.setCboAgentType("");
        this.tbmClearingData.setData(new ArrayList());
        this.tbmClearingData.fireTableDataChanged();
        //        this.recordData.clear();
        //        this.deleteData.clear();
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
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
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        oldAmount = 0.0;
        _authorizeMap = null;
        resetDepSubNoTbl();
        setTxtAccNo("");
        setTxtAmount("");
        //        setTxtAccHd("");
        setCboAgentType("");
        setBalance("");
        setTxtInitiatorChannel("");
        setDenominationList(null);
        setLblTransDate(DateUtil.getStringDate(ClientUtil.getCurrentDate()));
        ttNotifyObservers();
    }
    
    public void resetDepSubNoTbl(){
        this.tbmClearingData.setData(new ArrayList());
        this.tbmClearingData.fireTableDataChanged();
//        tbmClearingData.setDataArrayList(null,null);
    }
    
    public void resetTransactionDetails() {
        log.info("In resetForm()");
        oldAmount = 0.0;
        _authorizeMap = null;
        setTxtAccNo("");
        setTxtInitiatorChannel("");
        setRdoTransactionType_Credit(false);
        setTxtAmount("");
        //        setTxtAccHd("");
        setCboAgentType("");
        setBalance("");
        setTxtInitiatorChannel("");
        setDenominationList(null);
        ttNotifyObservers();
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        //        this.setTxtAccHd("");
        //        this.setCboAgentType("");
        this.setLblAccName("");
        //        this.setLblTransactionId("");
        //        this.setLblTransDate("");
        this.setBalance("");
        //        this.setLblInitiatorId("");
        //        this.setLblTransactionId("");
        //        this.setLblTransDate("");
        //        this.setLblInitiatorId("");
        
        setLblTransactionTypeValue("");
        setLblUserNumberValue("");
        setLblUserNameValue("");
        setLblUserCreditRefNoValue("");
        setLblECSTapeInputNoValue("");
        setLblSponsorValue("");
        setLblUserBankAcNoValue("");
        setLblLedgerFolioNoValue("");
        setLblUserLimitValue("");
        setLblTotalAmountValue("");
        setLblSettlementDateValue("");
        setLblECSItemNoValue("");
        setLblCheckSumValue("");
        setLblFilterValue("");
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
    
    public void setInitiatorChannelValue(){
        setTxtInitiatorChannel(INITIATORTYPE);
        ttNotifyObservers();
    }
    
    public void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    public String getTxtAccNo(){
        return this.txtAccNo;
    }
    public void setTxtInitiatorChannel(String txtInitiatorChannel){
        this.txtInitiatorChannel = txtInitiatorChannel;
        setChanged();
    }
    public String getTxtInitiatorChannel(){
        return this.txtInitiatorChannel;
    }
    
    
    void setRdoTransactionType_Credit(boolean rdoTransactionType_Credit){
        this.rdoTransactionType_Credit = rdoTransactionType_Credit;
        setChanged();
    }
    boolean getRdoTransactionType_Credit(){
        return this.rdoTransactionType_Credit;
    }
    
    
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        setChanged();
    }
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    
    //To Set the Lables In the UI...
    //==========================================
    public void setTxtAccHd(String txtAccHdId){
        this.txtAccHdId = txtAccHdId;
        setChanged();
    }
    public String getTxtAccHd(){
        return this.txtAccHdId;
    }
    
    
    public void setAccountHead() {
        //        try {
        //        HashMap accHead = new HashMap();
        //        accHead.put("PROD_ID", CommonUtil.convertObjToStr("PROD_ID"));
        //        List lst =
        //
        //        }catch(Exception e){
        //        }
    }
    // For setting the Name of the Account Number Holder...
    public void setLblAccName(String lblAccName){
        this.lblAccName = lblAccName;
        setChanged();
    }
    public String getLblAccName(){
        return this.lblAccName;
    }
    
    private ArrayList populatesetRow(ClearingDataImportDebitTO obj){
        ArrayList row = new ArrayList();
        //        row.add(obj.get("TRANS_CODE"));
//                txtSalaryStructureTotNoIncValue.setValidation(new NumericValidation(16));

        row.add(CommonUtil.convertObjToStr(obj.getDestinationACNo()));
        row.add(String.valueOf(obj.getDestinationACName()));
        row.add(String.valueOf(obj.getTotalAmt()));
        row.add(String.valueOf(obj.getUserName()));
        row.add(" ");
        return row;
    }
    
    public void setAccountName(String AccountNo){
        try {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM",AccountNo);
            System.out.println("@#@#v accountNameMap : " +accountNameMap);
            final List resultList = ClientUtil.executeQuery("getAccountNumberForDeposit",accountNameMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            System.out.println("resultMap : " +resultMap);
            setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
            setBalance(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
            //            setTxtAccNo(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
            //            setTxtAccNo(AccountNo);
            setTxtAccNo(AccountNo);
            System.out.println("setTxtAccNo : " +AccountNo);
            System.out.println("$#@ accountNameMap : " +accountNameMap);
            System.out.println("########AMOUNT" + resultMap.get("AMOUNT"));
            
        }catch(Exception e){
            
        }
    }
    
    private void getKeyValue(HashMap keyValue){
        log.info("In getKeyValue()");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void updatingBounceValues(int selectedRow,String values){
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_NAME,"InwardClearing.getBounceClearingType");
        where.put(CommonConstants.PARAMFORQUERY, ProxyParameters.BRANCH_ID);
        keyValue = ClientUtil.populateLookupData(where);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        setBouncingClearingType(value.toArray());
        String message = (String) COptionPane.showInputDialog(null, "Enter the Clearing Type For the Bounce: ", "Bouncing Reason", COptionPane.PLAIN_MESSAGE, null, getBouncingReasons(), "");
        if(getBouncingClearingType()!= null && getBouncingClearingType().length > 0){
            String bClearingType = (String) COptionPane.showInputDialog(null, "Enter the Clearing Type For the Bounce: ", "Bounce Clearing Type:",
            COptionPane.PLAIN_MESSAGE, null, getBouncingClearingType(), getBouncingClearingType()[0]);
        }
        tbmClearingData.setValueAt(values,selectedRow,4);
        tbmClearingData.fireTableDataChanged();
    }
    
    //__ To set the Bouncing ClearingType...
    public Object[] getBouncingClearingType() {
        return objBouncingReasons;
    }
    
    public void setBouncingClearingType(Object[] objBouncingReasons) {
        this.objBouncingReasons = objBouncingReasons;
    }
    
    public Object[] getBouncingReasons() {
        return objReasons;
    }
    
    public void setBouncingReasons(Object[] objReason) {
        this.objReasons = objReason;
    }
    
    // For setting the transaction Id in UI at the Time of Edit or Delete...
    public void setLblTransactionId(String lblTransactionId){
        this.lblTransactionId = lblTransactionId;
        setChanged();
    }
    public String getLblTransactionId(){
        return this.lblTransactionId;
    }
    
    // For setting the Name of the Clearing Date in Ui at the time of Edit and Delete...
    public void setLblTransDate(String lblTransDate){
        this.lblTransDate = lblTransDate;
        setChanged();
    }
    public String getLblTransDate(){
        return this.lblTransDate;
    }
    
    // For setting the Initiator Id in UI at the Time of Edit or Delete...
    public void setLblInitiatorId(String lblInitiatorId){
        this.lblInitiatorId = lblInitiatorId;
        setChanged();
    }
    public String getLblInitiatorId(){
        return this.lblInitiatorId;
    }
    
    /**
     * Getter for property cr_cash.
     * @return Value of property cr_cash.
     */
    public java.lang.String getCr_cash() {
        return cr_cash;
    }
    
    /**
     * Setter for property cr_cash.
     * @param cr_cash New value of property cr_cash.
     */
    public void setCr_cash(java.lang.String cr_cash) {
        this.cr_cash = cr_cash;
    }
    
    /**
     * Getter for property dr_cash.
     * @return Value of property dr_cash.
     */
    public java.lang.String getDr_cash() {
        return dr_cash;
    }
    
    /**
     * Setter for property dr_cash.
     * @param dr_cash New value of property dr_cash.
     */
    public void setDr_cash(java.lang.String dr_cash) {
        this.dr_cash = dr_cash;
    }
    
    /**
     * Getter for property denominationList.
     * @return Value of property denominationList.
     */
    public java.util.ArrayList getDenominationList() {
        return denominationList;
    }
    
    /**
     * Setter for property denominationList.
     * @param denominationList New value of property denominationList.
     */
    public void setDenominationList(java.util.ArrayList denominationList) {
        this.denominationList = denominationList;
    }
    
    /**
     * Getter for property balance.
     * @return Value of property balance.
     */
    public java.lang.String getBalance() {
        return balance;
    }
    
    /**
     * Setter for property balance.
     * @param balance New value of property balance.
     */
    public void setBalance(java.lang.String balance) {
        this.balance = balance;
    }
    
    /**
     * Getter for property cbmAgentType.
     * @return Value of property cbmAgentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentType() {
        return cbmAgentType;
    }
    
    /**
     * Setter for property cbmAgentType.
     * @param cbmAgentType New value of property cbmAgentType.
     */
    public void setCbmAgentType(com.see.truetransact.clientutil.ComboBoxModel cbmAgentType) {
        this.cbmAgentType = cbmAgentType;
    }
    
    /**
     * Getter for property depSubNoStatus.
     * @return Value of property depSubNoStatus.
     */
    public java.lang.String getDepSubNoStatus() {
        return depSubNoStatus;
    }
    
    /**
     * Setter for property depSubNoStatus.
     * @param depSubNoStatus New value of property depSubNoStatus.
     */
    public void setDepSubNoStatus(java.lang.String depSubNoStatus) {
        this.depSubNoStatus = depSubNoStatus;
    }
    
    /**
     * Getter for property cboAgentType.
     * @return Value of property cboAgentType.
     */
    public java.lang.String getCboAgentType() {
        return cboAgentType;
    }
    
    /**
     * Setter for property cboAgentType.
     * @param cboAgentType New value of property cboAgentType.
     */
    public void setCboAgentType(java.lang.String cboAgentType) {
        this.cboAgentType = cboAgentType;
        setChanged();
    }
    
    /**
     * Getter for property depDailybNoK.
     * @return Value of property depDailybNoK.
     */
    public java.lang.String getDepDailybNoK() {
        return depDailybNoK;
    }
    
    /**
     * Setter for property depDailybNoK.
     * @param depDailybNoK New value of property depDailybNoK.
     */
    public void setDepDailybNoK(java.lang.String depDailybNoK) {
        this.depDailybNoK = depDailybNoK;
    }
    
    /**
     * Getter for property depSubNoMode.
     * @return Value of property depSubNoMode.
     */
    public java.lang.String getDepSubNoMode() {
        return depSubNoMode;
    }
    
    /**
     * Setter for property depSubNoMode.
     * @param depSubNoMode New value of property depSubNoMode.
     */
    public void setDepSubNoMode(java.lang.String depSubNoMode) {
        this.depSubNoMode = depSubNoMode;
    }
    //
    //    /**
    //     * Getter for property tbmTransfer.
    //     * @return Value of property tbmTransfer.
    //     */
    //    public com.see.truetransact.clientutil.TableModel getTbmTransfer() {
    //        return tbmTransfer;
    //    }
    //
    //    /**
    //     * Setter for property tbmTransfer.
    //     * @param tbmTransfer New value of property tbmTransfer.
    //     */
    //    public void setTbmTransfer(com.see.truetransact.clientutil.TableModel tbmTransfer) {
    //        this.tbmTransfer = tbmTransfer;
    //        setChanged();
    //    }
    
    /**
     * Getter for property recordData.
     * @return Value of property recordData.
     */
    public java.util.ArrayList getRecordData() {
        return recordData;
    }
    //
    //    /**
    //     * Setter for property recordData.
    //     * @param recordData New value of property recordData.
    //     */
    //    public void setRecordData(java.util.ArrayList recordData) {
    //        this.recordData = recordData;
    //    }
    //
    //    /**
    //     * Getter for property .
    //     * @return Value of property record.
    //     */
    //    public java.util.LinkedHashMap getRecord() {
    //        return record;
    //    }
    
    /**
     * Setter for property record.
     * @param record New value of property record.
     */
    //    public void setRecord(java.util.LinkedHashMap record) {
    //        this.record = record;
    //    }
    
    
    /**
     * Getter for property deleteData.
     * @return Value of property deleteData.
     */
    public java.util.ArrayList getDeleteData() {
        return deleteData;
    }
    
    /**
     * Setter for property deleteData.
     * @param deleteData New value of property deleteData.
     */
    public void setDeleteData(java.util.ArrayList deleteData) {
        this.deleteData = deleteData;
    }
    
    /**
     * Getter for property prodId.
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }
    
    /**
     * Setter for property prodId.
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }
    
    /**
     * Getter for property prod_desc.
     * @return Value of property prod_desc.
     */
    public java.lang.String getProd_desc() {
        return prod_desc;
    }
    
    /**
     * Setter for property prod_desc.
     * @param prod_desc New value of property prod_desc.
     */
    public void setProd_desc(java.lang.String prod_desc) {
        this.prod_desc = prod_desc;
    }
    
    /**
     * Getter for property transStatus.
     * @return Value of property transStatus.
     */
    public java.lang.String getTransStatus() {
        return transStatus;
    }
    
    /**
     * Setter for property transStatus.
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus(java.lang.String transStatus) {
        this.transStatus = transStatus;
    }
    
    /**
     * Getter for property operation.
     * @return Value of property operation.
     */
    public int getOperation() {
        return operation;
    }
    
    /**
     * Setter for property operation.
     * @param operation New value of property operation.
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }
    
    /**
     * Getter for property oldAmount.
     * @return Value of property oldAmount.
     */
    public double getOldAmount() {
        return oldAmount;
    }
    
    /**
     * Setter for property oldAmount.
     * @param oldAmount New value of property oldAmount.
     */
    public void setOldAmount(double oldAmount) {
        this.oldAmount = oldAmount;
    }
    
    /**
     * Getter for property tdtInstrumentDate.
     * @return Value of property tdtInstrumentDate.
     */
    public java.util.Date getTdtInstrumentDate() {
        return tdtInstrumentDate;
    }
    
    /**
     * Setter for property tdtInstrumentDate.
     * @param tdtInstrumentDate New value of property tdtInstrumentDate.
     */
    public void setTdtInstrumentDate(java.util.Date tdtInstrumentDate) {
        this.tdtInstrumentDate = tdtInstrumentDate;
    }
    
    /**
     * Getter for property totalAmt.
     * @return Value of property totalAmt.
     */
    public double getTotalAmt() {
        return totalAmt;
    }
    
    /**
     * Setter for property totalAmt.
     * @param totalAmt New value of prop
     * erty totalAmt.
     */
    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }
    
    public void overallRecords(LinkedHashMap dataMap){
        System.out.println("HashMap dataMap :"+dataMap);
        try {
            String parameter = "";
            doActionPerform(dataMap);
            ArrayList row;
            ArrayList rows = new ArrayList();
            int size = this.dataMap.size();
            for(int i=0;i<dataMap.size();i++){
                HashMap records = new HashMap();
                if(i!=0){
                    records = (HashMap)dataMap.get(CommonUtil.convertObjToStr(dataMap.keySet().toArray()[i]));
                    row = setRow(records);
                    tbmClearingData.insertRow(tbmClearingData.getRowCount(), row);
                    rows.add(row);
                }
            }
            setTable();
            tbmClearingData.setData(rows);
            tbmClearingData.fireTableDataChanged();
        }catch(Exception e){
            
        }
    }
    
    public void updatingPostValues(int selectedRow,String values){
        tbmClearingData.setValueAt(values,selectedRow,4);
        tbmClearingData.fireTableDataChanged();
    }
    /**
     * Getter for property totalGlAmt.
     * @return Value of property totalGlAmt.
     */
    public double getTotalGlAmt() {
        return totalGlAmt;
    }
    
    /**
     * Setter for property totalGlAmt.
     * @param totalGlAmt New value of property totalGlAmt.
     */
    public void setTotalGlAmt(double totalGlAmt) {
        this.totalGlAmt = totalGlAmt;
    }
    public void resetMainPan(){
        //        setCboAgentType("");
        setLblTransDate("");
        setLblInitiatorId("");
        setLblTransactionId("");
        //        setTotalGlAmt(new Double(null).doubleValue());
        ttNotifyObservers();
        //        setTotalGlAmt(CommonUtil.convertObjToDouble(new Double(null)).doubleValue());
    }
    
    /**
     * Getter for property tbmClearingData.
     * @return Value of property tbmClearingData.
     */
    public com.see.truetransact.clientutil.TableModel getTbmClearingData() {
        return tbmClearingData;
    }
    
    /**
     * Setter for property tbmClearingData.
     * @param tbmClearingData New value of property tbmClearingData.
     */
    public void setTbmClearingData(com.see.truetransact.clientutil.TableModel tbmClearingData) {
        this.tbmClearingData = tbmClearingData;
    }
    
    /**
     * Getter for property lblTransactionTypeValue.
     * @return Value of property lblTransactionTypeValue.
     */
    public java.lang.String getLblTransactionTypeValue() {
        return lblTransactionTypeValue;
    }
    
    /**
     * Setter for property lblTransactionTypeValue.
     * @param lblTransactionTypeValue New value of property lblTransactionTypeValue.
     */
    public void setLblTransactionTypeValue(java.lang.String lblTransactionTypeValue) {
        this.lblTransactionTypeValue = lblTransactionTypeValue;
    }
    
    /**
     * Getter for property lblUserNumberValue.
     * @return Value of property lblUserNumberValue.
     */
    public java.lang.String getLblUserNumberValue() {
        return lblUserNumberValue;
    }
    
    /**
     * Setter for property lblUserNumberValue.
     * @param lblUserNumberValue New value of property lblUserNumberValue.
     */
    public void setLblUserNumberValue(java.lang.String lblUserNumberValue) {
        this.lblUserNumberValue = lblUserNumberValue;
    }
    
    /**
     * Getter for property lblUserNameValue.
     * @return Value of property lblUserNameValue.
     */
    public java.lang.String getLblUserNameValue() {
        return lblUserNameValue;
    }
    
    /**
     * Setter for property lblUserNameValue.
     * @param lblUserNameValue New value of property lblUserNameValue.
     */
    public void setLblUserNameValue(java.lang.String lblUserNameValue) {
        this.lblUserNameValue = lblUserNameValue;
    }
    
    /**
     * Getter for property lblUserCreditRefNoValue.
     * @return Value of property lblUserCreditRefNoValue.
     */
    public java.lang.String getLblUserCreditRefNoValue() {
        return lblUserCreditRefNoValue;
    }
    
    /**
     * Setter for property lblUserCreditRefNoValue.
     * @param lblUserCreditRefNoValue New value of property lblUserCreditRefNoValue.
     */
    public void setLblUserCreditRefNoValue(java.lang.String lblUserCreditRefNoValue) {
        this.lblUserCreditRefNoValue = lblUserCreditRefNoValue;
    }
    
    /**
     * Getter for property lblECSTapeInputNoValue.
     * @return Value of property lblECSTapeInputNoValue.
     */
    public java.lang.String getLblECSTapeInputNoValue() {
        return lblECSTapeInputNoValue;
    }
    
    /**
     * Setter for property lblECSTapeInputNoValue.
     * @param lblECSTapeInputNoValue New value of property lblECSTapeInputNoValue.
     */
    public void setLblECSTapeInputNoValue(java.lang.String lblECSTapeInputNoValue) {
        this.lblECSTapeInputNoValue = lblECSTapeInputNoValue;
    }
    
    /**
     * Getter for property lblSponsorValue.
     * @return Value of property lblSponsorValue.
     */
    public java.lang.String getLblSponsorValue() {
        return lblSponsorValue;
    }
    
    /**
     * Setter for property lblSponsorValue.
     * @param lblSponsorValue New value of property lblSponsorValue.
     */
    public void setLblSponsorValue(java.lang.String lblSponsorValue) {
        this.lblSponsorValue = lblSponsorValue;
    }
    
    /**
     * Getter for property lblUserBankAcNoValue.
     * @return Value of property lblUserBankAcNoValue.
     */
    public java.lang.String getLblUserBankAcNoValue() {
        return lblUserBankAcNoValue;
    }
    
    /**
     * Setter for property lblUserBankAcNoValue.
     * @param lblUserBankAcNoValue New value of property lblUserBankAcNoValue.
     */
    public void setLblUserBankAcNoValue(java.lang.String lblUserBankAcNoValue) {
        this.lblUserBankAcNoValue = lblUserBankAcNoValue;
    }
    
    /**
     * Getter for property lblLedgerFolioNoValue.
     * @return Value of property lblLedgerFolioNoValue.
     */
    public java.lang.String getLblLedgerFolioNoValue() {
        return lblLedgerFolioNoValue;
    }
    
    /**
     * Setter for property lblLedgerFolioNoValue.
     * @param lblLedgerFolioNoValue New value of property lblLedgerFolioNoValue.
     */
    public void setLblLedgerFolioNoValue(java.lang.String lblLedgerFolioNoValue) {
        this.lblLedgerFolioNoValue = lblLedgerFolioNoValue;
    }
    
    /**
     * Getter for property lblUserLimitValue.
     * @return Value of property lblUserLimitValue.
     */
    public java.lang.String getLblUserLimitValue() {
        return lblUserLimitValue;
    }
    
    /**
     * Setter for property lblUserLimitValue.
     * @param lblUserLimitValue New value of property lblUserLimitValue.
     */
    public void setLblUserLimitValue(java.lang.String lblUserLimitValue) {
        this.lblUserLimitValue = lblUserLimitValue;
    }
    
    /**
     * Getter for property lblTotalAmountValue.
     * @return Value of property lblTotalAmountValue.
     */
    public java.lang.String getLblTotalAmountValue() {
        return lblTotalAmountValue;
    }
    
    /**
     * Setter for property lblTotalAmountValue.
     * @param lblTotalAmountValue New value of property lblTotalAmountValue.
     */
    public void setLblTotalAmountValue(java.lang.String lblTotalAmountValue) {
        this.lblTotalAmountValue = lblTotalAmountValue;
    }
    
    /**
     * Getter for property lblSettlementDateValue.
     * @return Value of property lblSettlementDateValue.
     */
    public java.lang.String getLblSettlementDateValue() {
        return lblSettlementDateValue;
    }
    
    /**
     * Setter for property lblSettlementDateValue.
     * @param lblSettlementDateValue New value of property lblSettlementDateValue.
     */
    public void setLblSettlementDateValue(java.lang.String lblSettlementDateValue) {
        this.lblSettlementDateValue = lblSettlementDateValue;
    }
    
    /**
     * Getter for property lblECSItemNoValue.
     * @return Value of property lblECSItemNoValue.
     */
    public java.lang.String getLblECSItemNoValue() {
        return lblECSItemNoValue;
    }
    
    /**
     * Setter for property lblECSItemNoValue.
     * @param lblECSItemNoValue New value of property lblECSItemNoValue.
     */
    public void setLblECSItemNoValue(java.lang.String lblECSItemNoValue) {
        this.lblECSItemNoValue = lblECSItemNoValue;
    }
    
    /**
     * Getter for property lblCheckSumValue.
     * @return Value of property lblCheckSumValue.
     */
    public java.lang.String getLblCheckSumValue() {
        return lblCheckSumValue;
    }
    
    /**
     * Setter for property lblCheckSumValue.
     * @param lblCheckSumValue New value of property lblCheckSumValue.
     */
    public void setLblCheckSumValue(java.lang.String lblCheckSumValue) {
        this.lblCheckSumValue = lblCheckSumValue;
    }
    
    /**
     * Getter for property lblFilterValue.
     * @return Value of property lblFilterValue.
     */
    public java.lang.String getLblFilterValue() {
        return lblFilterValue;
    }
    
    /**
     * Setter for property lblFilterValue.
     * @param lblFilterValue New value of property lblFilterValue.
     */
    public void setLblFilterValue(java.lang.String lblFilterValue) {
        this.lblFilterValue = lblFilterValue;
    }
    
    /**
     * Getter for property rdoDownLoadingFile.
     * @return Value of property rdoDownLoadingFile.
     */
    public boolean getRdoDownLoadingFile() {
        return rdoDownLoadingFile;
    }
    
    /**
     * Setter for property rdoDownLoadingFile.
     * @param rdoDownLoadingFile New value of property rdoDownLoadingFile.
     */
    public void setRdoDownLoadingFile(boolean rdoDownLoadingFile) {
        this.rdoDownLoadingFile = rdoDownLoadingFile;
    }
    
    /**
     * Getter for property rdoUploadingFile.
     * @return Value of property rdoUploadingFile.
     */
    public boolean getRdoUploadingFile() {
        return rdoUploadingFile;
    }
    
    /**
     * Setter for property rdoUploadingFile.
     * @param rdoUploadingFile New value of property rdoUploadingFile.
     */
    public void setRdoUploadingFile(boolean rdoUploadingFile) {
        this.rdoUploadingFile = rdoUploadingFile;
    }
    
}
