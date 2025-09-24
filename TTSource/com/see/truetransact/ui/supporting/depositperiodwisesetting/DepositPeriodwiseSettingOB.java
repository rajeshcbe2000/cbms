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

package com.see.truetransact.ui.supporting.depositperiodwisesetting;

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
import com.see.truetransact.transferobject.supporting.depositperiodwisesetting.DepositPeriodwiseSettingTO;


/**
 *
 * @author  
 *
 */
public class DepositPeriodwiseSettingOB extends CObservable {
     private String txtid = "";
     private String txtid1 = "";
     private String txtID = "";
     private String txtID1 = "";
     private String txtid2 = "";
      private String txtid5 = ""; 
      private String txtid6 = ""; 
     private String txtPeriodName = "";
     private String txtPeriodFrom= "";
     private String txtPeriodTo = "";
     private String txtPriority = "";
     
     private String txtPeriodName1 = "";
     private String txtPeriodFrom1= "";
     private String txtPeriodTo1 = "";
     private String txtPriority1 = "";
     
     private String txtamountrange = "";
     private String txtfromamount = "";
     private String txttoamount = "";
      private String txtpriority = "";
      
      private String txtamountrange1 = "";
     private String txtfromamount1 = "";
     private String txttoamount1 = "";
      private String txtpriority1 = "";
      
      private String txtPriority2= "";
      private String txtdesc= "";
      private String txtPeriodFrom2= "";
      private String txtPeriodTo2= "";
      
      
  
      
       private String txtDoubtfrom= "";
       private String txtDoubtto= "";
       private String txtBadfrom= "";
       private String txtBadto= "";
       private String txtDocdoubtfrom= "";
       private String txtDocdoubtto= "";
       private String txtDocbadfrom= "";
       private String txtDocbadto= "";
       private String txtDoubtnarra= "";
       private String txtBadnarra= "";
       private String txtDocdoubtnara= "";
       private String txtDocbadnara= "";
  
       private String cboFluidType;
       private String txtModule;
       private String cboDescription;
       private String txtPercentage= "";
  
       private String cboPeriodType;
       private String cboPeriodType1;
       private String cboPeriodType2;
       private String cboPeriodType3;
       private String cboPeriodType4;
      private ComboBoxModel cbmid;
      private ComboBoxModel cbmDesc;
        
      private HashMap lookUpHash;
     private HashMap lookupMap;
     private String txtStatusBy = "";
     private String txtStatus = "";
     private String CreatedDt="";
     private String currBranName="";
    private int pan;
     private HashMap _authorizeMap;
     private int result;
     private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
     private final static Logger log = Logger.getLogger(DepositPeriodwiseSettingOB.class);
     private final static ClientParseException parseException = ClientParseException.getInstance();
     private HashMap map;
     private ProxyFactory proxy;
    //for filling dropdown
     
     private HashMap param;
     private HashMap keyValue;
     private ArrayList key;
     private ArrayList value;
     private int actionType;
    final int PERIOD=0;
    final int AMOUNT=1;
    final int LPERIOD=2;
    final int LAMOUNT=3;
    
    final int BDS=5;
    final int FP=6;


