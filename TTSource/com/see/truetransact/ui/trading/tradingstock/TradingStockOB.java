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

package com.see.truetransact.ui.trading.tradingstock;

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
import com.see.truetransact.transferobject.trading.tradingstock.PhysicalVerificationTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.trading.tradingstock.TradingStockTO;
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

public class TradingStockOB extends CObservable{
    
    Date curDate = null;
    private ArrayList key,value;
    private ProxyFactory proxy;
    private static TradingStockOB objTradingStockOB;
    private HashMap map,keyValue,lookUpHash;
    private final static Logger log = Logger.getLogger(TradingStockOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result,_actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private TradingStockTO objTradingStockTO;
    private PhysicalVerificationTO objPhysicalVerificationTO;
    private LinkedHashMap stockDetailsMap;
    private LinkedHashMap PVDetailsMap;
    private LinkedHashMap PVStkDetailsMap;
    private LinkedHashMap deletedPVMap;
    private EnhancedTableModel tblStockDetails;
    private EnhancedTableModel tblPVDetails;
    private EnhancedTableModel tblPVStockDetails;
    private EnhancedTableModel tblDeficitStockDetails;
    private EnhancedTableModel tblRestorePvStockDetails;
    //private EnhancedTableModel tblRestoreStockDetails;
    private TableModel tblRestoreDetails;
    final ArrayList tblStockDetailsTitle = new ArrayList();
    final ArrayList tblPVDetailsTitle = new ArrayList();
    final ArrayList tblPVStkDetailsTitle = new ArrayList();
    final ArrayList tblRestorePvStockDetailsTitle = new ArrayList();
    final ArrayList tblRestoreStockDetailsTitle = new ArrayList();
    private String product_ID = "";
    private String prodName = "";
    private String stockID = "";
    private String unitType = "";
    private String purchPrice = "";
    private String mrp = "";
    private String salesPrice = "";
    private String avaiQty = "";
    private String phyQty = "";
    private String Remarks = "";
    private String pvDate = "";
    private String pvID = "";
    private String totAmt = "";
    private Boolean newData = false;
    private HashMap authorizeMap;
    int pan = -1;
    private int STOCK = 1, PVSTOCK = 2,DEFICITSTOCK = 3,RESTORESTOCK =4;
    int panEditDelete = -1;
    HashMap deficitStockMap =  new HashMap();
//    private String tdsCeAchdId="";
    
    /** Consturctor Declaration  for  TDSConfigOB */
    private TradingStockOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTblStockDetails();
            setTblPVDetails();
            setTblPVStkDetails();
            setTblRestorePVStkDetails();
            setTblRestoreDetails();
            tblStockDetails = new EnhancedTableModel(null, tblStockDetailsTitle);
            tblPVDetails = new EnhancedTableModel(null, tblPVDetailsTitle);
            tblPVStockDetails = new EnhancedTableModel(null, tblPVStkDetailsTitle);
            tblDeficitStockDetails = new EnhancedTableModel(null, tblPVDetailsTitle);
            tblRestorePvStockDetails = new EnhancedTableModel(null, tblRestorePvStockDetailsTitle);
            tblRestoreDetails = new TableModel(null, tblRestoreStockDetailsTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingStockOB = new TradingStockOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingStockJNDI");
        map.put(CommonConstants.HOME, "trading.tradingstock.TradingStockHome");
        map.put(CommonConstants.REMOTE, "trading.tradingstock.TradingStock");
    }
    
    public void setTblStockDetails() {
        tblStockDetailsTitle.add("Sl No");
        tblStockDetailsTitle.add("Prod ID");
        tblStockDetailsTitle.add("Prod Name");
        tblStockDetailsTitle.add("Unit Type");
        tblStockDetailsTitle.add("Stock ID");
        tblStockDetailsTitle.add("Stock Qty");
        tblStockDetailsTitle.add("Purchase Price");
        tblStockDetailsTitle.add("MRP");
        tblStockDetailsTitle.add("Sales Price");
    }
    
    public void setTblPVDetails() {
        tblPVDetailsTitle.add("Sl No");
        tblPVDetailsTitle.add("Prod ID");
        tblPVDetailsTitle.add("Prod Name");
        tblPVDetailsTitle.add("Unit Type");
        tblPVDetailsTitle.add("Stock ID");
        tblPVDetailsTitle.add("Avail Qty");
        tblPVDetailsTitle.add("Phy Qty");
        tblPVDetailsTitle.add("Diff/Excess");
        tblPVDetailsTitle.add("Purch Price");
        tblPVDetailsTitle.add("MRP");
        tblPVDetailsTitle.add("Sales Price");
        tblPVDetailsTitle.add("Tot_Amt");
    }
    
    public void setTblRestoreDetails() {
        tblRestoreStockDetailsTitle.add("Select");
        tblRestoreStockDetailsTitle.add("Sl No");
        tblRestoreStockDetailsTitle.add("Prod ID");
        tblRestoreStockDetailsTitle.add("Prod Name");
        tblRestoreStockDetailsTitle.add("Type");
        tblRestoreStockDetailsTitle.add("Stock ID");
        tblRestoreStockDetailsTitle.add("Avail Qty");
        tblRestoreStockDetailsTitle.add("Phy Qty");
        tblRestoreStockDetailsTitle.add("Diff");
        tblRestoreStockDetailsTitle.add("Restore Qty");
        tblRestoreStockDetailsTitle.add("Purch Price");
        tblRestoreStockDetailsTitle.add("MRP");
        tblRestoreStockDetailsTitle.add("Sales Price");
        tblRestoreStockDetailsTitle.add("Tot Amt");
        tblRestoreStockDetailsTitle.add("Restore Amt");
    }
    
    public void setTblPVStkDetails() {
        tblPVStkDetailsTitle.add("Sl No");
        tblPVStkDetailsTitle.add("PV ID");
        tblPVStkDetailsTitle.add("PV DATE");
        tblPVStkDetailsTitle.add("AMOUNT");
        tblPVStkDetailsTitle.add("TRAN_ID");
    }
    
    public void setTblRestorePVStkDetails() {
        tblRestorePvStockDetailsTitle.add("Sl No");
        tblRestorePvStockDetailsTitle.add("PV ID");
        tblRestorePvStockDetailsTitle.add("PV DATE");
        tblRestorePvStockDetailsTitle.add("PROD ID");
        tblRestorePvStockDetailsTitle.add("AMOUNT");
        tblRestorePvStockDetailsTitle.add("TRAN_ID");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            log.info("Inside FillDropDown");
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void getData(HashMap whereMap,int panEditDelete) {
        try {
            if (panEditDelete == STOCK) {
                whereMap.put("STOCK", "STOCK");
            } else if (panEditDelete == PVSTOCK) {
                whereMap.put("PVSTOCK", "PVSTOCK");
            }else if (panEditDelete == DEFICITSTOCK) {
                whereMap.put("DEFICITSTOCK", "DEFICITSTOCK");
            }else if (panEditDelete == RESTORESTOCK) {
                whereMap.put("RESTORESTOCK", "RESTORESTOCK");
            }
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
              stockDetailsMap = new LinkedHashMap();
                if (data.containsKey("STOCK_DETAILS")) {
                    stockDetailsMap = (LinkedHashMap) data.get("STOCK_DETAILS");
                    ArrayList addList = new ArrayList(stockDetailsMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TradingStockTO objTradingStockTO = (TradingStockTO) stockDetailsMap.get(addList.get(i));
                        ArrayList SalRow = new ArrayList();
                        SalRow.add(String.valueOf(i + 1));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingStockTO.getProduct_ID()));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingStockTO.getProd_Name()));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingStockTO.getStock_Type()));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingStockTO.getStockID()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Quant()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Purchase_Price()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingStockTO.getStock_MRP()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Sales_Price()));
                        tblStockDetails.addRow(SalRow);
                        SalRow = null;

                    }
           }else if (data.containsKey("PV_DATA")) {
                    HashMap stockMap = new HashMap();
                    String purchPrice = "";
                    String salesPrice = "";
                    String mrp = "";
                    int diff = 0;
                    Double tot_amt = 0.0;
                    String Tot_Amt = "";
                    if (PVDetailsMap == null) {
                        PVDetailsMap = new LinkedHashMap();
                    }
                    PVDetailsMap = (LinkedHashMap) data.get("PV_DATA");
                    ArrayList addList = new ArrayList(PVDetailsMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVDetailsMap.get(addList.get(i));
                        objPhysicalVerificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        setPvDate(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getPhy_Dt()));
                        setPvID(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getPhy_ID()));
                        ArrayList SalRow = new ArrayList();
                        SalRow.add(String.valueOf(i + 1));
                        SalRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProduct_ID()));
                        SalRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProd_Name()));
                        SalRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Type()));
                        SalRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStockID()));
                        SalRow.add(CommonUtil.convertObjToInt(objPhysicalVerificationTO.getStock_Quant()));
                        SalRow.add(CommonUtil.convertObjToInt(objPhysicalVerificationTO.getPhy_Qty()));
                        SalRow.add(CommonUtil.convertObjToInt(objPhysicalVerificationTO.getStock_Diff()));
                        purchPrice = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_Purchase_Price()));
                        SalRow.add(purchPrice);
                        mrp = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_MRP()));
                        SalRow.add(mrp);
                        salesPrice = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_Sales_Price()));
                        SalRow.add(salesPrice);
                        tot_amt = CommonUtil.convertObjToInt(objPhysicalVerificationTO.getStock_Diff()) * CommonUtil.convertObjToDouble(purchPrice);
                        Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(tot_amt));
                        SalRow.add(Tot_Amt);
                        tblPVDetails.addRow(SalRow);
                        SalRow = null;
                    }
           }else if (data != null && data.containsKey("PV_STOCK_DATA")) {
                HashMap pvMap = new HashMap();
                deficitStockMap =  new HashMap();
                ArrayList SalRow = new ArrayList();
                List pvLst = (List) data.get("PV_STOCK_DATA");
                for (int i = 0; i < pvLst.size(); i++) {
                    pvMap = (HashMap) pvLst.get(i);
                    SalRow = new ArrayList();
                    SalRow.add(String.valueOf(i + 1));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_ID")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_DT")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("AMOUNT")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("BATCH_ID")));
                    tblPVStockDetails.addRow(SalRow);
                    SalRow = null;
                }
                if(data.containsKey("DEFICIT_DATA")){
                    deficitStockMap =(HashMap) data.get("DEFICIT_DATA");
                }
                
                
            }else if (data != null && data.containsKey("RESTORE_PV_STOCK_DATA")) {
                HashMap pvMap = new HashMap();
                deficitStockMap =  new HashMap();
                ArrayList SalRow = new ArrayList();
                List pvLst = (List) data.get("RESTORE_PV_STOCK_DATA");
                for (int i = 0; i < pvLst.size(); i++) {
                    pvMap = (HashMap) pvLst.get(i);
                    SalRow = new ArrayList();
                    SalRow.add(String.valueOf(i + 1));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_ID")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_DT")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PROD_ID")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("AMOUNT")));
                    SalRow.add(CommonUtil.convertObjToStr(pvMap.get("BATCH_ID")));
                    tblRestorePvStockDetails.addRow(SalRow);
                    SalRow = null;
                }
                if(data.containsKey("RESTORE_DATA")){
                    deficitStockMap =(HashMap) data.get("RESTORE_DATA");
                }
                
                
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void displayDeficitStock(String pvId) {
        HashMap singleMap = new HashMap();
        if (deficitStockMap.containsKey(pvId)) {
            singleMap.put("STOCK_DATA", deficitStockMap.get(pvId));
            HashMap pvMap = new HashMap();
            ArrayList SalRow = new ArrayList();
            List pvLst = (List) singleMap.get("STOCK_DATA");
            for (int i = 0; i < pvLst.size(); i++) {
                pvMap = (HashMap) pvLst.get(i);
                SalRow = new ArrayList();
                SalRow.add(String.valueOf(i + 1));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PROD_ID")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PROD_NAME")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("UNIT_TYPE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_ID")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("AVAIL_QTY")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_QTY")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("DIFF")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_PURCHASE_PRICE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_MRP")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_SALES_PRICE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("TOTAL_AMT")));
                tblDeficitStockDetails.addRow(SalRow);
                SalRow = null;
            }
        }
    }
    
    public void displayRestoreStock(String pvId) {
        HashMap singleMap = new HashMap();
        if (deficitStockMap.containsKey(pvId)) {
            singleMap.put("STOCK_DATA", deficitStockMap.get(pvId));
            HashMap pvMap = new HashMap();
            ArrayList SalRow = new ArrayList();
            List pvLst = (List) singleMap.get("STOCK_DATA");
            for (int i = 0; i < pvLst.size(); i++) {
                pvMap = (HashMap) pvLst.get(i);
                SalRow = new ArrayList();
                SalRow.add(new Boolean(false));
                SalRow.add(String.valueOf(i + 1));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PROD_ID")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PROD_NAME")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("UNIT_TYPE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_ID")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("AVAIL_QTY")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("PV_QTY")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("DIFF")));
                SalRow.add(CommonUtil.convertObjToInt("0"));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_PURCHASE_PRICE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_MRP")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("STOCK_SALES_PRICE")));
                SalRow.add(CommonUtil.convertObjToStr(pvMap.get("TOTAL_AMT")));
                SalRow.add(CommonUtil.convertObjToInt("0"));
                tblRestoreDetails.addRow(SalRow);
                SalRow = null;
            }
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
                    if (deletedPVMap != null && deletedPVMap.size() > 0) {
                        data.put("DELETE_DATA", deletedPVMap);
                    }
                }
            if (PVDetailsMap != null && PVDetailsMap.size() > 0) {
                data.put("PV_DATA", PVDetailsMap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            if (PVDetailsMap != null && PVDetailsMap.size() > 0) {
                data.put("PV_DATA", PVDetailsMap);
            }
        }
        data.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        System.out.println("data in Sales OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
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
    
    public void addDataToPVDetailsTable(int rowSelected, boolean updateMode, HashMap stockMap) {
        try {
            int rowSel = rowSelected;
            final PhysicalVerificationTO objPhysicalVerificationTO = new PhysicalVerificationTO();
            if (PVDetailsMap == null) {
                PVDetailsMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblPVDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                 if (getNewData()) {
                    ArrayList data = tblPVDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblPVDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
//                int b = CommonUtil.convertObjToInt(tblDamageDetails.getValueAt(rowSelected, 0));
//                slno = b;
            }
            objPhysicalVerificationTO.setPhy_ID(CommonUtil.convertObjToStr(getPvID()));
            objPhysicalVerificationTO.setPhy_Dt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getPvDate()))));
            objPhysicalVerificationTO.setProduct_ID(CommonUtil.convertObjToStr(getProduct_ID()));
            objPhysicalVerificationTO.setStockID(CommonUtil.convertObjToStr(getStockID()));
            objPhysicalVerificationTO.setProd_Name(CommonUtil.convertObjToStr(getProdName()));
            objPhysicalVerificationTO.setStock_Type(CommonUtil.convertObjToStr(getUnitType()));
            objPhysicalVerificationTO.setPhy_Qty(CommonUtil.convertObjToStr(getPhyQty()));
            objPhysicalVerificationTO.setPhy_Dt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getPvDate()))));
            objPhysicalVerificationTO.setStock_Quant(CommonUtil.convertObjToStr(getAvaiQty()));
            objPhysicalVerificationTO.setStock_Purchase_Price(CommonUtil.convertObjToStr(getPurchPrice()));
            objPhysicalVerificationTO.setStock_MRP(CommonUtil.convertObjToStr(getMrp()));
            objPhysicalVerificationTO.setStock_Sales_Price(CommonUtil.convertObjToStr(getSalesPrice()));
            objPhysicalVerificationTO.setStock_Diff(CommonUtil.convertObjToStr(stockMap.get("Stock_Diff")));
            objPhysicalVerificationTO.setRemarks(CommonUtil.convertObjToStr(getRemarks()));
            Double totAmt = 0.0;
            totAmt = CommonUtil.convertObjToInt(stockMap.get("Stock_Diff"))*CommonUtil.convertObjToDouble(getPurchPrice());
            objPhysicalVerificationTO.setTotAmt(CommonUtil.convertObjToStr(totAmt));
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (getNewData()) {
                    objPhysicalVerificationTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objPhysicalVerificationTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objPhysicalVerificationTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objPhysicalVerificationTO.setStatusBy(TrueTransactMain.USER_ID);
            objPhysicalVerificationTO.setStatusDt(curDate);
            objPhysicalVerificationTO.setBranchID(TrueTransactMain.BRANCH_ID);
            objPhysicalVerificationTO.setSl_No(String.valueOf(slno));
            PVDetailsMap.put(slno, objPhysicalVerificationTO);
            String sno = String.valueOf(slno);
            updateDamageDetailsTable(rowSel, sno, objPhysicalVerificationTO,stockMap);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    
    }
    
    private void updateDamageDetailsTable(int rowSel, String sno, PhysicalVerificationTO objPhysicalVerificationTO,HashMap stockMap) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        String purchPrice = "";
        String salesPrice = "";
        String mrp = "";
        int diff = 0;
        Double tot_amt = 0.0;
        String Tot_Amt ="";
        
        //If row already exists update it, else create a new row & append        
        for (int i = tblPVDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblPVDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList SalRow = new ArrayList();
                ArrayList data = tblPVDetails.getDataArrayList();
                data.remove(rowSel);
                SalRow.add(sno);
                SalRow.add(CommonUtil.convertObjToStr(getProduct_ID()));
                SalRow.add(CommonUtil.convertObjToStr(getProdName()));
                SalRow.add(CommonUtil.convertObjToStr(getUnitType()));
                SalRow.add(CommonUtil.convertObjToStr(getStockID()));
                SalRow.add(CommonUtil.convertObjToInt(getAvaiQty()));
                SalRow.add(CommonUtil.convertObjToInt(getPhyQty()));
                SalRow.add(CommonUtil.convertObjToInt(stockMap.get("Stock_Diff")));
                purchPrice = CurrencyValidation.formatCrore(String.valueOf(getPurchPrice()));
                SalRow.add(purchPrice);
                mrp = CurrencyValidation.formatCrore(String.valueOf(getMrp()));
                SalRow.add(mrp);
                salesPrice = CurrencyValidation.formatCrore(String.valueOf(getSalesPrice()));
                SalRow.add(salesPrice);
                tot_amt = CommonUtil.convertObjToInt(stockMap.get("Stock_Diff"))* CommonUtil.convertObjToDouble(purchPrice);
                Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(tot_amt));
                SalRow.add(Tot_Amt);
                tblPVDetails.insertRow(rowSel, SalRow);
                SalRow = null;
            }
        }
        if (!rowExists) {
            ArrayList SalRow = new ArrayList();
            ArrayList data = tblPVDetails.getDataArrayList();
            SalRow.add(sno);
            SalRow.add(CommonUtil.convertObjToStr(getProduct_ID()));
            SalRow.add(CommonUtil.convertObjToStr(getProdName()));
            SalRow.add(CommonUtil.convertObjToStr(getUnitType()));
            SalRow.add(CommonUtil.convertObjToStr(getStockID()));
            SalRow.add(CommonUtil.convertObjToInt(getAvaiQty()));
            SalRow.add(CommonUtil.convertObjToInt(getPhyQty()));
            SalRow.add(CommonUtil.convertObjToInt(stockMap.get("Stock_Diff")));
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(getPurchPrice()));
            SalRow.add(purchPrice);
            mrp = CurrencyValidation.formatCrore(String.valueOf(getMrp()));
            SalRow.add(mrp);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(getSalesPrice()));
            SalRow.add(salesPrice);
            tot_amt = CommonUtil.convertObjToInt(stockMap.get("Stock_Diff")) * CommonUtil.convertObjToDouble(purchPrice);
            Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(tot_amt));
            SalRow.add(Tot_Amt);
            tblPVDetails.insertRow(tblPVDetails.getRowCount(), SalRow);
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
            a = CommonUtil.convertObjToInt( tblPVDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }
    
     public void populatePVTableDetails(int row) {
        try {
            resetForm();
            final PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVDetailsMap.get(row);
            populatePVDetailsData(objPhysicalVerificationTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populatePVDetailsData(PhysicalVerificationTO objPhysicalVerificationTO) throws Exception {
        setProdName(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProd_Name()));
        setProduct_ID(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProduct_ID()));
        setStockID(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStockID()));
        setUnitType(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Type()));
        setPurchPrice(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Purchase_Price()));
        setMrp(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_MRP()));
        setSalesPrice(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Sales_Price()));
        setPhyQty(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getPhy_Qty()));
        setAvaiQty(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Quant()));
        setRemarks(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getRemarks()));
        setChanged();
        notifyObservers();
    }
    
     public void deletePVDetails(int val, int row) {
        if (deletedPVMap == null) {
            deletedPVMap = new LinkedHashMap();
        }
        PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVDetailsMap.get(val);
        objPhysicalVerificationTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedPVMap.put(CommonUtil.convertObjToStr(tblPVDetails.getValueAt(row, 0)), PVDetailsMap.get(val));
        Object obj;
        obj = val;
        PVDetailsMap.remove(val);
        resetTblDetails();
        try {
            populatePVTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
     
      private void populatePVTable() throws Exception {
          ArrayList addList = new ArrayList(PVDetailsMap.keySet());
          String purchPrice = "";
          String salesPrice = "";
          String mrp = "";
          int availQty = 0;
          int phyQty = 0;
          int diff = 0;
          Double tot_amt = 0.0;
          String Tot_Amt = "";
          for (int i = 0; i < addList.size(); i++) {
            PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVDetailsMap.get(addList.get(i));
            ArrayList SubGroupRow = new ArrayList();
            SubGroupRow.add(objPhysicalVerificationTO.getSl_No());
            SubGroupRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProduct_ID()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProd_Name()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Type()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStockID()));
            SubGroupRow.add(CommonUtil.convertObjToDouble(objPhysicalVerificationTO.getStock_Quant()));
            SubGroupRow.add(CommonUtil.convertObjToDouble(objPhysicalVerificationTO.getPhy_Qty()));
            availQty = CommonUtil.convertObjToInt(objPhysicalVerificationTO.getStock_Quant());
            phyQty = CommonUtil.convertObjToInt(objPhysicalVerificationTO.getPhy_Qty());
            diff = availQty - phyQty;
            SubGroupRow.add(diff);
            purchPrice = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_Purchase_Price()));
            SubGroupRow.add(purchPrice);
            mrp = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_MRP()));
            SubGroupRow.add(mrp);
            salesPrice = CurrencyValidation.formatCrore(String.valueOf(objPhysicalVerificationTO.getStock_Sales_Price()));
            SubGroupRow.add(salesPrice);
            tot_amt = CommonUtil.convertObjToInt(objPhysicalVerificationTO.getStock_Diff()) * CommonUtil.convertObjToDouble(purchPrice);
            Tot_Amt = CurrencyValidation.formatCrore(String.valueOf(tot_amt));
            SubGroupRow.add(Tot_Amt);
            tblPVDetails.addRow(SubGroupRow);
            SubGroupRow = null;
        }
        notifyObservers();
    }
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static TradingStockOB getInstance()throws Exception{
        return objTradingStockOB;
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
    public void resetForm() {
        setPvID("");
        setPvDate("");
        //setProduct_ID("");
        setProdName("");
        setUnitType("");
        setStockID("");
        setSalesPrice("");
        setMrp("");
        setPurchPrice("");
        setAvaiQty("");
        setPhyQty("");
        setRemarks("");
        notifyObservers();
    }
    
    public void resetMap(){
        PVDetailsMap = null;
    }
    
    public void resetTblDetails() {
        tblStockDetails.setDataArrayList(null, tblStockDetailsTitle);
        tblPVDetails.setDataArrayList(null, tblPVDetailsTitle);
        tblPVStockDetails.setDataArrayList(null, tblPVStkDetailsTitle);
        tblDeficitStockDetails.setDataArrayList(null, tblPVDetailsTitle);
        tblRestorePvStockDetails.setDataArrayList(null, tblRestorePvStockDetailsTitle);
        tblRestoreDetails.setDataArrayList(null, tblRestoreStockDetailsTitle);
    }
    
    public void resetDeficitStockTblDetails(){
        tblDeficitStockDetails.setDataArrayList(null, tblPVDetailsTitle);
    }
    
    public void resetRestorePvStockTblDetails(){
        tblRestorePvStockDetails.setDataArrayList(null, tblRestorePvStockDetailsTitle);
    }
    
    public void resetRestoreStockTblDetails(){
         tblRestoreDetails.setDataArrayList(null, tblRestoreStockDetailsTitle);
    }
    
    public void cellRestoreEditableColumnTrue() {
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            tblRestoreDetails.setEditColoumnNo(9);
        }
    }
    
    public void cellRestoreEditableColumnFalse() {
       tblRestoreDetails.setEditColoumnNo(-1);
    }
    
    /** Return an ArrayList by executing Query **/
    public ArrayList getResultList(){
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }
    

    public EnhancedTableModel getTblStockDetails() {
        return tblStockDetails;
    }

    public void setTblStockDetails(EnhancedTableModel tblStockDetails) {
        this.tblStockDetails = tblStockDetails;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
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

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getAvaiQty() {
        return avaiQty;
    }

    public void setAvaiQty(String avaiQty) {
        this.avaiQty = avaiQty;
    }

    public String getPhyQty() {
        return phyQty;
    }

    public void setPhyQty(String phyQty) {
        this.phyQty = phyQty;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getPvDate() {
        return pvDate;
    }

    public void setPvDate(String pvDate) {
        this.pvDate = pvDate;
    }

    public String getPvID() {
        return pvID;
    }

    public void setPvID(String pvID) {
        this.pvID = pvID;
    }

    public Boolean getNewData() {
        return newData;
    }

    public void setNewData(Boolean newData) {
        this.newData = newData;
    }

    public EnhancedTableModel getTblPVDetails() {
        return tblPVDetails;
    }

    public void setTblPVDetails(EnhancedTableModel tblPVDetails) {
        this.tblPVDetails = tblPVDetails;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public EnhancedTableModel getTblPVStockDetails() {
        return tblPVStockDetails;
    }

    public void setTblPVStockDetails(EnhancedTableModel tblPVStockDetails) {
        this.tblPVStockDetails = tblPVStockDetails;
    }

    public HashMap getDeficitStockMap() {
        return deficitStockMap;
    }

    public void setDeficitStockMap(HashMap deficitStockMap) {
        this.deficitStockMap = deficitStockMap;
    }

    public EnhancedTableModel getTblDeficitStockDetails() {
        return tblDeficitStockDetails;
    }

    public void setTblDeficitStockDetails(EnhancedTableModel tblDeficitStockDetails) {
        this.tblDeficitStockDetails = tblDeficitStockDetails;
    }

    public EnhancedTableModel getTblRestorePvStockDetails() {
        return tblRestorePvStockDetails;
    }

    public void setTblRestorePvStockDetails(EnhancedTableModel tblRestorePvStockDetails) {
        this.tblRestorePvStockDetails = tblRestorePvStockDetails;
    }

    public TableModel getTblRestoreDetails() {
        return tblRestoreDetails;
    }

    public void setTblRestoreDetails(TableModel tblRestoreDetails) {
        this.tblRestoreDetails = tblRestoreDetails;
    }

   
    
    
    
    
}