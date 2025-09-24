/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DrawingPowerMaintenanceOB.java
 *
 * Created on July 16, 2004, 10:37 AM
 */

package com.see.truetransact.ui.termloan.drawingpower;

import com.see.truetransact.ui.termloan.drawingpower.DrawingPowerMaintenanceRB;
import com.see.truetransact.transferobject.termloan.drawingpower.DrawingPowerMaintenanceTO;
import com.see.truetransact.transferobject.termloan.drawingpower.DrawingPowerMaintenanceDetailsTO;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author  Lohith R.
 */
public class DrawingPowerMaintenanceOB extends CObservable {
    
    DrawingPowerMaintenanceRB objDrawingPowerMaintenanceRB = new DrawingPowerMaintenanceRB();
    private DrawingPowerMaintenanceDetailsTO objDrawingPowerMaintenanceDetailsTO;
    private static DrawingPowerMaintenanceOB objDrawingPowerMaintenanceOB; // singleton object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private String txtParticularsofGoods = "";
    private String txtValueofOpeningStock = "";
    private String txtValueofClosingStock = "";
    private String txtPreviousDPValue = "";
    private String txtPurchase = "";
    private String txtSales = "";
    private String txtCalculatedDrawingPower = "";
    private String txtPresentStockValue = "";
    private String txtMargin = "";
    private String txtMarginAmt = "";
    private String datePreviousDPValCalOn = "";
    private String dateDueDate = "";
    private String dateStockSubmittedDate = "";
    private String dateDateofInspection = "";
    private String serialNumber = "";
    private String cboStockStmtFreq = "";
    private String cboStockStmtDay = "";
    private String cboPreviousDPMonth = "";
    private String cboCurrentDPMonth = "";
    private ComboBoxModel cbmStockStmtFreq;
    private ComboBoxModel cbmStockStmtDay;
    private ComboBoxModel cbmPreviousDPMonth;
    private ComboBoxModel cbmCurrentDPMonth;
    
    private String lblProductID = "";
    private String txtAccountNumber = "";
    private String lblAccountHead = "";
    private String lblBorrowNumber = "";
    private String lblSecurityNumber = "";
    private String lblTotalDrawingPower = "";
    private String lblDrawingPowerSanctioned = "";
    private String lblLastStockValue = "";
    private String lblDrawingPowerDateValue = "";
    private String lblCustID = "";
    private String lblCustomerName = "";
    
    private EnhancedTableModel tblDrawingPowerDetailsTab;
    private ArrayList drawingPowerDetailsTabTitle = new ArrayList();
    
    private HashMap drawinPowerDetailsHash;
    private ArrayList drawinPowerDetailsArrayList = new ArrayList();
    
    private HashMap drawinPowerHash;
    private ArrayList drawinPowerArrayList = new ArrayList();
    
    public boolean drawinPowerDetailsTabSelected = false;
    public boolean drawinPowerDetailsTabUpdate = false;
    public int drawinPowerDetailsSelectedRow = -1;
    
    private int margin = 0;
    public int marginalRangeValue = 0;
    
    private ArrayList drawinPowerDetailsTabInsertData = new ArrayList();
    private ArrayList drawinPowerDetailsTabUpdateData = new ArrayList();
    private ArrayList drawinPowerDetailsTabDeleteData = new ArrayList();
    
    private ArrayList drawinPowerDetailsArrayListTO;
    private ArrayList drawinPowerDetailsTabTO = new ArrayList();
    
    private ArrayList drawinPowerMaintenanceArrayListTO;
    private ArrayList drawinPowerMaintenanceTabTO = new ArrayList();

