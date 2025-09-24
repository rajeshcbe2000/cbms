/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DamageUI.java
 *
 * Created on January 04, 2016, 2:59 PM
 */

package com.see.truetransact.ui.trading.damage;

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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DamageUI extends CInternalFrame implements UIMandatoryField,Observer{
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.damage.DamageRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private DamageMRB objMandatoryRB = new DamageMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private DamageOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    Date currDt = null;
    int updateTab = -1;
    private boolean updateMode = false;
    boolean isFilled = false;
    
    /** Creates new form DamageUI */
    public DamageUI() {
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
        setSizeTableData();
        observable.resetForm();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panDamage, false);
        setButtonEnableDisable();
        btnAdd.setEnabled(false);
        btnTblDelete.setEnabled(false);
        btnDamageNew.setEnabled(false);
        lblDamageIDVal.setText("");
        
    }
    
    private void setSizeTableData() {
        tblDamage.getColumnModel().getColumn(0).setPreferredWidth(2);
        tblDamage.getColumnModel().getColumn(1).setPreferredWidth(25);
        tblDamage.getColumnModel().getColumn(2).setPreferredWidth(25);
        tblDamage.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblDamage.getColumnModel().getColumn(4).setPreferredWidth(20);
        tblDamage.getColumnModel().getColumn(5).setPreferredWidth(45);
        tblDamage.getColumnModel().getColumn(6).setPreferredWidth(25);
        tblDamage.getColumnModel().getColumn(7).setPreferredWidth(25);
        tblDamage.getColumnModel().getColumn(8).setPreferredWidth(35);
        tblDamage.getColumnModel().getColumn(9).setPreferredWidth(25);
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
    private void setMaxLengths(){
        txtDamageQty.setAllowAll(true);
        txtTotalAmt.setValidation(new CurrencyValidation(14, 2));
    }
    
    /**  This method is to add this class as an Observer to an Observable **/
    private void setObservable(){
        try{
            observable =DamageOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try{
             tblDamage.setModel(observable.getTblDamageDetails());
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
       observable.setTxtProdID(CommonUtil.convertObjToStr(txtProdID.getText()));
       observable.setTxtStockID(CommonUtil.convertObjToStr(txtStockID.getText()));
       observable.setTxtUnitType(CommonUtil.convertObjToStr(txtUnitType.getText()));
       observable.setTxtDamageQty(CommonUtil.convertObjToStr(txtDamageQty.getText()));
       observable.setTxtDamageDt(CommonUtil.convertObjToStr(tdtDamageDt.getDateValue()));
       observable.setTxtAvailQty(CommonUtil.convertObjToStr(txtAvailableQty.getText()));
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtProdID.setText(observable.getTxtProdID());
        txtStockID.setText(observable.getTxtStockID());
        txtUnitType.setText(observable.getTxtUnitType());
        txtDamageQty.setText(observable.getTxtDamageQty());
        txtAvailableQty.setText(observable.getTxtAvailQty());
        tdtDamageDt.setDateValue(observable.getTxtDamageDt());
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
         viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("Prod_ID")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingProductID");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Stock_ID")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            map.put("PROD_ID",txtProdID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingStockID");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Edit")||currField.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingDamageIDForEnquiry");
            new ViewAll(this, viewMap).show();
        }
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (viewType == "Prod_ID") {
            txtProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
        }else if (viewType == "Stock_ID") {
            txtStockID.setText(CommonUtil.convertObjToStr(hash.get("STOCK_ID")));
            txtUnitType.setText(CommonUtil.convertObjToStr(hash.get("STOCK_TYPE")));
            txtAvailableQty.setText(CommonUtil.convertObjToStr(hash.get("STOCK_QUANT")));
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            observable.getData(hash);
            lblDamageIDVal.setText(observable.getTxtDamageID());
            tdtDamageDt.setDateValue(observable.getTxtDamageDt());
            txtProdID.setEnabled(true);
            btnProdID.setEnabled(true);
            txtStockID.setEnabled(true);
            btnStockID.setEnabled(true);
            txtDamageQty.setEnabled(true);
            btnDamageNew.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            HashMap whereMap = new HashMap();
            setButtonEnableDisable();
            observable.getData(hash);
            lblDamageIDVal.setText(observable.getTxtDamageID());
            tdtDamageDt.setDateValue(observable.getTxtDamageDt());
            totalAmountCalc();
            totalDamaQty();
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
    private void saveAction(String status){
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                if (observable.getProxyReturnMap().containsKey("DAMAGE_ID")) {
                    ClientUtil.showMessageWindow("Damage ID : " + observable.getProxyReturnMap().get("DAMAGE_ID"));
                } 
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
        }
        btnCancelActionPerformed(null);
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
    
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
       // ClientUtil.enableDisable(panTDS, false);
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
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("DAMAGE_ID", lblDamageIDVal.getText());
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
            mapParam.put(CommonConstants.MAP_NAME, "getTradingDamageIDForEdit");
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
            observable.doAction();
            setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private int totalDamaQty() {
        int totQty = 0;
        if (tblDamage.getRowCount() > 0) {
            for (int i = 0; i < tblDamage.getRowCount(); i++) {
                String qty = CommonUtil.convertObjToStr(tblDamage.getValueAt(i, 8));
                totQty = totQty + CommonUtil.convertObjToInt(qty);
            }

            txtTotalDamageQty.setText(CommonUtil.convertObjToStr((totQty)));
        }
        return totQty;
    }
    
    private double totalAmountCalc() {
        double totAmt = 0.0;
        String value = "";
        if (tblDamage.getRowCount() > 0) {
            for (int i = 0; i < tblDamage.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblDamage.getValueAt(i, 9));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
            value = CurrencyValidation.formatCrore(String.valueOf(totAmt));
            txtTotalAmt.setText(CommonUtil.convertObjToStr(new Double(value)));
        } 
        return totAmt;
    }
    
    public void resetForm() {
        txtProdID.setText("");
        txtStockID.setText("");
        txtUnitType.setText("");
        txtDamageQty.setText("");
        txtAvailableQty.setText("");
    }
    
    private void setSubClearData(){
         txtProdID.setText("");
         txtStockID.setText("");
         txtUnitType.setText("");
         txtDamageQty.setText("");
         txtAvailableQty.setText("");
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
        panDamage = new com.see.truetransact.uicomponent.CPanel();
        panDamageDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDamageID = new com.see.truetransact.uicomponent.CLabel();
        lblDamageIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblDamageDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDamageDt = new com.see.truetransact.uicomponent.CDateField();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        panProdID = new com.see.truetransact.uicomponent.CPanel();
        txtProdID = new com.see.truetransact.uicomponent.CTextField();
        btnProdID = new com.see.truetransact.uicomponent.CButton();
        lblUnitType = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableQty = new com.see.truetransact.uicomponent.CLabel();
        txtAvailableQty = new com.see.truetransact.uicomponent.CTextField();
        txtDamageQty = new com.see.truetransact.uicomponent.CTextField();
        lblDamageQty = new com.see.truetransact.uicomponent.CLabel();
        panAddDelete = new com.see.truetransact.uicomponent.CPanel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnTblDelete = new com.see.truetransact.uicomponent.CButton();
        btnDamageNew = new com.see.truetransact.uicomponent.CButton();
        txtUnitType = new com.see.truetransact.uicomponent.CTextField();
        panStockID = new com.see.truetransact.uicomponent.CPanel();
        txtStockID = new com.see.truetransact.uicomponent.CTextField();
        btnStockID = new com.see.truetransact.uicomponent.CButton();
        lblStockID = new com.see.truetransact.uicomponent.CLabel();
        panDamageTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpDamageTableDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblDamage = new com.see.truetransact.uicomponent.CTable();
        panDamageTotalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTotalDamageQty = new com.see.truetransact.uicomponent.CLabel();
        txtTotalDamageQty = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment1 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(800, 558));
        setPreferredSize(new java.awt.Dimension(800, 558));

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

        panDamage.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDamage.setMinimumSize(new java.awt.Dimension(750, 500));
        panDamage.setPreferredSize(new java.awt.Dimension(750, 500));
        panDamage.setLayout(new java.awt.GridBagLayout());

        panDamageDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Damage Details"));
        panDamageDetails.setMinimumSize(new java.awt.Dimension(700, 180));
        panDamageDetails.setPreferredSize(new java.awt.Dimension(700, 180));
        panDamageDetails.setLayout(new java.awt.GridBagLayout());

        lblDamageID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDamageID.setText("Damage ID");
        lblDamageID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDamageID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblDamageID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblDamageID.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblDamageID, gridBagConstraints);

        lblDamageIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblDamageIDVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDamageIDVal.setText("Damage ID");
        lblDamageIDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDamageIDVal.setMaximumSize(new java.awt.Dimension(10, 18));
        lblDamageIDVal.setMinimumSize(new java.awt.Dimension(10, 18));
        lblDamageIDVal.setPreferredSize(new java.awt.Dimension(10, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblDamageIDVal, gridBagConstraints);

        lblDamageDt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDamageDt.setText("Damage Date");
        lblDamageDt.setMaximumSize(new java.awt.Dimension(80, 18));
        lblDamageDt.setMinimumSize(new java.awt.Dimension(80, 18));
        lblDamageDt.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblDamageDt, gridBagConstraints);

        tdtDamageDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(tdtDamageDt, gridBagConstraints);

        lblProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdID.setText("Product ID");
        lblProdID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblProdID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblProdID.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblProdID, gridBagConstraints);

        panProdID.setMinimumSize(new java.awt.Dimension(125, 23));
        panProdID.setPreferredSize(new java.awt.Dimension(125, 23));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProdID.add(btnProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panDamageDetails.add(panProdID, gridBagConstraints);

        lblUnitType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnitType.setText("Unit Type");
        lblUnitType.setMaximumSize(new java.awt.Dimension(70, 18));
        lblUnitType.setMinimumSize(new java.awt.Dimension(70, 18));
        lblUnitType.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblUnitType, gridBagConstraints);

        lblAvailableQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAvailableQty.setText("Available Qty");
        lblAvailableQty.setMaximumSize(new java.awt.Dimension(100, 18));
        lblAvailableQty.setMinimumSize(new java.awt.Dimension(100, 18));
        lblAvailableQty.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblAvailableQty, gridBagConstraints);

        txtAvailableQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAvailableQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAvailableQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(txtAvailableQty, gridBagConstraints);

        txtDamageQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDamageQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDamageQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(txtDamageQty, gridBagConstraints);

        lblDamageQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDamageQty.setText("Damage Qty");
        lblDamageQty.setMaximumSize(new java.awt.Dimension(70, 18));
        lblDamageQty.setMinimumSize(new java.awt.Dimension(150, 18));
        lblDamageQty.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblDamageQty, gridBagConstraints);

        panAddDelete.setMinimumSize(new java.awt.Dimension(310, 30));
        panAddDelete.setPreferredSize(new java.awt.Dimension(310, 30));
        panAddDelete.setLayout(new java.awt.GridBagLayout());

        btnAdd.setForeground(new java.awt.Color(0, 153, 51));
        btnAdd.setText("SAVE");
        btnAdd.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnAdd.setMaximumSize(new java.awt.Dimension(89, 30));
        btnAdd.setMinimumSize(new java.awt.Dimension(95, 30));
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 30));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        panAddDelete.add(btnAdd, gridBagConstraints);

        btnTblDelete.setForeground(new java.awt.Color(0, 153, 51));
        btnTblDelete.setText("DELETE");
        btnTblDelete.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnTblDelete.setMaximumSize(new java.awt.Dimension(89, 30));
        btnTblDelete.setMinimumSize(new java.awt.Dimension(89, 30));
        btnTblDelete.setPreferredSize(new java.awt.Dimension(89, 30));
        btnTblDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTblDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        panAddDelete.add(btnTblDelete, gridBagConstraints);

        btnDamageNew.setForeground(new java.awt.Color(0, 153, 51));
        btnDamageNew.setText("NEW");
        btnDamageNew.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDamageNew.setMaximumSize(new java.awt.Dimension(89, 30));
        btnDamageNew.setMinimumSize(new java.awt.Dimension(95, 30));
        btnDamageNew.setPreferredSize(new java.awt.Dimension(95, 30));
        btnDamageNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDamageNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        panAddDelete.add(btnDamageNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDamageDetails.add(panAddDelete, gridBagConstraints);

        txtUnitType.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUnitType.setMaximumSize(new java.awt.Dimension(100, 21));
        txtUnitType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(txtUnitType, gridBagConstraints);

        panStockID.setMinimumSize(new java.awt.Dimension(125, 23));
        panStockID.setPreferredSize(new java.awt.Dimension(125, 23));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStockID.add(btnStockID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panDamageDetails.add(panStockID, gridBagConstraints);

        lblStockID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStockID.setText("Stock ID");
        lblStockID.setMaximumSize(new java.awt.Dimension(75, 18));
        lblStockID.setMinimumSize(new java.awt.Dimension(75, 18));
        lblStockID.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDamageDetails.add(lblStockID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 12, 2);
        panDamage.add(panDamageDetails, gridBagConstraints);

        panDamageTableDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Damage Table Details"));
        panDamageTableDetails.setMaximumSize(new java.awt.Dimension(700, 170));
        panDamageTableDetails.setMinimumSize(new java.awt.Dimension(700, 170));
        panDamageTableDetails.setPreferredSize(new java.awt.Dimension(700, 170));
        panDamageTableDetails.setLayout(new java.awt.GridBagLayout());

        srpDamageTableDetails.setMaximumSize(new java.awt.Dimension(690, 150));
        srpDamageTableDetails.setMinimumSize(new java.awt.Dimension(690, 150));
        srpDamageTableDetails.setPreferredSize(new java.awt.Dimension(690, 150));
        srpDamageTableDetails.setRequestFocusEnabled(false);

        tblDamage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Prod ID", "Stock ID", "Prod Name", "Type", "Purchase Price", "Sales Price", "Avail Qty", "Damage Qty", "Total Amt"
            }
        ));
        tblDamage.setMinimumSize(new java.awt.Dimension(400, 700));
        tblDamage.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblDamage.setPreferredSize(new java.awt.Dimension(400, 700));
        tblDamage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDamageMousePressed(evt);
            }
        });
        tblDamage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDamageKeyReleased(evt);
            }
        });
        srpDamageTableDetails.setViewportView(tblDamage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDamageTableDetails.add(srpDamageTableDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panDamage.add(panDamageTableDetails, gridBagConstraints);

        panDamageTotalDetails.setMinimumSize(new java.awt.Dimension(700, 40));
        panDamageTotalDetails.setPreferredSize(new java.awt.Dimension(700, 40));
        panDamageTotalDetails.setLayout(new java.awt.GridBagLayout());

        lblTotalDamageQty.setText("Total Damage Qty :");
        lblTotalDamageQty.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalDamageQty.setMaximumSize(new java.awt.Dimension(125, 18));
        lblTotalDamageQty.setMinimumSize(new java.awt.Dimension(125, 18));
        lblTotalDamageQty.setPreferredSize(new java.awt.Dimension(125, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panDamageTotalDetails.add(lblTotalDamageQty, gridBagConstraints);

        txtTotalDamageQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalDamageQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panDamageTotalDetails.add(txtTotalDamageQty, gridBagConstraints);

        lblAllignment.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment.setMaximumSize(new java.awt.Dimension(160, 18));
        lblAllignment.setMinimumSize(new java.awt.Dimension(160, 18));
        lblAllignment.setPreferredSize(new java.awt.Dimension(160, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 0);
        panDamageTotalDetails.add(lblAllignment, gridBagConstraints);

        lblTotalAmt.setText("Total Amount :");
        lblTotalAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalAmt.setMaximumSize(new java.awt.Dimension(95, 18));
        lblTotalAmt.setMinimumSize(new java.awt.Dimension(95, 18));
        lblTotalAmt.setPreferredSize(new java.awt.Dimension(95, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panDamageTotalDetails.add(lblTotalAmt, gridBagConstraints);

        txtTotalAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panDamageTotalDetails.add(txtTotalAmt, gridBagConstraints);

        lblAllignment1.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment1.setMaximumSize(new java.awt.Dimension(50, 18));
        lblAllignment1.setMinimumSize(new java.awt.Dimension(50, 18));
        lblAllignment1.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panDamageTotalDetails.add(lblAllignment1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panDamage.add(panDamageTotalDetails, gridBagConstraints);

        getContentPane().add(panDamage, java.awt.BorderLayout.CENTER);

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
        ClientUtil.enableDisable(panDamage, false);
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
        resetForm();
        observable.resetTblDetails();
        tblDamage.setModel(observable.getTblDamageDetails());
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDamage, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        btnProdID.setEnabled(false);
        btnAdd.setEnabled(false);
        btnTblDelete.setEnabled(false);
        btnDamageNew.setEnabled(false);
        viewType = "";
        //__ Make the Screen Closable..
        setModified(false);
         btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblDamageIDVal.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (tblDamage.getRowCount() > 0) {
            savePerformed();
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("No records for Damage");
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
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        //ClientUtil.enableDisable(panDamage, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        tdtDamageDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        txtUnitType.setEnabled(false);
        txtAvailableQty.setEnabled(false);
        txtTotalDamageQty.setEnabled(false);
        tdtDamageDt.setEnabled(false);
        txtTotalAmt.setEnabled(false);
        //btnProdID.setEnabled(true);
        btnDamageNew.setEnabled(true);
        //btnAdd.setEnabled(true);
       // btnStockID.setEnabled(true);
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdIDActionPerformed

    private void txtProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProdIDFocusLost
        // TODO add your handling code here:
        if (txtProdID.getText() != null && txtProdID.getText().length() > 0) {
           // showingCustomerDetails(txtCustomerID.getText());
        }
    }//GEN-LAST:event_txtProdIDFocusLost

    private void btnProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIDActionPerformed
        // TODO add your handling code here:
        viewType = "Prod_ID";
        callView("Prod_ID");
    }//GEN-LAST:event_btnProdIDActionPerformed

    private void tblDamageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDamageMousePressed
        // TODO add your handling code here:
        if (tblDamage.getRowCount() > 0) {
            btnAdd.setEnabled(true);
            btnDamageNew.setEnabled(true);
            updateMode = true;
            updateTab = tblDamage.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblDamage.getValueAt(tblDamage.getSelectedRow(), 0));
            observable.populateDamageTableDetails(st);
            tdtDamageDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
            btnTblDelete.setEnabled(true);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                btnAdd.setEnabled(false);
                btnDamageNew.setEnabled(false);
                btnTblDelete.setEnabled(false);
            } 
        }
    }//GEN-LAST:event_tblDamageMousePressed

    private void tblDamageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDamageKeyReleased
        // TODO add your handling code here:
        int availQty = 0;
        int retQty = 0;
        double rate = 0.0;
        double returnTot = 0.0;
        if ((tblDamage.getRowCount() > 0) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
        HashMap stockMap = new HashMap();
        retQty = CommonUtil.convertObjToInt(tblDamage.getValueAt(tblDamage.getSelectedRow(), 9));
       // stockMap.put("SALES_NO", txtSalesReturnSalNo.getText());
        stockMap.put("PRODUCT_NAME", CommonUtil.convertObjToStr(tblDamage.getValueAt(tblDamage.getSelectedRow(), 1)));
        stockMap.put("UNIT_TYPE", CommonUtil.convertObjToStr(tblDamage.getValueAt(tblDamage.getSelectedRow(), 3)));
        stockMap.put("RATE", CommonUtil.convertObjToDouble(tblDamage.getValueAt(tblDamage.getSelectedRow(), 2)));
        }
    }//GEN-LAST:event_tblDamageKeyReleased

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (txtProdID.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Product ID should not be empty");
            return;
        } else if (txtStockID.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Stock ID should not be empty");
            return;
        } else if (txtDamageQty.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Damage Qty should not be empty");
            return;
        } else {
            updateOBFields();
            HashMap stockMap = new HashMap();
            double totalAmt = 0.0;
            double amt = 0.0;
            stockMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProdID.getText()));
            List stockLst = ClientUtil.executeQuery("getTradingStockID", stockMap);
            if (stockLst != null && stockLst.size() > 0) {
                stockMap = (HashMap) stockLst.get(0);
                amt = CommonUtil.convertObjToDouble(stockMap.get("STOCK_PURCHASE_PRICE"));
                totalAmt = amt * CommonUtil.convertObjToDouble(txtDamageQty.getText());
                stockMap.put("TOTAL_AMOUNT", totalAmt);
                observable.addDataToDamageDetailsTable(updateTab, updateMode, stockMap);
                totalDamaQty();
                totalAmountCalc();
                updateMode = false;
                observable.resetForm();
                resetForm();
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnTblDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTblDeleteActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblDamage.getValueAt(tblDamage.getSelectedRow(), 0));
        observable.deletePurchaseDetails(st, tblDamage.getSelectedRow());
        tblDamage.setModel(observable.getTblDamageDetails());
        totalAmountCalc();
        totalDamaQty();
        observable.resetForm();
        resetForm();
        btnDamageNew.setEnabled(true);
        txtAvailableQty.setEnabled(false);
        txtStockID.setEnabled(false);
        txtProdID.setEnabled(false);
        btnProdID.setEnabled(false);
        btnStockID.setEnabled(false);
    }//GEN-LAST:event_btnTblDeleteActionPerformed

    private void txtStockIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockIDActionPerformed

    private void txtStockIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockIDFocusLost

    private void btnStockIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockIDActionPerformed
        // TODO add your handling code here:
        callView("Stock_ID");
    }//GEN-LAST:event_btnStockIDActionPerformed

    private void btnDamageNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDamageNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panDamage, true);
        tdtDamageDt.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        txtUnitType.setEnabled(false);
        txtAvailableQty.setEnabled(false);
        txtTotalDamageQty.setEnabled(false);
        tdtDamageDt.setEnabled(false);
        txtTotalAmt.setEnabled(false);
        btnProdID.setEnabled(true);
        btnAdd.setEnabled(true);
        btnStockID.setEnabled(true);
        observable.setNewData(true);
        setSubClearData();
    }//GEN-LAST:event_btnDamageNewActionPerformed
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDamageNew;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdID;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStockID;
    private com.see.truetransact.uicomponent.CButton btnTblDelete;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAllignment;
    private com.see.truetransact.uicomponent.CLabel lblAllignment1;
    private com.see.truetransact.uicomponent.CLabel lblAvailableQty;
    private com.see.truetransact.uicomponent.CLabel lblDamageDt;
    private com.see.truetransact.uicomponent.CLabel lblDamageID;
    private com.see.truetransact.uicomponent.CLabel lblDamageIDVal;
    private com.see.truetransact.uicomponent.CLabel lblDamageQty;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
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
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalDamageQty;
    private com.see.truetransact.uicomponent.CLabel lblUnitType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAddDelete;
    private com.see.truetransact.uicomponent.CPanel panDamage;
    private com.see.truetransact.uicomponent.CPanel panDamageDetails;
    private com.see.truetransact.uicomponent.CPanel panDamageTableDetails;
    private com.see.truetransact.uicomponent.CPanel panDamageTotalDetails;
    private com.see.truetransact.uicomponent.CPanel panProdID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStockID;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpDamageTableDetails;
    private com.see.truetransact.uicomponent.CTable tblDamage;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtDamageDt;
    private com.see.truetransact.uicomponent.CTextField txtAvailableQty;
    private com.see.truetransact.uicomponent.CTextField txtDamageQty;
    private com.see.truetransact.uicomponent.CTextField txtProdID;
    private com.see.truetransact.uicomponent.CTextField txtStockID;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalDamageQty;
    private com.see.truetransact.uicomponent.CTextField txtUnitType;
    // End of variables declaration//GEN-END:variables
    
}
