/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashManagementOB.java
 *
 * Created on Fri Jan 28 16:55:46 IST 2005
 */

package com.see.truetransact.ui.transaction.cashmanagement;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementTO;
import com.see.truetransact.transferobject.transaction.cashmanagement.CashMovementDetailsTO;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  152715
 */

public class CashManagementOB extends CObservable {
    
    private boolean rdoVaultCash_Yes = false;
    private boolean rdoVaultCash_No = false;
    private String cashMovementID = "";
    private String txtReceivingCashierID = "";
    private String txtIssueCashierID = "";
    private String txtCashBoxBalance = "";
    private String lblDisplayCashierName = "";
    private String lblIssueCashierName = "";
    private String cashMovementStatus = "";
    private String cashMovementDetailsStatus = "";
    private String createdBy = "";
    private String createdDt = "";
    private String statusBy = "";
    private String statusDt = "";
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private String authorizeDt = "";
    private String denominationName = "";
    private String denominationCount = "";
    private String denominationTotal = "";
    private String cashDt = "";
    private boolean rdoTranscationType_Receipt = false;
    private boolean rdoTranscationType_Payment = false;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private HashMap operationMap;
    private ProxyFactory proxy;
    
    private final String YES = "Y";
    private final String NO = "N";
    private final String PAYMENT = "P";
    private final String RECEIPT = "R";
    private List cashDetailsList = null;
    private List transDetailsList = null;
    private List list=null;
    private List tranlist=null;
    private Date movementDate=null;
    private String totalValue="";
    final CashManagementRB objCashManagementRB = new CashManagementRB();
    
    
    private static CashManagementOB objCashManagementOB; // singleton object
    
