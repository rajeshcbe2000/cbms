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

package com.see.truetransact.ui.trading.damage;

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
import com.see.truetransact.transferobject.trading.damage.DamageTO;
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

public class DamageOB extends CObservable{
    
    Date curDate = null;
    private String txtProdID = "";
    private String txtStockID = "";
    private String txtUnitType = "";
    private String txtDamageQty = "";
    private String txtAvailQty = "";
    private String txtDamageDt= "";
    private String txtDamageID= "";
    private ArrayList key,value;
    private ProxyFactory proxy;
    private static DamageOB objDamageOB;
    private HashMap map,keyValue,lookUpHash;
    private final static Logger log = Logger.getLogger(DamageOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private DamageTO objDamageTO;
    private LinkedHashMap damageDetailsMap;
    private LinkedHashMap deletedDamageMap;
    private EnhancedTableModel tblDamageDetails;
     final ArrayList tblDamageDetailsTitle = new ArrayList();
     private Boolean newData = false;
     private HashMap authorizeMap;
//    private String tdsCeAchdId="";
    
    /** Consturctor Declaration  for  DamageOB */
    private DamageOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTblDamageDetails();
            tblDamageDetails = new EnhancedTableModel(null, tblDamageDetailsTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public void setTblDamageDetails() {
        tblDamageDetailsTitle.add("Sl No");
        tblDamageDetailsTitle.add("Prod ID");
        tblDamageDetailsTitle.add("Stock ID");
        tblDamageDetailsTitle.add("Prod Name");
        tblDamageDetailsTitle.add("Type");
        tblDamageDetailsTitle.add("Purchase Price");
        tblDamageDetailsTitle.add("Sales Price");
        tblDamageDetailsTitle.add("Avail Qty");
        tblDamageDetailsTitle.add("Damage Qty");
        tblDamageDetailsTitle.add("Total Amt");
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objDamageOB = new DamageOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DamageJNDI");
        map.put(CommonConstants.HOME, "trading.damage.DamageHome");
        map.put(CommonConstants.REMOTE, "trading.damage.Damage");
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
    
    
    
    public static DamageOB getInstance()throws Exception{
        return objDamageOB;
    }
    
    public void addDataToDamageDetailsTable(int rowSelected, boolean updateMode, HashMap stockMap) {
        try {
            int rowSel = rowSelected;
            final DamageTO objDamageTO = new DamageTO();
            if (damageDetailsMap == null) {
                damageDetailsMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblDamageDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                 if (getNewData()) {
                    ArrayList data = tblDamageDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblDamageDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
//                int b = CommonUtil.convertObjToInt(tblDamageDetails.getValueAt(rowSelected, 0));
//                slno = b;
            }
            objDamageTO.setDamageID(CommonUtil.convertObjToStr(getTxtDamageID()));
            objDamageTO.setProdID(CommonUtil.convertObjToStr(getTxtProdID()));
            objDamageTO.setStockID(CommonUtil.convertObjToStr(getTxtStockID()));
            objDamageTO.setProdName(CommonUtil.convertObjToStr(stockMap.get("PRO_NAME")));
            objDamageTO.setUnitType(CommonUtil.convertObjToStr(getTxtUnitType()));
            objDamageTO.setDamageQty(CommonUtil.convertObjToStr(getTxtDamageQty()));
            objDamageTO.setDamageDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTxtDamageDt()))));
            objDamageTO.setAvailQty(CommonUtil.convertObjToStr(getTxtAvailQty()));
            objDamageTO.setPurchasePrice(CommonUtil.convertObjToStr(stockMap.get("STOCK_PURCHASE_PRICE")));
            objDamageTO.setSalesPrice(CommonUtil.convertObjToStr(stockMap.get("STOCK_SALES_PRICE")));
            objDamageTO.setTotAmt(CommonUtil.convertObjToStr(stockMap.get("TOTAL_AMOUNT")));
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (getNewData()) {
                    objDamageTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objDamageTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objDamageTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objDamageTO.setStatusBy(TrueTransactMain.USER_ID);
            objDamageTO.setStatusDt(curDate);
            objDamageTO.setBranchID(TrueTransactMain.BRANCH_ID);
            objDamageTO.setSlNo(String.valueOf(slno));
            damageDetailsMap.put(slno, objDamageTO);
            String sno = String.valueOf(slno);
            updateDamageDetailsTable(rowSel, sno, objDamageTO,stockMap);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void updateDamageDetailsTable(int rowSel, String sno, DamageTO objDamageTO,HashMap stockMap) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        String purchPrice = "";
        String salesPrice = "";
        String totAmt = "";
        //If row already exists update it, else create a new row & append        
        for (int i = tblDamageDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblDamageDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList SalRow = new ArrayList();
                ArrayList data = tblDamageDetails.getDataArrayList();
                data.remove(rowSel);
                SalRow.add(sno);
                SalRow.add(CommonUtil.convertObjToStr(getTxtProdID()));
                SalRow.add(CommonUtil.convertObjToStr(getTxtStockID()));
                SalRow.add(CommonUtil.convertObjToStr(stockMap.get("PRO_NAME")));
                SalRow.add(CommonUtil.convertObjToStr(getTxtUnitType()));
                purchPrice = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("STOCK_PURCHASE_PRICE")));
                SalRow.add(purchPrice);
                salesPrice = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("STOCK_SALES_PRICE")));
                SalRow.add(salesPrice);
                SalRow.add(CommonUtil.convertObjToDouble(getTxtAvailQty()));
                SalRow.add(CommonUtil.convertObjToInt(getTxtDamageQty()));
                totAmt = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("TOTAL_AMOUNT")));
                SalRow.add(totAmt);
                tblDamageDetails.insertRow(rowSel, SalRow);
                SalRow = null;
            }
        }
        if (!rowExists) {
            ArrayList SalRow = new ArrayList();
            ArrayList data = tblDamageDetails.getDataArrayList();
            SalRow.add(sno);
            SalRow.add(CommonUtil.convertObjToStr(getTxtProdID()));
            SalRow.add(CommonUtil.convertObjToStr(getTxtStockID()));
            SalRow.add(CommonUtil.convertObjToStr(stockMap.get("PRO_NAME")));
            SalRow.add(CommonUtil.convertObjToStr(getTxtUnitType()));
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("STOCK_PURCHASE_PRICE")));
            SalRow.add(purchPrice);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("STOCK_SALES_PRICE")));
            SalRow.add(salesPrice);
            SalRow.add(CommonUtil.convertObjToDouble(getTxtAvailQty()));
            SalRow.add(CommonUtil.convertObjToInt(getTxtDamageQty()));
            totAmt = CurrencyValidation.formatCrore(String.valueOf(stockMap.get("TOTAL_AMOUNT")));
            SalRow.add(totAmt);
            tblDamageDetails.insertRow(tblDamageDetails.getRowCount(), SalRow);
            SalRow = null;
        }
    }
    
    public void populateDamageTableDetails(int row) {
        try {
            resetForm();
            final DamageTO objDamageTO = (DamageTO) damageDetailsMap.get(row);
            populateDamageDetailsData(objDamageTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populateDamageDetailsData(DamageTO objDamageTO) throws Exception {
        setTxtProdID(CommonUtil.convertObjToStr(objDamageTO.getProdID()));
        setTxtStockID(CommonUtil.convertObjToStr(objDamageTO.getStockID()));
        setTxtUnitType(CommonUtil.convertObjToStr(objDamageTO.getUnitType()));
        setTxtDamageQty(CommonUtil.convertObjToStr(objDamageTO.getDamageQty()));
        setTxtAvailQty(CommonUtil.convertObjToStr(objDamageTO.getAvailQty()));
        setTxtDamageDt(CommonUtil.convertObjToStr(objDamageTO.getDamageDt()));
        setChanged();
        notifyObservers();
    }
    
    public void deletePurchaseDetails(int val, int row) {
        if (deletedDamageMap == null) {
            deletedDamageMap = new LinkedHashMap();
        }
        DamageTO objDamageTO = (DamageTO) damageDetailsMap.get(val);
        objDamageTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedDamageMap.put(CommonUtil.convertObjToStr(tblDamageDetails.getValueAt(row, 0)), damageDetailsMap.get(val));
        Object obj;
        obj = val;
        damageDetailsMap.remove(val);
        resetTblDetails();
        try {
            populateTradingDamageTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populateTradingDamageTable() throws Exception {
        ArrayList addList = new ArrayList(damageDetailsMap.keySet());
        String purchPrice = "";
        String salesPrice = "";
        String totAmt = "";
        for (int i = 0; i < addList.size(); i++) {
            DamageTO objDamageTO = (DamageTO) damageDetailsMap.get(addList.get(i));
            ArrayList SubGroupRow = new ArrayList();
//            SubGroupRow.add(objDamageTO.getSlNo());
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdID()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getStockID()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdName()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getUnitType()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getPurchasePrice()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getSalesPrice()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getAvailQty()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getDamageQty()));
//            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getTotAmt()));
            SubGroupRow.add(objDamageTO.getSlNo());
            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdID()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getStockID()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdName()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objDamageTO.getUnitType()));
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getPurchasePrice()));
            SubGroupRow.add(purchPrice);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getSalesPrice()));
            SubGroupRow.add(salesPrice);
            SubGroupRow.add(CommonUtil.convertObjToDouble(objDamageTO.getAvailQty()));
            SubGroupRow.add(CommonUtil.convertObjToDouble(objDamageTO.getDamageQty()));
            totAmt = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getTotAmt()));
            SubGroupRow.add(totAmt);
            tblDamageDetails.addRow(SubGroupRow);
            SubGroupRow = null;
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
                    if (deletedDamageMap != null && deletedDamageMap.size() > 0) {
                        data.put("DELETE_DATA", deletedDamageMap);
                    }
                }
            if (damageDetailsMap != null && damageDetailsMap.size() > 0) {
                data.put("DAMAGE_DATA", damageDetailsMap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            if (damageDetailsMap != null && damageDetailsMap.size() > 0) {
                data.put("DAMAGE_DATA", damageDetailsMap);
            }
        }
        data.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        System.out.println("data in Sales OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        setResult(getActionType());
    }
    
    public void getData(HashMap whereMap) {
        try {
            String purchPrice = "";
            String salesPrice = "";
            String totAmt = "";
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
            if (data.containsKey("DAMAGE_DATA")) {
                damageDetailsMap = (LinkedHashMap) data.get("DAMAGE_DATA");
                ArrayList addList = new ArrayList(damageDetailsMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    DamageTO objDamageTO = (DamageTO) damageDetailsMap.get(addList.get(i));
                    objDamageTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    setTxtDamageDt(CommonUtil.convertObjToStr(objDamageTO.getDamageDt()));
                    setTxtDamageID(CommonUtil.convertObjToStr(objDamageTO.getDamageID()));
                    ArrayList SalRow = new ArrayList();
                    SalRow.add(String.valueOf(i + 1));
                    
                    SalRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdID()));
                    SalRow.add(CommonUtil.convertObjToStr(objDamageTO.getStockID()));
                    SalRow.add(CommonUtil.convertObjToStr(objDamageTO.getProdName()));
                    SalRow.add(CommonUtil.convertObjToStr(objDamageTO.getUnitType()));
                    purchPrice = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getPurchasePrice()));
                    SalRow.add(purchPrice);
                    salesPrice = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getSalesPrice()));
                    SalRow.add(salesPrice);
                    SalRow.add(CommonUtil.convertObjToDouble(objDamageTO.getAvailQty()));
                    SalRow.add(CommonUtil.convertObjToDouble(objDamageTO.getDamageQty()));
                    totAmt = CurrencyValidation.formatCrore(String.valueOf(objDamageTO.getTotAmt()));
                    SalRow.add(totAmt);
                    tblDamageDetails.addRow(SalRow);
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
    
    
    
    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblDamageDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
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
        setTxtProdID("");
        setTxtUnitType("");
        setTxtAvailQty("");
        setTxtDamageQty("");
        setTxtDamageDt("");
        setTxtStockID("");
        notifyObservers();
    }
    
    public void resetTblDetails(){
        tblDamageDetails = new EnhancedTableModel(null, tblDamageDetailsTitle);
    }
    
   
    
    /** Return an ArrayList by executing Query **/
    public ArrayList getResultList(){
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public String getTxtProdID() {
        return txtProdID;
    }

    public void setTxtProdID(String txtProdID) {
        this.txtProdID = txtProdID;
    }

    public String getTxtStockID() {
        return txtStockID;
    }

    public void setTxtStockID(String txtStockID) {
        this.txtStockID = txtStockID;
    }

    public String getTxtUnitType() {
        return txtUnitType;
    }

    public void setTxtUnitType(String txtUnitType) {
        this.txtUnitType = txtUnitType;
    }

    public String getTxtDamageQty() {
        return txtDamageQty;
    }

    public void setTxtDamageQty(String txtDamageQty) {
        this.txtDamageQty = txtDamageQty;
    }

    public String getTxtAvailQty() {
        return txtAvailQty;
    }

    public void setTxtAvailQty(String txtAvailQty) {
        this.txtAvailQty = txtAvailQty;
    }

    public String getTxtDamageDt() {
        return txtDamageDt;
    }

    public void setTxtDamageDt(String txtDamageDt) {
        this.txtDamageDt = txtDamageDt;
    }

    public EnhancedTableModel getTblDamageDetails() {
        return tblDamageDetails;
    }

    public void setTblDamageDetails(EnhancedTableModel tblDamageDetails) {
        this.tblDamageDetails = tblDamageDetails;
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

    public String getTxtDamageID() {
        return txtDamageID;
    }

    public void setTxtDamageID(String txtDamageID) {
        this.txtDamageID = txtDamageID;
    }
    
    
    
    /**
     * Getter for property tdsCeAchdId.
     * @return Value of property tdsCeAchdId.
     */
//    public java.lang.String getTdsCeAchdId() {
//        return tdsCeAchdId;
//    }
//    
//    /**
//     * Setter for property tdsCeAchdId.
//     * @param tdsCeAchdId New value of property tdsCeAchdId.
//     */
//    public void setTdsCeAchdId(java.lang.String tdsCeAchdId) {
//        this.tdsCeAchdId = tdsCeAchdId;
//    }
    
}