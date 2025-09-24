/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmpSalaryStructureOB.java
 *
 * Created on Sat Feb 26 14:13:51 GMT+05:30 2011
 */

package com.see.truetransact.ui.common;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.common.EmpSalaryStructureTO;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
/**
 *
 * @author
 */

public class EmpSalaryStructureOB extends CObservable{
    private EmpSalaryStructureRB resourceBundle = new EmpSalaryStructureRB();
    private EmpSalaryStructureTO objEmpSalaryStructureTO = new EmpSalaryStructureTO();
    private ProxyFactory proxy = null;
    private boolean rdoEarnOrDed_Earning = false;
    private boolean rdoEarnOrDed_Deduction = false;
    private String txtAllowanceType = "";
    private String txtAllowanceID = "";
    private boolean rdoPercentOrFixed_Percent = false;
    private String rdoPercentOrFixed = "";
    HashMap data = new HashMap();
    HashMap get_Data = new HashMap();
    private final int ADDRTYPE_COLNO = 0;
    private String rdoEarnOrDed="";
    private String rdoBasedOnBasic="";
    private boolean salaryDetailsExists = false;
    private boolean rdoPercentOrFixed_Fixed = false;
    private String salaryDetailsID;
    private String txtToAmount = "";
    private String txtAllowanceAmount = "";
    private String txtMaxAmount = "";
    private String txtFromAmount = "";
    private String tdtSalFromDate = "";
    private String tdtSalToDate = "";
    private String slNo = "";
    private boolean zonalPresent = false;
    private boolean rdoBasedOnBasic_Yes = false;
    private boolean rdoBasedOnBasic_No = false;
    private boolean newSalaryDetails = false;
    private EnhancedTableModel tblZonal;
    private EnhancedTableModel tblSalStruct;
    private EnhancedTableModel tblBranch;
    private EnhancedTableModel tblPopulation;
    private EnhancedTableModel tblGrade;
    Date curDate = null;
    private int actionType;
    private int result;
    public boolean isTableSet = false;
    HashMap map = new HashMap();
    private LinkedHashMap deletedsalary = null;
    private ArrayList zonalTabRow;
    private ArrayList branchTabRow;
    private ArrayList gradeTabRow;
    private ArrayList populationTabRow;
    private LinkedHashMap salDetailsMap = null;
    ArrayList zonalValueList = new ArrayList();
    ArrayList branchValueList = new ArrayList();
    ArrayList gradeValueList = new ArrayList();
    ArrayList populationValueList = new ArrayList();
    ArrayList zonalTabTitle = new ArrayList();
    ArrayList salStructTabTitle = new ArrayList();
    ArrayList branchTabTitle = new ArrayList();
    ArrayList gradeTabTitle = new ArrayList();
    ArrayList populationTabTitle = new ArrayList();
    private HashMap _authorizeMap= null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger log = Logger.getLogger(EmpSalaryStructureUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle objEmpSalaryStructureRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.EmpSalaryStructureRB", ProxyParameters.LANGUAGE);
    
    //    creates a new instance of EmpSalaryStructureOB
    private static EmpSalaryStructureOB empSalaryStructureOB;
    static {
        try {
            log.info("In InwardClearingOB Declaration");
            empSalaryStructureOB = new EmpSalaryStructureOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    public static EmpSalaryStructureOB getInstance() {
        return empSalaryStructureOB;
    }
    public EmpSalaryStructureOB(){
        try{
            curDate = ClientUtil.getCurrentDate();
            initianSetup();
        }
        catch (Exception e){
            parseException.logException(e,true);
        }
    }
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        setTabTitle();    //__ To set the Title of Table in Charges Tab...
        tblZonal = new EnhancedTableModel(null, zonalTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        tblBranch = new EnhancedTableModel(null, branchTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        tblGrade = new EnhancedTableModel(null, gradeTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        tblPopulation = new EnhancedTableModel(null, populationTabTitle){
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        tblSalStruct = new EnhancedTableModel(null, salStructTabTitle);
    }
    public void resetDataList(){
        branchValueList = null;
        branchTabRow = null;
        zonalTabRow = null;
        zonalValueList = null;
        gradeTabRow = null;
        gradeValueList = null;
        populationTabRow = null;
        populationValueList = null;
        data = null;
    }
    public void setTabTitle(){
        log.info("In setChargeTabTitle...");
        zonalTabTitle.add(objEmpSalaryStructureRB.getString("tblZonal1"));
        zonalTabTitle.add(objEmpSalaryStructureRB.getString("tblZonal2"));
        branchTabTitle.add(objEmpSalaryStructureRB.getString("tblBranch1"));
        branchTabTitle.add(objEmpSalaryStructureRB.getString("tblBranch2"));
        gradeTabTitle.add(objEmpSalaryStructureRB.getString("tblGrade1"));
        gradeTabTitle.add(objEmpSalaryStructureRB.getString("tblGrade2"));
        populationTabTitle.add(objEmpSalaryStructureRB.getString("tblPopulation1"));
        populationTabTitle.add(objEmpSalaryStructureRB.getString("tblPopulation2"));
        salStructTabTitle.add(objEmpSalaryStructureRB.getString("tblSalStruct1"));
        salStructTabTitle.add(objEmpSalaryStructureRB.getString("tblSalStruct2"));
        salStructTabTitle.add(objEmpSalaryStructureRB.getString("tblSalStruct3"));
        salStructTabTitle.add(objEmpSalaryStructureRB.getString("tblSalStruct4"));
        salStructTabTitle.add(objEmpSalaryStructureRB.getString("tblSalStruct5"));
        tblZonal = new EnhancedTableModel(null, zonalTabTitle);
        tblBranch = new EnhancedTableModel(null, branchTabTitle);
        tblGrade = new EnhancedTableModel(null, gradeTabTitle);
        tblPopulation = new EnhancedTableModel(null, populationTabTitle);
        tblSalStruct = new EnhancedTableModel(null, salStructTabTitle);
        setIsTableSet(true);
    }
    public  int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("CDialogNo")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    public void deleteSalaryDetails(int row){
        if(deletedsalary == null){
            deletedsalary = new LinkedHashMap();
        }
        EmpSalaryStructureTO objEmpSalaryStructureTO= (EmpSalaryStructureTO)salDetailsMap.get(CommonUtil.convertObjToStr(tblSalStruct.getValueAt(row,ADDRTYPE_COLNO)));
        objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmpSalaryStructureTO.setStatusDt(curDate);
        objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedsalary.put(CommonUtil.convertObjToStr(tblSalStruct.getValueAt(row,ADDRTYPE_COLNO)),salDetailsMap.get(CommonUtil.convertObjToStr(tblSalStruct.getValueAt(row,ADDRTYPE_COLNO))));
        salDetailsMap.remove(tblSalStruct.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteSalary();
        
    }
    private void resetDeleteSalary(){
        try{
            resetForm();
            resetSalStructureListTable();
            populateSalaryTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void populateSalaryTable(){
        ArrayList salaryDataLst = new ArrayList();
        salaryDataLst = new ArrayList(salDetailsMap.keySet());
        ArrayList addList =new ArrayList(salDetailsMap.keySet());
        int length = salaryDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList salaryTabRow = new ArrayList();
            EmpSalaryStructureTO EmpSalaryStructureTO = (EmpSalaryStructureTO) salDetailsMap.get(addList.get(i));
            salaryTabRow = new ArrayList();
            //HAVE TO CHANGE TO SL. NUMBER
            salaryTabRow.add(CommonUtil.convertObjToStr(EmpSalaryStructureTO.getSlNo()));
            salaryTabRow.add(CommonUtil.convertObjToStr(EmpSalaryStructureTO.getFromDate()));
            salaryTabRow.add(CommonUtil.convertObjToStr(EmpSalaryStructureTO.getToDate()));
            salaryTabRow.add(CommonUtil.convertObjToStr(EmpSalaryStructureTO.getFromAmount()));
            salaryTabRow.add(CommonUtil.convertObjToStr(EmpSalaryStructureTO.getToAmount()));
            tblSalStruct.insertRow(tblSalStruct.getRowCount(),salaryTabRow);
            salaryTabRow= null;
        }
    }
    public void resetForm(){
        log.info("In resetForm()");
        setTxtAllowanceAmount("");
        setSlNo("");
        setRdoBasedOnBasic("");
        setRdoEarnOrDed("");
        setRdoPercentOrFixed("");
        setTxtAllowanceID("");
        setTxtAllowanceType("");
        setTxtFromAmount("");
        setTxtMaxAmount("");
        setTxtToAmount("");
        setTdtSalFromDate("");
        setTdtSalToDate("");
    }
    
    public HashMap doAction(){
        HashMap proxyResultMap = new HashMap();
        try{
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                        break;
                    default:
                }
                if(data == null){
                    data = new HashMap();
                }
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                if(get_authorizeMap() != null && get_authorizeMap().size() > 0){
                    data.put(CommonConstants.AUTHORIZEMAP,get_authorizeMap());
                }
                
                data.put("COMMAND",getCommand());
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                System.out.println("ASD@#$@#$@#$data "+data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
                
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    
    private void insertData() throws Exception{
        objEmpSalaryStructureTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_CREATED );
        objEmpSalaryStructureTO.setStatusDt(curDate);
        objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
        setTableData();
        setData();
        
    }
    public boolean checkZonal(){
        setTableData();
        if(zonalValueList.isEmpty()){
            zonalPresent = false;
        }
        else{
            zonalPresent = true;
        }
        zonalValueList = null;
        branchValueList = null;
        gradeValueList = null;
        populationValueList = null;
        return zonalPresent;
    }
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
    
    private void updateData() throws Exception{
        
        objEmpSalaryStructureTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
        objEmpSalaryStructureTO.setStatusDt(curDate);
        setTableData();
        setData();
    }
    private void deleteData() throws Exception{
        objEmpSalaryStructureTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_DELETED);
        objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
        objEmpSalaryStructureTO.setStatusDt(curDate);
        setTableData();
        setData();
        
    }
    private void setTableData(){
        if(zonalValueList == null){
            zonalValueList = new ArrayList();
        }
        for(int i=0;i <tblZonal.getRowCount();i++){
            if(CommonUtil.convertObjToStr(tblZonal.getValueAt(i,0)) == "true"){
                zonalValueList.add(CommonUtil.convertObjToStr(tblZonal.getValueAt(i,1)));
            }
        }
        
        if(populationValueList == null){
            populationValueList = new ArrayList();
        }
        for(int i=0;i <tblPopulation.getRowCount();i++){
            if(CommonUtil.convertObjToStr(tblPopulation.getValueAt(i,0)) == "true"){
                populationValueList.add(CommonUtil.convertObjToStr(tblPopulation.getValueAt(i,1)));
            }
        }
        if(branchValueList == null){
            branchValueList = new ArrayList();
        }
        for(int i=0;i <tblBranch.getRowCount();i++){
            if(CommonUtil.convertObjToStr(tblBranch.getValueAt(i,0)) == "true"){
                branchValueList.add(CommonUtil.convertObjToStr(tblBranch.getValueAt(i,1)));
            }
        }
        if(gradeValueList == null){
            gradeValueList = new ArrayList();
        }
        for(int i=0;i <tblGrade.getRowCount();i++){
            if(CommonUtil.convertObjToStr(tblGrade.getValueAt(i,0)) == "true"){
                gradeValueList.add(CommonUtil.convertObjToStr(tblGrade.getValueAt(i,1)));
            }
        }
        System.out.println("#$%$%$%table values :"+zonalValueList+" : "+gradeValueList+ ": "+ " : " +branchValueList+ " : "+populationValueList );
    }
    
    public void setSalaryAllowanceDetails(){
        objEmpSalaryStructureTO.setAllowanceId(getTxtAllowanceID());
        objEmpSalaryStructureTO.setAllowanceType(getTxtAllowanceType());
        objEmpSalaryStructureTO.setPercentOrFixed(getRdoPercentOrFixed());
        objEmpSalaryStructureTO.setAmtOrPerAllowance(getRdoPercentOrFixed());
        objEmpSalaryStructureTO.setUsingBasic(getRdoBasedOnBasic());
        objEmpSalaryStructureTO.setEarnOrDed(getRdoEarnOrDed());
    }
    private void setData(){
        data = new HashMap();
        setSalaryAllowanceDetails();
        data.put("SALARY_ALLOWANCE_DETAILS",objEmpSalaryStructureTO);
        if(salDetailsMap!=null && salDetailsMap.size()>0){
            data.put("SALARY_DETAILS",salDetailsMap);
        }
        if(gradeValueList!=null && gradeValueList.size()>0 ) {
            data.put("GRADE_LIST",gradeValueList);
        }
        if(branchValueList!=null && branchValueList.size()>0){
            data.put("BRANCH_LIST",branchValueList);
        }
        if(populationValueList!=null && populationValueList.size()>0){
            data.put("POPULATION_LIST",populationValueList);
        }
        if(zonalValueList!=null && zonalValueList.size()>0){
            data.put("ZONAL_LIST",zonalValueList);
        }
        System.out.println("#@$#$@#$@#zonalValueList"+zonalValueList);
        if(zonalValueList.isEmpty()){
            System.out.println("#@$#$@#$@#zona#$%#$%#$%"+zonalValueList);
            String[] obj4 = {"Yes"};
            int option3 = COptionPane.showOptionDialog(null,("Please Enter the Zonal Branch before Saving!!"), ("Deposit Renewal Option"),
            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
            zonalPresent = false;
            if(option3 == 0){
                return;
            }
        }
        else{
            zonalPresent = true;
        }
        zonalValueList = null;
        branchValueList = null;
        gradeValueList = null;
        populationValueList = null;
        if(deletedsalary!=null && deletedsalary.size()>0){
            data.put("SALARY_DETAILS_DELETED",deletedsalary);
            deletedsalary=null;
        }
        System.out.println("@#$@#$@#$data in OB :"+data);
    }
    public void salaryDetails(int row,boolean salaryDetailsFlag){
        try{
            final EmpSalaryStructureTO objEmpSalaryStructureTO=new EmpSalaryStructureTO();
            if( salDetailsMap == null ){
                salDetailsMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewSalaryDetails()){
                    objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_CREATED);
                    objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmpSalaryStructureTO.setStatusDt(curDate);
                }else{
                    objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmpSalaryStructureTO.setStatusDt(curDate);
                }
            }else{
                objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int slno;
            slno=0;
            
            if(!salaryDetailsFlag){
                
                ArrayList data = tblSalStruct.getDataArrayList();
                slno=serialNo(data,tblSalStruct);
            }
            else if(isNewSalaryDetails()){
                int b=CommonUtil.convertObjToInt(tblSalStruct.getValueAt(row,0));
                slno= b + tblSalStruct.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblSalStruct.getValueAt(row,0));
            }
            objEmpSalaryStructureTO.setSlNo(String.valueOf(slno));
            objEmpSalaryStructureTO.setAllowanceId(getTxtAllowanceID());
            objEmpSalaryStructureTO.setAllowanceType(getTxtAllowanceType());
            objEmpSalaryStructureTO.setFromAmount(getTxtFromAmount());
            objEmpSalaryStructureTO.setAmtOrPerAllowance(getTxtAllowanceAmount());
            objEmpSalaryStructureTO.setMaxAmount(getTxtMaxAmount());
            objEmpSalaryStructureTO.setToAmount(getTxtToAmount());
            
            Date salToDate = DateUtil.getDateMMDDYYYY(getTdtSalToDate());
            if(salToDate!= null){
                Date salTo = (Date)curDate.clone();
                salTo.setDate(salToDate.getDate());
                salTo.setMonth(salToDate.getMonth());
                salTo.setYear(salToDate.getYear());
                objEmpSalaryStructureTO.setToDate(salTo);
            }else{
                objEmpSalaryStructureTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtSalToDate()));
            }
            
            Date salFromDate = DateUtil.getDateMMDDYYYY(getTdtSalFromDate());
            if(salFromDate!= null){
                Date salTo = (Date)curDate.clone();
                salTo.setDate(salFromDate.getDate());
                salTo.setMonth(salFromDate.getMonth());
                salTo.setYear(salFromDate.getYear());
                objEmpSalaryStructureTO.setFromDate(salTo);
            }else{
                objEmpSalaryStructureTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtSalFromDate()));
            }
            objEmpSalaryStructureTO.setPercentOrFixed(getRdoPercentOrFixed());
            objEmpSalaryStructureTO.setUsingBasic(getRdoBasedOnBasic());
            objEmpSalaryStructureTO.setEarnOrDed(getRdoEarnOrDed());
            salDetailsMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objEmpSalaryStructureTO);
            updateTblSalDetailsList(row,objEmpSalaryStructureTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
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
    private void updateTblSalDetailsList(int row,EmpSalaryStructureTO objEmpSalaryStructureTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        for(int i = tblSalStruct.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblSalStruct.getDataArrayList().get(j)).get(0);
            if(CommonUtil.convertObjToStr(getSlNo()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList salStructRow = new ArrayList();
        if(row == -1){
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getSlNo()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getFromDate()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getToDate()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getFromAmount()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getToAmount()));
            tblSalStruct.insertRow(tblSalStruct.getRowCount(),salStructRow);
            salStructRow = null;
        }else{
            tblSalStruct.removeRow(row);
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getSlNo()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getFromDate()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getToDate()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getFromAmount()));
            salStructRow.add(CommonUtil.convertObjToStr(objEmpSalaryStructureTO.getToAmount()));
            tblSalStruct.insertRow(row,salStructRow);
            salStructRow = null;
        }
    }
    public void populateSalaryDetails(int row){
        try{
            salaryDetailsChanged(CommonUtil.convertObjToStr(tblSalStruct.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void salaryDetailsChanged(String selectedItem){
        try{
            salaryDetailsExists = true;
            final EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO)salDetailsMap.get(selectedItem);
            populateSalaryDetailsData(objEmpSalaryStructureTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void populateSalaryDetailsData(EmpSalaryStructureTO objEmpSalaryStructureTO)  throws Exception{
        try{
            if(objEmpSalaryStructureTO != null){
                setSlNo(objEmpSalaryStructureTO.getSlNo());
                setTxtAllowanceID(objEmpSalaryStructureTO.getAllowanceId());
                setTxtAllowanceType(objEmpSalaryStructureTO.getAllowanceType());
                setTxtFromAmount(objEmpSalaryStructureTO.getFromAmount());
                setTxtAllowanceAmount(objEmpSalaryStructureTO.getAmtOrPerAllowance());
                setTxtMaxAmount(objEmpSalaryStructureTO.getMaxAmount());
                setTxtToAmount(objEmpSalaryStructureTO.getToAmount());
                setTdtSalFromDate(DateUtil.getStringDate(objEmpSalaryStructureTO.getFromDate()));
                setTdtSalToDate(DateUtil.getStringDate(objEmpSalaryStructureTO.getToDate()));
//                setRdoPercentOrFixed(objEmpSalaryStructureTO.getPercentOrFixed());
//                setRdoBasedOnBasic(objEmpSalaryStructureTO.getUsingBasic());
                setRdoEarnOrDed(objEmpSalaryStructureTO.getEarnOrDed());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void  getData(HashMap whereMap){
        try{
            get_Data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#get_Data:"+get_Data);
            if(get_Data.containsKey("ALLOWANCE_DETAILS") && get_Data.get("ALLOWANCE_DETAILS")!= null){
                HashMap allowanceDetails = new HashMap();
                allowanceDetails = (HashMap) get_Data.get("ALLOWANCE_DETAILS");
                populateSalaryAllowanceDetails(allowanceDetails);
                ttNotifyObservers();
                setChanged();
            }
            if(get_Data.containsKey("GRADE_LIST")) {
                ArrayList gradeList = (ArrayList) get_Data.get("GRADE_LIST");
                populateGradeTableData(gradeList);
            }
            if(get_Data.containsKey("POPULATION_LIST")){
                ArrayList populationList = (ArrayList) get_Data.get("POPULATION_LIST");
                populatePopulationTableData(populationList);
            }
            if(get_Data.containsKey("BRANCH_LIST")){
                ArrayList branchList = (ArrayList) get_Data.get("BRANCH_LIST");
                populateBranchTableData(branchList);
            }
            if(get_Data.containsKey("ZONAL_LIST")){
                ArrayList zonalList = (ArrayList) get_Data.get("ZONAL_LIST");
                populateZonalTableData(zonalList);
            }
            
            if(get_Data.containsKey("SALARY_DETAILS")){
                salDetailsMap = (LinkedHashMap)get_Data.get("SALARY_DETAILS");
                ArrayList addList =new ArrayList(salDetailsMap.keySet());
                for(int i=0;i<addList.size();i++){
                    EmpSalaryStructureTO objEmpSalaryStructureTO = (EmpSalaryStructureTO) salDetailsMap.get(addList.get(i));
                    objEmpSalaryStructureTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objEmpSalaryStructureTO.setStatusBy(TrueTransactMain.USER_ID);
                    objEmpSalaryStructureTO.setStatusDt(curDate);
                    salDetailsMap.put(objEmpSalaryStructureTO.getSlNo(), objEmpSalaryStructureTO);
                }
                populateSalaryTable();
            }
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    private void  populateSalaryAllowanceDetails(HashMap allowanceDetails){
        setTxtAllowanceID(CommonUtil.convertObjToStr(allowanceDetails.get("ALLOWANCE_ID")));
        setTxtAllowanceType(CommonUtil.convertObjToStr(allowanceDetails.get("ALLOWANCE_TYPE")));
        setRdoEarnOrDed(CommonUtil.convertObjToStr(allowanceDetails.get("EARNING_OR_DEDUCTION")));
        setRdoBasedOnBasic(CommonUtil.convertObjToStr(allowanceDetails.get("USING_BASIC")));
        setRdoPercentOrFixed(CommonUtil.convertObjToStr(allowanceDetails.get("PERCENT_OR_FIXED")));
    }
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void resetGradeTab(){
        try{
            ArrayList data = tblGrade.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblGrade.removeRow(i-1);
            }
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    //    to reset the Population Table
    public void resetPopulationTab(){
        try{
            ArrayList data = tblPopulation.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblPopulation.removeRow(i-1);
            }
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    
    public void resetSalStructureListTable(){
        for(int i = tblSalStruct.getRowCount(); i > 0; i--){
            tblSalStruct.removeRow(0);
        }
    }
    public void resetSalStructureTab(){
        try{
            ArrayList data = tblSalStruct.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblSalStruct.removeRow(i-1);
            }
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    //    to reset the Branch Table
    public void resetBranchTab(){
        try{
            ArrayList data = tblBranch.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblBranch.removeRow(i-1);
            }
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    //    to reset the Zonal Table
    public void resetZonalTab(){
        try{
            ArrayList data = tblZonal.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblZonal.removeRow(i-1);
            }
            setChanged();
            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetInterTab():");
            parseException.logException(e,true);
        }
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        map = new HashMap();
        map.put(CommonConstants.JNDI, "EmpSalaryStructureJNDI");
        map.put(CommonConstants.HOME, "EmpSalaryStructureHome");
        map.put(CommonConstants.REMOTE, "EmpSalaryStructure");
    }
    public void getZonalData(){
        try {
            log.info("getZonalData()");
            Iterator iterator = null;
            HashMap operationMap = new HashMap();
            final List resultList = ClientUtil.executeQuery("SalaryStructure.getZonalData", null);
            int length = resultList.size();
            if(length > 0){
                for(int i =0; i<length; i++){
                    operationMap = (HashMap) resultList.get(i);
                    zonalTabRow = new ArrayList();
                    zonalTabRow.add(new Boolean(false));
                    iterator = operationMap.values().iterator();
                    while (iterator.hasNext()) {
                        zonalTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblZonal.insertRow(0,zonalTabRow);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getZonalData()");
        }
    }
    public void populateZonalTableData(ArrayList zonalList){
        ArrayList zonalTableList = tblZonal.getDataArrayList();
        final int dataSize = zonalTableList.size();
        int length = zonalList.size();
        if(length > 0){
            for(int i =0; i<length; i++){
                for(int j= 0;j<dataSize ;j++){
                    if(tblZonal.getValueAt(j, 1).equals(zonalList.get(i)) ){
                        tblZonal.setValueAt(new Boolean(true), j, 0);
                    }
                }
            }
        }
    }
    public void populateBranchTableData(ArrayList branchList){
        ArrayList branchTableList = tblBranch.getDataArrayList();
        final int dataSize = branchTableList.size();
        int length = branchList.size();
        if(length > 0){
            for(int i =0; i<length; i++){
                for(int j= 0;j<dataSize ;j++){
                    if(tblBranch.getValueAt(j, 1).equals(branchList.get(i)) ){
                        tblBranch.setValueAt(new Boolean(true), j, 0);
                    }
                }
            }
        }
    }
    public void populatePopulationTableData(ArrayList populationList){
        ArrayList populationTableList = tblPopulation.getDataArrayList();
        final int dataSize = populationTableList.size();
        int length = populationList.size();
        if(length > 0){
            for(int i =0; i<length; i++){
                for(int j= 0;j<dataSize ;j++){
                    if(tblPopulation.getValueAt(j, 1).equals(populationList.get(i)) ){
                        tblPopulation.setValueAt(new Boolean(true), j, 0);
                    }
                }
            }
        }
    }
    public void populateGradeTableData(ArrayList gradeList){
        ArrayList gradeTableList = tblGrade.getDataArrayList();
        final int dataSize = gradeTableList.size();
        int length = gradeList.size();
        if(length > 0){
            for(int i =0; i<length; i++){
                for(int j= 0;j<dataSize ;j++){
                    if(tblGrade.getValueAt(j, 1).equals(gradeList.get(i)) ){
                        tblGrade.setValueAt(new Boolean(true), j, 0);
                    }
                }
            }
        }
    }
    public void getBranchData(){
        try {
            log.info("getBranchData()");
            Iterator iterator = null;
            HashMap operationMap = new HashMap();
            final List resultList = ClientUtil.executeQuery("SalaryStructure.getBranchData", null);
            int length = resultList.size();
            if(length > 0){
                for(int i =0; i<length; i++){
                    operationMap = (HashMap) resultList.get(i);
                    branchTabRow = new ArrayList();
                    branchTabRow.add(new Boolean(false));
                    iterator = operationMap.values().iterator();
                    while (iterator.hasNext()) {
                        branchTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblBranch.insertRow(0,branchTabRow);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getBranchData()");
        }
    }
    public void getGradeData(){
        try {
            log.info("getGradeData()");
            Iterator iterator = null;
            HashMap operationMap = new HashMap();
            final List resultList = ClientUtil.executeQuery("SalaryStructure.getGradeData", null);
            int length = resultList.size();
            if(length > 0){
                for(int i =0; i<length; i++){
                    operationMap = (HashMap) resultList.get(i);
                    gradeTabRow = new ArrayList();
                    gradeTabRow.add(new Boolean(false));
                    iterator = operationMap.values().iterator();
                    while (iterator.hasNext()) {
                        gradeTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblGrade.insertRow(0,gradeTabRow);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getGradeData()");
        }
    }
    public void getPopulationData(){
        try {
            log.info("getPopulationData()");
            Iterator iterator = null;
            HashMap operationMap = new HashMap();
            final List resultList = ClientUtil.executeQuery("SalaryStructure.getPopulationData", null);
            int length = resultList.size();
            if(length > 0){
                for(int i =0; i<length; i++){
                    operationMap = (HashMap) resultList.get(i);
                    populationTabRow = new ArrayList();
                    populationTabRow.add(new Boolean(false));
                    iterator = operationMap.values().iterator();
                    while (iterator.hasNext()) {
                        populationTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblPopulation.insertRow(0,populationTabRow);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getPopulationData()");
        }
    }
    // Setter method for rdoEarnOrDed_Earning
    void setRdoEarnOrDed_Earning(boolean rdoEarnOrDed_Earning){
        this.rdoEarnOrDed_Earning = rdoEarnOrDed_Earning;
        setChanged();
    }
    // Getter method for rdoEarnOrDed_Earning
    boolean getRdoEarnOrDed_Earning(){
        return this.rdoEarnOrDed_Earning;
    }
    
    // Setter method for rdoEarnOrDed_Deduction
    void setRdoEarnOrDed_Deduction(boolean rdoEarnOrDed_Deduction){
        this.rdoEarnOrDed_Deduction = rdoEarnOrDed_Deduction;
        setChanged();
    }
    // Getter method for rdoEarnOrDed_Deduction
    boolean getRdoEarnOrDed_Deduction(){
        return this.rdoEarnOrDed_Deduction;
    }
    
    // Setter method for txtAllowanceType
    void setTxtAllowanceType(String txtAllowanceType){
        this.txtAllowanceType = txtAllowanceType;
        setChanged();
    }
    // Getter method for txtAllowanceType
    String getTxtAllowanceType(){
        return this.txtAllowanceType;
    }
    
    // Setter method for txtAllowanceID
    void setTxtAllowanceID(String txtAllowanceID){
        this.txtAllowanceID = txtAllowanceID;
        setChanged();
    }
    // Getter method for txtAllowanceID
    String getTxtAllowanceID(){
        return this.txtAllowanceID;
    }
    
    // Setter method for rdoPercentOrFixed_Percent
    void setRdoPercentOrFixed_Percent(boolean rdoPercentOrFixed_Percent){
        this.rdoPercentOrFixed_Percent = rdoPercentOrFixed_Percent;
        setChanged();
    }
    // Getter method for rdoPercentOrFixed_Percent
    boolean getRdoPercentOrFixed_Percent(){
        return this.rdoPercentOrFixed_Percent;
    }
    
    // Setter method for rdoPercentOrFixed_Fixed
    void setRdoPercentOrFixed_Fixed(boolean rdoPercentOrFixed_Fixed){
        this.rdoPercentOrFixed_Fixed = rdoPercentOrFixed_Fixed;
        setChanged();
    }
    // Getter method for rdoPercentOrFixed_Fixed
    boolean getRdoPercentOrFixed_Fixed(){
        return this.rdoPercentOrFixed_Fixed;
    }
    
    // Setter method for txtToAmount
    void setTxtToAmount(String txtToAmount){
        this.txtToAmount = txtToAmount;
        setChanged();
    }
    // Getter method for txtToAmount
    String getTxtToAmount(){
        return this.txtToAmount;
    }
    
    // Setter method for txtAllowanceAmount
    void setTxtAllowanceAmount(String txtAllowanceAmount){
        this.txtAllowanceAmount = txtAllowanceAmount;
        setChanged();
    }
    // Getter method for txtAllowanceAmount
    String getTxtAllowanceAmount(){
        return this.txtAllowanceAmount;
    }
    
    // Setter method for txtMaxAmount
    void setTxtMaxAmount(String txtMaxAmount){
        this.txtMaxAmount = txtMaxAmount;
        setChanged();
    }
    // Getter method for txtMaxAmount
    String getTxtMaxAmount(){
        return this.txtMaxAmount;
    }
    
    // Setter method for txtFromAmount
    void setTxtFromAmount(String txtFromAmount){
        this.txtFromAmount = txtFromAmount;
        setChanged();
    }
    // Getter method for txtFromAmount
    String getTxtFromAmount(){
        return this.txtFromAmount;
    }
    
    // Setter method for tdtSalFromDate
    void setTdtSalFromDate(String tdtSalFromDate){
        this.tdtSalFromDate = tdtSalFromDate;
        setChanged();
    }
    // Getter method for tdtSalFromDate
    String getTdtSalFromDate(){
        return this.tdtSalFromDate;
    }
    
    // Setter method for tdtSalToDate
    void setTdtSalToDate(String tdtSalToDate){
        this.tdtSalToDate = tdtSalToDate;
        setChanged();
    }
    // Getter method for tdtSalToDate
    String getTdtSalToDate(){
        return this.tdtSalToDate;
    }
    
    // Setter method for rdoBasedOnBasic_Yes
    void setRdoBasedOnBasic_Yes(boolean rdoBasedOnBasic_Yes){
        this.rdoBasedOnBasic_Yes = rdoBasedOnBasic_Yes;
        setChanged();
    }
    // Getter method for rdoBasedOnBasic_Yes
    boolean getRdoBasedOnBasic_Yes(){
        return this.rdoBasedOnBasic_Yes;
    }
    
    // Setter method for rdoBasedOnBasic_No
    void setRdoBasedOnBasic_No(boolean rdoBasedOnBasic_No){
        this.rdoBasedOnBasic_No = rdoBasedOnBasic_No;
        setChanged();
    }
    // Getter method for rdoBasedOnBasic_No
    boolean getRdoBasedOnBasic_No(){
        return this.rdoBasedOnBasic_No;
    }
    
    /**
     * Getter for property tblZonal.
     * @return Value of property tblZonal.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblZonal() {
        return tblZonal;
    }
    
    /**
     * Setter for property tblZonal.
     * @param tblZonal New value of property tblZonal.
     */
    public void setTblZonal(com.see.truetransact.clientutil.EnhancedTableModel tblZonal) {
        this.tblZonal = tblZonal;
    }
    
    /**
     * Getter for property tblBranch.
     * @return Value of property tblBranch.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblBranch() {
        return tblBranch;
    }
    
    /**
     * Setter for property tblBranch.
     * @param tblBranch New value of property tblBranch.
     */
    public void setTblBranch(com.see.truetransact.clientutil.EnhancedTableModel tblBranch) {
        this.tblBranch = tblBranch;
    }
    
    /**
     * Getter for property tblPopulation.
     * @return Value of property tblPopulation.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblPopulation() {
        return tblPopulation;
    }
    
    /**
     * Setter for property tblPopulation.
     * @param tblPopulation New value of property tblPopulation.
     */
    public void setTblPopulation(com.see.truetransact.clientutil.EnhancedTableModel tblPopulation) {
        this.tblPopulation = tblPopulation;
    }
    
    /**
     * Getter for property tblGrade.
     * @return Value of property tblGrade.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblGrade() {
        return tblGrade;
    }
    
    /**
     * Setter for property tblGrade.
     * @param tblGrade New value of property tblGrade.
     */
    public void setTblGrade(com.see.truetransact.clientutil.EnhancedTableModel tblGrade) {
        this.tblGrade = tblGrade;
    }
    
    /**
     * Getter for property tblSalStruct.
     * @return Value of property tblSalStruct.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalStruct() {
        return tblSalStruct;
    }
    
    /**
     * Setter for property tblSalStruct.
     * @param tblSalStruct New value of property tblSalStruct.
     */
    public void setTblSalStruct(com.see.truetransact.clientutil.EnhancedTableModel tblSalStruct) {
        this.tblSalStruct = tblSalStruct;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    /** To set the status based on ActionType, either New, Edit, etc.
     */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property isTableSet.
     * @return Value of property isTableSet.
     */
    public boolean isIsTableSet() {
        return isTableSet;
    }
    
    /**
     * Setter for property isTableSet.
     * @param isTableSet New value of property isTableSet.
     */
    public void setIsTableSet(boolean isTableSet) {
        this.isTableSet = isTableSet;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property newSalaryDetails.
     * @return Value of property newSalaryDetails.
     */
    public boolean isNewSalaryDetails() {
        return newSalaryDetails;
    }
    
    /**
     * Setter for property newSalaryDetails.
     * @param newSalaryDetails New value of property newSalaryDetails.
     */
    public void setNewSalaryDetails(boolean newSalaryDetails) {
        this.newSalaryDetails = newSalaryDetails;
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
     * Getter for property rdoEarnOrDed.
     * @return Value of property rdoEarnOrDed.
     */
    public java.lang.String getRdoEarnOrDed() {
        return rdoEarnOrDed;
    }
    
    /**
     * Setter for property rdoEarnOrDed.
     * @param rdoEarnOrDed New value of property rdoEarnOrDed.
     */
    public void setRdoEarnOrDed(java.lang.String rdoEarnOrDed) {
        this.rdoEarnOrDed = rdoEarnOrDed;
    }
    
    /**
     * Getter for property rdoBasedOnBasic.
     * @return Value of property rdoBasedOnBasic.
     */
    public java.lang.String getRdoBasedOnBasic() {
        return rdoBasedOnBasic;
    }
    
    /**
     * Setter for property rdoBasedOnBasic.
     * @param rdoBasedOnBasic New value of property rdoBasedOnBasic.
     */
    public void setRdoBasedOnBasic(java.lang.String rdoBasedOnBasic) {
        this.rdoBasedOnBasic = rdoBasedOnBasic;
    }
    
    /**
     * Getter for property slNo.
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }
    
    /**
     * Setter for property slNo.
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }
    
    /**
     * Getter for property salaryDetailsID.
     * @return Value of property salaryDetailsID.
     */
    public java.lang.String getSalaryDetailsID() {
        return salaryDetailsID;
    }
    
    /**
     * Setter for property salaryDetailsID.
     * @param salaryDetailsID New value of property salaryDetailsID.
     */
    public void setSalaryDetailsID(java.lang.String salaryDetailsID) {
        this.salaryDetailsID = salaryDetailsID;
    }
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
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
    
}