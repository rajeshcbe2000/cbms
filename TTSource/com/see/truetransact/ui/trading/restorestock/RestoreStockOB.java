/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RestoreStockOB.java
 *
 * Created on Mon Apr 25 3:55 PM 2016
 */

package com.see.truetransact.ui.trading.restorestock;

import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author Revathi L
 */

public class RestoreStockOB extends CObservable{
    
    Date curDate = null;
    private ArrayList key,value;
    private ProxyFactory proxy;
    private static RestoreStockOB objTradingTransferOB;
    private HashMap map,keyValue,lookUpHash;
    private final static Logger log = Logger.getLogger(RestoreStockOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private LinkedHashMap restoreDetailsMap;
    private TableModel tblRestoreDetails;
    final ArrayList tblRestoreStockDetailsTitle = new ArrayList();
    private Boolean newData = false;
    private HashMap authorizeMap;
    private List finalList = null;
    private String fromProdID = "";
    private String toProdID = "";
    private String fromPvID = "";
    private String toPvID = "";
    private String fromDt = "";
    private String toDt = "";
    private String lblRestoreID = "";
    private String restoreTotAmt = "";
    HashMap deleteMap = new HashMap();
//    private String tdsCeAchdId="";
    
    /** Consturctor Declaration  for  TDSConfigOB */
    private RestoreStockOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTblRestoreDetails();
            tblRestoreDetails = new TableModel(null, tblRestoreStockDetailsTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public void setTblRestoreDetails() {
        tblRestoreStockDetailsTitle.add("Select");
        tblRestoreStockDetailsTitle.add("Sl No");
        tblRestoreStockDetailsTitle.add("PV ID");
        tblRestoreStockDetailsTitle.add("Prod ID");
        tblRestoreStockDetailsTitle.add("Prod Name");
        tblRestoreStockDetailsTitle.add("Type");
        tblRestoreStockDetailsTitle.add("Stock ID");
        tblRestoreStockDetailsTitle.add("Avail Qty");
        tblRestoreStockDetailsTitle.add("Phy Qty");
        tblRestoreStockDetailsTitle.add("Diff");
        tblRestoreStockDetailsTitle.add("Purch Price");
        tblRestoreStockDetailsTitle.add("MRP");
        tblRestoreStockDetailsTitle.add("Sales Price");
        tblRestoreStockDetailsTitle.add("Diff Amt");
        tblRestoreStockDetailsTitle.add("Restore Qty");
        tblRestoreStockDetailsTitle.add("Restore Amt");
    }
    
    public void cellRestoreEditableColumnTrue() {
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            tblRestoreDetails.setEditColoumnNo(14);
        }
    }
    
    public void cellRestoreEditableColumnFalse() {
       tblRestoreDetails.setEditColoumnNo(-1);
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingTransferOB = new RestoreStockOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "RestoreStockJNDI");
        map.put(CommonConstants.HOME, "trading.restorestock.RestoreStockHome");
        map.put(CommonConstants.REMOTE, "trading.restorestock.RestoreStock");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
    }
    
