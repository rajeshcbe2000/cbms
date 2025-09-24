/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalesOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.indend;
import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.indend.sale.SaleTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
/**
 *
 * @author  user
 */

public class SalesOB extends CObservable{
     private String txtSalesmanID = "",txtName="",
        txaArea="",txaRemarks="";
     Double txtSecAmt;
    private static SqlMap sqlMap = null; 
    private final static Logger log = Logger.getLogger(SalesOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static SalesOB objSalesOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
      
    /** Creates a new instance of NewBorrowingOB */
    public SalesOB() {
         try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
           // initUIComboBoxModel();
       //    fillDropdown();
           }catch(Exception e){
            //parseException.logException(e,true);
                 System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
     static {
        try {
            log.info("Creating ParameterOB...");
            objSalesOB = new SalesOB();
        } catch(Exception e) {
           // parseException.logException(e,true);
               System.out.println("Error in static():"+e);
        }
    }
      /* private void initUIComboBoxModel(){
        cbmDepoId = new ComboBoxModel();
        cbmTransType= new ComboBoxModel();
    }*/
     // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SaleJNDI");
        map.put(CommonConstants.HOME, "sale.SaleHome");
        map.put(CommonConstants.REMOTE, "sale.Sale");
    }
      /*  public com.see.truetransact.clientutil.ComboBoxModel getCbmDepoId() {
        return cbmDepoId;
    }
        public com.see.truetransact.clientutil.ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }*/
   
 
     public static SalesOB getInstance()throws Exception{
        return objSalesOB;
    }
      public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
      //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
      public void setStatus(){
       // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    /**
     * Setter for property cbmTokenType.
     * @param cbmTokenType New value of property cbmTokenType.
     */
  /*  public void setCbmDepoId(com.see.truetransact.clientutil.ComboBoxModel cbmBrAgency) {
        this.cbmDepoId = cbmDepoId;
    }
    public void setCbmTransType(com.see.truetransact.clientutil.ComboBoxModel cbmBrType) {
        this.cbmTransType = cbmTransType;
    }*/
  
     /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
     public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
             System.out.println("whereMap=="+whereMap);
             System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+((List) mapData.get("SaleTO")).get(0));
            SaleTO objTO =(SaleTO) ((List) mapData.get("SaleTO")).get(0);
           System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setSaleTO(objTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           // parseException.logException(e,true);
               System.out.println("Error in populateData():"+e);
            
        }
    }
      public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
       public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
      /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
       public int getResult(){
        return _result;
    }
      private void setSaleTO(SaleTO objTO){
     //   setBorrowingNo(objTO.getBorrowingNo());
       // setCboDepoId(CommonUtil.convertObjToStr(getCbmDepoId().getDataForKey(objTO.getDepoId())));
       // setCboTransType(CommonUtil.convertObjToStr(getCbmTransType().getDataForKey(objTO.getTransType())));
       setTxtSalesmanID(objTO.getSmId());
        setTxaArea(objTO.getAddress());
        setTxtSecAmt(objTO.getSec_amt());
        setTxaRemarks(objTO.getRemarks());
        setTxtName(objTO.getName());
        notifyObservers();
    }
        /** Resets all the UI Fields */
    public void resetForm(){
         setTxtSalesmanID("");
        setTxaArea("");
        setTxtSecAmt(null);
        setTxaRemarks("");
        setTxtName("");
        notifyObservers();
    }
      public void setSMNo(java.lang.String txtSMNo) {
        this.txtSalesmanID = txtSMNo;
        setChanged();
    }
       public String getSMNo() {
        return txtSalesmanID;
       
    }
      
         /* Executes Query using the TO object */
    public void execute(String command) {
        try {
           // System.out.println("GET BOPRRR NO IN EDIT :="+geIndendTO(command));
           
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, "0001");
            term.put(CommonConstants.BRANCH_ID, "0001");
            term.put("SaleTO", geSaleTO(command));
             System.out.println("GET term IN EDIT :="+term);
              System.out.println("GET map IN EDIT :="+map);
            HashMap proxyReturnMap = proxy.execute(term, map);
//            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            System.out.println("ACTIONN TYPEEE==="+getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
              System.out.println("Error in execute():"+e);
        }
    }
       private SaleTO geSaleTO(String command){
        SaleTO objTO = new SaleTO();
        objTO.setCommand(command);
        objTO.setSmId(getTxtSalesmanID());
        objTO.setName(getTxtName());
        objTO.setAddress(getTxaArea());
        objTO.setSec_amt(getTxtSecAmt());
        objTO.setRemarks(getTxaRemarks());
       
      
       if(command.equals(CommonConstants.TOSTATUS_INSERT)){
                objTO.setAuthorizeStatus("");
                objTO.setAuthorizeBy("");
                objTO.setAuthorizeDte(null);
                objTO.setStatus("CREATED");
        }
     
        return objTO;
    }
        /* Filling up the the ComboBox in the UI*/
   
    
    /**
     * Getter for property txtSalesmanID.
     * @return Value of property txtSalesmanID.
     */
    public java.lang.String getTxtSalesmanID() {
        return txtSalesmanID;
    }    
   
    /**
     * Setter for property txtSalesmanID.
     * @param txtSalesmanID New value of property txtSalesmanID.
     */
    public void setTxtSalesmanID(java.lang.String txtSalesmanID) {
        this.txtSalesmanID = txtSalesmanID;
    }    
    
    /**
     * Getter for property txtName.
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }
    
    /**
     * Setter for property txtName.
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }
    
    /**
     * Getter for property txaArea.
     * @return Value of property txaArea.
     */
    public java.lang.String getTxaArea() {
        return txaArea;
    }
    
    /**
     * Setter for property txaArea.
     * @param txaArea New value of property txaArea.
     */
    public void setTxaArea(java.lang.String txaArea) {
        this.txaArea = txaArea;
    }
    
    /**
     * Getter for property txtSecAmt.
     * @return Value of property txtSecAmt.
     */
    public java.lang.Double getTxtSecAmt() {
        return txtSecAmt;
    }
    
    /**
     * Setter for property txtSecAmt.
     * @param txtSecAmt New value of property txtSecAmt.
     */
    public void setTxtSecAmt(java.lang.Double txtSecAmt) {
        this.txtSecAmt = txtSecAmt;
    }
    
    /**
     * Getter for property txaRemarks.
     * @return Value of property txaRemarks.
     */
    public java.lang.String getTxaRemarks() {
        return txaRemarks;
    }
    
    /**
     * Setter for property txaRemarks.
     * @param txaRemarks New value of property txaRemarks.
     */
    public void setTxaRemarks(java.lang.String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }
    
}