     private DepositPeriodwiseSettingOB depositPeriodwiseSettingOB;
     DepositPeriodwiseSettingRB depositPeriodwiseSettingRB = new DepositPeriodwiseSettingRB();
     private static final CommonRB objCommonRB = new CommonRB();
     private DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO;
     private Date currDt = null;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public DepositPeriodwiseSettingOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositPeriodwiseSettingJNDI");
            map.put(CommonConstants.HOME, "DepositPeriodwiseSetting.DepositsHome");
            map.put(CommonConstants.REMOTE, "DepositPeriodwiseSetting.Deposits");
            fillDropdown();
           
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
  
     
         private void fillDropdown() throws Exception
    {
        HashMap lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
         lookup_keys.add("FLUID PARAMS");
         lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
         keyValue = ClientUtil.populateLookupData(lookUpHash);
        System.out.println("key key"+keyValue);
        getKeyValue((HashMap)keyValue.get("FLUID PARAMS"));
        System.out.println("key key"+key+ "ggggggg"+value);
         
         cbmDesc = new ComboBoxModel(key,value);
         setChanged();
         ttNotifyObservers();
    }
     
          
       private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
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
    public void doAction(int pan) {
        try {
          
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform(pan);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }

    
   
        
    /** To perform the necessary action */
    private void doActionPerform(int pan) throws Exception{
        
        final HashMap data = new HashMap();
         data.put("COMMAND",getCommand());
         System.out.println("pan pan pan"+pan);
         
         if(pan==0){
              final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
              if(get_authorizeMap() == null){
                 data.put("DepositPeriodwiseSetting",setDepositsData());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
          }
         else if(pan==1){
               final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO=new DepositPeriodwiseSettingTO();
               if(get_authorizeMap() == null){
                 data.put("DepositAmountwiseSetting",setDepositsDatacpy());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
         }
         else if(pan==2){
              final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
              if(get_authorizeMap() == null){
                 data.put("LoanPeriodwiseSetting",setDepositsData1());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
          }
         
         else if(pan==3){
               final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO=new DepositPeriodwiseSettingTO();
               if(get_authorizeMap() == null){
                 data.put("LoanAmountwiseSetting",setDepositsDatacpy2());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
         }
         
          else if(pan==4){
               final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO=new DepositPeriodwiseSettingTO();
               if(get_authorizeMap() == null){
                 data.put("LoanODPeriodwiseSetting",setDepositsData2());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
         }
         
           else if(pan==5){
               final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO=new DepositPeriodwiseSettingTO();
               if(get_authorizeMap() == null){
                 data.put("BadDoubtfullsetting",setDepositsData3());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
         }
         
           else if(pan==6){
               final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO=new DepositPeriodwiseSettingTO();
               if(get_authorizeMap() == null){
                 data.put("Fluidparameter",setDepositsData6());
               }
              else{
                 data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
              }
         }
         System.out.println("data data"+data+" map map map  "+map);
             
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        System.out.println("data in DepositPeriodwiseSettingy OB return... : " + proxyResultMap);
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
        return command;
    }
    
    private String getAction(){
       String action = null;
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
             
                System.out.println("whereMapwhereMap"+whereMap);
                 System.out.println("Mapob21;[,lopjj"+map);
                final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
                System.out.println("data"+data);
                objDepositPeriodwiseSettingTO = (DepositPeriodwiseSettingTO)((List) data.get("DepositsTO")).get(0);
                populateData(objDepositPeriodwiseSettingTO);
                ttNotifyObservers();
         }catch(Exception e){
                parseException.logException(e,true);
        }
    }
    
   
    
        
    /** To populate data into the screen */
    public DepositPeriodwiseSettingTO setDepositsData() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           objDepositPeriodwiseSettingTO.setperiodtype(getcboPeriodType());
           objDepositPeriodwiseSettingTO.setperiodname(gettxtPeriodName());
           objDepositPeriodwiseSettingTO.setperiodfrom(gettxtPeriodFrom());
           objDepositPeriodwiseSettingTO.setperiodto(gettxtPeriodTo());
           objDepositPeriodwiseSettingTO.setpriority(gettxtPriority());
           objDepositPeriodwiseSettingTO.setid(getTxtid());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
    
    //////////////////////////////////////////////nithi
    ////////////////////////////////////////////////////////
    ////////////////////////////////////
    
      public DepositPeriodwiseSettingTO setDepositsData1() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+getcboPeriodType1());
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+getcboPeriodType2());
           objDepositPeriodwiseSettingTO.setperiodtype1(getcboPeriodType1());
           objDepositPeriodwiseSettingTO.setperiodtype2(getcboPeriodType2());
           objDepositPeriodwiseSettingTO.setperiodrange(gettxtPeriodName1());
           objDepositPeriodwiseSettingTO.setperiodfrom1(gettxtPeriodFrom1());
           objDepositPeriodwiseSettingTO.setperiodto1(gettxtPeriodTo1());
           objDepositPeriodwiseSettingTO.setpriority1(gettxtPriority1());
           objDepositPeriodwiseSettingTO.setid(getTxtid1());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
    
     
    
     public DepositPeriodwiseSettingTO setDepositsDatacpy() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
            
           
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           objDepositPeriodwiseSettingTO.setamountrange(gettxtamountrange());
           objDepositPeriodwiseSettingTO.setfromamount(gettxtfromamount());
           objDepositPeriodwiseSettingTO.settoamount(gettxttoamount());
           objDepositPeriodwiseSettingTO.setpriorityy(gettxtpriority());
           objDepositPeriodwiseSettingTO.setid(getTxtID());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsDatacpy()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
    
     
     ///////////////////////
     //////////////////////////////////
     /////////////////////////////////
     
          public DepositPeriodwiseSettingTO setDepositsDatacpy2() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
            
           
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
                
          System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv----->"+gettxtamountrange1());
           System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv----->"+gettxtfromamount1());
            System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv----->"+gettxttoamount1());
             System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv----->"+gettxtpriority1());
              System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv----->"+getTxtID1());
           objDepositPeriodwiseSettingTO.setamountrange1(gettxtamountrange1());
           objDepositPeriodwiseSettingTO.setfromamount1(gettxtfromamount1());
           objDepositPeriodwiseSettingTO.settoamount1(gettxttoamount1());
           objDepositPeriodwiseSettingTO.setpriorityy1(gettxtpriority1());
           objDepositPeriodwiseSettingTO.setid(getTxtID1());
           
           
           objDepositPeriodwiseSettingTO.setBranCode(null);
           if(getCommand().equalsIgnoreCase("INSERT")){
           objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
           objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsDatacpy()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
          
           public DepositPeriodwiseSettingTO setDepositsData2() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+getcboPeriodType1());
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+getcboPeriodType2());
           objDepositPeriodwiseSettingTO.setPeriodtype3(getcboPeriodType3());
           objDepositPeriodwiseSettingTO.setPeriodtype4(getcboPeriodType4());
           objDepositPeriodwiseSettingTO.setPriority2(gettxtPriority2());
           objDepositPeriodwiseSettingTO.setDesc(gettxtdesc());
           objDepositPeriodwiseSettingTO.setPeriodfrom3(gettxtPeriodFrom2());
           objDepositPeriodwiseSettingTO.setPeriodto4(gettxtPeriodTo2());
           objDepositPeriodwiseSettingTO.setid(gettxtid2());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            public DepositPeriodwiseSettingTO setDepositsData3() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+gettxtDoubtfrom());
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+gettxtDoubtto());
           
           objDepositPeriodwiseSettingTO.setdoubtfrom(gettxtDoubtfrom());
           objDepositPeriodwiseSettingTO.setdoubtto(gettxtDoubtto());
           objDepositPeriodwiseSettingTO.setbadfrom(gettxtBadfrom());
           objDepositPeriodwiseSettingTO.setbadto(gettxtBadto());
           objDepositPeriodwiseSettingTO.setdocdoubtfrom(gettxtDocdoubtfrom());
           objDepositPeriodwiseSettingTO.setdocdoubtto(gettxtDocdoubtto());
           objDepositPeriodwiseSettingTO.setdocbadfrom(gettxtDocbadfrom());
           objDepositPeriodwiseSettingTO.setdocbadto(gettxtDocbadto());
           objDepositPeriodwiseSettingTO.setdoubtnarra(gettxtDoubtnarra());
           objDepositPeriodwiseSettingTO.setbadnarra(gettxtBadnarra());
           objDepositPeriodwiseSettingTO.setdocdoubtnara(gettxtDocdoubtnara());
           objDepositPeriodwiseSettingTO.setdocbadnara(gettxtDocbadnara());
            
           objDepositPeriodwiseSettingTO.setid(getTxtid5());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
            
         public DepositPeriodwiseSettingTO setDepositsData6() {
        
        final DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO = new DepositPeriodwiseSettingTO();
        try{
           objDepositPeriodwiseSettingTO.setCommand(getCommand());
           objDepositPeriodwiseSettingTO.setStatus(getAction());
           objDepositPeriodwiseSettingTO.setStatusBy(TrueTransactMain.USER_ID);
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+getcboFluidType());
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+gettxtModule());
           System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+cbmDesc.getSelectedItem());
        //   System.out.println("getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1getcboPeriodType1"+cbmdesc.getModel().getKeyForSelected());
           objDepositPeriodwiseSettingTO.setFluidType(getcboFluidType());
         