    /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void resetRestoretockTblDetails(){
        tblRestoreDetails.setDataArrayList(null, tblRestoreStockDetailsTitle);
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static RestoreStockOB getInstance()throws Exception{
        return objTradingTransferOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
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
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Clear up all the Fields of UI thru OB **/
    public void resetForm(){
        notifyObservers();
    }
    
    public void displayRestoreDetails(HashMap whereMap) throws Exception {
       try{
           String purchPrice = "";
           String salesPrice = "";
           String mrp = "";
           HashMap dataMap =  new HashMap();
           ArrayList rowList = new ArrayList();
           List restoreList = ClientUtil.executeQuery("displayStockDiffDetails", whereMap);
           
           if(restoreList!=null && restoreList.size()>0){
               for (int i = 0; i < restoreList.size(); i++) {
                    dataMap = (HashMap) restoreList.get(i);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(i+1);
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PV_ID")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PROD_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("UNIT_TYPE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("STOCK_ID")));
                    rowList.add(CommonUtil.convertObjToInt(dataMap.get("AVAIL_QTY")));
                    rowList.add(CommonUtil.convertObjToInt(dataMap.get("PV_QTY")));
                    rowList.add(CommonUtil.convertObjToInt(dataMap.get("DIFFERENCE")));
                    purchPrice = CurrencyValidation.formatCrore(String.valueOf(dataMap.get("STOCK_PURCHASE_PRICE")));
                    rowList.add(purchPrice);
                    mrp = CurrencyValidation.formatCrore(String.valueOf(dataMap.get("STOCK_MRP")));
                    rowList.add(mrp);
                    salesPrice = CurrencyValidation.formatCrore(String.valueOf(dataMap.get("STOCK_SALES_PRICE")));
                    rowList.add(salesPrice);
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("TOTAL_AMT")));
                    rowList.add(CommonUtil.convertObjToInt("0"));
                    rowList.add(CommonUtil.convertObjToDouble("0.0"));
                    dataMap.put("ROW_NO", CommonUtil.convertObjToInt(i+1));
                    tblRestoreDetails.addRow(rowList);
                    rowList = null;
               }
               setFinalList(restoreList);
           }
       }catch (Exception e) {
            parseException.logException(e, true);
        } 
    }
    
    public void doAction() {
        try {
            if (getActionType() != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (getAuthorizeMap() == null) {
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    if (getDeleteMap()!=null && getDeleteMap().size() > 0) {
                        data.put("DELETE_DATA", getDeleteMap());
                    }
                }
            data.put("RESTORE_DATA", setRestoreMap());
            if (getFinalList() != null && getFinalList().size() > 0) {
                data.put("RESTORE_LIST", getFinalList());
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put("RESTORE_DATA", setRestoreMap());
        }
        data.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        System.out.println("data in Sales OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        deleteMap = null;
        setResult(getActionType());
    }
    
    private String getCommand() {
        String command = null;
        switch (_actionType) {
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
    
    public HashMap setRestoreMap() {
        HashMap restoreMap = new HashMap();
        restoreMap.put("RESTORE_ID", CommonUtil.convertObjToStr(getLblRestoreID()));
        restoreMap.put("FROM_PROD_ID", CommonUtil.convertObjToStr(getFromProdID()));
        restoreMap.put("TO_PROD_ID", CommonUtil.convertObjToStr(getToProdID()));
        restoreMap.put("FROM_PV_ID", CommonUtil.convertObjToStr(getFromPvID()));
        restoreMap.put("TO_PV_ID", CommonUtil.convertObjToStr(getToPvID()));
        if (DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getFromDt())) != null) {
            restoreMap.put("FROM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getFromDt())));
        } else {
            restoreMap.put("FROM_DT", "");
        }
        if(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getToDt()))!=null){
            restoreMap.put("TO_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getToDt())));
        }else{
            restoreMap.put("TO_DT", "");
        }
        restoreMap.put("TOTAL_AMOUNT", CommonUtil.convertObjToStr(getRestoreTotAmt()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            restoreMap.put("STATUS", CommonConstants.STATUS_MODIFIED);
        } else if(getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            restoreMap.put("STATUS", CommonConstants.STATUS_CREATED);
        }
        restoreMap.put("STATUS_BY", TrueTransactMain.USER_ID);
        restoreMap.put("STATUS_DT", curDate);
        restoreMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        return restoreMap;
    }
    
    
    
    public void getData(HashMap whereMap) {
        try {
            HashMap restoreMap = new HashMap();
            ArrayList SubGroupRow = new ArrayList();
            String purchPrice = "";
            String mrp = "";
            String salesPrice = "";
            String restoreAmt = "";
            int diff = 0;
            Double tot_amt = 0.0;
            String Tot_Amt = "";
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
                if (data != null && data.containsKey("RESTORE_LIST")) {
                List restoreLst = (List) data.get("RESTORE_LIST");
                for (int i = 0; i < restoreLst.size(); i++) {
                    restoreMap = (HashMap) restoreLst.get(i);
                    SubGroupRow = new ArrayList();
                    SubGroupRow.add(new Boolean(true));
                    SubGroupRow.add(i+1);
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("PV_ID")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("PROD_ID")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("PROD_NAME")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("UNIT_TYPE")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("STOCK_ID")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("AVAIL_QTY")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("PV_QTY")));
                    SubGroupRow.add(CommonUtil.convertObjToStr(restoreMap.get("DIFF")));
                    purchPrice = CurrencyValidation.formatCrore(String.valueOf(restoreMap.get("STOCK_PURCHASE_PRICE")));
                    SubGroupRow.add(purchPrice);
                    mrp = CurrencyValidation.formatCrore(String.valueOf(restoreMap.get("STOCK_MRP")));
                    SubGroupRow.add(mrp);
                    salesPrice = CurrencyValidation.formatCrore(String.valueOf(restoreMap.get("STOCK_SALES_PRICE")));
                    SubGroupRow.add(salesPrice);
                    tot_amt = CommonUtil.convertObjToInt(restoreMap.get("DIFF")) * CommonUtil.convertObjToDouble(purchPrice);
                    Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(tot_amt));
                    SubGroupRow.add(Tot_Amt);
                    SubGroupRow.add(CommonUtil.convertObjToInt(restoreMap.get("RESTORE_QTY")));
                    restoreAmt = CurrencyValidation.formatCrore(String.valueOf(restoreMap.get("RESTORE_AMOUNT")));
                    SubGroupRow.add(restoreAmt);
                    restoreMap.put("ROW_NO", CommonUtil.convertObjToInt(i+1));
                    tblRestoreDetails.addRow(SubGroupRow);
                    SubGroupRow = null;
                }
                setFinalList(restoreLst);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    /** Return an ArrayList by executing Query **/
    public ArrayList getResultList(){
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public TableModel getTblRestoreDetails() {
        return tblRestoreDetails;
    }

    public void setTblRestoreDetails(TableModel tblRestoreDetails) {
        this.tblRestoreDetails = tblRestoreDetails;
    }

    public Boolean getNewData() {
        return newData;
    }

    public void setNewData(Boolean newData) {
        this.newData = newData;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }

    public String getFromProdID() {
        return fromProdID;
    }

    public void setFromProdID(String fromProdID) {
        this.fromProdID = fromProdID;
    }

    public String getToProdID() {
        return toProdID;
    }

    public void setToProdID(String toProdID) {
        this.toProdID = toProdID;
    }

    public String getFromPvID() {
        return fromPvID;
    }

    public void setFromPvID(String fromPvID) {
        this.fromPvID = fromPvID;
    }

    public String getToPvID() {
        return toPvID;
    }

    public void setToPvID(String toPvID) {
        this.toPvID = toPvID;
    }

    public String getFromDt() {
        return fromDt;
    }

    public void setFromDt(String fromDt) {
        this.fromDt = fromDt;
    }

    public String getToDt() {
        return toDt;
    }

    public void setToDt(String toDt) {
        this.toDt = toDt;
    }

    public String getLblRestoreID() {
        return lblRestoreID;
    }

    public void setLblRestoreID(String lblRestoreID) {
        this.lblRestoreID = lblRestoreID;
    }

    public HashMap getDeleteMap() {
        return deleteMap;
    }

    public void setDeleteMap(HashMap deleteMap) {
        this.deleteMap = deleteMap;
    }

    public String getRestoreTotAmt() {
        return restoreTotAmt;
    }

    public void setRestoreTotAmt(String restoreTotAmt) {
        this.restoreTotAmt = restoreTotAmt;
    }
    
    
    
}