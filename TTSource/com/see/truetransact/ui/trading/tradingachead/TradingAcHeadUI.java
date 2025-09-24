/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingAcHeadUI.java
 *
 * Created on Mon Mar 09 16:05:07 IST 2015
 */
package com.see.truetransact.ui.trading.tradingachead;

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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingAcHeadUI extends CInternalFrame implements UIMandatoryField, Observer {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.tradingachead.TradingAcHeadRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingAcHeadMRB objMandatoryRB = new TradingAcHeadMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingAcHeadOB observable;
    private int viewType = 0;
    //private final String AUTHORIZE = "Authorize";
    boolean isFilled = false;
    String acHdPid = "";
    private final String CAH = "Cash On Hand";
    private final String PUR = "Purchase";
    private final String SAL = "Sales";
    private final String PRE = "Purchase Return";
    private final String SRE = "Sales Return";
    private final String DA = "Damages";
    private final String STO = "Stock";
    private final String PVAT = "Purchase VAT";
    private final String SVAT = "Sales VAT";
    private final String SAR = "SA Receivable";
    private final String SLP = "SL Payable";
    private String btnType = "";
    private final int AUTHORIZE = 999;
    boolean isAuth = false;

    /**
     * Creates new form TDSConfigUI
     */
    public TradingAcHeadUI() {
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
        observable.resetForm();
        observable.getData();
        update();
        ClientUtil.enableDisable(panTradingAcHead, true);
        setBtnEnableDisable(true);
        setButtonEnableDisable();
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //lblHeadType.setText(resourceBundle.getString("lblTdsId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
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

    /**
     * This method sets the Maximum allowed lenght to the textfields *
     */
    private void setMaxLengths() {
        txtCashOnHand.setAllowNumber(true);
        txtPurchase.setAllowNumber(true);
        txtSales.setAllowNumber(true);
        txtPurchaseReturn.setAllowNumber(true);
        txtSalesReturn.setAllowNumber(true);
        txtDamages.setAllowNumber(true);
        txtPurchaseVAT.setAllowNumber(true);
        txtSalesVAT.setAllowNumber(true);
        txtValue.setAllowNumber(true);
        txtValue.setMaxLength(3);
        txtSAReceivable.setAllowNumber(true);
        txtSLPayable.setAllowNumber(true);
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = TradingAcHeadOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        cboPeriod.setModel(observable.getCbmPeriod());
    }

    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
    private void setButtonEnableDisable() {
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void setBtnEnableDisable(boolean flag) {
        btnCashOnHand.setEnabled(flag);
        btnPurchase.setEnabled(flag);
        btnSales.setEnabled(flag);
        btnPurchaseReturn.setEnabled(flag);
        btnSalesReturn.setEnabled(flag);
        btnDamages.setEnabled(flag);
        btnPurchaseVAT.setEnabled(flag);
        btnSalesVAT.setEnabled(flag);
        btnSAReceivable.setEnabled(flag);
        btnSLPayable.setEnabled(flag);
        txtValue.setEnabled(flag);
        cboPeriod.setEnabled(flag);
        btnStock.setEnabled(flag);
    }
    
    private void setEnableDisable(boolean flag) {
        txtCashOnHand.setEnabled(flag);
        txtPurchase.setEnabled(flag);
        txtSales.setEnabled(flag);
        txtPurchaseReturn.setEnabled(flag);
        txtSalesReturn.setEnabled(flag);
        txtDamages.setEnabled(flag);
        txtPurchaseVAT.setEnabled(flag);
        txtSalesVAT.setEnabled(flag);
        txtValue.setEnabled(flag);
        cboPeriod.setEnabled(flag);
        txtStock.setEnabled(flag);
        txtSAReceivable.setEnabled(flag);
        txtSLPayable.setEnabled(flag);
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCashOnHand(txtCashOnHand.getText());
        observable.setTxtPurchase(txtPurchase.getText());
        observable.setTxtSales(txtSales.getText());
        observable.setTxtPurchaseReturn(txtPurchaseReturn.getText());
        observable.setTxtSalesReturn(txtSalesReturn.getText());
        observable.setTxtDamages(txtDamages.getText());
        observable.setTxtPurchaseVAT(txtPurchaseVAT.getText());
        observable.setTxtSalesVAT(txtSalesVAT.getText());
        observable.setTxtSAReceivable(txtSAReceivable.getText());
        observable.setTxtSLPayable(txtSLPayable.getText());
        observable.setTxtValue(txtValue.getText());
        observable.setTxtStock(txtStock.getText());
        observable.setCboPeriod((String) cboPeriod.getSelectedItem());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    public void update(){
        txtCashOnHand.setText(observable.getTxtCashOnHand());
        if (!txtCashOnHand.getText().equals("") && txtCashOnHand.getText().length() > 0) {
            lblCashOnHandVal.setText(getAccHeadDesc(txtCashOnHand.getText()));
        }
        txtPurchase.setText(observable.getTxtPurchase());
        if (!txtPurchase.getText().equals("") && txtPurchase.getText().length() > 0) {
            lblPurchaseVal.setText(getAccHeadDesc(txtPurchase.getText()));
        }
        txtSales.setText(observable.getTxtSales());
        if (!txtSales.getText().equals("") && txtSales.getText().length() > 0) {
            lblSalesVal.setText(getAccHeadDesc(txtSales.getText()));
        }
        txtPurchaseReturn.setText(observable.getTxtPurchaseReturn());
        if (!txtPurchaseReturn.getText().equals("") && txtPurchaseReturn.getText().length() > 0) {
            lblPurchaseReturnVal.setText(getAccHeadDesc(txtPurchaseReturn.getText()));
        }
        txtSalesReturn.setText(observable.getTxtSalesReturn());
        if (!txtSalesReturn.getText().equals("") && txtSalesReturn.getText().length() > 0) {
            lblSalesReturnVal.setText(getAccHeadDesc(txtSalesReturn.getText()));
        }
        txtDamages.setText(observable.getTxtDamages());
        if (!txtDamages.getText().equals("") && txtDamages.getText().length() > 0) {
            lblDamagesVal.setText(getAccHeadDesc(txtDamages.getText()));
        }
        txtPurchaseVAT.setText(observable.getTxtPurchaseVAT());
        if (!txtPurchaseVAT.getText().equals("") && txtPurchaseVAT.getText().length() > 0) {
            lblPurchaseVATVal.setText(getAccHeadDesc(txtPurchaseVAT.getText()));
        }
        txtSalesVAT.setText(observable.getTxtSalesVAT());
        if (!txtSalesVAT.getText().equals("") && txtSalesVAT.getText().length() > 0) {
            lblSalesVATVal.setText(getAccHeadDesc(txtSalesVAT.getText()));
        }
        txtSAReceivable.setText(observable.getTxtSAReceivable());
        if (!txtSAReceivable.getText().equals("") && txtSAReceivable.getText().length() > 0) {
            lblSAReceivableVal.setText(getAccHeadDesc(txtSAReceivable.getText()));
        }
        txtSLPayable.setText(observable.getTxtSLPayable());
        if (!txtSLPayable.getText().equals("") && txtSLPayable.getText().length() > 0) {
            lblSLPayableVal.setText(getAccHeadDesc(txtSLPayable.getText()));
        }
        txtStock.setText(observable.getTxtStock());
        if (!txtStock.getText().equals("") && txtStock.getText().length() > 0) {
            lblStockVal.setText(getAccHeadDesc(txtStock.getText()));
        }
        txtValue.setText(observable.getTxtValue());
        cboPeriod.setSelectedItem(observable.getCboPeriod());
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

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String viewType) {
        //viewType = currField;
        HashMap viewMap = new HashMap();
//        if (currField.equalsIgnoreCase("AcHd")) {
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "getTradingAcHdID");
//            new ViewAll(this, viewMap).show();
//        }else if (currField.equalsIgnoreCase("Edit") || (currField.equalsIgnoreCase("Delete"))) {
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "getTradingAcHdEdit");
//            new ViewAll(this, viewMap).show();
//        }else if (currField.equalsIgnoreCase("Enquiry")) {
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "getTradingAcHdView");
//            new ViewAll(this, viewMap).show();
//        }else {
        
            btnType = viewType;
            updateOBFields();
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectActHdId");
            new ViewAll(this, viewMap).show();
        
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
         try {
             isFilled = true;
             HashMap hash = (HashMap) map;
             String acHdPid = CommonUtil.convertObjToStr(hash.get("ACCOUNTHEAD_PID"));
             if (CAH.equals(btnType)){
                 txtCashOnHand.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblCashOnHandVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             } else if(PUR.equals(btnType)){
                 txtPurchase.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblPurchaseVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(SAL.equals(btnType)){
                 txtSales.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblSalesVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(PRE.equals(btnType)){
                 txtPurchaseReturn.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblPurchaseReturnVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(SRE.equals(btnType)){
                 txtSalesReturn.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblSalesReturnVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(PVAT.equals(btnType)){
                 txtPurchaseVAT.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblPurchaseVATVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(SVAT.equals(btnType)){
                 txtSalesVAT.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblSalesVATVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if(DA.equals(btnType)){
                 txtDamages.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblDamagesVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if (STO.equals(btnType)){
                 txtStock.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblStockVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if (SAR.equals(btnType)){
                 txtSAReceivable.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblSAReceivableVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }else if (SLP.equals(btnType)){
                 txtSLPayable.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 lblSLPayableVal.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
             }
              if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                     || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                     || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                 setButtonEnableDisable();
                 observable.getData();
                 update();
             }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            observable.getData();
            update();
            btnSalesVAT.setEnabled(false);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setModified(true);
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
    private void saveAction() {
        
    }

    /* set the screen after the updation,insertion, deletion */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTradingAcHead, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /* Does necessary operaion when user clicks the save button */
    private void savePerformed() {
        updateOBFields();
        observable.doAction();
        btnCancelActionPerformed(null);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }
    
    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void authorizeStatus(String authorizeStatus) {
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
        panTradingAcHead = new com.see.truetransact.uicomponent.CPanel();
        lblSalesVAT = new com.see.truetransact.uicomponent.CLabel();
        lblSalesVATVal = new com.see.truetransact.uicomponent.CLabel();
        panSalesVAT = new com.see.truetransact.uicomponent.CPanel();
        txtSalesVAT = new com.see.truetransact.uicomponent.CTextField();
        btnSalesVAT = new com.see.truetransact.uicomponent.CButton();
        lblPurchaseVAT = new com.see.truetransact.uicomponent.CLabel();
        lblDamages = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturn = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseReturn = new com.see.truetransact.uicomponent.CLabel();
        lblSales = new com.see.truetransact.uicomponent.CLabel();
        lblPurchase = new com.see.truetransact.uicomponent.CLabel();
        lblCashOnHand = new com.see.truetransact.uicomponent.CLabel();
        panCashOnHand = new com.see.truetransact.uicomponent.CPanel();
        txtCashOnHand = new com.see.truetransact.uicomponent.CTextField();
        btnCashOnHand = new com.see.truetransact.uicomponent.CButton();
        panSales = new com.see.truetransact.uicomponent.CPanel();
        txtSales = new com.see.truetransact.uicomponent.CTextField();
        btnSales = new com.see.truetransact.uicomponent.CButton();
        panPurchaseReturn = new com.see.truetransact.uicomponent.CPanel();
        txtPurchaseReturn = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseReturn = new com.see.truetransact.uicomponent.CButton();
        panPurchaseVAT = new com.see.truetransact.uicomponent.CPanel();
        txtPurchaseVAT = new com.see.truetransact.uicomponent.CTextField();
        btnPurchaseVAT = new com.see.truetransact.uicomponent.CButton();
        panDamages = new com.see.truetransact.uicomponent.CPanel();
        txtDamages = new com.see.truetransact.uicomponent.CTextField();
        btnDamages = new com.see.truetransact.uicomponent.CButton();
        panSalesReturn = new com.see.truetransact.uicomponent.CPanel();
        txtSalesReturn = new com.see.truetransact.uicomponent.CTextField();
        btnSalesReturn = new com.see.truetransact.uicomponent.CButton();
        panPurchase = new com.see.truetransact.uicomponent.CPanel();
        txtPurchase = new com.see.truetransact.uicomponent.CTextField();
        btnPurchase = new com.see.truetransact.uicomponent.CButton();
        lblPurchaseVATVal = new com.see.truetransact.uicomponent.CLabel();
        lblDamagesVal = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseReturnVal = new com.see.truetransact.uicomponent.CLabel();
        lblSalesVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseVal = new com.see.truetransact.uicomponent.CLabel();
        lblCashOnHandVal = new com.see.truetransact.uicomponent.CLabel();
        lblSalesReturnBefore = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseOrSale = new com.see.truetransact.uicomponent.CPanel();
        cboPeriod = new com.see.truetransact.uicomponent.CComboBox();
        txtValue = new com.see.truetransact.uicomponent.CTextField();
        panStock = new com.see.truetransact.uicomponent.CPanel();
        txtStock = new com.see.truetransact.uicomponent.CTextField();
        btnStock = new com.see.truetransact.uicomponent.CButton();
        lblStockVal = new com.see.truetransact.uicomponent.CLabel();
        lblStock = new com.see.truetransact.uicomponent.CLabel();
        lblSLPayable = new com.see.truetransact.uicomponent.CLabel();
        lblSLPayableVal = new com.see.truetransact.uicomponent.CLabel();
        panSLPayable = new com.see.truetransact.uicomponent.CPanel();
        txtSLPayable = new com.see.truetransact.uicomponent.CTextField();
        btnSLPayable = new com.see.truetransact.uicomponent.CButton();
        lblSAReceivable = new com.see.truetransact.uicomponent.CLabel();
        lblSAReceivableVal = new com.see.truetransact.uicomponent.CLabel();
        panSAReceivable = new com.see.truetransact.uicomponent.CPanel();
        txtSAReceivable = new com.see.truetransact.uicomponent.CTextField();
        btnSAReceivable = new com.see.truetransact.uicomponent.CButton();
        tbrTDSConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(650, 450));
        setPreferredSize(new java.awt.Dimension(650, 450));

        panTradingAcHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTradingAcHead.setLayout(new java.awt.GridBagLayout());

        lblSalesVAT.setText("Sales VAT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesVAT, gridBagConstraints);

        lblSalesVATVal.setForeground(new java.awt.Color(0, 51, 255));
        lblSalesVATVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSalesVATVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSalesVATVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesVATVal, gridBagConstraints);

        panSalesVAT.setLayout(new java.awt.GridBagLayout());

        txtSalesVAT.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesVAT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesVATFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesVAT.add(txtSalesVAT, gridBagConstraints);

        btnSalesVAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesVAT.setEnabled(false);
        btnSalesVAT.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesVAT.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesVAT.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesVAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesVATActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesVAT.add(btnSalesVAT, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panSalesVAT, gridBagConstraints);

        lblPurchaseVAT.setText("Purchase VAT ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchaseVAT, gridBagConstraints);

        lblDamages.setText("Loss on Damages");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblDamages, gridBagConstraints);

        lblSalesReturn.setText("Sales Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesReturn, gridBagConstraints);

        lblPurchaseReturn.setText("Purchase Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchaseReturn, gridBagConstraints);

        lblSales.setText("Sales");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSales, gridBagConstraints);

        lblPurchase.setText("Purchase");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchase, gridBagConstraints);

        lblCashOnHand.setText("Cash On Hand");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblCashOnHand, gridBagConstraints);

        panCashOnHand.setLayout(new java.awt.GridBagLayout());

        txtCashOnHand.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashOnHand.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashOnHandFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashOnHand.add(txtCashOnHand, gridBagConstraints);

        btnCashOnHand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCashOnHand.setEnabled(false);
        btnCashOnHand.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCashOnHand.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCashOnHand.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCashOnHand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashOnHandActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashOnHand.add(btnCashOnHand, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panCashOnHand, gridBagConstraints);

        panSales.setLayout(new java.awt.GridBagLayout());

        txtSales.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSales.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSales.add(txtSales, gridBagConstraints);

        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSales.setEnabled(false);
        btnSales.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSales.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSales.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSales.add(btnSales, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panSales, gridBagConstraints);

        panPurchaseReturn.setLayout(new java.awt.GridBagLayout());

        txtPurchaseReturn.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseReturn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseReturnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseReturn.add(txtPurchaseReturn, gridBagConstraints);

        btnPurchaseReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseReturn.setEnabled(false);
        btnPurchaseReturn.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturn.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturn.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseReturnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseReturn.add(btnPurchaseReturn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panPurchaseReturn, gridBagConstraints);

        panPurchaseVAT.setLayout(new java.awt.GridBagLayout());

        txtPurchaseVAT.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseVAT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseVATFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseVAT.add(txtPurchaseVAT, gridBagConstraints);

        btnPurchaseVAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchaseVAT.setEnabled(false);
        btnPurchaseVAT.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchaseVAT.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchaseVAT.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchaseVAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseVATActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseVAT.add(btnPurchaseVAT, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panPurchaseVAT, gridBagConstraints);

        panDamages.setLayout(new java.awt.GridBagLayout());

        txtDamages.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDamages.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDamagesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDamages.add(txtDamages, gridBagConstraints);

        btnDamages.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDamages.setEnabled(false);
        btnDamages.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDamages.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDamages.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDamages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDamagesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDamages.add(btnDamages, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panDamages, gridBagConstraints);

        panSalesReturn.setLayout(new java.awt.GridBagLayout());

        txtSalesReturn.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalesReturn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesReturnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesReturn.add(txtSalesReturn, gridBagConstraints);

        btnSalesReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalesReturn.setEnabled(false);
        btnSalesReturn.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSalesReturn.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalesReturn.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalesReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesReturnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalesReturn.add(btnSalesReturn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panSalesReturn, gridBagConstraints);

        panPurchase.setLayout(new java.awt.GridBagLayout());

        txtPurchase.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchase.add(txtPurchase, gridBagConstraints);

        btnPurchase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPurchase.setEnabled(false);
        btnPurchase.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPurchase.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPurchase.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchase.add(btnPurchase, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panPurchase, gridBagConstraints);

        lblPurchaseVATVal.setForeground(new java.awt.Color(0, 51, 255));
        lblPurchaseVATVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblPurchaseVATVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblPurchaseVATVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchaseVATVal, gridBagConstraints);

        lblDamagesVal.setForeground(new java.awt.Color(0, 51, 255));
        lblDamagesVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblDamagesVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblDamagesVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblDamagesVal, gridBagConstraints);

        lblSalesReturnVal.setForeground(new java.awt.Color(0, 51, 255));
        lblSalesReturnVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSalesReturnVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSalesReturnVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesReturnVal, gridBagConstraints);

        lblPurchaseReturnVal.setForeground(new java.awt.Color(0, 51, 255));
        lblPurchaseReturnVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblPurchaseReturnVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblPurchaseReturnVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchaseReturnVal, gridBagConstraints);

        lblSalesVal.setForeground(new java.awt.Color(0, 51, 255));
        lblSalesVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSalesVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSalesVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesVal, gridBagConstraints);

        lblPurchaseVal.setForeground(new java.awt.Color(0, 51, 255));
        lblPurchaseVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblPurchaseVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblPurchaseVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblPurchaseVal, gridBagConstraints);

        lblCashOnHandVal.setForeground(new java.awt.Color(0, 51, 255));
        lblCashOnHandVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCashOnHandVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblCashOnHandVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblCashOnHandVal, gridBagConstraints);

        lblSalesReturnBefore.setText("Sales Return Before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSalesReturnBefore, gridBagConstraints);

        panPurchaseOrSale.setMaximumSize(new java.awt.Dimension(170, 25));
        panPurchaseOrSale.setMinimumSize(new java.awt.Dimension(170, 25));
        panPurchaseOrSale.setPreferredSize(new java.awt.Dimension(170, 25));
        panPurchaseOrSale.setLayout(new java.awt.GridBagLayout());

        cboPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPeriod.setPopupWidth(220);
        cboPeriod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboPeriodItemStateChanged(evt);
            }
        });
        cboPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodActionPerformed(evt);
            }
        });
        cboPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseOrSale.add(cboPeriod, gridBagConstraints);

        txtValue.setMinimumSize(new java.awt.Dimension(60, 21));
        txtValue.setPreferredSize(new java.awt.Dimension(60, 21));
        txtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panPurchaseOrSale.add(txtValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTradingAcHead.add(panPurchaseOrSale, gridBagConstraints);

        panStock.setLayout(new java.awt.GridBagLayout());

        txtStock.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStock.add(txtStock, gridBagConstraints);

        btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStock.setEnabled(false);
        btnStock.setMaximumSize(new java.awt.Dimension(21, 21));
        btnStock.setMinimumSize(new java.awt.Dimension(21, 21));
        btnStock.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStock.add(btnStock, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panStock, gridBagConstraints);

        lblStockVal.setForeground(new java.awt.Color(0, 51, 255));
        lblStockVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblStockVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblStockVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblStockVal, gridBagConstraints);

        lblStock.setText("Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblStock, gridBagConstraints);

        lblSLPayable.setText("SL-Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSLPayable, gridBagConstraints);

        lblSLPayableVal.setForeground(new java.awt.Color(0, 51, 255));
        lblSLPayableVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSLPayableVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSLPayableVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSLPayableVal, gridBagConstraints);

        panSLPayable.setLayout(new java.awt.GridBagLayout());

        txtSLPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSLPayable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSLPayableFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSLPayable.add(txtSLPayable, gridBagConstraints);

        btnSLPayable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSLPayable.setEnabled(false);
        btnSLPayable.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSLPayable.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSLPayable.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSLPayable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSLPayableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSLPayable.add(btnSLPayable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panSLPayable, gridBagConstraints);

        lblSAReceivable.setText("SA-Receivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSAReceivable, gridBagConstraints);

        lblSAReceivableVal.setForeground(new java.awt.Color(0, 51, 255));
        lblSAReceivableVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSAReceivableVal.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSAReceivableVal.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTradingAcHead.add(lblSAReceivableVal, gridBagConstraints);

        panSAReceivable.setLayout(new java.awt.GridBagLayout());

        txtSAReceivable.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSAReceivable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSAReceivableFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSAReceivable.add(txtSAReceivable, gridBagConstraints);

        btnSAReceivable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSAReceivable.setEnabled(false);
        btnSAReceivable.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSAReceivable.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSAReceivable.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSAReceivable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSAReceivableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSAReceivable.add(btnSAReceivable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTradingAcHead.add(panSAReceivable, gridBagConstraints);

        getContentPane().add(panTradingAcHead, java.awt.BorderLayout.CENTER);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnEdit);

        lblSpace6.setText("     ");
        tbrTDSConfig.add(lblSpace6);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnSave);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace70);

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

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace71);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnException);

        lblSpace5.setText("     ");
        tbrTDSConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTDSConfig.add(btnPrint);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace72);

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

    private void btnTdsGLAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTdsGLAccountHeadActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnTdsGLAccountHeadActionPerformed

    private void tdtStartDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtStartDateFocusLost
        // TODO add your handling code here:
        try {
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        Date dayBt = ClientUtil.getCurrentDate();

    }//GEN-LAST:event_tdtStartDateFocusLost

    private void tdtEndDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtEndDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtEndDateFocusLost

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
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
//        btnNewActionPerformed(evt);
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

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnException.setEnabled(false);
        observable.setLblStatus("AUTHORIZED");
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void updateAuthorizeStatus(String authorizeStatus) {
         if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            observable.setViewType(AUTHORIZE);
            observable.getData();
            update();
            boolean isAuth = observable.isIsAuth();
            if(isAuth){
                setAuthEnable(isAuth);
                ClientUtil.showAlertWindow("There is no Records to Authorize/Reject...");
                btnCancelActionPerformed(null);
            }else{
                ClientUtil.enableDisable(panTradingAcHead, false);
                setBtnEnableDisable(false);
                btnSave.setEnabled(false);
                setModified(true);
            }
            
        }else if (viewType == AUTHORIZE){
             HashMap authMap = new HashMap();
             authMap.put("STATUS", authorizeStatus);
             authMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
             authMap.put("AUTHORIZEDT", ClientUtil.getCurrentDate());
             authMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            observable.setAuthorizeMap(authMap);
            observable.doAction();
            observable.setAuthorizeMap(null);
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
        }
        
    }
    private void setAuthEnable(boolean value){
        btnAuthorize.setEnabled(!value);
        btnException.setEnabled(!value);
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setLblStatus("");
        observable.resetForm();
        resetForm();
        isAuth = false;
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTradingAcHead, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setBtnEnableDisable(true);
        setEnableDisable(true);
        btnEdit.setEnabled(true);
        viewType = 0;
        observable.setViewType(0);
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        observable.setLblStatus("UPDATED");

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.getData();
        update();
        setBtnEnableDisable(true);
        setEnableDisable(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void txtSalesVATFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesVATFocusLost
        // TODO add your handling code here:
        if (!(txtSalesVAT.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtSalesVAT, "TermLoan.getSelectAcctHeadTOList");
            lblSalesVATVal.setText(getAccHeadDesc(txtSalesVAT.getText()));
        }
    }//GEN-LAST:event_txtSalesVATFocusLost

    private void btnSalesVATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesVATActionPerformed
        // TODO add your handling code here:
        callView("Sales VAT");
    }//GEN-LAST:event_btnSalesVATActionPerformed

    private void txtCashOnHandFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashOnHandFocusLost
        // TODO add your handling code here:
        if (!(txtCashOnHand.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtCashOnHand, "TermLoan.getSelectAcctHeadTOList");
            lblCashOnHandVal.setText(getAccHeadDesc(txtCashOnHand.getText()));
        }
    }//GEN-LAST:event_txtCashOnHandFocusLost

    private void btnCashOnHandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashOnHandActionPerformed
        // TODO add your handling code here:
        callView("Cash On Hand");
    }//GEN-LAST:event_btnCashOnHandActionPerformed

    private void txtSalesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesFocusLost
        // TODO add your handling code here:
        if (!(txtSales.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtSales, "TermLoan.getSelectAcctHeadTOList");
            lblSalesVal.setText(getAccHeadDesc(txtSales.getText()));
        }
    }//GEN-LAST:event_txtSalesFocusLost

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        // TODO add your handling code here:
        callView("Sales");
    }//GEN-LAST:event_btnSalesActionPerformed

    private void txtPurchaseReturnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseReturnFocusLost
        // TODO add your handling code here:
        if (!(txtPurchaseReturn.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtPurchaseReturn, "TermLoan.getSelectAcctHeadTOList");
            lblPurchaseReturnVal.setText(getAccHeadDesc(txtPurchaseReturn.getText()));
        }
    }//GEN-LAST:event_txtPurchaseReturnFocusLost

    private void btnPurchaseReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseReturnActionPerformed
        // TODO add your handling code here:
        callView("Purchase Return");
    }//GEN-LAST:event_btnPurchaseReturnActionPerformed

    private void txtPurchaseVATFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseVATFocusLost
        // TODO add your handling code here:
         if (!(txtPurchaseVAT.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtPurchaseVAT, "TermLoan.getSelectAcctHeadTOList");
            lblPurchaseVATVal.setText(getAccHeadDesc(txtPurchaseVAT.getText()));
        }
    }//GEN-LAST:event_txtPurchaseVATFocusLost

    private void btnPurchaseVATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseVATActionPerformed
        // TODO add your handling code here:
        callView("Purchase VAT");
    }//GEN-LAST:event_btnPurchaseVATActionPerformed

    private void txtDamagesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDamagesFocusLost
        // TODO add your handling code here:
        if (!(txtDamages.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtDamages, "TermLoan.getSelectAcctHeadTOList");
            lblDamagesVal.setText(getAccHeadDesc(txtDamages.getText()));
        }
    }//GEN-LAST:event_txtDamagesFocusLost

    private void btnDamagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDamagesActionPerformed
        // TODO add your handling code here:
        callView("Damages");
    }//GEN-LAST:event_btnDamagesActionPerformed

    private void txtSalesReturnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesReturnFocusLost
        // TODO add your handling code here:
        if (!(txtSalesReturn.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtSalesReturn, "TermLoan.getSelectAcctHeadTOList");
            lblSalesReturnVal.setText(getAccHeadDesc(txtSalesReturn.getText()));
        }
    }//GEN-LAST:event_txtSalesReturnFocusLost

    private void btnSalesReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesReturnActionPerformed
        // TODO add your handling code here:
        callView("Sales Return");
    }//GEN-LAST:event_btnSalesReturnActionPerformed

    private void txtPurchaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseFocusLost
        // TODO add your handling code here:
        if (!(txtPurchase.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtPurchase, "TermLoan.getSelectAcctHeadTOList");
            lblPurchaseVal.setText(getAccHeadDesc(txtPurchase.getText()));
        }
    }//GEN-LAST:event_txtPurchaseFocusLost

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        callView("Purchase");
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void txtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValueFocusLost

    private void cboPeriodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboPeriodItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPeriodItemStateChanged

    private void cboPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodActionPerformed
        //        // TODO add your handling code here:
    }//GEN-LAST:event_cboPeriodActionPerformed

    private void cboPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPeriodFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPeriodFocusLost

    private void txtStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockFocusLost

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        // TODO add your handling code here:
         callView("Stock");
    }//GEN-LAST:event_btnStockActionPerformed

    private void txtSLPayableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSLPayableFocusLost
        // TODO add your handling code here:
       if (!(txtSLPayable.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtSLPayable, "TermLoan.getSelectAcctHeadTOList");
            lblSLPayableVal.setText(getAccHeadDesc(txtSLPayable.getText()));
        } 
    }//GEN-LAST:event_txtSLPayableFocusLost

    private void btnSLPayableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSLPayableActionPerformed
        // TODO add your handling code here:
        callView("SL Payable");
    }//GEN-LAST:event_btnSLPayableActionPerformed

    private void txtSAReceivableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSAReceivableFocusLost
        // TODO add your handling code here:
        if (!(txtSAReceivable.getText().equalsIgnoreCase(""))) {
            observable.verifyTradingAcctHead(txtSAReceivable, "TermLoan.getSelectAcctHeadTOList");
            lblSAReceivableVal.setText(getAccHeadDesc(txtSAReceivable.getText()));
        }
    }//GEN-LAST:event_txtSAReceivableFocusLost

    private void btnSAReceivableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSAReceivableActionPerformed
        // TODO add your handling code here:
        callView("SA Receivable");
    }//GEN-LAST:event_btnSAReceivableActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    private void resetForm() {
        txtCashOnHand.setText("");
        txtPurchase.setText("");
        txtSales.setText("");
        txtPurchaseReturn.setText("");
        txtSalesReturn.setText("");
        txtDamages.setText("");
        txtPurchaseVAT.setText("");
        txtSalesVAT.setText("");
        txtSAReceivable.setText("");
        txtSLPayable.setText("");
        lblCashOnHandVal.setText("");
        lblPurchaseVal.setText("");
        lblSalesVal.setText("");
        lblPurchaseReturnVal.setText("");
        lblSalesReturnVal.setText("");
        lblDamagesVal.setText("");
        lblPurchaseVATVal.setText("");
        lblSalesVATVal.setText("");
        txtStock.setText("");
        lblStockVal.setText("");
        lblSAReceivableVal.setText("");
        lblSLPayableVal.setText("");
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCashOnHand;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDamages;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPurchase;
    private com.see.truetransact.uicomponent.CButton btnPurchaseReturn;
    private com.see.truetransact.uicomponent.CButton btnPurchaseVAT;
    private com.see.truetransact.uicomponent.CButton btnSAReceivable;
    private com.see.truetransact.uicomponent.CButton btnSLPayable;
    private com.see.truetransact.uicomponent.CButton btnSales;
    private com.see.truetransact.uicomponent.CButton btnSalesReturn;
    private com.see.truetransact.uicomponent.CButton btnSalesVAT;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStock;
    private com.see.truetransact.uicomponent.CComboBox cboPeriod;
    private com.see.truetransact.uicomponent.CLabel lblCashOnHand;
    private com.see.truetransact.uicomponent.CLabel lblCashOnHandVal;
    private com.see.truetransact.uicomponent.CLabel lblDamages;
    private com.see.truetransact.uicomponent.CLabel lblDamagesVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPurchase;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseReturn;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseReturnVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseVAT;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseVATVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseVal;
    private com.see.truetransact.uicomponent.CLabel lblSAReceivable;
    private com.see.truetransact.uicomponent.CLabel lblSAReceivableVal;
    private com.see.truetransact.uicomponent.CLabel lblSLPayable;
    private com.see.truetransact.uicomponent.CLabel lblSLPayableVal;
    private com.see.truetransact.uicomponent.CLabel lblSales;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturn;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnBefore;
    private com.see.truetransact.uicomponent.CLabel lblSalesReturnVal;
    private com.see.truetransact.uicomponent.CLabel lblSalesVAT;
    private com.see.truetransact.uicomponent.CLabel lblSalesVATVal;
    private com.see.truetransact.uicomponent.CLabel lblSalesVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStock;
    private com.see.truetransact.uicomponent.CLabel lblStockVal;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCashOnHand;
    private com.see.truetransact.uicomponent.CPanel panDamages;
    private com.see.truetransact.uicomponent.CPanel panPurchase;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale;
    private com.see.truetransact.uicomponent.CPanel panPurchaseReturn;
    private com.see.truetransact.uicomponent.CPanel panPurchaseVAT;
    private com.see.truetransact.uicomponent.CPanel panSAReceivable;
    private com.see.truetransact.uicomponent.CPanel panSLPayable;
    private com.see.truetransact.uicomponent.CPanel panSales;
    private com.see.truetransact.uicomponent.CPanel panSalesReturn;
    private com.see.truetransact.uicomponent.CPanel panSalesVAT;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStock;
    private com.see.truetransact.uicomponent.CPanel panTradingAcHead;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CTextField txtCashOnHand;
    private com.see.truetransact.uicomponent.CTextField txtDamages;
    private com.see.truetransact.uicomponent.CTextField txtPurchase;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseReturn;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseVAT;
    private com.see.truetransact.uicomponent.CTextField txtSAReceivable;
    private com.see.truetransact.uicomponent.CTextField txtSLPayable;
    private com.see.truetransact.uicomponent.CTextField txtSales;
    private com.see.truetransact.uicomponent.CTextField txtSalesReturn;
    private com.see.truetransact.uicomponent.CTextField txtSalesVAT;
    private com.see.truetransact.uicomponent.CTextField txtStock;
    private com.see.truetransact.uicomponent.CTextField txtValue;
    // End of variables declaration//GEN-END:variables

}
