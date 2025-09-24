/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceOB.java
 *
 * Created on February 18, 2009, 01:40 PM
 *
 * AUTHOR : RAJESH.S
 */


package com.see.truetransact.ui.sysadmin.branchacnomaintenance;

import com.see.truetransact.transferobject.sysadmin.branchacnomaintenance.AcNoMaintenanceTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.transferobject.sysadmin.calender.WeeklyOffTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.Date;



/**
 *
 * @author  Administrator
 */
public class AcNoMaintenanceOB extends CObservable {
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private ComboBoxModel cbmBranches;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmGroupNo; // Added by nithya on 28-10-2017 for group deposit act no creation

    private String txtLastAcNo = "";
    private String txtNextAcNo = "";
    
    private ProxyFactory proxy;
    private int actionType;
    
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList calendarHolidayTabRow;
    private HashMap operationMap;
    private EnhancedTableModel tblAcNoMaintenanceTab ;
    private static AcNoMaintenanceOB acNoMaintenanceOB;
    private final AcNoMaintenanceRB objAcNoMaintenanceRB = new AcNoMaintenanceRB();
    private final ArrayList acNoMaintenanceTabTitle = new ArrayList();
    private int option = -1;
    private int optionCancel = 0;
    private ArrayList arrayAcNoMaintenanceTOs;

    private List weekOffResultList;
    private List monthResultList;
    private ArrayList existingDate;
    private ArrayList newDate;
    private ArrayList existingData;
    private ArrayList newData;
    
    public int selectedCombo = 0;
    Date curDate = null;
    private String isGroupDeposit = "";
    
