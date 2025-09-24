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

package com.see.truetransact.ui.directorboardmeeting;

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
import com.see.truetransact.transferobject.directorboardmeeting.DirectorBoardTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author  
 *
 */
public class DirectorBoardOB extends CObservable {
   /** private String txtEmpTransferID = "";
    private String txtEmpID = "";
    private String txtCurrBran = "";
    private String txtLastWorkingDay = "";
    private String txtDoj = "";
    private String txtStatusBy = "";
    private boolean rdoApp_Yes = false;
    private boolean rdoOff_Yes = false;
    private String txtStatus = "";
    private String CreatedDt="";
    private String empName="";
    private String currBranName="";
    */
   //------------------------------------------------------------------------------------------------------------------------------------------ 
    private String cboBoMember="";
    private Double txtSittingFeeAmount=null;
    private String txtdirectorBoardNo="";
    private String txtD1="";
    private String txtD2="";
    private boolean rdoYes1 = false;
    private boolean rdoNo1 = false;
    private boolean rdoYes2 = false;
    private boolean rdoNo2 = false;
    private int actionType;
    private String txtStatus = "";
     private int result;
     private HashMap _authorizeMap;
  private  DirectorBoardTO objDirectorBoardTO ;
     private String CreatedDt="";
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private String txtStatusBy = "";
     private HashMap map;
     private ProxyFactory proxy;
     private final static Logger log = Logger.getLogger(DirectorBoardOB.class);
     private ComboBoxModel cbmBoardMember;
       private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int _actionType;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
  //-------------------------------------------------------------------------------------------------------------------------------------------
   /** private String cboTransferBran="";
    private ComboBoxModel cbmRoleInCurrBran;
    private String cboRoleInCurrBran="";
    private ComboBoxModel cbmRoleInTranBran;
    private String cboRoleInTranBran="";
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txtNewAcctName = "";
    
    private final static Logger log = Logger.getLogger(EmpTransferOB.class);
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
    private EmpTransferOB empTransferOB;
    EmpTransferRB objEmpTransferRB = new EmpTransferRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private EmpTransferTO objEmpTransferTO;
    */
      private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates a new instance of TDS MiantenanceOB */
    public DirectorBoardOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DirectorBoardJNDI");
            map.put(CommonConstants.HOME, "DirectorBoardHome");
            map.put(CommonConstants.REMOTE, "DirectorBoard");
           fillDropdown();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
      lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"getAllBoardMem");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            param.put(CommonConstants.PARAMFORQUERY, where);
            where = null;
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmBoardMember = new ComboBoxModel(key,value);
            
          
     
    }
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
     
    
//    /** To get data for comboboxes */
//    private HashMap populateData(HashMap obj)  throws Exception{
//        keyValue = proxy.executeQuery(obj,lookupMap);
//        log.info("Got HashMap");
//        return keyValue;
//   }
    
  
   
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
          
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
//            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            parseException.logException(e,true);
        }
    }
    
    
    
