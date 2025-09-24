/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MisecllaniousDeductionOB.java
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
import com.see.truetransact.transferobject.common.HaltingAllowanceTO;
import com.see.truetransact.transferobject.common.MisecllaniousDeductionTO;
import com.see.truetransact.transferobject.common.GratuityTO;
//import com.see.truetransact.transferobject.common.HRAllowanceTO;
//import com.see.truetransact.transferobject.common.TAllowanceTO;
//import com.see.truetransact.transferobject.common.MAllowanceTO;
//import com.see.truetransact.transferobject.common.OtherAllowanceTO;
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

public class MisecllaniousDeductionOB extends CObservable{
    
    private static MisecllaniousDeductionOB misecllaniousDeductionOB;
    private ProxyFactory proxy;
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private int actionType;
    private int result;
    private String lblStatus;
    Date curDate = null;
    private boolean deleteEnable = false;
    
    private ComboBoxModel cbmHaltingDesignationValue,cbmHaltingAllowanceTypeValue,
    cbmHaltingParameterBasedOnValue,cbmHaltingSubParameterValue;
    private TableModel tbmHalting;
    private ArrayList key,value,HaltingTOs,deleteHaltingList;
    
    private ComboBoxModel cbmMisecllaniousDeduction,cbmMDCityType,cbmMDEligibleAllowances;
    private TableModel tbmMisecllaniousDeduction;
    private ArrayList MisecllaniousDeductionTOs,deleteMisecllaniousDeductionList;
    
    private ComboBoxModel cbmGratuityDesignation;//,cbmGratuityCityType;
    private TableModel tbmGratuity;
    private ArrayList GratuityTOs,deleteGratuityList;
    
    //    private ComboBoxModel cbmHRAllowance,cbmHRAllowanceCityType;
    //    private TableModel tbmHRAllowance;
    //    private ArrayList HRAllowanceTOs,deleteHRAllowanceList;
    //
    //    private ComboBoxModel cbmTAllowance,cbmTAllowanceCityType;
    //    private TableModel tbmTAllowance;
    //    private ArrayList TAllowanceTOs,deleteTAllowanceList;
    //
    //    private ComboBoxModel cbmMAidDesg;
    //    private TableModel tbmMAllowance;
    //    private ArrayList MAllowanceTOs,deleteMAllowanceList;
    
    //    private ComboBoxModel cbmOADesignationValue,cbmOAllowanceTypeValue,cbmOAParameterBasedOnValue,cbmOASubParameterValue;
    //    private TableModel tbmOAllowance;
    //    private ArrayList OAllowanceTOs,deleteOAllowanceList;
    
    private String cboHaltingDesignationValue,cboHaltingAllowanceTypeValue,cboHaltingParameterBasedOnValue,cboHaltingSubParameterValue;
    private String lblHaltingSLNOValue = "";
    private String tdtHaltingFromDateValue = "";
    private String tdtHaltingToDateValue = "";
    private String txtHaltingFixedAmtValue = "";
    private String txtHaltingMaximumOfValue = "";
    private String txtHaltingPercentageValue = "";
    private String mdFixedOrPercentage = "";
    private boolean chkFixedType = false;
    private boolean chkPercentageType = false;
    private Double lblHATempSLNoValue = null;
    //    private String cboSalaryStructureStagnationOnceIn;
    //    private boolean rdoStagnationIncrement_Yes = false;
    //    private boolean rdoStagnationIncrement_No = false;
    //    private String txtSalaryStructureAmtValue = "";
    //    private String txtSalaryStructureIncYearValue = "";
    //    private String txtSalaryStructureEndingAmtValue = "";
    //    private String txtSalaryStructureSingleRowAmt = "";
    //    private String txtSalaryStructureTotNoIncValue = "";
    //    private String txtSalaryStructureStagnationAmtValue = "";
    //    private String txtSalaryStructureNoOfStagnationValue = "";
    //    private String txtSalaryStructureStagnationOnceInValue = "";
    
    private String cboMisecllaniousDeduction,cboMDCityType,cboMDEligibleAllowances;
    private String lblMDSLNOValue = "";
    private String tdtMDFromDateValue = "";
    private String tdtMDToDateValue = "";
    private String txtMDBasedAmtValue = "";
    private String txtMDMaximumAmtValue = "";
    private String txtMdFixedAmtValue = "";
    private String txtFromAmount = "";
    private String txtToAmount = "";
    private String rdoUsingBasic = "";
    private String txtMDEligiblePercentageValue = "";
    private Double lblMDTempSLNoValue = null;
    
    private String cboGratuityDesignation;//,cboGratuityCityType;
    private String lblGratuitySLNOValue = "";
    private String tdtGratuityFromDateValue = "";
    private String tdtGratuityToDateValue = "";
    private String txtGratuityUptoValue = "";
    private String txtGratuityUptoServiceValue = "";
    private String txtGratuityMaximumOfValue = "";
    private String txtGratuityBeyondValue = "";
    private String txtGratuityBeyondServiceValue = "";
    private String txtGratuityMaximumAmtBeyongValue = "";
    private Double lblGATempSLNoValue = null;
    
    //    private String cboHRAllowanceDesignation;
    //    private String cboHRAllowanceCityType;
    //    private boolean rdoHRAPayable_Yes = false;
    //    private boolean rdoHRAPayable_No = false;
    //    private String tdtHRAllowanceFromDateValue = "";
    //    private String tdtHRAllowanceToDateValue = "";
    //    private String txtHRAllowanceStartingAmtValue = "";
    //    private String txtHRAllowanceEndingAmtValue = "";
    //
    //    private String cboTAllowanceCityType;
    //    private String cboTAllowanceDesgination;
    //    private String tdtTAFromDateValue = "";
    //    private String tdtTAToDateValue = "";
    //    private boolean chkFixedConveyance = false;
    //    private boolean chkPetrolAllowance = false;
    //    private String txtBasicAmtUptoValue = "";
    //    private String txtConveyancePerMonthValue = "";
    //    private String txtBasicAmtBeyondValue = "";
    //    private String txtConveyanceAmtValue = "";
    //    private String txtNooflitresValue = "";
    //    private String txtPricePerlitreValue = "";
    //    private String txtTotalConveyanceAmtValue = "";
    //
    //    private String cboMAidDesg;
    //    private String txtMAidAmtValue = "";
    //    private String tdtMAidFromDateValue = "";
    //    private String tdtMAidToDateValue = "";
    //
    //    private String cboOADesignationValue = "";
    //    private String cboOAllowanceTypeValue = "";
    //    private String cboOAParameterBasedOnValue = "";
    //    private String cboOASubParameterValue = "";
    //    private String tdtOAFromDateValue = "";
    //    private String tdtOAToDateValue = "";
    //    private boolean chkOAFixedValue = false;
    //    private boolean chkOAPercentageValue = false;
    //    private String txtOAFixedAmtValue = "";
    //    private String txtOAPercentageValue = "";
    //    private String txtOAMaximumOfValue = "";
    //    private String txtOAWashingAllownaceValue = "";
    //    private String txtOACycleAllowanceValue = "";
    //    private String txtOAShiftDutyAllowanceValue = "";
    
    private String HAAuthorizeStatus = "";
    private String MDAuthorizeStatus = "";
    private String GAuthorizeStatus = "";
    //    private String HRAAuthorizeStatus = "";
    //    private String TAAuthorizeStatus = "";
    //    private String MAAuthorizeStatus = "";
    //    private String OAAuthorizeStatus = "";
    int pan = -1;
    int panEditDelete = -1;
    private int MSCL_DEDUCTION = 2,HALTING_ALLOWANCE = 1,GRATUITY = 3;
    
