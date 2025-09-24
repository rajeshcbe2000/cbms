/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalaryStructureOB.java
 *
 * Created on May 26, 2004, 10:22 AM
 */

package com.see.truetransact.ui.common;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.common.SalaryStructureTO;
import com.see.truetransact.transferobject.common.CCAllowanceTO;
import com.see.truetransact.transferobject.common.DearnessAllowanceTO;
import com.see.truetransact.transferobject.common.HRAllowanceTO;
import com.see.truetransact.transferobject.common.TAllowanceTO;
import com.see.truetransact.transferobject.common.MAllowanceTO;
import com.see.truetransact.transferobject.common.OtherAllowanceTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  Sathiya
 */

public class SalaryStructureOB extends CObservable{
    
    private static SalaryStructureOB salaryStructureOB;
    private ProxyFactory proxy;
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private int actionType;
    private int result;
    private String lblStatus;
    Date curDate = null;
    private boolean deleteEnable = false;
    
    private ComboBoxModel cbmSalaryStructureProdId,cbmSalaryStructureStagnationOnceIn;
    private TableModel tbmLien;
    private ArrayList key,value,SalaryStructureTOs,deleteSalaryStructureList;
    
    private ComboBoxModel cbmDAValue;
    private TableModel tbmDAllowance;
    private ArrayList DAllowancesTOs,deleteDAllowancesList;
    
    private ComboBoxModel cbmCCAllowance,cbmCCAllowanceCityType;
    private TableModel tbmCCAllowance;
    private ArrayList CCAllowanceTOs,deleteCCAllowanceList;
    
    private ComboBoxModel cbmHRAllowance,cbmHRAllowanceCityType;
    private TableModel tbmHRAllowance;
    private ArrayList HRAllowanceTOs,deleteHRAllowanceList;
    
    private ComboBoxModel cbmTAllowance,cbmTAllowanceCityType;
    private TableModel tbmTAllowance;
    private ArrayList TAllowanceTOs,deleteTAllowanceList;
    
    private ComboBoxModel cbmMAidDesg;
    private TableModel tbmMAllowance;
    private ArrayList MAllowanceTOs,deleteMAllowanceList;
    
    private ComboBoxModel cbmOADesignationValue,cbmOAllowanceTypeValue,cbmOAParameterBasedOnValue,cbmOASubParameterValue;
    private TableModel tbmOAllowance;
    private ArrayList OAllowanceTOs,deleteOAllowanceList;
    
    private String cboSalaryStructureProdId;
    private String cboSalaryStructureStagnationOnceIn;
    private boolean rdoStagnationIncrement_Yes = false;
    private boolean rdoStagnationIncrement_No = false;
    private String lblSalaryStructureSLNOValue = "";
    private String lblSalaryStructureFromDateValue = "";
    private String lblSalaryStructureToDateValue = "";
    private String txtSalaryStructureStartingAmtValue = "";
    private String txtSalaryStructureAmtValue = "";
    private String txtSalaryStructureIncYearValue = "";
    private String txtSalaryStructureEndingAmtValue = "";
    private String txtSalaryStructureSingleRowAmt = "";
    private String txtSalaryStructureTotNoIncValue = "";
    private String txtSalaryStructureStagnationAmtValue = "";
    private String txtSalaryStructureNoOfStagnationValue = "";
    private String txtSalaryStructureStagnationOnceInValue = "";
    private String tempSlNo = "";
    private String cboDADesignationValue;
    private String tdtDAFromDateValue = "";
    private String tdtDAToDateValue = "";
    private String txtDANoOfPointsPerSlabValue = "";
    private String txtDAIndexValue = "";
    private String txtDAPercentagePerSlabValue = "";
    private String txtTotalNoofSlabValue = "";
    private String txtDATotalDAPercentageValue = "";
    private String rdoIndexOrPercent = "";
    private String cboCCAllowance;
    private String cboCCAllowanceCityType;
    private String tdtCCAllowanceFromDateValue = "";
    private String tdtCCAllowanceToDateValue = "";
    private String txtCCAllowanceStartingAmtValue = "";
    private String txtCCAllowanceEndingAmtValue = "";
    private String txtFromAmount = "";
    private String txtToAmount= "";
    private String rdoPercentOrFixed = "";
    private String cboHRAllowanceDesignation;
    private String cboHRAllowanceCityType;
    private boolean rdoHRAPayable_Yes = false;
    private boolean rdoHRAPayable_No = false;
    private String tdtHRAllowanceFromDateValue = "";
    private String tdtHRAllowanceToDateValue = "";
    private String txtHRAllowanceStartingAmtValue = "";
    private String txtHRAllowanceEndingAmtValue = "";
    
    private String cboTAllowanceCityType;
    private String cboTAllowanceDesgination;
    private String tdtTAFromDateValue = "";
    private String tdtTAToDateValue = "";
    private boolean chkFixedConveyance = false;
    private boolean chkPetrolAllowance = false;
    private String txtBasicAmtUptoValue = "";
    private String txtConveyancePerMonthValue = "";
    private String txtBasicAmtBeyondValue = "";
    private String txtConveyanceAmtValue = "";
    private String txtNooflitresValue = "";
    private String txtPricePerlitreValue = "";
    private String txtTotalConveyanceAmtValue = "";
    
    private String cboMAidDesg;
    private String txtMAidAmtValue = "";
    private String tdtMAidFromDateValue = "";
    private String tdtMAidToDateValue = "";
    