    private final static Logger log = Logger.getLogger(CashManagementOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private LinkedHashMap cashMovementDetailsMap = null;
    private ArrayList denomDetails = null;
    private String denominationType = "";//Varible used to Store the DenominationType either TransactionDenominaton(Receit or Payment) or CashBoxDenomination
   private String denomType = "";
    Date curDate = null;
    
    static {
        try {
            log.info("Creating CashManagementOB...");
            objCashManagementOB = new CashManagementOB();
        } catch(Exception e) {
            //_log.error(e);
            parseException.logException(e,true);
        }
    }
    /**
     * Returns an instance of CashManagementOB.
     * @return  CashManagementOB
     */
    public static CashManagementOB getInstance() {
        return objCashManagementOB;
    }
    /** Creates a new instance of CashManagementOB */
    private CashManagementOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
    }
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "CashMovementJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.transaction.cashmanagement.CashMovementHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.transaction.cashmanagement.CashMovement");
    }
    
    // Setter method for rdoVaultCash_Yes
    void setRdoVaultCash_Yes(boolean rdoVaultCash_Yes){
        this.rdoVaultCash_Yes = rdoVaultCash_Yes;
        setChanged();
    }
    // Getter method for rdoVaultCash_Yes
    boolean getRdoVaultCash_Yes(){
        return this.rdoVaultCash_Yes;
    }
    
    // Setter method for rdoVaultCash_No
    void setRdoVaultCash_No(boolean rdoVaultCash_No){
        this.rdoVaultCash_No = rdoVaultCash_No;
        setChanged();
    }
    // Getter method for rdoVaultCash_No
    boolean getRdoVaultCash_No(){
        return this.rdoVaultCash_No;
    }
    
    // Setter method for lblDisplayCashierName
    void setLblDisplayCashierName(String lblDisplayCashierName){
        this.lblDisplayCashierName = lblDisplayCashierName;
        setChanged();
    }
    // Getter method for lblDisplayCashierName
    String getLblDisplayCashierName(){
        return this.lblDisplayCashierName;
    }
    
    // Setter method for lblIssueCashierName
    void setLblIssueCashierName(String lblIssueCashierName){
        this.lblIssueCashierName = lblIssueCashierName;
        setChanged();
    }
    // Getter method for lblIssueCashierName
    String getLblIssueCashierName(){
        return this.lblIssueCashierName;
    }
    // Setter method for txtReceivingCashierID
    void setTxtReceivingCashierID(String txtReceivingCashierID){
        this.txtReceivingCashierID = txtReceivingCashierID;
        setChanged();
    }
    // Getter method for txtReceivingCashierID
    String getTxtReceivingCashierID(){
        return this.txtReceivingCashierID;
    }
    // Setter method for cashMovementID
    void setCashMovementID(String cashMovementID){
        this.cashMovementID = cashMovementID;
        setChanged();
    }
    // Getter method for cashMovementID
    String getCashMovementID(){
        return this.cashMovementID;
    }
    // Setter method for cashDt
    void setCashDt(String cashDt){
        this.cashDt = cashDt;
        setChanged();
    }
    // Getter method for cashDt
    String getCashDt(){
        return this.cashDt;
    }
    // Setter method for txtIssueCashierID
    void setTxtIssueCashierID(String txtIssueCashierID){
        this.txtIssueCashierID = txtIssueCashierID;
        setChanged();
    }
    // Getter method for txtIssueCashierID
    String getTxtIssueCashierID(){
        return this.txtIssueCashierID;
    }
    
    // Setter method for rdoTranscationType_Receipt
    void setRdoTranscationType_Receipt(boolean rdoTranscationType_Receipt){
        this.rdoTranscationType_Receipt = rdoTranscationType_Receipt;
        setChanged();
    }
    // Getter method for rdoTranscationType_Receipt
    boolean getRdoTranscationType_Receipt(){
        return this.rdoTranscationType_Receipt;
    }
    
    // Setter method for rdoTranscationType_Payment
    void setRdoTranscationType_Payment(boolean rdoTranscationType_Payment){
        this.rdoTranscationType_Payment = rdoTranscationType_Payment;
        setChanged();
    }
    // Getter method for rdoTranscationType_Payment
    boolean getRdoTranscationType_Payment(){
        return this.rdoTranscationType_Payment;
    }
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
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
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    // Setter method for authorizeDt
    void setAuthorizeDt(String authorizeDt){
        this.authorizeDt = authorizeDt;
        setChanged();
    }
    // Getter method for authorizeDt
    String getAuthorizeDt(){
        return this.authorizeDt;
    }
    // Setter method for authorizeBy
    void setAuthorizeBy(String authorizeBy){
        this.authorizeBy = authorizeBy;
        setChanged();
    }
    // Getter method for authorizeBy
    String getAuthorizeBy(){
        return this.authorizeBy;
    }
    // Setter method for authorizeStatus
    public void setAuthorizeStatus(String authorizeStatus){
        this.authorizeStatus = authorizeStatus;
        setChanged();
    }
    // Getter method for authorizeStatus
    public java.lang.String getAuthorizeStatus(){
        return this.authorizeStatus;
    }
    // Setter method for createdBy
    void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
        setChanged();
    }
    // Getter method for createdBy
    String getCreatedBy(){
        return this.createdBy;
    }
    // Setter method for createdDt
    void setCreatedDt(String createdDt){
        this.createdDt = createdDt;
        setChanged();
    }
    // Getter method for createdDt
    String getCreatedDt(){
        return this.createdDt;
    }
    // Setter method for statusDt
    void setStatusDt(String statusDt){
        this.statusDt = statusDt;
        setChanged();
    }
    // Getter method for statusDt
    String getStatusDt(){
        return this.statusDt;
    }
    // Setter method for statusBy
    void setStatusBy1(String statusBy){
        this.statusBy = statusBy;
        setChanged();
    }
    // Getter method for statusBy
    String getStatusBy1(){
        return this.statusBy;
    }
    // Setter method for cashMovementStatus
    void setCashMovementStatus(String cashMovementStatus){
        this.cashMovementStatus = cashMovementStatus;
        setChanged();
    }
    // Getter method for cashMovementStatus
    String getCashMovementStatus(){
        return this.cashMovementStatus;
    }
    // Setter method for cashMovementDetailsStatus
    void setCashMovementDetailsStatus(String cashMovementDetailsStatus){
        this.cashMovementDetailsStatus = cashMovementDetailsStatus;
        setChanged();
    }
    // Getter method for cashMovementDetailsStatus
    String getCashMovementDetailsStatus(){
        return this.cashMovementDetailsStatus;
    }
    // Setter method for denominationName
    void setDenominationName(String denominationName){
        this.denominationName = denominationName;
        setChanged();
    }
    // Getter method for denominationName
    String getDenominationName(){
        return this.denominationName;
    }
    
    /**
     * Getter for property denominationType.
     * @return Value of property denominationType.
     */
    public java.lang.String getDenominationType() {
        return this.denominationType;
    }
    
    /**
     * Setter for property denominationType.
     * @param denominationType New value of property denominationType.
     */
    public void setDenominationType(java.lang.String denominationType) {
        this.denominationType = denominationType;
        setChanged();
    }
    
    public java.lang.String getDenomType() {
        return this.denomType;
    }
    
    /**
     * Setter for property denominationType.
     * @param denominationType New value of property denominationType.
     */
    public void setDenomType(java.lang.String denomType) {
        this.denomType = denomType;
        setChanged();
    }
    // Setter method for denominationCount
    void setDenominationCount(String denominationCount){
        this.denominationCount = denominationCount;
        setChanged();
    }
    // Getter method for denominationCount
    String getDenominationCount(){
        return this.denominationCount;
    }
    // Setter method for denominationTotal
    void setDenominationTotal(String denominationTotal){
        this.denominationTotal = denominationTotal;
        setChanged();
    }
    // Getter method for denominationTotal
    String getDenominationTotal(){
        return this.denominationTotal;
    }
    // Setter method for txtCashBoxBalance
    void setTxtCashBoxBalance(String txtCashBoxBalance){
        this.txtCashBoxBalance = txtCashBoxBalance;
        setChanged();
    }
    // Getter method for txtCashBoxBalance
    String getTxtCashBoxBalance(){
        return this.txtCashBoxBalance;
    }
    /**
     * Set CashMovement fields in the OB
     */
    private void setCashMovementOB(CashMovementTO objCashMovementTO) throws Exception {
        log.info("Inside setOtherBankOB()");
        setCashMovementID(CommonUtil.convertObjToStr(objCashMovementTO.getCashMovementId()));
        setTxtIssueCashierID(CommonUtil.convertObjToStr(objCashMovementTO.getIssuedCashierId()));
        setTxtReceivingCashierID(CommonUtil.convertObjToStr(objCashMovementTO.getReceivedCashierId()));
        setTxtCashBoxBalance(CommonUtil.convertObjToStr(objCashMovementTO.getCashBoxBalance()));
        
        if(objCashMovementTO.getVaultCash()!=null)
        if (objCashMovementTO.getVaultCash().equals(YES)) {
            setRdoVaultCash_Yes(true);
        } else if (objCashMovementTO.getVaultCash().equals(NO)) {
            setRdoVaultCash_No(true);
        }
        if(objCashMovementTO.getTransType()!=null)
        if (objCashMovementTO.getTransType().equals(PAYMENT)) {
            setRdoTranscationType_Payment(true);
        } else if (objCashMovementTO.getTransType().equals(RECEIPT)) {
            setRdoTranscationType_Receipt(true);
        }
        
        setAuthorizeDt(DateUtil.getStringDate(objCashMovementTO.getAuthorizeDt()));
        setAuthorizeBy(CommonUtil.convertObjToStr(objCashMovementTO.getAuthorizeBy()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objCashMovementTO.getAuthorizeStatus()));
        setCreatedBy(CommonUtil.convertObjToStr(objCashMovementTO.getCreatedBy()));
        setCreatedDt(DateUtil.getStringDate(objCashMovementTO.getCreatedDt()));
        setStatusDt(DateUtil.getStringDate(objCashMovementTO.getStatusDt()));
        setStatusBy(CommonUtil.convertObjToStr(objCashMovementTO.getStatusBy()));
        setCashMovementStatus(CommonUtil.convertObjToStr(objCashMovementTO.getStatus()));
        // Display Labels
        setLblDisplayCashierName(CommonUtil.convertObjToStr(getLblDisplayCashierName()));
        setLblIssueCashierName(CommonUtil.convertObjToStr(getLblIssueCashierName()));
        setCashDt(CommonUtil.convertObjToStr(DateUtil.getStringDate(objCashMovementTO.getCashDt())));
    }
    
    /**
     * To set the data in CashMovementTO TO
     */
    public CashMovementTO setCashMovementTO() {
        log.info("In setCashMovementTO...");
        
        final CashMovementTO objCashMovementTO = new CashMovementTO();
        try{
            objCashMovementTO.setCashMovementId(CommonUtil.convertObjToStr(getCashMovementID()));
            objCashMovementTO.setIssuedCashierId(CommonUtil.convertObjToStr(getTxtIssueCashierID()));
            objCashMovementTO.setReceivedCashierId(CommonUtil.convertObjToStr(getTxtReceivingCashierID()));
            objCashMovementTO.setCashBoxBalance(CommonUtil.convertObjToDouble(getTxtCashBoxBalance()));
            
            Date CtDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCreatedDt()));
            if(CtDt != null){
            Date ctDate = (Date) curDate.clone();
            ctDate.setDate(CtDt.getDate());
            ctDate.setMonth(CtDt.getMonth());
            ctDate.setYear(CtDt.getYear());
//            objCashMovementTO.setCreatedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCreatedDt())));
            objCashMovementTO.setCreatedDt(ctDate);
            }else{
                objCashMovementTO.setCreatedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCreatedDt())));
            }
            
            objCashMovementTO.setCreatedBy(CommonUtil.convertObjToStr(getCreatedBy()));
            objCashMovementTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeStatus()));
            objCashMovementTO.setAuthorizeBy(CommonUtil.convertObjToStr(getAuthorizeBy()));
            
            Date AtDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt()));
            if(AtDt != null){
            Date atDate = (Date) curDate.clone();
            atDate.setDate(AtDt.getDate());
            atDate.setMonth(AtDt.getMonth());
            atDate.setYear(AtDt.getYear());
//            objCashMovementTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            objCashMovementTO.setAuthorizeDt(atDate);
            }else{
                objCashMovementTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getAuthorizeDt())));
            }
            
            Date StDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getStatusDt()));
            if(StDt != null){
            Date stDate = (Date) curDate.clone();
            stDate.setDate(StDt.getDate());
            stDate.setMonth(StDt.getMonth());
            stDate.setYear(StDt.getYear());
//            objCashMovementTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getStatusDt())));
            objCashMovementTO.setStatusDt(stDate);
            }else{
                objCashMovementTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getStatusDt())));
            }
            
            objCashMovementTO.setStatus(CommonUtil.convertObjToStr(getCashMovementStatus()));
            objCashMovementTO.setStatusBy(CommonUtil.convertObjToStr(getStatusBy()));
            objCashMovementTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            
            Date ChDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCashDt()));
            if(ChDt != null){
            Date chDate = (Date) curDate.clone();
            chDate.setDate(ChDt.getDate());
            chDate.setMonth(ChDt.getMonth());
            chDate.setYear(ChDt.getYear());
//            objCashMovementTO.setCashDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCashDt())));
            objCashMovementTO.setCashDt(chDate);
            }else{
                objCashMovementTO.setCashDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getCashDt())));
            }
            
            if (getRdoVaultCash_Yes()) {
                objCashMovementTO.setVaultCash(CommonUtil.convertObjToStr(YES));
            } else if (getRdoVaultCash_No()) {
                objCashMovementTO.setVaultCash(CommonUtil.convertObjToStr(NO));
            }
            if (getRdoTranscationType_Payment()) {
                objCashMovementTO.setTransType(CommonUtil.convertObjToStr(PAYMENT));
            } else if (getRdoTranscationType_Receipt()) {
                objCashMovementTO.setTransType(CommonUtil.convertObjToStr(RECEIPT));
            }
            
            
            
        }catch(Exception e){
            log.info("Error in setCashMovementTO()");
            parseException.logException(e,true);
        }
        return objCashMovementTO;
    }
    /**
     * To set the data in CashMovementDetailsTO TO
     */
    public CashMovementDetailsTO setCashMovementDetailsTO() {
        log.info("In setCashMovementTO...");
        
        final CashMovementDetailsTO objCashMovementDetailsTO = new CashMovementDetailsTO();
        try{
            objCashMovementDetailsTO.setCashMovementId(CommonUtil.convertObjToStr(getCashMovementID()));
            objCashMovementDetailsTO.setDenominationCount(CommonUtil.convertObjToDouble(getDenominationCount()));
            objCashMovementDetailsTO.setDenominationName(CommonUtil.convertObjToStr(getDenominationName()));
            objCashMovementDetailsTO.setDenominationTotal(CommonUtil.convertObjToDouble(getDenominationTotal()));
            objCashMovementDetailsTO.setStatus(CommonUtil.convertObjToStr(getCashMovementDetailsStatus()));
            objCashMovementDetailsTO.setDenominationType(CommonUtil.convertObjToStr(getDenominationType()));
            objCashMovementDetailsTO.setMovementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMovementDate())));
            objCashMovementDetailsTO.setTotalValue(CommonUtil.convertObjToDouble(getTotalValue()));
            objCashMovementDetailsTO.setDenomType(CommonUtil.convertObjToStr(getDenomType()));
        }catch(Exception e){
            log.info("Error in setCashMovementTO()");
            parseException.logException(e,true);
        }
        return objCashMovementDetailsTO;
    }
    public void saveCashMovementDetails(LinkedHashMap hashCashMovementDetails, String denominationType) {
        try {
            ArrayList denomList = new ArrayList();
            if (hashCashMovementDetails != null && hashCashMovementDetails.size()>0) {
                if (cashMovementDetailsMap == null)
                    cashMovementDetailsMap= new LinkedHashMap();
                Object keySets[] = hashCashMovementDetails.keySet().toArray();
                for (int i=0,j=hashCashMovementDetails.size();i<j;i++) {
                    HashMap denomMap = new HashMap();
                    ArrayList denominationValues = (ArrayList)hashCashMovementDetails.get(CommonUtil.convertObjToStr(keySets[i]));
                    setDenomType((String)denominationValues.get(0));
                    setDenominationName((String)denominationValues.get(1));
                    setDenominationCount((String)denominationValues.get(2));
                    setDenominationTotal((String)denominationValues.get(3));
                    setCashMovementDetailsStatus((String)denominationValues.get(4));
                    setDenominationType((String)denominationValues.get(5));
                    
                    HashMap hmap=new HashMap();
                    hmap.put("DENOMINATION_NAME",denominationValues.get(1));
                    if(denominationValues.get(5).equals("RECEIPT")){
                      setDenominationType("VAULTREC");   
                     hmap.put("SOURCE","VAULTREC");
                    }else if(denominationValues.get(5).equals("PAYMENT")){
                        setDenominationType("VAULTPAYMENT");   
                    }else if(denominationValues.get(5).equals("CASH BOX")){
                        setDenominationType("CASH BOX");   
                    }else{
                        setDenominationType("CASHMOV");  
                    }
//                    hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//                    List lst=ClientUtil.executeQuery("getTotalValueDuringDay", hmap);
//                    if(lst!=null && lst.size()>0)
//                    hmap=(HashMap)lst.get(0);
//                    double count=CommonUtil.convertObjToDouble(hmap.get("COUNT")).doubleValue();
//                    double amt=count-CommonUtil.convertObjToDouble(denominationValues.get(2)).doubleValue();
//                    String tot=CommonUtil.convertObjToStr(new Double(amt));
//                    setTotalValue(tot);
                    //                    if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    //
                    //                    }
                    
                    final CashMovementDetailsTO objCashMovementDetailsTO = setCashMovementDetailsTO();
                    denomMap.put(CommonUtil.convertObjToStr(keySets[i]), objCashMovementDetailsTO);
                    denomList.add(denomMap);
                    denomMap = null;
                    denominationValues = null;
                }
                cashMovementDetailsMap.put(denominationType, denomList);
                denomList = null;
                keySets = null;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        //            if (hashCashMovementDetails != null && hashCashMovementDetails.size()>0) {
        //                if (cashMovementDetailsMap == null)
        //                    cashMovementDetailsMap = new LinkedHashMap();
        //                Object keySets[] = hashCashMovementDetails.keySet().toArray();
        //                for (int i=0,j=hashCashMovementDetails.size();i<j;i++) {
        //                    ArrayList denominationValues = (ArrayList)hashCashMovementDetails.get(CommonUtil.convertObjToStr(keySets[i]));
        //                    setDenominationName((String)denominationValues.get(0));
        //                    setDenominationCount((String)denominationValues.get(1));
        //                    setDenominationTotal((String)denominationValues.get(2));
        //                    setCashMovementDetailsStatus((String)denominationValues.get(3));
        //                    setDenominationType((String)denominationValues.get(4));
        //                    //                    if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
        //                    //
        //                    //                    }
        //
        //                    final CashMovementDetailsTO objCashMovementDetailsTO = setCashMovementDetailsTO();
        //                    cashMovementDetailsMap.put(CommonUtil.convertObjToStr(keySets[i]), objCashMovementDetailsTO);
        //                    denominationValues = null;
        //                }
        //                keySets = null;
        //            }
        //        } catch (Exception e) {
        //            parseException.logException(e,true);
        //        }
    }
    /**
     * Set CashMovementDetails fields in the OB
     */
    private void setCashMovementDetailsOB(CashMovementDetailsTO objCashMovementDetailsTO) throws Exception {
        log.info("Inside setCashMovementDetailsOB()");
        setCashMovementID(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getCashMovementId()));
        setDenominationCount(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getDenominationCount()));
        setDenominationTotal(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getDenominationTotal()));
        setDenominationName(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getDenominationName()));
        setCashMovementDetailsStatus(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getStatus()));
        setDenominationType(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getDenominationType()));
        setDenomType(CommonUtil.convertObjToStr(objCashMovementDetailsTO.getDenomType()));
    }
    
    /* To get the type of command */
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
            default:
        }
        return command;
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    throw new TTException(objCashManagementRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    public void doActionPerformAuthorize() throws Exception{
        HashMap hmap=new HashMap();
           hmap.put("transDetailsList",getTransDetails());            
            hmap.put("cashDetailsList",getcashDetailsList());
    
                if (getRdoTranscationType_Payment())
            if(getRdoVaultCash_Yes()){              
               hmap.put("VAULT_YES", "VAULT_YES");
               hmap.put("PAYMENT_YES","PAYMENT_YES");          
            }
            if (getRdoTranscationType_Receipt())
                if(getRdoVaultCash_Yes()){              
               hmap.put("VAULT_YES", "VAULT_YES");
               hmap.put("RECEIPT_YES","RECEIPT_YES");          
            } 
            hmap.put("AUTHORIZED","AUTHORIZED");
            hmap.put("MOVEMENT_DATE",curDate);
        proxy.executeQuery(hmap,operationMap);
    }
   
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        CashMovementTO objCashMovementTO = setCashMovementTO();
        CashMovementDetailsTO objCashMovementDetailsTO;
        final HashMap data = new HashMap();
        data.put("CashMovementTO",objCashMovementTO);
        data.put("CashMovementDetailsTO",cashMovementDetailsMap);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        
        if(getRdoTranscationType_Receipt())
            if(getRdoVaultCash_Yes()){
                objCashMovementTO.setVaultCash(CommonUtil.convertObjToStr(YES));
                objCashMovementTO.setTransType(CommonUtil.convertObjToStr(RECEIPT));
               // objCashMovementDetailsTO.setDenominationType(CommonUtil.convertObjToStr(getDenominationType()));
                data.put("VAULT_YES", objCashMovementTO);
                data.put("RECIEPT_YES",objCashMovementTO);
                //data.put("TYPERECIEPT",objCashMovementDetailsTO);
                //data.put("DENOTYPE",getDenominationType());
            
        }
        if (getRdoTranscationType_Payment())
            if(getRdoVaultCash_Yes()){
               objCashMovementTO.setVaultCash(CommonUtil.convertObjToStr(YES));
               objCashMovementTO.setTransType(CommonUtil.convertObjToStr(PAYMENT));
               data.put("VAULT_YES", objCashMovementTO);
               data.put("PAYMENT_YES",objCashMovementTO);
            }
        objCashMovementTO = null;
        HashMap proxyReturnMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyReturnMap);
        setResult(actionType);
