/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeTrainingOB.java
 *
 * Created on june , 2010, 4:30 PM Swaroop
 */
package com.see.truetransact.ui.payroll.employeeTraining;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.employeeTraining.EmployeeTrainingTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * Modified by anjuanand on 08/12/2014
 *
 */
public class EmployeeTrainingOB extends CObservable {

    private String txtEmpTrainingID = "";
    private String txtEmpID = "";
    private String destination = "";
    private String location = "";
    private String team = "";
    private String teamSize = "";
    private String trainingFrom = "";
    private String trainingTo = "";
    private String empBran = "";
    private String txtStatus = "";
    private String CreatedDt = "";
    private String empName = "";
    private String txtStatusBy = "";
    private String remarks = "";
    private ComboBoxModel cbmTrainingDest;
    private String cboTrainingDest = "";
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
    private String txtSlNo = "";
    private String subj = "";
    private final static Logger log = Logger.getLogger(EmployeeTrainingOB.class);
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
    private EmployeeTrainingOB empTrainingOB;
    EmployeeTrainingRB objEmpTrainingRB = new EmployeeTrainingRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private EmployeeTrainingTO objEmpTrainingTO;
    private Date currDt = null;
    /**
     * Creates a new instance of TDS MiantenanceOB
     */
    public EmployeeTrainingOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();

