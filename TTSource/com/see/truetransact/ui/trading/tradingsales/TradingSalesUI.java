/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingSalesUI.java
 *
 * Created on January 31, 2005, 2:59 PM
 */
package com.see.truetransact.ui.trading.tradingsales;

/**
 *
 * @author Revathi L
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.ResourceBundle;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import java.util.List;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TradingSalesUI extends CInternalFrame implements UIMandatoryField, Observer {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.tradingsales.TradingSalesRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingSalesMRB objMandatoryRB = new TradingSalesMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingSalesOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    private double totalAdjust = 0.00;
    int updateTab = -1;
    private boolean updateMode = false;
    boolean isFilled = false;
    Date currDt = null;
    String prod_ID = "";
    String stock_ID = "";
    String tax = "";
    private ArrayList _heading;
    private ArrayList data;
    ArrayList colourList = new ArrayList();
    int pan = -1;
    int panEditDelete = -1;
    private int EDITSALES = 1, EDITRETURN = 2;
    private int DELETESALES = 1, DELETERETURN = 2;
    private int SALES = 1, SALESRETURN = 2;
    private List finalList = null;

    public TradingSalesUI() {
        initForm();
    }

    /**
     * Method called from consturctor to initialize the form *
     */
    private void initForm() {
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        observable.resetSalesForm();
        observable.resetSalesRetForm();
        panAccountDetails.setVisible(false);
        panTotalRadioDetails.setVisible(false);
        panInstallmentDetails.setVisible(false);
        setButtonEnableDisable();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panSales, false);
        ClientUtil.enableDisable(panSalesReprint, false);
        ClientUtil.enableDisable(panSalesReturn, false);
        ClientUtil.enableDisable(panAccountDetails, false);
