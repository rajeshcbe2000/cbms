/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DisbursalOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.borrowings;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB ;

/**
 *
 * @author  user
 */
public class DisbursalOB extends CObservable {
    
    private final static Logger log = Logger.getLogger(DisbursalOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static DisbursalOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    //    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
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
    private String txtBorrowingNo = "",txtAgency="",txtDisbursalNo="",
    txtBorrowingRefNo="",txtType="",txtaDescription="",
    txtPrinRepFrq="",
    txtIntRepFrq="",txtMorotorium="";
    Date tdtDateSanctioned=null,tdtDateExpiry=null;
    Double txtAmtSanctioned, txtAmtBorrowed,txtRateInterest,txtnoofInstall;
    Date currDt = null;
    double amtBorrowedMaster=0.0;
    String multiDis="";
    private EnhancedTableModel tblCheckBookTable;
    private ArrayList IncVal = new ArrayList();
    private HashMap finalMap=new HashMap();

  
    private HashMap mapData=null;
    double avalbalBorrowedMaster=0.0;
    final ArrayList tableTitle = new ArrayList();
    public static DisbursalOB getInstance()throws Exception{
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
    public DisbursalOB() {
        
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            currDt = ClientUtil.getCurrentDate();
             setTableTile();
            tblCheckBookTable = new EnhancedTableModel(null, tableTitle);
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
            objOB = new DisbursalOB();
        } catch(Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():"+e);
        }
    }
      private void setTableTile() throws Exception{
        tableTitle.add("Sl No");
        tableTitle.add("Borrowing Ref No");
        tableTitle.add("Borr.No");
        tableTitle.add("Amount");
        
        IncVal = new ArrayList();
    }
    /** Resets all the UI Fields */
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BorrowingDisbursalJNDI");
        map.put(CommonConstants.HOME, "borrowingDisbursal.BorrwingDisbursalHome");
        map.put(CommonConstants.REMOTE, "borrowingDisbursal.BorrwingDisbursal");
        
        
    }
     public void insertTable(ArrayList tableList){
        try{
        ArrayList aList=new ArrayList();
        if(tableList!=null){
        for(int i=0;i<tableList.size();i++){
            ArrayList eachList=new ArrayList();
            ArrayList eachListadd=new ArrayList();
            eachList=(ArrayList)tableList.get(i);
            System.out.println("eachList"+eachList);
            eachListadd.add(i+1);
            eachListadd.add((String)eachList.get(0));
            eachListadd.add((String)eachList.get(1));
            eachListadd.add((String)eachList.get(2));
            aList.add(eachListadd);
        }
        }
            System.out.println("aList"+aList);
        tblCheckBookTable=new EnhancedTableModel((ArrayList)aList,tableTitle);
        notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
      public void setTableData(HashMap mapData) {
          // BorrowingDisbursalTO objTO =(BorrowingDisbursalTO) ((List) mapData.get("BorrowingDisbursalTO")).get(0);
          try{
              HashMap newMap=new HashMap();
        List tblData = ((List) mapData.get("BorrowingDisbursalTO"));
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            BorrowingDisbursalTO objTo = (BorrowingDisbursalTO) tblData.get(i);
            ArrayList sList = new ArrayList();
            sList.add(objTo.getBorrowingrefNo());
            sList.add(objTo.getBorrowingNo());
            sList.add(CommonUtil.convertObjToStr(objTo.getAmtBorrowed()));
           // sList.add(CommonUtil.convertObjToStr(objTo.getNarration()));
            newMap.put(objTo.getBorrowingNo(),objTo);
            tblList.add(sList);
           
        }
        setFinalMap(newMap);
        insertTable(tblList);
          }catch(Exception e){
              e.printStackTrace();
          }
    }

    public void showData(String acno) {
        List tblData = ((List) ((HashMap) mapData.get("BorrowingDisbursalTO")).get("BorrowingDisbursalTO"));
        ArrayList tblList = new ArrayList();
        for (int i = 0; i < tblData.size(); i++) {
            BorrowingDisbursalTO objTo = (BorrowingDisbursalTO) tblData.get(i);
            if (acno.equals(objTo.getBorrowingNo())) {
                setBorrowingDisbursalTO(objTo);
            }
        }
    }
    public void populateData(HashMap whereMap) {
       // HashMap mapData=null;
        try {
            System.out.println("whereMap=="+whereMap);
            System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+ mapData);
            BorrowingDisbursalTO objTO =(BorrowingDisbursalTO) ((List) mapData.get("BorrowingDisbursalTO")).get(0);
            System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            if(getActionType()!=ClientConstants.ACTIONTYPE_NEW){
            setTableData(mapData);
            }
            setBorrowingDisbursalTO(objTO);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    private void setBorrowingDisbursalTO(BorrowingDisbursalTO objTO){
        
      
        setDisbursalNo(objTO.getDisbursalNo());
        setTxtBorrowingNo(objTO.getBorrowingNo());
        setTxtAgency(objTO.getAgencyCode());
        setTxtBorrowingRefNo(objTO.getBorrowingrefNo());
        setTxtType(objTO.getType());
        setTxtaDescription(objTO.getDescription());
        setTxtRateInterest(objTO.getRateofInt());
        setTxtnoofInstall(objTO.getNoofInstallments());
        setTxtPrinRepFrq(objTO.getPrinRepFrq());
        setTxtIntRepFrq(objTO.getIntRepFrq());
        setTxtMorotorium(objTO.getMorotorium());
        
        setTdtDateSanctioned(objTO.getSanctionDate());
        setTdtDateExpiry(objTO.getSanctionExpDate());
        setTxtAmtSanctioned(objTO.getSanctionAmt());
        setTxtAmtBorrowed(objTO.getAmtBorrowed());
        setAmtBorrowedMaster(objTO.getAmtBorrowedMaster());
        setAvalbalBorrowedMaster(objTO.getAvalbalBorrowedMaster());
        setMultiDis(objTO.getMultiDis());
         setChanged();
        notifyObservers();
       
    }
    public BorrowingDisbursalTO getBorrowingDisbursalTO(String command){
        BorrowingDisbursalTO objTO = new BorrowingDisbursalTO();
        objTO.setCommand(command);
        objTO.setDisbursalNo(getDisbursalNo());
        objTO.setBorrowingNo(getTxtBorrowingNo());
        
        objTO.setAgencyCode(getTxtAgency());
        objTO.setBorrowingrefNo(getTxtBorrowingRefNo());
        objTO.setType(getTxtType());
        objTO.setDescription(getTxtaDescription());
        objTO.setSanctionDate(getTdtDateSanctioned());
        objTO.setSanctionAmt(getTxtAmtSanctioned());
        objTO.setRateofInt(getTxtRateInterest());
        objTO.setNoofInstallments(getTxtnoofInstall());
        objTO.setPrinRepFrq(getTxtPrinRepFrq());
        objTO.setIntRepFrq(getTxtIntRepFrq());
        objTO.setMorotorium(getTxtMorotorium());
        objTO.setSanctionExpDate(getTdtDateExpiry());
        objTO.setAmtBorrowed(getTxtAmtBorrowed());
        objTO.setAmtBorrowedMaster(getAmtBorrowedMaster());
        objTO.setAvalbalBorrowedMaster(getAmtBorrowedMaster());
        objTO.setMultiDis(getMultiDis());
        //  objTO.setBranchId("0001");
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
            //added by Anju 15/5/2014
            objTO.setCreatedBy(TrueTransactMain.USER_ID);
            
        }
        
        return objTO;
    }
    public void resetForm(){
        setAmtBorrowedMaster(0.0);
        setAvalbalBorrowedMaster(0.0);
        setDisbursalNo("");
        setTxtBorrowingNo("");
        setTxtAgency("");
        setTxtBorrowingRefNo("");
        setTxtType("");
        setTxtaDescription("");
        setTxtRateInterest(null);
        setTxtnoofInstall(null);
        setTxtPrinRepFrq("");
        setTxtIntRepFrq("");
        setTxtMorotorium("");
        
        setTdtDateSanctioned(null);
        setTdtDateExpiry(null);
        setTxtAmtSanctioned(null);
        setTxtAmtBorrowed(null);
        setTxtAmtBorrowed(null);
        setMultiDis("");
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
    public java.lang.String getDisbursalNo() {
        return txtDisbursalNo;
    }
    
    /**
     * Setter for property txtBorrowingNo.
     * @param txtBorrowingNo New value of property txtBorrowingNo.
     */
    public void setDisbursalNo(java.lang.String disNo) {
        this.txtDisbursalNo = disNo;
    }
    /**
     * Getter for property txtBorrowingNo.
     * @return Value of property txtBorrowingNo.
     */
    
    public java.lang.String getTxtBorrowingNo() {
        return txtBorrowingNo;
    }
    
    /**
     * Setter for property txtBorrowingNo.
     * @param txtBorrowingNo New value of property txtBorrowingNo.
     */
    public void setTxtBorrowingNo(java.lang.String txtBorrowingNo) {
        this.txtBorrowingNo = txtBorrowingNo;
    }
    
    /**
     * Getter for property txtAgency.
     * @return Value of property txtAgency.
     */
    public java.lang.String getTxtAgency() {
        return txtAgency;
    }
    
    /**
     * Setter for property txtAgency.
     * @param txtAgency New value of property txtAgency.
     */
    public void setTxtAgency(java.lang.String txtAgency) {
        this.txtAgency = txtAgency;
    }
    
    /**
     * Getter for property txtBorrowingRefNo.
     * @return Value of property txtBorrowingRefNo.
     */
    public java.lang.String getTxtBorrowingRefNo() {
        return txtBorrowingRefNo;
    }
    
    /**
     * Setter for property txtBorrowingRefNo.
     * @param txtBorrowingRefNo New value of property txtBorrowingRefNo.
     */
    public void setTxtBorrowingRefNo(java.lang.String txtBorrowingRefNo) {
        this.txtBorrowingRefNo = txtBorrowingRefNo;
    }
    
    /**
     * Getter for property txtType.
     * @return Value of property txtType.
     */
    public java.lang.String getTxtType() {
        return txtType;
    }
    
    /**
     * Setter for property txtType.
     * @param txtType New value of property txtType.
     */
    public void setTxtType(java.lang.String txtType) {
        this.txtType = txtType;
    }
    
    /**
     * Getter for property txtaDescription.
     * @return Value of property txtaDescription.
     */
    public java.lang.String getTxtaDescription() {
        return txtaDescription;
    }
    
    /**
     * Setter for property txtaDescription.
     * @param txtaDescription New value of property txtaDescription.
     */
    public void setTxtaDescription(java.lang.String txtaDescription) {
        this.txtaDescription = txtaDescription;
    }
    
    /**
     * Getter for property txtPrinRepFrq.
     * @return Value of property txtPrinRepFrq.
     */
    public java.lang.String getTxtPrinRepFrq() {
        return txtPrinRepFrq;
    }
    
    /**
     * Setter for property txtPrinRepFrq.
     * @param txtPrinRepFrq New value of property txtPrinRepFrq.
     */
    public void setTxtPrinRepFrq(java.lang.String txtPrinRepFrq) {
        this.txtPrinRepFrq = txtPrinRepFrq;
    }
    
    /**
     * Getter for property txtIntRepFrq.
     * @return Value of property txtIntRepFrq.
     */
    public java.lang.String getTxtIntRepFrq() {
        return txtIntRepFrq;
    }
    
    /**
     * Setter for property txtIntRepFrq.
     * @param txtIntRepFrq New value of property txtIntRepFrq.
     */
    public void setTxtIntRepFrq(java.lang.String txtIntRepFrq) {
        this.txtIntRepFrq = txtIntRepFrq;
    }
    
    /**
     * Getter for property txtMorotorium.
     * @return Value of property txtMorotorium.
     */
    public java.lang.String getTxtMorotorium() {
        return txtMorotorium;
    }
    
    /**
     * Setter for property txtMorotorium.
     * @param txtMorotorium New value of property txtMorotorium.
     */
    public void setTxtMorotorium(java.lang.String txtMorotorium) {
        this.txtMorotorium = txtMorotorium;
    }
    
    /**
     * Getter for property tdtDateSanctioned.
     * @return Value of property tdtDateSanctioned.
     */
    public java.util.Date getTdtDateSanctioned() {
        return tdtDateSanctioned;
    }
    
    /**
     * Setter for property tdtDateSanctioned.
     * @param tdtDateSanctioned New value of property tdtDateSanctioned.
     */
    public void setTdtDateSanctioned(java.util.Date tdtDateSanctioned) {
        this.tdtDateSanctioned = tdtDateSanctioned;
    }
    
    /**
     * Getter for property tdtDateExpiry.
     * @return Value of property tdtDateExpiry.
     */
    public java.util.Date getTdtDateExpiry() {
        return tdtDateExpiry;
    }
    
    /**
     * Setter for property tdtDateExpiry.
     * @param tdtDateExpiry New value of property tdtDateExpiry.
     */
    public void setTdtDateExpiry(java.util.Date tdtDateExpiry) {
        this.tdtDateExpiry = tdtDateExpiry;
    }
    
    /**
     * Getter for property txtAmtSanctioned.
     * @return Value of property txtAmtSanctioned.
     */
    public java.lang.Double getTxtAmtSanctioned() {
        return txtAmtSanctioned;
    }
    
    /**
     * Setter for property txtAmtSanctioned.
     * @param txtAmtSanctioned New value of property txtAmtSanctioned.
     */
    public void setTxtAmtSanctioned(java.lang.Double txtAmtSanctioned) {
        this.txtAmtSanctioned = txtAmtSanctioned;
    }
    
    /**
     * Getter for property txtAmtBorrowed.
     * @return Value of property txtAmtBorrowed.
     */
    public java.lang.Double getTxtAmtBorrowed() {
        return txtAmtBorrowed;
    }
    
    /**
     * Setter for property txtAmtBorrowed.
     * @param txtAmtBorrowed New value of property txtAmtBorrowed.
     */
    public void setTxtAmtBorrowed(java.lang.Double txtAmtBorrowed) {
        this.txtAmtBorrowed = txtAmtBorrowed;
    }
    
    /**
     * Getter for property txtRateInterest.
     * @return Value of property txtRateInterest.
     */
    public java.lang.Double getTxtRateInterest() {
        return txtRateInterest;
    }
    
    /**
     * Setter for property txtRateInterest.
     * @param txtRateInterest New value of property txtRateInterest.
     */
    public void setTxtRateInterest(java.lang.Double txtRateInterest) {
        this.txtRateInterest = txtRateInterest;
    }
    
    /**
     * Getter for property txtnoofInstall.
     * @return Value of property txtnoofInstall.
     */
    public java.lang.Double getTxtnoofInstall() {
        return txtnoofInstall;
    }
    
    /**
     * Setter for property txtnoofInstall.
     * @param txtnoofInstall New value of property txtnoofInstall.
     */
    public void setTxtnoofInstall(java.lang.Double txtnoofInstall) {
        this.txtnoofInstall = txtnoofInstall;
    }
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("COMMAND", command);
             
            if(!command.equals(CommonConstants.AUTHORIZESTATUS)){
                term.put("finalMap", getFinalMap());
            term.put("BorrowingDisbursalTO", getBorrowingDisbursalTO(command));
           
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
                    System.out.println("fmapaaa"+getFinalMap());
                     term.put("finalMap", getFinalMap());
                    authMap = null;
                }
            HashMap proxyReturnMap = proxy.execute(term, map);
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
     * Getter for property amtBorrowedMastre.
     * @return Value of property amtBorrowedMastre.
     */
    public double getAmtBorrowedMaster() {
        return amtBorrowedMaster;
    }
    
    /**
     * Setter for property amtBorrowedMastre.
     * @param amtBorrowedMastre New value of property amtBorrowedMastre.
     */
    public void setAmtBorrowedMaster(double amtBorrowedMastre) {
        this.amtBorrowedMaster = amtBorrowedMaster;
    }
    
    /**
     * Getter for property avalbalBorrowedMaster.
     * @return Value of property avalbalBorrowedMaster.
     */
    public double getAvalbalBorrowedMaster() {
        return avalbalBorrowedMaster;
    }
    
    /**
     * Setter for property avalbalBorrowedMaster.
     * @param avalbalBorrowedMaster New value of property avalbalBorrowedMaster.
     */
    public void setAvalbalBorrowedMaster(double avalbalBorrowedMaster) {
        this.avalbalBorrowedMaster = avalbalBorrowedMaster;
    }
    
    /**
     * Getter for property multiDis.
     * @return Value of property multiDis.
     */
    public java.lang.String getMultiDis() {
        return multiDis;
    }
    
    /**
     * Setter for property multiDis.
     * @param multiDis New value of property multiDis.
     */
    public void setMultiDis(java.lang.String multiDis) {
        this.multiDis = multiDis;
    }
     public EnhancedTableModel getTblCheckBookTable() {
        return tblCheckBookTable;
    }

    public void setTblCheckBookTable(EnhancedTableModel tblCheckBookTable) {
        this.tblCheckBookTable = tblCheckBookTable;
    }  public HashMap getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(HashMap finalMap) {
        this.finalMap = finalMap;
    }
    
}
