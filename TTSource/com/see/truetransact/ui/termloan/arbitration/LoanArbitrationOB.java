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
package com.see.truetransact.ui.termloan.arbitration;

//import java.util.Observable;
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
import com.see.truetransact.ui.TrueTransactMain;
import java.text.SimpleDateFormat;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class LoanArbitrationOB extends CObservable {

    private boolean _isAvailable = true;
    private TableModel _tableModel;
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
    private Date currDt = null;
    private Date reportingDate = null; // Added by nithya on 05-03-2016 for 0003914
    String ARC_PROCESS = "1"; 
    String ARC_post = "0";
    public String getTxtArbRate() {
        return txtArbRate;
    }

    public void setTxtArbRate(String txtArbRate) {
        this.txtArbRate = txtArbRate;
    }
    
    // Added by nithya on 05-03-2016 for 0003914

    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }
 
    // End

    /**
     * Creates a new instance of AuthorizeOB
     */
    public LoanArbitrationOB() {
        try {
            proxy = ProxyFactory.createProxy();

            map = new HashMap();
            map.put(CommonConstants.JNDI, "TermLoanArbitrationJNDI");//TermLoanArbitrationJNDI
            map.put(CommonConstants.HOME, "termloan.arbitration.TermLoanArbitrationHome");
            map.put(CommonConstants.REMOTE, "termloan.arbitration.TermLoanArbitration");


        } catch (Exception e) {
            parseException.logException(e, true);
        }
         currDt = ClientUtil.getCurrentDate();
    }

    public void fillDropDown(String productType) {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap lookUpHash = new HashMap();
        lookUpHash.put("PROD_ID", productType);
        List keyValue = null;
        setProdType(productType);
        if (productType.equals("MDS")) {
            keyValue = ClientUtil.executeQuery("getProductsForMDSNoticeARC", lookUpHash);
        }else if (productType.equals("ROOMS")) {
            keyValue = ClientUtil.executeQuery("getBuildingDerailsForARC", lookUpHash);
        } else {
            keyValue = ClientUtil.executeQuery("getProductsForLoanNotice", lookUpHash);
        }
        key.add("");
        value.add("");
        LinkedHashMap prodMap = new LinkedHashMap();
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                prodMap = (LinkedHashMap) keyValue.get(i);
                key.add(prodMap.get("PROD_ID"));
                value.add(prodMap.get("PROD_DESC"));
            }
        }
        cbmProdId = new ComboBoxModel(key, value);
        key = null;
        value = null;
        lookUpHash.clear();
        lookUpHash = null;
        keyValue.clear();
        keyValue = null;
        prodMap.clear();
        prodMap = null;
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
    public HashMap insertCharges(HashMap whereMap) {
        HashMap obj = new HashMap();
        if (prodType.equals("MDS")) {
            obj.put("PROD_TYPE", "MDS");
        }
        obj.put("ARC_STATUS", "N"); // Processing
        obj.put("ARBITRATION", "ARBITRATION");
        if (whereMap.get("ARBITRATION_POST_LIST") != null) {
            obj.put("ARBITRATION_POST_LIST", whereMap.get("ARBITRATION_POST_LIST"));
            obj.put("ARB_DATE", getTdtAuctionDate());
            obj.put("ARB_RATE", getTxtArbRate());
            obj.put("PROD_ID", whereMap.get("PROD_ID"));
            obj.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
            obj.put("USER_ID", TrueTransactMain.USER_ID);
            obj.put("REPORTING_DATE", getReportingDate()); // Added by nithya on 05-03-2016 for 0003914     
            obj.put("ARC_ID", whereMap.get("ARC_ID"));
            obj.put("BRANCH_ID", TrueTransactMain.selBranch);
//            obj.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        }
        try {
            HashMap where = proxy.execute(obj, map);
            setResult(ClientConstants.RESULT_STATUS[1]);
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
    }
    public HashMap updateCharges(HashMap whereMap) {
        HashMap obj = new HashMap();
        int c = 0;
        try {
            if (whereMap.containsKey("ARBITRATION_EDIT_LIST")) {
                ArrayList alist = (ArrayList) whereMap.get("ARBITRATION_EDIT_LIST");
                String arcid = CommonUtil.convertObjToStr(whereMap.get("ARC_ID"));
                for (int i = 0; i < alist.size(); i++) {
                    ArrayList tempList = (ArrayList) alist.get(i);
                    if (tempList != null && tempList.size() > 1) {
                        String acno = CommonUtil.convertObjToStr(tempList.get(0));
                        String fileNo = CommonUtil.convertObjToStr(tempList.get(1));
                        HashMap Hmap = new HashMap();
                        Hmap.put("fileNo", fileNo);
                        Hmap.put("act_num", acno);
                        Hmap.put("ARC_ID", arcid);
                        ClientUtil.execute("updateFileNoForFiling", Hmap);
                        c++;
                    }
                }
                if (c > 0) {
                    HashMap where = new HashMap();
                    where.put("ARC_ID", arcid);
                    setResult(ClientConstants.RESULT_STATUS[2]);
                    return where;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
            setResult(ClientConstants.RESULT_STATUS[4]);
        }
        return null;
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

    /**
     * Retrives data and populates the CTable using TableModel
     *
     * @param mapID HashMap used to retrive data from DB
     * @param tblData CTable object used to update the table with TableModel
     * @return Returns ArrayList for populating Search Combobox
     */
        public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate=(Date)currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    public String getPenalCalc(String acct_num, String date, String prod_id, String behavesLike) {
        //ACC OPEN DATE
        HashMap singleAuthorizeMap = new HashMap();
        List aListOp = new ArrayList();
        singleAuthorizeMap.put("PROD_ID", prod_id);
        List accOpMap = ClientUtil.executeQuery("getPenalRateLoan", singleAuthorizeMap);
        String penal_rate = "";
        if (accOpMap.size() > 0 && accOpMap.get(0) != null) {
            HashMap mapop = (HashMap) accOpMap.get(0);
            if (accOpMap.get(0) != null) {
                penal_rate = mapop.get("PENAL_INT_RATE").toString();
            }
        }
        String inter = "0";
        HashMap singleAuthorizeMapOpBal = new HashMap();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        try {
            //  Date opDate1 = sdf.parse(opDate);
            //   opDate = sdf.format(opDate1);
            // Commented by nithya on 27-07-2016 for solving issue of passing curr_date as future date
//            Date date1 = sdf.parse(date);
//            date = sdf.format(date1);
            singleAuthorizeMapOpBal.put("ACCT_NUM", acct_num);
            ///System.out.println("opDate ===== "+opDate);
            singleAuthorizeMapOpBal.put("CURR_DATE", getProperDateFormat(date));
            // singleAuthorizeMapOpBal.put("OPEN_DATE",getProperDateFormat(opDate) );
            singleAuthorizeMapOpBal.put("PENAL_RATE", CommonUtil.convertObjToInt(penal_rate));
            singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if(behavesLike.equalsIgnoreCase("OD")){ // Added by nithya on 25-03-2020 for KD-1674
              aListOp = ClientUtil.executeQuery("getPenalForAdvanceFn", singleAuthorizeMapOpBal); 
            }else{
              aListOp = ClientUtil.executeQuery("getPenalForLoanFn", singleAuthorizeMapOpBal);
            }
            if (aListOp.size() > 0 && aListOp.get(0) != null) {
                HashMap mapop = (HashMap) aListOp.get(0);
                inter = CommonUtil.convertObjToStr(mapop.get("INTEREST"));
            }
        } catch (Exception e) {
            System.out.println("eee==penal==" + e);
        }
        return inter;
    }
    public String getChargeAmountMDS(String chittal) {
        //ACC OPEN DATE
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("CHITTAL_NO", chittal);
        List accOpMap = ClientUtil.executeQuery("getMDSChargeDet", singleAuthorizeMap);
        String charge = "";
        if (accOpMap.size() > 0 && accOpMap.get(0) != null) {
            HashMap mapop = (HashMap) accOpMap.get(0);
            if (accOpMap.get(0) != null) {
                charge = CommonUtil.convertObjToStr(getRoundVal(CommonUtil.convertObjToDouble(mapop.get("CHARGE"))));
            }
        }

        return charge;
    }

    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    public String getInterestCalc(String acct_num, String date, String behavesLike) {
        //ACC OPEN DATE
        HashMap singleAuthorizeMap = new HashMap();
        List aListOp = new ArrayList();
        singleAuthorizeMap.put("ACCT_NUM", acct_num);
        List accOpMap = ClientUtil.executeQuery("getAccOpdate", singleAuthorizeMap);
        String opDate = "";
        if (accOpMap.size() > 0 && accOpMap.get(0) != null) {
            HashMap mapop = (HashMap) accOpMap.get(0);
            if (accOpMap.get(0) != null) {
                opDate = mapop.get("ACCT_OPEN_DT").toString();
            }
        }
        String inter = "0";
        HashMap singleAuthorizeMapOpBal = new HashMap();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        try {
            Date opDate1 = sdf.parse(opDate);
            opDate = sdf.format(opDate1);
            // Date date1 = sdf.parse(date);
            // date = sdf.format(date1);
            singleAuthorizeMapOpBal.put("ACCT_NUM", acct_num);
            singleAuthorizeMapOpBal.put("CURR_DATE", getProperDateFormat(date));
            singleAuthorizeMapOpBal.put("OPEN_DATE", getProperDateFormat(opDate));
            singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if(behavesLike.equalsIgnoreCase("OD")){ // Added by nithya on 25-03-2020 for KD-1674
               aListOp = ClientUtil.executeQuery("getInterestForAdvanceFn", singleAuthorizeMapOpBal);
            }else{
               aListOp = ClientUtil.executeQuery("getInterestForLoanFn", singleAuthorizeMapOpBal);
            }
            if (aListOp.size() > 0 && aListOp.get(0) != null) {
                HashMap mapop = (HashMap) aListOp.get(0);
                inter = CommonUtil.convertObjToStr(mapop.get("INTEREST"));
            }
        } catch (Exception e) {
            System.out.println("eee====" + e);
        }
        return inter;
    }
    public String getRoundVal(double val)
    {
        String retVal="";
        try
        {
           Float val1=   new Float( Math.round(val)) ;
           retVal=CommonUtil.convertObjToStr(val1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return retVal;
    }
    public ArrayList populateEditData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
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

        HashMap map = new HashMap();
        Iterator iterator = null;
        if (list != null && list.size() > 0) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            ClientUtil.showMessageWindow("No Data..");
            return null;
        }


//        if (_isAvailable) {
//            map = (HashMap) list.get(0);
//            iterator = map.keySet().iterator();
//        } else {
//            if (_heading != null) {
//             //   setTblModel(tblData, null, _heading);
//            }
//            return null;
//        }

        _heading = null;
        setTblModel(tblData, null, _heading);
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
            while (iterator.hasNext()) {
                head = (String) iterator.next();
                sizeList.add(new Integer(head.length()));
                _heading.add(head);
            }
        }
        //  _heading.add("Total Due");
        //  _heading.add("ARC Fee");
        //  _heading.add("Total ARC");
        //  _heading.add("File No");
        // _heading.add("Misc Charge");
//        System.out.println("@@@$$$ sizeList : "+sizeList);

        //     seperateList(list);

        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
        for (int i = 0; i < list.size(); i++) {
            ArrayList newList = new ArrayList();
            map = (HashMap) list.get(i);
            newList.add(new Boolean(false));
            newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
            newList.add(CommonUtil.convertObjToStr(map.get("MEMBER_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("NAME")));
            newList.add(CommonUtil.convertObjToStr(map.get("PRINCIPAL_DUE")));
            newList.add(CommonUtil.convertObjToStr(map.get("INT_DUE")));
            newList.add(CommonUtil.convertObjToStr(map.get("PENAL")));
            newList.add(CommonUtil.convertObjToStr(map.get("CHARGES")));
            // newList.add(CommonUtil.convertObjToStr(map.get("TOT_DUE")));
            newList.add(CommonUtil.convertObjToStr(map.get("ARC_FEE")));
            newList.add(CommonUtil.convertObjToStr(map.get("TOT_ARC")));
            newList.add(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("ARB_DATE"))));
            newList.add(CommonUtil.convertObjToStr(map.get("FILE_NO")));
            newList.add(CommonUtil.convertObjToStr(map.get("MISC_CHARGES")));
            newList.add(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("ARC_CUST_REP_TDT")))); // Added by nithya on 05-03-2016 for 0003914
            newList.add(CommonUtil.convertObjToStr(map.get("PROD_ID"))); // nithya
            newList.add(CommonUtil.convertObjToStr(map.get("ARB_TYPE"))); // nithya
            data.add(newList);
        }
        setTblModel(tblData, data, _heading);
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

    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        String behavesLike = "";
        //Added By Suresh
        _heading = null;
        _tblData = tblData;
        HashMap tempMap = new HashMap();
        String arcFeeRoundOff = "";

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
        if(whereMap.containsKey("BEHAVES_LIKE") && whereMap.get("BEHAVES_LIKE") != null){
            behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
        }
        //System.out.println("behaveslike :: " + behavesLike);
        //System.out.println("prod type :: " + prodType);
        
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
            while (iterator.hasNext()) {
                head = (String) iterator.next();
                sizeList.add(new Integer(head.length()));
                _heading.add(head);
            }
        }
        _heading.add("Total Due");
        _heading.add("ARC Fee");
        _heading.add("Total ARC");
        _heading.add("File No");
        _heading.add("Misc Charge");
        seperateList(list);
        String cellData = "", keyData = "";
        Object obj = null;
        int cellLen = 0;
//        for (int i=0, j=list.size(), k=1; i < j; i++, k=1) {
//            map = (HashMap) list.get(i);
//            colData = new ArrayList();
//            iterator = map.values().iterator();
//            if (isMultiSelect)
//                colData.add(new Boolean(false));
//            
//            while (iterator.hasNext()) {
//                obj = iterator.next();
//                System.out.println("obj");
//                cellData = CommonUtil.convertObjToStr(obj);
//                cellLen = cellData.length();
//                if (cellLen > ((Integer) sizeList.get(k)).intValue()) {
//                    sizeList.remove(k);
//                    sizeList.add(k, new Integer(cellLen));
//                }
//                colData.add(cellData);
//                k++;
//            }
//            data.add(colData);
//        }
//        System.out.println("lst"+list);
        HashMap whereMap1 = new HashMap();
        whereMap1.put("CHARGE_TYPE", "ARC");
        List aList = ClientUtil.executeQuery("getArbChargeslab", whereMap1);
        if (aList == null && aList.size() <= 0) {
            ClientUtil.showMessageWindow("Configure Arbitration Charge details.. ");
            // return ;
        }
        if (aList != null && aList.size() > 0) {
            HashMap arbMap = (HashMap) aList.get(0);
            arcFeeRoundOff = CommonUtil.convertObjToStr(arbMap.get("ARC_FEE_ROUNDOFF"));
        }
        if (prodType.equals("MDS")) {
            for (int i = 0; i < list.size(); i++) {
                ArrayList newList = new ArrayList();
                double amt = 0;
                double fee = 0;
                map = (HashMap) list.get(i);
                newList.add(new Boolean(false));
                newList.add(CommonUtil.convertObjToStr(map.get("CHITTAL_NO")));
                newList.add(CommonUtil.convertObjToStr(map.get("MEM_NO")));
                newList.add(CommonUtil.convertObjToStr(map.get("MEMBER_NAME")));
                newList.add(CommonUtil.convertObjToStr(map.get("NO_OF_MONTHS_DUE")));
                newList.add(CommonUtil.convertObjToStr(map.get("DUE_AMT")));
                newList.add(CommonUtil.convertObjToStr(map.get("PENAL_AMT")));
                newList.add(getChargeAmountMDS(CommonUtil.convertObjToStr(map.get("CHITTAL_NO"))));
                amt = CommonUtil.convertObjToDouble(map.get("DUE_AMT")) + CommonUtil.convertObjToDouble(map.get("PENAL_AMT"))
                        + CommonUtil.convertObjToDouble(getChargeAmountMDS(CommonUtil.convertObjToStr(map.get("CHITTAL_NO"))));
                newList.add(amt);
                if (aList != null && aList.size() > 0) {
                    for (int j = 0; j < aList.size(); j++) {
                        whereMap1 = (HashMap) aList.get(j);
                        double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                        double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));

                        if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                            double CHARGE_RATE = CommonUtil.convertObjToDouble(whereMap1.get("CHARGE_RATE"));
                            double MIN_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MIN_CHARGE_AMOUNT"));
                            double MAX_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MAX_CHARGE_AMOUNT"));
                            fee = (amt * CHARGE_RATE) / 100;
                            if (fee <= MIN_CHARGE_AMOUNT) {
                                fee = MIN_CHARGE_AMOUNT;
                            }
                            if (fee >= MAX_CHARGE_AMOUNT) {
                                fee = MAX_CHARGE_AMOUNT;
                            }
                        }
                    }
                } else {
                    fee = (amt * CommonUtil.convertObjToDouble(getTxtArbRate())) / 100;
                }
                if (arcFeeRoundOff.equalsIgnoreCase("Higher Value")) {
                    newList.add(Math.ceil(fee));
                } else {
                    newList.add(getRoundVal(fee));
                }
                newList.add(amt + fee);
                newList.add("");
                newList.add("");
                data.add(newList);
            }
        }else if(prodType.equals("ROOMS")){
            System.out.println("inside list for rooms :: " + list);
             for (int i = 0; i < list.size(); i++) {
                ArrayList newList = new ArrayList();
                double amt = 0;
                double fee = 0;
                map = (HashMap) list.get(i);
                newList.add(new Boolean(false));
                newList.add(CommonUtil.convertObjToStr(map.get("ROOM_NO")));
                newList.add(CommonUtil.convertObjToStr(map.get("NAME")));
                newList.add(CommonUtil.convertObjToStr(map.get("DUE_AMT")));
                newList.add(CommonUtil.convertObjToStr(map.get("PENAL_AMT")));
                amt = CommonUtil.convertObjToDouble(map.get("DUE_AMT")) + CommonUtil.convertObjToDouble(map.get("PENAL_AMT"));
                newList.add(amt);                
                if (aList != null && aList.size() > 0) {
                    for (int j = 0; j < aList.size(); j++) {
                        whereMap1 = (HashMap) aList.get(j);
                        double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                        double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));

                        if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                            double CHARGE_RATE = CommonUtil.convertObjToDouble(whereMap1.get("CHARGE_RATE"));
                            double MIN_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MIN_CHARGE_AMOUNT"));
                            double MAX_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MAX_CHARGE_AMOUNT"));
                            fee = (amt * CHARGE_RATE) / 100;
                            if (fee <= MIN_CHARGE_AMOUNT) {
                                fee = MIN_CHARGE_AMOUNT;
                            }
                            if (fee >= MAX_CHARGE_AMOUNT) {
                                fee = MAX_CHARGE_AMOUNT;
                            }
                        }
                    }
        } else {
                    fee = (amt * CommonUtil.convertObjToDouble(getTxtArbRate())) / 100;
                }
                if (arcFeeRoundOff.equalsIgnoreCase("Higher Value")) {
                    newList.add(Math.ceil(fee));
                } else {
                    newList.add(getRoundVal(fee));
                }
                newList.add(amt + fee);
                newList.add("");
                newList.add("");
                data.add(newList);
            }
            
        }else {
            for (int i = 0; i < list.size(); i++) {
                ArrayList newList = new ArrayList();
                double amt = 0;
                double fee = 0;
                double chargeAmt = 0;
                map = (HashMap) list.get(i);
                newList.add(new Boolean(false));
                newList.add(CommonUtil.convertObjToStr(map.get("ACT_NUM")));
                newList.add(CommonUtil.convertObjToStr(map.get("MEM_NO")));
                newList.add(CommonUtil.convertObjToStr(map.get("NAME")));
                newList.add(CommonUtil.convertObjToStr(map.get("PRINCIPAL_DUE")));
                newList.add(getInterestCalc(CommonUtil.convertObjToStr(map.get("ACT_NUM")), whereMap.get("OVER_DUE_DT2").toString(),behavesLike));
                //newList.add(CommonUtil.convertObjToStr(map.get("PENAL")));
                newList.add(getPenalCalc(CommonUtil.convertObjToStr(map.get("ACT_NUM")), whereMap.get("OVER_DUE_DT2").toString(), CommonUtil.convertObjToStr(whereMap.get("PROD_ID")),behavesLike));
                chargeAmt = CommonUtil.convertObjToDouble(map.get("CHARGES"));
                // int chrgeInt=Math.round(chargeAmt);
                newList.add(CommonUtil.convertObjToStr(getRoundVal(chargeAmt)));
                amt = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_DUE"))
                        + CommonUtil.convertObjToDouble(getInterestCalc(CommonUtil.convertObjToStr(map.get("ACT_NUM")), whereMap.get("OVER_DUE_DT2").toString(),behavesLike))
                        + CommonUtil.convertObjToDouble(getPenalCalc(CommonUtil.convertObjToStr(map.get("ACT_NUM")), whereMap.get("OVER_DUE_DT2").toString(), CommonUtil.convertObjToStr(whereMap.get("PROD_ID")),behavesLike))
                        + CommonUtil.convertObjToDouble(getRoundVal(chargeAmt));
                newList.add(amt);
                if (aList != null && aList.size() > 0) {
                    for (int j = 0; j < aList.size(); j++) {
                        whereMap1 = (HashMap) aList.get(j);
                        double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                        double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));

                        if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                            double CHARGE_RATE = CommonUtil.convertObjToDouble(whereMap1.get("CHARGE_RATE"));
                            double MIN_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MIN_CHARGE_AMOUNT"));
                            double MAX_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MAX_CHARGE_AMOUNT"));
                            fee = (amt * CHARGE_RATE) / 100;
                            if (fee <= MIN_CHARGE_AMOUNT) {
                                fee = MIN_CHARGE_AMOUNT;
                            }
                            if (fee >= MAX_CHARGE_AMOUNT) {
                                fee = MAX_CHARGE_AMOUNT;
                            }
                        }
                    }
                } else {
                    fee = (amt * CommonUtil.convertObjToDouble(getTxtArbRate())) / 100;
                }
                if (arcFeeRoundOff.equalsIgnoreCase("Higher Value")) {
                    newList.add(new Float(Math.ceil(fee)));
                } else {
                    newList.add(getRoundVal(fee));
                }
                newList.add(amt + fee);
                newList.add("");
                newList.add("");
                data.add(newList);
            }
        }
        setTblModel(tblData, data, _heading);
        TableColumn col = null;
