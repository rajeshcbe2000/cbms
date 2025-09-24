/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TodAllowedOB.java
 *
 * Created on february , 2009, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.operativeaccount;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  Administrator
 * Modified by Karthik
 */
public class TodAllowedOB extends CObservable {
    private String txtAccountNumber = "";
    private String txtAcctName = "";
    private String dtdFromDate = "";
    private String dtdToDate = "";
    private String txtStatusBy = "";
    private String txtRemarks = "";
    private int actionType;
    private String cboProductId="";
    private String cboProdType="";
    private boolean rdoSingleTransaction = false;
    private boolean rdoRunningLimit = false;
    private String txtTODAllowed= "";
    private String cboPermitedBy="";
    private String txtPermissionRefNo = "";
    private String dtdPermittedDt = "";
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmPermitedBy;
    private ComboBoxModel cbmProdType;
    private HashMap _authorizeMap;
    private int result;
    private String lblTransId="";
    final String SINGLE = "SINGLE";
    final String RUNNING = "RUNNING";
    private String transaction_id="";
    private String CreatedDt="";
    private String txtRepayPeriod = "";
    private String cboRepayPeriod = "";
    private ComboBoxModel cbmRepayPeriod;
    private Date lblrepayedDateValue = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(TodAllowedOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private String loanInt;
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap totalLoanAmount;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private TodAllowedOB todAllowedOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    TodAllowedRB objTodAllowedRB = new TodAllowedRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private TodAllowedTO objTodAllowedTO;
    public boolean ADVANCEFlag = false;
    public boolean OAFlag = false;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public TodAllowedOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "TodAllowedJNDI");
            map.put(CommonConstants.HOME, "operativeaccount.TodAllowedHome");
            map.put(CommonConstants.REMOTE, "operativeaccount.TodAllowed");
            fillDropdown();
            
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
        param = new java.util.HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKeys = new ArrayList(1);
        lookupKeys.add("TERM_LOAN.SANCTIONING_AUTHORITY");
        lookupKeys.add("PERIOD");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("TERM_LOAN.SANCTIONING_AUTHORITY"));
        cbmPermitedBy = new ComboBoxModel(key,value);
        
        fillData((HashMap)lookupValues.get("PERIOD"));
        cbmRepayPeriod= new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
