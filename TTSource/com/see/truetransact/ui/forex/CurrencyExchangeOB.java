/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CurrencyExchangeOB.java
 *
 * Created on January 12, 2004, 1:08 PM
 */

package com.see.truetransact.ui.forex;

import java.util.Observable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.forex.CurrencyExchangeTO;
import com.see.truetransact.commonutil.xml.*;

/**
 *
 * @author  bala
 */
public class CurrencyExchangeOB extends Observable {
    private HashMap map = null;
    private ProxyFactory proxy = null;
    private int _actionType;
    private int _result;
    private final int FAILED = 4;
    
    private final String TR_TYPE_DEPOSIT = "Deposit";
    private final String TR_TYPE_WITHDRAWAL = "Withdrawal";
    private final String TR_ROOT = "Transaction";
    private final String TR_TYPE = "Type";
    private final String TR_CURRENCY = "TransCurrency";
    private final String TR_AMOUNT = "TransAmount"; 
    private final String TR_TO_BRANCH = "ToBranch";
    private final String TR_FROM_BRANCH = "FromBranch";
    private final String TR_ACC_NO = "AcctNo";
    private final String TR_VALUE_DATE = "ValueDate";
    private final String TR_TOTAL_AMOUNT = "TotalAmount";
    private final String TR_CONV_CURRENCY= "ConvCurrency";
    private final String TR_CUST_TYPE = "CustType";
    private final String TR_EXCHANGE_RATE = "ExchangeRate";
    private final String TR_REMARKS = "Remarks";
    private final String TR_ID = "TransId";
    private Date curDate = null;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private ComboBoxModel cbmTransCurrency;
    private ComboBoxModel cbmConvCurrency;
    private ComboBoxModel cbmCustType;
    
