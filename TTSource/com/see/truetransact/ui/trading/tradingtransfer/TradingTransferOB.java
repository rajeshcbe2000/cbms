/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigOB.java
 *
 * Created on Mon Jan 31 16:05:07 IST 2005
 */

package com.see.truetransact.ui.trading.tradingtransfer;

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
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.tradingtransfer.TradingTransferTO;
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

public class TradingTransferOB extends CObservable{
    Date curDate = null;
    private ArrayList key,value;
    private ProxyFactory proxy;
    private static TradingTransferOB objTradingTransferOB;
    private TradingTransferTO objTradingTransferTO;
    private HashMap map,keyValue,lookUpHash;
    private final static Logger log = Logger.getLogger(TradingTransferOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private String transferID = "";
    private String fromBranch = "";
    private String toBranch = "";
    private String tdtTransDt = "";
    private String achd = "";
    private String prodID = "";
    private String prodName = "";
    private String stockID = "";
    private String unitType = "";
    private String purchPrice = "";
    private String salesPrice = "";
    private String availQty = "";
    private String transferQty = "";
    private String totAmt = "";
    private String totQty = "";
    private LinkedHashMap transDetailsMap;
    private LinkedHashMap deleteTransMap;
    private EnhancedTableModel tblTransferDetails;
    final ArrayList tblTransDetailsTitle = new ArrayList();
    private Boolean newData = false;
    private HashMap authorizeMap;
//    private String tdsCeAchdId="";
    
    /** Consturctor Declaration  for  TDSConfigOB */
    private TradingTransferOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTblTransDetails();
            tblTransferDetails = new EnhancedTableModel(null, tblTransDetailsTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingTransferOB = new TradingTransferOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setTblTransDetails() {
        tblTransDetailsTitle.add("Sl No");
        tblTransDetailsTitle.add("Prod ID");
        tblTransDetailsTitle.add("Prod Name");
        tblTransDetailsTitle.add("Stock ID");
        tblTransDetailsTitle.add("Unit Type");
        tblTransDetailsTitle.add("Avail Qty");
        tblTransDetailsTitle.add("Purchase Price");
        tblTransDetailsTitle.add("Sales Price");
        tblTransDetailsTitle.add("Transfer Qty");
        tblTransDetailsTitle.add("Total Amt");
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingTransferJNDI");
        map.put(CommonConstants.HOME, "trading.tradingtransfer.TradingTransferHome");
        map.put(CommonConstants.REMOTE, "trading.tradingtransfer.TradingTransfer");
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
    
    public void resetMap(){
        transDetailsMap = null;
        deleteTransMap = null;
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    public void addDataToTransferDetailsTable(int rowSelected, boolean updateMode, HashMap stockMap) {
        try {
            int rowSel = rowSelected;
            final TradingTransferTO objTradingTransferTO = new TradingTransferTO();
            if (transDetailsMap == null) {
                transDetailsMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblTransferDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                 if (getNewData()) {
                    ArrayList data = tblTransferDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblTransferDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            objTradingTransferTO.setTransferID(CommonUtil.convertObjToStr(getTransferID()));
            objTradingTransferTO.setFromBranch(CommonUtil.convertObjToStr(getFromBranch()));
            objTradingTransferTO.setToBranch(CommonUtil.convertObjToStr(getToBranch()));
            objTradingTransferTO.setAchd(CommonUtil.convertObjToStr(getAchd()));
            objTradingTransferTO.setTransferDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtTransDt()))));
            objTradingTransferTO.setProdID(CommonUtil.convertObjToStr(getProdID()));
            objTradingTransferTO.setStockID(CommonUtil.convertObjToStr(getStockID()));
            objTradingTransferTO.setProd_Name(CommonUtil.convertObjToStr(getProdName()));
            objTradingTransferTO.setUnitType(CommonUtil.convertObjToStr(getUnitType()));
            objTradingTransferTO.setAvailQty(CommonUtil.convertObjToStr(getAvailQty()));
            objTradingTransferTO.setTransferQty(CommonUtil.convertObjToStr(getTransferQty()));
            objTradingTransferTO.setPurchasePrice(CommonUtil.convertObjToStr(getPurchPrice()));
            objTradingTransferTO.setSalesPrice(CommonUtil.convertObjToStr(getSalesPrice()));
            objTradingTransferTO.setTotAmt(CommonUtil.convertObjToStr(stockMap.get("TOTAL_AMT")));
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (getNewData()) {
                    objTradingTransferTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objTradingTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objTradingTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objTradingTransferTO.setStatusBy(TrueTransactMain.USER_ID);
            objTradingTransferTO.setBranchID(TrueTransactMain.BRANCH_ID);
            objTradingTransferTO.setSlNo(String.valueOf(slno));
            transDetailsMap.put(slno, objTradingTransferTO);
            String sno = String.valueOf(slno);
            updateTransDetailsTable(rowSel, sno, objTradingTransferTO,stockMap);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    
    }
    
    private void updateTransDetailsTable(int rowSel, String sno, TradingTransferTO objTradingTransferTO, HashMap stockMap) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        String purchPrice = "";
        String salesPrice = "";
        String mrp = "";
        int diff = 0;
        Double tot_amt = 0.0;
        String Tot_Amt = "";

        //If row already exists update it, else create a new row & append        
        for (int i = tblTransferDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblTransferDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList SalRow = new ArrayList();
                ArrayList data = tblTransferDetails.getDataArrayList();
                data.remove(rowSel);
                SalRow.add(sno);
                SalRow.add(CommonUtil.convertObjToStr(getProdID()));
                SalRow.add(CommonUtil.convertObjToStr(getProdName()));
                SalRow.add(CommonUtil.convertObjToStr(getStockID()));
                SalRow.add(CommonUtil.convertObjToStr(getUnitType()));
                SalRow.add(CommonUtil.convertObjToInt(getAvailQty()));
                purchPrice = CurrencyValidation.formatCrore(String.valueOf(getPurchPrice()));
                SalRow.add(purchPrice);
                salesPrice = CurrencyValidation.formatCrore(String.valueOf(getSalesPrice()));
                SalRow.add(salesPrice);
                SalRow.add(CommonUtil.convertObjToInt(getTransferQty()));
                SalRow.add(CommonUtil.convertObjToInt(stockMap.get("TOTAL_AMT")));
                tblTransferDetails.insertRow(rowSel, SalRow);
                SalRow = null;
            }
        }
        if (!rowExists) {
            ArrayList SalRow = new ArrayList();
            ArrayList data = tblTransferDetails.getDataArrayList();
            SalRow.add(sno);
            SalRow.add(CommonUtil.convertObjToStr(getProdID()));
            SalRow.add(CommonUtil.convertObjToStr(getProdName()));
            SalRow.add(CommonUtil.convertObjToStr(getStockID()));
            SalRow.add(CommonUtil.convertObjToStr(getUnitType()));
            SalRow.add(CommonUtil.convertObjToInt(getAvailQty()));
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(getPurchPrice()));
            SalRow.add(purchPrice);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(getSalesPrice()));
            SalRow.add(salesPrice);
            SalRow.add(CommonUtil.convertObjToInt(getTransferQty()));
            SalRow.add(CommonUtil.convertObjToInt(stockMap.get("TOTAL_AMT")));
            tblTransferDetails.insertRow(tblTransferDetails.getRowCount(), SalRow);
            SalRow = null;
        }
    }
    
    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt( tblTransferDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }
    
