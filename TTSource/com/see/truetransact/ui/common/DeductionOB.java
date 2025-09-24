/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DeductionOB.java
 *
 * Created on May 26, 2004, 10:22 AM
 */

package com.see.truetransact.ui.common;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.common.DeductionTO;
import com.see.truetransact.transferobject.common.EarningTO;
import com.see.truetransact.transferobject.common.CCAllowanceTO;
import com.see.truetransact.transferobject.common.DearnessAllowanceTO;
import com.see.truetransact.transferobject.common.GratuityTO;
import com.see.truetransact.transferobject.common.HRAllowanceTO;
import com.see.truetransact.transferobject.common.MAllowanceTO;
import com.see.truetransact.transferobject.common.MisecllaniousDeductionTO;
import com.see.truetransact.transferobject.common.SalaryStructureTO;
import com.see.truetransact.transferobject.common.TAllowanceTO;
import com.see.truetransact.transferobject.common.OtherAllowanceTO;
import com.see.truetransact.transferobject.common.HaltingAllowanceTO;
import com.see.truetransact.transferobject.common.SalaryDeductionTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;

/**
 * @author  Sathiya
 **/

public class DeductionOB extends CObservable{
    
    private static DeductionOB deductionOB;
    private ProxyFactory proxy;
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private int actionType;
    private int result;
    private String lblStatus;
    Date curDate = null;
    private boolean deleteEnable = false;
    
    private ComboBoxModel cbmDeductionTypeValue,cbmParameterBasedOnValue,cbmSubParameterValue,cbmCreditTypeValue,cbmCreditDesigValue,
    cbmBranchwiseValue;
    //    private TableModel tbmDeduction,tbmEarning;
    private EnhancedTableModel tbmDeduction,tbmEarning;
    private ArrayList key,value,DeductionTOs,deleteDeductionList,EarningTOs,deleteEarningList,SalaryListTOs,eachListTos,deleteSalaryList;
    
    private String cboDeductionTypeValue,cboParameterBasedOnValue,cboSubParameterValue,cboCreditTypeValue,cboCreditDesigValue,
    cboBranchwiseValue;
    private boolean rdoDeductionTypeFixed = false;
    private boolean rdoDeductionTypeInstallments = false;
    private String txtEmployeeId = "";
    private String lblEmployeeNameValue = "";
    private String lblDesignationValue = "";
    private String lblEmployeeBranchValue = "";
    private String txtFromDateMMValue = "";
    private String txtFromDateYYYYValue = "";
    private String txtToDateMMValue = "";
    private String txtToDateYYYYValue = "";
    private String txtPremiumAmtValue = "";
    private String txtCreditingACNo = "";
    private String customerId = "";
    private String earningID="";
    private String dtSlNo="";
    private DeductionRB resourceBundle = new DeductionRB();
    private final int ADDRTYPE_COLNO = 0;
    HashMap data = new HashMap();
    private HashMap keyValue = new HashMap();
    private String lblLoanAccNoValue = "";
    private String tdtLoanFromDateValue = "";
    private String tdtLoanToDateValue = "";
    private String txtLoanAmountValue = "";
    private String txtLoanSanctionAmt = "";
    private String txtInstallmentAmtValue = "";
    private String txtInstIntRate = "";
    private String txtIntNetAmount = "";
    private String txtNoofInstallmentsValue = "";
    private String txtLoanAvailedBranchValue = "";
    private String lblLoanDescValue = "";
    private String txtLoanStatusValue = "";
    private String tdtLoanStoppedDateValue = "";
    private String txtRemarksValue = "";
    HashMap param = new HashMap();
    //    private String cboParameterBasedOnValue = "";
    private String txtCreditEmployeeId = "";
    private String lblCreditEmployeeNameValue = "";
    private String lblCreditDesignationValue = "";
    private String lblCreditEmployeeBranchValue = "";
    private String txtCreditBasicPay = "";
    private String lblCreditLastIncDateValue = "";
    private String lblCreditnextIncDateValue = "";
    private String tdtFromDateValue = "";
    private String tdtToDateValue = "";
    private String txtCreditAmtValue = "";
    private String txtNoOfDaysLOP = "";
    private String tempSlNo = "";
    private boolean newEarning = false;
    private boolean newDeduction = false;
    private LinkedHashMap earningMap=new LinkedHashMap();
    private HashMap deletedEarningMap = null;
    
    private LinkedHashMap deductionMap=new LinkedHashMap();
    private HashMap deletedDeductionMap = null;
    private boolean rdoEmployeeWise = false;
    private boolean rdoBranchWise = false;
    private boolean rdoRegionalWise = false;
    private String txtFromEmpIdValue = "";
    private String txtToEmpIdValue = "";
    private String empId = "";
    private String empName = "";
    private String designation = "";
    private String empBranch = "";
    private String basic = "";
    private String da = "";
    private String hra = "";
    private String pf = "";
    private String pt = "";
    private String cca = "";
    private String splAllowance = "";
    private String it = "";
    private String loans = "";
    private String extraLeave = "";
    private String netSalary = "";
    private String salaryStatus = "";
    private String salaryDate = "";
    private String status = "";
    private String statusBy = "";
    private String statusDate = "";
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private String authorizeDate = "";
    private String branchCode = "";
    private String txtSalFromDateMMValue = "";
    private String txtSalFromDateYYYYValue = "";
    private String DTAuthorizeStatus = "";
    private String EDAuthorizeStatus = "";
    private HashMap _authorizeMap;
    int pan = -1;
    int panEditDelete = -1;
    private int DEDUCTION = 1,EARNING = 2;
    
    /** Creates a new instance of MisecllaniousDeductionOB */
    private DeductionOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DeductionJNDI");
        map.put(CommonConstants.HOME, "common.DeductionHome");
        map.put(CommonConstants.REMOTE,"common.Deduction");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
        createEarningsTable();
        createDeductionTable();
        DeductionTOs = new ArrayList();
        deleteDeductionList = new ArrayList();
        EarningTOs = new ArrayList();
        deleteEarningList = new ArrayList();
        SalaryListTOs = new ArrayList();
        eachListTos = new ArrayList();
        deleteSalaryList = new ArrayList();
    }
    
    public static DeductionOB getInstance(){
        try {
            deductionOB = new DeductionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return deductionOB;
    }
    
    private void createEarningsTable() throws Exception{
        final ArrayList earningsColoumn = new ArrayList();
        earningsColoumn.add("Sl No");
        earningsColoumn.add("Allowance Type");
        //        earningsColoumn.add("Parameter");
        earningsColoumn.add("Sub Parameter");
        earningsColoumn.add("Amount");
        earningsColoumn.add("Authorize Status");
        tbmEarning = new EnhancedTableModel(null,earningsColoumn);
    }
    private void createDeductionTable() throws Exception{
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Sl No");
        columnHeader.add("EmployeeId");
        columnHeader.add("EmployeeName");
        columnHeader.add("AllowanceType");
        columnHeader.add("Amount");
        columnHeader.add("LoanNo");
        columnHeader.add("LoanAmount");
        columnHeader.add("Loan Status");
        ArrayList data = new ArrayList();
        tbmDeduction = new TableModel(data,columnHeader);
    }
    private void setTable(){
        
    }
    /** This will show the alertwindow **/
    public  int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("CDialogNo")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    private void fillDropDown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("FIXED_DEDUCTION");
        lookupKey.add("INSTALLMENT_DEDUCTION");
        lookupKey.add("PARAMETER_SAL_STRUCTURE");
        lookupKey.add("SALARY_STRUCTURE");
        lookupKey.add("DEDUCTIONS");
        lookupKey.add("EARNING_ALLOWANCE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        final HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("DEDUCTIONS"));
        cbmDeductionTypeValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("PARAMETER_SAL_STRUCTURE"));
        this.cbmParameterBasedOnValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        cbmCreditDesigValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("EARNING_ALLOWANCE"));
        cbmCreditTypeValue = new ComboBoxModel(key,value);
        cbmSubParameterValue = new ComboBoxModel(key,value);
        List lst = (List)ClientUtil.executeQuery("getAllBranchesList", null);
        System.out.println("########ListForAgent : "+lst);
        getMap(lst);
        setCbmBranchwiseValue(new ComboBoxModel(key,value));
        key =  new ArrayList();
        value = new ArrayList();
        param = null;
//        lookupValues = null;
        key = null;
        value = null;
    }
    
    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        
        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
    }
    
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
         keyValue = proxy.executeQuery(obj,lookupMap);
//        log.info("Got HashMap");
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
    
    public void resetTable(){
        //        this.tbmDeduction.setData(new ArrayList());
        for(int i = tbmEarning.getRowCount(); i > 0; i--){
            tbmEarning.removeRow(0);
        }
        for(int i = tbmDeduction.getRowCount(); i > 0; i--){
            tbmDeduction.removeRow(0);
        }
        //        this.tbmDeduction.fireTableDataChanged();
        this.DeductionTOs.clear();
        //        this.tbmEarning.setData(new ArrayList());
        //        this.tbmEarning.fireTableDataChanged();
    }
    
    public void resetForm(){
        setLblStatus("");
        resetTable();
        resetDeduction();
        resetEarning();
        resetSalary();
        setLblLoanAccNoValue("");
        setTdtLoanFromDateValue("");
        setTdtLoanToDateValue("");
        setTxtLoanAmountValue("");
        setTxtLoanSanctionAmt("");
        setTxtInstallmentAmtValue("");
        setTxtInstIntRate("");
        setTxtIntNetAmount("");
        setTxtNoofInstallmentsValue("");
        setTxtLoanAvailedBranchValue("");
        setCustomerId("");
        setChanged();
        notifyObservers();
        earningMap = new LinkedHashMap();
        deductionMap = new LinkedHashMap();
        EarningTOs = new ArrayList();
        DeductionTOs =  new ArrayList();
    }
    public void populateKeyValues(){
        System.out.println("earningMap@!#!@#!:"+earningMap);
        ArrayList addList =new ArrayList(earningMap.keySet());
        EarningTO objEarningTO = (EarningTO) earningMap.get(addList.get(0));
//        final EarningTO objEarningTO = (EarningTO)earningMap.get(String.valueOf(tbmEarning.getRowCount()));
            if(objEarningTO != null){
                setTxtCreditEmployeeId(objEarningTO.getEmpId());
                setLblCreditEmployeeNameValue(objEarningTO.getEmpName());
                setLblCreditDesignationValue(objEarningTO.getEmpDesignation());
                setLblCreditEmployeeBranchValue(objEarningTO.getEmpBranch());
                setTxtCreditBasicPay(CommonUtil.convertObjToStr(objEarningTO.getPresentBasicPay()));
                setLblCreditLastIncDateValue(CommonUtil.convertObjToStr(objEarningTO.getLastIncDate()));
                setLblCreditnextIncDateValue(CommonUtil.convertObjToStr(objEarningTO.getNextIncDate()));
                setCboCreditDesigValue(CommonUtil.convertObjToStr(objEarningTO.getCboCreditDesigValue()));
            }
            ttNotifyObservers();
    }
    public void populateDeductionKeyValues(){
            System.out.println("deductionMap@!#!@#!:"+deductionMap);
        ArrayList addList =new ArrayList(deductionMap.keySet());
        DeductionTO objDeductionTO = (DeductionTO) deductionMap.get(addList.get(0));
//        final DeductionTO objDeductionTO = (EarningTO)deductionMap.get(String.valueOf(tbmEarning.getRowCount()));
            if(objDeductionTO != null){
                setTxtEmployeeId(objDeductionTO.getEmployeeId());
                setLblEmployeeNameValue(objDeductionTO.getEmployeeName());
                setLblDesignationValue(objDeductionTO.getDesignation());
                setLblEmployeeBranchValue(objDeductionTO.getEmployeeBranch()); 
            }
            ttNotifyObservers();
    }
    public void resetEarning(){
        setTxtCreditEmployeeId("");
        setLblCreditEmployeeNameValue("");
        setLblCreditDesignationValue("");
        setLblCreditEmployeeBranchValue("");
        setTxtCreditBasicPay("");
        setLblCreditLastIncDateValue("");
        setLblCreditnextIncDateValue("");
        setTdtFromDateValue("");
        setTdtToDateValue("");
        setTxtCreditAmtValue("");
        setTxtNoOfDaysLOP("");
        EarningTOs = new ArrayList();
    }
    
    public void resetSalary(){
        setRdoEmployeeWise(false);
        setRdoBranchWise(false);
        setRdoRegionalWise(false);
    }
    
    public void resetDeduction(){
        setDtSlNo("");
        setRdoDeductionTypeFixed(false);
        setRdoDeductionTypeInstallments(false);
        setTxtEmployeeId("");
        setLblEmployeeNameValue("");
        setLblDesignationValue("");
        setLblEmployeeBranchValue("");
        setCboDeductionTypeValue("");
        setTxtFromDateMMValue("");
        setTxtFromDateYYYYValue("");
        setTxtToDateMMValue("");
        setTxtToDateYYYYValue(""); 
        setTxtPremiumAmtValue("");
        setTxtCreditingACNo("");
        setDTAuthorizeStatus("");
    }
    
    private void setTableDataDeduction(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        DeductionTO objDT ;
        int size = DeductionTOs.size();
        for(int i=0;i<size;i++){
            objDT = (DeductionTO)DeductionTOs.get(i);
            objDT.setStatus(CommonConstants.STATUS_MODIFIED);
            setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
            row = setRowDeduction(objDT);
            rows.add(row);
        }
        //        tbmDeduction.setData(rows);
        tbmDeduction.fireTableDataChanged();
    }
    
    public void setRdoButtonLoading(String prodType) {
        if (prodType!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(prodType);
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(prodType));
                this.cbmDeductionTypeValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmDeductionTypeValue = new ComboBoxModel(key,value);
        this.cbmDeductionTypeValue = cbmDeductionTypeValue;
        setChanged();
    }
    
    public void getData(String employeeId,int panEditDelete)throws Exception{
        HashMap whereMap = new HashMap();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameGA = "";
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        whereMap.put("EMPLOYEE_ID",employeeId);
        System.out.println("@#$@#$@#$inside getdata:"+data);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){//||
            //        getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
            //        getActionType()==ClientConstants.ACTIONTYPE_VIEW){
            mapNameDT = "getSelectDeductionEditTO";
            mapNameED = "getSelectEarningEditTO";
            
            if(panEditDelete==EARNING){
                whereMap.put(CommonConstants.MAP_NAME,mapNameED);
                whereMap.put("EARNING","EARNING");
                data = (HashMap)proxy.executeQuery(whereMap,map);
                System.out.println("@#$@#$@#$inside getdata:"+data);
                if(data.containsKey("EARNING")){
                    earningMap = (LinkedHashMap)data.get("EARNING");
                    ArrayList addList =new ArrayList(earningMap.keySet());
                    for(int i=0;i<addList.size();i++){
                        EarningTO objEarningTO = (EarningTO) earningMap.get(addList.get(i));
                        objEarningTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objEarningTO.setStatusBy(TrueTransactMain.USER_ID);
                        objEarningTO.setStatusDt(curDate);
                        earningMap.put(objEarningTO.getEarningID(), objEarningTO);
                    }
                    populateEarningsTable();
                }
            }
            else if(panEditDelete==DEDUCTION){
                whereMap.put(CommonConstants.MAP_NAME,mapNameDT);
                whereMap.put("DEDUCTION","DEDUCTION ");
                data = (HashMap)proxy.executeQuery(whereMap,map);
                System.out.println("@#$@#$@#$inside getdata:"+data);
                if(data.containsKey("DEDUCTION")){
                    deductionMap = (LinkedHashMap)data.get("DEDUCTION");
                    ArrayList addList =new ArrayList(deductionMap.keySet());
                    for(int i=0;i<addList.size();i++){
                        DeductionTO objDeductionTO = (DeductionTO) deductionMap.get(addList.get(i));
                        objDeductionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
                        objDeductionTO.setStatusDate(curDate);
                        deductionMap.put(objDeductionTO.getDtSlNo(), objDeductionTO);
                    }
                    populateDeductionTable();
                }
            }
            whereMap = null;
        }
    }
    public void populateData(String grade){
        HashMap whereMap = new HashMap();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameGA = "";
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        whereMap.put("EMPLOYEE_ID",grade);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            mapNameDT = "getSelectDeductionEditTO";
            mapNameED = "getSelectEarningEditTO";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            if(list!=null && list.size()>0){
                DeductionTOs = (ArrayList)list;
                setTableDataDeduction();
            }
            list = ClientUtil.executeQuery(mapNameED,whereMap);
            if(list!=null && list.size()>0){
                EarningTOs = (ArrayList)list;
                setTableEarningData();
            }
        }else{
            setTableExistingData();
        }
        whereMap = null;
    }
    
    public void doAction(int pan){
        try{
            HashMap objHashMap=null;
            if(_authorizeMap==null) {
                objHashMap = new HashMap();
                if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                }
                //                FOR EARNINGS
                if(earningMap != null && earningMap.size()>0 && pan==EARNING){
                    objHashMap.put("earningMap",earningMap );
                    earningMap = null;
                }
                if(deletedEarningMap != null && deletedEarningMap.size()>0 && pan==EARNING){
                    objHashMap.put("deletedEarningMap",deletedEarningMap );
                    deletedEarningMap = null;
                }
                if(deductionMap != null && deductionMap.size()>0 && pan==DEDUCTION){
                    objHashMap.put("deductionMap",deductionMap );
                    deductionMap = null;
                }
                if(deletedDeductionMap != null && deletedDeductionMap.size()>0 && pan==DEDUCTION){
                    objHashMap.put("deletedDeductionMap",deletedDeductionMap );
                    deletedDeductionMap = null;
                }
                System.out.println("objHashMap :"+objHashMap);
            }
            else if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                if( objHashMap== null){
                    objHashMap = new HashMap();
                }
                objHashMap.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            }
            System.out.println("objHashMap @#@#@#:"+objHashMap);
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
    
