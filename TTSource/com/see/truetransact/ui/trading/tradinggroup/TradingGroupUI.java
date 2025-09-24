/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingGroupUI.java
 *
 * Created on January 31, 2005, 2:59 PM
 */
package com.see.truetransact.ui.trading.tradinggroup;

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
import com.see.truetransact.serverexception.TransRollbackException;
import java.util.ResourceBundle;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import java.util.List;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;

public class TradingGroupUI extends CInternalFrame implements UIMandatoryField, Observer {

    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.tradinggroup.TradingGroupRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TradingGroupMRB objMandatoryRB = new TradingGroupMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private TradingGroupOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    private boolean tblGroupPress = false;
    boolean isFilled = false;
    private boolean updateMode = false;
    int updateTab = -1;

    /**
     * Creates new form TDSConfigUI
     */
    public TradingGroupUI() {
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
        ClientUtil.enableDisable(panGroup, false);
        setButtonEnableDisable();
        enableDisableBtn(false);
        setSizeTableData();
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
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
    }

    private void setSizeTableData() {
        tblGroup.getColumnModel().getColumn(0).setPreferredWidth(3);
        tblGroup.getColumnModel().getColumn(1).setPreferredWidth(30);
        tblGroup.getColumnModel().getColumn(2).setPreferredWidth(35);
        tblGroup.getColumnModel().getColumn(3).setPreferredWidth(30);
        tblGroup.getColumnModel().getColumn(4).setPreferredWidth(35);
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

    /**
     * This method sets the Maximum allowed lenght to the textfields *
     */
    private void setMaxLengths() {
        txtGroupId.setAllowAll(true);
        txtGroupName.setAllowAll(true);
        txtSubGroupId.setAllowAll(true);
        txtSubGroupName.setAllowAll(true);
    }

    /**
     * This method is to add this class as an Observer to an Observable *
     */
    private void setObservable() {
        try {
            observable = new TradingGroupOB();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try {
            tblGroup.setModel(observable.getTblGroup());
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
    public void updateOBFields() {
        observable.setTxtGroupID(CommonUtil.convertObjToStr(txtGroupId.getText()));
        observable.setTxtGroupName(CommonUtil.convertObjToStr(txtGroupName.getText()));
        observable.setTxtSubGroupID(CommonUtil.convertObjToStr(txtSubGroupId.getText()));
        observable.setTxtSubGroupName(CommonUtil.convertObjToStr(txtSubGroupName.getText()));
        if (chkActive.isSelected()) {
            observable.setChkActive("Y");
        } else {
            observable.setChkActive("N");
        }
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        txtGroupId.setText(CommonUtil.convertObjToStr(observable.getTxtGroupID()));
        txtGroupName.setText(CommonUtil.convertObjToStr(observable.getTxtGroupName()));
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
        if (currField.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingGroupEdit");
            new ViewAll(this, viewMap).show();
        } else if (currField.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingGroupView");
            new ViewAll(this, viewMap).show();
        } else if ((currField.equalsIgnoreCase("Delete"))) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingGroupDelete");
            new ViewAll(this, viewMap).show();
        }
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            setSizeTableData();
            txtGroupId.setEnabled(false);
            txtGroupName.setEnabled(true);
            btnGroupNew.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setButtonEnableDisable();
            observable.getData(hash);
            update();
            setSizeTableData();
            txtGroupId.setEnabled(false);
            txtGroupName.setEnabled(false);
            txtSubGroupId.setEnabled(false);
            txtSubGroupName.setEnabled(false);
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
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                        && observable.getProxyReturnMap().containsKey("GROUP_ID")) {
                    ClientUtil.showMessageWindow("GROUP ID : " + observable.getProxyReturnMap().get("GROUP_ID"));
                }
            }
        }
        btnCancelActionPerformed(null);
    }

