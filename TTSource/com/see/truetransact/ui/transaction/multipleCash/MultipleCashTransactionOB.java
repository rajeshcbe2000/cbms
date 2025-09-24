/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */

package com.see.truetransact.ui.transaction.multipleCash;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.transaction.multipleCash.MultipleCashTransactionTO;

/**
 *
 * @author  
 *
 */
public class MultipleCashTransactionOB extends CObservable {
//    private String txtComplaintid = "";
//    private String txtDateofComplaint="";
//    private String txaNameAddress="";
//    private String txtEmployeeId="";
//    private String txaComments="";
    private String cboProdType="";
private String transId="";
private String singleTransId="";
    private Date currDt = null;
    public String getSingleTransId() {
        return singleTransId;
    }

    public void setSingleTransId(String singleTransId) {
        this.singleTransId = singleTransId;
    }


    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
    public String getCboInstrumentType() {
        return cboInstrumentType;
    }

    public void setCboInstrumentType(String cboInstrumentType) {
        this.cboInstrumentType = cboInstrumentType;
    }
    private String cboInstrumentType="";
    String prodType="";

    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;

    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public ComboBoxModel getCbmInputCurrency() {
        return cbmInputCurrency;
    }

    public void setCbmInputCurrency(ComboBoxModel cbmInputCurrency) {
        this.cbmInputCurrency = cbmInputCurrency;
    }

    public ComboBoxModel getCbmInstrumentType() {
        return cbmInstrumentType;
    }

    public void setCbmInstrumentType(ComboBoxModel cbmInstrumentType) {
        this.cbmInstrumentType = cbmInstrumentType;
    }
    
    private HashMap _authorizeMap;
    
    private ArrayList key;
    private ArrayList value;
    
     private ComboBoxModel cbmProdType;
 //   private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmInputCurrency;
    private ComboBoxModel cbmInstrumentType;
    
    
    
    
    public String getDepositPenalAmt() {
        return depositPenalAmt;
    }

    public void setDepositPenalAmt(String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }
    String depositPenalAmt="";

    public String getTxtAccHd() {
        return txtAccHd;
    }

    public void setTxtAccHd(String txtAccHd) {
        this.txtAccHd = txtAccHd;
    }
    String txtAccHd="";

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
    private String txtAccHed="";
    private String txtAmount="";
    private List list=new ArrayList();

    public String getCboProdType() {
        return cboProdType;
    }

