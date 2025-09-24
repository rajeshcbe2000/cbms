/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryProcessOB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.SalaryProcess;

import com.lowagie.text.pdf.hyphenation.Hyphenator;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CTable;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Rishad
 */
public class SalaryProcessOB extends CObservable {

    private HashMap operationMap;
    private String batchID = "";
    private Double amount = null;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(SalaryProcessUI.class);
    private ProxyFactory proxy = null;
    private String lblTransDate;
    private TableModel tbmEmployeeDetails;
    private TableModel tbmPaySettingDetails;
    private ArrayList recordData, deleteData;
    private ArrayList recordSettingData, deleteSettingData;
    HashMap wheremap=new  HashMap();
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private boolean _isAvailable = true;
    private HashMap  payrollMap;
    private String payrollId="";
    private Date salaryDate;
    private String salaryMonth = "";
   
    public Date getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }
   

    public String getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(String payrollId) {
        this.payrollId = payrollId;
    }

    public HashMap getPayrollMap() {
        return payrollMap;
    }

    public void setPayrollMap(HashMap payrollMap) {
        this.payrollMap = payrollMap;
    }
    
    public TableModel getTbmPaySettingDetails() {
        return tbmPaySettingDetails;
    }

    public void setTbmPaySettingDetails(TableModel tbmPaySettingDetails) {
        this.tbmPaySettingDetails = tbmPaySettingDetails;
    }
    
    public TableModel getTbmEmployeeDetails() {
        return tbmEmployeeDetails;
    }

    public void setTbmEmployeeDetails(TableModel tbmEmployeeDetails) {
        this.tbmEmployeeDetails = tbmEmployeeDetails;
    }

    public String getLblTransDate() {
        return lblTransDate;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public void setLblTransDate(String lblTransDate) {
        this.lblTransDate = lblTransDate;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }
     // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    private static SalaryProcessOB salaryprocessob;
    static {
        try {
            log.info("In salaryob Declaration");
            salaryprocessob= new SalaryProcessOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static SalaryProcessOB getInstance() {
        return salaryprocessob;
    }

    /**
     * Creates a new instance of SalaryProcessOB
     */
    public SalaryProcessOB() {
        try {
            initianSetup();
        } catch (Exception e) {
        }
    }

    private void initianSetup() throws Exception {
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
 public ArrayList populateData(HashMap mapID, CTable tblData,boolean select) {
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

        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        if(select==true){
        if (_heading != null && _heading.size() > 0) {
            _heading.add(0, "Select");
        }
        }
        ArrayList arrList = new ArrayList();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                arrList = (ArrayList) data.get(i);
                if(select==true){
                arrList.add(0, new Boolean(false));}
                data.set(i, arrList);
            }
        }
        System.out.println("### Data : " + data);
        populateTable();
        whereMap = null;
        return _heading;
    }
 
  public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            setTblModel(_tblData, data, _heading);
        } else {
            _isAvailable = false;
            dataExist = false;
            
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            ClientUtil.noDataAlert();
        }
    }
   private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
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
  
  
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        proxy = ProxyFactory.createProxy();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SalaryProcessJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.SalaryProcess.SalaryProcesslHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.SalaryProcess.SalaryProcess");
    }

    //Do display the Data from the Database, in UI
   
    public void doAction(String mode) {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
//            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
//                //If actionType has got propervalue then doActionPerform, else throw error
//                doActionPerform();
//
//            }
            doActionPerform(mode);
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform(String mode) throws Exception {
        log.info("In doActionPerform()");
try{
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put(CommonConstants.MODULE, "Salary Process");
        data.put(CommonConstants.SCREEN, "Salary Process");
        data.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
        data.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        data.put("MODE", mode);
        if (mode.equals("POST")) {
            data.put("salarypost", getPayrollMap());
        } else if (mode.equals("PROCESS")) {
            data.put("SAL_MONTH_YEAR",getSalaryMonth());
            data.put("payrollId", getPayrollId());
        }
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }
     catch (Exception e) {
            log.info("Error In doAction()" + e);
             ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
}

    // to decide which action Should be performed...
    private String getCommand() {
        log.info("In getCommand()");
        String command = null;
        System.out.println("actionType in getCommand(): " + actionType);
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
           
            default:
        }
        return command;
    }

    private String getAction() {
        log.info("In getAction()");
        String action = null;
        System.out.println("actionType in getAction(): " + actionType);
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
            default:
        }
        return action;
    }

   
    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
       
    }
  
    public String getBankName() {
        String bank = "";
        List bankList = ClientUtil.executeQuery("getSelectBankTOList", null);
        HashMap result = new HashMap();
        if (bankList != null && bankList.size() > 0) {
            result = (HashMap) bankList.get(0);
            bank = CommonUtil.convertObjToStr(result.get("BANK_NAME"));
            return bank;
        }
        return bank;
    }

    public List getPayDescEarnings() throws SQLException {
        final List earningsList = ClientUtil.executeQuery("getEarningsData", null);
        return earningsList;
    }

    public List getPayDescDeductions() throws SQLException {
        final List deductionsList = ClientUtil.executeQuery("getDeductionsData", null);
        return deductionsList;
    }

    public List getEmployeeReportDetails() throws SQLException {
        final List empReportList = ClientUtil.executeQuery("getEmployeeReportDetails", null);
        return empReportList;
    }

    public List getEmpPayrollAmt(HashMap dataMap) throws SQLException {
        final List empAmtList = ClientUtil.executeQuery("getEmpPayrollAmounts", dataMap);
        return empAmtList;
    }

    public List getPayrollEarnData(HashMap dataMap) throws SQLException {
        final List payrollEarnDataList = ClientUtil.executeQuery("getPayrollEarnData", dataMap);
        return payrollEarnDataList;
    }

    public Double getContraAmt(HashMap dataMap) throws SQLException {
        double contraAmt = 0.0;
        final List contraAmtList = ClientUtil.executeQuery("getContraAmount", dataMap);
        if (contraAmtList != null && contraAmtList.size() > 0) {
            HashMap contraAmtMap = new HashMap();
            contraAmtMap = (HashMap) contraAmtList.get(0);
            contraAmt = CommonUtil.convertObjToDouble(contraAmtMap.get("AMOUNT"));
            return contraAmt;
        }
        return contraAmt;
    }

    public List getPayrollEarningsData(HashMap dataMap) throws SQLException {
        final List payrollDataList = ClientUtil.executeQuery("getPayrollEarningsData", dataMap);
        return payrollDataList;
    }

    public List getPayrollDeductionsData(HashMap dataMap) throws SQLException {
        final List payrollDataList = ClientUtil.executeQuery("getPayrollDeductionsData", dataMap);
        return payrollDataList;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }
    
}
