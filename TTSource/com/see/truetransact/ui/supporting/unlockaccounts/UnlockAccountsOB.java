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
package com.see.truetransact.ui.supporting.unlockaccounts;

//import java.util.Observable;
import com.see.truetransact.ui.termloan.goldstockrelease.*;
import com.see.truetransact.ui.termloan.kcc.multiplerenewal.*;
import com.see.truetransact.ui.termloan.arbitration.*;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.see.truetransact.transferobject.termloan.KccRenewalTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.*;

/**
 * Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class UnlockAccountsOB extends CObservable {

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
    public UnlockAccountsOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            fillDropDown();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "KCCMultipleRenewalJNDI");
            map.put(CommonConstants.HOME, "termloan.kcc.multiplerenewal.KCCMultipleRenewalHome");
            map.put(CommonConstants.REMOTE, "termloan.kcc.multiplerenewal.KCCMultipleRenewal");

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
    private void fillDropDown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getProductsForKCCRenewal");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        cbmProdId = new ComboBoxModel(key, value);
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

    public ArrayList populateLockedAccountData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();
        List chargeList = new ArrayList();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        if (whereMap.containsKey("ACCT_LIST")) {
            chargeList = (List) whereMap.get("ACCT_LIST");
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
                //setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();
//            if (isMultiSelect) {
//                _heading.add("Select");
//            }
//            sizeList = new ArrayList();
//            if (isMultiSelect) {
//                sizeList.add(new Integer(6));
//            }
/*
{Acct No=0001259000057, Close Dt=2021-02-22 00:00:00.0, Customer ID=C010064482, 
Open Dt=2020-02-22 00:00:00.0, Stock= Net Weight : 95  
Gross Weight : 101  Stock Details :SARIMALA-1,KAICHAIN-1,THADAVALA-1, Name=Dr BENIL P  }
*/
            String head = "";
            _heading.add("AcctNo");
            _heading.add("Locked By");
            _heading.add("Screen Name");
            _heading.add("Status");
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
            System.out.println("maphere :: " + map);
            //newList.add(new Boolean(false));
            /*
            {Acct No=0001259000057, Close Dt=2021-02-22 00:00:00.0, Customer ID=C010064482, 
Open Dt=2020-02-22 00:00:00.0, Stock= Net Weight : 95  
Gross Weight : 101  Stock Details :SARIMALA-1,KAICHAIN-1,THADAVALA-1, Name=Dr BENIL P  }

            String head = "";
            _heading.add("AcctNo");
            _heading.add("CustomerId");
            _heading.add("Name");
            _heading.add("Open Dt");
            _heading.add("Close Dt");
            _heading.add("Gross Wt");
            _heading.add("Net Wt");
            _heading.add("Stock");
            */
           /* maphere :: {Acct No=0001259000057, Close Dt=2021-02-22 00:00:00.0, 
            Customer ID=C010064482, Open Dt=2020-02-22 00:00:00.0, 
            Stock= Net Weight : 95   Gross Weight : 101  Stock Details :SARIMALA-1,KAICHAIN-1,THADAVALA-1, Name=Dr BENIL P  }
*/
            newList.add(CommonUtil.convertObjToStr(map.get("RECORD_KEY")));
            newList.add(CommonUtil.convertObjToStr(map.get("LOCKED_BY")));
            newList.add(CommonUtil.convertObjToStr(map.get("SCREEN_NAME")));
            newList.add("Locked");
            data.add(newList);
        }
        System.out.println("data here :: " + data);
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableRiskFundModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableRiskFundModel = (TableModel) tblData.getModel();
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
            _heading.add("ACCT_NUM");
            _heading.add("BORROW_NO");
            _heading.add("FROM_DT");
            _heading.add("TO_DT");
            _heading.add("LIMIT");
            _heading.add("AVAILABLE_BALANCE");
            _heading.add("SURETY_AMOUNT");
            _heading.add("RENEW_FROM_DT");
            _heading.add("RENEW_TO_DT");
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
            newList.add(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("BORROW_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("FROM_DT")));
            newList.add(CommonUtil.convertObjToStr(map.get("TO_DT")));
            newList.add(CommonUtil.convertObjToStr(map.get("LIMIT")));
            newList.add(CommonUtil.convertObjToStr(map.get("AVAILABLE_BALANCE")));
            newList.add(CommonUtil.convertObjToStr(map.get("SURETY_AMOUNT")));
            if (whereMap.containsKey("NO_OF_YERAS")) {
                renewFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("TO_DT")));
                newList.add(DateUtil.getStringDate(renewFromDt));
                toDt = calculateMaturityDate(renewFromDt, CommonUtil.convertObjToInt(whereMap.get("NO_OF_YERAS")));
                newList.add(DateUtil.getStringDate(toDt));
            } else {
                renewFromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("RENEW_FROM_DT")));
                newList.add(DateUtil.getStringDate(renewFromDt));
                toDt = calculateMaturityDate(renewFromDt, CommonUtil.convertObjToInt(whereMap.get("RENEW_NO_OF_YERAS")));
                newList.add(DateUtil.getStringDate(toDt));
            }
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
    
    
     public ArrayList populateMDSData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        String editFlag = "N";
        HashMap tempMap = new HashMap();
        List chargeList = new ArrayList();

        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        } else {
            whereMap = new HashMap();
        }

        if (whereMap.containsKey("MDS_LIST")) {
            chargeList = (List) whereMap.get("MDS_LIST");
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
                //setTblModel(tblData, null, _heading);
            }
            return null;
        }

        if (_heading == null) {
            _heading = new ArrayList();
            _heading.add("Chittal No");
            _heading.add("Scheme");
            _heading.add("CustomerId");
            _heading.add("Name");
            _heading.add("Stock");
            _heading.add("Status");
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
            System.out.println("maphere :: " + map);         
            newList.add(CommonUtil.convertObjToStr(map.get("Chittal")));
            newList.add(CommonUtil.convertObjToStr(map.get("Scheme")));
            newList.add(CommonUtil.convertObjToStr(map.get("Customer ID")));
            newList.add(CommonUtil.convertObjToStr(map.get("Name")));
            newList.add(CommonUtil.convertObjToStr(map.get("Stock")));
            newList.add(CommonUtil.convertObjToStr("Not Released"));
            data.add(newList);
        }
        System.out.println("data here :: " + data);
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
        tblData.revalidate();
        if (tblData.getModel() instanceof TableSorter) {
            _tableRiskFundModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableRiskFundModel = (TableModel) tblData.getModel();
        }
        tempMap.clear();
        tempMap = null;
        return _heading;
    }

    
    
    
}