    public void populateTransTableDetails(int row) {
        try {
            resetForm();
            final TradingTransferTO objTradingTransferTO = (TradingTransferTO) transDetailsMap.get(row);
            populateTransDetailsData(objTradingTransferTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populateTransDetailsData(TradingTransferTO objTradingTransferTO) throws Exception {
        setProdID(CommonUtil.convertObjToStr(objTradingTransferTO.getProdID()));
        setProdName(CommonUtil.convertObjToStr(objTradingTransferTO.getProd_Name()));
        setStockID(CommonUtil.convertObjToStr(objTradingTransferTO.getStockID()));
        setUnitType(CommonUtil.convertObjToStr(objTradingTransferTO.getUnitType()));
        setPurchPrice(CommonUtil.convertObjToStr(objTradingTransferTO.getPurchasePrice()));
        setSalesPrice(CommonUtil.convertObjToStr(objTradingTransferTO.getSalesPrice()));
        setTransferQty(CommonUtil.convertObjToStr(objTradingTransferTO.getTransferQty()));
        setAvailQty(CommonUtil.convertObjToStr(objTradingTransferTO.getAvailQty()));
        setChanged();
        notifyObservers();
    }
    
    public void deleteTransDetails(int val, int row) {
        if (deleteTransMap == null) {
            deleteTransMap = new LinkedHashMap();
        }
        TradingTransferTO objTradingTransferTO = (TradingTransferTO) transDetailsMap.get(val);
        objTradingTransferTO.setStatus(CommonConstants.STATUS_DELETED);
        deleteTransMap.put(CommonUtil.convertObjToStr(tblTransferDetails.getValueAt(row, 0)), transDetailsMap.get(val));
        Object obj;
        obj = val;
        transDetailsMap.remove(val);
        resetTransferTblDetails();
        try {
            populateTransferTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
     
      private void populateTransferTable() throws Exception {
          ArrayList addList = new ArrayList(transDetailsMap.keySet());
          String purchPrice = "";
          String salesPrice = "";
          String mrp = "";
          int transQty = 0;
          double purchasePrice = 0.0;
          Double tot_amt = 0.0;
          String Tot_Amt = "";
          for (int i = 0; i < addList.size(); i++) {
            TradingTransferTO objTradingTransferTO = (TradingTransferTO) transDetailsMap.get(addList.get(i));
            ArrayList SalRow = new ArrayList();
            SalRow.add(objTradingTransferTO.getSlNo());
            SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getProdID()));
            SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getProd_Name()));
            SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getStockID()));
            SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getUnitType()));
            SalRow.add(CommonUtil.convertObjToInt(objTradingTransferTO.getAvailQty()));
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(objTradingTransferTO.getPurchasePrice()));
            SalRow.add(purchPrice);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(objTradingTransferTO.getSalesPrice()));
            SalRow.add(salesPrice);
            SalRow.add(CommonUtil.convertObjToInt(objTradingTransferTO.getTransferQty()));
            tot_amt = (CommonUtil.convertObjToInt(objTradingTransferTO.getTransferQty()))*(CommonUtil.convertObjToDouble(objTradingTransferTO.getPurchasePrice()));
            Tot_Amt = CommonUtil.convertObjToStr(tot_amt);
            Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(Tot_Amt));
            SalRow.add(Tot_Amt);
            tblTransferDetails.addRow(SalRow);
            SalRow = null;
        }
        notifyObservers();
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
                    if (deleteTransMap != null && deleteTransMap.size() > 0) {
                        data.put("DELETE_DATA", deleteTransMap);
                    }
                }
            if (transDetailsMap != null && transDetailsMap.size() > 0) {
                data.put("TRANSFER_DATA", transDetailsMap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            if (transDetailsMap != null && transDetailsMap.size() > 0) {
                data.put("TRANSFER_DATA", transDetailsMap);
            }
        }
        data.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        System.out.println("data in Sales OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        transDetailsMap = null;
        deleteTransMap = null;
        setResult(getActionType());
    }
    
    public void getData(HashMap whereMap) {
        try {
            String purchPrice = "";
            String salesPrice = "";
            String totAmt = "";
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            if (data.containsKey("TRANSFER_DATA")) {
                transDetailsMap = (LinkedHashMap) data.get("TRANSFER_DATA");
                ArrayList addList = new ArrayList(transDetailsMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingTransferTO objTradingTransferTO = (TradingTransferTO) transDetailsMap.get(addList.get(i));
                    objTradingTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    setTdtTransDt(CommonUtil.convertObjToStr(objTradingTransferTO.getTransferDt()));
                    setTransferID(CommonUtil.convertObjToStr(objTradingTransferTO.getTransferID()));
                    setFromBranch(CommonUtil.convertObjToStr(objTradingTransferTO.getFromBranch()));
                    setToBranch(CommonUtil.convertObjToStr(objTradingTransferTO.getToBranch()));
                    setAchd(CommonUtil.convertObjToStr(objTradingTransferTO.getAchd()));
                    ArrayList SalRow = new ArrayList();
                    SalRow.add(String.valueOf(i + 1));
                    SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getProdID()));
                    SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getProd_Name()));
                    SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getStockID()));
                    SalRow.add(CommonUtil.convertObjToStr(objTradingTransferTO.getUnitType()));
                    SalRow.add(CommonUtil.convertObjToDouble(objTradingTransferTO.getAvailQty()));
                    purchPrice = CurrencyValidation.formatCrore(String.valueOf(objTradingTransferTO.getPurchasePrice()));
                    SalRow.add(purchPrice);
                    salesPrice = CurrencyValidation.formatCrore(String.valueOf(objTradingTransferTO.getSalesPrice()));
                    SalRow.add(salesPrice);
                    SalRow.add(CommonUtil.convertObjToDouble(objTradingTransferTO.getTransferQty()));
                    totAmt = CurrencyValidation.formatCrore(String.valueOf(objTradingTransferTO.getTotAmt()));
                    SalRow.add(totAmt);
                    tblTransferDetails.addRow(SalRow);
                    SalRow = null;
                }
            }

        } catch (Exception e) {
            parseException.logException(e, true);
        }
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
    
    public static TradingTransferOB getInstance()throws Exception{
        return objTradingTransferOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public void resetTransferTblDetails(){
        tblTransferDetails.setDataArrayList(null, tblTransDetailsTitle);
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
        setProdID("");
        setProdName("");
        setStockID("");
        setUnitType("");
        setAvailQty("");
        setPurchPrice("");
        setSalesPrice("");
        setTransferQty("");
        setTotAmt("");
        notifyObservers();
    }
    
    public void resetMainDetails(){
        setFromBranch("");
        setToBranch("");
        setTdtTransDt("");
        setAchd("");
    }
    
    /** Return an ArrayList by executing Query **/
    public ArrayList getResultList(){
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getPurchPrice() {
        return purchPrice;
    }

    public void setPurchPrice(String purchPrice) {
        this.purchPrice = purchPrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getAvailQty() {
        return availQty;
    }

    public void setAvailQty(String availQty) {
        this.availQty = availQty;
    }

    public String getTransferQty() {
        return transferQty;
    }

    public void setTransferQty(String transferQty) {
        this.transferQty = transferQty;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public String getTotQty() {
        return totQty;
    }

    public void setTotQty(String totQty) {
        this.totQty = totQty;
    }

    public EnhancedTableModel getTblTransferDetails() {
        return tblTransferDetails;
    }

    public void setTblTransferDetails(EnhancedTableModel tblTransferDetails) {
        this.tblTransferDetails = tblTransferDetails;
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

    public String getTransferID() {
        return transferID;
    }

    public void setTransferID(String transferID) {
        this.transferID = transferID;
    }

    public String getFromBranch() {
        return fromBranch;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public String getToBranch() {
        return toBranch;
    }

    public void setToBranch(String toBranch) {
        this.toBranch = toBranch;
    }

    public String getTdtTransDt() {
        return tdtTransDt;
    }

    public void setTdtTransDt(String tdtTransDt) {
        this.tdtTransDt = tdtTransDt;
    }

    public String getAchd() {
        return achd;
    }

    public void setAchd(String achd) {
        this.achd = achd;
    }
    
    
    
}