    static {
        try {
            acNoMaintenanceOB = new AcNoMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of AcNoMaintenanceOB
     * @throws Exception
     */
    private AcNoMaintenanceOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setAcNoMaintenanceTabTitle();
        setOperationMap();
        tblAcNoMaintenanceTab = new EnhancedTableModel(null, acNoMaintenanceTabTitle);
        fillDropdown();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AcNoMaintenanceJNDI");
        operationMap.put(CommonConstants.HOME, "sysadmin.branchacnomaintenance.AcNoMaintenanceHome");
        operationMap.put(CommonConstants.REMOTE, "sysadmin.branchacnomaintenance.AcNoMaintenance");
    }
    
    private void setAcNoMaintenanceTabTitle() throws Exception{
        acNoMaintenanceTabTitle.add(objAcNoMaintenanceRB.getString("tblColumn1"));
        acNoMaintenanceTabTitle.add(objAcNoMaintenanceRB.getString("tblColumn2"));
        acNoMaintenanceTabTitle.add(objAcNoMaintenanceRB.getString("tblColumn3"));
        acNoMaintenanceTabTitle.add(objAcNoMaintenanceRB.getString("tblColumn4"));
        acNoMaintenanceTabTitle.add(objAcNoMaintenanceRB.getString("tblColumn5"));
    }
    
    /** A method to get the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getAllProducts");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProdId = new ComboBoxModel(key,value);

            lookUpHash.put(CommonConstants.MAP_NAME,"getAllBranchesIDs");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmBranches = new ComboBoxModel(key,value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key = null;
        value = null;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    
    public void setResult(int statusresult) {
        result = statusresult;
        setChanged();
    }
    public int getResult(){
        return result;
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return actionType;
    }
    
    public void setTblAcNoMaintenanceTab(EnhancedTableModel tblAcNoMaintenanceTab){
        this.tblAcNoMaintenanceTab = tblAcNoMaintenanceTab;
        setChanged();
    }
    public EnhancedTableModel getTblAcNoMaintenanceTab(){
        return this.tblAcNoMaintenanceTab;
    }
    
    public static AcNoMaintenanceOB getInstance() {
        return acNoMaintenanceOB;
    }
    
    private AcNoMaintenanceTO updateAcNoMaintenanceTO(AcNoMaintenanceTO oldTO, AcNoMaintenanceTO newTO) {
        oldTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        oldTO.setBranchId(newTO.getBranchId());
        oldTO.setProdId(newTO.getProdId());
        oldTO.setGroupNo(newTO.getGroupNo());// Added by nithya on 28-10-2017 for group deposit act no creation
        oldTO.setLastAcNo(newTO.getLastAcNo());
        oldTO.setNextAcNo(newTO.getNextAcNo());
        oldTO.setStatus(CommonConstants.STATUS_MODIFIED);
        oldTO.setStatusBy(TrueTransactMain.USER_ID);
        oldTO.setStatusDt(curDate);
        return oldTO;
    }
    
    private AcNoMaintenanceTO getAcNoMaintenanceTO() {
        AcNoMaintenanceTO objAcNoMaintenanceTO = new AcNoMaintenanceTO();
        objAcNoMaintenanceTO.setBranchId(CommonUtil.convertObjToStr(getCbmBranches().getKeyForSelected()));
        objAcNoMaintenanceTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
        objAcNoMaintenanceTO.setLastAcNo(getTxtLastAcNo());
        objAcNoMaintenanceTO.setNextAcNo(getTxtNextAcNo());
        objAcNoMaintenanceTO.setStatusBy(TrueTransactMain.USER_ID);
        objAcNoMaintenanceTO.setStatusDt(curDate);
        if(isGroupDeposit.equalsIgnoreCase("Y")){
            objAcNoMaintenanceTO.setGroupNo(CommonUtil.convertObjToStr(getCbmGroupNo().getKeyForSelected())); // Added by nithya on 28-10-2017 for group deposit act no creation
        }        
        return objAcNoMaintenanceTO;
    }
    
    private ArrayList setRow(AcNoMaintenanceTO objTO) {
        ArrayList tblRow = new ArrayList();
        tblRow.add(objTO.getProdId());
        tblRow.add(CommonUtil.convertObjToStr(cbmProdId.getDataForKey(objTO.getProdId())));
        tblRow.add(objTO.getLastAcNo());
        tblRow.add(objTO.getNextAcNo());
        tblRow.add(objTO.getGroupNo());// Added by nithya on 28-10-2017 for group deposit act no creation
        return tblRow;
    }
    
    /** To Insert or Update the Data entered  **/
    public void tableInsertUpdate(int rowNo){
        AcNoMaintenanceTO obj=getAcNoMaintenanceTO();
        if (arrayAcNoMaintenanceTOs==null)
            arrayAcNoMaintenanceTOs = new ArrayList();
        if(rowNo==-1){
            ArrayList tableData = tblAcNoMaintenanceTab.getDataArrayList();
            if(isGroupDeposit.equalsIgnoreCase("Y")){// Added by nithya on 28-10-2017 for group deposit act no creation
               if (checkDuplicateDataForGroupDeposit(tableData)) {
                ClientUtil.displayAlert("Duplicate data can't save");
                obj = null;
                tableData = null;
                return;
               } 
            }
            else if (checkDuplicateData(tableData)) {
                ClientUtil.displayAlert("Duplicate data can't save");
                obj = null;
                tableData = null;
                return;
            }
            obj.setCommand(CommonConstants.TOSTATUS_INSERT);
            obj.setStatus(CommonConstants.STATUS_CREATED);
            obj.setCreatedBy(TrueTransactMain.USER_ID);
            obj.setStatusDt(curDate);
            arrayAcNoMaintenanceTOs.add(obj);
            ArrayList irRow = setRow(obj);
            tblAcNoMaintenanceTab.insertRow(tblAcNoMaintenanceTab.getRowCount(), irRow);
        }else{
            obj=updateAcNoMaintenanceTO((AcNoMaintenanceTO)arrayAcNoMaintenanceTOs.get(rowNo),obj);
            ArrayList irRow = setRow(obj);
            arrayAcNoMaintenanceTOs.set(rowNo,obj);
            tblAcNoMaintenanceTab.removeRow(rowNo);
            tblAcNoMaintenanceTab.insertRow(rowNo,irRow);
        }
        tblAcNoMaintenanceTab.fireTableDataChanged();
        obj=null;
//        return 0;
    //        newDate = new ArrayList();
//        newData = new ArrayList();
//        optionCancel = 0;
//        boolean newdata = false;
//        
//        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
//            /** If it is UPDATE */
//            newdata = false;
//            setActionType(ClientConstants.ACTIONTYPE_EDIT);
//            /** New Date */
//            newDate.add(getCboBranches());
//            newDate.add(getCboProdId());
//            newData.add(getTxtLastAcNo());
//            newData.add(getTxtNextAcNo());
//
//            if(newDate.equals(existingDate)){
//                if(newData.equals(existingData)){
//                    setResult(ClientConstants.ACTIONTYPE_EDIT);
//                }else{
//                    doAction();
//                }
//                resetForm();
//                removeHolidayRow();
//                setResultStatus();
//            }else{;
//            newdata = checkDuplicateData(tableDate);
//            if(newdata){
//                doAction();
//                resetForm();
//                removeHolidayRow();
//                setResultStatus();
//            }
//            }
//        }else if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
//            /** If it is INSERT */
//            newdata = checkDuplicateData(tableDate);
//            if(newdata == true){
//                /** Insert the Data into the DataBase **/
//                setActionType(ClientConstants.ACTIONTYPE_NEW);
//                doAction();
//                resetForm();
//                removeHolidayRow();
//                setResultStatus();
//            }
//        }
    }
    
    public int doCancel(){
        return optionCancel;
    }
    
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final AcNoMaintenanceRB objAcNoMaintenanceRB = new AcNoMaintenanceRB();
                    throw new TTException(objAcNoMaintenanceRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To get the value of action performed */
    private String getCommand() throws Exception{
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
            default:
        }
        return command;
    }
    
    /** Method to Check the duplication */
    private boolean checkDuplicateData(ArrayList tableData ){
        boolean duplicatedata = false;
        int dataSize = 0;
        dataSize = tableData.size();
        String stringtableData = "";
        for(int i=0;i<dataSize;i++){
            stringtableData = ((String)((ArrayList)tableData.get(i)).get(0));
            if (stringtableData.equals(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()))){
                /** If the Date entered in the field matches the table data **/
                duplicatedata = true;
//                String[] options = {objAcNoMaintenanceRB.getString("cDialogYes"),objAcNoMaintenanceRB.getString("cDialogNo"),objAcNoMaintenanceRB.getString("cDialogCancel")};
//                option = COptionPane.showOptionDialog(null, objAcNoMaintenanceRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
//                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                break;
            }else{
                /** If the Date entered in the field doesn't matches the table data **/
                duplicatedata = false;
            }
        }
        return duplicatedata;
    }
    
