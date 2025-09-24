/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingTransferUI.java
 *
 * Created on April 29, 2016, 12.07 PM
 */

package com.see.truetransact.ui.trading.tradingtransfer;

/**
 *
 * @author  Revathi.L
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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingTransferUI extends CInternalFrame implements UIMandatoryField,Observer{
    
    
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.shopmaster.ShopMasterRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingTransferMRB objMandatoryRB = new TradingTransferMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingTransferOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    Date currDt = null;
    int updateTab = -1;
    private boolean updateMode = false;
    boolean isFilled = false;
    
    /** Creates new form TDSConfigUI */
    public TradingTransferUI() {
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
        observable.resetForm();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panTransfer, false);
        btnTransNew.setEnabled(false);
        btnTransSave.setEnabled(false);
        btnTransDel.setEnabled(false);
        setButtonEnableDisable();
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
        panTransfer.setName("panTDS");
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
        txtTransferQty.setAllowNumber(true);
        txtFromBranch.setAllowNumber(true);
        txtToBranch.setAllowNumber(true);
        txtFromBranch.setValidation(new NumericValidation());
        txtToBranch.setValidation(new NumericValidation());
        txtTransferQty.setValidation(new NumericValidation());
        txtTotalTransAmt.setValidation(new CurrencyValidation(14, 2));
    }
    
    /**  This method is to add this class as an Observer to an Observable **/
    private void setObservable(){
        try{
            observable = TradingTransferOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try{
           tblTransferDetails.setModel(observable.getTblTransferDetails()); 
        }catch(ClassCastException e){
            parseException.logException(e,true);
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
    public void updateOBFields() {
        observable.setTransferID(CommonUtil.convertObjToStr(lblTransferIDVal.getText()));
        observable.setProdID(CommonUtil.convertObjToStr(txtProdID.getText()));
        observable.setProdName(CommonUtil.convertObjToStr(lblProdName.getText()));
        observable.setStockID(CommonUtil.convertObjToStr(txtStockID.getText()));
        observable.setUnitType(CommonUtil.convertObjToStr(txtUnitType.getText()));
        observable.setAvailQty(CommonUtil.convertObjToStr(txtAvailQty.getText()));
        observable.setPurchPrice(CommonUtil.convertObjToStr(txtPurchPrice.getText()));
        observable.setSalesPrice(CommonUtil.convertObjToStr(txtSalesPrice.getText()));
        observable.setTransferQty(CommonUtil.convertObjToStr(txtTransferQty.getText()));
        observable.setFromBranch(CommonUtil.convertObjToStr(txtFromBranch.getText()));
        observable.setToBranch(CommonUtil.convertObjToStr(txtToBranch.getText()));
        observable.setAchd(CommonUtil.convertObjToStr(txtAcHd.getText()));
        observable.setTdtTransDt(CommonUtil.convertObjToStr(tdtDate.getDateValue()));
        
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    public void update() {
        lblTransferIDVal.setText(CommonUtil.convertObjToStr(observable.getTransferID()));
        txtProdID.setText(CommonUtil.convertObjToStr(observable.getProdID()));
        lblProdName.setText(CommonUtil.convertObjToStr(observable.getProdName()));
        txtStockID.setText(CommonUtil.convertObjToStr(observable.getStockID()));
        txtUnitType.setText(CommonUtil.convertObjToStr(observable.getUnitType()));
        txtAvailQty.setText(CommonUtil.convertObjToStr(observable.getAvailQty()));
        txtTransferQty.setText(CommonUtil.convertObjToStr(observable.getTransferQty()));
        txtPurchPrice.setText(CommonUtil.convertObjToStr(observable.getPurchPrice()));
        txtSalesPrice.setText(CommonUtil.convertObjToStr(observable.getSalesPrice()));
        txtFromBranch.setText(CommonUtil.convertObjToStr(observable.getFromBranch()));
        txtToBranch.setText(CommonUtil.convertObjToStr(observable.getToBranch()));
        tdtDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtTransDt()));
        txtAcHd.setText(CommonUtil.convertObjToStr(observable.getAchd()));
        lblAcHdVal.setText(getAccHeadDesc(txtAcHd.getText()));
        lblfromBranchName.setText(getBranchName(txtFromBranch.getText()));
        lblToBranchName.setText(getBranchName(txtToBranch.getText()));
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
     
    public String getBranchName(String branchCode) {
        HashMap hmap = new HashMap();
        hmap.put("BRANCH_CODE", branchCode);
        List lst = ClientUtil.executeQuery("getCityForDD", hmap);
        if (lst != null && lst.size() > 0) {
            hmap = (HashMap) lst.get(0);
            String branchName = hmap.get("BRANCH_NAME").toString();
            return branchName;
        } else {
            return "";
        }
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("FROM_BRANCH") || currField.equalsIgnoreCase("TO_BRANCH")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getRegionalOffice");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("PROD_ID")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingProductList");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("AC_HD")){
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("STOCK_ID")) {
            HashMap map = new HashMap();
            map.put("PRODUCT_ID", CommonUtil.convertObjToStr(txtProdID.getText()));
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingStockSingleList");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEditTransferDetails");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getViewTransferDetails");
            new ViewAll(this, viewMap).show();
        }
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if(viewType.equalsIgnoreCase("FROM_BRANCH")){
            txtFromBranch.setText(CommonUtil.convertObjToStr(hash.get("BRANCH CODE")));
            lblfromBranchName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH NAME")));
            if (txtFromBranch.getText().length() > 0 && txtToBranch.getText().length() > 0) {
                if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                        || (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                    if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                            && (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                        ClientUtil.showMessageWindow("Stock transfer it cannot be done between same branch.Change the From/To BranchID.");
                        return;
                    }
                    if (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)) {
                        lblAcHd.setText("Credit GL A/C Head");
                    } else {
                        lblAcHd.setText("Debit GL A/C Head");
                    }
                } else {
                    ClientUtil.showMessageWindow("Enter your BranchID for From BranchID or To BranchID.");
                    txtFromBranch.setText("");
                    txtToBranch.setText("");
                    return;
                }
            }
        }else if(viewType.equalsIgnoreCase("TO_BRANCH")){
            txtToBranch.setText(CommonUtil.convertObjToStr(hash.get("BRANCH CODE")));
            lblToBranchName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH NAME")));
            if((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                    || (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))){
                if((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                    && (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))){
                    ClientUtil.showMessageWindow("Stock transfer it cannot be done between same branch.Change the From/To BranchID.");
                    return;
                }
                if(CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)){
                    lblAcHd.setText("Credit GL A/C Head");
                }else{
                    lblAcHd.setText("Debit GL A/C Head");
                }
            }else{
                ClientUtil.showMessageWindow("Enter your BranchID for From BranchID or To BranchID.");
                txtFromBranch.setText("");
                txtToBranch.setText("");
                return;
            }
        }else if(viewType.equalsIgnoreCase("PROD_ID")){
            resetForm();
            txtProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
            lblProdName.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_NAME")));
        } else if (viewType.equalsIgnoreCase("AC_HD")) {
            txtAcHd.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            lblAcHdVal.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
        }else if (viewType.equalsIgnoreCase("STOCK_ID")) {
            txtStockID.setText(CommonUtil.convertObjToStr(hash.get("STOCK_ID")));
            txtUnitType.setText(CommonUtil.convertObjToStr(hash.get("TYPE")));
            txtPurchPrice.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_PRICE")));
            txtSalesPrice.setText(CommonUtil.convertObjToStr(hash.get("SALES_PRICE")));
            txtAvailQty.setText(CommonUtil.convertObjToStr(hash.get("QTY")));
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            observable.getData(hash);
            tblTransferDetails.setModel(observable.getTblTransferDetails());
            update();
            btnAcHd.setEnabled(true);
            if (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)) {
                lblAcHd.setText("Credit GL A/C Head");
            } else {
                lblAcHd.setText("Debit GL A/C Head");
            }
            totalAmountCalc();
            totalTransferQty();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            observable.getData(hash);
            tblTransferDetails.setModel(observable.getTblTransferDetails());
            update();
            if (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)) {
                lblAcHd.setText("Credit GL A/C Head");
            } else {
                lblAcHd.setText("Debit GL A/C Head");
            }
            totalAmountCalc();
            totalTransferQty();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
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
    private void saveAction(String status) {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_ID")) {
                    ClientUtil.showMessageWindow("TRANSFER ID : " + observable.getProxyReturnMap().get("TRANSFER_ID"));
                }
            }
        }
        btnCancelActionPerformed(null);
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTransfer, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    public void resetForm(){
        txtProdID.setText("");
        lblProdName.setText("");
        txtStockID.setText("");
        txtUnitType.setText("");
        txtTransferQty.setText("");
        txtPurchPrice.setText("");
        txtSalesPrice.setText("");
        txtAvailQty.setText("");
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
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
        panTransfer = new com.see.truetransact.uicomponent.CPanel();
        panAcHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAcHd = new com.see.truetransact.uicomponent.CLabel();
        panAcHd = new com.see.truetransact.uicomponent.CPanel();
        txtAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnAcHd = new com.see.truetransact.uicomponent.CButton();
        lblAcHdVal = new com.see.truetransact.uicomponent.CLabel();
        panTransferProductDetails = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        panStockID = new com.see.truetransact.uicomponent.CPanel();
        txtStockID = new com.see.truetransact.uicomponent.CTextField();
        btnStockID = new com.see.truetransact.uicomponent.CButton();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        panProdID = new com.see.truetransact.uicomponent.CPanel();
        txtProdID = new com.see.truetransact.uicomponent.CTextField();
        btnProdID = new com.see.truetransact.uicomponent.CButton();
        lbStockID = new com.see.truetransact.uicomponent.CLabel();
        lblProdName = new com.see.truetransact.uicomponent.CLabel();
        lblUnitType = new com.see.truetransact.uicomponent.CLabel();
        txtUnitType = new com.see.truetransact.uicomponent.CTextField();
        panProductRateDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransferQty = new com.see.truetransact.uicomponent.CLabel();
        txtTransferQty = new com.see.truetransact.uicomponent.CTextField();
        lblAvailQty = new com.see.truetransact.uicomponent.CLabel();
        txtAvailQty = new com.see.truetransact.uicomponent.CTextField();
        lblPurchPrice = new com.see.truetransact.uicomponent.CLabel();
        txtPurchPrice = new com.see.truetransact.uicomponent.CTextField();
        lblSalesPrice = new com.see.truetransact.uicomponent.CLabel();
        txtSalesPrice = new com.see.truetransact.uicomponent.CTextField();
        panTransferTableDetails = new com.see.truetransact.uicomponent.CPanel();
        panTotalAmount = new com.see.truetransact.uicomponent.CPanel();
        btnTransNew = new com.see.truetransact.uicomponent.CButton();
        btnTransSave = new com.see.truetransact.uicomponent.CButton();
        btnTransDel = new com.see.truetransact.uicomponent.CButton();
        lblTotalTransQty = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalTransAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotalTransQty = new com.see.truetransact.uicomponent.CTextField();
        srpTransferDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransferDetails = new com.see.truetransact.uicomponent.CTable();
        panBranchDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTransferID = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        lblTransferIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblToBranch = new com.see.truetransact.uicomponent.CLabel();
        panToBranch = new com.see.truetransact.uicomponent.CPanel();
        txtToBranch = new com.see.truetransact.uicomponent.CTextField();
        btnToBranch = new com.see.truetransact.uicomponent.CButton();
        lblFromBranch = new com.see.truetransact.uicomponent.CLabel();
        panFromBranch = new com.see.truetransact.uicomponent.CPanel();
        txtFromBranch = new com.see.truetransact.uicomponent.CTextField();
        btnFromBranch = new com.see.truetransact.uicomponent.CButton();
        lblToBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblfromBranchName = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(950, 650));
        setPreferredSize(new java.awt.Dimension(950, 650));

        panTransfer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTransfer.setMinimumSize(new java.awt.Dimension(920, 450));
        panTransfer.setPreferredSize(new java.awt.Dimension(920, 450));
        panTransfer.setLayout(new java.awt.GridBagLayout());

        panAcHeadDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAcHeadDetails.setMaximumSize(new java.awt.Dimension(910, 32));
        panAcHeadDetails.setMinimumSize(new java.awt.Dimension(910, 32));
        panAcHeadDetails.setPreferredSize(new java.awt.Dimension(910, 32));
        panAcHeadDetails.setLayout(new java.awt.GridBagLayout());

        lblAcHd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAcHd.setText("Debit GL A/C Head");
        lblAcHd.setMaximumSize(new java.awt.Dimension(150, 18));
        lblAcHd.setMinimumSize(new java.awt.Dimension(150, 18));
        lblAcHd.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcHeadDetails.add(lblAcHd, gridBagConstraints);

        panAcHd.setMinimumSize(new java.awt.Dimension(128, 29));
        panAcHd.setPreferredSize(new java.awt.Dimension(128, 29));
        panAcHd.setLayout(new java.awt.GridBagLayout());

        txtAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcHdActionPerformed(evt);
            }
        });
        txtAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHd.add(txtAcHd, gridBagConstraints);

        btnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcHd.setEnabled(false);
        btnAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHd.add(btnAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcHeadDetails.add(panAcHd, gridBagConstraints);

        lblAcHdVal.setForeground(new java.awt.Color(0, 51, 255));
        lblAcHdVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAcHdVal.setMaximumSize(new java.awt.Dimension(300, 18));
        lblAcHdVal.setMinimumSize(new java.awt.Dimension(300, 18));
        lblAcHdVal.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcHeadDetails.add(lblAcHdVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTransfer.add(panAcHeadDetails, gridBagConstraints);

        panTransferProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Tranfer Product Details"));
        panTransferProductDetails.setMaximumSize(new java.awt.Dimension(910, 120));
        panTransferProductDetails.setMinimumSize(new java.awt.Dimension(910, 120));
        panTransferProductDetails.setPreferredSize(new java.awt.Dimension(910, 120));
        panTransferProductDetails.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setMaximumSize(new java.awt.Dimension(500, 150));
        panProductDetails.setMinimumSize(new java.awt.Dimension(500, 150));
        panProductDetails.setPreferredSize(new java.awt.Dimension(500, 150));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        panStockID.setMinimumSize(new java.awt.Dimension(128, 29));
        panStockID.setPreferredSize(new java.awt.Dimension(128, 29));
        panStockID.setLayout(new java.awt.GridBagLayout());

        txtStockID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStockID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockIDActionPerformed(evt);
            }
        });
        txtStockID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStockID.add(txtStockID, gridBagConstraints);

        btnStockID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStockID.setEnabled(false);
        btnStockID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnStockID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnStockID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStockID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStockID.add(btnStockID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panProductDetails.add(panStockID, gridBagConstraints);

        lblProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdID.setText("Prod ID");
        lblProdID.setMaximumSize(new java.awt.Dimension(100, 18));
        lblProdID.setMinimumSize(new java.awt.Dimension(100, 18));
        lblProdID.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblProdID, gridBagConstraints);

        panProdID.setMinimumSize(new java.awt.Dimension(128, 29));
        panProdID.setPreferredSize(new java.awt.Dimension(128, 29));
        panProdID.setLayout(new java.awt.GridBagLayout());

        txtProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdIDActionPerformed(evt);
            }
        });
        txtProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdID.add(txtProdID, gridBagConstraints);

        btnProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProdID.setEnabled(false);
        btnProdID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnProdID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProdID.add(btnProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panProductDetails.add(panProdID, gridBagConstraints);

        lbStockID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbStockID.setText("Stock ID");
        lbStockID.setMaximumSize(new java.awt.Dimension(100, 18));
        lbStockID.setMinimumSize(new java.awt.Dimension(100, 18));
        lbStockID.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lbStockID, gridBagConstraints);

        lblProdName.setForeground(new java.awt.Color(0, 51, 255));
        lblProdName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProdName.setMaximumSize(new java.awt.Dimension(300, 18));
        lblProdName.setMinimumSize(new java.awt.Dimension(300, 18));
        lblProdName.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panProductDetails.add(lblProdName, gridBagConstraints);

        lblUnitType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnitType.setText("Unit Type");
        lblUnitType.setMaximumSize(new java.awt.Dimension(100, 18));
        lblUnitType.setMinimumSize(new java.awt.Dimension(100, 18));
        lblUnitType.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblUnitType, gridBagConstraints);

        txtUnitType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUnitType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnitTypeActionPerformed(evt);
            }
        });
        txtUnitType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUnitTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(txtUnitType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransferProductDetails.add(panProductDetails, gridBagConstraints);

        panProductRateDetails.setMaximumSize(new java.awt.Dimension(500, 150));
        panProductRateDetails.setMinimumSize(new java.awt.Dimension(500, 150));
        panProductRateDetails.setPreferredSize(new java.awt.Dimension(500, 150));
        panProductRateDetails.setLayout(new java.awt.GridBagLayout());

        lblTransferQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransferQty.setText("Transfer Qty");
        lblTransferQty.setMaximumSize(new java.awt.Dimension(100, 18));
        lblTransferQty.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTransferQty.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(lblTransferQty, gridBagConstraints);

        txtTransferQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransferQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransferQtyActionPerformed(evt);
            }
        });
        txtTransferQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransferQtyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(txtTransferQty, gridBagConstraints);

        lblAvailQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAvailQty.setText("Avail Qty");
        lblAvailQty.setMaximumSize(new java.awt.Dimension(100, 18));
        lblAvailQty.setMinimumSize(new java.awt.Dimension(100, 18));
        lblAvailQty.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(lblAvailQty, gridBagConstraints);

        txtAvailQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAvailQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAvailQtyActionPerformed(evt);
            }
        });
        txtAvailQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAvailQtyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(txtAvailQty, gridBagConstraints);

        lblPurchPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchPrice.setText("Purchase Price");
        lblPurchPrice.setMaximumSize(new java.awt.Dimension(100, 18));
        lblPurchPrice.setMinimumSize(new java.awt.Dimension(100, 18));
        lblPurchPrice.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(lblPurchPrice, gridBagConstraints);

        txtPurchPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurchPriceActionPerformed(evt);
            }
        });
        txtPurchPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchPriceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(txtPurchPrice, gridBagConstraints);

        lblSalesPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesPrice.setText("Sales Price");
        lblSalesPrice.setMaximumSize(new java.awt.Dimension(100, 18));
        lblSalesPrice.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSalesPrice.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(lblSalesPrice, gridBagConstraints);

        txtSalesPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesPriceActionPerformed(evt);
            }
        });
        txtSalesPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesPriceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductRateDetails.add(txtSalesPrice, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = -148;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransferProductDetails.add(panProductRateDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 51;
        panTransfer.add(panTransferProductDetails, gridBagConstraints);

        panTransferTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Transfer Details"));
        panTransferTableDetails.setMaximumSize(new java.awt.Dimension(910, 230));
        panTransferTableDetails.setMinimumSize(new java.awt.Dimension(910, 230));
        panTransferTableDetails.setPreferredSize(new java.awt.Dimension(910, 230));
        panTransferTableDetails.setLayout(new java.awt.GridBagLayout());

        panTotalAmount.setMaximumSize(new java.awt.Dimension(700, 32));
        panTotalAmount.setMinimumSize(new java.awt.Dimension(700, 32));
        panTotalAmount.setPreferredSize(new java.awt.Dimension(700, 32));
        panTotalAmount.setLayout(new java.awt.GridBagLayout());

        btnTransNew.setText("New");
        btnTransNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(btnTransNew, gridBagConstraints);

        btnTransSave.setText("Save");
        btnTransSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(btnTransSave, gridBagConstraints);

        btnTransDel.setText("Delete");
        btnTransDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(btnTransDel, gridBagConstraints);

        lblTotalTransQty.setForeground(new java.awt.Color(0, 102, 51));
        lblTotalTransQty.setText("Total Transfer Qty: ");
        lblTotalTransQty.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(lblTotalTransQty, gridBagConstraints);

        lblTotalTransAmt.setForeground(new java.awt.Color(0, 102, 51));
        lblTotalTransAmt.setText("Total Transfer Amount : ");
        lblTotalTransAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(lblTotalTransAmt, gridBagConstraints);

        txtTotalTransAmt.setForeground(new java.awt.Color(0, 102, 51));
        txtTotalTransAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalTransAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalTransAmtActionPerformed(evt);
            }
        });
        txtTotalTransAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalTransAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(txtTotalTransAmt, gridBagConstraints);

        txtTotalTransQty.setForeground(new java.awt.Color(0, 102, 51));
        txtTotalTransQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalTransQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalTransQtyActionPerformed(evt);
            }
        });
        txtTotalTransQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalTransQtyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTotalAmount.add(txtTotalTransQty, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransferTableDetails.add(panTotalAmount, gridBagConstraints);

        srpTransferDetails.setPreferredSize(new java.awt.Dimension(454, 344));

        tblTransferDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Prod ID", "Prod Name", "Type", "Stock ID", "Avail Qty", "Purh Price", "Sales Price", "Transfer Qty", "Total Amt"
            }
        ));
        tblTransferDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblTransferDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblTransferDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblTransferDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransferDetailsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTransferDetailsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblTransferDetailsMouseReleased(evt);
            }
        });
        tblTransferDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblTransferDetailsFocusLost(evt);
            }
        });
        tblTransferDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblTransferDetailsKeyReleased(evt);
            }
        });
        srpTransferDetails.setViewportView(tblTransferDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransferTableDetails.add(srpTransferDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panTransfer.add(panTransferTableDetails, gridBagConstraints);

        panBranchDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panBranchDetails.setMaximumSize(new java.awt.Dimension(910, 60));
        panBranchDetails.setMinimumSize(new java.awt.Dimension(910, 60));
        panBranchDetails.setPreferredSize(new java.awt.Dimension(910, 60));
        panBranchDetails.setLayout(new java.awt.GridBagLayout());

        lblTransferID.setForeground(new java.awt.Color(255, 51, 51));
        lblTransferID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransferID.setText("Transfer ID : ");
        lblTransferID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTransferID.setMaximumSize(new java.awt.Dimension(100, 18));
        lblTransferID.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTransferID.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDetails.add(lblTransferID, gridBagConstraints);

        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("Date : ");
        lblDate.setMaximumSize(new java.awt.Dimension(70, 18));
        lblDate.setMinimumSize(new java.awt.Dimension(70, 18));
        lblDate.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDetails.add(lblDate, gridBagConstraints);

        tdtDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBranchDetails.add(tdtDate, gridBagConstraints);

        lblTransferIDVal.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblTransferIDVal.setForeground(new java.awt.Color(255, 51, 51));
        lblTransferIDVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTransferIDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTransferIDVal.setMaximumSize(new java.awt.Dimension(10, 20));
        lblTransferIDVal.setMinimumSize(new java.awt.Dimension(10, 20));
        lblTransferIDVal.setPreferredSize(new java.awt.Dimension(10, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panBranchDetails.add(lblTransferIDVal, gridBagConstraints);

        lblToBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToBranch.setText("To Branch ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDetails.add(lblToBranch, gridBagConstraints);

        panToBranch.setMinimumSize(new java.awt.Dimension(128, 29));
        panToBranch.setPreferredSize(new java.awt.Dimension(128, 29));
        panToBranch.setLayout(new java.awt.GridBagLayout());

        txtToBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToBranchActionPerformed(evt);
            }
        });
        txtToBranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToBranchFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToBranch.add(txtToBranch, gridBagConstraints);

        btnToBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToBranch.setEnabled(false);
        btnToBranch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnToBranch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToBranch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToBranch.add(btnToBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panBranchDetails.add(panToBranch, gridBagConstraints);

        lblFromBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromBranch.setText("From Branch ID");
        lblFromBranch.setMaximumSize(new java.awt.Dimension(100, 18));
        lblFromBranch.setMinimumSize(new java.awt.Dimension(100, 18));
        lblFromBranch.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchDetails.add(lblFromBranch, gridBagConstraints);

        panFromBranch.setMinimumSize(new java.awt.Dimension(128, 29));
        panFromBranch.setPreferredSize(new java.awt.Dimension(128, 29));
        panFromBranch.setLayout(new java.awt.GridBagLayout());

        txtFromBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromBranchActionPerformed(evt);
            }
        });
        txtFromBranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromBranchFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFromBranch.add(txtFromBranch, gridBagConstraints);

        btnFromBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromBranch.setEnabled(false);
        btnFromBranch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnFromBranch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromBranch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromBranch.add(btnFromBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panBranchDetails.add(panFromBranch, gridBagConstraints);

        lblToBranchName.setForeground(new java.awt.Color(0, 51, 255));
        lblToBranchName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblToBranchName.setMaximumSize(new java.awt.Dimension(300, 18));
        lblToBranchName.setMinimumSize(new java.awt.Dimension(300, 18));
        lblToBranchName.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panBranchDetails.add(lblToBranchName, gridBagConstraints);

        lblfromBranchName.setForeground(new java.awt.Color(0, 51, 255));
        lblfromBranchName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblfromBranchName.setMaximumSize(new java.awt.Dimension(300, 18));
        lblfromBranchName.setMinimumSize(new java.awt.Dimension(300, 18));
        lblfromBranchName.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panBranchDetails.add(lblfromBranchName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTransfer.add(panBranchDetails, gridBagConstraints);

        getContentPane().add(panTransfer, java.awt.BorderLayout.CENTER);

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
        ClientUtil.enableDisable(panTransfer, false);
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
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        //super.removeEditLock(txtTdsId.getText());
        observable.resetForm();
        observable.resetMainDetails();
        observable.resetMap();
        resetForm();
        observable.resetTransferTblDetails();
        txtFromBranch.setText("");
        txtToBranch.setText("");
        txtAcHd.setText("");
        lblAcHdVal.setText("");
        lblTransferIDVal.setText("");
        lblfromBranchName.setText("");
        lblToBranchName.setText("");
        btnAcHd.setEnabled(false);
        btnFromBranch.setEnabled(false);
        btnToBranch.setEnabled(false);
        btnProdID.setEnabled(false);
        btnStockID.setEnabled(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTransfer, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        //btnTdsGLAccountHead.setEnabled(false);
        viewType = "";
        //__ Make the Screen Closable..
        setModified(false);
         btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtFromBranch.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("From Branch Should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(txtToBranch.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("To Branch Should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(txtAcHd.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("Account Head Should not be empty");
            return;
        } else {
            if (tblTransferDetails.getRowCount() > 0) {
                savePerformed();
            } else {
                ClientUtil.showMessageWindow("No Records For Transfer!!!");
                return;
            }
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnTransNew.setEnabled(true);
        txtFromBranch.setEnabled(true);
        txtToBranch.setEnabled(true);
        btnFromBranch.setEnabled(true);
        btnToBranch.setEnabled(true);
        tdtDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        setButtonEnableDisable();
        setModified(true);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcHdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcHdActionPerformed

    private void txtAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcHdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcHdFocusLost

    private void btnAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcHdActionPerformed
        // TODO add your handling code here:
        callView("AC_HD");
    }//GEN-LAST:event_btnAcHdActionPerformed

    private void txtToBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToBranchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToBranchActionPerformed

    private void txtToBranchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToBranchFocusLost
        // TODO add your handling code here:
        if (txtToBranch.getText().length() > 0 && txtFromBranch.getText().length()>0) {
            HashMap hmap = new HashMap();
            hmap.put("BRANCH_CODE", txtToBranch.getText());
            List lst = ClientUtil.executeQuery("getCityForDD", hmap);
            if (lst != null && lst.size() > 0) {
                hmap = (HashMap) lst.get(0);
                lblToBranchName.setText(CommonUtil.convertObjToStr(hmap.get("BRANCH_NAME")));
            } else {
                ClientUtil.displayAlert("Invalid branch code");
                txtToBranch.setText("");
                lblToBranchName.setText("Branch Name");
            }
            if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                    || (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                        && (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                    ClientUtil.showMessageWindow("Stock transfer it cannot be done between same branch.Change the From/To BranchID.");
                    return;
                }
                if (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)) {
                    lblAcHd.setText("Credit GL A/C Head");
                } else {
                    lblAcHd.setText("Debit GL A/C Head");
                }
            } else {
                ClientUtil.showMessageWindow("Enter your BranchID for From BranchID or To BranchID.");
                txtFromBranch.setText("");
                txtToBranch.setText("");
                return;
            }
        }else{
            ClientUtil.showMessageWindow("Select From Branch ID:");
            return;
        }
    }//GEN-LAST:event_txtToBranchFocusLost

    private void btnToBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToBranchActionPerformed
        // TODO add your handling code here:
        if (txtFromBranch.getText().equals("")) {
            ClientUtil.showMessageWindow("Please Select From Branch..");
            return;
        }else{
            callView("TO_BRANCH");
        }
    }//GEN-LAST:event_btnToBranchActionPerformed

    private void txtFromBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromBranchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromBranchActionPerformed

    private void txtFromBranchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromBranchFocusLost
        // TODO add your handling code here:
        if (txtFromBranch.getText().length() > 0) {
            HashMap hmap = new HashMap();
            hmap.put("BRANCH_CODE", txtFromBranch.getText());
            List lst = ClientUtil.executeQuery("getCityForDD", hmap);
            if (lst != null && lst.size() > 0) {
                hmap = (HashMap) lst.get(0);
                lblfromBranchName.setText(CommonUtil.convertObjToStr(hmap.get("BRANCH_NAME")));
            } else {
                ClientUtil.displayAlert("Invalid branch code");
                txtFromBranch.setText("");
                lblfromBranchName.setText("Branch Name");
            }
        }
        if (txtFromBranch.getText().length() > 0 && txtToBranch.getText().length() > 0) {
            if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                    || (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                if ((CommonUtil.convertObjToStr(txtFromBranch.getText()).equals(TrueTransactMain.BRANCH_ID))
                        && (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID))) {
                    ClientUtil.showMessageWindow("Stock transfer it cannot be done between same branch.Change the From/To BranchID.");
                    return;
                }
                if (CommonUtil.convertObjToStr(txtToBranch.getText()).equals(TrueTransactMain.BRANCH_ID)) {
                    lblAcHd.setText("Credit GL A/C Head");
                } else {
                    lblAcHd.setText("Debit GL A/C Head");
                }
            } else {
                ClientUtil.showMessageWindow("Enter your BranchID for From BranchID or To BranchID.");
                txtFromBranch.setText("");
                txtToBranch.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtFromBranchFocusLost

    private void btnFromBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromBranchActionPerformed
        // TODO add your handling code here:
        callView("FROM_BRANCH");
    }//GEN-LAST:event_btnFromBranchActionPerformed

    private void txtStockIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockIDActionPerformed

    private void txtStockIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockIDFocusLost

    private void btnStockIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockIDActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtProdID).length() > 0) {
            callView("STOCK_ID");
        }
    }//GEN-LAST:event_btnStockIDActionPerformed

    private void txtProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdIDActionPerformed

    private void txtProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProdIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdIDFocusLost

    private void btnProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIDActionPerformed
        // TODO add your handling code here:
        callView("PROD_ID");
    }//GEN-LAST:event_btnProdIDActionPerformed

    private void txtSalesPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesPriceActionPerformed

    private void txtSalesPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesPriceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesPriceFocusLost

    private void txtUnitTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnitTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitTypeActionPerformed

    private void txtUnitTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnitTypeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitTypeFocusLost

    private void txtTransferQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransferQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTransferQtyActionPerformed

    private void txtTransferQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransferQtyFocusLost
        // TODO add your handling code here:
        if (txtTransferQty.getText().length() > 0 && txtAvailQty.getText().length() > 0) {
            if (CommonUtil.convertObjToInt(txtTransferQty.getText()) > CommonUtil.convertObjToInt(txtAvailQty.getText())) {
                ClientUtil.showMessageWindow("Transfer Qty Should not be greater than available Qty");
                txtTransferQty.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtTransferQtyFocusLost

    private void txtAvailQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAvailQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAvailQtyActionPerformed

    private void txtAvailQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAvailQtyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAvailQtyFocusLost

    private void txtPurchPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchPriceActionPerformed

    private void txtPurchPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchPriceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchPriceFocusLost

    private void btnTransNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransNewActionPerformed
        // TODO add your handling code here:
        btnAcHd.setEnabled(true);
        btnProdID.setEnabled(true);
        btnStockID.setEnabled(true);
        txtTransferQty.setEnabled(true);
        btnTransNew.setEnabled(false);
        btnTransSave.setEnabled(true);
        observable.setNewData(true);
    }//GEN-LAST:event_btnTransNewActionPerformed

    private void btnTransSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtFromBranch.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("From Branch Should not be empty");
            return;
        } else if (CommonUtil.convertObjToStr(txtToBranch.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("To Branch Should not be empty");
            return;
        }else if (CommonUtil.convertObjToStr(txtAcHd.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("Account Head Should not be empty");
            return;
        }else if (CommonUtil.convertObjToStr(txtProdID.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("Product ID Should not be empty");
            return;
        }else if (CommonUtil.convertObjToStr(txtStockID.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("Stock ID Should not be empty");
            return;
        }else if (CommonUtil.convertObjToStr(txtTransferQty.getText()).length() <= 0) {
            ClientUtil.showMessageWindow("Transfer Qty Should not be empty");
            return;
        } else {
            updateOBFields();
            double totAmt = 0.0;
            HashMap stockMap = new HashMap();
            int transQty = CommonUtil.convertObjToInt(txtTransferQty.getText());
            double purchPrice = CommonUtil.convertObjToDouble(txtPurchPrice.getText());
            totAmt = transQty * purchPrice;
            stockMap.put("TOTAL_AMT", totAmt);
            observable.addDataToTransferDetailsTable(updateTab, updateMode, stockMap);
            totalAmountCalc();
            totalTransferQty();
            txtFromBranch.setEnabled(false);
            txtToBranch.setEnabled(false);
            btnFromBranch.setEnabled(false);
            btnToBranch.setEnabled(false);
            btnProdID.setEnabled(false);
            btnStockID.setEnabled(false);
            btnTransNew.setEnabled(true);
            btnTransSave.setEnabled(false);
            //setSizeTableData();
            observable.resetForm();
            resetForm();
            updateMode = false;
        }
    }//GEN-LAST:event_btnTransSaveActionPerformed

    private void btnTransDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransDelActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblTransferDetails.getValueAt(tblTransferDetails.getSelectedRow(), 0));
        observable.deleteTransDetails(st, tblTransferDetails.getSelectedRow());
        tblTransferDetails.setModel(observable.getTblTransferDetails());
        observable.resetForm();
        resetForm();
        totalAmountCalc();
        totalTransferQty();
        btnProdID.setEnabled(false);
        btnStockID.setEnabled(false);
        txtTransferQty.setEnabled(false);
        btnTransNew.setEnabled(true);
        btnTransDel.setEnabled(false);
    }//GEN-LAST:event_btnTransDelActionPerformed

    private void txtTotalTransAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalTransAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalTransAmtActionPerformed

    private void txtTotalTransAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalTransAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalTransAmtFocusLost

    private void txtTotalTransQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalTransQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalTransQtyActionPerformed

    private void txtTotalTransQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalTransQtyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalTransQtyFocusLost

    private void tblTransferDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransferDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransferDetailsMouseClicked

    private void tblTransferDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransferDetailsMouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tblTransferDetailsMouseReleased

    private void tblTransferDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblTransferDetailsFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_tblTransferDetailsFocusLost

    private void tblTransferDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTransferDetailsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransferDetailsKeyReleased

    private void tblTransferDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransferDetailsMousePressed
        // TODO add your handling code here:
        if (tblTransferDetails.getRowCount() > 0) {
            btnTransNew.setEnabled(true);
            updateMode = true;
            updateTab = tblTransferDetails.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblTransferDetails.getValueAt(tblTransferDetails.getSelectedRow(), 0));
            observable.populateTransTableDetails(st);
            update();
            btnTransDel.setEnabled(true);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                btnTransNew.setEnabled(false);
                btnTransSave.setEnabled(false);
                btnTransDel.setEnabled(false);
            }else{
               btnProdID.setEnabled(true);
               btnStockID.setEnabled(true);
               txtTransferQty.setEnabled(true);
               btnTransSave.setEnabled(true);
            } 
        }
    }//GEN-LAST:event_tblTransferDetailsMousePressed
    private double totalAmountCalc() {
        double totAmt = 0.0;
        String Amt = "";
        if (tblTransferDetails.getRowCount() > 0) {
           for (int i = 0; i < tblTransferDetails.getRowCount(); i++) {
                Amt = CommonUtil.convertObjToStr(tblTransferDetails.getValueAt(i, 9));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
           txtTotalTransAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }
        return totAmt;
    }
    
    private double totalTransferQty() {
        int totQty = 0;
        int  qty = 0;
        if (tblTransferDetails.getRowCount() > 0) {
           for (int i = 0; i < tblTransferDetails.getRowCount(); i++) {
                qty = CommonUtil.convertObjToInt(tblTransferDetails.getValueAt(i, 8));
                totQty = totQty + CommonUtil.convertObjToInt(qty);
            }
           txtTotalTransQty.setText(CommonUtil.convertObjToStr(new Double(totQty)));
        }
        return totQty;
    }
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("TRANSFER_ID", lblTransferIDVal.getText());
            singleAuthorizeMap.put("TOTAL_AMOUNT", txtTotalTransAmt.getText());
            singleAuthorizeMap.put("AC_HD", txtAcHd.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if (tblTransferDetails.getRowCount() > 0 && CommonUtil.convertObjToDouble(txtTotalTransAmt.getText()) > 0) {
                authorize(authorizeMap);
            }
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getEditTransferDetails");
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
            observable.setAuthorizeMap(map);
            updateOBFields();
            observable.doAction();
            setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcHd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromBranch;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdID;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStockID;
    private com.see.truetransact.uicomponent.CButton btnToBranch;
    private com.see.truetransact.uicomponent.CButton btnTransDel;
    private com.see.truetransact.uicomponent.CButton btnTransNew;
    private com.see.truetransact.uicomponent.CButton btnTransSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbStockID;
    private com.see.truetransact.uicomponent.CLabel lblAcHd;
    private com.see.truetransact.uicomponent.CLabel lblAcHdVal;
    private com.see.truetransact.uicomponent.CLabel lblAvailQty;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblFromBranch;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProdName;
    private com.see.truetransact.uicomponent.CLabel lblPurchPrice;
    private com.see.truetransact.uicomponent.CLabel lblSalesPrice;
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
    private com.see.truetransact.uicomponent.CLabel lblToBranch;
    private com.see.truetransact.uicomponent.CLabel lblToBranchName;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransQty;
    private com.see.truetransact.uicomponent.CLabel lblTransferID;
    private com.see.truetransact.uicomponent.CLabel lblTransferIDVal;
    private com.see.truetransact.uicomponent.CLabel lblTransferQty;
    private com.see.truetransact.uicomponent.CLabel lblUnitType;
    private com.see.truetransact.uicomponent.CLabel lblfromBranchName;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcHd;
    private com.see.truetransact.uicomponent.CPanel panAcHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panBranchDetails;
    private com.see.truetransact.uicomponent.CPanel panFromBranch;
    private com.see.truetransact.uicomponent.CPanel panProdID;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductRateDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStockID;
    private com.see.truetransact.uicomponent.CPanel panToBranch;
    private com.see.truetransact.uicomponent.CPanel panTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panTransfer;
    private com.see.truetransact.uicomponent.CPanel panTransferProductDetails;
    private com.see.truetransact.uicomponent.CPanel panTransferTableDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTransferDetails;
    private com.see.truetransact.uicomponent.CTable tblTransferDetails;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextField txtAcHd;
    private com.see.truetransact.uicomponent.CTextField txtAvailQty;
    private com.see.truetransact.uicomponent.CTextField txtFromBranch;
    private com.see.truetransact.uicomponent.CTextField txtProdID;
    private com.see.truetransact.uicomponent.CTextField txtPurchPrice;
    private com.see.truetransact.uicomponent.CTextField txtSalesPrice;
    private com.see.truetransact.uicomponent.CTextField txtStockID;
    private com.see.truetransact.uicomponent.CTextField txtToBranch;
    private com.see.truetransact.uicomponent.CTextField txtTotalTransAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalTransQty;
    private com.see.truetransact.uicomponent.CTextField txtTransferQty;
    private com.see.truetransact.uicomponent.CTextField txtUnitType;
    // End of variables declaration//GEN-END:variables
    
}