    /** Creates a new instance of MisecllaniousDeductionOB */
    private MisecllaniousDeductionOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "MisecllaniousDeductionJNDI");
        map.put(CommonConstants.HOME, "common.MisecllaniousDeductionHome");
        map.put(CommonConstants.REMOTE,"common.MisecllaniousDeduction");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
        setTable();
        HaltingTOs = new ArrayList();
        deleteHaltingList = new ArrayList();
        MisecllaniousDeductionTOs = new ArrayList();
        deleteMisecllaniousDeductionList = new ArrayList();
        GratuityTOs = new ArrayList();
        deleteGratuityList = new ArrayList();
        //        HRAllowanceTOs = new ArrayList();
        //        deleteHRAllowanceList= new ArrayList();
        //        TAllowanceTOs = new ArrayList();
        //        deleteTAllowanceList= new ArrayList();
        //        MAllowanceTOs = new ArrayList();
        //        deleteMAllowanceList= new ArrayList();
        //        HaltingTOs = new ArrayList();
        //        deleteOAllowanceList= new ArrayList();
    }
    
    public static MisecllaniousDeductionOB getInstance(){
        try {
            misecllaniousDeductionOB = new MisecllaniousDeductionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return misecllaniousDeductionOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        //        columnHeader.add("SL No.");
        columnHeader.add("Grade");
        //        columnHeader.add("From Dt");
        columnHeader.add("Parameter");
        columnHeader.add("Sub Parameter");
        columnHeader.add("FixedAmt");
        //        columnHeader.add("Total Amount");
        //        columnHeader.add("Type");
        columnHeader.add("Percentage");
        columnHeader.add("Authorize Status");
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            columnHeader.add("Verified");
        }
        ArrayList data = new ArrayList();
        tbmHalting = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
        columnHeader.add("Deduction Type");
        columnHeader.add("Fixed Amount");
        columnHeader.add("Index");
        columnHeader.add("% Per Slab");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmMisecllaniousDeduction = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("Grade");
        columnHeader.add("From Dt");
        columnHeader.add("CCA No.of Inc");
        columnHeader.add("CCA Total Amount");
        //        columnHeader.add("City Type");
        columnHeader.add("Authorize Status");
        data = new ArrayList();
        tbmGratuity = new TableModel(data,columnHeader);
        //        columnHeader = new ArrayList();
        //        columnHeader.add("Grade");
        //        columnHeader.add("From Dt");
        //        columnHeader.add("HRA No.of Inc");
        //        columnHeader.add("HRA Total Amount");
        ////        columnHeader.add("City Type");
        //        columnHeader.add("Authorize Status");
        //        data = new ArrayList();
        //        tbmHRAllowance = new TableModel(data,columnHeader);
        //        columnHeader = new ArrayList();
        //        columnHeader.add("Grade");
        //        columnHeader.add("From Dt");
        //        columnHeader.add("TA No.of Inc");
        //        columnHeader.add("TA Total Amount");
        //        columnHeader.add("Authorize Status");
        //        data = new ArrayList();
        //        tbmTAllowance = new TableModel(data,columnHeader);
        //        columnHeader = new ArrayList();
        //        columnHeader.add("Grade");
        //        columnHeader.add("From Dt");
        //        columnHeader.add("MA No.of Inc");
        //        columnHeader.add("Authorize Status");
        //        data = new ArrayList();
        //        tbmMAllowance = new TableModel(data,columnHeader);
        //        columnHeader = new ArrayList();
        //        columnHeader.add("Grade");
        //        columnHeader.add("From Dt");
        //        columnHeader.add("OA No.of Inc");
        //        columnHeader.add("OA No.of Inc");
        //        columnHeader.add("Authorize Status");
        //        data = new ArrayList();
        //        tbmOAllowance = new TableModel(data,columnHeader);
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
        lookupKey.add("OTHER_DEDUCTIONS");
        lookupKey.add("ELIGIBLE_ALLOWANCE");
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        this.cbmHaltingDesignationValue = new ComboBoxModel(key,value);
        this.cbmMisecllaniousDeduction = new ComboBoxModel(key,value);
        this.cbmGratuityDesignation = new ComboBoxModel(key,value);
        //        this.cbmHRAllowance = new ComboBoxModel(key,value);
        //        this.cbmTAllowance = new ComboBoxModel(key,value);
        //        this.cbmMAidDesg = new ComboBoxModel(key,value);
        //        this.cbmOADesignationValue = new ComboBoxModel(key,value);
        //        fillData((HashMap)lookupValues.get("CITY_TYPE"));
        //        this.cbmHaltingAllowanceTypeValue = new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("ELIGIBLE_ALLOWANCE"));
        this.cbmMDEligibleAllowances = new ComboBoxModel(key,value);
        
        //        this.cbmHRAllowanceCityType = new ComboBoxModel(key,value);
        //        this.cbmTAllowanceCityType = new ComboBoxModel(key,value);
        //        fillData((HashMap)lookupValues.get("PERIOD"));
        //        this.cbmSalaryStructureStagnationOnceIn = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        this.cbmHaltingAllowanceTypeValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("OTHER_DEDUCTIONS"));
        this.cbmMDCityType = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("PARAMETER_SAL_STRUCTURE"));
        this.cbmHaltingParameterBasedOnValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        //        fillData((HashMap)lookupValues.get("OTHER_DEDUCTIONS"));
        this.cbmHaltingSubParameterValue = new ComboBoxModel(key,value);
        
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
        resetHA();
        resetMDValues();
        resetGratuityValues();
        resetHRAValues();
        resetTAValues();
        resetMAValues();
        //        resetOAValues();
        resetOAValues();
        setChanged();
        notifyObservers();
    }
    
    public void resetHA(){
        setCboHaltingDesignationValue("");
        setCboHaltingAllowanceTypeValue("");
        setCboHaltingParameterBasedOnValue("");
        setCboHaltingSubParameterValue("");
        cbmHaltingDesignationValue.setKeyForSelected("");
        cbmHaltingAllowanceTypeValue.setKeyForSelected("");
        cbmHaltingParameterBasedOnValue.setKeyForSelected("");
        cbmHaltingSubParameterValue.setKeyForSelected("");
        setLblHaltingSLNOValue("");
        setTdtHaltingFromDateValue("");
        setTdtHaltingToDateValue("");
        setTxtHaltingFixedAmtValue("");
        setTxtHaltingMaximumOfValue("");
        setTxtHaltingPercentageValue("");
        setChkFixedType(false);
        setChkPercentageType(false);
    }
    
    public void resetMDValues(){
        cbmMisecllaniousDeduction.setKeyForSelected("");
        cbmMDCityType.setKeyForSelected("");
        cbmMDEligibleAllowances.setKeyForSelected("");
        setCboMisecllaniousDeduction("");
        setCboMDCityType("");
        setCboMDEligibleAllowances("");
        setLblMDSLNOValue("");
        setTdtMDFromDateValue("");
        setTdtMDToDateValue("");
        setTxtMDBasedAmtValue("");
        setTxtMdFixedAmtValue("");
        setTxtFromAmount("");
        setTxtToAmount("");
        setRdoUsingBasic("");
        setMdFixedOrPercentage("");
        setTxtMDMaximumAmtValue("");
        setTxtMDEligiblePercentageValue("");
    }
    
    public void resetGratuityValues(){
        cbmGratuityDesignation.setKeyForSelected("");
        setCboGratuityDesignation("");
        setLblGratuitySLNOValue("");
        setTdtGratuityFromDateValue("");
        setTdtGratuityToDateValue("");
        setTxtGratuityUptoValue("");
        setTxtGratuityUptoServiceValue("");
        setTxtGratuityMaximumOfValue("");
        setTxtGratuityBeyondValue("");
        setTxtGratuityBeyondServiceValue("");
        setTxtGratuityMaximumAmtBeyongValue("");
    }
    
    public void resetHRAValues(){
        //        setCboHRAllowanceCityType("");
        //        setCboHRAllowanceDesignation("");
        //        cbmHRAllowance.setKeyForSelected("");
        //        cbmHRAllowanceCityType.setKeyForSelected("");
        //        setTdtHRAllowanceFromDateValue("");
        //        setTdtHRAllowanceToDateValue("");
        //        setTxtHRAllowanceStartingAmtValue("");
        //        setTxtHRAllowanceEndingAmtValue("");
        //        setRdoHRAPayable_Yes(false);
        //        setRdoHRAPayable_No(false);
    }
    
    public void resetTAValues(){
        //        setCboTAllowanceDesgination("");
        //        setCboHRAllowanceDesignation("");
        //        cbmTAllowance.setKeyForSelected("");
        //        cbmHRAllowanceCityType.setKeyForSelected("");
        //        setTdtTAFromDateValue("");
        //        setTdtTAToDateValue("");
        //        setTxtBasicAmtUptoValue("");
        //        setTxtConveyancePerMonthValue("");
        //        setTxtBasicAmtBeyondValue("");
        //        setTxtConveyanceAmtValue("");
        //        setTxtNooflitresValue("");
        //        setTxtPricePerlitreValue("");
        //        setTxtTotalConveyanceAmtValue("");
        //        setChkFixedConveyance(false);
        //        setChkPetrolAllowance(false);
    }
    
    public void resetMAValues(){
        //        setCboMAidDesg("");
        //        cbmMAidDesg.setKeyForSelected("");
        //        setTdtMAidFromDateValue("");
        //        setTdtMAidToDateValue("");
        //        setTxtMAidAmtValue("");
    }
    
    public void resetOAValues(){
        //        setCboMAidDesg("");
        //        cbmMAidDesg.setKeyForSelected("");
        //        setTdtMAidFromDateValue("");
        //        setTdtMAidToDateValue("");
        //        setTxtMAidAmtValue("");
        //        setCboOADesignationValue("");
        //        setCboOAllowanceTypeValue("");
        //        setCboOAParameterBasedOnValue("");
        //        setCboOASubParameterValue("");
        //        setTdtOAFromDateValue("");
        //        setTdtOAToDateValue("");
        //        setChkOAFixedValue(false);
        //        setChkOAPercentageValue(false);
        //        setTxtOAFixedAmtValue("");
        //        setTxtOAPercentageValue("");
        //        setTxtOAMaximumOfValue("");
        //        setTxtOAWashingAllownaceValue("");
        //        setTxtOACycleAllowanceValue("");
        //        setTxtOAShiftDutyAllowanceValue("");
    }
    
    private void setTableDataHA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        HaltingAllowanceTO objHA ;
        int size = HaltingTOs.size();
        for(int i=0;i<size;i++){
            objHA = (HaltingAllowanceTO)HaltingTOs.get(i);
            objHA.setStatus(CommonConstants.STATUS_MODIFIED);
            setLblHATempSLNoValue(objHA.getTempSlNo());
            setCboHaltingDesignationValue(objHA.getHalting_grade());
            row = setRowHA(objHA);
            rows.add(row);
        }
        tbmHalting.setData(rows);
        tbmHalting.fireTableDataChanged();
    }
    
    private void setTableDataMD(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        MisecllaniousDeductionTO objMD ;
        int size = MisecllaniousDeductionTOs.size();
        for(int i=0;i<size;i++){
            objMD = (MisecllaniousDeductionTO)MisecllaniousDeductionTOs.get(i);
            objMD.setStatus(CommonConstants.STATUS_MODIFIED);
            setLblMDTempSLNoValue(objMD.getTempSlNo());
            setCboMisecllaniousDeduction(objMD.getMd_grade()); 
            row = setRowMD(objMD);
            rows.add(row);
        }
        tbmMisecllaniousDeduction.setData(rows);
        tbmMisecllaniousDeduction.fireTableDataChanged();
    }
    //
    private void setTableDataGA(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        GratuityTO objGA ;
        int size = GratuityTOs.size();
        for(int i=0;i<size;i++){
            objGA = (GratuityTO)GratuityTOs.get(i);
            objGA.setStatus(CommonConstants.STATUS_MODIFIED);
            setLblGATempSLNoValue(objGA.getTempSlNo());
            row = setRowGratuity(objGA);
            rows.add(row);
        }
        tbmGratuity.setData(rows);
        tbmGratuity.fireTableDataChanged();
    }
    //
    //    private void setTableDataTA(){
    //        ArrayList row;
    //        ArrayList rows = new ArrayList();
    //        TAllowanceTO objTA ;
    //        int size = TAllowanceTOs.size();
    //        for(int i=0;i<size;i++){
    //            objTA = (TAllowanceTO)TAllowanceTOs.get(i);
    //            objTA.setStatus(CommonConstants.STATUS_MODIFIED);
    //            row = populateAndsetTARow(objTA);
    //            rows.add(row);
    //        }
    //        tbmTAllowance.setData(rows);
    //        tbmTAllowance.fireTableDataChanged();
    //    }
    //
    //    private void setTableDataMA(){
    //        ArrayList row;
    //        ArrayList rows = new ArrayList();
    //        MAllowanceTO objMA ;
    //        int size = MAllowanceTOs.size();
    //        for(int i=0;i<size;i++){
    //            objMA = (MAllowanceTO)MAllowanceTOs.get(i);
    //            objMA.setStatus(CommonConstants.STATUS_MODIFIED);
    //            row = populateAndsetMARow(objMA);
    //            rows.add(row);
    //        }
    //        tbmMAllowance.setData(rows);
    //        tbmMAllowance.fireTableDataChanged();
    //    }
    //
    //    private void setTableDataOA(){
    //        ArrayList row;
    //        ArrayList rows = new ArrayList();
    //        OtherAllowanceTO objOA ;
    //        int size = HaltingTOs.size();
    //        for(int i=0;i<size;i++){
    //            objOA = (OtherAllowanceTO)HaltingTOs.get(i);
    //            objOA.setStatus(CommonConstants.STATUS_MODIFIED);
    //            row = populateAndsetOARow(objOA);
    //            rows.add(row);
    //        }
    //        tbmOAllowance.setData(rows);
    //        tbmOAllowance.fireTableDataChanged();
    //    }
    //
    public void setCbmProdId(String prodType) {
        if (cbmHaltingDesignationValue.getKeyForSelected()!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmHaltingDesignationValue.getKeyForSelected());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmHaltingDesignationValue.getKeyForSelected()));
                this.cbmHaltingAllowanceTypeValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmHaltingAllowanceTypeValue = new ComboBoxModel(key,value);
        this.cbmHaltingAllowanceTypeValue = cbmHaltingAllowanceTypeValue;
        setChanged();
    }
    //
    public void setCbmProd(String prodType) {
        if (cbmHaltingParameterBasedOnValue.getKeyForSelected()!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmHaltingParameterBasedOnValue.getKeyForSelected());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmHaltingParameterBasedOnValue.getKeyForSelected()));
                this.cbmHaltingSubParameterValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmHaltingSubParameterValue = new ComboBoxModel(key,value);
        this.cbmHaltingSubParameterValue = cbmHaltingSubParameterValue;
        setChanged();
    }
    //
    public void getSalaryStructureData(String grade,int panEditDelete){
        HashMap whereMap = new HashMap();
        String mapNameHA = "";
        String mapNameMD = "";
        String mapNameGA = "";
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        whereMap.put("GRADE",grade);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            if(panEditDelete == HALTING_ALLOWANCE){
                mapNameHA = "getSelectHaltingEditStructureTO";
                List list = ClientUtil.executeQuery(mapNameHA,whereMap);
                if(list!=null && list.size()>0){
                    HaltingTOs = (ArrayList)list;
                    System.out.println("@#$@#$#GratuityTOs");
                    setTableDataHA();
                }
            }
            if(panEditDelete == MSCL_DEDUCTION){
                whereMap.put("MD_DEDUCTION_TYPE",CommonUtil.convertObjToStr(getCboMDCityType()));
                mapNameMD = "getSelectMisecllaniousEditStructureTO";
                List list = ClientUtil.executeQuery(mapNameMD,whereMap);
                if(list!=null && list.size()>0){
                    MisecllaniousDeductionTOs = (ArrayList)list;
                    System.out.println("@#$@#$#GratuityTOs");
                    setTableDataMD();
                }
            }
            if(panEditDelete == GRATUITY){
                mapNameGA = "getSelectGratuityEditStructureTO";
                List list = ClientUtil.executeQuery(mapNameGA,whereMap);
                if(list!=null && list.size()>0){
                    GratuityTOs = (ArrayList)list;
                    System.out.println("@#$@#$#GratuityTOs");
                    setTableDataGA();
                }
            }
        }
        whereMap = null;
    }
    public void doAction(){
        try{
            HashMap objHashMap=null;
            if(authorizeMap==null) {
                //                HaltingTOs.addAll(deleteHaltingList);
                System.out.println("@#$@#$@#$@#$HaltingTOs:"+HaltingTOs);
                //                MisecllaniousDeductionTOs.addAll(deleteMisecllaniousDeductionList);
                //                GratuityTOs.addAll(deleteGratuityList);
                if((HaltingTOs != null && HaltingTOs.size()>0) || (MisecllaniousDeductionTOs != null && MisecllaniousDeductionTOs.size()>0) || (GratuityTOs != null && GratuityTOs.size()>0)
                || (deleteHaltingList != null && deleteHaltingList.size()>0)|| (deleteMisecllaniousDeductionList != null && deleteMisecllaniousDeductionList.size()>0) || (deleteGratuityList != null && deleteGratuityList.size()>0)) {
                    objHashMap = new HashMap();
                    if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                        //                        setStatusForTOs();
                    }
                    if(HaltingTOs != null && HaltingTOs.size()>0){
                        objHashMap.put("HaltingTOs",HaltingTOs);
                        HaltingTOs = null;
                    }
                    if(deleteHaltingList != null && deleteHaltingList.size()>0){
                        objHashMap.put("deleteHaltingList",deleteHaltingList);
                        deleteHaltingList = null;
                    }
                    if(MisecllaniousDeductionTOs != null && MisecllaniousDeductionTOs.size()>0){
                        objHashMap.put("MisecllaniousDeductionTOs",MisecllaniousDeductionTOs);
                        MisecllaniousDeductionTOs = null;
                    }
                    if(deleteMisecllaniousDeductionList != null && deleteMisecllaniousDeductionList.size()>0){
                        objHashMap.put("deleteMisecllaniousDeductionList",deleteMisecllaniousDeductionList);
                        deleteMisecllaniousDeductionList = null;
                    }
                    if(GratuityTOs != null && GratuityTOs.size()>0){
                        objHashMap.put("GratuityTOs",GratuityTOs);
                        GratuityTOs = null;
                    }
                    if(deleteGratuityList != null && deleteGratuityList.size()>0){
                        objHashMap.put("deleteGratuityList",deleteGratuityList);
                        deleteGratuityList = null;
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
    //
    //    private ArrayList setEditedRow(HaltingAllowanceTO obj){
    //        ArrayList row= new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getStartingScaleAmt()));
    //        if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("NI")){
    //            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
    //            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
    //            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
    //            row.add("NI");
    //        }else if(CommonUtil.convertObjToStr(obj.getSalaryType()).equals("SI")){
    //            row.add(CommonUtil.convertObjToStr(obj.getIncrementAmt()));
    //            row.add(CommonUtil.convertObjToStr(obj.getNoOfIncrement()));
    //            row.add(CommonUtil.convertObjToStr(obj.getSingleRowTotAmt()));
    //            row.add("SI");
    //        }
    //        row.add(CommonUtil.convertObjToStr(obj.getFromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    //        return row;
    //    }
    //
    //    private void setStatusForTOs(){
    //        SalaryStructureTO objTO;
    //        int size = SalaryStructureTOs.size();
    //        for(int i=0;i<size;i++){
    //            objTO =(SalaryStructureTO)SalaryStructureTOs.get(i);
    //            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
    //            SalaryStructureTOs.set(i, objTO);
    //        }
    //        objTO=null;
    //    }
    //    public void populateSalaryStructure(String lienNo){
    //        SalaryStructureTO obj;
    //        int size = SalaryStructureTOs.size();
    //        for(int i=0;i<size;i++){
    //            obj = (SalaryStructureTO)SalaryStructureTOs.get(i);
    //            if(obj != null){
    //                this.populateSalaryStructure(i);
    //            }
    //            obj = null;
    //            return;
    //        }
    //    }
    ////    public java.lang.String getAuthorizeStatus(){
    ////        if(this.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE)
    ////            return CommonConstants.STATUS_AUTHORIZED;
    ////        else if(this.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION)
    ////            return CommonConstants.STATUS_EXCEPTION;
    ////        else if(this.getActionType()==ClientConstants.ACTIONTYPE_REJECT)
    ////            return CommonConstants.STATUS_REJECTED;
    ////        return "";
    ////    }
    public void resetTabel(){
        this.tbmHalting.setData(new ArrayList());
        this.tbmHalting.fireTableDataChanged();
        HaltingTOs = null;
        this.tbmMisecllaniousDeduction.setData(new ArrayList());
        this.tbmMisecllaniousDeduction.fireTableDataChanged();
        MisecllaniousDeductionTOs = null;
        this.tbmGratuity.setData(new ArrayList());
        this.tbmGratuity.fireTableDataChanged();
        GratuityTOs = null;
    }
    
    /**
     * Getter for property cbmHaltingDesignationValue.
     * @return Value of property cbmHaltingDesignationValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHaltingDesignationValue() {
        return cbmHaltingDesignationValue;
    }
    
    /**
     * Setter for property cbmHaltingDesignationValue.
     * @param cbmHaltingDesignationValue New value of property cbmHaltingDesignationValue.
     */
    public void setCbmHaltingDesignationValue(com.see.truetransact.clientutil.ComboBoxModel cbmHaltingDesignationValue) {
        this.cbmHaltingDesignationValue = cbmHaltingDesignationValue;
    }
    
    /**
     * Getter for property cbmHaltingAllowanceTypeValue.
     * @return Value of property cbmHaltingAllowanceTypeValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHaltingAllowanceTypeValue() {
        return cbmHaltingAllowanceTypeValue;
    }
    
    /**
     * Setter for property cbmHaltingAllowanceTypeValue.
     * @param cbmHaltingAllowanceTypeValue New value of property cbmHaltingAllowanceTypeValue.
     */
    public void setCbmHaltingAllowanceTypeValue(com.see.truetransact.clientutil.ComboBoxModel cbmHaltingAllowanceTypeValue) {
        this.cbmHaltingAllowanceTypeValue = cbmHaltingAllowanceTypeValue;
    }
    
    /**
     * Getter for property cbmHaltingParameterBasedOnValue.
     * @return Value of property cbmHaltingParameterBasedOnValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHaltingParameterBasedOnValue() {
        return cbmHaltingParameterBasedOnValue;
    }
    
    /**
     * Setter for property cbmHaltingParameterBasedOnValue.
     * @param cbmHaltingParameterBasedOnValue New value of property cbmHaltingParameterBasedOnValue.
     */
    public void setCbmHaltingParameterBasedOnValue(com.see.truetransact.clientutil.ComboBoxModel cbmHaltingParameterBasedOnValue) {
        this.cbmHaltingParameterBasedOnValue = cbmHaltingParameterBasedOnValue;
    }
    
    /**
     * Getter for property cbmHaltingSubParameterValue.
     * @return Value of property cbmHaltingSubParameterValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmHaltingSubParameterValue() {
        return cbmHaltingSubParameterValue;
    }
    
    /**
     * Setter for property cbmHaltingSubParameterValue.
     * @param cbmHaltingSubParameterValue New value of property cbmHaltingSubParameterValue.
     */
    public void setCbmHaltingSubParameterValue(com.see.truetransact.clientutil.ComboBoxModel cbmHaltingSubParameterValue) {
        this.cbmHaltingSubParameterValue = cbmHaltingSubParameterValue;
    }
    
    /**
     * Getter for property tbmHalting.
     * @return Value of property tbmHalting.
     */
    public com.see.truetransact.clientutil.TableModel getTbmHalting() {
        return tbmHalting;
    }
    
    /**
     * Setter for property tbmHalting.
     * @param tbmHalting New value of property tbmHalting.
     */
    public void setTbmHalting(com.see.truetransact.clientutil.TableModel tbmHalting) {
        this.tbmHalting = tbmHalting;
    }
    
    /**
     * Getter for property cbmMisecllaniousDeduction.
     * @return Value of property cbmMisecllaniousDeduction.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMisecllaniousDeduction() {
        return cbmMisecllaniousDeduction;
    }
    
    /**
     * Setter for property cbmMisecllaniousDeduction.
     * @param cbmMisecllaniousDeduction New value of property cbmMisecllaniousDeduction.
     */
    public void setCbmMisecllaniousDeduction(com.see.truetransact.clientutil.ComboBoxModel cbmMisecllaniousDeduction) {
        this.cbmMisecllaniousDeduction = cbmMisecllaniousDeduction;
    }
    
    /**
     * Getter for property cbmMDCityType.
     * @return Value of property cbmMDCityType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMDCityType() {
        return cbmMDCityType;
    }
    
    /**
     * Setter for property cbmMDCityType.
     * @param cbmMDCityType New value of property cbmMDCityType.
     */
    public void setCbmMDCityType(com.see.truetransact.clientutil.ComboBoxModel cbmMDCityType) {
        this.cbmMDCityType = cbmMDCityType;
    }
    
    /**
     * Getter for property tbmMisecllaniousDeduction.
     * @return Value of property tbmMisecllaniousDeduction.
     */
    public com.see.truetransact.clientutil.TableModel getTbmMisecllaniousDeduction() {
        return tbmMisecllaniousDeduction;
    }
    
    /**
     * Setter for property tbmMisecllaniousDeduction.
     * @param tbmMisecllaniousDeduction New value of property tbmMisecllaniousDeduction.
     */
    public void setTbmMisecllaniousDeduction(com.see.truetransact.clientutil.TableModel tbmMisecllaniousDeduction) {
        this.tbmMisecllaniousDeduction = tbmMisecllaniousDeduction;
    }
    
    /**
     * Getter for property cbmGratuityDesignation.
     * @return Value of property cbmGratuityDesignation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmGratuityDesignation() {
        return cbmGratuityDesignation;
    }
    
    /**
     * Setter for property cbmGratuityDesignation.
     * @param cbmGratuityDesignation New value of property cbmGratuityDesignation.
     */
    public void setCbmGratuityDesignation(com.see.truetransact.clientutil.ComboBoxModel cbmGratuityDesignation) {
        this.cbmGratuityDesignation = cbmGratuityDesignation;
    }
    
    /**
     * Getter for property tbmGratuity.
     * @return Value of property tbmGratuity.
     */
    public com.see.truetransact.clientutil.TableModel getTbmGratuity() {
        return tbmGratuity;
    }
    
    /**
     * Setter for property tbmGratuity.
     * @param tbmGratuity New value of property tbmGratuity.
     */
    public void setTbmGratuity(com.see.truetransact.clientutil.TableModel tbmGratuity) {
        this.tbmGratuity = tbmGratuity;
    }
    
    /**
     * Getter for property cboHaltingDesignationValue.
     * @return Value of property cboHaltingDesignationValue.
     */
    public java.lang.String getCboHaltingDesignationValue() {
        return cboHaltingDesignationValue;
    }
    
    /**
     * Setter for property cboHaltingDesignationValue.
     * @param cboHaltingDesignationValue New value of property cboHaltingDesignationValue.
     */
    public void setCboHaltingDesignationValue(java.lang.String cboHaltingDesignationValue) {
        this.cboHaltingDesignationValue = cboHaltingDesignationValue;
    }
    
    /**
     * Getter for property cboHaltingAllowanceTypeValue.
     * @return Value of property cboHaltingAllowanceTypeValue.
     */
    public java.lang.String getCboHaltingAllowanceTypeValue() {
        return cboHaltingAllowanceTypeValue;
    }
    
    /**
     * Setter for property cboHaltingAllowanceTypeValue.
     * @param cboHaltingAllowanceTypeValue New value of property cboHaltingAllowanceTypeValue.
     */
    public void setCboHaltingAllowanceTypeValue(java.lang.String cboHaltingAllowanceTypeValue) {
        this.cboHaltingAllowanceTypeValue = cboHaltingAllowanceTypeValue;
    }
    
    /**
     * Getter for property cboHaltingParameterBasedOnValue.
     * @return Value of property cboHaltingParameterBasedOnValue.
     */
    public java.lang.String getCboHaltingParameterBasedOnValue() {
        return cboHaltingParameterBasedOnValue;
    }
    
    /**
     * Setter for property cboHaltingParameterBasedOnValue.
     * @param cboHaltingParameterBasedOnValue New value of property cboHaltingParameterBasedOnValue.
     */
    public void setCboHaltingParameterBasedOnValue(java.lang.String cboHaltingParameterBasedOnValue) {
        this.cboHaltingParameterBasedOnValue = cboHaltingParameterBasedOnValue;
    }
    
    /**
     * Getter for property cboHaltingSubParameterValue.
     * @return Value of property cboHaltingSubParameterValue.
     */
    public java.lang.String getCboHaltingSubParameterValue() {
        return cboHaltingSubParameterValue;
    }
    
    /**
     * Setter for property cboHaltingSubParameterValue.
     * @param cboHaltingSubParameterValue New value of property cboHaltingSubParameterValue.
     */
    public void setCboHaltingSubParameterValue(java.lang.String cboHaltingSubParameterValue) {
        this.cboHaltingSubParameterValue = cboHaltingSubParameterValue;
    }
    
    /**
     * Getter for property lblHaltingSLNOValue.
     * @return Value of property lblHaltingSLNOValue.
     */
    public java.lang.String getLblHaltingSLNOValue() {
        return lblHaltingSLNOValue;
    }
    
    /**
     * Setter for property lblHaltingSLNOValue.
     * @param lblHaltingSLNOValue New value of property lblHaltingSLNOValue.
     */
    public void setLblHaltingSLNOValue(java.lang.String lblHaltingSLNOValue) {
        this.lblHaltingSLNOValue = lblHaltingSLNOValue;
    }
    
    /**
     * Getter for property tdtHaltingFromDateValue.
     * @return Value of property tdtHaltingFromDateValue.
     */
    public java.lang.String getTdtHaltingFromDateValue() {
        return tdtHaltingFromDateValue;
    }
    
    /**
     * Setter for property tdtHaltingFromDateValue.
     * @param tdtHaltingFromDateValue New value of property tdtHaltingFromDateValue.
     */
    public void setTdtHaltingFromDateValue(java.lang.String tdtHaltingFromDateValue) {
        this.tdtHaltingFromDateValue = tdtHaltingFromDateValue;
    }
    
    /**
     * Getter for property tdtHaltingToDateValue.
     * @return Value of property tdtHaltingToDateValue.
     */
    public java.lang.String getTdtHaltingToDateValue() {
        return tdtHaltingToDateValue;
    }
    
    /**
     * Setter for property tdtHaltingToDateValue.
     * @param tdtHaltingToDateValue New value of property tdtHaltingToDateValue.
     */
    public void setTdtHaltingToDateValue(java.lang.String tdtHaltingToDateValue) {
        this.tdtHaltingToDateValue = tdtHaltingToDateValue;
    }
    
    /**
     * Getter for property txtHaltingFixedAmtValue.
     * @return Value of property txtHaltingFixedAmtValue.
     */
    public java.lang.String getTxtHaltingFixedAmtValue() {
        return txtHaltingFixedAmtValue;
    }
    
    /**
     * Setter for property txtHaltingFixedAmtValue.
     * @param txtHaltingFixedAmtValue New value of property txtHaltingFixedAmtValue.
     */
    public void setTxtHaltingFixedAmtValue(java.lang.String txtHaltingFixedAmtValue) {
        this.txtHaltingFixedAmtValue = txtHaltingFixedAmtValue;
    }
    
    /**
     * Getter for property cboMisecllaniousDeduction.
     * @return Value of property cboMisecllaniousDeduction.
     */
    public java.lang.String getCboMisecllaniousDeduction() {
        return cboMisecllaniousDeduction;
    }
    
    /**
     * Setter for property cboMisecllaniousDeduction.
     * @param cboMisecllaniousDeduction New value of property cboMisecllaniousDeduction.
     */
    public void setCboMisecllaniousDeduction(java.lang.String cboMisecllaniousDeduction) {
        this.cboMisecllaniousDeduction = cboMisecllaniousDeduction;
    }
    
    /**
     * Getter for property cboMDCityType.
     * @return Value of property cboMDCityType.
     */
    public java.lang.String getCboMDCityType() {
        return cboMDCityType;
    }
    
    /**
     * Setter for property cboMDCityType.
     * @param cboMDCityType New value of property cboMDCityType.
     */
    public void setCboMDCityType(java.lang.String cboMDCityType) {
        this.cboMDCityType = cboMDCityType;
    }
    
    /**
     * Getter for property lblMDSLNOValue.
     * @return Value of property lblMDSLNOValue.
     */
    public java.lang.String getLblMDSLNOValue() {
        return lblMDSLNOValue;
    }
    
    /**
     * Setter for property lblMDSLNOValue.
     * @param lblMDSLNOValue New value of property lblMDSLNOValue.
     */
    public void setLblMDSLNOValue(java.lang.String lblMDSLNOValue) {
        this.lblMDSLNOValue = lblMDSLNOValue;
    }
    
    /**
     * Getter for property tdtMDFromDateValue.
     * @return Value of property tdtMDFromDateValue.
     */
    public java.lang.String getTdtMDFromDateValue() {
        return tdtMDFromDateValue;
    }
    
    /**
     * Setter for property tdtMDFromDateValue.
     * @param tdtMDFromDateValue New value of property tdtMDFromDateValue.
     */
    public void setTdtMDFromDateValue(java.lang.String tdtMDFromDateValue) {
        this.tdtMDFromDateValue = tdtMDFromDateValue;
    }
    
    /**
     * Getter for property tdtMDToDateValue.
     * @return Value of property tdtMDToDateValue.
     */
    public java.lang.String getTdtMDToDateValue() {
        return tdtMDToDateValue;
    }
    
    /**
     * Setter for property tdtMDToDateValue.
     * @param tdtMDToDateValue New value of property tdtMDToDateValue.
     */
    public void setTdtMDToDateValue(java.lang.String tdtMDToDateValue) {
        this.tdtMDToDateValue = tdtMDToDateValue;
    }
    
    /**
     * Getter for property txtMDBasedAmtValue.
     * @return Value of property txtMDBasedAmtValue.
     */
    public java.lang.String getTxtMDBasedAmtValue() {
        return txtMDBasedAmtValue;
    }
    
    /**
     * Setter for property txtMDBasedAmtValue.
     * @param txtMDBasedAmtValue New value of property txtMDBasedAmtValue.
     */
    public void setTxtMDBasedAmtValue(java.lang.String txtMDBasedAmtValue) {
        this.txtMDBasedAmtValue = txtMDBasedAmtValue;
    }
    
    /**
     * Getter for property txtMDMaximumAmtValue.
     * @return Value of property txtMDMaximumAmtValue.
     */
    public java.lang.String getTxtMDMaximumAmtValue() {
        return txtMDMaximumAmtValue;
    }
    
    /**
     * Setter for property txtMDMaximumAmtValue.
     * @param txtMDMaximumAmtValue New value of property txtMDMaximumAmtValue.
     */
    public void setTxtMDMaximumAmtValue(java.lang.String txtMDMaximumAmtValue) {
        this.txtMDMaximumAmtValue = txtMDMaximumAmtValue;
    }
    
    /**
     * Getter for property cboGratuityDesignation.
     * @return Value of property cboGratuityDesignation.
     */
    public java.lang.String getCboGratuityDesignation() {
        return cboGratuityDesignation;
    }
    
    /**
     * Setter for property cboGratuityDesignation.
     * @param cboGratuityDesignation New value of property cboGratuityDesignation.
     */
    public void setCboGratuityDesignation(java.lang.String cboGratuityDesignation) {
        this.cboGratuityDesignation = cboGratuityDesignation;
    }
    
    /**
     * Getter for property lblGratuitySLNOValue.
     * @return Value of property lblGratuitySLNOValue.
     */
    public java.lang.String getLblGratuitySLNOValue() {
        return lblGratuitySLNOValue;
    }
    
    /**
     * Setter for property lblGratuitySLNOValue.
     * @param lblGratuitySLNOValue New value of property lblGratuitySLNOValue.
     */
    public void setLblGratuitySLNOValue(java.lang.String lblGratuitySLNOValue) {
        this.lblGratuitySLNOValue = lblGratuitySLNOValue;
    }
    
    /**
     * Getter for property tdtGratuityFromDateValue.
     * @return Value of property tdtGratuityFromDateValue.
     */
    public java.lang.String getTdtGratuityFromDateValue() {
        return tdtGratuityFromDateValue;
    }
    
    /**
     * Setter for property tdtGratuityFromDateValue.
     * @param tdtGratuityFromDateValue New value of property tdtGratuityFromDateValue.
     */
    public void setTdtGratuityFromDateValue(java.lang.String tdtGratuityFromDateValue) {
        this.tdtGratuityFromDateValue = tdtGratuityFromDateValue;
    }
    
    /**
     * Getter for property tdtGratuityToDateValue.
     * @return Value of property tdtGratuityToDateValue.
     */
    public java.lang.String getTdtGratuityToDateValue() {
        return tdtGratuityToDateValue;
    }
    
    /**
     * Setter for property tdtGratuityToDateValue.
     * @param tdtGratuityToDateValue New value of property tdtGratuityToDateValue.
     */
    public void setTdtGratuityToDateValue(java.lang.String tdtGratuityToDateValue) {
        this.tdtGratuityToDateValue = tdtGratuityToDateValue;
    }
    
    /**
     * Getter for property txtGratuityUptoValue.
     * @return Value of property txtGratuityUptoValue.
     */
    public java.lang.String getTxtGratuityUptoValue() {
        return txtGratuityUptoValue;
    }
    
    /**
     * Setter for property txtGratuityUptoValue.
     * @param txtGratuityUptoValue New value of property txtGratuityUptoValue.
     */
    public void setTxtGratuityUptoValue(java.lang.String txtGratuityUptoValue) {
        this.txtGratuityUptoValue = txtGratuityUptoValue;
    }
    
    /**
     * Getter for property txtGratuityUptoServiceValue.
     * @return Value of property txtGratuityUptoServiceValue.
     */
    public java.lang.String getTxtGratuityUptoServiceValue() {
        return txtGratuityUptoServiceValue;
    }
    
    /**
     * Setter for property txtGratuityUptoServiceValue.
     * @param txtGratuityUptoServiceValue New value of property txtGratuityUptoServiceValue.
     */
    public void setTxtGratuityUptoServiceValue(java.lang.String txtGratuityUptoServiceValue) {
        this.txtGratuityUptoServiceValue = txtGratuityUptoServiceValue;
    }
    
    /**
     * Getter for property txtGratuityMaximumOfValue.
     * @return Value of property txtGratuityMaximumOfValue.
     */
    public java.lang.String getTxtGratuityMaximumOfValue() {
        return txtGratuityMaximumOfValue;
    }
    
    /**
     * Setter for property txtGratuityMaximumOfValue.
     * @param txtGratuityMaximumOfValue New value of property txtGratuityMaximumOfValue.
     */
    public void setTxtGratuityMaximumOfValue(java.lang.String txtGratuityMaximumOfValue) {
        this.txtGratuityMaximumOfValue = txtGratuityMaximumOfValue;
    }
    
    /**
     * Getter for property txtGratuityBeyondValue.
     * @return Value of property txtGratuityBeyondValue.
     */
    public java.lang.String getTxtGratuityBeyondValue() {
        return txtGratuityBeyondValue;
    }
    
    /**
     * Setter for property txtGratuityBeyondValue.
     * @param txtGratuityBeyondValue New value of property txtGratuityBeyondValue.
     */
    public void setTxtGratuityBeyondValue(java.lang.String txtGratuityBeyondValue) {
        this.txtGratuityBeyondValue = txtGratuityBeyondValue;
    }
    
    /**
     * Getter for property txtGratuityBeyondServiceValue.
     * @return Value of property txtGratuityBeyondServiceValue.
     */
    public java.lang.String getTxtGratuityBeyondServiceValue() {
        return txtGratuityBeyondServiceValue;
    }
    
    /**
     * Setter for property txtGratuityBeyondServiceValue.
     * @param txtGratuityBeyondServiceValue New value of property txtGratuityBeyondServiceValue.
     */
    public void setTxtGratuityBeyondServiceValue(java.lang.String txtGratuityBeyondServiceValue) {
        this.txtGratuityBeyondServiceValue = txtGratuityBeyondServiceValue;
    }
    
    /**
     * Getter for property txtHaltingMaximumOfValue.
     * @return Value of property txtHaltingMaximumOfValue.
     */
    public java.lang.String getTxtHaltingMaximumOfValue() {
        return txtHaltingMaximumOfValue;
    }
    
    /**
     * Setter for property txtHaltingMaximumOfValue.
     * @param txtHaltingMaximumOfValue New value of property txtHaltingMaximumOfValue.
     */
    public void setTxtHaltingMaximumOfValue(java.lang.String txtHaltingMaximumOfValue) {
        this.txtHaltingMaximumOfValue = txtHaltingMaximumOfValue;
    }
    
    /**
     * Getter for property txtHaltingPercentageValue.
     * @return Value of property txtHaltingPercentageValue.
     */
    public java.lang.String getTxtHaltingPercentageValue() {
        return txtHaltingPercentageValue;
    }
    
    /**
     * Setter for property txtHaltingPercentageValue.
     * @param txtHaltingPercentageValue New value of property txtHaltingPercentageValue.
     */
    public void setTxtHaltingPercentageValue(java.lang.String txtHaltingPercentageValue) {
        this.txtHaltingPercentageValue = txtHaltingPercentageValue;
    }
    
    /**
     * Getter for property txtGratuityMaximumAmtBeyongValue.
     * @return Value of property txtGratuityMaximumAmtBeyongValue.
     */
    public java.lang.String getTxtGratuityMaximumAmtBeyongValue() {
        return txtGratuityMaximumAmtBeyongValue;
    }
    
    /**
     * Setter for property txtGratuityMaximumAmtBeyongValue.
     * @param txtGratuityMaximumAmtBeyongValue New value of property txtGratuityMaximumAmtBeyongValue.
     */
    public void setTxtGratuityMaximumAmtBeyongValue(java.lang.String txtGratuityMaximumAmtBeyongValue) {
        this.txtGratuityMaximumAmtBeyongValue = txtGratuityMaximumAmtBeyongValue;
    }
    
    /**
     * Getter for property cboMDEligibleAllowances.
     * @return Value of property cboMDEligibleAllowances.
     */
    public java.lang.String getCboMDEligibleAllowances() {
        return cboMDEligibleAllowances;
    }
    
    /**
     * Setter for property cboMDEligibleAllowances.
     * @param cboMDEligibleAllowances New value of property cboMDEligibleAllowances.
     */
    public void setCboMDEligibleAllowances(java.lang.String cboMDEligibleAllowances) {
        this.cboMDEligibleAllowances = cboMDEligibleAllowances;
    }
    
    /**
     * Getter for property txtMDEligiblePercentageValue.
     * @return Value of property txtMDEligiblePercentageValue.
     */
    public java.lang.String getTxtMDEligiblePercentageValue() {
        return txtMDEligiblePercentageValue;
    }
    
    /**
     * Setter for property txtMDEligiblePercentageValue.
     * @param txtMDEligiblePercentageValue New value of property txtMDEligiblePercentageValue.
     */
    public void setTxtMDEligiblePercentageValue(java.lang.String txtMDEligiblePercentageValue) {
        this.txtMDEligiblePercentageValue = txtMDEligiblePercentageValue;
    }
    
    /**
     * Getter for property cbmMDEligibleAllowances.
     * @return Value of property cbmMDEligibleAllowances.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMDEligibleAllowances() {
        return cbmMDEligibleAllowances;
    }
    
    /**
     * Setter for property cbmMDEligibleAllowances.
     * @param cbmMDEligibleAllowances New value of property cbmMDEligibleAllowances.
     */
    public void setCbmMDEligibleAllowances(com.see.truetransact.clientutil.ComboBoxModel cbmMDEligibleAllowances) {
        this.cbmMDEligibleAllowances = cbmMDEligibleAllowances;
    }
    
    //    public void deleteDAData(int rowNum) {
    //        deleteEnable = true;
    //        DearnessAllowanceTO obj = (DearnessAllowanceTO)DAllowancesTOs.get(rowNum);
    //        obj.setStatus(CommonConstants.STATUS_DELETED);
    //        deleteDAllowancesList.add(obj);
    //        DAllowancesTOs.remove(rowNum);
    //        tbmDAllowance.removeRow(rowNum);
    //        tbmDAllowance.fireTableDataChanged();
    //        obj = null;
    //    }
    //    public int insertDAData(int rowNo) {
    //        DearnessAllowanceTO obj = DearnessAllowanceTO(rowNo);
    //        if(rowNo == -1){
    //            DAllowancesTOs.add(obj);
    //            ArrayList irRow = this.setRowDA(obj);
    //            tbmDAllowance.insertRow(tbmDAllowance.getRowCount(), irRow);
    //            tbmDAllowance.fireTableDataChanged();
    //        }
    //        obj = null;
    //        return 0;
    //    }
    //
    //    private ArrayList setRowDA(MisecllaniousDeductionTO obj){
    //        ArrayList row= new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAPercentagePerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    //        return row;
    //    }
    //
    //    private MisecllaniousDeductionTO MisecllaniousDeductionTO(int rowNo){
    //        double startingAmt = 0.0;
    //        DearnessAllowanceTO obj = null;
    //        if(rowNo == -1){
    //            obj = new DearnessAllowanceTO();
    //            if(tbmDAllowance.getRowCount() == 0){
    //                obj.setDAslNo(String.valueOf(1));
    //            }else if(tbmDAllowance.getRowCount() >=1){
    //                obj.setDAslNo(String.valueOf(tbmDAllowance.getRowCount()+1));
    //            }
    //            obj.setDAgrade(getCboDADesignationValue());
    //            obj.setDAfromDate(DateUtil.getDateMMDDYYYY(getTdtDAFromDateValue()));
    //            obj.setDAtoDate(DateUtil.getDateMMDDYYYY(getTdtDAToDateValue()));
    //            obj.setDANoOfPointsPerSlab(getTxtDANoOfPointsPerSlabValue());
    //            obj.setDAIndex(getTxtDAIndexValue());
    //            obj.setDAPercentagePerSlab(getTxtDAPercentagePerSlabValue());
    //            obj.setDATotalNoOfSlab(getTxtTotalNoofSlabValue());
    //            obj.setDATotalDAPercentage(getTxtDATotalDAPercentageValue());
    //            obj.setStatus(CommonConstants.STATUS_CREATED);
    //            obj.setStatusBy(TrueTransactMain.USER_ID);
    //            obj.setStatusDate(curDate);
    //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //        }else{
    ////            int i = rowNo;
    ////            for (;i<tbmDAllowance.getRowCount();i++){
    //                obj = new DearnessAllowanceTO();
    //                obj.setDAgrade(getCboDADesignationValue());
    //                obj.setDAfromDate(DateUtil.getDateMMDDYYYY(getTdtDAFromDateValue()));
    //                obj.setDAtoDate(DateUtil.getDateMMDDYYYY(getTdtDAToDateValue()));
    //                if(rowNo == 0){
    //                    obj.setDAslNo(String.valueOf(1));
    //                }else{
    //                    int no = rowNo + 1;
    //                    obj.setDAslNo(String.valueOf(no));
    //                }
    ////                if(i == rowNo){
    //                    obj.setDANoOfPointsPerSlab(getTxtDANoOfPointsPerSlabValue());
    //                    obj.setDAIndex(getTxtDAIndexValue());
    //                    obj.setDAPercentagePerSlab(getTxtDAPercentagePerSlabValue());
    //                    obj.setDATotalNoOfSlab(getTxtTotalNoofSlabValue());
    //                    obj.setDATotalDAPercentage(getTxtDATotalDAPercentageValue());
    ////                }else{
    ////                    double points = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,1)).doubleValue();
    ////                    double index = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,2)).doubleValue();
    ////                    double daPer = CommonUtil.convertObjToDouble(tbmDAllowance.getValueAt(i,3)).doubleValue();
    ////                    double TotNoOfSlab = index/points;
    ////                    double daPerSlab = TotNoOfSlab * daPer;
    ////                    obj.setDANoOfPointsPerSlab(String.valueOf(points));
    ////                    obj.setDAIndex(String.valueOf(index));
    ////                    obj.setDAPercentagePerSlab(String.valueOf(daPer));
    ////                    obj.setDATotalNoOfSlab(String.valueOf(TotNoOfSlab));
    ////                    obj.setDATotalDAPercentage(String.valueOf(daPerSlab));
    ////                }
    //                obj.setStatus(CommonConstants.STATUS_MODIFIED);
    //                obj.setStatusDate(curDate);
    //                obj.setStatusBy(TrueTransactMain.USER_ID);
    //                obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //                ArrayList irRow = setRowDA(obj);
    //                DAllowancesTOs.set(rowNo,obj);
    //                tbmDAllowance.removeRow(rowNo);
    //                tbmDAllowance.insertRow(rowNo,irRow);
    ////            }
    //            tbmDAllowance.fireTableDataChanged();
    //        }
    //        return obj;
    //    }
    //
    ////    private ArrayList setEditedDARow(DearnessAllowanceTO obj){
    ////        ArrayList row = new ArrayList();
    ////        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getDAPercentagePerSlab()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
    ////        return row;
    ////    }
    //
    //    public void populateDA(int rowNum) {
    //        DearnessAllowanceTO obj = (DearnessAllowanceTO)DAllowancesTOs.get(rowNum);
    //        this.setCboDADesignationValue(obj.getDAgrade());
    //        this.setTdtDAFromDateValue(CommonUtil.convertObjToStr(obj.getDAfromDate()));
    //        this.setTdtDAToDateValue(CommonUtil.convertObjToStr(obj.getDAtoDate()));
    //        this.setTxtDANoOfPointsPerSlabValue(obj.getDANoOfPointsPerSlab());
    //        this.setTxtDAIndexValue(obj.getDAIndex());
    //        this.setTxtDAPercentagePerSlabValue(obj.getDAPercentagePerSlab());
    //        this.setTxtTotalNoofSlabValue(obj.getDATotalNoOfSlab());
    //        this.setTxtDATotalDAPercentageValue(obj.getDATotalDAPercentage());
    //        setDAAuthorizeStatus(obj.getAuthorizeStatus());
    //        obj = null;
    //    }
    //
    //    private ArrayList populateAndsetDARow(MisecllaniousDeductionTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getDAgrade()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDANoOfPointsPerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAIndex()));
    //        row.add(CommonUtil.convertObjToStr(obj.getDAPercentagePerSlab()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    ////        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
    ////        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
    ////            row.add("No");
    ////        }
    //        return row;
    //    }
    //
    //    //MD starts//
    public int insertMDData(int rowNo) {
        MisecllaniousDeductionTO obj = MisecllaniousDeductionTO(rowNo);
        if(rowNo == -1){
            if(MisecllaniousDeductionTOs == null){
                MisecllaniousDeductionTOs = new ArrayList();
            }
            MisecllaniousDeductionTOs.add(obj);
            ArrayList irRow = this.setRowMD(obj);
            tbmMisecllaniousDeduction.insertRow(tbmMisecllaniousDeduction.getRowCount(), irRow);
            tbmMisecllaniousDeduction.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateMD(int rowNum) {
        MisecllaniousDeductionTO obj = (MisecllaniousDeductionTO)MisecllaniousDeductionTOs.get(rowNum);
        this.setCboMisecllaniousDeduction(obj.getMd_grade());
        this.setCboMDCityType(obj.getMd_deduction_type());
        this.setCboMDEligibleAllowances(obj.getEligible_allowance());
        this.setTdtMDFromDateValue(CommonUtil.convertObjToStr(obj.getMd_from_date()));
        
        this.setTdtMDToDateValue(CommonUtil.convertObjToStr(obj.getMd_to_date()));
        this.setTxtMDBasedAmtValue(CommonUtil.convertObjToStr(obj.getPercentage()));
        this.setTxtMDMaximumAmtValue(CommonUtil.convertObjToStr(obj.getMaximum_amt()));
        this.setTxtMdFixedAmtValue(CommonUtil.convertObjToStr(obj.getTxtMdFixedAmtValue()));
        this.setTxtFromAmount(CommonUtil.convertObjToStr(obj.getTxtFromAmount()));
        this.setTxtToAmount(CommonUtil.convertObjToStr(obj.getTxtToAmount()));
        this.setRdoUsingBasic(CommonUtil.convertObjToStr(obj.getRdoUsingBasic()));
        this.setMdFixedOrPercentage(CommonUtil.convertObjToStr(obj.getMdFixedOrPercentage()));
        this.setTxtMDEligiblePercentageValue(CommonUtil.convertObjToStr(obj.getEligible_percentage()));
        setMDAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private MisecllaniousDeductionTO MisecllaniousDeductionTO(int rowNo){
        double startingAmt = 0.0;
        MisecllaniousDeductionTO obj = null;
        if(rowNo == -1){
            obj = new MisecllaniousDeductionTO();
            if(tbmMisecllaniousDeduction.getRowCount() == 0){
                obj.setMd_sl_no(new Double(1));
            }else if(tbmMisecllaniousDeduction.getRowCount() >=1){
                obj.setMd_sl_no(new Double(tbmMisecllaniousDeduction.getRowCount()+1));
            }
            obj.setMd_grade(getCboMisecllaniousDeduction());
            obj.setMd_deduction_type(getCboMDCityType());
            obj.setEligible_allowance(getCboMDEligibleAllowances());
            obj.setMd_from_date(DateUtil.getDateMMDDYYYY(getTdtMDFromDateValue()));
            obj.setMd_to_date(DateUtil.getDateMMDDYYYY(getTdtMDToDateValue()));
            obj.setPercentage(CommonUtil.convertObjToDouble(getTxtMDBasedAmtValue()));
            obj.setMaximum_amt(CommonUtil.convertObjToDouble(getTxtMDMaximumAmtValue()));
            obj.setMdFixedOrPercentage(CommonUtil.convertObjToStr(getMdFixedOrPercentage()));
            obj.setTxtMdFixedAmtValue(CommonUtil.convertObjToDouble(getTxtMdFixedAmtValue()));
            obj.setTxtFromAmount(CommonUtil.convertObjToDouble(getTxtFromAmount()));
            obj.setTxtToAmount(CommonUtil.convertObjToDouble(getTxtToAmount()));
            obj.setRdoUsingBasic(CommonUtil.convertObjToStr(getRdoUsingBasic()));
            obj.setEligible_percentage(CommonUtil.convertObjToDouble(getTxtMDEligiblePercentageValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new MisecllaniousDeductionTO();
            if(rowNo == 0){
                obj.setMd_sl_no(new Double(1));
            }else{
                int no = rowNo + 1;
                obj.setMd_sl_no(new Double(no));
            }
            obj.setMd_grade(getCboMisecllaniousDeduction());
            obj.setMd_deduction_type(getCboMDCityType());
            obj.setEligible_allowance(getCboMDEligibleAllowances());
            obj.setMd_from_date(DateUtil.getDateMMDDYYYY(getTdtMDFromDateValue()));
            obj.setMd_to_date(DateUtil.getDateMMDDYYYY(getTdtMDToDateValue()));
            obj.setPercentage(CommonUtil.convertObjToDouble(getTxtMDBasedAmtValue()));
            obj.setMaximum_amt(CommonUtil.convertObjToDouble(getTxtMDMaximumAmtValue()));
            obj.setTxtMdFixedAmtValue(CommonUtil.convertObjToDouble(getTxtMdFixedAmtValue()));
            obj.setTxtFromAmount(CommonUtil.convertObjToDouble(getTxtFromAmount()));
            obj.setTxtToAmount(CommonUtil.convertObjToDouble(getTxtToAmount()));
            obj.setRdoUsingBasic(CommonUtil.convertObjToStr(getRdoUsingBasic()));
            obj.setMdFixedOrPercentage(CommonUtil.convertObjToStr(getMdFixedOrPercentage()));
            obj.setEligible_percentage(CommonUtil.convertObjToDouble(getTxtMDEligiblePercentageValue()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowMD(obj);
            MisecllaniousDeductionTOs.set(rowNo,obj);
            tbmMisecllaniousDeduction.removeRow(rowNo);
            tbmMisecllaniousDeduction.insertRow(rowNo,irRow);
            tbmMisecllaniousDeduction.fireTableDataChanged();
        }
        return obj;
    }
    
    //    private ArrayList setEditedMDRow(MisecllaniousDeductionTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(getCboMDCityType());
    //        row.add(getCboMDEligibleAllowances());
    //        row.add(CommonUtil.convertObjToStr(obj.getMaximum_amt()));
    //        row.add(CommonUtil.convertObjToStr(obj.getCCincrementAmt()));
    //        return row;
    
    //    }
    private ArrayList setRowMD(MisecllaniousDeductionTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getMd_grade()));
        row.add(CommonUtil.convertObjToStr(obj.getMd_from_date()));
        row.add(CommonUtil.convertObjToStr(obj.getEligible_allowance()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtMdFixedAmtValue()));
        row.add(CommonUtil.convertObjToStr(obj.getMaximum_amt()));
        row.add(CommonUtil.convertObjToStr(obj.getEligible_percentage()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    public void deleteMDData(int rowNum) {
        deleteEnable = true;
        MisecllaniousDeductionTO obj = (MisecllaniousDeductionTO)MisecllaniousDeductionTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteMisecllaniousDeductionList == null){
            deleteMisecllaniousDeductionList = new ArrayList();
        }
        deleteMisecllaniousDeductionList.add(obj);
        MisecllaniousDeductionTOs.remove(rowNum);
        tbmMisecllaniousDeduction.removeRow(rowNum);
        tbmMisecllaniousDeduction.fireTableDataChanged();
        obj = null;
    }
    private ArrayList populateAndsetMDRow(MisecllaniousDeductionTO obj){
        ArrayList row= new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getMd_grade()));
        row.add(CommonUtil.convertObjToStr(obj.getMd_from_date()));
        row.add(CommonUtil.convertObjToStr(obj.getMd_deduction_type()));
        row.add(CommonUtil.convertObjToStr(obj.getTxtMdFixedAmtValue()));
        row.add(CommonUtil.convertObjToStr(obj.getMaximum_amt()));
        row.add(CommonUtil.convertObjToStr(obj.getEligible_percentage()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    //Gratuity starts//
    public int insertGratuityData(int rowNo) {
        GratuityTO obj = GratuityTO(rowNo);
        if(rowNo == -1){
            if(GratuityTOs== null){
                GratuityTOs = new ArrayList();
            }
            GratuityTOs.add(obj);
            ArrayList irRow = this.setRowGratuity(obj);
            tbmGratuity.insertRow(tbmGratuity.getRowCount(), irRow);
            tbmGratuity.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    public void populateGratuity(int rowNum) {
        GratuityTO obj = (GratuityTO)GratuityTOs.get(rowNum);
        this.setCboGratuityDesignation(obj.getGratuity_grade());
        this.setTdtGratuityFromDateValue(CommonUtil.convertObjToStr(obj.getGratuity_from_date()));
        this.setTdtGratuityToDateValue(CommonUtil.convertObjToStr(obj.getGratuity_to_date()));
        this.setTxtGratuityUptoValue(CommonUtil.convertObjToStr(obj.getUpto_year()));
        this.setTxtGratuityUptoServiceValue(CommonUtil.convertObjToStr(obj.getUpto_months_pay()));
        this.setTxtGratuityMaximumOfValue(CommonUtil.convertObjToStr(obj.getMaximum_months()));
        this.setTxtGratuityBeyondServiceValue(CommonUtil.convertObjToStr(obj.getBeyond_year()));
        this.setTxtGratuityBeyondValue(CommonUtil.convertObjToStr(obj.getBeyond_months_pay()));
        this.setTxtGratuityMaximumAmtBeyongValue(CommonUtil.convertObjToStr(obj.getMaximum_amount()));
        setGAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private GratuityTO GratuityTO(int rowNo){
        double startingAmt = 0.0;
        GratuityTO obj = null;
        if(rowNo == -1){
            obj = new GratuityTO();
            if(tbmGratuity.getRowCount() == 0){
                obj.setGratuity_sl_no(new Double(1));
            }else if(tbmGratuity.getRowCount() >=1){
                obj.setGratuity_sl_no(new Double(tbmGratuity.getRowCount()+1));
            }
            obj.setGratuity_grade(getCboGratuityDesignation());
            obj.setGratuity_from_date(DateUtil.getDateMMDDYYYY(getTdtGratuityFromDateValue()));
            obj.setGratuity_to_date(DateUtil.getDateMMDDYYYY(getTdtGratuityToDateValue()));
            obj.setUpto_year(CommonUtil.convertObjToDouble(getTxtGratuityUptoValue()));
            obj.setUpto_months_pay(CommonUtil.convertObjToDouble(getTxtGratuityUptoServiceValue()));
            obj.setMaximum_months(CommonUtil.convertObjToDouble(getTxtGratuityMaximumOfValue()));
            obj.setBeyond_year(CommonUtil.convertObjToDouble(getTxtGratuityBeyondServiceValue()));
            obj.setBeyond_months_pay(CommonUtil.convertObjToDouble(getTxtGratuityBeyondValue()));
            obj.setMaximum_amount(CommonUtil.convertObjToDouble(getTxtGratuityMaximumAmtBeyongValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new GratuityTO();
            if(rowNo == 0){
                obj.setGratuity_sl_no(new Double(1));
            }else{
                int no = rowNo + 1;
                obj.setGratuity_sl_no(new Double(no));
            }
            obj.setGratuity_grade(getCboGratuityDesignation());
            obj.setGratuity_from_date(DateUtil.getDateMMDDYYYY(getTdtGratuityFromDateValue()));
            obj.setGratuity_to_date(DateUtil.getDateMMDDYYYY(getTdtGratuityToDateValue()));
            obj.setUpto_year(CommonUtil.convertObjToDouble(getTxtGratuityUptoValue()));
            obj.setUpto_months_pay(CommonUtil.convertObjToDouble(getTxtGratuityUptoServiceValue()));
            obj.setMaximum_months(CommonUtil.convertObjToDouble(getTxtGratuityMaximumOfValue()));
            obj.setBeyond_year(CommonUtil.convertObjToDouble(getTxtGratuityBeyondServiceValue()));
            obj.setBeyond_months_pay(CommonUtil.convertObjToDouble(getTxtGratuityBeyondValue()));
            obj.setMaximum_amount(CommonUtil.convertObjToDouble(getTxtGratuityMaximumAmtBeyongValue()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusDate(curDate);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowGratuity(obj);
            GratuityTOs.set(rowNo,obj);
            tbmGratuity.removeRow(rowNo);
            tbmGratuity.insertRow(rowNo,irRow);
            tbmGratuity.fireTableDataChanged();
        }
        return obj;
    }
    private ArrayList setRowGratuity(GratuityTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getGratuity_grade()));
        row.add(CommonUtil.convertObjToStr(obj.getGratuity_from_date()));
        row.add(CommonUtil.convertObjToStr(obj.getUpto_year()));
        row.add(CommonUtil.convertObjToStr(obj.getBeyond_year()));
        //        row.add(CommonUtil.convertObjToStr(obj.getHRACityType()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    //    private ArrayList setEditedHRARow(GratuityTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(getCboHRAllowanceDesignation()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getHRAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getHRAstartingScaleAmt()));
    //        row.add(CommonUtil.convertObjToStr(obj.getHRAincrementAmt()));
    //        return row;
    //    }
    public void deleteGratuityData(int rowNum) {
        deleteEnable = true;
        GratuityTO obj = (GratuityTO)GratuityTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteGratuityList == null){
            deleteGratuityList = new ArrayList();
        }
        deleteGratuityList.add(obj);
        GratuityTOs.remove(rowNum);
        tbmGratuity.removeRow(rowNum);
        tbmGratuity.fireTableDataChanged();
        obj = null;
    }
    private ArrayList populateAndsetGratuityRow(GratuityTO obj){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(obj.getGratuity_grade()));
        row.add(CommonUtil.convertObjToStr(obj.getGratuity_from_date()));
        row.add(CommonUtil.convertObjToStr(obj.getUpto_year()));
        row.add(CommonUtil.convertObjToStr(obj.getBeyond_year()));
        //        row.add(CommonUtil.convertObjToStr(obj.getHRACityType()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    //=================================TA Starts from here=================================================================
    //    public int insertTAData(int rowNo) {
    //        TAllowanceTO obj = TAllowanceTO(rowNo);
    //        if(rowNo == -1){
    //            TAllowanceTOs.add(obj);
    //            ArrayList irRow = this.setRowTA(obj);
    //            tbmTAllowance.insertRow(tbmTAllowance.getRowCount(), irRow);
    //            tbmTAllowance.fireTableDataChanged();
    //        }
    //        obj = null;
    //        return 0;
    //    }
    //
    //    public void populateTA(int rowNum) {
    //        TAllowanceTO obj = (TAllowanceTO)TAllowanceTOs.get(rowNum);
    //        this.setCboTAllowanceDesgination(obj.getTAgrade());
    //        this.setTdtTAFromDateValue(CommonUtil.convertObjToStr(obj.getTAfromDate()));
    //        this.setTdtTAToDateValue(CommonUtil.convertObjToStr(obj.getTAtoDate()));
    //        if(obj.getTAFixed().equals("Y")){
    //            setChkFixedConveyance(true);
    //        }else{
    //            setChkFixedConveyance(false);
    //        }
    //        if(obj.getTAPetrol().equals("Y")){
    //            setChkPetrolAllowance(true);
    //        }else{
    //            setChkPetrolAllowance(false);
    //        }
    //        setTxtBasicAmtUptoValue(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
    //        setTxtConveyancePerMonthValue(CommonUtil.convertObjToStr(obj.getTAConveyancePerMonth()));
    //        setTxtBasicAmtBeyondValue(CommonUtil.convertObjToStr(obj.getTaBasicAmtBeyond()));
    //        setTxtConveyanceAmtValue(CommonUtil.convertObjToStr(obj.getTAConveyanceAmt()));
    //        setTxtNooflitresValue(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
    //        setTxtPricePerlitreValue(CommonUtil.convertObjToStr(obj.getTAPricePerLitre()));
    //        setTxtTotalConveyanceAmtValue(CommonUtil.convertObjToStr(obj.getTATotalConveyanceAmt()));
    //        setTAAuthorizeStatus(obj.getAuthorizeStatus());
    //    }
    //
    //    private TAllowanceTO TAllowanceTO(int rowNo){
    //        double startingAmt = 0.0;
    //        TAllowanceTO obj = null;
    //        if(rowNo == -1){
    //            obj = new TAllowanceTO();
    //            if(tbmTAllowance.getRowCount() == 0){
    //                obj.setTAslNo(String.valueOf(1));
    //            }else if(tbmTAllowance.getRowCount() >=1){
    //                obj.setTAslNo(String.valueOf(tbmTAllowance.getRowCount()+1));
    //            }
    //            obj.setTAgrade(getCboTAllowanceDesgination());
    //            obj.setTAfromDate(DateUtil.getDateMMDDYYYY(getTdtTAFromDateValue()));
    //            obj.setTAtoDate(DateUtil.getDateMMDDYYYY(getTdtTAToDateValue()));
    //            if(getChkFixedConveyance() == true){
    //                obj.setTAFixed("Y");
    //            }else{
    //                obj.setTAFixed("N");
    //            }
    //            if(getChkPetrolAllowance() == true){
    //                obj.setTAPetrol("Y");
    //            }else{
    //                obj.setTAPetrol("N");
    //            }
    //            obj.setTaBasicAmtUpto(CommonUtil.convertObjToStr(getTxtBasicAmtUptoValue()));
    //            obj.setTAConveyancePerMonth(CommonUtil.convertObjToStr(getTxtConveyancePerMonthValue()));
    //            obj.setTaBasicAmtBeyond(CommonUtil.convertObjToStr(getTxtBasicAmtBeyondValue()));
    //            obj.setTAConveyanceAmt(CommonUtil.convertObjToStr(getTxtConveyanceAmtValue()));
    //            obj.setTANoOflitresPerMonth(CommonUtil.convertObjToStr(getTxtNooflitresValue()));
    //            obj.setTAPricePerLitre(CommonUtil.convertObjToStr(getTxtPricePerlitreValue()));
    //            obj.setTATotalConveyanceAmt(CommonUtil.convertObjToStr(getTxtTotalConveyanceAmtValue()));
    //            obj.setStatus(CommonConstants.STATUS_CREATED);
    //            obj.setStatusBy(TrueTransactMain.USER_ID);
    //            obj.setStatusDate(curDate);
    //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //        }else{
    //            obj = new TAllowanceTO();
    //            if(rowNo == 0){
    //                obj.setTAslNo(String.valueOf(1));
    //            }else{
    //                int no = rowNo + 1;
    //                obj.setTAslNo(String.valueOf(no));
    //            }
    //            obj.setTAgrade(getCboTAllowanceDesgination());
    //            obj.setTAfromDate(DateUtil.getDateMMDDYYYY(getTdtTAFromDateValue()));
    //            obj.setTAtoDate(DateUtil.getDateMMDDYYYY(getTdtTAToDateValue()));
    //            if(getChkFixedConveyance() == true){
    //                obj.setTAFixed("Y");
    //            }else{
    //                obj.setTAFixed("N");
    //            }
    //            if(getChkPetrolAllowance() == true){
    //                obj.setTAPetrol("Y");
    //            }else{
    //                obj.setTAPetrol("N");
    //            }
    //            obj.setTaBasicAmtUpto(CommonUtil.convertObjToStr(getTxtBasicAmtUptoValue()));
    //            obj.setTAConveyancePerMonth(CommonUtil.convertObjToStr(getTxtConveyancePerMonthValue()));
    //            obj.setTaBasicAmtBeyond(CommonUtil.convertObjToStr(getTxtBasicAmtBeyondValue()));
    //            obj.setTAConveyanceAmt(CommonUtil.convertObjToStr(getTxtConveyanceAmtValue()));
    //            obj.setTANoOflitresPerMonth(CommonUtil.convertObjToStr(getTxtNooflitresValue()));
    //            obj.setTAPricePerLitre(CommonUtil.convertObjToStr(getTxtPricePerlitreValue()));
    //            obj.setTATotalConveyanceAmt(CommonUtil.convertObjToStr(getTxtTotalConveyanceAmtValue()));
    //            obj.setStatus(CommonConstants.STATUS_MODIFIED);
    //            obj.setStatusDate(curDate);
    //            obj.setStatusBy(TrueTransactMain.USER_ID);
    //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //            ArrayList irRow = setRowTA(obj);
    //            TAllowanceTOs.set(rowNo,obj);
    //            tbmTAllowance.removeRow(rowNo);
    //            tbmTAllowance.insertRow(rowNo,irRow);
    //            tbmTAllowance.fireTableDataChanged();
    //        }
    //        return obj;
    //    }
    //
    ////    private ArrayList setEditedTARow(TAllowanceTO obj){
    ////        ArrayList row = new ArrayList();
    ////        row.add(CommonUtil.convertObjToStr(getCboTAllowanceDesgination()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getTAConveyanceAmt()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
    ////        return row;
    ////    }
    //
    //    private ArrayList setRowTA(TAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getTAgrade()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    //        return row;
    //    }
    //
    //    public void deleteTAData(int rowNum) {
    //        deleteEnable = true;
    //        TAllowanceTO obj = (TAllowanceTO)TAllowanceTOs.get(rowNum);
    //        obj.setStatus(CommonConstants.STATUS_DELETED);
    //        deleteTAllowanceList.add(obj);
    //        TAllowanceTOs.remove(rowNum);
    //        tbmTAllowance.removeRow(rowNum);
    //        tbmTAllowance.fireTableDataChanged();
    //        obj = null;
    //    }
    //    private ArrayList populateAndsetTARow(TAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getTAgrade()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTaBasicAmtUpto()));
    //        row.add(CommonUtil.convertObjToStr(obj.getTANoOflitresPerMonth()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    ////        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
    ////        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
    ////            row.add("No");
    ////        }
    //        return row;
    //    }
    
    //=================================================================MA starts=====================================================
    //    public int insertMAData(int rowNo) {
    //        MAllowanceTO obj = MAllowanceTO(rowNo);
    //        if(rowNo == -1){
    //            MAllowanceTOs.add(obj);
    //            ArrayList irRow = this.setRowMA(obj);
    //            tbmMAllowance.insertRow(tbmMAllowance.getRowCount(), irRow);
    //            tbmMAllowance.fireTableDataChanged();
    //        }
    //        obj = null;
    //        return 0;
    //    }
    //
    //    public void populateMA(int rowNum) {
    //        MAllowanceTO obj = (MAllowanceTO)MAllowanceTOs.get(rowNum);
    //        this.setCboMAidDesg(obj.getMAgrade());
    //        this.setTdtMAidFromDateValue(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    //        this.setTdtMAidToDateValue(CommonUtil.convertObjToStr(obj.getMAtoDate()));
    //        this.setTxtMAidAmtValue(obj.getMAAmount());
    //        setMAAuthorizeStatus(obj.getAuthorizeStatus());
    //    }
    //
    //    private MAllowanceTO MAllowanceTO(int rowNo){
    //        double startingAmt = 0.0;
    //        MAllowanceTO obj = null;
    //        if(rowNo == -1){
    //            obj = new MAllowanceTO();
    //            if(tbmMAllowance.getRowCount() == 0){
    //                obj.setMAslNo(String.valueOf(1));
    //            }else if(tbmMAllowance.getRowCount() >=1){
    //                obj.setMAslNo(String.valueOf(tbmMAllowance.getRowCount()+1));
    //            }
    //            obj.setMAgrade(getCboMAidDesg());
    //            obj.setMAfromDate(DateUtil.getDateMMDDYYYY(getTdtMAidFromDateValue()));
    //            obj.setMAtoDate(DateUtil.getDateMMDDYYYY(getTdtMAidToDateValue()));
    //            obj.setMAAmount(getTxtMAidAmtValue());
    //            obj.setStatus(CommonConstants.STATUS_CREATED);
    //            obj.setStatusBy(TrueTransactMain.USER_ID);
    //            obj.setStatusDate(curDate);
    //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //        }else{
    //            obj = new MAllowanceTO();
    //            if(rowNo == 0){
    //                obj.setMAslNo(String.valueOf(1));
    //            }else{
    //                int no = rowNo + 1;
    //                obj.setMAslNo(String.valueOf(no));
    //            }
    //            obj.setMAgrade(getCboMAidDesg());
    //            obj.setMAfromDate(DateUtil.getDateMMDDYYYY(getTdtMAidFromDateValue()));
    //            obj.setMAtoDate(DateUtil.getDateMMDDYYYY(getTdtMAidToDateValue()));
    //            obj.setMAAmount(getTxtMAidAmtValue());
    //            obj.setStatus(CommonConstants.STATUS_MODIFIED);
    //            obj.setStatusDate(curDate);
    //            obj.setStatusBy(TrueTransactMain.USER_ID);
    //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
    //            ArrayList irRow = setRowMA(obj);
    //            MAllowanceTOs.set(rowNo,obj);
    //            tbmMAllowance.removeRow(rowNo);
    //            tbmMAllowance.insertRow(rowNo,irRow);
    //            tbmMAllowance.fireTableDataChanged();
    //        }
    //        return obj;
    //    }
    //
    //    private ArrayList setRowMA(MAllowanceTO obj){
    //        ArrayList row = new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
    //    	row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    //        return row;
    //    }
    //
    ////    private ArrayList setEditedMARow(MAllowanceTO obj){
    ////        ArrayList row = new ArrayList();
    ////        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
    ////    	row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    ////        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
    ////        return row;
    ////    }
    //
    //    public void deleteMAData(int rowNum) {
    //        deleteEnable = true;
    //        MAllowanceTO obj = (MAllowanceTO)MAllowanceTOs.get(rowNum);
    //        obj.setStatus(CommonConstants.STATUS_DELETED);
    //        deleteMAllowanceList.add(obj);
    //        MAllowanceTOs.remove(rowNum);
    //        tbmMAllowance.removeRow(rowNum);
    //        tbmMAllowance.fireTableDataChanged();
    //        obj = null;
    //    }
    //
    //    private ArrayList populateAndsetMARow(MAllowanceTO obj){
    //        ArrayList row= new ArrayList();
    //        row.add(CommonUtil.convertObjToStr(obj.getMAgrade()));
    //        row.add(CommonUtil.convertObjToStr(obj.getMAfromDate()));
    //        row.add(CommonUtil.convertObjToStr(obj.getMAAmount()));
    //        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
    ////        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
    ////        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
    ////            row.add("No");
    ////        }
    //        return row;
    //    }
    //=================================================================OA Starts===================================================
    
    public int insertHAData(int rowNo) {
        HaltingAllowanceTO obj = HaltingAllowanceTO(rowNo);
        System.out.println("@#$@#$#@$obj:"+obj);
        if(rowNo == -1){
            if(HaltingTOs == null){
                HaltingTOs = new ArrayList();
            }
            HaltingTOs.add(obj);
            System.out.println("@#$@#$#@HaltingTOs:"+HaltingTOs);
            ArrayList irRow = this.setRowHA(obj);
            tbmHalting.insertRow(tbmHalting.getRowCount(), irRow);
            tbmHalting.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    public void populateHA(int rowNum) {
        HaltingAllowanceTO obj = (HaltingAllowanceTO)HaltingTOs.get(rowNum);
        setCboHaltingDesignationValue(obj.getHalting_grade());
        setCboHaltingAllowanceTypeValue(obj.getHalting_allowance_type());
        setCboHaltingParameterBasedOnValue(obj.getHalting_parameter_based());
        setCboHaltingSubParameterValue(obj.getHalting_sub_parameter());
        if(obj.getHalting_fixed_type()!=null && obj.getHalting_fixed_type().equals("Y")){
            setChkFixedType(true);
            setChkPercentageType(false);
        }
        if(obj.getHalting_percentage_type()!=null && obj.getHalting_percentage_type().equals("Y")){
            setChkPercentageType(true);
            setChkFixedType(false);
        }
        setTdtHaltingFromDateValue(CommonUtil.convertObjToStr(obj.getHalting_from_date()));
        setTdtHaltingToDateValue(CommonUtil.convertObjToStr(obj.getHalting_to_date()));
        setTxtHaltingFixedAmtValue(CommonUtil.convertObjToStr(obj.getHalting_fixed_amt()));
        setTxtHaltingMaximumOfValue(CommonUtil.convertObjToStr(obj.getHalting_maximum_amt()));
        setTxtHaltingPercentageValue(CommonUtil.convertObjToStr(obj.getPercentageValue()));
        setHAAuthorizeStatus(obj.getAuthorizeStatus());
    }
    
    private HaltingAllowanceTO HaltingAllowanceTO(int rowNo){
        double startingAmt = 0.0;
        HaltingAllowanceTO obj = null;
        if(rowNo == -1){
            obj = new HaltingAllowanceTO();
            if(tbmHalting.getRowCount() == 0){
                obj.setHalting_sl_no(new Double(1));
            }else if(tbmHalting.getRowCount() >=1){
                obj.setHalting_sl_no(new Double(tbmHalting.getRowCount() + 1));
            }
            obj.setHalting_grade(getCboHaltingDesignationValue());
            if(getChkFixedType() == true){
                obj.setHalting_fixed_type("Y");
            }
            if(getChkPercentageType() == true){
                obj.setHalting_percentage_type("Y");
            }
            obj.setHalting_allowance_type(CommonUtil.convertObjToStr(getCboHaltingAllowanceTypeValue()));
            obj.setHalting_parameter_based(CommonUtil.convertObjToStr(getCboHaltingParameterBasedOnValue()));
            obj.setHalting_sub_parameter(CommonUtil.convertObjToStr(getCboHaltingSubParameterValue()));
            obj.setHalting_from_date(DateUtil.getDateMMDDYYYY(getTdtHaltingFromDateValue()));
            obj.setHalting_to_date(DateUtil.getDateMMDDYYYY(getTdtHaltingToDateValue()));
            obj.setHalting_fixed_amt(CommonUtil.convertObjToDouble(getTxtHaltingFixedAmtValue()));
            obj.setHalting_maximum_amt(CommonUtil.convertObjToDouble(getTxtHaltingMaximumOfValue()));
            obj.setPercentageValue(CommonUtil.convertObjToDouble(getTxtHaltingPercentageValue()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            int i = rowNo;
            obj = new HaltingAllowanceTO();
            if(rowNo == 0){
                obj.setHalting_sl_no(new Double(1));
            }else{
                int no = rowNo + 1;
                obj.setHalting_sl_no(new Double(no));
            }
            //                obj.setHalting_sl_no(new Double(rowNo));
            //                obj.setHalting_sl_no(new Double(i+1));
            obj.setHalting_grade(getCboHaltingDesignationValue());
            if(getChkFixedType() == true){
                obj.setHalting_fixed_type("Y");
            }
            if(getChkPercentageType() == true){
                obj.setHalting_percentage_type("Y");
            }
            obj.setHalting_allowance_type(CommonUtil.convertObjToStr(getCboHaltingAllowanceTypeValue()));
            obj.setHalting_parameter_based(CommonUtil.convertObjToStr(getCboHaltingParameterBasedOnValue()));
            obj.setHalting_sub_parameter(CommonUtil.convertObjToStr(getCboHaltingSubParameterValue()));
            obj.setHalting_from_date(DateUtil.getDateMMDDYYYY(getTdtHaltingFromDateValue()));
            obj.setHalting_to_date(DateUtil.getDateMMDDYYYY(getTdtHaltingToDateValue()));
            obj.setHalting_fixed_amt(CommonUtil.convertObjToDouble(getTxtHaltingFixedAmtValue()));
            obj.setHalting_maximum_amt(CommonUtil.convertObjToDouble(getTxtHaltingMaximumOfValue()));
            obj.setPercentageValue(CommonUtil.convertObjToDouble(getTxtHaltingPercentageValue()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowHA(obj);
            HaltingTOs.set(rowNo,obj);
            tbmHalting.removeRow(rowNo);
            tbmHalting.insertRow(rowNo,irRow);
            tbmHalting.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowHA(HaltingAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getHalting_grade());
        row.add(obj.getHalting_parameter_based());
        row.add(obj.getHalting_sub_parameter());
        row.add(obj.getHalting_fixed_amt());
        row.add(obj.getHalting_maximum_amt());
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //            row.add(CommonUtil.convertObjToStr(getCboHaltingDesignationValue()));
        //            row.add(CommonUtil.convertObjToStr(getCboHaltingSubParameterValue()));
        //            row.add(CommonUtil.convertObjToStr(getTxtHaltingFixedAmtValue()));
        //            row.add(CommonUtil.convertObjToStr(getTxtHaltingMaximumOfValue()));
        //            row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    public void deleteHAData(int rowNum) {
        deleteEnable = true;
        HaltingAllowanceTO obj = (HaltingAllowanceTO)HaltingTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        if(deleteHaltingList == null){
            deleteHaltingList = new ArrayList();
        }
        deleteHaltingList.add(obj);
        HaltingTOs.remove(rowNum);
        tbmHalting.removeRow(rowNum);
        tbmHalting.fireTableDataChanged();
        obj = null;
    }
    
    private ArrayList populateAndsetHARow(HaltingAllowanceTO obj){
        ArrayList row= new ArrayList();
        row.add(obj.getHalting_grade());
        row.add(obj.getHalting_sub_parameter());
        row.add(obj.getHalting_fixed_amt());
        row.add(obj.getHalting_maximum_amt());
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        //        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
        //            row.add("No");
        //        }
        return row;
    }
    
    /**
     * Getter for property chkFixedType.
     * @return Value of property chkFixedType.
     */
    public boolean getChkFixedType() {
        return chkFixedType;
    }
    
    /**
     * Setter for property chkFixedType.
     * @param chkFixedType New value of property chkFixedType.
     */
    public void setChkFixedType(boolean chkFixedType) {
        this.chkFixedType = chkFixedType;
    }
    
    /**
     * Getter for property chkPercentageType.
     * @return Value of property chkPercentageType.
     */
    public boolean getChkPercentageType() {
        return chkPercentageType;
    }
    
    /**
     * Setter for property chkPercentageType.
     * @param chkPercentageType New value of property chkPercentageType.
     */
    public void setChkPercentageType(boolean chkPercentageType) {
        this.chkPercentageType = chkPercentageType;
    }
    
    /**
     * Getter for property HAAuthorizeStatus.
     * @return Value of property HAAuthorizeStatus.
     */
    public java.lang.String getHAAuthorizeStatus() {
        return HAAuthorizeStatus;
    }
    
    /**
     * Setter for property HAAuthorizeStatus.
     * @param HAAuthorizeStatus New value of property HAAuthorizeStatus.
     */
    public void setHAAuthorizeStatus(java.lang.String HAAuthorizeStatus) {
        this.HAAuthorizeStatus = HAAuthorizeStatus;
    }
    
    /**
     * Getter for property MDAuthorizeStatus.
     * @return Value of property MDAuthorizeStatus.
     */
    public java.lang.String getMDAuthorizeStatus() {
        return MDAuthorizeStatus;
    }
    
    /**
     * Setter for property MDAuthorizeStatus.
     * @param MDAuthorizeStatus New value of property MDAuthorizeStatus.
     */
    public void setMDAuthorizeStatus(java.lang.String MDAuthorizeStatus) {
        this.MDAuthorizeStatus = MDAuthorizeStatus;
    }
    
    /**
     * Getter for property GAuthorizeStatus.
     * @return Value of property GAuthorizeStatus.
     */
    public java.lang.String getGAuthorizeStatus() {
        return GAuthorizeStatus;
    }
    
    /**
     * Setter for property GAuthorizeStatus.
     * @param GAuthorizeStatus New value of property GAuthorizeStatus.
     */
    public void setGAuthorizeStatus(java.lang.String GAuthorizeStatus) {
        this.GAuthorizeStatus = GAuthorizeStatus;
    }
    
    /**
     * Getter for property lblHATempSLNoValue.
     * @return Value of property lblHATempSLNoValue.
     */
    public java.lang.Double getLblHATempSLNoValue() {
        return lblHATempSLNoValue;
    }
    
    /**
     * Setter for property lblHATempSLNoValue.
     * @param lblHATempSLNoValue New value of property lblHATempSLNoValue.
     */
    public void setLblHATempSLNoValue(java.lang.Double lblHATempSLNoValue) {
        this.lblHATempSLNoValue = lblHATempSLNoValue;
    }
    
    /**
     * Getter for property lblMDTempSLNoValue.
     * @return Value of property lblMDTempSLNoValue.
     */
    public java.lang.Double getLblMDTempSLNoValue() {
        return lblMDTempSLNoValue;
    }
    
    /**
     * Setter for property lblMDTempSLNoValue.
     * @param lblMDTempSLNoValue New value of property lblMDTempSLNoValue.
     */
    public void setLblMDTempSLNoValue(java.lang.Double lblMDTempSLNoValue) {
        this.lblMDTempSLNoValue = lblMDTempSLNoValue;
    }
    
    /**
     * Getter for property lblGATempSLNoValue.
     * @return Value of property lblGATempSLNoValue.
     */
    public java.lang.Double getLblGATempSLNoValue() {
        return lblGATempSLNoValue;
    }
    
    /**
     * Setter for property lblGATempSLNoValue.
     * @param lblGATempSLNoValue New value of property lblGATempSLNoValue.
     */
    public void setLblGATempSLNoValue(java.lang.Double lblGATempSLNoValue) {
        this.lblGATempSLNoValue = lblGATempSLNoValue;
    }
    
    /**
     * Getter for property txtMdFixedAmtValue.
     * @return Value of property txtMdFixedAmtValue.
     */
    public java.lang.String getTxtMdFixedAmtValue() {
        return txtMdFixedAmtValue;
    }
    
    /**
     * Setter for property txtMdFixedAmtValue.
     * @param txtMdFixedAmtValue New value of property txtMdFixedAmtValue.
     */
    public void setTxtMdFixedAmtValue(java.lang.String txtMdFixedAmtValue) {
        this.txtMdFixedAmtValue = txtMdFixedAmtValue;
    }
    
    /**
     * Getter for property mdFixedOrPercentage.
     * @return Value of property mdFixedOrPercentage.
     */
    public java.lang.String getMdFixedOrPercentage() {
        return mdFixedOrPercentage;
    }
    
    /**
     * Setter for property mdFixedOrPercentage.
     * @param mdFixedOrPercentage New value of property mdFixedOrPercentage.
     */
    public void setMdFixedOrPercentage(java.lang.String mdFixedOrPercentage) {
        this.mdFixedOrPercentage = mdFixedOrPercentage;
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
     * Getter for property rdoUsingBasic.
     * @return Value of property rdoUsingBasic.
     */
    public java.lang.String getRdoUsingBasic() {
        return rdoUsingBasic;
    }
    
    /**
     * Setter for property rdoUsingBasic.
     * @param rdoUsingBasic New value of property rdoUsingBasic.
     */
    public void setRdoUsingBasic(java.lang.String rdoUsingBasic) {
        this.rdoUsingBasic = rdoUsingBasic;
    }
    
}
