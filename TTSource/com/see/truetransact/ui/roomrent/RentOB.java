/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RentOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.roomrent;
import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.roomrent.rent.RentTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import java.util.*;
import com.see.truetransact.clientproxy.ProxyParameters;
/**
 *
 * @author  user
 */

public class RentOB extends CObservable{
     private String txtRMNo = "",txtBuildingNo="",
        txtaBuildingDes="",txtRentAccHead="",txtPenelAccHead="",txtNoticeAccHead="",
        txtLegalAccHead="",txtArbAccHead="",txtCourtGrpHead="",
        txtExeGrpHead="",cboStatus="",txtRoomNo="",txtVersNo="",txtRentFeq="",txtAdvHead="",status = "";;
     Double txtRentAmt,txtPenelRate;
     Date tdtEffDate;
    private static SqlMap sqlMap = null; 
    private final static Logger log = Logger.getLogger(RentOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static RentOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private Vector dataV=null;  
    /** Creates a new instance of NewBorrowingOB */
    public RentOB() {
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
            objOB = new RentOB();
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
        map.put(CommonConstants.JNDI, "RentJNDI");
        map.put(CommonConstants.HOME, "rent.RentHome");
        map.put(CommonConstants.REMOTE, "rent.Rent");
    }
      /*  public com.see.truetransact.clientutil.ComboBoxModel getCbmDepoId() {
        return cbmDepoId;
    }
        public com.see.truetransact.clientutil.ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }*/
   
 
     public static RentOB getInstance()throws Exception{
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
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
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
             System.out.println("objmapData=="+mapData);
             
            System.out.println("objmapData=="+((List) mapData.get("RentTO")).get(0));
            RentTO objTO =(RentTO) ((List) mapData.get("RentTO")).get(0);
           System.out.println("objTOobjTOobjTOobjTO=="+((List) mapData.get("list")).get(0));
           // RentTO objTO1 =(RentTO) ((List) mapData.get("list")).get(0);
            setRentTO(objTO);
         //    Vector vector = new Vector();
           // objTO.setDataV(objTO1);
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
      private void setRentTO(RentTO objTO){
     //   setBorrowingNo(objTO.getBorrowingNo());
       // setCboDepoId(CommonUtil.convertObjToStr(getCbmDepoId().getDataForKey(objTO.getDepoId())));
       // setCboTransType(CommonUtil.convertObjToStr(getCbmTransType().getDataForKey(objTO.getTransType())));
            setTxtRMNo(objTO.getRmNumber());
            setTxtBuildingNo(objTO.getBuildingNo());
            setTxtaBuildingDes(objTO.getBuildingDes());
            setTxtRentAccHead(objTO.getRentAccHead());
            setTxtPenelAccHead(objTO.getPenelAccHead());
            setTxtNoticeAccHead(objTO.getNoticeAccHead());
            setTxtLegalAccHead(objTO.getLegalAccHead());
            setTxtArbAccHead(objTO.getArbAccHead());
            setTxtCourtGrpHead(objTO.getCourtGrpHead());
            setTxtExeGrpHead(objTO.getExeGrpHead());
            setCboStatus(objTO.getRStatus());
            setTxtRoomNo(objTO.getRoomNo());
            setTxtVersNo(objTO.getVersNo());
            setTxtRentFeq(objTO.getRentFeq());
            setTxtRentAmt(objTO.getRentAmt());
            setTxtPenelRate(objTO.getPenelRate());
            setTdtEffDate(objTO.getEffDate());
            setDataV(objTO.getDataV());
            setTxtAdvHead(objTO.getAdvHead());
            notifyObservers();
    }
        /** Resets all the UI Fields */
    public void resetForm(){
         setTxtRoomNo("");
            setTxtBuildingNo("");
            setTxtaBuildingDes("");
            setTxtRentAccHead("");
            setTxtPenelAccHead("");
            setTxtNoticeAccHead("");
            setTxtLegalAccHead("");
            setTxtArbAccHead("");
            setTxtCourtGrpHead("");
            setTxtExeGrpHead("");
            setCboStatus("");
            setTxtRoomNo("");
            setTxtVersNo("");
            setTxtRentFeq("");
            setTxtRentAmt(null);
            setTxtPenelRate(null);
            setTdtEffDate(null);
            setDataV(null);
            setTxtAdvHead("");
        notifyObservers();
    }
      public void setRMNo(java.lang.String txtRMNo) {
        this.txtRoomNo = txtRMNo;
        setChanged();
    }
       public String getRMNo() {
        return txtRoomNo;
       
    }
      
         /* Executes Query using the TO object */
    public void execute(String command) {
        try {
           // System.out.println("GET BOPRRR NO IN EDIT :="+geIndendTO(command));
           
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("RentTO", geRentTO(command));
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
       private RentTO geRentTO(String command){
        RentTO objTO = new RentTO();
        objTO.setCommand(command);
        objTO.setRmNumber(getTxtRMNo());
        objTO.setBuildingNo(getTxtBuildingNo());
        objTO.setBuildingDes(getTxtaBuildingDes());
        objTO.setRentAccHead(getTxtRentAccHead());
        objTO.setPenelAccHead(getTxtPenelAccHead());
        objTO.setNoticeAccHead(getTxtNoticeAccHead());
        objTO.setLegalAccHead(getTxtLegalAccHead());
        objTO.setArbAccHead(getTxtArbAccHead());
        objTO.setCourtGrpHead(getTxtCourtGrpHead());
        objTO.setExeGrpHead(getTxtExeGrpHead());
        objTO.setRStatus(getCboStatus());
        objTO.setRoomNo(getTxtRoomNo());
        objTO.setVersNo(getTxtVersNo());
        objTO.setRentFeq(getTxtRentFeq());
        objTO.setRentAmt(getTxtRentAmt());
        objTO.setPenelRate(getTxtPenelRate());
        objTO.setEffDate(getTdtEffDate());
       objTO.setDataV(getDataV()); 
       objTO.setAdvHead(getTxtAdvHead());
        objTO.setStatus_by(ProxyParameters.USER_ID);
        objTO.setBranchId(ProxyParameters.BRANCH_ID);
        System.out.println("userid-=-====="+ProxyParameters.USER_ID);
        
       if(command.equals(CommonConstants.TOSTATUS_INSERT)){
                objTO.setAuthorizeStatus("");
                objTO.setAuthorizeBy("");
                objTO.setAuthorizeDte(null);
                objTO.setStatus("CREATED");
        }
     
        return objTO;
    }
       
       /**
        * Getter for property txtRoomNo.
        * @return Value of property txtRoomNo.
        */
       public java.lang.String getTxtRMNo() {
           return txtRMNo;
       }
       
       /**
        * Setter for property txtRoomNo.
        * @param txtRoomNo New value of property txtRoomNo.
        */
       public void setTxtRMNo(java.lang.String txtRoomNo) {
           this.txtRMNo = txtRoomNo;
       }
       
       /**
        * Getter for property txtBuildingNo.
        * @return Value of property txtBuildingNo.
        */
       public java.lang.String getTxtBuildingNo() {
           return txtBuildingNo;
       }
       
       /**
        * Setter for property txtBuildingNo.
        * @param txtBuildingNo New value of property txtBuildingNo.
        */
       public void setTxtBuildingNo(java.lang.String txtBuildingNo) {
           this.txtBuildingNo = txtBuildingNo;
       }
       
       /**
        * Getter for property txtaBuildingDes.
        * @return Value of property txtaBuildingDes.
        */
       public java.lang.String getTxtaBuildingDes() {
           return txtaBuildingDes;
       }
       
       /**
        * Setter for property txtaBuildingDes.
        * @param txtaBuildingDes New value of property txtaBuildingDes.
        */
       public void setTxtaBuildingDes(java.lang.String txtaBuildingDes) {
           this.txtaBuildingDes = txtaBuildingDes;
       }
       
       /**
        * Getter for property txtRentAccHead.
        * @return Value of property txtRentAccHead.
        */
       public java.lang.String getTxtRentAccHead() {
           return txtRentAccHead;
       }
       
       /**
        * Setter for property txtRentAccHead.
        * @param txtRentAccHead New value of property txtRentAccHead.
        */
       public void setTxtRentAccHead(java.lang.String txtRentAccHead) {
           this.txtRentAccHead = txtRentAccHead;
       }
       
       /**
        * Getter for property txtPenelAccHead.
        * @return Value of property txtPenelAccHead.
        */
       public java.lang.String getTxtPenelAccHead() {
           return txtPenelAccHead;
       }
       
       /**
        * Setter for property txtPenelAccHead.
        * @param txtPenelAccHead New value of property txtPenelAccHead.
        */
       public void setTxtPenelAccHead(java.lang.String txtPenelAccHead) {
           this.txtPenelAccHead = txtPenelAccHead;
       }
       
       /**
        * Getter for property txtNoticeAccHead.
        * @return Value of property txtNoticeAccHead.
        */
       public java.lang.String getTxtNoticeAccHead() {
           return txtNoticeAccHead;
       }
       
       /**
        * Setter for property txtNoticeAccHead.
        * @param txtNoticeAccHead New value of property txtNoticeAccHead.
        */
       public void setTxtNoticeAccHead(java.lang.String txtNoticeAccHead) {
           this.txtNoticeAccHead = txtNoticeAccHead;
       }
       
       /**
        * Getter for property txtLegalAccHead.
        * @return Value of property txtLegalAccHead.
        */
       public java.lang.String getTxtLegalAccHead() {
           return txtLegalAccHead;
       }
       
       /**
        * Setter for property txtLegalAccHead.
        * @param txtLegalAccHead New value of property txtLegalAccHead.
        */
       public void setTxtLegalAccHead(java.lang.String txtLegalAccHead) {
           this.txtLegalAccHead = txtLegalAccHead;
       }
       
       /**
        * Getter for property txtArbAccHead.
        * @return Value of property txtArbAccHead.
        */
       public java.lang.String getTxtArbAccHead() {
           return txtArbAccHead;
       }
       
       /**
        * Setter for property txtArbAccHead.
        * @param txtArbAccHead New value of property txtArbAccHead.
        */
       public void setTxtArbAccHead(java.lang.String txtArbAccHead) {
           this.txtArbAccHead = txtArbAccHead;
       }
       
       /**
        * Getter for property txtCourtGrpHead.
        * @return Value of property txtCourtGrpHead.
        */
       public java.lang.String getTxtCourtGrpHead() {
           return txtCourtGrpHead;
       }
       
       /**
        * Setter for property txtCourtGrpHead.
        * @param txtCourtGrpHead New value of property txtCourtGrpHead.
        */
       public void setTxtCourtGrpHead(java.lang.String txtCourtGrpHead) {
           this.txtCourtGrpHead = txtCourtGrpHead;
       }
       
       /**
        * Getter for property txtExeGrpHead.
        * @return Value of property txtExeGrpHead.
        */
       public java.lang.String getTxtExeGrpHead() {
           return txtExeGrpHead;
       }
       
       /**
        * Setter for property txtExeGrpHead.
        * @param txtExeGrpHead New value of property txtExeGrpHead.
        */
       public void setTxtExeGrpHead(java.lang.String txtExeGrpHead) {
           this.txtExeGrpHead = txtExeGrpHead;
       }
       
       /**
        * Getter for property cboStatus.
        * @return Value of property cboStatus.
        */
       public java.lang.String getCboStatus() {
           return cboStatus;
       }
       
       /**
        * Setter for property cboStatus.
        * @param cboStatus New value of property cboStatus.
        */
       public void setCboStatus(java.lang.String cboStatus) {
           this.cboStatus = cboStatus;
       }
       
       /**
        * Getter for property txtRoomNo1.
        * @return Value of property txtRoomNo1.
        */
       public java.lang.String getTxtRoomNo() {
           return txtRoomNo;
       }
       
       /**
        * Setter for property txtRoomNo1.
        * @param txtRoomNo1 New value of property txtRoomNo1.
        */
       public void setTxtRoomNo(java.lang.String txtRoomNo) {
           this.txtRoomNo = txtRoomNo;
       }
       
       /**
        * Getter for property txtVersNo.
        * @return Value of property txtVersNo.
        */
       public java.lang.String getTxtVersNo() {
           return txtVersNo;
       }
       
       /**
        * Setter for property txtVersNo.
        * @param txtVersNo New value of property txtVersNo.
        */
       public void setTxtVersNo(java.lang.String txtVersNo) {
           this.txtVersNo = txtVersNo;
       }
       
       /**
        * Getter for property txtRentFeq.
        * @return Value of property txtRentFeq.
        */
       public java.lang.String getTxtRentFeq() {
           return txtRentFeq;
       }
       
       /**
        * Setter for property txtRentFeq.
        * @param txtRentFeq New value of property txtRentFeq.
        */
       public void setTxtRentFeq(java.lang.String txtRentFeq) {
           this.txtRentFeq = txtRentFeq;
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
        * Getter for property txtPenelRate.
        * @return Value of property txtPenelRate.
        */
       public java.lang.Double getTxtPenelRate() {
           return txtPenelRate;
       }
       
       /**
        * Setter for property txtPenelRate.
        * @param txtPenelRate New value of property txtPenelRate.
        */
       public void setTxtPenelRate(java.lang.Double txtPenelRate) {
           this.txtPenelRate = txtPenelRate;
       }
       
       /**
        * Getter for property tdtEffDate.
        * @return Value of property tdtEffDate.
        */
       public java.util.Date getTdtEffDate() {
           return tdtEffDate;
       }
       
       /**
        * Setter for property tdtEffDate.
        * @param tdtEffDate New value of property tdtEffDate.
        */
       public void setTdtEffDate(java.util.Date tdtEffDate) {
           this.tdtEffDate = tdtEffDate;
       }
       
       /**
        * Getter for property dataV.
        * @return Value of property dataV.
        */
       public java.util.Vector getDataV() {
           return dataV;
       }
       
       /**
        * Setter for property dataV.
        * @param dataV New value of property dataV.
        */
        public void setDataV(java.util.Vector dataV) {
            this.dataV = dataV;
        }

    /* Filling up the the ComboBox in the UI*/
        public String getTxtAdvHead() {
            return txtAdvHead;
         }

        public void setTxtAdvHead(String txtAdvHead) {
            this.txtAdvHead = txtAdvHead;
        }
        
        public java.lang.String getLblStatus() {
            return lblStatus;
         }
   
    
   
    
}
