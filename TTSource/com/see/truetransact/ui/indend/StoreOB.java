/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StoreOB.java
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
import com.see.truetransact.transferobject.indend.store.StoreTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
/**
 *
 * @author  user
 */

public class StoreOB extends CObservable{
     private String txtStoreNo = "",txtStoreName="",
        rdYes="";
     private String txtServiceTax="",txtVatTax="";
    private static SqlMap sqlMap = null; 
    private final static Logger log = Logger.getLogger(StoreOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static StoreOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
      
    /** Creates a new instance of NewBorrowingOB */
    public StoreOB() {
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
            objOB = new StoreOB();
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
        map.put(CommonConstants.JNDI, "StoreJNDI");
        map.put(CommonConstants.HOME, "store.StoreHome");
        map.put(CommonConstants.REMOTE, "store.Store");
    }
      /*  public com.see.truetransact.clientutil.ComboBoxModel getCbmDepoId() {
        return cbmDepoId;
    }
        public com.see.truetransact.clientutil.ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }*/
   
 
     public static StoreOB getInstance()throws Exception{
        return objOB;
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
            System.out.println("objmapData=="+((List) mapData.get("StoreTO")).get(0));
            StoreTO objTO =(StoreTO) ((List) mapData.get("StoreTO")).get(0);
           System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setStoreTO(objTO);
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
      private void setStoreTO(StoreTO objTO){
     //   setBorrowingNo(objTO.getBorrowingNo());
       // setCboDepoId(CommonUtil.convertObjToStr(getCbmDepoId().getDataForKey(objTO.getDepoId())));
       // setCboTransType(CommonUtil.convertObjToStr(getCbmTransType().getDataForKey(objTO.getTransType())));
       setTxtStoreNo(objTO.getStNo());
        setTxtStoreName(objTO.getName());
        setRdYes(objTO.getGenTrans());
        setTxtServiceTax(objTO.getServTax());
        setTxtVatTax(objTO.getVat());
        notifyObservers();
    }
        /** Resets all the UI Fields */
    public void resetForm(){
        setTxtStoreNo("");
        setTxtStoreName("");
        setRdYes("");
        setTxtServiceTax("");
        setTxtVatTax("");
        notifyObservers();
    }
      public void setStNo(java.lang.String txtSMNo) {
        this.txtStoreNo = txtSMNo;
        setChanged();
    }
       public String getStNo() {
        return txtStoreNo;
       
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
            term.put("StoreTO", geStoreTO(command));
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
       private StoreTO geStoreTO(String command){
        StoreTO objTO = new StoreTO();
        objTO.setCommand(command);
        objTO.setStNo(getTxtStoreNo());
        objTO.setName(getTxtStoreName());
        objTO.setGenTrans(getRdYes());
        objTO.setServTax(getTxtServiceTax());
          objTO.setVat(getTxtVatTax());
             
       if(command.equals(CommonConstants.TOSTATUS_INSERT)){
                objTO.setAuthorizeStatus("");
                objTO.setAuthorizeBy("");
                objTO.setAuthorizeDte(null);
                objTO.setStatus("CREATED");
        }
     
        return objTO;
    }
       
       /**
        * Getter for property txtStoreNo.
        * @return Value of property txtStoreNo.
        */
       public java.lang.String getTxtStoreNo() {
           return txtStoreNo;
       }
       
       /**
        * Setter for property txtStoreNo.
        * @param txtStoreNo New value of property txtStoreNo.
        */
       public void setTxtStoreNo(java.lang.String txtStoreNo) {
           this.txtStoreNo = txtStoreNo;
       }
       
       /**
        * Getter for property txtStoreName.
        * @return Value of property txtStoreName.
        */
       public java.lang.String getTxtStoreName() {
           return txtStoreName;
       }
       
       /**
        * Setter for property txtStoreName.
        * @param txtStoreName New value of property txtStoreName.
        */
       public void setTxtStoreName(java.lang.String txtStoreName) {
           this.txtStoreName = txtStoreName;
       }
       
       /**
        * Getter for property rdYes.
        * @return Value of property rdYes.
        */
       public java.lang.String getRdYes() {
           return rdYes;
       }
       
       /**
        * Setter for property rdYes.
        * @param rdYes New value of property rdYes.
        */
       public void setRdYes(java.lang.String rdYes) {
           this.rdYes = rdYes;
       }
       
       /**
        * Getter for property txtServiceTax.
        * @return Value of property txtServiceTax.
        */
       public String getTxtServiceTax() {
           return txtServiceTax;
       }
       
       /**
        * Setter for property txtServiceTax.
        * @param txtServiceTax New value of property txtServiceTax.
        */
       public void setTxtServiceTax(String txtServiceTax) {
           this.txtServiceTax = txtServiceTax;
       }
       
       /**
        * Getter for property txtVatTax.
        * @return Value of property txtVatTax.
        */
       public String getTxtVatTax() {
           return txtVatTax;
       }
       
       /**
        * Setter for property txtVatTax.
        * @param txtVatTax New value of property txtVatTax.
        */
       public void setTxtVatTax(String txtVatTax) {
           this.txtVatTax = txtVatTax;
       }
       
        /* Filling up the the ComboBox in the UI*/
   
    
   
    
}
