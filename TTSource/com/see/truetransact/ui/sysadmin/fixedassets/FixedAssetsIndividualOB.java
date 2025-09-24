
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

package com.see.truetransact.ui.sysadmin.fixedassets;

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
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetsIndividualTO;
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
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB ; //trans details
/**
 *
 * @author
 *
 */
public class FixedAssetsIndividualOB extends CObservable {
    
    private LinkedHashMap transactionDetailsTO = null; //trans details
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
    private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private TransactionOB transactionOB; //trans details
    private HashMap authMap = new HashMap(); //trans details
    private Date currDt=null; //trans details
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs"; //trans details
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs"; //trans details
    private double txtAmount; //trans details
    
    private String txtStatus = "";
    private String assetIndID = "";
    private String txtInvoiceNo = "";
    private String quantity = "";
    private String warranty = "";
    private String faceVal = "";
    private String floor ="";
    private String txtSlNo="";
    private String branchId = "";
    private String currVal = "";
    private String orderedDt = "";
    private String purchasedDt = "";
    private String assetNum = "";
    private String txtStatusBy = "";
    private String CreatedDt="";
    private ComboBoxModel cbmassetType;
    private String cboassetType="";
    private ComboBoxModel cbmassetDesc;
    private String cboAssetDesc="";
    private String company="";
    private ComboBoxModel cbmwarrVal;
    private String cbowarVal="";
    private String cboDepart="";
    private ComboBoxModel cbmDepart;
    //private ComboBoxModel cbmwarVal;
    private String cboBranchId="";
    private ComboBoxModel cbmBranchIdVal;
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String noOfTrainees = "";
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblEmpDetails;
    
    private boolean newData = false;
    private LinkedHashMap incParMap;
    private LinkedHashMap deletedTableMap;
    private String subj="";
    
    private final static Logger log = Logger.getLogger(FixedAssetsIndividualOB.class);
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
    private FixedAssetsIndividualOB fidOB;
    private static final CommonRB objCommonRB = new CommonRB();
    private FixedAssetsIndividualTO objFixedAssetsIndividualTO;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public FixedAssetsIndividualOB() {
        try {
            currDt=ClientUtil.getCurrentDate(); //trans details
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FixedAssetsIndividualJNDI");
            map.put(CommonConstants.HOME, "FixedAssetsIndividualHome");
            map.put(CommonConstants.REMOTE, "FixedAssetsIndividual");
            setTableTile();
            tblEmpDetails = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("SL No");
        tableTitle.add("Asset_No");
        tableTitle.add("Branch");
        tableTitle.add("Depart");
        tableTitle.add("Floor");
        tableTitle.add("Curr_Val");
        tableTitle.add("Face_Val");
        tableTitle.add("Auth_S");
        IncVal = new ArrayList();
    }
    
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        log.info("Inside FillDropDown");
        
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("FIXED_ASSETS");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("FIXED_ASSETS"));
        cbmwarrVal = new ComboBoxModel(key,value);
        
        HashMap param1 = new HashMap();
        param1.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKey1 = new ArrayList();
        lookupKey1.add("FIXED_ASSETS_DEPART");
        param1.put(CommonConstants.PARAMFORQUERY, lookupKey1);
        HashMap lookupValues1 = populateData(param1);
        fillData((HashMap)lookupValues1.get("FIXED_ASSETS_DEPART"));
        cbmDepart = new ComboBoxModel(key,value);
        