     private boolean checkDuplicateDataForGroupDeposit(ArrayList tableData ){
        boolean duplicatedata = false;
        int dataSize = 0;
        dataSize = tableData.size();
        String stringtableData = "";
        String groupNo = "";
        for(int i=0;i<dataSize;i++){
            groupNo = ((String)((ArrayList)tableData.get(i)).get(4));
            stringtableData = ((String)((ArrayList)tableData.get(i)).get(0));
            if (stringtableData.equals(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected())) ){
                /** If the Date entered in the field matches the table data **/
                if(groupNo.equals(CommonUtil.convertObjToStr(getCbmGroupNo().getKeyForSelected()))){
                    duplicatedata = true;
                    break;
                }
                
//                String[] options = {objAcNoMaintenanceRB.getString("cDialogYes"),objAcNoMaintenanceRB.getString("cDialogNo"),objAcNoMaintenanceRB.getString("cDialogCancel")};
//                option = COptionPane.showOptionDialog(null, objAcNoMaintenanceRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
//                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
               
            }else{
                /** If the Date entered in the field doesn't matches the table data **/
                duplicatedata = false;
            }
        }
        return duplicatedata;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        boolean sameData = false;
        
        HashMap data = new HashMap();
        
//        if (objAcNoMaintenanceTO.getHolidayDt() != null && objAcNoMaintenanceTO.getHolidayName() != null) {
            data.put("AcNoMaintenanceTO",arrayAcNoMaintenanceTOs);
//        }
        