    private String cboOADesignationValue = "";
    private String cboOAllowanceTypeValue = "";
    private String cboOAParameterBasedOnValue = "";
    private String cboOASubParameterValue = "";
    private String tdtOAFromDateValue = "";
    private String tdtOAToDateValue = "";
    private boolean chkOAFixedValue = false;
    private boolean chkOAPercentageValue = false;
    private String txtOAFixedAmtValue = "";
    private String txtOAPercentageValue = "";
    private String txtOAMaximumOfValue = "";
    
    
    private String SSAuthorizeStatus = "";
    private String DAAuthorizeStatus = "";
    private String CCAAuthorizeStatus = "";
    private String HRAAuthorizeStatus = "";
    private String TAAuthorizeStatus = "";
    private String MAAuthorizeStatus = "";
    private String OAAuthorizeStatus = "";
    private String oAbasedOnParameter = "";
    /** Creates a new instance of SalaryStructureOB */
    private SalaryStructureOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SalaryStructureJNDI");
        map.put(CommonConstants.HOME, "common.SalaryStructureHome");
        map.put(CommonConstants.REMOTE,"common.SalaryStructure");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
        setTable();
        SalaryStructureTOs = new ArrayList();
        deleteSalaryStructureList = new ArrayList();
        DAllowancesTOs = new ArrayList();
        deleteDAllowancesList = new ArrayList();
        CCAllowanceTOs = new ArrayList();
        deleteCCAllowanceList = new ArrayList();
        HRAllowanceTOs = new ArrayList();
        deleteHRAllowanceList= new ArrayList();
        TAllowanceTOs = new ArrayList();
        deleteTAllowanceList= new ArrayList();
        MAllowanceTOs = new ArrayList();
        deleteMAllowanceList= new ArrayList();
        OAllowanceTOs = new ArrayList();
        deleteOAllowanceList= new ArrayList();
    }
    
    public static SalaryStructureOB getInstance(){
        try {
            salaryStructureOB = new SalaryStructureOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return salaryStructureOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        //        columnHeader.add("SL No.");
        columnHeader.add("Starting Amt");
        columnHeader.add("Increment Amt");
        columnHeader.add("No.of Increment");
        columnHeader.add("Total Amount");
        columnHeader.add("Type");
        columnHeader.add("From Dt");
        columnHeader.add("Authorize Status");
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            columnHeader.add("Verified");
        }
        ArrayList data = new ArrayList();
        tbmLien = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
//        columnHeader.add("No.of Points");
        columnHeader.add("Index");
        columnHeader.add("Total Percent");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmDAllowance = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("From Dt");
        columnHeader.add("From Amount");
        columnHeader.add("To Amount");
        columnHeader.add("Percent/Fixed");
        columnHeader.add("Value");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmCCAllowance = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
        columnHeader.add("HRA No.of Inc");
        columnHeader.add("HRA Total Amount");
        columnHeader.add("City Type");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmHRAllowance = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
        columnHeader.add("TA No.of Inc");
        columnHeader.add("TA Total Amount");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmTAllowance = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
        columnHeader.add("MA No.of Inc");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmMAllowance = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("From Dt");
        columnHeader.add("Allowance");
        columnHeader.add("Parameter");
        columnHeader.add("SubParameter");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmOAllowance = new TableModel(data,columnHeader);
    }
    
    private void fillDropDown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        HashMap param = new HashMap();
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SALARY_STRUCTURE");
        lookupKey.add("PERIOD");
        lookupKey.add("CITY_TYPE");
        lookupKey.add("AAA");
        lookupKey.add("BBB");
        lookupKey.add("PARAMETER_SAL_STRUCTURE");
        lookupKey.add("OTHER_ALLOWANCE");
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        this.cbmSalaryStructureProdId = new ComboBoxModel(key,value);
        this.cbmDAValue = new ComboBoxModel(key,value);
        this.cbmCCAllowance = new ComboBoxModel(key,value);
        this.cbmHRAllowance = new ComboBoxModel(key,value);
        this.cbmTAllowance = new ComboBoxModel(key,value);
        this.cbmMAidDesg = new ComboBoxModel(key,value);
        this.cbmOADesignationValue = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("CITY_TYPE"));
        this.cbmCCAllowanceCityType = new ComboBoxModel(key,value);
        this.cbmHRAllowanceCityType = new ComboBoxModel(key,value);
        this.cbmTAllowanceCityType = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("PERIOD"));
        this.cbmSalaryStructureStagnationOnceIn = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("OTHER_ALLOWANCE"));
        this.cbmOAllowanceTypeValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("PARAMETER_SAL_STRUCTURE"));
        this.cbmOAParameterBasedOnValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        this.cbmOASubParameterValue = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        param = null;
        lookupValues = null;
        key = null;
        value = null;
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    /** Getter for property actionType.
     * @return Value of property actionType.
     *
     */
    public int getActionType() {
        return actionType;
    }
    /** Setter for property actionType.
     * @param actionType New value of property actionType.
     *
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    /** Getter for property result.
     * @return Value of property result.
     *
     */
    public int getResult() {
        return result;
    }
    /** Setter for property result.
     * @param result New value of property result.
     *
     */
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void setStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        notifyObservers();
    }
    
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    
    public void resetForm(){
        setLblStatus("");
        resetTabel();
        resetSalaryStructureValues();
        resetDAValues();
        resetCCAValues();
        resetHRAValues();
        resetTAValues();
        resetMAValues();
        //        resetOAValues();
        resetOAValues();
        setChanged();
        notifyObservers();
    }
    
    public void resetSalaryStructureValues(){
        setRdoStagnationIncrement_Yes(false);
        setRdoStagnationIncrement_No(false);
        setLblSalaryStructureSLNOValue("");
        setLblSalaryStructureFromDateValue("");
        setLblSalaryStructureToDateValue("");
        setTxtSalaryStructureStartingAmtValue("");
        setTxtSalaryStructureAmtValue("");
        setTxtSalaryStructureIncYearValue("");
        setTxtSalaryStructureEndingAmtValue("");
        setTxtSalaryStructureSingleRowAmt("");
        setTxtSalaryStructureTotNoIncValue("");
        setTxtSalaryStructureStagnationAmtValue("");
        setTxtSalaryStructureNoOfStagnationValue("");
        setTxtSalaryStructureStagnationOnceInValue("");
        cbmSalaryStructureProdId.setKeyForSelected("");
        cbmSalaryStructureStagnationOnceIn.setKeyForSelected("");
        setCboSalaryStructureProdId("");
        setCboSalaryStructureStagnationOnceIn("");
    }
    
    public void resetDAValues(){
        cbmDAValue.setKeyForSelected("");
        setCboDADesignationValue("");
        setTdtDAFromDateValue("");
        setTdtDAToDateValue("");
        setTxtDANoOfPointsPerSlabValue("");
        setTxtDAIndexValue("");
        setTxtDAPercentagePerSlabValue("");
        setTxtTotalNoofSlabValue("");
        setTxtDATotalDAPercentageValue("");
        setRdoIndexOrPercent("");
    }
    
    public void resetCCAValues(){
        setCboCCAllowance("");
        setCboCCAllowanceCityType("");
        cbmCCAllowance.setKeyForSelected("");
        cbmCCAllowanceCityType.setKeyForSelected("");
        setTdtCCAllowanceFromDateValue("");
        setTdtCCAllowanceToDateValue("");
        setTxtCCAllowanceStartingAmtValue("");
        setTxtFromAmount("");
        setTxtToAmount("");
        setRdoPercentOrFixed("");
        setTxtCCAllowanceEndingAmtValue("");
    }
    
    public void resetHRAValues(){
        setCboHRAllowanceCityType("");
        setCboHRAllowanceDesignation("");
        cbmHRAllowance.setKeyForSelected("");
        cbmHRAllowanceCityType.setKeyForSelected("");
        setTdtHRAllowanceFromDateValue("");
        setTdtHRAllowanceToDateValue("");
        setTxtHRAllowanceStartingAmtValue("");
        setTxtHRAllowanceEndingAmtValue("");
        setRdoHRAPayable_Yes(false);
        setRdoHRAPayable_No(false);
    }
    
    public void resetTAValues(){
        setCboTAllowanceDesgination("");
        //        setCboHRAllowanceDesignation("");
        cbmTAllowance.setKeyForSelected("");
        //        cbmHRAllowanceCityType.setKeyForSelected("");
        setTdtTAFromDateValue("");
        setTdtTAToDateValue("");
        setTxtBasicAmtUptoValue("");
        setTxtConveyancePerMonthValue("");
        setTxtBasicAmtBeyondValue("");
        setTxtConveyanceAmtValue("");
        setTxtNooflitresValue("");
        setTxtPricePerlitreValue("");
        setTxtTotalConveyanceAmtValue("");
        setChkFixedConveyance(false);
        setChkPetrolAllowance(false);
    }
    
    public void resetMAValues(){
        setCboMAidDesg("");
        cbmMAidDesg.setKeyForSelected("");
        setTdtMAidFromDateValue("");
        setTdtMAidToDateValue("");
        setTxtMAidAmtValue("");
    }
    
    public void resetOAValues(){
        setCboMAidDesg("");
        cbmMAidDesg.setKeyForSelected("");
        setTdtMAidFromDateValue("");
        setTdtMAidToDateValue("");
        setTxtMAidAmtValue("");
        setCboOADesignationValue("");
        setCboOAllowanceTypeValue("");
        setCboOAParameterBasedOnValue("");
        setCboOASubParameterValue("");
        setTdtOAFromDateValue("");
        setTdtOAToDateValue("");
        setChkOAFixedValue(false);
        setChkOAPercentageValue(false);
        setTxtOAFixedAmtValue("");
        setTxtOAPercentageValue("");
        setTxtOAMaximumOfValue("");
        setOAbasedOnParameter("");
    }
    
    /** Getter for property tbmLien.
     * @return Value of property tbmLien.
     *
     */
    public TableModel getTbmLien() {
        return tbmLien;
    }
    /** Setter for property tbmLien.
     * @param tbmLien New value of property tbmLien.
     *
     */
    public void setTbmLien(TableModel tbmLien) {
        this.tbmLien = tbmLien;
    }
    
    private void setTableDataDA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        DearnessAllowanceTO objDA ;
        int size = DAllowancesTOs.size();
        for(int i=0;i<size;i++){
            objDA = (DearnessAllowanceTO)DAllowancesTOs.get(i);
            objDA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetDARow(objDA);
            rows.add(row);
        }
        tbmDAllowance.setData(rows);
        tbmDAllowance.fireTableDataChanged();
    }
    
    private void setTableDataCCA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        CCAllowanceTO objCCA ;
        int size = CCAllowanceTOs.size();
        for(int i=0;i<size;i++){
            objCCA = (CCAllowanceTO)CCAllowanceTOs.get(i);
            objCCA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetCCAARow(objCCA);
            rows.add(row);
        }
        tbmCCAllowance.setData(rows);
        tbmCCAllowance.fireTableDataChanged();
    }
    
    private void setTableDataHRA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        HRAllowanceTO objHRA ;
        int size = HRAllowanceTOs.size();
        for(int i=0;i<size;i++){
            objHRA = (HRAllowanceTO)HRAllowanceTOs.get(i);
            objHRA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetHRAARow(objHRA);
            rows.add(row);
        }
        tbmHRAllowance.setData(rows);
        tbmHRAllowance.fireTableDataChanged();
    }
    
    private void setTableDataTA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        TAllowanceTO objTA ;
        int size = TAllowanceTOs.size();
        for(int i=0;i<size;i++){
            objTA = (TAllowanceTO)TAllowanceTOs.get(i);
            objTA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetTARow(objTA);
            rows.add(row);
        }
        tbmTAllowance.setData(rows);
        tbmTAllowance.fireTableDataChanged();
    }
    
    private void setTableDataMA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        MAllowanceTO objMA ;
        int size = MAllowanceTOs.size();
        for(int i=0;i<size;i++){
            objMA = (MAllowanceTO)MAllowanceTOs.get(i);
            objMA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetMARow(objMA);
            rows.add(row);
        }
        tbmMAllowance.setData(rows);
        tbmMAllowance.fireTableDataChanged();
    }
    
    private void setTableDataOA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        OtherAllowanceTO objOA ;
        int size = OAllowanceTOs.size();
        for(int i=0;i<size;i++){
            objOA = (OtherAllowanceTO)OAllowanceTOs.get(i);
            objOA.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetOARow(objOA);
            rows.add(row);
        }
        tbmOAllowance.setData(rows);
        tbmOAllowance.fireTableDataChanged();
    }
    
    public void setCbmProdId(String prodType) {
        if (cbmSalaryStructureProdId.getKeyForSelected()!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmSalaryStructureProdId.getKeyForSelected());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmSalaryStructureProdId.getKeyForSelected()));
                this.cbmOAllowanceTypeValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmOAllowanceTypeValue = new ComboBoxModel(key,value);
        this.cbmOAllowanceTypeValue = cbmOAllowanceTypeValue;
        setChanged();
    }
    
    public void setCbmProd(String prodType) {
        if (cbmOAParameterBasedOnValue.getKeyForSelected()!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmOAParameterBasedOnValue.getKeyForSelected());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmOAParameterBasedOnValue.getKeyForSelected()));
                this.cbmOASubParameterValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmOASubParameterValue = new ComboBoxModel(key,value);
        this.cbmOASubParameterValue = cbmOASubParameterValue;
        setChanged();
    }
    
    public void getSalaryStructureData(String grade){
        HashMap whereMap = new HashMap();
        String mapName = "";
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        whereMap.put("GRADE",grade);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){//||
            //        getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
            //        getActionType()==ClientConstants.ACTIONTYPE_VIEW){
            mapName = "getSelectSalaryEditStructureTO";
            //        }else if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
            //            getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION ||
            //            getActionType()==ClientConstants.ACTIONTYPE_REJECT ){
            //            mapName = "getSelectSalaryEditStructureTO";
        }
        List list = ClientUtil.executeQuery(mapName,whereMap);
        if(list!=null && list.size()>0){
            SalaryStructureTOs=(ArrayList)list;
            setTableData();
            //        }else {
            //            resetTabel();
        }
        whereMap = null;
    }
    
    private void setTableData(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        SalaryStructureTO obj ;
        int size = SalaryStructureTOs.size();
        for(int i=0;i<size;i++){
            obj = (SalaryStructureTO)SalaryStructureTOs.get(i);
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            row = populateAndsetRow(obj);
            rows.add(row);
        }
        //        setTable();
        tbmLien.setData(rows);
        tbmLien.fireTableDataChanged();
        obj=null;
    }
    
    private ArrayList setRow(SalaryStructureTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getStartingScaleAmt()));
        if(getRdoStagnationIncrement_No() == true){
            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("NI");
        }else if(getRdoStagnationIncrement_Yes() == true){
            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("SI");
        }
        row.add(CommonUtil.convertObjToStr(obj.getFromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    public void populateSalaryStructure(int rowNum) {
        
        System.out.println("@#$@#$@#$SalaryStructureTOs"+SalaryStructureTOs);
        SalaryStructureTO obj = (SalaryStructureTO)SalaryStructureTOs.get(rowNum);
        this.setCboSalaryStructureProdId(obj.getGrade());
        this.setLblSalaryStructureFromDateValue(CommonUtil.convertObjToStr(obj.getFromDate()));
        this.setTxtSalaryStructureStartingAmtValue(obj.getStartingScaleAmt());
        this.setTxtSalaryStructureEndingAmtValue(obj.getTotalAmount());
        if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("SI")){
            setRdoStagnationIncrement_Yes(true);
            setRdoStagnationIncrement_No(false);
            this.setTxtSalaryStructureAmtValue("");
            this.setTxtSalaryStructureIncYearValue("");
            setTxtSalaryStructureTotNoIncValue(obj.getTotNoOfStagnation());
            setTxtSalaryStructureStagnationAmtValue(obj.getStagnationIncAmt());
            setTxtSalaryStructureNoOfStagnationValue(obj.getNoofStagnation());
            setTxtSalaryStructureStagnationOnceInValue(obj.getStagnationOnceIn());
            setCboSalaryStructureStagnationOnceIn(obj.getStagnationValues());
        }else if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("NI")){
            setRdoStagnationIncrement_No(true);
            setRdoStagnationIncrement_Yes(false);
            this.setTxtSalaryStructureAmtValue(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            this.setTxtSalaryStructureIncYearValue(obj.getNoOfIncrement());
        }
        setSSAuthorizeStatus(obj.getAuthorizeStatus());
        HashMap whereMap = new HashMap();
        String mapNameDA = "";
        String mapNameCCA = "";
        String mapNameHRA = "";
        String mapNameTA = "";
        String mapNameMA = "";
        String mapNameOA = "";
        whereMap.put("BRANCH_CODE",obj.getBranchCode());
        whereMap.put("GRADE",obj.getGrade());
        whereMap.put("FROM_DATE",obj.getFromDate());
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){// ||
            //        getActionType()==ClientConstants.ACTIONTYPE_VIEW){
            mapNameDA = "getSelectDearnessAllowanceEditTO";
            mapNameCCA = "getSelectCAllowanceEditTO";
            mapNameHRA = "getSelectHRAllowanceEditTO";
            mapNameTA = "getSelectTAllowanceEditTO";
            mapNameMA = "getSelectMAllowanceEditTO";
            mapNameOA = "getSelectOAllowanceEditTO";
            //        }else if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
            //            getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION ||
            //            getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
            //            getActionType()==ClientConstants.ACTIONTYPE_REJECT){
            //            mapNameDA = "getSelectDearnessAllowanceTO";
            //            mapNameCCA = "getSelectCAllowanceTO";
            //            mapNameHRA = "getSelectHRAllowanceTO";
            //            mapNameTA = "getSelectTAllowanceTO";
            //            mapNameMA = "getSelectMAllowanceTO";
        }
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            List listDA = ClientUtil.executeQuery(mapNameDA,whereMap);
            if(listDA!=null && listDA.size()>0){
                DAllowancesTOs=(ArrayList)listDA;
                setTableDataDA();
            }
            List listCCA = ClientUtil.executeQuery(mapNameCCA,whereMap);
            if(listCCA!=null && listCCA.size()>0){
                CCAllowanceTOs=(ArrayList)listCCA;
                setTableDataCCA();
            }
            List listHRA = ClientUtil.executeQuery(mapNameHRA,whereMap);
            if(listHRA!=null && listHRA.size()>0){
                HRAllowanceTOs=(ArrayList)listHRA;
                setTableDataHRA();
            }
            List listTA = ClientUtil.executeQuery(mapNameTA,whereMap);
            if(listTA!=null && listTA.size()>0){
                TAllowanceTOs=(ArrayList)listTA;
                setTableDataTA();
            }
            List listMA = ClientUtil.executeQuery(mapNameMA,whereMap);
            if(listMA!=null && listMA.size()>0){
                MAllowanceTOs=(ArrayList)listMA;
                setTableDataMA();
            }
            List listOA = ClientUtil.executeQuery(mapNameOA,whereMap);
            if(listOA!=null && listOA.size()>0){
                OAllowanceTOs=(ArrayList)listOA;
                setTableDataOA();
            }
        }
        obj = null;
    }
    
    public void deleteLienData(int rowNum) {
        deleteEnable = true;
        SalaryStructureTO obj = (SalaryStructureTO)SalaryStructureTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteSalaryStructureList == null){
            deleteSalaryStructureList = new ArrayList();
        }
        deleteSalaryStructureList.add(obj);
        SalaryStructureTOs.remove(rowNum);
        tbmLien.removeRow(rowNum);
        tbmLien.fireTableDataChanged();
        //        insertLienData(rowNum);
        obj = null;
    }
    
    public int insertLienData(int rowNo) {
        SalaryStructureTO obj = SalaryStructureTO(rowNo);
        if(rowNo == -1){
            SalaryStructureTOs.add(obj);
            ArrayList irRow = this.setRow(obj);
            tbmLien.insertRow(tbmLien.getRowCount(), irRow);
            tbmLien.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    private SalaryStructureTO SalaryStructureTO(int rowNo){
        double startingAmt = 0.0;
        SalaryStructureTO obj = null;
        if(rowNo == -1){
            obj = new SalaryStructureTO();
            startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStartingAmtValue()).doubleValue();
            obj.setStartingScaleAmt(String.valueOf((long)startingAmt));
            if(getRdoStagnationIncrement_No() == true){
                obj.setIncrementAmt(getTxtSalaryStructureAmtValue());
                obj.setNoOfIncrement(getTxtSalaryStructureIncYearValue());
                obj.setSalaryType("NI");
                obj.setStagnationInc("N");
            }else if(getRdoStagnationIncrement_Yes() == true){
                obj.setIncrementAmt(getTxtSalaryStructureStagnationAmtValue());
                obj.setNoOfIncrement(getTxtSalaryStructureNoOfStagnationValue());
                obj.setSalaryType("SI");
                obj.setStagnationInc("Y");
            }
            obj.setTotNoOfStagnation(getTxtSalaryStructureTotNoIncValue());
            double stagnationAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStagnationAmtValue()).doubleValue();
            obj.setStagnationIncAmt(String.valueOf((long)stagnationAmt));
            double noOfInc = CommonUtil.convertObjToDouble(getTxtSalaryStructureNoOfStagnationValue()).doubleValue();
            obj.setNoofStagnation(String.valueOf((long)noOfInc));
            double onceIn = CommonUtil.convertObjToDouble(getTxtSalaryStructureStagnationOnceInValue()).doubleValue();
            obj.setStagnationOnceIn(String.valueOf((long)onceIn));
            obj.setStagnationValues(getCboSalaryStructureStagnationOnceIn());
            obj.setGrade(getCboSalaryStructureProdId());
            obj.setFromDate(DateUtil.getDateMMDDYYYY(getLblSalaryStructureFromDateValue()));
            if(tbmLien.getRowCount() == 0){
                startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStartingAmtValue()).doubleValue();
                double incAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureAmtValue()).doubleValue();
                double noOfAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureIncYearValue()).doubleValue();
                double total = (long)startingAmt + (long)(noOfAmt * incAmt);
                obj.setSingleRowTotAmt(String.valueOf((long)total));
                obj.setSlNo(String.valueOf((long)1));
                //                obj.setSlNo(String.valueOf(rowNum +1));
            }else if(tbmLien.getRowCount() >=1){
                startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureSingleRowAmt()).doubleValue();
                obj.setSingleRowTotAmt(String.valueOf((long)startingAmt));
                obj.setSlNo(String.valueOf((long)tbmLien.getRowCount()+1));
            }
            double totAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureEndingAmtValue()).doubleValue();
            obj.setTotalAmount(String.valueOf((long)totAmt));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            double sumAmt = 0.0;
            int i = rowNo;
            double lastRecAmt = 0.0;
            String type = "";
            for (;i<tbmLien.getRowCount();i++){
                obj = new SalaryStructureTO();
                startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStartingAmtValue()).doubleValue();
                obj.setStartingScaleAmt(String.valueOf((long)startingAmt));
                obj.setIncrementAmt(getTxtSalaryStructureAmtValue());
                obj.setNoOfIncrement(getTxtSalaryStructureIncYearValue());
                obj.setGrade(getCboSalaryStructureProdId());
                obj.setFromDate(DateUtil.getDateMMDDYYYY(getLblSalaryStructureFromDateValue()));
                if(i == 0 && CommonUtil.convertObjToStr(obj.getAuthorizeStatus()).equals("")){
                    lastRecAmt = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,0)).doubleValue();
                    type = CommonUtil.convertObjToStr(tbmLien.getValueAt(i,4));
                }else if(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()).equals("")){
                    String authStatus = CommonUtil.convertObjToStr(tbmLien.getValueAt(i-1,6));
                    if(authStatus.equals("")){
                        lastRecAmt = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i-1,3)).doubleValue();
                    }else{
                        lastRecAmt = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,0)).doubleValue();
                    }
                    type = CommonUtil.convertObjToStr(tbmLien.getValueAt(i,4));
                }
                if(type!=null && type.equals("NI")){
                    if(i == rowNo){
                        startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStartingAmtValue()).doubleValue();
                        double incAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureAmtValue()).doubleValue();
                        double noOfInc = CommonUtil.convertObjToDouble(getTxtSalaryStructureIncYearValue()).doubleValue();
                        //                        obj.setIncrementAmt(String.valueOf((long)incAmt));
                        obj.setIncrementAmt(getTxtSalaryStructureAmtValue());
                        obj.setNoOfIncrement(String.valueOf((long)noOfInc));
                        lastRecAmt = (long)lastRecAmt + (long)(incAmt * noOfInc);
                        obj.setSingleRowTotAmt(String.valueOf((long)lastRecAmt));
                    }else{
                        double incAmt = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,1)).doubleValue();
                        double noOfInc = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,2)).doubleValue();
                        obj.setIncrementAmt(String.valueOf((long)incAmt));
                        obj.setNoOfIncrement(String.valueOf((long)noOfInc));
                        sumAmt = (long)incAmt * (long)noOfInc;
                        lastRecAmt = (long)lastRecAmt + (long)sumAmt;
                        obj.setSingleRowTotAmt(String.valueOf((long)lastRecAmt));
                    }
                    obj.setSalaryType("NI");
                }else if(type != null && type.equals("SI")){
                    double incAmt = 0.0;
                    double noOfInc = 0.0;
                    if(i == rowNo){
                        startingAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStartingAmtValue()).doubleValue();
                        incAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureStagnationAmtValue()).doubleValue();
                        noOfInc = CommonUtil.convertObjToDouble(getTxtSalaryStructureNoOfStagnationValue()).doubleValue();
                        obj.setIncrementAmt(String.valueOf((long)incAmt));
                        obj.setNoOfIncrement(String.valueOf((long)noOfInc));
                        lastRecAmt = (long)lastRecAmt + (long)(incAmt * noOfInc);
                        obj.setSingleRowTotAmt(String.valueOf((long)lastRecAmt));
                    }else{
                        incAmt = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,1)).doubleValue();
                        noOfInc = CommonUtil.convertObjToDouble(tbmLien.getValueAt(i,2)).doubleValue();
                        obj.setIncrementAmt(String.valueOf((long)incAmt));
                        obj.setNoOfIncrement(String.valueOf((long)noOfInc));
                        //                        changed here
                        System.out.println("#@$@#$@#$obj"+obj);
                        sumAmt = (long)incAmt * (long)noOfInc;
                        lastRecAmt = (long)lastRecAmt + (long)sumAmt;
                        obj.setSingleRowTotAmt(String.valueOf((int)lastRecAmt));
                    }
                    
                    obj.setSingleRowTotAmt(String.valueOf((long)lastRecAmt));
                    obj.setTotNoOfStagnation(getTxtSalaryStructureTotNoIncValue());
                    obj.setStagnationIncAmt(String.valueOf((long)incAmt));
                    obj.setNoofStagnation(String.valueOf((long)noOfInc));
                    obj.setStagnationOnceIn(getTxtSalaryStructureStagnationOnceInValue());
                    obj.setStagnationValues(getCboSalaryStructureStagnationOnceIn());
                    obj.setSalaryType("SI");
                    System.out.println("#@$@#$@#$obj 2"+obj);
                }
                double totAmt = CommonUtil.convertObjToDouble(getTxtSalaryStructureEndingAmtValue()).doubleValue();
                obj.setSlNo(String.valueOf(i+1));
                obj.setTotalAmount(String.valueOf((long)totAmt));
                //                obj.setTotalAmount(String.valueOf(getTxtSalaryStructureEndingAmtValue()));
                obj.setStatus(CommonConstants.STATUS_MODIFIED);
                obj.setStatusDate(curDate);
                obj.setStatusBy(TrueTransactMain.USER_ID);
                obj.setBranchCode(TrueTransactMain.BRANCH_ID);
                System.out.println("#@$@#$@#$obj 3"+obj);
                ArrayList irRow = setEditedRow(obj);
                System.out.println("#@$@#$@#irRow:"+irRow);
                System.out.println("#@$@#$@#SalaryStructureTOs BEFORE:"+SalaryStructureTOs);
                SalaryStructureTOs.set(i,obj);
                System.out.println("#@$@#$@#SalaryStructureTOs AFTER:"+SalaryStructureTOs);
                tbmLien.removeRow(i);
                tbmLien.insertRow(i,irRow);
            }
            tbmLien.fireTableDataChanged();
        }
        return obj;
    }
    private ArrayList populateAndsetRow(SalaryStructureTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getStartingScaleAmt()));
        if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("NI")){
            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("NI");
        }else if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("SI")){
            row.add(CommonUtil.convertObjToStr(obj.getStagnationIncAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoofStagnation()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("SI");
        }
        row.add(CommonUtil.convertObjToStr(obj.getFromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            row.add("No");
        }
        return row;
    }
    
    public void doAction(){
        try{
            HashMap objHashMap=null;
            if(authorizeMap==null) {
                if(deleteCCAllowanceList!=null && deleteCCAllowanceList.size()>0){
                    SalaryStructureTOs.addAll(deleteSalaryStructureList);
                }
                if(deleteDAllowancesList!=null && deleteDAllowancesList.size()>0){
                    DAllowancesTOs.addAll(deleteDAllowancesList);
                }
                if(deleteCCAllowanceList!=null && deleteCCAllowanceList.size()>0){
                    CCAllowanceTOs.addAll(deleteCCAllowanceList);
                }
                if(deleteHRAllowanceList!=null && deleteHRAllowanceList.size()>0){
                    HRAllowanceTOs.addAll(deleteHRAllowanceList);
                }
                if(deleteTAllowanceList!=null && deleteTAllowanceList.size()>0){
                    TAllowanceTOs.addAll(deleteTAllowanceList);
                }
                if(deleteMAllowanceList!=null && deleteMAllowanceList.size()>0){
                    MAllowanceTOs.addAll(deleteMAllowanceList);
                }
                if(deleteOAllowanceList!=null && deleteOAllowanceList.size()>0){
                    OAllowanceTOs.addAll(deleteOAllowanceList);
                }
                if(SalaryStructureTOs!=null && SalaryStructureTOs.size()>0){
                    objHashMap = new HashMap();
                    if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                        setStatusForTOs();
                    }
                    if(SalaryStructureTOs!=null && SalaryStructureTOs.size()>0){
                        objHashMap.put("SalaryStructureTOs",SalaryStructureTOs);
                        //                        SalaryStructureTOs = null;
                    }
                    if(deleteSalaryStructureList!=null && deleteSalaryStructureList.size()>0){
                        objHashMap.put("deleteSalaryStructure",deleteSalaryStructureList);
                        deleteSalaryStructureList = null;
                    }
                    if(DAllowancesTOs!=null && DAllowancesTOs.size()>0){
                        objHashMap.put("DAllowancesTOs",DAllowancesTOs);
                        //                        DAllowancesTOs = null;
                    }
                    if(deleteDAllowancesList!=null && deleteDAllowancesList.size()>0){
                        objHashMap.put("deleteDAllowancesList",deleteDAllowancesList);
                        deleteDAllowancesList= null;
                    }
                    if(CCAllowanceTOs!=null && CCAllowanceTOs.size()>0){
                        objHashMap.put("CCAllowanceTOs",CCAllowanceTOs);
                    }
                    if(deleteCCAllowanceList!=null && deleteCCAllowanceList.size()>0){
                        objHashMap.put("deleteCCAllowanceList",deleteCCAllowanceList);
                        deleteCCAllowanceList= null;
                    }
                    if(HRAllowanceTOs!=null && HRAllowanceTOs.size()>0){
                        objHashMap.put("HRAllowanceTOs",HRAllowanceTOs);
                    }
                    if(deleteHRAllowanceList!=null && deleteHRAllowanceList.size()>0){
                        objHashMap.put("deleteHRAllowanceList",deleteHRAllowanceList);
                        deleteHRAllowanceList = null;
                    }
                    if(TAllowanceTOs!=null && TAllowanceTOs.size()>0){
                        objHashMap.put("TAllowanceTOs",TAllowanceTOs);
                        //                        TAllowanceTOs = null;
                    }
                    if(deleteTAllowanceList!=null && deleteTAllowanceList.size()>0){
                        objHashMap.put("deleteTAllowanceList",deleteTAllowanceList);
                        deleteTAllowanceList = null;
                    }
                    if(MAllowanceTOs!=null && MAllowanceTOs.size()>0){
                        objHashMap.put("MAllowanceTOs",MAllowanceTOs);
                        //                        MAllowanceTOs = null;
                    }
                    if(deleteMAllowanceList!=null && deleteMAllowanceList.size()>0){
                        objHashMap.put("deleteMAllowanceList",deleteMAllowanceList);
                        deleteMAllowanceList= null;
                    }
                    if(OAllowanceTOs!=null && OAllowanceTOs.size()>0){
                        objHashMap.put("OAllowanceTOs",OAllowanceTOs);
                        //                        OAllowanceTOs = null;
                    }
                    if(deleteOAllowanceList!=null && deleteOAllowanceList.size()>0){
                        objHashMap.put("deleteOAllowanceList",deleteOAllowanceList);
                        deleteOAllowanceList = null;
                    }
                    System.out.println("objHashMap :"+objHashMap);
                }
            }else {
                objHashMap=authorizeMap;
            }
            HashMap proxyResultMap = proxy.execute(objHashMap,map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
            objHashMap = null;
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    private ArrayList setEditedRow(SalaryStructureTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getStartingScaleAmt()));
        if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("NI")){
            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("NI");
        }else if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("SI")){
            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
            row.add("SI");
        }
        row.add(CommonUtil.convertObjToStr(obj.getFromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    private void setStatusForTOs(){
        SalaryStructureTO objTO;
        int size = SalaryStructureTOs.size();
        for(int i=0;i<size;i++){
            objTO =(SalaryStructureTO)SalaryStructureTOs.get(i);
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            SalaryStructureTOs.set(i, objTO);
        }
        objTO=null;
    }
    public void populateSalaryStructure(String lienNo){
        SalaryStructureTO obj;
        int size = SalaryStructureTOs.size();
        for(int i=0;i<size;i++){
            obj = (SalaryStructureTO)SalaryStructureTOs.get(i);
            if(obj != null){
                this.populateSalaryStructure(i);
            }
            obj = null;
            return;
        }
    }
    //    public java.lang.String getAuthorizeStatus(){
    //        if(this.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE)
    //            return CommonConstants.STATUS_AUTHORIZED;
    //        else if(this.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION)
    //            return CommonConstants.STATUS_EXCEPTION;
    //        else if(this.getActionType()==ClientConstants.ACTIONTYPE_REJECT)
    //            return CommonConstants.STATUS_REJECTED;
    //        return "";
    //    }
    public void resetTabel(){
        this.tbmLien.setData(new ArrayList());
        this.tbmLien.fireTableDataChanged();
        this.SalaryStructureTOs.clear();
        
        this.tbmDAllowance.setData(new ArrayList());
        this.tbmDAllowance.fireTableDataChanged();
        this.DAllowancesTOs.clear();
        
        this.tbmCCAllowance.setData(new ArrayList());
        this.tbmCCAllowance.fireTableDataChanged();
        this.CCAllowanceTOs.clear();
        
        this.tbmHRAllowance.setData(new ArrayList());
        this.tbmHRAllowance.fireTableDataChanged();
        this.HRAllowanceTOs.clear();
        
        this.tbmTAllowance.setData(new ArrayList());
        this.tbmTAllowance.fireTableDataChanged();
        this.TAllowanceTOs.clear();
        
        this.tbmMAllowance.setData(new ArrayList());
        this.tbmMAllowance.fireTableDataChanged();
        this.MAllowanceTOs.clear();
        
        this.tbmOAllowance.setData(new ArrayList());
        this.tbmOAllowance.fireTableDataChanged();
        this.OAllowanceTOs.clear();
    }
    public void deleteDAData(int rowNum) {
        deleteEnable = true;
        DearnessAllowanceTO obj = (DearnessAllowanceTO)DAllowancesTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deleteDAllowancesList.add(obj);
        DAllowancesTOs.remove(rowNum);
        tbmDAllowance.removeRow(rowNum);
        tbmDAllowance.fireTableDataChanged();
        obj = null;
    }
    public int insertDAData(int rowNo) {
        DearnessAllowanceTO obj = DearnessAllowanceTO(rowNo);
        if(rowNo == -1){
            DAllowancesTOs.add(obj);
            ArrayList irRow = this.setRowDA(obj);
            tbmDAllowance.insertRow(tbmDAllowance.getRowCount(), irRow);
            tbmDAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    private ArrayList setRowDA(DearnessAllowanceTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
//        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
        row.add(CommonUtil.convertObjToStr(obj.getDATotalDAPercentage()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    private DearnessAllowanceTO DearnessAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        DearnessAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new DearnessAllowanceTO();
            if(tbmDAllowance.getRowCount() == 0){
                obj.setDAslNo(String.valueOf(1));
            }else if(tbmDAllowance.getRowCount() >=1){
                obj.setDAslNo(String.valueOf(tbmDAllowance.getRowCount()+1));
            }
            obj.setDAgrade(getCboDADesignationValue());
            obj.setDAfromDate(DateUtil.getDateMMDDYYYY(getTdtDAFromDateValue()));
            obj.setDAtoDate(DateUtil.getDateMMDDYYYY(getTdtDAToDateValue()));
            obj.setDANoOfPointsPerSlab(getTxtDANoOfPointsPerSlabValue());
            obj.setDAIndex(getTxtDAIndexValue());
            obj.setDAPercentagePerSlab(getTxtDAPercentagePerSlabValue());
            obj.setDATotalNoOfSlab(getTxtTotalNoofSlabValue());
            obj.setDATotalDAPercentage(getTxtDATotalDAPercentageValue());
            obj.setRdoIndexOrPercent(getRdoIndexOrPercent());
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            //            int i = rowNo;
            //            for (;i<tbmDAllowance.getRowCount();i++){
            obj = new DearnessAllowanceTO();
            obj.setDAgrade(getCboDADesignationValue());
            obj.setDAfromDate(DateUtil.getDateMMDDYYYY(getTdtDAFromDateValue()));
            obj.setDAtoDate(DateUtil.getDateMMDDYYYY(getTdtDAToDateValue()));
            if(rowNo == 0){
                obj.setDAslNo(String.valueOf(1));
            }else{
                int no = rowNo + 1;
                obj.setDAslNo(String.valueOf(no));
            }
            //                if(i == rowNo){
            obj.setDANoOfPointsPerSlab(getTxtDANoOfPointsPerSlabValue());
            obj.setDAIndex(getTxtDAIndexValue());
            obj.setDAPercentagePerSlab(getTxtDAPercentagePerSlabValue());
            obj.setDATotalNoOfSlab(getTxtTotalNoofSlabValue());
            obj.setDATotalDAPercentage(getTxtDATotalDAPercentageValue());
            obj.setRdoIndexOrPercent(getRdoIndexOrPercent());
            //                }else{
            //                    double points = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,1)).doubleValue();
            //                    double index = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,2)).doubleValue();
            //                    double daPer = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,3)).doubleValue();
            //                    double TotNoOfSlab = index/points;
            //                    double daPerSlab = TotNoOfSlab * daPer;
            //                    obj.setDANoOfPointsPerSlab(String.valueOf(points));
            //                    obj.setDAIndex(String.valueOf(index));
            //                    obj.setDAPercentagePerSlab(String.valueOf(daPer));
            //                    obj.setDATotalNoOfSlab(String.valueOf(TotNoOfSlab));
            //                    obj.setDATotalDAPercentage(String.valueOf(daPerSlab));
            //                }
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowDA(obj);
            DAllowancesTOs.set(rowNo,obj);
            tbmDAllowance.removeRow(rowNo);
            tbmDAllowance.insertRow(rowNo,irRow);
            //            }
            tbmDAllowance.fireTableDataChanged();
        }
        return obj;
    }
    
    //    private ArrayList setEditedDARow(DearnessAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAPercentagePerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
    //        return row;
    //    }
    
    public void populateDA(int rowNum) {
        DearnessAllowanceTO obj = (DearnessAllowanceTO)DAllowancesTOs.get(rowNum);
        this.setCboDADesignationValue(obj.getDAgrade());
        this.setTdtDAFromDateValue(CommonUtil.convertObjToStr(obj.getDAfromDate()));
        this.setTdtDAToDateValue(CommonUtil.convertObjToStr(obj.getDAtoDate()));
        this.setTxtDANoOfPointsPerSlabValue(obj.getDANoOfPointsPerSlab());
        this.setTxtDAIndexValue(obj.getDAIndex());
        this.setTxtDAPercentagePerSlabValue(obj.getDAPercentagePerSlab());
        this.setTxtTotalNoofSlabValue(obj.getDATotalNoOfSlab());
        this.setTxtDATotalDAPercentageValue(obj.getDATotalDAPercentage());
        this.setRdoIndexOrPercent(obj.getRdoIndexOrPercent());
        setDAAuthorizeStatus(obj.getAuthorizeStatus());
        obj = null;
    }
    
    private ArrayList populateAndsetDARow(DearnessAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
//        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
        row.add(CommonUtil.convertObjToStr(obj.getDATotalDAPercentage()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    //cca starts//
    public int insertCCAData(int rowNo) {
        CCAllowanceTO obj = CCAllowanceTO(rowNo);
        if(rowNo == -1){
            CCAllowanceTOs.add(obj);
            ArrayList irRow = this.setRowCCA(obj);
            tbmCCAllowance.insertRow(tbmCCAllowance.getRowCount(), irRow);
            tbmCCAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateCCA(int rowNum) {
        CCAllowanceTO obj = (CCAllowanceTO)CCAllowanceTOs.get(rowNum);
        this.setCboCCAllowance(obj.getCCgrade());
        this.setCboCCAllowanceCityType(obj.getCCCityType());
        this.setTdtCCAllowanceFromDateValue(CommonUtil.convertObjToStr(obj.getCCfromDate()));
        this.setTdtCCAllowanceToDateValue(CommonUtil.convertObjToStr(obj.getCCtoDate()));
        this.setTxtCCAllowanceStartingAmtValue(obj.getCCstartingScaleAmt());
        this.setTxtFromAmount(obj.getTxtFromAmount());
        this.setTxtToAmount(obj.getTxtToAmount());
        this.setRdoPercentOrFixed(obj.getRdoPercentOrFixed());
        this.setTxtCCAllowanceEndingAmtValue(obj.getCCincrementAmt());
        setCCAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private CCAllowanceTO CCAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        CCAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new CCAllowanceTO();
            if(tbmCCAllowance.getRowCount() == 0){
                obj.setCCslNo(String.valueOf(1));
            }else if(tbmCCAllowance.getRowCount() >=1){
                obj.setCCslNo(String.valueOf(tbmCCAllowance.getRowCount()+1));
            }
            obj.setCCgrade(getCboCCAllowance());
            obj.setCCCityType(getCboCCAllowanceCityType());
            obj.setCCfromDate(DateUtil.getDateMMDDYYYY(getTdtCCAllowanceFromDateValue()));
            obj.setCCtoDate(DateUtil.getDateMMDDYYYY(getTdtCCAllowanceToDateValue()));
            obj.setCCstartingScaleAmt(CommonUtil.convertObjToStr(getTxtCCAllowanceStartingAmtValue()));
            obj.setTxtToAmount(CommonUtil.convertObjToStr(getTxtToAmount()));
            obj.setTxtFromAmount(CommonUtil.convertObjToStr(getTxtFromAmount()));
            obj.setRdoPercentOrFixed(CommonUtil.convertObjToStr(getRdoPercentOrFixed()));
            
            obj.setCCincrementAmt(CommonUtil.convertObjToStr(getTxtCCAllowanceEndingAmtValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            //            int i = rowNo;
            //            double lastRecAmt = 0.0;
            //            for (;i<tbmCCAllowance.getRowCount();i++){
            obj = new CCAllowanceTO();
            if(rowNo == 0){
                obj.setCCslNo(String.valueOf(1));
            }else{
                int no = rowNo + 1;
                obj.setCCslNo(String.valueOf(no));
            }
            obj.setCCgrade(getCboCCAllowance());
            obj.setCCCityType(getCboCCAllowanceCityType());
            obj.setCCfromDate(DateUtil.getDateMMDDYYYY(getTdtCCAllowanceFromDateValue()));
            obj.setCCtoDate(DateUtil.getDateMMDDYYYY(getTdtCCAllowanceToDateValue()));
            //                if(i == rowNo){
            obj.setCCstartingScaleAmt(CommonUtil.convertObjToStr(getTxtCCAllowanceStartingAmtValue()));
            obj.setTxtToAmount(CommonUtil.convertObjToStr(getTxtToAmount()));
            obj.setTxtFromAmount(CommonUtil.convertObjToStr(getTxtFromAmount()));
            obj.setRdoPercentOrFixed(CommonUtil.convertObjToStr(getRdoPercentOrFixed()));
            obj.setCCincrementAmt(CommonUtil.convertObjToStr(getTxtCCAllowanceEndingAmtValue()));
            //                }else{
            //                    double incAmt = CommonUtil.convertObjToDouble(tbmCCAllowance.getValueAt(i,2)).doubleValue();
            //                    double noOfInc = CommonUtil.convertObjToDouble(tbmCCAllowance.getValueAt(i,3)).doubleValue();
            //                    double sumAmt = incAmt * noOfInc;
            //                    lastRecAmt = lastRecAmt + sumAmt;
            //                    obj.setCCstartingScaleAmt(String.valueOf(sumAmt));
            //                    obj.setCCincrementAmt(String.valueOf(lastRecAmt));
            //                }
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowCCA(obj);
            CCAllowanceTOs.set(rowNo,obj);
            tbmCCAllowance.removeRow(rowNo);
            tbmCCAllowance.insertRow(rowNo,irRow);
            //            }
            tbmCCAllowance.fireTableDataChanged();
        }
        return obj;
    }
    
    //    private ArrayList setEditedCCARow(CCAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(getCboCCAllowance());
    //        row.add(CommonUtil.convertObjToStr(obj.getCCfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getCCstartingScaleAmt()));
    //        row.add(CommonUtil.convertObjToStr(obj.getCCincrementAmt()));
    //        return row;
    //    }
    private ArrayList setRowCCA(CCAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getCCfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtFromAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtToAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getRdoPercentOrFixed()));
        row.add(CommonUtil.convertObjToStr(obj.getCCstartingScaleAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
//        
//        row.add(CommonUtil.convertObjToStr(obj.getCCgrade()));
//        row.add(CommonUtil.convertObjToStr(obj.getCCfromDate()));
//        row.add(CommonUtil.convertObjToStr(obj.getCCstartingScaleAmt()));
//        row.add(CommonUtil.convertObjToStr(obj.getCCincrementAmt()));
//        row.add(CommonUtil.convertObjToStr(obj.getCCCityType()));
//        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    public void deleteCCAData(int rowNum) {
        deleteEnable = true;
        CCAllowanceTO obj = (CCAllowanceTO)CCAllowanceTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteCCAllowanceList== null){
            deleteCCAllowanceList = new ArrayList();
        }
        deleteCCAllowanceList.add(obj);
        CCAllowanceTOs.remove(rowNum);
        tbmCCAllowance.removeRow(rowNum);
        tbmCCAllowance.fireTableDataChanged();
        obj = null;
    }
    private ArrayList populateAndsetCCAARow(CCAllowanceTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getCCfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtFromAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtToAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getRdoPercentOrFixed()));
        row.add(CommonUtil.convertObjToStr(obj.getCCstartingScaleAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    //hra starts//
    public int insertHRAAData(int rowNo) {
        HRAllowanceTO obj = HRAllowanceTO(rowNo);
        if(rowNo == -1){
            HRAllowanceTOs.add(obj);
            ArrayList irRow = this.setRowHRA(obj);
            tbmHRAllowance.insertRow(tbmHRAllowance.getRowCount(), irRow);
            tbmHRAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    public void populateHRA(int rowNum) {
        HRAllowanceTO obj = (HRAllowanceTO)HRAllowanceTOs.get(rowNum);
        if(obj.getHRAPayable().equals("Y")){
            setRdoHRAPayable_Yes(true);
        }else{
            setRdoHRAPayable_No(true);
        }
        this.setCboHRAllowanceDesignation(obj.getHRAgrade());
        this.setCboHRAllowanceCityType(obj.getHRACityType());
        this.setTdtHRAllowanceFromDateValue(CommonUtil.convertObjToStr(obj.getHRAfromDate()));
        this.setTdtHRAllowanceToDateValue(CommonUtil.convertObjToStr(obj.getHRAtoDate()));
        this.setTxtHRAllowanceStartingAmtValue(obj.getHRAstartingScaleAmt());
        this.setTxtHRAllowanceEndingAmtValue(obj.getHRAincrementAmt());
        setHRAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private HRAllowanceTO HRAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        HRAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new HRAllowanceTO();
            if(tbmHRAllowance.getRowCount() == 0){
                obj.setHRAslNo(String.valueOf(1));
            }else if(tbmHRAllowance.getRowCount() >=1){
                obj.setHRAslNo(String.valueOf(tbmHRAllowance.getRowCount()+1));
            }
            obj.setHRAgrade(getCboHRAllowanceDesignation());
            obj.setHRACityType(getCboHRAllowanceCityType());
            obj.setHRAfromDate(DateUtil.getDateMMDDYYYY(getTdtHRAllowanceFromDateValue()));
            obj.setHRAtoDate(DateUtil.getDateMMDDYYYY(getTdtHRAllowanceToDateValue()));
            obj.setHRAstartingScaleAmt(CommonUtil.convertObjToStr(getTxtHRAllowanceStartingAmtValue()));
            obj.setHRAincrementAmt(CommonUtil.convertObjToStr(getTxtHRAllowanceEndingAmtValue()));
            if(getRdoHRAPayable_Yes() == true){
                obj.setHRAPayable("Y");
            }else {
                obj.setHRAPayable("N");
            }
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            //            int i = rowNo;
            //            for (;i<tbmHRAllowance.getRowCount();i++){
            obj = new HRAllowanceTO();
            if(rowNo == 0){
                obj.setHRAslNo(String.valueOf(1));
            }else{
                int no = rowNo + 1;
                obj.setHRAslNo(String.valueOf(no));
            }
            obj.setHRAgrade(getCboHRAllowanceDesignation());
            obj.setHRACityType(getCboHRAllowanceCityType());
            obj.setHRAfromDate(DateUtil.getDateMMDDYYYY(getTdtHRAllowanceFromDateValue()));
            obj.setHRAtoDate(DateUtil.getDateMMDDYYYY(getTdtHRAllowanceToDateValue()));
            if(getRdoHRAPayable_Yes() == true){
                obj.setHRAPayable("Y");
            }else {
                obj.setHRAPayable("N");
            }
            //                if(i == rowNo){
            obj.setHRAstartingScaleAmt(CommonUtil.convertObjToStr(getTxtHRAllowanceStartingAmtValue()));
            obj.setHRAincrementAmt(CommonUtil.convertObjToStr(getTxtHRAllowanceEndingAmtValue()));
            //                }else{
            //                    obj.setHRAstartingScaleAmt(CommonUtil.convertObjToStr(tbmHRAllowance.getValueAt(i,2)));
            //                    obj.setHRAincrementAmt(CommonUtil.convertObjToStr(tbmHRAllowance.getValueAt(i,3)));
            //                }
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowHRA(obj);
            HRAllowanceTOs.set(rowNo,obj);
            tbmHRAllowance.removeRow(rowNo);
            tbmHRAllowance.insertRow(rowNo,irRow);
            //            }
            tbmHRAllowance.fireTableDataChanged();
        }
        return obj;
    }
    private ArrayList setRowHRA(HRAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getHRAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAstartingScaleAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAincrementAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getHRACityType()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    //    private ArrayList setEditedHRARow(HRAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(getCboHRAllowanceDesignation()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getHRAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getHRAstartingScaleAmt()));
    //        row.add(CommonUtil.convertObjToStr(obj.getHRAincrementAmt()));
    //        return row;
    //    }
    public void deleteHRAData(int rowNum) {
        deleteEnable = true;
        HRAllowanceTO obj = (HRAllowanceTO)HRAllowanceTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deleteHRAllowanceList.add(obj);
        HRAllowanceTOs.remove(rowNum);
        tbmHRAllowance.removeRow(rowNum);
        tbmHRAllowance.fireTableDataChanged();
        obj = null;
    }
    private ArrayList populateAndsetHRAARow(HRAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getHRAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAstartingScaleAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getHRAincrementAmt()));
        row.add(CommonUtil.convertObjToStr(obj.getHRACityType()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    //=================================TA Starts from here=================================================================
    public int insertTAData(int rowNo) {
        TAllowanceTO obj = TAllowanceTO(rowNo);
        if(rowNo == -1){
            TAllowanceTOs.add(obj);
            ArrayList irRow = this.setRowTA(obj);
            tbmTAllowance.insertRow(tbmTAllowance.getRowCount(), irRow);
            tbmTAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateTA(int rowNum) {
        TAllowanceTO obj = (TAllowanceTO)TAllowanceTOs.get(rowNum);
        this.setCboTAllowanceDesgination(obj.getTAgrade());
        this.setTdtTAFromDateValue(CommonUtil.convertObjToStr(obj.getTAfromDate()));
        this.setTdtTAToDateValue(CommonUtil.convertObjToStr(obj.getTAtoDate()));
        
        if(obj.getTaType().equals("FIXED")){
            setChkFixedConveyance(true);
        }else if(obj.getTaType().equals("PETROL")){
            setChkPetrolAllowance(true);
        }
        setTxtBasicAmtUptoValue(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
        setTxtConveyancePerMonthValue(CommonUtil.convertObjToStr(obj.getTAConveyancePerMonth()));
        setTxtBasicAmtBeyondValue(CommonUtil.convertObjToStr(obj.getTaBasicAmtBeyond()));
        setTxtConveyanceAmtValue(CommonUtil.convertObjToStr(obj.getTAConveyanceAmt()));
        setTxtNooflitresValue(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
        setTxtPricePerlitreValue(CommonUtil.convertObjToStr(obj.getTAPricePerLitre()));
        setTxtTotalConveyanceAmtValue(CommonUtil.convertObjToStr(obj.getTATotalConveyanceAmt()));
        setTAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private TAllowanceTO TAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        TAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new TAllowanceTO();
            if(tbmTAllowance.getRowCount() == 0){
                obj.setTAslNo(String.valueOf(1));
            }else if(tbmTAllowance.getRowCount() >=1){
                obj.setTAslNo(String.valueOf(tbmTAllowance.getRowCount()+1));
            }
            obj.setTAgrade(getCboTAllowanceDesgination());
            obj.setTAfromDate(DateUtil.getDateMMDDYYYY(getTdtTAFromDateValue()));
            obj.setTAtoDate(DateUtil.getDateMMDDYYYY(getTdtTAToDateValue()));
            if(getChkFixedConveyance() == true){
                obj.setTaType("FIXED");
            }else if(getChkPetrolAllowance() == true){
                obj.setTaType("PETROL");
            }
            obj.setTaBasicAmtUpto(CommonUtil.convertObjToStr(getTxtBasicAmtUptoValue()));
            obj.setTAConveyancePerMonth(CommonUtil.convertObjToStr(getTxtConveyancePerMonthValue()));
            obj.setTaBasicAmtBeyond(CommonUtil.convertObjToStr(getTxtBasicAmtBeyondValue()));
            obj.setTAConveyanceAmt(CommonUtil.convertObjToStr(getTxtConveyanceAmtValue()));
            obj.setTANoOflitresPerMonth(CommonUtil.convertObjToStr(getTxtNooflitresValue()));
            obj.setTAPricePerLitre(CommonUtil.convertObjToStr(getTxtPricePerlitreValue()));
            obj.setTATotalConveyanceAmt(CommonUtil.convertObjToStr(getTxtTotalConveyanceAmtValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new TAllowanceTO();
            if(rowNo == 0){
                obj.setTAslNo(String.valueOf(1));
            }else{
                int no = rowNo + 1;
                obj.setTAslNo(String.valueOf(no));
            }
            obj.setTAgrade(getCboTAllowanceDesgination());
            obj.setTAfromDate(DateUtil.getDateMMDDYYYY(getTdtTAFromDateValue()));
            obj.setTAtoDate(DateUtil.getDateMMDDYYYY(getTdtTAToDateValue()));
            if(getChkFixedConveyance() == true){
                obj.setTaType("FIXED");
            }else if(getChkPetrolAllowance() == true){
                obj.setTaType("PETROL");
            }
            obj.setTaBasicAmtUpto(CommonUtil.convertObjToStr(getTxtBasicAmtUptoValue()));
            obj.setTAConveyancePerMonth(CommonUtil.convertObjToStr(getTxtConveyancePerMonthValue()));
            obj.setTaBasicAmtBeyond(CommonUtil.convertObjToStr(getTxtBasicAmtBeyondValue()));
            obj.setTAConveyanceAmt(CommonUtil.convertObjToStr(getTxtConveyanceAmtValue()));
            obj.setTANoOflitresPerMonth(CommonUtil.convertObjToStr(getTxtNooflitresValue()));
            obj.setTAPricePerLitre(CommonUtil.convertObjToStr(getTxtPricePerlitreValue()));
            obj.setTATotalConveyanceAmt(CommonUtil.convertObjToStr(getTxtTotalConveyanceAmtValue()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowTA(obj);
            TAllowanceTOs.set(rowNo,obj);
            tbmTAllowance.removeRow(rowNo);
            tbmTAllowance.insertRow(rowNo,irRow);
            tbmTAllowance.fireTableDataChanged();
        }
        return obj;
    }
    
    //    private ArrayList setEditedTARow(TAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(getCboTAllowanceDesgination()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTAConveyanceAmt()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
    //        return row;
    //    }
    
    private ArrayList setRowTA(TAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getTAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    public void deleteTAData(int rowNum) {
        deleteEnable = true;
        TAllowanceTO obj = (TAllowanceTO)TAllowanceTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deleteTAllowanceList.add(obj);
        TAllowanceTOs.remove(rowNum);
        tbmTAllowance.removeRow(rowNum);
        tbmTAllowance.fireTableDataChanged();
        obj = null;
    }
    private ArrayList populateAndsetTARow(TAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getTAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    //=================================================================MA starts=====================================================
    public int insertMAData(int rowNo) {
        MAllowanceTO obj = MAllowanceTO(rowNo);
        if(rowNo == -1){
            MAllowanceTOs.add(obj);
            ArrayList irRow = this.setRowMA(obj);
            tbmMAllowance.insertRow(tbmMAllowance.getRowCount(), irRow);
            tbmMAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateMA(int rowNum) {
        MAllowanceTO obj = (MAllowanceTO)MAllowanceTOs.get(rowNum);
        this.setCboMAidDesg(obj.getMAgrade());
        this.setTdtMAidFromDateValue(CommonUtil.convertObjToStr(obj.getMAfromDate()));
        this.setTdtMAidToDateValue(CommonUtil.convertObjToStr(obj.getMAtoDate()));
        this.setTxtMAidAmtValue(obj.getMAAmount());
        setMAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private MAllowanceTO MAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        MAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new MAllowanceTO();
            if(tbmMAllowance.getRowCount() == 0){
                obj.setMAslNo(String.valueOf(1));
            }else if(tbmMAllowance.getRowCount() >=1){
                obj.setMAslNo(String.valueOf(tbmMAllowance.getRowCount()+1));
            }
            obj.setMAgrade(getCboMAidDesg());
            obj.setMAfromDate(DateUtil.getDateMMDDYYYY(getTdtMAidFromDateValue()));
            obj.setMAtoDate(DateUtil.getDateMMDDYYYY(getTdtMAidToDateValue()));
            obj.setMAAmount(getTxtMAidAmtValue());
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new MAllowanceTO();
            if(rowNo == 0){
                obj.setMAslNo(String.valueOf(1));
            }else{
                int no = rowNo + 1;
                obj.setMAslNo(String.valueOf(no));
            }
            obj.setMAgrade(getCboMAidDesg());
            obj.setMAfromDate(DateUtil.getDateMMDDYYYY(getTdtMAidFromDateValue()));
            obj.setMAtoDate(DateUtil.getDateMMDDYYYY(getTdtMAidToDateValue()));
            obj.setMAAmount(getTxtMAidAmtValue());
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowMA(obj);
            MAllowanceTOs.set(rowNo,obj);
            tbmMAllowance.removeRow(rowNo);
            tbmMAllowance.insertRow(rowNo,irRow);
            tbmMAllowance.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowMA(MAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    //    private ArrayList setEditedMARow(MAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
    //        return row;
    //    }
    
    public void deleteMAData(int rowNum) {
        deleteEnable = true;
        MAllowanceTO obj = (MAllowanceTO)MAllowanceTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deleteMAllowanceList.add(obj);
        MAllowanceTOs.remove(rowNum);
        tbmMAllowance.removeRow(rowNum);
        tbmMAllowance.fireTableDataChanged();
        obj = null;
    }
    
    private ArrayList populateAndsetMARow(MAllowanceTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
        row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    //=================================================================OA Starts===================================================
    
    public int insertOAData(int rowNo) {
        OtherAllowanceTO obj = OtherAllowanceTO(rowNo);
        if(rowNo == -1){
            OAllowanceTOs.add(obj);
            ArrayList irRow = this.setRowOA(obj);
            tbmOAllowance.insertRow(tbmOAllowance.getRowCount(), irRow);
            tbmOAllowance.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateOA(int rowNum) {
        OtherAllowanceTO obj = (OtherAllowanceTO)OAllowanceTOs.get(rowNum);
        setCboOADesignationValue(obj.getOAgrade());
        setCboOAllowanceTypeValue(obj.getOAllowanceType());
        setCboOAParameterBasedOnValue(obj.getOAParameterBasedOn());
        setCboOASubParameterValue(obj.getOASubParameter());
        if(obj.getOAType()!=null && obj.getOAType().equals("FIXED")){
            setChkOAFixedValue(true);
            setChkOAPercentageValue(false);
        }else if(obj.getOAType()!=null && obj.getOAType().equals("PERCENTAGE")){
            setChkOAPercentageValue(true);
            setChkOAFixedValue(false);
        }
        if(obj.getOAbasedOnParameter() != null && obj.getOAbasedOnParameter().equals("Y")){
            setOAbasedOnParameter("Y");
        }else if(obj.getOAbasedOnParameter() != null && obj.getOAbasedOnParameter().equals("N")){
            setOAbasedOnParameter("N");
        }
        setTdtOAFromDateValue(CommonUtil.convertObjToStr(obj.getOAfromDate()));
        setTdtOAToDateValue(CommonUtil.convertObjToStr(obj.getOAtoDate()));
        setTxtOAFixedAmtValue(CommonUtil.convertObjToStr(obj.getOAFixedAmount()));
        setTxtOAPercentageValue(CommonUtil.convertObjToStr(obj.getOAPercentageValue()));
        setTxtOAMaximumOfValue(CommonUtil.convertObjToStr(obj.getOAMaximumPerAmt()));
        setOAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private OtherAllowanceTO OtherAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        OtherAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new OtherAllowanceTO();
            if(tbmOAllowance.getRowCount() == 0){
                obj.setOAslNo(new Double(1));
            }else if(tbmOAllowance.getRowCount() >=1){
                obj.setOAslNo(new Double(tbmOAllowance.getRowCount() + 1));
            }
            obj.setOAgrade(getCboOADesignationValue());
            if(getChkOAFixedValue() == true){
                obj.setOAType("FIXED");
            }else if(getChkOAPercentageValue() == true){
                obj.setOAType("PERCENTAGE");
            }
            obj.setOAbasedOnParameter(CommonUtil.convertObjToStr(getOAbasedOnParameter())); 
            obj.setOAllowanceType(CommonUtil.convertObjToStr(getCboOAllowanceTypeValue()));
            obj.setOAParameterBasedOn(CommonUtil.convertObjToStr(getCboOAParameterBasedOnValue()));
            obj.setOASubParameter(CommonUtil.convertObjToStr(getCboOASubParameterValue()));
            obj.setOAfromDate(DateUtil.getDateMMDDYYYY(getTdtOAFromDateValue()));
            obj.setOAtoDate(DateUtil.getDateMMDDYYYY(getTdtOAToDateValue()));
            obj.setOAFixedAmount(CommonUtil.convertObjToDouble(getTxtOAFixedAmtValue()));
            obj.setOAPercentageValue(CommonUtil.convertObjToDouble(getTxtOAPercentageValue()));
            obj.setOAMaximumPerAmt(CommonUtil.convertObjToDouble(getTxtOAMaximumOfValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new OtherAllowanceTO();
            if(rowNo == 0){
                obj.setOAslNo(new Double(1));
            }else{
                int no = rowNo + 1;
                obj.setOAslNo(new Double(no));
            }
            //                obj.setOAslNo(new Double(rowNo));
            obj.setOAgrade(getCboOADesignationValue());
            if(getChkOAFixedValue() == true){
                obj.setOAType("FIXED");
            }else if(getChkOAPercentageValue() == true){
                obj.setOAType("PERCENTAGE");
            }
            obj.setOAbasedOnParameter(CommonUtil.convertObjToStr(getOAbasedOnParameter()));
            obj.setOAllowanceType(CommonUtil.convertObjToStr(getCboOAllowanceTypeValue()));
            obj.setOAParameterBasedOn(CommonUtil.convertObjToStr(getCboOAParameterBasedOnValue()));
            obj.setOASubParameter(CommonUtil.convertObjToStr(getCboOASubParameterValue()));
            obj.setOAfromDate(DateUtil.getDateMMDDYYYY(getTdtOAFromDateValue()));
            obj.setOAtoDate(DateUtil.getDateMMDDYYYY(getTdtOAToDateValue()));
            obj.setOAFixedAmount(CommonUtil.convertObjToDouble(getTxtOAFixedAmtValue()));
            obj.setOAPercentageValue(CommonUtil.convertObjToDouble(getTxtOAPercentageValue()));
            obj.setOAMaximumPerAmt(CommonUtil.convertObjToDouble(getTxtOAMaximumOfValue()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowOA(obj);
            OAllowanceTOs.set(rowNo,obj);
            tbmOAllowance.removeRow(rowNo);
            tbmOAllowance.insertRow(rowNo,irRow);
            tbmOAllowance.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowOA(OtherAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(DateUtil.getDateMMDDYYYY(getTdtOAFromDateValue()));
        row.add(CommonUtil.convertObjToStr(getCboOAllowanceTypeValue()));
        row.add(CommonUtil.convertObjToStr(getCboOAParameterBasedOnValue()));
        row.add(CommonUtil.convertObjToStr(getCboOASubParameterValue()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    //    private ArrayList setEditedMARow(MAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
    //        return row;
    //    }
    
    public void deleteOAData(int rowNum) {
        deleteEnable = true;
        OtherAllowanceTO obj = (OtherAllowanceTO)OAllowanceTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteOAllowanceList == null || deleteOAllowanceList.size() == 0){
            deleteOAllowanceList = new ArrayList();
        }
        deleteOAllowanceList.add(obj);
        System.out.println("!@#$@#$OAllowanceTOs"+OAllowanceTOs);
        System.out.println("!@#$@#deleteOAllowanceList"+deleteOAllowanceList);
        OAllowanceTOs.remove(rowNum);
        System.out.println("!@#$@#$after removing OAllowanceTOs"+OAllowanceTOs);
        tbmOAllowance.removeRow(rowNum);
        tbmOAllowance.fireTableDataChanged();
        obj = null;
    }
    
    private ArrayList populateAndsetOARow(OtherAllowanceTO obj){
        ArrayList row= new ArrayList();
        row.add(obj.getOAfromDate());
        row.add(obj.getOAllowanceType());
        row.add(obj.getOAParameterBasedOn());
        row.add(obj.getOASubParameter());
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    /**
     * Getter for property cbmSalaryStructureProdId.
     * @return Value of property cbmSalaryStructureProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSalaryStructureProdId() {
        return cbmSalaryStructureProdId;
    }
    
    /**
     * Setter for property cbmSalaryStructureProdId.
     * @param cbmSalaryStructureProdId New value of property cbmSalaryStructureProdId.
     */
    public void setCbmSalaryStructureProdId(com.see.truetransact.clientutil.ComboBoxModel cbmSalaryStructureProdId) {
        this.cbmSalaryStructureProdId = cbmSalaryStructureProdId;
    }
    
    /**
     * Getter for property lblSalaryStructureSLNOValue.
     * @return Value of property lblSalaryStructureSLNOValue.
     */
    public java.lang.String getLblSalaryStructureSLNOValue() {
        return lblSalaryStructureSLNOValue;
    }
    
    /**
     * Setter for property lblSalaryStructureSLNOValue.
     * @param lblSalaryStructureSLNOValue New value of property lblSalaryStructureSLNOValue.
     */
    public void setLblSalaryStructureSLNOValue(java.lang.String lblSalaryStructureSLNOValue) {
        this.lblSalaryStructureSLNOValue = lblSalaryStructureSLNOValue;
    }
    
    /**
     * Getter for property lblSalaryStructureFromDateValue.
     * @return Value of property lblSalaryStructureFromDateValue.
     */
    public java.lang.String getLblSalaryStructureFromDateValue() {
        return lblSalaryStructureFromDateValue;
    }
    
    /**
     * Setter for property lblSalaryStructureFromDateValue.
     * @param lblSalaryStructureFromDateValue New value of property lblSalaryStructureFromDateValue.
     */
    public void setLblSalaryStructureFromDateValue(java.lang.String lblSalaryStructureFromDateValue) {
        this.lblSalaryStructureFromDateValue = lblSalaryStructureFromDateValue;
    }
    
    /**
     * Getter for property lblSalaryStructureToDateValue.
     * @return Value of property lblSalaryStructureToDateValue.
     */
    public java.lang.String getLblSalaryStructureToDateValue() {
        return lblSalaryStructureToDateValue;
    }
    
    /**
     * Setter for property lblSalaryStructureToDateValue.
     * @param lblSalaryStructureToDateValue New value of property lblSalaryStructureToDateValue.
     */
    public void setLblSalaryStructureToDateValue(java.lang.String lblSalaryStructureToDateValue) {
        this.lblSalaryStructureToDateValue = lblSalaryStructureToDateValue;
    }
    
    /**
     * Getter for property txtSalaryStructureStartingAmtValue.
     * @return Value of property txtSalaryStructureStartingAmtValue.
     */
    public java.lang.String getTxtSalaryStructureStartingAmtValue() {
        return txtSalaryStructureStartingAmtValue;
    }
    
    /**
     * Setter for property txtSalaryStructureStartingAmtValue.
     * @param txtSalaryStructureStartingAmtValue New value of property txtSalaryStructureStartingAmtValue.
     */
    public void setTxtSalaryStructureStartingAmtValue(java.lang.String txtSalaryStructureStartingAmtValue) {
        this.txtSalaryStructureStartingAmtValue = txtSalaryStructureStartingAmtValue;
    }
    
    /**
     * Getter for property txtSalaryStructureAmtValue.
     * @return Value of property txtSalaryStructureAmtValue.
     */
    public java.lang.String getTxtSalaryStructureAmtValue() {
        return txtSalaryStructureAmtValue;
    }
    
    /**
     * Setter for property txtSalaryStructureAmtValue.
     * @param txtSalaryStructureAmtValue New value of property txtSalaryStructureAmtValue.
     */
    public void setTxtSalaryStructureAmtValue(java.lang.String txtSalaryStructureAmtValue) {
        this.txtSalaryStructureAmtValue = txtSalaryStructureAmtValue;
    }
    
    /**
     * Getter for property txtSalaryStructureIncYearValue.
     * @return Value of property txtSalaryStructureIncYearValue.
     */
    public java.lang.String getTxtSalaryStructureIncYearValue() {
        return txtSalaryStructureIncYearValue;
    }
    
    /**
     * Setter for property txtSalaryStructureIncYearValue.
     * @param txtSalaryStructureIncYearValue New value of property txtSalaryStructureIncYearValue.
     */
    public void setTxtSalaryStructureIncYearValue(java.lang.String txtSalaryStructureIncYearValue) {
        this.txtSalaryStructureIncYearValue = txtSalaryStructureIncYearValue;
    }
    
    /**
     * Getter for property cboSalaryStructureProdId.
     * @return Value of property cboSalaryStructureProdId.
     */
    public java.lang.String getCboSalaryStructureProdId() {
        return cboSalaryStructureProdId;
    }
    
    /**
     * Setter for property cboSalaryStructureProdId.
     * @param cboSalaryStructureProdId New value of property cboSalaryStructureProdId.
     */
    public void setCboSalaryStructureProdId(java.lang.String cboSalaryStructureProdId) {
        this.cboSalaryStructureProdId = cboSalaryStructureProdId;
    }
    
    /**
     * Getter for property txtSalaryStructureEndingAmtValue.
     * @return Value of property txtSalaryStructureEndingAmtValue.
     */
    public java.lang.String getTxtSalaryStructureEndingAmtValue() {
        return txtSalaryStructureEndingAmtValue;
    }
    
    /**
     * Setter for property txtSalaryStructureEndingAmtValue.
     * @param txtSalaryStructureEndingAmtValue New value of property txtSalaryStructureEndingAmtValue.
     */
    public void setTxtSalaryStructureEndingAmtValue(java.lang.String txtSalaryStructureEndingAmtValue) {
        this.txtSalaryStructureEndingAmtValue = txtSalaryStructureEndingAmtValue;
    }
    
    /**
     * Getter for property txtSalaryStructureSingleRowAmt.
     * @return Value of property txtSalaryStructureSingleRowAmt.
     */
    public java.lang.String getTxtSalaryStructureSingleRowAmt() {
        return txtSalaryStructureSingleRowAmt;
    }
    
    /**
     * Setter for property txtSalaryStructureSingleRowAmt.
     * @param txtSalaryStructureSingleRowAmt New value of property txtSalaryStructureSingleRowAmt.
     */
    public void setTxtSalaryStructureSingleRowAmt(java.lang.String txtSalaryStructureSingleRowAmt) {
        this.txtSalaryStructureSingleRowAmt = txtSalaryStructureSingleRowAmt;
    }
    
    /**
     * Getter for property cbmSalaryStructureStagnationOnceIn.
     * @return Value of property cbmSalaryStructureStagnationOnceIn.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSalaryStructureStagnationOnceIn() {
        return cbmSalaryStructureStagnationOnceIn;
    }
    
    /**
     * Setter for property cbmSalaryStructureStagnationOnceIn.
     * @param cbmSalaryStructureStagnationOnceIn New value of property cbmSalaryStructureStagnationOnceIn.
     */
    public void setCbmSalaryStructureStagnationOnceIn(com.see.truetransact.clientutil.ComboBoxModel cbmSalaryStructureStagnationOnceIn) {
        this.cbmSalaryStructureStagnationOnceIn = cbmSalaryStructureStagnationOnceIn;
    }
    
    /**
     * Getter for property cboSalaryStructureStagnationOnceIn.
     * @return Value of property cboSalaryStructureStagnationOnceIn.
     */
    public java.lang.String getCboSalaryStructureStagnationOnceIn() {
        return cboSalaryStructureStagnationOnceIn;
    }
    
    /**
     * Setter for property cboSalaryStructureStagnationOnceIn.
     * @param cboSalaryStructureStagnationOnceIn New value of property cboSalaryStructureStagnationOnceIn.
     */
    public void setCboSalaryStructureStagnationOnceIn(java.lang.String cboSalaryStructureStagnationOnceIn) {
        this.cboSalaryStructureStagnationOnceIn = cboSalaryStructureStagnationOnceIn;
    }
    
    /**
     * Getter for property rdoStagnationIncrement_Yes.
     * @return Value of property rdoStagnationIncrement_Yes.
     */
    public boolean getRdoStagnationIncrement_Yes() {
        return rdoStagnationIncrement_Yes;
    }
    
    /**
     * Setter for property rdoStagnationIncrement_Yes.
     * @param rdoStagnationIncrement_Yes New value of property rdoStagnationIncrement_Yes.
     */
    public void setRdoStagnationIncrement_Yes(boolean rdoStagnationIncrement_Yes) {
        this.rdoStagnationIncrement_Yes = rdoStagnationIncrement_Yes;
    }
    
    /**
     * Getter for property rdoStagnationIncrement_No.
     * @return Value of property rdoStagnationIncrement_No.
     */
    public boolean getRdoStagnationIncrement_No() {
        return rdoStagnationIncrement_No;
    }
    
    /**
     * Setter for property rdoStagnationIncrement_No.
     * @param rdoStagnationIncrement_No New value of property rdoStagnationIncrement_No.
     */
    public void setRdoStagnationIncrement_No(boolean rdoStagnationIncrement_No) {
        this.rdoStagnationIncrement_No = rdoStagnationIncrement_No;
    }
    
    /**
     * Getter for property txtSalaryStructureStagnationAmtValue.
     * @return Value of property txtSalaryStructureStagnationAmtValue.
     */
    public java.lang.String getTxtSalaryStructureStagnationAmtValue() {
        return txtSalaryStructureStagnationAmtValue;
    }
    
    /**
     * Setter for property txtSalaryStructureStagnationAmtValue.
     * @param txtSalaryStructureStagnationAmtValue New value of property txtSalaryStructureStagnationAmtValue.
     */
    public void setTxtSalaryStructureStagnationAmtValue(java.lang.String txtSalaryStructureStagnationAmtValue) {
        this.txtSalaryStructureStagnationAmtValue = txtSalaryStructureStagnationAmtValue;
    }
    
    /**
     * Getter for property txtSalaryStructureNoOfStagnationValue.
     * @return Value of property txtSalaryStructureNoOfStagnationValue.
     */
    public java.lang.String getTxtSalaryStructureNoOfStagnationValue() {
        return txtSalaryStructureNoOfStagnationValue;
    }
    
    /**
     * Setter for property txtSalaryStructureNoOfStagnationValue.
     * @param txtSalaryStructureNoOfStagnationValue New value of property txtSalaryStructureNoOfStagnationValue.
     */
    public void setTxtSalaryStructureNoOfStagnationValue(java.lang.String txtSalaryStructureNoOfStagnationValue) {
        this.txtSalaryStructureNoOfStagnationValue = txtSalaryStructureNoOfStagnationValue;
    }
    
    /**
     * Getter for property txtSalaryStructureStagnationOnceInValue.
     * @return Value of property txtSalaryStructureStagnationOnceInValue.
     */
    public java.lang.String getTxtSalaryStructureStagnationOnceInValue() {
        return txtSalaryStructureStagnationOnceInValue;
    }
    
    /**
     * Setter for property txtSalaryStructureStagnationOnceInValue.
     * @param txtSalaryStructureStagnationOnceInValue New value of property txtSalaryStructureStagnationOnceInValue.
     */
    public void setTxtSalaryStructureStagnationOnceInValue(java.lang.String txtSalaryStructureStagnationOnceInValue) {
        this.txtSalaryStructureStagnationOnceInValue = txtSalaryStructureStagnationOnceInValue;
    }
    
    /**
     * Getter for property txtSalaryStructureTotNoIncValue.
     * @return Value of property txtSalaryStructureTotNoIncValue.
     */
    public java.lang.String getTxtSalaryStructureTotNoIncValue() {
        return txtSalaryStructureTotNoIncValue;
    }
    
    /**
     * Setter for property txtSalaryStructureTotNoIncValue.
     * @param txtSalaryStructureTotNoIncValue New value of property txtSalaryStructureTotNoIncValue.
     */
    public void setTxtSalaryStructureTotNoIncValue(java.lang.String txtSalaryStructureTotNoIncValue) {
        this.txtSalaryStructureTotNoIncValue = txtSalaryStructureTotNoIncValue;
    }
    
    /**
     * Getter for property cbmDAValue.
     * @return Value of property cbmDAValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDAValue() {
        return cbmDAValue;
    }
    
    /**
     * Setter for property cbmDAValue.
     * @param cbmDADesignationValue New value of property cbmDAValue.
     */
    public void setCbmDAValue(com.see.truetransact.clientutil.ComboBoxModel cbmDAValue) {
        this.cbmDAValue = cbmDAValue;
    }
    
    /**
     * Getter for property tbmDAllowance.
     * @return Value of property tbmDAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmDAllowance() {
        return tbmDAllowance;
    }
    
    /**
     * Setter for property tbmDAllowance.
     * @param tbmDAllowance New value of property tbmDAllowance.
     */
    public void setTbmDAllowance(com.see.truetransact.clientutil.TableModel tbmDAllowance) {
        this.tbmDAllowance = tbmDAllowance;
    }
    
    /**
     * Getter for property cbmCCAllowance.
     * @return Value of property cbmCCAllowance.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCCAllowance() {
        return cbmCCAllowance;
    }
    
    /**
     * Setter for property cbmCCAllowance.
     * @param cbmCCAllowance New value of property cbmCCAllowance.
     */
    public void setCbmCCAllowance(com.see.truetransact.clientutil.ComboBoxModel cbmCCAllowance) {
        this.cbmCCAllowance = cbmCCAllowance;
    }
    
    /**
     * Getter for property tbmCCAllowance.
     * @return Value of property tbmCCAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmCCAllowance() {
        return tbmCCAllowance;
    }
    
    /**
     * Setter for property tbmCCAllowance.
     * @param tbmCCAllowance New value of property tbmCCAllowance.
     */
    public void setTbmCCAllowance(com.see.truetransact.clientutil.TableModel tbmCCAllowance) {
        this.tbmCCAllowance = tbmCCAllowance;
    }
    
    /**
     * Getter for property cbmHRAllowanceDesignation.
     * @return Value of property cbmHRAllowanceDesignation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHRAllowanceDesignation() {
        return cbmHRAllowance;
    }
    
    /**
     * Setter for property cbmHRAllowance.
     * @param cbmHRAllowanceDesignation New value of property cbmHRAllowance.
     */
    public void setCbmHRAllowance(com.see.truetransact.clientutil.ComboBoxModel cbmHRAllowance) {
        this.cbmHRAllowance = cbmHRAllowance;
    }
    
    /**
     * Getter for property tbmHRAllowance.
     * @return Value of property tbmHRAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmHRAllowance() {
        return tbmHRAllowance;
    }
    
    /**
     * Setter for property tbmHRAllowance.
     * @param tbmHRAllowance New value of property tbmHRAllowance.
     */
    public void setTbmHRAllowance(com.see.truetransact.clientutil.TableModel tbmHRAllowance) {
        this.tbmHRAllowance = tbmHRAllowance;
    }
    
    /**
     * Getter for property cboDADesignationValue.
     * @return Value of property cboDADesignationValue.
     */
    public java.lang.String getCboDADesignationValue() {
        return cboDADesignationValue;
    }
    
    /**
     * Setter for property cboDADesignationValue.
     * @param cboDADesignationValue New value of property cboDADesignationValue.
     */
    public void setCboDADesignationValue(java.lang.String cboDADesignationValue) {
        this.cboDADesignationValue = cboDADesignationValue;
    }
    
    /**
     * Getter for property cboCCAllowance.
     * @return Value of property cboCCAllowance.
     */
    public java.lang.String getCboCCAllowance() {
        return cboCCAllowance;
    }
    
    /**
     * Setter for property cboCCAllowance.
     * @param cboCCAllowance New value of property cboCCAllowance.
     */
    public void setCboCCAllowance(java.lang.String cboCCAllowance) {
        this.cboCCAllowance = cboCCAllowance;
    }
    
    /**
     * Getter for property cboHRAllowanceDesignation.
     * @return Value of property cboHRAllowanceDesignation.
     */
    public java.lang.String getCboHRAllowanceDesignation() {
        return cboHRAllowanceDesignation;
    }
    
    /**
     * Setter for property cboHRAllowanceDesignation.
     * @param cboHRAllowanceDesignation New value of property cboHRAllowanceDesignation.
     */
    public void setCboHRAllowanceDesignation(java.lang.String cboHRAllowanceDesignation) {
        this.cboHRAllowanceDesignation = cboHRAllowanceDesignation;
    }
    
    /**
     * Getter for property cboCCAllowanceCityType.
     * @return Value of property cboCCAllowanceCityType.
     */
    public java.lang.String getCboCCAllowanceCityType() {
        return cboCCAllowanceCityType;
    }
    
    /**
     * Setter for property cboCCAllowanceCityType.
     * @param cboCCAllowanceCityType New value of property cboCCAllowanceCityType.
     */
    public void setCboCCAllowanceCityType(java.lang.String cboCCAllowanceCityType) {
        this.cboCCAllowanceCityType = cboCCAllowanceCityType;
    }
    
    /**
     * Getter for property cboHRAllowanceCityType.
     * @return Value of property cboHRAllowanceCityType.
     */
    public java.lang.String getCboHRAllowanceCityType() {
        return cboHRAllowanceCityType;
    }
    
    /**
     * Setter for property cboHRAllowanceCityType.
     * @param cboHRAllowanceCityType New value of property cboHRAllowanceCityType.
     */
    public void setCboHRAllowanceCityType(java.lang.String cboHRAllowanceCityType) {
        this.cboHRAllowanceCityType = cboHRAllowanceCityType;
    }
    
    /**
     * Getter for property cbmCCAllowanceCityType.
     * @return Value of property cbmCCAllowanceCityType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCCAllowanceCityType() {
        return cbmCCAllowanceCityType;
    }
    
    /**
     * Setter for property cbmCCAllowanceCityType.
     * @param cbmCCAllowanceCityType New value of property cbmCCAllowanceCityType.
     */
    public void setCbmCCAllowanceCityType(com.see.truetransact.clientutil.ComboBoxModel cbmCCAllowanceCityType) {
        this.cbmCCAllowanceCityType = cbmCCAllowanceCityType;
    }
    
    /**
     * Getter for property cbmHRAllowanceCityType.
     * @return Value of property cbmHRAllowanceCityType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHRAllowanceCityType() {
        return cbmHRAllowanceCityType;
    }
    
    /**
     * Setter for property cbmHRAllowanceCityType.
     * @param cbmHRAllowanceCityType New value of property cbmHRAllowanceCityType.
     */
    public void setCbmHRAllowanceCityType(com.see.truetransact.clientutil.ComboBoxModel cbmHRAllowanceCityType) {
        this.cbmHRAllowanceCityType = cbmHRAllowanceCityType;
    }
    
    /**
     * Getter for property lblDAFromDateValue.
     * @return Value of property lblDAFromDateValue.
     */
    public java.lang.String getTdtDAFromDateValue() {
        return tdtDAFromDateValue;
    }
    
    /**
     * Setter for property lblDAFromDateValue.
     * @param lblDAFromDateValue New value of property lblDAFromDateValue.
     */
    public void setTdtDAFromDateValue(java.lang.String tdtDAFromDateValue) {
        this.tdtDAFromDateValue = tdtDAFromDateValue;
    }
    
    /**
     * Getter for property lblDAToDateValue.
     * @return Value of property lblDAToDateValue.
     */
    public java.lang.String getTdtDAToDateValue() {
        return tdtDAToDateValue;
    }
    
    /**
     * Setter for property lblDAToDateValue.
     * @param lblDAToDateValue New value of property lblDAToDateValue.
     */
    public void setTdtDAToDateValue(java.lang.String tdtDAToDateValue) {
        this.tdtDAToDateValue = tdtDAToDateValue;
    }
    
    /**
     * Getter for property lblCCAllowanceFromDateValue.
     * @return Value of property lblCCAllowanceFromDateValue.
     */
    public java.lang.String getTdtCCAllowanceFromDateValue() {
        return tdtCCAllowanceFromDateValue;
    }
    
    /**
     * Setter for property lblCCAllowanceFromDateValue.
     * @param lblCCAllowanceFromDateValue New value of property lblCCAllowanceFromDateValue.
     */
    public void setTdtCCAllowanceFromDateValue(java.lang.String tdtCCAllowanceFromDateValue) {
        this.tdtCCAllowanceFromDateValue = tdtCCAllowanceFromDateValue;
    }
    
    /**
     * Getter for property lblCCAllowanceToDateValue.
     * @return Value of property lblCCAllowanceToDateValue.
     */
    public java.lang.String getTdtCCAllowanceToDateValue() {
        return tdtCCAllowanceToDateValue;
    }
    
    /**
     * Setter for property lblCCAllowanceToDateValue.
     * @param lblCCAllowanceToDateValue New value of property lblCCAllowanceToDateValue.
     */
    public void setTdtCCAllowanceToDateValue(java.lang.String tdtCCAllowanceToDateValue) {
        this.tdtCCAllowanceToDateValue = tdtCCAllowanceToDateValue;
    }
    
    /**
     * Getter for property txtCCAllowanceStartingAmtValue.
     * @return Value of property txtCCAllowanceStartingAmtValue.
     */
    public java.lang.String getTxtCCAllowanceStartingAmtValue() {
        return txtCCAllowanceStartingAmtValue;
    }
    
    /**
     * Setter for property txtCCAllowanceStartingAmtValue.
     * @param txtCCAllowanceStartingAmtValue New value of property txtCCAllowanceStartingAmtValue.
     */
    public void setTxtCCAllowanceStartingAmtValue(java.lang.String txtCCAllowanceStartingAmtValue) {
        this.txtCCAllowanceStartingAmtValue = txtCCAllowanceStartingAmtValue;
    }
    
    /**
     * Getter for property txtCCAllowanceEndingAmtValue.
     * @return Value of property txtCCAllowanceEndingAmtValue.
     */
    public java.lang.String getTxtCCAllowanceEndingAmtValue() {
        return txtCCAllowanceEndingAmtValue;
    }
    
    /**
     * Setter for property txtCCAllowanceEndingAmtValue.
     * @param txtCCAllowanceEndingAmtValue New value of property txtCCAllowanceEndingAmtValue.
     */
    public void setTxtCCAllowanceEndingAmtValue(java.lang.String txtCCAllowanceEndingAmtValue) {
        this.txtCCAllowanceEndingAmtValue = txtCCAllowanceEndingAmtValue;
    }
    
    /**
     * Getter for property lblHRAllowanceFromDateValue.
     * @return Value of property lblHRAllowanceFromDateValue.
     */
    public java.lang.String getTdtHRAllowanceFromDateValue() {
        return tdtHRAllowanceFromDateValue;
    }
    
    /**
     * Setter for property lblHRAllowanceFromDateValue.
     * @param lblHRAllowanceFromDateValue New value of property lblHRAllowanceFromDateValue.
     */
    public void setTdtHRAllowanceFromDateValue(java.lang.String tdtHRAllowanceFromDateValue) {
        this.tdtHRAllowanceFromDateValue = tdtHRAllowanceFromDateValue;
    }
    
    /**
     * Getter for property lblHRAllowanceToDateValue.
     * @return Value of property lblHRAllowanceToDateValue.
     */
    public java.lang.String getTdtHRAllowanceToDateValue() {
        return tdtHRAllowanceToDateValue;
    }
    
    /**
     * Setter for property lblHRAllowanceToDateValue.
     * @param lblHRAllowanceToDateValue New value of property lblHRAllowanceToDateValue.
     */
    public void setTdtHRAllowanceToDateValue(java.lang.String tdtHRAllowanceToDateValue) {
        this.tdtHRAllowanceToDateValue = tdtHRAllowanceToDateValue;
    }
    
    /**
     * Getter for property txtHRAllowanceStartingAmtValue.
     * @return Value of property txtHRAllowanceStartingAmtValue.
     */
    public java.lang.String getTxtHRAllowanceStartingAmtValue() {
        return txtHRAllowanceStartingAmtValue;
    }
    
    /**
     * Setter for property txtHRAllowanceStartingAmtValue.
     * @param txtHRAllowanceStartingAmtValue New value of property txtHRAllowanceStartingAmtValue.
     */
    public void setTxtHRAllowanceStartingAmtValue(java.lang.String txtHRAllowanceStartingAmtValue) {
        this.txtHRAllowanceStartingAmtValue = txtHRAllowanceStartingAmtValue;
    }
    
    /**
     * Getter for property txtHRAllowanceEndingAmtValue.
     * @return Value of property txtHRAllowanceEndingAmtValue.
     */
    public java.lang.String getTxtHRAllowanceEndingAmtValue() {
        return txtHRAllowanceEndingAmtValue;
    }
    
    /**
     * Setter for property txtHRAllowanceEndingAmtValue.
     * @param txtHRAllowanceEndingAmtValue New value of property txtHRAllowanceEndingAmtValue.
     */
    public void setTxtHRAllowanceEndingAmtValue(java.lang.String txtHRAllowanceEndingAmtValue) {
        this.txtHRAllowanceEndingAmtValue = txtHRAllowanceEndingAmtValue;
    }
    
    /**
     * Getter for property cboTAllowanceDesgination.
     * @return Value of property cboTAllowanceDesgination.
     */
    public java.lang.String getCboTAllowanceDesgination() {
        return cboTAllowanceDesgination;
    }
    
    /**
     * Setter for property cboTAllowanceDesgination.
     * @param cboTAllowanceDesgination New value of property cboTAllowanceDesgination.
     */
    public void setCboTAllowanceDesgination(java.lang.String cboTAllowanceDesgination) {
        this.cboTAllowanceDesgination = cboTAllowanceDesgination;
    }
    
    /**
     * Getter for property cboTAllowanceCityType.
     * @return Value of property cboTAllowanceCityType.
     */
    public java.lang.String getCboTAllowanceCityType() {
        return cboTAllowanceCityType;
    }
    
    /**
     * Setter for property cboTAllowanceCityType.
     * @param cboTAllowanceCityType New value of property cboTAllowanceCityType.
     */
    public void setCboTAllowanceCityType(java.lang.String cboTAllowanceCityType) {
        this.cboTAllowanceCityType = cboTAllowanceCityType;
    }
    
    /**
     * Getter for property tbmTAllowance.
     * @return Value of property tbmTAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmTAllowance() {
        return tbmTAllowance;
    }
    
    /**
     * Setter for property tbmTAllowance.
     * @param tbmTAllowance New value of property tbmTAllowance.
     */
    public void setTbmTAllowance(com.see.truetransact.clientutil.TableModel tbmTAllowance) {
        this.tbmTAllowance = tbmTAllowance;
    }
    
    /**
     * Getter for property cbmTAllowance.
     * @return Value of property cbmTAllowance.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTAllowance() {
        return cbmTAllowance;
    }
    
    /**
     * Setter for property cbmTAllowance.
     * @param cbmTAllowance New value of property cbmTAllowance.
     */
    public void setCbmTAllowance(com.see.truetransact.clientutil.ComboBoxModel cbmTAllowance) {
        this.cbmTAllowance = cbmTAllowance;
    }
    
    /**
     * Getter for property cbmTAllowanceCityType.
     * @return Value of property cbmTAllowanceCityType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTAllowanceCityType() {
        return cbmTAllowanceCityType;
    }
    
    /**
     * Setter for property cbmTAllowanceCityType.
     * @param cbmTAllowanceCityType New value of property cbmTAllowanceCityType.
     */
    public void setCbmTAllowanceCityType(com.see.truetransact.clientutil.ComboBoxModel cbmTAllowanceCityType) {
        this.cbmTAllowanceCityType = cbmTAllowanceCityType;
    }
    
    /**
     * Getter for property txtDAPercentagePerSlabValue.
     * @return Value of property txtDAPercentagePerSlabValue.
     */
    public java.lang.String getTxtDAPercentagePerSlabValue() {
        return txtDAPercentagePerSlabValue;
    }
    
    /**
     * Setter for property txtDAPercentagePerSlabValue.
     * @param txtDAPercentagePerSlabValue New value of property txtDAPercentagePerSlabValue.
     */
    public void setTxtDAPercentagePerSlabValue(java.lang.String txtDAPercentagePerSlabValue) {
        this.txtDAPercentagePerSlabValue = txtDAPercentagePerSlabValue;
    }
    
    /**
     * Getter for property txtTotalNoofSlabValue.
     * @return Value of property txtTotalNoofSlabValue.
     */
    public java.lang.String getTxtTotalNoofSlabValue() {
        return txtTotalNoofSlabValue;
    }
    
    /**
     * Setter for property txtTotalNoofSlabValue.
     * @param txtTotalNoofSlabValue New value of property txtTotalNoofSlabValue.
     */
    public void setTxtTotalNoofSlabValue(java.lang.String txtTotalNoofSlabValue) {
        this.txtTotalNoofSlabValue = txtTotalNoofSlabValue;
    }
    
    /**
     * Getter for property txtDATotalDAPercentageValue.
     * @return Value of property txtDATotalDAPercentageValue.
     */
    public java.lang.String getTxtDATotalDAPercentageValue() {
        return txtDATotalDAPercentageValue;
    }
    
    /**
     * Setter for property txtDATotalDAPercentageValue.
     * @param txtDATotalDAPercentageValue New value of property txtDATotalDAPercentageValue.
     */
    public void setTxtDATotalDAPercentageValue(java.lang.String txtDATotalDAPercentageValue) {
        this.txtDATotalDAPercentageValue = txtDATotalDAPercentageValue;
    }
    
    /**
     * Getter for property txtDANoOfPointsPerSlabValue.
     * @return Value of property txtDANoOfPointsPerSlabValue.
     */
    public java.lang.String getTxtDANoOfPointsPerSlabValue() {
        return txtDANoOfPointsPerSlabValue;
    }
    
    /**
     * Setter for property txtDANoOfPointsPerSlabValue.
     * @param txtDANoOfPointsPerSlabValue New value of property txtDANoOfPointsPerSlabValue.
     */
    public void setTxtDANoOfPointsPerSlabValue(java.lang.String txtDANoOfPointsPerSlabValue) {
        this.txtDANoOfPointsPerSlabValue = txtDANoOfPointsPerSlabValue;
    }
    
    /**
     * Getter for property txtDAIndexValue.
     * @return Value of property txtDAIndexValue.
     */
    public java.lang.String getTxtDAIndexValue() {
        return txtDAIndexValue;
    }
    
    /**
     * Setter for property txtDAIndexValue.
     * @param txtDAIndexValue New value of property txtDAIndexValue.
     */
    public void setTxtDAIndexValue(java.lang.String txtDAIndexValue) {
        this.txtDAIndexValue = txtDAIndexValue;
    }
    
    /**
     * Getter for property rdoHRAPayable_Yes.
     * @return Value of property rdoHRAPayable_Yes.
     */
    public boolean getRdoHRAPayable_Yes() {
        return rdoHRAPayable_Yes;
    }
    
    /**
     * Setter for property rdoHRAPayable_Yes.
     * @param rdoHRAPayable_Yes New value of property rdoHRAPayable_Yes.
     */
    public void setRdoHRAPayable_Yes(boolean rdoHRAPayable_Yes) {
        this.rdoHRAPayable_Yes = rdoHRAPayable_Yes;
    }
    
    /**
     * Getter for property rdoHRAPayable_No.
     * @return Value of property rdoHRAPayable_No.
     */
    public boolean getRdoHRAPayable_No() {
        return rdoHRAPayable_No;
    }
    
    /**
     * Setter for property rdoHRAPayable_No.
     * @param rdoHRAPayable_No New value of property rdoHRAPayable_No.
     */
    public void setRdoHRAPayable_No(boolean rdoHRAPayable_No) {
        this.rdoHRAPayable_No = rdoHRAPayable_No;
    }
    
    /**
     * Getter for property tdtTAFromDateValue.
     * @return Value of property tdtTAFromDateValue.
     */
    public java.lang.String getTdtTAFromDateValue() {
        return tdtTAFromDateValue;
    }
    
    /**
     * Setter for property tdtTAFromDateValue.
     * @param tdtTAFromDateValue New value of property tdtTAFromDateValue.
     */
    public void setTdtTAFromDateValue(java.lang.String tdtTAFromDateValue) {
        this.tdtTAFromDateValue = tdtTAFromDateValue;
    }
    
    /**
     * Getter for property tdtTAToDateValue.
     * @return Value of property tdtTAToDateValue.
     */
    public java.lang.String getTdtTAToDateValue() {
        return tdtTAToDateValue;
    }
    
    /**
     * Setter for property tdtTAToDateValue.
     * @param tdtTAToDateValue New value of property tdtTAToDateValue.
     */
    public void setTdtTAToDateValue(java.lang.String tdtTAToDateValue) {
        this.tdtTAToDateValue = tdtTAToDateValue;
    }
    
    /**
     * Getter for property txtBasicAmtUptoValue.
     * @return Value of property txtBasicAmtUptoValue.
     */
    public java.lang.String getTxtBasicAmtUptoValue() {
        return txtBasicAmtUptoValue;
    }
    
    /**
     * Setter for property txtBasicAmtUptoValue.
     * @param txtBasicAmtUptoValue New value of property txtBasicAmtUptoValue.
     */
    public void setTxtBasicAmtUptoValue(java.lang.String txtBasicAmtUptoValue) {
        this.txtBasicAmtUptoValue = txtBasicAmtUptoValue;
    }
    
    /**
     * Getter for property txtConveyancePerMonthValue.
     * @return Value of property txtConveyancePerMonthValue.
     */
    public java.lang.String getTxtConveyancePerMonthValue() {
        return txtConveyancePerMonthValue;
    }
    
    /**
     * Setter for property txtConveyancePerMonthValue.
     * @param txtConveyancePerMonthValue New value of property txtConveyancePerMonthValue.
     */
    public void setTxtConveyancePerMonthValue(java.lang.String txtConveyancePerMonthValue) {
        this.txtConveyancePerMonthValue = txtConveyancePerMonthValue;
    }
    
    /**
     * Getter for property txtBasicAmtBeyondValue.
     * @return Value of property txtBasicAmtBeyondValue.
     */
    public java.lang.String getTxtBasicAmtBeyondValue() {
        return txtBasicAmtBeyondValue;
    }
    
    /**
     * Setter for property txtBasicAmtBeyondValue.
     * @param txtBasicAmtBeyondValue New value of property txtBasicAmtBeyondValue.
     */
    public void setTxtBasicAmtBeyondValue(java.lang.String txtBasicAmtBeyondValue) {
        this.txtBasicAmtBeyondValue = txtBasicAmtBeyondValue;
    }
    
    /**
     * Getter for property txtConveyanceAmtValue.
     * @return Value of property txtConveyanceAmtValue.
     */
    public java.lang.String getTxtConveyanceAmtValue() {
        return txtConveyanceAmtValue;
    }
    
    /**
     * Setter for property txtConveyanceAmtValue.
     * @param txtConveyanceAmtValue New value of property txtConveyanceAmtValue.
     */
    public void setTxtConveyanceAmtValue(java.lang.String txtConveyanceAmtValue) {
        this.txtConveyanceAmtValue = txtConveyanceAmtValue;
    }
    
    /**
     * Getter for property txtNooflitresValue.
     * @return Value of property txtNooflitresValue.
     */
    public java.lang.String getTxtNooflitresValue() {
        return txtNooflitresValue;
    }
    
    /**
     * Setter for property txtNooflitresValue.
     * @param txtNooflitresValue New value of property txtNooflitresValue.
     */
    public void setTxtNooflitresValue(java.lang.String txtNooflitresValue) {
        this.txtNooflitresValue = txtNooflitresValue;
    }
    
    /**
     * Getter for property txtPricePerlitreValue.
     * @return Value of property txtPricePerlitreValue.
     */
    public java.lang.String getTxtPricePerlitreValue() {
        return txtPricePerlitreValue;
    }
    
    /**
     * Setter for property txtPricePerlitreValue.
     * @param txtPricePerlitreValue New value of property txtPricePerlitreValue.
     */
    public void setTxtPricePerlitreValue(java.lang.String txtPricePerlitreValue) {
        this.txtPricePerlitreValue = txtPricePerlitreValue;
    }
    
    /**
     * Getter for property txtTotalConveyanceAmtValue.
     * @return Value of property txtTotalConveyanceAmtValue.
     */
    public java.lang.String getTxtTotalConveyanceAmtValue() {
        return txtTotalConveyanceAmtValue;
    }
    
    /**
     * Setter for property txtTotalConveyanceAmtValue.
     * @param txtTotalConveyanceAmtValue New value of property txtTotalConveyanceAmtValue.
     */
    public void setTxtTotalConveyanceAmtValue(java.lang.String txtTotalConveyanceAmtValue) {
        this.txtTotalConveyanceAmtValue = txtTotalConveyanceAmtValue;
    }
    
    /**
     * Getter for property chkFixedConveyance.
     * @return Value of property chkFixedConveyance.
     */
    public boolean getChkFixedConveyance() {
        return chkFixedConveyance;
    }
    
    /**
     * Setter for property chkFixedConveyance.
     * @param chkFixedConveyance New value of property chkFixedConveyance.
     */
    public void setChkFixedConveyance(boolean chkFixedConveyance) {
        this.chkFixedConveyance = chkFixedConveyance;
    }
    
    /**
     * Getter for property chkPetrolAllowance.
     * @return Value of property chkPetrolAllowance.
     */
    public boolean getChkPetrolAllowance() {
        return chkPetrolAllowance;
    }
    
    /**
     * Setter for property chkPetrolAllowance.
     * @param chkPetrolAllowance New value of property chkPetrolAllowance.
     */
    public void setChkPetrolAllowance(boolean chkPetrolAllowance) {
        this.chkPetrolAllowance = chkPetrolAllowance;
    }
    
    /**
     * Getter for property cbmMAidDesg.
     * @return Value of property cbmMAidDesg.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMAidDesg() {
        return cbmMAidDesg;
    }
    
    /**
     * Setter for property cbmMAidDesg.
     * @param cbmMAidDesg New value of property cbmMAidDesg.
     */
    public void setCbmMAidDesg(com.see.truetransact.clientutil.ComboBoxModel cbmMAidDesg) {
        this.cbmMAidDesg = cbmMAidDesg;
    }
    
    /**
     * Getter for property tbmMAllowance.
     * @return Value of property tbmMAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmMAllowance() {
        return tbmMAllowance;
    }
    
    /**
     * Setter for property tbmMAllowance.
     * @param tbmMAllowance New value of property tbmMAllowance.
     */
    public void setTbmMAllowance(com.see.truetransact.clientutil.TableModel tbmMAllowance) {
        this.tbmMAllowance = tbmMAllowance;
    }
    
    /**
     * Getter for property cboMAidDesg.
     * @return Value of property cboMAidDesg.
     */
    public java.lang.String getCboMAidDesg() {
        return cboMAidDesg;
    }
    
    /**
     * Setter for property cboMAidDesg.
     * @param cboMAidDesg New value of property cboMAidDesg.
     */
    public void setCboMAidDesg(java.lang.String cboMAidDesg) {
        this.cboMAidDesg = cboMAidDesg;
    }
    
    /**
     * Getter for property txtMAidAmtValue.
     * @return Value of property txtMAidAmtValue.
     */
    public java.lang.String getTxtMAidAmtValue() {
        return txtMAidAmtValue;
    }
    
    /**
     * Setter for property txtMAidAmtValue.
     * @param txtMAidAmtValue New value of property txtMAidAmtValue.
     */
    public void setTxtMAidAmtValue(java.lang.String txtMAidAmtValue) {
        this.txtMAidAmtValue = txtMAidAmtValue;
    }
    
    /**
     * Getter for property tdtMAidFromDateValue.
     * @return Value of property tdtMAidFromDateValue.
     */
    public java.lang.String getTdtMAidFromDateValue() {
        return tdtMAidFromDateValue;
    }
    
    /**
     * Setter for property tdtMAidFromDateValue.
     * @param tdtMAidFromDateValue New value of property tdtMAidFromDateValue.
     */
    public void setTdtMAidFromDateValue(java.lang.String tdtMAidFromDateValue) {
        this.tdtMAidFromDateValue = tdtMAidFromDateValue;
    }
    
    /**
     * Getter for property tdtMAidToDateValue.
     * @return Value of property tdtMAidToDateValue.
     */
    public java.lang.String getTdtMAidToDateValue() {
        return tdtMAidToDateValue;
    }
    
    /**
     * Setter for property tdtMAidToDateValue.
     * @param tdtMAidToDateValue New value of property tdtMAidToDateValue.
     */
    public void setTdtMAidToDateValue(java.lang.String tdtMAidToDateValue) {
        this.tdtMAidToDateValue = tdtMAidToDateValue;
    }
    
    /**
     * Getter for property SSAuthorizeStatus.
     * @return Value of property SSAuthorizeStatus.
     */
    public java.lang.String getSSAuthorizeStatus() {
        return SSAuthorizeStatus;
    }
    
    /**
     * Setter for property SSAuthorizeStatus.
     * @param SSAuthorizeStatus New value of property SSAuthorizeStatus.
     */
    public void setSSAuthorizeStatus(java.lang.String SSAuthorizeStatus) {
        this.SSAuthorizeStatus = SSAuthorizeStatus;
    }
    
    /**
     * Getter for property DAAuthorizeStatus.
     * @return Value of property DAAuthorizeStatus.
     */
    public java.lang.String getDAAuthorizeStatus() {
        return DAAuthorizeStatus;
    }
    
    /**
     * Setter for property DAAuthorizeStatus.
     * @param DAAuthorizeStatus New value of property DAAuthorizeStatus.
     */
    public void setDAAuthorizeStatus(java.lang.String DAAuthorizeStatus) {
        this.DAAuthorizeStatus = DAAuthorizeStatus;
    }
    
    /**
     * Getter for property CCAAuthorizeStatus.
     * @return Value of property CCAAuthorizeStatus.
     */
    public java.lang.String getCCAAuthorizeStatus() {
        return CCAAuthorizeStatus;
    }
    
    /**
     * Setter for property CCAAuthorizeStatus.
     * @param CCAAuthorizeStatus New value of property CCAAuthorizeStatus.
     */
    public void setCCAAuthorizeStatus(java.lang.String CCAAuthorizeStatus) {
        this.CCAAuthorizeStatus = CCAAuthorizeStatus;
    }
    
    /**
     * Getter for property HRAAuthorizeStatus.
     * @return Value of property HRAAuthorizeStatus.
     */
    public java.lang.String getHRAAuthorizeStatus() {
        return HRAAuthorizeStatus;
    }
    
    /**
     * Setter for property HRAAuthorizeStatus.
     * @param HRAAuthorizeStatus New value of property HRAAuthorizeStatus.
     */
    public void setHRAAuthorizeStatus(java.lang.String HRAAuthorizeStatus) {
        this.HRAAuthorizeStatus = HRAAuthorizeStatus;
    }
    
    /**
     * Getter for property TAAuthorizeStatus.
     * @return Value of property TAAuthorizeStatus.
     */
    public java.lang.String getTAAuthorizeStatus() {
        return TAAuthorizeStatus;
    }
    
    /**
     * Setter for property TAAuthorizeStatus.
     * @param TAAuthorizeStatus New value of property TAAuthorizeStatus.
     */
    public void setTAAuthorizeStatus(java.lang.String TAAuthorizeStatus) {
        this.TAAuthorizeStatus = TAAuthorizeStatus;
    }
    
    /**
     * Getter for property MAAuthorizeStatus.
     * @return Value of property MAAuthorizeStatus.
     */
    public java.lang.String getMAAuthorizeStatus() {
        return MAAuthorizeStatus;
    }
    
    /**
     * Setter for property MAAuthorizeStatus.
     * @param MAAuthorizeStatus New value of property MAAuthorizeStatus.
     */
    public void setMAAuthorizeStatus(java.lang.String MAAuthorizeStatus) {
        this.MAAuthorizeStatus = MAAuthorizeStatus;
    }
    
    /**
     * Getter for property cbmOADesignationValue.
     * @return Value of property cbmOADesignationValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOADesignationValue() {
        return cbmOADesignationValue;
    }
    
    /**
     * Setter for property cbmOADesignationValue.
     * @param cbmOADesignationValue New value of property cbmOADesignationValue.
     */
    public void setCbmOADesignationValue(com.see.truetransact.clientutil.ComboBoxModel cbmOADesignationValue) {
        this.cbmOADesignationValue = cbmOADesignationValue;
    }
    
    /**
     * Getter for property cbmOAllowanceTypeValue.
     * @return Value of property cbmOAllowanceTypeValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOAllowanceTypeValue() {
        return cbmOAllowanceTypeValue;
    }
    
    /**
     * Setter for property cbmOAllowanceTypeValue.
     * @param cbmOAllowanceTypeValue New value of property cbmOAllowanceTypeValue.
     */
    public void setCbmOAllowanceTypeValue(com.see.truetransact.clientutil.ComboBoxModel cbmOAllowanceTypeValue) {
        this.cbmOAllowanceTypeValue = cbmOAllowanceTypeValue;
    }
    
    /**
     * Getter for property cbmOAParameterBasedOnValue.
     * @return Value of property cbmOAParameterBasedOnValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOAParameterBasedOnValue() {
        return cbmOAParameterBasedOnValue;
    }
    
    /**
     * Setter for property cbmOAParameterBasedOnValue.
     * @param cbmOAParameterBasedOnValue New value of property cbmOAParameterBasedOnValue.
     */
    public void setCbmOAParameterBasedOnValue(com.see.truetransact.clientutil.ComboBoxModel cbmOAParameterBasedOnValue) {
        this.cbmOAParameterBasedOnValue = cbmOAParameterBasedOnValue;
    }
    
    /**
     * Getter for property cbmOASubParameterValue.
     * @return Value of property cbmOASubParameterValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOASubParameterValue() {
        return cbmOASubParameterValue;
    }
    
    /**
     * Setter for property cbmOASubParameterValue.
     * @param cbmOASubParameterValue New value of property cbmOASubParameterValue.
     */
    public void setCbmOASubParameterValue(com.see.truetransact.clientutil.ComboBoxModel cbmOASubParameterValue) {
        this.cbmOASubParameterValue = cbmOASubParameterValue;
    }
    
    /**
     * Getter for property tbmOAllowance.
     * @return Value of property tbmOAllowance.
     */
    public com.see.truetransact.clientutil.TableModel getTbmOAllowance() {
        return tbmOAllowance;
    }
    
    /**
     * Setter for property tbmOAllowance.
     * @param tbmOAllowance New value of property tbmOAllowance.
     */
    public void setTbmOAllowance(com.see.truetransact.clientutil.TableModel tbmOAllowance) {
        this.tbmOAllowance = tbmOAllowance;
    }
    
    /**
     * Getter for property cboOADesignationValue.
     * @return Value of property cboOADesignationValue.
     */
    public java.lang.String getCboOADesignationValue() {
        return cboOADesignationValue;
    }
    
    /**
     * Setter for property cboOADesignationValue.
     * @param cboOADesignationValue New value of property cboOADesignationValue.
     */
    public void setCboOADesignationValue(java.lang.String cboOADesignationValue) {
        this.cboOADesignationValue = cboOADesignationValue;
    }
    
    /**
     * Getter for property tdtOAFromDateValue.
     * @return Value of property tdtOAFromDateValue.
     */
    public java.lang.String getTdtOAFromDateValue() {
        return tdtOAFromDateValue;
    }
    
    /**
     * Setter for property tdtOAFromDateValue.
     * @param tdtOAFromDateValue New value of property tdtOAFromDateValue.
     */
    public void setTdtOAFromDateValue(java.lang.String tdtOAFromDateValue) {
        this.tdtOAFromDateValue = tdtOAFromDateValue;
    }
    
    /**
     * Getter for property tdtOAToDateValue.
     * @return Value of property tdtOAToDateValue.
     */
    public java.lang.String getTdtOAToDateValue() {
        return tdtOAToDateValue;
    }
    
    /**
     * Setter for property tdtOAToDateValue.
     * @param tdtOAToDateValue New value of property tdtOAToDateValue.
     */
    public void setTdtOAToDateValue(java.lang.String tdtOAToDateValue) {
        this.tdtOAToDateValue = tdtOAToDateValue;
    }
    
    /**
     * Getter for property cboOAllowanceTypeValue.
     * @return Value of property cboOAllowanceTypeValue.
     */
    public java.lang.String getCboOAllowanceTypeValue() {
        return cboOAllowanceTypeValue;
    }
    
    /**
     * Setter for property cboOAllowanceTypeValue.
     * @param cboOAllowanceTypeValue New value of property cboOAllowanceTypeValue.
     */
    public void setCboOAllowanceTypeValue(java.lang.String cboOAllowanceTypeValue) {
        this.cboOAllowanceTypeValue = cboOAllowanceTypeValue;
    }
    
    /**
     * Getter for property cboOAParameterBasedOnValue.
     * @return Value of property cboOAParameterBasedOnValue.
     */
    public java.lang.String getCboOAParameterBasedOnValue() {
        return cboOAParameterBasedOnValue;
    }
    
    /**
     * Setter for property cboOAParameterBasedOnValue.
     * @param cboOAParameterBasedOnValue New value of property cboOAParameterBasedOnValue.
     */
    public void setCboOAParameterBasedOnValue(java.lang.String cboOAParameterBasedOnValue) {
        this.cboOAParameterBasedOnValue = cboOAParameterBasedOnValue;
    }
    
    /**
     * Getter for property cboOASubParameterValue.
     * @return Value of property cboOASubParameterValue.
     */
    public java.lang.String getCboOASubParameterValue() {
        return cboOASubParameterValue;
    }
    
    /**
     * Setter for property cboOASubParameterValue.
     * @param cboOASubParameterValue New value of property cboOASubParameterValue.
     */
    public void setCboOASubParameterValue(java.lang.String cboOASubParameterValue) {
        this.cboOASubParameterValue = cboOASubParameterValue;
    }
    
    /**
     * Getter for property chkOAFixedValue.
     * @return Value of property chkOAFixedValue.
     */
    public boolean getChkOAFixedValue() {
        return chkOAFixedValue;
    }
    
    /**
     * Setter for property chkOAFixedValue.
     * @param chkOAFixedValue New value of property chkOAFixedValue.
     */
    public void setChkOAFixedValue(boolean chkOAFixedValue) {
        this.chkOAFixedValue = chkOAFixedValue;
    }
    
    
    
    /**
     * Getter for property chkOAPercentageValue.
     * @return Value of property chkOAPercentageValue.
     */
    public boolean getChkOAPercentageValue() {
        return chkOAPercentageValue;
    }
    
    /**
     * Setter for property chkOAPercentageValue.
     * @param chkOAPercentageValue New value of property chkOAPercentageValue.
     */
    public void setChkOAPercentageValue(boolean chkOAPercentageValue) {
        this.chkOAPercentageValue = chkOAPercentageValue;
    }
    
    /**
     * Getter for property txtOAFixedAmtValue.
     * @return Value of property txtOAFixedAmtValue.
     */
    public java.lang.String getTxtOAFixedAmtValue() {
        return txtOAFixedAmtValue;
    }
    
    /**
     * Setter for property txtOAFixedAmtValue.
     * @param txtOAFixedAmtValue New value of property txtOAFixedAmtValue.
     */
    public void setTxtOAFixedAmtValue(java.lang.String txtOAFixedAmtValue) {
        this.txtOAFixedAmtValue = txtOAFixedAmtValue;
    }
    
    /**
     * Getter for property txtOAPercentageValue.
     * @return Value of property txtOAPercentageValue.
     */
    public java.lang.String getTxtOAPercentageValue() {
        return txtOAPercentageValue;
    }
    
    /**
     * Setter for property txtOAPercentageValue.
     * @param txtOAPercentageValue New value of property txtOAPercentageValue.
     */
    public void setTxtOAPercentageValue(java.lang.String txtOAPercentageValue) {
        this.txtOAPercentageValue = txtOAPercentageValue;
    }
    
    /**
     * Getter for property txtOAMaximumOfValue.
     * @return Value of property txtOAMaximumOfValue.
     */
    public java.lang.String getTxtOAMaximumOfValue() {
        return txtOAMaximumOfValue;
    }
    
    /**
     * Setter for property txtOAMaximumOfValue.
     * @param txtOAMaximumOfValue New value of property txtOAMaximumOfValue.
     */
    public void setTxtOAMaximumOfValue(java.lang.String txtOAMaximumOfValue) {
        this.txtOAMaximumOfValue = txtOAMaximumOfValue;
    }
    
    /**
     * Getter for property OAAuthorizeStatus.
     * @return Value of property OAAuthorizeStatus.
     */
    public java.lang.String getOAAuthorizeStatus() {
        return OAAuthorizeStatus;
    }
    
    /**
     * Setter for property OAAuthorizeStatus.
     * @param OAAuthorizeStatus New value of property OAAuthorizeStatus.
     */
    public void setOAAuthorizeStatus(java.lang.String OAAuthorizeStatus) {
        this.OAAuthorizeStatus = OAAuthorizeStatus;
    }
    
    /**
     * Getter for property tempSlNo.
     * @return Value of property tempSlNo.
     */
    public java.lang.String getTempSlNo() {
        return tempSlNo;
    }
    
    /**
     * Setter for property tempSlNo.
     * @param tempSlNo New value of property tempSlNo.
     */
    public void setTempSlNo(java.lang.String tempSlNo) {
        this.tempSlNo = tempSlNo;
    }
    
    /**
     * Getter for property txtFromAmount.
     * @return Value of property txtFromAmount.
     */
    public java.lang.String getTxtFromAmount() {
        return txtFromAmount;
    }
    
    /**
     * Setter for property txtFromAmount.
     * @param txtFromAmount New value of property txtFromAmount.
     */
    public void setTxtFromAmount(java.lang.String txtFromAmount) {
        this.txtFromAmount = txtFromAmount;
    }
    
    /**
     * Getter for property txtToAmount.
     * @return Value of property txtToAmount.
     */
    public java.lang.String getTxtToAmount() {
        return txtToAmount;
    }
    
    /**
     * Setter for property txtToAmount.
     * @param txtToAmount New value of property txtToAmount.
     */
    public void setTxtToAmount(java.lang.String txtToAmount) {
        this.txtToAmount = txtToAmount;
    }
    
    /**
     * Getter for property rdoPercentOrFixed.
     * @return Value of property rdoPercentOrFixed.
     */
    public java.lang.String getRdoPercentOrFixed() {
        return rdoPercentOrFixed;
    }
    
    /**
     * Setter for property rdoPercentOrFixed.
     * @param rdoPercentOrFixed New value of property rdoPercentOrFixed.
     */
    public void setRdoPercentOrFixed(java.lang.String rdoPercentOrFixed) {
        this.rdoPercentOrFixed = rdoPercentOrFixed;
    }
    
    /**
     * Getter for property rdoIndexOrPercent.
     * @return Value of property rdoIndexOrPercent.
     */
    public java.lang.String getRdoIndexOrPercent() {
        return rdoIndexOrPercent;
    }
    
    /**
     * Setter for property rdoIndexOrPercent.
     * @param rdoIndexOrPercent New value of property rdoIndexOrPercent.
     */
    public void setRdoIndexOrPercent(java.lang.String rdoIndexOrPercent) {
        this.rdoIndexOrPercent = rdoIndexOrPercent;
    }
    
    /**
     * Getter for property oAbasedOnParameter.
     * @return Value of property oAbasedOnParameter.
     */
    public java.lang.String getOAbasedOnParameter() {
        return oAbasedOnParameter;
    }    
    
    /**
     * Setter for property oAbasedOnParameter.
     * @param oAbasedOnParameter New value of property oAbasedOnParameter.
     */
    public void setOAbasedOnParameter(java.lang.String oAbasedOnParameter) {
        this.oAbasedOnParameter = oAbasedOnParameter;
    }    
    
}
