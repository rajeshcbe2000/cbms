/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingStockUI.java
 *
 * Created on April 12, 2016, 2:59 PM
 */

package com.see.truetransact.ui.trading.tradingstock;

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
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingStockUI extends CInternalFrame implements UIMandatoryField,Observer{
    
    
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.shopmaster.ShopMasterRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingStockMRB objMandatoryRB = new TradingStockMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingStockOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    int updateTab = -1;
    private boolean updateMode = false;
    int pan = -1;
    int panEditDelete = -1;
    private int EDITPVSTOCK = 2,EDITDEFICITSTOCK = 3,EDITSTOCK = 1;
    private int DELETEPVSTOCK = 1,DELETERESTORESTOCK = 2;
    private int PVSTOCK = 2, DEFICITSTOCK = 3,RESTORESTOCK = 4;
    boolean isFilled = false;
    Date currDt = null;
    /** Creates new form TDSConfigUI */
    public TradingStockUI() {
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
        setSizeTableData();
        setObservable();
        initComponentData();
        observable.resetForm();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panTradingStock, false);
        setButtonEnableDisable();
        btnSearch.setEnabled(false);
        btnFind.setEnabled(false);
        btnPVDel.setEnabled(true);
        btnPVNew.setEnabled(true);
        btnPVSave.setEnabled(true);
        btnPVNew.setEnabled(false);
        btnPVDel.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnView.setEnabled(false);
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
        panTradingStock.setName("panTDS");
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
//        tdtStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtStartDate"));
//        tdtEndDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtEndDate"));
//        txtTdsId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTdsId"));
//        txtCutOfAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCutOfAmount"));
//        cboScope.setHelpMessage(lblMsg, objMandatoryRB.getString("cboScope"));
//         cboCustTypeVal.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustTypeVal"));
//        txtPercentage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPercentage"));
//        rdoCutOff_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCutOff_Yes"));
    }
    
    /** This method sets the Maximum allowed lenght to the textfields **/
    private void setMaxLengths() {
        txtPVQty.setValidation(new NumericValidation());
        txtRemarks.setAllowAll(true);
        txtTotalAmt.setValidation(new CurrencyValidation(14, 2));
    }
    
    /**  This method is to add this class as an Observer to an Observable **/
    private void setObservable(){
        try{
            observable =TradingStockOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try{
           tblStock.setModel(observable.getTblStockDetails());
           tblPVStock.setModel(observable.getTblPVDetails());
           tblPVStockDetails.setModel(observable.getTblPVStockDetails());
           tblDeficitStockDetails.setModel(observable.getTblDeficitStockDetails());
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
    
    private void setButtonDisable(boolean flag) {
        btnEdit.setEnabled(flag);
        btnDelete.setEnabled(flag);
        btnSave.setEnabled(flag);
        btnView.setEnabled(flag);
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setPvID(CommonUtil.convertObjToStr(lblPVIDVal.getText()));
        observable.setProdName(CommonUtil.convertObjToStr(txtProductName.getText()));
        observable.setStockID(CommonUtil.convertObjToStr(txtStockId.getText()));
        observable.setUnitType(CommonUtil.convertObjToStr(txtUnitType.getText()));
        observable.setPurchPrice(CommonUtil.convertObjToStr(txtPurchasePrice.getText()));
        observable.setMrp(CommonUtil.convertObjToStr(txtMRP.getText()));
        observable.setSalesPrice(CommonUtil.convertObjToStr(txtSalesPrice.getText()));
        observable.setAvaiQty(CommonUtil.convertObjToStr(txtAvailQty.getText()));
        observable.setPhyQty(CommonUtil.convertObjToStr(txtPVQty.getText()));
        observable.setRemarks(CommonUtil.convertObjToStr(txtRemarks.getText()));
        observable.setPvDate(CommonUtil.convertObjToStr(tdtPVDt.getDateValue()));
       // observable.setTotAmt(CommonUtil.convertObjToStr(txtTotalAmt.getText()));
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtProductName.setText(observable.getProdName());
        txtStockId.setText(observable.getStockID());
        txtUnitType.setText(observable.getUnitType());
        txtPurchasePrice.setText(observable.getPurchPrice());
        txtMRP.setText(observable.getMrp());
        txtSalesPrice.setText(observable.getSalesPrice());
        txtPVQty.setText(observable.getPhyQty());
        txtAvailQty.setText(observable.getAvaiQty());
        txtRemarks.setText(observable.getRemarks());
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("FROM_PROD_ID") || currField.equalsIgnoreCase("TO_PROD_ID")
                || currField.equalsIgnoreCase("PROD_NAME") || currField.equalsIgnoreCase("PROD ID")) {
            HashMap map = new HashMap();
            if (viewType.equalsIgnoreCase("TO_PROD_ID")) {
                map.put("TO_PROD_ID", "TO_PROD_ID");
                map.put("PRODUCT_ID", CommonUtil.convertObjToStr(txtFromProdID.getText()));
            } else if (viewType.equalsIgnoreCase("PROD_NAME")) {
                if (CommonUtil.convertObjToStr(txtProductName.getText()).length() > 0) {
                    map.put("PRODUCT_NAME", txtProductName.getText());
                }
            }
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingProductList");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("STOCK_ID")) {
            HashMap map = new HashMap();
            map.put("PRODUCT_ID", CommonUtil.convertObjToStr(observable.getProduct_ID()));
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingStockSingleList");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Edit")) {
            if (panEditDelete == EDITPVSTOCK) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getPvStockList");
            } 
            new ViewAll(this, viewMap).show();
        }else if(currField.equalsIgnoreCase("Enquiry")) {
            if (panEditDelete == EDITPVSTOCK) {
                HashMap map = new HashMap();
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, map);
                viewMap.put(CommonConstants.MAP_NAME, "getPvStockListForView");
            } 
            new ViewAll(this, viewMap).show();
        }
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (viewType.equalsIgnoreCase("FROM_PROD_ID")) {
            txtFromProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
            btnSearch.setEnabled(true);
        } else if (viewType.equalsIgnoreCase("TO_PROD_ID")) {
            txtToProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
            btnSearch.setEnabled(true);
        }else if (viewType.equalsIgnoreCase("PROD_NAME")) {
            resetForm();
            txtProductName.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_NAME")));
            observable.setProduct_ID(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
        }else if (viewType.equalsIgnoreCase("STOCK_ID")) {
            txtStockId.setText(CommonUtil.convertObjToStr(hash.get("STOCK_ID")));
            txtUnitType.setText(CommonUtil.convertObjToStr(hash.get("TYPE")));
            txtPurchasePrice.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_PRICE")));
            txtMRP.setText(CommonUtil.convertObjToStr(hash.get("MRP")));
            txtSalesPrice.setText(CommonUtil.convertObjToStr(hash.get("SALES_PRICE")));
            txtAvailQty.setText(CommonUtil.convertObjToStr(hash.get("QTY")));
            txtPVQty.setText("");
            txtRemarks.setText("");
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            if(panPhysicalVerification.isShowing()==true){
                panEditDelete = PVSTOCK;
            }
            setButtonEnableDisable();
            observable.getData(hash,panEditDelete);
            totalAmountCalc();
            setSizeTableData();
            lblPVIDVal.setText(observable.getPvID());
            tdtPVDt.setDateValue(observable.getPvDate());
            tdtPVDt.setEnabled(true);
//            txtProductName.setEnabled(true);
//            btnStockId.setEnabled(true);
//            btnProductName.setEnabled(true);
//            txtPVQty.setEnabled(true);
//            txtRemarks.setEnabled(true);
            btnPVDel.setEnabled(false);
            btnPVSave.setEnabled(false);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                btnPVNew.setEnabled(false);
            }
//            txtDamageQty.setEnabled(true);
//            btnDamageNew.setEnabled(true);
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            HashMap whereMap = new HashMap();
            if(panPhysicalVerification.isShowing()==true){
                panEditDelete = PVSTOCK;
            }
            setButtonEnableDisable();
            observable.getData(hash,panEditDelete);
            totalAmountCalc();
            setSizeTableData();
            btnPVNew.setEnabled(false);
            btnPVSave.setEnabled(false);
            btnPVDel.setEnabled(false);
            lblPVIDVal.setText(observable.getPvID());
            tdtPVDt.setDateValue(observable.getPvDate());
        }
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
    
    private void saveAction(String status){
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                if (observable.getProxyReturnMap().containsKey("PV_ID")) {
                    ClientUtil.showMessageWindow("Physical Verification ID : " + observable.getProxyReturnMap().get("PV_ID"));
                }
            }
        }
        btnCancelActionPerformed(null);
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTradingStock, false);
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
    
    
    
    /** Checks up whether the userentered Date already exists in the DB if so , giving alertmessage and clearing up the DateField, so that user can make other entry **/
    public void checkStartDate(String startDate)throws Exception{
        ArrayList list = observable.getResultList();
        Date startDt = DateUtil.getDateMMDDYYYY(startDate);
        String startDateMsg = "startDateMsg";
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                HashMap map = (HashMap) list.get(i);
                Date dbStartDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("FIN_START_DT")));
                if(startDt.compareTo(dbStartDt)== 0){
                    showAlertWindow(startDateMsg);
                    break;
                }
            }
        }
    }
    
    /** Checks up whether the userentered Date already exists in the DB if so , giving alertmessage and clearing up the DateField, so that user can make other entry **/
    public void checkEndDate(String endDate)throws Exception{
        ArrayList list = observable.getResultList();
        Date endDt = DateUtil.getDateMMDDYYYY(endDate);
        String endDateMsg = "endDateMsg";
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                HashMap map = (HashMap) list.get(i);
                Date dbendDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("FIN_END_DT")));
                if(endDt.compareTo(dbendDt)== 0){
                    showAlertWindow(endDateMsg);
                    break;
                }
            }
        }
    }
    
    /** This will show the alertwindow **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
        panTradingStock = new com.see.truetransact.uicomponent.CPanel();
        tabStockList = new com.see.truetransact.uicomponent.CTabbedPane();
        panStock = new com.see.truetransact.uicomponent.CPanel();
        panProdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromProdID = new com.see.truetransact.uicomponent.CLabel();
        panFromProdID = new com.see.truetransact.uicomponent.CPanel();
        txtFromProdID = new com.see.truetransact.uicomponent.CTextField();
        btnFromProdID = new com.see.truetransact.uicomponent.CButton();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        lblToProdID = new com.see.truetransact.uicomponent.CLabel();
        panToProdID = new com.see.truetransact.uicomponent.CPanel();
        txtToProdID = new com.see.truetransact.uicomponent.CTextField();
        btnToProdID = new com.see.truetransact.uicomponent.CButton();
        panStockTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpStockTableDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblStock = new com.see.truetransact.uicomponent.CTable();
        panPhysicalVerification = new com.see.truetransact.uicomponent.CPanel();
        panPVDetails = new com.see.truetransact.uicomponent.CPanel();
        panFromProdID3 = new com.see.truetransact.uicomponent.CPanel();
        panToProdID3 = new com.see.truetransact.uicomponent.CPanel();
        lblPVDt = new com.see.truetransact.uicomponent.CLabel();
        tdtPVDt = new com.see.truetransact.uicomponent.CDateField();
        lblPVID = new com.see.truetransact.uicomponent.CLabel();
        lblPVIDVal = new com.see.truetransact.uicomponent.CLabel();
        panPVProdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalesPrice = new com.see.truetransact.uicomponent.CLabel();
        lblUnitType = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtSalesPrice = new com.see.truetransact.uicomponent.CTextField();
        lblMRP = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblPurchasePrice = new com.see.truetransact.uicomponent.CLabel();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        txtPurchasePrice = new com.see.truetransact.uicomponent.CTextField();
        txtAvailQty = new com.see.truetransact.uicomponent.CTextField();
        txtMRP = new com.see.truetransact.uicomponent.CTextField();
        lblAvailQty = new com.see.truetransact.uicomponent.CLabel();
        lblPVQty = new com.see.truetransact.uicomponent.CLabel();
        txtPVQty = new com.see.truetransact.uicomponent.CTextField();
        panProductName = new com.see.truetransact.uicomponent.CPanel();
        txtProductName = new com.see.truetransact.uicomponent.CTextField();
        btnProductName = new com.see.truetransact.uicomponent.CButton();
        txtUnitType = new com.see.truetransact.uicomponent.CTextField();
        panStockId = new com.see.truetransact.uicomponent.CPanel();
        txtStockId = new com.see.truetransact.uicomponent.CTextField();
        btnStockId = new com.see.truetransact.uicomponent.CButton();
        lblStockID = new com.see.truetransact.uicomponent.CLabel();
        panPVStockTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpPVStockTableDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblPVStock = new com.see.truetransact.uicomponent.CTable();
        panPVBtnDetails = new com.see.truetransact.uicomponent.CPanel();
        btnPVNew = new com.see.truetransact.uicomponent.CButton();
        btnPVSave = new com.see.truetransact.uicomponent.CButton();
        btnPVDel = new com.see.truetransact.uicomponent.CButton();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment3 = new com.see.truetransact.uicomponent.CLabel();
        panDeficitStock = new com.see.truetransact.uicomponent.CPanel();
        panDate = new com.see.truetransact.uicomponent.CPanel();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        btnFind = new com.see.truetransact.uicomponent.CButton();
        btnTblClear = new com.see.truetransact.uicomponent.CButton();
        panPVStockDetails = new com.see.truetransact.uicomponent.CPanel();
        srpPVStockDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblPVStockDetails = new com.see.truetransact.uicomponent.CTable();
        panDeficitStockDetails = new com.see.truetransact.uicomponent.CPanel();
        srpDeficitStockDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblDeficitStockDetails = new com.see.truetransact.uicomponent.CTable();
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
        setMinimumSize(new java.awt.Dimension(950, 600));
        setPreferredSize(new java.awt.Dimension(950, 600));

        panTradingStock.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTradingStock.setMinimumSize(new java.awt.Dimension(900, 400));
        panTradingStock.setPreferredSize(new java.awt.Dimension(900, 400));
        panTradingStock.setLayout(new java.awt.GridBagLayout());

        tabStockList.setMinimumSize(new java.awt.Dimension(930, 450));
        tabStockList.setPreferredSize(new java.awt.Dimension(930, 450));

        panStock.setMaximumSize(new java.awt.Dimension(750, 400));
        panStock.setMinimumSize(new java.awt.Dimension(750, 400));
        panStock.setPreferredSize(new java.awt.Dimension(750, 400));
        panStock.setLayout(new java.awt.GridBagLayout());

        panProdDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProdDetails.setMinimumSize(new java.awt.Dimension(860, 50));
        panProdDetails.setPreferredSize(new java.awt.Dimension(860, 50));
        panProdDetails.setLayout(new java.awt.GridBagLayout());

        lblFromProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromProdID.setText("From Prod ID");
        lblFromProdID.setMaximumSize(new java.awt.Dimension(80, 18));
        lblFromProdID.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFromProdID.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdDetails.add(lblFromProdID, gridBagConstraints);

        panFromProdID.setLayout(new java.awt.GridBagLayout());

        txtFromProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromProdID.add(txtFromProdID, gridBagConstraints);

        btnFromProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromProdID.setEnabled(false);
        btnFromProdID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnFromProdID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromProdID.add(btnFromProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdDetails.add(panFromProdID, gridBagConstraints);

        btnSearch.setForeground(new java.awt.Color(0, 153, 51));
        btnSearch.setText("SEARCH");
        btnSearch.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panProdDetails.add(btnSearch, gridBagConstraints);

        lblToProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToProdID.setText("To Prod ID");
        lblToProdID.setMaximumSize(new java.awt.Dimension(70, 18));
        lblToProdID.setMinimumSize(new java.awt.Dimension(70, 18));
        lblToProdID.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdDetails.add(lblToProdID, gridBagConstraints);

        panToProdID.setLayout(new java.awt.GridBagLayout());

        txtToProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToProdID.add(txtToProdID, gridBagConstraints);

        btnToProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToProdID.setEnabled(false);
        btnToProdID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnToProdID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToProdID.add(btnToProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdDetails.add(panToProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panStock.add(panProdDetails, gridBagConstraints);

        panStockTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock Table Details"));
        panStockTableDetails.setMaximumSize(new java.awt.Dimension(770, 350));
        panStockTableDetails.setMinimumSize(new java.awt.Dimension(770, 350));
        panStockTableDetails.setPreferredSize(new java.awt.Dimension(870, 350));
        panStockTableDetails.setLayout(new java.awt.GridBagLayout());

        srpStockTableDetails.setMaximumSize(new java.awt.Dimension(850, 320));
        srpStockTableDetails.setMinimumSize(new java.awt.Dimension(850, 320));
        srpStockTableDetails.setPreferredSize(new java.awt.Dimension(850, 320));
        srpStockTableDetails.setRequestFocusEnabled(false);

        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Prod ID", "Prod Name", "Type", "Stock ID", "Stock Qty", "Purchase Price", "MRP", "Sales Price"
            }
        ));
        tblStock.setMinimumSize(new java.awt.Dimension(400, 700));
        tblStock.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblStock.setPreferredSize(new java.awt.Dimension(400, 2000));
        tblStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblStockMousePressed(evt);
            }
        });
        tblStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblStockKeyReleased(evt);
            }
        });
        srpStockTableDetails.setViewportView(tblStock);

        panStockTableDetails.add(srpStockTableDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panStock.add(panStockTableDetails, gridBagConstraints);

        tabStockList.addTab("Stock List", panStock);

        panPhysicalVerification.setLayout(new java.awt.GridBagLayout());

        panPVDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPVDetails.setMinimumSize(new java.awt.Dimension(800, 50));
        panPVDetails.setPreferredSize(new java.awt.Dimension(800, 50));
        panPVDetails.setLayout(new java.awt.GridBagLayout());

        panFromProdID3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPVDetails.add(panFromProdID3, gridBagConstraints);

        panToProdID3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPVDetails.add(panToProdID3, gridBagConstraints);

        lblPVDt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPVDt.setText("PHYSICAL VERIFICATION OF ITEMS ON");
        lblPVDt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblPVDt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblPVDt.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVDetails.add(lblPVDt, gridBagConstraints);

        tdtPVDt.setEnabled(false);
        tdtPVDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPVDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVDetails.add(tdtPVDt, gridBagConstraints);

        lblPVID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPVID.setText("PV ID :");
        lblPVID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPVID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblPVID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblPVID.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 26, 4, 4);
        panPVDetails.add(lblPVID, gridBagConstraints);

        lblPVIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblPVIDVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPVIDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPVIDVal.setMaximumSize(new java.awt.Dimension(10, 18));
        lblPVIDVal.setMinimumSize(new java.awt.Dimension(10, 18));
        lblPVIDVal.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panPVDetails.add(lblPVIDVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalVerification.add(panPVDetails, gridBagConstraints);

        panPVProdDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPVProdDetails.setMinimumSize(new java.awt.Dimension(800, 100));
        panPVProdDetails.setPreferredSize(new java.awt.Dimension(800, 100));
        panPVProdDetails.setLayout(new java.awt.GridBagLayout());

        lblSalesPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalesPrice.setText("Sales Price");
        lblSalesPrice.setMaximumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setMinimumSize(new java.awt.Dimension(70, 18));
        lblSalesPrice.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblSalesPrice, gridBagConstraints);

        lblUnitType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnitType.setText("Unit Type");
        lblUnitType.setMaximumSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblUnitType, gridBagConstraints);

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtRemarks.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtRemarks, gridBagConstraints);

        txtSalesPrice.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSalesPrice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtSalesPrice, gridBagConstraints);

        lblMRP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMRP.setText("MRP");
        lblMRP.setMaximumSize(new java.awt.Dimension(70, 18));
        lblMRP.setMinimumSize(new java.awt.Dimension(35, 18));
        lblMRP.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblMRP, gridBagConstraints);

        lblRemarks.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRemarks.setText("Remarks");
        lblRemarks.setMaximumSize(new java.awt.Dimension(70, 18));
        lblRemarks.setMinimumSize(new java.awt.Dimension(55, 18));
        lblRemarks.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblRemarks, gridBagConstraints);

        lblPurchasePrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPurchasePrice.setText("Purchase Price");
        lblPurchasePrice.setMaximumSize(new java.awt.Dimension(70, 18));
        lblPurchasePrice.setMinimumSize(new java.awt.Dimension(150, 18));
        lblPurchasePrice.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblPurchasePrice, gridBagConstraints);

        lblProductName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProductName.setText("Product Name");
        lblProductName.setMaximumSize(new java.awt.Dimension(70, 18));
        lblProductName.setMinimumSize(new java.awt.Dimension(85, 18));
        lblProductName.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblProductName, gridBagConstraints);

        txtPurchasePrice.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPurchasePrice.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPurchasePrice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtPurchasePrice, gridBagConstraints);

        txtAvailQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAvailQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAvailQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtAvailQty, gridBagConstraints);

        txtMRP.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMRP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtMRP, gridBagConstraints);

        lblAvailQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAvailQty.setText("Avail.Qty");
        lblAvailQty.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAvailQty.setMinimumSize(new java.awt.Dimension(60, 18));
        lblAvailQty.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblAvailQty, gridBagConstraints);

        lblPVQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPVQty.setText("Physical Qty");
        lblPVQty.setMaximumSize(new java.awt.Dimension(100, 18));
        lblPVQty.setMinimumSize(new java.awt.Dimension(100, 18));
        lblPVQty.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblPVQty, gridBagConstraints);

        txtPVQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPVQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPVQty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPVQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPVQtyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtPVQty, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(panProductName, gridBagConstraints);

        txtUnitType.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUnitType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtUnitType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(txtUnitType, gridBagConstraints);

        panStockId.setLayout(new java.awt.GridBagLayout());

        txtStockId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStockId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStockId.add(txtStockId, gridBagConstraints);

        btnStockId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStockId.setEnabled(false);
        btnStockId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnStockId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnStockId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStockId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStockId.add(btnStockId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(panStockId, gridBagConstraints);

        lblStockID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStockID.setText("Stock ID");
        lblStockID.setMaximumSize(new java.awt.Dimension(70, 18));
        lblStockID.setMinimumSize(new java.awt.Dimension(85, 18));
        lblStockID.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPVProdDetails.add(lblStockID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalVerification.add(panPVProdDetails, gridBagConstraints);

        panPVStockTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock Table Details"));
        panPVStockTableDetails.setMaximumSize(new java.awt.Dimension(810, 155));
        panPVStockTableDetails.setMinimumSize(new java.awt.Dimension(810, 155));
        panPVStockTableDetails.setPreferredSize(new java.awt.Dimension(810, 185));
        panPVStockTableDetails.setLayout(new java.awt.GridBagLayout());

        srpPVStockTableDetails.setMaximumSize(new java.awt.Dimension(790, 155));
        srpPVStockTableDetails.setMinimumSize(new java.awt.Dimension(790, 155));
        srpPVStockTableDetails.setPreferredSize(new java.awt.Dimension(790, 155));
        srpPVStockTableDetails.setRequestFocusEnabled(false);

        tblPVStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Prod ID", "Prod Name", "Type", "Stock ID", "Avail Qty", "Phy Qty", "Diff/Excess", "Purch Price", "MRP", "Sales Price", "Tot_Amt"
            }
        ));
        tblPVStock.setMinimumSize(new java.awt.Dimension(400, 700));
        tblPVStock.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblPVStock.setPreferredSize(new java.awt.Dimension(400, 700));
        tblPVStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPVStockMousePressed(evt);
            }
        });
        srpPVStockTableDetails.setViewportView(tblPVStock);

        panPVStockTableDetails.add(srpPVStockTableDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalVerification.add(panPVStockTableDetails, gridBagConstraints);

        panPVBtnDetails.setMaximumSize(new java.awt.Dimension(800, 30));
        panPVBtnDetails.setMinimumSize(new java.awt.Dimension(800, 30));
        panPVBtnDetails.setPreferredSize(new java.awt.Dimension(800, 30));
        panPVBtnDetails.setLayout(new java.awt.GridBagLayout());

        btnPVNew.setText("NEW");
        btnPVNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPVNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 70, 2, 2);
        panPVBtnDetails.add(btnPVNew, gridBagConstraints);

        btnPVSave.setText("SAVE");
        btnPVSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPVSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPVBtnDetails.add(btnPVSave, gridBagConstraints);

        btnPVDel.setText("DELETE");
        btnPVDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPVDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPVBtnDetails.add(btnPVDel, gridBagConstraints);

        lblTotalAmt.setText("Total Deficit Amount :");
        lblTotalAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalAmt.setMaximumSize(new java.awt.Dimension(145, 18));
        lblTotalAmt.setMinimumSize(new java.awt.Dimension(145, 18));
        lblTotalAmt.setPreferredSize(new java.awt.Dimension(145, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panPVBtnDetails.add(lblTotalAmt, gridBagConstraints);

        txtTotalAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panPVBtnDetails.add(txtTotalAmt, gridBagConstraints);

        lblAllignment3.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment3.setMaximumSize(new java.awt.Dimension(50, 18));
        lblAllignment3.setMinimumSize(new java.awt.Dimension(50, 18));
        lblAllignment3.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panPVBtnDetails.add(lblAllignment3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalVerification.add(panPVBtnDetails, gridBagConstraints);

        tabStockList.addTab("Physical Verification", panPhysicalVerification);

        panDeficitStock.setLayout(new java.awt.GridBagLayout());

        panDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDate.setMinimumSize(new java.awt.Dimension(800, 30));
        panDate.setPreferredSize(new java.awt.Dimension(800, 30));
        panDate.setLayout(new java.awt.GridBagLayout());

        lblFromDt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDt.setText("From Date ");
        lblFromDt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblFromDt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFromDt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblFromDt, gridBagConstraints);

        tdtFromDt.setEnabled(false);
        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(tdtFromDt, gridBagConstraints);

        lblToDt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDt.setText("To Date ");
        lblToDt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblToDt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblToDt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblToDt, gridBagConstraints);

        tdtToDt.setEnabled(false);
        tdtToDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(tdtToDt, gridBagConstraints);

        btnFind.setForeground(new java.awt.Color(0, 153, 51));
        btnFind.setText("SEARCH");
        btnFind.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDate.add(btnFind, gridBagConstraints);

        btnTblClear.setForeground(new java.awt.Color(0, 153, 51));
        btnTblClear.setText("CLEAR");
        btnTblClear.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnTblClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTblClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        panDate.add(btnTblClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeficitStock.add(panDate, gridBagConstraints);

        panPVStockDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("PV List"));
        panPVStockDetails.setMinimumSize(new java.awt.Dimension(810, 155));
        panPVStockDetails.setPreferredSize(new java.awt.Dimension(810, 175));
        panPVStockDetails.setLayout(new java.awt.GridBagLayout());

        srpPVStockDetails.setMaximumSize(new java.awt.Dimension(790, 150));
        srpPVStockDetails.setMinimumSize(new java.awt.Dimension(790, 150));
        srpPVStockDetails.setPreferredSize(new java.awt.Dimension(790, 150));
        srpPVStockDetails.setRequestFocusEnabled(false);

        tblPVStockDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "PV ID", "PV Date", "Amount", "Tran ID"
            }
        ));
        tblPVStockDetails.setMinimumSize(new java.awt.Dimension(400, 700));
        tblPVStockDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblPVStockDetails.setPreferredSize(new java.awt.Dimension(400, 700));
        tblPVStockDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPVStockDetailsMousePressed(evt);
            }
        });
        tblPVStockDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPVStockDetailsKeyReleased(evt);
            }
        });
        srpPVStockDetails.setViewportView(tblPVStockDetails);

        panPVStockDetails.add(srpPVStockDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeficitStock.add(panPVStockDetails, gridBagConstraints);

        panDeficitStockDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock Table Details"));
        panDeficitStockDetails.setMaximumSize(new java.awt.Dimension(810, 155));
        panDeficitStockDetails.setMinimumSize(new java.awt.Dimension(810, 155));
        panDeficitStockDetails.setPreferredSize(new java.awt.Dimension(810, 185));
        panDeficitStockDetails.setLayout(new java.awt.GridBagLayout());

        srpDeficitStockDetails.setMaximumSize(new java.awt.Dimension(790, 155));
        srpDeficitStockDetails.setMinimumSize(new java.awt.Dimension(790, 155));
        srpDeficitStockDetails.setPreferredSize(new java.awt.Dimension(790, 155));
        srpDeficitStockDetails.setRequestFocusEnabled(false);

        tblDeficitStockDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Prod ID", "Prod Name", "Type", "Stock ID", "Avail Qty", "Phy Qty", "Diff/Excess", "Purch Price", "MRP", "Sales Price", "Tot_Amt"
            }
        ));
        tblDeficitStockDetails.setMinimumSize(new java.awt.Dimension(400, 700));
        tblDeficitStockDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblDeficitStockDetails.setPreferredSize(new java.awt.Dimension(400, 700));
        tblDeficitStockDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDeficitStockDetailsMousePressed(evt);
            }
        });
        tblDeficitStockDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDeficitStockDetailsKeyReleased(evt);
            }
        });
        srpDeficitStockDetails.setViewportView(tblDeficitStockDetails);

        panDeficitStockDetails.add(srpDeficitStockDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeficitStock.add(panDeficitStockDetails, gridBagConstraints);

        tabStockList.addTab("DeficitStock", panDeficitStock);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        panTradingStock.add(tabStockList, gridBagConstraints);

        getContentPane().add(panTradingStock, java.awt.BorderLayout.CENTER);

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
        if (panPhysicalVerification.isShowing() == true) {
            panEditDelete = EDITPVSTOCK;
            pan = PVSTOCK;
        } 
        callView("Enquiry");
        ClientUtil.enableDisable(panPhysicalVerification, false);
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
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("PV_ID", lblPVIDVal.getText());
            singleAuthorizeMap.put("TOT_AMT", txtTotalAmt.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
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
            mapParam.put(CommonConstants.MAP_NAME, "getPvStockList");
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
    
    private void setSizeTableData() {
        tblPVStock.getColumnModel().getColumn(0).setPreferredWidth(2);
        tblPVStock.getColumnModel().getColumn(1).setPreferredWidth(35);
        tblPVStock.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblPVStock.getColumnModel().getColumn(3).setPreferredWidth(33);
        tblPVStock.getColumnModel().getColumn(4).setPreferredWidth(35);
        tblPVStock.getColumnModel().getColumn(5).setPreferredWidth(30);
        tblPVStock.getColumnModel().getColumn(6).setPreferredWidth(40);
        tblPVStock.getColumnModel().getColumn(7).setPreferredWidth(37);
        tblPVStock.getColumnModel().getColumn(8).setPreferredWidth(35);
        tblPVStock.getColumnModel().getColumn(9).setPreferredWidth(20);
        tblPVStock.getColumnModel().getColumn(10).setPreferredWidth(30);
        tblPVStock.getColumnModel().getColumn(11).setPreferredWidth(25);
    }
    
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        observable.resetMap();
        observable.resetTblDetails();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTradingStock, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        if (panPhysicalVerification.isShowing() == true) {
            setButtonEnableDisable();
        }else{
            btnCancel.setEnabled(false);
            btnNew.setEnabled(true);
        }
        btnFromProdID.setEnabled(false);
        btnToProdID.setEnabled(false);
        btnSearch.setEnabled(false);
        btnProductName.setEnabled(false);
        btnStockId.setEnabled(false);
        btnPVSave.setEnabled(false);
        btnPVNew.setEnabled(false);
        lblPVIDVal.setText("");
        viewType = "";
        //__ Make the Screen Closable..
        setModified(false);
         btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(tdtPVDt.getDateValue()).length() > 0) {
            if (tblPVStock.getRowCount() > 0) {
                savePerformed();
                btnReject.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnException.setEnabled(true);
            } else {
                ClientUtil.showMessageWindow("No Records For Physical Verification!!!");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("Enter Physical Verification Date");
            return;
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
        if (panPhysicalVerification.isShowing() == true) {
            panEditDelete = EDITPVSTOCK;
            pan = PVSTOCK;
        } 
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        if(panStock.isShowing()==true){
        ClientUtil.enableDisable(panStock, true);
        setButtonEnableDisable();
        setButtonDisable(false);
        txtFromProdID.setEnabled(false);
        txtToProdID.setEnabled(false);
        btnFromProdID.setEnabled(true);
        btnToProdID.setEnabled(true);
        btnSearch.setEnabled(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnView.setEnabled(false);
        }else if(panPhysicalVerification.isShowing()==true){
            btnPVNew.setEnabled(true);
            btnPVDel.setEnabled(false);
            btnPVSave.setEnabled(false);
            setButtonEnableDisable();
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
        }else if(panDeficitStock.isShowing()==true){
            tdtFromDt.setEnabled(true);
            tdtToDt.setEnabled(true);
            btnFind.setEnabled(true);
            setButtonEnableDisable();
            setButtonDisable(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        //__ To Save the data in the Internal Frame...
        setModified(true);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtFromProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromProdIDFocusLost
        // TODO add your handling code here:
        btnSearch.setEnabled(true);
    }//GEN-LAST:event_txtFromProdIDFocusLost

    private void btnFromProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromProdIDActionPerformed
        // TODO add your handling code here:
        callView("FROM_PROD_ID");
    }//GEN-LAST:event_btnFromProdIDActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here: 
        if(tblStock.getRowCount()>0){
            observable.resetForm();
        }
        if (txtFromProdID.getText().length() > 0 && txtToProdID.getText().length() > 0) {
            HashMap hash = new HashMap();
            hash.put("FROM_PROD_ID", CommonUtil.convertObjToStr(txtFromProdID.getText()));
            hash.put("TO_PROD_ID", CommonUtil.convertObjToStr(txtToProdID.getText()));
            if (panTradingStock.isShowing() == true) {
                panEditDelete = EDITSTOCK;
            }
            observable.getData(hash, panEditDelete);
            tblStock.setModel(observable.getTblStockDetails());
            if (tblStock.getRowCount() <= 0) {
                ClientUtil.showMessageWindow("No relavant data.");
                return;
            }
            btnSearch.setEnabled(false);
        } else {
            ClientUtil.showMessageWindow("Enter From/To Product ID");
            return;
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblStockMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblStockMousePressed

    private void tblStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblStockKeyReleased
        // TODO add your handling code here:
        int availQty = 0;
        int retQty = 0;
        double rate = 0.0;
        double returnTot = 0.0;
        if ((tblStock.getRowCount() > 0) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
        HashMap stockMap = new HashMap();
        retQty = CommonUtil.convertObjToInt(tblStock.getValueAt(tblStock.getSelectedRow(), 9));
        // stockMap.put("SALES_NO", txtSalesReturnSalNo.getText());
        stockMap.put("PRODUCT_NAME", CommonUtil.convertObjToStr(tblStock.getValueAt(tblStock.getSelectedRow(), 1)));
        stockMap.put("UNIT_TYPE", CommonUtil.convertObjToStr(tblStock.getValueAt(tblStock.getSelectedRow(), 3)));
        stockMap.put("RATE", CommonUtil.convertObjToDouble(tblStock.getValueAt(tblStock.getSelectedRow(), 2)));
        }
    }//GEN-LAST:event_tblStockKeyReleased

    private void txtToProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToProdIDFocusLost
        // TODO add your handling code here:
        btnSearch.setEnabled(true);
    }//GEN-LAST:event_txtToProdIDFocusLost

    private void btnToProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToProdIDActionPerformed
        // TODO add your handling code here:
        if (txtFromProdID.getText().length() > 0) {
            callView("TO_PROD_ID");
        } else {
            ClientUtil.showMessageWindow("Please Select From Product ID...");
            return;
        }
    }//GEN-LAST:event_btnToProdIDActionPerformed

    private double totalAmountCalc() {
        double totAmt = 0.0;
        String Amt = "";
        if (tblPVStock.getRowCount() > 0) {
           for (int i = 0; i < tblPVStock.getRowCount(); i++) {
                Amt = CommonUtil.convertObjToStr(tblPVStock.getValueAt(i, 11));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
           txtTotalAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }
        return totAmt;
    }
    
    private void tdtPVDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPVDtFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tdtPVDt.getDateValue().length() > 0) {
                Date stmtDt = (DateUtil.getDateMMDDYYYY(tdtPVDt.getDateValue()));
                if (DateUtil.dateDiff(currDt, stmtDt) > 0) {
                    ClientUtil.showMessageWindow("Physical Verification Date Should not be greater than Current Date!!!");
                    return;
                }
                HashMap stmtMap =  new HashMap();
                List stmtLst = ClientUtil.executeQuery("checkPvAuthStatus", stmtMap);
                if (stmtLst != null && stmtLst.size() > 0) {
                    ClientUtil.showMessageWindow("Previous Physical Verification Stock,Pending Authorization");
                    btnCancelActionPerformed(null);
                    return;
                }
            }
        }
    }//GEN-LAST:event_tdtPVDtFocusLost

    private void txtPVQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPVQtyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPVQtyFocusLost

    private void txtProductNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameFocusLost

    private void btnProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductNameActionPerformed
        // TODO add your handling code here:
        callView("PROD_NAME");
    }//GEN-LAST:event_btnProductNameActionPerformed

    private void txtStockIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockIdFocusLost

    private void btnStockIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockIdActionPerformed
        // TODO add your handling code here:
         if(CommonUtil.convertObjToStr(observable.getProduct_ID()).length()>0){
            callView("STOCK_ID");
        }
    }//GEN-LAST:event_btnStockIdActionPerformed

    private void tblPVStockMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPVStockMousePressed
        // TODO add your handling code here:
         if (tblPVStock.getRowCount() > 0) {
            btnPVNew.setEnabled(true);
            updateMode = true;
            updateTab = tblPVStock.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblPVStock.getValueAt(tblPVStock.getSelectedRow(), 0));
            observable.populatePVTableDetails(st);
            btnPVDel.setEnabled(true);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                btnPVDel.setEnabled(false);
                btnPVNew.setEnabled(false);
                btnPVSave.setEnabled(false);
            }else{
               txtProductName.setEnabled(true);
               btnProductName.setEnabled(true);
               btnStockId.setEnabled(true);
               txtPVQty.setEnabled(true);
               txtRemarks.setEnabled(true);
               btnPVSave.setEnabled(true);
            } 
        }
    }//GEN-LAST:event_tblPVStockMousePressed

    private void btnPVNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPVNewActionPerformed
        // TODO add your handling code here:
        resetForm();
        HashMap stmtMap = new HashMap();
        btnPVNew.setEnabled(false);
        btnPVSave.setEnabled(true);
        txtProductName.setEnabled(true);
        btnProductName.setEnabled(true);
        btnStockId.setEnabled(true);
        txtPVQty.setEnabled(true);
        txtRemarks.setEnabled(true);
        tdtPVDt.setEnabled(true);
        observable.setNewData(true);
    }//GEN-LAST:event_btnPVNewActionPerformed

    private void btnPVSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPVSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtProductName.getText()).length() == 0) {
            ClientUtil.showMessageWindow("Product Name Should not be empty.");
            return;
        } else if (CommonUtil.convertObjToStr(txtStockId.getText()).length() == 0) {
            ClientUtil.showMessageWindow("Stock ID Should not be empty.");
            return;
        }else if (CommonUtil.convertObjToStr(txtPVQty.getText()).length() == 0) {
            ClientUtil.showMessageWindow("Physical Qty  Should not be empty.");
            return;
        } else {
            updateOBFields();
            HashMap stockMap = new HashMap();
            int availQty = CommonUtil.convertObjToInt(txtAvailQty.getText());
            int pvQty = CommonUtil.convertObjToInt(txtPVQty.getText());
            if (availQty < pvQty) {
                ClientUtil.showMessageWindow("Physical verification Qty should not be more than available stock");
                return;
            }
            int stockDiff = availQty - pvQty;
            stockMap.put("Stock_Diff", stockDiff);
            //observable.setNewData(true);
            observable.addDataToPVDetailsTable(updateTab, updateMode, stockMap);
            totalAmountCalc();
            setSizeTableData();
            observable.resetForm();
            resetForm();
            updateMode = false;
        }
    }//GEN-LAST:event_btnPVSaveActionPerformed

    private void btnPVDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPVDelActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblPVStock.getValueAt(tblPVStock.getSelectedRow(), 0));
        observable.deletePVDetails(st, tblPVStock.getSelectedRow());
        tblPVStock.setModel(observable.getTblPVDetails());
        observable.resetForm();
        resetForm();
        txtProductName.setEnabled(false);
        btnProductName.setEnabled(false);
        btnStockId.setEnabled(false);
        txtPVQty.setEnabled(false);
        txtRemarks.setEnabled(false);
        btnPVNew.setEnabled(true);
        btnPVDel.setEnabled(false);
    }//GEN-LAST:event_btnPVDelActionPerformed

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDtFocusLost

    private void tblDeficitStockDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDeficitStockDetailsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDeficitStockDetailsMousePressed

    private void tblDeficitStockDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDeficitStockDetailsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDeficitStockDetailsKeyReleased

    private void tdtToDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtToDtFocusLost

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:
        if(tblPVStockDetails.getRowCount()>0){
            observable.resetForm();
        }
        if (tdtFromDt.getDateValue().length() > 0 && tdtToDt.getDateValue().length() > 0) {
            Date fromDt = (DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
            Date toDt = (DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue()));
            if (DateUtil.dateDiff(toDt, fromDt) > 0) {
                ClientUtil.showMessageWindow("From Date should not be greater than To Date!!!");
                tdtFromDt.setDateValue("");
                tdtToDt.setDateValue("");
                return;
            }
            HashMap hash = new HashMap();
            hash.put("FROM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDt.getDateValue())));
            hash.put("TO_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDt.getDateValue())));
            if(panDeficitStock.isShowing()==true){
                panEditDelete = DEFICITSTOCK;
            }
            observable.setDeficitStockMap(null);
            observable.getData(hash,panEditDelete);
            tblPVStockDetails.setModel(observable.getTblPVStockDetails());
            if (tblPVStockDetails.getRowCount() <= 0) {
                ClientUtil.showMessageWindow("No relavant data.");
                return;
            }
            btnFind.setEnabled(false);
            btnTblClear.setEnabled(true);
            tdtFromDt.setEnabled(false);
            tdtToDt.setEnabled(false);
        } else {
            ClientUtil.showMessageWindow("Enter From/To Date");
            return;
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private void tblPVStockDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPVStockDetailsMousePressed
        // TODO add your handling code here:
        if (tblPVStockDetails.getRowCount() > 0) {
            String pvId = CommonUtil.convertObjToStr(tblPVStockDetails.getValueAt(tblPVStockDetails.getSelectedRow(), 1));
            observable.resetDeficitStockTblDetails();
            tblDeficitStockDetails.setModel(observable.getTblDeficitStockDetails());
            observable.displayDeficitStock(pvId);
            tblDeficitStockDetails.setModel(observable.getTblDeficitStockDetails());
            setSizeTableData();
        }
    }//GEN-LAST:event_tblPVStockDetailsMousePressed

    private void tblPVStockDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPVStockDetailsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPVStockDetailsKeyReleased

    private void btnTblClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTblClearActionPerformed
        // TODO add your handling code here:
        observable.resetTblDetails();
        tdtFromDt.setEnabled(true);
        tdtToDt.setEnabled(true);
        btnFind.setEnabled(true);
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        btnTblClear.setEnabled(false);
    }//GEN-LAST:event_btnTblClearActionPerformed
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
    
    public void resetForm() {
        txtProductName.setText("");
        txtStockId.setText("");
        txtUnitType.setText("");
        txtPurchasePrice.setText("");
        txtSalesPrice.setText("");
        txtMRP.setText("");
        txtAvailQty.setText("");
        txtPVQty.setText("");
        txtRemarks.setText("");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFind;
    private com.see.truetransact.uicomponent.CButton btnFromProdID;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPVDel;
    private com.see.truetransact.uicomponent.CButton btnPVNew;
    private com.see.truetransact.uicomponent.CButton btnPVSave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProductName;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnStockId;
    private com.see.truetransact.uicomponent.CButton btnTblClear;
    private com.see.truetransact.uicomponent.CButton btnToProdID;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAllignment3;
    private com.see.truetransact.uicomponent.CLabel lblAvailQty;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblFromProdID;
    private com.see.truetransact.uicomponent.CLabel lblMRP;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPVDt;
    private com.see.truetransact.uicomponent.CLabel lblPVID;
    private com.see.truetransact.uicomponent.CLabel lblPVIDVal;
    private com.see.truetransact.uicomponent.CLabel lblPVQty;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblPurchasePrice;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
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
    private com.see.truetransact.uicomponent.CLabel lblStockID;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CLabel lblToProdID;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblUnitType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDate;
    private com.see.truetransact.uicomponent.CPanel panDeficitStock;
    private com.see.truetransact.uicomponent.CPanel panDeficitStockDetails;
    private com.see.truetransact.uicomponent.CPanel panFromProdID;
    private com.see.truetransact.uicomponent.CPanel panFromProdID3;
    private com.see.truetransact.uicomponent.CPanel panPVBtnDetails;
    private com.see.truetransact.uicomponent.CPanel panPVDetails;
    private com.see.truetransact.uicomponent.CPanel panPVProdDetails;
    private com.see.truetransact.uicomponent.CPanel panPVStockDetails;
    private com.see.truetransact.uicomponent.CPanel panPVStockTableDetails;
    private com.see.truetransact.uicomponent.CPanel panPhysicalVerification;
    private com.see.truetransact.uicomponent.CPanel panProdDetails;
    private com.see.truetransact.uicomponent.CPanel panProductName;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStock;
    private com.see.truetransact.uicomponent.CPanel panStockId;
    private com.see.truetransact.uicomponent.CPanel panStockTableDetails;
    private com.see.truetransact.uicomponent.CPanel panToProdID;
    private com.see.truetransact.uicomponent.CPanel panToProdID3;
    private com.see.truetransact.uicomponent.CPanel panTradingStock;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpDeficitStockDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpPVStockDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpPVStockTableDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpStockTableDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabStockList;
    private com.see.truetransact.uicomponent.CTable tblDeficitStockDetails;
    private com.see.truetransact.uicomponent.CTable tblPVStock;
    private com.see.truetransact.uicomponent.CTable tblPVStockDetails;
    private com.see.truetransact.uicomponent.CTable tblStock;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtPVDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtAvailQty;
    private com.see.truetransact.uicomponent.CTextField txtFromProdID;
    private com.see.truetransact.uicomponent.CTextField txtMRP;
    private com.see.truetransact.uicomponent.CTextField txtPVQty;
    private com.see.truetransact.uicomponent.CTextField txtProductName;
    private com.see.truetransact.uicomponent.CTextField txtPurchasePrice;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSalesPrice;
    private com.see.truetransact.uicomponent.CTextField txtStockId;
    private com.see.truetransact.uicomponent.CTextField txtToProdID;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmt;
    private com.see.truetransact.uicomponent.CTextField txtUnitType;
    // End of variables declaration//GEN-END:variables
    
}
