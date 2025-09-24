/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSLetterGenerationOB.java
 *
 * 
 */

package com.see.truetransact.ui.mdsapplication.mdslettergeneration;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
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
import com.see.truetransact.transferobject.mdsapplication.mdslettergeneration.MDSLetterGenerationTO;

/**
 *
 * @author Suresh
 */

public class MDSLetterGenerationOB extends CObservable{
    
    private String lblLettorNoVal = "";
    private String txtSchemeName = "";
    private String txtChittalNo = "";
    private String txtAuctionAmount = "";
    private String tdtFromDt = "";
    private String tdtToDt = "";
    private String lblLettorNoVal1 = "";
    private String txtSchemeName1 = "";
    private String txtChittalNo1 = "";
    private String txtDiscountUpto = "";
    private Date tdtConductedOnDt = null;
    private String txtNoOfConsecMonth = "";
    private Date tdtValidUpto = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private int actionType;
    private final static Logger log = Logger.getLogger(MDSLetterGenerationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private MDSLetterGenerationTO objMDSLettorGenerationTO;
    private String chkCancel = "";
    private Date tdtCancelDt = null;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public MDSLetterGenerationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSLetterGenerationJNDI");
            map.put(CommonConstants.HOME, "mdsapplication.mdslettergeneration.MDSLetterGenerationHome");
            map.put(CommonConstants.REMOTE, "mdsapplication.mdslettergeneration.MDSLetterGeneration");
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            if(data.containsKey("MDSLetterGenerationTO")){
                objMDSLettorGenerationTO = (MDSLetterGenerationTO) ((List) data.get("MDSLetterGenerationTO")).get(0);
                populateData(objMDSLettorGenerationTO);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateData(MDSLetterGenerationTO objPrizedTO) throws Exception{        
        this.setLblLettorNoVal(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getLettorNo()));
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getSchemeName()));
        this.setTxtChittalNo(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getChittalNo()));
        this.setTxtAuctionAmount(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getAuctionAmount()));
        this.setTdtFromDt(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getFromDt()));
        this.setTdtToDt(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getToDt()));
       // this.setLblLettorNoVal1(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getLettorNo()));
       // this.setTxtSchemeName1(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getSchemeName()));
      //  this.setTxtChittalNo1(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getChittalNo()));
        this.setTxtDiscountUpto(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getDiscountUpto()));
        this.setTdtConductedOnDt(objMDSLettorGenerationTO.getConductedOnDt());
        this.setTdtValidUpto(objMDSLettorGenerationTO.getValidUpto());
        this.setTxtNoOfConsecMonth(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getNoOfConsecMonth()));
        this.setChkCancel(CommonUtil.convertObjToStr(objMDSLettorGenerationTO.getCancel()));
        this.setTdtCancelDt(objMDSLettorGenerationTO.getCancelDt());
        setChanged();
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        data.put("MDSLetterGeneration",setLettorDetailsTO());
        HashMap proxyResultMap = proxy.execute(data, map);
        setResult(getActionType());
    }
    
    /** To populate data into the screen */
    public MDSLetterGenerationTO setLettorDetailsTO() {
        final MDSLetterGenerationTO objMDSLettorGenerationTO = new MDSLetterGenerationTO();
        try{
            objMDSLettorGenerationTO.setLettorNo(getLblLettorNoVal());
            objMDSLettorGenerationTO.setSchemeName(getTxtSchemeName());
            objMDSLettorGenerationTO.setChittalNo(getTxtChittalNo());
            objMDSLettorGenerationTO.setAuctionAmount(CommonUtil.convertObjToDouble(getTxtAuctionAmount()));
            objMDSLettorGenerationTO.setFromDt(DateUtil.getDateMMDDYYYY(getTdtFromDt()));
            objMDSLettorGenerationTO.setToDt(DateUtil.getDateMMDDYYYY(getTdtToDt()));
            objMDSLettorGenerationTO.setStatus(getAction());
            objMDSLettorGenerationTO.setStatusBy(TrueTransactMain.USER_ID);
            objMDSLettorGenerationTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
           // if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
            //objMDSLettorGenerationTO.setChittalNo1(getTxtChittalNo1());
            objMDSLettorGenerationTO.setConductedOnDt(getTdtConductedOnDt());
            objMDSLettorGenerationTO.setDiscountUpto(CommonUtil.convertObjToDouble(getTxtDiscountUpto()));
            //objMDSLettorGenerationTO.setLettorNoVal1(getLblLettorNoVal1());
            objMDSLettorGenerationTO.setNoOfConsecMonth(CommonUtil.convertObjToInt(getTxtNoOfConsecMonth()));
           // objMDSLettorGenerationTO.setSchemeName1(getTxtSchemeName1());
            objMDSLettorGenerationTO.setValidUpto(getTdtValidUpto());
            objMDSLettorGenerationTO.setCancel(getChkCancel());
            objMDSLettorGenerationTO.setCancelDt(getTdtCancelDt());
          //  }
        }catch(Exception e){
            log.info("Error In setLettorDetailsTO()");
            e.printStackTrace();
        }
        return objMDSLettorGenerationTO;
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
    
    public void resetForm() {
        setLblLettorNoVal("");
        setTxtSchemeName(""); 
        setTxtChittalNo(""); 
        setTxtAuctionAmount(""); 
        setTdtFromDt(""); 
        setTdtToDt(""); 
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
     * Getter for property txtSchemeName.
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }
    
    /**
     * Setter for property txtSchemeName.
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }
    
    /**
     * Getter for property txtChittalNo.
     * @return Value of property txtChittalNo.
     */
    public java.lang.String getTxtChittalNo() {
        return txtChittalNo;
    }
    
    /**
     * Setter for property txtChittalNo.
     * @param txtChittalNo New value of property txtChittalNo.
     */
    public void setTxtChittalNo(java.lang.String txtChittalNo) {
        this.txtChittalNo = txtChittalNo;
    }
    
    /**
     * Getter for property txtAuctionAmount.
     * @return Value of property txtAuctionAmount.
     */
    public java.lang.String getTxtAuctionAmount() {
        return txtAuctionAmount;
    }
    
    /**
     * Setter for property txtAuctionAmount.
     * @param txtAuctionAmount New value of property txtAuctionAmount.
     */
    public void setTxtAuctionAmount(java.lang.String txtAuctionAmount) {
        this.txtAuctionAmount = txtAuctionAmount;
    }
    
    /**
     * Getter for property tdtFromDt.
     * @return Value of property tdtFromDt.
     */
    public java.lang.String getTdtFromDt() {
        return tdtFromDt;
    }
    
    /**
     * Setter for property tdtFromDt.
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(java.lang.String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }
    
    /**
     * Getter for property tdtToDt.
     * @return Value of property tdtToDt.
     */
    public java.lang.String getTdtToDt() {
        return tdtToDt;
    }
    
    /**
     * Setter for property tdtToDt.
     * @param tdtToDt New value of property tdtToDt.
     */
    public void setTdtToDt(java.lang.String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }
    
    /**
     * Getter for property lblLettorNoVal.
     * @return Value of property lblLettorNoVal.
     */
    public java.lang.String getLblLettorNoVal() {
        return lblLettorNoVal;
    }
    
    /**
     * Setter for property lblLettorNoVal.
     * @param lblLettorNoVal New value of property lblLettorNoVal.
     */
    public void setLblLettorNoVal(java.lang.String lblLettorNoVal) {
        this.lblLettorNoVal = lblLettorNoVal;
    }
    
    public String getLblLettorNoVal1() {
        return lblLettorNoVal1;
    }

    public void setLblLettorNoVal1(String lblLettorNoVal1) {
        this.lblLettorNoVal1 = lblLettorNoVal1;
    }

    public String getTxtSchemeName1() {
        return txtSchemeName1;
    }

    public void setTxtSchemeName1(String txtSchemeName1) {
        this.txtSchemeName1 = txtSchemeName1;
    }

    public String getTxtChittalNo1() {
        return txtChittalNo1;
    }

    public void setTxtChittalNo1(String txtChittalNo1) {
        this.txtChittalNo1 = txtChittalNo1;
    }

    public String getTxtDiscountUpto() {
        return txtDiscountUpto;
    }

    public void setTxtDiscountUpto(String txtDiscountUpto) {
        this.txtDiscountUpto = txtDiscountUpto;
    }

    public Date getTdtConductedOnDt() {
        return tdtConductedOnDt;
    }

    public void setTdtConductedOnDt(Date tdtConductedOnDt) {
        this.tdtConductedOnDt = tdtConductedOnDt;
    }

    public String getTxtNoOfConsecMonth() {
        return txtNoOfConsecMonth;
    }

    public void setTxtNoOfConsecMonth(String txtNoOfConsecMonth) {
        this.txtNoOfConsecMonth = txtNoOfConsecMonth;
    }

    public Date getTdtValidUpto() {
        return tdtValidUpto;
    }

    public void setTdtValidUpto(Date tdtValidUpto) {
        this.tdtValidUpto = tdtValidUpto;
    }
        public String getChkCancel() {
        return chkCancel;
    }

    public void setChkCancel(String chkCancel) {
        this.chkCancel = chkCancel;
    }

    public Date getTdtCancelDt() {
        return tdtCancelDt;
    }

    public void setTdtCancelDt(Date tdtCancelDt) {
        this.tdtCancelDt = tdtCancelDt;
    }
    
}