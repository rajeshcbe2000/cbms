/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalaryStructureOB.java
 *
 * Created on Aug 12 2014
 */
package com.see.truetransact.ui.payroll.salaryStructure;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.salaryStructure.ScaleDetailsTO;
import com.see.truetransact.transferobject.payroll.salaryStructure.ScaleMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class SalaryStructureOB extends Observable {

    private static SalaryStructureOB salaryStructureOB;
    private final static Logger log = Logger.getLogger(com.see.truetransact.ui.common.SalaryStructureUI.class);
    private ProxyFactory proxy = null;
    Date curDate = null;
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private String cboDesignation = "";
    private String cboIncrementPeriod = "";
    private String txtScaleId = "";
    private String txtVersionNo = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String txtStartingAmount = "";
    private double txtEndingAmount = 0.0;
    private double txtIncrementAmount = 0.0;
    private String txtNoOfIncrements = "";
    private String cboIncrementFrequency = "";
    private boolean rdoStagnationIncrement_Yes = false;
    private boolean rdoStagnationIncrement_No = false;
    private String txtTotalNoOfStagnationIncrements = "";
    private String txtStagIncrAmount = "";
    private String txtStagNoOfIncrements = "";
    private String lblStatus = "";
    private int operation;
    private ComboBoxModel cbmDesignation;
    private ComboBoxModel cbmIncrementPeriod;
    private ComboBoxModel cbmIncrementFrequency;
    private ScaleDetailsTO objScaleDetails;
    private ArrayList recordData, deleteData;
    private TableModel tbmSalaryStructure;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public TableModel getTbmSalaryStructure() {
        return tbmSalaryStructure;
    }

    public void setTbmSalaryStructure(TableModel tbmSalaryStructure) {
        this.tbmSalaryStructure = tbmSalaryStructure;
        setChanged();
    }

    public ComboBoxModel getCbmIncrementFrequency() {
        return cbmIncrementFrequency;
    }

    public void setCbmIncrementFrequency(ComboBoxModel cbmIncrementFrequency) {
        this.cbmIncrementFrequency = cbmIncrementFrequency;
    }

    public ComboBoxModel getCbmIncrementPeriod() {
        return cbmIncrementPeriod;
    }

    public void setCbmIncrementPeriod(ComboBoxModel cbmIncrementPeriod) {
        this.cbmIncrementPeriod = cbmIncrementPeriod;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCboIncrementPeriod() {
        return cboIncrementPeriod;
    }

    public void setCboIncrementPeriod(String cboIncrementPeriod) {
        this.cboIncrementPeriod = cboIncrementPeriod;
        setChanged();
    }

    public boolean isRdoStagnationIncrement_No() {
        return rdoStagnationIncrement_No;
    }

    public void setRdoStagnationIncrement_No(boolean rdoStagnationIncrement_No) {
        this.rdoStagnationIncrement_No = rdoStagnationIncrement_No;
    }

    public boolean isRdoStagnationIncrement_Yes() {
        return rdoStagnationIncrement_Yes;
    }

    public void setRdoStagnationIncrement_Yes(boolean rdoStagnationIncrement_Yes) {
        this.rdoStagnationIncrement_Yes = rdoStagnationIncrement_Yes;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public String getTdtToDate() {
        return tdtToDate;
    }

    public void setTdtToDate(String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    public double getTxtEndingAmount() {
        return txtEndingAmount;
    }

    public void setTxtEndingAmount(double txtEndingAmount) {
        this.txtEndingAmount = txtEndingAmount;
    }

    public double getTxtIncrementAmount() {
        return txtIncrementAmount;
    }

    public void setTxtIncrementAmount(double txtIncrementAmount) {
        this.txtIncrementAmount = txtIncrementAmount;
    }


    public String getTxtNoOfIncrements() {
        return txtNoOfIncrements;
    }

    public void setTxtNoOfIncrements(String txtNoOfIncrements) {
        this.txtNoOfIncrements = txtNoOfIncrements;
    }

    public String getTxtScaleId() {
        return txtScaleId;
    }

    public void setTxtScaleId(String txtScaleId) {
        this.txtScaleId = txtScaleId;
    }

    public String getCboIncrementFrequency() {
        return cboIncrementFrequency;
    }

    public void setCboIncrementFrequency(String cboIncrementFrequency) {
        this.cboIncrementFrequency = cboIncrementFrequency;
    }

    public String getTxtStagIncrAmount() {
        return txtStagIncrAmount;
    }

    public void setTxtStagIncrAmount(String txtStagIncrAmount) {
        this.txtStagIncrAmount = txtStagIncrAmount;
    }

    public String getTxtStagNoOfIncrements() {
        return txtStagNoOfIncrements;
    }

    public void setTxtStagNoOfIncrements(String txtStagNoOfIncrements) {
        this.txtStagNoOfIncrements = txtStagNoOfIncrements;
    }

    public String getTxtStartingAmount() {
        return txtStartingAmount;
    }

    public void setTxtStartingAmount(String txtStartingAmount) {
        this.txtStartingAmount = txtStartingAmount;
    }

    public String getTxtTotalNoOfStagnationIncrements() {
        return txtTotalNoOfStagnationIncrements;
    }

    public void setTxtTotalNoOfStagnationIncrements(String txtTotalNoOfStagnationIncrements) {
        this.txtTotalNoOfStagnationIncrements = txtTotalNoOfStagnationIncrements;
    }

    public String getTxtVersionNo() {
        return txtVersionNo;
    }

    public void setTxtVersionNo(String txtVersionNo) {
        this.txtVersionNo = txtVersionNo;
    }

    public String getCboDesignation() {
        return cboDesignation;
    }

    public void setCboDesignation(String cboDesignation) {
        this.cboDesignation = cboDesignation;
//        setChanged();
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public ComboBoxModel getCbmDesignation() {
        return cbmDesignation;
    }

    public void setCbmDesignation(ComboBoxModel cbmDesignation) {
        this.cbmDesignation = cbmDesignation;
        setChanged();
    }

    static {
        try {
            salaryStructureOB = new SalaryStructureOB();
        } catch (Exception e) {
            log.info("Error in salaryStructureOB:");
        }
    }

    public static SalaryStructureOB getInstance() {
        return salaryStructureOB;
    }

    /**
     * Creates a new instance of EmployeeOB
     */
    public SalaryStructureOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        setTable();
        recordData = new ArrayList();
        deleteData = new ArrayList();
        initianSetup();
    }

    private void initianSetup() throws Exception {
        log.info("initianSetup");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }

    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    doActionPerform();
                }
            } else {
                log.info("Action Type Not Defined In setInwardClearingTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");

        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        try {
            final ScaleMasterTO objScaleMasterTO = setScaleMasterTO();
            final ScaleDetailsTO objScaleDetailsTO = setScaleDetailsTO();
            objScaleMasterTO.setCommand(getCommand());
            objScaleDetailsTO.setCommand(getCommand());
            final HashMap data = new HashMap();
            data.put("SCALEMASTERTO", objScaleMasterTO);
            data.put("SCALEDETAILSTO", objScaleDetailsTO);
            data.put("SCALEDATA", recordData);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            if (proxyResultMap != null && proxyResultMap.containsKey("SCALE_ID") && proxyResultMap.get("SCALE_ID") != null) {
                ClientUtil.showMessageWindow("Successfully Completed - Scale Id: " + CommonUtil.convertObjToStr(proxyResultMap.get("SCALE_ID")));
            }
            setResult(actionType);
            resetForm();
        } catch (SQLException ex) {
            ClientUtil.showMessageWindow(ex.getMessage());
        } catch (Exception e) {
            ClientUtil.showMessageWindow(e.getMessage());
            log.info("Error In doActionPerform()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    public ScaleMasterTO setScaleMasterTO() {
        log.info("In setScaleMaster()");
        final ScaleMasterTO objScaleMasterTO = new ScaleMasterTO();
        objScaleMasterTO.setDesignation(CommonUtil.convertObjToStr(cboDesignation));
        objScaleMasterTO.setScaleId(CommonUtil.convertObjToInt(txtScaleId));
        objScaleMasterTO.setVersionNo(CommonUtil.convertObjToInt(txtVersionNo));
        Date fromDt = DateUtil.getDateMMDDYYYY(tdtFromDate);
        if (fromDt != null) {
            Date bdDate = (Date) curDate.clone();
            bdDate.setDate(fromDt.getDate());
            bdDate.setMonth(fromDt.getMonth());
            bdDate.setYear(fromDt.getYear());
            objScaleMasterTO.setFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate));
        } else {
            objScaleMasterTO.setFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate));
        }

        Date toDt = DateUtil.getDateMMDDYYYY(tdtToDate);
        if (toDt != null) {
            Date bdDate = (Date) curDate.clone();
            bdDate.setDate(toDt.getDate());
            bdDate.setMonth(toDt.getMonth());
            bdDate.setYear(toDt.getYear());
            objScaleMasterTO.setToDate(DateUtil.getDateMMDDYYYY(tdtToDate));
        } else {
            objScaleMasterTO.setToDate(DateUtil.getDateMMDDYYYY(tdtToDate));
        }
        if (rdoStagnationIncrement_Yes == true) {
            objScaleMasterTO.setStagReqd("YES");
        } else if (rdoStagnationIncrement_No == true) {
            objScaleMasterTO.setStagReqd("NO");
        }
        objScaleMasterTO.setStartingAmount(CommonUtil.convertObjToDouble(txtStartingAmount));
        objScaleMasterTO.setEndingAmount(CommonUtil.convertObjToDouble(txtEndingAmount));
        objScaleMasterTO.setStagAmount(CommonUtil.convertObjToDouble(txtStagIncrAmount));
        objScaleMasterTO.setStagCount(CommonUtil.convertObjToDouble(txtStagNoOfIncrements));
        objScaleMasterTO.setStagPeriod(CommonUtil.convertObjToStr(cboIncrementPeriod));
        objScaleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objScaleMasterTO.setCreatedBy(TrueTransactMain.USER_ID);
        objScaleMasterTO.setCreatedDate(curDate);
        objScaleMasterTO.setStatusDate(curDate);
        return objScaleMasterTO;
    }

    public ScaleDetailsTO setScaleDetailsTO() {
        log.info("In setScaleDetails()");
        final ScaleDetailsTO objScaleDetailsTO = new ScaleDetailsTO();
        objScaleDetailsTO.setScale_id(CommonUtil.convertObjToInt(txtScaleId));
        objScaleDetailsTO.setVersion_no(CommonUtil.convertObjToInt(txtVersionNo));
        objScaleDetailsTO.setIncr_amount(CommonUtil.convertObjToDouble(txtIncrementAmount));
        objScaleDetailsTO.setIncr_count(CommonUtil.convertObjToInt(txtNoOfIncrements));
        objScaleDetailsTO.setCount_freq(CommonUtil.convertObjToStr(cboIncrementFrequency));
        return objScaleDetailsTO;
    }

    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SalaryStructureJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.salaryStructure.SalaryStructureHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.salaryStructure.SalaryStructure");
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Start Amount");
        columnHeader.add("Increment Amount");
        columnHeader.add("Increment Count");
        columnHeader.add("End Amount");
        ArrayList data = new ArrayList();
        tbmSalaryStructure = new TableModel(data, columnHeader);
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PAYROLL.DESIGNATION");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PAYROLL.DESIGNATION"));
        cbmDesignation = new ComboBoxModel(key, value);
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public List getScaleId(HashMap dataMap) {
        List scale = ClientUtil.executeQuery("getSalStructScaleId", dataMap);
        if (scale != null && scale.size() > 0) {
        }
        return scale;
    }

    public String getVersionNo(HashMap dataMap) {
        String Version = "";
        List version = ClientUtil.executeQuery("getSalStructVersionNo", dataMap);
        HashMap map = new HashMap();
        if (version != null && version.size() > 0) {
            HashMap result = (HashMap) version.get(0);
            Version = CommonUtil.convertObjToStr(result.get("VERSION_NO"));
            setTxtVersionNo(Version);
        }
        return Version;
    }

    public boolean chkScaleExists(HashMap dataMap) {
        boolean flag = false;
        List scaleId = ClientUtil.executeQuery("checkScaleId", dataMap);
        if (scaleId != null && scaleId.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    private ScaleDetailsTO updateTableDataTO(ScaleDetailsTO oldScaleDetailsTO, ScaleDetailsTO newScaleDetailsTO) {
        oldScaleDetailsTO.setIncr_amount(newScaleDetailsTO.getIncr_amount());
        oldScaleDetailsTO.setIncr_count(newScaleDetailsTO.getIncr_count());
        return oldScaleDetailsTO;
    }

    public void resetForm() {
        setTxtScaleId("");
        setCboDesignation("");
        setTxtVersionNo("");
        recordData = new ArrayList();
    }

    public int insertIntoTableData(int rowNo) {
        objScaleDetails = (ScaleDetailsTO) setScaleDetails();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objScaleDetails != null) {
                row.add(objScaleDetails);
                recordData.add(objScaleDetails);
                ArrayList irRow = this.setRow(objScaleDetails);
                tbmSalaryStructure.insertRow(tbmSalaryStructure.getRowCount(), irRow);
            }
        } else {
            objScaleDetails = updateTableDataTO((ScaleDetailsTO) recordData.get(rowNo), objScaleDetails);
            ArrayList irRow = setRow(objScaleDetails);
            recordData.set(rowNo, objScaleDetails);
            tbmSalaryStructure.removeRow(rowNo);
            tbmSalaryStructure.insertRow(rowNo, irRow);
        }
        tbmSalaryStructure.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }

    private ArrayList setRow(ScaleDetailsTO objScaleDetails) {
        ArrayList row = new ArrayList();
        row.add(getTxtStartingAmount());
        row.add(objScaleDetails.getIncr_amount());
        row.add(objScaleDetails.getIncr_count());
        row.add(getTxtEndingAmount());
        return row;
    }

    public ScaleDetailsTO setScaleDetails() {
        ScaleDetailsTO objScaleDetails = new ScaleDetailsTO();
        objScaleDetails.setIncr_amount(CommonUtil.convertObjToDouble(txtIncrementAmount));
        objScaleDetails.setIncr_count(CommonUtil.convertObjToInt(txtNoOfIncrements));
        objScaleDetails.setCount_freq(CommonUtil.convertObjToStr(cboIncrementFrequency));
        objScaleDetails.setVersion_no(CommonUtil.convertObjToInt(txtVersionNo));
        return objScaleDetails;
    }

    void populateData(HashMap hash) {
        log.info("In populateData()");
        final List listData;
        HashMap data = new HashMap();
        data.put("SCALE", hash.get("SCALE_ID"));
        data.put("VERSION", hash.get("VERSION_NO"));
        listData = ClientUtil.executeQuery("getAllScaleDetails", data);
        if (listData != null && listData.size() > 0) {
            HashMap result = new HashMap();
            for (int i = 0; i < listData.size(); i++) {
                result = (HashMap) listData.get(i);
                setTxtScaleId(CommonUtil.convertObjToStr(result.get("SCALE_ID")));
                setTxtVersionNo(CommonUtil.convertObjToStr(result.get("VERSION_NO")));
                setTxtStartingAmount(CommonUtil.convertObjToStr(result.get("SCALE_START_AMOUNT")));
                setTxtEndingAmount(CommonUtil.convertObjToDouble(result.get("SCALE_END_AMOUNT")));
                setTxtIncrementAmount(CommonUtil.convertObjToDouble(result.get("INCREAMENT_AMOUNT")));
                setTxtNoOfIncrements(CommonUtil.convertObjToStr(result.get("INCREAMENT_COUNT")));
                setCboDesignation(CommonUtil.convertObjToStr(result.get("DESIGNATION")));
                setCboIncrementFrequency(CommonUtil.convertObjToStr(result.get("COUNT_FREQUENCY")));
                setCboIncrementPeriod(CommonUtil.convertObjToStr(result.get("STAGNATION_PERIOD")));
                setTxtStagIncrAmount(CommonUtil.convertObjToStr(result.get("STAGNATION_AMOUNT")));
                setTxtStagNoOfIncrements(CommonUtil.convertObjToStr(result.get("STAGNATION_COUNT")));
                setTdtFromDate(CommonUtil.convertObjToStr(result.get("EFF_DATE")));
                setTdtToDate(CommonUtil.convertObjToStr(result.get("TO_DATE")));
                if (CommonUtil.convertObjToStr(result.get("STAGNATION_REQUIRED")).equals("NO")) {
                    setRdoStagnationIncrement_No(true);
                    setRdoStagnationIncrement_Yes(false);
                } else if (CommonUtil.convertObjToStr(result.get("STAGNATION_REQUIRED")).equals("YES")) {
                    setRdoStagnationIncrement_Yes(true);
                    setRdoStagnationIncrement_No(false);
                }
            }
            populateOB(listData);
        }
    }

    private void populateOB(List lstData) {
        recordData = new ArrayList();
        int size = lstData.size();
        setTable();
        for (int i = 0; i < size; i++) {
            ScaleDetailsTO objScaleDetailsTo = new ScaleDetailsTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            objScaleDetailsTo.setIncr_amount(CommonUtil.convertObjToDouble(newMap.get("INCREAMENT_AMOUNT")));
            objScaleDetailsTo.setIncr_count(CommonUtil.convertObjToInt(newMap.get("INCREAMENT_COUNT")));
            objScaleDetailsTo.setCount_freq(CommonUtil.convertObjToStr(newMap.get("COUNT_FREQUENCY")));
            recordData.add(objScaleDetailsTo);
            ArrayList irRow = this.setRow(objScaleDetailsTo);
            tbmSalaryStructure.insertRow(tbmSalaryStructure.getRowCount(), irRow);
        }
        tbmSalaryStructure.fireTableDataChanged();
        ttNotifyObservers();
    }

    public void populateTableData(int rowNum) {
        objScaleDetails = (ScaleDetailsTO) recordData.get(rowNum);
        this.setTableValues(objScaleDetails);
        ttNotifyObservers();
    }

    private void setTableValues(ScaleDetailsTO objScaleDetails) {
        setTxtIncrementAmount(CommonUtil.convertObjToDouble(objScaleDetails.getIncr_amount()));
        setTxtNoOfIncrements(CommonUtil.convertObjToStr(objScaleDetails.getIncr_count()));
        setCboIncrementFrequency(CommonUtil.convertObjToStr(objScaleDetails.getCount_freq()));
    }

    void deleteDailyData(int rowSelected) {
        objScaleDetails = (ScaleDetailsTO) recordData.get(rowSelected);
        deleteData.add(objScaleDetails);
        recordData.remove(rowSelected);
        tbmSalaryStructure.removeRow(rowSelected);
        tbmSalaryStructure.fireTableDataChanged();
    }

    void resetOBFields() {
        this.setCboDesignation("");
        this.setCboIncrementFrequency("");
        this.setCboIncrementPeriod(cboIncrementPeriod);
        this.tbmSalaryStructure.setData(new ArrayList());
        this.tbmSalaryStructure.fireTableDataChanged();
        this.recordData.clear();
        this.deleteData.clear();
    }
}