    /** Creates a new instance of CurrencyExchangeOB */
    public CurrencyExchangeOB() {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "CurrencyExchangeJNDI");
        map.put(CommonConstants.HOME, "forex.CurrencyExchangeHome");
        map.put(CommonConstants.REMOTE, "forex.CurrencyExchange");

        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        populateCombo();
    }
    
    private void populateCombo() {
        HashMap param = new HashMap();
        ArrayList lookupKey = new ArrayList();
        HashMap lookupValues;
        HashMap keyValue;
        ArrayList key;
        ArrayList value;
        
        System.out.println("Inside populate Combo.");
         //for multiple lookup
        param.put(CommonConstants.MAP_NAME,null);
        
        lookupKey.add("FOREX.CURRENCY");
        lookupKey.add("FOREX.CUSTTYPE");

        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);

        keyValue = (HashMap)lookupValues.get("FOREX.CURRENCY");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        cbmTransCurrency = new ComboBoxModel(key,value);
        cbmConvCurrency = new ComboBoxModel(key,value);
        
        keyValue = (HashMap)lookupValues.get("FOREX.CUSTTYPE");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        cbmCustType = new ComboBoxModel(key,value);
    }
    
    public ComboBoxModel getCbmCustType () {
        return cbmCustType;
    }
    
    public ComboBoxModel getCbmTransCurrency () {
        return cbmTransCurrency;
    }
    
    public ComboBoxModel getCbmConvCurrency () {
        return cbmConvCurrency;
    }
    
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            
            CurrencyExchangeTO objCurrencyExchangeTO = 
                (CurrencyExchangeTO) ((List) mapData.get("CurrencyExchangeTO")).get(0);
            setCurrencyExchangeTO (objCurrencyExchangeTO);
        } catch( Exception e ) {
            setResult(FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }        
    }
    
    public void execute (String command) {
        try {
            HashMap currExchange = new HashMap();
            currExchange.put("CurrencyExchangeTO", getCurrencyExchangeTO(command));
            
            HashMap proxyResultMap = proxy.execute(currExchange, map);
            
            setResult(getActionType());
        } catch (Exception e) {
            setResult(FAILED);
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    private CurrencyExchangeTO getCurrencyExchangeTO (String command) {
        CurrencyExchangeTO objCurrencyExchangeTO = new CurrencyExchangeTO();
        objCurrencyExchangeTO.setCommand (command);
        objCurrencyExchangeTO.setTransId(getTxtTransId());
        
        objCurrencyExchangeTO.setTransDt (DateUtil.getDateMMDDYYYY (""));
        objCurrencyExchangeTO.setAcctNo (getTxtAcctNo());
        
        if (getRdoTransType_Deposit() == true) {
            objCurrencyExchangeTO.setTransType ("Deposit");
        } else if (getRdoTransType_Withdrawal() == true) {
            objCurrencyExchangeTO.setTransType ("Withdrawal");
        }

        objCurrencyExchangeTO.setTransCurrency (getCboTransCurrency());
        objCurrencyExchangeTO.setTransAmount (new Double (getTxtTransAmount()));
        objCurrencyExchangeTO.setConvCurrency (getCboConvCurrency());
        
        Date ValDt = DateUtil.getDateMMDDYYYY (getTxtValueDate());
        if(ValDt != null){
        Date valDate = (Date)curDate.clone();
        valDate.setDate(ValDt.getDate());
        valDate.setMonth(ValDt.getMonth());
        valDate.setYear(ValDt.getYear());
//        objCurrencyExchangeTO.setValueDate (DateUtil.getDateMMDDYYYY (getTxtValueDate()));
        objCurrencyExchangeTO.setValueDate (valDate);
        }else{
            objCurrencyExchangeTO.setValueDate (DateUtil.getDateMMDDYYYY (getTxtValueDate()));
        }
        
        objCurrencyExchangeTO.setExchangeRate (new Double (getTxtExchangeRate()));
        objCurrencyExchangeTO.setCommission (new Double (getTxtCommission()));
        objCurrencyExchangeTO.setTotalAmount (new Double (getTxtTotalAmount()));
        objCurrencyExchangeTO.setRemarks (getTxtRemarks());
        
        objCurrencyExchangeTO.setTouristName (getTxtTouristName());
        objCurrencyExchangeTO.setPassportNo (getTxtTouristPassportNo());
        objCurrencyExchangeTO.setTouristNote (getTxtTouristRemarks());
        objCurrencyExchangeTO.setInstrumentNo (getTxtTouristInstrumentNo());
        
        Date TInsDt = DateUtil.getDateMMDDYYYY (getTdtTouristInstrumentDt());
        if(TInsDt != null){
        Date tinsDate = (Date)curDate.clone();
        tinsDate.setDate(TInsDt.getDate());
        tinsDate.setMonth(TInsDt.getMonth());
        tinsDate.setYear(TInsDt.getYear());
//        objCurrencyExchangeTO.setInstrumentDate (DateUtil.getDateMMDDYYYY (getTdtTouristInstrumentDt()));
        objCurrencyExchangeTO.setInstrumentDate (tinsDate);
        }else{
            objCurrencyExchangeTO.setInstrumentDate (DateUtil.getDateMMDDYYYY (getTdtTouristInstrumentDt()));
        }
        
        objCurrencyExchangeTO.setInstrumentDetails (getTxtTouristBankDetails());
        objCurrencyExchangeTO.setFromBranch(getTxtFromBranch());
        objCurrencyExchangeTO.setFromTransId(getTxtImpTrID());
        
        //objCurrencyExchangeTO.setCreatedBy (getTxtCreatedBy());
        //objCurrencyExchangeTO.setCreatedDt (DateUtil.getDateMMDDYYYY (getTxtCreatedDt()));
        //objCurrencyExchangeTO.setAuthorizedBy (getTxtAuthorizedBy());
        //objCurrencyExchangeTO.setAuthorizedDt (DateUtil.getDateMMDDYYYY (getTxtAuthorizedDt()));
        objCurrencyExchangeTO.setStatus (command);
        return objCurrencyExchangeTO;
    }
    
    private void setCurrencyExchangeTO(CurrencyExchangeTO objCurrencyExchangeTO) {
        try {
            setTxtTransId (objCurrencyExchangeTO.getTransId ());
            //setTxtTransDt (DateUtil.getStringDate (objCurrencyExchangeTO.getTransDt ()));
            setTxtAcctNo (objCurrencyExchangeTO.getAcctNo ());
            //setRdoTransType_Deposit((objCurrencyExchangeTO.getTransType ());

            if (objCurrencyExchangeTO.getTransType ().equals("Deposit")) {
                setRdoTransType_Deposit(true);
            } else if (objCurrencyExchangeTO.getTransType ().equals("Withdrawal")) {
                setRdoTransType_Withdrawal(true);
            }

            setCboTransCurrency (objCurrencyExchangeTO.getTransCurrency ());
            setTxtTransAmount (objCurrencyExchangeTO.getTransAmount ().toString());
            setCboConvCurrency (objCurrencyExchangeTO.getConvCurrency ());
            setTxtValueDate (DateUtil.getStringDate (objCurrencyExchangeTO.getValueDate ()));
            setTxtExchangeRate (objCurrencyExchangeTO.getExchangeRate ().toString());
            setTxtCommission (objCurrencyExchangeTO.getCommission ().toString());
            setTxtTotalAmount (objCurrencyExchangeTO.getTotalAmount ().toString());
            setTxtRemarks (objCurrencyExchangeTO.getRemarks ());
            
            setTxtTouristName (objCurrencyExchangeTO.getTouristName ());
            setTxtTouristPassportNo (objCurrencyExchangeTO.getPassportNo ());
            setTxtTouristRemarks (objCurrencyExchangeTO.getTouristNote ());
            setTxtTouristInstrumentNo (objCurrencyExchangeTO.getInstrumentNo ());
            setTdtTouristInstrumentDt (DateUtil.getStringDate (objCurrencyExchangeTO.getInstrumentDate ()));
            setTxtTouristBankDetails (objCurrencyExchangeTO.getInstrumentDetails ());
            setTxtFromBranch(objCurrencyExchangeTO.getFromBranch());
            setTxtImpTrID(objCurrencyExchangeTO.getFromTransId());
            
            //setTxtCreatedBy (objCurrencyExchangeTO.getCreatedBy ());
            //setTxtCreatedDt (DateUtil.getStringDate (objCurrencyExchangeTO.getCreatedDt ()));
            //setTxtAuthorizedBy (objCurrencyExchangeTO.getAuthorizedBy ());
            //setTxtAuthorizedDt (DateUtil.getStringDate (objCurrencyExchangeTO.getAuthorizedDt ()));
            //setTxtStatus (objCurrencyExchangeTO.getStatus ());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        notifyObservers();
    }
    
    public void resetOBFields() {
        rdoTransType_Deposit = false;
        rdoTransType_Withdrawal = false;
        cboTransCurrency = "";
        cboConvCurrency = "";
        txtValueDate = "";
        txtExchangeRate = "";
        txtCommission = "";
        txtRemarks = "";
        txtTransId = "";
        cboCustType = "";
        txtAcctNo = "";
        txtName = "";
        txtCustGroup = "";
        txtBranchCode = "";
        txtType = "";
        txtCustRemarks = "";
        txtTouristName = "";
        txtTouristPassportNo = "";
        txtTouristInstrumentNo = "";
        tdtTouristInstrumentDt = "";
        txtTouristBankDetails = "";
        txtTouristRemarks = "";
        txtDiaTransAmt = "";
        txtDiaCrossCcyRate = "";
        txtDiaEquiAmt = "";
        txtDiaComm = "";
        txtDiaTotAmt = "";
        txtTotalAmount = "";
        txtTransAmount = "";
        //resetStatus();
        notifyObservers();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    /** To set the status based on ActionType, New, Edit, etc., */
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
    
    private boolean rdoTransType_Deposit = false;
    private boolean rdoTransType_Withdrawal = false;
    private String cboTransCurrency = "";
    private String cboConvCurrency = "";
    private String txtValueDate = "";
    private String txtExchangeRate = "";
    private String txtCommission = "";
    private String txtRemarks = "";
    private String txtTransId = "";
    private String cboCustType = "";
    private String txtAcctNo = "";
    private String txtName = "";
    private String txtCustGroup = "";
    private String txtBranchCode = "";
    private String txtType = "";
    private String txtCustRemarks = "";
    private String txtTouristName = "";
    private String txtTouristPassportNo = "";
    private String txtTouristInstrumentNo = "";
    private String tdtTouristInstrumentDt = "";
    private String txtTouristBankDetails = "";
    private String txtTouristRemarks = "";
    private String txtDiaTransAmt = "";
    private String txtDiaCrossCcyRate = "";
    private String txtDiaEquiAmt = "";
    private String txtDiaComm = "";
    private String txtDiaTotAmt = "";
    private String txtTotalAmount = "";
    private String txtTransAmount = "";
    private String txtFromBranch = "";
    private String txtImpTrID = "";
    
    void setRdoTransType_Deposit(boolean rdoTransType_Deposit){
            this.rdoTransType_Deposit = rdoTransType_Deposit;
            setChanged();
    }
    boolean getRdoTransType_Deposit(){
            return this.rdoTransType_Deposit;
    }

    void setRdoTransType_Withdrawal(boolean rdoTransType_Withdrawal){
            this.rdoTransType_Withdrawal = rdoTransType_Withdrawal;
            setChanged();
    }
    boolean getRdoTransType_Withdrawal(){
            return this.rdoTransType_Withdrawal;
    }

    void setCboTransCurrency(String cboTransCurrency){
            this.cboTransCurrency = cboTransCurrency;
            setChanged();
    }
    String getCboTransCurrency(){
            return this.cboTransCurrency;
    }

    void setCboConvCurrency(String cboConvCurrency){
            this.cboConvCurrency = cboConvCurrency;
            setChanged();
    }
    String getCboConvCurrency(){
            return this.cboConvCurrency;
    }

    void setTxtValueDate(String txtValueDate){
            this.txtValueDate = txtValueDate;
            setChanged();
    }
    String getTxtValueDate(){
            return this.txtValueDate;
    }

    void setTxtExchangeRate(String txtExchangeRate){
            this.txtExchangeRate = txtExchangeRate;
            setChanged();
    }
    String getTxtExchangeRate(){
            return this.txtExchangeRate;
    }

    void setTxtCommission(String txtCommission){
            this.txtCommission = txtCommission;
            setChanged();
    }
    String getTxtCommission(){
            return this.txtCommission;
    }

    void setTxtRemarks(String txtRemarks){
            this.txtRemarks = txtRemarks;
            setChanged();
    }
    String getTxtRemarks(){
            return this.txtRemarks;
    }

    void setTxtTransId(String txtTransId){
            this.txtTransId = txtTransId;
            setChanged();
    }
    String getTxtTransId(){
            return this.txtTransId;
    }

    void setCboCustType(String cboCustType){
            this.cboCustType = cboCustType;
            setChanged();
    }
    String getCboCustType(){
            return this.cboCustType;
    }

    void setTxtAcctNo(String txtAcctNo){
            this.txtAcctNo = txtAcctNo;
            setChanged();
    }
    String getTxtAcctNo(){
            return this.txtAcctNo;
    }

    void setTxtName(String txtName){
            this.txtName = txtName;
            setChanged();
    }
    String getTxtName(){
            return this.txtName;
    }

    void setTxtCustGroup(String txtCustGroup){
            this.txtCustGroup = txtCustGroup;
            setChanged();
    }
    String getTxtCustGroup(){
            return this.txtCustGroup;
    }

    void setTxtBranchCode(String txtBranchCode){
            this.txtBranchCode = txtBranchCode;
            setChanged();
    }
    String getTxtBranchCode(){
            return this.txtBranchCode;
    }

    void setTxtType(String txtType){
            this.txtType = txtType;
            setChanged();
    }
    String getTxtType(){
            return this.txtType;
    }

    void setTxtCustRemarks(String txtCustRemarks){
            this.txtCustRemarks = txtCustRemarks;
            setChanged();
    }
    String getTxtCustRemarks(){
            return this.txtCustRemarks;
    }

    void setTxtTouristName(String txtTouristName){
            this.txtTouristName = txtTouristName;
            setChanged();
    }
    String getTxtTouristName(){
            return this.txtTouristName;
    }

    void setTxtTouristPassportNo(String txtTouristPassportNo){
            this.txtTouristPassportNo = txtTouristPassportNo;
            setChanged();
    }
    String getTxtTouristPassportNo(){
            return this.txtTouristPassportNo;
    }

    void setTxtTouristInstrumentNo(String txtTouristInstrumentNo){
            this.txtTouristInstrumentNo = txtTouristInstrumentNo;
            setChanged();
    }
    String getTxtTouristInstrumentNo(){
            return this.txtTouristInstrumentNo;
    }

    void setTdtTouristInstrumentDt(String tdtTouristInstrumentDt){
            this.tdtTouristInstrumentDt = tdtTouristInstrumentDt;
            setChanged();
    }
    String getTdtTouristInstrumentDt(){
            return this.tdtTouristInstrumentDt;
    }

    void setTxtTouristBankDetails(String txtTouristBankDetails){
            this.txtTouristBankDetails = txtTouristBankDetails;
            setChanged();
    }
    String getTxtTouristBankDetails(){
            return this.txtTouristBankDetails;
    }

    void setTxtTouristRemarks(String txtTouristRemarks){
            this.txtTouristRemarks = txtTouristRemarks;
            setChanged();
    }
    String getTxtTouristRemarks(){
            return this.txtTouristRemarks;
    }

    void setTxtDiaTransAmt(String txtDiaTransAmt){
            this.txtDiaTransAmt = txtDiaTransAmt;
            setChanged();
    }
    String getTxtDiaTransAmt(){
            return this.txtDiaTransAmt;
    }

    void setTxtDiaCrossCcyRate(String txtDiaCrossCcyRate){
            this.txtDiaCrossCcyRate = txtDiaCrossCcyRate;
            setChanged();
    }
    String getTxtDiaCrossCcyRate(){
            return this.txtDiaCrossCcyRate;
    }

    void setTxtDiaEquiAmt(String txtDiaEquiAmt){
            this.txtDiaEquiAmt = txtDiaEquiAmt;
            setChanged();
    }
    String getTxtDiaEquiAmt(){
            return this.txtDiaEquiAmt;
    }

    void setTxtDiaComm(String txtDiaComm){
            this.txtDiaComm = txtDiaComm;
            setChanged();
    }
    String getTxtDiaComm(){
            return this.txtDiaComm;
    }

    void setTxtDiaTotAmt(String txtDiaTotAmt){
            this.txtDiaTotAmt = txtDiaTotAmt;
            setChanged();
    }
    String getTxtDiaTotAmt(){
            return this.txtDiaTotAmt;
    }

    void setTxtTotalAmount(String txtTotalAmount){
            this.txtTotalAmount = txtTotalAmount;
            setChanged();
    }
    String getTxtTotalAmount(){
            return this.txtTotalAmount;
    }

    void setTxtTransAmount(String txtTransAmount){
            this.txtTransAmount = txtTransAmount;
            setChanged();
    }
    String getTxtTransAmount(){
            return this.txtTransAmount;
    }
    
    public String importFile(String fileName){
        try{
            HashMap dataMap = XMLReader.read(fileName);
            //Checking if the Transaction is arleady Imported.
            if(isTransactionExist((String)dataMap.get(TR_ID))){
                return "EXIST";
            }
            //Check the Account no Branch code with the imported data if doesn't nactch
            //reset all components and show the error message.
            if(!((String)dataMap.get(TR_TO_BRANCH)).equals(ClientConstants.HO == null ? "" : ClientConstants.HO)){
                return "DIFF_BRANCH";
            } 
            populateMap(dataMap);
            System.out.println(dataMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    
    public String exportFile(){
        String fileName = "";
        try{
            HashMap dataMap = getExportMap();
            System.out.println(dataMap);
            fileName = getExportFile();
            XMLGenerator.generate(dataMap,fileName);
                        
        }catch(Exception e){
            e.printStackTrace();
        }   
        return fileName; 
    }
    private void populateMap(HashMap map){
        String toBranchCD = "";
        Iterator iterator = map.keySet().iterator();
        String key = "";
        while(iterator.hasNext()){
            key = (String)iterator.next();
            if (key.equals(TR_TYPE)){
                String value = (String)map.get(key);
                value.trim();
                if(value.equals(TR_TYPE_DEPOSIT))
                    setRdoTransType_Deposit(true);
                if(value.equals(TR_TYPE_WITHDRAWAL))
                    setRdoTransType_Withdrawal(true);
                continue;
            }else if (key.equals(TR_AMOUNT)){
                setTxtTransAmount((String)map.get(key));
                continue;
            }else if (key.equals(TR_CURRENCY)){
                setCboTransCurrency((String)map.get(key));
                continue;
            }else if (key.equals(TR_TO_BRANCH)){
                continue;
            }else if (key.equals(TR_FROM_BRANCH)){
                setTxtFromBranch((String)map.get(key));
                continue;
            }else if (key.equals(TR_ACC_NO)){
                setTxtAcctNo((String)map.get(key));
                continue;
            }else if (key.equals(TR_VALUE_DATE)){
                setTxtValueDate((String)map.get(key));
                continue;
            }else if (key.equals(TR_TOTAL_AMOUNT)){
                setTxtTotalAmount((String)map.get(key));
                continue;
            }else if (key.equals(TR_CONV_CURRENCY)){
                setCboConvCurrency((String)map.get(key));
                continue;
            }else if (key.equals(TR_CUST_TYPE)){
                setCboCustType((String)map.get(key));
                continue;
            }else if (key.equals(TR_EXCHANGE_RATE)){
                setTxtExchangeRate((String)map.get(key));
                continue;
            }else if (key.equals(TR_REMARKS)){
                setTxtRemarks((String)map.get(key));
                continue;
            }else if (key.equals(TR_ID)){
                setTxtImpTrID((String)map.get(key));
                continue;
            } 
        }
        
    }
    
    private HashMap getExportMap(){
        HashMap dataMap = new HashMap();
        dataMap.put("Root", TR_ROOT);
        if(getRdoTransType_Deposit())
            dataMap.put(TR_TYPE,TR_TYPE_DEPOSIT);
        else if(getRdoTransType_Withdrawal())
            dataMap.put(TR_TYPE,TR_TYPE_WITHDRAWAL);
        dataMap.put(TR_CURRENCY, getCboTransCurrency());
        dataMap.put(TR_AMOUNT, getTxtTransAmount());
        dataMap.put(TR_TO_BRANCH,getTxtBranchCode());
        //Set the From Brancch Code with the Current Branch operating code.  
        dataMap.put(TR_FROM_BRANCH,"Bran1");
        dataMap.put(TR_ACC_NO, getTxtAcctNo());
        dataMap.put(TR_VALUE_DATE, getTxtValueDate());
        dataMap.put(TR_TOTAL_AMOUNT, getTxtTotalAmount());
        dataMap.put(TR_CONV_CURRENCY, getCboConvCurrency());
        dataMap.put(TR_CUST_TYPE, getCboCustType());
        dataMap.put(TR_EXCHANGE_RATE, getTxtExchangeRate());
        dataMap.put(TR_REMARKS, getTxtRemarks());
        dataMap.put(TR_ID, getTxtTransId());
        return dataMap;
    }
    
    private String getExportFile() throws Exception{
        //System.out.println("Getting error log file");
        final StringBuffer directory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/export/");
        final File ttDirectory = new File(directory.toString());
        //Creates the directory, if not exists
        if( !ttDirectory.exists() ) {
            ttDirectory.mkdirs();
        }
        final StringBuffer exportFile = new StringBuffer().append(directory).append(getTxtBranchCode() + "_" + getTxtTransId() + ".xml");
        return exportFile.toString();
    }
    
    /** Getter for property txtFromBranch.
     * @return Value of property txtFromBranch.
     *
     */
    public java.lang.String getTxtFromBranch() {
        return txtFromBranch;
    }
    
    /** Setter for property txtFromBranch.
     * @param txtFromBranch New value of property txtFromBranch.
     *
     */
    public void setTxtFromBranch(java.lang.String txtFromBranch) {
        this.txtFromBranch = txtFromBranch;
    }
    
    /** Getter for property txtImpTrID.
     * @return Value of property txtImpTrID.
     *
     */
    public java.lang.String getTxtImpTrID() {
        return txtImpTrID;
    }
    
    /** Setter for property txtImpTrID.
     * @param txtImpTrID New value of property txtImpTrID.
     *
     */
    public void setTxtImpTrID(java.lang.String txtImpTrID) {
        this.txtImpTrID = txtImpTrID;
    }
    
    private boolean isTransactionExist(String trID){
        List impTrList = ClientUtil.executeQuery("getSelectImportedTransactions",null);
        if(impTrList != null){
            int i=0;
            int size = impTrList.size();
            while(i<size){
                if(trID.equals(((HashMap)impTrList.get(i)).get("FROM_TRANS_ID")))
                    return true;
                i++;
            }
            
        }
        return false;
    }
}