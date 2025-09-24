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
package com.see.truetransact.ui.deposit.depositfrommobileapp;

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
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.common.nominee.NomineeTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.customer.CustomerAddressTO;
import com.see.truetransact.transferobject.customer.CustomerPhoneTO;
import com.see.truetransact.transferobject.customer.CustomerProofTo;
import com.see.truetransact.transferobject.customer.CustomerTO;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.operativeaccount.AccountParamTO;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
import com.see.truetransact.transferobject.share.ShareAccInfoTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.transferobject.termloan.KccRenewalTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.share.ShareMRB;
import java.util.*;

/**
 * Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class DepositAutoCreationOB extends CObservable {

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
    private ComboBoxModel cbmCustAgentId;
    private ComboBoxModel cbmShareAgentId;
    private ComboBoxModel cbmDepositAgentId;
    private ComboBoxModel cbmSBAgentId;
    
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
    final String YES = "Y";
    final String NO = "N";
    Rounding rd = new Rounding();
    private int minorAge = 0;
    private int retireAge = 0;
    private HashMap custCreationResultMap;
    private HashMap shareOpeningResultMap;
    private HashMap depositOpeningResultMap;
    private HashMap SBOpeningResultMap;
    private HashMap shareDetailsMap;
    private static final int YEARLY = 365;
    private static final int MONTHLY = 30;
    public String getTxtArbRate() {
        return txtArbRate;
    }

    public void setTxtArbRate(String txtArbRate) {
        this.txtArbRate = txtArbRate;
    }

    /**
     * Creates a new instance of AuthorizeOB
     */
    public DepositAutoCreationOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            fillDropDown();
             List lst = ClientUtil.executeQuery("getMinorRetireAge", null);
        HashMap ageMap = new HashMap();
        ageMap = (HashMap) lst.get(0);
        minorAge = CommonUtil.convertObjToInt(ageMap.get("MINOR_AGE"));
        retireAge = CommonUtil.convertObjToInt(ageMap.get("RETIREMENT_AGE"));
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositAutoCreationJNDI");
            map.put(CommonConstants.HOME, "deposit.depositfrommobileapp.DepositAutoCreationHome");
            map.put(CommonConstants.REMOTE, "deposit.depositfrommobileapp.DepositAutoCreation");

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
   
    
    
      // To Fill the Combo boxes in the UI
    private void fillDropDown() throws Exception {
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = (List)ClientUtil.executeQuery("getAgentIDDetailsForDepositCreation", where);
        getMap(lst);
        setCbmCustAgentId(new ComboBoxModel(key,value));
        setCbmShareAgentId(new ComboBoxModel(key,value));
        setCbmDepositAgentId(new ComboBoxModel(key,value));
        setCbmSBAgentId(new ComboBoxModel(key,value));
    }
    
    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
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
        obj.put("PROD_ID", getRiskFundProdId());
        obj.put("PROD_DESC", getRiskFundProdDesc());
        obj.put("KCC_RISK_FUND_LIST", riskFundAcctList);
        obj.put("RISK_FUND_AC_HD", getRiskFundAcctHead());
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
            } else {
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
                if (exceptionHashMap.containsKey("ACCT_NUM") && exceptionHashMap.get("ACCT_NUM") != null) {
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
            double totalAmount = CommonUtil.convertObjToDouble(chargeTaxMap.get("RISK_FUND")) + CommonUtil.convertObjToDouble(chargeTaxMap.get("SGST")) + CommonUtil.convertObjToDouble(chargeTaxMap.get("CGST")) + CommonUtil.convertObjToDouble(chargeTaxMap.get("FLOOD_CESS"));
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
        
        if(list == null || list.size() == 0){
            ClientUtil.showAlertWindow("No Data Found !!!");
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
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            String head = "";
            _heading.add("ID");
            _heading.add("NAME");
            _heading.add("DOB");
            _heading.add("GENDER");
            _heading.add("MARITAL STATUS");
            _heading.add("HOUSE");
            _heading.add("PLACE");
            _heading.add("CITY");
            _heading.add("PIN");
            _heading.add("MOBILE NO");
            _heading.add("ID PROOF");
            _heading.add("UNIQUE ID");
            _heading.add("SHARETYPE");
            _heading.add("SHARENO");
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
            /*
            MM.MOB_DEP_ID,
        MD.CUST_NAME,
        MD.DOB,
        GENDER,
        MD.MARITAL_STATUS,
        MD.HOUSE_NAME,
        MD.PLACE,
        MD.CITY,
        MD.PINCODE,
        MD.MOBILE_NO,
        MD.IDENTITY_PROOF,
        MD.UNIQUE_ID,
        MD.PHOTO,
        MD.SIGN,
        MD.SHARE_TYPE,
        MM.SHARE_NO
            */
            //MOB_DEP_ID,CUST_NAME,DOB,MARITAL_STATUS,HOUSE_NAME,PLACE,CITY,
 //PINCODE,MOBILE_NO,IDENTITY_PROOF,UNIQUE_ID,PHOTO,SIGN,SHARE_TYPE,SHARE_NO
            newList.add(CommonUtil.convertObjToStr(map.get("MOB_DEP_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("DOB")));            
            newList.add(CommonUtil.convertObjToStr(map.get("GENDER")));
            newList.add(CommonUtil.convertObjToStr(map.get("MARITAL_STATUS")));
            newList.add(CommonUtil.convertObjToStr(map.get("HOUSE_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("PLACE")));
            newList.add(CommonUtil.convertObjToStr(map.get("CITY")));
            newList.add(CommonUtil.convertObjToStr(map.get("PINCODE")));
            newList.add(CommonUtil.convertObjToStr(map.get("MOBILE_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("IDENTITY_PROOF")));
            newList.add(CommonUtil.convertObjToStr(map.get("UNIQUE_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_TYPE")));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_NO")));           
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
    
    public ArrayList populateShareData(HashMap mapID, CTable tblData) {
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

        while (_tableModel != null && _tableModel.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
        if(list == null || list.size() == 0){
            ClientUtil.showAlertWindow("No Data Found !!!");
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
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            //
//MOB_DEP_ID,MOB_DEPDETAIL_ID,CUST_ID,FNAME,SHARE_TYPE,SHARE_AMOUNT,APPLCATION_FEE,
//MEMBERSHIP_FEE,SERVICE_TAX,TOTAL_SHARES,TOTAL_AMOUNT
             String head = "";
            _heading.add("MOB_DEP_ID");
            _heading.add("MOB_DEPDETAIL_ID");
            _heading.add("CUST_ID");
            _heading.add("FNAME");
            _heading.add("SHARE_TYPE");           
            _heading.add("FACE_VALUE");
            _heading.add("SHARE_AMOUNT");
            _heading.add("NO OF SHARES");
            _heading.add("TOTAL_AMOUNT");
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
            newList.add(CommonUtil.convertObjToStr(map.get("MOB_DEP_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("MOB_DEPDETAIL_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_ID")));            
            newList.add(CommonUtil.convertObjToStr(map.get("FNAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_TYPE")));            
            HashMap faceValueMap = (HashMap)(getShareDetailsMap().get(map.get("SHARE_TYPE")));          
            newList.add(CommonUtil.convertObjToStr(faceValueMap.get("FACE_VALUE")));     
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_AMOUNT")));
            double faceValue = CommonUtil.convertObjToDouble(faceValueMap.get("FACE_VALUE"));
            double shareAmt = CommonUtil.convertObjToDouble(map.get("SHARE_AMOUNT"));
            double noOfShare = shareAmt/faceValue;
            double total = noOfShare * faceValue;
            newList.add(CommonUtil.convertObjToStr(noOfShare));
            newList.add(CommonUtil.convertObjToStr(total));
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
        GregorianCalendar cal = new GregorianCalendar(depDt.getYear() + yearTobeAdded, depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, depDt.getYear() + noOfYears);
        cal.add(GregorianCalendar.MONTH, depDt.getMonth());
        cal.add(GregorianCalendar.DAY_OF_MONTH, depDt.getDate());
        String matDt = DateUtil.getStringDate(cal.getTime());
        return cal.getTime();
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

    public ComboBoxModel getCbmCustAgentId() {
        return cbmCustAgentId;
    }

    public void setCbmCustAgentId(ComboBoxModel cbmCustAgentId) {
        this.cbmCustAgentId = cbmCustAgentId;
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

    // Functions for customer creation starts here
    private CustomerTO setCustomerData(HashMap custDataMap) throws Exception {
        CustomerTO objCustomerTO = new CustomerTO();
        objCustomerTO.setCustType("INDIVIDUAL");
        objCustomerTO.setCommand("INSERT");
        objCustomerTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        objCustomerTO.setCreateddt(curDate);
        objCustomerTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        objCustomerTO.setFname(CommonUtil.convertObjToStr(custDataMap.get("CUST_NAME")));
        objCustomerTO.setCustomergroup("NONE");
        objCustomerTO.setCustomerStatus("PRESENT");
        objCustomerTO.setIntroType("NOT_APPLICABLE");
        objCustomerTO.setJoiningDate(curDate);
        objCustomerTO.setAmsam(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
        objCustomerTO.setDesam(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
        Date DobDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custDataMap.get("DOB")));
        //objCustomerTO.setDob(DobDt);
        objCustomerTO.setDob(getProperDateFormat(DobDt));
        // Calculate age,retiredt & retireage
          if (DobDt != null) {
                long a = DateUtil.dateDiff(DobDt, curDate);
                long b = a / 365;
                if(b < 18){
                    objCustomerTO.setMinor(YES);
                }else{
                    objCustomerTO.setMinor(NO);
                }
                String ages = String.valueOf(b);
                objCustomerTO.setAge(CommonUtil.convertObjToInt(ages));
                int agediff = retireAge - CommonUtil.convertObjToInt(ages);
                if (agediff > 0) {
                    Date retDt = DateUtil.addDays(DobDt, (retireAge * 365));
                    objCustomerTO.setRetDt(retDt);
                    long diff = DateUtil.dateDiff(DobDt, retDt);
                    long retAge = diff / 365;
                    objCustomerTO.setRetAge(CommonUtil.convertObjToInt(retAge));
                }
            }
        // End
        objCustomerTO.setRetDt(getProperDateFormat(objCustomerTO.getRetDt()));
        objCustomerTO.setCustTypeId("CUSTOMER");       
        objCustomerTO.setNetworth(0.0);
        if(custDataMap.get("GENDER").equals("F")){
            objCustomerTO.setGender("Female");
            objCustomerTO.setTitle("MRS");
        }else if(custDataMap.get("GENDER").equals("M")){
            objCustomerTO.setGender("Male");
            objCustomerTO.setTitle("MR");
        }else{
          objCustomerTO.setGender(CommonUtil.convertObjToStr(custDataMap.get("GENDER")));
        }        
        objCustomerTO.setResidentialstatus("RESIDENT");
        objCustomerTO.setStatus("CREATED");
        if(custDataMap.get("MARITAL_STATUS").equals("M")){
          objCustomerTO.setMaritalstatus("Married");
        }else{
          objCustomerTO.setMaritalstatus("Single");  
        }
        objCustomerTO.setProfession("");
        objCustomerTO.setMinority("");
        objCustomerTO.setCommAddrType("HOME");
        objCustomerTO.setNationality("INDIAN");
        //objCustomerTO.setMembershipClass(CommonUtil.convertObjToStr(custDataMap.get("SHARE_TYPE")));
        objCustomerTO.setAddrVerified(YES);
        objCustomerTO.setPhoneVerified(YES);
        objCustomerTO.setObtainFinstat(YES);
        objCustomerTO.setSendThanksLetter(YES);
        objCustomerTO.setConfirmThanks(YES);
        objCustomerTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerTO.setStatusDt(curDate);
        objCustomerTO.setAddrProof(CommonUtil.convertObjToStr(custDataMap.get("IDENTITY_PROOF")));
        objCustomerTO.setIdenProof(CommonUtil.convertObjToStr(custDataMap.get("IDENTITY_PROOF")));
        objCustomerTO.setMembershipNum(CommonUtil.convertObjToStr(custDataMap.get("SHARE_NO")));
        objCustomerTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objCustomerTO.setStatus(CommonConstants.STATUS_CREATED);
        objCustomerTO.setCreateddt(curDate);
        objCustomerTO.setCreatedBy(TrueTransactMain.USER_ID);
        objCustomerTO.setAgentCustId(CommonUtil.convertObjToStr(getCbmCustAgentId().getDataForKey(getCbmCustAgentId().getKeyForSelected()))); // To be taken from combo in the screen
        return objCustomerTO;
    }
    
    
    public HashMap addAddressMap(HashMap custDataMap) {
        HashMap addressMap = null;
        HashMap phoneMap = null;
        try {
            final CustomerAddressTO objCustomerAddressTO = new CustomerAddressTO();
            if (addressMap == null) {
                addressMap = new HashMap();
            }
            if (phoneMap == null) {
                phoneMap = new HashMap();
            }
            
            objCustomerAddressTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objCustomerAddressTO.setStatus(CommonConstants.STATUS_CREATED);
            objCustomerAddressTO.setStatusBy(TrueTransactMain.USER_ID);
            objCustomerAddressTO.setStatusDt(curDate);
            objCustomerAddressTO.setStreet(CommonUtil.convertObjToStr(custDataMap.get("HOUSE_NAME")));
            objCustomerAddressTO.setArea(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
            objCustomerAddressTO.setCity(CommonUtil.convertObjToStr(custDataMap.get("CITY")));
            objCustomerAddressTO.setState("KERALA");
            objCustomerAddressTO.setPostOffice(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
            objCustomerAddressTO.setPinCode(CommonUtil.convertObjToStr(custDataMap.get("PINCODE")));
            //objCustomerAddressTO.setCountryCode(CommonUtil.convertObjToStr(getCbmCountry().getKeyForSelected()));
            objCustomerAddressTO.setAddrType("HOME");
            //objCustomerAddressTO.setRemarks(getTxtAddrRemarks());
            objCustomerAddressTO.setVillage(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
            objCustomerAddressTO.setTaluk(CommonUtil.convertObjToStr(custDataMap.get("PLACE")));
            objCustomerAddressTO.setWardName(CommonUtil.convertObjToStr(custDataMap.get("PLACE"))); // Added by nithya
            addressMap.put("HOME", objCustomerAddressTO);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return addressMap;
    }
    
    
     public HashMap addPhoneList(HashMap custDataMap) {
         HashMap phoneList = null;
         HashMap phoneMap = new HashMap();
        try {
            CustomerPhoneTO objCustomerPhoneTO = new CustomerPhoneTO();            
            if (phoneList == null) {
                phoneList = new HashMap();
            }
            objCustomerPhoneTO.setPhoneId(new Double(1));
               objCustomerPhoneTO.setStatus(CommonConstants.STATUS_CREATED);
                        objCustomerPhoneTO.setStatusBy(TrueTransactMain.USER_ID);
                        objCustomerPhoneTO.setStatusDt(curDate);
                        objCustomerPhoneTO.setPhoneTypeId("MOBILE");
            objCustomerPhoneTO.setAreaCode("");
            objCustomerPhoneTO.setPhoneNumber(CommonUtil.convertObjToStr(custDataMap.get("MOBILE_NO")));
            objCustomerPhoneTO.setAddrType("HOME");
            phoneList.put(objCustomerPhoneTO.getPhoneId(), objCustomerPhoneTO);          
        } catch (Exception e) {
            parseException.logException(e, true);
        }
         if (phoneList != null) {
                phoneMap.put("HOME", phoneList);
            }
        return phoneMap;
    }
    
     public HashMap addProofMap(HashMap custDataMap) {
        HashMap proofMap = null;
         try {
            final CustomerProofTo objCustomerProofTo = new CustomerProofTo();
            if (proofMap == null) {
                proofMap = new HashMap();
            }
            objCustomerProofTo.setStatus(CommonConstants.STATUS_CREATED);
            objCustomerProofTo.setUniqueId(CommonUtil.convertObjToStr(custDataMap.get("UNIQUE_ID")));
            objCustomerProofTo.setProofType(CommonUtil.convertObjToStr(custDataMap.get("IDENTITY_PROOF")));
            proofMap.put(CommonUtil.convertObjToStr(custDataMap.get("IDENTITY_PROOF")), objCustomerProofTo);          
           
        } catch (Exception e) {
            parseException.logException(e, true);
        }
         
        return proofMap; 
    }    

     
      public HashMap createCustomerProcess(HashMap whereMap) throws Exception {
        HashMap obj = new HashMap();
        System.out.println("wheremappppp@@@@!!!1111>>>>" + whereMap);
        List allCustomeList = new ArrayList();
        HashMap singleCustomerMap ;
        HashMap singleCustomerDepMap ;
        obj.put("CUSTOMER_CREATION_LIST", "CUSTOMER_CREATION_LIST");
        if (whereMap.get("CUSTOMER_CREATION_LIST") != null) {
            List custCollectedList = (List) whereMap.get("CUSTOMER_CREATION_LIST");
            if (custCollectedList != null && custCollectedList.size() > 0) {
                for (int i = 0; i < custCollectedList.size(); i++) {
                    singleCustomerDepMap = new HashMap();
                    singleCustomerMap = new HashMap();
                    HashMap renewalMap = (HashMap) custCollectedList.get(i);
                    singleCustomerMap.put("PHONE", addPhoneList(renewalMap));
                    singleCustomerMap.put("CUSTOMER",setCustomerData(renewalMap));
                    singleCustomerMap.put("ADDRESS",addAddressMap(renewalMap));
                    singleCustomerMap.put("Proof",addProofMap(renewalMap));                    
                    singleCustomerMap.put("INCOMEPAR",new HashMap());
                    singleCustomerMap.put("LANDDETAILS",new HashMap());
                    singleCustomerMap.put("PREVINTROTYPE",new HashMap());
                    singleCustomerMap.put("FINANCE",new HashMap());
                    singleCustomerMap.put("JointAccntTO",new LinkedHashMap());
                    singleCustomerMap.put("AuthPersonsTO",new HashMap());
                    singleCustomerMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    singleCustomerMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                    singleCustomerMap.put("PHOTO", null);
                    singleCustomerMap.put("PROOF_PHOTO", null);
                    singleCustomerMap.put("SIGN",null);
                    singleCustomerMap.put("NOT_APPLICABLE",new HashMap());
                    singleCustomerMap.put("INTRO_TYPE","NOT_APPLICABLE");
                    singleCustomerMap.put("USER_ID", TrueTransactMain.USER_ID);
                    singleCustomerDepMap.put(renewalMap.get("MOB_DEP_ID"),singleCustomerMap);
                    allCustomeList.add(singleCustomerDepMap);
                }
            }
        }
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("OPEN_CUSTOMER_ID", "OPEN_CUSTOMER_ID");
        obj.put("CUSTOMER_CREATION_LIST", allCustomeList);
        //System.out.println("map in Create customer process : " + obj);
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
      
      
    public HashMap shareOpeningProcess(HashMap whereMap) throws Exception {
        HashMap obj = new HashMap();
        System.out.println("inside shareOpeningProcess" + whereMap);
        List allShareList = new ArrayList();
        HashMap agentDetailMap = new HashMap();
        HashMap singleShareMap ;
        HashMap singleShareDepMap ;
        String agentId = CommonUtil.convertObjToStr(getCbmShareAgentId().getKeyForSelected());
        System.out.println("agentId  :: " + agentId);
        whereMap.put("AGENT_ID",agentId);
        List agentAccountList = ClientUtil.executeQuery("getAgentAccountDetails", whereMap);
        if(agentAccountList != null && agentAccountList.size() > 0){
            agentDetailMap = (HashMap)agentAccountList.get(0);
        }
        obj.put("SHARE_OPENING_LIST", "SHARE_OPENING_LIST");
        if (whereMap.get("SHARE_OPENING_LIST") != null) {
            List shareCollectedList = (List) whereMap.get("SHARE_OPENING_LIST");
            if (shareCollectedList != null && shareCollectedList.size() > 0) {
                for (int i = 0; i < shareCollectedList.size(); i++) {
                    singleShareDepMap = new HashMap();
                    singleShareMap = new HashMap();
                    HashMap shareDetailMap = (HashMap) shareCollectedList.get(i);
                    singleShareMap.put("SMSSubscriptionTO", null);
                    singleShareMap.put("DRFAccountNomineeDeleteTO", new ArrayList());
                    singleShareMap.put("DRFAccountNomineeTO",new ArrayList());
                    singleShareMap.put("AccountNomineeDeleteTO", new ArrayList());
                    singleShareMap.put("AccountNomineeTO", new ArrayList());
                    singleShareMap.put("MODULE", null);
                    singleShareMap.put("SCREEN",null);
                    singleShareMap.put("SHARE_TYPE",shareDetailMap.get("SHARE_TYPE"));
                    singleShareMap.put("NO_OF_SHARES",shareDetailMap.get("NO_OF_SHARES"));
                    singleShareMap.put("TOTAL_AMOUNT",shareDetailMap.get("TOTAL_AMOUNT"));
                    singleShareMap.put("ShareAccDet",setShareAcctDetData(shareDetailMap));
                    singleShareMap.put("ShareAccInfo",setShareAccInfoData(shareDetailMap));     
                    singleShareMap.put("TransactionTO",getTransactionMap(shareDetailMap,agentDetailMap));
                    singleShareMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    singleShareMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());                   
                    singleShareMap.put("USER_ID", TrueTransactMain.USER_ID);
                    singleShareDepMap.put(shareDetailMap.get("MOB_DEP_ID"),singleShareMap);
                    allShareList.add(singleShareDepMap);
                }
            }
        }
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("OPEN_SHARE_ACT", "OPEN_SHARE_ACT");
        obj.put("SHARE_OPENING_LIST", allShareList);
        System.out.println("map in Share Opening process : " + obj);
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
    
    
    private LinkedHashMap getTransactionMap(HashMap detailMap, HashMap agentDetailMap){
        LinkedHashMap transactionMap = new LinkedHashMap();
        LinkedHashMap finalMap = new LinkedHashMap();
        TransactionTO objTransactionTO = new TransactionTO();
        objTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objTransactionTO.setApplName(CommonUtil.convertObjToStr(agentDetailMap.get("FNAME")));
        objTransactionTO.setTransType("TRANSFER");
        objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(detailMap.get("TOTAL_AMOUNT")));
        objTransactionTO.setProductId(CommonUtil.convertObjToStr(agentDetailMap.get("DP_PROD_ID")));
        objTransactionTO.setProductType(CommonUtil.convertObjToStr(agentDetailMap.get("DP_PROD_TYPE")));
        objTransactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(agentDetailMap.get("DP_ACT_NUM")));
        objTransactionTO.setInstType("VOUCHER");
        objTransactionTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objTransactionTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        transactionMap.put("1",objTransactionTO);
        finalMap.put("NOT_DELETED_TRANS_TOs",transactionMap);        
        return finalMap;
    }
    
    
    private ShareAccInfoTO setShareAccInfoData(HashMap shareDataMap){
        ShareAccInfoTO objShareAccInfoTO = new ShareAccInfoTO();
        try{
            objShareAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objShareAccInfoTO.setAcctStatus("PROVISIONAL");
            objShareAccInfoTO.setConstitution("INDIVIDUAL");
            objShareAccInfoTO.setApplFee(0.0);
            objShareAccInfoTO.setConnectedGroup("");
            objShareAccInfoTO.setCustId(CommonUtil.convertObjToStr(shareDataMap.get("CUST_ID")));
            objShareAccInfoTO.setTxtRatification("N");
            objShareAccInfoTO.setDirectorRelative(null);            
            objShareAccInfoTO.setMemFee(0.0);
	    objShareAccInfoTO.setNotEligibleDt(null);
            objShareAccInfoTO.setNotEligibleLoan("N");
            objShareAccInfoTO.setPropertyDetails("");
            objShareAccInfoTO.setRelativeMembers("");
            objShareAccInfoTO.setResolutionNo("");
            objShareAccInfoTO.setShareAmount(CommonUtil.convertObjToDouble(shareDataMap.get("TOTAL_AMOUNT")));
            objShareAccInfoTO.setShareFee(CommonUtil.convertObjToDouble(0.0));
            objShareAccInfoTO.setWelfareFundPaid("");
            objShareAccInfoTO.setShareType(CommonUtil.convertObjToStr(shareDataMap.get("SHARE_TYPE")));
            if(getShareDetailsMap() != null && getShareDetailsMap().containsKey(CommonUtil.convertObjToStr(shareDataMap.get("SHARE_TYPE")))){
                System.out.println("share map here :: " + getShareDetailsMap().get(CommonUtil.convertObjToStr(shareDataMap.get("SHARE_TYPE"))));
                HashMap shareConfMap = (HashMap)getShareDetailsMap().get(CommonUtil.convertObjToStr(shareDataMap.get("SHARE_TYPE")));
                if(shareConfMap.containsKey("RATIFICATION_REQUIRED") && shareConfMap.get("RATIFICATION_REQUIRED") != null){
                    objShareAccInfoTO.setTxtRatification(CommonUtil.convertObjToStr(shareConfMap.get("RATIFICATION_REQUIRED")));
                }                
            }
            if(objShareAccInfoTO.getTxtRatification().equals("Y")){
                objShareAccInfoTO.setIdIssueDt(null);
            }else{
                objShareAccInfoTO.setIdIssueDt(curDate);
            }
            objShareAccInfoTO.setRemarks("FROM_MOBILE_APP");
            objShareAccInfoTO.setCommAddrType("HOME");
	    objShareAccInfoTO.setCreatedDt(curDate);
            objShareAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objShareAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objShareAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objShareAccInfoTO.setStatusDt(curDate);
            objShareAccInfoTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);            
            objShareAccInfoTO.setBranchCode(TrueTransactMain.BRANCH_ID);                         
            objShareAccInfoTO.setCboDivProdType("");
            objShareAccInfoTO.setCboDivProdId("");
            objShareAccInfoTO.setTxtDivAcNo("");
            objShareAccInfoTO.setCboDivPayMode("");
            objShareAccInfoTO.setIdCardNo("");
            objShareAccInfoTO.setTdtIDIssuedDt(null);
            objShareAccInfoTO.setChkDuplicateIDCardYN("");
            objShareAccInfoTO.setChkDrfApplicableYN("N");
            objShareAccInfoTO.setCboDrfProdId("");            
            objShareAccInfoTO.setTdtIDResolutionDt(curDate);
            objShareAccInfoTO.setTxtIDResolutionNo("");
            objShareAccInfoTO.setImbp(0.0);
            objShareAccInfoTO.setEmpRefNoNew("");
            objShareAccInfoTO.setEmpRefNoOld("");
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objShareAccInfoTO;
    }
    
    
     private LinkedHashMap setShareAcctDetData(HashMap shareDetailMap) throws Exception {
        HashMap singleRecordShareAcctDet;
        LinkedHashMap shareAcctDetTOMap = new LinkedHashMap();
        ShareAcctDetailsTO objShareAcctDetailsTO;
        int tblShareAcctDetSize = 1;
        for(int i = 0;i<tblShareAcctDetSize;i++){
                objShareAcctDetailsTO = new ShareAcctDetailsTO();
                objShareAcctDetailsTO.setShareAcctDetNo("1");
                objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(shareDetailMap.get("NO_OF_SHARES")));
                objShareAcctDetailsTO.setShareCertIssueDt(curDate);
                objShareAcctDetailsTO.setTxtNoShares(CommonUtil.convertObjToStr(shareDetailMap.get("NO_OF_SHARES")));
                objShareAcctDetailsTO.setTxtShareValue(CommonUtil.convertObjToDouble(shareDetailMap.get("TOTAL_AMOUNT")));
                objShareAcctDetailsTO.setTxtShareMemFee(CommonUtil.convertObjToDouble(0.0));
                objShareAcctDetailsTO.setTxtShareApplFee(CommonUtil.convertObjToDouble(0.0));
                objShareAcctDetailsTO.setTxtTotShareFee(CommonUtil.convertObjToDouble(0.0));
                objShareAcctDetailsTO.setTxtShareTotAmount(CommonUtil.convertObjToStr(shareDetailMap.get("TOTAL_AMOUNT")));
                objShareAcctDetailsTO.setTxtFromSL_No(0);
                objShareAcctDetailsTO.setTxtToSL_No(1);
                objShareAcctDetailsTO.setShareNoFrom("ADD");
                objShareAcctDetailsTO.setShareNoTo("");
                objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                objShareAcctDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
                objShareAcctDetailsTO.setStatusDt(curDate);
                objShareAcctDetailsTO.setShareStatus(CommonConstants.STATUS_RESOLUTION_ACCEPT);                
                objShareAcctDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
                objShareAcctDetailsTO.setStatusDt(curDate);
                objShareAcctDetailsTO.setResolutionNo("");
                shareAcctDetTOMap.put(String.valueOf(i),objShareAcctDetailsTO);
                objShareAcctDetailsTO = null;
                singleRecordShareAcctDet = null;
            }
        return shareAcctDetTOMap;
    }
    
      
       public ArrayList populateDepositData(HashMap mapID, CTable tblData) {
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

        while (_tableModel != null && _tableModel.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
        if(list == null || list.size() == 0){
            ClientUtil.showAlertWindow("No Data Found !!!");
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
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            _heading.add("MOB_DEP_ID");
            _heading.add("CUST_ID");
            _heading.add("NAME");
            _heading.add("SHARE_NO");           
            _heading.add("PROD_ID");
            _heading.add("PRODUCT");
            _heading.add("TYPE");
            _heading.add("DEPOSIT_AMOUNT");
            _heading.add("DEPOSIT_PERIOD");
            _heading.add("DEPOSIT_PERIOD_TYPE");
            _heading.add("ROI");
            _heading.add("DEPOSIT_DT");
            _heading.add("MATURITY_DT");
            _heading.add("MATURITY_AMOUNT");
            _heading.add("TOTAL_INT");
            _heading.add("MOBILE NO");
            _heading.add("NOMINEE");
            _heading.add("RELATIONSHIP");
            _heading.add("REMARKS");
        }      
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            map = (HashMap) list.get(i);
            newList.add(new Boolean(false));          
            newList.add(CommonUtil.convertObjToStr(map.get("MOB_DEP_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_ID")));            
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_NO")));           
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID")));     
            newList.add(CommonUtil.convertObjToStr(map.get("PRODUCT")));   
            newList.add(CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE")));
            newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_AMOUNT")));
            newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_PERIOD")));           
            newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_PERIOD_TYPE")));  
            HashMap depDetailsMap = getDepositInterest_MaturityAmtMap(map);
            System.out.println("depDetailsMap :: " + depDetailsMap);
            newList.add(CommonUtil.convertObjToDouble(depDetailsMap.get("ROI")));
            newList.add(DateUtil.getStringDate(curDate));
            newList.add(DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depDetailsMap.get("MATURITY_DT")))));  
            double maturityAmt = CommonUtil.convertObjToDouble(depDetailsMap.get("AMOUNT"));
            maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
            double interest = CommonUtil.convertObjToDouble(depDetailsMap.get("INTEREST"));
            interest = (double) getNearest((long) (interest * 100), 100) / 100;
            if(CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE")).equals("DAILY")){
                maturityAmt = 0.0;
                interest = 0.0;
            }
            newList.add(maturityAmt); 
            newList.add(interest); 
            newList.add(CommonUtil.convertObjToStr(map.get("MOBILE_NO")));  
            newList.add(CommonUtil.convertObjToStr(map.get("NOMINEE_NAME")));  
            newList.add(CommonUtil.convertObjToStr(map.get("RELATIONSHIP")));  
            newList.add(CommonUtil.convertObjToStr(map.get("REMARKS")));  
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
     
     
    public ArrayList populateSBData(HashMap mapID, CTable tblData) {
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

        while (_tableModel != null && _tableModel.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
        while (tblModel != null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;

        tempMap.putAll(whereMap);

        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
                whereMap);
        
        if(list == null || list.size() == 0){
            ClientUtil.showAlertWindow("No Data Found !!!");
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
            if (isMultiSelect) {
                _heading.add("Select");
            }
            sizeList = new ArrayList();
            if (isMultiSelect) {
                sizeList.add(new Integer(6));
            }
            _heading.add("MOB_DEP_ID");
            _heading.add("CUST_ID");
            _heading.add("NAME");
            _heading.add("SHARE_NO");           
            _heading.add("PROD_ID");
            _heading.add("PRODUCT");
            _heading.add("TYPE");
            _heading.add("AMOUNT");
           // _heading.add("DEPOSIT_PERIOD");
           // _heading.add("DEPOSIT_PERIOD_TYPE");
           // _heading.add("ROI");
            _heading.add("DEPOSIT_DT");
            //_heading.add("MATURITY_DT");
            //_heading.add("MATURITY_AMOUNT");
            //_heading.add("TOTAL_INT");
            _heading.add("MOBILE NO");
            _heading.add("NOMINEE");
            _heading.add("RELATIONSHIT");
            _heading.add("REMARKS");
        }      
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            map = (HashMap) list.get(i);
            newList.add(new Boolean(false));          
            newList.add(CommonUtil.convertObjToStr(map.get("MOB_DEP_ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_ID")));            
            newList.add(CommonUtil.convertObjToStr(map.get("CUST_NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("SHARE_NO")));           
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID")));     
            newList.add(CommonUtil.convertObjToStr(map.get("PRODUCT")));   
            newList.add(CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE")));
            newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_AMOUNT")));
            //newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_PERIOD")));           
            //newList.add(CommonUtil.convertObjToStr(map.get("DEPOSIT_PERIOD_TYPE")));
            //newList.add(CommonUtil.convertObjToDouble(depDetailsMap.get("ROI")));
            newList.add(DateUtil.getStringDate(curDate));
            //newList.add(maturityAmt); 
            //newList.add(interest); 
            newList.add(CommonUtil.convertObjToStr(map.get("MOBILE_NO")));  
            newList.add(CommonUtil.convertObjToStr(map.get("NOMINEE_NAME")));   
            newList.add(CommonUtil.convertObjToStr(map.get("RELATIONSHIP")));
            newList.add(CommonUtil.convertObjToStr(map.get("REMARKS")));
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
     
     
   private HashMap getDepositInterest_MaturityAmtMap(HashMap depositDataMap){
    HashMap amountMap =  new HashMap();
    String depositPeriodType = CommonUtil.convertObjToStr(depositDataMap.get("DEPOSIT_PERIOD_TYPE"));
    int periodDays = 0;
    int maturityDays = 0;
    if(depositPeriodType.equals("Y")){
        amountMap.put("PERIOD_DAYS",0);
        amountMap.put("PERIOD_YEARS",depositDataMap.get("DEPOSIT_PERIOD"));
        amountMap.put("PERIOD_MONTHS",0);
        periodDays = CommonUtil.convertObjToInt(depositDataMap.get("DEPOSIT_PERIOD")) * YEARLY;
        maturityDays = periodDays;
    }else if(depositPeriodType.equals("M")){
        amountMap.put("PERIOD_DAYS",0);
        amountMap.put("PERIOD_YEARS",0);
        amountMap.put("PERIOD_MONTHS",depositDataMap.get("DEPOSIT_PERIOD"));
        periodDays = CommonUtil.convertObjToInt(depositDataMap.get("DEPOSIT_PERIOD")) * MONTHLY;
        maturityDays = (CommonUtil.convertObjToInt(depositDataMap.get("DEPOSIT_PERIOD"))/12) * YEARLY;
    }else{
        amountMap.put("PERIOD_DAYS",depositDataMap.get("DEPOSIT_PERIOD"));
        amountMap.put("PERIOD_YEARS",0);
        amountMap.put("PERIOD_MONTHS",0);
        periodDays = CommonUtil.convertObjToInt(depositDataMap.get("DEPOSIT_PERIOD"));
        maturityDays = periodDays;
    } 
    int days = CommonUtil.convertObjToInt(amountMap.get("PERIOD_DAYS"));
    int months = CommonUtil.convertObjToInt(amountMap.get("PERIOD_MONTHS"));
    int years = CommonUtil.convertObjToInt(amountMap.get("PERIOD_YEARS"));
    HashMap maturityMap = new HashMap();
    maturityMap.put("DEPOSIT_DT", curDate);
    maturityMap.put("ADD_DAYS",periodDays);
    String maturityDate = calculateMatDate(days, months, years, 0);
    System.out.println("maturity  date *** new :: " + maturityDate);
    amountMap.put("CATEGORY_ID","GENERAL_CATEGORY");
    amountMap.put("ROUND_OFF","NEAREST_VALUE");    
    amountMap.put("BEHAVES_LIKE",depositDataMap.get("BEHAVES_LIKE"));
    amountMap.put("NEAREST_VALUE","NEAREST_VALUE");    
    double roi = getROI(depositDataMap, DateUtil.getDateMMDDYYYY(maturityDate));
    amountMap.put("ROI",roi);
    amountMap.put("COMP_FREQ",0); // To be clarified
    amountMap.put("AMOUNT",depositDataMap.get("DEPOSIT_AMOUNT"));
    amountMap.put("DISCOUNTED_RATE","N");    
    amountMap.put("INT_APPL_FREQ",YEARLY);
    amountMap.put("PROD_ID",depositDataMap.get("PROD_ID"));   
    amountMap.put("PEROID", periodDays);
    amountMap.put("MATURITY_DT",DateUtil.getDateMMDDYYYY(maturityDate));
    amountMap.put("DAYS_YEARS",YEARLY);
    amountMap.put("DEPOSIT_DT",curDate);
    amountMap.put("AMT_MULTIPLES",1.0);//To be clarified
    amountMap.put("INT_TYPE","SIMPLE");
    InterestCalc interestCalc = new InterestCalc();
    HashMap amtDetHash = interestCalc.calcAmtDetails(amountMap);
    amtDetHash.put("MATURITY_DT", maturityDate);
    amtDetHash.put("ROI",roi);
    System.out.println("amtDetHash :: " + amtDetHash);   
    return amtDetHash;        
   }
     
    private double getROI(HashMap dataMap, Date maturityDate) {
        double roi = 0.0;
        HashMap roiMap = new HashMap();
        long period = DateUtil.dateDiff(curDate, maturityDate);
        roiMap.put("PRODUCT_TYPE", "TD");
        roiMap.put("PROD_ID", dataMap.get("PROD_ID"));
        roiMap.put("CATEGORY_ID", "GENERAL_CATEGORY");
        roiMap.put("AMOUNT", dataMap.get("DEPOSIT_AMOUNT"));
        roiMap.put("PERIOD", period);
        roiMap.put("DEPOSIT_DT", curDate);
        List roiList = (List) ClientUtil.executeQuery("icm.getInterestRates", roiMap);
        if (roiList != null && roiList.size() > 0) {
            roiMap = (HashMap) roiList.get(0);
            roi = CommonUtil.convertObjToDouble(roiMap.get("ROI")).doubleValue();
        }
        return roi;
    }
   
   
   public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }
   
   public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

   
   private String calculateMatDate(int days,int months,int years,int weeks) {
       String maturityDate = null;
       int yeartobeAdded = 1900;
        Date depDate = curDate;
        System.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yeartobeAdded), depDate.getMonth(), depDate.getDate());
            if (years > 0) {
                cal.add(GregorianCalendar.YEAR, years);
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if (months > 0) {
                cal.add(GregorianCalendar.MONTH, months);
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if (days > 0) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, days);
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);

            }
            if (weeks > 0) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, (weeks * 7));
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);

            }
           System.out.println("DateUtil.getStringDate(cal.getTime()) :: " + DateUtil.getStringDate(cal.getTime()));
            maturityDate =DateUtil.getStringDate(cal.getTime());
        }
        return maturityDate;
    }
   
   
   
    public HashMap depositOpeningProcess(HashMap whereMap) throws Exception {
        HashMap obj = new HashMap();
        System.out.println("inside depositOpeningProcess" + whereMap);
        List allDepositList = new ArrayList();
        HashMap agentDetailMap = new HashMap();
        HashMap singleDepositMap ;
        HashMap singleDepositDetMap ;
        String agentId = CommonUtil.convertObjToStr(getCbmDepositAgentId().getKeyForSelected());
        System.out.println("agentId  :: " + agentId);
        whereMap.put("AGENT_ID",agentId);
        List agentAccountList = ClientUtil.executeQuery("getAgentAccountDetails", whereMap);
        if(agentAccountList != null && agentAccountList.size() > 0){
            agentDetailMap = (HashMap)agentAccountList.get(0);
        }
        if (whereMap.get("DEPOSIT_OPENING_LIST") != null) {
            List depositCollectedList = (List) whereMap.get("DEPOSIT_OPENING_LIST");
            if (depositCollectedList != null && depositCollectedList.size() > 0) {
                for (int i = 0; i < depositCollectedList.size(); i++) {
                    singleDepositDetMap = new HashMap();
                    singleDepositMap = new HashMap();
                    HashMap depositDetailMap = (HashMap) depositCollectedList.get(i);
                    // For deposit
                    AccInfoTO objAccInfoTO = setAccInfoData(depositDetailMap);
                    singleDepositMap.put("TERMDEPOSIT", objAccInfoTO); 
                    singleDepositMap.put("SCREEN", "Deposit Auto Creation");
                    singleDepositMap.put("UI_PRODUCT_TYPE","TD");
                    singleDepositMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                    singleDepositMap.put("TransactionTO",getTransactionMap(depositDetailMap,agentDetailMap));
                    singleDepositMap.put("Transaction Details Data",getTransDetailMap(agentDetailMap,depositDetailMap));  
                    singleDepositMap.put("DepSubNoAccInfoTO",setDepSubNoAccInfoData(depositDetailMap));
                    if(depositDetailMap.containsKey("NOMINEE_NAME") && depositDetailMap.get("NOMINEE_NAME") != null && CommonUtil.convertObjToStr(depositDetailMap.get("NOMINEE_NAME")).length() > 0){
                      singleDepositMap.put("AccountNomineeTO", getNomineeList(depositDetailMap));
                    }
                    singleDepositMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    singleDepositMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());                   
                    singleDepositMap.put("USER_ID", TrueTransactMain.USER_ID);
                    singleDepositDetMap.put(depositDetailMap.get("MOB_DEP_ID"),singleDepositMap);
                    singleDepositMap.put("MODE","INSERT");
                    singleDepositMap.put("BEHAVES_LIKE",depositDetailMap.get("BEHAVES_LIKE"));
                    if(depositDetailMap.containsKey("MOBILE_NO") && depositDetailMap.get("MOBILE_NO") != null && CommonUtil.convertObjToStr(depositDetailMap.get("MOBILE_NO")).length() > 0){
                        singleDepositMap.put("SMSSubscriptionTO",getMobileBanking(objAccInfoTO,CommonUtil.convertObjToStr(depositDetailMap.get("MOBILE_NO"))));
                    }
                    allDepositList.add(singleDepositDetMap);
                }
            }
        }
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        obj.put("OPEN_DEPOSIT_ACT", "OPEN_DEPOSIT_ACT");
        obj.put("DEPOSIT_OPENING_LIST", allDepositList);
        System.out.println("map in Deposit Opening process : " + obj);
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
    
    
    private ArrayList getNomineeList(HashMap depositDetailMap){
        ArrayList nomineeList = new ArrayList();
        NomineeTO objNomineeTO =  new NomineeTO();
        objNomineeTO.setNomineeId("1");
        objNomineeTO.setNomineeName(CommonUtil.convertObjToStr(depositDetailMap.get("NOMINEE_NAME")));
        objNomineeTO.setRelationship(CommonUtil.convertObjToStr(depositDetailMap.get("RELATIONSHIP")));
        objNomineeTO.setStatusDt(curDate);
        objNomineeTO.setSharePer(Double.valueOf(100.0));
        nomineeList.add(objNomineeTO);
        return  nomineeList;
    }
    
   
    private SMSSubscriptionTO getMobileBanking(AccInfoTO objAccInfoTO, String mobileNo) {
        SMSSubscriptionTO objSMSSubscriptionTO = new SMSSubscriptionTO();
        objSMSSubscriptionTO.setProdType("TD");
        objSMSSubscriptionTO.setProdId(objAccInfoTO.getProdId());
        objSMSSubscriptionTO.setActNum(objAccInfoTO.getDepositNo());
        objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(mobileNo));
        objSMSSubscriptionTO.setSubscriptionDt((Date) curDate.clone());
        if (!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
        objSMSSubscriptionTO.setStatusDt((Date) curDate.clone());
        objSMSSubscriptionTO.setCreatedBy(ProxyParameters.USER_ID);
        objSMSSubscriptionTO.setCreatedDt((Date) curDate.clone());
        return objSMSSubscriptionTO;
    }

    private HashMap getTransDetailMap(HashMap agentMap,HashMap dataMap){
        HashMap transDetailMap = new HashMap();
        transDetailMap.put("TRANS_TYPE","TRANSFER");
        transDetailMap.put("USER_ID",TrueTransactMain.USER_ID);
        transDetailMap.put("CR_ACT_NUM",agentMap.get("DP_ACT_NUM"));
        transDetailMap.put("PROD_TYPE",agentMap.get("DP_PROD_TYPE"));
        transDetailMap.put("DR_PROD_ID",agentMap.get("DP_PROD_ID"));
        transDetailMap.put("TRANSACTION_PART","TRANSACTION_PART");
        transDetailMap.put("DEPOSIT_AMOUNT",dataMap.get("DEPOSIT_AMOUNT"));
        transDetailMap.put("PROD_ID",dataMap.get("PROD_ID"));
        return transDetailMap;
    }

    
     public HashMap setDepSubNoAccInfoData(HashMap depositDataMap) { 
         System.out.println("depositDataMap :: " + depositDataMap);
        LinkedHashMap depSubNoTOMap = new LinkedHashMap();
        try {
            DepSubNoAccInfoTO objDepSubNoAccInfoTO;
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
            objDepSubNoAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objDepSubNoAccInfoTO.setAdtAmt(0.0);
            objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(depositDataMap.get("DEPOSIT_AMOUNT")));
            objDepSubNoAccInfoTO.setDepositDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositDataMap.get("DEPOSIT_DT"))));
            objDepSubNoAccInfoTO.setDepositNo("");
            if (depositDataMap.get("DEPOSIT_PERIOD_TYPE").equals("M")) {
                objDepSubNoAccInfoTO.setDepositPeriodDd(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(depositDataMap.get("DEPOSIT_PERIOD")));
                objDepSubNoAccInfoTO.setDepositPeriodYy(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodWk(0.0);
            } else if (depositDataMap.get("DEPOSIT_PERIOD_TYPE").equals("Y")) {
                objDepSubNoAccInfoTO.setDepositPeriodDd(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodMm(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(depositDataMap.get("DEPOSIT_PERIOD")));
                objDepSubNoAccInfoTO.setDepositPeriodWk(0.0);
            } else {
                objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(depositDataMap.get("DEPOSIT_PERIOD")));
                objDepSubNoAccInfoTO.setDepositPeriodMm(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodYy(0.0);
                objDepSubNoAccInfoTO.setDepositPeriodWk(0.0);
            }
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
            objDepSubNoAccInfoTO.setIntpayFreq(0.0);
            objDepSubNoAccInfoTO.setIntpayMode("CASH");
            objDepSubNoAccInfoTO.setInstallType("");
            objDepSubNoAccInfoTO.setPaymentType("");
            objDepSubNoAccInfoTO.setPaymentDay(null);
            objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(depositDataMap.get("MATURITY_AMOUNT")));
            objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositDataMap.get("MATURITY_DT"))));
            objDepSubNoAccInfoTO.setPeriodicIntAmt(0.0);
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(depositDataMap.get("ROI")));
            objDepSubNoAccInfoTO.setCreateBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusDt(curDate);
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));
            objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(depositDataMap.get("TOTAL_INT")));
            objDepSubNoAccInfoTO.setIntPayProdId("");
            objDepSubNoAccInfoTO.setIntPayProdType("");
            objDepSubNoAccInfoTO.setIntPayAcNo("");
            objDepSubNoAccInfoTO.setCalender_freq("N");
            objDepSubNoAccInfoTO.setCalender_date(null);
            objDepSubNoAccInfoTO.setCalender_day(0.0);
            objDepSubNoAccInfoTO.setFlexi_status("N");
            objDepSubNoAccInfoTO.setTotalIntCredit(0.0);
            objDepSubNoAccInfoTO.setTotalIntDrawn(0.0);
            objDepSubNoAccInfoTO.setSalaryRecovery("N");
            objDepSubNoAccInfoTO.setAcctStatus("NEW");
            objDepSubNoAccInfoTO.setPostageAmt(0.0);
            objDepSubNoAccInfoTO.setRenewPostageAmt(0.0);
            if (depositDataMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                double period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodYy());
                double periodMm = 0.0;
                if (period >= 1) {
                    period = period * 12.0;
                    periodMm = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                    period = period + periodMm;
                } else {
                    period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                }

                objDepSubNoAccInfoTO.setTotalInstallments(new Double(period));
                if (objDepSubNoAccInfoTO.getDepositPeriodWk() != null && objDepSubNoAccInfoTO.getDepositPeriodWk() > 0) {
                    objDepSubNoAccInfoTO.setTotalInstallments(objDepSubNoAccInfoTO.getDepositPeriodWk());
                }
                // Added by nithya on 13/09/2017
                int dep_freq = 0;
                HashMap chkMap = new HashMap();
                chkMap.put("PID", depositDataMap.get("PROD_ID"));
                List chklist = ClientUtil.executeQuery("getDailyDepositFrequency", chkMap);
                if (chklist != null && chklist.size() > 0) {
                    HashMap sing = (HashMap) chklist.get(0);
                    if (sing != null && sing.containsKey("DEPOSIT_FREQ")) {
                        dep_freq = CommonUtil.convertObjToInt(sing.get("DEPOSIT_FREQ"));
                        if (dep_freq == 365) {
                            objDepSubNoAccInfoTO.setTotalInstallments(objDepSubNoAccInfoTO.getDepositPeriodYy());
                        }
                    }
                }
            }
            if (depositDataMap.get("BEHAVES_LIKE").equals("DAILY")) {
                double yearPeriod = objDepSubNoAccInfoTO.getDepositPeriodYy();
                double monthPeriod = objDepSubNoAccInfoTO.getDepositPeriodMm();
                if (yearPeriod > 1) {
                    yearPeriod = yearPeriod * 12;
                    yearPeriod = yearPeriod + monthPeriod;
                    objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(new Double(yearPeriod)));
                    objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(new Double(0.0)));
                }
            }
            depSubNoTOMap.put("0", objDepSubNoAccInfoTO);
            objDepSubNoAccInfoTO = null;
        } catch (Exception e) {
            parseException.logException(e);
        }
        return depSubNoTOMap;
    }

    
    
    
    public AccInfoTO setAccInfoData(HashMap depositDataMap) {
        AccInfoTO objAccInfoTO = new AccInfoTO();
        String remarks = "";
        try {
            
            if(depositDataMap.containsKey("REMARKS") && depositDataMap.get("REMARKS") !=  null){
               remarks = CommonUtil.convertObjToStr(depositDataMap.get("REMARKS"));
            }
            objAccInfoTO.setAuthorizedSignatory("N");
            objAccInfoTO.setCommAddress("HOME");
            objAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objAccInfoTO.setCustId(CommonUtil.convertObjToStr(depositDataMap.get("CUST_ID")));
            objAccInfoTO.setDepositNo(null);
            objAccInfoTO.setFifteenhDeclare("N");
            objAccInfoTO.setNomineeDetails("N");
            objAccInfoTO.setOpeningMode("Normal");
            objAccInfoTO.setRenewalFromDeposit("");
            objAccInfoTO.setTransOut("N");
            objAccInfoTO.setDeathClaim("N");
            objAccInfoTO.setAutoRenewal("N");
            objAccInfoTO.setRenewWithInt("N");
            objAccInfoTO.setMatAlertRep("N");
            objAccInfoTO.setStandingInstruct("N");
            objAccInfoTO.setPanNumber("");
            objAccInfoTO.setPoa("N");
            objAccInfoTO.setAgentId(CommonUtil.convertObjToStr(getCbmDepositAgentId().getKeyForSelected()));
            objAccInfoTO.setProdId(CommonUtil.convertObjToStr(depositDataMap.get("PROD_ID")));
            objAccInfoTO.setRemarks("FROM_MOBILE_APP-"+remarks);
            objAccInfoTO.setSettlementMode("02");// To be Clarified
            objAccInfoTO.setConstitution("INDIVIDUAL");
            objAccInfoTO.setAddressType("HOME");
            objAccInfoTO.setCategory("GENERAL_CATEGORY");
            objAccInfoTO.setCustType("");
            objAccInfoTO.setMdsGroup("");
            objAccInfoTO.setMdsRemarks("");
            objAccInfoTO.setDepositGroup("");
            objAccInfoTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setCreatedDt(curDate);
            objAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt(curDate);
            objAccInfoTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objAccInfoTO.setPrintingNo(CommonUtil.convertObjToInt(0));
            objAccInfoTO.setReferenceNo("");
            objAccInfoTO.setMember("N");
            objAccInfoTO.setTaxDeductions("N");
            objAccInfoTO.setAccZeroBalYN("N");
            objAccInfoTO.setIntroducer(CommonUtil.convertObjToStr(getCbmDepositAgentId().getKeyForSelected()));
            objAccInfoTO.setNomineeDetails("Y");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objAccInfoTO;
    }
    
    
    
    public HashMap getCustCreationResultMap() {
        return custCreationResultMap;
    }

    public void setCustCreationResultMap(HashMap custCreationResultMap) {
        this.custCreationResultMap = custCreationResultMap;
    }

    public ComboBoxModel getCbmShareAgentId() {
        return cbmShareAgentId;
    }

    public void setCbmShareAgentId(ComboBoxModel cbmShareAgentId) {
        this.cbmShareAgentId = cbmShareAgentId;
    }

    public HashMap getShareDetailsMap() {
        return shareDetailsMap;
    }

    public void setShareDetailsMap(HashMap shareDetailsMap) {
        this.shareDetailsMap = shareDetailsMap;
    }

    public HashMap getShareOpeningResultMap() {
        return shareOpeningResultMap;
    }

    public void setShareOpeningResultMap(HashMap shareOpeningResultMap) {
        this.shareOpeningResultMap = shareOpeningResultMap;
    }

    public ComboBoxModel getCbmDepositAgentId() {
        return cbmDepositAgentId;
    }

    public void setCbmDepositAgentId(ComboBoxModel cbmDepositAgentId) {
        this.cbmDepositAgentId = cbmDepositAgentId;
    }

    public HashMap getDepositOpeningResultMap() {
        return depositOpeningResultMap;
    }

    public void setDepositOpeningResultMap(HashMap depositOpeningResultMap) {
        this.depositOpeningResultMap = depositOpeningResultMap;
    }

    public ComboBoxModel getCbmSBAgentId() {
        return cbmSBAgentId;
    }

    public void setCbmSBAgentId(ComboBoxModel cbmSBAgentId) {
        this.cbmSBAgentId = cbmSBAgentId;
    }   

    public HashMap getSBOpeningResultMap() {
        return SBOpeningResultMap;
    }

    public void setSBOpeningResultMap(HashMap SBOpeningResultMap) {
        this.SBOpeningResultMap = SBOpeningResultMap;
    }
    
      public HashMap SBOpeningProcess(HashMap whereMap) throws Exception {
        HashMap obj = new HashMap();
        System.out.println("inside SBOpeningProcess" + whereMap);
        List allSBList = new ArrayList();
        HashMap agentDetailMap = new HashMap();
        HashMap singleSBMap ;
        HashMap singleSBDetMap ;
        String agentId = CommonUtil.convertObjToStr(getCbmSBAgentId().getKeyForSelected());
        System.out.println("agentId  :: " + agentId);
        whereMap.put("AGENT_ID",agentId);
        List agentAccountList = ClientUtil.executeQuery("getAgentAccountDetails", whereMap);
        if(agentAccountList != null && agentAccountList.size() > 0){
            agentDetailMap = (HashMap)agentAccountList.get(0);
        }
        if (whereMap.get("SB_OPENING_LIST") != null) {
            List sbCollectedList = (List) whereMap.get("SB_OPENING_LIST");
            if (sbCollectedList != null && sbCollectedList.size() > 0) {
                for (int i = 0; i < sbCollectedList.size(); i++) {
                    singleSBDetMap = new HashMap();
                    HashMap account = new HashMap();
                    singleSBMap = new HashMap();
                    HashMap sbDetailMap = (HashMap) sbCollectedList.get(i);
                    AccountTO acctTO = getAccountTO(sbDetailMap);       
                    singleSBMap.put("AccountTO", acctTO);
                    singleSBMap.put("AccountParamTO", getAccountParamTO(sbDetailMap));
                    singleSBMap.put("AuthorizedSignatoryTO",new HashMap());
                    if(sbDetailMap.containsKey("NOMINEE_NAME") && sbDetailMap.get("NOMINEE_NAME") != null && CommonUtil.convertObjToStr(sbDetailMap.get("NOMINEE_NAME")).length() > 0){
                     singleSBMap.put("AccountNomineeTO", getNomineeList(sbDetailMap));
                    }else{
                     singleSBMap.put("AccountNomineeTO", new ArrayList());
                    }
                    singleSBMap.put("AccountNomineeDeleteTO", new ArrayList());
                    singleSBMap.put("AuthorizedSignatoryInstructionTO",new HashMap());
                    singleSBMap.put("PowerAttorneyTO",new HashMap());
                    if(sbDetailMap.containsKey("MOBILE_NO") && sbDetailMap.get("MOBILE_NO") != null && CommonUtil.convertObjToStr(sbDetailMap.get("MOBILE_NO")).length() > 0){
                      singleSBMap.put("SMSSubscriptionTO",getSMSSubscriptionTO(sbDetailMap));
                    }
                    /*
                    Transaction Details Data={TRANS_TYPE=TRANSFER, TRANSACTION_PART=TRANSACTION_PART, CR_ACT_NUM=0001101000020, 
                    USER_ID=admin, OPERATIVE_AMOUNT=100.0, PROD_ID=101}
                    */
                    if(sbDetailMap.containsKey("AMOUNT") && sbDetailMap.get("AMOUNT") != null && CommonUtil.convertObjToDouble(sbDetailMap.get("AMOUNT")) > 0){
                        HashMap transMap = new HashMap();
                        transMap.put("TRANS_TYPE","TRANSFER");
                        transMap.put("TRANSACTION_PART","TRANSACTION_PART");
                        transMap.put("CR_ACT_NUM",agentDetailMap.get("DP_ACT_NUM"));
                        transMap.put("USER_ID", TrueTransactMain.USER_ID);
                        transMap.put("OPERATIVE_AMOUNT",CommonUtil.convertObjToDouble(sbDetailMap.get("AMOUNT")));
                        transMap.put("PROD_ID",CommonUtil.convertObjToStr(agentDetailMap.get("DP_PROD_ID")));
                        transMap.put("PROD_TYPE",CommonUtil.convertObjToStr(agentDetailMap.get("DP_PROD_TYPE")));
                        singleSBMap.put("Transaction Details Data",transMap);
                    }
                    singleSBMap.put("USER_ID", TrueTransactMain.USER_ID);
                    singleSBMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    singleSBDetMap.put(sbDetailMap.get("MOB_DEP_ID"),singleSBMap);
                    allSBList.add(singleSBDetMap);                   
                }
            }
        }
        obj.put("USER_ID", TrueTransactMain.USER_ID);        
        obj.put("OPEN_SB_ACT", "OPEN_SB_ACT");
        obj.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        obj.put("SB_OPENING_LIST", allSBList);
        System.out.println("map in SB Opening process : " + obj);
        try {
            HashMap where = proxy.execute(obj, map);
            System.out.println("status from backend ::" + where);
            setResult(ClientConstants.RESULT_STATUS[2]);
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }  
      
      
      
       private AccountTO getAccountTO(HashMap sbDetailMap) {
        System.out.println("sbDetailMap :: " + sbDetailMap);   
        AccountTO account = new AccountTO();
        String remarks = "";
        if(sbDetailMap.containsKey("REMARKS") && sbDetailMap.get("REMARKS") !=  null){
               remarks = CommonUtil.convertObjToStr(sbDetailMap.get("REMARKS"));
        }
        account.setCommand(CommonConstants.TOSTATUS_INSERT);
        account.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        account.setProdId(CommonUtil.convertObjToStr(sbDetailMap.get("PROD_ID")));
        account.setCustId(CommonUtil.convertObjToStr(sbDetailMap.get("CUST_ID")));
        account.setBranchCode(TrueTransactMain.BRANCH_ID);
        account.setCreateDt(curDate);
        account.setActStatusId("NEW");
        account.setActCatId("INDIVIDUAL");
        account.setOptModeId("No.1 only");
        account.setCommAddrType("HOME");
        account.setTodLimit(CommonUtil.convertObjToDouble(0.0));
        account.setGroupCodeId("NONE");
        account.setCategoryId("");
        account.setPrevActNum("");
        account.setClearBalance(CommonUtil.convertObjToDouble(0.0));
        account.setUnclearBalance(CommonUtil.convertObjToDouble(0.0));
        account.setFloatBalance(CommonUtil.convertObjToDouble(0.0));
        account.setEffectiveBalance(CommonUtil.convertObjToDouble(0.0));
        account.setAvailableBalance(CommonUtil.convertObjToDouble(0.0));
        account.setAuthorizationStatus("");
        account.setBaseCurr(LocaleConstants.DEFAULT_CURRENCY);
        account.setAcctName(CommonUtil.convertObjToStr(sbDetailMap.get("FNAME")));
        account.setRemarks("FROM_MOBILE_APP-"+remarks);
        account.setCardActNo("");
        account.setLinkingActNum("");
        account.setAtmCardLimitAmt(CommonUtil.convertObjToDouble(0.0));
        account.setOpeningAmount(CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(sbDetailMap.get("AMOUNT"))));
        account.setAgentId(CommonUtil.convertObjToStr(getCbmSBAgentId().getKeyForSelected()));
        account.setIntroducer(CommonUtil.convertObjToStr(getCbmSBAgentId().getKeyForSelected()));
        account.setPrimaryAccount("N");
        return account;
    }
   
      private AccountParamTO getAccountParamTO(HashMap sbDetailMap) {
        AccountParamTO param = new AccountParamTO();       
        param.setIntCrBal(YES);
        param.setIntDrBal(YES);
        param.setChkBook(YES);
        param.setNroStatus(YES);
        param.setMobileBanking(YES);
        param.setCustgrpLimitValidation(YES);
        param.setAtmCard(NO);
        param.setAtmCardNo("");
        param.setDrCard(NO);
        param.setDrCardNo("");
        param.setFlexi(NO);
        param.setMinBal1Flexi(CommonUtil.convertObjToDouble(0.0));
        param.setMinBal2Flexi(CommonUtil.convertObjToDouble(0.0));
        param.setReqFlexiPd(CommonUtil.convertObjToDouble(0.0));
        param.setStatFreq(CommonUtil.convertObjToDouble(0.0));
        param.setStopPayChrg(NO);
        param.setChkReturn(NO);
        param.setInopChrg(NO);
        param.setStatChrg(NO);
        param.setPassBook(YES);
        param.setActOpenChrg(CommonUtil.convertObjToDouble(0.0));
        param.setActClosingChrg(CommonUtil.convertObjToDouble(0.0));
        param.setMiscServChrg(CommonUtil.convertObjToDouble(0.0));
        param.setChkBookChrg(CommonUtil.convertObjToDouble(0.0));
        param.setFolioChrg(CommonUtil.convertObjToDouble(0.0));
        param.setExcessWithdChrg(CommonUtil.convertObjToDouble(0.0));        
        param.setNonmainChrg(NO);
        param.setMinActBal(CommonUtil.convertObjToDouble(0.0));
        param.setAbb(NO);
        param.setAbbChrg(CommonUtil.convertObjToDouble(0.0));
        param.setNpa(NO);        
        param.setHideBalance(NO);
        param.setShowBalanceTo("");        
        return param;
    }  
      
    private SMSSubscriptionTO getSMSSubscriptionTO(HashMap sbDetailMap){
        SMSSubscriptionTO objSMSSubscriptionTO = new SMSSubscriptionTO();
            objSMSSubscriptionTO.setProdType("OA");
            objSMSSubscriptionTO.setProdId(CommonUtil.convertObjToStr(sbDetailMap.get("PROD_ID")));
            objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(sbDetailMap.get("MOBILE_NO")));
            objSMSSubscriptionTO.setSubscriptionDt(curDate);
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
            objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
            objSMSSubscriptionTO.setStatusDt(curDate);
            return objSMSSubscriptionTO;
    }  
      
       
    
}