    public void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
    }

    public String getTxtAccHed() {
        return txtAccHed;
    }

    public void setTxtAccHed(String txtAccHed) {
        this.txtAccHed = txtAccHed;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }
    private String txtStatusBy = "";
    private String txtStatus = "";
    private String CreatedDt="";
    private String currBranName="";
    
   
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(MultipleCashTransactionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
  
    private int actionType;
    private MultipleCashTransactionOB ComplaintRegisterOB;
    MultipleCashTransactionRB complaintRB = new MultipleCashTransactionRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private MultipleCashTransactionTO objComplaintsTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public MultipleCashTransactionOB() {
        try {
            proxy = ProxyFactory.createProxy();
            currDt = ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MultipleCashTransactionJNDI");
            map.put(CommonConstants.HOME, "transaction.multipleCash.MultipleCashTransactionHome");
            map.put(CommonConstants.REMOTE,"transaction.multipleCash.MultipleCashTransaction");
            fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
     //   lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("INSTRUMENTTYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
       keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
//        cbmInputCurrency = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INSTRUMENTTYPE"));
        int idx=key.indexOf("ONLINE_TRANSFER");
        key.remove(idx);
        value.remove(idx);
        
        cbmInstrumentType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
     
    }
    
    
    
    
     private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
        
        
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        System.out.println("obj###$$$??>>>>"+obj);
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
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
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final MultipleCashTransactionTO objComplaintsTO = new MultipleCashTransactionTO();
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
         data.put("LIST",getList());
         data.put("MultipleCashTO",setComplaintsData());
        }
        else{
        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in complaint OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
         System.out.println("data in complaint OB return... : " + proxyResultMap);
//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
//                ClientUtil.showMessageWindow("Complaint No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("COMPLAIN_ID")));
//            }
        setResult(getActionType());
        setResult(actionType);
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
    
   
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
         try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            objComplaintsTO = (MultipleCashTransactionTO)((List) data.get("ComplaintsTO")).get(0);
            populateData(objComplaintsTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public MultipleCashTransactionTO setComplaintsData() {
        
        final MultipleCashTransactionTO objComplaintsTO = new MultipleCashTransactionTO();
        try{
            
            objComplaintsTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objComplaintsTO.setStatusBy(ProxyParameters.USER_ID);
//            objComplaintsTO.setAcHed(getTxtAccHed());
//            objComplaintsTO.setProdType(getCboProdType());
//            objComplaintsTO.setAmount(getTxtAmount());
//            objComplaintsTO.setComplaintid(getTxtComplaintid());
         
//           objComplaintsTO.setDateofComplaint(DateUtil.getDateMMDDYYYY(getTxtDateofComplaint()));
           objComplaintsTO.setCommand(getCommand());
           objComplaintsTO.setStatus(getAction());
           objComplaintsTO.setStatusBy(TrueTransactMain.USER_ID);
           objComplaintsTO.setList(getList());
//           objComplaintsTO.setNameAddress(getTxaNameAddress());
//           objComplaintsTO.setEmployeeid(getTxtEmployeeId());
//           objComplaintsTO.setComments(getTxaComments());
           objComplaintsTO.setBranCode(null);
           if(getCommand().equalsIgnoreCase("INSERT")){
           objComplaintsTO.setCreatedBy(TrueTransactMain.USER_ID);
           objComplaintsTO.setCreatedDt(currDt);
           }
           objComplaintsTO.setScreenName(getScreen());
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objComplaintsTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setMultipleTransdactionData()");
            e.printStackTrace();
        }
        return objComplaintsTO;
    }
//    
    private void populateData(MultipleCashTransactionTO objComplaintsTO) throws Exception{
       
//        this.setCboProdType(cboProdType);
//        this.setTxtAccHed(txtAccHed);
//        this.setTxtStatusBy(CommonUtil.convertObjToStr(objComplaintsTO.getStatusBy()));
//        this.setCreatedDt(CommonUtil.convertObjToStr(objComplaintsTO.getCreatedDt()));
////        this.setTxtDateofComplaint(CommonUtil.convertObjToStr(objComplaintsTO.getDateofComplaint()));
//        //this.setTxt(CommonUtil.convertObjToStr(objComplaintsTO.getCurrBran()));
//        this.setCurrBranName(CommonUtil.convertObjToStr(objComplaintsTO.getCurrBranName()));
//        this.setStatusBy(CommonUtil.convertObjToStr(objComplaintsTO.getStatusBy()));
//        setChanged();
//        notifyObservers();
    }
    
    
    public void resetForm(){
//       setTxtDateofComplaint("");
//       setTxaNameAddress("");
//       setTxaComments("");
//       setTxtEmployeeId("");
        setTxtAccHed("");
        setTxtAmount("");
        setCboProdType("");
       setChanged();
       ttNotifyObservers();
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
     * Getter for property txtStatus.
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }
    
    /**
     * Setter for property txtStatus.
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }
  
    
    /**
     * Getter for property currBranName.
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }
    
    /**
     * Setter for property currBranName.
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }
    
  
    
    /**
     * Getter for property txaNameAddress.
     * @return Value of property txaNameAddress.
     */
//    public String getTxaNameAddress() {
//        return txaNameAddress;
//    }
//    
//    /**
//     * Setter for property txaNameAddress.
//     * @param txaNameAddress New value of property txaNameAddress.
//     */
//    public void setTxaNameAddress(String txaNameAddress) {
//        this.txaNameAddress = txaNameAddress;
//    }
//    
// 
//    
//   
//
//    
//    /**
//     * Getter for property txtComplaintid.
//     * @return Value of property txtComplaintid.
//     */
//    public String getTxtComplaintid() {
//        return txtComplaintid;
//    }
//    
//    /**
//     * Setter for property txtComplaintid.
//     * @param txtComplaintid New value of property txtComplaintid.
//     */
//    public void setTxtComplaintid(String txtComplaintid) {
//        this.txtComplaintid = txtComplaintid;
//    }
//    
//    /**
//     * Getter for property txtDateofComplaint.
//     * @return Value of property txtDateofComplaint.
//     */
//    public java.lang.String getTxtDateofComplaint() {
//        return txtDateofComplaint;
//    }
//    
//    /**
//     * Setter for property txtDateofComplaint.
//     * @param txtDateofComplaint New value of property txtDateofComplaint.
//     */
//    public void setTxtDateofComplaint(java.lang.String txtDateofComplaint) {
//        this.txtDateofComplaint = txtDateofComplaint;
//    }
//    
//    /**
//     * Getter for property txtEmployeeId.
//     * @return Value of property txtEmployeeId.
//     */
//    public String getTxtEmployeeId() {
//        return txtEmployeeId;
//    }
//    
//    /**
//     * Setter for property txtEmployeeId.
//     * @param txtEmployeeId New value of property txtEmployeeId.
//     */
//    public void setTxtEmployeeId(String txtEmployeeId) {
//        this.txtEmployeeId = txtEmployeeId;
//    }
//    
//    /**
//     * Getter for property txaComments.
//     * @return Value of property txaComments.
//     */
//    public String getTxaComments() {
//        return txaComments;
//    }
//    
//    /**
//     * Setter for property txaComments.
//     * @param txaComments New value of property txaComments.
//     */
//    public void setTxaComments(String txaComments) {
//        this.txaComments = txaComments;
//    }
//    
//    /**
//     * Setter for property lblStatus.
//     * @param lblStatus New value of property lblStatus.
//   
//    /**
//     * Setter for property lblStatus.
//     * @param lblStatus New value of property lblStatus.
//     */
//  
//    
   }
//    
//  
    
   