//    public int insertDeductionData(int rowNo) {
//        DeductionTO obj = DeductionTO(rowNo);
//        if(rowNo == -1){
//            DeductionTOs.add(obj);
//            ArrayList irRow = this.setRowDeduction(obj);
//            tbmDeduction.insertRow(tbmDeduction.getRowCount(), irRow);
//            tbmDeduction.fireTableDataChanged();
//        }
//        obj = null;
//        return 0;
//    }
    
//    public void populateDeduction(int rowNum) {
//        DeductionTO obj = (DeductionTO)DeductionTOs.get(rowNum);
//        setTxtEmployeeId(obj.getEmployeeId());
//        setLblEmployeeNameValue(obj.getEmployeeName());
//        setLblDesignationValue(obj.getDesignation());
//        setLblEmployeeBranchValue(obj.getEmployeeBranch());
//        if(obj.getFixed()!=null && obj.getFixed().equals("Y")){
//            setRdoDeductionTypeFixed(true);
//            setRdoDeductionTypeInstallments(false);
//            getCbmDeductionTypeValue().setSelectedItem(CommonUtil.convertObjToStr(obj.getDeductionType()));
//            setTxtFromDateMMValue(CommonUtil.convertObjToStr(obj.getFromMM()));
//            setTxtFromDateYYYYValue(CommonUtil.convertObjToStr(obj.getFromYYYY()));
//            setTxtToDateMMValue(CommonUtil.convertObjToStr(obj.getToMM()));
//            setTxtToDateYYYYValue(CommonUtil.convertObjToStr(obj.getToYYYY()));
//            setTxtPremiumAmtValue(CommonUtil.convertObjToStr(obj.getAmount()));
//        }
//        if(obj.getInstallments()!=null && obj.getInstallments().equals("Y")){
//            setRdoDeductionTypeInstallments(true);
//            setRdoDeductionTypeFixed(false);
//            getCbmDeductionTypeValue().setSelectedItem(CommonUtil.convertObjToStr(obj.getDeductionType()));
//            setLblLoanAccNoValue(obj.getLoanAccNo());
//            setTdtLoanFromDateValue(CommonUtil.convertObjToStr(obj.getLoanFromDate()));
//            setTdtLoanToDateValue(CommonUtil.convertObjToStr(obj.getLoanToDate()));
//            setTxtLoanAmountValue(obj.getLoanAmount());
//            setTxtInstallmentAmtValue(obj.getInstallmentAmt());
//            setTxtNoofInstallmentsValue(obj.getNoofInstallments());
//            setTxtLoanAvailedBranchValue(obj.getLoanAvailedBranch());
//            setLblLoanDescValue(obj.getLoanDesc());
//            setTxtLoanStatusValue(obj.getLoanStatus());
//            setTdtLoanStoppedDateValue(CommonUtil.convertObjToStr(obj.getLoanStoppedDate()));
//            setTxtRemarksValue(obj.getRemarks());
//        }
//        setTxtFromDateMMValue(CommonUtil.convertObjToStr(obj.getFromMM()));
//        setTxtFromDateYYYYValue(CommonUtil.convertObjToStr(obj.getFromYYYY()));
//        setTxtToDateMMValue(CommonUtil.convertObjToStr(obj.getToMM()));
//        setTxtToDateYYYYValue(CommonUtil.convertObjToStr(obj.getToYYYY()));
//        setTxtPremiumAmtValue(CommonUtil.convertObjToStr(obj.getAmount()));
//        setTxtCreditingACNo(obj.getAccountHead());
//        setDTAuthorizeStatus(obj.getAuthorizeStatus());
//    }
    
    private DeductionTO DeductionTO(int rowNo){
//        DeductionTO obj = null;
//        if(rowNo == -1){
//            obj = new DeductionTO();
//            if(tbmDeduction.getRowCount() == 0){
//                obj.setDtSlNo(new Double(1));
//            }else if(tbmDeduction.getRowCount() >=1){
//                obj.setDtSlNo(new Double(tbmDeduction.getRowCount() + 1));
//            }
//            obj.setEmployeeId(getTxtEmployeeId());
//            obj.setEmployeeName(getLblEmployeeNameValue());
//            obj.setDesignation(getLblDesignationValue());
//            obj.setEmployeeBranch(getLblEmployeeBranchValue());
//            if(getRdoDeductionTypeFixed() == true){
//                obj.setFixed("Y");
//                obj.setInstallments("N");
//                obj.setFromMM(CommonUtil.convertObjToDouble(getTxtFromDateMMValue()));
//                obj.setFromYYYY(CommonUtil.convertObjToDouble(getTxtFromDateYYYYValue()));
//                obj.setToMM(CommonUtil.convertObjToDouble(getTxtToDateMMValue()));
//                obj.setToYYYY(CommonUtil.convertObjToDouble(getTxtToDateYYYYValue()));
//                obj.setAmount(CommonUtil.convertObjToDouble(getTxtPremiumAmtValue()));
//            }
//            if(getRdoDeductionTypeInstallments() == true){
//                obj.setFixed("N");
//                obj.setInstallments("Y");
//                obj.setLoanAccNo(CommonUtil.convertObjToStr(getLblLoanAccNoValue()));
//                obj.setLoanFromDate(DateUtil.getDateMMDDYYYY(getTdtLoanFromDateValue()));
//                obj.setLoanToDate(DateUtil.getDateMMDDYYYY(getTdtLoanToDateValue()));
//                obj.setLoanAmount(CommonUtil.convertObjToStr(getTxtLoanAmountValue()));
//                obj.setInstallmentAmt(CommonUtil.convertObjToStr(getTxtInstallmentAmtValue()));
//                obj.setNoofInstallments(CommonUtil.convertObjToStr(getTxtNoofInstallmentsValue()));
//                obj.setLoanAvailedBranch(CommonUtil.convertObjToStr(getTxtLoanAvailedBranchValue()));
//                obj.setLoanDesc(CommonUtil.convertObjToStr(getLblLoanDescValue()));
//                obj.setLoanStatus(getTxtLoanStatusValue());
//                obj.setLoanStoppedDate(DateUtil.getDateMMDDYYYY(getTdtLoanStoppedDateValue()));
//                obj.setRemarks(CommonUtil.convertObjToStr(getTxtRemarksValue()));
//            }
//            obj.setAuthorizeStatus(getDTAuthorizeStatus());
//            obj.setDeductionType(CommonUtil.convertObjToStr(getCbmDeductionTypeValue().getSelectedItem()));
//            obj.setAccountHead(getTxtCreditingACNo());
//            obj.setStatus(CommonConstants.STATUS_CREATED);
//            obj.setStatusBy(TrueTransactMain.USER_ID);
//            obj.setStatusDate(curDate);
//            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
//        }else{
//            obj = new DeductionTO();
//            obj.setDtSlNo(new Double(rowNo));
//            obj.setEmployeeId(getTxtEmployeeId());
//            obj.setEmployeeName(getLblEmployeeNameValue());
//            obj.setDesignation(getLblDesignationValue());
//            obj.setEmployeeBranch(getLblEmployeeBranchValue());
//            if(getRdoDeductionTypeFixed() == true){
//                obj.setFixed("Y");
//                obj.setInstallments("N");
//                obj.setFromMM(CommonUtil.convertObjToDouble(getTxtFromDateMMValue()));
//                obj.setFromYYYY(CommonUtil.convertObjToDouble(getTxtFromDateYYYYValue()));
//                obj.setToMM(CommonUtil.convertObjToDouble(getTxtToDateMMValue()));
//                obj.setToYYYY(CommonUtil.convertObjToDouble(getTxtToDateYYYYValue()));
//                obj.setAmount(CommonUtil.convertObjToDouble(getTxtPremiumAmtValue()));
//            }
//            if(getRdoDeductionTypeInstallments() == true){
//                obj.setFixed("N");
//                obj.setInstallments("Y");
//                obj.setLoanAccNo(CommonUtil.convertObjToStr(getLblLoanAccNoValue()));
//                obj.setLoanFromDate(DateUtil.getDateMMDDYYYY(getTdtLoanFromDateValue()));
//                obj.setLoanToDate(DateUtil.getDateMMDDYYYY(getTdtLoanToDateValue()));
//                obj.setLoanAmount(CommonUtil.convertObjToStr(getTxtLoanAmountValue()));
//                obj.setInstallmentAmt(CommonUtil.convertObjToStr(getTxtInstallmentAmtValue()));
//                obj.setNoofInstallments(CommonUtil.convertObjToStr(getTxtNoofInstallmentsValue()));
//                obj.setLoanAvailedBranch(CommonUtil.convertObjToStr(getTxtLoanAvailedBranchValue()));
//                obj.setLoanDesc(CommonUtil.convertObjToStr(getLblLoanDescValue()));
//                obj.setLoanStatus(CommonUtil.convertObjToStr(getTxtLoanStatusValue()));
//                obj.setLoanStoppedDate(DateUtil.getDateMMDDYYYY(getTdtLoanStoppedDateValue()));
//                obj.setRemarks(CommonUtil.convertObjToStr(getTxtRemarksValue()));
//            }
//            obj.setAuthorizeStatus(getDTAuthorizeStatus());
//            obj.setDeductionType(CommonUtil.convertObjToStr(getCbmDeductionTypeValue().getSelectedItem()));
//            obj.setAccountHead(getTxtCreditingACNo());
//            obj.setStatus(CommonConstants.STATUS_CREATED);
//            obj.setStatusBy(TrueTransactMain.USER_ID);
//            obj.setStatusDate(curDate);
//            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
//            ArrayList irRow = setRowDeduction(obj);
//            DeductionTOs.set(rowNo,obj);
//            tbmDeduction.removeRow(rowNo);
//            tbmDeduction.insertRow(rowNo,irRow);
//            tbmDeduction.fireTableDataChanged();
//        }
        return null;
    }
    
    private ArrayList setRowDeduction(DeductionTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getEmployeeId());
        row.add(obj.getEmployeeName());
        row.add(obj.getDeductionType());
        row.add(obj.getAmount());
        if(getRdoDeductionTypeFixed() == true){
            row.add("");
            row.add("");
            row.add("");
            //            row.add("");
        }else{
            //            row.add("");
            //            row.add("");
            //            row.add("");
            //            row.add("");
            row.add(obj.getLoanAccNo());
            row.add(obj.getLoanAmount());
            //            row.add(obj.getLoanFromDate());
            row.add(obj.getLoanStatus());
        }
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
//    public void deleteDeductionData(int rowNum) {
//        deleteEnable = true;
//        DeductionTO obj = (DeductionTO)DeductionTOs.get(rowNum);
//        obj.setStatus(CommonConstants.STATUS_DELETED);
//        deleteDeductionList.add(obj);
//        DeductionTOs.remove(rowNum);
//        tbmDeduction.removeRow(rowNum);
//        tbmDeduction.fireTableDataChanged();
//        obj = null;
//    }
    
    public void populateLoanDetails(){
        if(getCustomerId()!=null && getCustomerId().length()>0){
            HashMap whereMap = new HashMap();
            String mapNameDT = "";
            whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            whereMap.put("CUST_ID",getCustomerId());
            mapNameDT = "getSelectLoanDetails";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            if(list!=null && list.size()>0){
                setTableLoanData(list);
            }
        }
    }
    
    private void setTableLoanData(List list){
//        ArrayList row;
//        ArrayList rows = new ArrayList();
//        HashMap LoanMap = new HashMap();
//        DeductionTO objDT ;
//        int size = list.size();
//        for(int i=0;i<size;i++){
//            LoanMap = (HashMap)list.get(i);
//            setRdoDeductionTypeInstallments(true);
//            setLblLoanAccNoValue(CommonUtil.convertObjToStr(LoanMap.get("ACCT_NUM")));
//            setTdtLoanFromDateValue(CommonUtil.convertObjToStr(LoanMap.get("DISBURSEMENT_DT")));
//            setTdtLoanToDateValue(CommonUtil.convertObjToStr(LoanMap.get("ACCT_OPEN_DT")));
//            setTxtLoanAmountValue(CommonUtil.convertObjToStr(LoanMap.get("LOAN_AMOUNT")));
//            setTxtInstallmentAmtValue(CommonUtil.convertObjToStr(LoanMap.get("")));
//            setTxtNoofInstallmentsValue(CommonUtil.convertObjToStr(LoanMap.get("NO_INSTALLMENTS")));
//            setTxtLoanAvailedBranchValue(CommonUtil.convertObjToStr(LoanMap.get("BRANCH_NAME")));
//            setLblLoanDescValue(CommonUtil.convertObjToStr(LoanMap.get("PROD_DESC")));
//            setTxtLoanStatusValue(CommonUtil.convertObjToStr(LoanMap.get("ACCT_STATUS")));
//            setTdtLoanStoppedDateValue(CommonUtil.convertObjToStr(LoanMap.get("")));
//            setTxtRemarksValue(CommonUtil.convertObjToStr(LoanMap.get("")));
//            setCboDeductionTypeValue("loan");
//            getCbmDeductionTypeValue().setKeyForSelected("loan");
//            insertDeductionData(-1);
//        }
    }
    
    private ArrayList setRowLoan(HashMap LoanMap){
        ArrayList row = new ArrayList();
        row.add(CommonUtil.convertObjToStr(LoanMap.get("")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("ACCT_NUM")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("LOAN_AMOUNT")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("ACCT_OPEN_DT")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("ACCT_STATUS")));
        row.add(CommonUtil.convertObjToStr(LoanMap.get("")));
        return row;
    }
    
    public void fillLoanValues(String LoanNo){
        if(getCustomerId() != null && getCustomerId().length()>0 && LoanNo.length()>0 && !LoanNo.equals("")){
            HashMap whereMap = new HashMap();
            HashMap loanMap = new HashMap();
            String mapNameDT = "";
            String mapNameAmt = "";
            whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            whereMap.put("CUST_ID",getCustomerId());
            whereMap.put("ACCT_NUM", LoanNo);
            mapNameDT = "getSelectLoanDetails";
            mapNameAmt = "getInstallmentAmount";
            List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            if(list!=null && list.size()>0){
                loanMap = (HashMap)list.get(0);
                setLblLoanAccNoValue(CommonUtil.convertObjToStr(loanMap.get("ACCT_NUM")));
                setTdtLoanFromDateValue(CommonUtil.convertObjToStr(loanMap.get("ACCT_OPEN_DT")));
                setTdtLoanToDateValue(CommonUtil.convertObjToStr(loanMap.get("LAST_INSTALL_DT")));
                setTxtLoanAmountValue(CommonUtil.convertObjToStr(loanMap.get("LOAN_AMOUNT")));
                
                setTxtNoofInstallmentsValue(CommonUtil.convertObjToStr(loanMap.get("NO_INSTALLMENTS")));
                setTxtLoanAvailedBranchValue(CommonUtil.convertObjToStr(loanMap.get("BRANCH_NAME")));
                setLblLoanDescValue(CommonUtil.convertObjToStr(loanMap.get("PROD_DESC")));
                setTxtLoanStatusValue(CommonUtil.convertObjToStr(loanMap.get("ACCT_STATUS")));
                setTxtInstIntRate(CommonUtil.convertObjToStr(loanMap.get("INTEREST_RATE")));
                setTxtIntNetAmount(CommonUtil.convertObjToStr(loanMap.get("NET_LOAN_AMOUNT")));
            }
            list = ClientUtil.executeQuery(mapNameAmt,whereMap);
            if(list!=null && list.size()>0){
                loanMap = (HashMap)list.get(0);
                setTxtInstallmentAmtValue(CommonUtil.convertObjToStr(loanMap.get("TOTAL_AMT")));
            }
        }
    }
    
    public void employeeBasicDetails(){
        HashMap employeeMap = new HashMap();
        employeeMap.put("EMPLOYEE_ID", getTxtCreditEmployeeId());
        List lst = ClientUtil.executeQuery("getSelectEmployeeBasic", employeeMap);
        if(lst!=null && lst.size()>0){
            employeeMap = (HashMap)lst.get(0);
            setTxtCreditBasicPay(CommonUtil.convertObjToStr(employeeMap.get("PRESENT_BASIC")));
            setLblCreditLastIncDateValue(CommonUtil.convertObjToStr(employeeMap.get("LAST_INCREMENT_DATE")));
            setLblCreditnextIncDateValue(CommonUtil.convertObjToStr(employeeMap.get("NEXT_INCREMENT_DATE")));
        }
        employeeMap = null;
    }
    
    public void setCbmProd(String prodType) {
        if (cbmParameterBasedOnValue.getKeyForSelected()!=null) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmParameterBasedOnValue.getKeyForSelected());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmParameterBasedOnValue.getKeyForSelected()));
                this.cbmSubParameterValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmSubParameterValue = new ComboBoxModel(key,value);
        this.cbmSubParameterValue= cbmSubParameterValue;
        setChanged();
    }