//        for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
//            k = ((Integer)sizeList.get(i)).intValue();
//            if (k > 50) {
//                k = 400;
//            } else {
//                k *= 8;
//            }
//            col = tblData.getColumn(_heading.get(i));
//            col.setPreferredWidth(k);
//            col.setMinWidth(k);
////            System.out.println("@@@$$$ col width : "+k);
//            //col.setMaxWidth(k);
//        }
        //Added By Suresh
        if (prodType.equals("MDS")) {
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
        }
        tblData.revalidate();

        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        populateCharges(tempMap);
        tempMap.clear();
        tempMap = null;

        return _heading;
    }

    private void populateCharges(HashMap whereMap) {
//        if (whereMap.containsKey("PROD_ID")) {
//            List lst=null;
//            if(prodType.equals("MDS")){
//                lst = ClientUtil.executeQuery("getChargesForMDSNotices", whereMap);
//            }else{
//                lst = ClientUtil.executeQuery("getChargesForLoanNotices", whereMap);
//            }
////            HashMap resultMap = null;
//            HashMap resultMap = new HashMap();
//            if (lst!=null && lst.size()>0) {
//                resultMap = (HashMap) lst.get(0);
//                txtNoticeCharge = CommonUtil.convertObjToStr(resultMap.get("NOTICE_CHARGE_AMT"));
//                txtPostageCharge = CommonUtil.convertObjToStr(resultMap.get("POSTAGE_AMT"));
//            }
//            resultMap.clear();
//            resultMap = null;
//            lst.clear();
//            lst = null;
//        }
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

    public void populateGuarantorTable(String actNum, CTable tblData) {
        if (_heading != null) {
            if (guarantorMap != null && guarantorMap.containsKey(actNum)) {
                guarantorList = (ArrayList) guarantorMap.get(actNum);
                setTblModel(tblData, guarantorList, _heading);
                TableColumn col = null;
                for (int i = 0, j = sizeList.size(), k = 0; i < j; i++) {
                    k = ((Integer) sizeList.get(i)).intValue();
                    if (k > 50) {
                        k = 400;
                    } else {
                        k *= 8;
                    }
                    col = tblData.getColumn(_heading.get(i));
                    col.setPreferredWidth(k);
                    col.setMinWidth(k);
                }
                //Added By Suresh
                if (prodType.equals("MDS")) {
                    _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
                    _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
                }
                tblData.revalidate();
            } else {
                setTblModel(tblData, null, _heading);
            }
        }
    }
    private void seperateList(List list) {
        ArrayList tempList = new ArrayList();
        Map map = new HashMap();
        Iterator iterator = null;
        String cellData;
        for (int i = 0; i < list.size(); i++) {
            map = (HashMap) list.get(i);
            if (String.valueOf(map.get("CUST_TYPE")).equals("GUARANTOR")) {
                iterator = map.values().iterator();
                guarantorList = new ArrayList();
//                }
                guarantorList.add(new Boolean(false));
                if (guarantorMap == null) {
                    guarantorMap = new HashMap();
                }
                while (iterator.hasNext()) {
                    cellData = CommonUtil.convertObjToStr(iterator.next());
                    guarantorList.add(cellData);
                }
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
            }
        }
    }

    public void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        //  tableSorter.addMouseListenerToHeaderInTable(tbl);
        // Modified mColIndex == 13 by nithya on 05-03-2016 for 0003914 
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (prodType.equals("ROOMS")) {
                    if (mColIndex == 0 || mColIndex == 3 || mColIndex == 4 || mColIndex == 6 || mColIndex == 8 || mColIndex == 9) {                        
                        return true;
                    } else {
                        return false;
                    }
                }else{
                if (mColIndex == 0 || mColIndex == 4 || mColIndex == 5 || mColIndex == 6 || mColIndex == 7 || mColIndex == 9 || mColIndex == 11
                        || mColIndex == 12 || mColIndex == 13) {
                    //tbl.setValueAt(map, rowIndex, mColIndex);
                    return true;
                } else {
                    return false;
                    }
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

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
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
    
    
    public HashMap deleteAccountsFromARC(HashMap whereMap) {
        
        try {
            HashMap where = proxy.execute(whereMap, map);  
            setResult(ClientConstants.RESULT_STATUS[3]);
            return where;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);          
        }
        return null;
    }
    
     // added by nithya on 10-03-2016
    
    public HashMap updateLoanArbitrationAfterProcess(HashMap whereMap) {
        
        HashMap obj = new HashMap();
        if (prodType.equals("MDS")) {
            obj.put("PROD_TYPE", "MDS");
        }        
        obj.put("ARC_STATUS","Y"); // Posted        
        obj.put("ARBITRATION", "ARBITRATION");
        obj.put("ARBITRATION_UPDATE", "ARBITRATION_UPDATE");
        if (whereMap.get("ARBITRATION_POST_LIST") != null) {
            obj.put("ARBITRATION_POST_LIST", whereMap.get("ARBITRATION_POST_LIST"));            
            obj.put("PROD_ID", whereMap.get("PROD_ID"));
            obj.put("PROD_TYPE", whereMap.get("PROD_TYPE"));
            obj.put("USER_ID", TrueTransactMain.USER_ID);            
            obj.put("ARC_ID",whereMap.get("ARC_ID"));
            obj.put(CommonConstants.SELECTED_BRANCH_ID, TrueTransactMain.selBranch);
        }
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

}