            map = new HashMap();
            map.put(CommonConstants.JNDI, "EmpTrainingJNDI");
            map.put(CommonConstants.HOME, "payroll.employeeTraining.EmployeeTrainingHome");
            map.put(CommonConstants.REMOTE, "payroll.employeeTraining.EmployeeTraining");
            setTableTile();
            tblEmpDetails = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("SL No");
        tableTitle.add("Emp Name");
        tableTitle.add("Emp ID");
        IncVal = new ArrayList();
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        log.info("Inside FillDropDown");
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("EMP_TRAINING_DESTINATION");
        param.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get("EMP_TRAINING_DESTINATION"));
        cbmTrainingDest = new ComboBoxModel(key, value);

    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    /**
     * To get data for comboboxes
     */
    public HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }

    public String getEmpBranchCode(HashMap dataMap) throws SQLException {
        List cust = ClientUtil.executeQuery("getEmpBranchCode", dataMap);
        String branchCode = "";
        HashMap resultMap = new HashMap();
        resultMap = (HashMap) cust.get(0);
        branchCode = CommonUtil.convertObjToStr(resultMap.get("BRANCH_CODE"));
        return branchCode;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {

            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final EmployeeTrainingTO objEmpTrainingTO = new EmployeeTrainingTO();
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            data.put("EmpTraining", setEmpTrainingData());
            data.put("EmpTableDetails", incParMap);
            data.put("deletedEmpTableDetails", deletedTableMap);
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            data.put("EmpTableDetails", incParMap);
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if (proxyResultMap != null && getCommand() != null && getCommand().equalsIgnoreCase("INSERT")) {
            ClientUtil.showMessageWindow("Training Id : " + CommonUtil.convertObjToStr(proxyResultMap.get("TRAINING_ID")));
        }
        setResult(getActionType());
        setResult(actionType);
    }

    private String getCommand() {
        String command = null;
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

    private String getAction() {
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
        return action;
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            objEmpTrainingTO = (EmployeeTrainingTO) ((List) data.get("EmpTrainingTO")).get(0);
            populateEmpTrainingData(objEmpTrainingTO);
            if (data.containsKey("EmpDetailsTO")) {
                incParMap = (LinkedHashMap) data.get("EmpDetailsTO");
                ArrayList addList = new ArrayList(incParMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    objEmpTrainingTO = (EmployeeTrainingTO) incParMap.get(addList.get(i));
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getSlNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
                    tblEmpDetails.addRow(incTabRow);
                }
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To populate data into the screen
     */
    public EmployeeTrainingTO setEmpTrainingData() {

        final EmployeeTrainingTO objEmpTrainingTO = new EmployeeTrainingTO();
        try {
            objEmpTrainingTO.setBranCode(getEmpBran());
            objEmpTrainingTO.setDestination(getCboTrainingDest());
            objEmpTrainingTO.setTrainingFrom(DateUtil.getDateMMDDYYYY(getTrainingFrom()));
            objEmpTrainingTO.setEmpID(getTxtEmpID());
            objEmpTrainingTO.setTrainingTo(DateUtil.getDateMMDDYYYY(getTrainingTo()));
            objEmpTrainingTO.setCommand(getCommand());
            objEmpTrainingTO.setStatus(getAction());
            objEmpTrainingTO.setStatusBy(TrueTransactMain.USER_ID);
            objEmpTrainingTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objEmpTrainingTO.setLocation(getLocation());
            objEmpTrainingTO.setTeam(getTeam());
            objEmpTrainingTO.setTeamSize(CommonUtil.convertObjToInt(getTeamSize()));
            objEmpTrainingTO.setNoOfEmp(CommonUtil.convertObjToInt(getNoOfTrainees()));
            objEmpTrainingTO.setSubj(getSubj());
            objEmpTrainingTO.setRemarks(getRemarks());
            if (getCommand().equalsIgnoreCase("INSERT")) {
                objEmpTrainingTO.setCreatedBy(TrueTransactMain.USER_ID);
                objEmpTrainingTO.setCreatedDt(currDt);
                objEmpTrainingTO.setEmpBran(getEmpBran());
            }
            if (getCommand().equalsIgnoreCase("UPDATE") || getCommand().equalsIgnoreCase("DELETE")) {
                objEmpTrainingTO.setEmpTrainingID(getTxtEmpTrainingID());
            }
        } catch (Exception e) {
            log.info("Error In setEmpTranferData()");
            e.printStackTrace();
        }
        return objEmpTrainingTO;
    }

    private void populateEmpTrainingData(EmployeeTrainingTO objEmpTrainingTO) throws Exception {
        this.setTxtEmpTrainingID(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpTrainingID()));
        this.setTxtEmpID(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
        this.setTxtStatusBy(CommonUtil.convertObjToStr(objEmpTrainingTO.getStatusBy()));
        this.setCboTrainingDest(CommonUtil.convertObjToStr(objEmpTrainingTO.getDestination()));
        this.setCreatedDt(CommonUtil.convertObjToStr(objEmpTrainingTO.getCreatedDt()));
        this.setLocation(CommonUtil.convertObjToStr(objEmpTrainingTO.getLocation()));
        this.setTeam(CommonUtil.convertObjToStr(objEmpTrainingTO.getTeam()));
        this.setTeamSize(CommonUtil.convertObjToStr(objEmpTrainingTO.getTeamSize()));
        this.setTrainingFrom(CommonUtil.convertObjToStr(objEmpTrainingTO.getTrainingFrom()));
        this.setTrainingTo(CommonUtil.convertObjToStr(objEmpTrainingTO.getTrainingTo()));
        this.setStatusBy(CommonUtil.convertObjToStr(objEmpTrainingTO.getStatusBy()));
        this.setNoOfTrainees(CommonUtil.convertObjToStr(objEmpTrainingTO.getNoOfEmp()));
        this.setSubj(CommonUtil.convertObjToStr(objEmpTrainingTO.getSubj()));
        this.setRemarks(CommonUtil.convertObjToStr(objEmpTrainingTO.getRemarks()));
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        setCboTrainingDest("");
        setTxtEmpID("");
        setTxtEmpTrainingID("");
        setTeam("");
        setTeamSize("");
        setTrainingFrom("");
        setTrainingTo("");
        setEmpName("");
        tblEmpDetails.setDataArrayList(null, tableTitle);
        setNoOfTrainees("");
        setLocation("");
        setSubj("");
        setChanged();
        setRemarks("");
        ttNotifyObservers();
    }

    public void resetEmpDetails() {
        setTxtEmpID("");
        setChanged();
        ttNotifyObservers();
    }

    public void deleteTableData(String val, int row) {
        if (deletedTableMap == null) {
            deletedTableMap = new LinkedHashMap();
        }
        EmployeeTrainingTO objEmpTrainingTO = (EmployeeTrainingTO) incParMap.get(val);
        objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_DELETED);
        deletedTableMap.put(CommonUtil.convertObjToStr(tblEmpDetails.getValueAt(row, 0)), incParMap.get(val));
        Object obj;
        obj = val;
        incParMap.remove(val);
        resetTableValues();
        try {
            populateTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(incParMap.keySet());
        ArrayList addList = new ArrayList(incParMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            EmployeeTrainingTO objEmpTrainingTO = (EmployeeTrainingTO) incParMap.get(addList.get(i));

            IncVal.add(objEmpTrainingTO);
            if (!objEmpTrainingTO.getEmpStatus().equals("DELETED")) {
                incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
                incTabRow.add(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
                incTabRow.add("");
                tblEmpDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[150];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void addToTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final EmployeeTrainingTO objEmpTrainingTO = new EmployeeTrainingTO();
            if (incParMap == null) {
                incParMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewData()) {
                    objEmpTrainingTO.setEmpCreatedBy(TrueTransactMain.USER_ID);
                    objEmpTrainingTO.setEmpStatusDt(currDt);
                    objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
                    objEmpTrainingTO.setEmpCreatedDt(currDt);
                    objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objEmpTrainingTO.setEmpStatusDt(currDt);
                    objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
                    objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objEmpTrainingTO.setEmpCreatedBy(TrueTransactMain.USER_ID);
                objEmpTrainingTO.setEmpStatusDt(currDt);
                objEmpTrainingTO.setEmpStatusBy(TrueTransactMain.USER_ID);
                objEmpTrainingTO.setEmpCreatedDt(currDt);
                objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_CREATED);
            }
            int slno = 0;
            int nums[] = new int[150];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblEmpDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblEmpDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblEmpDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }

            objEmpTrainingTO.setSlNo(slno);
            objEmpTrainingTO.setEmpName(getEmpName());
            objEmpTrainingTO.setEmpID(getTxtEmpID());
            incParMap.put(objEmpTrainingTO.getSlNo(), objEmpTrainingTO);
            String sno = String.valueOf(slno);
            updateEmpDetails(rowSel, sno, objEmpTrainingTO);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateEmpDetails(int rowSel, String sno, EmployeeTrainingTO objEmpTrainingTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for (int i = tblEmpDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblEmpDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblEmpDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getEmpName());
                IncParRow.add(getTxtEmpID());
                tblEmpDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getEmpName());
            IncParRow.add(getTxtEmpID());
            tblEmpDetails.insertRow(tblEmpDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }

    public void populateLeaveDetails(String row) {
        try {
            resetEmpDetails();
            final EmployeeTrainingTO objEmpTrainingTO = (EmployeeTrainingTO) incParMap.get(row);
            populateTableData(objEmpTrainingTO);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTableData(EmployeeTrainingTO objEmpTrainingTO) throws Exception {
        setEmpName(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpName()));
        setTxtSlNo(CommonUtil.convertObjToStr(objEmpTrainingTO.getSlNo()));
        setTxtEmpID(CommonUtil.convertObjToStr(objEmpTrainingTO.getEmpID()));
        setChanged();
        notifyObservers();
    }

    public void resetTableValues() {
        tblEmpDetails.setDataArrayList(null, tableTitle);
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

    /**
     * Getter for property txtStatusBy.
     *
     * @return Value of property txtStatusBy.
     */
    public java.lang.String getTxtStatusBy() {
        return txtStatusBy;
    }

    /**
     * Setter for property txtStatusBy.
     *
     * @param txtStatusBy New value of property txtStatusBy.
     */
    public void setTxtStatusBy(java.lang.String txtStatusBy) {
        this.txtStatusBy = txtStatusBy;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
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
     *
     * @return Value of property CreatedDt.
     */
    public java.lang.String getCreatedDt() {
        return CreatedDt;
    }

    /**
     * Setter for property CreatedDt.
     *
     * @param CreatedDt New value of property CreatedDt.
     */
    public void setCreatedDt(java.lang.String CreatedDt) {
        this.CreatedDt = CreatedDt;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property txtEmpID.
     *
     * @return Value of property txtEmpID.
     */
    public java.lang.String getTxtEmpID() {
        return txtEmpID;
    }

    /**
     * Setter for property txtEmpID.
     *
     * @param txtEmpID New value of property txtEmpID.
     */
    public void setTxtEmpID(java.lang.String txtEmpID) {
        this.txtEmpID = txtEmpID;
    }

    /**
     * Getter for property txtStatus.
     *
     * @return Value of property txtStatus.
     */
    public java.lang.String getTxtStatus() {
        return txtStatus;
    }

    /**
     * Setter for property txtStatus.
     *
     * @param txtStatus New value of property txtStatus.
     */
    public void setTxtStatus(java.lang.String txtStatus) {
        this.txtStatus = txtStatus;
    }

    /**
     * Getter for property empName.
     *
     * @return Value of property empName.
     */
    public java.lang.String getEmpName() {
        return empName;
    }

    /**
     * Setter for property empName.
     *
     * @param empName New value of property empName.
     */
    public void setEmpName(java.lang.String empName) {
        this.empName = empName;
    }

    /**
     * Getter for property txtEmpTrainingID.
     *
     * @return Value of property txtEmpTrainingID.
     */
    public java.lang.String getTxtEmpTrainingID() {
        return txtEmpTrainingID;
    }

    /**
     * Setter for property txtEmpTrainingID.
     *
     * @param txtEmpTrainingID New value of property txtEmpTrainingID.
     */
    public void setTxtEmpTrainingID(java.lang.String txtEmpTrainingID) {
        this.txtEmpTrainingID = txtEmpTrainingID;
    }

    /**
     * Getter for property destination.
     *
     * @return Value of property destination.
     */
    public java.lang.String getDestination() {
        return destination;
    }

    /**
     * Setter for property destination.
     *
     * @param destination New value of property destination.
     */
    public void setDestination(java.lang.String destination) {
        this.destination = destination;
    }

    /**
     * Getter for property location.
     *
     * @return Value of property location.
     */
    public java.lang.String getLocation() {
        return location;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    /**
     * Getter for property team.
     *
     * @return Value of property team.
     */
    public java.lang.String getTeam() {
        return team;
    }

    /**
     * Setter for property team.
     *
     * @param team New value of property team.
     */
    public void setTeam(java.lang.String team) {
        this.team = team;
    }

    /**
     * Getter for property teamSize.
     *
     * @return Value of property teamSize.
     */
    public java.lang.String getTeamSize() {
        return teamSize;
    }

    /**
     * Setter for property teamSize.
     *
     * @param teamSize New value of property teamSize.
     */
    public void setTeamSize(java.lang.String teamSize) {
        this.teamSize = teamSize;
    }

    /**
     * Getter for property trainingFrom.
     *
     * @return Value of property trainingFrom.
     */
    public java.lang.String getTrainingFrom() {
        return trainingFrom;
    }

    /**
     * Setter for property trainingFrom.
     *
     * @param trainingFrom New value of property trainingFrom.
     */
    public void setTrainingFrom(java.lang.String trainingFrom) {
        this.trainingFrom = trainingFrom;
    }

    /**
     * Getter for property trainingTo.
     *
     * @return Value of property trainingTo.
     */
    public java.lang.String getTrainingTo() {
        return trainingTo;
    }

    /**
     * Setter for property trainingTo.
     *
     * @param trainingTo New value of property trainingTo.
     */
    public void setTrainingTo(java.lang.String trainingTo) {
        this.trainingTo = trainingTo;
    }

    /**
     * Getter for property empBran.
     *
     * @return Value of property empBran.
     */
    public java.lang.String getEmpBran() {
        return empBran;
    }

    /**
     * Setter for property empBran.
     *
     * @param empBran New value of property empBran.
     */
    public void setEmpBran(java.lang.String empBran) {
        this.empBran = empBran;
    }

    /**
     * Getter for property cbmTrainingDest.
     *
     * @return Value of property cbmTrainingDest.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTrainingDest() {
        return cbmTrainingDest;
    }

    /**
     * Setter for property cbmTrainingDest.
     *
     * @param cbmTrainingDest New value of property cbmTrainingDest.
     */
    public void setCbmTrainingDest(com.see.truetransact.clientutil.ComboBoxModel cbmTrainingDest) {
        this.cbmTrainingDest = cbmTrainingDest;
    }

    /**
     * Getter for property cboTrainingDest.
     *
     * @return Value of property cboTrainingDest.
     */
    public java.lang.String getCboTrainingDest() {
        return cboTrainingDest;
    }

    /**
     * Setter for property cboTrainingDest.
     *
     * @param cboTrainingDest New value of property cboTrainingDest.
     */
    public void setCboTrainingDest(java.lang.String cboTrainingDest) {
        this.cboTrainingDest = cboTrainingDest;
    }

    /**
     * Getter for property noOfTrainees.
     *
     * @return Value of property noOfTrainees.
     */
    public java.lang.String getNoOfTrainees() {
        return noOfTrainees;
    }

    /**
     * Setter for property noOfTrainees.
     *
     * @param noOfTrainees New value of property noOfTrainees.
     */
    public void setNoOfTrainees(java.lang.String noOfTrainees) {
        this.noOfTrainees = noOfTrainees;
    }

    /**
     * Getter for property newData.
     *
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }

    /**
     * Setter for property newData.
     *
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    /**
     * Getter for property txtSlNo.
     *
     * @return Value of property txtSlNo.
     */
    public java.lang.String getTxtSlNo() {
        return txtSlNo;
    }

    /**
     * Setter for property txtSlNo.
     *
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(java.lang.String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }

    /**
     * Getter for property tblEmpDetails.
     *
     * @return Value of property tblEmpDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblEmpDetails() {
        return tblEmpDetails;
    }

    /**
     * Setter for property tblEmpDetails.
     *
     * @param tblEmpDetails New value of property tblEmpDetails.
     */
    public void setTblEmpDetails(com.see.truetransact.clientutil.EnhancedTableModel tblEmpDetails) {
        this.tblEmpDetails = tblEmpDetails;
    }

    /**
     * Getter for property subj.
     *
     * @return Value of property subj.
     */
    public java.lang.String getSubj() {
        return subj;
    }

    /**
     * Setter for property subj.
     *
     * @param subj New value of property subj.
     */
    public void setSubj(java.lang.String subj) {
        this.subj = subj;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param subj New value of property remarks.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
