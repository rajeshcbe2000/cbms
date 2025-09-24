/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransOB.java
 *
 * Created on Tue Jan 18 16:23:35 IST 2011
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.Map;
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
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetMovementTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetDepreciationTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetBreakageTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetSaleTO;
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


import java.util.Observable;
import java.util.HashMap;

/**
 *
 * @author
 */

public class FixedAssetsTransOB extends CObservable{
    
    private String cboProductType = "";
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmSubType;
    private String cboSubType = "";
    private String tdtDepDate = "";
    private String txtFromAssetId = "";
    private String txtToAssetId = "";
    private String deprBatchId = "";
    private String deprBranchCode = "";
    private String cboProductTypeSale = "";
    private ComboBoxModel cbmProductTypeSale;
    private ComboBoxModel cbmSubTypeSale;
    private String cboSubTypeSale = "";
    private String txtAssetIdSale = "";
    private String txtFaceValueSale = "";
    private String txtCurrentValueSale = "";
    private String txtPurchasedDate = "";
    private String txtAssetIdMove = "";
    private String txtSlNo = "";
    private String txtFloor = "";
    private String txtFaceVal = "";
    private String txtCurrValue = "";
    private String txtDepart = "";
    private String txtBranchId = "";
    private String cboBranchIdMove = "";
    private ComboBoxModel cbmBranchIdMove;
    private String cboDepartMove = "";
    private ComboBoxModel cbmDepartMove;
    private String txtFloorMove = "";
    private String txtAssetIdBreak = "";
    private String txtSlNoBreak = "";
    private String txtFloorBreak = "";
    private String txtFaceValBreak = "";
    private String txtCurrValueBreak = "";
    private String txtBreakageRegion = "";
    private String txtDepartBreak = "";
    private String txtBranchIdBreak = "";
    private String lblDepBatchId = "";
    private String lblTotNewCurValue = "";
    private String lblTotDepValue = "";
    private String lblTotalCurrentValue = "";
    private String txtAssetId = "";
    private String saleBatchId="";
    private String moveBatchId="";
    private String brkBatchId="";
    
    private LinkedHashMap transactionDetailsTO = null; //trans details
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
    private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private TransactionOB transactionOB; //trans details
    private HashMap authMap = new HashMap(); //trans details
    private Date currDt=null; //trans details
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs"; //trans details
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs"; //trans details
    
    private String txtStatus = "";
    private String txtStatusBy = "";
    
    private HashMap _authorizeMap;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String noOfTrainees = "";
    final ArrayList tableTitle = new ArrayList();
    final ArrayList tableTitleBreakList = new ArrayList();
    final ArrayList tableTitleDepreList = new ArrayList();
    final ArrayList tableTitleSaleList = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblEmpDetails,tblDepreciationList,tblSaleList,tblBreakageList;
    
    private boolean newData = false;
    private ArrayList depreciationList;
    private ArrayList breakageList;
    private ArrayList movementList;
    private LinkedHashMap depreciationMap;
    private LinkedHashMap saleMap;
    private LinkedHashMap deleteSaleTableMap;
    private LinkedHashMap deleteDepreciationTableMap;
    private LinkedHashMap movementMap;
    private LinkedHashMap deleteMoveTableMap;
    private LinkedHashMap breakageMap;
    private LinkedHashMap deleteBreakTableMap;
    private String subj="";
    
    final int DEPRECIATION=0,SALE=1,MOVEMENT=2,BREAKAGE=3;
    int pan=-1;
    int panEditDelete=-1;
    
    HashMap data = new HashMap();
    
    
    private final static Logger log = Logger.getLogger(FixedAssetsTransOB.class);
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
    private FixedAssetsTransOB fidOB;
    private static final CommonRB objCommonRB = new CommonRB();
    private FixedAssetMovementTO objFixedAssetMovementTO;
    private FixedAssetDepreciationTO objFixedAssetDepreciationTO;
    private FixedAssetBreakageTO objFixedAssetBreakageTO;
    private FixedAssetSaleTO objFixedAssetSaleTO;
    private Rounding rd = null;
    int totCurrent=0;
    // Date currDt = null;
    
    /** Creates a new instance of TDS MiantenanceOB */
    public FixedAssetsTransOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FixedAssetsTransJNDI");
            map.put(CommonConstants.HOME, "FixedAssetsTransHome");
            map.put(CommonConstants.REMOTE, "FixedAssetsTrans");
            setDepreciationTableTile();
            setSaleTableTile();
            setMovementTableTile();
            setBreakageTableTile();
            tblEmpDetails = new EnhancedTableModel(null, tableTitle);
            tblBreakageList = new EnhancedTableModel(null, tableTitleBreakList);
            tblDepreciationList = new EnhancedTableModel(null, tableTitleDepreList);
            tblSaleList = new EnhancedTableModel(null, tableTitleSaleList);
            fillDropdown();
            rd = new Rounding();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setDepreciationTableTile() throws Exception{
        tableTitleDepreList.add("Asset ID");
        tableTitleDepreList.add("Depreciation Value");
        tableTitleDepreList.add("Current Value");
        //tableTitleDepreList.add("Depreciation %");
        // tableTitleDepreList.add("Depreciation Value");
        // tableTitleDepreList.add("New Current Value");
        IncVal = new ArrayList();
    }
    
    private void setSaleTableTile() throws Exception{
        tableTitleSaleList.add("Asset ID");
        tableTitleSaleList.add("Face Value");
        tableTitleSaleList.add("Current Value");
        IncVal = new ArrayList();
    }
    
    private void setMovementTableTile() throws Exception{
        tableTitle.add("Asset ID");
        tableTitle.add("Branch Code");
        tableTitle.add("Department");
        tableTitle.add("Floor");
        IncVal = new ArrayList();
    }
    
