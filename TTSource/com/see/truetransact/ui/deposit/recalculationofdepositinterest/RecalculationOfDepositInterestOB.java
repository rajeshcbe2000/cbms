/*

 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RecalculationOfDepositInterestOB.java
 *
 * Created on August 13, 2003, 4:32 PM
 */

package com.see.truetransact.ui.deposit.recalculationofdepositinterest;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.ui.batchprocess.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Sathiya
 */
public class RecalculationOfDepositInterestOB extends CObservable {
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private HashMap taskMap;
    final ArrayList tabTitle = new ArrayList();
    
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmProdType;
    
    /** Variables related to the task*/
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    private ThreadPool threadPool;
    private boolean taskRunning;
    
    private int exeTaskCount;
    private int exeTaskCountUI;
    private ArrayList dataList = null;
    HashMap operationMap = new HashMap();
    private ProxyFactory proxy;
    
    private EnhancedTableModel tblLog;
    // To get the Value of Column Title and Dialogue Box...
    final RecalculationOfDepositInterestRB objrecalculationOfDepositInterestRB = new RecalculationOfDepositInterestRB();
    
    private int actionType;
    private int result;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static RecalculationOfDepositInterestOB recalculationOfDepositInterestOB;
    
    static {
        try {
            recalculationOfDepositInterestOB = new RecalculationOfDepositInterestOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static RecalculationOfDepositInterestOB getInstance() {
        return recalculationOfDepositInterestOB;
    }
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SelectAllJNDI");
        operationMap.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        operationMap.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
    }

    /** Creates a new instance of RecalculationOfDepositInterestOB */
    public RecalculationOfDepositInterestOB() throws Exception {
        fillDropdown();           // To Fill all the Combo Boxes
        setTabTitle();            // To set the Title of Table
        setOperationMap();        
        try{
            proxy = ProxyFactory.createProxy();                     
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        System.out.println("After setTabTitle");
        System.out.println("tabTitle: " + tabTitle);
        tblLog = new EnhancedTableModel(null, tabTitle);
        
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        exeTaskCount = 0;
    }
    
    // To set the Column title in Table...
    private void setTabTitle() throws Exception{
        
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn1"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn2"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn3"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn4"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn5"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn6"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn7"));
        tabTitle.add(objrecalculationOfDepositInterestRB.getString("tblColumn8"));
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
//        cbmProdType = new ComboBoxModel(key,value);
        
        // To obtain the Product id from the Table "OP_AC_PRODUCT"...
        // here "getAccProduct" is the mapped Statement name, defined in InwardClearingMap...
        lookUpHash.put(CommonConstants.MAP_NAME,"deposit_getProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    ComboBoxModel getCbmProductId(){
        return cbmProductId;
    }
    
    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblLog(EnhancedTableModel tblLog){
        this.tblLog = tblLog;
        setChanged();
    }
    
    EnhancedTableModel getTblLog(){
        return this.tblLog;
    }
    
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
//    public void resetStatus(){
//        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
//    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
//        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
//        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    private String cboProductId = "";
    private String txtFromAccount = "";
    private String tdtFromDate = "";
    private boolean chkFolioCharges = false;
    private boolean chkInoperativeCharges = false;
    private boolean chkChequeBookIssueCharges = false;
    private boolean chkStopPaymentCharges = false;
    private boolean chkStatementCharges = false;
    private boolean chkExcessTransactionCharges = false;
    private boolean chkChequeReturnCharges = false;
    private boolean chkMiscCharges = false;
    private boolean chkNonMaintenanceOFMinBalCharges = false;
    private String txtToAccount = "";
    private String tdtToDate = "";
    private String cboProdType ="";
    
    // Setter method for cboProductId
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId
    String getCboProductId(){
        return this.cboProductId;
    }
    
    // Setter method for cboFromAccount
    void setTxtFromAccount(String txtFromAccount){
        this.txtFromAccount = txtFromAccount;
        setChanged();
    }
    // Getter method for cboFromAccount
    String getTxtFromAccount(){
        return this.txtFromAccount;
    }
    
    // Setter method for tdtFromDate
    void setTdtFromDate(String tdtFromDate){
        this.tdtFromDate = tdtFromDate;
        setChanged();
    }
    // Getter method for tdtFromDate
    String getTdtFromDate(){
        return this.tdtFromDate;
    }
    
    // Setter method for chkFolioCharges
    void setChkFolioCharges(boolean chkFolioCharges){
        this.chkFolioCharges = chkFolioCharges;
        setChanged();
    }
    // Getter method for chkFolioCharges
    boolean getChkFolioCharges(){
        return this.chkFolioCharges;
    }
    
    // Setter method for chkInoperativeCharges
    void setChkInoperativeCharges(boolean chkInoperativeCharges){
        this.chkInoperativeCharges = chkInoperativeCharges;
        setChanged();
    }
    // Getter method for chkInoperativeCharges
    boolean getChkInoperativeCharges(){
        return this.chkInoperativeCharges;
    }
    
    // Setter method for chkChequeBookIssueCharges
    void setChkChequeBookIssueCharges(boolean chkChequeBookIssueCharges){
        this.chkChequeBookIssueCharges = chkChequeBookIssueCharges;
        setChanged();
    }
    // Getter method for chkChequeBookIssueCharges
    boolean getChkChequeBookIssueCharges(){
        return this.chkChequeBookIssueCharges;
    }
    
    // Setter method for chkStopPaymentCharges
    void setChkStopPaymentCharges(boolean chkStopPaymentCharges){
        this.chkStopPaymentCharges = chkStopPaymentCharges;
        setChanged();
    }
    // Getter method for chkStopPaymentCharges
    boolean getChkStopPaymentCharges(){
        return this.chkStopPaymentCharges;
    }
    
    // Setter method for chkStatementCharges
    void setChkStatementCharges(boolean chkStatementCharges){
        this.chkStatementCharges = chkStatementCharges;
        setChanged();
    }
    // Getter method for chkStatementCharges
    boolean getChkStatementCharges(){
        return this.chkStatementCharges;
    }
    
    // Setter method for chkExcessTransactionCharges
    void setChkExcessTransactionCharges(boolean chkExcessTransactionCharges){
        this.chkExcessTransactionCharges = chkExcessTransactionCharges;
        setChanged();
    }
    // Getter method for chkExcessTransactionCharges
    boolean getChkExcessTransactionCharges(){
        return this.chkExcessTransactionCharges;
    }
    
    // Setter method for chkChequeReturnCharges
    void setChkChequeReturnCharges(boolean chkChequeReturnCharges){
        this.chkChequeReturnCharges = chkChequeReturnCharges;
        setChanged();
    }
    // Getter method for chkChequeReturnCharges
    boolean getChkChequeReturnCharges(){
        return this.chkChequeReturnCharges;
    }
    
    // Setter method for chkMiscCharges
    void setChkMiscCharges(boolean chkMiscCharges){
        this.chkMiscCharges = chkMiscCharges;
        setChanged();
    }
    // Getter method for chkMiscCharges
    boolean getChkMiscCharges(){
        return this.chkMiscCharges;
    }
    
    // Setter method for chkNonMaintenanceOFMinBalCharges
    void setChkNonMaintenanceOFMinBalCharges(boolean chkNonMaintenanceOFMinBalCharges){
        this.chkNonMaintenanceOFMinBalCharges = chkNonMaintenanceOFMinBalCharges;
        setChanged();
    }
    // Getter method for chkNonMaintenanceOFMinBalCharges
    boolean getChkNonMaintenanceOFMinBalCharges(){
        return this.chkNonMaintenanceOFMinBalCharges;
    }
    
    // Setter method for cboToAccount
    void setTxtToAccount(String txtToAccount){
        this.txtToAccount = txtToAccount;
        setChanged();
    }
    // Getter method for cboToAccount
    String getTxtToAccount(){
        return this.txtToAccount;
    }
    
    // Setter method for tdtToDate
    void setTdtToDate(String tdtToDate){
        this.tdtToDate = tdtToDate;
        setChanged();
    }
    // Getter method for tdtToDate
    String getTdtToDate(){
        return this.tdtToDate;
    }
     
    /** TO RESET THE TABLE...*/
    public void resetTable(){
        try{
            ArrayList data = tblLog.getDataArrayList();
            for(int i=data.size(); i>0; i--){
                tblLog.removeRow(i-1);
            }
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    public void setTaskStatus(TaskStatus tskStatus){
        this.tskStatus = tskStatus;
        setChanged();
    }
    
    public TaskStatus getTaskStatus(){
        return this.tskStatus;
    }
    
    public void setTaskHeader(TaskHeader tskHeader){
        this.tskHeader = tskHeader;
        setChanged();
    }
    
    public TaskHeader getTaskHeader(){
        return this.tskHeader;
    }
    
    public void setExeTaskCount(int exeTaskCount){
        this.exeTaskCount = exeTaskCount;
        setChanged();
    }
    
    public int getExeTaskCount(){
        return this.exeTaskCount;
    }
    
    public void recalculatedRecords(java.util.Date fromDt,java.util.Date toDt,String prodId,String fromNo,String toNo){
        HashMap listMap = new HashMap();
        ArrayList totdepSubNoRow = new ArrayList();
        if(!prodId.equals("") && prodId.length()>0){
            listMap.put("PROD_ID",prodId);
        }
        if(fromDt!=null){
            listMap.put("FROM_DT", fromDt);
        }
        if(toDt!=null){
            listMap.put("TO_DT", toDt);
        }
        if(!fromNo.equals("") && fromNo.length()>0){
            listMap.put("FROM_NO",fromNo);
        }
        if(!toNo.equals("") && toNo.length()>0){
            listMap.put("TO_NO",toNo);
        }
        listMap.put("BRANCH_CODE",com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("SelectRecalculatedDetails", listMap);
        if(lst!=null && lst.size()>0){
            for(int i = 0;i<lst.size();i++){
                listMap = (HashMap)lst.get(i);
                ArrayList depSubNoRow = new ArrayList();
                depSubNoRow.add(0, listMap.get("DEPOSIT_NO"));
                depSubNoRow.add(1, listMap.get("Customer Name"));
                depSubNoRow.add(2, listMap.get("CATEGORY_ID"));
                depSubNoRow.add(3, listMap.get("DEPOSIT_DT"));
                depSubNoRow.add(4, listMap.get("OLD_ROI"));
                depSubNoRow.add(5, listMap.get("NEW_ROI"));
                depSubNoRow.add(6, listMap.get("OLD_MATURITY_AMT"));
                depSubNoRow.add(7, listMap.get("NEW_MATURITY_AMT"));
                totdepSubNoRow.add(depSubNoRow);
            }
            tblLog.fireTableDataChanged();
            tblLog.setDataArrayList(totdepSubNoRow, tabTitle);
        }else{
            tblLog = new EnhancedTableModel(null, tabTitle);
            tblLog.fireTableDataChanged();
            tblLog.setDataArrayList(totdepSubNoRow, tabTitle);            
        }
    }
        
    public void resetForm(){
        setCboProductId("");
        setTxtFromAccount("");
        setTdtFromDate("");
        setChkFolioCharges(false);
        setChkInoperativeCharges(false);
        setChkChequeBookIssueCharges(false);
        setChkStopPaymentCharges(false);
        setChkStatementCharges(false);
        setChkExcessTransactionCharges(false);
        setChkChequeReturnCharges(false);
        setChkMiscCharges(false);
        setChkNonMaintenanceOFMinBalCharges(false);
        setTxtToAccount("");
        setTdtToDate("");
        
        ttNotifyObservers();
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    public void displayingRecords(java.util.Date fromDt,String prodId,String fromNo,String toNo){
        HashMap listMap = new HashMap();
        listMap.put("DEPOSIT_DT",fromDt);
        if(!prodId.equals("") && prodId.length()>0){
            listMap.put("PROD_ID",prodId);
        }
        if(!fromNo.equals("") && fromNo.length()>0){
            listMap.put("FROM_NO",fromNo);
        }
        if(!toNo.equals("") && toNo.length()>0){
            listMap.put("TO_NO",toNo);
        }
        listMap.put("BRANCH_CODE",com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("TDRecalcuationInterestList", listMap);
        ArrayList totdepSubNoRow = new ArrayList();
        if(lst!=null && lst.size()>0){
            for(int i = 0;i<lst.size();i++){
                listMap = (HashMap)lst.get(i);
                long period = 0;
                HashMap eachDepositMap = new HashMap();
                ArrayList depSubNoRow = new ArrayList();
                HashMap eachRecordMap = new HashMap();
                eachDepositMap.put("DEPOSIT_NO",listMap.get("Deposit Number"));
                List lstEach = ClientUtil.executeQuery("getSelectEachDepositPeriod", eachDepositMap);
                if(lstEach!=null && lstEach.size()>0){
                    eachDepositMap = (HashMap)lstEach.get(0);
                    long year = CommonUtil.convertObjToLong(eachDepositMap.get("DEPOSIT_PERIOD_YY"));
                    long month = CommonUtil.convertObjToLong(eachDepositMap.get("DEPOSIT_PERIOD_MM"));
                    long day = CommonUtil.convertObjToLong(eachDepositMap.get("DEPOSIT_PERIOD_DD"));
                    if(year>0){
                        year = year * 360;
                    }
                    if(month>0){
                        month = month * 30;
                    }
                    period = year + month + day;
                    eachDepositMap = null;
                    eachRecordMap.put("PRODUCT_TYPE", "TD");
                    eachRecordMap.put("PROD_ID",listMap.get("PRODID"));
                    eachRecordMap.put("CATEGORY_ID",listMap.get("Category"));
                    eachRecordMap.put("AMOUNT", CommonUtil.convertObjToDouble(listMap.get("DepositAmt")));
                    eachRecordMap.put("DEPOSIT_DT",listMap.get("Deposit Date"));
                    eachRecordMap.put("PERIOD",new Long(period));
                    List lstNew = ClientUtil.executeQuery("icm.getInterestRates", eachRecordMap);
                    if(lstNew!=null && lstNew.size()>0){
                        eachRecordMap = (HashMap)lstNew.get(0);
                        depSubNoRow.add(0, listMap.get("Deposit Number"));
                        depSubNoRow.add(1, listMap.get("Customer Name"));
                        depSubNoRow.add(2, listMap.get("Category"));
                        depSubNoRow.add(3, listMap.get("Deposit Date"));
                        depSubNoRow.add(4, listMap.get("Roi"));
                        depSubNoRow.add(5, eachRecordMap.get("ROI"));
                        depSubNoRow.add(6, listMap.get("maturityAmt"));
                        depSubNoRow.add(7, listMap.get(""));
                        eachRecordMap = null;
                        totdepSubNoRow.add(depSubNoRow);
                    }
                }
            }
            tblLog.fireTableDataChanged();
            tblLog.setDataArrayList(totdepSubNoRow, tabTitle);
        }
    }
    
    public void doAction(HashMap executeMap)throws Exception{
        try{
            operationMap.put(CommonConstants.MODULE, getModule());
            operationMap.put(CommonConstants.SCREEN, getScreen());
            operationMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            operationMap.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(executeMap, operationMap);
            setResult(getActionType());
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }        
    }
}
