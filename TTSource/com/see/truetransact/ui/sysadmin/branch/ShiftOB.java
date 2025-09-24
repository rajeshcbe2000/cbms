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

package com.see.truetransact.ui.sysadmin.branch;

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
import com.see.truetransact.transferobject.sysadmin.branch.ShiftTO;
//import com.see.truetransact.ui.outwardregister.OutwardRB;

/**
 *
 * @author  
 *
 */
public class ShiftOB extends CObservable {

    
    private String cboShift="";
    private ComboBoxModel cbmShift;
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(ShiftOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookUpHash;
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private ShiftOB ShiftOB;
//    ShiftRB ShiftRB = new ShiftRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private ShiftTO objShiftTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public ShiftOB() 
    {
        try 
        {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ShiftJNDI");
            map.put(CommonConstants.HOME, "Branch.ShiftHome");
            map.put(CommonConstants.REMOTE, "Branch.Shift");
            initUIComboBoxModel();
            fillDropdown();
                       
        }
        catch (Exception e) 
        {
            parseException.logException(e,true);
        }
    }
    
      public void execute(String command) 
      {
        try 
        {
            HashMap term = new HashMap();
            term.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            term.put("SHIFT", getShiftTO().getShift());
            System.out.println("BRANCH_ID=============="+TrueTransactMain.BRANCH_ID);
            //HashMap ss=getShiftTO();
            System.out.println("SHIFT=============="+getShiftTO().getShift());
            
            ClientUtil.execute("updateBranchMasterShift" , term);
            
            //System.out.println("aaaaaaaaaaaaaaaaaaaa"+aa.get(0));
            HashMap proxyResultMap = proxy.execute(term, map);
           // setResult(getActionType());
           // delBillsCharge = null;
        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
        public ShiftTO getShiftTO()
        {
        ShiftTO objShiftTO = new ShiftTO();
        objShiftTO.setShift(CommonUtil.convertObjToStr((String)cbmShift.getSelectedItem()));
        return objShiftTO;
        }
        
        private void setShiftTO(ShiftTO objShiftTO) throws Exception
        {
        setCboShift((String) getCbmShift().getDataForKey(CommonUtil.convertObjToStr(objShiftTO.getShift())));
             objShiftTO=null;
        }
    
    private void initUIComboBoxModel()
        {
        cbmShift = new ComboBoxModel();
        }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception
    {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
              
               log.info("Inside FillDropDown");
               lookUpHash = new HashMap();
               lookUpHash.put(CommonConstants.MAP_NAME, "getShift");

//               final ArrayList lookup_keys = new ArrayList();
//               lookup_keys.add("BRANCH_SHIFT");
               lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
               
               HashMap lookupValues = ClientUtil.populateLookupData(lookUpHash);
        
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        cbmShift=new ComboBoxModel(key,value);
    }

     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
      private HashMap populateDataLocal(HashMap obj)  throws Exception
      {
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
      }
 
    
     private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
   
  
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
   
  
    private String getCommand()
    {
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
    
    private String getAction()
    {
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
    
   

    
     public void populateData(HashMap whereMap) 
     {
        HashMap mapData=null;
        try 
        {
             System.out.println("whereMap=="+whereMap);
             System.out.println("map=="+map);
             mapData = proxy.executeQuery(whereMap, map);
             System.out.println("objmapData=="+((List) mapData.get("ShiftTO")).get(0));
             ShiftTO objTO =(ShiftTO) ((List) mapData.get("ShiftTO")).get(0);
             System.out.println("objTOobjTOobjTOobjTO=="+objTO);
//             setShiftTO(objTO);
        } 
        catch( Exception e ) 
        {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//           // parseException.logException(e,true);
//               System.out.println("Error in populateData():"+e);
        }
    }
    
     
    
    public void resetForm()
    {
       setCboShift("");
       //setChanged();
       ttNotifyObservers();
    }
    
    /**
     * Getter for property cboShift.
     * @return Value of property cboShift.
     */
    public String getCboShift() {
        return cboShift;
    }
    
    /**
     * Setter for property cboShift.
     * @param cboShift New value of property cboShift.
     */
    public void setCboShift(String cboShift) {
        this.cboShift = cboShift;
    }
    
    /**
     * Getter for property cbmShift.
     * @return Value of property cbmShift.
     */
    public ComboBoxModel getCbmShift() {
        return cbmShift;
    }
    
    /**
     * Setter for property cbmShift.
     * @param cbmShift New value of property cbmShift.
     */
    public void setCbmShift(ComboBoxModel cbmShift) {
        this.cbmShift = cbmShift;
    }
    
   
    
    }
    
  
    
   