        param.put(CommonConstants.MAP_NAME,"getFixedAssetsProd");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmassetType = new ComboBoxModel(key,value);
        cbmassetDesc = new ComboBoxModel();
        setBranchid();
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    public HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    public void setBranchid() {
        List lst=(List)ClientUtil.executeQuery("getBranchId",null);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmBranchIdVal = new ComboBoxModel(key,value);
        }
    }
    
    public void setAssetDesc(HashMap intTangibleMap){
        List lst = (List)ClientUtil.executeQuery("getFixedAssetDescValue", intTangibleMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmassetDesc = new ComboBoxModel(key,value);
        }
    }
    
    private void getMap(List list){
        key = new ArrayList();
        value = new ArrayList();
        //The first values in the ArrayList key and value are empty String to display the first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
    }
    
    public void setCbmassetDesc(String assetType) {
        if (CommonUtil.convertObjToStr(assetType).length()>1) {
            try {
                HashMap lookUpHash = new HashMap();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmassetDesc = new ComboBoxModel(key,value);
        this.cbmassetDesc = cbmassetDesc;
        setChanged();
    }
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            //  HashMap term = new HashMap();//trans details
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        /*
            //trans details
            if (transactionDetailsTO == null)
                transactionDetailsTO = new LinkedHashMap();
            if (deletedTransactionDetailsTO != null) {
                System.out.println("deletedTransactionDetailsTOhgsadg>>>>>..."+deletedTransactionDetailsTO);
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
             System.out.println("allowedTransactionDetailsTOhgsadg>>>>>..."+allowedTransactionDetailsTO);
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            System.out.println("transactionDetailsTOhgsadg>>>>>..."+transactionDetailsTO);
            term.put("TransactionTO",transactionDetailsTO);
         
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
            System.out.println("proxyy111>>>>>>>==="+proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
            //             System.out.println(setProxyReturnMap(proxyReturnMap));
            //end..
         */
            
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final FixedAssetsIndividualTO objFixedAssetsIndividualTO = new FixedAssetsIndividualTO();
        //     HashMap term = new HashMap();//trans details
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("FixedAssetsIndividual",setFixedAssetsIndividualData());
            data.put("FixedAssetsIndividualTableDetails",incParMap);
            data.put("deletedFixedAssetsIndividualTableDetails",deletedTableMap);
        }else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            data.put("FixedAssetsIndividualTableDetails",incParMap);
        }
        System.out.println("data in FixedAssetsIndividual OB : " + data);
        //trans details
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if (deletedTransactionDetailsTO != null) {
            System.out.println("deletedTransactionDetailsTOhgsadg>>>>>..."+deletedTransactionDetailsTO);
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        System.out.println("allowedTransactionDetailsTOhgsadg>>>>>..."+allowedTransactionDetailsTO);
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        System.out.println("transactionDetailsTOhgsadg>>>>>..."+transactionDetailsTO);
        data.put("TransactionTO",transactionDetailsTO);
        
        if(getAuthMap() != null && getAuthMap().size() > 0 ){
            if( getAuthMap() != null){
                data.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
            }
            if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                if (transactionDetailsTO == null){
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                data.put("TransactionTO",transactionDetailsTO);
                allowedTransactionDetailsTO = null;
            }
            authMap = null;
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
   //     _authorizeMap = new HashMap();
        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT")){
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("FIXED_INDIVIDUAL_ID")));
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
        HashMap mapData=null;//trans details
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            mapData = proxy.executeQuery(whereMap, map);//trans details
            objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) ((List) data.get("FixedIndividualTO")).get(0);
            populateFixedAssetsIndividualData(objFixedAssetsIndividualTO);
            //trans details
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            //end..
            if(data.containsKey("FixedIndividualDetailsTO")){
                incParMap = (LinkedHashMap)data.get("FixedIndividualDetailsTO");
                ArrayList addList =new ArrayList(incParMap.keySet());
                for(int i=0;i<addList.size();i++){
                    objFixedAssetsIndividualTO = (FixedAssetsIndividualTO)  incParMap.get(addList.get(i));
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getSlNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetNum()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getBranchId()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCboDepart()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFloor()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCurrVal()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFaceVal()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetTabAuthorizedStatus()));
                    tblEmpDetails.addRow(incTabRow);
                }
            }
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To populate data into the screen */
    public FixedAssetsIndividualTO setFixedAssetsIndividualData() {
        
        final FixedAssetsIndividualTO objFixedAssetsIndividualTO = new FixedAssetsIndividualTO();
        try{
            objFixedAssetsIndividualTO.setAssetType(CommonUtil.convertObjToStr(getCbmassetType().getKeyForSelected()));
            if (!CommonUtil.convertObjToStr(getCbmassetDesc()).equals(""))
                objFixedAssetsIndividualTO.setAssetDesc(CommonUtil.convertObjToStr(getCbmassetDesc().getKeyForSelected()));
            objFixedAssetsIndividualTO.setInvoiceNo(getTxtInvoiceNo());
            objFixedAssetsIndividualTO.setBranCode(TrueTransactMain.BRANCH_ID);
            objFixedAssetsIndividualTO.setOrderedDt(DateUtil.getDateMMDDYYYY(getOrderedDt()));
            objFixedAssetsIndividualTO.setPurchasedDt(DateUtil.getDateMMDDYYYY(getPurchasedDt()));
            objFixedAssetsIndividualTO.setCommand(getCommand());
            objFixedAssetsIndividualTO.setStatus(getAction());
            objFixedAssetsIndividualTO.setStatusBy(TrueTransactMain.USER_ID);
            objFixedAssetsIndividualTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objFixedAssetsIndividualTO.setQuantity(getQuantity());
            objFixedAssetsIndividualTO.setWarranty(getWarranty());
            objFixedAssetsIndividualTO.setWarrantyVal(CommonUtil.convertObjToStr(getCbmwarrVal().getKeyForSelected()));
            objFixedAssetsIndividualTO.setCompany(getCompany());
            objFixedAssetsIndividualTO.setAmount(CommonUtil.convertObjToStr(getTxtAmount()));
            if(getCommand().equalsIgnoreCase("INSERT")){
                objFixedAssetsIndividualTO.setCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetsIndividualTO.setCreatedDt(currDt);
            }
            if(getCommand().equalsIgnoreCase("UPDATE")||getCommand().equalsIgnoreCase("DELETE")){
                objFixedAssetsIndividualTO.setAssetIndID(getAssetIndID());
            }
        }catch(Exception e){
            log.info("Error In setEmpTranferData()");
            e.printStackTrace();
        }
        return objFixedAssetsIndividualTO;
    }
    
    private void populateFixedAssetsIndividualData(FixedAssetsIndividualTO objFixedAssetsIndividualTO) throws Exception{
        getCbmassetType().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetType()));
        setCboassetType((String) getCbmassetType().getDataForKey(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetType())));
        if (!(objFixedAssetsIndividualTO.getAssetDesc()).equals("")) {
            setCbmassetDesc(objFixedAssetsIndividualTO.getAssetType());
            getCbmassetDesc().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetDesc())); // This line added by Rajesh
            setCboassetDesc((String) getCbmassetDesc().getDataForKey(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetDesc())));
        }
        // getCbmBranchIdVal().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getBranchId()));
        getCbmwarrVal().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getWarrantyVal()));
        this.setAssetIndID(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetIndID()));
        this.setQuantity(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getQuantity()));
        this.setWarranty(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getWarranty()));
        this.setOrderedDt(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getOrderedDt()));
        this.setPurchasedDt(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getPurchasedDt()));
        this.setStatusBy(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getStatusBy()));
        this.setCompany(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCompany()));
        this.setTxtInvoiceNo(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getInvoiceNo()));
        setChanged();
        notifyObservers();
    }
    
    public void resetForm(){
        setCboassetType("");
        setCboBranchId("");
        setCboDepart("");
        setCboassetDesc("");
        setQuantity("");
        setOrderedDt("");
        setPurchasedDt("");
        setAssetIndID("");
        setWarranty("");
        setFaceVal("");
        setTxtSlNo("");
        setFloor("");
        setCurrVal("");
        setAssetNum("");
        setCompany("");
        setCbowarVal("");
        resetTableValues();
        setTxtInvoiceNo("");
        setAuthorizeStatus("");
        incParMap = null;
        setChanged();
        ttNotifyObservers();
    }
    public void resetEmpDetails() {
        setAssetNum("");
        setCboBranchId("");
        setCboDepart("");
        setFaceVal("");
        setTxtSlNo("");
        setFloor("");
        setCurrVal("");
        setChanged();
        ttNotifyObservers();
    }
    
    public void deleteTableData(String val, int row){
        if(deletedTableMap == null){
            deletedTableMap = new LinkedHashMap();
        }
        FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) incParMap.get(val);
        objFixedAssetsIndividualTO.setAssetTabStatus(CommonConstants.STATUS_DELETED);
        deletedTableMap.put(CommonUtil.convertObjToStr(tblEmpDetails.getValueAt(row,0)),incParMap.get(val));
        Object obj;
        obj=val;
        incParMap.remove(val);
        resetTableValues();
        try{
            populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(incParMap.keySet());
        ArrayList addList =new ArrayList(incParMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) incParMap.get(addList.get(i));
            IncVal.add(objFixedAssetsIndividualTO);
            if(!objFixedAssetsIndividualTO.getAssetTabStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetNum()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getBranchId()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCboDepart()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFloor()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCurrVal()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFaceVal()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetTabAuthorizedStatus()));
                tblEmpDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    //    public int serialNo(ArrayList data){
    //        final int dataSize = data.size();
    //        int nums[]= new int[150];
    //        int max=nums[0];
    //        int slno=0;
    //        int a=0;
    //        slno=dataSize+1;
    //        for(int i=0;i<data.size();i++){
    //            a=CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(i,0));
    //            nums[i]=a;
    //            if(nums[i]>max)
    //                max=nums[i];
    //            slno=max+1;
    //        }
    //        return slno;
    //    }
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final FixedAssetsIndividualTO objFixedAssetsIndividualTO = new FixedAssetsIndividualTO();
            if( incParMap == null ){
                incParMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objFixedAssetsIndividualTO.setAssetTabCreatedBy(TrueTransactMain.USER_ID);
                    objFixedAssetsIndividualTO.setAssetTabStatusDt(currDt);
                    objFixedAssetsIndividualTO.setAssetTabStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetsIndividualTO.setAssetTabCreatedDt(currDt);
                    objFixedAssetsIndividualTO.setAssetTabStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objFixedAssetsIndividualTO.setAssetTabStatusDt(currDt);
                    objFixedAssetsIndividualTO.setAssetTabStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetsIndividualTO.setAssetTabStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objFixedAssetsIndividualTO.setAssetTabCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetsIndividualTO.setAssetTabStatusDt(currDt);
                objFixedAssetsIndividualTO.setAssetTabStatusBy(TrueTransactMain.USER_ID);
                objFixedAssetsIndividualTO.setAssetTabCreatedDt(currDt);
                objFixedAssetsIndividualTO.setAssetTabStatus(CommonConstants.STATUS_CREATED);
            }
            //            int  slno=0;
            //            int nums[]= new int[150];
            //            int max=nums[0];
            //            if(!updateMode){
            //                ArrayList data = tblEmpDetails.getDataArrayList();
            //                slno=serialNo(data);
            //            }
            //            else{
            //                if(isNewData()){
            //                    ArrayList data = tblEmpDetails.getDataArrayList();
            //                    slno=serialNo(data);
            //                }
            //                else{
            //                    int b=CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(rowSelected,0));
            //                    slno=b;
            //                }
            //            }
            //objFixedAssetsIndividualTO.setSlNo(String.valueOf(slno));
            objFixedAssetsIndividualTO.setSlNo(getTxtSlNo());
            objFixedAssetsIndividualTO.setAssetNum(getAssetNum());
            objFixedAssetsIndividualTO.setBranchId(getCboBranchId());
            objFixedAssetsIndividualTO.setCboDepart(getCboDepart());
            objFixedAssetsIndividualTO.setFloor(getFloor());
            objFixedAssetsIndividualTO.setCurrVal(CommonUtil.convertObjToDouble(getCurrVal()));
            objFixedAssetsIndividualTO.setFaceVal(CommonUtil.convertObjToDouble(getFaceVal()));
            objFixedAssetsIndividualTO.setAssetTabAuthorizedStatus(getAuthorizeStatus());
            objFixedAssetsIndividualTO.setBranchId(CommonUtil.convertObjToStr(getCbmBranchIdVal().getKeyForSelected()));
            // objFixedAssetsIndividualTO.setWarrantyVal(CommonUtil.convertObjToStr(getCbmwarrVal().getKeyForSelected()));
            incParMap.put(objFixedAssetsIndividualTO.getSlNo(),objFixedAssetsIndividualTO);
            //String sno=String.valueOf(slno);
            updateFixedAssetIndividualDetails(rowSel,objFixedAssetsIndividualTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void updateFixedAssetIndividualDetails(int rowSel,  FixedAssetsIndividualTO objFixedAssetsIndividualTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblEmpDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblEmpDetails.getDataArrayList().get(j)).get(0);
            if(getTxtSlNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblEmpDetails.getDataArrayList();
                data.remove(rowSel);
                //   IncParRow.add(sno);
                IncParRow.add(getTxtSlNo());
                IncParRow.add(getAssetNum());
                IncParRow.add(CommonUtil.convertObjToStr(getCbmBranchIdVal().getKeyForSelected()));
                IncParRow.add(CommonUtil.convertObjToStr(getCbmDepart().getKeyForSelected()));
                IncParRow.add(getFloor());
                //IncParRow.add(getWarranty()+(CommonUtil.convertObjToStr(getCbmwarrVal().getKeyForSelected())));
                IncParRow.add(getCurrVal());
                IncParRow.add(getFaceVal());
                IncParRow.add(getAuthorizeStatus());
                tblEmpDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            //   IncParRow.add(sno);
            IncParRow.add(getTxtSlNo());
            IncParRow.add(getAssetNum());
            IncParRow.add(getCboBranchId());
            IncParRow.add(getCboDepart());
            IncParRow.add(getFloor());
            //IncParRow.add(getWarranty()+(CommonUtil.convertObjToStr(getCbmwarrVal().getKeyForSelected())));
            IncParRow.add(getCurrVal());
            IncParRow.add(getFaceVal());
            IncParRow.add(getAuthorizeStatus());
            tblEmpDetails.insertRow(tblEmpDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateLeaveDetails(String row){
        try{
            resetEmpDetails();
            final FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO)incParMap.get(row);
            populateTableData(objFixedAssetsIndividualTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(FixedAssetsIndividualTO objFixedAssetsIndividualTO)  throws Exception{
        //setWarranty(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getWarranty()));
        //  setTxtSlNo(objFixedAssetsIndividualTO.getSlNo());
        setTxtSlNo(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getSlNo()));
        setAssetNum(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetNum()));
        setCboBranchId(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getBranchId()));
        setCboDepart(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCboDepart()));
        setFloor(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFloor()));
        setCurrVal(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getCurrVal()));
        setFaceVal(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getFaceVal()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getAssetTabAuthorizedStatus()));
        // getCbmwarrVal().setKeyForSelected(CommonUtil.convertObjToStr(objFixedAssetsIndividualTO.getWarrantyVal()));
        setChanged();
        notifyObservers();
    }
    
    public void resetTableValues(){
        tblEmpDetails.setDataArrayList(null,tableTitle);
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
    
    
    
    
    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }
    
    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }
    
    /**
     * Getter for property txtSlNo.
     * @return Value of property txtSlNo.
     */
    public java.lang.String getTxtSlNo() {
        return txtSlNo;
    }
    
    /**
     * Setter for property txtSlNo.
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(java.lang.String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }
    
    
    /**
     * Getter for property tblEmpDetails.
     * @return Value of property tblEmpDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblEmpDetails() {
        return tblEmpDetails;
    }
    
    /**
     * Setter for property tblEmpDetails.
     * @param tblEmpDetails New value of property tblEmpDetails.
     */
    public void setTblEmpDetails(com.see.truetransact.clientutil.EnhancedTableModel tblEmpDetails) {
        this.tblEmpDetails = tblEmpDetails;
    }
    
    /**
     * Getter for property subj.
     * @return Value of property subj.
     */
    public java.lang.String getSubj() {
        return subj;
    }
    
    /**
     * Setter for property subj.
     * @param subj New value of property subj.
     */
    public void setSubj(java.lang.String subj) {
        this.subj = subj;
    }
    
    /**
     * Getter for property assetIndID.
     * @return Value of property assetIndID.
     */
    public java.lang.String getAssetIndID() {
        return assetIndID;
    }
    
    /**
     * Setter for property assetIndID.
     * @param assetIndID New value of property assetIndID.
     */
    public void setAssetIndID(java.lang.String assetIndID) {
        this.assetIndID = assetIndID;
    }
    
    /**
     * Getter for property quantity.
     * @return Value of property quantity.
     */
    public java.lang.String getQuantity() {
        return quantity;
    }
    
    /**
     * Setter for property quantity.
     * @param quantity New value of property quantity.
     */
    public void setQuantity(java.lang.String quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Getter for property warranty.
     * @return Value of property warranty.
     */
    public java.lang.String getWarranty() {
        return warranty;
    }
    
    /**
     * Setter for property warranty.
     * @param warranty New value of property warranty.
     */
    public void setWarranty(java.lang.String warranty) {
        this.warranty = warranty;
    }
    
    /**
     * Getter for property faceVal.
     * @return Value of property faceVal.
     */
    public java.lang.String getFaceVal() {
        return faceVal;
    }
    
    /**
     * Setter for property faceVal.
     * @param faceVal New value of property faceVal.
     */
    public void setFaceVal(java.lang.String faceVal) {
        this.faceVal = faceVal;
    }
    
    /**
     * Getter for property currVal.
     * @return Value of property currVal.
     */
    public java.lang.String getCurrVal() {
        return currVal;
    }
    
    /**
     * Setter for property currVal.
     * @param currVal New value of property currVal.
     */
    public void setCurrVal(java.lang.String currVal) {
        this.currVal = currVal;
    }
    
    /**
     * Getter for property orderedDt.
     * @return Value of property orderedDt.
     */
    public java.lang.String getOrderedDt() {
        return orderedDt;
    }
    
    /**
     * Setter for property orderedDt.
     * @param orderedDt New value of property orderedDt.
     */
    public void setOrderedDt(java.lang.String orderedDt) {
        this.orderedDt = orderedDt;
    }
    
    /**
     * Getter for property purchasedDt.
     * @return Value of property purchasedDt.
     */
    public java.lang.String getPurchasedDt() {
        return purchasedDt;
    }
    
    /**
     * Setter for property purchasedDt.
     * @param purchasedDt New value of property purchasedDt.
     */
    public void setPurchasedDt(java.lang.String purchasedDt) {
        this.purchasedDt = purchasedDt;
    }
    
    /**
     * Getter for property assetNum.
     * @return Value of property assetNum.
     */
    public java.lang.String getAssetNum() {
        return assetNum;
    }
    
    /**
     * Setter for property assetNum.
     * @param assetNum New value of property assetNum.
     */
    public void setAssetNum(java.lang.String assetNum) {
        this.assetNum = assetNum;
    }
    
    /**
     * Getter for property cbmassetType.
     * @return Value of property cbmassetType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmassetType() {
        return cbmassetType;
    }
    
    /**
     * Setter for property cbmassetType.
     * @param cbmassetType New value of property cbmassetType.
     */
    public void setCbmassetType(com.see.truetransact.clientutil.ComboBoxModel cbmassetType) {
        this.cbmassetType = cbmassetType;
    }
    
    /**
     * Getter for property cboassetType.
     * @return Value of property cboassetType.
     */
    public java.lang.String getCboassetType() {
        return cboassetType;
    }
    
    /**
     * Setter for property cboassetType.
     * @param cboassetType New value of property cboassetType.
     */
    public void setCboassetType(java.lang.String cboassetType) {
        this.cboassetType = cboassetType;
    }
    
    /**
     * Getter for property cbmassetDesc.
     * @return Value of property cbmassetDesc.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmassetDesc() {
        return cbmassetDesc;
    }
    
    /**
     * Setter for property cbmassetDesc.
     * @param cbmassetDesc New value of property cbmassetDesc.
     */
    public void setCbmassetDesc(com.see.truetransact.clientutil.ComboBoxModel cbmassetDesc) {
        this.cbmassetDesc = cbmassetDesc;
    }
    
    /**
     * Getter for property cboassetDesc.
     * @return Value of property cboassetDesc.
     */
    public java.lang.String getCboassetDesc() {
        return cboAssetDesc;
    }
    
    /**
     * Setter for property cboassetDesc.
     * @param cboassetDesc New value of property cboassetDesc.
     */
    public void setCboassetDesc(java.lang.String cboassetDesc) {
        this.cboAssetDesc = cboAssetDesc;
    }
    
    /**
     * Getter for property company.
     * @return Value of property company.
     */
    public java.lang.String getCompany() {
        return company;
    }
    
    /**
     * Setter for property company.
     * @param company New value of property company.
     */
    public void setCompany(java.lang.String company) {
        this.company = company;
    }
    
    /**
     * Getter for property cbmwarrVal.
     * @return Value of property cbmwarrVal.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmwarrVal() {
        return cbmwarrVal;
    }
    
    /**
     * Setter for property cbmwarrVal.
     * @param cbmwarrVal New value of property cbmwarrVal.
     */
    public void setCbmwarrVal(com.see.truetransact.clientutil.ComboBoxModel cbmwarrVal) {
        this.cbmwarrVal = cbmwarrVal;
    }
    
    /**
     * Getter for property cbowarVal.
     * @return Value of property cbowarVal.
     */
    public java.lang.String getCbowarVal() {
        return cbowarVal;
    }
    
    /**
     * Setter for property cbowarVal.
     * @param cbowarVal New value of property cbowarVal.
     */
    public void setCbowarVal(java.lang.String cbowarVal) {
        this.cbowarVal = cbowarVal;
    }
    
    
    public void setCbmBranchIdVal(com.see.truetransact.clientutil.ComboBoxModel cbmBranchIdVal) {
        this.cbmBranchIdVal = cbmBranchIdVal;
    }
    
    /**
     * Getter for property cboBranchId.
     * @return Value of property cboBranchId.
     */
    public java.lang.String getCboBranchId() {
        return cboBranchId;
    }
    
    /**
     * Setter for property cboBranchId.
     * @param cboBranchId New value of property cboBranchId.
     */
    public void setCboBranchId(java.lang.String cboBranchId) {
        this.cboBranchId = cboBranchId;
    }
    
    /**
     * Getter for property cbmBranchIdVal.
     * @return Value of property cbmBranchIdVal.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchIdVal() {
        return cbmBranchIdVal;
    }
    
    /**
     * Getter for property txtInvoiceNo.
     * @return Value of property txtInvoiceNo.
     */
    public java.lang.String getTxtInvoiceNo() {
        return txtInvoiceNo;
    }
    
    /**
     * Setter for property txtInvoiceNo.
     * @param txtInvoiceNo New value of property txtInvoiceNo.
     */
    public void setTxtInvoiceNo(java.lang.String txtInvoiceNo) {
        this.txtInvoiceNo = txtInvoiceNo;
    }
    
    /**
     * Getter for property floor.
     * @return Value of property floor.
     */
    public java.lang.String getFloor() {
        return floor;
    }
    
    /**
     * Setter for property floor.
     * @param floor New value of property floor.
     */
    public void setFloor(java.lang.String floor) {
        this.floor = floor;
    }
    
    /**
     * Getter for property cboDepart.
     * @return Value of property cboDepart.
     */
    public java.lang.String getCboDepart() {
        return cboDepart;
    }
    
    /**
     * Setter for property cboDepart.
     * @param cboDepart New value of property cboDepart.
     */
    public void setCboDepart(java.lang.String cboDepart) {
        this.cboDepart = cboDepart;
    }
    
    /**
     * Getter for property cbmDepart.
     * @return Value of property cbmDepart.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepart() {
        return cbmDepart;
    }
    
    /**
     * Setter for property cbmDepart.
     * @param cbmDepart New value of property cbmDepart.
     */
    public void setCbmDepart(com.see.truetransact.clientutil.ComboBoxModel cbmDepart) {
        this.cbmDepart = cbmDepart;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property currDt.
     * @return Value of property currDt.
     */
    public Date getCurrDt() {
        return currDt;
    }
    
    /**
     * Setter for property currDt.
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(Date currDt) {
        this.currDt = currDt;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property authMap.
     * @return Value of property authMap.
     */
    public HashMap getAuthMap() {
        return authMap;
    }
    
    /**
     * Setter for property authMap.
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }
    
    /**
     * Getter for property txtAmount.
     * @return Value of property txtAmount.
     */
    public double getTxtAmount() {
        return txtAmount;
    }
    
    /**
     * Setter for property txtAmount.
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(double txtAmount) {
        this.txtAmount = txtAmount;
    }
    
}