    private int actionType;
    private int result;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    
    private int tableSerialNumber = 0;
    private int maxTableSerialNumber = 0;
    Date curDate = null;
    public boolean authStatus = false;
    HashMap tamMaintenanceCreateMap = new HashMap();
    static {
        try {
            objDrawingPowerMaintenanceOB = new DrawingPowerMaintenanceOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of DrawingPowerMaintenanceOB */
    public DrawingPowerMaintenanceOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        setDrawingPowerDetailsTabTitle();
        tblDrawingPowerDetailsTab = new EnhancedTableModel(null, drawingPowerDetailsTabTitle);
        fillDropdown();
    }
    
    /** Creates a new instance of TAMMaintenanceCreatOB */
    public static DrawingPowerMaintenanceOB getInstance() {
        return objDrawingPowerMaintenanceOB;
    }
    
    /** Sets the HashMap required with JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "DrawingPowerMaintenanceJNDI");
        operationMap.put(CommonConstants.HOME, "termloan.drawingpower.DrawingPowerMaintenanceHome");
        operationMap.put(CommonConstants.REMOTE, "termloan.drawingpower.DrawingPowerMaintenance");
    }
    
    private void setDrawingPowerDetailsTabTitle() throws Exception{
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdSerialNum"));
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdSecurityNum"));
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdCalDrwPow"));
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdMargin"));
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdStatus"));
        drawingPowerDetailsTabTitle.add(objDrawingPowerMaintenanceRB.getString("tblHdAuthStatus"));
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        // The data to be show in Combo Box from LOOKUP_MASTER table
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("FREQUENCY");
        lookup_keys.add("CALENDAR.MONTH");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        cbmStockStmtFreq = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CALENDAR.MONTH"));
        cbmCurrentDPMonth = new ComboBoxModel(key,value);
        cbmPreviousDPMonth = new ComboBoxModel(key,value);
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for(int i=1; i<=31; i++){
            key.add(String.valueOf(i));
            value.add(String.valueOf(i));
            cbmStockStmtDay = new ComboBoxModel(key,value);
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
    
    /** Setter method for cboStockStmtFreq */
    void setCboStockStmtFreq(String cboStockStmtFreq){
        this.cboStockStmtFreq = cboStockStmtFreq;
        setChanged();
    }
    /** Getter method for cboStockStmtFreq */
    String getCboStockStmtFreq(){
        return this.cboStockStmtFreq;
    }
    
    /** Setter method for cbmStockStmtFreq */
    void setCbmStockStmtFreq(ComboBoxModel cbmStockStmtFreq){
        this.cbmStockStmtFreq = cbmStockStmtFreq;
        setChanged();
    }
    /** Getter method for cbmStockStmtFreq */
    ComboBoxModel getCbmStockStmtFreq(){
        return cbmStockStmtFreq;
    }
    
    /** Setter method for datePreviousDPValCalOn */
    void setDatePreviousDPValCalOn(String datePreviousDPValCalOn){
        this.datePreviousDPValCalOn = datePreviousDPValCalOn;
        setChanged();
    }
    /** Getter method for datePreviousDPValCalOn */
    String getDatePreviousDPValCalOn(){
        return this.datePreviousDPValCalOn;
    }
    
    /** Setter method for cboPreviousDPMonth */
    void setCboPreviousDPMonth(String cboPreviousDPMonth){
        this.cboPreviousDPMonth = cboPreviousDPMonth;
        setChanged();
    }
    /** Getter method for cboPreviousDPMonth */
    String getCboPreviousDPMonth(){
        return this.cboPreviousDPMonth;
    }
    
    /** Setter method for cbmPreviousDPMonth */
    void setCbmPreviousDPMonth(ComboBoxModel cbmPreviousDPMonth){
        this.cbmPreviousDPMonth = cbmPreviousDPMonth;
        setChanged();
    }
    /** Getter method for cbmPreviousDPMonth */
    ComboBoxModel getCbmPreviousDPMonth(){
        return cbmPreviousDPMonth;
    }
    
    /** Setter method for dateDueDate */
    void setDateDueDate(String dateDueDate){
        this.dateDueDate = dateDueDate;
        setChanged();
    }
    /** Getter method for dateDueDate */
    String getDateDueDate(){
        return this.dateDueDate;
    }
    
    /** Setter method for txtParticularsofGoods */
    void setTxtParticularsofGoods(String txtParticularsofGoods){
        this.txtParticularsofGoods = txtParticularsofGoods;
        setChanged();
    }
    /** Getter method for txtParticularsofGoods */
    String getTxtParticularsofGoods(){
        return this.txtParticularsofGoods;
    }
    
    /** Setter method for txtValueofOpeningStock */
    void setTxtValueofOpeningStock(String txtValueofOpeningStock){
        this.txtValueofOpeningStock = txtValueofOpeningStock;
        setChanged();
    }
    /** Getter method for txtValueofOpeningStock */
    String getTxtValueofOpeningStock(){
        return this.txtValueofOpeningStock;
    }
    
    /** Setter method for txtValueofClosingStock */
    void setTxtValueofClosingStock(String txtValueofClosingStock){
        this.txtValueofClosingStock = txtValueofClosingStock;
        setChanged();
    }
    /** Getter method for txtValueofClosingStock */
    String getTxtValueofClosingStock(){
        return this.txtValueofClosingStock;
    }
    
    /** Setter method for txtPreviousDPValue */
    void setTxtPreviousDPValue(String txtPreviousDPValue){
        this.txtPreviousDPValue = txtPreviousDPValue;
        setChanged();
    }
    /** Getter method for txtPreviousDPValue */
    String getTxtPreviousDPValue(){
        return this.txtPreviousDPValue;
    }
    
    /** Setter method for cboCurrentDPMonth */
    void setCboCurrentDPMonth(String cboCurrentDPMonth){
        this.cboCurrentDPMonth = cboCurrentDPMonth;
        setChanged();
    }
    /** Getter method for cboCurrentDPMonth */
    String getCboCurrentDPMonth(){
        return this.cboCurrentDPMonth;
    }
    
    /** Setter method for cbmCurrentDPMonth */
    void setCbmCurrentDPMonth(ComboBoxModel cbmCurrentDPMonth){
        this.cbmCurrentDPMonth = cbmCurrentDPMonth;
        setChanged();
    }
    /** Getter method for cbmCurrentDPMonth */
    ComboBoxModel getCbmCurrentDPMonth(){
        return cbmCurrentDPMonth;
    }
    
    /** Setter method for dateStockSubmittedDate */
    void setDateStockSubmittedDate(String dateStockSubmittedDate){
        this.dateStockSubmittedDate = dateStockSubmittedDate;
        setChanged();
    }
    /** Getter method for dateStockSubmittedDate */
    String getDateStockSubmittedDate(){
        return this.dateStockSubmittedDate;
    }
    
    /** Setter method for dateDateofInspection */
    void setDateDateofInspection(String dateDateofInspection){
        this.dateDateofInspection = dateDateofInspection;
        setChanged();
    }
    /** Getter method for dateDateofInspection */
    String getDateDateofInspection(){
        return this.dateDateofInspection;
    }
    
    /** Setter method for txtPurchase */
    void setTxtPurchase(String txtPurchase){
        this.txtPurchase = txtPurchase;
        setChanged();
    }
    /** Getter method for txtPurchase */
    String getTxtPurchase(){
        return this.txtPurchase;
    }
    
    /** Setter method for txtSales */
    void setTxtSales(String txtSales){
        this.txtSales = txtSales;
        setChanged();
    }
    /** Getter method for txtSales */
    String getTxtSales(){
        return this.txtSales;
    }
    
    /** Setter method for txtCalculatedDrawingPower */
    void setTxtCalculatedDrawingPower(String txtCalculatedDrawingPower){
        this.txtCalculatedDrawingPower = txtCalculatedDrawingPower;
        setChanged();
    }
    /** Getter method for txtCalculatedDrawingPower */
    String getTxtCalculatedDrawingPower(){
        return this.txtCalculatedDrawingPower;
    }
    
    /** Setter method for txtPresentStockValue */
    void setTxtPresentStockValue(String txtPresentStockValue){
        this.txtPresentStockValue = txtPresentStockValue;
        setChanged();
    }
    /** Getter method for txtPresentStockValue */
    String getTxtPresentStockValue(){
        return this.txtPresentStockValue;
    }
    
    /** Setter method for txtMargin */
    void setTxtMargin(String txtMargin){
        this.txtMargin = txtMargin;
        setChanged();
    }
    /** Getter method for txtMargin */
    String getTxtMargin(){
        return this.txtMargin;
    }
    
    /** Setter method for tblDrawingPowerDetailsTab */
    void setTblDrawingPowerDetailsTab(EnhancedTableModel tblDrawingPowerDetailsTab){
        this.tblDrawingPowerDetailsTab = tblDrawingPowerDetailsTab;
        setChanged();
    }
    /** Getter method for tblDrawingPowerDetailsTab */
    EnhancedTableModel getTblDrawingPowerDetailsTab(){
        return this.tblDrawingPowerDetailsTab;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
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
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    public void setResult(int result) {
        this.result = result;
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
    
    /** Setter method for serialNumber */
    void setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
        setChanged();
    }
    /** Getter method for serialNumber */
    String getSerialNumber(){
        return this.serialNumber;
    }
    
    /** Setter method for lblProductID */
    void setLblProductID(String lblProductID){
        this.lblProductID = lblProductID;
        setChanged();
    }
    /** Getter method for lblProductID */
    String getLblProductID(){
        return this.lblProductID;
    }
    
       
    /** Setter method for lblAccountHead */
    void setLblAccountHead(String lblAccountHead){
        this.lblAccountHead = lblAccountHead;
        setChanged();
    }
    /** Getter method for lblAccountHead */
    String getLblAccountHead(){
        return this.lblAccountHead;
    }
      
    /** Setter method for lblSecurityNumber */
    void setLblSecurityNumber(String lblSecurityNumber){
        this.lblSecurityNumber = lblSecurityNumber;
        setChanged();
    }
    /** Getter method for lblSecurityNumber */
    String getLblSecurityNumber(){
        return this.lblSecurityNumber;
    }
    
    /** Setter method for lblLastStockValue */
    void setLblLastStockValue(String lblLastStockValue){
        this.lblLastStockValue = lblLastStockValue;
        setChanged();
    }
    /** Getter method for lblLastStockValue */
    String getLblLastStockValue(){
        return this.lblLastStockValue;
    }
    
    /** Setter method for lblTotalDrawingPower */
    void setLblTotalDrawingPower(String lblTotalDrawingPower){
        this.lblTotalDrawingPower = lblTotalDrawingPower;
        setChanged();
    }
    /** Getter method for lblTotalDrawingPower */
    String getLblTotalDrawingPower(){
        return this.lblTotalDrawingPower;
    }
    
    /** Setter method for lblDrawingPowerSanctioned */
    void setLblDrawingPowerSanctioned(String lblDrawingPowerSanctioned){
        this.lblDrawingPowerSanctioned = lblDrawingPowerSanctioned;
        setChanged();
    }
    /** Getter method for lblDrawingPowerSanctioned */
    String getLblDrawingPowerSanctioned(){
        return this.lblDrawingPowerSanctioned;
    }
    
    /** This method gets necessary fields and accordingly this data is Inserted,
     * Updated or Deleted
     */
    public int addDrawingPowerDetailsTab(){
        int option = -1;
        try{
            int totalMargin = 0;
            int marginalRange = 100 ;
            int tableSize = tblDrawingPowerDetailsTab.getRowCount();
            ArrayList drawingPowerDetailsTabRow = new ArrayList();
            int marginalTextValue = Integer.parseInt(getTxtMargin());
            StringBuffer marginalRangeWarning = new StringBuffer();
            //notifyObservers();
            for(int i=0;i<tableSize;i++){
                // Gets the Total marginal value frm the CTable...
                if(i != drawinPowerDetailsSelectedRow ){
                    int marginalTableValue = Integer.parseInt(tblDrawingPowerDetailsTab.getValueAt((i),3).toString());
                    totalMargin = totalMargin + marginalTableValue ;
                }
            }
            marginalRange = marginalRange - totalMargin;
            marginalRangeValue = marginalRange - marginalTextValue;
//            if(marginalTextValue > 100){
//                // If the value entered in the Margin text box is greater than 100
//                option = 0;
//                marginalRangeWarning.append(objDrawingPowerMaintenanceRB.getString("marginWarning"));
//                marginalRangeWarning.append("1 - ");
//                marginalRangeWarning.append(marginalRange);
//                displayCDialogue(marginalRangeWarning.toString());
//            }else{
                // If the value entered in the Margin text box is lesser than 100
                totalMargin = totalMargin + marginalTextValue;
                option = 1;
//                if(totalMargin<=100){
                    // If total Marginal value <= 100 (Update / Insert into CTable...)
                    if(drawinPowerDetailsTabSelected == true){
                        // If Drawing Power Details Table is selected (Update --> change values in CTable..)
                        updateDrawinPowerDetailsTab(drawinPowerDetailsSelectedRow);
                        doDrawinPowerDetailsUpdateData(drawinPowerDetailsSelectedRow);
                        doDrawinPowerUpdateData(drawinPowerDetailsSelectedRow);
//                        setActionType(ClientConstants.ACTIONTYPE_EDIT);
                    }else{
                        // If Drawing Power Details Table is not selected (Insert --> add new row in CTable..)
                        int row = tblDrawingPowerDetailsTab.getRowCount();
                        tableSerialNumber = row + 1;
                        setSerialNumber(String.valueOf(tableSerialNumber));
                        setTableData(drawingPowerDetailsTabRow);
                        setDrawingPowerMaintenanceHash();
                        setDrawingPowerDetailsHash();
                        tblDrawingPowerDetailsTab.insertRow(row, drawingPowerDetailsTabRow);
                    }
//                    updateTableSerialNumber();

                    drawinPowerDetailsSelectedRow = -1;
//                }else{
//                    // If total Marginal value > 100 (Display alter Message ...)
//                    option = 0;
//                    marginalRangeWarning.append(objDrawingPowerMaintenanceRB.getString("marginWarning"));
//                    marginalRangeWarning.append("1 - ");
//                    marginalRangeWarning.append(marginalRange);
//                    displayCDialogue(marginalRangeWarning.toString());
//                }
//            }
            margin = 0;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    // Updates the CTable....
    private void updateDrawinPowerDetailsTab(int row) throws Exception{
        tblDrawingPowerDetailsTab.setValueAt(getSerialNumber(), row, 0);
        tblDrawingPowerDetailsTab.setValueAt(getTxtCalculatedDrawingPower(), row, 1);
        tblDrawingPowerDetailsTab.setValueAt(getTxtPresentStockValue(), row, 2);
        tblDrawingPowerDetailsTab.setValueAt(getTxtMargin(), row, 3);
        tblDrawingPowerDetailsTab.setValueAt(CommonConstants.STATUS_MODIFIED, row, 4);
        tblDrawingPowerDetailsTab.setValueAt(null, row, 5);
        setChanged();
        notifyObservers();
    }

    private boolean doDrawinPowerDetailsDeleteData(int row){
        boolean dataExists = false;
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap drawinPowerDetailsHash;
        ArrayList data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerDetailsArrayListSize;i++){
            drawinPowerDetailsHash = new HashMap();
            drawinPowerDetailsHash = (HashMap)drawinPowerDetailsArrayList.get(row);
            if(drawinPowerDetailsHash.containsKey("STATUS") && drawinPowerDetailsHash.get("STATUS").equals("UPDATE")){
                HashMap authMap = new HashMap();
                authMap.put("ACCT_NO",getTxtAccountNumber());
                authMap.put("SL_NO",getSerialNumber());
                List lst = ClientUtil.executeQuery("getAuthorizedRecordsDetails", authMap);
                if(lst!= null && lst.size()>0){
                    authStatus = true;
                }else{                
                    drawinPowerDetailsArrayList.remove(row);
                    drawinPowerDetailsHash.put("STATUS", "DELETE");
                    drawinPowerDetailsHash.put("BORROW_NO",getLblBorrowNumber());
                    drawinPowerDetailsHash.put("PROD_ID", getLblProductID());
                    drawinPowerDetailsHash.put("ACCT_NO", getTxtAccountNumber());
                    drawinPowerDetailsHash.put("SECURITY_NO", getLblSecurityNumber());
                    drawinPowerDetailsHash.put("SL_NO", getSerialNumber());
                    drawinPowerDetailsHash.put("LAST_STOCK_VALUE", getLblLastStockValue());
                    drawinPowerDetailsHash.put("CALC_DRAWING_POWER", getTxtCalculatedDrawingPower());
                    drawinPowerDetailsHash.put("PRESENT_STOCK_VALUE", getTxtPresentStockValue());
                    drawinPowerDetailsHash.put("MARGIN", getTxtMargin());
                    drawinPowerDetailsHash.put("MARGIN_AMT", getTxtMarginAmt());
                    drawinPowerDetailsHash.put("TOTAL_DRAWING_POWER", getLblTotalDrawingPower());
                    drawinPowerDetailsHash.put("DRAWING_POWER_SANCTIONED", getLblDrawingPowerSanctioned());
                    drawinPowerDetailsArrayList.add(row,drawinPowerDetailsHash);
                    drawinPowerDetailsHash = null;
                    deleteDrawinPowerRowTab(row);
                    break;
                }
            }else{
                drawinPowerDetailsArrayList.remove(row);
                tblDrawingPowerDetailsTab.removeRow(row);
                authStatus = false;
//                setTableData();
            }
        }
        return authStatus;            
    }
    
    private void doDrawinPowerDeletedData(int row){
        boolean dataExists = false;
        int drawinPowerArrayListSize = drawinPowerArrayList.size();
        HashMap drawinPowerHash;
        ArrayList data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerArrayListSize;i++){
            drawinPowerHash = new HashMap();
            drawinPowerHash = (HashMap)drawinPowerArrayList.get(row);
            if(drawinPowerHash.containsKey("STATUS") && drawinPowerHash.get("STATUS").equals("UPDATE")){
                if(authStatus == false){
                    drawinPowerArrayList.remove(row);
                    drawinPowerHash.put("STATUS", "DELETE");
                    drawinPowerHash.put("BORROW_NO", getLblBorrowNumber());
                    drawinPowerHash.put("PROD_ID", getLblProductID());
                    drawinPowerHash.put("ACCT_NO", getTxtAccountNumber());
                    drawinPowerHash.put("STOCK_STAT_FREQ", CommonUtil.convertObjToStr(getCbmStockStmtFreq().getKeyForSelected()));
                    drawinPowerHash.put("STOCK_STAT_DAY", CommonUtil.convertObjToStr(getCbmStockStmtDay().getKeyForSelected()));
                    drawinPowerHash.put("PREV_DP_MONTH", CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getKeyForSelected()));
                    drawinPowerHash.put("CURR_DP_MONTH", CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getKeyForSelected()));
                    drawinPowerHash.put("PREV_DPVALUE_CALCDT", getDatePreviousDPValCalOn());
                    drawinPowerHash.put("PREV_DP_VALUE", getTxtPreviousDPValue());
                    drawinPowerHash.put("DUE_DT", getDateDueDate());
                    drawinPowerHash.put("STOCK_SUBMIT_DT", getDateStockSubmittedDate());
                    drawinPowerHash.put("GOODS_PARTICULARS", getTxtParticularsofGoods());
                    drawinPowerHash.put("INSPECTION_DT", getDateDateofInspection());
                    drawinPowerHash.put("OPENING_STOCK_VALUE", getTxtValueofOpeningStock());
                    drawinPowerHash.put("PURCHASE", getTxtPurchase());
                    drawinPowerHash.put("CLOSING_STOCK_VALUE", getTxtValueofClosingStock());
                    drawinPowerHash.put("SALES", getTxtSales());
                    drawinPowerHash.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    drawinPowerHash.put("CREATED_BY", ProxyParameters.USER_ID);
                    drawinPowerHash.put("CREATED_DT", ClientUtil.getCurrentDate());
                    drawinPowerHash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            //        drawinPowerDetailsTabInsertData.add(getSerialNumber());
                    drawinPowerArrayList.add(row,drawinPowerHash);
                    drawinPowerHash = null;
                    break;
                }
            }else
                drawinPowerArrayList.remove(row);
        }
    }
    
