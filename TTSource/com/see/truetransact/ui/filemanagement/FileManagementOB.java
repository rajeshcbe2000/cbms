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

package com.see.truetransact.ui.filemanagement;

import com.see.truetransact.ui.inwardregister.*;
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
import com.see.truetransact.transferobject.filemenagement.FileManagementTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.inwardregister.InwardRegisterTO;

/**
 *
 * @author  
 *
 */
public class FileManagementOB extends CObservable {
    private String txtInwardNo = "";
    private String txtDate="";
    private String txaDetails="";
    private String txaRemarks="";    
    private String txtReferenceNo="";
    private String txtActionTaken="";
    private String txtStatusBy = "";
    private String txtStatus = "";
    private String CreatedDt="";
    private String currBranName="";
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(FileManagementOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private FileManagementOB InwardOB;
    InwardRB InwardRB = new InwardRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private FileManagementTO objFileManagementTO;
    private Date currDt = null;
    
    /*  Fields for new screen*/
    private String tdtApplnDt = "";
    private String txtApplnNo = "";
    private String txtFileNo = "";
    private String txtMemberNo = "";
    private String txtSubmittedBy = "";
    private String txtAddress = "";
    private String txtParticulars = "";
    private String txtRemarks = "";
    
    private String cboSubmittedTo = "";
    private String tdtSubmissionDt1 = "";
    private String tdtSubmissionDt2 = "";
    private String tdtSubmissionDt3 = "";
    private String txtSubmissionAction1 = "";
    private String txtSubmissionAction2 = "";
    private String txtSubmissionAction3 = "";
    
    private String cboApprovalStatus = "";
    private String tdtApprovalDt1 = "";
    private String tdtApprovalDt2 = "";
    private String tdtApprovalDt3 = "";
    private String txtApprovalAction1 = "";
    private String txtApprovalAction2 = "";
    private String txtApprovalAction3 = "";
    
    private ComboBoxModel cbmSubmittedTo ;
    private ComboBoxModel cbmapprovedBy;
    
    //End
    
    
    /** Creates a new instance of TDS MiantenanceOB */
    public FileManagementOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FileManagementJNDI");
            map.put(CommonConstants.HOME, "FileManagementHome");
            map.put(CommonConstants.REMOTE, "FileManagement");
            fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
//    private void fillDropdown() throws Exception{
//       lookupMap = new HashMap();
//            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
//            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
//            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//            
//            final ArrayList lookupKey = new ArrayList();
//            lookupKey.add("CUSTOMER.TITLE");
//            lookupKey.add("CUSTOMER.RESIDENTIALSTATUS");
//            
//            HashMap param = new HashMap();
//            param.put(CommonConstants.MAP_NAME,null);
//            HashMap where = new HashMap();
//            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//            param.put(CommonConstants.PARAMFORQUERY, where);
//            where = null;
//            keyValue = ClientUtil.populateLookupData(param);
//            fillData((HashMap)keyValue.get("CUSTOMER.TITLE"));
//            cbmSubmittedTo = new ComboBoxModel(key,value);           
//                       
//            fillData((HashMap)keyValue.get("CUSTOMER.TITLE"));
//            cbmapprovedBy = new ComboBoxModel(key,value);          
//     
//    }
    
    
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SUBMITTED_TO");
        lookupKey.add("APPROVED_STATUS");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        final HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("SUBMITTED_TO"));
        cbmSubmittedTo = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("APPROVED_STATUS"));
        cbmapprovedBy = new ComboBoxModel(key, value);
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
//    private void doActionPerform() throws Exception{
//        final InwardRegisterTO objInwardRegisterTO = new InwardRegisterTO();
//        final HashMap data = new HashMap();
//         data.put("COMMAND",getCommand());
//        if(get_authorizeMap() == null){
//         data.put("InwardRegister",setInwardData());
//         System.out.println("inward dataaaaaaaaaaaaa"+data);
//        }
//        else{
//        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
//        }
//        System.out.println("data in InwardRegister OB : " + data);
//        HashMap proxyResultMap = proxy.execute(data, map);
//        setProxyReturnMap(proxyResultMap);
//         System.out.println("data in InwardRegister OB return... : " + proxyResultMap);
//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
//                ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("INWARD_NO")));
//            }
//        setResult(getActionType());
//        setResult(actionType);
//    }
    
    
     private void doActionPerform() throws Exception {
        final FileManagementTO objInwardRegisterTO = new FileManagementTO();
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            data.put("FILE_MANAGEMENT_DATA", setFileData());
            System.out.println("FILE_MANAGEMENT_DATA" + data);
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FileManagement OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        System.out.println("data in FileManagement OB return... : " + proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("INWARD_NO")));
        }
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
            objFileManagementTO = (FileManagementTO)((List) data.get("FileManagementTO")).get(0);
            System.out.println("in ob...return map"+(FileManagementTO)((List) data.get("FileManagementTO")).get(0));
            populateData(objFileManagementTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void getNextInvardNo() {
         try{
            HashMap inwardMap = new HashMap();
            inwardMap.put("BRANCH_CODE",getCommand());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public FileManagementTO setFileData() {
        final FileManagementTO objFileManagementTO = new FileManagementTO();
        try {
            objFileManagementTO.setApplnDt(DateUtil.getDateMMDDYYYY(getTdtApplnDt()));
            objFileManagementTO.setApplnNo(getTxtApplnNo());
            objFileManagementTO.setFileNo(getTxtFileNo());
            objFileManagementTO.setMemberNo(getTxtMemberNo());
            objFileManagementTO.setSubmittedBy(getTxtSubmittedBy());
            objFileManagementTO.setAddress(getTxtAddress());
            objFileManagementTO.setParticulars(getTxtParticulars());
            objFileManagementTO.setRemarks(getTxtRemarks());            
            objFileManagementTO.setSubmittedTo(getCboSubmittedTo());
            objFileManagementTO.setSubmissionDt1(DateUtil.getDateMMDDYYYY(getTdtSubmissionDt1()));
            objFileManagementTO.setSubmissionDt2(DateUtil.getDateMMDDYYYY(getTdtSubmissionDt2()));
            objFileManagementTO.setSubmissionDt3(DateUtil.getDateMMDDYYYY(getTdtSubmissionDt3()));
            objFileManagementTO.setSubmissionAction1(getTxtSubmissionAction1());
            objFileManagementTO.setSubmissionAction2(getTxtSubmissionAction2());
            objFileManagementTO.setSubmissionAction3(getTxtSubmissionAction3());
            objFileManagementTO.setApprovalStatus(getCboApprovalStatus());
            objFileManagementTO.setApprovalDt1(DateUtil.getDateMMDDYYYY(getTdtApprovalDt1()));
            objFileManagementTO.setApprovalDt2(DateUtil.getDateMMDDYYYY(getTdtApprovalDt2()));
            objFileManagementTO.setApprovalDt3(DateUtil.getDateMMDDYYYY(getTdtApprovalDt3()));
            objFileManagementTO.setApprovalAction1(getTxtApprovalAction1());
            objFileManagementTO.setApprovalAction2(getTxtApprovalAction2());
            objFileManagementTO.setApprovalAction3(getTxtApprovalAction3());
            objFileManagementTO.setBranchId(TrueTransactMain.BRANCH_ID);           
            objFileManagementTO.setCreatedBy(TrueTransactMain.USER_ID);
            objFileManagementTO.setCreatedDt(currDt);            
        } catch (Exception e) {
            log.info("Error In setFileData()");
            e.printStackTrace();
        }
        return objFileManagementTO;
    }
   
    private void populateData(FileManagementTO objFileManagementTO) throws Exception{        
        this.setTdtApplnDt(CommonUtil.convertObjToStr(objFileManagementTO.getApplnDt()));
        this.setTxtApplnNo(objFileManagementTO.getApplnNo());
        this.setTxtFileNo(objFileManagementTO.getFileNo());
        this.setTxtMemberNo(objFileManagementTO.getMemberNo());
        this.setTxtSubmittedBy(objFileManagementTO.getSubmittedBy());
        this.setTxtAddress(objFileManagementTO.getAddress());
        this.setTxtParticulars(objFileManagementTO.getParticulars());
        this.setTxtRemarks(objFileManagementTO.getRemarks());
        this.setCboSubmittedTo((String) getCbmSubmittedTo().getDataForKey(CommonUtil.convertObjToStr(objFileManagementTO.getSubmittedTo())));
        this.setTdtSubmissionDt1(CommonUtil.convertObjToStr(objFileManagementTO.getSubmissionDt1()));
        this.setTdtSubmissionDt2(CommonUtil.convertObjToStr(objFileManagementTO.getSubmissionDt2()));
        this.setTdtSubmissionDt3(CommonUtil.convertObjToStr(objFileManagementTO.getSubmissionDt3()));
        this.setTxtSubmissionAction1(objFileManagementTO.getSubmissionAction1());
        this.setTxtSubmissionAction2(objFileManagementTO.getSubmissionAction2());
        this.setTxtSubmissionAction3(objFileManagementTO.getSubmissionAction3());
        this.setCboApprovalStatus((String) getCbmapprovedBy().getDataForKey(CommonUtil.convertObjToStr(objFileManagementTO.getApprovalStatus())));
        this.setTdtApprovalDt1(CommonUtil.convertObjToStr(objFileManagementTO.getApprovalDt1()));
        this.setTdtApprovalDt2(CommonUtil.convertObjToStr(objFileManagementTO.getApprovalDt2()));
        this.setTdtApprovalDt3(CommonUtil.convertObjToStr(objFileManagementTO.getApprovalDt3()));
        this.setTxtApprovalAction1(objFileManagementTO.getApprovalAction1());
        this.setTxtApprovalAction2(objFileManagementTO.getApprovalAction2());
        this.setTxtApprovalAction3(objFileManagementTO.getApprovalAction3());
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm() {
        setTdtApplnDt("");
        setTxtApplnNo("");
        setTxtFileNo("");
        setTxtMemberNo("");
        setTxtSubmittedBy("");
        setTxtAddress("");
        setTxtParticulars("");
        setTxtRemarks("");
        setCboSubmittedTo("");
        setTdtSubmissionDt1("");
        setTdtSubmissionDt2("");
        setTdtSubmissionDt3("");
        setTxtSubmissionAction1("");
        setTxtSubmissionAction2("");
        setTxtSubmissionAction3("");
        setCboApprovalStatus("");
        setTdtApprovalDt1("");
        setTdtApprovalDt2("");
        setTdtApprovalDt3("");
        setTxtApprovalAction1("");
        setTxtApprovalAction2("");
        setTxtApprovalAction3("");
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
     * Getter for property txaPurposeofVisit.
     * @return Value of property txaPurposeofVisit.
     */
    public String getTxaRemarks() {
        return txaRemarks;
    }
    
    /**
     * Setter for property txaPurposeofVisit.
     * @param txaPurposeofVisit New value of property txaPurposeofVisit.
     */
    public void setTxaRemarks(String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }
    
    /**
     * Getter for property txaCommentsLeft.
     * @return Value of property txaCommentsLeft.
     */
    public String getTxaDetails() {
        return txaDetails;
    }
    
    /**
     * Setter for property txaCommentsLeft.
     * @param txaCommentsLeft New value of property txaCommentsLeft.
     */
    public void setTxaDetails(String txaDetails) {
        this.txaDetails = txaDetails;
    }
    
    /**
     * Getter for property txtDateofVisit.
     * @return Value of property txtDateofVisit.
     */
    public String getTxtDate() {
        return txtDate;
    }    

    /**
     * Setter for property txtDateofVisit.
     * @param txtDateofVisit New value of property txtDateofVisit.
     */
    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }    
    
    /**
     * Getter for property txtSubmittedBy.
     * @return Value of property txtSubmittedBy.
     */
    public String getTxtSubmittedBy() {
        return txtSubmittedBy;
    }
    
    /**
     * Setter for property txtSubmittedBy.
     * @param txtSubmittedBy New value of property txtSubmittedBy.
     */
    public void setTxtSubmittedBy(String txtSubmittedBy) {
        this.txtSubmittedBy = txtSubmittedBy;
    }
    
    /**
     * Getter for property txtReferenceNo.
     * @return Value of property txtReferenceNo.
     */
    public String getTxtReferenceNo() {
        return txtReferenceNo;
    }
    
    /**
     * Setter for property txtReferenceNo.
     * @param txtReferenceNo New value of property txtReferenceNo.
     */
    public void setTxtReferenceNo(String txtReferenceNo) {
        this.txtReferenceNo = txtReferenceNo;
    }
    
    /**
     * Getter for property txtActionTaken.
     * @return Value of property txtActionTaken.
     */
    public String getTxtActionTaken() {
        return txtActionTaken;
    }
    
    /**
     * Setter for property txtActionTaken.
     * @param txtActionTaken New value of property txtActionTaken.
     */
    public void setTxtActionTaken(String txtActionTaken) {
        this.txtActionTaken = txtActionTaken;
    }
    
    /**
     * Getter for property txtInwardNo.
     * @return Value of property txtInwardNo.
     */
    public String getTxtInwardNo() {
        return txtInwardNo;
    }
    
    /**
     * Setter for property txtInwardNo.
     * @param txtInwardNo New value of property txtInwardNo.
     */
    public void setTxtInwardNo(String txtInwardNo) {
        this.txtInwardNo = txtInwardNo;
    }

    public String getCboApprovalStatus() {
        return cboApprovalStatus;
    }

    public void setCboApprovalStatus(String cboApprovalStatus) {
        this.cboApprovalStatus = cboApprovalStatus;
    }


    public String getCboSubmittedTo() {
        return cboSubmittedTo;
    }

    public void setCboSubmittedTo(String cboSubmittedTo) {
        this.cboSubmittedTo = cboSubmittedTo;
    }

    public String getTdtApplnDt() {
        return tdtApplnDt;
    }

    public void setTdtApplnDt(String tdtApplnDt) {
        this.tdtApplnDt = tdtApplnDt;
    }

    public String getTdtApprovalDt1() {
        return tdtApprovalDt1;
    }

    public void setTdtApprovalDt1(String tdtApprovalDt1) {
        this.tdtApprovalDt1 = tdtApprovalDt1;
    }

    public String getTdtApprovalDt2() {
        return tdtApprovalDt2;
    }

    public void setTdtApprovalDt2(String tdtApprovalDt2) {
        this.tdtApprovalDt2 = tdtApprovalDt2;
    }

    public String getTdtApprovalDt3() {
        return tdtApprovalDt3;
    }

    public void setTdtApprovalDt3(String tdtApprovalDt3) {
        this.tdtApprovalDt3 = tdtApprovalDt3;
    }

    public String getTdtSubmissionDt1() {
        return tdtSubmissionDt1;
    }

    public void setTdtSubmissionDt1(String tdtSubmissionDt1) {
        this.tdtSubmissionDt1 = tdtSubmissionDt1;
    }

    public String getTdtSubmissionDt2() {
        return tdtSubmissionDt2;
    }

    public void setTdtSubmissionDt2(String tdtSubmissionDt2) {
        this.tdtSubmissionDt2 = tdtSubmissionDt2;
    }

    public String getTdtSubmissionDt3() {
        return tdtSubmissionDt3;
    }

    public void setTdtSubmissionDt3(String tdtSubmissionDt3) {
        this.tdtSubmissionDt3 = tdtSubmissionDt3;
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtApplnNo() {
        return txtApplnNo;
    }

    public void setTxtApplnNo(String txtApplnNo) {
        this.txtApplnNo = txtApplnNo;
    }

    public String getTxtApprovalAction1() {
        return txtApprovalAction1;
    }

    public void setTxtApprovalAction1(String txtApprovalAction1) {
        this.txtApprovalAction1 = txtApprovalAction1;
    }

    public String getTxtApprovalAction2() {
        return txtApprovalAction2;
    }

    public void setTxtApprovalAction2(String txtApprovalAction2) {
        this.txtApprovalAction2 = txtApprovalAction2;
    }

    public String getTxtApprovalAction3() {
        return txtApprovalAction3;
    }

    public void setTxtApprovalAction3(String txtApprovalAction3) {
        this.txtApprovalAction3 = txtApprovalAction3;
    }

    public String getTxtFileNo() {
        return txtFileNo;
    }

    public void setTxtFileNo(String txtFileNo) {
        this.txtFileNo = txtFileNo;
    }

    public String getTxtMemberNo() {
        return txtMemberNo;
    }

    public void setTxtMemberNo(String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }

    public String getTxtParticulars() {
        return txtParticulars;
    }

    public void setTxtParticulars(String txtParticulars) {
        this.txtParticulars = txtParticulars;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public String getTxtSubmissionAction1() {
        return txtSubmissionAction1;
    }

    public void setTxtSubmissionAction1(String txtSubmissionAction1) {
        this.txtSubmissionAction1 = txtSubmissionAction1;
    }

    public String getTxtSubmissionAction2() {
        return txtSubmissionAction2;
    }

    public void setTxtSubmissionAction2(String txtSubmissionAction2) {
        this.txtSubmissionAction2 = txtSubmissionAction2;
    }

    public String getTxtSubmissionAction3() {
        return txtSubmissionAction3;
    }

    public void setTxtSubmissionAction3(String txtSubmissionAction3) {
        this.txtSubmissionAction3 = txtSubmissionAction3;
    }

    public ComboBoxModel getCbmSubmittedTo() {
        return cbmSubmittedTo;
    }

    public void setCbmSubmittedTo(ComboBoxModel cbmSubmittedTo) {
        this.cbmSubmittedTo = cbmSubmittedTo;
    }

    public ComboBoxModel getCbmapprovedBy() {
        return cbmapprovedBy;
    }

    public void setCbmapprovedBy(ComboBoxModel cbmapprovedBy) {
        this.cbmapprovedBy = cbmapprovedBy;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
   
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
  
    
    }
    
  
    
   