//        cboUnitType.setVisible(false);
//        lblUnitType.setVisible(false);
        txtProductName.setEnabled(false);
        txtCreditAmt.setEnabled(false);
        btnLedger.setEnabled(false);
        setSizeTableData();
        panDateDetails.setVisible(false);
        lblSalesRetNameVal.setText("");
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lbSpace2.setName("lbSpace2");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrTDSConfig.setName("mbrTDSConfig");
        panCutOff.setName("panCutOff");
        panStatus.setName("panStatus");
        panSalesReturn.setName("panTDS");
    }

    private void setSizeTableData() {
        tblSalesDetails.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblSalesDetails.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblSalesDetails.getColumnModel().getColumn(2).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(3).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(4).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(5).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(6).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(7).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(8).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(9).setPreferredWidth(30);
        tblSalesDetails.getColumnModel().getColumn(10).setPreferredWidth(40);
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        // rdoCutOff_No.setText(resourceBundle.getString("rdoCutOff_No"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //lblCutOff.setText(resourceBundle.getString("lblCutOff"));
        // lblTdsId.setText(resourceBundle.getString("lblTdsId"));
        // lblEndDate.setText(resourceBundle.getString("lblEndDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        // lblStartDate.setText(resourceBundle.getString("lblStartDate"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //lblCutOfAmount.setText(resourceBundle.getString("lblCutOfAmount"));
        //lblPercentage.setText(resourceBundle.getString("lblPercentage"));
        // lblScope.setText(resourceBundle.getString("lblScope"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        // rdoCutOff_Yes.setText(resourceBundle.getString("rdoCutOff_Yes"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        // lblTdsCreditAchdId.setText(resourceBundle.getString("lblTdsCreditAchdId"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
   
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
   
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
        mandatoryMap.put("txtTdsId", new Boolean(true));
        mandatoryMap.put("txtCutOfAmount", new Boolean(true));
        mandatoryMap.put("cboScope", new Boolean(true));
        mandatoryMap.put("cboCustTypeVal", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(true));
        mandatoryMap.put("rdoCutOff_Yes", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
//        tdtStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtStartDate"));
//        tdtEndDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtEndDate"));
//        txtTdsId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTdsId"));
//        txtCutOfAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCutOfAmount"));
//        cboScope.setHelpMessage(lblMsg, objMandatoryRB.getString("cboScope"));
//         cboCustTypeVal.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustTypeVal"));
//        txtPercentage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPercentage"));
//        rdoCutOff_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCutOff_Yes"));
    }

    /**
     * This method sets the Maximum allowed lenght to the textfields *
     */
    private void setMaxLengths() {
//        txtCutOfAmount.setMaxLength(16);
//        txtPercentage.setMaxLength(8);
        txtQty.setAllowNumber(true);
        txtCustomerID.setAllowAll(true);
        txtProdDesc.setAllowAll(true);
        txtAcNo.setAllowAll(true);
        txtBarCodeNumber.setAllowAll(true);
        txtSalesPrice.setAllowNumber(true);
        txtProductName.setAllowAll(true);
        txtMRP.setAllowNumber(true);
        txtAvailQty.setAllowNumber(true);
        txtRemarks.setAllowAll(true);
        txtCash.setAllowNumber(true);
        txtAdjust.setAllowNumber(true);
        txtTotal.setAllowNumber(true);
        txtDiscPercentage.setAllowNumber(true);
        txtTranspotationCharges.setAllowNumber(true);
        txtGrandTotal.setAllowNumber(true);
        txtInstallmentNo.setAllowAll(true);
        txtInstallmentAmt.setAllowNumber(true);
        txtFirstInstAmt.setAllowNumber(true);
        txtCreditAmt.setValidation(new CurrencyValidation(14, 2));
        txtCash.setValidation(new CurrencyValidation(14, 2));
        txtAdjust.setValidation(new CurrencyValidation(14, 2));
        txtDiscount.setValidation(new CurrencyValidation(14, 2));
        txtTotal.setValidation(new CurrencyValidation(14, 2));
        txtTranspotationCharges.setValidation(new CurrencyValidation(14, 2));
        txtGrandTotal.setValidation(new CurrencyValidation(14, 2));
        txtInstallmentAmt.setValidation(new CurrencyValidation(14, 2));
        txtFirstInstAmt.setValidation(new CurrencyValidation(14, 2));
        txtQty.setValidation(new NumericValidation(3, 2));
        txtSales.setValidation(new CurrencyValidation(14, 2));
        txtReturn.setValidation(new CurrencyValidation(14, 2));
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = TradingSalesOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try {
            cboSalesType.setModel(observable.getCbmSalesType());
            tblSalesDetails.setModel(observable.getTblSalesDetails());
            cboProdID.setModel(observable.getCbmProdID());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateSalesOBFields() {
        observable.setCboSalesType(CommonUtil.convertObjToStr(cboSalesType.getSelectedItem()));
        observable.setTdtSalesDt(CommonUtil.convertObjToStr(tdtSalesDt.getDateValue()));
        observable.setTxtEmpNo(CommonUtil.convertObjToStr(txtCustomerID.getText()));
        observable.setTxtName(CommonUtil.convertObjToStr(lblNameVal.getText()));
        observable.setCboProdID(CommonUtil.convertObjToStr(cboProdID.getSelectedItem()));
        observable.setTxtProdDesc(CommonUtil.convertObjToStr(txtProdDesc.getText()));
        observable.setProdName(CommonUtil.convertObjToStr(txtProductName.getText()));
        observable.setTxtACNo(CommonUtil.convertObjToStr(txtAcNo.getText()));
        observable.setCboPlace(CommonUtil.convertObjToStr(txtPlace.getText()));
        observable.setCboUnitType(CommonUtil.convertObjToStr(txtUnitType.getText()));
        observable.setTxtBarCodeNumber(CommonUtil.convertObjToStr(txtBarCodeNumber.getText()));
        observable.setTxtGrandTotal(CommonUtil.convertObjToStr(txtGrandTotal.getText()));
        observable.setTxtInstallmentAmt(CommonUtil.convertObjToStr(txtInstallmentAmt.getText()));
        observable.setTxtInstallmentNo(CommonUtil.convertObjToStr(txtInstallmentNo.getText()));
        observable.setTxtFirstInstAmt(CommonUtil.convertObjToStr(txtFirstInstAmt.getText()));
        observable.setTdtSalesDate(CommonUtil.convertObjToStr(tdtSalesDate.getDateValue()));
        observable.setStockID(stock_ID);
        observable.setChkInstallment(chkInstallment.isSelected());
        observable.setTxtRemarks(CommonUtil.convertObjToStr(txtRemarks.getText()));
        observable.setTax(CommonUtil.convertObjToStr(tax));
        observable.setRate(CommonUtil.convertObjToStr(txtSalesPrice.getText()));
        observable.setUnitType(CommonUtil.convertObjToStr(txtUnitType.getText()));
        observable.setQty(CommonUtil.convertObjToStr(txtQty.getText()));
        observable.setMrp(CommonUtil.convertObjToStr(txtMRP.getText()));
        observable.setTxtDiscount(CommonUtil.convertObjToStr(txtDiscount.getText()));
        observable.setDiscPercentage(CommonUtil.convertObjToStr(txtDiscPercentage.getText()));
        observable.setTxtTranspotationCharges(CommonUtil.convertObjToStr(txtTranspotationCharges.getText()));
        if (rdoTypeMembers.isSelected()) {
            observable.setType("M");
        } else if (rdoNonMembers.isSelected()) {
            observable.setType("N");
        }
    }

    public void updateSalesRetOBFields() {
        observable.setTxtSalesretNo(CommonUtil.convertObjToStr(txtSalesretNo.getText()));
        observable.setTxtSalesReturnCustId(CommonUtil.convertObjToStr(txtSalesReturnCustID.getText()));
        observable.setTxtSalesReturnName(CommonUtil.convertObjToStr(lblSalesRetNameVal.getText()));
        observable.setTxtSalesReturnSalesNo(CommonUtil.convertObjToStr(txtSalesReturnSalNo.getText()));
        observable.setTdtSalesReturnSalesDate(CommonUtil.convertObjToStr(tdtSalesReturnSalesDate.getDateValue()));
        observable.setTxtSalesRetSalType(CommonUtil.convertObjToStr(txtSalesReturnSalesType.getText()));
        observable.setTxtSalesRetBankAcHD(CommonUtil.convertObjToStr(txtBankAcHead.getText()));
        observable.setTdtSalesReturnDate(CommonUtil.convertObjToStr(tdtSalesReturnDate.getDateValue()));
        observable.setTxtSales(CommonUtil.convertObjToStr(txtSales.getText()));
        observable.setTxtReturn(CommonUtil.convertObjToStr(txtReturn.getText()));
    }

    public void update(Observable observed, Object arg) {
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update() {
        lblSalesNoVal.setText(CommonUtil.convertObjToStr(observable.getTxtSalesNo()));
        cboSalesType.setSelectedItem(observable.getCboSalesType());
        tdtSalesDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSalesDt()));
        if (observable.getType().equals("M")) {
            rdoTypeMembers.setSelected(true);
        } else if (observable.getType().equals("N")) {
            rdoNonMembers.setSelected(true);
        }
        txtCustomerID.setText(CommonUtil.convertObjToStr(observable.getTxtEmpNo()));
        lblNameVal.setText(CommonUtil.convertObjToStr(observable.getTxtName()));
        txtBarCodeNumber.setText(CommonUtil.convertObjToStr(observable.getTxtBarCodeNumber()));
        cboProdID.setSelectedItem(observable.getCboProdID());
        txtProdDesc.setText(CommonUtil.convertObjToStr(observable.getTxtProdDesc()));
        txtAcNo.setText(CommonUtil.convertObjToStr(observable.getTxtACNo()));
        cboProdID.setSelectedItem(observable.getCboProdID());
        if (observable.isChkInstallment() == true) {
            chkInstallment.setSelected(true);
        } else {
            chkInstallment.setSelected(false);
        }
        chkInstallmentActionPerformed(null);
        txtInstallmentNo.setText(CommonUtil.convertObjToStr(observable.getTxtInstallmentNo()));
        txtInstallmentAmt.setText(CommonUtil.convertObjToStr(observable.getTxtInstallmentAmt()));
        txtFirstInstAmt.setText(CommonUtil.convertObjToStr(observable.getTxtFirstInstAmt()));
        tdtSalesDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSalesDate()));
        txtGrandTotal.setText(CommonUtil.convertObjToStr(observable.getTxtGrandTotal()));
        txtPlace.setText(CommonUtil.convertObjToStr(observable.getCboPlace()));
        txtDiscPercentage.setText(CommonUtil.convertObjToStr(observable.getDiscPercentage()));
        txtDiscount.setText(CommonUtil.convertObjToStr(observable.getTxtDiscount()));
        txtTranspotationCharges.setText(CommonUtil.convertObjToStr(observable.getTxtTranspotationCharges()));
        //SALES RETURN
        txtSalesretNo.setText(CommonUtil.convertObjToStr(observable.getTxtSalesretNo()));
        txtSalesReturnCustID.setText(CommonUtil.convertObjToStr(observable.getTxtSalesReturnCustId()));
        lblSalesRetNameVal.setText(getCustName(CommonUtil.convertObjToStr(observable.getTxtSalesReturnCustId())));
        txtSalesReturnSalNo.setText(CommonUtil.convertObjToStr(observable.getTxtSalesReturnSalesNo()));
        txtSalesReturnSalesType.setText(CommonUtil.convertObjToStr(observable.getTxtSalesRetSalType()));
        txtBankAcHead.setText(CommonUtil.convertObjToStr(observable.getTxtSalesRetBankAcHD()));
        tdtSalesReturnSalesDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSalesReturnSalesDate()));
        tdtSalesReturnDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSalesReturnDate()));
    }

    public String getCustName(String custNo) {
        String custName = "";
        if (!custNo.equals("")) {
            HashMap dataMap = new HashMap();
            dataMap.put("CUST_ID", custNo);
            final List resultList = ClientUtil.executeQuery("Account.getCustName", dataMap);
            final HashMap resultMap = (HashMap) resultList.get(0);
            custName = CommonUtil.convertObjToStr(resultMap.get("NAME"));
        }
        return custName;
        
    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("AC_NO")) {
            HashMap map = new HashMap();
            map.put("CUSTOMER_ID", CommonUtil.convertObjToStr(txtCustomerID.getText()));
            map.put("PROD_ID", CommonUtil.convertObjToStr(cboProdID.getSelectedItem()));
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSuspenseAcNoForSale");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("PROD_NAME")) {
            if (CommonUtil.convertObjToStr(txtProductName.getText()).length() > 0) {
                HashMap map = new HashMap();
                map.put("PRODUCT_NAME", txtProductName.getText());
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getTradingProductName");
                new ViewAll(this, viewMap).show();
            } else {
                ClientUtil.showMessageWindow("Please Enter minimum 4 characters of the Product Name..");
                return;
            }
        } else if (currField.equalsIgnoreCase("Edit") || (currField.equalsIgnoreCase("Delete"))) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            if (panSales.isShowing() == true) {
                pan = SALES;
                viewMap.put(CommonConstants.MAP_NAME, "getTradingSalesEdit");
            } else if (panSalesReturn.isShowing() == true) {
                pan = SALESRETURN;
                viewMap.put(CommonConstants.MAP_NAME, "getTradingSalesReturnEdit");
            }
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingSalesView");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("SALES_NO")) {
            HashMap map = new HashMap();
            map.put("CUST_ID", CommonUtil.convertObjToStr(txtSalesReturnCustID.getText()));
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSalesNoForReturn");
            new ViewAll(this, viewMap).show();
        }
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        isFilled = true;
        double clearAmt = 0.0;
        if (viewType.equalsIgnoreCase("AC_NO")) {
            txtAcNo.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_ACCT_NUM")));
            clearAmt = CommonUtil.convertObjToDouble(hash.get("CLEAR_BALANCE"));
            if (clearAmt < 0) {
                clearAmt = clearAmt * -1;
                txtCreditAmt.setText(CommonUtil.convertObjToStr(clearAmt));
            } else {
                txtCreditAmt.setText(CommonUtil.convertObjToStr(clearAmt));
            }
            btnLedger.setEnabled(true);
        } else if (viewType.equals("Customer")) {
            HashMap custMap = new HashMap();
            if (panSales.isShowing() == true) {
                txtCustomerID.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                lblNameVal.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                if (CommonUtil.convertObjToStr(cboSalesType).equals("Credit Sale")) {
                    HashMap CheckMap = new HashMap();
                    CheckMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustomerID.getText()));
                    List checkLst = ClientUtil.executeQuery("getSuspenseCrAcDetails", CheckMap);
                    if (checkLst != null && checkLst.size() > 0) {
                        CheckMap = (HashMap) checkLst.get(0);
                        cboProdID.setSelectedItem(CommonUtil.convertObjToStr(CheckMap.get("SUSPENSE_PROD_ID")));
                        txtProdDesc.setText(CommonUtil.convertObjToStr(CheckMap.get("SUSPENSE_PROD_DESC")));
                        txtAcNo.setText(CommonUtil.convertObjToStr(CheckMap.get("SUSPENSE_ACCT_NUM")));
                        ClientUtil.enableDisable(panAccountDetails, false);
                        btnAcNo.setEnabled(false);
                    } else {
                        ClientUtil.showMessageWindow("Please Create Sundry Debtors Suspense Account for this Customer ID...!");
                        cboProdID.setSelectedItem("");
                        txtProdDesc.setText("");
                        txtAcNo.setText("");
                        return;
                    }
                }
            } else if (panSalesReturn.isShowing() == true) {
                txtSalesReturnCustID.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                lblSalesRetNameVal.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            }
        } else if (viewType.equalsIgnoreCase("SALES_NO")) {
            List salesNoLst = ClientUtil.executeQuery("getCheckingSalesNo", hash);
            if (salesNoLst != null && salesNoLst.size() > 0) {
                HashMap purchNoMap = (HashMap) salesNoLst.get(0);
                if (CommonUtil.convertObjToStr(purchNoMap.get("AUTHORIZE_STATUS")).equals("")) {
                    ClientUtil.showMessageWindow("Earlier Sales Return for this Sales No" + " " + CommonUtil.convertObjToStr(hash.get("SALES_NO"))
                            + " " + "Not yet Authorized");
                    btnCancelActionPerformed(null);
                    return;
                }
            }
            txtSalesReturnSalNo.setText(CommonUtil.convertObjToStr(hash.get("SALES_NO")));
            tdtSalesReturnSalesDate.setDateValue(CommonUtil.convertObjToStr(hash.get("SALES_DATE")));
            txtSalesReturnSalesType.setText(CommonUtil.convertObjToStr(hash.get("SALES_TYPE")));
            if (CommonUtil.convertObjToStr(hash.get("SALES_TYPE")).equals("Credit Sale")) {
                txtBankAcHead.setText(CommonUtil.convertObjToStr(hash.get("ACC_NO")));
            } else {
                lblBankAcHead.setVisible(false);
                txtBankAcHead.setVisible(false);
            }
            if (txtSalesReturnSalNo.getText().length() > 0) {
                observable.populateSalesReturnData(txtSalesReturnSalNo.getText());
                tblSalesReturn.setModel(observable.getTblSalesReturn());
                Double amt = 0.0;
                totalCatAmountCalc();
            }
        } else if (viewType.equalsIgnoreCase("PROD_NAME")) {
            txtProductName.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_NAME")));
            txtAvailQty.setText(CommonUtil.convertObjToStr(hash.get("STOCK_QUANT")));
            txtSalesPrice.setText(CommonUtil.convertObjToStr(hash.get("STOCK_SALES_PRICE")));
            txtMRP.setText(CommonUtil.convertObjToStr(hash.get("STOCK_MRP")));
            observable.setProdName(CommonUtil.convertObjToStr(hash.get("PRODUCT_NAME")));
            observable.setRate(CommonUtil.convertObjToStr(hash.get("STOCK_SALES_PRICE")));
            txtUnitType.setText(CommonUtil.convertObjToStr(hash.get("UNITTYPE")));
            observable.setQty(CommonUtil.convertObjToStr(txtQty.getText()));
            observable.setMrp(CommonUtil.convertObjToStr(hash.get("STOCK_MRP")));
//            //observable.setTax(CommonUtil.convertObjToStr(hash.get("STOCK_QUANT")));
//            observable.setTaxableAmt(CommonUtil.convertObjToStr(hash.get("STOCK_SALES_PRICE")));
//            observable.setCash(CommonUtil.convertObjToStr(hash.get("STOCK_SALES_PRICE")));
//            // observable.setAdjust(CommonUtil.convertObjToStr(hash.get("STOCK_QUANT")));
//            observable.setTotal(CommonUtil.convertObjToStr(hash.get("STOCK_SALES_PRICE")));
            tax = CommonUtil.convertObjToStr(hash.get("TAX"));
            prod_ID = CommonUtil.convertObjToStr(hash.get("PRODUCT_ID"));
            stock_ID = CommonUtil.convertObjToStr(hash.get("STOCK_ID"));
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            if(pan==SALES){
            panEditDelete = SALES;
            }else if(pan==SALESRETURN){
            panEditDelete = SALESRETURN;
            }
            observable.getData(hash, panEditDelete);
            if (pan == SALESRETURN) {
                tblSalesReturn.setModel(observable.getTblSalesReturn());
            }
            update();
            txtProductName.setEnabled(false);
            btnProductName.setEnabled(true);
            txtQty.setEnabled(true);
            totalCatAmountCalc();
            discAmountCalc();
            tcpAmountCalc();
            totalRetAmountCalc();
            txtAdjust.setText(CommonUtil.convertObjToStr(totalAdjust));
            //txtTranspotationCharges.setText(CommonUtil.convertObjToStr(totalAdjust));
            btnAcNo.setEnabled(false);
            setSizeTableData();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                btnAdd.setEnabled(false);
                btnProductName.setEnabled(false);
            }
            txtCustomerID.setEnabled(false);
            txtProductName.setEnabled(true);
            txtTranspotationCharges.setEnabled(true);
            txtDiscPercentage.setEnabled(true);
            chkInstallment.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            if (panSales.isShowing() == true) {
                panEditDelete = SALES;
                observable.getData(hash, panEditDelete);
                totalCatAmountCalc();
                discAmountCalc();
                tcpAmountCalc();
                txtAdjust.setText(CommonUtil.convertObjToStr(totalAdjust));
                txtTranspotationCharges.setText(CommonUtil.convertObjToStr(totalAdjust));
                btnAcNo.setEnabled(false);
                btnAdd.setEnabled(false);
                txtCustomerID.setEnabled(false);
                btnProductName.setEnabled(false);
                setSizeTableData();
            } else if (panSalesReturn.isShowing() == true) {
                panEditDelete = SALESRETURN;
                observable.getData(hash, panEditDelete);
                tblSalesReturn.setModel(observable.getTblSalesReturn());
                totalRetAmountCalc();
                totalCatAmountCalc();
            }
            update();
        }
    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /* Calls the execute method of TDSConfigOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        observable.doAction(pan);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                if (observable.getProxyReturnMap().containsKey("SALES_NO")) {
                    ClientUtil.showMessageWindow("Sales No : " + observable.getProxyReturnMap().get("SALES_NO"));
                } else {
                    ClientUtil.showMessageWindow("Sales Return No : " + observable.getProxyReturnMap().get("SALES_RETURN_NO"));
                }
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
        }
        btnCancelActionPerformed(null);
    }

    /* set the screen after the updation,insertion, deletion */
    private void settings() {
        observable.resetSalesForm();
        observable.resetSalesRetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSalesReturn, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /* Does necessary operaion when user clicks the save button */
    private void savePerformed() {
        if (panSales.isShowing() == true) {
            updateSalesOBFields();
        } else if (panSalesReturn.isShowing() == true) {
            updateSalesRetOBFields();
        }
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }

        //__ Make the Screen Closable..
        setModified(false);
    }

    private double totalCatAmountCalc() {
        double totAmt = 0.0;
        String value = "";
        if (tblSalesDetails.getRowCount() > 0) {
            for (int i = 0; i < tblSalesDetails.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblSalesDetails.getValueAt(i, 10));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }

            txtGrandTotal.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
            txtCash.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
            txtTotal.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
            discAmountCalc();
//        txtInstallmentAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
//        txtFirstInstAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        } else {
            for (int i = 0; i < tblSalesReturn.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(i, 8));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
            value = CurrencyValidation.formatCrore(String.valueOf(totAmt));
            txtSales.setText(value);
        }
        return totAmt;
    }

    private void discAmountCalc() {
        if (txtDiscPercentage.getText().length() > 0) {
            double percentage = CommonUtil.convertObjToDouble(txtDiscPercentage.getText());
            double total = CommonUtil.convertObjToDouble(txtCash.getText());
            double DiscAmt = (percentage * total / 100);
            txtDiscount.setText(CommonUtil.convertObjToStr(DiscAmt));
            double grandTot = total - DiscAmt;
            //txtCash.setText(CommonUtil.convertObjToStr(grandTot));
            txtTotal.setText(CommonUtil.convertObjToStr(grandTot));
            Rounding rod = new Rounding();
            grandTot = (double) rod.getNearest((long) (grandTot * 100), 100) / 100;
            txtGrandTotal.setText(CommonUtil.convertObjToStr(grandTot));
        }
    }

    private void tcpAmountCalc() {
        if (txtTranspotationCharges.getText().length() > 0) {
            double total = CommonUtil.convertObjToDouble(txtTotal.getText());
            double tcpCharges = CommonUtil.convertObjToDouble(txtTranspotationCharges.getText());
            double grandTot = total + tcpCharges;
            Rounding rod = new Rounding();
            grandTot = (double) rod.getNearest((long) (grandTot * 100), 100) / 100;
            txtGrandTotal.setText(CommonUtil.convertObjToStr(grandTot));
        }
    }

    /**
     * This will show the alertwindow *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoType = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTDSConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTradingSales = new com.see.truetransact.uicomponent.CPanel();
        tabTradingSales = new com.see.truetransact.uicomponent.CTabbedPane();
        panSales = new com.see.truetransact.uicomponent.CPanel();
        panSalesDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPlace = new com.see.truetransact.uicomponent.CLabel();
        lblSalesNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalesDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSalesDt = new com.see.truetransact.uicomponent.CDateField();
        lblSalesNoVal = new com.see.truetransact.uicomponent.CLabel();
        panType = new com.see.truetransact.uicomponent.CPanel();
        rdoTypeMembers = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNonMembers = new com.see.truetransact.uicomponent.CRadioButton();
        txtPlace = new com.see.truetransact.uicomponent.CTextField();
        panSalesEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblACNo = new com.see.truetransact.uicomponent.CLabel();
        cboProdID = new com.see.truetransact.uicomponent.CComboBox();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProdDesc = new com.see.truetransact.uicomponent.CTextField();
        panName1 = new com.see.truetransact.uicomponent.CPanel();
        txtAcNo = new com.see.truetransact.uicomponent.CTextField();
        btnAcNo = new com.see.truetransact.uicomponent.CButton();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmpNo = new com.see.truetransact.uicomponent.CPanel();
        txtCreditAmt = new com.see.truetransact.uicomponent.CTextField();
        lblEmpNo = new com.see.truetransact.uicomponent.CLabel();
        lblCreditAmt = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblSalesType = new com.see.truetransact.uicomponent.CLabel();
        cboSalesType = new com.see.truetransact.uicomponent.CComboBox();
        panName = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        btnLedger = new com.see.truetransact.uicomponent.CButton();
        panDateDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnGo = new com.see.truetransact.uicomponent.CButton();
        panSalesProdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalesPrice = new com.see.truetransact.uicomponent.CLabel();
        lblUnitType = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtSalesPrice = new com.see.truetransact.uicomponent.CTextField();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblMRP = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblBarCodeNumber = new com.see.truetransact.uicomponent.CLabel();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        txtBarCodeNumber = new com.see.truetransact.uicomponent.CTextField();
        txtAvailQty = new com.see.truetransact.uicomponent.CTextField();
        txtMRP = new com.see.truetransact.uicomponent.CTextField();
        lblAvailQty = new com.see.truetransact.uicomponent.CLabel();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        panProductName = new com.see.truetransact.uicomponent.CPanel();
        txtProductName = new com.see.truetransact.uicomponent.CTextField();
        btnProductName = new com.see.truetransact.uicomponent.CButton();
        txtUnitType = new com.see.truetransact.uicomponent.CTextField();
        btnTblDelete = new com.see.truetransact.uicomponent.CButton();
        panSalesTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalesDetails = new com.see.truetransact.uicomponent.CTable();
        panTotalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTranspotationCharges = new com.see.truetransact.uicomponent.CLabel();
        lblAdjust = new com.see.truetransact.uicomponent.CLabel();
        lblDiscount = new com.see.truetransact.uicomponent.CLabel();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        txtAdjust = new com.see.truetransact.uicomponent.CTextField();
        txtDiscount = new com.see.truetransact.uicomponent.CTextField();
        txtTotal = new com.see.truetransact.uicomponent.CTextField();
        lblGrandTotal = new com.see.truetransact.uicomponent.CLabel();
        lblCash = new com.see.truetransact.uicomponent.CLabel();
        txtGrandTotal = new com.see.truetransact.uicomponent.CTextField();
        txtCash = new com.see.truetransact.uicomponent.CTextField();
        panTotalRadioDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoCeil = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFloor = new com.see.truetransact.uicomponent.CRadioButton();
        txtTranspotationCharges = new com.see.truetransact.uicomponent.CTextField();
        panInstallmentDetails = new com.see.truetransact.uicomponent.CPanel();
        chkInstallment = new com.see.truetransact.uicomponent.CCheckBox();
        lblInstallmentNo = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentNo = new com.see.truetransact.uicomponent.CTextField();
        lblFirstInstAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFirstInstAmt = new com.see.truetransact.uicomponent.CTextField();
        txtInstallmentAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInstallmentAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSalesDate = new com.see.truetransact.uicomponent.CDateField();
        txtDiscPercentage = new com.see.truetransact.uicomponent.CTextField();
        lblDiscPercentage = new com.see.truetransact.uicomponent.CLabel();
        panSalesReturn = new com.see.truetransact.uicomponent.CPanel();
        panCutOff = new com.see.truetransact.uicomponent.CPanel();
        panSalesReturnTotalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSales = new com.see.truetransact.uicomponent.CLabel();
        lblReturn = new com.see.truetransact.uicomponent.CLabel();
        txtSales = new com.see.truetransact.uicomponent.CTextField();
        txtReturn = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment = new com.see.truetransact.uicomponent.CLabel();
        panSalesReturnSubDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalesretNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnDate = new com.see.truetransact.uicomponent.CLabel();
        txtSalesretNo = new com.see.truetransact.uicomponent.CTextField();
        lblSalesReturnSalesType = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnSalesDate = new com.see.truetransact.uicomponent.CLabel();
        lblSalesRetCustID = new com.see.truetransact.uicomponent.CLabel();
        txtSalesReturnSalesType = new com.see.truetransact.uicomponent.CTextField();
        txtBankAcHead = new com.see.truetransact.uicomponent.CTextField();
        lblSalesReturnSalesNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnName = new com.see.truetransact.uicomponent.CLabel();
        tdtSalesReturnDate = new com.see.truetransact.uicomponent.CDateField();
        tdtSalesReturnSalesDate = new com.see.truetransact.uicomponent.CDateField();
        panEmpNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtSalesReturnSalNo = new com.see.truetransact.uicomponent.CTextField();
        btnSalesReturnSalNo = new com.see.truetransact.uicomponent.CButton();
        panEmpNo2 = new com.see.truetransact.uicomponent.CPanel();
        txtSalesReturnCustID = new com.see.truetransact.uicomponent.CTextField();
        btnSalesReturnCustID = new com.see.truetransact.uicomponent.CButton();
        lblBankAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblSalesRetNameVal = new com.see.truetransact.uicomponent.CLabel();
        panSalesReturnTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalesReturn = new com.see.truetransact.uicomponent.CTable();
        panSalesReprint = new com.see.truetransact.uicomponent.CPanel();
        panSalesReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalesReprintSubDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalesReprintNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReprintSalesDate = new com.see.truetransact.uicomponent.CLabel();
        btnSalesReprintNo = new com.see.truetransact.uicomponent.CButton();
        lblSalesReprintName = new com.see.truetransact.uicomponent.CLabel();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        txtSalesReprintName = new com.see.truetransact.uicomponent.CTextField();
        txtSalesReprintNo = new com.see.truetransact.uicomponent.CTextField();
        rdoSal = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRet = new com.see.truetransact.uicomponent.CRadioButton();
        btnSalesReprintAdd = new com.see.truetransact.uicomponent.CButton();
        tdtSalesReprintSalesDate = new com.see.truetransact.uicomponent.CDateField();
        panSalesReprintTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW3 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalesRePrint = new com.see.truetransact.uicomponent.CTable();
        panSalTotal = new com.see.truetransact.uicomponent.CPanel();
        lblSalTotal = new com.see.truetransact.uicomponent.CLabel();
        txtSalTotal = new com.see.truetransact.uicomponent.CTextField();
        panSalesReprintButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSalesReprint = new com.see.truetransact.uicomponent.CButton();
        btnSalesReprintClear = new com.see.truetransact.uicomponent.CButton();
        btnSalesReprintReject = new com.see.truetransact.uicomponent.CButton();
        mbrTDSConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(955, 665));
        setNormalBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(955, 665));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnView);

        lblSpace4.setText("     ");
        tbrTDSConfig.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTDSConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTDSConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTDSConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTDSConfig.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnClose);

        getContentPane().add(tbrTDSConfig, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panTradingSales.setMinimumSize(new java.awt.Dimension(955, 625));
        panTradingSales.setPreferredSize(new java.awt.Dimension(955, 625));
        panTradingSales.setLayout(new java.awt.GridBagLayout());

        tabTradingSales.setMinimumSize(new java.awt.Dimension(955, 625));
        tabTradingSales.setPreferredSize(new java.awt.Dimension(955, 625));

        panSales.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSales.setMaximumSize(new java.awt.Dimension(900, 620));
        panSales.setMinimumSize(new java.awt.Dimension(900, 620));
        panSales.setPreferredSize(new java.awt.Dimension(900, 620));
        panSales.setLayout(new java.awt.GridBagLayout());

        panSalesDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesDetails.setMaximumSize(new java.awt.Dimension(880, 42));
        panSalesDetails.setMinimumSize(new java.awt.Dimension(880, 42));
        panSalesDetails.setPreferredSize(new java.awt.Dimension(880, 42));
        panSalesDetails.setLayout(new java.awt.GridBagLayout());

        lblPlace.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPlace.setText("Place");
        lblPlace.setMaximumSize(new java.awt.Dimension(82, 18));
        lblPlace.setMinimumSize(new java.awt.Dimension(82, 18));
        lblPlace.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesDetails.add(lblPlace, gridBagConstraints);

        lblSalesNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesNo.setText("Sales No : ");
        lblSalesNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSalesNo.setMaximumSize(new java.awt.Dimension(75, 18));
        lblSalesNo.setMinimumSize(new java.awt.Dimension(75, 18));
        lblSalesNo.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 26, 4, 4);
        panSalesDetails.add(lblSalesNo, gridBagConstraints);

        lblSalesDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesDate.setText("Sales Date");
        lblSalesDate.setMaximumSize(new java.awt.Dimension(70, 18));
        lblSalesDate.setMinimumSize(new java.awt.Dimension(70, 18));
        lblSalesDate.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesDetails.add(lblSalesDate, gridBagConstraints);

        tdtSalesDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panSalesDetails.add(tdtSalesDt, gridBagConstraints);

        lblSalesNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblSalesNoVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSalesNoVal.setText("Sales No");
        lblSalesNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSalesNoVal.setMaximumSize(new java.awt.Dimension(10, 18));
        lblSalesNoVal.setMinimumSize(new java.awt.Dimension(10, 18));
        lblSalesNoVal.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSalesDetails.add(lblSalesNoVal, gridBagConstraints);

        panType.setMaximumSize(new java.awt.Dimension(200, 27));
        panType.setMinimumSize(new java.awt.Dimension(200, 27));
        panType.setPreferredSize(new java.awt.Dimension(200, 27));
        panType.setLayout(new java.awt.GridBagLayout());

        rdoType.add(rdoTypeMembers);
        rdoTypeMembers.setText("Members");
        rdoTypeMembers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTypeMembersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panType.add(rdoTypeMembers, gridBagConstraints);

        rdoType.add(rdoNonMembers);
        rdoNonMembers.setText("Non Members");
        rdoNonMembers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonMembersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panType.add(rdoNonMembers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSalesDetails.add(panType, gridBagConstraints);

        txtPlace.setForeground(new java.awt.Color(255, 51, 51));
        txtPlace.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPlace.setMaximumSize(new java.awt.Dimension(150, 21));
        txtPlace.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPlace.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesDetails.add(txtPlace, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSales.add(panSalesDetails, gridBagConstraints);

        panSalesEmpDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesEmpDetails.setMinimumSize(new java.awt.Dimension(880, 120));
        panSalesEmpDetails.setPreferredSize(new java.awt.Dimension(880, 120));
        panSalesEmpDetails.setLayout(new java.awt.GridBagLayout());

        panAccountDetails.setMinimumSize(new java.awt.Dimension(300, 90));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(300, 90));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        lblACNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblACNo.setText("A/C No");
        lblACNo.setMaximumSize(new java.awt.Dimension(70, 18));
        lblACNo.setMinimumSize(new java.awt.Dimension(70, 18));
        lblACNo.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblACNo, gridBagConstraints);

        cboProdID.setMinimumSize(new java.awt.Dimension(100, 20));
        cboProdID.setPreferredSize(new java.awt.Dimension(100, 20));
        cboProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(cboProdID, gridBagConstraints);

        lblProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdID.setText("Prod ID");
        lblProdID.setMaximumSize(new java.awt.Dimension(70, 18));
        lblProdID.setMinimumSize(new java.awt.Dimension(70, 18));
        lblProdID.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblProdID, gridBagConstraints);

        lblProdDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdDesc.setText("Prod Desc");
        lblProdDesc.setMaximumSize(new java.awt.Dimension(70, 18));
        lblProdDesc.setMinimumSize(new java.awt.Dimension(70, 18));
        lblProdDesc.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblProdDesc, gridBagConstraints);

        txtProdDesc.setMaximumSize(new java.awt.Dimension(170, 21));
        txtProdDesc.setMinimumSize(new java.awt.Dimension(170, 21));
        txtProdDesc.setPreferredSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtProdDesc, gridBagConstraints);

        panName1.setMinimumSize(new java.awt.Dimension(210, 29));
        panName1.setPreferredSize(new java.awt.Dimension(128, 29));
        panName1.setLayout(new java.awt.GridBagLayout());

        txtAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcNoActionPerformed(evt);
            }
        });
        txtAcNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panName1.add(txtAcNo, gridBagConstraints);

        btnAcNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcNo.setEnabled(false);
        btnAcNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panName1.add(btnAcNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAccountDetails.add(panName1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panSalesEmpDetails.add(panAccountDetails, gridBagConstraints);

        panEmpDetails.setMinimumSize(new java.awt.Dimension(550, 120));
        panEmpDetails.setPreferredSize(new java.awt.Dimension(550, 120));
        panEmpDetails.setLayout(new java.awt.GridBagLayout());

        panEmpNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panEmpDetails.add(panEmpNo, gridBagConstraints);

        txtCreditAmt.setForeground(new java.awt.Color(255, 51, 51));
        txtCreditAmt.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCreditAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCreditAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(txtCreditAmt, gridBagConstraints);

        lblEmpNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmpNo.setText("Customer ID");
        lblEmpNo.setMaximumSize(new java.awt.Dimension(75, 18));
        lblEmpNo.setMinimumSize(new java.awt.Dimension(75, 18));
        lblEmpNo.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(lblEmpNo, gridBagConstraints);

        lblCreditAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditAmt.setText("Outstanding Amt");
        lblCreditAmt.setMaximumSize(new java.awt.Dimension(70, 18));
        lblCreditAmt.setMinimumSize(new java.awt.Dimension(70, 18));
        lblCreditAmt.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panEmpDetails.add(lblCreditAmt, gridBagConstraints);

        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setText("Name");
        lblName.setMaximumSize(new java.awt.Dimension(40, 18));
        lblName.setMinimumSize(new java.awt.Dimension(40, 18));
        lblName.setPreferredSize(new java.awt.Dimension(40, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(lblName, gridBagConstraints);

        lblNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblNameVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNameVal.setText("Name");
        lblNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameVal.setMaximumSize(new java.awt.Dimension(50, 18));
        lblNameVal.setMinimumSize(new java.awt.Dimension(50, 18));
        lblNameVal.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(lblNameVal, gridBagConstraints);

        lblSalesType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesType.setText("Sales Type");
        lblSalesType.setMaximumSize(new java.awt.Dimension(70, 18));
        lblSalesType.setMinimumSize(new java.awt.Dimension(70, 18));
        lblSalesType.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(lblSalesType, gridBagConstraints);

        cboSalesType.setMinimumSize(new java.awt.Dimension(100, 20));
        cboSalesType.setPreferredSize(new java.awt.Dimension(100, 20));
        cboSalesType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSalesTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(cboSalesType, gridBagConstraints);

        panName.setMinimumSize(new java.awt.Dimension(210, 29));
        panName.setPreferredSize(new java.awt.Dimension(128, 29));
        panName.setLayout(new java.awt.GridBagLayout());

        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIDActionPerformed(evt);
            }
        });
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panName.add(txtCustomerID, gridBagConstraints);

        btnCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustId.setEnabled(false);
        btnCustId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panName.add(btnCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panEmpDetails.add(panName, gridBagConstraints);

        btnLedger.setText("Ledger");
        btnLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLedgerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpDetails.add(btnLedger, gridBagConstraints);

        panDateDetails.setMinimumSize(new java.awt.Dimension(250, 55));
        panDateDetails.setPreferredSize(new java.awt.Dimension(250, 55));
        panDateDetails.setLayout(new java.awt.GridBagLayout());

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(65, 18));
        lblFromDate.setMinimumSize(new java.awt.Dimension(65, 18));
        lblFromDate.setPreferredSize(new java.awt.Dimension(65, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateDetails.add(lblFromDate, gridBagConstraints);

        tdtToDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDateDetails.add(tdtToDate, gridBagConstraints);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("To Date");
        lblToDate.setMaximumSize(new java.awt.Dimension(50, 18));
        lblToDate.setMinimumSize(new java.awt.Dimension(50, 18));
        lblToDate.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateDetails.add(lblToDate, gridBagConstraints);

        tdtFromDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panDateDetails.add(tdtFromDate, gridBagConstraints);

        btnGo.setForeground(new java.awt.Color(0, 153, 51));
        btnGo.setText("GO");
        btnGo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnGo.setMinimumSize(new java.awt.Dimension(52, 27));
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDateDetails.add(btnGo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 68, 0, 0);
        panEmpDetails.add(panDateDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panSalesEmpDetails.add(panEmpDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSales.add(panSalesEmpDetails, gridBagConstraints);

        panSalesProdDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesProdDetails.setMinimumSize(new java.awt.Dimension(880, 80));
        panSalesProdDetails.setPreferredSize(new java.awt.Dimension(880, 80));
        panSalesProdDetails.setLayout(new java.awt.GridBagLayout());

        lblSalesPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesPrice.setText("Sales Price");
        lblSalesPrice.setMaximumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setMinimumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblSalesPrice, gridBagConstraints);

        lblUnitType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnitType.setText("Unit Type");
        lblUnitType.setMaximumSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblUnitType, gridBagConstraints);

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtRemarks.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtRemarks, gridBagConstraints);

        txtSalesPrice.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtSalesPrice, gridBagConstraints);

        btnAdd.setForeground(new java.awt.Color(0, 153, 51));
        btnAdd.setText("Add/Mod");
        btnAdd.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panSalesProdDetails.add(btnAdd, gridBagConstraints);

        lblMRP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMRP.setText("MRP");
        lblMRP.setMaximumSize(new java.awt.Dimension(70, 18));
        lblMRP.setMinimumSize(new java.awt.Dimension(35, 18));
        lblMRP.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblMRP, gridBagConstraints);

        lblRemarks.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRemarks.setText("Remarks");
        lblRemarks.setMaximumSize(new java.awt.Dimension(70, 18));
        lblRemarks.setMinimumSize(new java.awt.Dimension(55, 18));
        lblRemarks.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblRemarks, gridBagConstraints);

        lblBarCodeNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBarCodeNumber.setText("Barcode Number");
        lblBarCodeNumber.setMaximumSize(new java.awt.Dimension(70, 18));
        lblBarCodeNumber.setMinimumSize(new java.awt.Dimension(150, 18));
        lblBarCodeNumber.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblBarCodeNumber, gridBagConstraints);

        lblProductName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProductName.setText("Product Name");
        lblProductName.setMaximumSize(new java.awt.Dimension(70, 18));
        lblProductName.setMinimumSize(new java.awt.Dimension(85, 18));
        lblProductName.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblProductName, gridBagConstraints);

        txtBarCodeNumber.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtBarCodeNumber.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBarCodeNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtBarCodeNumber, gridBagConstraints);

        txtAvailQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAvailQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAvailQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtAvailQty, gridBagConstraints);

        txtMRP.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMRP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtMRP, gridBagConstraints);

        lblAvailQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAvailQty.setText("Avail.Qty");
        lblAvailQty.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAvailQty.setMinimumSize(new java.awt.Dimension(60, 18));
        lblAvailQty.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblAvailQty, gridBagConstraints);

        lblQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQty.setText("Qty");
        lblQty.setMaximumSize(new java.awt.Dimension(70, 18));
        lblQty.setMinimumSize(new java.awt.Dimension(35, 18));
        lblQty.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(lblQty, gridBagConstraints);

        txtQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtQty, gridBagConstraints);

        panProductName.setLayout(new java.awt.GridBagLayout());

        txtProductName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductName.add(txtProductName, gridBagConstraints);

        btnProductName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductName.setEnabled(false);
        btnProductName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnProductName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProductName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductName.add(btnProductName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(panProductName, gridBagConstraints);

        txtUnitType.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUnitType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtUnitType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalesProdDetails.add(txtUnitType, gridBagConstraints);

        btnTblDelete.setForeground(new java.awt.Color(0, 153, 51));
        btnTblDelete.setText("Delete");
        btnTblDelete.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnTblDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTblDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panSalesProdDetails.add(btnTblDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panSales.add(panSalesProdDetails, gridBagConstraints);

        panSalesTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesTableDetails.setMaximumSize(new java.awt.Dimension(880, 145));
        panSalesTableDetails.setMinimumSize(new java.awt.Dimension(880, 145));
        panSalesTableDetails.setPreferredSize(new java.awt.Dimension(880, 145));
        panSalesTableDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW1.setMaximumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW1.setMinimumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW1.setPreferredSize(new java.awt.Dimension(870, 135));
        srpTable_ASW1.setRequestFocusEnabled(false);

        tblSalesDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblSalesDetails.setMinimumSize(new java.awt.Dimension(400, 700));
        tblSalesDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblSalesDetails.setPreferredSize(new java.awt.Dimension(400, 700));
        tblSalesDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalesDetailsMousePressed(evt);
            }
        });
        srpTable_ASW1.setViewportView(tblSalesDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesTableDetails.add(srpTable_ASW1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSales.add(panSalesTableDetails, gridBagConstraints);

        panTotalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));
        panTotalDetails.setMinimumSize(new java.awt.Dimension(880, 140));
        panTotalDetails.setPreferredSize(new java.awt.Dimension(880, 140));
        panTotalDetails.setLayout(new java.awt.GridBagLayout());

        lblTranspotationCharges.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTranspotationCharges.setText("Transpotation Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblTranspotationCharges, gridBagConstraints);

        lblAdjust.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdjust.setText("Adjust");
        lblAdjust.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAdjust.setMinimumSize(new java.awt.Dimension(70, 18));
        lblAdjust.setPreferredSize(new java.awt.Dimension(40, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblAdjust, gridBagConstraints);

        lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscount.setText("Discount");
        lblDiscount.setMaximumSize(new java.awt.Dimension(70, 18));
        lblDiscount.setMinimumSize(new java.awt.Dimension(70, 18));
        lblDiscount.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblDiscount, gridBagConstraints);

        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("Total");
        lblTotal.setMaximumSize(new java.awt.Dimension(70, 18));
        lblTotal.setMinimumSize(new java.awt.Dimension(70, 18));
        lblTotal.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 11, 4);
        panTotalDetails.add(lblTotal, gridBagConstraints);

        txtAdjust.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAdjust.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdjust.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdjustFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtAdjust, gridBagConstraints);

        txtDiscount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDiscount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtDiscount, gridBagConstraints);

        txtTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 11, 4);
        panTotalDetails.add(txtTotal, gridBagConstraints);

        lblGrandTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrandTotal.setText("Grand Total");
        lblGrandTotal.setMaximumSize(new java.awt.Dimension(70, 18));
        lblGrandTotal.setMinimumSize(new java.awt.Dimension(70, 18));
        lblGrandTotal.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblGrandTotal, gridBagConstraints);

        lblCash.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCash.setText("Cash");
        lblCash.setMaximumSize(new java.awt.Dimension(70, 18));
        lblCash.setMinimumSize(new java.awt.Dimension(70, 18));
        lblCash.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblCash, gridBagConstraints);

        txtGrandTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtGrandTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtGrandTotal, gridBagConstraints);

        txtCash.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCash.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCash.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtCash, gridBagConstraints);

        panTotalRadioDetails.setMinimumSize(new java.awt.Dimension(150, 25));
        panTotalRadioDetails.setPreferredSize(new java.awt.Dimension(200, 25));

        rdoCeil.setText("Ceil");
        panTotalRadioDetails.add(rdoCeil);

        rdoFloor.setText("Floor");
        panTotalRadioDetails.add(rdoFloor);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panTotalDetails.add(panTotalRadioDetails, gridBagConstraints);

        txtTranspotationCharges.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTranspotationCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTranspotationCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTranspotationChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtTranspotationCharges, gridBagConstraints);

        panInstallmentDetails.setMinimumSize(new java.awt.Dimension(410, 90));
        panInstallmentDetails.setPreferredSize(new java.awt.Dimension(410, 90));
        panInstallmentDetails.setLayout(new java.awt.GridBagLayout());

        chkInstallment.setText("Installment ");
        chkInstallment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkInstallment.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkInstallment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInstallmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 54);
        panInstallmentDetails.add(chkInstallment, gridBagConstraints);

        lblInstallmentNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstallmentNo.setText("Installment.No");
        lblInstallmentNo.setMaximumSize(new java.awt.Dimension(85, 18));
        lblInstallmentNo.setMinimumSize(new java.awt.Dimension(85, 18));
        lblInstallmentNo.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(lblInstallmentNo, gridBagConstraints);

        txtInstallmentNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInstallmentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstallmentNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstallmentNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(txtInstallmentNo, gridBagConstraints);

        lblFirstInstAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFirstInstAmt.setText("First Inst Amt");
        lblFirstInstAmt.setMaximumSize(new java.awt.Dimension(90, 18));
        lblFirstInstAmt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFirstInstAmt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(lblFirstInstAmt, gridBagConstraints);

        txtFirstInstAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFirstInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(txtFirstInstAmt, gridBagConstraints);

        txtInstallmentAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInstallmentAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(txtInstallmentAmt, gridBagConstraints);

        lblInstallmentAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstallmentAmt.setText("Installment Amt");
        lblInstallmentAmt.setMaximumSize(new java.awt.Dimension(95, 18));
        lblInstallmentAmt.setMinimumSize(new java.awt.Dimension(95, 18));
        lblInstallmentAmt.setPreferredSize(new java.awt.Dimension(95, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(lblInstallmentAmt, gridBagConstraints);

        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("Date");
        lblDate.setMaximumSize(new java.awt.Dimension(30, 18));
        lblDate.setMinimumSize(new java.awt.Dimension(30, 18));
        lblDate.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstallmentDetails.add(lblDate, gridBagConstraints);

        tdtSalesDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panInstallmentDetails.add(tdtSalesDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panTotalDetails.add(panInstallmentDetails, gridBagConstraints);

        txtDiscPercentage.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDiscPercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDiscPercentage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscPercentageFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(txtDiscPercentage, gridBagConstraints);

        lblDiscPercentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscPercentage.setText("Discount %");
        lblDiscPercentage.setMaximumSize(new java.awt.Dimension(70, 18));
        lblDiscPercentage.setMinimumSize(new java.awt.Dimension(70, 18));
        lblDiscPercentage.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDetails.add(lblDiscPercentage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panSales.add(panTotalDetails, gridBagConstraints);

        tabTradingSales.addTab("Sales", panSales);

        panSalesReturn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalesReturn.setMaximumSize(new java.awt.Dimension(947, 456));
        panSalesReturn.setMinimumSize(new java.awt.Dimension(947, 456));
        panSalesReturn.setPreferredSize(new java.awt.Dimension(1359, 786));
        panSalesReturn.setLayout(new java.awt.GridBagLayout());

        panCutOff.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSalesReturn.add(panCutOff, gridBagConstraints);

        panSalesReturnTotalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));
        panSalesReturnTotalDetails.setMinimumSize(new java.awt.Dimension(880, 50));
        panSalesReturnTotalDetails.setPreferredSize(new java.awt.Dimension(880, 50));
        panSalesReturnTotalDetails.setLayout(new java.awt.GridBagLayout());

        lblSales.setText("Sales");
        lblSales.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSales.setMaximumSize(new java.awt.Dimension(45, 18));
        lblSales.setMinimumSize(new java.awt.Dimension(45, 18));
        lblSales.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnTotalDetails.add(lblSales, gridBagConstraints);

        lblReturn.setText("Return");
        lblReturn.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblReturn.setMaximumSize(new java.awt.Dimension(45, 18));
        lblReturn.setMinimumSize(new java.awt.Dimension(45, 18));
        lblReturn.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnTotalDetails.add(lblReturn, gridBagConstraints);

        txtSales.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSales.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnTotalDetails.add(txtSales, gridBagConstraints);

        txtReturn.setMaximumSize(new java.awt.Dimension(100, 21));
        txtReturn.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnTotalDetails.add(txtReturn, gridBagConstraints);

        lblAllignment.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment.setMaximumSize(new java.awt.Dimension(450, 18));
        lblAllignment.setMinimumSize(new java.awt.Dimension(450, 18));
        lblAllignment.setPreferredSize(new java.awt.Dimension(450, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panSalesReturnTotalDetails.add(lblAllignment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 119, 0);
        panSalesReturn.add(panSalesReturnTotalDetails, gridBagConstraints);

        panSalesReturnSubDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesReturnSubDetails.setMinimumSize(new java.awt.Dimension(600, 200));
        panSalesReturnSubDetails.setPreferredSize(new java.awt.Dimension(600, 200));
        panSalesReturnSubDetails.setLayout(new java.awt.GridBagLayout());

        lblSalesretNo.setText("Sales Ret No");
        lblSalesretNo.setMaximumSize(new java.awt.Dimension(80, 18));
        lblSalesretNo.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSalesretNo.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesretNo, gridBagConstraints);

        lblSalesReturnDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesReturnDate, gridBagConstraints);

        txtSalesretNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesretNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(txtSalesretNo, gridBagConstraints);

        lblSalesReturnSalesType.setText("Sales Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesReturnSalesType, gridBagConstraints);

        lblSalesReturnSalesDate.setText("Sales Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesReturnSalesDate, gridBagConstraints);

        lblSalesRetCustID.setText("CustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesRetCustID, gridBagConstraints);

        txtSalesReturnSalesType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReturnSalesType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(txtSalesReturnSalesType, gridBagConstraints);

        txtBankAcHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(txtBankAcHead, gridBagConstraints);

        lblSalesReturnSalesNo.setText("Sales No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesReturnSalesNo, gridBagConstraints);

        lblSalesReturnName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesReturnName, gridBagConstraints);

        tdtSalesReturnDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(tdtSalesReturnDate, gridBagConstraints);

        tdtSalesReturnSalesDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(tdtSalesReturnSalesDate, gridBagConstraints);

        panEmpNo1.setLayout(new java.awt.GridBagLayout());

        txtSalesReturnSalNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesReturnSalNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesReturnSalNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpNo1.add(txtSalesReturnSalNo, gridBagConstraints);

        btnSalesReturnSalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesReturnSalNo.setEnabled(false);
        btnSalesReturnSalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnSalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnSalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesReturnSalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesReturnSalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpNo1.add(btnSalesReturnSalNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(panEmpNo1, gridBagConstraints);

        panEmpNo2.setLayout(new java.awt.GridBagLayout());

        txtSalesReturnCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesReturnCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesReturnCustIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpNo2.add(txtSalesReturnCustID, gridBagConstraints);

        btnSalesReturnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesReturnCustID.setEnabled(false);
        btnSalesReturnCustID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesReturnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesReturnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesReturnCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpNo2.add(btnSalesReturnCustID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(panEmpNo2, gridBagConstraints);

        lblBankAcHead.setText("Bank Ac Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblBankAcHead, gridBagConstraints);

        lblSalesRetNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblSalesRetNameVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSalesRetNameVal.setText("Name");
        lblSalesRetNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSalesRetNameVal.setMaximumSize(new java.awt.Dimension(50, 18));
        lblSalesRetNameVal.setMinimumSize(new java.awt.Dimension(50, 18));
        lblSalesRetNameVal.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReturnSubDetails.add(lblSalesRetNameVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 64, 1);
        panSalesReturn.add(panSalesReturnSubDetails, gridBagConstraints);

        panSalesReturnTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesReturnTableDetails.setMaximumSize(new java.awt.Dimension(880, 145));
        panSalesReturnTableDetails.setMinimumSize(new java.awt.Dimension(880, 145));
        panSalesReturnTableDetails.setPreferredSize(new java.awt.Dimension(880, 145));
        panSalesReturnTableDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW2.setMaximumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW2.setMinimumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW2.setPreferredSize(new java.awt.Dimension(870, 135));
        srpTable_ASW2.setRequestFocusEnabled(false);

        tblSalesReturn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblSalesReturn.setMinimumSize(new java.awt.Dimension(400, 700));
        tblSalesReturn.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblSalesReturn.setPreferredSize(new java.awt.Dimension(400, 700));
        tblSalesReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalesReturnMousePressed(evt);
            }
        });
        tblSalesReturn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblSalesReturnKeyReleased(evt);
            }
        });
        srpTable_ASW2.setViewportView(tblSalesReturn);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesReturnTableDetails.add(srpTable_ASW2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panSalesReturn.add(panSalesReturnTableDetails, gridBagConstraints);

        tabTradingSales.addTab("SalesReturn", panSalesReturn);

        panSalesReprint.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalesReprint.setMaximumSize(new java.awt.Dimension(947, 456));
        panSalesReprint.setMinimumSize(new java.awt.Dimension(947, 456));
        panSalesReprint.setPreferredSize(new java.awt.Dimension(947, 456));
        panSalesReprint.setLayout(new java.awt.GridBagLayout());

        panSalesReprintDetails.setMaximumSize(new java.awt.Dimension(900, 256));
        panSalesReprintDetails.setMinimumSize(new java.awt.Dimension(900, 256));
        panSalesReprintDetails.setPreferredSize(new java.awt.Dimension(900, 256));
        panSalesReprintDetails.setLayout(new java.awt.GridBagLayout());

        panSalesReprintSubDetails.setMaximumSize(new java.awt.Dimension(900, 60));
        panSalesReprintSubDetails.setMinimumSize(new java.awt.Dimension(900, 60));
        panSalesReprintSubDetails.setPreferredSize(new java.awt.Dimension(900, 60));
        panSalesReprintSubDetails.setLayout(new java.awt.GridBagLayout());

        lblSalesReprintNo.setText("Sales No");
        lblSalesReprintNo.setMaximumSize(new java.awt.Dimension(65, 18));
        lblSalesReprintNo.setMinimumSize(new java.awt.Dimension(65, 18));
        lblSalesReprintNo.setPreferredSize(new java.awt.Dimension(65, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(lblSalesReprintNo, gridBagConstraints);

        lblSalesReprintSalesDate.setText("Sales Date");
        lblSalesReprintSalesDate.setMaximumSize(new java.awt.Dimension(65, 18));
        lblSalesReprintSalesDate.setMinimumSize(new java.awt.Dimension(65, 18));
        lblSalesReprintSalesDate.setPreferredSize(new java.awt.Dimension(65, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(lblSalesReprintSalesDate, gridBagConstraints);

        btnSalesReprintNo.setText("...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(btnSalesReprintNo, gridBagConstraints);

        lblSalesReprintName.setText("Name");
        lblSalesReprintName.setMaximumSize(new java.awt.Dimension(40, 18));
        lblSalesReprintName.setMinimumSize(new java.awt.Dimension(40, 18));
        lblSalesReprintName.setPreferredSize(new java.awt.Dimension(40, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(lblSalesReprintName, gridBagConstraints);

        lblType.setText("Type");
        lblType.setMaximumSize(new java.awt.Dimension(30, 18));
        lblType.setMinimumSize(new java.awt.Dimension(30, 18));
        lblType.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(lblType, gridBagConstraints);

        txtSalesReprintName.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReprintName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(txtSalesReprintName, gridBagConstraints);

        txtSalesReprintNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesReprintNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(txtSalesReprintNo, gridBagConstraints);

        rdoSal.setText("Sal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(rdoSal, gridBagConstraints);

        rdoRet.setText("Ret");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(rdoRet, gridBagConstraints);

        btnSalesReprintAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintSubDetails.add(btnSalesReprintAdd, gridBagConstraints);

        tdtSalesReprintSalesDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panSalesReprintSubDetails.add(tdtSalesReprintSalesDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSalesReprintDetails.add(panSalesReprintSubDetails, gridBagConstraints);

        panSalesReprintTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalesReprintTableDetails.setMaximumSize(new java.awt.Dimension(900, 200));
        panSalesReprintTableDetails.setMinimumSize(new java.awt.Dimension(900, 200));
        panSalesReprintTableDetails.setPreferredSize(new java.awt.Dimension(900, 200));
        panSalesReprintTableDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW3.setMaximumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW3.setMinimumSize(new java.awt.Dimension(870, 135));
        srpTable_ASW3.setRequestFocusEnabled(false);

        tblSalesRePrint.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblSalesRePrint.setMinimumSize(new java.awt.Dimension(400, 700));
        tblSalesRePrint.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblSalesRePrint.setPreferredSize(new java.awt.Dimension(400, 700));
        tblSalesRePrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalesRePrintMousePressed(evt);
            }
        });
        srpTable_ASW3.setViewportView(tblSalesRePrint);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        panSalesReprintTableDetails.add(srpTable_ASW3, gridBagConstraints);

        panSalTotal.setMaximumSize(new java.awt.Dimension(850, 30));
        panSalTotal.setMinimumSize(new java.awt.Dimension(850, 30));
        panSalTotal.setPreferredSize(new java.awt.Dimension(850, 30));
        panSalTotal.setLayout(new java.awt.GridBagLayout());

        lblSalTotal.setForeground(new java.awt.Color(0, 51, 204));
        lblSalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalTotal.setText("Sal Total");
        lblSalTotal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSalTotal.setMaximumSize(new java.awt.Dimension(10, 15));
        lblSalTotal.setMinimumSize(new java.awt.Dimension(10, 15));
        lblSalTotal.setPreferredSize(new java.awt.Dimension(10, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 56, 4, 4);
        panSalTotal.add(lblSalTotal, gridBagConstraints);

        txtSalTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 56, 4, 4);
        panSalTotal.add(txtSalTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panSalesReprintTableDetails.add(panSalTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSalesReprintDetails.add(panSalesReprintTableDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSalesReprint.add(panSalesReprintDetails, gridBagConstraints);

        panSalesReprintButtons.setMaximumSize(new java.awt.Dimension(900, 60));
        panSalesReprintButtons.setMinimumSize(new java.awt.Dimension(900, 60));
        panSalesReprintButtons.setPreferredSize(new java.awt.Dimension(900, 60));
        panSalesReprintButtons.setLayout(new java.awt.GridBagLayout());

        btnSalesReprint.setText("Print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintButtons.add(btnSalesReprint, gridBagConstraints);

        btnSalesReprintClear.setText("Clear");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintButtons.add(btnSalesReprintClear, gridBagConstraints);

        btnSalesReprintReject.setText("Reject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalesReprintButtons.add(btnSalesReprintReject, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panSalesReprint.add(panSalesReprintButtons, gridBagConstraints);

        tabTradingSales.addTab("Sales Reprint", panSalesReprint);

        panTradingSales.add(tabTradingSales, new java.awt.GridBagConstraints());

        getContentPane().add(panTradingSales, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTDSConfig.add(mnuProcess);

        setJMenuBar(mbrTDSConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        ClientUtil.enableDisable(panSales, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
        }
        btnTblDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);
    }

    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            if (panSales.isShowing() == true) {
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("SALES_NO", lblSalesNoVal.getText());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("SALES", "SALES");
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap);
                viewType = "";
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                arrList = null;
                authorizeMap = null;
            } else if (panSalesReturn.isShowing() == true) {
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("SALES_RET_NO", txtSalesretNo.getText());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("SALES_RETURN", "SALES_RETURN");
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap);
                viewType = "";
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                arrList = null;
                authorizeMap = null;
            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (panSales.isShowing() == true) {
                mapParam.put(CommonConstants.MAP_NAME, "getTradingSalesForAuthorize");
            } else if (panSalesReturn.isShowing() == true) {
                mapParam.put(CommonConstants.MAP_NAME, "getTradingSalesRetForAuthorize");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            if (panSales.isShowing() == true) {
                pan = SALES;
                observable.setAuthorizeMap(map);
                observable.doAction(pan);
            } else if (panSalesReturn.isShowing() == true) {
                pan = SALESRETURN;
                observable.setAuthorizeMap(map);
                observable.doAction(pan);
            }
            setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        //super.removeEditLock(txtTdsId.getText());
        isFilled = false;
        observable.resetSalesForm();
        observable.resetSalesRetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSales, false);
        ClientUtil.enableDisable(panSalesReturn, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        lblSalesNoVal.setText("");
        lblNameVal.setText("");
        txtSalesReturnCustID.setText("");
        txtSalesReturnSalNo.setText("");
        lblSalesRetNameVal.setText("");
        btnSalesReturnCustID.setEnabled(false);
        btnSalesReturnSalNo.setEnabled(false);
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        setSizeTableData();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (panSales.isShowing() == true) {
            if (!rdoTypeMembers.isSelected() && !rdoNonMembers.isSelected()) {
                ClientUtil.showMessageWindow("Please Select Member/Non Member...!!!");
                return;
            } else if (CommonUtil.convertObjToStr(cboSalesType.getSelectedItem()).length() <= 0) {
                ClientUtil.showMessageWindow("Sales Type should not be empty");
                return;
            } else if (CommonUtil.convertObjToStr(txtCustomerID.getText()).length() <= 0) {
                ClientUtil.showMessageWindow("Customer ID should not be empty");
                return;
            } else if (CommonUtil.convertObjToStr(cboSalesType.getSelectedItem()).equals("Credit Sale") && CommonUtil.convertObjToStr(txtAcNo.getText()).length() == 0) {
                ClientUtil.showMessageWindow("Invalid Data");
                return;
            } else {
                if (tblSalesDetails.getRowCount() > 0) {
                    savePerformed();
                    btnReject.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnException.setEnabled(true);
                } else {
                    ClientUtil.showMessageWindow("No rcords for Sales");
                    return;
                }
            }
        } else if (panSalesReturn.isShowing() == true) {
            finalList = observable.getFinalList();
            HashMap salesRetMap = new HashMap();
            if (finalList != null && finalList.size() > 0) {
                for (int i = 0; i < finalList.size(); i++) {
                    String slNo = "";
                    salesRetMap = (HashMap) finalList.get(i);
                    slNo = CommonUtil.convertObjToStr(salesRetMap.get("SL_NO"));
                    for (int j = 0; j < tblSalesReturn.getRowCount(); j++) {
                        if (CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(j, 0)).equals(slNo)) {
                            salesRetMap.put("RETURN_QTY", CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(j, 9)));
                            salesRetMap.put("RETURN_TOTAL", CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(j, 10)));
                            //salesRetMap.put("AVAIL_QTY", CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(j, 7)));
                        }
                    }
                }
                if (finalList != null && finalList.size() > 0) {
                    observable.setFinalList(finalList);
                    savePerformed();
                }
                btnReject.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnException.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panSales, false);
        if (lblSalesNoVal.getText().length() <= 0) {
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
         if (panSales.isShowing() == true) {
            
        } else if (panSalesReturn.isShowing() == true) {
            pan = SALESRETURN;
        }
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (panSales.isShowing() == true) {
            tradingSalesNew();
            pan = SALES;
        } else if (panSalesReturn.isShowing() == true) {
            tradingSalesReturnNew();
            pan = SALESRETURN;
            colourList = new ArrayList();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void tblSalesDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesDetailsMousePressed
        // TODO add your handling code here: 
        if (tblSalesDetails.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblSalesDetails.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblSalesDetails.getValueAt(tblSalesDetails.getSelectedRow(), 0));
            observable.populateSalesTableDetails(st);
//            HashMap stkMap = new HashMap();
//            if (stock_ID != null && stock_ID.length() > 0) {
//                stkMap.put("STOCK_ID", stock_ID);
//                List stkLst = ClientUtil.executeQuery("getStockQuantity", stkMap);
//                if (stkLst != null && stkLst.size() > 0) {
//                    stkMap = (HashMap) stkLst.get(0);
//                    txtAvailQty.setText(CommonUtil.convertObjToStr(stkMap.get("STOCK_QUANT")));
//                }
//            }
            salesTableUpdate();
        }
    }//GEN-LAST:event_tblSalesDetailsMousePressed

    private void tblSalesRePrintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesRePrintMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSalesRePrintMousePressed

    private void cboSalesTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSalesTypeActionPerformed
        // TODO add your handling code here:
        if (cboSalesType.getSelectedIndex() > 0) {
            if (cboSalesType.getSelectedItem().equals("Credit Sale")) {
                ClientUtil.enableDisable(panAccountDetails, true);
                //btnLedger.setEnabled(true);
                panAccountDetails.setVisible(true);
                panInstallmentDetails.setVisible(true);
                btnAcNo.setEnabled(true);
                lblInstallmentNo.setVisible(false);
                txtInstallmentNo.setVisible(false);
                lblInstallmentAmt.setVisible(false);
                txtInstallmentAmt.setVisible(false);
                lblFirstInstAmt.setVisible(false);
                txtFirstInstAmt.setVisible(false);
                lblDate.setVisible(false);
                tdtSalesDate.setVisible(false);
                lblCash.setText("Credit");

            } else {
                ClientUtil.enableDisable(panAccountDetails, false);
                panAccountDetails.setVisible(false);
                panInstallmentDetails.setVisible(false);
                cboProdID.setSelectedItem("");
                txtProdDesc.setText("");
                txtAcNo.setText("");
                txtInstallmentNo.setText("");
                txtInstallmentAmt.setText("");
                txtFirstInstAmt.setText("");
                tdtSalesDate.setDateValue("");
                lblCash.setText("Cash");
            }
        }
    }//GEN-LAST:event_cboSalesTypeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (txtProductName.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Product Name Should Not be Empty ...!!!");
            return;
        } else if (txtQty.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Quantity Should Not be Empty ...!!!");
        } else if (CommonUtil.convertObjToInt(txtQty.getText()) > CommonUtil.convertObjToInt(txtAvailQty.getText())) {
            ClientUtil.showMessageWindow("Quantity should be lesser than Available Qty...!!!");
            txtQty.setText("");
            return;
        } else {
            addQuantityBaseAmountCalc(prod_ID);
            updateSalesOBFields();
            observable.addDataToSalesDetailsTable(updateTab, updateMode, tax);
            updateMode = false;
            totalCatAmountCalc();
            txtAdjust.setText(CommonUtil.convertObjToStr(totalAdjust));
            txtTranspotationCharges.setText(CommonUtil.convertObjToStr(totalAdjust));
            observable.setProdName("");
            observable.setTxtQty("");
            txtProductName.setText("");
            txtQty.setText("");
            txtAvailQty.setText("");
            txtSalesPrice.setText("");
            txtMRP.setText("");
            txtRemarks.setText("");
            txtUnitType.setText("");
            setSizeTableData();
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtProductNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameFocusLost

    private void btnProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductNameActionPerformed
        // TODO add your handling code here:
        callView("PROD_NAME");
    }//GEN-LAST:event_btnProductNameActionPerformed

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        // TODO add your handling code here:
        observable.setQty(CommonUtil.convertObjToStr(txtQty.getText()));
    }//GEN-LAST:event_txtQtyFocusLost

    private void tblSalesReturnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesReturnMousePressed
        // TODO add your handling code here:
        Date salesDt = null;
        Date currDt = null;
        int value = 0;
        int days = 0;
        String period = "";
        salesDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSalesReturnSalesDate.getDateValue()));
        currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtSalesReturnDate.getDateValue()));
        value = CommonUtil.convertObjToInt(DateUtil.dateDiff(salesDt, currDt));
        HashMap calcMap = new HashMap();
        List calcLst = ClientUtil.executeQuery("getCalcSalesRetDays", calcMap);
        if (calcLst != null && calcLst.size() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                calcMap = (HashMap) calcLst.get(0);
                days = CommonUtil.convertObjToInt(calcMap.get("SALES_RET_BEFORE"));
                period = CommonUtil.convertObjToStr(calcMap.get("PERIOD"));
                if (CommonUtil.convertObjToStr(period).equals("DAYS")) {
                    if (days < value) {
                        ClientUtil.showMessageWindow("Not Allowed to return!!!!");
                        return;
                    }
                }
            }
        }

    }//GEN-LAST:event_tblSalesReturnMousePressed

    private void txtSalesReturnSalNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesReturnSalNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesReturnSalNoFocusLost

    private void btnSalesReturnSalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesReturnSalNoActionPerformed
        // TODO add your handling code here:
        callView("SALES_NO");
    }//GEN-LAST:event_btnSalesReturnSalNoActionPerformed

    private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerIDActionPerformed

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if (txtCustomerID.getText() != null && txtCustomerID.getText().length() > 0) {
            showingCustomerDetails(txtCustomerID.getText());
        }
    }//GEN-LAST:event_txtCustomerIDFocusLost

    private void showingCustomerDetails(String custId) {
        HashMap customerMap = new HashMap();
        String supplierID = "";
        customerMap.put("CUST_ID", custId);
        customerMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        List custListData = ClientUtil.executeQuery("getSelectCustDetails", customerMap);
        if (custListData != null && custListData.size() > 0) {
            customerMap = (HashMap) custListData.get(0);
            if (panSales.isShowing() == true) {
                lblNameVal.setText(CommonUtil.convertObjToStr(customerMap.get("NAME")));
            } else if (panSalesReturn.isShowing() == true) {
                lblSalesRetNameVal.setText(CommonUtil.convertObjToStr(customerMap.get("NAME")));
            }
        }
    }

    private void tradingSalesNew() {
        updateMode = false;
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setNewData(true);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panSales, true);
        ClientUtil.clearAll(this);
        HashMap branchMap = TrueTransactMain.BRANCHINFO;
        txtPlace.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
        txtCreditAmt.setEditable(false);
        //txtCreditAmt.setEnabled(false);
        txtUnitType.setEnabled(false);
        txtProductName.setEnabled(true);
        txtPlace.setEnabled(false);
        tdtSalesDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        txtCustomerID.setEnabled(true);
        btnAdd.setEnabled(true);
        txtTranspotationCharges.setEnabled(true);
        btnCustId.setEnabled(true);
        btnProductName.setEnabled(true);
        lblSalesNoVal.setEnabled(false);
        btnLedger.setEnabled(false);
        tdtSalesDt.setEnabled(false);
        txtSalesPrice.setEnabled(false);
        txtMRP.setEnabled(false);
        txtAvailQty.setEnabled(false);
        txtCash.setEnabled(false);
        txtAdjust.setEnabled(false);
        txtTotal.setEnabled(false);
        txtDiscount.setEnabled(false);
        lblSalesNoVal.setText("");
        lblNameVal.setText("");
        txtTranspotationCharges.setEnabled(false);
        txtGrandTotal.setEnabled(false);
        lblStatus.setText("New");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }

    private void tradingSalesReturnNew() {
        updateMode = false;
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        HashMap branchMap = TrueTransactMain.BRANCHINFO;
        txtSalesReturnCustID.setEnabled(true);
        btnSalesReturnCustID.setEnabled(true);
        txtSalesReturnSalNo.setEnabled(true);
        btnSalesReturnSalNo.setEnabled(true);
        tdtSalesReturnDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }

    private void setClearData() {
        txtProductName.setText("");
        txtQty.setText("");
        txtAvailQty.setText("");
        txtSalesPrice.setText("");
        txtMRP.setText("");
        txtRemarks.setText("");
        txtUnitType.setText("");
    }

    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIdActionPerformed

    private void cboProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIDActionPerformed
        // TODO add your handling code here:
        if (cboProdID.getSelectedIndex() > 0) {
            txtProdDesc.setText(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdID.getModel()).getKeyForSelected()));
//            HashMap accMap = new HashMap();
//            accMap.put("CUSTOMER_ID", CommonUtil.convertObjToStr(txtCustomerID.getText()));
//            accMap.put("PROD_ID", CommonUtil.convertObjToStr(cboProdID.getSelectedItem()));
//            List accLst = ClientUtil.executeQuery("getSuspenseAcNoForSale", accMap);
//            if (accLst != null && accLst.size() > 0) {
//                accMap = (HashMap) accLst.get(0);
//                txtAcNo.setText(CommonUtil.convertObjToStr(accMap.get("SUSPENSE_ACCT_NUM")));
//                txtCreditAmt.setText(CommonUtil.convertObjToStr(accMap.get("AVAILABLE_BALANCE")));
//            }
        }
    }//GEN-LAST:event_cboProdIDActionPerformed

    private void txtAcNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcNoActionPerformed

    private void txtAcNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcNoFocusLost

    private void btnAcNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcNoActionPerformed
        // TODO add your handling code here:
        callView("AC_NO");
    }//GEN-LAST:event_btnAcNoActionPerformed

    private void chkInstallmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInstallmentActionPerformed
        // TODO add your handling code here:
        if (chkInstallment.isSelected()) {
            lblInstallmentNo.setVisible(true);
            lblInstallmentAmt.setVisible(true);
            lblFirstInstAmt.setVisible(true);
            lblDate.setVisible(true);
            txtInstallmentNo.setVisible(true);
            txtInstallmentAmt.setVisible(true);
            txtFirstInstAmt.setVisible(true);
            tdtSalesDate.setVisible(true);
            txtInstallmentAmt.setEnabled(false);
            txtInstallmentNo.setText("1");
            tdtSalesDate.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDt, 30)));
        } else {
            lblInstallmentNo.setVisible(false);
            lblInstallmentAmt.setVisible(false);
            lblFirstInstAmt.setVisible(false);
            lblDate.setVisible(false);
            txtInstallmentNo.setVisible(false);
            txtInstallmentAmt.setVisible(false);
            txtFirstInstAmt.setVisible(false);
            tdtSalesDate.setVisible(false);
        }
    }//GEN-LAST:event_chkInstallmentActionPerformed

    private void rdoNonMembersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonMembersActionPerformed
        // TODO add your handling code here:
        if (rdoNonMembers.isSelected()) {
            lblEmpNo.setText("Name");
            btnCustId.setVisible(false);
            lblName.setVisible(false);
            lblNameVal.setVisible(false);
            cboSalesType.setSelectedItem("Cash Sale");
            cboSalesType.setEnabled(false);
        } else {
            lblEmpNo.setText("Customer ID");
            btnCustId.setVisible(true);
            lblName.setVisible(true);
            lblNameVal.setVisible(true);
            cboSalesType.setSelectedItem("");
            cboSalesType.setEnabled(true);
        }
    }//GEN-LAST:event_rdoNonMembersActionPerformed

    private void rdoTypeMembersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTypeMembersActionPerformed
        // TODO add your handling code here:
        if (rdoTypeMembers.isSelected()) {
            lblEmpNo.setText("Customer ID");
            btnCustId.setVisible(true);
            lblName.setVisible(true);
            lblNameVal.setVisible(true);
            cboSalesType.setSelectedItem("");
            cboSalesType.setEnabled(true);
        }
    }//GEN-LAST:event_rdoTypeMembersActionPerformed

    private void txtAdjustFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdjustFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdjustFocusLost

    private void txtDiscPercentageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscPercentageFocusLost
        // TODO add your handling code here:
        discAmountCalc();
    }//GEN-LAST:event_txtDiscPercentageFocusLost

    private void txtTranspotationChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTranspotationChargesFocusLost
        // TODO add your handling code here:
        tcpAmountCalc();
    }//GEN-LAST:event_txtTranspotationChargesFocusLost

    private void btnTblDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTblDeleteActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblSalesDetails.getValueAt(tblSalesDetails.getSelectedRow(), 0));
        observable.deleteSalesDetails(st, tblSalesDetails.getSelectedRow());
        totalCatAmountCalc();
        observable.resetSalesTable();
        setClearData();

    }//GEN-LAST:event_btnTblDeleteActionPerformed

    private void txtInstallmentNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstallmentNoFocusLost
        // TODO add your handling code here:
        if (txtInstallmentNo.getText() != null && txtInstallmentNo.getText().length() > 0) {
            int instNo = 0;
            Double totAmt = 0.0;
            Double instAmt = 0.0;
            Double lastInstAmt = 0.0;
            Double totInstAmt = 0.0;
            Double diffAmt = 0.0;
            instNo = CommonUtil.convertObjToInt(txtInstallmentNo.getText());
            totAmt = CommonUtil.convertObjToDouble(txtGrandTotal.getText());
            instAmt = totAmt / instNo;
            Rounding rod = new Rounding();
            instAmt = (double) rod.getNearest((long) (instAmt * 100), 100) / 100;
            totInstAmt = instAmt * instNo;
            diffAmt = totInstAmt - totAmt;
            lastInstAmt = instAmt - diffAmt;
            txtInstallmentAmt.setText(CommonUtil.convertObjToStr(instAmt));
            txtFirstInstAmt.setText(CommonUtil.convertObjToStr(lastInstAmt));
        }
    }//GEN-LAST:event_txtInstallmentNoFocusLost

    private void btnLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLedgerActionPerformed
        // TODO add your handling code here:
        panDateDetails.setVisible(true);
        panDateDetails.setEnabled(true);
//        HashMap newMap = new HashMap();
//        HashMap ledgerMap = new HashMap();
//        HashMap balanceMap = new HashMap();
//        Double balance = 0.0;
//        ArrayList balanceList = new ArrayList();
//        newMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcNo.getText()));
//        List calcLedgerLst = ClientUtil.executeQuery("getCalcForLedger", newMap);
//        if (calcLedgerLst != null && calcLedgerLst.size() > 0) {
//            balanceMap = calcBalanceAmount(calcLedgerLst);
//            balanceList = (ArrayList) balanceMap.get("BALANCE");
//            System.out.println("@#@#@#@#@#@#@#@# balanceMap :" + balanceMap);
//            System.out.println("@#@#@#@#@#@#@#@# calcLedgerLst" + calcLedgerLst);
//            for (int i = 0; i < calcLedgerLst.size(); i++) {
//                newMap = (HashMap) calcLedgerLst.get(i);
//                balanceMap.put("BALANCE", balanceList.get(i));
//                balance = CommonUtil.convertObjToDouble(balanceMap.get("BALANCE"));
//                if (balance < 0) {
//                    balance = balance * -1;
//                    newMap.put("BALANCE", balance);
//                } else {
//                    newMap.put("BALANCE", balanceList.get(i));
//                }
//                ledgerMap.put(String.valueOf(i), newMap);
//                System.out.println("@#@#@#@#@#@#@#@# newMap" + newMap);
//                System.out.println("@#@#@#@#@#@#@#@# balanceList" + balanceList);
//            }
//            System.out.println(ledgerMap);
//            if (ledgerMap != null && ledgerMap.size() > 0) {
//                newMap = new HashMap();
//                newMap.put("TRADING SALES",ledgerMap);
//                TableDialogUI tableDialogUI = null;
//                tableDialogUI = new TableDialogUI(newMap, "TRADING SALES");
//                tableDialogUI.setTitle("Sales Ledger");
//                tableDialogUI.show();
//            }
//        }
    }//GEN-LAST:event_btnLedgerActionPerformed

    private void btnGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoActionPerformed
        // TODO add your handling code here:
        HashMap newMap = new HashMap();
        HashMap ledgerMap = new HashMap();
        HashMap balanceMap = new HashMap();
        Date fromDt = null;
        Date toDt = null;
        Double balance = 0.0;
        ArrayList balanceList = new ArrayList();
        fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
        toDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDate.getDateValue()));
        newMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcNo.getText()));
        newMap.put("FROM_DT", fromDt);
        newMap.put("TO_DT", toDt);
        List calcLedgerLst = ClientUtil.executeQuery("getCalcForLedger", newMap);
        if (calcLedgerLst != null && calcLedgerLst.size() > 0) {
            balanceMap = calcBalanceAmount(calcLedgerLst);
            balanceList = (ArrayList) balanceMap.get("BALANCE");
            for (int i = 0; i < calcLedgerLst.size(); i++) {
                newMap = (HashMap) calcLedgerLst.get(i);
                balanceMap.put("BALANCE", balanceList.get(i));
                balance = CommonUtil.convertObjToDouble(balanceMap.get("BALANCE"));
                if (balance < 0) {
                    balance = balance * -1;
                    newMap.put("BALANCE", balance);
                } else {
                    newMap.put("BALANCE", balanceList.get(i));
                }
                ledgerMap.put(String.valueOf(i), newMap);
            }
            //System.out.println(ledgerMap);
            if (ledgerMap != null && ledgerMap.size() > 0) {
                newMap = new HashMap();
                newMap.put("TRADING SALES", ledgerMap);
                TableDialogUI tableDialogUI = null;
                tableDialogUI = new TableDialogUI(newMap, "TRADING SALES");
                tableDialogUI.setTitle("Sales Ledger");
                tableDialogUI.show();
            }
        } else {
            ClientUtil.showMessageWindow("Invalid Data");
            return;
        }
        panDateDetails.setVisible(false);
    }//GEN-LAST:event_btnGoActionPerformed

    private void txtSalesReturnCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesReturnCustIDFocusLost
        // TODO add your handling code here:
        if (txtSalesReturnCustID.getText() != null && txtSalesReturnCustID.getText().length() > 0) {
            showingCustomerDetails(txtSalesReturnCustID.getText());
        }
    }//GEN-LAST:event_txtSalesReturnCustIDFocusLost

    private void btnSalesReturnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesReturnCustIDActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnSalesReturnCustIDActionPerformed

    private void tblSalesReturnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSalesReturnKeyReleased
        // TODO add your handling code here:
        int availQty = 0;
        int retQty = 0;
        double rate = 0.0;
        double returnTot = 0.0;
        if ((tblSalesReturn.getRowCount() > 0) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
            HashMap stockMap = new HashMap();
            retQty = CommonUtil.convertObjToInt(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 9));
            stockMap.put("SALES_NO", txtSalesReturnSalNo.getText());
            stockMap.put("PRODUCT_NAME", CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 1)));
            stockMap.put("UNIT_TYPE", CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 3)));
            stockMap.put("RATE", CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 2)));
            List stkLst = ClientUtil.executeQuery("getSalRetQtyForStock", stockMap);
            if (stkLst != null && stkLst.size() > 0) {
                stockMap = (HashMap) stkLst.get(0);
                int returnQty = 0;
                int qty = 0;
                returnQty = CommonUtil.convertObjToInt(stockMap.get("RETURN_QTY"));
                if (returnQty > 0) {
                    qty = CommonUtil.convertObjToInt(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 4));
                    availQty = qty - returnQty;
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, " Already Return Do you want to Continue ?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    if (yesNo == 0) {
                        if (availQty < retQty) {
                            ClientUtil.showMessageWindow("Returning Qty should be equal to or lesser than Available Qty.");
                            tblSalesReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")), tblSalesReturn.getSelectedRow(), 9);
                            return;
                        } else {
                            rate = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 2));
                            returnTot = rate * retQty;
                            Rounding rod = new Rounding();
                            returnTot = (double) rod.getNearest((long) (returnTot * 100), 100) / 100;
                            tblSalesReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf(returnTot)), tblSalesReturn.getSelectedRow(), 10);
                        }
                    } else {
                        tblSalesReturn.setValueAt(CommonUtil.convertObjToInt(String.valueOf("0")), tblSalesReturn.getSelectedRow(), 9);
                        tblSalesReturn.setValueAt(CommonUtil.convertObjToInt(String.valueOf("0.00")), tblSalesReturn.getSelectedRow(), 10);
                        return;
                    }
                } else {
                    availQty = CommonUtil.convertObjToInt(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 4));
                    if (availQty < retQty) {
                        ClientUtil.showMessageWindow("Returning Qty should be equal to or lesser than Available Qty.");
                        tblSalesReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")), tblSalesReturn.getSelectedRow(), 9);
                        return;
                    } else {
                        rate = CommonUtil.convertObjToDouble(tblSalesReturn.getValueAt(tblSalesReturn.getSelectedRow(), 2));
                        returnTot = rate * retQty;
                        Rounding rod = new Rounding();
                        returnTot = (double) rod.getNearest((long) (returnTot * 100), 100) / 100;
                        tblSalesReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf(returnTot)), tblSalesReturn.getSelectedRow(), 10);
                    }
                }
                totalRetAmountCalc();
            }

//            setColorList();
//            setColour();
        }
    }//GEN-LAST:event_tblSalesReturnKeyReleased

    private void txtCashFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashFocusLost
        // TODO add your handling code here:
         discAmountCalc();
    }//GEN-LAST:event_txtCashFocusLost

    private void setColorList() {
        if (tblSalesReturn.getRowCount() > 0) {
            colourList = new ArrayList();
            int retQty = 0;
            for (int i = 0; i < tblSalesReturn.getRowCount(); i++) {
                retQty = CommonUtil.convertObjToInt(tblSalesReturn.getValueAt(i, 8));
                if (retQty > 0) {
                    colourList.add(String.valueOf(i));
                }
            }
        }
    }

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblSalesReturn.setDefaultRenderer(Object.class, renderer);
    }

    private double totalRetAmountCalc() {
        double totAmt = 0.0;
        String value = "";
        if (tblSalesReturn.getRowCount() > 0) {
            for (int i = 0; i < tblSalesReturn.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblSalesReturn.getValueAt(i, 10));
                //Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
            value = CurrencyValidation.formatCrore(String.valueOf(totAmt));
            txtReturn.setText(CommonUtil.convertObjToStr(value));
        }
        return totAmt;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private HashMap calcBalanceAmount(List calcLedgerLst) {
        HashMap returnMap = new HashMap();
        ArrayList balanceList = new ArrayList();
        double creditAmt = 0.0;
        double debitAmt = 0.0;
        double balance = 0.0;
        HashMap newMap = new HashMap();
        HashMap balanceMap = new HashMap();
        for (int i = 0; i < calcLedgerLst.size(); i++) {
            newMap = (HashMap) calcLedgerLst.get(i);
            creditAmt = CommonUtil.convertObjToDouble(newMap.get("CREDIT"));
            debitAmt = CommonUtil.convertObjToDouble(newMap.get("DEBIT"));
            balance = balance + creditAmt - debitAmt;
            balanceList.add(balance);
//        String oldAmount = "";
//        String transType = "";
//        String oldAmt = "";
//        String value = "";
//        String drAmt = "";
//        String crAmt = "";
//        double amt = 0.0;
//        if ((tblSalesDetails.getRowCount() > 1) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
//                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
//            
//                //oldAmount = CommonUtil.convertObjToStr(tblSalesDetails.getValueAt(i - 1, 10));
//                creditAmt = CommonUtil.convertObjToDouble(tblSalesDetails.getValueAt(i, 5));
//                debitAmt = CommonUtil.convertObjToDouble(tblSalesDetails.getValueAt(i, 4));
//                if ((creditAmt > 0 || debitAmt > 0)) {
//                    amt = balance + creditAmt - debitAmt;
//                    if (amt < 0) {
//                        amt = amt * -1;
//                        value = CurrencyValidation.formatCrore(String.valueOf(amt));
//                    } else {
//                        value = CurrencyValidation.formatCrore(String.valueOf(amt));
//                    }
////                    if (debitAmt > 0) {
////                        drAmt = CurrencyValidation.formatCrore(String.valueOf(debitAmt));
////                        tblSalesDetails.setValueAt((String.valueOf(drAmt)), i, 8);
////                    } else {
////                        crAmt = CurrencyValidation.formatCrore(String.valueOf(creditAmt));
////                        tblSalesDetails.setValueAt((String.valueOf(crAmt)), i, 9);
////                    }
//                    tblSalesDetails.setValueAt((String.valueOf(value)), i, 6);
////                    setRightAlignment(8);
////                    setRightAlignment(9);
////                    setRightAlignment(10);
//                }
//            }
        }
        returnMap.put("BALANCE", balanceList);
        return returnMap;
    }

    private void addQuantityBaseAmountCalc(String prod_ID) {
        if (txtQty.getText().length() > 0) {
            int qty = CommonUtil.convertObjToInt(txtQty.getText());
            double salesPrice = CommonUtil.convertObjToDouble(txtSalesPrice.getText());
            double amt = qty * salesPrice;
            observable.setTaxableAmt(CommonUtil.convertObjToStr(amt));
            observable.setCash(CommonUtil.convertObjToStr(amt));
            observable.setTotal(CommonUtil.convertObjToStr(amt));
        }
    }

    public void salesTableUpdate() {
        txtProductName.setText(observable.getTxtProductName());
        txtSalesPrice.setText(observable.getTxtSalesPrice());
        txtQty.setText(observable.getTxtQty());
        txtMRP.setText(observable.getTxtMRP());
        txtAvailQty.setText(observable.getTxtAvailQty());
        txtRemarks.setText(observable.getTxtRemarks());
        txtUnitType.setText(observable.getCboUnitType());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcNo;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGo;
    private com.see.truetransact.uicomponent.CButton btnLedger;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProductName;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSalesReprint;
    private com.see.truetransact.uicomponent.CButton btnSalesReprintAdd;
    private com.see.truetransact.uicomponent.CButton btnSalesReprintClear;
    private com.see.truetransact.uicomponent.CButton btnSalesReprintNo;
    private com.see.truetransact.uicomponent.CButton btnSalesReprintReject;
    private com.see.truetransact.uicomponent.CButton btnSalesReturnCustID;
    private com.see.truetransact.uicomponent.CButton btnSalesReturnSalNo;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTblDelete;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdID;
    private com.see.truetransact.uicomponent.CComboBox cboSalesType;
    private com.see.truetransact.uicomponent.CCheckBox chkInstallment;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblACNo;
    private com.see.truetransact.uicomponent.CLabel lblAdjust;
    private com.see.truetransact.uicomponent.CLabel lblAllignment;
    private com.see.truetransact.uicomponent.CLabel lblAvailQty;
    private com.see.truetransact.uicomponent.CLabel lblBankAcHead;
    private com.see.truetransact.uicomponent.CLabel lblBarCodeNumber;
    private com.see.truetransact.uicomponent.CLabel lblCash;
    private com.see.truetransact.uicomponent.CLabel lblCreditAmt;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDiscPercentage;
    private com.see.truetransact.uicomponent.CLabel lblDiscount;
    private com.see.truetransact.uicomponent.CLabel lblEmpNo;
    private com.see.truetransact.uicomponent.CLabel lblFirstInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGrandTotal;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentAmt;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNo;
    private com.see.truetransact.uicomponent.CLabel lblMRP;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNameVal;
    private com.see.truetransact.uicomponent.CLabel lblPlace;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReturn;
    private com.see.truetransact.uicomponent.CLabel lblSalTotal;
    private com.see.truetransact.uicomponent.CLabel lblSales;
    private com.see.truetransact.uicomponent.CLabel lblSalesDate;
    private com.see.truetransact.uicomponent.CLabel lblSalesNo;
    private com.see.truetransact.uicomponent.CLabel lblSalesNoVal;
    private com.see.truetransact.uicomponent.CLabel lblSalesPrice;
    private com.see.truetransact.uicomponent.CLabel lblSalesReprintName;
    private com.see.truetransact.uicomponent.CLabel lblSalesReprintNo;
    private com.see.truetransact.uicomponent.CLabel lblSalesReprintSalesDate;
    private com.see.truetransact.uicomponent.CLabel lblSalesRetCustID;
    private com.see.truetransact.uicomponent.CLabel lblSalesRetNameVal;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnDate;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnName;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnSalesDate;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnSalesNo;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnSalesType;
    private com.see.truetransact.uicomponent.CLabel lblSalesType;
    private com.see.truetransact.uicomponent.CLabel lblSalesretNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTranspotationCharges;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CLabel lblUnitType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panCutOff;
    private com.see.truetransact.uicomponent.CPanel panDateDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpNo;
    private com.see.truetransact.uicomponent.CPanel panEmpNo1;
    private com.see.truetransact.uicomponent.CPanel panEmpNo2;
    private com.see.truetransact.uicomponent.CPanel panInstallmentDetails;
    private com.see.truetransact.uicomponent.CPanel panName;
    private com.see.truetransact.uicomponent.CPanel panName1;
    private com.see.truetransact.uicomponent.CPanel panProductName;
    private com.see.truetransact.uicomponent.CPanel panSalTotal;
    private com.see.truetransact.uicomponent.CPanel panSales;
    private com.see.truetransact.uicomponent.CPanel panSalesDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesProdDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReprint;
    private com.see.truetransact.uicomponent.CPanel panSalesReprintButtons;
    private com.see.truetransact.uicomponent.CPanel panSalesReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReprintSubDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReprintTableDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReturn;
    private com.see.truetransact.uicomponent.CPanel panSalesReturnSubDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReturnTableDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesReturnTotalDetails;
    private com.see.truetransact.uicomponent.CPanel panSalesTableDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalDetails;
    private com.see.truetransact.uicomponent.CPanel panTotalRadioDetails;
    private com.see.truetransact.uicomponent.CPanel panTradingSales;
    private com.see.truetransact.uicomponent.CPanel panType;
    private com.see.truetransact.uicomponent.CRadioButton rdoCeil;
    private com.see.truetransact.uicomponent.CRadioButton rdoFloor;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonMembers;
    private com.see.truetransact.uicomponent.CRadioButton rdoRet;
    private com.see.truetransact.uicomponent.CRadioButton rdoSal;
    private com.see.truetransact.uicomponent.CButtonGroup rdoType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTypeMembers;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW1;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW2;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW3;
    private com.see.truetransact.uicomponent.CTabbedPane tabTradingSales;
    private com.see.truetransact.uicomponent.CTable tblSalesDetails;
    private com.see.truetransact.uicomponent.CTable tblSalesRePrint;
    private com.see.truetransact.uicomponent.CTable tblSalesReturn;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalesDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalesDt;
    private com.see.truetransact.uicomponent.CDateField tdtSalesReprintSalesDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalesReturnDate;
    private com.see.truetransact.uicomponent.CDateField tdtSalesReturnSalesDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAcNo;
    private com.see.truetransact.uicomponent.CTextField txtAdjust;
    private com.see.truetransact.uicomponent.CTextField txtAvailQty;
    private com.see.truetransact.uicomponent.CTextField txtBankAcHead;
    private com.see.truetransact.uicomponent.CTextField txtBarCodeNumber;
    private com.see.truetransact.uicomponent.CTextField txtCash;
    private com.see.truetransact.uicomponent.CTextField txtCreditAmt;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtDiscPercentage;
    private com.see.truetransact.uicomponent.CTextField txtDiscount;
    private com.see.truetransact.uicomponent.CTextField txtFirstInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtGrandTotal;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentNo;
    private com.see.truetransact.uicomponent.CTextField txtMRP;
    private com.see.truetransact.uicomponent.CTextField txtPlace;
    private com.see.truetransact.uicomponent.CTextField txtProdDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductName;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtReturn;
    private com.see.truetransact.uicomponent.CTextField txtSalTotal;
    private com.see.truetransact.uicomponent.CTextField txtSales;
    private com.see.truetransact.uicomponent.CTextField txtSalesPrice;
    private com.see.truetransact.uicomponent.CTextField txtSalesReprintName;
    private com.see.truetransact.uicomponent.CTextField txtSalesReprintNo;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturnCustID;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturnSalNo;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturnSalesType;
    private com.see.truetransact.uicomponent.CTextField txtSalesretNo;
    private com.see.truetransact.uicomponent.CTextField txtTotal;
    private com.see.truetransact.uicomponent.CTextField txtTranspotationCharges;
    private com.see.truetransact.uicomponent.CTextField txtUnitType;
    // End of variables declaration//GEN-END:variables
}
