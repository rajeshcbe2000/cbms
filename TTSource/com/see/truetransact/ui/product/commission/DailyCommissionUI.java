/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DailyCommissionUI.java
 */
package com.see.truetransact.ui.product.commission;
/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 15-06-2015
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
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;

public class DailyCommissionUI extends CInternalFrame implements UIMandatoryField, Observer {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.commission.DailyCommissionRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private DailyCommissionOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    private String commGenId ="",roiGenId ="";
    ///final int ROITYPE = 5;
    /**
     * Creates new form TDSConfigUI
     */
    public DailyCommissionUI() {
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
        ClientUtil.enableDisable(panTDS, false);
        setButtonEnableDisable();
        tblDetails.setModel(observable.getTblDetails());
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
        cboCommTypeVal.setName("cboCommTypeVal");
        lbSpace2.setName("lbSpace2");
        lblPeriod.setName("lblPeriod");
        lblMsg.setName("lblMsg");
        lblAmt.setName("lblAmt");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblCommId.setName("lblCommId");
        mbrTDSConfig.setName("mbrTDSConfig");
        panStatus.setName("panStatus");
        panTDS.setName("panTDS");
        txtAmt.setName("txtAmt");
        txtTo.setName("txtTo");
        txtCommId.setName("txtCommId");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblCommId.setText(resourceBundle.getString("lblCommId"));
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
        lblPeriod.setText(resourceBundle.getString("lblPeriod"));
        lblAmt.setText(resourceBundle.getString("lblAmt"));
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
        mandatoryMap.put("cboCommTypeVal", new Boolean(true));
        mandatoryMap.put("txtAmt", new Boolean(true));
        mandatoryMap.put("txtFrom", new Boolean(true));
        mandatoryMap.put("txtTo", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboCustTypeVal", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    /**
     * This method sets the Maximum allowed lenght to the textfields *
     */
    private void setMaxLengths() {
        txtAmt.setMaxLength(16);
        txtTo.setMaxLength(8);
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = DailyCommissionOB.getInstance();
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
            cboProductType.setModel(observable.getCbmProductType());
            //  cboCommTypeVal.setModel(observable.getCbmCommType());
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
//        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtFrom(txtFrom.getText());
        observable.setTxtPenal(txtPenal.getText());
        observable.setTxtCommId(txtCommId.getText());
        observable.setTxtTo(txtTo.getText());
        observable.setCboProductType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()));
        observable.setCboCommTypeVal(CommonUtil.convertObjToStr(cboCommTypeVal.getSelectedItem()));
        observable.setTxtAmt(txtAmt.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtCommId(txtCommId.getText());
        if(rdoCommission.isSelected()){
           observable.setType("COMMISSION"); 
        }
        if(rdoRoi.isSelected()){
           observable.setType("ROI"); 
        }
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        txtFrom.setText(observable.getTxtFrom());
        txtAmt.setText(observable.getTxtAmt());
        txtCommId.setText(observable.getTxtCommId());
        txtPenal.setText(observable.getTxtPenal());
        cboProductType.setSelectedItem(observable.getCboProductType());
        cboCommTypeVal.setSelectedItem(observable.getCboCommTypeVal());
        txtTo.setText(observable.getTxtTo());

    }

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currField) {
        viewType = currField;
//        HashMap viewMap = new HashMap();
//        ArrayList lst = new ArrayList();
//        lst.add("TDS_ID");
//        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
//        lst = null;
//        viewMap.put(CommonConstants.MAP_NAME, "getSelectTDSConfig");
//        new ViewAll(this,viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals("COMMACT")) {
                if (rdoCommission.isSelected()) {
                    hash.put("PROCESS_TYPE", "COMMISSION");
                } else if (rdoRoi.isSelected()) {
                    hash.put("PROCESS_TYPE", "ROI");
                }
                hash.put("ACTION", "NEW");
                System.out.println("Hash......... :" + hash);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    if (rdoCommission.isSelected() && (commGenId == null || commGenId.length() <= 0)) {
                        observable.populateData(hash);
                           commGenId = observable.getTxtCommId();
                    } else if (rdoRoi.isSelected() && (roiGenId == null || roiGenId.length() <= 0)) {
                        observable.populateData(hash);
                           roiGenId = observable.getTxtCommId();
                    }
                    if (rdoCommission.isSelected()) {
                      observable.setTxtCommId(commGenId); 
                    } else if (rdoRoi.isSelected()) {
                       observable.setTxtCommId(roiGenId);
                    }
                }
            }
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                    || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("COMM_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                if(rdoCommission.isSelected()){
                    hash.put("PROCESS_TYPE","COMMISSION");
                }else if(rdoRoi.isSelected()){
                    hash.put("PROCESS_TYPE","ROI");
                }
                observable.populateData(hash);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panTDS, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panTDS, true);
                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panTDS, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        } else {
        }