    /** This method get the update data entered  **/
    private void doDrawinPowerDetailsUpdateData(int row){
        boolean dataExists = false;
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap drawinPowerDetailsHash;
        ArrayList data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerDetailsArrayListSize;i++){
            drawinPowerDetailsHash = new HashMap();
            drawinPowerDetailsHash = (HashMap)drawinPowerDetailsArrayList.get(row);
            if(drawinPowerDetailsHash.containsKey("STATUS"))
                drawinPowerDetailsHash.put("STATUS", drawinPowerDetailsHash.get("STATUS"));            
            drawinPowerDetailsArrayList.remove(row);
//            if(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")).equals(getSerialNumber()) ){
//                // Deletes the row element from the ArrayList
//                drawinPowerDetailsArrayList.remove(j);
//                break;
//            }
//            drawinPowerDetailsHash = null;        
        // Updates elements to the ArrayList
            drawinPowerDetailsHash.put("BORROW_NO",getLblBorrowNumber());
            drawinPowerDetailsHash.put("PROD_ID", getLblProductID());
            drawinPowerDetailsHash.put("ACCT_NO", getTxtAccountNumber());
            drawinPowerDetailsHash.put("SECURITY_NO", getLblSecurityNumber());
            drawinPowerDetailsHash.put("SL_NO", getSerialNumber());
            drawinPowerDetailsHash.put("LAST_STOCK_VALUE", getLblLastStockValue());
            drawinPowerDetailsHash.put("CALC_DRAWING_POWER", getTxtCalculatedDrawingPower());
            drawinPowerDetailsHash.put("PRESENT_STOCK_VALUE", getTxtPresentStockValue());
            drawinPowerDetailsHash.put("MARGIN", getTxtMargin());
            drawinPowerDetailsHash.put("MARGIN_AMT", getTxtMarginAmt());
            drawinPowerDetailsHash.put("TOTAL_DRAWING_POWER", getLblTotalDrawingPower());
            drawinPowerDetailsHash.put("DRAWING_POWER_SANCTIONED", getLblDrawingPowerSanctioned());
            drawinPowerDetailsArrayList.add(row,drawinPowerDetailsHash);
        }
        int serialNum =  Integer.parseInt(getSerialNumber());
        int serialNumInUpdateArrayList = 0;
        for(int i=0;i<drawinPowerDetailsTabUpdateData.size();i++){
            serialNumInUpdateArrayList = Integer.parseInt(drawinPowerDetailsTabUpdateData.get(i).toString());
            if(serialNum == serialNumInUpdateArrayList){
                // If serial num present in Drw Pow Details Tab Update Data (Dont add in drawinPowerDetailsTabUpdateData)
                dataExists = true;
                break;
            }
            
        }
        if(drawinPowerDetailsTabUpdate  == true && drawinPowerDetailsTabSelected == true && serialNum <= maxTableSerialNumber && dataExists == false){
            // If serial num is not present in Drw Pow Details Tab Update Data (Add to drawinPowerDetailsTabUpdateData)
            drawinPowerDetailsTabUpdateData.add(getSerialNumber());
        }
        drawinPowerDetailsHash = null;
    }
    
    private void doDrawinPowerUpdateData(int row){
        boolean dataExists = false;
        int drawinPowerArrayListSize = drawinPowerArrayList.size();
        HashMap drawinPowerHash;
        ArrayList data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerArrayListSize;i++){
            drawinPowerHash = new HashMap();
            drawinPowerHash = (HashMap)drawinPowerArrayList.get(row);
            drawinPowerArrayList.remove(row);

            // Updates elements to the ArrayList
            drawinPowerHash.put("BORROW_NO", getLblBorrowNumber());
            drawinPowerHash.put("PROD_ID", getLblProductID());
            drawinPowerHash.put("ACCT_NO", getTxtAccountNumber());
            drawinPowerHash.put("STOCK_STAT_FREQ", CommonUtil.convertObjToStr(getCbmStockStmtFreq().getKeyForSelected()));
            drawinPowerHash.put("STOCK_STAT_DAY", CommonUtil.convertObjToStr(getCbmStockStmtDay().getKeyForSelected()));
            drawinPowerHash.put("PREV_DP_MONTH", CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getKeyForSelected()));
            drawinPowerHash.put("CURR_DP_MONTH", CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getKeyForSelected()));
            drawinPowerHash.put("PREV_DPVALUE_CALCDT", getDatePreviousDPValCalOn());
            drawinPowerHash.put("PREV_DP_VALUE", getTxtPreviousDPValue());
            drawinPowerHash.put("DUE_DT", getDateDueDate());
            drawinPowerHash.put("STOCK_SUBMIT_DT", getDateStockSubmittedDate());
            drawinPowerHash.put("GOODS_PARTICULARS", getTxtParticularsofGoods());
            drawinPowerHash.put("INSPECTION_DT", getDateDateofInspection());
            drawinPowerHash.put("OPENING_STOCK_VALUE", getTxtValueofOpeningStock());
            drawinPowerHash.put("PURCHASE", getTxtPurchase());
            drawinPowerHash.put("CLOSING_STOCK_VALUE", getTxtValueofClosingStock());
            drawinPowerHash.put("SALES", getTxtSales());
            drawinPowerHash.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            drawinPowerHash.put("CREATED_BY", ProxyParameters.USER_ID);
            drawinPowerHash.put("CREATED_DT", ClientUtil.getCurrentDate());
            drawinPowerHash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
    //        drawinPowerDetailsTabInsertData.add(getSerialNumber());
            drawinPowerArrayList.add(row,drawinPowerHash);
            drawinPowerHash = null;        
        }
//            if(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")).equals(getSerialNumber()) ){
//                // Deletes the row element from the ArrayList
//                drawinPowerDetailsArrayList.remove(j);
//                break;
//            }
//            drawinPowerDetailsHash = null;
//        }
    }

    /** This method adds all the required data to ArrayList.... */
    private void setTableData(ArrayList drawingPowerDetailsTabRow){
        drawingPowerDetailsTabRow.add(getSerialNumber());
        drawingPowerDetailsTabRow.add(getTxtCalculatedDrawingPower());
        drawingPowerDetailsTabRow.add(getTxtPresentStockValue());
        drawingPowerDetailsTabRow.add(getTxtMargin());
        drawingPowerDetailsTabRow.add(CommonConstants.STATUS_CREATED);
        drawingPowerDetailsTabRow.add(null);
    }
    
    /** Method to set the Drawing Power Details data to HashMap */
    public void setDrawingPowerDetailsHash(){
        drawinPowerDetailsHash = new HashMap();
        drawinPowerDetailsHash.put("BORROW_NO", getLblBorrowNumber());
        drawinPowerDetailsHash.put("PROD_ID", getLblProductID());
        drawinPowerDetailsHash.put("ACCT_NO", getTxtAccountNumber());
        drawinPowerDetailsHash.put("SECURITY_NO", getLblSecurityNumber());
        drawinPowerDetailsHash.put("SL_NO", getSerialNumber());
        drawinPowerDetailsHash.put("LAST_STOCK_VALUE", getLblLastStockValue());
        drawinPowerDetailsHash.put("CALC_DRAWING_POWER", getTxtCalculatedDrawingPower());
        drawinPowerDetailsHash.put("PRESENT_STOCK_VALUE", getTxtPresentStockValue());
        drawinPowerDetailsHash.put("MARGIN", getTxtMargin());
        drawinPowerDetailsHash.put("MARGIN_AMT", getTxtMarginAmt());
        drawinPowerDetailsHash.put("TOTAL_DRAWING_POWER", getLblTotalDrawingPower());
        drawinPowerDetailsHash.put("DRAWING_POWER_SANCTIONED", getLblDrawingPowerSanctioned());
        drawinPowerDetailsTabInsertData.add(getSerialNumber());
        drawinPowerDetailsArrayList.add(drawinPowerDetailsHash);
        drawinPowerDetailsHash = null;
    }
    
    public void setDrawingPowerMaintenanceHash(){
        drawinPowerHash = new HashMap();
        drawinPowerHash.put("BORROW_NO", getLblBorrowNumber());
        drawinPowerHash.put("PROD_ID", getLblProductID());
        drawinPowerHash.put("ACCT_NO", getTxtAccountNumber());
        drawinPowerHash.put("STOCK_STAT_FREQ", CommonUtil.convertObjToStr(getCbmStockStmtFreq().getKeyForSelected()));
        drawinPowerHash.put("STOCK_STAT_DAY", CommonUtil.convertObjToStr(getCbmStockStmtDay().getKeyForSelected()));
        drawinPowerHash.put("PREV_DP_MONTH", CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getKeyForSelected()));
        drawinPowerHash.put("CURR_DP_MONTH", CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getKeyForSelected()));
        drawinPowerHash.put("PREV_DPVALUE_CALCDT", getDatePreviousDPValCalOn());
        drawinPowerHash.put("PREV_DP_VALUE", getTxtPreviousDPValue());
        drawinPowerHash.put("DUE_DT", getDateDueDate());
        drawinPowerHash.put("STOCK_SUBMIT_DT", getDateStockSubmittedDate());
        drawinPowerHash.put("GOODS_PARTICULARS", getTxtParticularsofGoods());
        drawinPowerHash.put("INSPECTION_DT", getDateDateofInspection());
        drawinPowerHash.put("OPENING_STOCK_VALUE", getTxtValueofOpeningStock());
        drawinPowerHash.put("PURCHASE", getTxtPurchase());
        drawinPowerHash.put("CLOSING_STOCK_VALUE", getTxtValueofClosingStock());
        drawinPowerHash.put("SALES", getTxtSales());
        drawinPowerHash.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        drawinPowerHash.put("CREATED_BY", ProxyParameters.USER_ID);
        drawinPowerHash.put("CREATED_DT", ClientUtil.getCurrentDate());
        drawinPowerHash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