//    
//     public void execute(String command) {
//        try {
//            System.out.println("GET BOPRRR NO IN EDIT :="+getDirectorBoardTO(command));
//           
//            HashMap term = new HashMap();
//            term.put(CommonConstants.MODULE, getModule());
//            term.put(CommonConstants.SCREEN, getScreen());
//            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//            term.put("DirectorBoardTO", getDirectorBoardTO(command));
//             System.out.println("GET term IN EDIT :="+term);
//              System.out.println("GET map IN EDIT :="+map);
//            HashMap proxyReturnMap = proxy.execute(term, map);
////            setProxyReturnMap(proxyReturnMap);
//            setResult(getActionType());
//            System.out.println("ACTIONN TYPEEE==="+getActionType());
//        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            //parseException.logException(e,true);
//              System.out.println("Error in execute():"+e);
//        }
//    }
        
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final DirectorBoardTO objDirectorBoardTO = new DirectorBoardTO();
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put("USER", ProxyParameters.USER_ID);
        data.put("SCREEN",getScreen());
        if (get_authorizeMap() == null) {
            data.put("DirectorBoardMeeting", setDirectorBoardTO());
            if (CommonUtil.convertObjToDouble(getTxtSittingFeeAmount()) > 0 && allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                data.put("TransactionTo", allowedTransactionDetailsTO);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in DirectorBoard OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("DIRECTOR_ID")));
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
           objDirectorBoardTO = (DirectorBoardTO)((List) data.get("DirectorBoardTO")).get(0);
            List list = (List) data.get("TransactionTO");
            transactionOB.setDetails(list);
           System.out.println("obj populate?>>>>>"+objDirectorBoardTO.getCboBoMember());
            populateDirectorBoardData(objDirectorBoardTO);
            ttNotifyObservers();
        }catch(Exception e){
//            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    //-----------------------------------------------------------------------------------------------------------------------------------------------
    public DirectorBoardTO setDirectorBoardTO() {
        
        final DirectorBoardTO objDirectorBoardTO = new DirectorBoardTO();
        try{
           if (isRdoYes1() == true) objDirectorBoardTO.setApplType("ATTENDED");
           else objDirectorBoardTO.setApplType("NOT ATTENDED");
            if (isRdoYes2() == true) objDirectorBoardTO.setApplType1("SITTING FEE PAID");
           else objDirectorBoardTO.setApplType1("SITTING FEE NOT PAID");
           
           
            objDirectorBoardTO.setStatusBy(ProxyParameters.USER_ID);
            objDirectorBoardTO.setSittingFeeAmount(getTxtSittingFeeAmount());
            System.out.println("amt in to and ob"+objDirectorBoardTO.getSittingFeeAmount()+"---"+getTxtSittingFeeAmount());
           objDirectorBoardTO.setMeetngDate(DateUtil.getDateMMDDYYYY(getTxtMeetingDate()));
            objDirectorBoardTO.setPaidDate(DateUtil.getDateMMDDYYYY(getTxtPaidDate()));
           objDirectorBoardTO.setCommand(getCommand());
            objDirectorBoardTO.setStatus(getAction());
            objDirectorBoardTO.setStatusBy(TrueTransactMain.USER_ID);
            objDirectorBoardTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objDirectorBoardTO.setCboBoMember(getCboBoMember());
            objDirectorBoardTO.setDirectorBoardNo(getTxtdirectorBoardNo());
         
            //   objDirectorBoardTO.setCboToBoardMember(CommonUtil.convertObjToStr(getCbmBoardMember().getKeyForSelected()));
          // objDirectorBoardTO.setCboBoMember(ogetCboBoardMember());
            
            //-------------------------------------------------------------------------------------------------------------------------------------------
          
//           objEmpTransferTO.setRoleInTransferBran(CommonUtil.convertObjToStr(getCbmRoleInTranBran().getKeyForSelected()));
//           objEmpTransferTO.setTransferBran(CommonUtil.convertObjToStr(getCbmTransferBran().getKeyForSelected()));
//           objEmpTransferTO.setEmpName(getEmpName());
//           objEmpTransferTO.setCurrBranName(getCurrBranName());
//           if(getCommand().equalsIgnoreCase("INSERT")){
//           objEmpTransferTO.setCreatedBy(TrueTransactMain.USER_ID);
//           objEmpTransferTO.setCreatedDt(currDt);
//           }
//            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
//              objEmpTransferTO.setEmpTransferID(getTxtEmpTransferID());  
//            }
       }catch(Exception e){
           // log.info("Error In setDirectoryBoardMeetingData()");
            e.printStackTrace();
        }
        return objDirectorBoardTO;
    }
    
    private void populateDirectorBoardData(DirectorBoardTO objDirectorBoardTO) throws Exception{
        
        //this.setTxtSittingFeeAmount(CommonUtil.convertObjToStr(objDirectorBoardTO.getTxtSittingFeeAmount()));
        this.setTxtSittingFeeAmount(objDirectorBoardTO.getSittingFeeAmount());
         this.setTxtMeetingDate(CommonUtil.convertObjToStr(objDirectorBoardTO.getMeetngDate()));
         this.setTxtPaidDate(CommonUtil.convertObjToStr(objDirectorBoardTO.getPaidDate()));
        this.setCboBoMember(CommonUtil.convertObjToStr(objDirectorBoardTO.getCboBoMember()));
        System.out.println("board memberrr>>>>"+getCboBoMember());
         // getCbmBoardMember().setKeyForSelected(CommonUtil.convertObjToStr(objDirectorBoardTO.getCboToBoardMember()));
        if(objDirectorBoardTO.getApplType()!=null)
            if(objDirectorBoardTO.getApplType().equalsIgnoreCase("ATTENDED")){
                setRdoYes1(true);
                setRdoNo1(false);
            }
            else{
                setRdoNo1(true);
               setRdoYes1(false);
            }
      if(objDirectorBoardTO.getApplType()!=null)
            if(objDirectorBoardTO.getApplType1().equalsIgnoreCase("SITTING FEE PAID")){
                setRdoYes2(true);
                 setRdoNo2(false);
            }
            else{
                setRdoNo2(true);
                setRdoYes2(false);
            }
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objDirectorBoardTO.getStatusBy()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objDirectorBoardTO.getCreatedDt()));
        this.setStatusBy(CommonUtil.convertObjToStr(objDirectorBoardTO.getStatusBy()));
        this.setTxtdirectorBoardNo(objDirectorBoardTO.getDirectorBoardNo());
        System.out.println("board no....."+getTxtdirectorBoardNo()+"to value"+objDirectorBoardTO.getDirectorBoardNo());
        setChanged();
        notifyObservers();
    }
    
    
    public void resetForm(){
        setCboBoMember("");
       // cboBoardMember="";
        setTxtSittingFeeAmount(null);
        setTxtMeetingDate("");
        setTxtPaidDate("");
        setRdoNo1(true);
        setRdoNo2(true);
        setRdoYes1(false);
        setRdoYes2(false);
        setTransactionOB(null);
        allowedTransactionDetailsTO = null;
        setChanged();
        set_authorizeMap(null);
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
     *
     *
     * /**
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
    
    
    
   
    
    
    
   //-------------------------------------------------------
    
      public boolean isRdoNo2() {
        return rdoNo2;
    }
    
   
    public void setRdoNo2(boolean rdoNo2) {
        this.rdoNo2 = rdoNo2;
    }
    
    public boolean isRdoYes2() {
        return rdoYes2;
    }
    
   
    public void setRdoYes2(boolean rdoYes2) {
        this.rdoYes2 = rdoYes2;
    }
   
    public boolean isRdoNo1() {
        return rdoNo1;
    }
    
   
    public void setRdoNo1(boolean rdoNo1) {
        this.rdoNo1 = rdoNo1;
    }
    
    
    
     public boolean isRdoYes1() {
        return rdoYes1;
    }
    
   
    public void setRdoYes1(boolean rdoYes1) {
        this.rdoYes1 = rdoYes1;
    }
    
    //combobox board member
 /*    public com.see.truetransact.clientutil.ComboBoxModel getCbmBoardMember() {
        return cboBoardMember;
    }
    
    
    public void setCbmBoardMember(com.see.truetransact.clientutil.ComboBoxModel cboBoardMember) {
        this.cboBoardMember = cboBoardMember;
    }
    */
   //sitting fee amt textfield
    
    public java.lang.Double getTxtSittingFeeAmount() {
        return txtSittingFeeAmount;
    }
    
 
    public void setTxtSittingFeeAmount(java.lang.Double txtSittingFeeAmount) {
        this.txtSittingFeeAmount = txtSittingFeeAmount;
    }
    
     public java.lang.String getTxtMeetingDate() {
        return txtD1;
    }
    
  
    public void setTxtMeetingDate(java.lang.String txtD1) {
        this.txtD1 = txtD1;
    }
    
     public java.lang.String getTxtPaidDate() {
        return txtD2;
    }
    
   
    public void setTxtPaidDate(java.lang.String txtD2) {
        this.txtD2 = txtD2;
    }
    
    /**
     * Getter for property cboBoMember.
     * @return Value of property cboBoMember.
     */
    public String getCboBoMember() {
        return cboBoMember;
    }    

    /**
     * Setter for property cboBoMember.
     * @param cboBoMember New value of property cboBoMember.
     */
    public void setCboBoMember(String cboBoMember) {
        this.cboBoMember = cboBoMember;
    }    
   
    /**
     * Getter for property txtdirectorBoardNo.
     * @return Value of property txtdirectorBoardNo.
     */
    public String getTxtdirectorBoardNo() {
        return txtdirectorBoardNo;
    }
    
    /**
     * Setter for property txtdirectorBoardNo.
     * @param txtdirectorBoardNo New value of property txtdirectorBoardNo.
     */
    public void setTxtdirectorBoardNo(String txtdirectorBoardNo) {
        this.txtdirectorBoardNo = txtdirectorBoardNo;
    }
    
    /**
     * Getter for property cbmBoardMember.
     * @return Value of property cbmBoardMember.
     */
    public ComboBoxModel getCbmBoardMember() {
        return cbmBoardMember;
    }
    
    /**
     * Setter for property cbmBoardMember.
     * @param cbmBoardMember New value of property cbmBoardMember.
     */
    public void setCbmBoardMember(ComboBoxModel cbmBoardMember) {
        this.cbmBoardMember = cbmBoardMember;
    }
    
    /**
     * Getter for property _actionType.
     * @return Value of property _actionType.
     */
    public int get_actionType() {
        return _actionType;
    }
    
    /**
     * Setter for property _actionType.
     * @param _actionType New value of property _actionType.
     */
    public void set_actionType(int _actionType) {
        this._actionType = _actionType;
        setStatus();
        setChanged();
    }

    public TransactionOB getTransactionOB() {
        return transactionOB;
    }

    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    
}
    //-----------------------------------------------------------
   
   
    
    