        System.out.println("#$#$ data before save : " + data);
        /** Check (sameData) if the the Data in the Weekly Off is same as the combo datas (Weekly Off Combo) */
//        sameData = checkWeeklyOffUpdate();
        if(!sameData){
            /** If data in Weekly Off is not same it has to INSERT / UPDATE */
//            final WeeklyOffTO objWeeklyOffTO = setWeeklyOffData();
//            objWeeklyOffTO.setCommand(getCommand());
//            if(objWeeklyOffTO != null){
//                data.put("WeeklyOffTO",objWeeklyOffTO);
//            }
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_NEW;
        data = null;
    }
    
    /** This method helps in get the CommonConstants.MAP_WHERE codition and executing the query **/
    public void populateData(HashMap whereMap) {
        try {
            if(whereMap.containsKey("HOLIDAY_ID")){
                whereMap.put(CommonConstants.MAP_WHERE, whereMap.get("HOLIDAY_ID"));
            } else {
                //--- In AutorizationMode, key is HOLIDAY ID
                whereMap.put(CommonConstants.MAP_WHERE, whereMap.get("HOLIDAY ID"));
            }
            
            whereMap.put("BRANCH ID", TrueTransactMain.BRANCH_ID);
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB();
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** This method helps in populating the data from the View All table to the respective Fileds **/
    public void populateOB() {
        HashMap inputMap = new HashMap();
        HashMap whereMap = new HashMap();
        try {
            inputMap.put(CommonConstants.BRANCH_ID, cbmBranches.getKeyForSelected());
            whereMap.put(CommonConstants.MAP_WHERE, inputMap);
            inputMap = proxy.executeQuery(whereMap, operationMap);
            if (inputMap!=null && inputMap.containsKey("AcNoMaintenanceTO")) {
                List lst = (List)inputMap.get("AcNoMaintenanceTO");
                populateTable(lst);
                lst=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
        inputMap = null;
        whereMap = null;
    }
    
    public void populateTable(List lst) {
        if (lst!=null && lst.size()>0) {
            arrayAcNoMaintenanceTOs = (ArrayList) lst;
            ArrayList dataList = new ArrayList();
            for (int i=0; i<lst.size(); i++) {
                dataList.add(setRow((AcNoMaintenanceTO)lst.get(i)));
            }
            tblAcNoMaintenanceTab.setDataArrayList(dataList, acNoMaintenanceTabTitle);
            tblAcNoMaintenanceTab.fireTableDataChanged();
            dataList = null;
        }
    }
    
    public void populateTableDetails(int rowNo) {
        AcNoMaintenanceTO objAcNoMaintenanceTO = (AcNoMaintenanceTO) arrayAcNoMaintenanceTOs.get(rowNo);
        cbmProdId.setKeyForSelected(objAcNoMaintenanceTO.getProdId());
        setTxtLastAcNo(objAcNoMaintenanceTO.getLastAcNo());
        setTxtNextAcNo(objAcNoMaintenanceTO.getNextAcNo());
        if(isGroupDeposit.equalsIgnoreCase("Y")){
            cbmGroupNo.setKeyForSelected(objAcNoMaintenanceTO.getGroupNo());// Added by nithya on 28-10-2017 for group deposit act no creation
        }       
        objAcNoMaintenanceTO = null;
        notifyObservers();
    }
    
    /** Set Table Model with the Header (Title))*/
    public void setTableModel(){
        tblAcNoMaintenanceTab = new EnhancedTableModel(null, acNoMaintenanceTabTitle);
        notifyObservers();
    }
    
    /** Method to reset Form */
    public void resetForm(){
        // To reset All Fields
        setTxtLastAcNo("");
        setTxtNextAcNo("");
        getCbmBranches().setKeyForSelected("");
        getCbmProdId().setKeyForSelected("");
//        getCbmGroupNo().setKeyForSelected("");// Added by nithya on 28-10-2017 for group deposit act no creation
        setIsGroupDeposit("");// Added by nithya on 28-10-2017 for group deposit act no creation
        setTblAcNoMaintenanceTab(new EnhancedTableModel(null, acNoMaintenanceTabTitle));
        setChanged();
        notifyObservers();
    }
    
    /** Method to reset Form */
    public void resetFields(){
        // To reset All Fields
        setTxtLastAcNo("");
        setTxtNextAcNo("");
        getCbmProdId().setKeyForSelected("");
        setChanged();
        notifyObservers();
    }
    
    /**
     * Getter for property cbmBranches.
     * @return Value of property cbmBranches.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranches() {
        return cbmBranches;
    }
    
    /**
     * Setter for property cbmBranches.
     * @param cbmBranches New value of property cbmBranches.
     */
    public void setCbmBranches(com.see.truetransact.clientutil.ComboBoxModel cbmBranches) {
        this.cbmBranches = cbmBranches;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    
    /**
     * Getter for property txtLastAcNo.
     * @return Value of property txtLastAcNo.
     */
    public java.lang.String getTxtLastAcNo() {
        return txtLastAcNo;
    }
    
    /**
     * Setter for property txtLastAcNo.
     * @param txtLastAcNo New value of property txtLastAcNo.
     */
    public void setTxtLastAcNo(java.lang.String txtLastAcNo) {
        this.txtLastAcNo = txtLastAcNo;
        setChanged();
    }
    
    /**
     * Getter for property txtNextAcNo.
     * @return Value of property txtNextAcNo.
     */
    public java.lang.String getTxtNextAcNo() {
        return txtNextAcNo;
    }
    
    /**
     * Setter for property txtNextAcNo.
     * @param txtNextAcNo New value of property txtNextAcNo.
     */
    public void setTxtNextAcNo(java.lang.String txtNextAcNo) {
        this.txtNextAcNo = txtNextAcNo;
        setChanged();
    }

    // Added by nithya on 28-10-2017 for group deposit act no creation
    public ComboBoxModel getCbmGroupNo() {
        return cbmGroupNo;
    }

    public void setCbmGroupNo(ComboBoxModel cbmGroupNo) {
        this.cbmGroupNo = cbmGroupNo;
    }

    public String getIsGroupDeposit() {
        return isGroupDeposit;
    }

    public void setIsGroupDeposit(String isGroupDeposit) {
        this.isGroupDeposit = isGroupDeposit;
    }       
    
    public boolean populateGroupDepositCombo(String prodId){
                   // For populating deposit group 
          boolean dataExists = false;
          HashMap groupData=new HashMap();
          groupData.put("PROD_ID",prodId);
          groupData.put("BRANCH_ID",CommonUtil.convertObjToStr(getCbmBranches().getKeyForSelected()));
          List groupDataList = ClientUtil.executeQuery("getAllDepositGroup", groupData);
            //system.out.println("#### mapDataList :"+mapDataList);
           key=new ArrayList();
           value=new ArrayList();
            if (groupDataList != null && groupDataList.size() > 0) {
                dataExists = true;
                for(int i=0;i< groupDataList.size();i++){
                groupData = (HashMap) groupDataList.get(i);
                String key1=CommonUtil.convertObjToStr(groupData.get("GROUP_NO"));
                String val1=CommonUtil.convertObjToStr(groupData.get("GROUP_DESC"));
                 cbmGroupNo = new ComboBoxModel(key, value);
                 if(i==0)
                     cbmGroupNo.addKeyAndElement("", "");
                     cbmGroupNo.addKeyAndElement(key1, val1);
                }
            }
           return dataExists; 
     }

}