//        drawinPowerDetailsTabInsertData.add(getSerialNumber());
        drawinPowerArrayList.add(drawinPowerHash);
        drawinPowerHash = null;
    }
    
    /** This method gets  Maximum Serial Number frm the data base.... */
    public void getQuery(){
        HashMap whereMap = new HashMap();
        setLblSecurityNumber("");
        whereMap.put("SECURITY_NO", getLblSecurityNumber());
        //__ whereMap.put("BORROW_NO", getTxtBorrowerNum());
        whereMap.put("ACCT_NO", getTxtAccountNumber());
        
        final List resultList = ClientUtil.executeQuery("Drawing_Power.getSerialNumber", whereMap);
        final HashMap resultMap = (HashMap)resultList.get(0);
        if(resultMap.get("SERIAL NUMBER") != null){
            maxTableSerialNumber = Integer.parseInt(resultMap.get("SERIAL NUMBER").toString());
        }
        tableSerialNumber = maxTableSerialNumber;
    }
    
    /** To get the Date from the Table into the UI... */
    public boolean populateDrawinPowerDetailsTab(int row){
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap drawinPowerDetailsHash= new HashMap();
        ArrayList data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerDetailsArrayListSize;i++){
            drawinPowerDetailsHash = new HashMap();
            drawinPowerDetailsHash = (HashMap)drawinPowerDetailsArrayList.get(row);
            HashMap todMap = new HashMap();
            String securityNo =CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SECURITY_NO"));
            if(!securityNo.equals("") && securityNo.length()>0){
                todMap.put("SECURITY_NO",drawinPowerDetailsHash.get("SECURITY_NO"));
                List lst = ClientUtil.executeQuery("getSelectTODCreatedDate", todMap);
                if(lst!=null && lst.size()>0){
                    todMap = (HashMap)lst.get(0);
                    setLblDrawingPowerDateValue(CommonUtil.convertObjToStr(todMap.get("FROM_DT")));
                }
                lst = null;
                todMap = null;
            }else
                setLblDrawingPowerDateValue("");                
//            if((CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")).equals(CommonUtil.convertObjToStr(data.get(0))))){
//                setLblSecurityNumber(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SECURITY_NO")));
            setLblLastStockValue(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("LAST_STOCK_VALUE")));