//        actionType = ClientConstants.ACTIONTYPE_CANCEL;
//        resetForm();
    }
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("The error in populateData");
            parseException.logException(e,true);
        }
    }
    /**
     * populate CashMovement & CashMovementDetails
     */
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB...");
        if (mapData.containsKey("getAvailableDenominations")) {
            cashDetailsList = (List) mapData.get("getAvailableDenominations");
//        } else if (mapData.containsKey("getAvailableDenominations")) {
               } else if (mapData.containsKey("getClosingDenominations")) {
            cashDetailsList = (List) mapData.get("getClosingDenominations");
        } else {
            CashMovementTO objCashMovementTO = (CashMovementTO) ((List) mapData.get("CashMovementTO")).get(0);
            setCashMovementOB(objCashMovementTO);
            transDetailsList = (List) mapData.get("TransactionDetailsTO");
            cashDetailsList = (List) mapData.get("CashBoxDetailsTO");
           
            setcashDetailsList(cashDetailsList);
            setTransDetails(transDetailsList);
            objCashMovementTO = null;
        }
        ttNotifyObservers();
    }
    // to populate cash movement details returning a ArrayList which is populated with list of hashmap each holding
    //the TransferObject instance with DenominationName as their Key
    public ArrayList populateCashDenominations() {
        ArrayList denomMapList = new ArrayList();
        HashMap denomHashMap = new HashMap();
        try {
            if(cashDetailsList !=null)
            for (int i=0,j=cashDetailsList.size();i<j;i++) {
                CashMovementDetailsTO objCashMovementDetailsTO = (CashMovementDetailsTO)cashDetailsList.get(i);
//                String dname=CommonUtil.convertObjToStr(new Integer(i));
              String  dname=objCashMovementDetailsTO.getDenominationName()+objCashMovementDetailsTO.getDenomType();
//                denomHashMap.put((String)objCashMovementDetailsTO.getDenominationName(),objCashMovementDetailsTO);
                denomHashMap.put(dname,objCashMovementDetailsTO);
                objCashMovementDetailsTO = null;
            }
            denomMapList.add(denomHashMap);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return denomMapList;
        
    }
    
    public ArrayList populateTransDenominations() {
        ArrayList denomMapList = new ArrayList();
        HashMap denomHashMap = new HashMap();
        try {
            for (int i=0,j=transDetailsList.size();i<j;i++) {
                CashMovementDetailsTO objCashMovementDetailsTO = (CashMovementDetailsTO)transDetailsList.get(i);
                String dname=CommonUtil.convertObjToStr(new Integer(i));
                dname=(String)objCashMovementDetailsTO.getDenominationName()+objCashMovementDetailsTO.getDenomType();
//                denomHashMap.put((String)objCashMovementDetailsTO.getDenominationName(),objCashMovementDetailsTO);
                denomHashMap.put(dname,objCashMovementDetailsTO);
                objCashMovementDetailsTO = null;
            }
            denomMapList.add(denomHashMap);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return denomMapList;
        
    }
    /**
     * To place the denomination count in the corresponding denomination name
     */
    public int matchDenomination(int index, String label, String dType,ArrayList denomList) {
        int returnInt = -1;
        try {
            if (label != null && !label.equals("") && label.length()>0 && denomList != null && denomList.size()>0) {
                for(int i=0,j=denomList.size();i<j;i++){
                    HashMap toMap = (HashMap) denomList.get(i);
                    Object objArr[] = toMap.keySet().toArray();
                    for (int k=0,l=toMap.size();k<l;k++) {
                        final CashMovementDetailsTO objCashMovementDetailsTO = (CashMovementDetailsTO) toMap.get(CommonUtil.convertObjToStr(objArr[k]));
                        if (objCashMovementDetailsTO.getDenominationName().equals(label) && objCashMovementDetailsTO.getDenomType().equals(dType)) {
                            setCashMovementDetailsOB(objCashMovementDetailsTO);
                            returnInt = index;
                        }
                    }
                    objArr = null;
                    toMap = null;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return returnInt;
    }
    // returns the date for display
    public String returnDisplayDate() {
        return DateUtil.getStringDate((Date) curDate.clone());
    }
    // to display emp name at edit/delete
    public String getCashierName(String userId) {
        String empName = "";
        if (userId != null && !userId.equals("") && userId.length()>0) {
            HashMap where = new HashMap();
            where.put("USER_ID", userId);
            List list  = ClientUtil.executeQuery("getEmployeeNameAtEdit", where);
            where = null;
            if (list != null && list.size()>0) {
                empName = (String)((LinkedHashMap)list.get(0)).get("EMP NAME");
            }
        }
        return empName;
    }
    // reset fields
    public void resetForm() {
        setCashMovementID("");
        setTxtCashBoxBalance("");
        setRdoTranscationType_Payment(false);
        setRdoTranscationType_Receipt(false);
        setRdoVaultCash_No(false);
        setRdoVaultCash_Yes(false);
        setTxtIssueCashierID("");
        setTxtReceivingCashierID("");
        setAuthorizeDt("");
        setAuthorizeBy("");
        setAuthorizeStatus("");
        setCreatedBy("");
        setCreatedDt("");
        setStatusDt("");
        setStatusBy("");
        setCashMovementStatus("");
        setLblDisplayCashierName("");
        setCashDt("");
        setLblIssueCashierName("");
        setDenominationCount("");
        setDenominationTotal("");
        setDenominationName("");
        setDenominationType("");
        cashMovementDetailsMap = null;
        ttNotifyObservers();
    }
    
    public void setMovementDate(Date movementDate){
        this.movementDate=movementDate;
    }
    
    public Date getMovementDate(){
      return movementDate;
    }
    public void setTotalValue(String totalValue){
        this.totalValue=totalValue;
    }
    
     public String getTotalValue(){
        return totalValue;
    }
     public void setcashDetailsList(List list){
         this.list=list;
     }
     public List getcashDetailsList(){
         return list;
     }
       
     
     public void setTransDetails(List tranlist){
         this.tranlist=tranlist;
     }
     public List getTransDetails(){
         return tranlist;
     }
    
}
