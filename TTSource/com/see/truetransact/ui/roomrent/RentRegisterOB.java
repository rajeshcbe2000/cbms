/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RentRegisterOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.roomrent;
import org.apache.log4j.Logger;
import java.util.Observable;
import com.see.truetransact.clientproxy.ProxyParameters;
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
import java.util.LinkedHashMap;
import com.see.truetransact.transferobject.roomrent.rentregister.RentRegisterTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB ;
/**
 *
 * @author  user
 */

public class RentRegisterOB extends CObservable{
     private String RRId="", cboRoomNo="",cboBuildingNo="",txtAppNo="",txtName="",txtHouseName="",txtPlace="",txtCity="",txtPhNo="",txtMobNo="",txtEmailId="",
     txtGuardian="",txtNominee="",txtRecommBy="",txtAgreNo="", txtPenalGrPeriod="",txtaAdvDetails="",cboRentDate="",txtRmNumber="";
     
      Date tdtOccDate,tdtCommDate,tdtAgrDate,txtApplDate;

    public Date getTxtApplDate() {
        return txtApplDate;
    }

    public void setTxtApplDate(Date txtApplDate) {
        this.txtApplDate = txtApplDate;
    }
      Double txtRentAmt,txtAdvAmt;

    private static SqlMap sqlMap = null;
    private TransactionOB transactionOB;
    Date currDt = null;
    private final static Logger log = Logger.getLogger(RentRegisterOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static RentRegisterOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
       private HashMap authMap = new HashMap();
      
    /** Creates a new instance of NewBorrowingOB */
       public RentRegisterOB() {
         try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            currDt = ClientUtil.getCurrentDate();
           // initUIComboBoxModel();
          // fillDropdown();
           }catch(Exception e){
            //parseException.logException(e,true);
                 System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
     static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new RentRegisterOB();
        } catch(Exception e) {
           // parseException.logException(e,true);
               System.out.println("Error in static():"+e);
        }
    }
       private void initUIComboBoxModel(){
        //cbmDepoId = new ComboBoxModel();
       // cbmTransType= new ComboBoxModel();
    }
          /**
     * Setter for property currDt.
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }
         public java.util.Date getCurrDt() {
        return currDt;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
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
     // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "RentRegisterJNDI");
        map.put(CommonConstants.HOME, "rentregister.RentRegisterHome");
        map.put(CommonConstants.REMOTE, "rentregister.RentRegister");
    }
     
   
 
     public static RentRegisterOB getInstance()throws Exception{
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
            System.out.println("mapData========="+mapData);
           System.out.println("objmapData=="+((List) mapData.get("RentRegisterTO")));
           RentRegisterTO objTO =(RentRegisterTO) ((List) mapData.get("RentRegisterTO")).get(0);
          // System.out.println("objTOobjTOobjTOobjTO=="+objTO);
            setRentRegisterTO(objTO);
            //System.out.println("mapData=UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUu="+mapData); 
              if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                //System.out.println("mapData=list999999999999999999999999999999999999999999999999="+list); 
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                    transactionOB.setTxtTransactionAmt(String.valueOf(objTO.getRentAmt()));
                }
            }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
           // parseException.logException(e,true);
               System.out.println("Error in populateData():"+e);
               e.printStackTrace();
            
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
      private void setRentRegisterTO(RentRegisterTO objTO){
          
             setRRId(objTO.getRrId());
            setTxtAppNo(objTO.getAppNo());
            setTxtName(objTO.getName());
            setTxtHouseName(objTO.getHouseName());
            setTxtPlace(objTO.getPlace());
            setTxtCity(objTO.getCity());
            setTxtPhNo(objTO.getPhNo());
            setTxtMobNo(objTO.getMobNo());
            setTxtEmailId(objTO.getEmailId());
            setTxtGuardian(objTO.getGuardian());
            setTxtNominee(objTO.getNominee());
            setTxtRecommBy(objTO.getRecommBy());
            setTxtAgreNo(objTO.getAgreNo());
            setTxtPenalGrPeriod(objTO.getPenalGrPeriod());
            setTxtaAdvDetails(objTO.getAAdvDetails());
            setTxtApplDate(objTO.getApplDate());
            setTdtOccDate(objTO.getOccDate());
            setTdtCommDate(objTO.getCommDate());
            setTdtAgrDate(objTO.getAgrDate());
            setCboRentDate(objTO.getRentDate());
            setTxtRentAmt(objTO.getRentAmt());
            setTxtAdvAmt(objTO.getAdvAmt());
            setCboRoomNo(objTO.getRoomNo());
            setCboBuildingNo(objTO.getBuidingNo());
            setTxtRmNumber(objTO.getRmNumber());
             notifyObservers();
     
      
    }
        /** Resets all the UI Fields */
    public void resetForm(){
          setTxtAppNo("");
            setTxtName("");
            setTxtHouseName("");
            setTxtPlace("");
            setTxtCity("");
            setTxtPhNo("");
            setTxtMobNo("");
            setTxtEmailId("");
            setTxtGuardian("");
            setTxtNominee("");
            setTxtRecommBy("");
            setTxtAgreNo("");
            setTxtPenalGrPeriod("");
            setTxtaAdvDetails("");
            setTxtApplDate(null);
            setTdtOccDate(null);
            setTdtCommDate(null);
            setTdtAgrDate(null);
            setCboRentDate(null);
            setTxtRentAmt(null);
            setTxtAdvAmt(null);
            setCboRoomNo("");
            setCboBuildingNo("");
        notifyObservers();
    }
    
      
         /* Executes Query using the TO object */
    public void execute(String command) {
   
         try {
             System.out.println("command====="+command);
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID,  ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if(!command.equals(CommonConstants.AUTHORIZESTATUS))
            {
            term.put("RentRegisterTO", geRentRegisterTO(command));
            if (transactionDetailsTO == null)
                transactionDetailsTO = new LinkedHashMap();
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
                 if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                     transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                     allowedTransactionDetailsTO = null;
                     term.put("TransactionTO", transactionDetailsTO);
                 }
             }
            if(getAuthMap() != null && getAuthMap().size() > 0 )
            {
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
            System.out.println("term=========="+term);
            System.out.println("map=========="+map);
            
            HashMap proxyReturnMap = proxy.execute(term, map);
            System.out.println("proxyReturnMap in RR=="+proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():"+e);
        }
    }
       private RentRegisterTO geRentRegisterTO(String command){
        RentRegisterTO objTO = new RentRegisterTO();
        objTO.setCommand(command);
        
            objTO.setRrId(getRRId());
            objTO.setAppNo(getTxtAppNo());
            objTO.setName(getTxtName());
            objTO.setHouseName(getTxtHouseName());
            objTO.setPlace(getTxtPlace());
            objTO.setCity(getTxtCity());
            objTO.setPhNo(getTxtPhNo());
            objTO.setMobNo(getTxtMobNo());
            objTO.setEmailId(getTxtEmailId());
            objTO.setGuardian(getTxtGuardian());
            objTO.setNominee(getTxtNominee());
            objTO.setRecommBy(getTxtRecommBy());
            objTO.setAgreNo(getTxtAgreNo());
            objTO.setPenalGrPeriod(getTxtPenalGrPeriod());
            objTO.setAAdvDetails(getTxtaAdvDetails());
            objTO.setApplDate(getTxtApplDate());
            objTO.setOccDate(getTdtOccDate());
            objTO.setCommDate(getTdtCommDate());
            objTO.setAgrDate(getTdtAgrDate());
            objTO.setRentDate(getCboRentDate());
            objTO.setRentAmt(getTxtRentAmt());
            objTO.setAdvAmt(getTxtAdvAmt());
            objTO.setRoomNo(getCboRoomNo());
            objTO.setBuidingNo(getCboBuildingNo());
            objTO.setRmNumber(getTxtRmNumber());
            objTO.setStatusBy(ProxyParameters.USER_ID);
            objTO.setStatusDate((Date)currDt.clone());
      
       if(command.equals(CommonConstants.TOSTATUS_INSERT)){
                objTO.setAuthorizeStatus("");
                objTO.setAuthorizeBy("");
                objTO.setAuthorizeDte(null);
                objTO.setStatus("CREATED");
        }
     
        return objTO;
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
        /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            //This part is fill the agency dropdown
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
         //   final ArrayList lookup_keys = new ArrayList();
        //    lookup_keys.add("INDEND_DEPID");
          //  lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
          //  keyValue = ClientUtil.populateLookupData(lookUpHash);
        //    getKeyValue((HashMap)keyValue.get("INDEND_DEPID"));
           // cbmDepoId = new ComboBoxModel(key,value);
            
            
            
          //  List aList=ClientUtil.executeQuery("Indend.getIndendDepos",keyValue); 
          // cbmDepoId = new ComboBoxModel(aList);
            
            //This part is fill the type dropdown
            final ArrayList lookup_keys1 = new ArrayList();
            lookup_keys1.add("INDEND_TRANSTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys1);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("INDEND_TRANSTYPE"));
            //cbmTransType = new ComboBoxModel(key,value);
           
            //This part is fill the principal and interest repayment dropdowns
           /*******************************/
            
        }catch(NullPointerException e){
           // parseException.logException(e,true);
             System.out.println("Error in fillDropdown():"+e);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property RRId.
     * @return Value of property RRId.
     */
    public java.lang.String getRRId() {
        return RRId;
    }    
   
    /**
     * Setter for property RRId.
     * @param RRId New value of property RRId.
     */
    public void setRRId(java.lang.String RRId) {
        this.RRId = RRId;
    }    
    
 
    
    /**
     * Getter for property txtAppNo.
     * @return Value of property txtAppNo.
     */
    public java.lang.String getTxtAppNo() {
        return txtAppNo;
    }
    
    /**
     * Setter for property txtAppNo.
     * @param txtAppNo New value of property txtAppNo.
     */
    public void setTxtAppNo(java.lang.String txtAppNo) {
        this.txtAppNo = txtAppNo;
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
     * Getter for property txtHouseName.
     * @return Value of property txtHouseName.
     */
    public java.lang.String getTxtHouseName() {
        return txtHouseName;
    }
    
    /**
     * Setter for property txtHouseName.
     * @param txtHouseName New value of property txtHouseName.
     */
    public void setTxtHouseName(java.lang.String txtHouseName) {
        this.txtHouseName = txtHouseName;
    }
    
    /**
     * Getter for property txtPlace.
     * @return Value of property txtPlace.
     */
    public java.lang.String getTxtPlace() {
        return txtPlace;
    }
    
    /**
     * Setter for property txtPlace.
     * @param txtPlace New value of property txtPlace.
     */
    public void setTxtPlace(java.lang.String txtPlace) {
        this.txtPlace = txtPlace;
    }
    
    /**
     * Getter for property txtCity.
     * @return Value of property txtCity.
     */
    public java.lang.String getTxtCity() {
        return txtCity;
    }
    
    /**
     * Setter for property txtCity.
     * @param txtCity New value of property txtCity.
     */
    public void setTxtCity(java.lang.String txtCity) {
        this.txtCity = txtCity;
    }
    
    /**
     * Getter for property txtPhNo.
     * @return Value of property txtPhNo.
     */
    public java.lang.String getTxtPhNo() {
        return txtPhNo;
    }
    
    /**
     * Setter for property txtPhNo.
     * @param txtPhNo New value of property txtPhNo.
     */
    public void setTxtPhNo(java.lang.String txtPhNo) {
        this.txtPhNo = txtPhNo;
    }
    
    /**
     * Getter for property txtMobNo.
     * @return Value of property txtMobNo.
     */
    public java.lang.String getTxtMobNo() {
        return txtMobNo;
    }
    
    /**
     * Setter for property txtMobNo.
     * @param txtMobNo New value of property txtMobNo.
     */
    public void setTxtMobNo(java.lang.String txtMobNo) {
        this.txtMobNo = txtMobNo;
    }
    
    /**
     * Getter for property txtEmailId.
     * @return Value of property txtEmailId.
     */
    public java.lang.String getTxtEmailId() {
        return txtEmailId;
    }
    
    /**
     * Setter for property txtEmailId.
     * @param txtEmailId New value of property txtEmailId.
     */
    public void setTxtEmailId(java.lang.String txtEmailId) {
        this.txtEmailId = txtEmailId;
    }
    
    /**
     * Getter for property txtGuardian.
     * @return Value of property txtGuardian.
     */
    public java.lang.String getTxtGuardian() {
        return txtGuardian;
    }
    
    /**
     * Setter for property txtGuardian.
     * @param txtGuardian New value of property txtGuardian.
     */
    public void setTxtGuardian(java.lang.String txtGuardian) {
        this.txtGuardian = txtGuardian;
    }
    
    /**
     * Getter for property txtNominee.
     * @return Value of property txtNominee.
     */
    public java.lang.String getTxtNominee() {
        return txtNominee;
    }
    
    /**
     * Setter for property txtNominee.
     * @param txtNominee New value of property txtNominee.
     */
    public void setTxtNominee(java.lang.String txtNominee) {
        this.txtNominee = txtNominee;
    }
    
    /**
     * Getter for property txtRecommBy.
     * @return Value of property txtRecommBy.
     */
    public java.lang.String getTxtRecommBy() {
        return txtRecommBy;
    }
    
    /**
     * Setter for property txtRecommBy.
     * @param txtRecommBy New value of property txtRecommBy.
     */
    public void setTxtRecommBy(java.lang.String txtRecommBy) {
        this.txtRecommBy = txtRecommBy;
    }
    
    /**
     * Getter for property txtAgreNo.
     * @return Value of property txtAgreNo.
     */
    public java.lang.String getTxtAgreNo() {
        return txtAgreNo;
    }
    
    /**
     * Setter for property txtAgreNo.
     * @param txtAgreNo New value of property txtAgreNo.
     */
    public void setTxtAgreNo(java.lang.String txtAgreNo) {
        this.txtAgreNo = txtAgreNo;
    }
    
    /**
     * Getter for property txtPenalGrPeriod.
     * @return Value of property txtPenalGrPeriod.
     */
    public java.lang.String getTxtPenalGrPeriod() {
        return txtPenalGrPeriod;
    }
    
    /**
     * Setter for property txtPenalGrPeriod.
     * @param txtPenalGrPeriod New value of property txtPenalGrPeriod.
     */
    public void setTxtPenalGrPeriod(java.lang.String txtPenalGrPeriod) {
        this.txtPenalGrPeriod = txtPenalGrPeriod;
    }
    
    /**
     * Getter for property txtaAdvDetails.
     * @return Value of property txtaAdvDetails.
     */
    public java.lang.String getTxtaAdvDetails() {
        return txtaAdvDetails;
    }
    
    /**
     * Setter for property txtaAdvDetails.
     * @param txtaAdvDetails New value of property txtaAdvDetails.
     */
    public void setTxtaAdvDetails(java.lang.String txtaAdvDetails) {
        this.txtaAdvDetails = txtaAdvDetails;
    }
    
    /**
     * Getter for property tdtApplDate.
     * @return Value of property tdtApplDate.
     */
   
    
    /**
     * Setter for property tdtApplDate.
     * @param tdtApplDate New value of property tdtApplDate.
     */
    
    
    /**
     * Getter for property tdtOccDate.
     * @return Value of property tdtOccDate.
     */
    public java.util.Date getTdtOccDate() {
        return tdtOccDate;
    }
    
    /**
     * Setter for property tdtOccDate.
     * @param tdtOccDate New value of property tdtOccDate.
     */
    public void setTdtOccDate(java.util.Date tdtOccDate) {
        this.tdtOccDate = tdtOccDate;
    }
    
    /**
     * Getter for property tdtCommDate.
     * @return Value of property tdtCommDate.
     */
    public java.util.Date getTdtCommDate() {
        return tdtCommDate;
    }
    
    /**
     * Setter for property tdtCommDate.
     * @param tdtCommDate New value of property tdtCommDate.
     */
    public void setTdtCommDate(java.util.Date tdtCommDate) {
        this.tdtCommDate = tdtCommDate;
    }
    
    /**
     * Getter for property tdtAgrDate.
     * @return Value of property tdtAgrDate.
     */
    public java.util.Date getTdtAgrDate() {
        return tdtAgrDate;
    }
    
    /**
     * Setter for property tdtAgrDate.
     * @param tdtAgrDate New value of property tdtAgrDate.
     */
    public void setTdtAgrDate(java.util.Date tdtAgrDate) {
        this.tdtAgrDate = tdtAgrDate;
    }
    
    /**
     * Getter for property tdtRentDate.
     * @return Value of property tdtRentDate.
     */
    public String getCboRentDate() {
        return cboRentDate;
    }
    
    /**
     * Setter for property tdtRentDate.
     * @param tdtRentDate New value of property tdtRentDate.
     */
    public void setCboRentDate(String cboRentDate) {
        this.cboRentDate = cboRentDate;
    }
    
    /**
     * Getter for property txtRentAmt.
     * @return Value of property txtRentAmt.
     */
    public java.lang.Double getTxtRentAmt() {
        return txtRentAmt;
    }
    
    /**
     * Setter for property txtRentAmt.
     * @param txtRentAmt New value of property txtRentAmt.
     */
    public void setTxtRentAmt(java.lang.Double txtRentAmt) {
        this.txtRentAmt = txtRentAmt;
    }
    
    /**
     * Getter for property txtAdvAmt.
     * @return Value of property txtAdvAmt.
     */
    public java.lang.Double getTxtAdvAmt() {
        return txtAdvAmt;
    }
    
    /**
     * Setter for property txtAdvAmt.
     * @param txtAdvAmt New value of property txtAdvAmt.
     */
    public void setTxtAdvAmt(java.lang.Double txtAdvAmt) {
        this.txtAdvAmt = txtAdvAmt;
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
    
    /**
     * Getter for property txtRmNumber.
     * @return Value of property txtRmNumber.
     */
  
    /**
     * Getter for property txtRmNumber.
     * @return Value of property txtRmNumber.
     */
   
    
    /**
     * Getter for property txtRmNumber.
     * @return Value of property txtRmNumber.
     */
 
    
}
