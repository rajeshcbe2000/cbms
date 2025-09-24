/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShopMasterUI.java
 *
 * Created on January 31, 2005, 2:59 PM
 */
package com.see.truetransact.ui.trading.shopmaster;

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
import java.util.*;
import com.see.truetransact.clientutil.ComboBoxModel;

public class ShopMasterUI extends CInternalFrame implements Observer, UIMandatoryField {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.shopmaster.ShopMasterRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private ShopMasterMRB objMandatoryRB = new ShopMasterMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private ShopMasterOB observable;
    int viewType = -1;
    boolean isFilled = false;
    int btnType = -1;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 100, VIEW = 200;
    final int CashAccountHead = 2, TransAccountHead = 3, KVATAccountHead = 4;

    public ShopMasterUI() {
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
        //setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panCustomerDetails, false);
        setButtonEnableDisable();
        setenableData();
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
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
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrTDSConfig.setName("mbrTDSConfig");
        panStatus.setName("panStatus");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
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

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
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

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaxLengths() {
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = ShopMasterOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Setting model to the combobox cboScope
     */
    private void initComponentData() {
        try {
            cboBranchID.setModel(observable.getCbmBranchID());
            cboPlace.setModel(observable.getCbmPlace());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Makes the button Enable or Disable accordingly when usier clicks new,edit
     * or delete buttons
     */
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

    private void setenableData() {
        txtCashAcHead.setEnabled(false);
        txtKvatAcHead.setEnabled(false);
        txtTransAcHead.setEnabled(false);
        btnCashAcHead.setEnabled(false);
        btnTransAcHead.setEnabled(false);
        btnKvatAcHead.setEnabled(false);
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtStoreID(txtStoreID.getText());
        observable.setTxtName(txtName.getText());
        observable.setCboBranchID((String) cboBranchID.getSelectedItem());
        observable.setCboPlace((String) cboPlace.getSelectedItem());
        observable.setChkActive(chkActive.isSelected());
        observable.setTxtCasAcHead(txtCashAcHead.getText());
        observable.setTxtTransAcHead(txtTransAcHead.getText());
        observable.setTxtKvatAcHead(txtKvatAcHead.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());

    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        txtStoreID.setText(observable.getTxtStoreID());
        txtName.setText(observable.getTxtName());
        cboPlace.setSelectedItem(observable.getCboPlace());
        cboBranchID.setSelectedItem(observable.getCboBranchID());
        //chkActive.setSelected(observable.isChkActive());
        txtCashAcHead.setText(observable.getTxtCasAcHead());
        if (!txtCashAcHead.getText().equals("")) {
            lblCashAcHdDesc.setText(getAccHeadDesc(observable.getTxtCasAcHead()));
            chkActive.setSelected(true);
        }
        txtTransAcHead.setText(observable.getTxtTransAcHead());
        if (!txtTransAcHead.getText().equals("")) {
            lblTransAcHdDesc.setText(getAccHeadDesc(observable.getTxtTransAcHead()));
        }
        txtKvatAcHead.setText(observable.getTxtKvatAcHead());
        if (!txtKvatAcHead.getText().equals("")) {
            lblKvatAcHdDesc.setText(getAccHeadDesc(observable.getTxtKvatAcHead()));
        }
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


    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (viewType == CashAccountHead) {
                txtCashAcHead.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
                lblCashAcHdDesc.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_DESC")));
            } else if (viewType == TransAccountHead) {
                txtTransAcHead.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
                lblTransAcHdDesc.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_DESC")));
            } else if (viewType == KVATAccountHead) {
                txtKvatAcHead.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
                lblKvatAcHdDesc.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_DESC")));
            }
            if (viewType == EDIT || viewType == DELETE || viewType == VIEW) {
                setButtonEnableDisable();
                observable.getData(hashMap);
                update();
                if(viewType == EDIT){
                    if(chkActive.isSelected()==true){
                      btnCashAcHead.setEnabled(true);  
                    }
                    btnTransAcHead.setEnabled(true);
                    btnKvatAcHead.setEnabled(true);
                }
            } else if (viewType == AUTHORIZE) {
                observable.getData(hashMap);
                update();
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
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

    }

    /*
     * Calls the execute method of TDSConfigOB to do insertion or updation or
     * deletion
     */
    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);

        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                        && observable.getProxyReturnMap().containsKey("SHOP_MASTER_ID")) {
                    ClientUtil.showMessageWindow("SHOP MASTER ID : " + observable.getProxyReturnMap().get("SHOP_MASTER_ID"));
                    btnCancelActionPerformed(null);
                    btnCancel.setEnabled(true);
                    lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                }
            }
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("SHOP_MASTER_ID", observable.getTxtStoreID());
            singleAuthorizeMap.put("SHOP_MASTER__NAME", observable.getTxtName());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = 0;
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
            mapParam.put(CommonConstants.MAP_NAME, "getShopMasterAuthorize");
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
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
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
        panShopMaster = new com.see.truetransact.uicomponent.CPanel();
        panCustomerDetails = new com.see.truetransact.uicomponent.CPanel();
        lblStoreID = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblPlace = new com.see.truetransact.uicomponent.CLabel();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        txtStoreID = new com.see.truetransact.uicomponent.CTextField();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        cboBranchID = new com.see.truetransact.uicomponent.CComboBox();
        lblCashACHead = new com.see.truetransact.uicomponent.CLabel();
        lblActive = new com.see.truetransact.uicomponent.CLabel();
        lblTransACHead = new com.see.truetransact.uicomponent.CLabel();
        lblKVATACHead = new com.see.truetransact.uicomponent.CLabel();
        chkActive = new com.see.truetransact.uicomponent.CCheckBox();
        lblBranchIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCashAcHdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblKvatAcHdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTransAcHdDesc = new com.see.truetransact.uicomponent.CLabel();
        cboPlace = new com.see.truetransact.uicomponent.CComboBox();
        txtTransAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnKvatAcHead = new com.see.truetransact.uicomponent.CButton();
        txtKvatAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnTransAcHead = new com.see.truetransact.uicomponent.CButton();
        txtCashAcHead = new com.see.truetransact.uicomponent.CTextField();
        btnCashAcHead = new com.see.truetransact.uicomponent.CButton();
        tbrShopMaster = new com.see.truetransact.uicomponent.CToolBar();
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
        setMinimumSize(new java.awt.Dimension(750, 588));
        setPreferredSize(new java.awt.Dimension(750, 588));

        panShopMaster.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShopMaster.setMinimumSize(new java.awt.Dimension(850, 308));
        panShopMaster.setPreferredSize(new java.awt.Dimension(850, 308));
        panShopMaster.setLayout(new java.awt.GridBagLayout());

        panCustomerDetails.setMinimumSize(new java.awt.Dimension(830, 190));
        panCustomerDetails.setPreferredSize(new java.awt.Dimension(830, 190));
        panCustomerDetails.setLayout(new java.awt.GridBagLayout());

        lblStoreID.setText("Store ID");
        lblStoreID.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblStoreID, gridBagConstraints);

        lblName.setText("Name");
        lblName.setMaximumSize(new java.awt.Dimension(37, 14));
        lblName.setMinimumSize(new java.awt.Dimension(37, 14));
        lblName.setPreferredSize(new java.awt.Dimension(37, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblName, gridBagConstraints);

        lblPlace.setText("Place");
        lblPlace.setMaximumSize(new java.awt.Dimension(32, 14));
        lblPlace.setMinimumSize(new java.awt.Dimension(32, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblPlace, gridBagConstraints);

        lblBranchID.setText("Branch ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblBranchID, gridBagConstraints);

        txtStoreID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtStoreID, gridBagConstraints);

        txtName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtName.setMinimumSize(new java.awt.Dimension(250, 21));
        txtName.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtName, gridBagConstraints);

        cboBranchID.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranchID.setPreferredSize(new java.awt.Dimension(200, 21));
        cboBranchID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(cboBranchID, gridBagConstraints);

        lblCashACHead.setText("Cash AC Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblCashACHead, gridBagConstraints);

        lblActive.setText("Active");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblActive, gridBagConstraints);

        lblTransACHead.setText("Trans AC Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblTransACHead, gridBagConstraints);

        lblKVATACHead.setText("KVAT AC Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblKVATACHead, gridBagConstraints);

        chkActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panCustomerDetails.add(chkActive, gridBagConstraints);

        lblBranchIdDesc.setForeground(new java.awt.Color(0, 51, 255));
        lblBranchIdDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBranchIdDesc.setMaximumSize(new java.awt.Dimension(300, 18));
        lblBranchIdDesc.setMinimumSize(new java.awt.Dimension(300, 18));
        lblBranchIdDesc.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCustomerDetails.add(lblBranchIdDesc, gridBagConstraints);

        lblCashAcHdDesc.setForeground(new java.awt.Color(0, 51, 255));
        lblCashAcHdDesc.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCashAcHdDesc.setMinimumSize(new java.awt.Dimension(200, 18));
        lblCashAcHdDesc.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblCashAcHdDesc, gridBagConstraints);

        lblKvatAcHdDesc.setForeground(new java.awt.Color(0, 51, 255));
        lblKvatAcHdDesc.setMaximumSize(new java.awt.Dimension(200, 18));
        lblKvatAcHdDesc.setMinimumSize(new java.awt.Dimension(200, 18));
        lblKvatAcHdDesc.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblKvatAcHdDesc, gridBagConstraints);

        lblTransAcHdDesc.setForeground(new java.awt.Color(0, 51, 255));
        lblTransAcHdDesc.setMaximumSize(new java.awt.Dimension(200, 18));
        lblTransAcHdDesc.setMinimumSize(new java.awt.Dimension(200, 18));
        lblTransAcHdDesc.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(lblTransAcHdDesc, gridBagConstraints);

        cboPlace.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(cboPlace, gridBagConstraints);

        txtTransAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtTransAcHead, gridBagConstraints);

        btnKvatAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnKvatAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnKvatAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnKvatAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKvatAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panCustomerDetails.add(btnKvatAcHead, gridBagConstraints);

        txtKvatAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKvatAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtKvatAcHead, gridBagConstraints);

        btnTransAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTransAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panCustomerDetails.add(btnTransAcHead, gridBagConstraints);

        txtCashAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetails.add(txtCashAcHead, gridBagConstraints);

        btnCashAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCashAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCashAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCashAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panCustomerDetails.add(btnCashAcHead, gridBagConstraints);

        panShopMaster.add(panCustomerDetails, new java.awt.GridBagConstraints());

        getContentPane().add(panShopMaster, java.awt.BorderLayout.CENTER);

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
        tbrShopMaster.add(btnView);

        lblSpace4.setText("     ");
        tbrShopMaster.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnDelete);

        lbSpace2.setText("     ");
        tbrShopMaster.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShopMaster.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnReject);

        lblSpace5.setText("     ");
        tbrShopMaster.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShopMaster.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShopMaster.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShopMaster.add(btnClose);

        getContentPane().add(tbrShopMaster, java.awt.BorderLayout.NORTH);

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
        popUp(VIEW);
        ClientUtil.enableDisable(panCustomerDetails, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
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
//        btnEditActionPerformed(evt);
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
        observable.resetForm();
        ClientUtil.clearAll(this);
        lblCashAcHdDesc.setText("");
        lblTransAcHdDesc.setText("");
        lblKvatAcHdDesc.setText("");
        lblBranchIdDesc.setText("");
        btnCashAcHead.setEnabled(false);
        btnTransAcHead.setEnabled(false);
        btnKvatAcHead.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panCustomerDetails, false);
        viewType = 0;
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblStatus.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (chkActive.isSelected() == true && txtCashAcHead.getText() == null && txtCashAcHead.getText().length() >= 0) {
            ClientUtil.showMessageWindow("CashAcHead Should not be empty");
            return;
        } else if (txtName.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Name Should not be empty");
        } else if (CommonUtil.convertObjToStr(cboPlace.getSelectedItem()).length() <= 0) {
            ClientUtil.showMessageWindow("Place Should not be empty");
        } else if (CommonUtil.convertObjToStr(cboBranchID.getSelectedItem()).length() <= 0) {
            ClientUtil.showMessageWindow("Branch ID Should not be empty");
        } else {
            updateOBFields();
            savePerformed();
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
            setModified(false);
            ClientUtil.clearAll(this);
            ClientUtil.enableDisable(panCustomerDetails, false);
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panCustomerDetails, true);
        if (txtCashAcHead.getText().length() > 0) {
            txtCashAcHead.setEnabled(true);
            chkActive.setSelected(true);
        } else {
            txtCashAcHead.setEnabled(false);
            chkActive.setSelected(false);
        }
        txtStoreID.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void popUp(int field) {
        HashMap viewMap = new HashMap();
        viewType = field;
        if (field == EDIT) {
            HashMap map = new HashMap();
            //map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getShopEdit");
        } else if (field == VIEW) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getShopMasterView");
        } else if (field == DELETE) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getShopDelete");
        } else {
            updateOBFields();
            HashMap whereMap = new HashMap();

            if (viewType == CashAccountHead) {
            } else if (viewType == TransAccountHead) {
            } else if (viewType == KVATAccountHead) {
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectActHdId");
        }
        new ViewAll(this, viewMap).show();
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panCustomerDetails, true);
        ClientUtil.clearAll(this);
        txtCashAcHead.setEnabled(false);
        btnCashAcHead.setEnabled(false);
        txtStoreID.setEnabled(false);
        btnTransAcHead.setEnabled(true);
        btnKvatAcHead.setEnabled(true);
        lblStatus.setText("New");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnNewActionPerformed

    private void chkActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActiveActionPerformed
        // TODO add your handling code here:
        if (chkActive.isSelected()) {
            txtCashAcHead.setEnabled(true);
            btnCashAcHead.setEnabled(true);
        } else {
            txtCashAcHead.setEnabled(false);
            txtCashAcHead.setText("");
            lblCashAcHdDesc.setText("");
            btnCashAcHead.setEnabled(false);
        }
    }//GEN-LAST:event_chkActiveActionPerformed

    private void txtTransAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransAcHeadFocusLost
    }//GEN-LAST:event_txtTransAcHeadFocusLost

    private void btnKvatAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKvatAcHeadActionPerformed
        // TODO add your handling code here
        popUp(KVATAccountHead);
    }//GEN-LAST:event_btnKvatAcHeadActionPerformed

    private void txtKvatAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKvatAcHeadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKvatAcHeadFocusLost

    private void btnTransAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransAcHeadActionPerformed
        // TODO add your handling code here:
        popUp(TransAccountHead);
    }//GEN-LAST:event_btnTransAcHeadActionPerformed

    private void txtCashAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashAcHeadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCashAcHeadFocusLost

    private void btnCashAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashAcHeadActionPerformed
        // TODO add your handling code here:
        popUp(CashAccountHead);
    }//GEN-LAST:event_btnCashAcHeadActionPerformed

    private void cboBranchIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchIDActionPerformed
        // TODO add your handling code here:
        if (cboBranchID.getSelectedIndex() > 0) {
            String desc = CommonUtil.convertObjToStr(cboBranchID.getSelectedItem());
            // String desc = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchID.getModel()).getKeyForSelected());
            if (desc.indexOf("-") != -1) {
                desc = desc.substring(desc.indexOf("-") + 1, desc.length());
                lblBranchIdDesc.setText(desc);
            }
        }
    }//GEN-LAST:event_cboBranchIDActionPerformed
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

    private void callView(int viewType) {
        btnType = viewType;
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSelectActHdId");
        new ViewAll(this, viewMap).show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCashAcHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnKvatAcHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTransAcHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchID;
    private com.see.truetransact.uicomponent.CComboBox cboPlace;
    private com.see.truetransact.uicomponent.CCheckBox chkActive;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblActive;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblBranchIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblCashACHead;
    private com.see.truetransact.uicomponent.CLabel lblCashAcHdDesc;
    private com.see.truetransact.uicomponent.CLabel lblKVATACHead;
    private com.see.truetransact.uicomponent.CLabel lblKvatAcHdDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblPlace;
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
    private com.see.truetransact.uicomponent.CLabel lblStoreID;
    private com.see.truetransact.uicomponent.CLabel lblTransACHead;
    private com.see.truetransact.uicomponent.CLabel lblTransAcHdDesc;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetails;
    private com.see.truetransact.uicomponent.CPanel panShopMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrShopMaster;
    private com.see.truetransact.uicomponent.CTextField txtCashAcHead;
    private com.see.truetransact.uicomponent.CTextField txtKvatAcHead;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtStoreID;
    private com.see.truetransact.uicomponent.CTextField txtTransAcHead;
    // End of variables declaration//GEN-END:variables
}
