/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IncrementsPromotionsOB.java
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
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.common.IncrementTO;
import com.see.truetransact.transferobject.common.PromotionTO;
import com.see.truetransact.transferobject.common.SalaryMasterTO;
import com.see.truetransact.transferobject.common.SalaryMasterDetailsTO;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import org.apache.log4j.Logger;
/**
 * @author  Sathiya
 **/

public class IncrementsPromotionsOB extends CObservable{
    
    private static IncrementsPromotionsOB incrementsPromotionsOB;
    private ProxyFactory proxy;
    private HashMap _authorizeMap;
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private int actionType;
    private int result;
    private String lblStatus;
    Date curDate = null;
    private EnhancedTableModel tblSalaryMaster,tblSalaryMasterDtails;
    private ComboBoxModel cbmPromotionDesigValue,cbmBranchwiseValue,cbmRegionalValue;
    private TableModel tbmIncrement,tbmPromotion,tbmLog;
    private ArrayList key,value,IncrementTOs,deleteIncrementList,PromotionTOs,deletePromotionList,SalaryCalcList;
    private String cboPromotionDesigValue;
    private String customerId = "";
    private String lblIncrementSLNOValue = "";
    private String txtEmployeeId = "";
    private String txtIncrementEmpName="";
    private String txtIncrementSLNO="";
    private String txtIncrementDesignation = "";
    private String txtBasicSalary="";
    private String tdtIncrementDate="";
    private String tdtIncrementEffectiveDateValue = "";
    private String tdtIncrementCreatedDateValue = "";
    private String lblPromotionSLNOValue = "";
    private String txtPromotionEmployeeId = "";
    private String txtPromotionEmployeeName="";
    private String txtPromotionDesignation="";
    private String txtPromotionEmpBranch="";
    private String txtPromotionLastDesg = "";
    private String tdtPromotionEffectiveDateValue = "";
    private String tdtPromotionCreatedDateValue = "";
    private String txtPromotionBasicPayValue = "";
    private String DTAuthorizeStatus = "";
    private String EDAuthorizeStatus = "";
    private boolean rdoEmployeeWise = false;
    private boolean rdoBranchWise = false;
    private boolean rdoRegionalWise = false;
    private String txtFromEmpIdValue = "";
    private String txtEmployeeName = "";
    private String txtEmployeeDesig = "";
    private String txtToEmpIdValue = "";
    private String txtEmployeeGrade="";
    private String txtIncrementAmount="";
    private String txtNewBasic="";
    private String txtIncrementNo="";
    private String cboBranchwiseValue = "";
    private String cboRegionalValue = "";
    private String tdtSalFromDateValue = "";
    private String incrementID="";
    private String promotionID="";
    private HashMap empSalDetailsMap = null;
    private String SalaryMasterStatus = "";
    /*FOR SALARY CALCULATON OF THE EMPLOYEE*/
    //    private String cboRegionalValue="";
    //    private String cboBranchwiseValue="";
    //    private String txtFromEmpIdValue="";
    //    private String txtToEmpIdValue="";
    //    // the last Effective Date and the Current date
    //    private Date tdtSalFromDateValue= null;
    private ArrayList salMasterColumn = new ArrayList();
    private ArrayList salDetailsColumn = new ArrayList();
    private String tdtSalToDateValue= "";
    private String txtSalFromDateMMValue="";
    private String txtSalFromDateYYYYValue="";
    private String txtSalaryID="";
    private String txtSalToDateMMValue="";
    private String txtSalToDateYYYYValue="";
    private String txtTotalDeduction="";
    private String txtTotalEarnings="";
    private String txtNetSalary="";
    int pan = -1;
    int panEditDelete = -1;
    private int INCREMENT = 1,PROMOTION = 2,ARREARS = 3;
    /** Creates a new instance of MisecllaniousDeductionOB */
    private IncrementsPromotionsOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        if(curDate != null){
            Date insDate = (Date) curDate.clone();
            insDate.setDate(curDate.getDate());
            insDate.setMonth(curDate.getMonth());
            insDate.setYear(curDate.getYear());
            curDate = insDate;
        }
        map = new HashMap();
        map.put(CommonConstants.JNDI, "IncrementsPromotionsJNDI");
        map.put(CommonConstants.HOME, "common.IncrementsPromotionsHome");
        map.put(CommonConstants.REMOTE,"common.IncrementsPromotions");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        createSalaryMasterTable();
        createSalaryDetailsTable();
        fillDropDown();
        setTable();
        IncrementTOs = new ArrayList();
        deleteIncrementList = new ArrayList();
        PromotionTOs = new ArrayList();
        deletePromotionList = new ArrayList();
    }
    private void createSalaryMasterTable() throws Exception{
        
        salMasterColumn.add("EMPLOYEE ID");
        salMasterColumn.add("BASIC");
        salMasterColumn.add("SALARY DT");
        salMasterColumn.add("CREATED DT");
        salMasterColumn.add("STATUS");
        tblSalaryMaster = new EnhancedTableModel(null, salMasterColumn);
        
    }
    private void createSalaryDetailsTable() throws Exception{
        
        salDetailsColumn.add("SALARY TYPE");
        salDetailsColumn.add("AMOUNT");
        salDetailsColumn.add("EARN/DED");
        tblSalaryMasterDtails = new EnhancedTableModel(null, salDetailsColumn);
        
    }
    public static IncrementsPromotionsOB getInstance(){
        try {
            incrementsPromotionsOB = new IncrementsPromotionsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return incrementsPromotionsOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("EmployeeId");
        columnHeader.add("Effective Date");
        columnHeader.add("Employee Stage");
        ArrayList data = new ArrayList();
        tbmIncrement = new TableModel(data,columnHeader);
        columnHeader = new ArrayList();
        columnHeader.add("EmployeeId");
        columnHeader.add("EmployeeName");
        columnHeader.add("Effective Date");
        columnHeader.add("Last Grade");
        columnHeader.add("Promotion Grade");
        data = new ArrayList();
        tbmPromotion = new TableModel(data,columnHeader);
    }
    
    private void fillDropDown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "IncrementsPromotionsJNDI");
        lookupMap.put(CommonConstants.HOME, "serverside.common.IncrementsPromotionsHome");
        lookupMap.put(CommonConstants.REMOTE, "serverside.common.IncrementsPromotions");
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SALARY_STRUCTURE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        key =  new ArrayList();
        value = new ArrayList();
        fillData((HashMap)lookupValues.get("SALARY_STRUCTURE"));
        this.cbmPromotionDesigValue = new ComboBoxModel(key,value);
        key =  new ArrayList();
        value = new ArrayList();
        this.cbmBranchwiseValue = new ComboBoxModel(key,value);
        List lst = (List)ClientUtil.executeQuery("getAllBranchesList", null);
        System.out.println("########ListForAgent : "+lst);
        getMap(lst);
        setCbmBranchwiseValue(new ComboBoxModel(key,value));
        key =  new ArrayList();
        value = new ArrayList();
        this.cbmRegionalValue = new ComboBoxModel(key,value);
        List lstZonal = (List)ClientUtil.executeQuery("getAllZonalList", null);
        System.out.println("########ListForAgent : "+lst);
        getMap(lstZonal);
        setCbmRegionalValue(new ComboBoxModel(key,value));
        param = null;
        lookupValues = null;
        key =  new ArrayList();
        value = new ArrayList();
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
    
    public void resetTable(){
        this.tbmPromotion.setData(new ArrayList());
        this.tbmPromotion.fireTableDataChanged();
        this.PromotionTOs.clear();
        this.deletePromotionList.clear();
        this.tbmIncrement.setData(new ArrayList());
        this.tbmIncrement.fireTableDataChanged();
        this.IncrementTOs.clear();
        this.deleteIncrementList.clear();
    }
    
    public void resetForm(){
        setLblStatus("");
        resetTable();
        resetIncrement();
        resetPromotion();
        resetArrears();
        resetSalaryMasterTable();
        resetSalaryMasterDetailsTable();
        setChanged();
        notifyObservers();
    }
    public void resetArrears(){
        setTxtTotalDeduction("");
        setTxtTotalEarnings("");
        setTxtNetSalary("");
        setTxtSalFromDateMMValue("");
        setTxtSalFromDateYYYYValue("");
        setTxtSalToDateMMValue("");
        setTxtSalToDateYYYYValue("");
        setTxtSalaryID("");
        setCboBranchwiseValue("");
        setTxtFromEmpIdValue("");
        setTxtEmployeeName("");
        setTxtEmployeeDesig("");
        setTxtToEmpIdValue("");
    }
    public void resetSalaryMasterTable(){
        for(int i = tblSalaryMaster.getRowCount(); i > 0; i--){
            tblSalaryMaster.removeRow(0);
        }
    }
    
    public void resetSalaryMasterDetailsTable(){
        for(int i = tblSalaryMasterDtails.getRowCount(); i > 0; i--){
            tblSalaryMasterDtails.removeRow(0);
        }
    }
    public void resetPromotion(){
        setLblPromotionSLNOValue("");
        setTxtPromotionEmployeeId("");
        setTxtPromotionEmployeeName("");
        setTxtPromotionDesignation("");
        setTxtPromotionEmpBranch("");
        setPromotionID("");
        setTxtPromotionLastDesg("");
        setTdtPromotionEffectiveDateValue("");
        setTdtPromotionCreatedDateValue("");
        setTxtPromotionBasicPayValue("");
        getCbmPromotionDesigValue().setKeyForSelected("");
    }
    
    public void resetIncrement(){
        setLblIncrementSLNOValue("");
        setTxtEmployeeId("");
        setTxtIncrementEmpName("");
        setTxtIncrementSLNO("");
        setTxtIncrementDesignation("");
        setIncrementID("");
        setTxtBasicSalary("");
        setTdtIncrementDate("");
        setTdtIncrementEffectiveDateValue("");
        setTdtIncrementCreatedDateValue("");
        setTxtEmployeeGrade("");
        setTxtIncrementAmount("");
        setTxtNewBasic("");
        setTxtIncrementNo("");
    }
    
    private void setTableDataIncrement(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        IncrementTO objDT ;
        int size = IncrementTOs.size();
        for(int i=0;i<size;i++){
            objDT = (IncrementTO)IncrementTOs.get(i);
            objDT.setStatus(CommonConstants.STATUS_MODIFIED);
            HashMap eachEmpMap = new HashMap();
            eachEmpMap.put("EMPLOYEE_ID",objDT.getEmpId());
            List lst = ClientUtil.executeQuery("getSelectCustandLastIncDate", eachEmpMap);
            if(lst!=null && lst.size()>0){
                eachEmpMap = (HashMap)lst.get(0);
                setTxtIncrementEmpName(CommonUtil.convertObjToStr(eachEmpMap.get("Customer")));
                setTxtIncrementDesignation(CommonUtil.convertObjToStr(eachEmpMap.get("Customer")));
                setTdtIncrementDate(CommonUtil.convertObjToStr(eachEmpMap.get("LAST_INCREMENT_DATE")));
            }
            row = setRowIncrement(objDT);
            rows.add(row);
        }
        tbmIncrement.setData(rows);
        tbmIncrement.fireTableDataChanged();
    }
    
    public HashMap checkIfAlreadyCalculated(int month,int year){
        Date salaryForDate = (Date) curDate.clone();
        HashMap salaryViewMap = null;
        if(month != 0 && year !=0){
            salaryForDate.setDate(1);
            salaryForDate.setMonth(month-1);
            salaryForDate.setYear(year-1900);
            salaryForDate = setProperDtFormat(salaryForDate);
            salaryViewMap = new HashMap();
            salaryViewMap.put("SALARY_FROM_DT",salaryForDate);
            List salary_idLst = ClientUtil.executeQuery("checkIfSalIDpresent", salaryViewMap);
            salaryViewMap = null;
            if(salary_idLst!= null && salary_idLst.size()>0){
                salaryViewMap = new HashMap();
                salaryViewMap = (HashMap)salary_idLst.get(0);
                System.out.println("@#$@#$salaryViewMap"+salaryViewMap);
            }
        }
        return salaryViewMap;
    }
    public void populateSalaryDetails(HashMap SalaryMap,String salCalcType){
        resetSalaryMasterTable();
        resetSalaryMasterDetailsTable();
        HashMap whereMap = new HashMap();
        List tableLst = new ArrayList();
        whereMap.put("SALARY_ID",SalaryMap.get("SALARY_ID"));
        if(salCalcType.equals("TRIAL")){
            List salDetailsList = (List) SalaryMap.get("SALARY_DETAILS");
            System.out.println("#@$@#$@#salDetailsList:"+salDetailsList);
            HashMap individualEmpMap = null;
            if(salDetailsList != null){
                for(int i =0;i<salDetailsList.size();i++){
                    System.out.println("!@#!@#!@#salDetailsList.size():"+salDetailsList.size());
                    List rowLst = new ArrayList();
                    individualEmpMap = new HashMap();
                    ArrayList individualEmpLst = new ArrayList();
                    individualEmpLst = (ArrayList)salDetailsList.get(i);
                    individualEmpMap = new HashMap();
                    if(individualEmpLst.size()> 0){
                        System.out.println("!@#!@#!@#salDetailsList.get(i):"+salDetailsList.get(i));
                        individualEmpMap = (HashMap)individualEmpLst.get(0);
                        System.out.println("#$@$@#$@individualEmpMap"+individualEmpMap);
//                        setTxtEmployeeDesig(CommonUtil.convertObjToStr(individualEmpMap.get("PRESENT_DISGNATION")));
//                        setTxtEmployeeName(CommonUtil.convertObjToStr(individualEmpMap.get("FIRST_NAME")));
                        rowLst.add(individualEmpMap.get("EMPLOYEE_CODE"));
                        rowLst.add(individualEmpMap.get("PRESENT_BASIC"));
                        rowLst.add(SalaryMap.get("SALARY_DT"));
                        //                        rowLst.add(curDate);
                        rowLst.add(curDate);
                        //                    rowLst.add(individualEmpMap.get("CREATED_DATE"));
                        rowLst.add(salCalcType);
                        rowLst.add(individualEmpMap.get("SALARY_STATUS"));
                        empSalDetailsMap.put(individualEmpMap.get("EMPLOYEE_CODE"),individualEmpMap);
                        tableLst.add(rowLst);
                    }
                }
                setTxtSalaryID(CommonUtil.convertObjToStr(salCalcType));
                //                setCboBranchwiseValue(CommonUtil.convertObjToStr(individualEmpMap.get("BRANCH_CODE")));
                //                setCboRegionalValue(CommonUtil.convertObjToStr(individualEmpMap.get("BRANCH_CODE")));
                tblSalaryMaster = new EnhancedTableModel((ArrayList)tableLst,salMasterColumn);
            }
        }
        else if(salCalcType.equals("ENQUIRY")){
            String salaryId = "";
            System.out.println("@!#!@#inside ENQUIRY:"+SalaryMap);
            List salDetailsList = (List) SalaryMap.get("SALARY_DETAILS");
            System.out.println("#@$@#$@#salDetailsList:"+salDetailsList);
            if(salDetailsList!= null && salDetailsList.size()>0){
                for(int i =0;i<salDetailsList.size();i++){
                    List rowLst = new ArrayList();
                    HashMap indEmpSalMap = (HashMap)salDetailsList.get(i);
                    salaryId = (String)CommonUtil.convertObjToStr(indEmpSalMap.get("SALARY_ID"));
                    rowLst.add(indEmpSalMap.get("EMPLOYEE_ID"));
                    rowLst.add(indEmpSalMap.get("BASIC"));
                    rowLst.add(SalaryMap.get("SALARY_DT"));
                    rowLst.add(curDate);
                    //                        rowLst.add(salCalcType);
                    rowLst.add(indEmpSalMap.get("SALARY_STATUS"));
                    empSalDetailsMap.put(indEmpSalMap.get("EMPLOYEE_ID"),indEmpSalMap);
                    tableLst.add(rowLst);
                }
                setTxtSalaryID(CommonUtil.convertObjToStr(salaryId));
                tblSalaryMaster = new EnhancedTableModel((ArrayList)tableLst,salMasterColumn);
            }
            else{
                ClientUtil.showMessageWindow("No Records found!!!");
            }
        }
        else{
            List list=ClientUtil.executeQuery("getEmployeesFromSalID", whereMap);
            System.out.println("#$%#$%#$%#% employee list:"+list);
            LinkedHashMap dataMap = new LinkedHashMap();
            List rowLst= new ArrayList();
            if(list!= null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    rowLst = new ArrayList();
                    dataMap = new LinkedHashMap();
                    dataMap =(LinkedHashMap)list.get(i);
                    if(i == 0){
                        setTxtFromEmpIdValue(CommonUtil.convertObjToStr(dataMap.get("EMPLOYEE_ID")));
                    }
                    rowLst.add(dataMap.get("EMPLOYEE_ID"));
                    rowLst.add(dataMap.get("BASIC"));
                    rowLst.add(dataMap.get("SALARY_FROM_DT"));
                    rowLst.add(dataMap.get("CREATED_DATE"));
                    rowLst.add(dataMap.get("SALARY_STATUS"));
                    if(i == list.size()-1){
                        setTxtToEmpIdValue(CommonUtil.convertObjToStr(dataMap.get("EMPLOYEE_ID")));
                    }
                    tableLst.add(rowLst);
                }
                setTxtEmployeeDesig(CommonUtil.convertObjToStr(dataMap.get("PRESENT_DISGNATION")));
                setTxtEmployeeName(CommonUtil.convertObjToStr(dataMap.get("FIRST_NAME")));
                setTxtSalaryID(CommonUtil.convertObjToStr(dataMap.get("SALARY_ID")));
                setCboBranchwiseValue(CommonUtil.convertObjToStr(dataMap.get("PRESENT_BRANCH_CODE")));
                setCboRegionalValue(CommonUtil.convertObjToStr(dataMap.get("ZONAL_CODE")));
                tblSalaryMaster = new EnhancedTableModel((ArrayList)tableLst,salMasterColumn);
            }
        }
        
    }
    public void insertSalaryData(int rowNo) {
        System.out.println("@#$@#$SalaryMasterStatus:"+SalaryMasterStatus);
        LinkedHashMap dataMap = new LinkedHashMap();
        List rowLst= new ArrayList();
        List tableLst = new ArrayList();
        String empId = CommonUtil.convertObjToStr(tblSalaryMaster.getValueAt(rowNo,0));
        String earnOrDeduction = "";
        if(SalaryMasterStatus.equals("TRIAL")){
            System.out.println("@#$@#$#@empSalDetailsMap:"+empId);
            HashMap indEmpMap =(HashMap)  empSalDetailsMap.get(empId);
            System.out.println("@#$@#$#@indEmpMap:"+indEmpMap);
            setTxtEmployeeDesig(CommonUtil.convertObjToStr(indEmpMap.get("PRESENT_DISGNATION")));
            setTxtEmployeeName(CommonUtil.convertObjToStr(indEmpMap.get("FIRST_NAME")));
            HashMap salTypeMap = (HashMap) indEmpMap.get("SALARY_TYPE");
            HashMap earnOrDeductionMap = (HashMap) indEmpMap.get("EARNING_OR_DEDUCTION");
            double earning = 0.0;
            double deduction = 0.0;
            double totalSalary = 0.0;
            ArrayList earningList = new ArrayList();
            ArrayList deductionList = new ArrayList();
            Object[] objKeys = salTypeMap.keySet().toArray();
            for(int i=0; i<objKeys.length; i++) {
                rowLst = new ArrayList();
                rowLst.add(CommonUtil.convertObjToStr(objKeys[i]));
                rowLst.add(CommonUtil.convertObjToStr(salTypeMap.get(objKeys[i])));
                rowLst.add(CommonUtil.convertObjToStr(earnOrDeductionMap.get(objKeys[i])));
                double amountAllowance = CommonUtil.convertObjToDouble(salTypeMap.get(objKeys[i])).doubleValue();
                String earnOrDeduct = CommonUtil.convertObjToStr(earnOrDeductionMap.get(objKeys[i]));
                
                if(earnOrDeduct.equals("EARNING")){
                    earning += amountAllowance;
                    earningList.add(rowLst);
                }
                else if(earnOrDeduct.equals("DEDUCTION")){
                    deduction += amountAllowance;
                    deductionList.add(rowLst);
                }
                
            }
            tableLst.addAll(earningList);
            tableLst.addAll(deductionList);
            Collections.synchronizedList(tableLst);
            totalSalary = earning - deduction;
            setTxtTotalEarnings(String.valueOf(earning));
            setTxtTotalDeduction(String.valueOf(deduction));
            setTxtNetSalary(String.valueOf(totalSalary));
            tblSalaryMasterDtails = new EnhancedTableModel((ArrayList)tableLst,salDetailsColumn);
        }
        else{
            String salaryID = getTxtSalaryID();
            HashMap whereMap = new HashMap();
            whereMap.put("EMPLOYEE_ID",empId);
            whereMap.put("SALARY_ID",salaryID);
            List lst = ClientUtil.executeQuery("getSalaryMasterDetails", whereMap);
            if(lst!=null && lst.size()>0){
                LinkedHashMap detailsMap = new LinkedHashMap();
                detailsMap =(LinkedHashMap)lst.get(0);
                System.out.println("@#$@#$@#$detailsMap"+detailsMap);
                setTxtEmployeeDesig(CommonUtil.convertObjToStr(detailsMap.get("PRESENT_DISGNATION")));
                setTxtEmployeeName(CommonUtil.convertObjToStr(detailsMap.get("FIRST_NAME")));
                double earning = 0.0;
                double deduction = 0.0;
                double totalSalary = 0;
                ArrayList earningList = new ArrayList();
                ArrayList deductionList = new ArrayList();
                for(int i=0;i<lst.size();i++){
                    rowLst = new ArrayList();
                    dataMap = new LinkedHashMap();
                    dataMap =(LinkedHashMap)lst.get(i);
                    rowLst.add(dataMap.get("SALARY_TYPE"));
                    rowLst.add(dataMap.get("AMOUNT"));
                    rowLst.add(dataMap.get("EARNING_OR_DEDUCTION"));
                    double amountAllowance = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                    String earnOrDeduct = CommonUtil.convertObjToStr(dataMap.get("EARNING_OR_DEDUCTION"));
                    
                    if(earnOrDeduct.equals("EARNING")){
                        earning += amountAllowance;
                        earningList.add(rowLst);
                    }
                    else if(earnOrDeduct.equals("DEDUCTION")){
                        deduction += amountAllowance;
                        deductionList.add(rowLst);
                    }
                    totalSalary = earning - deduction;
                    
                    setTxtTotalEarnings(String.valueOf(earning));
                    setTxtTotalDeduction(String.valueOf(deduction));
                    setTxtNetSalary(String.valueOf(totalSalary));
                }
                tableLst.addAll(earningList);
                tableLst.addAll(deductionList);
                Collections.synchronizedList(tableLst);
                tblSalaryMasterDtails = new EnhancedTableModel((ArrayList)tableLst,salDetailsColumn);
            }
        }
    }
    public void populateIncrementPromotionData(String grade,int panEditDelete){
        HashMap whereMap = new HashMap();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameSal = "";
        whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        whereMap.put("EMPLOYEE_ID",grade);
        if(getActionType() != ClientConstants.ACTIONTYPE_NEW){
            mapNameDT = "getSelectIncrementEditTO";
            mapNameED = "getSelectPromotionEditTO";
            mapNameSal="getSelectSalaryMasterDetEditTO";
            
            if(panEditDelete==INCREMENT){
                List list = ClientUtil.executeQuery(mapNameDT,whereMap);
                if(list!=null && list.size()>0){
                    IncrementTOs = (ArrayList)list;
                    setTableDataIncrement();
                }
            }
            else if(panEditDelete==PROMOTION){
                List list = ClientUtil.executeQuery(mapNameED,whereMap);
                if(list!=null && list.size()>0){
                    PromotionTOs = (ArrayList)list;
                    setTablePromotionData();
                }
            }
            //            else if(panEditDelete==ARREARS){
            //                List list = ClientUtil.executeQuery(mapNameDT,whereMap);
            //                for(int i=0;i<list.size();i++){
            //
            //                }
            //                IncrementTO obj = null;
            //                obj.setStatus(CommonConstants.STATUS_MODIFIED);
            //            }
        }
        whereMap = null;
    }
    
    public void doAction(int pan){
        try{
            HashMap objHashMap=null;
            if(_authorizeMap==null) {
                if (deleteIncrementList!=null) {
                    IncrementTOs.addAll(deleteIncrementList);
                }
                if (deletePromotionList!=null) {
                    PromotionTOs.addAll(deletePromotionList);
                }
                objHashMap = new HashMap();
                if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                }
                if(IncrementTOs != null && IncrementTOs.size()>0 && pan==INCREMENT){
                    objHashMap.put("IncrementTOs",IncrementTOs);
                    objHashMap.put("deleteIncrementList",deleteIncrementList);
                }
                else if(PromotionTOs != null && PromotionTOs.size()>0 && pan==PROMOTION){
                    objHashMap.put("PromotionTOs",PromotionTOs);
                    objHashMap.put("deletePromotionList",deletePromotionList);
                }
                else if(SalaryCalcList != null && SalaryCalcList.size()>0 && pan==ARREARS){
                    objHashMap.put("SalaryCalcList",SalaryCalcList);
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
    
    public int insertIncrementData(int rowNo) {
        IncrementTO obj = IncrementTO(rowNo);
        if(rowNo == -1){
            if (IncrementTOs==null) {
                IncrementTOs = new ArrayList();
            }
            IncrementTOs.add(obj);
            ArrayList irRow = this.setRowIncrement(obj);
            tbmIncrement.insertRow(tbmIncrement.getRowCount(), irRow);
            tbmIncrement.fireTableDataChanged();
        }
        obj = null;
        return 0;
    }
    
    
    public void populateIncrement(int rowNum) {
        IncrementTO obj = (IncrementTO)IncrementTOs.get(rowNum);
        System.out.println("obj.getEmpId()@#@#"+obj.getEmpId());
        setTxtEmployeeId(obj.getEmpId());
        setTxtBasicSalary(obj.getTxtBasicSalary());
        setTxtIncrementDesignation(obj.getTxtIncrementDesignation());
        setIncrementID(obj.getIncrementID());
        setTxtIncrementEmpName(obj.getTxtIncrementEmpName());
        setTdtIncrementDate(CommonUtil.convertObjToStr(obj.getTdtIncrementDate()));
        setTdtIncrementEffectiveDateValue(CommonUtil.convertObjToStr(obj.getEffectiveDate()));
        setTdtIncrementCreatedDateValue(CommonUtil.convertObjToStr(obj.getCreatedDate()));
        setTxtEmployeeGrade(CommonUtil.convertObjToStr(obj.getEmployeeStage()));
        setTxtIncrementAmount(CommonUtil.convertObjToStr(obj.getTxtIncrementAmount()));
        setTxtNewBasic(CommonUtil.convertObjToStr(obj.getTxtNewBasic()));
        setTxtIncrementNo(CommonUtil.convertObjToStr(obj.getTxtIncrementNo())); 
    }
    
    private IncrementTO IncrementTO(int rowNo){
        IncrementTO obj = null;
        if(rowNo == -1){
            obj = new IncrementTO();
            if(tbmIncrement.getRowCount() == 0){
                obj.setSlNo(new Double(1));
            }else if(tbmIncrement.getRowCount() >=1){
                obj.setSlNo(new Double(tbmIncrement.getRowCount() + 1));
            }
            obj.setTdtIncrementDate(DateUtil.getDateMMDDYYYY(getTdtIncrementDate()));
            obj.setEmpId(getTxtEmployeeId());
            obj.setTxtBasicSalary(getTxtBasicSalary());
            obj.setTxtIncrementDesignation(getTxtIncrementDesignation());
            obj.setTxtIncrementEmpName(getTxtIncrementEmpName());
            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtIncrementEffectiveDateValue()));
            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtIncrementCreatedDateValue()));
            obj.setEmployeeStage(CommonUtil.convertObjToStr(getTxtEmployeeGrade()));
            obj.setTxtIncrementAmount(CommonUtil.convertObjToStr(getTxtIncrementAmount()));
            obj.setTxtNewBasic(CommonUtil.convertObjToStr(getTxtNewBasic()));
            obj.setTxtIncrementNo(CommonUtil.convertObjToStr(getTxtIncrementNo()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDt(curDate);
        }else{
            obj = (IncrementTO)IncrementTOs.get(rowNo);
            obj.setEmpId(getTxtEmployeeId());
            obj.setTxtBasicSalary(getTxtBasicSalary());
            obj.setTxtIncrementDesignation(getTxtIncrementDesignation());
            obj.setIncrementID(getIncrementID());
            obj.setTxtIncrementEmpName(getTxtIncrementEmpName());
            obj.setTdtIncrementDate(DateUtil.getDateMMDDYYYY(getTdtIncrementDate()));
            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtIncrementEffectiveDateValue()));
            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtIncrementCreatedDateValue()));
            obj.setEmployeeStage(CommonUtil.convertObjToStr(getTxtEmployeeGrade()));
            obj.setTxtIncrementAmount(CommonUtil.convertObjToStr(getTxtIncrementAmount()));
            obj.setTxtNewBasic(CommonUtil.convertObjToStr(getTxtNewBasic()));
            obj.setTxtIncrementNo(CommonUtil.convertObjToStr(getTxtIncrementNo()));
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDt(curDate);
            ArrayList irRow = setRowIncrement(obj);
            IncrementTOs.set(rowNo,obj);
            tbmIncrement.removeRow(rowNo);
            tbmIncrement.insertRow(rowNo,irRow);
            tbmIncrement.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowIncrement(IncrementTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getEmpId());
        row.add(obj.getEffectiveDate());
        row.add(obj.getEmployeeStage());
        return row;
    }
    
    public void deleteIncrementData(int rowNum) {
        IncrementTO obj = (IncrementTO)IncrementTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deleteIncrementList.add(obj);
        IncrementTOs.remove(rowNum);
        tbmIncrement.removeRow(rowNum);
        tbmIncrement.fireTableDataChanged();
        obj = null;
    }
    
    public int insertPromotionData(int rowNo) {
        //      To insert values on to the PromotionTO one Row at a time
        PromotionTO obj = PromotionTO(rowNo);
        if(rowNo == -1){
            //      To insert To values Into PromotionTOs Arraylist
            PromotionTOs.add(obj);
            ArrayList irRow = this.setRowPromotion(obj);
            tbmPromotion.insertRow(tbmPromotion.getRowCount(), irRow);
            tbmPromotion.fireTableDataChanged();
        }
        return 0;
    }
    
    private PromotionTO PromotionTO(int rowNo){
        PromotionTO obj = null;
        obj = new PromotionTO();
        if(rowNo == -1){
            //            To add into the PromotionTO for a new value
            if(tbmPromotion.getRowCount() == 0){
                obj.setSlNo(new Double(1));
            }else if(tbmPromotion.getRowCount()>0){
                obj.setSlNo(new Double(tbmPromotion.getRowCount() + 1));
            }
            obj.setEmpId(getTxtPromotionEmployeeId());
            obj.setTxtPromotionEmployeeName(getTxtPromotionEmployeeName());
            obj.setLastDesignation(getTxtPromotionDesignation());
            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtPromotionEffectiveDateValue()));
            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtPromotionCreatedDateValue()));
            //            obj.setEmployeeStage(getTxtPromotionLastDesg());
            obj.setTxtPromotionEmpBranch(getTxtPromotionEmpBranch());
            obj.setPresentBasic(getTxtPromotionBasicPayValue());
            //            obj.setPromotionStatus(CommonUtil.convertObjToStr(getCbmPromotionDesigValue().getKeyForSelected()));
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDt(curDate);
        }else{
            //            To add into PromotionTo in Edit Mode
            obj = new PromotionTO();
            obj = (PromotionTO)PromotionTOs.get(rowNo);
            obj.setTxtPromotionEmployeeName(getTxtPromotionEmployeeName());
            obj.setLastDesignation(getTxtPromotionDesignation());
            obj.setEffectiveDate(DateUtil.getDateMMDDYYYY(getTdtPromotionEffectiveDateValue()));
            obj.setCreatedDate(DateUtil.getDateMMDDYYYY(getTdtPromotionCreatedDateValue()));
            //            obj.setEmployeeStage(getTxtPromotionLastDesg());
            obj.setTxtPromotionEmpBranch(getTxtPromotionEmpBranch());
            obj.setPromotionID(getPromotionID());
            obj.setPresentBasic(getTxtPromotionBasicPayValue());
            //            obj.setPromotionStatus(CommonUtil.convertObjToStr(getCbmPromotionDesigValue().getKeyForSelected()));
            obj.setStatusBy(TrueTransactMain.USER_ID);
            obj.setStatusDt(curDate);
            ArrayList irRow = setRowPromotion(obj);
            PromotionTOs.set(rowNo,obj);
            tbmPromotion.removeRow(rowNo);
            tbmPromotion.insertRow(rowNo,irRow);
            tbmPromotion.fireTableDataChanged();
        }
        return obj;
    }
    
    private ArrayList setRowPromotion(PromotionTO obj){
        ArrayList row = new ArrayList();
        row.add(obj.getEmpId());
        row.add(obj.getTxtPromotionEmployeeName());
        row.add(obj.getEffectiveDate());
        //        row.add(obj.getEmployeeStage());
        ////        row.add(obj.getPromotionStatus());
        return row;
    }
    
    public void populateIncrementValue(int rowNum) {
        if(IncrementTOs!=null && IncrementTOs.size()>0){
            IncrementTO obj = (IncrementTO)IncrementTOs.get(rowNum);
            System.out.println("obj.getEmpId()@#@#"+obj.getEmpId());
            setTxtEmployeeId(obj.getEmpId());
            setTxtBasicSalary(obj.getTxtBasicSalary());
            setTxtIncrementDesignation(obj.getTxtIncrementDesignation());
            setIncrementID(obj.getIncrementID());
            setTxtIncrementEmpName(obj.getTxtIncrementEmpName());
            setTdtIncrementEffectiveDateValue(CommonUtil.convertObjToStr(obj.getEffectiveDate()));
            setTdtIncrementCreatedDateValue(CommonUtil.convertObjToStr(obj.getCreatedDate()));
            setTxtEmployeeGrade(CommonUtil.convertObjToStr(obj.getEmployeeStage()));
            setTxtIncrementAmount(CommonUtil.convertObjToStr(obj.getTxtIncrementAmount()));
            setTxtNewBasic(CommonUtil.convertObjToStr(obj.getTxtNewBasic()));
            setTxtIncrementNo(CommonUtil.convertObjToStr(obj.getTxtIncrementNo()));
        }
    }
    private void setTablePromotionData(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        PromotionTO obj ;
        int size = PromotionTOs.size();
        for(int i=0;i<size;i++){
            obj = (PromotionTO)PromotionTOs.get(i);
            obj.setStatus(CommonConstants.STATUS_MODIFIED);
            HashMap eachEmpMap = new HashMap();
            eachEmpMap.put("EMPLOYEE_ID",obj.getEmpId());
            List lst = ClientUtil.executeQuery("getSelectCustandLastIncDate", eachEmpMap);
            if(lst!=null && lst.size()>0){
                eachEmpMap = (HashMap)lst.get(0);
                setTxtPromotionEmployeeName(CommonUtil.convertObjToStr(eachEmpMap.get("Customer")));
                setTxtPromotionEmpBranch(CommonUtil.convertObjToStr(eachEmpMap.get("BRANCH_CODE")));
                setTdtIncrementDate(CommonUtil.convertObjToStr(eachEmpMap.get("LAST_INCREMENT_DATE")));
            }
            row = setRowPromotion(obj);
            rows.add(row);
        }
        tbmPromotion.setData(rows);
        tbmPromotion.fireTableDataChanged();
    }
    
    public void populatePromotion(int rowNum) {
        PromotionTO obj = (PromotionTO)PromotionTOs.get(rowNum);
        HashMap custMap = new HashMap();
        System.out.println("obj.getEmpId()"+obj.getEmpId());
        custMap.put("EMPLOYEE_CODE",obj.getEmpId());
        List lst = ClientUtil.executeQuery("getSelectEmployeeDetails", custMap);
        if(lst!=null && lst.size()>0){
            custMap = (HashMap)lst.get(0);
            setTxtPromotionDesignation(CommonUtil.convertObjToStr(custMap.get("DESIG_ID")));
        }
        setTxtPromotionEmployeeId(obj.getEmpId());
        //        setTxtPromotionLastDesg(obj.getEmployeeStage());
        setTxtPromotionEmployeeName(obj.getTxtPromotionEmployeeName());
        setTxtPromotionEmpBranch(obj.getTxtPromotionEmpBranch());
        setPromotionID(obj.getPromotionID());
        setTdtPromotionEffectiveDateValue(CommonUtil.convertObjToStr(obj.getEffectiveDate()));
        setTdtPromotionCreatedDateValue(CommonUtil.convertObjToStr(obj.getCreatedDate()));
        setTxtPromotionBasicPayValue(obj.getPresentBasic());
        //        getCbmPromotionDesigValue().setKeyForSelected(obj.getPromotionStatus());
        //        setCboPromotionDesigValue(obj.getPromotionStatus());
    }
    
    public void ArrearCalculations(String SalaryStatus,String basedOn){
        try{
            HashMap ArrearCalcMap = new HashMap();
            HashMap objHashMap=null;
            SalaryMasterStatus = SalaryStatus;
            SalaryCalcList = new ArrayList();
            ArrearCalcMap.put("SALARY_ID",getTxtSalaryID());
            ArrearCalcMap.put("STATUS",SalaryStatus);
            ArrearCalcMap.put("BASED_ON",basedOn);
            ArrearCalcMap.put("FROM_EMPLOYEE_CODE",getTxtFromEmpIdValue());
            ArrearCalcMap.put("TO_EMPLOYEE_CODE",getTxtToEmpIdValue());
            ArrearCalcMap.put("BRANCH_CODE",getCboBranchwiseValue());
            ArrearCalcMap.put("REGIONAL_CODE",getCboRegionalValue());
            ArrearCalcMap.put("FROM_MONTH",getTxtSalFromDateMMValue());
            ArrearCalcMap.put("FROM_YEAR",getTxtSalFromDateYYYYValue());
            ArrearCalcMap.put("TO_MONTH",getTxtSalToDateMMValue());
            ArrearCalcMap.put("TO_YEAR",getTxtSalToDateYYYYValue());
            SalaryCalcList.add(ArrearCalcMap);
            pan = ARREARS;
            if(SalaryMasterStatus.equals("TRIAL") || SalaryMasterStatus.equals("ENQUIRY")){
                objHashMap = new HashMap();
                objHashMap.put("SalaryCalcList",SalaryCalcList);
                System.out.println("#$@#$@#$SalaryCalcList"+SalaryCalcList);
                HashMap salaryViewMap = proxy.executeQuery(objHashMap,map);
                System.out.println("#@$@#$@#$salaryViewMap:"+salaryViewMap);
                resetSalaryMasterTable();
                resetSalaryMasterDetailsTable();
                if(salaryViewMap != null && salaryViewMap.containsKey("SALARY_DETAILS")){
                    empSalDetailsMap = new HashMap();
                    Date salaryForDate = (Date) curDate.clone();
                    
                    if(ArrearCalcMap.containsKey("FROM_MONTH") && ArrearCalcMap.containsKey("FROM_YEAR")){
                        int month = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_MONTH"));
                        int year = CommonUtil.convertObjToInt(ArrearCalcMap.get("FROM_YEAR"));
                        salaryForDate.setDate(1);
                        salaryForDate.setMonth(month-1);
                        salaryForDate.setYear(year-1900);
                        salaryForDate = setProperDtFormat(salaryForDate);
                    }
                    salaryViewMap.put("SALARY_DT",salaryForDate);
                    populateSalaryDetails(salaryViewMap, SalaryMasterStatus);
                    System.out.println("#$%#$%#$empSalDetailsMap:"+empSalDetailsMap);
                }
                else{
                    ClientUtil.showMessageWindow("No Records found!!!");
                }
            }
            else if(SalaryMasterStatus.equals("TRIAL_PROCESS") || SalaryMasterStatus.equals("FINAL")){
                doAction(pan);
            }
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        
    }
    private Date setProperDtFormat(Date dt){
        Date tempDt=(Date)curDate.clone();
        if(dt!=null){
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    public void deletePromotionData(int rowNum) {
        PromotionTO obj = (PromotionTO)PromotionTOs.get(rowNum);
        obj.setStatus(CommonConstants.STATUS_DELETED);
        deletePromotionList.add(obj);
        PromotionTOs.remove(rowNum);
        tbmPromotion.removeRow(rowNum);
        tbmPromotion.fireTableDataChanged();
        obj = null;
    }
    
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
     * Getter for property cbmPromotionDesigValue.
     * @return Value of property cbmPromotionDesigValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPromotionDesigValue() {
        return cbmPromotionDesigValue;
    }
    
    /**
     * Setter for property cbmPromotionDesigValue.
     * @param cbmPromotionDesigValue New value of property cbmPromotionDesigValue.
     */
    public void setCbmPromotionDesigValue(com.see.truetransact.clientutil.ComboBoxModel cbmPromotionDesigValue) {
        this.cbmPromotionDesigValue = cbmPromotionDesigValue;
    }
    
    /**
     * Getter for property cbmIncrementTypeValue.
     * @return Value of property cbmIncrementTypeValue.
     */
    //    public com.see.truetransact.clientutil.ComboBoxModel getCbmIncrementTypeValue() {
    //        return cbmIncrementTypeValue;
    //    }
    
    /**
     * Setter for property cbmIncrementTypeValue.
     * @param cbmIncrementTypeValue New value of property cbmIncrementTypeValue.
     * //     */
    //    public void setCbmIncrementTypeValue(com.see.truetransact.clientutil.ComboBoxModel cbmIncrementTypeValue) {
    //        this.cbmIncrementTypeValue = cbmIncrementTypeValue;
    //    }
    //
    /**
     * Getter for property tbmIncrement.
     * @return Value of property tbmIncrement.
     */
    public com.see.truetransact.clientutil.TableModel getTbmIncrement() {
        return tbmIncrement;
    }
    
    /**
     * Setter for property tbmIncrement.
     * @param tbmIncrement New value of property tbmIncrement.
     */
    public void setTbmIncrement(com.see.truetransact.clientutil.TableModel tbmIncrement) {
        this.tbmIncrement = tbmIncrement;
    }
    
    /**
     * Getter for property tbmPromotion.
     * @return Value of property tbmPromotion.
     */
    public com.see.truetransact.clientutil.TableModel getTbmPromotion() {
        return tbmPromotion;
    }
    
    /**
     * Setter for property tbmPromotion.
     * @param tbmPromotion New value of property tbmPromotion.
     */
    public void setTbmPromotion(com.see.truetransact.clientutil.TableModel tbmPromotion) {
        this.tbmPromotion = tbmPromotion;
    }
    
    /**
     * Getter for property cboPromotionDesigValue.
     * @return Value of property cboPromotionDesigValue.
     */
    public java.lang.String getCboPromotionDesigValue() {
        return cboPromotionDesigValue;
    }
    
    /**
     * Setter for property cboPromotionDesigValue.
     * @param cboPromotionDesigValue New value of property cboPromotionDesigValue.
     */
    public void setCboPromotionDesigValue(java.lang.String cboPromotionDesigValue) {
        this.cboPromotionDesigValue = cboPromotionDesigValue;
    }
    
    /**
     * Getter for property cboIncrementTypeValue.
     * @return Value of property cboIncrementTypeValue.
     * //     */
    //    public java.lang.String getCboIncrementTypeValue() {
    //        return cboIncrementTypeValue;
    //    }
    //
    /**
     * Setter for property cboIncrementTypeValue.
     * @param cboIncrementTypeValue New value of property cboIncrementTypeValue.
     */
    //    public void setCboIncrementTypeValue(java.lang.String cboIncrementTypeValue) {
    //        this.cboIncrementTypeValue = cboIncrementTypeValue;
    //    }
    
    /**
     * Getter for property lblIncrementSLNOValue.
     * @return Value of property lblIncrementSLNOValue.
     */
    public java.lang.String getLblIncrementSLNOValue() {
        return lblIncrementSLNOValue;
    }
    
    /**
     * Setter for property lblIncrementSLNOValue.
     * @param lblIncrementSLNOValue New value of property lblIncrementSLNOValue.
     */
    public void setLblIncrementSLNOValue(java.lang.String lblIncrementSLNOValue) {
        this.lblIncrementSLNOValue = lblIncrementSLNOValue;
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
     * Getter for property lblIncrementEmpNameValue.
     * @return Value of property lblIncrementEmpNameValue.
     */
    //    public java.lang.String getLblIncrementEmpNameValue() {
    //        return lblIncrementEmpNameValue;
    //    }
    //
    /**
     * Setter for property lblIncrementEmpNameValue.
     * @param lblIncrementEmpNameValue New value of property lblIncrementEmpNameValue.
     */
    //    public void setLblIncrementEmpNameValue(java.lang.String lblIncrementEmpNameValue) {
    //        this.lblIncrementEmpNameValue = lblIncrementEmpNameValue;
    //    }
    
    /**
     * Getter for property lblIncrementDesignationValue.
     * @return Value of property lblIncrementDesignationValue.
     */
    //    public java.lang.String getLblIncrementDesignationValue() {
    //        return lblIncrementDesignationValue;
    //    }
    
    /**
     * Setter for property lblIncrementDesignationValue.
     * @param lblIncrementDesignationValue New value of property lblIncrementDesignationValue.
     */
    //    public void setLblIncrementDesignationValue(java.lang.String lblIncrementDesignationValue) {
    //        this.lblIncrementDesignationValue = lblIncrementDesignationValue;
    //    }
    
    /**
     * Getter for property tdtIncrementEffectiveDateValue.
     * @return Value of property tdtIncrementEffectiveDateValue.
     */
    public java.lang.String getTdtIncrementEffectiveDateValue() {
        return tdtIncrementEffectiveDateValue;
    }
    
    /**
     * Setter for property tdtIncrementEffectiveDateValue.
     * @param tdtIncrementEffectiveDateValue New value of property tdtIncrementEffectiveDateValue.
     */
    public void setTdtIncrementEffectiveDateValue(java.lang.String tdtIncrementEffectiveDateValue) {
        this.tdtIncrementEffectiveDateValue = tdtIncrementEffectiveDateValue;
    }
    
    /**
     * Getter for property tdtIncrementCreatedDateValue.
     * @return Value of property tdtIncrementCreatedDateValue.
     */
    public java.lang.String getTdtIncrementCreatedDateValue() {
        return tdtIncrementCreatedDateValue;
    }
    
    /**
     * Setter for property tdtIncrementCreatedDateValue.
     * @param tdtIncrementCreatedDateValue New value of property tdtIncrementCreatedDateValue.
     */
    public void setTdtIncrementCreatedDateValue(java.lang.String tdtIncrementCreatedDateValue) {
        this.tdtIncrementCreatedDateValue = tdtIncrementCreatedDateValue;
    }
    
    /**
     * Getter for property lblPromotionSLNOValue.
     * @return Value of property lblPromotionSLNOValue.
     */
    public java.lang.String getLblPromotionSLNOValue() {
        return lblPromotionSLNOValue;
    }
    
    /**
     * Setter for property lblPromotionSLNOValue.
     * @param lblPromotionSLNOValue New value of property lblPromotionSLNOValue.
     */
    public void setLblPromotionSLNOValue(java.lang.String lblPromotionSLNOValue) {
        this.lblPromotionSLNOValue = lblPromotionSLNOValue;
    }
    
    /**
     * Getter for property txtPromotionEmployeeId.
     * @return Value of property txtPromotionEmployeeId.
     */
    public java.lang.String getTxtPromotionEmployeeId() {
        return txtPromotionEmployeeId;
    }
    
    /**
     * Setter for property txtPromotionEmployeeId.
     * @param txtPromotionEmployeeId New value of property txtPromotionEmployeeId.
     */
    public void setTxtPromotionEmployeeId(java.lang.String txtPromotionEmployeeId) {
        this.txtPromotionEmployeeId = txtPromotionEmployeeId;
    }
    
    
    /**
     * Getter for property tdtPromotionEffectiveDateValue.
     * @return Value of property tdtPromotionEffectiveDateValue.
     */
    public java.lang.String getTdtPromotionEffectiveDateValue() {
        return tdtPromotionEffectiveDateValue;
    }
    
    /**
     * Setter for property tdtPromotionEffectiveDateValue.
     * @param tdtPromotionEffectiveDateValue New value of property tdtPromotionEffectiveDateValue.
     */
    public void setTdtPromotionEffectiveDateValue(java.lang.String tdtPromotionEffectiveDateValue) {
        this.tdtPromotionEffectiveDateValue = tdtPromotionEffectiveDateValue;
    }
    
    /**
     * Getter for property tdtPromotionCreatedDateValue.
     * @return Value of property tdtPromotionCreatedDateValue.
     */
    public java.lang.String getTdtPromotionCreatedDateValue() {
        return tdtPromotionCreatedDateValue;
    }
    
    /**
     * Setter for property tdtPromotionCreatedDateValue.
     * @param tdtPromotionCreatedDateValue New value of property tdtPromotionCreatedDateValue.
     */
    public void setTdtPromotionCreatedDateValue(java.lang.String tdtPromotionCreatedDateValue) {
        this.tdtPromotionCreatedDateValue = tdtPromotionCreatedDateValue;
    }
    
    /**
     * Getter for property txtPromotionBasicPayValue.
     * @return Value of property txtPromotionBasicPayValue.
     */
    public java.lang.String getTxtPromotionBasicPayValue() {
        return txtPromotionBasicPayValue;
    }
    
    /**
     * Setter for property txtPromotionBasicPayValue.
     * @param txtPromotionBasicPayValue New value of property txtPromotionBasicPayValue.
     */
    public void setTxtPromotionBasicPayValue(java.lang.String txtPromotionBasicPayValue) {
        this.txtPromotionBasicPayValue = txtPromotionBasicPayValue;
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
     * Getter for property cboRegionalValue.
     * @return Value of property cboRegionalValue.
     */
    public java.lang.String getCboRegionalValue() {
        return cboRegionalValue;
    }
    
    /**
     * Setter for property cboRegionalValue.
     * @param cboRegionalValue New value of property cboRegionalValue.
     */
    public void setCboRegionalValue(java.lang.String cboRegionalValue) {
        this.cboRegionalValue = cboRegionalValue;
    }
    
    /**
     * Getter for property tdtSalFromDateValue.
     * @return Value of property tdtSalFromDateValue.
     */
    public java.lang.String getTdtSalFromDateValue() {
        return tdtSalFromDateValue;
    }
    
    /**
     * Setter for property tdtSalFromDateValue.
     * @param tdtSalFromDateValue New value of property tdtSalFromDateValue.
     */
    public void setTdtSalFromDateValue(java.lang.String tdtSalFromDateValue) {
        this.tdtSalFromDateValue = tdtSalFromDateValue;
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
     * Getter for property cbmRegionalValue.
     * @return Value of property cbmRegionalValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRegionalValue() {
        return cbmRegionalValue;
    }
    
    /**
     * Setter for property cbmRegionalValue.
     * @param cbmRegionalValue New value of property cbmRegionalValue.
     */
    public void setCbmRegionalValue(com.see.truetransact.clientutil.ComboBoxModel cbmRegionalValue) {
        this.cbmRegionalValue = cbmRegionalValue;
    }
    
    /**
     * Getter for property txtIncrementEmpName.
     * @return Value of property txtIncrementEmpName.
     */
    public java.lang.String getTxtIncrementEmpName() {
        return txtIncrementEmpName;
    }
    
    /**
     * Setter for property txtIncrementEmpName.
     * @param txtIncrementEmpName New value of property txtIncrementEmpName.
     */
    public void setTxtIncrementEmpName(java.lang.String txtIncrementEmpName) {
        this.txtIncrementEmpName = txtIncrementEmpName;
    }
    
    /**
     * Getter for property txtIncrementDesignation.
     * @return Value of property txtIncrementDesignation.
     */
    public java.lang.String getTxtIncrementDesignation() {
        return txtIncrementDesignation;
    }
    
    /**
     * Setter for property txtIncrementDesignation.
     * @param txtIncrementDesignation New value of property txtIncrementDesignation.
     */
    public void setTxtIncrementDesignation(java.lang.String txtIncrementDesignation) {
        this.txtIncrementDesignation = txtIncrementDesignation;
    }
    
    /**
     * Getter for property tdtIncrementDate.
     * @return Value of property tdtIncrementDate.
     */
    public java.lang.String getTdtIncrementDate() {
        return tdtIncrementDate;
    }
    
    /**
     * Setter for property tdtIncrementDate.
     * @param tdtIncrementDate New value of property tdtIncrementDate.
     */
    public void setTdtIncrementDate(java.lang.String tdtIncrementDate) {
        this.tdtIncrementDate = tdtIncrementDate;
    }
    
    /**
     * Getter for property txtBasicSalary.
     * @return Value of property txtBasicSalary.
     */
    public java.lang.String getTxtBasicSalary() {
        return txtBasicSalary;
    }
    
    /**
     * Setter for property txtBasicSalary.
     * @param txtBasicSalary New value of property txtBasicSalary.
     */
    public void setTxtBasicSalary(java.lang.String txtBasicSalary) {
        this.txtBasicSalary = txtBasicSalary;
    }
    
    /**
     * Getter for property txtEmployeeGrade.
     * @return Value of property txtEmployeeGrade.
     */
    public java.lang.String getTxtEmployeeGrade() {
        return txtEmployeeGrade;
    }
    
    /**
     * Setter for property txtEmployeeGrade.
     * @param txtEmployeeGrade New value of property txtEmployeeGrade.
     */
    public void setTxtEmployeeGrade(java.lang.String txtEmployeeGrade) {
        this.txtEmployeeGrade = txtEmployeeGrade;
    }
    
    /**
     * Getter for property txtIncrementSLNO.
     * @return Value of property txtIncrementSLNO.
     */
    public java.lang.String getTxtIncrementSLNO() {
        return txtIncrementSLNO;
    }
    
    /**
     * Setter for property txtIncrementSLNO.
     * @param txtIncrementSLNO New value of property txtIncrementSLNO.
     */
    public void setTxtIncrementSLNO(java.lang.String txtIncrementSLNO) {
        this.txtIncrementSLNO = txtIncrementSLNO;
    }
    
    /**
     * Getter for property txtPromotionEmployeeName.
     * @return Value of property txtPromotionEmployeeName.
     */
    public java.lang.String getTxtPromotionEmployeeName() {
        return txtPromotionEmployeeName;
    }
    
    /**
     * Setter for property txtPromotionEmployeeName.
     * @param txtPromotionEmployeeName New value of property txtPromotionEmployeeName.
     */
    public void setTxtPromotionEmployeeName(java.lang.String txtPromotionEmployeeName) {
        this.txtPromotionEmployeeName = txtPromotionEmployeeName;
    }
    
    /**
     * Getter for property txtPromotionDesignation.
     * @return Value of property txtPromotionDesignation.
     */
    public java.lang.String getTxtPromotionDesignation() {
        return txtPromotionDesignation;
    }
    
    /**
     * Setter for property txtPromotionDesignation.
     * @param txtPromotionDesignation New value of property txtPromotionDesignation.
     */
    public void setTxtPromotionDesignation(java.lang.String txtPromotionDesignation) {
        this.txtPromotionDesignation = txtPromotionDesignation;
    }
    
    /**
     * Getter for property txtPromotionEmpBranch.
     * @return Value of property txtPromotionEmpBranch.
     */
    public java.lang.String getTxtPromotionEmpBranch() {
        return txtPromotionEmpBranch;
    }
    
    /**
     * Setter for property txtPromotionEmpBranch.
     * @param txtPromotionEmpBranch New value of property txtPromotionEmpBranch.
     */
    public void setTxtPromotionEmpBranch(java.lang.String txtPromotionEmpBranch) {
        this.txtPromotionEmpBranch = txtPromotionEmpBranch;
    }
    
    /**
     * Getter for property txtPromotionLastDesg.
     * @return Value of property txtPromotionLastDesg.
     */
    public java.lang.String getTxtPromotionLastDesg() {
        return txtPromotionLastDesg;
    }
    
    /**
     * Setter for property txtPromotionLastDesg.
     * @param txtPromotionLastDesg New value of property txtPromotionLastDesg.
     */
    public void setTxtPromotionLastDesg(java.lang.String txtPromotionLastDesg) {
        this.txtPromotionLastDesg = txtPromotionLastDesg;
    }
    
    /**
     * Getter for property txtIncrementAmount.
     * @return Value of property txtIncrementAmount.
     */
    public java.lang.String getTxtIncrementAmount() {
        return txtIncrementAmount;
    }
    
    /**
     * Setter for property txtIncrementAmount.
     * @param txtIncrementAmount New value of property txtIncrementAmount.
     */
    public void setTxtIncrementAmount(java.lang.String txtIncrementAmount) {
        this.txtIncrementAmount = txtIncrementAmount;
    }
    
    /**
     * Getter for property txtNewBasic.
     * @return Value of property txtNewBasic.
     */
    public java.lang.String getTxtNewBasic() {
        return txtNewBasic;
    }
    
    /**
     * Setter for property txtNewBasic.
     * @param txtNewBasic New value of property txtNewBasic.
     */
    public void setTxtNewBasic(java.lang.String txtNewBasic) {
        this.txtNewBasic = txtNewBasic;
    }
    
    /**
     * Getter for property incrementID.
     * @return Value of property incrementID.
     */
    public java.lang.String getIncrementID() {
        return incrementID;
    }
    
    /**
     * Setter for property incrementID.
     * @param incrementID New value of property incrementID.
     */
    public void setIncrementID(java.lang.String incrementID) {
        this.incrementID = incrementID;
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
     * Getter for property promotionID.
     * @return Value of property promotionID.
     */
    public java.lang.String getPromotionID() {
        return promotionID;
    }
    
    /**
     * Setter for property promotionID.
     * @param promotionID New value of property promotionID.
     */
    public void setPromotionID(java.lang.String promotionID) {
        this.promotionID = promotionID;
    }
    
    /**
     * Getter for property txtIncrementNo.
     * @return Value of property txtIncrementNo.
     */
    public java.lang.String getTxtIncrementNo() {
        return txtIncrementNo;
    }
    
    /**
     * Setter for property txtIncrementNo.
     * @param txtIncrementNo New value of property txtIncrementNo.
     */
    public void setTxtIncrementNo(java.lang.String txtIncrementNo) {
        this.txtIncrementNo = txtIncrementNo;
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
     * Getter for property txtSalToDateMMValue.
     * @return Value of property txtSalToDateMMValue.
     */
    public java.lang.String getTxtSalToDateMMValue() {
        return txtSalToDateMMValue;
    }
    
    /**
     * Setter for property txtSalToDateMMValue.
     * @param txtSalToDateMMValue New value of property txtSalToDateMMValue.
     */
    public void setTxtSalToDateMMValue(java.lang.String txtSalToDateMMValue) {
        this.txtSalToDateMMValue = txtSalToDateMMValue;
    }
    
    /**
     * Getter for property txtSalToDateYYYYValue.
     * @return Value of property txtSalToDateYYYYValue.
     */
    public java.lang.String getTxtSalToDateYYYYValue() {
        return txtSalToDateYYYYValue;
    }
    
    /**
     * Setter for property txtSalToDateYYYYValue.
     * @param txtSalToDateYYYYValue New value of property txtSalToDateYYYYValue.
     */
    public void setTxtSalToDateYYYYValue(java.lang.String txtSalToDateYYYYValue) {
        this.txtSalToDateYYYYValue = txtSalToDateYYYYValue;
    }
    
    /**
     * Setter for property tdtSalToDateValue.
     * @param tdtSalToDateValue New value of property tdtSalToDateValue.
     */
    public void setTdtSalToDateValue(java.lang.String tdtSalToDateValue) {
        this.tdtSalToDateValue = tdtSalToDateValue;
    }
    
    /**
     * Getter for property tbmLog.
     * @return Value of property tbmLog.
     */
    public com.see.truetransact.clientutil.TableModel getTbmLog() {
        return tbmLog;
    }
    
    /**
     * Setter for property tbmLog.
     * @param tbmLog New value of property tbmLog.
     */
    public void setTbmLog(com.see.truetransact.clientutil.TableModel tbmLog) {
        this.tbmLog = tbmLog;
    }
    
    /**
     * Getter for property tblSalaryMaster.
     * @return Value of property tblSalaryMaster.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryMaster() {
        return tblSalaryMaster;
    }
    
    /**
     * Setter for property tblSalaryMaster.
     * @param tblSalaryMaster New value of property tblSalaryMaster.
     */
    public void setTblSalaryMaster(com.see.truetransact.clientutil.EnhancedTableModel tblSalaryMaster) {
        this.tblSalaryMaster = tblSalaryMaster;
    }
    
    /**
     * Getter for property txtSalaryID.
     * @return Value of property txtSalaryID.
     */
    public java.lang.String getTxtSalaryID() {
        return txtSalaryID;
    }
    
    /**
     * Setter for property txtSalaryID.
     * @param txtSalaryID New value of property txtSalaryID.
     */
    public void setTxtSalaryID(java.lang.String txtSalaryID) {
        this.txtSalaryID = txtSalaryID;
    }
    
    /**
     * Getter for property tblSalaryMasterDtails.
     * @return Value of property tblSalaryMasterDtails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryMasterDtails() {
        return tblSalaryMasterDtails;
    }
    
    /**
     * Setter for property tblSalaryMasterDtails.
     * @param tblSalaryMasterDtails New value of property tblSalaryMasterDtails.
     */
    public void setTblSalaryMasterDtails(com.see.truetransact.clientutil.EnhancedTableModel tblSalaryMasterDtails) {
        this.tblSalaryMasterDtails = tblSalaryMasterDtails;
    }
    
    /**
     * Getter for property txtTotalDeduction.
     * @return Value of property txtTotalDeduction.
     */
    public java.lang.String getTxtTotalDeduction() {
        return txtTotalDeduction;
    }
    
    /**
     * Setter for property txtTotalDeduction.
     * @param txtTotalDeduction New value of property txtTotalDeduction.
     */
    public void setTxtTotalDeduction(java.lang.String txtTotalDeduction) {
        this.txtTotalDeduction = txtTotalDeduction;
    }
    
    /**
     * Getter for property txtTotalEarnings.
     * @return Value of property txtTotalEarnings.
     */
    public java.lang.String getTxtTotalEarnings() {
        return txtTotalEarnings;
    }
    
    /**
     * Setter for property txtTotalEarnings.
     * @param txtTotalEarnings New value of property txtTotalEarnings.
     */
    public void setTxtTotalEarnings(java.lang.String txtTotalEarnings) {
        this.txtTotalEarnings = txtTotalEarnings;
    }
    
    /**
     * Getter for property txtNetSalary.
     * @return Value of property txtNetSalary.
     */
    public java.lang.String getTxtNetSalary() {
        return txtNetSalary;
    }
    
    /**
     * Setter for property txtNetSalary.
     * @param txtNetSalary New value of property txtNetSalary.
     */
    public void setTxtNetSalary(java.lang.String txtNetSalary) {
        this.txtNetSalary = txtNetSalary;
    }
    
    /**
     * Getter for property txtEmployeeName.
     * @return Value of property txtEmployeeName.
     */
    public java.lang.String getTxtEmployeeName() {
        return txtEmployeeName;
    }
    
    /**
     * Setter for property txtEmployeeName.
     * @param txtEmployeeName New value of property txtEmployeeName.
     */
    public void setTxtEmployeeName(java.lang.String txtEmployeeName) {
        this.txtEmployeeName = txtEmployeeName;
    }
    
    /**
     * Getter for property txtEmployeeDesig.
     * @return Value of property txtEmployeeDesig.
     */
    public java.lang.String getTxtEmployeeDesig() {
        return txtEmployeeDesig;
    }
    
    /**
     * Setter for property txtEmployeeDesig.
     * @param txtEmployeeDesig New value of property txtEmployeeDesig.
     */
    public void setTxtEmployeeDesig(java.lang.String txtEmployeeDesig) {
        this.txtEmployeeDesig = txtEmployeeDesig;
    }
    
}
