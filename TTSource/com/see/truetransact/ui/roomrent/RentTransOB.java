/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RentTransOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.roomrent;

import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.roomrent.renttrans.RentTransTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import com.see.truetransact.ui.common.transaction.TransactionOB ;

/**
 *
 * @author  user
 */
public class RentTransOB extends CObservable {
    
    private final static Logger log = Logger.getLogger(RentTransOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static RentTransOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private HashMap authMap = new HashMap();
    private String cboBuildingNo="",cboRoomNo="",cbClosure="",rtId="",txtRoomStatus="",txtRmNumber="",cbDefaulter="",txtAcNumber="",accStatus;
    private Double txtDueAmt,txtRentAmt,txtPenelAmt,txtNoticeAmt,txtLegalAmt,txtArbAmt,txtCourtAmt,txtExeAmt,txtRentTotal;
 
    Date currDt = null,tdtRentDate=null,tdtRentPFrm=null,tdtRentPto=null,tdtClosedDate=null,tdtRentCDate=null;
    Date txtTransDate=null;
    private String lblServiceTaxval = "";
    private HashMap serviceTax_Map = null;

    public Date getTxtTransDate() {
        return txtTransDate;
    }

    public void setTxtTransDate(Date txtTransDate) {
        this.txtTransDate = txtTransDate;
    }
    double amtBorrowedMaster=0.0;
    String multiDis="";
double avalbalBorrowedMaster=0.0;
    public static RentTransOB getInstance()throws Exception{
        return objOB;
    }
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        // System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
    public void setStatus(){
        // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    /** Creates a new instance of NewBorrowingOB */
    public RentTransOB() {
        
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            currDt = ClientUtil.getCurrentDate();
            //initUIComboBoxModel();
            // fillDropdown();
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
    static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new RentTransOB();
        } catch(Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():"+e);
        }
    }
    /** Resets all the UI Fields */
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "RentTransJNDI");
        map.put(CommonConstants.HOME, "renttrans.RentTransHome");
        map.put(CommonConstants.REMOTE, "renttrans.RentTrans");
        
        
    }
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            System.out.println("whereMap=="+whereMap);
            System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+ mapData);
            RentTransTO objTO =(RentTransTO) ((List) mapData.get("RentTransTO")).get(0);
            System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            // setBorrData(mapData);
            setRentTransTO(objTO);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                
               //  System.out.println("IN DISBURASALLLLLLLLLLLLLLLLLLLLLLLLLLLLL=="+list); 
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                    transactionOB.setTxtTransactionAmt(String.valueOf(objTO.getRentTotal()));
                }
            }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
        
    }
  
    
    private void setRentTransTO(RentTransTO objTO){
        
        setRtId(objTO.getRrId());
        setCboBuildingNo(objTO.getBuildingNo());
        setCboRoomNo(objTO.getRoomNo());
        setCbClosure(objTO.getClosure());
        setTxtDueAmt(objTO.getDueAmt());
        setTxtRentAmt(objTO.getRentAmt());
        setTxtPenelAmt(objTO.getPenelAmt());
        setTxtNoticeAmt(objTO.getNoticeAmt());
        setTxtLegalAmt(objTO.getLegalAmt());
        setTxtArbAmt(objTO.getArbAmt());
        setTxtCourtAmt(objTO.getCourtAmt());
        setTxtExeAmt(objTO.getExeAmt());
        setTdtRentDate(objTO.getRentDate());
        setTxtTransDate(objTO.getTransDate()); 
        setTdtRentPFrm(objTO.getRentPFrm());
        setTdtRentPto(objTO.getRentPto());
        setTdtClosedDate(objTO.getClosedDate());
        setTxtRentTotal(objTO.getRentTotal());
        setTdtRentCDate(objTO.getRentCDate());
        notifyObservers();
    }
    private RentTransTO getRentTransTO(String command){
        RentTransTO objTO = new RentTransTO();
        objTO.setCommand(command);
        //objTO.setDisbursalNo(getDisbursalNo());
        objTO.setBuildingNo(getCboBuildingNo());
        objTO.setRoomNo(getCboRoomNo());
        objTO.setRrId(getRtId());
        objTO.setClosure(getCbClosure());
        objTO.setRentDate(getTdtRentDate());
        objTO.setRentCDate(getTdtRentCDate());
        objTO.setTransDate(getTxtTransDate());
        objTO.setRentPFrm(getTdtRentPFrm());
        objTO.setRentPto(getTdtRentPto());
        objTO.setClosedDate(getTdtClosedDate());
        objTO.setDueAmt(getTxtDueAmt());
        objTO.setRentAmt(getTxtRentAmt());
        objTO.setPenelAmt(getTxtPenelAmt());
        objTO.setNoticeAmt(getTxtNoticeAmt());
        objTO.setLegalAmt(getTxtLegalAmt());
        objTO.setArbAmt(getTxtArbAmt());
        objTO.setCourtAmt(getTxtCourtAmt());
        objTO.setExeAmt(getTxtExeAmt());
        objTO.setRentTotal(getTxtRentTotal());
        objTO.setRoomStatus(getTxtRoomStatus());
        objTO.setRmNumber(getTxtRmNumber());
        objTO.setDefaulter(getCbDefaulter());
        objTO.setAccNumber(getTxtAcNumber());
        objTO.setAccStatus(getAccStatus());
        objTO.setStatusBy(ProxyParameters.USER_ID);
        objTO.setStatusDate(currDt);
        //  objTO.setBranchId("0001");
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
//            if (getCbDefaulter().equals("YES")) {
//                objTO.setStatus("DEFAULTER");
//            } else {
                objTO.setStatus("CREATED");
//            }
        }
        
        return objTO;
    }
    public void resetForm(){
        setRtId("");
        setCboBuildingNo("");
        setCboRoomNo("");
        setCbClosure("");
        setTxtDueAmt(null);
        setTxtRentAmt(null);
        setTxtPenelAmt(null);
        setTxtNoticeAmt(null);
        setTxtLegalAmt(null);
        setTxtArbAmt(null);
        setTxtCourtAmt(null);
        setTxtExeAmt(null);
        setTdtRentDate(null);
        setTdtRentCDate(null);
        setTxtTransDate(null);
        setTdtRentPFrm(null);
        setTdtRentPto(null);
        setTdtClosedDate(null);
        setTxtRentTotal(null);
        setCbDefaulter(null);
        setTxtAcNumber(null);
        setAccStatus(null);
        setServiceTax_Map(null);
        setLblServiceTaxval("");
        notifyObservers();
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    public int getResult(){
        return _result;
    }
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
   
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if(!command.equals(CommonConstants.AUTHORIZESTATUS)){
            term.put("RentTransTO", getRentTransTO(command));
            if (transactionDetailsTO == null)
                transactionDetailsTO = new LinkedHashMap();
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            term.put("TransactionTO",transactionDetailsTO);
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                term.put("serviceTaxDetails", getServiceTax_Map());
                term.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
           }
            if(getAuthMap() != null && getAuthMap().size() > 0 ){
                    if( getAuthMap() != null){
                        term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                    }
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                        if (transactionDetailsTO == null){
                            transactionDetailsTO = new LinkedHashMap();
                        }
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                        term.put("TransactionTO",transactionDetailsTO);
                        allowedTransactionDetailsTO = null;
                    }
                    authMap = null;
                }
            HashMap proxyReturnMap = proxy.execute(term, map);
            System.out.println("proxyReturnMap in RT=="+proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():"+e);
        }
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
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
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property currDt.
     * @return Value of property currDt.
     */
    public java.util.Date getCurrDt() {
        return currDt;
    }
    
    /**
     * Setter for property currDt.
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }
    
    /**
     * Getter for property authMap.
     * @return Value of property authMap.
     */
    public java.util.HashMap getAuthMap() {
        return authMap;
    }
    
    /**
     * Setter for property authMap.
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }
    
    /**
     * Getter for property cboBuildingNo.
     * @return Value of property cboBuildingNo.
     */
    public java.lang.String getCboBuildingNo() {
        return cboBuildingNo;
    }    

    /**
     * Setter for property cboBuildingNo.
     * @param cboBuildingNo New value of property cboBuildingNo.
     */
    public void setCboBuildingNo(java.lang.String cboBuildingNo) {
        this.cboBuildingNo = cboBuildingNo;
    }    
    
    /**
     * Getter for property cboRoomNo.
     * @return Value of property cboRoomNo.
     */
    public java.lang.String getCboRoomNo() {
        return cboRoomNo;
    }
    
    /**
     * Setter for property cboRoomNo.
     * @param cboRoomNo New value of property cboRoomNo.
     */
    public void setCboRoomNo(java.lang.String cboRoomNo) {
        this.cboRoomNo = cboRoomNo;
    }
    
    /**
     * Getter for property cbClosure.
     * @return Value of property cbClosure.
     */
    public java.lang.String getCbClosure() {
        return cbClosure;
    }
    
    /**
     * Setter for property cbClosure.
     * @param cbClosure New value of property cbClosure.
     */
    public void setCbClosure(java.lang.String cbClosure) {
        this.cbClosure = cbClosure;
    }
    
    /**
     * Getter for property txtDueAmt.
     * @return Value of property txtDueAmt.
     */
    public Double getTxtDueAmt() {
        return txtDueAmt;
    }
    
    /**
     * Setter for property txtDueAmt.
     * @param txtDueAmt New value of property txtDueAmt.
     */
    public void setTxtDueAmt(Double txtDueAmt) {
        this.txtDueAmt = txtDueAmt;
    }
    
    /**
     * Getter for property txtRentAmt.
     * @return Value of property txtRentAmt.
     */
    public Double getTxtRentAmt() {
        return txtRentAmt;
    }
    
    /**
     * Setter for property txtRentAmt.
     * @param txtRentAmt New value of property txtRentAmt.
     */
    public void setTxtRentAmt(Double txtRentAmt) {
        this.txtRentAmt = txtRentAmt;
    }
    
    /**
     * Getter for property txtPenelAmt.
     * @return Value of property txtPenelAmt.
     */
    public Double getTxtPenelAmt() {
        return txtPenelAmt;
    }
    
    /**
     * Setter for property txtPenelAmt.
     * @param txtPenelAmt New value of property txtPenelAmt.
     */
    public void setTxtPenelAmt(Double txtPenelAmt) {
        this.txtPenelAmt = txtPenelAmt;
    }
    
    /**
     * Getter for property txtNoticeAmt.
     * @return Value of property txtNoticeAmt.
     */
    public Double getTxtNoticeAmt() {
        return txtNoticeAmt;
    }
    
    /**
     * Setter for property txtNoticeAmt.
     * @param txtNoticeAmt New value of property txtNoticeAmt.
     */
    public void setTxtNoticeAmt(Double txtNoticeAmt) {
        this.txtNoticeAmt = txtNoticeAmt;
    }
    
    /**
     * Getter for property txtLegalAmt.
     * @return Value of property txtLegalAmt.
     */
    public Double getTxtLegalAmt() {
        return txtLegalAmt;
    }
    
    /**
     * Setter for property txtLegalAmt.
     * @param txtLegalAmt New value of property txtLegalAmt.
     */
    public void setTxtLegalAmt(Double txtLegalAmt) {
        this.txtLegalAmt = txtLegalAmt;
    }
    
    /**
     * Getter for property txtArbAmt.
     * @return Value of property txtArbAmt.
     */
    public Double getTxtArbAmt() {
        return txtArbAmt;
    }
    
    /**
     * Setter for property txtArbAmt.
     * @param txtArbAmt New value of property txtArbAmt.
     */
    public void setTxtArbAmt(Double txtArbAmt) {
        this.txtArbAmt = txtArbAmt;
    }
    
    /**
     * Getter for property txtCourtAmt.
     * @return Value of property txtCourtAmt.
     */
    public Double getTxtCourtAmt() {
        return txtCourtAmt;
    }
    
    /**
     * Setter for property txtCourtAmt.
     * @param txtCourtAmt New value of property txtCourtAmt.
     */
    public void setTxtCourtAmt(Double txtCourtAmt) {
        this.txtCourtAmt = txtCourtAmt;
    }
    
    /**
     * Getter for property txtExeAmt.
     * @return Value of property txtExeAmt.
     */
    public Double getTxtExeAmt() {
        return txtExeAmt;
    }
    
    /**
     * Setter for property txtExeAmt.
     * @param txtExeAmt New value of property txtExeAmt.
     */
    public void setTxtExeAmt(Double txtExeAmt) {
        this.txtExeAmt = txtExeAmt;
    }
    
    /**
     * Getter for property tdtRentDate.
     * @return Value of property tdtRentDate.
     */
    public java.util.Date getTdtRentDate() {
        return tdtRentDate;
    }
    
    /**
     * Setter for property tdtRentDate.
     * @param tdtRentDate New value of property tdtRentDate.
     */
    public void setTdtRentDate(java.util.Date tdtRentDate) {
        this.tdtRentDate = tdtRentDate;
    }
    
    /**
     * Getter for property tdtTransDate.
     * @return Value of property tdtTransDate.
     */
  
    
    /**
     * Setter for property tdtTransDate.
     * @param tdtTransDate New value of property tdtTransDate.
     */
  
    
    /**
     * Getter for property tdtRentPFrm.
     * @return Value of property tdtRentPFrm.
     */
    public java.util.Date getTdtRentPFrm() {
        return tdtRentPFrm;
    }
    
    /**
     * Setter for property tdtRentPFrm.
     * @param tdtRentPFrm New value of property tdtRentPFrm.
     */
    public void setTdtRentPFrm(java.util.Date tdtRentPFrm) {
        this.tdtRentPFrm = tdtRentPFrm;
    }
    
    /**
     * Getter for property tdtRentPto.
     * @return Value of property tdtRentPto.
     */
    public java.util.Date getTdtRentPto() {
        return tdtRentPto;
    }
    
    /**
     * Setter for property tdtRentPto.
     * @param tdtRentPto New value of property tdtRentPto.
     */
    public void setTdtRentPto(java.util.Date tdtRentPto) {
        this.tdtRentPto = tdtRentPto;
    }
    
    /**
     * Getter for property tdtClosedDate.
     * @return Value of property tdtClosedDate.
     */
    public java.util.Date getTdtClosedDate() {
        return tdtClosedDate;
    }
    
    /**
     * Setter for property tdtClosedDate.
     * @param tdtClosedDate New value of property tdtClosedDate.
     */
    public void setTdtClosedDate(java.util.Date tdtClosedDate) {
        this.tdtClosedDate = tdtClosedDate;
    }
    
    /**
     * Getter for property rtId.
     * @return Value of property rtId.
     */
    public java.lang.String getRtId() {
        return rtId;
    }
    
    /**
     * Setter for property rtId.
     * @param rtId New value of property rtId.
     */
    public void setRtId(java.lang.String rtId) {
        this.rtId = rtId;
    }
    
    /**
     * Getter for property txtRentTotal.
     * @return Value of property txtRentTotal.
     */
    public java.lang.Double getTxtRentTotal() {
        return txtRentTotal;
    }
    
    /**
     * Setter for property txtRentTotal.
     * @param txtRentTotal New value of property txtRentTotal.
     */
    public void setTxtRentTotal(java.lang.Double txtRentTotal) {
        this.txtRentTotal = txtRentTotal;
    }
    
    /**
     * Getter for property tdtRentCDate.
     * @return Value of property tdtRentCDate.
     */
    public java.util.Date getTdtRentCDate() {
        return tdtRentCDate;
    }
    
    /**
     * Setter for property tdtRentCDate.
     * @param tdtRentCDate New value of property tdtRentCDate.
     */
    public void setTdtRentCDate(java.util.Date tdtRentCDate) {
        this.tdtRentCDate = tdtRentCDate;
    }
    
    /**
     * Getter for property txtRoomStatus.
     * @return Value of property txtRoomStatus.
     */
    public String getTxtRoomStatus() {
        return txtRoomStatus;
    }    
    
    /**
     * Setter for property txtRoomStatus.
     * @param txtRoomStatus New value of property txtRoomStatus.
     */
    public void setTxtRoomStatus(String txtRoomStatus) {
        this.txtRoomStatus = txtRoomStatus;
    }    
  
    /**
     * Getter for property txtRmNumber.
     * @return Value of property txtRmNumber.
     */
    public String getTxtRmNumber() {
        return txtRmNumber;
    }    
    
    /**
     * Setter for property txtRmNumber.
     * @param txtRmNumber New value of property txtRmNumber.
     */
    public void setTxtRmNumber(String txtRmNumber) {
        this.txtRmNumber = txtRmNumber;
    }

    public String getCbDefaulter() {
        return cbDefaulter;
    }

    public void setCbDefaulter(String cbDefaulter) {
        this.cbDefaulter = cbDefaulter;
    }

    public String getTxtAcNumber() {
        return txtAcNumber;
    }

    public void setTxtAcNumber(String txtAcNumber) {
        this.txtAcNumber = txtAcNumber;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }
    
     public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
  
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
     public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setCommand("INSERT");
            objservicetaxDetTo.setStatus("CREATED");
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getCboRoomNo());
            objservicetaxDetTo.setParticulars("Loan Closing");

            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDt);
            objservicetaxDetTo.setTrans_type("C");
           
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDt);
            
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
}
