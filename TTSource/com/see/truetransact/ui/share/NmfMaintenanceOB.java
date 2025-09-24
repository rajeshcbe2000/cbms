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

package com.see.truetransact.ui.share;


import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.share.NmfMaintenanceTO;
import com.see.truetransact.commonutil.DateUtil;
/**
 *
 * @author Ashok Vijayakumar
 */

public class NmfMaintenanceOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
//    private String txtTokenConfigId = "";
//    private String cboTokenType = "";
//    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
//    private String txtSeriesNo = "";
//    private String txtStartingTokenNo = "";
//    private String txtEndingTokenNo = "";
//    private String txtNoOfTokens = "";
     private TransactionOB transactionOB;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockeroperation.LockerOperationRB", ProxyParameters.LANGUAGE);
    
    
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private static NmfMaintenanceOB objLockerSurrenderOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(NmfMaintenanceOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtCustId="";
    private String dtdJoiningDt=null;
    private String txtNominalMemFee="";
    private String rdoNomineeReqyes="";
    private String rdoNomineereqNo="";
    private String txtNominalMemNo="";
    private String custName="";
    private HashMap _authorizeMap;
    private LinkedHashMap allowedTransactionDetailsTO=null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private Date currDt = null;
    /** Consturctor Declaration  for  TokenConfigOB */
    private NmfMaintenanceOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
          
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objLockerSurrenderOB = new NmfMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
     public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "NmfMaintenanceJNDI");
        map.put(CommonConstants.HOME, "share.NmfMaintenanceHome");
        map.put(CommonConstants.REMOTE, "share.NmfMaintenance");
    }
    
    private void createTblHeadings(){
       
//        tblHeadings1.add(resourceBundle.getString("tblHeading4"));
    }
   
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{

    }
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
   
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static NmfMaintenanceOB getInstance()throws Exception{
        return objLockerSurrenderOB;
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
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
//    private LockerSurrenderTO getLockerSurrenderTO(String command){
//        
//    }
    
    private NmfMaintenanceTO setNmfMaintenanceTO(String command){
       NmfMaintenanceTO objNmfMaintenanceTO=new NmfMaintenanceTO();
       objNmfMaintenanceTO.setNominalMemNo(getTxtNominalMemNo());
       objNmfMaintenanceTO.setCustId(getTxtCustId());
       objNmfMaintenanceTO.setOpeningDt(DateUtil.getDateMMDDYYYY(getDtdJoiningDt()));
       objNmfMaintenanceTO.setNominalMemFee(CommonUtil.convertObjToDouble(getTxtNominalMemFee()));
       if(getRdoNomineeReqyes().equals("Y")){
           objNmfMaintenanceTO.setNomineeReq("Y");
       }else if(getRdoNomineereqNo().equals("Y")){
           objNmfMaintenanceTO.setNomineeReq("N");
       }
       objNmfMaintenanceTO.setStatusBy(TrueTransactMain.USER_ID);
       objNmfMaintenanceTO.setStatusDt(currDt);
       objNmfMaintenanceTO.setCreatedBy(TrueTransactMain.USER_ID);
       objNmfMaintenanceTO.setCreatedDt(currDt);
       objNmfMaintenanceTO.setCommand(command);
       objNmfMaintenanceTO.setCustomerName(getCustName());
        return objNmfMaintenanceTO;
      
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setCustName("");
        setTxtNominalMemFee("");
        setTxtNominalMemNo("");
        setTxtCustId("");
        setDtdJoiningDt("");
        setRdoNomineeReqyes("");
        setRdoNomineereqNo("");
        
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
//            term.put("LockerOperationTO", getLockerSurrenderTO(command));
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            if (proxyReturnMap != null && proxyReturnMap.containsKey("LOCKER_OPT_ID"))  {
            ClientUtil.showMessageWindow("Operation ID : "+CommonUtil.convertObjToStr(proxyReturnMap.get("LOCKER_OPT_ID"))+"\n"
            /* +"Instrument No. : "+CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO1")) + "-" + CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO2"))*/);
        }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public void doAction(String command) {
        try {
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
//                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform(command);
//                }
//                else{
//                    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
//                    throw new TTException(objRemittancePaymentRB.getString("TOCommandError"));
//                }
//            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private void doActionPerform(String command) throws Exception{
        final HashMap data = new HashMap();
         if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
         }else{
        data.put("NmfMaintenanceTO",setNmfMaintenanceTO(command));
        if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            
        if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            
         transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
         allowedTransactionDetailsTO = null;
            
         data.put("TransactionTO",transactionDetailsTO);
         data.put("COMMAND",command);
         }
         HashMap proxyResultMap = proxy.execute(data, map);
         if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
                setProxyReturnMap(proxyResultMap);
            ClientUtil.showMessageWindow("Nominal Member No : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
         }
        setProxyReturnMap(proxyResultMap);
        setResult(_actionType);
    }
   
      public void ttNotifyObservers(){
        this.notifyObservers();
        setChanged();
    }
    /*Populates the TO object by executing a Query */
      public void populateData(HashMap whereMap) {
          HashMap mapData=null;
          try {
              mapData = proxy.executeQuery(whereMap, map);
              System.out.println("the map data is"+mapData);
                  if(mapData.containsKey("TransactionTO")){
                      List TransList=((List)mapData.get("TransactionTO"));
                      transactionOB.setDetails (TransList); 
                      setAllowedTransactionDetailsTO(transactionOB.getAllowedTransactionDetailsTO());
                  }
          }catch( Exception e ) {
              setResult(ClientConstants.ACTIONTYPE_FAILED);
              parseException.logException(e,true);
              
          }
      }
    
//   
//    /**
//     * Getter for property _authorizeMap.
//     * @return Value of property _authorizeMap.
//     */
//    public java.util.HashMap getAuthorizeMap() {
//        return _authorizeMap;
//    }
//    
//    /**
//     * Setter for property _authorizeMap.
//     * @param _authorizeMap New value of property _authorizeMap.
//     */
//    public void setAuthorizeMap(java.util.HashMap _authorizeMap) {
//        this._authorizeMap = _authorizeMap;
//    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
//    /**
//     * Getter for property transactionDetailsTO.
//     * @return Value of property transactionDetailsTO.
//     */
//    public java.util.LinkedHashMap getTransactionDetailsTO() {
//        return transactionDetailsTO;
//    }
//    
//    /**
//     * Setter for property transactionDetailsTO.
//     * @param transactionDetailsTO New value of property transactionDetailsTO.
//     */
//    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
//        this.transactionDetailsTO = transactionDetailsTO;
//    }

    public String getDtdJoiningDt() {
        return dtdJoiningDt;
    }

    public void setDtdJoiningDt(String dtdJoiningDt) {
        this.dtdJoiningDt = dtdJoiningDt;
    }

    public String getRdoNomineeReqyes() {
        return rdoNomineeReqyes;
    }

    public void setRdoNomineeReqyes(String rdoNomineeReqyes) {
        this.rdoNomineeReqyes = rdoNomineeReqyes;
    }

    public String getRdoNomineereqNo() {
        return rdoNomineereqNo;
    }

    public void setRdoNomineereqNo(String rdoNomineereqNo) {
        this.rdoNomineereqNo = rdoNomineereqNo;
    }

    public String getTxtCustId() {
        return txtCustId;
    }

    public void setTxtCustId(String txtCustId) {
        this.txtCustId = txtCustId;
    }

    public String getTxtNominalMemFee() {
        return txtNominalMemFee;
    }

    public void setTxtNominalMemFee(String txtNominalMemFee) {
        this.txtNominalMemFee = txtNominalMemFee;
    }

    public String getTxtNominalMemNo() {
        return txtNominalMemNo;
    }

    public void setTxtNominalMemNo(String txtNominalMemNo) {
        this.txtNominalMemNo = txtNominalMemNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
   
   
    
}