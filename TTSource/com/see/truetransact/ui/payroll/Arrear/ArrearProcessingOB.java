/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ArrearProcessingOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */
package com.see.truetransact.ui.payroll.Arrear;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.payroll.arrear.ArrearTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import java.util.Iterator;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

/**
 *
 * @author  Sreekrishnan R
 */
public class ArrearProcessingOB extends CObservable {

    final ArrayList EmployeeTableTitle = new ArrayList();
    final ArrayList processTableTitle = new ArrayList();
    private static SqlMap sqlMap = null;
    private int dataSize;
    private Iterator processLstIterator;
    HashMap proxyReturnMap = null;
    private CTable _tblData;
    private TableModel _tableModel;

    public java.util.HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }

    public void setProxyReturnMap(java.util.HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
    private HashMap dataHash;
    private Date currDt = null; //trans details
    private CTable _ArrearTblData;
    private ArrayList data;
    private ArrayList _heading;
    private final static Logger log = Logger.getLogger(ArrearProcessingOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static ArrearProcessingOB objCashierApprovalOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key, value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tblReciept;
    private EnhancedTableModel tblProcessData;
    private EnhancedTableModel clearTable;
    public ArrayList employeeData = new ArrayList();
    private String arrearType = "";
    private Date fromDate = null;
    private String debitAccount = "";
    private String debitProdType = "";
    private String debitProdID = "";
    private boolean pensionFund = false;
    private boolean providentFund = false;

    public boolean isPensionFund() {
        return pensionFund;
    }

    public void setPensionFund(boolean pensionFund) {
        this.pensionFund = pensionFund;
    }

    public boolean isProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(boolean providentFund) {
        this.providentFund = providentFund;
    }

    public String getDebitProdID() {
        return debitProdID;
    }

    public void setDebitProdID(String debitProdID) {
        this.debitProdID = debitProdID;
    }
    
    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getDebitProdType() {
        return debitProdType;
    }

    public void setDebitProdType(String debitProdType) {
        this.debitProdType = debitProdType;
    }

    public EnhancedTableModel getClearTable() {
        return clearTable;
    }

    public void setClearTable(EnhancedTableModel clearTable) {
        this.clearTable = clearTable;
    }

    public double getDApercentage() {
        return DApercentage;
    }

    public void setDApercentage(double DApercentage) {
        this.DApercentage = DApercentage;
    }

    public Date getToDate() {
        return ToDate;
    }

    public void setToDate(Date ToDate) {
        this.ToDate = ToDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    Date ToDate = null;
    double DApercentage = 0;

    public String getArrearType() {
        return arrearType;
    }

    public void setArrearType(String arrearType) {
        this.arrearType = arrearType;
    }

    public ArrayList getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(ArrayList employeeData) {
        this.employeeData = employeeData;
    }

    public EnhancedTableModel getTblProcessData() {
        return tblProcessData;
    }

    public void setTblProcessData(EnhancedTableModel tblProcessData) {
        this.tblProcessData = tblProcessData;
    }

    public EnhancedTableModel getTblReciept() {
        return tblReciept;
    }

    public void setTblReciept(EnhancedTableModel tblReciept) {
        this.tblReciept = tblReciept;
    }

    /** Creates a new instance of NewBorrowingOB */
    public ArrearProcessingOB() {
        try {
            setTableTitle();
            currDt = ClientUtil.getCurrentDate(); //trans details

            proxy = ProxyFactory.createProxy();
            setOperationMap();
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in NewBorrowingOB():" + e);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objCashierApprovalOB = new ArrearProcessingOB();
        } catch (Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():" + e);
        }
    }

    private void setTableTitle() {
        EmployeeTableTitle.add("Select");
        EmployeeTableTitle.add("Employee Id");
        EmployeeTableTitle.add("Name");

        processTableTitle.add("Select");
        processTableTitle.add("PayCode");
        processTableTitle.add("PayDesc");


    }
    // Sets the HashMap required to set JNDI,Home and Remote

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "arrearProcessingJNDI");
        map.put(CommonConstants.HOME, "arrear.arrearProcessingHome");
        map.put(CommonConstants.REMOTE, "arrear.arrearProcessing");
    }

    public void populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;

        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        //_heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        //System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("Datas :" + data.size());
        System.out.println("Datas :" + data);
        ArrayList tblDatanew = new ArrayList();
        for (int i = 0; i <= data.size() - 1; i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            newList.add(false);
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            tblDatanew.add(newList);
        }
        tblReciept = new EnhancedTableModel((ArrayList) tblDatanew, EmployeeTableTitle);
        setTblReciept(tblReciept);
        setDataSize(data.size());
    }

    public void arrearTableData(HashMap mapID, CTable tblData) {
        _ArrearTblData = tblData;

        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("Datas :" + data.size());
        System.out.println("Datas :" + data);
        ArrayList tblDatanew = new ArrayList();
        if (data.size() > 0) {
            for (int i = 0; i <= data.size() - 1; i++) {
                List tmpList = (List) data.get(i);
                ArrayList newList = new ArrayList();
                for (int j = 0; j < tmpList.size(); j++) {
                    newList.add(tmpList.get(j));
                }
                tblDatanew.add(newList);
            }
        } else {
            ClientUtil.showMessageWindow("No Arrear For Processing!!!");
        }
        tblProcessData = new EnhancedTableModel((ArrayList) tblDatanew, _heading);
        setTblProcessData(tblProcessData);
        setDataSize(data.size());
    }

    public void clearTable() {
        clearTable = new EnhancedTableModel((ArrayList) null, null);
    }

    public void resetData() {
        setDApercentage(0);
        setEmployeeData(null);
        setFromDate(null);
        setToDate(null);
        setArrearType("");
        setDebitAccount("");
        setDebitProdID("");
        setDebitProdType("");
        setPensionFund(false);
    }
    
       public void processPFTableData() throws TTException {
        TTException exception = null;
        try {
            HashMap calcMap = new HashMap();
            HashMap incrMap = new HashMap();
            HashMap empSal = new HashMap();
            HashMap salaryMap = new HashMap();
            HashMap procesMap = new HashMap();
            HashMap finalMap = new HashMap();
            ArrayList processData = new ArrayList();
            ArrayList finalData = null;
            HashMap paycodeMap = new HashMap();
            double IncDA = 0.0;
            double DAper = getDApercentage();
            double newAmount = 0.0;
            double calcPercentage = 0.0;
            for (int i = 0; i < getEmployeeData().size(); i++) {
                finalData = new ArrayList();
                incrMap.put("FROM_DT", getFromDate());
                incrMap.put("TO_DT", getToDate());
                incrMap.put("EMPLOYEEID", getEmployeeData().get(i));
                incrMap.put("TRANS_DT", currDt.clone());
                List empSallst = ClientUtil.executeQuery("getDistinctSalaryMonths", incrMap);
                if (empSallst != null && empSallst.size() > 0) {
                    for (int j = 0; j < empSallst.size(); j++) {
                        empSal = new HashMap();
                        empSal = (HashMap) empSallst.get(j);
                        incrMap.put("SALARY_DATE", empSal.get("MONTH_YEAR"));
                        System.out.println("isPensionFund^$^$^$^$^"+isPensionFund());
                        if(isPensionFund()){
                            incrMap.put("PENSION_ONLY", "PENSION_ONLY");
                        }
                        List salList = ClientUtil.executeQuery("getEmployeePFSalaryDetails", incrMap);
                        if (salList != null && salList.size() > 0) {
                            for (int p = 0; p < salList.size(); p++) {
                                salaryMap = (HashMap) salList.get(p);
                                List payCodeSet = ClientUtil.executeQuery("getPayCodeSettings", salaryMap);
                                if (payCodeSet != null && payCodeSet.size() > 0) {
                                    // for (int t = 0; t < payCodeSet.size(); t++) {
                                    paycodeMap = (HashMap) payCodeSet.get(0);
                                    if (CommonUtil.convertObjToStr(paycodeMap.get("PAY_MODULE_TYPE")).equals("PF")) {
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE")));
                                        procesMap.put("OLD_AMOUNT", Math.round(arrearDetails(procesMap, CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")))));
                                        procesMap.put("OLD_PERCENTAGE", CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT")));
                                        List returnList = splitPayCalculations(paycodeMap);
                                        double calc = 0.0;
                                        if (returnList != null && returnList.size() > 0) {
                                            for (int s = 0; s < returnList.size(); s++) {
                                                incrMap.put("CALCULATIONS", returnList.get(s));
                                                List calcList = ClientUtil.executeQuery("getCalculatedamount", incrMap);
                                                if (calcList != null && calcList.size() > 0) {
                                                    calcMap = (HashMap) calcList.get(0);
                                                    calc = calc + CommonUtil.convertObjToDouble(calcMap.get("AMOUNT"));
                                                }
                                            }
                                        }
                                        if (calc > 0) {
                                            IncDA = CommonUtil.convertObjToDouble((calc * DAper) / 100);
                                            procesMap.put("NEW_PERCENTAGE", DAper);
                                            procesMap.put("NEW_AMOUNT", Math.round(checkMinMax(paycodeMap, IncDA)));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        }
                                    } 
                                    // }
                                }
                                //insertArrearTable(processData);
                            }
                        }

                    }
                }
                System.out.println("finalData##########" + finalData);
                insertArrearTable(finalData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in processDATableData():" + e);
            ClientUtil.showMessageWindow(e.getMessage());
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
            if (exception != null) {
                parseException.logException(exception, true);
            }
        }
    }

    public void processDATableData() throws TTException {
        TTException exception = null;
        try {
            HashMap calcMap = new HashMap();
            HashMap incrMap = new HashMap();
            HashMap empSal = new HashMap();
            HashMap salaryMap = new HashMap();
            HashMap procesMap = new HashMap();
            HashMap finalMap = new HashMap();
            ArrayList processData = new ArrayList();
            ArrayList finalData = null;
            HashMap paycodeMap = new HashMap();
            double IncDA = 0.0;
            double DAper = getDApercentage();
            double newAmount = 0.0;
            double calcPercentage = 0.0;
            for (int i = 0; i < getEmployeeData().size(); i++) {
                finalData = new ArrayList();
                incrMap.put("FROM_DT", getFromDate());
                incrMap.put("TO_DT", getToDate());
                incrMap.put("EMPLOYEEID", getEmployeeData().get(i));
                List empSallst = ClientUtil.executeQuery("getDistinctSalaryMonths", incrMap);
                if (empSallst != null && empSallst.size() > 0) {
                    for (int j = 0; j < empSallst.size(); j++) {
                        empSal = new HashMap();
                        empSal = (HashMap) empSallst.get(j);
                        incrMap.put("SALARY_DATE", empSal.get("MONTH_YEAR"));
                        List salList = ClientUtil.executeQuery("getEmployeeSalaryDetails", incrMap);
                        if (salList != null && salList.size() > 0) {
                            for (int p = 0; p < salList.size(); p++) {
                                salaryMap = (HashMap) salList.get(p);
                                List payCodeSet = ClientUtil.executeQuery("getPayCodeSettings", salaryMap);
                                if (payCodeSet != null && payCodeSet.size() > 0) {
                                    // for (int t = 0; t < payCodeSet.size(); t++) {
                                    paycodeMap = (HashMap) payCodeSet.get(0);
                                    if (CommonUtil.convertObjToStr(paycodeMap.get("PAY_MODULE_TYPE")).equalsIgnoreCase("DearNess Allowance")) {    
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE"))); 
                                        procesMap.put("OLD_AMOUNT", Math.round(arrearDetails(procesMap, CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")))));
                                        //procesMap.put("OLD_AMOUNT", CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")));
                                        procesMap.put("OLD_PERCENTAGE", CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT")));
                                        List returnList = splitPayCalculations(paycodeMap);
                                        double calc = 0.0;
                                        if (returnList != null && returnList.size() > 0) {
                                            for (int s = 0; s < returnList.size(); s++) {
                                                incrMap.put("CALCULATIONS", returnList.get(s));
                                                List calcList = ClientUtil.executeQuery("getCalculatedamount", incrMap);
                                                if (calcList != null && calcList.size() > 0) {
                                                    calcMap = (HashMap) calcList.get(0);
                                                    calc = calc + CommonUtil.convertObjToDouble(calcMap.get("AMOUNT"));
                                                }
                                            }
                                        }
                                        if (calc > 0) {
                                            IncDA = CommonUtil.convertObjToDouble((calc * DAper) / 100);
                                            procesMap.put("NEW_PERCENTAGE", DAper);
                                            procesMap.put("NEW_AMOUNT", Math.round(checkMinMax(paycodeMap, IncDA)));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        }
                                    } else {
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE")));
                                        procesMap.put("OLD_AMOUNT", Math.round(arrearDetails(procesMap, CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")))));
                                        List returnList = splitPayCalculations(paycodeMap);
                                        double calc = 0.0;
                                        if (returnList != null && returnList.size() > 0) {
                                            for (int s = 0; s < returnList.size(); s++) {
                                                incrMap.put("CALCULATIONS", returnList.get(s));
                                                if (incrMap.get("CALCULATIONS").equals(getPaycodeAmount("DearNess Allowance"))) {
                                                    calc = calc + IncDA; 
                                                } else {
                                                    List calcList = ClientUtil.executeQuery("getCalculatedamount", incrMap);
                                                    if (calcList != null && calcList.size() > 0) {
                                                        calcMap = (HashMap) calcList.get(0);
                                                        calc = calc + CommonUtil.convertObjToDouble(calcMap.get("AMOUNT"));
                                                    }
                                                }
                                            }
                                        }
                                        if (calc > 0) {
                                            calcPercentage = CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT"));
                                            newAmount = CommonUtil.convertObjToDouble((calc * calcPercentage) / 100);
                                            procesMap.put("NEW_AMOUNT", Math.round(checkMinMax(paycodeMap, newAmount)));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        }
                                    }
                                    // }
                                }
                                //insertArrearTable(processData);
                            }
                        }

                    }
                }
                System.out.println("finalData##########" + finalData);
                insertArrearTable(finalData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in processDATableData():" + e);
            ClientUtil.showMessageWindow(e.getMessage());
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
            if (exception != null) {
                parseException.logException(exception, true);
            }
        }
    }

    public void processBasicTableData() throws TTException {
        TTException exception = null;
        try {
            HashMap calcMap = new HashMap();
            HashMap incrMap = new HashMap();
            HashMap empSal = new HashMap();
            HashMap salaryMap = new HashMap();
            HashMap procesMap = new HashMap();
            HashMap finalMap = new HashMap();
            ArrayList processData = new ArrayList();
            ArrayList finalData = new ArrayList();
            HashMap paycodeMap = new HashMap();
            double IncDA = 0.0;
            double IncBasic = 0.0;
            double DAper = getDApercentage();
            double newAmount = 0.0;
            double calcPercentage = 0.0;
            for (int i = 0; i < getEmployeeData().size(); i++) {
                finalData = new ArrayList();
                incrMap.put("FROM_DT", getFromDate());
                incrMap.put("TO_DT", getToDate());
                incrMap.put("EMPLOYEEID", getEmployeeData().get(i));
                List empSallst = ClientUtil.executeQuery("getDistinctSalaryMonths", incrMap);
                if (empSallst != null && empSallst.size() > 0) {
                    for (int j = 0; j < empSallst.size(); j++) {
                        empSal = new HashMap();
                        empSal = (HashMap) empSallst.get(j);
                        incrMap.put("SALARY_DATE", empSal.get("MONTH_YEAR"));
                        List salList = ClientUtil.executeQuery("getEmployeeSalaryDetails", incrMap);
                        if (salList != null && salList.size() > 0) {
                            for (int p = 0; p < salList.size(); p++) {
                                salaryMap = (HashMap) salList.get(p);
                                List payCodeSet = ClientUtil.executeQuery("getPayCodeSettings", salaryMap);
                                if (payCodeSet != null && payCodeSet.size() > 0) {
                                    // for (int t = 0; t < payCodeSet.size(); t++) {
                                    paycodeMap = (HashMap) payCodeSet.get(0);
                                    if (CommonUtil.convertObjToStr(paycodeMap.get("PAY_MODULE_TYPE")).equalsIgnoreCase("BASICPAY")) {
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE")));
                                        procesMap.put("OLD_AMOUNT", CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")));
                                        if ((IncBasic = incrementDetails(procesMap, IncBasic)) > 0) {
                                            procesMap.put("NEW_AMOUNT", Math.round(IncBasic));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        } else {
                                            throw new TTException("Increment not alloted for arrear from date!!!");
                                        }
                                    } else if (CommonUtil.convertObjToStr(paycodeMap.get("PAY_MODULE_TYPE")).equalsIgnoreCase("DearNess Allowance")) {
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE")));
                                        procesMap.put("OLD_AMOUNT", Math.round(arrearDetails(procesMap, CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")))));
                                        procesMap.put("OLD_PERCENTAGE", CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT")));
                                        List returnList = splitPayCalculations(paycodeMap);
                                        double calc = 0.0;
                                        if (returnList != null && returnList.size() > 0) {
                                            for (int s = 0; s < returnList.size(); s++) {
                                                incrMap.put("CALCULATIONS", returnList.get(s));
                                                if (incrMap.get("CALCULATIONS").equals(getPaycodeAmount("BASICPAY"))) {
                                                    calc = calc + IncBasic;
                                                } else {
                                                    List calcList = ClientUtil.executeQuery("getCalculatedamount", incrMap);
                                                    if (calcList != null && calcList.size() > 0) {
                                                        calcMap = (HashMap) calcList.get(0);
                                                        calc = calc + CommonUtil.convertObjToDouble(calcMap.get("AMOUNT"));
                                                    }
                                                }
                                            }
                                        }
                                        if (calc > 0) {
                                            //calcPercentage = CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT"));
//                                            calcPercentage = CommonUtil.convertObjToDouble(paycodeMap.get("ARREAR_PAY_PERCENT"));
                                            calcPercentage = DAper;
                                            IncDA = CommonUtil.convertObjToDouble((calc * calcPercentage) / 100);
                                            procesMap.put("NEW_AMOUNT", Math.round(checkMinMax(paycodeMap, IncDA)));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        }
                                    } else {
                                        processData = new ArrayList();
                                        procesMap = new HashMap();
                                        procesMap.put("EMPLOYEEID", incrMap.get("EMPLOYEEID"));
                                        procesMap.put("FROM_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("TO_DT", incrMap.get("FROM_DT"));
                                        procesMap.put("SALARY_MONTH", empSal.get("MONTH_YEAR"));
                                        procesMap.put("PAY_CODE", CommonUtil.convertObjToStr(paycodeMap.get("PAY_CODE")));
                                        procesMap.put("OLD_AMOUNT", CommonUtil.convertObjToDouble(salaryMap.get("AMOUNT")));
                                        List returnList = splitPayCalculations(paycodeMap);
                                        double calc = 0.0;
                                        if (returnList != null && returnList.size() > 0) {
                                            for (int s = 0; s < returnList.size(); s++) {
                                                incrMap.put("CALCULATIONS", returnList.get(s));
                                                if (incrMap.get("CALCULATIONS").equals(getPaycodeAmount("DearNess Allowance"))) {
                                                    calc = calc + IncDA;
                                                } else if (incrMap.get("CALCULATIONS").equals(getPaycodeAmount("BASICPAY"))) {
                                                    calc = calc + IncBasic;
                                                } else {
                                                    List calcList = ClientUtil.executeQuery("getCalculatedamount", incrMap);
                                                    if (calcList != null && calcList.size() > 0) {
                                                        calcMap = (HashMap) calcList.get(0);
                                                        calc = calc + CommonUtil.convertObjToDouble(calcMap.get("AMOUNT"));
                                                    }
                                                }
                                            }
                                        }
                                        if (calc > 0) {
                                            calcPercentage = CommonUtil.convertObjToDouble(paycodeMap.get("PAY_PERCENT"));
                                            newAmount = CommonUtil.convertObjToDouble((calc * calcPercentage) / 100);
                                            procesMap.put("NEW_AMOUNT", Math.round(checkMinMax(paycodeMap, newAmount)));
                                            processData.add(procesMap);
                                            finalData.add(processData);
                                        }
                                    }
                                    // }
                                }
                                //insertArrearTable(processData);
                            }
                        }

                    }
                }
                System.out.println("finalData##########" + finalData);
                insertArrearTable(finalData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in processBasicTableData():" + e);
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
            if (exception != null) {
                parseException.logException(exception, true);
            }
        }
    }

    public double incrementDetails(HashMap map, double IncBasic) {
        double increment = 0.0;
        HashMap incMap = new HashMap();
        String payDesc = "";
        incMap.put("EMPLOYEEID", map.get("EMPLOYEEID"));
        incMap.put("FROM_DT", map.get("SALARY_MONTH"));
        List inclst = ClientUtil.executeQuery("getIncrementDetails", incMap);
        if (inclst != null && inclst.size() > 0) {
            incMap = (HashMap) inclst.get(0);
            if (CommonUtil.convertObjToDouble(incMap.get("NEW_BASIC")) > CommonUtil.convertObjToDouble(incMap.get("PRESENT_BASIC"))) {
                increment = increment + CommonUtil.convertObjToDouble(incMap.get("NEW_BASIC"));
            }
            if (increment == 0) {
                increment = IncBasic;
            } else {
                increment = increment;
            }
        } else {
            increment = IncBasic;
        }
        return increment;
    }

    public double arrearDetails(HashMap map, double arrearAmt) {
        System.out.println("map%#%#%#%#%#%%#"+map);
        System.out.println("arrearAmt%#%#%#%#%#%%#"+arrearAmt);
        double newArrear = 0.0;
        HashMap incMap = new HashMap();
        String payDesc = "";
        incMap.put("EMPLOYEEID", map.get("EMPLOYEEID"));
        incMap.put("FROM_DT", map.get("SALARY_MONTH"));
        incMap.put("PAY_CODE", map.get("PAY_CODE"));
        incMap.put("TRANS_DT", currDt.clone());
        List inclst = ClientUtil.executeQuery("getPaidArrearDetails", incMap);
        if (inclst != null && inclst.size() > 0) {
            incMap = (HashMap) inclst.get(0);
            if (CommonUtil.convertObjToDouble(incMap.get("AMOUNT")) > 0) {
                newArrear = arrearAmt + CommonUtil.convertObjToDouble(incMap.get("AMOUNT"));
                System.out.println("newArrear#$#$11^"+newArrear);
            }
            if (newArrear == 0) {
                newArrear = arrearAmt;
            } else {
                newArrear = newArrear;
            }
        } else {
            newArrear = arrearAmt;
        }
        System.out.println("newArrear#$#$22^"+newArrear);
        return newArrear;
    }    
    
    public void insertArrearTable(ArrayList data) {
        HashMap dataMap = new HashMap();
        try {
            for (int i = 0; i < data.size(); i++) {
                List list = (List) data.get(i);
                dataMap = (HashMap) list.get(0);
                HashMap arrearMap = setArrearT0(dataMap);
                arrearMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                doActionPerform(arrearMap);
            }
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in insertArrearTable():" + e);
        }
    }

    public void doActionPerform(HashMap dataMap) {
        TTException exception = null;
        try {
            if (dataMap.containsKey("COMMAND")) {
                dataMap.put("FROM_DT", getFromDate());
                dataMap.put("TO_DT", getToDate());
                dataMap.put("EMPLOYEE_DATA", getEmployeeData());
                dataMap.put("TRANS_DT", currDt.clone());
                if (dataMap.containsKey("DIFFERENT_ACCOUNT")) {
                    dataMap.put("DEBIT_PRODUCT_TYPE", getDebitProdType());
                    dataMap.put("DEBIT_PRODUCT_ID", getDebitProdID());
                    dataMap.put("DEBIT_ACCOUNT_NO", getDebitAccount());
                }
                if (isPensionFund()) {
                    dataMap.put("PENSION_ONLY", "Y");
                } else {
                    dataMap.put("PENSION_ONLY", "N");
                }
                if (CommonUtil.convertObjToStr(getArrearType()).equalsIgnoreCase("DearNess Allowance")) {
                    dataMap.put("BASED_ON", "DearNess Allowance");
                } else if (getArrearType().equalsIgnoreCase("BasicPay")) {
                    dataMap.put("BASED_ON", "BASICPAY");
                } else {
                    dataMap.put("BASED_ON", "PF");
                }
                dataMap.put(CommonConstants.MODULE, "Arrear Process");
                dataMap.put(CommonConstants.SCREEN, "Arrear Process");
                dataMap.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
                dataMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                System.out.println("Befor proxy exeute map is %$%$%$%$"+dataMap);
                proxyReturnMap = proxy.execute(dataMap, map);
                setProxyReturnMap(proxyReturnMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
            System.out.println("Error in doActionPerform():" + e);
            if (e instanceof TTException) {
                exception = (TTException) e;
            }

            if (exception != null) {
                parseException.logException(exception, true);
            }
        }
    }

    public HashMap setArrearT0(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        ArrearTO arrearTO = new ArrearTO();
        arrearTO.setEmployeeId(CommonUtil.convertObjToStr(map.get("EMPLOYEEID")));
        arrearTO.setPayCode(CommonUtil.convertObjToStr(map.get("PAY_CODE")));
        arrearTO.setPayDesc(getPayDescription(arrearTO.getPayCode()));
        arrearTO.setFromDt(getProperDateFormat(map.get("SALARY_MONTH")));
        arrearTO.setToDt(getProperDateFormat(map.get("SALARY_MONTH")));
        arrearTO.setNewAmount(CommonUtil.convertObjToDouble(map.get("NEW_AMOUNT")));
        arrearTO.setOldAmount(CommonUtil.convertObjToDouble(map.get("OLD_AMOUNT")));
        arrearTO.setDifference(arrearTO.getNewAmount() - arrearTO.getOldAmount());
        arrearTO.setOldDaPer(CommonUtil.convertObjToDouble(map.get("OLD_PERCENTAGE")));
        arrearTO.setNewDaPer(CommonUtil.convertObjToDouble(map.get("NEW_PERCENTAGE")));
        if (getArrearType().equalsIgnoreCase("DearNess Allowance")) {
            arrearTO.setBaseOn("DearNess Allowance");
        } else if (getArrearType().equalsIgnoreCase("BasicPay")) {
            arrearTO.setBaseOn("BASICPAY");
        }else{
            arrearTO.setBaseOn("PF");
        }
        arrearTO.setStatus(CommonConstants.STATUS_CREATED);
        arrearTO.setTransDt((Date)currDt.clone());
        dataMap.put("ArrearTO", arrearTO);
        System.out.println("arrearTO####" + arrearTO);
        return dataMap;
    }

   public ArrearTO setOpeningArrearT0(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        ArrearTO arrearTO = new ArrearTO();
        arrearTO.setEmployeeId(CommonUtil.convertObjToStr(map.get("EMPLOYEEID")));
        arrearTO.setPayCode(CommonUtil.convertObjToStr(map.get("PAY_CODE")));
        arrearTO.setPayDesc(getPayDescription(arrearTO.getPayCode()));
        arrearTO.setFromDt(getProperDateFormat(map.get("SALARY_MONTH")));
        arrearTO.setToDt(getProperDateFormat(map.get("SALARY_MONTH")));
        arrearTO.setNewAmount(CommonUtil.convertObjToDouble(map.get("NEW_AMOUNT")));
        arrearTO.setOldAmount(CommonUtil.convertObjToDouble(map.get("OLD_AMOUNT")));
        arrearTO.setDifference(arrearTO.getNewAmount() - arrearTO.getOldAmount());
        arrearTO.setOldDaPer(CommonUtil.convertObjToDouble(map.get("OLD_PERCENTAGE")));
        arrearTO.setNewDaPer(CommonUtil.convertObjToDouble(map.get("NEW_PERCENTAGE")));
        if (getArrearType().equalsIgnoreCase("DearNess Allowance")) {
            arrearTO.setBaseOn("DearNess Allowance");
        } else if (getArrearType().equalsIgnoreCase("BasicPay")) {
            arrearTO.setBaseOn("BASICPAY");
        }else{
            arrearTO.setBaseOn("PF");
        }
        arrearTO.setStatus(CommonConstants.STATUS_CREATED);
        arrearTO.setTransDt((Date)currDt.clone());
        dataMap.put("ArrearTO", arrearTO);
        System.out.println("arrearTO####" + arrearTO);
        return arrearTO;
    }
        
    public String getPayDescription(String PayCode) {
        HashMap custMap = new HashMap();
        String payDesc = "";
        custMap.put("PAY_CODE", PayCode);
        List lst = ClientUtil.executeQuery("getPayDescription", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            payDesc = CommonUtil.convertObjToStr(custMap.get("PAY_DESCRI"));
        }
        return payDesc;
    }
    
    public String getPayCodeArrear(String arrearType) {
        HashMap custMap = new HashMap();
        String payDesc = "";
        custMap.put("ARREAR_TYPE", arrearType);
        List lst = ClientUtil.executeQuery("getPayCodeForArrear", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            payDesc = CommonUtil.convertObjToStr(custMap.get("PAY_CODE"));
        }
        return payDesc;
    }

    public List splitPayCalculations(HashMap map) {
        String s = "";
        String[] temp;
        List list = new ArrayList();
        HashMap returnMap = new HashMap();
        MultiMap mhm = new MultiHashMap();
        String delimiter;
        delimiter = "\\+";
        if (!map.get("PAY_MODULE_TYPE").equals("") && map.get("PAY_MODULE_TYPE") != null) {
            HashMap dataMap = new HashMap();
            dataMap.put("PAY_MODULE_TYPE", map.get("PAY_MODULE_TYPE"));
            List empSallst = ClientUtil.executeQuery("getPaycodeCalcOn", dataMap);
            if (empSallst != null && empSallst.size() > 0) {
                map = (HashMap) empSallst.get(0);
                s = CommonUtil.convertObjToStr(map.get("PAY_CALC_ON"));
            }
            temp = s.split(delimiter);
            for (int i = 0; i < temp.length; i++) {
                list.add(temp[i]);
                mhm.put("PAY_CODE", temp[i]);
                System.out.println(list);
            }
        }
        return list;
    }

    public double checkMinMax(HashMap map, double amount) {
        double checkAmount = 0.0;
        if (amount < CommonUtil.convertObjToDouble(map.get("PAY_MIN_AMT"))) {
            checkAmount = CommonUtil.convertObjToDouble(map.get("PAY_MIN_AMT"));
        } else if (amount > CommonUtil.convertObjToDouble(map.get("PAY_MAX_AMT"))) {
            checkAmount = CommonUtil.convertObjToDouble(map.get("PAY_MAX_AMT"));
        } else {
            checkAmount = amount;
        }
        return checkAmount;
    }

    public String getPaycodeAmount(String payCode) {
        HashMap payCodeMap = new HashMap();
        payCodeMap.put("PAY_MODULE_TYPE", payCode);
        List payCodelst = ClientUtil.executeQuery("getPayCodeFromModuleType", payCodeMap);
        if (payCodelst != null && payCodelst.size() > 0) {
            payCodeMap = (HashMap) payCodelst.get(0);
        }
        System.out.println("in get paycode amount method" + payCodeMap);
        return CommonUtil.convertObjToStr(payCodeMap.get("PAY_CODE"));
    }

    public void populatePayCodeData(HashMap mapID, CTable tblData) {
        _tblData = tblData;

        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        ArrayList tblDatanew = new ArrayList();
        for (int i = 0; i <= data.size() - 1; i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            newList.add(false);
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            tblDatanew.add(newList);
        }
        setDataSize(data.size());
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    public static ArrearProcessingOB getInstance() throws Exception {
        return objCashierApprovalOB;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
    }

    public void populateData(HashMap whereMap) {
    }

    public void setResult(int result) {
        _result = result;
        setChanged();
    }

    public int getResult() {
        return _result;
    }
}
