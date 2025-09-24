/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingPurchaseUI.java
 *
 * Created on January 31, 2005, 2:59 PM
 */

package com.see.truetransact.ui.trading.tradingpurchase;

/**
 *
 * @author  Revathi L
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

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.TextUI;
import java.util.List;
import javax.swing.JOptionPane;
import com.see.truetransact.ui.trading.tradingproduct.TradingProductUI;
import com.see.truetransact.ui.trading.tradingpurchase.TradingProductDetailsUI;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TradingPurchaseUI extends CInternalFrame implements UIMandatoryField,Observer{
    
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.tradingpurchase.TradingPurchaseRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingPurchaseMRB objMandatoryRB = new TradingPurchaseMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingPurchaseOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    Date currDt = null;
    int updateTab = -1;
    private boolean updateMode = false;
    private double totalAdjust = 0.00;
    boolean isFilled = false;
    private boolean isProduct = false;
    private String prod_id = "";
    TradingProductUI tradingProductUI;
    TradingProductDetailsUI tradingProdDetailsUI;
    private String newProduct = "";
    HashMap prodMap = new HashMap();
    public int column = -1;
    private List finalList = null;
    int pan = -1;
    int panEditDelete = -1;
    private int EDITPURCHASE = 1,EDITRETURN = 2;
    private int DELETEPURCHASE = 1,DELETERETURN = 2;
    private int PURCHASE = 1, PURCHASERETURN = 2;
    ArrayList colourList = new ArrayList();
    
    /** Creates new form TDSConfigUI */
    public TradingPurchaseUI() {
        initForm();
    }
    
    /** Method called from consturctor to initialize the form **/
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        observable.resetPurchaseRetForm();
        observable.resetPurchaseForm();
        currDt = ClientUtil.getCurrentDate();
        tdtVoucherDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        tdtBillDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        ClientUtil.enableDisable(panPurchase, false);
        txtPurchAmt.setText("0.00");
        txtSalesAmt.setText("0.00");
        txtQtyUnit.setText("0");
        txtQtyUnit.setEnabled(false);
        txtPurchasePrice.setEnabled(false);
        txtPlace.setEnabled(false);
        cboPurchaseType.setEnabled(false);
        txtBankAcHead.setEnabled(false);
        btnPurchaseNew.setEnabled(false);
        btnPurchaseSave.setEnabled(false);
        btnPurchaseDel.setEnabled(false);
        setButtonEnableDisable();
        btnAddNewItems.setEnabled(false);