//setLblTotalDrawingPower(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("TOTAL_DRAWING_POWER")));
            setLblDrawingPowerSanctioned(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("DRAWING_POWER_SANCTIONED")));
            setSerialNumber(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")));
            setTxtCalculatedDrawingPower(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("CALC_DRAWING_POWER")));
            setTxtPresentStockValue(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("PRESENT_STOCK_VALUE")));
            setTxtMargin(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("MARGIN")));
            setTxtMarginAmt(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("MARGIN_AMT")));
            margin = Integer.parseInt(getTxtMargin());
            break;
        }
        int drawinPowerArrayListSize = drawinPowerArrayList.size();
        HashMap drawinPowerHash= new HashMap();
        data = (ArrayList)tblDrawingPowerDetailsTab.getDataArrayList().get(row);
        for(int i=0;i<drawinPowerArrayListSize;i++){
            drawinPowerHash = new HashMap();
            drawinPowerHash = (HashMap)drawinPowerArrayList.get(row);                
            setLblProductID(CommonUtil.convertObjToStr(drawinPowerHash.get("PROD_ID")));
            setLblBorrowNumber(CommonUtil.convertObjToStr(drawinPowerHash.get("BORROW_NO")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(drawinPowerHash.get("ACCT_NO")));
            setCboStockStmtDay(CommonUtil.convertObjToStr(drawinPowerHash.get("STOCK_STAT_DAY")));
            setCboStockStmtFreq((String)getCbmStockStmtFreq().getDataForKey(CommonUtil.convertObjToStr(drawinPowerHash.get("STOCK_STAT_FREQ"))));
            setCboPreviousDPMonth(CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getDataForKey(drawinPowerHash.get("PREV_DP_MONTH"))));
            setCboCurrentDPMonth(CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getDataForKey(drawinPowerHash.get("CURR_DP_MONTH"))));
            setDateDateofInspection(CommonUtil.convertObjToStr(drawinPowerHash.get("INSPECTION_DT")));
            setDateDueDate(CommonUtil.convertObjToStr(drawinPowerHash.get("DUE_DT")));
            setDatePreviousDPValCalOn(CommonUtil.convertObjToStr(drawinPowerHash.get("PREV_DPVALUE_CALCDT")));
            setDateStockSubmittedDate(CommonUtil.convertObjToStr(drawinPowerHash.get("STOCK_SUBMIT_DT")));
            setTxtParticularsofGoods(CommonUtil.convertObjToStr(drawinPowerHash.get("GOODS_PARTICULARS")));
            setTxtValueofOpeningStock(CommonUtil.convertObjToStr(drawinPowerHash.get("OPENING_STOCK_VALUE")));
            setTxtValueofClosingStock(CommonUtil.convertObjToStr(drawinPowerHash.get("CLOSING_STOCK_VALUE")));
            setTxtPreviousDPValue(CommonUtil.convertObjToStr(drawinPowerHash.get("PREV_DP_VALUE")));
            setTxtPurchase(CommonUtil.convertObjToStr(drawinPowerHash.get("PURCHASE")));
            setTxtSales(CommonUtil.convertObjToStr(drawinPowerHash.get("SALES")));
            break;
        }
        HashMap authMap = new HashMap();
        authMap.put("ACCT_NO",drawinPowerHash.get("ACCT_NO"));
        authMap.put("SL_NO",drawinPowerDetailsHash.get("SL_NO"));
        List lst = ClientUtil.executeQuery("getAuthorizedRecordsDetails", authMap);
        if(lst!= null && lst.size()>0){
            authStatus = true;
        }else{ 
            authStatus = false;
        }
        notifyObservers();
        return authStatus;
    }
    
    /** Method to delete the row from CTable... */
    public void deleteDrawinPowerDetailsTab(int row){
        try{
            final ArrayList data = tblDrawingPowerDetailsTab.getDataArrayList();
            final int dataSize = data.size();
            int marginalTextValue = Integer.parseInt(getSerialNumber());
            if(marginalTextValue > maxTableSerialNumber){
                // If Marginal Text Value > Max of Serial num frm database...
                tableSerialNumber = tableSerialNumber - 1;
            }
            ArrayList arrayListData;
            for (int i=0;i<dataSize;i++){
                arrayListData = new ArrayList();
                arrayListData = (ArrayList)data.get(i);
                if ((CommonUtil.convertObjToStr(arrayListData.get(0))).equals(CommonUtil.convertObjToStr(getSerialNumber()))){
                    deleteRow();
                    if(marginalTextValue>maxTableSerialNumber){
                        // If Marginal Text Value > Max of Serial num frm database (Update HashMap Values)...
                        updateHash();
                    }
                    tblDrawingPowerDetailsTab.removeRow(i);
                    notifyObservers();
                    updateTableSerialNumber();
                    deleteDrawinPowerDetailsArrayList(tableSerialNumber+1);
                    deleteDrawinPowerDetailsUpdateArrayList(getSerialNumber());
                    arrayListData = null;
                    break;
                }
                arrayListData = null;
            }
            setChanged();
            notifyObservers();
            doDrawinPowerDeletedData(row);
            doDrawinPowerDetailsDeleteData(row);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public boolean deleteDrawinPowerTab(int row){
        try{
//            final ArrayList data = tblDrawingPowerDetailsTab.getDataArrayList();
//            final int dataSize = data.size();
//            int marginalTextValue = Integer.parseInt(getSerialNumber());
//            if(marginalTextValue > maxTableSerialNumber){
//                // If Marginal Text Value > Max of Serial num frm database...
//                tableSerialNumber = tableSerialNumber - 1;
//            }
//            ArrayList arrayListData;
//            for (int i=0;i<dataSize;i++){
//                arrayListData = new ArrayList();
//                arrayListData = (ArrayList)data.get(i);
//                if ((CommonUtil.convertObjToStr(arrayListData.get(0))).equals(CommonUtil.convertObjToStr(getSerialNumber()))){
//                    deleteRow();
//                    if(marginalTextValue>maxTableSerialNumber){
//                        // If Marginal Text Value > Max of Serial num frm database (Update HashMap Values)...
//                        updateHash();
//                    }
//                    tblDrawingPowerDetailsTab.removeRow(i);
//                    notifyObservers();
//                    updateTableSerialNumber();
//                    deleteDrawinPowerDetailsArrayList(tableSerialNumber+1);
//                    deleteDrawinPowerDetailsUpdateArrayList(getSerialNumber());
//                    arrayListData = null;
//                    break;
//                }
//                arrayListData = null;
//            }
//            setChanged();
//            notifyObservers();
            authStatus = doDrawinPowerDetailsDeleteData(row);
            doDrawinPowerDeletedData(row);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return authStatus;
    }
    
    private void deleteDrawinPowerRowTab(int row){
        tblDrawingPowerDetailsTab.setValueAt(getSerialNumber(), row, 0);
        tblDrawingPowerDetailsTab.setValueAt(getTxtCalculatedDrawingPower(), row, 1);
        tblDrawingPowerDetailsTab.setValueAt(getTxtPresentStockValue(), row, 2);
        tblDrawingPowerDetailsTab.setValueAt(getTxtMargin(), row, 3);
        tblDrawingPowerDetailsTab.setValueAt(CommonConstants.STATUS_DELETED, row, 4);
        setChanged();
        notifyObservers();        
    }
    /** Method to delete the row from the ArrayList */
    private void deleteRow(){
        ArrayList deleteArrayList = new ArrayList();
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap tempHash = new HashMap();
        HashMap drawinPowerDetailsHash;
        int yy = Integer.parseInt(getSerialNumber());
        
        // Adds the element that has to be updated to the remittProductBrchDeleteRow (DELETE DATA)
        tempHash.put("BORROW_NO", getLblBorrowNumber());
        tempHash.put("PROD_ID", getLblProductID());
        tempHash.put("ACCT_NO", getTxtAccountNumber());
        tempHash.put("SECURITY_NO", getLblSecurityNumber());
        tempHash.put("SL_NO", getSerialNumber());
        tempHash.put("LAST_STOCK_VALUE", getLblLastStockValue());
        tempHash.put("CALC_DRAWING_POWER", getTxtCalculatedDrawingPower());
        tempHash.put("PRESENT_STOCK_VALUE", getTxtPresentStockValue());
        tempHash.put("MARGIN", getTxtMargin());
        tempHash.put("MARGIN_AMT", getTxtMarginAmt());
        tempHash.put("TOTAL_DRAWING_POWER", getLblTotalDrawingPower());
        tempHash.put("DRAWING_POWER_SANCTIONED", getLblDrawingPowerSanctioned());
        deleteArrayList.add(tempHash);
        
        if(drawinPowerDetailsTabUpdate  == true && drawinPowerDetailsTabSelected == true && yy <= maxTableSerialNumber){
            drawinPowerDetailsTabDeleteData.add(tempHash);
        }
        tempHash = null;
        deleteArrayList = null;
        for (int j = 0;j<drawinPowerDetailsArrayListSize;j++){
            drawinPowerDetailsHash = new HashMap();
            drawinPowerDetailsHash = (HashMap)drawinPowerDetailsArrayList.get(j);
            if(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")).equals(CommonUtil.convertObjToStr(getSerialNumber()))){
                // Deletes the row element from the ArrayList
                drawinPowerDetailsArrayList.remove(j);
                break;
            }
        }
    }
    
    private void updateHash(){
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        int tabelSerialNumber = 0;
        int textSerialNumber = 0;
        textSerialNumber = Integer.parseInt(getSerialNumber());
        for (int j = 0;j<drawinPowerDetailsArrayListSize;j++){
            drawinPowerDetailsHash = new HashMap();
            drawinPowerDetailsHash = (HashMap)drawinPowerDetailsArrayList.get(j);
            tabelSerialNumber = Integer.parseInt(((HashMap)drawinPowerDetailsArrayList.get(j)).get("SL_NO").toString());
            if(tabelSerialNumber > textSerialNumber){
                if(CommonUtil.convertObjToStr(drawinPowerDetailsHash.get("SL_NO")).equals(CommonUtil.convertObjToStr(String.valueOf(tabelSerialNumber)))){
                    // Deletes the row element from the ArrayList
                    ((HashMap)drawinPowerDetailsArrayList.get(j)).put("SL_NO", String.valueOf(tabelSerialNumber - 1));
                }
            }
        }
        
    }
    
    private void updateTableSerialNumber(){
        int tableSerialNumber = 0;
        int marginalTextValue = 0;
        marginalTextValue = Integer.parseInt(getSerialNumber());
        int tblDrawingPowerDetailsTabRowCount = tblDrawingPowerDetailsTab.getRowCount();
        for (int j = 0;j<tblDrawingPowerDetailsTabRowCount;j++){
            tableSerialNumber = Integer.parseInt(CommonUtil.convertObjToStr(tblDrawingPowerDetailsTab.getValueAt(j, 0)));
            if(marginalTextValue > maxTableSerialNumber  && tableSerialNumber > marginalTextValue){
                tblDrawingPowerDetailsTab.setValueAt(String.valueOf(tableSerialNumber-1), j, 0);
            }
        }
        notifyObservers();
    }
    
    private void deleteDrawinPowerDetailsArrayList(int element){
        int insertArrayList = 0;
        int securityNumberNewDataSize = drawinPowerDetailsTabInsertData.size();
        int serialNumber = element;
        for(int i=0;i<securityNumberNewDataSize;i++){
            insertArrayList = Integer.parseInt(drawinPowerDetailsTabInsertData.get(i).toString());
            if(insertArrayList == serialNumber){
                drawinPowerDetailsTabInsertData.remove(i);
                break;
            }
        }
    }
    
    private void deleteDrawinPowerDetailsUpdateArrayList(String serialNum){
        int updateArrayList = 0;
        int serialNumber  = Integer.parseInt(serialNum);
        int securityNumberUpdateDataSize = drawinPowerDetailsTabUpdateData.size();
        for(int i=0;i<securityNumberUpdateDataSize;i++){
            updateArrayList = Integer.parseInt(drawinPowerDetailsTabUpdateData.get(i).toString());
            if(updateArrayList == serialNumber ){
                drawinPowerDetailsTabUpdateData.remove(i);
                break;
            }
        }
    }
    
    public void doSave(){
//        drawinPowerDetailsArrayListTO = new ArrayList();
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
//            updateDrawingPowerDetailsData();
//            deleteDrawingPowerDetailsData();
//            insertDrawingPowerDetailsData();
        }
        doAction();
        drawinPowerDetailsTabUpdateData.clear();
        drawinPowerDetailsTabInsertData.clear();
        drawinPowerDetailsTabDeleteData.clear();
        drawinPowerDetailsArrayListTO = null;
    }
    
    /** Gets all the data that has to be Updated and Sets to TO... */
    public void updateDrawingPowerDetailsData(){
        int securityNumberUpdateDataSize = drawinPowerDetailsTabUpdateData.size();
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap drawinPowerDetailsHash;
        for(int i=0;i<drawinPowerDetailsArrayListSize;i++){
            for(int j=0;j<securityNumberUpdateDataSize;j++){
                int serialNumberArrayList = Integer.parseInt(((HashMap)drawinPowerDetailsArrayList.get(i)).get("SL_NO").toString());
                int serialNumberUpdateArrayList = Integer.parseInt(drawinPowerDetailsTabUpdateData.get(j).toString());
                if(serialNumberArrayList == serialNumberUpdateArrayList){
                    setTO(drawinPowerDetailsArrayList, i, CommonConstants.STATUS_MODIFIED, CommonConstants.TOSTATUS_UPDATE);
                }
            }
        }
    }
    
    /** Gets all the data that has to be Deleted and Sets to TO... */
    public void deleteDrawingPowerDetailsData(){
        int securityNumberDeleteDataDataSize = drawinPowerDetailsTabDeleteData.size();
        HashMap drawinPowerDetailsHash;
        for(int i=0;i<securityNumberDeleteDataDataSize;i++){
            setTO(drawinPowerDetailsTabDeleteData, i, CommonConstants.STATUS_DELETED, CommonConstants.TOSTATUS_DELETE);
        }
    }
    
    /** Gets all the data that has to be Inserted and Sets to TO... */
    public void insertDrawingPowerDetailsData(){
        int securityNumberNewDataSize = drawinPowerDetailsTabInsertData.size();
        int drawinPowerDetailsArrayListSize = drawinPowerDetailsArrayList.size();
        HashMap drawinPowerDetailsHash;
        for(int i=0;i<drawinPowerDetailsArrayListSize;i++){
            for(int j=0;j<securityNumberNewDataSize;j++){
                int serialNumberArrayList = Integer.parseInt(((HashMap)drawinPowerDetailsArrayList.get(i)).get("SL_NO").toString());
                int serialNumberInsertArrayList = Integer.parseInt(drawinPowerDetailsTabInsertData.get(j).toString());
                if(serialNumberInsertArrayList == serialNumberArrayList){
                    setTO(drawinPowerDetailsArrayList, i, CommonConstants.STATUS_CREATED, CommonConstants.TOSTATUS_INSERT);
                }
            }
        }
    }
    
    /** Sets data to TO --> drawinPowerDetailsArrayListTO ... */
    private void setTO(ArrayList drawinPowerDetailsArrayList,int row, String status, String command){
        HashMap hash = new HashMap();
        hash = (HashMap) drawinPowerDetailsArrayList.get(row);
        hash.put("BORROW_NO", getLblBorrowNumber());
        hash.put("PROD_ID", getLblProductID());
        hash.put("ACCT_NO", getTxtAccountNumber());
        hash.put("STATUS", status);
        hash.put("COMMAND", command);
        drawinPowerDetailsArrayListTO.add(hash);
        hash = null;
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
            // If Action type is  Insert / Update CTable....
            if(drawinPowerArrayList.size()>0){
                drawinPowerMaintenanceArrayListTO = setDrawingPowerMaintenanceTO(drawinPowerArrayList);
                data.put("DrawingPowerMaintenanceTO",drawinPowerMaintenanceArrayListTO);                
            }
            if(drawinPowerDetailsArrayList.size() >0){
                drawinPowerDetailsTabTO = setDrawingPowerMaintenanceDetailsTO(drawinPowerDetailsArrayList);
                data.put("DrawingPowerMaintenanceDetailsTO",drawinPowerDetailsTabTO);
            }
        }//else{
            // If Action type is  Delete CTable...
//            ArrayList remitProdBrch = new ArrayList();
//            getDeleteData(remitProdBrch);
//            if(drawinPowerDetailsArrayListTO.size() >0){
//                drawinPowerDetailsTabTO = setDrawingPowerMaintenanceDetailsTO( drawinPowerDetailsArrayListTO );
//                data.put("DrawingPowerMaintenanceDetailsTO",drawinPowerDetailsTabTO);
//            }
//        }
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            data.put("AUTHORIZE_MAP",tamMaintenanceCreateMap);                    
        }else
            data.put("COMMAND",getCommand());        
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(getActionType());
        drawinPowerDetailsArrayListTO = null;
        tamMaintenanceCreateMap = null;
    }
    
    /** Gets the command issued Insert , Upadate or Delete **/
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
    
    /**  Gets the datas from the Fields and sets to Drawing Power Maintenance TO */
    public ArrayList setDrawingPowerMaintenanceTO(ArrayList remittProdMainBrchTO) throws Exception{
        int remittProdMainBrchTOSize = remittProdMainBrchTO.size();
        ArrayList remitProdBrchTOMainData = new ArrayList();
        HashMap hashremittProdMainBrchTO;
        try{
        for(int i=0;i<remittProdMainBrchTOSize;i++){
            hashremittProdMainBrchTO = new HashMap();
            DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO = new DrawingPowerMaintenanceTO();
            hashremittProdMainBrchTO = (HashMap)remittProdMainBrchTO.get(i);
            if(hashremittProdMainBrchTO.containsKey("STATUS") && hashremittProdMainBrchTO.get("STATUS").equals("UPDATE")){
                objDrawingPowerMaintenanceTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            }else if(hashremittProdMainBrchTO.containsKey("STATUS") && hashremittProdMainBrchTO.get("STATUS").equals("DELETE")){
                objDrawingPowerMaintenanceTO.setCommand(CommonConstants.TOSTATUS_DELETE);
            }else{
                objDrawingPowerMaintenanceTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                objDrawingPowerMaintenanceTO.setCreatedBy(TrueTransactMain.USER_ID);
                objDrawingPowerMaintenanceTO.setCreatedDt(curDate);
            }            
            objDrawingPowerMaintenanceTO.setBorrowNo(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("BORROW_NO")));
            objDrawingPowerMaintenanceTO.setAcctNo(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("ACCT_NO")));                
            Date PreDt = DateUtil.getDateMMDDYYYY(datePreviousDPValCalOn);
            if(PreDt != null){
                Date preDate = (Date)curDate.clone();
                preDate.setDate(PreDt.getDate());
                preDate.setMonth(PreDt.getMonth());
                preDate.setYear(PreDt.getYear());
                objDrawingPowerMaintenanceTO.setPrevDpvalueCalcdt(preDate);
            }else{
                objDrawingPowerMaintenanceTO.setPrevDpvalueCalcdt(DateUtil.getDateMMDDYYYY(datePreviousDPValCalOn));
            }                        
            Date DueDt = DateUtil.getDateMMDDYYYY(dateDueDate);
            if(DueDt != null){
                Date dueDate = (Date)curDate.clone();
                dueDate.setDate(DueDt.getDate());
                dueDate.setMonth(DueDt.getMonth());
                dueDate.setYear(DueDt.getYear());
                objDrawingPowerMaintenanceTO.setDueDt(dueDate);
            }else{
                objDrawingPowerMaintenanceTO.setDueDt(DateUtil.getDateMMDDYYYY(dateDueDate));
            }            
            Date SsDt = DateUtil.getDateMMDDYYYY(dateStockSubmittedDate);
            if(SsDt != null){
                Date ssDate = (Date)curDate.clone();
                ssDate.setDate(SsDt.getDate());
                ssDate.setMonth(SsDt.getMonth());
                ssDate.setYear(SsDt.getYear());
                objDrawingPowerMaintenanceTO.setStockSubmitDt(ssDate);
            }else{
                objDrawingPowerMaintenanceTO.setStockSubmitDt(DateUtil.getDateMMDDYYYY(dateStockSubmittedDate));
            }
            Date DoiDt = DateUtil.getDateMMDDYYYY(dateDateofInspection);
            if(DoiDt != null){
                Date doiDate = (Date)curDate.clone();
                doiDate.setDate(DoiDt.getDate());
                doiDate.setMonth(DoiDt.getMonth());
                doiDate.setYear(DoiDt.getYear());
                objDrawingPowerMaintenanceTO.setInspectionDt(doiDate);
            }else{
                objDrawingPowerMaintenanceTO.setInspectionDt(DateUtil.getDateMMDDYYYY(dateDateofInspection));
            }
//            objDrawingPowerMaintenanceTO.setSecurityNo(getLblSecurityNumber());
            objDrawingPowerMaintenanceTO.setProdId(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("PROD_ID")));
            objDrawingPowerMaintenanceTO.setStockStatFreq(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("STOCK_STAT_FREQ")));            
            objDrawingPowerMaintenanceTO.setPrevDpMonth(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("PREV_DP_MONTH")));
            objDrawingPowerMaintenanceTO.setPrevDpValue(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("PREV_DP_VALUE")));
            objDrawingPowerMaintenanceTO.setCurrDpMonth(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("CURR_DP_MONTH")));
            objDrawingPowerMaintenanceTO.setGoodsParticulars(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("GOODS_PARTICULARS")));            
            objDrawingPowerMaintenanceTO.setOpeningStockValue(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("OPENING_STOCK_VALUE")));
            objDrawingPowerMaintenanceTO.setPurchase(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("PURCHASE")));
            objDrawingPowerMaintenanceTO.setClosingStockValue(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("CLOSING_STOCK_VALUE")));
            objDrawingPowerMaintenanceTO.setSales(CommonUtil.convertObjToDouble(hashremittProdMainBrchTO.get("SALES")));
            objDrawingPowerMaintenanceTO.setStockStatDay(CommonUtil.convertObjToStr(hashremittProdMainBrchTO.get("STOCK_STAT_DAY")));
            objDrawingPowerMaintenanceTO.setStatusBy(TrueTransactMain.USER_ID);
            objDrawingPowerMaintenanceTO.setStatusDt(curDate);
            objDrawingPowerMaintenanceTO.setBranchCode(getSelectedBranchID());
            objDrawingPowerMaintenanceTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objDrawingPowerMaintenanceTO.setSerialNo(new Double(i+1));
            remitProdBrchTOMainData.add(objDrawingPowerMaintenanceTO);
            hashremittProdMainBrchTO = null;
            objDrawingPowerMaintenanceDetailsTO =  null;
        }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return remitProdBrchTOMainData;