//    public void setDesignation(HashMap designationMap)throws Exception{
//        List lst = (List)ClientUtil.executeQuery("getDesignationFromGrade", designationMap);
//        if(lst!=null && lst.size()>0){
//            getMap(lst);
//            cbmSubParameterValue = new ComboBoxModel(key,value);
//        }
//    }
    public double setDeductionAmt(String employeeId){
        double fixedAmount = 0.0;
        double percentage = 0.0;
        double maxAmount = 0.0;
        double amount = 0.0;
        HashMap employeeMap = new HashMap();
        employeeMap.put("EMPLOYEE_ID",employeeId);
        employeeMap.put("OASUB_PARAMETER",cbmSubParameterValue.getKeyForSelected());
        employeeMap.put("OAGRADE",cbmCreditDesigValue.getKeyForSelected());
        employeeMap.put("OALLOWANCE_TYPE",cbmCreditTypeValue.getKeyForSelected());
        //        Changed here to check on a new query.
        //        List lst = ClientUtil.executeQuery("getSelectAmount", employeeMap);
        List lst = ClientUtil.executeQuery("getOtherAllowanceAmount", employeeMap);
        if(lst != null && lst.size()>0){
            employeeMap = (HashMap)lst.get(0);
            //            if(!employeeMap.get("OAFIXED_AMT").equals("")){
            fixedAmount = CommonUtil.convertObjToDouble(employeeMap.get("OAFIXED_AMT")).doubleValue();
            if(!String.valueOf(fixedAmount).equals("0.0")){
                amount = fixedAmount;
            }
            else{
                percentage = CommonUtil.convertObjToDouble(employeeMap.get("OAPERCENTAGE_VALUE")).doubleValue();
                maxAmount = CommonUtil.convertObjToDouble(employeeMap.get("OAMAXIMUM_AMT")).doubleValue();
                double basic = CommonUtil.convertObjToDouble(getTxtCreditBasicPay()).doubleValue();
                if(!String.valueOf(maxAmount).equals("0.0")){
                    amount = basic * percentage/100;
                    if(amount > maxAmount){
                        amount = maxAmount;
                    }
                }
                else{
                    amount = basic * percentage/100;
                }
            }
        }
        return amount;
    }
    //---------------------------------------------------------------------------------------//
    public void insertEarningData(int rowNo,boolean earningsFlag) {
        try{
            final EarningTO objEarningTO = new EarningTO();
            if( earningMap == null ){
                earningMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewEarning()){
                    objEarningTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEarningTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEarningTO.setStatusDt(curDate);
                }else{
                    objEarningTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEarningTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEarningTO.setStatusDt(curDate);
                }
            }else{
                objEarningTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                //                objEarningTO.setTxtEmpId(sysId);
            }
            
            
            int slno;
            slno=0;
            
            if(!earningsFlag){
                
                ArrayList data = tbmEarning.getDataArrayList();
                slno=serialNo(data,tbmEarning);
            }
            else if(isNewEarning()){
                int b=CommonUtil.convertObjToInt(tbmEarning.getValueAt(rowNo,0));
                slno= b + tbmEarning.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tbmEarning.getValueAt(rowNo,0));
            }
            objEarningTO.setEarningID(String.valueOf(slno));
            objEarningTO.setEmpId(CommonUtil.convertObjToStr(getTxtCreditEmployeeId()));
            objEarningTO.setEmpName(CommonUtil.convertObjToStr(getLblCreditEmployeeNameValue()));
            objEarningTO.setEmpDesignation(CommonUtil.convertObjToStr(getLblCreditDesignationValue()));
            objEarningTO.setEmpBranch(CommonUtil.convertObjToStr(getLblCreditEmployeeBranchValue()));
            objEarningTO.setPresentBasicPay(CommonUtil.convertObjToDouble(getTxtCreditBasicPay()));
            objEarningTO.setLastIncDate(DateUtil.getDateMMDDYYYY(getLblCreditLastIncDateValue()));
            objEarningTO.setNextIncDate(DateUtil.getDateMMDDYYYY(getLblCreditnextIncDateValue()));
            objEarningTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDateValue()));
            objEarningTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDateValue()));
            objEarningTO.setParameter(CommonUtil.convertObjToStr(getCbmParameterBasedOnValue().getSelectedItem()));
            objEarningTO.setSubParameter(CommonUtil.convertObjToStr(getCbmSubParameterValue().getSelectedItem()));
            objEarningTO.setCboCreditTypeValue(CommonUtil.convertObjToStr(getCbmCreditTypeValue().getSelectedItem()));
            objEarningTO.setCboCreditDesigValue(CommonUtil.convertObjToStr(getCbmCreditDesigValue().getSelectedItem()));
            objEarningTO.setAmount(CommonUtil.convertObjToDouble(getTxtCreditAmtValue()));
            objEarningTO.setTxtNoOfDaysLOP(CommonUtil.convertObjToStr(getTxtNoOfDaysLOP()));
            
            earningMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objEarningTO);
            System.out.println("!@#!@#!@#earningMap"+earningMap);
            updateTblEarningList(rowNo,objEarningTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    public void insertDeductionData(int rowNo,boolean deductionFlag) {
        try{
            final DeductionTO objDeductionTO = new DeductionTO();
            if( deductionMap == null ){
                deductionMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewDeduction()){
                    objDeductionTO.setStatus(CommonConstants.STATUS_CREATED);
                    objDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDeductionTO.setStatusDate(curDate);
                }else{
                    objDeductionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDeductionTO.setStatusDate(curDate);
                }
            }else{
                objDeductionTO.setStatus(CommonConstants.STATUS_CREATED);
                
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                //                objEarningTO.setTxtEmpId(sysId);
            }
            
            
            int slno;
            slno=0;
            
            if(!deductionFlag){
                
                ArrayList data = tbmDeduction.getDataArrayList();
                slno=serialNo(data,tbmDeduction);
            }
            else if(isNewDeduction()){
                int b=CommonUtil.convertObjToInt(tbmDeduction.getValueAt(rowNo,0));
                slno= b + tbmDeduction.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tbmDeduction.getValueAt(rowNo,0));
            }
            objDeductionTO.setDtSlNo(String.valueOf(slno));
            objDeductionTO.setEmployeeId(getTxtEmployeeId());
            objDeductionTO.setEmployeeName(getLblEmployeeNameValue());
            objDeductionTO.setDesignation(getLblDesignationValue());
            objDeductionTO.setEmployeeBranch(getLblEmployeeBranchValue());
            if(getRdoDeductionTypeFixed() == true){
                objDeductionTO.setFixed("Y");
                objDeductionTO.setInstallments("N");
                objDeductionTO.setFromMM(CommonUtil.convertObjToDouble(getTxtFromDateMMValue()));
                objDeductionTO.setFromYYYY(CommonUtil.convertObjToDouble(getTxtFromDateYYYYValue()));
                objDeductionTO.setToMM(CommonUtil.convertObjToDouble(getTxtToDateMMValue()));
                objDeductionTO.setToYYYY(CommonUtil.convertObjToDouble(getTxtToDateYYYYValue()));
                objDeductionTO.setAmount(CommonUtil.convertObjToDouble(getTxtPremiumAmtValue()));
                objDeductionTO.setLoanAccNo("");
                objDeductionTO.setLoanFromDate(DateUtil.getDateMMDDYYYY(""));
                objDeductionTO.setLoanToDate(DateUtil.getDateMMDDYYYY(""));
                objDeductionTO.setLoanAmount("");
                objDeductionTO.setInstallmentAmt("");
                objDeductionTO.setNoofInstallments("");
                objDeductionTO.setLoanAvailedBranch("");
                objDeductionTO.setLoanDesc("");
                objDeductionTO.setLoanStatus("");
                objDeductionTO.setLoanStoppedDate(DateUtil.getDateMMDDYYYY(""));
                objDeductionTO.setTxtInstIntRate("");
                objDeductionTO.setTxtIntNetAmount("");
                objDeductionTO.setRemarks("");  
                
            }
            if(getRdoDeductionTypeInstallments() == true){
                objDeductionTO.setFixed("N");
                objDeductionTO.setInstallments("Y");
                objDeductionTO.setLoanAccNo(CommonUtil.convertObjToStr(getLblLoanAccNoValue()));
                objDeductionTO.setLoanFromDate(DateUtil.getDateMMDDYYYY(getTdtLoanFromDateValue()));
                objDeductionTO.setLoanToDate(DateUtil.getDateMMDDYYYY(getTdtLoanToDateValue()));
                objDeductionTO.setLoanAmount(CommonUtil.convertObjToStr(getTxtLoanAmountValue()));
                objDeductionTO.setInstallmentAmt(CommonUtil.convertObjToStr(getTxtInstallmentAmtValue()));
                objDeductionTO.setNoofInstallments(CommonUtil.convertObjToStr(getTxtNoofInstallmentsValue()));
                objDeductionTO.setLoanAvailedBranch(CommonUtil.convertObjToStr(getTxtLoanAvailedBranchValue()));
                objDeductionTO.setLoanDesc(CommonUtil.convertObjToStr(getLblLoanDescValue()));
                objDeductionTO.setLoanStatus(getTxtLoanStatusValue());
                objDeductionTO.setTxtInstIntRate(getTxtInstIntRate());
                objDeductionTO.setTxtIntNetAmount(getTxtIntNetAmount());
                objDeductionTO.setLoanStoppedDate(DateUtil.getDateMMDDYYYY(getTdtLoanStoppedDateValue()));
                objDeductionTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarksValue()));
                objDeductionTO.setFromMM(CommonUtil.convertObjToDouble(""));
                objDeductionTO.setFromYYYY(CommonUtil.convertObjToDouble(""));
                objDeductionTO.setToMM(CommonUtil.convertObjToDouble(""));
                objDeductionTO.setToYYYY(CommonUtil.convertObjToDouble(""));
                objDeductionTO.setAmount(CommonUtil.convertObjToDouble(""));
            }
            objDeductionTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
            objDeductionTO.setStatusDate(curDate);
            objDeductionTO.setDeductionType(CommonUtil.convertObjToStr(getCbmDeductionTypeValue().getSelectedItem()));
            objDeductionTO.setAccountHead(getTxtCreditingACNo());
            deductionMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objDeductionTO);
            System.out.println("!@#!@#!@#deductionMap"+deductionMap);
            updateTblDeductionList(rowNo,objDeductionTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
        //        setChanged();
    }
    private void updateTblEarningList(int row,EarningTO objEarningTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tbmEarning.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tbmEarning.getDataArrayList().get(j)).get(0);
            if((CommonUtil.convertObjToStr(getEarningID())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(objEarningTO.getEarningID());
            addressRow.add(objEarningTO.getCboCreditTypeValue());
            //            addressRow.add(objEarningTO.getParameter());
            addressRow.add(objEarningTO.getSubParameter());
            addressRow.add(CommonUtil.convertObjToStr(objEarningTO.getAmount()));
            addressRow.add(CommonUtil.convertObjToStr(objEarningTO.getAuthorizeStatus()));
            tbmEarning.insertRow(tbmEarning.getRowCount(),addressRow);
            addressRow = null;
        }
        else {
            tbmEarning.removeRow(row);
            addressRow.add(objEarningTO.getEarningID());
            addressRow.add(objEarningTO.getCboCreditTypeValue());
            //            addressRow.add(objEarningTO.getParameter());
            addressRow.add(objEarningTO.getSubParameter());
            addressRow.add(CommonUtil.convertObjToStr(objEarningTO.getAmount()));
            addressRow.add(CommonUtil.convertObjToStr(objEarningTO.getAuthorizeStatus()));
            tbmEarning.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    private void updateTblDeductionList(int row,DeductionTO objDeductionTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tbmDeduction.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tbmDeduction.getDataArrayList().get(j)).get(0);
            if((CommonUtil.convertObjToStr(getDtSlNo())).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(objDeductionTO.getDtSlNo());
            addressRow.add(objDeductionTO.getEmployeeId());
            addressRow.add(objDeductionTO.getEmployeeName());
            addressRow.add(objDeductionTO.getDeductionType());
            addressRow.add(objDeductionTO.getAmount());
            addressRow.add(objDeductionTO.getLoanAccNo());
            addressRow.add(objDeductionTO.getLoanAmount());
            addressRow.add(objDeductionTO.getLoanStatus());
            tbmDeduction.insertRow(tbmDeduction.getRowCount(),addressRow);
            addressRow = null;
        }
        else {
            tbmDeduction.removeRow(row);
            addressRow.add(objDeductionTO.getDtSlNo());
            addressRow.add(objDeductionTO.getEmployeeId());
            addressRow.add(objDeductionTO.getEmployeeName());
            addressRow.add(objDeductionTO.getDeductionType());
            addressRow.add(objDeductionTO.getAmount());
            addressRow.add(objDeductionTO.getLoanAccNo());
            addressRow.add(objDeductionTO.getLoanAmount());
            addressRow.add(objDeductionTO.getLoanStatus());
            tbmDeduction.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    public int serialNo(ArrayList data, EnhancedTableModel table_name){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(table_name.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    
    private EarningTO EarningTO(int rowNo){
        EarningTO obj = null;
        if(rowNo == -1){
            obj = new EarningTO();
            if(tbmEarning.getRowCount() == 0){
                obj.setEdSlNo(new Double(1));
            }else if(tbmEarning.getRowCount() >=1){
                obj.setEdSlNo(new Double(tbmEarning.getRowCount() + 1));
            }
            obj.setEmpId(getTxtEmployeeId());
            obj.setEmpName(getLblEmployeeNameValue());
            obj.setEmpDesignation(getLblDesignationValue());
            obj.setEmpBranch(getLblEmployeeBranchValue());
            obj.setPresentBasicPay(CommonUtil.convertObjToDouble(getTxtCreditBasicPay()));
            obj.setLastIncDate(DateUtil.getDateMMDDYYYY(getLblCreditLastIncDateValue()));
            obj.setNextIncDate(DateUtil.getDateMMDDYYYY(getLblCreditnextIncDateValue()));
            obj.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDateValue()));
            obj.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDateValue()));
            obj.setParameter(CommonUtil.convertObjToStr(getCbmParameterBasedOnValue().getSelectedItem()));
            obj.setSubParameter(CommonUtil.convertObjToStr(getCbmSubParameterValue().getSelectedItem()));
            obj.setCboCreditTypeValue(CommonUtil.convertObjToStr(getCbmCreditTypeValue().getSelectedItem()));
            obj.setAmount(CommonUtil.convertObjToDouble(getTxtCreditAmtValue()));
            obj.setTxtNoOfDaysLOP(CommonUtil.convertObjToStr(getTxtNoOfDaysLOP()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            //            obj.setStatusDate(curDate);
            //            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
        }else{
            obj = new EarningTO();
            obj.setEdSlNo(new Double(rowNo));
            obj.setEmpId(getTxtEmployeeId());
            obj.setEmpName(getLblEmployeeNameValue());
            obj.setEmpDesignation(getLblDesignationValue());
            obj.setEmpBranch(getLblEmployeeBranchValue());
            obj.setPresentBasicPay(CommonUtil.convertObjToDouble(getTxtCreditBasicPay()));
            obj.setLastIncDate(DateUtil.getDateMMDDYYYY(getLblCreditLastIncDateValue()));
            obj.setNextIncDate(DateUtil.getDateMMDDYYYY(getLblCreditnextIncDateValue()));
            obj.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDateValue()));
            obj.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDateValue()));
            obj.setParameter(CommonUtil.convertObjToStr(getCbmParameterBasedOnValue().getSelectedItem()));
            obj.setSubParameter(CommonUtil.convertObjToStr(getCbmSubParameterValue().getSelectedItem()));
            obj.setCboCreditTypeValue(CommonUtil.convertObjToStr(getCbmCreditTypeValue().getSelectedItem()));
            obj.setAmount(CommonUtil.convertObjToDouble(getTxtCreditAmtValue()));
            obj.setTxtNoOfDaysLOP(CommonUtil.convertObjToStr(getTxtNoOfDaysLOP()));
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            //            obj.setStatusDate(curDate);
            obj.setBranchCode(TrueTransactMain.BRANCH_ID);
            ArrayList irRow = setRowEarning(obj);
            DeductionTOs.set(rowNo,obj);
            tbmEarning.removeRow(rowNo);
            tbmEarning.insertRow(rowNo,irRow);
            tbmEarning.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowEarning(EarningTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getCboCreditTypeValue());
        //        row.add(obj.getToDate());
        row.add(obj.getParameter());
        row.add(obj.getSubParameter());
        row.add(CommonUtil.convertObjToStr(obj.getAmount()));
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    //    public void populateEarning(int rowNum) {
    //        if(EarningTOs!=null && EarningTOs.size()>0){
    //            EarningTO obj = (EarningTO)EarningTOs.get(rowNum);
    //            setTxtCreditEmployeeId(obj.getEmpId());
    //            setLblCreditEmployeeNameValue(obj.getEmpName());
    //            setLblCreditDesignationValue(obj.getEmpDesignation());
    //            setLblCreditEmployeeBranchValue(obj.getEmpBranch());
    //            setTxtCreditBasicPay(CommonUtil.convertObjToStr(obj.getPresentBasicPay()));
    //            setLblCreditLastIncDateValue(CommonUtil.convertObjToStr(obj.getLastIncDate()));
    //            setLblCreditnextIncDateValue(CommonUtil.convertObjToStr(obj.getNextIncDate()));
    //            setTdtFromDateValue(CommonUtil.convertObjToStr(obj.getFromDate()));
    //            setTdtToDateValue(CommonUtil.convertObjToStr(obj.getToDate()));
    //            getCbmParameterBasedOnValue().setSelectedItem(obj.getParameter());
    //            getCbmSubParameterValue().setSelectedItem(obj.getSubParameter());
    //            getCbmCreditTypeValue().setSelectedItem(obj.getCboCreditTypeValue());
    //            setTxtCreditAmtValue(CommonUtil.convertObjToStr(obj.getAmount()));
    //            setDTAuthorizeStatus(obj.getAuthorizeStatus());
    //        }
    //    }
    
    private void setTableExistingData(){
        ArrayList rows = new ArrayList();
        HashMap whereMap = new HashMap();
        whereMap.put("GRADE","SUB-STAFF");
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        //        List list = ClientUtil.executeQuery("getSelectAuthRecSalaryStructureTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList SalaryTOs = (ArrayList)list;
        //            ArrayList rowSS = new ArrayList();
        ////            rows = new ArrayList();
        //            SalaryStructureTO objDT ;
        //            int size = SalaryTOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (SalaryStructureTO)SalaryTOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowSS = setRowAuthSS(objDT);
        //                rows.add(rowSS);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        //        list = ClientUtil.executeQuery("getSelectAuthRecDearnessAllowanceTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList DATOs = (ArrayList)list;
        //            ArrayList rowDA = new ArrayList();
        ////            rows = new ArrayList();
        //            DearnessAllowanceTO objDT ;
        //            int size = DATOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (DearnessAllowanceTO)DATOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowDA = setRowAuthDA(objDT);
        //                rows.add(rowDA);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        //        list = ClientUtil.executeQuery("getSelectAuthRecCAllowanceTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList CCATOs = (ArrayList)list;
        //            ArrayList rowCCA = new ArrayList();
        ////            rows = new ArrayList();
        //            CCAllowanceTO objDT ;
        //            int size = CCATOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (CCAllowanceTO)CCATOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowCCA = setRowAuthCCA(objDT);
        //                rows.add(rowCCA);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        //        list = ClientUtil.executeQuery("getSelectAuthRecHRAllowanceTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList HRATOs = (ArrayList)list;
        //            ArrayList rowHRA = new ArrayList();
        ////            rows = new ArrayList();
        //            HRAllowanceTO objDT ;
        //            int size = HRATOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (HRAllowanceTO)HRATOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowHRA = setRowAuthHRA(objDT);
        //                rows.add(rowHRA);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        //        list = ClientUtil.executeQuery("getSelectAuthRecMAllowanceTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList MATOs = (ArrayList)list;
        //            ArrayList rowMA = new ArrayList();
        ////            rows = new ArrayList();
        //            MAllowanceTO objDT ;
        //            int size = MATOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (MAllowanceTO)MATOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowMA = setRowAuthMA(objDT);
        //                rows.add(rowMA);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        //        list = ClientUtil.executeQuery("getSelectAuthRecTAllowanceTO",whereMap);
        //        if(list!=null && list.size()>0){
        //            ArrayList TATOs = (ArrayList)list;
        //            ArrayList rowTA = new ArrayList();
        ////            rows = new ArrayList();
        //            TAllowanceTO objDT ;
        //            int size = TATOs.size();
        //            for(int i=0;i<size;i++){
        //                objDT = (TAllowanceTO)TATOs.get(i);
        //                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
        //                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
        //                rowTA = setRowAuthTA(objDT);
        //                rows.add(rowTA);
        //            }
        ////            tbmEarning.setData(rows);
        ////            tbmEarning.fireTableDataChanged();
        //        }
        List list = ClientUtil.executeQuery("getSelectAuthRecOAllowanceTO",whereMap);
        if(list!=null && list.size()>0){
            ArrayList OATOs = (ArrayList)list;
            ArrayList rowOA = new ArrayList();
            OtherAllowanceTO objDT ;
            int size = OATOs.size();
            for(int i=0;i<size;i++){
                objDT = (OtherAllowanceTO)OATOs.get(i);
                objDT.setStatus(CommonConstants.STATUS_MODIFIED);
                setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
                rowOA = setRowAuthOA(objDT);
                rows.add(rowOA);
            }
            //            tbmEarning.setData(rows);
            //            tbmEarning.fireTableDataChanged();
        }
        //        tbmEarning.setData(rows);
        tbmEarning.fireTableDataChanged();
    }
    
    private void setTableEarningData(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        EarningTO objDT ;
        int size = EarningTOs.size();
        for(int i=0;i<size;i++){
            objDT = (EarningTO)EarningTOs.get(i);
            objDT.setStatus(CommonConstants.STATUS_MODIFIED);
            setTempSlNo(CommonUtil.convertObjToStr(objDT.getTempSlNo()));
            row = setRowEarning(objDT);
            rows.add(row);
        }
        //        tbmEarning.setData(rows);
        tbmEarning.fireTableDataChanged();
    }
    public void setCbmType() {
        if (cbmCreditDesigValue.getSize()>0) {
            try {
                final ArrayList lookupKey = new ArrayList();
                HashMap param = new HashMap();
                lookupKey.add(cbmCreditDesigValue.getSelectedItem());
                param.put(CommonConstants.MAP_NAME, null);
                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                HashMap lookupValues = ClientUtil.populateLookupData(param);
                fillData((HashMap)lookupValues.get(cbmCreditDesigValue.getSelectedItem()));
                this.cbmCreditTypeValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmCreditTypeValue = new ComboBoxModel(key,value);
        this.cbmCreditTypeValue= cbmCreditTypeValue;
        setChanged();
    }
    
    
    public void setCbmValue() {
        if (cbmCreditTypeValue.getSize()>0) {
            try {
                HashMap existingMap = new HashMap();
                existingMap.put("ALLOWANCE_TYPE",cbmCreditTypeValue.getSelectedItem());
                List lst = ClientUtil.executeQuery("",existingMap);
                if(lst!=null && lst.size()>0){
                    existingMap = (HashMap)lst.get(0);
                }
                //                final ArrayList lookupKey = new ArrayList();
                //                HashMap param = new HashMap();
                //                lookupKey.add(cbmCreditTypeValue.getSelectedItem());
                //                param.put(CommonConstants.MAP_NAME, null);
                //                param.put(CommonConstants.PARAMFORQUERY, lookupKey);
                //                HashMap lookupValues = ClientUtil.populateLookupData(param);
                //                fillData((HashMap)lookupValues.get(cbmCreditDesigValue.getSelectedItem()));
                //                this.cbmCreditTypeValue = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmCreditTypeValue = new ComboBoxModel(key,value);
        this.cbmCreditTypeValue= cbmCreditTypeValue;
        setChanged();
    }
    public void populateEarning(int row){
        try{
            earningTypeChanged(CommonUtil.convertObjToStr(tbmEarning.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void populateDeduction(int row){ 
        try{
            deductionTypeChanged(CommonUtil.convertObjToStr(tbmDeduction.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void deductionTypeChanged(String selectedItem){
        try{
            final DeductionTO objDeductionTO = (DeductionTO)deductionMap.get(selectedItem);
            System.out.println("!@#!#@!@#objDeductionTO"+objDeductionTO);
            populateDeductionData(objDeductionTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void earningTypeChanged(String selectedItem){
        try{
            final EarningTO objEarningTO = (EarningTO)earningMap.get(selectedItem);
            populateEarningData(objEarningTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void populateDeductionData(DeductionTO objDeductionTO)  throws Exception{
        try{
            if(objDeductionTO != null){
                 setDtSlNo(objDeductionTO.getDtSlNo());
        setTxtEmployeeId(objDeductionTO.getEmployeeId());
        setLblEmployeeNameValue(objDeductionTO.getEmployeeName());
        setLblDesignationValue(objDeductionTO.getDesignation());
        setLblEmployeeBranchValue(objDeductionTO.getEmployeeBranch());
        if(objDeductionTO.getFixed()!=null && objDeductionTO.getFixed().equals("Y")){
            setRdoDeductionTypeFixed(true);
            setRdoDeductionTypeInstallments(false);
            getCbmDeductionTypeValue().setSelectedItem(CommonUtil.convertObjToStr(objDeductionTO.getDeductionType()));
            setTxtFromDateMMValue(CommonUtil.convertObjToStr(objDeductionTO.getFromMM()));
            setTxtFromDateYYYYValue(CommonUtil.convertObjToStr(objDeductionTO.getFromYYYY()));
            setTxtToDateMMValue(CommonUtil.convertObjToStr(objDeductionTO.getToMM()));
            setTxtToDateYYYYValue(CommonUtil.convertObjToStr(objDeductionTO.getToYYYY()));
            setTxtPremiumAmtValue(CommonUtil.convertObjToStr(objDeductionTO.getAmount()));
            setTxtCreditingACNo(objDeductionTO.getAccountHead());
        }
        if(objDeductionTO.getInstallments()!=null && objDeductionTO.getInstallments().equals("Y")){
            setRdoDeductionTypeInstallments(true);
            setRdoDeductionTypeFixed(false);
            getCbmDeductionTypeValue().setSelectedItem(CommonUtil.convertObjToStr(objDeductionTO.getDeductionType()));
            setLblLoanAccNoValue(objDeductionTO.getLoanAccNo());
            setTdtLoanFromDateValue(CommonUtil.convertObjToStr(objDeductionTO.getLoanFromDate()));
            setTdtLoanToDateValue(CommonUtil.convertObjToStr(objDeductionTO.getLoanToDate()));
            setTxtLoanAmountValue(objDeductionTO.getLoanAmount());
            setTxtInstallmentAmtValue(objDeductionTO.getInstallmentAmt());
            setTxtNoofInstallmentsValue(objDeductionTO.getNoofInstallments());
            setTxtLoanAvailedBranchValue(objDeductionTO.getLoanAvailedBranch());
            setLblLoanDescValue(objDeductionTO.getLoanDesc());
            setTxtLoanStatusValue(objDeductionTO.getLoanStatus());
            setTxtInstIntRate(objDeductionTO.getTxtInstIntRate());
            setTxtIntNetAmount(objDeductionTO.getTxtIntNetAmount());
            setTdtLoanStoppedDateValue(CommonUtil.convertObjToStr(objDeductionTO.getLoanStoppedDate()));
            setTxtRemarksValue(objDeductionTO.getRemarks());
        }
//        setTxtFromDateMMValue(CommonUtil.convertObjToStr(objDeductionTO.getFromMM()));
//        setTxtFromDateYYYYValue(CommonUtil.convertObjToStr(objDeductionTO.getFromYYYY()));
//        setTxtToDateMMValue(CommonUtil.convertObjToStr(objDeductionTO.getToMM()));
//        setTxtToDateYYYYValue(CommonUtil.convertObjToStr(objDeductionTO.getToYYYY()));
//        setTxtPremiumAmtValue(CommonUtil.convertObjToStr(objDeductionTO.getAmount()));
        setDTAuthorizeStatus(objDeductionTO.getAuthorizeStatus());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void populateEarningData(EarningTO objEarningTO)  throws Exception{
        try{
            if(objEarningTO != null){
                setEarningID(objEarningTO.getEarningID());
                //            EarningTO objEarningTO = (EarningTO)EarningTOs.get(rowNum);
                setTxtCreditEmployeeId(objEarningTO.getEmpId());
                setLblCreditEmployeeNameValue(objEarningTO.getEmpName());
                setLblCreditDesignationValue(objEarningTO.getEmpDesignation());
                setLblCreditEmployeeBranchValue(objEarningTO.getEmpBranch());
                setTxtCreditBasicPay(CommonUtil.convertObjToStr(objEarningTO.getPresentBasicPay()));
                setLblCreditLastIncDateValue(CommonUtil.convertObjToStr(objEarningTO.getLastIncDate()));
                setLblCreditnextIncDateValue(CommonUtil.convertObjToStr(objEarningTO.getNextIncDate()));
                setTdtFromDateValue(CommonUtil.convertObjToStr(objEarningTO.getFromDate()));
                setTdtToDateValue(CommonUtil.convertObjToStr(objEarningTO.getToDate()));
                getCbmParameterBasedOnValue().setSelectedItem(objEarningTO.getParameter());
                getCbmSubParameterValue().setSelectedItem(objEarningTO.getSubParameter());
                getCbmCreditTypeValue().setSelectedItem(objEarningTO.getCboCreditTypeValue());
                setTxtCreditAmtValue(CommonUtil.convertObjToStr(objEarningTO.getAmount()));
                setTxtNoOfDaysLOP(CommonUtil.convertObjToStr(objEarningTO.getTxtNoOfDaysLOP()));
                setDTAuthorizeStatus(objEarningTO.getAuthorizeStatus());
                getCbmCreditDesigValue().setSelectedItem(objEarningTO.getCboCreditDesigValue());
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteEarningData(int rowNum) {
        if(deletedEarningMap == null){
            deletedEarningMap = new HashMap();
        }
        EarningTO objEarningTO = (EarningTO)earningMap.get(CommonUtil.convertObjToStr(tbmEarning.getValueAt(rowNum,ADDRTYPE_COLNO)));
        objEarningTO.setStatus(CommonConstants.STATUS_DELETED);
        objEarningTO.setStatusDt(curDate);
        objEarningTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedEarningMap.put(CommonUtil.convertObjToStr(tbmEarning.getValueAt(rowNum,ADDRTYPE_COLNO)),earningMap.get(CommonUtil.convertObjToStr(tbmEarning.getValueAt(rowNum,ADDRTYPE_COLNO))));
        earningMap.remove(tbmEarning.getValueAt(rowNum,ADDRTYPE_COLNO));
        resetDeleteEarning();
    }
    public void deleteDeductionData(int rowNum) {
        if(deletedDeductionMap == null){
            deletedDeductionMap = new HashMap();
        }
        DeductionTO objDeductionTO = (DeductionTO)deductionMap.get(CommonUtil.convertObjToStr(tbmDeduction.getValueAt(rowNum,ADDRTYPE_COLNO)));
        objDeductionTO.setStatus(CommonConstants.STATUS_DELETED);
        objDeductionTO.setStatusDate(curDate);
        objDeductionTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedDeductionMap.put(CommonUtil.convertObjToStr(tbmDeduction.getValueAt(rowNum,ADDRTYPE_COLNO)),deductionMap.get(CommonUtil.convertObjToStr(tbmDeduction.getValueAt(rowNum,ADDRTYPE_COLNO))));
        deductionMap.remove(tbmDeduction.getValueAt(rowNum,ADDRTYPE_COLNO));
        System.out.println("!@#!@#!@#deletedDeductionMap"+deletedDeductionMap);
        System.out.println("!@#!@#!@#deductionMap"+deductionMap);
        resetDeleteDeduction();
        
    }
    public void resetDeleteDeduction(){
        try{
            
            resetDeduction();
            resetDeductionListTable();
            populateDeductionTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void resetDeleteEarning(){
        try{
            
            resetEarning();
            resetEarningListTable();
            populateEarningsTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateEarningsTable()  throws Exception{
        //added from here
        ArrayList earningsDataLst = new ArrayList();
        earningsDataLst  = new ArrayList(earningMap.keySet());
        ArrayList addList =new ArrayList(earningMap.keySet());
        int length = earningsDataLst .size();
        for(int i=0; i<length; i++){
            ArrayList earningsTabRow = new ArrayList();
            EarningTO objEarningTO = (EarningTO) earningMap.get(addList.get(i));
            earningsTabRow = new ArrayList();
            earningsTabRow.add(CommonUtil.convertObjToStr(objEarningTO.getEarningID()));
            earningsTabRow.add(CommonUtil.convertObjToStr(objEarningTO.getCboCreditTypeValue()));
            earningsTabRow.add(CommonUtil.convertObjToStr(objEarningTO.getSubParameter()));
            earningsTabRow.add(CommonUtil.convertObjToStr(objEarningTO.getAmount()));
            earningsTabRow.add(CommonUtil.convertObjToStr(objEarningTO.getAuthorizeStatus()));
            tbmEarning.insertRow(tbmEarning.getRowCount(),earningsTabRow);
        }
        
    }
    private void populateDeductionTable()  throws Exception{
        //added from here
        ArrayList deductionDataLst = new ArrayList();
        deductionDataLst  = new ArrayList(deductionMap.keySet());
        ArrayList addList =new ArrayList(deductionMap.keySet());
        int length = deductionDataLst .size();
        for(int i=0; i<length; i++){
            ArrayList deductionTabRow = new ArrayList();
            DeductionTO objDeductionTO = (DeductionTO) deductionMap.get(addList.get(i));
            deductionTabRow = new ArrayList();
            deductionTabRow.add(objDeductionTO.getDtSlNo());
            deductionTabRow.add(objDeductionTO.getEmployeeId());
            deductionTabRow.add(objDeductionTO.getEmployeeName());
            deductionTabRow.add(objDeductionTO.getDeductionType());
            deductionTabRow.add(objDeductionTO.getAmount());
            deductionTabRow.add(objDeductionTO.getLoanAccNo());
            deductionTabRow.add(objDeductionTO.getLoanAmount());
            deductionTabRow.add(objDeductionTO.getLoanStatus());
            tbmDeduction.insertRow(tbmDeduction.getRowCount(),deductionTabRow);
        }
        
    }
    public void resetEarningListTable(){
        for(int i = tbmEarning.getRowCount(); i > 0; i--){
            tbmEarning.removeRow(0);
        }
    }
    public void resetDeductionListTable(){
        for(int i = tbmDeduction.getRowCount(); i > 0; i--){
            tbmDeduction.removeRow(0);
        }
    }
    private ArrayList setRowAuthOA(OtherAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getOAgrade());
        row.add("Other Allowance");
        row.add(obj.getOAfromDate());
        row.add(obj.getOAParameterBasedOn());
        row.add(obj.getOASubParameter());
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthMA(MAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getMAgrade());
        row.add("Medical Allowance");
        row.add(obj.getMAfromDate());
        row.add("");
        row.add("");
        row.add(obj.getMAAmount());
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthTA(TAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getTAgrade());
        row.add("Travelling Allowance");
        row.add(obj.getTAfromDate());
        row.add("");
        row.add("");
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthHRA(HRAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getHRAgrade());
        row.add("HR Allowance");
        row.add(obj.getHRAfromDate());
        row.add(obj.getHRACityType());
        row.add("");
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthCCA(CCAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getCCgrade());
        row.add("CC Allowance");
        row.add(obj.getCCfromDate());
        row.add(obj.getCCCityType());
        row.add("");
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthDA(DearnessAllowanceTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getDAgrade());
        row.add("DA Allowance");
        row.add(obj.getDAfromDate());
        row.add("");
        row.add("");
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    private ArrayList setRowAuthSS(SalaryStructureTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getGrade());
        row.add("SS Allowance");
        row.add(obj.getFromDate());
        row.add("");
        row.add("");
        row.add("");
        row.add(CommonUtil.convertObjToStr(obj.getAuthorizeStatus()));
        return row;
    }
    
    //    public void SalaryCalculations(){
    //        HashMap calculationMap = new HashMap();
    //        if(getRdoEmployeeWise() == true){
    //            if(getTxtFromEmpIdValue().length()>0 && getTxtFromEmpIdValue().length()>0){
    //                calculationMap.put("FROM_EMPLOYEE_CODE",getTxtFromEmpIdValue());
    //                calculationMap.put("TO_EMPLOYEE_CODE",getTxtToEmpIdValue());
    //            }else if(getTxtFromEmpIdValue().length() == 0){
    //                ClientUtil.showAlertWindow("From employee id should not be empty");
    //                return;
    //            }else if(getTxtToEmpIdValue().length() == 0){
    //                ClientUtil.showAlertWindow("To employee id should not be empty");
    //                return;
    //            }
    //        }else if(getRdoBranchWise() == true){
    //
    //        }else if (getRdoRegionalWise() == true){
    //
    //        }
    //        SalaryDeductionTO obj = new SalaryDeductionTO();
    //        HashMap listMap = new HashMap();
    //        List lst = ClientUtil.executeQuery("getAllDetailsFromBranches", calculationMap);
    //        if(lst!=null && lst.size()>0){
    //            for (int i = 0;i<lst.size(); i++){
    //                obj = new SalaryDeductionTO();
    //                calculationMap = (HashMap)lst.get(i);
    //                List list = ClientUtil.executeQuery("getSelectAlltableDetails", calculationMap);
    //                if(list!=null && list.size()>0){
    //                    listMap = (HashMap)list.get(0);
    //                    double fixedAmt = 0.0;
    //                    double OAMaxAmt = 0.0;
    //                    double OApercentage = 0.0;
    //                    double basicAmt = CommonUtil.convertObjToDouble(listMap.get("PRESENT_BASIC")).doubleValue();
    //                    String Type = CommonUtil.convertObjToStr(listMap.get("OATYPE"));
    //                    if(Type!=null && Type.equals("FIXED")){
    //                        fixedAmt = CommonUtil.convertObjToDouble(listMap.get("OAFIXED_AMT")).doubleValue();
    //                    }else{
    //                        OApercentage = CommonUtil.convertObjToDouble(listMap.get("OAPERCENTAGE_VALUE")).doubleValue();
    //                        OAMaxAmt = CommonUtil.convertObjToDouble(listMap.get("OAMAXIMUM_AMT")).doubleValue();
    //                    }
    //                    double HRAPercentage = CommonUtil.convertObjToDouble(listMap.get("HRA_PERCENTAGE")).doubleValue();
    //                    double HRAMaxAmt = CommonUtil.convertObjToDouble(listMap.get("HRA_MAX_AMOUNT")).doubleValue();
    //                    double HRATotAmt = basicAmt * HRAPercentage / 100;
    //                    String fixed = CommonUtil.convertObjToStr(listMap.get("FIXED"));
    //                    String petrol = CommonUtil.convertObjToStr(listMap.get("PETROL"));
    //                    if(fixed!=null && fixed.equals("Y")){
    //                        double percentage = CommonUtil.convertObjToDouble(listMap.get("BASIC_AMTUPTO")).doubleValue();
    //                        double convPerMth = CommonUtil.convertObjToDouble(listMap.get("CONV_PERMONTH")).doubleValue();
    //                        double basicAmtUpto = CommonUtil.convertObjToDouble(listMap.get("BASIC_AMT_BEYOND")).doubleValue();
    //                        double conveyanceAmt = CommonUtil.convertObjToDouble(listMap.get("CONVEYANCE_AMT")).doubleValue();
    //                    }else if(petrol!=null && petrol.equals("Y")){
    //                        double noofLitre = CommonUtil.convertObjToDouble(listMap.get("NOOF_LITRES_PERMONTH")).doubleValue();
    //                        double pricePerLitre = CommonUtil.convertObjToDouble(listMap.get("PRICE_PERLITRE")).doubleValue();
    //                        double totalAmt = CommonUtil.convertObjToDouble(listMap.get("TOTAL_CONV_AMT")).doubleValue();
    //                    }
    //                    double CCAPercentage = CommonUtil.convertObjToDouble(listMap.get("CCA_PERCENT")).doubleValue();
    //                    double CCAMaxAmt = CommonUtil.convertObjToDouble(listMap.get("CCA_MAX")).doubleValue();
    //                    double CCATotAmt = basicAmt * CCAPercentage / 100;
    //                    Date currentDt = curDate;
    //                    double DAAmt = 0.0;
    //                    calculationMap.put("FROM_DATE", currentDt);
    //                    list = ClientUtil.executeQuery("getSelectDADetails", calculationMap);
    //                    if(list!=null && list.size()>0){
    //                        listMap = (HashMap)list.get(0);
    //                        double DAPercentage = CommonUtil.convertObjToDouble(listMap.get("DAPERCENTAGE_PER_SLAB")).doubleValue();
    //                        double DATotPer = CommonUtil.convertObjToDouble(listMap.get("DATOTAL_PERCENTAGE")).doubleValue();
    //                        DAAmt = basicAmt * DAPercentage;
    //                    }
    //                    double deductAmt = 0.0;
    //                    list = ClientUtil.executeQuery("getSelectAllDeductionDetails", calculationMap);
    //                    if(list!=null && list.size()>0){
    //                        for(int j = 0;j<list.size();j++){
    //                            listMap = (HashMap)list.get(j);
    //                            deductAmt += CommonUtil.convertObjToDouble(listMap.get("AMOUNT")).doubleValue();
    //                        }
    //                    }
    //                    long lossofPayAmt = 0;
    //                    HashMap lossOfPayMap = new HashMap();
    //                    lossOfPayMap.put("EMP_ID",calculationMap.get("EMPLOYEE_CODE"));
    //                    lossOfPayMap.put("BRANCH_CODE",calculationMap.get("BRANCH_CODE"));
    //                    List Pay = ClientUtil.executeQuery("getSelectLossofPayDetails", lossOfPayMap);
    //                    if(Pay!=null && Pay.size()>0){
    //                        lossOfPayMap = (HashMap)Pay.get(0);
    //                        Date currDt = curDate;
    //                        GregorianCalendar cal = new GregorianCalendar((currDt.getYear()+1900),currDt.getMonth(),currDt.getDate());
    //                        long nofdays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    //                        long day = CommonUtil.convertObjToLong(lossOfPayMap.get("NO_OF_DAYS"));
    //                        lossofPayAmt = (((long)basicAmt / nofdays) * day);
    //                    }
    //                    double totalAmt = ((basicAmt + DAAmt + HRATotAmt + CCATotAmt + fixedAmt)-(deductAmt + lossofPayAmt));
    //                    System.out.println("######### totalAmt"+totalAmt);
    //                    HashMap eachListMap = new HashMap();
    //                    eachListMap.put("EMPLOYEE_CODE",calculationMap.get("EMPLOYEE_CODE"));
    //                    eachListMap.put("CUST_ID",calculationMap.get("CUST_ID"));
    //                    eachListMap.put("DESIG_ID",calculationMap.get("DESIG_ID"));
    //                    eachListMap.put("EMP_BRANCH",calculationMap.get("BRANCH_CODE"));
    //                    eachListMap.put("FNAME",calculationMap.get("FNAME"));
    //                    eachListMap.put("BASIC",new Double(basicAmt));
    //                    eachListMap.put("DA",new Double(DAAmt));
    //                    eachListMap.put("HRA",new Double(HRATotAmt));
    //                    eachListMap.put("CCA",new Double(CCATotAmt));
    //                    eachListMap.put("SPLALLOWANCE",new Double(fixedAmt));
    //                    eachListMap.put("DEDUCT",new Double(deductAmt));
    //                    eachListMap.put("LOSSOFPAY",new Double(lossofPayAmt));
    //                    eachListMap.put("SAL_MONTH",getTxtSalFromDateMMValue());
    //                    eachListMap.put("SAL_YEAR",getTxtSalFromDateYYYYValue());
    //                    SalaryListTOs.add(eachListMap);
    //                }
    //            }
    //            doAction();
    //        }
    //    }
    /**
     * Getter for property cbmDeductionTypeValue.
     * @return Value of property cbmDeductionTypeValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDeductionTypeValue() {
        return cbmDeductionTypeValue;
    }
    
    /**
     * Setter for property cbmDeductionTypeValue.
     * @param cbmDeductionTypeValue New value of property cbmDeductionTypeValue.
     */
    public void setCbmDeductionTypeValue(com.see.truetransact.clientutil.ComboBoxModel cbmDeductionTypeValue) {
        this.cbmDeductionTypeValue = cbmDeductionTypeValue;
    }
    
    /**
     * Getter for property cboDeductionTypeValue.
     * @return Value of property cboDeductionTypeValue.
     */
    public java.lang.String getCboDeductionTypeValue() {
        return cboDeductionTypeValue;
    }
    
    /**
     * Setter for property cboDeductionTypeValue.
     * @param cboDeductionTypeValue New value of property cboDeductionTypeValue.
     */
    public void setCboDeductionTypeValue(java.lang.String cboDeductionTypeValue) {
        this.cboDeductionTypeValue = cboDeductionTypeValue;
    }
    
    /**
     * Getter for property rdoDeductionTypeFixed.
     * @return Value of property rdoDeductionTypeFixed.
     */
    public boolean getRdoDeductionTypeFixed() {
        return rdoDeductionTypeFixed;
    }
    
    /**
     * Setter for property rdoDeductionTypeFixed.
     * @param rdoDeductionTypeFixed New value of property rdoDeductionTypeFixed.
     */
    public void setRdoDeductionTypeFixed(boolean rdoDeductionTypeFixed) {
        this.rdoDeductionTypeFixed = rdoDeductionTypeFixed;
    }
    
    /**
     * Getter for property rdoDeductionTypeInstallments.
     * @return Value of property rdoDeductionTypeInstallments.
     */
    public boolean getRdoDeductionTypeInstallments() {
        return rdoDeductionTypeInstallments;
    }
    
    /**
     * Setter for property rdoDeductionTypeInstallments.
     * @param rdoDeductionTypeInstallments New value of property rdoDeductionTypeInstallments.
     */
    public void setRdoDeductionTypeInstallments(boolean rdoDeductionTypeInstallments) {
        this.rdoDeductionTypeInstallments = rdoDeductionTypeInstallments;
    }
    
    /**
     * Getter for property txtEmployeeId.
     * @return Value of property txtEmployeeId.
     */
    public java.lang.String getTxtEmployeeId() {
        return txtEmployeeId;
    }
    
    /**
     * Setter for property txtEmployeeId.
     * @param txtEmployeeId New value of property txtEmployeeId.
     */
    public void setTxtEmployeeId(java.lang.String txtEmployeeId) {
        this.txtEmployeeId = txtEmployeeId;
    }
    
    /**
     * Getter for property lblEmployeeNameValue.
     * @return Value of property lblEmployeeNameValue.
     */
    public java.lang.String getLblEmployeeNameValue() {
        return lblEmployeeNameValue;
    }
    
    /**
     * Setter for property lblEmployeeNameValue.
     * @param lblEmployeeNameValue New value of property lblEmployeeNameValue.
     */
    public void setLblEmployeeNameValue(java.lang.String lblEmployeeNameValue) {
        this.lblEmployeeNameValue = lblEmployeeNameValue;
    }
    
    /**
     * Getter for property lblDesignationValue.
     * @return Value of property lblDesignationValue.
     */
    public java.lang.String getLblDesignationValue() {
        return lblDesignationValue;
    }
    
    /**
     * Setter for property lblDesignationValue.
     * @param lblDesignationValue New value of property lblDesignationValue.
     */
    public void setLblDesignationValue(java.lang.String lblDesignationValue) {
        this.lblDesignationValue = lblDesignationValue;
    }
    
    /**
     * Getter for property lblEmployeeBranchValue.
     * @return Value of property lblEmployeeBranchValue.
     */
    public java.lang.String getLblEmployeeBranchValue() {
        return lblEmployeeBranchValue;
    }
    
    /**
     * Setter for property lblEmployeeBranchValue.
     * @param lblEmployeeBranchValue New value of property lblEmployeeBranchValue.
     */
    public void setLblEmployeeBranchValue(java.lang.String lblEmployeeBranchValue) {
        this.lblEmployeeBranchValue = lblEmployeeBranchValue;
    }
    
    /**
     * Getter for property txtCreditingACNo.
     * @return Value of property txtCreditingACNo.
     */
    public java.lang.String getTxtCreditingACNo() {
        return txtCreditingACNo;
    }
    
    /**
     * Setter for property txtCreditingACNo.
     * @param txtCreditingACNo New value of property txtCreditingACNo.
     */
    public void setTxtCreditingACNo(java.lang.String txtCreditingACNo) {
        this.txtCreditingACNo = txtCreditingACNo;
    }
    
    /**
     * Getter for property DTAuthorizeStatus.
     * @return Value of property DTAuthorizeStatus.
     */
    public java.lang.String getDTAuthorizeStatus() {
        return DTAuthorizeStatus;
    }
    
    /**
     * Setter for property DTAuthorizeStatus.
     * @param DTAuthorizeStatus New value of property DTAuthorizeStatus.
     */
    public void setDTAuthorizeStatus(java.lang.String DTAuthorizeStatus) {
        this.DTAuthorizeStatus = DTAuthorizeStatus;
    }
    
    /**
     * Getter for property txtFromDateMMValue.
     * @return Value of property txtFromDateMMValue.
     */
    public java.lang.String getTxtFromDateMMValue() {
        return txtFromDateMMValue;
    }
    
    /**
     * Setter for property txtFromDateMMValue.
     * @param txtFromDateMMValue New value of property txtFromDateMMValue.
     */
    public void setTxtFromDateMMValue(java.lang.String txtFromDateMMValue) {
        this.txtFromDateMMValue = txtFromDateMMValue;
    }
    
    /**
     * Getter for property txtFromDateYYYYValue.
     * @return Value of property txtFromDateYYYYValue.
     */
    public java.lang.String getTxtFromDateYYYYValue() {
        return txtFromDateYYYYValue;
    }
    
    /**
     * Setter for property txtFromDateYYYYValue.
     * @param txtFromDateYYYYValue New value of property txtFromDateYYYYValue.
     */
    public void setTxtFromDateYYYYValue(java.lang.String txtFromDateYYYYValue) {
        this.txtFromDateYYYYValue = txtFromDateYYYYValue;
    }
    
    /**
     * Getter for property txtToDateMMValue.
     * @return Value of property txtToDateMMValue.
     */
    public java.lang.String getTxtToDateMMValue() {
        return txtToDateMMValue;
    }
    
    /**
     * Setter for property txtToDateMMValue.
     * @param txtToDateMMValue New value of property txtToDateMMValue.
     */
    public void setTxtToDateMMValue(java.lang.String txtToDateMMValue) {
        this.txtToDateMMValue = txtToDateMMValue;
    }
    
    /**
     * Getter for property txtToDateYYYYValue.
     * @return Value of property txtToDateYYYYValue.
     */
    public java.lang.String getTxtToDateYYYYValue() {
        return txtToDateYYYYValue;
    }
    
    /**
     * Setter for property txtToDateYYYYValue.
     * @param txtToDateYYYYValue New value of property txtToDateYYYYValue.
     */
    public void setTxtToDateYYYYValue(java.lang.String txtToDateYYYYValue) {
        this.txtToDateYYYYValue = txtToDateYYYYValue;
    }
    
    /**
     * Getter for property txtPremiumAmtValue.
     * @return Value of property txtPremiumAmtValue.
     */
    public java.lang.String getTxtPremiumAmtValue() {
        return txtPremiumAmtValue;
    }
    
    /**
     * Setter for property txtPremiumAmtValue.
     * @param txtPremiumAmtValue New value of property txtPremiumAmtValue.
     */
    public void setTxtPremiumAmtValue(java.lang.String txtPremiumAmtValue) {
        this.txtPremiumAmtValue = txtPremiumAmtValue;
    }
    //
    //    /**
    //     * Getter for property tbmLoanDetails.
    //     * @return Value of property tbmLoanDetails.
    //     */
    //    public com.see.truetransact.clientutil.TableModel getTbmLoanDetails() {
    //        return tbmLoanDetails;
    //    }
    //
    //    /**
    //     * Setter for property tbmLoanDetails.
    //     * @param tbmLoanDetails New value of property tbmLoanDetails.
    //     */
    //    public void setTbmLoanDetails(com.see.truetransact.clientutil.TableModel tbmLoanDetails) {
    //        this.tbmLoanDetails = tbmLoanDetails;
    //    }
    
    /**
     * Getter for property customerId.
     * @return Value of property customerId.
     */
    public java.lang.String getCustomerId() {
        return customerId;
    }
    
    /**
     * Setter for property customerId.
     * @param customerId New value of property customerId.
     */
    public void setCustomerId(java.lang.String customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Getter for property tdtLoanFromDateValue.
     * @return Value of property tdtLoanFromDateValue.
     */
    public java.lang.String getTdtLoanFromDateValue() {
        return tdtLoanFromDateValue;
    }
    
    /**
     * Setter for property tdtLoanFromDateValue.
     * @param tdtLoanFromDateValue New value of property tdtLoanFromDateValue.
     */
    public void setTdtLoanFromDateValue(java.lang.String tdtLoanFromDateValue) {
        this.tdtLoanFromDateValue = tdtLoanFromDateValue;
    }
    
    /**
     * Getter for property tdtLoanToDateValue.
     * @return Value of property tdtLoanToDateValue.
     */
    public java.lang.String getTdtLoanToDateValue() {
        return tdtLoanToDateValue;
    }
    
    /**
     * Setter for property tdtLoanToDateValue.
     * @param tdtLoanToDateValue New value of property tdtLoanToDateValue.
     */
    public void setTdtLoanToDateValue(java.lang.String tdtLoanToDateValue) {
        this.tdtLoanToDateValue = tdtLoanToDateValue;
    }
    
    /**
     * Getter for property txtLoanAmountValue.
     * @return Value of property txtLoanAmountValue.
     */
    public java.lang.String getTxtLoanAmountValue() {
        return txtLoanAmountValue;
    }
    
    /**
     * Setter for property txtLoanAmountValue.
     * @param txtLoanAmountValue New value of property txtLoanAmountValue.
     */
    public void setTxtLoanAmountValue(java.lang.String txtLoanAmountValue) {
        this.txtLoanAmountValue = txtLoanAmountValue;
    }
    
    /**
     * Getter for property txtInstallmentAmtValue.
     * @return Value of property txtInstallmentAmtValue.
     */
    public java.lang.String getTxtInstallmentAmtValue() {
        return txtInstallmentAmtValue;
    }
    
    /**
     * Setter for property txtInstallmentAmtValue.
     * @param txtInstallmentAmtValue New value of property txtInstallmentAmtValue.
     */
    public void setTxtInstallmentAmtValue(java.lang.String txtInstallmentAmtValue) {
        this.txtInstallmentAmtValue = txtInstallmentAmtValue;
    }
    
    /**
     * Getter for property txtNoofInstallmentsValue.
     * @return Value of property txtNoofInstallmentsValue.
     */
    public java.lang.String getTxtNoofInstallmentsValue() {
        return txtNoofInstallmentsValue;
    }
    
    /**
     * Setter for property txtNoofInstallmentsValue.
     * @param txtNoofInstallmentsValue New value of property txtNoofInstallmentsValue.
     */
    public void setTxtNoofInstallmentsValue(java.lang.String txtNoofInstallmentsValue) {
        this.txtNoofInstallmentsValue = txtNoofInstallmentsValue;
    }
    
    /**
     * Getter for property txtLoanAvailedBranchValue.
     * @return Value of property txtLoanAvailedBranchValue.
     */
    public java.lang.String getTxtLoanAvailedBranchValue() {
        return txtLoanAvailedBranchValue;
    }
    
    /**
     * Setter for property txtLoanAvailedBranchValue.
     * @param txtLoanAvailedBranchValue New value of property txtLoanAvailedBranchValue.
     */
    public void setTxtLoanAvailedBranchValue(java.lang.String txtLoanAvailedBranchValue) {
        this.txtLoanAvailedBranchValue = txtLoanAvailedBranchValue;
    }
    
    /**
     * Getter for property lblLoanAccNoValue.
     * @return Value of property lblLoanAccNoValue.
     */
    public java.lang.String getLblLoanAccNoValue() {
        return lblLoanAccNoValue;
    }
    
    /**
     * Setter for property lblLoanAccNoValue.
     * @param lblLoanAccNoValue New value of property lblLoanAccNoValue.
     */
    public void setLblLoanAccNoValue(java.lang.String lblLoanAccNoValue) {
        this.lblLoanAccNoValue = lblLoanAccNoValue;
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
     * Getter for property lblLoanDescValue.
     * @return Value of property lblLoanDescValue.
     */
    public java.lang.String getLblLoanDescValue() {
        return lblLoanDescValue;
    }
    
    /**
     * Setter for property lblLoanDescValue.
     * @param lblLoanDescValue New value of property lblLoanDescValue.
     */
    public void setLblLoanDescValue(java.lang.String lblLoanDescValue) {
        this.lblLoanDescValue = lblLoanDescValue;
    }
    
    /**
     * Getter for property txtLoanStatusValue.
     * @return Value of property txtLoanStatusValue.
     */
    public java.lang.String getTxtLoanStatusValue() {
        return txtLoanStatusValue;
    }
    
    /**
     * Setter for property txtLoanStatusValue.
     * @param txtLoanStatusValue New value of property txtLoanStatusValue.
     */
    public void setTxtLoanStatusValue(java.lang.String txtLoanStatusValue) {
        this.txtLoanStatusValue = txtLoanStatusValue;
    }
    
    /**
     * Getter for property tdtLoanStoppedDateValue.
     * @return Value of property tdtLoanStoppedDateValue.
     */
    public java.lang.String getTdtLoanStoppedDateValue() {
        return tdtLoanStoppedDateValue;
    }
    
    /**
     * Setter for property tdtLoanStoppedDateValue.
     * @param tdtLoanStoppedDateValue New value of property tdtLoanStoppedDateValue.
     */
    public void setTdtLoanStoppedDateValue(java.lang.String tdtLoanStoppedDateValue) {
        this.tdtLoanStoppedDateValue = tdtLoanStoppedDateValue;
    }
    
    /**
     * Getter for property txtRemarksValue.
     * @return Value of property txtRemarksValue.
     */
    public java.lang.String getTxtRemarksValue() {
        return txtRemarksValue;
    }
    
    /**
     * Setter for property txtRemarksValue.
     * @param txtRemarksValue New value of property txtRemarksValue.
     */
    public void setTxtRemarksValue(java.lang.String txtRemarksValue) {
        this.txtRemarksValue = txtRemarksValue;
    }
    
    /**
     * Getter for property txtCreditEmployeeId.
     * @return Value of property txtCreditEmployeeId.
     */
    public java.lang.String getTxtCreditEmployeeId() {
        return txtCreditEmployeeId;
    }
    
    /**
     * Setter for property txtCreditEmployeeId.
     * @param txtCreditEmployeeId New value of property txtCreditEmployeeId.
     */
    public void setTxtCreditEmployeeId(java.lang.String txtCreditEmployeeId) {
        this.txtCreditEmployeeId = txtCreditEmployeeId;
    }
    
    /**
     * Getter for property lblCreditEmployeeNameValue.
     * @return Value of property lblCreditEmployeeNameValue.
     */
    public java.lang.String getLblCreditEmployeeNameValue() {
        return lblCreditEmployeeNameValue;
    }
    
    /**
     * Setter for property lblCreditEmployeeNameValue.
     * @param lblCreditEmployeeNameValue New value of property lblCreditEmployeeNameValue.
     */
    public void setLblCreditEmployeeNameValue(java.lang.String lblCreditEmployeeNameValue) {
        this.lblCreditEmployeeNameValue = lblCreditEmployeeNameValue;
    }
    
    /**
     * Getter for property lblCreditDesignationValue.
     * @return Value of property lblCreditDesignationValue.
     */
    public java.lang.String getLblCreditDesignationValue() {
        return lblCreditDesignationValue;
    }
    
    /**
     * Setter for property lblCreditDesignationValue.
     * @param lblCreditDesignationValue New value of property lblCreditDesignationValue.
     */
    public void setLblCreditDesignationValue(java.lang.String lblCreditDesignationValue) {
        this.lblCreditDesignationValue = lblCreditDesignationValue;
    }
    
    /**
     * Getter for property lblCreditEmployeeBranchValue.
     * @return Value of property lblCreditEmployeeBranchValue.
     */
    public java.lang.String getLblCreditEmployeeBranchValue() {
        return lblCreditEmployeeBranchValue;
    }
    
    /**
     * Setter for property lblCreditEmployeeBranchValue.
     * @param lblCreditEmployeeBranchValue New value of property lblCreditEmployeeBranchValue.
     */
    public void setLblCreditEmployeeBranchValue(java.lang.String lblCreditEmployeeBranchValue) {
        this.lblCreditEmployeeBranchValue = lblCreditEmployeeBranchValue;
    }
    
    /**
     * Getter for property txtCreditBasicPay.
     * @return Value of property txtCreditBasicPay.
     */
    public java.lang.String getTxtCreditBasicPay() {
        return txtCreditBasicPay;
    }
    
    /**
     * Setter for property txtCreditBasicPay.
     * @param txtCreditBasicPay New value of property txtCreditBasicPay.
     */
    public void setTxtCreditBasicPay(java.lang.String txtCreditBasicPay) {
        this.txtCreditBasicPay = txtCreditBasicPay;
    }
    
    /**
     * Getter for property lblCreditLastIncDateValue.
     * @return Value of property lblCreditLastIncDateValue.
     */
    public java.lang.String getLblCreditLastIncDateValue() {
        return lblCreditLastIncDateValue;
    }
    
    /**
     * Setter for property lblCreditLastIncDateValue.
     * @param lblCreditLastIncDateValue New value of property lblCreditLastIncDateValue.
     */
    public void setLblCreditLastIncDateValue(java.lang.String lblCreditLastIncDateValue) {
        this.lblCreditLastIncDateValue = lblCreditLastIncDateValue;
    }
    
    /**
     * Getter for property lblCreditnextIncDateValue.
     * @return Value of property lblCreditnextIncDateValue.
     */
    public java.lang.String getLblCreditnextIncDateValue() {
        return lblCreditnextIncDateValue;
    }
    
    /**
     * Setter for property lblCreditnextIncDateValue.
     * @param lblCreditnextIncDateValue New value of property lblCreditnextIncDateValue.
     */
    public void setLblCreditnextIncDateValue(java.lang.String lblCreditnextIncDateValue) {
        this.lblCreditnextIncDateValue = lblCreditnextIncDateValue;
    }
    
    /**
     * Getter for property cbmParameterBasedOnValue.
     * @return Value of property cbmParameterBasedOnValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmParameterBasedOnValue() {
        return cbmParameterBasedOnValue;
    }
    
    /**
     * Setter for property cbmParameterBasedOnValue.
     * @param cbmParameterBasedOnValue New value of property cbmParameterBasedOnValue.
     */
    public void setCbmParameterBasedOnValue(com.see.truetransact.clientutil.ComboBoxModel cbmParameterBasedOnValue) {
        this.cbmParameterBasedOnValue = cbmParameterBasedOnValue;
    }
    
    /**
     * Getter for property cboParameterBasedOnValue.
     * @return Value of property cboParameterBasedOnValue.
     */
    public java.lang.String getCboParameterBasedOnValue() {
        return cboParameterBasedOnValue;
    }
    
    /**
     * Setter for property cboParameterBasedOnValue.
     * @param cboParameterBasedOnValue New value of property cboParameterBasedOnValue.
     */
    public void setCboParameterBasedOnValue(java.lang.String cboParameterBasedOnValue) {
        this.cboParameterBasedOnValue = cboParameterBasedOnValue;
    }
    
    /**
     * Getter for property cbmSubParameterValue.
     * @return Value of property cbmSubParameterValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSubParameterValue() {
        return cbmSubParameterValue;
    }
    
    /**
     * Setter for property cbmSubParameterValue.
     * @param cbmSubParameterValue New value of property cbmSubParameterValue.
     */
    public void setCbmSubParameterValue(com.see.truetransact.clientutil.ComboBoxModel cbmSubParameterValue) {
        this.cbmSubParameterValue = cbmSubParameterValue;
    }
    
    /**
     * Getter for property cboSubParameterValue.
     * @return Value of property cboSubParameterValue.
     */
    public java.lang.String getCboSubParameterValue() {
        return cboSubParameterValue;
    }
    
    /**
     * Setter for property cboSubParameterValue.
     * @param cboSubParameterValue New value of property cboSubParameterValue.
     */
    public void setCboSubParameterValue(java.lang.String cboSubParameterValue) {
        this.cboSubParameterValue = cboSubParameterValue;
    }
    
    /**
     * Getter for property tdtFromDateValue.
     * @return Value of property tdtFromDateValue.
     */
    public java.lang.String getTdtFromDateValue() {
        return tdtFromDateValue;
    }
    
    /**
     * Setter for property tdtFromDateValue.
     * @param tdtFromDateValue New value of property tdtFromDateValue.
     */
    public void setTdtFromDateValue(java.lang.String tdtFromDateValue) {
        this.tdtFromDateValue = tdtFromDateValue;
    }
    
    /**
     * Getter for property tdtToDateValue.
     * @return Value of property tdtToDateValue.
     */
    public java.lang.String getTdtToDateValue() {
        return tdtToDateValue;
    }
    
    /**
     * Setter for property tdtToDateValue.
     * @param tdtToDateValue New value of property tdtToDateValue.
     */
    public void setTdtToDateValue(java.lang.String tdtToDateValue) {
        this.tdtToDateValue = tdtToDateValue;
    }
    
    /**
     * Getter for property txtCreditAmtValue.
     * @return Value of property txtCreditAmtValue.
     */
    public java.lang.String getTxtCreditAmtValue() {
        return txtCreditAmtValue;
    }
    
    /**
     * Setter for property txtCreditAmtValue.
     * @param txtCreditAmtValue New value of property txtCreditAmtValue.
     */
    public void setTxtCreditAmtValue(java.lang.String txtCreditAmtValue) {
        this.txtCreditAmtValue = txtCreditAmtValue;
    }
    
    /**
     * Getter for property EDAuthorizeStatus.
     * @return Value of property EDAuthorizeStatus.
     */
    public java.lang.String getEDAuthorizeStatus() {
        return EDAuthorizeStatus;
    }
    
    /**
     * Setter for property EDAuthorizeStatus.
     * @param EDAuthorizeStatus New value of property EDAuthorizeStatus.
     */
    public void setEDAuthorizeStatus(java.lang.String EDAuthorizeStatus) {
        this.EDAuthorizeStatus = EDAuthorizeStatus;
    }
    
    /**
     * Getter for property cbmCreditTypeValue.
     * @return Value of property cbmCreditTypeValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCreditTypeValue() {
        return cbmCreditTypeValue;
    }
    
    /**
     * Setter for property cbmCreditTypeValue.
     * @param cbmCreditTypeValue New value of property cbmCreditTypeValue.
     */
    public void setCbmCreditTypeValue(com.see.truetransact.clientutil.ComboBoxModel cbmCreditTypeValue) {
        this.cbmCreditTypeValue = cbmCreditTypeValue;
    }
    
    /**
     * Getter for property cboCreditTypeValue.
     * @return Value of property cboCreditTypeValue.
     */
    public java.lang.String getCboCreditTypeValue() {
        return cboCreditTypeValue;
    }
    
    /**
     * Setter for property cboCreditTypeValue.
     * @param cboCreditTypeValue New value of property cboCreditTypeValue.
     */
    public void setCboCreditTypeValue(java.lang.String cboCreditTypeValue) {
        this.cboCreditTypeValue = cboCreditTypeValue;
    }
    
    /**
     * Getter for property cboCreditDesigValue.
     * @return Value of property cboCreditDesigValue.
     */
    public java.lang.String getCboCreditDesigValue() {
        return cboCreditDesigValue;
    }
    
    /**
     * Setter for property cboCreditDesigValue.
     * @param cboCreditDesigValue New value of property cboCreditDesigValue.
     */
    public void setCboCreditDesigValue(java.lang.String cboCreditDesigValue) {
        this.cboCreditDesigValue = cboCreditDesigValue;
    }
    
    /**
     * Getter for property cbmCreditDesigValue.
     * @return Value of property cbmCreditDesigValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCreditDesigValue() {
        return cbmCreditDesigValue;
    }
    
    /**
     * Setter for property cbmCreditDesigValue.
     * @param cbmCreditDesigValue New value of property cbmCreditDesigValue.
     */
    public void setCbmCreditDesigValue(com.see.truetransact.clientutil.ComboBoxModel cbmCreditDesigValue) {
        this.cbmCreditDesigValue = cbmCreditDesigValue;
    }
    
    /**
     * Getter for property cbmBranchwiseValue.
     * @return Value of property cbmBranchwiseValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchwiseValue() {
        return cbmBranchwiseValue;
    }
    
    /**
     * Setter for property cbmBranchwiseValue.
     * @param cbmBranchwiseValue New value of property cbmBranchwiseValue.
     */
    public void setCbmBranchwiseValue(com.see.truetransact.clientutil.ComboBoxModel cbmBranchwiseValue) {
        this.cbmBranchwiseValue = cbmBranchwiseValue;
    }
    
    /**
     * Getter for property cboBranchwiseValue.
     * @return Value of property cboBranchwiseValue.
     */
    public java.lang.String getCboBranchwiseValue() {
        return cboBranchwiseValue;
    }
    
    /**
     * Setter for property cboBranchwiseValue.
     * @param cboBranchwiseValue New value of property cboBranchwiseValue.
     */
    public void setCboBranchwiseValue(java.lang.String cboBranchwiseValue) {
        this.cboBranchwiseValue = cboBranchwiseValue;
    }
    
    /**
     * Getter for property rdoEmployeeWise.
     * @return Value of property rdoEmployeeWise.
     */
    public boolean getRdoEmployeeWise() {
        return rdoEmployeeWise;
    }
    
    /**
     * Setter for property rdoEmployeeWise.
     * @param rdoEmployeeWise New value of property rdoEmployeeWise.
     */
    public void setRdoEmployeeWise(boolean rdoEmployeeWise) {
        this.rdoEmployeeWise = rdoEmployeeWise;
    }
    
    /**
     * Getter for property rdoBranchWise.
     * @return Value of property rdoBranchWise.
     */
    public boolean getRdoBranchWise() {
        return rdoBranchWise;
    }
    
    /**
     * Setter for property rdoBranchWise.
     * @param rdoBranchWise New value of property rdoBranchWise.
     */
    public void setRdoBranchWise(boolean rdoBranchWise) {
        this.rdoBranchWise = rdoBranchWise;
    }
    
    /**
     * Getter for property rdoRegionalWise.
     * @return Value of property rdoRegionalWise.
     */
    public boolean getRdoRegionalWise() {
        return rdoRegionalWise;
    }
    
    /**
     * Setter for property rdoRegionalWise.
     * @param rdoRegionalWise New value of property rdoRegionalWise.
     */
    public void setRdoRegionalWise(boolean rdoRegionalWise) {
        this.rdoRegionalWise = rdoRegionalWise;
    }
    
    /**
     * Getter for property txtFromEmpIdValue.
     * @return Value of property txtFromEmpIdValue.
     */
    public java.lang.String getTxtFromEmpIdValue() {
        return txtFromEmpIdValue;
    }
    
    /**
     * Setter for property txtFromEmpIdValue.
     * @param txtFromEmpIdValue New value of property txtFromEmpIdValue.
     */
    public void setTxtFromEmpIdValue(java.lang.String txtFromEmpIdValue) {
        this.txtFromEmpIdValue = txtFromEmpIdValue;
    }
    
    /**
     * Getter for property txtToEmpIdValue.
     * @return Value of property txtToEmpIdValue.
     */
    public java.lang.String getTxtToEmpIdValue() {
        return txtToEmpIdValue;
    }
    
    /**
     * Setter for property txtToEmpIdValue.
     * @param txtToEmpIdValue New value of property txtToEmpIdValue.
     */
    public void setTxtToEmpIdValue(java.lang.String txtToEmpIdValue) {
        this.txtToEmpIdValue = txtToEmpIdValue;
    }
    
    /**
     * Getter for property empId.
     * @return Value of property empId.
     */
    public java.lang.String getEmpId() {
        return empId;
    }
    
    /**
     * Setter for property empId.
     * @param empId New value of property empId.
     */
    public void setEmpId(java.lang.String empId) {
        this.empId = empId;
    }
    
    /**
     * Getter for property empName.
     * @return Value of property empName.
     */
    public java.lang.String getEmpName() {
        return empName;
    }
    
    /**
     * Setter for property empName.
     * @param empName New value of property empName.
     */
    public void setEmpName(java.lang.String empName) {
        this.empName = empName;
    }
    
    /**
     * Getter for property designation.
     * @return Value of property designation.
     */
    public java.lang.String getDesignation() {
        return designation;
    }
    
    /**
     * Setter for property designation.
     * @param designation New value of property designation.
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
    }
    
    /**
     * Getter for property empBranch.
     * @return Value of property empBranch.
     */
    public java.lang.String getEmpBranch() {
        return empBranch;
    }
    
    /**
     * Setter for property empBranch.
     * @param empBranch New value of property empBranch.
     */
    public void setEmpBranch(java.lang.String empBranch) {
        this.empBranch = empBranch;
    }
    
    /**
     * Getter for property basic.
     * @return Value of property basic.
     */
    public java.lang.String getBasic() {
        return basic;
    }
    
    /**
     * Setter for property basic.
     * @param basic New value of property basic.
     */
    public void setBasic(java.lang.String basic) {
        this.basic = basic;
    }
    
    /**
     * Getter for property da.
     * @return Value of property da.
     */
    public java.lang.String getDa() {
        return da;
    }
    
    /**
     * Setter for property da.
     * @param da New value of property da.
     */
    public void setDa(java.lang.String da) {
        this.da = da;
    }
    
    /**
     * Getter for property hra.
     * @return Value of property hra.
     */
    public java.lang.String getHra() {
        return hra;
    }
    
    /**
     * Setter for property hra.
     * @param hra New value of property hra.
     */
    public void setHra(java.lang.String hra) {
        this.hra = hra;
    }
    
    /**
     * Getter for property pf.
     * @return Value of property pf.
     */
    public java.lang.String getPf() {
        return pf;
    }
    
    /**
     * Setter for property pf.
     * @param pf New value of property pf.
     */
    public void setPf(java.lang.String pf) {
        this.pf = pf;
    }
    
    /**
     * Getter for property pt.
     * @return Value of property pt.
     */
    public java.lang.String getPt() {
        return pt;
    }
    
    /**
     * Setter for property pt.
     * @param pt New value of property pt.
     */
    public void setPt(java.lang.String pt) {
        this.pt = pt;
    }
    
    /**
     * Getter for property cca.
     * @return Value of property cca.
     */
    public java.lang.String getCca() {
        return cca;
    }
    
    /**
     * Setter for property cca.
     * @param cca New value of property cca.
     */
    public void setCca(java.lang.String cca) {
        this.cca = cca;
    }
    
    /**
     * Getter for property splAllowance.
     * @return Value of property splAllowance.
     */
    public java.lang.String getSplAllowance() {
        return splAllowance;
    }
    
    /**
     * Setter for property splAllowance.
     * @param splAllowance New value of property splAllowance.
     */
    public void setSplAllowance(java.lang.String splAllowance) {
        this.splAllowance = splAllowance;
    }
    
    /**
     * Getter for property it.
     * @return Value of property it.
     */
    public java.lang.String getIt() {
        return it;
    }
    
    /**
     * Setter for property it.
     * @param it New value of property it.
     */
    public void setIt(java.lang.String it) {
        this.it = it;
    }
    
    /**
     * Getter for property loans.
     * @return Value of property loans.
     */
    public java.lang.String getLoans() {
        return loans;
    }
    
    /**
     * Setter for property loans.
     * @param loans New value of property loans.
     */
    public void setLoans(java.lang.String loans) {
        this.loans = loans;
    }
    
    /**
     * Getter for property extraLeave.
     * @return Value of property extraLeave.
     */
    public java.lang.String getExtraLeave() {
        return extraLeave;
    }
    
    /**
     * Setter for property extraLeave.
     * @param extraLeave New value of property extraLeave.
     */
    public void setExtraLeave(java.lang.String extraLeave) {
        this.extraLeave = extraLeave;
    }
    
    /**
     * Getter for property salaryStatus.
     * @return Value of property salaryStatus.
     */
    public java.lang.String getSalaryStatus() {
        return salaryStatus;
    }
    
    /**
     * Setter for property salaryStatus.
     * @param salaryStatus New value of property salaryStatus.
     */
    public void setSalaryStatus(java.lang.String salaryStatus) {
        this.salaryStatus = salaryStatus;
    }
    
    /**
     * Getter for property salaryDate.
     * @return Value of property salaryDate.
     */
    public java.lang.String getSalaryDate() {
        return salaryDate;
    }
    
    /**
     * Setter for property salaryDate.
     * @param salaryDate New value of property salaryDate.
     */
    public void setSalaryDate(java.lang.String salaryDate) {
        this.salaryDate = salaryDate;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    /**
     * Getter for property statusBy.
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }
    
    /**
     * Setter for property statusBy.
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }
    
    /**
     * Getter for property statusDate.
     * @return Value of property statusDate.
     */
    public java.lang.String getStatusDate() {
        return statusDate;
    }
    
    /**
     * Setter for property statusDate.
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.lang.String statusDate) {
        this.statusDate = statusDate;
    }
    
    /**
     * Getter for property authorizeStatus.
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }
    
    /**
     * Setter for property authorizeStatus.
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
    /**
     * Getter for property authorizeBy.
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }
    
    /**
     * Setter for property authorizeBy.
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }
    
    /**
     * Getter for property authorizeDate.
     * @return Value of property authorizeDate.
     */
    public java.lang.String getAuthorizeDate() {
        return authorizeDate;
    }
    
    /**
     * Setter for property authorizeDate.
     * @param authorizeDate New value of property authorizeDate.
     */
    public void setAuthorizeDate(java.lang.String authorizeDate) {
        this.authorizeDate = authorizeDate;
    }
    
    /**
     * Getter for property branchCode.
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }
    
    /**
     * Setter for property branchCode.
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }
    
    /**
     * Getter for property netSalary.
     * @return Value of property netSalary.
     */
    public java.lang.String getNetSalary() {
        return netSalary;
    }
    
    /**
     * Setter for property netSalary.
     * @param netSalary New value of property netSalary.
     */
    public void setNetSalary(java.lang.String netSalary) {
        this.netSalary = netSalary;
    }
    
    /**
     * Getter for property txtSalFromDateMMValue.
     * @return Value of property txtSalFromDateMMValue.
     */
    public java.lang.String getTxtSalFromDateMMValue() {
        return txtSalFromDateMMValue;
    }
    
    /**
     * Setter for property txtSalFromDateMMValue.
     * @param txtSalFromDateMMValue New value of property txtSalFromDateMMValue.
     */
    public void setTxtSalFromDateMMValue(java.lang.String txtSalFromDateMMValue) {
        this.txtSalFromDateMMValue = txtSalFromDateMMValue;
    }
    
    /**
     * Getter for property txtSalFromDateYYYYValue.
     * @return Value of property txtSalFromDateYYYYValue.
     */
    public java.lang.String getTxtSalFromDateYYYYValue() {
        return txtSalFromDateYYYYValue;
    }
    
    /**
     * Setter for property txtSalFromDateYYYYValue.
     * @param txtSalFromDateYYYYValue New value of property txtSalFromDateYYYYValue.
     */
    public void setTxtSalFromDateYYYYValue(java.lang.String txtSalFromDateYYYYValue) {
        this.txtSalFromDateYYYYValue = txtSalFromDateYYYYValue;
    }
    
    /**
     * Getter for property newEarning.
     * @return Value of property newEarning.
     */
    public boolean isNewEarning() {
        return newEarning;
    }
    
    /**
     * Setter for property newEarning.
     * @param newEarning New value of property newEarning.
     */
    public void setNewEarning(boolean newEarning) {
        this.newEarning = newEarning;
    }
    
    /**
     * Getter for property earningID.
     * @return Value of property earningID.
     */
    public java.lang.String getEarningID() {
        return earningID;
    }
    
    /**
     * Setter for property earningID.
     * @param earningID New value of property earningID.
     */
    public void setEarningID(java.lang.String earningID) {
        this.earningID = earningID;
    }
    
    /**
     * Getter for property tbmEarning.
     * @return Value of property tbmEarning.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmEarning() {
        return tbmEarning;
    }
    
    /**
     * Setter for property tbmEarning.
     * @param tbmEarning New value of property tbmEarning.
     */
    public void setTbmEarning(com.see.truetransact.clientutil.EnhancedTableModel tbmEarning) {
        this.tbmEarning = tbmEarning;
    }
    
    /**
     * Getter for property tbmDeduction.
     * @return Value of property tbmDeduction.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmDeduction() {
        return tbmDeduction;
    }
    
    /**
     * Setter for property tbmDeduction.
     * @param tbmDeduction New value of property tbmDeduction.
     */
    public void setTbmDeduction(com.see.truetransact.clientutil.EnhancedTableModel tbmDeduction) {
        this.tbmDeduction = tbmDeduction;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property newDeduction.
     * @return Value of property newDeduction.
     */
    public boolean isNewDeduction() {
        return newDeduction;
    }
    
    /**
     * Setter for property newDeduction.
     * @param newDeduction New value of property newDeduction.
     */
    public void setNewDeduction(boolean newDeduction) {
        this.newDeduction = newDeduction;
    }
    
    /**
     * Getter for property dtSlNo.
     * @return Value of property dtSlNo.
     */
    public java.lang.String getDtSlNo() {
        return dtSlNo;
    }
    
    /**
     * Setter for property dtSlNo.
     * @param dtSlNo New value of property dtSlNo.
     */
    public void setDtSlNo(java.lang.String dtSlNo) {
        this.dtSlNo = dtSlNo;
    }
    
    /**
     * Getter for property txtNoOfDaysLOP.
     * @return Value of property txtNoOfDaysLOP.
     */
    public java.lang.String getTxtNoOfDaysLOP() {
        return txtNoOfDaysLOP;
    }    
    
    /**
     * Setter for property txtNoOfDaysLOP.
     * @param txtNoOfDaysLOP New value of property txtNoOfDaysLOP.
     */
    public void setTxtNoOfDaysLOP(java.lang.String txtNoOfDaysLOP) {
        this.txtNoOfDaysLOP = txtNoOfDaysLOP;
    }
    
    /**
     * Getter for property txtInstIntRate.
     * @return Value of property txtInstIntRate.
     */
    public java.lang.String getTxtInstIntRate() {
        return txtInstIntRate;
    }
    
    /**
     * Setter for property txtInstIntRate.
     * @param txtInstIntRate New value of property txtInstIntRate.
     */
    public void setTxtInstIntRate(java.lang.String txtInstIntRate) {
        this.txtInstIntRate = txtInstIntRate;
    }
    
    /**
     * Getter for property txtIntNetAmount.
     * @return Value of property txtIntNetAmount.
     */
    public java.lang.String getTxtIntNetAmount() {
        return txtIntNetAmount;
    }
    
    /**
     * Setter for property txtIntNetAmount.
     * @param txtIntNetAmount New value of property txtIntNetAmount.
     */
    public void setTxtIntNetAmount(java.lang.String txtIntNetAmount) {
        this.txtIntNetAmount = txtIntNetAmount;
    }
    
    /**
     * Getter for property txtLoanSanctionAmt.
     * @return Value of property txtLoanSanctionAmt.
     */
    public java.lang.String getTxtLoanSanctionAmt() {
        return txtLoanSanctionAmt;
    }
    
    /**
     * Setter for property txtLoanSanctionAmt.
     * @param txtLoanSanctionAmt New value of property txtLoanSanctionAmt.
     */
    public void setTxtLoanSanctionAmt(java.lang.String txtLoanSanctionAmt) {
        this.txtLoanSanctionAmt = txtLoanSanctionAmt;
    }
    
}
