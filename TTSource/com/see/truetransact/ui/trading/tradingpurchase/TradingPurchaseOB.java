/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingPurchaseOB.java
 *
 * Created on Mon Jan 31 16:05:07 IST 2005
 */
package com.see.truetransact.ui.trading.tradingpurchase;

import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseDetailsTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseReturnDetailsTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseReturnTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author Revathi L
 */
public class TradingPurchaseOB extends CObservable {

    private ComboBoxModel cbmBranchCode;
    private ComboBoxModel cbmSupplierName;
    private ComboBoxModel cbmVoucherNo;
    private ComboBoxModel cbmUnitType;
    private ComboBoxModel cbmPurchaseType;
    private ComboBoxModel cbmBankAcHead;
    private String cboBranchCode = "";
    private String cboSupplierName = "";
    private String cboVoucherNo = "";
    private String cboUnitType = "";
    private String cboPurchaseType = "";
    private String cboBankAcHead = "";
    Date curDate = null;
    private String txtPurchaseNo = "";
    private String txtBillNo = "";
    private String txtProductName = "";
    private String txtQty = "";
    private String stkQty = "";
    private String txtPuchaseTotal = "";
    private String txtQtyUnit = "";
    private String totQty = "";
    private String txtTax = "";
    private String txtDiscount = "";
    private String txtTPCharges = "";
    private String txtShrinkageQty = "";
    private String txtPurchasePrice = "";
    private String txtMRP = "";
    private String txtSalesPrice = "";
    private String txtParticulars = "";
    private String txtPlace = "";
    private String txtIndentNo = "";
    private Date voucherDt = null;
    private Date billDt = null;
    private Date expiryDt = null;
    private String chkShrinkage = "";
    private String chkFree = "";
    private String txtPurchaseReturn = "";
    private String txtPurchase = "";
    private String txtTaxTot = "";
    private String txtDiscTot = "";
    private String txtTPTot = "";
    private String txtTotal = "";
    private String txtPurchAmt = "";
    private String txtSalesAmt = "";
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static TradingPurchaseOB objTradingPurchaseOB;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(TradingPurchaseOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    // *****Purchase Return****
    private String txtPurchRetNo = "";
    private String txtPurchNo = "";
    private String txtPurchBillNo = "";
    private String txtPurchType = "";
    private String txtBankAcHead = "";
    private String txtPurchBankAcHead = "";
    private String txtPurch = "";
    private String txtReturn = "";
    private String txtMfgBatchID = "";
    private Date purchRetDt = null;
    private Date purchDt = null;
    private Date purchBillDt = null;
    final ArrayList tableTitle = new ArrayList();
    private TableModel tblPurchaseReturn;
    final ArrayList purchaseRetTableTitle = new ArrayList();
    private EnhancedTableModel tblPurchaseDetails;
    // private EnhancedTableModel tblPurchaseReturn;
    private TradingPurchaseTO objTradingPurchaseTO;
    private TradingPurchaseDetailsTO objTradingPurchaseDetailsTO;
    private TradingPurchaseReturnDetailsTO objTradingPurchaseReturnDetailsTO;
    private TradingPurchaseReturnTO objTradingPurchaseReturnTO;
    private LinkedHashMap purchaseDetailsMap;
    private LinkedHashMap returnDetailsMap;
    private HashMap purchaseMap;
    private HashMap authorizeMap;
    private Boolean newData = false;
    private LinkedHashMap deletedPurchaseMap = null;
    private LinkedHashMap totalPurchaseMap = null;
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    int stkQty1 = 0;
    int qty = 0;
    int totqty = 0;
    private List finalList = null;
    int pan = -1;
    private int PURCHASE = 1, PURCHASERETURN = 2;
    int panEditDelete = -1;
    // final ArrayList tblPurchaseDetailsTitle = new ArrayList();

    /**
     * Consturctor Declaration for TDSConfigOB
     */
    private TradingPurchaseOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTableTile();
            setPurchRetTblTile();
            tblPurchaseDetails = new EnhancedTableModel(null, tableTitle);
            tblPurchaseReturn = new TableModel(null, purchaseRetTableTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingPurchaseOB = new TradingPurchaseOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingPurchaseJNDI");
        map.put(CommonConstants.HOME, "trading.tradingpurchase.TradingPurchaseHome");
        map.put(CommonConstants.REMOTE, "trading.tradingpurchase.TradingPurchase");
    }

    public void cellPurchReturnEditableColumnTrue() {
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            tblPurchaseReturn.setEditColoumnNo(8);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Sl No");
        tableTitle.add("Prod Desc");
        tableTitle.add("Type");
        tableTitle.add("Tax");
        tableTitle.add("UnitP.Price");
        tableTitle.add("UnitMrp");
        tableTitle.add("UnitS.Price");
        tableTitle.add("StkQty");
        tableTitle.add("Qty");
        tableTitle.add("Qty/Unit");
        tableTitle.add("TotQty");
        tableTitle.add("Discount");
        tableTitle.add("Total");
    }

    private void setPurchRetTblTile() throws Exception {
        purchaseRetTableTitle.add("Sl No");
        purchaseRetTableTitle.add("Prod Name");
        purchaseRetTableTitle.add("Type");
        purchaseRetTableTitle.add("Tax");
        purchaseRetTableTitle.add("UnitP.Price");
        purchaseRetTableTitle.add("UnitS.Price");
        purchaseRetTableTitle.add("Purch Qty");
        purchaseRetTableTitle.add("Avail Qty");
        purchaseRetTableTitle.add("Return Qty");
        purchaseRetTableTitle.add("PTotal");
        purchaseRetTableTitle.add("RTotal");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        HashMap where = new HashMap();
        key = new ArrayList();
        value = new ArrayList();
        List supLst = (List) ClientUtil.executeQuery("getTradingSupplierName", null);
        getKeyValue(supLst);
        setCbmSupplierName(new ComboBoxModel(key, value));


        param.put(CommonConstants.MAP_NAME, null);
        lookupKey.add("UNIT_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("UNIT_TYPE"));
        cbmUnitType = new ComboBoxModel(key, value);

        param.put(CommonConstants.MAP_NAME, null);
        lookupKey.add("PURCHASE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("PURCHASE_TYPE"));
        cbmPurchaseType = new ComboBoxModel(key, value);
        key = null;
        value = null;
    }

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static TradingPurchaseOB getInstance() throws Exception {
        return objTradingPurchaseOB;
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public int getResult() {
        return _result;
    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
    public void resetPurchaseForm() {
        setTxtPurchaseNo("");
        setCboSupplierName("");
        setVoucherDt(null);
        setCboVoucherNo("");
        setTxtBillNo("");
        setBillDt(null);
        setTxtProductName("");
        setCboUnitType("");
        setTxtQty("");
        setTxtPuchaseTotal("");
        setTxtQtyUnit("");
        setTxtTax("");
        setTxtDiscount("");
        setTxtTPCharges("");
        setTxtShrinkageQty("");
        setTxtPurchasePrice("");
        setTxtMRP("");
        setTxtSalesPrice("");
        setExpiryDt(null);
        setTxtParticulars("");
        setTxtPlace("");
        setCboPurchaseType("");
        setCboBankAcHead("");
        setTxtIndentNo("");
        setCboBranchCode("");
        setTxtPurchaseReturn("");
        setTxtPurchase("");
        setTxtTaxTot("");
        setTxtDiscTot("");
        setTxtTPTot("");
        setTxtTotal("");
        setTxtPurchAmt("");
        setTxtSalesAmt("");
        tblPurchaseDetails.setDataArrayList(null, tableTitle);
        purchaseDetailsMap = null;
        deletedPurchaseMap = null;
        totalPurchaseMap = null;
        resetPurchaseTable();
        notifyObservers();

    }

    public void resetPurchaseRetForm() {
        setTxtPurchRetNo("");
        setCboSupplierName("");
        setTxtPurchNo("");
        setTxtBillNo("");
        setTxtPurchType("");
        setTxtBankAcHead("");
        setPurchRetDt(null);
        setPurchDt(null);
        setPurchBillDt(null);
        setTxtPurchase("");
        setTxtReturn("");
        tblPurchaseReturn.setDataArrayList(null, tableTitle);
        notifyObservers();
    }

    public void setCbmVoucherNo(String supplierID, String voucherDt) {
        try {
            if ((!supplierID.equals("")) && voucherDt.length() > 0) {
                HashMap queryWhereMap = new HashMap();
                queryWhereMap.put("SUPPLIER_ID", supplierID);
                queryWhereMap.put("FROM_DT", voucherDt);
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, null);
                final ArrayList lookup_keys = new ArrayList();
                if (!(getActionType() == ClientConstants.ACTIONTYPE_VIEW)) {
                    lookUpHash.put(CommonConstants.MAP_NAME, "getVoucherNo");
                } else {
                    lookUpHash.put(CommonConstants.MAP_NAME, "getVoucherNoEnquiry");
                }
                lookUpHash.put(CommonConstants.PARAMFORQUERY, queryWhereMap);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                fillData((HashMap) keyValue.get(CommonConstants.DATA));
                cbmVoucherNo = new ComboBoxModel(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setChanged();
    }

    public void addDataToSalesDetailsTable(int rowSelected, boolean updateMode, String prod_id) {
        try {
            int rowSel = rowSelected;
            final TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = new TradingPurchaseDetailsTO();
            if (purchaseDetailsMap == null) {
                purchaseDetailsMap = new LinkedHashMap();
            }

            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblPurchaseDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (getNewData()) {
                    ArrayList data = tblPurchaseDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblPurchaseDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (getNewData()) {
                    objTradingPurchaseDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objTradingPurchaseDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objTradingPurchaseDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objTradingPurchaseDetailsTO.setPurchaseNo(CommonUtil.convertObjToStr(getTxtPurchaseNo()));
            objTradingPurchaseDetailsTO.setProdName(CommonUtil.convertObjToStr(getTxtProductName()));
            objTradingPurchaseDetailsTO.setUnitType(CommonUtil.convertObjToStr(getCboUnitType()));
            objTradingPurchaseDetailsTO.setBillDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBillDt()))));
            objTradingPurchaseDetailsTO.setBillNo(CommonUtil.convertObjToStr(getTxtBillNo()));
            objTradingPurchaseDetailsTO.setVoucherNo(CommonUtil.convertObjToStr(getCboVoucherNo()));
            objTradingPurchaseDetailsTO.setPurchasePrice(CommonUtil.convertObjToStr(getTxtPurchasePrice()));
            objTradingPurchaseDetailsTO.setMrp(CommonUtil.convertObjToStr(getTxtMRP()));
            objTradingPurchaseDetailsTO.setSalesPrice(CommonUtil.convertObjToStr(getTxtSalesPrice()));
            objTradingPurchaseDetailsTO.setMfgBatchID(CommonUtil.convertObjToStr(getTxtMfgBatchID()));
            HashMap qtyMap = new HashMap();
            if (prod_id != null && prod_id.length() > 0) {
                qtyMap.put("PRODUCT_ID", prod_id);
                qtyMap.put("STOCK_TYPE", CommonUtil.convertObjToStr(getCboUnitType()));
                qtyMap.put("STOCK_PURCHASE_PRICE", getTxtPurchasePrice());
                qtyMap.put("STOCK_SALES_PRICE", getTxtSalesPrice());
                qtyMap.put("STOCK_MRP", getTxtMRP());
                List qtyList = ClientUtil.executeQuery("checkTradingStockQuant", qtyMap);
                if (qtyList != null && qtyList.size() > 0) {
                    qtyMap = (HashMap) qtyList.get(0);
                    stkQty1 = CommonUtil.convertObjToInt(qtyMap.get("STOCK_QUANT"));
                }
            }
            qty = CommonUtil.convertObjToInt(getTxtQty());
            totqty = stkQty1 + qty;
            objTradingPurchaseDetailsTO.setStkQty(CommonUtil.convertObjToStr(stkQty1));
            objTradingPurchaseDetailsTO.setQty(CommonUtil.convertObjToStr(qty));
            objTradingPurchaseDetailsTO.setQtyUnit(CommonUtil.convertObjToStr(getTxtQtyUnit()));
            objTradingPurchaseDetailsTO.setTotQty(CommonUtil.convertObjToStr(totqty));
            objTradingPurchaseDetailsTO.setDiscount(CommonUtil.convertObjToStr(getTxtDiscount()));
            objTradingPurchaseDetailsTO.setTotal(CommonUtil.convertObjToStr(getTxtPuchaseTotal()));
            objTradingPurchaseDetailsTO.setExpiry_Dt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getExpiryDt()))));
            objTradingPurchaseDetailsTO.setParticulars(CommonUtil.convertObjToStr(getTxtParticulars()));
            objTradingPurchaseDetailsTO.setSlNo(String.valueOf(slno));
            purchaseDetailsMap.put(slno, objTradingPurchaseDetailsTO);
            String sno = String.valueOf(slno);
            updatePurchaseDetailsTable(rowSel, sno, objTradingPurchaseDetailsTO, qty, stkQty1, totqty);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updatePurchaseDetailsTable(int rowSel, String sno, TradingPurchaseDetailsTO objTradingPurchaseDetailsTO, int qty, int stkQty1, int totqty) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblPurchaseDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblPurchaseDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList SalRow = new ArrayList();
                ArrayList data = tblPurchaseDetails.getDataArrayList();
                data.remove(rowSel);
                SalRow.add(sno);
                SalRow.add(CommonUtil.convertObjToStr(getTxtProductName()));
                SalRow.add(CommonUtil.convertObjToStr(getCboUnitType()));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtTax()));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtPurchasePrice()));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtMRP()));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtSalesPrice()));
                SalRow.add(CommonUtil.convertObjToInt(stkQty1));
                SalRow.add(CommonUtil.convertObjToDouble(qty));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtQtyUnit()));
                SalRow.add(CommonUtil.convertObjToDouble(totqty));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtDiscount()));
                SalRow.add(CommonUtil.convertObjToDouble(getTxtPuchaseTotal()));
                tblPurchaseDetails.insertRow(rowSel, SalRow);
                SalRow = null;
            }
        }
        if (!rowExists) {
            ArrayList SalRow = new ArrayList();
            ArrayList data = tblPurchaseDetails.getDataArrayList();
            SalRow.add(sno);
            SalRow.add(CommonUtil.convertObjToStr(getTxtProductName()));
            SalRow.add(CommonUtil.convertObjToStr(getCboUnitType()));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtTax()));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtPurchasePrice()));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtMRP()));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtSalesPrice()));
            SalRow.add(CommonUtil.convertObjToDouble(stkQty1));
            SalRow.add(CommonUtil.convertObjToDouble(qty));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtQtyUnit()));
            SalRow.add(CommonUtil.convertObjToDouble(totqty));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtDiscount()));
            SalRow.add(CommonUtil.convertObjToDouble(getTxtPuchaseTotal()));
            tblPurchaseDetails.insertRow(tblPurchaseDetails.getRowCount(), SalRow);
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
            a = CommonUtil.convertObjToInt(tblPurchaseDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void doAction(int pan) {
        try {
            if (getActionType() != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
                doActionPerform(pan);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform(int pan) throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (pan != -1 && pan == PURCHASE) {
            if (totalPurchaseMap == null) {
                totalPurchaseMap = new LinkedHashMap();
            }
            totalPurchaseMap.put(TO_NOT_DELETED_AT_UPDATE_MODE, purchaseDetailsMap);
            if (getAuthorizeMap() == null) {
                data.put("objTradingPurchaseTO", setTradingPurchaseTO());
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    if (deletedPurchaseMap != null && deletedPurchaseMap.size() > 0) {
                        data.put("DELETE_DATA", deletedPurchaseMap);
                    }
                }
            } else {
                data.put("objTradingPurchaseTO", "objTradingPurchaseTO");
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            }
            data.put("PURCHASE_DATA", totalPurchaseMap);
        } else if (pan != -1 && pan == PURCHASERETURN) {
            if (getAuthorizeMap() == null) {
                data.put("objTradingPurchaseReturnTO", setTradingPurchaseReturnTO());
//                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//                    if (deletedPurchaseMap != null && deletedPurchaseMap.size() > 0) {
//                        data.put("DELETE_DATA", deletedPurchaseMap);
//                    }
//                }
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            }
            if (getFinalList() != null && getFinalList().size() > 0) {
                data.put("RETURN_DATA", getFinalList());
            }
        }

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

    private TradingPurchaseTO setTradingPurchaseTO() {
        TradingPurchaseTO objTradingPurchaseTO = new TradingPurchaseTO();
        objTradingPurchaseTO.setPurchaseNo(CommonUtil.convertObjToStr(getTxtPurchaseNo()));
        objTradingPurchaseTO.setSupplier(CommonUtil.convertObjToStr(getCbmSupplierName().getKeyForSelected()));
        objTradingPurchaseTO.setVoucherDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getVoucherDt()))));
        objTradingPurchaseTO.setVoucherNo(CommonUtil.convertObjToStr(getCboVoucherNo()));
        objTradingPurchaseTO.setBillNo(CommonUtil.convertObjToStr(getTxtBillNo()));
        objTradingPurchaseTO.setPurchaseType(CommonUtil.convertObjToStr(getCbmPurchaseType().getKeyForSelected()));
        objTradingPurchaseTO.setBankAcHead(CommonUtil.convertObjToStr(getTxtBankAcHead()));
        objTradingPurchaseTO.setIndentNo(CommonUtil.convertObjToStr(getTxtIndentNo()));
        objTradingPurchaseTO.setBranchCode(CommonUtil.convertObjToStr(getCboBranchCode()));
        objTradingPurchaseTO.setPurchaseReturn(CommonUtil.convertObjToStr(getTxtPurchaseReturn()));
        objTradingPurchaseTO.setPurchaseTot(CommonUtil.convertObjToStr(getTxtPurchase()));
        objTradingPurchaseTO.setTaxTot(CommonUtil.convertObjToStr(getTxtTaxTot()));
        objTradingPurchaseTO.setTpTot(CommonUtil.convertObjToStr(getTxtTPTot()));
        objTradingPurchaseTO.setDiscTot(CommonUtil.convertObjToStr(getTxtDiscTot()));
        objTradingPurchaseTO.setTotal(CommonUtil.convertObjToStr(getTxtTotal()));
        objTradingPurchaseTO.setPurchaseAmt(CommonUtil.convertObjToStr(getTxtPurchAmt()));
        objTradingPurchaseTO.setSalesAmt(CommonUtil.convertObjToStr(getTxtSalesAmt()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingPurchaseTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingPurchaseTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingPurchaseTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingPurchaseTO.setStatusDt(curDate);
        return objTradingPurchaseTO;
    }

    private TradingPurchaseReturnTO setTradingPurchaseReturnTO() {
        TradingPurchaseReturnTO objTradingPurchaseReturnTO = new TradingPurchaseReturnTO();
        objTradingPurchaseReturnTO.setPurchaseRetNo(CommonUtil.convertObjToStr(getTxtPurchRetNo()));
        objTradingPurchaseReturnTO.setSupplierName(CommonUtil.convertObjToStr(getCbmSupplierName().getKeyForSelected()));
        objTradingPurchaseReturnTO.setPurchaseNo(CommonUtil.convertObjToStr(getTxtPurchNo()));
        objTradingPurchaseReturnTO.setBillNo(CommonUtil.convertObjToStr(getTxtPurchBillNo()));
        objTradingPurchaseReturnTO.setPurchaseType(CommonUtil.convertObjToStr(getTxtPurchType()));
        objTradingPurchaseReturnTO.setBankAcHead(CommonUtil.convertObjToStr(getTxtPurchBankAcHead()));
        objTradingPurchaseReturnTO.setPuchaseDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getPurchDt()))));
        objTradingPurchaseReturnTO.setCreatedDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getPurchRetDt()))));
        objTradingPurchaseReturnTO.setBillDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getPurchBillDt()))));
        objTradingPurchaseReturnTO.setPurchase(CommonUtil.convertObjToStr(getTxtPurch()));
        objTradingPurchaseReturnTO.setPurchReturn(CommonUtil.convertObjToStr(getTxtReturn()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingPurchaseReturnTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingPurchaseReturnTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingPurchaseReturnTO.setStatusBy(TrueTransactMain.USER_ID);
        return objTradingPurchaseReturnTO;
    }

    public void getData(HashMap whereMap, int panEditDelete) {
        try {
            if (panEditDelete == PURCHASE) {
                whereMap.put("PURCHASE", "PURCHASE");
            } else if (panEditDelete == PURCHASERETURN) {
                whereMap.put("PURCHASE_RETURN", "PURCHASE_RETURN");
            }
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            if (data != null && data.containsKey("objTradingPurchaseTO")) {
                objTradingPurchaseTO = (TradingPurchaseTO) ((List) data.get("objTradingPurchaseTO")).get(0);
                populateTradingPurchase(objTradingPurchaseTO);
                purchaseDetailsMap = new LinkedHashMap();
                if (data.containsKey("PURCHASE_DATA")) {
                    purchaseDetailsMap = (LinkedHashMap) data.get("PURCHASE_DATA");
                    ArrayList addList = new ArrayList(purchaseDetailsMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseDetailsMap.get(addList.get(i));
                        setTxtBillNo(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getBillNo()));
                        setBillDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getBillDt()))));
                        setTxtMfgBatchID(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getMfgBatchID()));
                        objTradingPurchaseDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        ArrayList SubGroupRow = new ArrayList();
                        SubGroupRow.add(String.valueOf(i + 1));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdName()));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getUnitType()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getTax()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getPurchasePrice()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getMrp()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getSalesPrice()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getStkQty()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getQty()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getQtyUnit()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getTotQty()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getDiscount()));
                        SubGroupRow.add(CommonUtil.convertObjToDouble(objTradingPurchaseDetailsTO.getTotal()));
                        tblPurchaseDetails.addRow(SubGroupRow);
                        SubGroupRow = null;

                    }
                }
            } else if (data != null && data.containsKey("objTradingPurchaseReturnTO")) {
                objTradingPurchaseReturnTO = (TradingPurchaseReturnTO) ((List) data.get("objTradingPurchaseReturnTO")).get(0);
                populateTradingPurchaseReturn(objTradingPurchaseReturnTO);
                returnDetailsMap = new LinkedHashMap();
                if (data.containsKey("RETURN_DATA")) {
                    populatePurchaseReturnDetData(data);
                    cellPurchReturnEditableColumnTrue();
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populatePurchaseReturnDetData(HashMap data) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            List purchRetLst = (List) data.get("RETURN_DATA");
            if (purchRetLst != null && purchRetLst.size() > 0) {
                for (int i = 0; i < purchRetLst.size(); i++) {
                    dataMap = (HashMap) purchRetLst.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SL_NO")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PRODUCT_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("UNIT_TYPE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("TAX")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("PURCHASE_PRICE")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("SALES_PRICE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PURCHASE_QTY")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("AVAIL_QTY")));
                    rowList.add(CommonUtil.convertObjToInt(dataMap.get("RETURN_QTY")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PURCH_TOTAL")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("RETURN_TOTAL")));
                    dataMap.put("QTY",CommonUtil.convertObjToStr(dataMap.get("PURCHASE_QTY")));
                    dataMap.put("STK_QTY", CommonUtil.convertObjToStr(dataMap.get("AVAIL_QTY")));
                    tableList.add(rowList);
                }
                tblPurchaseReturn = new TableModel(tableList, purchaseRetTableTitle);
                setFinalList(purchRetLst);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateSalesTableDetails(int row) {
        try {
            resetPurchaseTable();
            final TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseDetailsMap.get(row);
            populatePurchaseDetailsData(objTradingPurchaseDetailsTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateTradingPurchase(TradingPurchaseTO objTradingPurchaseTO) {
        HashMap mapData = null;
        try {
            setTxtPurchaseNo(CommonUtil.convertObjToStr(objTradingPurchaseTO.getPurchaseNo()));
            setCboSupplierName((String) getCbmSupplierName().getDataForKey(CommonUtil.convertObjToStr(objTradingPurchaseTO.getSupplier())));
            setVoucherDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseTO.getVoucherDt()))));
            setCbmVoucherNo(CommonUtil.convertObjToStr(objTradingPurchaseTO.getSupplier()), CommonUtil.convertObjToStr(getVoucherDt()));
            setCboVoucherNo(CommonUtil.convertObjToStr(objTradingPurchaseTO.getVoucherNo()));
            setTxtBankAcHead(CommonUtil.convertObjToStr(objTradingPurchaseTO.getBankAcHead()));
            setCboPurchaseType(CommonUtil.convertObjToStr(objTradingPurchaseTO.getPurchaseType()));
            setCboBranchCode(CommonUtil.convertObjToStr(objTradingPurchaseTO.getBranchCode()));
            setTxtIndentNo(CommonUtil.convertObjToStr(objTradingPurchaseTO.getIndentNo()));
            setTxtBillNo(CommonUtil.convertObjToStr(objTradingPurchaseTO.getBillNo()));
            setTxtPurchAmt(CommonUtil.convertObjToStr(objTradingPurchaseTO.getPurchaseAmt()));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public void populateTradingPurchaseReturn(TradingPurchaseReturnTO objTradingPurchaseReturnTO) {
        HashMap mapData = null;
        try {
            setTxtPurchRetNo(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPurchaseRetNo()));
            setCboSupplierName((String) getCbmSupplierName().getDataForKey(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getSupplierName())));
            setTxtPurchNo(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPurchaseNo()));
            setTxtPurchBillNo(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getBillNo()));
            setTxtPurchType(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPurchaseType()));
            setTxtPurchBankAcHead(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getBankAcHead()));
            setPurchRetDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getCreatedDt()))));
            setPurchBillDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getBillDt()))));
            setPurchDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPuchaseDt()))));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    private void populatePurchaseDetailsData(TradingPurchaseDetailsTO objTradingPurchaseDetailsTO) throws Exception {
        setTxtProductName(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdName()));
        setCboUnitType(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getUnitType()));
        setBillDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getBillDt()))));
        setTxtBillNo(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getBillNo()));
        setCboVoucherNo(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getVoucherNo()));
        setTxtTax(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTax()));
        setTxtPurchasePrice(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getPurchasePrice()));
        setTxtMRP(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getMrp()));
        setTxtSalesPrice(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getSalesPrice()));
        setTxtQty(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getQty()));
        setTxtMfgBatchID(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getMfgBatchID()));
        setTxtDiscount(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getDiscount()));
        setTxtPuchaseTotal(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTotal()));
        setTxtParticulars(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getParticulars()));
        setExpiryDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getExpiry_Dt()))));
        setChanged();
        notifyObservers();
    }

    public void populatePurchReturnData(String purchNo) {
        purchaseMap = new HashMap();
        ArrayList tableList = new ArrayList();
        purchaseMap.put("PURCHASE_NO", purchNo);
        List purchaseList = ClientUtil.executeQuery("getTradingPurchDetailsTO", purchaseMap);
        if (purchaseList != null && purchaseList.size() > 0) {
            for (int i = 0; i < purchaseList.size(); i++) {
                purchaseMap = (HashMap) purchaseList.get(i);
                ArrayList addList = new ArrayList(purchaseMap.keySet());
                // TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseList.get(i);
                ArrayList SubGroupRow = new ArrayList();
                SubGroupRow.add(String.valueOf(i + 1));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("PRODUCT_NAME")));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("UNIT_TYPE")));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("TAX")));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("PURCHASE_PRICE")));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("SALES_PRICE")));
                SubGroupRow.add(CommonUtil.convertObjToStr(purchaseMap.get("QTY")));
                String prodName = CommonUtil.convertObjToStr(purchaseMap.get("PRODUCT_NAME"));
                HashMap qtyMap = new HashMap();
                int retQty = 0;
                int qty = 0;
                int stkQty = 0;
                List stkLst = ClientUtil.executeQuery("getReturnQtyForStock", purchaseMap);
                if(stkLst != null && stkLst.size()>0){
                    qtyMap = (HashMap) stkLst.get(0);
                    qty = CommonUtil.convertObjToInt(purchaseMap.get("QTY"));
                    retQty = CommonUtil.convertObjToInt(qtyMap.get("RETURN_QTY"));
                    stkQty = qty-retQty;
                    SubGroupRow.add(CommonUtil.convertObjToInt(stkQty));
                }else{
                    SubGroupRow.add(CommonUtil.convertObjToDouble(purchaseMap.get("QTY")));
                }
                SubGroupRow.add(CommonUtil.convertObjToInt("0"));
                SubGroupRow.add(CommonUtil.convertObjToDouble(purchaseMap.get("TOTAL")));
                SubGroupRow.add(CommonUtil.convertObjToDouble("0"));
                tableList.add(SubGroupRow);
                SubGroupRow = null;
            }
            tblPurchaseReturn = new TableModel((ArrayList) tableList, purchaseRetTableTitle);
        }
        setFinalList(purchaseList);
        cellPurchReturnEditableColumnTrue();
    }

    public void deletePurchaseDetails(int val, int row) {
        if (deletedPurchaseMap == null) {
            deletedPurchaseMap = new LinkedHashMap();
        }
        TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseDetailsMap.get(val);
        objTradingPurchaseDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedPurchaseMap.put(CommonUtil.convertObjToStr(tblPurchaseDetails.getValueAt(row, 0)), purchaseDetailsMap.get(val));
        Object obj;
        obj = val;
        purchaseDetailsMap.remove(val);
        resetPurchasetableValues();
        try {
            populateTradingPurchaseTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTradingPurchaseTable() throws Exception {
        ArrayList addList = new ArrayList(purchaseDetailsMap.keySet());
        for (int i = 0; i < addList.size(); i++) {
            TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseDetailsMap.get(addList.get(i));
            ArrayList SubGroupRow = new ArrayList();
            SubGroupRow.add(objTradingPurchaseDetailsTO.getSlNo());
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdName()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getUnitType()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTax()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getPurchasePrice()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getMrp()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getSalesPrice()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getStkQty()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getQty()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getQtyUnit()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTotQty()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getDiscount()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTotal()));
            tblPurchaseDetails.addRow(SubGroupRow);
            SubGroupRow = null;
        }
        notifyObservers();
    }

    public void resetPurchaseTable() {
        setTxtProductName("");
        setCboUnitType("");
        setTxtTax("");
        setTxtPurchasePrice("");
        setTxtMRP("");
        setTxtSalesPrice("");
        setTxtQty("");
        setTxtDiscount("");
        setTxtPuchaseTotal("");
        setTxtPurchasePrice("");
        setTxtParticulars("");
        setExpiryDt(null);
    }

    public void resetPurchasetableValues() {
        tblPurchaseDetails.setDataArrayList(null, tableTitle);
    }

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public String getCboBranchCode() {
        return cboBranchCode;
    }

    public void setCboBranchCode(String cboBranchCode) {
        this.cboBranchCode = cboBranchCode;
    }

    public ComboBoxModel getCbmBranchCode() {
        return cbmBranchCode;
    }

    public void setCbmBranchCode(ComboBoxModel cbmBranchCode) {
        this.cbmBranchCode = cbmBranchCode;
    }

    public ComboBoxModel getCbmSupplierName() {
        return cbmSupplierName;
    }

    public void setCbmSupplierName(ComboBoxModel cbmSupplierName) {
        this.cbmSupplierName = cbmSupplierName;
    }

    public ComboBoxModel getCbmVoucherNo() {
        return cbmVoucherNo;
    }

    public void setCbmVoucherNo(ComboBoxModel cbmVoucherNo) {
        this.cbmVoucherNo = cbmVoucherNo;
    }

    public ComboBoxModel getCbmUnitType() {
        return cbmUnitType;
    }

    public void setCbmUnitType(ComboBoxModel cbmUnitType) {
        this.cbmUnitType = cbmUnitType;
    }

    public ComboBoxModel getCbmPurchaseType() {
        return cbmPurchaseType;
    }

    public void setCbmPurchaseType(ComboBoxModel cbmPurchaseType) {
        this.cbmPurchaseType = cbmPurchaseType;
    }

    public ComboBoxModel getCbmBankAcHead() {
        return cbmBankAcHead;
    }

    public void setCbmBankAcHead(ComboBoxModel cbmBankAcHead) {
        this.cbmBankAcHead = cbmBankAcHead;
    }

    public String getCboSupplierName() {
        return cboSupplierName;
    }

    public void setCboSupplierName(String cboSupplierName) {
        this.cboSupplierName = cboSupplierName;
    }

    public String getCboVoucherNo() {
        return cboVoucherNo;
    }

    public void setCboVoucherNo(String cboVoucherNo) {
        this.cboVoucherNo = cboVoucherNo;
    }

    public String getCboUnitType() {
        return cboUnitType;
    }

    public void setCboUnitType(String cboUnitType) {
        this.cboUnitType = cboUnitType;
    }

    public String getCboPurchaseType() {
        return cboPurchaseType;
    }

    public void setCboPurchaseType(String cboPurchaseType) {
        this.cboPurchaseType = cboPurchaseType;
    }

    public String getCboBankAcHead() {
        return cboBankAcHead;
    }

    public void setCboBankAcHead(String cboBankAcHead) {
        this.cboBankAcHead = cboBankAcHead;
    }

    public String getTxtPurchaseNo() {
        return txtPurchaseNo;
    }

    public void setTxtPurchaseNo(String txtPurchaseNo) {
        this.txtPurchaseNo = txtPurchaseNo;
    }

    public String getTxtBillNo() {
        return txtBillNo;
    }

    public void setTxtBillNo(String txtBillNo) {
        this.txtBillNo = txtBillNo;
    }

    public String getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(String txtProductName) {
        this.txtProductName = txtProductName;
    }

    public String getTxtQty() {
        return txtQty;
    }

    public void setTxtQty(String txtQty) {
        this.txtQty = txtQty;
    }

    public String getTxtPuchaseTotal() {
        return txtPuchaseTotal;
    }

    public void setTxtPuchaseTotal(String txtPuchaseTotal) {
        this.txtPuchaseTotal = txtPuchaseTotal;
    }

    public String getTxtQtyUnit() {
        return txtQtyUnit;
    }

    public void setTxtQtyUnit(String txtQtyUnit) {
        this.txtQtyUnit = txtQtyUnit;
    }

    public String getTxtTax() {
        return txtTax;
    }

    public void setTxtTax(String txtTax) {
        this.txtTax = txtTax;
    }

    public String getTxtDiscount() {
        return txtDiscount;
    }

    public void setTxtDiscount(String txtDiscount) {
        this.txtDiscount = txtDiscount;
    }

    public String getTxtTPCharges() {
        return txtTPCharges;
    }

    public void setTxtTPCharges(String txtTPCharges) {
        this.txtTPCharges = txtTPCharges;
    }

    public String getTxtShrinkageQty() {
        return txtShrinkageQty;
    }

    public void setTxtShrinkageQty(String txtShrinkageQty) {
        this.txtShrinkageQty = txtShrinkageQty;
    }

    public String getTxtPurchasePrice() {
        return txtPurchasePrice;
    }

    public void setTxtPurchasePrice(String txtPurchasePrice) {
        this.txtPurchasePrice = txtPurchasePrice;
    }

    public String getTxtMRP() {
        return txtMRP;
    }

    public void setTxtMRP(String txtMRP) {
        this.txtMRP = txtMRP;
    }

    public String getTxtSalesPrice() {
        return txtSalesPrice;
    }

    public void setTxtSalesPrice(String txtSalesPrice) {
        this.txtSalesPrice = txtSalesPrice;
    }

    public String getTxtParticulars() {
        return txtParticulars;
    }

    public void setTxtParticulars(String txtParticulars) {
        this.txtParticulars = txtParticulars;
    }

    public String getTxtPlace() {
        return txtPlace;
    }

    public void setTxtPlace(String txtPlace) {
        this.txtPlace = txtPlace;
    }

    public String getTxtIndentNo() {
        return txtIndentNo;
    }

    public void setTxtIndentNo(String txtIndentNo) {
        this.txtIndentNo = txtIndentNo;
    }

    public Date getVoucherDt() {
        return voucherDt;
    }

    public void setVoucherDt(Date voucherDt) {
        this.voucherDt = voucherDt;
    }

    public Date getBillDt() {
        return billDt;
    }

    public void setBillDt(Date billDt) {
        this.billDt = billDt;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public String getChkShrinkage() {
        return chkShrinkage;
    }

    public void setChkShrinkage(String chkShrinkage) {
        this.chkShrinkage = chkShrinkage;
    }

    public String getChkFree() {
        return chkFree;
    }

    public void setChkFree(String chkFree) {
        this.chkFree = chkFree;
    }

    public String getTxtPurchaseReturn() {
        return txtPurchaseReturn;
    }

    public void setTxtPurchaseReturn(String txtPurchaseReturn) {
        this.txtPurchaseReturn = txtPurchaseReturn;
    }

    public String getTxtPurchase() {
        return txtPurchase;
    }

    public void setTxtPurchase(String txtPurchase) {
        this.txtPurchase = txtPurchase;
    }

    public String getTxtTaxTot() {
        return txtTaxTot;
    }

    public void setTxtTaxTot(String txtTaxTot) {
        this.txtTaxTot = txtTaxTot;
    }

    public String getTxtDiscTot() {
        return txtDiscTot;
    }

    public void setTxtDiscTot(String txtDiscTot) {
        this.txtDiscTot = txtDiscTot;
    }

    public String getTxtTPTot() {
        return txtTPTot;
    }

    public void setTxtTPTot(String txtTPTot) {
        this.txtTPTot = txtTPTot;
    }

    public String getTxtTotal() {
        return txtTotal;
    }

    public void setTxtTotal(String txtTotal) {
        this.txtTotal = txtTotal;
    }

    public String getTxtPurchAmt() {
        return txtPurchAmt;
    }

    public void setTxtPurchAmt(String txtPurchAmt) {
        this.txtPurchAmt = txtPurchAmt;
    }

    public String getTxtSalesAmt() {
        return txtSalesAmt;
    }

    public void setTxtSalesAmt(String txtSalesAmt) {
        this.txtSalesAmt = txtSalesAmt;
    }

    public String getTxtPurchRetNo() {
        return txtPurchRetNo;
    }

    public void setTxtPurchRetNo(String txtPurchRetNo) {
        this.txtPurchRetNo = txtPurchRetNo;
    }

    public String getTxtPurchNo() {
        return txtPurchNo;
    }

    public void setTxtPurchNo(String txtPurchNo) {
        this.txtPurchNo = txtPurchNo;
    }

    public String getTxtPurchBillNo() {
        return txtPurchBillNo;
    }

    public void setTxtPurchBillNo(String txtPurchBillNo) {
        this.txtPurchBillNo = txtPurchBillNo;
    }

    public String getTxtPurchType() {
        return txtPurchType;
    }

    public void setTxtPurchType(String txtPurchType) {
        this.txtPurchType = txtPurchType;
    }

    public String getTxtBankAcHead() {
        return txtBankAcHead;
    }

    public void setTxtBankAcHead(String txtBankAcHead) {
        this.txtBankAcHead = txtBankAcHead;
    }

    public String getTxtPurch() {
        return txtPurch;
    }

    public void setTxtPurch(String txtPurch) {
        this.txtPurch = txtPurch;
    }

    public String getTxtReturn() {
        return txtReturn;
    }

    public void setTxtReturn(String txtReturn) {
        this.txtReturn = txtReturn;
    }

    public Date getPurchRetDt() {
        return purchRetDt;
    }

    public void setPurchRetDt(Date purchRetDt) {
        this.purchRetDt = purchRetDt;
    }

    public Date getPurchDt() {
        return purchDt;
    }

    public void setPurchDt(Date purchDt) {
        this.purchDt = purchDt;
    }

    public Date getPurchBillDt() {
        return purchBillDt;
    }

    public void setPurchBillDt(Date purchBillDt) {
        this.purchBillDt = purchBillDt;
    }

    public EnhancedTableModel getTblPurchaseDetails() {
        return tblPurchaseDetails;
    }

    public void setTblPurchaseDetails(EnhancedTableModel tblPurchaseDetails) {
        this.tblPurchaseDetails = tblPurchaseDetails;
    }

    public String getStkQty() {
        return stkQty;
    }

    public void setStkQty(String stkQty) {
        this.stkQty = stkQty;
    }

    public String getTotQty() {
        return totQty;
    }

    public void setTotQty(String totQty) {
        this.totQty = totQty;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public Boolean getNewData() {
        return newData;
    }

    public void setNewData(Boolean newData) {
        this.newData = newData;
    }

    public String getTxtMfgBatchID() {
        return txtMfgBatchID;
    }

    public void setTxtMfgBatchID(String txtMfgBatchID) {
        this.txtMfgBatchID = txtMfgBatchID;
    }

    public EnhancedTableModel getTblPurchaseReturn() {
        return tblPurchaseReturn;
    }

    public void setTblPurchaseReturn(TableModel tblPurchaseReturn) {
        this.tblPurchaseReturn = tblPurchaseReturn;
    }

    public String getTxtPurchBankAcHead() {
        return txtPurchBankAcHead;
    }

    public void setTxtPurchBankAcHead(String txtPurchBankAcHead) {
        this.txtPurchBankAcHead = txtPurchBankAcHead;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }
}