//        final ArrayList lookupKeys = new ArrayList(1);
        lookupKeys.add("PRODUCTTYPE_TOD");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("PRODUCTTYPE_TOD"));
        cbmProdType = new ComboBoxModel(key,value);
        lookupValues = null;
     
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
    
  public void setCbmProductId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    HashMap lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                   fillData((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProductId = new ComboBoxModel(key,value);
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
          
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final TodAllowedTO objTodAllowedTO = new TodAllowedTO();
        objTodAllowedTO.setCommand(getCommand());
        objTodAllowedTO.setStatus(getAction());
        objTodAllowedTO.setStatusBy(TrueTransactMain.USER_ID);
        objTodAllowedTO.setStatusDt(ClientUtil.getCurrentDate());
        final HashMap data = new HashMap();
            if(get_authorizeMap() == null)  
            data.put("TodAllowed",setTodTransaction());
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap()); //For Authorization added 28 Apr 2005
        data.put("MODE",getCommand());
        String Md = getCommand();
        System.out.println("data in A/c Closing OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID) && Md=="INSERT"){
                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            }
        setResult(getActionType());
        setResult(actionType);
//        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        if(actionType !=8)
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
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
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objTodAllowedTO = (TodAllowedTO) ((List) data.get("TodAllowedTO")).get(0);
            populateTodData(objTodAllowedTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public TodAllowedTO setTodTransaction() {
        log.info("In setCashTransaction()");
        
        final TodAllowedTO objTodAllowedTO = new TodAllowedTO();
        try{
           objTodAllowedTO.setTrans_id(CommonUtil.convertObjToStr(getLblTransId()));
           objTodAllowedTO.setAccountNumber(CommonUtil.convertObjToStr(getTxtAccountNumber()));
           objTodAllowedTO.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
           Date FromDt = DateUtil.getDateMMDDYYYY(getDtdFromDate());
           objTodAllowedTO.setFromDate(FromDt);
           Date ToDt = DateUtil.getDateMMDDYYYY(getDtdToDate());
           objTodAllowedTO.setToDate(ToDt);
           Date PermittedDt = DateUtil.getDateMMDDYYYY(getDtdPermittedDt());
           objTodAllowedTO.setPermittedDt(PermittedDt);
           objTodAllowedTO.setStatusBy(ProxyParameters.USER_ID);
           objTodAllowedTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objTodAllowedTO.setProductType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
           if (!CommonUtil.convertObjToStr(getCboProductId()).equals(""))
                objTodAllowedTO.setProductId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
           if (!CommonUtil.convertObjToStr(getCboPermitedBy()).equals(""))
                objTodAllowedTO.setPermitedBy(CommonUtil.convertObjToStr(getCbmPermitedBy().getKeyForSelected()));
           objTodAllowedTO.setTodAllowed(CommonUtil.convertObjToStr(getTxtTODAllowed()));
           objTodAllowedTO.setPermissionRefNo(CommonUtil.convertObjToStr(getTxtPermissionRefNo()));
           objTodAllowedTO.setBranchCode(TrueTransactMain.BRANCH_ID);
           objTodAllowedTO.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
           
//           objTodAllowedTO.setIntCalcDt(PermittedDt);
           objTodAllowedTO.setRepayPeriod(CommonUtil.convertObjToDouble(getTxtRepayPeriod()));
           if (!CommonUtil.convertObjToStr(getCboRepayPeriod()).equals(""))
               objTodAllowedTO.setRepayPeriodDDMMYY(CommonUtil.convertObjToStr(getCbmRepayPeriod().getKeyForSelected()));
           if (isRdoSingleTransaction() == true) {
                objTodAllowedTO.setTypeOfTOD(SINGLE);
            } else if (isRdoRunningLimit() == true) {
                objTodAllowedTO.setTypeOfTOD(RUNNING);
            }
           objTodAllowedTO.setRepayDt(lblrepayedDateValue);
        }catch(Exception e){
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objTodAllowedTO;
    }
    
    private void populateTodData(TodAllowedTO objTodAllowedTO) throws Exception{
        this.setLblTransId(CommonUtil.convertObjToStr(objTodAllowedTO.getTrans_id()));
        this.setTxtAccountNumber(CommonUtil.convertObjToStr(objTodAllowedTO.getAccountNumber()));
        this.setTxtAcctName(CommonUtil.convertObjToStr(objTodAllowedTO.getAcctName()));
        this.setTxtRemarks(CommonUtil.convertObjToStr(objTodAllowedTO.getRemarks()));
        this.setDtdFromDate(DateUtil.getStringDate(objTodAllowedTO.getFromDate()));
        this.setDtdToDate(DateUtil.getStringDate(objTodAllowedTO.getToDate()));
        this.setDtdPermittedDt(DateUtil.getStringDate(objTodAllowedTO.getPermittedDt()));
        this.setTxtTODAllowed(CommonUtil.convertObjToStr(objTodAllowedTO.getTodAllowed()));
        this.setTxtPermissionRefNo(CommonUtil.convertObjToStr(objTodAllowedTO.getPermissionRefNo()));
        this.setTxtStatusBy(objTodAllowedTO.getStatusBy());
        this.setTransaction_id(objTodAllowedTO.getTrans_id());
         if (CommonUtil.convertObjToStr(objTodAllowedTO.getTypeOfTOD()).equals(SINGLE))
               setRdoSingleTransaction(true);
        else setRdoRunningLimit(true); 
        this.setTxtAcctName(CommonUtil.convertObjToStr(objTodAllowedTO.getAcctName()));
        getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(objTodAllowedTO.getProductType())); // This line added by Rajesh
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objTodAllowedTO.getProductType())));
//        setCboProdType(CommonUtil.convertObjToStr(objTodAllowedTO.getProductType()));
        setCboPermitedBy(CommonUtil.convertObjToStr(objTodAllowedTO.getPermitedBy()));
        if (!CommonUtil.convertObjToStr(objTodAllowedTO.getProductId()).equals("")) {
            setCbmProductId(objTodAllowedTO.getProductType());
            getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(objTodAllowedTO.getProductId())); // This line added by Rajesh
            setCboProductId((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objTodAllowedTO.getProductId())));
        }
        
        setTxtRepayPeriod(CommonUtil.convertObjToStr(objTodAllowedTO.getRepayPeriod()));
        setCboRepayPeriod(CommonUtil.convertObjToStr(objTodAllowedTO.getRepayPeriodDDMMYY()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objTodAllowedTO.getCreatedDt()));
        setLblrepayedDateValue(objTodAllowedTO.getRepayDt());
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
                if(mapData.get("PROD_TYPE").equals("OA") || mapData.get("PROD_TYPE").equals("AD")){
                    cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                    cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    isExists = true;
                }else{
                    cbmProductId.setKeyForSelected("");
                    cbmProdType.setKeyForSelected("");
                    isExists = false;
                }
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
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
    
    public void resetForm(){
       txtAccountNumber ="";
       txtPermissionRefNo="";
       txtRemarks="";
       txtTODAllowed="";
       dtdFromDate="";
       dtdPermittedDt="";
       dtdToDate="";
       cboPermitedBy="";
       cboProductId="";
       txtAcctName="";
       transaction_id ="";
       cboProdType="";
       txtRepayPeriod = "";
       cboRepayPeriod = "";
       setChanged();
       ttNotifyObservers();
       setCboProductId("");
       getCbmProdType().setKeyForSelected("");
       setRdoRunningLimit(false);
       setRdoSingleTransaction(false);
       CreatedDt="";
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
    
    public void updateAuthorizeStatus() {
//        HashMap hash = null;
     String status = null;
//        // System.out.println("Records'll be updated... ");
  if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            status = CommonConstants.STATUS_AUTHORIZED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            status = CommonConstants.STATUS_REJECTED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            status = CommonConstants.STATUS_EXCEPTION;
        }      
    }
    
    /**
     * Getter for property txtAcctName.
     * @return Value of property txtAcctName.
     */
    public java.lang.String getTxtAcctName() {
        return txtAcctName;
    }    
   
    /**
     * Setter for property txtAcctName.
     * @param txtAcctName New value of property txtAcctName.
     */
    public void setTxtAcctName(java.lang.String txtAcctName) {
        this.txtAcctName = txtAcctName;
    }    
  
    /**
     * Getter for property dtdFromDate.
     * @return Value of property dtdFromDate.
     */
    public java.lang.String getDtdFromDate() {
        return dtdFromDate;
    }
    
    /**
     * Setter for property dtdFromDate.
     * @param dtdFromDate New value of property dtdFromDate.
     */
    public void setDtdFromDate(java.lang.String dtdFromDate) {
        this.dtdFromDate = dtdFromDate;
    }
    
    /**
     * Getter for property dtdToDate.
     * @return Value of property dtdToDate.
     */
    public java.lang.String getDtdToDate() {
        return dtdToDate;
    }
    
    /**
     * Setter for property dtdToDate.
     * @param dtdToDate New value of property dtdToDate.
     */
    public void setDtdToDate(java.lang.String dtdToDate) {
        this.dtdToDate = dtdToDate;
    }
    
    /**
     * Getter for property txtStatusBy.
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }
    
    /**
     * Setter for property txtStatusBy.
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
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
     * Getter for property cboProductId.
     * @return Value of property cboProductId.
     */
    public java.lang.String getCboProductId() {
        return cboProductId;
    }
    
    /**
     * Setter for property cboProductId.
     * @param cboProductId New value of property cboProductId.
     */
    public void setCboProductId(java.lang.String cboProductId) {
        this.cboProductId = cboProductId;
    }
    
    /**
     * Getter for property rdoSingleTransaction.
     * @return Value of property rdoSingleTransaction.
     */
    public boolean isRdoSingleTransaction() {
        return rdoSingleTransaction;
    }
    
    /**
     * Setter for property rdoSingleTransaction.
     * @param rdoSingleTransaction New value of property rdoSingleTransaction.
     */
    public void setRdoSingleTransaction(boolean rdoSingleTransaction) {
        this.rdoSingleTransaction = rdoSingleTransaction;
    }
    
    /**
     * Getter for property rdoRunningLimit.
     * @return Value of property rdoRunningLimit.
     */
    public boolean isRdoRunningLimit() {
        return rdoRunningLimit;
    }
    
    /**
     * Setter for property rdoRunningLimit.
     * @param rdoRunningLimit New value of property rdoRunningLimit.
     */
    public void setRdoRunningLimit(boolean rdoRunningLimit) {
        this.rdoRunningLimit = rdoRunningLimit;
    }

    /**
     * Getter for property cboPermitedBy.
     * @return Value of property cboPermitedBy.
     */
    public java.lang.String getCboPermitedBy() {
        return cboPermitedBy;
    }
    
    /**
     * Setter for property cboPermitedBy.
     * @param cboPermitedBy New value of property cboPermitedBy.
     */
    public void setCboPermitedBy(java.lang.String cboPermitedBy) {
        this.cboPermitedBy = cboPermitedBy;
    }
    
    /**
     * Getter for property txtPermissionRefNo.
     * @return Value of property txtPermissionRefNo.
     */
    public java.lang.String getTxtPermissionRefNo() {
        return txtPermissionRefNo;
    }
    
    /**
     * Setter for property txtPermissionRefNo.
     * @param txtPermissionRefNo New value of property txtPermissionRefNo.
     */
    public void setTxtPermissionRefNo(java.lang.String txtPermissionRefNo) {
        this.txtPermissionRefNo = txtPermissionRefNo;
    }
    
    /**
     * Getter for property dtdPermittedDt.
     * @return Value of property dtdPermittedDt.
     */
    public java.lang.String getDtdPermittedDt() {
        return dtdPermittedDt;
    }
    
    /**
     * Setter for property dtdPermittedDt.
     * @param dtdPermittedDt New value of property dtdPermittedDt.
     */
    public void setDtdPermittedDt(java.lang.String dtdPermittedDt) {
        this.dtdPermittedDt = dtdPermittedDt;
    }
        
    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /**
     * Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     */
    public void setCbmProductId(com.see.truetransact.clientutil.ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
    }
    
    /**
     * Getter for property cbmPermitedBy.
     * @return Value of property cbmPermitedBy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPermitedBy() {
        return cbmPermitedBy;
    }
    
    /**
     * Setter for property cbmPermitedBy.
     * @param cbmPermitedBy New value of property cbmPermitedBy.
     */
    public void setCbmPermitedBy(com.see.truetransact.clientutil.ComboBoxModel cbmPermitedBy) {
        this.cbmPermitedBy = cbmPermitedBy;
    }
    
    /**
     * Getter for property txtTODAllowed.
     * @return Value of property txtTODAllowed.
     */
    public java.lang.String getTxtTODAllowed() {
        return txtTODAllowed;
    }
    
    /**
     * Setter for property txtTODAllowed.
     * @param txtTODAllowed New value of property txtTODAllowed.
     */
    public void setTxtTODAllowed(java.lang.String txtTODAllowed) {
        this.txtTODAllowed = txtTODAllowed;
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
     * Getter for property lblTransId.
     * @return Value of property lblTransId.
     */
    public java.lang.String getLblTransId() {
        return lblTransId;
    }
    
    /**
     * Setter for property lblTransId.
     * @param lblTransId New value of property lblTransId.
     */
    public void setLblTransId(java.lang.String lblTransId) {
        this.lblTransId = lblTransId;
    }
    
    /**
     * Getter for property transaction_id.
     * @return Value of property transaction_id.
     */
    public java.lang.String getTransaction_id() {
        return transaction_id;
    }
    
    /**
     * Setter for property transaction_id.
     * @param transaction_id New value of property transaction_id.
     */
    public void setTransaction_id(java.lang.String transaction_id) {
        this.transaction_id = transaction_id;
    }
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }
    
    /**
     * Getter for property CreatedDt.
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }
    
    /**
     * Setter for property CreatedDt.
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
    }
    
    /**
     * Getter for property txtRepayPeriod.
     * @return Value of property txtRepayPeriod.
     */
    public java.lang.String getTxtRepayPeriod() {
        return txtRepayPeriod;
    }
    
    /**
     * Setter for property txtRepayPeriod.
     * @param txtRepayPeriod New value of property txtRepayPeriod.
     */
    public void setTxtRepayPeriod(java.lang.String txtRepayPeriod) {
        this.txtRepayPeriod = txtRepayPeriod;
    }
    
    /**
     * Getter for property cboRepayPeriod.
     * @return Value of property cboRepayPeriod.
     */
    public java.lang.String getCboRepayPeriod() {
        return cboRepayPeriod;
    }
    
    /**
     * Setter for property cboRepayPeriod.
     * @param cboRepayPeriod New value of property cboRepayPeriod.
     */
    public void setCboRepayPeriod(java.lang.String cboRepayPeriod) {
        this.cboRepayPeriod = cboRepayPeriod;
    }
    
    /**
     * Getter for property cbmRepayPeriod.
     * @return Value of property cbmRepayPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRepayPeriod() {
        return cbmRepayPeriod;
    }
    
    /**
     * Setter for property cbmRepayPeriod.
     * @param cbmRepayPeriod New value of property cbmRepayPeriod.
     */
    public void setCbmRepayPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmRepayPeriod) {
        this.cbmRepayPeriod = cbmRepayPeriod;
    }
    
    /**
     * Getter for property lblrepayedDateValue.
     * @return Value of property lblrepayedDateValue.
     */
    public java.util.Date getLblrepayedDateValue() {
        return lblrepayedDateValue;
    }
    
    /**
     * Setter for property lblrepayedDateValue.
     * @param lblrepayedDateValue New value of property lblrepayedDateValue.
     */
    public void setLblrepayedDateValue(java.util.Date lblrepayedDateValue) {
        this.lblrepayedDateValue = lblrepayedDateValue;
    }
    
    }
    
  
    
   