    /* set the screen after the updation,insertion, deletion */
    private void settings() {
//        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panGroup, false);
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
        panGroup = new com.see.truetransact.uicomponent.CPanel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panGroupTable = new com.see.truetransact.uicomponent.CPanel();
        srpGroup = new com.see.truetransact.uicomponent.CScrollPane();
        tblGroup = new com.see.truetransact.uicomponent.CTable();
        panSubGroupName = new com.see.truetransact.uicomponent.CPanel();
        lblSubGroupId = new com.see.truetransact.uicomponent.CLabel();
        lblSubGroupName = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnGroupSave = new com.see.truetransact.uicomponent.CButton();
        btnGroupNew = new com.see.truetransact.uicomponent.CButton();
        btnGroupDelete = new com.see.truetransact.uicomponent.CButton();
        txtSubGroupId = new com.see.truetransact.uicomponent.CTextField();
        txtSubGroupName = new com.see.truetransact.uicomponent.CTextField();
        lblActive = new com.see.truetransact.uicomponent.CLabel();
        chkActive = new com.see.truetransact.uicomponent.CCheckBox();
        panGroupName = new com.see.truetransact.uicomponent.CPanel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        lblGroupID = new com.see.truetransact.uicomponent.CLabel();
        txtGroupId = new com.see.truetransact.uicomponent.CTextField();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
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
        setMinimumSize(new java.awt.Dimension(900, 500));
        setPreferredSize(new java.awt.Dimension(900, 500));

        panGroup.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGroup.setMinimumSize(new java.awt.Dimension(780, 400));
        panGroup.setPreferredSize(new java.awt.Dimension(780, 400));
        panGroup.setLayout(new java.awt.GridBagLayout());

        panMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMain.setMinimumSize(new java.awt.Dimension(750, 350));
        panMain.setPreferredSize(new java.awt.Dimension(750, 350));
        panMain.setLayout(new java.awt.GridBagLayout());

        panGroupTable.setMinimumSize(new java.awt.Dimension(320, 580));
        panGroupTable.setPreferredSize(new java.awt.Dimension(320, 580));
        panGroupTable.setLayout(new java.awt.GridBagLayout());

        srpGroup.setMinimumSize(new java.awt.Dimension(280, 200));
        srpGroup.setPreferredSize(new java.awt.Dimension(280, 200));

        tblGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblGroup.setMinimumSize(new java.awt.Dimension(250, 400));
        tblGroup.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGroupMousePressed(evt);
            }
        });
        srpGroup.setViewportView(tblGroup);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupTable.add(srpGroup, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panGroupTable, gridBagConstraints);

        panSubGroupName.setBorder(javax.swing.BorderFactory.createTitledBorder("Sub Group Name"));
        panSubGroupName.setMinimumSize(new java.awt.Dimension(250, 200));
        panSubGroupName.setPreferredSize(new java.awt.Dimension(250, 100));
        panSubGroupName.setLayout(new java.awt.GridBagLayout());

        lblSubGroupId.setText("Sub Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 2, 4);
        panSubGroupName.add(lblSubGroupId, gridBagConstraints);

        lblSubGroupName.setText("Sub Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubGroupName.add(lblSubGroupName, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnGroupSave.setText("Save");
        btnGroupSave.setEnabled(false);
        btnGroupSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnGroupSave, gridBagConstraints);

        btnGroupNew.setText("New");
        btnGroupNew.setEnabled(false);
        btnGroupNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnGroupNew, gridBagConstraints);

        btnGroupDelete.setText("Delete");
        btnGroupDelete.setEnabled(false);
        btnGroupDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnGroupDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(23, 0, 0, 0);
        panSubGroupName.add(panButton, gridBagConstraints);

        txtSubGroupId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 2, 4);
        panSubGroupName.add(txtSubGroupId, gridBagConstraints);

        txtSubGroupName.setMaxLength(128);
        txtSubGroupName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtSubGroupName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubGroupName.add(txtSubGroupName, gridBagConstraints);

        lblActive.setText("Active");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubGroupName.add(lblActive, gridBagConstraints);

        chkActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSubGroupName.add(chkActive, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panSubGroupName, gridBagConstraints);

        panGroupName.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Name"));
        panGroupName.setMinimumSize(new java.awt.Dimension(250, 100));
        panGroupName.setPreferredSize(new java.awt.Dimension(250, 100));
        panGroupName.setLayout(new java.awt.GridBagLayout());

        lblGroupName.setText("Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panGroupName.add(lblGroupName, gridBagConstraints);

        lblGroupID.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panGroupName.add(lblGroupID, gridBagConstraints);

        txtGroupId.setMinimumSize(new java.awt.Dimension(200, 21));
        txtGroupId.setPreferredSize(new java.awt.Dimension(200, 21));
        txtGroupId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panGroupName.add(txtGroupId, gridBagConstraints);

        txtGroupName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtGroupName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtGroupName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panGroupName.add(txtGroupName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panGroupName, gridBagConstraints);

        panGroup.add(panMain, new java.awt.GridBagConstraints());

        getContentPane().add(panGroup, java.awt.BorderLayout.CENTER);

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
        ClientUtil.enableDisable(panGroupName, false);
        ClientUtil.enableDisable(panSubGroupName, false);
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

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panGroup, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        enableDisableBtn(false);
        setSizeTableData();
        lblStatus.setText("");
        //setSizeTableData();

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (tblGroup.getRowCount() > 0) {
            savePerformed();
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("No records to Save");
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panGroupName, true);
        enableDisableBtn(false);
        btnGroupNew.setEnabled(true);
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        getCurrentGroupID();
        txtGroupId.setEnabled(false);
        //observable.setStatus();
        //lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnNewActionPerformed

    private void getCurrentGroupID() {
        HashMap groupIDMap = new HashMap();
        List groupLst = ClientUtil.executeQuery("getTradingGroupID", groupIDMap);
        if (groupLst != null && groupLst.size() > 0) {
            groupIDMap = (HashMap) groupLst.get(0);
            List getGroupList = ClientUtil.executeQuery("getCurrentIDBefore", groupIDMap);
            if (getGroupList != null && getGroupList.size() > 0) {
                groupIDMap = (HashMap) getGroupList.get(0);
                txtGroupId.setText(CommonUtil.convertObjToStr(groupIDMap.get("ID")));
            }
        }
    }

    private void tblGroupMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupMousePressed
        // TODO add your handling code here:
        if (tblGroup.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblGroup.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblGroup.getValueAt(tblGroup.getSelectedRow(), 0));
            String authStatus = CommonUtil.convertObjToStr(tblGroup.getValueAt(tblGroup.getSelectedRow(), 3));
            ClientUtil.enableDisable(panSubGroupName, true);
            observable.populateMemberTableDetails(st);
            updateSubGroup();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                enableDisableBtn(false);
                enableDisablePanSubGroupName(closable);
            } else {
                enableDisableBtn(true);
                btnGroupNew.setEnabled(false);
                txtSubGroupId.setEnabled(false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                enableDisablePanSubGroupName(false);
                enableDisableBtn(false);
                ClientUtil.enableDisable(panSubGroupName, false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (authStatus.equals("AUTHORIZED")) {
                    btnGroupDelete.setEnabled(false);
                    btnGroupSave.setEnabled(false);
                } else {
                    btnGroupDelete.setEnabled(true);
                    btnGroupSave.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_tblGroupMousePressed

    private void btnGroupSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupSaveActionPerformed
        // TODO add your handling code here:
        try {
            if (txtGroupName.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Group Name Should not be empty");
                return;
            } else if (txtSubGroupName.getText().length() <= 0) {
                ClientUtil.showMessageWindow("Sub Group Name Should not be empty");
                return;
            } else {
                updateOBFields();
                observable.saveSubGroup(updateTab, updateMode);
                tblGroup.setModel(observable.getTblGroup());
                observable.resetSubGroup();
                //observable.resetSubGroup();
                enableDisableBtn(false);
                btnGroupNew.setEnabled(true);
                chkActive.setSelected(false);
                enableDisablePanSubGroupName(false);
                updateSubGroup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnGroupSaveActionPerformed

    private void btnGroupNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        enableDisableBtn(false);
        txtSubGroupName.setText("");
        txtSubGroupId.setText("");
        btnGroupSave.setEnabled(true);
        observable.setNewData(true);
        txtSubGroupId.setEnabled(false);
        txtSubGroupName.setEnabled(true);
        chkActive.setEnabled(true);
        chkActive.setSelected(true);
    }//GEN-LAST:event_btnGroupNewActionPerformed

    private void btnGroupDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupDeleteActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblGroup.getValueAt(tblGroup.getSelectedRow(), 0));
        observable.deleteSubGroup(st, tblGroup.getSelectedRow());
        observable.resetSubGroup();
        txtSubGroupId.setText("");
        txtSubGroupName.setText("");
        txtSubGroupId.setEnabled(false);
        txtSubGroupName.setEnabled(false);
        enableDisableBtn(false);
        btnGroupNew.setEnabled(true);
    }//GEN-LAST:event_btnGroupDeleteActionPerformed

    private void txtGroupIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupIdFocusLost

    private void txtGroupNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupNameFocusLost
        // TODO add your handling code here:
        btnGroupNew.setEnabled(true);

    }//GEN-LAST:event_txtGroupNameFocusLost

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

    public void enableDisablePanSubGroupName(boolean flag) {
        ClientUtil.enableDisable(panSubGroupName, flag);

    }

    private void enableDisableBtn(boolean flag) {
        btnGroupNew.setEnabled(flag);
        btnGroupSave.setEnabled(flag);
        btnGroupDelete.setEnabled(flag);
    }

    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("GROUP_ID", txtGroupId.getText());
            singleAuthorizeMap.put("SUB_GROUP_ID", txtSubGroupId.getText());
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
            mapParam.put(CommonConstants.MAP_NAME, "getTradingGroupForAuthorize");
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

    private void updateSubGroup() {
        txtSubGroupId.setText(observable.getTxtSubGroupID());
        txtSubGroupName.setText(observable.getTxtSubGroupName());
        if (CommonUtil.convertObjToStr(observable.getChkActive()).equals("Y")) {
            chkActive.setSelected(true);
        } else {
            chkActive.setSelected(false);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGroupDelete;
    private com.see.truetransact.uicomponent.CButton btnGroupNew;
    private com.see.truetransact.uicomponent.CButton btnGroupSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CCheckBox chkActive;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblActive;
    private com.see.truetransact.uicomponent.CLabel lblGroupID;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
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
    private com.see.truetransact.uicomponent.CLabel lblSubGroupId;
    private com.see.truetransact.uicomponent.CLabel lblSubGroupName;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panGroup;
    private com.see.truetransact.uicomponent.CPanel panGroupName;
    private com.see.truetransact.uicomponent.CPanel panGroupTable;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubGroupName;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpGroup;
    private com.see.truetransact.uicomponent.CTable tblGroup;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CTextField txtGroupId;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtSubGroupId;
    private com.see.truetransact.uicomponent.CTextField txtSubGroupName;
    // End of variables declaration//GEN-END:variables
}
