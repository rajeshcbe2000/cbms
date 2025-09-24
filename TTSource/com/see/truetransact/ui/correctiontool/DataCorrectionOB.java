/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AuthorizeOB.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.correctiontool;

//import java.util.Observable;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Date;
//import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
//import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.transferobject.correctiontool.DataCorrectionLogTO;
import com.see.truetransact.transferobject.termloan.KccRenewalTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.*;

/**
 * Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class DataCorrectionOB extends CObservable {

    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private TableModel _tableRiskFundModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList = null;
    private TableModel searchTableModel;
    private ArrayList dataArrayList;
    private String relationalOperator = "";
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmNewProdId;
    private ComboBoxModel cbmCorrectionType;
    private ComboBoxModel cbmNewProductName;
    private ComboBoxModel cbmProductName;
    private ComboBoxModel cbmTransType;
    private ComboBoxModel cbmTransTypeDebit;
    private ComboBoxModel cbmTransTypeCredit;
    private ComboBoxModel cbmbranch;
    private ComboBoxModel cbmIndendBranch;
    private ComboBoxModel cbmIndendCorrectionType;
    private ComboBoxModel cbmDepo;
//    private ComboBoxModel cbmNoticeType;
    private Map guarantorMap = null;
    private ArrayList guarantorList = null;
    private String txtNoticeCharge = "";
    private String txtPostageCharge = "";
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType = "";
    private String result;
    private Date tdtAuctionDate = null;
    private String txtArbRate = "";
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    Date curDate = null;
    private String riskFundProdId = "";
    private String riskFundProdDesc = "";
    private String riskFundAcctHead = "";
    Rounding rd = new Rounding();

    public String getTxtArbRate() {
        return txtArbRate;
    }

    public void setTxtArbRate(String txtArbRate) {
        this.txtArbRate = txtArbRate;
    }

    /**
     * Creates a new instance of AuthorizeOB
     */
    public DataCorrectionOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DataCorrectionJNDI");
            map.put(CommonConstants.HOME, "correctiontool.DataCorrectionHome");
            map.put(CommonConstants.REMOTE, "correctiontool.DataCorrection");

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

//    public void fillDropDown() throws Exception {       
//        HashMap param = new HashMap();
//        param.put(CommonConstants.MAP_NAME, null);
//        final ArrayList lookupKey = new ArrayList();
//        lookupKey.add("SHARE_TYPE");
//        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
//        HashMap lookupValues = ClientUtil.populateLookupData(param);
//        key =  new ArrayList();
//        value = new ArrayList();
//        fillData((HashMap)lookupValues.get("SHARE_TYPE"));
//        System.out.println("key :: " + key +"  Value ::" + value);
//        this.cbmProdId = new ComboBoxModel(key,value);       
//        param = null;
//        lookupValues = null;
//        key =  new ArrayList();
//        value = new ArrayList();
//        key = null;
//        value = null;
//    }
//    private void fillDropDown() throws Exception {
//        lookupMap = new HashMap();
//        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
//        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
//        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//
//        HashMap param = new HashMap();
//        param.put(CommonConstants.MAP_NAME, "getProductsForKCCRenewal");
//        HashMap where = new HashMap();
//        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//        param.put(CommonConstants.PARAMFORQUERY, where);
//        where = null;
//        keyValue = ClientUtil.populateLookupData(param);
//        fillData((HashMap) keyValue.get(CommonConstants.DATA));
//        cbmProdId = new ComboBoxModel(key, value);
//    }
//    
//    
    
    
    
