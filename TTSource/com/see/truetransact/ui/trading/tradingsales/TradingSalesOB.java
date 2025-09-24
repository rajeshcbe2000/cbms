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
package com.see.truetransact.ui.trading.tradingsales;

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
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesMasterTO;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesTO;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesRetTO;
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
public class TradingSalesOB extends CObservable {

    Date curDate = null;
//    private String tdtStartDate = "";
//    private String tdtEndDate = "";
//    private String txtTdsId = "";
//    private String txtCutOfAmount = "";
//    private String cboScope = "";
//    private String cboCustType= "";
//    private String tdsCrAcHdId="";
//    private ComboBoxModel cbmScope;
//    private ComboBoxModel cbmCustType;
//    private String txtPercentage = "";
//    private boolean rdoCutOff_Yes = false;
//    private boolean rdoCutOff_No = false;
    private ComboBoxModel cbmSalesType;
    private ComboBoxModel cbmPlace;
    private ComboBoxModel cbmProdID;
    private ComboBoxModel cbmUnitType;
    private String cboSalesType;
    private String cboPlace;
    private String cboProdID;
    private String cboUnitType;
    private String txtEmpNo;
    private String txtName;
    private String txtSalesNo;
    private String tdtSalesDt;
    private String txtCreditAmt;
    private String txtProdDesc;
    private String txtACNo;
    private String txtBarCodeNumber;
    private String txtSalesPrice;
    private String txtProductName;
    private String txtMRP;
    private String txtQty;
    private String txtAvailQty;
    private String txtRemarks;
    private String txtCash;
    private String txtAdjust;
    private String txtDiscount;
    private String txtTotal;
    private String txtTranspotationCharges;
    private String txtGrandTotal;
    private String txtInstallmentNo;
    private String txtInstallmentAmt;
    private String txtFirstInstAmt;
    private String tdtSalesDate;
    private boolean chkInstallment;
    private String txtSalesretNo;
    private String tdtSalesReturnDate;
    private String txtSalesReturnCustId;
    private String tdtSalesReturnSalesDate;
    private String txtSalesReturnEmpNo;
    private String txtSalesReturnSalesNo;
    private String txtSalesReturnName;
    private String txtSalesRetSalType;
    private String txtSalesRetBankAcHD;
    private String txtTrChg;
    private String txtSales;
    private String txtReturn;
    private String txtSalesReprintNo;
    private String tdtSalesReprintSalesDate;
    private String txtSalesReprintName;
    private String txtSalTotal;
    private String stockID;
    final String YES = "Y";
    final String NO = "N";
    private String type = "";
    private Boolean ceil = false;
    private Boolean sal = false;
    private Boolean newData = false;
    private EnhancedTableModel tblSalesDetails;
    //private EnhancedTableModel tblSalesReturn;
    private TableModel tblSalesReturn;
    private HashMap authorizeMap;
    private TradingSalesMasterTO objTradingSalesMasterTO;
    private TradingSalesTO objTradingSalesTO;
    private TradingSalesRetTO objTradingSalesRetTO;
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static TradingSalesOB objTradingSalesOB;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(TradingSalesOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    final ArrayList tblSalesDetailsTitle = new ArrayList();
    final ArrayList tblSalesReturnTitle = new ArrayList();
    private ArrayList allTblDataList;
    private String billID = "";
    private String prodName = "";
    private String rate = "";
    private String unitType = "";
    private String qty = "";
    private String mrp = "";
    private String tax = "";
    private String taxableAmt = "";
    private String cash = "";
    private String adjust = "";
    private String total = "";
    private String transportationCharges = "";
    private String discount = "";
    private String discPercentage = "";
    private LinkedHashMap salesDetailsMap;
    double percent = 0.0;
    double totAmt = 0.0;
    double taxAmt = 0.0;
    private LinkedHashMap deletedSalesMap = null;
    int pan = -1;
    private int SALES = 1, SALESRETURN = 2;
    int panEditDelete = -1;
    private List finalList = null;

    /**
     * Consturctor Declaration for TDSConfigOB
     */
    private TradingSalesOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
            setTblSalesDetails();
            setTblSalesReturn();
            tblSalesDetails = new EnhancedTableModel(null, tblSalesDetailsTitle);
            tblSalesReturn = new TableModel(null, tblSalesReturnTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingSalesOB = new TradingSalesOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingSalesJNDI");
        map.put(CommonConstants.HOME, "trading.tradingsales.TradingSalesHome");
        map.put(CommonConstants.REMOTE, "trading.tradingsales.TradingSales");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
//        cbmScope = new ComboBoxModel();
//        cbmCustType = new ComboBoxModel();
        cbmSalesType = new ComboBoxModel();
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.JNDI, "LookUpJNDI");
        lookUpHash.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookUpHash.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("SALES_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        getKeyValue((HashMap) lookupValues.get("SALES_TYPE"));
        cbmSalesType = new ComboBoxModel(key, value);
//        key = new ArrayList();
//        value = new ArrayList();
//        List lst = (List) ClientUtil.executeQuery("getTradingPlace", null);
//        getMap(lst);
//        setCbmPlace(new ComboBoxModel(key, value));
        key = new ArrayList();
        value = new ArrayList();
        List prodLst = (List) ClientUtil.executeQuery("getSuspenseProdForSale", null);
        getMap(prodLst);
        setCbmProdID(new ComboBoxModel(key, value));
//        key = new ArrayList();
//        value = new ArrayList();
//        key = null;
//        value = null;
    }

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static TradingSalesOB getInstance() throws Exception {
        return objTradingSalesOB;
    }

    private void getMap(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
    }

    // Setter method for tdtStartDate
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

    public EnhancedTableModel getTblSalesDetails() {
        return tblSalesDetails;
    }

    public void setTblSalesDetails(EnhancedTableModel tblSalesDetails) {
        this.tblSalesDetails = tblSalesDetails;
    }

    public ArrayList getAllTblDataList() {
        return allTblDataList;
    }

    public void setAllTblDataList(ArrayList allTblDataList) {
        this.allTblDataList = allTblDataList;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getAdjust() {
        return adjust;
    }

    public void setAdjust(String adjust) {
        this.adjust = adjust;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getTransportationCharges() {
        return transportationCharges;
    }

    public void setTransportationCharges(String transportationCharges) {
        this.transportationCharges = transportationCharges;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
//    public void resetForm(){
//        setType("");
//        setTxtSalesNo("");
//        setCboSalesType("");
//        setTdtSalesDt("");
//        setCboPlace("");
//        setType("");
//        setTxtEmpNo("");
//        setTxtName("");
//        setCboProdID("");
//        setTxtProductName("");
//        setTxtProdDesc("");
//        setTxtACNo("");
//        setTxtBarCodeNumber("");
//        setTxtSalesPrice("");
//        setTxtMRP("");
//        setTxtQty("");
//        setTxtAvailQty("");
//        setTxtRemarks("");
//        setTxtCash("");
//        setTxtAdjust("");
//        setTxtDiscount("");
//        setTxtTotal("");
//        setTxtTranspotationCharges("");
//        setTxtGrandTotal("");
//        setTxtInstallmentNo("");
//        setTxtInstallmentAmt("");
//        setTxtFirstInstAmt("");
//        setTdtSalesDate("");
//        salesDetailsMap = null;
//        allTblDataList = null;
//        resetSalesDetailsTbl();
//        notifyObservers();
//        
//    }
    public void resetSalesForm() {
        setType("");
        setTxtSalesNo("");
        setCboSalesType("");
        setTdtSalesDt("");
        setCboPlace("");
        setType("");
        setTxtEmpNo("");
        setTxtName("");
        setCboProdID("");
        setTxtProductName("");
        setTxtProdDesc("");
        setTxtACNo("");
        setTxtBarCodeNumber("");
        setTxtSalesPrice("");
        setTxtMRP("");
        setTxtQty("");
        setTxtAvailQty("");
        setTxtRemarks("");
        setTxtCash("");
        setTxtAdjust("");
        setTxtDiscount("");
        setTxtTotal("");
        setTxtTranspotationCharges("");
        setTxtGrandTotal("");
        setTxtInstallmentNo("");
        setTxtInstallmentAmt("");
        setTxtFirstInstAmt("");
        setTdtSalesDate("");
        salesDetailsMap = null;
        allTblDataList = null;
        resetSalesDetailsTbl();
        notifyObservers();

    }

    public void resetSalesRetForm() {
        setType("");
        setTxtSalesNo("");
        setCboSalesType("");
        setTdtSalesDt("");
        setCboPlace("");
        setType("");
        setTxtEmpNo("");
        setTxtName("");
        setCboProdID("");
        setTxtProductName("");
        setTxtProdDesc("");
        setTxtACNo("");
        setTxtBarCodeNumber("");
        setTxtSalesPrice("");
        setTxtMRP("");
        setTxtQty("");
        setTxtAvailQty("");
        setTxtRemarks("");
        setTxtCash("");
        setTxtAdjust("");
        setTxtDiscount("");
        setTxtTotal("");
        setTxtTranspotationCharges("");
        setTxtGrandTotal("");
        setTxtInstallmentNo("");
        setTxtInstallmentAmt("");
        setTxtFirstInstAmt("");
        setTdtSalesDate("");
        salesDetailsMap = null;
        allTblDataList = null;
        resetSalesDetailsTbl();
        notifyObservers();

    }

    /* Populates the TO object by executing a Query */
    public void populateSalesData(TradingSalesMasterTO objTradingSalesMasterTO) {
        HashMap mapData = null;
        try {
            setTxtSalesNo(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getSalesNo()));
            setCboSalesType(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getSalesType()));
            setTdtSalesDt(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getSalesDate()));
            setCboPlace(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getPlace()));
            setType(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getMemberType()));
            setTxtEmpNo(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getEmpNo()));
            setTxtName(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getEmpName()));
            setTxtBarCodeNumber(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getBarcodeNumber()));
            setCboProdID(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getProdID()));
            setTxtProdDesc(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getProdDesc()));
            setTxtACNo(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getAcNo()));
            if (objTradingSalesMasterTO.getInstallment().equals(YES)) {
                setChkInstallment(true);
            } else {
                setChkInstallment(false);
            }
            setTxtInstallmentNo(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getInstallmentNo()));
            setTxtInstallmentAmt(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getInstallmentAmt()));
            setTxtFirstInstAmt(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getFirstInstAmt()));
            setTxtBarCodeNumber(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getBarcodeNumber()));
            setTdtSalesDate(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getInstDt()));
            setTxtGrandTotal(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getGrandTotal()));
            setTxtDiscount(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getDiscAmt()));
            setDiscPercentage(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getDiscount()));
            setTxtTranspotationCharges(CommonUtil.convertObjToStr(objTradingSalesMasterTO.getTransportationCharges()));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public void populateSalesReturnData(String salesNo) {
        HashMap salesRetMap = new HashMap();
        ArrayList tableList = new ArrayList();
        String value = "";
        salesRetMap.put("SALES_NO", salesNo);
        List salesRetList = ClientUtil.executeQuery("getSalesRetDataDetails", salesRetMap);
        if (salesRetList != null && salesRetList.size() > 0) {
            for (int i = 0; i < salesRetList.size(); i++) {
                salesRetMap = (HashMap) salesRetList.get(i);
                ArrayList addList = new ArrayList(salesRetMap.keySet());
                ArrayList SubGroupRow = new ArrayList();
                //SubGroupRow.add(String.valueOf(i + 1));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("SL_NO")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("PROD_NAME")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("RATE")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("UNIT_TYPE")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("QTY")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("TAX")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("TAXABLE_AMOUNT")));
                SubGroupRow.add(CommonUtil.convertObjToStr(salesRetMap.get("STOCK_ID")));
                value = CommonUtil.convertObjToStr(salesRetMap.get("TAXABLE_AMOUNT"));
                value = value.replace(",", "");
                SubGroupRow.add(value);
//                String prodName = CommonUtil.convertObjToStr(salesRetMap.get("PRODUCT_NAME"));
//                HashMap qtyMap = new HashMap();
//                int retQty = 0;
//                int qty = 0;
//                int stkQty = 0;
//                List stkLst = ClientUtil.executeQuery("getReturnQtyForStock", purchaseMap);
//                if(stkLst != null && stkLst.size()>0){
//                    qtyMap = (HashMap) stkLst.get(0);
//                    System.out.println("Return Qty :" +qtyMap);
//                    qty = CommonUtil.convertObjToInt(purchaseMap.get("QTY"));
//                    retQty = CommonUtil.convertObjToInt(qtyMap.get("RETURN_QTY"));
//                    stkQty = qty-retQty;
//                    SubGroupRow.add(CommonUtil.convertObjToInt(stkQty));
//                }else{
//                    SubGroupRow.add(CommonUtil.convertObjToDouble(purchaseMap.get("QTY")));
//                }
                SubGroupRow.add(CommonUtil.convertObjToInt("0"));
                SubGroupRow.add(CommonUtil.convertObjToDouble("0"));
                tableList.add(SubGroupRow);
                SubGroupRow = null;
            }
            tblSalesReturn = new TableModel((ArrayList) tableList, tblSalesReturnTitle);
        }
        setFinalList(salesRetList);
        cellSalesReturnEditableColumnTrue();
    }

    public void cellSalesReturnEditableColumnTrue() {
        double amt = 0.0;
        double taxableAmt = 0.0;
        double tax = 0.0;
        double tot = 0.0;
        String value = "";
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            tblSalesReturn.setEditColoumnNo(9);
        }
        for (int i = 0; i <= tblSalesReturn.getRowCount() - 1; i++) {
            amt = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(i, 2));
            value = CurrencyValidation.formatCrore(String.valueOf(amt));
            tblSalesReturn.setValueAt(value, i, 2);
            if (CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(i, 5)).length() > 0) {
                taxableAmt = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(i, 5));
                value = CurrencyValidation.formatCrore(String.valueOf(taxableAmt));
                tblSalesReturn.setValueAt(value, i, 5);
            } else {
                tblSalesReturn.setValueAt("0.00", i, 5);
            }
            tax = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(i, 6));
            value = CurrencyValidation.formatCrore(String.valueOf(tax));
            tblSalesReturn.setValueAt(value, i, 6);
            tot = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(i, 8));
            value = CurrencyValidation.formatCrore(String.valueOf(tot));
            tblSalesReturn.setValueAt(value, i, 8);
        }
    }

    public void addDataToSalesDetailsTable(int rowSelected, boolean updateMode, String tax) {
        try {
            int rowSel = rowSelected;
            final TradingSalesTO objTradingSalesTO = new TradingSalesTO();
            if (salesDetailsMap == null) {
                salesDetailsMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblSalesDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                int b = CommonUtil.convertObjToInt(tblSalesDetails.getValueAt(rowSelected, 0));
                slno = b;
            }
            objTradingSalesTO.setSalesNo(CommonUtil.convertObjToStr(getTxtSalesNo()));
            objTradingSalesTO.setProdName(CommonUtil.convertObjToStr(getProdName()));
            objTradingSalesTO.setRate(CommonUtil.convertObjToStr(getRate()));
            objTradingSalesTO.setUnitType(CommonUtil.convertObjToStr(getCboUnitType()));
            objTradingSalesTO.setQty(CommonUtil.convertObjToStr(getQty()));
            objTradingSalesTO.setMrp(CommonUtil.convertObjToStr(getMrp()));
            if (tax != null && tax.length() > 0) {
                double percent = CommonUtil.convertObjToDouble(tax);
                double totAmt = CommonUtil.convertObjToDouble(getTaxableAmt());
                double taxAmt = (percent * totAmt / 100);
                Rounding rod = new Rounding();
                taxAmt = (double) rod.getNearest((long) (taxAmt * 100), 100) / 100;
                objTradingSalesTO.setTax(CommonUtil.convertObjToStr(taxAmt));
                double total = totAmt + taxAmt;
                setTax(CommonUtil.convertObjToStr(taxAmt));
                setTotal(CommonUtil.convertObjToStr(total));
                setCash(CommonUtil.convertObjToStr(total));
                objTradingSalesTO.setCash(CommonUtil.convertObjToStr(getCash()));
                objTradingSalesTO.setTotal(CommonUtil.convertObjToStr(getTotal()));
            } else {
                objTradingSalesTO.setTax(CommonUtil.convertObjToStr(getTax()));
                objTradingSalesTO.setCash(CommonUtil.convertObjToStr(getCash()));
                objTradingSalesTO.setTotal(CommonUtil.convertObjToStr(getTotal()));
            }
            objTradingSalesTO.setTaxableAmt(CommonUtil.convertObjToStr(getTaxableAmt()));
            objTradingSalesTO.setAdjust(CommonUtil.convertObjToStr(getAdjust()));
            objTradingSalesTO.setTransportationCharges(CommonUtil.convertObjToStr(getTransportationCharges()));
            objTradingSalesTO.setDiscount(CommonUtil.convertObjToStr(getDiscount()));
            objTradingSalesTO.setStockID(CommonUtil.convertObjToStr(getStockID()));
            objTradingSalesTO.setStatus(CommonUtil.convertObjToStr("CREATED"));
            objTradingSalesTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objTradingSalesTO.setSlNo(String.valueOf(slno));
            salesDetailsMap.put(slno, objTradingSalesTO);
            String sno = String.valueOf(slno);
            updateSalesDetailsTable(rowSel, sno, objTradingSalesTO);
            notifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateSalesDetailsTable(int rowSel, String sno, TradingSalesTO objTradingSalesTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblSalesDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblSalesDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList SalRow = new ArrayList();
                ArrayList data = tblSalesDetails.getDataArrayList();
                data.remove(rowSel);
                SalRow.add(sno);
                SalRow.add(CommonUtil.convertObjToStr(getProdName()));
                SalRow.add(CommonUtil.convertObjToStr(getRate()));
                SalRow.add(CommonUtil.convertObjToDouble(getUnitType()));
                SalRow.add(CommonUtil.convertObjToDouble(getQty()));
                SalRow.add(CommonUtil.convertObjToDouble(getMrp()));
                SalRow.add(CommonUtil.convertObjToDouble(getTaxableAmt()));
                SalRow.add(CommonUtil.convertObjToDouble(getTax()));
                SalRow.add(CommonUtil.convertObjToDouble(getCash()));
                SalRow.add(CommonUtil.convertObjToDouble(getAdjust()));
                SalRow.add(CommonUtil.convertObjToDouble(getTotal()));
                tblSalesDetails.insertRow(rowSel, SalRow);
                SalRow = null;
            }
        }
        if (!rowExists) {
            ArrayList SalRow = new ArrayList();
            ArrayList data = tblSalesDetails.getDataArrayList();
            SalRow.add(sno);
            SalRow.add(CommonUtil.convertObjToStr(getProdName()));
            SalRow.add(CommonUtil.convertObjToStr(getRate()));
            SalRow.add(CommonUtil.convertObjToDouble(getUnitType()));
            SalRow.add(CommonUtil.convertObjToDouble(getQty()));
            SalRow.add(CommonUtil.convertObjToDouble(getMrp()));
            SalRow.add(CommonUtil.convertObjToDouble(getTaxableAmt()));
            SalRow.add(CommonUtil.convertObjToDouble(getTax()));
            SalRow.add(CommonUtil.convertObjToDouble(getCash()));
            SalRow.add(CommonUtil.convertObjToDouble(getAdjust()));
            SalRow.add(CommonUtil.convertObjToDouble(getTotal()));
            tblSalesDetails.insertRow(tblSalesDetails.getRowCount(), SalRow);
            SalRow = null;
        }
    }

    private String calculateTotalValue(ArrayList data) {
        final int column = 10;
        String returnTotalAmt = "";
        try {
            double totalAmount = 0.0;
            ArrayList rowData = new ArrayList();
            for (int i = 0, j = allTblDataList.size(); i < j; i++) {
                rowData = (ArrayList) allTblDataList.get(i);
                totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData.get(column)));
                rowData = null;
            }
            returnTotalAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));

        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return returnTotalAmt;
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblSalesDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void setTblSalesDetails() {
        tblSalesDetailsTitle.add("Sl No");
        tblSalesDetailsTitle.add("Prod Name");
        tblSalesDetailsTitle.add("Rate");
        tblSalesDetailsTitle.add("Unit Type");
        tblSalesDetailsTitle.add("Qty");
        tblSalesDetailsTitle.add("MRP");
        tblSalesDetailsTitle.add("Taxable Amt");
        tblSalesDetailsTitle.add("Tax");
        tblSalesDetailsTitle.add("Cash");
        tblSalesDetailsTitle.add("Adjust");
        tblSalesDetailsTitle.add("Total");
    }

    public void setTblSalesReturn() {
        tblSalesReturnTitle.add("Sl No");
        tblSalesReturnTitle.add("Prod Name");
        tblSalesReturnTitle.add("Rate");
        tblSalesReturnTitle.add("Unit Type");
        tblSalesReturnTitle.add("Qty");
        tblSalesReturnTitle.add("Taxable Amt");
        tblSalesReturnTitle.add("Tax");
        tblSalesReturnTitle.add("Stock ID");
        tblSalesReturnTitle.add("PTotal");
        tblSalesReturnTitle.add("Ret Qty");
        tblSalesReturnTitle.add("RTotal");
    }

    /* Executes Query using the TO object */
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
        if (pan != -1 && pan == SALES) {
            if (getAuthorizeMap() == null) {
                data.put("objTradingSalesMasterTO", setTradingSalesMasterTO());
                if (salesDetailsMap != null && salesDetailsMap.size() > 0) {
                    data.put("SALES_DATA", salesDetailsMap);
                }
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                if (salesDetailsMap != null && salesDetailsMap.size() > 0) {
                    data.put("SALES_DATA", salesDetailsMap);
                }
            }
        } else if (pan != -1 && pan == SALESRETURN) {
            if (getAuthorizeMap() == null) {
                data.put("objTradingSalesReturnTO", setTradingSalesReturnTO());
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

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public void resetSalesDetailsTbl() {
        tblSalesDetails.setDataArrayList(null, tblSalesDetailsTitle);
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

    private TradingSalesMasterTO setTradingSalesMasterTO() {
        TradingSalesMasterTO objTradingSalesMasterTO = new TradingSalesMasterTO();
        objTradingSalesMasterTO.setSalesNo(CommonUtil.convertObjToStr(getTxtSalesNo()));
        objTradingSalesMasterTO.setSalesType(CommonUtil.convertObjToStr(getCbmSalesType().getKeyForSelected()));
        objTradingSalesMasterTO.setSalesDate(curDate);
        objTradingSalesMasterTO.setEmpName(CommonUtil.convertObjToStr(getTxtName()));
        objTradingSalesMasterTO.setEmpNo(CommonUtil.convertObjToStr(getTxtEmpNo()));
        objTradingSalesMasterTO.setProdDesc(CommonUtil.convertObjToStr(getTxtProdDesc()));
        objTradingSalesMasterTO.setAcNo(CommonUtil.convertObjToStr(getTxtACNo()));
        objTradingSalesMasterTO.setPlace(CommonUtil.convertObjToStr(getCboPlace()));
        objTradingSalesMasterTO.setBarcodeNumber(CommonUtil.convertObjToStr(getTxtBarCodeNumber()));
        objTradingSalesMasterTO.setGrandTotal(CommonUtil.convertObjToStr(getTxtGrandTotal()));
        objTradingSalesMasterTO.setProdID(CommonUtil.convertObjToStr(getCboProdID()));
        objTradingSalesMasterTO.setInstallmentAmt(CommonUtil.convertObjToStr(getTxtInstallmentAmt()));
        objTradingSalesMasterTO.setInstallmentNo(CommonUtil.convertObjToStr(getTxtInstallmentNo()));
        objTradingSalesMasterTO.setFirstInstAmt(CommonUtil.convertObjToStr(getTxtFirstInstAmt()));
        objTradingSalesMasterTO.setDiscAmt(CommonUtil.convertObjToStr(getTxtDiscount()));
        objTradingSalesMasterTO.setDiscount(CommonUtil.convertObjToStr(getDiscPercentage()));
        objTradingSalesMasterTO.setTransportationCharges(CommonUtil.convertObjToStr(getTxtTranspotationCharges()));
        objTradingSalesMasterTO.setInstDt(curDate);
        if (isChkInstallment()) {
            objTradingSalesMasterTO.setInstallment(YES);
        } else {
            objTradingSalesMasterTO.setInstallment(NO);
        }
        objTradingSalesMasterTO.setMemberType(CommonUtil.convertObjToStr(getType()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingSalesMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingSalesMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingSalesMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingSalesMasterTO.setStatusDt(curDate);
        return objTradingSalesMasterTO;
    }

    private TradingSalesRetTO setTradingSalesReturnTO() {
        TradingSalesRetTO objTradingSalesRetTO = new TradingSalesRetTO();
        objTradingSalesRetTO.setSalesRetNo(CommonUtil.convertObjToStr(getTxtSalesretNo()));
        objTradingSalesRetTO.setSalesType(CommonUtil.convertObjToStr(getTxtSalesRetSalType()));
        objTradingSalesRetTO.setSalesDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtSalesReturnSalesDate())));
        objTradingSalesRetTO.setName(CommonUtil.convertObjToStr(getTxtSalesReturnName()));
        objTradingSalesRetTO.setCustID(CommonUtil.convertObjToStr(getTxtSalesReturnCustId()));
        objTradingSalesRetTO.setSalesNo(CommonUtil.convertObjToStr(getTxtSalesReturnSalesNo()));
        objTradingSalesRetTO.setBankAcHD(CommonUtil.convertObjToStr(getTxtSalesRetBankAcHD()));
        objTradingSalesRetTO.setSalesRetDt(curDate);
        objTradingSalesRetTO.setSales(CommonUtil.convertObjToStr(getTxtSales()));
        objTradingSalesRetTO.setSalReturn(CommonUtil.convertObjToStr(getTxtReturn()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingSalesRetTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingSalesRetTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingSalesRetTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingSalesRetTO.setStatusDt(curDate);
        return objTradingSalesRetTO;
    }

    public void getData(HashMap whereMap,int panEditDelete) {
        try {
            if (panEditDelete == SALES) {
                whereMap.put("SALES", "SALES");
            } else if (panEditDelete == SALESRETURN) {
                whereMap.put("SALESRETURN", "SALESRETURN");
            }
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            if (data != null && data.containsKey("objTradingSalesMasterTO")) {
                objTradingSalesMasterTO = (TradingSalesMasterTO) ((List) data.get("objTradingSalesMasterTO")).get(0);
                populateSalesData(objTradingSalesMasterTO);
                salesDetailsMap = new LinkedHashMap();
                if (data.containsKey("SALES_DATA")) {
                    salesDetailsMap = (LinkedHashMap) data.get("SALES_DATA");
                    ArrayList addList = new ArrayList(salesDetailsMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesDetailsMap.get(addList.get(i));
                        setStockID(CommonUtil.convertObjToStr(objTradingSalesTO.getStockID()));
                        ArrayList SalRow = new ArrayList();
                        SalRow.add(String.valueOf(i + 1));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingSalesTO.getProdName()));
                        SalRow.add(CommonUtil.convertObjToStr(objTradingSalesTO.getRate()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getUnitType()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getQty()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getMrp()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTax()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTaxableAmt()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getCash()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getAdjust()));
                        SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTotal()));
                        tblSalesDetails.addRow(SalRow);
                        SalRow = null;

                    }
                }
            }else if (data != null && data.containsKey("objTradingSalesReturnTO")) {
                objTradingSalesRetTO = (TradingSalesRetTO) ((List) data.get("objTradingSalesReturnTO")).get(0);
                populateTradingSalesReturn(objTradingSalesRetTO);
                //returnDetailsMap = new LinkedHashMap();
                if (data.containsKey("RETURN_DATA")) {
                    populateSalesReturnDetData(data);
                    cellSalesReturnEditableColumnTrue();
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateSalesTableDetails(int row) {
        try {
            resetSalesTable();
            final TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesDetailsMap.get(row);
            populateSalesMasterDetailsData(objTradingSalesTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void populateSalesReturnDetData(HashMap data) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            List salesRetLst = (List) data.get("RETURN_DATA");
            if (salesRetLst != null && salesRetLst.size() > 0) {
                for (int i = 0; i < salesRetLst.size(); i++) {
                    dataMap = (HashMap) salesRetLst.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SL_NO")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PRODUCT_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("RATE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("UNIT_TYPE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("QTY")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("TAXABLE_AMOUNT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("TAX")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("STOCK_ID")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SALES_TOTAL")));
                    rowList.add(CommonUtil.convertObjToInt(dataMap.get("RETURN_QTY")));
                    rowList.add(CommonUtil.convertObjToDouble(dataMap.get("RETURN_TOTAL")));
                    tableList.add(rowList);
                }
                tblSalesReturn = new TableModel(tableList, tblSalesReturnTitle);
                setFinalList(salesRetLst);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void populateTradingSalesReturn(TradingSalesRetTO objTradingSalesRetTO) {
        HashMap mapData = null;
        try {
            setTxtSalesretNo(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesRetNo()));
            setTxtSalesReturnCustId(CommonUtil.convertObjToStr(objTradingSalesRetTO.getCustID()));
            setTxtSalesReturnSalesNo(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesNo()));
            setTxtSalesRetSalType(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesType()));
            setTxtSalesRetBankAcHD(CommonUtil.convertObjToStr(objTradingSalesRetTO.getBankAcHD()));
            setTdtSalesReturnSalesDate(CommonUtil.convertObjToStr(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesDate()))));
            setTdtSalesReturnDate(CommonUtil.convertObjToStr(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingSalesRetTO.getStatusDt()))));
//            setTxtSales(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSales()));
//            setTxtReturn(CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalReturn()));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    private void populateSalesMasterDetailsData(TradingSalesTO objTradingSalesTO) throws Exception {
        setTxtProductName(CommonUtil.convertObjToStr(objTradingSalesTO.getProdName()));
        setTxtSalesPrice(CommonUtil.convertObjToStr(objTradingSalesTO.getRate()));
        setTxtMRP(CommonUtil.convertObjToStr(objTradingSalesTO.getMrp()));
        setTxtQty(CommonUtil.convertObjToStr(objTradingSalesTO.getQty()));
        setTxtRemarks(CommonUtil.convertObjToStr(objTradingSalesTO.getRemarks()));
        setCboUnitType(CommonUtil.convertObjToStr(objTradingSalesTO.getUnitType()));
        HashMap stkMap = new HashMap();
        String stock_ID = "";
        stock_ID = CommonUtil.convertObjToStr(objTradingSalesTO.getStockID());
        if (stock_ID != null && stock_ID.length() > 0) {
            stkMap.put("STOCK_ID", stock_ID);
            List stkLst = ClientUtil.executeQuery("getStockQuantity", stkMap);
            if (stkLst != null && stkLst.size() > 0) {
                stkMap = (HashMap) stkLst.get(0);
                setTxtAvailQty(CommonUtil.convertObjToStr(stkMap.get("STOCK_QUANT")));
            }
        }
        setChanged();
        notifyObservers();
    }

    private void populateTradingSalesTable() throws Exception {
        ArrayList addList = new ArrayList(salesDetailsMap.keySet());
        for (int i = 0; i < addList.size(); i++) {
            TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesDetailsMap.get(addList.get(i));
            ArrayList SalRow = new ArrayList();
            SalRow.add(CommonUtil.convertObjToStr(objTradingSalesTO.getSlNo()));
            SalRow.add(CommonUtil.convertObjToStr(objTradingSalesTO.getProdName()));
            SalRow.add(CommonUtil.convertObjToStr(objTradingSalesTO.getRate()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getUnitType()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getQty()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getMrp()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTax()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTaxableAmt()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getCash()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getAdjust()));
            SalRow.add(CommonUtil.convertObjToDouble(objTradingSalesTO.getTotal()));
            tblSalesDetails.addRow(SalRow);
            SalRow = null;
        }
        notifyObservers();
    }

    public void resetSalesTable() {
        setTxtProductName("");
        setTxtSalesPrice("");
        setTxtMRP("");
        setTxtQty("");
        //tblSalesDetails = new EnhancedTableModel(null, tblSalesDetailsTitle);
    }

    public void resetSalesTableDetails() {
        tblSalesDetails.setDataArrayList(null, tblSalesDetailsTitle);
    }

    public void deleteSalesDetails(int val, int row) {
        if (deletedSalesMap == null) {
            deletedSalesMap = new LinkedHashMap();
        }
        TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesDetailsMap.get(val);
        objTradingSalesTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedSalesMap.put(CommonUtil.convertObjToStr(tblSalesDetails.getValueAt(row, 0)), salesDetailsMap.get(val));
        Object obj;
        obj = val;
        salesDetailsMap.remove(val);
        resetSalesTableDetails();
        try {
            populateTradingSalesTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * Getter for property tdsCrAcHdId.
     *
     * @return Value of property tdsCrAcHdId.
     */
    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public ComboBoxModel getCbmSalesType() {
        return cbmSalesType;
    }

    public void setCbmSalesType(ComboBoxModel cbmSalesType) {
        this.cbmSalesType = cbmSalesType;
    }

    public ComboBoxModel getCbmPlace() {
        return cbmPlace;
    }

    public void setCbmPlace(ComboBoxModel cbmPlace) {
        this.cbmPlace = cbmPlace;
    }

    public ComboBoxModel getCbmProdID() {
        return cbmProdID;
    }

    public void setCbmProdID(ComboBoxModel cbmProdID) {
        this.cbmProdID = cbmProdID;
    }

    public ComboBoxModel getCbmUnitType() {
        return cbmUnitType;
    }

    public void setCbmUnitType(ComboBoxModel cbmUnitType) {
        this.cbmUnitType = cbmUnitType;
    }

    public String getCboSalesType() {
        return cboSalesType;
    }

    public void setCboSalesType(String cboSalesType) {
        this.cboSalesType = cboSalesType;
    }

    public String getCboPlace() {
        return cboPlace;
    }

    public void setCboPlace(String cboPlace) {
        this.cboPlace = cboPlace;
    }

    public String getCboProdID() {
        return cboProdID;
    }

    public void setCboProdID(String cboProdID) {
        this.cboProdID = cboProdID;
    }

    public String getCboUnitType() {
        return cboUnitType;
    }

    public void setCboUnitType(String cboUnitType) {
        this.cboUnitType = cboUnitType;
    }

    public String getTxtEmpNo() {
        return txtEmpNo;
    }

    public void setTxtEmpNo(String txtEmpNo) {
        this.txtEmpNo = txtEmpNo;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtSalesNo() {
        return txtSalesNo;
    }

    public void setTxtSalesNo(String txtSalesNo) {
        this.txtSalesNo = txtSalesNo;
    }

    public String getTdtSalesDt() {
        return tdtSalesDt;
    }

    public void setTdtSalesDt(String tdtSalesDt) {
        this.tdtSalesDt = tdtSalesDt;
    }

    public String getTxtCreditAmt() {
        return txtCreditAmt;
    }

    public void setTxtCreditAmt(String txtCreditAmt) {
        this.txtCreditAmt = txtCreditAmt;
    }

    public String getTxtProdDesc() {
        return txtProdDesc;
    }

    public void setTxtProdDesc(String txtProdDesc) {
        this.txtProdDesc = txtProdDesc;
    }

    public String getTxtACNo() {
        return txtACNo;
    }

    public void setTxtACNo(String txtACNo) {
        this.txtACNo = txtACNo;
    }

    public String getTxtBarCodeNumber() {
        return txtBarCodeNumber;
    }

    public void setTxtBarCodeNumber(String txtBarCodeNumber) {
        this.txtBarCodeNumber = txtBarCodeNumber;
    }

    public String getTxtSalesPrice() {
        return txtSalesPrice;
    }

    public void setTxtSalesPrice(String txtSalesPrice) {
        this.txtSalesPrice = txtSalesPrice;
    }

    public String getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(String txtProductName) {
        this.txtProductName = txtProductName;
    }

    public String getTxtMRP() {
        return txtMRP;
    }

    public void setTxtMRP(String txtMRP) {
        this.txtMRP = txtMRP;
    }

    public String getTxtQty() {
        return txtQty;
    }

    public void setTxtQty(String txtQty) {
        this.txtQty = txtQty;
    }

    public String getTxtAvailQty() {
        return txtAvailQty;
    }

    public void setTxtAvailQty(String txtAvailQty) {
        this.txtAvailQty = txtAvailQty;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public String getTxtCash() {
        return txtCash;
    }

    public void setTxtCash(String txtCash) {
        this.txtCash = txtCash;
    }

    public String getTxtAdjust() {
        return txtAdjust;
    }

    public void setTxtAdjust(String txtAdjust) {
        this.txtAdjust = txtAdjust;
    }

    public String getTxtDiscount() {
        return txtDiscount;
    }

    public void setTxtDiscount(String txtDiscount) {
        this.txtDiscount = txtDiscount;
    }

    public String getTxtTotal() {
        return txtTotal;
    }

    public void setTxtTotal(String txtTotal) {
        this.txtTotal = txtTotal;
    }

    public String getTxtTranspotationCharges() {
        return txtTranspotationCharges;
    }

    public void setTxtTranspotationCharges(String txtTranspotationCharges) {
        this.txtTranspotationCharges = txtTranspotationCharges;
    }

    public String getTxtGrandTotal() {
        return txtGrandTotal;
    }

    public void setTxtGrandTotal(String txtGrandTotal) {
        this.txtGrandTotal = txtGrandTotal;
    }

    public String getTxtInstallmentNo() {
        return txtInstallmentNo;
    }

    public void setTxtInstallmentNo(String txtInstallmentNo) {
        this.txtInstallmentNo = txtInstallmentNo;
    }

    public String getTxtInstallmentAmt() {
        return txtInstallmentAmt;
    }

    public void setTxtInstallmentAmt(String txtInstallmentAmt) {
        this.txtInstallmentAmt = txtInstallmentAmt;
    }

    public String getTxtFirstInstAmt() {
        return txtFirstInstAmt;
    }

    public void setTxtFirstInstAmt(String txtFirstInstAmt) {
        this.txtFirstInstAmt = txtFirstInstAmt;
    }

    public String getTdtSalesDate() {
        return tdtSalesDate;
    }

    public void setTdtSalesDate(String tdtSalesDate) {
        this.tdtSalesDate = tdtSalesDate;
    }

    public boolean isChkInstallment() {
        return chkInstallment;
    }

    public void setChkInstallment(boolean chkInstallment) {
        this.chkInstallment = chkInstallment;
    }

    public String getTxtSalesretNo() {
        return txtSalesretNo;
    }

    public void setTxtSalesretNo(String txtSalesretNo) {
        this.txtSalesretNo = txtSalesretNo;
    }

    public String getTxtSalesReturnCustId() {
        return txtSalesReturnCustId;
    }

    public void setTxtSalesReturnCustId(String txtSalesReturnCustId) {
        this.txtSalesReturnCustId = txtSalesReturnCustId;
    }

    public String getTdtSalesReturnDate() {
        return tdtSalesReturnDate;
    }

    public void setTdtSalesReturnDate(String tdtSalesReturnDate) {
        this.tdtSalesReturnDate = tdtSalesReturnDate;
    }

    public String getTdtSalesReturnSalesDate() {
        return tdtSalesReturnSalesDate;
    }

    public void setTdtSalesReturnSalesDate(String tdtSalesReturnSalesDate) {
        this.tdtSalesReturnSalesDate = tdtSalesReturnSalesDate;
    }

    public String getTxtSalesReturnEmpNo() {
        return txtSalesReturnEmpNo;
    }

    public void setTxtSalesReturnEmpNo(String txtSalesReturnEmpNo) {
        this.txtSalesReturnEmpNo = txtSalesReturnEmpNo;
    }

    public String getTxtSalesReturnSalesNo() {
        return txtSalesReturnSalesNo;
    }

    public void setTxtSalesReturnSalesNo(String txtSalesReturnSalesNo) {
        this.txtSalesReturnSalesNo = txtSalesReturnSalesNo;
    }

    public String getTxtSalesReturnName() {
        return txtSalesReturnName;
    }

    public void setTxtSalesReturnName(String txtSalesReturnName) {
        this.txtSalesReturnName = txtSalesReturnName;
    }

    public String getTxtTrChg() {
        return txtTrChg;
    }

    public void setTxtTrChg(String txtTrChg) {
        this.txtTrChg = txtTrChg;
    }

    public String getTxtSales() {
        return txtSales;
    }

    public void setTxtSales(String txtSales) {
        this.txtSales = txtSales;
    }

    public String getTxtReturn() {
        return txtReturn;
    }

    public void setTxtReturn(String txtReturn) {
        this.txtReturn = txtReturn;
    }

    public String getTxtSalesReprintNo() {
        return txtSalesReprintNo;
    }

    public void setTxtSalesReprintNo(String txtSalesReprintNo) {
        this.txtSalesReprintNo = txtSalesReprintNo;
    }

    public String getTdtSalesReprintSalesDate() {
        return tdtSalesReprintSalesDate;
    }

    public void setTdtSalesReprintSalesDate(String tdtSalesReprintSalesDate) {
        this.tdtSalesReprintSalesDate = tdtSalesReprintSalesDate;
    }

    public String getTxtSalesReprintName() {
        return txtSalesReprintName;
    }

    public void setTxtSalesReprintName(String txtSalesReprintName) {
        this.txtSalesReprintName = txtSalesReprintName;
    }

    public String getTxtSalTotal() {
        return txtSalTotal;
    }

    public void setTxtSalTotal(String txtSalTotal) {
        this.txtSalTotal = txtSalTotal;
    }

    public Boolean getCeil() {
        return ceil;
    }

    public void setCeil(Boolean ceil) {
        this.ceil = ceil;
    }

    public Boolean getSal() {
        return sal;
    }

    public void setSal(Boolean sal) {
        this.sal = sal;
    }

    public static TradingSalesOB getObjTradingSalesOB() {
        return objTradingSalesOB;
    }

    public static void setObjTradingSalesOB(TradingSalesOB objTradingSalesOB) {
        TradingSalesOB.objTradingSalesOB = objTradingSalesOB;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getDiscPercentage() {
        return discPercentage;
    }

    public void setDiscPercentage(String discPercentage) {
        this.discPercentage = discPercentage;
    }

    public TableModel getTblSalesReturn() {
        return tblSalesReturn;
    }

    public void setTblSalesReturn(TableModel tblSalesReturn) {
        this.tblSalesReturn = tblSalesReturn;
    }

    public String getTxtSalesRetSalType() {
        return txtSalesRetSalType;
    }

    public void setTxtSalesRetSalType(String txtSalesRetSalType) {
        this.txtSalesRetSalType = txtSalesRetSalType;
    }

    public String getTxtSalesRetBankAcHD() {
        return txtSalesRetBankAcHD;
    }

    public void setTxtSalesRetBankAcHD(String txtSalesRetBankAcHD) {
        this.txtSalesRetBankAcHD = txtSalesRetBankAcHD;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }
}