    private void setBreakageTableTile() throws Exception{
        tableTitleBreakList.add("Asset ID");
        tableTitleBreakList.add("Breakage Region");
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
        final ArrayList lookupKey1 = new ArrayList();
        lookupKey1.add("FIXED_ASSETS_DEPART");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey1);
        HashMap lookupValues = populateComboData(param);
        fillData((HashMap)lookupValues.get("FIXED_ASSETS_DEPART"));
        cbmDepartMove = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,"getFixedAssetsProd");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductType = new ComboBoxModel(key,value);
        cbmSubType = new ComboBoxModel();
        
        param.put(CommonConstants.MAP_NAME,"getFixedAssetsProd");
        where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductTypeSale = new ComboBoxModel(key,value);
        cbmSubTypeSale = new ComboBoxModel();
        setBranchid();
    }
    
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    public HashMap populateComboData(HashMap obj) throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    
    public void populateData(HashMap whereMap,int pan)  throws Exception{
        //keyValue = proxy.executeQuery(obj,lookupMap);
        //  log.info("Got HashMap");
        //   return keyValue;
        
        HashMap mapData=null;
        try{
            System.out.println("whereMap113431=="+whereMap);
            if(pan == DEPRECIATION){
                System.out.println("whereMap111=="+whereMap);
                System.out.println("map111=="+map);
                mapData = proxy.executeQuery(whereMap, map);
                System.out.println("objmapdepreciationData=="+((List) mapData.get("FixedAssetsDepreciationTO")).get(0));
                FixedAssetDepreciationTO objTO =(FixedAssetDepreciationTO) ((List) mapData.get("FixedAssetsDepreciationTO")).get(0);
                System.out.println("objTOobjTOobjTOobjTO111=="+objTO);
                setDepreciationTO(objTO);
            }else if(pan == MOVEMENT){
                System.out.println("whereMap222=="+whereMap);
                System.out.println("map222=="+map);
                mapData = proxy.executeQuery(whereMap, map);
                System.out.println("objmapmovemntData=="+((List) mapData.get("FixedAssetsMovementTO")).get(0));
                FixedAssetMovementTO objTO =(FixedAssetMovementTO) ((List) mapData.get("FixedAssetsMovementTO")).get(0);
                System.out.println("objTOobjTOobjTOobjTO222=="+objTO);
                setMovementTO(objTO);
            }else if(pan == BREAKAGE){
                System.out.println("whereMap333=="+whereMap);
                System.out.println("map333=="+map);
                mapData = proxy.executeQuery(whereMap, map);
                System.out.println("objmapbreakageData=="+((List) mapData.get("FixedAssetsBreakageTO")).get(0));
                FixedAssetBreakageTO objTO =(FixedAssetBreakageTO) ((List) mapData.get("FixedAssetsBreakageTO")).get(0);
                System.out.println("objTOobjTOobjTOobjTO333=="+objTO);
                setBreakageTO(objTO);
            }
            
            
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():"+e);
            
        }
    }
    
    private void setDepreciationTO(FixedAssetDepreciationTO objTO){
        setTdtDepDate(CommonUtil.convertObjToStr(objTO.getDepreciationDate()));
        setChanged();
        ttNotifyObservers();
    }
    
    private void setMovementTO(FixedAssetMovementTO objTO){
        System.out.println("movmntdestnatnbranch.....>>>>"+objTO.getDestinationBranchId());
        setCboBranchIdMove(objTO.getDestinationBranchId());
        setCboDepartMove(objTO.getDestinationDepartment());
        setTxtAssetIdMove(objTO.getFaMoveId());
        setTxtDepart(objTO.getSourceDepartment());
        setTxtBranchId(objTO.getSourceBranchId());
        setTxtFloor(objTO.getSourceFloor());
        setTxtFloorMove(objTO.getDestinationFloor());
        System.out.println("movmnt1111.....>>>>");
        setChanged();
        ttNotifyObservers();
    }
    private void setBreakageTO(FixedAssetBreakageTO objTO){
        setTxtAssetIdBreak(objTO.getFaBreakageId());
        setTxtBreakageRegion(objTO.getBreakageRegion());
        setChanged();
        ttNotifyObservers();
    }
    
    public void setBranchid() {
        List lst=(List)ClientUtil.executeQuery("getBranchId",null);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmBranchIdMove = new ComboBoxModel(key,value);
        }
    }
    
    public void setAssetSubTypeSale(HashMap intTangibleMap){
        List lst = (List)ClientUtil.executeQuery("getFixedAssetDescValue", intTangibleMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmSubTypeSale = new ComboBoxModel(key,value);
        }
    }
    
    public void setAssetSubType(HashMap intTangibleMap){
        List lst = (List)ClientUtil.executeQuery("getFixedAssetDescValue", intTangibleMap);
        if(lst!=null && lst.size()>0){
            getMap(lst);
            cbmSubType = new ComboBoxModel(key,value);
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
        //      cbmassetDesc = new ComboBoxModel(key,value);
        //       this.cbmassetDesc = cbmassetDesc;
        setChanged();
    }
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public HashMap doAction(int pan){
        HashMap proxyResultMap = new HashMap();
        try{
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData(pan);
                        break;
                        //                    case ClientConstants.ACTIONTYPE_EDIT:
                        //                        updateData();
                        //                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData(pan);
                        break;
                    default:
                        // throw new ActionNotFoundException();
                }
                
                if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                    data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                }
                
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                if(pan == SALE){
                    //trans details
                    if (transactionDetailsTO == null)
                        transactionDetailsTO = new LinkedHashMap();
                    if (deletedTransactionDetailsTO != null) {
                        transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                        deletedTransactionDetailsTO = null;
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                    data.put("TransactionTO",transactionDetailsTO);
                    data.put("PAN","SALE");
                    
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
                }
                proxyResultMap = proxy.execute(data, map);
           /*     if(pan==DEPRECIATION && objFixedAssetDepreciationTO.getDeprBatchId()!=null){
                    proxyResultMap.put("DEPR_ID",objFixedAssetDepreciationTO.getDeprBatchId());
                }
                if(pan==SALE && objFixedAssetSaleTO.getSaleBatchId()!=null){
                    proxyResultMap.put("SALE_ID",objFixedAssetSaleTO.getSaleBatchId());
                }
                if(pan==MOVEMENT && objFixedAssetMovementTO.getMoveBatchId()!=null){
                    proxyResultMap.put("MOVE_ID",objFixedAssetMovementTO.getMoveBatchId());
                }
                if(pan==BREAKAGE && objFixedAssetBreakageTO.getBreakBatchId()!=null){
                    proxyResultMap.put("BREAK_ID",objFixedAssetBreakageTO.getBreakBatchId());
                }
            */
                setProxyReturnMap(proxyResultMap);
                System.out.println("returnnn>>>jjagdsjd"+proxyResultMap+"gtfyujygyukh"+getProxyReturnMap());
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    
    private void deleteData(int pan){
        if(pan == SALE){
            System.out.println("saleebatchid>>>>>>>adss4322346566768"+getSaleBatchId());
            data.put("SALE_BATCH_ID", getSaleBatchId());
        }
        if(pan == DEPRECIATION){
            data.put("DEPR_BATCH_ID", getDeprBatchId());
        }
        if(pan == MOVEMENT){
            data.put("MOVE_BATCH_ID", getMoveBatchId());
        }
        if(pan == BREAKAGE){
            System.out.println("brkkksfhjdfhjf>>>>>brk"+getBrkBatchId());
            data.put("BREAK_BATCH_ID", getBrkBatchId());//BREAK_BATCH_ID
        }
    }
    private void insertData(int pan) throws Exception{
        if(pan == DEPRECIATION){
            setDepreciationData();
        }
        else if(pan == SALE){
            setSaleData();
        }
        else if (pan == BREAKAGE){
            setBreakageData();
        }else if (pan == MOVEMENT){
            setMovementData();
        }
    }
    
    private void setDepreciationData(){
        ArrayList tableList = tblDepreciationList.getDataArrayList();
        ArrayList rowList = null;
        FixedAssetDepreciationTO objFixedAssetDepreciationTO = null;
        depreciationList = new ArrayList();
        for (int i=0; i<tableList.size(); i++) {
            rowList = (ArrayList) tableList.get(i);
            objFixedAssetDepreciationTO = new FixedAssetDepreciationTO();
            objFixedAssetDepreciationTO.setFaId(CommonUtil.convertObjToStr(rowList.get(0)));
            objFixedAssetDepreciationTO.setDepreciationValue(CommonUtil.convertObjToStr(rowList.get(4)));
            objFixedAssetDepreciationTO.setNewCurrentValue(CommonUtil.convertObjToStr(rowList.get(5)));
            objFixedAssetDepreciationTO.setDepreciationDate(currDt);
            objFixedAssetDepreciationTO.setStatusBy(ProxyParameters.USER_ID);
            objFixedAssetDepreciationTO.setBranchCode(ProxyParameters.BRANCH_ID);
            depreciationList.add(objFixedAssetDepreciationTO);
        }
        if(depreciationList!=null && depreciationList.size()>0 )
            data.put("DEPRECIATION_LIST",depreciationList);
    }
    private void setSaleData(){
        ArrayList tableList = tblSaleList.getDataArrayList();
        ArrayList rowList = null;
        FixedAssetSaleTO objFixedAssetSaleTO = null;
        ArrayList saleList = new ArrayList();
        for(int i=0;i<tableList.size();i++){
            rowList = (ArrayList) tableList.get(i);
            objFixedAssetSaleTO = new FixedAssetSaleTO();
            objFixedAssetSaleTO.setAssetIdSale(CommonUtil.convertObjToStr(rowList.get(0)));
            System.out.println("khutftywqdsfsf324234534"+getLblTotalCurrentValue());
            objFixedAssetSaleTO.setSaleAmount(getLblTotalCurrentValue());
            System.out.println("grtjhthj123131"+getTxtAssetId());
            objFixedAssetSaleTO.setAssetId(getTxtAssetId());
            objFixedAssetSaleTO.setSaleDate(currDt);
            objFixedAssetSaleTO.setStatusBy(ProxyParameters.USER_ID);
            objFixedAssetSaleTO.setSaleBatchId(ProxyParameters.BRANCH_ID);
            System.out.println("command>>>rdwerewr"+getCommand());
            objFixedAssetSaleTO.setCommand(getCommand());
            saleList.add(objFixedAssetSaleTO);
        }
        if(saleList!=null && saleList.size()>0)
            data.put("SALE_LIST", saleList);
    }
    
    private void setBreakageData(){
        ArrayList tableList = tblBreakageList.getDataArrayList();
        ArrayList rowList = null;
        FixedAssetBreakageTO objFixedAssetBreakageTO = null;
        breakageList = new ArrayList();
        for (int i=0; i<tableList.size(); i++) {
            rowList = (ArrayList) tableList.get(i);
            objFixedAssetBreakageTO = new FixedAssetBreakageTO();
            objFixedAssetBreakageTO.setFaBreakageId(CommonUtil.convertObjToStr(rowList.get(0)));
            objFixedAssetBreakageTO.setBreakageRegion(CommonUtil.convertObjToStr(rowList.get(1)));
            objFixedAssetBreakageTO.setStatus(getAction());
            objFixedAssetBreakageTO.setStatusDt(currDt);
            objFixedAssetBreakageTO.setStatusBy(ProxyParameters.USER_ID);
            breakageList.add(objFixedAssetBreakageTO);
        }
        if(breakageList!=null && breakageList.size()>0 )
            data.put("BREAKAGE_LIST",breakageList);
    }
    
    private void setMovementData(){
        ArrayList tableList = tblEmpDetails.getDataArrayList();
        ArrayList rowList = null;
        FixedAssetMovementTO objFixedAssetMovementTO = null;
        movementList= new ArrayList();
        for (int i=0; i<tableList.size(); i++) {
            rowList = (ArrayList) tableList.get(i);
            objFixedAssetMovementTO= new FixedAssetMovementTO();
            objFixedAssetMovementTO.setFaMoveId(CommonUtil.convertObjToStr(rowList.get(0)));
            objFixedAssetMovementTO.setDestinationBranchId(CommonUtil.convertObjToStr(rowList.get(1)));
            objFixedAssetMovementTO.setDestinationDepartment(CommonUtil.convertObjToStr(rowList.get(2)));
            objFixedAssetMovementTO.setDestinationFloor(CommonUtil.convertObjToStr(rowList.get(3)));
            objFixedAssetMovementTO.setSourceBranchId(getTxtBranchId());
            objFixedAssetMovementTO.setSourceDepartment(getTxtDepart());
            objFixedAssetMovementTO.setSourceFloor(getTxtFloor());
            
            objFixedAssetMovementTO.setStatus(getAction());
            objFixedAssetMovementTO.setStatusDt(currDt);
            objFixedAssetMovementTO.setStatusBy(ProxyParameters.USER_ID);
            movementList.add(objFixedAssetMovementTO);
        }
        if(movementList!=null && movementList.size()>0 )
            data.put("MOVEMENT_LIST",movementList);
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
            if(data.containsKey("FixedAssetsDepreciationTO")){
                depreciationMap = (LinkedHashMap)data.get("FixedAssetsDepreciationTO");
                ArrayList addList =new ArrayList(depreciationMap.keySet());
                for(int i=0;i<addList.size();i++){
                    FixedAssetDepreciationTO  objFixedAssetDepreciationTO = (FixedAssetDepreciationTO)  depreciationMap.get(addList.get(i));
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetDepreciationTO.getFaId()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetDepreciationTO.getDepreciationValue()));
                    incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetDepreciationTO.getNewCurrentValue()));
                    tblEmpDetails.addRow(incTabRow);
                }
            }
            //trans details
            HashMap mapData=null;
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            //end..
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void populateDepreciationData(String grade,int panEditDelete){
        HashMap whereMap = new HashMap();
        LinkedHashMap dataMap = null;
        List rowList = new ArrayList();
        List depreciationList = new ArrayList();
        List saleList = new ArrayList();
        List moveList = new ArrayList();
        List brkList = new ArrayList();
        String mapNameDepr = "";
        String mapNameSale = "";
        String mapNameMove = "";
        String mapNameBrk = "";
        String mapNameED = "";
        String mapNameGA = "";
                whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        whereMap.put("DEPR_DATE", currDt);
                whereMap.put("DEPR_BATCH_ID",grade);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            mapNameDepr = "getSelectFixedAssetDepreciationList";
            mapNameSale= "getSelectFixedAssetSaleDetailsList1";
            mapNameMove= "getSelectFixedAssetMoveList";
            mapNameBrk= "getSelectFixedAssetBreakList1";
            //            mapNameED = "getSelectPromotionEditTO";
            if(panEditDelete==DEPRECIATION){
                List list = ClientUtil.executeQuery(mapNameDepr,whereMap);
                    for (int i = 0;i<list.size();i++){
                        dataMap = (LinkedHashMap) list.get(i);
                        rowList = new ArrayList();
                        rowList.add(dataMap.get("FA_ID"));
                        rowList.add(dataMap.get("FACE_VALUE"));
                        rowList.add(dataMap.get("CURR_VALUE"));
                            rowList.add(dataMap.get("DEPRECIATION_RATE"));
                        rowList.add(dataMap.get("DEPRECIATION_VALUE"));
                        rowList.add(dataMap.get("NEW_CURRENT_VALUE"));
                        depreciationList.add(rowList);
                    }
                    tblDepreciationList = new EnhancedTableModel((ArrayList)depreciationList, tableTitleDepreList);
                    setLblTotNewCurValue(calculateTotalNewCurAmount(depreciationList));
                    setLblTotDepValue(calculateTotalDeprAmount(depreciationList));
                }
            else if(panEditDelete==SALE){
                
                List list = ClientUtil.executeQuery(mapNameSale,whereMap);
                for (int i = 0;i<list.size();i++){
                    dataMap = (LinkedHashMap) list.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("FA_ID"));
                    rowList.add(dataMap.get("FACE_VALUE"));
                    rowList.add(dataMap.get("CURR_VALUE"));
                    //  rowList.add(dataMap.get("PURCHASED_DT"));
                    //  rowList.add(dataMap.get("DEPRECIATION_VALUE"));
                    //  rowList.add(dataMap.get("NEW_CURRENT_VALUE"));
                    saleList.add(rowList);
                }
                tblSaleList = new EnhancedTableModel((ArrayList)saleList, tableTitleSaleList);
                
            }
            else if(panEditDelete==MOVEMENT){
                
                List list = ClientUtil.executeQuery(mapNameMove,whereMap);
                    for (int i = 0;i<list.size();i++){
                        dataMap = (LinkedHashMap)list.get(i);
                        rowList = new ArrayList();
                        rowList.add(dataMap.get("FA_ID"));
                    rowList.add(dataMap.get("SUPPLIED_BRANCH_ID"));
                    rowList.add(dataMap.get("DEPARTMENT"));
                    rowList.add(dataMap.get("FLOOR"));
                    //  rowList.add(dataMap.get("DEPRECIATION_VALUE"));
                    //  rowList.add(dataMap.get("NEW_CURRENT_VALUE"));
                        moveList.add(rowList);
                    }
                tblEmpDetails = new EnhancedTableModel((ArrayList)moveList, tableTitle);
            }
            
            else if(panEditDelete==BREAKAGE){
                
                List list = ClientUtil.executeQuery(mapNameBrk,whereMap);
                    for (int i = 0;i<list.size();i++){
                        dataMap = (LinkedHashMap)list.get(i);
                rowList = new ArrayList();
                rowList.add(dataMap.get("FA_ID"));
                rowList.add(dataMap.get("BREAKAGE_REASON"));
                    // rowList.add(dataMap.get("SUPPLIED_BRANCH_ID"));
                    //  rowList.add(dataMap.get("DEPARTMENT"));
                    //  rowList.add(dataMap.get("FLOOR"));
                    //  rowList.add(dataMap.get("DEPRECIATION_VALUE"));
                    //  rowList.add(dataMap.get("NEW_CURRENT_VALUE"));
                    brkList.add(rowList);
                }
                tblBreakageList = new EnhancedTableModel((ArrayList)brkList, tableTitleBreakList);
            }
            whereMap = null;
        }
    }
    public void resetForm(){
        movementList = null;
        breakageList = null;
        breakageList = null;
        breakageMap = null;
        saleMap = null;
        movementMap = null;
        resetTableValues();
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetBreakageDetails() {
        setTxtAssetIdBreak("");
        setTxtBreakageRegion("");
        
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetDepreciationDetails() {
        
        
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetSaleDetails() {
        
        
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetMovementDetails() {
        //movementList = null;
        
        setChanged();
        ttNotifyObservers();
    }
    
    
    public void addSaleMapTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final FixedAssetSaleTO objFixedAssetSaleTO = new FixedAssetSaleTO();
            if( saleMap == null ){
                saleMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objFixedAssetSaleTO.setCreatedBy(TrueTransactMain.USER_ID);
                    objFixedAssetSaleTO.setStatusDt(currDt);
                    objFixedAssetSaleTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetSaleTO.setCreatedDt(currDt);
                    objFixedAssetSaleTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objFixedAssetSaleTO.setStatusDt(currDt);
                    objFixedAssetSaleTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetSaleTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objFixedAssetSaleTO.setCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetSaleTO.setStatusDt(currDt);
                objFixedAssetSaleTO.setStatusBy(TrueTransactMain.USER_ID);
                objFixedAssetSaleTO.setCreatedDt(currDt);
                objFixedAssetSaleTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objFixedAssetSaleTO.setAssetIdSale(getTxtAssetIdSale());
            objFixedAssetSaleTO.setFaceValueSale(getTxtFaceValueSale());
            objFixedAssetSaleTO.setCurrentValueSale(getTxtCurrentValueSale());
            saleMap.put(getTxtAssetIdSale(),objFixedAssetSaleTO);
            
            
            updateFixedAssetSaleDetails(rowSel,objFixedAssetSaleTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void addMoveMapTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final FixedAssetMovementTO objFixedAssetMovementTO = new FixedAssetMovementTO();
            if( movementMap == null ){
                movementMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objFixedAssetMovementTO.setCreatedBy(TrueTransactMain.USER_ID);
                    objFixedAssetMovementTO.setStatusDt(currDt);
                    objFixedAssetMovementTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetMovementTO.setCreatedDt(currDt);
                    objFixedAssetMovementTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objFixedAssetMovementTO.setStatusDt(currDt);
                    objFixedAssetMovementTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetMovementTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objFixedAssetMovementTO.setCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetMovementTO.setStatusDt(currDt);
                objFixedAssetMovementTO.setStatusBy(TrueTransactMain.USER_ID);
                objFixedAssetMovementTO.setCreatedDt(currDt);
                objFixedAssetMovementTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objFixedAssetMovementTO.setFaMoveId(getTxtAssetIdMove());
            objFixedAssetMovementTO.setDestinationBranchId(getCboBranchIdMove());
            objFixedAssetMovementTO.setDestinationDepartment(getCboDepartMove());
            objFixedAssetMovementTO.setDestinationFloor(getTxtFloorMove());
            movementMap.put(getTxtAssetIdMove(),objFixedAssetMovementTO);
            updateFixedAssetMovementDetails(rowSel,objFixedAssetMovementTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void addBreakageMapTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final FixedAssetBreakageTO objFixedAssetBreakageTO = new FixedAssetBreakageTO();
            if( breakageMap == null ){
                breakageMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objFixedAssetBreakageTO.setCreatedBy(TrueTransactMain.USER_ID);
                    objFixedAssetBreakageTO.setStatusDt(currDt);
                    objFixedAssetBreakageTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetBreakageTO.setCreatedDt(currDt);
                    objFixedAssetBreakageTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objFixedAssetBreakageTO.setStatusDt(currDt);
                    objFixedAssetBreakageTO.setStatusBy(TrueTransactMain.USER_ID);
                    objFixedAssetBreakageTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objFixedAssetBreakageTO.setCreatedBy(TrueTransactMain.USER_ID);
                objFixedAssetBreakageTO.setStatusDt(currDt);
                objFixedAssetBreakageTO.setStatusBy(TrueTransactMain.USER_ID);
                objFixedAssetBreakageTO.setCreatedDt(currDt);
                objFixedAssetBreakageTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objFixedAssetBreakageTO.setFaBreakageId(getTxtAssetIdBreak());
            objFixedAssetBreakageTO.setBreakageRegion(getTxtBreakageRegion());
            //                 breakageMap.put(String.valueOf(tblBreakageList.getRowCount()),objFixedAssetBreakageTO);
            breakageMap.put(getTxtAssetIdBreak(),objFixedAssetBreakageTO);
            updateFixedAssetBreakageDetails(rowSel,objFixedAssetBreakageTO);
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    private void updateFixedAssetSaleDetails(int rowSel,  FixedAssetSaleTO objFixedAssetSaleTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        totCurrent=0;
        //If row already exists update it, else create a new row & append
        for(int i = tblSaleList.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblSaleList.getDataArrayList().get(j)).get(0);
            if(getTxtAssetIdSale().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblSaleList.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtAssetIdSale());
                IncParRow.add(getTxtFaceValueSale());
                IncParRow.add(getTxtCurrentValueSale());
                tblSaleList.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        
        if(!rowExists){
            
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtAssetIdSale());
            IncParRow.add(getTxtFaceValueSale());
            IncParRow.add(getTxtCurrentValueSale());
            IncParRow.set(2, ClientUtil.convertObjToCurrency(IncParRow.get(2)));
            tblSaleList.insertRow(tblSaleList.getRowCount(),IncParRow);
            IncParRow = null;
        }
        
        totCurrent += CommonUtil.convertObjToInt( getTxtCurrentValueSale());
        setLblTotalCurrentValue(String.valueOf(totCurrent));
        
    }
    
    private void updateFixedAssetMovementDetails(int rowSel,  FixedAssetMovementTO objFixedAssetMovementTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblEmpDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblEmpDetails.getDataArrayList().get(j)).get(0);
            if(getTxtAssetIdMove().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblEmpDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtAssetIdMove());
                IncParRow.add(getCboBranchIdMove());
                IncParRow.add(getCboDepartMove());
                IncParRow.add(getTxtFloorMove());
                tblEmpDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        
        if(!rowExists){
            if(movementList==null){
                movementList= new ArrayList();
            }
            movementList.add(objFixedAssetMovementTO);
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtAssetIdMove());
            IncParRow.add(getCboBranchIdMove());
            IncParRow.add(getCboDepartMove());
            IncParRow.add(getTxtFloorMove());
            tblEmpDetails.insertRow(tblEmpDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateMovement(int rowNum) {
        FixedAssetMovementTO obj = (FixedAssetMovementTO)movementList.get(rowNum);
        HashMap moveMap = new HashMap();
        System.out.println("obj.getAssetId()"+obj.getFaMoveId());
        moveMap.put("FA_ID",obj.getFaMoveId());
        List lst = ClientUtil.executeQuery("getSelectBreakageDetails", moveMap);
        if(lst!=null && lst.size()>0){
            moveMap = (HashMap)lst.get(0);
            setTxtSlNo(CommonUtil.convertObjToStr(moveMap.get("SL_NO")));
            setTxtBranchId(CommonUtil.convertObjToStr(moveMap.get("SUPPLIED_BRANCH_ID")));
            setTxtDepart(CommonUtil.convertObjToStr(moveMap.get("DEPARTMENT")));
            setTxtFloor(CommonUtil.convertObjToStr(moveMap.get("FLOOR")));
            setTxtFaceVal(CommonUtil.convertObjToStr(moveMap.get("FACE_VALUE")));
            setTxtCurrValue(CommonUtil.convertObjToStr(moveMap.get("CURR_VALUE")));
        }
        setTxtAssetIdMove(obj.getFaMoveId());
        setCboBranchIdMove(obj.getDestinationBranchId());
        setCboDepartMove(obj.getDestinationDepartment());
        setTxtFloorMove(obj.getDestinationFloor());
    }
    
    private void updateFixedAssetBreakageDetails(int rowSel,  FixedAssetBreakageTO objFixedAssetBreakageTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblBreakageList.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblBreakageList.getDataArrayList().get(j)).get(0);
            if(getTxtAssetIdBreak().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblBreakageList.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtAssetIdBreak());
                IncParRow.add(getTxtBreakageRegion());
                tblBreakageList.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        
        if(!rowExists){
            if(breakageList==null){
                breakageList= new ArrayList();
            }
            breakageList.add(objFixedAssetBreakageTO);
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtAssetIdBreak());
            IncParRow.add(getTxtBreakageRegion());
            tblBreakageList.insertRow(tblBreakageList.getRowCount(),IncParRow);
            IncParRow = null;
        }
        
    }
    
    public void deleteBreakageTableData(String val, int row){
        if(deleteBreakTableMap == null){
            deleteBreakTableMap = new LinkedHashMap();
        }
        FixedAssetBreakageTO objFixedAssetBreakageTO = new FixedAssetBreakageTO();
        objFixedAssetBreakageTO = (FixedAssetBreakageTO) breakageMap.get(val);
        objFixedAssetBreakageTO.setStatus(CommonConstants.STATUS_DELETED);
        deleteBreakTableMap.put(CommonUtil.convertObjToStr(tblBreakageList.getValueAt(row,0)),objFixedAssetBreakageTO);
        Object obj;
        obj=val;
        breakageMap.remove(val);
        resetTableValues();
        try{
            populateBreakageTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateBreakageTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(breakageMap.keySet());
        ArrayList addList =new ArrayList(breakageMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            FixedAssetBreakageTO objFixedAssetBreakageTO = (FixedAssetBreakageTO) breakageMap.get(addList.get(i));
            
            IncVal.add(objFixedAssetBreakageTO);
            if(!objFixedAssetBreakageTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetBreakageTO.getFaBreakageId()));
                incTabRow.add(CommonUtil.convertObjToStr(objFixedAssetBreakageTO.getBreakageRegion()));
                incTabRow.add("");
                tblBreakageList.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    
    public void populateBreakage(int rowNum) {
        FixedAssetBreakageTO obj = (FixedAssetBreakageTO)breakageList.get(rowNum);
        HashMap breakMap = new HashMap();
        System.out.println("obj.getAssetId()"+obj.getFaBreakageId());
        breakMap.put("FA_ID",obj.getFaBreakageId());
        List lst = ClientUtil.executeQuery("getSelectBreakageDetails", breakMap);
        if(lst!=null && lst.size()>0){
            breakMap = (HashMap)lst.get(0);
            setTxtSlNoBreak(CommonUtil.convertObjToStr(breakMap.get("SL_NO")));
            setTxtBranchIdBreak(CommonUtil.convertObjToStr(breakMap.get("SUPPLIED_BRANCH_ID")));
            setTxtDepartBreak(CommonUtil.convertObjToStr(breakMap.get("DEPARTMENT")));
            setTxtFloorBreak(CommonUtil.convertObjToStr(breakMap.get("FLOOR")));
            setTxtFaceValBreak(CommonUtil.convertObjToStr(breakMap.get("FACE_VALUE")));
            setTxtCurrValueBreak(CommonUtil.convertObjToStr(breakMap.get("CURR_VALUE")));
        }
        setTxtAssetIdBreak(obj.getFaBreakageId());
        setTxtBreakageRegion(obj.getBreakageRegion());
    }
    
    
    
    public void populateDepreciationTable(Map where){
        
        List lst = (List) ClientUtil.executeQuery("getSelectDepreciationTableDetailsList", (HashMap)where);
        System.out.println("@@@@@@@@@@List:"+lst);
            List rowList = new ArrayList();
            List tableList = new ArrayList();
            LinkedHashMap dataMap = null;
            String depreciationType = "";
            double depreciationPercentage = 0;
            double calcValue = 0;
            double depreciationValue = 0;
            double newCurrentValue = 0;
            String roundOff = "";
        int currValRoundOff = 0;
            int roundingFactor = 0;
            for (int i = 0;i<lst.size();i++){
                dataMap = (LinkedHashMap) lst.get(i);
                rowList = new ArrayList();
                rowList.add(dataMap.get("FA_ID"));
                rowList.add(dataMap.get("FACE_VALUE"));
                rowList.add(dataMap.get("CURR_VALUE"));
            rowList.add(dataMap.get("DEPRECIATION_RATE"));
                depreciationPercentage = CommonUtil.convertObjToDouble(dataMap.get("DEPRECIATION_RATE")).doubleValue();
                depreciationType = CommonUtil.convertObjToStr(dataMap.get("DEPRECIATION"));
                if (depreciationType.equals("Face_Value")) {
                    calcValue = CommonUtil.convertObjToDouble(dataMap.get("FACE_VALUE")).doubleValue();
                } else if (depreciationType.equals("Current_Value")) {
                    calcValue = CommonUtil.convertObjToDouble(dataMap.get("CURR_VALUE")).doubleValue();
                }
                depreciationValue = calcValue * depreciationPercentage / 100.0;
                roundOff = CommonUtil.convertObjToStr(dataMap.get("ROUND_OFF_TYPE"));
                
                if (!roundOff.equals("NO_ROUND_OFF")) {
                    if (roundOff.equals("NEAREST_VALUE")) {
                        roundingFactor = 100;
                    } else if (roundOff.equals("NEAREST_TENS")) {
                        roundingFactor = 10;
                    } else if (roundOff.equals("NEAREST_HUNDREDS")) {
                        roundingFactor = 1;
                    }
                    depreciationValue = rd.getNearest((long)(depreciationValue*roundingFactor), roundingFactor)/roundingFactor;
                }
            
            currValRoundOff = CommonUtil.convertObjToInt(dataMap.get("CURR_VAL_ROUND_OFF"));
                newCurrentValue = calcValue - depreciationValue;
                if (currValRoundOff >= newCurrentValue) {
                    depreciationValue+=newCurrentValue;
                    newCurrentValue=0;
                }
                rowList.add(new Double(depreciationValue));
                rowList.add(new Double(newCurrentValue));
                tableList.add(rowList);
            }
            tblDepreciationList = new EnhancedTableModel((ArrayList)tableList, tableTitleDepreList);
            setLblTotNewCurValue(calculateTotalNewCurAmount(tableList));
            setLblTotDepValue(calculateTotalDeprAmount(tableList));
    }
    
    private String calculateTotalNewCurAmount(List tableData) {
        final int column = 5;
        String returnTotalAmt = "";
        try {
            double totalAmount = 0.0;
            ArrayList rowData = new ArrayList();
            for (int i=0,j=tableData.size();i<j;i++) {
                rowData = (ArrayList) tableData.get(i);
                totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData.get(column)));
                rowData = null;
            }
            returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return returnTotalAmt;
    }
    
    private String calculateTotalDeprAmount(List tableData) {
        final int column = 4;
        String returnTotalAmt = "";
        try {
            double totalAmount = 0.0;
            ArrayList rowData = new ArrayList();
            for (int i=0,j=tableData.size();i<j;i++) {
                rowData = (ArrayList) tableData.get(i);
                totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData.get(column)));
                rowData = null;
            }
            returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return returnTotalAmt;
    }
    
    
    public void populateDeprTablData(String deprId){
        HashMap whereMap = new HashMap();
        LinkedHashMap dataMap = null;
        List rowList = new ArrayList();
        List depreciationList = new ArrayList();
        String mapNameDepr = "";
        // String mapNameED = "";
        // String mapNameGA = "";
        whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        whereMap.put("DEPR_BATCH_ID",deprId);
        
        HashMap deprTableMap = new HashMap();
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            mapNameDepr = "FixedAssetDepreciationOB.getSelectFixedAssetDepreciation";
            //            mapNameED = "getSelectPromotionEditTO";
            List list = ClientUtil.executeQuery(mapNameDepr,whereMap);
            for (int i = 0;i<list.size();i++){
                ArrayList technicalTabRow = new ArrayList();
                deprTableMap = (HashMap)list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(CommonUtil.convertObjToStr(deprTableMap.get("FA_ID")));
                technicalTabRow.add(CommonUtil.convertObjToStr(deprTableMap.get("DEPRECIATION_VALUE")));
                technicalTabRow.add(CommonUtil.convertObjToStr(deprTableMap.get("NEW_CURRENT_VALUE")));
                tblDepreciationList.insertRow(tblDepreciationList.getRowCount(),technicalTabRow);
                System.out.println("kjhkjkjbkqw     sd"+deprTableMap);
                System.out.println("kjhkjkjbkqw     sd row count>>>"+tblDepreciationList.getRowCount());
            }
        }
        whereMap = null;
    }
    
    public void resetTableValues(){
        tblEmpDetails.setDataArrayList(null,tableTitle);
        tblDepreciationList.setDataArrayList(null,tableTitleDepreList);
        tblBreakageList.setDataArrayList(null,tableTitleBreakList);
        tblSaleList.setDataArrayList(null,tableTitleSaleList);
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
    
    // Setter method for cboProductType
    void setCboProductType(String cboProductType){
        this.cboProductType = cboProductType;
        setChanged();
    }
    // Getter method for cboProductType
    String getCboProductType(){
        return this.cboProductType;
    }
    
    // Setter method for cboSubType
    void setCboSubType(String cboSubType){
        this.cboSubType = cboSubType;
        setChanged();
    }
    // Getter method for cboSubType
    String getCboSubType(){
        return this.cboSubType;
    }
    
    // Setter method for tdtDepDate
    void setTdtDepDate(String tdtDepDate){
        this.tdtDepDate = tdtDepDate;
        setChanged();
    }
    // Getter method for tdtDepDate
    String getTdtDepDate(){
        return this.tdtDepDate;
    }
    
    // Setter method for txtFromAssetId
    void setTxtFromAssetId(String txtFromAssetId){
        this.txtFromAssetId = txtFromAssetId;
        setChanged();
    }
    // Getter method for txtFromAssetId
    String getTxtFromAssetId(){
        return this.txtFromAssetId;
    }
    
    // Setter method for txtToAssetId
    void setTxtToAssetId(String txtToAssetId){
        this.txtToAssetId = txtToAssetId;
        setChanged();
    }
    // Getter method for txtToAssetId
    String getTxtToAssetId(){
        return this.txtToAssetId;
    }
    
    // Setter method for cboProductTypeSale
    void setCboProductTypeSale(String cboProductTypeSale){
        this.cboProductTypeSale = cboProductTypeSale;
        setChanged();
    }
    // Getter method for cboProductTypeSale
    String getCboProductTypeSale(){
        return this.cboProductTypeSale;
    }
    
    // Setter method for cboSubTypeSale
    void setCboSubTypeSale(String cboSubTypeSale){
        this.cboSubTypeSale = cboSubTypeSale;
        setChanged();
    }
    // Getter method for cboSubTypeSale
    String getCboSubTypeSale(){
        return this.cboSubTypeSale;
    }
    
    // Setter method for txtAssetIdSale
    void setTxtAssetIdSale(String txtAssetIdSale){
        this.txtAssetIdSale = txtAssetIdSale;
        setChanged();
    }
    // Getter method for txtAssetIdSale
    String getTxtAssetIdSale(){
        return this.txtAssetIdSale;
    }
    
    // Setter method for txtFaceValueSale
    void setTxtFaceValueSale(String txtFaceValueSale){
        this.txtFaceValueSale = txtFaceValueSale;
        setChanged();
    }
    // Getter method for txtFaceValueSale
    String getTxtFaceValueSale(){
        return this.txtFaceValueSale;
    }
    
    // Setter method for txtCurrentValueSale
    void setTxtCurrentValueSale(String txtCurrentValueSale){
        this.txtCurrentValueSale = txtCurrentValueSale;
        setChanged();
    }
    // Getter method for txtCurrentValueSale
    String getTxtCurrentValueSale(){
        return this.txtCurrentValueSale;
    }
    
    // Setter method for txtPurchasedDate
    void setTxtPurchasedDate(String txtPurchasedDate){
        this.txtPurchasedDate = txtPurchasedDate;
        setChanged();
    }
    // Getter method for txtPurchasedDate
    String getTxtPurchasedDate(){
        return this.txtPurchasedDate;
    }
    
    // Setter method for txtAssetIdMove
    void setTxtAssetIdMove(String txtAssetIdMove){
        this.txtAssetIdMove = txtAssetIdMove;
        setChanged();
    }
    // Getter method for txtAssetIdMove
    String getTxtAssetIdMove(){
        return this.txtAssetIdMove;
    }
    
    // Setter method for txtSlNo
    void setTxtSlNo(String txtSlNo){
        this.txtSlNo = txtSlNo;
        setChanged();
    }
    // Getter method for txtSlNo
    String getTxtSlNo(){
        return this.txtSlNo;
    }
    
    // Setter method for txtFloor
    void setTxtFloor(String txtFloor){
        this.txtFloor = txtFloor;
        setChanged();
    }
    // Getter method for txtFloor
    String getTxtFloor(){
        return this.txtFloor;
    }
    
    // Setter method for txtFaceVal
    void setTxtFaceVal(String txtFaceVal){
        this.txtFaceVal = txtFaceVal;
        setChanged();
    }
    // Getter method for txtFaceVal
    String getTxtFaceVal(){
        return this.txtFaceVal;
    }
    
    // Setter method for txtCurrValue
    void setTxtCurrValue(String txtCurrValue){
        this.txtCurrValue = txtCurrValue;
        setChanged();
    }
    // Getter method for txtCurrValue
    String getTxtCurrValue(){
        return this.txtCurrValue;
    }
    
    // Setter method for txtDepart
    void setTxtDepart(String txtDepart){
        this.txtDepart = txtDepart;
        setChanged();
    }
    // Getter method for txtDepart
    String getTxtDepart(){
        return this.txtDepart;
    }
    
    // Setter method for txtBranchId
    void setTxtBranchId(String txtBranchId){
        this.txtBranchId = txtBranchId;
        setChanged();
    }
    // Getter method for txtBranchId
    String getTxtBranchId(){
        return this.txtBranchId;
    }
    
    // Setter method for cboBranchIdMove
    void setCboBranchIdMove(String cboBranchIdMove){
        this.cboBranchIdMove = cboBranchIdMove;
        setChanged();
    }
    // Getter method for cboBranchIdMove
    String getCboBranchIdMove(){
        return this.cboBranchIdMove;
    }
    
    // Setter method for cboDepartMove
    void setCboDepartMove(String cboDepartMove){
        this.cboDepartMove = cboDepartMove;
        setChanged();
    }
    // Getter method for cboDepartMove
    String getCboDepartMove(){
        return this.cboDepartMove;
    }
    
    // Setter method for txtFloorMove
    void setTxtFloorMove(String txtFloorMove){
        this.txtFloorMove = txtFloorMove;
        setChanged();
    }
    // Getter method for txtFloorMove
    String getTxtFloorMove(){
        return this.txtFloorMove;
    }
    
    // Setter method for txtAssetIdBreak
    void setTxtAssetIdBreak(String txtAssetIdBreak){
        this.txtAssetIdBreak = txtAssetIdBreak;
        setChanged();
    }
    // Getter method for txtAssetIdBreak
    String getTxtAssetIdBreak(){
        return this.txtAssetIdBreak;
    }
    
    // Setter method for txtSlNoBreak
    void setTxtSlNoBreak(String txtSlNoBreak){
        this.txtSlNoBreak = txtSlNoBreak;
        setChanged();
    }
    // Getter method for txtSlNoBreak
    String getTxtSlNoBreak(){
        return this.txtSlNoBreak;
    }
    
    // Setter method for txtFloorBreak
    void setTxtFloorBreak(String txtFloorBreak){
        this.txtFloorBreak = txtFloorBreak;
        setChanged();
    }
    // Getter method for txtFloorBreak
    String getTxtFloorBreak(){
        return this.txtFloorBreak;
    }
    
    // Setter method for txtFaceValBreak
    void setTxtFaceValBreak(String txtFaceValBreak){
        this.txtFaceValBreak = txtFaceValBreak;
        setChanged();
    }
    // Getter method for txtFaceValBreak
    String getTxtFaceValBreak(){
        return this.txtFaceValBreak;
    }
    
    // Setter method for txtCurrValueBreak
    void setTxtCurrValueBreak(String txtCurrValueBreak){
        this.txtCurrValueBreak = txtCurrValueBreak;
        setChanged();
    }
    // Getter method for txtCurrValueBreak
    String getTxtCurrValueBreak(){
        return this.txtCurrValueBreak;
    }
    
    // Setter method for txtBreakageRegion
    public void setTxtBreakageRegion(String txtBreakageRegion){
        this.txtBreakageRegion = txtBreakageRegion;
        setChanged();
    }
    // Getter method for txtBreakageRegion
    public String getTxtBreakageRegion(){
        return this.txtBreakageRegion;
    }
    
    // Setter method for txtDepartBreak
    void setTxtDepartBreak(String txtDepartBreak){
        this.txtDepartBreak = txtDepartBreak;
        setChanged();
    }
    // Getter method for txtDepartBreak
    String getTxtDepartBreak(){
        return this.txtDepartBreak;
    }
    
    // Setter method for txtBranchIdBreak
    void setTxtBranchIdBreak(String txtBranchIdBreak){
        this.txtBranchIdBreak = txtBranchIdBreak;
        setChanged();
    }
    // Getter method for txtBranchIdBreak
    String getTxtBranchIdBreak(){
        return this.txtBranchIdBreak;
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
     * Getter for property cbmDepartMove.
     * @return Value of property cbmDepartMove.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepartMove() {
        return cbmDepartMove;
    }
    
    /**
     * Setter for property cbmDepartMove.
     * @param cbmDepartMove New value of property cbmDepartMove.
     */
    public void setCbmDepartMove(com.see.truetransact.clientutil.ComboBoxModel cbmDepartMove) {
        this.cbmDepartMove = cbmDepartMove;
    }
    
    /**
     * Getter for property cbmBranchIdMove.
     * @return Value of property cbmBranchIdMove.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchIdMove() {
        return cbmBranchIdMove;
    }
    
    /**
     * Setter for property cbmBranchIdMove.
     * @param cbmBranchIdMove New value of property cbmBranchIdMove.
     */
    public void setCbmBranchIdMove(com.see.truetransact.clientutil.ComboBoxModel cbmBranchIdMove) {
        this.cbmBranchIdMove = cbmBranchIdMove;
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /**
     * Getter for property cbmSubType.
     * @return Value of property cbmSubType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSubType() {
        return cbmSubType;
    }
    
    /**
     * Setter for property cbmSubType.
     * @param cbmSubType New value of property cbmSubType.
     */
    public void setCbmSubType(com.see.truetransact.clientutil.ComboBoxModel cbmSubType) {
        this.cbmSubType = cbmSubType;
    }
    
    /**
     * Getter for property cbmProductTypeSale.
     * @return Value of property cbmProductTypeSale.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductTypeSale() {
        return cbmProductTypeSale;
    }
    
    /**
     * Setter for property cbmProductTypeSale.
     * @param cbmProductTypeSale New value of property cbmProductTypeSale.
     */
    public void setCbmProductTypeSale(com.see.truetransact.clientutil.ComboBoxModel cbmProductTypeSale) {
        this.cbmProductTypeSale = cbmProductTypeSale;
    }
    
    /**
     * Getter for property cbmSubTypeSale.
     * @return Value of property cbmSubTypeSale.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSubTypeSale() {
        return cbmSubTypeSale;
    }
    
    /**
     * Setter for property cbmSubTypeSale.
     * @param cbmSubTypeSale New value of property cbmSubTypeSale.
     */
    public void setCbmSubTypeSale(com.see.truetransact.clientutil.ComboBoxModel cbmSubTypeSale) {
        this.cbmSubTypeSale = cbmSubTypeSale;
    }
    
    /**
     * Getter for property tblDepreciationList.
     * @return Value of property tblDepreciationList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepreciationList() {
        return tblDepreciationList;
    }
    
    /**
     * Setter for property tblDepreciationList.
     * @param tblDepreciationList New value of property tblDepreciationList.
     */
    public void setTblDepreciationList(com.see.truetransact.clientutil.EnhancedTableModel tblDepreciationList) {
        this.tblDepreciationList = tblDepreciationList;
    }
    
    /**
     * Getter for property lblTotNewCurValue.
     * @return Value of property lblTotNewCurValue.
     */
    public java.lang.String getLblTotNewCurValue() {
        return lblTotNewCurValue;
    }
    
    /**
     * Setter for property lblTotNewCurValue.
     * @param lblTotNewCurValue New value of property lblTotNewCurValue.
     */
    public void setLblTotNewCurValue(java.lang.String lblTotNewCurValue) {
        this.lblTotNewCurValue = lblTotNewCurValue;
    }
    
    /**
     * Getter for property lblTotDepValue.
     * @return Value of property lblTotDepValue.
     */
    public java.lang.String getLblTotDepValue() {
        return lblTotDepValue;
    }
    
    /**
     * Setter for property lblTotDepValue.
     * @param lblTotDepValue New value of property lblTotDepValue.
     */
    public void setLblTotDepValue(java.lang.String lblTotDepValue) {
        this.lblTotDepValue = lblTotDepValue;
    }
    
    /**
     * Getter for property tblSaleList.
     * @return Value of property tblSaleList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSaleList() {
        return tblSaleList;
    }
    
    /**
     * Setter for property tblSaleList.
     * @param tblSaleList New value of property tblSaleList.
     */
    public void setTblSaleList(com.see.truetransact.clientutil.EnhancedTableModel tblSaleList) {
        this.tblSaleList = tblSaleList;
    }
    
    /**
     * Getter for property tblBreakageList.
     * @return Value of property tblBreakageList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblBreakageList() {
        return tblBreakageList;
    }
    
    /**
     * Setter for property tblBreakageList.
     * @param tblBreakageList New value of property tblBreakageList.
     */
    public void setTblBreakageList(com.see.truetransact.clientutil.EnhancedTableModel tblBreakageList) {
        this.tblBreakageList = tblBreakageList;
    }
    
    /**
     * Getter for property lblTotalCurrentValue.
     * @return Value of property lblTotalCurrentValue.
     */
    public java.lang.String getLblTotalCurrentValue() {
        return lblTotalCurrentValue;
    }
    
    /**
     * Setter for property lblTotalCurrentValue.
     * @param lblTotalCurrentValue New value of property lblTotalCurrentValue.
     */
    public void setLblTotalCurrentValue(java.lang.String lblTotalCurrentValue) {
        this.lblTotalCurrentValue = lblTotalCurrentValue;
    }
    
    /**
     * Getter for property deprBatchId.
     * @return Value of property deprBatchId.
     */
    public java.lang.String getDeprBatchId() {
        return deprBatchId;
    }
    
    /**
     * Setter for property deprBatchId.
     * @param deprBatchId New value of property deprBatchId.
     */
    public void setDeprBatchId(java.lang.String deprBatchId) {
        this.deprBatchId = deprBatchId;
    }
    
    /**
     * Getter for property deprBranchCode.
     * @return Value of property deprBranchCode.
     */
    public java.lang.String getDeprBranchCode() {
        return deprBranchCode;
    }
    
    /**
     * Setter for property deprBranchCode.
     * @param deprBranchCode New value of property deprBranchCode.
     */
    public void setDeprBranchCode(java.lang.String deprBranchCode) {
        this.deprBranchCode = deprBranchCode;
    }
    
    /**
     * Getter for property lblDepBatchId.
     * @return Value of property lblDepBatchId.
     */
    public java.lang.String getLblDepBatchId() {
        return lblDepBatchId;
    }
    
    /**
     * Setter for property lblDepBatchId.
     * @param lblDepBatchId New value of property lblDepBatchId.
     */
    public void setLblDepBatchId(java.lang.String lblDepBatchId) {
        this.lblDepBatchId = lblDepBatchId;
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
     * Getter for property txtAssetId.
     * @return Value of property txtAssetId.
     */
    public String getTxtAssetId() {
        return txtAssetId;
    }
    
    /**
     * Setter for property txtAssetId.
     * @param txtAssetId New value of property txtAssetId.
     */
    public void setTxtAssetId(String txtAssetId) {
        this.txtAssetId = txtAssetId;
    }
    
    /**
     * Getter for property saleBatchId.
     * @return Value of property saleBatchId.
     */
    public String getSaleBatchId() {
        return saleBatchId;
    }
    
    /**
     * Setter for property saleBatchId.
     * @param saleBatchId New value of property saleBatchId.
     */
    public void setSaleBatchId(String saleBatchId) {
        this.saleBatchId = saleBatchId;
    }
    
    /**
     * Getter for property moveBatchId.
     * @return Value of property moveBatchId.
     */
    public String getMoveBatchId() {
        return moveBatchId;
    }
    
    /**
     * Setter for property moveBatchId.
     * @param moveBatchId New value of property moveBatchId.
     */
    public void setMoveBatchId(String moveBatchId) {
        this.moveBatchId = moveBatchId;
    }
    
    /**
     * Getter for property brkBatchId.
     * @return Value of property brkBatchId.
     */
    public String getBrkBatchId() {
        return brkBatchId;
    }
    
    /**
     * Setter for property brkBatchId.
     * @param brkBatchId New value of property brkBatchId.
     */
    public void setBrkBatchId(String brkBatchId) {
        this.brkBatchId = brkBatchId;
    }
    
}