//        
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
        cmd.show();
    }

    /*
     * Calls the execute method of TDSConfigOB to do insertion or updation or
     * deletion
     */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        String prdtType = CommonUtil.convertObjToStr(cboProductType.getSelectedItem());
        if (prdtType == null || prdtType.length() <= 0) {
            ClientUtil.displayAlert("Enter the product Type");
            return;
        }
        if (observable.getType() == null || observable.getType().length() <= 0) {
            ClientUtil.displayAlert("Select the Type");
            return;
        }
        int rowcount = tblDetails.getRowCount();
        if (rowcount <= 0) {
            ClientUtil.displayAlert("Enter the details");
            return;
        }
        observable.execute(status);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            super.removeEditLock(txtCommId.getText());
            settings();
        }
    }

    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTDS, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        updateOBFields();
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
            singleAuthorizeMap.put("TDS_ID", txtCommId.getText());
            ClientUtil.execute("authorizeTDSConfig", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }

    /**
     * Checks up whether the userentered Date already exists in the DB if so ,
     * giving alertmessage and clearing up the DateField, so that user can make
     * other entry *
     */
    /**
     * Checks up whether the userentered Date already exists in the DB if so ,
     * giving alertmessage and clearing up the DateField, so that user can make
     * other entry *
     */
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
        panTDS = new com.see.truetransact.uicomponent.CPanel();
        lblCommId = new com.see.truetransact.uicomponent.CLabel();
        txtCommId = new com.see.truetransact.uicomponent.CTextField();
        panDet = new com.see.truetransact.uicomponent.CPanel();
        txtFrom = new com.see.truetransact.uicomponent.CTextField();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtTo = new com.see.truetransact.uicomponent.CTextField();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPenal = new com.see.truetransact.uicomponent.CLabel();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPenal = new com.see.truetransact.uicomponent.CTextField();
        panbtnTxDetails = new com.see.truetransact.uicomponent.CPanel();
        btnNewTxDetails = new com.see.truetransact.uicomponent.CButton();
        btnSaveTxDetails = new com.see.truetransact.uicomponent.CButton();
        btnDeleteTxDetails = new com.see.truetransact.uicomponent.CButton();
        cboCommTypeVal = new com.see.truetransact.uicomponent.CComboBox();
        lblcommType = new com.see.truetransact.uicomponent.CLabel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblDetails = new com.see.truetransact.uicomponent.CTable();
        rdoCommission = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRoi = new com.see.truetransact.uicomponent.CRadioButton();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        tbrTDSConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(897, 319));

        panTDS.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTDS.setLayout(new java.awt.GridBagLayout());

        lblCommId.setText("Commission ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(lblCommId, gridBagConstraints);

        txtCommId.setEditable(false);
        txtCommId.setAllowAll(true);
        txtCommId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(txtCommId, gridBagConstraints);

        panDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        panDet.setLayout(new java.awt.GridBagLayout());

        txtFrom.setAllowAll(true);
        txtFrom.setAllowNumber(true);
        txtFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(txtFrom, gridBagConstraints);

        lblPeriod.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(lblPeriod, gridBagConstraints);

        txtTo.setAllowAll(true);
        txtTo.setAllowNumber(true);
        txtTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTo.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(txtTo, gridBagConstraints);

        txtAmt.setAllowAll(true);
        txtAmt.setAllowNumber(true);
        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(txtAmt, gridBagConstraints);

        lblPenal.setText("Penal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(lblPenal, gridBagConstraints);

        lblAmt.setText("Roi");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(lblAmt, gridBagConstraints);

        txtPenal.setAllowAll(true);
        txtPenal.setAllowNumber(true);
        txtPenal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDet.add(txtPenal, gridBagConstraints);

        panbtnTxDetails.setMinimumSize(new java.awt.Dimension(127, 30));
        panbtnTxDetails.setPreferredSize(new java.awt.Dimension(127, 30));
        panbtnTxDetails.setLayout(new java.awt.GridBagLayout());

        btnNewTxDetails.setText("New");
        btnNewTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnNewTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTxDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnNewTxDetails, gridBagConstraints);

        btnSaveTxDetails.setText("Save");
        btnSaveTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnSaveTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTxDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnSaveTxDetails, gridBagConstraints);

        btnDeleteTxDetails.setText("Delete");
        btnDeleteTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnDeleteTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTxDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnDeleteTxDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDet.add(panbtnTxDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        panTDS.add(panDet, gridBagConstraints);

        cboCommTypeVal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months" }));
        cboCommTypeVal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(cboCommTypeVal, gridBagConstraints);

        lblcommType.setText("Commission Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(lblcommType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(lblProductType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductTypeItemStateChanged(evt);
            }
        });
        cboProductType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(cboProductType, gridBagConstraints);

        srpTransDetails.setMinimumSize(new java.awt.Dimension(285, 80));

        tblDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailsMouseClicked(evt);
            }
        });
        srpTransDetails.setViewportView(tblDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTDS.add(srpTransDetails, gridBagConstraints);

        rdoCommission.setText("Commission");
        rdoCommission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCommissionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panTDS.add(rdoCommission, gridBagConstraints);

        rdoRoi.setText("ROI");
        rdoRoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRoiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        panTDS.add(rdoRoi, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panTDS.add(lblType, gridBagConstraints);

        getContentPane().add(panTDS, java.awt.BorderLayout.CENTER);

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace21);

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

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace22);

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
        //  TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
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
        //observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        //  observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
     //   authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        //  observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
       // authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void resetObjects() {
        observable.resetObjects();
        tblDetails.setModel(observable.getTblDetails());
        update(null, null);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        super.removeEditLock(txtCommId.getText());
        observable.resetObjects();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTDS, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        commGenId="";
        roiGenId = "";
        resetObjects();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void enableDisableDetails() {
        txtFrom.setText("");
        txtTo.setText("");
        txtAmt.setText("");
        txtPenal.setText("");
        btnSaveTxDetails.setEnabled(false);
        btnDeleteTxDetails.setEnabled(false);
        txtFrom.setEnabled(false);
        txtTo.setEnabled(false);
        txtAmt.setEnabled(false);
        txtPenal.setEnabled(false);

    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnCancelActionPerformed(null);
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
     //   callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
         ClientUtil.enableDisable(panTDS, true);
        enableDisableDetails();
        setButtonEnableDisable();
        setModified(true);
        txtCommId.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        ClientUtil.enableDisable(panTDS, true);
        enableDisableDetails();
        setButtonEnableDisable();
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtFrom.setEnabled(false);
        commGenId="";
        roiGenId = "";
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTxDetailsActionPerformed
      if(validateTableData()){
        updateOBFields();
        observable.saveTransactionDetails(false, 0, false, tblDetails.getRowCount());
        observable.resetTransactionDetails();
        enableDisableDetails();
      }else{
          return;
      }
    }//GEN-LAST:event_btnSaveTxDetailsActionPerformed
    public boolean validateTableData() {

        int c = tblDetails.getRowCount();
        int frmTxt = CommonUtil.convertObjToInt(txtFrom.getText());
        int toTxt = CommonUtil.convertObjToInt(txtTo.getText());
        if (frmTxt > toTxt) {
            ClientUtil.displayAlert("To Period Should be grater than from Period");
            return false;
        }
        if (txtAmt.getText() == null || CommonUtil.convertObjToDouble(txtAmt.getText()) <= 0) {
            ClientUtil.displayAlert("Enter amount");
            return false;
        }
        if (c > 0) {
            System.out.println(" ccc :" + c);
            int from = CommonUtil.convertObjToInt(tblDetails.getValueAt(c - 1, 1));
            System.out.println("from :" + from);
            int to = CommonUtil.convertObjToInt(tblDetails.getValueAt(c - 1, 2));
            System.out.println("to :" + to);
            if (to >= frmTxt) {
                ClientUtil.displayAlert("OverLaping is not allowed!!");
                return false;
            }
        }
        return true;
    }
    private void btnDeleteTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTxDetailsActionPerformed
        if (tblDetails.getSelectedRow() >= 0) {
            observable.deleteTransDetails(tblDetails.getSelectedRow());
            observable.resetTransactionDetails();
            enableDisableDetails();
        }
    }//GEN-LAST:event_btnDeleteTxDetailsActionPerformed

    private void btnNewTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTxDetailsActionPerformed
        observable.resetTransactionDetails();
        txtFrom.setEnabled(true);
        txtTo.setEnabled(true);
        txtAmt.setEnabled(true);
        txtPenal.setEnabled(true);
        btnSaveTxDetails.setEnabled(true);
        btnDeleteTxDetails.setEnabled(true);
    }//GEN-LAST:event_btnNewTxDetailsActionPerformed

    private void rdoCommissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommissionActionPerformed
        if (rdoCommission.isSelected()) {
            rdoRoi.setSelected(false);
                viewType = "COMMACT";
                fillData(new HashMap());
                update(null, null);
        }
    }//GEN-LAST:event_rdoCommissionActionPerformed

    private void rdoRoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRoiActionPerformed
        if (rdoRoi.isSelected()) {
            rdoCommission.setSelected(false);
                viewType = "COMMACT";
                fillData(new HashMap());
                update(null, null);
        }
    }//GEN-LAST:event_rdoRoiActionPerformed

    private void cboProductTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductTypeItemStateChanged

    }//GEN-LAST:event_cboProductTypeItemStateChanged

    private void cboProductTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductTypeFocusLost
       if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && cboProductType.getSelectedItem()!=null && CommonUtil.convertObjToStr(cboProductType.getSelectedItem()).length()>0){
            HashMap  whereMap = new HashMap();
            whereMap.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()));
            whereMap.put("PERIOD_IN",CommonUtil.convertObjToStr(cboCommTypeVal.getSelectedItem()));
              if(rdoCommission.isSelected()){
                    whereMap.put("PROCESS_TYPE","COMMISSION");
                }else if(rdoRoi.isSelected()){
                    whereMap.put("PROCESS_TYPE","ROI");
                }
              whereMap.put("GET_DATA","GET_DATA");
              observable.setPopulatedData(whereMap);
               txtCommId.setText(observable.getTxtCommId());
        }
    }//GEN-LAST:event_cboProductTypeFocusLost

    private void tblDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailsMouseClicked
      btnDeleteTxDetails.setEnabled(true);
    }//GEN-LAST:event_tblDetailsMouseClicked

    private void txtCommIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommIdActionPerformed
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
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteTxDetails;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewTxDetails;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveTxDetails;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCommTypeVal;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblCommId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPenal;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CLabel lblcommType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDet;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTDS;
    private com.see.truetransact.uicomponent.CPanel panbtnTxDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommission;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private com.see.truetransact.uicomponent.CRadioButton rdoRoi;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private com.see.truetransact.uicomponent.CTable tblDetails;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtCommId;
    private com.see.truetransact.uicomponent.CTextField txtFrom;
    private com.see.truetransact.uicomponent.CTextField txtPenal;
    private com.see.truetransact.uicomponent.CTextField txtTo;
    // End of variables declaration//GEN-END:variables
}