           objDepositPeriodwiseSettingTO.setModule(gettxtModule());
      
            objDepositPeriodwiseSettingTO.setDescription(CommonUtil.convertObjToStr((String)cbmDesc.getSelectedItem()));
           objDepositPeriodwiseSettingTO.setPercentage(gettxtPercentage());
            objDepositPeriodwiseSettingTO.setid(getTxtid6());
           objDepositPeriodwiseSettingTO.setBranCode(null);
           
           if(getCommand().equalsIgnoreCase("INSERT")){
                objDepositPeriodwiseSettingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDepositPeriodwiseSettingTO.setCreatedDt(currDt);
           }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
              //objVisitorsDiaryTO.setEmpTransferID(getTxtEmpTransferID());  
            }
        }catch(Exception e){
            log.info("Error In setDepositsData()");
            e.printStackTrace();
        }
        return objDepositPeriodwiseSettingTO;
    }
     
   
    private void populateData(DepositPeriodwiseSettingTO objDepositPeriodwiseSettingTO) throws Exception{
       
          System.out.println("pan"+pan);
          pan=getPan();
        if(pan==0){
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodname());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodname());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodname());
        this.settxtPeriodName(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodname()));
        this.settxtPeriodFrom(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodfrom()));
        this.settxtPeriodTo(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodto()));
        this.settxtPriority(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getpriority()));
        this.setcboPeriodType(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodtype()));
        this.setTxtid(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        }
        else if(pan==1){
            
             System.out.println("           kkkbjgfhdfgd                           "+objDepositPeriodwiseSettingTO.getamountrange());
            this.settxtamountrange(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getamountrange()));
        this.settxtfromamount(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getfromamount()));
        this.settxttoamount(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.gettoamount()));
        this.settxtpriority(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getpriorityy()));
        this.setTxtID(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        }
        else  if(pan==2){
       
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodrange());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodfrom1());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getperiodto1());
        
        this.settxtPeriodName1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodrange()));
        this.settxtPeriodFrom1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodfrom1()));
        this.settxtPeriodTo1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodto1()));
        this.settxtPriority1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getpriority1()));
        this.setcboPeriodType1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodtype1()));
        this.setcboPeriodType2(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodtype2()));
        this.setTxtid(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        }  
              
        else if(pan==3){
            
             System.out.println("kkkbjgfhdfgd                           "+objDepositPeriodwiseSettingTO.getamountrange1());
        this.settxtamountrange1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getamountrange1()));
        this.settxtfromamount1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getfromamount1()));
        this.settxttoamount1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.gettoamount1()));
        this.settxtpriority1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getpriorityy1()));
        this.setTxtID1(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        }
        
         else  if(pan==4){
       
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getPriority2());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getDesc());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getPeriodfrom3());
        
        this.settxtPriority2(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPriority2()));
        this.settxtdesc(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getDesc()));
        this.settxtPeriodFrom2(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPeriodfrom3()));
        this.settxtPeriodTo2(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPeriodto4()));
        this.setcboPeriodType3(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPeriodtype3()));
        this.setcboPeriodType4(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPeriodtype4()));
        this.settxtid2(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        } 
          
        else  if(pan==5){
       
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getPriority2());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getDesc());
        System.out.println("                                      "+objDepositPeriodwiseSettingTO.getPeriodfrom3());
        
        this.settxtDoubtfrom(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdoubtfrom()));
        this.settxtDoubtto(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdoubtto()));
        this.settxtBadfrom(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getbadfrom()));
        this.settxtBadto(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getbadto()));
        this.settxtDocdoubtfrom(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocdoubtfrom()));
        this.settxtDocdoubtto(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocdoubtto()));
        this.settxtDocbadfrom(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocbadfrom()));
        this.settxtDocbadto(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocbadto()));
        this.settxtDoubtnarra(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdoubtnarra()));
        this.settxtBadnarra(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getbadnarra()));
        this.settxtDocdoubtnara(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocdoubtnara()));
        this.settxtDocbadnara(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getdocbadnara()));
        
        this.setTxtid5(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid()));
        } 
           else  if(pan==6){
               
               System.out.println("getFluidType()"+objDepositPeriodwiseSettingTO.getFluidType());
               System.out.println("getFluidType()"+objDepositPeriodwiseSettingTO.getModule());
               System.out.println("getFluidType()"+objDepositPeriodwiseSettingTO.getPercentage());
                System.out.println("getFluidType()"+objDepositPeriodwiseSettingTO.getDescription());
               
              this.setcboFluidType(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getFluidType()));
             this.settxtModule(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getModule()));
            //  this.setcboModule((String) getCbmid().getDataForKey(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getModule())));
              //this.setcboDescription(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getDescription())); 
              this.setcboDescription(objDepositPeriodwiseSettingTO.getDescription());//(String) getCbmDesc().getDataForKey(CommonUtil.convertObjToStr(
               System.out.println("getFluid()6875875"+getcboDescription());
              this.settxtPercentage(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getPercentage())); 
              this.setTxtid6(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getid())); 
           }
        //  this.set(CommonUtil.convertObjToStr(objDepositPeriodwiseSettingTO.getperiodtype()));
        setChanged();
        notifyObservers();
    }
    
    private void initUIComboBoxModel()
        {
        
        cbmDesc = new ComboBoxModel();
        }
    
    
    
    public void resetForm(){
        
       
        
       settxtPeriodName("");
       settxtPeriodFrom("");
       settxtPeriodTo("");
       settxtPriority("");
       setcboPeriodType("");
       
       settxtamountrange("");
       settxtfromamount("");
       settxttoamount("");
       settxtpriority("");
       
       settxtPeriodName1("");
       settxtPeriodFrom1("");
       settxtPeriodTo1("");
       settxtPriority1("");
       setcboPeriodType1("");
       setcboPeriodType2("");
       
       
         
       settxtamountrange1("");
       settxtfromamount1("");
       settxttoamount1("");
       settxtpriority1("");
       
        settxtPriority2("");
        settxtdesc("");
        settxtPeriodFrom2("");
        settxtPeriodTo2("");
        setcboPeriodType3("");
        setcboPeriodType4("");
        
        
       	settxtDoubtfrom("");
        settxtDoubtto("");
        settxtBadfrom("");
        settxtBadto("");
        settxtDocdoubtfrom("");
        settxtDocdoubtto("");
        settxtDocbadfrom("");
        settxtDocbadto("");
        settxtDoubtnarra("");
        settxtBadnarra("");
        settxtDocdoubtnara("");
        settxtDocbadnara("");
        
        setcboFluidType("");
	settxtModule("");
	setcboDescription("");	
	settxtPercentage("");
       
       setChanged();
       ttNotifyObservers();
    }
   
    /** To set the status based on ActionType, either New, Edit, etc., */
   
    public void setStatus(){
       this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by 
     () method */
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
    
       public String gettxtPeriodName() {
        return txtPeriodName;
    }
    
    
    public void settxtPeriodName(String txtPeriodName) {
        this.txtPeriodName = txtPeriodName;
    }
    
           public String gettxtPeriodName1() {
        return txtPeriodName1;
    }
    
    
    public void settxtPeriodName1(String txtPeriodName1) {
        this.txtPeriodName1 = txtPeriodName1;
    }
    
        public String gettxtamountrange() {
        return txtamountrange;
    }
    
        public void settxtamountrange(String txtamountrange) {
        this.txtamountrange = txtamountrange;
    }
    
      public String gettxtamountrange1() {
        return txtamountrange1;
    }
    
    /**
     * Setter for property txaInstNameAddress.
     * @param txaInstNameAddress New value of property txaInstNameAddress.
     */
    public void settxtamountrange1(String txtamountrange1) {
        this.txtamountrange1 = txtamountrange1;
    }
    
    
   
         public void settxtpan(int pan) {
        this.pan = pan;
    }
    
    
         public int gettxtpan() {
        return pan;
    }
       
    public String gettxtfromamount() {
        return txtfromamount;
    }
    
    /**
     * Setter for property txaInstNameAddress.
     * @param txaInstNameAddress New value of property txaInstNameAddress.
     */
    public void settxtfromamount(String txtfromamount) {
        this.txtfromamount =txtfromamount;
    }
    
     public String gettxtfromamount1() {
        return txtfromamount1;
    }
    
    
    public void settxtfromamount1(String txtfromamount1) {
        this.txtfromamount1 =txtfromamount1;
    }
    
    
         public String gettxttoamount() {
        return  txttoamount;
    }
    
      public void settxttoamount(String  txttoamount) {
        this.txttoamount = txttoamount;
    }
    
    
     public String gettxttoamount1() {
        return  txttoamount1;
    }
    
    /**
     * Setter for property txaInstNameAddress.
     * @param txaInstNameAddress New value of property txaInstNameAddress.
     */
    public void settxttoamount1(String  txttoamount1) {
        this.txttoamount1 = txttoamount1;
    }
    
    
    
    
    
    
    /**
     * Getter for property txaNameAddress.
     * @return Value of property txaNameAddress.
     */
    public String gettxtPeriodFrom() {
        return txtPeriodFrom;
    }
    
    public void settxtPeriodFrom(String txtPeriodFrom) {
        this.txtPeriodFrom = txtPeriodFrom;
    }
    
     public String gettxtPeriodFrom1() {
        return txtPeriodFrom1;
    }
    
    public void settxtPeriodFrom1(String txtPeriodFrom1) {
        this.txtPeriodFrom1 = txtPeriodFrom1;
    }
    
    
    /**
     * Getter for property txaPurposeofVisit.
     * @return Value of property txaPurposeofVisit.
     */
    public String gettxtPeriodTo() {
        return txtPeriodTo;
    }
  
    public void settxtPeriodTo(String txtPeriodTo) {
        this.txtPeriodTo = txtPeriodTo;
    }
    
       public String gettxtPeriodTo1() {
        return txtPeriodTo1;
    }
  
    public void settxtPeriodTo1(String txtPeriodTo1) {
        this.txtPeriodTo1 = txtPeriodTo1;
    }
    
    /**
     * Getter for property txaCommentsLeft.
     * @return Value of property txaCommentsLeft.
     */
    public String gettxtPriority() {
        return txtPriority;
    }
    
    
    public void settxtPriority(String txtPriority) {
        this.txtPriority = txtPriority;
    }
    
       public String gettxtPriority1() {
        return txtPriority1;
    }
    
    
    public void settxtPriority1(String txtPriority1) {
        this.txtPriority1 = txtPriority1;
    }
    
    
   
    public java.lang.String getcboPeriodType() {
        return cboPeriodType;
    }    
    

   public void setcboPeriodType(java.lang.String cboPeriodType) {
        this.cboPeriodType = cboPeriodType;
    }
   
      public java.lang.String getcboPeriodType1() {
        return cboPeriodType1;
    }    
    

   public void setcboPeriodType1(java.lang.String cboPeriodType1) {
        this.cboPeriodType1 = cboPeriodType1;
    }
      public java.lang.String getcboPeriodType2() {
        return cboPeriodType2;
    }    
    

   public void setcboPeriodType2(java.lang.String cboPeriodType2) {
        this.cboPeriodType2 = cboPeriodType2;
    }
   
     public String gettxtpriority() {
        return txtpriority;
    }
    
     public void settxtpriority(String txtpriority) {
        this.txtpriority = txtpriority;
    }
    
    
   public String gettxtpriority1() {
        return txtpriority1;
    }
    
  
    public void settxtpriority1(String txtpriority1) {
        this.txtpriority1 = txtpriority1;
    }
    
   
    public String getTxtid() {
        return txtid;
    }
    
     public void setTxtid(String txtid) {
        this.txtid = txtid;
    }
      
    public String getTxtID() {
        return txtID;
    }
    
    
    public void setTxtID(String txtID) {
        this.txtID = txtID;
    }
    
    
       public String getTxtID1() {
        return txtID1;
    }
    
       public void setTxtID1(String txtID1) {
        this.txtID1 = txtID1;
    }
    
   
    /**
     * Getter for property pan.
     * @return Value of property pan.
     */
    public int getPan() {
        return pan;
    }
    
    /**
     * Setter for property pan.
     * @param pan New value of property pan.
     */
    public void setPan(int pan) {
        this.pan = pan;
    }
    
    /**
     * Getter for property txtid1.
     * @return Value of property txtid1.
     */
    public String getTxtid1() {
        return txtid1;
    }
    
    /**
     * Setter for property txtid1.
     * @param txtid1 New value of property txtid1.
     */
    public void setTxtid1(String txtid1) {
        this.txtid1 = txtid1;
    }
    
    public java.lang.String getcboPeriodType3() {
        return cboPeriodType3;
    }    
    

   public void setcboPeriodType3(java.lang.String cboPeriodType3) {
        this.cboPeriodType3 = cboPeriodType3;
    }
   
    
       public java.lang.String getcboPeriodType4() {
        return cboPeriodType4;
    }    
    

   public void setcboPeriodType4(java.lang.String cboPeriodType4) {
        this.cboPeriodType4 = cboPeriodType4;
    }
   
    
       public String gettxtPriority2() {
        return txtPriority2;
    }
    
    
    public void settxtPriority2(String txtPriority2) {
        this.txtPriority2 = txtPriority2;
        
    }
    
         public String gettxtdesc() {
        return txtdesc;
    }
    
    
    public void settxtdesc(String txtdesc) {
        this.txtdesc = txtdesc;
        
    }
         public String gettxtPeriodFrom2() {
        return txtPeriodFrom2;
    }
    
    
    public void settxtPeriodFrom2(String txtPeriodFrom2) {
        this.txtPeriodFrom2 = txtPeriodFrom2;
        
    }
    
           public String gettxtPeriodTo2() {
        return txtPeriodTo2;
    }
    
    
    public void settxtPeriodTo2(String txtPeriodTo2) {
        this.txtPeriodTo2 = txtPeriodTo2;
        
    }
     
    
           public String gettxtid2() {
        return txtid2;
    }
    
    
    public void settxtid2(String txtid2) {
        this.txtid2 = txtid2;
        
    }
    
    /**
     * Getter for property txtid5.
     * @return Value of property txtid5.
     */
    public String getTxtid5() {
        return txtid5;
    }
    
    /**
     * Setter for property txtid5.
     * @param txtid5 New value of property txtid5.
     */
    public void setTxtid5(String txtid5) {
        this.txtid5 = txtid5;
    }
    
     public String getTxtid6() {
        return txtid6;
    }
    
    /**
     * Setter for property txtid5.
     * @param txtid5 New value of property txtid5.
     */
    public void setTxtid6(String txtid6) {
        this.txtid6 = txtid6;
    }
    
    
    
     public String gettxtDoubtfrom() {
        return txtDoubtfrom;
    }   
     
      public void settxtDoubtfrom(String txtDoubtfrom) {
        this.txtDoubtfrom = txtDoubtfrom;
    }
      
       public String gettxtDoubtto() {
        return txtDoubtto;
    }   
     
      public void settxtDoubtto(String txtdoubtto) {
        this.txtDoubtto = txtdoubtto;
    }
      
        public String gettxtBadfrom() {
        return txtBadfrom;
    }   
     
      public void settxtBadfrom(String txtbadfrom) {
        this.txtBadfrom = txtbadfrom;
    }
      
        public String gettxtBadto() {
        return txtBadto;
    }   
     
      public void settxtBadto(String txtbadto) {
        this.txtBadto = txtbadto;
    }
      
          public String gettxtDocdoubtfrom() {
        return txtDocdoubtfrom;
    }   
     
      public void settxtDocdoubtfrom(String txtdocdoubtfrom) {
        this.txtDocdoubtfrom = txtdocdoubtfrom;
    }
      
            public String gettxtDocdoubtto() {
        return txtDocdoubtto;
    }   
     
      public void settxtDocdoubtto(String txtdocdoubtto) {
        this.txtDocdoubtto = txtdocdoubtto;
    }
            
      public String gettxtDocbadfrom() {
        return txtDocbadfrom;
    }   
     
      public void settxtDocbadfrom(String txtdocbadfrom) {
        this.txtDocbadfrom = txtdocbadfrom;
    }
      
           public String gettxtDocbadto() {
        return txtDocbadto;
    }   
     
      public void settxtDocbadto(String txtdocbadto) {
        this.txtDocbadto = txtdocbadto;
    }
           
      public String gettxtDoubtnarra() {
        return txtDoubtnarra;
    }   
     
      public void settxtDoubtnarra(String txtdoubtnarra) {
        this.txtDoubtnarra = txtdoubtnarra;
    }
      
    public String gettxtBadnarra() {
        return txtBadnarra;
    }   
     
      public void settxtBadnarra(String txtbadnarra) {
        this.txtBadnarra = txtbadnarra;
    }  
      
      public String gettxtDocdoubtnara() {
        return txtDocdoubtnara;
    }   
     
      public void settxtDocdoubtnara(String txtdocdoubtnara) {
        this.txtDocdoubtnara = txtdocdoubtnara;
    }  
          public String gettxtDocbadnara() {
        return txtDocbadnara;
    }   
     
      public void settxtDocbadnara(String txtdocbadnara) {
        this.txtDocbadnara = txtdocbadnara;
    }  
    
        
         public java.lang.String getcboFluidType() {
        return cboFluidType;
    }    
       public void setcboFluidType(java.lang.String cboFluidType) {
        this.cboFluidType = cboFluidType;
    }
       
           public java.lang.String gettxtModule() {
        return txtModule;
    }    
       public void settxtModule(java.lang.String txtModule) {
        this.txtModule = txtModule;
    }
           public java.lang.String getcboDescription() {
        return cboDescription;
    }    
       public void setcboDescription(java.lang.String cboDescription) {
        this.cboDescription = cboDescription;
    }
       
        public String gettxtPercentage() {
        return txtPercentage;
    }   
     
      public void settxtPercentage(String txtPercentage) {
        this.txtPercentage = txtPercentage;
    }  
      
        public ComboBoxModel getCbmid() {
        return cbmid;
    }
    
    
    public void setCbmid(ComboBoxModel cbmid) {
        this.cbmid = cbmid;
    }
    
    /**
     * Getter for property cbmDesc.
     * @return Value of property cbmDesc.
     */
    public ComboBoxModel getCbmDesc() {
        return cbmDesc;
    }    
       
    /**
     * Setter for property cbmDesc.
     * @param cbmDesc New value of property cbmDesc.
     */
    public void setCbmDesc(ComboBoxModel cbmDesc) {
        this.cbmDesc = cbmDesc;
    }
    
   }
    
  
    
   