//        cboSupplierName.setEnabled(false);
//        txtPurchNo.setEnabled(false);
//        txtPurchBankHd.setEnabled(false);
        ClientUtil.enableDisable(panPurchaseReturn, false);
        
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
        panStatus.setName("panStatus");
    }
    
   /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
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
    }
    
    /** This method sets the Maximum allowed lenght to the textfields **/
    private void setMaxLengths(){
        txtBillNo.setAllowAll(true);
        txtQty.setAllowNumber(true);
        txtPurchaseTotal.setAllowAll(true);
        txtTax.setAllowNumber(true);
        txtDiscount.setAllowNumber(true);
        txtTransPortationCharge.setAllowNumber(true);
        txtShrinkageQty.setAllowNumber(true);
        txtPurchasePrice.setAllowAll(true);
        txtSalesPrice.setAllowAll(true);
        txtMRP.setAllowAll(true);
        txtParticulars.setAllowAll(true);
        txtPlace.setAllowAll(true);
        txtIndentNo.setAllowAll(true);
        txtMfgBatchID.setAllowAll(true);
        txtPurchasePrice.setValidation(new CurrencyValidation(14, 2));
        txtPurchase.setValidation(new CurrencyValidation(14, 2));
        txtTotal.setValidation(new CurrencyValidation(14, 2));
        txtPurchAmt.setValidation(new CurrencyValidation(14, 2));
        txtSalesAmt.setValidation(new CurrencyValidation(14, 2));
        txtMRP.setValidation(new CurrencyValidation(14, 2));
        txtSalesPrice.setValidation(new CurrencyValidation(14, 2));
        txtPurchaseTotal.setValidation(new CurrencyValidation(14, 2));
        txtPRPurchase.setValidation(new CurrencyValidation(14, 2));
        txtReturn.setValidation(new CurrencyValidation(14, 2));
        txtTaxTot.setValidation(new NumericValidation());
    }
    
    /**  This method is to add this class as an Observer to an Observable **/
    private void setObservable(){
        try{
            observable =TradingPurchaseOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try {
            //cboBranchCode.setModel(observable.getCbmBranchCode());
            cboSupplier.setModel(observable.getCbmSupplierName());
            cboUnitType.setModel(observable.getCbmUnitType());
            cboPurchaseType.setModel(observable.getCbmPurchaseType());
            tblPurchaseDetails.setModel(observable.getTblPurchaseDetails());
            tblPurchaseReturn.setModel(observable.getTblPurchaseReturn());
            cboSupplierName.setModel(observable.getCbmSupplierName());
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
    
    private void setMainEnableDisable(boolean flag) {
        cboSupplier.setEnabled(flag);
        tdtVoucherDt.setEnabled(flag);
        cboVoucherNo.setEnabled(flag);
        cboPurchaseType.setEnabled(flag);
        txtBankAcHead.setEnabled(flag);
        txtBranchCode.setEnabled(flag);
        tdtBillDt.setEnabled(flag);
        txtBillNo.setEnabled(flag);
        txtIndentNo.setEnabled(flag);
    }
    
    private void setPurchRetEnableDisable(boolean flag) {
        cboSupplierName.setEnabled(flag);
        txtPurchNo.setEnabled(flag);
        txtPRBillNo.setEnabled(flag);
        txtPurchType.setEnabled(flag);
        txtPurchBankHd.setEnabled(flag);
        tdtPurchRetDt.setEnabled(flag);
        tdtPurchDt.setEnabled(flag);
        tdtPurchBillDt.setEnabled(flag);
        btnPurchNo.setEnabled(flag);
    }
    
    private void setEnableDisable(boolean flag) {
        txtProductName.setEnabled(flag);
        btnProductName.setEnabled(flag);
        txtQty.setEnabled(flag);
        txtPurchaseTotal.setEnabled(flag);
        txtTax.setEnabled(flag);
        txtDiscount.setEnabled(flag);
        txtTransPortationCharge.setEnabled(flag);
        txtPurchasePrice.setEnabled(flag);
        txtMRP.setEnabled(flag);
        txtSalesPrice.setEnabled(flag);
        tdtExpiryDt.setEnabled(flag);
        txtParticulars.setEnabled(flag);
        tdtBillDt.setEnabled(flag);
        txtBillNo.setEnabled(flag);
        txtMfgBatchID.setEnabled(flag);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtPurchaseNo(CommonUtil.convertObjToStr(lblPurchaseNoVal.getText()));
        observable.setTxtBillNo(CommonUtil.convertObjToStr(txtBillNo.getText()));
        observable.setTxtProductName(CommonUtil.convertObjToStr(txtProductName.getText()));
        observable.setTxtQty(CommonUtil.convertObjToStr(txtQty.getText()));
        observable.setTxtPuchaseTotal(CommonUtil.convertObjToStr(txtPurchaseTotal.getText()));
        observable.setTxtQtyUnit(CommonUtil.convertObjToStr(txtQtyUnit.getText()));
        observable.setTxtTax(CommonUtil.convertObjToStr(txtTax.getText()));
        observable.setTxtDiscount(CommonUtil.convertObjToStr(txtDiscount.getText()));
        observable.setTxtTPCharges(CommonUtil.convertObjToStr(txtTransPortationCharge.getText()));
        observable.setTxtShrinkageQty(CommonUtil.convertObjToStr(txtShrinkageQty.getText()));
        observable.setTxtPurchasePrice(CommonUtil.convertObjToStr(txtPurchasePrice.getText()));
        observable.setTxtMRP(CommonUtil.convertObjToStr(txtMRP.getText()));
        observable.setTxtSalesPrice(CommonUtil.convertObjToStr(txtSalesPrice.getText()));
        observable.setTxtParticulars(CommonUtil.convertObjToStr(txtParticulars.getText()));
        observable.setTxtPlace(CommonUtil.convertObjToStr(txtPlace.getText()));
        observable.setTxtBankAcHead(CommonUtil.convertObjToStr(txtBankAcHead.getText()));
        observable.setTxtIndentNo(CommonUtil.convertObjToStr(txtIndentNo.getText()));
        observable.setTxtPurchaseReturn(CommonUtil.convertObjToStr(txtPurchReturn.getText()));
        observable.setTxtPurchase(CommonUtil.convertObjToStr(txtPurchase.getText()));
        observable.setTxtTaxTot(CommonUtil.convertObjToStr(txtTaxTot.getText()));
        observable.setTxtDiscTot(CommonUtil.convertObjToStr(txtDiscountTot.getText()));
        observable.setTxtTPTot(CommonUtil.convertObjToStr(txtTP.getText()));
        observable.setTxtTotal(CommonUtil.convertObjToStr(txtTotal.getText()));
        observable.setTxtPurchAmt(CommonUtil.convertObjToStr(txtPurchAmt.getText()));
        observable.setTxtSalesAmt(CommonUtil.convertObjToStr(txtSalesAmt.getText()));
        observable.setTxtMfgBatchID(CommonUtil.convertObjToStr(txtMfgBatchID.getText()));
        observable.setBillDt(DateUtil.getDateMMDDYYYY(tdtBillDt.getDateValue()));
        observable.setVoucherDt(DateUtil.getDateMMDDYYYY(tdtVoucherDt.getDateValue()));
        observable.setExpiryDt(DateUtil.getDateMMDDYYYY(tdtExpiryDt.getDateValue()));
        observable.setCboSupplierName(CommonUtil.convertObjToStr(cboSupplier.getSelectedItem()));
        observable.setCboUnitType(CommonUtil.convertObjToStr(cboUnitType.getSelectedItem()));
        observable.setCboVoucherNo(CommonUtil.convertObjToStr(cboVoucherNo.getSelectedItem()));
        observable.setCboPurchaseType(CommonUtil.convertObjToStr(cboPurchaseType.getSelectedItem()));
        observable.setCboBranchCode(CommonUtil.convertObjToStr(txtBranchCode.getText()));
        //Purchase Return
        observable.setTxtPurchRetNo(CommonUtil.convertObjToStr(txtPurchRetNo.getText()));
        observable.setCboSupplierName(CommonUtil.convertObjToStr(cboSupplierName.getSelectedItem()));
        observable.setTxtPurchNo(CommonUtil.convertObjToStr(txtPurchNo.getText()));
        observable.setTxtPurchType(CommonUtil.convertObjToStr(txtPurchType.getText()));
        observable.setTxtPurchBillNo(CommonUtil.convertObjToStr(txtPRBillNo.getText()));
        observable.setTxtPurchBankAcHead(CommonUtil.convertObjToStr(txtPurchBankHd.getText()));
        observable.setTxtPurch(CommonUtil.convertObjToStr(txtPRPurchase.getText()));
        observable.setTxtReturn(CommonUtil.convertObjToStr(txtReturn.getText()));
        observable.setPurchDt(DateUtil.getDateMMDDYYYY(tdtPurchDt.getDateValue()));
        observable.setPurchBillDt(DateUtil.getDateMMDDYYYY(tdtPurchBillDt.getDateValue()));
        observable.setPurchRetDt(DateUtil.getDateMMDDYYYY(tdtPurchRetDt.getDateValue()));
        
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        if (panPurchase.isShowing() == true) {
            tadingPurchaseUpdate();
        } else if (panPurchaseReturn.isShowing() == true) {
            tadingPurchaseRetUpdate();
        }

    }

    public void tadingPurchaseUpdate() {
        lblPurchaseNoVal.setText(CommonUtil.convertObjToStr(observable.getTxtPurchaseNo()));
        cboSupplier.setSelectedItem((observable.getCboSupplierName()));
        tdtVoucherDt.setDateValue(CommonUtil.convertObjToStr(observable.getVoucherDt()));
        if (!(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
            cboVoucherNo.setModel(observable.getCbmVoucherNo());
        }
        cboVoucherNo.setSelectedItem((observable.getCboVoucherNo()));
        cboPurchaseType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboPurchaseType()));
        txtBranchCode.setText(CommonUtil.convertObjToStr(observable.getCboBranchCode()));
        txtBankAcHead.setText(CommonUtil.convertObjToStr(observable.getTxtBankAcHead()));
        txtIndentNo.setText(CommonUtil.convertObjToStr(observable.getTxtIndentNo()));
        if (!txtBankAcHead.getText().equals("")) {
            if (cboPurchaseType.getSelectedItem().equals("Cash")) {
                lblBankAcHeadDesc.setText(getAccHeadDesc(observable.getTxtBankAcHead()));
            } else {
                lblBankAcHeadDesc.setText(observable.getCboSupplierName());
            }
        }
        txtPurchAmt.setText(CommonUtil.convertObjToStr(observable.getTxtPurchAmt()));
    }

    public void tadingPurchaseRetUpdate() {
        txtPurchRetNo.setText(CommonUtil.convertObjToStr(observable.getTxtPurchRetNo()));
        cboSupplierName.setSelectedItem((observable.getCboSupplierName()));
        txtPurchNo.setText(CommonUtil.convertObjToStr(observable.getTxtPurchNo()));
        txtPRBillNo.setText(CommonUtil.convertObjToStr(observable.getTxtPurchBillNo()));
        txtPurchType.setText(CommonUtil.convertObjToStr(observable.getTxtPurchType()));
        txtPurchBankHd.setText(CommonUtil.convertObjToStr(observable.getTxtPurchBankAcHead()));
        tdtPurchBillDt.setDateValue(CommonUtil.convertObjToStr(observable.getPurchBillDt()));
        tdtPurchDt.setDateValue(CommonUtil.convertObjToStr(observable.getPurchDt()));
        tdtPurchRetDt.setDateValue(CommonUtil.convertObjToStr(observable.getPurchRetDt()));
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField,int panEditDelete) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("PROD_NAME")) {
            HashMap map = new HashMap();
            if(isProduct){
               map.put("UNAUTHORIZED", "UNAUTHORIZED");
            }else{
                map.put("UNAUTHORIZED", "");
            }
            map.put("PRODUCT_NAME", txtProductName.getText());
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingProdName");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Edit")) {
            if (panEditDelete == EDITPURCHASE) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseEdit");
            } else if (panEditDelete == EDITRETURN) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseReturnEdit");
            }
            new ViewAll(this, viewMap).show();
        } else if (viewType=="Enquiry") {
            if (panEditDelete == EDITPURCHASE) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseView");
            }else if(panEditDelete == EDITRETURN){
             HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseReturnView");   
            }
            new ViewAll(this, viewMap).show();
        } else if (viewType == "Delete") {
            if (panEditDelete == DELETEPURCHASE) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseDelete");
            } else if (panEditDelete == DELETERETURN) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseReturnDelete");
            }
            new ViewAll(this, viewMap).show();
        }else if (viewType=="PURCHASE_NO") {
            HashMap map = new HashMap();
            if(cboSupplierName.getSelectedIndex() > 0){
                map.put("SUPPLIER", "SUPPLIER");
                map.put("SUPPLIER_NAME",CommonUtil.convertObjToStr(((ComboBoxModel) cboSupplier.getModel()).getKeyForSelected()));
            }
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingPurchaseData");
            new ViewAll(this, viewMap).show();
        }
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
         setModified(true);
        isFilled = true;
        HashMap hash = (HashMap) map;
        if (viewType == "PROD_NAME") {
            txtProductName.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_NAME")));
            cboUnitType.setSelectedItem(CommonUtil.convertObjToStr(hash.get("UNITTYPE")));
            prod_id = CommonUtil.convertObjToStr(hash.get("PRODUCT_ID"));
        } else if (viewType == newProduct) {
            if (isProduct) {
                prodMap = (HashMap) map;
            }
            if (prodMap != null && prodMap.size() > 0) {
                txtProductName.setText(CommonUtil.convertObjToStr(prodMap.get("PRODUCT_NAME")));
            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            if(panPurchase.isShowing()==true){
                panEditDelete = PURCHASE;
            }else if(panPurchaseReturn.isShowing()==true){
                panEditDelete = PURCHASERETURN;
            }
            observable.getData(hash, panEditDelete);
            tblPurchaseReturn.setModel(observable.getTblPurchaseReturn());
            update();
            purchaseTableUpdate();
            HashMap purchMap = new HashMap();
            purchMap.put("PURCHASE_ENTRY_ID", (cboVoucherNo.getSelectedItem()));
            totalRetAmountCalc();
            totalAmountCalc();
            totalTaxAmountCalc();
            totalDiscAmountCalc();
            btnPurchaseNew.setEnabled(true);
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            HashMap whereMap = new HashMap();
            setButtonEnableDisable();
            if (panPurchase.isShowing() == true) {
                panEditDelete = PURCHASE;
                observable.getData(hash, panEditDelete);
                StringBuffer PRODUCT_NAME = getProductName();
                whereMap.put("UN_AUTHORIZE", "UN_AUTHORIZE");
                whereMap.put("PRODUCT_NAME", PRODUCT_NAME);
                List prodLst = ClientUtil.executeQuery("getTradingProductPendingForAuthorize", whereMap);
                if (prodLst != null && prodLst.size() > 0) {
                    ClientUtil.showMessageWindow("Please Authorize The New Products Added.");
                    // checking Product Pending Authorize List
                    String prodName = "";
                    new TradingProductDetailsUI(prodName).show();
                }
                ClientUtil.enableDisable(panPurchase, false);
            }else if(panPurchaseReturn.isShowing()==true){
                panEditDelete = PURCHASERETURN;
                observable.getData(hash, panEditDelete);
                tblPurchaseReturn.setModel(observable.getTblPurchaseReturn());
                totalRetAmountCalc();
            }
            update();
            
            totalAmountCalc();
            totalTaxAmountCalc();
            totalDiscAmountCalc();
        }else if(viewType == "PURCHASE_NO"){
            ClientUtil.enableDisable(panPurchaseReturn, false);
            List purchNoLst = ClientUtil.executeQuery("getCheckingPurchaseNo", hash);
            if (purchNoLst != null && purchNoLst.size() > 0) {
                HashMap purchNoMap = (HashMap) purchNoLst.get(0);
                if (CommonUtil.convertObjToStr(purchNoMap.get("AUTHORIZE_STATUS")).equals("")) {
                    ClientUtil.showMessageWindow("Earlier Purchase Return for this Purchase No"+" "+CommonUtil.convertObjToStr(hash.get("PURCHASE_NO"))+
                            " "+"Not yet Authorized");
                    btnCancelActionPerformed(null);
                    return;
                }
            }
            txtPurchNo.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_NO")));
            txtPRBillNo.setText(CommonUtil.convertObjToStr(hash.get("BILL_NO")));
            txtPurchType.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_TYPE")));
            txtPurchBankHd.setText(CommonUtil.convertObjToStr(hash.get("BANK_AC_HEAD")));
            tdtPurchRetDt.setDateValue(CommonUtil.convertObjToStr(currDt));
            tdtPurchDt.setDateValue(CommonUtil.convertObjToStr(hash.get("PURCHASE_DT")));
            tdtPurchBillDt.setDateValue(CommonUtil.convertObjToStr(hash.get("BILL_DT")));
            if (cboSupplierName.getSelectedIndex() == 0) {
                HashMap supNameMap = new HashMap();
                supNameMap.put("SUPPLIERID", CommonUtil.convertObjToStr(hash.get("SUPPLIER")));
                List supNameLst = ClientUtil.executeQuery("getSuppNameforPurchase", supNameMap);
                if (supNameLst != null && supNameLst.size() > 0) {
                    supNameMap = (HashMap) supNameLst.get(0);
                    cboSupplierName.setSelectedItem(CommonUtil.convertObjToStr(supNameMap.get("SUPP_NAME")));
                }
            }
            if (txtPurchNo.getText().length() > 0) {
                observable.populatePurchReturnData(txtPurchNo.getText());
                tblPurchaseReturn.setModel(observable.getTblPurchaseReturn());
                totalAmountCalc();
            }

        }
    }

    private StringBuffer getProductName() {
        StringBuffer prod_Name = new StringBuffer();
        if (tblPurchaseDetails.getRowCount() > 0) {
            for (int i = 0; i < tblPurchaseDetails.getRowCount(); i++) {
                String prodDet = CommonUtil.convertObjToStr(tblPurchaseDetails.getValueAt(i, 1));
                if (prod_Name.length() > 0) {
                    prod_Name.append(',');
                }
                prod_Name.append("'" + prodDet + "'");
            }
        }
        return prod_Name;
    }
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of TDSConfigOB to do insertion or updation or deletion */
    private void saveAction(String status){
         observable.doAction(pan);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                    if (observable.getProxyReturnMap().containsKey("PURCHASE_NO")) {
                        ClientUtil.showMessageWindow("Purchase No : " + observable.getProxyReturnMap().get("PURCHASE_NO"));
                    } else {
                        ClientUtil.showMessageWindow("Purchase Return No : " + observable.getProxyReturnMap().get("PURCHASE_Return_NO"));
                    }
                }
            }
        }
        btnCancelActionPerformed(null);
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetPurchaseForm();
        observable.resetPurchaseRetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
        
        //__ Make the Screen Closable..
        setModified(false);
    }
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getTDSConfigAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeTDSConfig");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            ClientUtil.execute("authorizeTDSConfig", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
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
        tabTradingPurchase = new com.see.truetransact.uicomponent.CTabbedPane();
        panPurchase = new com.see.truetransact.uicomponent.CPanel();
        panPurchaseTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpPurchaseTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblPurchaseDetails = new com.see.truetransact.uicomponent.CTable();
        panPurchaseDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPurchaseNo = new com.see.truetransact.uicomponent.CLabel();
        lblSupplier = new com.see.truetransact.uicomponent.CLabel();
        lblVoucherDt = new com.see.truetransact.uicomponent.CLabel();
        lblVoucherNo = new com.see.truetransact.uicomponent.CLabel();
        lblBillNo = new com.see.truetransact.uicomponent.CLabel();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        txtBillNo = new com.see.truetransact.uicomponent.CTextField();
        lblUnitType = new com.see.truetransact.uicomponent.CLabel();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseTotal = new com.see.truetransact.uicomponent.CTextField();
        btnAddNewItems = new com.see.truetransact.uicomponent.CButton();
        tdtVoucherDt = new com.see.truetransact.uicomponent.CDateField();
        tdtBillDt = new com.see.truetransact.uicomponent.CDateField();
        panProductName = new com.see.truetransact.uicomponent.CPanel();
        txtProductName = new com.see.truetransact.uicomponent.CTextField();
        btnProductName = new com.see.truetransact.uicomponent.CButton();
        lblQtyUnit = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseTotal = new com.see.truetransact.uicomponent.CLabel();
        txtSalesPrice = new com.see.truetransact.uicomponent.CTextField();
        cboSupplier = new com.see.truetransact.uicomponent.CComboBox();
        cboUnitType = new com.see.truetransact.uicomponent.CComboBox();
        lblTax = new com.see.truetransact.uicomponent.CLabel();
        lblBillDt = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDt = new com.see.truetransact.uicomponent.CLabel();
        lblSalesPrice = new com.see.truetransact.uicomponent.CLabel();
        lblMRP = new com.see.truetransact.uicomponent.CLabel();
        lblPurchasePrice = new com.see.truetransact.uicomponent.CLabel();
        lblDiscount = new com.see.truetransact.uicomponent.CLabel();
        lblTransportationCharge = new com.see.truetransact.uicomponent.CLabel();
        lblShrinkageQty = new com.see.truetransact.uicomponent.CLabel();
        tdtExpiryDt = new com.see.truetransact.uicomponent.CDateField();
        txtMRP = new com.see.truetransact.uicomponent.CTextField();
        txtPurchasePrice = new com.see.truetransact.uicomponent.CTextField();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        txtDiscount = new com.see.truetransact.uicomponent.CTextField();
        txtTransPortationCharge = new com.see.truetransact.uicomponent.CTextField();
        txtShrinkageQty = new com.see.truetransact.uicomponent.CTextField();
        txtQtyUnit = new com.see.truetransact.uicomponent.CTextField();
        panShrinkage = new com.see.truetransact.uicomponent.CPanel();
        chkShrinkage = new com.see.truetransact.uicomponent.CCheckBox();
        chkFree = new com.see.truetransact.uicomponent.CCheckBox();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblPlace = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseType = new com.see.truetransact.uicomponent.CLabel();
        lblBankAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblIndentNo = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        cboPurchaseType = new com.see.truetransact.uicomponent.CComboBox();
        cboVoucherNo = new com.see.truetransact.uicomponent.CComboBox();
        txtPlace = new com.see.truetransact.uicomponent.CTextField();
        txtIndentNo = new com.see.truetransact.uicomponent.CTextField();
        txtTax = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblBranchIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblBankAcHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        txtBankAcHead = new com.see.truetransact.uicomponent.CTextField();
        lblMfgBatchID = new com.see.truetransact.uicomponent.CLabel();
        txtMfgBatchID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseTotalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPurchase = new com.see.truetransact.uicomponent.CLabel();
        lblTaxTot = new com.see.truetransact.uicomponent.CLabel();
        lblTP = new com.see.truetransact.uicomponent.CLabel();
        txtPurchase = new com.see.truetransact.uicomponent.CTextField();
        txtTaxTot = new com.see.truetransact.uicomponent.CTextField();
        txtPurchReturn = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountTot = new com.see.truetransact.uicomponent.CLabel();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchReturn = new com.see.truetransact.uicomponent.CLabel();
        txtTP = new com.see.truetransact.uicomponent.CTextField();
        txtDiscountTot = new com.see.truetransact.uicomponent.CTextField();
        txtTotal = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseAmtDetails = new com.see.truetransact.uicomponent.CPanel();
        btnPurchaseNew = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseSave = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseDel = new com.see.truetransact.uicomponent.CButton();
        lblPurchAmt = new com.see.truetransact.uicomponent.CLabel();
        lblSalesAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPurchAmt = new com.see.truetransact.uicomponent.CTextField();
        txtSalesAmt = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseReturn = new com.see.truetransact.uicomponent.CPanel();
        panCutOff = new com.see.truetransact.uicomponent.CPanel();
        panPurchaseReturnTotalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPRPurchase = new com.see.truetransact.uicomponent.CLabel();
        lblReturn = new com.see.truetransact.uicomponent.CLabel();
        txtPRPurchase = new com.see.truetransact.uicomponent.CTextField();
        txtReturn = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseReturnDetails = new com.see.truetransact.uicomponent.CPanel();
        panPurchaseReturnSubDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPRBillNo = new com.see.truetransact.uicomponent.CLabel();
        lblSupplierName = new com.see.truetransact.uicomponent.CLabel();
        lblPurchNo = new com.see.truetransact.uicomponent.CLabel();
        txtPurchRetNo = new com.see.truetransact.uicomponent.CTextField();
        lblPurchType = new com.see.truetransact.uicomponent.CLabel();
        lblPurchRetDt = new com.see.truetransact.uicomponent.CLabel();
        lblPurchDt = new com.see.truetransact.uicomponent.CLabel();
        txtPRBillNo = new com.see.truetransact.uicomponent.CTextField();
        txtPurchBankHd = new com.see.truetransact.uicomponent.CTextField();
        lblPurchBillDt = new com.see.truetransact.uicomponent.CLabel();
        lblPurchBankHd = new com.see.truetransact.uicomponent.CLabel();
        txtPurchType = new com.see.truetransact.uicomponent.CTextField();
        tdtPurchDt = new com.see.truetransact.uicomponent.CDateField();
        tdtPurchRetDt = new com.see.truetransact.uicomponent.CDateField();
        lblPurchRetNo = new com.see.truetransact.uicomponent.CLabel();
        tdtPurchBillDt = new com.see.truetransact.uicomponent.CDateField();
        panPurchNo = new com.see.truetransact.uicomponent.CPanel();
        txtPurchNo = new com.see.truetransact.uicomponent.CTextField();
        btnPurchNo = new com.see.truetransact.uicomponent.CButton();
        cboSupplierName = new com.see.truetransact.uicomponent.CComboBox();
        panPurchaseReturnTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpPurchaseReturn = new com.see.truetransact.uicomponent.CScrollPane();
        tblPurchaseReturn = new com.see.truetransact.uicomponent.CTable();
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

        tabTradingPurchase.setMinimumSize(new java.awt.Dimension(955, 625));
        tabTradingPurchase.setPreferredSize(new java.awt.Dimension(955, 625));

        panPurchase.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPurchase.setMaximumSize(new java.awt.Dimension(900, 620));
        panPurchase.setMinimumSize(new java.awt.Dimension(900, 620));
        panPurchase.setPreferredSize(new java.awt.Dimension(900, 620));
        panPurchase.setLayout(new java.awt.GridBagLayout());

        panPurchaseTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseTableDetails.setMaximumSize(new java.awt.Dimension(900, 145));
        panPurchaseTableDetails.setMinimumSize(new java.awt.Dimension(900, 145));
        panPurchaseTableDetails.setPreferredSize(new java.awt.Dimension(900, 145));
        panPurchaseTableDetails.setLayout(new java.awt.GridBagLayout());

        srpPurchaseTable.setMaximumSize(new java.awt.Dimension(880, 135));
        srpPurchaseTable.setMinimumSize(new java.awt.Dimension(880, 135));
        srpPurchaseTable.setPreferredSize(new java.awt.Dimension(880, 135));
        srpPurchaseTable.setRequestFocusEnabled(false);

        tblPurchaseDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblPurchaseDetails.setMinimumSize(new java.awt.Dimension(400, 700));
        tblPurchaseDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblPurchaseDetails.setPreferredSize(new java.awt.Dimension(400, 700));
        tblPurchaseDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchaseDetailsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPurchaseDetailsMousePressed(evt);
            }
        });
        srpPurchaseTable.setViewportView(tblPurchaseDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseTableDetails.add(srpPurchaseTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panPurchase.add(panPurchaseTableDetails, gridBagConstraints);

        panPurchaseDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseDetails.setMaximumSize(new java.awt.Dimension(900, 300));
        panPurchaseDetails.setMinimumSize(new java.awt.Dimension(900, 300));
        panPurchaseDetails.setPreferredSize(new java.awt.Dimension(900, 300));
        panPurchaseDetails.setLayout(new java.awt.GridBagLayout());

        lblPurchaseNo.setText("Purchase No");
        lblPurchaseNo.setMaximumSize(new java.awt.Dimension(76, 18));
        lblPurchaseNo.setMinimumSize(new java.awt.Dimension(76, 18));
        lblPurchaseNo.setPreferredSize(new java.awt.Dimension(76, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPurchaseNo, gridBagConstraints);

        lblSupplier.setText("Supplier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblSupplier, gridBagConstraints);

        lblVoucherDt.setText("Voucher Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblVoucherDt, gridBagConstraints);

        lblVoucherNo.setText("Voucher No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblVoucherNo, gridBagConstraints);

        lblBillNo.setText("Bill No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblBillNo, gridBagConstraints);

        lblProductName.setText("Product Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblProductName, gridBagConstraints);

        txtQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtQty, gridBagConstraints);

        txtBillNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBillNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtBillNo, gridBagConstraints);

        lblUnitType.setText("Unit Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblUnitType, gridBagConstraints);

        lblQty.setText("Qty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblQty, gridBagConstraints);

        txtPurchaseTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchaseTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseTotalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtPurchaseTotal, gridBagConstraints);

        btnAddNewItems.setText("ADD New Items");
        btnAddNewItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewItemsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(btnAddNewItems, gridBagConstraints);

        tdtVoucherDt.setEnabled(false);
        tdtVoucherDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtVoucherDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(tdtVoucherDt, gridBagConstraints);

        tdtBillDt.setEnabled(false);
        tdtBillDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtBillDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(tdtBillDt, gridBagConstraints);

        panProductName.setMinimumSize(new java.awt.Dimension(200, 21));
        panProductName.setPreferredSize(new java.awt.Dimension(200, 21));
        panProductName.setLayout(new java.awt.GridBagLayout());

        txtProductName.setMinimumSize(new java.awt.Dimension(180, 21));
        txtProductName.setPreferredSize(new java.awt.Dimension(180, 21));
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
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(panProductName, gridBagConstraints);

        lblQtyUnit.setText("Qty/Unit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblQtyUnit, gridBagConstraints);

        lblPurchaseTotal.setText("Purchase Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPurchaseTotal, gridBagConstraints);

        txtSalesPrice.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesPriceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtSalesPrice, gridBagConstraints);

        cboSupplier.setMinimumSize(new java.awt.Dimension(200, 21));
        cboSupplier.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(cboSupplier, gridBagConstraints);

        cboUnitType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(cboUnitType, gridBagConstraints);

        lblTax.setText("Tax");
        lblTax.setMaximumSize(new java.awt.Dimension(22, 18));
        lblTax.setMinimumSize(new java.awt.Dimension(22, 18));
        lblTax.setPreferredSize(new java.awt.Dimension(22, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblTax, gridBagConstraints);

        lblBillDt.setText("Bill Date");
        lblBillDt.setMaximumSize(new java.awt.Dimension(52, 18));
        lblBillDt.setMinimumSize(new java.awt.Dimension(52, 18));
        lblBillDt.setPreferredSize(new java.awt.Dimension(52, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblBillDt, gridBagConstraints);

        lblExpiryDt.setText("Expiry Date");
        lblExpiryDt.setMaximumSize(new java.awt.Dimension(72, 18));
        lblExpiryDt.setMinimumSize(new java.awt.Dimension(72, 18));
        lblExpiryDt.setPreferredSize(new java.awt.Dimension(72, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblExpiryDt, gridBagConstraints);

        lblSalesPrice.setText("Sales Price");
        lblSalesPrice.setMaximumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setMinimumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblSalesPrice, gridBagConstraints);

        lblMRP.setText("MRP");
        lblMRP.setMaximumSize(new java.awt.Dimension(32, 18));
        lblMRP.setMinimumSize(new java.awt.Dimension(32, 18));
        lblMRP.setPreferredSize(new java.awt.Dimension(32, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblMRP, gridBagConstraints);

        lblPurchasePrice.setText("Purchase Price");
        lblPurchasePrice.setMaximumSize(new java.awt.Dimension(90, 18));
        lblPurchasePrice.setMinimumSize(new java.awt.Dimension(90, 18));
        lblPurchasePrice.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPurchasePrice, gridBagConstraints);

        lblDiscount.setText("Discount");
        lblDiscount.setMaximumSize(new java.awt.Dimension(52, 18));
        lblDiscount.setMinimumSize(new java.awt.Dimension(52, 18));
        lblDiscount.setPreferredSize(new java.awt.Dimension(52, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblDiscount, gridBagConstraints);

        lblTransportationCharge.setText("Transportation Charge");
        lblTransportationCharge.setMaximumSize(new java.awt.Dimension(130, 18));
        lblTransportationCharge.setMinimumSize(new java.awt.Dimension(130, 18));
        lblTransportationCharge.setPreferredSize(new java.awt.Dimension(130, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblTransportationCharge, gridBagConstraints);

        lblShrinkageQty.setText("Shrinkage Qty");
        lblShrinkageQty.setMaximumSize(new java.awt.Dimension(85, 18));
        lblShrinkageQty.setMinimumSize(new java.awt.Dimension(85, 18));
        lblShrinkageQty.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblShrinkageQty, gridBagConstraints);

        tdtExpiryDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(tdtExpiryDt, gridBagConstraints);

        txtMRP.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMRP.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMRP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMRPFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtMRP, gridBagConstraints);

        txtPurchasePrice.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchasePrice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtPurchasePrice, gridBagConstraints);

        txtParticulars.setMaximumSize(new java.awt.Dimension(100, 21));
        txtParticulars.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtParticulars, gridBagConstraints);

        txtDiscount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDiscount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtDiscount, gridBagConstraints);

        txtTransPortationCharge.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTransPortationCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtTransPortationCharge, gridBagConstraints);

        txtShrinkageQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtShrinkageQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtShrinkageQty, gridBagConstraints);

        txtQtyUnit.setMaximumSize(new java.awt.Dimension(100, 21));
        txtQtyUnit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtQtyUnit, gridBagConstraints);

        panShrinkage.setMinimumSize(new java.awt.Dimension(150, 25));
        panShrinkage.setPreferredSize(new java.awt.Dimension(200, 25));

        chkShrinkage.setText("Shrinkage");
        chkShrinkage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkShrinkage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        panShrinkage.add(chkShrinkage);

        chkFree.setText("Free");
        chkFree.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkFree.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        panShrinkage.add(chkFree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(panShrinkage, gridBagConstraints);

        lblParticulars.setText("Particulars");
        lblParticulars.setMaximumSize(new java.awt.Dimension(70, 18));
        lblParticulars.setMinimumSize(new java.awt.Dimension(70, 18));
        lblParticulars.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblParticulars, gridBagConstraints);

        lblPlace.setText("Place");
        lblPlace.setMaximumSize(new java.awt.Dimension(40, 18));
        lblPlace.setMinimumSize(new java.awt.Dimension(40, 18));
        lblPlace.setPreferredSize(new java.awt.Dimension(40, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPlace, gridBagConstraints);

        lblPurchaseType.setText("Purchase Type");
        lblPurchaseType.setMaximumSize(new java.awt.Dimension(90, 18));
        lblPurchaseType.setMinimumSize(new java.awt.Dimension(90, 18));
        lblPurchaseType.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPurchaseType, gridBagConstraints);

        lblBankAcHead.setText("Bank Ac Head");
        lblBankAcHead.setMaximumSize(new java.awt.Dimension(90, 18));
        lblBankAcHead.setMinimumSize(new java.awt.Dimension(90, 18));
        lblBankAcHead.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblBankAcHead, gridBagConstraints);

        lblIndentNo.setText("Indent No");
        lblIndentNo.setMaximumSize(new java.awt.Dimension(60, 18));
        lblIndentNo.setMinimumSize(new java.awt.Dimension(60, 18));
        lblIndentNo.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblIndentNo, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        lblBranchCode.setMaximumSize(new java.awt.Dimension(80, 18));
        lblBranchCode.setMinimumSize(new java.awt.Dimension(80, 18));
        lblBranchCode.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblBranchCode, gridBagConstraints);

        cboPurchaseType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurchaseType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPurchaseTypeActionPerformed(evt);
            }
        });
        cboPurchaseType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPurchaseTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(cboPurchaseType, gridBagConstraints);

        cboVoucherNo.setMinimumSize(new java.awt.Dimension(100, 21));
        cboVoucherNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVoucherNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(cboVoucherNo, gridBagConstraints);

        txtPlace.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPlace.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtPlace, gridBagConstraints);

        txtIndentNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIndentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtIndentNo, gridBagConstraints);

        txtTax.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTax.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtTax, gridBagConstraints);

        lblPurchaseNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblPurchaseNoVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPurchaseNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPurchaseNoVal.setMaximumSize(new java.awt.Dimension(10, 18));
        lblPurchaseNoVal.setMinimumSize(new java.awt.Dimension(10, 18));
        lblPurchaseNoVal.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblPurchaseNoVal, gridBagConstraints);

        lblBranchIdDesc.setForeground(new java.awt.Color(0, 51, 255));
        lblBranchIdDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBranchIdDesc.setMaximumSize(new java.awt.Dimension(300, 18));
        lblBranchIdDesc.setMinimumSize(new java.awt.Dimension(300, 18));
        lblBranchIdDesc.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panPurchaseDetails.add(lblBranchIdDesc, gridBagConstraints);

        lblBankAcHeadDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblBankAcHeadDesc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBankAcHeadDesc.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBankAcHeadDesc.setMaximumSize(new java.awt.Dimension(10, 18));
        lblBankAcHeadDesc.setMinimumSize(new java.awt.Dimension(10, 18));
        lblBankAcHeadDesc.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 1, 2);
        panPurchaseDetails.add(lblBankAcHeadDesc, gridBagConstraints);

        txtBankAcHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtBankAcHead, gridBagConstraints);

        lblMfgBatchID.setText("Mfg BatchID");
        lblMfgBatchID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblMfgBatchID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblMfgBatchID.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(lblMfgBatchID, gridBagConstraints);

        txtMfgBatchID.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMfgBatchID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtMfgBatchID, gridBagConstraints);

        txtBranchCode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseDetails.add(txtBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 1);
        panPurchase.add(panPurchaseDetails, gridBagConstraints);

        panPurchaseTotalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseTotalDetails.setMaximumSize(new java.awt.Dimension(900, 50));
        panPurchaseTotalDetails.setMinimumSize(new java.awt.Dimension(900, 50));
        panPurchaseTotalDetails.setPreferredSize(new java.awt.Dimension(900, 50));
        panPurchaseTotalDetails.setLayout(new java.awt.GridBagLayout());

        lblPurchase.setText("Purchase");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblPurchase, gridBagConstraints);

        lblTaxTot.setText("Tax");
        lblTaxTot.setMaximumSize(new java.awt.Dimension(25, 18));
        lblTaxTot.setMinimumSize(new java.awt.Dimension(25, 18));
        lblTaxTot.setPreferredSize(new java.awt.Dimension(25, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblTaxTot, gridBagConstraints);

        lblTP.setText("T.P");
        lblTP.setMaximumSize(new java.awt.Dimension(25, 18));
        lblTP.setMinimumSize(new java.awt.Dimension(25, 18));
        lblTP.setPreferredSize(new java.awt.Dimension(25, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblTP, gridBagConstraints);

        txtPurchase.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchase.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtPurchase, gridBagConstraints);

        txtTaxTot.setMaximumSize(new java.awt.Dimension(50, 21));
        txtTaxTot.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTaxTot.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtTaxTot, gridBagConstraints);

        txtPurchReturn.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchReturn.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtPurchReturn, gridBagConstraints);

        lblDiscountTot.setText("Discount");
        lblDiscountTot.setMaximumSize(new java.awt.Dimension(55, 18));
        lblDiscountTot.setMinimumSize(new java.awt.Dimension(55, 18));
        lblDiscountTot.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblDiscountTot, gridBagConstraints);

        lblTotal.setText("Total");
        lblTotal.setMaximumSize(new java.awt.Dimension(30, 18));
        lblTotal.setMinimumSize(new java.awt.Dimension(30, 18));
        lblTotal.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblTotal, gridBagConstraints);

        lblPurchReturn.setText("Purchase Return");
        lblPurchReturn.setMaximumSize(new java.awt.Dimension(100, 18));
        lblPurchReturn.setMinimumSize(new java.awt.Dimension(100, 18));
        lblPurchReturn.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(lblPurchReturn, gridBagConstraints);

        txtTP.setMaximumSize(new java.awt.Dimension(80, 21));
        txtTP.setMinimumSize(new java.awt.Dimension(80, 21));
        txtTP.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtTP, gridBagConstraints);

        txtDiscountTot.setMaximumSize(new java.awt.Dimension(50, 21));
        txtDiscountTot.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDiscountTot.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtDiscountTot, gridBagConstraints);

        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseTotalDetails.add(txtTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPurchase.add(panPurchaseTotalDetails, gridBagConstraints);

        panPurchaseAmtDetails.setMaximumSize(new java.awt.Dimension(900, 30));
        panPurchaseAmtDetails.setMinimumSize(new java.awt.Dimension(900, 30));
        panPurchaseAmtDetails.setPreferredSize(new java.awt.Dimension(900, 30));
        panPurchaseAmtDetails.setLayout(new java.awt.GridBagLayout());

        btnPurchaseNew.setText("New");
        btnPurchaseNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 70, 2, 2);
        panPurchaseAmtDetails.add(btnPurchaseNew, gridBagConstraints);

        btnPurchaseSave.setText("Save");
        btnPurchaseSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseAmtDetails.add(btnPurchaseSave, gridBagConstraints);

        btnPurchaseDel.setText("Delete");
        btnPurchaseDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseAmtDetails.add(btnPurchaseDel, gridBagConstraints);

        lblPurchAmt.setText("Purch.Amt");
        lblPurchAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblPurchAmt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblPurchAmt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 29, 2, 2);
        panPurchaseAmtDetails.add(lblPurchAmt, gridBagConstraints);

        lblSalesAmt.setText("Sales Amt");
        lblSalesAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblSalesAmt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSalesAmt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseAmtDetails.add(lblSalesAmt, gridBagConstraints);

        txtPurchAmt.setForeground(new java.awt.Color(255, 0, 0));
        txtPurchAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        txtPurchAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseAmtDetails.add(txtPurchAmt, gridBagConstraints);

        txtSalesAmt.setForeground(new java.awt.Color(255, 0, 0));
        txtSalesAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        txtSalesAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPurchaseAmtDetails.add(txtSalesAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panPurchase.add(panPurchaseAmtDetails, gridBagConstraints);

        tabTradingPurchase.addTab("Purchase", panPurchase);

        panPurchaseReturn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPurchaseReturn.setMaximumSize(new java.awt.Dimension(947, 456));
        panPurchaseReturn.setMinimumSize(new java.awt.Dimension(947, 456));
        panPurchaseReturn.setPreferredSize(new java.awt.Dimension(1359, 786));
        panPurchaseReturn.setLayout(new java.awt.GridBagLayout());

        panCutOff.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPurchaseReturn.add(panCutOff, gridBagConstraints);

        panPurchaseReturnTotalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseReturnTotalDetails.setMaximumSize(new java.awt.Dimension(880, 50));
        panPurchaseReturnTotalDetails.setMinimumSize(new java.awt.Dimension(880, 50));
        panPurchaseReturnTotalDetails.setPreferredSize(new java.awt.Dimension(880, 50));
        panPurchaseReturnTotalDetails.setLayout(new java.awt.GridBagLayout());

        lblPRPurchase.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPRPurchase.setText("Purchase Amt");
        lblPRPurchase.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPRPurchase.setMaximumSize(new java.awt.Dimension(90, 18));
        lblPRPurchase.setMinimumSize(new java.awt.Dimension(90, 18));
        lblPRPurchase.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 14, 4, 4);
        panPurchaseReturnTotalDetails.add(lblPRPurchase, gridBagConstraints);

        lblReturn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReturn.setText("Return Amt");
        lblReturn.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblReturn.setMaximumSize(new java.awt.Dimension(80, 18));
        lblReturn.setMinimumSize(new java.awt.Dimension(80, 18));
        lblReturn.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 4, 4);
        panPurchaseReturnTotalDetails.add(lblReturn, gridBagConstraints);

        txtPRPurchase.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPRPurchase.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnTotalDetails.add(txtPRPurchase, gridBagConstraints);

        txtReturn.setMaximumSize(new java.awt.Dimension(100, 21));
        txtReturn.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnTotalDetails.add(txtReturn, gridBagConstraints);

        lblAllignment.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment.setMaximumSize(new java.awt.Dimension(300, 18));
        lblAllignment.setMinimumSize(new java.awt.Dimension(300, 18));
        lblAllignment.setPreferredSize(new java.awt.Dimension(420, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panPurchaseReturnTotalDetails.add(lblAllignment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        panPurchaseReturn.add(panPurchaseReturnTotalDetails, gridBagConstraints);

        panPurchaseReturnDetails.setMaximumSize(new java.awt.Dimension(900, 400));
        panPurchaseReturnDetails.setMinimumSize(new java.awt.Dimension(900, 400));
        panPurchaseReturnDetails.setPreferredSize(new java.awt.Dimension(900, 400));
        panPurchaseReturnDetails.setLayout(new java.awt.GridBagLayout());

        panPurchaseReturnSubDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseReturnSubDetails.setMinimumSize(new java.awt.Dimension(880, 200));
        panPurchaseReturnSubDetails.setPreferredSize(new java.awt.Dimension(880, 200));
        panPurchaseReturnSubDetails.setLayout(new java.awt.GridBagLayout());

        lblPRBillNo.setText("Bill No");
        lblPRBillNo.setMaximumSize(new java.awt.Dimension(40, 18));
        lblPRBillNo.setMinimumSize(new java.awt.Dimension(40, 18));
        lblPRBillNo.setPreferredSize(new java.awt.Dimension(40, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPRBillNo, gridBagConstraints);

        lblSupplierName.setText("Supplier Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblSupplierName, gridBagConstraints);

        lblPurchNo.setText("Purchase No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchNo, gridBagConstraints);

        txtPurchRetNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchRetNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(txtPurchRetNo, gridBagConstraints);

        lblPurchType.setText("Purchase Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchType, gridBagConstraints);

        lblPurchRetDt.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchRetDt, gridBagConstraints);

        lblPurchDt.setText("Purchase Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchDt, gridBagConstraints);

        txtPRBillNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPRBillNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(txtPRBillNo, gridBagConstraints);

        txtPurchBankHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchBankHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(txtPurchBankHd, gridBagConstraints);

        lblPurchBillDt.setText("Bill Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchBillDt, gridBagConstraints);

        lblPurchBankHd.setText("Bank Ac Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchBankHd, gridBagConstraints);

        txtPurchType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(txtPurchType, gridBagConstraints);

        tdtPurchDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(tdtPurchDt, gridBagConstraints);

        tdtPurchRetDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(tdtPurchRetDt, gridBagConstraints);

        lblPurchRetNo.setText("Purchase Ret No");
        lblPurchRetNo.setMaximumSize(new java.awt.Dimension(102, 18));
        lblPurchRetNo.setMinimumSize(new java.awt.Dimension(102, 18));
        lblPurchRetNo.setPreferredSize(new java.awt.Dimension(102, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(lblPurchRetNo, gridBagConstraints);

        tdtPurchBillDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(tdtPurchBillDt, gridBagConstraints);

        panPurchNo.setMinimumSize(new java.awt.Dimension(200, 21));
        panPurchNo.setPreferredSize(new java.awt.Dimension(200, 21));
        panPurchNo.setLayout(new java.awt.GridBagLayout());

        txtPurchNo.setMinimumSize(new java.awt.Dimension(180, 21));
        txtPurchNo.setPreferredSize(new java.awt.Dimension(180, 21));
        txtPurchNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchNo.add(txtPurchNo, gridBagConstraints);

        btnPurchNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchNo.setEnabled(false);
        btnPurchNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchNo.add(btnPurchNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(panPurchNo, gridBagConstraints);

        cboSupplierName.setMinimumSize(new java.awt.Dimension(200, 21));
        cboSupplierName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseReturnSubDetails.add(cboSupplierName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 19, 1);
        panPurchaseReturnDetails.add(panPurchaseReturnSubDetails, gridBagConstraints);

        panPurchaseReturnTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPurchaseReturnTableDetails.setMaximumSize(new java.awt.Dimension(880, 145));
        panPurchaseReturnTableDetails.setMinimumSize(new java.awt.Dimension(880, 145));
        panPurchaseReturnTableDetails.setPreferredSize(new java.awt.Dimension(880, 145));
        panPurchaseReturnTableDetails.setLayout(new java.awt.GridBagLayout());

        srpPurchaseReturn.setMaximumSize(new java.awt.Dimension(870, 135));
        srpPurchaseReturn.setMinimumSize(new java.awt.Dimension(870, 135));
        srpPurchaseReturn.setPreferredSize(new java.awt.Dimension(870, 135));
        srpPurchaseReturn.setRequestFocusEnabled(false);

        tblPurchaseReturn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Prod Name", "Type", "Tax", "Unit P Price", "Unit S Price", "Purch Qty", "Avail Qty", "Ret Qty", "P Total", "R Total"
            }
        ));
        tblPurchaseReturn.setMinimumSize(new java.awt.Dimension(400, 700));
        tblPurchaseReturn.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblPurchaseReturn.setPreferredSize(new java.awt.Dimension(400, 700));
        tblPurchaseReturn.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblPurchaseReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchaseReturnMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPurchaseReturnMousePressed(evt);
            }
        });
        tblPurchaseReturn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPurchaseReturnKeyReleased(evt);
            }
        });
        srpPurchaseReturn.setViewportView(tblPurchaseReturn);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseReturnTableDetails.add(srpPurchaseReturn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panPurchaseReturnDetails.add(panPurchaseReturnTableDetails, gridBagConstraints);

        panPurchaseReturn.add(panPurchaseReturnDetails, new java.awt.GridBagConstraints());

        tabTradingPurchase.addTab("PurchaseReturn", panPurchaseReturn);

        getContentPane().add(tabTradingPurchase, java.awt.BorderLayout.CENTER);

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
         if (panPurchase.isShowing() == true) {
            panEditDelete = EDITPURCHASE;
            pan = PURCHASE;
        } else if (panPurchaseReturn.isShowing() == true) {
            panEditDelete = EDITRETURN;
            pan = PURCHASERETURN;
        }
        callView("Enquiry",panEditDelete);
        ClientUtil.enableDisable(panPurchase, false);
        enableDisableBtn(false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
        btnProductName.setEnabled(false);
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
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetPurchaseForm();
        observable.resetPurchaseRetForm();
        ClientUtil.clearAll(this);
        lblBranchIdDesc.setText("");
        lblPurchaseNoVal.setText("");
        lblBankAcHeadDesc.setText("");
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        btnPurchNo.setEnabled(false);
        enableDisableBtn(false);
        btnProductName.setEnabled(false);
        ClientUtil.enableDisable(panPurchase, false);
        ClientUtil.enableDisable(panPurchaseReturn, false);
        viewType = null;
        isProduct = false;
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblStatus.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (panPurchase.isShowing() == true) {
            if(CommonUtil.convertObjToStr(cboSupplier.getSelectedItem()).length()<=0){
                ClientUtil.showMessageWindow("Supplier ID Should not be empty");
                return;
            }else if(CommonUtil.convertObjToStr(cboVoucherNo.getSelectedItem()).length()<=0){
                ClientUtil.showMessageWindow("Voucher No Should not be empty");
                return;
            }
            if (CommonUtil.convertObjToInt(txtPurchAmt.getText()) == CommonUtil.convertObjToInt(txtSalesAmt.getText())) {
                savePerformed();
                btnReject.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnException.setEnabled(true);
            } else {
                ClientUtil.showMessageWindow("Sales Amount Should be equal to Purchase Amount");
                return;
            }
        } else if (panPurchaseReturn.isShowing() == true) {
            finalList = observable.getFinalList();
            HashMap purchRetMap = new HashMap();
            if (finalList != null && finalList.size() > 0) {
                for (int i = 0; i < finalList.size(); i++) {
                    String slNo = "";
                    purchRetMap = (HashMap) finalList.get(i);
                    slNo = CommonUtil.convertObjToStr(purchRetMap.get("SL_NO"));
                    for (int j = 0; j < tblPurchaseReturn.getRowCount(); j++) {
                        if (CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(j, 0)).equals(slNo)) {
                            purchRetMap.put("RETURN_QTY", CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(j, 8)));
                            purchRetMap.put("RETURN_TOTAL", CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(j, 10)));
                            purchRetMap.put("AVAIL_QTY", CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(j, 7)));
                            
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
        if (panPurchase.isShowing() == true) {
            panEditDelete = DELETEPURCHASE;
            pan = PURCHASE;
        } else if (panPurchaseReturn.isShowing() == true) {
            panEditDelete = DELETERETURN;
            pan = PURCHASERETURN;
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3],panEditDelete);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (panPurchase.isShowing() == true) {
            panEditDelete = EDITPURCHASE;
            pan = PURCHASE;
        } else if (panPurchaseReturn.isShowing() == true) {
            panEditDelete = EDITRETURN;
            pan = PURCHASERETURN;
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2],panEditDelete);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        if (panPurchase.isShowing() == true) {
            pan = PURCHASE;
            updateMode = false;
            setButtonEnableDisable();
            setModified(true);
            setMainEnableDisable(true);
            btnAddNewItems.setEnabled(false);
            tdtVoucherDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
            tdtBillDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
            btnPurchaseNew.setEnabled(true);
            txtPurchAmt.setText("0.00");
            txtSalesAmt.setText("0.00");
            txtQtyUnit.setEnabled(false);
            txtBranchCode.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            txtBranchCode.setText(TrueTransactMain.BRANCH_ID);
            HashMap branchMap = TrueTransactMain.BRANCHINFO;
            lblBranchIdDesc.setText("<HTML><b><font color=Blue>"
                    + CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME"))
                    + "</font></b></html>");
            txtBranchCode.setEnabled(false);
        } else if (panPurchaseReturn.isShowing() == true) {
            pan = PURCHASERETURN;
            setPurchRetEnableDisable(true);
            colourList = new ArrayList();
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        //        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void tblPurchaseDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseDetailsMousePressed
        // TODO add your handling code here:
        if (tblPurchaseDetails.getRowCount() > 0) {
            setEnableDisable(true);
            updateMode = true;
            updateTab = tblPurchaseDetails.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblPurchaseDetails.getValueAt(tblPurchaseDetails.getSelectedRow(), 0));
            observable.populateSalesTableDetails(st);
            purchaseTableUpdate();
            txtPurchasePrice.setEnabled(false);
            btnPurchaseSave.setEnabled(true);
            btnPurchaseDel.setEnabled(true);
            btnPurchaseNew.setEnabled(false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                enableDisableBtn(false);
                setEnableDisable(false);
            } 
    }//GEN-LAST:event_tblPurchaseDetailsMousePressed
    
    public void purchaseTableUpdate() {
        txtProductName.setText(observable.getTxtProductName());
        cboUnitType.setSelectedItem(observable.getCboUnitType());
        txtBillNo.setText(CommonUtil.convertObjToStr(observable.getTxtBillNo()));
        txtMfgBatchID.setText(CommonUtil.convertObjToStr(observable.getTxtMfgBatchID()));
        tdtBillDt.setDateValue(CommonUtil.convertObjToStr(observable.getBillDt()));
        txtTax.setText(observable.getTxtTax());
        txtPurchasePrice.setText(observable.getTxtPurchasePrice());
        txtMRP.setText(observable.getTxtMRP());
        txtSalesPrice.setText(observable.getTxtSalesPrice());
        txtQty.setText(observable.getTxtQty());
        txtDiscount.setText(observable.getTxtDiscount());
        txtPurchaseTotal.setText(observable.getTxtPuchaseTotal());
        txtParticulars.setText(observable.getTxtParticulars());
        tdtExpiryDt.setDateValue(CommonUtil.convertObjToStr(observable.getExpiryDt()));
    }
    
    private void tblPurchaseReturnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseReturnMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPurchaseReturnMousePressed

    private void txtProductNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameFocusLost

    private void btnProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductNameActionPerformed
        // TODO add your handling code here:
         callView("PROD_NAME",panEditDelete);
    }//GEN-LAST:event_btnProductNameActionPerformed

    private void btnPurchNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchNoActionPerformed
        // TODO add your handling code here:
        callView("PURCHASE_NO",panEditDelete);
    }//GEN-LAST:event_btnPurchNoActionPerformed

    private void txtPurchNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchNoFocusLost

    private void tdtVoucherDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtVoucherDtFocusLost
        // TODO add your handling code here:if (observable.getActionType() == Clie
        if (tdtVoucherDt.getDateValue().length() > 0 && cboSupplier.getSelectedIndex() > 0) {
            Date voucherDt = (DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtVoucherDt.getDateValue())));
            if ((DateUtil.dateDiff(currDt, voucherDt) <= 0)) {
                String supplierID = CommonUtil.convertObjToStr(((ComboBoxModel) cboSupplier.getModel()).getKeyForSelected());
                String fromDt = (tdtVoucherDt.getDateValue());
                if (!supplierID.equals("")) {
                    observable.setCbmVoucherNo(supplierID, fromDt);
                    cboVoucherNo.setModel(observable.getCbmVoucherNo());
                }
            } else {
                ClientUtil.showMessageWindow("Future Date Not Allowed...");
                tdtVoucherDt.setDateValue("");
                return;
            }
        }
    }//GEN-LAST:event_tdtVoucherDtFocusLost

    private void cboVoucherNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVoucherNoActionPerformed
        // TODO add your handling code here:
        if (cboVoucherNo.getSelectedIndex() >= 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap voucherMap = new HashMap();
            voucherMap.put("VOUCHER_NO", CommonUtil.convertObjToStr(cboVoucherNo.getSelectedItem()));
            List voucherLst = ClientUtil.executeQuery("getValidateVoucherNo", voucherMap);
            if (voucherLst != null && voucherLst.size() > 0) {
                ClientUtil.showMessageWindow("This Voucher No is Pending for Authorization...!");
                btnCancelActionPerformed(null);
                return;
            }
            txtPurchAmt.setText(CommonUtil.convertObjToStr(((ComboBoxModel) cboVoucherNo.getModel()).getKeyForSelected()));
            HashMap purchTypeMap = new HashMap();
            if (cboSupplier.getSelectedIndex() > 0) {
                purchTypeMap.put("PURCHASE_ENTRY_ID", CommonUtil.convertObjToStr(cboVoucherNo.getSelectedItem()));
                purchTypeMap.put("SUPPLIER_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboSupplier.getModel()).getKeyForSelected()));
                List purchaseTypeLst = ClientUtil.executeQuery("getTradingPurchaseType", purchTypeMap);
                if (purchaseTypeLst != null && purchaseTypeLst.size() > 0) {
                    purchTypeMap = (HashMap) purchaseTypeLst.get(0);
                    cboPurchaseType.setSelectedItem(CommonUtil.convertObjToStr(purchTypeMap.get("TRANS_TYPE")));
                    if (cboPurchaseType.getSelectedItem().equals("Cash")) {
                        purchTypeMap = new HashMap();
                        List acheadLst = ClientUtil.executeQuery("getTradingBankAcHead", purchTypeMap);
                        if (acheadLst != null && acheadLst.size() > 0) {
                            purchTypeMap = (HashMap) acheadLst.get(0);
                            txtBankAcHead.setText(CommonUtil.convertObjToStr(purchTypeMap.get("CASH_AC_HD")));
                            if (!txtBankAcHead.getText().equals("")) {
                                lblBankAcHeadDesc.setText(getAccHeadDesc(CommonUtil.convertObjToStr(txtBankAcHead.getText())));
                            }
                        }
                        cboPurchaseType.setEnabled(false);
                        txtBankAcHead.setEnabled(false);
                        lblBankAcHeadDesc.setEnabled(false);
                    } else {
                        if(CommonUtil.convertObjToStr(purchTypeMap.get("CREDIT_FROM")).equals("O")){
                            txtBankAcHead.setText(CommonUtil.convertObjToStr(purchTypeMap.get("AC_HEAD")));
                            lblBankAcHeadDesc.setText(getAccHeadDesc(CommonUtil.convertObjToStr(txtBankAcHead.getText())));
                        }else if(CommonUtil.convertObjToStr(purchTypeMap.get("CREDIT_FROM")).equals("S")){
                           txtBankAcHead.setText(CommonUtil.convertObjToStr(purchTypeMap.get("AC_HEAD")));
                           lblBankAcHeadDesc.setText(CommonUtil.convertObjToStr(cboSupplier.getSelectedItem())); 
                        }
//                        purchTypeMap = new HashMap();
//                        purchTypeMap.put("SUPPLIERID", CommonUtil.convertObjToStr(((ComboBoxModel) cboSupplier.getModel()).getKeyForSelected()));
//                        List sunLst = ClientUtil.executeQuery("getTradingSupplierAcNo", purchTypeMap);
//                        if (sunLst != null && sunLst.size() > 0) {
//                            purchTypeMap = (HashMap) sunLst.get(0);
//                            txtBankAcHead.setText(CommonUtil.convertObjToStr(purchTypeMap.get("SUNDRY_CREDITORS_AC_NO")));
//                            lblBankAcHeadDesc.setText(CommonUtil.convertObjToStr(cboSupplier.getSelectedItem()));
//                        }
                        cboPurchaseType.setEnabled(false);
                        txtBankAcHead.setEnabled(false);
                        lblBankAcHeadDesc.setEnabled(false);
                    }
                }
            }
        }
    }//GEN-LAST:event_cboVoucherNoActionPerformed

    private void txtPurchaseTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseTotalFocusLost
        // TODO add your handling code here:
        if (txtQty.getText() != null && !txtQty.getText().equals("")) {
            if (!chkFree.isSelected()) {
                if (txtPurchaseTotal.getText() != null && !txtPurchaseTotal.getText().equals("")) {
                    double purchPrice = (Double.parseDouble(txtPurchaseTotal.getText()) / Double.parseDouble(txtQty.getText()));
                    txtPurchasePrice.setText(CommonUtil.convertObjToStr(purchPrice));
                } else {
                    ClientUtil.showMessageWindow( "Please enter Total Puchase Pice");
                }
            }
        }
    }//GEN-LAST:event_txtPurchaseTotalFocusLost

    private void cboPurchaseTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPurchaseTypeActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboPurchaseTypeActionPerformed

    private void cboPurchaseTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPurchaseTypeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboPurchaseTypeFocusLost

    private void btnPurchaseDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseDelActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblPurchaseDetails.getValueAt(tblPurchaseDetails.getSelectedRow(), 0));
        observable.deletePurchaseDetails(st, tblPurchaseDetails.getSelectedRow());
        totalAmountCalc();
        observable.resetPurchaseTable();
        btnPurchaseNew.setEnabled(true);
        setClearData();
    }//GEN-LAST:event_btnPurchaseDelActionPerformed

    private void btnPurchaseNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        enableDisableBtn(false);
        setEnableDisable(true);
        txtPurchasePrice.setEnabled(false);
        txtMfgBatchID.setEnabled(true);
        setSubClearData();
        btnPurchaseSave.setEnabled(true);
        btnAddNewItems.setEnabled(true);
        observable.setNewData(true);
    }//GEN-LAST:event_btnPurchaseNewActionPerformed

    private void btnPurchaseSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtProductName.getText()).equals("") ){
            ClientUtil.showMessageWindow("Product Name Should Not be Empty...");
            return;
        } else if (CommonUtil.convertObjToStr(txtQty.getText()).equals("") ) {
            ClientUtil.showMessageWindow("Qty Should Not be Empty...");
             return;
        } else if (CommonUtil.convertObjToStr(txtPurchaseTotal.getText()).equals("") ) {
            ClientUtil.showMessageWindow("Purchase Total Should Not be Empty...");
             return;
        } else if (CommonUtil.convertObjToStr(txtSalesPrice.getText()).equals("") ) {
            ClientUtil.showMessageWindow("Sales Price Should Not be Empty...");
             return;
        } else if (CommonUtil.convertObjToStr(txtMRP.getText()).equals("") ) {
            ClientUtil.showMessageWindow("MRP Should Not be Empty...");
             return;
        } else {
            updateOBFields();
            observable.addDataToSalesDetailsTable(updateTab, updateMode, prod_id);
            totalAmountCalc();
            totalTaxAmountCalc();
            totalDiscAmountCalc();
            txtTP.setText(CommonUtil.convertObjToStr(totalAdjust));
            purchaseTableUpdate();
            observable.resetPurchaseTable();
            setSubClearData();
            enableDisableBtn(false);
            btnPurchaseNew.setEnabled(true);
            ClientUtil.enableDisable(panPurchaseDetails, false);
        }
    }//GEN-LAST:event_btnPurchaseSaveActionPerformed

    private void tdtBillDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtBillDtFocusLost
        // TODO add your handling code here:
        if (tdtBillDt != null && tdtBillDt.getDateValue().length() > 0) {
            Date billDt = (DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtBillDt.getDateValue())));
            if (!(DateUtil.dateDiff(currDt, billDt) <= 0)) {
                ClientUtil.showMessageWindow("Future Date Not Allowed...");
                tdtBillDt.setDateValue("");
                return;
            }
        }
    }//GEN-LAST:event_tdtBillDtFocusLost

    private void txtSalesPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesPriceFocusLost
        // TODO add your handling code here:
        if (txtSalesPrice.getText() != null && txtSalesPrice.getText().length() > 0) {
            if ((CommonUtil.convertObjToDouble(txtMRP.getText()) < CommonUtil.convertObjToDouble(txtSalesPrice.getText()))) {
                ClientUtil.showAlertWindow("Sales Price Should be Lesser than or equal to MRP...");
                txtSalesPrice.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtSalesPriceFocusLost

    private void btnAddNewItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewItemsActionPerformed
        // TODO add your handling code here:
            viewType = newProduct;
            isProduct = true;
            txtProductName.setText("");
            tradingProductUI = new TradingProductUI();
            com.see.truetransact.ui.TrueTransactMain.showScreen(tradingProductUI);
            tradingProductUI.productCreation(this);
    }//GEN-LAST:event_btnAddNewItemsActionPerformed

    private void txtMRPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMRPFocusLost
        // TODO add your handling code here:
        if (txtMRP.getText() != null && txtMRP.getText().length() > 0) {
            if ((CommonUtil.convertObjToDouble(txtMRP.getText()) < CommonUtil.convertObjToDouble(txtPurchasePrice.getText()))) {
                ClientUtil.showAlertWindow("Purchase Price Should be Lesser than or equal to MRP...");
                txtSalesPrice.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtMRPFocusLost

    private void tblPurchaseDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPurchaseDetailsMouseClicked

    private void tblPurchaseReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseReturnMouseClicked
        // TODO add your handling code here:
         if (tblPurchaseReturn.getRowCount() > 0 && tblPurchaseReturn.getSelectedColumn() == 0
                /**&& observable.getActionType() == ClientConstants.ACTIONTYPE_NEW**/) {
            String st = CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(tblPurchaseReturn.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblPurchaseReturn.setValueAt(new Boolean(false), tblPurchaseReturn.getSelectedRow(), 0);
            } else {
                tblPurchaseReturn.setValueAt(new Boolean(true), tblPurchaseReturn.getSelectedRow(), 0);
            }
            //calcTableTotalAmount();
        }
        if (tblPurchaseReturn.getRowCount() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            
        }
        setRightAlignment(8);
    }//GEN-LAST:event_tblPurchaseReturnMouseClicked

    private void tblPurchaseReturnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPurchaseReturnKeyReleased
        // TODO add your handling code here:
        int availQty = 0;
        int retQty = 0;
        double purchPrice = 0.0;
        double returnTot = 0.0;
        if ((tblPurchaseReturn.getRowCount() > 0) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW||
                observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
            availQty = CommonUtil.convertObjToInt(tblPurchaseReturn.getValueAt(tblPurchaseReturn.getSelectedRow(), 7));
            retQty = CommonUtil.convertObjToInt(tblPurchaseReturn.getValueAt(tblPurchaseReturn.getSelectedRow(), 8));
            if (availQty < retQty) {
                ClientUtil.showMessageWindow("Returning Qty should be equal to or lesser than Available Qty.");
                tblPurchaseReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")), tblPurchaseReturn.getSelectedRow(), 8);
                return;
            } else {
                purchPrice = CommonUtil.convertObjToDouble(tblPurchaseReturn.getValueAt(tblPurchaseReturn.getSelectedRow(), 4));
                returnTot = purchPrice * retQty;
                Rounding rod = new Rounding();
                returnTot = (double) rod.getNearest((long) (returnTot * 100), 100) / 100;
                tblPurchaseReturn.setValueAt(CommonUtil.convertObjToDouble(String.valueOf(returnTot)), tblPurchaseReturn.getSelectedRow(), 10);
            }
            totalRetAmountCalc();
            setColorList();
            setColour();
        }
    }//GEN-LAST:event_tblPurchaseReturnKeyReleased
    
    private void setTotalAmount() {
//        double adjustedAmt = 0.0;
//        amountValidation();
//        if (tblSubsidyDetails.getRowCount() > 0) {
//            for (int i = 0; i < tblSubsidyDetails.getRowCount(); i++) {
//                if (((Boolean) tblSubsidyDetails.getValueAt(i, 0)).booleanValue()) {
//                    adjustedAmt = adjustedAmt + CommonUtil.convertObjToDouble(tblSubsidyDetails.getValueAt(i, 7)).doubleValue();
//                }
//            }
//        }
//        txtTotalTodayAdjustment.setText(CommonUtil.convertObjToStr(new Double(adjustedAmt)));
//        //set Transaction Amount For Common Trans Screen
//        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (rdoInterestType_Recovery.isSelected())) {
//            transactionUI.cancelAction(false);
//            transactionUI.setButtonEnableDisable(true);
//            transactionUI.resetObjects();
//            transactionUI.setCallingTransType("TRANSFER");
//            transactionUI.setCallingAmount(txtTotalTodayAdjustment.getText());
//        }
    }
    
    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblPurchaseReturn.getColumnModel().getColumn(col).setCellRenderer(r);
        tblPurchaseReturn.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    private void enableDisableBtn(boolean flag) {
        btnPurchaseNew.setEnabled(flag);
        btnPurchaseSave.setEnabled(flag);
        btnPurchaseDel.setEnabled(flag);
    }
     private void setClearData(){
         txtProductName.setText("");
         txtQty.setText("");
         txtPurchaseTotal.setText("");
         txtTax.setText("");
         txtDiscount.setText("");
         txtMRP.setText("");
         txtSalesPrice.setText("");
         tdtExpiryDt.setDateValue("");
         txtPurchasePrice.setText("");
         txtParticulars.setText("");
         txtPlace.setText("");
         cboUnitType.setSelectedItem("");
         txtBankAcHead.setText("");
         cboSupplier.setEnabled(false);
         txtBillNo.setEnabled(false);
         tdtBillDt.setEnabled(false);
         cboPurchaseType.setEnabled(false);
         txtBankAcHead.setEnabled(false);
         txtIndentNo.setEnabled(false);
         txtBranchCode.setEnabled(false);
     }
     
     private void setSubClearData(){
         txtProductName.setText("");
         txtQty.setText("");
         txtPurchaseTotal.setText("");
         txtTax.setText("");
         txtDiscount.setText("");
         txtMRP.setText("");
         txtSalesPrice.setText("");
         tdtExpiryDt.setDateValue("");
         txtPurchasePrice.setText("");
         txtParticulars.setText("");
         txtPlace.setText("");
         txtMfgBatchID.setText("");
     }
    
    private double totalAmountCalc() {
        double totAmt = 0.0;
        if (tblPurchaseDetails.getRowCount() > 0) {
            for (int i = 0; i < tblPurchaseDetails.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblPurchaseDetails.getValueAt(i, 12));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }

            txtTotal.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
            txtPurchase.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
            txtSalesAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }else{
           for (int i = 0; i < tblPurchaseReturn.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(i, 9));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }

            txtPRPurchase.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }
        return totAmt;
    }
    
    private double totalTaxAmountCalc() {
        double totTaxAmt = 0.0;
        if (tblPurchaseDetails.getRowCount() > 0) {
            for (int i = 0; i < tblPurchaseDetails.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblPurchaseDetails.getValueAt(i, 3));
                Amt = Amt.replace(",", "");
                totTaxAmt = totTaxAmt + CommonUtil.convertObjToDouble(Amt);
            }
        }
        txtTaxTot.setText(CommonUtil.convertObjToStr(new Double(totTaxAmt)));
        return totTaxAmt;
    }
    
     private double totalDiscAmountCalc() {
        double totDiscAmt = 0.0;
        if (tblPurchaseDetails.getRowCount() > 0) {
            for (int i = 0; i < tblPurchaseDetails.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblPurchaseDetails.getValueAt(i, 11));
                Amt = Amt.replace(",", "");
                totDiscAmt = totDiscAmt + CommonUtil.convertObjToDouble(Amt);
            }
        }
        txtDiscountTot.setText(CommonUtil.convertObjToStr(new Double(totDiscAmt)));
        return totDiscAmt;
    }
    private double totalRetAmountCalc() {
        double totAmt = 0.0;
        if (tblPurchaseReturn.getRowCount() > 0) {
            for (int i = 0; i < tblPurchaseReturn.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblPurchaseReturn.getValueAt(i, 10));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
            txtReturn.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }
        return totAmt;
    }
     
     public String getAccHeadDesc(String accHeadID) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHeadID);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHeadDesc = map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        } else {
            return "";
        }
    }
     
     private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            if(panPurchase.isShowing()==true){
            HashMap whereMap = new HashMap();
            StringBuffer PRODUCT_NAME = getProductName();
            whereMap.put("UN_AUTHORIZE", "UN_AUTHORIZE");
            whereMap.put("PRODUCT_NAME", PRODUCT_NAME);
            List prodLst = ClientUtil.executeQuery("getTradingProductPendingForAuthorize", whereMap);
            if (prodLst != null && prodLst.size() > 0) {
                ClientUtil.showMessageWindow("Please Authorize The New Products Added.");
                // checking Product Pending Authorize List
                String prodName = "";
                new TradingProductDetailsUI(prodName).show();
            } else {
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("PURCHASE_NO", lblPurchaseNoVal.getText());
                singleAuthorizeMap.put("VOUCHER_NO", cboVoucherNo.getSelectedItem());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("PURCHASE", "PURCHASE");
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
            }else if(panPurchaseReturn.isShowing()==true){
               ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("PURCHASE_RET_NO", txtPurchRetNo.getText());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                singleAuthorizeMap.put("PURCHASE_RETURN", "PURCHASE_RETURN");
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
            if(panPurchase.isShowing()==true){
            mapParam.put(CommonConstants.MAP_NAME, "getTradingPurchaseForAuthorize");
            }else if(panPurchaseReturn.isShowing()==true){
             mapParam.put(CommonConstants.MAP_NAME, "getTradingPurchReturnForAuthorize");   
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
            if(panPurchase.isShowing() == true){
                pan = PURCHASE;
                observable.setAuthorizeMap(map);
                observable.doAction(pan);
            } else if (panPurchaseReturn.isShowing() == true) {
                pan = PURCHASERETURN;
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
    
    private void setColorList() {
        if (tblPurchaseReturn.getRowCount() > 0) {
            colourList = new ArrayList();
            int retQty = 0;
            for (int i = 0; i < tblPurchaseReturn.getRowCount(); i++) {
                retQty = CommonUtil.convertObjToInt(tblPurchaseReturn.getValueAt(i, 8));
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
        tblPurchaseReturn.setDefaultRenderer(Object.class, renderer);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddNewItems;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProductName;
    private com.see.truetransact.uicomponent.CButton btnPurchNo;
    private com.see.truetransact.uicomponent.CButton btnPurchaseDel;
    private com.see.truetransact.uicomponent.CButton btnPurchaseNew;
    private com.see.truetransact.uicomponent.CButton btnPurchaseSave;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboPurchaseType;
    private com.see.truetransact.uicomponent.CComboBox cboSupplier;
    private com.see.truetransact.uicomponent.CComboBox cboSupplierName;
    private com.see.truetransact.uicomponent.CComboBox cboUnitType;
    private com.see.truetransact.uicomponent.CComboBox cboVoucherNo;
    private com.see.truetransact.uicomponent.CCheckBox chkFree;
    private com.see.truetransact.uicomponent.CCheckBox chkShrinkage;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAllignment;
    private com.see.truetransact.uicomponent.CLabel lblBankAcHead;
    private com.see.truetransact.uicomponent.CLabel lblBankAcHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblBillDt;
    private com.see.truetransact.uicomponent.CLabel lblBillNo;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblDiscount;
    private com.see.truetransact.uicomponent.CLabel lblDiscountTot;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDt;
    private com.see.truetransact.uicomponent.CLabel lblIndentNo;
    private com.see.truetransact.uicomponent.CLabel lblMRP;
    private com.see.truetransact.uicomponent.CLabel lblMfgBatchID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPRBillNo;
    private com.see.truetransact.uicomponent.CLabel lblPRPurchase;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblPlace;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblPurchAmt;
    private com.see.truetransact.uicomponent.CLabel lblPurchBankHd;
    private com.see.truetransact.uicomponent.CLabel lblPurchBillDt;
    private com.see.truetransact.uicomponent.CLabel lblPurchDt;
    private com.see.truetransact.uicomponent.CLabel lblPurchNo;
    private com.see.truetransact.uicomponent.CLabel lblPurchRetDt;
    private com.see.truetransact.uicomponent.CLabel lblPurchRetNo;
    private com.see.truetransact.uicomponent.CLabel lblPurchReturn;
    private com.see.truetransact.uicomponent.CLabel lblPurchType;
    private com.see.truetransact.uicomponent.CLabel lblPurchase;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseNo;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseNoVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchasePrice;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseTotal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseType;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblQtyUnit;
    private com.see.truetransact.uicomponent.CLabel lblReturn;
    private com.see.truetransact.uicomponent.CLabel lblSalesAmt;
    private com.see.truetransact.uicomponent.CLabel lblSalesPrice;
    private com.see.truetransact.uicomponent.CLabel lblShrinkageQty;
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
    private com.see.truetransact.uicomponent.CLabel lblSupplier;
    private com.see.truetransact.uicomponent.CLabel lblSupplierName;
    private com.see.truetransact.uicomponent.CLabel lblTP;
    private com.see.truetransact.uicomponent.CLabel lblTax;
    private com.see.truetransact.uicomponent.CLabel lblTaxTot;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTransportationCharge;
    private com.see.truetransact.uicomponent.CLabel lblUnitType;
    private com.see.truetransact.uicomponent.CLabel lblVoucherDt;
    private com.see.truetransact.uicomponent.CLabel lblVoucherNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCutOff;
    private com.see.truetransact.uicomponent.CPanel panProductName;
    private com.see.truetransact.uicomponent.CPanel panPurchNo;
    private com.see.truetransact.uicomponent.CPanel panPurchase;
    private com.see.truetransact.uicomponent.CPanel panPurchaseAmtDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturn;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturnDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturnSubDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturnTableDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturnTotalDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseTableDetails;
    private com.see.truetransact.uicomponent.CPanel panPurchaseTotalDetails;
    private com.see.truetransact.uicomponent.CPanel panShrinkage;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpPurchaseReturn;
    private com.see.truetransact.uicomponent.CScrollPane srpPurchaseTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabTradingPurchase;
    private com.see.truetransact.uicomponent.CTable tblPurchaseDetails;
    private com.see.truetransact.uicomponent.CTable tblPurchaseReturn;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtBillDt;
    private com.see.truetransact.uicomponent.CDateField tdtExpiryDt;
    private com.see.truetransact.uicomponent.CDateField tdtPurchBillDt;
    private com.see.truetransact.uicomponent.CDateField tdtPurchDt;
    private com.see.truetransact.uicomponent.CDateField tdtPurchRetDt;
    private com.see.truetransact.uicomponent.CDateField tdtVoucherDt;
    private com.see.truetransact.uicomponent.CTextField txtBankAcHead;
    private com.see.truetransact.uicomponent.CTextField txtBillNo;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtDiscount;
    private com.see.truetransact.uicomponent.CTextField txtDiscountTot;
    private com.see.truetransact.uicomponent.CTextField txtIndentNo;
    private com.see.truetransact.uicomponent.CTextField txtMRP;
    private com.see.truetransact.uicomponent.CTextField txtMfgBatchID;
    private com.see.truetransact.uicomponent.CTextField txtPRBillNo;
    private com.see.truetransact.uicomponent.CTextField txtPRPurchase;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtPlace;
    private com.see.truetransact.uicomponent.CTextField txtProductName;
    private com.see.truetransact.uicomponent.CTextField txtPurchAmt;
    private com.see.truetransact.uicomponent.CTextField txtPurchBankHd;
    private com.see.truetransact.uicomponent.CTextField txtPurchNo;
    private com.see.truetransact.uicomponent.CTextField txtPurchRetNo;
    private com.see.truetransact.uicomponent.CTextField txtPurchReturn;
    private com.see.truetransact.uicomponent.CTextField txtPurchType;
    private com.see.truetransact.uicomponent.CTextField txtPurchase;
    private com.see.truetransact.uicomponent.CTextField txtPurchasePrice;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseTotal;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtQtyUnit;
    private com.see.truetransact.uicomponent.CTextField txtReturn;
    private com.see.truetransact.uicomponent.CTextField txtSalesAmt;
    private com.see.truetransact.uicomponent.CTextField txtSalesPrice;
    private com.see.truetransact.uicomponent.CTextField txtShrinkageQty;
    private com.see.truetransact.uicomponent.CTextField txtTP;
    private com.see.truetransact.uicomponent.CTextField txtTax;
    private com.see.truetransact.uicomponent.CTextField txtTaxTot;
    private com.see.truetransact.uicomponent.CTextField txtTotal;
    private com.see.truetransact.uicomponent.CTextField txtTransPortationCharge;
    // End of variables declaration//GEN-END:variables
    
}