//        final DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO = new DrawingPowerMaintenanceTO();
//        try{
//            objDrawingPowerMaintenanceTO.setBorrowNo(getTxtBorrowerNum());
//            objDrawingPowerMaintenanceTO.setSecurityNo(getLblSecurityNumber());
//            objDrawingPowerMaintenanceTO.setProdId(getLblProductID());
//            objDrawingPowerMaintenanceTO.setAcctNo(getLblAccountNumber());
//            objDrawingPowerMaintenanceTO.setStockStatFreq(CommonUtil.convertObjToDouble(cbmStockStmtFreq.getKeyForSelected()));
//            
//            Date PreDt = DateUtil.getDateMMDDYYYY(datePreviousDPValCalOn);
//            if(PreDt != null){
//            Date preDate = (Date)curDate.clone();
//            preDate.setDate(PreDt.getDate());
//            preDate.setMonth(PreDt.getMonth());
//            preDate.setYear(PreDt.getYear());
////            objDrawingPowerMaintenanceTO.setPrevDpvalueCalcdt(DateUtil.getDateMMDDYYYY(datePreviousDPValCalOn));
//            objDrawingPowerMaintenanceTO.setPrevDpvalueCalcdt(preDate);
//            }else{
//                objDrawingPowerMaintenanceTO.setPrevDpvalueCalcdt(DateUtil.getDateMMDDYYYY(datePreviousDPValCalOn));
//            }
//            
//            objDrawingPowerMaintenanceTO.setPrevDpMonth(CommonUtil.convertObjToStr(cbmPreviousDPMonth.getKeyForSelected()));
//            objDrawingPowerMaintenanceTO.setPrevDpValue(CommonUtil.convertObjToDouble(getTxtPreviousDPValue()));
//            objDrawingPowerMaintenanceTO.setCurrDpMonth(CommonUtil.convertObjToStr(cbmCurrentDPMonth.getKeyForSelected()));
//            
//            Date DueDt = DateUtil.getDateMMDDYYYY(dateDueDate);
//            if(DueDt != null){
//            Date dueDate = (Date)curDate.clone();
//            dueDate.setDate(DueDt.getDate());
//            dueDate.setMonth(DueDt.getMonth());
//            dueDate.setYear(DueDt.getYear());
////            objDrawingPowerMaintenanceTO.setDueDt(DateUtil.getDateMMDDYYYY(dateDueDate));
//            objDrawingPowerMaintenanceTO.setDueDt(dueDate);
//            }else{
//               objDrawingPowerMaintenanceTO.setDueDt(DateUtil.getDateMMDDYYYY(dateDueDate)); 
//            }
//            
//            Date SsDt = DateUtil.getDateMMDDYYYY(dateStockSubmittedDate);
//            if(SsDt != null){
//            Date ssDate = (Date)curDate.clone();
//            ssDate.setDate(SsDt.getDate());
//            ssDate.setMonth(SsDt.getMonth());
//            ssDate.setYear(SsDt.getYear());
////            objDrawingPowerMaintenanceTO.setStockSubmitDt(DateUtil.getDateMMDDYYYY(dateStockSubmittedDate));
//            objDrawingPowerMaintenanceTO.setStockSubmitDt(ssDate);
//            }else{
//                objDrawingPowerMaintenanceTO.setStockSubmitDt(DateUtil.getDateMMDDYYYY(dateStockSubmittedDate));
//            }
//            
//            objDrawingPowerMaintenanceTO.setGoodsParticulars(getTxtParticularsofGoods());
//            
//            Date DoiDt = DateUtil.getDateMMDDYYYY(dateDateofInspection);
//            if(DoiDt != null){
//            Date doiDate = (Date)curDate.clone();
//            doiDate.setDate(DoiDt.getDate());
//            doiDate.setMonth(DoiDt.getMonth());
//            doiDate.setYear(DoiDt.getYear());
////            objDrawingPowerMaintenanceTO.setInspectionDt(DateUtil.getDateMMDDYYYY(dateDateofInspection));
//            objDrawingPowerMaintenanceTO.setInspectionDt(doiDate);
//            }else{
//                objDrawingPowerMaintenanceTO.setInspectionDt(DateUtil.getDateMMDDYYYY(dateDateofInspection));
//            }
//            
//            objDrawingPowerMaintenanceTO.setOpeningStockValue(CommonUtil.convertObjToDouble(getTxtValueofOpeningStock()));
//            objDrawingPowerMaintenanceTO.setPurchase(CommonUtil.convertObjToDouble(getTxtPurchase()));
//            objDrawingPowerMaintenanceTO.setClosingStockValue(CommonUtil.convertObjToDouble(getTxtValueofClosingStock()));
//            objDrawingPowerMaintenanceTO.setSales(CommonUtil.convertObjToDouble(getTxtSales()));
//            objDrawingPowerMaintenanceTO.setStatusBy(TrueTransactMain.USER_ID);
//            objDrawingPowerMaintenanceTO.setStatusDt(curDate);
//            objDrawingPowerMaintenanceTO.setBranchCode(getSelectedBranchID());
//            objDrawingPowerMaintenanceTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
//            objDrawingPowerMaintenanceTO.setStockStatDay(CommonUtil.convertObjToStr(cbmPreviousDPMonth.getKeyForSelected()));
//        }catch(Exception e){
//            parseException.logException(e,true);
//        }
//        return objDrawingPowerMaintenanceTO;
    }
    
    /** Gets the datas from the Fields and sets to Drawing Power Details TO */
    private ArrayList setDrawingPowerMaintenanceDetailsTO(ArrayList remittProdBrchTO){
        int remittProdBrchTOSize = remittProdBrchTO.size();
        ArrayList remitProdBrchTOData = new ArrayList();
        HashMap hashRemittProdBrchTO;
        for(int i=0;i<remittProdBrchTOSize;i++){
            objDrawingPowerMaintenanceDetailsTO = new DrawingPowerMaintenanceDetailsTO();
            hashRemittProdBrchTO = new HashMap();
            hashRemittProdBrchTO = (HashMap)remittProdBrchTO.get(i);
            if(hashRemittProdBrchTO.containsKey("STATUS") && hashRemittProdBrchTO.get("STATUS").equals("UPDATE"))
                objDrawingPowerMaintenanceDetailsTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            else if(hashRemittProdBrchTO.containsKey("STATUS") && hashRemittProdBrchTO.get("STATUS").equals("DELETE"))
                objDrawingPowerMaintenanceDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
            else
                objDrawingPowerMaintenanceDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objDrawingPowerMaintenanceDetailsTO.setBorrowNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("BORROW_NO")));
            objDrawingPowerMaintenanceDetailsTO.setAcctNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("ACCT_NO")));                
            objDrawingPowerMaintenanceDetailsTO.setProdId(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("PROD_ID")));
            objDrawingPowerMaintenanceDetailsTO.setSecurityNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("SECURITY_NO")));
            objDrawingPowerMaintenanceDetailsTO.setSlNo(CommonUtil.convertObjToStr(hashRemittProdBrchTO.get("SL_NO")));
            objDrawingPowerMaintenanceDetailsTO.setPresentStockValue(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("PRESENT_STOCK_VALUE")));
            objDrawingPowerMaintenanceDetailsTO.setMargin(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("MARGIN")));
            objDrawingPowerMaintenanceDetailsTO.setMarginAmt(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("MARGIN_AMT")));
            objDrawingPowerMaintenanceDetailsTO.setLastStockValue(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("LAST_STOCK_VALUE")));
            objDrawingPowerMaintenanceDetailsTO.setCalcDrawingPower(CommonUtil.convertObjToDouble(hashRemittProdBrchTO.get("CALC_DRAWING_POWER")));
            objDrawingPowerMaintenanceDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objDrawingPowerMaintenanceDetailsTO.setStatusDt(curDate);
            remitProdBrchTOData.add(objDrawingPowerMaintenanceDetailsTO);
            hashRemittProdBrchTO = null;
            objDrawingPowerMaintenanceDetailsTO =  null;
        }
        return remitProdBrchTOData;
    }
    
    /** Method gets the datas to be Deleted */
    private void  getDeleteData(ArrayList remitProdBrch){
        int arrayListRemitProdBrchTabUpdateSize = drawinPowerDetailsArrayList.size();
        for(int i=0;i<arrayListRemitProdBrchTabUpdateSize;i++){
            setTO(drawinPowerDetailsArrayList, i, CommonConstants.STATUS_DELETED, CommonConstants.TOSTATUS_DELETE);
        }
    }
    
    /** To populate to the screen */
    public void populateData(HashMap whereMap) {
        HashMap mapData = new HashMap() ;
        
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) {
        
        ArrayList remitProdBrchTOArrayList  = new ArrayList();
        ArrayList remitProdTOArrayList  = new ArrayList();
//        DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO;
//        objDrawingPowerMaintenanceTO = (DrawingPowerMaintenanceTO) ((List) mapData.get("DrawingPowerMaintenanceTO")).get(0);
        remitProdTOArrayList = (ArrayList) mapData.get("DrawingPowerMaintenanceTO");
//        getDrawingPowerMaintenanceTO(objDrawingPowerMaintenanceTO);
        remitProdBrchTOArrayList  = (ArrayList) mapData.get("DrawingPowerMaintenanceDetailsTO");
        getDrawingPowerMaintenanceDetailsTO(remitProdBrchTOArrayList);
        getDrawingPowerMaintenanceTO(remitProdTOArrayList);
//        notifyObservers();
    }
    
    /** Gets datas from Drawing Power Maintenance TO and sets to Fields */
    public void getDrawingPowerMaintenanceTO(DrawingPowerMaintenanceTO objDrawingPowerMaintenanceTO){
//        setLblProductID(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getProdId()));
//        setTxtBorrowerNum(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getBorrowNo()));
//        setLblAccountNumber(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getAcctNo()));
//        setCboStockStmtFreq(CommonUtil.convertObjToStr(getCbmStockStmtFreq().getDataForKey(String.valueOf(Integer.parseInt(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getStockStatFreq()))))));
//        setCboPreviousDPMonth(CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getDataForKey(objDrawingPowerMaintenanceTO.getPrevDpMonth())));
//        setCboCurrentDPMonth(CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getDataForKey(objDrawingPowerMaintenanceTO.getCurrDpMonth())));
//        setDateDateofInspection(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getInspectionDt()));
//        setDateDueDate(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getDueDt()));
//        setDatePreviousDPValCalOn(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getPrevDpvalueCalcdt()));
//        setDateStockSubmittedDate(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getStockSubmitDt()));
//        setTxtParticularsofGoods(objDrawingPowerMaintenanceTO.getGoodsParticulars());
//        setTxtValueofOpeningStock(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getOpeningStockValue()));
//        setTxtValueofClosingStock(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getClosingStockValue()));
//        setTxtPreviousDPValue(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getPrevDpValue()));
//        setTxtPurchase(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getPurchase()));
//        setTxtSales(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getSales()));
//        int remitProdBrchTOArrayListSize = prodBrchTOArrayList.size();
//        HashMap temp;
//        HashMap hashRemitProdBrchTOArrayList;
//        drawinPowerDetailsArrayList = new ArrayList();
//        for(int i=0;i<remitProdBrchTOArrayListSize;i++){
//            temp = new HashMap();
//            int tabRowCount = tblDrawingPowerDetailsTab.getRowCount();
//            drawinPowerDetailsHash = new HashMap();
//            hashRemitProdBrchTOArrayList = new HashMap();
//            hashRemitProdBrchTOArrayList = (HashMap)prodBrchTOArrayList.get(i);
//
//        setLblProductID(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("PROD_ID")));
//        setTxtBorrowerNum(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("ACCT_NO")));
//        setLblAccountNumber(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("BORROW_NO")));
////        setCboStockStmtFreq(CommonUtil.convertObjToStr(getCbmStockStmtFreq().getDataForKey(String.valueOf(Integer.parseInt(CommonUtil.convertObjToStr(objDrawingPowerMaintenanceTO.getStockStatFreq()))))));
////        setCboPreviousDPMonth(CommonUtil.convertObjToStr(getCbmPreviousDPMonth().getDataForKey(objDrawingPowerMaintenanceTO.getPrevDpMonth())));
////        setCboCurrentDPMonth(CommonUtil.convertObjToStr(getCbmCurrentDPMonth().getDataForKey(objDrawingPowerMaintenanceTO.getCurrDpMonth())));
////        setDateDateofInspection(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getInspectionDt()));
////        setDateDueDate(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getDueDt()));
////        setDatePreviousDPValCalOn(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getPrevDpvalueCalcdt()));
////        setDateStockSubmittedDate(DateUtil.getStringDate(objDrawingPowerMaintenanceTO.getStockSubmitDt()));
//        setTxtParticularsofGoods(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("GOODS_PARTICULARS")));
//        setTxtValueofOpeningStock(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("OPENING_STOCK_VALUE")));
//        setTxtValueofClosingStock(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("CLOSING_STOCK_VALUE")));
//        setTxtPreviousDPValue(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("PREV_DP_VALUE")));
//        setTxtPurchase(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("PURCHASE")));
//        setTxtSales(CommonUtil.convertObjToStr(hashRemitProdBrchTOArrayList.get("SALES")));
//        HashMap custName = new HashMap();
//        custName.put("ACC_NUM",objDrawingPowerMaintenanceTO.getAcctNo());
//        List lst = ClientUtil.executeQuery("getAccountNumberNameAD", custName);
//        if(lst!=null && lst.size()>0){
//            custName = (HashMap)lst.get(0);
//            setLblCustID(CommonUtil.convertObjToStr(custName.get("CUST_ID")));
//            setLblCustomerName(CommonUtil.convertObjToStr(custName.get("CUSTOMER_NAME")));
//        }
//        lst = null;
//        custName = new HashMap();
//        custName.put("PROD_ID",objDrawingPowerMaintenanceTO.getProdId());
//        lst = ClientUtil.executeQuery("getAccountHeadProdAD", custName);
//        if(lst!=null && lst.size()>0){
//            custName = (HashMap)lst.get(0);
//            setLblAccountHead(CommonUtil.convertObjToStr(custName.get("ACCT_HEAD")));
//        }
//        lst = null;
//        custName = null;        
//        }
    }
    
    /** Gets datas from Drawing Power Details TO and sets to CTable....  */
    public void getDrawingPowerMaintenanceDetailsTO(ArrayList remitProdBrchTOArrayList){
        int remitProdBrchTOArrayListSize = remitProdBrchTOArrayList.size();
        HashMap temp;
        HashMap hashRemitProdBrchTOArrayList;
        drawinPowerDetailsArrayList = new ArrayList();
        for(int i=0;i<remitProdBrchTOArrayListSize;i++){
            temp = new HashMap();
            int tabRowCount = tblDrawingPowerDetailsTab.getRowCount();
            drawinPowerDetailsHash = new HashMap();
            hashRemitProdBrchTOArrayList = new HashMap();
            hashRemitProdBrchTOArrayList = (HashMap)remitProdBrchTOArrayList.get(i);
            
            temp.put("BORROW_NO", hashRemitProdBrchTOArrayList.get("BORROW_NO"));
            temp.put("PROD_ID", hashRemitProdBrchTOArrayList.get("PROD_ID"));
            temp.put("ACCT_NO", hashRemitProdBrchTOArrayList.get("ACCT_NO"));
            temp.put("SECURITY_NO", hashRemitProdBrchTOArrayList.get("SECURITY_NO"));
            temp.put("SL_NO", hashRemitProdBrchTOArrayList.get("SL_NO"));
            temp.put("LAST_STOCK_VALUE", hashRemitProdBrchTOArrayList.get("LAST_STOCK_VALUE"));
            temp.put("CALC_DRAWING_POWER", hashRemitProdBrchTOArrayList.get("CALC_DRAWING_POWER"));
            temp.put("PRESENT_STOCK_VALUE", hashRemitProdBrchTOArrayList.get("PRESENT_STOCK_VALUE"));
            temp.put("MARGIN", hashRemitProdBrchTOArrayList.get("MARGIN"));
            temp.put("TOTAL_DRAWING_POWER", hashRemitProdBrchTOArrayList.get("TOTAL_DRAWING_POWER"));
            temp.put("DRAWING_POWER_SANCTIONED", hashRemitProdBrchTOArrayList.get("DRAWING_POWER_SANCTIONED"));
            temp.put("MARGIN_AMT", hashRemitProdBrchTOArrayList.get("MARGIN_AMT"));
            temp.put("STATUS", "UPDATE");
            
            drawinPowerDetailsArrayList.add(temp);
            
            temp = null;
            ArrayList remitProdBrchTORow = new ArrayList();
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("SL_NO"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("CALC_DRAWING_POWER"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("PRESENT_STOCK_VALUE"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("MARGIN"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("STATUS"));
            remitProdBrchTORow.add(hashRemitProdBrchTOArrayList.get("AUTHORIZE_STATUS"));
            
            tblDrawingPowerDetailsTab.insertRow(tabRowCount,remitProdBrchTORow);
            hashRemitProdBrchTOArrayList = null;
            remitProdBrchTORow = null;
        }
    }
    
    public void getDrawingPowerMaintenanceTO(ArrayList remitProdTOArrayList ){
        int remitProdTOArrayListSize = remitProdTOArrayList.size();
        HashMap temp;
        HashMap hashRemitProdTOArrayList;
        drawinPowerArrayList = new ArrayList();
        for(int j=0;j<remitProdTOArrayListSize;j++){
            temp = new HashMap();
            int tabRowCount = tblDrawingPowerDetailsTab.getRowCount();
            drawinPowerHash = new HashMap();
            hashRemitProdTOArrayList = new HashMap();
            hashRemitProdTOArrayList = (HashMap)remitProdTOArrayList.get(j);            
            temp.put("BORROW_NO", hashRemitProdTOArrayList.get("BORROW_NO"));
            temp.put("PROD_ID", hashRemitProdTOArrayList.get("PROD_ID"));
            temp.put("ACCT_NO", hashRemitProdTOArrayList.get("ACCT_NO"));
            temp.put("PREV_DP_MONTH", hashRemitProdTOArrayList.get("PREV_DP_MONTH"));
            temp.put("CURR_DP_MONTH", hashRemitProdTOArrayList.get("CURR_DP_MONTH"));
            temp.put("STOCK_STAT_FREQ", hashRemitProdTOArrayList.get("STOCK_STAT_FREQ"));
            temp.put("STOCK_STAT_DAY", hashRemitProdTOArrayList.get("STOCK_STAT_DAY"));
            temp.put("INSPECTION_DT", hashRemitProdTOArrayList.get("INSPECTION_DT"));
            temp.put("DUE_DT", hashRemitProdTOArrayList.get("DUE_DT"));
            temp.put("PREV_DPVALUE_CALCDT", hashRemitProdTOArrayList.get("PREV_DPVALUE_CALCDT"));
            temp.put("STOCK_SUBMIT_DT", hashRemitProdTOArrayList.get("STOCK_SUBMIT_DT"));
            temp.put("GOODS_PARTICULARS", hashRemitProdTOArrayList.get("GOODS_PARTICULARS"));
            temp.put("OPENING_STOCK_VALUE", hashRemitProdTOArrayList.get("OPENING_STOCK_VALUE"));
            temp.put("CLOSING_STOCK_VALUE", hashRemitProdTOArrayList.get("CLOSING_STOCK_VALUE"));
            temp.put("PREV_DP_VALUE", hashRemitProdTOArrayList.get("PREV_DP_VALUE"));
            temp.put("PURCHASE", hashRemitProdTOArrayList.get("PURCHASE"));
            temp.put("SALES", hashRemitProdTOArrayList.get("SALES"));
            temp.put("STATUS", "UPDATE");
            
            drawinPowerArrayList.add(temp);
            temp = null;
            List lst = null;
            HashMap custName = new HashMap();
            if(hashRemitProdTOArrayList.containsKey("ACCT_NO")){
            custName.put("ACC_NUM", hashRemitProdTOArrayList.get("ACCT_NO"));
                lst = ClientUtil.executeQuery("getAccountNumberNameAD", custName);
                if(lst!=null && lst.size()>0){
                    custName = (HashMap)lst.get(0);
                    setLblCustID(CommonUtil.convertObjToStr(custName.get("CUST_ID")));
                    setLblCustomerName(CommonUtil.convertObjToStr(custName.get("CUSTOMER_NAME")));
                }
            }
            lst = null;
            custName = new HashMap();
            if(hashRemitProdTOArrayList.containsKey("PROD_ID")){
                custName.put("PROD_ID", hashRemitProdTOArrayList.get("PROD_ID"));
                lst = ClientUtil.executeQuery("getAccountHeadProdAD", custName);
                if(lst!=null && lst.size()>0){
                    custName = (HashMap)lst.get(0);
                    setLblAccountHead(CommonUtil.convertObjToStr(custName.get("ACCT_HEAD")));
                }
            }
            lst = null;
            custName = null;        
            hashRemitProdTOArrayList = null;
        }
    }
    
    /** Method to display alert message */
    public void displayCDialogue(String warningMessage){
        int option = -1;
        String[] options = {objDrawingPowerMaintenanceRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, warningMessage, CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    /** This Removes existing row from the Table */
    public void removeTableRow(){
        int tabRow = tblDrawingPowerDetailsTab.getRowCount();
        for(int i=0;i<tabRow;i++) {
            tblDrawingPowerDetailsTab.removeRow(0);
        }
        tblDrawingPowerDetailsTab = new EnhancedTableModel(null, drawingPowerDetailsTabTitle);
        setTblDrawingPowerDetailsTab(tblDrawingPowerDetailsTab);
    }
    
    public void clearData(){
        drawinPowerDetailsTabUpdateData.clear();
        drawinPowerDetailsTabInsertData.clear();
        drawinPowerDetailsArrayList.clear();
    }
    
    /** Method to set Label values Drawing Power Maintenance... */
    public void setLableDrawingPower(){
//        setLblProductID("010212BC");
//        setTxtBorrowerNum("01332");
//        setLblAccountNumber("21755EC");
//        setLblAccountHead("MG Road");
//        notifyObservers();
    }
    
    /** Method to set Label values Drawing Power Details... */
    public void setLableDrawingPowerDetails(){
//        setLblSecurityNumber("532321HD");
//        setLblLastStockValue("23243");
//        setLblTotalDrawingPower("532321");
//        setLblDrawingPowerSanctioned("235342");
//        notifyObservers();
    }

    public void resetDrawingPowerAllFields(){
        setLblProductID("");
        setLblBorrowNumber("");
        setTxtAccountNumber("");
        setLblAccountHead("");
        setLblCustID("");
        setLblCustomerName("");
        notifyObservers();
    }
    
    /** Method to set Label values Drawing Power Maintenance to NULL... */
    public void resetDrawingPowerFields(){
        setCboStockStmtFreq("");
        setCboPreviousDPMonth("");
        setCboCurrentDPMonth("");
        setTxtParticularsofGoods("");
        setTxtValueofOpeningStock("");
        setTxtValueofClosingStock("");
        setTxtPreviousDPValue("");
        setTxtPurchase("");
        setTxtSales("");
        setDatePreviousDPValCalOn("");
        setDateDueDate("");
        setDateStockSubmittedDate("");
        setDateDateofInspection("");
        setLblSecurityNumber("");
        setLblLastStockValue("");
        setLblDrawingPowerSanctioned("");
        setTxtCalculatedDrawingPower("");
        setTxtPresentStockValue("");
        setTxtMargin("");
        setTxtMarginAmt("");
        setCboStockStmtDay("");
        setLblDrawingPowerDateValue("");
        notifyObservers();
    }
    
    /** Method to set Label values Drawing Power Details to NULL... */
    public void resetDrawingPowerNewFields(){
        setCboCurrentDPMonth("");
        setTxtParticularsofGoods("");
        setTxtValueofOpeningStock("");
        setTxtValueofClosingStock("");
        setTxtPurchase("");
        setTxtSales("");
        setDateStockSubmittedDate("");
        setDateDateofInspection("");
        setTxtCalculatedDrawingPower("");
        setTxtPresentStockValue("");
        setTxtMargin("");
        setTxtMarginAmt("");
        notifyObservers();
    }
    
    public void resetDrawingPowerDetailsFields(){
//        setLblSecurityNumber("");
//        setLblLastStockValue("");
//        setLblDrawingPowerSanctioned("");
//        setTxtCalculatedDrawingPower("");
//        setTxtPresentStockValue("");
//        setTxtMargin("");
        //setLblTotalDrawingPower("");
//        notifyObservers();
    }
        
    /**
     * Getter for property lblCustID.
     * @return Value of property lblCustID.
     */
    public java.lang.String getLblCustID() {
        return lblCustID;
    }
    
    /**
     * Setter for property lblCustID.
     * @param lblCustID New value of property lblCustID.
     */
    public void setLblCustID(java.lang.String lblCustID) {
        this.lblCustID = lblCustID;
    }
    
    /**
     * Getter for property lblCustomerName.
     * @return Value of property lblCustomerName.
     */
    public java.lang.String getLblCustomerName() {
        return lblCustomerName;
    }
    
    /**
     * Setter for property lblCustomerName.
     * @param lblCustomerName New value of property lblCustomerName.
     */
    public void setLblCustomerName(java.lang.String lblCustomerName) {
        this.lblCustomerName = lblCustomerName;
    }
    
    
    public void resetDetailsTabList(){
        drawinPowerDetailsArrayListTO = null;
        drawinPowerDetailsArrayListTO = new ArrayList();
    }
    
    /**
     * Getter for property cbmStockStmtDay.
     * @return Value of property cbmStockStmtDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmStockStmtDay() {
        return cbmStockStmtDay;
    }
    
    /**
     * Setter for property cbmStockStmtDay.
     * @param cbmStockStmtDay New value of property cbmStockStmtDay.
     */
    public void setCbmStockStmtDay(com.see.truetransact.clientutil.ComboBoxModel cbmStockStmtDay) {
        this.cbmStockStmtDay = cbmStockStmtDay;
    }
    
    /**
     * Getter for property cboStockStmtDay.
     * @return Value of property cboStockStmtDay.
     */
    public java.lang.String getCboStockStmtDay() {
        return cboStockStmtDay;
    }
    
    /**
     * Setter for property cboStockStmtDay.
     * @param cboStockStmtDay New value of property cboStockStmtDay.
     */
    public void setCboStockStmtDay(java.lang.String cboStockStmtDay) {
        this.cboStockStmtDay = cboStockStmtDay;
    }
    
    /**
     * Getter for property txtMarginAmt.
     * @return Value of property txtMarginAmt.
     */
    public java.lang.String getTxtMarginAmt() {
        return txtMarginAmt;
    }
    
    /**
     * Setter for property txtMarginAmt.
     * @param txtMarginAmt New value of property txtMarginAmt.
     */
    public void setTxtMarginAmt(java.lang.String txtMarginAmt) {
        this.txtMarginAmt = txtMarginAmt;
    }
    
    /**
     * Getter for property txtAccountNumber.
     * @return Value of property txtAccountNumber.
     */
    public java.lang.String getTxtAccountNumber() {
        return txtAccountNumber;
    }
    
    /**
     * Setter for property txtAccountNumber.
     * @param txtAccountNumber New value of property txtAccountNumber.
     */
    public void setTxtAccountNumber(java.lang.String txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }
    
    /**
     * Getter for property lblBorrowNumber.
     * @return Value of property lblBorrowNumber.
     */
    public java.lang.String getLblBorrowNumber() {
        return lblBorrowNumber;
    }
    
    /**
     * Setter for property lblBorrowNumber.
     * @param lblBorrowNumber New value of property lblBorrowNumber.
     */
    public void setLblBorrowNumber(java.lang.String lblBorrowNumber) {
        this.lblBorrowNumber = lblBorrowNumber;
    }
    
    /**
     * Getter for property lblDrawingPowerDateValue.
     * @return Value of property lblDrawingPowerDateValue.
     */
    public java.lang.String getLblDrawingPowerDateValue() {
        return lblDrawingPowerDateValue;
    }
    
    /**
     * Setter for property lblDrawingPowerDateValue.
     * @param lblDrawingPowerDateValue New value of property lblDrawingPowerDateValue.
     */
    public void setLblDrawingPowerDateValue(java.lang.String lblDrawingPowerDateValue) {
        this.lblDrawingPowerDateValue = lblDrawingPowerDateValue;
    }
    
}