//      public void fillDropDown() throws Exception {
//        try {
//            HashMap lookUpHash = new HashMap();
//            lookUpHash.put(CommonConstants.MAP_NAME, null);
//            final ArrayList lookup_keys = new ArrayList();
//            lookup_keys.add("DATA.CORRECTION_TYPE");
//            //lookup_keys.add("PRODUCTTYPE");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            fillData((HashMap) keyValue.get("DATA.CORRECTION_TYPE"));
//            cbmCorrectionType = new ComboBoxModel(key, value);             
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
      
      
      
     private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);

        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("DATA.CORRECTION_TYPE");
        lookupKey.add("PRODUCTTYPE");
        lookupKey.add("ACCTHEADMAIN.BALTYPE");
        lookupKey.add("DATA.INDEND_CORRECTION_TYPE");

        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        final HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("DATA.CORRECTION_TYPE"));        
        cbmCorrectionType = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("PRODUCTTYPE"));        
        cbmNewProductName = new ComboBoxModel(key, value);
        cbmProductName = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("ACCTHEADMAIN.BALTYPE"));        
        cbmTransType = new ComboBoxModel(key, value);
        cbmTransTypeCredit = new ComboBoxModel(key, value);
        cbmTransTypeDebit = new ComboBoxModel(key, value);
        
        fillData((HashMap) lookupValues.get("DATA.INDEND_CORRECTION_TYPE"));        
        cbmIndendCorrectionType = new ComboBoxModel(key, value);
        
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranches", mapShare);
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_CODE"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);    
        cbmIndendBranch = new ComboBoxModel(key, value);   
    }  

    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }

    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }
     
     
     
      
     private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
 
      
    
    public void setCbmProdId(String prodType,String correctionType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    HashMap lookUpHash = new HashMap();
                    if (correctionType.equals("GOLD_ITEM_CHANGE") || correctionType.equals("GOLD_WEIGHT_CHANGE")) {
                       lookUpHash.put(CommonConstants.MAP_NAME, "getAllGoldLoanProducts");
                    }else{
                       lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    }
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    
     public void setCbmNewProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    HashMap lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmNewProdId = new ComboBoxModel(key, value);
        this.cbmNewProdId = cbmNewProdId;
        setChanged();
    }
    
    
    
    

    public void printSMS(HashMap smsMap) {
        try {
            smsMap = proxy.execute(smsMap, map);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
    }

    /**
     * updateStatus method used to update the database field based on the UI
     * button pressed
     *
     * @param map HashMap from UI which is passed as a argument to Authorize UI
     * constructor
     * @param status Passed by UI. (Authorize, Reject, Exception - statuses)
     */
    public HashMap renewAccounts(HashMap whereMap) {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + whereMap);
        List kccToLst = new ArrayList();
        KccRenewalTO objKccRenewalTO = null;
        obj.put("KCC_RENEWAL", "KCC_RENEWAL");
        if (whereMap.get("KCC_RENEWAL") != null) {
            List kccRenewalList = (List) whereMap.get("KCC_RENEWAL");
            if (kccRenewalList != null && kccRenewalList.size() > 0) {
                for (int i = 0; i < kccRenewalList.size(); i++) {
                    HashMap renewalMap = (HashMap) kccRenewalList.get(i);
                    objKccRenewalTO = insertKccRenewalDetail(renewalMap);
                    kccToLst.add(objKccRenewalTO);
                }
            }

        }
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("KCC_MULTIPLE_RENEWAL", "KCC_MULTIPLE_RENEWAL");
        obj.put("KccRenewalTO", kccToLst);
        System.out.println("map in LoanAwrdOB : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            setResult(ClientConstants.RESULT_STATUS[2]);
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    
    private DataCorrectionLogTO setAcctNoChangeCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setTransDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("TRANS_DT"))));
        objDataCorrectionLogTO.setTransId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        objDataCorrectionLogTO.setBatchId(CommonUtil.convertObjToStr(dataMap.get("BATCH_ID")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setTransMode(CommonUtil.convertObjToStr(dataMap.get("TRANS_MODE")));
        objDataCorrectionLogTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        objDataCorrectionLogTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objDataCorrectionLogTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
        objDataCorrectionLogTO.setInterBranchAcct(CommonUtil.convertObjToStr(dataMap.get("INTERBRANCH_ACCT")));
        objDataCorrectionLogTO.setAcctBranchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_BRANCH_ID")));
        if(dataMap.containsKey("AC_HD_ID") &&  dataMap.get("AC_HD_ID") != null){
            objDataCorrectionLogTO.setOtherBankAcctHead(CommonUtil.convertObjToStr(dataMap.get("AC_HD_ID")));
        }if(dataMap.containsKey("OTHER_BANK_PROD_ID") &&  dataMap.get("OTHER_BANK_PROD_ID") != null){
            objDataCorrectionLogTO.setOtherBankAcctProdId(CommonUtil.convertObjToStr(dataMap.get("OTHER_BANK_PROD_ID")));
        }
        return objDataCorrectionLogTO;
    }
    
     private DataCorrectionLogTO setTransAmtChangeCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setTransDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("TRANS_DT"))));
        //objDataCorrectionLogTO.setTransId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        objDataCorrectionLogTO.setBatchId(CommonUtil.convertObjToStr(dataMap.get("BATCH_ID")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        //objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setTransMode(CommonUtil.convertObjToStr(dataMap.get("TRANS_MODE")));
        //objDataCorrectionLogTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        objDataCorrectionLogTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        //objDataCorrectionLogTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
        objDataCorrectionLogTO.setInterBranchAcct(CommonUtil.convertObjToStr(dataMap.get("INTERBRANCH_ACCT")));
        objDataCorrectionLogTO.setAcctBranchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_BRANCH_ID")));
       
        return objDataCorrectionLogTO;
    }
     
     private DataCorrectionLogTO setTransTypeInterChangeCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setTransDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("TRANS_DT"))));
        //objDataCorrectionLogTO.setTransId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        objDataCorrectionLogTO.setBatchId(CommonUtil.convertObjToStr(dataMap.get("BATCH_ID")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setTransMode(CommonUtil.convertObjToStr(dataMap.get("TRANS_MODE")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        objDataCorrectionLogTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objDataCorrectionLogTO.setInterBranchAcct(CommonUtil.convertObjToStr(dataMap.get("INTERBRANCH_ACCT")));
        objDataCorrectionLogTO.setAcctBranchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_BRANCH_ID")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        return objDataCorrectionLogTO;
    } 
    
    private DataCorrectionLogTO setDataCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setTransDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("TRANS_DT"))));
        objDataCorrectionLogTO.setTransId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        objDataCorrectionLogTO.setBatchId(CommonUtil.convertObjToStr(dataMap.get("BATCH_ID")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setTransMode(CommonUtil.convertObjToStr(dataMap.get("TRANS_MODE")));
        objDataCorrectionLogTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        objDataCorrectionLogTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objDataCorrectionLogTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
        return objDataCorrectionLogTO;
    }
    
     public HashMap doTransAmtChangeCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setTransAmtChangeCorrectionLogTO(dataMap));
        System.out.println("map in doCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    
      public HashMap doTransTypeInterChangeCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setTransTypeInterChangeCorrectionLogTO(dataMap));
        obj.put("TRANS_ID_MAP",dataMap.get("TRANS_ID_MAP"));
        System.out.println("map in doCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    
     
    
    public HashMap doAcctNoChangeCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setAcctNoChangeCorrectionLogTO(dataMap));
        System.out.println("map in doCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    
     public HashMap doCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setDataCorrectionLogTO(dataMap));
        System.out.println("map in doCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
     
      public HashMap doGoldWeightCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("inside doGoldRelatedCorrectionProcess :: " + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setGoldDataCorrectionLogTO(dataMap));
        obj.put("GOLD_DATA_MAP",dataMap.get("GOLD_DATA_MAP"));
        System.out.println("map in doGoldRelatedCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            System.out.println("where :: " + where);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    
      
     public HashMap doGoldItemCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("inside doGoldRelatedCorrectionProcess :: " + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setGoldItemCorrectionLogTO(dataMap));
        System.out.println("map in doGoldRelatedCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            System.out.println("where :: " + where);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
      
      
      
    private DataCorrectionLogTO setGoldDataCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        return objDataCorrectionLogTO;
    }
    
    private DataCorrectionLogTO setGoldItemCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        return objDataCorrectionLogTO;
    }
    
    
    public HashMap processRiskFund(HashMap whereMap) {
        TTException exception = null;
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + whereMap);
        List riskFundAcctList = new ArrayList();
        KccRenewalTO objKccRenewalTO = null;
        obj.put("KCC_RENEWAL_RISKFUND_PROCESS", "KCC_RENEWAL_RISKFUND_PROCESS");
        if (whereMap.get("KCC_RISK_FUND_LIST") != null) {
            riskFundAcctList = (List) whereMap.get("KCC_RISK_FUND_LIST"); 
        }
        obj.put("PROD_ID",getRiskFundProdId());
        obj.put("PROD_DESC",getRiskFundProdDesc());
        obj.put("KCC_RISK_FUND_LIST",riskFundAcctList);
        obj.put("RISK_FUND_AC_HD",getRiskFundAcctHead());
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put(CommonConstants.SCREEN, getScreen());
        System.out.println("map in processRiskFund : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            setProxyReturnMap(where);
            setResult(ClientConstants.RESULT_STATUS[2]);
            return where;
        } catch (Exception e) {
             if (e instanceof TTException) {
                exception = (TTException) e;
             }else{
            e.printStackTrace();
            parseException.logException(e, true);           
            ClientUtil.showMessageWindow(e.getMessage());
             }
              setResult(ClientConstants.RESULT_STATUS[4]);
        }
         if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
             System.out.println("exceptionHashMap :: " + exceptionHashMap);
            if (exceptionHashMap != null) {
                String acctNum = "";
                if(exceptionHashMap.containsKey("ACCT_NUM") && exceptionHashMap.get("ACCT_NUM") != null){
                    acctNum = CommonUtil.convertObjToStr(exceptionHashMap.get("ACCT_NUM"));
                }
                ClientUtil.showMessageWindow("Error in the account number - " + acctNum);
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
            }
        }
        return null;
    }

    private KccRenewalTO insertKccRenewalDetail(HashMap renewalMap) {
        KccRenewalTO KccRenewalTO = new KccRenewalTO();
        try {

            KccRenewalTO.setActNum(CommonUtil.convertObjToStr(renewalMap.get("ACCT_NUM")));
            KccRenewalTO.setBorrowNo(CommonUtil.convertObjToStr(renewalMap.get("BORROW_NO")));
            KccRenewalTO.setFromDt(getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEW_FROM_DT")))));
            KccRenewalTO.setToDt(getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEW_TO_DT")))));
            KccRenewalTO.setStatus("CREATED");
            KccRenewalTO.setBranchID(TrueTransactMain.BRANCH_ID);
            KccRenewalTO.setStatusDt(curDate);
            KccRenewalTO.setLimit(CommonUtil.convertObjToDouble(renewalMap.get("LIMIT")));
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return KccRenewalTO;

    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    public String getSelected() {
        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
//        ArrayList selectedList = new ArrayList();
        String selected = "";
        for (int i = 0, j = _tableModel.getRowCount(); i < j; i++) {
            bln = (Boolean) _tableModel.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                if (prodType.equals("MDS")) {
                    selected += "'" + _tableModel.getValueAt(i, 2);
                } else {
                    selected += "'" + _tableModel.getValueAt(i, 1);
                }
                selected += "',";
//                selectedList.add(_tableModel.getValueAt(i, 1));
            }
        }
        selected = selected.length() > 0 ? selected.substring(0, selected.length() - 1) : "";
        System.out.println("#$#$ selected : " + selected);
        return selected;
    }

    public void setSelectAll(CTable table, Boolean selected) {
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
        }
    }

    public ArrayList populateRiskFundData(HashMap mapID, CTable tblRiskFundData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblRiskFundData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();
        List chargeList = new ArrayList();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        if (whereMap.containsKey("CHARGE_LIST")) {
            chargeList = (List) whereMap.get("CHARGE_LIST");
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblRiskFundData.getModel() instanceof TableSorter) {
            _tableRiskFundModel = ((TableSorter) tblRiskFundData.getModel()).getModel();
        } else if (tblRiskFundData.getModel() instanceof TableModel) {
            _tableRiskFundModel = (TableModel) tblRiskFundData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblRiskFundData.getModel();
        }

        while (_tableRiskFundModel != null && tblRiskFundData.getRowCount() > 0) {
            _tableRiskFundModel.removeRow(0);
        }
        while (tblModel != null && tblRiskFundData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblRiskFundData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("ACT_NUM");
            _heading.add("FROM_DT");
            _heading.add("TO_DT");
            _heading.add("LIMIT");
            _heading.add("AVAILABLE_BALANCE");
            _heading.add("RISK FUND");
            _heading.add("CGST");
            _heading.add("SGST");
            _heading.add("FLOOD CESS");
            _heading.add("TOTAL AMOUNT");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            newList.add(new Boolean(false));
            newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("FROM_DT")));
            newList.add(CommonUtil.convertObjToStr(map.get("TO_DT")));
            newList.add(CommonUtil.convertObjToStr(map.get("LIMIT")));
            newList.add(CommonUtil.convertObjToStr(map.get("AVAILABLE_BALANCE")));
            HashMap chargeTaxMap = getRiskFundAndTaxDetails(chargeList, CommonUtil.convertObjToStr(map.get("ACT_NUM")), CommonUtil.convertObjToDouble(map.get("LIMIT")));
            newList.add(CommonUtil.convertObjToStr(chargeTaxMap.get("RISK_FUND")));
            newList.add(CommonUtil.convertObjToStr(chargeTaxMap.get("CGST")));
            newList.add(CommonUtil.convertObjToStr(chargeTaxMap.get("SGST")));
            newList.add(CommonUtil.convertObjToStr(chargeTaxMap.get("FLOOD_CESS")));
            double totalAmount = CommonUtil.convertObjToDouble(chargeTaxMap.get("RISK_FUND"))+ CommonUtil.convertObjToDouble(chargeTaxMap.get("SGST")) + CommonUtil.convertObjToDouble(chargeTaxMap.get("CGST")) + CommonUtil.convertObjToDouble(chargeTaxMap.get("FLOOD_CESS"));
            newList.add(CommonUtil.convertObjToStr(totalAmount));
            data.add(newList);
        }
        setTblModel(tblRiskFundData, data, _heading);
        TableColumn col = null;
        tblRiskFundData.revalidate();
        if (tblRiskFundData.getModel() instanceof TableSorter) {
            _tableRiskFundModel = ((TableSorter) tblRiskFundData.getModel()).getModel();
        } else {
            _tableRiskFundModel = (TableModel) tblRiskFundData.getModel();
        }
        tempMap.clear();
        tempMap = null;
        return _heading;
    }

    private HashMap serviceTaxAmount(String desc) { // Added by nithya on 30-12-2019 for KD-1131
        HashMap checkForTaxMap = new HashMap();
        String scheme = desc;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", scheme);
        whereMap.put("CHARGE_DESC", "Risk Fund");
        String retStr = "";
        List resultList = ClientUtil.executeQuery("getCheckServiceTaxApplicable", whereMap);
        HashMap checkMap = new HashMap();
        if (resultList != null && resultList.size() > 0) {
            checkMap = (HashMap) resultList.get(0);
            if (checkMap != null && checkMap.containsKey("SERVICE_TAX_APPLICABLE") && checkMap.containsKey("SERVICE_TAX_ID")) {
                retStr = CommonUtil.convertObjToStr(checkMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_APPLICABLE", checkMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_ID", checkMap.get("SERVICE_TAX_ID"));
            }
        }
        return checkForTaxMap;
    }

    private HashMap calculateServiceTax(List chargelst, double riskFund) { // Added by nithya on 30-12-2019 for KD-1131
        List taxSettingsList = new ArrayList();
        HashMap checkForTaxMap = new HashMap();
        HashMap finalTaxMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            double chrgamt = 0;
            checkForTaxMap = serviceTaxAmount(getRiskFundProdDesc());
            if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                    HashMap serviceTaSettingsMap = new HashMap();
                    chrgamt = riskFund;
                    if (chrgamt > 0) {
                        serviceTaSettingsMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                        taxSettingsList.add(serviceTaSettingsMap);
                    }
                }
            }


        }
        //System.out.println("taxSettingsList :: " + taxSettingsList);
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curDate.clone());
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                HashMap serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                finalTaxMap.put("CGST", serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS));
                finalTaxMap.put("SGST", serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                finalTaxMap.put("FLOOD_CESS", serviceTax_Map.get(ServiceTaxCalculation.SERVICE_TAX));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            finalTaxMap.put("CGST", 0.0);
            finalTaxMap.put("SGST", 0.0);
            finalTaxMap.put("FLOOD_CESS", 0.0);
        }
        return finalTaxMap;
    }

    private HashMap getRiskFundAndTaxDetails(List chargeList, String AcctNo, double limit) {
        System.out.println("chargeList :: " + chargeList);
        System.out.println("\nAcctNo ::" + AcctNo);
        HashMap chargeTaxMap = new HashMap();
        if (chargeList != null && chargeList.size() > 0) {
            for (int i = 0; i < chargeList.size(); i++) {
                HashMap chargeMap = (HashMap) chargeList.get(i);
                setRiskFundAcctHead(CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD")));              
                String accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                double chargeAmt = 0;
                if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                    chargeAmt = limit * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                    float newchrgAmt = (float) chargeAmt;
                    long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                    if (roundOffType != 0) {
                        chargeAmt = rd.getNearest((long) (newchrgAmt * roundOffType), roundOffType) / roundOffType;
                    } else {
                        chargeAmt = newchrgAmt;
                    }
                    double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                    double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                    if (chargeAmt < minAmt) {
                        chargeAmt = minAmt;
                    }
                    if (chargeAmt > maxAmt) {
                        chargeAmt = maxAmt;
                    }
                    chargeTaxMap.put("RISK_FUND", chargeAmt);
                } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {

                    List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                    if (chargeslabLst != null && chargeslabLst.size() > 0) {
                        double minAmt = 0;
                        double maxAmt = 0;
                        for (int k = 0; k < chargeslabLst.size(); k++) {
                            LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);
                            double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
                            double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
                            if (limit >= minAmtRange && limit <= maxAmtRange) {
                                double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
                                minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
                                maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();

                                chargeAmt = limit * chargeRate / 100;
                                if (chargeAmt < minAmt) {
                                    chargeAmt = minAmt;
                                }
                                if (chargeAmt > maxAmt) {
                                    chargeAmt = maxAmt;
                                }
                                break;
                            }
                        }
                    }
                    long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                    if (roundOffType != 0) {
                        chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                    } else {
                        chargeAmt = chargeAmt;
                    }
                    chargeTaxMap.put("RISK_FUND", chargeAmt);
                } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                    chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                    chargeTaxMap.put("RISK_FUND", chargeAmt);
                }
            }
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                HashMap serviceTaxMap = calculateServiceTax(chargeList, CommonUtil.convertObjToDouble(chargeTaxMap.get("RISK_FUND")));
                chargeTaxMap.put("CGST", serviceTaxMap.get("CGST"));
                chargeTaxMap.put("SGST", serviceTaxMap.get("SGST"));
                chargeTaxMap.put("FLOOD_CESS", serviceTaxMap.get("FLOOD_CESS"));
            } else {
                chargeTaxMap.put("CGST", 0.0);
                chargeTaxMap.put("SGST", 0.0);
                chargeTaxMap.put("FLOOD_CESS", 0.0);
            }
        } else {
            chargeTaxMap.put("RISK_FUND", 0.0);
            chargeTaxMap.put("CGST", 0.0);
            chargeTaxMap.put("SGST", 0.0);
            chargeTaxMap.put("FLOOD_CESS", 0.0);
        }
        return chargeTaxMap;
    }

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }

    /**
     * Retrives data and populates the CTable using TableModel
     *
     * @param mapID HashMap used to retrive data from DB
     * @param tblData CTable object used to update the table with TableModel
     * @return Returns ArrayList for populating Search Combobox
     */
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
        boolean morethanoneRecord = false;
        if(list != null && list.size() > 1){            
            morethanoneRecord = true;
            list = null;
        }
        
        if(list == null){
            if(morethanoneRecord){
                ClientUtil.showAlertWindow("More than one record !!!\n Corrections not possible !!!");
            }else{
                ClientUtil.showAlertWindow("No Data !!!");
            }
        }

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();           
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("Trans Id");
            _heading.add("Batch Id");
            _heading.add("Ac Hd");
            _heading.add("Trans Type");
            _heading.add("Acct Num");
            _heading.add("Amount");
            _heading.add("link batchid");
            _heading.add("Particulars");
            _heading.add("Narration");
            _heading.add("Prod Id");
            _heading.add("Prod Desc");
            _heading.add("Screen");
            _heading.add("Status By");
            _heading.add("Authorize By");
            _heading.add("Prod Type");
            _heading.add("Branch");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("AC_HD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("AMOUNT")));
            newList.add(CommonUtil.convertObjToStr(map.get("LINK_BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PARTICULARS")));
            newList.add(CommonUtil.convertObjToStr(map.get("NARRATION")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PRODUCT")));
            newList.add(CommonUtil.convertObjToStr(map.get("SCREEN_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("AUTHORIZE_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
       
        tempMap.clear();
        tempMap = null;
        return _heading;
    }
    
    public ArrayList populateTransAmtChangeData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
       
        boolean morethanTwoRecord = false;
        if(list != null && list.size() > 2){            
            morethanTwoRecord = true;
            list = null;
        }
        
        if(list == null){
            if(morethanTwoRecord){
                ClientUtil.showAlertWindow("More than two record !!!\n Corrections not possible !!!");
            }else{
                ClientUtil.showAlertWindow("No Data !!!");
            }
        }

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();           
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("Trans Id");
            _heading.add("Batch Id");
            _heading.add("Ac Hd");
            _heading.add("Trans Type");
            _heading.add("Acct Num");
            _heading.add("Amount");
            _heading.add("link batchid");
            _heading.add("Particulars");
            _heading.add("Narration");
            _heading.add("Prod Id");
            _heading.add("Prod Desc");
            _heading.add("Screen");
            _heading.add("Status By");
            _heading.add("Authorize By");
            _heading.add("Prod Type");
            _heading.add("Branch");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("AC_HD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("AMOUNT")));
            newList.add(CommonUtil.convertObjToStr(map.get("LINK_BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PARTICULARS")));
            newList.add(CommonUtil.convertObjToStr(map.get("NARRATION")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PRODUCT")));
            newList.add(CommonUtil.convertObjToStr(map.get("SCREEN_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("AUTHORIZE_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
       
        tempMap.clear();
        tempMap = null;
        return _heading;
    }
    
    public ArrayList populateSecurityData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
        if(list == null){
            ClientUtil.showAlertWindow("No Data !!!");
        }

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();           
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            /*
            sd.acct_num,ls.borrow_no,ls.limit,sd.gross_weight,
sd.net_weight,sd.purity,sd.market_rate,sd.margin,
sd.margin_amt,sd.eligible_loan_amt,sd.particulars,
sd.status_by,sd.authorize_by
            */
            _heading.add("Acct No");
            _heading.add("Borrow No");
            _heading.add("Limit");
            _heading.add("Gross Weight");
            _heading.add("Net Weight");
            _heading.add("Purity");
            _heading.add("Market Rate");
            _heading.add("Margin");
            _heading.add("Margin Amt");
            _heading.add("Eligible LoanAmt");
            _heading.add("Particulars");
            _heading.add("Status By");
            _heading.add("Authorize By");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
              /*
            sd.acct_num,ls.borrow_no,ls.limit,sd.gross_weight,
sd.net_weight,sd.purity,sd.market_rate,sd.margin,
sd.margin_amt,sd.eligible_loan_amt,sd.particulars,
sd.status_by,sd.authorize_by
            */
            newList.add(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("BORROW_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("LIMIT")));
            newList.add(CommonUtil.convertObjToStr(map.get("GROSS_WEIGHT")));
            newList.add(CommonUtil.convertObjToStr(map.get("NET_WEIGHT")));
            newList.add(CommonUtil.convertObjToStr(map.get("PURITY")));
            newList.add(CommonUtil.convertObjToStr(map.get("MARKET_RATE")));
            newList.add(CommonUtil.convertObjToStr(map.get("MARGIN")));
            newList.add(CommonUtil.convertObjToStr(map.get("MARGIN_AMT")));
            newList.add(CommonUtil.convertObjToStr(map.get("ELIGIBLE_LOAN_AMT")));
            newList.add(CommonUtil.convertObjToStr(map.get("PARTICULARS")));
            newList.add(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("AUTHORIZE_BY")));
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
       
        tempMap.clear();
        tempMap = null;
        return _heading;
    }

    public Date calculateMaturityDate(Date renewFromDt, int noOfYears) {
        int yearTobeAdded = 1900;
        Date depDt = renewFromDt;
        depDt.setYear(depDt.getYear() + noOfYears);
        GregorianCalendar cal = new GregorianCalendar(depDt.getYear(), depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, depDt.getYear() + noOfYears);
        cal.add(GregorianCalendar.MONTH, depDt.getMonth());
        cal.add(GregorianCalendar.DAY_OF_MONTH, depDt.getDate());
        String matDt = DateUtil.getStringDate(cal.getTime());
        return depDt;
    }

   

    public void removeRowsFromGuarantorTable(CTable tblData) {
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;
    }

   public void removeRowsFromRiskFundTable(CTable tblData) {
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableRiskFundModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableRiskFundModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableRiskFundModel != null && tblData.getRowCount() > 0) {
            _tableRiskFundModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
    }


    private void seperateList(List list) {
        ArrayList tempList = new ArrayList();
//        System.out.println("@@@$$$ list : "+list);
        Map map = new HashMap();
        Iterator iterator = null;
        String cellData;
        for (int i = 0; i < list.size(); i++) {
            map = (HashMap) list.get(i);
//            System.out.println("@@@$$$ map : "+map);
            if (String.valueOf(map.get("CUST_TYPE")).equals("GUARANTOR")) {
                iterator = map.values().iterator();
//                if (guarantorList==null) {
                guarantorList = new ArrayList();
//                }
                guarantorList.add(new Boolean(false));
                if (guarantorMap == null) {
                    guarantorMap = new HashMap();
                }
//                System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
                while (iterator.hasNext()) {
                    cellData = CommonUtil.convertObjToStr(iterator.next());
                    guarantorList.add(cellData);
                }
//                System.out.println("@@@$$$ guarantorList : "+guarantorList);
                //Changed By Suresh
                String actNum = "";
                if (prodType.equals("MDS")) {
                    actNum = String.valueOf(map.get("CHITTAL_NO"));
                } else {
                    actNum = String.valueOf(map.get("ACT_NUM"));
                }
                if (guarantorMap.containsKey(actNum)) {
                    tempList = (ArrayList) guarantorMap.get(actNum);
                } else {
                    tempList = new ArrayList();
                }
                tempList.add(guarantorList);
                guarantorMap.put(actNum, tempList);
                list.remove(i--);
//                if (i<8) {
//                    System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
//                }
            }
        }
//        System.out.println("@@@$$$ final list : "+list);
//        System.out.println("@@@$$$ final guarantorMap : "+guarantorMap);
    }

    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        //tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0 || mColIndex == 9 || mColIndex == 10 || mColIndex == 11 || mColIndex == 12) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();

        tbl.setModel(tableSorter);
        tbl.revalidate();
    }

    /**
     * Table Object Setter method
     *
     * @param tbl CTable Object
     */
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }

    /**
     * Is Data Available or not checking Method
     *
     * @return Returns boolean
     */
    public boolean isAvailable() {
        return _isAvailable;
    }

    /**
     * fillData populates the UI based on the table row selected
     *
     * @param rowIndexSelected Selected Table Row index
     * @return Returns HashMap with Table Column & Row values for the selected
     * row.
     */
    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = _tableModel.getRow(rowIndexSelected);

        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            obj = rowdata.get(i);

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
//            hashdata.put(strColName, CommonUtil.convertObjToStr(obj));

            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }

        hashdata.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        // Adding Authorization Date
        hashdata.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());

        return hashdata;
    }

    /**
     * Getter method for TableModel
     *
     * @return Returns TableModel
     */
    public TableModel getTableModel() {
        return _tableModel;
    }

    /**
     * Search used to update the table model based on the search criteria given
     * by the user.
     *
     * @param searchTxt Search Text which is entered by the user
     * @param selCol Colunm selected from the combobox
     * @param selColCri Condition selected from the condition combobox
     * @param chkCase Match case checking
     */
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            for (int i = 0, j = _tblData.getRowCount(); i < j; i++) {
                arrOriRow = _tableModel.getRow(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) {
                        strArrData = strArrData.toUpperCase();
                    }

                    if ((selColCri == 2 && strArrData.equals(searchTxt))
                            || (selColCri == 0 && strArrData.startsWith(searchTxt))
                            || (selColCri == 1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri == 3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) {
                            arrFilterRow.add(arrOriRow);
                        }
                    }
                }
            }

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);

            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {

                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();

            _tblData.setModel(tableSorter);
            _tblData.revalidate();
        }
    }

    public void setDataArrayList() {
        dataArrayList = searchTableModel.getDataArrayList();
    }

    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase, String operator) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            for (int i = 0, j = dataArrayList.size(); i < j; i++) {
                arrOriRow = (ArrayList) dataArrayList.get(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) {
                        strArrData = strArrData.toUpperCase();
                    }

                    if ((selColCri == 2 && strArrData.equals(searchTxt))
                            || (selColCri == 0 && strArrData.startsWith(searchTxt))
                            || (selColCri == 1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri == 3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) {
                            arrFilterRow.add(arrOriRow);
                        }
                    }
                }
            }

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);

            if (relationalOperator.equals("Or")) {
                arrFilterRow.addAll(tempArrayList);
            }
            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {

                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();

            _tblData.setModel(tableSorter);
            _tblData.revalidate();
//            if (relationalOperator.equals("Or"))
            if (operator.equals("And")) {
                dataArrayList = arrFilterRow;
            }
            if (operator.equals("Or")) {
                if (tempArrayList == null) {
                    tempArrayList = new ArrayList();
                }
                tempArrayList = arrFilterRow;
            }
            relationalOperator = operator;
        }
    }

    /**
     * Getter for property searchTableModel.
     *
     * @return Value of property searchTableModel.
     */
    public ArrayList getTableModel(CTable table) {
        TableModel tblModel = null;
        if (table.getModel() instanceof TableSorter) {
            tblModel = ((TableSorter) table.getModel()).getModel();
        } else if (table.getModel() instanceof TableModel) {
            tblModel = (TableModel) table.getModel();
        }
        System.out.println("@@## Data ArrayList : " + tblModel.getDataArrayList());
        return tblModel.getDataArrayList();
    }

    /**
     * Getter for property searchTableModel.
     *
     * @return Value of property searchTableModel.
     */
    public com.see.truetransact.clientutil.TableModel getSearchTableModel() {
        return searchTableModel;
    }

    /**
     * Setter for property searchTableModel.
     *
     * @param searchTableModel New value of property searchTableModel.
     */
    public void setSearchTableModel(com.see.truetransact.clientutil.TableModel searchTableModel) {
        this.searchTableModel = searchTableModel;
        setDataArrayList();
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property guarantorMap.
     *
     * @return Value of property guarantorMap.
     */
    public java.util.Map getGuarantorMap() {
        return guarantorMap;
    }

    /**
     * Setter for property guarantorMap.
     *
     * @param guarantorMap New value of property guarantorMap.
     */
    public void setGuarantorMap(java.util.Map guarantorMap) {
        this.guarantorMap = guarantorMap;
    }

    /**
     * Getter for property txtNoticeCharge.
     *
     * @return Value of property txtNoticeCharge.
     */
    public java.lang.String getTxtNoticeCharge() {
        return txtNoticeCharge;
    }

    /**
     * Setter for property txtNoticeCharge.
     *
     * @param txtNoticeCharge New value of property txtNoticeCharge.
     */
    public void setTxtNoticeCharge(java.lang.String txtNoticeCharge) {
        this.txtNoticeCharge = txtNoticeCharge;
    }

    /**
     * Getter for property txtPostageCharge.
     *
     * @return Value of property txtPostageCharge.
     */
    public java.lang.String getTxtPostageCharge() {
        return txtPostageCharge;
    }

    /**
     * Setter for property txtPostageCharge.
     *
     * @param txtPostageCharge New value of property txtPostageCharge.
     */
    public void setTxtPostageCharge(java.lang.String txtPostageCharge) {
        this.txtPostageCharge = txtPostageCharge;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property tdtAuctionDate.
     *
     * @return Value of property tdtAuctionDate.
     */
    public Date getTdtAuctionDate() {
        return tdtAuctionDate;
    }

    /**
     * Setter for property tdtAuctionDate.
     *
     * @param tdtAuctionDate New value of property tdtAuctionDate.
     */
    public void setTdtAuctionDate(Date tdtAuctionDate) {
        this.tdtAuctionDate = tdtAuctionDate;
    }

    public String getRiskFundProdDesc() {
        return riskFundProdDesc;
    }

    public void setRiskFundProdDesc(String riskFundProdDesc) {
        this.riskFundProdDesc = riskFundProdDesc;
    }

    public String getRiskFundProdId() {
        return riskFundProdId;
    }

    public void setRiskFundProdId(String riskFundProdId) {
        this.riskFundProdId = riskFundProdId;
    }

    public String getRiskFundAcctHead() {
        return riskFundAcctHead;
    }

    public void setRiskFundAcctHead(String riskFundAcctHead) {
        this.riskFundAcctHead = riskFundAcctHead;
    }

    public ComboBoxModel getCbmCorrectionType() {
        return cbmCorrectionType;
    }

    public void setCbmCorrectionType(ComboBoxModel cbmCorrectionType) {
        this.cbmCorrectionType = cbmCorrectionType;
    }

    public ComboBoxModel getCbmNewProductName() {
        return cbmNewProductName;
    }

    public void setCbmNewProductName(ComboBoxModel cbmNewProductName) {
        this.cbmNewProductName = cbmNewProductName;
    }

    public ComboBoxModel getCbmProductName() {
        return cbmProductName;
    }

    public void setCbmProductName(ComboBoxModel cbmProductName) {
        this.cbmProductName = cbmProductName;
    }

    public ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }

    public void setCbmTransType(ComboBoxModel cbmTransType) {
        this.cbmTransType = cbmTransType;
    }

    public ComboBoxModel getCbmTransTypeDebit() {
        return cbmTransTypeDebit;
    }

    public void setCbmTransTypeDebit(ComboBoxModel cbmTransTypeDebit) {
        this.cbmTransTypeDebit = cbmTransTypeDebit;
    }

    public ComboBoxModel getCbmTransTypeCredit() {
        return cbmTransTypeCredit;
    }

    public void setCbmTransTypeCredit(ComboBoxModel cbmTransTypeCredit) {
        this.cbmTransTypeCredit = cbmTransTypeCredit;
    }

    public ComboBoxModel getCbmNewProdId() {
        return cbmNewProdId;
    }

    public void setCbmNewProdId(ComboBoxModel cbmNewProdId) {
        this.cbmNewProdId = cbmNewProdId;
    }
    
    public ArrayList populateTransTypeInterChangeData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
       
        
        boolean morethanTwoRecord = false;
        if(list != null && list.size() > 2){            
            morethanTwoRecord = true;
            list = null;
        }
        
        if(list == null){
            if(morethanTwoRecord){
                ClientUtil.showAlertWindow("More than two record !!!\n Corrections not possible !!!");
            }else{
                ClientUtil.showAlertWindow("No Data !!!");
            }
        }

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();           
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("Trans Id");
            _heading.add("Batch Id");
            _heading.add("Ac Hd");
            _heading.add("Trans Type");
            _heading.add("Acct Num");
            _heading.add("Amount");
            _heading.add("link batchid");
            _heading.add("Particulars");
            _heading.add("Narration");
            _heading.add("Prod Id");
            _heading.add("Prod Desc");
            _heading.add("Screen");
            _heading.add("Status By");
            _heading.add("Authorize By");
            _heading.add("Prod Type");
            _heading.add("Branch");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("AC_HD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("AMOUNT")));
            newList.add(CommonUtil.convertObjToStr(map.get("LINK_BATCH_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PARTICULARS")));
            newList.add(CommonUtil.convertObjToStr(map.get("NARRATION")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("PRODUCT")));
            newList.add(CommonUtil.convertObjToStr(map.get("SCREEN_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("STATUS_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("AUTHORIZE_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("BRANCH_ID")));
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
       
        tempMap.clear();
        tempMap = null;
        return _heading;
    }
    
     private DataCorrectionLogTO setAllAcctNoChangeCorrectionLogTO(HashMap dataMap){    
        DataCorrectionLogTO objDataCorrectionLogTO =  new DataCorrectionLogTO();
        objDataCorrectionLogTO.setBranchCode(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objDataCorrectionLogTO.setTransDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("TRANS_DT"))));
        objDataCorrectionLogTO.setTransId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        objDataCorrectionLogTO.setBatchId(CommonUtil.convertObjToStr(dataMap.get("BATCH_ID")));
        objDataCorrectionLogTO.setModificationType(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));
        objDataCorrectionLogTO.setOldFieldValue(CommonUtil.convertObjToStr(dataMap.get("OLD_VALUE")));
        objDataCorrectionLogTO.setNewFieldValue(CommonUtil.convertObjToStr(dataMap.get("NEW_VALUE")));
        objDataCorrectionLogTO.setOldStatusBy(CommonUtil.convertObjToStr(dataMap.get("STATUS_BY")));
        objDataCorrectionLogTO.setOldAuthorizedBy(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZED_BY")));
        objDataCorrectionLogTO.setCorrectionUserId(CommonUtil.convertObjToStr(dataMap.get("CORRECTION_STAFF")));
        objDataCorrectionLogTO.setCorrectionDt(curDate);
        objDataCorrectionLogTO.setCorrectionAuthorizeStaff(CommonUtil.convertObjToStr(dataMap.get("AUTH_STAFF")));
        objDataCorrectionLogTO.setCorrectionAuthorizeDt(curDate);
        objDataCorrectionLogTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        objDataCorrectionLogTO.setTransMode(CommonUtil.convertObjToStr(dataMap.get("TRANS_MODE")));
        objDataCorrectionLogTO.setProdType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));
        objDataCorrectionLogTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
        objDataCorrectionLogTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")));
        objDataCorrectionLogTO.setTransType(CommonUtil.convertObjToStr(dataMap.get("TRANS_TYPE")));
        objDataCorrectionLogTO.setInterBranchAcct(CommonUtil.convertObjToStr(dataMap.get("INTERBRANCH_ACCT")));
        objDataCorrectionLogTO.setAcctBranchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_BRANCH_ID")));
       
        //New and old value settings
        objDataCorrectionLogTO.setNewProdType(CommonUtil.convertObjToStr(dataMap.get("NEW_PROD_TYPE")));
        objDataCorrectionLogTO.setNewProdId(CommonUtil.convertObjToStr(dataMap.get("NEW_PROD_ID")));
        objDataCorrectionLogTO.setNewAcctNum(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_NUM")));
        objDataCorrectionLogTO.setNewHeadId(CommonUtil.convertObjToStr(dataMap.get("NEW_AC_HD_ID")));        
        objDataCorrectionLogTO.setOldProdType(CommonUtil.convertObjToStr(dataMap.get("OLD_PROD_TYPE")));
        objDataCorrectionLogTO.setOldProdId(CommonUtil.convertObjToStr(dataMap.get("OLD_PROD_ID")));
        objDataCorrectionLogTO.setOldAcctNum(CommonUtil.convertObjToStr(dataMap.get("OLD_ACT_NUM")));
        objDataCorrectionLogTO.setOldHeadId(CommonUtil.convertObjToStr(dataMap.get("OLD_AC_HD_ID")));  
        
        return objDataCorrectionLogTO;
    }
    
    
     public HashMap doAllAcctNoChangeCorrectionProcess(HashMap dataMap) {
        HashMap obj = new HashMap();
        System.out.println("doAllAcctNoChangeCorrectionProcess@@@@!!!1111>>>>" + dataMap);
        obj.put("CORRECTION_TYPE", CommonUtil.convertObjToStr(dataMap.get("CORRECTION_TYPE")));       
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("DataCorrectionLogTO", setAllAcctNoChangeCorrectionLogTO(dataMap));
        System.out.println("map in doAllAcctNoChangeCorrectionProcess : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            if(where.containsKey("STATUS") && where.get("STATUS") != null && CommonUtil.convertObjToStr(where.get("STATUS")).equals("SUCCESS")){
              setResult(ClientConstants.RESULT_STATUS[2]);
            }
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }

    public ComboBoxModel getCbmIndendBranch() {
        return cbmIndendBranch;
    }

    public void setCbmIndendBranch(ComboBoxModel cbmIndendBranch) {
        this.cbmIndendBranch = cbmIndendBranch;
    }

    public ComboBoxModel getCbmIndendCorrectionType() {
        return cbmIndendCorrectionType;
    }

    public void setCbmIndendCorrectionType(ComboBoxModel cbmIndendCorrectionType) {
        this.cbmIndendCorrectionType = cbmIndendCorrectionType;
    }

    public ComboBoxModel getCbmDepo() {
        return cbmDepo;
    }

    public void setCbmDepo(ComboBoxModel cbmDepo) {
        this.cbmDepo = cbmDepo;
    }
     
     
    public ArrayList populateIndendTransData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put(CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        if (whereMap != null && whereMap.containsKey("EDIT")) {
            editFlag = CommonUtil.convertObjToStr(whereMap.get("EDIT"));
        }
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }

        while (_tableModel != null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
                  
        if(list == null){            
                ClientUtil.showAlertWindow("No Data !!!");            
        }

        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        _isAvailable = list.size() > 0 ? true : false;

        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;

        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();           
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("IR ID");
            _heading.add("Trans Type");
            _heading.add("Depo Id");
            _heading.add("Depo Name");
            _heading.add("Purchase Amt");
            _heading.add("Amount");
            _heading.add("Remark");
            _heading.add("Store");
        }
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        Date renewFromDt;
        Date toDt;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            double amt = 0;
            double fee = 0;
            map = (HashMap) list.get(i);
            //ir.irid ,ir.trans_type,ir.depid,dm.depo_name ,ir.purchase_amt,ir.amount,ir.remark,ir.store_name  
            newList.add(CommonUtil.convertObjToStr(map.get("IRID")));
            newList.add(CommonUtil.convertObjToStr(map.get("TRANS_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("DEPID")));
            newList.add(CommonUtil.convertObjToStr(map.get("DEPO_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("PURCHASE_AMT")));
            newList.add(CommonUtil.convertObjToStr(map.get("AMOUNT")));
            newList.add(CommonUtil.convertObjToStr(map.get("REMARK")));
            newList.add(CommonUtil.convertObjToStr(map.get("STORE_NAME")));
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
       
        tempMap.clear();
        tempMap = null;
        return _heading;
    }
    
  }