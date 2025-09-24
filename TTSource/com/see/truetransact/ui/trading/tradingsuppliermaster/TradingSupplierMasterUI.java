/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingSupplierMasterUI.java
 *
 * Created on Mar 31, 2015, 2:59 PM
 */
package com.see.truetransact.ui.trading.tradingsuppliermaster;

/**
 *
 * @author Revathi.L
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
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingSupplierMasterUI extends CInternalFrame implements UIMandatoryField, Observer {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.tradingsuppliermaster.TradingSupplierMasterRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingSupplierMasterMRB objMandatoryRB = new TradingSupplierMasterMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingSupplierMasterOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    boolean isFilled = false;
    Date currDt = null;

    /**
     * Creates new form TDSConfigUI
     */
    public TradingSupplierMasterUI() {
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
        ClientUtil.enableDisable(panSupplier, false);
        setButtonEnableDisable();
        setButtonEnable(false);
        currDt = ClientUtil.getCurrentDate();
        tdtDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        lblPurchaseName.setVisible(false);
        lblPurchase.setVisible(false);
        txtPurchase.setVisible(false);
        btnPurchase.setVisible(false);
        panSundryCreditors.setVisible(false);
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
        lbTinNo.setName("lbTinNo");
        lblDate.setName("lblDate");
        lblMsg.setName("lblMsg");
        lblphone.setName("lblphone");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblSupplierID.setName("lblTdsId");
        mbrTDSConfig.setName("mbrTDSConfig");
        panCutOff.setName("panCutOff");
        panStatus.setName("panStatus");
        panSupplier.setName("panTDS");
        tdtDate.setName("tdtEndDate");
        txtTinNo.setName("txtCutOfAmount");
        txtPhone.setName("txtPercentage");
        txtSupplierID.setName("txtTdsId");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSupplierID.setText(resourceBundle.getString("lblTdsId"));
        lblDate.setText(resourceBundle.getString("lblDate"));
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
        lbTinNo.setText(resourceBundle.getString("lbTinNo"));
        lblphone.setText(resourceBundle.getString("lblphone"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
   
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
   
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtSupplierID", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtSundryCreditors", new Boolean(true));
        mandatoryMap.put("txtCSTNO", new Boolean(true));
        mandatoryMap.put("txtKGSTNO", new Boolean(true));
        mandatoryMap.put("txtTinNo", new Boolean(true));
        mandatoryMap.put("txtPhone", new Boolean(true));
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

    /**
     * This method sets the Maximum allowed lenght to the textfields *
     */
    private void setMaxLengths() {
        txtCSTNO.setAllowAll(true);
        txtKGSTNO.setAllowAll(true);
        txtTinNo.setAllowAll(true);
        txtSundryCreditors.setAllowAll(true);
        txtPurchase.setAllowAll(true);
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = TradingSupplierMasterOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try {
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

    private void setButtonEnable(boolean flag) {
        btnSundryCreditors.setEnabled(flag);
        btnPurchase.setEnabled(flag);
        btnCustId.setEnabled(flag);
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTdtDate(DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
        observable.setTxtSupplierID(txtSupplierID.getText());
        observable.setTxtTinNo(txtTinNo.getText());
        observable.setTxtPhone(txtPhone.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setTxtCSTNO(txtCSTNO.getText());
        observable.setTxtKGSTNO(txtKGSTNO.getText());
        observable.setTxtName(txtCustomerID.getText());
        observable.setTxtSundryCreditors(txtSundryCreditors.getText());
        observable.setTxtPurchase(txtPurchase.getText());
        if (chkActive.isSelected()) {
            observable.setChkActive("Y");
        } else {
            observable.setChkActive("N");
        }
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        txtSupplierID.setText(observable.getTxtSupplierID());
        txtCustomerID.setText(observable.getCustID());
        txtSundryCreditors.setText(observable.getTxtSundryCreditors());
        txtPurchase.setText(observable.getTxtPurchase());
        tdtDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtDate()));
        txtPhone.setText(observable.getTxtPhone());
        txtTinNo.setText(observable.getTxtTinNo());
        txtAddress.setText(observable.getTxtAddress());
        txtCSTNO.setText(observable.getTxtCSTNO());
        txtKGSTNO.setText(observable.getTxtKGSTNO());
        lblCustomerName.setText(observable.getTxtName());
        lblSundryCreditorsName.setText(observable.getSundryCreditorsName());
        lblPurchaseName.setText(observable.getPurchaseName());
        if (CommonUtil.convertObjToStr(observable.getChkActive()).equals("Y")) {
            chkActive.setSelected(true);
        } else {
            chkActive.setSelected(false);
        }
    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("SUNDRY CREDITORS")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            map.put("CUST_ID", txtCustomerID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSundryCreditorsAcNo");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("PURCHASE")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            map.put("CUST_ID", txtCustomerID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getPurchaseSuspenseAcNo");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingSupplierMasterEdit");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingSupplierView");
            new ViewAll(this, viewMap).show();
        } else if ((currField.equalsIgnoreCase("Delete"))) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingSupplierMasterDelete");
            new ViewAll(this, viewMap).show();
        }
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (viewType.equals("Customer")) {
            HashMap custMap = new HashMap();
            String supplierID = "";
            custMap.put("CUST_ID", CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            List custLst = ClientUtil.executeQuery("getExistCustList", custMap);
            if (custLst != null && custLst.size() > 0) {
                custMap = (HashMap) custLst.get(0);
                supplierID = CommonUtil.convertObjToStr(custMap.get("SUPPLIERID"));
                if (CommonUtil.convertObjToStr(custMap.get("AUTHORIZED_STATUS")).equals("AUTHORIZED")) {
                    ClientUtil.showMessageWindow("SupplierID for this Customer Already Exist.(" + supplierID + ")");
                    return;
                } else {
                    ClientUtil.showMessageWindow("SupplierID already created for this Customer and is pending for Authorization.");
                    return;
                }
            }
            txtCustomerID.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            txtAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
            lblCustomerName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        } else if (viewType.equals("SUNDRY CREDITORS")) {
            txtSundryCreditors.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_ACCT_NUM")));
            lblSundryCreditorsName.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_NAME")));
        } else if (viewType.equals("PURCHASE")) {
            txtPurchase.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_ACCT_NUM")));
            lblPurchaseName.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_NAME")));
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(panSupplier, true);
                txtSupplierID.setEnabled(false);
                txtCustomerID.setEnabled(false);
                txtAddress.setEnabled(false);
                txtSundryCreditors.setEnabled(false);
                txtPurchase.setEnabled(false);
                btnSundryCreditors.setEnabled(true);
                btnPurchase.setEnabled(true);
            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            ClientUtil.enableDisable(panSupplier, false);
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
    private void saveAction(String status) {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                        && observable.getProxyReturnMap().containsKey("SUPPLIER_ID")) {
                    ClientUtil.showMessageWindow("SUPPLIER ID : " + observable.getProxyReturnMap().get("SUPPLIER_ID"));
                }
            }
        }
        btnCancelActionPerformed(null);
    }

    /* set the screen after the updation,insertion, deletion */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSupplier, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /* Does necessary operaion when user clicks the save button */
    private void savePerformed() {
        updateOBFields();
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            if (!chkActive.isSelected()) {
                HashMap activeMap = new HashMap();
                String act_num = txtSundryCreditors.getText();
                activeMap.put("SUSPENSE_ACCT_NUM", txtSundryCreditors.getText());
                List activeLst = ClientUtil.executeQuery("getSundryAcName", activeMap);
                if (activeLst != null && activeLst.size() > 0) {
                    activeMap = (HashMap) activeLst.get(0);
                    if (CommonUtil.convertObjToDouble(activeMap.get("AVAILABLE_BALANCE")) > 0) {
                        ClientUtil.showMessageWindow("Balance OutStanding  in this" + act_num + ".cannot deactivate Supplier ID");
                        return;
                    }
                }
            }
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }

        //__ Make the Screen Closable..
        setModified(false);
    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
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
        } else if (viewType.equals(AUTHORIZE)) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("TDS_ID", txtSupplierID.getText());
            ClientUtil.execute("authorizeTDSConfig", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
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

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
        panSupplier = new com.see.truetransact.uicomponent.CPanel();
        lblSupplierID = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lbTinNo = new com.see.truetransact.uicomponent.CLabel();
        lblphone = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        txtSupplierID = new com.see.truetransact.uicomponent.CTextField();
        txtTinNo = new com.see.truetransact.uicomponent.CTextField();
        txtPhone = new com.see.truetransact.uicomponent.CTextField();
        panCutOff = new com.see.truetransact.uicomponent.CPanel();
        panSundryCreditors = new com.see.truetransact.uicomponent.CPanel();
        txtPurchase = new com.see.truetransact.uicomponent.CTextField();
        btnPurchase = new com.see.truetransact.uicomponent.CButton();
        panName = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        panPurchase1 = new com.see.truetransact.uicomponent.CPanel();
        txtSundryCreditors = new com.see.truetransact.uicomponent.CTextField();
        btnSundryCreditors = new com.see.truetransact.uicomponent.CButton();
        lblCSTNO = new com.see.truetransact.uicomponent.CLabel();
        lblCutOfAmount2 = new com.see.truetransact.uicomponent.CLabel();
        txtCSTNO = new com.see.truetransact.uicomponent.CTextField();
        lbKGSTNO = new com.see.truetransact.uicomponent.CLabel();
        txtKGSTNO = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        lblPurchase = new com.see.truetransact.uicomponent.CLabel();
        lblSundryCreditors = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextArea();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseName = new com.see.truetransact.uicomponent.CLabel();
        lblSundryCreditorsName = new com.see.truetransact.uicomponent.CLabel();
        lblActive = new com.see.truetransact.uicomponent.CLabel();
        chkActive = new com.see.truetransact.uicomponent.CCheckBox();
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
        setMinimumSize(new java.awt.Dimension(590, 486));
        setPreferredSize(new java.awt.Dimension(590, 486));

        panSupplier.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSupplier.setLayout(new java.awt.GridBagLayout());

        lblSupplierID.setText("Supplier ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblSupplierID, gridBagConstraints);

        lblDate.setText(" Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblDate, gridBagConstraints);

        lbTinNo.setText("Tin No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lbTinNo, gridBagConstraints);

        lblphone.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblphone, gridBagConstraints);

        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(tdtDate, gridBagConstraints);

        txtSupplierID.setEditable(false);
        txtSupplierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(txtSupplierID, gridBagConstraints);

        txtTinNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTinNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(txtTinNo, gridBagConstraints);

        txtPhone.setAllowNumber(true);
        txtPhone.setMaxLength(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(txtPhone, gridBagConstraints);

        panCutOff.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSupplier.add(panCutOff, gridBagConstraints);

        panSundryCreditors.setMinimumSize(new java.awt.Dimension(130, 29));
        panSundryCreditors.setPreferredSize(new java.awt.Dimension(130, 29));
        panSundryCreditors.setLayout(new java.awt.GridBagLayout());

        txtPurchase.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSundryCreditors.add(txtPurchase, gridBagConstraints);

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSundryCreditors.add(btnPurchase, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panSupplier.add(panSundryCreditors, gridBagConstraints);

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
        panSupplier.add(panName, gridBagConstraints);

        panPurchase1.setMaximumSize(new java.awt.Dimension(130, 29));
        panPurchase1.setMinimumSize(new java.awt.Dimension(130, 29));
        panPurchase1.setPreferredSize(new java.awt.Dimension(130, 29));
        panPurchase1.setLayout(new java.awt.GridBagLayout());

        txtSundryCreditors.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSundryCreditors.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSundryCreditorsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchase1.add(txtSundryCreditors, gridBagConstraints);

        btnSundryCreditors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSundryCreditors.setEnabled(false);
        btnSundryCreditors.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSundryCreditors.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSundryCreditors.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSundryCreditors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSundryCreditorsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchase1.add(btnSundryCreditors, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panSupplier.add(panPurchase1, gridBagConstraints);

        lblCSTNO.setText("CST NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblCSTNO, gridBagConstraints);

        lblCutOfAmount2.setText("CST NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblCutOfAmount2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(txtCSTNO, gridBagConstraints);

        lbKGSTNO.setText("KGST NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lbKGSTNO, gridBagConstraints);

        txtKGSTNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKGSTNOActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(txtKGSTNO, gridBagConstraints);

        lblCustomerID.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCustomerID.setText("Customer ID");
        lblCustomerID.setMaximumSize(new java.awt.Dimension(80, 20));
        lblCustomerID.setMinimumSize(new java.awt.Dimension(80, 20));
        lblCustomerID.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panSupplier.add(lblCustomerID, gridBagConstraints);

        lblPurchase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPurchase.setText("Purchase");
        lblPurchase.setMaximumSize(new java.awt.Dimension(62, 20));
        lblPurchase.setMinimumSize(new java.awt.Dimension(62, 20));
        lblPurchase.setPreferredSize(new java.awt.Dimension(62, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panSupplier.add(lblPurchase, gridBagConstraints);

        lblSundryCreditors.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSundryCreditors.setText("Sundry Creditors");
        lblSundryCreditors.setMaximumSize(new java.awt.Dimension(102, 20));
        lblSundryCreditors.setMinimumSize(new java.awt.Dimension(102, 20));
        lblSundryCreditors.setPreferredSize(new java.awt.Dimension(102, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panSupplier.add(lblSundryCreditors, gridBagConstraints);

        txtAddress.setBackground(new java.awt.Color(240, 240, 240));
        txtAddress.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtAddress.setLineWrap(true);
        txtAddress.setMaximumSize(new java.awt.Dimension(240, 35));
        txtAddress.setMinimumSize(new java.awt.Dimension(240, 35));
        txtAddress.setPreferredSize(new java.awt.Dimension(240, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panSupplier.add(txtAddress, gridBagConstraints);

        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAddress.setText("Address");
        lblAddress.setMaximumSize(new java.awt.Dimension(42, 20));
        lblAddress.setMinimumSize(new java.awt.Dimension(52, 20));
        lblAddress.setPreferredSize(new java.awt.Dimension(42, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        panSupplier.add(lblAddress, gridBagConstraints);

        lblCustomerName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCustomerName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblCustomerName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSupplier.add(lblCustomerName, gridBagConstraints);

        lblPurchaseName.setForeground(new java.awt.Color(0, 51, 255));
        lblPurchaseName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblPurchaseName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblPurchaseName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblPurchaseName, gridBagConstraints);

        lblSundryCreditorsName.setForeground(new java.awt.Color(0, 51, 255));
        lblSundryCreditorsName.setMaximumSize(new java.awt.Dimension(200, 18));
        lblSundryCreditorsName.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSundryCreditorsName.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblSundryCreditorsName, gridBagConstraints);

        lblActive.setText("Active");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSupplier.add(lblActive, gridBagConstraints);

        chkActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSupplier.add(chkActive, gridBagConstraints);

        getContentPane().add(panSupplier, java.awt.BorderLayout.CENTER);

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
        ClientUtil.enableDisable(panSupplier, false);
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
            singleAuthorizeMap.put("SUPPLIER_ID", txtSupplierID.getText());
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
            mapParam.put(CommonConstants.MAP_NAME, "getTradingSupplierForAuthorize");
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

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        super.removeEditLock(txtSupplierID.getText());
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panSupplier, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setButtonEnable(false);
        lblCustomerName.setText("");
        lblSundryCreditorsName.setText("");
        lblPurchaseName.setText("");
        viewType = "";
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:  
        if (txtCustomerID.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Customer ID Should not be empty");
            return;
        } else if (txtSundryCreditors.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Sundry Creditors Should not be empty");
            return;
        } else {
            savePerformed();
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
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
        ClientUtil.enableDisable(panSupplier, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setButtonEnable(true);
        setButtonEnable(true);
        txtSupplierID.setEnabled(false);
        tdtDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSundryCreditorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSundryCreditorsActionPerformed
        // TODO add your handling code here:
        if (txtCustomerID.getText() != null && txtCustomerID.getText().length() > 0) {
            callView("SUNDRY CREDITORS");
        } else {
            ClientUtil.showMessageWindow("Please Enter Customer ID ...!");
            return;
        }
    }//GEN-LAST:event_btnSundryCreditorsActionPerformed

    private void txtSundryCreditorsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSundryCreditorsFocusLost
        // TODO add your handling code here:
        if (txtSundryCreditors.getText() != null && txtSundryCreditors.getText().length() > 0) {
            HashMap sundryMap = new HashMap();
            sundryMap.put("SUSPENSE_ACCT_NUM", txtSundryCreditors.getText());
            List sundryLst = ClientUtil.executeQuery("getSundryAcName", sundryMap);
            if (sundryLst != null && sundryLst.size() > 0) {
                sundryMap = (HashMap) sundryLst.get(0);
                lblSundryCreditorsName.setText(CommonUtil.convertObjToStr(sundryMap.get("SUSPENSE_NAME")));
                txtCustomerID.setText(CommonUtil.convertObjToStr(sundryMap.get("CUSTOMER_ID")));
                if (txtCustomerID.getText() != null && txtCustomerID.getText().length() > 0) {
                    showingCustomerDetails(txtCustomerID.getText());
                }
            } else {
                ClientUtil.showMessageWindow("Invalid Suspense Account Number !!!");
                txtSundryCreditors.setText("");
            }
        }
    }//GEN-LAST:event_txtSundryCreditorsFocusLost

    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIdActionPerformed

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
        List custLst = ClientUtil.executeQuery("getExistCustList", customerMap);
        if (custLst != null && custLst.size() > 0) {
            customerMap = (HashMap) custLst.get(0);
            supplierID = CommonUtil.convertObjToStr(customerMap.get("SUPPLIERID"));
            if (CommonUtil.convertObjToStr(customerMap.get("AUTHORIZED_STATUS")).equals("AUTHORIZED")) {
                ClientUtil.showMessageWindow("SupplierID for this Customer Already Exist.(" + supplierID + ")");
                txtSundryCreditors.setText("");
                lblSundryCreditorsName.setText("");
                return;
            } else {
                ClientUtil.showMessageWindow("SupplierID already created for this Customer and is pending for Authorization.");
                txtSundryCreditors.setText("");
                lblSundryCreditorsName.setText("");
                return;
            }
        }
        List custListData = ClientUtil.executeQuery("getSelectCustDetails", customerMap);
        if (custListData != null && custListData.size() > 0) {
            customerMap = (HashMap) custListData.get(0);
            lblCustomerName.setText(CommonUtil.convertObjToStr(customerMap.get("NAME")));
            txtAddress.setText(CommonUtil.convertObjToStr(customerMap.get("ADDRESS")));
        }
    }

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        if (txtCustomerID.getText() != null && txtCustomerID.getText().length() > 0) {
            callView("PURCHASE");
        } else {
            ClientUtil.showMessageWindow("Please Enter Customer ID ...!");
            return;
        }
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void txtPurchaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseFocusLost
        // TODO add your handling code here:
        if (txtPurchase.getText().length() > 0) {
            HashMap sundryMap = new HashMap();
            sundryMap.put("SUSPENSE_ACCT_NUM", txtSundryCreditors.getText());
            List sundryLst = ClientUtil.executeQuery("getSundryAcName", sundryMap);
            if (sundryLst != null && sundryLst.size() > 0) {
                sundryMap = (HashMap) sundryLst.get(0);
                lblPurchaseName.setText(CommonUtil.convertObjToStr(sundryMap.get("SUSPENSE_NAME")));
                txtCustomerID.setText(CommonUtil.convertObjToStr(sundryMap.get("CUSTOMER_ID")));
                if (txtCustomerID.getText() != null && txtCustomerID.getText().length() > 0) {
                    showingCustomerDetails(txtCustomerID.getText());
                }
            } else {
                ClientUtil.showMessageWindow("Invalid Suspense Account Number !!!");
                txtPurchase.setText("");
            }
        }
    }//GEN-LAST:event_txtPurchaseFocusLost

    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtDateFocusLost

    private void txtSupplierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierIDActionPerformed

    private void txtKGSTNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKGSTNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKGSTNOActionPerformed

    private void txtTinNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTinNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTinNoActionPerformed

    private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerIDActionPerformed

    private void chkActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActiveActionPerformed
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPurchase;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSundryCreditors;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CCheckBox chkActive;
    private com.see.truetransact.uicomponent.CLabel lbKGSTNO;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbTinNo;
    private com.see.truetransact.uicomponent.CLabel lblActive;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblCSTNO;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCutOfAmount2;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPurchase;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseName;
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
    private com.see.truetransact.uicomponent.CLabel lblSundryCreditors;
    private com.see.truetransact.uicomponent.CLabel lblSundryCreditorsName;
    private com.see.truetransact.uicomponent.CLabel lblSupplierID;
    private com.see.truetransact.uicomponent.CLabel lblphone;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCutOff;
    private com.see.truetransact.uicomponent.CPanel panName;
    private com.see.truetransact.uicomponent.CPanel panPurchase1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSundryCreditors;
    private com.see.truetransact.uicomponent.CPanel panSupplier;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextArea txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtCSTNO;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtKGSTNO;
    private com.see.truetransact.uicomponent.CTextField txtPhone;
    private com.see.truetransact.uicomponent.CTextField txtPurchase;
    private com.see.truetransact.uicomponent.CTextField txtSundryCreditors;
    private com.see.truetransact.uicomponent.CTextField txtSupplierID;
    private com.see.truetransact.uicomponent.CTextField txtTinNo;
    // End of variables declaration//GEN-END:variables
}
